/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2020 The original authors, and other authors.
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assumptions.assumeFalse;

import java.lang.module.ModuleDescriptor;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@SuppressWarnings("all")
public class CallerTest {

	private InnerCallerTest caller;
	
	/**
	 * @throws Exception
	 */
	@BeforeEach
	public void setUp() throws Exception {
		this.caller = new InnerCallerTest();
	}
	
	/**
	 * @throws Exception
	 */
	@AfterEach
	public void tearDown() throws Exception {
		this.caller = null;
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
	@Test
	public void getCallerMethod() throws Exception {
    	assertEquals("innerinnerTestGetCallerMethod",  //$NON-NLS-1$
    			this.caller.innerinnerTestGetCallerMethod());
	}

	/**
	 * @throws Exception
	 */
	@Test
	public void getCallerClass() throws Exception {
    	assertEquals(InnerCallerTest.class, this.caller.innerinnerTestGetCallerClass());
	}

	/**
	 * @throws Exception
	 */
	@Test
	public void getCallerClassInt() throws Exception {
    	assertEquals(CallerTest.class, this.caller.innerinnerTestGetCallerClass(0));
    	assertEquals(InnerCallerTest.class, this.caller.innerinnerTestGetCallerClass(1));
    	assertEquals(CallerTest.class, this.caller.innerinnerTestGetCallerClass(2));
	}
	
	/**
	 * @throws Exception
	 */
	@Test
	public void getCallerMethodInt() throws Exception {
    	assertEquals("innerTestGetCallerMethod",  //$NON-NLS-1$
    			this.caller.innerinnerTestGetCallerMethod(0));
    	assertEquals("innerinnerTestGetCallerMethod",  //$NON-NLS-1$
    			this.caller.innerinnerTestGetCallerMethod(1));
    	assertEquals("getCallerMethodInt",  //$NON-NLS-1$
    			this.caller.innerinnerTestGetCallerMethod(2));
	}

	@Test
	public void vmutilsModuleName() throws Exception {
		final Module module = Caller.class.getModule();
		if (module != null) {
			final ModuleDescriptor descriptor = module.getDescriptor();
			if (descriptor != null) {
				final String name = descriptor.name();
				assertEquals(name, Caller.MODULE_NAME);
			} else {
				assumeFalse(true, "The testing framework does not support modules");
			}
		} else {
			assumeFalse(true, "The testing framework does not support modules");
		}
	}

	@Test
	public void findClassForFirstCallerOutsideVmutilModule() throws Exception {
		// Depending on the test framework (Junit with Maven, or Junit within Eclipse)
		// The testing code is not (or is) considered as part of the module
		Class<?> expected = Caller.getCallerClass();

		final Module module = CallerTest.class.getModule();
		if (module != null && !Caller.MODULE_NAME.equals(module.getName())) {
			expected = CallerTest.class;
		}

		Class<?> c = Caller.findClassForFirstCallerOutsideVmutilModule();
		assertEquals(expected, c);
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
