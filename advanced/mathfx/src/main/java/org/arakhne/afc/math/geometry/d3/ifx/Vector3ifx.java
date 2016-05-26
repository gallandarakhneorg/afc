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

import org.arakhne.afc.math.geometry.d3.GeomFactory3D;
import org.arakhne.afc.math.geometry.d3.Tuple3D;
import org.arakhne.afc.math.geometry.d3.UnmodifiableVector3D;
import org.arakhne.afc.math.geometry.d3.Vector3D;
import org.eclipse.xtext.xbase.lib.Pure;

import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ReadOnlyDoubleWrapper;

/** 3D Vector with 3 integer FX properties.
 * 
 * @author $Author: sgalland$
 * @author $Author: tpiotrow$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public class Vector3ifx extends Tuple3ifx<Vector3ifx> implements Vector3D<Vector3ifx, Point3ifx> {

	private static final long serialVersionUID = 5782200591782721145L;

	/** Property that contains the squared length.
	 */
	private ReadOnlyDoubleWrapper lengthSquareProperty;
	
	/** Property that contains the length.
	 */
	private ReadOnlyDoubleWrapper lengthProperty;

	/**
	 */
	public Vector3ifx() {
		//
	}

	/**
	 * @param xProperty
	 * @param yProperty
	 * @param zProperty
	 */
	public Vector3ifx(IntegerProperty xProperty, IntegerProperty yProperty, IntegerProperty zProperty) {
		super(xProperty, yProperty, zProperty);
	}

	/**
	 * @param tuple is the tuple to copy.
	 */
	public Vector3ifx(Tuple3D<?> tuple) {
		super(tuple);
	}

	/**
	 * @param tuple is the tuple to copy.
	 */
	public Vector3ifx(int[] tuple) {
		super(tuple);
	}

	/**
	 * @param tuple is the tuple to copy.
	 */
	public Vector3ifx(double[] tuple) {
		super(tuple);
	}

	/**
	 * @param x
	 * @param y
	 * @param z
	 */
	public Vector3ifx(int x, int y, int z) {
		super(x,y,z);
	}

	/**
	 * @param x
	 * @param y
	 */
	public Vector3ifx(float x, float y, float z) {
		super(x,y,z);
	}

	/**
	 * @param x
	 * @param y
	 * @param z
	 */
	public Vector3ifx(double x, double y, double z) {
		super(x,y,z);
	}

	/**
	 * @param x
	 * @param y
	 * @param z
	 */
	public Vector3ifx(long x, long y, long z) {
		super(x,y,z);
	}
	
	@Override
	public Vector3ifx clone() {
		Vector3ifx clone = super.clone();
		clone.lengthSquareProperty = null;
		clone.lengthProperty = null;
		return clone;
	}

	@Override
	public Vector3ifx toUnitVector() {
		double length = getLength();
		if (length == 0.) {
			return getGeomFactory().newVector();
		}
		int x = (int) Math.round(ix() / length);
		int y = (int) Math.round(iy() / length);
		int z = (int) Math.round(iz() / length);
		return getGeomFactory().newVector(x, y, z);
	}
	
	@Override
	public double getLength() {
		return lengthProperty().get();
	}
	
	/** Replies the property that represents the length of the vector.
	 *
	 * @return the length property
	 */
	public DoubleProperty lengthProperty() {
		if (this.lengthProperty == null) {
			this.lengthProperty = new ReadOnlyDoubleWrapper(this, "length"); //$NON-NLS-1$
			this.lengthProperty.bind(Bindings.createDoubleBinding(
					() -> {
						return Math.sqrt(lengthSquaredProperty().doubleValue());
					}, lengthSquaredProperty()));
		}
		return this.lengthProperty;
	}

	@Override
	public double getLengthSquared() {
		return lengthSquaredProperty().get();
	}
	
	/** Replies the property that represents the length of the vector.
	 *
	 * @return the length property
	 */
	public DoubleProperty lengthSquaredProperty() {
		if (this.lengthSquareProperty == null) {
			this.lengthSquareProperty = new ReadOnlyDoubleWrapper(this, "lengthSquared"); //$NON-NLS-1$
			this.lengthSquareProperty.bind(Bindings.createDoubleBinding(
					() -> {
						return Vector3ifx.this.x.doubleValue() * Vector3ifx.this.x.doubleValue()
								+ Vector3ifx.this.y.doubleValue() * Vector3ifx.this.y.doubleValue()
								+ Vector3ifx.this.z.doubleValue() * Vector3ifx.this.z.doubleValue();
					}, this.x, this.y, this.z));
		}
		return this.lengthSquareProperty;
	}

	@Override
	public GeomFactory3ifx getGeomFactory() {
		return GeomFactory3ifx.SINGLETON;
	}
	
	@Pure
	@Override
	public UnmodifiableVector3D<Vector3ifx, Point3ifx> toUnmodifiable() {
		return new UnmodifiableVector3D<Vector3ifx, Point3ifx>() {

			private static final long serialVersionUID = -3525974627723161583L;

			@Override
			public GeomFactory3D<Vector3ifx, Point3ifx> getGeomFactory() {
				return Vector3ifx.this.getGeomFactory();
			}
			
			@Override
			public Vector3ifx toUnitVector() {
				return Vector3ifx.this.toUnitVector();
			}
			
			@Override
			public Vector3ifx clone() {
				return Vector3ifx.this.getGeomFactory().newVector(
						Vector3ifx.this.ix(),
						Vector3ifx.this.iy(),
						Vector3ifx.this.iz());
			}

			@Override
			public double getX() {
				return Vector3ifx.this.getX();
			}

			@Override
			public int ix() {
				return Vector3ifx.this.ix();
			}
			
			@Override
			public double getY() {
				return Vector3ifx.this.getY();
			}
			
			@Override
			public int iy() {
				return Vector3ifx.this.iy();
			}

			@Override
			public double getZ() {
				return Vector3ifx.this.getZ();
			}

			@Override
			public int iz() {
				return Vector3ifx.this.iz();
			}

		};
	}

}
