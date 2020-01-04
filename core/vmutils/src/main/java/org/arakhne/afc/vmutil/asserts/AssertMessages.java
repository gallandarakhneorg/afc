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

package org.arakhne.afc.vmutil.asserts;

import java.util.Arrays;

import org.eclipse.xtext.xbase.lib.Inline;
import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.vmutil.locale.Locale;

/**
 * Provides standard error messages for assertions.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public final class AssertMessages {

	private AssertMessages() {
		//
	}

	/** Parameters must be defined in CCW order.
	 *
	 * @param parameterIndexes the indexes of the formal parameters.
	 * @return the error message.
	 */
	@Pure
	public static String ccwParameters(int... parameterIndexes) {
		return msg("A15", Arrays.toString(parameterIndexes)); //$NON-NLS-1$
	}

	/** The value of Parameter must be <code>true</code>.
	 *
	 * @param parameterIndex the index of the formal parameter.
	 * @param functionName the name of the function that should reply <code>true</code>.
	 * @return the error message.
	 */
	@Pure
	public static String invalidFalseValue(int parameterIndex, String functionName) {
		return msg("A13", functionName); //$NON-NLS-1$
	}

	/** The value of first Parameter must be <code>true</code>.
	 *
	 * @param functionName the name of the function that should reply <code>true</code>.
	 * @return the error message.
	 */
	@Pure
	@Inline(value = "AssertMessages.invalidFalseValue(0, $1)", imported = {AssertMessages.class})
	public static String invalidFalseValue(String functionName) {
		return invalidFalseValue(0, functionName);
	}

	/** The value of Parameter must be <code>false</code>.
	 *
	 * @param parameterIndex the index of the formal parameter.
	 * @param functionName the name of the function that should reply <code>false</code>.
	 * @return the error message.
	 */
	@Pure
	public static String invalidTrueValue(int parameterIndex, String functionName) {
		return msg("A12", functionName); //$NON-NLS-1$
	}

	/** The value of first Parameter must be <code>false</code>.
	 *
	 * @param functionName the name of the function that should reply <code>false</code>.
	 * @return the error message.
	 */
	@Pure
	@Inline(value = "AssertMessages.invalidTrueValue(0, $1)", imported = {AssertMessages.class})
	public static String invalidTrueValue(String functionName) {
		return invalidTrueValue(0, functionName);
	}

	/** The value of Parameter is invalid.
	 *
	 * @return the error message.
	 */
	@Pure
	@Inline(value = "AssertMessages.invalidValue(0)", imported = {AssertMessages.class})
	public static String invalidValue() {
		return invalidValue(0);
	}

	/** The value of Parameter is invalid.
	 *
	 * @param parameterIndex the index of the formal parameter.
	 * @return the error message.
	 */
	@Pure
	public static String invalidValue(int parameterIndex) {
		return msg("A14"); //$NON-NLS-1$
	}

	/** Parameter A must be lower than or equal to the given value.
	 *
	 * @param aindex the index of the parameter A.
	 * @param avalue the value of the parameter A.
	 * @param value the value.
	 * @return the error message.
	 */
	@Pure
	public static String lowerEqualParameter(int aindex, Object avalue, Object value) {
		return msg("A11", aindex, avalue, value); //$NON-NLS-1$
	}

	/** Parameter A must be lower than or equal to Parameter B.
	 *
	 * @param aindex the index of the parameter A.
	 * @param avalue the value of the parameter A.
	 * @param bindex the index of the parameter B.
	 * @param bvalue the value of the parameter B.
	 * @return the error message.
	 */
	@Pure
	public static String lowerEqualParameters(int aindex, Object avalue, int bindex, Object bvalue) {
		return msg("A3", aindex, avalue, bindex, bvalue); //$NON-NLS-1$
	}

	private static String msg(String id, Object... parameters) {
		return Locale.getStringFrom(AssertMessages.class.getPackage().getName() + ".assert_messages", //$NON-NLS-1$
				id, parameters);
	}

	/** Negative or zero parameter.
	 *
	 * @return the error message.
	 */
	@Pure
	@Inline(value = "AssertMessages.negativeOrZeroParameter(0)", imported = {AssertMessages.class})
	public static String negativeOrZeroParameter() {
		return negativeOrZeroParameter(0);
	}

	/** Negative or zero parameter.
	 *
	 * @param parameterIndex the index of the formal parameter.
	 * @return the error message.
	 */
	@Pure
	public static String negativeOrZeroParameter(int parameterIndex) {
		return msg("A7", parameterIndex); //$NON-NLS-1$
	}

	/** First parameter must be normalized.
	 *
	 * @return the error message.
	 */
	@Pure
	@Inline(value = "AssertMessages.normalizedParameter(0)", imported = {AssertMessages.class})
	public static String normalizedParameter() {
		return normalizedParameter(0);
	}

	/** Parameter must be normalized.
	 *
	 * @param parameterIndex the index of the formal parameter.
	 * @return the error message.
	 */
	@Pure
	public static String normalizedParameter(int parameterIndex) {
		return msg("A8", parameterIndex); //$NON-NLS-1$
	}

	/** Parameters must be normalized together.
	 *
	 * @param parameterIndexes the indexes of the formal parameters.
	 * @return the error message.
	 */
	@Pure
	public static String normalizedParameters(int... parameterIndexes) {
		return msg("A9", Arrays.toString(parameterIndexes)); //$NON-NLS-1$
	}

	/** First parameter must be not null.
	 *
	 * @return the error message.
	 */
	@Pure
	@Inline(value = "AssertMessages.notNullParameter(0)", imported = {AssertMessages.class})
	public static String notNullParameter() {
		return notNullParameter(0);
	}

	/** Parameter must be not null.
	 *
	 * @param parameterIndex the index of the formal parameter.
	 * @return the error message.
	 */
	@Pure
	public static String notNullParameter(int parameterIndex) {
		return msg("A4", parameterIndex); //$NON-NLS-1$
	}

	/** One of the parameters must be not null.
	 *
	 * @param parameterIndexes the indexes of the formal parameters.
	 * @return the error message.
	 */
	@Pure
	public static String oneNotNullParameter(int... parameterIndexes) {
		return msg("A10", Arrays.toString(parameterIndexes)); //$NON-NLS-1$
	}

	/** Value is outside range.
	 *
	 * @param parameterIndex the index of the formal parameter.
	 * @param currentValue current value.
	 * @param minValue minimum value in range.
	 * @param maxValue maximum value in range.
	 * @return the error message.
	 */
	@Pure
	public static String outsideRangeInclusiveParameter(int parameterIndex, Object currentValue,
			Object minValue, Object maxValue) {
		return msg("A6", parameterIndex, currentValue, minValue, maxValue); //$NON-NLS-1$
	}

	/** First parameter value is outside range.
	 *
	 * @param currentValue current value.
	 * @param minValue minimum value in range.
	 * @param maxValue maximum value in range.
	 * @return the error message.
	 */
	@Pure
	@Inline(value = "AssertMessages.outsideRangeInclusiveParameter(0, $1, $2, $3)", imported = {AssertMessages.class})
	public static String outsideRangeInclusiveParameter(Object currentValue,
			Object minValue, Object maxValue) {
		return outsideRangeInclusiveParameter(0, currentValue, minValue, maxValue);
	}

	/** Positive or zero parameter.
	 *
	 * @return the error message.
	 */
	@Pure
	@Inline(value = "AssertMessages.positiveOrZeroParameter(0)", imported = {AssertMessages.class})
	public static String positiveOrZeroParameter() {
		return positiveOrZeroParameter(0);
	}

	/** Positive or zero parameter.
	 *
	 * @param parameterIndex the index of the formal parameter.
	 * @return the error message.
	 */
	@Pure
	public static String positiveOrZeroParameter(int parameterIndex) {
		return msg("A1", parameterIndex); //$NON-NLS-1$
	}

	/** Positive parameter.
	 *
	 * @return the error message.
	 */
	@Pure
	@Inline(value = "AssertMessages.positiveStrictlyParameter(0)", imported = {AssertMessages.class})
	public static String positiveStrictlyParameter() {
		return positiveStrictlyParameter(0);
	}

	/** Positive parameter.
	 *
	 * @param parameterIndex the index of the formal parameter.
	 * @return the error message.
	 */
	@Pure
	public static String positiveStrictlyParameter(int parameterIndex) {
		return msg("A16", parameterIndex); //$NON-NLS-1$
	}

	/** Size of the first array parameter is too small.
	 *
	 * @param currentSize current size of the array.
	 * @param expectedSize expected size.
	 * @return the error message.
	 */
	@Pure
	@Inline(value = "AssertMessages.tooSmallArrayParameter(0, $1, $2)", imported = {AssertMessages.class})
	public static String tooSmallArrayParameter(int currentSize, int expectedSize) {
		return tooSmallArrayParameter(0, currentSize, expectedSize);
	}

	/** Size of the array parameter is too small.
	 *
	 * @param parameterIndex the index of the formal parameter.
	 * @param currentSize current size of the array.
	 * @param expectedSize expected size.
	 * @return the error message.
	 */
	@Pure
	public static String tooSmallArrayParameter(int parameterIndex, int currentSize, int expectedSize) {
		return msg("A5", parameterIndex, currentSize, expectedSize); //$NON-NLS-1$
	}

	/** Unsupported primitive type.
	 *
	 * @return the error message.
	 */
	@Pure
	public static String unsupportedPrimitiveType() {
		return msg("A2"); //$NON-NLS-1$
	}

}
