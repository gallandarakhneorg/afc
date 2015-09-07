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
import java.util.Arrays;
import java.util.List;

import org.arakhne.afc.math.geometry.d3.Point3D;

/**
 * This class implements the Cox De Boor algorithm.
 * 
 * @author $Author: nvieval$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class CoxDeBourSpline extends AbstractSpline<CoxDeBourSpline> {

	private static final long serialVersionUID = -7378696524032654553L;

	/** the degree of spline. */
    private int k = 3;

    private final List<Float> weights = new ArrayList<>();
	
	/**
	 */
	public CoxDeBourSpline() {
		//
	}

	/**
	 * @param degree degree of the spline.
	 */
	public CoxDeBourSpline(int degree) {
		this.k = degree;
	}

	/** Replies the degree of this spline.
	 *
	 * @return the degree.
	 */
	public int getDegree() {
		return this.k;
	}
	
    /**
     * Change the degree of curve.
     *
     * @param k the spline degree
     */
    public void setDegree(int k) {
        this.k = k;
        reset();
    }

    /**
	 * Change the control points and recompute the spline's points.
	 *
	 * @param controlPoints the control points.
	 */
	public void setControlPoints(Point3D... controlPoints) {
		setControlPoints(Arrays.asList(controlPoints));
	}

	/**
	 * Change the control points and recompute the spline's points.
	 *
	 * @param controlPoints the control points.
	 */
	public void setControlPoints(Iterable<? extends Point3D> controlPoints) {
		this.controlPoints.clear();
		for (Point3D p : controlPoints) {
			this.controlPoints.add(p.clone());
		}
		this.weights.clear();
        int size = this.controlPoints.size() + this.k;
		if (this.weights.isEmpty()) {
	        for(int i = 0; i <= size; ++i) {
	            this.weights.add(new Float(i));
	        }
		}
		reset();
	}

	/**
	 * Change the control points and recompute the spline's points.
	 *
	 * @param controlPoints the control points.
	 * @param weights the weights of the control points.
	 */
	public void setControlPoints(Iterable<? extends Point3D> controlPoints, Iterable<Float> weights) {
		this.controlPoints.clear();
		this.weights.clear();
		for (Point3D p : controlPoints) {
			this.controlPoints.add(p.clone());
		}
		for (Float w : weights) {
			this.weights.add(new Float(w));
		}
		reset();
	}

	@Override
	protected void ensurePoints() {
		if (this.points == null) {
	        double temp = 0;
	        /* In a default case the weight are 0,1,2,3,4,5,...
	         * But in a custom case the user send for example : 4, 1, 1, 4
	         * you need to complete with the same length that points and additionate
	         * values extends => for 6 points 0,4,5,6,10,10
	         */
	        int add = ((this.controlPoints.size() - this.weights.size()) + this.k) / 2;
	        List<Float> weightT = new ArrayList<>();
	        for(int i = 0; i < add; i++) {
	            weightT.add(new Float(0));
	        }
	        for(int i = 0; i < this.weights.size(); ++i) {
	            temp = temp + this.weights.get(i).floatValue();
	            weightT.add(new Float(temp));
	        }
	        while(weightT.size() < (this.controlPoints.size() + this.k)) {
	            weightT.add(new Float(temp));
	        }
	        // end of weight computation
	        
	        List<Point3D> globalPoints = new ArrayList<>();
	        double t;
			for(int r = this.k; r < this.controlPoints.size(); ++r) {
				for(t = weightT.get(r); t <= weightT.get(r+1); t += getDiscretizationFactor()) {
	                globalPoints.add(computeCoxDeBour(r, t));
				}
			}

			this.points = globalPoints;
		}
	}
    /**
     * start the cox de boor algorithm.
     * @param r r value of the algorithm
     * @param t t value
     * @param points the control points
     * @param weight the weight
     * @return
     */
    private Point3D computeCoxDeBour(int r, double t) {
        Point3D[][] Pt;

        Pt = new Point3D[this.k+1][r+1];
        for(int i = r-this.k; i<=r; i++) {
            Pt[0][i] = this.controlPoints.get(i);
        }

        for(int j = 1; j <= this.k; j++) {
            for(int i = r-this.k+j; i <= r; i++) {
                Pt[j][i] = computePointCalcul(i, j, t, Pt);
            }
        }
        return Pt[this.k][r];
    }

    /**
     * compute point.
     * @param i
     * @param j
     * @param t
     * @param Pt
     * @param weight
     * @return
     */
    private Point3D computePointCalcul(int i, int j, double t, Point3D[][] Pt){
        double a = t - this.weights.get(i).intValue();
        double b = this.weights.get(i - j + this.k + 1).intValue() - t;
        double c = this.weights.get(i - j + this.k + 1).intValue() - this.weights.get(i).intValue();
        
        Point3D res = Pt[0][0].clone();
        res.set(
        		((Pt[j-1][i].getX() *a) + (Pt[j-1][i-1].getX() * b)) / c,
        		((Pt[j-1][i].getY() *a) + (Pt[j-1][i-1].getY() * b)) / c,
        		((Pt[j-1][i].getZ() *a) + (Pt[j-1][i-1].getZ() * b)) / c);
        return res;
    }

}
