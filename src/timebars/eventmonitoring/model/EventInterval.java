/*
 *  File: EventInterval.java 
 *  Copyright (c) 2004-2008  Peter Kliem (Peter.Kliem@jaret.de)
 *  A commercial license is available, see http://www.jaret.de.
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package timebars.eventmonitoring.model;

import de.jaret.util.date.IntervalImpl;
import de.jaret.util.date.JaretDate;

/**
 * Simple interval extension that holds a titel string.
 * 
 * @author kliem
 * @version $Id: EventInterval.java 801 2008-12-27 22:44:54Z kliem $
 */
public class EventInterval extends IntervalImpl {
    private String _title;
    private String sql;
    private String sessionId;
    private String statementType;
    private String SCN;

    
    private String osName;
    
    
    public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getOsName() {
		return osName;
	}

	public void setOsName(String osName) {
		this.osName = osName;
	}
	
	public String getSCN() {
		return this.SCN;
	}

	public void setSCN(String SCN) {
		this.SCN = SCN;
	}
	
	public String getType() {
		return statementType;
	}

	public void setType(String statementType) {
		this.statementType = statementType;
	}

	public EventInterval(JaretDate from, JaretDate to) {
        super(from, to);
    }

    public String getTitle() {
        return _title;
    }

    public void setTitle(String title) {
        _title = title;
    }
    
    public String getSql() {
        return this.sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    @Override
    public String toString() {
        return _title + ":" + super.toString();
    }

}
