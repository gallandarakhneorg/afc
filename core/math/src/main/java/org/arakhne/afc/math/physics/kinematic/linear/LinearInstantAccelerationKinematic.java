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
package org.arakhne.afc.math.physics.kinematic.linear;

import org.arakhne.afc.math.physics.SpeedUnit;

/**
 * This interface describes an object that is able to
 * provide linear instant speed, velocity, and acceleration.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 4.1
 */
public interface LinearInstantAccelerationKinematic
extends LinearInstantVelocityKinematic {

	/**
	 * Returns the linear acceleration of this object in m/s^2.
	 * <p>
	 * The sign of the acceleration indicates if the object
	 * is accelerating (positive) or decelerating (negative).
	 * <p>
	 * The replied value is in <code>[-d;a]</code> where:<ul>
	 * <li><code>d</code> is the max deceleration value, replied
	 * by <code>getMaxLinearDeceleration</code>.</li>
	 * <li><code>a</code> is the max acceleration value, replied
	 * by <code>getMaxLinearAcceleration</code>.</li>
	 * </ul>
	 * 
	 * @return the linear acceleration of this object in m/s^2.
	 */
	public double getLinearAcceleration();
	
	/**
	 * Returns the linear acceleration of this object in the acceleration corresponding to the given speed unit
	 * e.g if the speed unit is m/s the acceleration will be given in m/s^2
	 * <p>
	 * The sign of the acceleration indicates if the object
	 * is accelerating (positive) or decelerating (negative).
	 * <p>
	 * The replied value is in <code>[-d;a]</code> where:<ul>
	 * <li><code>d</code> is the max deceleration value, replied
	 * by <code>getMaxLinearDeceleration</code>.</li>
	 * <li><code>a</code> is the max acceleration value, replied
	 * by <code>getMaxLinearAcceleration</code>.</li>
	 * </ul>
	 * 
	 * @param unit the unit in which the speed will be given.
	 * @return the lineat acceleration of this object in the given unit.
	 */
	public double getLinearAcceleration(SpeedUnit unit);

}
