/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2022 The original authors, and other authors.
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

package org.arakhne.afc.math.geometry.d3.dfx;

import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.ReadOnlyDoubleWrapper;

import org.arakhne.afc.math.geometry.d3.GeomFactory3D;
import org.arakhne.afc.math.geometry.d3.Tuple3D;
import org.arakhne.afc.math.geometry.d3.UnmodifiableVector3D;
import org.arakhne.afc.math.geometry.d3.Vector3D;
import org.arakhne.afc.math.geometry.fx.MathFXAttributeNames;
import org.arakhne.afc.vmutil.json.JsonBuffer;

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

	/** Construct a zero vector.
     */
	public Vector3dfx() {
		//
	}

	/** Construct a vector with the given coordinate properties.
	 * @param x the property for the x coordinate.
	 * @param y the property for the y coordinate.
	 * @param z the property for the y coordinate.
	 */
	public Vector3dfx(DoubleProperty x, DoubleProperty y, DoubleProperty z) {
		super(x, y, z);
	}

	/** Constructor by copy.
     * @param tuple is the tuple to copy.
     */
	public Vector3dfx(Tuple3D<?> tuple) {
		super(tuple);
	}

	/** Constructor by copy.
     * @param tuple is the tuple to copy.
     */
	public Vector3dfx(int[] tuple) {
		super(tuple);
	}

	/** Constructor by copy.
     * @param tuple is the tuple to copy.
     */
	public Vector3dfx(double[] tuple) {
		super(tuple);
	}

	/** Construct a vector with the given coordinates.
     * @param x x coordinate.
     * @param y y coordinate.
     * @param z z coordinate.
     */
	public Vector3dfx(int x, int y, int z) {
		super(x, y, z);
	}

	/** Construct a vector with the given coordinates.
     * @param x x coordinate.
     * @param y y coordinate.
     * @param z z coordinate.
     */
	public Vector3dfx(float x, float y, float z) {
		super(x, y, z);
	}

	/** Construct a vector with the given coordinates.
     * @param x x coordinate.
     * @param y y coordinate.
     * @param z z coordinate.
     */
	public Vector3dfx(double x, double y, double z) {
		super(x, y, z);
	}

	/** Construct a vector with the given coordinates.
     * @param x x coordinate.
     * @param y y coordinate.
     * @param z z coordinate.
     */
	public Vector3dfx(long x, long y, long z) {
		super(x, y, z);
	}

	@Override
	public Vector3dfx clone() {
		final Vector3dfx clone = super.clone();
		clone.lengthSquareProperty = null;
		clone.lengthProperty = null;
		return clone;
	}

	/** Convert the given tuple to a real Vector3dfx.
	 *
	 * <p>If the given tuple is already a Vector3dfx, it is replied.
	 *
	 * @param tuple the tuple.
	 * @return the Vector3dfx.
	 * @since 14.0
	 */
	public static Vector3dfx convert(Tuple3D<?> tuple) {
		if (tuple instanceof Vector3dfx) {
			return (Vector3dfx) tuple;
		}
		return new Vector3dfx(tuple.getX(), tuple.getY(), tuple.getZ());
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
			this.lengthProperty.bind(Bindings.createDoubleBinding(() ->
			    Math.sqrt(lengthSquaredProperty().doubleValue()), lengthSquaredProperty()));
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
			this.lengthSquareProperty.bind(Bindings.createDoubleBinding(() ->
			    Vector3dfx.this.x.doubleValue() * Vector3dfx.this.x.doubleValue()
			            + Vector3dfx.this.y.doubleValue() * Vector3dfx.this.y.doubleValue()
			            + Vector3dfx.this.z.doubleValue() * Vector3dfx.this.z.doubleValue(), this.x, this.y, this.z));
		}
		return this.lengthSquareProperty.getReadOnlyProperty();
	}

	@Override
	public GeomFactory3dfx getGeomFactory() {
		return GeomFactory3dfx.SINGLETON;
	}

	@Override
	public UnmodifiableVector3D<Vector3dfx, Point3dfx> toUnmodifiable() {
		return new UnmodifiableVector3D<>() {

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
			@SuppressWarnings("checkstyle:superclone")
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

			@Override
			public String toString() {
				return Vector3dfx.this.toString();
			}

			@Override
			public void toJson(JsonBuffer buffer) {
				Vector3dfx.this.toJson(buffer);
			}

		};
	}

}
