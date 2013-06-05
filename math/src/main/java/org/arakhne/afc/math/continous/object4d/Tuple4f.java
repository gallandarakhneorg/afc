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
package org.arakhne.afc.math.continous.object4d;

import org.arakhne.afc.math.generic.Tuple4D;

/**
 * A 4-element tuple represented by single-precision floating point x,y,z,w 
 * coordinates.
 * 
 * @param <T> is the implementation type of the tuple.
 * @author $Author: bohrhauer$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class Tuple4f<T extends Tuple4D<? super T>> implements Tuple4D<T> {

  static final long serialVersionUID =  7068460319248845763L;

  /**
   * The x coordinate.
   */
  protected	float	x;

  /**
   * The y coordinate.
   */
  protected	float	y;

  /**
   * The z coordinate.
   */
  protected	float	z;

  /**
   * The w coordinate.
   */
  protected	float	w;


  /**
   * Constructs and initializes a Tuple4f from the specified xyzw coordinates.
   * @param x the x coordinate
   * @param y the y coordinate
   * @param z the z coordinate
   * @param w the w coordinate
   */
  public Tuple4f(float x, float y, float z, float w)
  {
    this.x = x;
    this.y = y;
    this.z = z;
    this.w = w;
  }


  /**
   * Constructs and initializes a Tuple4f from the array of length 4. 
   * @param t the array of length 4 containing xyzw in order
   */
  public Tuple4f(float[] t)
  {
    this.x = t[0];
    this.y = t[1];
    this.z = t[2];
    this.w = t[3];
  }


  /**
	 * @param tuple is the tuple to copy.
	 */
	public Tuple4f(Tuple4D<?> tuple)
	{
		this.x = tuple.getX();
		this.y = tuple.getY();
		this.z = tuple.getZ();
		this.w = tuple.getW();
	}


  /**
   * Constructs and initializes a Tuple4f to (0,0,0,0).
   */
  public Tuple4f()
  {
    this.x = 0.0f;
    this.y = 0.0f;
    this.z = 0.0f;
    this.w = 0.0f;
  }


    /**
     * Sets the value of this tuple to the specified xyzw coordinates.
     * @param x the x coordinate
     * @param y the y coordinate
     * @param z the z coordinate
     * @param w the w coordinate
     */
    @Override
	public final void set(float x, float y, float z, float w)
    {
	this.x = x;
	this.y = y;
	this.z = z;
	this.w = w;
    }


    /**
     * Sets the value of this tuple to the specified coordinates in the
     * array of length 4.
     * @param t the array of length 4 containing xyzw in order
     */
    @Override
	public final void set(float[] t)
    {
	this.x = t[0];
	this.y = t[1];
	this.z = t[2];
	this.w = t[3];
    }


    /**
     * Copies the values of this tuple into the array t.
     * @param t the array 
     */
   @Override
public final void get(float[] t)
   {
      t[0] = this.x;
      t[1] = this.y;
      t[2] = this.z;
      t[3] = this.w;
   } 

 
    /**
   * Sets the value of this tuple to the sum of tuples t1 and t2.
   * @param t1 the first tuple
   * @param t2 the second tuple
   */
  public final void add(Tuple4f<?> t1, Tuple4f<?> t2)
  {
    this.x = t1.x + t2.x;
    this.y = t1.y + t2.y;
    this.z = t1.z + t2.z;
    this.w = t1.w + t2.w;
  }


  /**
   * Sets the value of this tuple to the sum of itself and t1.
   * @param t1 the other tuple
   */
  public final void add(Tuple4f<?> t1)
  { 
    this.x += t1.x;
    this.y += t1.y;
    this.z += t1.z;
    this.w += t1.w;
  }


  /**
   * Sets the value of this tuple to the difference
   * of tuples t1 and t2 (this = t1 - t2).
   * @param t1 the first tuple
   * @param t2 the second tuple
   */
  public final void sub(Tuple4f<?> t1, Tuple4f<?> t2)
  {
    this.x = t1.x - t2.x;
    this.y = t1.y - t2.y;
    this.z = t1.z - t2.z;
    this.w = t1.w - t2.w;
  }


  /**
   * Sets the value of this tuple to the difference
   * of itself and t1 (this = this - t1).
   * @param t1 the other tuple 
   */
  public final void sub(Tuple4f<?> t1)
  { 
    this.x -= t1.x;
    this.y -= t1.y;
    this.z -= t1.z;
    this.w -= t1.w;
  }


  /**
   * Sets the value of this tuple to the negation of tuple t1.
   * @param t1 the source tuple
   */
  public final void negate(Tuple4f<?> t1)
  {
    this.x = -t1.x;
    this.y = -t1.y;
    this.z = -t1.z;
    this.w = -t1.w;
  }


  /**
   * Negates the value of this tuple in place.
   */
  @Override
public final void negate()
  {
    this.x = -this.x;
    this.y = -this.y;
    this.z = -this.z;
    this.w = -this.w;
  }


  /**
   * Sets the value of this tuple to the scalar multiplication
   * of tuple t1.
   * @param s the scalar value
   * @param t1 the source tuple
   */
  public final void scale(float s, Tuple4f<?> t1)
  {
    this.x = s*t1.x;
    this.y = s*t1.y;
    this.z = s*t1.z;
    this.w = s*t1.w;
  }


  /**
   * Sets the value of this tuple to the scalar multiplication
   * of the scale factor with this.
   * @param s the scalar value
   */
  @Override
public final void scale(float s)
  {
    this.x *= s;
    this.y *= s;
    this.z *= s;
    this.w *= s;
  }


  /**
   * Sets the value of this tuple to the scalar multiplication
   * of tuple t1 plus tuple t2 (this = s*t1 + t2).
   * @param s the scalar value
   * @param t1 the tuple to be multipled
   * @param t2 the tuple to be added
   */
  public final void scaleAdd(float s, Tuple4f<?> t1, Tuple4f<?> t2)
  {
    this.x = s*t1.x + t2.x;
    this.y = s*t1.y + t2.y;
    this.z = s*t1.z + t2.z;
    this.w = s*t1.w + t2.w;
  }


    /**
     * Sets the value of this tuple to the scalar multiplication
     * of itself and then adds tuple t1 (this = s*this + t1).
     * @param s the scalar value
     * @param t1 the tuple to be added
     */  
    public final void scaleAdd(float s, Tuple4f<?> t1)
    {
        this.x = s*this.x + t1.x;
        this.y = s*this.y + t1.y;
        this.z = s*this.z + t1.z;
        this.w = s*this.w + t1.w;
    }



   /**
     * Returns a string that contains the values of this Tuple4f.
     * The form is (x,y,z,w).
     * @return the String representation
     */  
    @Override
	public String toString() {
        return "(" + this.x + ", " + this.y + ", " + this.z + ", " + this.w + ")";  //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
    }

   /**
     * Returns true if all of the data members of Tuple4f t1 are
     * equal to the corresponding data members in this Tuple4f.
     * @param t1  the vector with which the comparison is made
     * @return  true or false
     */  
    public boolean equals(Tuple4f<?> t1)
    {
        try {
        return(this.x == t1.x && this.y == t1.y && this.z == t1.z
            && this.w == t1.w);
        }
        catch (NullPointerException e2) {return false;}
    }

   /**
     * Returns true if the Object t1 is of type Tuple4f and all of the
     * data members of t1 are equal to the corresponding data members in
     * this Tuple4f.
     * @param t1  the object with which the comparison is made
     * @return  true or false
     */
    @Override
	public boolean equals(Object t1)
    {
        try {
           Tuple4f<?> t2 = (Tuple4f<?>) t1;
           return(this.x == t2.x && this.y == t2.y && 
                  this.z == t2.z && this.w == t2.w);
        }
        catch (NullPointerException e2) {return false;}
        catch (ClassCastException   e1) {return false;}
    }


   /**
     * Returns true if the L-infinite distance between this tuple
     * and tuple t1 is less than or equal to the epsilon parameter, 
     * otherwise returns false.  The L-infinite
     * distance is equal to 
     * MAX[abs(x1-x2), abs(y1-y2), abs(z1-z2), abs(w1-w2)].
     * @param t1  the tuple to be compared to this tuple
     * @param epsilon  the threshold value  
     * @return  true or false
     */
    public boolean epsilonEquals(Tuple4f<?> t1, float epsilon)
    {
       float diff;

       diff = this.x - t1.x;
       if(Float.isNaN(diff)) return false;
       if((diff<0?-diff:diff) > epsilon) return false;

       diff = this.y - t1.y;
       if(Float.isNaN(diff)) return false;
       if((diff<0?-diff:diff) > epsilon) return false;

       diff = this.z - t1.z;
       if(Float.isNaN(diff)) return false;
       if((diff<0?-diff:diff) > epsilon) return false;

       diff = this.w - t1.w;
       if(Float.isNaN(diff)) return false;
       if((diff<0?-diff:diff) > epsilon) return false;

       return true;
    }


    /**
     * Returns a hash code value based on the data values in this
     * object.  Two different Tuple4f objects with identical data values
     * (i.e., Tuple4f.equals returns true) will return the same hash
     * code value.  Two objects with different data members may return the
     * same hash value, although this is not likely.
     * @return the integer hash code value
     */  
    @Override
	public int hashCode() {
	long bits = 1L;
	bits = 31L * bits + Float.floatToIntBits(this.x);
	bits = 31L * bits + Float.floatToIntBits(this.y);
	bits = 31L * bits + Float.floatToIntBits(this.z);
	bits = 31L * bits + Float.floatToIntBits(this.w);
	return (int) (bits ^ (bits >> 32));
    }


  /**
    *  Clamps the tuple parameter to the range [low, high] and 
    *  places the values into this tuple.  
    *  @param min   the lowest value in the tuple after clamping
    *  @param max  the highest value in the tuple after clamping 
    *  @param t   the source tuple, which will not be modified
    */
   public final void clamp(float min, float max, Tuple4f<?> t)
   {
        if( t.x > max ) {
          this.x = max;
        } else if( t.x < min ){
          this.x = min;
        } else { 
          this.x = t.x;
        }
 
        if( t.y > max ) {
          this.y = max;
        } else if( t.y < min ){
          this.y = min;
        } else {
          this.y = t.y;
        }
 
        if( t.z > max ) {
          this.z = max;
        } else if( t.z < min ){
          this.z = min;
        } else {
          this.z = t.z;
        }

        if( t.w > max ) {
          this.w = max;
        } else if( t.w < min ){
          this.w = min;
        } else {
          this.w = t.w;
        }

   }


  /** 
    *  Clamps the minimum value of the tuple parameter to the min 
    *  parameter and places the values into this tuple.
    *  @param min   the lowest value in the tuple after clamping 
    *  @param t   the source tuple, which will not be modified
    */   
   public final void clampMin(float min, Tuple4f<?> t) 
   { 
        if( t.x < min ) {
          this.x = min;
        } else {
          this.x = t.x;
        }
 
        if( t.y < min ) {
          this.y = min;
        } else {
          this.y = t.y;
        }
 
        if( t.z < min ) {
          this.z = min;
        } else {
          this.z = t.z;
        }
 
        if( t.w < min ) {
          this.w = min;
        } else {
          this.w = t.w;
        }
 

   } 


  /**  
    *  Clamps the maximum value of the tuple parameter to the max 
    *  parameter and places the values into this tuple.
    *  @param max   the highest value in the tuple after clamping  
    *  @param t   the source tuple, which will not be modified
    */    
   public final void clampMax(float max, Tuple4f<?> t)  
   {  
        if( t.x > max ) {
          this.x = max;
        } else {
          this.x = t.x;
        }
 
        if( t.y > max ) {
          this.y = max;
        } else {
          this.y = t.y;
        }
 
        if( t.z > max ) {
          this.z = max;
        } else {
          this.z = t.z;
        }
 
        if( t.w > max ) {
          this.w = max;
        } else {
          this.w = t.z;
        }

   } 


  /**  
    *  Sets each component of the tuple parameter to its absolute 
    *  value and places the modified values into this tuple.
    *  @param t   the source tuple, which will not be modified
    */    
  public final void absolute(Tuple4f<?> t)
  {
       this.x = Math.abs(t.x);
       this.y = Math.abs(t.y);
       this.z = Math.abs(t.z);
       this.w = Math.abs(t.w);
  } 


  /**
    *  Clamps this tuple to the range [low, high].
    *  @param min  the lowest value in this tuple after clamping
    *  @param max  the highest value in this tuple after clamping
    */
   @Override
public final void clamp(float min, float max) {
	   clamp(min, max);
   }

 
  /**
    *  Clamps the minimum value of this tuple to the min parameter.
    *  @param min   the lowest value in this tuple after clamping
    */
   @Override
public final void clampMin(float min)
   { 
      if( this.x < min ) this.x=min;
      if( this.y < min ) this.y=min;
      if( this.z < min ) this.z=min;
      if( this.w < min ) this.w=min;

   } 
 
 
  /**
    *  Clamps the maximum value of this tuple to the max parameter.
    *  @param max   the highest value in the tuple after clamping
    */
   @Override
public final void clampMax(float max)
   { 
      if( this.x > max ) this.x=max;
      if( this.y > max ) this.y=max;
      if( this.z > max ) this.z=max;
      if( this.w > max ) this.w=max;

   }


  /**
    *  Sets each component of this tuple to its absolute value.
    */
  @Override
public final void absolute()
  {
     this.x = Math.abs(this.x);
     this.y = Math.abs(this.y);
     this.z = Math.abs(this.z);
     this.w = Math.abs(this.w);
  }


  /**  
    *  Linearly interpolates between tuples t1 and t2 and places the 
    *  result into this tuple:  this = (1-alpha)*t1 + alpha*t2.
    *  @param t1  the first tuple
    *  @param t2  the second tuple  
    *  @param alpha  the alpha interpolation parameter  
    */   
  public void interpolate(Tuple4f<?> t1, Tuple4f<?> t2, float alpha) 
  { 
           this.x = (1-alpha)*t1.x + alpha*t2.x;
           this.y = (1-alpha)*t1.y + alpha*t2.y;
           this.z = (1-alpha)*t1.z + alpha*t2.z;
           this.w = (1-alpha)*t1.w + alpha*t2.w;

  } 
 
 
  /**   
    *  Linearly interpolates between this tuple and tuple t1 and 
    *  places the result into this tuple:  this = (1-alpha)*this + alpha*t1. 
    *  @param t1  the first tuple 
    *  @param alpha  the alpha interpolation parameter   
    */    
  public void interpolate(Tuple4f<?> t1, float alpha)  
  {  
     this.x = (1-alpha)*this.x + alpha*t1.x;
     this.y = (1-alpha)*this.y + alpha*t1.y;
     this.z = (1-alpha)*this.z + alpha*t1.z;
     this.w = (1-alpha)*this.w + alpha*t1.w;

  }  
 
	/** {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public T clone() {
		try {
			return (T)super.clone();
		}
		catch(CloneNotSupportedException e) {
			throw new Error(e);
		}
	}

    /**
	 * Get the <i>x</i> coordinate.
	 * 
	 * @return the <i>x</i> coordinate.
	 */
	@Override
	public final float getX() {
		return this.x;
	}


	/**
	 * Set the <i>x</i> coordinate.
	 * 
	 * @param x  value to <i>x</i> coordinate.
	 * 
	 * @since vecmath 1.5
	 */
	@Override
	public final void setX(float x) {
		this.x = x;
	}


	/**
	 * Get the <i>y</i> coordinate.
	 * 
	 * @return the <i>y</i> coordinate.
	 * 
	 * @since vecmath 1.5
	 */
	@Override
	public final float getY() {
		return this.y;
	}


	/**
	 * Set the <i>y</i> coordinate.
	 * 
	 * @param y value to <i>y</i> coordinate.
	 * 
	 * @since vecmath 1.5
	 */
	@Override
	public final void setY(float y) {
		this.y = y;
	}

	/**
	 * Get the <i>z</i> coordinate.
	 * 
	 * @return the <i>z</i> coordinate.
	 * 
	 * @since vecmath 1.5
	 */
	@Override
	public final float getZ() {
		return this.z;
	}


	/**
	 * Set the <i>z</i> coordinate.
	 * 
	 * @param z value to <i>z</i> coordinate.
	 * 
	 * @since vecmath 1.5
	 */
	@Override
	public final void setZ(float z) {
		this.z = z;
	}


	/**
	 * Get the <i>w</i> coordinate.
	 * 
	 * @return the <i>w</i> coordinate.
	 * 
	 * @since vecmath 1.5
	 */
	@Override
	public final float getW() {
		return this.w;
	}


	/**
	 * Set the <i>w</i> coordinate.
	 * 
	 * @param w value to <i>w</i> coordinate.
	 * 
	 * @since vecmath 1.5
	 */
	@Override
	public final void setW(float w) {
		this.w = w;
	}


	@Override
	public void absolute(T t) {
		t.set(Math.abs(this.x), Math.abs(this.y), Math.abs(this.z), Math.abs(this.w));
	}


	@Override
	public void add(int x, int y, int z, int w) {
		this.x += x;
		this.y += y;
		this.z += z;
		this.w += w;
	}


	@Override
	public void add(float x, float y, float z, float w) {
		this.x += x;
		this.y += y;
		this.z += z;
		this.w += w;
	}


	@Override
	public void addX(int x) {
		this.x += x;
	}


	@Override
	public void addX(float x) {
		this.x += x;
	}


	@Override
	public void addY(int y) {
		this.y += y;
	}


	@Override
	public void addY(float y) {
		this.y += y;
	}


	@Override
	public void addZ(int z) {
		this.z += z;
	}


	@Override
	public void addZ(float z) {
		this.z += z;
	}


	@Override
	public void addW(int w) {
		this.w += w;
	}


	@Override
	public void addW(float w) {
		this.w += w;
	}


	@Override
	public void clamp(int min, int max) {
		
       if( this.x > max ) {
           this.x = max;
         } else if( this.x < min ){
           this.x = min;
         }
  
         if( this.y > max ) {
           this.y = max;
         } else if( this.y < min ){
           this.y = min;
         }
  
         if( this.z > max ) {
           this.z = max;
         } else if( this.z < min ){
           this.z = min;
         }
  
         if( this.w > max ) {
           this.w = max;
         } else if( this.w < min ){
           this.w = min;
         }
	}


	@Override
	public void clampMin(int min) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void clampMax(int max) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void clamp(int min, int max, T t) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void clamp(float min, float max, T t) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void clampMin(int min, T t) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void clampMin(float min, T t) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void clampMax(int max, T t) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void clampMax(float max, T t) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void get(T t) {
		t.set(this.x, this.y, this.z, this.w);
	}


	@Override
	public void get(int[] t) {
		t[0] = (int)this.x;
		t[1] = (int)this.y;
		t[2] = (int)this.z;
		t[3] = (int)this.w;
	}


	@Override
	public void negate(T t1) {
		this.x = -t1.getX();
		this.y = -t1.getY();
		this.z = -t1.getZ();
		this.w = -t1.getW();
	}


	@Override
	public void scale(int s, T t1) {
		this.x = s * t1.getX();
		this.y = s * t1.getY();
		this.z = s * t1.getZ();
		this.w = s * t1.getW();
	}


	@Override
	public void scale(float s, T t1) {
		this.x = (s * t1.getX());
		this.y = (s * t1.getY());
		this.z = (s * t1.getZ());
		this.w = (s * t1.getW());
	}


	@Override
	public void scale(int s) {
		this.x = s * this.x;
		this.y = s * this.y;
		this.z = s * this.z;
		this.w = s * this.w;
	}


	@Override
	public void set(Tuple4D<?> t1) {
		this.x = t1.getX();
		this.y = t1.getY();
		this.z = t1.getZ();
		this.w = t1.getW();
		
	}


	@Override
	public void set(int x, int y, int z, int w) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
		
	}


	@Override
	public void set(int[] t) {
		this.x = t[0];
		this.y = t[1];
		this.z = t[2];
		this.w = t[3];
	}


	@Override
	public int x() {
		return (int) this.x;
	}


	@Override
	public void setX(int x) {
		this.x = x;		
	}


	@Override
	public int y() {
		return (int) this.y;
	}


	@Override
	public void setY(int y) {
		this.y = y;
	}


	@Override
	public int z() {
		return (int) this.z;
	}


	@Override
	public int w() {
		return (int) this.w;
	}


	@Override
	public void setZ(int z) {
		this.z = z;
	}


	@Override
	public void setW(int w) {
		this.w = w;
	}


	@Override
	public void sub(int x, int y, int z, int w) {
		this.x -= x;
		this.y -= y;
		this.z -= z;
		this.w -= w;
	}


	@Override
	public void sub(float x, float y, float z, float w) {
		this.x -= x;
		this.y -= y;
		this.z -= z;
		this.w -= w;
	}


	@Override
	public void subX(int x) {
		this.x -= x;
	}


	@Override
	public void subX(float x) {
		this.x -= x;
	}


	@Override
	public void subY(int y) {
		this.y -= y;
	}


	@Override
	public void subY(float y) {
		this.y -= y;
	}


	@Override
	public void subZ(int z) {
		this.z -= z;
	}


	@Override
	public void subZ(float z) {
		this.z -= z;
	}


	@Override
	public void subW(int w) {
		this.w -= w;
	}


	@Override
	public void subW(float w) {
		this.w -= w;
	}


	@Override
	public void interpolate(T t1, T t2, float alpha) {
		this.x = ((1f-alpha)*t1.getX() + alpha*t2.getX());
		this.y = ((1f-alpha)*t1.getY() + alpha*t2.getY());
		this.z = ((1f-alpha)*t1.getZ() + alpha*t2.getZ());
		this.w = ((1f-alpha)*t1.getW() + alpha*t2.getW());
	}


	@Override
	public void interpolate(T t1, float alpha) {
		this.x = ((1f-alpha)*this.x + alpha*t1.getX());
		this.y = ((1f-alpha)*this.y + alpha*t1.getY());
		this.z = ((1f-alpha)*this.z + alpha*t1.getZ());
		this.w = ((1f-alpha)*this.w + alpha*t1.getW());
	}


	@Override
	public boolean equals(Tuple4D<?> t1) {
		try {
			return(this.x == t1.getX() && this.y == t1.getY() && this.z == t1.getZ() && this.w == t1.getW());
		}
		catch (NullPointerException e2) {
			return false;
		}
	}


	@Override
	public boolean epsilonEquals(T t1, float epsilon) {
		float diff;

		diff = this.x - t1.getX();
		if(Float.isNaN(diff)) return false;
		if((diff<0?-diff:diff) > epsilon) return false;

		diff = this.y - t1.getY();
		if(Float.isNaN(diff)) return false;
		if((diff<0?-diff:diff) > epsilon) return false;

		diff = this.z - t1.getZ();
		if(Float.isNaN(diff)) return false;
		if((diff<0?-diff:diff) > epsilon) return false;
		
		diff = this.w - t1.getW();
		if(Float.isNaN(diff)) return false;
		if((diff<0?-diff:diff) > epsilon) return false;

		return true;
	}
}
