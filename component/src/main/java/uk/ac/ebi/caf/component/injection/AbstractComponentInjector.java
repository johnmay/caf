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
public abstract class AbstractComponentInjector {

    private static final Logger LOGGER = Logger.getLogger(AbstractComponentInjector.class);


    public void inject(Object object){
        for(Field field : object.getClass().getDeclaredFields()){
            if(field.isAnnotationPresent(Inject.class)){
                inject(object, field);
            }
        }
    }


    private void inject(Object object, Field field) {

        Inject inject = field.getAnnotation(Inject.class);
        String name   = inject.value().isEmpty() ? field.getName() : inject.value();

        try {
            // could look up an accessor method but for time being we will
            // be naughty and break encapsulation
            field.setAccessible(true);
            JComponent component = (JComponent) field.get(object);
            field.setAccessible(false);

            // inject the
            inject(component, name);

        } catch (IllegalAccessException ex) {
            LOGGER.info("Field was not accessible");
        }

    }


    protected static boolean canSetIcon(JComponent component) {
        // JToggleButton, JCheckBox and JRadioButton inherit from JButton so aren't tested
        return component instanceof JLabel || component instanceof JButton;
    }

    protected static boolean canSetSelectedIcon(JComponent component) {
        // JToggleButton, JCheckBox and JRadioButton inherit from JButton so aren't tested
        return component instanceof JButton;
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

    protected static void setIcon(JComponent component, Icon icon) {
        AbstractComponentInjector.invoke(component, "setIcon", icon);
    }

    protected static void setDisabledIcon(JComponent component, Icon icon) {
        AbstractComponentInjector.invoke(component, "setDisabledIcon", icon);
    }

    protected static void setSelectedIcon(JComponent component, Icon icon) {
        AbstractComponentInjector.invoke(component, "setSelectedIcon", icon);
    }

    protected abstract void inject(JComponent component, String name);

}
