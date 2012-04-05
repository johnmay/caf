/**
 * PreferencePanelFactory.java
 *
 * 2012.01.29
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
package uk.ac.ebi.caf.component.factory;

import com.google.common.collect.Multimap;
import com.jgoodies.forms.factories.Borders;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.layout.Sizes;
import org.apache.log4j.Logger;
import uk.ac.ebi.caf.component.theme.ComponentPreferences;
import uk.ac.ebi.caf.utility.preference.Preference;
import uk.ac.ebi.caf.utility.preference.type.FilePreference;
import uk.ac.ebi.caf.utility.preference.type.IntegerPreference;
import uk.ac.ebi.caf.utility.preference.type.StringPreference;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


/**
 * PreferencePanelFactory 2012.01.29
 *
 * @author johnmay
 * @author $Author$ (this version)
 *         <p/>
 *         Class description
 * @version $Rev$ : Last Changed $Date$
 */
public class PreferencePanelFactory {

    private static final Logger LOGGER = Logger.getLogger(PreferencePanelFactory.class);

    private static final CellConstraints cc = new CellConstraints();


    public static JComponent getPreferencePanel(Collection<Preference> descriptors) {

        JComponent component = PanelFactory.createInfoPanel();
        FormLayout layout = new FormLayout("p, 4dlu, p", "p");
        component.setLayout(layout);

        final List<PreferenceUpdater> updaters = new ArrayList<PreferenceUpdater>();

        for (Preference pref : descriptors) {
            getPreferenceEditor(pref, component, layout);
        }
        component.setBorder(Borders.DLU4_BORDER);

        return component;


    }

    public static JComponent getPreferencePanel(Preference... descriptors) {

        JComponent component = PanelFactory.createInfoPanel();
        FormLayout layout = new FormLayout("p, 4dlu, p:grow, 4dlu, min", "p");
        component.setLayout(layout);


        for (Preference pref : descriptors) {
            getPreferenceEditor(pref, component, layout);
        }

        component.setBorder(Borders.DLU4_BORDER);

        return component;


    }


    public static void getPreferenceEditor(Preference preference,
                                           JComponent component,
                                           FormLayout layout) {
        if (preference instanceof StringPreference) {
            getPreferenceEditor((StringPreference) preference, component, layout);
        } else if (preference instanceof IntegerPreference) {
            getPreferenceEditor((IntegerPreference) preference, component, layout);
        } else if (preference instanceof FilePreference) {
            getPreferenceEditor((FilePreference) preference, component, layout);
        }
    }


    public static void getPreferenceEditor(final IntegerPreference preference,
                                           JComponent component,
                                           FormLayout layout) {

        final JLabel label = LabelFactory.newFormLabel(preference.getName(),
                                                       preference.getDescription());


        final SpinnerNumberModel model = new SpinnerNumberModel(preference.get().intValue(),
                                                                Integer.MIN_VALUE,
                                                                Integer.MAX_VALUE,
                                                                1);

        final JSpinner spinner = new JSpinner(model);

        // spinner.setPreferredSize(new Dimension(64, spinner.getPreferredSize().height));


        component.add(label, cc.xy(1, layout.getRowCount()));
        component.add(spinner, cc.xy(3, layout.getRowCount()));
        layout.appendRow(new RowSpec(Sizes.DLUY4));
        layout.appendRow(new RowSpec(Sizes.PREFERRED));


        spinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (!preference.get().equals(spinner.getValue())) {
                    preference.put((Integer) model.getValue());
                }
            }
        });
    }


    public static void getPreferenceEditor(final StringPreference preference,
                                           JComponent component,
                                           FormLayout layout) {


        final JLabel label = LabelFactory.newFormLabel(preference.getName(),
                                                       preference.getDescription());
        final JTextField field = FieldFactory.newField(preference.get());

        component.add(label, cc.xy(1, layout.getRowCount()));

        component.add(field, cc.xy(3, layout.getRowCount()));
        layout.appendRow(new RowSpec(Sizes.DLUY4));
        layout.appendRow(new RowSpec(Sizes.PREFERRED));

        field.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                if (!preference.get().equals(field.getText())) {
                    preference.put(field.getText());
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                if (!preference.get().equals(field.getText())) {
                    preference.put(field.getText());
                    if (field.getText().isEmpty()) {
                        preference.put(preference.getDefault());
                    }
                }
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                if (!preference.get().equals(field.getText())) {
                    preference.put(field.getText());
                }
            }
        });

    }

    
    private static JFileChooser chooser = new JFileChooser();

    public static void getPreferenceEditor(final FilePreference preference,
                                           final JComponent component,
                                           FormLayout layout) {


        final JLabel label = LabelFactory.newFormLabel(preference.getName(),
                                                       preference.getDescription());
        final JTextField field = FieldFactory.newField(preference.get().getAbsolutePath());

        final JButton browse = ButtonFactory.newCleanButton(new AbstractAction("Browse") {
            @Override
            public void actionPerformed(ActionEvent e) {
                chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                chooser.setSelectedFile(preference.get());
                int choice = chooser.showOpenDialog(component);
                if(choice == JFileChooser.APPROVE_OPTION){
                    field.setText(chooser.getSelectedFile().getAbsolutePath());
                }
            }
        });
        
        component.add(label, cc.xy(1, layout.getRowCount()));
        component.add(field, cc.xy(3, layout.getRowCount()));
        component.add(browse, cc.xy(5, layout.getRowCount()));
        layout.appendRow(new RowSpec(Sizes.DLUY4));
        layout.appendRow(new RowSpec(Sizes.PREFERRED));
        

        field.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                if (!preference.get().equals(field.getText())) {
                    preference.put(new File(field.getText()));
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                if (!preference.get().equals(field.getText())) {
                    preference.put(new File(field.getText()));
                }
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                if (!preference.get().equals(field.getText())) {
                    preference.put(new File(field.getText()));
                }
            }
        });


    }


    private interface PreferenceUpdater {

        public void update();
    }


    public static void main(String[] args) {

        JFrame frame = new JFrame();

        Multimap<String, Preference> map = ComponentPreferences.getInstance().getCategoryMap();
        ComponentPreferences.getInstance().list(System.out);
        System.out.println(map);
        frame.add(
                PreferencePanelFactory.getPreferencePanel(map.get("Rendering")));
        frame.setVisible(true);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
