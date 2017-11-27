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

package fr.utbm.set.jasim.environment.model.perception.frustum;

import java.util.List;
import java.util.UUID;

import fr.utbm.set.jasim.environment.model.world.Entity1D5;
import fr.utbm.set.geom.intersection.IntersectionType;
import fr.utbm.set.geom.object.Segment1D;

/**
 * This class is the result of the frustum culler iterator.
 * 
 * @param <D> is the type of the culled object.
 * @author St&eacute;phane GALLAND &lt;stephane.galland@utbm.fr&gt;
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid fr.utbm.set.sfc.jasim
 * @mavenartifactid jasim-environment1d5
 */
public class CullingResult1D5<D extends Entity1D5<?>> extends CullingResult<D> {

	private final boolean sameDirection;
	private final boolean inFrontOf;
	private final double shiftDistance;
	private final double curvilineDistance;
	private final List<? extends Segment1D> path;

	/**
	 * @param frustum
	 * @param type
	 * @param object
	 * @param relativeCurvilineDistance
	 * @param relativeShiftDistance
	 * @param isInFrontOf
	 * @param hasSameDirection
	 * @param path
	 */
	public CullingResult1D5(UUID frustum, IntersectionType type, D object,
			double relativeCurvilineDistance, double relativeShiftDistance,
			boolean isInFrontOf, boolean hasSameDirection,
			List<? extends Segment1D> path) {
		super(frustum, type, object);
		this.curvilineDistance = relativeCurvilineDistance;
		this.inFrontOf = isInFrontOf;
		this.shiftDistance = relativeShiftDistance;
		this.sameDirection = hasSameDirection;
		this.path = path;
	}
	
	/**
	 * Replies the path to the perceived object.
	 * 
	 * @return the path to the perceived object.
	 */
	public List<? extends Segment1D> getPathToPerceivedObject() {
		return this.path;
	}

	/**
	 * Replies the relative curviline distance to the perceived object.
	 * 
	 * @return the relative curviline distance to the perceived object.
	 */
	public double getPerceivedObjectCurvilineDistance() {
		return this.curvilineDistance;
	}

	/**
	 * Replies the relative shift distance to the perceived object.
	 * 
	 * @return the relative shift distance to the perceived object.
	 */
	public double getPerceivedObjectJuttingDistance() {
		return this.shiftDistance;
	}

	/**
	 * Replies if the perceived object is in front of the perceiver.
	 * 
	 * @return <code>true</code> if the perceived object is in front of,
	 * otherwise <code>false</code>
	 */
	public boolean isInFront() {
		return this.inFrontOf;
	}

	/**
	 * Replies if the perceived object has the same direction as 
	 * the perceiver.
	 * 
	 * @return <code>true</code> if same direction, otherwise <code>false</code>.
	 */
	public boolean isSameDirection() {
		return this.sameDirection;
	}

}
