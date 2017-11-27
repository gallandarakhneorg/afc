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

import fr.utbm.set.gis.road.primitive.RoadSegment;
import fr.utbm.set.jasim.environment.time.Clock;
import fr.utbm.set.geom.object.Point1D5;
import fr.utbm.set.geom.transform.Transform1D5;

/** This class describes an action inside a 1.5d situated environment.
 * <p>
 * <em>The embedded transformation is local.</em>
 *
 * @author St&eacute;phane GALLAND &lt;stephane.galland@utbm.fr&gt;
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid fr.utbm.set.sfc.jasim
 * @mavenartifactid jasim-environment1d5
 */
public class DefaultEnvironmentalAction1D5 extends AbstractEnvironmentalAction implements EnvironmentalAction1D5 {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7007743158467092934L;
	private final Transform1D5<RoadSegment> transform;
	private final Point1D5 previousPosition;
	
	/**
	 * @param influencedObject is the object that must be influenced.
	 * @param clock is the date at which the action occured.
	 * @param transformation is the transformation to apply.
	 * @param previousPosition is the position of the entity BEFORE action application.
	 */
	public DefaultEnvironmentalAction1D5(Object influencedObject, Clock clock, Transform1D5<RoadSegment> transformation, Point1D5 previousPosition) {
		super(influencedObject, clock);
		this.transform = transformation;
		this.previousPosition = previousPosition;
	}
	
	/** {@inheritDoc}
	 */
	@Override
	public Transform1D5<RoadSegment> getTransformation() {
		return this.transform;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Point1D5 getPreviousPosition() {
		return this.previousPosition;
	}

}