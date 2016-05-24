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
package org.arakhne.afc.math.geometry.d3.dfx;

import org.arakhne.afc.math.geometry.d3.GeomFactory3D;
import org.arakhne.afc.math.geometry.d3.Tuple3D;
import org.arakhne.afc.math.geometry.d3.UnmodifiableVector3D;
import org.arakhne.afc.math.geometry.d3.Vector3D;

import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.ReadOnlyDoubleWrapper;

/** 3D Vector with 3 double precision floating-point FX properties.
 * 
 * @author $Author: sgalland$
 * @author $Author: olamotte$
 * @author $Author: hjaffali$
 * @author $Author: tpiotrow$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public class Vector3dfx extends Tuple3dfx<Vector3dfx> implements Vector3D<Vector3dfx, Point3dfx> {

	private static final long serialVersionUID = 8394433458442716159L;

	/** Property that contains the squared length.
	 */
	private ReadOnlyDoubleWrapper lengthSquareProperty;
	
	/** Property that contains the length.
	 */
	private ReadOnlyDoubleWrapper lengthProperty;

	/**
	 */
	public Vector3dfx() {
		//
	}

	/**
	 * @param x
	 * @param y
	 * @param z 
	 */
	public Vector3dfx(DoubleProperty x, DoubleProperty y, DoubleProperty z) {
		super(x, y, z);
	}

	/**
	 * @param tuple is the tuple to copy.
	 */
	public Vector3dfx(Tuple3D<?> tuple) {
		super(tuple);
	}

	/**
	 * @param tuple is the tuple to copy.
	 */
	public Vector3dfx(int[] tuple) {
		super(tuple);
	}

	/**
	 * @param tuple is the tuple to copy.
	 */
	public Vector3dfx(double[] tuple) {
		super(tuple);
	}

	/**
	 * @param x
	 * @param y
	 * @param z
	 */
	public Vector3dfx(int x, int y, int z) {
		super(x,y,z);
	}

	/**
	 * @param x
	 * @param y
	 * @param z
	 */
	public Vector3dfx(float x, float y, float z) {
		super(x,y,z);
	}

	/**
	 * @param x
	 * @param y
	 * @param z 
	 */
	public Vector3dfx(double x, double y, double z) {
		super(x,y,z);
	}

	/**
	 * @param x
	 * @param y
	 * @param z 
	 */
	public Vector3dfx(long x, long y, long z) {
		super(x,y,z);
	}
		
	@Override
	public Vector3dfx clone() {
		Vector3dfx clone = super.clone();
		clone.lengthSquareProperty = null;
		clone.lengthProperty = null;
		return clone;
	}
	
	@Override
	public Vector3dfx toUnitVector() {
		double length = getLength();
		if (length == 0.) {
			return getGeomFactory().newVector();
		}
		return getGeomFactory().newVector(getX() / length, getY() / length, getZ() / length);
	}
	
	@Override
	public double getLength() {
		return lengthProperty().get();
	}
	
	/** Replies the property that represents the length of the vector.
	 *
	 * @return the length property
	 */
	public ReadOnlyDoubleProperty lengthProperty() {
		if (this.lengthProperty == null) {
			this.lengthProperty = new ReadOnlyDoubleWrapper(this, "length"); //$NON-NLS-1$
			this.lengthProperty.bind(Bindings.createDoubleBinding(
					() -> {
						return Math.sqrt(lengthSquaredProperty().doubleValue());
					}, lengthSquaredProperty()));
		}
		return this.lengthProperty.getReadOnlyProperty();
	}

	@Override
	public double getLengthSquared() {
		return lengthSquaredProperty().get();
	}
	
	/** Replies the property that represents the length of the vector.
	 *
	 * @return the length property
	 */
	public ReadOnlyDoubleProperty lengthSquaredProperty() {
		if (this.lengthSquareProperty == null) {
			this.lengthSquareProperty = new ReadOnlyDoubleWrapper(this, "lengthSquared"); //$NON-NLS-1$
			this.lengthSquareProperty.bind(Bindings.createDoubleBinding(
					() -> {
						return Vector3dfx.this.x.doubleValue() * Vector3dfx.this.x.doubleValue()
							 + Vector3dfx.this.y.doubleValue() * Vector3dfx.this.y.doubleValue()
							 + Vector3dfx.this.z.doubleValue() * Vector3dfx.this.z.doubleValue();
					}, this.x, this.y, this.z));
		}
		return this.lengthSquareProperty.getReadOnlyProperty();
	}
	
	@Override
	public GeomFactory3dfx getGeomFactory() {
		return GeomFactory3dfx.SINGLETON;
	}

	@Override
	public UnmodifiableVector3D<Vector3dfx, Point3dfx> toUnmodifiable() {
		return new UnmodifiableVector3D<Vector3dfx, Point3dfx>() {
			
			private static final long serialVersionUID = 1638306005394957111L;

			@Override
			public GeomFactory3D<Vector3dfx, Point3dfx> getGeomFactory() {
				return Vector3dfx.this.getGeomFactory();
			}
			
			@Override
			public Vector3dfx toUnitVector() {
				return Vector3dfx.this.toUnitVector();
			}

			@Override
			public Vector3dfx clone() {
				return Vector3dfx.this.getGeomFactory().newVector(
						Vector3dfx.this.getX(), 
						Vector3dfx.this.getY(),
						Vector3dfx.this.getZ());
			}
			
			@Override
			public int iy() {
				return Vector3dfx.this.iy();
			}
			
			@Override
			public int ix() {
				return Vector3dfx.this.ix();
			}
			
			@Override
			public int iz() {
				return Vector3dfx.this.iz();
			}
			
			@Override
			public double getY() {
				return Vector3dfx.this.getY();
			}
			
			@Override
			public double getX() {
				return Vector3dfx.this.getX();
			}
			
			@Override
			public double getZ() {
				return Vector3dfx.this.getZ();
			}

		};
	}

}