/*
* $Id$
 * 
 * Copyright (c) 2006-09, Multiagent Team - Systems and Transportation Laboratory (SeT)
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
package fr.utbm.set.jasim.environment.model.world;

import java.util.UUID;

import fr.utbm.set.geom.bounds.bounds3d.CombinableBounds3D;
import fr.utbm.set.jasim.environment.interfaces.body.AgentBody3D;
import fr.utbm.set.jasim.environment.interfaces.body.influences.Influence;
import fr.utbm.set.jasim.environment.interfaces.body.perceptions.Perception;
import fr.utbm.set.jasim.environment.model.world.Mesh3D;

/**
 * Stub for agent body 3D.
 * 
 * @param <INF> The type of influence this body may receive and forward to the collector
 * @param <PT> The type of perception this body may receive
 * @param <B> The type of Bound associated to the 3D representation of this body in its environment
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
class AgentBody3DStub<INF extends Influence,
								  PT extends Perception,
								  B extends CombinableBounds3D> 
extends AgentBody3D<INF,PT,B> {
	
	private static final long serialVersionUID = -6905426333331216569L;

	/**
	 * Builds a new AgentBody3D
	 * @param ownerIdentifier is the address of the holon owning this body
	 * @param perceptionType is the type of supported perceptions.
	 * @param bounds 
	 * @param convexHull is the convex hull of the entity. If global mesh,
	 * it will be localized according to the entity position.
	 */
	public AgentBody3DStub(UUID ownerIdentifier, Class<PT> perceptionType, B bounds, Mesh3D convexHull) {
		super(ownerIdentifier, perceptionType, bounds, convexHull);
	}
	
}
