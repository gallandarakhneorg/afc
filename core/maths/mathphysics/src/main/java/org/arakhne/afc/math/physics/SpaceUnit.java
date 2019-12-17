/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2019 The original authors, and other authors.
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

package org.arakhne.afc.math.physics;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.vmutil.locale.Locale;


/**
 * A <tt>SpaceUnit</tt> represents space distance at a given unit of
 * granularity.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 * @see MeasureUnitUtil
 */
public enum SpaceUnit {
	/** femtometer.
	 */
	FEMTOMETER,
	/** picometer.
	 */
	PICOMETER,
	/** nanometer.
	 */
	NANOMETER,
	/** micrometer.
	 */
	MICROMETER,
	/** millimeter.
	 */
	MILLIMETER,
	/** centimeter.
	 */
	CENTIMETER,
	/** decimeter.
	 */
	DECIMETER,
	/** meter.
	 */
	METER,
	/** decameter.
	 */
	DECAMETER,
	/** hectometer.
	 */
	HECTOMETER,
	/** kilometer.
	 */
	KILOMETER,
	/** megaeter.
	 */
	MEGAMETER,
	/** gigameter.
	 */
	GIGAMETER,
	/** terameter.
	 */
	TERAMETER;

	/** Replies the space unit just lower than the
	 * current space unit, or the space unit itself if
	 * it is the smallest.
	 * @return the space unit just lower than the
	 *     current space unit, or the space unit itself if
	 *     it is the smallest.
	 */
	@Pure
	public SpaceUnit lower() {
		final int o = ordinal();
		if (o <= 0) {
			return this;
		}
		return values()[o - 1];
	}

	/** Replies the space unit just upper than the
	 * current space unit, or the space unit itself if
	 * it is the uppest.
	 * @return the space unit just upper than the
	 *     current space unit, or the space unit itself if
	 *     it is the uppest.
	 */
	@Pure
	public SpaceUnit upper() {
		final int o = ordinal();
		final SpaceUnit[] units = values();
		if (o >= units.length - 1) {
			return this;
		}
		return units[o + 1];
	}

	/** Replies the localized symbol for this space unit.
	 *
	 * @return the localized symbol for this space unit.
	 */
	@Pure
	public String getSymbol() {
		return Locale.getString(name());
	}

}
