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

package org.arakhne.afc.testtools;

import static org.junit.Assert.fail;

import java.awt.geom.Point2D;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Random;

import org.junit.Assert;
import org.junit.ComparisonFailure;

/** Abstract class that is providing a base for unit tests.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@SuppressWarnings("checkstyle:methodcount")
public abstract class AbstractTestCase extends EnableAssertion {

	/** Precision of the floating point number epsilon-tests.
	 */
	public static final int DEFAULT_DECIMAL_COUNT = 8;

	/** Precision of the floating point number epsilon-tests.
	 */
	public static final double EPSILON = 10 * 1.110223024E-16;

	private int decimalPrecision = DEFAULT_DECIMAL_COUNT;

	/** Random number sequence.
	 */
	private final Random random = new Random();

	/** Replies the random number generator.
	 *
	 * @return the random number generator.
	 */
	public Random getRandom() {
		return this.random;
	}

	/** Set the epsilon used be testing floating-point values.
	 *
	 * @param precision is the count of decimal digits to support
	 */
	public void setDecimalPrecision(int precision) {
		this.decimalPrecision = Math.max(0, precision);
	}

	/** Set the epsilon used be testing floating-point values to
	 * its default value.
	 */
	public void setDefaultDecimalPrecision() {
		this.decimalPrecision = DEFAULT_DECIMAL_COUNT;
	}

	/** Replies if two values are equals at espilon.
	 *
	 * @param v1 the first value.
	 * @param v2 the second value.
	 * @param isNaNEqual indicates if the NaN value is equals to itself.
	 * @return <code>true</code> or <code>false</code>
	 */
	public boolean isEpsilonEquals(double v1, double v2, boolean isNaNEqual) {
		if (v1 == v2) {
			return true;
		}
		final boolean nanA = Double.isNaN(v1);
		final boolean nanB = Double.isNaN(v2);
		if (nanA || nanB) {
			if (isNaNEqual) {
				return nanA == nanB;
			}
			return false;
		}
		if (!Double.isInfinite(v1) && !Double.isInfinite(v1)
			&& !Double.isNaN(v1) && !Double.isNaN(v2)) {
			return isEpsilonEquals(new BigDecimal(v1), new BigDecimal(v2), this.decimalPrecision / 2);
		}
		return false;
	}

	/** Replies if two values are equals at espilon.
	 *
	 * @param v1 the first value.
	 * @param v2 the second value.
	 * @return <code>true</code> or <code>false</code>
	 */
	public boolean isEpsilonEquals(double v1, double v2) {
		return isEpsilonEquals(v1, v2, true);
	}

	/** Replies if two values are equals at espilon.
	 *
	 * @param v1 the first value.
	 * @param v2 the second value.
	 * @return <code>true</code> or <code>false</code>
	 */
	public boolean isEpsilonEquals(BigDecimal v1, BigDecimal v2) {
		return isEpsilonEquals(v1, v2, this.decimalPrecision);
	}

	/** Replies if two values are equals at espilon.
	 *
	 * @param v1 the first value.
	 * @param v2 the second value.
	 * @param precision is the number of decimal digits to test.
	 * @return <code>true</code> or <code>false</code>
	 */
	public static boolean isEpsilonEquals(BigDecimal v1, BigDecimal v2, int precision) {
		final BigDecimal ma = v1.movePointRight(precision);
		final BigDecimal mb = v2.movePointRight(precision);
		BigDecimal aa = ma.setScale(0, BigDecimal.ROUND_HALF_UP);
		BigDecimal bb = mb.setScale(0, BigDecimal.ROUND_HALF_UP);
		if (aa.compareTo(bb) == 0) {
			return true;
		}
		aa = ma.setScale(0, BigDecimal.ROUND_DOWN);
		bb = mb.setScale(0, BigDecimal.ROUND_DOWN);
		return aa.compareTo(bb) == 0;
	}

	/**
	 * Replies if two arrays have the same values at epsilon.
	 *
	 * @param v1 the first value.
	 * @param v2 the second value.
	 * @return <code>true</code> if the two arrays are equal, otherwise
	 * <code>false</code>.
	 */
	public boolean isEpsilonEquals(double[] v1, double[] v2) {
		if (v1 == v2) {
			return true;
		}
		if (v1 == null && v2 != null) {
			return false;
		}
		if (v1 != null && v2 == null) {
			return false;
		}
		assert v1 != null && v2 != null;
		if (v1.length != v2.length) {
			return false;
		}
		for (int i = 0; i < v1.length; ++i) {
			if (!isEpsilonEquals(v1[i], v2[i])) {
				return false;
			}
		}
		return true;
	}

	/** Test if the actual value is not equal to the expected value with
	 * a distance of epsilon.
	 *
	 * @param expected the expected value.
	 * @param actual the actual value.
	 */
	public static void assertNotEquals(int expected, int actual) {
		assertNotEquals(null, expected, actual);
	}

	/** Test if the actual value is not equal to the expected value with
	 * a distance of epsilon.
	 *
	 * @param message the error message.
	 * @param expected the expected value.
	 * @param actual the actual value.
	 */
	public static void assertNotEquals(String message, int expected, int actual) {
		if (expected == actual) {
			fail(formatFailMessage(message, "same value, expecting not equal to:", expected, actual)); //$NON-NLS-1$
		}
	}

	/** Test if the given value is not a number.
	 *
	 * @param value the value to test.
	 */
	public static void assertNaN(double value) {
		if (!Double.isNaN(value)) {
			fail(formatFailMessage(null, "Expecting NaN", value)); //$NON-NLS-1$
		}
	}

	/** Test if the given value is not a number.
	 *
	 * @param value the value to test.
	 */
	public static void assertNaN(float value) {
		if (!Float.isNaN(value)) {
			fail(formatFailMessage(null, "Expecting NaN", value)); //$NON-NLS-1$
		}
	}

	/** Test if the given value is equal to zero.
	 *
	 * @param value the value to test.
	 */
	public static void assertZero(byte value) {
		if (value != 0) {
			fail(formatFailMessage(null, "Expecting zero", value)); //$NON-NLS-1$
		}
	}

	/** Test if the given value is equal to zero.
	 *
	 * @param value the value to test.
	 */
	public static void assertZero(short value) {
		if (value != 0) {
			fail(formatFailMessage(null, "Expecting zero", value)); //$NON-NLS-1$
		}
	}

	/** Test if the given value is equal to zero.
	 *
	 * @param value the value to test.
	 */
	public static void assertZero(int value) {
		if (value != 0) {
			fail(formatFailMessage(null, "Expecting zero", value)); //$NON-NLS-1$
		}
	}

	/** Test if the given value is equal to zero.
	 *
	 * @param value the value to test.
	 */
	public static void assertZero(long value) {
		if (value != 0) {
			fail(formatFailMessage(null, "Expecting zero", value)); //$NON-NLS-1$
		}
	}

	/** Test if the given value is equal to zero.
	 *
	 * @param value the value to test.
	 */
	public static void assertZero(float value) {
		if (value != 0f) {
			fail(formatFailMessage(null, "Expecting zero", value)); //$NON-NLS-1$
		}
	}

	/** Test if the given value is equal to zero.
	 *
	 * @param value the value to test.
	 */
	public static void assertZero(double value) {
		assertZero(null, value);
	}

	/** Test if the given value is equal to zero.
	 *
	 * @param message the error message.
	 * @param value the value to test.
	 */
	public static void assertZero(String message, double value) {
		if (value != 0.) {
			fail(formatFailMessage(message, "Expecting zero", value)); //$NON-NLS-1$
		}
	}

	/** Test if the given value is near to zero.
	 *
	 * @param value the value to test.
	 */
	public void assertEpsilonZero(double value) {
		assertEpsilonZero(null, value);
	}

	/** Test if the given value is near to zero.
	 *
	 * @param message the error message.
	 * @param value the value to test.
	 */
	public void assertEpsilonZero(String message, double value) {
		if (!isEpsilonEquals(value, 0.)) {
			fail(formatFailMessage(message, "Expecting zero", value)); //$NON-NLS-1$
		}
	}

	/** Assert two iterable objects have the same elements.
	 *
	 * @param <T> the type of the elements in the iterable objects.
	 * @param expected the expected value.
	 * @param actual the actual value.
	 */
	public static <T> void assertCollectionEquals(Iterable<? extends T> expected, Iterable<? extends T> actual) {
		final Iterator<? extends T> it1 = expected.iterator();
		final Iterator<? extends T> it2 = actual.iterator();
		while (it1.hasNext()) {
			if (!it2.hasNext()) {
				throw new ComparisonFailure(
						formatFailMessage(null, "Element is missed", expected, actual), //$NON-NLS-1$
						toString(expected), toString(actual));
			}
			final T expect = it1.next();
			final T act = it2.next();
			if (!Objects.equals(expect, act)) {
				throw new ComparisonFailure(formatFailMessage(null, "Not same element", expected, actual), //$NON-NLS-1$
						toString(expected), toString(actual));
			}
		}
		if (it2.hasNext()) {
			throw new ComparisonFailure(formatFailMessage(null, "Too many elements", expected, actual), //$NON-NLS-1$
					toString(expected), toString(actual));
		}
	}

	private static String toString(Iterable<?> it) {
		final StringBuilder b = new StringBuilder();
		for (final Object o : it) {
			b.append(o);
			b.append(",\n"); //$NON-NLS-1$
		}
		return b.toString();
	}

	@SuppressWarnings({"checkstyle:returncount", "checkstyle:npathcomplexity"})
	private static String arrayToString(Object obj) {
		if (obj == null) {
			return null;
		}
		if (obj instanceof boolean[]) {
			return Arrays.toString((boolean[]) obj);
		}
		if (obj instanceof byte[]) {
			return Arrays.toString((byte[]) obj);
		}
		if (obj instanceof char[]) {
			return Arrays.toString((char[]) obj);
		}
		if (obj instanceof short[]) {
			return Arrays.toString((short[]) obj);
		}
		if (obj instanceof int[]) {
			return Arrays.toString((int[]) obj);
		}
		if (obj instanceof long[]) {
			return Arrays.toString((long[]) obj);
		}
		if (obj instanceof float[]) {
			return Arrays.toString((float[]) obj);
		}
		if (obj instanceof double[]) {
			return Arrays.toString((double[]) obj);
		}
		if (obj instanceof Object[]) {
			return Arrays.toString((Object[]) obj);
		}
		return obj.toString();
	}

	/** Replies if the given elements is in the array.
	 *
	 * <p>This function is based on {@link Object#equals(java.lang.Object)}.
	 */
	private static <T> boolean arrayContainsAll(T[] elts, T[] array) {
		boolean found;
		for (final T elt : elts) {
			found = false;
			for (final T t : array) {
				if ((t == elt)
					|| ((t != null) && (t.equals(elt)))) {
					found = true;
					break;
				}
			}
			if (!found) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Format a failure message for invalid value.
	 *
	 * @param message is the message to reply.
	 * @param expected is the expected object.
	 * @param actual is the actual value of the object.
	 * @return the message
	 */
	protected static String formatFailMessage(String message, Object expected, Object actual) {
		final StringBuilder formatted = new StringBuilder();
		if (message != null) {
			formatted.append(message);
			formatted.append(' ');
		}
		formatted.append("expected: <"); //$NON-NLS-1$
		formatted.append(arrayToString(expected));
		formatted.append("> but was: <"); //$NON-NLS-1$
		formatted.append(arrayToString(actual));
		formatted.append(">"); //$NON-NLS-1$
		return formatted.toString();
	}

	/**
	 * Format a failure message.
	 *
	 * @param message the first part of the message (optional).
	 * @param msg the second part of the message.
	 * @return the message
	 */
	protected static String formatFailMessage(String message, String msg) {
		final StringBuilder formatted = new StringBuilder();
		if (message != null) {
			formatted.append(message);
			formatted.append(' ');
		}
		formatted.append(msg);
		return formatted.toString();
	}

	/**
	 * Format a failure message for invalid value.
	 *
	 * @param message is the first part of the message (optional).
	 * @param msg is the second part of the message (mandatory).
	 * @param expected is the expected object.
	 * @param actual is the actual value of the object.
	 * @return the message
	 */
	protected static String formatFailMessage(String message, String msg, Object expected, Object actual) {
		final StringBuilder formatted = new StringBuilder();
		if (message != null) {
			formatted.append(message);
			formatted.append(' ');
		}
		formatted.append(msg);
		formatted.append("; expected: <"); //$NON-NLS-1$
		formatted.append(arrayToString(expected));
		formatted.append("> but was: <"); //$NON-NLS-1$
		formatted.append(arrayToString(actual));
		formatted.append(">"); //$NON-NLS-1$
		return formatted.toString();
	}

	/**
	 * Format a failure message for invalid value.
	 *
	 * @param message is the first part of the message (optional).
	 * @param msg is the second part of the message (mandatory).
	 * @param actual is the actual value of the object.
	 * @return the message
	 */
	protected static String formatFailMessage(String message, String msg, Object actual) {
		final StringBuilder formatted = new StringBuilder();
		if (message != null) {
			formatted.append(message);
			formatted.append(' ');
		}
		formatted.append(msg);
		formatted.append(" but was: <"); //$NON-NLS-1$
		formatted.append(arrayToString(actual));
		formatted.append(">"); //$NON-NLS-1$
		return formatted.toString();
	}

	/** Asserts that the actual object is equal to one of the expected objects. If not
	 * an AssertionFailedError is thrown with the given message.
	 *
	 * @param message is the error message to put inside the assertion.
	 * @param expectedObjects are the set of expected values during the unit test.
	 * @param actual is the actual value of the object in the unit test.
	 */
	public static void assertContains(String message, Object[] expectedObjects, Object actual) {
		if ((expectedObjects != null) && (expectedObjects.length > 0)) {
			for (final Object object : expectedObjects) {
				if ((object == null) && (actual == null)) {
					return;
				}
				if ((object != null) && (object.equals(actual))) {
					return;
				}
			}
		}
		fail(formatFailMessage(message, expectedObjects, actual));
	}

	/** Asserts that the actual object is equal to one of the expected objects. If not
	 * an AssertionFailedError is thrown.
	 *
	 * @param expectedObjects are the set of expected values during the unit test.
	 * @param actual is the actual value of the object in the unit test.
	 */
	public static void assertContains(Object[] expectedObjects, Object actual) {
	    assertContains(null, expectedObjects, actual);
	}

	/** Asserts that the actual object is equal to one of the expected objects. If not
	 * an AssertionFailedError is thrown.
	 * This assertion function tests the types of its parameters to call the best
	 * {@code assertEquals} function.
	 *
	 * @param expected is the expected value during the unit test.
	 * @param actual is the actual value of the object during the unit test.
	 * @see Assert#assertEquals(Object, Object)
	 * @see #assertContains(Object[], Object)
	 */
	public static void assertEqualsGeneric(Object expected, Object actual) {
		assertEqualsGeneric(null, expected, actual);
	}

	/** Asserts that the actual object is equal to one of the expected objects. If not
	 * an AssertionFailedError is thrown.
	 * This assertion function tests the types of its parameters to call the best
	 * {@code assertEquals} function.
	 *
	 * @param message is the error message to put inside the assertion.
	 * @param expected is the expected value during the unit test.
	 * @param actual is the actual value of the object during the unit test.
	 * @see Assert#assertEquals(Object, Object)
	 * @see #assertContains(Object[], Object)
	 */
	public static void assertEqualsGeneric(String message, Object expected, Object actual) {
		if ((expected != null) && (actual != null) && (expected.getClass().isArray())) {
			if (actual.getClass().isArray()) {
				assertContains(message, (Object[]) expected, (Object[]) actual);
			} else {
				assertContains(message, (Object[]) expected, actual);
			}
		} else {
			Assert.assertEquals(message, expected, actual);
		}
	}

	/** Asserts that the actual similar is equal to one of the expected objects. If not
	 * an AssertionFailedError is thrown.
	 *
	 * @param <T> is the type of the values
	 * @param expectedObjects are the expected values during the unit test.
	 * @param actual are the actual values of the objects during the unit test.
	 */
	public static <T> void assertSimilars(T[] expectedObjects, T[] actual) {
	    assertSimilars(null, expectedObjects, actual);
	}

	/** Asserts that the actual similar is equal to one of the expected objects. If not
	 * an AssertionFailedError is thrown.
	 *
	 * @param <T> is the type of the values
	 * @param message is the error message to put inside the assertion.
	 * @param expectedObjects are the expected values during the unit test.
	 * @param actual are the actual values of the objects during the unit test.
	 */
	public static <T> void assertSimilars(String message, T[] expectedObjects, T[] actual) {
		if (expectedObjects == actual) {
			return;
		}
		if ((arrayContainsAll(expectedObjects, actual)) && (arrayContainsAll(actual, expectedObjects))) {
			return;
		}
		fail(formatFailMessage(message, expectedObjects, actual));
	}

	/** Asserts that the actual similar is equal to one of the expected objects. If not
	 * an AssertionFailedError is thrown.
	 *
	 * @param <T> is the type of the values
	 * @param expectedObjects are the expected values during the unit test.
	 * @param actual are the actual values of the objects during the unit test.
	 */
	public static <T> void assertSimilars(Collection<T> expectedObjects, Collection<T> actual) {
	    assertSimilars(null, expectedObjects, actual);
	}

	/** Asserts that the actual similar is equal to one of the expected objects. If not
	 * an AssertionFailedError is thrown.
	 *
	 * @param <T> is the type of the values
	 * @param message is the error message to put inside the assertion.
	 * @param expectedObjects are the expected values during the unit test.
	 * @param actual are the actual values of the objects during the unit test.
	 */
	public static <T> void assertSimilars(String message, Collection<T> expectedObjects, Collection<T> actual) {
		if (expectedObjects == actual) {
			return;
		}
		if (similars(expectedObjects, actual)) {
			return;
		}
		fail(formatFailMessage(message, expectedObjects, actual));
	}

	/** Asserts that the actual object is equal to one of the expected objects. If not
	 * an AssertionFailedError is thrown.
	 *
	 * @param <T> is the type of the values
	 * @param <X> is the element's type of the values if they are arrays.
	 * @param message is the error message to put inside the assertion.
	 * @param expected is the expected value during the unit test.
	 * @param actual are the actual value of the object during the unit test.
	 */
	@SuppressWarnings("unchecked")
	public static <T, X> void assertSimilars(String message, T expected, T actual) {
		if (expected == actual) {
			return;
		}
		if (expected instanceof Collection) {
			assertSimilars(message, (Collection<?>) expected, (Collection<?>) actual);
		} else if (expected instanceof Point2D) {
			assertSimilars(message, (Point2D) expected, (Point2D) actual);
		} else if (expected instanceof Date) {
			assertSimilars(message, (Date) expected, (Date) actual);
		} else if (expected.getClass().isArray()) {
			assertSimilars(message, (X[]) expected, (X[]) actual);
		} else {
			Assert.assertEquals(message, expected, actual);
		}
	}

	private static <T> boolean similars(Collection<T> c1, Collection<T> c2) {
		final List<T> a = new ArrayList<>();
		a.addAll(c2);
		for (final T elt : c1) {
			if (!a.remove(elt)) {
				return false;
			}
		}
		return a.isEmpty();
	}

	/** Asserts that the actual object is equal to one of the expected objects. If not
	 * an AssertionFailedError is thrown.
	 *
	 * @param <T> is the type of the values.
	 * @param <X> is the element's type of the values if they are arrays.
	 * @param message is the error message to put inside the assertion.
	 * @param expected is the expected value.
	 * @param actual is the current value.
	 */
	public static <T, X> void assertNotSimilars(String message, T expected, T actual) {
		if (expected != actual) {
			try {
				assertSimilars(message, expected, actual);
			} catch (Throwable exception) {
				// ok
				return;
			}
		}
		fail(formatFailMessage(message, expected, actual));
	}

	/** Replies if the two objects are similars.
	 *
	 * @param <T> is the type of the values
	 * @param <X> is the element's type of the values if they are arrays.
	 * @param obj1 the first object.
	 * @param obj2 the second object.
	 * @return <code>true</code> if the objects are similar, otherwise <code>false</code>
	 */
	public static <T, X> boolean isSimilarObjects(T obj1, T obj2) {
		if (obj1 == obj2) {
			return true;
		}
		try {
			assertSimilars(null, obj1, obj2);
			return true;
		} catch (Throwable exception) {
			return false;
		}
	}

	/** Asserts that the actual similar is equal to one of the expected objects. If not
	 * an AssertionFailedError is thrown.
	 *
	 * @param <K> is the type of the map keys.
	 * @param <V> is the type of the map values.
	 * @param expectedObjects is the expected map.
	 * @param actual is the current map.
	 */
	public static <K, V> void assertDeepSimilars(Map<K, V> expectedObjects, Map<K, V> actual) {
	    assertDeepSimilars(null, expectedObjects, actual);
	}

	/** Asserts that the actual similar is equal to one of the expected objects. If not
	 * an AssertionFailedError is thrown.
	 *
	 * @param <K> is the type of the map keys.
	 * @param <V> is the type of the map values.
	 * @param message is the error message to put inside the assertion.
	 * @param expectedObjects is the expected map.
	 * @param actual is the current map.
	 */
	public static <K, V> void assertDeepSimilars(String message, Map<K, V> expectedObjects, Map<K, V> actual) {
		if (expectedObjects == actual) {
			return;
		}
		if (similars(expectedObjects.keySet(), actual.keySet())) {
			for (final Entry<K, V> entry : expectedObjects.entrySet()) {
				final V v1 = entry.getValue();
				final V v2 = actual.get(entry.getKey());
				assertSimilars(message, v1, v2);
			}
			// all values are correct
			return;
		}
		fail(formatFailMessage(message, expectedObjects, actual));
	}

	/** Asserts that the actual similar is equal to one of the expected objects. If not
	 * an AssertionFailedError is thrown.
	 *
	 * @param <K> is the type of the map keys.
	 * @param <V> is the type of the map values.
	 * @param expectedObjects is the expected map.
	 * @param actual is the current map.
	 */
	public static <K, V> void assertNotDeepSimilars(Map<K, V> expectedObjects, Map<K, V> actual) {
	    assertNotDeepSimilars(null, expectedObjects, actual);
	}

	/** Asserts that the actual similar is equal to one of the expected objects. If not
	 * an AssertionFailedError is thrown.
	 *
	 * @param <K> is the type of the map keys.
	 * @param <V> is the type of the map values.
	 * @param message is the error message to put inside the assertion.
	 * @param expectedObjects is the expected map.
	 * @param actual is the current map.
	 */
	public static <K, V> void assertNotDeepSimilars(String message, Map<K, V> expectedObjects, Map<K, V> actual) {
		if (expectedObjects != actual) {
			if (!similars(expectedObjects.keySet(), actual.keySet())) {
				return;
			}

			for (final Entry<K, V> entry : expectedObjects.entrySet()) {
				final V v1 = entry.getValue();
				final V v2 = actual.get(entry.getKey());
				if (!isSimilarObjects(v1, v2)) {
					return;
				}
			}
		}
		fail(formatFailMessage(message, expectedObjects, actual));
	}

	/** Asserts that the specified value is stricly negative.
	 *
	 * @param number the value.
	 */
	public static void assertStrictlyNegative(Number number) {
		assertStrictlyNegative(null, number);
	}

	/** Asserts that the specified value is stricly negative.
	 *
	 * @param message is the error message to put inside the assertion.
	 * @param number  the value.
	 */
	public static void assertStrictlyNegative(String message, Number number) {
		if (number.doubleValue() < 0.) {
			return;
		}
		fail(formatFailMessage(message, "expected negative value", number)); //$NON-NLS-1$
	}

	/** Asserts that the specified value is stricly positive.
	 *
	 * @param number the value.
	 */
	public static void assertStrictlyPositive(Number number) {
		assertStrictlyPositive(null, number);
	}

	/** Asserts that the specified value is stricly positive.
	 *
	 * @param message is the error message to put inside the assertion.
	 * @param number the value.
	 */
	public static void assertStrictlyPositive(String message, Number number) {
		if (number.doubleValue() > 0.) {
			return;
		}
		fail(formatFailMessage(message, "expected positive value", number)); //$NON-NLS-1$
	}

	/** Asserts that the specified value is negative.
	 *
	 * @param number the value.
	 */
	public static void assertNegative(Number number) {
		assertNegative(null, number);
	}

	/** Asserts that the specified value is negative.
	 *
	 * @param message is the error message to put inside the assertion.
	 * @param number the value.
	 */
	public static void assertNegative(String message, Number number) {
		if (number.doubleValue() <= 0.) {
			return;
		}
		fail(formatFailMessage(message, "expected negative value", number)); //$NON-NLS-1$
	}

	/** Asserts that the specified value is positive.
	 *
	 * @param number the value.
	 */
	public static void assertPositive(Number number) {
		assertPositive(null, number);
	}

	/** Asserts that the specified value is positive.
	 *
	 * @param message is the error message to put inside the assertion.
	 * @param number the value.
	 */
	public static void assertPositive(String message, Number number) {
		if (number.doubleValue() >= 0.) {
			return;
		}
		fail(formatFailMessage(message, "expected positive value", number)); //$NON-NLS-1$
	}

	/**
	 * Replies an array which is containing values randomly selected from the parameter.
	 * Duplicate values are allowed.
	 *
	 * @param <T> is the type of the values.
	 * @param availableValues is the collection of available values.
	 * @return the selected values (possible duplicated)
	 */
	@SuppressWarnings("unchecked")
	public <T> T[] extractRandomValues(T[] availableValues) {
		final int count = getRandom().nextInt(500);
		final List<T> tab = new ArrayList<>(count);
		for (int i = 0; i < count; ++i) {
			tab.add(availableValues[getRandom().nextInt(availableValues.length)]);
		}
		final Class<?> clazz = availableValues.getClass().getComponentType();
		final T[] array = (T[]) Array.newInstance(clazz, tab.size());
		tab.toArray(array);
		tab.clear();
		return array;
	}

	/** Replies a randomized string.
	 *
	 * @return a random string.
	 */
	public String randomString() {
		return randomString(-1);
	}

	/** Replies a randomized string with a max length.
	 *
	 * @param maxSize is the max length of the string.
	 * @return a random string with a max length.
	 */
	@SuppressWarnings("checkstyle:magicnumber")
	public String randomString(int maxSize) {
		final StringBuilder b = new StringBuilder();
		final int count = getRandom().nextInt(maxSize <= 0 ? 255 : maxSize - 1) + 1;
		for (int i = 0; i < count; ++i) {
			final char c = (char) ('A' + getRandom().nextInt(26));
			b.append(c);
		}
		return b.toString();
	}

	/** Random a value from an enumeration.
	 *
	 * @param <E> is the type of the enumeration
	 * @param type is the type of the enumeration
	 * @return a value from the specified enumeration.
	 */
	public <E extends Enum<E>> E randomEnum(Class<E> type) {
		final E[] constants = type.getEnumConstants();
		return constants[getRandom().nextInt(constants.length)];
	}

	/** Assert the the specified method thrown an exception.
	 *
	 * @param self is the calling object
	 * @param method is the name of the method to invoke
	 * @param types is the parameter types of the method
	 * @param parameters is the parameter values to pass at invocation.
	 */
	public static void assertException(Object self, String method, Class<?>[] types, Object[] parameters) {
		assertException((String) null, self, method, types, parameters);
	}

	/** Assert the the specified method thrown an exception.
	 *
	 * @param message is the error message to put in the assertion.
	 * @param self is the calling object
	 * @param method is the name of the method to invoke
	 * @param types is the parameter types of the method
	 * @param parameters is the parameter values to pass at invocation.
	 */
	public static void assertException(String message, Object self, String method, Class<?>[] types, Object[] parameters) {
		try {
			final Class<?> clazz = self.getClass();
			final Method methodFunc = clazz.getMethod(method, types);
			methodFunc.invoke(self, parameters);
			fail(formatFailMessage(message, "An exception was attempted but never thrown.")); //$NON-NLS-1$
		} catch (Exception exception) {
			// Expected behavior
		}
	}

	/** Assert the the specified method thrown an exception.
	 *
	 * @param self is the calling object
	 * @param method is the name of the method to invoke
	 */
	public static void assertException(Object self, String method) {
		assertException((String) null, self, method, new Class<?>[0], new Object[0]);
	}

	/** Assert the the specified method thrown an exception.
	 *
	 * @param message is the error message to put in the assertion.
	 * @param self is the calling object
	 * @param method is the name of the method to invoke
	 */
	public static void assertException(String message, Object self, String method) {
		assertException(message, self, method, new Class<?>[0], new Object[0]);
	}

	/** Test if the given method entity throws the specified exception on the given entity.
	 *
	 * @param expectedException the expected exception.
	 * @param object the object that is supposed to generate the exception.
	 * @param methodName the method that is supposed to generate the exception.
	 * @param values are the values to pass to the methods.
	 */
	protected static void assertException(Class<? extends Throwable> expectedException,
			Object object, String methodName, Object... values) {
		assertException(null, expectedException, object, methodName, values);
	}

	/** Test if the given method entity throws the specified exception on the given entity.
	 *
	 * @param message the error message.
	 * @param expectedException the expected exception.
	 * @param object the object that is supposed to generate the exception.
	 * @param methodName the method that is supposed to generate the exception.
	 * @param values are the values to pass to the methods.
	 */
	protected static void assertException(String message, Class<? extends Throwable> expectedException,
			Object object, String methodName, Object... values) {
		final Class<?>[] types = new Class<?>[values.length];
		for (int idx = 0; idx < types.length; ++idx) {
			types[idx] = values[idx].getClass();
		}
		assertException(message, expectedException, object, methodName, types, values);
	}

	/** Test if the given method entity throws the specified exception on the given entity.
	 *
	 * @param expectedException the expected exception.
	 * @param object the object that is supposed to generate the exception.
	 * @param methodName the method that is supposed to generate the exception.
	 * @param types are the types of the method parameters.
	 * @param values are the values to pass to the methods.
	 */
	protected static void assertException(Class<? extends Throwable> expectedException,
			Object object, String methodName, Class<?>[] types, Object... values) {
		assertException(null, expectedException, object, methodName, types, values);
	}

	/** Test if the given method entity throws the specified exception on the given entity.
	 *
	 * @param message the error message.
	 * @param expectedException the expected exception.
	 * @param object the object that is supposed to generate the exception.
	 * @param methodName the method that is supposed to generate the exception.
	 * @param types are the types of the method parameters.
	 * @param values are the values to pass to the methods.
	 */
	protected static void assertException(String message, Class<? extends Throwable> expectedException,
			Object object, String methodName, Class<?>[] types, Object... values) {
		assert object != null;
		final Class<?> objType;
		Object obj = object;
		if (obj instanceof Class<?>) {
			objType = (Class<?>) obj;
			obj = null;
		} else {
			objType = obj.getClass();
		}
		Method method = null;
		Throwable t = null;
		try {
			method = objType.getMethod(methodName, types);
			if (method == null) {
				fail(formatFailMessage(message, "unable to find the method " + methodName)); //$NON-NLS-1$
				return;
			}
		} catch (Exception exception) {
			fail(formatFailMessage(message, "unable to find the method " + methodName)); //$NON-NLS-1$
			return;
		}
		try {
			method.invoke(obj, values);
		} catch (InvocationTargetException e) {
			if (expectedException.equals(e.getCause().getClass())) {
				return;
			}
			t = e.getCause();
		} catch (Throwable e) {
			if (expectedException.equals(e.getClass())) {
				return;
			}
			t = e;
		}
		if (t != null) {
			fail(formatFailMessage(message, "the method " + methodName //$NON-NLS-1$
					+ " does not thrown the expected exception of type " + expectedException //$NON-NLS-1$
					+ ". An exception of type " + t.getClass().getName() + " is thrown insteed.")); //$NON-NLS-1$ //$NON-NLS-2$
		} else {
			fail(formatFailMessage(message, "the method " + methodName //$NON-NLS-1$
					+ " does not thrown the expected exception of type " + expectedException //$NON-NLS-1$
					+ ". No exception was thrown insteed.")); //$NON-NLS-1$
		}
	}

	/** Test if the actual value is equal to the expected value with
	 * a distance of epsilon.
	 *
	 * @param expected the expected value.
	 * @param actual the actual value.
	 */
	public void assertEpsilonEquals(double expected, double actual) {
		assertEpsilonEquals(null, expected, actual);
	}

	/** Test if the actual value is equal to the expected value with
	 * a distance of epsilon.
	 *
	 * @param message the error message.
	 * @param expected the expected value.
	 * @param actual the actual value.
	 */
	public void assertEpsilonEquals(String message, double expected, double actual) {
		if (isEpsilonEquals(expected, actual)) {
			return;
		}
		throw new ComparisonFailure(
				formatFailMessage(message, "not same double value.", expected, actual), //$NON-NLS-1$
				Double.toString(expected),
				Double.toString(actual));
	}

	/** Test if the two collections contain the same elements without
	 * taking into account the order of the elements in the collections.
	 *
	 * @param <T> the type of the elements in the collections.
	 * @param expected the expected collection.
	 * @param actual the actual collection.
	 */
	public static <T> void assertEpsilonEquals(Collection<? extends T> expected, Collection<? extends T> actual) {
		assertEpsilonEquals(null, expected, actual);
	}

	/** Test if the two collections contain the same elements without
	 * taking into account the order of the elements in the collections.
	 *
	 * @param <T> the type of the elements in the collections.
	 * @param message the error message.
	 * @param expected the expected collection.
	 * @param actual the actual collection.
	 */
	public static <T> void assertEpsilonEquals(String message, Collection<? extends T> expected, Collection<? extends T> actual) {
		final List<T> l = new ArrayList<>(actual);
		for (final T e : expected) {
			if (!l.remove(e)) {
				throw new ComparisonFailure(
						formatFailMessage(message, "not similar collections", expected, actual), //$NON-NLS-1$
						expected.toString(), actual.toString());
			}
		}
		if (!l.isEmpty()) {
			throw new ComparisonFailure(
					formatFailMessage(message, "not similar collections, not expected elements", expected, actual), //$NON-NLS-1$
					expected.toString(), actual.toString());
		}
	}

	/** Test if the two collections contain the same elements without
	 * taking into account the order of the elements in the collections.
	 *
	 * @param <T> the type of the elements in the collections.
	 * @param expected the expected collection.
	 * @param actual the actual collection.
	 */
	public static <T> void assertEpsilonEquals(T[] expected, T[] actual) {
		assertEpsilonEquals(null, expected, actual);
	}

	/** Test if the two collections contain the same elements without
	 * taking into account the order of the elements in the collections.
	 *
	 * @param <T> the type of the elements in the collections.
	 * @param message the error message.
	 * @param expected the expected collection.
	 * @param actual the actual collection.
	 */
	public static <T> void assertEpsilonEquals(String message, T[] expected, T[] actual) {
		final List<T> l = new ArrayList<>(Arrays.asList(actual));
		for (final T e : expected) {
			if (!l.remove(e)) {
				throw new ComparisonFailure(
						formatFailMessage(message, "not similar collections", expected, actual), //$NON-NLS-1$
						Arrays.toString(expected), Arrays.toString(actual));
			}
		}
		if (!l.isEmpty()) {
			throw new ComparisonFailure(
					formatFailMessage(message, "not similar collections, not expected elements", expected, actual), //$NON-NLS-1$
					Arrays.toString(expected), Arrays.toString(actual));
		}
	}

	/** Test if the two date are equals with an epsilon.
	 *
	 * @param expected the expected date.
	 * @param actual the actual date.
	 */
	protected static void assertEpsilonEquals(Date expected, Date actual) {
		if (expected == actual) {
			return;
		}
		if (expected == null && actual != null) {
			throw new ComparisonFailure(formatFailMessage("not same", expected, actual), //$NON-NLS-1$
					null,
					actual.toString());
		}
		if (expected != null && actual == null) {
			throw new ComparisonFailure(formatFailMessage("not same", expected, actual), //$NON-NLS-1$
					expected.toString(),
					null);
		}
		assert expected != null && actual != null;
		final DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd"); //$NON-NLS-1$
		final String expectedStr = fmt.format(expected);
		final String actualStr = fmt.format(expected);
		if (expectedStr.equals(actualStr)) {
			return;
		}
		throw new ComparisonFailure(formatFailMessage(null, expected, actual),
				expected.toString(),
				actual.toString());
	}

	/** Test if the actual value is not equal to the expected value with
	 * a distance of epsilon.
	 *
	 * @param expected the expected value.
	 * @param actual the actual value.
	 */
	public void assertNotEpsilonEquals(double expected, double actual) {
		assertNotEpsilonEquals(null, expected, actual);
	}

	/** Test if the actual value is not equal to the expected value with
	 * a distance of epsilon.
	 *
	 * @param message the error message.
	 * @param expected the expected value.
	 * @param actual the actual value.
	 */
	public void assertNotEpsilonEquals(String message, double expected, double actual) {
		if (!isEpsilonEquals(expected, actual, false)) {
			return;
		}
		throw new ComparisonFailure(
				formatFailMessage(message, "same double value.", expected, actual), //$NON-NLS-1$
				Double.toString(expected),
				Double.toString(actual));
	}

	/** Test if the two collections do no contain the same elements without
	 * taking into account the order of the elements in the collections.
	 *
	 * @param <T> the type of the elements in the collections.
	 * @param expected the expected collection.
	 * @param actual the actual collection.
	 */
	public static <T> void assertNotEpsilonEquals(T[] expected, T[] actual) {
		assertNotEpsilonEquals(null, expected, actual);
	}

	/** Test if the two collections do no contain the same elements without
	 * taking into account the order of the elements in the collections.
	 *
	 * @param <T> the type of the elements in the collections.
	 * @param message the error message.
	 * @param expected the expected collection.
	 * @param actual the actual collection.
	 */
	public static <T> void assertNotEpsilonEquals(String message, T[] expected, T[] actual) {
		final List<T> l = new ArrayList<>(Arrays.asList(actual));
		for (final T e : expected) {
			if (!l.remove(e)) {
				return;
			}
		}
		if (l.isEmpty()) {
			throw new ComparisonFailure(
					formatFailMessage(message, "having similar collections is not expected", expected, actual), //$NON-NLS-1$
					Arrays.toString(expected), Arrays.toString(actual));
		}
	}

}
