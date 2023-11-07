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

package org.arakhne.afc.gis.road.primitive;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.TreeSet;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.geometry.coordinatesystem.CoordinateSystem2D;

/** This class provides the implementation
 * of a road connection wrapped to an other road connection
 * and that is excluding several road segments of the
 * wrapped connection.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
public class ExclusionRoadConnection extends AbstractWrapRoadConnection {

	private Set<RoadSegment> excludes;

	/** Constructor.
	 * @param con is the wrapped connection.
	 * @param excludedSegments are the segments excluded from the collection
	 *     of the connected segments.
	 */
	public ExclusionRoadConnection(RoadConnection con, RoadSegment... excludedSegments) {
		super(con);
		this.excludes = new TreeSet<>(new RoadSegmentComparator());
		for (final RoadSegment s : excludedSegments) {
			this.excludes.add(s.getWrappedRoadSegment());
		}
	}

	/** Replies if the given road segment is excluded.
	 *
	 * @param segment a segment.
	 * @return {@code true} if the road segment is excluded;
	 *     otherwise {@code false}.
	 */
	@Pure
	public boolean isExcludedRoadSegment(RoadSegment segment) {
		return this.excludes.contains(segment.getWrappedRoadSegment());
	}

	@Override
	@Pure
	public RoadSegment getConnectedSegment(int index) throws ArrayIndexOutOfBoundsException {
		final int max = this.connection.get().getConnectedSegmentCount();
		RoadSegment seg = null;
		for (int i = 0, j = 0; seg == null && i < max; ++i) {
			final RoadSegment c = this.connection.get().getConnectedSegment(i);
			if (!isExcludedRoadSegment(c)) {
				if (j == index) {
					seg = c;
				}
				++j;
			}
		}
		return seg;
	}

	@Override
	@Pure
	public int getConnectedSegmentCount() {
		return this.connection.get().getConnectedSegmentCount() - this.excludes.size();
	}

	@Override
	@Pure
	public Iterable<RoadSegment> getConnectedSegments() {
		return () -> new ExclusionIterator(this.connection.get().getConnectedSegments().iterator());
	}

	@Override
	@Pure
	public Iterable<RoadSegment> getConnectedSegmentsStartingFrom(RoadSegment startingSegment) {
		return () -> new ExclusionIterator(this.connection.get().getConnectedSegmentsStartingFrom(startingSegment).iterator());
	}

	@Override
	public Iterable<RoadSegment> getConnectedSegmentsStartingFromInReverseOrder(RoadSegment startingSegment) {
		return () -> new ExclusionIterator(this.connection.get().getConnectedSegmentsStartingFromInReverseOrder(startingSegment).iterator());
	}

	@Override
	@Pure
	public Iterable<? extends GraphPointConnection<RoadConnection, RoadSegment>> getConnections() {
		return () -> new ExclusionIterator2(this.connection.get().getConnections().iterator());
	}

	@Override
	@Pure
	public Iterable<? extends GraphPointConnection<RoadConnection, RoadSegment>> getConnectionsStartingFrom(
			RoadSegment startingPoint) {
		return () -> new ExclusionIterator2(this.connection.get().getConnectionsStartingFrom(startingPoint).iterator());
	}

	@Override
	@Pure
	public Iterable<? extends GraphPointConnection<RoadConnection, RoadSegment>> getConnectionsStartingFromInReverseOrder(
			RoadSegment startingPoint) {
		return () -> new ExclusionIterator2(this.connection.get().getConnectionsStartingFromInReverseOrder(startingPoint).iterator());
	}

	@Override
	@Pure
	public RoadSegment getOtherSideSegment(RoadSegment refSegment) {
		if (isExcludedRoadSegment(refSegment)) {
			return null;
		}
		final RoadSegment other = this.connection.get().getOtherSideSegment(refSegment);
		if (isExcludedRoadSegment(other)) {
			return null;
		}
		return other;
	}

	@Override
	@Pure
	public boolean isConnectedSegment(RoadSegment segment) {
		if (isExcludedRoadSegment(segment)) {
			return false;
		}
		return this.connection.get().isConnectedSegment(segment);
	}

	@Override
	@Pure
	public Iterator<RoadSegment> toClockwiseIterator(RoadSegment startSegment, RoadSegment endSegment) {
		return new ExclusionIterator(
				this.connection.get().toClockwiseIterator(startSegment, endSegment));
	}

	@Override
	@Pure
	public Iterator<RoadSegment> toClockwiseIterator(
			RoadSegment startSegment,
			Boolean startSegmentConnectedByItsStart,
			RoadSegment endSegment, Boolean endSegmentConnectedByItsStart) {
		return new ExclusionIterator(
				this.connection.get().toClockwiseIterator(
						startSegment,
						startSegmentConnectedByItsStart,
						endSegment,
						endSegmentConnectedByItsStart));
	}

	@Override
	@Pure
	public Iterator<RoadSegment> toClockwiseIterator(
			RoadSegment startSegment, RoadSegment endSegment,
			ClockwiseBoundType boundType) {
		return new ExclusionIterator(
				this.connection.get().toClockwiseIterator(
						startSegment,
						endSegment,
						boundType));
	}

	@Override
	@Pure
	public Iterator<RoadSegment> toClockwiseIterator(
			RoadSegment startSegment,
			Boolean startSegmentConnectedByItsStart,
			RoadSegment endSegment, Boolean endSegmentConnectedByItsStart,
			ClockwiseBoundType boundType) {
		return new ExclusionIterator(
				this.connection.get().toClockwiseIterator(
						startSegment,
						startSegmentConnectedByItsStart,
						endSegment,
						endSegmentConnectedByItsStart,
						boundType));
	}

	@Override
	@Pure
	public Iterator<RoadSegment> toClockwiseIterator(
			RoadSegment startSegment) {
		return new ExclusionIterator(
				this.connection.get().toClockwiseIterator(
						startSegment));
	}

	@Override
	@Pure
	public Iterator<RoadSegment> toClockwiseIterator(
			RoadSegment startSegment, ClockwiseBoundType boundType) {
		return new ExclusionIterator(
				this.connection.get().toClockwiseIterator(
						startSegment,
						boundType));
	}

	@Override
	@Pure
	public Iterator<RoadSegment> toClockwiseIterator(
			RoadSegment startSegment, RoadSegment endSegment,
			CoordinateSystem2D system) {
		return new ExclusionIterator(
				this.connection.get().toClockwiseIterator(
						startSegment,
						endSegment,
						system));
	}

	@Override
	@Pure
	public Iterator<RoadSegment> toClockwiseIterator(
			RoadSegment startSegment,
			Boolean startSegmentConnectedByItsStart,
			RoadSegment endSegment, Boolean endSegmentConnectedByItsStart,
			CoordinateSystem2D system) {
		return new ExclusionIterator(
				this.connection.get().toClockwiseIterator(
						startSegment,
						startSegmentConnectedByItsStart,
						endSegment,
						endSegmentConnectedByItsStart,
						system));
	}

	@Override
	@Pure
	public Iterator<RoadSegment> toClockwiseIterator(
			RoadSegment startSegment, RoadSegment endSegment,
			ClockwiseBoundType boundType, CoordinateSystem2D system) {
		return new ExclusionIterator(
				this.connection.get().toClockwiseIterator(
						startSegment,
						endSegment,
						boundType,
						system));
	}

	@Override
	@Pure
	public Iterator<RoadSegment> toClockwiseIterator(
			RoadSegment startSegment,
			Boolean startSegmentConnectedByItsStart,
			RoadSegment endSegment, Boolean endSegmentConnectedByItsStart,
			ClockwiseBoundType boundType, CoordinateSystem2D system) {
		return new ExclusionIterator(
				this.connection.get().toClockwiseIterator(
						startSegment,
						startSegmentConnectedByItsStart,
						endSegment,
						endSegmentConnectedByItsStart,
						boundType,
						system));
	}

	@Override
	@Pure
	public Iterator<RoadSegment> toClockwiseIterator(
			RoadSegment startSegment, CoordinateSystem2D system) {
		return new ExclusionIterator(
				this.connection.get().toClockwiseIterator(
						startSegment,
						system));
	}

	@Override
	@Pure
	public Iterator<RoadSegment> toClockwiseIterator(
			RoadSegment startSegment, ClockwiseBoundType boundType,
			CoordinateSystem2D system) {
		return new ExclusionIterator(
				this.connection.get().toClockwiseIterator(
						startSegment,
						boundType,
						system));
	}

	@Override
	@Pure
	public Iterator<RoadSegment> toCounterclockwiseIterator(
			RoadSegment startSegment, RoadSegment endSegment) {
		return new ExclusionIterator(
				this.connection.get().toCounterclockwiseIterator(
						startSegment,
						endSegment));
	}

	@Override
	@Pure
	public Iterator<RoadSegment> toCounterclockwiseIterator(
			RoadSegment startSegment,
			Boolean startSegmentConnectedByItsStart,
			RoadSegment endSegment, Boolean endSegmentConnectedByItsStart) {
		return new ExclusionIterator(
				this.connection.get().toCounterclockwiseIterator(
						startSegment,
						startSegmentConnectedByItsStart,
						endSegment,
						endSegmentConnectedByItsStart));
	}

	@Override
	@Pure
	public Iterator<RoadSegment> toCounterclockwiseIterator(
			RoadSegment startSegment, RoadSegment endSegment,
			ClockwiseBoundType boundType) {
		return new ExclusionIterator(
				this.connection.get().toCounterclockwiseIterator(
						startSegment,
						endSegment,
						boundType));
	}

	@Override
	@Pure
	public Iterator<RoadSegment> toCounterclockwiseIterator(
			RoadSegment startSegment,
			Boolean startSegmentConnectedByItsStart,
			RoadSegment endSegment, Boolean endSegmentConnectedByItsStart,
			ClockwiseBoundType boundType) {
		return new ExclusionIterator(
				this.connection.get().toCounterclockwiseIterator(
						startSegment,
						startSegmentConnectedByItsStart,
						endSegment,
						endSegmentConnectedByItsStart,
						boundType));
	}

	@Override
	@Pure
	public Iterator<RoadSegment> toCounterclockwiseIterator(
			RoadSegment startSegment) {
		return new ExclusionIterator(
				this.connection.get().toCounterclockwiseIterator(
						startSegment));
	}

	@Override
	@Pure
	public Iterator<RoadSegment> toCounterclockwiseIterator(
			RoadSegment startSegment, ClockwiseBoundType boundType) {
		return new ExclusionIterator(
				this.connection.get().toCounterclockwiseIterator(
						startSegment,
						boundType));
	}

	@Override
	@Pure
	public Iterator<RoadSegment> toCounterclockwiseIterator(
			RoadSegment startSegment, RoadSegment endSegment,
			CoordinateSystem2D system) {
		return new ExclusionIterator(
				this.connection.get().toCounterclockwiseIterator(
						startSegment,
						endSegment,
						system));
	}

	@Override
	@Pure
	public Iterator<RoadSegment> toCounterclockwiseIterator(
			RoadSegment startSegment,
			Boolean startSegmentConnectedByItsStart,
			RoadSegment endSegment, Boolean endSegmentConnectedByItsStart,
			CoordinateSystem2D system) {
		return new ExclusionIterator(
				this.connection.get().toCounterclockwiseIterator(
						startSegment,
						startSegmentConnectedByItsStart,
						endSegment,
						endSegmentConnectedByItsStart,
						system));
	}

	@Override
	@Pure
	public Iterator<RoadSegment> toCounterclockwiseIterator(
			RoadSegment startSegment, RoadSegment endSegment,
			ClockwiseBoundType boundType, CoordinateSystem2D system) {
		return new ExclusionIterator(
				this.connection.get().toCounterclockwiseIterator(
						startSegment,
						endSegment,
						boundType,
						system));
	}

	@Override
	@Pure
	public Iterator<RoadSegment> toCounterclockwiseIterator(
			RoadSegment startSegment,
			Boolean startSegmentConnectedByItsStart,
			RoadSegment endSegment, Boolean endSegmentConnectedByItsStart,
			ClockwiseBoundType boundType, CoordinateSystem2D system) {
		return new ExclusionIterator(
				this.connection.get().toCounterclockwiseIterator(
						startSegment,
						startSegmentConnectedByItsStart,
						endSegment,
						endSegmentConnectedByItsStart,
						boundType,
						system));
	}

	@Override
	@Pure
	public Iterator<RoadSegment> toCounterclockwiseIterator(
			RoadSegment startSegment, CoordinateSystem2D system) {
		return new ExclusionIterator(
				this.connection.get().toCounterclockwiseIterator(
						startSegment,
						system));
	}

	@Override
	@Pure
	public Iterator<RoadSegment> toCounterclockwiseIterator(
			RoadSegment startSegment, ClockwiseBoundType boundType,
			CoordinateSystem2D system) {
		return new ExclusionIterator(
				this.connection.get().toCounterclockwiseIterator(
						startSegment,
						boundType,
						system));
	}

	/** Internal iterator.
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 14.0
	 */
	private class ExclusionIterator implements Iterator<RoadSegment> {

		private final Iterator<RoadSegment> iterator;

		private RoadSegment next;

		/** Constructor.
		 *
		 * @param iterator the original iterator.
		 */
		ExclusionIterator(Iterator<RoadSegment> iterator) {
			this.iterator = iterator;
			searchNext();
		}

		private void searchNext() {
			this.next = null;
			while (this.next == null && this.iterator.hasNext()) {
				final RoadSegment s = this.iterator.next();
				if (!isExcludedRoadSegment(s)) {
					this.next = s;
				}
			}
		}

		@Override
		@Pure
		public boolean hasNext() {
			return this.next != null;
		}

		@Override
		public RoadSegment next() {
			final RoadSegment n = this.next;
			if (n == null) {
				throw new NoSuchElementException();
			}
			searchNext();
			return n;
		}

	}

	/** Internal iterator.
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 14.0
	 */
	private class ExclusionIterator2 implements Iterator<GraphPointConnection<RoadConnection, RoadSegment>> {

		private final Iterator<? extends GraphPointConnection<RoadConnection, RoadSegment>> iterator;

		private GraphPointConnection<RoadConnection, RoadSegment> next;

		/** Constructor.
		 *
		 * @param iterator the original iterator.
		 */
		ExclusionIterator2(Iterator<? extends GraphPointConnection<RoadConnection, RoadSegment>> iterator) {
			this.iterator = iterator;
			searchNext();
		}

		private void searchNext() {
			this.next = null;
			while (this.next == null && this.iterator.hasNext()) {
				final GraphPointConnection<RoadConnection, RoadSegment> s = this.iterator.next();
				if (!isExcludedRoadSegment(s.getGraphSegment())) {
					this.next = s;
				}
			}
		}

		@Override
		@Pure
		public boolean hasNext() {
			return this.next != null;
		}

		@Override
		public GraphPointConnection<RoadConnection, RoadSegment> next() {
			final GraphPointConnection<RoadConnection, RoadSegment> n = this.next;
			if (n == null) {
				throw new NoSuchElementException();
			}
			searchNext();
			return n;
		}

	}

}

