/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2016 The original authors, and other authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.arakhne.afc.math.geometry.d2.ifx;

import org.arakhne.afc.math.geometry.d2.GeomFactory2D;
import org.arakhne.afc.math.geometry.d2.Tuple2D;
import org.arakhne.afc.math.geometry.d2.UnmodifiableVector2D;
import org.arakhne.afc.math.geometry.d2.Vector2D;
import org.eclipse.xtext.xbase.lib.Pure;

import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ReadOnlyDoubleWrapper;
import org.eclipse.xtext.xbase.lib.Inline;
import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.geometry.d2.GeomFactory2D;
import org.arakhne.afc.math.geometry.d2.MathFXAttributeNames;
import org.arakhne.afc.math.geometry.d2.Tuple2D;
import org.arakhne.afc.math.geometry.d2.UnmodifiableVector2D;
import org.arakhne.afc.math.geometry.d2.Vector2D;

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

	/** Construct a zero vector.
	 */
	public Vector2ifx() {
		//
	}

	/** Construct a vector with the given properties for the coordinates.
	 * @param xProperty property for the x coordinate.
	 * @param yProperty property for the y coordinate.
	 */
	public Vector2ifx(IntegerProperty xProperty, IntegerProperty yProperty) {
		super(xProperty, yProperty);
	}

	/** Constructor by copy.
	 * @param tuple is the tuple to copy.
	 */
	public Vector2ifx(Tuple2D<?> tuple) {
		super(tuple);
	}

	/** Constructor by copy.
	 * @param tuple is the tuple to copy.
	 */
	public Vector2ifx(int[] tuple) {
		super(tuple);
	}

	/** Constructor by copy.
	 * @param tuple is the tuple to copy.
	 */
	public Vector2ifx(double[] tuple) {
		super(tuple);
	}

	/** Construct a vector with the given coordinates.
	 * @param x x coordinate.
	 * @param y y coordinate.
	 */
	public Vector2ifx(int x, int y) {
		super(x, y);
	}

	/** Construct a vector with the given coordinates.
	 * @param x x coordinate.
	 * @param y y coordinate.
	 */
	public Vector2ifx(float x, float y) {
		super(x, y);
	}

	/** Construct a vector with the given coordinates.
	 * @param x x coordinate.
	 * @param y y coordinate.
	 */
	public Vector2ifx(double x, double y) {
		super(x, y);
	}

	/** Construct a vector with the given coordinates.
	 * @param x x coordinate.
	 * @param y y coordinate.
	 */
	public Vector2ifx(long x, long y) {
		super(x, y);
	}

	/** Replies the orientation vector, which is corresponding
	 * to the given angle on a trigonometric circle.
	 *
	 * @param angle is the angle in radians to translate.
	 * @return the orientation vector which is corresponding to the given angle.
	 */
	@Pure
	@Inline(value = "new Vector2ifx(Math.cos($1), Math.sin($1))", imported = {Vector2ifx.class})
	public static Vector2ifx toOrientationVector(double angle) {
		return new Vector2ifx(Math.cos(angle), Math.sin(angle));
	}

	@Override
	public Vector2ifx clone() {
		final Vector2ifx clone = super.clone();
		clone.lengthSquareProperty = null;
		clone.lengthProperty = null;
		return clone;
	}

	@Override
	public Vector2ifx toUnitVector() {
		final double length = getLength();
		if (length == 0.) {
			return getGeomFactory().newVector();
		}
		final int x = (int) Math.round(ix() / length);
		final int y = (int) Math.round(iy() / length);
		return getGeomFactory().newVector(x, y);
	}

	@Override
	public Vector2ifx toOrthogonalVector() {
		return getGeomFactory().newVector(-iy(), ix());
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
			this.lengthProperty = new ReadOnlyDoubleWrapper(this, MathFXAttributeNames.LENGTH);
			this.lengthProperty.bind(Bindings.createDoubleBinding(() -> {
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
			this.lengthSquareProperty = new ReadOnlyDoubleWrapper(this, MathFXAttributeNames.LENGTH_SQUARED);
			this.lengthSquareProperty.bind(Bindings.createDoubleBinding(() -> {
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
			public GeomFactory2D<Vector2ifx, Point2ifx> getGeomFactory() {
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
			@SuppressWarnings("checkstyle:superclone")
			public Vector2ifx clone() {
				return Vector2ifx.this.getGeomFactory().newVector(
						Vector2ifx.this.ix(),
						Vector2ifx.this.iy());
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
