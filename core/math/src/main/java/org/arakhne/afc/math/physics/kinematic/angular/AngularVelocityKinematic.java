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

import org.arakhne.afc.math.physics.AngularUnit;

/**
 * This interface describes an object that is able to
 * provide angular instant speed and velocity, and 
 * the maximal values for the speeds.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 4.1
 */
public interface AngularVelocityKinematic extends AngularInstantVelocityKinematic {

	/**
	 * Returns the maximal angular speed of this object in r/s.
	 * @return the maximal angular speed of this object in r/s,
	 * always >=0.
	 */
	public float getMaxAngularSpeed();

	/**
	 * Returns the maximal angular speed of this object.
	 * 
	 * @param unit the unit in which the speed will be given
	 * @return the maximal angular speed of this object in the given unit,
	 * always >=0.
	 */
	public float getMaxAngularSpeed(AngularUnit unit);
	
}
