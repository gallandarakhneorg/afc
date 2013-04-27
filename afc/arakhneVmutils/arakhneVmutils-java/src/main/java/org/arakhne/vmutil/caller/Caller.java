/* 
 * $Id$
 * 
 * Copyright (C) 2012 Stephane GALLAND.
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
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

package org.arakhne.vmutil.caller;

/**
 * This utility class provides a way to determine which class
 * call a function.
 * 
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public interface Caller {
	
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
	public Class<?> getCallerClass(int level);

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
	 * <code>Method</code> instance. It is due to JRE that does not
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
	public String getCallerMethod(int level);

	/** Replies the line number of the caller from the stack according to its level.
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
	 * The returned value is the line number of the calling method.
	 * 
	 * @param level is the desired level of the class
	 * @return the line number of method from the call stack
	 * according to the given level.
	 */
	public long getCallerLine(int level);

	/** Replies the filename of the method of the caller from the stack according to its level.
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
	 * 
	 * @param level is the desired level of the class
	 * @return the filename of the method from the call
	 * stack according to the given level.
	 */
	public String getCallerFilename(int level);

}
