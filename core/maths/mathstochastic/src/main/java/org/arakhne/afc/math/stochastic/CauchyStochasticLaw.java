/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2023 The original authors and other contributors.
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
 * Law that representes a Cauchy-Lorentz density.
 *
 * <p>Reference:
 * <a href="http://en.wikipedia.org/wiki/Cauchy_distribution">Cauchy-Lorentz Distribution</a>.
 *
 * <p>This class uses the uniform random number distribution provided by {@link Random}.
 *
 * @author $Author: cbohrhauer$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
@SuppressWarnings("checkstyle:parametername")
public class CauchyStochasticLaw extends StochasticLaw {

	private static final String X0_NAME = "x0"; //$NON-NLS-1$

	private static final String GAMMA_NAME = "gamma"; //$NON-NLS-1$

	private final double x0;

	private final double gamma;

	/**
	 * Construct a law with the following parameters.
	 * <ul>
	 * <li>{@code x0}</li>
	 * <li>{@code gamma}</li>
	 * </ul>
	 *
	 * @param parameters is the set of accepted paramters.
	 * @throws LawParameterNotFoundException if the list of parameters does not permits to create the law.
	 * @throws OutsideDomainException when gamma is outside its domain
	 */
	public CauchyStochasticLaw(Map<String, String> parameters) throws OutsideDomainException, LawParameterNotFoundException {
		this.x0 = paramDouble(X0_NAME, parameters);
		this.gamma = paramDouble(GAMMA_NAME, parameters);
		if (this.gamma <= 0) {
			throw new OutsideDomainException(this.gamma);
		}
	}

	/** Constructor.
	 * @param x0 is the location parameter that specifying the location of the peak
	 *     of the distribution
	 * @param gamma is the scale parameter which specifies the half-width at half-maximum (HWHM).
	 * @throws OutsideDomainException when gamma is outside its domain
	 */
	public CauchyStochasticLaw(double x0, double gamma) throws OutsideDomainException {
		if (gamma <= 0) {
			throw new OutsideDomainException(gamma);
		}
		this.x0 = x0;
		this.gamma = gamma;
	}

	/** Replies a random value that respect
	 * the current stochastic law.
	 *
	 * @param k represents the shape of the distribution
	 * @param xmin is the minimum value of the distribution
	 * @return a value depending of the stochastic law parameters
	 * @throws MathException when error in the math definition.
	 */
	@Pure
	public static double random(double k, double xmin) throws MathException {
		return StochasticGenerator.generateRandomValue(new CauchyStochasticLaw(k, xmin));
	}

	@Pure
	@Override
	public double f(double x)  throws MathException {
		final double xm = x - this.x0;
		return 1. / Math.PI * (this.gamma / ((xm * xm) + (this.gamma * this.gamma)));
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
	@SuppressWarnings("checkstyle:magicnumber")
	public double inverseF(double u) throws MathException {
		return this.x0 + this.gamma * Math.tan(Math.PI * (u - .5));
	}

	@Pure
	@Override
	public void toJson(JsonBuffer buffer) {
		buffer.add(NAME_NAME, getLawName());
		buffer.add(X0_NAME, this.x0);
		buffer.add(GAMMA_NAME, this.gamma);
	}

}
