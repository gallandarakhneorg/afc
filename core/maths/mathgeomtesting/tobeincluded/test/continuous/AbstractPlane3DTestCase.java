/* 
 * $Id$
 * 
 * Copyright (C) 2013 Christophe BOHRHAUER.
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
package org.arakhne.afc.math.geometry.d3.continuous;

import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.util.Random;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @param <T> is the type of the plane.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@SuppressWarnings("all")
public abstract class AbstractPlane3DTestCase<T extends Plane3D<? super T>> {

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
	
	
	/**
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
		this.r = createPlane();
	}
	
	/** Create the shape to test.
	 * 
	 * @return the shape to test.
	 */
	protected abstract T createPlane();
	
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
	public abstract void distanceToPoint3D();

	@Test
	public abstract void distanceToPlane3D();

	@Test
	public abstract void getIntersectionPlane3D();

	@Test
	public abstract void getIntersectionSegment3D();

	@Test
	public abstract void getProjectionPoint3D(); 
	
	@Test
	public abstract void isValid();

	@Test
    public abstract void classifiesPlane3D();

	@Test
    public abstract void classifiesPoint3D();

	@Test
    public abstract void classifiesSphere3f();

	@Test
    public abstract void classifiesAlignedBox3f();

	@Test
    public abstract void intersectsPlane3D();

	@Test
    public abstract void intersectsPoint3D();
    
	@Test
    public abstract void intersectsSphere3f();

	@Test
    public abstract void intersectsAlignedBox3f();

	@Test
	public abstract void getProjectionDoubleDoubleDouble();

	@Test
	public abstract void distanceToDoubleDoubleDouble();

	@Test
    public abstract void classifiesDoubleDoubleDouble();

	@Test
    public abstract void intersectsDoubleDoubleDouble();

	@Test
    public abstract void setPivotPoint3D();

	@Test
    public abstract void setPivotDoubleDoubleDouble();

	@Test
    public abstract void getPivot();
	
	/** Test if the actual value is equal to the expected value with
	 * a distance of epsilon.
	 * 
	 * @param expected
	 * @param actual
	 */
	protected void assertEpsilonEquals(double expected, double actual) {
		assertEpsilonEquals(null, expected, actual);
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
	
	/** Replies if two values are equals at espilon.
	 * 
	 * @param a
	 * @param b
	 * @return {@code true} or {@code false}
	 */
	protected boolean isEpsilonEquals(double a, double b) {
		return isEpsilonEquals(a, b, true);
	}

	/** Replies if two values are equals at espilon.
	 * 
	 * @param a
	 * @param b
	 * @param isNaNEqual indicates if the NaN value is equals to itself.
	 * @return {@code true} or {@code false}
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
	 * @return {@code true} or {@code false}
	 */
	protected boolean isEpsilonEquals(BigDecimal a, BigDecimal b) {
		return isEpsilonEquals(a, b, this.decimalPrecision);
	}

	/** Replies if two values are equals at espilon.
	 * 
	 * @param a
	 * @param b
	 * @param precision is the number of decimal digits to test.
	 * @return {@code true} or {@code false}
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
	
}