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
import org.arakhne.afc.gis.location.GeoLocationUtil;
import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.Shape2D;
import org.arakhne.afc.math.geometry.d2.afp.Rectangle2afp;
import org.arakhne.afc.math.geometry.d2.d.Path2d;
import org.arakhne.afc.math.geometry.d2.d.Point2d;
import org.arakhne.afc.math.geometry.d2.d.Shape2d;
import org.arakhne.afc.vmutil.json.JsonBuffer;

/**
 * Class the permits to display a multi-point.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
public class MapMultiPoint extends MapComposedElement {

	private static final long serialVersionUID = 1449769206238650080L;

	/** Size of the points in pixels.
	 */
	private double pointSize = GeoLocationUtil.GIS_POINT_SIZE;

	/** Must the point be drawn with a double frame.
	 */
	private boolean doubleFramed;

	/** Create a new map element.
	 *
	 * @param attributeSource is the source of the attributes for this map element.
	 */
	public MapMultiPoint(AttributeCollection attributeSource) {
		super(null, attributeSource);
	}

	/** Create a new map element.
	 */
	public MapMultiPoint() {
		super(null, null);
	}

	/** Create a new map element.
	 *
	 * @param id is the unique identifier of this element, or {@code null} if unknown.
	 * @param attributeSource is the source of the attributes for this map element.
	 * @since 4.0
	 */
	public MapMultiPoint(UUID id, AttributeCollection attributeSource) {
		super(id, attributeSource);
	}

	/** Create a new map element.
	 *
	 * @param id is the unique identifier of this element, or {@code null} if unknown.
	 * @since 4.0
	 */
	public MapMultiPoint(UUID id) {
		super(id, null);
	}

	@Override
	@Pure
	public void toJson(JsonBuffer buffer) {
		super.toJson(buffer);
		buffer.add("doubleFramed", isDoubleFramed()); //$NON-NLS-1$
		buffer.add("pointSize", getPointSize()); //$NON-NLS-1$
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
			for (final PointGroup grp : groups()) {
				for (final Point2d pts : grp) {
					if (rectangle.contains(pts)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * Replies the distance between this MapElement and
	 * point.
	 *
	 * @param point the point to compute the distance to.
	 * @return the distance. Should be negative depending of the MapElement type.
	 */
	@Override
	@Pure
	public double getDistance(Point2D<?, ?> point) {
		double mind = Double.MAX_VALUE;
		for (final Point2d p : points()) {
			final double d = p.getDistance(point);
			if (d < mind) {
				mind = d;
			}
		}
		return mind;
	}

	/** Set the size of this point in pixels.
	 *
	 * @param sizeInPixel the new size.
	 */
	public void setPointSize(double sizeInPixel) {
		this.pointSize = sizeInPixel;
		fireShapeChanged();
		fireElementChanged();
	}

	/** Replies the size of this point in pixels.
	 *
	 * @return the size of this point in pixels.
	 */
	@Pure
	public double getPointSize() {
		return this.pointSize;
	}

	/** Set if this point must be drawn with a
	 * double frame.
	 *
	 * @param doubleFramed is {@code true} if the points must have
	 *     a double frame, otherwise {@code false}
	 */
	public void setDoubleFramed(boolean doubleFramed) {
		this.doubleFramed = doubleFramed;
		fireShapeChanged();
		fireElementChanged();
	}

	/** Replies if this point is dfouble framed.
	 *
	 * @return {@code true} if the points must have
	 *     a double frame, otherwise {@code false}
	 */
	@Pure
	public boolean isDoubleFramed() {
		return this.doubleFramed;
	}

	@Override
	public Shape2d<?> getShape() {
		final Path2d path = new Path2d();
		for (final Point2d pts : points()) {
			updateShape(path, pts);
		}
		return null;
	}

	private void updateShape(Path2d path, Point2d pts) {
		double x = pts.getX();
		double y = pts.getY();

		final double w = this.doubleFramed ? this.pointSize * 2 : this.pointSize;
		final double h = w;

		x -= w / 2.;
		y -= h / 2.;

		path.moveTo(x, y);
		path.lineTo(x + w, y);
		path.lineTo(x + w, y + h);
		path.lineTo(x, y + h);
		path.closePath();
	}

}
