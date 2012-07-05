/*
 * This file is part of Core Application Framework (CAF).
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
package uk.ac.ebi.caf.action;

import org.apache.log4j.Logger;
import sun.misc.CompoundEnumeration;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.Properties;


/**
 * Singleton class to load action properties from '.properties' files. These files
 * are currently located in the /uk/ac/ebi/caf/action.properties to simplify merging
 * when combining jars (originally the property file was located in the package of the
 * class which needed it but this proved troublesome to combined when creating a single
 * jar with dependencies).
 *
 * @author johnmay
 * @date Apr 13, 2011
 */
public class ActionProperties extends Properties {

    private static final Logger LOGGER = Logger.getLogger(ActionProperties.class);

    private final static String LOCATION = "META-INF/actions/action.properties";

    /**
     * Load the action properties for a given package
     *
     */
    private ActionProperties() {

        Enumeration<URL> resources = getResources(LOCATION);

        while (resources.hasMoreElements()) {

            load(resources.nextElement());

        }

    }

    /**
     * Loads the properties for the provided url. If a property for the given key already exists the
     * property will be overwritten.
     *
     * @param url resource location to load
     */
    private void load(URL url) {

        if (url == null) {
            LOGGER.error("Could not open action properties for: "
                                 + url.getPath() + " please make sure it exists", new Exception());
            return;
        }

        InputStream stream = null;
        try {
            stream = url.openStream();
            // load appends properties (and overwrite if key clash)
            load(stream);
        } catch (IOException ex) {
            LOGGER.error("Could not open action properties for: "
                                 + url.getPath(), ex);

        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException ex) {
                    LOGGER.error("Could not close action properties for: "
                                         + url.getPath(), ex);
                }
            }
        }

    }

    /**
     * Loads resources from the specified location, if no resources are found an empty collection is returned
     *
     * @param path resource location
     *
     * @return all resources for the given location
     */
    private Enumeration<URL> getResources(String path) {

        ClassLoader loader = Thread.currentThread().getContextClassLoader();

        try {
            return loader.getResources(path);
        } catch (IOException ex) {
            LOGGER.error("Unable to read " + LOCATION + " from class loader - low level I/O exception:", ex);
        }

        return new CompoundEnumeration<URL>(new Enumeration[0]);

    }


    /**
     * Singleton access
     *
     * @param location
     *
     * @return
     */
    public static ActionProperties getInstance() {
        return ActionPropertiesHolder.INSTANCE;
    }


    private static class ActionPropertiesHolder {
        private static final ActionProperties INSTANCE = new ActionProperties();
    }
}
