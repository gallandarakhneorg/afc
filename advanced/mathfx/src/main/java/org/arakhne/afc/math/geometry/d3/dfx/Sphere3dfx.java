/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2023 The original authors and other contributors.
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
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.geometry.d3.Point3D;
import org.arakhne.afc.math.geometry.d3.afp.Sphere3afp;
import org.arakhne.afc.math.geometry.fx.MathFXAttributeNames;
import org.arakhne.afc.vmutil.asserts.AssertMessages;

/** Sphere with 3 double precision floating-point FX properties.
 *
 * @author $Author: sgalland$
 * @author $Author: hjaffali$
 * @author $Author: tpiotrow$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public class Sphere3dfx
		extends AbstractShape3dfx<Sphere3dfx>
		implements Sphere3afp<Shape3dfx<?>, Sphere3dfx, PathElement3dfx, Point3dfx, Vector3dfx, AlignedBox3dfx> {

	private static final long serialVersionUID = 837592010117981823L;

	private Point3dfx center = new Point3dfx();

	private DoubleProperty radius;

	/** Construct an empty sphere.
     */
	public Sphere3dfx() {
		//
	}

	/** Construct a sphere with the given position and radius.
	 * @param center the center of the sphere.
	 * @param radius the radius of the sphere.
	 */
	public Sphere3dfx(Point3D<?, ?> center, double radius) {
	    assert center != null : AssertMessages.notNullParameter(0);
	    set(center.getX(), center.getY(), center.getZ(), radius);
	}

	/** Construct a sphere by setting the given position and radius.
     * @param center the center of the sphere.
     * @param radius the radius of the sphere.
     */
	public Sphere3dfx(Point3dfx center, double radius) {
		assert center != null : AssertMessages.notNullParameter(0);
		this.center = center;
		setRadius(radius);
	}

	/** Construct a sphere with the given position and radius.
     * @param x x coordinate of the center of the sphere.
     * @param y y coordinate of the center of the sphere.
     * @param z z coordinate of the center of the sphere.
     * @param radius the radius of the circle.
     */
	public Sphere3dfx(double x, double y, double z, double radius) {
		set(x, y, z, radius);
	}

	/** Constructor by copy.
	 * @param sphere the sphere to copy.
	 */
	public Sphere3dfx(Sphere3afp<?, ?, ?, ?, ?, ?> sphere) {
	    assert sphere != null : AssertMessages.notNullParameter();
	    set(sphere.getX(), sphere.getY(), sphere.getZ(), sphere.getRadius());
	}

	/** Constructor by setting.
     * @param sphere the sphere to set.
     */
	public Sphere3dfx(Sphere3dfx sphere) {
		assert sphere != null : AssertMessages.notNullParameter();
		this.center = sphere.center;
		setRadius(sphere.getRadius());
	}

	@Pure
	@Override
	public Sphere3dfx clone() {
		final Sphere3dfx clone = super.clone();
		if (clone.center != null) {
			clone.center = null;
			clone.center = this.center.clone();
		}
		if (clone.radius != null) {
			clone.radius = null;
			clone.radiusProperty().set(getRadius());
		}
		return clone;
	}

	@Pure
	@Override
	@SuppressWarnings("checkstyle:equalshashcode")
	public int hashCode() {
		int bits = 1;
		bits = 31 * bits + Double.hashCode(getX());
		bits = 31 * bits + Double.hashCode(getY());
		bits = 31 * bits + Double.hashCode(getZ());
		bits = 31 * bits + Double.hashCode(getRadius());
		return bits ^ (bits >> 31);
	}

	@Pure
	@Override
	public double getX() {
		return this.center.getX();
	}

	@Pure
	@Override
	public double getY() {
		return this.center.getY();
	}

	@Pure
	@Override
	public double getZ() {
		return this.center.getZ();
	}

	@Pure
	@Override
	public Point3dfx getCenter() {
		return this.center;
	}

	@Override
	public void setX(double x) {
		xProperty().set(x);
	}

	@Override
	public void setY(double y) {
		yProperty().set(y);
	}

	@Override
	public void setZ(double z) {
		zProperty().set(z);
	}

	/** Replies the property that is the x coordinate of the circle center.
	 *
	 * @return the x property.
	 */
	@Pure
	public DoubleProperty xProperty() {
		return this.center.xProperty();
	}

	/** Replies the property that is the y coordinate of the circle center.
	 *
	 * @return the y property.
	 */
	@Pure
	public DoubleProperty yProperty() {
		return this.center.yProperty();
	}

	/** Replies the property that is the z coordinate of the circle center.
	 *
	 * @return the z property.
	 */
	@Pure
	public DoubleProperty zProperty() {
		return this.center.zProperty();
	}

	@Pure
	@Override
	public double getRadius() {
		return this.radius == null ? 0 : this.radius.get();
	}

	@Override
	public void setRadius(double radius) {
		assert radius >= 0 : AssertMessages.positiveOrZeroParameter();
		radiusProperty().set(radius);
	}

	/** Replies the property that is the radius of the circle.
	 *
	 * @return the radius property.
	 */
	@Pure
	public DoubleProperty radiusProperty() {
		if (this.radius == null) {
			this.radius = new SimpleDoubleProperty(this, MathFXAttributeNames.RADIUS) {
				@Override
				protected void invalidated() {
					if (get() < 0.) {
						set(0.);
					}
				}
			};
		}
		return this.radius;
	}

	@Override
	public void set(double x, double y, double z, double radius) {
		assert radius >= 0 : AssertMessages.positiveOrZeroParameter(3);
		xProperty().set(x);
		yProperty().set(y);
		zProperty().set(z);
		radiusProperty().set(radius);
	}

	@Override
	public ObjectProperty<AlignedBox3dfx> boundingBoxProperty() {
		if (this.boundingBox == null) {
			this.boundingBox = new SimpleObjectProperty<>(this, MathFXAttributeNames.BOUNDING_BOX);
			this.boundingBox.bind(Bindings.createObjectBinding(() ->
			    toBoundingBox(),
			        xProperty(), yProperty(), zProperty(), radiusProperty()));
		}
		return this.boundingBox;
	}

}
