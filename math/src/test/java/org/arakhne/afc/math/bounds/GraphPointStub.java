/* 
 * $Id$
 * 
 * Copyright (c) 2006-10, Multiagent Team,
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
package org.arakhne.afc.math.bounds;

import java.util.ArrayList;
import java.util.List;

import org.arakhne.afc.math.continous.object2d.Point2f;
import org.arakhne.afc.math.graph.GraphPoint;


/**
 * Stub for GraphSegment class.
 * 
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class GraphPointStub implements GraphPoint<GraphPointStub, GraphSegmentStub> {

	/**
	 */
	List<GraphSegmentStub> segments;
	
	private final Point2f position;

	/**
	 */
	public GraphPointStub() {
		this.position = new Point2f((Math.random()-Math.random())*500.,(Math.random()-Math.random())*500.);
		this.segments = new ArrayList<GraphSegmentStub>();
	}
	
	/**
	 * @param p
	 */
	public GraphPointStub(Point2f p) {
		this.position = p;
		this.segments = new ArrayList<GraphSegmentStub>();
	}

	/**
	 * @param x
	 * @param y
	 */
	public GraphPointStub(float x, float y) {
		this.position = new Point2f(x,y);
		this.segments = new ArrayList<GraphSegmentStub>();
	}

	@Override
	public Iterable<GraphSegmentStub> getConnectedSegments() {
		return this.segments;
	}

	@Override
	public Iterable<? extends GraphPointConnection<GraphPointStub, GraphSegmentStub>> getConnections() {
		return null;
	}

	@Override
	public Iterable<? extends GraphPointConnection<GraphPointStub, GraphSegmentStub>> getConnectionsStartingFrom(
			GraphSegmentStub startingPoint) {
		return null;
	}

	@Override
	public Iterable<GraphSegmentStub> getConnectedSegmentsStartingFrom(
			GraphSegmentStub startingPoint) {
		return this.segments;
	}


	@Override
	public boolean isConnectedSegment(GraphSegmentStub arg0) {
		return this.segments.contains(arg0);
	}

	@Override
	public boolean isFinalConnectionPoint() {
		return this.segments.size()<=1;
	}
	
	/** Replies the position.
	 * @return the position.
	 */
	public Point2f getPosition() {
		return this.position;
	}

	@Override
	public int compareTo(GraphPoint<GraphPointStub, GraphSegmentStub> o) {
		return o.hashCode() - hashCode();
	}

	@Override
	public int getConnectedSegmentCount() {
		return this.segments.size();
	}

}
