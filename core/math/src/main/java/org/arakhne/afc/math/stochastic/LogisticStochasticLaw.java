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
 * Law that representes a logistic density.
 * <p>
 * Reference:
 * <a href="http://en.wikipedia.org/wiki/Logistic_distribution">Logistic Distribution</a>.
 * <p>
 * This class uses the uniform random number distribution provided by {@link Random}. 
 * 
 * @author $Author: cbohrhauer$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class LogisticStochasticLaw extends StochasticLaw {
	
	/** Replies a random value that respect
	 * the current stochastic law.
	 * 
	 * @param mu is the location of the distribution
	 * @param scale is the scale of the distristibution ({@code &gt;0})
	 * @return a value depending of the stochastic law parameters
	 * @throws MathException 
	 */
	public static double random(double mu, double scale) throws MathException {
		return StochasticGenerator.generateRandomValue(new LogisticStochasticLaw(mu, scale));
	}

	private final double mu;
	private final double scale;

	/**
	 * Construct a law with the following parameters.
	 * <ul>
	 * <li><code>mu</code></li>
	 * <li><code>scale</code></li>
	 * </ul>
	 * 
	 * @param parameters is the set of accepted paramters.
	 * @throws LawParameterNotFoundException if the list of parameters does not permits to create the law.
	 * @throws OutsideDomainException when scale is negative. 
	 */
	public LogisticStochasticLaw(Map<String,String> parameters) throws OutsideDomainException, LawParameterNotFoundException {
		this.mu = paramFloat("mu",parameters); //$NON-NLS-1$
		this.scale = paramFloat("scale",parameters); //$NON-NLS-1$
		if (this.scale<=0) throw new OutsideDomainException(this.scale);
	}

	/**
	 * @param mu is the location of the distribution
	 * @param scale is the scale of the distristibution ({@code &gt;0})
	 * @throws OutsideDomainException when scale is negative. 
	 */
	public LogisticStochasticLaw(double mu, double scale) throws OutsideDomainException {
		if (scale<=0) throw new OutsideDomainException(scale);
		this.mu = mu;
		this.scale = scale;
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @return {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuilder b = new StringBuilder();
		b.append("LOGISTIC(mu="); //$NON-NLS-1$
		b.append(this.mu);
		b.append(",scale="); //$NON-NLS-1$
		b.append(this.scale);
		b.append(')');
		return b.toString();
	}
	
	/** {@inheritDoc}
	 */
	@Override
	public double f(double x)  throws MathException {
		double ex = Math.exp((this.mu-x)/this.scale);
		double denom = (1.f+ex)*(1.f+ex);
		return ex / (this.scale*denom);
	}

	/** {@inheritDoc}
	 */
	@Override
	public MathFunctionRange[] getRange() {
		return MathFunctionRange.createInfinitySet();
	}
	
	/** Replies the x according to the value of the distribution function.
	 * 
	 * @param u is a value given by the uniform random variable generator {@code U(0,1)}.
	 * @return {@code F<sup>-1</sup>(u)}
	 * @throws MathException in case {@code F<sup>-1</sup>(u)} could not be computed
	 */
	@Override
	public double inverseF(double u) throws MathException {
		return this.mu + this.scale*Math.log(u/(1.f-u)); 
	}

}