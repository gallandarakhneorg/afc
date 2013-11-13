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
 * Law that representes a triangular density.
 * <p>
 * Reference:
 * <a href="http://mathworld.wolfram.com/ExponentialDistribution.html">Exponential Distribution</a>.
 * <p>
 * This class uses the uniform random number distribution provided by {@link Random}. 
 * 
 * @author $Author: cbohrhauer$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class ExponentialStochasticLaw extends StochasticLaw {
	
	/** Replies a random value that respect
	 * the current stochastic law.
	 * 
	 * @param lambda is the parameter of the distribution.
	 * @param xmin is the x coordinate where {@code f(x)=lambda}
	 * @return a value depending of the stochastic law parameters
	 * @throws MathException 
	 */
	public static float random(float lambda, float xmin) throws MathException {
		return StochasticGenerator.generateRandomValue(new ExponentialStochasticLaw(lambda,xmin));
	}

	private final float lambda;
	private final float xmin;

	/**
	 * Construct a law with the following parameters.
	 * <ul>
	 * <li><code>lambda</code></li>
	 * <li><code>xmin</code></li>
	 * </ul>
	 * 
	 * @param parameters is the set of accepted paramters.
	 * @throws LawParameterNotFoundException if the list of parameters does not permits to create the law.
	 * @throws OutsideDomainException when lambda is outside its domain
	 */
	public ExponentialStochasticLaw(Map<String,String> parameters) throws OutsideDomainException, LawParameterNotFoundException {
		this.lambda = paramFloat("lambda",parameters); //$NON-NLS-1$
		this.xmin = paramFloat("xmin",parameters); //$NON-NLS-1$
		if (this.lambda<=0) throw new OutsideDomainException(this.lambda);
	}

	/**
	 * @param lambda must be positive or nul.
	 * @param xmin
	 * @throws OutsideDomainException when lambda is outside its domain
	 */
	public ExponentialStochasticLaw(float lambda, float xmin) throws OutsideDomainException {
		if (lambda<=0) throw new OutsideDomainException(lambda);
		this.lambda = lambda;
		this.xmin = xmin;
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @return {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuilder b = new StringBuilder();
		b.append("EXPONENTIAL(lambda="); //$NON-NLS-1$
		b.append(this.lambda);
		b.append(";["); //$NON-NLS-1$
		b.append(this.xmin);
		b.append(";+inf)"); //$NON-NLS-1$
		return b.toString();
	}
	
	/** {@inheritDoc}
	 */
	@Override
	public float f(float x)  throws MathException {
		if (x<this.xmin) throw new OutsideDomainException(x);
		return (float) (this.lambda * Math.exp(-this.lambda*(x-this.xmin))); 
	}

	/** {@inheritDoc}
	 */
	@Override
	public MathFunctionRange[] getBounds() {
		return MathFunctionRange.createSet(this.xmin,Float.POSITIVE_INFINITY);
	}
	
	/** Replies the x according to the value of the distribution function.
	 * 
	 * @param u is a value given by the uniform random variable generator {@code U(0,1)}.
	 * @return {@code F<sup>-1</sup>(u)}
	 * @throws MathException in case {@code F<sup>-1</sup>(u)} could not be computed
	 */
	@Override
	public float inverseF(float u) throws MathException {
		return (float) (this.xmin - (Math.log(u)/this.lambda));
	}

}