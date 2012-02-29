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
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.swing.*;
import org.apache.log4j.Logger;
import uk.ac.ebi.caf.component.theme.ComponentPreferences;
import uk.ac.ebi.caf.utility.preference.Preference;
import uk.ac.ebi.caf.utility.preference.type.IntegerPreference;
import uk.ac.ebi.caf.utility.preference.type.StringPreference;


/**
 *
 *          PreferencePanelFactory 2012.01.29
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 *
 *          Class description
 *
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
            updaters.add(getPreferenceEditor(pref, component, layout));
        }

        component.add(ButtonFactory.newButton("Update", new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                for (PreferenceUpdater pu : updaters) {
                    pu.update();
                }
            }
        }), cc.xy(3, layout.getRowCount()));
        
        
        component.setBorder(Borders.DLU4_BORDER);

        return component;


    }


    public static PreferenceUpdater getPreferenceEditor(Preference preference,
                                                        JComponent component,
                                                        FormLayout layout) {
        if (preference instanceof StringPreference) {
            return getPreferenceEditor((StringPreference) preference, component, layout);
        }
        if (preference instanceof IntegerPreference) {
            return getPreferenceEditor((IntegerPreference) preference, component, layout);
        }
        return new PreferenceUpdater() {

            public void update() {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        };
    }


    public static PreferenceUpdater getPreferenceEditor(final IntegerPreference preference,
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


        return new PreferenceUpdater() {

            public void update() {
                if (!preference.get().equals(spinner.getValue())) {
                    preference.put((Integer) model.getValue());
                }
            }
        };
    }


    public static PreferenceUpdater getPreferenceEditor(final StringPreference preference,
                                                        JComponent component,
                                                        FormLayout layout) {


        final JLabel label = LabelFactory.newFormLabel(preference.getName(),
                                                       preference.getDescription());
        final JTextField field = FieldFactory.newField(preference.get());

        component.add(label, cc.xy(1, layout.getRowCount()));

        component.add(field, cc.xy(3, layout.getRowCount()));
        layout.appendRow(new RowSpec(Sizes.DLUY4));
        layout.appendRow(new RowSpec(Sizes.PREFERRED));

        return new PreferenceUpdater() {

            public void update() {
                if (!preference.get().equals(field.getText())) {
                    preference.put(field.getText());
                }
            }
        };

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
