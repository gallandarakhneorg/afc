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
package org.arakhne.afc.math.graph.astar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.arakhne.afc.math.continous.object2d.Point2f;
import org.arakhne.afc.math.graph.GraphPoint;


/**
 * This interface representes a graph's point.
 * 
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
class AStarNodeStub implements GraphPoint<AStarNodeStub,AStarEdgeStub>, AStarNode<AStarEdgeStub,AStarNodeStub> {

	/**
	 */
	final List<AStarEdgeStub> segments = new ArrayList<AStarEdgeStub>();

	private final String id;
	
	/** Position of the node.
	 */
	final Point2f position;
	
	private float cost = Float.NaN;
	private float eCost = Float.NaN;
	private AStarEdgeStub astarEntryConnection = null;
	
	/**
	 * @param id
	 * @param x
	 * @param y
	 */
	public AStarNodeStub(String id, float x, float y) {
		this.id = id;
		this.position = new Point2f(x,y);
	}
	
	@Override
	public String toString() {
		StringBuilder b = new StringBuilder();
		b.append("{N="); //$NON-NLS-1$
		b.append(this.id);
		b.append(";E="); //$NON-NLS-1$
		b.append(this.astarEntryConnection);
		b.append(";g="); //$NON-NLS-1$
		b.append(Float.toString(cost()));
		b.append(";h="); //$NON-NLS-1$
		b.append(Float.toString(estimatedCost()));
		b.append(";f="); //$NON-NLS-1$
		b.append(Float.toString(pathCost()));
		b.append("}"); //$NON-NLS-1$
		return b.toString();
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof AStarNodeStub)
			return this.id.equals(((AStarNodeStub)o).id);
		return false;
	}
	
	@Override
	public int hashCode() {
		return this.id.hashCode() * 7;
	}
	
	@Override
	public int compareTo(GraphPoint<AStarNodeStub, AStarEdgeStub> o) {
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
	public Iterable<AStarEdgeStub> getConnectedSegments() {
		return this.segments;
	}
	
	/** {@inheritDoc}
	 */
	@Override
	public Iterable<AStarEdgeStub> getConnectedSegmentsStartingFrom(AStarEdgeStub startingPoint) {
		return this.segments;
	}

	/** {@inheritDoc}
	 */
	@Override
	public Iterable<? extends GraphPointConnection<AStarNodeStub,AStarEdgeStub>> getConnections() {
		return new GPCIterable();
	}

	/** {@inheritDoc}
	 */
	@Override
	public Iterable<? extends GraphPointConnection<AStarNodeStub,AStarEdgeStub>> getConnectionsStartingFrom(AStarEdgeStub startingPoint) {
		return new GPCIterable();
	}

	/** {@inheritDoc}
	 */
	@Override
	public boolean isConnectedSegment(AStarEdgeStub segment) {
		return this.segments.contains(segment);
	}

	/** {@inheritDoc}
	 */
	@Override
	public boolean isFinalConnectionPoint() {
		return this.segments.size()<=1;
	}

	/** {@inheritDoc}
	 */
	@Override
	public float cost() {
		return this.cost;
	}

	/** {@inheritDoc}
	 */
	@Override
	public float setCost(float cost) {
		return this.cost = cost;
	}

	/** {@inheritDoc}
	 */
	@Override
	public float estimatedCost() {
		return this.eCost;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public float setEstimatedCost(float cost) {
		return this.eCost = cost;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public AStarEdgeStub getArrivalConnection() {
		return this.astarEntryConnection;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public AStarNodeStub getGraphPoint() {
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterable<AStarEdgeStub> getGraphSegments() {
		return Collections.unmodifiableCollection(this.segments);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public float pathCost() {
		return this.cost + this.eCost;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public AStarEdgeStub setArrivalConnection(AStarEdgeStub connection) {
		return this.astarEntryConnection = connection;
	}

	/**
     * @author $Author: sgalland$
     * @version $FullVersion$
     * @mavengroupid $GroupId$
     * @mavenartifactid $ArtifactId$
     */
	private class GPCIterable implements Iterable<GraphPointConnection<AStarNodeStub,AStarEdgeStub>> {

		/**
		 */
		public GPCIterable() {
			//
		}

		@Override
		public Iterator<GraphPoint.GraphPointConnection<AStarNodeStub, AStarEdgeStub>> iterator() {
			return new GPCIterator(AStarNodeStub.this.segments.iterator());
		}

	}

    /** 
     * @author $Author: sgalland$
     * @version $FullVersion$
     * @mavengroupid $GroupId$
     * @mavenartifactid $ArtifactId$
     */
	private class GPCIterator implements Iterator<GraphPointConnection<AStarNodeStub,AStarEdgeStub>> {

		private final Iterator<AStarEdgeStub> iter;
		
		/**
		 * @param iterator
		 */
		public GPCIterator(Iterator<AStarEdgeStub> iterator) {
			this.iter = iterator;
		}

		@Override
		public boolean hasNext() {
			return this.iter.hasNext();
		}

		@Override
		public GraphPointConnection<AStarNodeStub, AStarEdgeStub> next() {
			AStarEdgeStub s = this.iter.next();
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
	private class GPC implements GraphPointConnection<AStarNodeStub,AStarEdgeStub> {

		private final AStarEdgeStub segment;
		
		/**
		 * @param segment
		 */
		public GPC(AStarEdgeStub segment) {
			this.segment = segment;
		}

		@Override
		public AStarNodeStub getGraphPoint() {
			return AStarNodeStub.this;
		}

		@Override
		public AStarEdgeStub getGraphSegment() {
			return this.segment;
		}

		@Override
		public boolean isSegmentStartConnected() {
			return this.segment.c1==AStarNodeStub.this;
		}
		
	}
	
}
