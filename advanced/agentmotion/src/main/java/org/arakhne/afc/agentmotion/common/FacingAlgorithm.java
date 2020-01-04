/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2020 The original authors, and other authors.
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

import org.arakhne.afc.agentmotion.AligningMotionAlgorithm;
import org.arakhne.afc.agentmotion.FacingMotionAlgorithm;
import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.Vector2D;
import org.arakhne.afc.math.geometry.d2.d.Vector2d;
import org.arakhne.afc.vmutil.asserts.AssertMessages;

/** Agent is changing its orientation for facing the target point.
 *
 * <p>This algorithm uses speeds or accelerations depending of the seeking algorithm to delegate to.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
public class FacingAlgorithm implements FacingMotionAlgorithm, Serializable, Cloneable {

	private static final long serialVersionUID = -8318025671219960417L;

	/** Aligning algorithm to delegate to.
	 */
	protected final AligningMotionAlgorithm aligning;

	/** Constructor.
	 *
	 * @param aligning the aligning algorithm to delegate to.
	 */
	public FacingAlgorithm(AligningMotionAlgorithm aligning) {
		assert aligning != null : AssertMessages.notNullParameter(0);
		this.aligning = aligning;
	}

	@Pure
	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj != null && obj.getClass() == getClass()) {
			final FacingAlgorithm algo = (FacingAlgorithm) obj;
			return algo.aligning.equals(this.aligning);
		}
		return false;
	}

	@Pure
	@Override
	public int hashCode() {
		return this.aligning.hashCode();
	}

	@Pure
	@Override
	public FacingAlgorithm clone() {
		try {
			return (FacingAlgorithm) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new Error(e);
		}
	}

	@Override
	public double calculate(Point2D<?, ?> position, Vector2D<?, ?> orientation, double angularSpeed, double maxAngular,
			Point2D<?, ?> target) {
		final Vector2d alignTarget = new Vector2d(
				target.getX() - position.getX(),
				target.getY() - position.getY());
		return this.aligning.calculate(orientation, angularSpeed, maxAngular, alignTarget);
	}

}
