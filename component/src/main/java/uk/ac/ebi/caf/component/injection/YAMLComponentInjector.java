package uk.ac.ebi.caf.component.injection;

import org.apache.log4j.Logger;
import org.yaml.snakeyaml.Yaml;

import javax.swing.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.security.InvalidParameterException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import static uk.ac.ebi.caf.utility.ResourceUtility.getIcon;

/**
 * An component injector that uses a YAML resource file ('.yml').
 *
 * @author John May
 * @see ComponentInjector
 */
public class YAMLComponentInjector
        extends AbstractComponentInjector
        implements ComponentInjector {

    private static final Logger LOGGER = Logger
            .getLogger(YAMLComponentInjector.class);

    private Map<String, Map<String, Map<String, String>>> map = new HashMap<String, Map<String, Map<String, String>>>(50);

    public YAMLComponentInjector(String path) throws IOException {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();

        Enumeration<URL> urls = loader.getResources(path);
        while (urls.hasMoreElements()) {
            URL url = urls.nextElement();
            InputStream in = url.openStream();
            if (in != null) {
                try {
                    // note - may clobber if duplicate top level but as the top
                    // level is a class name this should be fine
                    map.putAll(load(in));
                } catch (IOException e) {
                    throw e;
                } finally {
                    in.close();
                }
            }
        }
    }

    public YAMLComponentInjector(URL url) throws IOException {
        this(url.openStream());
    }

    public YAMLComponentInjector(InputStream stream) throws IOException {
        this(new InputStreamReader(stream));
    }

    public YAMLComponentInjector(Reader reader) throws IOException {

        Yaml yaml = new Yaml();

        // could map to a better object
        Object data = yaml.load(reader);

        if (isValid(data)) {
            map.putAll((Map<String, Map<String, Map<String, String>>>) data);
        } else {
            throw new InvalidParameterException("Map<String,Map<String,String>> expected in yaml file");
        }
    }

    private Map<String, Map<String, Map<String, String>>> load(InputStream in) throws
                                                                               IOException {
        Yaml yaml = new Yaml();
        Object data = yaml.load(in);
        if (isValid(data)) {
            return (Map<String, Map<String, Map<String, String>>>) data;
        } else {
            throw new IOException("Map<String,Map<String,String>> expected in yaml file");
        }
    }

    private boolean isValid(Object data) {

        if (!(data instanceof Map))
            return false;

        Map<?, ?> dataMap = (Map) data;

        for (Map.Entry<?, ?> dataMapEntry : dataMap.entrySet()) {

            if (!(dataMapEntry.getKey() instanceof String))
                return false;

            if (!(dataMapEntry.getValue() instanceof Map))
                return false;

            Map<?, ?> subMap = (Map) dataMapEntry.getValue();

            for (Map.Entry<?, ?> subMapEntry : subMap.entrySet()) {
                if (!(dataMapEntry.getKey() instanceof String))
                    return false;

                if (!(dataMapEntry.getValue() instanceof Map))
                    return false;
            }

            // could go on to check Map<String,String>

        }

        return true;

    }

    @Override
    public void inject(Class c, JComponent component, String fieldName) {

        if (map.containsKey(c.getSimpleName())) {
            Map<String, Map<String, String>> valueMap = map
                    .get(c.getSimpleName());
            if (valueMap.containsKey(fieldName)) {

                Map<String, String> properties = valueMap.get(fieldName);

                if (properties.containsKey("text")) {
                    setText(component, properties.get("text"));
                }

                if (properties.containsKey("tooltip")) {
                    component.setToolTipText(properties.get("tooltip"));
                }

                if (properties.containsKey("icon") && canSetIcon(component)) {
                    setIcon(component, getIcon(c, properties.get("icon")));
                }

                if (properties
                        .containsKey("selectedIcon") && canSetSelectedIcon(component)) {
                    setSelectedIcon(component, getIcon(c, properties
                            .get("icon")));
                }

                if (properties
                        .containsKey("disabledIcon") && canSetIcon(component)) {
                    setDisabledIcon(component, getIcon(c, properties
                            .get("icon")));
                }


            }
        }

    }

}
