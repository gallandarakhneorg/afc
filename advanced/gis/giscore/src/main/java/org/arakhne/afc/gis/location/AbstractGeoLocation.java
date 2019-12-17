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

package org.arakhne.afc.gis.location;

import java.util.UUID;

import org.eclipse.xtext.xbase.lib.Pure;

/** Abstract implementation of a GeoLocation.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
public abstract class AbstractGeoLocation implements GeoLocation {

	@Pure
	@Override
	public final String toString() {
		return toGeoId().toString();
	}

	@Pure
	@Override
	public final UUID toUUID() {
		return toGeoId().toUUID();
	}

	@Pure
	@Override
	public final int compareTo(GeoLocation location) {
		return GeoLocationUtil.compare(this, location);
	}

	/** Replies if the specified location is equals to this one.
	 */
	@Pure
	@Override
	@SuppressWarnings("checkstyle:equalshashcode")
	public final boolean equals(Object location) {
		if (location instanceof GeoLocation) {
			return GeoLocationUtil.compare(this, (GeoLocation) location) == 0;
		}
		return toGeoId().equals(location);
	}

	@Pure
	@Override
	public abstract int hashCode();

}
