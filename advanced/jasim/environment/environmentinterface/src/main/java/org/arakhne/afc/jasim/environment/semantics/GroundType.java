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
package org.arakhne.afc.jasim.environment.semantics;

/** This class describes a semantical tag associated to ground.
 *
 * @author $Author: sgalland$
 * @author $Author: ngaud$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class GroundType extends AbstractSemantic {
	
	private static final long serialVersionUID = -7669938235235356751L;
	
	/** Singleton instance of this semantical object.
	 */
	public static final GroundType GROUNDTYPE_SINGLETON = new GroundType();
	
	/**
	 */
	protected GroundType() {
		//
	}
	
}