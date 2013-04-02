package uk.ac.ebi.caf.utility.font;

import uk.ac.ebi.caf.utility.TtfFontLoader;

/**
 * @author John May
 */
public class IconFontDelegate implements IconFont {

    private final TtfFontLoader loader;
    private char codepoint;

    public IconFontDelegate(TtfFontLoader loader, char codepoint) {
        this.loader = loader;
        this.codepoint = codepoint;
    }

    public TtfFontLoader.IconBuilder create() {
        return loader.create(codepoint);
    }

}
