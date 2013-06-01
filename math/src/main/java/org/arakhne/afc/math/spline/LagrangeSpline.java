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
package org.arakhne.afc.math.spline;

import java.util.ArrayList;
import java.util.List;

import org.arakhne.afc.math.continous.object3d.Point3f;

/**
 * This class implements the Lagrange algorithm.
 * This Lagrange interpolation is based on the Aitken algorithm:
 * <code>Pij = Pij-1 (ti+j - t) / (ti+j - ti) + Pi+1j-1 (t - ti) / (ti+j - ti) ,
 * j =[1, n]     i = [0, n-j]</code> 
 *
 * @author $Author: sgalland$
 * @author $Author: nvieval$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class LagrangeSpline extends AbstractSpline{
	
    @Override
    public List<Point3f> compute(List<Point3f> points){
        int N = points.size(); // Knots count
        
        int totalSize = Math.round(points.size()/getPrecision());

        List<Point3f> res = new ArrayList<Point3f>(totalSize);
        
        float[] ti = new float[N];
        
        for (int i=0; i<N; i++)
            ti[i] = (float)i / (N-1);
        
        float t, step, P;
        Point3f Pi, Pj;
        
        t = 0.f;
        step = 1.f/(totalSize-1);
        
        for (int k = 0; k < totalSize; k++){        //  basis functions calculation
        	Pi = new Point3f();
        	
        	for (int j = 0; j < N; j++){
        		Pj = points.get(j);
        		P = 1;
        		for (int i = 0; i < N; i++)
        			if (i != j) P = P*(t-ti[i])/(ti[j] - ti[i]);

        		Pi.setX(Pi.getX() + (Pj.getX() * P));
        		Pi.setY(Pi.getY() + (Pj.getY() * P));
        		Pi.setZ(Pi.getZ() + (Pj.getZ() * P));
        	}
        	
        	res.add(Pi);
        	
        	t += step;
        }
        
        assert(res.size()==totalSize);
        
        return res;
    }

}
