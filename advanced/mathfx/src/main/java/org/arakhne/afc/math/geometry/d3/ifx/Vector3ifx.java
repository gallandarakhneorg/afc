/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2018 The original authors, and other authors.
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

package org.arakhne.afc.math.geometry.d3.ifx;

import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ReadOnlyDoubleWrapper;
import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.geometry.MathFXAttributeNames;
import org.arakhne.afc.math.geometry.d3.GeomFactory3D;
import org.arakhne.afc.math.geometry.d3.Tuple3D;
import org.arakhne.afc.math.geometry.d3.UnmodifiableVector3D;
import org.arakhne.afc.math.geometry.d3.Vector3D;

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

	/** Construct a zero vector.
     */
	public Vector3ifx() {
		//
	}

	/** Construct a vector with the given properties for the coordinates.
     * @param xProperty property for the x coordinate.
     * @param yProperty property for the y coordinate.
     * @param zProperty property for the z coordinate.
     */
	public Vector3ifx(IntegerProperty xProperty, IntegerProperty yProperty, IntegerProperty zProperty) {
		super(xProperty, yProperty, zProperty);
	}

	/** Constructor by copy.
     * @param tuple is the tuple to copy.
     */
	public Vector3ifx(Tuple3D<?> tuple) {
		super(tuple);
	}

	/** Constructor by copy.
     * @param tuple is the tuple to copy.
     */
	public Vector3ifx(int[] tuple) {
		super(tuple);
	}

	/** Constructor by copy.
     * @param tuple is the tuple to copy.
     */
	public Vector3ifx(double[] tuple) {
		super(tuple);
	}

	/** Construct a vector with the given coordinates.
     * @param x x coordinate.
     * @param y y coordinate.
     * @param z z coordinate.
     */
	public Vector3ifx(int x, int y, int z) {
		super(x, y, z);
	}

	/** Construct a vector with the given coordinates.
     * @param x x coordinate.
     * @param y y coordinate.
     * @param z z coordinate.
     */
	public Vector3ifx(float x, float y, float z) {
		super(x, y, z);
	}

	/** Construct a vector with the given coordinates.
     * @param x x coordinate.
     * @param y y coordinate.
     * @param z z coordinate.
     */
	public Vector3ifx(double x, double y, double z) {
		super(x, y, z);
	}

	/** Construct a vector with the given coordinates.
     * @param x x coordinate.
     * @param y y coordinate.
     * @param z z coordinate.
     */
	public Vector3ifx(long x, long y, long z) {
		super(x, y, z);
	}

	/** Convert the given tuple to a real Vector3ifx.
	 *
	 * <p>If the given tuple is already a Vector3ifx, it is replied.
	 *
	 * @param tuple the tuple.
	 * @return the Vector3ifx.
	 * @since 14.0
	 */
	public static Vector3ifx convert(Tuple3D<?> tuple) {
		if (tuple instanceof Vector3ifx) {
			return (Vector3ifx) tuple;
		}
		return new Vector3ifx(tuple.getX(), tuple.getY(), tuple.getZ());
	}

	@Override
	public Vector3ifx clone() {
		final Vector3ifx clone = super.clone();
		clone.lengthSquareProperty = null;
		clone.lengthProperty = null;
		return clone;
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
			this.lengthProperty.bind(Bindings.createDoubleBinding(() ->
			    Math.sqrt(lengthSquaredProperty().doubleValue()), lengthSquaredProperty()));
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
			this.lengthSquareProperty.bind(Bindings.createDoubleBinding(() ->
					Vector3ifx.this.x.doubleValue() * Vector3ifx.this.x.doubleValue()
							+ Vector3ifx.this.y.doubleValue() * Vector3ifx.this.y.doubleValue()
							+ Vector3ifx.this.z.doubleValue() * Vector3ifx.this.z.doubleValue(), this.x, this.y, this.z));
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
			@SuppressWarnings("checkstyle:superclone")
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
