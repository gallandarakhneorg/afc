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
package org.arakhne.afc.math.geometry.continuous.object3d.spline;

import java.util.ArrayList;
import java.util.List;

import org.arakhne.afc.math.geometry.continuous.object3d.Point3f;
import org.arakhne.afc.math.matrix.Matrix4f;

/**
 * This class compute the cattmulrom spline.
 * 
 * @author $Author: nvieval$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class CatMullRomSpline extends AbstractSpline{

    /** the catmullrom matrix. */
    private final Matrix4f catMullRomMatrix = new Matrix4f();

	private int discretizeLevel = 100;

    /**
     * constructor.
     */
    public CatMullRomSpline() {
		this(100);
    }

    /**
     * @param discretizeLvl
     */
	public CatMullRomSpline(int discretizeLvl) {
		this.setCatmullRomMatrix();
		setDiscretizationLevel(discretizeLvl);
	}
	
	

    /** Replies the level of discretization of the spline.
     * 
     * @return the level of discretization of the spline.
     */
	public int getDiscretizationLevel() {
		return this.discretizeLevel;
	}
    
    /** Set the level of discretization of the spline.
     * 
     * @param level is the level of discretization of the spline.
     */
	public void setDiscretizationLevel(int level) {
		this.discretizeLevel = level;
		setPrecision(1.f/level);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setPrecision(float precision) {
		super.setPrecision(precision);
		this.discretizeLevel = (int)(1./precision);
	}

	/**
     * compute the spline with differents configuration.
     * 2 points, 3 points, 4 points, ....., N points
     * @param points
     * @return {@inheritDoc}
     */
    @Override
    public List<Point3f> compute(List<Point3f> points) {
		ArrayList<Point3f> globalPoints = new ArrayList<Point3f>();
        ArrayList<Point3f> intermediatePoints = new ArrayList<Point3f>();
		for (int i = 0; i < (points.size() - 1); i++) {
			if (i == 0) {
				if (points.size() > 2) {                                        // fist part
                    // use the first point two time for catch spline on this point
					intermediatePoints = this.splineEval(points.get(i), points.get(i), points.get(i + 1), points.get(i + 2));
				} else {
                    // just 2 points ! a line
					intermediatePoints = this.splineEval(points.get(i), points.get(i), points.get(i + 1), points.get(i + 1));
				}
			} else {
				if (i == (points.size() - 2)) {
					intermediatePoints = this.splineEval(points.get(i - 1), points.get(i), points.get(i + 1), points.get(i + 1));
				} else {
					intermediatePoints = this.splineEval(points.get(i - 1), points.get(i), points.get(i + 1), points.get(i + 2));

				}
			}
            globalPoints.addAll(intermediatePoints);
            intermediatePoints.clear();
		}
        return globalPoints;
    }

    /**
	 * Calculate the spline between point 2 and point 3.<br>
	 * the point 1 and the point 4 are used for the catmullrom method.
	 *
	 * @param pt1
	 *            the first point
	 * @param pt2
	 *            the second point
	 * @param pt3
	 *            the third point
	 * @param pt4
	 *            the fourth point
	 * @return the array list of points
	 */
	public ArrayList<Point3f> splineEval(Point3f pt1, Point3f pt2, Point3f pt3, Point3f pt4) {
		ArrayList<Point3f> newPoints = new ArrayList<Point3f>();

		float step = 1.f / this.discretizeLevel;
		
		for (float j = 0f; j < 1f; j += step) {
			newPoints.add(this.computeIntermediatePoint(j, pt1, pt2, pt3, pt4));
		}
		
		return newPoints;
	}

	/**
	 * Calculate the intermediate point with t and the geometric matrix.
	 * xi(t) = 1/6[(1-t)^3xi + (3t^3-6t^2+4)xi+1 + (-3t^3+3t^2+3t+1)xi+2 + t^3xi+3]
     * yi(t) = 1/6[(1-t)^3yi + (3t^3-6t^2+4)yi+1 + (-3t^3+3t^2+3t+1)yi+2 + t^3yi+3]
     *
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
	private Point3f computeIntermediatePoint(float t, Point3f pt1, Point3f pt2, Point3f pt3, Point3f pt4) {
		// math for point
		// Q(t) = (x(t), y(t) ) = T.C = T.M.G
		// T : t³ + t² + t + 1
		// M : base matrix
		// G : geometric matrix
		float xValue = 0f, yValue = 0f, zValue = 0f;
        float t3 = (float) Math.pow(t, 3);
        float t2 = (float) Math.pow(t, 2);

		Matrix4f pointsMatrix = new Matrix4f(this.setGeometricMatrix(pt1, pt2, pt3, pt4)); // generate the geometric matrix

		Matrix4f inter = new Matrix4f(); // generate the intermediate matrix
		inter.mul(this.catMullRomMatrix, pointsMatrix);

		xValue = (inter.getM00() * t3) + (inter.getM10() * t2) + (inter.getM20() * t) + inter.getM30();
		yValue = (inter.getM01() * t3) + (inter.getM11() * t2) + (inter.getM21() * t) + inter.getM31();
		zValue = (inter.getM02() * t3) + (inter.getM12() * t2) + (inter.getM22() * t) + inter.getM32();

		return new Point3f(xValue, yValue, zValue);
	}

    /**
	 * Set the catmullrom matrix for polyspline.<br>
     * |-1  3 -3  1 |<br>
     * | 1 -5  4 -1 | * 1/2<br>
     * |-1  0  1  0 |<br>
     * | 0  1  0  0 |
	 */
	private void setCatmullRomMatrix() {

		this.catMullRomMatrix.setM00(-0.5f);
		this.catMullRomMatrix.setM01(1.5f);
		this.catMullRomMatrix.setM02(-1.5f);
		this.catMullRomMatrix.setM03(0.5f);

		this.catMullRomMatrix.setM10(1f);
		this.catMullRomMatrix.setM11(-2.5f);
		this.catMullRomMatrix.setM12(2f);
		this.catMullRomMatrix.setM13(-0.5f);

		this.catMullRomMatrix.setM20(-0.5f);
		this.catMullRomMatrix.setM21(0);
		this.catMullRomMatrix.setM22(0.5f);
		this.catMullRomMatrix.setM23(0);

		this.catMullRomMatrix.setM30(0);
		this.catMullRomMatrix.setM31(1f);
		this.catMullRomMatrix.setM32(0);
		this.catMullRomMatrix.setM33(0);
	}

	/**
	 * Set matrix of geometric contsraint
	 *
	 * @param pt1
	 *            the first point to define the matrix
	 * @param pt2
	 *            the second point to define the matrix
	 * @param pt3
	 *            the third point to define the matrix
	 * @param pt4
	 *            the fourth point to define the matrix
	 * @return the geometric matrix
	 */
	private Matrix4f setGeometricMatrix(Point3f pt1, Point3f pt2, Point3f pt3, Point3f pt4) {

		Matrix4f pointsMatrix = new Matrix4f();

		pointsMatrix.setM00(pt1.getX());
		pointsMatrix.setM01(pt1.getY());
		pointsMatrix.setM02(pt1.getZ());
		pointsMatrix.setM03(0);
		pointsMatrix.setM10(pt2.getX());
		pointsMatrix.setM11(pt2.getY());
		pointsMatrix.setM12(pt2.getZ());
		pointsMatrix.setM13(0);
		pointsMatrix.setM20(pt3.getX());
		pointsMatrix.setM21(pt3.getY());
		pointsMatrix.setM22(pt3.getZ());
		pointsMatrix.setM23(0);
		pointsMatrix.setM30(pt4.getX());
		pointsMatrix.setM31(pt4.getY());
		pointsMatrix.setM32(pt4.getZ());
		pointsMatrix.setM33(0);

		return pointsMatrix;
	}
}
