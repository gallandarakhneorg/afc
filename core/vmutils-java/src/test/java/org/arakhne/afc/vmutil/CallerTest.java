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
package org.arakhne.afc.vmutil;

import org.arakhne.afc.vmutil.Caller;

import junit.framework.TestCase;

/**
 * @author $Author: galland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid org.arakhne.afc
 * @mavenartifactid arakhneVmutils
 */
public class CallerTest extends TestCase {

	private InnerCallerTest caller;
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setUp() throws Exception {
		super.setUp();
		this.caller = new InnerCallerTest();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void tearDown() throws Exception {
		this.caller = null;
		super.tearDown();
	}
	
	/**
	 * @return caller
	 */
	static String innerTestGetCallerMethod() {
		String m = Caller.getCallerMethod();
		assertNotNull(m);
		return m;
	}

	/**
	 * @return caller
	 */
	static Class<?> innerTestGetCallerClass() {
		Class<?> c = Caller.getCallerClass();
		assertNotNull(c);
		return c;
	}

	/**
	 * @param level
	 * @return caller
	 */
	static Class<?> innerTestGetCallerClass(int level) {
		Class<?> c = Caller.getCallerClass(level);
		assertNotNull(c);
		return c;
	}

	/**
	 * @param level
	 * @return caller
	 */
	static String innerTestGetCallerMethod(int level) {
		String m = Caller.getCallerMethod(level);
		assertNotNull(m);
		return m;
	}

	/**
	 * @throws Exception
	 */
	public void testGetCallerMethod() throws Exception {
    	assertEquals("innerinnerTestGetCallerMethod", //$NON-NLS-1$
    			this.caller.innerinnerTestGetCallerMethod());
	}

	/**
	 * @throws Exception
	 */
	public void testGetCallerClass() throws Exception {
    	assertEquals(InnerCallerTest.class, this.caller.innerinnerTestGetCallerClass());
	}

	/**
	 * @throws Exception
	 */
	public void testGetCallerClassInt() throws Exception {
    	assertEquals(CallerTest.class, this.caller.innerinnerTestGetCallerClass(0));
    	assertEquals(InnerCallerTest.class, this.caller.innerinnerTestGetCallerClass(1));
    	assertEquals(CallerTest.class, this.caller.innerinnerTestGetCallerClass(2));
	}
	
	/**
	 * @throws Exception
	 */
	public void testGetCallerMethodInt() throws Exception {
    	assertEquals("innerTestGetCallerMethod", //$NON-NLS-1$
    			this.caller.innerinnerTestGetCallerMethod(0));
    	assertEquals("innerinnerTestGetCallerMethod", //$NON-NLS-1$
    			this.caller.innerinnerTestGetCallerMethod(1));
    	assertEquals("testGetCallerMethodInt", //$NON-NLS-1$
    			this.caller.innerinnerTestGetCallerMethod(2));
	}
	
	/**
	 * @author StephanStephanStephane GALLAND
	 * @version $Name$ $Revision$ $Date$
	 * @mavengroupid org.arakhne.afc
	 * @mavenartifactid arakhneVmutils
	 */
	private class InnerCallerTest {

		/**
		 */
		public InnerCallerTest() {
			//
		}
		
		public String innerinnerTestGetCallerMethod() {
			return innerTestGetCallerMethod();
		}

		public Class<?> innerinnerTestGetCallerClass() {
			return innerTestGetCallerClass();
		}

		public Class<?> innerinnerTestGetCallerClass(int level) {
			return innerTestGetCallerClass(level);
		}

		public String innerinnerTestGetCallerMethod(int level) {
			return innerTestGetCallerMethod(level);
		}

	}
	
}
