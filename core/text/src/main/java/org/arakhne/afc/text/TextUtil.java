/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2018 The original authors, and other authors.
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

package org.arakhne.afc.text;

import java.lang.ref.SoftReference;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.TreeMap;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.xtext.xbase.lib.Inline;
import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.vmutil.locale.Locale;

/**
 * This class permits to manipulate texts.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@SuppressWarnings("checkstyle:methodcount")
public final class TextUtil {

	private static final ReentrantLock LOCK = new ReentrantLock();

	private static SoftReference<Map<Character, String>> accentTransTbl;

	private static SoftReference<Map<String, Integer>> htmlToJavaTransTbl;

	private static SoftReference<Map<Character, String>> javaToHtmlTransTbl;

	private TextUtil() {
		//
	}

	/** Enforced version of the equality test on two strings with case ignoring.
	 * This enforced version supported <code>null</code> values
	 * given as parameters.
	 *
	 * @param firstText first text.
	 * @param secondText second text.
	 * @param isNullEmptyEquivalence indicates if the <code>null</code> value
	 *     is assimilated to the empty string.
	 * @return <code>true</code> if a is equal to b; otherwise <code>false</code>.
	 */
	@Pure
	public static boolean equalsIgnoreCase(String firstText, String secondText, boolean isNullEmptyEquivalence) {
		final String aa = (firstText != null || !isNullEmptyEquivalence) ? firstText : ""; //$NON-NLS-1$
		final String bb = (secondText != null || !isNullEmptyEquivalence) ? secondText : ""; //$NON-NLS-1$
		if (aa == null) {
			return bb == null;
		}
		if (bb == null) {
			return false;
		}
		return aa.equalsIgnoreCase(bb);
	}

	/** Replies a base 26 encoding string for the given
	 * number.
	 *
	 * @param number the number to encode.
	 * @return the base 26 encoding.
	 * @since 4.0
	 */
	@Pure
	@SuppressWarnings("checkstyle:magicnumber")
	public static String encodeBase26(int number) {
		final StringBuilder value = new StringBuilder();
		int code = number;
		do {
			final int rest = code % 26;
			value.insert(0, (char) ('A' + rest));
			code = code / 26 - 1;
		}
		while (code >= 0);
		return value.toString();
	}

	/** Replies the html-to-java's translation table.
	 *
	 * <p>This method read the translations from the
	 * resource file <code>HTML_TRANS_TBL</code>.
	 *
	 * @return the translation table or <code>null</code> if none was found.
	 *     The translation table maps an HTML entity to its corresponding ISO chararacter code.
	 * @since 4.0
	 */
	@Pure
	@SuppressWarnings("checkstyle:npathcomplexity")
	public static Map<String, Integer> getHtmlToJavaTranslationTable() {
		Map<String, Integer> map = null;
		try {
			LOCK.lock();
			if (htmlToJavaTransTbl != null) {
				map = htmlToJavaTransTbl.get();
			}
		} finally {
			LOCK.unlock();
		}

		if (map != null) {
			return map;
		}

		// Get the resource file
		ResourceBundle resource = null;
		try {
			resource = ResourceBundle.getBundle(
					TextUtil.class.getCanonicalName(),
					java.util.Locale.getDefault());
		} catch (MissingResourceException exep) {
			return null;
		}

		// get the resource string
		final String result;

		try {
			result = resource.getString("HTML_TRANS_TBL"); //$NON-NLS-1$
		} catch (Exception e) {
			return null;
		}

		map = new TreeMap<>();

		final String[] pairs = result.split("(\\}\\{)|\\{|\\}"); //$NON-NLS-1$
		Integer isoCode;
		String entity;
		String code;
		for (int i = 1; (i + 1) < pairs.length; i += 2) {
			try {
				entity = pairs[i];
				code = pairs[i + 1];
				isoCode = Integer.valueOf(code);
				if (isoCode != null) {
					map.put(entity, isoCode);
				}
			} catch (Throwable exception) {
				//
			}
		}

		try {
			LOCK.lock();
			htmlToJavaTransTbl = new SoftReference<>(map);
		} finally {
			LOCK.unlock();
		}

		return map;
	}

	/** Replies the java-to-html's translation table.
	 *
	 * <p>This method read the translations from the
	 * resource file <code>HTML_TRANS_TBL</code>.
	 *
	 * @return the translation table or <code>null</code> if none was found.
	 *     The translation table maps an ISO character code to its corresponding HTML entity.
	 * @since 4.0
	 */
	@Pure
	@SuppressWarnings("checkstyle:npathcomplexity")
	public static Map<Character, String> getJavaToHTMLTranslationTable() {
		Map<Character, String> map = null;
		try {
			LOCK.lock();
			if (javaToHtmlTransTbl != null) {
				map = javaToHtmlTransTbl.get();
			}
		} finally {
			LOCK.unlock();
		}

		if (map != null) {
			return map;
		}

		// Get the resource file
		ResourceBundle resource = null;
		try {
			resource = ResourceBundle.getBundle(
					TextUtil.class.getCanonicalName(),
					java.util.Locale.getDefault());
		} catch (MissingResourceException exep) {
			return null;
		}

		// get the resource string
		final String result;

		try {
			result = resource.getString("HTML_TRANS_TBL"); //$NON-NLS-1$
		} catch (Exception e) {
			return null;
		}

		map = new TreeMap<>();

		final String[] pairs = result.split("(\\}\\{)|\\{|\\}"); //$NON-NLS-1$
		Integer isoCode;
		String entity;
		String code;
		for (int i = 1; (i + 1) < pairs.length; i += 2) {
			try {
				entity = pairs[i];
				code = pairs[i + 1];
				isoCode = Integer.valueOf(code);
				if (isoCode != null) {
					map.put((char) isoCode.intValue(), entity);
				}
			} catch (Throwable exception) {
				//
			}
		}

		try {
			LOCK.lock();
			javaToHtmlTransTbl = new SoftReference<>(map);
		} finally {
			LOCK.unlock();
		}

		return map;
	}

	/** Parse the given HTML text and replace all
	 *  the HTML entities by the corresponding unicode
	 *  character.
	 *
	 * @param html is the HTML to convert.
	 * @return the unicode representation of the given html text.
	 * @since 4.0
	 * @see #toHTML(String)
	 */
	@Pure
	@SuppressWarnings("checkstyle:magicnumber")
	public static String parseHTML(String html) {
		if (html == null) {
			return null;
		}
		final Map<String, Integer> transTbl = getHtmlToJavaTranslationTable();
		assert transTbl != null;
		if (transTbl.isEmpty()) {
			return html;
		}
		final Pattern pattern = Pattern.compile("[&](([a-zA-Z]+)|(#x?[0-9]+))[;]"); //$NON-NLS-1$
		final Matcher matcher = pattern.matcher(html);
		final StringBuilder result = new StringBuilder();
		String entity;
		Integer isoCode;
		int lastIndex = 0;
		while (matcher.find()) {
			final int idx = matcher.start();
			result.append(html.substring(lastIndex, idx));
			lastIndex = matcher.end();
			entity = matcher.group(1);
			if (entity.startsWith("#x")) { //$NON-NLS-1$
				try {
					isoCode = Integer.valueOf(entity.substring(2), 16);
				} catch (Throwable exception) {
					isoCode = null;
				}
			} else if (entity.startsWith("#")) { //$NON-NLS-1$
				try {
					isoCode = Integer.valueOf(entity.substring(1));
				} catch (Throwable exception) {
					isoCode = null;
				}
			} else {
				isoCode = transTbl.get(entity);
			}

			if (isoCode == null) {
				result.append(matcher.group());
			} else {
				result.append((char) isoCode.intValue());
			}
		}
		if (lastIndex < html.length()) {
			result.append(html.substring(lastIndex));
		}
		return result.toString();
	}

	/** Translate all the special character from the given
	 * text to their equivalent HTML entities.
	 *
	 *  @param text is the text to convert.
	 *  @return the HTML text which is corresponding to the given text.
	 *  @since 4.0
	 *  @see #parseHTML(String)
	 */
	@Pure
	public static String toHTML(String text) {
		if (text == null) {
			return null;
		}
		final Map<Character, String> transTbl = getJavaToHTMLTranslationTable();
		assert transTbl != null;
		if (transTbl.isEmpty()) {
			return text;
		}
		final StringBuilder patternStr = new StringBuilder();
		for (final Character c : transTbl.keySet()) {
			if (patternStr.length() > 0) {
				patternStr.append("|"); //$NON-NLS-1$
			}
			patternStr.append(Pattern.quote(c.toString()));
		}
		final Pattern pattern = Pattern.compile(patternStr.toString());
		final Matcher matcher = pattern.matcher(text);
		final StringBuilder result = new StringBuilder();
		String character;
		String entity;
		int lastIndex = 0;
		while (matcher.find()) {
			final int idx = matcher.start();
			result.append(text.substring(lastIndex, idx));
			lastIndex = matcher.end();
			character = matcher.group();
			if (character.length() == 1) {
				entity = transTbl.get(Character.valueOf(character.charAt(0)));
				if (entity != null) {
					entity = "&" + entity + ";"; //$NON-NLS-1$ //$NON-NLS-2$
				} else {
					entity = character;
				}
			} else {
				entity = character;
			}
			result.append(entity);
		}
		if (lastIndex < text.length()) {
			result.append(text.substring(lastIndex));
		}
		return result.toString();
	}

	/** Format the text to be sure that each line is not
	 * more longer than the specified quantity of characters.
	 *
	 * @param text is the string to cut
	 * @param column is the column number that corresponds to the splitting point.
	 * @return the given {@code text} splitted in lines separated by <code>\n</code>.
	 */
	@Pure
	public static String cutString(String text, int column) {
		final StringBuilder buffer = new StringBuilder();
		cutStringAlgo(text, new CutStringColumnCritera(column), new CutStringToString(buffer));
		return buffer.toString();
	}

	/** Format the text to be sure that each line is not
	 * more longer than the specified quantity of characters.
	 *
	 * @param text is the string to cut
	 * @param column is the column number that corresponds to the splitting point.
	 * @return the given {@code text} splitted in lines separated by <code>\n</code>.
	 */
	@Pure
	public static String[] cutStringAsArray(String text, int column) {
		final List<String> list = new ArrayList<>();
		cutStringAlgo(text, new CutStringColumnCritera(column), new CutStringToArray(list));
		final String[] result = new String[list.size()];
		list.toArray(result);
		list.clear();
		return result;
	}

	/** Format the text to be sure that each line is not
	 * more longer than the specified critera.
	 *
	 * @param text is the string to cut
	 * @param critera is the critera to respect.
	 * @param output is the given {@code text} splitted in lines separated by <code>\n</code>.
	 * @since 4.0
	 */
	public static void cutStringAsArray(String text, CutStringCritera critera, List<String> output) {
		cutStringAlgo(text, critera, new CutStringToArray(output));
	}

	private static void cutStringAlgo(String text, CutStringCritera critera, CutStringAlgorithm algo) {
		assert critera != null;

		if (text == null || critera.getCritera() <= 0) {
			return;
		}

		assert algo != null;

		final StringBuilder line = new StringBuilder();
		final String[] lines = text.split("[\\n\\r\r\n]"); //$NON-NLS-1$
		long lineLength;
		int maxLength;

		for (int idxLine = 0; idxLine < lines.length; ++idxLine) {
			final String[] words = lines[idxLine].split("[\t \n\r\f]+"); //$NON-NLS-1$
			lineLength = 0;
			for (int i = 0; i < words.length; ++i) {
				final String word = words[i];
				if (critera.isOverfull(lineLength, word)) {
					if (line.length() > 0 || i > 0) {
						algo.addLine(line.toString());
					}
					line.setLength(0);
					line.append(word);
					// Split the word
					maxLength = critera.getCutIndexFor(line.toString());
					while (maxLength > 0) {
						algo.addLine(line.substring(0, maxLength));
						line.delete(0, maxLength);
						maxLength = critera.getCutIndexFor(line.toString());
					}
					// Append last part of the word
					lineLength = critera.getLengthFor(line.toString());
				} else {
					if (line.length() > 0) {
						line.append(' ');
						lineLength += critera.getLengthFor(" "); //$NON-NLS-1$
					}
					// Append word
					line.append(word);
					lineLength += critera.getLengthFor(word);
				}
			}
			if (line.length() > 0) {
				algo.addLine(line.toString());
				line.setLength(0);
			}
		}
	}

	/** Replies the character which follow the first '&amp;'.
	 *
	 * @param text is the text to scan.
	 * @return the character that is following the first '&amp;' or '<code>\0</code>'
	 */
	@Pure
	public static char getMnemonicChar(String text) {
		if (text != null) {
			final int pos = text.indexOf('&');
			if ((pos != -1) && (pos < text.length() - 1)) {
				return text.charAt(pos + 1);
			}
		}
		return '\0';
	}

	/** Remove the mnemonic char from the specified string.
	 *
	 * @param text is the text to scan.
	 * @return the given text without the mnemonic character.
	 */
	@Pure
	public static String removeMnemonicChar(String text) {
		if (text == null) {
			return text;
		}
		return text.replaceFirst("&", ""); //$NON-NLS-1$ //$NON-NLS-2$
	}

	/** Replies the accent's translation table.
	 *
	 * <p>This method read the translations from the
	 * resource file <code>ACCENT_TRANS_TBL</code>.
	 *
	 *  @return the translation table or <code>null</code> if none was found.
	 */
	@Pure
	@SuppressWarnings("checkstyle:npathcomplexity")
	public static Map<Character, String> getAccentTranslationTable() {
		Map<Character, String> map = null;
		try {
			LOCK.lock();
			if (accentTransTbl != null) {
				map = accentTransTbl.get();
			}
		} finally {
			LOCK.unlock();
		}

		if (map != null) {
			return map;
		}

		// Get the resource file
		ResourceBundle resource = null;
		try {
			resource = ResourceBundle.getBundle(
					TextUtil.class.getCanonicalName(),
					java.util.Locale.getDefault());
		} catch (MissingResourceException exep) {
			return null;
		}

		// get the resource string
		final String result;

		try {
			result = resource.getString("ACCENT_TRANS_TBL"); //$NON-NLS-1$
		} catch (Exception e) {
			return null;
		}

		map = new TreeMap<>();

		final String[] pairs = result.split("(\\}\\{)|\\{|\\}"); //$NON-NLS-1$
		for (final String pair : pairs) {
			if (pair.length() > 1) {
				map.put(pair.charAt(0), pair.substring(1));
			}
		}

		try {
			LOCK.lock();
			accentTransTbl = new SoftReference<>(map);
		} finally {
			LOCK.unlock();
		}

		return map;
	}

	/** Remove the accents inside the specified string.
	 *
	 * @param text is the string into which the accents must be removed.
	 * @return the given string without the accents
	 */
	@Pure
	public static String removeAccents(String text) {
		final Map<Character, String> map = getAccentTranslationTable();
		if ((map == null) || (map.isEmpty())) {
			return text;
		}
		return removeAccents(text, map);
	}

	/** Remove the accents inside the specified string.
	 *
	 * @param text is the string into which the accents must be removed.
	 * @param map is the translation table from an accentuated character to an
	 *     unaccentuated character.
	 * @return the given string without the accents
	 */
	@Pure
	public static String removeAccents(String text, Map<Character, String> map) {
		if (text == null) {
			return text;
		}
		final StringBuilder buffer = new StringBuilder();
		for (final char c : text.toCharArray()) {
			final String trans = map.get(c);
			if (trans != null) {
				buffer.append(trans);
			} else {
				buffer.append(c);
			}
		}
		return buffer.toString();
	}

	/** Split the given string according to brackets.
	 * The brackets are used to delimit the groups
	 * of characters.
	 *
	 * <p>Examples:
	 * <ul>
	 * <li><code>splitBrackets("{a}{b}{cd}")</code> returns the array
	 * <code>["a","b","cd"]</code></li>
	 * <li><code>splitBrackets("abcd")</code> returns the array
	 * <code>["abcd"]</code></li>
	 * <li><code>splitBrackets("a{bcd")</code> returns the array
	 * <code>["a","bcd"]</code></li>
	 * </ul>
	 *
	 * @param str is the strig with brackets.
	 * @return the groups of strings
	 */
	@Pure
	@Inline(value = "textUtil.split('{', '}', $1)", imported = {TextUtil.class})
	public static String[] splitBrackets(String str) {
		return split('{', '}', str);
	}

	/** Split the given string according to the separators.
	 * The separators are used to delimit the groups
	 * of characters.
	 *
	 * <p>Examples:
	 * <ul>
	 * <li><code>split('{','}',"{a}{b}{cd}")</code> returns the array
	 * <code>["a","b","cd"]</code></li>
	 * <li><code>split('{','}',"abcd")</code> returns the array
	 * <code>["abcd"]</code></li>
	 * <li><code>split('{','}',"a{bcd")</code> returns the array
	 * <code>["a","bcd"]</code></li>
	 * </ul>
	 *
	 * @param leftSeparator is the left separator.
	 * @param rightSeparator is the right separator.
	 * @param str is the strig with brackets.
	 * @return the groups of strings
	 * @since 4.0
	 */
	@Pure
	public static String[] split(char leftSeparator, char rightSeparator, String str) {
		final SplitSeparatorToArrayAlgorithm algo = new SplitSeparatorToArrayAlgorithm();
		splitSeparatorAlgorithm(leftSeparator, rightSeparator, str, algo);
		return algo.toArray();
	}

	/** Split the given string according to brackets.
	 * The brackets are used to delimit the groups
	 * of characters.
	 *
	 * <p>Examples:
	 * <ul>
	 * <li><code>splitBrackets("{a}{b}{cd}")</code> returns the array
	 * <code>["a","b","cd"]</code></li>
	 * <li><code>splitBrackets("abcd")</code> returns the array
	 * <code>["abcd"]</code></li>
	 * <li><code>splitBrackets("a{bcd")</code> returns the array
	 * <code>["a","bcd"]</code></li>
	 * </ul>
	 *
	 * @param str is the elements enclosed by backets.
	 * @return the groups of strings
	 */
	@Pure
	@Inline(value = "textUtil.splitAsList('{', '}', $1)", imported = {TextUtil.class})
	public static List<String> splitBracketsAsList(String str) {
		return splitAsList('{', '}', str);
	}

	/** Split the given string according to separators.
	 * The separators are used to delimit the groups
	 * of characters.
	 *
	 * <p>Examples:
	 * <ul>
	 * <li><code>split('{','}',"{a}{b}{cd}")</code> returns the array
	 * <code>["a","b","cd"]</code></li>
	 * <li><code>split('{','}',"abcd")</code> returns the array
	 * <code>["abcd"]</code></li>
	 * <li><code>split('{','}',"a{bcd")</code> returns the array
	 * <code>["a","bcd"]</code></li>
	 * </ul>
	 *
	 * @param leftSeparator is the left separator.
	 * @param rightSeparator is the right separator.
	 * @param str is the elements enclosed by backets.
	 * @return the groups of strings
	 */
	@Pure
	public static List<String> splitAsList(char leftSeparator, char rightSeparator, String str) {
		final List<String> list = new ArrayList<>();
		splitSeparatorAlgorithm(
				leftSeparator, rightSeparator, str,
				new SplitSeparatorToListAlgorithm(list));
		return list;
	}

	@SuppressWarnings({"checkstyle:cyclomaticcomplexity", "checkstyle:npathcomplexity"})
	private static void splitSeparatorAlgorithm(
			char leftSeparator, char rightSeparator,
			String str, SplitSeparatorAlgorithm buffer) {
		assert buffer != null;
		if (str != null && str.length() > 0) {
			final StringBuilder patternStr = new StringBuilder();
			patternStr.append("([^\\"); //$NON-NLS-1$
			patternStr.append(leftSeparator);
			patternStr.append("\\"); //$NON-NLS-1$
			patternStr.append(rightSeparator);
			patternStr.append("]*)(\\"); //$NON-NLS-1$
			patternStr.append(leftSeparator);
			patternStr.append("|\\"); //$NON-NLS-1$
			patternStr.append(rightSeparator);
			patternStr.append(")"); //$NON-NLS-1$
			final Pattern pattern = Pattern.compile(patternStr.toString());
			final Matcher matcher = pattern.matcher(str);

			// inclusive
			int startOffset = 0;
			// exclusive
			int endOffset;
			int depth = 0;

			final StringBuilder token = new StringBuilder();

			while (matcher.find()) {
				final String previousText = matcher.group(1);
				final String separator = matcher.group(2);
				endOffset = startOffset + previousText.length();

				if (startOffset < str.length() && endOffset > startOffset) {
					token.append(str.substring(startOffset, endOffset));
				}

				if (separator.equals(Character.toString(leftSeparator))) {
					if (depth > 0) {
						token.append(separator);
					} else if (token.length() > 0) {
						final String s = token.toString().trim();
						if (s.length() > 0) {
							buffer.addToken(s);
						}
						token.setLength(0);
					}
					++depth;
				} else if (separator.equals(Character.toString(rightSeparator))) {
					if (depth == 0) {
						token.append(separator);
					} else {
						--depth;
						if (depth > 0) {
							token.append(separator);
						} else {
							buffer.addToken(token.toString());
							token.setLength(0);
						}
					}
				} else {
					throw new IllegalStateException();
				}

				startOffset = endOffset + separator.length();
			}

			if (startOffset < str.length()) {
				token.append(str.substring(startOffset));
			}

			if (token.length() > 0) {
				final String s = token.toString().trim();
				if (s.length() > 0) {
					buffer.addToken(s);
				}
			}
		}
	}

	/** Split the given string according to brackets.
	 * The brackets are used to delimit the groups
	 * of characters.
	 *
	 * <p>Examples:
	 * <ul>
	 * <li><code>splitBrackets("{a}{b}{cd}")</code> returns the array
	 * <code>["a","b","cd"]</code></li>
	 * <li><code>splitBrackets("abcd")</code> returns the array
	 * <code>["abcd"]</code></li>
	 * <li><code>splitBrackets("a{bcd")</code> returns the array
	 * <code>["a","bcd"]</code></li>
	 * </ul>
	 *
	 * @param str is the elements enclosed by backets.
	 * @return the groups of strings
	 */
	@Pure
	@Inline(value = "textUtil.splitAsUUIDs('{', '}', $1)", imported = {TextUtil.class})
	public static List<UUID> splitBracketsAsUUIDs(String str) {
		return splitAsUUIDs('{', '}', str);
	}

	/** Split the given string according to separators.
	 * The separators are used to delimit the groups
	 * of characters.
	 *
	 * <p>Examples:
	 * <ul>
	 * <li><code>split('{','}',"{a}{b}{cd}")</code> returns the array
	 * <code>["a","b","cd"]</code></li>
	 * <li><code>split('{','}',"abcd")</code> returns the array
	 * <code>["abcd"]</code></li>
	 * <li><code>split('{','}',"a{bcd")</code> returns the array
	 * <code>["a","bcd"]</code></li>
	 * </ul>
	 *
	 * @param leftSeparator is the left separator.
	 * @param rightSeparator is the right separator.
	 * @param str is the elements enclosed by backets.
	 * @return the groups of strings
	 * @since 4.0
	 */
	@Pure
	public static List<UUID> splitAsUUIDs(char leftSeparator, char rightSeparator, String str) {
		final List<UUID> list = new ArrayList<>();
		splitSeparatorAlgorithm(leftSeparator, rightSeparator, str, new UUIDSplitSeparatorAlgorithm(list));
		return list;
	}

	/** Merge the given strings with to brackets.
	 * The brackets are used to delimit the groups
	 * of characters.
	 *
	 * <p>Examples:
	 * <ul>
	 * <li><code>mergeBrackets("a","b","cd")</code> returns the string
	 * <code>"{a}{b}{cd}"</code></li>
	 * <li><code>mergeBrackets("a{bcd")</code> returns the string
	 * <code>"{a{bcd}"</code></li>
	 * </ul>
	 *
	 * @param <T> is the type of the parameters.
	 * @param strs is the array of strings.
	 * @return the string with bracketed strings.
	 */
	@Pure
	@Inline(value = "TextUtil.join('{', '}', $1)", imported = {TextUtil.class})
	public static <T> String mergeBrackets(@SuppressWarnings("unchecked") T... strs) {
		return join('{', '}', strs);
	}

	/** Merge the given strings with to brackets.
	 * The brackets are used to delimit the groups
	 * of characters.
	 *
	 * <p>Examples:
	 * <ul>
	 * <li><code>mergeBrackets("a","b","cd")</code> returns the string
	 * <code>"{a}{b}{cd}"</code></li>
	 * <li><code>mergeBrackets("a{bcd")</code> returns the string
	 * <code>"{a{bcd}"</code></li>
	 * </ul>
	 *
	 * @param strs is the array of strings.
	 * @return the string with bracketed strings.
	 * @see #join(char, char, Iterable)
	 */
	@Pure
	@Inline(value = "TextUtil.join('{', '}', $1)", imported = {TextUtil.class})
	public static String mergeBrackets(Iterable<?> strs) {
		return join('{', '}', strs);
	}

	/** Merge the given strings with to separators.
	 * The separators are used to delimit the groups
	 * of characters.
	 *
	 * <p>Examples:
	 * <ul>
	 * <li><code>merge('{','}',"a","b","cd")</code> returns the string
	 * <code>"{a}{b}{cd}"</code></li>
	 * <li><code>merge('{','}',"a{bcd")</code> returns the string
	 * <code>"{a{bcd}"</code></li>
	 * </ul>
	 *
	 * @param <T> is the type of the parameters.
	 * @param leftSeparator is the left separator to use.
	 * @param rightSeparator is the right separator to use.
	 * @param strs is the array of strings.
	 * @return the string with merged strings.
	 * @since 4.0
	 */
	@Pure
	public static <T> String join(char leftSeparator, char rightSeparator, @SuppressWarnings("unchecked") T... strs) {
		final StringBuilder buffer = new StringBuilder();
		for (final Object s : strs) {
			buffer.append(leftSeparator);
			if (s != null) {
				buffer.append(s.toString());
			}
			buffer.append(rightSeparator);
		}
		return buffer.toString();
	}

	/** Merge the given strings with to separators.
	 * The separators are used to delimit the groups
	 * of characters.
	 *
	 * <p>Examples:
	 * <ul>
	 * <li><code>merge('{','}',"a","b","cd")</code> returns the string
	 * <code>"{a}{b}{cd}"</code></li>
	 * <li><code>merge('{','}',"a{bcd")</code> returns the string
	 * <code>"{a{bcd}"</code></li>
	 * </ul>
	 *
	 * @param leftSeparator is the left separator to use.
	 * @param rightSeparator is the right separator to use.
	 * @param strs is the array of strings.
	 * @return the string with merged strings.
	 * @since 4.0
	 */
	@Pure
	public static String join(char leftSeparator, char rightSeparator, Iterable<?> strs) {
		final StringBuilder buffer = new StringBuilder();
		for (final Object s : strs) {
			buffer.append(leftSeparator);
			if (s != null) {
				buffer.append(s.toString());
			}
			buffer.append(rightSeparator);
		}
		return buffer.toString();
	}

	/** Join the elements of the given array with the given join text.
	 *
	 * @param <T> is the type of the elements
	 * @param joinText the text to use for joining.
	 * @param elements the parts of text to join.
	 * @return the joining text
	 */
	@Pure
	@Inline(value = "TextUtil.join($1, null, null, Arrays.asList($2))", imported = {TextUtil.class, Arrays.class})
	public static <T> String join(String joinText, @SuppressWarnings("unchecked") T... elements) {
		return join(joinText, null, null, elements);
	}

	/** Join the elements of the given array with the given join text.
	 *
	 * @param joinText the text to use for joining.
	 * @param elements the parts of text to join.
	 * @return the joining text
	 */
	@Pure
	@Inline(value = "TextUtil.join($1, null, null, $2)", imported = {TextUtil.class})
	public static String join(String joinText, boolean... elements) {
		return join(joinText, null, null, elements);
	}

	/** Join the elements of the given array with the given join text.
	 *
	 * @param joinText the text to use for joining.
	 * @param elements the parts of text to join.
	 * @return the joining text
	 */
	@Pure
	@Inline(value = "TextUtil.join($1, null, null, $2)", imported = {TextUtil.class})
	public static String join(String joinText, byte... elements) {
		return join(joinText, null, null, elements);
	}

	/** Join the elements of the given array with the given join text.
	 *
	 * @param joinText the text to use for joining.
	 * @param elements the parts of text to join.
	 * @return the joining text
	 */
	@Pure
	@Inline(value = "TextUtil.join($1, null, null, $2)", imported = {TextUtil.class})
	public static String join(String joinText, char... elements) {
		return join(joinText, null, null, elements);
	}

	/** Join the elements of the given array with the given join text.
	 *
	 * @param joinText the text to use for joining.
	 * @param elements the parts of text to join.
	 * @return the joining text
	 */
	@Pure
	@Inline(value = "TextUtil.join($1, null, null, $2)", imported = {TextUtil.class})
	public static String join(String joinText, short... elements) {
		return join(joinText, null, null, elements);
	}

	/** Join the elements of the given array with the given join text.
	 *
	 * @param joinText the text to use for joining.
	 * @param elements the parts of text to join.
	 * @return the joining text
	 */
	@Pure
	@Inline(value = "TextUtil.join($1, null, null, $2)", imported = {TextUtil.class})
	public static String join(String joinText, int... elements) {
		return join(joinText, null, null, elements);
	}

	/** Join the elements of the given array with the given join text.
	 *
	 * @param joinText the text to use for joining.
	 * @param elements the parts of text to join.
	 * @return the joining text
	 */
	@Pure
	@Inline(value = "TextUtil.join($1, null, null, $2)", imported = {TextUtil.class})
	public static String join(String joinText, long... elements) {
		return join(joinText, null, null, elements);
	}

	/** Join the elements of the given array with the given join text.
	 *
	 * @param joinText the text to use for joining.
	 * @param elements the parts of text to join.
	 * @return the joining text
	 */
	@Pure
	@Inline(value = "TextUtil.join($1, null, null, $2)", imported = {TextUtil.class})
	public static String join(String joinText, float... elements) {
		return join(joinText, null, null, elements);
	}

	/** Join the elements of the given array with the given join text.
	 *
	 * @param joinText the text to use for joining.
	 * @param elements the parts of text to join.
	 * @return the joining text
	 */
	@Pure
	@Inline(value = "TextUtil.join($1, null, null, $2)", imported = {TextUtil.class})
	public static String join(String joinText, double... elements) {
		return join(joinText, null, null, elements);
	}

	/** Join the elements of the given array with the given join text.
	 *
	 * @param joinText the text to use for joining.
	 * @param elements the parts of text to join.
	 * @return the joining text
	 */
	@Pure
	@Inline(value = "TextUtil.join($1, null, null, $2)", imported = {TextUtil.class})
	public static String join(String joinText, Iterable<?> elements) {
		return join(joinText, null, null, elements);
	}

	/** Join the elements of the given array with the given join text.
	 * The {@code prefix} and {@code postfix} values will be put
	 * just before and just after each element respectively.
	 *
	 * @param <T> is the type of the elements
	 *
	 * @param joinText the text to use for joining.
	 * @param prefix the text to put as prefix.
	 * @param postfix the text to put as postfix.
	 * @param elements the parts of text to join.
	 * @return the joining text
	 */
	@Pure
	@Inline(value = "TextUtil.join($1, $2, $3, Arrays.asList($4))", imported = {TextUtil.class, Arrays.class})
	public static <T> String join(String joinText, String prefix, String postfix, @SuppressWarnings("unchecked") T... elements) {
		return join(joinText, prefix, postfix, Arrays.asList(elements));
	}

	/** Join the elements of the given array with the given join text.
	 * The {@code prefix} and {@code postfix} values will be put
	 * just before and just after each element respectively.
	 *
	 * @param joinText the text to use for joining.
	 * @param prefix the text to put as prefix.
	 * @param postfix the text to put as postfix.
	 * @param elements the parts of text to join.
	 * @return the joining text
	 */
	@Pure
	public static String join(String joinText, String prefix, String postfix, Iterable<?> elements) {
		final StringBuilder buffer = new StringBuilder();
		String txt;
		for (final Object e : elements) {
			if (e != null) {
				txt = e.toString();
				if (txt != null && txt.length() > 0) {
					if (buffer.length() > 0) {
						buffer.append(joinText);
					}
					if (prefix != null) {
						buffer.append(prefix);
					}
					buffer.append(txt);
					if (postfix != null) {
						buffer.append(postfix);
					}
				}
			}
		}
		return buffer.toString();
	}

	/** Join the elements of the given array with the given join text.
	 * The {@code prefix} and {@code postfix} values will be put
	 * just before and just after each element respectively.
	 *
	 * @param joinText the text to use for joining.
	 * @param prefix the text to put as prefix.
	 * @param postfix the text to put as postfix.
	 * @param elements the parts of text to join.
	 * @return the joining text
	 */
	@Pure
	public static String join(String joinText, String prefix, String postfix, boolean... elements) {
		final StringBuilder buffer = new StringBuilder();
		for (final boolean e : elements) {
			if (buffer.length() > 0) {
				buffer.append(joinText);
			}
			if (prefix != null) {
				buffer.append(prefix);
			}
			buffer.append(e);
			if (postfix != null) {
				buffer.append(postfix);
			}
		}
		return buffer.toString();
	}

	/** Join the elements of the given array with the given join text.
	 * The {@code prefix} and {@code postfix} values will be put
	 * just before and just after each element respectively.
	 *
	 * @param joinText the text to use for joining.
	 * @param prefix the text to put as prefix.
	 * @param postfix the text to put as postfix.
	 * @param elements the parts of text to join.
	 * @return the joining text
	 */
	@Pure
	public static String join(String joinText, String prefix, String postfix, byte... elements) {
		final StringBuilder buffer = new StringBuilder();
		for (final byte e : elements) {
			if (buffer.length() > 0) {
				buffer.append(joinText);
			}
			if (prefix != null) {
				buffer.append(prefix);
			}
			buffer.append(e);
			if (postfix != null) {
				buffer.append(postfix);
			}
		}
		return buffer.toString();
	}

	/** Join the elements of the given array with the given join text.
	 * The {@code prefix} and {@code postfix} values will be put
	 * just before and just after each element respectively.
	 *
	 * @param joinText the text to use for joining.
	 * @param prefix the text to put as prefix.
	 * @param postfix the text to put as postfix.
	 * @param elements the parts of text to join.
	 * @return the joining text
	 */
	@Pure
	public static String join(String joinText, String prefix, String postfix, char... elements) {
		final StringBuilder buffer = new StringBuilder();
		for (final char e : elements) {
			if (buffer.length() > 0) {
				buffer.append(joinText);
			}
			if (prefix != null) {
				buffer.append(prefix);
			}
			buffer.append(e);
			if (postfix != null) {
				buffer.append(postfix);
			}
		}
		return buffer.toString();
	}

	/** Join the elements of the given array with the given join text.
	 * The {@code prefix} and {@code postfix} values will be put
	 * just before and just after each element respectively.
	 *
	 * @param joinText the text to use for joining.
	 * @param prefix the text to put as prefix.
	 * @param postfix the text to put as postfix.
	 * @param elements the parts of text to join.
	 * @return the joining text
	 */
	@Pure
	public static String join(String joinText, String prefix, String postfix, short... elements) {
		final StringBuilder buffer = new StringBuilder();
		for (final short e : elements) {
			if (buffer.length() > 0) {
				buffer.append(joinText);
			}
			if (prefix != null) {
				buffer.append(prefix);
			}
			buffer.append(e);
			if (postfix != null) {
				buffer.append(postfix);
			}
		}
		return buffer.toString();
	}

	/** Join the elements of the given array with the given join text.
	 * The {@code prefix} and {@code postfix} values will be put
	 * just before and just after each element respectively.
	 *
	 * @param joinText the text to use for joining.
	 * @param prefix the text to put as prefix.
	 * @param postfix the text to put as postfix.
	 * @param elements the parts of text to join.
	 * @return the joining text
	 */
	@Pure
	public static String join(String joinText, String prefix, String postfix, int... elements) {
		final StringBuilder buffer = new StringBuilder();
		for (final int e : elements) {
			if (buffer.length() > 0) {
				buffer.append(joinText);
			}
			if (prefix != null) {
				buffer.append(prefix);
			}
			buffer.append(e);
			if (postfix != null) {
				buffer.append(postfix);
			}
		}
		return buffer.toString();
	}

	/** Join the elements of the given array with the given join text.
	 * The {@code prefix} and {@code postfix} values will be put
	 * just before and just after each element respectively.
	 *
	 * @param joinText the text to use for joining.
	 * @param prefix the text to put as prefix.
	 * @param postfix the text to put as postfix.
	 * @param elements the parts of text to join.
	 * @return the joining text
	 */
	@Pure
	public static String join(String joinText, String prefix, String postfix, long... elements) {
		final StringBuilder buffer = new StringBuilder();
		for (final long e : elements) {
			if (buffer.length() > 0) {
				buffer.append(joinText);
			}
			if (prefix != null) {
				buffer.append(prefix);
			}
			buffer.append(e);
			if (postfix != null) {
				buffer.append(postfix);
			}
		}
		return buffer.toString();
	}

	/** Join the elements of the given array with the given join text.
	 * The {@code prefix} and {@code postfix} values will be put
	 * just before and just after each element respectively.
	 *
	 * @param joinText the text to use for joining.
	 * @param prefix the text to put as prefix.
	 * @param postfix the text to put as postfix.
	 * @param elements the parts of text to join.
	 * @return the joining text
	 */
	@Pure
	public static String join(String joinText, String prefix, String postfix, float... elements) {
		final StringBuilder buffer = new StringBuilder();
		for (final float e : elements) {
			if (buffer.length() > 0) {
				buffer.append(joinText);
			}
			if (prefix != null) {
				buffer.append(prefix);
			}
			buffer.append(e);
			if (postfix != null) {
				buffer.append(postfix);
			}
		}
		return buffer.toString();
	}

	/** Join the elements of the given array with the given join text.
	 * The {@code prefix} and {@code postfix} values will be put
	 * just before and just after each element respectively.
	 *
	 * @param joinText the text to use for joining.
	 * @param prefix the text to put as prefix.
	 * @param postfix the text to put as postfix.
	 * @param elements the parts of text to join.
	 * @return the joining text
	 */
	@Pure
	public static String join(String joinText, String prefix, String postfix, double... elements) {
		final StringBuilder buffer = new StringBuilder();
		for (final double e : elements) {
			if (buffer.length() > 0) {
				buffer.append(joinText);
			}
			if (prefix != null) {
				buffer.append(prefix);
			}
			buffer.append(e);
			if (postfix != null) {
				buffer.append(postfix);
			}
		}
		return buffer.toString();
	}

	/**
	 * Compares this <code>String</code> to another <code>String</code>,
	 * ignoring accent considerations.  Two strings are considered equal
	 * ignoring accents if they are of the same length, and corresponding
	 * characters in the two strings are equal ignoring accents.
	 *
	 * <p>This method is equivalent to:
	 * <pre><code>
	 * TextUtil.removeAccents(s1,map).equals(TextUtil.removeAccents(s2,map));
	 * </code></pre>
	 *
	 * @param   s1 is the first string to compare.
	 * @param   s2 is the second string to compare.
	 * @param map is the translation table from an accentuated character to an
	 *     unaccentuated character.
	 * @return  <code>true</code> if the argument is not <code>null</code>
	 *          and the <code>String</code>s are equal,
	 *          ignoring case; <code>false</code> otherwise.
	 * @see     #removeAccents(String, Map)
	 */
	@Pure
	@Inline(value = "TextUtil.removeAccents($1, $3).equals(TextUtil.removeAccents($2, $3))", imported = {TextUtil.class})
	public static boolean equalsIgnoreAccents(String s1, String s2, Map<Character, String> map) {
		return removeAccents(s1, map).equals(removeAccents(s2, map));
	}

	/**
	 * Compares this <code>String</code> to another <code>String</code>,
	 * ignoring case and accent considerations.  Two strings are considered equal
	 * ignoring case and accents if they are of the same length, and corresponding
	 * characters in the two strings are equal ignoring case and accents.
	 *
	 * <p>This method is equivalent to:
	 * <pre><code>
	 * TextUtil.removeAccents(s1,map).equalsIgnoreCase(TextUtil.removeAccents(s2,map));
	 * </code></pre>
	 *
	 * @param   s1 is the first string to compare.
	 * @param   s2 is the second string to compare.
	 * @param map is the translation table from an accentuated character to an
	 *     unaccentuated character.
	 * @return  <code>true</code> if the argument is not <code>null</code>
	 *          and the <code>String</code>s are equal,
	 *          ignoring case; <code>false</code> otherwise.
	 * @see     #removeAccents(String, Map)
	 */
	@Pure
	@Inline(value = "TextUtil.removeAccents($1, $3).equalsIgnoreCase(TextUtil.removeAccents($2, $3))",
			imported = {TextUtil.class})
	public static boolean equalsIgnoreCaseAccents(String s1, String s2, Map<Character, String> map) {
		return removeAccents(s1, map).equalsIgnoreCase(removeAccents(s2, map));
	}

	/** Translate the specified string to lower case and remove the accents.
	 *
	 * @param text is the text to scan.
	 * @return the given string without the accents and lower cased
	 */
	@Pure
	public static String toLowerCaseWithoutAccent(String text) {
		final Map<Character, String> map = getAccentTranslationTable();
		if ((map == null) || (map.isEmpty())) {
			return text;
		}
		return toLowerCaseWithoutAccent(text, map);
	}

	/** Translate the specified string to lower case and remove the accents.
	 *
	 * @param text is the text to scan.
	 * @param map is the translation table from an accentuated character to an
	 *     unaccentuated character.
	 * @return the given string without the accents and lower cased
	 */
	@Pure
	public static String toLowerCaseWithoutAccent(String text, Map<Character, String> map) {
		final StringBuilder buffer = new StringBuilder();
		for (final char c : text.toCharArray()) {
			final String trans = map.get(c);
			if (trans != null) {
				buffer.append(trans.toLowerCase());
			} else {
				buffer.append(Character.toLowerCase(c));
			}
		}
		return buffer.toString();
	}

	/** Translate the specified string to upper case and remove the accents.
	 *
	 * @param text is the text to scan.
	 * @return the given string without the accents and upper cased
	 */
	@Pure
	public static String toUpperCaseWithoutAccent(String text) {
		final Map<Character, String> map = getAccentTranslationTable();
		if ((map == null) || (map.isEmpty())) {
			return text;
		}
		return toUpperCaseWithoutAccent(text, map);
	}

	/** Translate the specified string to upper case and remove the accents.
	 *
	 * @param text is the text to scan.
	 * @param map is the translation table from an accentuated character to an
	 *     unaccentuated character.
	 * @return the given string without the accents and upper cased
	 */
	@Pure
	public static String toUpperCaseWithoutAccent(String text, Map<Character, String> map) {
		final StringBuilder buffer = new StringBuilder();
		for (final char c : text.toCharArray()) {
			final String trans = map.get(c);
			if (trans != null) {
				buffer.append(trans.toUpperCase());
			} else {
				buffer.append(Character.toUpperCase(c));
			}
		}
		return buffer.toString();
	}

	/** Compute the better metric representing
	 * the given time amount and reply a string representation
	 * of the given amount with this selected unit.
	 *
	 * <p>This function try to use a greater metric unit.
	 *
	 * @param amount is the amount expressed in the given unit.
	 * @param unit is the unit of the given amount.
	 * @return a string representation of the given amount.
	 */
	@Pure
	@SuppressWarnings({"checkstyle:magicnumber", "checkstyle:cyclomaticcomplexity", "checkstyle:npathcomplexity"})
	public static String formatTime(double amount, TimeUnit unit) {
		double amt;
		double coef = 1.;
		switch (unit) {
		case DAYS:
			coef = 86400.;
			break;
		case HOURS:
			coef = 3600.;
			break;
		case MINUTES:
			coef = 60.;
			break;
		case SECONDS:
			break;
		case MILLISECONDS:
			coef = 1e-3;
			break;
		case MICROSECONDS:
			coef = 1e-6;
			break;
		case NANOSECONDS:
			coef = 1e-9;
			break;
		default:
			throw new IllegalArgumentException();
		}

		// amount is in seconds
		amt = amount * coef;

		final StringBuilder text = new StringBuilder();

		String centuries = ""; //$NON-NLS-1$
		String years = ""; //$NON-NLS-1$
		String days = ""; //$NON-NLS-1$
		String hours = ""; //$NON-NLS-1$
		String minutes = ""; //$NON-NLS-1$
		String seconds = ""; //$NON-NLS-1$
		long ah = 0;
		long am = 0;
		long as = 0;
		int idx = 0;

		if (amt >= 3153600000.) {
			final long a = (long) Math.floor(amt / 3153600000.);
			centuries = Locale.getString((a >= 2) ? "TIME_FORMAT_Cs" : "TIME_FORMAT_C", //$NON-NLS-1$ //$NON-NLS-2$
					Long.toString(a));
			amt -= a * 3153600000.;
			text.append(centuries);
			idx |= 32;
		}

		if (amt >= 31536000.) {
			final long a = (long) Math.floor(amt / 31536000.);
			years = Locale.getString((a >= 2) ? "TIME_FORMAT_Ys" : "TIME_FORMAT_Y", //$NON-NLS-1$ //$NON-NLS-2$
					Long.toString(a));
			amt -= a * 31536000.;
			if (text.length() > 0) {
				text.append(' ');
			}
			text.append(years);
			idx |= 16;
		}

		if (amt >= 86400.) {
			final long a = (long) Math.floor(amt / 86400.);
			days = Locale.getString((a >= 2) ? "TIME_FORMAT_Ds" : "TIME_FORMAT_D", //$NON-NLS-1$ //$NON-NLS-2$
					Long.toString(a));
			amt -= a * 86400.;
			if (text.length() > 0) {
				text.append(' ');
			}
			text.append(days);
			idx |= 8;
		}

		//-------------------

		if (amt >= 3600.) {
			ah = (long) Math.floor(amt / 3600.);
			hours = Long.toString(ah);
			if (ah < 10.) {
				hours = "0" + hours; //$NON-NLS-1$
			}
			amt -= ah * 3600.;
			idx |= 4;
		}

		if (amt >= 60.) {
			am = (long) Math.floor(amt / 60.);
			minutes = Long.toString(am);
			if (am < 10.) {
				minutes = "0" + minutes; //$NON-NLS-1$
			}
			amt -= am * 60.;
			idx |= 2;
		}

		if (amt >= 0. || idx == 0) {
			if (idx >= 8) {
				as = (long) Math.floor(amt);
				seconds = Long.toString(as);
			} else {
				final NumberFormat fmt = new DecimalFormat("#0.000"); //$NON-NLS-1$
				seconds = fmt.format(amt);
			}
			idx |= 1;
		}

		if ((idx & 7) == 7) {
			if (text.length() > 0) {
				text.append(' ');
			}
			if (idx >= 8 && as > 0) {
				if (as < 10.) {
					seconds = "0" + seconds; //$NON-NLS-1$
				}
			} else if (idx < 8 && amt > 0. && amt < 10.) {
				seconds = "0" + seconds; //$NON-NLS-1$
			}
			text.append(Locale.getString("TIME_FORMAT_HMS", hours, minutes, seconds)); //$NON-NLS-1$
		} else {
			if (ah > 0) {
				if (text.length() > 0) {
					text.append(' ');
				}
				text.append(Locale.getString((ah >= 2) ? "TIME_FORMAT_Hs" : "TIME_FORMAT_H", //$NON-NLS-1$ //$NON-NLS-2$
						hours));
			}
			if (am > 0) {
				if (text.length() > 0) {
					text.append(' ');
				}
				text.append(Locale.getString((am >= 2) ? "TIME_FORMAT_Ms" : "TIME_FORMAT_M", //$NON-NLS-1$ //$NON-NLS-2$
						minutes));
			}
			if (idx >= 8 && as > 0) {
				if (text.length() > 0) {
					text.append(' ');
				}
				text.append(Locale.getString((as >= 2) ? "TIME_FORMAT_Ss" : "TIME_FORMAT_S", //$NON-NLS-1$ //$NON-NLS-2$
						seconds));
			} else if (idx < 8 && amt > 0.) {
				if (text.length() > 0) {
					text.append(' ');
				}
				text.append(Locale.getString((amt >= 2.) ? "TIME_FORMAT_Ss" : "TIME_FORMAT_S", //$NON-NLS-1$ //$NON-NLS-2$
						seconds));
			}
		}

		return text.toString();
	}

	/** Format the given double value.
	 *
	 * @param amount the value to convert.
	 * @param decimalCount is the maximal count of decimal to put in the string.
	 * @return a string representation of the given value.
	 */
	@Pure
	public static String formatDouble(double amount, int decimalCount) {
		final int dc = (decimalCount < 0) ? 0 : decimalCount;

		final StringBuilder str = new StringBuilder("#0"); //$NON-NLS-1$

		if (dc > 0) {
			str.append('.');
			for (int i = 0; i < dc; ++i) {
				str.append('0');
			}
		}

		final DecimalFormat fmt = new DecimalFormat(str.toString());
		return fmt.format(amount);
	}

	/** Format the given float value.
	 *
	 * @param amount the value to convert.
	 * @param decimalCount is the maximal count of decimal to put in the string.
	 * @return a string representation of the given value.
	 */
	@Pure
	public static String formatFloat(float amount, int decimalCount) {
		final int dc = (decimalCount < 0) ? 0 : decimalCount;

		final StringBuilder str = new StringBuilder("#0"); //$NON-NLS-1$

		if (dc > 0) {
			str.append('.');
			for (int i = 0; i < dc; ++i) {
				str.append('0');
			}
		}

		final DecimalFormat fmt = new DecimalFormat(str.toString());
		return fmt.format(amount);
	}

	/** Compute the Levenshstein distance between two strings.
	 *
	 * <p>Null string is assimilated to the empty string.
	 *
	 * @param firstString first string.
	 * @param secondString second string.
	 * @return the Levenshstein distance.
	 * @see "https://en.wikibooks.org/wiki/Algorithm_Implementation/Strings/Levenshtein_distance"
	 */
	public static int getLevenshteinDistance(String firstString, String secondString) {
		final String s0 = firstString == null ? "" : firstString; //$NON-NLS-1$
		final String s1 = secondString == null ? "" : secondString; //$NON-NLS-1$

		final int len0 = s0.length() + 1;
		final int len1 = s1.length() + 1;

		// the array of distances
		int[] cost = new int[len0];
		int[] newcost = new int[len0];

		// initial cost of skipping prefix in String s0
		for (int i = 0; i < len0; ++i) {
			cost[i] = i;
		}

		// dynamically computing the array of distances

		// transformation cost for each letter in s1
		for (int j = 1; j < len1; ++j) {
			// initial cost of skipping prefix in String s1
			newcost[0] = j;

			// transformation cost for each letter in s0
			for (int i = 1; i < len0; ++i) {
				// matching current letters in both strings
				final int match = (s0.charAt(i - 1) == s1.charAt(j - 1)) ? 0 : 1;

				// computing cost for each transformation
				final int costReplace = cost[i - 1] + match;
				final int costInsert  = cost[i] + 1;
				final int costDelete  = newcost[i - 1] + 1;

				// keep minimum cost
				newcost[i] = Math.min(Math.min(costInsert, costDelete), costReplace);
			}

			// swap cost/newcost arrays
			final int[] swap = cost;
			cost = newcost;
			newcost = swap;
		}

		// the distance is the cost for transforming all letters in both strings
		return cost[len0 - 1];
	}

	/**
	 * Algorithm interface used by cut string functions to provide
	 * a buffer filler.
	 *
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	@FunctionalInterface
	private interface CutStringAlgorithm {

		/** Add a line to the output buffer.
		 *
		 * @param line the line to add.
		 */
		void addLine(String line);

	}

	/**
	 * Algorithm interface used by split bracket functions to provide
	 * a buffer filler.
	 *
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	@FunctionalInterface
	private interface SplitSeparatorAlgorithm {

		/** Add a token to the output buffer.
		 *
		 * @param token the token to add.
		 */
		void addToken(String token);

	}

	/** Algorithm implementation.
	 *
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	private static class CutStringToArray implements CutStringAlgorithm {

		private final List<String> buffer;

		/**
		 * @param buffer is the buffer to fill.
		 */
		CutStringToArray(List<String> buffer) {
			this.buffer = buffer;
		}

		@Override
		public void addLine(String line) {
			this.buffer.add(line);
		}

	}

	/** Algorithm implementation.
	 *
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	private static class CutStringToString implements CutStringAlgorithm {

		private final StringBuilder buffer;

		/**
		 * @param buffer is the buffer to fill.
		 */
		CutStringToString(StringBuilder buffer) {
			this.buffer = buffer;
		}

		@Override
		public void addLine(String line) {
			if (this.buffer.length() > 0) {
				this.buffer.append('\n');
			}
			this.buffer.append(line);
		}

	}

	/** Algorithm implementation.
	 *
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	private static class SplitSeparatorToListAlgorithm implements SplitSeparatorAlgorithm {

		private final List<String> list;

		/** Construct.
		 *
		 * @param tab strings.
		 */
		SplitSeparatorToListAlgorithm(List<String> tab) {
			this.list = tab;
		}

		@Override
		public void addToken(String token) {
			this.list.add(token);
		}

	}

	/** Algorithm implementation.
	 *
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	private static class SplitSeparatorToArrayAlgorithm implements SplitSeparatorAlgorithm {

		private static final int BUFFER_SIZE = 20;

		private String[] array = new String[BUFFER_SIZE];

		private int size;

		/** Construct.
		 */
		SplitSeparatorToArrayAlgorithm() {
			//
		}

		@Override
		public void addToken(String token) {
			if (this.size >= this.array.length) {
				final String[] t = new String[this.array.length + BUFFER_SIZE];
				System.arraycopy(this.array, 0, t, 0, this.array.length);
				this.array = t;
			}
			this.array[this.size] = token;
			++this.size;
		}

		/** Replies the array;
		 *
		 * @return the array.
		 */
		public String[] toArray() {
			if (this.array.length > this.size) {
				final String[] t = new String[this.size];
				System.arraycopy(this.array, 0, t, 0, this.size);
				return t;
			}
			return this.array;
		}

	}

	/** Algorithm implementation.
	 *
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	private static class UUIDSplitSeparatorAlgorithm implements SplitSeparatorAlgorithm {

		private final List<UUID> list;

		/** Construct.
		 * @param tab the list.
		 */
		UUIDSplitSeparatorAlgorithm(List<UUID> tab) {
			this.list = tab;
		}

		@Override
		public void addToken(String token) {
			this.list.add(UUID.fromString(token));
		}

	}

	/**
	 * Define the cutting critera of the string.
	 *
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 4.0
	 */
	public interface CutStringCritera {

		/** Replies if the given word is overfull the line.
		 *
		 * @param lineLength is the current length of the line.
		 * @param word is the word to add.
		 * @return <code>true</code> if the word is overfulling the line,
		 * <code>false</code> otherwise.
		 */
		boolean isOverfull(long lineLength, String word);

		/** Replies the length of the given string.
		 *
		 * @param str the string.
		 * @return the length of {@code str}.
		 */
		long getLengthFor(String str);

		/** Replies the character index at which the given string
		 * may be cut to fit the critera.
		 *
		 * @param str the string.
		 * @return the character index.
		 */
		int getCutIndexFor(String str);

		/** Replies the critera.
		 *
		 * @return the critera.
		 */
		long getCritera();

	}

	/**
	 * Define the cutting critera of the string.
	 *
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 4.0
	 */
	private static class CutStringColumnCritera implements CutStringCritera {

		private final int column;

		/** Construct.
		 *
		 * @param column the column.
		 */
		CutStringColumnCritera(int column) {
			this.column = column;
		}

		@Override
		public boolean isOverfull(long lineLength, String word) {
			return (lineLength + word.length() + 1) > this.column;
		}

		@Override
		public long getLengthFor(String str) {
			return str.length();
		}

		@Override
		public int getCutIndexFor(String str) {
			if (str.length() > this.column) {
				return this.column;
			}
			return 0;
		}

		@Override
		public long getCritera() {
			return this.column;
		}

	}

}
