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

/** This class implements pair of values. 
 *
 * @param <A> is the type of the first value.
 * @param <B> is the type of the second value.
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class Pair<A,B> {

	private A a;
	private B b;

	/**
	 */
	public Pair() {
		this.a = null; 
		this.b = null; 
	}

	/**
	 * @param p
	 */
	public Pair(Pair<? extends A, ? extends B> p) {
		this.a = p.getA();
		this.b = p.getB();
	}

	/**
	 * @param a
	 * @param b
	 */
	public Pair(A a, B b) {
		this.a = a;
		this.b = b;
	}
	
	/** Replies the first value of the pair.
	 * 
	 * @return the first value of the pair.
	 */
	public A getA() {
		return this.a;
	}

	/** Replies the second value of the pair.
	 * 
	 * @return the second value of the pair.
	 */
	public B getB() {
		return this.b;
	}

	/** Set the first value of the pair.
	 * 
	 * @param a is the first value of the pair.
	 */
	public void setA(A a) {
		this.a = a;
	}

	/** Set the second value of the pair.
	 * 
	 * @param b is the second value of the pair.
	 */
	public void setB(B b) {
		this.b = b;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "<"+this.a+";"+this.b+">";  //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$
	}

}
