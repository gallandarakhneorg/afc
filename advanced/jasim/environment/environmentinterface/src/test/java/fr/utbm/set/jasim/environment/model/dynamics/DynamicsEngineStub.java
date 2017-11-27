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
package fr.utbm.set.jasim.environment.model.dynamics;

import org.arakhne.afc.jasim.environment.model.dynamics.DynamicsEngine;
import org.arakhne.afc.jasim.environment.model.influences.InfluenceCollector;
import org.arakhne.afc.jasim.environment.model.perceptions.PerceptionGenerator;

/** This interface representes the environment's dynamics engine.
 *
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class DynamicsEngineStub implements DynamicsEngine {

	/** Run the behaviour of the dynamics engine.
	 * <p>
	 * This behaviour is supposed to get perceptions from a
	 * {@link PerceptionGenerator} and produce influences
	 * collected by a {@link InfluenceCollector}
	 */
	@Override
	public void run() {
		//
	}

}