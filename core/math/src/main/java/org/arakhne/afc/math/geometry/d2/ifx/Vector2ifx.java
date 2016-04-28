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

import org.arakhne.afc.math.geometry.d2.GeomFactory;
import org.arakhne.afc.math.geometry.d2.Tuple2D;
import org.arakhne.afc.math.geometry.d2.UnmodifiableVector2D;
import org.arakhne.afc.math.geometry.d2.Vector2D;
import org.eclipse.xtext.xbase.lib.Pure;

import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ReadOnlyDoubleWrapper;

/** 2D Vector with 2 integer FX properties.
 * 
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public class Vector2ifx extends Tuple2ifx<Vector2ifx> implements Vector2D<Vector2ifx, Point2ifx> {

	private static final long serialVersionUID = 5782200591782721145L;

	/** Property that contains the squared length.
	 */
	private ReadOnlyDoubleWrapper lengthSquareProperty;
	
	/** Property that contains the length.
	 */
	private ReadOnlyDoubleWrapper lengthProperty;

	/**
	 */
	public Vector2ifx() {
		//
	}

	/**
	 * @param xProperty
	 * @param yProperty
	 */
	public Vector2ifx(IntegerProperty xProperty, IntegerProperty yProperty) {
		super(xProperty, yProperty);
	}

	/**
	 * @param tuple is the tuple to copy.
	 */
	public Vector2ifx(Tuple2D<?> tuple) {
		super(tuple);
	}

	/**
	 * @param tuple is the tuple to copy.
	 */
	public Vector2ifx(int[] tuple) {
		super(tuple);
	}

	/**
	 * @param tuple is the tuple to copy.
	 */
	public Vector2ifx(double[] tuple) {
		super(tuple);
	}

	/**
	 * @param x
	 * @param y
	 */
	public Vector2ifx(int x, int y) {
		super(x,y);
	}

	/**
	 * @param x
	 * @param y
	 */
	public Vector2ifx(float x, float y) {
		super(x,y);
	}

	/**
	 * @param x
	 * @param y
	 */
	public Vector2ifx(double x, double y) {
		super(x,y);
	}

	/**
	 * @param x
	 * @param y
	 */
	public Vector2ifx(long x, long y) {
		super(x,y);
	}
	
	@Override
	public Vector2ifx clone() {
		Vector2ifx clone = super.clone();
		clone.lengthSquareProperty = null;
		clone.lengthProperty = null;
		return clone;
	}

	@Override
	public Vector2ifx toUnitVector() {
		double length = getLength();
		if (length == 0.) {
			return new Vector2ifx();
		}
		return new Vector2ifx(getX() / length, getY() / length);
	}
	
	@Override
	public Vector2ifx toOrthogonalVector() {
		return new Vector2ifx(-iy(), ix());
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
						return Vector2ifx.this.x.doubleValue() * Vector2ifx.this.x.doubleValue()
								+ Vector2ifx.this.y.doubleValue() * Vector2ifx.this.y.doubleValue();
					}, this.x, this.y));
		}
		return this.lengthSquareProperty;
	}

	@Override
	public GeomFactory2ifx getGeomFactory() {
		return GeomFactory2ifx.SINGLETON;
	}
	
	@Pure
	@Override
	public UnmodifiableVector2D<Vector2ifx, Point2ifx> toUnmodifiable() {
		return new UnmodifiableVector2D<Vector2ifx, Point2ifx>() {

			private static final long serialVersionUID = -3525974627723161583L;

			@Override
			public GeomFactory<Vector2ifx, Point2ifx> getGeomFactory() {
				return Vector2ifx.this.getGeomFactory();
			}
			
			@Override
			public Vector2ifx toUnitVector() {
				return Vector2ifx.this.toUnitVector();
			}
			
			@Override
			public Vector2ifx toOrthogonalVector() {
				return Vector2ifx.this.toOrthogonalVector();
			}
			
			@Override
			public Vector2ifx clone() {
				return new Vector2ifx(Vector2ifx.this);
			}

			@Override
			public double getX() {
				return Vector2ifx.this.getX();
			}

			@Override
			public int ix() {
				return Vector2ifx.this.ix();
			}

			@Override
			public double getY() {
				return Vector2ifx.this.getY();
			}

			@Override
			public int iy() {
				return Vector2ifx.this.iy();
			}

		};
	}

}