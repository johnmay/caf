package uk.ac.ebi.caf.component;

import org.apache.log4j.Logger;
import uk.ac.ebi.caf.utility.ColorUtility;

import javax.swing.DefaultListModel;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.event.DocumentEvent;
import java.awt.AWTKeyStroke;
import java.awt.Color;
import java.awt.KeyboardFocusManager;
import java.awt.Point;
import java.awt.Window;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.Collections;

/**
 * SuggestDialog - 24.02.2012 <br/> <p/> Class descriptions.
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public class SuggestDialog extends JDialog {

    private static final Logger LOGGER = Logger.getLogger(SuggestDialog.class);

    private JTextField        component;
    private Window            window;
    private JList             list;
    private DefaultListModel  model;
    private JScrollPane       pane;
    private SuggestionHandler handler;

    public SuggestDialog(Window window, JTextField component, int nVisibleRows, SuggestionHandler handler) {

        super(window);

        setUndecorated(true);
        setFocusable(false);
        setFocusableWindowState(false);
        setAlwaysOnTop(true);

        this.window = window;
        this.component = component;
        this.handler = handler;

        model = new DefaultListModel();
        list = new JList(model);
        list.setVisibleRowCount(nVisibleRows);
        list.setBackground(component.getBackground());
        list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        ListCellRenderer renderer = handler.getRenderer();
        prepareRenderer(renderer);
        list.setCellRenderer(renderer);

        pane = new JScrollPane(list);
        add(pane);


        window.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent componentEvent) {
                reposition();
            }

            @Override
            public void componentMoved(ComponentEvent componentEvent) {
                reposition();
            }
        });

        setFocusTraversalKeysEnabled(false);
        setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS,
                              Collections.<AWTKeyStroke>emptySet());
        setFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS,
                              Collections.<AWTKeyStroke>emptySet());

    }

    public JList getList() {
        return list;
    }

    public void suggest(DocumentEvent event) {
        DefaultListModel model = (DefaultListModel) list.getModel();
        model.removeAllElements();
        for (Object suggestion : handler.getSuggestions(event)) {
            model.addElement(suggestion);
        }
    }

    public void reposition() {
        try {
            Point p = component.getLocationOnScreen();
            p.y += component.getHeight();
            setLocation(p);
            setSize(component.getWidth(),
                    list.getPreferredScrollableViewportSize().height);
            pane.setSize(component.getWidth(),
                         list.getPreferredScrollableViewportSize().height);
        } catch (Exception ex) {
            // probably because the component wasn't visible
        }
    }

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible && model.getSize() > 0);
    }

    public boolean hasSelection() {
        return list.getSelectedIndex() != -1;
    }

    public Object getSelection() {
        return list.getSelectedValue();
    }

    public void next() {

        int max = list.getModel().getSize();
        int current = list.getSelectedIndex();
        int next = current == -1 ? 0 : current + 1;

        if (next < max) {
            list.setSelectedIndex(next);
            list.ensureIndexIsVisible(next);
        }

    }

    public void prepareRenderer(ListCellRenderer renderer) {
        // set fg col
        Color foreground = ColorUtility.shade(component.getForeground(), 0.4f);
        list.setFont(component.getFont());
        list.setForeground(foreground);
    }

    public void previous() {

        int max = list.getModel().getSize();
        int current = list.getSelectedIndex();
        int prev = current == -1 ? max - 1 : current - 1;

        if (prev >= 0) {
            list.setSelectedIndex(prev);
            list.ensureIndexIsVisible(prev);
        } else {
            list.removeSelectionInterval(0, max);
        }
    }

    public void clear() {
        model.clear();
        setVisible(false);
        dispose();
    }

}
