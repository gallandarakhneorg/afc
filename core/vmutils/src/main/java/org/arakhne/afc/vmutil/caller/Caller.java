/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2019 The original authors, and other authors.
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

package org.arakhne.afc.vmutil.caller;

import org.eclipse.xtext.xbase.lib.Pure;

/**
 * This utility class provides a way to determine which class
 * call a function.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public interface Caller {

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
	Class<?> getCallerClass(int level);

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
     * {@code Method} instance. It is due to JRE that does not
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
	@Pure
	String getCallerMethod(int level);

	/** Replies the line number of the caller from the stack according to its level.
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
     * <p>The returned value is the line number of the calling method.
     *
     * @param level is the desired level of the class
     * @return the line number of method from the call stack
     *     according to the given level.
     */
	@Pure
	long getCallerLine(int level);

	/** Replies the filename of the method of the caller from the stack according to its level.
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
     * @param level is the desired level of the class
     * @return the filename of the method from the call
     *     stack according to the given level.
     */
	@Pure
	String getCallerFilename(int level);

}
