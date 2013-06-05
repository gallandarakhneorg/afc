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
package org.arakhne.afc.math.object;

import org.arakhne.afc.math.MathUtil;
import org.arakhne.afc.math.PseudoHamelDimension;
import org.arakhne.afc.math.continous.object2d.Tuple2f;
import org.arakhne.afc.math.continous.object3d.Tuple3f;
import org.arakhne.afc.math.continous.object3d.Vector3f;
import org.arakhne.afc.math.continous.object4d.AxisAngle4f;
import org.arakhne.afc.math.euclide.EuclidianDirection;

/**
 * This class represents a euclidian 3D direction.
 * 
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class Direction3D extends AxisAngle4f implements EuclidianDirection {

	private static final long serialVersionUID = 266970004083275293L;

	/**
     * Constructs and initializes a Vector3f from the specified xy coordinates.
     * @param x the x coordinate
     * @param y the y coordinate
     * @param z the z coordinate
     * @param angle is the rotation angle around the axis.
     */
    public Direction3D(float x, float y, float z, float angle) {
         super(x,y,z,angle);
    }


    /**
     * Constructs and initializes a Vector3f from the specified array.
     * @param p the array of length 3 containing xyz in order
     */
    public Direction3D(float[] p) {
         super(p);
    }


    /**
     * Constructs and initializes a Vector3f from the specified Point2d.
     * @param p1 the Point2d containing the initialization x y data
     */
    public Direction3D(AxisAngle4f p1) {
        super(p1);
    }


    /**
     * Constructs and initializes a Vector3f from the specified Tuple2d.
     * @param t1 the Tuple2d containing the initialization x y data
     * @param angle is the rotation angle around the axis.
     */ 
    public Direction3D(Vector3f t1, float angle) {
       super(t1, angle);
    }


    /**
     * Constructs and initializes a AxisAngle to (0,0).
     */
    public Direction3D() {
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
		case 3:
			return Double.compare(this.angle, value);
		default:
			throw new IndexOutOfBoundsException(coordinateIndex+">=4"); //$NON-NLS-1$
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
		case 3:
			return MathUtil.epsilonCompareToDistance(this.angle, value);
		default:
			throw new IndexOutOfBoundsException(coordinateIndex+">=4"); //$NON-NLS-1$
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
		case 3:
			return this.angle;
		default:
			return Float.NaN;
		}
	}
	
	public void toTuple2d(Tuple2f<?> tuple) {
		tuple.setX(this.x);
		tuple.setY(this.y);
	}

	public void toTuple3d(Tuple3f<?> tuple) {
		tuple.setX(this.x);
		tuple.setY(this.y);
		tuple.setZ(this.z);
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
