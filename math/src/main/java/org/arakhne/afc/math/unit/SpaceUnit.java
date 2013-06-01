/* 
 * $Id$
 * 
 * Copyright (C) 2010-2011 Janus Core Developers
 * Copyright (C) 2012 Stéphane GALLAND
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.arakhne.afc.math.unit;

import org.arakhne.vmutil.locale.Locale;

/**
 * A <tt>SpaceUnit</tt> represents space distance at a given unit of
 * granularity.
 *
 * @see MeasureUnitUtil
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
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
	 * current space unit, or the space unit itself if
	 * it is the smallest.
	 * @since 4.0
	 */
	public SpaceUnit lower() {
		int o = ordinal();
		if (o<=0) return this;
		return values()[o-1];
	}
	
	/** Replies the space unit just upper than the
	 * current space unit, or the space unit itself if
	 * it is the uppest.
	 * @return the space unit just upper than the
	 * current space unit, or the space unit itself if
	 * it is the uppest.
	 * @since 4.0
	 */
	public SpaceUnit upper() {
		int o = ordinal();
		SpaceUnit[] units = values();
		if (o>=units.length-1) return this;
		return units[o+1];
	}
	
	/** Replies the localized symbol for this space unit.
	 * 
	 * @return the localized symbol for this space unit.
	 * @since 4.0
	 */
	public String getSymbol() {
		return Locale.getString(name());
	}

}