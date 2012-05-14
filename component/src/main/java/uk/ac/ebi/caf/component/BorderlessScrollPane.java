package uk.ac.ebi.caf.component;

import com.jgoodies.forms.factories.Borders;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.*;

/**
 *          Wraps JScrollPane with a constructor that uses an empty border
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
public class BorderlessScrollPane extends JScrollPane {

    private static final Logger LOGGER = Logger.getLogger(BorderlessScrollPane.class);

    public BorderlessScrollPane(Component view) {
        super(view);
        setBorder(Borders.EMPTY_BORDER);
        setViewportBorder(Borders.EMPTY_BORDER);
    }

    public BorderlessScrollPane(Component view, int horizontalBar) {
        super(view);
        setBorder(Borders.EMPTY_BORDER);
        setViewportBorder(Borders.EMPTY_BORDER);
        setHorizontalScrollBarPolicy(horizontalBar);
    }
}