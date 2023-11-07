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

package org.arakhne.afc.math.physics;

import java.util.concurrent.TimeUnit;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.vmutil.locale.Locale;

/**
 * A <tt>SpeedUnit</tt> represents speed at a given unit of
 * granularity.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 * @see MeasureUnitUtil
 */
public enum SpeedUnit {

	/** millimeters/second.
	 */
	MILLIMETERS_PER_SECOND,
	/** meters/second.
	 */
	METERS_PER_SECOND,
	/** kilometer/hour.
	 */
	KILOMETERS_PER_HOUR;

	/** Replies the time unit that corresponds to this speed unit.
	 *
	 * @return the time unit.
	 * @throws IllegalArgumentException should never append
	 */
	@Pure
	public TimeUnit toTimeUnit() {
		switch (this) {
		case KILOMETERS_PER_HOUR:
			return TimeUnit.HOURS;
		case METERS_PER_SECOND:
		case MILLIMETERS_PER_SECOND:
			return TimeUnit.SECONDS;
		default:
			throw new IllegalArgumentException();
		}
	}

	/** Replies the space unit that corresponds to this speed unit.
	 *
	 * @return the space unit.
	 * @throws IllegalArgumentException should never append
	 */
	@Pure
	public SpaceUnit toSpaceUnit() {
		switch (this) {
		case KILOMETERS_PER_HOUR:
			return SpaceUnit.KILOMETER;
		case METERS_PER_SECOND:
			return SpaceUnit.METER;
		case MILLIMETERS_PER_SECOND:
			return SpaceUnit.MILLIMETER;
		default:
			throw new IllegalArgumentException();
		}
	}

	/** Replies the speed unit just lower than the
	 * current speed unit, or the speed unit itself if
	 * it is the smallest.
	 * @return the speed unit just lower than the
	 *     current speed unit, or the speed unit itself if
	 *     it is the smallest.
	 */
	@Pure
	public SpeedUnit lower() {
		final int o = ordinal();
		if (o <= 0) {
			return this;
		}
		return values()[o - 1];
	}

	/** Replies the speed unit just upper than the
	 * current speed unit, or the speed unit itself if
	 * it is the uppest.
	 * @return the speed unit just upper than the
	 *     current speed unit, or the speed unit itself if
	 *     it is the uppest.
	 */
	@Pure
	public SpeedUnit upper() {
		final int o = ordinal();
		final SpeedUnit[] units = values();
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
