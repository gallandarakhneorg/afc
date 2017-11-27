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
package org.arakhne.afc.jasim.environment.interfaces.body.perceptions;

import java.util.UUID;

import fr.utbm.set.geom.intersection.IntersectionType;

/** This class describes a perception inside a situated environment.
 *
 * @author $Author: sgalland$
 * @author $Author: ngaud$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public interface Perception {
	
	/**
	 * Replies the elements inside the perception frustum of an agent.
	 * 
	 * @return an ordered collection of perceived elements.
	 */
	public Perceivable getPerceivedObject();
	
	/** Replies the classification of the perceived object according
	 * to the perception frustum
	 * 
	 * @return the position of the perceived object against the perception frustum.
	 */
	public IntersectionType getClassification();
	
	/** Replies the identifier of frustum that permits to perceive the object.
	 * 
	 * @return the frustum.
	 */
	public UUID getFrustum();	

}