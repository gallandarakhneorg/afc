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
package fr.utbm.set.jasim.environment.model.influencereaction;

import fr.utbm.set.jasim.environment.model.influencereaction.AbstractEnvironmentalAction;
import fr.utbm.set.jasim.environment.time.Clock;

/**
 * This class describes an influence.
 * <p>
 * <em>The embedded transformation is local.</em>
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class AbstractEnvironmentalActionStub extends AbstractEnvironmentalAction {

	private static final long serialVersionUID = 3392635774544687676L;

	/**
	 * @param influencedObject is the object that must be influenced.
	 * @param clock is the date at which the action occured, never <code>null</code>.
	 */
	public AbstractEnvironmentalActionStub(Object influencedObject, Clock clock) {
		super(influencedObject, clock);
	}
	
}
