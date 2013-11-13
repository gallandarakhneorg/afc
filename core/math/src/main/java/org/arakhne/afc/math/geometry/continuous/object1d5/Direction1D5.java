/* 
 * $Id$
 * 
 * Copyright (C) 2010-2013 Christophe BOHRHAUER.
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
package org.arakhne.afc.math.geometry.continuous.object1d5;

import org.arakhne.afc.math.MathUtil;
import org.arakhne.afc.math.PseudoHamelDimension;
import org.arakhne.afc.math.geometry.continuous.euclide.EuclidianDirection;
import org.arakhne.afc.math.geometry.continuous.object2d.Tuple2f;
import org.arakhne.afc.math.geometry.continuous.object2d.Vector2f;
import org.arakhne.afc.math.geometry.continuous.object3d.Tuple3f;

/**
 * This class represents a euclidian 1.5D direction.
 * 
 * @author $Author: cbohrhauer$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class Direction1D5 extends Vector2f{

	private static final long serialVersionUID = 5399832893488781090L;

	/**
     * Constructs and initializes a Vector2f from the specified xy coordinates.
     * @param x the x coordinate
     * @param s the shift coordinate
     */
    public Direction1D5(float x, float s) {
         super(x,s);
    }


    /**
     * Constructs and initializes a Vector2f from the specified array.
     * @param p the array of length 2 containing xy in order
     */
    public Direction1D5(float[] p) {
         super(p);
    }


    /**
     * Constructs and initializes a Vector2f from the specified Point2f.
     * @param p1 the Point2f containing the initialization x y data
     */
    public Direction1D5(Vector2f p1) {
        super(p1);
    }


    /**
     * Constructs and initializes a Vector2f from the specified Tuple2f.
     * @param t1 the Tuple2f containing the initialization x y data
     */ 
    public Direction1D5(Tuple2f<?> t1) {
       super(t1);
    }


    /**
     * Constructs and initializes a Vector2f to (0,0).
     */
    public Direction1D5() {
       //
    }
}
