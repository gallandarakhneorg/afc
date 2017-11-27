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

package fr.utbm.set.jasim.environment.model.perception.frustum;

/**
 * This enumeration describes the type of occlusion between objects.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public enum OcclusionCullingType {

	/** The object is entirely occluded by another one.
	 */
	TOTAL_OCCLUSION,
	
	/** The object is partly occluded by another one.
	 */
	PARTIAL_OCCLUSION,

	/** The object is not occluded by another one.
	 */
	NO_OCCLUSION;

}
