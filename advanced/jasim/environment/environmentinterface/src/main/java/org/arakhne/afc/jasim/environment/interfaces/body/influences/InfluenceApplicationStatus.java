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
package org.arakhne.afc.jasim.environment.interfaces.body.influences;

/**
 * Status concerning the last try to apply an influence
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public enum InfluenceApplicationStatus {

	/** Last influence was successfully applied.
	 */
	SUCCESS,

	/** The current position of the entity seems to be invalid.
	 * Perhaps it is because it is on a non traversable area.
	 */
	INVALID_POSITION,

	/** The movement component of the last influence is discarted
	 */
	MOVE_DISCARTED,
	
	/** The rotation component of the last influence is discarted
	 */
	ROTATION_DISCARTED,
	
	/** The movement component of the last influence is partly applied
	 */
	MOVE_PARTLY_APPLIED,
	
	/** The rotation component of the last influence is partly applied
	 */
	ROTATION_PARTLY_APPLIED,
	
	/** The movement and rotation components of the last influence is partly applied
	 */
	MOVE_AND_ROTATION_PARTLY_APPLIED,

	/** The move action provided by the agent has an invalid component value.
	 * It is sometimes because the agent's behaviour generate a {@link Double#NaN}
	 * value.
	 */
	INVALID_MOVE_VALUE,

	/** The rotation action provided by the agent has an invalid component value.
	 * It is sometimes because the agent's behaviour generate a {@link Double#NaN}
	 * value.
	 */
	INVALID_ROTATION_VALUE;

}
