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
 * Bezier Spline.
 * 
 * <img src="./doc-files/bezierspline.png"/>
 * 
 * The bezier curve may be quadratic or cubic: <ul>
 * <li><strong>Quadratic Curve:
 * <math xmlns="http://www.w3.org/1998/Math/MathML">
 * <mrow><mi>B</mi><mrow><mo form="prefix">(</mo><mi>t</mi>
 * <mo form="postfix">)</mo></mrow><mo>=</mo>
 * <msup><mrow><mo form="prefix">(</mo><mn>1</mn><mo>-</mo>
 * <mi>t</mi><mo form="postfix">)</mo></mrow><mn>2</mn>
 * </msup><msub><mi>P</mi><mn>0</mn></msub><mo>+</mo>
 * <mn>2</mn><mi>t</mi><mrow><mo form="prefix">(</mo>
 * <mn>1</mn><mo>-</mo><mi>t</mi><mo form="postfix">)</mo>
 * </mrow><msub><mi>P</mi><mn>1</mn></msub><mo>+</mo>
 * <msup><mi>t</mi><mn>2</mn></msup><msub><mi>P</mi>
 * <mn>2</mn></msub><mo>,</mo><mi>t</mi><mo>&#x02208;</mo><mrow>
 * <mo form="prefix">[</mo><mn>0,1</mn><mo form="postfix">]</mo>
 * </mrow></mrow></math></li>
 * <li>Cubic Curve:
 * <math xmlns="http://www.w3.org/1998/Math/MathML">
 * <mrow><mi>B</mi><mrow><mo form="prefix">(</mo><mi>t</mi>
 * <mo form="postfix">)</mo></mrow><mo>=</mo><msup><mrow><mo form="prefix">(</mo>
 * <mn>1</mn><mo>-</mo><mi>t</mi><mo form="postfix">)</mo>
 * </mrow><mn>3</mn></msup><msub><mi>P</mi>
 * <mn>0</mn></msub><mo>+</mo><mn>3</mn><mi>t</mi><msup><mrow><mo form="prefix">(</mo>
 * <mn>1</mn><mo>-</mo><mi>t</mi><mo form="postfix">)</mo>
 * </mrow><mn>2</mn></msup><msub><mi>P</mi>
 * <mn>1</mn></msub><mo>+</mo><mn>3</mn><msup><mi>t</mi><mn>2</mn>
 * </msup><mrow><mo form="prefix">(</mo><mn>1</mn>
 * <mo>-</mo><mi>t</mi><mo form="postfix">)</mo></mrow><msub>
 * <mi>P</mi><mn>2</mn></msub><mo>+</mo><msup>
 * <mi>t</mi><mn>3</mn></msup><msub><mi>P</mi><mn>3</mn></msub><mo>,</mo><mi>t</mi>
 * <mo>&#x02208;</mo><mrow><mo form="prefix">[</mo>
 * <mn>0,1</mn><mo form="postfix">]</mo></mrow>
 * </mrow></math></li>
 * </ul>
 * 
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class BezierSpline extends AbstractSpline<BezierSpline> {

	private static final long serialVersionUID = -8871053215350578400L;

	private boolean isCubic = false;

	/**
	 */
	public BezierSpline() {
		//
	}

	/**
	 * @param isCubic <code>true</code> if the bezier curve is cubic.	
	 * <code>false</code> if it is quadratic. 
	 */
	public BezierSpline(boolean isCubic) {
		this.isCubic = true;
	}

	/**
	 * @param isCubic <code>true</code> if the bezier curve is cubic.
	 * <code>false</code> if it is quadratic. 
	 * @param points the control points of the spline.
	 */
	public BezierSpline(boolean isCubic, Point3D... points) {
		super(points);
		this.isCubic = isCubic;
	}

	/**
	 * @param points the control points of the spline.
	 */
	public BezierSpline(Point3D... points) {
		super(points);
	}

	/**
	 * @param isCubic <code>true</code> if the bezier curve is cubic.
	 * <code>false</code> if it is quadratic. 
	 * @param points the control points of the spline.
	 */
	public BezierSpline(boolean isCubic, List<? extends Point3D> points) {
		super(points);
		this.isCubic = isCubic;
	}

	/**
	 * @param points the control points of the spline.
	 */
	public BezierSpline(List<? extends Point3D> points) {
		super(points);
	}

	@Override
	public boolean equals(Object obj) {
		if (super.equals(obj)) {
			BezierSpline s = (BezierSpline) obj;
			return isCubic() == s.isCubic();
		}
		return false;
	}

	@Override
	public int hashCode() {
		long bits = super.hashCode();
		bits = 31L * bits + Boolean.valueOf(isCubic()).hashCode();
		return (int) (bits ^ (bits >> 32));
	}

	/** Replies if the bezier spline is cubic.
	 *
	 * @return <code>true</code> if the bezier curve is cubic.
	 * <code>false</code> if it is quadratic. 
	 */
	public boolean isCubic() {
		return this.isCubic;
	}

	/** Replies if the bezier spline is quadratic.
	 *
	 * @return <code>true</code> if the bezier curve is quadratic.
	 * <code>false</code> if it is cubic.
	 */
	public boolean isQuadratic() {
		return !this.isCubic;
	}

	/** Force the bezier spline to be cubic.
	 *
	 * @param isCubic <code>true</code> if the bezier curve is cubic.
	 * <code>false</code> if it is quadratic. 
	 */
	public void setCubic(boolean isCubic) {
		this.isCubic = isCubic;
	}

	/** Force the bezier spline to be quadratic.
	 *
	 * @param isQuadratic <code>true</code> if the bezier curve is quadratic.
	 * <code>false</code> if it is cubic.
	 */
	public void setQuadratic(boolean isQuadratic) {
		this.isCubic = !isQuadratic;
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

	@Override
	public Iterator<Point3f> iterator() {
		if (isCubic()) {
			return new CubicBezierPointIterator();
		}
		return new QuadraticBezierPointIterator();
	}

	/** Iterator on the points of a	cubic Bezier spline.
	 *
	 * @author $Author: galland$
	 * @version $Name$ $Revision$ $Date$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	protected class CubicBezierPointIterator extends AbstractPointIterator {

		private Point3D p0;
		private Point3D p1;
		private Point3D p2;
		private Point3D p3;

		/**
		 */
		public CubicBezierPointIterator() {
			super();
		}

		@Override
		protected Point3f computeNextPoint(boolean isFirstSegment, Iterator<? extends Point3D> controlPointIterator) {
			if (this.t > 1. || isFirstSegment) {
				if (isFirstSegment) {
					// First bezier curve
					if (controlPointIterator.hasNext()) {
						this.p0 = controlPointIterator.next();
					} else {
						// Not enough control point.
						return null;
					}
				} else {
					// A second bezier curve is needed
					this.p0 = this.p3;
				}
				if (controlPointIterator.hasNext()) {
					this.p1 = controlPointIterator.next();
				} else {
					// Not enough control point.
					return null;
				}
				if (controlPointIterator.hasNext()) {
					this.p2 = controlPointIterator.next();
				} else {
					// Not enough control point.
					return null;
				}
				if (controlPointIterator.hasNext()) {
					this.p3 = controlPointIterator.next();
				} else {
					// Not enough control point.
					return null;
				}
				this.t = 0.;
			}

			Point3f p = new Point3f();

			double it = 1. - this.t;
			double f = it * it * it;
			p.add(f * this.p0.getX(), f * this.p0.getY(), f * this.p0.getZ());
			f = 3. * this.t * it * it;
			p.add(f * this.p1.getX(), f * this.p1.getY(), f * this.p1.getZ());
			f = 3. * this.t * this.t * it;
			p.add(f * this.p2.getX(), f * this.p2.getY(), f * this.p2.getZ());
			f = this.t * this.t * this.t;
			p.add(f * this.p3.getX(), f * this.p3.getY(), f * this.p3.getZ());

			this.t += this.dt;

			return p;
		}

	}


	/** Iterator on the points of a quadratic Bezier spline.
	 *
	 * @author $Author: galland$
	 * @version $Name$ $Revision$ $Date$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	protected class QuadraticBezierPointIterator extends AbstractPointIterator {

		private Point3D p0;
		private Point3D p1;
		private Point3D p2;

		/**
		 */
		public QuadraticBezierPointIterator() {
			super();
		}

		@Override
		protected Point3f computeNextPoint(boolean isFirstSegment, Iterator<? extends Point3D> controlPointIterator) {
			if (this.t > 1. || isFirstSegment) {
				if (isFirstSegment) {
					// First bezier curve
					if (controlPointIterator.hasNext()) {
						this.p0 = controlPointIterator.next();
					} else {
						// Not enough control point.
						return null;
					}
				} else {
					// A second bezier curve is needed
					this.p0 = this.p2;
				}
				if (controlPointIterator.hasNext()) {
					this.p1 = controlPointIterator.next();
				} else {
					// Not enough control point.
					return null;
				}
				if (controlPointIterator.hasNext()) {
					this.p2 = controlPointIterator.next();
				} else {
					// Not enough control point.
					return null;
				}
				this.t = 0.;
			}

			Point3f p = new Point3f();

			double it = 1. - this.t;
			double f = it * it;
			p.add(f * this.p0.getX(), f * this.p0.getY(), f * this.p0.getZ());
			f = 2. * this.t * it;
			p.add(f * this.p1.getX(), f * this.p1.getY(), f * this.p1.getZ());
			f = this.t * this.t;
			p.add(f * this.p2.getX(), f * this.p2.getY(), f * this.p2.getZ());

			this.t += this.dt;

			return p;
		}

	}

}
