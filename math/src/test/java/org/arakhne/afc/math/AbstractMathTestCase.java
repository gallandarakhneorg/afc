/* 
 * $Id$
 * 
 * Copyright (c) 2006-10, Multiagent Team, Laboratoire Systemes et Transports, Universite de Technologie de Belfort-Montbeliard.
 * Copyright (C) 2012 Stephane GALLAND.
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
package org.arakhne.afc.math;

import java.math.BigDecimal;

import junit.framework.TestCase;

import org.arakhne.afc.math.continous.object2d.Tuple2f;

/**
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public abstract class AbstractMathTestCase extends TestCase {
	
	/** Precision of the floating point number epsilon-tests.
	 */
	protected final static int DEFAULT_DECIMAL_COUNT = 8;
	
	private int decimalPrecision = DEFAULT_DECIMAL_COUNT;
	
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
	protected void assertEpsilonEquals(float expected, float actual) {
		assertEpsilonEquals(null, expected, actual);
	}
	
	/** Test if the actual value is not equal to the expected value with
	 * a distance of epsilon.
	 * 
	 * @param expected
	 * @param actual
	 */
	protected void assertNotEpsilonEquals(float expected, float actual) {
		assertNotEpsilonEquals(null, expected, actual);
	}

	/** Replies if two values are equals at espilon.
	 * 
	 * @param a
	 * @param b
	 * @param isNaNEqual indicates if the NaN value is equals to itself.
	 * @return <code>true</code> or <code>false</code>
	 */
	private boolean isEpsilonEquals(float a, float b, boolean isNaNEqual) {
		if (a==b) return true;
		boolean nanA = Float.isNaN(a);
		boolean nanB = Float.isNaN(b);
		if (nanA || nanB) {
			if (isNaNEqual) return nanA==nanB;
			return false;
		}
		if (!Float.isInfinite(a) && !Float.isInfinite(a)
			&& !Float.isNaN(a) && !Float.isNaN(b)) {
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
	protected boolean isEpsilonEquals(float a, float b) {
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
	protected void assertEpsilonEquals(String message, float expected, float actual) {
		if (isEpsilonEquals(expected,actual)) return;
		fail((message==null ? "" : (message+": "))  //$NON-NLS-1$//$NON-NLS-2$
				+"not same float value, expected:"+ //$NON-NLS-1$
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
	protected void assertNotEpsilonEquals(String message, float expected, float actual) {
		if (!isEpsilonEquals(expected,actual, false)) return;
		fail((message==null ? "" : (message+": "))  //$NON-NLS-1$//$NON-NLS-2$
				+"same float value, unexpected:"+ //$NON-NLS-1$
				expected
				+", actual:"+actual); //$NON-NLS-1$
	}

	/** Test if the actual value is equal to the expected value with
	 * a distance of epsilon.
	 * 
	 * @param expected
	 * @param actual
	 */
	protected void assertEpsilonEquals(Tuple2f<?> expected, Tuple2f<?> actual) {
		assertEpsilonEquals(null, expected, actual);
	}

	/** Test if the actual value is not equal to the expected value with
	 * a distance of epsilon.
	 * 
	 * @param expected
	 * @param actual
	 */
	protected void assertNotEpsilonEquals(Tuple2f<?> expected, Tuple2f<?> actual) {
		assertNotEpsilonEquals(null, expected, actual);
	}

	/** Test if the actual value is equal to the expected value with
	 * a distance of epsilon.
	 * 
	 * @param message
	 * @param expected
	 * @param actual
	 */
	protected void assertEpsilonEquals(String message, Tuple2f<?> expected, Tuple2f<?> actual) {
		if (!isEpsilonEquals(expected.getX(), actual.getX())) {
			fail((message==null ? "" : (message+": "))  //$NON-NLS-1$//$NON-NLS-2$
					+"not same x value, expected:"+ //$NON-NLS-1$
					expected.getX()
					+", actual:"+actual.getX()); //$NON-NLS-1$
			return;
		}
		if (!isEpsilonEquals(expected.getY(), actual.getY())) {
			fail((message==null ? "" : (message+": "))  //$NON-NLS-1$//$NON-NLS-2$
					+"not same y value, expected:"+ //$NON-NLS-1$
					expected.getY()
					+", actual:"+actual.getY()); //$NON-NLS-1$
			return;
		}
	}

	/** Test if the actual value is not equal to the expected value with
	 * a distance of epsilon.
	 * 
	 * @param message
	 * @param expected
	 * @param actual
	 */
	protected void assertNotEpsilonEquals(String message, Tuple2f<?> expected, Tuple2f<?> actual) {
		if (isEpsilonEquals(expected.getX(), actual.getX(), false)) {
			fail((message==null ? "" : (message+": "))  //$NON-NLS-1$//$NON-NLS-2$
					+"same x value, unexpected:"+ //$NON-NLS-1$
					expected.getX()
					+", actual:"+actual.getX()); //$NON-NLS-1$
			return;
		}
		if (isEpsilonEquals(expected.getY(), actual.getY(), false)) {
			fail((message==null ? "" : (message+": "))  //$NON-NLS-1$//$NON-NLS-2$
					+"not same y value, expected:"+ //$NON-NLS-1$
					expected.getY()
					+", actual:"+actual.getY()); //$NON-NLS-1$
			return;
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
	@SuppressWarnings("static-method")
	protected void assertNotEquals(String message, int expected, int actual) {
		if (expected==actual) {
			fail((message==null ? "" : (message+": "))  //$NON-NLS-1$//$NON-NLS-2$
					+"same value, expecting not equal to:"+ //$NON-NLS-1$
					expected);
		}
	}

}