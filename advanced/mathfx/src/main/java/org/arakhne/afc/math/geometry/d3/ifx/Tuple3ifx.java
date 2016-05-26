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
package org.arakhne.afc.math.geometry.d3.ifx;

import org.arakhne.afc.math.geometry.d3.Tuple3D;
import org.eclipse.xtext.xbase.lib.Pure;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

/** 3D tuple with 3 integer FX properties.
 * 
 * @param <RT> is the type of return tuples by the tuple.
 * @author $Author: sgalland$
 * @author $Author: tpiotrow$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public class Tuple3ifx<RT extends Tuple3ifx<? super RT>> implements Tuple3D<RT> {

	private static final long serialVersionUID = 3136314939750740492L;

	/** x coordinate.
	 */
	IntegerProperty x;
	
	/** y coordinate.
	 */
	IntegerProperty y;

	/** z coordinate.
	 */
	IntegerProperty z;

	/**
	 */
	public Tuple3ifx() {
		this(0, 0, 0);
	}
	
	/**
	 * @param xProperty
	 * @param yProperty
	 * @param zProperty
	 */
	public Tuple3ifx(IntegerProperty xProperty, IntegerProperty yProperty, IntegerProperty zProperty) {
		this.x = xProperty;
		this.y = yProperty;
		this.z = zProperty;
	}

	/**
	 * @param tuple is the tuple to copy.
	 */
	public Tuple3ifx(Tuple3D<?> tuple) {
		this(tuple.ix(), tuple.iy(), tuple.iz());
	}

	/**
	 * @param tuple is the tuple to copy.
	 */
	public Tuple3ifx(int[] tuple) {
		this(tuple[0], tuple[1], tuple[2]);
	}

	/**
	 * @param tuple is the tuple to copy.
	 */
	public Tuple3ifx(double[] tuple) {
		this((int) Math.round(tuple[0]), (int) Math.round(tuple[1]), (int) Math.round(tuple[2]));
	}

	/**
	 * @param x
	 * @param y
	 */
	public Tuple3ifx(double x, double y, double z) {
		this((int) Math.round(x), (int) Math.round(y), (int) Math.round(z));
	}

	/**
	 * @param x
	 * @param y
	 * @param z
	 */
	public Tuple3ifx(int x, int y, int z) {
		this(new SimpleIntegerProperty(x), new SimpleIntegerProperty(y), new SimpleIntegerProperty(z));
	}

	@SuppressWarnings("unchecked")
	@Pure
	@Override
	public RT clone() {
		try {
			RT clone = (RT) super.clone();
			if (this.x != null) {
				clone.x = null;
				clone.xProperty().set(ix());
			}
			if (this.y != null) {
				clone.y = null;
				clone.yProperty().set(iy());
			}
			if (this.z != null) {
				clone.z = null;
				clone.zProperty().set(iz());
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
		bits = 31 * bits + ix();
		bits = 31 * bits + iy();
		bits = 31 * bits + iz();
		return bits ^ (bits >> 32);
	}
	
	@Pure
	@Override
	public String toString() {
		return "(" //$NON-NLS-1$
				+ix()
				+";" //$NON-NLS-1$
				+iy()
				+";" //$NON-NLS-1$
				+iz()
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

	/** Replies the z property.
	 *
	 * @return the z property.
	 */
	@Pure
	public IntegerProperty zProperty() {
		if (this.z == null) {
			this.z = new SimpleIntegerProperty(this, "z"); //$NON-NLS-1$
		}
		return this.z;
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

	@Override
	public double getZ() {
		return this.z == null ? 0 : this.z.get();
	}

	@Override
	public int iz() {
		return this.z == null ? 0 : this.z.get();
	}

	@Override
	public void setZ(int z) {
		zProperty().set(z);
	}

	@Override
	public void setZ(double z) {
		zProperty().set((int) Math.round(z));
	}

}