/* 
 * $Id$
 * 
 * Copyright (C) 2013 Christophe BOHRHAUER.
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
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

import java.lang.ref.WeakReference;

import org.arakhne.afc.math.geometry.d3.Vector3D;
import org.eclipse.xtext.xbase.lib.Pure;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.ReadOnlyDoubleWrapper;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;

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
		Vector3dfx v = getGeomFactory().newVector();
		this.x = new ReadOnlyDoubleWrapper(v, "x"); //$NON-NLS-1$
		this.y = new ReadOnlyDoubleWrapper(v, "y"); //$NON-NLS-1$
		this.z = new ReadOnlyDoubleWrapper(v, "z"); //$NON-NLS-1$
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
			DoubleProperty x = new SimpleDoubleProperty(this.fake, "x"); //$NON-NLS-1$
			x.bind(internalXProperty());
			DoubleProperty y = new SimpleDoubleProperty(this.fake, "y"); //$NON-NLS-1$
			y.bind(internalYProperty());
			DoubleProperty z = new SimpleDoubleProperty(this.fake, "z"); //$NON-NLS-1$
			z.bind(internalYProperty());
			this.fake.set(x, y, z);
		}
		return this.fake;
	}
	
	@Override
	public void set(Vector3dfx newValue) {
		assert (newValue != null) : "Initial value must be not null"; //$NON-NLS-1$
		set(newValue.getX(), newValue.getY(), newValue.getZ());
	}

	/** Change the coordinates of the vector.
	 *
	 * @param x x coordinate of the vector.
	 * @param y y coordinate of the vector.
	 * @param z z coordinate of the vector.
	 */
	public void set(double x, double y, double z) {
		assert (Vector3D.isUnitVector(x, y, z)) : "Vector coordinates must correspond to a unit vector"; //$NON-NLS-1$
		if ((x != getX() || y != getY() || z != getZ()) && !isBound()) {
			Vector3dfx v = super.get();
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
