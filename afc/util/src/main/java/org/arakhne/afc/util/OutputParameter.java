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
package org.arakhne.afc.util;

/** Utilities class that permits to obtain output parameters for Java functions.
 * <p>
 * The generic type describes the type of the parameter.
 * <p>
 * Example of the classic swap function:
 * <pre><code>
 * class SwapTest {
 *   public static void swap(OutputParameter<Long> a, OutputParameter<Long> b) {
 *     Long tmp = a.get();
 *     a.set(b.get();
 *     b.set(tmp);
 *   }
 *   
 *   public static void main(String[]) {
 *      OutputParameter<Long> opx = new OuputParameter(24);
 *      OutputParameter<Long> opy = new OuputParameter(36);
 *      System.out.println("before, x="+opx.get());
 *      System.out.println("before, y="+opy.get());
 *      swap(opx,opy);
 *      System.out.println("after, x="+opx.get());
 *      System.out.println("after, y="+opy.get());
 *   }
 * }
 * </code></pre>
 * <p>
 * The example outputs the text:
 * <pre><code>
 * before, x=24
 * before, y=36
 * after, x=36
 * after, y=24
 * </code></pre>
 *
 * @param <T> is the type of the value to output with this object.
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class OutputParameter<T> {

	private T object;
	
	/**
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
	
	/**
	 * {@inheritDoc}
	 * 
	 * @return {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		str.append("output:"); //$NON-NLS-1$
		str.append((this.object==null) ? this.object : this.object.toString());
		return str.toString();
	}

	/** Replies the parameter value.
	 * 
	 * @return the value embedded inside this output parameter.
	 */
	public T get() {
		return this.object;
	}

	/** Set the parameter.
	 * 
	 * @param newValue is the value of the parameter.
	 * @return the old value of the parameter. 
	 */
	public T set(T newValue) {
		T obj = this.object;
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
	public boolean isSet() {
		return this.object != null;
	}

	/** Replies if the value was not set, i.e. <code>null</code>.
	 * 
	 * @return <code>false</code> is the parameter vlaue was set, otherwise <code>true</code>
	 * @see #isSet()
	 */
	public boolean isEmpty() {
		return this.object == null;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object o) {
		return (this.object==o)
				||
				(this.object!=null && this.object.equals(o));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return this.object==null ? 0 : HashCodeUtil.hash(this.object);
	}

}
