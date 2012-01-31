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

import java.util.prefs.Preferences;
import org.apache.log4j.Logger;


/**
 *
 *          AbstractPreference 2012.01.29
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 *
 *          Class description
 *
 */
public abstract class AbstractPreference<T> implements Preference<T> {

    private static final Logger LOGGER = Logger.getLogger(AbstractPreference.class);

    private T value;

    private Preferences scope;

    private String category;

    private String key;

    private String name;

    private String description;


    protected AbstractPreference(T value,
                                 Preferences scope,
                                 String category,
                                 String key,
                                 String name,
                                 String description) {
        this.value = value;
        this.scope = scope;
        this.category = category;
        this.key = key;
        this.name = name;
        this.description = description;
    }


    public T getDefault() {
        return value;
    }


    public Preferences getScope() {
        return scope;
    }


    public String getName() {
        return name;
    }


    public String getCategory() {
        return category;
    }


    public String getDescription() {
        return description;
    }


    public String getKey() {
        return key;
    }
}
