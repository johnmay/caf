package uk.ac.ebi.caf.component.list;

import com.jgoodies.forms.factories.Borders;
import org.apache.log4j.Logger;
import uk.ac.ebi.caf.component.theme.ThemeManager;

import javax.swing.*;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

/**
 * Provides support for generic types on JList using the DefaultListModel
 *
 * @author John May
 */
public class GenericJList<E> extends JList {

    private static final Logger LOGGER = Logger.getLogger(GenericJList.class);

    public GenericJList() {
        this(new DefaultListModel());
    }

    public GenericJList(DefaultListModel model) {
        super(model);
        setFont(ThemeManager.getInstance().getTheme().getBodyFont());
        // helps icon alignment
        setBorder(Borders.createEmptyBorder("2px, 2px, 2px, 2px"));
    }

    /**
     * Add an element to the underlying list model
     *
     * @param obj
     */
    public void addElement(E obj) {
        getModel().addElement(obj);
    }

    /**
     * Allows access to the elements in the list
     *
     * @return
     */
    public List<E> getElements() {

        List<E> elements = new ArrayList<E>();

        DefaultListModel model = getModel();
        for (int i = 0; i < model.size(); i++) {
            elements.add((E) model.get(i));
        }

        return elements;

    }

    public List<E> getSelected() {
        List<E> selected = new ArrayList<E>();
        for (Object o : getSelectedValues()) {
            selected.add((E) o);
        }
        return selected;
    }

    public void remove(E element) {
        int index = getModel().indexOf(element);
        if (index != -1)
            getModel().remove(index);
    }

    @Override
    public void setModel(ListModel model) {
        if (!(model instanceof DefaultListModel)) {
            throw new InvalidParameterException("List model must be DefaultListModel!");
        }
        super.setModel(model);
    }

    @Override
    public DefaultListModel getModel() {
        return (DefaultListModel) super.getModel();
    }

    @Override
    public E getSelectedValue() {
        return (E) super.getSelectedValue();
    }
}
