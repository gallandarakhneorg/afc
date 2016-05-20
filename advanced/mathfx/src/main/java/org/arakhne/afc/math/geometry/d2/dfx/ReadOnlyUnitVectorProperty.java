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
package org.arakhne.afc.math.geometry.d2.dfx;

import javafx.beans.property.ReadOnlyProperty;

/**
 * A JavaFX read-only property that is representing a unit vector.
 * 
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public interface ReadOnlyUnitVectorProperty extends ReadOnlyProperty<Vector2dfx> {

    /**
     * Returns the current value of this property.
     * 
     * @return the current value.
     */
    Vector2dfx get();

    /** Replies the x coordinate of the vector.
	 *
	 * @return the x coordinate of the vector.
	 */
	double getX();

	/** Replies the y coordinate of the vector.
	 *
	 * @return the y coordinate of the vector.
	 */
	double getY();

}
