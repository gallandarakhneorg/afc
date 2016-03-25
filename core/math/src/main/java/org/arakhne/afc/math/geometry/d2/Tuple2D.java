/* 
 * $Id$
 * 
 * Copyright (C) 2010-2012 Stephane GALLAND.
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
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
package org.arakhne.afc.math.geometry.d2;

import java.io.Serializable;

import org.arakhne.afc.math.MathUtil;
import org.eclipse.xtext.xbase.lib.Pure;

/** 2D tuple.
 * 
 * @param <TT> is the type of data that can be added or substracted to this tuple.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public interface Tuple2D<TT extends Tuple2D<? super TT>>
		extends Cloneable, Serializable {

	/** Clone this point.
	 * 
	 * @return the clone.
	 */
	@Pure
	TT clone();

	/**
	 *  Sets each component of this tuple to its absolute value.
	 */
	default void absolute() {
		setX(Math.abs(getX()));
		setY(Math.abs(getY()));
	}

	/**
	 * Sets each component of the tuple parameter to its absolute
	 * value and places the modified values into this tuple.
	 *
	 * @param tuple   the source tuple, which will not be modified
	 */
	default void absolute(TT tuple)  {
		tuple.set(Math.abs(getX()), Math.abs(getY()));
	}

	/**
	 * Sets the value of this tuple to the sum of itself and x and y.
	 * @param x
	 * @param y
	 */
	default void add(int x, int y) {
		set(getX() + x, getY() + y);
	}

	/**
	 * Sets the value of this tuple to the sum of itself and x and y.
	 * @param x
	 * @param y
	 */
	default void add(double x, double y) {
		set(getX() + x, this.getY() + y);
	}

	/**
	 * Sets the x value of this tuple to the sum of itself and x.
	 * @param x
	 */
	default void addX(int x) {
		setX(getX() + x);
	}

	/**
	 * Sets the x value of this tuple to the sum of itself and x.
	 * @param x
	 */
	default void addX(double x) {
		setX(getX() + x);
	}

	/**
	 * Sets the y value of this tuple to the sum of itself and y.
	 * @param y
	 */
	default void addY(int y) {
		setY(getY() + y);
	}

	/**
	 * Sets the y value of this tuple to the sum of itself and y.
	 * @param y
	 */
	default void addY(double y) {
		setY(getY() + y);
	}

	/**
	 *  Clamps this tuple to the range [low, high].
	 *  @param min  the lowest value in this tuple after clamping
	 *  @param max  the highest value in this tuple after clamping
	 */
	default void clamp(int min, int max) {
		double x = MathUtil.clamp(getX(), min, max);
		double y = MathUtil.clamp(getY(), min, max);
		set(x, y);
	}

	/**
	 *  Clamps this tuple to the range [low, high].
	 *  @param min  the lowest value in this tuple after clamping
	 *  @param max  the highest value in this tuple after clamping
	 */
	default void clamp(double min, double max) {
		double x = MathUtil.clamp(getX(), min, max);
		double y = MathUtil.clamp(getY(), min, max);
		set(x, y);
	}

	/**
	 *  Clamps the minimum value of this tuple to the min parameter.
	 *  @param min   the lowest value in this tuple after clamping
	 */
	default void clampMin(int min) {
		double x = getX();
		double y = getY();
		if (x < min) {
			x = min;
		}
		if (y < min) {
			y = min;
		}
		set(x, y);
	}

	/**
	 *  Clamps the minimum value of this tuple to the min parameter.
	 *  @param min   the lowest value in this tuple after clamping
	 */
	default void clampMin(double min) {
		double x = getX();
		double y = getY();
		if (x < min) {
			x = min;
		}
		if (y < min) {
			y = min;
		}
		set(x, y);
	}

	/**
	 *  Clamps the maximum value of this tuple to the max parameter.
	 *  @param max   the highest value in the tuple after clamping
	 */
	default void clampMax(int max) {
		double x = getX();
		double y = getY();
		if (x > max) {
			x = max;
		}
		if (y > max) {
			y = max;
		}
		set(x, y);
	}

	/**
	 *  Clamps the maximum value of this tuple to the max parameter.
	 *  @param max   the highest value in the tuple after clamping
	 */
	default void clampMax(double max) {
		double x = getX();
		double y = getY();
		if (x > max) {
			x = max;
		}
		if (y > max) {
			y = max;
		}
		set(x, y);
	}

	/**
	 *  Clamps the tuple parameter to the range [min, max] and
	 *  places the values into this tuple.
	 *
	 *  @param min   the lowest value in the tuple after clamping
	 *  @param max  the highest value in the tuple after clamping
	 *  @param tuple   the source tuple, which will not be modified
	 */
	default void clamp(int min, int max, TT tuple) {
		double x = MathUtil.clamp(tuple.getX(), min, max);
		double y = MathUtil.clamp(tuple.getY(), min, max);
		set(x, y);
	}

	/**
	 *  Clamps the tuple parameter to the range [low, high] and
	 *  places the values into this tuple.
	 *  @param min   the lowest value in the tuple after clamping
	 *  @param max  the highest value in the tuple after clamping
	 *  @param tuple   the source tuple, which will not be modified
	 */
	default void clamp(double min, double max, TT tuple) {
		double x = MathUtil.clamp(tuple.getX(), min, max);
		double y = MathUtil.clamp(tuple.getY(), min, max);
		set(x, y);
	}

	/**
	 *  Clamps the minimum value of the tuple parameter to the min
	 *  parameter and places the values into this tuple.
	 *  @param min   the lowest value in the tuple after clamping
	 *  @param tuple   the source tuple, which will not be modified
	 */
	default void clampMin(int min, TT tuple) {
		double x = tuple.getX();
		double y = tuple.getY();
		if (x < min) {
			x = min;
		}
		if (y < min) {
			y = min;
		}
		set(x, y);
	}

	/**
	 *  Clamps the minimum value of the tuple parameter to the min
	 *  parameter and places the values into this tuple.
	 *  @param min   the lowest value in the tuple after clamping
	 *  @param tuple   the source tuple, which will not be modified
	 */
	default void clampMin(double min, TT tuple) {
		double x = tuple.getX();
		double y = tuple.getY();
		if (x < min) {
			x = min;
		}
		if (y < min) {
			y = min;
		}
		set(x, y);
	}

	/**
	 *  Clamps the maximum value of the tuple parameter to the max
	 *  parameter and places the values into this tuple.
	 *  @param max   the highest value in the tuple after clamping
	 *  @param tuple   the source tuple, which will not be modified
	 */
	default void clampMax(int max, TT tuple) {
		double x = tuple.getX();
		double y = tuple.getY();
		if (x > max) {
			x = max;
		}
		if (y > max) {
			y = max;
		}
		set(x, y);
	}

	/**
	 *  Clamps the maximum value of the tuple parameter to the max
	 *  parameter and places the values into this tuple.
	 *  @param max   the highest value in the tuple after clamping
	 *  @param tuple   the source tuple, which will not be modified
	 */
	default void clampMax(double max, TT tuple) {
		double x = tuple.getX();
		double y = tuple.getY();
		if (x > max) {
			x = max;
		}
		if (y > max) {
			y = max;
		}
		set(x, y);
	}

	/**
	 * Copies the values of this tuple into the tuple t.
	 * @param tuple is the target tuple
	 */
	default void get(TT tuple) {
		tuple.set(getX(), getY());
	}

	/**
	 *  Copies the value of the elements of this tuple into the array t.
	 *  @param tuple the array that will contain the values of the vector
	 */
	default void get(int[] tuple) {
		tuple[0] = ix();
		tuple[1] = iy();
	}

	/**
	 *  Copies the value of the elements of this tuple into the array t.
	 *  @param tuple the array that will contain the values of the vector
	 */
	default void get(double[] tuple) {
		tuple[0] = getX();
		tuple[1]= getY();
	}

	/**
	 * Sets the value of this tuple to the negation of tuple t1.
	 * @param tuple the source tuple
	 */
	default void negate(TT tuple) {
		set(-tuple.getX(), -tuple.getY());
	}

	/**
	 * Negates the value of this tuple in place.
	 */
	default void negate() {
		set(-getX(), -getY());
	}

	/**
	 * Sets the value of this tuple to the scalar multiplication
	 * of tuple t1.
	 * @param scale the scalar value
	 * @param tuple the source tuple
	 */
	default void scale(int scale, TT tuple) {
		set(scale * tuple.getX(), scale * tuple.getY());
	}

	/**
	 * Sets the value of this tuple to the scalar multiplication
	 * of tuple t1.
	 * @param scale the scalar value
	 * @param tuple the source tuple
	 */
	default void scale(double scale, TT tuple) {
		set(scale * tuple.getX(), scale * tuple.getY());
	}

	/**
	 * Sets the value of this tuple to the scalar multiplication
	 * of the scale factor with this.
	 * @param scale the scalar value
	 */
	default void scale(int scale) {
		set(scale * getX(), scale * getY());
	}

	/**
	 * Sets the value of this tuple to the scalar multiplication
	 * of the scale factor with this.
	 * @param scale the scalar value
	 */
	default void scale(double scale) {
		set(scale * getX(), scale * getY());
	}

	/**
	 * Sets the value of this tuple to the value of tuple t1.
	 * @param tuple the tuple to be copied
	 */
	default void set(Tuple2D<?> tuple) {
		set(tuple.getX(), tuple.getY());
	}

	/**
	 * Sets the value of this tuple to the specified x and y
	 * coordinates.
	 * @param x the x coordinate
	 * @param y the y coordinate
	 */
	default void set(int x, int y) {
		setX(x);
		setY(y);
	}

	/**
	 * Sets the value of this tuple to the specified x and y
	 * coordinates.
	 * @param x the x coordinate
	 * @param y the y coordinate
	 */
	default void set(double x, double y) {
		setX(x);
		setY(y);
	}

	/**
	 * Sets the value of this tuple from the 2 values specified in 
	 * the array.
	 * @param tuple the array of length 2 containing xy in order
	 */
	default void set(int[] tuple) {
		setX(tuple[0]);
		setY(tuple[1]);
	}

	/**
	 * Sets the value of this tuple from the 2 values specified in 
	 * the array.
	 * @param tuple the array of length 2 containing xy in order
	 */
	default void set(double[] tuple) {
		setX(tuple[0]);
		setY(tuple[1]);
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
	 * Sets the value of this tuple to the difference of itself and x and y.
	 * @param x
	 * @param y
	 */
	default void sub(int x, int y) {
		set(getX() - x, getY() - y);
	}

	/**
	 * Sets the value of this tuple to the difference of itself and x and y.
	 * @param x
	 * @param y
	 */
	default void sub(double x, double y) {
		set(getX() - x, getY() - y);
	}

	/**
	 * Sets the x value of this tuple to the difference of itself and x.
	 * @param x
	 */
	default void subX(int x) {
		setX(getX() - x);
	}

	/**
	 * Sets the x value of this tuple to the difference of itself and x.
	 * @param x
	 */
	default void subX(double x) {
		setX(getX() - x);
	}

	/**
	 * Sets the y value of this tuple to the difference of itself and y.
	 * @param y
	 */
	default void subY(int y) {
		setY(getY() - y);
	}

	/**
	 * Sets the y value of this tuple to the difference of itself and y.
	 * @param y
	 */
	default void subY(double y) {
		setY(getY() - y);
	}

	/** 
	 *  Linearly interpolates between tuples t1 and t2 and places the 
	 *  result into this tuple:  this = (1-alpha)*t1 + alpha*t2.
	 *  @param tuple1  the first tuple
	 *  @param tuple2  the second tuple
	 *  @param alpha  the alpha interpolation parameter
	 */
	default void interpolate(TT tuple1, TT tuple2, double alpha) {
		set((1. - alpha) * tuple1.getX() + alpha * tuple2.getX(),
			(1. - alpha) * tuple1.getY() + alpha * tuple2.getY());
	}

	/**  
	 *  Linearly interpolates between this tuple and tuple t1 and 
	 *  places the result into this tuple:  this = (1-alpha)*this + alpha*t1.
	 *  @param tuple  the first tuple
	 *  @param alpha  the alpha interpolation parameter  
	 */   
	default void interpolate(TT tuple, double alpha) {
		set((1. - alpha) * getX() + alpha * tuple.getX(),
			(1. - alpha) * getY() + alpha * tuple.getY());
	} 

	/**   
	 * Returns true if all of the data members of Tuple2D t1 are
	 * equal to the corresponding data members in this Tuple2D.
	 * @param tuple  the vector with which the comparison is made
	 * @return  true or false
	 */  
	@Pure
	default boolean equals(Tuple2D<?> tuple) {
		try {
			return (getX() == tuple.getX() && getY() == tuple.getY());
		}
		catch (Throwable exception) {
			return false;
		}
	}

	/**   
	 * Returns true if the Object t1 is of type Tuple2D and all of the
	 * data members of t1 are equal to the corresponding data members in
	 * this Tuple2D.
	 * @param object  the object with which the comparison is made
	 * @return  true or false
	 */  
	@Pure
	@Override
	boolean equals(Object object);

	/**
	 * Returns true if the Euclidian distance between this tuple
	 * and tuple t1 is less than or equal to the epsilon parameter, 
	 * otherwise returns false.
	 * 
	 * @param tuple  the tuple to be compared to this tuple
	 * @param epsilon  the threshold value  
	 * @return  true or false
	 */
	@Pure
	default boolean epsilonEquals(TT tuple, double epsilon) {
		double dx = getX() - tuple.getX();
		double dy = getY() - tuple.getY();
		return (dx * dx + dy * dy) <= (epsilon * epsilon);
	}

	/**
	 * Returns a hash code value based on the data values in this
	 * object.  Two different {@link Tuple2D} objects with identical data values
	 * (i.e., {@link Tuple2D#equals(Object)} returns true) will return the same hash
	 * code value.  Two objects with different data members may return the
	 * same hash value, although this is not likely.
	 *
	 * @return the integer hash code value
	 */  
	@Pure
	@Override
	int hashCode();
	
}