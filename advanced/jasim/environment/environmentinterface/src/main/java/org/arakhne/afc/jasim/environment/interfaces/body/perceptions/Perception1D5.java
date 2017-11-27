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
package org.arakhne.afc.jasim.environment.interfaces.body.perceptions;

import java.util.List;

import fr.utbm.set.geom.object.Segment1D;

/** This class describes a perception inside a situated 1.5D environment.
 *
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public interface Perception1D5 extends Perception {
	
	/** Replies the segment on which the perception lies.
	 * 
	 * @return a segment
	 */
	public Segment1D getPerceivedObjectSegment();
	
	/**
	 * Replies the relative distance between the perceiver body and the
	 * perceived body along the graph segments. The distance is given from the eye to
	 * the nearest point of the perceived object's bounds.
	 * 
	 * @return a distance.
	 */
	public double getPerceivedObjectCurvilineDistance();
	
	/**
	 * Replies the relative distance between the perceiver body and the
	 * perceived body along the perpendicular direction to the segment
	 * (aka. jutting distance). The distance is given from the eye to
	 * the nearest point of the perceived object's bounds.
	 * 
	 * @return a distance.
	 */
	public double getPerceivedObjectJuttingDistance();
	
	/**
	 * Returns if the perceived object is moving in the same direction
	 * @return <code>true</code> if the perceived entity and the perceiver has
	 * the same direction, otherwise <code>false</code>
	 */
	public boolean isSameDirection();
	
	/**
	 * Replies if the perceived entity is in front of the frustum
	 * @return <code>true</code> if the perceived entity is in front of the perceiver
	 */
	public boolean isInFront();

	/**
	 * Returns the path to the perceived object.
	 * @return the path to the perceived object.
	 */
	public List<? extends Segment1D> getPathToPerceivedObject();

	/**
	 * Returns the path to the perceived object.
	 * 
	 * @param <P> is the type of the path to replies.
	 * @param type is the type of the path to replies.
	 * @return the path to the perceived object or <code>null</code> if the path type
	 * does not match the given type.
	 */
	public <P extends List<? extends Segment1D>> P getPathToPerceivedObject(Class<P> type);

}