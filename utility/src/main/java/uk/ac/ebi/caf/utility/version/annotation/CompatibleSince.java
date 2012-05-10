package uk.ac.ebi.caf.utility.version.annotation;

import uk.ac.ebi.caf.utility.version.Version;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * CompatibleSince - 10.03.2012 <br/>
 * <p/>
 * Provides an annotation for marking version compatability
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface CompatibleSince {
    String value();
}
