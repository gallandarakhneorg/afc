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

import java.util.Map;
import java.util.Random;

import org.eclipse.xtext.xbase.lib.Pure;

/**
 * Law that representes a Bernoulli density.
 *
 * <p>Bernoulli distribution with parameter {@code p} is defined for
 * {@code x=0} and {@code x=1}:<br>
 * {@code F(x) = p.x + (1-p)(1-x) = (2p-1).x - p + 1}<br>
 * This distribution returns
 * 0 or 1 with probability (1-p) and p, respectively.
 *
 * <p>This class uses the uniform random number distribution provider by {@link Random}.
 *
 * @author $Author: cbohrhauer$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
@SuppressWarnings({"checkstyle:parametername", "checkstyle:membername"})
public class BernoulliStochasticLaw extends StochasticLaw {

    private final double p;

    /**
     * Construct a law with the following parameters.
     * <ul>
     * <li><code>p</code></li>
     * </ul>
     *
     * @param parameters is the set of accepted paramters.
     * @throws LawParameterNotFoundException if the list of parameters does not permits to create the law.
     */
    public BernoulliStochasticLaw(Map<String, String> parameters) throws LawParameterNotFoundException {
        this.p = paramFloat("p", parameters); //$NON-NLS-1$
    }

    /**Construct a law with the p parameter.
     *
     * @param p is the probability where the value is {@code 1}
     */
    public BernoulliStochasticLaw(double p) {
        this.p = p;
    }

    /** Replies a random value that respect
     * the current stochastic law.
     *
     * @param p is the probability where the value is {@code 1}
     * @return a value depending of the stochastic law parameters
     * @throws MathException when error in the math definition.
     */
    @Pure
    public static double random(double p) throws MathException {
        return StochasticGenerator.generateRandomValue(new BernoulliStochasticLaw(p));
    }

    @Pure
    @Override
    public String toString() {
        final StringBuilder b = new StringBuilder();
        b.append("BERNOUILLI(P(0)="); //$NON-NLS-1$
        b.append(1. - this.p);
        b.append(";P(1)="); //$NON-NLS-1$
        b.append(this.p);
        b.append(')');
        return b.toString();
    }

    @Pure
    @Override
    public double f(double x)  throws MathException {
        if ((x != 0.) && (x != 1.)) {
            throw new OutsideDomainException(x);
        }
        return (x == 1.) ? this.p : (1. - this.p);
    }

    @Pure
    @Override
    public MathFunctionRange[] getRange() {
        return MathFunctionRange.createDiscreteSet(0., 1.);
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
        return (u <= this.p) ? 1. : 0.;
    }

}
