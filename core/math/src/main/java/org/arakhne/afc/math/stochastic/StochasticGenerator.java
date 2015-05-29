/* 
 * $Id$
 * 
 * Copyright (c) 2013 Christophe BOHRHAUER
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 * This program is free software; you can redistribute it and/or modify
 */
package org.arakhne.afc.math.stochastic;

import java.util.Random;

import org.arakhne.afc.math.MathException;
import org.arakhne.afc.math.MathFunction;

/**
 * Generator of random values according to stochastic laws.
 * 
 * @author $Author: cbohrhauer$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class StochasticGenerator {
	
	private static Random uniformRandomVariableList = null;
	
	private static void initRandomNumberList() {
		if (uniformRandomVariableList==null) {
			uniformRandomVariableList = new Random();
		}
	}
	
	/** Generate a stochastic value according to the given law.
	 * <p>
	 * A probability {@code p} is randomly selected using the specified random number list.
	 * The returned value
	 * is when a randomly selected value inside the set of available values.
	 * <p>
	 * This method uses a {@link UniformStochasticLaw uniform distribution random number generation}.
	 * 
	 * @param law is the stochastic law to use.
	 * @return a value which was randomly selected according to a stochastic law.
	 * @throws MathException in case the value could not be computed.
	 */
	public static float generateRandomValue(StochasticLaw law) throws MathException {
		initRandomNumberList();
		return law.inverseF(uniformRandomVariableList);
	}
	
	/** Add a noise to the specified value.
	 * <p>
	 * The returned value is given by:
	 * {@code (value-noise) &lt; value &lt; (value+noise)}
	 * where {@code 0 &lt;= noise &lt;= max(abs(value),noiseLaw(value))}.
	 * The {@code noise} is randomly selected according to the
	 * given random number list.
	 * <p>
	 * This method uses a {@link UniformStochasticLaw uniform distribution random number generation}.
	 * 
	 * @param value is the value to noise
	 * @param noiseLaw is the law used to selected tyhe noise amount.
	 * @return the value 
	 * @throws MathException is case the value is not valid
	 */
	public static float noiseValue(float value, MathFunction noiseLaw) throws MathException {
		try {
			float noise = Math.abs(noiseLaw.f(value));
			initRandomNumberList();
			noise *= uniformRandomVariableList.nextFloat();
			if (uniformRandomVariableList.nextBoolean())
				noise = -noise;
			return value+noise;
		}
		catch(MathException _) {
			return value;
		}
	}
	
}