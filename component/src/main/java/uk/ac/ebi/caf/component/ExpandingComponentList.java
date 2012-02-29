package uk.ac.ebi.caf.component;

/**
 * AbstractExpandingComponentList.java
 *
 * 2012.02.03
 *
 * This file is part of the CheMet library
 * 
 * The CheMet library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * CheMet is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with CheMet.  If not, see <http://www.gnu.org/licenses/>.
 */
import java.awt.*;
import java.awt.event.ActionEvent;
import java.security.InvalidParameterException;
import java.util.LinkedList;
import java.util.List;
import javax.swing.*;
import org.apache.log4j.Logger;
import uk.ac.ebi.caf.component.factory.ButtonFactory;
import uk.ac.ebi.caf.component.factory.LabelFactory;
import uk.ac.ebi.caf.utility.ResourceUtility;


/**
 *
 *          AbstractExpandingComponentList 2012.02.03
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 *
 *          Class description
 *
 */
public abstract class ExpandingComponentList<C extends JComponent> {

    private static final Logger LOGGER = Logger.getLogger(ExpandingComponentList.class);

    private JComponent box;

    private List<C> components = new LinkedList<C>();

    private List<JComponent> spacers = new LinkedList<JComponent>();

    private List<JButton> subtract = new LinkedList<JButton>();

    private List<JButton> append = new LinkedList<JButton>();

    private int limit = 8;

    private Window window;

    private final int controlAxis;

    private final int axis;

    private boolean restrict = true; // whether to limit the lower/upper bounds


    public ExpandingComponentList(Window window) {
        this(window, BoxLayout.PAGE_AXIS);
    }


    /**
     * 
     * Create a new expanding component with specified axis
     * 
     * @param window window will be {@see Window#pack()} on every 
     *               append/subtract. This is useful for dialogs
     * @param axis   can be {@see BoxLayout.X_AXIS},
     *               {@see BoxLayout.Y_AXIS}, {@see BoxLayout.LINE_AXIS}
     *               or {@see BoxLayout.PAGE_AXIS}.
     * 
     */
    public ExpandingComponentList(Window window, int axis) {

        this.axis = axis == BoxLayout.X_AXIS ? BoxLayout.LINE_AXIS : axis == BoxLayout.Y_AXIS ? BoxLayout.PAGE_AXIS : axis;

        box = new Box(this.axis); // throws warning if not axis

        // control axis is at a right-angle to normal axis        
        this.controlAxis = axis == BoxLayout.LINE_AXIS ? BoxLayout.PAGE_AXIS : BoxLayout.LINE_AXIS;
        this.window = window;
        
        box.setOpaque(true);
        box.setBackground(Color.WHITE);

    }
    
    public void setBackground(Color color){
        box.setBackground(color);
    }


    public ExpandingComponentList(int axis) {
        this(null, axis);
    }


    public void setRestrict(boolean restrict) {
        this.restrict = restrict;
    }


    public void reset() {
        setSize(0);
        append();
    }


    public int getLimit() {
        return limit;
    }


    public void setLimit(int limit) {
        this.limit = limit;
    }


    public C getComponent(int i) {
        return components.get(i);
    }


    public void append() {
        append(components.size());
    }


    public int getSize() {
        return components.size();
    }


    /**
     * 
     * Set the size of the expandable component. The size will append components
     * to the end if larger then the current capacity or remove components from
     * the beginning if the new size is smaller
     * 
     * An invalid parameter will be thrown if the {@code size > getLimit()} or 
     * less then zero
     * 
     * @param size 
     */
    public void setSize(int size) {

        if ((size > getLimit() && restrict) && size < 0) {
            throw new InvalidParameterException("Size must not be larger then the current limit or less then 0: " + getLimit());
        }

        while (size > getSize()) {
            append();
        }

        while (size < getSize()) {
            remove(0);
        }

    }


    public void ensureCapacity(int capacity) {
        if (capacity > getLimit()) {
            setLimit(capacity);
        }
    }


    public JComponent newComponent(int index) {
        JComponent row = new Box(controlAxis);

        C component = newComponent();

        components.add(index, component);
        append.add(index, newAppendButton(component));
        subtract.add(index, newSubtractButton(component));

        Box componentBox = new Box(axis);

        componentBox.add(createGlue(axis));
        componentBox.add(component);
        componentBox.add(createGlue(axis));

        row.add(createGlue(controlAxis));
        row.add(componentBox);
        row.add(createStrut(controlAxis, 5));
        row.add(getControls(subtract.get(index),
                            append.get(index)));
        row.add(createGlue(controlAxis));


        Box pair = new Box(axis);
        spacers.add(index, newSpacer());
        pair.add(spacers.get(index));
        pair.add(row);


        return pair;
    }


    public Component createGlue(int axis) {
        return axis == BoxLayout.LINE_AXIS ? Box.createHorizontalGlue() : Box.createVerticalGlue();
    }


    public Component createStrut(int axis, int size) {
        return axis == BoxLayout.LINE_AXIS ? Box.createHorizontalStrut(size) : Box.createVerticalStrut(size);
    }


    public JComponent newSpacer() {
        return new JComponent() {
        };
    }




    /**
     * Assembles the subtract/append buttons into a control
     * component
     * 
     * @param subtractControl
     * @param appendControl
     * @return 
     */
    public JComponent getControls(JComponent subtractControl,
                                  JComponent appendControl) {

        Box controls = Box.createHorizontalBox();

        controls.add(Box.createHorizontalGlue());
        controls.add(subtractControl);
        controls.add(Box.createHorizontalStrut(5));
        controls.add(appendControl);
        controls.add(Box.createHorizontalGlue());

        return controls;

    }


    /**
     * 
     * Remove a component at the specified index
     * 
     * @param index 
     * 
     */
    public void remove(int index) {

        box.remove(index);
        spacers.remove(index);
        components.remove(index);
        subtract.remove(index);
        append.remove(index);

        // stop removal of the last component
        if (components.size() == 1 && restrict) {
            subtract.get(0).setEnabled(false);
        }

        for (JButton button : append) {
            button.setEnabled(true);
        }

        for (JComponent component : spacers) {
            component.setVisible(true);
        }

        if (!spacers.isEmpty()) {
            spacers.get(0).setVisible(false);
        }

        box.revalidate();

        if (window != null) {
            window.pack();
            window.repaint();
        }

    }


    public void append(JComponent component) {
        append(components.indexOf(component) + 1);
    }


    public void remove(JComponent component) {
        remove(components.indexOf(component));
    }


    /**
     * Appends a new component at the specific index
     * 
     * @param index the index where the new
     *              component will be added
     * 
     */
    public void append(int index) {

        box.add(newComponent(index), index);

        // stop adding more when limit is reached
        if (subtract.size() == limit && restrict) {
            for (JButton button : append) {
                button.setEnabled(false);
            }
        }

        if (components.size() == 1 && restrict) {
            subtract.get(0).setEnabled(false);
        }

        subtract.get(0).setEnabled(true);
        spacers.get(0).setVisible(false);


        box.revalidate();

        if (window != null) {
            window.pack();
            window.repaint();
        }

    }


    public JButton newAppendButton(final C component) {

        return ButtonFactory.newCleanButton(ResourceUtility.getIcon(ExpandingComponentList.class, "plus_12x12.png"),
                                            newAppendAction(component),
                                            box.getBackground());
    }


    public Action newAppendAction(final C component) {
        return new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                append(component);
            }
        };
    }


    public JButton newSubtractButton(final C component) {
        return ButtonFactory.newCleanButton(ResourceUtility.getIcon(ExpandingComponentList.class, "minus_12x12.png"),
                                            newSubtractAction(component),
                                            box.getBackground());
    }


    public Action newSubtractAction(final C component) {
        return new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                remove(component);
            }
        };
    }


    public JComponent getComponent() {
        return box;
    }


    public abstract C newComponent();


    public static void main(String[] args) {

        JFrame frame = new JFrame();

        ExpandingComponentList componentList = new ExpandingComponentList(frame, BoxLayout.LINE_AXIS) {

            @Override
            public JComponent newComponent() {
                return LabelFactory.newLabel("A new label object " + getComponent().getComponents().length);
            }
        };

        componentList.append();

        frame.add(componentList.getComponent());

        frame.setVisible(true);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
