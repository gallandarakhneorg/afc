/* $Id$
 * 
 * Copyright (C) 2007-09 Stephane GALLAND.
 * Copyright (C) 2012 Stephane GALLAND.
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
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
package org.arakhne.vmutil;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

/**
* @author $Author: galland$
* @version $Name$ $Revision$ $Date$
* @mavengroupid org.arakhne.afc
* @mavenartifactid arakhneVmutils
*/
public class VMCommandLineTest extends TestCase {

	private static final String[] commandLine = new String[] { 
			"-D=true", "-v", "clean", "-v", //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$//$NON-NLS-4$
			"-F", "-b", "-v", "package", //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$//$NON-NLS-4$
			"-F", "123", "-nob", "installters", //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$//$NON-NLS-4$
			"-S", "-b", "--", "-v"}; //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$//$NON-NLS-4$
	
	private static final String[] commandLine2 = new String[] {"-D=true"}; //$NON-NLS-1$

	private static final String[] optionDefinitions = new String[] {
			"D=b", "S=s", "F:f", "v+", "b!"}; //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$//$NON-NLS-4$//$NON-NLS-5$

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		VMCommandLine.saveVMParameters((Class<?>)null, new String[0]);
	}
	
	/**
	 */
	public static final void testSaveVMParameters() {
		assertTrue(Arrays.equals(new String[0], VMCommandLine.getCommandLineParameters()));
		VMCommandLine.saveVMParameters(VMCommandLineTest.class, commandLine);
		assertTrue(Arrays.equals(commandLine, VMCommandLine.getCommandLineParameters()));
		VMCommandLine.saveVMParameters(VMCommandLineTest.class, commandLine2);
		assertTrue(Arrays.equals(commandLine2, VMCommandLine.getCommandLineParameters()));
	}

	/**
	 */
	public static final void testSaveVMParametersIfNotSet() {
		assertTrue(Arrays.equals(new String[0], VMCommandLine.getCommandLineParameters()));
		VMCommandLine.saveVMParametersIfNotSet(VMCommandLineTest.class, commandLine);
		assertTrue(Arrays.equals(commandLine, VMCommandLine.getCommandLineParameters()));
		VMCommandLine.saveVMParametersIfNotSet(VMCommandLineTest.class, commandLine2);
		assertTrue(Arrays.equals(commandLine, VMCommandLine.getCommandLineParameters()));
	}

	/**
	 */
	public static final void testShiftCommandLineParameters() {
		VMCommandLine.saveVMParameters(VMCommandLineTest.class, commandLine);
		assertEquals("-D=true", VMCommandLine.shiftCommandLineParameters()); //$NON-NLS-1$
		assertTrue(Arrays.equals(new String[] { 
				"-v", "clean", "-v", //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
				"-F", "-b", "-v", "package", //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$//$NON-NLS-4$
				"-F", "123", "-nob", "installters", //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$//$NON-NLS-4$
				"-S", "-b", "--", "-v"}, //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$//$NON-NLS-4$
				VMCommandLine.getCommandLineParameters()));
	}

	/**
	 */
	public static final void testGetCommandLineOptions() {
		assertEquals(Collections.emptyMap(), VMCommandLine.getCommandLineOptions());
	}

	/**
	 */
	public static final void testSplitOptionsAndParameters() {
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

	/**
	 */
	public static final void testGetCommandLineOption() {
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

	/**
	 */
	public static final void testHasCommandLineOption() {
		assertFalse(VMCommandLine.hasCommandLineOption("S")); //$NON-NLS-1$
		
		VMCommandLine.saveVMParameters(VMCommandLineTest.class, commandLine);
		VMCommandLine.splitOptionsAndParameters(optionDefinitions);
		
		assertTrue(VMCommandLine.hasCommandLineOption("S")); //$NON-NLS-1$
		assertFalse(VMCommandLine.hasCommandLineOption("nob")); //$NON-NLS-1$
	}

	/**
	 */
	public static final void testVMCommandLineClassOfQStringArray() {
		VMCommandLine c = new VMCommandLine(VMCommandLineTest.class, commandLine);
		assertTrue(Arrays.equals(commandLine, c.getParameters()));
	}

	/**
	 */
	public static final void testVMCommandLineClassOfQStringArrayStringArray() {
		VMCommandLine c = new VMCommandLine(VMCommandLineTest.class, optionDefinitions, commandLine);
		assertTrue(Arrays.equals(new String[] {
				"clean", "package", "installters", "-v" //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$//$NON-NLS-4$
		}, c.getParameters()));
	}

	/**
	 */
	public static final void testHasOption() {
		VMCommandLine c = new VMCommandLine(VMCommandLineTest.class, optionDefinitions, commandLine);
		assertTrue(c.hasOption("S")); //$NON-NLS-1$
		assertTrue(c.hasOption("b")); //$NON-NLS-1$
		assertFalse(c.hasOption("nob")); //$NON-NLS-1$
	}

	/**
	 */
	public static final void testGetFirstOptionValue() {
		VMCommandLine c = new VMCommandLine(VMCommandLineTest.class, optionDefinitions, commandLine);
		assertEquals("-b", c.getFirstOptionValue("S")); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals(false, c.getFirstOptionValue("b")); //$NON-NLS-1$
		assertEquals(0., c.getFirstOptionValue("F")); //$NON-NLS-1$
		assertNull(c.getFirstOptionValue("nob")); //$NON-NLS-1$
	}

	/**
	 */
	public static final void testGetOptionValues() {
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

	/**
	 */
	public static final void testGetParameters() {
		VMCommandLine c = new VMCommandLine(VMCommandLineTest.class, optionDefinitions, commandLine);
		assertNotSame(commandLine, c.getParameters());
		assertTrue(Arrays.equals(new String[] {
				"clean", "package", "installters", "-v" //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$//$NON-NLS-4$
		}, c.getParameters()));
	}

	/**
	 */
	public static final void testShiftParameters() {
		VMCommandLine c = new VMCommandLine(VMCommandLineTest.class, optionDefinitions, commandLine);
		assertEquals("clean", c.shiftParameters()); //$NON-NLS-1$
		assertNotSame(commandLine, c.getParameters());
		assertTrue(Arrays.equals(new String[] {
				"package", "installters", "-v" //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
		}, c.getParameters()));
	}

	/**
	 */
	public static final void testGetParameterCount() {
		VMCommandLine c = new VMCommandLine(VMCommandLineTest.class, optionDefinitions, commandLine);
		assertEquals(4, c.getParameterCount());
	}

	/**
	 */
	public static final void testGetParameterAt() {
		VMCommandLine c = new VMCommandLine(VMCommandLineTest.class, optionDefinitions, commandLine);
		assertEquals("clean", c.getParameterAt(0)); //$NON-NLS-1$
		assertEquals("package", c.getParameterAt(1)); //$NON-NLS-1$
		assertEquals("installters", c.getParameterAt(2)); //$NON-NLS-1$
		assertEquals("-v", c.getParameterAt(3)); //$NON-NLS-1$
	}

	/**
	 */
	public static final void testIsParameterExists() {
		VMCommandLine c = new VMCommandLine(VMCommandLineTest.class, optionDefinitions, commandLine);
		assertTrue(c.isParameterExists(0));
		assertTrue(c.isParameterExists(1));
		assertTrue(c.isParameterExists(2));
		assertTrue(c.isParameterExists(3));
		assertFalse(c.isParameterExists(5));
	}

}
