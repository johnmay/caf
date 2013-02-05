package uk.ac.ebi.caf.component.injection;

import junit.framework.Assert;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.yaml.snakeyaml.Yaml;

import javax.swing.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * @author John May
 */
public class YAMLComponentInjectorTest {

    private static final Logger LOGGER = Logger.getLogger(YAMLComponentInjectorTest.class);


    @Test public void testSimpleName() throws IOException {

        ComponentInjector injector = new YAMLComponentInjector(getClass().getResource("SimpleName.yml"));

        TestClass test = new TestClass();

        injector.inject(test);

        Assert.assertEquals("Sample label", test.getLabel().getText());
        Assert.assertEquals("Tooltip text label", test.getLabel().getToolTipText());

        System.out.println(test.getClass());

    }

    private class TestClass {

        @Inject
        private JLabel label = new JLabel();

        public JLabel getLabel() { return label; }

    }

}
