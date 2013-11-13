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

/**
 * this class calculate a cubic bezier spline.
 * 
 * @author $Author: nvieval$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class CubicBezierSpline extends AbstractSpline{

    /**
     * copnstructor.
     */
    public CubicBezierSpline() {
    	// default & empty
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
     * evaluation of spline between 4 points
     * Pt = p1 * (1-t)³ + p2 * 3t(1-t)² p3 * 3t²(1-t) p4* t³
     *
     * @param p1 the first point
     * @param p2 the second point
     * @param p3 the third point
     * @param p4 the fourth point
     * @return the points of spline between this points
     */
    private ArrayList<Point3f> splineEval( Point3f p1, Point3f p2, Point3f p3, Point3f p4) {
        ArrayList <Point3f> res = new ArrayList<Point3f>();
        double xp, yp, zp;
        double t3, t2, t1, t0;
        for (double t=0 ; t<=1 ; t+=this.getPrecision()){
            t3 = (1-t)*(1-t)*(1-t);
            t2 = 3*t*(1-t)*(1-t);
            t1 = 3*t*(1-t)*t;
            t0 = t*t*t;

            xp = (t3*p1.getX() + t2*p2.getX() + t1*p3.getX() + t0*p4.getX());
            yp = (t3*p1.getY() + t2*p2.getY() + t1*p3.getY() + t0*p4.getY());
            zp = (t3*p1.getZ() + t2*p2.getZ() + t1*p3.getZ() + t0*p4.getZ());
            res.add(new Point3f(xp, yp, zp));
        }
        return res;
    }

}
