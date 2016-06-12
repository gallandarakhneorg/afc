/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2016 The original authors, and other authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.arakhne.afc.math.geometry.d3;

import java.io.Serializable;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.MathUtil;

/** 3D tuple.
 *
 * @param <RT> is the type of data that can be added or substracted to this tuple.
 * @author $Author: sgalland$
 * @author $Author: tpiotrow$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public interface Tuple3D<RT extends Tuple3D<? super RT>>
	extends Cloneable, Serializable {

	/** Clone this point.
	 *
	 * @return the clone.
	 */
	@Pure
	RT clone();

	/**
	 *  Sets each component of this tuple to its absolute value.
	 */
	default void absolute() {
		setX(Math.abs(getX()));
		setY(Math.abs(getY()));
		setZ(Math.abs(getZ()));
	}

	/**
	 *  Sets each component of the tuple parameter to its absolute
	 *  value and places the modified values into this tuple.
	 *  @param tuple   the source tuple, which will not be modified
	 */

	default void absolute(Tuple3D<?> tuple) {
		assert tuple != null : "Tuple must not be null"; 		//$NON-NLS-1$
		tuple.set(Math.abs(getX()), Math.abs(getY()), Math.abs(getZ()));
	}

	/**
	 * Sets the value of this tuple to the sum of itself and x and y.
	 * @param x x coordinate to add.
	 * @param y y coordinate to add.
	 * @param z z coordinate to add.
	 */
	default void add(int x, int y, int z) {
		set(getX() + x, getY() + y, getZ() + z);
	}

	/**
	 * Sets the value of this tuple to the sum of itself and x and y.
	 * @param x x coordinate to add.
	 * @param y y coordinate to add.
	 * @param z z coordinate to add.
	 */
	default void add(double x, double y, double z) {
		set(getX() + x, getY() + y, getZ() + z);
	}

	/**
	 * Sets the x value of this tuple to the sum of itself and x.
	 * @param x x coordinate to add.
	 */
	default void addX(int x) {
		setX(getX() + x);
	}

	/**
	 * Sets the x value of this tuple to the sum of itself and x.
	 * @param x x coordinate to add.
	 */
	default void addX(double x) {
		setX(getX() + x);
	}

	/**
	 * Sets the y value of this tuple to the sum of itself and y.
	 * @param y y coordinate to add.
	 */
	default void addY(int y) {
		setY(getY() + y);
	}

	/**
	 * Sets the y value of this tuple to the sum of itself and y.
	 * @param y y coordinate to add.
	 */
	default void addY(double y) {
		setY(getY() + y);
	}

	/**
	 * Sets the z value of this tuple to the sum of itself and z.
	 * @param z z coordinate to add.
	 */
	default void addZ(int z) {
		setZ(getZ() + z);
	}

	/**
	 * Sets the z value of this tuple to the sum of itself and z.
	 * @param z z coordinate to add.
	 */
	default void addZ(double z) {
		setZ(getZ() + z);
	}

	/**
	 *  Clamps this tuple to the range [low, high].
	 *  @param min  the lowest value in this tuple after clamping
	 *  @param max  the highest value in this tuple after clamping
	 */
	default void clamp(int min, int max) {
		assert min <= max : "min must be lower or equal to max"; //$NON-NLS-1$
		final double x = MathUtil.clamp(getX(), min, max);
		final double y = MathUtil.clamp(getY(), min, max);
		final double z = MathUtil.clamp(getZ(), min, max);
		set(x, y, z);
	}

	/**
	 *  Clamps the tuple parameter to the range [low, high] and
	 *  places the values into this tuple.
	 *  @param min   the lowest value in the tuple after clamping
	 *  @param max  the highest value in the tuple after clamping
	 *  @param tuple   the source tuple, which will not be modified
	 */
	default void clamp(int min, int max, Tuple3D<?> tuple)  {
	    assert min <= max : "min must be lower or equal to max"; //$NON-NLS-1$
	    assert tuple != null : "Tuple must not be null"; //$NON-NLS-1$
	    final double x = MathUtil.clamp(tuple.getX(), min, max);
	    final double y = MathUtil.clamp(tuple.getY(), min, max);
	    final double z = MathUtil.clamp(tuple.getZ(), min, max);
	    set(x, y, z);
	}

	/**
	 *  Clamps this tuple to the range [low, high].
	 *  @param min  the lowest value in this tuple after clamping
	 *  @param max  the highest value in this tuple after clamping
	 */
	default void clamp(double min, double max) {
		assert min <= max : "min must be lower or equal to max"; //$NON-NLS-1$
		final double x = MathUtil.clamp(getX(), min, max);
		final double y = MathUtil.clamp(getY(), min, max);
		final double z = MathUtil.clamp(getZ(), min, max);
		set(x, y, z);
	}

    /**
     *  Clamps the tuple parameter to the range [low, high] and
     *  places the values into this tuple.
     *  @param min   the lowest value in the tuple after clamping
     *  @param max  the highest value in the tuple after clamping
     *  @param tuple   the source tuple, which will not be modified
     */
    default void clamp(double min, double max, Tuple3D<?> tuple) {
        assert min <= max : "min must be lower or equal to max"; //$NON-NLS-1$
        assert tuple != null : "Tuple must not be null"; //$NON-NLS-1$
        final double x = MathUtil.clamp(tuple.getX(), min, max);
        final double y = MathUtil.clamp(tuple.getY(), min, max);
        final double z = MathUtil.clamp(tuple.getZ(), min, max);
        set(x, y, z);
    }

	/**
	 *  Clamps the minimum value of this tuple to the min parameter.
	 *  @param min   the lowest value in this tuple after clamping
	 */
	default void clampMin(int min) {
		double x = getX();
		double y = getY();
		double z = getZ();
		if (x < min) {
			x = min;
		}
		if (y < min) {
			y = min;
		}
		if (z < min) {
			z = min;
		}
		set(x, y, z);
	}

	/**
	 *  Clamps the minimum value of this tuple to the min parameter.
	 *  @param min   the lowest value in this tuple after clamping
	 */
	default void clampMin(double min) {
		double x = getX();
		double y = getY();
		double z = getZ();
		if (x < min) {
			x = min;
		}
		if (y < min) {
			y = min;
		}
		if (z < min) {
			z = min;
		}
		set(x, y, z);
	}

    /**
     *  Clamps the minimum value of the tuple parameter to the min
     *  parameter and places the values into this tuple.
     *  @param min the lowest value in the tuple after clamping
     *  @param tuple the source tuple, which will not be modified
     */
    default void clampMin(int min, Tuple3D<?> tuple) {
        assert tuple != null : "Tuple must not be null"; //$NON-NLS-1$
        double x = tuple.getX();
        double y = tuple.getY();
        double z = tuple.getY();
        if (x < min) {
            x = min;
        }
        if (y < min) {
            y = min;
        }
        if (z < min) {
            z = min;
        }
        set(x, y, z);
    }

    /**
     *  Clamps the minimum value of the tuple parameter to the min
     *  parameter and places the values into this tuple.
     *  @param min   the lowest value in the tuple after clamping
     *  @param tuple   the source tuple, which will not be modified
     */
    default void clampMin(double min, Tuple3D<?> tuple) {
        assert tuple != null : "Tuple must not be null"; //$NON-NLS-1$
        double x = tuple.getX();
        double y = tuple.getY();
        double z = tuple.getY();
        if (x < min) {
            x = min;
        }
        if (y < min) {
            y = min;
        }
        if (z < min) {
            z = min;
        }
        set(x, y, z);
    }

	/**
	 *  Clamps the maximum value of this tuple to the max parameter.
	 *  @param max   the highest value in the tuple after clamping
	 */
	default void clampMax(int max) {
		double x = getX();
		double y = getY();
		double z = getZ();
		if (x > max) {
			x = max;
		}
		if (y > max) {
			y = max;
		}
		if (z > max) {
			z = max;
		}
		set(x, y, z);
	}

	/**
	 *  Clamps the maximum value of this tuple to the max parameter.
	 *  @param max   the highest value in the tuple after clamping
	 */
	default void clampMax(double max) {
		double x = getX();
		double y = getY();
		double z = getZ();
		if (x > max) {
			x = max;
		}
		if (y > max) {
			y = max;
		}
		if (z > max) {
			z = max;
		}
		set(x, y, z);
	}

	/**
	 *  Clamps the maximum value of the tuple parameter to the max
	 *  parameter and places the values into this tuple.
	 *  @param max   the highest value in the tuple after clamping
	 *  @param tuple   the source tuple, which will not be modified
	 */
	default void clampMax(int max, Tuple3D<?> tuple) {
		assert tuple != null : "Tuple must not be null"; //$NON-NLS-1$
		double x = tuple.getX();
		double y = tuple.getY();
		double z = tuple.getY();
		if (x > max) {
			x = max;
		}
		if (y > max) {
			y = max;
		}
		if (z > max) {
			z = max;
		}
		set(x, y, z);
	}

	/**
	 *  Clamps the maximum value of the tuple parameter to the max
	 *  parameter and places the values into this tuple.
	 *  @param max   the highest value in the tuple after clamping
	 *  @param tuple   the source tuple, which will not be modified
	 */
	default void clampMax(double max, Tuple3D<?> tuple) {
		assert tuple != null : "Tuple must not be null"; //$NON-NLS-1$
		double x = tuple.getX();
		double y = tuple.getY();
		double z = tuple.getY();
		if (x > max) {
			x = max;
		}
		if (y > max) {
			y = max;
		}
		if (z > max) {
			z = max;
		}
		set(x, y, z);
	}

	/**
	 * Copies the values of this tuple into the tuple tuple.
	 * @param tuple is the target tuple
	 */
	default void get(Tuple3D<?> tuple) {
		assert tuple != null : "Tuple must not be null"; //$NON-NLS-1$
		tuple.set(getX(), getY(), getZ());
	}

	/**
	 *  Copies the value of the elements of this tuple into the array tuple.
	 *  @param tuple the array that will contain the values of the vector
	 */
	default void get(int[] tuple) {
		assert tuple != null : "Tuple must not be null"; //$NON-NLS-1$
		assert tuple.length >= 3 : "Size of the array too low"; //$NON-NLS-1$
		tuple[0] = ix();
		tuple[1] = iy();
		tuple[2] = iz();
	}

	/**
	 *  Copies the value of the elements of this tuple into the array tuple.
	 *  @param tuple the array that will contain the values of the vector
	 */
	default void get(double[] tuple) {
		assert tuple != null : "Tuple must not be null"; //$NON-NLS-1$
		assert tuple.length >= 3 : "Size of the array too low"; //$NON-NLS-1$
		tuple[0] = getX();
		tuple[1] = getY();
		tuple[2] = getZ();
	}

	/**
	 * Sets the value of this tuple to the negation of tuple t1.
	 * @param tuple the source tuple
	 */
	default void negate(Tuple3D<?> tuple) {
		assert tuple != null : "Tuple must not be null"; //$NON-NLS-1$
		set(-tuple.getX(), -tuple.getY(), -tuple.getZ());
	}

	/**
	 * Negates the value of this tuple in place.
	 */
	default void negate() {
		set(-getX(), -getY(), -getZ());
	}

	/**
	 * Sets the value of this tuple to the scalar multiplication
	 * of tuple t1.
	 * @param scale the scalar value
	 * @param tuple the source tuple
	 */
	default void scale(int scale, Tuple3D<?> tuple) {
		assert tuple != null : "Tuple must not be null"; //$NON-NLS-1$
		set(scale * tuple.getX(), scale * tuple.getY(), scale * tuple.getZ());
	}

	/**
	 * Sets the value of this tuple to the scalar multiplication
	 * of tuple t1.
	 * @param scale the scalar value
	 * @param tuple the source tuple
	 */
	default void scale(double scale, Tuple3D<?> tuple) {
		assert tuple != null : "Tuple must not be null"; //$NON-NLS-1$
		set(scale * tuple.getX(), scale * tuple.getY(), scale * tuple.getZ());
	}

	/**
	 * Sets the value of this tuple to the scalar multiplication
	 * of the scale factor with this.
	 * @param scale the scalar value
	 */
	default void scale(int scale) {
		set(scale * getX(), scale * getY(), scale * getZ());
	}

	/**
	 * Sets the value of this tuple to the scalar multiplication
	 * of the scale factor with this.
	 * @param scale the scalar value
	 */
	default void scale(double scale) {
		set(scale * getX(), scale * getY(), scale * getZ());
	}

	/**
	 * Sets the value of this tuple to the value of tuple t1.
	 * @param tuple the tuple to be copied
	 */
	default void set(Tuple3D<?> tuple) {
		assert tuple != null : "Tuple must not be null"; //$NON-NLS-1$
		set(tuple.getX(), tuple.getY(), tuple.getZ());
	}

	/**
	 * Sets the value of this tuple to the specified x and y and z
	 * coordinates.
	 * @param x the x coordinate
	 * @param y the y coordinate
	 * @param z the z coordinate
	 */
	default void set(int x, int y, int z) {
		setX(x);
		setY(y);
		setZ(z);
	}

	/**
	 * Sets the value of this tuple to the specified x and y and z
	 * coordinates.
	 * @param x the x coordinate
	 * @param y the y coordinate
	 * @param z the z coordinate
	 */
	default void set(double x, double y, double z) {
		setX(x);
		setY(y);
		setZ(z);
	}

	/**
	 * Sets the value of this tuple from the 3 values specified in
	 * the array.
	 * @param tuple the array of length 2 containing xyz in order
	 */
	default void set(int[] tuple) {
		assert tuple != null : "Tuple must not be null"; //$NON-NLS-1$
		assert tuple.length >= 3 : "Array size is too low"; //$NON-NLS-1$
		setX(tuple[0]);
		setY(tuple[1]);
		setZ(tuple[2]);
	}

	/**
	 * Sets the value of this tuple from the 2 values specified in
	 * the array.
	 * @param tuple the array of length 2 containing xy in order
	 */
	default void set(double[] tuple) {
		assert tuple != null : "Tuple must not be null"; //$NON-NLS-1$
		assert tuple.length >= 3 : "Array size is too low"; //$NON-NLS-1$
		setX(tuple[0]);
		setY(tuple[1]);
		setZ(tuple[2]);
	}

	/**
	 * Get the <i>x</i> coordinate.
	 *
	 * @return the x coordinate.
	 */
	@Pure
	double getX();

	/**
	 * Get the <i>x</i> coordinate.
	 *
	 * @return the x coordinate.
	 */
	@Pure
	int ix();

	/**
	 * Set the <i>x</i> coordinate.
	 *
	 * @param x  value to <i>x</i> coordinate.
	 */
	void setX(int x);

	/**
	 * Set the <i>x</i> coordinate.
	 *
	 * @param x  value to <i>x</i> coordinate.
	 */
	void setX(double x);

	/**
	 * Get the <i>y</i> coordinate.
	 *
	 * @return  the <i>y</i> coordinate.
	 */
	@Pure
	double getY();

	/**
	 * Get the <i>y</i> coordinate.
	 *
	 * @return  the <i>y</i> coordinate.
	 */
	@Pure
	int iy();

	/**
	 * Set the <i>y</i> coordinate.
	 *
	 * @param y value to <i>y</i> coordinate.
	 */
	void setY(int y);

	/**
	 * Set the <i>y</i> coordinate.
	 *
	 * @param y value to <i>y</i> coordinate.
	 */
	void setY(double y);

	/**
	 * Get the <i>z</i> coordinate.
	 *
	 * @return  the <i>z</i> coordinate.
	 */
	@Pure
	double getZ();

	/**
	 * Get the <i>z</i> coordinate.
	 *
	 * @return  the <i>z</i> coordinate.
	 */
	@Pure
	int iz();

	/**
	 * Set the <i>z</i> coordinate.
	 *
	 * @param z value to <i>z</i> coordinate.
	 */
	void setZ(int z);

	/**
	 * Set the <i>z</i> coordinate.
	 *
	 * @param z value to <i>z</i> coordinate.
	 */
	void setZ(double z);

	/**
	 * Sets the value of this tuple to the difference of itself and x, y and z.
	 * @param x x coordinate to substract.
	 * @param y y coordinate to substract.
	 * @param z y coordinate to substract.
	 */
	default void sub(int x, int y, int z) {
		set(getX() - x, getY() - y, getZ() - z);
	}

	/**
	 * Sets the value of this tuple to the difference of itself and x, y and z.
	 * @param x x coordinate to substract.
	 * @param y y coordinate to substract.
	 * @param z y coordinate to substract.
	 */
	default void sub(double x, double y, double z) {
		set(getX() - x, getY() - y, getZ() - z);
	}

	/**
	 * Sets the x value of this tuple to the difference of itself and x.
	 * @param x x coordinate to substract.
	 */
	default void subX(int x) {
		setX(getX() - x);
	}

	/**
	 * Sets the x value of this tuple to the difference of itself and x.
	 * @param x x coordinate to substract.
	 */
	default void subX(double x) {
		setX(getX() - x);
	}

	/**
	 * Sets the y value of this tuple to the difference of itself and y.
	 * @param y y coordinate to substract.
	 */
	default void subY(int y) {
		setY(getY() - y);
	}

	/**
	 * Sets the y value of this tuple to the difference of itself and y.
	 * @param y y coordinate to substract.
	 */
	default void subY(double y) {
		setY(getY() - y);
	}

	/**
	 * Sets the z value of this tuple to the difference of itself and z.
	 * @param z y coordinate to substract.
	 */
	default void subZ(int z) {
		setZ(getZ() - z);
	}

	/**
	 * Sets the z value of this tuple to the difference of itself and z.
	 * @param z y coordinate to substract.
	 */
	default void subZ(double z) {
		setZ(getZ() - z);
	}

	/**
	 *  Linearly interpolates between tuples t1 and t2 and places the
	 *  result into this tuple:  this = (1-alpha)*t1 + alpha*t2.
	 *  @param tuple1  the first tuple
	 *  @param tuple2  the second tuple
	 *  @param alpha  the alpha interpolation parameter
	 */
	default void interpolate(Tuple3D<?> tuple1, Tuple3D<?> tuple2, double alpha) {
		assert tuple1 != null : "First tuple must not be null"; //$NON-NLS-1$
		assert tuple2 != null : "Second tuple must not be null"; //$NON-NLS-1$
		assert alpha >= 0. && alpha <= 1. : "Alpha must be in [0; 1]"; //$NON-NLS-1$
		set((1. - alpha) * tuple1.getX() + alpha * tuple2.getX(),
			(1. - alpha) * tuple1.getY() + alpha * tuple2.getY(),
			(1. - alpha) * tuple1.getZ() + alpha * tuple2.getZ());
	}

	/**
	 *  Linearly interpolates between this tuple and tuple t1 and
	 *  places the result into this tuple:  this = (1-alpha)*this + alpha*t1.
	 *  @param tuple  the first tuple
	 *  @param alpha  the alpha interpolation parameter
	 */
	default void interpolate(Tuple3D<?> tuple, double alpha) {
		assert tuple != null : "First tuple must not be null"; //$NON-NLS-1$
		set((1. - alpha) * getX() + alpha * tuple.getX(),
			(1. - alpha) * getY() + alpha * tuple.getY(),
			(1. - alpha) * getZ() + alpha * tuple.getZ());
	}

	/**
	 * Returns true if all of the data members of Tuple2f t1 are
	 * equal to the corresponding data members in this Tuple2f.
	 * @param tuple  the vector with which the comparison is made
	 * @return  true or false
	 */
	@Pure
	default boolean equals(Tuple3D<?> tuple) {
		try {
            return getX() == tuple.getX() && getY() == tuple.getY() && getZ() == tuple.getZ();
		} catch (Throwable exception) {
			return false;
		}
	}

	/**
	 * Returns true if the Object t1 is of type Tuple2f and all of the
	 * data members of t1 are equal to the corresponding data members in
	 * this Tuple2f.
	 * @param t1  the object with which the comparison is made
	 * @return  true or false
	 */
	@Pure
	@Override
	boolean equals(Object t1);

	/**
	 * Returns true if the L-infinite distance between this tuple
	 * and tuple t1 is less than or equal to the epsilon parameter,
	 * otherwise returns false.  The L-infinite
	 * distance is equal to MAX[abs(x1-x2), abs(y1-y2)].
	 * @param tuple  the tuple to be compared to this tuple
	 * @param epsilon the threshold value
	 * @return  true or false
	 */
	@Pure
	default boolean epsilonEquals(Tuple3D<?> tuple, double epsilon) {
		assert tuple != null : "Tuple must not be null"; //$NON-NLS-1$
		final double dx = getX() - tuple.getX();
		final double dy = getY() - tuple.getY();
		final double dz = getZ() - tuple.getZ();
		return (dx * dx + dy * dy + dz * dz) <= (epsilon * epsilon);
	}

	/**
	 * Returns a hash code value based on the data values in this
	 * object.  Two different Tuple2f objects with identical data values
	 * (i.e., Tuple2f.equals returns true) will return the same hash
	 * code value.  Two objects with different data members may return the
	 * same hash value, although this is not likely.
	 * @return the integer hash code value
	 */
	@Pure
	@Override
	int hashCode();

}
