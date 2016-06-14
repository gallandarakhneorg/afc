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

package org.arakhne.afc.math.geometry.d3.i;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.geometry.d3.Tuple3D;

/** 3D tuple with 3 integer numbers.
 *
 * @param <RT> is the replied type by the tuple.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public class Tuple3i<RT extends Tuple3i<? super RT>> implements Tuple3D<RT> {

	private static final long serialVersionUID = 3136314939750740492L;

	/** x coordinate.
	 */
	int x;

	/** y coordinate.
	 */
	int y;

	/** z coordinate.
	 */
	int z;

	/** Construct aa zero tuple.
     */
	public Tuple3i() {
		this.x = 0;
		this.y = 0;
		this.z = 0;
	}

	/** Constructor by copy.
     * @param tuple is the tuple to copy.
     */
	public Tuple3i(Tuple3i<?> tuple) {
		assert tuple != null : "Input tuple must not be null";
		this.x = tuple.x;
		this.y = tuple.y;
		this.z = tuple.z;
	}

	/** Constructor by copy.
     * @param tuple is the tuple to copy.
     */
	public Tuple3i(Tuple3D<?> tuple) {
		assert tuple != null : "Input tuple must not be null";
		this.x = tuple.ix();
		this.y = tuple.iy();
		this.z = tuple.iz();
	}

	/**
	 * @param tuple is the tuple to copy.
	 */
	public Tuple3i(int[] tuple) {
		assert tuple != null : "Input tuple must not be null";
		assert tuple.length >= 3 : "Size of the input array is too small";
		this.x = tuple[0];
		this.y = tuple[1];
		this.z = tuple[2];
	}

	/**
	 * @param tuple is the tuple to copy.
	 */
	public Tuple3i(double[] tuple) {
		assert tuple != null : "Input tuple must not be null";
		assert tuple.length >= 3 : "Size of the input array is too small";
		this.x = (int) Math.round(tuple[0]);
		this.y = (int) Math.round(tuple[1]);
		this.z = (int) Math.round(tuple[2]);
	}

	/** Construct a tuple with the given coordinates.
     * @param x x coordinate.
     * @param y y coordinate.
     * @param z z coordinate.
     */
	public Tuple3i(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	/** Construct a tuple with the given coordinates.
     * @param x x coordinate.
     * @param y y coordinate.
     * @param z z coordinate.
     */
	public Tuple3i(double x, double y, double z) {
		this.x = (int) Math.round(x);
		this.y = (int) Math.round(y);
		this.z = (int) Math.round(z);
	}

	@SuppressWarnings("unchecked")
	@Pure
	@Override
	public RT clone() {
		try {
			return (RT) super.clone();
        } catch (CloneNotSupportedException e) {
			throw new InternalError(e);
		}
	}

	@Override
	public void absolute() {
		this.x = Math.abs(this.x);
		this.y = Math.abs(this.y);
		this.z = Math.abs(this.z);
	}

	@Override
	public void absolute(Tuple3D<?> tuple) {
		assert tuple != null : "Output tuple must not be null";
		tuple.set(Math.abs(this.x), Math.abs(this.y), Math.abs(this.z));
	}

	@Override
	public void add(int x, int y, int z) {
		this.x += x;
		this.y += y;
		this.z += z;
	}

	@Override
	public void add(double x, double y, double z) {
		this.x = (int) Math.round(this.x + x);
		this.y = (int) Math.round(this.y + y);
		this.z = (int) Math.round(this.z + z);
	}

	@Override
	public void addX(int x) {
		this.x += x;
	}

	@Override
	public void addX(double x) {
		this.x = (int) Math.round(this.x + x);
	}

	@Override
	public void addY(int y) {
		this.y += y;
	}

	@Override
	public void addY(double y) {
		this.y = (int) Math.round(this.y + y);
	}

	@Override
	public void addZ(int z) {
		this.z += z;
	}

	@Override
	public void addZ(double z) {
		this.z = (int) Math.round(this.z + z);
	}

	@Override
	public void negate(Tuple3D<?> tuple) {
		assert tuple != null : "Input tuple must not be null";
		this.x = -tuple.ix();
		this.y = -tuple.iy();
		this.z = -tuple.iz();
	}

	@Override
	public void negate() {
		this.x = -this.x;
		this.y = -this.y;
		this.z = -this.z;
	}

	@Override
	public void scale(int scale, Tuple3D<?> tuple) {
		assert tuple != null : "Input tuple must not be null";
		this.x = (int) Math.round(scale * tuple.getX());
		this.y = (int) Math.round(scale * tuple.getY());
		this.z = (int) Math.round(scale * tuple.getZ());
	}

	@Override
	public void scale(double scale, Tuple3D<?> tuple) {
		assert tuple != null : "Input tuple must not be null";
		this.x = (int) Math.round(scale * tuple.getX());
		this.y = (int) Math.round(scale * tuple.getY());
		this.z = (int) Math.round(scale * tuple.getZ());
	}

	@Override
	public void scale(int scale) {
		this.x *= scale;
		this.y *= scale;
		this.z *= scale;
	}

	@Override
	public void scale(double scale) {
		this.x = (int) Math.round(scale * this.x);
		this.y = (int) Math.round(scale * this.y);
		this.z = (int) Math.round(scale * this.z);
	}

	@Override
	public void set(Tuple3D<?> tuple) {
		assert tuple != null : "Input tuple must not be null";
		this.x = tuple.ix();
		this.y = tuple.iy();
		this.z = tuple.iz();
	}

	@Override
	public void set(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	@Override
	public void set(double x, double y, double z) {
		this.x = (int) Math.round(x);
		this.y = (int) Math.round(y);
		this.z = (int) Math.round(z);
	}

	@Override
	public void set(int[] tuple) {
		assert tuple != null : "Input tuple must not be null";
		assert tuple.length >= 3 : "Size of the input tuple is too small";
		this.x = tuple[0];
		this.y = tuple[1];
		this.z = tuple[2];
	}

	@Override
	public void set(double[] tuple) {
		assert tuple != null : "Input tuple must not be null";
		assert tuple.length >= 3 : "Size of the input tuple is too small";
		this.x = (int) Math.round(tuple[0]);
		this.y = (int) Math.round(tuple[1]);
		this.z = (int) Math.round(tuple[2]);
	}

	@Pure
	@Override
	public double getX() {
		return this.x;
	}

	@Pure
	@Override
	public int ix() {
		return this.x;
	}

	@Override
	public void setX(int x) {
		this.x = x;
	}

	@Override
	public void setX(double x) {
		this.x = (int) Math.round(x);
	}

	@Pure
	@Override
	public double getY() {
		return this.y;
	}

	@Pure
	@Override
	public int iy() {
		return this.y;
	}

	@Override
	public void setY(int y) {
		this.y = y;
	}

	@Override
	public void setY(double y) {
		this.y = (int) Math.round(y);
	}

	@Pure
	@Override
	public double getZ() {
		return this.z;
	}

	@Pure
	@Override
	public int iz() {
		return this.z;
	}

	@Override
	public void setZ(int z) {
		this.z = z;
	}

	@Override
	public void setZ(double z) {
		this.z = (int) Math.round(z);
	}

	@Override
	public void sub(int x, int y, int z) {
		this.x -= x;
		this.y -= y;
		this.z -= z;
	}

    @Override
    public void sub(double x, double y, double z) {
        this.x = (int) Math.round(this.x - x);
        this.y = (int) Math.round(this.y - y);
        this.z = (int) Math.round(this.z - z);
    }

	@Override
	public void subX(int x) {
		this.x -= x;
	}

    @Override
    public void subX(double x) {
        this.x = (int) Math.round(this.x - x);
    }

	@Override
	public void subY(int y) {
		this.y -= y;
	}

    @Override
    public void subY(double y) {
        this.y = (int) Math.round(this.y - y);
    }

	@Override
	public void subZ(int z) {
		this.z -= z;
	}

	@Override
	public void subZ(double z) {
		this.z = (int) Math.round(this.z - z);
	}

	@SuppressWarnings("unchecked")
	@Pure
	@Override
	public boolean equals(Object object) {
		try {
			return equals((RT) object);
        } catch (AssertionError e) {
			throw e;
		} catch (Throwable e2) {
			return false;
		}
	}

	@Pure
	@Override
	public int hashCode() {
		int bits = 1;
		bits = 31 * bits + this.x;
		bits = 31 * bits + this.y;
		bits = 31 * bits + this.z;
		return bits ^ (bits >> 31);
	}

	@Pure
	@Override
	public String toString() {
        return Tuple3D.toString(this.x, this.y, this.z);
	}

}
