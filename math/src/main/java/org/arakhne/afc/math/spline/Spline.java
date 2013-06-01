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

import java.util.List;

import org.arakhne.afc.math.continous.object3d.Point3f;

/**
 * This class is used for implements a new type of spline.
 * All the methods in this class are the only visibles
 * except some Spline some particylar spline.
 * 
 * @author $Author: nvieval$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public interface Spline {

    /**
     * the computation method. add control points to compute
     * and the computation method return the result spline.
     * @param points the control points
     * @return the result points
     */
    public List<Point3f> compute(List<Point3f> points);

    /**
     * set the precision factor.
     * @param p the factor
     */
    public void setPrecision(float p);

    /**
     * get the precision factor.
     * @return the factor
     */
    public float getPrecision();
    
}
