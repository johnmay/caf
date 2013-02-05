package uk.ac.ebi.caf.component.injection;

import org.apache.log4j.Logger;

import javax.swing.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public abstract class AbstractComponentInjector implements ComponentInjector {

    private static final Logger LOGGER = Logger.getLogger(AbstractComponentInjector.class);

    public void inject(Object object) {

        if (object == null)
            throw new NullPointerException("Null object passed to ");

        inject(object.getClass(), object.getClass(), object);

    }

    private void inject(Class root, Class current, Object object) {

        for (Field field : current.getDeclaredFields()) {
            if (field.isAnnotationPresent(Inject.class)) {
                inject(root, object, field);
            }
        }

        Class parent = current.getSuperclass();

        if (parent != null && parent != Object.class) {
            inject(root, parent, object);
        }

    }


    private void inject(Class c, Object object, Field field) {

        Inject inject = field.getAnnotation(Inject.class);
        String name = inject.value().isEmpty() ? field.getName() : inject.value();

        try {

            // XXX breaking encapsulation >_>
            field.setAccessible(true);
            JComponent component = (JComponent) field.get(object);
            field.setAccessible(false);

            // don't inject null components
            if (component == null) {
                LOGGER.error("Injection attempted on uninitialised field - " + field);
                return;
            }

            // inject the information
            inject(c, component, name);

        } catch (IllegalAccessException ex) {
            LOGGER.info("Field was not accessible");
        }

    }


    protected static boolean canSetIcon(JComponent component) {
        // JToggleButton, JCheckBox and JRadioButton inherit from JButton so aren't tested
        return component instanceof JLabel || component instanceof AbstractButton;
    }

    protected static boolean canSetSelectedIcon(JComponent component) {
        // JToggleButton, JCheckBox and JRadioButton inherit from JButton so aren't tested
        return component instanceof AbstractButton;
    }

    protected static void invoke(JComponent component, String methodName, Object... params) {
        Class c = component.getClass();
        try {
            Method m = c.getMethod(methodName, Icon.class);
            m.invoke(component, params);
        } catch (Exception ex) {
            LOGGER.error("Unable to inject component, method: '" + methodName + "'(" + params + ") class: " + c);
        }
    }

    protected static void setText(JComponent component, String text) {

        if (text == null || text.isEmpty())
            return;

        if (component instanceof JLabel) {
            ((JLabel) component).setText(text);
        } else if (component instanceof AbstractButton) {
            ((AbstractButton) component).setText(text);
        }
    }

    protected static void setIcon(JComponent component, Icon icon) {
        AbstractComponentInjector.invoke(component, "setIcon", icon);
    }

    protected static void setDisabledIcon(JComponent component, Icon icon) {
        AbstractComponentInjector.invoke(component, "setDisabledIcon", icon);
    }

    protected static void setSelectedIcon(JComponent component, Icon icon) {
        AbstractComponentInjector.invoke(component, "setSelectedIcon", icon);
    }

}
