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

import fr.utbm.set.geom.object.Point1D5;
import fr.utbm.set.geom.transform.Transform1D5;
import fr.utbm.set.gis.road.primitive.RoadSegment;
import fr.utbm.set.jasim.environment.interfaces.internalevents.EnvironmentalAction;


/** This class describes an action inside a situated environment.
 * <p>
 * <em>The embedded transformation is local.</em>
 *
 * @author St&eacute;phane GALLAND &lt;stephane.galland@utbm.fr&gt;
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid fr.utbm.set.sfc.jasim
 * @mavenartifactid jasim-environment1d5
 */
public interface EnvironmentalAction1D5 extends EnvironmentalAction {
	
	/** Replies the 1.5D transformation contained in this 1.5D action.
	 * @return the 1.5D transformation contained in this 1.5D action
	 */
	public Transform1D5<RoadSegment> getTransformation();

	/** Replies the 1.5D position of the entity BEFORE the action application.
	 * @return the 1.5D position of the entity BEFORE the action application.
	 */
	public Point1D5 getPreviousPosition();

}