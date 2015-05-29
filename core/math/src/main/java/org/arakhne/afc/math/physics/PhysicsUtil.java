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

import org.arakhne.afc.math.geometry2d.continuous.Vector2f;
import org.arakhne.afc.math.geometry3d.continuous.Vector3f;

/**
 * Some physic utility functions.
 * <p>
 * Definitions:
 * <ul>
 * <li><a href="http://en.wikipedia.org/wiki/Acceleration">Acceleration</a></li>
 * <li><a href="http://en.wikipedia.org/wiki/Velocity">Velocity</a></li>
 * <li><a href="http://en.wikipedia.org/wiki/Speed">Speed</a></li>
 * <li><a href="http://en.wikipedia.org/wiki/Equations_of_Motion">Equations of Motion</a></li>
 * </ul>
 * 
 * @author $Author: cbohrhauer$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class PhysicsUtil {

	private static PhysicsEngine engine = new JavaPhysicsEngine();
	
	/**
	 * 
	 */
	protected PhysicsUtil() {
		//
	}
	
	/** Replies the current physics engine.
	 * 
	 * @return the current physics engine.
	 */
	public static PhysicsEngine getPhysicsEngine() {
		return engine;
	}

	/** Set the current physics engine.
	 * <p>
	 * If the given engine is <code>null</code>, the default physics engine
	 * is used (java implementation).
	 * 
	 * @param newEngine the current physics engine, or <code>null</code> for default engine.
	 * @return previous physics engine.
	 */
	public static PhysicsEngine setPhysicsEngine(PhysicsEngine newEngine) {
		PhysicsEngine oldEngine = engine;
		if (newEngine==null)
			engine = new JavaPhysicsEngine();
		else
			engine = newEngine;
		return oldEngine;
	}

	/** Compute and replies a motion according
	 * to high school physics Newton's equations for motion.
	 * <p>
	 * <code>movement = speed * dt + 0.5 * acceleration * dt * dt</code>
	 * 
	 * @param speed is the current speed of the object.
	 * @param acceleration is the current acceleration of the object.
	 * @param dt is the time
	 * @return a motion
	 */
	public static float motionNewtonLaw(
			float speed,
			float acceleration, 
			float dt) {
		return engine.motionNewtonLaw(speed, acceleration, dt);
	}

	/** Compute and replies a motion according
	 * to high school physics Newton's equations for motion.
	 * <p>
	 * This function allows to clamp acceleration and velocity.
	 * <p>
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
	public static float motionNewtonLaw1D(
			float velocity,
			float minSpeed,
			float maxSpeed,
			float acceleration, 
			float minAcceleration,
			float maxAcceleration,
			float dt) {
		return engine.motionNewtonLaw1D(velocity, minSpeed, maxSpeed, acceleration, minAcceleration, maxAcceleration, dt);
	}

	/** Compute and replies a motion according
	 * to high school physics Newton's equations for motion.
	 * <p>
	 * This function allows to clamp acceleration and velocity.
	 * <p>
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
	public static Vector2f motionNewtonLaw1D5(
			Vector2f velocity,
			float minSpeed,
			float maxSpeed,
			Vector2f acceleration, 
			float minAcceleration,
			float maxAcceleration,
			float dt) {
		return engine.motionNewtonLaw1D5(velocity, minSpeed, maxSpeed, acceleration, minAcceleration, maxAcceleration, dt);
	}

	/** Compute and replies a motion according
	 * to high school physics Newton's equations for motion.
	 * <p>
	 * This function allows to clamp acceleration and velocity.
	 * <p>
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
	public static Vector2f motionNewtonLaw2D(
			Vector2f velocity,
			float minSpeed,
			float maxSpeed,
			Vector2f acceleration, 
			float minAcceleration,
			float maxAcceleration,
			float dt) {
		return engine.motionNewtonLaw2D(velocity, minSpeed, maxSpeed, acceleration, minAcceleration, maxAcceleration, dt);
	}

	/** Compute and replies a motion according
	 * to high school physics Newton's equations for motion.
	 * <p>
	 * This function allows to clamp acceleration and velocity.
	 * <p>
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
	public static Vector3f motionNewtonLaw2D5(
			Vector3f velocity,
			float minSpeed,
			float maxSpeed,
			Vector3f acceleration, 
			float minAcceleration,
			float maxAcceleration,
			float dt) {
		return engine.motionNewtonLaw2D5(velocity, minSpeed, maxSpeed, acceleration, minAcceleration, maxAcceleration, dt);
	}
	
	/** Compute and replies a motion according
	 * to high school physics Newton's equations for motion.
	 * <p>
	 * This function allows to clamp acceleration and velocity.
	 * <p>
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
	public static Vector3f motionNewtonLaw3D(
			Vector3f velocity,
			float minSpeed,
			float maxSpeed,
			Vector3f acceleration, 
			float minAcceleration,
			float maxAcceleration,
			float dt) {
		return engine.motionNewtonLaw3D(velocity, minSpeed, maxSpeed, acceleration, minAcceleration, maxAcceleration, dt);
	}

	/** Compute and replies a motion according
	 * to Newton-Euler-1 equations for motion.
	 * <p>
	 * <code>movement = speed * dt</code>
	 * 
	 * @param speed is the current  speed of the object.
	 * @param dt is the time
	 * @return a motion
	 */
	public static float motionNewtonEuler1Law(
			float speed,
			float dt) {
		return engine.motionNewtonEuler1Law(speed, dt);
	}
	
	/** Compute and replies a motion according
	 * to Newton-Euler-1 equations for motion.
	 * <p>
	 * <code>movement = clamp(velocity) * dt</code>
	 * 
	 * @param velocity is the current  velocity of the object.
	 * @param minSpeed is the minimal speed allowed.
	 * @param maxSpeed is the maximal speed allowed.
	 * @param dt is the time
	 * @return a motion
	 */
	public static float motionNewtonEuler1Law1D(
			float velocity,
			float minSpeed,
			float maxSpeed,
			float dt) {
		return engine.motionNewtonEuler1Law1D(velocity, minSpeed, maxSpeed, dt);
	}

	/** Compute and replies a motion according
	 * to Newton-Euler-1 equations for motion.
	 * <p>
	 * <code>movement = clamp(velocity) * dt</code>
	 * 
	 * @param velocity is the current  velocity of the object.
	 * @param minSpeed is the minimal speed allowed (clamped to 0 if negative).
	 * @param maxSpeed is the maximal speed allowed.
	 * @param dt is the time
	 * @return a motion
	 */
	public static Vector2f motionNewtonEuler1Law1D5(
			Vector2f velocity,
			float minSpeed,
			float maxSpeed,
			float dt) {
		return engine.motionNewtonEuler1Law1D5(velocity, minSpeed, maxSpeed, dt);
	}

	/** Compute and replies a motion according
	 * to Newton-Euler-1 equations for motion.
	 * <p>
	 * <code>movement = clamp(velocity) * dt</code>
	 * 
	 * @param velocity is the current  velocity of the object.
	 * @param minSpeed is the minimal speed allowed (clamped to 0 if negative).
	 * @param maxSpeed is the maximal speed allowed.
	 * @param dt is the time
	 * @return a motion
	 */
	public static Vector2f motionNewtonEuler1Law2D(
			Vector2f velocity,
			float minSpeed,
			float maxSpeed,
			float dt) {
		return engine.motionNewtonEuler1Law2D(velocity, minSpeed, maxSpeed, dt);
	}

	/** Compute and replies a motion according
	 * to Newton-Euler-1 equations for motion.
	 * <p>
	 * <code>movement = clamp(velocity) * dt</code>
	 * 
	 * @param velocity is the current  velocity of the object.
	 * @param minSpeed is the minimal speed allowed (clamped to 0 if negative).
	 * @param maxSpeed is the maximal speed allowed.
	 * @param dt is the time
	 * @return a motion
	 */
	public static Vector3f motionNewtonEuler1Law2D5(
			Vector3f velocity,
			float minSpeed,
			float maxSpeed,
			float dt) {
		return engine.motionNewtonEuler1Law2D5(velocity, minSpeed, maxSpeed, dt);
	}

	/** Compute and replies a motion according
	 * to Newton-Euler-1 equations for motion.
	 * <p>
	 * <code>movement = clamp(velocity) * dt</code>
	 * 
	 * @param velocity is the current  velocity of the object.
	 * @param minSpeed is the minimal speed allowed (clamped to 0 if negative).
	 * @param maxSpeed is the maximal speed allowed.
	 * @param dt is the time
	 * @return a motion
	 */
	public static Vector3f motionNewtonEuler1Law3D(
			Vector3f velocity,
			float minSpeed,
			float maxSpeed,
			float dt) {
		return engine.motionNewtonEuler1Law3D(velocity, minSpeed, maxSpeed, dt);
	}

	/** Replies the new  velocity according to a previous velocity and
	 * a mouvement during a given time.
	 * <p>
	 * <code>velocity = movement / dt</code>
	 * 
	 * @param movement is the movement distance.
	 * @param dt is the time
	 * @return a new speed
	 * @deprecated see {@link #speed(float, float)}
	 */
	@Deprecated
	public static float velocity(float movement, float dt) {
		return engine.velocity(movement, dt);
	}

	/** Replies the new  velocity according to a previous velocity and
	 * a mouvement during a given time.
	 * <p>
	 * <code>velocity = movement / dt</code>
	 * 
	 * @param movement is the movement distance.
	 * @param dt is the time
	 * @return a new speed
	 * @since 4.1
	 */
	public static float speed(float movement, float dt) {
		return engine.speed(movement, dt);
	}

	/** Replies the new  acceleration according to a previous 
	 * velocity and a current  velocity, and given time.
	 * <p>
	 * <code>(currentVelocity - previousVelocity) / dt</code>
	 * 
	 * @param previousSpeed is the previous  speed of the object.
	 * @param currentSpeed is the current  speed of the object.
	 * @param dt is the time
	 * @return a new acceleration
	 */
	public static float acceleration(
			float previousSpeed,
			float currentSpeed, 
			float dt) {
		return engine.acceleration(previousSpeed, currentSpeed, dt);
	}

}
