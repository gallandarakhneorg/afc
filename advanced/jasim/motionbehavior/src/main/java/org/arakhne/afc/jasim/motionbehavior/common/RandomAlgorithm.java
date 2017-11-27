/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2018 The original authors, and other authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.arakhne.afc.jasim.motionbehavior.common;

import java.io.Serializable;
import java.util.Random;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.jasim.motionbehavior.AgentMotion;
import org.arakhne.afc.jasim.motionbehavior.FacingMotionAlgorithm;
import org.arakhne.afc.jasim.motionbehavior.RandomMotionAlgorithm;
import org.arakhne.afc.jasim.motionbehavior.SeekingMotionAlgorithm;
import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.Vector2D;
import org.arakhne.afc.math.geometry.d2.d.Point2d;
import org.arakhne.afc.vmutil.asserts.AssertMessages;

/** Agent is changing randomly its position and orientation.
 *
 * <p>This algorithm uses speeds or accelerations, depending on its delegates.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
public class RandomAlgorithm implements RandomMotionAlgorithm, Serializable, Cloneable {

	private static final long serialVersionUID = -8318025671219960417L;

	/** Distance between the agent position and the center of the virtual wheel.
	 */
	protected final double wheelDistance;

	/** Radius of the virtual wheel.
	 */
	protected final double wheelRadius;

	/** Maximal rotation of the virtual wheel.
	 */
	protected final double maxWheelRotation;

	/** Facing algorithm to delegate to.
	 */
	protected final FacingMotionAlgorithm facing;

	/** Seeking algorithm to delegate to.
	 */
	protected final SeekingMotionAlgorithm seeking;

	private Random random = new Random();

	private double rotation;

	/** Constructor.
	 *
	 * @param wheelDistance the distance between the agent position and the center of the virtual wheel.
	 * @param wheelRadius the radius of the virtual wheel.
	 * @param maxWheelRotation the maximal rotation of the virtual wheel.
	 * @param facing the facing algorithm to delegate to.
	 * @param seeking the seeking algorithm to delegate to.
	 */
	public RandomAlgorithm(double wheelDistance, double wheelRadius, double maxWheelRotation,
			FacingMotionAlgorithm facing, SeekingMotionAlgorithm seeking) {
		assert wheelDistance >= 0. : AssertMessages.positiveOrZeroParameter(0);
		assert wheelRadius >= 0. : AssertMessages.positiveOrZeroParameter(1);
		assert maxWheelRotation >= 0. : AssertMessages.positiveOrZeroParameter(2);
		this.wheelDistance = wheelDistance;
		this.wheelRadius = wheelRadius;
		this.maxWheelRotation = maxWheelRotation;
		this.facing = facing;
		this.seeking = seeking;
	}

	@Pure
	@Override
	public RandomAlgorithm clone() {
		try {
			final RandomAlgorithm clone = (RandomAlgorithm) super.clone();
			clone.random = new Random();
			return clone;
		} catch (CloneNotSupportedException e) {
			throw new Error(e);
		}
	}

	@Pure
	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj != null && obj.getClass() == getClass()) {
			final RandomAlgorithm algo = (RandomAlgorithm) obj;
			return algo.wheelDistance == this.wheelDistance
					&& algo.wheelRadius == this.wheelRadius
					&& algo.maxWheelRotation == this.maxWheelRotation
					&& algo.facing.equals(this.facing)
					&& algo.seeking.equals(this.seeking);
		}
		return false;
	}

	@Pure
	@Override
	public int hashCode() {
		int bits = 1;
		bits = 31 * bits + Double.hashCode(this.wheelDistance);
		bits = 31 * bits + Double.hashCode(this.wheelRadius);
		bits = 31 * bits + Double.hashCode(this.maxWheelRotation);
		bits = 31 * bits + this.facing.hashCode();
		bits = 31 * bits + this.seeking.hashCode();
		return bits ^ (bits >> 31);
	}

	@Override
	public AgentMotion calculate(Point2D<?, ?> position, Vector2D<?, ?> orientation, double linearSpeed,
			double maxLinear, double angularSpeed, double maxAngular) {
		// Calculate the circle center
		final Vector2D<?, ?> wheelCenter = orientation.toColinearVector(this.wheelDistance);

		// Calculate the displacement force
		final Vector2D<?, ?>  displacement = wheelCenter.toColinearVector(this.wheelRadius);
		displacement.turn(this.rotation);

		// Change angle just a bit, so it
		// won't have the same value in the
		// next frame.
		this.rotation += (this.random.nextDouble() * 2. - 1.) * this.maxWheelRotation;

		// Finally calculate the wander force
		final Point2d circleCenterPosition = new Point2d(
				position.getX() + wheelCenter.getX(),
				position.getY() + wheelCenter.getY());
		final Point2d faceTarget = new Point2d(
				circleCenterPosition.getX() + displacement.getX(),
				circleCenterPosition.getY() + displacement.getY());

		// Delegate
		final double angularMotion = this.facing.calculate(position, orientation, angularSpeed, maxAngular, faceTarget);
		final Vector2D<?, ?> linearMotion = this.seeking.calculate(position, linearSpeed, maxLinear, circleCenterPosition);

		return new AgentMotion(linearMotion, angularMotion);
	}

}
