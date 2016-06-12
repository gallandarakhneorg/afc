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

package org.arakhne.afc.math.geometry.d3.dfx;

import javafx.beans.property.ObjectProperty;

import org.arakhne.afc.math.geometry.d3.afp.Shape3afp;

/** 3D shape with 3 double precision floating-point FX properties.
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
