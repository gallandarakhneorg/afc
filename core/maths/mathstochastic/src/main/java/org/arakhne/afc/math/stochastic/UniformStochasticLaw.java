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
 * Law that representes an uniform density.
 *
 * <p>The uniform law is described by:<br>
 * {@code U(minX, maxX) = 1/abs(maxX-minY) iff maxY<>minY}<br>
 * {@code U(minX, maxX) = 1 iff maxY=minY}
 *
 * <p>Reference:
 * <a href="http://mathworld.wolfram.com/UniformDistribution.html">Uniform Distribution</a>.
 *
 * <p>This class uses the uniform random number distribution provided by {@link Random}.
 * This class replies a random number equivalent to the value replied by
 * {@code (maxX-minX)*Math.random()+minX}.
 *
 * @author $Author: cbohrhauer$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public class UniformStochasticLaw extends StochasticLaw {

	private static final String MINX_NAME = "minX"; //$NON-NLS-1$

	private static final String MAXX_NAME = "maxX"; //$NON-NLS-1$

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
	 * @param parameters is the set of accepted parameters.
	 * @throws LawParameterNotFoundException if the list of parameters does not permits to create the law.
	 */
	public UniformStochasticLaw(Map<String, String> parameters) throws LawParameterNotFoundException {
		this(paramDouble(MINX_NAME, parameters),
				paramDouble(MAXX_NAME, parameters));
	}

	/** Create a uniform stochastic law.
	 *
	 * @param minX1 is the lower bound
	 * @param maxX1 is the upper bound
	 */
	public UniformStochasticLaw(double minX1, double maxX1) {
		if (minX1 < maxX1) {
			this.minX = minX1;
			this.maxX = maxX1;
		} else {
			this.minX = maxX1;
			this.maxX = minX1;
		}

		this.delta = this.maxX - this.minX;
	}

	/** Replies a random value that respect
	 * the current stochastic law.
	 *
	 * @param minX is the lower bound
	 * @param maxX is the upper bound
	 * @return a value depending of the stochastic law parameters
	 * @throws MathException when math definition error.
	 */
	@Pure
	@Inline(value = "StochasticGenerator.generateRandomValue(new UniformStochasticLaw(($1), ($2)))",
		imported = {StochasticGenerator.class, UniformStochasticLaw.class})
	public static double random(double minX, double maxX) throws MathException {
		return StochasticGenerator.generateRandomValue(new UniformStochasticLaw(minX, maxX));
	}

	@Pure
	@Override
	public double f(double x)  throws MathException {
		if ((x < this.minX) || (x > this.maxX)) {
			throw new OutsideDomainException(x);
		}
		return 1. / this.delta;
	}

	@Override
	@Pure
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
		return this.delta * u + this.minX;
	}

	@Pure
	@Override
	public void toJson(JsonBuffer buffer) {
		buffer.add(NAME_NAME, getLawName());
		buffer.add(MINX_NAME, this.minX);
		buffer.add(MAXX_NAME, this.maxX);
	}

}
