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
package org.arakhne.afc.math.generic;

import java.io.Serializable;

/** 2D tuple.
 * 
 * @param <TT> is the type of data that can be added or substracted to this tuple.
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @deprecated see {@link org.arakhne.afc.math.geometry.d2.Tuple2D}
 */
@Deprecated
public interface Tuple2D<TT extends Tuple2D<? super TT>>
extends Cloneable, Serializable {

	/** Clone this point.
	 * 
	 * @return the clone.
	 */
	public TT clone();

	/**
	 *  Sets each component of this tuple to its absolute value.
	 */
	public void absolute();

	/**
	 *  Sets each component of the tuple parameter to its absolute
	 *  value and places the modified values into this tuple.
	 *  @param t   the source tuple, which will not be modified
	 */
	public void absolute(TT t);

	/**
	 * Sets the value of this tuple to the sum of itself and x and y.
	 * @param x
	 * @param y
	 */
	public void add(int x, int y);

	/**
	 * Sets the value of this tuple to the sum of itself and x and y.
	 * @param x
	 * @param y
	 */
	public void add(double x, double y);

	/**
	 * Sets the x value of this tuple to the sum of itself and x.
	 * @param x
	 */
	public void addX(int x);

	/**
	 * Sets the x value of this tuple to the sum of itself and x.
	 * @param x
	 */
	public void addX(double x);

	/**
	 * Sets the y value of this tuple to the sum of itself and y.
	 * @param y
	 */
	public void addY(int y);

	/**
	 * Sets the y value of this tuple to the sum of itself and y.
	 * @param y
	 */
	public void addY(double y);

	/**
	 *  Clamps this tuple to the range [low, high].
	 *  @param min  the lowest value in this tuple after clamping
	 *  @param max  the highest value in this tuple after clamping
	 */
	public void clamp(int min, int max);

	/**
	 *  Clamps this tuple to the range [low, high].
	 *  @param min  the lowest value in this tuple after clamping
	 *  @param max  the highest value in this tuple after clamping
	 */
	public void clamp(double min, double max);

	/**
	 *  Clamps the minimum value of this tuple to the min parameter.
	 *  @param min   the lowest value in this tuple after clamping
	 */
	public void clampMin(int min);

	/**
	 *  Clamps the minimum value of this tuple to the min parameter.
	 *  @param min   the lowest value in this tuple after clamping
	 */
	public void clampMin(double min);

	/**
	 *  Clamps the maximum value of this tuple to the max parameter.
	 *  @param max   the highest value in the tuple after clamping
	 */
	public void clampMax(int max);

	/**
	 *  Clamps the maximum value of this tuple to the max parameter.
	 *  @param max   the highest value in the tuple after clamping
	 */
	public void clampMax(double max);

	/**
	 *  Clamps the tuple parameter to the range [low, high] and
	 *  places the values into this tuple.
	 *  @param min   the lowest value in the tuple after clamping
	 *  @param max  the highest value in the tuple after clamping
	 *  @param t   the source tuple, which will not be modified
	 */
	public void clamp(int min, int max, TT t);

	/**
	 *  Clamps the tuple parameter to the range [low, high] and
	 *  places the values into this tuple.
	 *  @param min   the lowest value in the tuple after clamping
	 *  @param max  the highest value in the tuple after clamping
	 *  @param t   the source tuple, which will not be modified
	 */
	public void clamp(double min, double max, TT t);

	/**
	 *  Clamps the minimum value of the tuple parameter to the min
	 *  parameter and places the values into this tuple.
	 *  @param min   the lowest value in the tuple after clamping
	 *  @param t   the source tuple, which will not be modified
	 */
	public void clampMin(int min, TT t);

	/**
	 *  Clamps the minimum value of the tuple parameter to the min
	 *  parameter and places the values into this tuple.
	 *  @param min   the lowest value in the tuple after clamping
	 *  @param t   the source tuple, which will not be modified
	 */
	public void clampMin(double min, TT t);

	/**
	 *  Clamps the maximum value of the tuple parameter to the max
	 *  parameter and places the values into this tuple.
	 *  @param max   the highest value in the tuple after clamping
	 *  @param t   the source tuple, which will not be modified
	 */
	public void clampMax(int max, TT t);

	/**
	 *  Clamps the maximum value of the tuple parameter to the max
	 *  parameter and places the values into this tuple.
	 *  @param max   the highest value in the tuple after clamping
	 *  @param t   the source tuple, which will not be modified
	 */
	public void clampMax(double max, TT t);

	/**
	 * Copies the values of this tuple into the tuple t.
	 * @param t is the target tuple
	 */
	public void get(TT t);

	/**
	 *  Copies the value of the elements of this tuple into the array t.
	 *  @param t the array that will contain the values of the vector
	 */
	public void get(int[] t);

	/**
	 *  Copies the value of the elements of this tuple into the array t.
	 *  @param t the array that will contain the values of the vector
	 */
	public void get(double[] t);

	/**
	 * Sets the value of this tuple to the negation of tuple t1.
	 * @param t1 the source tuple
	 */
	public void negate(TT t1);

	/**
	 * Negates the value of this tuple in place.
	 */
	public void negate();

	/**
	 * Sets the value of this tuple to the scalar multiplication
	 * of tuple t1.
	 * @param s the scalar value
	 * @param t1 the source tuple
	 */
	public void scale(int s, TT t1);

	/**
	 * Sets the value of this tuple to the scalar multiplication
	 * of tuple t1.
	 * @param s the scalar value
	 * @param t1 the source tuple
	 */
	public void scale(double s, TT t1);

	/**
	 * Sets the value of this tuple to the scalar multiplication
	 * of the scale factor with this.
	 * @param s the scalar value
	 */
	public void scale(int s);

	/**
	 * Sets the value of this tuple to the scalar multiplication
	 * of the scale factor with this.
	 * @param s the scalar value
	 */
	public void scale(double s);

	/**
	 * Sets the value of this tuple to the value of tuple t1.
	 * @param t1 the tuple to be copied
	 */
	public void set(Tuple2D<?> t1);

	/**
	 * Sets the value of this tuple to the specified x and y
	 * coordinates.
	 * @param x0 the x coordinate
	 * @param y0 the y coordinate
	 */
	public void set(double x0, double y0);

	/**
	 * Sets the value of this tuple to the specified x and y
	 * coordinates.
	 * @param x the x coordinate
	 * @param y the y coordinate
	 */
	public void set(float x, float y);

	/**
	 * Sets the value of this tuple from the 2 values specified in 
	 * the array.
	 * @param t the array of length 2 containing xy in order
	 */
	public void set(int[] t);

	/**
	 * Sets the value of this tuple from the 2 values specified in 
	 * the array.
	 * @param t the array of length 2 containing xy in order
	 */
	public void set(double[] t);

	/**
	 * Get the <i>x</i> coordinate.
	 * 
	 * @return the x coordinate.
	 */
	public double getX();

	/**
	 * Get the <i>x</i> coordinate.
	 * 
	 * @return the x coordinate.
	 */
	public int ix();

	/**
	 * Set the <i>x</i> coordinate.
	 * 
	 * @param x  value to <i>x</i> coordinate.
	 */
	public void setX(int x);

	/**
	 * Set the <i>x</i> coordinate.
	 * 
	 * @param x  value to <i>x</i> coordinate.
	 */
	public void setX(double x);

	/**
	 * Get the <i>y</i> coordinate.
	 * 
	 * @return  the <i>y</i> coordinate.
	 */
	public double getY();

	/**
	 * Get the <i>y</i> coordinate.
	 * 
	 * @return  the <i>y</i> coordinate.
	 */
	public int iy();

	/**
	 * Set the <i>y</i> coordinate.
	 * 
	 * @param y value to <i>y</i> coordinate.
	 */
	public void setY(int y);

	/**
	 * Set the <i>y</i> coordinate.
	 * 
	 * @param y value to <i>y</i> coordinate.
	 */
	public void setY(double y);

	/**
	 * Sets the value of this tuple to the difference of itself and x and y.
	 * @param x
	 * @param y
	 */
	public void sub(int x, int y);

	/**
	 * Sets the value of this tuple to the difference of itself and x and y.
	 * @param x
	 * @param y
	 */
	public void sub(double x, double y);

	/**
	 * Sets the x value of this tuple to the difference of itself and x.
	 * @param x
	 */
	public void subX(int x);

	/**
	 * Sets the x value of this tuple to the difference of itself and x.
	 * @param x
	 */
	public void subX(double x);

	/**
	 * Sets the y value of this tuple to the difference of itself and y.
	 * @param y
	 */
	public void subY(int y);

	/**
	 * Sets the y value of this tuple to the difference of itself and y.
	 * @param y
	 */
	public void subY(double y);

	/** 
	 *  Linearly interpolates between tuples t1 and t2 and places the 
	 *  result into this tuple:  this = (1-alpha)*t1 + alpha*t2.
	 *  @param t1  the first tuple
	 *  @param t2  the second tuple
	 *  @param alpha  the alpha interpolation parameter
	 */
	public void interpolate(TT t1, TT t2, double alpha);

	/**  
	 *  Linearly interpolates between this tuple and tuple t1 and 
	 *  places the result into this tuple:  this = (1-alpha)*this + alpha*t1.
	 *  @param t1  the first tuple
	 *  @param alpha  the alpha interpolation parameter  
	 */   
	public void interpolate(TT t1, double alpha); 

	/**   
	 * Returns true if all of the data members of Tuple2f t1 are
	 * equal to the corresponding data members in this Tuple2f.
	 * @param t1  the vector with which the comparison is made
	 * @return  true or false
	 */  
	public boolean equals(Tuple2D<?> t1);

	/**   
	 * Returns true if the Object t1 is of type Tuple2f and all of the
	 * data members of t1 are equal to the corresponding data members in
	 * this Tuple2f.
	 * @param t1  the object with which the comparison is made
	 * @return  true or false
	 */  
	@Override
	public boolean equals(Object t1);

	/**
	 * Returns true if the L-infinite distance between this tuple
	 * and tuple t1 is less than or equal to the epsilon parameter, 
	 * otherwise returns false.  The L-infinite
	 * distance is equal to MAX[abs(x1-x2), abs(y1-y2)]. 
	 * @param t1  the tuple to be compared to this tuple
	 * @param epsilon  the threshold value  
	 * @return  true or false
	 */
	public boolean epsilonEquals(TT t1, double epsilon);

	/**
	 * Returns a hash code value based on the data values in this
	 * object.  Two different Tuple2f objects with identical data values
	 * (i.e., Tuple2f.equals returns true) will return the same hash
	 * code value.  Two objects with different data members may return the
	 * same hash value, although this is not likely.
	 * @return the integer hash code value
	 */  
	@Override
	public int hashCode();

}