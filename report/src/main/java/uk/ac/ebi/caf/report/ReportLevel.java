/*
 * This file is part of Core Application Framework (CAF).
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
package uk.ac.ebi.caf.report;

import org.apache.log4j.Level;


/**
 * Provides internal enumeration and mapping to Log4j levels
 * @author johnmay
 */
public enum ReportLevel {

    ALL(Level.ALL),
    DEBUG(Level.DEBUG),
    INFO(Level.INFO),
    WARN(Level.WARN),
    ERROR(Level.ERROR),
    FATAL(Level.FATAL),
    OFF(Level.OFF);

    private Level log4jLevel;


    private ReportLevel(Level level) {
        this.log4jLevel = level;
    }


    public Level getLog4jLevel() {
        return log4jLevel;
    }
}
