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

import com.jgoodies.forms.layout.FormLayout;
import java.awt.Color;
import javax.swing.JPanel;
import org.apache.log4j.Logger;

/**
 *          PanelFactory - 2011.11.07 <br>
 *          A class to manage JPanel generation
 * @version $Rev: 1007 $ : Last Changed $Date: 2011-12-13 15:21:02 +0000 (Tue, 13 Dec 2011) $
 * @author  johnmay
 * @author  $Author: johnmay $ (this version)
 */
public class PanelFactory {

    private static final Logger LOGGER = Logger.getLogger(PanelFactory.class);
    private static final Color DIALOG_BACKGROUND = new Color(237, 237, 237);

    public static JPanel create() {
        return new JPanel();
    }

    /**
     * Creates a panel the dialog background (Light Gray-ish)
     * @return
     */
    public static JPanel createDialogPanel() {
        JPanel panel = create();
        panel.setBackground(DIALOG_BACKGROUND);
        return panel;
    }

    /**
     * Creates a JGoodies FormLayout panel using the provided encoding spec
     * @param encodedColumnSpec
     * @param encodedRowSpec
     * @return
     */
    public static JPanel createDialogPanel(String encodedColumnSpec, String encodedRowSpec) {
        JPanel panel = createDialogPanel();
        panel.setLayout(new FormLayout(encodedColumnSpec, encodedRowSpec));
        return panel;
    }

    /**
     * Creates a white panel for display info cleanly
     */
    public static JPanel createInfoPanel() {
        JPanel panel = create();
        panel.setBackground(Color.WHITE);
        return panel;
    }

    /**
     * Creates a JGoodies FormLayout panel using the provided encoding spec
     * @param encodedColumnSpec
     * @param encodedRowSpec
     * @return
     */
    public static JPanel createInfoPanel(String encodedColumnSpec, String encodedRowSpec) {
        JPanel panel = createInfoPanel();
        panel.setLayout(new FormLayout(encodedColumnSpec, encodedRowSpec));
        return panel;
    }
}
