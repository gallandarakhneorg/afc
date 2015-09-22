/**
 * 
 * fr.utbm.v3g.core.math.Tuple2d.java
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
package org.arakhne.afc.math.geometry.d2.continuous;

import org.arakhne.afc.math.geometry.d2.FunctionalTuple2D;
import org.arakhne.afc.math.geometry.d2.Tuple2D;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

/**
 * @author Olivier LAMOTTE (olivier.lamotte@utbm.fr)
 *
 */
public class Tuple2d<TT extends Tuple2D<? super TT>> implements FunctionalTuple2D<TT> {

	private static final long serialVersionUID = 2754372505837958112L;

	protected DoubleProperty xProperty;
	protected DoubleProperty yProperty;
	
	public Tuple2d() {
		this(0, 0);
	}
	
	public Tuple2d(Tuple2d<?> t) {
		this(t.getX(), t.getY());
	}
	
	public Tuple2d(double x, double y) {
		this.xProperty = new SimpleDoubleProperty(x);
		this.yProperty = new SimpleDoubleProperty(y);
	}
	
	public DoubleProperty xProperty() {
		return this.xProperty;
	}
	public DoubleProperty yProperty() {
		return this.yProperty;
	}

	/* (non-Javadoc)
	 * @see org.arakhne.afc.math.geometry.d2.continuous.AbstractTuple2D#setX(double)
	 */
	@Override
	public void setX(double x) {
		this.xProperty.set(x);
	}

	/* (non-Javadoc)
	 * @see org.arakhne.afc.math.geometry.d2.continuous.AbstractTuple2D#setY(double)
	 */
	@Override
	public void setY(double y) {
		this.yProperty.set(y);
	}

	/* (non-Javadoc)
	 * @see org.arakhne.afc.math.geometry.d2.continuous.AbstractTuple2D#getX()
	 */
	@Override
	public double getX() {
		return this.xProperty.doubleValue();
	}

	/* (non-Javadoc)
	 * @see org.arakhne.afc.math.geometry.d2.continuous.AbstractTuple2D#getY()
	 */
	@Override
	public double getY() {
		return this.yProperty.doubleValue();
	}
	
	/* (non-Javadoc)
	 * @see org.arakhne.afc.math.geometry.d2.Tuple2D#clone()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public TT clone() {
		try {
			return (TT)super.clone();
		}
		catch(CloneNotSupportedException e) {
			throw new Error(e);
		}
	}
	
	/* (non-Javadoc)
	 * @see org.arakhne.afc.math.geometry.d2.Tuple2D#hashCode()
	 */
	@Override
	public int hashCode() {
		long bits = 1;
		bits = 31 * bits + Double.doubleToLongBits(this.getX());
		bits = 31 * bits + Double.doubleToLongBits(this.getY());
		int b = (int) bits;
		return b ^ (b >> 32);
	}

	/* (non-Javadoc)
	 * @see org.arakhne.afc.math.geometry.d2.Tuple2D#toString()
	 */
	@Override
	public String toString() {
		return "(" //$NON-NLS-1$
				+this.getX()
				+";" //$NON-NLS-1$
				+this.getY()
				+")"; //$NON-NLS-1$
	}
	
}
