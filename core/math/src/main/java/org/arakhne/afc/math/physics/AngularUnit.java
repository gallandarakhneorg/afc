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
package org.arakhne.afc.math.physics;

import java.util.concurrent.TimeUnit;


/**
 * A <tt>AngularUnit</tt> represents speed at a given unit of
 * granularity.
 *
 * @see MeasureUnitUtil
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public enum AngularUnit {

	/** turns/second.
	 */
	TURNS_PER_SECOND,
	/** radians/second.
	 */
	RADIANS_PER_SECOND,
	/** degrees/second.
	 */
	DEGREES_PER_SECOND;
	
	/** Replies the time unit that corresponds to this angular unit.
	 * 
	 * @return the time unit.
	 */
	public TimeUnit toTimeUnit() {
		switch(this) {
		case TURNS_PER_SECOND:
		case RADIANS_PER_SECOND:
		case DEGREES_PER_SECOND:
			return TimeUnit.SECONDS;
		default:
			throw new IllegalArgumentException();
		}
	}

}