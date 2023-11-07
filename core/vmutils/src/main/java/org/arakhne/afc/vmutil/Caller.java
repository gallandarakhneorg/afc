/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2023 The original authors and other contributors.
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

import java.lang.reflect.Method;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.vmutil.caller.StackTraceCaller;

/**
 * This utility class provides a way to determine which class
 * call a function.
 *
 * <p>It inspirated from the Sun's {@code sun.reflect.Reflection} class
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public final class Caller {

	/** Name of the {@code vmutils} module that is considered by {@link #findClassForFirstCallerOutsideVmutilModule()}.
	 *
	 * @since 17.0
	 */
	public static final String MODULE_NAME = "org.arakhne.afc.core.vmutils"; //$NON-NLS-1$

	private static org.arakhne.afc.vmutil.caller.Caller caller;

	private Caller() {
		//
	}

	/** Replies the stack trace mamager used by this utility
	 * class.
	 *
	 * @return the stack trace mamager.
	 */
	@Pure
	public static org.arakhne.afc.vmutil.caller.Caller getCaller() {
		synchronized (Caller.class) {
			if (caller == null) {
				caller = new StackTraceCaller();
			}
			return caller;
		}
	}

	/** Replies the method of the caller that invoked the function
	 * from which {@code getCallerMethod()} was invoked.
	 *
	 * <p>The returned value is the name of the method instead of a
	 * {@link Method} instance. It is due to JRE that does not
	 * store in the stack trace the complete prototype of the
	 * methods. So the following code failed: the stack contains
	 * the method name "test2", but no function has the prototype
	 * {@code void test2()}.
	 * <pre>
	 * class Test {
	 *     public void test1(int a) {
	 *         test2();
	 *     }
	 *     public void test2(int a) {
	 *     	   getCallerMethod(); // IllegalArgumentException because test1() not defined.
	 *     }
	 * }
	 * </pre>
	 *
	 * <p>Another failure example:
	 * <pre>
	 * class Test2 {
	 *     public void test1(int a) {
	 *         test2();
	 *     }
	 *     public void test1() {
	 *     }
	 *     public void test2(int a) {
	 *     	   getCallerMethod(); // test1() is replied !!! not test1(int)
	 *     }
	 * }
	 * </pre>
	 *
	 * @return the method of the caller that invoked the function
	 *     from which {@code getCallerMethod()} was invoked.
	 */
	@Pure
	public static String getCallerMethod() {
    	return getCaller().getCallerMethod(2);
	}

	/** Replies the method from the stack according to its level.
	 *
	 * <p>The given {@code level} permits to specify which method to reply:
	 * <ul>
	 * <li>{@code 0}: the method where is defined the function ({@code f<sub>0</sub>})
	 * that has called {@code getCallerClass()}</li>
	 * <li>{@code 1}: the method where is defined the function ({@code f<sub>1</sub>})
	 * that has called {@code f<sub>0</sub>}</li>
	 * <li>{@code 2}: the method where is defined the function ({@code f<sub>2</sub>})
	 * that has called {@code f<sub>1</sub>}</li>
	 * <li>etc.</li>
	 * </ul>
	 *
	 * <p>The returned value is the name of the method instead of a
	 * {@link Method} instance. It is due to JRE that does not
	 * store in the stack trace the complete prototype of the
	 * methods. So the following code failed: the stack contains
	 * the method name "test2", but no function has the prototype
	 * {@code void test2()}.
	 * <pre>
	 * class Test {
	 *     public void test1(int a) {
	 *         test2();
	 *     }
	 *     public void test2(int a) {
	 *     	   getCallerMethod(); // IllegalArgumentException because test1() not defined.
	 *     }
	 * }
	 * </pre>
	 *
	 * <p>Another failure example:
	 * <pre>
	 * class Test2 {
	 *     public void test1(int a) {
	 *         test2();
	 *     }
	 *     public void test1() {
	 *     }
	 *     public void test2(int a) {
	 *     	   getCallerMethod(); // test1() is replied !!! not test1(int)
	 *     }
	 * }
	 * </pre>
	 *
	 * @param level is the desired level of the class
	 * @return the method from the call stack according to the given level.
	 */
	@Pure
	public static String getCallerMethod(int level) {
		return getCaller().getCallerMethod(level + 1);
	}

	/** Replies the class of the caller that invoked the function
	 * from which {@code getCallerClass()} was invoked.
	 *
	 * @return the class of the caller that invoked the function
	 *     from which {@code getCallerClass()} was invoked.
	 */
	@Pure
	public static Class<?> getCallerClass() {
    	return getCaller().getCallerClass(2);
	}

	/** Replies the class from the stack according to its level.
	 *
	 * <p>The given {@code level} permits to specify which class to reply:
	 * <ul>
	 * <li>{@code 0}: the class where is defined the function ({@code f<sub>0</sub>})
	 * that has called {@code getCallerClass()}</li>
	 * <li>{@code 1}: the class where is defined the function ({@code f<sub>1</sub>})
	 * that has called {@code f<sub>0</sub>}</li>
	 * <li>{@code 2}: the class where is defined the function ({@code f<sub>2</sub>})
	 * that has called {@code f<sub>1</sub>}</li>
	 * <li>etc.</li>
	 * </ul>
	 *
	 * @param level is the desired level of the class
	 * @return the class from the call stack according to the given level.
	 */
	@Pure
	public static Class<?> getCallerClass(int level) {
    	return getCaller().getCallerClass(level + 1);
	}

	/** Replies the class of the first caller that invoked the function
	 * from which {@code getCallerClass()} was invoked and is outside
	 * the vmutils module.
	 *
	 * @return the class of the caller that invoked the function
	 *     from which {@code getCallerClass()} was invoked and
	 *     outside the current module.
	 *     The {@code null} value may be replied if the caller class cannot
	 *     be retrieved.
	 * @since 17.0
	 */
	@Pure
	public static Class<?> findClassForFirstCallerOutsideVmutilModule() {
		Class<?> type = getCaller().getCallerClass(1);
		int i = 3;
		while (type != null && MODULE_NAME.equals(type.getModule().getName())) {
			type = getCaller().getCallerClass(i);
			++i;
		}
		return type;
	}

}
