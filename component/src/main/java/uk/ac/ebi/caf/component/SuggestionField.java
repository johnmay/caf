package uk.ac.ebi.caf.component;

import org.apache.log4j.Logger;
import uk.ac.ebi.caf.component.factory.LabelFactory;
import uk.ac.ebi.caf.component.theme.ThemeManager;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 * SuggestionField - 24.02.2012 <br/>
 * <p/>
 * Class descriptions.
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public class SuggestionField extends JTextField {

    private static final Logger LOGGER = Logger.getLogger(SuggestionField.class);

    private SuggestDialog dialog;
    private DocumentListener listener;

    private boolean suggest = true;

    public SuggestionField(Window window, int col,
                           SuggestionHandler suggestionHandler,
                           final ReplacementHandler replacementHandler) {
        super(col);

        setFont(ThemeManager.getInstance().getTheme().getBodyFont());
        setForeground(ThemeManager.getInstance().getTheme().getForeground());

        dialog = new SuggestDialog(window, this, suggestionHandler);
        final JTextField component = this;

        getInputMap().put(KeyStroke.getKeyStroke("DOWN"), new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.next();
            }
        });
        getInputMap().put(KeyStroke.getKeyStroke("UP"), new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.previous();
            }
        });
        getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE,
                                                 InputEvent.CTRL_MASK), new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {

                dialog.setVisible(!dialog.isVisible()); // toggle

                if (dialog.hasSelection()) {
                    replacementHandler.replace(component, dialog.getSelection());
                }
            }
        });
        getInputMap().put(KeyStroke.getKeyStroke("ESCAPE"), new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.setVisible(false);
            }
        });

        listener = new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent documentEvent) {
                String text = getText();
                dialog.suggest(text);
                dialog.setVisible(true);
                dialog.reposition();
            }

            @Override
            public void removeUpdate(DocumentEvent documentEvent) {
                dialog.setVisible(false);
            }

            @Override
            public void changedUpdate(DocumentEvent documentEvent) {
                String text = getText();
                dialog.suggest(text);
                dialog.setVisible(true);
            }
        };

        getDocument().addDocumentListener(listener);

    }

    public void replaceWithSuggestion(Object object) {
        setText(object.toString());
    }

    public void setSuggest(boolean suggest) {
        if (this.suggest == suggest) {
            return; // not changed
        }
        this.suggest = suggest;
        if (suggest) {
            getDocument().addDocumentListener(listener);
        } else {
            getDocument().removeDocumentListener(listener);
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new SuggestionField(frame, 30, new SuggestionHandler() {
            @Override
            public Collection<Object> getSuggestions(String text) {
                return new ArrayList<Object>(Arrays.asList("1", "2", "3"));
            }
        }, new ReplacementHandler()));
        frame.pack();
        frame.setVisible(true);
    }


}
