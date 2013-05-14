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

import javax.swing.JTextField;
import org.apache.log4j.Logger;
import uk.ac.ebi.caf.component.theme.Theme;
import uk.ac.ebi.caf.component.theme.ThemeManager;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;


/**
 *          FieldFactory - 2011.11.17 <br>
 *          Class description
 * @version $Rev: 1007 $ : Last Changed $Date: 2011-12-13 15:21:02 +0000 (Tue, 13 Dec 2011) $
 * @author  johnmay
 * @author  $Author: johnmay $ (this version)
 */
public class FieldFactory {

    private static final Logger LOGGER = Logger.getLogger(FieldFactory.class);

    private static Theme theme = ThemeManager.getInstance().getTheme();


    public static JTextField newField(int columns) {
        final JTextField field = new JTextField();
        field.setFont(theme.getBodyFont());
        field.setForeground(theme.getForeground());
        field.setColumns(columns);
        field.addFocusListener(new FocusAdapter() {
            @Override public void focusGained(FocusEvent e) {
                field.setSelectionStart(field.getSelectionEnd());
            }
        });
        return field;
    }


    public static JTextField newField(String text) {
        final JTextField field = new JTextField(text);
        field.setFont(theme.getBodyFont());
        field.setForeground(theme.getForeground());
        field.addFocusListener(new FocusAdapter() {
            @Override public void focusGained(FocusEvent e) {
                field.setSelectionStart(field.getSelectionEnd());
            }
        });
        return field;
    }


    public static JTextField newTransparentField(int columns) {
        JTextField field = new JTextField(columns);
        field.setFont(theme.getBodyFont());
        field.setForeground(theme.getForeground());
        field.setBackground(null);
        field.setBorder(null);
        return field;
    }


    public static JTextField newTransparentField(int columns, boolean editable) {
        JTextField field = newTransparentField(columns);
        field.setEditable(editable);
        return field;
    }
}
