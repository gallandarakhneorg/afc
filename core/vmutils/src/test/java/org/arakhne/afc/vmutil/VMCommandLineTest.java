/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2016 The original authors, and other authors.
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
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

@SuppressWarnings("all")
public class VMCommandLineTest {

	private static final String[] commandLine = new String[] { 
			"-D=true", "-v", "clean", "-v", //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$//$NON-NLS-4$
			"-F", "-b", "-v", "package", //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$//$NON-NLS-4$
			"-F", "123", "-nob", "installters", //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$//$NON-NLS-4$
			"-S", "-b", "--", "-v"}; //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$//$NON-NLS-4$
	
	private static final String[] commandLine2 = new String[] {"-D=true"}; //$NON-NLS-1$

	private static final String[] optionDefinitions = new String[] {
			"D=b", "S=s", "F:f", "v+", "b!"}; //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$//$NON-NLS-4$//$NON-NLS-5$

	@Before
	public void setUp() throws Exception {
		VMCommandLine.saveVMParameters((Class<?>)null, new String[0]);
	}
	
	/**
	 */
	@Test
	public void saveVMParameters() {
		assertTrue(Arrays.equals(new String[0], VMCommandLine.getCommandLineParameters()));
		VMCommandLine.saveVMParameters(VMCommandLineTest.class, commandLine);
		assertTrue(Arrays.equals(commandLine, VMCommandLine.getCommandLineParameters()));
		VMCommandLine.saveVMParameters(VMCommandLineTest.class, commandLine2);
		assertTrue(Arrays.equals(commandLine2, VMCommandLine.getCommandLineParameters()));
	}

	@Test
	public void saveVMParametersIfNotSet() {
		assertTrue(Arrays.equals(new String[0], VMCommandLine.getCommandLineParameters()));
		VMCommandLine.saveVMParametersIfNotSet(VMCommandLineTest.class, commandLine);
		assertTrue(Arrays.equals(commandLine, VMCommandLine.getCommandLineParameters()));
		VMCommandLine.saveVMParametersIfNotSet(VMCommandLineTest.class, commandLine2);
		assertTrue(Arrays.equals(commandLine, VMCommandLine.getCommandLineParameters()));
	}

	@Test
	public void shiftCommandLineParameters() {
		VMCommandLine.saveVMParameters(VMCommandLineTest.class, commandLine);
		assertEquals("-D=true", VMCommandLine.shiftCommandLineParameters()); //$NON-NLS-1$
		assertTrue(Arrays.equals(new String[] { 
				"-v", "clean", "-v", //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
				"-F", "-b", "-v", "package", //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$//$NON-NLS-4$
				"-F", "123", "-nob", "installters", //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$//$NON-NLS-4$
				"-S", "-b", "--", "-v"}, //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$//$NON-NLS-4$
				VMCommandLine.getCommandLineParameters()));
	}

	@Test
	public void getCommandLineOptions() {
		assertEquals(Collections.emptyMap(), VMCommandLine.getCommandLineOptions());
	}

	@Test
	public void splitOptionsAndParameters() {
		VMCommandLine.saveVMParameters(VMCommandLineTest.class, commandLine);
		VMCommandLine.splitOptionsAndParameters(optionDefinitions);

		Map<String,List<Object>> options = VMCommandLine.getCommandLineOptions();
		String[] parameters = VMCommandLine.getCommandLineParameters();
		List<Object> values;
		
		assertNotNull(options);
		assertEquals(5, options.size());
		
		assertTrue(options.containsKey("D")); //$NON-NLS-1$
		values = options.get("D"); //$NON-NLS-1$
		assertNotNull(values);
		assertEquals(1, values.size());
		assertEquals(true, values.get(0));

		assertTrue(options.containsKey("v")); //$NON-NLS-1$
		values = options.get("v"); //$NON-NLS-1$
		assertNotNull(values);
		assertEquals(1, values.size());
		assertEquals((long)3, values.get(0));

		assertTrue(options.containsKey("F")); //$NON-NLS-1$
		values = options.get("F"); //$NON-NLS-1$
		assertNotNull(values);
		assertEquals(2, values.size());
		assertEquals(0., values.get(0));
		assertEquals(123., values.get(1));

		assertTrue(options.containsKey("b")); //$NON-NLS-1$
		values = options.get("b"); //$NON-NLS-1$
		assertNotNull(values);
		assertEquals(1, values.size());
		assertEquals(false, values.get(0));

		assertTrue(options.containsKey("S")); //$NON-NLS-1$
		values = options.get("S"); //$NON-NLS-1$
		assertNotNull(values);
		assertEquals(1, values.size());
		assertEquals("-b", values.get(0)); //$NON-NLS-1$
		
		values = options.get("nob"); //$NON-NLS-1$
		assertNull(values);

		assertNotNull(parameters);
		assertEquals(4, parameters.length);
		assertEquals("clean", parameters[0]); //$NON-NLS-1$
		assertEquals("package", parameters[1]); //$NON-NLS-1$
		assertEquals("installters", parameters[2]); //$NON-NLS-1$
		assertEquals("-v", parameters[3]); //$NON-NLS-1$
	}

	@Test
	public void getCommandLineOption() {
		assertNull(VMCommandLine.getCommandLineOption("S")); //$NON-NLS-1$
		
		VMCommandLine.saveVMParameters(VMCommandLineTest.class, commandLine);
		VMCommandLine.splitOptionsAndParameters(optionDefinitions);
		
		List<Object> values;
		values = VMCommandLine.getCommandLineOption("S"); //$NON-NLS-1$
		assertNotNull(values);
		assertEquals(1, values.size());
		assertEquals("-b", values.get(0)); //$NON-NLS-1$

		assertNull(VMCommandLine.getCommandLineOption("nob")); //$NON-NLS-1$
	}

	@Test
	public void hasCommandLineOption() {
		assertFalse(VMCommandLine.hasCommandLineOption("S")); //$NON-NLS-1$
		
		VMCommandLine.saveVMParameters(VMCommandLineTest.class, commandLine);
		VMCommandLine.splitOptionsAndParameters(optionDefinitions);
		
		assertTrue(VMCommandLine.hasCommandLineOption("S")); //$NON-NLS-1$
		assertFalse(VMCommandLine.hasCommandLineOption("nob")); //$NON-NLS-1$
	}

	@Test
	public void testVMCommandLineClassOfQStringArray() {
		VMCommandLine c = new VMCommandLine(VMCommandLineTest.class, commandLine);
		assertTrue(Arrays.equals(commandLine, c.getParameters()));
	}

	@Test
	public void vmCommandLineClassOfQStringArrayStringArray() {
		VMCommandLine c = new VMCommandLine(VMCommandLineTest.class, optionDefinitions, commandLine);
		assertTrue(Arrays.equals(new String[] {
				"clean", "package", "installters", "-v" //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$//$NON-NLS-4$
		}, c.getParameters()));
	}

	@Test
	public void hasOption() {
		VMCommandLine c = new VMCommandLine(VMCommandLineTest.class, optionDefinitions, commandLine);
		assertTrue(c.hasOption("S")); //$NON-NLS-1$
		assertTrue(c.hasOption("b")); //$NON-NLS-1$
		assertFalse(c.hasOption("nob")); //$NON-NLS-1$
	}

	@Test
	public void getFirstOptionValue() {
		VMCommandLine c = new VMCommandLine(VMCommandLineTest.class, optionDefinitions, commandLine);
		assertEquals("-b", c.getFirstOptionValue("S")); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals(false, c.getFirstOptionValue("b")); //$NON-NLS-1$
		assertEquals(0., c.getFirstOptionValue("F")); //$NON-NLS-1$
		assertNull(c.getFirstOptionValue("nob")); //$NON-NLS-1$
	}

	@Test
	public void getOptionValues() {
		VMCommandLine c = new VMCommandLine(VMCommandLineTest.class, optionDefinitions, commandLine);
		List<Object> values;
		
		values = c.getOptionValues("D"); //$NON-NLS-1$
		assertNotNull(values);
		assertEquals(1, values.size());
		assertEquals(true, values.get(0));

		values = c.getOptionValues("v"); //$NON-NLS-1$
		assertNotNull(values);
		assertEquals(1, values.size());
		assertEquals((long)3, values.get(0));

		values = c.getOptionValues("F"); //$NON-NLS-1$
		assertNotNull(values);
		assertEquals(2, values.size());
		assertEquals(0., values.get(0));
		assertEquals(123., values.get(1));

		values = c.getOptionValues("b"); //$NON-NLS-1$
		assertNotNull(values);
		assertEquals(1, values.size());
		assertEquals(false, values.get(0));

		values = c.getOptionValues("S"); //$NON-NLS-1$
		assertNotNull(values);
		assertEquals(1, values.size());
		assertEquals("-b", values.get(0)); //$NON-NLS-1$

		assertNull(c.getOptionValues("nob")); //$NON-NLS-1$
	}

	@Test
	public void getParameters() {
		VMCommandLine c = new VMCommandLine(VMCommandLineTest.class, optionDefinitions, commandLine);
		assertNotSame(commandLine, c.getParameters());
		assertTrue(Arrays.equals(new String[] {
				"clean", "package", "installters", "-v" //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$//$NON-NLS-4$
		}, c.getParameters()));
	}

	@Test
	public void shiftParameters() {
		VMCommandLine c = new VMCommandLine(VMCommandLineTest.class, optionDefinitions, commandLine);
		assertEquals("clean", c.shiftParameters()); //$NON-NLS-1$
		assertNotSame(commandLine, c.getParameters());
		assertTrue(Arrays.equals(new String[] {
				"package", "installters", "-v" //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
		}, c.getParameters()));
	}

	@Test
	public void getParameterCount() {
		VMCommandLine c = new VMCommandLine(VMCommandLineTest.class, optionDefinitions, commandLine);
		assertEquals(4, c.getParameterCount());
	}

	@Test
	public void getParameterAt() {
		VMCommandLine c = new VMCommandLine(VMCommandLineTest.class, optionDefinitions, commandLine);
		assertEquals("clean", c.getParameterAt(0)); //$NON-NLS-1$
		assertEquals("package", c.getParameterAt(1)); //$NON-NLS-1$
		assertEquals("installters", c.getParameterAt(2)); //$NON-NLS-1$
		assertEquals("-v", c.getParameterAt(3)); //$NON-NLS-1$
	}

	@Test
	public void isParameterExists() {
		VMCommandLine c = new VMCommandLine(VMCommandLineTest.class, optionDefinitions, commandLine);
		assertTrue(c.isParameterExists(0));
		assertTrue(c.isParameterExists(1));
		assertTrue(c.isParameterExists(2));
		assertTrue(c.isParameterExists(3));
		assertFalse(c.isParameterExists(5));
	}

	@Test
	public void launchVMWithClassPathClassStringStringArray() {
		assertInlineParameterUsage(VMCommandLine.class, "launchVMWithClassPath", Class.class, String.class, String[].class);
	}

	@Test
	public void launchVMWithClassPathClassFileArrayStringArray() {
		assertInlineParameterUsage(VMCommandLine.class, "launchVMWithClassPath", Class.class, File[].class, String[].class);
	}

	@Test
	public void launchVMClassStringArray() {
		assertInlineParameterUsage(VMCommandLine.class, "launchVM", Class.class, String[].class);
	}

	@Test
	public void launchVMStringStringArray() {
		assertInlineParameterUsage(VMCommandLine.class, "launchVM", String.class, String[].class);
	}
	
	@Test
	public void saveVMParametersClassStringArray() {
		assertInlineParameterUsage(VMCommandLine.class, "saveVMParameters", Class.class, String[].class);
	}

	@Test
	public void saveVMParametersIfNotSetClassStringArray() {
		assertInlineParameterUsage(VMCommandLine.class, "saveVMParametersIfNotSet", Class.class, String[].class);
	}

}
