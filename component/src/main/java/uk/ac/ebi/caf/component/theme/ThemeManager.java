/**
 * ThemeManager.java
 *
 * 2012.01.25
 *
 * This file is part of the CheMet library
 * 
 * The CheMet library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * CheMet is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with CheMet.  If not, see <http://www.gnu.org/licenses/>.
 */
package uk.ac.ebi.caf.component.theme;

import java.util.HashMap;
import java.util.Map;
import java.util.prefs.Preferences;
import uk.ac.ebi.caf.utility.preference.type.StringPreference;


/**
 *
 *          ThemeManager 2012.01.25
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 *
 *          Singleton description
 *
 */
public class ThemeManager {

    private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(ThemeManager.class);

    private Map<String, Theme> themeMap = new HashMap<String, Theme>();

    private Theme[] themes = new Theme[]{new DefaultTheme(),
                                         new DarkTheme()};


    private ThemeManager() {

        for (Theme theme : themes) {
            themeMap.put(theme.getClass().getSimpleName(), theme);
        }
    }


    public static ThemeManager getInstance() {
        return ThemeManagerHolder.INSTANCE;
    }


    private static class ThemeManagerHolder {

        private static final ThemeManager INSTANCE = new ThemeManager();
    }


    public Theme getTheme() {
        
        ComponentPreferences PREFERENCES = ComponentPreferences.getInstance();        
        StringPreference currentThemePref = PREFERENCES.getPreference("CURRENT_THEME");
        String name = currentThemePref.get();
        
        if (!themeMap.containsKey(name)) {
            logger.error("Theme " + name + " was not found");
            return new DefaultTheme();
        }

        return themeMap.get(name);

    }
}
