package uk.ac.ebi.caf.component.injection;

import org.apache.log4j.Logger;
import uk.ac.ebi.caf.utility.ResourceUtility;

import javax.swing.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.Properties;

/**
 * Supports resource injection from properties
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public class PropertyComponentInjector
        extends AbstractComponentInjector
        implements ComponentInjector {

    private static final Logger LOGGER = Logger.getLogger(PropertyComponentInjector.class);

    // key suffixes
    private static final String NAME          = ".Text";
    private static final String TOOLTIP       = ".TooltipText";
    private static final String ICON          = ".Icon";
    private static final String SELECTED_ICON = ".SelectedIcon";
    private static final String DISABLED_ICON = ".DisabledIcon";

    private final Properties properties;

    /**
     * Construct the injector from a URL. The url will be opened as
     * an input stream and closed once the properties are loaded.
     *
     * @param location
     *
     * @throws IOException low-level I/O exception
     */
    public PropertyComponentInjector(URL location) throws IOException {
        this(location.openStream());
    }

    /**
     * Construct the injector from an input stream. The stream will be
     * automatically closed once the properties are loaded.
     *
     * @param stream
     *
     * @throws IOException low-level I/O exception
     */
    public PropertyComponentInjector(InputStream stream) throws IOException {
        this(new InputStreamReader(stream));
    }

    /**
     * Construct an injector from a {@see Reader} of a property file. The reader
     * will be automatically closed once the property file is read.
     *
     * @param reader instance of a reader for a property configuration
     *
     * @throws IOException low-level I/O exception
     */
    public PropertyComponentInjector(Reader reader) throws IOException {

        if (reader == null)
            throw new NullPointerException("Null reader provided");

        properties = new Properties();
        properties.load(reader);

        reader.close();
    }

    /**
     * Constructs the injector with an existing properties instance
     *
     * @param properties properties containing
     */
    public PropertyComponentInjector(Properties properties) {
        this.properties = properties;
    }

    /**
     * @inheritDoc
     */
    @Override
    public void inject(Class c, JComponent component, String fieldName) {

        setText(component, get(c, fieldName + NAME));
        component.setToolTipText(get(c, fieldName + TOOLTIP, component.getToolTipText()));

        String path = get(c, fieldName + ICON);
        if (!path.isEmpty() && canSetIcon(component)) {
            setIcon(component, ResourceUtility.getIcon(c,
                                                       path));

        }

        String disabledPath = get(c, fieldName + DISABLED_ICON);
        if (!disabledPath.isEmpty() && canSetIcon(component)) {
            setDisabledIcon(component, ResourceUtility.getIcon(c,
                                                               disabledPath));
        }

        String selectedPath = get(c, fieldName + SELECTED_ICON);
        if (!selectedPath.isEmpty() && canSetIcon(component)) {
            setSelectedIcon(component, ResourceUtility.getIcon(c,
                                                               path));
        }

    }

    /**
     * Attempts to find the key for the given class
     * <p/>
     * -> could move to abstract class
     *
     * @param c
     * @param suffix
     * @param defaultValue
     *
     * @return
     */
    private String get(Class c, String suffix, String defaultValue) {

        String key = c.getName() + "." + suffix;
        String value = properties.getProperty(key);

        if (value != null)
            return value;

        key = c.getSimpleName() + "." + suffix;
        value = properties.getProperty(key);

        if (value != null)
            return value;

        return defaultValue;

    }

    private String get(Class c, String suffix) {
        return get(c, suffix, "");
    }

    private boolean exists(String key) {
        return properties.containsKey(key);
    }
}
