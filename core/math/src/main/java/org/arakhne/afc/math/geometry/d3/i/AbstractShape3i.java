/* 
 * $Id$
 * 
 * Copyright (C) 2010-2013 Stephane GALLAND.
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
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
package org.arakhne.afc.math.geometry.d3.i;

import java.util.List;

import org.arakhne.afc.math.geometry.d3.PathIterator3D;
import org.arakhne.afc.references.WeakArrayList;
import org.eclipse.xtext.xbase.lib.Pure;

/** Abstract shape with 2 integer numbers.
 * 
 * @param <T> the type of the shape.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public abstract class AbstractShape3i<T extends AbstractShape3i<?>> implements Shape3i<T> {

	private static final long serialVersionUID = -1068879174912644974L;

	private List<ShapeGeometryChangeListener> geometryListeners;

	/** Add listener on geometry changes.
	 *
	 * @param listener the listener.
	 */
	protected synchronized void addShapeGeometryChangeListener(ShapeGeometryChangeListener listener) {
		assert (listener != null) : "Listener must be not null"; //$NON-NLS-1$
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
		assert (listener != null) : "Listener must be not null"; //$NON-NLS-1$
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
		ShapeGeometryChangeListener[] array = new ShapeGeometryChangeListener[this.geometryListeners.size()];
		this.geometryListeners.toArray(array);
		for (ShapeGeometryChangeListener listener : array) {
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
	
	@SuppressWarnings("unchecked")
	@Pure
	@Override
	public final boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		try {
			try {
				return equalsToShape((T) obj);
			} catch (ClassCastException exception) {
				return equalsToPathIterator((PathIterator3D<?>) obj);
			}
		} catch (Throwable exception) {
			//
		}
		return false;
	}

	@Pure
    @Override
    public abstract int hashCode();

	@Override
	public final GeomFactory3i getGeomFactory() {
		return GeomFactory3i.SINGLETON;
	}

}