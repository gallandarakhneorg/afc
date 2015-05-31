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

import org.arakhne.afc.math.MathException;
import org.arakhne.afc.math.MathFunctionRange;

/**
 * Law that representes an uniform density.
 * <p>
 * The uniform law is described by:<br>
 * {@code U(minX,maxX) = 1/abs(maxX-minY) iff maxY<>minY}<br>
 * {@code U(minX,maxX) = 1 iff maxY=minY}
 * <p>
 * Reference:
 * <a href="http://mathworld.wolfram.com/UniformDistribution.html">Uniform Distribution</a>.
 * <p>
 * This class uses the uniform random number distribution provided by {@link Random}.
 * This class replies a random number equivalent to the value replied by 
 * {@code (maxX-minX)*Math.random()+minX}.
 * 
 * @author $Author: cbohrhauer$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class UniformStochasticLaw extends StochasticLaw {
	
	/** Replies a random value that respect
	 * the current stochastic law.
	 * 
	 * @param minX is the lower bound
	 * @param maxX is the upper bound
	 * @return a value depending of the stochastic law parameters
	 * @throws MathException 
	 */
	public static double random(double minX, double maxX) throws MathException {
		return StochasticGenerator.generateRandomValue(new UniformStochasticLaw(minX, maxX));
	}

	private final double minX;
	private final double maxX;
	
	private final double delta;
	
	/**
	 * Construct a law with the following parameters.
	 * <ul>
	 * <li><code>minX</code></li>
	 * <li><code>maxX</code></li>
	 * </ul>
	 * 
	 * @param parameters is the set of accepted paramters.
	 * @throws LawParameterNotFoundException if the list of parameters does not permits to create the law.
	 */
	public UniformStochasticLaw(Map<String,String> parameters) throws LawParameterNotFoundException {
		this(paramFloat("minX",parameters), //$NON-NLS-1$
			paramFloat("maxX",parameters)); //$NON-NLS-1$
	}

	/** Create a uniform stochastic law.
	 * 
	 * @param minX is the lower bound
	 * @param maxX is the upper bound
	 */
	public UniformStochasticLaw(double minX, double maxX) {
		if (minX<maxX) {
			this.minX = minX;
			this.maxX = maxX;
		}
		else {
			this.minX = maxX;
			this.maxX = minX;
		}
		
		this.delta = this.maxX - this.minX;
	}
	
	/** {@inheritDoc}
	 */
	@Override
	public double f(double x)  throws MathException {
		if ((x<this.minX)||(x>this.maxX))
			throw new OutsideDomainException(x);
		return 1.f/this.delta;
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
	public double inverseF(double u) throws MathException {
		return this.delta*u + this.minX;
	}

}