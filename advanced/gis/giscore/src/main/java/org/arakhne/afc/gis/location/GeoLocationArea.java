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

import java.util.Objects;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.gis.coordinate.GISCoordinates;
import org.arakhne.afc.gis.coordinate.GeodesicPosition;
import org.arakhne.afc.math.geometry.d2.d.Rectangle2d;
import org.arakhne.afc.math.geometry.d2.d.Shape2d;

/** Describes a geo-localized feature.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
public final class GeoLocationArea extends AbstractGeoLocation {

	private final float x1;

	private final float y1;

	private final float x2;

	private final float y2;

	/** Constructor.
	 * @param x1 x coordinate of the first area corner.
	 * @param y1 y coordinate of the first area corner.
	 * @param x2 x coordinate of the second area corner.
	 * @param y2 y coordinate of the second area corner.
	 */
	public GeoLocationArea(double x1, double y1, double x2, double y2) {
		this.x1 = GeoLocationUtil.castDistance(Math.min(x1, x2));
		this.y1 = GeoLocationUtil.castDistance(Math.min(y1, y2));
		this.x2 = GeoLocationUtil.castDistance(Math.max(x1, x2));
		this.y2 = GeoLocationUtil.castDistance(Math.max(y1, y2));
	}

	/** Constructor.
	 * @param area the area specification.
	 */
	public GeoLocationArea(Shape2d<?> area) {
		assert area != null;
		final Rectangle2d box = area.toBoundingBox();
		this.x1 = GeoLocationUtil.castDistance(box.getMinX());
		this.y1 = GeoLocationUtil.castDistance(box.getMinY());
		this.x2 = GeoLocationUtil.castDistance(box.getMaxX());
		this.y2 = GeoLocationUtil.castDistance(box.getMaxY());
	}

	@Pure
	@Override
	public GeoId toGeoId() {
		return new GeoId(
				this.x1, this.y1, this.x2, this.y2,
				GeoLocationUtil.makeInternalId(this.x1, this.y1, this.x2, this.y2));
	}

	@Pure
	@Override
	@SuppressWarnings("checkstyle:equalshashcode")
	public int hashCode() {
		return Objects.hash(this.x1, this.y1, this.x2, this.y2);
	}

	/** Replies the x coordinate of the lower-left corner.
	 *
	 * @return the minimum x
	 */
	@Pure
	public float getMinX() {
		return this.x1;
	}

	/** Replies the y coordinate of the lower-left corner.
	 *
	 * @return the minimum y
	 */
	@Pure
	public float getMinY() {
		return this.y1;
	}

	/** Replies the x coordinate of the lower-left corner.
	 *
	 * @return the minimum x
	 */
	@Pure
	public float getX() {
		return this.x1;
	}

	/** Replies the y coordinate of the lower-left corner.
	 *
	 * @return the minimum y
	 */
	@Pure
	public float getY() {
		return this.y1;
	}

	/** Replies the x coordinate of the upper-right corner.
	 *
	 * @return the maximum x
	 */
	@Pure
	public float getMaxX() {
		return this.x2;
	}

	/** Replies the y coordinate of the upper-right corner.
	 *
	 * @return the maximum y
	 */
	@Pure
	public float getMaxY() {
		return this.y2;
	}

	/** Replies the width of the area.
	 *
	 * @return the width
	 */
	@Pure
	public float getWidth() {
		return this.x2 - this.x1;
	}

	/** Replies the height of the area.
	 *
	 * @return the height
	 */
	@Pure
	public float getHeight() {
		return this.y2 - this.y1;
	}

	@Pure
	@Override
	public Rectangle2d toBounds2D() {
		final Rectangle2d r = new Rectangle2d();
		r.setFromCorners(this.x1, this.y1, this.x2, this.y2);
		return r;
	}

	@Pure
	@Override
	public GeodesicPosition toGeodesicPosition() {
		return GISCoordinates.EL2_WSG84(
				(this.x1 + this.x2) / 2.,
				(this.y1 + this.y2) / 2.);
	}

}
