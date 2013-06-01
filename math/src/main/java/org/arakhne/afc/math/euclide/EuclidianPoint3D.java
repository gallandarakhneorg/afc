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
import org.arakhne.afc.math.continous.object2d.Tuple2f;
import org.arakhne.afc.math.continous.object3d.Point3f;
import org.arakhne.afc.math.continous.object3d.Tuple3f;


/**
 * This class represents an euclidian 3D point.
 * 
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class EuclidianPoint3D extends Point3f implements EuclidianPoint {

	private static final long serialVersionUID = -2979048906116339391L;

	/**
     * Constructs and initializes a Point3f from the specified xyz coordinates.
     * 
     * @param x the x coordinate
     * @param y the y coordinate
     * @param z the z coordinate
     */
    public EuclidianPoint3D(float x, float y, float z) {
        super(x,y,z);
    }


    /**
     * Constructs and initializes a Point3f from the array of length 3.
     * 
     * @param p the array of length 3 containing xyz in order
     */
    public EuclidianPoint3D(float[] p) {
       super(p);
    }


    /**
     * Constructs and initializes a Point3f from the specified Point3f.
     * 
     * @param p1 the Point3f containing the initialization x y z data
     */
    public EuclidianPoint3D(Point3f p1) {
         super(p1);
    }


    /** 
     * Constructs and initializes a Point3f from the specified Tuple3f.
     *  
     * @param t1 the Tuple3f containing the initialization x y z data 
     */  
    public EuclidianPoint3D(Tuple3f<?> t1) { 
       super(t1); 
    }
 
 
    /**
     * Constructs and initializes a Point3f to (0,0,0).
     */
    public EuclidianPoint3D() {
       //
    }

    @Override
	public int compareCoordinateTo(int coordinateIndex, float value) {
		switch(coordinateIndex) {
		case 0:
			return Double.compare(this.x, value);
		case 1:
			return Double.compare(this.y, value);
		case 2:
			return Double.compare(this.z, value);
		default:
			throw new IndexOutOfBoundsException(coordinateIndex+">=3"); //$NON-NLS-1$
		}
	}

	@Override
	public int compareCoordinateToEpsilon(int coordinateIndex, float value) {
		switch(coordinateIndex) {
		case 0:
			return MathUtil.epsilonCompareToDistance(this.x, value);
		case 1:
			return MathUtil.epsilonCompareToDistance(this.y, value);
		case 2:
			return MathUtil.epsilonCompareToDistance(this.z, value);
		default:
			throw new IndexOutOfBoundsException(coordinateIndex+">=3"); //$NON-NLS-1$
		}
	}

	@Override
	public PseudoHamelDimension getMathematicalDimension() {
		return PseudoHamelDimension.DIMENSION_3D;
	}

	@Override
	public float getCoordinate(int coordinateIndex) {
		switch(coordinateIndex) {
		case 0:
			return this.x;
		case 1:
			return this.y;
		case 2:
			return this.z;
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
		case 2:
			this.z = value;
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
		tuple.setZ(this.z);
	}

}
