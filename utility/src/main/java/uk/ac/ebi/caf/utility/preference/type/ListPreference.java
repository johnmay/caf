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
package uk.ac.ebi.caf.utility.preference.type;

import java.util.ArrayList;
import java.util.List;
import java.util.prefs.Preferences;
import org.apache.log4j.Logger;
import uk.ac.ebi.caf.utility.preference.AbstractPreference;


/**
 *
 *          ListPreference 2012.01.29
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 *
 *          Class description
 *
 */
public class ListPreference extends AbstractPreference<List<String>> {

    private static final Logger LOGGER = Logger.getLogger(ListPreference.class);

    public static final String LIST_SEPARATOR_SPLIT = "(?<!\\\\);";

    public static final String LIST_SEPARATOR = ";";

    public static final String LIST_SEPARATOR_ESCAPED = "\\\\;";


    public ListPreference(List<String> value,
                          Preferences scope,
                          String category,
                          String key,
                          String name,
                          String description) {
        super(value, scope, category, key, name, description);
    }


    public static List<String> unwrap(String value) {

        List<String> values = new ArrayList<String>();

        for (String item : value.split(LIST_SEPARATOR_SPLIT)) {
            values.add(item.replaceAll(LIST_SEPARATOR_ESCAPED, LIST_SEPARATOR));
        }

        return values;
    }


    public List<String> get() {

        List<String> values = unwrap(getScope().get(getKey(), ""));

        return values.isEmpty() ? getDefault() : values;

    }


    public void put(List<String> values) {

        StringBuilder sb = new StringBuilder(values.size() * 14);

        for (String item : values) {
            sb.append(item.replaceAll(LIST_SEPARATOR, LIST_SEPARATOR_ESCAPED));
            if (!item.equals(values.get(values.size() - 1))) {
                sb.append(LIST_SEPARATOR);
            }
        }

        getScope().put(getKey(), sb.toString());
    }
}
