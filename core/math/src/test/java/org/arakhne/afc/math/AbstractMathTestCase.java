/* 
 * $Id$
 * 
 * Copyright (c) 2006-10, Multiagent Team, Laboratoire Systemes et Transports, Universite de Technologie de Belfort-Montbeliard.
 * Copyright (C) 2012 Stephane GALLAND.
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
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
package org.arakhne.afc.math;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.Objects;
import java.util.Random;

import org.arakhne.afc.math.geometry.d2.Tuple2D;
import org.arakhne.afc.math.geometry.d2.continuous.Point2f;
import org.arakhne.afc.math.geometry.d2.continuous.Vector2f;
import org.arakhne.afc.math.geometry.d3.Tuple3D;
import org.arakhne.afc.math.geometry.d3.continuous.Point3f;
import org.arakhne.afc.math.geometry.d3.continuous.Quaternion;
import org.arakhne.afc.math.geometry.d3.continuous.Vector3f;
import org.arakhne.afc.math.matrix.Matrix2f;
import org.arakhne.afc.math.matrix.Matrix3f;
import org.arakhne.afc.math.matrix.Matrix4f;
import org.junit.ComparisonFailure;

/**
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@SuppressWarnings("static-method")
public abstract class AbstractMathTestCase {
	
	/** Precision of the floating point number epsilon-tests.
	 */
	protected final static int DEFAULT_DECIMAL_COUNT = 8;
	
	private int decimalPrecision = DEFAULT_DECIMAL_COUNT;
	
	/** Random number sequence.
	 */
	protected final Random random = new Random();
	
	/** Set the epsilon used ben testing floating-point values.
	 * 
	 * @param precision is the count of decimal digits to support
	 */
	protected void setDecimalPrecision(int precision) {
		this.decimalPrecision = Math.max(0, precision);
	}
	
	/** Set the epsilon used ben testing floating-point values to
	 * its default value.
	 */
	protected void setDefaultDecimalPrecision() {
		this.decimalPrecision = DEFAULT_DECIMAL_COUNT;
	}
	
	/** Test if the actual value is equal to the expected value with
	 * a distance of epsilon.
	 * 
	 * @param expected
	 * @param actual
	 */
	protected void assertEpsilonEquals(double expected, double actual) {
		assertEpsilonEquals(null, expected, actual);
	}
	
	/** Test if the actual value is not equal to the expected value with
	 * a distance of epsilon.
	 * 
	 * @param expected
	 * @param actual
	 */
	protected void assertNotEpsilonEquals(double expected, double actual) {
		assertNotEpsilonEquals(null, expected, actual);
	}

	/** Replies if two values are equals at espilon.
	 * 
	 * @param a
	 * @param b
	 * @param isNaNEqual indicates if the NaN value is equals to itself.
	 * @return <code>true</code> or <code>false</code>
	 */
	protected boolean isEpsilonEquals(double a, double b, boolean isNaNEqual) {
		if (a==b) return true;
		boolean nanA = Double.isNaN(a);
		boolean nanB = Double.isNaN(b);
		if (nanA || nanB) {
			if (isNaNEqual) return nanA==nanB;
			return false;
		}
		if (!Double.isInfinite(a) && !Double.isInfinite(a)
			&& !Double.isNaN(a) && !Double.isNaN(b)) {
			return isEpsilonEquals(new BigDecimal(a), new BigDecimal(b), this.decimalPrecision/2);
		}
		return false;
	}
	
	/** Replies if two values are equals at espilon.
	 * 
	 * @param a
	 * @param b
	 * @return <code>true</code> or <code>false</code>
	 */
	protected boolean isEpsilonEquals(double a, double b) {
		return isEpsilonEquals(a, b, true);
	}

	/** Replies if two values are equals at espilon.
	 * 
	 * @param a
	 * @param b
	 * @return <code>true</code> or <code>false</code>
	 */
	protected boolean isEpsilonEquals(BigDecimal a, BigDecimal b) {
		return isEpsilonEquals(a, b, this.decimalPrecision);
	}

	/** Replies if two values are equals at espilon.
	 * 
	 * @param a
	 * @param b
	 * @param precision is the number of decimal digits to test.
	 * @return <code>true</code> or <code>false</code>
	 */
	protected static boolean isEpsilonEquals(BigDecimal a, BigDecimal b, int precision) {
		BigDecimal ma = a.movePointRight(precision);
		BigDecimal mb = b.movePointRight(precision);
		BigDecimal aa = ma.setScale(0,BigDecimal.ROUND_HALF_UP);
		BigDecimal bb = mb.setScale(0,BigDecimal.ROUND_HALF_UP);
		if (aa.compareTo(bb)==0) return true;
		aa = ma.setScale(0,BigDecimal.ROUND_DOWN);
		bb = mb.setScale(0,BigDecimal.ROUND_DOWN);
		return (aa.compareTo(bb)==0);
	}
	
	/** Test if the actual value is equal to the expected value with
	 * a distance of epsilon.
	 * 
	 * @param message
	 * @param expected
	 * @param actual
	 */
	protected void assertEpsilonEquals(String message, double expected, double actual) {
		if (isEpsilonEquals(expected,actual)) return;
		fail((message==null ? "" : (message+": "))  //$NON-NLS-1$//$NON-NLS-2$
				+"not same double value, expected:"+ //$NON-NLS-1$
				expected
				+", actual:"+actual); //$NON-NLS-1$
	}

	/** Test if the actual value is not equal to the expected value with
	 * a distance of epsilon.
	 * 
	 * @param message
	 * @param expected
	 * @param actual
	 */
	protected void assertNotEpsilonEquals(String message, double expected, double actual) {
		if (!isEpsilonEquals(expected,actual, false)) return;
		fail((message==null ? "" : (message+": "))  //$NON-NLS-1$//$NON-NLS-2$
				+"same double value, unexpected:"+ //$NON-NLS-1$
				expected
				+", actual:"+actual); //$NON-NLS-1$
	}

	/** Test if the actual value is equal to the expected value with
	 * a distance of epsilon.
	 * 
	 * @param expected
	 * @param actual
	 */
	protected void assertEpsilonEquals(Tuple2D<?> expected, Tuple2D<?> actual) {
		assertEpsilonEquals(null, expected, actual);
	}

	/** Test if the actual value is not equal to the expected value with
	 * a distance of epsilon.
	 * 
	 * @param expected
	 * @param actual
	 */
	protected void assertNotEpsilonEquals(Tuple2D<?> expected, Tuple2D<?> actual) {
		assertNotEpsilonEquals(null, expected, actual);
	}

	/** Test if the actual value is equal to the expected value with
	 * a distance of epsilon.
	 * 
	 * @param message
	 * @param expected
	 * @param actual
	 */
	protected void assertEpsilonEquals(String message, Tuple2D<?> expected, Tuple2D<?> actual) {
		if (!isEpsilonEquals(expected.getX(), actual.getX())) {
			throw new ComparisonFailure(
					(message==null ? "" : (message+": "))  //$NON-NLS-1$//$NON-NLS-2$
					+"not same x value", //$NON-NLS-1$
					expected.toString(),
					actual.toString());
		}
		if (!isEpsilonEquals(expected.getY(), actual.getY())) {
			throw new ComparisonFailure(
					(message==null ? "" : (message+": "))  //$NON-NLS-1$//$NON-NLS-2$
					+"not same y value", //$NON-NLS-1$
					expected.toString(),
					actual.toString());
		}
	}

	/** Test if the actual value is not equal to the expected value with
	 * a distance of epsilon.
	 * 
	 * @param message
	 * @param expected
	 * @param actual
	 */
	protected void assertNotEpsilonEquals(String message, Tuple2D<?> expected, Tuple2D<?> actual) {
		if (isEpsilonEquals(expected.getX(), actual.getX(), false)) {
			throw new ComparisonFailure(
					(message==null ? "" : (message+": "))  //$NON-NLS-1$//$NON-NLS-2$
					+"not same x value", //$NON-NLS-1$
					expected.toString(),
					actual.toString());
		}
		if (isEpsilonEquals(expected.getY(), actual.getY(), false)) {
			throw new ComparisonFailure(
					(message==null ? "" : (message+": "))  //$NON-NLS-1$//$NON-NLS-2$
					+"not same y value", //$NON-NLS-1$
					expected.toString(),
					actual.toString());
		}
	}

	/** Test if the actual value is equal to the expected value with
	 * a distance of epsilon.
	 * 
	 * @param expected
	 * @param actual
	 */
	protected void assertEpsilonEquals(Tuple3D<?> expected, Tuple3D<?> actual) {
		assertEpsilonEquals(null, expected, actual);
	}

	/** Test if the actual value is not equal to the expected value with
	 * a distance of epsilon.
	 * 
	 * @param expected
	 * @param actual
	 */
	protected void assertNotEpsilonEquals(Tuple3D<?> expected, Tuple3D<?> actual) {
		assertNotEpsilonEquals(null, expected, actual);
	}

	/** Test if the actual value is equal to the expected value with
	 * a distance of epsilon.
	 * 
	 * @param message
	 * @param expected
	 * @param actual
	 */
	protected void assertEpsilonEquals(String message, Tuple3D<?> expected, Tuple3D<?> actual) {
		if (!isEpsilonEquals(expected.getX(), actual.getX())) {
			throw new ComparisonFailure(
					(message==null ? "" : (message+": "))  //$NON-NLS-1$//$NON-NLS-2$
					+"not same x value", //$NON-NLS-1$
					expected.toString(),
					actual.toString());
		}
		if (!isEpsilonEquals(expected.getY(), actual.getY())) {
			throw new ComparisonFailure(
					(message==null ? "" : (message+": "))  //$NON-NLS-1$//$NON-NLS-2$
					+"not same y value", //$NON-NLS-1$
					expected.toString(),
					actual.toString());
		}
		if (!isEpsilonEquals(expected.getZ(), actual.getZ())) {
			throw new ComparisonFailure(
					(message==null ? "" : (message+": "))  //$NON-NLS-1$//$NON-NLS-2$
					+"not same z value", //$NON-NLS-1$
					expected.toString(),
					actual.toString());
		}
	}

	/** Replies if the actual value is equal to the expected value with
	 * a distance of epsilon.
	 * 
	 * @param expected
	 * @param actual
	 * @return the test result.
	 */
	protected boolean isEpsilonEquals(Tuple3D<?> expected, Tuple3D<?> actual) {
		return isEpsilonEquals(expected.getX(), actual.getX())
				&& isEpsilonEquals(expected.getY(), actual.getY())
				&& isEpsilonEquals(expected.getZ(), actual.getZ());
	}

	/** Test if the actual value is not equal to the expected value with
	 * a distance of epsilon.
	 * 
	 * @param message
	 * @param expected
	 * @param actual
	 */
	protected void assertNotEpsilonEquals(String message, Tuple3D<?> expected, Tuple3D<?> actual) {
		if (isEpsilonEquals(expected.getX(), actual.getX(), false)) {
			throw new ComparisonFailure(
					(message==null ? "" : (message+": "))  //$NON-NLS-1$//$NON-NLS-2$
					+"not same x value", //$NON-NLS-1$
					expected.toString(),
					actual.toString());
		}
		if (isEpsilonEquals(expected.getY(), actual.getY(), false)) {
			throw new ComparisonFailure(
					(message==null ? "" : (message+": "))  //$NON-NLS-1$//$NON-NLS-2$
					+"not same y value", //$NON-NLS-1$
					expected.toString(),
					actual.toString());
		}
		if (isEpsilonEquals(expected.getZ(), actual.getZ(), false)) {
			throw new ComparisonFailure(
					(message==null ? "" : (message+": "))  //$NON-NLS-1$//$NON-NLS-2$
					+"not same z value", //$NON-NLS-1$
					expected.toString(),
					actual.toString());
		}
	}

	/** Test if the actual value is equal to the expected value with
	 * a distance of epsilon.
	 * 
	 * @param expected
	 * @param actual
	 */
	protected void assertEpsilonEquals(Quaternion expected, Quaternion actual) {
		assertEpsilonEquals(null, expected, actual);
	}

	/** Test if the actual value is not equal to the expected value with
	 * a distance of epsilon.
	 * 
	 * @param expected
	 * @param actual
	 */
	protected void assertNotEpsilonEquals(Quaternion expected, Quaternion actual) {
		assertNotEpsilonEquals(null, expected, actual);
	}

	/** Test if the actual value is equal to the expected value with
	 * a distance of epsilon.
	 * 
	 * @param message
	 * @param expected
	 * @param actual
	 */
	protected void assertEpsilonEquals(String message, Quaternion expected, Quaternion actual) {
		if (!isEpsilonEquals(expected.getX(), actual.getX())) {
			throw new ComparisonFailure(
					(message==null ? "" : (message+": "))  //$NON-NLS-1$//$NON-NLS-2$
					+"not same x value", //$NON-NLS-1$
					expected.toString(),
					actual.toString());
		}
		if (!isEpsilonEquals(expected.getY(), actual.getY())) {
			throw new ComparisonFailure(
					(message==null ? "" : (message+": "))  //$NON-NLS-1$//$NON-NLS-2$
					+"not same y value", //$NON-NLS-1$
					expected.toString(),
					actual.toString());
		}
		if (!isEpsilonEquals(expected.getZ(), actual.getZ())) {
			throw new ComparisonFailure(
					(message==null ? "" : (message+": "))  //$NON-NLS-1$//$NON-NLS-2$
					+"not same z value", //$NON-NLS-1$
					expected.toString(),
					actual.toString());
		}
		if (!isEpsilonEquals(expected.getW(), actual.getW())) {
			throw new ComparisonFailure(
					(message==null ? "" : (message+": "))  //$NON-NLS-1$//$NON-NLS-2$
					+"not same w value", //$NON-NLS-1$
					expected.toString(),
					actual.toString());
		}
	}

	/** Test if the actual value is not equal to the expected value with
	 * a distance of epsilon.
	 * 
	 * @param message
	 * @param expected
	 * @param actual
	 */
	protected void assertNotEpsilonEquals(String message, Quaternion expected, Quaternion actual) {
		if (isEpsilonEquals(expected.getX(), actual.getX(), false)) {
			throw new ComparisonFailure(
					(message==null ? "" : (message+": "))  //$NON-NLS-1$//$NON-NLS-2$
					+"not same x value", //$NON-NLS-1$
					expected.toString(),
					actual.toString());
		}
		if (isEpsilonEquals(expected.getY(), actual.getY(), false)) {
			throw new ComparisonFailure(
					(message==null ? "" : (message+": "))  //$NON-NLS-1$//$NON-NLS-2$
					+"not same y value", //$NON-NLS-1$
					expected.toString(),
					actual.toString());
		}
		if (isEpsilonEquals(expected.getZ(), actual.getZ(), false)) {
			throw new ComparisonFailure(
					(message==null ? "" : (message+": "))  //$NON-NLS-1$//$NON-NLS-2$
					+"not same z value", //$NON-NLS-1$
					expected.toString(),
					actual.toString());
		}
		if (isEpsilonEquals(expected.getW(), actual.getW(), false)) {
			throw new ComparisonFailure(
					(message==null ? "" : (message+": "))  //$NON-NLS-1$//$NON-NLS-2$
					+"not same w value", //$NON-NLS-1$
					expected.toString(),
					actual.toString());
		}
	}

	/** Test if the actual value is not equal to the expected value with
	 * a distance of epsilon.
	 * 
	 * @param expected
	 * @param actual
	 */
	protected void assertNotEquals(int expected, int actual) {
		assertNotEquals(null, expected, actual);
	}

	/** Test if the actual value is not equal to the expected value with
	 * a distance of epsilon.
	 * 
	 * @param message
	 * @param expected
	 * @param actual
	 */
	protected void assertNotEquals(String message, int expected, int actual) {
		if (expected==actual) {
			fail((message==null ? "" : (message+": "))  //$NON-NLS-1$//$NON-NLS-2$
					+"same value, expecting not equal to:"+ //$NON-NLS-1$
					expected);
		}
	}
	
	/** Create a random point.
	 *
	 * @return the random point.
	 */
	protected Point2f randomPoint2f() {
		return new Point2f(
				this.random.nextDouble() * 1000 - 500,
				this.random.nextDouble() * 1000 - 500);
	}
	
	/** Create a random vector.
	 *
	 * @return the random vector.
	 */
	protected Vector2f randomVector2f() {
		return new Vector2f(
				this.random.nextDouble() * 1000 - 500,
				this.random.nextDouble() * 1000 - 500);
	}

	/** Create a random point.
	 *
	 * @return the random point.
	 */
	protected Point3f randomPoint3f() {
		return new Point3f(
				this.random.nextDouble() * 1000 - 500,
				this.random.nextDouble() * 1000 - 500,
				this.random.nextDouble() * 1000 - 500);
	}
	
	/** Create a random vector.
	 *
	 * @return the random vector.
	 */
	protected Vector3f randomVector3f() {
		return new Vector3f(
				this.random.nextDouble() * 1000 - 500,
				this.random.nextDouble() * 1000 - 500,
				this.random.nextDouble() * 1000 - 500);
	}

	/** Create a random matrix.
	 *
	 * @return the random matrix.
	 */
	protected Matrix2f randomMatrix2f() {
		return new Matrix2f(
				this.random.nextDouble() * 1000 - 500,
				this.random.nextDouble() * 1000 - 500,
				this.random.nextDouble() * 1000 - 500,
				this.random.nextDouble() * 1000 - 500);
	}
	
	
	/** Create a random matrix.
	 *
	 * @return the random matrix.
	 */
	protected Matrix3f randomMatrix3f() {
		return new Matrix3f(
				this.random.nextDouble() * 1000 - 500,
				this.random.nextDouble() * 1000 - 500,
				this.random.nextDouble() * 1000 - 500,
				this.random.nextDouble() * 1000 - 500,
				this.random.nextDouble() * 1000 - 500,
				this.random.nextDouble() * 1000 - 500,
				this.random.nextDouble() * 1000 - 500,
				this.random.nextDouble() * 1000 - 500,
				this.random.nextDouble() * 1000 - 500);
	}
	
	
	/** Create a random matrix.
	 *
	 * @return the random matrix.
	 */
	protected Matrix4f randomMatrix4f() {
		return new Matrix4f(
				this.random.nextDouble() * 1000 - 500,
				this.random.nextDouble() * 1000 - 500,
				this.random.nextDouble() * 1000 - 500,
				this.random.nextDouble() * 1000 - 500,
				this.random.nextDouble() * 1000 - 500,
				this.random.nextDouble() * 1000 - 500,
				this.random.nextDouble() * 1000 - 500,
				this.random.nextDouble() * 1000 - 500,
				this.random.nextDouble() * 1000 - 500,
				this.random.nextDouble() * 1000 - 500,
				this.random.nextDouble() * 1000 - 500,
				this.random.nextDouble() * 1000 - 500,
				this.random.nextDouble() * 1000 - 500,
				this.random.nextDouble() * 1000 - 500,
				this.random.nextDouble() * 1000 - 500,
				this.random.nextDouble() * 1000 - 500);
	}

	
	
	/**
	 * Replies if two arrays have the same values at epsilon.
	 * 
	 * @param a
	 * @param b
	 * @return <code>true</code> if the two arrays are equal, otherwise
	 * <code>false</code>.
	 */
	protected boolean isEpsilonEquals(double[] a, double[] b) {
		if (a==b) return true;
		if (a==null && b!=null) return false;
		if (a!=null && b==null) return false;
		assert(a!=null && b!=null);
		if (a.length!=b.length) return false;
		for(int i=0; i<a.length; ++i) {
			if (!isEpsilonEquals(a[i], b[i])) return false;
		}
		return true;
	}

	/** Test if the given value is not a number.
	 *
	 * @param value
	 */
	protected void assertNaN(double value) {
		if (!Double.isNaN(value)) {
			fail("Expected NaN, but has: " + value); //$NON-NLS-1$
		}
	}

	/** Test if the given value is not a number.
	 *
	 * @param value
	 */
	protected void assertNaN(float value) {
		if (!Float.isNaN(value)) {
			fail("Expected NaN, but has: " + value); //$NON-NLS-1$
		}
	}
	
	/** Test if the given value is equal to zero.
	 *
	 * @param value
	 */
	protected void assertZero(byte value) {
		if (value != 0) {
			fail("Expected zero, but has: " + value); //$NON-NLS-1$
		}
	}

	/** Test if the given value is equal to zero.
	 *
	 * @param value
	 */
	protected void assertZero(short value) {
		if (value != 0) {
			fail("Expected zero, but has: " + value); //$NON-NLS-1$
		}
	}

	/** Test if the given value is equal to zero.
	 *
	 * @param value
	 */
	protected void assertZero(int value) {
		if (value != 0) {
			fail("Expected zero, but has: " + value); //$NON-NLS-1$
		}
	}

	/** Test if the given value is equal to zero.
	 *
	 * @param value
	 */
	protected void assertZero(long value) {
		if (value != 0) {
			fail("Expected zero, but has: " + value); //$NON-NLS-1$
		}
	}

	/** Test if the given value is equal to zero.
	 *
	 * @param value
	 */
	protected void assertZero(float value) {
		if (value != 0f) {
			fail("Expected zero, but has: " + value); //$NON-NLS-1$
		}
	}

	/** Test if the given value is equal to zero.
	 *
	 * @param value
	 */
	protected void assertZero(double value) {
		if (value != 0.) {
			fail("Expected zero, but has: " + value); //$NON-NLS-1$
		}
	}

	/** Assert two iterable objects have the same elements.
	 * 
	 * @param expected
	 * @param actual
	 */
	protected <T> void assertCollectionEquals(Iterable<? extends T> expected, Iterable<? extends T> actual) {
		Iterator<? extends T> it1 = expected.iterator();
		Iterator<? extends T> it2 = actual.iterator();
		while (it1.hasNext()) {
			if (!it2.hasNext()) {
				throw new ComparisonFailure("Element is missed", toString(expected), toString(actual)); //$NON-NLS-1$
			}
			T expect = it1.next();
			T act = it2.next();
			if (!Objects.equals(expect, act)) {
				throw new ComparisonFailure("Not same element", toString(expected), toString(actual)); //$NON-NLS-1$
			}
		}
		if (it2.hasNext()) {
			throw new ComparisonFailure("Too many elements", toString(expected), toString(actual)); //$NON-NLS-1$
		}
	}

	private String toString(Iterable<?> it) {
		StringBuilder b = new StringBuilder();
		for (Object o : it) {
			b.append(o);
			b.append(",\n"); //$NON-NLS-1$
		}
		return b.toString();
	}

}