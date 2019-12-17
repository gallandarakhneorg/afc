/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2019 The original authors, and other authors.
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

package org.arakhne.afc.gis.road.ui.drawers;

/** Constants for the road network drawers.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 15.0
 */
public final class RoadNetworkDrawerConstants {

	/** Road borders should be drawn.
	 */
	public static final int DRAWING_STATE_ROAD_BORDERS = 0;

	/** Standard road interior should be drawn.
	 */
	public static final int DRAWING_STATE_ROAD_INTERIOR = 1;

	/** Standard details should be drawn.
	 */
	public static final int DRAWING_STATE_ROAD_DETAILS = 2;

	/** Opacity for drawing the lane lines.
	 */
	public static final double LANE_LINE_OPACITY = .25;

	private RoadNetworkDrawerConstants() {
		//
	}

}
