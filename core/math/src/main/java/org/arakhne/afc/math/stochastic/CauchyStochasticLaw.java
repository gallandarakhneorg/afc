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
 * Law that representes a Cauchy-Lorentz density.
 * <p>
 * Reference:
 * <a href="http://en.wikipedia.org/wiki/Cauchy_distribution">Cauchy-Lorentz Distribution</a>.
 * <p>
 * This class uses the uniform random number distribution provided by {@link Random}. 
 * 
 * @author $Author: cbohrhauer$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class CauchyStochasticLaw extends StochasticLaw {
	
	/** Replies a random value that respect
	 * the current stochastic law.
	 * 
	 * @param k represents the shape of the distribution
	 * @param xmin is the minimum value of the distribution
	 * @return a value depending of the stochastic law parameters
	 * @throws MathException 
	 */
	public static double random(double k, double xmin) throws MathException {
		return StochasticGenerator.generateRandomValue(new CauchyStochasticLaw(k, xmin));
	}

	private final double x0;
	private final double gamma;

	/**
	 * Construct a law with the following parameters.
	 * <ul>
	 * <li><code>x0</code></li>
	 * <li><code>gamma</code></li>
	 * </ul>
	 * 
	 * @param parameters is the set of accepted paramters.
	 * @throws LawParameterNotFoundException if the list of parameters does not permits to create the law.
	 * @throws OutsideDomainException when gamma is outside its domain
	 */
	public CauchyStochasticLaw(Map<String,String> parameters) throws OutsideDomainException,LawParameterNotFoundException {
		this.x0 = paramFloat("x0",parameters); //$NON-NLS-1$
		this.gamma = paramFloat("gamma",parameters); //$NON-NLS-1$
		if (this.gamma<=0) throw new OutsideDomainException(this.gamma);
	}

	/**
	 * @param x01 is the location parameter that specifying the location of the peak
	 * of the distribution
	 * @param gamma1 is the scale parameter which specifies the half-width at half-maximum (HWHM).
	 * @throws OutsideDomainException when gamma is outside its domain
	 */
	public CauchyStochasticLaw(double x01, double gamma1) throws OutsideDomainException {
		if (gamma1<=0) throw new OutsideDomainException(gamma1);
		this.x0 = x01;
		this.gamma = gamma1;
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @return {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuilder b = new StringBuilder();
		b.append("CAUCHY(x0="); //$NON-NLS-1$
		b.append(this.x0);
		b.append(",gamma="); //$NON-NLS-1$
		b.append(this.gamma);
		b.append(')');
		return b.toString();
	}
	
	/** {@inheritDoc}
	 */
	@Override
	public double f(double x)  throws MathException {
		double xm = x - this.x0;
		return 1.f/Math.PI * (this.gamma/((xm * xm)+(this.gamma * this.gamma))); 
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
		return this.x0 + this.gamma * Math.tan(Math.PI * (u-.5f)); 
	}

}