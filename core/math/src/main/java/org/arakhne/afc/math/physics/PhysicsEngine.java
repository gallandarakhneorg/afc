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
package org.arakhne.afc.math.physics;

import org.arakhne.afc.math.geometry.d2.continuous.Vector2f;
import org.arakhne.afc.math.geometry.d3.continuous.Vector3f;

/**
 * Some physic utility functions implementing in Java.
 * <p>
 * Definitions:
 * <ul>
 * <li><a href="http://en.wikipedia.org/wiki/Acceleration">Acceleration</a></li>
 * <li><a href="http://en.wikipedia.org/wiki/Velocity">Velocity</a></li>
 * <li><a href="http://en.wikipedia.org/wiki/Speed">Speed</a></li>
 * <li><a href="http://en.wikipedia.org/wiki/Equations_of_Motion">Equations of Motion</a></li>
 * <li><a href="http://en.wikipedia.org/wiki/Newton%E2%80%93Euler_equations">Newton-Euler Equations</a></li>
 * </ul>
 * 
 * @author $Author: cbohrhauer$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public interface PhysicsEngine {

	/** Compute and replies a motion according
	 * to high school physics Newton's equations for motion.
	 * <p>
	 * From Equation 2 of the SUVAT form:<br>
	 * <code>movement = speed * dt + 0.5 * acceleration * dt * dt</code>
	 * 
	 * @param speed is the current speed of the object.
	 * @param acceleration is the current acceleration of the object.
	 * @param dt is the time
	 * @return a motion
	 */
	public double motionNewtonLaw(
			double speed,
			double acceleration, 
			double dt);

	/** Compute and replies a motion according
	 * to high school physics Newton's equations for motion.
	 * <p>
	 * This function allows to clamp acceleration and velocity.
	 * <p>
	 * From Equation 2 of the SUVAT form:<br>
	 * <code>clamped_acceleration = clamp(acceleration, minAcceleration, maxAcceleration)</code><br>
	 * <code>new_velocity = velocity + 0.5 * clamped_acceleration * dt</code><br>
	 * <code>clamped_velocity = clamp(new_velocity, minSpeed, maxSpeed)</code><br>
	 * <code>motion = clamped_velocity * dt</code><br>
	 * 
	 * @param velocity is the current velocity of the object.
	 * @param minSpeed is the minimal speed allowed.
	 * @param maxSpeed is the maximal speed allowed.
	 * @param acceleration is the current acceleration of the object.
	 * @param minAcceleration is the minimal acceleration allowed.
	 * @param maxAcceleration is the maximal  acceleration allowed.
	 * Length of this vector is the acceleration amount. Direction of this
	 * vector becomes movement direction.
	 * @param dt is the time.
	 * @return the motion
	 * @see "http://en.wikibooks.org/wiki/High_School_Physics/Velocity"
	 */
	public double motionNewtonLaw1D(
			double velocity,
			double minSpeed,
			double maxSpeed,
			double acceleration, 
			double minAcceleration,
			double maxAcceleration,
			double dt);

	/** Compute and replies a motion according
	 * to high school physics Newton's equations for motion.
	 * <p>
	 * This function allows to clamp acceleration and velocity.
	 * <p>
	 * From Equation 2 of the SUVAT form:<br>
	 * <code>clamped_acceleration = clamp(acceleration, minAcceleration, maxAcceleration)</code><br>
	 * <code>new_velocity = velocity + 0.5 * clamped_acceleration * dt</code><br>
	 * <code>clamped_velocity = clamp(new_velocity, minSpeed, maxSpeed)</code><br>
	 * <code>motion = clamped_velocity * dt</code><br>
	 * 
	 * @param velocity is the current velocity of the object. Norm of vector is speed in m/s for example.
	 * @param minSpeed is the minimal speed allowed.
	 * @param maxSpeed is the maximal speed allowed.
	 * @param acceleration is the current acceleration of the object. Norm of vector is acceleration amount in m/s<sup>2</sup> for example.
	 * @param minAcceleration is the minimal acceleration allowed.
	 * @param maxAcceleration is the maximal  acceleration allowed.
	 * Length of this vector is the acceleration amount. Direction of this
	 * vector becomes movement direction.
	 * @param dt is the time.
	 * @return the motion.
	 * @see "http://en.wikibooks.org/wiki/High_School_Physics/Velocity"
	 */
	public Vector2f motionNewtonLaw1D5(
			Vector2f velocity,
			double minSpeed,
			double maxSpeed,
			Vector2f acceleration, 
			double minAcceleration,
			double maxAcceleration,
			double dt);

	/** Compute and replies a motion according
	 * to high school physics Newton's equations for motion.
	 * <p>
	 * This function allows to clamp acceleration and velocity.
	 * <p>
	 * From Equation 2 of the SUVAT form:<br>
	 * <code>clamped_acceleration = clamp(acceleration, minAcceleration, maxAcceleration)</code><br>
	 * <code>new_velocity = velocity + 0.5 * clamped_acceleration * dt</code><br>
	 * <code>clamped_velocity = clamp(new_velocity, minSpeed, maxSpeed)</code><br>
	 * <code>motion = clamped_velocity * dt</code><br>
	 * 
	 * @param velocity is the current velocity of the object. Norm of vector is speed in m/s for example.
	 * @param minSpeed is the minimal speed allowed.
	 * @param maxSpeed is the maximal speed allowed.
	 * @param acceleration is the current acceleration of the object. Norm of vector is acceleration amount in m/s<sup>2</sup> for example.
	 * @param minAcceleration is the minimal acceleration allowed.
	 * @param maxAcceleration is the maximal  acceleration allowed.
	 * Length of this vector is the acceleration amount. Direction of this
	 * vector becomes movement direction.
	 * @param dt is the time.
	 * @return the motion.
	 * @see "http://en.wikibooks.org/wiki/High_School_Physics/Velocity"
	 */
	public Vector2f motionNewtonLaw2D(
			Vector2f velocity,
			double minSpeed,
			double maxSpeed,
			Vector2f acceleration, 
			double minAcceleration,
			double maxAcceleration,
			double dt);
	
	/** Compute and replies a motion according
	 * to high school physics Newton's equations for motion.
	 * <p>
	 * This function allows to clamp acceleration and velocity.
	 * <p>
	 * From Equation 2 of the SUVAT form:<br>
	 * <code>clamped_acceleration = clamp(acceleration, minAcceleration, maxAcceleration)</code><br>
	 * <code>new_velocity = velocity + 0.5 * clamped_acceleration * dt</code><br>
	 * <code>clamped_velocity = clamp(new_velocity, minSpeed, maxSpeed)</code><br>
	 * <code>motion = clamped_velocity * dt</code><br>
	 * 
	 * @param velocity is the current velocity of the object. Norm of vector is speed in m/s for example.
	 * @param minSpeed is the minimal speed allowed.
	 * @param maxSpeed is the maximal speed allowed.
	 * @param acceleration is the current acceleration of the object. Norm of vector is acceleration amount in m/s<sup>2</sup> for example.
	 * @param minAcceleration is the minimal acceleration allowed.
	 * @param maxAcceleration is the maximal  acceleration allowed.
	 * Length of this vector is the acceleration amount. Direction of this
	 * vector becomes movement direction.
	 * @param dt is the time.
	 * @return the motion.
	 * @see "http://en.wikibooks.org/wiki/High_School_Physics/Velocity"
	 */
	public Vector3f motionNewtonLaw2D5(
			Vector3f velocity,
			double minSpeed,
			double maxSpeed,
			Vector3f acceleration, 
			double minAcceleration,
			double maxAcceleration,
			double dt);
	
	/** Compute and replies a motion according
	 * to high school physics Newton's equations for motion.
	 * <p>
	 * This function allows to clamp acceleration and velocity.
	 * <p>
	 * From Equation 2 of the SUVAT form:<br>
	 * <code>clamped_acceleration = clamp(acceleration, minAcceleration, maxAcceleration)</code><br>
	 * <code>new_velocity = velocity + 0.5 * clamped_acceleration * dt</code><br>
	 * <code>clamped_velocity = clamp(new_velocity, minSpeed, maxSpeed)</code><br>
	 * <code>motion = clamped_velocity * dt</code><br>
	 * 
	 * @param velocity is the current velocity of the object. Norm of vector is speed in m/s for example.
	 * @param minSpeed is the minimal speed allowed.
	 * @param maxSpeed is the maximal speed allowed.
	 * @param acceleration is the current acceleration of the object. Norm of vector is acceleration amount in m/s<sup>2</sup> for example.
	 * @param minAcceleration is the minimal acceleration allowed.
	 * @param maxAcceleration is the maximal  acceleration allowed.
	 * Length of this vector is the acceleration amount. Direction of this
	 * vector becomes movement direction.
	 * @param dt is the time.
	 * @return the motion.
	 * @see "http://en.wikibooks.org/wiki/High_School_Physics/Velocity"
	 */
	public Vector3f motionNewtonLaw3D(
			Vector3f velocity,
			double minSpeed,
			double maxSpeed,
			Vector3f acceleration, 
			double minAcceleration,
			double maxAcceleration,
			double dt);

	/** Compute and replies a motion according
	 * to Newton-Euler-1 equations for motion
	 * (where acceleration is not significant).
	 * <p>
	 * <p>
	 * From the first-order Newton-Euler method:
	 * <code>movement = speed * dt</code>
	 * 
	 * @param speed is the current  speed of the object.
	 * @param dt is the time
	 * @return a motion
	 */
	public double motionNewtonEuler1Law(
			double speed,
			double dt);
	
	/** Compute and replies a motion according
	 * to Newton-Euler-1 equations for motion
	 * (where acceleration is not significant).
	 * <p>
	 * From the first-order Newton-Euler method:
	 * <code>movement = clamp(velocity) * dt</code>
	 * 
	 * @param velocity is the current  velocity of the object.
	 * @param minSpeed is the minimal speed allowed.
	 * @param maxSpeed is the maximal speed allowed.
	 * @param dt is the time
	 * @return a motion
	 */
	public double motionNewtonEuler1Law1D(
			double velocity,
			double minSpeed,
			double maxSpeed,
			double dt);

	/** Compute and replies a motion according
	 * to Newton-Euler-1 equations for motion
	 * (where acceleration is not significant).
	 * <p>
	 * From the first-order Newton-Euler method:
	 * <code>movement = clamp(velocity) * dt</code>
	 * 
	 * @param velocity is the current  velocity of the object.
	 * @param minSpeed is the minimal speed allowed (clamped to 0 if negative).
	 * @param maxSpeed is the maximal speed allowed.
	 * @param dt is the time
	 * @return a motion
	 */
	public Vector2f motionNewtonEuler1Law1D5(
			Vector2f velocity,
			double minSpeed,
			double maxSpeed,
			double dt);

	/** Compute and replies a motion according
	 * to Newton-Euler-1 equations for motion
	 * (where acceleration is not significant).
	 * <p>
	 * From the first-order Newton-Euler method:
	 * <code>movement = clamp(velocity) * dt</code>
	 * 
	 * @param velocity is the current  velocity of the object.
	 * @param minSpeed is the minimal speed allowed (clamped to 0 if negative).
	 * @param maxSpeed is the maximal speed allowed.
	 * @param dt is the time
	 * @return a motion
	 */
	public Vector2f motionNewtonEuler1Law2D(
			Vector2f velocity,
			double minSpeed,
			double maxSpeed,
			double dt);

	/** Compute and replies a motion according
	 * to Newton-Euler-1 equations for motion
	 * (where acceleration is not significant).
	 * <p>
	 * From the first-order Newton-Euler method:
	 * <code>movement = clamp(velocity) * dt</code>
	 * 
	 * @param velocity is the current  velocity of the object.
	 * @param minSpeed is the minimal speed allowed (clamped to 0 if negative).
	 * @param maxSpeed is the maximal speed allowed.
	 * @param dt is the time
	 * @return a motion
	 */
	public Vector3f motionNewtonEuler1Law2D5(
			Vector3f velocity,
			double minSpeed,
			double maxSpeed,
			double dt);

	/** Compute and replies a motion according
	 * to Newton-Euler-1 equations for motion
	 * (where acceleration is not significant).
	 * <p>
	 * From the first-order Newton-Euler method:
	 * <code>movement = clamp(velocity) * dt</code>
	 * 
	 * @param velocity is the current  velocity of the object.
	 * @param minSpeed is the minimal speed allowed (clamped to 0 if negative).
	 * @param maxSpeed is the maximal speed allowed.
	 * @param dt is the time
	 * @return a motion
	 */
	public Vector3f motionNewtonEuler1Law3D(
			Vector3f velocity,
			double minSpeed,
			double maxSpeed,
			double dt);

	/** Replies the new speed according to a previous speed and
	 * a mouvement during a given time.
	 * <p>
	 * From the first-order Newton-Euler method:
	 * <code>speed = movement / dt</code>
	 * 
	 * @param movement is the movement distance.
	 * @param dt is the time
	 * @return a new speed
	 * @since 4.1
	 */
	public double speed(double movement, double dt);

	/** Replies the new  acceleration according to a previous 
	 * speed and a current speed, and given time.
	 * <p>
	 * From the second-order Newton-Euler method:
	 * <code>(currentSpeed - previousSpeed) / dt</code>
	 * 
	 * @param previousSpeed is the previous  speed of the object.
	 * @param currentSpeed is the current  speed of the object.
	 * @param dt is the time
	 * @return a new acceleration
	 */
	public double acceleration(
			double previousSpeed,
			double currentSpeed, 
			double dt);

}
