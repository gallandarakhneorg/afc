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

package org.arakhne.afc.gis.road.primitive;

import org.eclipse.xtext.xbase.lib.Pure;

/**
 * Direction of the traffic on a road segment.
 * The traffic direction is given according
 * to the geometrical direction of the road segment.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
public enum TrafficDirection {

	/** The road segment is traversable from
	 * the first point to the last, and not
	 * from the last to the first point.
	 */
	ONE_WAY,

	/** The road segment is not traversable from
	 * the first point to the last, but is
	 * traversable from the last to the first point.
	 */
	NO_ENTRY,

	/** The road segment is traversable in
	 * both directions.
	 */
	DOUBLE_WAY,

	/** The road segment is not traversable in
	 * both directions.
	 */
	NO_WAY;

	/** Replies the traffic direction which is corresponding
	 * to the given string value. The value is matched
	 * according to {@link RoadNetworkConstants#getPreferredAttributeValueForTrafficDirection(TrafficDirection, int)}.
	 * @param value the value to parse.
	 * @return the traffic direction if matched, otherwise <code>null</code>
	 *     if the given value does not match any constant.
	 */
	@Pure
	public static TrafficDirection fromString(String value) {
		String constant;
		int i;
		for (final TrafficDirection d : TrafficDirection.values()) {
			constant = null;
			i = 0;
			do {
				constant = RoadNetworkConstants.getPreferredAttributeValueForTrafficDirection(d, i);
				++i;
			}
			while (constant != null && !constant.equalsIgnoreCase(value));
			if (constant != null) {
				return d;
			}
		}
		return null;
	}

}
