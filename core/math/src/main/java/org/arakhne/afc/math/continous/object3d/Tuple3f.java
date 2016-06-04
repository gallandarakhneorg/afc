/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2016 The original authors, and other authors.
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

package org.arakhne.afc.math.continous.object3d;

import org.arakhne.afc.math.generic.Tuple3D;

/** 3D tuple with 3 floating-point numbers.
 *
 * @param <T> is the implementation type of the tuple.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @deprecated Replacement will be provided in Version 14.0
 */
@Deprecated
@SuppressWarnings("all")
public class Tuple3f<T extends Tuple3D<? super T>> implements Tuple3D<T> {

	private static final long serialVersionUID = -2153633162767463917L;

	/** x coordinate.
	 */
	protected float x;

	/** y coordinate.
	 */
	protected float y;

	/** z coordinate.
	 */
	protected float z;

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
	public Tuple3f(float[] tuple) {
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
	public Tuple3f(float x, float y, float z) {
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
	public void add(float x, float y, float z) {
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
	public void addX(float x) {
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
	public void addY(float y) {
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
	public void addZ(float z) {
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
	public void clamp(float min, float max) {
		clamp(min, max);
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
	public void clampMin(float min) {
		clampMin(min);
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
	public void clampMax(float max) {
		clampMax(max);
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
	public void clamp(float min, float max, T t) {
		clamp(min, max, t);
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
	public void clampMin(float min, T t) {
		clampMin(min, t);
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
	public void clampMax(float max, T t) {
		clampMax(max, t);
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
	public void get(float[] t) {
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
	public void scale(float s, T t1) {
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
	public void scale(float s) {
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
	public void set(float x, float y, float z) {
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
	public void set(float[] t) {
		this.x = t[0];
		this.y = t[1];
		this.z = t[2];
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public float getX() {
		return this.x;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int x() {
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
	public void setX(float x) {
		this.x = x;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public float getY() {
		return this.y;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int y() {
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
	public void setY(float y) {
		this.y = y;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public float getZ() {
		return this.z;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int z() {
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
	public void setZ(float z) {
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
	public void sub(float x, float y, float z) {
		this.x -= x;
		this.y -= y;
		this.z -= z;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void subX(float x) {
		this.x -= x;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void subY(float y) {
		this.y -= y;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void subZ(float z) {
		this.z -= z;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void interpolate(T t1, T t2, float alpha) {
		this.x = ((1f-alpha)*t1.getX() + alpha*t2.getX());
		this.y = ((1f-alpha)*t1.getY() + alpha*t2.getY());
		this.z = ((1f-alpha)*t1.getZ() + alpha*t2.getZ());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void interpolate(T t1, float alpha) {
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
	public boolean epsilonEquals(T t1, float epsilon) {
		float diff;

		diff = this.x - t1.getX();
		if(Float.isNaN(diff)) return false;
		if((diff<0?-diff:diff) > epsilon) return false;

		diff = this.y - t1.getY();
		if(Float.isNaN(diff)) return false;
		if((diff<0?-diff:diff) > epsilon) return false;

		diff = this.z - t1.getZ();
		if(Float.isNaN(diff)) return false;
		if((diff<0?-diff:diff) > epsilon) return false;

		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		int bits = 1;
		bits = 31 * bits + Float.floatToIntBits(this.x);
		bits = 31 * bits + Float.floatToIntBits(this.y);
		bits = 31 * bits + Float.floatToIntBits(this.z);
		return bits ^ (bits >> 32);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "(" 
				+this.x
				+";" 
				+this.y
				+";" 
				+this.z
				+")"; 
	}

}
