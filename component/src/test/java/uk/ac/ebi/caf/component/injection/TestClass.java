package uk.ac.ebi.caf.component.injection;

import org.apache.log4j.Logger;

import javax.swing.*;

/**
 * @author John May
 */
public class TestClass {

    @Inject
    private JLabel label = new JLabel();

    public JLabel getLabel() { return label; }


}
