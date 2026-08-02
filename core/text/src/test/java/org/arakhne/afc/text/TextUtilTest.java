/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2026 The original authors and other contributors.
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
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.arakhne.afc.testtools.AbstractTestCase;
import org.arakhne.afc.vmutil.locale.Locale;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("TextUtil")
@SuppressWarnings("all")
public class TextUtilTest extends AbstractTestCase {

	@DisplayName("cutStringAsArray")
	@Nested
	public class CutStringAsArray {

		@DisplayName("#1")
		@Test
		public void test_1() throws Exception {
			var src = Locale.getString("A_SOURCE");  //$NON-NLS-1$
			var res = Locale.getString("A_RESULT").split("\n");   //$NON-NLS-1$ //$NON-NLS-2$
			var actual = TextUtil.cutStringAsArray(src, 80);
			assertNotNull(actual);
			assertEquals(res.length, actual.length);
			for(int i=0; i<res.length; i++) {
				assertTrue(actual[i].length()<=80, "A:Line Size "+(i+1)+": "+actual[i]+" = "+actual[i].length());    //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				assertEquals(res[i], actual[i], "A:Line "+(i+1));  //$NON-NLS-1$
			}
		}

		@DisplayName("#2")
		@Test
		public void test_2() throws Exception {
			var src = Locale.getString("B_SOURCE");  //$NON-NLS-1$
			var res = Locale.getString("B_RESULT").split("\n");   //$NON-NLS-1$ //$NON-NLS-2$
			var actual = TextUtil.cutStringAsArray(src, 80);
			assertNotNull(actual);
			assertEquals(res.length, actual.length);
			for(int i=0; i<res.length; i++) {
				assertTrue(actual[i].length()<=80, "B:Line Size "+(i+1)+": "+actual[i]+" = "+actual[i].length());    //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				assertEquals(res[i], actual[i]," B:Line "+(i+1));  //$NON-NLS-1$
			}
		}

		@DisplayName("#3")
		@Test
		public void test_3() throws Exception {
			var src = Locale.getString("C_SOURCE");  //$NON-NLS-1$
			var res = Locale.getString("C_RESULT").split("\n");   //$NON-NLS-1$ //$NON-NLS-2$
			var actual = TextUtil.cutStringAsArray(src, 80);
			assertNotNull(actual);
			assertEquals(res.length, actual.length);
			for(int i=0; i<res.length; i++) {
				assertTrue(actual[i].length()<=80, "C:Line Size "+(i+1)+": "+actual[i]+" = "+actual[i].length());    //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				assertEquals(res[i], actual[i], "C:Line "+(i+1));  //$NON-NLS-1$
			}
		}

		@DisplayName("#4")
		@Test
		public void test_4() throws Exception {
			var src = Locale.getString("D_SOURCE");  //$NON-NLS-1$
			var res = Locale.getString("D_RESULT").split("\n");   //$NON-NLS-1$ //$NON-NLS-2$
			var actual = TextUtil.cutStringAsArray(src, 80);
			assertNotNull(actual);
			assertEquals(res.length, actual.length);
			for(int i=0; i<res.length; i++) {
				assertTrue(actual[i].length()<=80, "D:Line Size "+(i+1)+": "+actual[i]+" = "+actual[i].length());    //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				assertEquals(res[i], actual[i], "D:Line "+(i+1));  //$NON-NLS-1$
			}
		}
	}

	@DisplayName("cutString")
	@Nested
	public class CutString {

		@DisplayName("#1")
		@Test
		public void test_1() throws Exception {
			var src = Locale.getString("A_SOURCE");  //$NON-NLS-1$
			var res = Locale.getString("A_RESULT");  //$NON-NLS-1$
			var actual = TextUtil.cutString(src, 80);
			assertNotNull(actual);
			assertEquals(res, actual, "A:");  //$NON-NLS-1$
		}

		@DisplayName("#2")
		@Test
		public void test_2() throws Exception {
			var src = Locale.getString("B_SOURCE");  //$NON-NLS-1$
			var res = Locale.getString("B_RESULT");  //$NON-NLS-1$
			var actual = TextUtil.cutString(src, 80);
			assertNotNull(actual);
			assertEquals(res, actual, "B:");  //$NON-NLS-1$
		}

		@DisplayName("#3")
		@Test
		public void test_3() throws Exception {
			var src = Locale.getString("C_SOURCE");  //$NON-NLS-1$
			var res = Locale.getString("C_RESULT");  //$NON-NLS-1$
			var actual = TextUtil.cutString(src, 80);
			assertNotNull(actual);
			assertEquals(res, actual, "C:");  //$NON-NLS-1$
		}

		@DisplayName("#4")
		@Test
		public void test_4() throws Exception {
			var src = Locale.getString("D_SOURCE");  //$NON-NLS-1$
			var res = Locale.getString("D_RESULT");  //$NON-NLS-1$
			var actual = TextUtil.cutString(src, 80);
			assertNotNull(actual);
			assertEquals(res, actual, "D:");  //$NON-NLS-1$
		}
	}
	
	@DisplayName("splitBrackets")
	@Nested
	public class SplitBrackets {

		@DisplayName("#1")
		@Test
		public void test_1() throws Exception {
			var tab = TextUtil.splitBrackets("{a}{b}{c}{d}");  //$NON-NLS-1$
			assertArrayEquals(new String[]{
					"a",  //$NON-NLS-1$
					"b",  //$NON-NLS-1$
					"c",  //$NON-NLS-1$
					"d",  //$NON-NLS-1$
				}, tab);
		}

		@DisplayName("#2")
		@Test
		public void test_2() throws Exception {
			var tab = TextUtil.splitBrackets("start {a}bbb {b eee}{c}{d}zzz end");  //$NON-NLS-1$
			assertArrayEquals(new String[]{
					"start",  //$NON-NLS-1$
					"a",  //$NON-NLS-1$
					"bbb",  //$NON-NLS-1$
					"b eee",  //$NON-NLS-1$
					"c",  //$NON-NLS-1$
					"d",  //$NON-NLS-1$
					"zzz end",  //$NON-NLS-1$
				}, tab);
		}

		@DisplayName("#3")
		@Test
		public void test_3() throws Exception {
			var tab = TextUtil.splitBrackets("start {a}bbb {b {eee}}{c}{d}zzz end");  //$NON-NLS-1$
			assertArrayEquals(new String[]{
					"start",  //$NON-NLS-1$
					"a",  //$NON-NLS-1$
					"bbb",  //$NON-NLS-1$
					"b {eee}",  //$NON-NLS-1$
					"c",  //$NON-NLS-1$
					"d",  //$NON-NLS-1$
					"zzz end",  //$NON-NLS-1$
				}, tab);
		}

		@DisplayName("#4")
		@Test
		public void test_4() throws Exception {
			var tab = TextUtil.splitBrackets("start {a}bbb {b {e{e{e}f}}}{c}{d}zzz end");  //$NON-NLS-1$
			assertArrayEquals(new String[]{
					"start",  //$NON-NLS-1$
					"a",  //$NON-NLS-1$
					"bbb",  //$NON-NLS-1$
					"b {e{e{e}f}}",  //$NON-NLS-1$
					"c",  //$NON-NLS-1$
					"d",  //$NON-NLS-1$
					"zzz end",  //$NON-NLS-1$
				}, tab);
		}

		@DisplayName("#5")
		@Test
		public void test_5() throws Exception {
			var tab = TextUtil.splitBrackets("start {a}bbb {b {e{e{e}f}}}{}{d}zzz end");  //$NON-NLS-1$
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

		@DisplayName("#6")
		@Test
		public void test_6() {
			assertInlineParameterUsage(TextUtil.class, "splitBrackets", String.class); //$NON-NLS-1$
		}
	}
	
	@DisplayName("split")
	@Nested
	public class Split {

		@DisplayName("#1")
		@Test
		public void test_1() throws Exception {
			var tab = TextUtil.split('(',']',"(a](b](c](d]");  //$NON-NLS-1$
			assertArrayEquals(new String[]{
					"a",  //$NON-NLS-1$
					"b",  //$NON-NLS-1$
					"c",  //$NON-NLS-1$
					"d",  //$NON-NLS-1$
				}, tab);
		}

		@DisplayName("#2")
		@Test
		public void test_2() throws Exception {
			var tab = TextUtil.split('(',']',"start (a]bbb (b eee](c](d]zzz end");  //$NON-NLS-1$
			assertArrayEquals(new String[]{
					"start",  //$NON-NLS-1$
					"a",  //$NON-NLS-1$
					"bbb",  //$NON-NLS-1$
					"b eee",  //$NON-NLS-1$
					"c",  //$NON-NLS-1$
					"d",  //$NON-NLS-1$
					"zzz end",  //$NON-NLS-1$
				}, tab);
		}

		@DisplayName("#3")
		@Test
		public void test_3() throws Exception {
			var tab = TextUtil.split('(',']',"start (a]bbb (b (eee]](c](d]zzz end");  //$NON-NLS-1$
			assertArrayEquals(new String[]{
					"start",  //$NON-NLS-1$
					"a",  //$NON-NLS-1$
					"bbb",  //$NON-NLS-1$
					"b (eee]",  //$NON-NLS-1$
					"c",  //$NON-NLS-1$
					"d",  //$NON-NLS-1$
					"zzz end",  //$NON-NLS-1$
				}, tab);
		}

		@DisplayName("#4")
		@Test
		public void test_4() throws Exception {
			var tab = TextUtil.split('(',']',"start (a]bbb (b (e(e(e]f]]](c](d]zzz end");  //$NON-NLS-1$
			assertArrayEquals(new String[]{
					"start",  //$NON-NLS-1$
					"a",  //$NON-NLS-1$
					"bbb",  //$NON-NLS-1$
					"b (e(e(e]f]]",  //$NON-NLS-1$
					"c",  //$NON-NLS-1$
					"d",  //$NON-NLS-1$
					"zzz end",  //$NON-NLS-1$
				}, tab);
		}

		@DisplayName("#5")
		@Test
		public void test_5() throws Exception {
			var tab = TextUtil.split('(',']',"start (a]bbb (b (e(e(e]f]]](](d]zzz end");  //$NON-NLS-1$
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
	}

	@DisplayName("splitBracketsAsList")
	@Nested
	public class SplitBracketsAsList {

		@DisplayName("#1")
		@Test
		public void test_1() throws Exception {
			var tab = TextUtil.splitBracketsAsList("{a}{b}{c}{d}");  //$NON-NLS-1$
			assertCollectionEquals(Arrays.asList(
					"a",  //$NON-NLS-1$
					"b",  //$NON-NLS-1$
					"c",  //$NON-NLS-1$
					"d"  //$NON-NLS-1$
				), tab);
		}

		@DisplayName("#2")
		@Test
		public void test_2() throws Exception {
			var tab = TextUtil.splitBracketsAsList("start {a}bbb {b eee}{c}{d}zzz end");  //$NON-NLS-1$
			assertCollectionEquals(Arrays.asList(
					"start",  //$NON-NLS-1$
					"a",  //$NON-NLS-1$
					"bbb",  //$NON-NLS-1$
					"b eee",  //$NON-NLS-1$
					"c",  //$NON-NLS-1$
					"d",  //$NON-NLS-1$
					"zzz end"  //$NON-NLS-1$
				), tab);
		}

		@DisplayName("#3")
		@Test
		public void test_3() throws Exception {
			var tab = TextUtil.splitBracketsAsList("start {a}bbb {b {eee}}{c}{d}zzz end");  //$NON-NLS-1$
			assertCollectionEquals(Arrays.asList(
					"start",  //$NON-NLS-1$
					"a",  //$NON-NLS-1$
					"bbb",  //$NON-NLS-1$
					"b {eee}",  //$NON-NLS-1$
					"c",  //$NON-NLS-1$
					"d",  //$NON-NLS-1$
					"zzz end"  //$NON-NLS-1$
				), tab);
		}

		@DisplayName("#4")
		@Test
		public void test_4() throws Exception {
			var tab = TextUtil.splitBracketsAsList("start {a}bbb {b {e{e{e}f}}}{c}{d}zzz end");  //$NON-NLS-1$
			assertCollectionEquals(Arrays.asList(
					"start",  //$NON-NLS-1$
					"a",  //$NON-NLS-1$
					"bbb",  //$NON-NLS-1$
					"b {e{e{e}f}}",  //$NON-NLS-1$
					"c",  //$NON-NLS-1$
					"d",  //$NON-NLS-1$
					"zzz end"  //$NON-NLS-1$
				), tab);
		}

		@DisplayName("#5")
		@Test
		public void test_5() throws Exception {
			var tab = TextUtil.splitBracketsAsList("start {a}bbb {b {e{e{e}f}}}{}{d}zzz end");  //$NON-NLS-1$
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

		@DisplayName("#6")
		@Test
		public void test_6() {
			assertInlineParameterUsage(TextUtil.class, "splitBracketsAsList", String.class); //$NON-NLS-1$
		}
	}

	@DisplayName("splitAsList")
	@Nested
	public class SplitAsList {

		@DisplayName("#1")
		@Test
		public void test_1() throws Exception {
			var tab = TextUtil.splitAsList('|','=',"|a=|b=|c=|d=");  //$NON-NLS-1$
			assertCollectionEquals(Arrays.asList(
					"a",  //$NON-NLS-1$
					"b",  //$NON-NLS-1$
					"c",  //$NON-NLS-1$
					"d"  //$NON-NLS-1$
				), tab);
		}

		@DisplayName("#2")
		@Test
		public void test_2() throws Exception {
			var tab = TextUtil.splitAsList('|','=',"start |a=bbb |b eee=|c=|d=zzz end");  //$NON-NLS-1$
			assertCollectionEquals(Arrays.asList(
					"start",  //$NON-NLS-1$
					"a",  //$NON-NLS-1$
					"bbb",  //$NON-NLS-1$
					"b eee",  //$NON-NLS-1$
					"c",  //$NON-NLS-1$
					"d",  //$NON-NLS-1$
					"zzz end"  //$NON-NLS-1$
				), tab);
		}

		@DisplayName("#3")
		@Test
		public void test_3() throws Exception {
			var tab = TextUtil.splitAsList('|','=',"start |a=bbb |b |eee==|c=|d=zzz end");  //$NON-NLS-1$
			assertCollectionEquals(Arrays.asList(
					"start",  //$NON-NLS-1$
					"a",  //$NON-NLS-1$
					"bbb",  //$NON-NLS-1$
					"b |eee=",  //$NON-NLS-1$
					"c",  //$NON-NLS-1$
					"d",  //$NON-NLS-1$
					"zzz end"  //$NON-NLS-1$
				), tab);
		}

		@DisplayName("#4")
		@Test
		public void test_4() throws Exception {
			var tab = TextUtil.splitAsList('|','=',"start |a=bbb |b |e|e|e=f===|c=|d=zzz end");  //$NON-NLS-1$
			assertCollectionEquals(Arrays.asList(
					"start",  //$NON-NLS-1$
					"a",  //$NON-NLS-1$
					"bbb",  //$NON-NLS-1$
					"b |e|e|e=f==",  //$NON-NLS-1$
					"c",  //$NON-NLS-1$
					"d",  //$NON-NLS-1$
					"zzz end"  //$NON-NLS-1$
				), tab);
		}

		@DisplayName("#5")
		@Test
		public void test_5() throws Exception {
			var tab = TextUtil.splitAsList('|','=',"start |a=bbb |b |e|e|e=f===|=|d=zzz end");  //$NON-NLS-1$
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
	}
	
	@DisplayName("parseHTML")
	@Nested
	public class ParseHTML {

		@DisplayName("#1")
		@Test
		public void test_1() throws Exception {
			assertNull(TextUtil.parseHTML(null));
		}

		@DisplayName("#2")
		@Test
		public void test_2() throws Exception {
			var source = Locale.getString("HTML_JAVA_SOURCE");  //$NON-NLS-1$
			var expected = Locale.getString("HTML_JAVA_EXPECTED");  //$NON-NLS-1$
			var actual = TextUtil.parseHTML(source);
			assertEquals(expected, actual);
		}
	}

	@DisplayName("toHTML")
	@Nested
	public class ToHTML {

		@DisplayName("#1")
		@Test
		public void test_1() throws Exception {
			assertNull(TextUtil.toHTML(null));
		}

		@DisplayName("#2")
		@Test
		public void test_2() throws Exception {
			var source = Locale.getString("JAVA_HTML_SOURCE");  //$NON-NLS-1$
			var expected = Locale.getString("JAVA_HTML_EXPECTED");  //$NON-NLS-1$
			var actual = TextUtil.toHTML(source);
			assertEquals(expected, actual);
		}
	}

	@DisplayName("equalsIgnoreAccents")
	@Nested
	public class EqualsIgnoreAccents {

		@DisplayName("#1")
		@Test
		public void test_1() throws Exception {
			assertInlineParameterUsage(TextUtil.class, "equalsIgnoreAccents", String.class, String.class, Map.class); //$NON-NLS-1$
		}
	}

	@DisplayName("equalsIgnoreCaseAccents")
	@Nested
	public class EqualsIgnoreCaseAccents {

		@DisplayName("#1")
		@Test
		public void test_1() throws Exception {
			assertInlineParameterUsage(TextUtil.class, "equalsIgnoreCaseAccents", String.class, String.class, Map.class); //$NON-NLS-1$
		}
	}

	@DisplayName("join")
	@Nested
	public class Join {

		@DisplayName("(String,boolean[])")
		@Test
		public void test_1() throws Exception {
			assertInlineParameterUsage(TextUtil.class, "join", String.class, boolean[].class); //$NON-NLS-1$
		}

		@DisplayName("(String,byte[])")
		@Test
		public void test_2() throws Exception {
			assertInlineParameterUsage(TextUtil.class, "join", String.class, byte[].class); //$NON-NLS-1$
		}

		@DisplayName("(String,char[])")
		@Test
		public void joinStringCharArray() {
			assertInlineParameterUsage(TextUtil.class, "join", String.class, char[].class); //$NON-NLS-1$
		}

		@DisplayName("(String,short[])")
		@Test
		public void joinStringShortArray() {
			assertInlineParameterUsage(TextUtil.class, "join", String.class, short[].class); //$NON-NLS-1$
		}

		@DisplayName("(String,double[])")
		@Test
		public void joinStringDoubleArray() {
			assertInlineParameterUsage(TextUtil.class, "join", String.class, double[].class); //$NON-NLS-1$
		}

		@DisplayName("(String,float[])")
		@Test
		public void joinStringFloatArray() {
			assertInlineParameterUsage(TextUtil.class, "join", String.class, float[].class); //$NON-NLS-1$
		}

		@DisplayName("(String,int[])")
		@Test
		public void joinStringIntArray() {
			assertInlineParameterUsage(TextUtil.class, "join", String.class, int[].class); //$NON-NLS-1$
		}

		@DisplayName("(String,long[])")
		@Test
		public void joinStringLongArray() {
			assertInlineParameterUsage(TextUtil.class, "join", String.class, long[].class); //$NON-NLS-1$
		}

		@DisplayName("(String,Iterable)")
		@Test
		public void joinStringIterable() {
			assertInlineParameterUsage(TextUtil.class, "join", String.class, Iterable.class); //$NON-NLS-1$
		}
		
		@DisplayName("(String,String,String,Object[])")
		@Test
		public void joinStringStringStringObjectArray() {
			assertInlineParameterUsage(TextUtil.class, "join", String.class, String.class, String.class, Object[].class); //$NON-NLS-1$
		}

		@DisplayName("(String,Object[])")
		@Test
		public void joinStringObjectArray() {
			assertInlineParameterUsage(TextUtil.class, "join", String.class, Object[].class); //$NON-NLS-1$
		}
	}

	@DisplayName("mergeBrackets")
	@Nested
	public class MergeBrackets {

		@DisplayName("(Iterable)")
		@Test
		public void mergeBracketsIterable() {
			assertInlineParameterUsage(TextUtil.class, "mergeBrackets", Iterable.class); //$NON-NLS-1$
		}
	
		@DisplayName("(Object[])")
		@Test
		public void mergeBracketsObjectArray() {
			assertInlineParameterUsage(TextUtil.class, "mergeBrackets", Object[].class); //$NON-NLS-1$
		}
	}

	@DisplayName("splitBracketsAsUUIDs")
	@Nested
	public class SplitBracketsAsUUIDs {

		@DisplayName("#1")
		@Test
		public void test_1() throws Exception {
			assertInlineParameterUsage(TextUtil.class, "splitBracketsAsUUIDs", String.class); //$NON-NLS-1$
		}
	}

	@DisplayName("getLevenshteinDistance")
	@Nested
	public class GetLevenshteinDistance {

		@DisplayName("#1")
		@Test
		public void test_1() throws Exception {
			assertEquals(0, TextUtil.getLevenshteinDistance(null, null));
		}

		@DisplayName("#2")
		@Test
		public void test_2() throws Exception {
			assertEquals(0, TextUtil.getLevenshteinDistance(null, "")); //$NON-NLS-1$
		}

		@DisplayName("#3")
		@Test
		public void test_3() throws Exception {
			assertEquals(0, TextUtil.getLevenshteinDistance("", null)); //$NON-NLS-1$
		}

		@DisplayName("#4")
		@Test
		public void test_4() throws Exception {
			assertEquals(0, TextUtil.getLevenshteinDistance("", "")); //$NON-NLS-1$ //$NON-NLS-2$
		}

		@DisplayName("#5")
		@Test
		public void test_5() throws Exception {
			assertEquals(1, TextUtil.getLevenshteinDistance("", "a")); //$NON-NLS-1$ //$NON-NLS-2$
		}

		@DisplayName("#6")
		@Test
		public void test_6() throws Exception {
			assertEquals(3, TextUtil.getLevenshteinDistance("a", "abcd")); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	@DisplayName("toJavaString")
	@Nested
	public class ToJavaString {

		@DisplayName("#1")
		@Test
		public void toJavaString_01() {
			assertEquals("abc", TextUtil.toJavaString("abc")); //$NON-NLS-1$ //$NON-NLS-2$
		}

		@DisplayName("#2")
		@Test
		public void toJavaString_02() {
			assertEquals("a\\nb\\tc", TextUtil.toJavaString("a\nb\tc")); //$NON-NLS-1$ //$NON-NLS-2$
		}

		@DisplayName("#3")
		@Test
		public void toJavaString_03() {
			assertEquals("a\\\\nbc\\\\T", TextUtil.toJavaString("a\\nbc\\T")); //$NON-NLS-1$ //$NON-NLS-2$
		}

		@DisplayName("#4")
		@Test
		public void toJavaString_04() {
			assertEquals("ab/c", TextUtil.toJavaString("ab/c")); //$NON-NLS-1$ //$NON-NLS-2$
		}

		@DisplayName("#5")
		@Test
		public void toJavaString_05() {
			assertEquals("ab\\\"c", TextUtil.toJavaString("ab\"c")); //$NON-NLS-1$ //$NON-NLS-2$
		}

		@DisplayName("#6")
		@Test
		public void toJavaString_06() {
			assertEquals("ab'c", TextUtil.toJavaString("ab'c")); //$NON-NLS-1$ //$NON-NLS-2$
		}

		@DisplayName("#7")
		@Test
		public void toJavaString_07() {
			assertEquals("ab\\u0004c", TextUtil.toJavaString("ab\u0004c")); //$NON-NLS-1$ //$NON-NLS-2$
		}

		@DisplayName("#8")
		@Test
		public void toJavaString_08() {
			assertEquals("ab\\\\u0004c", TextUtil.toJavaString("ab\\u0004c")); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	@DisplayName("toJsonString")
	@Nested
	public class ToJsonString {

		@DisplayName("#1")
		@Test
		public void toJsonString_01() {
			assertEquals("abc", TextUtil.toJsonString("abc")); //$NON-NLS-1$ //$NON-NLS-2$
		}
	
		@DisplayName("#2")
		@Test
		public void toJsonString_02() {
			assertEquals("a\\nb\\tc", TextUtil.toJsonString("a\nb\tc")); //$NON-NLS-1$ //$NON-NLS-2$
		}
	
		@DisplayName("#3")
		@Test
		public void toJsonString_03() {
			assertEquals("a\\\\nbc\\\\T", TextUtil.toJsonString("a\\nbc\\T")); //$NON-NLS-1$ //$NON-NLS-2$
		}
	
		@DisplayName("#4")
		@Test
		public void toJsonString_04() {
			assertEquals("ab\\/c", TextUtil.toJsonString("ab/c")); //$NON-NLS-1$ //$NON-NLS-2$
		}
	
		@DisplayName("#5")
		@Test
		public void toJsonString_05() {
			assertEquals("ab\\\"c", TextUtil.toJsonString("ab\"c")); //$NON-NLS-1$ //$NON-NLS-2$
		}
	
		@DisplayName("#6")
		@Test
		public void toJsonString_06() {
			assertEquals("ab'c", TextUtil.toJsonString("ab'c")); //$NON-NLS-1$ //$NON-NLS-2$
		}
	
		@DisplayName("#7")
		@Test
		public void toJsonString_07() {
			assertEquals("ab\\u0004c", TextUtil.toJsonString("ab\u0004c")); //$NON-NLS-1$ //$NON-NLS-2$
		}
	
		@DisplayName("#8")
		@Test
		public void toJsonString_08() {
			assertEquals("ab\\\\u0004c", TextUtil.toJsonString("ab\\u0004c")); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	@DisplayName("formatHex")
	@Nested
	public class FormatHex {

		@DisplayName("#1")
		@Test
		public void formatHex_01() {
			assertEquals("4f", TextUtil.formatHex(0x4F, 1)); //$NON-NLS-1$
		}
	
		@DisplayName("#2")
		@Test
		public void formatHex_02() {
			assertEquals("4f", TextUtil.formatHex(0x4F, 2)); //$NON-NLS-1$
		}
	
		@DisplayName("#3")
		@Test
		public void formatHex_03() {
			assertEquals("04f", TextUtil.formatHex(0x4F, 3)); //$NON-NLS-1$
		}
	
		@DisplayName("#4")
		@Test
		public void formatHex_04() {
			assertEquals("004f", TextUtil.formatHex(0x4F, 4)); //$NON-NLS-1$
		}
	}

}
