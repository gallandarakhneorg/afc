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

package org.arakhne.afc.gis.road.path.astar;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.gis.road.primitive.RoadConnection;
import org.arakhne.afc.math.geometry.d2.d.Point2d;
import org.arakhne.afc.math.graph.astar.AStarHeuristic;

/** This class implements a euclidian distance heuristic
 * for the {@link RoadAStar A* algorithm}.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
public class RoadAStarDistanceHeuristic implements AStarHeuristic<RoadConnection> {

	@Override
	@Pure
	public double evaluate(RoadConnection p1, RoadConnection p2) {
		assert p1 != null && p2 != null;
		final Point2d position1 = p1.getWrappedRoadConnection().getPoint();
		final Point2d position2 = p2.getWrappedRoadConnection().getPoint();
		assert position1 != null && position2 != null;
		return position1.getDistance(position2);
	}

}
