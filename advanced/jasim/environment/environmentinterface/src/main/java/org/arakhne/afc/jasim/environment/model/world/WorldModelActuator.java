/*
 * 
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
package org.arakhne.afc.jasim.environment.model.world;

import org.arakhne.afc.jasim.environment.interfaces.internalevents.EnvironmentalAction;
import org.arakhne.afc.jasim.environment.time.Clock;


/**
 * Modifies the data structures used to store the world entities.
 * <p>
 * A world model updater is a specialization of a
 * {@link DynamicEntityManager} able to treat the
 * {@link EnvironmentalAction environmental actions}. 
 * 
 * @param <EA> is the type of supported environmental actions.
 * @param <ME> is the type of entites supported by this actuator.
 * @author $Author: sgalland$
 * @author $Author: ngaud$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public interface WorldModelActuator<EA extends EnvironmentalAction, ME extends MobileEntity<?>>
extends DynamicEntityManager<ME> {
	
	/**
	 * Invoked when actions must be applied.
	 * <p>
	 * You must invoce {@link #commit()} to commit the actions.
	 * 
	 * @param time is the current simulation time.
	 * @param actions is the list of actions to apply.
	 */
	public void registerActions(Clock time, Iterable<EA> actions);

	/** Replies the raw implementation of the world model.
	 * <p>
	 * The raw implementation of the world depends on the
	 * world dimension (graph, plane...).
	 * 
	 * @param <WM> is the type of the raw implementation to reply.
	 * @param type is the type of the raw implementation to reply.
	 * @return the world model of the given type or <code>null</code>.
	 */
	public <WM> WM getInnerWorldModel(Class<WM> type);

}
