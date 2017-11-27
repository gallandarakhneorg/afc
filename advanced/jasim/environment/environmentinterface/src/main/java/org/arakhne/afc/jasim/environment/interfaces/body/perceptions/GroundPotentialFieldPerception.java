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

import javax.vecmath.Vector2d;

/** This interface describes a perception of the ground.
 *
 * @author $Author: sgalland$
 * @author $Author: ngaud$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public interface GroundPotentialFieldPerception extends GroundPerception {
	
	/** Replies the attraction information provided by the ground on the current position.
	 *
	 * @return an attraction information provided by the ground on the current position.
	 */
	public Vector2d getAttraction();
	
	/** Replies the repulsion information provided by the ground on the current position.
	 *
	 * @return a repulsion information provided by the ground on the current position.
	 */
	public Vector2d getRepulsion();

}