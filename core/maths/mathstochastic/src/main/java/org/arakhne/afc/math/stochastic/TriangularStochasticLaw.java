/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2019 The original authors, and other authors.
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

import org.eclipse.xtext.xbase.lib.Inline;
import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.vmutil.json.JsonBuffer;

/**
 * Law that representes a triangular density.
 *
 * <p>Reference:
 * <a href="http://mathworld.wolfram.com/TriangularDistribution.html">Triangular Distribution</a>.
 *
 * <p>This class uses the uniform random number distribution provided by {@link Random}.
 *
 * @author $Author: cbohrhauer$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public class TriangularStochasticLaw extends StochasticLaw {

	private static final String MINX_NAME = "minX"; //$NON-NLS-1$

	private static final String MAXX_NAME = "maxX"; //$NON-NLS-1$

	private static final String MODE_NAME = "mode"; //$NON-NLS-1$

	private final double minX;

	private final double mode;

	private final double maxX;

	private final double dxmode;

	private final double delta1;

	private final double delta2;

	/**
	 * Construct a law with the following parameters.
	 * <ul>
	 * <li><code>minX</code></li>
	 * <li><code>mode</code></li>
	 * <li><code>maxX</code></li>
	 * </ul>
	 *
	 * @param parameters is the set of accepted paramters.
	 * @throws LawParameterNotFoundException if the list of parameters does not permits to create the law.
	 */
	public TriangularStochasticLaw(Map<String, String> parameters) throws LawParameterNotFoundException {
		this(paramDouble(MINX_NAME, parameters),
				paramDouble(MODE_NAME, parameters),
				paramDouble(MAXX_NAME, parameters));
	}

	/** Construct the law.
	 * @param minX1 is the lower bound where {@code f(minX) = 0}
	 * @param mode is the maxima point of the distribution {@code f(mode) = max(f(x))}
	 * @param maxX1 is the upper bound where {@code f(maxX) = 0}
	 */
	public TriangularStochasticLaw(double minX1, double mode, double maxX1) {
		double i = minX1;
		double a = maxX1;
		double mod = mode;
		if (i > a) {
			final double t = a;
			a = i;
			i = t;
		}
		if (i > mod) {
			final double t = mod;
			mod = i;
			i = t;
		}
		if (mod > a) {
			final double t = a;
			a = mod;
			mod = t;
		}
		this.minX = i;
		this.mode = mod;
		this.maxX = a;


		this.delta1 = (this.maxX - this.minX) * (this.mode - this.minX);
		this.delta2 = (this.maxX - this.minX) * (this.maxX - this.mode);

		this.dxmode = (this.mode - this.minX) / (this.maxX - this.minX);
	}

	/** Replies a random value that respect
	 * the current stochastic law.
	 *
	 * @param minX is the lower bound where {@code f(minX) = 0}
	 * @param mode is the maxima point of the distribution {@code f(mode) = max(f(x))}
	 * @param maxX is the upper bound where {@code f(maxX) = 0}
	 * @return a value depending of the stochastic law parameters
	 * @throws MathException when math definition error.
	 */
	@Pure
	@Inline(value = "StochasticGenerator.generateRandomValue(new TriangularStochasticLaw(($1), ($2), ($3)))",
		imported = {StochasticGenerator.class, TriangularStochasticLaw.class})
	public static double random(double minX, double mode, double maxX) throws MathException {
		return StochasticGenerator.generateRandomValue(new TriangularStochasticLaw(minX, mode, maxX));
	}

	@Pure
	@Override
	public double f(double x)  throws MathException {
		if ((x < this.minX) || (x > this.maxX)) {
			throw new OutsideDomainException(x);
		}

		final double denom = this.maxX - this.minX;

		if (x <= this.mode) {
			final double xm = 2. * (x - this.minX);
			return xm / (denom * (this.mode - this.minX));
		}

		final double xm = 2. * (this.maxX - x);
		return 1.f - (xm / (denom * (this.maxX - this.mode)));

	}

	@Pure
	@Override
	public MathFunctionRange[] getRange() {
		return MathFunctionRange.createSet(this.minX, this.maxX);
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
		if ((u < 0) || (u > 1)) {
			throw new OutsideDomainException(u);
		}

		if (u < this.dxmode) {
			return Math.sqrt(u * this.delta1) + this.minX;
		}

		return this.maxX - Math.sqrt((1 - u) * this.delta2);
	}

	@Pure
	@Override
	public void toJson(JsonBuffer buffer) {
		buffer.add(NAME_NAME, getLawName());
		buffer.add(MINX_NAME, this.minX);
		buffer.add(MAXX_NAME, this.maxX);
		buffer.add(MODE_NAME, this.mode);
	}

}
