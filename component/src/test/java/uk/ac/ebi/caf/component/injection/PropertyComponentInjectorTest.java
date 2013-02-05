package uk.ac.ebi.caf.component.injection;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;

import javax.swing.*;
import java.io.IOException;

/**
 * @author John May
 */
public class PropertyComponentInjectorTest {

    private static final Logger LOGGER = Logger.getLogger(PropertyComponentInjectorTest.class);

    @Test
    public void testSimpleName() throws IOException {

        ComponentInjector injector = new PropertyComponentInjector(getClass().getResource("SimpleName.properties"));

        TestClass test = new TestClass();

        injector.inject(test);

        Assert.assertEquals("Sample label", test.getLabel().getText());
        Assert.assertEquals("Tooltip text label", test.getLabel().getToolTipText());

    }

    /**
     * Tests injection using full class name
     * @throws IOException
     */
    @Test
    public void testFullName() throws IOException {

        ComponentInjector injector = new PropertyComponentInjector(getClass().getResource("SimpleName.properties"));

        uk.ac.ebi.caf.component.injection.TestClass test = new uk.ac.ebi.caf.component.injection.TestClass();

        injector.inject(test);

        Assert.assertEquals("Different Sample label", test.getLabel().getText());
        Assert.assertEquals("Different Tooltip text label", test.getLabel().getToolTipText());

    }

    private class TestClass {

        @Inject
        private JLabel label = new JLabel();

        public JLabel getLabel() {
            return label;
        }

    }


}
