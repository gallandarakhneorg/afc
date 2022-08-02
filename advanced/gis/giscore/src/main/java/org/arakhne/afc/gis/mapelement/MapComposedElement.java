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

package org.arakhne.afc.gis.mapelement;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.UUID;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.attrs.collection.AttributeCollection;
import org.arakhne.afc.gis.location.GeoLocation;
import org.arakhne.afc.gis.location.GeoLocationPointList;
import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.d.Point2d;
import org.arakhne.afc.math.geometry.d2.d.Rectangle2d;
import org.arakhne.afc.vmutil.json.JsonBuffer;

/** Abstract class that contains a set of grouped points (aka. groups).
 * This class permits to implement polylines and polygons...
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
public abstract class MapComposedElement extends MapElement {

	private static final long serialVersionUID = -6302802665318165511L;

	private static final Validator DEFAULT_VALIDATOR = new Validator();

	/**
	 * List of all the points.
	 * The x-coordinates are at the odd indexes, the y-coordinates
	 * are at the even ones.
	 */
	private double[] pointCoordinates;

	/** List of part's starting points. Except for the first part
	 * for with the starting index is always 0.
	 */
	private int[] partIndexes;

	/** Create a new map element.
	 *
	 * @param id is the unique identifier of this element, or <code>null</code> if unknown.
	 * @param attributeSource is the source of the attributes for this map element.
	 * @since 4.0
	 */
	public MapComposedElement(UUID id, AttributeCollection attributeSource) {
		super(id, attributeSource);
	}

	@Override
	@Pure
	public void toJson(JsonBuffer buffer) {
		super.toJson(buffer);
		final JsonBuffer groups = new JsonBuffer();
		int grpIdx = 0;
		for (final PointGroup grp : groups()) {
			final JsonBuffer group = new JsonBuffer();
			group.add("index", grpIdx); //$NON-NLS-1$
			groups.add("points", grp.points()); //$NON-NLS-1$
			++grpIdx;
		}
		buffer.add("groups", groups); //$NON-NLS-1$
	}

	/** Clone this object to obtain a valid copy.
	 *
	 * @return a copy
	 */
	@Override
	@Pure
	public MapComposedElement clone() {
		final MapComposedElement element = (MapComposedElement) super.clone();
		if (this.partIndexes == null) {
			element.partIndexes = null;
		} else {
			element.partIndexes = this.partIndexes.clone();
		}
		if (this.pointCoordinates == null) {
			element.pointCoordinates = null;
		} else {
			element.pointCoordinates = this.pointCoordinates.clone();
		}
		return element;
	}

	/** Replies if the specified objects is the same as this one.
	 *
	 * <p>If the specified object is a MapComposedElement, these points
	 * are matched to the points of this object.
	 * If the specified object is not a MapcomposedElement, this
	 * function replies <code>false</code>
	 */
	@Override
	@SuppressWarnings({"checkstyle:equalshashcode", "checkstyle:covariantequals"})
	@Pure
	public boolean equals(MapElement element) {
		if (element == this) {
			return true;
		}
		if (element instanceof MapComposedElement) {
			final MapComposedElement e = (MapComposedElement) element;
			final int ptsCount = getPointCount();
			if (ptsCount != e.getPointCount()) {
				return false;
			}
			for (int i = 0; i < ptsCount; ++i) {
				final Point2d p1 = getPointAt(i);
				final Point2d p2 = e.getPointAt(i);
				if (!p1.epsilonEquals(p2, MapElementConstants.POINT_FUSION_DISTANCE)) {
					return false;
				}
			}
			int grpCount = getGroupCount();
			if (grpCount != e.getGroupCount()) {
				return false;
			}
			// Because the first group was not inside the array
			--grpCount;
			for (int i = 0; i < grpCount; ++i) {
				final int idx1 = this.partIndexes[i];
				final int idx2 = e.partIndexes[i];
				if (idx1 != idx2) {
					return false;
				}
			}
			return true;
		}
		return false;
	}

	@Override
	@SuppressWarnings({"checkstyle:equalshashcode"})
	@Pure
	public int hashCode() {
		return Objects.hash(this.partIndexes, this.pointCoordinates);
	}

	/**
	 * Replies if the specified point (<var>x</var>,<var>y</var>)
	 * was inside the figure of this MapElement.
	 *
	 * @param point is a geo-referenced coordinate
	 * @param delta is the geo-referenced distance that corresponds to a approximation
	 *     distance in the screen coordinate system
	 * @return <code>true</code> if the specified point has a distance nearest than delta
	 *     to this element, otherwise <code>false</code>
	 */
	@Override
	@Pure
	public boolean contains(Point2D<?, ?> point, double delta) {
		return getDistance(point) <= delta;
	}

	/** Compute the bounds of this element.
	 * This function does not update the internal
	 * attribute replied by {@link #getBoundingBox()}
	 */
	@Override
	@Pure
	protected Rectangle2d calcBounds() {
		if (getPointCount() == 0) {
			return null;
		}

		final Iterator<Point2d> iterPoints = points().iterator();
		if (!iterPoints.hasNext()) {
			return null;
		}

		Point2d point = iterPoints.next();
		if (point == null) {
			return null;
		}

		double minx = point.getX();
		double maxx = point.getX();
		double miny = point.getY();
		double maxy = point.getY();

		while (iterPoints.hasNext()) {
			point = iterPoints.next();
			if (point != null) {
				final double x = point.getX();
				final double y = point.getY();
				if (x < minx) {
					minx = x;
				}
				if (y < miny) {
					miny = y;
				}
				if (x > maxx) {
					maxx = x;
				}
				if (y > maxy) {
					maxy = y;
				}
			}
		}
		final Rectangle2d r = new Rectangle2d();
		r.setFromCorners(minx, miny, maxx, maxy);
		return r;
	}

	@Override
	@Pure
	public GeoLocation getGeoLocation() {
		return new GeoLocationPointList(this.pointCoordinates);
	}

	/** Remove all the points.
	 */
	public void clear() {
		this.pointCoordinates = null;
		this.partIndexes = null;
		resetBoundingBox();
		fireShapeChanged();
		fireElementChanged();
	}

	/** Replies the count of groups.
	 *
	 * @return the count of groups.
	 */
	@Pure
	public int getGroupCount() {
		if (this.pointCoordinates == null) {
			return 0;
		}
		return this.partIndexes == null ? 1 : this.partIndexes.length + 1;
	}

	/** Replies the count of points in all the parts.
	 *
	 * @return the count of points
	 */
	@Pure
	public int getPointCount() {
		return this.pointCoordinates == null ? 0 : this.pointCoordinates.length / 2;
	}


	/** Check if point p is contained in this coordinates collections.
	 *
	 * @param point the point to compare with this coordinates
	 * @return true if p is already part of coordinates
	 */
	@Pure
	public boolean containsPoint(Point2D<?, ?> point) {
		if (point == null) {
			return false;
		}
		for (int i = 0; i < getPointCount(); ++i) {
			final Point2d cur = getPointAt(i);
			if (cur.epsilonEquals(point, MapElementConstants.POINT_FUSION_DISTANCE)) {
				return true;
			}
		}
		return false;
	}

	/** Check if point p is contained in this coordinates collections.
	 *
	 * @param point the point to compare with this coordinates
	 * @param groupIndex into look for
	 * @return true if p is already part of coordinates
	 */
	@Pure
	public boolean containsPoint(Point2D<?, ?> point, int groupIndex) {
		final int grpCount = getGroupCount();
		if (groupIndex < 0) {
			throw new IndexOutOfBoundsException(groupIndex + "<0"); //$NON-NLS-1$
		}
		if (groupIndex > grpCount) {
			throw new IndexOutOfBoundsException(groupIndex + ">" + grpCount); //$NON-NLS-1$
		}
		if (point == null) {
			return false;
		}

		for (int i = 0; i < getPointCountInGroup(groupIndex); ++i) {
			final Point2d cur = getPointAt(groupIndex, i);
			if (cur.epsilonEquals(point, MapElementConstants.POINT_FUSION_DISTANCE)) {
				return true;
			}
		}
		return false;
	}

	/** Replies the starting index of a point in a group.
	 *
	 * @param groupIndex is the index of the desired group
	 * @return the index of the point in the list of points.
	 *     This value is between <code>0</code> and <code>this.pointCoordinates.length()-2</code>
	 * @throws IndexOutOfBoundsException in case of error.
	 */
	private int firstInGroup(int groupIndex) {
		if (this.pointCoordinates == null) {
			throw new IndexOutOfBoundsException();
		}
		final int count = getGroupCount();
		if (groupIndex < 0) {
			throw new IndexOutOfBoundsException(groupIndex + "<0"); //$NON-NLS-1$
		}
		if (groupIndex >= count) {
			throw new IndexOutOfBoundsException(groupIndex + ">=" + count); //$NON-NLS-1$
		}
		final int g = groupIndex - 1;
		if (g >= 0 && this.partIndexes != null && g < this.partIndexes.length) {
			return this.partIndexes[g];
		}
		return 0;
	}

	/** Replies the ending index of a point in a group.
	 *
	 * @param groupIndex is the index of the desired group
	 * @return the index of the point in the list of points.
	 *     This value is between <code>0</code> and <code>this.pointCoordinates.length()-2</code>
	 * @throws IndexOutOfBoundsException in case of error.
	 */
	private int lastInGroup(int groupIndex) {
		if (this.pointCoordinates == null) {
			throw new IndexOutOfBoundsException();
		}
		final int count = getGroupCount();
		if (groupIndex < 0) {
			throw new IndexOutOfBoundsException(groupIndex + "<0"); //$NON-NLS-1$
		}
		if (groupIndex >= count) {
			throw new IndexOutOfBoundsException(groupIndex + ">=" + count); //$NON-NLS-1$
		}
		if (this.partIndexes != null && groupIndex < this.partIndexes.length) {
			return this.partIndexes[groupIndex] - 2;
		}
		return this.pointCoordinates.length - 2;
	}

	/** Replies the group index inside which the point is located at the specified index.
	 *
	 * @param pointIndex is the global index of the point.
	 * @return the index of the group.
	 *     This value is between <code>0</code> and <code>this.getGroupCount()</code>
	 * @throws IndexOutOfBoundsException in case of error.
	 */
	private int groupIndexForPoint(int pointIndex) {
		if (this.pointCoordinates == null || pointIndex < 0 || pointIndex >= this.pointCoordinates.length) {
			throw new IndexOutOfBoundsException();
		}

		if (this.partIndexes == null) {
			return 0;
		}

		for (int i = 0; i < this.partIndexes.length; ++i) {
			if (pointIndex < this.partIndexes[i]) {
				return i;
			}
		}

		return this.partIndexes.length;
	}

	/** Replies the count of points in the specified group.
	 *
	 * @param groupIndex the group index.
	 * @return the count of points in the specified group.
	 * @throws IndexOutOfBoundsException in case of error.
	 */
	@Pure
	public int getPointCountInGroup(int groupIndex) {
		if (groupIndex == 0 && this.pointCoordinates == null) {
			return 0;
		}
		final int firstInGroup = firstInGroup(groupIndex);
		final int lastInGroup = lastInGroup(groupIndex);
		return (lastInGroup - firstInGroup) / 2 + 1;
	}

	/** Replies the global index of the point that corresponds to
	 * the position in the given group.
	 *
	 * @param groupIndex the group index.
	 * @param position is the index of the point in the group
	 * @return the global index of the point
	 * @throws IndexOutOfBoundsException in case of error.
	 */
	@Pure
	public int getPointIndex(int groupIndex, int position) {
		final int groupMemberCount = getPointCountInGroup(groupIndex);
		final int pos;
		if (position < 0) {
			pos = 0;
		} else if (position >= groupMemberCount) {
			pos = groupMemberCount - 1;
		} else {
			pos = position;
		}

		// Move the start/end indexes to corresponds to the bounds of the new points
		return (firstInGroup(groupIndex) + pos * 2) / 2;
	}

	/** Replies the global index of the point2d in the given group.
	 *
	 * @param groupIndex the group index.
	 * @param point2d  is the point in the group
	 * @return the global index of the point or <code>-1</code> if not found
	 */
	@Pure
	public int getPointIndex(int groupIndex, Point2D<?, ?> point2d) {
		if (!this.containsPoint(point2d, groupIndex)) {
			return -1;
		}
		Point2d cur = null;
		int pos = -1;
		for (int i = 0; i < getPointCountInGroup(groupIndex); ++i) {
			cur = getPointAt(groupIndex, i);
			if (cur.epsilonEquals(point2d, MapElementConstants.POINT_FUSION_DISTANCE)) {
				pos = i;
				break;
			}
		}

		return (firstInGroup(groupIndex) + pos * 2) / 2;
	}

	/** Replies the global index of the point that starts a group.
	 *
	 * @param groupIndex the group index.
	 * @return the index of the first point in the group
	 * @throws IndexOutOfBoundsException in case of error.
	 */
	@Pure
	public int getFirstPointIndexInGroup(int groupIndex) {
		final int first = firstInGroup(groupIndex);
		return first / 2;
	}

	/** Replies the global index of the point that ends a group.
	 *
	 * @param groupIndex the group index.
	 * @return the index of the last point in the group
	 * @throws IndexOutOfBoundsException in case of error.
	 */
	@Pure
	public int getLastPointIndexInGroup(int groupIndex) {
		final int last = lastInGroup(groupIndex);
		return last / 2;
	}

	/** Replies the part at the specified index.
	 *
	 * @param index the index.
	 * @return the group
	 * @throws IndexOutOfBoundsException in case of error.
	 */
	@Pure
	public PointGroup getGroupAt(int index) {
		final int count = getGroupCount();
		if (index < 0) {
			throw new IndexOutOfBoundsException(index + "<0"); //$NON-NLS-1$
		}
		if (index >= count) {
			throw new IndexOutOfBoundsException(index + ">=" + count); //$NON-NLS-1$
		}
		return new PointGroup(index);
	}

	/** Replies the iterator on the groups.
	 *
	 * @return the iterator on the groups.
	 */
	@Pure
	public Iterable<PointGroup> groups() {
		return () -> MapComposedElement.this.groupIterator();
	}

	/** Replies the iterator on the groups.
	 *
	 * @return the iterator on the groups.
	 */
	@Pure
	public Iterator<PointGroup> groupIterator() {
		return new GroupIterator();
	}

	/** Replies the iterator on the points.
	 *
	 * @return the iterator on the points.
	 */
	@Pure
	public Iterable<Point2d> points() {
		return () -> MapComposedElement.this.pointIterator();
	}

	/** Replies the iterator on the points.
	 *
	 * @return the iterator on the points.
	 */
	@Pure
	public Iterator<Point2d> pointIterator() {
		return new PointIterator();
	}

	/** Add the specified point at the end of the last group.
	 *
	 * @param point the new point.
	 * @return the index of the new point in the element.
	 */
	public final int addPoint(Point2D<?, ?> point) {
		return addPoint(point.getX(), point.getY());
	}

	/** Add the specified point at the end of the last group.
	 *
	 * @param x x coordinate
	 * @param y y coordinate
	 * @return the index of the new point in the element.
	 */
	public int addPoint(double x, double y) {
		int pointIndex;

		if (this.pointCoordinates == null) {
			this.pointCoordinates = new double[] {x, y};
			this.partIndexes = null;
			pointIndex = 0;
		} else {
			double[] pts = new double[this.pointCoordinates.length + 2];
			System.arraycopy(this.pointCoordinates, 0, pts, 0, this.pointCoordinates.length);
			pointIndex = pts.length - 2;
			pts[pointIndex] = x;
			pts[pointIndex + 1] = y;
			this.pointCoordinates = pts;
			pts = null;

			pointIndex /= 2;
		}

		fireShapeChanged();
		fireElementChanged();

		return pointIndex;
	}

	/** Add the specified point at the end of the specified group.
	 *
	 * @param point the new point.
	 * @param indexGroup the index of the group.
	 * @return the index of the point in the element.
	 * @throws IndexOutOfBoundsException in case of error.
	 */
	public final int addPoint(Point2D<?, ?> point, int indexGroup) {
		return addPoint(point.getX(), point.getY(), indexGroup);
	}

	/** Add the specified point at the end of the specified group.
	 *
	 * @param x x coordinate
	 * @param y y coordinate
	 * @param groupIndex the index of the group.
	 * @return the index of the point in the element.
	 * @throws IndexOutOfBoundsException in case of error.
	 */
	public int addPoint(double x, double y, int groupIndex) {
		final int groupCount = getGroupCount();

		if (groupIndex < 0) {
			throw new IndexOutOfBoundsException(groupIndex + "<0"); //$NON-NLS-1$
		}
		if (groupIndex >= groupCount) {
			throw new IndexOutOfBoundsException(groupIndex + ">=" + groupCount); //$NON-NLS-1$
		}

		int pointIndex;

		if (this.pointCoordinates == null) {
			this.pointCoordinates = new double[] {x, y};
			this.partIndexes = null;
			pointIndex = 0;
		} else {
			pointIndex = lastInGroup(groupIndex);
			double[] pts = new double[this.pointCoordinates.length + 2];

			pointIndex += 2;

			System.arraycopy(this.pointCoordinates, 0, pts, 0, pointIndex);
			System.arraycopy(this.pointCoordinates, pointIndex, pts, pointIndex + 2, this.pointCoordinates.length - pointIndex);
			pts[pointIndex] = x;
			pts[pointIndex + 1] = y;
			this.pointCoordinates = pts;
			pts = null;

			//Shift the following groups's indexes
			if (this.partIndexes != null) {
				for (int idx = groupIndex; idx < this.partIndexes.length; ++idx) {
					this.partIndexes[idx] += 2;
				}
			}

			pointIndex /= 2.;
		}

		fireShapeChanged();
		fireElementChanged();

		return pointIndex;
	}

	/** Add the specified point into a newgroup.
	 *
	 * @param point the point.
	 */
	public final void addGroup(Point2D<?, ?> point) {
		addGroup(point.getX(), point.getY());
	}

	/** Add the specified point into a newgroup.
	 *
	 * @param x x coordinate
	 * @param y y coordinate
	 * @return the index of the new point in this element.
	 */
	public int addGroup(double x, double y) {
		int pointIndex;
		if (this.pointCoordinates == null) {
			this.pointCoordinates = new double[] {x, y};
			this.partIndexes = null;
			pointIndex = 0;
		} else {
			double[] pts = new double[this.pointCoordinates.length + 2];
			System.arraycopy(this.pointCoordinates, 0, pts, 0, this.pointCoordinates.length);
			pointIndex = pts.length - 2;
			pts[pointIndex] = x;
			pts[pointIndex + 1] = y;

			final int groupCount = getGroupCount();
			int[] grps = new int[groupCount];
			if (this.partIndexes != null) {
				System.arraycopy(this.partIndexes, 0, grps, 0, groupCount - 1);
			}
			grps[groupCount - 1] = pointIndex;

			this.pointCoordinates = pts;
			pts = null;

			this.partIndexes = grps;
			grps = null;

			pointIndex /= 2;
		}

		fireShapeChanged();
		fireElementChanged();

		return pointIndex;
	}

	/**
	 * invert the points coordinates of this element on the groupIndex in argument.
	 *
	 * <p>This method is reversible:
	 * {@code this.invertPointsIn(sameIndex) == this.invertPointsIn(sameIndex).invertPointsIn(sameIndex)}
	 *
	 * <p>NOTE: invert each parts with {@link MapComposedElement#invertPointsIn(int)}
	 * does'nt produce the same result as {@link MapComposedElement#invert()}.
	 *
	 * <p>This method invert points coordinates only.
	 * this.invertPointsIn(0 .. indexCount) != this.invert()
	 *
	 * @param groupIndex int element on which invert points
	 * @return this
	 * @throws IndexOutOfBoundsException if the given groupIndex is out of range.
	 */
	public MapComposedElement invertPointsIn(int groupIndex) {
		if (this.pointCoordinates == null) {
			throw new IndexOutOfBoundsException();
		}
		final int grpCount = getGroupCount();
		if (groupIndex < 0) {
			throw new IndexOutOfBoundsException(groupIndex + "<0"); //$NON-NLS-1$
		}
		if (groupIndex > grpCount) {
			throw new IndexOutOfBoundsException(groupIndex + ">" + grpCount); //$NON-NLS-1$
		}

		final int count = this.getPointCountInGroup(groupIndex);
		final int first = firstInGroup(groupIndex);
		double[] tmp = new double[count * 2];
		for (int i = 0; i < count * 2; i += 2) {
			tmp[i] = this.pointCoordinates[first + 2 * count - 1 - (i + 1)];
			tmp[i + 1] = this.pointCoordinates[first + 2 * count - 1  - i];
		}
		System.arraycopy(tmp, 0, this.pointCoordinates, first, tmp.length);
		tmp = null;
		return this;
	}

	/** Invert the order of points coordinates of this element and reorder the groupIndex too.
	 *
	 * <p>NOTE: invert each parts with {@link MapComposedElement#invertPointsIn(int)}
	 * does'nt produce the same result as {@link MapComposedElement#invert()}
	 *
	 * <p>This method invert points coordinates AND start index of parts to
	 * keep the logical order of points
	 * this method is reversible
	 *
	 * <p>{@code this.invert() == this.invert().invert()}
	 *
	 * @return the inverted element
	 * @throws IndexOutOfBoundsException if there is no point into the composed element.
	 */
	public MapComposedElement invert() {
		if (this.pointCoordinates == null) {
			throw new IndexOutOfBoundsException();
		}
		double[] tmp = new double[this.pointCoordinates.length];
		for (int i = 0; i < this.pointCoordinates.length; i += 2) {
			tmp[i] = this.pointCoordinates[this.pointCoordinates.length - 1 - (i + 1)];
			tmp[i + 1] = this.pointCoordinates[this.pointCoordinates.length - 1 - i];
		}
		System.arraycopy(tmp, 0, this.pointCoordinates, 0, this.pointCoordinates.length);

		if (this.partIndexes != null) {
			int[] tmpint = new int[this.partIndexes.length];
			//part 0 not inside the index array
			for (int i = 0; i < this.partIndexes.length; ++i) {
				tmpint[this.partIndexes.length - 1 - i] = this.pointCoordinates.length - this.partIndexes[i];
			}
			System.arraycopy(tmpint, 0, this.partIndexes, 0, this.partIndexes.length);
			tmpint = null;
		}
		tmp = null;
		return this;
	}

	/** Insert the specified point at the given index in the specified group.
	 *
	 * @param point is the point to insert
	 * @param indexGroup is the index of the group
	 * @param indexInGroup is the index of the ponit in the group (0 for the
	 *     first point of the group...).
	 * @return the index of the new point in the element.
	 * @throws IndexOutOfBoundsException in case of error.
	 */
	public final int insertPointAt(Point2D<?, ?> point, int indexGroup, int indexInGroup) {
		return insertPointAt(point.getX(), point.getY(), indexGroup, indexInGroup);
	}

	/** Insert the specified point at the given index in the specified group.
	 *
	 * @param x is the point to insert
	 * @param y is the point to insert
	 * @param groupIndex is the index of the group
	 * @param indexInGroup is the index of the ponit in the group (0 for the
	 *     first point of the group...).
	 * @return the index of the new point in the element.
	 * @throws IndexOutOfBoundsException in case of error.
	 */
	public int insertPointAt(double x, double y, int groupIndex, int indexInGroup) {
		final int groupCount = getGroupCount();

		if (groupIndex < 0) {
			throw new IndexOutOfBoundsException(groupIndex + "<0"); //$NON-NLS-1$
		}
		if (groupIndex >= groupCount) {
			throw new IndexOutOfBoundsException(groupIndex + ">=" + groupCount); //$NON-NLS-1$
		}

		int pointIndex;

		if (this.pointCoordinates == null) {
			this.pointCoordinates = new double[] {x, y};
			this.partIndexes = null;
			pointIndex = 0;
		} else {
			final int startIndex = firstInGroup(groupIndex);

			// Be sure that the member's index is in the group index's range
			final int groupMemberCount = getPointCountInGroup(groupIndex);
			final int g;
			if (indexInGroup < 0) {
				g = 0;
			} else if (indexInGroup > groupMemberCount) {
				g = groupMemberCount;
			} else {
				g = indexInGroup;
			}

			// Move the start/end indexes to corresponds to the bounds of the new points
			pointIndex = startIndex + g * 2;

			// Update the array of points
			double[] pts = new double[this.pointCoordinates.length + 2];
			System.arraycopy(this.pointCoordinates, 0, pts, 0, pointIndex);
			System.arraycopy(this.pointCoordinates, pointIndex, pts, pointIndex + 2, this.pointCoordinates.length - pointIndex);
			pts[pointIndex] = x;
			pts[pointIndex + 1] = y;
			this.pointCoordinates = pts;
			pts = null;

			//Shift the following groups's indexes
			if (this.partIndexes != null) {
				for (int idx = groupIndex; idx < this.partIndexes.length; ++idx) {
					this.partIndexes[idx] += 2;
				}
			}

			pointIndex /= 2;
		}

		fireShapeChanged();
		fireElementChanged();

		return pointIndex;
	}

	/** Replies the specified point at the given index.
	 *
	 * <p>If the <var>index</var> is negative, it will corresponds
	 * to an index starting from the end of the list.
	 *
	 * @param index is the index of the desired point
	 * @return the point at the given index
	 * @throws IndexOutOfBoundsException in case of error.
	 */
	@Pure
	public Point2d getPointAt(int index) {
		final int count = getPointCount();
		int idx = index;
		if (idx < 0) {
			idx = count + idx;
		}
		if (idx < 0) {
			throw new IndexOutOfBoundsException(idx + "<0"); //$NON-NLS-1$
		}
		if (idx >= count) {
			throw new IndexOutOfBoundsException(idx + ">=" + count); //$NON-NLS-1$
		}
		return new Point2d(
				this.pointCoordinates[idx * 2],
				this.pointCoordinates[idx * 2 + 1]);
	}

	/** Replies the specified point at the given index in the specified group.
	 *
	 * @param groupIndex is the index of the group
	 * @param indexInGroup is the index of the point in the group (0 for the
	 *     first point of the group...).
	 * @return the point
	 * @throws IndexOutOfBoundsException in case of error.
	 */
	@Pure
	public Point2d getPointAt(int groupIndex, int indexInGroup) {
		final int startIndex = firstInGroup(groupIndex);

		// Besure that the member's index is in the group index's range
		final int groupMemberCount = getPointCountInGroup(groupIndex);
		if (indexInGroup < 0) {
			throw new IndexOutOfBoundsException(indexInGroup + "<0"); //$NON-NLS-1$
		}
		if (indexInGroup >= groupMemberCount) {
			throw new IndexOutOfBoundsException(indexInGroup + ">=" + groupMemberCount); //$NON-NLS-1$
		}

		return new Point2d(
				this.pointCoordinates[startIndex + indexInGroup * 2],
				this.pointCoordinates[startIndex + indexInGroup * 2 + 1]);
	}

	/** Set the specified point at the given index.
	 *
	 * <p>If the <var>index</var> is negative, it will corresponds
	 * to an index starting from the end of the list.
	 *
	 * @param index is the index of the desired point
	 * @param x is the new value of the point
	 * @param y is the new value of the point
	 * @return <code>true</code> if the point was set, <code>false</code> if
	 *     the specified coordinates correspond to the already existing point.
	 * @throws IndexOutOfBoundsException in case of error.
	 */
	public final boolean setPointAt(int index, double x, double y) {
		return setPointAt(index, x, y, false);
	}

	/** Set the specified point at the given index.
	 *
	 * <p>If the <var>index</var> is negative, it will corresponds
	 * to an index starting from the end of the list.
	 *
	 * @param index is the index of the desired point
	 * @param x is the new value of the point
	 * @param y is the new value of the point
	 * @param canonize indicates if the function {@link #canonize(int)} must be called.
	 * @return <code>true</code> if the point was set, <code>false</code> if
	 *     the specified coordinates correspond to the already existing point.
	 * @throws IndexOutOfBoundsException in case of error.
	 */
	public boolean setPointAt(int index, double x, double y, boolean canonize) {
		final int count = getPointCount();
		int idx = index;
		if (idx < 0) {
			idx = count + idx;
		}
		if (idx < 0) {
			throw new IndexOutOfBoundsException(idx + "<0"); //$NON-NLS-1$
		}
		if (idx >= count) {
			throw new IndexOutOfBoundsException(idx + ">=" + count); //$NON-NLS-1$
		}
		final PointFusionValidator validator = getPointFusionValidator();
		final int idx1 = idx * 2;
		final int idx2 = idx1 + 1;
		if (!validator.isSame(x, y, this.pointCoordinates[idx1], this.pointCoordinates[idx2])) {
			this.pointCoordinates[idx1] = x;
			this.pointCoordinates[idx2] = y;

			if (canonize) {
				canonize(idx);
			}

			fireShapeChanged();
			fireElementChanged();
			return true;
		}
		return false;
	}

	/** Set the specified point at the given index.
	 *
	 * <p>If the <var>index</var> is negative, it will corresponds
	 * to an index starting from the end of the list.
	 *
	 * @param index is the index of the desired point
	 * @param point is the new value of the point
	 * @return <code>true</code> if the point was set, <code>false</code> if
	 *     the specified coordinates correspond to the already existing point.
	 * @throws IndexOutOfBoundsException in case of error.
	 */
	public final boolean setPointAt(int index, Point2D<?, ?> point) {
		return setPointAt(index, point.getX(), point.getY(), false);
	}

	/** Set the specified point at the given index.
	 *
	 * <p>If the <var>index</var> is negative, it will corresponds
	 * to an index starting from the end of the list.
	 *
	 * @param index is the index of the desired point
	 * @param point is the new value of the point
	 * @param canonize indicates if the function {@link #canonize(int)} must be called.
	 * @return <code>true</code> if the point was set, <code>false</code> if
	 *     the specified coordinates correspond to the already existing point.
	 * @throws IndexOutOfBoundsException in case of error.
	 */
	public final boolean setPointAt(int index, Point2D<?, ?> point, boolean canonize) {
		return setPointAt(index, point.getX(), point.getY(), canonize);
	}

	/** Set the specified point at the given index in the specified group.
	 *
	 * @param groupIndex is the index of the group
	 * @param indexInGroup is the index of the ponit in the group (0 for the
	 *     first point of the group...).
	 * @param x is the new value.
	 * @param y is the new value.
	 * @return <code>true</code> if the point was set, <code>false</code> if
	 *     the specified coordinates correspond to the already existing point.
	 * @throws IndexOutOfBoundsException in case of error
	 */
	public final boolean setPointAt(int groupIndex, int indexInGroup, double x, double y) {
		return setPointAt(groupIndex, indexInGroup, x, y, false);
	}

	/** Set the specified point at the given index in the specified group.
	 *
	 * @param groupIndex is the index of the group
	 * @param indexInGroup is the index of the ponit in the group (0 for the
	 *     first point of the group...).
	 * @param x is the new value.
	 * @param y is the new value.
	 * @param canonize indicates if the function {@link #canonize(int)} must be called.
	 * @return <code>true</code> if the point was set, <code>false</code> if
	 *     the specified coordinates correspond to the already existing point.
	 * @throws IndexOutOfBoundsException in case of error.
	 */
	public boolean setPointAt(int groupIndex, int indexInGroup, double x, double y, boolean canonize) {
		final int startIndex = firstInGroup(groupIndex);

		// Besure that the member's index is in the group index's range
		final int groupMemberCount = getPointCountInGroup(groupIndex);
		if (indexInGroup < 0) {
			throw new IndexOutOfBoundsException(indexInGroup + "<0"); //$NON-NLS-1$
		}
		if (indexInGroup >= groupMemberCount) {
			throw new IndexOutOfBoundsException(indexInGroup + ">=" + groupMemberCount); //$NON-NLS-1$
		}
		final PointFusionValidator validator = getPointFusionValidator();
		final int idx1 = startIndex + indexInGroup * 2;
		final int idx2 = idx1 + 1;

		if (!validator.isSame(x, y, this.pointCoordinates[idx1], this.pointCoordinates[idx2])) {
			this.pointCoordinates[idx1] = x;
			this.pointCoordinates[idx2] = y;

			if (canonize) {
				canonize(idx1 / 2);
			}

			fireShapeChanged();
			fireElementChanged();
			return true;
		}

		return false;
	}

	/** Set the specified point at the given index in the specified group.
	 *
	 * @param groupIndex is the index of the group
	 * @param indexInGroup is the index of the ponit in the group (0 for the
	 *     first point of the group...).
	 * @param point is the new value.
	 * @return <code>true</code> if the point was set, <code>false</code> if
	 *     the specified coordinates correspond to the already existing point.
	 * @throws IndexOutOfBoundsException in case of error.
	 */
	public final boolean setPointAt(int groupIndex, int indexInGroup, Point2D<?, ?> point) {
		return setPointAt(groupIndex, indexInGroup, point, false);
	}

	/** Set the specified point at the given index in the specified group.
	 *
	 * @param groupIndex is the index of the group
	 * @param indexInGroup is the index of the ponit in the group (0 for the
	 *     first point of the group...).
	 * @param point is the new value.
	 * @param canonize indicates if the function {@link #canonize(int)} must be called.
	 * @return <code>true</code> if the point was set, <code>false</code> if
	 *     the specified coordinates correspond to the already existing point.
	 * @throws IndexOutOfBoundsException in case of error.
	 */
	public final boolean setPointAt(int groupIndex, int indexInGroup, Point2D<?, ?> point, boolean canonize) {
		return setPointAt(groupIndex, indexInGroup, point.getX(), point.getY(), canonize);
	}

	/** Remove the specified group.
	 *
	 * @param groupIndex the index of the group.
	 * @return <code>true</code> on success, otherwise <code>false</code>
	 */
	public boolean removeGroupAt(int groupIndex) {
		try {
			final int startIndex = firstInGroup(groupIndex);
			final int lastIndex = lastInGroup(groupIndex);

			final int ptsToRemoveCount = (lastIndex - startIndex + 2) / 2;
			int rest = this.pointCoordinates.length / 2 - ptsToRemoveCount;
			if (rest > 0) {
				// Remove the points
				rest *= 2;
				final double[] newPts = new double[rest];
				System.arraycopy(this.pointCoordinates, 0, newPts, 0, startIndex);
				System.arraycopy(
						this.pointCoordinates, lastIndex + 2,
						newPts, startIndex,
						rest - startIndex);
				this.pointCoordinates = newPts;

				// Remove the group
				if (this.partIndexes != null) {
					// Shift the group's indexes
					for (int i = groupIndex; i < this.partIndexes.length; ++i) {
						this.partIndexes[i] -= ptsToRemoveCount * 2;
					}

					// Removing the group
					int[] newGroups = null;

					if (this.partIndexes.length > 1) {
						newGroups = new int[this.partIndexes.length - 1];
						if (groupIndex == 0) {
							System.arraycopy(this.partIndexes, 1, newGroups, 0, this.partIndexes.length - 1);
						} else {
							System.arraycopy(this.partIndexes, 0, newGroups, 0, groupIndex - 1);
							System.arraycopy(
									this.partIndexes, groupIndex,
									newGroups, groupIndex - 1,
									this.partIndexes.length - groupIndex);
						}
					}

					this.partIndexes = newGroups;
				}
			} else {
				// Remove all the points
				this.pointCoordinates = null;
				this.partIndexes = null;
			}

			fireShapeChanged();
			fireElementChanged();

			return true;
		} catch (IndexOutOfBoundsException exception) {
			//
		}
		return false;
	}

	/** Remove the specified point at the given index in the specified group.
	 *
	 * @param groupIndex is the index of the group
	 * @param indexInGroup is the index of the ponit in the group (0 for the
	 *     first point of the group...).
	 * @return the removed point
	 * @throws IndexOutOfBoundsException in case of error.
	 */
	public Point2d removePointAt(int groupIndex, int indexInGroup) {
		final int startIndex = firstInGroup(groupIndex);
		final int lastIndex = lastInGroup(groupIndex);

		// Translate local point's coordinate into global point's coordinate
		final int g = indexInGroup * 2 + startIndex;

		// Be sure that the member's index is in the group index's range
		if (g < startIndex) {
			throw new IndexOutOfBoundsException(g + "<" + startIndex); //$NON-NLS-1$
		}
		if (g > lastIndex) {
			throw new IndexOutOfBoundsException(g + ">" + lastIndex); //$NON-NLS-1$
		}

		final Point2d p = new Point2d(
				this.pointCoordinates[g],
				this.pointCoordinates[g + 1]);

		// Deleting the point
		final double[] newPtsArray;
		if (this.pointCoordinates.length <= 2) {
			newPtsArray = null;
		} else {
			newPtsArray = new double[this.pointCoordinates.length - 2];
			System.arraycopy(this.pointCoordinates, 0, newPtsArray, 0, g);
			System.arraycopy(
					this.pointCoordinates, g + 2,
					newPtsArray, g, this.pointCoordinates.length - g - 2);
		}
		this.pointCoordinates = newPtsArray;

		if (this.partIndexes != null) {
			// Shift the group's indexes
			for (int i = groupIndex; i < this.partIndexes.length; ++i) {
				this.partIndexes[i] -= 2;
			}
			// Removing the group
			final int ptsCount = (lastIndex - startIndex) / 2;
			if (ptsCount <= 0) {
				int[] newGroups = null;

				if (this.partIndexes.length > 1) {
					newGroups = new int[this.partIndexes.length - 1];
					if (groupIndex == 0) {
						System.arraycopy(this.partIndexes, 1, newGroups, 0, this.partIndexes.length - 1);
					} else {
						System.arraycopy(this.partIndexes, 0, newGroups, 0, groupIndex - 1);
						System.arraycopy(
								this.partIndexes, groupIndex,
								newGroups, groupIndex - 1,
								this.partIndexes.length - groupIndex);
					}
				}

				this.partIndexes = newGroups;
			}
		}

		fireShapeChanged();
		fireElementChanged();

		return p;
	}

	/** Remove unnecessary points from the specified index.
	 *
	 * @return <code>true</code> if points are removed, otherwise <code>false</code>
	 */
	public boolean canonize() {
		return canonize(0);
	}

	/** Remove unnecessary points from the specified index.
	 *
	 * @return <code>true</code> if points are removed, otherwise <code>false</code>
	 */
	private boolean canonize(int index) {
		final int count = getPointCount();

		int ix = index;
		if (ix < 0) {
			ix = count + ix;
		}
		if (ix < 0) {
			throw new IndexOutOfBoundsException(ix + "<0"); //$NON-NLS-1$
		}
		if (ix >= count) {
			throw new IndexOutOfBoundsException(ix + ">=" + count); //$NON-NLS-1$
		}

		final int partIndex = groupIndexForPoint(ix);

		final int firstPts = firstInGroup(partIndex);
		final int endPts = lastInGroup(partIndex);

		final int myIndex = ix * 2;
		int firstToRemove = myIndex;
		int lastToRemove = myIndex;
		boolean removeOne = false;

		final double xbase = this.pointCoordinates[ix * 2];
		final double ybase = this.pointCoordinates[ix * 2 + 1];

		final PointFusionValidator validator = getPointFusionValidator();

		// Search for the first point to remove
		for (int idx = myIndex - 2; idx >= firstPts; idx -= 2) {
			final double x = this.pointCoordinates[idx];
			final double y = this.pointCoordinates[idx + 1];
			if (validator.isSame(xbase, ybase, x, y)) {
				firstToRemove = idx;
				removeOne = true;
			} else {
				// Stop search
				break;
			}
		}

		// Search for the last point to remove
		for (int idx = myIndex + 2; idx <= endPts; idx += 2) {
			final double x = this.pointCoordinates[idx];
			final double y = this.pointCoordinates[idx + 1];
			if (validator.isSame(xbase, ybase, x, y)) {
				lastToRemove = idx;
				removeOne = true;
			} else {
				// Stop search
				break;
			}
		}

		if (removeOne) {
			// A set of points are detected as too near.
			// They should be removed and replaced by the reference point

			final int removalCount = (lastToRemove / 2 - firstToRemove / 2) * 2;

			// Deleting the point
			final double[] newPtsArray = new double[this.pointCoordinates.length - removalCount];
			assert newPtsArray.length >= 2;

			System.arraycopy(this.pointCoordinates, 0, newPtsArray, 0, firstToRemove);
			System.arraycopy(this.pointCoordinates, lastToRemove + 2, newPtsArray,
					firstToRemove + 2, this.pointCoordinates.length - lastToRemove - 2);
			newPtsArray[firstToRemove] = xbase;
			newPtsArray[firstToRemove + 1] = ybase;

			this.pointCoordinates = newPtsArray;

			if (this.partIndexes != null) {
				// Shift the group's indexes
				for (int i = partIndex; i < this.partIndexes.length; ++i) {
					this.partIndexes[i] -= removalCount;
				}
			}

			return true;
		}

		return false;
	}

	/** Replies the validator that is able
	 * to detect points at the same location.
	 *
	 * <p>The validator provided by MapComposedElement
	 * is based on {@link Point2D#epsilonEquals(org.arakhne.afc.math.geometry.d2.Tuple2D, double)}.
	 * Subclasses should override this function
	 * to provide specifical implementation of a validator.
	 *
	 * @return the validator, never <code>null</code>
	 */
	@SuppressWarnings("static-method")
	@Pure
	public PointFusionValidator getPointFusionValidator() {
		return DEFAULT_VALIDATOR;
	}

	/**
	 * This class represents a group of points inside a composed MapElement.
	 *
	 * @author $Author: olamotte$
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 14.0
	 */
	public final class PointGroup implements Comparable<PointGroup>, Iterable<Point2d> {

		/** is the index of this group.
		 */
		protected final int partIndex;

		/** Constructor.
		 * @param index the group index.
		 */
		public PointGroup(int index) {
			this.partIndex = index;
		}

		@Override
		@Pure
		public int compareTo(PointGroup group) {
			return group.partIndex - this.partIndex;
		}

		/** Replies the gorup index.
		 * @return the index of this group
		 */
		@Pure
		public int getGroupIndex() {
			return this.partIndex;
		}

		@Pure
		private MapComposedElement getParentInstance() {
			return MapComposedElement.this;
		}

		@Override
		@Pure
		public boolean equals(Object obj) {
			if (obj instanceof MapComposedElement.PointGroup) {
				final PointGroup g = (PointGroup) obj;
				return MapComposedElement.this == g.getParentInstance()
						&& g.partIndex == this.partIndex;
			}
			return false;
		}

		@Override
		@Pure
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + MapComposedElement.this.getGeoId().hashCode();
			result = prime * result + this.partIndex;
			return result;
		}

		/** Add a point at the end of this part.
		 *
		 * @param point the new point.
		 */
		public void add(Point2d point) {
			MapComposedElement.this.addPoint(point, this.partIndex);
		}

		/** Add a point at the end of this part.
		 *
		 * @param x x coordinate.
		 * @param y y coordinate.
		 */
		public void add(double x, double y) {
			MapComposedElement.this.addPoint(x, y, this.partIndex);
		}

		/** Insert a point at the specified index.
		 *
		 * @param point the new point.
		 * @param index the insertion index.
		 * @throws IndexOutOfBoundsException in case of error.
		 */
		public void add(Point2D<?, ?> point, int index) {
			MapComposedElement.this.insertPointAt(point, this.partIndex, index);
		}

		/** Insert a point at the specified index.
		 *
		 * @param x x coordinate.
		 * @param y y coordinate.
		 * @param index the insertion index.
		 * @throws IndexOutOfBoundsException in case of error.
		 */
		public void add(double x, double y, int index) {
			MapComposedElement.this.insertPointAt(x, y, this.partIndex, index);
		}

		/** Remove the point at the specified index.
		 *
		 * @param index the index.
		 * @return the removed point.
		 * @throws IndexOutOfBoundsException in case of error.
		 */
		public Point2d removeAt(int index) {
			return MapComposedElement.this.removePointAt(this.partIndex, index);
		}

		/** Replies the count of points.
		 *
		 * @return the count of points.
		 */
		@Pure
		public int size() {
			return MapComposedElement.this.getPointCountInGroup(this.partIndex);
		}

		/** Replies the point at the specified index.
		 *
		 * @param index the index.
		 * @return the point
		 * @throws IndexOutOfBoundsException in case of error.
		 */
		@Pure
		public Point2d get(int index) {
			return MapComposedElement.this.getPointAt(this.partIndex, index);
		}

		/** Replies the points from the specified index.
		 * Fill the specified array of points with
		 * the point's coordinates starting from
		 * the specified ponit index.
		 *
		 * <p>Thus function does not create the Point2D instances,
		 * the function {@link Point2d#set(double[])}
		 * is used instead.
		 *
		 * @param index is the index of the first point to reply.
		 * @param points is the list of points to fill.
		 * @return the count of elements set.
		 * @throws IndexOutOfBoundsException in case of error.
		 */
		@Pure
		public int getMany(int index, Point2D<?, ?>... points) {
			int count = 0;
			try {
				final int size = MapComposedElement.this.getPointCountInGroup(this.partIndex);
				for (int idx = index, idxP = 0; idx < size && idxP < points.length; ++idx, ++idxP) {
					final Point2d pts = MapComposedElement.this.getPointAt(this.partIndex, idx);
					if (pts != null && points[idxP] != null) {
						points[idxP].set(pts);
						++count;
					}
				}
			} catch (IndexOutOfBoundsException exception) {
				//
			}
			return count;
		}

		/** Set the point at the specified index.
		 *
		 * @param index the index.
		 * @param x x coordinate.
		 * @param y y coordinate.
		 * @throws IndexOutOfBoundsException in case of error.
		 */
		public void set(int index, double x, double y) {
			MapComposedElement.this.setPointAt(this.partIndex, index, x, y);
		}

		/** Set the point at the specified index.
		 *
		 * @param index the index.
		 * @param point the point coordinates.
		 * @throws IndexOutOfBoundsException in case of error.
		 */
		public void set(int index, Point2D<?, ?> point) {
			MapComposedElement.this.setPointAt(this.partIndex, index, point.getX(), point.getY());
		}

		@Override
		@Pure
		public Iterator<Point2d> iterator() {
			return new PointIterator();
		}

		/** Replies the points that are composing this element.
		 * @return the points of this group.
		 */
		@Pure
		public Iterable<Point2d> points() {
			return this;
		}

		/**
		 * Iterator on points.
		 *
		 * @author $Author: sgalland$
		 * @version $FullVersion$
		 * @mavengroupid $GroupId$
		 * @mavenartifactid $ArtifactId$
		 * @since 14.0
		 */
		private class PointIterator implements Iterator<Point2d> {

			private int nextPointIndex;

			PointIterator() {
				//
			}

			@Override
			@Pure
			public boolean hasNext() {
				return this.nextPointIndex < PointGroup.this.size();
			}

			@Override
			@Pure
			public Point2d next() {
				if (this.nextPointIndex >= PointGroup.this.size()) {
					throw new NoSuchElementException();
				}

				try {
					final Point2d pt = PointGroup.this.get(this.nextPointIndex);
					++this.nextPointIndex;
					return pt;
				} catch (IndexOutOfBoundsException exception) {
					throw new NoSuchElementException();
				}
			}

			@Override
			public void remove() {
				final int idxToRemove = this.nextPointIndex - 1;
				if (idxToRemove < 0 || idxToRemove >= PointGroup.this.size()) {
					throw new NoSuchElementException();
				}

				try {
					PointGroup.this.removeAt(idxToRemove);
					this.nextPointIndex = idxToRemove;
				} catch (IndexOutOfBoundsException exception) {
					throw new ConcurrentModificationException();
				}
			}

		} /* class PointIterator */

	} /* class PointGroup */

	/**
	 * Iterator on groups.
	 *
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	private class GroupIterator implements Iterator<PointGroup> {

		private int nextGroupIndex;

		/** Constructor.
		 */
		GroupIterator() {
			//
		}

		@Override
		@Pure
		public boolean hasNext() {
			return this.nextGroupIndex < MapComposedElement.this.getGroupCount();
		}

		@Override
		@Pure
		public PointGroup next() {
			if (this.nextGroupIndex >= MapComposedElement.this.getGroupCount()) {
				throw new NoSuchElementException();
			}

			try {
				final PointGroup grp = MapComposedElement.this.getGroupAt(this.nextGroupIndex);
				++this.nextGroupIndex;
				return grp;
			} catch (IndexOutOfBoundsException exception) {
				throw new NoSuchElementException();
			}
		}

		@Override
		public void remove() {
			final int idxToRemove = this.nextGroupIndex - 1;
			if (idxToRemove < 0 || idxToRemove >= MapComposedElement.this.getGroupCount()) {
				throw new NoSuchElementException();
			}

			if (MapComposedElement.this.removeGroupAt(idxToRemove)) {
				this.nextGroupIndex = idxToRemove;
			}
		}

	} /* class GroupIterator */

	/**
	 * Iterator on points.
	 *
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	private class PointIterator implements Iterator<Point2d> {

		private int nextPointIndex;

		/** Constructor.
		 */
		PointIterator() {
			//
		}

		@Override
		@Pure
		public boolean hasNext() {
			return this.nextPointIndex < MapComposedElement.this.getPointCount();
		}

		@Pure
		@Override
		public Point2d next() {
			if (this.nextPointIndex >= MapComposedElement.this.getPointCount()) {
				throw new NoSuchElementException();
			}

			try {
				final Point2d pt = MapComposedElement.this.getPointAt(this.nextPointIndex);
				++this.nextPointIndex;
				return pt;
			} catch (IndexOutOfBoundsException exception) {
				throw new NoSuchElementException();
			}
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}

	} /* class PointIterator */

	/** Internal point fusion validator.
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 4.0
	 */
	private static class Validator implements PointFusionValidator {

		/** Constructor.
		 */
		Validator() {
			//
		}

		@Override
		@Pure
		public boolean isSame(double x1, double y1, double x2, double y2) {
			return Point2D.getDistanceSquaredPointPoint(x1, y1, x2, y2) <= MapElementConstants.POINT_FUSION_SQUARED_DISTANCE;
		}

	} /* class Validator */

}
