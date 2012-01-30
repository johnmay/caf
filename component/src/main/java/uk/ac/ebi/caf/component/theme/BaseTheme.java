/**
 * BaseTheme.java
 *
 * 2012.01.25
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

import java.awt.Color;
import java.awt.Font;
import org.apache.log4j.Logger;
import uk.ac.ebi.caf.utility.preference.type.IntegerPreference;
import uk.ac.ebi.caf.utility.preference.type.StringPreference;


/**
 *
 *          BaseTheme 2012.01.25
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 *
 *          Class description
 *
 */
public abstract class BaseTheme implements Theme {

    private static final Logger LOGGER = Logger.getLogger(BaseTheme.class);

    private static final ComponentPreferences PREFERENCES = ComponentPreferences.getInstance();

    public final Font BODY_FONT;

    public final Font HEADER_FONT;

    public final Font LINK_FONT;

    private final Color DIALOG_BACKGROUND = new Color(237, 237, 237);


    public BaseTheme() {

        StringPreference bodyfontfamily = PREFERENCES.getPreference("BODY_FONT_FAMILY");
        IntegerPreference bodyfontsize = PREFERENCES.getPreference("BODY_FONT_SIZE");
        StringPreference headerfontfamily = PREFERENCES.getPreference("HEADER_FONT_FAMILY");
        IntegerPreference headerfontsize = PREFERENCES.getPreference("HEADER_FONT_SIZE");
        StringPreference linkfontfamily = PREFERENCES.getPreference("LINK_FONT_FAMILY");
        IntegerPreference linkfontsize = PREFERENCES.getPreference("LINK_FONT_SIZE");


        BODY_FONT = new Font(bodyfontfamily.get(), Font.PLAIN, bodyfontsize.get());
        HEADER_FONT = new Font(headerfontfamily.get(), Font.PLAIN, bodyfontsize.get());
        LINK_FONT = new Font(linkfontfamily.get(), Font.PLAIN, linkfontsize.get());

    }


    public Font getBodyFont() {
        return BODY_FONT;
    }


    public Font getHeaderFont() {
        return HEADER_FONT;
    }


    public Font getLinkFont() {
        return LINK_FONT;
    }


    public Color getDialogBackground() {
        return DIALOG_BACKGROUND;
    }


    public float getDialogOpacity() {
        return 0.95f;
    }
}
