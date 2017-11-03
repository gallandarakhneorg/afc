/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2018 The original authors, and other authors.
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

package org.arakhne.afc.util;

import java.util.Objects;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.vmutil.ReflectionUtil;

/** Utilities class that permits to obtain output parameters for Java functions.
 *
 * <p>The generic type describes the type of the parameter.
 *
 * <p>Example of the classic swap function:
 * <pre><code>
 * class SwapTest {
 *   public static void swap(OutputParameter&lt;Long&gt; a, OutputParameter&lt;Long&gt; b) {
 *     Long tmp = a.get();
 *     a.set(b.get();
 *     b.set(tmp);
 *   }
 *
 *   public static void main(String[]) {
 *      OutputParameter&lt;Long&gt; opx = new OuputParameter(24);
 *      OutputParameter&lt;Long&gt; opy = new OuputParameter(36);
 *      System.out.println("before, x="+opx.get());
 *      System.out.println("before, y="+opy.get());
 *      swap(opx,opy);
 *      System.out.println("after, x="+opx.get());
 *      System.out.println("after, y="+opy.get());
 *   }
 * }
 * </code></pre>
 *
 * <p>The example outputs the text:<pre><code>
 * before, x=24
 * before, y=36
 * after, x=36
 * after, y=24
 * </code></pre>
 *
 * @param <T> is the type of the value to output with this object.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class OutputParameter<T> {

	private T object;

	/** Constructor.
	 * @param initialValue is the initial value of the output parameter.
	 */
	public OutputParameter(T initialValue) {
		this.object = initialValue;
	}

	/**
	 * Create empty output parameter.
	 */
	public OutputParameter() {
		this.object = null;
	}

	@Pure
	@Override
	public String toString() {
		return ReflectionUtil.toString(this);
	}

	/** Replies the parameter value.
	 *
	 * @return the value embedded inside this output parameter.
	 */
	@Pure
	public T get() {
		return this.object;
	}

	/** Set the parameter.
	 *
	 * @param newValue is the value of the parameter.
	 * @return the old value of the parameter.
	 */
	public T set(T newValue) {
		final T obj = this.object;
		this.object = newValue;
		return obj;
	}

	/** Clear this parameter.
	 */
	public void clear() {
		this.object = null;
	}

	/** Replies if the value was set, i.e. not <code>null</code>.
	 *
	 * @return <code>true</code> is the parameter vlaue was set, otherwise <code>false</code>
	 * @see #isEmpty()
	 */
	@Pure
	public boolean isSet() {
		return this.object != null;
	}

	/** Replies if the value was not set, i.e. <code>null</code>.
	 *
	 * @return <code>false</code> is the parameter vlaue was set, otherwise <code>true</code>
	 * @see #isSet()
	 */
	@Pure
	public boolean isEmpty() {
		return this.object == null;
	}

	@Pure
	@Override
	public boolean equals(Object obj) {
		return Objects.equals(this.object, obj);
	}

	@Pure
	@Override
	public int hashCode() {
		return Objects.hashCode(this.object);
	}

}
