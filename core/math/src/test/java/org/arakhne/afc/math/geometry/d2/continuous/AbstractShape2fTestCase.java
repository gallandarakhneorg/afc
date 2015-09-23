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
package org.arakhne.afc.math.geometry.d2.continuous;

import static org.junit.Assert.fail;

import java.util.Arrays;

import junit.framework.AssertionFailedError;

import org.arakhne.afc.math.AbstractMathTestCase;
import org.arakhne.afc.math.geometry.PathElementType;
import org.arakhne.afc.math.geometry.d2.continuous.AbstractPathElement2F;
import org.arakhne.afc.math.geometry.d2.continuous.PathIterator2f;
import org.arakhne.afc.math.geometry.d2.continuous.Shape2F;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @param <T> is the type of the shape to test
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public abstract class AbstractShape2fTestCase<T extends Shape2F> extends AbstractMathTestCase {
	
	/** Is the rectangular shape to test.
	 */
	protected T r;
	
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
	
	/** Assert is the given path iterator has a first element with the
	 * given information.
	 * 
	 * @param pi
	 * @param type
	 * @param coords
	 */
	protected void assertElement(PathIterator2f pi, PathElementType type, double... coords) {
		if (!pi.hasNext()) {
			fail("expected path element but the iterator is empty"); //$NON-NLS-1$
		}
		AbstractPathElement2F pe = pi.next();
		if (!type.equals(pe.type)) {
			fail("expected: "+type+"; actual: "+pe.type);  //$NON-NLS-1$//$NON-NLS-2$
		}
		double[] c = pe.toArray();
		if (!isEpsilonEquals(c, coords)) {
			throw new AssertionFailedError("expected: "+Arrays.toString(coords)+"; actual: "+Arrays.toString(c));  //$NON-NLS-1$//$NON-NLS-2$
		}
	}
	
	/** Assert is the given path iterator has no element.
	 * 
	 * @param pi
	 */
	@SuppressWarnings("static-method")
	protected void assertNoElement(PathIterator2f pi) {
		if (pi.hasNext()) {
			fail("expected no path element but the iterator is not empty"); //$NON-NLS-1$
		}
	}
	
	/**
	 */
	@Test
	public abstract void distancePoint2D();

	/**
	 */
	@Test
	public abstract void distanceSquaredPoint2D();

	/**
	 */
	@Test
	public abstract void distanceL1Point2D();

	/**
	 */
	@Test
	public abstract void distanceLinfPoint2D();
	
	/**
	 */
	@Test
	public abstract void translateFloatFloat();
	
	/**
	 */
	@Test
	public abstract void toBoundingBox();

	/**
	 */
	@Test
	public abstract void isEmpty();

	/**
	 */
	@Test
	public abstract void testClone();

	/**
	 */
	@Test
	public abstract void clear();

	/**
	 */
	@Test
	public abstract void intersectsPath2f();

	/**
	 */
	@Test
	public abstract void intersectsPathIterator2f();
	
}