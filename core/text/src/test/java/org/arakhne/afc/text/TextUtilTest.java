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

import static org.arakhne.afc.testtools.XbaseInlineTestUtil.assertInlineParameterUsage;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import org.arakhne.afc.testtools.AbstractTestCase;
import org.arakhne.afc.vmutil.locale.Locale;

@SuppressWarnings("all")
public class TextUtilTest extends AbstractTestCase {

	@Test
	public void cutStringAsArray() throws Exception {
		String src;
		String[] res;
		String[] actual;
		
		src = Locale.getString("A_SOURCE");  //$NON-NLS-1$
		res = Locale.getString("A_RESULT").split("\n");   //$NON-NLS-1$ //$NON-NLS-2$
		actual = TextUtil.cutStringAsArray(src, 80);
		assertNotNull(actual);
		Assert.assertEquals(res.length, actual.length);
		for(int i=0; i<res.length; i++) {
			assertTrue("A:Line Size "+(i+1)+": "+actual[i]+" = "+actual[i].length(), actual[i].length()<=80);    //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			Assert.assertEquals("A:Line "+(i+1), res[i], actual[i]);  //$NON-NLS-1$
		}

		src = Locale.getString("B_SOURCE");  //$NON-NLS-1$
		res = Locale.getString("B_RESULT").split("\n");   //$NON-NLS-1$ //$NON-NLS-2$
		actual = TextUtil.cutStringAsArray(src, 80);
		assertNotNull(actual);
		Assert.assertEquals(res.length, actual.length);
		for(int i=0; i<res.length; i++) {
			assertTrue("B:Line Size "+(i+1)+": "+actual[i]+" = "+actual[i].length(), actual[i].length()<=80);    //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			Assert.assertEquals("B:Line "+(i+1), res[i], actual[i]);  //$NON-NLS-1$
		}

		src = Locale.getString("C_SOURCE");  //$NON-NLS-1$
		res = Locale.getString("C_RESULT").split("\n");   //$NON-NLS-1$ //$NON-NLS-2$
		actual = TextUtil.cutStringAsArray(src, 80);
		assertNotNull(actual);
		Assert.assertEquals(res.length, actual.length);
		for(int i=0; i<res.length; i++) {
			assertTrue("C:Line Size "+(i+1)+": "+actual[i]+" = "+actual[i].length(), actual[i].length()<=80);    //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			Assert.assertEquals("C:Line "+(i+1), res[i], actual[i]);  //$NON-NLS-1$
		}

		src = Locale.getString("D_SOURCE");  //$NON-NLS-1$
		res = Locale.getString("D_RESULT").split("\n");   //$NON-NLS-1$ //$NON-NLS-2$
		actual = TextUtil.cutStringAsArray(src, 80);
		assertNotNull(actual);
		Assert.assertEquals(res.length, actual.length);
		for(int i=0; i<res.length; i++) {
			assertTrue("D:Line Size "+(i+1)+": "+actual[i]+" = "+actual[i].length(), actual[i].length()<=80);    //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			Assert.assertEquals("D:Line "+(i+1), res[i], actual[i]);  //$NON-NLS-1$
		}
	}

	@Test
	public void cutString() throws Exception {
		String src, res, actual;
		
		src = Locale.getString("A_SOURCE");  //$NON-NLS-1$
		res = Locale.getString("A_RESULT");  //$NON-NLS-1$
		actual = TextUtil.cutString(src, 80);
		assertNotNull(actual);
		Assert.assertEquals("A:", res, actual);  //$NON-NLS-1$

		src = Locale.getString("B_SOURCE");  //$NON-NLS-1$
		res = Locale.getString("B_RESULT");  //$NON-NLS-1$
		actual = TextUtil.cutString(src, 80);
		assertNotNull(actual);
		Assert.assertEquals("B:", res, actual);  //$NON-NLS-1$

		src = Locale.getString("C_SOURCE");  //$NON-NLS-1$
		res = Locale.getString("C_RESULT");  //$NON-NLS-1$
		actual = TextUtil.cutString(src, 80);
		assertNotNull(actual);
		Assert.assertEquals("C:", res, actual);  //$NON-NLS-1$

		src = Locale.getString("D_SOURCE");  //$NON-NLS-1$
		res = Locale.getString("D_RESULT");  //$NON-NLS-1$
		actual = TextUtil.cutString(src, 80);
		assertNotNull(actual);
		Assert.assertEquals("D:", res, actual);  //$NON-NLS-1$
	}
	
	@Test
	public void splitBrackets() throws Exception {
		String[] tab;
		
		tab = TextUtil.splitBrackets("{a}{b}{c}{d}");  //$NON-NLS-1$
		assertArrayEquals(new String[]{
				"a",  //$NON-NLS-1$
				"b",  //$NON-NLS-1$
				"c",  //$NON-NLS-1$
				"d",  //$NON-NLS-1$
			}, tab);

		tab = TextUtil.splitBrackets("start {a}bbb {b eee}{c}{d}zzz end");  //$NON-NLS-1$
		assertArrayEquals(new String[]{
				"start",  //$NON-NLS-1$
				"a",  //$NON-NLS-1$
				"bbb",  //$NON-NLS-1$
				"b eee",  //$NON-NLS-1$
				"c",  //$NON-NLS-1$
				"d",  //$NON-NLS-1$
				"zzz end",  //$NON-NLS-1$
			}, tab);

		tab = TextUtil.splitBrackets("start {a}bbb {b {eee}}{c}{d}zzz end");  //$NON-NLS-1$
		assertArrayEquals(new String[]{
				"start",  //$NON-NLS-1$
				"a",  //$NON-NLS-1$
				"bbb",  //$NON-NLS-1$
				"b {eee}",  //$NON-NLS-1$
				"c",  //$NON-NLS-1$
				"d",  //$NON-NLS-1$
				"zzz end",  //$NON-NLS-1$
			}, tab);

		tab = TextUtil.splitBrackets("start {a}bbb {b {e{e{e}f}}}{c}{d}zzz end");  //$NON-NLS-1$
		assertArrayEquals(new String[]{
				"start",  //$NON-NLS-1$
				"a",  //$NON-NLS-1$
				"bbb",  //$NON-NLS-1$
				"b {e{e{e}f}}",  //$NON-NLS-1$
				"c",  //$NON-NLS-1$
				"d",  //$NON-NLS-1$
				"zzz end",  //$NON-NLS-1$
			}, tab);

		tab = TextUtil.splitBrackets("start {a}bbb {b {e{e{e}f}}}{}{d}zzz end");  //$NON-NLS-1$
		assertArrayEquals(new String[]{
				"start",  //$NON-NLS-1$
				"a",  //$NON-NLS-1$
				"bbb",  //$NON-NLS-1$
				"b {e{e{e}f}}",  //$NON-NLS-1$
				"",  //$NON-NLS-1$
				"d",  //$NON-NLS-1$
				"zzz end",  //$NON-NLS-1$
			}, tab);
	}
	
	@Test
	public void splitCharCharStr() throws Exception {
		String[] tab;
		
		tab = TextUtil.split('(',']',"(a](b](c](d]");  //$NON-NLS-1$
		assertArrayEquals(new String[]{
				"a",  //$NON-NLS-1$
				"b",  //$NON-NLS-1$
				"c",  //$NON-NLS-1$
				"d",  //$NON-NLS-1$
			}, tab);

		tab = TextUtil.split('(',']',"start (a]bbb (b eee](c](d]zzz end");  //$NON-NLS-1$
		assertArrayEquals(new String[]{
				"start",  //$NON-NLS-1$
				"a",  //$NON-NLS-1$
				"bbb",  //$NON-NLS-1$
				"b eee",  //$NON-NLS-1$
				"c",  //$NON-NLS-1$
				"d",  //$NON-NLS-1$
				"zzz end",  //$NON-NLS-1$
			}, tab);

		tab = TextUtil.split('(',']',"start (a]bbb (b (eee]](c](d]zzz end");  //$NON-NLS-1$
		assertArrayEquals(new String[]{
				"start",  //$NON-NLS-1$
				"a",  //$NON-NLS-1$
				"bbb",  //$NON-NLS-1$
				"b (eee]",  //$NON-NLS-1$
				"c",  //$NON-NLS-1$
				"d",  //$NON-NLS-1$
				"zzz end",  //$NON-NLS-1$
			}, tab);

		tab = TextUtil.split('(',']',"start (a]bbb (b (e(e(e]f]]](c](d]zzz end");  //$NON-NLS-1$
		assertArrayEquals(new String[]{
				"start",  //$NON-NLS-1$
				"a",  //$NON-NLS-1$
				"bbb",  //$NON-NLS-1$
				"b (e(e(e]f]]",  //$NON-NLS-1$
				"c",  //$NON-NLS-1$
				"d",  //$NON-NLS-1$
				"zzz end",  //$NON-NLS-1$
			}, tab);

		tab = TextUtil.split('(',']',"start (a]bbb (b (e(e(e]f]]](](d]zzz end");  //$NON-NLS-1$
		assertArrayEquals(new String[]{
				"start",  //$NON-NLS-1$
				"a",  //$NON-NLS-1$
				"bbb",  //$NON-NLS-1$
				"b (e(e(e]f]]",  //$NON-NLS-1$
				"",  //$NON-NLS-1$
				"d",  //$NON-NLS-1$
				"zzz end",  //$NON-NLS-1$
			}, tab);
	}

	@Test
	public void splitBracketsAsList() throws Exception {
		List<String> tab;
		
		tab = TextUtil.splitBracketsAsList("{a}{b}{c}{d}");  //$NON-NLS-1$
		assertCollectionEquals(Arrays.asList(
				"a",  //$NON-NLS-1$
				"b",  //$NON-NLS-1$
				"c",  //$NON-NLS-1$
				"d"  //$NON-NLS-1$
			), tab);

		tab = TextUtil.splitBracketsAsList("start {a}bbb {b eee}{c}{d}zzz end");  //$NON-NLS-1$
		assertCollectionEquals(Arrays.asList(
				"start",  //$NON-NLS-1$
				"a",  //$NON-NLS-1$
				"bbb",  //$NON-NLS-1$
				"b eee",  //$NON-NLS-1$
				"c",  //$NON-NLS-1$
				"d",  //$NON-NLS-1$
				"zzz end"  //$NON-NLS-1$
			), tab);

		tab = TextUtil.splitBracketsAsList("start {a}bbb {b {eee}}{c}{d}zzz end");  //$NON-NLS-1$
		assertCollectionEquals(Arrays.asList(
				"start",  //$NON-NLS-1$
				"a",  //$NON-NLS-1$
				"bbb",  //$NON-NLS-1$
				"b {eee}",  //$NON-NLS-1$
				"c",  //$NON-NLS-1$
				"d",  //$NON-NLS-1$
				"zzz end"  //$NON-NLS-1$
			), tab);

		tab = TextUtil.splitBracketsAsList("start {a}bbb {b {e{e{e}f}}}{c}{d}zzz end");  //$NON-NLS-1$
		assertCollectionEquals(Arrays.asList(
				"start",  //$NON-NLS-1$
				"a",  //$NON-NLS-1$
				"bbb",  //$NON-NLS-1$
				"b {e{e{e}f}}",  //$NON-NLS-1$
				"c",  //$NON-NLS-1$
				"d",  //$NON-NLS-1$
				"zzz end"  //$NON-NLS-1$
			), tab);

		tab = TextUtil.splitBracketsAsList("start {a}bbb {b {e{e{e}f}}}{}{d}zzz end");  //$NON-NLS-1$
		assertCollectionEquals(Arrays.asList(
				"start",  //$NON-NLS-1$
				"a",  //$NON-NLS-1$
				"bbb",  //$NON-NLS-1$
				"b {e{e{e}f}}",  //$NON-NLS-1$
				"",  //$NON-NLS-1$
				"d",  //$NON-NLS-1$
				"zzz end"  //$NON-NLS-1$
			), tab);
	}

	@Test
	public void splitAsListCharCharStr() throws Exception {
		List<String> tab;
		
		tab = TextUtil.splitAsList('|','=',"|a=|b=|c=|d=");  //$NON-NLS-1$
		assertCollectionEquals(Arrays.asList(
				"a",  //$NON-NLS-1$
				"b",  //$NON-NLS-1$
				"c",  //$NON-NLS-1$
				"d"  //$NON-NLS-1$
			), tab);

		tab = TextUtil.splitAsList('|','=',"start |a=bbb |b eee=|c=|d=zzz end");  //$NON-NLS-1$
		assertCollectionEquals(Arrays.asList(
				"start",  //$NON-NLS-1$
				"a",  //$NON-NLS-1$
				"bbb",  //$NON-NLS-1$
				"b eee",  //$NON-NLS-1$
				"c",  //$NON-NLS-1$
				"d",  //$NON-NLS-1$
				"zzz end"  //$NON-NLS-1$
			), tab);

		tab = TextUtil.splitAsList('|','=',"start |a=bbb |b |eee==|c=|d=zzz end");  //$NON-NLS-1$
		assertCollectionEquals(Arrays.asList(
				"start",  //$NON-NLS-1$
				"a",  //$NON-NLS-1$
				"bbb",  //$NON-NLS-1$
				"b |eee=",  //$NON-NLS-1$
				"c",  //$NON-NLS-1$
				"d",  //$NON-NLS-1$
				"zzz end"  //$NON-NLS-1$
			), tab);

		tab = TextUtil.splitAsList('|','=',"start |a=bbb |b |e|e|e=f===|c=|d=zzz end");  //$NON-NLS-1$
		assertCollectionEquals(Arrays.asList(
				"start",  //$NON-NLS-1$
				"a",  //$NON-NLS-1$
				"bbb",  //$NON-NLS-1$
				"b |e|e|e=f==",  //$NON-NLS-1$
				"c",  //$NON-NLS-1$
				"d",  //$NON-NLS-1$
				"zzz end"  //$NON-NLS-1$
			), tab);

		tab = TextUtil.splitAsList('|','=',"start |a=bbb |b |e|e|e=f===|=|d=zzz end");  //$NON-NLS-1$
		assertCollectionEquals(Arrays.asList(
				"start",  //$NON-NLS-1$
				"a",  //$NON-NLS-1$
				"bbb",  //$NON-NLS-1$
				"b |e|e|e=f==",  //$NON-NLS-1$
				"",  //$NON-NLS-1$
				"d",  //$NON-NLS-1$
				"zzz end"  //$NON-NLS-1$
			), tab);
	}
	
	@Test
	public void parseHTML() {
		String source, expected, actual;
		
		source = Locale.getString("HTML_JAVA_SOURCE");  //$NON-NLS-1$
		expected = Locale.getString("HTML_JAVA_EXPECTED");  //$NON-NLS-1$
		
		assertNull(TextUtil.parseHTML(null));
		
		actual = TextUtil.parseHTML(source);
		Assert.assertEquals(expected, actual);
	}

	@Test
	public void toHTML() {
		String source, expected, actual;
		
		source = Locale.getString("JAVA_HTML_SOURCE");  //$NON-NLS-1$
		expected = Locale.getString("JAVA_HTML_EXPECTED");  //$NON-NLS-1$
		
		assertNull(TextUtil.toHTML(null));
		
		actual = TextUtil.toHTML(source);
		Assert.assertEquals(expected, actual);
	}

	@Test
	public void equalsIgnoreAccentsStringStringMap() {
		assertInlineParameterUsage(TextUtil.class, "equalsIgnoreAccents", String.class, String.class, Map.class); //$NON-NLS-1$
	}

	@Test
	public void equalsIgnoreCaseAccentsStringStringMap() {
		assertInlineParameterUsage(TextUtil.class, "equalsIgnoreCaseAccents", String.class, String.class, Map.class); //$NON-NLS-1$
	}

	@Test
	public void joinStringBooleanArray() {
		assertInlineParameterUsage(TextUtil.class, "join", String.class, boolean[].class); //$NON-NLS-1$
	}

	@Test
	public void joinStringByteArray() {
		assertInlineParameterUsage(TextUtil.class, "join", String.class, byte[].class); //$NON-NLS-1$
	}

	@Test
	public void joinStringCharArray() {
		assertInlineParameterUsage(TextUtil.class, "join", String.class, char[].class); //$NON-NLS-1$
	}

	@Test
	public void joinStringShortArray() {
		assertInlineParameterUsage(TextUtil.class, "join", String.class, short[].class); //$NON-NLS-1$
	}

	@Test
	public void joinStringDoubleArray() {
		assertInlineParameterUsage(TextUtil.class, "join", String.class, double[].class); //$NON-NLS-1$
	}

	@Test
	public void joinStringFloatArray() {
		assertInlineParameterUsage(TextUtil.class, "join", String.class, float[].class); //$NON-NLS-1$
	}

	@Test
	public void joinStringIntArray() {
		assertInlineParameterUsage(TextUtil.class, "join", String.class, int[].class); //$NON-NLS-1$
	}

	@Test
	public void joinStringLongArray() {
		assertInlineParameterUsage(TextUtil.class, "join", String.class, long[].class); //$NON-NLS-1$
	}

	@Test
	public void joinStringIterable() {
		assertInlineParameterUsage(TextUtil.class, "join", String.class, Iterable.class); //$NON-NLS-1$
	}
	
	@Test
	public void joinStringStringStringObjectArray() {
		assertInlineParameterUsage(TextUtil.class, "join", String.class, String.class, String.class, Object[].class); //$NON-NLS-1$
	}

	@Test
	public void joinStringObjectArray() {
		assertInlineParameterUsage(TextUtil.class, "join", String.class, Object[].class); //$NON-NLS-1$
	}
	
	@Test
	public void mergeBracketsIterable() {
		assertInlineParameterUsage(TextUtil.class, "mergeBrackets", Iterable.class); //$NON-NLS-1$
	}

	@Test
	public void mergeBracketsObjectArray() {
		assertInlineParameterUsage(TextUtil.class, "mergeBrackets", Object[].class); //$NON-NLS-1$
	}

	@Test
	public void splitBracketsString() {
		assertInlineParameterUsage(TextUtil.class, "splitBrackets", String.class); //$NON-NLS-1$
	}

	@Test
	public void splitBracketsAsListString() {
		assertInlineParameterUsage(TextUtil.class, "splitBracketsAsList", String.class); //$NON-NLS-1$
	}

	@Test
	public void splitBracketsAsUUIDsString() {
		assertInlineParameterUsage(TextUtil.class, "splitBracketsAsUUIDs", String.class); //$NON-NLS-1$
	}

	@Test
	public void getLevenshteinDistance() {
		assertEquals(0, TextUtil.getLevenshteinDistance(null, null));
		assertEquals(0, TextUtil.getLevenshteinDistance(null, "")); //$NON-NLS-1$
		assertEquals(0, TextUtil.getLevenshteinDistance("", null)); //$NON-NLS-1$
		assertEquals(0, TextUtil.getLevenshteinDistance("", "")); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals(1, TextUtil.getLevenshteinDistance("", "a")); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals(3, TextUtil.getLevenshteinDistance("a", "abcd")); //$NON-NLS-1$ //$NON-NLS-2$
	}

	@Test

	@Test
	public void formatHex_01() {
		assertEquals("4f", TextUtil.formatHex(0x4F, 1)); //$NON-NLS-1$
	}

	@Test
	public void formatHex_02() {
		assertEquals("4f", TextUtil.formatHex(0x4F, 2)); //$NON-NLS-1$
	}

	@Test
	public void formatHex_03() {
		assertEquals("04f", TextUtil.formatHex(0x4F, 3)); //$NON-NLS-1$
	}

	@Test
	public void formatHex_04() {
		assertEquals("004f", TextUtil.formatHex(0x4F, 4)); //$NON-NLS-1$
	}

}
