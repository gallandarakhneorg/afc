/**
 * 
 * org.arakhne.afc.math.geometry.d2.FunctionalTuple3D.java
 *
 * Copyright (c) 2008-10, Multiagent Team - Systems and Transportation Laboratory (SeT)
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of the Systems and Transportation Laboratory ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with the SeT.
 * 
 * http://www.multiagent.fr/
 *
 * Primary author : Olivier LAMOTTE (olivier.lamotte@utbm.fr) - 2015
 *
 */
package org.arakhne.afc.math.geometry.d3;

import org.eclipse.xtext.xbase.lib.Pure;

/**
 * @author Olivier LAMOTTE (olivier.lamotte@utbm.fr)
 *
 */
public interface FunctionalTuple3D<TT extends Tuple3D<? super TT>> extends Tuple3D<TT> {

	/* (non-Javadoc)
	 * @see org.arakhne.afc.math.geometry.d3.Tuple3D#absolute()
	 */
	@Override
	default void absolute() {
		this.setX(Math.abs(this.getX()));
		this.setY(Math.abs(this.getY()));
		this.setZ(Math.abs(this.getZ()));
	}

	/* (non-Javadoc)
	 * @see org.arakhne.afc.math.geometry.d3.Tuple3D#absolute(org.arakhne.afc.math.geometry.d3.Tuple3D)
	 */
	@Override
	default void absolute(TT t) {
		t.set(Math.abs(this.getX()), Math.abs(this.getY()), Math.abs(this.getZ()));
	}

	/* (non-Javadoc)
	 * @see org.arakhne.afc.math.geometry.d3.Tuple3D#add(int, int, int)
	 */
	@Override
	default void add(int x, int y, int z) {
		this.setX(this.getX() + x);
		this.setY(this.getY() + y);
		this.setZ(this.getZ() + z);
	}

	/* (non-Javadoc)
	 * @see org.arakhne.afc.math.geometry.d3.Tuple3D#add(double, double, double)
	 */
	@Override
	default void add(double x, double y, double z) {
		this.setX(this.getX() + x);
		this.setY(this.getY() + y);
		this.setZ(this.getZ() + z);
	}

	/* (non-Javadoc)
	 * @see org.arakhne.afc.math.geometry.d3.Tuple3D#addX(int)
	 */
	@Override
	default void addX(int x) {
		this.setX(this.getX() + x);		
	}

	/* (non-Javadoc)
	 * @see org.arakhne.afc.math.geometry.d3.Tuple3D#addX(double)
	 */
	@Override
	default void addX(double x) {
		this.setX(this.getX() + x);		
	}

	/* (non-Javadoc)
	 * @see org.arakhne.afc.math.geometry.d3.Tuple3D#addY(int)
	 */
	@Override
	default void addY(int y) {
		this.setY(this.getY() + y);		
	}

	/* (non-Javadoc)
	 * @see org.arakhne.afc.math.geometry.d3.Tuple3D#addY(double)
	 */
	@Override
	default void addY(double y) {
		this.setY(this.getY() + y);		
	}

	/* (non-Javadoc)
	 * @see org.arakhne.afc.math.geometry.d3.Tuple3D#addZ(int)
	 */
	@Override
	default void addZ(int z) {
		this.setZ(this.getZ() + z);		
	}

	/* (non-Javadoc)
	 * @see org.arakhne.afc.math.geometry.d3.Tuple3D#addZ(double)
	 */
	@Override
	default void addZ(double z) {
		this.setZ(this.getZ() + z);		
	}

	/* (non-Javadoc)
	 * @see org.arakhne.afc.math.geometry.d3.Tuple3D#clamp(int, int)
	 */
	@Override
	default void clamp(int min, int max) {
		if (this.getX() < min) this.setX(min);
		else if (this.getX() > max) this.setX(max);
		if (this.getY() < min) this.setY(min);
		else if (this.getY() > max) this.setY(max);
	}

	/* (non-Javadoc)
	 * @see org.arakhne.afc.math.geometry.d3.Tuple3D#clamp(double, double)
	 */
	@Override
	default void clamp(double min, double max) {
		if (this.getX() < min) this.setX(min);
		else if (this.getX() > max) this.setX(max);
		if (this.getY() < min) this.setY(min);
		else if (this.getY() > max) this.setY(max);
	}

	/* (non-Javadoc)
	 * @see org.arakhne.afc.math.geometry.d3.Tuple3D#clampMin(int)
	 */
	@Override
	default void clampMin(int min) {
		if (this.getX() < min) this.setX(min);
		if (this.getY() < min) this.setY(min);		
	}

	/* (non-Javadoc)
	 * @see org.arakhne.afc.math.geometry.d3.Tuple3D#clampMin(double)
	 */
	@Override
	default void clampMin(double min) {
		if (this.getX() < min) this.setX(min);
		if (this.getY() < min) this.setY(min);		
	}

	/* (non-Javadoc)
	 * @see org.arakhne.afc.math.geometry.d3.Tuple3D#clampMax(int)
	 */
	@Override
	default void clampMax(int max) {
		if (this.getX() > max) this.setX(max);
		if (this.getY() > max) this.setY(max);
	}

	/* (non-Javadoc)
	 * @see org.arakhne.afc.math.geometry.d3.Tuple3D#clampMax(double)
	 */
	@Override
	default void clampMax(double max) {
		if (this.getX() > max) this.setX(max);
		if (this.getY() > max) this.setY(max);
	}

	/* (non-Javadoc)
	 * @see org.arakhne.afc.math.geometry.d3.Tuple3D#clamp(int, int, org.arakhne.afc.math.geometry.d3.Tuple3D)
	 */
	@Override
	default void clamp(int min, int max, TT t) {
		if (this.getX() < min) t.setX(min);
		else if (this.getX() > max) t.setX(max);
		if (this.getY() < min) t.setY(min);
		else if (this.getY() > max) t.setY(max);
	}

	/* (non-Javadoc)
	 * @see org.arakhne.afc.math.geometry.d3.Tuple3D#clamp(double, double, org.arakhne.afc.math.geometry.d3.Tuple3D)
	 */
	@Override
	default void clamp(double min, double max, TT t) {
		if (this.getX() < min) t.setX(min);
		else if (this.getX() > max) t.setX(max);
		if (this.getY() < min) t.setY(min);
		else if (this.getY() > max) t.setY(max);
	}

	/* (non-Javadoc)
	 * @see org.arakhne.afc.math.geometry.d3.Tuple3D#clampMin(int, org.arakhne.afc.math.geometry.d3.Tuple3D)
	 */
	@Override
	default void clampMin(int min, TT t) {
		if (this.getX() < min) t.setX(min);
		if (this.getY() < min) t.setY(min);
	}

	/* (non-Javadoc)
	 * @see org.arakhne.afc.math.geometry.d3.Tuple3D#clampMin(double, org.arakhne.afc.math.geometry.d3.Tuple3D)
	 */
	@Override
	default void clampMin(double min, TT t) {
		if (this.getX() < min) t.setX(min);
		if (this.getY() < min) t.setY(min);
	}

	/* (non-Javadoc)
	 * @see org.arakhne.afc.math.geometry.d3.Tuple3D#clampMax(int, org.arakhne.afc.math.geometry.d3.Tuple3D)
	 */
	@Override
	default void clampMax(int max, TT t) {
		if (this.getX() > max) t.setX(max);
		if (this.getY() > max) t.setY(max);
	}

	/* (non-Javadoc)
	 * @see org.arakhne.afc.math.geometry.d3.Tuple3D#clampMax(double, org.arakhne.afc.math.geometry.d3.Tuple3D)
	 */
	@Override
	default void clampMax(double max, TT t) {
		if (this.getX() > max) t.setX(max);
		if (this.getY() > max) t.setY(max);
	}

	/* (non-Javadoc)
	 * @see org.arakhne.afc.math.geometry.d3.Tuple3D#get(org.arakhne.afc.math.geometry.d3.Tuple3D)
	 */
	@Override
	default void get(TT t) {
		t.set(this.getX(), this.getY(), this.getZ());
	}

	/* (non-Javadoc)
	 * @see org.arakhne.afc.math.geometry.d3.Tuple3D#get(int[])
	 */
	@Override
	default void get(int[] t) {
		t[0] = (int)this.getX();
		t[1] = (int)this.getY();
		t[2] = (int)this.getZ();
	}

	/* (non-Javadoc)
	 * @see org.arakhne.afc.math.geometry.d3.Tuple3D#get(double[])
	 */
	@Override
	default void get(double[] t) {
		t[0] = this.getX();
		t[1] = this.getY();
		t[2] = this.getZ();
	}

	/* (non-Javadoc)
	 * @see org.arakhne.afc.math.geometry.d3.Tuple3D#negate(org.arakhne.afc.math.geometry.d3.Tuple3D)
	 */
	@Override
	default void negate(TT t1) {
		this.setX(-t1.getX());
		this.setY(-t1.getY());
		this.setZ(-t1.getZ());
	}

	/* (non-Javadoc)
	 * @see org.arakhne.afc.math.geometry.d3.Tuple3D#negate()
	 */
	@Override
	default void negate() {
		this.setX(-this.getX());
		this.setY(-this.getY());
		this.setZ(-this.getZ());
	}

	/* (non-Javadoc)
	 * @see org.arakhne.afc.math.geometry.d3.Tuple3D#scale(int, org.arakhne.afc.math.geometry.d3.Tuple3D)
	 */
	@Override
	default void scale(int s, TT t1) {
		this.setX(s * t1.getX());
		this.setY(s * t1.getY());
		this.setZ(s * t1.getZ());
	}

	/* (non-Javadoc)
	 * @see org.arakhne.afc.math.geometry.d3.Tuple3D#scale(double, org.arakhne.afc.math.geometry.d3.Tuple3D)
	 */
	@Override
	default void scale(double s, TT t1) {
		this.setX(s * t1.getX());
		this.setY(s * t1.getY());
		this.setZ(s * t1.getZ());
	}

	/* (non-Javadoc)
	 * @see org.arakhne.afc.math.geometry.d3.Tuple3D#scale(int)
	 */
	@Override
	default void scale(int s) {
		this.setX(s * this.getX());
		this.setY(s * this.getY());
		this.setZ(s * this.getZ());
	}

	/* (non-Javadoc)
	 * @see org.arakhne.afc.math.geometry.d3.Tuple3D#scale(double)
	 */
	@Override
	default void scale(double s) {
		this.setX(s * this.getX());
		this.setY(s * this.getY());
		this.setZ(s * this.getZ());
	}

	/* (non-Javadoc)
	 * @see org.arakhne.afc.math.geometry.d3.Tuple3D#set(org.arakhne.afc.math.geometry.d3.Tuple3D)
	 */
	@Override
	default void set(Tuple3D<?> t1) {
		this.setX(t1.getX());
		this.setY(t1.getY());
		this.setZ(t1.getZ());
	}

	/* (non-Javadoc)
	 * @see org.arakhne.afc.math.geometry.d3.Tuple3D#set(int, int, int)
	 */
	@Override
	default void set(int x, int y, int z) {
		this.setX(x);
		this.setY(y);
		this.setZ(z);
	}

	/* (non-Javadoc)
	 * @see org.arakhne.afc.math.geometry.d3.Tuple3D#set(int[])
	 */
	@Override
	default void set(int[] t) {
		this.setX(t[0]);
		this.setY(t[1]);
		this.setZ(t[2]);
	}

	/* (non-Javadoc)
	 * @see org.arakhne.afc.math.geometry.d3.Tuple3D#set(double[])
	 */
	@Override
	default void set(double[] t) {
		this.setX(t[0]);
		this.setY(t[1]);
		this.setZ(t[2]);
	}

	/* (non-Javadoc)
	 * @see org.arakhne.afc.math.geometry.d3.Tuple3D#x()
	 */
	@Pure
	@Override
	default int ix() {
		return (int)this.getX();
	}

	/* (non-Javadoc)
	 * @see org.arakhne.afc.math.geometry.d3.Tuple3D#setX(int)
	 */
	@Override
	default void setX(int x) {
		this.setX((double)x);
	}

	/* (non-Javadoc)
	 * @see org.arakhne.afc.math.geometry.d3.Tuple3D#y()
	 */
	@Pure
	@Override
	default int iy() {
		return (int)this.getY();
	}

	/* (non-Javadoc)
	 * @see org.arakhne.afc.math.geometry.d3.Tuple3D#setY(int)
	 */
	@Override
	default void setY(int y) {
		this.setY((double)y);
	}

	/* (non-Javadoc)
	 * @see org.arakhne.afc.math.geometry.d3.Tuple3D#z()
	 */
	@Pure
	@Override
	default int iz() {
		return (int)this.getZ();
	}

	/* (non-Javadoc)
	 * @see org.arakhne.afc.math.geometry.d3.Tuple3D#setZ(int)
	 */
	@Override
	default void setZ(int z) {
		this.setZ((double)z);
	}

	/* (non-Javadoc)
	 * @see org.arakhne.afc.math.geometry.d3.Tuple3D#sub(int, int, int)
	 */
	@Override
	default void sub(int x, int y, int z) {
		this.setX(this.getX() - x);
		this.setY(this.getY() - y);
		this.setZ(this.getZ() - z);
	}

	/* (non-Javadoc)
	 * @see org.arakhne.afc.math.geometry.d3.Tuple3D#sub(double, double, double)
	 */
	@Override
	default void sub(double x, double y, double z) {
		this.setX(this.getX() - x);
		this.setY(this.getY() - y);
		this.setZ(this.getZ() - z);		
	}

	/* (non-Javadoc)
	 * @see org.arakhne.afc.math.geometry.d3.Tuple3D#subX(int)
	 */
	@Override
	default void subX(int x) {
		this.setX(this.getX() - x);		
	}

	/* (non-Javadoc)
	 * @see org.arakhne.afc.math.geometry.d3.Tuple3D#subX(double)
	 */
	@Override
	default void subX(double x) {
		this.setX(this.getX() - x);		
	}

	/* (non-Javadoc)
	 * @see org.arakhne.afc.math.geometry.d3.Tuple3D#subY(int)
	 */
	@Override
	default void subY(int y) {
		this.setY(this.getY() - y);
	}

	/* (non-Javadoc)
	 * @see org.arakhne.afc.math.geometry.d3.Tuple3D#subY(double)
	 */
	@Override
	default void subY(double y) {
		this.setY(this.getY() - y);
	}

	/* (non-Javadoc)
	 * @see org.arakhne.afc.math.geometry.d3.Tuple3D#subZ(int)
	 */
	@Override
	default void subZ(int z) {
		this.setZ(this.getZ() - z);	
	}

	/* (non-Javadoc)
	 * @see org.arakhne.afc.math.geometry.d3.Tuple3D#subZ(double)
	 */
	@Override
	default void subZ(double z) {
		this.setZ(this.getZ() - z);	
	}

	/* (non-Javadoc)
	 * @see org.arakhne.afc.math.geometry.d3.Tuple3D#interpolate(org.arakhne.afc.math.geometry.d3.Tuple3D, org.arakhne.afc.math.geometry.d3.Tuple3D, double)
	 */
	@Override
	default void interpolate(TT t1, TT t2, double alpha) {
		this.setX(((1f-alpha)*t1.getX() + alpha*t2.getX()));
		this.setY(((1f-alpha)*t1.getY() + alpha*t2.getY()));
		this.setZ(((1f-alpha)*t1.getZ() + alpha*t2.getZ()));
	}

	/* (non-Javadoc)
	 * @see org.arakhne.afc.math.geometry.d3.Tuple3D#interpolate(org.arakhne.afc.math.geometry.d3.Tuple3D, double)
	 */
	@Override
	default void interpolate(TT t1, double alpha) {
		this.setX(((1f-alpha)*this.getX() + alpha*t1.getX()));
		this.setY(((1f-alpha)*this.getY() + alpha*t1.getY()));
		this.setZ(((1f-alpha)*this.getZ() + alpha*t1.getZ()));
	}

	/* (non-Javadoc)
	 * @see org.arakhne.afc.math.geometry.d3.Tuple3D#equals(org.arakhne.afc.math.geometry.d3.Tuple3D)
	 */
	@Pure
	@Override
	default boolean equals(Tuple3D<?> t1) {
		try {
			return(this.getX() == t1.getX() && this.getY() == t1.getY() && this.getZ() == t1.getZ());
		}
		catch (NullPointerException e2) {
			return false;
		}
	}
	
	/* (non-Javadoc)
	 * @see org.arakhne.afc.math.geometry.d3.Tuple3D#epsilonEquals(org.arakhne.afc.math.geometry.d3.Tuple3D, double)
	 */
	@Pure
	@Override
	default boolean epsilonEquals(TT t1, double epsilon) {
		double diff;

		diff = this.getX() - t1.getX();
		if(Double.isNaN(diff)) return false;
		if((diff<0?-diff:diff) > epsilon) return false;

		diff = this.getY() - t1.getY();
		if(Double.isNaN(diff)) return false;
		if((diff<0?-diff:diff) > epsilon) return false;

		diff = this.getZ() - t1.getZ();
		if(Double.isNaN(diff)) return false;
		if((diff<0?-diff:diff) > epsilon) return false;

		return true;
	}
	
}
