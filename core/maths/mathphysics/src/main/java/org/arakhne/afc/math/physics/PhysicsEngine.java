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

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.geometry.d1.Vector1D;
import org.arakhne.afc.math.geometry.d2.Vector2D;
import org.arakhne.afc.math.geometry.d3.Vector3D;

/**
 * Some physic utility functions implementing in Java.
 *
 * <p>Definitions:
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
	 *
	 * <p>From Equation 2 of the SUVAT form:<br>
	 * {@code movement = speed * dt + 0.5 * acceleration * dt * dt}
	 *
	 * @param speed is the current speed of the object.
	 * @param acceleration is the current acceleration of the object.
	 * @param dt is the time
	 * @return a motion
	 */
	@Pure
	double motionNewtonLaw(
			double speed,
			double acceleration,
			double dt);

	/** Compute and replies a motion according
	 * to high school physics Newton's equations for motion.
	 *
	 * <p>This function allows to clamp acceleration and velocity.
	 *
	 * <p>From Equation 2 of the SUVAT form:<br>
	 * {@code clamped_acceleration = clamp(acceleration, minAcceleration, maxAcceleration)}<br>
	 * {@code new_velocity = velocity + 0.5 * clamped_acceleration * dt}<br>
	 * {@code clamped_velocity = clamp(new_velocity, minSpeed, maxSpeed)}<br>
	 * {@code motion = clamped_velocity * dt}<br>
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
	double motionNewtonLaw1D(
			double velocity,
			double minSpeed,
			double maxSpeed,
			double acceleration,
			double minAcceleration,
			double maxAcceleration,
			double dt);

	/** Compute and replies a motion according
	 * to high school physics Newton's equations for motion.
	 *
	 * <p>This function allows to clamp acceleration and velocity.
	 *
	 * <p>From Equation 2 of the SUVAT form:<br>
	 * {@code clamped_acceleration = clamp(acceleration, minAcceleration, maxAcceleration)}<br>
	 * {@code new_velocity = velocity + 0.5 * clamped_acceleration * dt}<br>
	 * {@code clamped_velocity = clamp(new_velocity, minSpeed, maxSpeed)}<br>
	 * {@code motion = clamped_velocity * dt}<br>
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
	 * @deprecated since 16.0, see {@link #motionNewtonLaw1D5(Vector1D, double, double, Vector1D, double, double, double, Vector1D)}
	 */
	@Deprecated(since = "16.0", forRemoval = true)
	@Pure
	void motionNewtonLaw1D5(
			Vector2D<?, ?> velocity,
			double minSpeed,
			double maxSpeed,
			Vector2D<?, ?> acceleration,
			double minAcceleration,
			double maxAcceleration,
			double dt,
			Vector2D<?, ?> result);

	/** Compute and replies a motion according
	 * to high school physics Newton's equations for motion.
	 *
	 * <p>This function allows to clamp acceleration and velocity.
	 *
	 * <p>From Equation 2 of the SUVAT form:<br>
	 * {@code clamped_acceleration = clamp(acceleration, minAcceleration, maxAcceleration)}<br>
	 * {@code new_velocity = velocity + 0.5 * clamped_acceleration * dt}<br>
	 * {@code clamped_velocity = clamp(new_velocity, minSpeed, maxSpeed)}<br>
	 * {@code motion = clamped_velocity * dt}<br>
	 *
	 * <p>Caution: The resulting vector has the same segment as the provided vector.
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
	 * @since 16.0
	 */
	@Pure
	void motionNewtonLaw1D5(
			Vector1D<?, ?, ?> velocity,
			double minSpeed,
			double maxSpeed,
			Vector1D<?, ?, ?> acceleration,
			double minAcceleration,
			double maxAcceleration,
			double dt,
			Vector1D<?, ?, ?> result);

	/** Compute and replies a motion according
	 * to high school physics Newton's equations for motion.
	 *
	 * <p>This function allows to clamp acceleration and velocity.
	 *
	 * <p>From Equation 2 of the SUVAT form:<br>
	 * {@code clamped_acceleration = clamp(acceleration, minAcceleration, maxAcceleration)}<br>
	 * {@code new_velocity = velocity + 0.5 * clamped_acceleration * dt}<br>
	 * {@code clamped_velocity = clamp(new_velocity, minSpeed, maxSpeed)}<br>
	 * {@code motion = clamped_velocity * dt}<br>
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
	void motionNewtonLaw2D(
			Vector2D<?, ?> velocity,
			double minSpeed,
			double maxSpeed,
			Vector2D<?, ?> acceleration,
			double minAcceleration,
			double maxAcceleration,
			double dt,
			Vector2D<?, ?> result);

	/** Compute and replies a motion according
	 * to high school physics Newton's equations for motion.
	 *
	 * <p>This function allows to clamp acceleration and velocity.
	 *
	 * <p>From Equation 2 of the SUVAT form:<br>
	 * {@code clamped_acceleration = clamp(acceleration, minAcceleration, maxAcceleration)}<br>
	 * {@code new_velocity = velocity + 0.5 * clamped_acceleration * dt}<br>
	 * {@code clamped_velocity = clamp(new_velocity, minSpeed, maxSpeed)}<br>
	 * {@code motion = clamped_velocity * dt}<br>
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
	void motionNewtonLaw2D5(
			Vector3D<?, ?, ?> velocity,
			double minSpeed,
			double maxSpeed,
			Vector3D<?, ?, ?> acceleration,
			double minAcceleration,
			double maxAcceleration,
			double dt,
			Vector3D<?, ?, ?> result);

	/** Compute and replies a motion according
	 * to high school physics Newton's equations for motion.
	 *
	 * <p>This function allows to clamp acceleration and velocity.
	 *
	 * <p>From Equation 2 of the SUVAT form:<br>
	 * {@code clamped_acceleration = clamp(acceleration, minAcceleration, maxAcceleration)}<br>
	 * {@code new_velocity = velocity + 0.5 * clamped_acceleration * dt}<br>
	 * {@code clamped_velocity = clamp(new_velocity, minSpeed, maxSpeed)}<br>
	 * {@code motion = clamped_velocity * dt}<br>
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
	void motionNewtonLaw3D(
			Vector3D<?, ?, ?> velocity,
			double minSpeed,
			double maxSpeed,
			Vector3D<?, ?, ?> acceleration,
			double minAcceleration,
			double maxAcceleration,
			double dt,
			Vector3D<?, ?, ?> result);

	/** Compute and replies a motion according
	 * to Newton-Euler-1 equations for motion
	 * (where acceleration is not significant).
	 *
	 * <p>From the first-order Newton-Euler method:
	 * {@code movement = speed * dt}
	 *
	 * @param speed is the current  speed of the object.
	 * @param dt is the time
	 * @return a motion
	 */
	@Pure
	double motionNewtonEuler1Law(
			double speed,
			double dt);

	/** Compute and replies a motion according
	 * to Newton-Euler-1 equations for motion
	 * (where acceleration is not significant).
	 *
	 * <p>From the first-order Newton-Euler method:
	 * {@code movement = clamp(velocity) * dt}
	 *
	 * @param velocity is the current  velocity of the object.
	 * @param minSpeed is the minimal speed allowed.
	 * @param maxSpeed is the maximal speed allowed.
	 * @param dt is the time
	 * @return a motion
	 */
	@Pure
	double motionNewtonEuler1Law1D(
			double velocity,
			double minSpeed,
			double maxSpeed,
			double dt);

	/** Compute and replies a motion according
	 * to Newton-Euler-1 equations for motion
	 * (where acceleration is not significant).
	 *
	 * <p>From the first-order Newton-Euler method:
	 * {@code movement = clamp(velocity) * dt}
	 *
	 * @param velocity is the current  velocity of the object.
	 * @param minSpeed is the minimal speed allowed (clamped to 0 if negative).
	 * @param maxSpeed is the maximal speed allowed.
	 * @param dt is the time
	 * @param result a motion
	 * @deprecated since 16.0, see {@link #motionNewtonEuler1Law1D5(Vector1D, double, double, double, Vector1D)}
	 */
	@Pure
	@Deprecated(since = "16.0", forRemoval = true)
	void motionNewtonEuler1Law1D5(
			Vector2D<?, ?> velocity,
			double minSpeed,
			double maxSpeed,
			double dt,
			Vector2D<?, ?> result);

	/** Compute and replies a motion according
	 * to Newton-Euler-1 equations for motion
	 * (where acceleration is not significant).
	 *
	 * <p>From the first-order Newton-Euler method:
	 * {@code movement = clamp(velocity) * dt}
	 *
	 * <p>Caution: The resulting vector has the same segment as the provided vector.
	 *
	 * @param velocity is the current  velocity of the object.
	 * @param minSpeed is the minimal speed allowed (clamped to 0 if negative).
	 * @param maxSpeed is the maximal speed allowed.
	 * @param dt is the time
	 * @param result a motion
	 * @since 16.0
	 */
	@Pure
	void motionNewtonEuler1Law1D5(
			Vector1D<?, ?, ?> velocity,
			double minSpeed,
			double maxSpeed,
			double dt,
			Vector1D<?, ?, ?> result);

	/** Compute and replies a motion according
	 * to Newton-Euler-1 equations for motion
	 * (where acceleration is not significant).
	 *
	 * <p>From the first-order Newton-Euler method:
	 * {@code movement = clamp(velocity) * dt}
	 *
	 * @param velocity is the current  velocity of the object.
	 * @param minSpeed is the minimal speed allowed (clamped to 0 if negative).
	 * @param maxSpeed is the maximal speed allowed.
	 * @param dt is the time
	 * @param result a motion
	 */
	@Pure
	void motionNewtonEuler1Law2D(
			Vector2D<?, ?> velocity,
			double minSpeed,
			double maxSpeed,
			double dt,
			Vector2D<?, ?> result);

	/** Compute and replies a motion according
	 * to Newton-Euler-1 equations for motion
	 * (where acceleration is not significant).
	 *
	 * <p>From the first-order Newton-Euler method:
	 * {@code movement = clamp(velocity) * dt}
	 *
	 * @param velocity is the current  velocity of the object.
	 * @param minSpeed is the minimal speed allowed (clamped to 0 if negative).
	 * @param maxSpeed is the maximal speed allowed.
	 * @param dt is the time
	 * @param result a motion
	 */
	@Pure
	void motionNewtonEuler1Law2D5(
			Vector3D<?, ?, ?> velocity,
			double minSpeed,
			double maxSpeed,
			double dt,
			Vector3D<?, ?, ?> result);

	/** Compute and replies a motion according
	 * to Newton-Euler-1 equations for motion
	 * (where acceleration is not significant).
	 *
	 * <p>From the first-order Newton-Euler method:
	 * {@code movement = clamp(velocity) * dt}
	 *
	 * @param velocity is the current  velocity of the object.
	 * @param minSpeed is the minimal speed allowed (clamped to 0 if negative).
	 * @param maxSpeed is the maximal speed allowed.
	 * @param dt is the time
	 * @param result a motion
	 */
	@Pure
	void motionNewtonEuler1Law3D(
			Vector3D<?, ?, ?> velocity,
			double minSpeed,
			double maxSpeed,
			double dt,
			Vector3D<?, ?, ?> result);

	/** Replies the new speed according to a previous speed and
	 * a mouvement during a given time.
	 *
	 * <p>From the first-order Newton-Euler method:
	 * {@code speed = movement / dt}
	 *
	 * @param movement is the movement distance.
	 * @param dt is the time
	 * @return a new speed
	 * @since 4.1
	 */
	@Pure
	double speed(double movement, double dt);

	/** Replies the new  acceleration according to a previous
	 * speed and a current speed, and given time.
	 *
	 * <p>From the second-order Newton-Euler method:
	 * {@code (currentSpeed - previousSpeed) / dt}
	 *
	 * @param previousSpeed is the previous  speed of the object.
	 * @param currentSpeed is the current  speed of the object.
	 * @param dt is the time
	 * @return a new acceleration
	 */
	@Pure
	double acceleration(
			double previousSpeed,
			double currentSpeed,
			double dt);

}
