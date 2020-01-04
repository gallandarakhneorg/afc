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

package org.arakhne.afc.gis.location;

import java.util.UUID;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.gis.coordinate.GeodesicPosition;
import org.arakhne.afc.math.geometry.d2.d.Rectangle2d;

/** Describes a geo-localized feature.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
public interface GeoLocation extends Comparable<GeoLocation> {

	/** Replies the string representation of the geo-location.
	 *
	 * @return the string representation of this location
	 */
	@Pure
	@Override
	String toString();

	/** Replies the GeoId representation of the geo-location.
	 *
	 * @return the GeoId representation of this location
	 */
	@Pure
	GeoId toGeoId();

	/** Replies the UUID of the geo-location.
	 *
	 * @return the UUID of this location
	 */
	@Pure
	UUID toUUID();

	/** Replies if the specified location is equals to this one.
	 *
	 * @param location the location to test.
	 * @return <code>true</code> if the given location is equal to this location,
	 *     otherwise <code>false</code>
	 */
	@Pure
	@Override
	boolean equals(Object location);

	/** Replies the area cover by this location.
	 *
	 * @return the bounds
	 */
	@Pure
	Rectangle2d toBounds2D();

	/** Replies the WSG84 (GPS) representation of this point.
	 *
	 * <p>If this GeoLocation is not a point, the implementation
	 * class should provides a significant GPS point.
	 *
	 * @return the WSG84 (GPS) representation of this point; or
	 * <code>null</code> if the GPS point could not be computed.
	 */
	@Pure
	GeodesicPosition toGeodesicPosition();

}
