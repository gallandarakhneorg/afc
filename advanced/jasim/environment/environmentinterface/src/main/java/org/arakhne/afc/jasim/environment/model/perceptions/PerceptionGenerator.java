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
package org.arakhne.afc.jasim.environment.model.perceptions;

import org.arakhne.afc.jasim.environment.interfaces.body.perceptions.Perceiver;
import org.arakhne.afc.jasim.environment.interfaces.body.perceptions.Perception;
import org.arakhne.afc.jasim.environment.interfaces.body.perceptions.Perceptions;

/**
 * This interface describes a generator of perceptions.
 * 
 * @author $Author: sgalland$
 * @author $Author: ngaud$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public interface PerceptionGenerator {

	/**
	 * Inform the perceptionGenerator that he may generate the perception for all entities evolving in the environment.
	 */
	public void computeAgentPerceptions();
	
	/**
	 * Replies the percepts for the given agent.
	 *
	 * @param <PT> is the type of the attempted perceptions.
	 * @param perceiver is the address of the perceiver.
	 * @param perceptionType is the type of the attempted perceptions
	 * @return the list of environmental percepts.
	 */
	public <PT extends Perception> Perceptions<PT> getPerceptions(Perceiver perceiver, Class<PT> perceptionType);

}
