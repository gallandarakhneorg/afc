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

package org.arakhne.afc.math.physics.kinematic.linear;

import org.eclipse.xtext.xbase.lib.Inline;
import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.geometry.d1.Vector1D;
import org.arakhne.afc.math.geometry.d2.Vector2D;
import org.arakhne.afc.math.geometry.d3.Vector3D;
import org.arakhne.afc.math.physics.SpeedUnit;

/**
 * This interface describes an object that is able to
 * provide linear instant speed and velocity.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public interface LinearInstantVelocityKinematic {

	/**
	 * Returns the linear speed of this object in m/s.
	 *
	 * <p>The sign of the linear speed indicates if the object is
	 * going forward (positive) or backward (negative).
	 *
	 * @return the linear speed of this object in m/s.
	 */
	@Pure
	@Inline(value = "getLinearSpeed($1.METERS_PER_SECOND)", imported = {SpeedUnit.class})
	default double getLinearSpeed() {
		return getLinearSpeed(SpeedUnit.METERS_PER_SECOND);
	}

	/**
	 * Returns the Linear speed of this object.
	 *
	 * <p>The sign of the linear speed indicates if the object is
	 * going forward (positive) or backward (negative).
	 *
	 * @param unit the unit in which the speed will be given.
	 * @return the linear speed of this object in the given unit.
	 */
	@Pure
	double getLinearSpeed(SpeedUnit unit);


	/** Replies the instant velocity of the object.
	 * The velocity is the motion vector with a length
	 * equal to the speed replied by {@link #getLinearSpeed()}.
	 *
	 * <p>This function replies the velocity in 3D if possible.
	 *
	 * @return the velocity of the object.
	 * @throws UnsupportedOperationException not supported yet.
	 */
	@Pure
	default Vector3D<?, ?> getLinearVelocity3D() {
		throw new UnsupportedOperationException();
	}

	/** Replies the instant velocity of the object.
	 * The velocity is the motion vector with a length
	 * equal to the speed replied by {@link #getLinearSpeed()}.
	 *
	 * <p>This function replies the velocity in 2D if possible.
	 *
	 * @return the velocity of the object.
	 * @throws UnsupportedOperationException not supported yet.
	 */
	@Pure
	default Vector2D<?, ?> getLinearVelocity2D() {
		throw new UnsupportedOperationException();
	}

	/** Replies the instant velocity of the object.
	 * The velocity is the motion vector with a length
	 * equal to the speed replied by {@link #getLinearSpeed()}.
	 *
	 * <p>This function replies the velocity in 1.5D if possible.
	 *
	 * @return the velocity of the object.
	 * @throws UnsupportedOperationException not supported yet.
	 */
	@Pure
	default Vector1D<?, ?, ?> getLinearVelocity1D5() {
		throw new UnsupportedOperationException();
	}

	/** Replies the instant velocity of the object.
	 * The velocity is the motion vector with a length
	 * equal to the speed replied by {@link #getLinearSpeed()}.
	 *
	 * <p>This function replies the velocity in 1D if possible.
	 *
	 * @return the velocity of the object.
	 */
	@Pure
	default double getLinearVelocity1D() {
		return getLinearSpeed();
	}

}
