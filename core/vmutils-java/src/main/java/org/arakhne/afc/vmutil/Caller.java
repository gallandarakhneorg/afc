/* 
 * $Id$
 * 
 * Copyright (C) 2004-2008, 2012-13 Stephane GALLAND.
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

import java.lang.reflect.Method;

import org.arakhne.afc.vmutil.caller.StackTraceCaller;

/**
 * This utility class provides a way to determine which class
 * call a function.
 * <p>
 * It inspirated from the Sun's <code>sun.reflect.Reflection</code> class
 * 
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class Caller {
	
	private static org.arakhne.afc.vmutil.caller.Caller caller = null;

	/** Replies the stack trace mamager used by this utility
	 * class.
	 * 
	 * @return the stack trace mamager.
	 */
	public static org.arakhne.afc.vmutil.caller.Caller getCaller() {
		synchronized(Caller.class) {
			if (caller==null) {
				caller = new StackTraceCaller();
			}
			return caller;
		}
	}
	
	/** Replies the method of the caller that invoked the function
	 * from which <code>getCallerMethod()</code> was invoked.
	 * <p>
	 * The returned value is the name of the method instead of a
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
	 * Another failure example: 
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
	 * from which <code>getCallerMethod()</code> was invoked.
	 */
	public static String getCallerMethod() {
    	return getCaller().getCallerMethod(2);
	}

	/** Replies the class of the caller that invoked the function
	 * from which <code>getCallerClass()</code> was invoked.
	 * 
	 * @return the class of the caller that invoked the function
	 * from which <code>getCallerClass()</code> was invoked.
	 */
	public static Class<?> getCallerClass() {
    	return getCaller().getCallerClass(2);
	}
	
	/** Replies the class from the stack according to its level.
	 * <p>
	 * The given <var>level</var> permits to specify which class to reply:
	 * <ul>
	 * <li><code>0</code>: the class where is defined the function (<code>f<sub>0</sub></code>) 
	 * that has called <code>getCallerClass()</code></li>
	 * <li><code>1</code>: the class where is defined the function (<code>f<sub>1</sub></code>) 
	 * that has called <code>f<sub>0</sub></code></li>
	 * <li><code>2</code>: the class where is defined the function (<code>f<sub>2</sub></code>) 
	 * that has called <code>f<sub>1</sub></code></li>
	 * <li>etc.</li>
	 * </ul>
	 * 
	 * @param level is the desired level of the class
	 * @return the class from the call stack according to the given level.
	 */
	public static Class<?> getCallerClass(int level) {
    	return getCaller().getCallerClass(level+1);
	}

	/** Replies the method from the stack according to its level.
	 * <p>
	 * The given <var>level</var> permits to specify which method to reply:
	 * <ul>
	 * <li><code>0</code>: the method where is defined the function (<code>f<sub>0</sub></code>) 
	 * that has called <code>getCallerClass()</code></li>
	 * <li><code>1</code>: the method where is defined the function (<code>f<sub>1</sub></code>) 
	 * that has called <code>f<sub>0</sub></code></li>
	 * <li><code>2</code>: the method where is defined the function (<code>f<sub>2</sub></code>) 
	 * that has called <code>f<sub>1</sub></code></li>
	 * <li>etc.</li>
	 * </ul>
	 * <p>
	 * The returned value is the name of the method instead of a
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
	 * Another failure example: 
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
	public static String getCallerMethod(int level) {
    	return getCaller().getCallerMethod(level+1);
	}

}
