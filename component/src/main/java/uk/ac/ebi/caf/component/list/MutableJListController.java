package uk.ac.ebi.caf.component.list;

import com.jgoodies.forms.factories.Borders;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.Sizes;
import org.apache.log4j.Logger;
import uk.ac.ebi.caf.component.BorderlessScrollPane;
import uk.ac.ebi.caf.component.factory.ButtonFactory;
import uk.ac.ebi.caf.component.factory.PanelFactory;
import uk.ac.ebi.caf.utility.ResourceUtility;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * @author John May
 */
public class MutableJListController {

    private static final Logger LOGGER = Logger.getLogger(MutableJListController.class);

    private JPanel controller = PanelFactory.createDialogPanel("min, 2dlu, min, 8dlu:grow, min, 0dlu, min",
                                                               "p");
    private JPanel combined;

    public MutableJList list;

    public MutableJListController(MutableJList list) {
        this.list = list;
        createPanel();
    }

    private JButton createRefreshButton() {
        return ButtonFactory.newCleanButton(ResourceUtility.getIcon(getClass(),
                                                                    "refresh_16x16.png"), new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                list.refresh();
            }
        }, "Refresh list contents");
    }

    private JButton createDeleteButton() {
        return ButtonFactory.newCleanButton(ResourceUtility.getIcon(getClass(), "trash_12x12.png"), new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                int index = list.getSelectedIndex();
                if (index != -1) {
                    list.getModel().remove(index);
                }
            }
        }, "Delete selected list item");
    }

    private JButton createNudgeUpButton() {
        return ButtonFactory.newCleanButton(ResourceUtility.getIcon(getClass(), "up_16x16.png"), new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int index = list.getSelectedIndex();
                if (index - 1 >= 0) {
                    Object element = list.getSelectedValue();
                    list.remove(element);
                    list.getModel().add(index - 1, element);
                    list.setSelectedValue(element, true);
                }
            }
        }, "Move selected list item up");
    }

    private JButton createNudgeDownButton() {
        return ButtonFactory.newCleanButton(ResourceUtility.getIcon(getClass(), "down_16x16.png"),
                                            new AbstractAction() {
                                                @Override
                                                public void actionPerformed(ActionEvent e) {
                                                    int index = list.getSelectedIndex();
                                                    if (index != -1
                                                            && index + 1 < list.getModel().getSize()) {
                                                        Object element = list.getSelectedValue();
                                                        list.remove(element);
                                                        list.getModel().add(index + 1, element);
                                                        list.setSelectedValue(element, true);
                                                    }
                                                }
                                            }, "Move selected list item down");
    }

    private final void createPanel() {

        CellConstraints cc = new CellConstraints();

        controller.add(createRefreshButton(), cc.xy(1, 1));
        controller.add(createDeleteButton(), cc.xy(3, 1));

        controller.add(createNudgeDownButton(), cc.xy(5, 1));
        controller.add(createNudgeUpButton(), cc.xy(7, 1));

        controller.setBorder(Borders.createEmptyBorder(Sizes.DLUY2, Sizes.ZERO, Sizes.DLUY2, Sizes.ZERO));

    }

    public JComponent getControls() {
        return controller;
    }

    private final JPanel createListWithController() {
        JPanel panel = PanelFactory.createDialogPanel("p:grow",
                                                      "p, p");
        CellConstraints cc = new CellConstraints();

        panel.add(new BorderlessScrollPane(list), cc.xy(1, 1));
        panel.add(controller, cc.xy(1, 2));

        return panel;

    }

    public JComponent getListWithController() {
        if (combined == null)
            combined = createListWithController();
        return combined;
    }

}
