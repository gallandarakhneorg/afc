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

package org.arakhne.afc.math.geometry.d3.ifx;

import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.geometry.d3.Point3D;
import org.arakhne.afc.math.geometry.d3.ai.Sphere3ai;
import org.arakhne.afc.math.geometry.fx.MathFXAttributeNames;
import org.arakhne.afc.vmutil.asserts.AssertMessages;

/** A sphere with 3 integer FX properties.
 *
 * @author $Author: sgalland$
 * @author $Author: tpiotrow$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public class Sphere3ifx
		extends AbstractShape3ifx<Sphere3ifx>
		implements Sphere3ai<Shape3ifx<?>, Sphere3ifx, PathElement3ifx, Point3ifx, Vector3ifx, RectangularPrism3ifx> {

	private static final long serialVersionUID = 3750916959512063017L;

	private Point3ifx center = new Point3ifx();

	private IntegerProperty radius;

	/** Construct an empty sphere.
	 */
	public Sphere3ifx() {
	    //
	}

	/** Construct a sphere at the given position and with the given radius.
	 * @param center the center of the sphere.
	 * @param radius the radius of the sphere.
	 */
	public Sphere3ifx(Point3D<?, ?> center, int radius) {
	    assert center != null : AssertMessages.notNullParameter();
	    set(center.ix(), center.iy(), center.iz(), radius);
	}

	/** Construct a sphere by setting the given position and radius.
	 * @param center the center of the sphere.
	 * @param radius the radius of the sphere.
	 */
	public Sphere3ifx(Point3ifx center, int radius) {
	    assert center != null;
	    this.center = center;
	    setRadius(radius);
	}

	/** Construct a sphere at the given position and with the given radius.
	 * @param x x coordinate of the center of the sphere.
	 * @param y y coordinate of the center of the sphere.
	 * @param z z coordinate of the center of the sphere.
	 * @param radius the radius of the sphere.
	 */
	public Sphere3ifx(int x, int y, int z, int radius) {
	    set(x, y, z, radius);
	}

	/** Constructor by copy.
	 * @param sphere the sphere to copy.
	 */
	public Sphere3ifx(Sphere3ai<?, ?, ?, ?, ?, ?> sphere) {
	    assert sphere != null : AssertMessages.notNullParameter();
	    set(sphere.getX(), sphere.getY(), sphere.getZ(), sphere.getRadius());
	}

	/** Construct by setting.
     * @param sphere the sphere to set.
     */
	public Sphere3ifx(Sphere3ifx sphere) {
		assert sphere != null : AssertMessages.notNullParameter();
		this.center = sphere.center;
		setRadius(sphere.getRadius());
	}

	@Override
	public Sphere3ifx clone() {
		final Sphere3ifx clone = super.clone();
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
		bits = 31 * bits + getX();
		bits = 31 * bits + getY();
		bits = 31 * bits + getZ();
		bits = 31 * bits + getRadius();
		return bits ^ (bits >> 31);
	}

	@Pure
	@Override
	public int getX() {
		return this.center.ix();
	}

	/** Replies the property that is the x coordinate of the sphere center.
	 *
	 * @return the x property.
	 */
	@Pure
	public IntegerProperty xProperty() {
		return this.center.xProperty();
	}

	@Pure
	@Override
	public int getY() {
		return this.center.iy();
	}

	/** Replies the property that is the y coordinate of the sphere center.
	 *
	 * @return the y property.
	 */
	@Pure
	public IntegerProperty yProperty() {
		return this.center.yProperty();
	}

	@Pure
	@Override
	public int getZ() {
		return this.center.iz();
	}

	/** Replies the property that is the z coordinate of the sphere center.
	 *
	 * @return the z property.
	 */
	@Pure
	public IntegerProperty zProperty() {
		return this.center.zProperty();
	}

	@Override
	public void setX(int x) {
		xProperty().set(x);
	}

	@Override
	public void setY(int y) {
		yProperty().set(y);
	}

	@Override
	public void setZ(int z) {
		zProperty().set(z);
	}

	@Pure
	@Override
	public int getRadius() {
		return this.radius == null ? 0 : this.radius.get();
	}

	@Override
	public void setRadius(int radius) {
		assert radius >= 0 : "Radius must be positive or zero"; //$NON-NLS-1$
		radiusProperty().set(radius);
	}

	/** Replies the property that is the radius of the sphere.
	 *
	 * @return the radius property.
	 */
	@Pure
	public IntegerProperty radiusProperty() {
		if (this.radius == null) {
			this.radius = new SimpleIntegerProperty(this, MathFXAttributeNames.RADIUS) {
				@Override
				protected void invalidated() {
					if (get() < 0) {
						set(0);
					}
				}
			};
		}
		return this.radius;
	}

	@Override
	public void set(int x, int y, int z, int radius) {
		assert radius >= 0 : "Radius must be positive or zero"; //$NON-NLS-1$
		xProperty().set(x);
		yProperty().set(y);
		zProperty().set(z);
		radiusProperty().set(radius);
	}

	@Override
	public ObjectProperty<RectangularPrism3ifx> boundingBoxProperty() {
		if (this.boundingBox == null) {
			this.boundingBox = new SimpleObjectProperty<>(this, MathFXAttributeNames.BOUNDING_BOX);
			this.boundingBox.bind(Bindings.createObjectBinding(() ->
			   toBoundingBox(),
			        xProperty(), yProperty(), zProperty(), radiusProperty()));
		}
		return this.boundingBox;
	}

}
