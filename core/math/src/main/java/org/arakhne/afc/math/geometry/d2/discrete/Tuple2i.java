/* 
 * $Id$
 * 
 * Copyright (C) 2011 Janus Core Developers
 * Copyright (C) 2012 Stephane GALLAND.
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
package org.arakhne.afc.math.geometry.d2.discrete;

import org.arakhne.afc.math.geometry.d2.Tuple2D;
import org.eclipse.xtext.xbase.lib.Pure;

/** 2D tuple with 2 integers.
 * 
 * @param <T> is the implementation type of the tuple.
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@SuppressWarnings("hiding")
public class Tuple2i<T extends Tuple2D<? super T>> implements Tuple2D<T> {

	private static final long serialVersionUID = -7779997414431055683L;
	
	/** x coordinate.
	 */
	protected int x;

	/** y coordinate.
	 */
	protected int y;

	/**
	 */
	public Tuple2i() {
		this.x = this.y = 0;
	}

	/**
	 * @param tuple is the tuple to copy.
	 */
	public Tuple2i(Tuple2i<?> tuple) {
		this.x = tuple.x;
		this.y = tuple.y;
	}

	/**
	 * @param tuple is the tuple to copy.
	 */
	public Tuple2i(Tuple2D<?> tuple) {
		this.x = (int)tuple.getX();
		this.y = (int)tuple.getY();
	}

	/**
	 * @param tuple is the tuple to copy.
	 */
	public Tuple2i(int[] tuple) {
		this.x = tuple[0];
		this.y = tuple[1];
	}

	/**
	 * @param tuple is the tuple to copy.
	 */
	public Tuple2i(double[] tuple) {
		this.x = (int)tuple[0];
		this.y = (int)tuple[1];
	}

	/**
	 * @param x
	 * @param y
	 */
	public Tuple2i(int x, int y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * @param x
	 * @param y
	 */
	public Tuple2i(double x, double y) {
		this.x = (int)x;
		this.y = (int)y;
	}

	/** {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Pure
	@Override
	public T clone() {
		try {
			return (T)super.clone();
		}
		catch(CloneNotSupportedException e) {
			throw new Error(e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void absolute() {
		this.x = Math.abs(this.x);
		this.y = Math.abs(this.y);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void absolute(T t) {
		t.set(Math.abs(this.x), Math.abs(this.y));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void add(int x, int y) {
		this.x += x;
		this.y += y;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void add(double x, double y) {
		this.x += x;
		this.y += y;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addX(int x) {
		this.x += x;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addX(double x) {
		this.x += x;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addY(int y) {
		this.y += y;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addY(double y) {
		this.y += y;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void clamp(int min, int max) {
		if (this.x < min) this.x = min;
		else if (this.x > max) this.x = max;
		if (this.y < min) this.y = min;
		else if (this.y > max) this.y = max;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void clamp(double min, double max) {
		clamp((int)min, (int)max);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void clampMin(int min) {
		if (this.x < min) this.x = min;
		if (this.y < min) this.y = min;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void clampMin(double min) {
		clampMin((int)min);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void clampMax(int max) {
		if (this.x > max) this.x = max;
		if (this.y > max) this.y = max;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void clampMax(double max) {
		clampMax((int)max);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void clamp(int min, int max, T t) {
		if (this.x < min) t.setX(min);
		else if (this.x > max) t.setX(max);
		if (this.y < min) t.setY(min);
		else if (this.y > max) t.setY(max);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void clamp(double min, double max, T t) {
		clamp((int)min, (int)max, t);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void clampMin(int min, T t) {
		if (this.x < min) t.setX(min);
		if (this.y < min) t.setY(min);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void clampMin(double min, T t) {
		clampMin((int)min, t);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void clampMax(int max, T t) {
		if (this.x > max) t.setX(max);
		if (this.y > max) t.setY(max);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void clampMax(double max, T t) {
		clampMax((int)max, t);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void get(T t) {
		t.set(this.x, this.y);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void get(int[] t) {
		t[0] = this.x;
		t[1] = this.y;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void get(double[] t) {
		t[0] = this.x;
		t[1] = this.y;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void negate(T t1) {
		this.x = -t1.ix();
		this.y = -t1.iy();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void negate() {
		this.x = -this.x;
		this.y = -this.y;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void scale(int s, T t1) {
		this.x = (int)(s * t1.getX());
		this.y = (int)(s * t1.getY());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void scale(double s, T t1) {
		this.x = (int)(s * t1.getX());
		this.y = (int)(s * t1.getY());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void scale(int s) {
		this.x = s * this.x;
		this.y = s * this.y;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void scale(double s) {
		this.x = (int)(s * this.x);
		this.y = (int)(s * this.y);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void set(Tuple2D<?> t1) {
		this.x = t1.ix();
		this.y = t1.iy();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void set(int x, int y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void set(double x, double y) {
		this.x = (int)x;
		this.y = (int)y;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void set(int[] t) {
		this.x = t[0];
		this.y = t[1];
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void set(double[] t) {
		this.x = (int)t[0];
		this.y = (int)t[1];
	}

	/**
	 * {@inheritDoc}
	 */
	@Pure
	@Override
	public double getX() {
		return this.x;
	}

	/**
	 * {@inheritDoc}
	 */
	@Pure
	@Override
	public int ix() {
		return this.x;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setX(int x) {
		this.x = x;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setX(double x) {
		this.x = (int)x;
	}

	/**
	 * {@inheritDoc}
	 */
	@Pure
	@Override
	public double getY() {
		return this.y;
	}

	/**
	 * {@inheritDoc}
	 */
	@Pure
	@Override
	public int iy() {
		return this.y;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setY(int y) {
		this.y = y;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setY(double y) {
		this.y = (int)y;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void sub(int x, int y) {
		this.x -= x;
		this.y -= y;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void subX(int x) {
		this.x -= x;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void subY(int y) {
		this.y -= y;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void sub(double x, double y) {
		this.x -= x;
		this.y -= y;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void subX(double x) {
		this.x -= x;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void subY(double y) {
		this.y -= y;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void interpolate(T t1, T t2, double alpha) {
		this.x = (int)((1f-alpha)*t1.getX() + alpha*t2.getX());
		this.y = (int)((1f-alpha)*t1.getY() + alpha*t2.getY());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void interpolate(T t1, double alpha) {
		this.x = (int)((1f-alpha)*this.x + alpha*t1.getX());
		this.y = (int)((1f-alpha)*this.y + alpha*t1.getY());
	}

	/**
	 * {@inheritDoc}
	 */
	@Pure
	@Override
	public boolean equals(Tuple2D<?> t1) {
		try {
			return(this.x == t1.ix() && this.y == t1.iy());
		}
		catch (NullPointerException e2) {
			return false;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Pure
	@Override
	public boolean equals(Object t1) {
		try {
			T t2 = (T) t1;
			return(this.x == t2.ix() && this.y == t2.iy());
		}
		catch(AssertionError e) {
			throw e;
		}
		catch (Throwable e2) {
			return false;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Pure
	@Override
	public boolean epsilonEquals(T t1, double epsilon) {
		double diff;

		diff = this.x - t1.getX();
		if(Double.isNaN(diff)) return false;
		if((diff<0?-diff:diff) > epsilon) return false;

		diff = this.y - t1.getY();
		if(Double.isNaN(diff)) return false;
		if((diff<0?-diff:diff) > epsilon) return false;

		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Pure
	@Override
	public int hashCode() {
		int bits = 1;
		bits = 31 * bits + this.x;
		bits = 31 * bits + this.y;
		return bits ^ (bits >> 32);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Pure
	@Override
	public String toString() {
		return "(" //$NON-NLS-1$
				+this.x
				+";" //$NON-NLS-1$
				+this.y
				+")"; //$NON-NLS-1$
	}

}