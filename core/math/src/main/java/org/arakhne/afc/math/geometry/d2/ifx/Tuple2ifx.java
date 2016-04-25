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
package org.arakhne.afc.math.geometry.d2.ifx;

import org.arakhne.afc.math.geometry.d2.Tuple2D;
import org.eclipse.xtext.xbase.lib.Pure;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

/** 2D tuple with 2 integer FX properties.
 * 
 * @param <T> is the abstract type of the tuple.
 * @param <IT> is the implementation type of the tuple.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public class Tuple2ifx<T extends Tuple2D<? super T>, IT extends T> implements Tuple2D<T> {

	private static final long serialVersionUID = 3136314939750740492L;

	/** x coordinate.
	 */
	IntegerProperty x;

	/** y coordinate.
	 */
	IntegerProperty y;

	/**
	 */
	public Tuple2ifx() {
		this(0, 0);
	}
	
	/**
	 * @param xProperty
	 * @param yProperty
	 */
	public Tuple2ifx(IntegerProperty xProperty, IntegerProperty yProperty) {
		this.x = xProperty;
		this.y = yProperty;
	}

	/**
	 * @param tuple is the tuple to copy.
	 */
	public Tuple2ifx(Tuple2D<?> tuple) {
		this(tuple.ix(), tuple.iy());
	}

	/**
	 * @param tuple is the tuple to copy.
	 */
	public Tuple2ifx(int[] tuple) {
		this(tuple[0], tuple[1]);
	}

	/**
	 * @param tuple is the tuple to copy.
	 */
	public Tuple2ifx(double[] tuple) {
		this((int) Math.round(tuple[0]), (int) Math.round(tuple[1]));
	}

	/**
	 * @param x
	 * @param y
	 */
	public Tuple2ifx(double x, double y) {
		this((int) Math.round(x), (int) Math.round(y));
	}

	/**
	 * @param x
	 * @param y
	 */
	public Tuple2ifx(int x, int y) {
		this(new SimpleIntegerProperty(x), new SimpleIntegerProperty(y));
	}

	@SuppressWarnings("unchecked")
	@Pure
	@Override
	public IT clone() {
		try {
			IT clone = (IT) super.clone();
			Tuple2ifx<?, ?> tifx = (Tuple2ifx<?, ?>) clone;
			if (this.x != null) {
				tifx.x = null;
				tifx.xProperty().set(ix());
			}
			if (this.y != null) {
				tifx.y = null;
				tifx.yProperty().set(iy());
			}
			return clone;
		}
		catch(CloneNotSupportedException e) {
			throw new InternalError(e);
		}
	}

	@SuppressWarnings("unchecked")
	@Pure
	@Override
	public boolean equals(Object object) {
		try {
			return equals((T) object);
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
		bits = 31 * bits + ix();
		bits = 31 * bits + iy();
		return bits ^ (bits >> 32);
	}
	
	@Pure
	@Override
	public String toString() {
		return "(" //$NON-NLS-1$
				+ix()
				+";" //$NON-NLS-1$
				+iy()
				+")"; //$NON-NLS-1$
	}

	/** Replies the x property.
	 *
	 * @return the x property.
	 */
	@Pure
	public IntegerProperty xProperty() {
		if (this.x == null) {
			this.x = new SimpleIntegerProperty(this, "x"); //$NON-NLS-1$
		}
		return this.x;
	}

	/** Replies the y property.
	 *
	 * @return the y property.
	 */
	@Pure
	public IntegerProperty yProperty() {
		if (this.y == null) {
			this.y = new SimpleIntegerProperty(this, "y"); //$NON-NLS-1$
		}
		return this.y;
	}

	@Override
	public double getX() {
		return this.x == null ? 0 : this.x.get();
	}

	@Override
	public int ix() {
		return this.x == null ? 0 : this.x.get();
	}

	@Override
	public void setX(int x) {
		xProperty().set(x);
	}

	@Override
	public void setX(double x) {
		xProperty().set((int) Math.round(x));
	}

	@Override
	public double getY() {
		return this.y == null ? 0 : this.y.get();
	}

	@Override
	public int iy() {
		return this.y == null ? 0 : this.y.get();
	}

	@Override
	public void setY(int y) {
		yProperty().set(y);
	}

	@Override
	public void setY(double y) {
		yProperty().set((int) Math.round(y));
	}

}