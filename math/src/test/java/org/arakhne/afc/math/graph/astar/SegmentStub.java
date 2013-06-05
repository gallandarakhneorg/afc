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

import org.arakhne.afc.math.graph.GraphSegment;

/** This interface representes a graph's segment.
 * 
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
class SegmentStub implements GraphSegment<SegmentStub,ConnectionStub> {

	/**
	 */
	public final ConnectionStub c1;
	/**
	 */
	public final ConnectionStub c2;
	
	private final String id;
	
	/**
	 * @param id
	 * @param c1
	 * @param c2
	 */
	public SegmentStub(String id, ConnectionStub c1, ConnectionStub c2) {
		this.id = id;
		this.c1 = c1;
		this.c2 = c2;
		this.c1.segments.add(this);
		this.c2.segments.add(this);
	}
	
	@Override
	public String toString() {
		return this.id;
	}

	/** {@inheritDoc}
	 */
	@Override
	public ConnectionStub getBeginPoint() {
		return this.c1;
	}

	/** {@inheritDoc}
	 */
	@Override
	public ConnectionStub getEndPoint() {
		return this.c2;
	}

	/** {@inheritDoc}
	 */
	@Override
	public ConnectionStub getOtherSidePoint(ConnectionStub point) {
		if (point==this.c1) return this.c2;
		return this.c1;
	}
	
	/** {@inheritDoc}
	 */
	@Override
	public float getLength() {
		return 1.f;
	}

}
