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
package org.arakhne.afc.math.geometry.d3.dfx;

import org.arakhne.afc.math.geometry.d3.afp.Shape3afp;

import javafx.beans.property.ObjectProperty;

/** 2D shape with 2 double precision floating-point FX properties.
 *
 * @param <IT> is the type of the implementation of this shape.
 * @author $Author: sgalland$
 * @author $Author: olamotte$
 * @author $Author: tpiotrow$
 * @author $Author: hjaffali$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public interface Shape3dfx<IT extends Shape3dfx<?>>
		extends Shape3afp<Shape3dfx<?>, IT, PathElement3dfx, Point3dfx, Vector3dfx, RectangularPrism3dfx> {
	
	/** Replies the property that contains the bounding box for this shape.
	 *
	 * @return the bounding box.
	 */
	ObjectProperty<RectangularPrism3dfx> boundingBoxProperty();

}