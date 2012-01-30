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
package uk.ac.ebi.caf.utility.preference;

import java.util.prefs.Preferences;


/**
 *
 *          Preference 2012.01.29
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 *
 *          Class provides storage of preference metadata
 *
 */
public interface Preference<T> {

    /**
     * Access the value of the preference
     * @return 
     */
    public T get();


    public void put(T value);


    public T getDefault();


    public Preferences getScope();


    public String getName();


    public String getCategory();


    public String getDescription();
}
