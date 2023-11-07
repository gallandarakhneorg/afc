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

package org.arakhne.afc.agentmotion;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.Vector2D;

/** Agent is changing its position for pursuing the target point.
 *
 * <p>Pursuing is seeking a target position by predicting its next position.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
public interface PursuingMotionAlgorithm {

	/** Clone this algorithm.
	 *
	 * @return the clone.
	 */
	@Pure
	PursuingMotionAlgorithm clone();

	/** Calculate the linear motion for pursuing the target point.
	 *
	 * @param position is the current position of the entity.
	 * @param linearSpeed is the current linear speed of the entity.
	 * @param maxLinear is the maximal linear speed or acceleration of the entity.
	 * @param targetPosition is the position of the target.
	 * @param targetLinearMotion is the linear motion of the target.
	 * @return the agent motion.
	 */
	@Pure
	Vector2D<?, ?> calculate(Point2D<?, ?> position, double linearSpeed, double maxLinear,
			Point2D<?, ?> targetPosition, Vector2D<?, ?> targetLinearMotion);

	/** Predict the next position of the target.
	 *
	 * @param targetPosition is the position of the target.
	 * @param targetLinearMotion is the linear motion of the target.
	 * @return the predicted position.
	 */
	@Pure
	Point2D<?, ?> predictTargetPosition(Point2D<?, ?> targetPosition, Vector2D<?, ?> targetLinearMotion);

}
