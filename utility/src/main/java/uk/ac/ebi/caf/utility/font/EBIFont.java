package uk.ac.ebi.caf.utility.font;

import uk.ac.ebi.caf.utility.TtfFontLoader;

/**
 * Enumeration of EBI TrueType fonts.
 *
 * @author John May
 */
public enum EBIFont {

    CONCEPTUAL("uk/ac/ebi/caf/utility/EBI-Conceptual.ttf"),
    GENERIC("uk/ac/ebi/caf/utility/EBI-Generic.ttf"),
    FUNCTIONAL("uk/ac/ebi/caf/utility/EBI-Functional.ttf"),
    FILE_FORMATS("uk/ac/ebi/caf/utility/EBI-FileFormats.ttf"),
    SPECIES("uk/ac/ebi/caf/utility/EBI-Species.ttf"),
    CHEMISTRY("uk/ac/ebi/caf/utility/EBI-Chemistry.ttf");

    private final TtfFontLoader loader;

    private EBIFont(String path) {
        this.loader = TtfFontLoader.load(path);
    }

    /**
     * Access the TrueType font loader.
     *
     * @return font loader
     */
    public TtfFontLoader loader() {
        return loader;
    }
}
