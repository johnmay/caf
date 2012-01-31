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


/**
 *
 *          Version 2012.01.31
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 *
 *          Class describes a 4 digit version. The digits represent the major,
 *          minor, sub and dev version iterations. The major version can take a 
 *          value 0-255 whilst the others 0-16. The 4 digits are condensed to a 
 *          single integer index {@see getIndex()} which can easily be compared
 *          and ordered
 * 
 *
 */
public class Version {

    private static final Logger LOGGER = Logger.getLogger(Version.class);

    private int index;

    private String hex;


    /**
     * Create a new version specifying the 4 digits.
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
     * Create a new version from an index (useful for serialization)
     * @param index 
     */
    public Version(int index) {
        this.index = index;
        this.hex = String.format("%05x", index);
    }


    /**
     * Access the hexadecimal value
     * @return 
     */
    public String getHex() {
        return hex;
    }


    /**
     * Access the index
     * @return 
     */
    public int getIndex() {
        return index;
    }


    /**
     * Access the major version (first digit)
     * @return 
     */
    public int getMajorVersion() {
        return Integer.parseInt(hex.substring(0, 2), 16);
    }


    /**
     * Access the minor version (second digit)
     * @return 
     */
    public int getMinorVersion() {
        return Integer.parseInt(hex.substring(2, 3), 16);
    }


    /**
     * Access the sub version (third digit)
     * @return 
     */
    public int getSubVesion() {
        return Integer.parseInt(hex.substring(3, 4), 16);
    }


    /**
     * Access the dev version (forth digit)
     * @return 
     */
    public int getDevVersion() {
        return Integer.parseInt(hex.substring(4, 5), 16);
    }


    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();

        sb.append("v");
        sb.append(getMajorVersion()).append(".");
        sb.append(getMinorVersion()).append("/");
        sb.append(getSubVesion()).append("-");
        sb.append(getDevVersion());

        return sb.toString();

    }
}
