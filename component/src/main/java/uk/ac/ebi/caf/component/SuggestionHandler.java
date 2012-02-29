package uk.ac.ebi.caf.component;

import org.apache.log4j.Logger;

import javax.swing.*;
import java.util.Collection;

/**
 * SuggestionHandler - 24.02.2012 <br/>
 * <p/>
 * Class descriptions.
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public abstract class SuggestionHandler {

    public  ListCellRenderer getRenderer(){
        return new DefaultListCellRenderer();
    }

    public abstract Collection<Object> getSuggestions(String text);

}
