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

import java.util.Map;
import java.util.Random;

/**
 * Abstract implementation of a stochastic law that
 * provides the bounds of a law.
 * 
 * @author $Author: cbohrhauer$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public abstract class StochasticLaw implements MathInversableFunction {

	/** Extract a parameter value from a map of parameters.
	 * 
	 * @param paramName is the nameof the parameter to extract.
	 * @param parameters is the map of available parameters
	 * @return the extract value
	 * @throws LawParameterNotFoundException if the parameter was not found or the value is not a double.
	 */
	protected static double paramFloat(String paramName, Map<String,String> parameters)
	throws LawParameterNotFoundException {
		String sValue = parameters.get(paramName);
		if (sValue!=null && !"".equals(sValue)) { //$NON-NLS-1$
			try {
				return Float.parseFloat(sValue);
			}
			catch(AssertionError e) {
				throw e;
			}
			catch(Throwable e) {
				//
			}
		}
		throw new LawParameterNotFoundException(paramName);
	}
	
	/** Extract a parameter value from a map of parameters.
	 * 
	 * @param paramName is the nameof the parameter to extract.
	 * @param parameters is the map of available parameters
	 * @return the extract value
	 * @throws LawParameterNotFoundException if the parameter was not found or the value is not a double.
	 */
	protected static boolean paramBoolean(String paramName, Map<String,String> parameters)
	throws LawParameterNotFoundException {
		String sValue = parameters.get(paramName);
		if (sValue!=null && !"".equals(sValue)) { //$NON-NLS-1$
			try {
				return Boolean.parseBoolean(sValue);
			}
			catch(AssertionError e) {
				throw e;
			}
			catch(Throwable e) {
				//
			}
		}
		throw new LawParameterNotFoundException(paramName);
	}

	/**
	 * 
	 */
	public StochasticLaw() {
		//
	}
	
	/** Replies a random value that respect
	 * the current stochastic law.
	 * 
	 * @return a value depending of the stochastic law parameters
	 * @throws MathException 
	 */
	public double generateRandomValue() throws MathException {
		return StochasticGenerator.generateRandomValue(this);
	}
	
	/** Replies the x according to the value of the inverted 
	 * cummulative distribution function {@code F<sup>-1</sup>(u)}
	 * where {@code u = U(0,1)}.
	 * 
	 * @param U is the uniform random variable generator {@code U(0,1)}.
	 * @return {@code F<sup>-1</sup>(u)}
	 * @throws MathException in case {@code F<sup>-1</sup>(u)} could not be computed
	 */
	protected double inverseF(Random U) throws MathException {
		return inverseF(1.f-U.nextFloat());
	}

	/** Replies the x according to the value of the inverted 
	 * cummulative distribution function {@code F<sup>-1</sup>(u)}
	 * where {@code u = U(0,1)}.
	 * 
	 * @param u is a value given by the uniform random variable generator {@code U(0,1)}.
	 * @return {@code F<sup>-1</sup>(u)}
	 * @throws MathException in case {@code F<sup>-1</sup>(u)} could not be computed
	 */
	@Override
	public abstract double inverseF(double u) throws MathException;

}