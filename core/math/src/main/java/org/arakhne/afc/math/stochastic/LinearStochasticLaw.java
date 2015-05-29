/* 
 * $Id$
 * 
 * Copyright (c) 2013 Christophe BOHRHAUER
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
package org.arakhne.afc.math.stochastic;

import java.util.Map;
import java.util.Random;

import org.arakhne.afc.math.MathFunctionRange;
import org.arakhne.afc.math.MathException;

/**
 * Law that representes a linear density.
 * <p>
 * The linear distribution is based on {@code f(x) = a.x+b}
 * where, if the distribution is ascendent, {@code a>0} and
 * {@code f(minX) = 0}, and if the distribution is descendent,
 * {@code a<0} and {@code f(maxX) = 0}.
 * <p>
 * This class uses the uniform random number distribution provided by {@link Random}. 
 * 
 * @author $Author: cbohrhauer$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class LinearStochasticLaw extends StochasticLaw {
	
	/** Replies a random value that respect
	 * the current stochastic law.
	 * 
	 * @param minX is the lower bound of the distribution
	 * @param maxX is the upper bound of the distribution
	 * @param ascendent indicates of the distribution function is ascendent or not
	 * @return a value depending of the stochastic law parameters
	 * @throws MathException 
	 */
	public static float random(float minX, float maxX, boolean ascendent) throws MathException {
		return StochasticGenerator.generateRandomValue(new LinearStochasticLaw(minX, maxX, ascendent));
	}

	/** Replies a random value that respect
	 * the current stochastic law.
	 * <p>
	 * The used stochastic law is the ascendent linear distribution.
	 * 
	 * @param minX is the lower bound of the distribution
	 * @param maxX is the upper bound of the distribution
	 * @return a value depending of the stochastic law parameters
	 * @throws MathException 
	 */
	public static float random(float minX, float maxX) throws MathException {
		return StochasticGenerator.generateRandomValue(new LinearStochasticLaw(minX, maxX));
	}

	private final boolean ascendent;
	private final float minX;
	private final float maxX;
	private final float delta;

	/**
	 * Construct a law with the following parameters.
	 * <ul>
	 * <li><code>ascendent</code></li>
	 * <li><code>minX</code></li>
	 * <li><code>maxY</code></li>
	 * <li><code>delta</code></li>
	 * </ul>
	 * 
	 * @param parameters is the set of accepted paramters.
	 * @throws LawParameterNotFoundException if the list of parameters does not permits to create the law.
	 */
	public LinearStochasticLaw(Map<String,String> parameters) throws LawParameterNotFoundException {
		this.ascendent = paramBoolean("ascendent",parameters); //$NON-NLS-1$
		this.minX = paramFloat("maxX",parameters); //$NON-NLS-1$
		this.maxX = paramFloat("maxX",parameters); //$NON-NLS-1$
		this.delta = paramFloat("delta",parameters); //$NON-NLS-1$
	}

	/** Create a ascendent linear distribution.
	 * 
	 * @param minX is the lower bound of the distribution
	 * @param maxX is the upper bound of the distribution
	 */
	public LinearStochasticLaw(float minX, float maxX) {
		this(minX, maxX, true);
	}

	/**
	 * @param minX is the lower bound of the distribution
	 * @param maxX is the upper bound of the distribution
	 * @param ascendent indicates of the distribution function is ascendent or not
	 */
	public LinearStochasticLaw(float minX, float maxX, boolean ascendent) {
		float i = minX;
		float a = maxX;
		if (i>a) {
			float t = i;
			i = a;
			a = t;
		}
		
		this.ascendent = ascendent;
		this.minX = i;
		this.maxX = a;
		
		this.delta = this.ascendent ? (this.maxX - this.minX) : (this.minX - this.maxX);
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @return {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuilder b = new StringBuilder();
		b.append("LINEAR(["); //$NON-NLS-1$
		b.append(this.minX);
		b.append(';');
		b.append(this.maxX);
		b.append(';');
		if (this.ascendent)
			b.append("asc"); //$NON-NLS-1$
		else
			b.append("desc"); //$NON-NLS-1$
		b.append("])"); //$NON-NLS-1$
		return b.toString();
	}
	
	/** {@inheritDoc}
	 */
	@Override
	public float f(float x)  throws MathException {
		if ((x<this.minX)||(x>this.maxX))
			throw new OutsideDomainException(x);
		float a = 2.f / (this.delta * this.delta);
		float b;
		if (this.ascendent)
			b = -a * this.minX;
		else
			b = -a * this.maxX;
		return a * x + b;
	}

	/** {@inheritDoc}
	 */
	@Override
	public MathFunctionRange[] getRange() {
		return MathFunctionRange.createSet(this.minX, this.maxX);
	}
	
	/** Replies the x according to the value of the distribution function.
	 * 
	 * @param u is a value given by the uniform random variable generator {@code U(0,1)}.
	 * @return {@code F<sup>-1</sup>(u)}
	 * @throws MathException in case {@code F<sup>-1</sup>(u)} could not be computed
	 */
	@Override
	public float inverseF(float u) throws MathException {
		if (this.ascendent) {
			return (float) (this.delta * Math.sqrt( u ) + this.minX);
		}

		return (float) (this.delta * Math.sqrt( u ) + this.maxX);
	}

}