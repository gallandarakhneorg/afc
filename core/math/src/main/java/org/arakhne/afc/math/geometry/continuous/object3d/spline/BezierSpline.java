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
 * this class calculate a bezier curve.
 * 
 * @author $Author: nvieval$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class BezierSpline extends AbstractSpline{
    
    /** number of points in the result spline. */
    private final int n;

    /**
     * default constructor;
     */
    public BezierSpline() {
        super();
        this.n = (int) (1 / this.getPrecision());
    }
    
    /**
     * constructor.
     * @param nbPoints the number of out points
     */
    public BezierSpline(int nbPoints) {
        this.n = nbPoints;
    }

    /**
     * compute the points of bezier curve from control points.
     * @param points the control points
     * @return the spline points
     */
    @Override
    public List<Point3f> compute(List<Point3f> points) {
		ArrayList<Point3f> globalPoints = new ArrayList<Point3f>();
        for(int i=0;i<this.n;i++) {
            float t=(float)i/this.n;
            globalPoints.add(bezierPoint(t, points.size(), points));
        }
        return globalPoints;
    }

    /**
     * compute a bezier point with the Casteljeau algorithm
     * @param t the t
     * @param nbControlPoint the numer of control points
     * @param points the controls points
     * @return the point
     */
    private Point3f bezierPoint(float t, int nbControlPoint, List<Point3f> points) {
        float xbezier, ybezier, zbezier;
        float[] xjp=new float[nbControlPoint];
        float[] yjp=new float[nbControlPoint];
        float[] zjp=new float[nbControlPoint];
        
        for(int i=0;i<nbControlPoint;i++) {
            Point3f p = points.get(i);
            xjp[i] = p.getX();
            yjp[i] = p.getY();
            zjp[i] = p.getZ();
        }

        for(int j=1;j<=(nbControlPoint-1);j++) {
            if((nbControlPoint-j)>=1) {
                float[] xj=new float[nbControlPoint-j];
                float[] yj=new float[nbControlPoint-j];
                float[] zj=new float[nbControlPoint-j];

                for(int i=0;i<(nbControlPoint-j);i++) {
                    xj[i]=interpol(t,xjp[i],xjp[i+1]);
                    yj[i]=interpol(t,yjp[i],yjp[i+1]);
                    zj[i]=interpol(t,zjp[i],zjp[i+1]);
                }
                for(int i=0;i<(nbControlPoint-j);i++) {
                    xjp[i]=xj[i];
                    yjp[i]=yj[i];
                    zjp[i]=zj[i];
                }
            }
        }
        xbezier=xjp[0];
        ybezier=yjp[0];
        zbezier=zjp[0];
        
        return new Point3f(xbezier, ybezier, zbezier);
    }

    /**
     * interpole position between a and b with t
     * @param t t
     * @param a a
     * @param b b
     * @return value
     */
    public float interpol(float t, float a, float b) {
        return a*(1-t)+b*t;
    }
}
