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
public final class GeoLocationNowhere extends AbstractGeoLocation {

	private final UUID uid;

	/** Constructor.
	 * @param objectID is the identifier of the associated object.
	 */
	public GeoLocationNowhere(UUID objectID) {
		this.uid = objectID;
	}

	@Pure
	@Override
	public GeoId toGeoId() {
		return new GeoId(GeoLocationUtil.makeInternalId(this.uid));
	}

	@Pure
	@Override
	@SuppressWarnings("checkstyle:equalshashcode")
	public int hashCode() {
		return this.uid.hashCode();
	}

	@Pure
	@Override
	public Rectangle2d toBounds2D() {
		return new Rectangle2d();
	}

	@Pure
	@Override
	public GeodesicPosition toGeodesicPosition() {
		return null;
	}

}
