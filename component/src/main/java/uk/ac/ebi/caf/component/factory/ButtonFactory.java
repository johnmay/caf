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
package uk.ac.ebi.caf.component.factory;

import com.jgoodies.forms.factories.Borders;
import uk.ac.ebi.caf.component.theme.ThemeManager;

import javax.swing.*;
import java.awt.*;


/**
 * ButtonFactory - 2011.12.12 <br> Provides centralized instantiation of various type of buttons
 *
 * @author johnmay
 * @author $Author: johnmay $ (this version)
 * @version $Rev: 1009 $ : Last Changed $Date: 2011-12-13 16:36:52 +0000 (Tue, 13 Dec 2011) $
 */
public final class ButtonFactory {

    /**
     * Creates a normal button with the provided action. Note that in most cases the action provides the
     * text/images/description for the button. These can be easily specified using GeneralAction and ActionProperties
     * files
     *
     * @param action
     * @return new button instance
     */
    public static JButton newButton(Action action) {
        return new JButton(action);
    }


    /**
     * Creates a button with the associated action and specified text
     *
     * @param text   title to display on button
     * @param action the action to perform
     * @return new button instance
     */
    public static JButton newButton(String text,
                                    Action action) {
        JButton button = newButton(action);
        button.setText(text);
        return button;
    }


    /**
     * Creates a 'clean' button with null background color and an empty border. This is useful for buttons that will
     * only use an image and no text.
     *
     * @param action the action to perform
     * @return new button instance
     */
    public static JButton newCleanButton(Action action) {
        return newCleanButton(action, SwingConstants.CENTER);
    }


    public static JButton newCleanButton(Action action, int alignment) {
        return newCleanButton(action, CLEAR, alignment);
    }

    public static JButton newCleanButton(Action action, Color background) {
        return newCleanButton(action, background, SwingConstants.CENTER);
    }

    public static JButton newCleanButton(Action action, Color background, int alignment) {
        JButton button = newButton(action);
        button.setFont(ThemeManager.getInstance().getTheme().getBodyFont());
        button.setHorizontalAlignment(alignment);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setBorder(Borders.EMPTY_BORDER);
        button.setBackground(background);
        return button;
    }

    private static Color CLEAR = new Color(255, 255, 255, 5);


    /**
     * Creates a 'clean' button with null background color and an empty border. This is useful for buttons that will
     * only use an image and no text.
     *
     * @param icon   the icon to place on the button
     * @param action the action to perform
     * @return new button instance
     */
    public static JButton newCleanButton(Icon icon, Action action) {
        JButton button = newCleanButton(action);
        button.setIcon(icon);
        return button;
    }

    /**
     * Creates a 'clean' button with null background color and an empty border. This is useful for buttons that will
     * only use an image and no text but with a tooltip
     *
     * @param icon   the icon to place on the button
     * @param action the action to perform
     * @return new button instance
     */
    public static JButton newCleanButton(Icon icon, Action action, String tooltip) {
        JButton button = newCleanButton(action);
        button.setIcon(icon);
        button.setToolTipText(tooltip);
        return button;
    }

    public static JButton newCleanButton(Icon icon, Action action, Color background) {
        JButton button = newCleanButton(action, background);
        button.setIcon(icon);
        return button;
    }
}
