/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2022 The original authors, and other authors.
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

import org.arakhne.afc.agentmotion.PursuingMotionAlgorithm;
import org.arakhne.afc.agentmotion.SeekingMotionAlgorithm;
import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.Vector2D;
import org.arakhne.afc.vmutil.asserts.AssertMessages;

/** Agent is changing its position for pursuing the target point.
 *
 * <p>This algorithm uses speeds or accelerations depending of the seeking algorithm to delegate to.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
public class PursuingAlgorithm extends AbstractTargetPositionPredictionAlgorithm
		implements PursuingMotionAlgorithm {

	private static final long serialVersionUID = -8318025671219960417L;

	/** Seeking algorithm to delegate to.
	 */
	protected final SeekingMotionAlgorithm seeking;

	/** Constructor.
	 *
	 * @param seeking the seeking algorithm to delegate to.
	 * @param predictionDuration the duration of prediction.
	 */
	public PursuingAlgorithm(SeekingMotionAlgorithm seeking, double predictionDuration) {
		super(predictionDuration);
		assert seeking != null : AssertMessages.notNullParameter(0);
		this.seeking = seeking;
	}

	@Pure
	@Override
	public boolean equals(Object obj) {
		if (!super.equals(obj)) {
			return false;
		}
		assert obj != null;
		final PursuingAlgorithm algo = (PursuingAlgorithm) obj;
		return algo.seeking.equals(this.seeking);
	}

	@Pure
	@Override
	public int hashCode() {
		int bits = 1;
		bits = 31 * bits + super.hashCode();
		bits = 31 * bits + this.seeking.hashCode();
		return bits ^ (bits >> 31);
	}

	@Pure
	@Override
	public PursuingAlgorithm clone() {
		return (PursuingAlgorithm) super.clone();
	}

	@Override
	public Vector2D<?, ?> calculate(Point2D<?, ?> position, double linearSpeed, double maxLinear,
			Point2D<?, ?> targetPosition, Vector2D<?, ?> targetLinearMotion) {
		return this.seeking.calculate(position, linearSpeed, maxLinear,
				predictTargetPosition(targetPosition, targetLinearMotion));
	}

}
