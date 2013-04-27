/* 
  * $Id$
 * 
 * Copyright (C) 2004-2009 Stephane GALLAND.
 * Copyright (C) 2012 Stephane GALLAND.
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 * This program is free software; you can redistribute it and/or modify
 */

package org.arakhne.vmutil;

import java.io.InputStream;
import java.util.regex.Pattern;

/**
 * Wrapper to the OS dependent functions.
 * This class was introduced to avoid to kill the current
 * JVM even if the native functions are unloadable.
 * In this way, on operating system without the support
 * for the native libs is still able to be run. 
 *
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 6.3
 */
abstract class AbstractOperatingSystemWrapper implements OperatingSystemWrapper {

	/**
	 */
	public AbstractOperatingSystemWrapper() {
		//
	}
	
	/** Run a shell command.
	 * 
	 * @param command is the sheel command.
	 * @return the standard output
	 */
	protected static String runCommand(String... command) {
		try {
			Process p = Runtime.getRuntime().exec(command);
			if (p==null) return null;
			StringBuilder bStr = new StringBuilder();
			InputStream standardOutput = p.getInputStream();
			try {
				byte[] buffer = new byte[4086];
				int len;
				while ((len=standardOutput.read(buffer))>0) {
					bStr.append(new String(buffer, 0, len));
				}
				p.waitFor();
				return bStr.toString();
			}
			finally {
				standardOutput.close();
			}
		}
		catch (Exception e) {
			return null;
		}
	}
	
	/** Replies the first line that contains the given selector.
	 * 
	 * @param selector is the string to search for.
	 * @param text is the text to search in.
	 * @return the found line or <code>null</code>.
	 */
	protected static String grep(String selector, String text) {
		if (text==null || text.isEmpty()) return null;
		StringBuilder line = new StringBuilder();
		char c;
		String s;
		for(int i=0; i<text.length(); ++i) {
			c = text.charAt(i);
			if (c=='\n' || c=='\r') {
				s = line.toString();
				if (s.contains(selector)) return s;
				line.setLength(0);
			}
			else {
				line.append(c);
			}
		}
		if (line.length()>0) {
			s = line.toString();
			if (s.contains(selector)) return s;
		}
		return null;
	}

	/** Cut the line in columns and replies the given column.
	 * 
	 * @param delimiter is the delmiter to use to cut.
	 * @param column is the number of the column to reply.
	 * @param lineText is the line to cut.
	 * @return the column or <code>null</code>.
	 */
	protected static String cut(String delimiter, int column, String lineText) {
		if (lineText==null || lineText.isEmpty()) return null;
		String[] columns = lineText.split(Pattern.quote(delimiter));
		if (columns!=null && column>=0 && column<columns.length) {
			return columns[column].trim();
		}
		return null;
	}

}
