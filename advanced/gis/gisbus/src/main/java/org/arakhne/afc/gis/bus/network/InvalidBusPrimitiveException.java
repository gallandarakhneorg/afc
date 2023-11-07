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

package org.arakhne.afc.gis.bus.network;

/**
 * A bus itinerary is not valid.
 *
 * <p>An itinerary could be invalid if one of the following critera is not true:
 * <ol>
 * <li>an itinerary must contains one road segment,</li>
 * <li>all the road segment are connected in a sequence,</li>
 * <li>the count of bus stops must be at least 2,</li>
 * <li>the first stop must be a starting bus stop,</li>
 * <li>the last stop must be of terminus,</li>
 * <li>the other bus stops must not be of type starting bus stop nor terminus,</li>
 * <li>each bus stop must be located on a road segment of the itinerary,</li>
 * <li>the bus stops must be ordered, no cycle nor go-back is allowed.</li>
 * </ol>
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
public class InvalidBusPrimitiveException extends IllegalStateException {

	private static final long serialVersionUID = 4225888425351456084L;

	/** Constructor.
	 */
	public InvalidBusPrimitiveException() {
		//
	}

}
