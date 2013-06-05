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
package org.arakhne.afc.math.object;

import org.arakhne.afc.math.MathUtil;
import org.arakhne.afc.math.PseudoHamelDimension;
import org.arakhne.afc.math.continous.object2d.Tuple2f;
import org.arakhne.afc.math.continous.object2d.Vector2f;
import org.arakhne.afc.math.continous.object3d.Tuple3f;
import org.arakhne.afc.math.euclide.EuclidianDirection;

/**
 * This class represents a euclidian 2D direction.
 * 
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class Direction2D extends Vector2f implements EuclidianDirection {

	private static final long serialVersionUID = 5399832893488781090L;

	/**
     * Constructs and initializes a Vector2f from the specified xy coordinates.
     * @param x the x coordinate
     * @param y the y coordinate
     */
    public Direction2D(float x, float y) {
         super(x,y);
    }


    /**
     * Constructs and initializes a Vector2f from the specified array.
     * @param p the array of length 2 containing xy in order
     */
    public Direction2D(float[] p) {
         super(p);
    }


    /**
     * Constructs and initializes a Vector2f from the specified Point2f.
     * @param p1 the Point2f containing the initialization x y data
     */
    public Direction2D(Vector2f p1) {
        super(p1);
    }


    /**
     * Constructs and initializes a Vector2f from the specified Tuple2f.
     * @param t1 the Tuple2f containing the initialization x y data
     */ 
    public Direction2D(Tuple2f<?> t1) {
       super(t1);
    }


    /**
     * Constructs and initializes a Vector2f to (0,0).
     */
    public Direction2D() {
       //
    }

    @Override
	public int compareCoordinateTo(int coordinateIndex, float value) {
		switch(coordinateIndex) {
		case 0:
			return Float.compare(this.x, value);
		case 1:
			return Float.compare(this.y, value);
		default:
			throw new IndexOutOfBoundsException(coordinateIndex+">=2"); //$NON-NLS-1$
		}
	}

	@Override
	public int compareCoordinateToEpsilon(int coordinateIndex, float value) {
		switch(coordinateIndex) {
		case 0:
			return MathUtil.epsilonCompareToDistance(this.x, value);
		case 1:
			return MathUtil.epsilonCompareToDistance(this.y, value);
		default:
			throw new IndexOutOfBoundsException(coordinateIndex+">=2"); //$NON-NLS-1$
		}
	}

	@Override
	public PseudoHamelDimension getMathematicalDimension() {
		return PseudoHamelDimension.DIMENSION_2D;
	}

	@Override
	public float getCoordinate(int coordinateIndex) {
		switch(coordinateIndex) {
		case 0:
			return this.x;
		case 1:
			return this.y;
		default:
			return Float.NaN;
		}
	}
	
	@Override
	public void toTuple2f(Tuple2f<?> tuple) {
		tuple.setX(this.x);
		tuple.setY(this.y);
	}

	@Override
	public void toTuple3f(Tuple3f<?> tuple) {
		tuple.setX(this.x);
		tuple.setY(this.y);
		tuple.setZ(Float.NaN);
	}

}
