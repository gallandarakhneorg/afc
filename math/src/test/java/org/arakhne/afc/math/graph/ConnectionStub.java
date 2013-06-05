/* 
 * $Id$
 * 
 * Copyright (c) 2005-10, Multiagent Team,
 * Laboratoire Systemes et Transports,
 * Universite de Technologie de Belfort-Montbeliard.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of the Laboratoire Systemes et Transports
 * of the Universite de Technologie de Belfort-Montbeliard ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with the SeT.
 *
 * http://www.multiagent.fr/
 */
package org.arakhne.afc.math.graph;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * This interface representes a graph's point.
 * 
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
class ConnectionStub implements GraphPoint<ConnectionStub,SegmentStub> {

	/**
	 */
	final List<SegmentStub> segments = new ArrayList<SegmentStub>();
	
	private final String id;
	
	/**
	 * @param id
	 */
	public ConnectionStub(String id) {
		this.id = id;
	}
	
	@Override
	public String toString() {
		return this.id;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof ConnectionStub)
			return this.id.equals(((ConnectionStub)o).id);
		return false;
	}
	
	@Override
	public int hashCode() {
		return this.id.hashCode() * 7;
	}
	
	@Override
	public int compareTo(GraphPoint<ConnectionStub, SegmentStub> o) {
		return o.hashCode() - hashCode();
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
	public Iterable<SegmentStub> getConnectedSegments() {
		return this.segments;
	}
	
	/** {@inheritDoc}
	 */
	@Override
	public Iterable<SegmentStub> getConnectedSegmentsStartingFrom(SegmentStub startingPoint) {
		return this.segments;
	}

	/** {@inheritDoc}
	 */
	@Override
	public Iterable<? extends GraphPointConnection<ConnectionStub,SegmentStub>> getConnections() {
		return new GPCIterable();
	}

	/** {@inheritDoc}
	 */
	@Override
	public Iterable<? extends GraphPointConnection<ConnectionStub,SegmentStub>> getConnectionsStartingFrom(SegmentStub startingPoint) {
		return new GPCIterable();
	}

	/** {@inheritDoc}
	 */
	@Override
	public boolean isConnectedSegment(SegmentStub segment) {
		return this.segments.contains(segment);
	}

	/** {@inheritDoc}
	 */
	@Override
	public boolean isFinalConnectionPoint() {
		return this.segments.size()<=1;
	}

    /**
     * @author $Author: sgalland$
     * @version $FullVersion$
     * @mavengroupid $GroupId$
     * @mavenartifactid $ArtifactId$
     */
	private class GPCIterable implements Iterable<GraphPointConnection<ConnectionStub,SegmentStub>> {

		/**
		 */
		public GPCIterable() {
			//
		}

		@Override
		public Iterator<GraphPoint.GraphPointConnection<ConnectionStub, SegmentStub>> iterator() {
			return new GPCIterator(ConnectionStub.this.segments.iterator());
		}

	}

    /** 
     * @author $Author: sgalland$
     * @version $FullVersion$
     * @mavengroupid $GroupId$
     * @mavenartifactid $ArtifactId$
     */
	private class GPCIterator implements Iterator<GraphPointConnection<ConnectionStub,SegmentStub>> {

		private final Iterator<SegmentStub> iter;
		
		/**
		 * @param iterator
		 */
		public GPCIterator(Iterator<SegmentStub> iterator) {
			this.iter = iterator;
		}

		@Override
		public boolean hasNext() {
			return this.iter.hasNext();
		}

		@Override
		public GraphPointConnection<ConnectionStub, SegmentStub> next() {
			SegmentStub s = this.iter.next();
			return new GPC(s);
		}

		@Override
		public void remove() {
			this.iter.remove();
		}

	}

    /** 
     * @author $Author: sgalland$
     * @version $FullVersion$
     * @mavengroupid $GroupId$
     * @mavenartifactid $ArtifactId$
     */
	private class GPC implements GraphPointConnection<ConnectionStub,SegmentStub> {

		private final SegmentStub segment;
		
		/**
		 * @param segment
		 */
		public GPC(SegmentStub segment) {
			this.segment = segment;
		}

		@Override
		public ConnectionStub getGraphPoint() {
			return ConnectionStub.this;
		}

		@Override
		public SegmentStub getGraphSegment() {
			return this.segment;
		}

		@Override
		public boolean isSegmentStartConnected() {
			return this.segment.c1==ConnectionStub.this;
		}
		
	}
	
}
