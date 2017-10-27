/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2018 The original authors, and other authors.
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

/** Describes a geo-localized feature.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
public final class GeoLocationPointList extends AbstractGeoLocation {

	private final float[] pts;

	private transient int hash;

	private transient Rectangle2d bounds;

	/** Constructor.
	 *
	 * @param points the points.
	 */
	public GeoLocationPointList(double... points) {
		if (points == null || points.length < 2) {
			this.pts = new float[0];
		} else {
			assert (points.length % 2) == 0;
			this.pts = new float[points.length];

			if ((points[0] < points[points.length - 2])
					|| ((points[0] == points[points.length - 2]) && (points[1] <= points[points.length - 1]))) {
				for (int i = 0; i < points.length; ++i) {
					this.pts[i] = GeoLocationUtil.castDistance(points[i]);
				}
			} else {
				for (int i = 0, j = points.length - 2; j >= 0; i += 2, j -= 2) {
					this.pts[i] = GeoLocationUtil.castDistance(points[j]);
					this.pts[i + 1] = GeoLocationUtil.castDistance(points[j + 1]);
				}
			}
		}
	}

	@Override
	@Pure
	public GeoId toGeoId() {
		final Rectangle2d bounds = new Rectangle2d();
		final String id = GeoLocationUtil.makeInternalId(this.pts, bounds);

		if (!bounds.isEmpty()) {
			return new GeoId(
					bounds.getMinX(), bounds.getMinY(),
					bounds.getMaxX(), bounds.getMaxY(),
					id);
		}
		return new GeoId(id);
	}

	@Override
	@SuppressWarnings("checkstyle:equalshashcode")
	@Pure
	public int hashCode() {
		if (this.hash == 0) {
			this.hash = Objects.hash(this.pts);
		}
		return this.hash;
	}

	/** Replies the count of points.
	 *
	 * @return the count of points.
	 */
	@Pure
	public int size() {
		return this.pts.length / 2;
	}

	/** Replies the x-location.
	 *
	 * @param index is the index of the point
	 * @return x
	 */
	@Pure
	public float getX(int index) {
		return this.pts[index * 2];
	}

	/** Replies the y-location.
	 *
	 * @param index is the index of the point
	 * @return y
	 */
	@Pure
	public float getY(int index) {
		return this.pts[index * 2 + 1];
	}

	@Override
	@Pure
	public Rectangle2d toBounds2D() {
		if (this.bounds == null && this.pts != null) {
			double x1 = Double.MAX_VALUE;
			double y1 = Double.MAX_VALUE;
			double x2 = Double.MIN_VALUE;
			double y2 = Double.MIN_VALUE;
			for (int i = 0; i < this.pts.length; i += 2) {
				if (this.pts[i] < x1) {
					x1 = this.pts[i];
				}
				if (this.pts[i] > x2) {
					x2 = this.pts[i];
				}
				if (this.pts[i + 1] < y1) {
					y1 = this.pts[i + 1];
				}
				if (this.pts[i + 1] > y2) {
					y2 = this.pts[i + 1];
				}
			}
			this.bounds = new Rectangle2d(x1, y1, x2 - x1, y2 - y1);
		}
		return this.bounds;
	}

	@Override
	@Pure
	public GeodesicPosition toGeodesicPosition() {
		final Rectangle2d b = toBounds2D().toBoundingBox();
		if (b == null) {
			return null;
		}
		return GISCoordinates.EL2_WSG84(b.getCenterX(), b.getCenterY());
	}

	/** Replies the coordinates of this geo-location.
	 *
	 * @return the coordinates of this geo-location.
	 */
	@Pure
	float[] toArray() {
		return this.pts;
	}

}
