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

package org.arakhne.afc.gis.location;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.geometry.d2.d.Rectangle2d;

/** An unique identifier of a Geo-referenced element.
 *
 * <p>A geoId is an identifier from which the localization could be extracted.
 * The location is here restricted to the bounds of the primitives.
 *
 * <p>A Geo-Id is unique according to the geo-location of the element.
 * If two elements have the same points in the same order, or
 * if two elements have the same points in a reverse order, they
 * must have the same Geo-Id.
 *
 * <p>This class calls {@code {@link GeoLocation}.{@link GeoLocation#toGeoId()}}.
 *
 * <p>The following code is always true (where the arguments of the
 * constructors are the list of points of the polyline). It illustrates
 * that for two elements with the same geo-localized points, they
 * have the same geo-location identifier (Geo-Id) and they
 * have different unique ientifier (Uid):
 * <pre>{@code 
 * GISElement obj1 = new MapPolyline(100,10,200,30,300,4);
 * GISElement obj2 = new MapPolyline(100,10,200,30,300,4);
 * assert( obj1.getGeoId().equals(obj2.getGeoId()) );
 * assert( obj2.getGeoId().equals(obj1.getGeoId()) );
 * assert( ! obj1.getUid().equals(obj2.getUid()) );
 * assert( ! obj2.getUid().equals(obj1.getUid()) );
 * }</pre>
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
public final class GeoId implements Serializable, Comparable<GeoId> {

	private static final long serialVersionUID = 5402736192573807251L;

	private final String id;

	/** Constructor.
	 * @param id is a unique identifier.
	 */
	GeoId(String id) {
		this.id = id;
	}

	/** Constructor.
	 * @param lowerx is the lowest x-coordinate of the bounds.
	 * @param lowery is the lowest y-coordinate of the bounds.
	 * @param upperx is the uppest x-coordinate of the bounds.
	 * @param uppery is the uppest y-coordinate of the bounds.
	 * @param id is a unique identifier.
	 */
	GeoId(double lowerx, double lowery, double upperx, double uppery, String id) {
		final StringBuilder geoid = new StringBuilder(id);
		geoid.append('#');
		geoid.append((long) Math.floor(lowerx));
		geoid.append(';');
		geoid.append((long) Math.floor(lowery));
		geoid.append(';');
		geoid.append((long) Math.ceil(upperx));
		geoid.append(';');
		geoid.append((long) Math.ceil(uppery));
		this.id = geoid.toString();
	}

	/** Parse the given string to extract a GeoId.
	 *
	 * <p>This function never fails but may replied a geoid
	 * without bounds.
	 *
	 * @param geoId the identifier.
	 * @return a geoid instance.
	 */
	@Pure
	public static GeoId valueOf(String geoId) {
		return new GeoId(geoId);
	}

	@Override
	@Pure
	public String toString() {
		return this.id;
	}

	/**
	 * Replies the UUID representation of the geo-reference identifier.
	 *
	 * @return the UUID representation of the geo-reference identifier.
	 */
	@Pure
	public UUID toUUID() {
		return UUID.nameUUIDFromBytes(this.id.getBytes());
	}

	@Pure
	@Override
	public int compareTo(GeoId obj) {
		if (obj == null) {
			return 1;
		}
		return this.id.compareTo(obj.id);
	}

	@Pure
	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj instanceof GeoId) {
			return this.id.equals(((GeoId) obj).id);
		}
		if (obj instanceof UUID) {
			return toUUID().equals(obj);
		}
		if (obj instanceof GeoLocation) {
			return this.id.equals(((GeoLocation) obj).toGeoId().id);
		}
		return this.id.equals(obj.toString());
	}

	@Pure
	@Override
	public int hashCode() {
		return Objects.hashCode(this.id);
	}

	/** Extract the primitive bounds from this geoId.
	 *
	 * <p>A geoId is an identifier from which the localization could be extracted.
	 * The location is here restricted to the bounds of the primitives.
	 *
	 * @return a rectangle or {@code null} if invalid geoid.
	 */
	@Pure
	public Rectangle2d toBounds2D() {
		int startIndex = this.id.indexOf('#');
		if (startIndex <= 0) {
			return null;
		}

		try {
			int endIndex = this.id.indexOf(';', startIndex);
			if (endIndex <= startIndex) {
				return null;
			}
			final long minx = Long.parseLong(this.id.substring(startIndex + 1, endIndex));

			startIndex = endIndex + 1;
			endIndex = this.id.indexOf(';', startIndex);
			if (endIndex <= startIndex) {
				return null;
			}
			final long miny = Long.parseLong(this.id.substring(startIndex, endIndex));

			startIndex = endIndex + 1;
			endIndex = this.id.indexOf(';', startIndex);
			if (endIndex <= startIndex) {
				return null;
			}
			final long maxx = Long.parseLong(this.id.substring(startIndex, endIndex));

			startIndex = endIndex + 1;
			final long maxy = Long.parseLong(this.id.substring(startIndex));

			final Rectangle2d r = new Rectangle2d();
			r.setFromCorners(minx, miny, maxx, maxy);
			return r;
		} catch (Throwable exception) {
			//
		}

		return null;
	}

	/** Extract the unique identifier stored in this geoId.
	 *
	 * @return the internal identifier or {@code null} if this
	 *     geoid has invalid format.
	 */
	@Pure
	public String getInternalId() {
		final int endIndex = this.id.indexOf('#');
		if (endIndex <= 0) {
			return null;
		}
		return this.id.substring(0, endIndex);
	}

}
