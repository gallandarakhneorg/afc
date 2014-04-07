/* 
 * $Id$
 * 
 * Copyright (C) 2013 Stephane GALLAND.
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
package org.arakhne.afc.util;

import java.io.Serializable;

/** This class implements triplet of values. 
 *
 * @param <A> is the type of the first value.
 * @param <B> is the type of the second value.
 * @param <C> is the type of the third value.
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class Triplet<A,B,C> implements Serializable {

	private static final long serialVersionUID = -6979156649417633870L;
	
	private A a;
	private B b;
	private C c;

	/**
	 */
	public Triplet() {
		this.a = null; 
		this.b = null; 
		this.c = null; 
	}

	/**
	 * @param p
	 */
	public Triplet(Triplet<? extends A, ? extends B, ? extends C> p) {
		this.a = p.getA();
		this.b = p.getB();
		this.c = p.getC();
	}

	/**
	 * @param a
	 * @param b
	 * @param c
	 */
	public Triplet(A a, B b, C c) {
		this.a = a;
		this.b = b;
		this.c = c;
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

	/** Replies the third value of the pair.
	 * 
	 * @return the third value of the pair.
	 */
	public C getC() {
		return this.c;
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
	
	/** Set the third value of the pair.
	 * 
	 * @param c is the third value of the pair.
	 */
	public void setC(C c) {
		this.c = c;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "<"+this.a+";"+this.b+";"+this.c+">";  //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
	}

	@Override
	public boolean equals(Object obj) {
		if (obj==this) return true;
		if (obj instanceof Triplet) {
			Triplet<?,?,?> p = (Triplet<?,?,?>)obj;
			return innerEqual(this.a, p.a)
					&& innerEqual(this.b, p.b)
					&& innerEqual(this.c, p.c);
		}
		return false;
	}
	
	private static boolean innerEqual(Object a, Object b) {
		if (a==b) return true;
		if (a==null || b==null) return false;
		return a.equals(b);
	}
	
	@Override
	public int hashCode() {
		int h = HashCodeUtil.hash(this.a);
		h = HashCodeUtil.hash(h, this.b);
		h = HashCodeUtil.hash(h, this.c);
		return h;
	}

}
