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
package fr.utbm.set.jasim.environment.model.perception.percepts;

import fr.utbm.set.gis.road.SubRoadNetwork;
import fr.utbm.set.jasim.environment.interfaces.body.perceptions.GroundPerception1D5;
import fr.utbm.set.jasim.environment.semantics.GroundType;
import fr.utbm.set.jasim.environment.semantics.RoadType;

/** This class describes a perception of the gorund inside a situated 1.5D environment.
 *
 * @author St&eacute;phane GALLAND &lt;stephane.galland@utbm.fr&gt;
 * @author Nicolas GAUD &lt;nicolas.gaud@utbm.fr&gt;
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid fr.utbm.set.sfc.jasim
 * @mavenartifactid jasim-environment1d5
 */
public class DefaultGroundPerception1D5 implements GroundPerception1D5 {

	private final GroundType type;
	private final SubRoadNetwork network;
	
	/**
	 * @param type is the type associated to the ground
	 * @param network is the perceived ground
	 */
	public DefaultGroundPerception1D5(GroundType type, SubRoadNetwork network) {
		this.type = type;
		this.network = network;
	}
	
	/**
	 * @param network is the perceived ground
	 */
	public DefaultGroundPerception1D5(SubRoadNetwork network) {
		this.type = RoadType.ROADTYPE_SINGLETON;
		this.network = network;
	}

	/** {@inheritDoc}
	 */
	@Override
	public SubRoadNetwork getRoads() {
		return this.network;
	}

	@Override
	public GroundType getSemantic() {
		return this.type;
	}

}