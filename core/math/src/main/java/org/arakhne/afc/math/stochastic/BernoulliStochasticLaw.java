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
 * Law that representes a Bernoulli density.
 * <p>
 * Bernoulli distribution with parameter {@code p} is defined for
 * {@code x=0} and {@code x=1}:<br>
 * {@code F(x) = p.x + (1-p)(1-x) = (2p-1).x - p + 1}<br>
 * This distribution returns
 * 0 or 1 with probability (1-p) and p, respectively.
 * <p>
 * This class uses the uniform random number distribution provider by {@link Random}. 
 * 
 * @author $Author: cbohrhauer$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class BernoulliStochasticLaw extends StochasticLaw {
	
	/** Replies a random value that respect
	 * the current stochastic law.
	 * 
	 * @param p is the probability where the value is {@code 1}
	 * @return a value depending of the stochastic law parameters
	 * @throws MathException 
	 */
	public static float random(float p) throws MathException {
		return StochasticGenerator.generateRandomValue(new BernoulliStochasticLaw(p));
	}

	private final float p;

	/**
	 * Construct a law with the following parameters.
	 * <ul>
	 * <li><code>p</code></li>
	 * </ul>
	 * 
	 * @param parameters is the set of accepted paramters.
	 * @throws LawParameterNotFoundException if the list of parameters does not permits to create the law.
	 */
	public BernoulliStochasticLaw(Map<String,String> parameters) throws LawParameterNotFoundException {
		this.p = paramFloat("p",parameters); //$NON-NLS-1$
	}

	/**
	 * @param p is the probability where the value is {@code 1}
	 */
	public BernoulliStochasticLaw(float p) {
		this.p = p;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @return {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuilder b = new StringBuilder();
		b.append("BERNOUILLI(P(0)="); //$NON-NLS-1$
		b.append(1.-this.p);
		b.append(";P(1)="); //$NON-NLS-1$
		b.append(this.p);
		b.append(')');
		return b.toString();
	}

	/** {@inheritDoc}
	 */
	@Override
	public float f(float x)  throws MathException {
		if ((x!=0.)&&(x!=1.))
			throw new OutsideDomainException(x);
		return (x==1.) ? this.p : (1.f-this.p); 
	}

	/** {@inheritDoc}
	 */
	@Override
	public MathFunctionRange[] getRange() {
		return MathFunctionRange.createDiscreteSet(0.f, 1.f);
	}
	
	/** Replies the x according to the value of the distribution function.
	 * 
	 * @param u is a value given by the uniform random variable generator {@code U(0,1)}.
	 * @return {@code F<sup>-1</sup>(u)}
	 * @throws MathException in case {@code F<sup>-1</sup>(u)} could not be computed
	 */
	@Override
	public float inverseF(float u) throws MathException {
		return (u<=this.p) ? 1.f : 0.f;
	}

}