/* 
 * $Id$
 * 
 * Copyright (C) 2013 Christophe BOHRHAUER.
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
package org.arakhne.afc.math.geometry.d3.continuous.spline;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.arakhne.afc.math.geometry.d3.Point3D;
import org.arakhne.afc.math.geometry.d3.continuous.Point3f;

/**
 * Catmull-Rom spline.
 * 
 * <img src="./doc-files/catmullromspline.png"/>
 * 
 * The <a href="./doc-files/catmullrom.pdf">Catmull-Rom spline</a> has the parameter alpha:<ul>
 * <li>alpha = 0, the spline is a uniform Catmull-Rom spline;</li>
 * <li>alpha = 0.5 (default), the spline is a centripetal Catmull-Rom spline;</li>
 * <li>alpha = 1, the spline is chordal Catmull-Rom spline.</li>
 * </ul>
 * 
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @see "http://en.wikipedia.org/wiki/Centripetal_Catmull%E2%80%93Rom_spline"
 * @see "http://www.mvps.org/directx/articles/catmull/"
 */
public class CatmullRomSpline extends AbstractSpline<CatmullRomSpline> {

	private static final long serialVersionUID = 6241376226243318800L;

	/** For centripetal Catmull-Rom spline, the value of alpha is 0.5.
	 * When alpha = 0, the resulting curve is the standard Catmull-Rom spline 
	 * (uniform Catmull-Rom spline); when alpha = 1, the product is a chordal Catmull-Rom spline.
	 */
	private double theta = 0.5;

	/**
	 */
	public CatmullRomSpline() {
		//
	}

	/**
	 * @param points the control points of the spline.
	 */
	public CatmullRomSpline(Point3D... points) {
		super(points);
	}

	/**
	 * @param points the control points of the spline.
	 */
	public CatmullRomSpline(List<? extends Point3D> points) {
		super(points);
	}

	/**
	 * Change the control points and recompute the spline's points.
	 *
	 * @param controlPoints the control points.
	 */
	public void setControlPoints(List<? extends Point3D> controlPoints) {
		assert (controlPoints != null);
		this.controlPoints = controlPoints;
	}

	/**
	 * Change the control points and recompute the spline's points.
	 *
	 * @param controlPoints the control points.
	 */
	public void setControlPoints(Point3D... controlPoints) {
		setControlPoints(Arrays.asList(controlPoints));
	}

	/** Replies theta parameter for the Catmull-Rom spline.
	 *
	 * @return the alpha parameter.
	 */
	public double getTheta() {
		return this.theta;
	}

	/** Change theta parameter for the Catmull-Rom spline.
	 *
	 * @param theta is the new value for the alpha parameter.
	 */
	public void setTheta(double theta) {
		this.theta = theta;
	}

	@Override
	public Iterator<Point3f> iterator() {
		return new PointIterator();
	}

	/** Iterator on the points of the Catmull-Rom spline.
	 *
	 * @author $Author: galland$
	 * @version $Name$ $Revision$ $Date$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	protected class PointIterator extends AbstractPointIterator {

		@SuppressWarnings("hiding")
		private final double theta;

		private Point3D p0;
		private Point3D p1;
		private Point3D p2;
		private Point3D p3;

		private final Point3D c0 = new Point3f();
		private final Point3D c1 = new Point3f();
		private final Point3D c2 = new Point3f();
		private final Point3D c3 = new Point3f();

		/**
		 */
		public PointIterator() {
			super();
			this.theta = getTheta();
		}

		@Override
		protected Point3f computeNextPoint(boolean isFirstSegment, Iterator<? extends Point3D> controlPointIterator) {
			if (this.t > 1. || isFirstSegment) {
				if (isFirstSegment) {
					// First part of the spline chain.
					if (controlPointIterator.hasNext()) {
						this.p0 = controlPointIterator.next();
					} else {
						// Not enough control point.
						return null;
					}
					this.p1 = this.p0;
					if (controlPointIterator.hasNext()) {
						this.p2 = controlPointIterator.next();
					} else {
						// Not enough control point.
						return null;
					}
					if (controlPointIterator.hasNext()) {
						this.p3 = controlPointIterator.next();
					} else {
						// The spline is a line!
						this.p3 = this.p2;
					}
				} else {
					this.p0 = this.p1;
					this.p1 = this.p2;
					this.p2 = this.p3;
					if (controlPointIterator.hasNext()) {
						this.p3 = controlPointIterator.next();
					} else if (this.p1 == this.p3) {
						// No more part in the chain.
						return null;
					}
				}

				double t1 = 2. * this.theta;
				double t2 = this.theta - 3.;
				double t3 = 3. - t1;
				double t4 = 2. - this.theta;
				double t5 = this.theta - 2.;
				
				this.c0.set(this.p1);
				this.c1.set(
						this.theta * this.p2.getX() - this.theta * this.p0.getX(),
						this.theta * this.p2.getY() - this.theta * this.p0.getY(),
						this.theta * this.p2.getZ() - this.theta * this.p0.getZ());
				this.c2.set(
						t1 * this.p0.getX() + t2 * this.p1.getX() + t3 * this.p2.getX() - this.theta * this.p3.getX(),
						t1 * this.p0.getY() + t2 * this.p1.getY() + t3 * this.p2.getY() - this.theta * this.p3.getY(),
						t1 * this.p0.getZ() + t2 * this.p1.getZ() + t3 * this.p2.getZ() - this.theta * this.p3.getZ());
				this.c3.set(
						t4 * this.p1.getX() + t5 * this.p2.getX() + this.theta * this.p3.getX() - this.theta * this.p0.getX(),
						t4 * this.p1.getY() + t5 * this.p2.getY() + this.theta * this.p3.getY() - this.theta * this.p0.getY(),
						t4 * this.p1.getZ() + t5 * this.p2.getZ() + this.theta * this.p3.getZ() - this.theta * this.p0.getZ());
				
				this.t = 0.;
				
			}

			Point3f p = new Point3f();
		
			//
			//                        [ c0 ]
			//                        [ c1 ]
			//                        [ c2 ]
			// [ 1  u  u^2  u^3 ]  *  [ c3 ]
			//
			
			double u2 = this.t * this.t;
			double u3 = u2 * this.t;
			
			p.set(
					this.c0.getX() + this.t * this.c1.getX() + u2 * this.c2.getX() + u3 * this.c3.getX(),
					this.c0.getY() + this.t * this.c1.getY() + u2 * this.c2.getY() + u3 * this.c3.getY(),
					this.c0.getZ() + this.t * this.c1.getZ() + u2 * this.c2.getZ() + u3 * this.c3.getZ());

			this.t += this.dt;

			return p;
		}

	}

}
