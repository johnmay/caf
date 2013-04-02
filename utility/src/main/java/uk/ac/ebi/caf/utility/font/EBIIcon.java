package uk.ac.ebi.caf.utility.font;

import uk.ac.ebi.caf.utility.TtfFontLoader;

/**
 * Enumeration of EBI font icons.
 *
 * @author John May
 */
public enum EBIIcon implements IconFont {

    CHEMICAL(EBIFont.CONCEPTUAL, 'b'),
    DNA(EBIFont.CONCEPTUAL, 'd'),
    GENE(EBIFont.CONCEPTUAL, 'g'),
    ONTOLOGIES(EBIFont.CONCEPTUAL, 'o'),
    OTHER(EBIFont.CONCEPTUAL, 'c'),
    PROTEINS(EBIFont.CONCEPTUAL, 'P'),
    STRUCTURES(EBIFont.CONCEPTUAL, 's'),
    SYSTEMS(EBIFont.CONCEPTUAL, 'y'),
    LITERATURE(EBIFont.CONCEPTUAL, 'l'),

    DIRECTION_UNKNOWN(EBIFont.CHEMISTRY, 'U'),
    DIRECTION_RIGHT(EBIFont.CHEMISTRY, 'r'),
    DIRECTION_LEFT(EBIFont.CHEMISTRY, 'l'),
    DIRECTION_REVERSIBLE(EBIFont.CHEMISTRY, 'R');

    private final IconFontDelegate delegate;

    private EBIIcon(EBIFont loader, char codepoint) {
        this.delegate = new IconFontDelegate(loader.loader(), codepoint);
    }

    /**
     * Start creating a new icon
     *
     * @return new icon
     */
    @Override public TtfFontLoader.IconBuilder create() {
        return delegate.create();
    }
}
