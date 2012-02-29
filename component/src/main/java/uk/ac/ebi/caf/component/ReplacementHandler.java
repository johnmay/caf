package uk.ac.ebi.caf.component;

import org.apache.log4j.Logger;

import javax.swing.*;

/**
 * ReplacementHandler - 24.02.2012 <br/>
 * <p/>
 * Class descriptions.
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public class ReplacementHandler {

    public void replace(JTextField field, Object value){
        field.setText(value.toString());
    }

}
