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

import org.arakhne.afc.math.MathBounds;
import org.arakhne.afc.math.MathException;

/**
 * Law that representes a gaussian density.
 * <p>
 * Reference:
 * <a href="http://mathworld.wolfram.com/NormalDistribution.html">Normal Distribution</a>.
 * <p>
 * This class uses the gaussian random number distribution provided by {@link Random}. 
 * 
 * @author $Author: cbohrhauer$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class GaussianStochasticLaw extends StochasticLaw {
	
	private static final float SQRT2PI = (float) Math.sqrt(2.f*Math.PI);
	
	/** Replies a random value that respect
	 * the current stochastic law.
	 * 
	 * @param mean is the mean of the normal distribution.
	 * @param standardDeviation is the standard deviation associated to the nromal distribution.
	 * @return a value depending of the stochastic law parameters
	 * @throws MathException 
	 */
	public static float random(float mean, float standardDeviation) throws MathException {
		return StochasticGenerator.generateRandomValue(new GaussianStochasticLaw(mean, standardDeviation));
	}

	private float mean;
	private float standardDeviation;

	/**
	 * Construct a law with the following parameters.
	 * <ul>
	 * <li><code>mean</code></li>
	 * <li><code>standardDeviation</code></li>
	 * </ul>
	 * 
	 * @param parameters is the set of accepted paramters.
	 * @throws OutsideDomainException when standardDeviation is not positive or nul.
	 * @throws LawParameterNotFoundException when a mandatory parameter was not found.
	 */
	public GaussianStochasticLaw(Map<String,String> parameters) throws OutsideDomainException, LawParameterNotFoundException {
		this.mean = paramFloat("mean",parameters); //$NON-NLS-1$
		this.standardDeviation = paramFloat("standardDeviation",parameters); //$NON-NLS-1$
		if (this.standardDeviation<=0) throw new OutsideDomainException(this.standardDeviation);
	}

	/**
	 * @param mean is the mean of the normal distribution.
	 * @param standardDeviation is the standard deviation associated to the nromal distribution.
	 * @throws OutsideDomainException when standardDeviation is not positive or nul.
	 */
	public GaussianStochasticLaw(float mean, float standardDeviation) throws OutsideDomainException {
		if (standardDeviation<=0) throw new OutsideDomainException(standardDeviation);
		this.mean = mean;
		this.standardDeviation = standardDeviation;
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @return {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuilder b = new StringBuilder();
		b.append("NORMAL(mean="); //$NON-NLS-1$
		b.append(this.mean);
		b.append(";deviation="); //$NON-NLS-1$
		b.append(this.standardDeviation);
		b.append(')');
		return b.toString();
	}
	
	/** {@inheritDoc}
	 */
	@Override
	public float f(float x)  throws MathException {
		float xm = x - this.mean;
		xm = xm * xm;
		return (float) ((1.f/(this.standardDeviation*SQRT2PI)) * Math.exp((-xm)/(2.f*this.standardDeviation*this.standardDeviation))); 
	}

	/** {@inheritDoc}
	 */
	@Override
	public MathBounds[] getBounds() {
		return MathBounds.createInfinitySet();
	}
	
	/** Replies the x according to the value of the distribution function.
	 * 
	 * @param u is a value given by the uniform random variable generator {@code U(0,1)}.
	 * @return {@code F<sup>-1</sup>(u)}
	 * @throws MathException in case {@code F<sup>-1</sup>(u)} could not be computed
	 */
	@Override
	public float inverseF(float u) throws MathException {
		return this.standardDeviation*u + this.mean;
	}

	/** Replies the x according to the value of the inverted 
	 * cummulative distribution function {@code F<sup>-1</sup>(u)}
	 * where {@code u = U(0,1)}.
	 * 
	 * @param U is the uniform random variable generator {@code U(0,1)}.
	 * @return {@code F<sup>-1</sup>(u)}
	 * @throws MathException in case {@code F<sup>-1</sup>(u)} could not be computed
	 */
	@Override
	protected final float inverseF(Random U) throws MathException {
		float u = (float) ((U.nextGaussian()+1)/2.f);
		return inverseF(u);
	}

}