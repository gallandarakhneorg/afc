/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2018 The original authors, and other authors.
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

import java.util.Random;

import org.eclipse.xtext.xbase.lib.Pure;

/**
 * Law that representes an uniform density with as its upper and lower bounds
 * equal.
 *
 * <p>This class uses the uniform random number distribution provided by {@link Random}.
 * This class replies the value of the law.
 *
 * @author $Author: cbohrhauer$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public class ConstantStochasticLaw extends StochasticLaw {

	private final double value;

	/** Create a constant stochastic law.
	 *
	 * @param value1 is the value replied by this law.
	 */
	public ConstantStochasticLaw(double value1) {
		this.value = value1;
	}

	@Pure
	@Override
	public double inverseF(double u) throws MathException {
		return this.value;
	}

	@Pure
	@Override
	public double f(double x) throws MathException {
		return (x != this.value) ? 0 : 1.f;
	}

	@Pure
	@Override
	public MathFunctionRange[] getRange() {
		return new MathFunctionRange[] {
			new MathFunctionRange(this.value),
		};
	}

}
