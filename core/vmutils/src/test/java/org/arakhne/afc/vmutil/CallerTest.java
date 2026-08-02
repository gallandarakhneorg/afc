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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assumptions.assumeFalse;

import java.lang.module.ModuleDescriptor;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("Caller")
@SuppressWarnings("all")
public class CallerTest {

	private InnerCallerTest caller;
	
	@BeforeEach
	public void setUp() throws Exception {
		this.caller = new InnerCallerTest();
	}
	
	@AfterEach
	public void tearDown() throws Exception {
		this.caller = null;
	}
	
	static String innerTestGetCallerMethod() {
		String m = Caller.getCallerMethod();
		assertNotNull(m);
		return m;
	}

	static Class<?> innerTestGetCallerClass() {
		Class<?> c = Caller.getCallerClass();
		assertNotNull(c);
		return c;
	}

	static Class<?> innerTestGetCallerClass(int level) {
		Class<?> c = Caller.getCallerClass(level);
		assertNotNull(c);
		return c;
	}

	static String innerTestGetCallerMethod(int level) {
		String m = Caller.getCallerMethod(level);
		assertNotNull(m);
		return m;
	}

	@DisplayName("getCallerMethod")
	@Nested
	public class GetCallerMethod {

		@DisplayName("()")
		@Test
		public void getCallerMethod() throws Exception {
	    	assertEquals("innerinnerTestGetCallerMethod",  //$NON-NLS-1$
	    			caller.innerinnerTestGetCallerMethod());
		}

		@Test
		@DisplayName("(int) #1")
		public void getCallerMethodInt_1() throws Exception {
	    	assertEquals("innerTestGetCallerMethod",  //$NON-NLS-1$
	    			caller.innerinnerTestGetCallerMethod(0));
		}

		@Test
		@DisplayName("(int) #2")
		public void getCallerMethodInt_2() throws Exception {
	    	assertEquals("innerinnerTestGetCallerMethod",  //$NON-NLS-1$
	    			caller.innerinnerTestGetCallerMethod(1));
		}

		@Test
		@DisplayName("(int) #3")
		public void getCallerMethodInt_3() throws Exception {
	    	assertEquals("getCallerMethodInt_3",  //$NON-NLS-1$
	    			caller.innerinnerTestGetCallerMethod(2));
		}
	}

	@DisplayName("getCallerClass")
	@Nested
	public class GetCallerClass {

		@DisplayName("()")
		@Test
		public void getCallerClass() throws Exception {
	    	assertEquals(InnerCallerTest.class, caller.innerinnerTestGetCallerClass());
		}

		@Test
		@DisplayName("(int) #1")
		public void getCallerClassInt_1() throws Exception {
	    	assertEquals(CallerTest.class, caller.innerinnerTestGetCallerClass(0));
		}

		@Test
		@DisplayName("(int) #2")
		public void getCallerClassInt_2() throws Exception {
	    	assertEquals(InnerCallerTest.class, caller.innerinnerTestGetCallerClass(1));
		}

		@Test
		@DisplayName("(int) #3")
		public void getCallerClassInt_3() throws Exception {
	    	assertEquals(GetCallerClass.class, caller.innerinnerTestGetCallerClass(2));
		}
	}

	@DisplayName("Caller.MODULE_NAME")
	@Nested
	public class VmutilsModuleName {

		@Test
		@DisplayName("Caller.class.getModule().name() == Caller.MODULE_NAME")
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
	}

	@DisplayName("findClassForFirstCallerOutsideVmutilModule")
	@Nested
	public class FindClassForFirstCallerOutsideVmutilModule {

		@DisplayName("#1")
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
