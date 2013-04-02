package uk.ac.ebi.caf.utility;

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

    private static BufferedImage create(char codepoint, Font font, Color color) {
        String text = Character.toString(codepoint);
        FontRenderContext context = new FontRenderContext(new AffineTransform(), true, false);
        TextLayout layout = new TextLayout(text, font, context);
        Rectangle2D bounds = layout.getBounds();

        int longestSide = (int) Math.max(bounds.getWidth(), bounds.getHeight());

        BufferedImage img = new BufferedImage(longestSide,
                                              longestSide,
                                              BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D g2 = img.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setColor(color);
        g2.setFont(font);

        int x = -(int) bounds.getX();
        int y = (int) ((img.getHeight() / 2) - (bounds.getHeight() / 2) - bounds
                .getY());

        g2.drawString(text, x, y);
        g2.dispose();
        return img;
    }

    /**
     * fluent icon building API
     */
    public static class IconBuilder {
        private char codepoint;
        private Font font;
        private Color color = new Color(0x444444);

        /**
         * A new icon builder for the font and codepoint.
         *
         * @param font      a font
         * @param codepoint a codepoint to create an icon for
         */
        private IconBuilder(Font font, char codepoint) {
            checkNotNull(font);
            this.codepoint = codepoint;
            this.font = font;
        }

        /**
         * Change the size of the font and the created icon.
         *
         * @param pt point size
         * @return reference for method chaining
         */
        public IconBuilder size(float pt) {
            font = font.deriveFont(pt);
            return this;
        }

        /**
         * Change the foreground color of the created icon.
         *
         * @param color new foreground color
         * @return reference for method chaining
         */
        public IconBuilder color(Color color) {
            checkNotNull(color);
            this.color = color;
            return this;
        }

        /**
         * Generate a square buffered image the size of the icon (longest
         * side).
         *
         * @return new buffered image
         */
        public BufferedImage image() {
            return create(codepoint, font, color);
        }

        /**
         * Generate a square image icon the size of the icon (longest side)
         *
         * @return new image icon
         */
        public ImageIcon icon() {
            return new ImageIcon(image());
        }
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
        checkArgument(!path.startsWith("/"), "path should not start with a leading slash");
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
