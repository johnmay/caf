package uk.ac.ebi.caf.component;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.sun.awt.AWTUtilities;
import uk.ac.ebi.caf.component.factory.ButtonFactory;
import uk.ac.ebi.caf.component.factory.ShapeFactory;
import uk.ac.ebi.caf.utility.ResourceUtility;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;

/**
 * ${Name}.java - 21.02.2012 <br/> Description...
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$
 */
public class CalloutDialog
        extends JDialog {

    private JPanel backgroundPanel = new CalloutPanel();
    private JPanel mainPanel = new JPanel();
    private JComponent anchor;
    private Window anchorWindow;
    private ComponentAdapter anchorListener = new ComponentAdapter() {
        @Override
        public void componentMoved(ComponentEvent componentEvent) {
            place();
        }
    };

    public CalloutDialog(Window parent) {
        this(parent, ModalityType.APPLICATION_MODAL);
    }

    public CalloutDialog(Window parent, ModalityType modality) {

        super(parent, modality);

        setContentPane(backgroundPanel);
        setUndecorated(true);
        AWTUtilities.setWindowOpaque(this, false);

        CellConstraints cc = new CellConstraints();

        backgroundPanel.setLayout(new FormLayout("8px, 16px, p, 16px, 8px",
                                                 "8px, 16px, p, 16px, 8"));


        JButton close = ButtonFactory.newCleanButton(ResourceUtility.getIcon(CalloutDialog.class, "close_16x16.png"),
                                                     new AbstractAction() {
                                                         @Override
                                                         public void actionPerformed(ActionEvent actionEvent) {
                                                            setVisible(false);
                                                         }
                                                     });

        backgroundPanel.add(close, cc.xy(2, 2));
        backgroundPanel.add(mainPanel, cc.xy(3, 3));

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent componentEvent) {
                if(anchor != null){
                    place();
                }
            }
        });

        //setResizable(false);

    }

    /**
     * Place the dialog on the screen based on the anchor location
     */
    public void place(){
        Point point = anchor.getLocationOnScreen();
        int x = point.x + (anchor.getWidth() / 2) ;
        int y = point.y + (anchor.getHeight() / 2);
        setLocation(x - (getWidth() / 2), y - getHeight() + 7 );
    }

    public JComponent getMainPanel() {
        return mainPanel;
    }

    public void setAnchor(JComponent anchor){
        if(this.anchor != null){
            SwingUtilities.getWindowAncestor(anchor).removeComponentListener(anchorListener);
        }
        this.anchor = anchor;
        SwingUtilities.getWindowAncestor(anchor).addComponentListener(anchorListener);
    }

    public static void main(String[] args) {
        final JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        final JButton button = new JButton();
        button.setAction(new AbstractAction("Show dialog") {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                final CalloutDialog dialog = new CalloutDialog(frame, ModalityType.APPLICATION_MODAL);
                ExpandingComponentList expand = new ExpandingComponentList<JLabel>(dialog) {
                    @Override
                    public JLabel newComponent() {
                        return new JLabel("This a long label");
                    }
                };
                dialog.setAnchor(button);
                dialog.getMainPanel().add(expand.getComponent());
                expand.append();
                dialog.setVisible(true);

            }
        });
        frame.add(button);

        frame.setVisible(true);
        frame.pack();
    }


    private class CalloutPanel extends JPanel {

        private BufferedImage img = ShapeFactory.getCalloutImage(getPreferredSize());

        public CalloutPanel() {
            addComponentListener(new ComponentAdapter() {
                @Override
                public void componentResized(ComponentEvent componentEvent) {
                    img = ShapeFactory.getCalloutImage(getPreferredSize());
                    repaint();
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            g.drawImage(img, 0, 0, null);
            super.paintComponent(g);
        }

    }

}
