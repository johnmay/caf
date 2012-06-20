/*
 * This file is part of Creative Application Framework (CAF).
 *
 * The Core Application Framework is free software: you can redistribute it
 * and/or modify it under the terms of the GNU Lesser General Public License
 * as published  by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * The Core Application Framework is distributed in the hope that it
 * will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Core Application Framework.  If not, 
 * see <http://www.gnu.org/licenses/>.
 * 
 */
package uk.ac.ebi.caf.utility.preference.type;

import org.apache.log4j.Logger;
import uk.ac.ebi.caf.utility.preference.AbstractPreference;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.prefs.Preferences;


/**
 * FilePreference 2012.01.29
 *
 * @author johnmay
 * @author $Author$ (this version)
 *         <p/>
 *         Class description
 * @version $Rev$ : Last Changed $Date$
 */
public class FilePreference extends AbstractPreference<File> {

    private static final Logger LOGGER = Logger.getLogger(FilePreference.class);

    public static final File   OS_APP_DATA_FILE = getOSAppDataRoot();
    public static final String OS_APP_DATA_PATH = OS_APP_DATA_FILE.getAbsolutePath().replaceAll("\\\\", "\\\\\\\\");

    public FilePreference(File value,
                          Preferences scope,
                          String category,
                          String key,
                          String name,
                          String description) {
        super(value, scope, category, key, name, description);
    }


    /**
     * Access the location of the file. Special use:
     * <p/>
     * <pre>{@code
     *  <os.app.data> is replaced with the OS Dependant application data folder
     *  <user.home> is replaced with the users home directory
     * <p/>
     * }</pre>
     *
     * @return
     */
    public File get() {

        String path = getScope().get(getKey(), "");

        String value = path.isEmpty() ? getDefault().getPath() : path;

        value = value.replaceAll("<os.app.data>", OS_APP_DATA_PATH);
        value = value.replaceAll("<user.home>", System.getProperty("user.home"));

        return new File(value);

    }


    public void put(File value) {
        getScope().put(getKey(), value.getPath());
    }

    /**
     * Access the AppData root dependant or os
     * <p/>
     * Win -> %AppData%
     * OSX -> ~/Library/Application Support/
     * Unix -> ~/.appdata
     * Other -> user.dir
     *
     * @return
     */
    private static File getOSAppDataRoot() {

        String OS = System.getProperty("os.name").toUpperCase();

        if (OS.contains("WIN")) {
            // make it easier to handle from within java
            return new File(System.getenv("APPDATA"));
        } else if (OS.contains("MAC")) {
            return new File(System.getProperty("user.home")
                                    + File.separator + "Library"
                                    + File.separator + "Application Support");
        } else if (OS.contains("NUX")) {

            // would be good to write an program specific name
            File appdata = new File(System.getProperty("user.home")
                                            + File.separator + ".appdata");

            File readme = new File(appdata, "README");

            // create an place a README
            if (!readme.exists()) {
                if (appdata.mkdirs()) {
                    FileWriter writer = null;

                    try {
                        writer = new FileWriter(readme);
                        writer.write(".appdata is the default folder used by the creative application" +
                                             " framework http://www.github.com/johnmay/creative-application-framework");
                        writer.write("The creative application framework is currently used by MDK and Metingear but" +
                                             "may be useed by other applications in future.");
                    } catch (IOException e) {
                        LOGGER.error("Could not create README file for ~/.appdata");
                    } finally {
                        try {
                            if (writer != null) {
                                writer.close();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                        }
                    }


                }
            }

            return appdata;

        }

        return new File(System.getProperty("user.dir"));

    }


}
