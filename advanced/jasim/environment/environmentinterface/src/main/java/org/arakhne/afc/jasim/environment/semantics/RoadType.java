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

/** This class describes a semantical tag associated to roads.
 *
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class RoadType extends TraversableGroundType {
	
	private static final long serialVersionUID = 3976825777537545802L;
	
	/** Singleton instance of this semantical object.
	 */
	public static final RoadType ROADTYPE_SINGLETON = new RoadType();
	
	/**
	 */
	protected RoadType() {
		//
	}
	
	/** Replies the negation of this type.
	 *
	 * @return the negation of this type, or <code>null</code>
	 */
	@Override
	public Semantic negate() {
		return NotTraversableGroundType.NOTTRAVERSABLEGROUNDTYPE_SINGLETON;
	}

}