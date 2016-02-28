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
package org.arakhne.afc.math.geometry.d2.continuous;

import org.arakhne.afc.math.geometry.d2.FunctionalTuple2D;
import org.arakhne.afc.math.geometry.d2.Tuple2D;
import org.eclipse.xtext.xbase.lib.Pure;

/** 2D tuple with 2 floating-point numbers.
 * 
 * @param <T> is the implementation type of the tuple.
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class Tuple2f<T extends Tuple2D<? super T>> implements FunctionalTuple2D<T> {

	private static final long serialVersionUID = 6447733811545555778L;
	
	/** x coordinate.
	 */
	protected double x;

	/** y coordinate.
	 */
	protected double y;

	/**
	 */
	public Tuple2f() {
		this.x = this.y = 0;
	}

	/**
	 * @param tuple is the tuple to copy.
	 */
	public Tuple2f(Tuple2D<?> tuple) {
		this.x = tuple.getX();
		this.y = tuple.getY();
	}

	/**
	 * @param tuple is the tuple to copy.
	 */
	public Tuple2f(int[] tuple) {
		this.x = tuple[0];
		this.y = tuple[1];
	}

	/**
	 * @param tuple is the tuple to copy.
	 */
	public Tuple2f(double[] tuple) {
		this.x = tuple[0];
		this.y = tuple[1];
	}

	/**
	 * @param x
	 * @param y
	 */
	@SuppressWarnings("hiding")
	public Tuple2f(int x, int y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * @param x
	 * @param y
	 */
	@SuppressWarnings("hiding")
	public Tuple2f(double x, double y) {
		this.x = x;
		this.y = y;
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
	@Pure
	@Override
	public double getX() {
		return this.x;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setX(double x1) {
		this.x = x1;
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
	@Override
	public void setY(double y1) {
		this.y = y1;
	}

	/**
	 * {@inheritDoc}
	 */
	@Pure
	@Override
	public int hashCode() {
		long bits = 1;
		bits = 31 * bits + Double.doubleToLongBits(this.x);
		bits = 31 * bits + Double.doubleToLongBits(this.y);
		int b = (int) bits;
		return b ^ (b >> 32);
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