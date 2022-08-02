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

package org.arakhne.afc.gis.grid;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.gis.GISPolylineSet;
import org.arakhne.afc.gis.mapelement.MapPolyline;
import org.arakhne.afc.gis.primitive.GISPrimitive;
import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.d.Point2d;
import org.arakhne.afc.math.geometry.d2.d.Rectangle2d;
import org.arakhne.afc.util.OutputParameter;

/**
 * This class describes a grid that contains map polylines
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
public class MapPolylineGridSet<P extends MapPolyline> extends MapElementGridSet<P> implements GISPolylineSet<P> {

	/** Constructor.
	 * @param nRows numbers of rows in the grid
	 * @param nColumns numbers of columns in the grid
	 * @param boundsX is the bounds of the scene.
	 * @param boundsY is the bounds of the scene.
	 * @param boundsWidth is the bounds of the scene.
	 * @param boundsHeight is the bounds of the scene.
	 */
	public MapPolylineGridSet(int nRows, int nColumns, double boundsX, double boundsY, double boundsWidth, double boundsHeight) {
		super(nRows, nColumns, boundsX, boundsY, boundsWidth, boundsHeight);
	}

	/** Constructor.
	 * @param nRows numbers of rows in the grid
	 * @param nColumns numbers of columns in the grid
	 * @param bounds are the bounds of the scene stored inside this tree.
	 */
	public MapPolylineGridSet(int nRows, int nColumns, Rectangle2d bounds) {
		super(nRows, nColumns, bounds);
	}

	//-----------------------------------------------------------------
	// Dedicated API
	//----------------------------------------------------------------

	@Override
	public boolean add(P polyline, double precision,
			OutputParameter<P> firstNeighbour,
			OutputParameter<P> secondNeighbour) {
		if (add(polyline)) {
			Point2d point;

			point = polyline.getPointAt(0);
			if (point != null) {
				firstNeighbour.set(getNearestEnd(polyline, point, precision));
			}

			point = polyline.getPointAt(polyline.getPointCount() - 1);
			if (point != null) {
				secondNeighbour.set(getNearestEnd(polyline, point, precision));
			}

			return true;
		}

		return false;
	}

	@Override
	@Pure
	public final P getNearestEnd(double x, double y) {
		return getNearestEnd(new Point2d(x, y));
	}

	private P getNearestEnd(P exception, Point2D<?, ?> position, double maximalDistance) {
		final AroundCellIterator<P> iterator = this.grid.getGridCellsAround(
				position,
				maximalDistance).aroundIterator();
		GridCell<P> cell;
		double maxDistance = maximalDistance;
		P nearest = null;
		int level = 1;
		boolean foundInLevel = false;
		while (iterator.hasNext()) {
			cell = iterator.next();
			if (iterator.getLevel() > level) {
				if (!foundInLevel && nearest != null) {
					return nearest;
				}
				level = iterator.getLevel();
				foundInLevel = false;
			}
			double dist = cell.getBounds().getDistance(position);
			if (dist <= maxDistance) {
				foundInLevel = true;
				for (final P element : cell) {
					if (exception == null || !exception.equals(element)) {
						dist = element.distanceToEnd(position);
						if (dist <= maxDistance) {
							maxDistance = dist;
							nearest = element;
						}
					}
				}
			}
		}
		return nearest;
	}

	@Override
	@Pure
	public P getNearestEnd(Point2D<?, ?> position) {
		return getNearestEnd(null, position, Double.POSITIVE_INFINITY);
	}

}
