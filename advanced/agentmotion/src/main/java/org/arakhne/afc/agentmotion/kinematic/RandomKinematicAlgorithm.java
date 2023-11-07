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

package org.arakhne.afc.agentmotion.kinematic;

import java.io.Serializable;
import java.util.Random;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.agentmotion.AgentMotion;
import org.arakhne.afc.agentmotion.RandomMotionAlgorithm;
import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.Vector2D;

/** Agent is changing randomly its position and orientation.
 *
 * <p>This algorithm uses speeds.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
public class RandomKinematicAlgorithm implements RandomMotionAlgorithm, Serializable, Cloneable {

	private static final long serialVersionUID = -8318025671219960417L;

	private Random random = new Random();

	@Pure
	@Override
	public RandomKinematicAlgorithm clone() {
		try {
			final RandomKinematicAlgorithm clone = (RandomKinematicAlgorithm) super.clone();
			clone.random = new Random();
			return clone;
		} catch (CloneNotSupportedException e) {
			throw new Error(e);
		}
	}

	@Override
	public AgentMotion calculate(Point2D<?, ?> position, Vector2D<?, ?> orientation, double linearSpeed,
			double maxLinearSpeed, double angularSpeed, double maxAngularSpeed) {
		final Vector2D<?, ?> v = orientation.toColinearVector(maxLinearSpeed);
		final double rotation = this.random.nextGaussian() * maxAngularSpeed;
		return new AgentMotion(v, rotation);
	}

}
