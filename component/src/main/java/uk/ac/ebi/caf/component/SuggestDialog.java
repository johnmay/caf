package uk.ac.ebi.caf.component;

import com.sun.awt.AWTUtilities;
import org.apache.log4j.Logger;

import javax.swing.*;
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

    private JTextField component;
    private Window window;
    private JList list;
    private JScrollPane pane;
    private SuggestionHandler handler;

    public SuggestDialog(Window window,
                         JTextField component,
                         SuggestionHandler handler) {

        super(window);

        setUndecorated(true);
        setFocusable(false);
        setFocusableWindowState(false);

        list = new JList(new DefaultListModel());
        list.setVisibleRowCount(5);
        list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        list.setCellRenderer(handler.getRenderer());
        JScrollPane pane = new JScrollPane(list);
        add(pane);

        this.window    = window;
        this.component = component;
        this.handler   = handler;
        
            
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
    
    public void suggest(String text){
        DefaultListModel model = (DefaultListModel) list.getModel();
        model.removeAllElements();
        for(Object suggestion : handler.getSuggestions(text)){
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
        
        if(next < max){
            list.setSelectedIndex(next);
            list.ensureIndexIsVisible(next);
        }
        
    }

    public void previous() {

        int max = list.getModel().getSize();
        int current = list.getSelectedIndex();
        int prev = current == -1
                 ? max - 1
                 : current - 1;

        if(prev >= 0){
            list.setSelectedIndex(prev);
            list.ensureIndexIsVisible(prev);
        } else {
            list.removeSelectionInterval(0,max);
        }
    }

}
