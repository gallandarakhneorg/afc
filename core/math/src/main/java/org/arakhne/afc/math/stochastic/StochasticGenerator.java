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

import java.util.Random;

import org.eclipse.xtext.xbase.lib.Pure;

/**
 * Generator of random values according to stochastic laws.
 *
 * @author $Author: cbohrhauer$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public final class StochasticGenerator {

    private static Random uniformRandomVariableList;

    private StochasticGenerator() {
        //
    }

    private static void initRandomNumberList() {
        if (uniformRandomVariableList == null) {
            uniformRandomVariableList = new Random();
        }
    }

    /** Generate a stochastic value according to the given law.
     *
     * <p>A probability {@code p} is randomly selected using the specified random number list.
     * The returned value
     * is when a randomly selected value inside the set of available values.
     *
     * <p>This method uses a {@link UniformStochasticLaw uniform distribution random number generation}.
     *
     * @param law is the stochastic law to use.
     * @return a value which was randomly selected according to a stochastic law.
     * @throws MathException in case the value could not be computed.
     */
    @Pure
    public static double generateRandomValue(StochasticLaw law) throws MathException {
        initRandomNumberList();
        return law.inverseF(uniformRandomVariableList);
    }

    /** Add a noise to the specified value.
     *
     * <p>The returned value is given by:
     * {@code (value-noise) &lt; value &lt; (value+noise)}
     * where {@code 0 &lt;= noise &lt;= max(abs(value), noiseLaw(value))}.
     * The {@code noise} is randomly selected according to the
     * given random number list.
     *
     * <p>This method uses a {@link UniformStochasticLaw uniform distribution random number generation}.
     *
     * @param value is the value to noise
     * @param noiseLaw is the law used to selected tyhe noise amount.
     * @return the value
     * @throws MathException is case the value is not valid
     */
    @Pure
    public static double noiseValue(double value, MathFunction noiseLaw) throws MathException {
        try {
            double noise = Math.abs(noiseLaw.f(value));
            initRandomNumberList();
            noise *= uniformRandomVariableList.nextFloat();
            if (uniformRandomVariableList.nextBoolean()) {
                noise = -noise;
            }
            return value + noise;
        } catch (MathException e) {
            return value;
        }
    }

}
