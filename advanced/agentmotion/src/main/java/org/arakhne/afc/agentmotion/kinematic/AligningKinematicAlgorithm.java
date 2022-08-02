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

package org.arakhne.afc.agentmotion.kinematic;

import java.io.Serializable;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.agentmotion.AligningMotionAlgorithm;
import org.arakhne.afc.math.MathUtil;
import org.arakhne.afc.math.geometry.d2.Vector2D;
import org.arakhne.afc.vmutil.asserts.AssertMessages;

/** Agent is changing its oriented for being align to the target vector.
 *
 * <p>This algorithm uses speeds.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
public class AligningKinematicAlgorithm implements AligningMotionAlgorithm, Serializable, Cloneable {

	private static final long serialVersionUID = -8318025671219960417L;

	/** Angle to the target under which the agent could stop.
	 */
	protected final double stopAngle;

	/** Constructor.
	 *
	 * @param stopAngle the angle to the target under which the agent could stop.
	 */
	public AligningKinematicAlgorithm(double stopAngle) {
		assert stopAngle >= 0. : AssertMessages.positiveOrZeroParameter();
		this.stopAngle = stopAngle;
	}

	@Pure
	@Override
	public AligningKinematicAlgorithm clone() {
		try {
			return (AligningKinematicAlgorithm) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new Error(e);
		}
	}

	@Override
	public double calculate(Vector2D<?, ?> orientation, double angularSpeed, double maxAngularSpeed, Vector2D<?, ?> target) {
		final double angle = orientation.signedAngle(target);
		if (Math.abs(angle) <= this.stopAngle) {
			return 0;
		}
		return MathUtil.clamp(angle, -maxAngularSpeed, maxAngularSpeed);
	}

}
