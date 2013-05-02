package uk.ac.ebi.caf.utility.version;

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

import org.apache.log4j.Logger;

import java.util.regex.Pattern;


/**
 * Version 2012.01.31
 *
 * @author johnmay
 * @author $Author$ (this version)
 *         <p/>
 *         Class describes a 4 digit version. The digits represent the major,
 *         minor, sub and dev version iterations. The major version can take a
 *         value 0-255 whilst the others 0-16. The 4 digits are condensed to a
 *         single integer index {@see getIndex()} which can easily be compared
 *         and ordered
 * @version $Rev$ : Last Changed $Date$
 */
public class Version implements Comparable<Version> {

    private static final Logger LOGGER = Logger.getLogger(Version.class);

    private int index;

    private String hex;

    private Pattern split = Pattern.compile("[^\\d]+");

    /**
     * Create a new version specifying the 4 digits.
     *
     * @param i
     * @param j
     * @param k
     * @param l
     */
    public Version(int i,
                   int j,
                   int k,
                   int l) {

        hex =
                String.format("%02x", i)
                        + Integer.toHexString(j & 0xF)
                        + Integer.toHexString(k & 0xF)
                        + Integer.toHexString(l & 0xF);


        index = Integer.parseInt(hex.substring(0, 5), 16);

    }

    /**
     * Create a version instance from a string. The string is split
     * on non number characters and is used to fill up the version
     * number. If too few number are provided then the version is
     * padded with 0's. i.e  "0.9" -> "0.9.0.0". If too many digits
     * are found then the value is truncated "0.9.0.0.1" -> "0.9.0.0"
     *
     * @param version the version id to create
     */
    public Version(String version) {

        String[] values = split.split(version.replaceAll("-SNAPSHOT", ""));
        int[] base = new int[]{0, 0, 0, 0};
        for (int i = 0; i < values.length; i++) {
            base[i] = Integer.parseInt(values[i]);
        }

        hex =
                String.format("%02x", base[0])
                        + Integer.toHexString(base[1] & 0xF)
                        + Integer.toHexString(base[2] & 0xF)
                        + Integer.toHexString(base[3] & 0xF);


        index = Integer.parseInt(hex.substring(0, 5), 16);

    }

    /**
     * Create a new version from an index (useful for serialization)
     *
     * @param index
     */
    public Version(int index) {
        this.index = index;
        this.hex = String.format("%05x", index);
    }


    /**
     * Access the hexadecimal value
     *
     * @return
     */
    public String getHex() {
        return hex;
    }


    /**
     * Access the index
     *
     * @return
     */
    public int getIndex() {
        return index;
    }


    /**
     * Access the major version (first digit)
     *
     * @return
     */
    public int getMajorVersion() {
        return Integer.parseInt(hex.substring(0, 2), 16);
    }


    /**
     * Access the minor version (second digit)
     *
     * @return
     */
    public int getMinorVersion() {
        return Integer.parseInt(hex.substring(2, 3), 16);
    }


    /**
     * Access the sub version (third digit)
     *
     * @return
     */
    public int getSubVesion() {
        return Integer.parseInt(hex.substring(3, 4), 16);
    }


    /**
     * Access the dev version (forth digit)
     *
     * @return
     */
    public int getDevVersion() {
        return Integer.parseInt(hex.substring(4, 5), 16);
    }


    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();

        sb.append(getMajorVersion()).append(".");
        sb.append(getMinorVersion()).append(".");
        sb.append(getSubVesion()).append(".");
        sb.append(getDevVersion());

        return sb.toString();

    }

    @Override
    public int compareTo(Version o) {
        return ((Integer) o.getIndex()).compareTo(getIndex());
    }
}
