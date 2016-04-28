/* 
 * $Id$
 * 
 * Copyright (c) 2011, Multiagent Team,
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

/**
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@SuppressWarnings("all")
public class ConnectionHeuristicStub implements AStarHeuristic<ConnectionStub> {

	/**
	 */
	public ConnectionHeuristicStub() {
		//
	}
	
	/** {@inheritDoc}
	 */
	@Override
	public double evaluate(ConnectionStub p1, ConnectionStub p2) {
		return p1.position.getDistance(p2.position);
	}

}
