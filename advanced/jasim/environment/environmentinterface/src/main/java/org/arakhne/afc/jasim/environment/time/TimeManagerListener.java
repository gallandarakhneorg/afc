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

import java.util.EventListener;

/** Listener on time changes.
 *
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public interface TimeManagerListener extends EventListener {
	
	/**
	 * Invoked each time the time manager has changed its clock.
	 * 
	 * @param event
	 */
	public void onSimulationClockChanged(TimeManagerEvent event);
	
}