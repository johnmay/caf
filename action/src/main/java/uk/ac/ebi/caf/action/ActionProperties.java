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

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.prefs.Preferences;


/**
 * Singleton class to load action properties from a resource file
 *
 * @author johnmay
 * @date Apr 13, 2011
 */
public class ActionProperties
        extends Properties {

    private static final Logger LOGGER = Logger.getLogger(ActionProperties.class);

    private final static String RESOURCE_NAME = Preferences.userNodeForPackage(ActionProperties.class).get("Action.Resource.Name", "action.properties");


    /**
     * Load the action properties for a given package (determined by class)
     *
     * @param location
     */
    private ActionProperties(Class location) {

        URL url = location.getResource(RESOURCE_NAME);

        if (url == null) {
            LOGGER.error("Could not open action properties for: "
                                 + location.getPackage() + " please make sure it exists", new Exception());
            return;
        }

        InputStream stream = null;
        try {
            stream = url.openStream();
            load(stream);
        } catch (IOException ex) {
            LOGGER.error("Could not open action properties for: "
                                 + location.getPackage(), ex);

        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException ex) {
                    LOGGER.error("Could not close action properties for: "
                                         + location.getPackage(), ex);
                }
            }
        }

    }


    public static ActionProperties getInstance(Class location) {
        return ActionPropertiesHolder.getInstance(location);
    }


    private static class ActionPropertiesHolder {

        private static Map<Package, ActionProperties> propertiesMap = new HashMap<Package, ActionProperties>();


        private static ActionProperties getInstance(Class location) {
            if (!propertiesMap.containsKey(location.getPackage())) {
                propertiesMap.put(location.getPackage(), new ActionProperties(location));
            }
            return propertiesMap.get(location.getPackage());
        }
    }
}
