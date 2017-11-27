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

import fr.utbm.set.jasim.environment.interfaces.internalevents.EnvironmentalAction;
import fr.utbm.set.jasim.environment.time.Clock;

/**
 * This class describes an influence.
 * <p>
 * <em>The embedded transformation is local.</em>
 * 
 * @author $Author: sgalland$
 * @author $Author: ngaud$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public abstract class AbstractEnvironmentalAction implements EnvironmentalAction {

	private static final long serialVersionUID = -2074308058741551855L;

	private final Object influencedObject;
	
	private final Clock clock;

	/**
	 * @param influencedObject is the object that must be influenced.
	 * @param clock is the date at which the action occured, never <code>null</code>.
	 */
	public AbstractEnvironmentalAction(Object influencedObject, Clock clock) {
		this.influencedObject = influencedObject;
		this.clock = clock;
	}
	
	/** {@inheritDoc}
	 */
	@Override
	public Clock getClock() {
		return this.clock;
	}

	/** {@inheritDoc}
	 */
	@Override
	public <ET> ET getEnvironmentalObject(Class<ET> type) {
		if (this.influencedObject!=null && type.isInstance(this.influencedObject))
			return type.cast(this.influencedObject);
		return null;
	}

}
