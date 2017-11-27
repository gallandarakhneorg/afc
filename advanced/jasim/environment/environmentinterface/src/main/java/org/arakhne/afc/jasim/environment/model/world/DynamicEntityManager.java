/*
 * 
 * $Id$
 * 
 * Copyright (c) 2006-09, Multiagent Team - Systems and Transportation Laboratory (SeT)
 * Copyright (c) 2013, Multiagent Team - Systems and Transportation Laboratory (SeT)
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


/**
 * The interface describes an object able to manage the arrival into and
 * departure from the environment of mobile entities.
 * <p>
 * The arrival and departure are asynchronous because they must
 * be controled by the environment itself.
 * 
 * @param <ME> is the type of mobile entity supported by this environment.
 * @author $Author: sgalland$
 * @author $Author: ngaud$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public interface DynamicEntityManager<ME extends MobileEntity<?>> {

	/**
	 * Add dynamically a new mobile entity to the various data structures
	 * used to manage the perception mechanisms.
	 * <p>
	 * You must invoce {@link #commit()} to commit the addition.
	 * 
	 * @param entity - the new mobile entity to add
	 */
	public void registerMobileEntity(ME entity);
	
	/**
	 * Remove dynamically a mobile entity from the various data structures
	 * used to manage the perception mechanisms
	 * <p>
	 * You must invoce {@link #commit()} to commit the removal.
	 * 
	 * @param entity - the mobile entity to remove
	 */
	public void unregisterMobileEntity(ME entity);

	/**
	 * Apply the changes (additions, removals or any other change).
	 */
	public void commit();

}
