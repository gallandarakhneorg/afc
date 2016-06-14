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

import java.lang.ref.WeakReference;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.ReadOnlyDoubleWrapper;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.geometry.MathFXAttributeNames;
import org.arakhne.afc.math.geometry.d2.Vector2D;
import org.arakhne.afc.vmutil.asserts.AssertMessages;

/**
 * A JavaFX property that is representing a unit vector.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public class UnitVectorProperty extends SimpleObjectProperty<Vector2dfx> {

	private final WeakReference<GeomFactory2dfx> factory;

	private ReadOnlyDoubleWrapper x;

	private ReadOnlyDoubleWrapper y;

	private Vector2dfx fake;

	/** Construct a property.
	 *
	 * @param bean the owner of the property.
	 * @param name the name of the property.
	 * @param factory the factory to use.
	 */
	public UnitVectorProperty(Object bean, String name, GeomFactory2dfx factory) {
		super(bean, name);
		this.factory = new WeakReference<>(factory);
	}

	/** Replies the geometry factory associated to this property.
	 *
	 * @return the geometry factory.
	 */
	public GeomFactory2dfx getGeomFactory() {
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

	private void init() {
		final Vector2dfx v = getGeomFactory().newVector();
		this.x = new ReadOnlyDoubleWrapper(v, MathFXAttributeNames.X);
		this.y = new ReadOnlyDoubleWrapper(v, MathFXAttributeNames.X);
		v.set(this.x, this.y);
		super.set(v);
	}

	@Override
	public Vector2dfx get() {
		if (isBound()) {
			return super.get();
		}
		if (this.fake == null) {
			this.fake = getGeomFactory().newVector();
			final DoubleProperty x = new SimpleDoubleProperty(this.fake, MathFXAttributeNames.X);
			x.bind(internalXProperty());
			final DoubleProperty y = new SimpleDoubleProperty(this.fake, MathFXAttributeNames.Y);
			y.bind(internalYProperty());
			this.fake.set(x, y);
		}
		return this.fake;
	}

	@Override
	public void set(Vector2dfx newValue) {
		assert newValue != null : AssertMessages.notNullParameter();
		set(newValue.getX(), newValue.getY());
	}

	/** Change the coordinates of the vector.
	 *
	 * @param x x coordinate of the vector.
	 * @param y y coordinate of the vector.
	 */
	public void set(double x, double y) {
		assert Vector2D.isUnitVector(x, y) : AssertMessages.normalizedParameters(0, 1);
		if ((x != getX() || y != getY()) && !isBound()) {
			final Vector2dfx v = super.get();
			v.set(x, y);
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

}
