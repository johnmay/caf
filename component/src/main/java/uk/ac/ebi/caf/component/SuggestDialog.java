package uk.ac.ebi.caf.component;

import org.apache.log4j.Logger;
import uk.ac.ebi.caf.utility.ColorUtility;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

/**
 * SuggestDialog - 24.02.2012 <br/>
 * <p/>
 * Class descriptions.
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
    private JScrollPane       pane;
    private SuggestionHandler handler;

    public SuggestDialog(Window window,
                         JTextField component,
                         SuggestionHandler handler) {

        super(window);

        setUndecorated(true);
        setFocusable(false);
        setFocusableWindowState(false);
        setAlwaysOnTop(true);

        list = new JList(new DefaultListModel());
        list.setVisibleRowCount(5);
        list.setBackground(component.getBackground());
        list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        ListCellRenderer renderer = handler.getRenderer();
        prepareRenderer(renderer);
        list.setCellRenderer(renderer);

        JScrollPane pane = new JScrollPane(list);
        add(pane);

        this.window = window;
        this.component = component;
        this.handler = handler;


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
            setSize(component.getWidth(), list.getPreferredScrollableViewportSize().height);
        } catch (Exception ex) {
            // probably because the component wasn't visible
        }
    }

    @Override
    public void setVisible(boolean visible) {
        boolean hasSuggestions = list.getModel().getSize() != 0;
        super.setVisible(visible && hasSuggestions);
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
        int next = current == -1
                   ? 0
                   : current + 1;

        if (next < max) {
            list.setSelectedIndex(next);
            list.ensureIndexIsVisible(next);
        }

    }

    public void prepareRenderer(ListCellRenderer renderer) {
        if (renderer instanceof Component) {
            Component component = (Component) renderer;
            Font font = component.getFont();
            component.setFont(font.deriveFont(font.getSize() * 0.8f));

            list.setFont(component.getFont());

            // set fg col
            Color foreground = ColorUtility.shade(component.getForeground(), 0.4f);
            component.setForeground(foreground);
            list.setForeground(foreground);
        }
    }

    public void previous() {

        int max = list.getModel().getSize();
        int current = list.getSelectedIndex();
        int prev = current == -1
                   ? max - 1
                   : current - 1;

        if (prev >= 0) {
            list.setSelectedIndex(prev);
            list.ensureIndexIsVisible(prev);
        } else {
            list.removeSelectionInterval(0, max);
        }
    }

}
