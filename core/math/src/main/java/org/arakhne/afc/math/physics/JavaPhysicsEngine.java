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

import org.arakhne.afc.math.MathUtil;
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
 * </ul>
 * 
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
class JavaPhysicsEngine implements PhysicsEngine {

	/**
	 * 
	 */
	JavaPhysicsEngine() {
		//
	}
	
	private static int signum(double v) {
		return (byte)((v<-0.) ? -1 : 1);
	}

	/** {@inheritDoc}
	 */
	@Override
	public double motionNewtonLaw(
			double speed,
			double acceleration, 
			double dt) {
		return speed * dt + .5f * acceleration * dt * dt;
	}

	/** {@inheritDoc}
	 */
	@Override
	public double motionNewtonLaw1D(
			double velocity,
			double minSpeed,
			double maxSpeed,
			double acceleration, 
			double minAcceleration,
			double maxAcceleration,
			double dt) {
		assert(minSpeed>=0.);
		double acc = MathUtil.clamp(acceleration, minAcceleration, maxAcceleration);
		int s = signum(velocity);
		double velocityNorm = Math.abs(velocity) + .5f * acc * dt;
		if (velocityNorm<0) s = -s;
		velocityNorm = MathUtil.clamp(Math.abs(velocityNorm), minSpeed, maxSpeed);
		return s * velocityNorm * dt;
	}

	/** {@inheritDoc}
	 */
	@Override
	public Vector2f motionNewtonLaw1D5(
			Vector2f velocity,
			double minSpeed,
			double maxSpeed,
			Vector2f acceleration, 
			double minAcceleration,
			double maxAcceleration,
			double dt) {
		assert(velocity!=null);
		assert(acceleration!=null);
		assert(minSpeed>=0.);

		double oLength = acceleration.length();
		double vx, vy, a;
		
		if (oLength!=0.) {
			a = MathUtil.clamp(
					(acceleration.dot(velocity)<0.) ? -oLength : oLength, 
							minAcceleration, 
							maxAcceleration);
			
			a = Math.abs(a) / oLength;
			vx = a * acceleration.getX();
			vy = a * acceleration.getY();

			a = .5f * dt;
			
			vx = velocity.getX() + a * vx;
			vy = velocity.getY() + a * vy;
		}
		else {
			vx = velocity.getX();
			vy = velocity.getY();
		}
		
		oLength = Math.sqrt(vx*vx+vy*vy);
		if (oLength!=0.) {
			a = MathUtil.clamp(
					(Vector2f.dotProduct(vx, vy, velocity.getX(), velocity.getY())<0.) ? -oLength : oLength, 
							minSpeed, 
							maxSpeed);
		
			a = dt * Math.abs(a) / oLength;
		
			return new Vector2f(a * vx, a * vy);
		}
		
		return new Vector2f(0., 0.);
	}

	/** {@inheritDoc}
	 */
	@Override
	public Vector2f motionNewtonLaw2D(
			Vector2f velocity,
			double minSpeed,
			double maxSpeed,
			Vector2f acceleration, 
			double minAcceleration,
			double maxAcceleration,
			double dt) {
		assert(velocity!=null);
		assert(acceleration!=null);
		assert(minSpeed>=0.);

		double oLength = acceleration.length();
		double vx, vy, a;
		
		if (oLength!=0.) {
			a = MathUtil.clamp(
					(acceleration.dot(velocity)<0.) ? -oLength : oLength, 
							minAcceleration, 
							maxAcceleration);
			
			a = Math.abs(a) / oLength;
			vx = a * acceleration.getX();
			vy = a * acceleration.getY();

			a = .5f * dt;
			
			vx = velocity.getX() + a * vx;
			vy = velocity.getY() + a * vy;
		}
		else {
			vx = velocity.getX();
			vy = velocity.getY();
		}
		
		oLength = Math.sqrt(vx*vx+vy*vy);
		if (oLength!=0.) {
			a = MathUtil.clamp(
					(Vector2f.dotProduct(vx, vy, velocity.getX(), velocity.getY())<0.) ? -oLength : oLength, 
							minSpeed, 
							maxSpeed);
		
			a = dt * Math.abs(a) / oLength;
		
			return new Vector2f(a * vx, a * vy);
		}
		
		return new Vector2f(0., 0.);
	}

	/** {@inheritDoc}
	 */
	@Override
	public Vector3f motionNewtonLaw2D5(
			Vector3f velocity,
			double minSpeed,
			double maxSpeed,
			Vector3f acceleration, 
			double minAcceleration,
			double maxAcceleration,
			double dt) {
		return motionNewtonLaw3D(
				velocity, 
				minSpeed, maxSpeed, 
				acceleration, 
				minAcceleration, maxAcceleration, 
				dt);
	}
	
	/** {@inheritDoc}
	 */
	@Override
	public Vector3f motionNewtonLaw3D(
			Vector3f velocity,
			double minSpeed,
			double maxSpeed,
			Vector3f acceleration, 
			double minAcceleration,
			double maxAcceleration,
			double dt) {
		assert(velocity!=null);
		assert(acceleration!=null);
		assert(minSpeed>=0.);

		double oLength = acceleration.length();
		double vx, vy, vz, a;
		
		if (oLength!=0.) {
			a = MathUtil.clamp(
					(acceleration.dot(velocity)<0.) ? -oLength : oLength, 
							minAcceleration, 
							maxAcceleration);
			
			a = Math.abs(a) / oLength;
			vx = a * acceleration.getX();
			vy = a * acceleration.getY();
			vz = a * acceleration.getZ();

			a = .5f * dt;
			
			vx = velocity.getX() + a * vx;
			vy = velocity.getY() + a * vy;
			vz = velocity.getZ() + a * vz;
		}
		else {
			vx = velocity.getX();
			vy = velocity.getY();
			vz = velocity.getZ();
		}
		
		oLength = Math.sqrt(vx*vx+vy*vy+vz*vz);
		if (oLength!=0.) {
			a = MathUtil.clamp(
					(Vector3f.dotProduct(vx, vy, vz, velocity.getX(), velocity.getY(), velocity.getZ())<0.) ? -oLength : oLength, 
							minSpeed, 
							maxSpeed);
		
			a = dt * Math.abs(a) / oLength;
		
			return new Vector3f(a * vx, a * vy, a * vz);
		}
		
		return new Vector3f(0., 0., 0.);
	}

	/** {@inheritDoc}
	 */
	@Override
	public double motionNewtonEuler1Law(
			double speed,
			double dt) {
		return speed * dt;
	}
	
	/** {@inheritDoc}
	 */
	@Override
	public double motionNewtonEuler1Law1D(
			double speed,
			double minSpeed,
			double maxSpeed,
			double dt) {
		assert(minSpeed>=0.);
		return signum(speed) * MathUtil.clamp(Math.abs(speed), minSpeed, maxSpeed) * dt;
	}

	/** {@inheritDoc}
	 */
	@Override
	public Vector2f motionNewtonEuler1Law1D5(
			Vector2f velocity,
			double minSpeed,
			double maxSpeed,
			double dt) {
		assert(minSpeed>=0.);
		double l = velocity.length();
		if (l!=0.) {
			double a = dt * MathUtil.clamp(l, minSpeed, maxSpeed) / l;
			return new Vector2f(velocity.getX() * a, velocity.getY() * a);
		}
		return new Vector2f(0.,0.);
	}

	/** {@inheritDoc}
	 */
	@Override
	public Vector2f motionNewtonEuler1Law2D(
			Vector2f velocity,
			double minSpeed,
			double maxSpeed,
			double dt) {
		assert(minSpeed>=0.);
		double l = velocity.length();
		if (l!=0.) {
			double a = dt * MathUtil.clamp(l, minSpeed, maxSpeed) / l;
			return new Vector2f(velocity.getX() * a, velocity.getY() * a);
		}
		return new Vector2f(0.,0.);
	}

	/** {@inheritDoc}
	 */
	@Override
	public Vector3f motionNewtonEuler1Law2D5(
			Vector3f velocity,
			double minSpeed,
			double maxSpeed,
			double dt) {
		return motionNewtonEuler1Law3D(
				velocity, 
				minSpeed, maxSpeed,
				dt);
	}

	/** {@inheritDoc}
	 */
	@Override
	public Vector3f motionNewtonEuler1Law3D(
			Vector3f velocity,
			double minSpeed,
			double maxSpeed,
			double dt) {
		assert(minSpeed>=0.);
		double l = velocity.length();
		if (l!=0.) {
			double a = dt * MathUtil.clamp(l, minSpeed, maxSpeed) / l;
			return new Vector3f(velocity.getX() * a, velocity.getY() * a, velocity.getZ() * a);
		}
		return new Vector3f(0., 0., 0.);
	}

	/** {@inheritDoc}
	 * @deprecated
	 */
	@Deprecated
	@Override
	public double velocity(double movement, double dt) {
		return speed(movement, dt);
	}

	/** {@inheritDoc}
	 */

	@Override
	public double speed(double movement, double dt) {
		return movement / dt;
	}

	/** {@inheritDoc}
	 */
	@Override
	public double acceleration(
			double previousSpeed,
			double currentSpeed, 
			double dt) {
		return (currentSpeed - previousSpeed) / dt;
	}

}
