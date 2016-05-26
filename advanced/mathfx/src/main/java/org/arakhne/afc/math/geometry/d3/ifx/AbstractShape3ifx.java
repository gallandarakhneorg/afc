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
package org.arakhne.afc.math.geometry.d3.ifx;

import org.arakhne.afc.math.geometry.d3.PathIterator3D;
import org.eclipse.xtext.xbase.lib.Pure;

import javafx.beans.property.ObjectProperty;

/** Abstract shape with 3 integer FX properties.
 * 
 * @param <T> the type of the shape.
 * @author $Author: sgalland$
 * @author $Author: tpiotrow$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public abstract class AbstractShape3ifx<T extends AbstractShape3ifx<?>> implements Shape3ifx<T> {

	private static final long serialVersionUID = -2142220243877033508L;

	/** Bounding box property.
	 */
	protected ObjectProperty<RectangularPrism3ifx> boundingBox;
	
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
	public final GeomFactory3ifx getGeomFactory() {
		return GeomFactory3ifx.SINGLETON;
	}

}