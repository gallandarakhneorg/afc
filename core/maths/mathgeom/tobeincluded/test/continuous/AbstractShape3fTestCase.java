/* 
 * $Id$
 * 
 * Copyright (C) 2010-2013 Stephane GALLAND.
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
package org.arakhne.afc.math.geometry.d3.continuous;

import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.util.Random;

import org.arakhne.afc.math.geometry.d2.Tuple2D;
import org.arakhne.afc.math.geometry.d3.Tuple3D;
import org.junit.After;
import org.junit.Before;
import org.junit.ComparisonFailure;
import org.junit.Test;

/**
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@SuppressWarnings("all")
public abstract class AbstractShape3fTestCase<T extends Shape3F> {

	/** Is the rectangular shape to test.
	 */
	protected T r;
	
	/** Precision of the floating point number epsilon-tests.
	 */
	protected final static int DEFAULT_DECIMAL_COUNT = 8;
	
	private int decimalPrecision = DEFAULT_DECIMAL_COUNT;
	
	/** Random number sequence.
	 */
	protected final Random random = new Random();
	
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
			fail((message==null ? "" : (message+": "))  
					+"same value, expecting not equal to:"+ 
					expected);
		}
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
					(message==null ? "" : (message+": "))  
					+"not same x value", 
					expected.toString(),
					actual.toString());
		}
		if (isEpsilonEquals(expected.getY(), actual.getY(), false)) {
			throw new ComparisonFailure(
					(message==null ? "" : (message+": "))  
					+"not same y value", 
					expected.toString(),
					actual.toString());
		}
		if (isEpsilonEquals(expected.getZ(), actual.getZ(), false)) {
			throw new ComparisonFailure(
					(message==null ? "" : (message+": "))  
					+"not same z value", 
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
					(message==null ? "" : (message+": "))  
					+"not same x value", 
					expected.toString(),
					actual.toString());
		}
		if (!isEpsilonEquals(expected.getY(), actual.getY())) {
			throw new ComparisonFailure(
					(message==null ? "" : (message+": "))  
					+"not same y value", 
					expected.toString(),
					actual.toString());
		}
		if (!isEpsilonEquals(expected.getZ(), actual.getZ())) {
			throw new ComparisonFailure(
					(message==null ? "" : (message+": "))  
					+"not same z value", 
					expected.toString(),
					actual.toString());
		}
		if (!isEpsilonEquals(expected.getW(), actual.getW())) {
			throw new ComparisonFailure(
					(message==null ? "" : (message+": "))  
					+"not same w value", 
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
					(message==null ? "" : (message+": "))  
					+"not same x value", 
					expected.toString(),
					actual.toString());
		}
		if (isEpsilonEquals(expected.getY(), actual.getY(), false)) {
			throw new ComparisonFailure(
					(message==null ? "" : (message+": "))  
					+"not same y value", 
					expected.toString(),
					actual.toString());
		}
		if (isEpsilonEquals(expected.getZ(), actual.getZ(), false)) {
			throw new ComparisonFailure(
					(message==null ? "" : (message+": "))  
					+"not same z value", 
					expected.toString(),
					actual.toString());
		}
		if (isEpsilonEquals(expected.getW(), actual.getW(), false)) {
			throw new ComparisonFailure(
					(message==null ? "" : (message+": "))  
					+"not same w value", 
					expected.toString(),
					actual.toString());
		}
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
					(message==null ? "" : (message+": "))  
					+"not same x value", 
					expected.toString(),
					actual.toString());
		}
		if (!isEpsilonEquals(expected.getY(), actual.getY())) {
			throw new ComparisonFailure(
					(message==null ? "" : (message+": "))  
					+"not same y value", 
					expected.toString(),
					actual.toString());
		}
		if (!isEpsilonEquals(expected.getZ(), actual.getZ())) {
			throw new ComparisonFailure(
					(message==null ? "" : (message+": "))  
					+"not same z value", 
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
	
	/** Test if the actual value is equal to the expected value with
	 * a distance of epsilon.
	 * 
	 * @param message
	 * @param expected
	 * @param actual
	 */
	protected void assertEpsilonEquals(String message, double expected, double actual) {
		if (isEpsilonEquals(expected,actual)) return;
		fail((message==null ? "" : (message+": "))  
				+"not same double value, expected:"+ 
				expected
				+", actual:"+actual); 
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
		fail((message==null ? "" : (message+": "))  
				+"same double value, unexpected:"+ 
				expected
				+", actual:"+actual); 
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
					(message==null ? "" : (message+": "))  
					+"not same x value", 
					expected.toString(),
					actual.toString());
		}
		if (!isEpsilonEquals(expected.getY(), actual.getY())) {
			throw new ComparisonFailure(
					(message==null ? "" : (message+": "))  
					+"not same y value", 
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
					(message==null ? "" : (message+": "))  
					+"not same x value", 
					expected.toString(),
					actual.toString());
		}
		if (isEpsilonEquals(expected.getY(), actual.getY(), false)) {
			throw new ComparisonFailure(
					(message==null ? "" : (message+": "))  
					+"not same y value", 
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
		
	
	
	/**
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
		this.r = createShape();
	}
	
	/** Create the shape to test.
	 * 
	 * @return the shape to test.
	 */
	protected abstract T createShape();
	
	/**
	 * @throws Exception
	 */
	@After
	public void tearDown() throws Exception {
		this.r = null;
	}
	
	@Test
	public abstract void testClone();
	
	@Test
	public abstract void distancePoint3D();

	@Test
	public abstract void containsPoint3D();

    @Test
    public abstract void testEquals();
    
    @Test
    public abstract void testHashCode();
   
    @Test
    public abstract void toBoundingBox();
	
    @Test
    public abstract void toBoundingBoxAlignedBox3x();

    @Test
    public abstract void distanceSquaredPoint3D();

    @Test
    public abstract void distanceL1Point3D();

    @Test
    public abstract void distanceLinfPoint3D();

    @Test
    public abstract void translateVector3D(); 

    @Test
    public abstract void translateDoubleDoubleDouble(); 
	
    @Test
    public abstract void containsDoubleDoubleDouble();
	
    @Test
    public abstract void intersectsAlignedBox3x();

    @Test
    public abstract void intersectsSphere3x();

    @Test
    public abstract void intersectsSegment3x();

    @Test
    public abstract void intersectsTriangle3x();

    @Test
    public abstract void intersectsCapsule3x();

    @Test
    public abstract void intersectsOrientedBox3x();

    @Test
	public abstract void intersectsAbstractPlane3D();

}