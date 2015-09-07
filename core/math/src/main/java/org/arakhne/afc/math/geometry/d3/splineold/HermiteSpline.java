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
package org.arakhne.afc.math.geometry.d3.splineold;

import java.util.ArrayList;
import java.util.List;

import org.arakhne.afc.math.geometry.d3.Point3D;
import org.arakhne.afc.math.geometry.d3.Vector3D;
import org.arakhne.afc.math.matrix.Matrix4f;

/**
 * This class compute an Hermite curve.
 * The main calculation is operate by the method
 * compute(points, tangentes)
 * The simple method compute(points) is not usable
 * because Hermite need tengantes for computation
 * 
 * @author $Author: nvieval$
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class HermiteSpline extends AbstractSpline<HermiteSpline> {
    
	private static final long serialVersionUID = -3412797260160384464L;

	/** Replies the Hermite matrix for supporting polyspline.
	 *
	 * <pre><code>
    * | 2 -2  1  1 |
    * |-3  3 -2 -1 |
    * | 0  0  1  0 |
    * | 1  0  0  0 |
    * </code></pre>
	 *
	 * @return matrix the hermite matrix.
	 */
	private static Matrix4f getPolySplineMatrix() {
		Matrix4f hermiteMatrix = new Matrix4f();
		
		hermiteMatrix.setM00(2f);
		hermiteMatrix.setM01(-2f);
		hermiteMatrix.setM02(1f);
		hermiteMatrix.setM03(1f);

		hermiteMatrix.setM10(-3f);
		hermiteMatrix.setM11(3f);
		hermiteMatrix.setM12(-2f);
		hermiteMatrix.setM13(-1f);

		hermiteMatrix.setM20(0f);
		hermiteMatrix.setM21(0f);
		hermiteMatrix.setM22(1f);
		hermiteMatrix.setM23(0f);

		hermiteMatrix.setM30(1f);
		hermiteMatrix.setM31(0f);
		hermiteMatrix.setM32(0f);
		hermiteMatrix.setM33(0f);
		
		return hermiteMatrix;
	}
	
	/** Hermite matrix for supporting polyspline.
	 */
	protected static final Matrix4f POLYSPLINE_MATRIX = getPolySplineMatrix();

	private final List<Vector3D> tangents = new ArrayList<>();
	
	/**
	 */
	public HermiteSpline() {
		//
	}

    /**
     * Change the control points and recompute the spline's points.
     *
     * @param controlPoints the control points.
     * @param tangents the tangents.
     */
    public void setControlPoints(
    		List<? extends Point3D> controlPoints,
    		List<? extends Vector3D> tangents) {
    	assert (controlPoints.size() == tangents.size());
    	
    	this.controlPoints.clear();
    	this.tangents.clear();
    	for (Point3D p : controlPoints) {
    		this.controlPoints.add(p.clone());
    	}
    	this.tangents.addAll(tangents);
    	
    	reset();
    }
    
    @Override
	protected void ensurePoints() {
		if (this.points == null) {
			List<Point3D> globalPoints = new ArrayList<>();
	        Point3D p1, p2;
	        Vector3D t1, t2;
			for (int i = 0; i < (this.controlPoints.size() - 1); ++i) {
	            p1 = this.controlPoints.get(i);
	            if(i > 0) {
	                p2 = this.controlPoints.get(i - 1);
	            } else {
	            	p2 = p1.clone();
	            	p2.set(0, 0, 0);
	            }
	            t1 = this.tangents.get(i);
	            if(i > 0) {
	                t2 = this.tangents.get(i - 1);
	            } else {
	            	t2 = t1.clone();
	            	t2.set(0, 0, 0);
	            }
				if (i == 0) {
					List<Point3D> intermediatePoints = new ArrayList<>();
					intermediatePoints = computeSplineSegment(p1, p1, t1, t1);
					globalPoints.addAll(intermediatePoints);
				} else {
					if (i == (this.controlPoints.size() - 2)) {
						List<Point3D> intermediatePoints = new ArrayList<>();
						intermediatePoints = computeSplineSegment(p2, p1, t2, t1);
						globalPoints.addAll(intermediatePoints);
					} else {
						List<Point3D> intermediatePoints = new ArrayList<>();
						intermediatePoints = computeSplineSegment(p2, p1, t2, t1);
						globalPoints.addAll(intermediatePoints);
					}
				}
			}
	        this.points = globalPoints;
		}
	}

    /**
	 * Calculate the spline between point 1 and point 2.<br>
	 * the tengantes 1 and 2 are use too.
	 *
	 * @param pt1
	 *            the first point
	 * @param pt2
	 *            the second point
	 * @param t1
	 *            the first tengante
	 * @param t2
	 *            the second tengante
	 * @return the array list of points
	 */
	private List<Point3D> computeSplineSegment(Point3D pt1, Point3D pt2, Vector3D t1, Vector3D t2) {
		ArrayList<Point3D> newPoints = new ArrayList<>();
		double segmentLength         = pt1.distance(pt2);
		double nbIntermediatePoints  = segmentLength / getDiscretizationFactor();

		for (double j = 0D; j < 1D; j += 1 / nbIntermediatePoints) {
			newPoints.add(computeIntermediatePoint(j, pt1, pt2, t1, t2));
		}
		return newPoints;
	}

	/**
	 * Calculate the intermediate point with t and the geometric matrix.
	 * @param t
	 *            the place the point
	 * @param pt1
	 *            the first point
	 * @param pt2
	 *            the second point
	 * @param pt3
	 *            the third point
	 * @param pt4
	 *            the fourth point
	 * @return the intermediate point
	 */
	private static Point3D computeIntermediatePoint(double t, Point3D pt1, Point3D pt2, Vector3D ta1, Vector3D ta2) {
		// math for point
		// Q(t) = (x(t), y(t) ) = T.C = T.M.G
		// T : t³ + t² + t + 1
		// M : base matrix
		// G : geometric matrix
		double xValue = 0D, yValue = 0D, zValue = 0D;
        double t3 = Math.pow(t, 3);
        double t2 = Math.pow(t, 2);
        
        // generate the geometric matrix
		Matrix4f pointsMatrix = getGeometricMatrix(pt1, pt2, ta1, ta2);

		Matrix4f inter = new Matrix4f(); // generate the intermediate matrix
		inter.mul(POLYSPLINE_MATRIX, pointsMatrix);

		xValue = (inter.getM00() * t3) + (inter.getM10() * t2) + (inter.getM20() * t) + inter.getM30();
		yValue = (inter.getM01() * t3) + (inter.getM11() * t2) + (inter.getM21() * t) + inter.getM31();
		zValue = (inter.getM02() * t3) + (inter.getM12() * t2) + (inter.getM22() * t) + inter.getM32();

		Point3D p = pt1.clone();
		p.set(xValue, yValue, zValue);
		return p;
	}

	/**
	 * Set matrix of geometric contsraint
	 *
	 * @param pt1
	 *            the first point to define the matrix
	 * @param pt2
	 *            the second point to define the matrix
	 * @param t1
	 *            the first tengante to define the matrix
	 * @param t2
	 *            the second tengante to define the matrix
	 * @return the geometric matrix
	 */
	private static Matrix4f getGeometricMatrix(Point3D pt1, Point3D pt2, Vector3D t1, Vector3D t2) {
		Matrix4f pointsMatrix = new Matrix4f();

		pointsMatrix.setM00(pt1.getX());
		pointsMatrix.setM01(pt1.getY());
		pointsMatrix.setM02(pt1.getZ());
		pointsMatrix.setM03(0);
		pointsMatrix.setM10(pt2.getX());
		pointsMatrix.setM11(pt2.getY());
		pointsMatrix.setM12(pt2.getZ());
		pointsMatrix.setM13(0);
		pointsMatrix.setM20(t1.getX());
		pointsMatrix.setM21(t1.getY());
		pointsMatrix.setM22(t1.getZ());
		pointsMatrix.setM23(0);
		pointsMatrix.setM30(t2.getX());
		pointsMatrix.setM31(t2.getY());
		pointsMatrix.setM32(t2.getZ());
		pointsMatrix.setM33(0);

		return pointsMatrix;
	}
}
