package uk.ac.ebi.caf.component.injection;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Flags a field for resource injection.
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Inject {
    String value() default "";
}
