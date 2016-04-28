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
 * @param <RT> is the type of the data returned by the tuple.
 * @author $Author: sgalland$
 * @author $Author: olamotte$
 * @author $Author: hjaffali$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public class Tuple2fx<RT extends Tuple2fx<? super RT>> implements Tuple2D<RT> {

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
		//
	}

	/**
	 * @param x
	 * @param y
	 */
	public Tuple2fx(DoubleProperty x, DoubleProperty y) {
		set(x, y);
	}

	/**
	 * @param tuple is the tuple to copy.
	 */
	public Tuple2fx(Tuple2D<?> tuple) {
		this(tuple.getX(), tuple.getY());
	}

	/**
	 * @param tuple is the tuple to copy.
	 */
	public Tuple2fx(int[] tuple) {
		this((double) tuple[0], (double) tuple[1]);
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
		this((double) x, (double) y);
	}

	/**
	 * @param x
	 * @param y
	 */
	public Tuple2fx(double x, double y) {
		xProperty().set(x);
		yProperty().set(y);
	}
	
	/** Change the x and y properties.
	 *
	 * @param x the new x property.
	 * @param y the new y property.
	 */
	void set(DoubleProperty x, DoubleProperty y) {
		this.x = x;
		this.y = y;
	}

	@SuppressWarnings("unchecked")
	@Pure
	@Override
	public RT clone() {
		try {
			RT clone = (RT) super.clone();
			if (clone.x != null) {
				clone.x = null;
				clone.xProperty().set(getX());
			}
			if (clone.y != null) {
				clone.y = null;
				clone.yProperty().set(getY());
			}
			return clone;
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
		bits = 31 * bits + Double.doubleToLongBits(getX());
		bits = 31 * bits + Double.doubleToLongBits(getY());
		int b = (int) bits;
		return b ^ (b >> 32);
	}
	
	@Pure
	@Override
	public String toString() {
		return "(" //$NON-NLS-1$
				+ getX()
				+ ";" //$NON-NLS-1$
				+ getY()
				+ ")"; //$NON-NLS-1$
	}

	/** Replies the x property.
	 *
	 * @return the x property.
	 */
	@Pure
	public DoubleProperty xProperty() {
		if (this.x == null) {
			this.x = new SimpleDoubleProperty(this, "x"); //$NON-NLS-1$;
		}
		return this.x;
	}

	/** Replies the y property.
	 *
	 * @return the y property.
	 */
	@Pure
	public DoubleProperty yProperty() {
		if (this.y == null) {
			this.y = new SimpleDoubleProperty(this, "y"); //$NON-NLS-1$
		}
		return this.y;
	}

	@Override
	public double getX() {
		return this.x == null ? 0 : this.x.doubleValue();
	}

	@Override
	public int ix() {
		return this.x == null ? 0 : this.x.intValue();
	}

	@Override
	public void setX(int x) {
		xProperty().set(x);
	}

	@Override
	public void setX(double x) {
		xProperty().set(x);
	}

	@Override
	public double getY() {
		return this.y == null ? 0 : this.y.doubleValue();
	}

	@Override
	public int iy() {
		return this.y == null ? 0 : this.y.intValue();
	}

	@Override
	public void setY(int y) {
		yProperty().set(y);
	}

	@Override
	public void setY(double y) {
		yProperty().set(y);
	}

}