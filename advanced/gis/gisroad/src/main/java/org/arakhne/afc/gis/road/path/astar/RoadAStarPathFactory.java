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

package org.arakhne.afc.gis.road.path.astar;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.gis.road.path.RoadPath;
import org.arakhne.afc.gis.road.path.astar.RoadAStar.VirtualSegment;
import org.arakhne.afc.gis.road.primitive.RoadConnection;
import org.arakhne.afc.gis.road.primitive.RoadSegment;
import org.arakhne.afc.math.graph.astar.AStarPathFactory;

/**
 * This class is the factory used to create a road path
 * during the execution of the A* algorithm.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
public class RoadAStarPathFactory implements AStarPathFactory<RoadPath, RoadSegment, RoadConnection> {

	@Override
	@Pure
	public RoadPath newPath(RoadConnection startPoint, RoadSegment segment) {
		assert startPoint != null;
		assert segment != null;
		if (segment instanceof VirtualSegment) {
			final RoadSegment s = ((VirtualSegment) segment).getVirtualizedSegment();
			return new RoadPath(s);
		}
		return new RoadPath(segment, startPoint);
	}

	@Override
	public boolean addToPath(RoadPath path, RoadSegment segment) {
		assert path != null;
		assert segment != null;
		RoadSegment seg = segment;
		if (segment instanceof VirtualSegment) {
			seg = ((VirtualSegment) segment).getVirtualizedSegment();
			if (seg.equals(path.getLastSegment())) {
				return false;
			}
		}
		return path.add(seg);
	}

}
