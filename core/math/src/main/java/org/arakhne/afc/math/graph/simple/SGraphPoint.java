/* 
 * $Id$
 * 
 * Copyright (c) 2013 Christophe BOHRHAUER.
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 * This program is free software; you can redistribute it and/or modify
 */
package org.arakhne.afc.math.graph.simple;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.arakhne.afc.math.graph.GraphPoint;
import org.arakhne.afc.references.WeakArrayList;

/** This class provides a simple implementation of a graph's point
 * for a {@link SGraph}.
 * 
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 4.1
 */
public class SGraphPoint implements GraphPoint<SGraphPoint,SGraphSegment> {

	private final List<SGraphSegment> segments = new WeakArrayList<>();
	private final WeakReference<SGraph> graph;
	private List<Object> userData = null;
	
	/**
	 * @param graph is the graph in which the connection is.
	 */
	SGraphPoint(SGraph graph) {
		this.graph = new WeakReference<>(graph);
	}
	
	/** Clear the connection.
	 */
	void clear() {
		this.segments.clear();
	}
	
	/** Add the given segments in the connection.
	 * 
	 * @param segments
	 */
	void add(Iterable<SGraphSegment> segments) {
		for(SGraphSegment segment : segments) {
			this.segments.add(segment);
		}
	}
	
	/** Add the given segment in the connection.
	 * 
	 * @param segment
	 */
	void add(SGraphSegment segment) {
		this.segments.add(segment);
	}

	/** Remove the given segment from the connection.
	 * 
	 * @param segment
	 */
	void remove(SGraphSegment segment) {
		this.segments.remove(segment);
	}
	
	/** Replies the graph in which this connection is.
	 * 
	 * @return the graph in which this connection is.
	 */
	public SGraph getGraph() {
		return this.graph.get();
	}

	/** {@inheritDoc}
     */
	@Override
	public int getConnectedSegmentCount() {
		return this.segments.size();
	}

    /** {@inheritDoc}
     */
	@Override
	public Iterable<SGraphSegment> getConnectedSegments() {
		return Collections.unmodifiableList(this.segments);
	}
	
	/** {@inheritDoc}
     */
	@Override
	public boolean isConnectedSegment(SGraphSegment segment) {
		return this.segments.contains(segment);
	}

    /** {@inheritDoc}
     */
	@Override
	public boolean isFinalConnectionPoint() {
		return this.segments.size()<=1;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int compareTo(GraphPoint<SGraphPoint,SGraphSegment> o) {
		if (o==null) return Integer.MAX_VALUE;
		return hashCode() - o.hashCode();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterable<SGraphSegment> getConnectedSegmentsStartingFrom(SGraphSegment startingPoint) {
		List<SGraphSegment> l = new ArrayList<>(this.segments.size());
		int idx = 0;
		for(SGraphSegment segment : this.segments) {
			if (segment!=null) {
				if (idx>0 || segment.equals(startingPoint)) {
					l.add(idx, segment);
					++idx;
				}
				else {
					l.add(segment);
				}
			}
		}
		return l;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterable<? extends GraphPointConnection<SGraphPoint, SGraphSegment>> getConnectionsStartingFrom(
			SGraphSegment startingPoint) {
		List<PointConnection> l = new ArrayList<>(this.segments.size());
		int idx = 0;
		PointConnection connection;
		for(SGraphSegment segment : this.segments) {
			if (segment!=null) {
				
				if (equals(segment.getBeginPoint())) {
					connection = new PointConnection(segment, true);
				}
				else if (equals(segment.getEndPoint())) {
					connection = new PointConnection(segment, false);
				}
				else {
					connection = null;
				}

				if (connection!=null) {
					if (idx>0 || segment.equals(startingPoint)) {
						l.add(idx, connection);
						++idx;
					}
					else {
						l.add(connection);
					}
				}
			}
		}
		return l;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterable<? extends GraphPointConnection<SGraphPoint, SGraphSegment>> getConnections() {
		Collection<PointConnection> l = new ArrayList<>(this.segments.size());
		for(SGraphSegment segment : this.segments) {
			if (segment!=null) {
				if (equals(segment.getBeginPoint())) {
					l.add(new PointConnection(segment, true));
				}
				else if (equals(segment.getEndPoint())) {
					l.add(new PointConnection(segment, false));
				}
			}
		}
		return l;
	}
	
	/** Add a user data in the data associated to this point.
	 * 
	 * @param userData
	 * @return <code>true</code> if the data was added; otherwise <code>false</code>.
	 */
	public boolean addUserData(Object userData) {
		if (this.userData==null) {
			this.userData = new ArrayList<>();
		}
		return this.userData.add(userData);
	}

	/** Remove a user data from the data associated to this point.
	 * 
	 * @param userData
	 * @return <code>true</code> if the data was removed; otherwise <code>false</code>.
	 */
	public boolean removeUserData(Object userData) {
		return (this.userData!=null && this.userData.remove(userData));
	}
	
	/** Replies the number of user data.
	 * 
	 * @return the number of user data.
	 */
	public int getUserDataCount() {
		return (this.userData==null) ? 0 : this.userData.size();
	}
	
	/** Replies the user data at the given index.
	 * 
	 * @param index is the index of the data.
	 * @return the data
	 * @throws IndexOutOfBoundsException
	 */
	public Object getUserDataAt(int index) {
		if (this.userData==null) throw new IndexOutOfBoundsException();
		return this.userData.get(index);
	}
	
	/** Set the user data at the given index.
	 * 
	 * @param index is the index of the data.
	 * @param data is the data
	 * @throws IndexOutOfBoundsException
	 */
	public void setUserDataAt(int index, Object data) {
		if (this.userData==null) throw new IndexOutOfBoundsException();
		this.userData.set(index, data);
	}

	/** Replies all the user data.
	 * 
	 * @return an unmodifiable collection of user data.
	 */
	public Collection<Object> getAllUserData() {
		if (this.userData==null) return Collections.emptyList();
		return Collections.unmodifiableCollection(this.userData);
	}

	/**
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	private class PointConnection implements GraphPointConnection<SGraphPoint,SGraphSegment> {

		private final WeakReference<SGraphSegment> segment;
		private boolean connectedWithBeginPoint;
		
		/**
		 * @param segment is the connected segment.
		 * @param connectedWithBeginPoint is <code>true</code> if the segment is connected
		 * by its begin point, <code>false</code> if connected by its end point.
		 */
		public PointConnection(SGraphSegment segment, boolean connectedWithBeginPoint) {
			this.segment = new WeakReference<>(segment);
			this.connectedWithBeginPoint = connectedWithBeginPoint;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public SGraphSegment getGraphSegment() {
			return this.segment.get();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public SGraphPoint getGraphPoint() {
			return SGraphPoint.this;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean isSegmentStartConnected() {
			return this.connectedWithBeginPoint;
		}
		
	} // class PointConnection
	
}
