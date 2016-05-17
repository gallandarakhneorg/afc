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

package org.arakhne.afc.math.geometry.d2.dfx;

import org.arakhne.afc.math.geometry.d2.GeomFactory2D;
import org.arakhne.afc.math.geometry.d2.Tuple2D;
import org.arakhne.afc.math.geometry.d2.UnmodifiableVector2D;
import org.arakhne.afc.math.geometry.d2.Vector2D;
import org.eclipse.xtext.xbase.lib.Pure;

import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.ReadOnlyDoubleWrapper;
import org.eclipse.xtext.xbase.lib.Inline;
import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.geometry.d2.GeomFactory;
import org.arakhne.afc.math.geometry.d2.MathFXAttributeNames;
import org.arakhne.afc.math.geometry.d2.Tuple2D;
import org.arakhne.afc.math.geometry.d2.UnmodifiableVector2D;
import org.arakhne.afc.math.geometry.d2.Vector2D;

/** 2D Vector with 2 double precision floating-point FX properties.
 *
 * @author $Author: sgalland$
 * @author $Author: olamotte$
 * @author $Author: hjaffali$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public class Vector2dfx extends Tuple2dfx<Vector2dfx> implements Vector2D<Vector2dfx, Point2dfx> {

	private static final long serialVersionUID = 8394433458442716159L;

	/** Property that contains the squared length.
	 */
	private ReadOnlyDoubleWrapper lengthSquareProperty;

	/** Property that contains the length.
	 */
	private ReadOnlyDoubleWrapper lengthProperty;

	/** Construct a zero vector.
	 */
	public Vector2dfx() {
		//
	}

	/** Construct a vector with the given coordinate properties.
	 * @param x the property for the x coordinate.
	 * @param y the property for the y coordinate.
	 */
	public Vector2dfx(DoubleProperty x, DoubleProperty y) {
		super(x, y);
	}

	/** Constructor by copy.
	 * @param tuple is the tuple to copy.
	 */
	public Vector2dfx(Tuple2D<?> tuple) {
		super(tuple);
	}

	/** Constructor by copy.
	 * @param tuple is the tuple to copy.
	 */
	public Vector2dfx(int[] tuple) {
		super(tuple);
	}

	/** Constructor by copy.
	 * @param tuple is the tuple to copy.
	 */
	public Vector2dfx(double[] tuple) {
		super(tuple);
	}

	/** Construct a vector with the given coordinates.
	 * @param x x coordinate.
	 * @param y y coordinate.
	 */
	public Vector2dfx(int x, int y) {
		super(x, y);
	}

	/** Construct a vector with the given coordinates.
	 * @param x x coordinate.
	 * @param y y coordinate.
	 */
	public Vector2dfx(float x, float y) {
		super(x, y);
	}

	/** Construct a vector with the given coordinates.
	 * @param x x coordinate.
	 * @param y y coordinate.
	 */
	public Vector2dfx(double x, double y) {
		super(x, y);
	}

	/** Construct a vector with the given coordinates.
	 * @param x x coordinate.
	 * @param y y coordinate.
	 */
	public Vector2dfx(long x, long y) {
		super(x, y);
	}

	/** Replies the orientation vector, which is corresponding
	 * to the given angle on a trigonometric circle.
	 *
	 * @param angle is the angle in radians to translate.
	 * @return the orientation vector which is corresponding to the given angle.
	 */
	@Pure
	@Inline(value = "new Vector2dfx(Math.cos($1), Math.sin($1))", imported = {Vector2dfx.class})
	public static Vector2dfx toOrientationVector(double angle) {
		return new Vector2dfx(Math.cos(angle), Math.sin(angle));
	}

	@Override
	public Vector2dfx clone() {
		final Vector2dfx clone = super.clone();
		clone.lengthSquareProperty = null;
		clone.lengthProperty = null;
		return clone;
	}

	@Override
	public Vector2dfx toUnitVector() {
		final double length = getLength();
		if (length == 0.) {
			return getGeomFactory().newVector();
		}
		return getGeomFactory().newVector(getX() / length, getY() / length);
	}

	@Override
	public Vector2dfx toOrthogonalVector() {
		return getGeomFactory().newVector(-getY(), getX());
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
			this.lengthProperty = new ReadOnlyDoubleWrapper(this, MathFXAttributeNames.LENGTH);
			this.lengthProperty.bind(Bindings.createDoubleBinding(() -> {
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
			this.lengthSquareProperty = new ReadOnlyDoubleWrapper(this, MathFXAttributeNames.LENGTH_SQUARED);
			this.lengthSquareProperty.bind(Bindings.createDoubleBinding(() -> {
				return Vector2dfx.this.x.doubleValue() * Vector2dfx.this.x.doubleValue()
						+ Vector2dfx.this.y.doubleValue() * Vector2dfx.this.y.doubleValue();
			}, this.x, this.y));
		}
		return this.lengthSquareProperty.getReadOnlyProperty();
	}

	@Override
	public GeomFactory2dfx getGeomFactory() {
		return GeomFactory2dfx.SINGLETON;
	}

	@Override
	public UnmodifiableVector2D<Vector2dfx, Point2dfx> toUnmodifiable() {
		return new UnmodifiableVector2D<Vector2dfx, Point2dfx>() {

			private static final long serialVersionUID = 1638306005394957111L;

			@Override
			public GeomFactory2D<Vector2dfx, Point2dfx> getGeomFactory() {
				return Vector2dfx.this.getGeomFactory();
			}

			@Override
			public Vector2dfx toUnitVector() {
				return Vector2dfx.this.toUnitVector();
			}

			@Override
			public Vector2dfx toOrthogonalVector() {
				return Vector2dfx.this.toOrthogonalVector();
			}

			@Override
			@SuppressWarnings("checkstyle:superclone")
			public Vector2dfx clone() {
				return Vector2dfx.this.getGeomFactory().newVector(
						Vector2dfx.this.getX(), Vector2dfx.this.getY());
			}

			@Override
			public int iy() {
				return Vector2dfx.this.iy();
			}

			@Override
			public int ix() {
				return Vector2dfx.this.ix();
			}

			@Override
			public double getY() {
				return Vector2dfx.this.getY();
			}

			@Override
			public double getX() {
				return Vector2dfx.this.getX();
			}

		};
	}

}
