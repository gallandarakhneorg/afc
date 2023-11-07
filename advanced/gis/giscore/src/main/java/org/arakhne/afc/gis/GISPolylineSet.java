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

package org.arakhne.afc.gis;

import org.arakhne.afc.gis.mapelement.MapPolyline;
import org.arakhne.afc.gis.primitive.GISPrimitive;
import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.util.OutputParameter;

/**
 * This interface describes a set that contains GIS primitives
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
public interface GISPolylineSet<P extends MapPolyline> extends GISElementSet<P> {

	/** Add the selected polyline inside the tree and replies the two nearest
	 * polylines that are already inside the tree.
	 *
	 * @param polyline is the polyline to add
	 * @param precision is the precision (in meters) used to detect the neighbours.
	 * @param firstNeighbour is one of the two nearest polylines that could be connected to the new segment.
	 * @param secondNeighbour is one of the two nearest polylines that could be connected to the new segment.
	 * @return {@code true} if successfully added, {@code false} otherwise
	 */
	boolean add(P polyline, double precision, OutputParameter<P> firstNeighbour,
			OutputParameter<P> secondNeighbour);

	/** Replies the object that has the nearest end to the specified point.
	 *
	 * @param position is the position from which the nearest primitive must be replied.
	 * @return the nearest element or {@code null} if none.
	 * @see #getNearestEnd(double, double)
	 */
	P getNearestEnd(Point2D<?, ?> position);

	/** Replies the object that has the nearest end to the specified point.
	 *
	 * @param x is the position from which the nearest primitive must be replied.
	 * @param y is the position from which the nearest primitive must be replied.
	 * @return the nearest element or {@code null} if none.
	 */
	P getNearestEnd(double x, double y);

}
