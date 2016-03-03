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
	 *  Sets each component of the tuple parameter to its absolute
	 *  value and places the modified values into this tuple.
	 *  @param t   the source tuple, which will not be modified
	 */
	default void absolute(TT t)  {
		t.set(Math.abs(getX()), Math.abs(getY()));
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
	 *  @param min   the lowest value in the tuple after clamping
	 *  @param max  the highest value in the tuple after clamping
	 *  @param t   the source tuple, which will not be modified
	 */
	default void clamp(int min, int max, TT t) {
		double x = MathUtil.clamp(t.getX(), min, max);
		double y = MathUtil.clamp(t.getY(), min, max);
		set(x, y);
	}

	/**
	 *  Clamps the tuple parameter to the range [low, high] and
	 *  places the values into this tuple.
	 *  @param min   the lowest value in the tuple after clamping
	 *  @param max  the highest value in the tuple after clamping
	 *  @param t   the source tuple, which will not be modified
	 */
	default void clamp(double min, double max, TT t) {
		double x = MathUtil.clamp(t.getX(), min, max);
		double y = MathUtil.clamp(t.getY(), min, max);
		set(x, y);
	}

	/**
	 *  Clamps the minimum value of the tuple parameter to the min
	 *  parameter and places the values into this tuple.
	 *  @param min   the lowest value in the tuple after clamping
	 *  @param t   the source tuple, which will not be modified
	 */
	default void clampMin(int min, TT t) {
		double x = t.getX();
		double y = t.getY();
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
	 *  @param t   the source tuple, which will not be modified
	 */
	default void clampMin(double min, TT t) {
		double x = t.getX();
		double y = t.getY();
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
	 *  @param t   the source tuple, which will not be modified
	 */
	default void clampMax(int max, TT t) {
		double x = t.getX();
		double y = t.getY();
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
	 *  @param t   the source tuple, which will not be modified
	 */
	default void clampMax(double max, TT t) {
		double x = t.getX();
		double y = t.getY();
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
	 * @param t is the target tuple
	 */
	default void get(TT t) {
		t.set(getX(), getY());
	}

	/**
	 *  Copies the value of the elements of this tuple into the array t.
	 *  @param t the array that will contain the values of the vector
	 */
	default void get(int[] t) {
		t[0] = ix();
		t[1] = iy();
	}

	/**
	 *  Copies the value of the elements of this tuple into the array t.
	 *  @param t the array that will contain the values of the vector
	 */
	default void get(double[] t) {
		t[0] = getX();
		t[1]= getY();
	}

	/**
	 * Sets the value of this tuple to the negation of tuple t1.
	 * @param t1 the source tuple
	 */
	default void negate(TT t1) {
		set(-t1.getX(), -t1.getY());
	}

	/**
	 * Negates the value of this tuple in place.
	 */
	default void negate() {
		set(getX(), getY());
	}

	/**
	 * Sets the value of this tuple to the scalar multiplication
	 * of tuple t1.
	 * @param s the scalar value
	 * @param t1 the source tuple
	 */
	default void scale(int s, TT t1) {
		set(s * t1.getX(), s * t1.getY());
	}

	/**
	 * Sets the value of this tuple to the scalar multiplication
	 * of tuple t1.
	 * @param s the scalar value
	 * @param t1 the source tuple
	 */
	default void scale(double s, TT t1) {
		set(s * t1.getX(), s * t1.getY());
	}

	/**
	 * Sets the value of this tuple to the scalar multiplication
	 * of the scale factor with this.
	 * @param s the scalar value
	 */
	default void scale(int s) {
		set(s * getX(), s * getY());
	}

	/**
	 * Sets the value of this tuple to the scalar multiplication
	 * of the scale factor with this.
	 * @param s the scalar value
	 */
	default void scale(double s) {
		set(s * getX(), s * getY());
	}

	/**
	 * Sets the value of this tuple to the value of tuple t1.
	 * @param t1 the tuple to be copied
	 */
	default void set(Tuple2D<?> t1) {
		set(t1.getX(), t1.getY());
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
	 * @param t the array of length 2 containing xy in order
	 */
	default void set(int[] t) {
		setX(t[0]);
		setY(t[1]);
	}

	/**
	 * Sets the value of this tuple from the 2 values specified in 
	 * the array.
	 * @param t the array of length 2 containing xy in order
	 */
	default void set(double[] t) {
		setX(t[0]);
		setY(t[1]);
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
	 *  @param t1  the first tuple
	 *  @param t2  the second tuple
	 *  @param alpha  the alpha interpolation parameter
	 */
	default void interpolate(TT t1, TT t2, double alpha) {
		set((1. - alpha) * t1.getX() + alpha * t2.getX(),
			(1. - alpha) * t1.getY() + alpha * t2.getY());
	}

	/**  
	 *  Linearly interpolates between this tuple and tuple t1 and 
	 *  places the result into this tuple:  this = (1-alpha)*this + alpha*t1.
	 *  @param t1  the first tuple
	 *  @param alpha  the alpha interpolation parameter  
	 */   
	default void interpolate(TT t1, double alpha) {
		set((1. - alpha) * getX() + alpha * t1.getX(),
			(1. - alpha) * getY() + alpha * t1.getY());
	} 

	/**   
	 * Returns true if all of the data members of Tuple2D t1 are
	 * equal to the corresponding data members in this Tuple2D.
	 * @param t1  the vector with which the comparison is made
	 * @return  true or false
	 */  
	@Pure
	default boolean equals(Tuple2D<?> t1) {
		try {
			return (getX() == getX() && getY() == getY());
		}
		catch (NullPointerException e2) {
			return false;
		}
	}

	/**   
	 * Returns true if the Object t1 is of type Tuple2D and all of the
	 * data members of t1 are equal to the corresponding data members in
	 * this Tuple2D.
	 * @param t1  the object with which the comparison is made
	 * @return  true or false
	 */  
	@Pure
	@Override
	boolean equals(Object t1);

	/**
	 * Returns true if the Euclidian distance between this tuple
	 * and tuple t1 is less than or equal to the epsilon parameter, 
	 * otherwise returns false.
	 * 
	 * @param t1  the tuple to be compared to this tuple
	 * @param epsilon  the threshold value  
	 * @return  true or false
	 */
	@Pure
	default boolean epsilonEquals(TT t1, double epsilon) {
		double dx = getX() - t1.getX();
		double dy = getY() - t1.getY();
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

	/** Unmodifiable 2D tuple.
	 * 
	 * @param <TT> is the type of data that can be added or substracted to this tuple.
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 13.0
	 */
	public interface UnmodifiableTuple2D<TT extends Tuple2D<? super TT>>
			extends Tuple2D<TT> {

		@Override
		default void absolute() {
			throw new UnsupportedOperationException();
		}

		@Override
		default void absolute(TT t)  {
			throw new UnsupportedOperationException();
		}

		@Override
		default void add(int x, int y) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void add(double x, double y) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void addX(int x) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void addX(double x) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void addY(int y) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void addY(double y) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void clamp(int min, int max) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void clamp(double min, double max) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void clampMin(int min) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void clampMin(double min) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void clampMax(int max) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void clampMax(double max) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void clamp(int min, int max, TT t) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void clamp(double min, double max, TT t) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void clampMin(int min, TT t) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void clampMin(double min, TT t) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void clampMax(int max, TT t) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void clampMax(double max, TT t) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void negate(TT t1) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void negate() {
			throw new UnsupportedOperationException();
		}

		@Override
		default void scale(int s, TT t1) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void scale(double s, TT t1) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void scale(int s) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void scale(double s) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void set(Tuple2D<?> t1) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void set(int x, int y) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void set(double x, double y) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void set(int[] t) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void set(double[] t) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void setX(int x) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void setX(double x) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void setY(int y) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void setY(double y) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void sub(int x, int y) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void sub(double x, double y) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void subX(int x) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void subX(double x) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void subY(int y) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void subY(double y) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void interpolate(TT t1, TT t2, double alpha) {
			throw new UnsupportedOperationException();
		}

		@Override
		default void interpolate(TT t1, double alpha) {
			throw new UnsupportedOperationException();
		} 

	}
	
}