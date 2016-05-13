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
package org.arakhne.afc.math.geometry.d2.i;

import org.arakhne.afc.math.geometry.d2.Tuple2D;
import org.eclipse.xtext.xbase.lib.Pure;

/** 2D tuple with 2 integer numbers.
 * 
 * @param <RT> is the replied type by the tuple.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public class Tuple2i<RT extends Tuple2i<? super RT>> implements Tuple2D<RT> {

	private static final long serialVersionUID = 3136314939750740492L;

	/** x coordinate.
	 */
	int x;

	/** y coordinate.
	 */
	int y;

	/**
	 */
	public Tuple2i() {
		this.x = 0;
		this.y = 0;
	}

	/**
	 * @param tuple is the tuple to copy.
	 */
	public Tuple2i(Tuple2i<?> tuple) {
		assert (tuple != null) : "Input tuple must not be null"; //$NON-NLS-1$
		this.x = tuple.x;
		this.y = tuple.y;
	}

	/**
	 * @param tuple is the tuple to copy.
	 */
	public Tuple2i(Tuple2D<?> tuple) {
		assert (tuple != null) : "Input tuple must not be null"; //$NON-NLS-1$
		this.x = tuple.ix();
		this.y = tuple.iy();
	}

	/**
	 * @param tuple is the tuple to copy.
	 */
	public Tuple2i(int[] tuple) {
		assert (tuple != null) : "Input tuple must not be null"; //$NON-NLS-1$
		assert (tuple.length >= 2) : "Size of the input array is too small"; //$NON-NLS-1$
		this.x = tuple[0];
		this.y = tuple[1];
	}

	/**
	 * @param tuple is the tuple to copy.
	 */
	public Tuple2i(double[] tuple) {
		assert (tuple != null) : "Input tuple must not be null"; //$NON-NLS-1$
		assert (tuple.length >= 2) : "Size of the input array is too small"; //$NON-NLS-1$
		this.x = (int) Math.round(tuple[0]);
		this.y = (int) Math.round(tuple[1]);
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
		this.x = (int) Math.round(x);
		this.y = (int) Math.round(y);
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
	}

	@Override
	public void absolute(Tuple2D<?> tuple) {
		assert (tuple != null) : "Output tuple must not be null"; //$NON-NLS-1$
		tuple.set(Math.abs(this.x), Math.abs(this.y));
	}

	@Override
	public void add(int x, int y) {
		this.x += x;
		this.y += y;
	}

	@Override
	public void add(double x, double y) {
		this.x = (int) Math.round(this.x + x);
		this.y = (int) Math.round(this.y + y);
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
	public void negate(Tuple2D<?> tuple) {
		assert (tuple != null) : "Input tuple must not be null"; //$NON-NLS-1$
		this.x = -tuple.ix();
		this.y = -tuple.iy();
	}

	@Override
	public void negate() {
		this.x = -this.x;
		this.y = -this.y;
	}

	@Override
	public void scale(int scale, Tuple2D<?> tuple) {
		assert (tuple != null) : "Input tuple must not be null"; //$NON-NLS-1$
		this.x = (int) Math.round(scale * tuple.getX());
		this.y = (int) Math.round(scale * tuple.getY());
	}

	@Override
	public void scale(double scale, Tuple2D<?> tuple) {
		assert (tuple != null) : "Input tuple must not be null"; //$NON-NLS-1$
		this.x = (int) Math.round(scale * tuple.getX());
		this.y = (int) Math.round(scale * tuple.getY());
	}

	@Override
	public void scale(int scale) {
		this.x *= scale;
		this.y *= scale;
	}

	@Override
	public void scale(double scale) {
		this.x = (int) Math.round(scale * this.x);
		this.y = (int) Math.round(scale * this.y);
	}

	@Override
	public void set(Tuple2D<?> tuple) {
		assert (tuple != null) : "Input tuple must not be null"; //$NON-NLS-1$
		this.x = tuple.ix();
		this.y = tuple.iy();
	}

	@Override
	public void set(int x, int y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public void set(double x, double y) {
		this.x = (int) Math.round(x);
		this.y = (int) Math.round(y);
	}

	@Override
	public void set(int[] tuple) {
		assert (tuple != null) : "Input tuple must not be null"; //$NON-NLS-1$
		assert (tuple.length >= 2) : "Size of the input tuple is too small"; //$NON-NLS-1$
		this.x = tuple[0];
		this.y = tuple[1];
	}

	@Override
	public void set(double[] tuple) {
		assert (tuple != null) : "Input tuple must not be null"; //$NON-NLS-1$
		assert (tuple.length >= 2) : "Size of the input tuple is too small"; //$NON-NLS-1$
		this.x = (int) Math.round(tuple[0]);
		this.y = (int) Math.round(tuple[1]);
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

	@Override
	public void sub(int x, int y) {
		this.x -= x;
		this.y -= y;
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
	public void sub(double x, double y) {
		this.x = (int) Math.round(this.x - x);
		this.y = (int) Math.round(this.y - y);
	}

	@Override
	public void subX(double x) {
		this.x = (int) Math.round(this.x - x);
	}

	@Override
	public void subY(double y) {
		this.y = (int) Math.round(this.y - y);
	}

	@SuppressWarnings("unchecked")
	@Pure
	@Override
	public boolean equals(Object object) {
		try {
			return equals((RT) object);
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
		int bits = 1;
		bits = 31 * bits + this.x;
		bits = 31 * bits + this.y;
		return bits ^ (bits >> 32);
	}
	
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