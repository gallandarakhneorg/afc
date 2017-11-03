/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2018 The original authors, and other authors.
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

import org.eclipse.xtext.xbase.lib.Inline;
import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.geometry.d2.Vector2D;
import org.arakhne.afc.math.geometry.d3.Vector3D;

/**
 * Some physic utility functions.
 *
 * <p>Definitions:
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
 * @since 13.0
 */
public final class PhysicsUtil {

	private static PhysicsEngine engine = new JavaPhysicsEngine();

	private PhysicsUtil() {
		//
	}

	/** Replies the current physics engine.
	 *
	 * @return the current physics engine.
	 */
	@Pure
	public static PhysicsEngine getPhysicsEngine() {
		return engine;
	}

	/** Set the current physics engine.
	 *
	 * <p>If the given engine is <code>null</code>, the default physics engine
	 * is used (java implementation).
	 *
	 * @param newEngine the current physics engine, or <code>null</code> for default engine.
	 * @return previous physics engine.
	 */
	public static PhysicsEngine setPhysicsEngine(PhysicsEngine newEngine) {
		final PhysicsEngine oldEngine = engine;
		if (newEngine == null) {
			engine = new JavaPhysicsEngine();
		} else {
			engine = newEngine;
		}
		return oldEngine;
	}

	/** Compute and replies a motion according
	 * to high school physics Newton's equations for motion.
	 *
	 * <p><code>movement = speed * dt + 0.5 * acceleration * dt * dt</code>
	 *
	 * @param speed is the current speed of the object.
	 * @param acceleration is the current acceleration of the object.
	 * @param dt is the time
	 * @return a motion
	 */
	@Pure
	@Inline(value = "PhysicsUtil.getPhysicsEngine().motionNewtonLaw(($1), ($2), ($3))",
			imported = {PhysicsUtil.class})
	public static double motionNewtonLaw(
			double speed,
			double acceleration,
			double dt) {
		return engine.motionNewtonLaw(speed, acceleration, dt);
	}

	/** Compute and replies a motion according
	 * to high school physics Newton's equations for motion.
	 *
	 * <p>This function allows to clamp acceleration and velocity.
	 *
	 * <p><code>clamped_acceleration = clamp(acceleration, minAcceleration, maxAcceleration)</code><br>
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
	 *     Length of this vector is the acceleration amount. Direction of this
	 *     vector becomes movement direction.
	 * @param dt is the time.
	 * @return the motion
	 * @see "http://en.wikibooks.org/wiki/High_School_Physics/Velocity"
	 */
	@Pure
	@Inline(value = "PhysicsUtil.getPhysicsEngine().motionNewtonLaw1D(($1), ($2), ($3), ($4), ($5), ($6), ($7))",
			imported = {PhysicsUtil.class})
	public static double motionNewtonLaw1D(
			double velocity,
			double minSpeed,
			double maxSpeed,
			double acceleration,
			double minAcceleration,
			double maxAcceleration,
			double dt) {
		return engine.motionNewtonLaw1D(velocity, minSpeed, maxSpeed, acceleration, minAcceleration, maxAcceleration, dt);
	}

	/** Compute and replies a motion according
	 * to high school physics Newton's equations for motion.
	 *
	 * <p>This function allows to clamp acceleration and velocity.
	 *
	 * <p><code>clamped_acceleration = clamp(acceleration, minAcceleration, maxAcceleration)</code><br>
	 * <code>new_velocity = velocity + 0.5 * clamped_acceleration * dt</code><br>
	 * <code>clamped_velocity = clamp(new_velocity, minSpeed, maxSpeed)</code><br>
	 * <code>motion = clamped_velocity * dt</code><br>
	 *
	 * @param velocity is the current velocity of the object. Norm of vector is speed in m/s for example.
	 * @param minSpeed is the minimal speed allowed.
	 * @param maxSpeed is the maximal speed allowed.
	 * @param acceleration is the current acceleration of the object. Norm of vector is acceleration
	 *     amount in m/s<sup>2</sup> for example.
	 * @param minAcceleration is the minimal acceleration allowed.
	 * @param maxAcceleration is the maximal  acceleration allowed.
	 *     Length of this vector is the acceleration amount. Direction of this
	 *     vector becomes movement direction.
	 * @param dt is the time.
	 * @param result the motion.
	 * @see "http://en.wikibooks.org/wiki/High_School_Physics/Velocity"
	 */
	@Pure
	@Inline(value = "PhysicsUtil.getPhysicsEngine().motionNewtonLaw1D5(($1), ($2), ($3), ($4), ($5), ($6), ($7), ($8))",
			imported = {PhysicsUtil.class})
	public static void motionNewtonLaw1D5(
			Vector2D<?, ?> velocity,
			double minSpeed,
			double maxSpeed,
			Vector2D<?, ?> acceleration,
			double minAcceleration,
			double maxAcceleration,
			double dt,
			Vector2D<?, ?> result) {
		engine.motionNewtonLaw1D5(velocity, minSpeed, maxSpeed, acceleration, minAcceleration, maxAcceleration, dt, result);
	}

	/** Compute and replies a motion according
	 * to high school physics Newton's equations for motion.
	 *
	 * <p>This function allows to clamp acceleration and velocity.
	 *
	 * <p><code>clamped_acceleration = clamp(acceleration, minAcceleration, maxAcceleration)</code><br>
	 * <code>new_velocity = velocity + 0.5 * clamped_acceleration * dt</code><br>
	 * <code>clamped_velocity = clamp(new_velocity, minSpeed, maxSpeed)</code><br>
	 * <code>motion = clamped_velocity * dt</code><br>
	 *
	 * @param velocity is the current velocity of the object. Norm of vector is speed in m/s for example.
	 * @param minSpeed is the minimal speed allowed.
	 * @param maxSpeed is the maximal speed allowed.
	 * @param acceleration is the current acceleration of the object. Norm of vector is acceleration
	 *     amount in m/s<sup>2</sup> for example.
	 * @param minAcceleration is the minimal acceleration allowed.
	 * @param maxAcceleration is the maximal  acceleration allowed.
	 *     Length of this vector is the acceleration amount. Direction of this
	 *     vector becomes movement direction.
	 * @param dt is the time.
	 * @param result the motion.
	 * @see "http://en.wikibooks.org/wiki/High_School_Physics/Velocity"
	 */
	@Pure
	@Inline(value = "PhysicsUtil.getPhysicsEngine().motionNewtonLaw2D(($1), ($2), ($3), ($4), ($5), ($6), ($7), ($8))",
			imported = {PhysicsUtil.class})
	public static void motionNewtonLaw2D(
			Vector2D<?, ?> velocity,
			double minSpeed,
			double maxSpeed,
			Vector2D<?, ?> acceleration,
			double minAcceleration,
			double maxAcceleration,
			double dt,
			Vector2D<?, ?> result) {
		engine.motionNewtonLaw2D(velocity, minSpeed, maxSpeed, acceleration, minAcceleration, maxAcceleration, dt, result);
	}

	/** Compute and replies a motion according
	 * to high school physics Newton's equations for motion.
	 *
	 * <p>This function allows to clamp acceleration and velocity.
	 *
	 * <p><code>clamped_acceleration = clamp(acceleration, minAcceleration, maxAcceleration)</code><br>
	 * <code>new_velocity = velocity + 0.5 * clamped_acceleration * dt</code><br>
	 * <code>clamped_velocity = clamp(new_velocity, minSpeed, maxSpeed)</code><br>
	 * <code>motion = clamped_velocity * dt</code><br>
	 *
	 * @param velocity is the current velocity of the object. Norm of vector is speed in m/s for example.
	 * @param minSpeed is the minimal speed allowed.
	 * @param maxSpeed is the maximal speed allowed.
	 * @param acceleration is the current acceleration of the object. Norm of vector is acceleration
	 *     amount in m/s<sup>2</sup> for example.
	 * @param minAcceleration is the minimal acceleration allowed.
	 * @param maxAcceleration is the maximal  acceleration allowed.
	 *     Length of this vector is the acceleration amount. Direction of this
	 *     vector becomes movement direction.
	 * @param dt is the time.
	 * @param result the motion.
	 * @see "http://en.wikibooks.org/wiki/High_School_Physics/Velocity"
	 */
	@Pure
	@Inline(value = "PhysicsUtil.getPhysicsEngine().motionNewtonLaw2D5(($1), ($2), ($3), ($4), ($5), ($6), ($7), ($8))",
			imported = {PhysicsUtil.class})
	public static void motionNewtonLaw2D5(
			Vector3D<?, ?> velocity,
			double minSpeed,
			double maxSpeed,
			Vector3D<?, ?> acceleration,
			double minAcceleration,
			double maxAcceleration,
			double dt,
			Vector3D<?, ?> result) {
		engine.motionNewtonLaw2D5(velocity, minSpeed, maxSpeed, acceleration, minAcceleration, maxAcceleration, dt, result);
	}

	/** Compute and replies a motion according
	 * to high school physics Newton's equations for motion.
	 *
	 * <p>This function allows to clamp acceleration and velocity.
	 *
	 * <p><code>clamped_acceleration = clamp(acceleration, minAcceleration, maxAcceleration)</code><br>
	 * <code>new_velocity = velocity + 0.5 * clamped_acceleration * dt</code><br>
	 * <code>clamped_velocity = clamp(new_velocity, minSpeed, maxSpeed)</code><br>
	 * <code>motion = clamped_velocity * dt</code><br>
	 *
	 * @param velocity is the current velocity of the object. Norm of vector is speed in m/s for example.
	 * @param minSpeed is the minimal speed allowed.
	 * @param maxSpeed is the maximal speed allowed.
	 * @param acceleration is the current acceleration of the object. Norm of vector is acceleration
	 *     amount in m/s<sup>2</sup> for example.
	 * @param minAcceleration is the minimal acceleration allowed.
	 * @param maxAcceleration is the maximal  acceleration allowed.
	 *     Length of this vector is the acceleration amount. Direction of this
	 *     vector becomes movement direction.
	 * @param dt is the time.
	 * @param result the motion.
	 * @see "http://en.wikibooks.org/wiki/High_School_Physics/Velocity"
	 */
	@Pure
	@Inline(value = "PhysicsUtil.getPhysicsEngine().motionNewtonLaw3D(($1), ($2), ($3), ($4), ($5), ($6), ($7), ($8))",
			imported = {PhysicsUtil.class})
	public static void motionNewtonLaw3D(
			Vector3D<?, ?> velocity,
			double minSpeed,
			double maxSpeed,
			Vector3D<?, ?> acceleration,
			double minAcceleration,
			double maxAcceleration,
			double dt,
			Vector3D<?, ?> result) {
		engine.motionNewtonLaw3D(velocity, minSpeed, maxSpeed, acceleration, minAcceleration, maxAcceleration, dt, result);
	}

	/** Compute and replies a motion according
	 * to Newton-Euler-1 equations for motion.
	 *
	 * <p><code>movement = speed * dt</code>
	 *
	 * @param speed is the current  speed of the object.
	 * @param dt is the time
	 * @return a motion
	 */
	@Pure
	@Inline(value = "PhysicsUtil.getPhysicsEngine().motionNewtonEuler1Law(($1), ($2))",
			imported = {PhysicsUtil.class})
	public static double motionNewtonEuler1Law(
			double speed,
			double dt) {
		return engine.motionNewtonEuler1Law(speed, dt);
	}

	/** Compute and replies a motion according
	 * to Newton-Euler-1 equations for motion.
	 *
	 * <p><code>movement = clamp(velocity) * dt</code>
	 *
	 * @param velocity is the current  velocity of the object.
	 * @param minSpeed is the minimal speed allowed.
	 * @param maxSpeed is the maximal speed allowed.
	 * @param dt is the time
	 * @return a motion
	 */
	@Pure
	@Inline(value = "PhysicsUtil.getPhysicsEngine().motionNewtonEuler1Law1D(($1), ($2), ($3), ($4))",
			imported = {PhysicsUtil.class})
	public static double motionNewtonEuler1Law1D(
			double velocity,
			double minSpeed,
			double maxSpeed,
			double dt) {
		return engine.motionNewtonEuler1Law1D(velocity, minSpeed, maxSpeed, dt);
	}

	/** Compute and replies a motion according
	 * to Newton-Euler-1 equations for motion.
	 *
	 * <p><code>movement = clamp(velocity) * dt</code>
	 *
	 * @param velocity is the current  velocity of the object.
	 * @param minSpeed is the minimal speed allowed (clamped to 0 if negative).
	 * @param maxSpeed is the maximal speed allowed.
	 * @param dt is the time
	 * @param result a motion
	 */
	@Pure
	@Inline(value = "PhysicsUtil.getPhysicsEngine().motionNewtonEuler1Law1D5(($1), ($2), ($3), ($4), ($5))",
			imported = {PhysicsUtil.class})
	public static void motionNewtonEuler1Law1D5(
			Vector2D<?, ?> velocity,
			double minSpeed,
			double maxSpeed,
			double dt,
			Vector2D<?, ?> result) {
		engine.motionNewtonEuler1Law1D5(velocity, minSpeed, maxSpeed, dt, result);
	}

	/** Compute and replies a motion according
	 * to Newton-Euler-1 equations for motion.
	 *
	 * <p><code>movement = clamp(velocity) * dt</code>
	 *
	 * @param velocity is the current  velocity of the object.
	 * @param minSpeed is the minimal speed allowed (clamped to 0 if negative).
	 * @param maxSpeed is the maximal speed allowed.
	 * @param dt is the time
	 * @param result a motion
	 */
	@Pure
	@Inline(value = "PhysicsUtil.getPhysicsEngine().motionNewtonEuler1Law2D(($1), ($2), ($3), ($4), ($5))",
			imported = {PhysicsUtil.class})
	public static void motionNewtonEuler1Law2D(
			Vector2D<?, ?> velocity,
			double minSpeed,
			double maxSpeed,
			double dt,
			Vector2D<?, ?> result) {
		engine.motionNewtonEuler1Law2D(velocity, minSpeed, maxSpeed, dt, result);
	}

	/** Compute and replies a motion according
	 * to Newton-Euler-1 equations for motion.
	 *
	 * <p><code>movement = clamp(velocity) * dt</code>
	 *
	 * @param velocity is the current  velocity of the object.
	 * @param minSpeed is the minimal speed allowed (clamped to 0 if negative).
	 * @param maxSpeed is the maximal speed allowed.
	 * @param dt is the time
	 * @param result a motion
	 */
	@Pure
	@Inline(value = "PhysicsUtil.getPhysicsEngine().motionNewtonEuler1Law2D5(($1), ($2), ($3), ($4), ($5))",
			imported = {PhysicsUtil.class})
	public static void motionNewtonEuler1Law2D5(
			Vector3D<?, ?> velocity,
			double minSpeed,
			double maxSpeed,
			double dt,
			Vector3D<?, ?> result) {
		engine.motionNewtonEuler1Law2D5(velocity, minSpeed, maxSpeed, dt, result);
	}

	/** Compute and replies a motion according
	 * to Newton-Euler-1 equations for motion.
	 *
	 * <p><code>movement = clamp(velocity) * dt</code>
	 *
	 * @param velocity is the current  velocity of the object.
	 * @param minSpeed is the minimal speed allowed (clamped to 0 if negative).
	 * @param maxSpeed is the maximal speed allowed.
	 * @param dt is the time
	 * @param result a motion
	 */
	@Pure
	@Inline(value = "PhysicsUtil.getPhysicsEngine().motionNewtonEuler1Law3D(($1), ($2), ($3), ($4), ($5))",
			imported = {PhysicsUtil.class})
	public static void motionNewtonEuler1Law3D(
			Vector3D<?, ?> velocity,
			double minSpeed,
			double maxSpeed,
			double dt,
			Vector3D<?, ?> result) {
		engine.motionNewtonEuler1Law3D(velocity, minSpeed, maxSpeed, dt, result);
	}

	/** Replies the new  velocity according to a previous velocity and
	 * a mouvement during a given time.
	 *
	 * <p><code>velocity = movement / dt</code>
	 *
	 * @param movement is the movement distance.
	 * @param dt is the time
	 * @return a new speed
	 */
	@Pure
	@Inline(value = "PhysicsUtil.getPhysicsEngine().speed(($1), ($2))",
			imported = {PhysicsUtil.class})
	public static double speed(double movement, double dt) {
		return engine.speed(movement, dt);
	}

	/** Replies the new  acceleration according to a previous
	 * velocity and a current  velocity, and given time.
	 *
	 * <p><code>(currentVelocity - previousVelocity) / dt</code>
	 *
	 * @param previousSpeed is the previous  speed of the object.
	 * @param currentSpeed is the current  speed of the object.
	 * @param dt is the time
	 * @return a new acceleration
	 */
	@Pure
	@Inline(value = "PhysicsUtil.getPhysicsEngine().acceleration(($1), ($2), ($3))",
			imported = {PhysicsUtil.class})
	public static double acceleration(
			double previousSpeed,
			double currentSpeed,
			double dt) {
		return engine.acceleration(previousSpeed, currentSpeed, dt);
	}

}
