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

import java.io.Serializable;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.Vector2D;
import org.arakhne.afc.math.geometry.d2.d.Point2d;
import org.arakhne.afc.vmutil.asserts.AssertMessages;

/** Algorithm that is providing a simple target position prediction.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
public abstract class AbstractTargetPositionPredictionAlgorithm implements Serializable, Cloneable {

	private static final long serialVersionUID = -8318025671219960417L;

	/** Duration of prediction.
	 */
	protected final double predictionDuration;

	/** Constructor.
	 *
	 * @param predictionDuration the duration of prediction.
	 */
	public AbstractTargetPositionPredictionAlgorithm(double predictionDuration) {
		assert predictionDuration > 0. : AssertMessages.positiveStrictlyParameter(1);
		this.predictionDuration = predictionDuration;
	}

	@Pure
	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj != null && obj.getClass() == getClass()) {
			final AbstractTargetPositionPredictionAlgorithm algo = (AbstractTargetPositionPredictionAlgorithm) obj;
			return algo.predictionDuration == this.predictionDuration;
		}
		return false;
	}

	@Pure
	@Override
	public int hashCode() {
		return Double.hashCode(this.predictionDuration);
	}

	@Pure
	@Override
	public AbstractTargetPositionPredictionAlgorithm clone() {
		try {
			return (AbstractTargetPositionPredictionAlgorithm) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new Error(e);
		}
	}

	/** Predict the next position of the target.
	 *
	 * @param targetPosition is the position of the target.
	 * @param targetLinearMotion is the linear motion of the target.
	 * @return the predicted position.
	 */
	@Pure
	public Point2D<?, ?> predictTargetPosition(Point2D<?, ?> targetPosition, Vector2D<?, ?> targetLinearMotion) {
		return new Point2d(
				targetPosition.getX() + targetLinearMotion.getX() * this.predictionDuration,
				targetPosition.getY() + targetLinearMotion.getY() * this.predictionDuration);
	}

}
