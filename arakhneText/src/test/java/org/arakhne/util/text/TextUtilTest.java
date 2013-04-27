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

import java.util.List;

import org.arakhne.vmutil.locale.Locale;

import junit.framework.TestCase;

/**
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class TextUtilTest extends TestCase {

	/**
	 * @throws Exception
	 */
	public static void testCutStringAsArray() throws Exception {
		String src;
		String[] res;
		String[] actual;
		
		src = Locale.getString("A_SOURCE"); //$NON-NLS-1$
		res = Locale.getString("A_RESULT").split("\n"); //$NON-NLS-1$ //$NON-NLS-2$
		actual = TextUtil.cutStringAsArray(src, 80);
		assertNotNull(actual);
		assertEquals(res.length, actual.length);
		for(int i=0; i<res.length; i++) {
			assertTrue("A:Line Size "+(i+1)+": "+actual[i]+" = "+actual[i].length(), actual[i].length()<=80); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			assertEquals("A:Line "+(i+1), res[i], actual[i]); //$NON-NLS-1$
		}

		src = Locale.getString("B_SOURCE"); //$NON-NLS-1$
		res = Locale.getString("B_RESULT").split("\n"); //$NON-NLS-1$ //$NON-NLS-2$
		actual = TextUtil.cutStringAsArray(src, 80);
		assertNotNull(actual);
		assertEquals(res.length, actual.length);
		for(int i=0; i<res.length; i++) {
			assertTrue("B:Line Size "+(i+1)+": "+actual[i]+" = "+actual[i].length(), actual[i].length()<=80); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			assertEquals("B:Line "+(i+1), res[i], actual[i]); //$NON-NLS-1$
		}

		src = Locale.getString("C_SOURCE"); //$NON-NLS-1$
		res = Locale.getString("C_RESULT").split("\n"); //$NON-NLS-1$ //$NON-NLS-2$
		actual = TextUtil.cutStringAsArray(src, 80);
		assertNotNull(actual);
		assertEquals(res.length, actual.length);
		for(int i=0; i<res.length; i++) {
			assertTrue("C:Line Size "+(i+1)+": "+actual[i]+" = "+actual[i].length(), actual[i].length()<=80); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			assertEquals("C:Line "+(i+1), res[i], actual[i]); //$NON-NLS-1$
		}

		src = Locale.getString("D_SOURCE"); //$NON-NLS-1$
		res = Locale.getString("D_RESULT").split("\n"); //$NON-NLS-1$ //$NON-NLS-2$
		actual = TextUtil.cutStringAsArray(src, 80);
		assertNotNull(actual);
		assertEquals(res.length, actual.length);
		for(int i=0; i<res.length; i++) {
			assertTrue("D:Line Size "+(i+1)+": "+actual[i]+" = "+actual[i].length(), actual[i].length()<=80); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			assertEquals("D:Line "+(i+1), res[i], actual[i]); //$NON-NLS-1$
		}
	}

	/**
	 * @throws Exception
	 */
	public static void testCutString() throws Exception {
		String src, res, actual;
		
		src = Locale.getString("A_SOURCE"); //$NON-NLS-1$
		res = Locale.getString("A_RESULT"); //$NON-NLS-1$
		actual = TextUtil.cutString(src, 80);
		assertNotNull(actual);
		assertEquals("A:", res, actual); //$NON-NLS-1$

		src = Locale.getString("B_SOURCE"); //$NON-NLS-1$
		res = Locale.getString("B_RESULT"); //$NON-NLS-1$
		actual = TextUtil.cutString(src, 80);
		assertNotNull(actual);
		assertEquals("B:", res, actual); //$NON-NLS-1$

		src = Locale.getString("C_SOURCE"); //$NON-NLS-1$
		res = Locale.getString("C_RESULT"); //$NON-NLS-1$
		actual = TextUtil.cutString(src, 80);
		assertNotNull(actual);
		assertEquals("C:", res, actual); //$NON-NLS-1$

		src = Locale.getString("D_SOURCE"); //$NON-NLS-1$
		res = Locale.getString("D_RESULT"); //$NON-NLS-1$
		actual = TextUtil.cutString(src, 80);
		assertNotNull(actual);
		assertEquals("D:", res, actual); //$NON-NLS-1$
	}
	
	/**
	 * @throws Exception
	 */
	public static void testSplitBrackets() throws Exception {
		String[] tab;
		
		tab = TextUtil.splitBrackets("{a}{b}{c}{d}"); //$NON-NLS-1$
		assertEquals(new String[]{
				"a", //$NON-NLS-1$
				"b", //$NON-NLS-1$
				"c", //$NON-NLS-1$
				"d", //$NON-NLS-1$
			}, tab);

		tab = TextUtil.splitBrackets("start {a}bbb {b eee}{c}{d}zzz end"); //$NON-NLS-1$
		assertEquals(new String[]{
				"start", //$NON-NLS-1$
				"a", //$NON-NLS-1$
				"bbb", //$NON-NLS-1$
				"b eee", //$NON-NLS-1$
				"c", //$NON-NLS-1$
				"d", //$NON-NLS-1$
				"zzz end", //$NON-NLS-1$
			}, tab);

		tab = TextUtil.splitBrackets("start {a}bbb {b {eee}}{c}{d}zzz end"); //$NON-NLS-1$
		assertEquals(new String[]{
				"start", //$NON-NLS-1$
				"a", //$NON-NLS-1$
				"bbb", //$NON-NLS-1$
				"b {eee}", //$NON-NLS-1$
				"c", //$NON-NLS-1$
				"d", //$NON-NLS-1$
				"zzz end", //$NON-NLS-1$
			}, tab);

		tab = TextUtil.splitBrackets("start {a}bbb {b {e{e{e}f}}}{c}{d}zzz end"); //$NON-NLS-1$
		assertEquals(new String[]{
				"start", //$NON-NLS-1$
				"a", //$NON-NLS-1$
				"bbb", //$NON-NLS-1$
				"b {e{e{e}f}}", //$NON-NLS-1$
				"c", //$NON-NLS-1$
				"d", //$NON-NLS-1$
				"zzz end", //$NON-NLS-1$
			}, tab);

		tab = TextUtil.splitBrackets("start {a}bbb {b {e{e{e}f}}}{}{d}zzz end"); //$NON-NLS-1$
		assertEquals(new String[]{
				"start", //$NON-NLS-1$
				"a", //$NON-NLS-1$
				"bbb", //$NON-NLS-1$
				"b {e{e{e}f}}", //$NON-NLS-1$
				"", //$NON-NLS-1$
				"d", //$NON-NLS-1$
				"zzz end", //$NON-NLS-1$
			}, tab);
	}
	
	/**
	 * @throws Exception
	 */
	public static void testSplitCharCharStr() throws Exception {
		String[] tab;
		
		tab = TextUtil.split('(',']',"(a](b](c](d]"); //$NON-NLS-1$
		assertEquals(new String[]{
				"a", //$NON-NLS-1$
				"b", //$NON-NLS-1$
				"c", //$NON-NLS-1$
				"d", //$NON-NLS-1$
			}, tab);

		tab = TextUtil.split('(',']',"start (a]bbb (b eee](c](d]zzz end"); //$NON-NLS-1$
		assertEquals(new String[]{
				"start", //$NON-NLS-1$
				"a", //$NON-NLS-1$
				"bbb", //$NON-NLS-1$
				"b eee", //$NON-NLS-1$
				"c", //$NON-NLS-1$
				"d", //$NON-NLS-1$
				"zzz end", //$NON-NLS-1$
			}, tab);

		tab = TextUtil.split('(',']',"start (a]bbb (b (eee]](c](d]zzz end"); //$NON-NLS-1$
		assertEquals(new String[]{
				"start", //$NON-NLS-1$
				"a", //$NON-NLS-1$
				"bbb", //$NON-NLS-1$
				"b (eee]", //$NON-NLS-1$
				"c", //$NON-NLS-1$
				"d", //$NON-NLS-1$
				"zzz end", //$NON-NLS-1$
			}, tab);

		tab = TextUtil.split('(',']',"start (a]bbb (b (e(e(e]f]]](c](d]zzz end"); //$NON-NLS-1$
		assertEquals(new String[]{
				"start", //$NON-NLS-1$
				"a", //$NON-NLS-1$
				"bbb", //$NON-NLS-1$
				"b (e(e(e]f]]", //$NON-NLS-1$
				"c", //$NON-NLS-1$
				"d", //$NON-NLS-1$
				"zzz end", //$NON-NLS-1$
			}, tab);

		tab = TextUtil.split('(',']',"start (a]bbb (b (e(e(e]f]]](](d]zzz end"); //$NON-NLS-1$
		assertEquals(new String[]{
				"start", //$NON-NLS-1$
				"a", //$NON-NLS-1$
				"bbb", //$NON-NLS-1$
				"b (e(e(e]f]]", //$NON-NLS-1$
				"", //$NON-NLS-1$
				"d", //$NON-NLS-1$
				"zzz end", //$NON-NLS-1$
			}, tab);
	}

	/**
	 * @throws Exception
	 */
	public static void testSplitBracketsAsList() throws Exception {
		List<String> tab;
		
		tab = TextUtil.splitBracketsAsList("{a}{b}{c}{d}"); //$NON-NLS-1$
		assertEquals(new String[]{
				"a", //$NON-NLS-1$
				"b", //$NON-NLS-1$
				"c", //$NON-NLS-1$
				"d", //$NON-NLS-1$
			}, tab);

		tab = TextUtil.splitBracketsAsList("start {a}bbb {b eee}{c}{d}zzz end"); //$NON-NLS-1$
		assertEquals(new String[]{
				"start", //$NON-NLS-1$
				"a", //$NON-NLS-1$
				"bbb", //$NON-NLS-1$
				"b eee", //$NON-NLS-1$
				"c", //$NON-NLS-1$
				"d", //$NON-NLS-1$
				"zzz end", //$NON-NLS-1$
			}, tab);

		tab = TextUtil.splitBracketsAsList("start {a}bbb {b {eee}}{c}{d}zzz end"); //$NON-NLS-1$
		assertEquals(new String[]{
				"start", //$NON-NLS-1$
				"a", //$NON-NLS-1$
				"bbb", //$NON-NLS-1$
				"b {eee}", //$NON-NLS-1$
				"c", //$NON-NLS-1$
				"d", //$NON-NLS-1$
				"zzz end", //$NON-NLS-1$
			}, tab);

		tab = TextUtil.splitBracketsAsList("start {a}bbb {b {e{e{e}f}}}{c}{d}zzz end"); //$NON-NLS-1$
		assertEquals(new String[]{
				"start", //$NON-NLS-1$
				"a", //$NON-NLS-1$
				"bbb", //$NON-NLS-1$
				"b {e{e{e}f}}", //$NON-NLS-1$
				"c", //$NON-NLS-1$
				"d", //$NON-NLS-1$
				"zzz end", //$NON-NLS-1$
			}, tab);

		tab = TextUtil.splitBracketsAsList("start {a}bbb {b {e{e{e}f}}}{}{d}zzz end"); //$NON-NLS-1$
		assertEquals(new String[]{
				"start", //$NON-NLS-1$
				"a", //$NON-NLS-1$
				"bbb", //$NON-NLS-1$
				"b {e{e{e}f}}", //$NON-NLS-1$
				"", //$NON-NLS-1$
				"d", //$NON-NLS-1$
				"zzz end", //$NON-NLS-1$
			}, tab);
	}

	/**
	 * @throws Exception
	 */
	public static void testSplitAsListCharCharStr() throws Exception {
		List<String> tab;
		
		tab = TextUtil.splitAsList('|','=',"|a=|b=|c=|d="); //$NON-NLS-1$
		assertEquals(new String[]{
				"a", //$NON-NLS-1$
				"b", //$NON-NLS-1$
				"c", //$NON-NLS-1$
				"d", //$NON-NLS-1$
			}, tab);

		tab = TextUtil.splitAsList('|','=',"start |a=bbb |b eee=|c=|d=zzz end"); //$NON-NLS-1$
		assertEquals(new String[]{
				"start", //$NON-NLS-1$
				"a", //$NON-NLS-1$
				"bbb", //$NON-NLS-1$
				"b eee", //$NON-NLS-1$
				"c", //$NON-NLS-1$
				"d", //$NON-NLS-1$
				"zzz end", //$NON-NLS-1$
			}, tab);

		tab = TextUtil.splitAsList('|','=',"start |a=bbb |b |eee==|c=|d=zzz end"); //$NON-NLS-1$
		assertEquals(new String[]{
				"start", //$NON-NLS-1$
				"a", //$NON-NLS-1$
				"bbb", //$NON-NLS-1$
				"b |eee=", //$NON-NLS-1$
				"c", //$NON-NLS-1$
				"d", //$NON-NLS-1$
				"zzz end", //$NON-NLS-1$
			}, tab);

		tab = TextUtil.splitAsList('|','=',"start |a=bbb |b |e|e|e=f===|c=|d=zzz end"); //$NON-NLS-1$
		assertEquals(new String[]{
				"start", //$NON-NLS-1$
				"a", //$NON-NLS-1$
				"bbb", //$NON-NLS-1$
				"b |e|e|e=f==", //$NON-NLS-1$
				"c", //$NON-NLS-1$
				"d", //$NON-NLS-1$
				"zzz end", //$NON-NLS-1$
			}, tab);

		tab = TextUtil.splitAsList('|','=',"start |a=bbb |b |e|e|e=f===|=|d=zzz end"); //$NON-NLS-1$
		assertEquals(new String[]{
				"start", //$NON-NLS-1$
				"a", //$NON-NLS-1$
				"bbb", //$NON-NLS-1$
				"b |e|e|e=f==", //$NON-NLS-1$
				"", //$NON-NLS-1$
				"d", //$NON-NLS-1$
				"zzz end", //$NON-NLS-1$
			}, tab);
	}

	private static void assertEquals(String[] expected, String[] actual) {
		assertEquals("arrays have not same size;", //$NON-NLS-1$
				expected.length,
				actual.length);
		for(int i=0; i<expected.length; i++) {
			assertEquals("invalid value for element at position " //$NON-NLS-1$
					+i
					+";", //$NON-NLS-1$
					expected[i],
					actual[i]);
		}
	}

	private static void assertEquals(String[] expected, List<String> actual) {
		assertEquals("arrays have not same size;", //$NON-NLS-1$
				expected.length,
				actual.size());
		for(int i=0; i<expected.length; i++) {
			assertEquals("invalid value for element at position " //$NON-NLS-1$
					+i
					+";", //$NON-NLS-1$
					expected[i],
					actual.get(i));
		}
	}
	
	/**
	 */
	public static void testParseHTML() {
		String source, expected, actual;
		
		source = Locale.getString("HTML_JAVA_SOURCE"); //$NON-NLS-1$
		expected = Locale.getString("HTML_JAVA_EXPECTED"); //$NON-NLS-1$
		
		assertNull(TextUtil.parseHTML(null));
		
		actual = TextUtil.parseHTML(source);
		assertEquals(expected, actual);
	}

	/**
	 */
	public static void testToHTML() {
		String source, expected, actual;
		
		source = Locale.getString("JAVA_HTML_SOURCE"); //$NON-NLS-1$
		expected = Locale.getString("JAVA_HTML_EXPECTED"); //$NON-NLS-1$
		
		assertNull(TextUtil.toHTML(null));
		
		actual = TextUtil.toHTML(source);
		assertEquals(expected, actual);
	}

}
