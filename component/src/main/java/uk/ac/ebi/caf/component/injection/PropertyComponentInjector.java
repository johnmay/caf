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
    private final Class root;

    public PropertyComponentInjector(Properties properties, Class root){
        this.properties = properties;
        this.root       = root;
    }

    @Override
    protected void inject(JComponent component, String name) {

        component.setName(properties.getProperty(name + NAME, component.getName()));
        component.setToolTipText(properties.getProperty(name + TOOLTIP, component.getToolTipText()));

        String iconKey = name + ICON;
        if(exists(iconKey) && canSetIcon(component)){
            setIcon(component, ResourceUtility.getIcon(root,
                                                       properties.getProperty(iconKey)));

        }

        String disabledIconKey = name + DISABLED_ICON;
        if(exists(iconKey) && canSetIcon(component)){
            setDisabledIcon(component, ResourceUtility.getIcon(root,
                                                               properties.getProperty(disabledIconKey)));
        }

        String selectedIconKey = name + SELECTED_ICON;
        if(exists(iconKey) && canSetIcon(component)){
            setSelectedIcon(component, ResourceUtility.getIcon(root,
                                                               properties.getProperty(selectedIconKey)));
        }

    }

    private boolean exists(String key){
        return properties.getProperty(key) != null;
    }
}
