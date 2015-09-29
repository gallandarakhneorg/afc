/* 
 * $Id$
 * 
 * Copyright (C) 2010-2013 Christophe BOHRHAUER.
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
package org.arakhne.afc.math.geometry.d3.continuous;

import java.lang.ref.WeakReference;

import org.arakhne.afc.math.MathUtil;
import org.arakhne.afc.math.geometry.d3.FunctionalVector3D;
import org.arakhne.afc.math.geometry.d3.Point3D;
import org.arakhne.afc.math.geometry.d3.Tuple3D;
import org.arakhne.afc.math.geometry.d3.Vector3D;

/** This class represents a 3D plane.
 * <p>
 * <math xmlns="http://www.w3.org/1998/Math/MathML"><mi>P</mi></math>
 * be the point we wish to lie in the plane, and let
 * <math><mover><mi>n</mi><mo>&#x20D7;</mo></mover></math>
 * be a nonzero normal vector to the plane.
 * The desired plane is the set of all points
 * <math><mi mathvariant="bold">r</mi></math> such that
 * <math>
 *   <mover>
 *     <mi>n</mi>
 *     <mo>&#x20D7;</mo>
 *   </mover>
 *   <mo>&#x22C5;</mo>
 *   <mfenced separators="">
 *     <mi>r</mi>
 *     <mo>-</mo>
 *     <mi>p</mi>
 *   </mfenced>
 *   <mo>=</mo>
 *   <mn>0</mn>
 * </math>.
 * <p>
 * If we write
 * <math>
 *   <mrow>
 *     <mover>
 *       <mi>n</mi><mo>&#x20D7;</mo>
 *     </mover>
 *     <mo>=</mo>
 *     <mfenced close="]" open="[">
 *       <mtable>
 *         <mtr><mtd><mi>a</mi></mtd></mtr>
 *         <mtr><mtd><mi>b</mi></mtd></mtr>
 *         <mtr><mtd><mi>c</mi></mtd></mtr>
 *       </mtable>
 *     </mfenced>
 *   </mrow>
 * </math>,<br>
 * <math>
 *   <mrow>
 *     <mi mathvariant="bold">r</mi>
 *     <mo>=</mo>
 *     <mfenced>
 *       <mi>x</mi><mi>y</mi><mi>z</mi>
 *     </mfenced>
 *   </mrow>
 * </math>, and
 * <math><mi>d</mi></math> as the dot product
 * <math>
 *   <mrow>
 *     <mover>
 *       <mi>n</mi><mo>&#x20D7;</mo>
 *     </mover>
 *     <mo>&#x22C5;</mo>
 *     <mi mathvariant="bold">p</mi>
 *     <mo>=</mo><mo>-</mo><mi>d</mi>
 *   </mrow>
 * </math>,<br>
 * then the plane <math><mi>&#x03A0;</mi></math> is determined by the condition:<br>
 * <math>
 *   <mrow>
 *     <mi>&#x03A0;</mi><mo>:</mo><mi>a</mi><mo>.</mo><mi>x</mi>
 *     <mo>+</mo>
 *     <mi>b</mi><mo>.</mo><mi>y</mi>
 *     <mo>+</mo>
 *     <mi>c</mi><mo>.</mo><mi>z</mi>
 *     <mo>+</mo>
 *     <mi>d</mi><mo>=</mo><mn>0</mn>
 *   </mrow>
 * </math>
 * <p>
 * The normal to the plane is the vector
 * <math><mfenced><mi>a</mi><mi>b</mi><mi>c</mi></mfenced></math>.
 * 
 * Given three points in space
 * <math><mfenced><msub><mi>x</mi><mn>1</mn></msub></mi><msub><mi>y</mi><mn>1</mn></msub><msub><mi>z</mi><mn>1</mn></msub></mfenced></math>,
 * <math><mfenced><msub><mi>x</mi><mn>2</mn></msub></mi><msub><mi>y</mi><mn>2</mn></msub><msub><mi>z</mi><mn>2</mn></msub></mfenced></math> and 
 * <math><mfenced><msub><mi>x</mi><mn>3</mn></msub></mi><msub><mi>y</mi><mn>3</mn></msub><msub><mi>z</mi><mn>3</mn></msub></mfenced></math>,
 * the equation of the plane through these points is
 * given by the following determinants.
 * <p>
 * <math>
 *   <mrow>
 *     <mi>a</mi><mo>=</mo>
 *     <mfenced close="&#x2223; " open="&#x2223; ">
 *       <mtable>
 *         <mtr>
 *           <mtd><mn>1</mn></mtd>
 *           <mtd><msub><mi>y</mi><mn>1</mn></msub></mtd>
 *           <mtd><msub><mi>z</mi><mn>1</mn></msub></mtd>
 *         </mtr>
 *         <mtr>
 *           <mtd><mn>1</mn></mtd>
 *           <mtd><msub><mi>y</mi><mn>2</mn></msub></mtd>
 *           <mtd><msub><mi>z</mi><mn>2</mn></msub></mtd>
 *         </mtr>
 *         <mtr>
 *           <mtd><mn>1</mn></mtd>
 *           <mtd><msub><mi>y</mi><mn>3</mn></msub></mtd>
 *           <mtd><msub><mi>z</mi><mn>3</mn></msub></mtd>
 *         </mtr>
 *       </mtable>
 *     </mfenced>
 *   </mrow>
 * </math>
 * <math>
 *   <mrow>
 *     <mi>b</mi><mo>=</mo>
 *     <mfenced close="&#x2223; " open="&#x2223; ">
 *       <mtable>
 *         <mtr>
 *           <mtd><msub><mi>x</mi><mn>1</mn></msub></mtd>
 *           <mtd><mn>1</mn></mtd>
 *           <mtd><msub><mi>z</mi><mn>1</mn></msub></mtd>
 *         </mtr>
 *         <mtr>
 *           <mtd><msub><mi>x</mi><mn>2</mn></msub></mtd>
 *           <mtd><mn>1</mn></mtd>
 *           <mtd><msub><mi>z</mi><mn>2</mn></msub></mtd>
 *         </mtr>
 *         <mtr>
 *           <mtd><msub><mi>x</mi><mn>3</mn></msub></mtd>
 *           <mtd><mn>1</mn></mtd>
 *           <mtd><msub><mi>z</mi><mn>3</mn></msub></mtd>
 *         </mtr>
 *       </mtable>
 *     </mfenced>
 *   </mrow>
 * </math>
 * <math>
 *   <mrow>
 *     <mi>c</mi><mo>=</mo>
 *     <mfenced close="&#x2223; " open="&#x2223; ">
 *       <mtable>
 *         <mtr>
 *           <mtd><msub><mi>x</mi><mn>1</mn></msub></mtd>
 *           <mtd><msub><mi>y</mi><mn>1</mn></msub></mtd>
 *           <mtd><mn>1</mn></mtd>
 *         </mtr>
 *         <mtr>
 *           <mtd><msub><mi>x</mi><mn>2</mn></msub></mtd>
 *           <mtd><msub><mi>y</mi><mn>2</mn></msub></mtd>
 *           <mtd><mn>1</mn></mtd>
 *         </mtr>
 *         <mtr>
 *           <mtd><msub><mi>x</mi><mn>3</mn></msub></mtd>
 *           <mtd><msub><mi>y</mi><mn>3</mn></msub></mtd>
 *           <mtd><mn>1</mn></mtd>
 *         </mtr>
 *       </mtable>
 *     </mfenced>
 *   </mrow>
 * </math>
 * <math>
 *   <mrow>
 *     <mi>d</mi><mo>=</mo><mo>-</mo>
 *     <mfenced close="&#x2223; " open="&#x2223; ">
 *       <mtable>
 *         <mtr>
 *           <mtd><msub><mi>x</mi><mn>1</mn></msub></mtd>
 *           <mtd><msub><mi>y</mi><mn>1</mn></msub></mtd>
 *           <mtd><msub><mi>z</mi><mn>1</mn></msub></mtd>
 *         </mtr>
 *         <mtr>
 *           <mtd><msub><mi>x</mi><mn>2</mn></msub></mtd>
 *           <mtd><msub><mi>y</mi><mn>2</mn></msub></mtd>
 *           <mtd><msub><mi>z</mi><mn>2</mn></msub></mtd>
 *         </mtr>
 *         <mtr>
 *           <mtd><msub><mi>x</mi><mn>3</mn></msub></mtd>
 *           <mtd><msub><mi>y</mi><mn>3</mn></msub></mtd>
 *           <mtd><msub><mi>z</mi><mn>3</mn></msub></mtd>
 *         </mtr>
 *       </mtable>
 *     </mfenced>
 *   </mrow> 
 * </math>
 *
 * @author $Author: cbohrhauer$
 * @author $Author: sgalland$
 * @author $Author: hjaffali$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public abstract class AbstractPlane4F extends AbstractPlane3D<AbstractPlane4F> {

	private static final long serialVersionUID = 3765029151610752644L;

	/** Replies the intersection factor of the given segment
	 * when it is intersecting the plane.
	 * <p>
	 * If the segment and the plane are not intersecting, this
	 * function replies {@link Double#NaN}.
	 * If the segment and the plane are intersecting, two cases:<ol>
	 * <li>the segment is coplanar to the plane, then this function replies
	 * {@link Double#POSITIVE_INFINITY}.</li>
	 * <li>the segment and the plane have a single point of intersection,
	 * then this function replies the factor of the line's equation that
	 * permits to retreive the intersection point from the segment definition.
	 * </ol>
	 * 
	 * @param a first component of the plane equation.
	 * @param b second component of the plane equation.
	 * @param c third component of the plane equation.
	 * @param d fourth component of the plane equation.
	 * @param sx1 x coordinate of the first point of the segment.
	 * @param sy1 y coordinate of the first point of the segment.
	 * @param sz1 z coordinate of the first axis of the oriented box.
	 * @param sx2 x coordinate of the second point of the segment.
	 * @param sy2 y coordinate of the second point of the segment.
	 * @param sz2 z coordinate of the second axis of the oriented box.
	 * @return the factor that permits to compute the intersection point,
	 * {@link Double#NaN} when no intersection, {@link Double#POSITIVE_INFINITY}
	 * when an infinite number of intersection points.
	 */
	public static double getIntersectionFactorPlaneSegment(
			double a, double b, double c, double d,
			double sx1, double sy1, double sz1,
			double sx2, double sy2, double sz2) {
		double denom = a * (sx2 - sx1) + b * (sy2 - sy1) + c * (sz2 - sz1);
		
		if (denom == 0.) {
			// Segment and triangle's plane are parallel
			// Compute the distance between a point of the segment and the plane.
			double dist = a * sx1 + b * sy1 + c * sz1 + d;
			if (MathUtil.isEpsilonZero(dist)) {
				return Double.POSITIVE_INFINITY;
			}
		} else {
			double factor = (-a * sx1 - b * sy1 - c * sz1 - d) / denom;
			if (factor >= 0. && factor <= 1.) {
				return factor;
			}
		}
		
		return Double.NaN;
	}

	/**
	 * Replies the projection on the plane of a point.
	 * <p>
	 * <strong>First Approach: arithmetic resolution</strong>
	 * <p>
	 * Let <math xmlns="http://www.w3.org/1998/Math/MathML"><mover><mrow><mi>u</mi></mrow><mo stretchy="false">&#x02192;</mo></mover></math> a vector colinear to the line <math><mi mathvariant="normal">&#x00394;</mi></math> with components <math><mo stretchy="false">(</mo><msub><mi>x</mi><mi>u</mi></msub><mo>&#x0002C;</mo><msub><mi>y</mi><mi>u</mi></msub> <mo>&#x0002C;</mo><msub><mi>z</mi><mi>u</mi></msub><mo stretchy="false">)</mo></math>.<br>
	 * Let the equation of the plane <math><mi mathvariant="normal">&#x003A0;</mi></math> as <math><mi>a</mi><mo>.</mo><mi>x</mi><mo>&#x0002B;</mo><mi>b</mi><mo>.</mo><mi>y</mi><mo>&#x0002B;</mo><mi>c</mi><mo>.</mo> <mi>z</mi><mo>&#x0002B;</mo><mi>d</mi><mo>&#x0003D;</mo><mn>0</mn></math><br>
	 * Let the point <math><mi>A</mi></math> at coordinates <math><mo stretchy="false">(</mo><msub><mi>x</mi><mi>A</mi></msub><mo>&#x0002C;</mo><msub><mi>y</mi><mi>A</mi></msub> <mo>&#x0002C;</mo><msub><mi>z</mi><mi>A</mi></msub><mo stretchy="false">)</mo></math> and its projection point <math><msup><mi>A</mi><mo>&#x02032;</mo></msup></math> at coordinates <math><mo stretchy="false">(</mo><msub><mi>x</mi><mrow><msup><mi>A</mi><mo>&#x02032;</mo></msup></mrow></msub>
	 * <mo>&#x0002C;</mo><msub><mi>y</mi><mrow><msup><mi>A</mi><mo>&#x02032;</mo></msup></mrow></msub> <mo>&#x0002C;</mo><msub><mi>z</mi><mrow><msup><mi>A</mi><mo>&#x02032;</mo></msup></mrow></msub><mo stretchy="false">)</mo></math>.
	 * <p>
	 * Since <math><mo stretchy="false">(</mo><mi>A</mi><msup><mi>A</mi><mo>&#x02032;</mo></msup><mo stretchy="false">)</mo></math> is parallel to <math><mi mathvariant="normal">&#x00394;</mi></math>, a scalar <math><mi>k</mi></math> exists such as <math><mover><mrow><mi>A</mi><mrow><mi>A</mi><msup><mo>&#x02032;</mo></msup></mrow></mrow><mo stretchy="true">&#x02192;</mo></mover> <mo>&#x0003D;</mo><mi>k</mi><mo>&#x022C5;</mo><mover><mi>u</mi><mo
	 * stretchy="false">&#x02192;</mo></mover></math><br>
	 * that <math><mrow><mo>&#x0007B;</mo><mtable><mtr><mtd><msub><mi>x</mi> <mrow><msup><mi>A</mi><mo>&#x02032;</mo></msup></mrow></msub> <mo>&#x02212;</mo><msub><mi>x</mi><mi>A</mi></msub><mo>&#x0003D;</mo> <mi>k</mi><mo>&#x022C5;</mo><msub><mi>x</mi><mi>u</mi></msub> </mtd></mtr><mtr><mtd><msub><mi>y</mi><mrow><msup><mi>A</mi> <mo>&#x02032;</mo></msup></mrow></msub><mo>&#x02212;</mo><msub> <mi>y</mi><mi>A</mi></msub><mo>&#x0003D;</mo><mi>k</mi>
	 * <mo>&#x022C5;</mo><msub><mi>y</mi><mi>u</mi></msub></mtd></mtr> <mtr><mtd><msub><mi>z</mi><mrow><msup><mi>A</mi><mo>&#x02032;</mo> </msup></mrow></msub><mo>&#x02212;</mo><msub><mi>z</mi><mi>A</mi> </msub><mo>&#x0003D;</mo><mi>k</mi><mo>&#x022C5;</mo><msub><mi>z</mi> <mi>u</mi></msub></mtd></mtr></mtable></mrow></math>.
	 * <p>
	 * 
	 * In addition, <math><msup><mi>A</mi><mo>&#x02032;</mo></msup></math> is on <math><mi mathvariant="normal">&#x003A0;</mi></math>, what means that <math> <mi>a</mi><mo>.</mo><msub><mi>x</mi><mrow><msup><mi>A</mi> <mo>&#x02032;</mo></msup></mrow></msub><mo>&#x0002B;</mo><mi>b</mi> <mo>.</mo><msub><mi>y</mi><mrow><msup><mi>A</mi><mo>&#x02032;</mo> </msup></mrow></msub><mo>&#x0002B;</mo><mi>c</mi><mo>.</mo> <msub><mi>z</mi><mrow><msup><mi>A</mi><mo>&#x02032;</mo></msup>
	 * </mrow></msub><mo>&#x0002B;</mo><mi>d</mi><mo>&#x0003D;</mo><mn>0</mn> </math>.
	 * <p>
	 * The result is a system of 4 equations with 4 unknown variables: <math><msub><mi>x</mi><mrow><msup><mi>A</mi><mo>&#x02032;</mo></msup></mrow></msub><math>, <math><msub><mi>y</mi><mrow><msup><mi>A</mi><mo>&#x02032;</mo></msup></mrow></msub><math>, <math><msub><mi>z</mi><mrow><msup><mi>A</mi><mo>&#x02032;</mo></msup></mrow></msub><math> and <math><mi>k</mi><math>.
	 * <p>
	 * <math> <mrow><mo>&#x0007B;</mo><mtable><mtr><mtd><msub><mi>x</mi><mrow> <msup><mi>A</mi><mo>&#x02032;</mo></msup></mrow></msub> <mo>&#x0003D;</mo><mfrac><mrow><mi>b</mi><mo>&#x022C5;</mo><mo stretchy="false">(</mo><msub><mi>x</mi><mi>A</mi></msub><msub> <mi>y</mi><mi>u</mi></msub><mo>&#x02212;</mo><msub><mi>y</mi> <mi>A</mi></msub><msub><mi>x</mi><mi>u</mi></msub><mo stretchy="false">)</mo><mo>&#x0002B;</mo><mi>c</mi><mo>&#x022C5;</mo> <mo
	 * stretchy="false">(</mo><msub><mi>x</mi><mi>A</mi></msub> <msub><mi>z</mi><mi>u</mi></msub><mo>&#x02212;</mo><msub><mi>z</mi> <mi>A</mi></msub><msub><mi>x</mi><mi>u</mi></msub><mo stretchy="false">)</mo><mo>&#x02212;</mo><mi>d</mi><mo>&#x022C5;</mo> <msub><mi>x</mi><mi>u</mi></msub></mrow><mrow><mi>a</mi> <mo>&#x022C5;</mo><msub><mi>x</mi><mi>u</mi></msub><mo>&#x0002B;</mo> <mi>b</mi><mo>&#x022C5;</mo><msub><mi>y</mi><mi>u</mi></msub>
	 * <mo>&#x0002B;</mo><mi>c</mi><mo>&#x022C5;</mo><msub><mi>z</mi> <mi>u</mi></msub></mrow></mfrac></mtd></mtr><mtr><mtd><msub> <mi>y</mi><mrow><msup><mi>A</mi><mo>&#x02032;</mo></msup></mrow> </msub><mo>&#x0003D;</mo><mfrac><mrow><mi>a</mi><mo>&#x022C5;</mo> <mo stretchy="false">(</mo><msub><mi>y</mi><mi>A</mi></msub> <msub><mi>x</mi><mi>u</mi></msub><mo>&#x02212;</mo><msub><mi>x</mi> <mi>A</mi></msub><msub><mi>y</mi><mi>u</mi></msub><mo
	 * stretchy="false">)</mo><mo>&#x0002B;</mo><mi>c</mi><mo>&#x022C5;</mo> <mo stretchy="false">(</mo><msub><mi>y</mi><mi>A</mi></msub> <msub><mi>z</mi><mi>u</mi></msub><mo>&#x02212;</mo><msub><mi>z</mi> <mi>A</mi></msub><msub><mi>y</mi><mi>u</mi></msub><mo stretchy="false">)</mo><mo>&#x02212;</mo><mi>d</mi><mo>&#x022C5;</mo> <msub><mi>y</mi><mi>u</mi></msub></mrow><mrow><mi>a</mi> <mo>&#x022C5;</mo><msub><mi>x</mi><mi>u</mi></msub><mo>&#x0002B;</mo>
	 * <mi>b</mi><mo>&#x022C5;</mo><msub><mi>y</mi><mi>u</mi></msub> <mo>&#x0002B;</mo><mi>c</mi><mo>&#x022C5;</mo><msub><mi>z</mi> <mi>u</mi></msub></mrow></mfrac></mtd></mtr><mtr><mtd><msub> <mi>z</mi><mrow><msup><mi>A</mi><mo>&#x02032;</mo></msup></mrow> </msub><mo>&#x0003D;</mo><mfrac><mrow><mi>a</mi><mo>&#x022C5;</mo> <mo stretchy="false">(</mo><msub><mi>z</mi><mi>A</mi></msub> <msub><mi>x</mi><mi>u</mi></msub><mo>&#x02212;</mo><msub><mi>x</mi>
	 * <mi>A</mi></msub><msub><mi>z</mi><mi>u</mi></msub><mo stretchy="false">)</mo><mo>&#x0002B;</mo><mi>b</mi><mo>&#x022C5;</mo> <mo stretchy="false">(</mo><msub><mi>z</mi><mi>A</mi></msub> <msub><mi>y</mi><mi>u</mi></msub><mo>&#x02212;</mo><msub><mi>y</mi> <mi>A</mi></msub><msub><mi>z</mi><mi>u</mi></msub><mo stretchy="false">)</mo><mo>&#x02212;</mo><mi>d</mi><mo>&#x022C5;</mo> <msub><mi>z</mi><mi>u</mi></msub></mrow><mrow><mi>a</mi>
	 * <mo>&#x022C5;</mo><msub><mi>x</mi><mi>u</mi></msub><mo>&#x0002B;</mo> <mi>b</mi><mo>&#x022C5;</mo><msub><mi>y</mi><mi>u</mi></msub> <mo>&#x0002B;</mo><mi>c</mi><mo>&#x022C5;</mo><msub><mi>z</mi> <mi>u</mi></msub></mrow></mfrac></mtd></mtr></mtable></mrow> </math>
	 * <p>
	 * In the case of an orthogonal projection and if the reference axis are orthonormal, one can choose <math><msub><mi>x</mi><mi>u</mi></msub><mo>&#x0003D;</mo><mi>a</mi></math>, <math><msub><mi>y</mi><mi>u</mi></msub><mo>&#x0003D;</mo><mi>b</mi></math>, <math><msub><mi>z</mi><mi>u</mi></msub><mo>&#x0003D;</mo><mi>c</mi></math>, then:
	 * <p>
	 * <math display="block" xmlns="http://www.w3.org/1998/Math/MathML"><mrow> <mo>&#x0007B;</mo><mtable><mtr><mtd><msub><mi>x</mi><mrow><msup> <mi>A</mi><mo>&#x02032;</mo></msup></mrow></msub><mo>&#x0003D;</mo> <mfrac><mrow><mo stretchy="false">(</mo><msup><mi>b</mi><mn>2</mn> </msup><mo>&#x0002B;</mo><msup><mi>c</mi><mn>2</mn></msup><mo stretchy="false">)</mo><mo>&#x022C5;</mo><msub><mi>x</mi><mi>A</mi> </msub><mo>&#x02212;</mo><mi>a</mi><mi>b</mi><mo>&#x022C5;</mo>
	 * <msub><mi>y</mi><mi>A</mi></msub><mo>&#x02212;</mo><mi>a</mi> <mi>c</mi><mo>&#x022C5;</mo><msub><mi>z</mi><mi>A</mi></msub> <mo>&#x02212;</mo><mi>d</mi><mo>&#x022C5;</mo><mi>a</mi></mrow> <mrow><msup><mi>a</mi><mn>2</mn></msup><mo>&#x0002B;</mo><msup> <mi>b</mi><mn>2</mn></msup><mo>&#x0002B;</mo><msup><mi>c</mi> <mn>2</mn></msup></mrow></mfrac></mtd></mtr><mtr><mtd><msub> <mi>y</mi><mrow><msup><mi>A</mi><mo>&#x02032;</mo></msup></mrow>
	 * </msub><mo>&#x0003D;</mo><mfrac><mrow><mo>&#x02212;</mo><mi>a</mi> <mi>b</mi><mo>&#x022C5;</mo><msub><mi>x</mi><mi>A</mi></msub> <mo>&#x0002B;</mo><mo stretchy="false">(</mo><msup><mi>a</mi><mn>2</mn> </msup><mo>&#x0002B;</mo><msup><mi>c</mi><mn>2</mn></msup><mo stretchy="false">)</mo><mo>&#x022C5;</mo><msub><mi>y</mi><mi>A</mi> </msub><mo>&#x02212;</mo><mi>b</mi><mi>c</mi><mo>&#x022C5;</mo> <msub><mi>z</mi><mi>A</mi></msub><mo>&#x02212;</mo><mi>d</mi>
	 * <mo>&#x022C5;</mo><mi>b</mi></mrow><mrow><msup><mi>a</mi><mn>2</mn> </msup><mo>&#x0002B;</mo><msup><mi>b</mi><mn>2</mn></msup> <mo>&#x0002B;</mo><msup><mi>c</mi><mn>2</mn></msup></mrow> </mfrac></mtd></mtr><mtr><mtd><msub><mi>z</mi><mrow><msup><mi>A</mi> <mo>&#x02032;</mo></msup></mrow></msub><mo>&#x0003D;</mo><mfrac> <mrow><mo>&#x02212;</mo><mi>a</mi><mi>c</mi><mo>&#x022C5;</mo> <msub><mi>x</mi><mi>A</mi></msub><mo>&#x02212;</mo><mi>b</mi>
	 * <mi>c</mi><mo>&#x022C5;</mo><msub><mi>y</mi><mi>A</mi></msub> <mo>&#x0002B;</mo><mo stretchy="false">(</mo><msup><mi>a</mi><mn>2</mn> </msup><mo>&#x0002B;</mo><msup><mi>b</mi><mn>2</mn></msup><mo stretchy="false">)</mo><mo>&#x022C5;</mo><msub><mi>z</mi><mi>A</mi> </msub><mo>&#x02212;</mo><mi>d</mi><mo>&#x022C5;</mo><mi>c</mi> </mrow><mrow><msup><mi>a</mi><mn>2</mn></msup><mo>&#x0002B;</mo> <msup><mi>b</mi><mn>2</mn></msup><mo>&#x0002B;</mo><msup><mi>c</mi>
	 * <mn>2</mn></msup></mrow></mfrac></mtd></mtr></mtable></mrow> </math>
	 * <p>
	 * <strong>Second Approach: vectorial resolution</strong>
	 * <p>
	 * Let the normal of the plane be <math><mover><mi>n</mi><mo stretchy="false">&#x02192;</mo></mover></math> with the coordinates <math><mo stretchy="false">(</mo><msub><mi>n</mi><mi>x</mi></msub><mo>&#x0002C;</mo> <msub><mi>n</mi><mi>y</mi></msub><mo>&#x0002C;</mo><msub><mi>n</mi><mi>z</mi></msub> <mo stretchy="false">)</mo></math>.<br>
	 * Let <math><mi>A</mi></math> the point of coordinates <math><mo stretchy="false">(</mo><msub><mi>A</mi><mi>x</mi></msub><mo>&#x0002C;</mo> <msub><mi>A</mi><mi>y</mi></msub><mo>&#x0002C;</mo><msub><mi>A</mi><mi>z</mi></msub> <mo stretchy="false">)</mo></math> and its projection on the plane <math><msup><mi>A</mi><mo>&#x02032;</mo></msup></math> with the coordinates <math><mo
	 * stretchy="false">(</mo><msub><mrow><msup><mi>A</mi><mo>&#x02032;</mo></msup></mrow><mi>x</mi></msub> <mo>&#x0002C;</mo><msub><mrow><msup><mi>A</mi><mo>&#x02032;</mo></msup></mrow><mi>y</mi></msub> <mo>&#x0002C;</mo><msub><mrow><msup><mi>A</mi><mo>&#x02032;</mo></msup></mrow><mi>z</mi></msub> <mo stretchy="false">)</mo></math>.
	 * <p>
	 * Let <math><mi>s</mi></math> the distance between the point <math><mi>A</mi></math> and the nearest point on the plane, ie. the point <math><msup><mi>A</mi><mo>&#x02032;</mo></msup></math> such as: <math display="block" xmlns="http://www.w3.org/1998/Math/MathML"><mi>s</mi><mo>&#x0003D;</mo> <mi>a</mi><mo>.</mo><msub><mi>A</mi><mi>x</mi></msub> <mo>&#x0002B;</mo><mi>b</mi><mo>.</mo><msub><mi>A</mi><mi>y</mi> </msub><mo>&#x0002B;</mo><mi>c</mi><mo>.</mo><msub><mi>A</mi>
	 * <mi>z</mi></msub><mo>&#x0002B;</mo><mi>d</mi></math>. If <math><mi>s</mi></math> is positive, the point is in the front of the plane. If <math><mi>s</mi></math> is negative, the point is behind the plane.
	 * <p>
	 * Consequently <math display="block" xmlns="http://www.w3.org/1998/Math/MathML"><msup><mi>A</mi> <mo>&#x02032;</mo></msup><mo>&#x0003D;</mo><mi>A</mi><mo>&#x02212;</mo> <mfrac><mrow><mi>s</mi></mrow><mrow><mo stretchy="false">&#x0007C;</mo> <mover><mrow><mrow><mi>n</mi></mrow></mrow><mo stretchy="false">&#x02192;</mo></mover><mo stretchy="false">&#x0007C;</mo> </mrow></mfrac><mover><mrow><mrow><mi>n</mi></mrow></mrow><mo stretchy="false">&#x02192;</mo></mover></math>.
	 * <p>
	 * <math display="block" xmlns="http://www.w3.org/1998/Math/MathML"> <mrow> <mo>&#x0007B;</mo> <mtable> <mtr> <mtd> <msub> <mi>x</mi> <mrow> <msup> <mi>A</mi> <mo>&#x02032;</mo> </msup> </mrow> </msub> <mo>&#x0003D;</mo> <msub> <mi>x</mi> <mi>A</mi> </msub> <mo>&#x02212;</mo> <mfrac> <mrow> <mi>s</mi> </mrow> <mrow> <mo stretchy="false">&#x0007C;</mo> <mover> <mrow> <mrow> <mi>n</mi> </mrow> </mrow> <mo stretchy="false">&#x02192;</mo> </mover> <mo
	 * stretchy="false">&#x0007C;</mo> </mrow> </mfrac> <msub> <mi>n</mi> <mi>x</mi> </msub> </mtd> </mtr> <mtr> <mtd> <msub> <mi>y</mi> <mrow> <msup> <mi>A</mi> <mo>&#x02032;</mo> </msup> </mrow> </msub> <mo>&#x0003D;</mo> <msub> <mi>y</mi> <mi>A</mi> </msub> <mo>&#x02212;</mo> <mfrac> <mrow> <mi>s</mi> </mrow> <mrow> <mo stretchy="false">&#x0007C;</mo> <mover> <mrow> <mrow> <mi>n</mi> </mrow> </mrow> <mo stretchy="false">&#x02192;</mo> </mover> <mo
	 * stretchy="false">&#x0007C;</mo> </mrow> </mfrac> <msub> <mi>n</mi> <mi>y</mi> </msub> </mtd> </mtr> <mtr> <mtd> <msub> <mi>z</mi> <mrow> <msup> <mi>A</mi> <mo>&#x02032;</mo> </msup> </mrow> </msub> <mo>&#x0003D;</mo> <msub> <mi>z</mi> <mi>A</mi> </msub> <mo>&#x02212;</mo> <mfrac> <mrow> <mi>s</mi> </mrow> <mrow> <mo stretchy="false">&#x0007C;</mo> <mover> <mrow> <mrow> <mi>n</mi> </mrow> </mrow> <mo stretchy="false">&#x02192;</mo> </mover> <mo
	 * stretchy="false">&#x0007C;</mo> </mrow> </mfrac> <msub> <mi>n</mi> <mi>z</mi> </msub> </mtd> </mtr> </mtable> </mrow> </math>
	 * 
	 * @param a the a component of the plane equation.
	 * @param b the b component of the plane equation.
	 * @param c the c component of the plane equation.
	 * @param d the d component of the plane equation.
	 * @param x the x coordinate of the project to project on the plane.
	 * @param y the y coordinate of the project to project on the plane.
	 * @param z the z coordinate of the project to project on the plane.
	 * @return the projection of the specified point on the plane.
	 */
	public static Point3f computePointProjection(
			double a, double b, double c, double d,
			double x, double y, double z) {
		// Arithmetic resolution
		/*
		 * plane.normalize();
		 * 
		 * Vector3d n = plane.getNormal(); double d = plane.getEquationComponentD(); // normalization apply that a*a+b*b+c*c = 1
		 * 
		 * double xaprime = (n.y*n.y+n.z*n.z)*px -n.x*n.y*py -n.x*n.z*pz -d*n.x;
		 * 
		 * double yaprime = -n.x*n.y*px +(n.x*n.x+n.z*n.z)*py +n.y*n.z*pz -d*n.y;
		 * 
		 * double zaprime = -n.x*n.z*px +n.y*n.z*py +(n.x*n.x+n.y*n.y)*pz -d*n.z;
		 * 
		 * return new Point3d(xaprime,yaprime,zaprime);
		 */

		// Vectorial resolution
		Vector3f invnormal = new Vector3f(-a, -b, -c);
		invnormal.normalize();

		// Distance between the point and the plane
		double distance = a * x + b * y + c * z + d;

		double xaprime = x + distance * invnormal.x;
		double yaprime = y + distance * invnormal.y;
		double zaprime = z + distance * invnormal.z;

		return new Point3f(xaprime, yaprime, zaprime);
	}

	

	/** Clear buffered values.
	 */
	abstract protected void clearBufferedValues();

	/** {@inheritDoc}
	 */
	@Override
	public void negate() {
		this.set(-getEquationComponentA(), -getEquationComponentB(),- getEquationComponentC(), getEquationComponentD());
	}

	/** {@inheritDoc}
	 */
	@Override
	public void absolute() {
		this.set(Math.abs(getEquationComponentA()), Math.abs(getEquationComponentB()), Math.abs(getEquationComponentC()), Math.abs(getEquationComponentD()));
	}

	/** {@inheritDoc}
	 */
	@Override
	public AbstractPlane4F normalize() {
		double t = Math.sqrt(this.getEquationComponentA()*this.getEquationComponentA()+this.getEquationComponentB()*this.getEquationComponentB()+this.getEquationComponentC()*this.getEquationComponentC());
		this.set(getEquationComponentA()/t, getEquationComponentB()/t, getEquationComponentC()/t, getEquationComponentD()/t);
		return this;
	}

	/** {@inheritDoc}
	 */
	@Override
	abstract public void set(Plane3D<?> plane);

	/** Set this pane to be coplanar with all the three specified points.
	 * 
	 * @param p1x is a point on the plane
	 * @param p1y is a point on the plane
	 * @param p1z is a point on the plane
	 * @param p2x is a point on the plane
	 * @param p2y is a point on the plane
	 * @param p2z is a point on the plane
	 * @param p3x is a point on the plane
	 * @param p3y is a point on the plane
	 * @param p3z is a point on the plane
	 */
	abstract public void set(double p1x, double p1y, double p1z, double p2x, double p2y, double p2z, double p3x, double p3y, double p3z);

	/** Set this pane to be coplanar with all the three specified points.
	 *  
	 * @param p1 is a point on the plane
	 * @param p2 is a point on the plane
	 * @param p3 is a point on the plane
	 */
	abstract public void set(Point3D p1, Point3D p2, Point3D p3);

	/** Set this pane with the given factors.
	 *  
	 * @param a is the first factor of the plane equation.
	 * @param b is the first factor of the plane equation.
	 * @param c is the first factor of the plane equation.
	 * @param d is the first factor of the plane equation.
	 */
	@SuppressWarnings("hiding")
	abstract public void set(double a, double b, double c, double d);

	/** {@inheritDoc}
	 */
	@Override
	abstract public Vector3f getNormal();

	/** {@inheritDoc}
	 */
	@Override
	abstract public double getEquationComponentA();

	/** {@inheritDoc}
	 */
	@Override
	abstract public double getEquationComponentB();

	/** {@inheritDoc}
	 */
	@Override
	abstract public double getEquationComponentC();

	/** {@inheritDoc}
	 */
	@Override
	abstract public double getEquationComponentD();
	
	
	abstract protected void setEquationComponentC(double z);

	abstract protected void setEquationComponentB(double y);

	abstract protected void setEquationComponentA(double x);
	
	abstract protected void setEquationComponentD(double w);


	/** {@inheritDoc}
	 */
	@Override
	public double distanceTo(double x, double y, double z) {
		return this.getEquationComponentA() * x + this.getEquationComponentB() * y + this.getEquationComponentC() * z + this.getEquationComponentD();
	}

	/** {@inheritDoc}
	 */
	@Override
	public void clear() {
		this.set(0f,0f,0f,0f);
	}

	@Override
	abstract public void setPivot(double x, double y, double z);

	/** Replies the pivot point around which the rotation must be done.
	 * 
	 * @return a reference on the buffered pivot point.
	 */
	abstract public Point3f getPivot();

	@Override
	abstract public Point3f getProjection(double x, double y, double z);

	/** Apply the given transformation matrix on the plane.
	 * The pivot point used for transforming this plane with
	 * the given matrix is given by {@link #getPivot()}.
	 *
	 * @param transform the transformation matrix.
	 */
	public void transform(Transform3D transform) {
		transform(transform, null);
	}

	/** Apply the given transformation matrix on the plane.
	 *
	 * @param transform the transformation matrix.
	 * @param pivot the pivot point.
	 */
	public void transform(Transform3D transform, Point3D pivot) {
		Point3D refPoint = (pivot == null) ? getPivot() : pivot;
		Vector3f v = new Vector3f(this.getEquationComponentA(),this.getEquationComponentB(),this.getEquationComponentC());
    	transform.transform(v);

    	// Update the plane equation according
    	// to the desired normal (computed from
    	// the identity vector and the rotations).
    	
    	// a.x + b.y + c.z + d = 0
    	// where (x,y,z) is the translation point
    	
    	
    	this.set(v.getX(),v.getY(),v.getZ(),- (this.getEquationComponentA()*(refPoint.getX()+transform.m03) +
    				this.getEquationComponentB()*(refPoint.getY()+transform.m13) +
    				this.getEquationComponentC()*(refPoint.getZ()+transform.m23)));
    	
		clearBufferedValues();
	}

	/** Translate the pivot point of the plane.
	 *
	 * @param translation the translation to apply.
	 */
	public void translate(Vector3D translation) {
		translate(translation.getX(), translation.getY(), translation.getZ());
	}

	/** Translate the pivot point of the plane.
	 *
	 * @param dx the translation to apply.
	 * @param dy the translation to apply.
	 * @param dz the translation to apply.
	 */
	public void translate(double dx, double dy, double dz) {
		// Compute the reference point for the plane
    	// (usefull for translation)
    	Point3f refPoint = getPivot();
    	
    	// a.x + b.y + c.z + d = 0
    	// where (x,y,z) is the translation point
    	setPivot(refPoint.getX()+dx,refPoint.getY()+dy,refPoint.getZ()+dz);
	}

	/** Rotate the plane around the pivot point.
	 *
	 * @param rotation the rotation to apply.
	 */
	public void rotate(Quaternion rotation) {
		rotate(rotation, null);
	}

	/** Rotate the plane around the given pivot point.
	 *
	 * @param rotation the rotation to apply.
	 * @param pivot the pivot point.
	 */
	public void rotate(Quaternion rotation, Point3D pivot) {
		Point3D currentPivot = getPivot();
    	// Update the plane equation according
    	// to the desired normal (computed from
    	// the identity vector and the rotations).
    	Transform3D m = new Transform3D();
    	m.setRotation(rotation);
    	Vector3f v = new Vector3f(this.getEquationComponentA(),this.getEquationComponentB(),this.getEquationComponentC());
    	m.transform(v);
    	this.setEquationComponentA(v.getX());
    	this.setEquationComponentB(v.getY());
    	this.setEquationComponentC(v.getZ());
    	
    	if (pivot==null) {
	    	// a.x + b.y + c.z + d = 0
	    	// where (x,y,z) is the translation point
	    	this.setEquationComponentD( - (this.getEquationComponentA()*currentPivot.getX() +
	    				this.getEquationComponentB()*currentPivot.getY() +
	    				this.getEquationComponentC()*currentPivot.getZ()));
    	}
    	else {
    		// Compute the new point
    		Point3f nP = new Point3f(
    				currentPivot.getX() - pivot.getX(),
    				currentPivot.getY() - pivot.getY(),
    				currentPivot.getZ() - pivot.getZ());
    		m.transform(nP);
    		nP.setX(nP.getX() + pivot.getX());
    		nP.setY(nP.getY() + pivot.getY());
    		nP.setZ(nP.getZ() + pivot.getZ());
    		
	    	// a.x + b.y + c.z + d = 0
	    	// where (x,y,z) is the translation point
	    	this.setEquationComponentD( - (this.getEquationComponentA()*nP.getX() +
	    				this.getEquationComponentB()*nP.getY() +
	    				this.getEquationComponentC()*nP.getZ()));
    	}
    	
		clearBufferedValues();
	}

	/** Rotate the plane with the given angle around the given axis.
	 * The rotation is done around the pivot point.
	 *
	 * @param axis the rotation axis.
	 * @param angle the rotation angle.
	 */
	public void rotate(Vector3D axis, double angle) {
		Quaternion q = new Quaternion();
		q.setAxisAngle(axis, angle);
		rotate(q, null);
	}

	/** Rotate the plane with the given angle around the given axis.
	 * The rotation is done around the pivot point.
	 *
	 * @param axis the rotation axis.
	 * @param angle the rotation angle.
	 * @param pivot the pivot point.
	 */
	public void rotate(Vector3D axis, double angle, Point3D pivot) {
		Quaternion q = new Quaternion();
		q.setAxisAngle(axis, angle);
		rotate(q, pivot);
   	}

	@SuppressWarnings("static-method")
	public boolean isValid() {
		// TODO Auto-generated method stub
		return false;
	}


}