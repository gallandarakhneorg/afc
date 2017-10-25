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
 * Law that representes a triangular density.
 *
 * <p>Reference:
 * <a href="http://mathworld.wolfram.com/ExponentialDistribution.html">Exponential Distribution</a>.
 *
 * <p>This class uses the uniform random number distribution provided by {@link Random}.
 *
 * @author $Author: cbohrhauer$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public class ExponentialStochasticLaw extends StochasticLaw {

    private final double lambda;

    private final double xmin;

    /**
     * Construct a law with the following parameters.
     * <ul>
     * <li><code>lambda</code></li>
     * <li><code>xmin</code></li>
     * </ul>
     *
     * @param parameters is the set of accepted paramters.
     * @throws LawParameterNotFoundException if the list of parameters does not permits to create the law.
     * @throws OutsideDomainException when lambda is outside its domain
     */
    public ExponentialStochasticLaw(Map<String, String> parameters) throws OutsideDomainException, LawParameterNotFoundException {
        this.lambda = paramFloat("lambda", parameters); //$NON-NLS-1$
        this.xmin = paramFloat("xmin", parameters); //$NON-NLS-1$
        if (this.lambda <= 0) {
            throw new OutsideDomainException(this.lambda);
        }
    }

    /**
     * @param lambda must be positive or nul.
     * @param xmin the xmin parameter.
     * @throws OutsideDomainException when lambda is outside its domain
     */
    public ExponentialStochasticLaw(double lambda, double xmin) throws OutsideDomainException {
        if (lambda <= 0) {
            throw new OutsideDomainException(lambda);
        }
        this.lambda = lambda;
        this.xmin = xmin;
    }

    /** Replies a random value that respect
     * the current stochastic law.
     *
     * @param lambda is the parameter of the distribution.
     * @param xmin is the x coordinate where {@code f(x)=lambda}
     * @return a value depending of the stochastic law parameters
     * @throws MathException when error in the math definition.
     */
    @Pure
    public static double random(double lambda, double xmin) throws MathException {
        return StochasticGenerator.generateRandomValue(new ExponentialStochasticLaw(lambda, xmin));
    }

    @Pure
    @Override
    public String toString() {
        final StringBuilder b = new StringBuilder();
        b.append("EXPONENTIAL(lambda="); //$NON-NLS-1$
        b.append(this.lambda);
        b.append(";["); //$NON-NLS-1$
        b.append(this.xmin);
        b.append(";+inf)"); //$NON-NLS-1$
        return b.toString();
    }

    @Pure
    @Override
    public double f(double x)  throws MathException {
        if (x < this.xmin) {
            throw new OutsideDomainException(x);
        }
        return this.lambda * Math.exp(-this.lambda * (x - this.xmin));
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
    @Override
    @Pure
    public double inverseF(double u) throws MathException {
        return this.xmin - (Math.log(u) / this.lambda);
    }

}
