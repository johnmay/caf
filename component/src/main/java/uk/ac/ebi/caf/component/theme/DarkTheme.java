/**
 * DefaultTheme.java
 *
 * 2011.09.30
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
package uk.ac.ebi.caf.component.theme;

import javax.swing.Icon;
import java.awt.Color;
import java.awt.Font;
import org.apache.log4j.Logger;


/**
 *          DefaultTheme – 2011.09.30 <br>
 *          Class description
 * @version $Rev: 1000 $ : Last Changed $Date: 2011-12-13 08:59:08 +0000 (Tue, 13 Dec 2011) $
 * @author  johnmay
 * @author  $Author: johnmay $ (this version)
 */
public class DarkTheme
        extends BaseTheme
        implements Theme {

    private static final Logger LOGGER = Logger.getLogger(DarkTheme.class);

    private Color bg = new Color(64, 64, 64);


    public Color getWarningForeground() {
        return Color.YELLOW;
    }


    public Color getForeground() {
        return Color.WHITE;
    }


    public Color getBackground() {
        return bg;
    }


    public Color getAltForeground() {
        return Color.DARK_GRAY;
    }





    public Color getEmphasisedForeground() {
        throw new UnsupportedOperationException("Not supported yet.");
    }


    



}
