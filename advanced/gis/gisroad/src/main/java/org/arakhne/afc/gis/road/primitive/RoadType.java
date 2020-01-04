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

package org.arakhne.afc.gis.road.primitive;

/**
 * Standard types of roads.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
public enum RoadType {

	/** Pedetrian or other roads.
	 */
	OTHER,

	/** Privacy access or path.
	 */
	PRIVACY_PATH,

	/** Path or rough road which is made of soil
	 * rather than having a surface covered with
	 * stone or other material.
	 */
	TRACK,

	/** Road for cycles.
	 */
	BIKEWAY,

	/** Local road of minor of high importance. Local connecting road.
	 */
	LOCAL_ROAD,

	/** Interchange ramp.
	 */
	INTERCHANGE_RAMP,

	/** Major urban axis.
	 */
	MAJOR_URBAN_AXIS,

	/** Secondrary roads.
	 */
	SECONDARY_ROAD,

	/** Major roads which are not {@link #FREEWAY}.
	 */
	MAJOR_ROAD,

	/** A wide road built for fast moving
	 * traffic travelling long distances.
	 * Includes freeway, motorway, highway.
	 */
	FREEWAY,

}
