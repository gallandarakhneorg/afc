/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2022 The original authors, and other authors.
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

import org.arakhne.afc.vmutil.json.JsonBuffer;

/**
 * Law that representes a logistic density.
 *
 * <p>Reference:
 * <a href="http://en.wikipedia.org/wiki/Logistic_distribution">Logistic Distribution</a>.
 *
 * <p>This class uses the uniform random number distribution provided by {@link Random}.
 *
 * @author $Author: cbohrhauer$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public class LogisticStochasticLaw extends StochasticLaw {

	private static final String MU_NAME = "mu"; //$NON-NLS-1$

	private static final String SCALE_NAME = "scale"; //$NON-NLS-1$

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
	public LogisticStochasticLaw(Map<String, String> parameters) throws OutsideDomainException, LawParameterNotFoundException {
		this.mu = paramDouble(MU_NAME, parameters);
		this.scale = paramDouble(SCALE_NAME, parameters);
		if (this.scale <= 0) {
			throw new OutsideDomainException(this.scale);
		}
	}

	/** Constructor.
	 * @param mu1 is the location of the distribution
	 * @param scale is the scale of the distristibution ({@code &gt;0})
	 * @throws OutsideDomainException when scale is negative.
	 */
	public LogisticStochasticLaw(double mu1, double scale) throws OutsideDomainException {
		if (scale <= 0) {
			throw new OutsideDomainException(scale);
		}
		this.mu = mu1;
		this.scale = scale;
	}

	/** Replies a random value that respect
	 * the current stochastic law.
	 *
	 * @param mu is the location of the distribution
	 * @param scale is the scale of the distristibution ({@code &gt;0})
	 * @return a value depending of the stochastic law parameters
	 * @throws MathException when error in math definition.
	 */
	@Pure
	public static double random(double mu, double scale) throws MathException {
		return StochasticGenerator.generateRandomValue(new LogisticStochasticLaw(mu, scale));
	}

	@Pure
	@Override
	public double f(double x)  throws MathException {
		final double ex = Math.exp((this.mu - x) / this.scale);
		final double denom = (1. + ex) * (1. + ex);
		return ex / (this.scale * denom);
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
		return this.mu + this.scale * Math.log(u / (1. - u));
	}

	@Pure
	@Override
	public void toJson(JsonBuffer buffer) {
		buffer.add(NAME_NAME, getLawName());
		buffer.add(MU_NAME, this.mu);
		buffer.add(SCALE_NAME, this.scale);
	}

}
