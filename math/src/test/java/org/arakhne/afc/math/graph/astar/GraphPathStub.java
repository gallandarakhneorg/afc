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

import org.arakhne.afc.math.graph.GraphPath;


/**
 * This interface representes a graph's point.
 * 
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
class GraphPathStub extends GraphPath<GraphPathStub,SegmentStub,ConnectionStub> {

	/**
	 */
	public GraphPathStub() {
		super();
	}
	
	/**
	 * @param segment
	 * @param point
	 */
	public GraphPathStub(SegmentStub segment, ConnectionStub point) {
		super(segment, point);
	}

}
