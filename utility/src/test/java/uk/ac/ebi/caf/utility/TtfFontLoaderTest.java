package uk.ac.ebi.caf.utility;

import org.junit.Test;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import java.awt.Font;
import java.lang.reflect.InvocationTargetException;

/**
 * @author John May
 */
public class TtfFontLoaderTest {

    @Test(expected = IllegalArgumentException.class)
    public void leadingSlash() {
        TtfFontLoader.load("/uk/ac/ebi/caf/utility/EBI-Chemistry.ttf");
    }

    @Test(expected = IllegalArgumentException.class)
    public void invalidPath() {
        TtfFontLoader.load("not a path!");
    }

    @Test(expected = NullPointerException.class)
    public void nullColor() {
        TtfFontLoader.load("uk/ac/ebi/caf/utility/EBI-Chemistry.ttf").create('U').color(null);
    }

    @Test public void demo() {
        TtfFontLoader.load("uk/ac/ebi/caf/utility/EBI-Chemistry.ttf").create('U').icon();
    }
}
