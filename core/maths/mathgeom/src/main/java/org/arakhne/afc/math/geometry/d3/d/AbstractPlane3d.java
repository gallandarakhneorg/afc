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

package org.arakhne.afc.math.geometry.d3.d;

import java.util.List;

import org.arakhne.afc.math.geometry.d3.Plane3D;
import org.arakhne.afc.math.geometry.d3.afp.Plane3afp;
import org.arakhne.afc.references.WeakArrayList;
import org.arakhne.afc.vmutil.ReflectionUtil;
import org.arakhne.afc.vmutil.asserts.AssertMessages;
import org.arakhne.afc.vmutil.json.JsonBuffer;
import org.eclipse.xtext.xbase.lib.Pure;

/** Abstract plane with 2 double precision floating-point numbers.
 *
 * @param <T> the type of the plane.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 18.0
 */
public abstract class AbstractPlane3d<T extends AbstractPlane3d<T>>
		implements Plane3afp<T, Segment3d, Point3d, Vector3d, Quaternion4d> {
	
	private static final long serialVersionUID = 8113456406143445368L;

	private List<ShapeGeometryChangeListener> geometryListeners;

	/** Add listener on geometry changes.
	 *
	 * @param listener the listener.
	 */
	protected synchronized void addShapeGeometryChangeListener(ShapeGeometryChangeListener listener) {
		assert listener != null : AssertMessages.notNullParameter();
		if (this.geometryListeners == null) {
			this.geometryListeners = new WeakArrayList<>();
		}
		this.geometryListeners.add(listener);
	}

	/** Remove listener on geometry changes.
	 *
	 * @param listener the listener.
	 */
	protected synchronized void removeShapeGeometryChangeListener(ShapeGeometryChangeListener listener) {
		assert listener != null : AssertMessages.notNullParameter();
		if (this.geometryListeners != null) {
			this.geometryListeners.remove(listener);
			if (this.geometryListeners.isEmpty()) {
				this.geometryListeners = null;
			}
		}
	}

	/** Notify any listener of a geometry change.
	 */
	protected synchronized void fireGeometryChange() {
		if (this.geometryListeners == null) {
			return;
		}
		final ShapeGeometryChangeListener[] array = new ShapeGeometryChangeListener[this.geometryListeners.size()];
		this.geometryListeners.toArray(array);
		for (final ShapeGeometryChangeListener listener : array) {
			listener.planeGeometryChange(this);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public T clone() {
		try {
			return (T) super.clone();
		} catch (CloneNotSupportedException exception) {
			throw new InternalError(exception);
		}
	}

	@Pure
	@Override
	public final boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		try {
			final Plane3D<?, ?, ?, ?, ?> p = (Plane3D<?, ?, ?, ?, ?>) obj;
			return getEquationComponentA() == p.getEquationComponentA()
					&& getEquationComponentB() == p.getEquationComponentB()
					&& getEquationComponentC() == p.getEquationComponentC()
					&& getEquationComponentD() == p.getEquationComponentD();
		} catch (Throwable exception) {
			//
		}
		return false;
	}

	@Pure
	@Override
	public int hashCode() {
		long bits = 1;
		bits = 31 * bits + Double.doubleToLongBits(getEquationComponentA());
		bits = 31 * bits + Double.doubleToLongBits(getEquationComponentB());
		bits = 31 * bits + Double.doubleToLongBits(getEquationComponentC());
		bits = 31 * bits + Double.doubleToLongBits(getEquationComponentD());
		final int b = (int) bits;
		return b ^ (b >> 31);
	}

	@Override
	public final GeomFactory3d getGeomFactory() {
		return GeomFactory3d.SINGLETON;
	}

	@Pure
	@Override
	public String toString() {
		return ReflectionUtil.toString(this);
	}

	@Override
	public void toJson(JsonBuffer buffer) {
		ReflectionUtil.toJson(this, buffer);
	}

}
