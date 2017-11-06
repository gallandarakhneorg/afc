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

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.MathUtil;
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
 * </ul>
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
@SuppressWarnings("checkstyle:magicnumber")
class JavaPhysicsEngine implements PhysicsEngine {

	/** Construct the Java-base physic engine.
	 */
	JavaPhysicsEngine() {
		//
	}

	@Pure
	@Override
	public double motionNewtonLaw(
			double speed,
			double acceleration,
			double dt) {
		return speed * dt + .5 * acceleration * dt * dt;
	}

	@Pure
	@Override
	public double motionNewtonLaw1D(
			double velocity,
			double minSpeed,
			double maxSpeed,
			double acceleration,
			double minAcceleration,
			double maxAcceleration,
			double dt) {
		assert minSpeed >= 0.;
		final double acc = MathUtil.clamp(acceleration, minAcceleration, maxAcceleration);
		int sign = MathUtil.sign(velocity);
		double velocityNorm = Math.abs(velocity) + .5f * acc * dt;
		if (velocityNorm < 0) {
			sign = -sign;
		}
		velocityNorm = MathUtil.clamp(Math.abs(velocityNorm), minSpeed, maxSpeed);
		return sign * velocityNorm * dt;
	}

	@Pure
	@Override
	public void motionNewtonLaw1D5(
			Vector2D<?, ?> velocity,
			double minSpeed,
			double maxSpeed,
			Vector2D<?, ?> acceleration,
			double minAcceleration,
			double maxAcceleration,
			double dt,
			Vector2D<?, ?> result) {
		assert velocity != null;
		assert acceleration != null;
		assert minSpeed >= 0.;

		double olength = acceleration.getLength();
		double vx;
		double vy;
		double a;

		if (olength != 0.) {
			a = MathUtil.clamp(
					(acceleration.dot(velocity) < 0.) ? -olength : olength,
							minAcceleration,
							maxAcceleration);

			a = Math.abs(a) / olength;
			vx = a * acceleration.getX();
			vy = a * acceleration.getY();

			a = .5f * dt;

			vx = velocity.getX() + a * vx;
			vy = velocity.getY() + a * vy;
		} else {
			vx = velocity.getX();
			vy = velocity.getY();
		}

		olength = Math.hypot(vx, vy);
		if (olength != 0.) {
			a = MathUtil.clamp(
					(Vector2D.dotProduct(vx, vy, velocity.getX(), velocity.getY()) < 0.) ? -olength : olength,
							minSpeed,
							maxSpeed);

			a = dt * Math.abs(a) / olength;

			result.set(a * vx, a * vy);
		} else {
			result.set(0., 0.);
		}
	}

	@Pure
	@Override
	public void motionNewtonLaw2D(
			Vector2D<?, ?> velocity,
			double minSpeed,
			double maxSpeed,
			Vector2D<?, ?> acceleration,
			double minAcceleration,
			double maxAcceleration,
			double dt,
			Vector2D<?, ?> result) {
		assert velocity != null;
		assert acceleration != null;
		assert minSpeed >= 0.;

		double olength = acceleration.getLength();
		double vx;
		double vy;
		double a;

		if (olength != 0.) {
			a = MathUtil.clamp(
					(acceleration.dot(velocity) < 0.) ? -olength : olength,
							minAcceleration,
							maxAcceleration);

			a = Math.abs(a) / olength;
			vx = a * acceleration.getX();
			vy = a * acceleration.getY();

			a = .5f * dt;

			vx = velocity.getX() + a * vx;
			vy = velocity.getY() + a * vy;
		} else {
			vx = velocity.getX();
			vy = velocity.getY();
		}

		olength = Math.hypot(vx, vy);
		if (olength != 0.) {
			a = MathUtil.clamp(
					(Vector2D.dotProduct(vx, vy, velocity.getX(), velocity.getY()) < 0.) ? -olength : olength,
							minSpeed,
							maxSpeed);

			a = dt * Math.abs(a) / olength;

			result.set(a * vx, a * vy);
		} else {
			result.set(0., 0.);
		}
	}

	@Pure
	@Override
	public void motionNewtonLaw2D5(
			Vector3D<?, ?> velocity,
			double minSpeed,
			double maxSpeed,
			Vector3D<?, ?> acceleration,
			double minAcceleration,
			double maxAcceleration,
			double dt,
			Vector3D<?, ?> result) {
		motionNewtonLaw3D(
				velocity,
				minSpeed, maxSpeed,
				acceleration,
				minAcceleration, maxAcceleration,
				dt,
				result);
	}

	@Pure
	@Override
	public void motionNewtonLaw3D(
			Vector3D<?, ?> velocity,
			double minSpeed,
			double maxSpeed,
			Vector3D<?, ?> acceleration,
			double minAcceleration,
			double maxAcceleration,
			double dt,
			Vector3D<?, ?> result) {
		assert velocity != null;
		assert acceleration != null;
		assert minSpeed >= 0.;

		double olength = acceleration.getLength();
		double vx;
		double vy;
		double vz;
		double a;

		if (olength != 0.) {
			a = MathUtil.clamp(
					(acceleration.dot(velocity) < 0.) ? -olength : olength,
							minAcceleration,
							maxAcceleration);

			a = Math.abs(a) / olength;
			vx = a * acceleration.getX();
			vy = a * acceleration.getY();
			vz = a * acceleration.getZ();

			a = .5f * dt;

			vx = velocity.getX() + a * vx;
			vy = velocity.getY() + a * vy;
			vz = velocity.getZ() + a * vz;
		} else {
			vx = velocity.getX();
			vy = velocity.getY();
			vz = velocity.getZ();
		}

		olength = Math.sqrt(vx * vx + vy * vy + vz * vz);
		if (olength != 0.) {
			a = MathUtil.clamp(
					(Vector3D.dotProduct(vx, vy, vz,
							velocity.getX(), velocity.getY(), velocity.getZ()) < 0.) ? -olength : olength,
							minSpeed,
							maxSpeed);

			a = dt * Math.abs(a) / olength;

			result.set(a * vx, a * vy, a * vz);
		} else {
			result.set(0., 0., 0.);
		}
	}

	@Pure
	@Override
	public double motionNewtonEuler1Law(
			double speed,
			double dt) {
		return speed * dt;
	}

	@Pure
	@Override
	public double motionNewtonEuler1Law1D(
			double speed,
			double minSpeed,
			double maxSpeed,
			double dt) {
		assert minSpeed >= 0.;
		return MathUtil.sign(speed) * MathUtil.clamp(Math.abs(speed), minSpeed, maxSpeed) * dt;
	}

	@Pure
	@Override
	public void motionNewtonEuler1Law1D5(
			Vector2D<?, ?> velocity,
			double minSpeed,
			double maxSpeed,
			double dt,
			Vector2D<?, ?> result) {
		assert minSpeed >= 0.;
		final double length = velocity.getLength();
		if (length != 0.) {
			final double acc = dt * MathUtil.clamp(length, minSpeed, maxSpeed) / length;
			result.set(velocity.getX() * acc, velocity.getY() * acc);
		} else {
			result.set(0., 0.);
		}
	}

	@Pure
	@Override
	public void motionNewtonEuler1Law2D(
			Vector2D<?, ?> velocity,
			double minSpeed,
			double maxSpeed,
			double dt,
			Vector2D<?, ?> result) {
		assert minSpeed >= 0.;
		final double length = velocity.getLength();
		if (length != 0.) {
			final double a = dt * MathUtil.clamp(length, minSpeed, maxSpeed) / length;
			result.set(velocity.getX() * a, velocity.getY() * a);
		} else {
			result.set(0., 0.);
		}
	}

	@Pure
	@Override
	public void motionNewtonEuler1Law2D5(
			Vector3D<?, ?> velocity,
			double minSpeed,
			double maxSpeed,
			double dt,
			Vector3D<?, ?> result) {
		motionNewtonEuler1Law3D(
				velocity,
				minSpeed, maxSpeed,
				dt,
				result);
	}

	@Pure
	@Override
	public void motionNewtonEuler1Law3D(
			Vector3D<?, ?> velocity,
			double minSpeed,
			double maxSpeed,
			double dt,
			Vector3D<?, ?> result) {
		assert minSpeed >= 0.;
		final double l = velocity.getLength();
		if (l != 0.) {
			final double a = dt * MathUtil.clamp(l, minSpeed, maxSpeed) / l;
			result.set(velocity.getX() * a, velocity.getY() * a, velocity.getZ() * a);
		} else {
			result.set(0., 0., 0.);
		}
	}

	@Pure
	@Override
	public double speed(double movement, double dt) {
		return movement / dt;
	}

	@Pure
	@Override
	public double acceleration(
			double previousSpeed,
			double currentSpeed,
			double dt) {
		return (currentSpeed - previousSpeed) / dt;
	}

}
