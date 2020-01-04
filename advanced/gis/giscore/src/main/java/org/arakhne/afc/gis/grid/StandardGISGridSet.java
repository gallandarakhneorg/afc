/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2020 The original authors, and other authors.
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

package org.arakhne.afc.gis.grid;

import org.arakhne.afc.gis.primitive.GISPrimitive;
import org.arakhne.afc.math.geometry.d2.d.Rectangle2d;

/**
 * This class describes a grid that contains GIS primitives
 * and that permits to find them according to there geo-location.
 *
 * @param <P> is the type of the user data inside the node.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 * @see GISPrimitive
 */
public class StandardGISGridSet<P extends GISPrimitive>
		extends AbstractGISGridSet<P> {

	/** Constructor.
	 * @param nRows numbers of rows in the grid
	 * @param nColumns numbers of columns in the grid
	 * @param bounds are the bounds of the grid cell.
	 */
	public StandardGISGridSet(int nRows, int nColumns, Rectangle2d bounds) {
		super(nRows, nColumns, bounds);
	}

	/** Constructor.
	 * @param nRows numbers of rows in the grid
	 * @param nColumns numbers of columns in the grid
	 * @param boundsX is the bounds of the scene.
	 * @param boundsY is the bounds of the scene.
	 * @param boundsWidth is the bounds of the scene.
	 * @param boundsHeight is the bounds of the scene.
	 */
	public StandardGISGridSet(int nRows, int nColumns, double boundsX, double boundsY, double boundsWidth, double boundsHeight) {
		super(nRows, nColumns, boundsX, boundsY, boundsWidth, boundsHeight);
	}

	@Override
	public boolean add(P point) {
		if (this.grid.addElement(point)) {
			updateComponentType(point);
			return true;
		}
		return false;
	}

}
