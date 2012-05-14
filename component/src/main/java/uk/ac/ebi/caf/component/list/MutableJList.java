package uk.ac.ebi.caf.component.list;

import com.jgoodies.forms.layout.CellConstraints;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import uk.ac.ebi.caf.component.factory.CheckBoxFactory;
import uk.ac.ebi.caf.component.factory.PanelFactory;

import javax.swing.*;
import java.awt.datatransfer.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * A list with some default drag and drop behaviour enabled. The list allows drag and drop
 * within the same list, between multiple lists, to other components (e.g. TextField) and
 * off the application (copies to clipboard). The
 * list is generic allowing simplified object access. The default constructor allows transfer
 * to lists of any type but can be constrain to a specific class:
 * <p/>
 * In this example drag and drop will only work between {@code list1} and {@code list3} despite
 * list2 also being of generic type {@see String} the type is 'erased' at runtime.
 * <pre>{@code
 * MutableJList<String> list1 = new MutableJList<String>(String.class);
 * MutableJList<String> list2 = new MutableJList<String>();
 * MutableJList<String> list3 = new MutableJList<String>(String.class);
 * }</pre>
 * <p/>
 * This behaviour means two different list types can accepts drops from each other.
 * <pre>{@code
 * MutableJList<String> list1 = new MutableJList<String>();
 * MutableJList<Integer> list2 = new MutableJList<Integer>();
 * }</pre>
 * <p/>
 * It is therefore favourable to specify the type in the constructor if more then one
 * list type will appear.
 * <p/>
 * The delete key is also hooked up to remove items.
 *
 * @author John May
 */
public class MutableJList<E> extends GenericJList<E> {

    private static final Logger LOGGER = Logger.getLogger(MutableJList.class);

    private static DataFlavor MUTABLE_LIST_DATA_FLAVOUR;

    static {
        // create the mimetype
        try {
            MUTABLE_LIST_DATA_FLAVOUR = new DataFlavor(DataFlavor.javaJVMLocalObjectMimeType
                                                               + ";class=uk.ac.ebi.caf.component.list.MutableJList$TransferableElement");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private Class<? extends E> type;

    public MutableJList() {
        super(new MyRefreshableModel());
        setTransferHandler(new MyTransferHandler());
        setDragEnabled(true);
        setDropMode(DropMode.ON_OR_INSERT);


        // register delete key - perhaps this should be optional

        KeyStroke keyStroke = KeyStroke.getKeyStroke("DELETE");
        Action delete = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (E element : getSelected()) {
                    remove(element);
                }
                repaint();
            }
        };

        String key = "delete-element";
        getInputMap(WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(keyStroke, key);
        getActionMap().put(key, delete);


    }

    public MutableJList(Class<? extends E> type) {
        this();
        this.type = type;
    }

    /**
     * Determines whether the this list type (class) is compatible with another
     * provided list. If the types differ on one type is present and the
     * other absent this method will return false. Please not inheritance is not
     * yet supported.
     *
     * @param other other list to test the type off
     *
     * @return
     */
    public boolean isTypeCompatible(MutableJList other) {
        return type != null ? type.equals(other.type) : other.type == null;
    }


    public void refresh() {
        if (getModel() instanceof MyRefreshableModel) {
            MyRefreshableModel model = (MyRefreshableModel) getModel();
            model.refresh();
        }
    }

    /**
     * Simple wrapper class that bundle the list with the selected object.
     * It may be possible to get the selected value after the drop but to
     * be safe we store it separately
     */
    private class TransferableElement {

        private MutableJList source;
        private Object element;

        private TransferableElement(MutableJList source, Object element) {
            this.source = source;
            this.element = element;
        }

        public MutableJList getSource() {
            return source;
        }

        public Object getElement() {
            return element;
        }
    }


    /**
     * Transfer handler will import data of flavour MUTABLE_LIST_DATA_FLAVOUR
     */
    @SuppressWarnings("unchecked")
    private class MyTransferHandler extends TransferHandler {


        @Override
        public boolean canImport(TransferSupport support) {

            if (!support.isDataFlavorSupported(MUTABLE_LIST_DATA_FLAVOUR)) {
                return false;
            }

            Transferable transferable = support.getTransferable();
            try {

                TransferableElement te = (TransferableElement) transferable.getTransferData(MUTABLE_LIST_DATA_FLAVOUR);
                return isTypeCompatible(te.getSource());

            } catch (UnsupportedFlavorException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return false;

        }

        @Override
        public boolean importData(TransferSupport support) {

            if (!support.isDrop()) {
                return false;
            }

            // Check for String flavor
            if (!support.isDataFlavorSupported(MUTABLE_LIST_DATA_FLAVOUR)) {
                return false;
            }

            JList.DropLocation dl = (JList.DropLocation) support.getDropLocation();


            try {

                TransferableElement transferable = (TransferableElement) support.getTransferable().getTransferData(MUTABLE_LIST_DATA_FLAVOUR);

                int sourceIndex = transferable.getSource().getModel().indexOf(transferable.getElement());
                int targetIndex = dl.getIndex();

                // check if the source is before the target index as we may need to adjust the index
                // when me insert, This only needs to be done if we're moving in the same model
                boolean beforeTarget = transferable.getSource().getModel().equals(getModel()) && sourceIndex < targetIndex;

                // remove the element from the source
                transferable.getSource().remove(transferable.getElement());

                // add the element to the target
                getModel().add(beforeTarget ? targetIndex - 1 : targetIndex, transferable.getElement());
                repaint();

                return true;

            } catch (UnsupportedFlavorException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return false;

        }

        @Override
        public void exportToClipboard(JComponent comp, Clipboard clip, int action) throws IllegalStateException {
            clip.setContents(new StringSelection(getSelectedValue().toString()), new ClipboardOwner() {
                @Override
                public void lostOwnership(Clipboard clipboard, Transferable contents) {
                    // do nothing
                }
            });
        }

        @Override
        protected Transferable createTransferable(JComponent c) {

            final MutableJList list = (MutableJList) c;

            return new Transferable() {
                @Override
                public DataFlavor[] getTransferDataFlavors() {
                    return new DataFlavor[]{MUTABLE_LIST_DATA_FLAVOUR, DataFlavor.stringFlavor};
                }

                @Override
                public boolean isDataFlavorSupported(DataFlavor flavor) {
                    return MUTABLE_LIST_DATA_FLAVOUR.equals(flavor) || DataFlavor.stringFlavor.equals(flavor);
                }

                @Override
                public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
                    if (flavor.equals(MUTABLE_LIST_DATA_FLAVOUR)) {
                        return new TransferableElement(list, list.getSelectedValue());
                    }
                    return list.getSelectedValue().toString();
                }

            };
        }

        @Override
        public int getSourceActions(JComponent c) {
            return COPY_OR_MOVE;
        }
    }


    private static class MyRefreshableModel extends DefaultListModel {

        private List backup = new ArrayList();

        public MyRefreshableModel() {
            super();
        }


        @Override
        public void setElementAt(Object obj, int index) {
            super.setElementAt(obj, index);
            backup.add(index, obj);
        }

        @Override
        public void clear() {
            super.clear();
            backup.clear();
        }

        @Override
        public void add(int index, Object element) {
            super.add(index, element);
            if (!backup.contains(element)) backup.add(index, element);
        }

        @Override
        public Object set(int index, Object element) {
            backup.set(index, element);
            return super.set(index, element);
        }

        @Override
        public void removeAllElements() {
            backup.clear();
            super.removeAllElements();
        }

        @Override
        public void addElement(Object obj) {
            super.addElement(obj);
            if (!backup.contains(obj)) backup.add(obj);
        }

        @Override
        public void insertElementAt(Object obj, int index) {
            if (!backup.contains(obj)) backup.add(index, obj);
            super.insertElementAt(obj, index);
        }

        public void refresh() {
            super.clear();
            for (Object obj : backup) {
                super.addElement(obj);
            }
        }

    }

}
