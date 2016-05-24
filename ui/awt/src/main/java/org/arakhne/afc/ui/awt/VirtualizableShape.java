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

package org.arakhne.afc.ui.awt;

import java.awt.Shape;

import org.arakhne.afc.ui.CenteringTransform;

/** Shape that may be transformed from screen to a logical
 * coordinate space.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @deprecated see JavaFX API
 */
@Deprecated
public interface VirtualizableShape extends Shape {

	/** Replies a shape that is the same of this shape
	 * on the screen.
	 * 
	 * @param centeringTransform is the transform to apply to the points to change from/to the coordinate
	 * system from the "global" logical coordinate system to/from the "centered" logical coordinate
	 * system.
	 * @param zoom is the current zooming factor of the view.
	 * @return the shape.
	 */
	public VirtualizableShape toScreen(CenteringTransform centeringTransform, float zoom);

	/** Replies a shape that is the same of this shape
	 * on the virtual area.
	 * 
	 * @param centeringTransform is the transform to apply to the points to change from/to the coordinate
	 * system from the "global" logical coordinate system to/from the "centered" logical coordinate
	 * system.
	 * @param zoom is the current zooming factor of the view.
	 * @return the shape.
	 */
	public VirtualizableShape fromScreen(CenteringTransform centeringTransform, float zoom);

}
