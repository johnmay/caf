package uk.ac.ebi.caf.utility;

import org.junit.Test;
import uk.ac.ebi.caf.utility.font.EBIFont;
import uk.ac.ebi.caf.utility.font.EBIIcon;

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

    public static void main(String[] args) {

        JFrame frame = new JFrame();
        frame.add(new JLabel(EBIIcon.REFRESH.create().size(22f).flipHorizontal().highlight().icon()));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
