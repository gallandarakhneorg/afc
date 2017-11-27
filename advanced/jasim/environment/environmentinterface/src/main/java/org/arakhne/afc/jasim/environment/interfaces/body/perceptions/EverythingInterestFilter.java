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

/** This class defines a filter for agent's perceptions according
 * to the interests of the agents.
 * <p>
 * This filter implementation accepts all the perceived objects
 * (no filtering).
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class EverythingInterestFilter implements InterestFilter {
	
	/**
	 */
	public EverythingInterestFilter() {
		//
	}
	
	/** {@inheritDoc}
	 */
	@Override
	public boolean filter(Bounds<?,?,?> bounds, Semanticable object, IntersectionType classification, UUID frustum) {
		return true;
	}

	/** {@inheritDoc}
	 */
	@Override
	public void reset() {
		//
	}

}