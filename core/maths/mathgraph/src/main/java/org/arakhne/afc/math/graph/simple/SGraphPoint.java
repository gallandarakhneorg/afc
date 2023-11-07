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

package org.arakhne.afc.math.graph.simple;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.google.common.collect.Lists;
import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.graph.GraphPoint;
import org.arakhne.afc.references.WeakArrayList;

/** This class provides a simple implementation of a graph's point
 * for a {@link SGraph}.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public class SGraphPoint implements GraphPoint<SGraphPoint, SGraphSegment> {

	private final List<SGraphSegment> segments = new WeakArrayList<>();

	private final WeakReference<SGraph> graph;

	private List<Object> userData;

	/** Constructor.
	 * @param graph1 is the graph in which the connection is.
	 */
	SGraphPoint(SGraph graph1) {
		this.graph = new WeakReference<>(graph1);
	}

	/** Clear the connection.
	 */
	void clear() {
		this.segments.clear();
	}

	/** Add the given segments in the connection.
	 *
	 * @param segments the segments to add.
	 */
	void add(Iterable<SGraphSegment> segments) {
		for (final SGraphSegment segment : segments) {
			this.segments.add(segment);
		}
	}

	/** Add the given segment in the connection.
	 *
	 * @param segment the segment to add.
	 */
	void add(SGraphSegment segment) {
		this.segments.add(segment);
	}

	/** Remove the given segment from the connection.
	 *
	 * @param segment the segment to remove.
	 */
	void remove(SGraphSegment segment) {
		this.segments.remove(segment);
	}

	/** Replies the graph in which this connection is.
	 *
	 * @return the graph in which this connection is.
	 */
	@Pure
	public SGraph getGraph() {
		return this.graph.get();
	}

	@Pure
	@Override
	public int getConnectedSegmentCount() {
		return this.segments.size();
	}

	@Pure
	@Override
	public Iterable<SGraphSegment> getConnectedSegments() {
		return Collections.unmodifiableList(this.segments);
	}

	@Pure
	@Override
	public boolean isConnectedSegment(SGraphSegment segment) {
		return this.segments.contains(segment);
	}

	@Pure
	@Override
	public boolean isFinalConnectionPoint() {
		return this.segments.size() <= 1;
	}

	@Pure
	@Override
	public int compareTo(GraphPoint<SGraphPoint, SGraphSegment> pt) {
		if (pt == null) {
			return Integer.MAX_VALUE;
		}
		return hashCode() - pt.hashCode();
	}

	private List<SGraphSegment> computeConnectedSegmentsStartingFrom(SGraphSegment startingPoint) {
		final List<SGraphSegment> list = new ArrayList<>(this.segments.size());
		int idx = 0;
		for (final SGraphSegment segment : this.segments) {
			if (segment != null) {
				if (idx > 0 || segment.equals(startingPoint)) {
					list.add(idx, segment);
					++idx;
				} else {
					list.add(segment);
				}
			}
		}
		return list;
	}

	@Pure
	@Override
	public Iterable<SGraphSegment> getConnectedSegmentsStartingFrom(SGraphSegment startingPoint) {
		return computeConnectedSegmentsStartingFrom(startingPoint);
	}

	@Override
	public Iterable<SGraphSegment> getConnectedSegmentsStartingFromInReverseOrder(SGraphSegment startingPoint) {
		return Lists.reverse(computeConnectedSegmentsStartingFrom(startingPoint));
	}

	@Pure
	@Override
	public Iterable<? extends GraphPointConnection<SGraphPoint, SGraphSegment>> getConnectionsStartingFrom(
			SGraphSegment startingPoint) {
		final List<PointConnection> list = new ArrayList<>(this.segments.size());
		int idx = 0;
		PointConnection connection;
		for (final SGraphSegment segment : this.segments) {
			if (segment != null) {
				if (equals(segment.getBeginPoint())) {
					connection = new PointConnection(segment, true);
				} else if (equals(segment.getEndPoint())) {
					connection = new PointConnection(segment, false);
				} else {
					connection = null;
				}

				if (connection != null) {
					if (idx > 0 || segment.equals(startingPoint)) {
						list.add(idx, connection);
						++idx;
					} else {
						list.add(connection);
					}
				}
			}
		}
		return list;
	}

	@Pure
	@Override
	public Iterable<? extends GraphPointConnection<SGraphPoint, SGraphSegment>> getConnectionsStartingFromInReverseOrder(
			SGraphSegment startingPoint) {
		final List<PointConnection> list = new ArrayList<>(this.segments.size());
		PointConnection connection;
		for (final SGraphSegment segment : this.segments) {
			if (segment != null) {
				if (equals(segment.getBeginPoint())) {
					connection = new PointConnection(segment, true);
				} else if (equals(segment.getEndPoint())) {
					connection = new PointConnection(segment, false);
				} else {
					connection = null;
				}

				if (connection != null) {
					if (!list.isEmpty() || segment.equals(startingPoint)) {
						list.add(0, connection);
					} else {
						list.add(connection);
					}
				}
			}
		}
		return list;
	}

	@Pure
	@Override
	public Iterable<? extends GraphPointConnection<SGraphPoint, SGraphSegment>> getConnections() {
		final Collection<PointConnection> list = new ArrayList<>(this.segments.size());
		for (final SGraphSegment segment : this.segments) {
			if (segment != null) {
				if (equals(segment.getBeginPoint())) {
					list.add(new PointConnection(segment, true));
				} else if (equals(segment.getEndPoint())) {
					list.add(new PointConnection(segment, false));
				}
			}
		}
		return list;
	}

	/** Add a user data in the data associated to this point.
	 *
	 * @param userData the user data to add.
	 * @return {@code true} if the data was added; otherwise {@code false}.
	 */
	public boolean addUserData(Object userData) {
		if (this.userData == null) {
			this.userData = new ArrayList<>();
		}
		return this.userData.add(userData);
	}

	/** Remove a user data from the data associated to this point.
	 *
	 * @param userData the user data to remove.
	 * @return {@code true} if the data was removed; otherwise {@code false}.
	 */
	public boolean removeUserData(Object userData) {
		return this.userData != null && this.userData.remove(userData);
	}

	/** Replies the number of user data.
	 *
	 * @return the number of user data.
	 */
	@Pure
	public int getUserDataCount() {
		return (this.userData == null) ? 0 : this.userData.size();
	}

	/** Replies the user data at the given index.
	 *
	 * @param index is the index of the data.
	 * @return the data
	 * @throws IndexOutOfBoundsException if the given index is out of the range.
	 */
	@Pure
	public Object getUserDataAt(int index) {
		if (this.userData == null) {
			throw new IndexOutOfBoundsException();
		}
		return this.userData.get(index);
	}

	/** Set the user data at the given index.
	 *
	 * @param index is the index of the data.
	 * @param data is the data.
	 * @throws IndexOutOfBoundsException if the given index is out of the range.
	 */
	public void setUserDataAt(int index, Object data) {
		if (this.userData == null) {
			throw new IndexOutOfBoundsException();
		}
		this.userData.set(index, data);
	}

	/** Replies all the user data.
	 *
	 * @return an unmodifiable collection of user data.
	 */
	@Pure
	public Collection<Object> getAllUserData() {
		if (this.userData == null) {
			return Collections.emptyList();
		}
		return Collections.unmodifiableCollection(this.userData);
	}

	/** Internal connection implementation.
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 13.0
	 */
	private class PointConnection implements GraphPointConnection<SGraphPoint, SGraphSegment> {

		private final WeakReference<SGraphSegment> segment;

		private boolean connectedWithBeginPoint;

		/** Constructor.
		 * @param segment1 is the connected segment.
		 * @param connectedWithBeginPoint1 is {@code true} if the segment is connected
		 *     by its begin point, {@code false} if connected by its end point.
		 */
		PointConnection(SGraphSegment segment1, boolean connectedWithBeginPoint1) {
			this.segment = new WeakReference<>(segment1);
			this.connectedWithBeginPoint = connectedWithBeginPoint1;
		}

		@Pure
		@Override
		public SGraphSegment getGraphSegment() {
			return this.segment.get();
		}

		@Pure
		@Override
		public SGraphPoint getGraphPoint() {
			return SGraphPoint.this;
		}

		@Pure
		@Override
		public boolean isSegmentStartConnected() {
			return this.connectedWithBeginPoint;
		}

	}

}
