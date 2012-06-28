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

import javax.swing.*;
import java.awt.*;


/**
 * GeneralAction.java
 *
 * @author johnmay
 * @date Apr 8, 2011
 */
public abstract class GeneralAction extends AbstractAction {

    private static final org.apache.log4j.Logger LOGGER = org.apache.log4j.Logger.getLogger(GeneralAction.class);

    private ActionProperties actionProperties;

    private String command;

    public static final String PROJECT_REQUIRMENTS = "ProjectRequirements";

    public static final String EXPAND_BUTTON_OPEN_ICON = "OpenIcon";

    public static final String EXPAND_BUTTON_CLOSE_ICON = "CloseIcon";

    private String[] actionValues = new String[]{
            Action.NAME,
            Action.SHORT_DESCRIPTION,
            Action.ACCELERATOR_KEY,
            Action.LARGE_ICON_KEY,
            GeneralAction.PROJECT_REQUIRMENTS,
            EXPAND_BUTTON_OPEN_ICON,
            EXPAND_BUTTON_CLOSE_ICON
            // old use two different buttons and switch between them with setVisible
    };


    public GeneralAction(Class clazz, String command) {
        this.command = command;
        this.actionProperties = ActionProperties.getInstance(clazz);


        for (String actionValue : actionValues) {
            String action = actionProperties.getProperty(command + ".Action." + actionValue);
            setLoadedValue(actionValue, action);
        }
    }


    /**
     * Constructor loads the properties from action.properties (ActionProperties)
     * given the command name
     *
     * @param command
     */
    public GeneralAction(String command) {

        this.command = command;

        if (command != null && !command.isEmpty()) {

        this.actionProperties = ActionProperties.getInstance(getClass());


            for (String actionValue : actionValues) {
                String value = actionProperties.getProperty(command + ".Action." + actionValue);
                setLoadedValue(actionValue, value);
            }
        }
    }


    /**
     * Set the loaded values of the key. If the key is ACCELERATOR_KEY then the
     * newValue is automatically cast to a KeyStroke.
     *
     * @param key   Action.NAME, Action.SHORT_DESCRIPTION etc...
     * @param value
     */
    private void setLoadedValue(String key, String value) {

        if (key == null || value == null) {
            return;
        }

        if (key.equals(GeneralAction.PROJECT_REQUIRMENTS)) {
            putValue(key, value);
        } else if (key.equals(Action.LARGE_ICON_KEY)) {
            putValue(key, getIcon(value));
        } else if (key.equals(Action.ACCELERATOR_KEY)) {
            putValue(key, resolveKeystroke(value));
        } else {
            putValue(key, value);
        }

    }

    public static KeyStroke resolveKeystroke(String value) {
        if (value.contains("<mask>")) {
            KeyStroke stroke = KeyStroke.getKeyStroke(value.replaceAll("\\<mask\\>", ""));
            return KeyStroke.getKeyStroke(stroke.getKeyCode(), stroke.getModifiers() + Toolkit.getDefaultToolkit().getMenuShortcutKeyMask());
        }
        return KeyStroke.getKeyStroke(value);
    }

    private ImageIcon getIcon(String path) {

        java.net.URL imageURL = getClass().getResource(path);

        if (imageURL != null) {
            return new ImageIcon(imageURL);
        } else {
            LOGGER.error("Couldn't find file: " + path);
            return new ImageIcon(new byte[0], "Unloaded image: " + path);
        }
    }


    public String getName() {
        return actionProperties.getProperty(command + ".Action.Name");
    }
}
