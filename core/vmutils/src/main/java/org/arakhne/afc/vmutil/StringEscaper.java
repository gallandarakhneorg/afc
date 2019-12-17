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

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.vmutil.asserts.AssertMessages;

/** String Escaper.
*
* @author $Author: sgalland$
* @version $FullVersion$
* @mavengroupid $GroupId$
* @mavenartifactid $ArtifactId$
* @since 15.0
*/
public class StringEscaper {

	/** Java special characters.
	 */
	public static final String[][] JAVA_SPECIAL_CHARS = {
		{"\b", "\\b"}, //$NON-NLS-1$//$NON-NLS-2$
		{"\n", "\\n"}, //$NON-NLS-1$//$NON-NLS-2$
		{"\t", "\\t"}, //$NON-NLS-1$//$NON-NLS-2$
		{"\f", "\\f"}, //$NON-NLS-1$//$NON-NLS-2$
		{"\r", "\\r"}, //$NON-NLS-1$//$NON-NLS-2$
	};

	/** Java escape character.
	 */
	public static final String JAVA_ESCAPE_CHAR = "\\"; //$NON-NLS-1$

	/** Java string character.
	 */
	public static final String JAVA_STRING_CHAR = "\""; //$NON-NLS-1$

	/** Json special character.
	 */
	public static final String JSON_SPECIAL_ESCAPED_CHAR = "/"; //$NON-NLS-1$

	/** Minimal code of the valid characters for Java strings.
	 */
	public static final int JAVA_MIN_CHAR = 32;

	/** Maximal code of the valid characters for Java strings.
	 */
	public static final int JAVA_MAX_CHAR = 0x7f;

	private final Set<String> escapeCharacters = new TreeSet<>();

	private final String toEscapeCharacter;

	private final Map<String, String> specialChars = new TreeMap<>();

	private int minValidChar;

	private int maxValidChar;

	/** Constructor with the default Java escape character.
	 */
	public StringEscaper() {
		this(JAVA_ESCAPE_CHAR, JAVA_STRING_CHAR, JAVA_ESCAPE_CHAR);
	}

	/** Constructor with the given escape character.
	 *
	 * @param toEscapeCharacter the escape character within the result string.
	 * @param escapeCharacters the escape characters from the source string.
	 */
	public StringEscaper(CharSequence toEscapeCharacter, CharSequence... escapeCharacters) {
		assert toEscapeCharacter != null : AssertMessages.notNullParameter(0);
		assert toEscapeCharacter.length() > 0 : AssertMessages.invalidValue(0);
		assert escapeCharacters != null : AssertMessages.notNullParameter(1);
		assert escapeCharacters.length > 0 : AssertMessages.tooSmallArrayParameter(escapeCharacters.length, 1);
		for (final CharSequence c : escapeCharacters) {
			this.escapeCharacters.add(c.toString());
		}
		this.toEscapeCharacter = toEscapeCharacter.toString();
		setSpecialChars(JAVA_SPECIAL_CHARS);
		setValidCharRange(JAVA_MIN_CHAR, JAVA_MAX_CHAR);
	}

	/** Change the special characters.
	 *
	 * <p>The given table is an array of string pairs. The first element of each pair is
	 * the string of characters to replace. The second element of each pair is the replacement
	 * string of characters.
	 *
	 * @param chars the translation table.
	 */
	public void setSpecialChars(String[][] chars) {
		assert chars != null : AssertMessages.notNullParameter();
		this.specialChars.clear();
		for (final String[] pair : chars) {
			assert pair != null;
			assert pair.length == 2;
			assert pair[0].length() > 0;
			assert pair[1].length() > 0;
			this.specialChars.put(pair[0], pair[1]);
		}
	}

	/** Change the range of the valid characters that should not be escaped.
	 * Any character outside the given range is automatically escaped.
	 *
	 * <p>If {@code maxValidChar} is lower or equal to zero, the invalid characters
	 * are not put within the result of the escaping.
	 *
	 * @param minValidChar the code of the minimal valid character.
	 * @param maxValidChar the code of the maximal valid character.
	 */
	public void setValidCharRange(int minValidChar, int maxValidChar) {
		if (minValidChar <= maxValidChar) {
			this.minValidChar = minValidChar;
			this.maxValidChar = maxValidChar;
		} else {
			this.maxValidChar = minValidChar;
			this.minValidChar = maxValidChar;
		}
	}

	/** Escape the given text.
	 *
	 *  @param text is the text to convert.
	 *  @return the Java string for the text.
	 */
	@SuppressWarnings("checkstyle:magicnumber")
	public String escape(CharSequence text) {
		final StringBuilder result = new StringBuilder();

		for (int i = 0; i < text.length(); ++i) {
			final char c = text.charAt(i);
			final String cs = Character.toString(c);
			if (this.escapeCharacters.contains(cs)) {
				// Escape protected elements
				result.append(this.toEscapeCharacter);
				result.append(cs);
			} else {
				// Escape special characters
				final String special = this.specialChars.get(cs);
				if (special != null) {
					result.append(special);
				} else if (c < this.minValidChar || c > this.maxValidChar) {
					if (this.maxValidChar > 0) {
						// Escape invalid characters.
						result.append("\\u"); //$NON-NLS-1$
						result.append(formatHex(c, 4));
					}
				} else {
					result.append(cs);
				}
			}
		}
		return result.toString();
	}

	/** Format the given int value to hexadecimal.
	 *
	 * @param amount the value to convert.
	 * @param digits the minimal number of digits.
	 * @return a string representation of the given value.
	 */
	@Pure
	public static String formatHex(int amount, int digits) {
		final StringBuffer hex = new StringBuffer(Integer.toHexString(amount));
		while (hex.length() < digits) {
			hex.insert(0, "0"); //$NON-NLS-1$
		}
		return hex.toString();
	}

}
