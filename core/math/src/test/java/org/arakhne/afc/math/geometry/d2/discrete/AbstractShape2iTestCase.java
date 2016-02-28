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
package org.arakhne.afc.math.geometry.d2.discrete;

import static org.junit.Assert.fail;

import java.util.Arrays;

import org.arakhne.afc.math.AbstractMathTestCase;
import org.arakhne.afc.math.geometry.PathElementType;
import org.arakhne.afc.math.geometry.d2.discrete.PathElement2i;
import org.arakhne.afc.math.geometry.d2.discrete.PathIterator2i;
import org.arakhne.afc.math.geometry.d2.discrete.Shape2i;
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
@SuppressWarnings("static-method")
public abstract class AbstractShape2iTestCase<T extends Shape2i> extends AbstractMathTestCase {
	
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
	protected void assertElement(PathIterator2i pi, PathElementType type, int... coords) {
		if (!pi.hasNext()) {
			fail("expected path element but the iterator is empty"); //$NON-NLS-1$
		}
		PathElement2i pe = pi.next();
		if (!type.equals(pe.type)) {
			fail("expected: "+type+"; actual: "+pe.type);  //$NON-NLS-1$//$NON-NLS-2$
		}
		int[] c = pe.toArray();
		if (!isEquals(c, coords)) {
			fail("expected: "+Arrays.toString(coords)+"; actual: "+Arrays.toString(c));  //$NON-NLS-1$//$NON-NLS-2$
		}
	}
	
	/**
	 * Replies if two arrays have the same values at epsilon.
	 * 
	 * @param a
	 * @param b
	 * @return <code>true</code> if the two arrays are equal, otherwise
	 * <code>false</code>.
	 */
	@SuppressWarnings("null")
	public boolean isEpsilonEquals(float[] a, float[] b) {
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
	 * Replies if two arrays have the same values at epsilon.
	 * 
	 * @param a
	 * @param b
	 * @return <code>true</code> if the two arrays are equal, otherwise
	 * <code>false</code>.
	 */
	@SuppressWarnings("null")
	protected boolean isEquals(int[] a, int[] b) {
		if (a==b) return true;
		if (a==null && b!=null) return false;
		if (a!=null && b==null) return false;
		assert(a!=null && b!=null);
		if (a.length!=b.length) return false;
		for(int i=0; i<a.length; ++i) {
			if (a[i]!=b[i]) return false;
		}
		return true;
	}

	/** Assert is the given path iterator has no element.
	 * 
	 * @param pi
	 */
	protected void assertNoElement(PathIterator2i pi) {
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
	public abstract void translateIntInt();
	
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

}