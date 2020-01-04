/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2020 The original authors, and other authors.
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
 * Law that representes a Pareto density.
 *
 * <p>Reference:
 * <a href="http://mathworld.wolfram.com/ParetoDistribution.html">Pareto Distribution</a>.
 *
 * <p>This class uses the uniform random number distribution provided by {@link Random}.
 *
 * @author $Author: cbohrhauer$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
@SuppressWarnings({"checkstyle:parametername", "checkstyle:membername"})
public class ParetoStochasticLaw extends StochasticLaw {

	private static final String XMIN_NAME = "xmin"; //$NON-NLS-1$

	private static final String K_NAME = "k"; //$NON-NLS-1$

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
	public ParetoStochasticLaw(Map<String, String> parameters) throws OutsideDomainException, LawParameterNotFoundException {
		this.xmin = paramDouble(XMIN_NAME, parameters);
		this.k = paramDouble(K_NAME, parameters);
		if (this.xmin <= 0) {
			throw new OutsideDomainException(this.xmin);
		}
		if (this.k <= 0) {
			throw new OutsideDomainException(this.k);
		}
	}

	/** Constructor.
	 * @param k1 represents the shape of the distribution
	 * @param xmin1 is the minimum value of the distribution
	 * @throws OutsideDomainException when xmin or k is negative or nul.
	 */
	public ParetoStochasticLaw(double k1, double xmin1) throws OutsideDomainException {
		if (xmin1 <= 0) {
			throw new OutsideDomainException(xmin1);
		}
		if (k1 <= 0) {
			throw new OutsideDomainException(k1);
		}
		this.xmin = xmin1;
		this.k = k1;
	}

	/** Replies a random value that respect
	 * the current stochastic law.
	 *
	 * @param k represents the shape of the distribution
	 * @param xmin is the minimum value of the distribution
	 * @return a value depending of the stochastic law parameters
	 * @throws MathException when error in math definition.
	 */
	@Pure
	public static double random(double k, double xmin) throws MathException {
		return StochasticGenerator.generateRandomValue(new ParetoStochasticLaw(k, xmin));
	}

	@Pure
	@Override
	public double f(double x)  throws MathException {
		if (x < this.xmin) {
			throw new OutsideDomainException(x);
		}
		return this.k * ((Math.pow(this.xmin, this.k)) / (Math.pow(x, this.k + 1)));
	}

	@Pure
	@Override
	public MathFunctionRange[] getRange() {
		return MathFunctionRange.createSet(this.xmin, Double.POSITIVE_INFINITY);
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
		return this.xmin / Math.pow(u, 1. / this.k);
	}

	@Pure
	@Override
	public void toJson(JsonBuffer buffer) {
		buffer.add(NAME_NAME, getLawName());
		buffer.add(XMIN_NAME, this.xmin);
		buffer.add(K_NAME, this.k);
	}

}
