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
package uk.ac.ebi.caf.utility;

import java.net.URL;
import javax.swing.ImageIcon;
import org.apache.log4j.Logger;


/**
 * @name    ColorUtilities - 2011.10.11 <br>
 *          Class description
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
public class ResourceUtility {

    private static final Logger LOGGER = Logger.getLogger(ResourceUtility.class);


    public static final ImageIcon getIcon(Class root, String path) {
        java.net.URL imageURL = root.getResource(path);

        if (imageURL != null) {
            return new ImageIcon(imageURL);
        } else {
            LOGGER.error("Couldn't find file: " + path);
            return new ImageIcon(new byte[0], path);
        }
    }


    public static final ImageIcon getIcon(String path) {
        java.net.URL imageURL = ResourceUtility.class.getResource(path);

        if (imageURL != null) {
            return new ImageIcon(imageURL);
        } else {
            LOGGER.error("Couldn't find file: " + path);
            return new ImageIcon(new byte[0], path);
        }
    }
}
