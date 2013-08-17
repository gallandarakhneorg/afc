/* $Id$
 * 
 * This is a part of NetEditor project from Arakhne.org:
 * package org.arakhne.neteditor.zoompanel.
 * 
 * Copyright (C) 2002  St&eacute;phane GALLAND, Mahdi Hannoun
 * Copyright (C) 2004-2012  St&eacute;phane GALLAND
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

package org.arakhne.afc.ui.awt;

import java.awt.Shape;

import org.arakhne.afc.ui.CenteringTransform;

/** Shape that may be transformed from screen to a logical
 * coordinate space.
 *
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
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
