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

package org.arakhne.afc.gis.road.path;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.gis.road.primitive.RoadConnection;
import org.arakhne.afc.gis.road.primitive.RoadSegment;
import org.arakhne.afc.math.geometry.d1.Direction1D;
import org.arakhne.afc.math.graph.GraphPath;

/**
 * This class describes a path inside a road network.
 * A RoadPath is composed of a sequence of connected
 * RoadSegments. Consequently, if you try to add
 * a RoadSegment not connected
 * to the first or to the last segment in the RoadPath,
 * then is will be rejected.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 * @see ClusteredRoadPath
 */
public class RoadPath extends GraphPath<RoadPath, RoadSegment, RoadConnection> {

	/** Constructor of road path that is backed to the given list.
	 *
	 * @param original the list to be the backend.
	 * @since 16.0
	 */
	protected RoadPath(List<RoadSegment> original) {
		super(original);
	}

	/** Constructor.
	 */
	public RoadPath() {
		super();
	}

	/** Constructor.
	 * @param segment is the segment from which to start.
	 * @since 4.0
	 */
	public RoadPath(RoadSegment segment) {
		super();
		add(segment);
	}

	/** Constructor.
	 * @param segment is the segment from which to start.
	 * @param starting_point is the segment's point indicating the direction.
	 */
	public RoadPath(RoadSegment segment, RoadConnection starting_point) {
		super(segment, starting_point);
	}

	/** Constructor.
	 * @param path is the path to copy.
	 * @since 4.0
	 */
	public RoadPath(RoadPath path) {
		super();
		addAll(path);
	}

	/** Add the elements stored inside a road path into a road path.
	 * This function takes care about the two ends to the path to insert into.
	 *
	 * @param inside is the path to change.
	 * @param elements are the elements to insert.
	 * @return <code>true</code> if all the elements were added;
	 *     otherwise <code>false</code>.
	 * @since 4.0
	 */
	public static boolean addPathToPath(RoadPath inside, RoadPath elements) {
		RoadConnection first = elements.getFirstPoint();
		RoadConnection last = elements.getLastPoint();
		assert first != null;
		assert last != null;
		first = first.getWrappedRoadConnection();
		last = last.getWrappedRoadConnection();
		assert first != null;
		assert last != null;

		if (last.equals(inside.getLastPoint()) || last.equals(inside.getFirstPoint())) {
			for (int i = elements.size() - 1; i >= 0; --i) {
				if (!inside.add(elements.get(i))) {
					return false;
				}
			}
		} else {
			for (final RoadSegment segment : elements) {
				if (!inside.add(segment)) {
					return false;
				}
			}
		}
		return true;
	}

	/** Create a road path that is backed to the given list.
	 *
	 * @param original the backend list.
	 * @return the road path.
	 * @since 16.0
	 */
	public static RoadPath of(List<RoadSegment> original) {
		return new RoadPath(original);
	}

	@Override
	@Pure
	public Object[] toArray() {
		final RoadSegment[] tab = new RoadSegment[size()];
		toArray(tab);
		return tab;
	}

	/** Replies the direction of this path
	 * on the road segment at the given
	 * index.
	 *
	 * @param index is the index of the road segment.
	 * @return the direction of the segment at the given index
	 *     for this path.
	 * @since 4.0
	 */
	@Pure
	public Direction1D getSegmentDirectionAt(int index) {
		final RoadSegment sgmt = get(index);
		assert sgmt != null;
		final RoadConnection conn = getStartingPointFor(index);
		assert conn != null;
		if (conn.equals(sgmt.getBeginPoint())) {
			return Direction1D.SEGMENT_DIRECTION;
		}
		assert conn.equals(sgmt.getEndPoint());
		return Direction1D.REVERTED_DIRECTION;
	}

	/** Replies if the given road path is connectable to this road path.
	 *
	 * @param path a path.
	 * @return <code>true</code> if the given road path is connectable;
	 *     otherwise <code>false</code>.
	 * @since 4.0
	 * @see #isFirstPointConnectableTo(RoadPath)
	 * @see #isLastPointConnectableTo(RoadPath)
	 */
	@Pure
	public boolean isConnectableTo(RoadPath path) {
		assert path != null;
		if (path.isEmpty()) {
			return false;
		}
		RoadConnection first1 = getFirstPoint();
		RoadConnection last1 = getLastPoint();
		first1 = first1.getWrappedRoadConnection();
		last1 = last1.getWrappedRoadConnection();
		RoadConnection first2 = path.getFirstPoint();
		RoadConnection last2 = path.getLastPoint();
		first2 = first2.getWrappedRoadConnection();
		last2 = last2.getWrappedRoadConnection();
		return first1.equals(first2) || first1.equals(last2)
				|| last1.equals(first2) || last1.equals(last2);
	}

	/** Replies if the road segment of <var>path</var> (the first or the last in this order)
	 * that could be connected to the first point of the current path.
	 *
	 * @param path is the path from which a road segment should be read.
	 * @return the connectable segment from the <var>path</var>; or <code>null</code>
	 *      if no connection is possible.
	 * @since 4.0
	 */
	@Pure
	public RoadSegment getConnectableSegmentToFirstPoint(RoadPath path) {
		assert path != null;
		if (path.isEmpty()) {
			return null;
		}
		RoadConnection first1 = getFirstPoint();
		RoadConnection first2 = path.getFirstPoint();
		RoadConnection last2 = path.getLastPoint();
		first1 = first1.getWrappedRoadConnection();
		first2 = first2.getWrappedRoadConnection();
		last2 = last2.getWrappedRoadConnection();
		if (first1.equals(first2)) {
			return path.getFirstSegment();
		}
		if (first1.equals(last2)) {
			return path.getLastSegment();
		}
		return null;
	}

	/** Replies if the road segment of <var>path</var> (the first or the last in this order)
	 * that could be connected to the last point of the current path.
	 *
	 * @param path is the path from which a road segment should be read.
	 * @return the connectable segment from the <var>path</var>; or <code>null</code>
	 *     if no connection is possible.
	 * @since 4.0
	 */
	@Pure
	public RoadSegment getConnectableSegmentToLastPoint(RoadPath path) {
		assert path != null;
		if (path.isEmpty()) {
			return null;
		}
		RoadConnection last1 = getLastPoint();
		RoadConnection first2 = path.getFirstPoint();
		RoadConnection last2 = path.getLastPoint();
		last1 = last1.getWrappedRoadConnection();
		first2 = first2.getWrappedRoadConnection();
		last2 = last2.getWrappedRoadConnection();
		if (last1.equals(first2)) {
			return path.getFirstSegment();
		}
		if (last1.equals(last2)) {
			return path.getLastSegment();
		}
		return null;
	}

	/** Replies if the given road path is connectable to the first point of this road path.
	 *
	 * @param path a path
	 * @return <code>true</code> if the given road path is connectable;
	 *     otherwise <code>false</code>.
	 * @since 4.0
	 * @see #isConnectableTo(RoadPath)
	 * @see #isLastPointConnectableTo(RoadPath)
	 */
	@Pure
	public boolean isFirstPointConnectableTo(RoadPath path) {
		assert path != null;
		if (path.isEmpty()) {
			return false;
		}
		RoadConnection first1 = getFirstPoint();
		RoadConnection first2 = path.getFirstPoint();
		RoadConnection last2 = path.getLastPoint();
		first1 = first1.getWrappedRoadConnection();
		first2 = first2.getWrappedRoadConnection();
		last2 = last2.getWrappedRoadConnection();
		return first1.equals(first2) || first1.equals(last2);
	}

	/** Replies if the given road path is connectable to the last point of this road path.
	 *
	 * @param path a path.
	 * @return <code>true</code> if the given road path is connectable;
	 *     otherwise <code>false</code>.
	 * @since 4.0
	 * @see #isConnectableTo(RoadPath)
	 * @see #isFirstPointConnectableTo(RoadPath)
	 */
	@Pure
	public boolean isLastPointConnectableTo(RoadPath path) {
		assert path != null;
		if (path.isEmpty()) {
			return false;
		}
		RoadConnection last1 = getLastPoint();
		RoadConnection first2 = path.getFirstPoint();
		RoadConnection last2 = path.getLastPoint();
		last1 = last1.getWrappedRoadConnection();
		first2 = first2.getWrappedRoadConnection();
		last2 = last2.getWrappedRoadConnection();
		return last1.equals(first2) || last1.equals(last2);
	}

	/** Replies if the last segment in this path is a real cul-de-sac.
	 *
	 * <p>This function is based on {@link RoadConnection#isReallyCulDeSac()}
	 * which permits to identify the cul-de-sac ways.
	 *
	 * @return <code>true</code> if the last ponit in this path was marked
	 *     as cul-de-sac, otherwise <code>false</code>.
	 * @see RoadConnection#isReallyCulDeSac()
	 */
	@Pure
	public boolean isCulDeSacWay() {
		final RoadConnection lastPt = getLastPoint();
		return lastPt != null && lastPt.isReallyCulDeSac();
	}

	/** Replies the first cross-road point in the path.
	 *
	 * @return the cross-road point or <code>null</code> if none.
	 */
	@Pure
	public RoadConnection getFirstCrossRoad() {
		for (final RoadConnection pt : points()) {
			if (pt.getConnectedSegmentCount() > 2) {
				return pt;
			}
		}
		return null;
	}

	/** Replies the first cul-de-sac or cross-road point in the path.
	 *
	 * @return the cul-de-sac/cross-road point or <code>null</code> if none.
	 */
	@Pure
	public CrossRoad getFirstJunctionPoint() {
		RoadConnection point = getFirstPoint();
		double distance = 0;
		RoadSegment beforeSegment = null;
		RoadSegment afterSegment = null;
		int beforeSegmentIndex = -1;
		int afterSegmentIndex = -1;
		boolean found = false;
		if (point != null) {
			int index = 0;
			for (final RoadSegment sgmt : this) {
				if (found) {
					afterSegment = sgmt;
					afterSegmentIndex = index;
					//stop now
					break;
				}

				point = sgmt.getOtherSidePoint(point);
				beforeSegment = sgmt;
				beforeSegmentIndex = index;
				distance += sgmt.getLength();
				final int count = point.getConnectedSegmentCount();
				if (count != 2) {
					found = true;
				}
				++index;
			}
		}

		if (found) {
			return new CrossRoad(point, beforeSegment, beforeSegmentIndex,
					afterSegment, afterSegmentIndex, distance, distance);
		}

		return null;
	}

	/** Replies an iterator on the junction points in this path.
	 *
	 * @return an iterator on the junction points in this path.
	 */
	@Pure
	public Iterator<CrossRoad> crossRoads() {
		return new CrossRoadIterator();
	}

	/** Internal iterator.
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 14.0
	 */
	private class CrossRoadIterator implements Iterator<CrossRoad> {

		private Iterator<RoadSegment> iterator;

		private RoadConnection point;

		private RoadSegment beforeSegment;

		private int beforeSegmentIndex;

		private RoadSegment afterSegment;

		private int afterSegmentIndex;

		private double distance;

		private double distanceFromPrevious;

		private int index;

		/** Constructor.
		 */
		CrossRoadIterator() {
			this.iterator = RoadPath.this.iterator();
			this.index = -1;
			this.point = getFirstPoint();
			this.distance = 0.;
			this.distanceFromPrevious = 0.;
			searchNext();
		}

		private void searchNext() {
			boolean found = false;

			this.distanceFromPrevious = 0.;
			this.beforeSegment = null;
			this.beforeSegmentIndex = -1;
			this.afterSegmentIndex = -1;

			if (this.point != null) {
				RoadSegment sgmt;

				if (this.afterSegment != null) {
					sgmt = this.afterSegment;
				} else {
					sgmt = this.iterator.hasNext() ? this.iterator.next() : null;
					++this.index;
				}

				this.afterSegment = null;

				while (sgmt != null) {
					if (found) {
						this.afterSegment = sgmt;
						this.afterSegmentIndex = this.index;
						//stop now
						break;
					}

					this.point = sgmt.getOtherSidePoint(this.point);
					this.beforeSegment = sgmt;
					this.beforeSegmentIndex = this.index;
					final double length = sgmt.getLength();
					this.distance += length;
					this.distanceFromPrevious += length;
					final int count = this.point.getConnectedSegmentCount();
					if (count != 2) {
						found = true;
					}

					sgmt = this.iterator.hasNext() ? this.iterator.next() : null;
					++this.index;
				}
			}

			if (!found) {
				this.beforeSegment = null;
				this.afterSegment = null;
				this.beforeSegmentIndex = -1;
				this.afterSegmentIndex = -1;
			}
		}

		@Override
		@Pure
		public boolean hasNext() {
			return this.afterSegment != null || this.beforeSegment != null;
		}

		@Override
		public CrossRoad next() {
			if (this.afterSegment == null && this.beforeSegment == null) {
				throw new NoSuchElementException();
			}
			final CrossRoad cr = new CrossRoad(this.point, this.beforeSegment,
					this.beforeSegmentIndex, this.afterSegment,
					this.afterSegmentIndex, this.distance, this.distanceFromPrevious);
			searchNext();
			return cr;
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}

	} // class CrossRoadIterator

}
