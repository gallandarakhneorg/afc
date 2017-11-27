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

/** This interface describes a semantical tag associated to an immobile and traversable object.
 *
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class ImmobileTraversableObjectType extends ImmobileObjectType {
	
	private static final long serialVersionUID = -8952910338469853943L;
	
	/** This is a singleton instance of the body type.
	 */
	public static final ImmobileTraversableObjectType IMMOBILETRAVERSABLEOBJECTTYPE_SINGLETON = new ImmobileTraversableObjectType();	

	/**
	 */
	protected ImmobileTraversableObjectType() {
		//
	}
	
}