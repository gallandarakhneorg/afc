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

package org.arakhne.afc.gis.mapelement;

import java.util.UUID;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.attrs.collection.AttributeCollection;
import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.Shape2D;
import org.arakhne.afc.math.geometry.d2.afp.Rectangle2afp;
import org.arakhne.afc.math.geometry.d2.afp.Segment2afp;
import org.arakhne.afc.math.geometry.d2.d.Path2d;
import org.arakhne.afc.math.geometry.d2.d.Point2d;
import org.arakhne.afc.math.geometry.d2.d.Shape2d;

/**
 * Class the permits to display a polygon.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
public class MapPolygon extends MapComposedElement {

	private static final long serialVersionUID = 2544090957795621479L;

	/** Create a new map element.
	 */
	public MapPolygon() {
		super(null, null);
	}

	/** Create a new map element.
	 *
	 * @param attributeSource is the source of the attributes for this map element.
	 */
	public MapPolygon(AttributeCollection attributeSource) {
		super(null, attributeSource);
	}

	/** Create a new map element.
	 *
	 * @param id is the unique identifier of this element, or {@code null} if unknown.
	 * @since 4.0
	 */
	public MapPolygon(UUID id) {
		super(id, null);
	}

	/** Create a new map element.
	 *
	 * @param id is the unique identifier of this element, or {@code null} if unknown.
	 * @param attributeSource is the source of the attributes for this map element.
	 * @since 4.0
	 */
	public MapPolygon(UUID id, AttributeCollection attributeSource) {
		super(id, attributeSource);
	}

	/**
	 * Replies the distance between this MapElement and
	 * point.
	 *
	 * @return the distance. Should be negative depending of the MapElement type.
	 */
	@Override
	@Pure
	public final double getDistance(Point2D<?, ?> point) {
		return distance(point, 0);
	}

	/** Replies the distance between this figure and the specified point.
	 *
	 * @param point is the x-coordinate of the point.
	 * @param width is the width of the polygon.
	 * @return the computed distance
	 */
	@Pure
	public double distance(Point2D<?, ?> point, double width) {
		double mind = Double.MAX_VALUE;
		double dist;
		final Point2d pts1 = new Point2d();
		final Point2d pts2 = new Point2d();

		double w = width;
		w = Math.abs(w) / 2.;

		for (final PointGroup grp : groups()) {
			for (int idx = 0; idx < grp.size(); idx += 2) {
				if (grp.getMany(idx, pts1, pts2) > 1) {
					dist = Segment2afp.calculatesDistanceSegmentPoint(
							pts1.getX(), pts1.getY(), pts2.getX(), pts2.getY(),
							point.getX(), point.getY()) - w;
					if (dist < mind) {
						mind = dist;
					}
				} else {
					dist = pts1.getDistance(point);
					if (dist < mind) {
						mind = dist;
					}
				}
			}
		}

		return mind;
	}

	/**
	 * Replies if this element has an intersection
	 * with the specified rectangle.
	 *
	 * @return {@code true} if this MapElement is intersecting the specified area,
	 *     otherwise {@code false}
	 */
	@Override
	@Pure
	public boolean intersects(Shape2D<?, ?, ?, ?, ?, ? extends Rectangle2afp<?, ?, ?, ?, ?, ?>> rectangle) {
		if (boundsIntersects(rectangle)) {
			final Path2d p = toPath2D();
			return p.intersects(rectangle);
		}
		return false;
	}

	@Override
	public Shape2d<?> getShape() {
		return toPath2D();
	}

	/**
	 * Replies the Path2D that corresponds to this polygon.
	 *
	 * @return {@code true} if this MapElement is intersecting the specified area,
	 *     otherwise {@code false}
	 */
	@Pure
	public final Path2d toPath2D() {
		// this is the path to draw
		final Path2d path = new Path2d();

		// loop on parts and build the path to draw
		boolean firstPoint;
		for (final PointGroup grp : groups()) {
			firstPoint = true;
			for (final Point2d pts : grp) {
				final double x = pts.getX();
				final double y = pts.getY();
				if (firstPoint) {
					path.moveTo(x, y);
					firstPoint = false;
				} else {
					path.lineTo(x, y);
				}
			}
		}

		path.closePath();

		return path;
	}

}
