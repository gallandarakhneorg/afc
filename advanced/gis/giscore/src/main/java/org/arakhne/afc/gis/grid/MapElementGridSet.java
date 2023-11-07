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

package org.arakhne.afc.gis.grid;

import org.eclipse.xtext.xbase.lib.Pair;
import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.gis.GISElementSet;
import org.arakhne.afc.gis.mapelement.MapElement;
import org.arakhne.afc.gis.primitive.GISPrimitive;
import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.d.Point2d;
import org.arakhne.afc.math.geometry.d2.d.Rectangle2d;

/**
 * This class describes a grid that contains map elements
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
public class MapElementGridSet<P extends MapElement> extends AbstractGISGridSet<P> implements GISElementSet<P> {

	/** Constructor.
	 * @param nRows numbers of rows in the grid
	 * @param nColumns numbers of columns in the grid
	 * @param boundsX is the bounds of the scene.
	 * @param boundsY is the bounds of the scene.
	 * @param boundsWidth is the bounds of the scene.
	 * @param boundsHeight is the bounds of the scene.
	 */
	public MapElementGridSet(int nRows, int nColumns, double boundsX, double boundsY, double boundsWidth, double boundsHeight) {
		super(nRows, nColumns, boundsX, boundsY, boundsWidth, boundsHeight);
	}

	/** Constructor.
	 * @param nRows numbers of rows in the grid
	 * @param nColumns numbers of columns in the grid
	 * @param bounds are the bounds of the scene stored inside this tree.
	 */
	public MapElementGridSet(int nRows, int nColumns, Rectangle2d bounds) {
		super(nRows, nColumns, bounds);
	}

	//-----------------------------------------------------------------
	// Dedicated API
	//----------------------------------------------------------------

	@Override
	public boolean add(P point) {
		if (this.grid.addElement(point)) {
			updateComponentType(point);
			return true;
		}
		return false;
	}

	@Override
	@Pure
	public final P getNearest(Point2D<?, ?> position) {
		final Pair<P, Double> pair = getNearestData(position);
		if (pair != null) {
			return pair.getKey();
		}
		return null;
	}

	@Override
	@Pure
	public final P getNearest(double x, double y) {
		return getNearest(new Point2d(x, y));
	}

	@Override
	@Pure
	public final Pair<P, Double> getNearestData(double x, double y) {
		return getNearestData(new Point2d(x, y));
	}

	@Override
	@Pure
	public Pair<P, Double> getNearestData(Point2D<?, ?> position) {
		final AroundCellIterator<P> iterator = this.grid.getGridCellsAround(
				position,
				Double.POSITIVE_INFINITY).aroundIterator();
		GridCell<P> cell;
		double maxDistance = Double.POSITIVE_INFINITY;
		P nearest = null;
		int level = 1;
		boolean foundInLevel = false;
		while (iterator.hasNext()) {
			cell = iterator.next();
			if (iterator.getLevel() > level) {
				if (!foundInLevel && nearest != null) {
					return new Pair<>(nearest, maxDistance);
				}
				level = iterator.getLevel();
				foundInLevel = false;
			}
			double dist = cell.getBounds().getDistance(position);
			if (dist <= maxDistance) {
				foundInLevel = true;
				for (final P element : cell) {
					dist = element.getDistance(position);
					if (dist <= maxDistance) {
						maxDistance = dist;
						nearest = element;
					}
				}
			}
		}
		if (nearest != null) {
			return new Pair<>(nearest, maxDistance);
		}
		return null;
	}

}
