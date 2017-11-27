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

import fr.utbm.set.geom.bounds.Bounds;
import fr.utbm.set.geom.intersection.IntersectionType;

import org.arakhne.afc.jasim.environment.semantics.Semanticable;

/** This class defines physical alterations of the agent's perceptions.
 * <p>
 * This alterator implementation accepts all the perceived objects
 * (no filtering).
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class IdentityPhysicalPerceptionAlterator implements PhysicalPerceptionAlterator {
	
	/**
	 */
	public IdentityPhysicalPerceptionAlterator() {
		//
	}
	
	/** {@inheritDoc}
	 */
	@Override
	public IntersectionType alters(Bounds<?,?,?> bounds, Semanticable object, IntersectionType classification, UUID frustum) {
		return classification;
	}

	/** {@inheritDoc}
	 */
	@Override
	public void reset() {
		//
	}

}