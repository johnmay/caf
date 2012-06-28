package uk.ac.ebi.caf.component;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import java.util.ArrayList;
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

    public ListCellRenderer getRenderer() {
        return new DefaultListCellRenderer();
    }

    public Collection<Object> getSuggestions(String text) {
        return new ArrayList<Object>();
    }

    public Collection<Object> getSuggestions(DocumentEvent e) {
        try {
            Document document = e.getDocument();
            return getSuggestions(document.getText(0, document.getLength()));
        } catch (BadLocationException e1) {
            return getSuggestions("");
        }
    }

}
