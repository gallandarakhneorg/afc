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

import java.lang.ref.WeakReference;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.ReadOnlyDoubleWrapper;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.geometry.d3.Vector3D;
import org.arakhne.afc.math.geometry.fx.MathFXAttributeNames;
import org.arakhne.afc.vmutil.asserts.AssertMessages;

/**
 * A JavaFX property that is representing a unit vector.
 *
 * @author $Author: sgalland$
 * @author $Author: tpiotrow$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public class UnitVectorProperty3dfx extends SimpleObjectProperty<Vector3dfx> {

	private final WeakReference<GeomFactory3dfx> factory;

	private ReadOnlyDoubleWrapper x;

	private ReadOnlyDoubleWrapper y;

	private ReadOnlyDoubleWrapper z;

	private Vector3dfx fake;

	/** Construct a property.
	 *
	 * @param bean the owner of the property.
	 * @param name the name of the property.
	 * @param factory the factory to use.
	 */
	public UnitVectorProperty3dfx(Object bean, String name, GeomFactory3dfx factory) {
		super(bean, name);
		this.factory = new WeakReference<>(factory);
	}

	/** Replies the geometry factory associated to this property.
	 *
	 * @return the geometry factory.
	 */
	public GeomFactory3dfx getGeomFactory() {
		return this.factory.get();
	}

	private ReadOnlyDoubleWrapper internalXProperty() {
		if (this.x == null) {
			init();
		}
		return this.x;
	}

	private ReadOnlyDoubleWrapper internalYProperty() {
		if (this.y == null) {
			init();
		}
		return this.y;
	}

	private ReadOnlyDoubleWrapper internalZProperty() {
		if (this.z == null) {
			init();
		}
		return this.z;
	}

	private void init() {
		final Vector3dfx v = getGeomFactory().newVector();
		this.x = new ReadOnlyDoubleWrapper(v, MathFXAttributeNames.X);
		this.y = new ReadOnlyDoubleWrapper(v, MathFXAttributeNames.Y);
		this.z = new ReadOnlyDoubleWrapper(v, MathFXAttributeNames.Z);
		v.set(this.x, this.y, this.z);
		super.set(v);
	}

	@Override
	public Vector3dfx get() {
		if (isBound()) {
			return super.get();
		}
		if (this.fake == null) {
			this.fake = getGeomFactory().newVector();
			final DoubleProperty x = new SimpleDoubleProperty(this.fake, MathFXAttributeNames.X);
			x.bind(internalXProperty());
			final DoubleProperty y = new SimpleDoubleProperty(this.fake, MathFXAttributeNames.Y);
			y.bind(internalYProperty());
			final DoubleProperty z = new SimpleDoubleProperty(this.fake, MathFXAttributeNames.Z);
			z.bind(internalYProperty());
			this.fake.set(x, y, z);
		}
		return this.fake;
	}

	@Override
	public void set(Vector3dfx newValue) {
		assert newValue != null : AssertMessages.notNullParameter();
		set(newValue.getX(), newValue.getY(), newValue.getZ());
	}

	/** Change the coordinates of the vector.
	 *
	 * @param x x coordinate of the vector.
	 * @param y y coordinate of the vector.
	 * @param z z coordinate of the vector.
	 */
	public void set(double x, double y, double z) {
		assert Vector3D.isUnitVector(x, y, z) : AssertMessages.normalizedParameters(0, 1, 2);
		if ((x != getX() || y != getY() || z != getZ()) && !isBound()) {
			final Vector3dfx v = super.get();
			v.set(x, y, z);
			fireValueChangedEvent();
		}
	}

	/** Replies the x coordinate of the vector.
	 *
	 * @return the x coordinate of the vector.
	 */
	public double getX() {
		if (isBound()) {
			return super.get().getX();
		}
		return internalXProperty().get();
	}

	/** Replies the y coordinate of the vector.
	 *
	 * @return the y coordinate of the vector.
	 */
	public double getY() {
		if (isBound()) {
			return super.get().getY();
		}
		return internalYProperty().get();
	}

	/** Replies the z coordinate of the vector.
	 *
	 * @return the z coordinate of the vector.
	 */
	public double getZ() {
		if (isBound()) {
			return super.get().getZ();
		}
		return internalZProperty().get();
	}

	/** Replies the x property.
	 *
	 * @return the x property.
	 */
	@Pure
	public ReadOnlyDoubleProperty xProperty() {
		if (isBound()) {
			return super.get().xProperty();
		}
		return internalXProperty().getReadOnlyProperty();
	}

	/** Replies the y property.
	 *
	 * @return the y property.
	 */
	@Pure
	public ReadOnlyDoubleProperty yProperty() {
		if (isBound()) {
			return super.get().yProperty();
		}
		return internalYProperty().getReadOnlyProperty();
	}

	/** Replies the z property.
	 *
	 * @return the z property.
	 */
	@Pure
	public ReadOnlyDoubleProperty zProperty() {
		if (isBound()) {
			return super.get().zProperty();
		}
		return internalZProperty().getReadOnlyProperty();
	}

}
