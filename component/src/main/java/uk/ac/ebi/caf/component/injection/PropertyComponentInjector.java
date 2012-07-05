package uk.ac.ebi.caf.component.injection;

import org.apache.log4j.Logger;
import uk.ac.ebi.caf.utility.ResourceUtility;

import javax.swing.*;
import java.util.Properties;

/**
 * Supports resource injection from properties
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public class PropertyComponentInjector extends AbstractComponentInjector {

    private static final Logger LOGGER = Logger.getLogger(PropertyComponentInjector.class);

    // key suffixes
    private static final String NAME          = ".Text";
    private static final String TOOLTIP       = ".TooltipText";
    private static final String ICON          = ".Icon";
    private static final String SELECTED_ICON = ".Icon.Selected";
    private static final String DISABLED_ICON = ".Icon.Disabled";

    private final Properties properties;

    public PropertyComponentInjector(Properties properties) {
        this.properties = properties;
    }

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
