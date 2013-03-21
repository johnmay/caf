package uk.ac.ebi.caf.component;

import org.apache.log4j.Logger;
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

    private SuggestDialog      dialog;
    private DocumentListener   listener;
    private ReplacementHandler replacementHandler;
    private Window             window;
    private SuggestionHandler  suggestionHandler;

    private boolean suggest = true;


    public SuggestionField(Window window, int col,
                           SuggestionHandler suggestionHandler,
                           ReplacementHandler replacementHandler) {
        this(window, col, 5, suggestionHandler, replacementHandler);
    }

    public SuggestionField(Window window, int col, int nVisibleRows,
                           SuggestionHandler suggestionHandler,
                           ReplacementHandler replacementHandler) {
        super(col);


        this.replacementHandler = replacementHandler;
        this.suggestionHandler  = suggestionHandler;

        listener = new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent documentEvent) {
                String text = getText();
                dialog.suggest(documentEvent);
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
                dialog.suggest(documentEvent);
                dialog.setVisible(true);
            }
        };

        setFont(ThemeManager.getInstance().getTheme().getBodyFont());
        setForeground(ThemeManager.getInstance().getTheme().getForeground());

        dialog = new SuggestDialog(window, this, nVisibleRows, suggestionHandler);

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
                    handleReplacement();
                }
            }
        });
        getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,
                                                 0), new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {

                dialog.setVisible(!dialog.isVisible()); // toggle

                if (dialog.hasSelection()) {
                    handleReplacement();
                }
            }
        });
        getInputMap().put(KeyStroke.getKeyStroke("ESCAPE"), new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.setVisible(false);
            }
        });

        dialog.getList().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // could simply check for hasSelection() but a user may click the list
                // and not select an item...
                int index = dialog.getList().locationToIndex(e.getPoint());
                if (index != -1) {
                    dialog.setVisible(!dialog.isVisible());
                    handleReplacement();
                }
            }
        });


        getDocument().addDocumentListener(listener);

    }

    private void handleReplacement() {
        getDocument().removeDocumentListener(listener);
        replacementHandler.replace(this, dialog.getSelection());
        getDocument().addDocumentListener(listener);
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

    public void clear() {
        getDocument().removeDocumentListener(listener);
        setText("");
        dialog.clear();
        getDocument().addDocumentListener(listener);
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
