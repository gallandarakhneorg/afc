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
package org.arakhne.afc.math.geometry.d2.fpfx;

import org.arakhne.afc.math.geometry.d2.Tuple2D;
import org.eclipse.xtext.xbase.lib.Pure;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

/** 2D tuple with 2 double precision floating-point FX properties.
 * 
 * @param <T> is the abstract type of the tuple.
 * @param <IT> is the implementation type of the tuple.
 * @author $Author: sgalland$
 * @author $Author: olamotte$
 * @author $Author: hjaffali$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public class Tuple2fx<T extends Tuple2D<? super T>, IT extends T> implements Tuple2D<T> {

	private static final long serialVersionUID = 2510506877090400718L;

	/** x coordinate.
	 */
	DoubleProperty x;

	/** y coordinate.
	 */
	DoubleProperty y;

	/**
	 */
	public Tuple2fx() {
		this(0., 0.);
	}

	/**
	 * @param x
	 * @param y
	 */
	public Tuple2fx(DoubleProperty x, DoubleProperty y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * @param tuple is the tuple to copy.
	 */
	public Tuple2fx(Tuple2D<?> tuple) {
		this.x = new SimpleDoubleProperty(tuple.getX());
		this.y = new SimpleDoubleProperty(tuple.getY());
	}

	/**
	 * @param tuple is the tuple to copy.
	 */
	public Tuple2fx(int[] tuple) {
		this(tuple[0], tuple[1]);
	}

	/**
	 * @param tuple is the tuple to copy.
	 */
	public Tuple2fx(double[] tuple) {
		this(tuple[0], tuple[1]);
	}

	/**
	 * @param x
	 * @param y
	 */
	public Tuple2fx(int x, int y) {
		this(new SimpleDoubleProperty(x), new SimpleDoubleProperty(y));
	}

	/**
	 * @param x
	 * @param y
	 */
	public Tuple2fx(double x, double y) {
		this(new SimpleDoubleProperty(x), new SimpleDoubleProperty(y));
	}

	@SuppressWarnings("unchecked")
	@Pure
	@Override
	public IT clone() {
		try {
			Tuple2fx<?, ?> clone = (Tuple2fx<?, ?>) super.clone();
			clone.x = new SimpleDoubleProperty(this.x.doubleValue());
			clone.y = new SimpleDoubleProperty(this.y.doubleValue());
			return (IT) clone;
		}
		catch(CloneNotSupportedException e) {
			throw new InternalError(e);
		}
	}

	@Pure
	@Override
	public boolean equals(Object t1) {
		try {
			return equals((Tuple2D<?>) t1);
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
		bits = 31 * bits + Double.doubleToLongBits(this.x.get());
		bits = 31 * bits + Double.doubleToLongBits(this.y.get());
		int b = (int) bits;
		return b ^ (b >> 32);
	}
	
	@Pure
	@Override
	public String toString() {
		return "(" //$NON-NLS-1$
				+this.x.get()
				+";" //$NON-NLS-1$
				+this.y.get()
				+")"; //$NON-NLS-1$
	}

	/** Replies the x property.
	 *
	 * @return the x property.
	 */
	@Pure
	public DoubleProperty xProperty() {
		return this.x;
	}

	/** Replies the y property.
	 *
	 * @return the y property.
	 */
	@Pure
	public DoubleProperty yProperty() {
		return this.y;
	}

	@Override
	public double getX() {
		return this.x.doubleValue();
	}

	@Override
	public int ix() {
		return this.x.intValue();
	}

	@Override
	public void setX(int x) {
		this.x.set(x);
	}

	@Override
	public void setX(double x) {
		this.x.set(x);
	}

	@Override
	public double getY() {
		return this.y.doubleValue();
	}

	@Override
	public int iy() {
		return this.y.intValue();
	}

	@Override
	public void setY(int y) {
		this.y.set(y);
	}

	@Override
	public void setY(double y) {
		this.y.set(y);
	}

}