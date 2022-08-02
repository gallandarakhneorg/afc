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

package org.arakhne.afc.gis.primitive;

import java.io.Serializable;
import java.util.Comparator;
import java.util.UUID;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.gis.location.GeoId;
import org.arakhne.afc.gis.location.GeoLocation;
import org.arakhne.afc.vmutil.json.JsonableObject;

/** A primitive inside a GIS context.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
public interface GISPrimitive extends Serializable, JsonableObject {

	/** Comparator on GISPrimitive.
	 * It uses the geolocation of the elements
	 */
	Comparator<GISPrimitive> COMPARATOR = (GISPrimitive o1, GISPrimitive o2) -> {
		final GeoLocation loc1 = o1.getGeoLocation();
		final GeoLocation loc2 = o2.getGeoLocation();
		return loc1.compareTo(loc2);
	};

	/** Replies the Unique identifier.
	 *
	 * @return the Unique identifier, never <code>null</code>.
	 */
	@Pure
	UUID getUUID();

	/** Replies the geo-location.
	 *
	 * <p>A GeoLocation is unique according to the geo-location of the element.
	 * If two elements have the same points in the same order, or
	 * if two elements have the same points in a reverse order, they
	 * must have equal GeoLocations.
	 *
	 * <p>The following code is always true (where the arguments of the
	 * constructors are the list of points of the polyline). It illustrates
	 * that for two elements with the same geo-localized points, they
	 * have the same geo-location identifier (Geo-Id) and they
	 * have different unique ientifier (Uid):
	 * <pre><code>
	 * GISElement obj1 = new MapPolyline(100,10,200,30,300,4);
	 * GISElement obj2 = new MapPolyline(100,10,200,30,300,4);
	 * assert( obj1.getGeoId().equals(obj2.getGeoId()) );
	 * assert( obj2.getGeoId().equals(obj1.getGeoId()) );
	 * assert( ! obj1.getUid().equals(obj2.getUid()) );
	 * assert( ! obj2.getUid().equals(obj1.getUid()) );
	 * </code></pre>
	 *
	 * @return a location
	 * @see #getGeoId()
	 * @see #getGeoLocation()
	 */
	@Pure
	GeoLocation getGeoLocation();

	/** Replies an unique identifier for primitive.
	 *
	 * <p>A Geo-Id is unique according to the geo-location of the element.
	 * If two elements have the same points in the same order, or
	 * if two elements have the same points in a reverse order, they
	 * must have the same Geo-Id.
	 *
	 * @return an identifier
	 * @see #getGeoLocation()
	 */
	@Pure
	GeoId getGeoId();

}
