/**
 * FieldFactory.java
 *
 * 2011.11.17
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
package uk.ac.ebi.caf.component.factory;

import java.util.Collection;
import javax.swing.JComboBox;
import org.apache.log4j.Logger;
import uk.ac.ebi.caf.component.theme.Theme;
import uk.ac.ebi.caf.component.theme.ThemeManager;


/**
 *          FieldFactory - 2011.11.17 <br>
 *          Class description
 * @version $Rev: 1007 $ : Last Changed $Date: 2011-12-13 15:21:02 +0000 (Tue, 13 Dec 2011) $
 * @author  johnmay
 * @author  $Author: johnmay $ (this version)
 */
public class ComboBoxFactory {

    private static final Logger LOGGER = Logger.getLogger(ComboBoxFactory.class);

    private static Theme theme = ThemeManager.getInstance().getTheme();


    public static JComboBox newComboBox(Object... items) {
        if (items.length == 1 && items[0] instanceof Collection) {
            items = ((Collection) items[0]).toArray(new Object[0]);
        }
        JComboBox combobox = new JComboBox(items);
        combobox.setFont(theme.getBodyFont());
        combobox.setForeground(theme.getForeground());
        return combobox;
    }
}
