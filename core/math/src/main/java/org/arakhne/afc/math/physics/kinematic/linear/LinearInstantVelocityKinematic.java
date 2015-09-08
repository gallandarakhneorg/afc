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

import org.arakhne.afc.math.geometry.d2.continuous.Vector2f;
import org.arakhne.afc.math.geometry.d3.continuous.Vector3f;
import org.arakhne.afc.math.physics.SpeedUnit;

/**
 * This interface describes an object that is able to
 * provide linear instant speed and velocity.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 4.1
 */
public interface LinearInstantVelocityKinematic {

	/**
	 * Returns the linear speed of this object in m/s.
	 * <p>
	 * The sign of the linear speed indicates if the object is
	 * going forward (positive) or backward (negative). 
	 * 
	 * @return the linear speed of this object in m/s.
	 */
	public double getLinearSpeed();
	
	/**
	 * Returns the Linear speed of this object.
	 * <p>
	 * The sign of the linear speed indicates if the object is
	 * going forward (positive) or backward (negative).
	 *  
	 * @param unit the unit in which the speed will be given.
	 * @return the linear speed of this object in the given unit.
	 */
	public double getLinearSpeed(SpeedUnit unit);

	
	/** Replies the instant velocity of the object.
	 * The velocity is the motion vector with a length
	 * equal to the speed replied by {@link #getLinearSpeed()}.
	 * <p>
	 * This function replies the velocity in 3D if possible.
	 * 
	 * @return the velocity of the object. 
	 */
	public Vector3f getLinearVelocity3D();
	
	/** Replies the instant velocity of the object.
	 * The velocity is the motion vector with a length
	 * equal to the speed replied by {@link #getLinearSpeed()}.
	 * <p>
	 * This function replies the velocity in 2D if possible.
	 * 
	 * @return the velocity of the object. 
	 */
	public Vector2f getLinearVelocity2D();

	/** Replies the instant velocity of the object.
	 * The velocity is the motion vector with a length
	 * equal to the speed replied by {@link #getLinearSpeed()}.
	 * <p>
	 * This function replies the velocity in 1.5D if possible.
	 * 
	 * @return the velocity of the object. 
	 */
	public Vector2f getLinearVelocity1D5();

	/** Replies the instant velocity of the object.
	 * The velocity is the motion vector with a length
	 * equal to the speed replied by {@link #getLinearSpeed()}.
	 * <p>
	 * This function replies the velocity in 1D if possible.
	 * 
	 * @return the velocity of the object. 
	 */
	public double getLinearVelocity1D();

}
