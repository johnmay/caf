package uk.ac.ebi.caf.utility.font;

import javax.swing.ImageIcon;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author John May
 */
public class IconBuilder {

    private char codepoint;

    private Font font;

    private AffineTransform transform = new AffineTransform();

    private enum Flip {
        Horizontal,
        Vertical,
        None
    }

    ;

    private Flip flip = Flip.None;

    private Color dropShadow;

    private Color color = new Color(0x444444);

    /**
     * A new icon builder for the font and codepoint.
     *
     * @param font      a font
     * @param codepoint a codepoint to create an icon for
     */
    public IconBuilder(Font font, char codepoint) {
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
     * A 1px drop shadow (white).
     *
     * @return reference for method chaining
     */
    public IconBuilder highlight() {
        this.dropShadow = Color.WHITE;
        return this;
    }

    /**
     * A 1px drop shadow (black).
     *
     * @return reference for method chaining
     */
    public IconBuilder lowlight() {
        this.dropShadow = Color.BLACK;
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

    private TextLayout layout() {
        String text = Character.toString(codepoint);
        FontRenderContext context = new FontRenderContext(transform, true, false);
        TextLayout layout = new TextLayout(text, font, context);
        return layout;
    }

    private int length(TextLayout layout) {
        int longestSide = (int) Math.ceil(Math.max(layout.getBounds()
                                                         .getWidth(), layout
                                                           .getBounds()
                                                           .getHeight()));
        return dropShadow != null ? 2 + longestSide : longestSide;
    }

    /**
     * Generate a square buffered image the size of the icon (longest side).
     *
     * @return new buffered image
     */
    public BufferedImage image() {

        String text = Character.toString(codepoint);
        TextLayout layout = layout();
        Rectangle2D bounds = layout.getBounds();

        int length = length(layout);

        BufferedImage img = new BufferedImage(length,
                                              length,
                                              BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D g2 = img.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setFont(font);
        g2.setTransform(transform(length));

        float cx = (float) (length / 2 - bounds.getWidth() / 2);
        float cy = (float) (length / 2 - bounds.getHeight() / 2);
        float ox = cx - (float) bounds.getX();
        float oy = cy - (float) bounds.getY();

        if (dropShadow != null) {
            g2.setColor(dropShadow);
            g2.drawString(text, ox, oy + 1);
        }

        g2.draw(bounds);

        g2.setColor(color);
        g2.drawString(text, ox, oy);

        g2.dispose();
        return img;

    }

    private AffineTransform transform(int length) {
        transform = new AffineTransform();
        if (flip == Flip.Horizontal) {
            transform.setToScale(-1, 1);
            transform.translate(-length, 0);
        }
        return transform;
    }

    public IconBuilder flipHorizontal() {
        this.flip = Flip.Horizontal;
        return this;
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
