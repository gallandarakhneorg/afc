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

import java.util.EventObject;

/** Describes a change of time in a simulation.
 *
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class TimeManagerEvent extends EventObject {
	
	private static final long serialVersionUID = -9209063006022652237L;
	
	private final Clock clock;
	
	/**
	 * @param source is the source of the event.
	 */
	public TimeManagerEvent(TimeManager source) {
		super(source);
		this.clock = new EventClock(source.getClock());
	}
	
	/** Replies the time manager that fire this event.
	 * 
	 * @return the time manager, never <code>null</code>
	 */
	public TimeManager getTimeManager() {
		return (TimeManager)getSource();
	}
	
	/** Replies the new clock value.
	 * 
	 * @return the clock, never <code>null</code>
	 */
	public Clock getClock() {
		return this.clock;
	}
	
}