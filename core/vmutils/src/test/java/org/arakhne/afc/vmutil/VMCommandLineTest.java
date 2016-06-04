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
			"-D=true", "-v", "clean", "-v", 
			"-F", "-b", "-v", "package", 
			"-F", "123", "-nob", "installters", 
			"-S", "-b", "--", "-v"}; 
	
	private static final String[] commandLine2 = new String[] {"-D=true"}; 

	private static final String[] optionDefinitions = new String[] {
			"D=b", "S=s", "F:f", "v+", "b!"}; 

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
		assertEquals("-D=true", VMCommandLine.shiftCommandLineParameters()); 
		assertTrue(Arrays.equals(new String[] { 
				"-v", "clean", "-v", 
				"-F", "-b", "-v", "package", 
				"-F", "123", "-nob", "installters", 
				"-S", "-b", "--", "-v"}, 
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
		
		assertTrue(options.containsKey("D")); 
		values = options.get("D"); 
		assertNotNull(values);
		assertEquals(1, values.size());
		assertEquals(true, values.get(0));

		assertTrue(options.containsKey("v")); 
		values = options.get("v"); 
		assertNotNull(values);
		assertEquals(1, values.size());
		assertEquals((long)3, values.get(0));

		assertTrue(options.containsKey("F")); 
		values = options.get("F"); 
		assertNotNull(values);
		assertEquals(2, values.size());
		assertEquals(0., values.get(0));
		assertEquals(123., values.get(1));

		assertTrue(options.containsKey("b")); 
		values = options.get("b"); 
		assertNotNull(values);
		assertEquals(1, values.size());
		assertEquals(false, values.get(0));

		assertTrue(options.containsKey("S")); 
		values = options.get("S"); 
		assertNotNull(values);
		assertEquals(1, values.size());
		assertEquals("-b", values.get(0)); 
		
		values = options.get("nob"); 
		assertNull(values);

		assertNotNull(parameters);
		assertEquals(4, parameters.length);
		assertEquals("clean", parameters[0]); 
		assertEquals("package", parameters[1]); 
		assertEquals("installters", parameters[2]); 
		assertEquals("-v", parameters[3]); 
	}

	@Test
	public void getCommandLineOption() {
		assertNull(VMCommandLine.getCommandLineOption("S")); 
		
		VMCommandLine.saveVMParameters(VMCommandLineTest.class, commandLine);
		VMCommandLine.splitOptionsAndParameters(optionDefinitions);
		
		List<Object> values;
		values = VMCommandLine.getCommandLineOption("S"); 
		assertNotNull(values);
		assertEquals(1, values.size());
		assertEquals("-b", values.get(0)); 

		assertNull(VMCommandLine.getCommandLineOption("nob")); 
	}

	@Test
	public void hasCommandLineOption() {
		assertFalse(VMCommandLine.hasCommandLineOption("S")); 
		
		VMCommandLine.saveVMParameters(VMCommandLineTest.class, commandLine);
		VMCommandLine.splitOptionsAndParameters(optionDefinitions);
		
		assertTrue(VMCommandLine.hasCommandLineOption("S")); 
		assertFalse(VMCommandLine.hasCommandLineOption("nob")); 
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
				"clean", "package", "installters", "-v" 
		}, c.getParameters()));
	}

	@Test
	public void hasOption() {
		VMCommandLine c = new VMCommandLine(VMCommandLineTest.class, optionDefinitions, commandLine);
		assertTrue(c.hasOption("S")); 
		assertTrue(c.hasOption("b")); 
		assertFalse(c.hasOption("nob")); 
	}

	@Test
	public void getFirstOptionValue() {
		VMCommandLine c = new VMCommandLine(VMCommandLineTest.class, optionDefinitions, commandLine);
		assertEquals("-b", c.getFirstOptionValue("S"));  
		assertEquals(false, c.getFirstOptionValue("b")); 
		assertEquals(0., c.getFirstOptionValue("F")); 
		assertNull(c.getFirstOptionValue("nob")); 
	}

	@Test
	public void getOptionValues() {
		VMCommandLine c = new VMCommandLine(VMCommandLineTest.class, optionDefinitions, commandLine);
		List<Object> values;
		
		values = c.getOptionValues("D"); 
		assertNotNull(values);
		assertEquals(1, values.size());
		assertEquals(true, values.get(0));

		values = c.getOptionValues("v"); 
		assertNotNull(values);
		assertEquals(1, values.size());
		assertEquals((long)3, values.get(0));

		values = c.getOptionValues("F"); 
		assertNotNull(values);
		assertEquals(2, values.size());
		assertEquals(0., values.get(0));
		assertEquals(123., values.get(1));

		values = c.getOptionValues("b"); 
		assertNotNull(values);
		assertEquals(1, values.size());
		assertEquals(false, values.get(0));

		values = c.getOptionValues("S"); 
		assertNotNull(values);
		assertEquals(1, values.size());
		assertEquals("-b", values.get(0)); 

		assertNull(c.getOptionValues("nob")); 
	}

	@Test
	public void getParameters() {
		VMCommandLine c = new VMCommandLine(VMCommandLineTest.class, optionDefinitions, commandLine);
		assertNotSame(commandLine, c.getParameters());
		assertTrue(Arrays.equals(new String[] {
				"clean", "package", "installters", "-v" 
		}, c.getParameters()));
	}

	@Test
	public void shiftParameters() {
		VMCommandLine c = new VMCommandLine(VMCommandLineTest.class, optionDefinitions, commandLine);
		assertEquals("clean", c.shiftParameters()); 
		assertNotSame(commandLine, c.getParameters());
		assertTrue(Arrays.equals(new String[] {
				"package", "installters", "-v" 
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
		assertEquals("clean", c.getParameterAt(0)); 
		assertEquals("package", c.getParameterAt(1)); 
		assertEquals("installters", c.getParameterAt(2)); 
		assertEquals("-v", c.getParameterAt(3)); 
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
