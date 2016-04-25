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
import org.arakhne.afc.math.geometry.d2.Vector2D;
import org.arakhne.afc.math.geometry.d3.Vector3D;


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
 * @since 13.0
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
	public void motionNewtonLaw1D5(
			Vector2D velocity,
			double minSpeed,
			double maxSpeed,
			Vector2D acceleration, 
			double minAcceleration,
			double maxAcceleration,
			double dt,
			Vector2D result) {
		assert(velocity!=null);
		assert(acceleration!=null);
		assert(minSpeed>=0.);

		double oLength = acceleration.getLength();
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
					(Vector2D.dotProduct(vx, vy, velocity.getX(), velocity.getY())<0.) ? -oLength : oLength, 
							minSpeed, 
							maxSpeed);
		
			a = dt * Math.abs(a) / oLength;
		
			result.set(a * vx, a * vy);
		} else {
			result.set(0., 0.);
		}
	}

	/** {@inheritDoc}
	 */
	@Override
	public void motionNewtonLaw2D(
			Vector2D velocity,
			double minSpeed,
			double maxSpeed,
			Vector2D acceleration, 
			double minAcceleration,
			double maxAcceleration,
			double dt,
			Vector2D result) {
		assert(velocity!=null);
		assert(acceleration!=null);
		assert(minSpeed>=0.);

		double oLength = acceleration.getLength();
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
					(Vector2D.dotProduct(vx, vy, velocity.getX(), velocity.getY())<0.) ? -oLength : oLength, 
							minSpeed, 
							maxSpeed);
		
			a = dt * Math.abs(a) / oLength;
		
			result.set(a * vx, a * vy);
		} else {
			result.set(0., 0.);
		}
	}

	/** {@inheritDoc}
	 */
	@Override
	public void motionNewtonLaw2D5(
			Vector3D velocity,
			double minSpeed,
			double maxSpeed,
			Vector3D acceleration, 
			double minAcceleration,
			double maxAcceleration,
			double dt,
			Vector3D result) {
		motionNewtonLaw3D(
				velocity, 
				minSpeed, maxSpeed, 
				acceleration, 
				minAcceleration, maxAcceleration, 
				dt,
				result);
	}
	
	/** {@inheritDoc}
	 */
	@Override
	public void motionNewtonLaw3D(
			Vector3D velocity,
			double minSpeed,
			double maxSpeed,
			Vector3D acceleration, 
			double minAcceleration,
			double maxAcceleration,
			double dt,
			Vector3D result) {
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
					(Vector3D.dotProduct(vx, vy, vz, velocity.getX(), velocity.getY(), velocity.getZ())<0.) ? -oLength : oLength, 
							minSpeed, 
							maxSpeed);
		
			a = dt * Math.abs(a) / oLength;
		
			result.set(a * vx, a * vy, a * vz);
		} else {
			result.set(0., 0., 0.);
		}
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
	public void motionNewtonEuler1Law1D5(
			Vector2D velocity,
			double minSpeed,
			double maxSpeed,
			double dt,
			Vector2D result) {
		assert(minSpeed>=0.);
		double l = velocity.getLength();
		if (l!=0.) {
			double a = dt * MathUtil.clamp(l, minSpeed, maxSpeed) / l;
			result.set(velocity.getX() * a, velocity.getY() * a);
		} else {
			result.set(0.,0.);
		}
	}

	/** {@inheritDoc}
	 */
	@Override
	public void motionNewtonEuler1Law2D(
			Vector2D velocity,
			double minSpeed,
			double maxSpeed,
			double dt,
			Vector2D result) {
		assert(minSpeed>=0.);
		double l = velocity.getLength();
		if (l!=0.) {
			double a = dt * MathUtil.clamp(l, minSpeed, maxSpeed) / l;
			result.set(velocity.getX() * a, velocity.getY() * a);
		} else {
			result.set(0.,0.);
		}
	}

	/** {@inheritDoc}
	 */
	@Override
	public void motionNewtonEuler1Law2D5(
			Vector3D velocity,
			double minSpeed,
			double maxSpeed,
			double dt,
			Vector3D result) {
		motionNewtonEuler1Law3D(
				velocity, 
				minSpeed, maxSpeed,
				dt,
				result);
	}

	/** {@inheritDoc}
	 */
	@Override
	public void motionNewtonEuler1Law3D(
			Vector3D velocity,
			double minSpeed,
			double maxSpeed,
			double dt,
			Vector3D result) {
		assert(minSpeed>=0.);
		double l = velocity.length();
		if (l!=0.) {
			double a = dt * MathUtil.clamp(l, minSpeed, maxSpeed) / l;
			result.set(velocity.getX() * a, velocity.getY() * a, velocity.getZ() * a);
		} else {
			result.set(0., 0., 0.);
		}
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
