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

package org.arakhne.afc.math.physics.kinematic.linear;

import org.eclipse.xtext.xbase.lib.Inline;
import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.physics.SpeedUnit;

/**
 * This interface describes an object that is able to
 * provide linear instant speed and velocity, and
 * the maximal values for the speeds.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public interface LinearVelocityKinematic extends LinearInstantVelocityKinematic {

	/**
	 * Returns the maximal linear speed of this object in m/s.
	 *
	 * @return the maximal linear speed of this object in m/s,
	 *     always &gt;= 0.
	 */
	@Pure
	@Inline(value = "getMaxLinearSpeed($1.METERS_PER_SECOND)", imported = {SpeedUnit.class})
	default double getMaxLinearSpeed() {
		return getMaxLinearSpeed(SpeedUnit.METERS_PER_SECOND);
	}

	/**
	 * Returns the maximal linear speed of this object.
	 *
	 * @param unit the unit in which the speed will be given
	 * @return the maximal linear speed of this object in the given unit,
	 *     always &gt;= 0.
	 */
	@Pure
	double getMaxLinearSpeed(SpeedUnit unit);

}
