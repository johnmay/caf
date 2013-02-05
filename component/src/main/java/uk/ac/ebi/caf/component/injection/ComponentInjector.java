package uk.ac.ebi.caf.component.injection;

import javax.swing.*;

/**
 * Injects resources into JComponents (e.g. labels, buttons). The injector can create new
 * components or inject existing components.
 *
 * @author John May
 */
public interface ComponentInjector {

    public void inject(Object object);

    public void inject(Class c, JComponent component, String fieldName);

}
