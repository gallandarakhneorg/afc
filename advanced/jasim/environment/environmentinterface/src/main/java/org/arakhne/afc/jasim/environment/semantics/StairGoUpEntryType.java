/* 
 * $Id$
 * 
 * Copyright (c) 2010, Multiagent Team - Systems and Transportation Laboratory (SeT)
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

/** This interface describes a semantical tag associated to the entry of 
 * a stair which permits to go up.
 *
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class StairGoUpEntryType extends ImmobileObjectType {
	
	private static final long serialVersionUID = -8571870583268090127L;
	
	/** This is a singleton instance of the body type.
	 */
	public static final StairGoUpEntryType STAIRGOUPENTRY_SINGLETON = new StairGoUpEntryType();	

	/**
	 */
	protected StairGoUpEntryType() {
		//
	}
	
}