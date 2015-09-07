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
package org.arakhne.afc.math.geometry.d3.continuous.spline;

import java.io.Serializable;

import org.arakhne.afc.math.geometry.d3.Point3D;
import org.arakhne.afc.math.geometry.d3.continuous.Point3f;

/**
 * This class is used for implements a spline.
 * 
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public interface Spline extends Serializable, Cloneable, Iterable<Point3f> {

    /** Replies the control points of the spline.
     * 
     * @return the control points.
     */
    public Iterable<Point3D> getControlPoints();
    
    /** Replies the number of control points of the spline.
     * 
     * @return the number of control points.
     */
    public int getControlPointCount();

    /**
     * Set the factor that is used for discretizing the spline.
     * The semantic of this factor depends on spline implementation.
     *
     * The discretization factor is in [0;1] since this value is 
     * used for moving a t value from 0 to 1 for computing the spline's points.
     *
     * @return the discretization factor.
     */
    public double getDiscretizationFactor();
    
}
