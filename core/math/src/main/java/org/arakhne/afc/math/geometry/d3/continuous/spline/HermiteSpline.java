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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import org.arakhne.afc.math.geometry.d3.Point3D;
import org.arakhne.afc.math.geometry.d3.Vector3D;
import org.arakhne.afc.math.geometry.d3.continuous.Point3f;
import org.arakhne.afc.math.geometry.d3.continuous.Vector3f;

/**
 * Cubic Hermite spline.
 * 
 * <img src="./doc-files/hermitespline.png"/>
 * 
 * <math xmlns="http://www.w3.org/1998/Math/MathML"><mrow><mi>p</mi>
 * <mrow><mo form="prefix">(</mo><mi>t</mi><mo form="postfix">)</mo>
 * </mrow><mo>=</mo><mrow><mo form="prefix">(</mo><mn>2</mn><msup>
 * <mi>t</mi><mn>3</mn></msup><mo>-</mo><mn>3</mn><msup><mi>t</mi>
 * <mn>2</mn></msup><mo>+</mo><mn>1</mn><mo form="postfix">)</mo>
 * </mrow><msub><mi>p</mi><mn>0</mn></msub><mo>+</mo><mrow>
 * <mo form="prefix">(</mo><msup><mi>t</mi><mn>3</mn></msup>
 * <mo>-</mo><mn>2</mn><msup><mi>t</mi><mn>2</mn></msup><mo>+</mo>
 * <mi>t</mi><mo form="postfix">)</mo></mrow><msub><mi>m</mi>
 * <mn>0</mn></msub><mo>+</mo><mrow><mo form="prefix">(</mo>
 * <mo>-</mo><mn>2</mn><msup><mi>t</mi><mn>3</mn></msup>
 * <mo>+</mo><mn>3</mn><msup><mi>t</mi><mn>2</mn></msup>
 * <mo form="postfix">)</mo></mrow><msub><mi>p</mi><mn>1</mn>
 * </msub><mo>+</mo><mrow><mo form="prefix">(</mo><msup>
 * <mi>t</mi><mn>3</mn></msup><mo>-</mo><msup><mi>t</mi>
 * <mn>2</mn></msup><mo form="postfix">)</mo></mrow>
 * <msub><mi>m</mi><mn>1</mn></msub></mrow></math>
 * <p>
 * The tangents may be computed in different ways.
 * This class provides a function for computing the tangents 
 * for cardinal spline: <br/>
 * <math xmlns="http://www.w3.org/1998/Math/MathML">
 * <mtable class="m-equation-square" displaystyle="true" style="display: block; margin-top: 1.0em; margin-bottom: 2.0em">
 * <mtr><mtd><mspace width="6.0em" /></mtd><mtd columnalign="left">
 * <msub><mi>m</mi><mi>k</mi></msub><mo>=</mo><mrow>
 * <mo rspace="0.3em" lspace="0em" stretchy="true" fence="true" form="prefix">{</mo>
 * <mtable class="m-cases" columnalign="left"><mtr><mtd><mrow>
 * <mo form="prefix">(</mo><mn>1</mn><mo>-</mo><mi>c</mi>
 * <mo form="postfix">)</mo></mrow><mrow><mo form="prefix">(</mo>
 * <msub><mi>p</mi><mrow><mi>k</mi><mo>+</mo><mn>1</mn>
 * </mrow></msub><mo>-</mo><msub><mi>p</mi><mrow>
 * <mi>k</mi><mo>-</mo><mn>1</mn></mrow></msub><mo form="postfix">)</mo>
 * </mrow></mtd><mtd><mtext>if </mtext><mn>1</mn><mo>&#x02264;</mo>
 * <mi>k</mi><mo>&#x0003C;</mo><mi>n</mi><mo>-</mo><mn>1</mn>
 * </mtd></mtr><mtr><mtd><mstyle scriptlevel="0" displaystyle="true">
 * <mrow><mfrac linethickness="1"><mrow><mn>1</mn><mo>-</mo>
 * <mi>c</mi></mrow><mn>2</mn></mfrac></mrow></mstyle>
 * <mrow><mo form="prefix">(</mo><msub><mi>p</mi><mrow>
 * <mi>k</mi><mo>+</mo><mn>1</mn></mrow></msub><mo>-</mo>
 * <mi>p</mi><mi>k</mi><mo form="postfix">)</mo></mrow>
 * </mtd><mtd><mtext>if </mtext><mi>k</mi><mo>=</mo><mn>0</mn>
 * </mtd></mtr><mtr><mtd><mstyle scriptlevel="0" displaystyle="true">
 * <mrow><mfrac linethickness="1"><mrow><mn>1</mn><mo>-</mo>
 * <mi>c</mi></mrow><mn>2</mn></mfrac></mrow></mstyle>
 * <mrow><mo form="prefix">(</mo><msub><mi>p</mi><mi>k</mi>
 * </msub><mo>-</mo><mi>p</mi><mrow><mi>k</mi><mo>-</mo>
 * <mn>1</mn></mrow><mo form="postfix">)</mo></mrow></mtd>
 * <mtd><mtext>if </mtext><mi>k</mi><mo>=</mo><mi>n</mi>
 * <mo>-</mo><mn>1</mn></mtd></mtr></mtable>
 * <mphantom rspace="0em" lspace="0.3em" stretchy="true" fence="true" form="postfix">}</mphantom>
 * </mrow></mtd></mtr></mtable></math>
 * <p>
 * The parameter c is a tension parameter that must be in the interval (0,1).
 * It can be interpreted as the "length" of the tangent.
 * c=1 will yield all zero tangents, and c=0 yields a {@link CatmullRomSpline Catmull–Rom spline}.
 * 
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @see "http://en.wikipedia.org/wiki/Cubic_Hermite_spline"
 */
public class HermiteSpline extends AbstractSpline<HermiteSpline> {

	private static final long serialVersionUID = 6241376226243318800L;

	/** Compute the tangents at the given points for a cardinal spline.
	 * The tension parapemter must be in the interval (0,1).
	 * It can be interpreted as the "length" of the tangent.
	 * tension=1 will yield all zero tangents, and tension=0 yields a
	 * {@link CatmullRomSpline Catmull–Rom spline}.
	 *
	 * @param points the control points of the spline.
	 * @param tension the tension of the spline.
	 * @return the tangents.
	 */
	public static List<Vector3f> computeTangentsForCardinalSpline(List<? extends Point3D> points, double tension) {
		assert (tension >= 0 && tension <= 1.);
		
		List<Vector3f> tangents = new ArrayList<>(points.size());
		Iterator<? extends Point3D> iterator = points.iterator();
		Point3D p0, p1, p2;
		Vector3f v;
		
		// First tangent
		if (!iterator.hasNext()) {
			return tangents;
		}
		p0 = iterator.next();
		if (!iterator.hasNext()) {
			return tangents;
		}
		p1 = iterator.next();
		v = new Vector3f();
		v.sub(p1, p0);
		v.scale((1. - tension) * 0.5);
		tangents.add(v);
		
		// Internal points' tangents
		p2 = iterator.next();
		if (!iterator.hasNext()) {
			return tangents;
		}
		do {
			v = new Vector3f();
			v.sub(p2, p0);
			v.scale(1. - tension);
			tangents.add(v);
			
			p0 = p1;
			p1 = p2;
			p2 = (iterator.hasNext()) ? iterator.next() : null;
		} while (p2 != null);
		
		// Tangent of the last point
		v = new Vector3f();
		v.sub(p1, p0);
		v.scale((1. - tension) * 0.5);
		tangents.add(v);
		
		return tangents;
	}

	/** Tangents at the control points.
	 */
	protected List<? extends Vector3D> tangents;

	/**
	 */
	public HermiteSpline() {
		this.tangents = Collections.emptyList();
	}

	/**
	 * @param points the control points of the spline.
	 * @param tangents the tangents of the spline.
	 */
	public HermiteSpline(List<? extends Point3D> points, List<? extends Vector3D> tangents) {
		super(points);
		assert (tangents != null);
		assert (points.size() == tangents.size());
		this.tangents = tangents;
	}

	/**
	 * Change the control points and the tangents, and recompute the spline's points.
	 *
	 * @param controlPoints the control points.
	 * @param tangents the tangents of the spline.
	 */
	public void setControlPoints(List<? extends Point3D> controlPoints, List<? extends Vector3D> tangents) {
		assert (controlPoints != null);
		assert (tangents != null);
		assert (controlPoints.size() == tangents.size());
		this.controlPoints = controlPoints;
		this.tangents = tangents;
	}
	
	/** Replies the tangents at the control points.
	 *
	 * @return the tangents.
	 */
	public List<? extends Vector3D> getControlPointTangents() {
		return this.tangents;
	}

	@Override
	public Iterator<Point3f> iterator() {
		return new PointIterator();
	}

	/** Iterator on the points of an Hermite spline.
	 *
	 * @author $Author: galland$
	 * @version $Name$ $Revision$ $Date$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	protected class PointIterator extends AbstractPointIterator {

		private final Iterator<? extends Vector3D> tangentIterator;

		private final Vector3f v0 = new Vector3f();
		private final Vector3f v1 = new Vector3f();
		private final Vector3f v2 = new Vector3f();
		private final Vector3f v3 = new Vector3f();

		private Point3D p0;
		private Vector3D m0;
		private Point3D p1;
		private Vector3D m1;
		
		/**
		 */
		public PointIterator() {
			super();
			this.tangentIterator = HermiteSpline.this.tangents.iterator();
		}

		@Override
		protected Point3f computeNextPoint(boolean isFirstSegment, Iterator<? extends Point3D> controlPointIterator) {
			if (this.t >= 1. || isFirstSegment) {
				// Start new segment
//				Point3D p0;
//				Vector3D m0;
				if (isFirstSegment) {
					// First segment
					if (controlPointIterator.hasNext()) {
						p0 = controlPointIterator.next();
					} else {
						// Not enough control point.
						return null;
					}
					if (this.tangentIterator.hasNext()) {
						m0 = this.tangentIterator.next();
					} else {
						// Not enough control point.
						return null;
					}
				} else {
					p0 = this.p1;
					m0 = this.m1;
				}
				if (controlPointIterator.hasNext()) {
					this.p1 = controlPointIterator.next();
				} else {
					// Not enough control point.
					return null;
				}
				if (this.tangentIterator.hasNext()) {
					this.m1 = this.tangentIterator.next();
				} else {
					// Not enough control point.
					return null;
				}
				
				this.t = 0.;
				
				// Compute the multiplication of the two right-most matrices
				// since these matrices will not change in the current spline segment.

				// P(t) = (2 t^3 - 3 t^2 + 1) p0 + (t^3 - 2 t^2 + t) m0 + (-2 t^3 + 3 t^2) p1 + (t^3 - t^2) m1

				// P(t) =   (2 p0 t^3)  + (-3 p0 t^2)          + (p0)
				//        + (m0 t^3)    + (-2 m0 t^2) + (m0 t)
				//        + (-2 p1 t^3) + (3 p1 t^2)
				//        + (m1 t^3)    + (-m1 t^2)

				// P(t) = [ (2 p0)    (-3 p0)    (0)    (p0) ]  X  [ t^3 ]
				//        [ (m0)      (-2 m0)    (m0)   (0)  ]     [ t^2 ]
				//        [ (-2 p1)   (3 p1)     (0)    (0)  ]     [  t  ]
				//        [ (m1)      (-m1)      (0)    (0)  ]     [  1  ]
				
				// P(t) = [ c0a    c0b     -   c0c ]  X  [ t^3 ]
				//        [ c1a    c1b    c1c   -  ]     [ t^2 ]
				//        [ c2a    c2b     -    -  ]     [  t  ]
				//        [ c3a    c3b     -    -  ]     [  1  ]
				
				// P(t) =    c0a t^3  +  c0b t^2           +  c0c
				//        +  c1a t^3  +  c1b t^2  + c1c t
				//        +  c2a t^3  +  c2b t^2
				//        +  c3a t^3  +  c3b t^2
				
				// P(t) = (c0a + c1a + c2a + c3a) t^3 + (c0b + c1b + c2b + c3b) t^2 + c1c t + c0c
				
				// P(t) = v0 t^3 + v1 t^2 + v2 t + v3

				// v0 = 2 p0 + m0 - 2 p1 + m1
				// v1 = -3 p0 - 2 m0 + 3 p1 - m1
				// v2 = m0
				// v3 = p0

				this.v0.set(
						2. * p0.getX() + m0.getX() - 2. * this.p1.getX() + this.m1.getX(),
						2. * p0.getY() + m0.getY() - 2. * this.p1.getY() + this.m1.getY(),
						2. * p0.getZ() + m0.getZ() - 2. * this.p1.getZ() + this.m1.getZ());
				this.v1.set(
						-3. * p0.getX() - 2. * m0.getX() + 3. * this.p1.getX() - this.m1.getX(),
						-3. * p0.getY() - 2. * m0.getY() + 3. * this.p1.getY() - this.m1.getY(),
						-3. * p0.getZ() - 2. * m0.getZ() + 3. * this.p1.getZ() - this.m1.getZ());
				this.v2.set(m0);
				this.v3.set(p0);
			}

			Point3f p = new Point3f();

			// P(t) = v0 t^3 + v1 t^2 + v2 t + v3
//			double t2 = this.t * this.t;
//			double t3 = t2 * this.t;
//			p.set(
//					this.v0.getX() * t3 + this.v1.getX() * t2 + this.v2.getX() * this.t + this.v3.getX(),
//					this.v0.getY() * t3 + this.v1.getY() * t2 + this.v2.getY() * this.t + this.v3.getY(),
//					this.v0.getZ() * t3 + this.v1.getZ() * t2 + this.v2.getZ() * this.t + this.v3.getZ());

			
			//(2*t**3 - 3*t**2 + 1) * p0 + (t**3 - 2*t**2 + t) * m0 + (-2*t**3 + 3*t**2) * p1 + (t**3 - t**2) * m1;
			double t2 = this.t * this.t;
			double t3 = t2 * this.t;
			p.set(
				(2*t3-3*t2+1)*p0.getX() + (t3-2*t2+t)*m0.getX() + (-2*t3+3*t2)*p1.getX() + (t3-t2)*m1.getX(),
				(2*t3-3*t2+1)*p0.getY() + (t3-2*t2+t)*m0.getY() + (-2*t3+3*t2)*p1.getY() + (t3-t2)*m1.getY(),
				(2*t3-3*t2+1)*p0.getZ() + (t3-2*t2+t)*m0.getZ() + (-2*t3+3*t2)*p1.getZ() + (t3-t2)*m1.getZ());
			
			this.t += this.dt;

			return p;
		}

	}

}
