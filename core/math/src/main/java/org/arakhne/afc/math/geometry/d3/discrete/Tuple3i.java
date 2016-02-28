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
package org.arakhne.afc.math.geometry.d3.discrete;

import org.arakhne.afc.math.geometry.d3.Tuple3D;
import org.eclipse.xtext.xbase.lib.Pure;

/** 3D tuple with 3 integers.
 * 
 * @param <T> is the implementation type of the tuple.
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class Tuple3i<T extends Tuple3D<? super T>> implements Tuple3D<T> {

	private static final long serialVersionUID = 358537735186816489L;

	/** x coordinate.
	 */
	protected int x;

	/** y coordinate.
	 */
	protected int y;

	/** z coordinate.
	 */
	protected int z;

	/**
	 */
	public Tuple3i() {
		this.x = this.y = this.z = 0;
	}

	/**
	 * @param tuple is the tuple to copy.
	 */
	public Tuple3i(Tuple3i<?> tuple) {
		this.x = tuple.x;
		this.y = tuple.y;
		this.z = tuple.z;
	}

	/**
	 * @param tuple is the tuple to copy.
	 */
	public Tuple3i(Tuple3D<?> tuple) {
		this.x = (int)tuple.getX();
		this.y = (int)tuple.getY();
		this.z = (int)tuple.getZ();
	}

	/**
	 * @param tuple is the tuple to copy.
	 */
	public Tuple3i(int[] tuple) {
		this.x = tuple[0];
		this.y = tuple[1];
		this.z = tuple[2];
	}

	/**
	 * @param tuple is the tuple to copy.
	 */
	public Tuple3i(double[] tuple) {
		this.x = (int)tuple[0];
		this.y = (int)tuple[1];
		this.z = (int)tuple[2];
	}

	/**
	 * @param x
	 * @param y
	 * @param z
	 */
	@SuppressWarnings("hiding")
	public Tuple3i(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	/**
	 * @param x
	 * @param y
	 * @param z
	 */
	@SuppressWarnings("hiding")
	public Tuple3i(double x, double y, double z) {
		this.x = (int)x;
		this.y = (int)y;
		this.z = (int)z;
	}

	/** {@inheritDoc}
	 */
	@Pure
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
	@Override@SuppressWarnings("hiding")
	public void add(int x, int y, int z) {
		this.x += x;
		this.y += y;
		this.z += z;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override@SuppressWarnings("hiding")
	public void add(double x, double y, double z) {
		this.x += x;
		this.y += y;
		this.z += z;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override@SuppressWarnings("hiding")
	public void addX(int x) {
		this.x += x;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override@SuppressWarnings("hiding")
	public void addX(double x) {
		this.x += x;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override@SuppressWarnings("hiding")
	public void addY(int y) {
		this.y += y;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override@SuppressWarnings("hiding")
	public void addY(double y) {
		this.y += y;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override@SuppressWarnings("hiding")
	public void addZ(int z) {
		this.z += z;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override@SuppressWarnings("hiding")
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
		if (this.z < min) this.z = min;
		else if (this.z > max) this.z = max;
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
		if (this.z < min) this.z = min;
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
		if (this.z > max) this.z = max;
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
		if (this.z < min) t.setZ(min);
		else if (this.z > max) t.setZ(max);
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
		if (this.z < min) t.setZ(min);
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
		if (this.z > max) t.setZ(max);
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
		t.set(this.x, this.y, this.z);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void get(int[] t) {
		t[0] = this.x;
		t[1] = this.y;
		t[2] = this.z;
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
		this.x = -t1.ix();
		this.y = -t1.iy();
		this.z = -t1.iz();
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
		this.x = (int)(s * t1.getX());
		this.y = (int)(s * t1.getY());
		this.z = (int)(s * t1.getZ());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void scale(double s, T t1) {
		this.x = (int)(s * t1.getX());
		this.y = (int)(s * t1.getY());
		this.z = (int)(s * t1.getZ());
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
		this.x = (int)(s * this.x);
		this.y = (int)(s * this.y);
		this.z = (int)(s * this.z);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void set(Tuple3D<?> t1) {
		this.x = t1.ix();
		this.y = t1.iy();
		this.z = t1.iz();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override@SuppressWarnings("hiding")
	public void set(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override@SuppressWarnings("hiding")
	public void set(double x, double y, double z) {
		this.x = (int)x;
		this.y = (int)y;
		this.z = (int)z;
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
		this.x = (int)t[0];
		this.y = (int)t[1];
		this.z = (int)t[2];
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
	@Override@SuppressWarnings("hiding")
	public void setX(int x) {
		this.x = x;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override@SuppressWarnings("hiding")
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
	@Override@SuppressWarnings("hiding")
	public void setY(int y) {
		this.y = y;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override@SuppressWarnings("hiding")
	public void setY(double y) {
		this.y = (int)y;
	}

	/**
	 * {@inheritDoc}
	 */
	@Pure
	@Override
	public double getZ() {
		return this.z;
	}

	/**
	 * {@inheritDoc}
	 */
	@Pure
	@Override
	public int iz() {
		return this.z;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override@SuppressWarnings("hiding")
	public void setZ(int z) {
		this.z = z;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override@SuppressWarnings("hiding")
	public void setZ(double z) {
		this.z = (int)z;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override@SuppressWarnings("hiding")
	public void sub(int x, int y, int z) {
		this.x -= x;
		this.y -= y;
		this.z -= z;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override@SuppressWarnings("hiding")
	public void subX(int x) {
		this.x -= x;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override@SuppressWarnings("hiding")
	public void subY(int y) {
		this.y -= y;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override@SuppressWarnings("hiding")
	public void subZ(int z) {
		this.z -= z;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override@SuppressWarnings("hiding")
	public void sub(double x, double y, double z) {
		this.x -= x;
		this.y -= y;
		this.z -= z;
	}

	/**
	 * {@inheritDoc}
	 */@SuppressWarnings("hiding")
	@Override
	public void subX(double x) {
		this.x -= x;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override@SuppressWarnings("hiding")
	public void subY(double y) {
		this.y -= y;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override@SuppressWarnings("hiding")
	public void subZ(double z) {
		this.z -= z;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void interpolate(T t1, T t2, double alpha) {
		this.x = (int)((1f-alpha)*t1.getX() + alpha*t2.getX());
		this.y = (int)((1f-alpha)*t1.getY() + alpha*t2.getY());
		this.z = (int)((1f-alpha)*t1.getZ() + alpha*t2.getZ());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void interpolate(T t1, double alpha) {
		this.x = (int)((1f-alpha)*this.x + alpha*t1.getX());
		this.y = (int)((1f-alpha)*this.y + alpha*t1.getY());
		this.z = (int)((1f-alpha)*this.z + alpha*t1.getZ());
	}

	/**
	 * {@inheritDoc}
	 */
	@Pure
	@Override
	public boolean equals(Tuple3D<?> t1) {
		try {
			return(this.x == t1.ix() && this.y == t1.iy() && this.z == t1.iz());
		}
		catch (NullPointerException e2) {
			e2.printStackTrace();return false;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Pure
	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(Object t1) {
		try {
			T t2 = (T) t1;
			return(this.x == t2.ix() && this.y == t2.iy() && this.z == t2.iz());
		}
		catch(AssertionError e) {
			throw e;
		}
		catch (Throwable e2) {
			e2.printStackTrace();return false;
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
		
		diff = this.z - t1.getZ();
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
		bits = 31 * bits + this.z;
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
				+";" //$NON-NLS-1$
				+this.z
				+")"; //$NON-NLS-1$
	}

}