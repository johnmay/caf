/*
 * This file is part of Creative Application Framework (CAF).
 *
 * The Core Application Framework is free software: you can redistribute it
 * and/or modify it under the terms of the GNU Lesser General Public License
 * as published  by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * The Core Application Framework is distributed in the hope that it
 * will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Core Application Framework.  If not, 
 * see <http://www.gnu.org/licenses/>.
 * 
 */
package uk.ac.ebi.caf.utility;

import org.apache.log4j.Logger;

import javax.swing.*;
import java.io.File;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;


/**
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$ : Last Changed $Date$
 * @name ColorUtilities - 2011.10.11 <br>
 * Class description
 */
public class ResourceUtility {

    private static final Logger LOGGER = Logger.getLogger(ResourceUtility.class);

    private static final int MAX_CACHE_SIZE = 30;

    private static Map<String, ImageIcon> CACHE = new LinkedHashMap<String, ImageIcon>() {
        @Override
        protected boolean removeEldestEntry(Map.Entry<String, ImageIcon> eldest) {
            return size() >= MAX_CACHE_SIZE;
        }
    };

    public static final ImageIcon getIcon(Class root, String path) {

        String packageName = root.getPackage().getName();

        return getIcon("/" + packageName.replaceAll("\\.", "/") + File.separator + path);

    }


    public static final ImageIcon getIcon(String path) {

        ImageIcon icon = CACHE.get(path);

        if (icon == null) {
            icon = loadNewIcon(path);
        }

        // renews the cache
        CACHE.put(path, icon);

        return icon;

    }

    private static final ImageIcon loadNewIcon(String path) {

        URL url = ResourceUtility.class.getResource(path);

        if (url != null) {
            return new ImageIcon(url);
        } else {
            LOGGER.error("Unable to load image from: " + path);
            return new ImageIcon(new byte[0], path);
        }

    }
}
