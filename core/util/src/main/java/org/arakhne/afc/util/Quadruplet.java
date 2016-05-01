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

/** This class implements quadruplet of values. 
 *
 * @param <A> is the type of the first value.
 * @param <B> is the type of the second value.
 * @param <C> is the type of the third value.
 * @param <D> is the type of the fourth value.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class Quadruplet<A,B,C,D> implements Serializable {

	private static final long serialVersionUID = -1357391439043190025L;
	
	private A a;
	private B b;
	private C c;
	private D d;

	/**
	 */
	public Quadruplet() {
		this.a = null; 
		this.b = null; 
		this.c = null; 
		this.d = null; 
	}

	/**
	 * @param p
	 */
	public Quadruplet(Quadruplet<? extends A, ? extends B, ? extends C, ? extends D> p) {
		this.a = p.getA();
		this.b = p.getB();
		this.c = p.getC();
		this.d = p.getD();
	}

	/**
	 * @param a
	 * @param b
	 * @param c
	 * @param d
	 */
	public Quadruplet(A a, B b, C c, D d) {
		this.a = a;
		this.b = b;
		this.c = c;
		this.d = d;
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

	/** Replies the fourth value of the pair.
	 * 
	 * @return the fourth value of the pair.
	 */
	public D getD() {
		return this.d;
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

	/** Set the fourth value of the pair.
	 * 
	 * @param d is the fourth value of the pair.
	 */
	public void setD(D d) {
		this.d = d;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "<"+this.a+";"+this.b+";"+this.c+";"+this.d+">";  //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
	}

	@Override
	public boolean equals(Object obj) {
		if (obj==this) return true;
		if (obj instanceof Quadruplet) {
			Quadruplet<?,?,?,?> p = (Quadruplet<?,?,?,?>)obj;
			return innerEqual(this.a, p.a)
					&& innerEqual(this.b, p.b)
					&& innerEqual(this.c, p.c)
					&& innerEqual(this.d, p.d);
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
		h = HashCodeUtil.hash(h, this.d);
		return h;
	}

}
