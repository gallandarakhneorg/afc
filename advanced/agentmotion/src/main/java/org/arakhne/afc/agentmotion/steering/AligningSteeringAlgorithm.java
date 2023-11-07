/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2023 The original authors and other contributors.
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

package org.arakhne.afc.agentmotion.steering;

import java.io.Serializable;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.agentmotion.AligningMotionAlgorithm;
import org.arakhne.afc.math.geometry.d2.Vector2D;
import org.arakhne.afc.vmutil.asserts.AssertMessages;

/** Agent is changing its oriented for being align to the target vector.
 *
 * <p>This algorithm uses accelerations.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
public class AligningSteeringAlgorithm implements AligningMotionAlgorithm, Serializable, Cloneable {

	private static final long serialVersionUID = -8318025671219960417L;

	/** Angle to the target under which the agent could stop.
	 */
	protected final double stopAngle;

	/** Angle to the target under which the agent could decelerate.
	 */
	protected final double decelerationAngle;

	/** Approximation factor.
	 */
	protected final int approximationFactor;

	/** Constructor.
	 *
	 * @param stopAngle the angle to the target under which the agent could stop.
	 * @param decelerationAngle the angle the target under which the agent could decelerate.
	 * @param approximationFactor the factor used for approximate the deceleration. It must be greater than 1.
	 */
	public AligningSteeringAlgorithm(double stopAngle, double decelerationAngle, int approximationFactor) {
		assert stopAngle >= 0. : AssertMessages.positiveOrZeroParameter();
		assert decelerationAngle >= stopAngle : AssertMessages.lowerEqualParameters(0, stopAngle, 1, decelerationAngle);
		assert approximationFactor > 1 : AssertMessages.lowerEqualParameter(2, decelerationAngle, 2);
		this.stopAngle = stopAngle;
		this.decelerationAngle = decelerationAngle;
		this.approximationFactor = approximationFactor;
	}

	@Pure
	@Override
	public AligningSteeringAlgorithm clone() {
		try {
			return (AligningSteeringAlgorithm) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new Error(e);
		}
	}

	@Override
	public double calculate(Vector2D<?, ?> orientation, double angularSpeed, double maxAngularAcceleration,
			Vector2D<?, ?> target) {
		final double sangle = orientation.signedAngle(target);
		final double angle = Math.abs(sangle);
		if (angle <= this.stopAngle) {
			return -angularSpeed / 1.;
		}
		if (angle > this.decelerationAngle) {
			return Math.signum(sangle) * maxAngularAcceleration;
		}
		return sangle / (this.decelerationAngle * this.decelerationAngle);
	}

}
