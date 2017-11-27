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

package org.arakhne.afc.jasim.environment.time;

import java.util.concurrent.TimeUnit;

/** This interface manages the time for a environment.
 *
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public interface TimeManager {
	
	/**
	 * Replies the time unit used by this manager.
	 * 
	 * @return the time unit.
	 */
	public TimeUnit getTimeUnit();

	/**
	 * Replies the simulation clock used by this manager.
	 * 
	 * @return the simulation clock.
	 */
	public Clock getClock();

	/** Notify this manager that the environment loop has started.
	 */
	public void onEnvironmentBehaviourStarted();

	/** Notify this manager that the environment loop has finished.
	 */
	public void onEnvironmentBehaviourFinished();
	
	/**
	 * Add a listener on this time manager that will be invoked at each time change.
	 * 
	 * @param listener
	 */
	public void addTimeManagerListener(TimeManagerListener listener);

	/**
	 * Remove a listener on this time manager.
	 * 
	 * @param listener
	 */
	public void removeTimeManagerListener(TimeManagerListener listener);

}