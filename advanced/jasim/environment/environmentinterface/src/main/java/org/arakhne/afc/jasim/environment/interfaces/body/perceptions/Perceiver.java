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

import java.util.Collection;
import java.util.UUID;

import org.arakhne.afc.jasim.environment.interfaces.body.frustums.Frustum;

/** This interface describes an object able to perceive.
 *
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public interface Perceiver {
	
	/**
	 * Replies the identifier of this object.
	 * 
	 * @return the identifier of this object.
	 */
	public UUID getIdentifier();

	/**
	 * Returns the list of the various frustums used by this body to perceive its environment
	 * 
	 * @return the list of the various frustums used by this body to perceive its environment
	 */
	public Collection<? extends Frustum<?,?,?>> getFrustums();

	/**
	 * Returns the object used to filter the perception.
	 * 
	 * @return the perception filter.
	 */
	public InterestFilter getInterestFilter();

}