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

import java.util.prefs.Preferences;
import org.apache.log4j.Logger;
import uk.ac.ebi.caf.utility.preference.AbstractPreference;


/**
 *
 *          StringPreference 2012.01.29
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 *
 *          Class description
 *
 */
public class StringPreference extends AbstractPreference<String> {

    private static final Logger LOGGER = Logger.getLogger(StringPreference.class);


    public StringPreference(String value,
                            Preferences scope,
                            String category,
                            String key,
                            String name,
                            String description) {
        super(value, scope, category, key, name, description);
    }


    public String get() {
        return getScope().get(getKey(), getDefault());
    }
    
    
    public void put(String value){
        getScope().put(getKey(), value);
    }
}
