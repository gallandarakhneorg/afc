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
package fr.utbm.set.jasim.environment.model.perception.percepts;

import fr.utbm.set.jasim.environment.interfaces.body.perceptions.GroundPerception;
import fr.utbm.set.jasim.environment.semantics.GroundType;

/** This interface describes a perception of the ground.
 *
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class GroundPerceptionStub implements GroundPerception {
	
	/** {@inheritDoc}
	 */
	@Override
	public GroundType getSemantic() {
		return null;
	}
	
}