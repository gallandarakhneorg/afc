/* 
 * $Id$
 * 
 * Copyright (C) 2010-2012 Stephane GALLAND.
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
package org.arakhne.afc.math.geometry.d3.continuous;

import org.arakhne.afc.math.geometry.d3.Tuple3D;

/** 3D tuple with 3 floating-point numbers.
 * 
 * @param <T> is the implementation type of the tuple.
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class Tuple3f<T extends Tuple3D<? super T>> implements Tuple3D<T> {

	private static final long serialVersionUID = -2153633162767463917L;

	/** x coordinate.
	 */
	protected double x;

	/** y coordinate.
	 */
	protected double y;

	/** z coordinate.
	 */
	protected double z;

	/**
	 */
	public Tuple3f() {
		this.x = this.y = this.z = 0;
	}

	/**
	 * @param tuple is the tuple to copy.
	 */
	public Tuple3f(Tuple3D<?> tuple) {
		this.x = tuple.getX();
		this.y = tuple.getY();
		this.z = tuple.getZ();
	}

	/**
	 * @param tuple is the tuple to copy.
	 */
	public Tuple3f(int[] tuple) {
		this.x = tuple[0];
		this.y = tuple[1];
		this.z = tuple[2];
	}

	/**
	 * @param tuple is the tuple to copy.
	 */
	public Tuple3f(double[] tuple) {
		this.x = tuple[0];
		this.y = tuple[1];
		this.z = tuple[2];
	}

	/**
	 * @param x
	 * @param y
	 * @param z
	 */
	public Tuple3f(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	/**
	 * @param x
	 * @param y
	 * @param z
	 */
	public Tuple3f(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	/** {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
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
		this.z = Math.abs(this.z);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void absolute(T t) {
		t.set(Math.abs(this.x), Math.abs(this.y), Math.abs(this.z));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void add(int x, int y, int z) {
		this.x += x;
		this.y += y;
		this.z += z;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void add(double x, double y, double z) {
		this.x += x;
		this.y += y;
		this.z += z;
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
	public void addZ(int z) {
		this.z += z;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addZ(double z) {
		this.z += z;
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
		if (this.x < min) this.x = min;
		else if (this.x > max) this.x = max;
		if (this.y < min) this.y = min;
		else if (this.y > max) this.y = max;
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
		if (this.x < min) this.x = min;
		if (this.y < min) this.y = min;
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
		if (this.x > max) this.x = max;
		if (this.y > max) this.y = max;
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
		if (this.x < min) t.setX(min);
		else if (this.x > max) t.setX(max);
		if (this.y < min) t.setY(min);
		else if (this.y > max) t.setY(max);
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
		if (this.x < min) t.setX(min);
		if (this.y < min) t.setY(min);
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
		if (this.x > max) t.setX(max);
		if (this.y > max) t.setY(max);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void get(T t) {
		t.set(this.x, this.y, this.z);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void get(int[] t) {
		t[0] = (int)this.x;
		t[1] = (int)this.y;
		t[2] = (int)this.z;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void get(double[] t) {
		t[0] = this.x;
		t[1] = this.y;
		t[2] = this.z;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void negate(T t1) {
		this.x = -t1.getX();
		this.y = -t1.getY();
		this.z = -t1.getZ();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void negate() {
		this.x = -this.x;
		this.y = -this.y;
		this.z = -this.z;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void scale(int s, T t1) {
		this.x = s * t1.getX();
		this.y = s * t1.getY();
		this.z = s * t1.getZ();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void scale(double s, T t1) {
		this.x = (s * t1.getX());
		this.y = (s * t1.getY());
		this.z = (s * t1.getZ());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void scale(int s) {
		this.x = s * this.x;
		this.y = s * this.y;
		this.z = s * this.z;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void scale(double s) {
		this.x = (s * this.x);
		this.y = (s * this.y);
		this.z = (s * this.z);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void set(Tuple3D<?> t1) {
		this.x = t1.getX();
		this.y = t1.getY();
		this.z = t1.getZ();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void set(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void set(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void set(int[] t) {
		this.x = t[0];
		this.y = t[1];
		this.z = t[2];
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void set(double[] t) {
		this.x = t[0];
		this.y = t[1];
		this.z = t[2];
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public double getX() {
		return this.x;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int ix() {
		return (int)this.x;
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
		this.x = x;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public double getY() {
		return this.y;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int iy() {
		return (int)this.y;
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
		this.y = y;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public double getZ() {
		return this.z;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int iz() {
		return (int)this.z;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setZ(int z) {
		this.z = z;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setZ(double z) {
		this.z = z;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void sub(int x, int y, int z) {
		this.x -= x;
		this.y -= y;
		this.z -= z;
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
	public void subZ(int z) {
		this.z -= z;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void sub(double x, double y, double z) {
		this.x -= x;
		this.y -= y;
		this.z -= z;
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
	public void subZ(double z) {
		this.z -= z;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void interpolate(T t1, T t2, double alpha) {
		this.x = ((1f-alpha)*t1.getX() + alpha*t2.getX());
		this.y = ((1f-alpha)*t1.getY() + alpha*t2.getY());
		this.z = ((1f-alpha)*t1.getZ() + alpha*t2.getZ());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void interpolate(T t1, double alpha) {
		this.x = ((1f-alpha)*this.x + alpha*t1.getX());
		this.y = ((1f-alpha)*this.y + alpha*t1.getY());
		this.z = ((1f-alpha)*this.z + alpha*t1.getZ());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Tuple3D<?> t1) {
		try {
			return(this.x == t1.getX() && this.y == t1.getY() && this.z == t1.getZ());
		}
		catch (NullPointerException e2) {
			return false;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(Object t1) {
		try {
			T t2 = (T) t1;
			return(this.x == t2.getX() && this.y == t2.getY() && this.z == t2.getZ());
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
	@Override
	public boolean epsilonEquals(T t1, double epsilon) {
		double diff;

		diff = this.x - t1.getX();
		if(Double.isNaN(diff)) return false;
		if((diff<0?-diff:diff) > epsilon) return false;

		diff = this.y - t1.getY();
		if(Double.isNaN(diff)) return false;
		if((diff<0?-diff:diff) > epsilon) return false;

		diff = this.z - t1.getZ();
		if(Double.isNaN(diff)) return false;
		if((diff<0?-diff:diff) > epsilon) return false;

		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		long bits = 1;
		bits = 31 * bits + Double.doubleToLongBits(this.x);
		bits = 31 * bits + Double.doubleToLongBits(this.y);
		bits = 31 * bits + Double.doubleToLongBits(this.z);
		int b = (int) bits;
		return b ^ (b >> 32);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "(" //$NON-NLS-1$
				+this.x
				+";" //$NON-NLS-1$
				+this.y
				+";" //$NON-NLS-1$
				+this.z
				+")"; //$NON-NLS-1$
	}

}