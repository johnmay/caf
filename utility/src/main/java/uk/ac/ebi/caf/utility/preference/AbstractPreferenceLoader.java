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
package uk.ac.ebi.caf.utility.preference;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import java.io.File;
import java.io.IOException;
import uk.ac.ebi.caf.utility.preference.type.StringPreference;
import java.io.InputStream;
import java.lang.String;
import java.util.*;
import java.util.prefs.Preferences;
import javax.swing.tree.DefaultMutableTreeNode;
import org.apache.log4j.Logger;
import uk.ac.ebi.caf.utility.preference.type.*;


/**
 *
 *          PreferenceManagement 2012.01.29
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 *
 *          Class provides management of preferences loaded from a config 
 *
 */
public abstract class AbstractPreferenceLoader extends Properties {

    private static final Logger LOGGER = Logger.getLogger(AbstractPreferenceLoader.class);

    private Map<String, Preference> preferenceMap = new HashMap<String, Preference>(40);

    private static final String KEY_JOIN = ".";

    private static final String TYPE = "Type";

    private static final String CATEGORY = "Category";

    private static final String SCOPE = "Scope";

    private static final String NAME = "Name";

    private static final String DESCRIPTION = "Description";

    private static final String DEFAULT = "Default";


    public AbstractPreferenceLoader() {
        try {
            load();
        } catch (IOException ex) {
            LOGGER.error("Could not load preference file");
        }
    }


    public final Set<String> getPreferenceKeys() {

        Set<String> keys = new HashSet<String>();

        for (Object key : keySet()) {

            System.out.println("key:" + key);

            if (key instanceof String) {
                String keyString = (String) key;
                keys.add(keyString.substring(0, keyString.indexOf(KEY_JOIN)));
            }
        }

        return keys;

    }


    public static Multimap<String, Preference> getCategoryMap(AbstractPreferenceLoader... loaders) {

        List<AbstractPreferenceLoader> loaderList = Arrays.asList(loaders);
        Iterator<AbstractPreferenceLoader> loaderIterator = loaderList.iterator();

        Multimap<String, Preference> map = loaderIterator.next().getCategoryMap();

        while (loaderIterator.hasNext()) {
            map.putAll(loaderIterator.next().getCategoryMap());
        }

        return map;

    }


    public final Multimap<String, Preference> getCategoryMap() {

        Multimap<String, Preference> map = HashMultimap.create();

        for (String key : getPreferenceKeys()) {
            Preference pref = getPreference(key);
            map.put(pref.getCategory(), pref);
        }

        return map;
    }


    public final void load() throws IOException {

        InputStream in = getConfig();
        load(in);
        in.close();

    }


    public <Pref extends AbstractPreference> Pref getPreference(String key) {
        if (!preferenceMap.containsKey(key)) {
            preferenceMap.put(key, createNew(key));
        }
        return (Pref) preferenceMap.get(key);
    }


    public AbstractPreference createNew(String key) {

        String type = getProperty(key + KEY_JOIN + TYPE);
        String scopeType = getProperty(key + KEY_JOIN + SCOPE);

        if (type == null) {
            LOGGER.fatal("Unable to load preference for key:" + key);
            throw new UnsupportedOperationException("Unable to load preference for key:" + key);
        }

        Preferences scope = scopeType.equals("User") ? getUserApplicationNode() : getSystemApplicationNode();

        if (type.equals("String")) {

            String value = getProperty(key + KEY_JOIN + DEFAULT);

            return new StringPreference(value, scope,
                                        getProperty(key + KEY_JOIN + CATEGORY),
                                        key,
                                        getProperty(key + KEY_JOIN + NAME),
                                        getProperty(key + KEY_JOIN + DESCRIPTION));
        }

        if (type.equals("Incremental")) {

            Long value = Long.parseLong(getProperty(key + KEY_JOIN + DEFAULT));

            return new IncrementalPreference(value,
                                             scope,
                                             getProperty(key + KEY_JOIN + CATEGORY),
                                             key,
                                             getProperty(key + KEY_JOIN + NAME),
                                             getProperty(key + KEY_JOIN + DESCRIPTION));
        }

        if (type.equals("Integer")) {

            Integer value = Integer.parseInt(getProperty(key + KEY_JOIN + DEFAULT));

            return new IntegerPreference(value,
                                         scope,
                                         getProperty(key + KEY_JOIN + CATEGORY),
                                         key,
                                         getProperty(key + KEY_JOIN + NAME),
                                         getProperty(key + KEY_JOIN + DESCRIPTION));
        }
        if (type.equals("Double")) {

            Double value = Double.parseDouble(getProperty(key + KEY_JOIN + DEFAULT));

            return new DoublePreference(value,
                                        scope,
                                        getProperty(key + KEY_JOIN + CATEGORY),
                                        key,
                                        getProperty(key + KEY_JOIN + NAME),
                                        getProperty(key + KEY_JOIN + DESCRIPTION));
        }

        if (type.equals("List")) {

            List<String> value = ListPreference.unwrap(getProperty(key + KEY_JOIN + DEFAULT));

            return new ListPreference(value,
                                      scope,
                                      getProperty(key + KEY_JOIN + CATEGORY),
                                      key,
                                      getProperty(key + KEY_JOIN + NAME),
                                      getProperty(key + KEY_JOIN + DESCRIPTION));
        }
        if (type.equals("File")) {

            File value = new File(getProperty(key + KEY_JOIN + DEFAULT));

            return new FilePreference(value,
                                      scope,
                                      getProperty(key + KEY_JOIN + CATEGORY),
                                      key,
                                      getProperty(key + KEY_JOIN + NAME),
                                      getProperty(key + KEY_JOIN + DESCRIPTION));
        }

        throw new UnsupportedOperationException("Unsupported preference type for key:" + key);
    }


    /**
     * Provides the configuration properties file input stream.
     * @return 
     */
    public abstract InputStream getConfig() throws IOException;


    /**
     * Provides the {@see Preferences} node for user. This should be
     * provided on a per application/library basis and would normally
     * be the root for the packages (e.g. org.openscience.cdk).
     * 
     * <pre>
     *      Preferences.userNodeForPackage(AtomContainer.class);
     * </pre>
     * 
     * By default this provides the node for the package the loader resides
     * in
     * 
     * @return 
     */
    public Preferences getUserApplicationNode() {
        return Preferences.userNodeForPackage(getClass());
    }


    /**
     * Provides the {@see Preferences} node for system. This should be
     * provided on a per application/library basis and would normally
     * be the root for the packages (e.g. org.openscience.cdk).
     * 
     * By default this provides the node for the package the loader resides
     * in

     * 
     */
    public Preferences getSystemApplicationNode() {
        return Preferences.systemNodeForPackage(getClass());
    }
}
