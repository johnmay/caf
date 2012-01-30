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

import uk.ac.ebi.caf.report.ReportLevel;
import uk.ac.ebi.caf.report.ReportLevel;


/**
 * @name    Message - 2011.10.03 <br>
 *          Interface description
 * @version $Rev: 1057 $ : Last Changed $Date: 2011-12-14 21:57:20 +0000 (Wed, 14 Dec 2011) $
 * @author  johnmay
 * @author  $Author: johnmay $ (this version)
 */
public interface Report {

    public String getMessage();


    public ReportLevel getLevel();
}
