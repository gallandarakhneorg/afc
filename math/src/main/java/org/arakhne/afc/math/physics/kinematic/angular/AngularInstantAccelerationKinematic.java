/* 
 * $Id$
 * 
 * Copyright (c) 2013 Christophe BOHRHAUER.
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 * This program is free software; you can redistribute it and/or modify
 */
package org.arakhne.afc.math.physics.kinematic.angular;

import org.arakhne.afc.math.unit.AngularUnit;

/**
 * This interface describes an object that is able to
 * provide angular instant speed, velocity, and acceleration.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 4.1
 */
public interface AngularInstantAccelerationKinematic
extends AngularInstantVelocityKinematic {

	/**
	 * Returns the angular acceleration of this object in r/s^2.
	 * <p>
	 * The sign of the acceleration indicates if the object
	 * is accelerating (positive) or decelerating (negative).
	 * <p>
	 * The replied value is in <code>[-d;a]</code> where:<ul>
	 * <li><code>d</code> is the max deceleration value, replied
	 * by <code>getMaxAngularDeceleration</code>.</li>
	 * <li><code>a</code> is the max acceleration value, replied
	 * by <code>getMaxAngularAcceleration</code>.</li>
	 * </ul>
	 * 
	 * @return the angular acceleration of this object in r/s^2.
	 */
	public float getAngularAcceleration();
	
	/**
	 * Returns the angular acceleration of this object in the acceleration corresponding to the given speed unit
	 * e.g if the speed unit is m/s the acceleration will be given in m/s^2
	 * <p>
	 * The sign of the acceleration indicates if the object
	 * is accelerating (positive) or decelerating (negative).
	 * <p>
	 * The replied value is in <code>[-d;a]</code> where:<ul>
	 * <li><code>d</code> is the max deceleration value, replied
	 * by <code>getMaxAngularDeceleration</code>.</li>
	 * <li><code>a</code> is the max acceleration value, replied
	 * by <code>getMaxAngularAcceleration</code>.</li>
	 * </ul>
	 * 
	 * @param unit the unit in which the speed will be given.
	 * @return the angular acceleration of this object in the given unit.
	 */
	public float getAngularAcceleration(AngularUnit unit);

}
