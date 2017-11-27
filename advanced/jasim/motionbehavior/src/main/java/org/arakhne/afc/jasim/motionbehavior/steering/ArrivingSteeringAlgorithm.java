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

package org.arakhne.afc.jasim.motionbehavior.steering;

import java.io.Serializable;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.jasim.motionbehavior.ArrivingMotionAlgorithm;
import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.Vector2D;
import org.arakhne.afc.math.geometry.d2.d.Vector2d;
import org.arakhne.afc.vmutil.asserts.AssertMessages;

/** Agent is changing its position for arriving (stopping) the target point.
 *
 * <p>This algorithm uses accelerations.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
public class ArrivingSteeringAlgorithm implements ArrivingMotionAlgorithm, Serializable, Cloneable {

	private static final long serialVersionUID = -8318025671219960417L;

	/** Square distance to the target under which the agent could stop.
	 */
	protected final double stopDistance;

	/** Square distance to the target under which the agent could decelerate.
	 */
	protected final double decelerationDistance;

	/** Approximation factor.
	 */
	protected final int approximationFactor;

	/** Constructor.
	 *
	 * @param stopDistance the distance to the target under which the agent could stop.
	 * @param decelerationDistance the distance the target under which the agent could decelerate.
	 * @param approximationFactor the factor used for approximate the deceleration. It must be greater than 1.
	 */
	public ArrivingSteeringAlgorithm(double stopDistance, double decelerationDistance, int approximationFactor) {
		assert stopDistance >= 0. : AssertMessages.positiveOrZeroParameter();
		assert decelerationDistance >= stopDistance : AssertMessages.lowerEqualParameters(0, stopDistance,
				1, decelerationDistance);
		assert approximationFactor > 1 : AssertMessages.lowerEqualParameter(2, decelerationDistance, 2);
		this.stopDistance = stopDistance * stopDistance;
		this.decelerationDistance = decelerationDistance * decelerationDistance;
		this.approximationFactor = approximationFactor;
	}

	@Pure
	@Override
	public ArrivingSteeringAlgorithm clone() {
		try {
			return (ArrivingSteeringAlgorithm) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new Error(e);
		}
	}

	@Override
	public Vector2D<?, ?> calculate(Point2D<?, ?> position, double linearSpeed, double maxLinear,
			Point2D<?, ?> target) {
		final Vector2d v = new Vector2d(
				target.getX() - position.getX(),
				target.getY() - position.getY());
		double length =  v.getLengthSquared();
		if (length <= this.stopDistance) {
			length = -linearSpeed / 1.;
		} else  if (length > this.decelerationDistance) {
			length = maxLinear;
		} else {
			length = length / (this.decelerationDistance * this.decelerationDistance);
		}
		v.setLength(length);
		return v;
	}

}
