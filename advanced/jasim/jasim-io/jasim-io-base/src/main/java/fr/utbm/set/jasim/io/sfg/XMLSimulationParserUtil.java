/*
 * $Id$
 * 
 * Copyright (c) 2006-09, Multiagent Team - Systems and Transportation Laboratory (SeT)
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of the Systems and Transportation Laboratory ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with the SeT.
 * 
 * http://www.multiagent.fr/
 */
package fr.utbm.set.jasim.io.sfg;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Parse an XML file to extract simulation parameters.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
class XMLSimulationParserUtil implements XMLSimulationConfigConstants {

	/** List of default date formats which hare not directly supported
	 * by the JVM.
	 */
	private static final String[] DEFAULT_DATE_FORMATS = new String[]{
		"yyyy-MM-dd", //$NON-NLS-1$
		"yyyy/MM/dd", //$NON-NLS-1$
		"dd/MM/yyyy", //$NON-NLS-1$
	};
		
	/** List of default hour formats which hare not directly supported
	 * by the JVM.
	 */
	private static final String[] DEFAULT_HOUR_FORMATS = new String[]{
		"yyyy-MM-dd hh:mm:ss", //$NON-NLS-1$
		"yyyy-MM-dd hh:mm", //$NON-NLS-1$
		"yyyy/MM/dd hh:mm:ss", //$NON-NLS-1$
		"yyyy/MM/dd hh:mm", //$NON-NLS-1$
		"dd/MM/yyyy hh:mm:ss", //$NON-NLS-1$
		"dd/MM/yyyy hh:mm", //$NON-NLS-1$
	};
	
	/** Replies the Date in the given string.
	 * 
	 * @param value is the value to parse.
	 * @return the Date or <code>null</code>
	 */	
	public static Date parseDate(String value) {
		try {
			return DateFormat.getDateInstance().parse(value);
		}
		catch (ParseException e1) {
			// The current format of the date is not recognized, try a default format
			Date dt = null;
			DateFormat fmt;
			for(String format: DEFAULT_DATE_FORMATS) {
				fmt = new SimpleDateFormat(format);
				try {
					dt = fmt.parse(value);
					break;
				}
				catch (ParseException _) {
					//
				}
			}
			return dt;
		}
	}

	/** Replies the Date in the given string.
	 * 
	 * @param value is the value to parse.
	 * @return the Date or <code>null</code>
	 */	
	public static Date parseHour(String value) {
		try {
			return DateFormat.getDateInstance().parse(value);
		}
		catch (ParseException e1) {
			// The current format of the date is not recognized, try a default format
			Date dt = null;
			DateFormat fmt;
			for(String format: DEFAULT_HOUR_FORMATS) {
				fmt = new SimpleDateFormat(format);
				try {
					dt = fmt.parse(value);
					break;
				}
				catch (ParseException _) {
					//
				}
			}
			if (dt!=null) return null;
		}
		
		return parseDate(value);
	}

}
