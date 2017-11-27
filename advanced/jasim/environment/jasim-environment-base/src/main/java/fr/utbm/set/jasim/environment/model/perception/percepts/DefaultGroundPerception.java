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

import java.lang.ref.WeakReference;

import javax.vecmath.Vector2d;

import fr.utbm.set.jasim.environment.interfaces.body.perceptions.GroundPotentialFieldPerception;
import fr.utbm.set.jasim.environment.model.ground.PerceivableGround;
import fr.utbm.set.jasim.environment.semantics.GroundType;

/** This class provides a perception of the ground.
 *
 * @author $Author: sgalland$
 * @author $Author: ngaud$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class DefaultGroundPerception implements GroundPotentialFieldPerception {
	
	private final WeakReference<PerceivableGround> ground;
	private final double x;
	private final double y;
	
	private Vector2d bufferedAttraction = null;
	private Vector2d bufferedRepulsion = null;
	private GroundType bufferedSemantic = null;
	
	/**
	 * @param ground is the ground to perceive.
	 * @param x is the position of the perceiver.
	 * @param y is the position of the perceiver.
	 */
	public DefaultGroundPerception(PerceivableGround ground, double x, double y) {
		this.ground = new WeakReference<PerceivableGround>(ground);
		this.x = x;
		this.y = y;
	}
	
	/** {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuilder buffer = new StringBuilder();
		buffer.append("("); //$NON-NLS-1$
		buffer.append(this.x);
		buffer.append(","); //$NON-NLS-1$
		buffer.append(this.y);
		buffer.append(") -> ["); //$NON-NLS-1$
		Vector2d repulsion = getRepulsion();
		buffer.append(repulsion.x);
		buffer.append(","); //$NON-NLS-1$
		buffer.append(repulsion.y);
		buffer.append("]"); //$NON-NLS-1$
		return buffer.toString();
	}
	
	/** {@inheritDoc}
	 */
	@Override
	public Vector2d getAttraction() {
		if (this.bufferedAttraction==null) {
			PerceivableGround lground = this.ground.get();
			if (lground==null) return null;
			this.bufferedAttraction = lground.getAttraction(this.x, this.y);
		}
		return this.bufferedAttraction;
	}
	
	/** {@inheritDoc}
	 */
	@Override
	public Vector2d getRepulsion() {
		if (this.bufferedRepulsion==null) {
			PerceivableGround lground = this.ground.get();
			if (lground==null) return null;
			this.bufferedRepulsion = lground.getRepulsion(this.x, this.y);
		}
		return this.bufferedRepulsion;
	}

	/** {@inheritDoc}
	 */
	@Override
	public GroundType getSemantic() {
		if (this.bufferedSemantic==null) {
			PerceivableGround lground = this.ground.get();
			if (lground==null) return null;
			this.bufferedSemantic = lground.getGroundType(this.x, this.y);
		}
		return this.bufferedSemantic;
	}
	
}