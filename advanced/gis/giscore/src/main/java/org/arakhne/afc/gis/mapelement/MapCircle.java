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

package org.arakhne.afc.gis.mapelement;

import java.util.Objects;
import java.util.UUID;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.attrs.collection.AttributeCollection;
import org.arakhne.afc.math.MathUtil;
import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.Shape2D;
import org.arakhne.afc.math.geometry.d2.afp.Rectangle2afp;
import org.arakhne.afc.math.geometry.d2.afp.Segment2afp;
import org.arakhne.afc.math.geometry.d2.d.Circle2d;
import org.arakhne.afc.math.geometry.d2.d.Rectangle2d;
import org.arakhne.afc.math.geometry.d2.d.Shape2d;
import org.arakhne.afc.vmutil.json.JsonBuffer;

/**
 * This class permits to define a circle.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
public class MapCircle extends MapPonctualElement {

	private static final long serialVersionUID = -8342043070262210048L;

	/** Size of the circle.
	 */
	private double radius;

	/** Create a new map element.
	 *
	 * @param point is a location component of the circle in the geo-referenced space.
	 */
	public MapCircle(Point2D<?, ?> point) {
		this(null, null, point.getX(), point.getY(), MapElementConstants.getPreferredRadius());
	}

	/** Create a new map element.
	 *
	 * @param x is a location component of the circle in the geo-referenced space.
	 * @param y is a location component of the circle in the geo-referenced space.
	 */
	public MapCircle(double x, double y) {
		this(null, null, x, y, MapElementConstants.getPreferredRadius());
	}

	/** Create a new map element.
	 *
	 * @param point is a location component of the circle in the geo-referenced space.
	 * @param radius is the radius
	 */
	public MapCircle(Point2D<?, ?> point, double radius) {
		this(null, null, point.getX(), point.getY(), radius);
	}

	/** Create a new map element.
	 *
	 * @param x is a location component of the circle in the geo-referenced space.
	 * @param y is a location component of the circle in the geo-referenced space.
	 * @param radius is the radius
	 */
	public MapCircle(double x, double y, double radius) {
		this(null, null, x, y, radius);
	}

	/** Create a new map element.
	 *
	 * @param attributeSource is the provider of the attributes for this element.
	 * @param point is a location component of the circle in the geo-referenced space.
	 */
	public MapCircle(AttributeCollection attributeSource, Point2D<?, ?> point) {
		this(null, attributeSource, point.getX(), point.getY(), MapElementConstants.getPreferredRadius());
	}

	/** Create a new map element.
	 *
	 * @param attributeSource is the provider of the attributes for this element.
	 * @param x is a location component of the circle in the geo-referenced space.
	 * @param y is a location component of the circle in the geo-referenced space.
	 */
	public MapCircle(AttributeCollection attributeSource, double x, double y) {
		this(null, attributeSource, x, y, MapElementConstants.getPreferredRadius());
	}

	/** Create a new map element.
	 *
	 * @param attributeSource is the provider of the attributes for this element.
	 * @param point is a location component of the circle in the geo-referenced space.
	 * @param radius is the radius
	 */
	public MapCircle(AttributeCollection attributeSource, Point2D<?, ?> point, double radius) {
		this(null, attributeSource, point.getX(), point.getY(), radius);
	}

	/** Create a new map element.
	 *
	 * @param attributeSource is the provider of the attributes for this element.
	 * @param x is a location component of the circle in the geo-referenced space.
	 * @param y is a location component of the circle in the geo-referenced space.
	 * @param radius is the radius
	 */
	public MapCircle(AttributeCollection attributeSource, double x, double y, double radius) {
		this(null, attributeSource, x, y, radius);
	}

	/** Create a new map element.
	 *
	 * @param id is the unique identifier of this element, or <code>null</code> if unknown.
	 * @param point is a location component of the circle in the geo-referenced space.
	 * @since 4.0
	 */
	public MapCircle(UUID id, Point2D<?, ?> point) {
		this(id, null, point.getX(), point.getY(), MapElementConstants.getPreferredRadius());
	}

	/** Create a new map element.
	 *
	 * @param id is the unique identifier of this element, or <code>null</code> if unknown.
	 * @param x is a location component of the circle in the geo-referenced space.
	 * @param y is a location component of the circle in the geo-referenced space.
	 * @since 4.0
	 */
	public MapCircle(UUID id, double x, double y) {
		this(id, null, x, y, MapElementConstants.getPreferredRadius());
	}

	/** Create a new map element.
	 *
	 * @param id is the unique identifier of this element, or <code>null</code> if unknown.
	 * @param point is a location component of the circle in the geo-referenced space.
	 * @param radius is the radius
	 * @since 4.0
	 */
	public MapCircle(UUID id, Point2D<?, ?> point, double radius) {
		this(id, null, point.getX(), point.getY(), radius);
	}

	/** Create a new map element.
	 *
	 * @param id is the unique identifier of this element, or <code>null</code> if unknown.
	 * @param x is a location component of the circle in the geo-referenced space.
	 * @param y is a location component of the circle in the geo-referenced space.
	 * @param radius is the radius
	 * @since 4.0
	 */
	public MapCircle(UUID id, double x, double y, double radius) {
		this(id, null, x, y, radius);
	}

	/** Create a new map element.
	 *
	 * @param id is the unique identifier of this element, or <code>null</code> if unknown.
	 * @param attributeSource is the provider of the attributes for this element.
	 * @param point is a location component of the circle in the geo-referenced space.
	 * @since 4.0
	 */
	public MapCircle(UUID id, AttributeCollection attributeSource, Point2D<?, ?> point) {
		this(id, attributeSource, point.getX(), point.getY(), MapElementConstants.getPreferredRadius());
	}

	/** Create a new map element.
	 *
	 * @param id is the unique identifier of this element, or <code>null</code> if unknown.
	 * @param attributeSource is the provider of the attributes for this element.
	 * @param x is a location component of the circle in the geo-referenced space.
	 * @param y is a location component of the circle in the geo-referenced space.
	 * @since 4.0
	 */
	public MapCircle(UUID id, AttributeCollection attributeSource, double x, double y) {
		this(id, attributeSource, x, y, MapElementConstants.getPreferredRadius());
	}

	/** Create a new map element.
	 *
	 * @param id is the unique identifier of this element, or <code>null</code> if unknown.
	 * @param attributeSource is the provider of the attributes for this element.
	 * @param point is a location component of the circle in the geo-referenced space.
	 * @param radius is the radius
	 * @since 4.0
	 */
	public MapCircle(UUID id, AttributeCollection attributeSource, Point2D<?, ?> point, double radius) {
		this(id, attributeSource, point.getX(), point.getY(), radius);
	}

	/** Create a new map element.
	 *
	 * @param id is the unique identifier of this element, or <code>null</code> if unknown.
	 * @param attributeSource is the provider of the attributes for this element.
	 * @param x is a location component of the circle in the geo-referenced space.
	 * @param y is a location component of the circle in the geo-referenced space.
	 * @param radius is the radius
	 * @since 4.0
	 */
	public MapCircle(UUID id, AttributeCollection attributeSource, double x, double y, double radius) {
		super(id, attributeSource, x, y);
		this.radius = radius;
	}

	@Override
	@Pure
	public void toJson(JsonBuffer buffer) {
		super.toJson(buffer);
		buffer.add("radius", getRadius()); //$NON-NLS-1$
	}

	/** Replies if the specified objects is the same as this one.
	 */
	@Override
	@SuppressWarnings({"checkstyle:equalshashcode", "checkstyle:covariantequals"})
	@Pure
	public boolean equals(MapElement element) {
		if (super.equals(element)) {
			final MapCircle e = (MapCircle) element;
			return MathUtil.isEpsilonEqual(this.radius, e.radius, MapElementConstants.POINT_FUSION_DISTANCE);
		}
		return false;
	}

	@Override
	@SuppressWarnings({"checkstyle:equalshashcode"})
	@Pure
	public int hashCode() {
		return Objects.hash(super.hashCode(), this.radius);
	}

	/** Replies the radius.
	 *
	 * @return the radius.
	 */
	@Pure
	public double getRadius() {
		return this.radius;
	}

	/** Replies the diameter.
	 *
	 * @return the diameter.
	 */
	@Pure
	public double getDiameter() {
		return this.radius * 2;
	}

	/** Set the radius.
	 *
	 * @param radius the new radius
	 */
	public void setRadius(double radius) {
		this.radius = Math.abs(radius);
		fireShapeChanged();
		fireElementChanged();
	}

	/** Set the diameter.
	 *
	 * @param diameter the new diameter
	 */
	public void setDiameter(double diameter) {
		this.radius = Math.abs(diameter) / 2.;
		fireShapeChanged();
		fireElementChanged();
	}

	/**
	 * Replies the distance between this MapPoint and
	 * <var>another_point</var>.
	 */
	@Override
	@Pure
	public <OT extends MapPonctualElement> double distance(OT another_point) {
		double dist = super.distance(another_point);
		dist -= this.radius;
		return dist;
	}

	/**
	 * Replies the distance between this MapCircle and
	 * point.
	 *
	 * @param point the point to compute the distance to.
	 * @return the distance. Should be negative if the point is inside the circle.
	 */
	@Override
	@Pure
	public double getDistance(Point2D<?, ?> point) {
		double dist = super.getDistance(point);
		dist -= this.radius;
		return dist;
	}

	/**
	 * Replies if this element has an intersection
	 * with the specified rectangle.
	 *
	 * @return <code>true</code> if this MapElement is intersecting the specified area,
	 *     otherwise <code>false</code>
	 */
	@Override
	@Pure
	public boolean intersects(Shape2D<?, ?, ?, ?, ?, ? extends Rectangle2afp<?, ?, ?, ?, ?, ?>> rectangle) {
		if (boundsIntersects(rectangle)) {
			final double x = getX();
			final double y = getY();
			final Rectangle2afp<?, ?, ?, ?, ?, ?> box = rectangle.toBoundingBox();
			final double minx = box.getMinX();
			final double miny = box.getMinY();
			final double maxx = box.getMaxX();
			final double maxy = box.getMaxY();

			return MathUtil.min(
					Segment2afp.calculatesDistanceSegmentPoint(minx, miny, maxx, miny, x, y),
					Segment2afp.calculatesDistanceSegmentPoint(minx, miny, minx, maxy, x, y),
					Segment2afp.calculatesDistanceSegmentPoint(maxx, miny, maxx, maxy, x, y),
					Segment2afp.calculatesDistanceSegmentPoint(minx, maxy, maxx, maxy, x, y)) <= this.radius;
		}
		return false;
	}

	/** Compute the bounds of this element.
	 * This function does not update the internal
	 * attribute replied by {@link #getBoundingBox()}
	 */
	@Override
	@Pure
	protected Rectangle2d calcBounds() {
		double x = this.getX();
		double y = this.getY();

		final double w = this.radius * 2;
		final double h = w;

		x -= w / 2.;
		y -= h / 2.;

		return new Rectangle2d(x, y, w, h);
	}

	@Override
	public Shape2d<?> getShape() {
		return new Circle2d(getX(), getY(), getRadius());
	}

}
