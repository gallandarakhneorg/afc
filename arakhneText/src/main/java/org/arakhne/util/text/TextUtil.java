/* 
 * $Id$
 * 
 * Copyright (c) 2005-10 Multiagent Team,
 * Laboratoire Systemes et Transports,
 * Universite de Technologie de Belfort-Montbeliard.
 * All rights reserved.
 * 
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

package org.arakhne.util.text;

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

import org.arakhne.vmutil.locale.Locale;

/**
 * This class permits to manipulate texts.
 * 
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class TextUtil {

	private static final ReentrantLock lock = new ReentrantLock();
	
	private static SoftReference<Map<Character,String>> accentTransTbl = null;
	private static SoftReference<Map<String,Integer>> htmlToJavaTransTbl = null;
	private static SoftReference<Map<Character,String>> javaToHtmlTransTbl = null;

	/** Enforced version of the equality test on two strings with case ignoring.
	 * This enforced version supported <code>null</code> values
	 * given as parameters.
	 * 
	 * @param a
	 * @param b
	 * @param isNullEmptyEquivalence indicates if the <code>null</code> value
	 * is assimilated to the empty string.
	 * @return <code>true</code> if a is equal to b; otherwise <code>false</code>.
	 */
	public static boolean equalsIgnoreCase(String a, String b, boolean isNullEmptyEquivalence) {
		String aa = (a!=null || !isNullEmptyEquivalence) ? a : "";  //$NON-NLS-1$
		String bb = (b!=null || !isNullEmptyEquivalence) ? b : "";  //$NON-NLS-1$
		if (aa==null) return bb==null;
		if (bb==null) return false;
		return aa.equalsIgnoreCase(bb);
	}

	/** Replies a base 26 encoding string for the given
	 * number.
	 * 
	 * @param number
	 * @return the base 26 encoding.
	 * @since 4.0
	 */
	public static String encodeBase26(int number) {
		StringBuilder value = new StringBuilder();
		int q = number;
		do {
			int r = q % 26;
			value.insert(0,(char)('A'+r));
			q = q / 26 - 1;
		}
		while (q>=0);
		return value.toString();
	}
	
	/** Replies the html-to-java's translation table.
	 * <p>
	 * This method read the translations from the
	 * resource file <code>HTML_TRANS_TBL</code>.
	 * 
	 *  @return the translation table or <code>null</code> if none was found.
	 *  The translation table maps an HTML entity to its corresponding ISO chararacter code.
	 *  @since 4.0
	 */
	public static Map<String,Integer> getHtmlToJavaTranslationTable() {
		Map<String,Integer> map = null;
		try {
			lock.lock();
			if (htmlToJavaTransTbl!=null)
				map = htmlToJavaTransTbl.get();
		}
		finally {
			lock.unlock();
		}
		
		if (map!=null) return map;
		
        // Get the resource file
		ResourceBundle resource = null;
        try {
            resource = ResourceBundle.getBundle(
            		TextUtil.class.getCanonicalName(),
            		java.util.Locale.getDefault());
        }
        catch (MissingResourceException exep) {
            return null;
        }
        
        // get the resource string
        String result;
        
        try {
            result = resource.getString("HTML_TRANS_TBL"); //$NON-NLS-1$
        }
        catch (Exception e) {
            return null;
        }
        
        map = new TreeMap<String,Integer>();
        
        String[] pairs = result.split("(\\}\\{)|\\{|\\}"); //$NON-NLS-1$
        Integer isoCode;
        String entity;
        String code;
        for(int i=1; (i+1)<pairs.length; i+=2) {
        	try {
        		entity = pairs[i];
        		code = pairs[i+1];
        		isoCode = Integer.valueOf(code);
        		if (isoCode!=null) map.put(entity, isoCode);
        	}
        	catch(Throwable _) {
        		//
        	}
		}
        
		try {
			lock.lock();
			htmlToJavaTransTbl = new SoftReference<Map<String,Integer>>(map);
		}
		finally {
			lock.unlock();
		}

		return map;
	}

	/** Replies the java-to-html's translation table.
	 * <p>
	 * This method read the translations from the
	 * resource file <code>HTML_TRANS_TBL</code>.
	 * 
	 *  @return the translation table or <code>null</code> if none was found.
	 *  The translation table maps an ISO character code to its corresponding HTML entity.
	 *  @since 4.0
	 */
	public static Map<Character,String> getJavaToHTMLTranslationTable() {
		Map<Character,String> map = null;
		try {
			lock.lock();
			if (javaToHtmlTransTbl!=null)
				map = javaToHtmlTransTbl.get();
		}
		finally {
			lock.unlock();
		}
		
		if (map!=null) return map;
		
        // Get the resource file
        ResourceBundle resource = null;
        try {
            resource = ResourceBundle.getBundle(
            		TextUtil.class.getCanonicalName(),
            		java.util.Locale.getDefault());
        }
        catch (MissingResourceException exep) {
            return null;
        }
        
        // get the resource string
        String result;
        
        try {
            result = resource.getString("HTML_TRANS_TBL"); //$NON-NLS-1$
        }
        catch (Exception e) {
            return null;
        }
        
        map = new TreeMap<Character,String>();
        
        String[] pairs = result.split("(\\}\\{)|\\{|\\}"); //$NON-NLS-1$
        Integer isoCode;
        String entity;
        String code;
        for(int i=1; (i+1)<pairs.length; i+=2) {
        	try {
        		entity = pairs[i];
        		code = pairs[i+1];
        		isoCode = Integer.valueOf(code);
        		if (isoCode!=null) map.put((char)isoCode.intValue(), entity);
        	}
        	catch(Throwable _) {
        		//
        	}
		}
        
		try {
			lock.lock();
			javaToHtmlTransTbl = new SoftReference<Map<Character,String>>(map);
		}
		finally {
			lock.unlock();
		}

		return map;
	}

	/** Parse the given HTML text and replace all
	 *  the HTML entities by the corresponding unicode
	 *  character.
	 *  
	 *  @param html is the HTML to convert.
	 *  @return the unicode representation of the given html text.
	 *  @see #toHTML(String)
	 *  @since 4.0
	 */
	public static String parseHTML(String html) {
		if (html==null) return null;
		Map<String,Integer> transTbl = getHtmlToJavaTranslationTable();
		assert(transTbl!=null);
		if (transTbl.isEmpty()) return html;
		Pattern pattern = Pattern.compile("[&](([a-zA-Z]+)|(#x?[0-9]+))[;]"); //$NON-NLS-1$
		Matcher matcher = pattern.matcher(html);
		StringBuilder result = new StringBuilder();
		String entity;
		Integer isoCode;
		int idx, lastIndex = 0;
		while (matcher.find()) {
			idx = matcher.start();
			result.append(html.substring(lastIndex, idx));
			lastIndex = matcher.end();
			entity = matcher.group(1);
			if (entity.startsWith("#x")) { //$NON-NLS-1$
				try {
					isoCode = Integer.valueOf(entity.substring(2), 16);
				}
				catch(Throwable _) {
					isoCode = null;
				}
			}
			else if (entity.startsWith("#")) { //$NON-NLS-1$
				try {
					isoCode = Integer.valueOf(entity.substring(1));
				}
				catch(Throwable _) {
					isoCode = null;
				}
			}
			else {
				isoCode = transTbl.get(entity);
			}
			
			if (isoCode==null) {
				result.append(matcher.group());
			}
			else {
				result.append((char)isoCode.intValue());
			}
		}
		if (lastIndex<html.length()) result.append(html.substring(lastIndex));
		return result.toString();
	}

	/** Translate all the special character from the given
	 * text to their equivalent HTML entities.
	 *  
	 *  @param text is the text to convert.
	 *  @return the HTML text which is corresponding to the given text.
	 *  @see #parseHTML(String)
	 *  @since 4.0
	 */
	public static String toHTML(String text) {
		if (text==null) return null;
		Map<Character,String> transTbl = getJavaToHTMLTranslationTable();
		assert(transTbl!=null);
		if (transTbl.isEmpty()) return text;
		StringBuilder patternStr = new StringBuilder();
		for(Character c : transTbl.keySet()) {
			if (patternStr.length()>0) patternStr.append("|"); //$NON-NLS-1$
			patternStr.append(Pattern.quote(c.toString()));
		}
		Pattern pattern = Pattern.compile(patternStr.toString());
		Matcher matcher = pattern.matcher(text);
		StringBuilder result = new StringBuilder();
		String character, entity;
		int idx, lastIndex = 0;
		while (matcher.find()) {
			idx = matcher.start();
			result.append(text.substring(lastIndex, idx));
			lastIndex = matcher.end();
			character = matcher.group();
			if (character.length()==1) {
				entity = transTbl.get(Character.valueOf(character.charAt(0)));
				if (entity!=null) {
					entity = "&"+entity+";"; //$NON-NLS-1$ //$NON-NLS-2$
				}
				else {
					entity = character;
				}
			}
			else {
				entity = character;
			}
			result.append(entity);
		}
		if (lastIndex<text.length()) result.append(text.substring(lastIndex));
		return result.toString();
	}

	/** Format the text to be sure that each line is not
	 * more longer than the specified quantity of characters.
	 * 
	 * @param text is the string to cut
	 * @param column is the column number that corresponds to the splitting point.
	 * @return the given <var>text</var> splitted in lines separated by <code>\n</code>.
	 */
	public static String cutString(String text, int column) {
		StringBuilder buffer = new StringBuilder();
		cutStringAlgo(text, new CutStringColumnCritera(column), new CutStringToString(buffer));
		return buffer.toString();
	}

	/** Format the text to be sure that each line is not
	 * more longer than the specified quantity of characters.
	 * 
	 * @param text is the string to cut
	 * @param column is the column number that corresponds to the splitting point.
	 * @return the given <var>text</var> splitted in lines separated by <code>\n</code>.
	 */
	public static String[] cutStringAsArray(String text, int column) {
		List<String> list = new ArrayList<String>();
		cutStringAlgo(text, new CutStringColumnCritera(column), new CutStringToArray(list));
		String[] result = new String[list.size()];
		list.toArray(result);
		list.clear();
		return result;
	}
	
	/** Format the text to be sure that each line is not
	 * more longer than the specified critera.
	 * 
	 * @param text is the string to cut
	 * @param critera is the critera to respect.
	 * @param output is the given <var>text</var> splitted in lines separated by <code>\n</code>.
	 * @since 4.0
	 */
	public static void cutStringAsArray(String text, CutStringCritera critera, List<String> output) {
		cutStringAlgo(text, critera, new CutStringToArray(output));
	}

	private static void cutStringAlgo(String text, CutStringCritera critera, CutStringAlgorithm algo) {
		assert(critera!=null);
		
		if (text==null || critera.getCritera()<=0) return;
		
		assert(algo!=null);
		
		StringBuilder line = new StringBuilder();
		String[] lines = text.split("[\\n\\r\r\n]"); //$NON-NLS-1$
		String[] words;
		String word;
		long lineLength;
		int maxLength;
		
		for(int idxLine=0; idxLine<lines.length; ++idxLine) {
			words = lines[idxLine].split("[\t \n\r\f]+"); //$NON-NLS-1$
			lineLength = 0;
			for(int i=0; i<words.length; ++i) {
				word = words[i];
				if (critera.isOverfull(lineLength,word)) {
					if (line.length()>0 || i>0)
						algo.addLine(line.toString());
					line.setLength(0);
					line.append(word);
					// Split the word
					maxLength = critera.getCutIndexFor(line.toString());
					while (maxLength>0) {
						algo.addLine(line.substring(0, maxLength));
						line.delete(0, maxLength);
						maxLength = critera.getCutIndexFor(line.toString());
					}
					// Append last part of the word
					lineLength = critera.getLengthFor(line.toString());
				}
				else {
					if (line.length()>0) {
						line.append(' ');
						lineLength += critera.getLengthFor(" "); //$NON-NLS-1$
					}
					// Append word
					line.append(word);
					lineLength += critera.getLengthFor(word);
				}
			}
			if (line.length()>0) {
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
	public static char getMnemonicChar(String text) {
		if (text!=null) {
			int pos = text.indexOf('&');
			if ((pos!=-1)&&(pos<text.length()-1)) {
				return text.charAt(pos+1);
			}
		}
		return '\0';
	}
	
	/** Remove the mnemonic char from the specified string.
	 *  
	 * @param text is the text to scan.
	 * @return the given text without the mnemonic character.
	 */
	public static String removeMnemonicChar(String text) {
		if (text==null) return text;
		return text.replaceFirst("&",""); //$NON-NLS-1$ //$NON-NLS-2$
	}

	/** Replies the accent's translation table.
	 * <p>
	 * This method read the translations from the
	 * resource file <code>ACCENT_TRANS_TBL</code>.
	 * 
	 *  @return the translation table or <code>null</code> if none was found.
	 */
	public static Map<Character,String> getAccentTranslationTable() {
		Map<Character,String> map = null;
		try {
			lock.lock();
			if (accentTransTbl!=null)
				map = accentTransTbl.get();
		}
		finally {
			lock.unlock();
		}
		
		if (map!=null) return map;
		
        // Get the resource file
        ResourceBundle resource = null;
        try {
            resource = ResourceBundle.getBundle(
            		TextUtil.class.getCanonicalName(),
            		java.util.Locale.getDefault());
        }
        catch (MissingResourceException exep) {
            return null;
        }
        
        // get the resource string
        String result;
        
        try {
            result = resource.getString("ACCENT_TRANS_TBL"); //$NON-NLS-1$
        }
        catch (Exception e) {
            return null;
        }
        
        map = new TreeMap<Character,String>();
        
        String[] pairs = result.split("(\\}\\{)|\\{|\\}"); //$NON-NLS-1$
        for (String pair : pairs) {
        	if (pair.length()>1) {
        		map.put(pair.charAt(0),pair.substring(1));
        	}
		}
        
		try {
			lock.lock();
			accentTransTbl = new SoftReference<Map<Character,String>>(map);
		}
		finally {
			lock.unlock();
		}

		return map;
	}

	/** Remove the accents inside the specified string.
	 * 
	 * @param text is the string into which the accents must be removed.
	 * @return the given string without the accents
	 */
	public static String removeAccents(String text) {
		Map<Character,String> map = getAccentTranslationTable();
		if ((map==null)||(map.isEmpty())) return text;
        return removeAccents(text, map);
	}
	
	/** Remove the accents inside the specified string.
	 * 
	 *  @param text is the string into which the accents must be removed.
	 *  @param map is the translation table from an accentuated character to an
	 *  unaccentuated character.
	 * @return the given string without the accents
	 */
	public static String removeAccents(String text, Map<Character,String> map) {
		if (text==null) return text;
		StringBuilder buffer = new StringBuilder();
		for (char c : text.toCharArray()) {
			String trans = map.get(c);
			if (trans!=null)
				buffer.append(trans);
			else
				buffer.append(c);
		}
		return buffer.toString();
	}
	
	/** Split the given string according to brackets.
	 * The brackets are used to delimit the groups
	 * of characters.
	 * <p>
	 * Examples:
	 * <ul>
	 * <li><code>splitBrackets("{a}{b}{cd}")</code> returns the array
	 * <code>["a","b","cd"]</code></li>
	 * <li><code>splitBrackets("abcd")</code> returns the array
	 * <code>["abcd"]</code></li>
	 * <li><code>splitBrackets("a{bcd")</code> returns the array
	 * <code>["a","bcd"]</code></li>
	 * <ul>
	 * 
	 * @param str is the strig with brackets.
	 * @return the groups of strings
	 */
	public static String[] splitBrackets(String str) {
		return split('{','}',str);
	}

	/** Split the given string according to the separators.
	 * The separators are used to delimit the groups
	 * of characters.
	 * <p>
	 * Examples:
	 * <ul>
	 * <li><code>split('{','}',"{a}{b}{cd}")</code> returns the array
	 * <code>["a","b","cd"]</code></li>
	 * <li><code>split('{','}',"abcd")</code> returns the array
	 * <code>["abcd"]</code></li>
	 * <li><code>split('{','}',"a{bcd")</code> returns the array
	 * <code>["a","bcd"]</code></li>
	 * <ul>
	 * 
	 * @param leftSeparator is the left separator.
	 * @param rightSeparator is the right separator.
	 * @param str is the strig with brackets.
	 * @return the groups of strings
	 * @since 4.0
	 */
	public static String[] split(char leftSeparator, char rightSeparator, String str) {
		SplitSeparatorToArrayAlgorithm algo = new SplitSeparatorToArrayAlgorithm();
		splitSeparatorAlgorithm(leftSeparator, rightSeparator, str, algo);
		return algo.toArray();
	}

	/** Split the given string according to brackets.
	 * The brackets are used to delimit the groups
	 * of characters.
	 * <p>
	 * Examples:
	 * <ul>
	 * <li><code>splitBrackets("{a}{b}{cd}")</code> returns the array
	 * <code>["a","b","cd"]</code></li>
	 * <li><code>splitBrackets("abcd")</code> returns the array
	 * <code>["abcd"]</code></li>
	 * <li><code>splitBrackets("a{bcd")</code> returns the array
	 * <code>["a","bcd"]</code></li>
	 * <ul>
	 * 
	 * @param str is the elements enclosed by backets.
	 * @return the groups of strings
	 */
	public static List<String> splitBracketsAsList(String str) {
		return splitAsList('{', '}', str);
	}
	
	/** Split the given string according to separators.
	 * The separators are used to delimit the groups
	 * of characters.
	 * <p>
	 * Examples:
	 * <ul>
	 * <li><code>split('{','}',"{a}{b}{cd}")</code> returns the array
	 * <code>["a","b","cd"]</code></li>
	 * <li><code>split('{','}',"abcd")</code> returns the array
	 * <code>["abcd"]</code></li>
	 * <li><code>split('{','}',"a{bcd")</code> returns the array
	 * <code>["a","bcd"]</code></li>
	 * <ul>
	 * 
	 * @param leftSeparator is the left separator.
	 * @param rightSeparator is the right separator.
	 * @param str is the elements enclosed by backets.
	 * @return the groups of strings
	 */
	public static List<String> splitAsList(char leftSeparator, char rightSeparator, String str) {
		List<String> list = new ArrayList<String>();
		splitSeparatorAlgorithm(
				leftSeparator, rightSeparator, str,
				new SplitSeparatorToListAlgorithm(list));
		return list;
	}

	private static void splitSeparatorAlgorithm(
			char leftSeparator, char rightSeparator,
			String str, SplitSeparatorAlgorithm buffer) {
		assert(buffer!=null);
		if (str!=null && str.length()>0) {
			StringBuilder patternStr = new StringBuilder();
			patternStr.append("([^\\"); //$NON-NLS-1$
			patternStr.append(leftSeparator);
			patternStr.append("\\"); //$NON-NLS-1$
			patternStr.append(rightSeparator);
			patternStr.append("]*)(\\"); //$NON-NLS-1$
			patternStr.append(leftSeparator);
			patternStr.append("|\\"); //$NON-NLS-1$
			patternStr.append(rightSeparator);
			patternStr.append(")"); //$NON-NLS-1$
			Pattern pattern = Pattern.compile(patternStr.toString());
			Matcher matcher = pattern.matcher(str);
			
			int startOffset = 0; // inclusive
			int endOffset = 0; // exclusive
			String s, separator, previousText;
			int depth = 0;
			
			StringBuilder token = new StringBuilder();
			
			while (matcher.find()) {
				previousText = matcher.group(1);
				separator = matcher.group(2);
				endOffset = startOffset+previousText.length();
			
				if (startOffset<str.length() && endOffset>startOffset) {
					token.append(str.substring(startOffset, endOffset));
				}
				
				if (separator.equals(Character.toString(leftSeparator))) {
					if (depth>0) {
						token.append(separator);
					}
					else if (token.length()>0) {
						s = token.toString().trim();
						if (s.length()>0) buffer.addToken(s);
						token.setLength(0);
					}
					++depth;
				}
				else if (separator.equals(Character.toString(rightSeparator))) {
					if (depth==0) {
						token.append(separator);
					}
					else {
						--depth;
						if (depth>0) {
							token.append(separator);
						}
						else {
							buffer.addToken(token.toString());
							token.setLength(0);
						}
					}
				}
				else {
					throw new IllegalStateException();
				}
				
				startOffset = endOffset + separator.length();
			}
			
			if (startOffset<str.length()) {
				token.append(str.substring(startOffset));
			}
			
			if (token.length()>0) {
				s = token.toString().trim();
				if (s.length()>0) buffer.addToken(s);
			}
		}
	}

	/** Split the given string according to brackets.
	 * The brackets are used to delimit the groups
	 * of characters.
	 * <p>
	 * Examples:
	 * <ul>
	 * <li><code>splitBrackets("{a}{b}{cd}")</code> returns the array
	 * <code>["a","b","cd"]</code></li>
	 * <li><code>splitBrackets("abcd")</code> returns the array
	 * <code>["abcd"]</code></li>
	 * <li><code>splitBrackets("a{bcd")</code> returns the array
	 * <code>["a","bcd"]</code></li>
	 * <ul>
	 * 
	 * @param str is the elements enclosed by backets.
	 * @return the groups of strings
	 */
	public static List<UUID> splitBracketsAsUUIDs(String str) {
		return splitAsUUIDs('{','}',str);
	}

	/** Split the given string according to separators.
	 * The separators are used to delimit the groups
	 * of characters.
	 * <p>
	 * Examples:
	 * <ul>
	 * <li><code>split('{','}',"{a}{b}{cd}")</code> returns the array
	 * <code>["a","b","cd"]</code></li>
	 * <li><code>split('{','}',"abcd")</code> returns the array
	 * <code>["abcd"]</code></li>
	 * <li><code>split('{','}',"a{bcd")</code> returns the array
	 * <code>["a","bcd"]</code></li>
	 * <ul>
	 * 
	 * @param leftSeparator is the left separator.
	 * @param rightSeparator is the right separator.
	 * @param str is the elements enclosed by backets.
	 * @return the groups of strings
	 * @since 4.0
	 */
	public static List<UUID> splitAsUUIDs(char leftSeparator, char rightSeparator, String str) {
		List<UUID> list = new ArrayList<UUID>();
		splitSeparatorAlgorithm(leftSeparator, rightSeparator, str, new UUIDSplitSeparatorAlgorithm(list));
		return list;
	}

	/** Merge the given strings with to brackets.
	 * The brackets are used to delimit the groups
	 * of characters.
	 * <p>
	 * Examples:
	 * <ul>
	 * <li><code>mergeBrackets("a","b","cd")</code> returns the string
	 * <code>"{a}{b}{cd}"</code></li>
	 * <li><code>mergeBrackets("a{bcd")</code> returns the string
	 * <code>"{a{bcd}"</code></li>
	 * <ul>
	 * 
	 * @param <T> is the type of the parameters.
	 * @param strs is the array of strings.
	 * @return the string with bracketed strings.
	 */
	public static <T> String mergeBrackets(T... strs) {
		return join('{','}',strs);
	}

	/** Merge the given strings with to separators.
	 * The separators are used to delimit the groups
	 * of characters.
	 * <p>
	 * Examples:
	 * <ul>
	 * <li><code>merge('{','}',"a","b","cd")</code> returns the string
	 * <code>"{a}{b}{cd}"</code></li>
	 * <li><code>merge('{','}',"a{bcd")</code> returns the string
	 * <code>"{a{bcd}"</code></li>
	 * <ul>
	 * 
	 * @param <T> is the type of the parameters.
	 * @param leftSeparator is the left separator to use.
	 * @param rightSeparator is the right separator to use.
	 * @param strs is the array of strings.
	 * @return the string with merged strings.
	 * @since 4.0
	 */
	public static <T> String join(char leftSeparator, char rightSeparator, T... strs) {
		StringBuilder buffer = new StringBuilder();
		for(Object s : strs) {
			buffer.append(leftSeparator);
			if (s!=null) buffer.append(s.toString());
			buffer.append(rightSeparator);
		}
		return buffer.toString();
	}

	/** Merge the given strings with to brackets.
	 * The brackets are used to delimit the groups
	 * of characters.
	 * <p>
	 * Examples:
	 * <ul>
	 * <li><code>mergeBrackets("a","b","cd")</code> returns the string
	 * <code>"{a}{b}{cd}"</code></li>
	 * <li><code>mergeBrackets("a{bcd")</code> returns the string
	 * <code>"{a{bcd}"</code></li>
	 * <ul>
	 * 
	 * @param strs is the array of strings.
	 * @return the string with bracketed strings.
	 * @see #join(char, char, Iterable)
	 */
	public static String mergeBrackets(Iterable<?> strs) {
		return join('{','}',strs);
	}

	/** Merge the given strings with to separators.
	 * The separators are used to delimit the groups
	 * of characters.
	 * <p>
	 * Examples:
	 * <ul>
	 * <li><code>merge('{','}',"a","b","cd")</code> returns the string
	 * <code>"{a}{b}{cd}"</code></li>
	 * <li><code>merge('{','}',"a{bcd")</code> returns the string
	 * <code>"{a{bcd}"</code></li>
	 * <ul>
	 * 
	 * @param leftSeparator is the left separator to use.
	 * @param rightSeparator is the right separator to use.
	 * @param strs is the array of strings.
	 * @return the string with merged strings.
	 * @since 4.0
	 */
	public static String join(char leftSeparator, char rightSeparator, Iterable<?> strs) {
		StringBuilder buffer = new StringBuilder();
		for(Object s : strs) {
			buffer.append(leftSeparator);
			if (s!=null) buffer.append(s.toString());
			buffer.append(rightSeparator);
		}
		return buffer.toString();
	}

	/**
     * Compares this <code>String</code> to another <code>String</code>,
     * ignoring accent considerations.  Two strings are considered equal
     * ignoring accents if they are of the same length, and corresponding
     * characters in the two strings are equal ignoring accents.
     * <p>
     * This method is equivalent to:
     * <pre><code>
     * TextUtil.removeAccents(s1,map).equals(TextUtil.removeAccents(s2,map));
     * </code></pre> 
     *
     * @param   s1 is the first string to compare.
     * @param   s2 is the second string to compare.
	 *  @param map is the translation table from an accentuated character to an
	 *  unaccentuated character.
     * @return  <code>true</code> if the argument is not <code>null</code>
     *          and the <code>String</code>s are equal,
     *          ignoring case; <code>false</code> otherwise.
     * @see     #removeAccents(String, Map)
	 */
	public static boolean equalsIgnoreAccents(String s1, String s2, Map<Character,String> map) {
		return removeAccents(s1,map).equals(removeAccents(s2,map));
	}

	/**
     * Compares this <code>String</code> to another <code>String</code>,
     * ignoring case and accent considerations.  Two strings are considered equal
     * ignoring case and accents if they are of the same length, and corresponding
     * characters in the two strings are equal ignoring case and accents.
     * <p>
     * This method is equivalent to:
     * <pre><code>
     * TextUtil.removeAccents(s1,map).equalsIgnoreCase(TextUtil.removeAccents(s2,map));
     * </code></pre> 
     *
     * @param   s1 is the first string to compare.
     * @param   s2 is the second string to compare.
	 *  @param map is the translation table from an accentuated character to an
	 *  unaccentuated character.
     * @return  <code>true</code> if the argument is not <code>null</code>
     *          and the <code>String</code>s are equal,
     *          ignoring case; <code>false</code> otherwise.
     * @see     #removeAccents(String, Map)
	 */
	public static boolean equalsIgnoreCaseAccents(String s1, String s2, Map<Character,String> map) {
		return removeAccents(s1,map).equalsIgnoreCase(removeAccents(s2,map));
	}

	/** Translate the specified string to lower case and remove the accents.
	 * 
	 * @param text is the text to scan.
	 * @return the given string without the accents and lower cased
	 */
	public static String toLowerCaseWithoutAccent(String text) {
		Map<Character,String> map = getAccentTranslationTable();
		if ((map==null)||(map.isEmpty())) return text;
        return toLowerCaseWithoutAccent(text, map);
	}
	
	/** Translate the specified string to lower case and remove the accents.
	 * 
	 * @param text is the text to scan.
	 * @param map is the translation table from an accentuated character to an
	 *  unaccentuated character.
	 * @return the given string without the accents and lower cased
	 */
	public static String toLowerCaseWithoutAccent(String text, Map<Character,String> map) {
		StringBuilder buffer = new StringBuilder();
		for (char c : text.toCharArray()) {
			String trans = map.get(c);
			if (trans!=null)
				buffer.append(trans.toLowerCase());
			else
				buffer.append(Character.toLowerCase(c));
		}
		return buffer.toString();
	}

	/** Translate the specified string to upper case and remove the accents.
	 * 
	 * @param text is the text to scan.
	 * @return the given string without the accents and upper cased
	 */
	public static String toUpperCaseWithoutAccent(String text) {
		Map<Character,String> map = getAccentTranslationTable();
		if ((map==null)||(map.isEmpty())) return text;
        return toUpperCaseWithoutAccent(text, map);
	}
	
	/** Translate the specified string to upper case and remove the accents.
	 * 
	 * @param text is the text to scan.
	 * @param map is the translation table from an accentuated character to an
	 *  unaccentuated character.
	 * @return the given string without the accents and upper cased
	 */
	public static String toUpperCaseWithoutAccent(String text, Map<Character,String> map) {
		StringBuilder buffer = new StringBuilder();
		for (char c : text.toCharArray()) {
			String trans = map.get(c);
			if (trans!=null)
				buffer.append(trans.toUpperCase());
			else
				buffer.append(Character.toUpperCase(c));
		}
		return buffer.toString();
	}
	
	/** Compute the better metric representing
	 * the given time amount and reply a string representation
	 * of the given amount with this selected unit.
	 * <p>
	 * This function try to use a greater metric unit.
	 * 
	 * @param amount is the amount expressed in the given unit.
	 * @param unit is the unit of the given amount.
	 * @return a string representation of the given amount.
	 */
	public static String formatTime(double amount, TimeUnit unit) {
		double amt;
		double coef = 1.;
		switch(unit) {
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

		StringBuilder text = new StringBuilder();
		String centuries, years, days, hours, minutes, seconds;
		int idx;
		long a, ah, am, as;
		
		centuries = years = days = hours = minutes = seconds = ""; //$NON-NLS-1$
		ah = am = as = 0;
		idx = 0;
		
		if (amt>=3153600000.) {
			a = (long)Math.floor(amt / 3153600000.);
			centuries = Locale.getString((a>=2) ? "TIME_FORMAT_Cs" : "TIME_FORMAT_C", Long.toString(a)); //$NON-NLS-1$ //$NON-NLS-2$
			amt -= a * 3153600000.;
			text.append(centuries);
			idx |= 32;
		}

		if (amt>=31536000.) {
			a = (long)Math.floor(amt / 31536000.);
			years = Locale.getString((a>=2) ? "TIME_FORMAT_Ys" : "TIME_FORMAT_Y", Long.toString(a)); //$NON-NLS-1$ //$NON-NLS-2$
			amt -= a * 31536000.;
			if (text.length()>0) text.append(' ');
			text.append(years);
			idx |= 16;
		}

		if (amt>=86400.) {
			a = (long)Math.floor(amt / 86400.);
			days = Locale.getString((a>=2) ? "TIME_FORMAT_Ds" : "TIME_FORMAT_D", Long.toString(a)); //$NON-NLS-1$ //$NON-NLS-2$
			amt -= a * 86400.;
			if (text.length()>0) text.append(' ');
			text.append(days);
			idx |= 8;
		}
		
		//-------------------
		
		if (amt>=3600.) {
			ah = (long)Math.floor(amt / 3600.);
			hours = Long.toString(ah);
			if (ah<10.) hours = "0" + hours; //$NON-NLS-1$
			amt -= ah * 3600.;
			idx |= 4;
		}

		if (amt>=60.) {
			am = (long)Math.floor(amt / 60.);
			minutes = Long.toString(am);
			if (am<10.) minutes = "0" + minutes; //$NON-NLS-1$
			amt -= am * 60.;
			idx |= 2;
		}
		
		if (amt>=0. || idx==0) {
			if (idx>=8) {
				as = (long)Math.floor(amt);
				seconds = Long.toString(as);
			}
			else {
				NumberFormat fmt = new DecimalFormat("#0.000"); //$NON-NLS-1$				
				seconds = fmt.format(amt);
			}
			idx |= 1;
		}

		if ((idx&7)==7) {
			if (text.length()>0) text.append(' ');
			if (idx>=8 && as>0) {
				if (as<10.) seconds = "0" + seconds; //$NON-NLS-1$
			}
			else if (idx<8 && amt>0.) {
				if (amt<10.) seconds = "0" + seconds; //$NON-NLS-1$
			}			
			text.append(Locale.getString("TIME_FORMAT_HMS", hours, minutes, seconds)); //$NON-NLS-1$
		}
		else {
			if (ah>0) {
				if (text.length()>0) text.append(' ');
				text.append(Locale.getString((ah>=2) ? "TIME_FORMAT_Hs" : "TIME_FORMAT_H", hours)); //$NON-NLS-1$ //$NON-NLS-2$
			}
			if (am>0) {
				if (text.length()>0) text.append(' ');
				text.append(Locale.getString((am>=2) ? "TIME_FORMAT_Ms" : "TIME_FORMAT_M", minutes)); //$NON-NLS-1$ //$NON-NLS-2$
			}
			if (idx>=8 && as>0) {
				if (text.length()>0) text.append(' ');
				text.append(Locale.getString((as>=2) ? "TIME_FORMAT_Ss" : "TIME_FORMAT_S", seconds)); //$NON-NLS-1$ //$NON-NLS-2$
			}
			else if (idx<8 && amt>0.) {
				if (text.length()>0) text.append(' ');
				text.append(Locale.getString((amt>=2.) ? "TIME_FORMAT_Ss" : "TIME_FORMAT_S", seconds)); //$NON-NLS-1$ //$NON-NLS-2$
			}
		}

		return text.toString();
	}
	
	/** Format the given double value.
	 * 
	 * @param amount
	 * @param decimalCount is the maximal count of decimal to put in the string.
	 * @return a string representation of the given value.
	 */
	public static String formatDouble(double amount, int decimalCount) {
		int dc = (decimalCount<0) ? 0 : decimalCount;
		
		StringBuilder str = new StringBuilder("#0"); //$NON-NLS-1$

		if (dc>0) {
			str.append('.');
			for(int i=0; i<dc; ++i)
				str.append('0');
		}
		
		DecimalFormat fmt = new DecimalFormat(str.toString());
		return fmt.format(amount);
	}

	/** Format the given float value.
	 * 
	 * @param amount
	 * @param decimalCount is the maximal count of decimal to put in the string.
	 * @return a string representation of the given value.
	 */
	public static String formatFloat(float amount, int decimalCount) {
		int dc = (decimalCount<0) ? 0 : decimalCount;
		
		StringBuilder str = new StringBuilder("#0"); //$NON-NLS-1$

		if (dc>0) {
			str.append('.');
			for(int i=0; i<dc; ++i)
				str.append('0');
		}
		
		DecimalFormat fmt = new DecimalFormat(str.toString());
		return fmt.format(amount);
	}
	
	/** Join the elements of the given array with the given join text.
	 * 
	 * @param <T> is the type of the elements
	 * @param joinText
	 * @param elements
	 * @return the joining text
	 */
	public static <T> String join(String joinText, T... elements) {
		return join(joinText, null, null, elements);
	}

	/** Join the elements of the given array with the given join text.
	 * 
	 * @param joinText
	 * @param elements
	 * @return the joining text
	 */
	public static String join(String joinText, boolean... elements) {
		return join(joinText, null, null, elements);
	}

	/** Join the elements of the given array with the given join text.
	 * 
	 * @param joinText
	 * @param elements
	 * @return the joining text
	 */
	public static String join(String joinText, byte... elements) {
		return join(joinText, null, null, elements);
	}

	/** Join the elements of the given array with the given join text.
	 * 
	 * @param joinText
	 * @param elements
	 * @return the joining text
	 */
	public static String join(String joinText, char... elements) {
		return join(joinText, null, null, elements);
	}

	/** Join the elements of the given array with the given join text.
	 * 
	 * @param joinText
	 * @param elements
	 * @return the joining text
	 */
	public static String join(String joinText, short... elements) {
		return join(joinText, null, null, elements);
	}

	/** Join the elements of the given array with the given join text.
	 * 
	 * @param joinText
	 * @param elements
	 * @return the joining text
	 */
	public static String join(String joinText, int... elements) {
		return join(joinText, null, null, elements);
	}

	/** Join the elements of the given array with the given join text.
	 * 
	 * @param joinText
	 * @param elements
	 * @return the joining text
	 */
	public static String join(String joinText, long... elements) {
		return join(joinText, null, null, elements);
	}

	/** Join the elements of the given array with the given join text.
	 * 
	 * @param joinText
	 * @param elements
	 * @return the joining text
	 */
	public static String join(String joinText, float... elements) {
		return join(joinText, null, null, elements);
	}

	/** Join the elements of the given array with the given join text.
	 * 
	 * @param joinText
	 * @param elements
	 * @return the joining text
	 */
	public static String join(String joinText, double... elements) {
		return join(joinText, null, null, elements);
	}

	/** Join the elements of the given array with the given join text.
	 * 
	 * @param joinText
	 * @param elements
	 * @return the joining text
	 */
	public static String join(String joinText, Iterable<?> elements) {
		return join(joinText, null, null, elements);
	}

	/** Join the elements of the given array with the given join text.
	 * The <var>prefix</var> and <var>postfix</var> values will be put
	 * just before and just after each element respectively. 
	 * 
	 * @param <T> is the type of the elements
	 * @param joinText
	 * @param prefix
	 * @param postfix
	 * @param elements
	 * @return the joining text
	 */
	public static <T> String join(String joinText, String prefix, String postfix, T... elements) {
		return join(joinText, prefix, postfix, Arrays.asList(elements));
	}

	/** Join the elements of the given array with the given join text.
	 * The <var>prefix</var> and <var>postfix</var> values will be put
	 * just before and just after each element respectively. 
	 * 
	 * @param joinText
	 * @param prefix
	 * @param postfix
	 * @param elements
	 * @return the joining text
	 */
	public static String join(String joinText, String prefix, String postfix, Iterable<?> elements) {
		StringBuilder buffer = new StringBuilder();
		String txt;
		for(Object e : elements) {
			if (e!=null) {
				txt = e.toString();
				if (txt!=null && txt.length()>0) {
					if (buffer.length()>0) buffer.append(joinText);
					if (prefix!=null) buffer.append(prefix);
					buffer.append(txt);
					if (postfix!=null) buffer.append(postfix);
				}
			}
		}
		return buffer.toString();
	}

	/** Join the elements of the given array with the given join text.
	 * The <var>prefix</var> and <var>postfix</var> values will be put
	 * just before and just after each element respectively. 
	 * 
	 * @param joinText
	 * @param prefix
	 * @param postfix
	 * @param elements
	 * @return the joining text
	 */
	public static String join(String joinText, String prefix, String postfix, boolean... elements) {
		StringBuilder buffer = new StringBuilder();
		for(boolean e : elements) {
			if (buffer.length()>0) buffer.append(joinText);
			if (prefix!=null) buffer.append(prefix);
			buffer.append(e);
			if (postfix!=null) buffer.append(postfix);
		}
		return buffer.toString();
	}

	/** Join the elements of the given array with the given join text.
	 * The <var>prefix</var> and <var>postfix</var> values will be put
	 * just before and just after each element respectively. 
	 * 
	 * @param joinText
	 * @param prefix
	 * @param postfix
	 * @param elements
	 * @return the joining text
	 */
	public static String join(String joinText, String prefix, String postfix, byte... elements) {
		StringBuilder buffer = new StringBuilder();
		for(byte e : elements) {
			if (buffer.length()>0) buffer.append(joinText);
			if (prefix!=null) buffer.append(prefix);
			buffer.append(e);
			if (postfix!=null) buffer.append(postfix);
		}
		return buffer.toString();
	}

	/** Join the elements of the given array with the given join text.
	 * The <var>prefix</var> and <var>postfix</var> values will be put
	 * just before and just after each element respectively. 
	 * 
	 * @param joinText
	 * @param prefix
	 * @param postfix
	 * @param elements
	 * @return the joining text
	 */
	public static String join(String joinText, String prefix, String postfix, char... elements) {
		StringBuilder buffer = new StringBuilder();
		for(char e : elements) {
			if (buffer.length()>0) buffer.append(joinText);
			if (prefix!=null) buffer.append(prefix);
			buffer.append(e);
			if (postfix!=null) buffer.append(postfix);
		}
		return buffer.toString();
	}

	/** Join the elements of the given array with the given join text.
	 * The <var>prefix</var> and <var>postfix</var> values will be put
	 * just before and just after each element respectively. 
	 * 
	 * @param joinText
	 * @param prefix
	 * @param postfix
	 * @param elements
	 * @return the joining text
	 */
	public static String join(String joinText, String prefix, String postfix, short... elements) {
		StringBuilder buffer = new StringBuilder();
		for(short e : elements) {
			if (buffer.length()>0) buffer.append(joinText);
			if (prefix!=null) buffer.append(prefix);
			buffer.append(e);
			if (postfix!=null) buffer.append(postfix);
		}
		return buffer.toString();
	}

	/** Join the elements of the given array with the given join text.
	 * The <var>prefix</var> and <var>postfix</var> values will be put
	 * just before and just after each element respectively. 
	 * 
	 * @param joinText
	 * @param prefix
	 * @param postfix
	 * @param elements
	 * @return the joining text
	 */
	public static String join(String joinText, String prefix, String postfix, int... elements) {
		StringBuilder buffer = new StringBuilder();
		for(int e : elements) {
			if (buffer.length()>0) buffer.append(joinText);
			if (prefix!=null) buffer.append(prefix);
			buffer.append(e);
			if (postfix!=null) buffer.append(postfix);
		}
		return buffer.toString();
	}

	/** Join the elements of the given array with the given join text.
	 * The <var>prefix</var> and <var>postfix</var> values will be put
	 * just before and just after each element respectively. 
	 * 
	 * @param joinText
	 * @param prefix
	 * @param postfix
	 * @param elements
	 * @return the joining text
	 */
	public static String join(String joinText, String prefix, String postfix, long... elements) {
		StringBuilder buffer = new StringBuilder();
		for(long e : elements) {
			if (buffer.length()>0) buffer.append(joinText);
			if (prefix!=null) buffer.append(prefix);
			buffer.append(e);
			if (postfix!=null) buffer.append(postfix);
		}
		return buffer.toString();
	}

	/** Join the elements of the given array with the given join text.
	 * The <var>prefix</var> and <var>postfix</var> values will be put
	 * just before and just after each element respectively. 
	 * 
	 * @param joinText
	 * @param prefix
	 * @param postfix
	 * @param elements
	 * @return the joining text
	 */
	public static String join(String joinText, String prefix, String postfix, float... elements) {
		StringBuilder buffer = new StringBuilder();
		for(float e : elements) {
			if (buffer.length()>0) buffer.append(joinText);
			if (prefix!=null) buffer.append(prefix);
			buffer.append(e);
			if (postfix!=null) buffer.append(postfix);
		}
		return buffer.toString();
	}

	/** Join the elements of the given array with the given join text.
	 * The <var>prefix</var> and <var>postfix</var> values will be put
	 * just before and just after each element respectively. 
	 * 
	 * @param joinText
	 * @param prefix
	 * @param postfix
	 * @param elements
	 * @return the joining text
	 */
	public static String join(String joinText, String prefix, String postfix, double... elements) {
		StringBuilder buffer = new StringBuilder();
		for(double e : elements) {
			if (buffer.length()>0) buffer.append(joinText);
			if (prefix!=null) buffer.append(prefix);
			buffer.append(e);
			if (postfix!=null) buffer.append(postfix);
		}
		return buffer.toString();
	}

	/**
	 * Algorithm interface used by cut string functions to provide
	 * a buffer filler.
	 * 
	 * @author $Author: galland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	private interface CutStringAlgorithm {

		/** Add a line to the output buffer.
		 * 
		 * @param line
		 */
		public void addLine(String line);
		
	}

	/**
	 * Algorithm interface used by split bracket functions to provide
	 * a buffer filler.
	 * 
	 * @author $Author: galland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	private interface SplitSeparatorAlgorithm {

		/** Add a token to the output buffer.
		 * 
		 * @param token
		 */
		public void addToken(String token);
		
	}

	/**
	 * @author $Author: galland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	private static class CutStringToArray implements CutStringAlgorithm {

		private final List<String> buffer;
		
		/**
		 * @param buffer is the buffer to fill.
		 */
		public CutStringToArray(List<String> buffer) {
			this.buffer = buffer;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void addLine(String line) {
			this.buffer.add(line);
		}
		
	}
	
	/**
	 * @author $Author: galland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	private static class CutStringToString implements CutStringAlgorithm {

		private final StringBuilder buffer;
		
		/**
		 * @param buffer is the buffer to fill.
		 */
		public CutStringToString(StringBuilder buffer) {
			this.buffer = buffer;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void addLine(String line) {
			if (this.buffer.length()>0)
				this.buffer.append('\n');
			this.buffer.append(line);
		}
		
	}

	/**
	 * @author $Author: galland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	private static class SplitSeparatorToListAlgorithm implements SplitSeparatorAlgorithm {

		private final List<String> list;
		
		/**
		 * @param tab
		 */
		public SplitSeparatorToListAlgorithm(List<String> tab) {
			this.list = tab;
		}
		
		/** {@inheritDoc}
		 */
		@Override
		public void addToken(String token) {
			this.list.add(token);
		}
		
	}

	/**
	 * @author $Author: galland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	private static class SplitSeparatorToArrayAlgorithm implements SplitSeparatorAlgorithm {

		private static final int BUFFER_SIZE = 20;
		
		/**
		 */
		private String[] array = new String[BUFFER_SIZE];
		private int size = 0;
		
		/**
		 */
		public SplitSeparatorToArrayAlgorithm() {
			//
		}
		
		/** {@inheritDoc}
		 */
		@Override
		public void addToken(String token) {
			if (this.size>=this.array.length) {
				String[] t = new String[this.array.length+BUFFER_SIZE];
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
			if (this.array.length>this.size) {
				String[] t = new String[this.size];
				System.arraycopy(this.array, 0, t, 0, this.size);
				return t;
			}
			return this.array;
		}
		
	}

	/**
	 * @author $Author: galland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	private static class UUIDSplitSeparatorAlgorithm implements SplitSeparatorAlgorithm {

		private final List<UUID> list;
		
		/**
		 * @param tab
		 */
		public UUIDSplitSeparatorAlgorithm(List<UUID> tab) {
			this.list = tab;
		}
		
		/** {@inheritDoc}
		 */
		@Override
		public void addToken(String token) {
			this.list.add(UUID.fromString(token));
		}
		
	}

	/**
	 * Define the cutting critera of the string.
	 * 
	 * @author $Author: galland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 4.0
	 */
	public static interface CutStringCritera {

		/** Replies if the given word is overfull the line.
		 * 
		 * @param lineLength is the current length of the line.
		 * @param word is the word to add.
		 * @return <code>true</code> if the word is overfulling the line,
		 * <code>false</code> otherwise.
		 */
		public boolean isOverfull(long lineLength, String word);

		/** Replies the length of the given string.
		 * 
		 * @param str
		 * @return the length of <var>str</var>.
		 */
		public long getLengthFor(String str);

		/** Replies the character index at which the given string
		 * may be cut to fit the critera.
		 * 
		 * @param str
		 * @return the character index.
		 */
		public int getCutIndexFor(String str);
		
		/** Replies the critera.
		 * 
		 * @return the critera.
		 */
		public long getCritera();
		
	}
	
	/**
	 * Define the cutting critera of the string.
	 * 
	 * @author $Author: galland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 4.0
	 */
	private static class CutStringColumnCritera implements CutStringCritera {

		private final int column;
		
		/**
		 * @param column
		 */
		public CutStringColumnCritera(int column) {
			this.column = column;
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean isOverfull(long lineLength, String word) {
			return (lineLength+word.length()+1)>this.column;
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public long getLengthFor(String str) {
			return str.length();
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public int getCutIndexFor(String str) {
			if (str.length()>this.column) {
				return this.column;
			}
			return 0;
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public long getCritera() {
			return this.column;
		}
		
	}

}
