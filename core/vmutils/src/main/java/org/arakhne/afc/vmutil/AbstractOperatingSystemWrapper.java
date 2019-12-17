/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2019 The original authors, and other authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.arakhne.afc.vmutil;

import java.io.InputStream;
import java.util.regex.Pattern;

/**
 * Wrapper to the OS dependent functions.
 * This class was introduced to avoid to kill the current
 * JVM even if the native functions are unloadable.
 * In this way, on operating system without the support
 * for the native libs is still able to be run.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 6.3
 */
abstract class AbstractOperatingSystemWrapper implements OperatingSystemWrapper {

	private static final int BUFFER_SIZE = 4086;

	/** Construct a wrapper.
	 */
	protected AbstractOperatingSystemWrapper() {
        super();
    }

	/** Run a shell command.
	 *
	 * @param command is the shell command to run.
	 * @return the standard output
	 */
	protected static String runCommand(String... command) {
		try {
			final Process p = Runtime.getRuntime().exec(command);
			if (p == null) {
				return null;
			}
			final StringBuilder bStr = new StringBuilder();
			try (InputStream standardOutput = p.getInputStream()) {
				final byte[] buffer = new byte[BUFFER_SIZE];
				int len;
				while ((len = standardOutput.read(buffer)) > 0) {
					bStr.append(new String(buffer, 0, len));
				}
				p.waitFor();
				return bStr.toString();
			}
		} catch (Exception e) {
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
		if (text == null || text.isEmpty()) {
			return null;
		}
		final StringBuilder line = new StringBuilder();
		final int textLength = text.length();
		char c;
		String string;
		for (int i = 0; i < textLength; ++i) {
			c = text.charAt(i);
			if (c == '\n' || c == '\r') {
				string = line.toString();
				if (string.contains(selector)) {
					return string;
				}
				line.setLength(0);
			} else {
				line.append(c);
			}
		}
		if (line.length() > 0) {
			string = line.toString();
			if (string.contains(selector)) {
				return string;
			}
		}
		return null;
	}

	/** Cut the line in columns and replies the given column.
	 *
	 * @param delimiter is the delmiter to use to cut.
	 * @param column is the number of the column to reply.
	 * @param lineText is the line to cut.
	 * @return the column or {@code null}.
	 */
	protected static String cut(String delimiter, int column, String lineText) {
		if (lineText == null || lineText.isEmpty()) {
			return null;
		}
		final String[] columns = lineText.split(Pattern.quote(delimiter));
		if (columns != null && column >= 0 && column < columns.length) {
			return columns[column].trim();
		}
		return null;
	}

}
