/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2016 The original authors, and other authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.arakhne.afc.math.stochastic;

import java.util.Map;
import java.util.Random;

import org.eclipse.xtext.xbase.lib.Pure;

/**
 * Law that representes a gaussian density.
 *
 * <p>Reference:
 * <a href="http://mathworld.wolfram.com/NormalDistribution.html">Normal Distribution</a>.
 *
 * <p>This class uses the gaussian random number distribution provided by {@link Random}.
 *
 * @author $Author: cbohrhauer$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public class GaussianStochasticLaw extends StochasticLaw {

	private static final double SQRT2PI = Math.sqrt(2. * Math.PI);

	private double mean;

	private double standardDeviation;

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
	public GaussianStochasticLaw(Map<String, String> parameters) throws OutsideDomainException, LawParameterNotFoundException {
		this.mean = paramFloat("mean", parameters); //$NON-NLS-1$
		this.standardDeviation = paramFloat("standardDeviation", parameters); //$NON-NLS-1$
		if (this.standardDeviation <= 0) {
			throw new OutsideDomainException(this.standardDeviation);
		}
	}

	/**
	 * @param mean is the mean of the normal distribution.
	 * @param standardDeviation is the standard deviation associated to the nromal distribution.
	 * @throws OutsideDomainException when standardDeviation is not positive or nul.
	 */
	public GaussianStochasticLaw(double mean, double standardDeviation) throws OutsideDomainException {
		if (standardDeviation <= 0) {
			throw new OutsideDomainException(standardDeviation);
		}
		this.mean = mean;
		this.standardDeviation = standardDeviation;
	}

	/** Replies a random value that respect
	 * the current stochastic law.
	 *
	 * @param mean is the mean of the normal distribution.
	 * @param standardDeviation is the standard deviation associated to the nromal distribution.
	 * @return a value depending of the stochastic law parameters
	 * @throws MathException when error in mah definition.
	 */
	public static double random(double mean, double standardDeviation) throws MathException {
		return StochasticGenerator.generateRandomValue(new GaussianStochasticLaw(mean, standardDeviation));
	}

	@Pure
	@Override
	public String toString() {
		final StringBuilder b = new StringBuilder();
		b.append("NORMAL(mean="); //$NON-NLS-1$
		b.append(this.mean);
		b.append(";deviation="); //$NON-NLS-1$
		b.append(this.standardDeviation);
		b.append(')');
		return b.toString();
	}

	@Pure
	@Override
	public double f(double x)  throws MathException {
		double xm = x - this.mean;
		xm = xm * xm;
		return (1. / (this.standardDeviation * SQRT2PI)) * Math.exp((-xm) / (2.
				* this.standardDeviation * this.standardDeviation));
	}

	@Pure
	@Override
	public MathFunctionRange[] getRange() {
		return MathFunctionRange.createInfinitySet();
	}

	/** Replies the x according to the value of the distribution function.
	 *
	 * @param u is a value given by the uniform random variable generator {@code U(0, 1)}.
	 * @return {@code F<sup>-1</sup>(u)}
	 * @throws MathException in case {@code F<sup>-1</sup>(u)} could not be computed
	 */
	@Pure
	@Override
	public double inverseF(double u) throws MathException {
		return this.standardDeviation * u + this.mean;
	}

	/** Replies the x according to the value of the inverted
	 * cummulative distribution function {@code F<sup>-1</sup>(u)}
	 * where {@code u = U(0, 1)}.
	 *
	 * @param rnd is the uniform random variable generator {@code U(0, 1)}.
	 * @return {@code F<sup>-1</sup>(u)}
	 * @throws MathException in case {@code F<sup>-1</sup>(u)} could not be computed
	 */
	@Override
	protected final double inverseF(Random rnd) throws MathException {
		final double u = (rnd.nextGaussian() + 1) / 2.;
		return inverseF(u);
	}

}
