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

import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.fp.Point2fp;
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
	final List<AStarEdgeStub> segments = new ArrayList<>();

	private final String id;
	
	/** Position of the node.
	 */
	final Point2D position;
	
	private double cost = Double.NaN;
	private double eCost = Double.NaN;
	private AStarEdgeStub astarEntryConnection = null;
	
	/**
	 * @param id1
	 * @param x
	 * @param y
	 */
	public AStarNodeStub(String id1, double x, double y) {
		this.id = id1;
		this.position = new Point2fp(x,y);
	}
	
	@Override
	public String toString() {
		StringBuilder b = new StringBuilder();
		b.append("{N="); //$NON-NLS-1$
		b.append(this.id);
		b.append(";E="); //$NON-NLS-1$
		b.append(this.astarEntryConnection);
		b.append(";g="); //$NON-NLS-1$
		b.append(Double.toString(cost()));
		b.append(";h="); //$NON-NLS-1$
		b.append(Double.toString(estimatedCost()));
		b.append(";f="); //$NON-NLS-1$
		b.append(Double.toString(pathCost()));
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
	public double cost() {
		return this.cost;
	}

	/** {@inheritDoc}
	 */
	@Override
	public double setCost(double cost1) {
		return this.cost = cost1;
	}

	/** {@inheritDoc}
	 */
	@Override
	public double estimatedCost() {
		return this.eCost;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public double setEstimatedCost(double cost1) {
		return this.eCost = cost1;
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
	public double pathCost() {
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
		 * @param segment1
		 */
		public GPC(AStarEdgeStub segment1) {
			this.segment = segment1;
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
