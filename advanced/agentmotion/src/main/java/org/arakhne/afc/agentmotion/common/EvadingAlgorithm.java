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

package org.arakhne.afc.agentmotion.common;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.agentmotion.EvadingMotionAlgorithm;
import org.arakhne.afc.agentmotion.FleeingMotionAlgorithm;
import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.Vector2D;
import org.arakhne.afc.vmutil.asserts.AssertMessages;

/** Agent is changing its position for evading the target vector.
 *
 * <p>This algorithm uses speeds or accelerations depending of the fleeing algorithm to delegate to.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
public class EvadingAlgorithm extends AbstractTargetPositionPredictionAlgorithm
		implements EvadingMotionAlgorithm {

	private static final long serialVersionUID = -8318025671219960417L;

	/** Fleeing algorithm to delegate to.
	 */
	protected final FleeingMotionAlgorithm fleeing;

	/** Constructor.
	 *
	 * @param fleeing the fleeing algorithm to delegate to.
	 * @param predictionDuration the duration of prediction.
	 */
	public EvadingAlgorithm(FleeingMotionAlgorithm fleeing, double predictionDuration) {
		super(predictionDuration);
		assert fleeing != null : AssertMessages.notNullParameter(0);
		this.fleeing = fleeing;

	}

	@Pure
	@Override
	public boolean equals(Object obj) {
		if (!super.equals(obj)) {
			return false;
		}
		assert obj != null;
		final EvadingAlgorithm algo = (EvadingAlgorithm) obj;
		return algo.fleeing.equals(this.fleeing);
	}

	@Pure
	@Override
	public int hashCode() {
		int bits = 1;
		bits = 31 * bits + super.hashCode();
		bits = 31 * bits + this.fleeing.hashCode();
		return bits ^ (bits >> 31);
	}

	@Pure
	@Override
	public EvadingAlgorithm clone() {
		return (EvadingAlgorithm) super.clone();
	}

	@Override
	public Vector2D<?, ?> calculate(Point2D<?, ?> position, double linearSpeed, double maxLinear,
			Point2D<?, ?> targetPosition, Vector2D<?, ?> targetLinearMotion) {
		return this.fleeing.calculate(position, linearSpeed, maxLinear,
				predictTargetPosition(targetPosition, targetLinearMotion));
	}

}
