package uk.ac.ebi.caf.utility;

import uk.ac.ebi.caf.utility.font.IconBuilder;

import javax.swing.ImageIcon;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * TrueType font (ttf) loader providing utility to create images and icons.
 * <blockquote><pre>
 *     TtfFontLoader loader = TtfFontLoader.load("uk/ac/ebi/caf/utility/EBI-Chemistry.ttf");
 *
 *     ImageIcon     icon = loader.create('U').icon();
 *     BufferedImage img  = loader.create('U').image();
 *
 *     ImageIcon     icon = loader.create('U')
 *                                .size(32f)
 *                                .color(Color.RED)
 *                                .icon();
 * </pre></blockquote>
 *
 * @author John May
 */
public final class TtfFontLoader {

    private final Font font;

    private TtfFontLoader(InputStream in) throws IOException,
                                                 FontFormatException {
        this.font = Font.createFont(Font.TRUETYPE_FONT, in).deriveFont(16f);
    }

    /**
     * Access the raw loaded font.
     *
     * @return the font
     */
    public Font raw() {
        return this.font;
    }

    /**
     * Create an icon builder for the <i>codepoint</i>.
     *
     * <blockquote><pre>
     *     TtfFontLoader loader = TtfFontLoader.load(...);
     *
     *     ImageIcon     icon = loader.create('U').icon();
     *     BufferedImage img  = loader.create('U').image();
     *
     *     ImageIcon     icon = loader.create('U')
     *                                .size(32f)
     *                                .color(Color.RED)
     *                                .icon();
     * </pre></blockquote>
     *
     * @param codepoint the code point to build an icon for
     * @return a new icon builder
     */
    public IconBuilder create(char codepoint) {
        return new IconBuilder(font, codepoint);
    }


    /**
     * Load a ttf from an absolute classpath. The method wraps the io handling
     * and rethrows any exceptions as illegal arguments (not meant for unknown
     * input).
     *
     * @param path path to the ttf resource
     * @return usable instance
     * @throws IllegalArgumentException thrown if an IOException or
     *                                  FontFormatException occurred
     */
    public static TtfFontLoader load(String path) {
        checkArgument(!path
                .startsWith("/"), "path should not start with a leading slash");
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        InputStream in = loader.getResourceAsStream(path);
        try {
            return new TtfFontLoader(in);
        } catch (IOException ex) {
            throw new IllegalArgumentException(ex);
        } catch (FontFormatException ex) {
            throw new IllegalArgumentException(ex);
        } finally {
            try {
                if (in != null)
                    in.close();
            } catch (IOException ex) {
                // can't do anything
            }
        }
    }

}
