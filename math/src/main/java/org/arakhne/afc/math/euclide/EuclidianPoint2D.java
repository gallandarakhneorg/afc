/* 
 * $Id$
 * 
 * Copyright (C) 2010-2013 Stephane GALLAND.
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
package org.arakhne.afc.math.euclide;

import org.arakhne.afc.math.MathUtil;
import org.arakhne.afc.math.PseudoHamelDimension;
import org.arakhne.afc.math.continous.object2d.Point2f;
import org.arakhne.afc.math.continous.object2d.Tuple2f;
import org.arakhne.afc.math.continous.object3d.Tuple3f;

/**
 * This class represents a euclidian 2D point.
 * 
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class EuclidianPoint2D extends Point2f implements EuclidianPoint {

	private static final long serialVersionUID = 411650141727092327L;

	/**
     * Constructs and initializes a Point2f from the specified xy coordinates.
     * @param x the x coordinate
     * @param y the y coordinate
     */
    public EuclidianPoint2D(float x, float y) {
         super(x,y);
    }


    /**
     * Constructs and initializes a Point2f from the specified array.
     * @param p the array of length 2 containing xy in order
     */
    public EuclidianPoint2D(float[] p) {
         super(p);
    }


    /**
     * Constructs and initializes a Point2f from the specified Point2f.
     * @param p1 the Point2f containing the initialization x y data
     */
    public EuclidianPoint2D(Point2f p1) {
        super(p1);
    }


    /**
     * Constructs and initializes a Point2f from the specified Tuple2f.
     * @param t1 the Tuple2f containing the initialization x y data
     */ 
    public EuclidianPoint2D(Tuple2f<?> t1) {
       super(t1);
    }


    /**
     * Constructs and initializes a Point2f to (0,0).
     */
    public EuclidianPoint2D() {
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
	
	/** {@inheritDoc}
	 */
	@Override
	public void setCoordinate(int coordinateIndex, float value) {
		switch(coordinateIndex) {
		case 0:
			this.x = value;
			break;
		case 1:
			this.y = value;
			break;
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
