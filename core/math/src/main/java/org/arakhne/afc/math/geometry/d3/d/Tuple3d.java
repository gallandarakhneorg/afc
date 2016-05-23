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
package org.arakhne.afc.math.geometry.d3.d;

import org.arakhne.afc.math.geometry.d3.Tuple3D;
import org.eclipse.xtext.xbase.lib.Pure;

/** 3D tuple with 3 double precision floating-point numbers.
 * 
 * @param <RT> is the type of the data returned by the tuple.
 * @author $Author: sgalland$
 * @author $Author: tpiotrow$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public class Tuple3d<RT extends Tuple3d<? super RT>> implements Tuple3D<RT> {

	private static final long serialVersionUID = -1204612842037872952L;

	/** x coordinate.
	 */
	double x;
	
	/** y coordinate.
	 */
	double y;

	/** z coordinate.
	 */
	double z;

	/**
	 */
	public Tuple3d() {
		this.x = 0;
		this.y = 0;
		this.z = 0;
	}

	/**
	 * @param tuple is the tuple to copy.
	 */
	public Tuple3d(Tuple3D<?> tuple) {
		assert (tuple != null) : "Tuple must be not null"; //$NON-NLS-1$
		this.x = tuple.getX();
		this.y = tuple.getY();
		this.z = tuple.getZ();
	}

	/**
	 * @param tuple is the tuple to copy.
	 */
	public Tuple3d(int[] tuple) {
		assert (tuple != null) : "Tuple must be not null"; //$NON-NLS-1$
		assert (tuple.length >= 3) : "Tuple size is too small"; //$NON-NLS-1$
		this.x = tuple[0];
		this.y = tuple[1];
		this.z = tuple[2];
	}

	/**
	 * @param tuple is the tuple to copy.
	 */
	public Tuple3d(double[] tuple) {
		assert (tuple != null) : "Tuple must be not null"; //$NON-NLS-1$
		assert (tuple.length >= 2) : "Tuple size is too small"; //$NON-NLS-1$
		this.x = tuple[0];
		this.y = tuple[1];
	}

	/**
	 * @param x
	 * @param y
	 * @param z
	 */
	public Tuple3d(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	/**
	 * @param x
	 * @param y
	 * @param z
	 */
	public Tuple3d(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	@SuppressWarnings("unchecked")
	@Pure
	@Override
	public RT clone() {
		try {
			return (RT) super.clone();
		}
		catch(CloneNotSupportedException e) {
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
		assert (tuple != null) : "Tuple must be not null"; //$NON-NLS-1$
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
		this.x += x;
		this.y += y;
		this.z += z;
	}

	@Override
	public void addX(int x) {
		this.x += x;
	}

	@Override
	public void addX(double x) {
		this.x += x;
	}
	
	@Override
	public void addY(int y) {
		this.y += y;
	}
	
	@Override
	public void addY(double y) {
		this.y += y;
	}

	@Override
	public void addZ(int z) {
		this.z += z;
	}

	@Override
	public void addZ(double z) {
		this.z += z;
	}

	@Override
	public void negate(Tuple3D<?> tuple) {
		assert (tuple != null) : "Tuple must be not null"; //$NON-NLS-1$
		this.x = -tuple.getX();
		this.y = -tuple.getY();
		this.z = -tuple.getZ();
	}

	@Override
	public void negate() {
		this.x = -this.x;
		this.y = -this.y;
		this.z = -this.z;
	}

	@Override
	public void scale(int s, Tuple3D<?> tuple) {
		assert (tuple != null) : "Tuple must be not null"; //$NON-NLS-1$
		this.x = s * tuple.getX();
		this.y = s * tuple.getY();
		this.z = s * tuple.getZ();
	}

	@Override
	public void scale(double s, Tuple3D<?> tuple) {
		assert (tuple != null) : "Tuple must be not null"; //$NON-NLS-1$
		this.x = s * tuple.getX();
		this.y = s * tuple.getY();
		this.z = s * tuple.getZ();
	}

	@Override
	public void scale(int s) {
		this.x = s * this.x;
		this.y = s * this.y;
		this.z = s * this.z;
	}

	@Override
	public void scale(double s) {
		this.x = s * this.x;
		this.y = s * this.y;
		this.z = s * this.z;
	}

	@Override
	public void set(Tuple3D<?> tuple) {
		assert (tuple != null) : "Tuple must be not null"; //$NON-NLS-1$
		this.x = tuple.getX();
		this.y = tuple.getY();
		this.z = tuple.getZ();
	}

	@Override
	public void set(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	@Override
	public void set(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	@Override
	public void set(int[] tuple) {
		assert (tuple != null) : "Tuple must be not null"; //$NON-NLS-1$
		assert (tuple.length >= 3) : "Tuple size is too small"; //$NON-NLS-1$
		this.x = tuple[0];
		this.y = tuple[1];
		this.z = tuple[2];
	}

	@Override
	public void set(double[] tuple) {
		assert (tuple != null) : "Tuple must be not null"; //$NON-NLS-1$
		assert (tuple.length >= 3) : "Tuple size is too small"; //$NON-NLS-1$
		this.x = tuple[0];
		this.y = tuple[1];
		this.z = tuple[2];
	}

	@Pure
	@Override
	public double getX() {
		return this.x;
	}

	@Pure
	@Override
	public int ix() {
		return (int) this.x;
	}

	@Override
	public void setX(int x) {
		this.x = x;
	}

	@Override
	public void setX(double x) {
		this.x = x;
	}
	
	@Pure
	@Override
	public double getY() {
		return this.y;
	}
	
	@Pure
	@Override
	public int iy() {
		return (int) this.y;
	}
	
	@Override
	public void setY(int y) {
		this.y = y;
	}
	
	@Override
	public void setY(double y) {
		this.y = y;
	}

	@Pure
	@Override
	public double getZ() {
		return this.z;
	}

	@Pure
	@Override
	public int iz() {
		return (int) this.z;
	}

	@Override
	public void setZ(int z) {
		this.z = z;
	}

	@Override
	public void setZ(double z) {
		this.z = z;
	}

	@Override
	public void sub(int x, int y, int z) {
		this.x -= x;
		this.y -= y;
		this.z -= z;
	}

	@Override
	public void subX(int x) {
		this.x -= x;
	}
	
	@Override
	public void subY(int y) {
		this.y -= y;
	}

	@Override
	public void subZ(int z) {
		this.z -= z;
	}

	@Override
	public void sub(double x, double y, double z) {
		this.x -= x;
		this.y -= y;
		this.z -= z;
	}

	@Override
	public void subX(double x) {
		this.x -= x;
	}
	
	@Override
	public void subY(double y) {
		this.y -= y;
	}

	@Override
	public void subZ(double z) {
		this.z -= z;
	}

	@Pure
	@Override
	public boolean equals(Object t1) {
		try {
			return equals((Tuple3D<?>) t1);
		}
		catch(AssertionError e) {
			throw e;
		}
		catch (Throwable e2) {
			return false;
		}
	}

	@Pure
	@Override
	public int hashCode() {
		long bits = 1;
		bits = 31 * bits + Double.doubleToLongBits(this.x);
		bits = 31 * bits + Double.doubleToLongBits(this.y);
		bits = 31 * bits + Double.doubleToLongBits(this.z);
		int b = (int) bits;
		return b ^ (b >> 32);
	}
	
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