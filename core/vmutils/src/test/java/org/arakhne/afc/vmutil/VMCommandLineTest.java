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

package org.arakhne.afc.vmutil;

import static org.arakhne.afc.testtools.XbaseInlineTestUtil.assertInlineParameterUsage;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("VMCommandLine")
@SuppressWarnings("all")
public class VMCommandLineTest {

	private static final String[] commandLine = new String[] { 
			"-D=true", "-v", "clean", "-v",  //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
			"-F", "-b", "-v", "package",  //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
			"-F", "123", "-nob", "installters",  //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
			"-S", "-b", "--", "-v"};  //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
	
	private static final String[] commandLine2 = new String[] {"-D=true"};  //$NON-NLS-1$

	private static final String[] optionDefinitions = new String[] {
			"D=b", "S=s", "F:f", "v+", "b!"};  //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$

	@BeforeEach
	public void setUp() throws Exception {
		VMCommandLine.saveVMParameters((Class<?>)null, new String[0]);
	}

	@DisplayName("saveVMParameters")
	@Nested
	public class SaveVMParameters {

		@DisplayName("#1")
		@Test
		public void test_1() {
			assertTrue(Arrays.equals(new String[0], VMCommandLine.getCommandLineParameters()));
		}

		@DisplayName("#2")
		@Test
		public void test_2() {
			VMCommandLine.saveVMParameters(VMCommandLineTest.class, commandLine);
			assertTrue(Arrays.equals(commandLine, VMCommandLine.getCommandLineParameters()));
		}

		@DisplayName("#3")
		@Test
		public void test_3() {
			VMCommandLine.saveVMParameters(VMCommandLineTest.class, commandLine2);
			assertTrue(Arrays.equals(commandLine2, VMCommandLine.getCommandLineParameters()));
		}

		@DisplayName("#4")
		@Test
		public void saveVMParametersClassStringArray() {
			assertInlineParameterUsage(VMCommandLine.class, "saveVMParameters", Class.class, String[].class); //$NON-NLS-1$
		}
	}

	@DisplayName("saveVMParametersIfNotSet")
	@Nested
	public class SaveVMParametersIfNotSet {

		@DisplayName("#1")
		@Test
		public void test_1() {
			assertTrue(Arrays.equals(new String[0], VMCommandLine.getCommandLineParameters()));
		}

		@DisplayName("#2")
		@Test
		public void test_2() {
			VMCommandLine.saveVMParametersIfNotSet(VMCommandLineTest.class, commandLine);
			assertTrue(Arrays.equals(commandLine, VMCommandLine.getCommandLineParameters()));
		}

		@DisplayName("#3")
		@Test
		public void test_3() {
			VMCommandLine.saveVMParametersIfNotSet(VMCommandLineTest.class, commandLine2);
			assertTrue(Arrays.equals(commandLine2, VMCommandLine.getCommandLineParameters()));
		}

		@DisplayName("#4")
		@Test
		public void saveVMParametersIfNotSetClassStringArray() {
			assertInlineParameterUsage(VMCommandLine.class, "saveVMParametersIfNotSet", Class.class, String[].class); //$NON-NLS-1$
		}
	}

	@DisplayName("shiftCommandLineParameters")
	@Nested
	public class ShiftCommandLineParameters {

		@DisplayName("#1")
		@Test
		public void test_1() {
			VMCommandLine.saveVMParameters(VMCommandLineTest.class, commandLine);
			assertEquals("-D=true", VMCommandLine.shiftCommandLineParameters());  //$NON-NLS-1$
			assertTrue(Arrays.equals(new String[] { 
					"-v", "clean", "-v",  //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
					"-F", "-b", "-v", "package",  //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
					"-F", "123", "-nob", "installters",  //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
					"-S", "-b", "--", "-v"},  //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
					VMCommandLine.getCommandLineParameters()));
		}
	}

	@DisplayName("getCommandLineOptions")
	@Nested
	public class GetCommandLineOptions {

		@DisplayName("#1")
		@Test
		public void test_1() {
			assertEquals(Collections.emptyMap(), VMCommandLine.getCommandLineOptions());
		}
	}

	@DisplayName("splitOptionsAndParameters")
	@Nested
	public class SplitOptionsAndParameters {

		private Map<String,List<Object>> options;
		private String[] parameters;

		@BeforeEach
		public void setUp() {
			VMCommandLine.saveVMParameters(VMCommandLineTest.class, commandLine);
			VMCommandLine.splitOptionsAndParameters(optionDefinitions);
			options = VMCommandLine.getCommandLineOptions();
			parameters = VMCommandLine.getCommandLineParameters();
		}

		@DisplayName("#1")
		@Test
		public void test_1() {
			assertNotNull(options);
			assertEquals(5, options.size());
		}

		@DisplayName("#2")
		@Test
		public void test_2() {
			assertTrue(options.containsKey("D"));  //$NON-NLS-1$
			var values = options.get("D");  //$NON-NLS-1$
			assertNotNull(values);
			assertEquals(1, values.size());
			assertEquals(true, values.get(0));
		}

		@DisplayName("#3")
		@Test
		public void test_3() {
			assertTrue(options.containsKey("v"));  //$NON-NLS-1$
			var values = options.get("v");  //$NON-NLS-1$
			assertNotNull(values);
			assertEquals(1, values.size());
			assertEquals((long)3, values.get(0));
		}

		@DisplayName("#4")
		@Test
		public void test_4() {
			assertTrue(options.containsKey("F"));  //$NON-NLS-1$
			var values = options.get("F");  //$NON-NLS-1$
			assertNotNull(values);
			assertEquals(2, values.size());
			assertEquals(0., values.get(0));
			assertEquals(123., values.get(1));
		}

		@DisplayName("#5")
		@Test
		public void test_5() {
			assertTrue(options.containsKey("b"));  //$NON-NLS-1$
			var values = options.get("b");  //$NON-NLS-1$
			assertNotNull(values);
			assertEquals(1, values.size());
			assertEquals(false, values.get(0));
		}

		@DisplayName("#6")
		@Test
		public void test_6() {
			assertTrue(options.containsKey("S"));  //$NON-NLS-1$
			var values = options.get("S");  //$NON-NLS-1$
			assertNotNull(values);
			assertEquals(1, values.size());
			assertEquals("-b", values.get(0));  //$NON-NLS-1$
		}

		@DisplayName("#7")
		@Test
		public void test_7() {
			var values = options.get("nob");  //$NON-NLS-1$
			assertNull(values);
		}

		@DisplayName("#8")
		@Test
		public void test_8() {
			assertNotNull(parameters);
		}

		@DisplayName("#9")
		@Test
		public void test_9() {
			assertEquals(4, parameters.length);
		}

		@DisplayName("#10")
		@Test
		public void test_10() {
			assertEquals("clean", parameters[0]);  //$NON-NLS-1$
		}

		@DisplayName("#11")
		@Test
		public void test_11() {
			assertEquals("package", parameters[1]);  //$NON-NLS-1$
		}

		@DisplayName("#12")
		@Test
		public void test_12() {
			assertEquals("installters", parameters[2]);  //$NON-NLS-1$
		}

		@DisplayName("#13")
		@Test
		public void test_13() {
			assertEquals("-v", parameters[3]);  //$NON-NLS-1$
		}
	}

	@DisplayName("getCommandLineOption")
	@Nested
	public class GetCommandLineOption {

		private List<Object> values;

		@BeforeEach
		public void setUp() {
			assertEquals(new ArrayList<>(0), VMCommandLine.getCommandLineOption("S")); //$NON-NLS-1$
			VMCommandLine.saveVMParameters(VMCommandLineTest.class, commandLine);
			VMCommandLine.splitOptionsAndParameters(optionDefinitions);
			values = VMCommandLine.getCommandLineOption("S");  //$NON-NLS-1$
		}
		
		@DisplayName("#1")
		@Test
		public void test_1() {
			assertNotNull(values);
		}
		
		@DisplayName("#2")
		@Test
		public void test_2() {
			assertEquals(1, values.size());
		}
		
		@DisplayName("#3")
		@Test
		public void test_3() {
			assertEquals("-b", values.get(0)); //$NON-NLS-1$
		}
		
		@DisplayName("#4")
		@Test
		public void test_4() {
			assertEquals(new ArrayList<>(0), VMCommandLine.getCommandLineOption("nob")); //$NON-NLS-1$
		}
	}

	@DisplayName("hasCommandLineOption")
	@Nested
	public class HasCommandLineOption {

		@BeforeEach
		public void setUp() {
			assertEquals(new ArrayList<>(0), VMCommandLine.getCommandLineOption("S")); //$NON-NLS-1$
			VMCommandLine.saveVMParameters(VMCommandLineTest.class, commandLine);
			VMCommandLine.splitOptionsAndParameters(optionDefinitions);
		}

		@DisplayName("#1")
		@Test
		public void test_1() {
			assertTrue(VMCommandLine.hasCommandLineOption("S"));  //$NON-NLS-1$
		}

		@DisplayName("#2")
		@Test
		public void test_2() {
			assertFalse(VMCommandLine.hasCommandLineOption("nob"));  //$NON-NLS-1$
		}
	}

	@DisplayName("getParameters")
	@Nested
	public class GetParameters {

		@DisplayName("#1")
		@Test
		public void testVMCommandLineClassOfQStringArray() {
			VMCommandLine c = new VMCommandLine(VMCommandLineTest.class, commandLine);
			assertTrue(Arrays.equals(commandLine, c.getParameters()));
		}

		@DisplayName("#2")
		@Test
		public void vmCommandLineClassOfQStringArrayStringArray() {
			VMCommandLine c = new VMCommandLine(VMCommandLineTest.class, optionDefinitions, commandLine);
			assertTrue(Arrays.equals(new String[] {
					"clean", "package", "installters", "-v"  //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
			}, c.getParameters()));
		}

		@DisplayName("#3")
		@Test
		public void getParameters() {
			VMCommandLine c = new VMCommandLine(VMCommandLineTest.class, optionDefinitions, commandLine);
			assertNotSame(commandLine, c.getParameters());
		}
	}

	@DisplayName("hasOption")
	@Nested
	public class HasOption {

		private VMCommandLine c;

		@BeforeEach
		public void setUp() {
			c = new VMCommandLine(VMCommandLineTest.class, optionDefinitions, commandLine);
		}

		@DisplayName("#1")
		@Test
		public void test_1() {
			assertTrue(c.hasOption("S"));  //$NON-NLS-1$
		}

		@DisplayName("#2")
		@Test
		public void test_2() {
			assertTrue(c.hasOption("b"));  //$NON-NLS-1$
		}

		@DisplayName("#3")
		@Test
		public void test_3() {
			assertFalse(c.hasOption("nob"));  //$NON-NLS-1$
		}
	}

	@DisplayName("getFirstOptionValue")
	@Nested
	public class GetFirstOptionValue {

		private VMCommandLine c;

		@BeforeEach
		public void setUp() {
			c = new VMCommandLine(VMCommandLineTest.class, optionDefinitions, commandLine);
		}

		@DisplayName("#1")
		@Test
		public void test_1() {
			assertEquals("-b", c.getFirstOptionValue("S"));   //$NON-NLS-1$ //$NON-NLS-2$
		}

		@DisplayName("#2")
		@Test
		public void test_2() {
			assertEquals(false, c.getFirstOptionValue("b"));  //$NON-NLS-1$
		}

		@DisplayName("#3")
		@Test
		public void test_3() {
			assertEquals(0., c.getFirstOptionValue("F"));  //$NON-NLS-1$
		}

		@DisplayName("#4")
		@Test
		public void test_4() {
			assertNull(c.getFirstOptionValue("nob"));  //$NON-NLS-1$
		}
	}

	@DisplayName("getOptionValues")
	@Nested
	public class GetOptionValues {

		private VMCommandLine c;

		@BeforeEach
		public void setUp() {
			c = new VMCommandLine(VMCommandLineTest.class, optionDefinitions, commandLine);
		}

		@DisplayName("#1")
		@Test
		public void test_1() {
			var values = c.getOptionValues("D");  //$NON-NLS-1$
			assertNotNull(values);
			assertEquals(1, values.size());
			assertEquals(true, values.get(0));
		}

		@DisplayName("#2")
		@Test
		public void test_2() {
			var values = c.getOptionValues("v");  //$NON-NLS-1$
			assertNotNull(values);
			assertEquals(1, values.size());
			assertEquals((long)3, values.get(0));
		}

		@DisplayName("#3")
		@Test
		public void test_3() {
			var values = c.getOptionValues("F");  //$NON-NLS-1$
			assertNotNull(values);
			assertEquals(2, values.size());
			assertEquals(0., values.get(0));
			assertEquals(123., values.get(1));
		}

		@DisplayName("#4")
		@Test
		public void test_4() {
			var values = c.getOptionValues("b");  //$NON-NLS-1$
			assertNotNull(values);
			assertEquals(1, values.size());
			assertEquals(false, values.get(0));
		}

		@DisplayName("#5")
		@Test
		public void test_5() {
			var values = c.getOptionValues("S");  //$NON-NLS-1$
			assertNotNull(values);
			assertEquals(1, values.size());
			assertEquals("-b", values.get(0)); //$NON-NLS-1$
			assertEquals(new ArrayList<>(0), c.getOptionValues("nob")); //$NON-NLS-1$	
		}
	}

	@DisplayName("shiftParameters")
	@Nested
	public class ShiftParameters {

		private VMCommandLine c;

		@BeforeEach
		public void setUp() {
			c = new VMCommandLine(VMCommandLineTest.class, optionDefinitions, commandLine);
		}

		@DisplayName("#1")
		@Test
		public void test_1() {
			assertEquals("clean", c.shiftParameters());  //$NON-NLS-1$
		}

		@DisplayName("#2")
		@Test
		public void test_2() {
			c.shiftParameters();
			assertNotSame(commandLine, c.getParameters());
		}

		@DisplayName("#3")
		@Test
		public void test_3() {
			c.shiftParameters();
			assertTrue(Arrays.equals(new String[] {
					"package", "installters", "-v"  //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			}, c.getParameters()));
		}
	}

	@DisplayName("getParameterCount")
	@Nested
	public class GetParameterCount {

		private VMCommandLine c;

		@BeforeEach
		public void setUp() {
			c = new VMCommandLine(VMCommandLineTest.class, optionDefinitions, commandLine);
		}

		@DisplayName("#1")
		@Test
		public void test_1() {
			assertEquals(4, c.getParameterCount());
		}
	}

	@DisplayName("getParameterAt")
	@Nested
	public class GetParameterAt {

		private VMCommandLine c;

		@BeforeEach
		public void setUp() {
			c = new VMCommandLine(VMCommandLineTest.class, optionDefinitions, commandLine);
		}

		@DisplayName("#1")
		@Test
		public void test_1() {
			assertEquals("clean", c.getParameterAt(0));  //$NON-NLS-1$
		}

		@DisplayName("#2")
		@Test
		public void test_2() {
			assertEquals("package", c.getParameterAt(1));  //$NON-NLS-1$
		}

		@DisplayName("#3")
		@Test
		public void test_3() {
			assertEquals("installters", c.getParameterAt(2));  //$NON-NLS-1$
		}

		@DisplayName("#4")
		@Test
		public void test_4() {
			assertEquals("-v", c.getParameterAt(3));  //$NON-NLS-1$
		}
	}

	@DisplayName("isParameterExists")
	@Nested
	public class IsParameterExists {

		private VMCommandLine c;

		@BeforeEach
		public void setUp() {
			c = new VMCommandLine(VMCommandLineTest.class, optionDefinitions, commandLine);
		}

		@DisplayName("#1")
		@Test
		public void test_1() {
			assertTrue(c.isParameterExists(0));
		}

		@DisplayName("#2")
		@Test
		public void test_2() {
			assertTrue(c.isParameterExists(1));
		}

		@DisplayName("#3")
		@Test
		public void test_3() {
			assertTrue(c.isParameterExists(2));
		}

		@DisplayName("#4")
		@Test
		public void test_4() {
			assertTrue(c.isParameterExists(3));
		}

		@DisplayName("#5")
		@Test
		public void test_5() {
			assertFalse(c.isParameterExists(5));
		}
	}

	@DisplayName("launchVMWithClassPath")
	@Nested
	public class launchVMWithClassPath {

		private VMCommandLine c;

		@BeforeEach
		public void setUp() {
			c = new VMCommandLine(VMCommandLineTest.class, optionDefinitions, commandLine);
		}

		@DisplayName("#1")
		@Test
		public void test_1() {
			assertInlineParameterUsage(VMCommandLine.class, "launchVMWithClassPath", Class.class, String.class, String[].class); //$NON-NLS-1$
		}
	
		@DisplayName("#2")
		@Test
		public void launchVMWithClassPathClassFileArrayStringArray() {
			assertInlineParameterUsage(VMCommandLine.class, "launchVMWithClassPath", Class.class, File[].class, String[].class); //$NON-NLS-1$
		}
	}

	@DisplayName("launchVM")
	@Nested
	public class LaunchVM {

		private VMCommandLine c;

		@BeforeEach
		public void setUp() {
			c = new VMCommandLine(VMCommandLineTest.class, optionDefinitions, commandLine);
		}

		@DisplayName("#1")
		@Test
		public void test_1() {
			assertInlineParameterUsage(VMCommandLine.class, "launchVM", Class.class, String[].class); //$NON-NLS-1$
		}
	
		@DisplayName("#2")
		@Test
		public void launchVMStringStringArray() {
			assertInlineParameterUsage(VMCommandLine.class, "launchVM", String.class, String[].class); //$NON-NLS-1$
		}
	}

}
