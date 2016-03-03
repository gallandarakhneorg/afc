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
 * Law that representes a Pareto density.
 * <p>
 * Reference:
 * <a href="http://mathworld.wolfram.com/ParetoDistribution.html">Pareto Distribution</a>.
 * <p>
 * This class uses the uniform random number distribution provided by {@link Random}. 
 * 
 * @author $Author: cbohrhauer$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public class ParetoStochasticLaw extends StochasticLaw {
	
	/** Replies a random value that respect
	 * the current stochastic law.
	 * 
	 * @param k represents the shape of the distribution
	 * @param xmin is the minimum value of the distribution
	 * @return a value depending of the stochastic law parameters
	 * @throws MathException 
	 */
	public static double random(double k, double xmin) throws MathException {
		return StochasticGenerator.generateRandomValue(new ParetoStochasticLaw(k, xmin));
	}

	private final double xmin;
	private final double k;

	/**
	 * Construct a law with the following parameters.
	 * <ul>
	 * <li><code>xmin</code></li>
	 * <li><code>k</code></li>
	 * </ul>
	 * 
	 * @param parameters is the set of accepted paramters.
	 * @throws OutsideDomainException when xmin or k is negative or nul.
	 * @throws LawParameterNotFoundException if the list of parameters does not permits to create the law.
	 */
	public ParetoStochasticLaw(Map<String,String> parameters) throws OutsideDomainException, LawParameterNotFoundException {
		this.xmin = paramFloat("xmin",parameters); //$NON-NLS-1$
		this.k = paramFloat("k",parameters); //$NON-NLS-1$
		if (this.xmin<=0) throw new OutsideDomainException(this.xmin);
		if (this.k<=0) throw new OutsideDomainException(this.k);
	}

	/**
	 * @param k1 represents the shape of the distribution
	 * @param xmin1 is the minimum value of the distribution
	 * @throws OutsideDomainException when xmin or k is negative or nul.
	 */
	public ParetoStochasticLaw(double k1, double xmin1) throws OutsideDomainException {
		if (xmin1<=0) throw new OutsideDomainException(xmin1);
		if (k1<=0) throw new OutsideDomainException(k1);
		this.xmin = xmin1;
		this.k = k1;
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @return {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuilder b = new StringBuilder();
		b.append("PARETO(k="); //$NON-NLS-1$
		b.append(this.k);
		b.append(",xmin="); //$NON-NLS-1$
		b.append(this.xmin);
		b.append(')');
		return b.toString();
	}
	
	/** {@inheritDoc}
	 */
	@Override
	public double f(double x)  throws MathException {
		if (x<this.xmin)
			throw new OutsideDomainException(x);
		return this.k * ((Math.pow(this.xmin, this.k))/(Math.pow(x, this.k+1)));
	}

	/** {@inheritDoc}
	 */
	@Override
	public MathFunctionRange[] getRange() {
		return MathFunctionRange.createSet(this.xmin, Double.POSITIVE_INFINITY);
	}

	/** Replies the x according to the value of the distribution function.
	 * 
	 * @param u is a value given by the uniform random variable generator {@code U(0,1)}.
	 * @return {@code F<sup>-1</sup>(u)}
	 * @throws MathException in case {@code F<sup>-1</sup>(u)} could not be computed
	 */
	@Override
	public double inverseF(double u) throws MathException {
		return this.xmin / Math.pow(u, 1/this.k); 
	}

}