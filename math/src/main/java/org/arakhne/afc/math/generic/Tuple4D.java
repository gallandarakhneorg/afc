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
package org.arakhne.afc.math.generic;

import java.io.Serializable;

/** 4D tuple.
 * 
 * @param <TT> is the type of data that can be added or substracted to this tuple.
 * @author $Author: bohrhauer$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public interface Tuple4D<TT extends Tuple4D<? super TT>>
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
	 * @param z
	 * @param w
	 */
	public void add(int x, int y, int z, int w);

	/**
	 * Sets the value of this tuple to the sum of itself and x and y.
	 * @param x
	 * @param y
	 * @param z
	 * @param w
	 */
	public void add(float x, float y, float z, float w);

	/**
	 * Sets the x value of this tuple to the sum of itself and x.
	 * @param x
	 */
	public void addX(int x);

	/**
	 * Sets the x value of this tuple to the sum of itself and x.
	 * @param x
	 */
	public void addX(float x);

	/**
	 * Sets the y value of this tuple to the sum of itself and y.
	 * @param y
	 */
	public void addY(int y);

	/**
	 * Sets the y value of this tuple to the sum of itself and y.
	 * @param y
	 */
	public void addY(float y);

	/**
	 * Sets the z value of this tuple to the sum of itself and z.
	 * @param z
	 */
	public void addZ(int z);

	/**
	 * Sets the z value of this tuple to the sum of itself and z.
	 * @param z
	 */
	public void addZ(float z);
	
	/**
	 * Sets the w value of this tuple to the sum of itself and w.
	 * @param w
	 */
	public void addW(int w);

	/**
	 * Sets the w value of this tuple to the sum of itself and w.
	 * @param w
	 */
	public void addW(float w);
	
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
	public void clamp(float min, float max);

	/**
	 *  Clamps the minimum value of this tuple to the min parameter.
	 *  @param min   the lowest value in this tuple after clamping
	 */
	public void clampMin(int min);

	/**
	 *  Clamps the minimum value of this tuple to the min parameter.
	 *  @param min   the lowest value in this tuple after clamping
	 */
	public void clampMin(float min);

	/**
	 *  Clamps the maximum value of this tuple to the max parameter.
	 *  @param max   the highest value in the tuple after clamping
	 */
	public void clampMax(int max);

	/**
	 *  Clamps the maximum value of this tuple to the max parameter.
	 *  @param max   the highest value in the tuple after clamping
	 */
	public void clampMax(float max);

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
	public void clamp(float min, float max, TT t);

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
	public void clampMin(float min, TT t);

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
	public void clampMax(float max, TT t);

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
	public void get(float[] t);

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
	public void scale(float s, TT t1);

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
	public void scale(float s);

	/**
	 * Sets the value of this tuple to the value of tuple t1.
	 * @param t1 the tuple to be copied
	 */
	public void set(Tuple4D<?> t1);

	/**
	 * Sets the value of this tuple to the specified x and y
	 * coordinates.
	 * @param x the x coordinate
	 * @param y the y coordinate
	 * @param z the z coordinate
	 * @param z the w coordinate
	 */
	public void set(int x, int y, int z, int w);

	/**
	 * Sets the value of this tuple to the specified x and y
	 * coordinates.
	 * @param x the x coordinate
	 * @param y the y coordinate
	 * @param z the z coordinate
	 * @param w the w coordinate
	 */
	public void set(float x, float y, float z, float w);

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
	public void set(float[] t);

	/**
	 * Get the <i>x</i> coordinate.
	 * 
	 * @return the x coordinate.
	 */
	public float getX();

	/**
	 * Get the <i>x</i> coordinate.
	 * 
	 * @return the x coordinate.
	 */
	public int x();

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
	public void setX(float x);

	/**
	 * Get the <i>y</i> coordinate.
	 * 
	 * @return  the <i>y</i> coordinate.
	 */
	public float getY();

	/**
	 * Get the <i>y</i> coordinate.
	 * 
	 * @return  the <i>y</i> coordinate.
	 */
	public int y();

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
	public void setY(float y);

	/**
	 * Get the <i>z</i> coordinate.
	 * 
	 * @return  the <i>z</i> coordinate.
	 */
	public float getZ();
	
	/**
	 * Get the <i>w</i> coordinate.
	 * 
	 * @return  the <i>w</i> coordinate.
	 */
	public float getW();

	/**
	 * Get the <i>z</i> coordinate.
	 * 
	 * @return  the <i>z</i> coordinate.
	 */
	public int z();
	
	/**
	 * Get the <i>w</i> coordinate.
	 * 
	 * @return  the <i>w</i> coordinate.
	 */
	public int w();

	/**
	 * Set the <i>z</i> coordinate.
	 * 
	 * @param z value to <i>z</i> coordinate.
	 */
	public void setZ(int z);

	/**
	 * Set the <i>z</i> coordinate.
	 * 
	 * @param z value to <i>z</i> coordinate.
	 */
	public void setZ(float z);
	
	/**
	 * Set the <i>w</i> coordinate.
	 * 
	 * @param w value to <i>w</i> coordinate.
	 */
	public void setW(int w);

	/**
	 * Set the <i>w</i> coordinate.
	 * 
	 * @param w value to <i>w</i> coordinate.
	 */
	public void setW(float w);

	/**
	 * Sets the value of this tuple to the difference of itself and x, y, z and w.
	 * @param x
	 * @param y
	 * @param z
	 * @param w
	 */
	public void sub(int x, int y, int z, int w);

	/**
	 * Sets the value of this tuple to the difference of itself and x, y, z and w.
	 * @param x
	 * @param y
	 * @param z
	 * @param w
	 */
	public void sub(float x, float y, float z, float w);

	/**
	 * Sets the x value of this tuple to the difference of itself and x.
	 * @param x
	 */
	public void subX(int x);

	/**
	 * Sets the x value of this tuple to the difference of itself and x.
	 * @param x
	 */
	public void subX(float x);

	/**
	 * Sets the y value of this tuple to the difference of itself and y.
	 * @param y
	 */
	public void subY(int y);

	/**
	 * Sets the y value of this tuple to the difference of itself and y.
	 * @param y
	 */
	public void subY(float y);

	/**
	 * Sets the z value of this tuple to the difference of itself and z.
	 * @param z
	 */
	public void subZ(int z);

	/**
	 * Sets the z value of this tuple to the difference of itself and z.
	 * @param z
	 */
	public void subZ(float z);

	/**
	 * Sets the w value of this tuple to the difference of itself and w.
	 * @param w
	 */
	public void subW(int w);

	/**
	 * Sets the w value of this tuple to the difference of itself and w.
	 * @param w
	 */
	public void subW(float w);
	
	/** 
	 *  Linearly interpolates between tuples t1 and t2 and places the 
	 *  result into this tuple:  this = (1-alpha)*t1 + alpha*t2.
	 *  @param t1  the first tuple
	 *  @param t2  the second tuple
	 *  @param alpha  the alpha interpolation parameter
	 */
	public void interpolate(TT t1, TT t2, float alpha);

	/**  
	 *  Linearly interpolates between this tuple and tuple t1 and 
	 *  places the result into this tuple:  this = (1-alpha)*this + alpha*t1.
	 *  @param t1  the first tuple
	 *  @param alpha  the alpha interpolation parameter  
	 */   
	public void interpolate(TT t1, float alpha); 

	/**   
	 * Returns true if all of the data members of Tuple2f t1 are
	 * equal to the corresponding data members in this Tuple2f.
	 * @param t1  the vector with which the comparison is made
	 * @return  true or false
	 */  
	public boolean equals(Tuple4D<?> t1);

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
	public boolean epsilonEquals(TT t1, float epsilon);

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