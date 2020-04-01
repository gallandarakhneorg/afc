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

import org.eclipse.xtext.xbase.lib.Functions.Function0;
import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.vmutil.json.JsonBuffer;
import org.arakhne.afc.vmutil.json.JsonableObject;

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
public abstract class StochasticLaw implements MathInversableFunction, JsonableObject {

	/** Name of the property that contains the law's name.
	 */
	protected static final String NAME_NAME = "name"; //$NON-NLS-1$

	/** Construct a stochastic law.
	 */
	public StochasticLaw() {
		//
	}

	/** Replies the name of the law.
	 *
	 * @return the name.
	 */
	protected String getLawName() {
		return getClass().getSimpleName().replace("StochasticLaw", ""); //$NON-NLS-1$ //$NON-NLS-2$
	}

	/** Extract a parameter value from a map of parameters.
	 *
	 * @param paramName is the name of the parameter to extract.
	 * @param defaultValue is the default value.
	 * @param parameters is the map of available parameters
	 * @return the extract value
	 * @since 18.0
	 */
	@Pure
	protected static double paramDouble(String paramName, double defaultValue, Map<String, String> parameters) {
		final String svalue = parameters.get(paramName);
		if (svalue != null && !"".equals(svalue)) { //$NON-NLS-1$
			try {
				return Float.parseFloat(svalue);
			} catch (AssertionError e) {
				throw e;
			} catch (Throwable e) {
				//
			}
		}
		return defaultValue;
	}

	/** Extract a parameter value from a map of parameters.
	 *
	 * @param paramName is the name of the parameter to extract.
	 * @param defaultValue is the default value.
	 * @param parameters is the map of available parameters
	 * @return the extract value
	 * @since 18.0
	 */
	@Pure
	protected static double paramDouble(String paramName, Function0<? extends Double> defaultValue, Map<String, String> parameters) {
		assert defaultValue != null;
		final String svalue = parameters.get(paramName);
		if (svalue != null && !"".equals(svalue)) { //$NON-NLS-1$
			try {
				return Float.parseFloat(svalue);
			} catch (AssertionError e) {
				throw e;
			} catch (Throwable e) {
				//
			}
		}
		return defaultValue.apply();
	}

	/** Extract a parameter value from a map of parameters.
	 *
	 * @param paramName is the name of the parameter to extract.
	 * @param parameters is the map of available parameters
	 * @return the extract value
	 * @throws LawParameterNotFoundException if the parameter was not found or the value is not a double.
	 */
	@Pure
	protected static double paramDouble(String paramName, Map<String, String> parameters)
			throws LawParameterNotFoundException {
		final String svalue = parameters.get(paramName);
		if (svalue != null && !"".equals(svalue)) { //$NON-NLS-1$
			try {
				return Float.parseFloat(svalue);
			} catch (AssertionError e) {
				throw e;
			} catch (Throwable e) {
				//
			}
		}
		throw new LawParameterNotFoundException(paramName);
	}

	/** Extract a parameter value from a map of parameters.
	 *
	 * @param paramName is the name of the parameter to extract.
	 * @param defaultValue the default value of the parameters is not declared.
	 * @param parameters is the map of available parameters
	 * @return the extract value
	 * @since 18.0
	 */
	@Pure
	protected static boolean paramBoolean(String paramName, boolean defaultValue, Map<String, String> parameters) {
		final String svalue = parameters.get(paramName);
		if (svalue != null && !"".equals(svalue)) { //$NON-NLS-1$
			try {
				return Boolean.parseBoolean(svalue);
			} catch (AssertionError e) {
				throw e;
			} catch (Throwable e) {
				//
			}
		}
		return defaultValue;
	}

	/** Extract a parameter value from a map of parameters.
	 *
	 * @param paramName is the name of the parameter to extract.
	 * @param defaultValue the default value of the parameters is not declared.
	 * @param parameters is the map of available parameters
	 * @return the extract value
	 * @since 18.0
	 */
	@Pure
	protected static boolean paramBoolean(String paramName, Function0<? extends Boolean> defaultValue, Map<String, String> parameters) {
		assert defaultValue != null;
		final String svalue = parameters.get(paramName);
		if (svalue != null && !"".equals(svalue)) { //$NON-NLS-1$
			try {
				return Boolean.parseBoolean(svalue);
			} catch (AssertionError e) {
				throw e;
			} catch (Throwable e) {
				//
			}
		}
		return defaultValue.apply();
	}

	/** Extract a parameter value from a map of parameters.
	 *
	 * @param paramName is the name of the parameter to extract.
	 * @param parameters is the map of available parameters
	 * @return the extract value
	 * @throws LawParameterNotFoundException if the parameter was not found or the value is not a double.
	 */
	@Pure
	protected static boolean paramBoolean(String paramName, Map<String, String> parameters)
			throws LawParameterNotFoundException {
		final String svalue = parameters.getOrDefault(paramName, null);
		if (svalue != null && !"".equals(svalue)) { //$NON-NLS-1$
			try {
				return Boolean.parseBoolean(svalue);
			} catch (AssertionError e) {
				throw e;
			} catch (Throwable e) {
				//
			}
		}
		throw new LawParameterNotFoundException(paramName);
	}

	/** Replies a random value that respect
	 * the current stochastic law.
	 *
	 * @return a value depending of the stochastic law parameters
	 * @throws MathException when error in math definition.
	 */
	public double generateRandomValue() throws MathException {
		return StochasticGenerator.generateRandomValue(this);
	}

	/** Replies the x according to the value of the inverted
	 * cummulative distribution function {@code F<sup>-1</sup>(u)}
	 * where {@code u = U(0, 1)}.
	 *
	 * @param u is the uniform random variable generator {@code U(0, 1)}.
	 * @return {@code F<sup>-1</sup>(u)}
	 * @throws MathException in case {@code F<sup>-1</sup>(u)} could not be computed
	 */
	protected double inverseF(Random u) throws MathException {
		return inverseF(1. - u.nextFloat());
	}

	/** Replies the x according to the value of the inverted
	 * cummulative distribution function {@code F<sup>-1</sup>(u)}
	 * where {@code u = U(0, 1)}.
	 *
	 * @param u is a value given by the uniform random variable generator {@code U(0, 1)}.
	 * @return {@code F<sup>-1</sup>(u)}
	 * @throws MathException in case {@code F<sup>-1</sup>(u)} could not be computed
	 */
	@Pure
	@Override
	public abstract double inverseF(double u) throws MathException;

	@Pure
	@Override
	public final String toString() {
		final JsonBuffer buffer = new JsonBuffer();
		toJson(buffer);
		return buffer.toString();
	}

}
