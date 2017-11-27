/*
 * 
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
package org.arakhne.afc.jasim.environment.interfaces.body;

import org.arakhne.afc.jasim.environment.interfaces.body.factory.AgentBodyFactory;
import org.arakhne.afc.jasim.environment.interfaces.body.factory.BodyDescription;
import org.arakhne.afc.jasim.environment.interfaces.body.factory.FrustumDescription;
import org.arakhne.afc.jasim.environment.model.influences.InfluenceCollector;
import org.arakhne.afc.jasim.environment.model.perceptions.PerceptionGenerator;
import org.arakhne.afc.jasim.environment.time.Clock;

import fr.utbm.set.geom.object.EuclidianDirection;
import fr.utbm.set.geom.object.EuclidianPoint;

/**
 * The situated environment that provides interface for agent's bodies.
 * 
 * @author $Author: sgalland$
 * @author $Author: ngaud$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public interface BodyContainerEnvironment {
	
	/** Replies the perception generator from this environment.
	 * 
	 * @return the perception generator.
	 */
	public PerceptionGenerator getPerceptionGenerator();
	
	/** Replies the influence collector from this environment.
	 * 
	 * @return the influence collector.
	 */
	public InfluenceCollector getInfluenceCollector();

	/**
	 * Returns the current simulation time.
	 * 
	 * @return the current simulation time.
	 */
	public Clock getSimulationClock();
	
	/** Replies a factory of bodies.
	 * 
	 * @param position is the position at which the factory will create the bodies.
	 * @param orientation is the orientation which will be given to the bodies.
	 * @param description is the description of the bodies to create.
	 * @param frustums are the descriptions of the default frustums to add to created bodies.
	 * @return a factory of bodies.
	 */
	public AgentBodyFactory getAgentBodyFactory(
			EuclidianPoint position, 
			EuclidianDirection orientation,
			BodyDescription description,
			Iterable<FrustumDescription> frustums);

}
