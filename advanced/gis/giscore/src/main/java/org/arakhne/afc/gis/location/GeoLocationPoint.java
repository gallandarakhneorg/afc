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

import java.util.Objects;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.gis.coordinate.GISCoordinates;
import org.arakhne.afc.gis.coordinate.GeodesicPosition;
import org.arakhne.afc.math.geometry.d2.d.Point2d;
import org.arakhne.afc.math.geometry.d2.d.Rectangle2d;

/** Describes a geo-localized feature.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
public final class GeoLocationPoint extends AbstractGeoLocation {

	private final float x;

	private final float y;

	/** Constructor.
	 * @param x x coordinate.
	 * @param y y coordinate.
	 */
	public GeoLocationPoint(double x, double y) {
		this.x = GeoLocationUtil.castDistance(x);
		this.y = GeoLocationUtil.castDistance(y);
	}

	@Override
	@Pure
	public GeoId toGeoId() {
		return new GeoId(
				this.x, this.y, this.x, this.y,
				GeoLocationUtil.makeInternalId(this.x, this.y));
	}

	@Override
	@SuppressWarnings("checkstyle:equalshashcode")
	@Pure
	public int hashCode() {
		return Objects.hash(this.x, this.y);
	}

	/** Replies the X-coordinate.
	 *
	 * @return x
	 */
	@Pure
	public float getX() {
		return this.x;
	}

	/** Replies the Y-coordinate.
	 *
	 * @return y
	 */
	@Pure
	public float getY() {
		return this.y;
	}

	/** Replies the point.
	 *
	 * @return the point
	 */
	@Pure
	public Point2d getPoint() {
		return new Point2d(this.x, this.y);
	}

	@Pure
	@Override
	public Rectangle2d toBounds2D() {
		return new Rectangle2d(
				this.x - GeoLocationUtil.GIS_POINT_DEMI_SIZE,
				this.y - GeoLocationUtil.GIS_POINT_DEMI_SIZE,
				GeoLocationUtil.GIS_POINT_SIZE,
				GeoLocationUtil.GIS_POINT_SIZE);
	}

	@Pure
	@Override
	public GeodesicPosition toGeodesicPosition() {
		return GISCoordinates.EL2_WGS84(this.x, this.y);
	}

}
