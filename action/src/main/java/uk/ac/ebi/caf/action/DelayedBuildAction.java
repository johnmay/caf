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

import java.awt.event.ActionEvent;


/**
 * DelayedBuildAction.java
 *
 *
 * @author johnmay
 * @date Apr 14, 2011
 */
public abstract class DelayedBuildAction extends GeneralAction {

    private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(DelayedBuildAction.class);

    private boolean built = false;


    public DelayedBuildAction(Class clazz, String command) {
        super(clazz, command);
    }


    public DelayedBuildAction(String command) {
        super(command);
    }


    public abstract void buildComponents();


    public abstract void activateActions();


    public void actionPerformed(ActionEvent e) {
        if (built == false) {
            buildComponents();
            built = true;
        }
        activateActions();
    }
}
