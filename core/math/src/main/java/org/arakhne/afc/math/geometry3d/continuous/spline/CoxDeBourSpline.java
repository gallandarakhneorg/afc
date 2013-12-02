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
package org.arakhne.afc.math.geometry3d.continuous.spline;

import java.util.ArrayList;
import java.util.List;

import org.arakhne.afc.math.geometry3d.continuous.Point3f;

/**
 * This class implements the Cox De Boor algorithm.
 * 
 * @author $Author: nvieval$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class CoxDeBourSpline extends AbstractSpline{

    /** the degree of spline. */
    private int k;

    /**
     * default constructor.
     */
    public CoxDeBourSpline() {
        this(3);
    }

    /**
     * constructor.
     * @param degree degree of spline
     */
    public CoxDeBourSpline(int degree) {
        this.k = degree;
    }

    /**
     * default computation. The weight of every points are
     * set by program at start.
     * @param points the control  points
     * @return the spline points
     */
    @Override
    public List<Point3f> compute(List<Point3f> points) {
        int size = points.size() + this.k;
        ArrayList<Float> weight = new ArrayList<Float>();                     // generate weight
        for(int i=0; i<= size; i++) {
            weight.add(new Float(i));                                          // just a iteratif number
        }
        
		float t;
        ArrayList<Point3f> globalPoints = new ArrayList<Point3f>();
        
		for(int r = this.k; r < points.size(); r++) {
			for(t=weight.get(r); t<=weight.get(r+1); t+= this.getPrecision()) {
                globalPoints.add(computeCoxDeBoor(r, t, points, weight));       // compute cox de boor
			}
		}
        return globalPoints;
    }

    /**
     * the custom computation. You need to define a weight for every
     * control point.
     * @param points the control points
     * @param weight the weight 
     * @return the points of the spline
     */
    public List<Point3f> compute(List<Point3f> points, List <Float> weight) {
        float temp = 0;
        /* In a default case the weight are 0,1,2,3,4,5,...
         * But in a custom case the user send for example : 4, 1, 1, 4
         * you need to complete with the same length that points and additionate
         * values extends => for 6 points 0,4,5,6,10,10
         */
        int add = ((points.size() - weight.size()) + this.k) / 2;
        ArrayList<Float> weightT = new ArrayList<Float>();
        for(int i = 0; i < add; i++) {
            weightT.add(new Float(0));
        }
        for(int i = 0; i < weight.size(); i++) {
            temp = temp + weight.get(i).floatValue();
            weightT.add(new Float(temp));
        }
        while(weightT.size() < (points.size() + this.k)) {
            weightT.add(new Float(temp));
        }
        // end of weight computation
        
        ArrayList<Point3f> globalPoints = new ArrayList<Point3f>();
        float t;
		for(int r = this.k; r < points.size(); r++) {
			for(t=weightT.get(r); t<=weightT.get(r+1); t+= this.getPrecision()) {
                globalPoints.add(computeCoxDeBoor(r, t , points, weightT));
			}
		}
        return globalPoints;
    }

    /**
     * start the cox de boor algorithm.
     * @param r r value of the algorithm
     * @param t t value
     * @param points the control points
     * @param weight the weight
     * @return
     */
    private Point3f computeCoxDeBoor(int r, float t, List<Point3f> points, List<Float> weight) {
        Point3f[][] Pt;

        Pt = new Point3f[this.k+1][r+1];
        for(int i = r-this.k; i<=r; i++) {
            Pt[0][i] = points.get(i);
        }

        for(int j = 1; j <= this.k; j++) {
            for(int i = r-this.k+j; i <= r; i++) {
                Pt[j][i] = this.computePointCalcul(i, j, t, Pt, weight);
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
    private Point3f computePointCalcul(int i, int j, float t, Point3f[][] Pt, List<Float> weight){
        Point3f res = new Point3f();
        float a = t - weight.get(i).intValue();
        float b = weight.get(i - j + this.k + 1).intValue() - t;
        float c = weight.get(i - j + this.k + 1).intValue() - weight.get(i).intValue();
        
        res.setX(((Pt[j-1][i].getX() *a) + (Pt[j-1][i-1].getX() * b)) / c);
        res.setY(((Pt[j-1][i].getY() *a) + (Pt[j-1][i-1].getY() * b)) / c);
        res.setZ(((Pt[j-1][i].getZ() *a) + (Pt[j-1][i-1].getZ() * b)) / c);

        return res;
    }

    /**
     * set the degree of curve.
     * @param k the nex degree
     */
    public void setDegree(int k) {
        this.k = k;
    }

    /**
     * get the degree.
     * @return degree
     */
    public int getDegree() {
        return this.k;
    }
}
