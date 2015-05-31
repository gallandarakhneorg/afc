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
package org.arakhne.afc.math.geometry.sfc.geometry3d.continuous.spline;

import java.util.ArrayList;
import java.util.List;

import org.arakhne.afc.math.geometry.sfc.geometry3d.continuous.Point3f;
import org.arakhne.afc.math.geometry.sfc.geometry3d.continuous.Vector3f;
import org.arakhne.afc.math.sfc.Matrix4f;

/**
 * This class compute an Hermite curve.
 * The main calculation is operate by the method
 * compute(points, tangentes)
 * The simple method compute(points) is not usable
 * because Hermite need tengantes for computation
 * 
 * @author $Author: nvieval$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class HermiteSpline extends AbstractSpline{
    /** the hermite matrix */
    private final Matrix4f hermiteMatrix = new Matrix4f();

    /**
     * constructor.
     */
    public HermiteSpline() {
        this.setHermiteMatrix();
    }

    /**
     * WARNING : don't use this function use the other.
     * @param points the points
     * @return points
     */
    @Override
    public List<Point3f> compute(List<Point3f> points) {
        return points;
    }

    /**
     * compute the Hermite algorithm. The points array and the tengantes array
     * need the same length.
     * @param points the control points
     * @param tangentes the tengantes on points
     * @return the points of the spline
     */
    public List<Point3f> compute(List<Point3f> points, List <Vector3f> tangentes) {
		ArrayList<Point3f> globalPoints = new ArrayList<Point3f>();
        Point3f p1, p2 = new Point3f();
        Vector3f t1, t2 = new Vector3f();
		for (int i = 0; i < (points.size() - 1); i++) {
            p1 = points.get(i);
            if(i > 0) {
                p2 = points.get(i - 1);
            }
            t1 = tangentes.get(i);
            if(i > 0) {
                t2 = tangentes.get(i - 1);
            }
			if (i == 0) {
				ArrayList<Point3f> intermediatePoints = new ArrayList<Point3f>();
				intermediatePoints = this.computeSplineSegment(p1, p1, t1, t1);
				globalPoints.addAll(intermediatePoints);
			} else {
				if (i == (points.size() - 2)) {
					ArrayList<Point3f> intermediatePoints = new ArrayList<Point3f>();
					intermediatePoints = this.computeSplineSegment(p2, p1, t2, t1);
					globalPoints.addAll(intermediatePoints);
				} else {
					ArrayList<Point3f> intermediatePoints = new ArrayList<Point3f>();
					intermediatePoints = this.computeSplineSegment(p2, p1, t2, t1);
					globalPoints.addAll(intermediatePoints);
				}
			}
		}
        return globalPoints;
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
	private ArrayList<Point3f> computeSplineSegment(Point3f pt1, Point3f pt2, Vector3f t1, Vector3f t2) {
		ArrayList<Point3f> newPoints = new ArrayList<Point3f>();
		double segmentLength         = pt1.getDistance(pt2);
		double nbIntermediatePoints  = segmentLength / this.getPrecision();

		for (double j = 0D; j < 1D; j += 1 / nbIntermediatePoints) {
			newPoints.add(this.computeIntermediatePoint(j, pt1, pt2, t1, t2));
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
	private Point3f computeIntermediatePoint(double t, Point3f pt1, Point3f pt2, Vector3f ta1, Vector3f ta2) {
		// math for point
		// Q(t) = (x(t), y(t) ) = T.C = T.M.G
		// T : t³ + t² + t + 1
		// M : base matrix
		// G : geometric matrix
		double xValue = 0D, yValue = 0D, zValue = 0D;
        double t3 = Math.pow(t, 3);
        double t2 = Math.pow(t, 2);
        
		Matrix4f pointsMatrix = new Matrix4f(this.setGeometricMatrix(pt1, pt2, ta1, ta2)); // generate the geometric matrix

		Matrix4f inter = new Matrix4f(); // generate the intermediate matrix
		inter.mul(this.hermiteMatrix, pointsMatrix);

		xValue = (inter.getM00() * t3) + (inter.getM10() * t2) + (inter.getM20() * t) + inter.getM30();
		yValue = (inter.getM01() * t3) + (inter.getM11() * t2) + (inter.getM21() * t) + inter.getM31();
		zValue = (inter.getM02() * t3) + (inter.getM12() * t2) + (inter.getM22() * t) + inter.getM32();

		return new Point3f(xValue, yValue, zValue);
	}

    /**
	 * Set the Hermite matrix for polyspline.<br>
     * | 2 -2  1  1 |<br>
     * |-3  3 -2 -1 |<br>
     * | 0  0  1  0 |<br>
     * | 1  0  0  0 |
	 */
	private void setHermiteMatrix() {

		this.hermiteMatrix.setM00(2f);
		this.hermiteMatrix.setM01(-2f);
		this.hermiteMatrix.setM02(1f);
		this.hermiteMatrix.setM03(1f);

		this.hermiteMatrix.setM10(-3f);
		this.hermiteMatrix.setM11(3f);
		this.hermiteMatrix.setM12(-2f);
		this.hermiteMatrix.setM13(-1f);

		this.hermiteMatrix.setM20(0f);
		this.hermiteMatrix.setM21(0f);
		this.hermiteMatrix.setM22(1f);
		this.hermiteMatrix.setM23(0f);

		this.hermiteMatrix.setM30(1f);
		this.hermiteMatrix.setM31(0f);
		this.hermiteMatrix.setM32(0f);
		this.hermiteMatrix.setM33(0f);
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
	private Matrix4f setGeometricMatrix(Point3f pt1, Point3f pt2, Vector3f t1, Vector3f t2) {

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
