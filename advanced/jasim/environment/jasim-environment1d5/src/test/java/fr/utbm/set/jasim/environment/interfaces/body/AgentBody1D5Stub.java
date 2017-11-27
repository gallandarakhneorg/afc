/*
 * $Id$
 * 
 * Copyright (c) 2006-10, Multiagent Team - Systems and Transportation Laboratory (SeT)
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of the Systems and Transportation Laboratory ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with the SeT.
 * 
 * http://www.multiagent.fr/
 */
package fr.utbm.set.jasim.environment.interfaces.body;

import java.util.UUID;

import fr.utbm.set.geom.bounds.bounds1d5.CombinableBounds1D5;
import fr.utbm.set.gis.road.primitive.RoadConnection;
import fr.utbm.set.gis.road.primitive.RoadSegment;
import fr.utbm.set.jasim.environment.interfaces.body.influences.Influence;
import fr.utbm.set.jasim.environment.interfaces.body.perceptions.Perception;

/**
 * This class describes the body of an situated agent inside a 1D5 environment.
 * The body is the only available interaction mean between
 * an agent and the environment.
 * 
 * @param <INF> The type of influence this body may receive and forward to the collector
 * @param <PT> The type of perception this body may receive
 * @param <B> The type of Bound associated to the 3D representation of this body in its environment
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
class AgentBody1D5Stub<INF extends Influence, 
		                           PT extends Perception, 
		                           B extends CombinableBounds1D5<RoadSegment>>
extends AgentBody1D5<INF,PT,B> {

	private static final long serialVersionUID = -7942911423150253869L;

	/**
	 * Builds a new AgentBody1D5.
	 * 
	 * @param ownerIdentifier
	 *            is the address of the holon owning this body
	 * @param perceptionType is the type of supported perceptions.
	 * @param bounds
	 * @param roadEntry
	 *            the entry of the body on the segment
	 */
	public AgentBody1D5Stub(UUID ownerIdentifier,
			Class<PT> perceptionType, B bounds, RoadConnection roadEntry) {
		super(ownerIdentifier, perceptionType, bounds, roadEntry);
	}

}
