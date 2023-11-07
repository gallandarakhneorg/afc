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
 * Law that representes a linear density.
 *
 * <p>The linear distribution is based on {@code f(x) = a.x+b}
 * where, if the distribution is ascendent, {@code a>0} and
 * {@code f(minX) = 0}, and if the distribution is descendent,
 * {@code a<0} and {@code f(maxX) = 0}.
 *
 * <p>This class uses the uniform random number distribution provided by {@link Random}.
 *
 * @author $Author: cbohrhauer$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public class LinearStochasticLaw extends StochasticLaw {

	private static final String ASCENDENT_NAME = "ascendent"; //$NON-NLS-1$

	private static final boolean ASCENDENT_DEFAULT = true;

	private static final String MINX_NAME = "minX"; //$NON-NLS-1$

	private static final String MAXX_NAME = "maxX"; //$NON-NLS-1$

	private static final String DELTA_NAME = "delta"; //$NON-NLS-1$

	private final boolean ascendent;

	private final double minX;

	private final double maxX;

	private final double delta;

	/**
	 * Construct a law with the following parameters.
	 * <ul>
	 * <li>{@code ascendent}</li>
	 * <li>{@code minX}</li>
	 * <li>{@code maxY}</li>
	 * <li>{@code delta}</li>
	 * </ul>
	 *
	 * @param parameters is the set of accepted paramters.
	 * @throws LawParameterNotFoundException if the list of parameters does not permits to create the law.
	 */
	public LinearStochasticLaw(Map<String, String> parameters) throws LawParameterNotFoundException {
		this(
				paramDouble(MINX_NAME, parameters),
				paramDouble(MAXX_NAME, parameters),
				paramBoolean(ASCENDENT_NAME, ASCENDENT_DEFAULT, parameters));
	}

	/** Create a ascendent linear distribution.
	 *
	 * @param minX is the lower bound of the distribution
	 * @param maxX is the upper bound of the distribution
	 */
	public LinearStochasticLaw(double minX, double maxX) {
		this(minX, maxX, ASCENDENT_DEFAULT);
	}

	/** Create a linear distribution.
	 * @param minX is the lower bound of the distribution
	 * @param maxX is the upper bound of the distribution
	 * @param ascendent indicates of the distribution function is ascendent or not
	 */
	public LinearStochasticLaw(double minX, double maxX, boolean ascendent) {
		double i = minX;
		double a = maxX;
		if (i > a) {
			final double t = i;
			i = a;
			a = t;
		}

		this.ascendent = ascendent;
		this.minX = i;
		this.maxX = a;

		this.delta = this.ascendent ? (this.maxX - this.minX) : (this.minX - this.maxX);
	}

	/** Replies a random value that respect
	 * the current stochastic law.
	 *
	 * @param minX is the lower bound of the distribution
	 * @param maxX is the upper bound of the distribution
	 * @param ascendent indicates of the distribution function is ascendent or not
	 * @return a value depending of the stochastic law parameters
	 * @throws MathException when error in the math definition.
	 */
	@Pure
	public static double random(double minX, double maxX, boolean ascendent) throws MathException {
		return StochasticGenerator.generateRandomValue(new LinearStochasticLaw(minX, maxX, ascendent));
	}

	/** Replies a random value that respect
	 * the current stochastic law.
	 *
	 * <p>The used stochastic law is the ascendent linear distribution.
	 *
	 * @param minX is the lower bound of the distribution
	 * @param maxX is the upper bound of the distribution
	 * @return a value depending of the stochastic law parameters
	 * @throws MathException when error in the math definition.
	 */
	@Pure
	public static double random(double minX, double maxX) throws MathException {
		return StochasticGenerator.generateRandomValue(new LinearStochasticLaw(minX, maxX));
	}

	@Pure
	@Override
	public double f(double x)  throws MathException {
		if ((x < this.minX) || (x > this.maxX)) {
			throw new OutsideDomainException(x);
		}
		final double a = 2. / (this.delta * this.delta);
		final double b;
		if (this.ascendent) {
			b = -a * this.minX;
		} else {
			b = -a * this.maxX;
		}
		return a * x + b;
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
	@Override
	@Pure
	public double inverseF(double u) throws MathException {
		if (this.ascendent) {
			return this.delta * Math.sqrt(u) + this.minX;
		}

		return this.delta * Math.sqrt(u) + this.maxX;
	}

	@Pure
	@Override
	public void toJson(JsonBuffer buffer) {
		buffer.add(NAME_NAME, getLawName());
		buffer.add(ASCENDENT_NAME, this.ascendent);
		buffer.add(MINX_NAME, this.minX);
		buffer.add(MAXX_NAME, this.maxX);
		buffer.add(DELTA_NAME, this.delta);
	}

}
