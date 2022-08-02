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

package org.arakhne.afc.math.geometry.d2.d;

import java.util.List;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.geometry.d2.Shape2D;
import org.arakhne.afc.references.WeakArrayList;
import org.arakhne.afc.vmutil.ReflectionUtil;
import org.arakhne.afc.vmutil.asserts.AssertMessages;
import org.arakhne.afc.vmutil.json.JsonBuffer;

/** Abstract shape with 2 double precision floating-point numbers.
 *
 * @param <T> the type of the shape.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public abstract class AbstractShape2d<T extends AbstractShape2d<?>> implements Shape2d<T> {

	private static final long serialVersionUID = 8998400951370579721L;

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
			listener.shapeGeometryChange(this);
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
	@SuppressWarnings("checkstyle:equalshashcode")
	public final boolean equals(Object obj) {
		return Shape2D.equals(this, obj);
	}

	@Pure
	@Override
    public abstract int hashCode();

	@Override
	public final GeomFactory2d getGeomFactory() {
		return GeomFactory2d.SINGLETON;
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
