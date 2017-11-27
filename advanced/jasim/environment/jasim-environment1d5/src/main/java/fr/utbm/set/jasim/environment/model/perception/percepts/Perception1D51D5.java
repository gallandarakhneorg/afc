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

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import fr.utbm.set.jasim.environment.interfaces.body.perceptions.Perception1D5;
import fr.utbm.set.jasim.environment.model.world.Entity1D5;
import fr.utbm.set.geom.intersection.IntersectionType;
import fr.utbm.set.geom.object.Segment1D;

/** This class describes a 1.5D mapping of a 1.5D perception inside a situated environment.
 *
 * @author St&eacute;phane GALLAND &lt;stephane.galland@utbm.fr&gt;
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid fr.utbm.set.sfc.jasim
 * @mavenartifactid jasim-environment1d5
 */
class Perception1D51D5 extends AbstractPerception1D5 implements Perception1D5 {

	private final boolean sameDirection;
	private final boolean inFrontOf;
	private final double shiftDistance;
	private final double curvilineDistance;
	private final List<? extends Segment1D> path;
	
	/**
	 * @param frustum
	 * @param type
	 * @param perceivedObject
	 * @param relativeCurvilineDistance
	 * @param relativeShiftDistance
	 * @param isInFrontOf
	 * @param hasSameDirection
	 * @param path
	 */
	public Perception1D51D5(UUID frustum, IntersectionType type, Entity1D5<?> perceivedObject,
			double relativeCurvilineDistance, double relativeShiftDistance,
			boolean isInFrontOf, boolean hasSameDirection,
			List<? extends Segment1D> path) {
		super(frustum, type, perceivedObject);
		this.curvilineDistance = relativeCurvilineDistance;
		this.shiftDistance = relativeShiftDistance;
		this.inFrontOf = isInFrontOf;
		this.sameDirection = hasSameDirection;
		this.path = path;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<? extends Segment1D> getPathToPerceivedObject() {
		return Collections.unmodifiableList(this.path);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <P extends List<? extends Segment1D>> P getPathToPerceivedObject(Class<P> type) {
		if (this.path!=null && type.isInstance(this.path))
			return type.cast(Collections.unmodifiableList(this.path));
		return type.cast(Collections.emptyList());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public double getPerceivedObjectCurvilineDistance() {
		return this.curvilineDistance;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public double getPerceivedObjectJuttingDistance() {
		return this.shiftDistance;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Segment1D getPerceivedObjectSegment() {
		return getPerceivedObject().getPosition1D5().getSegment();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isInFront() {
		return this.inFrontOf;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isSameDirection() {
		return this.sameDirection;
	}
	
}