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
package org.arakhne.afc.math.object;

import java.util.Arrays;
import java.util.Iterator;

import org.arakhne.afc.math.continous.object2d.Point2f;
import org.arakhne.afc.math.continous.object3d.Point3f;
import org.arakhne.util.ref.AbstractTestCase;


/**
 * @author $Author: cbohrhauer$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class PointFilterXYIterableTest extends AbstractTestCase {

	private Point3f[] reference;
	private PointFilterXYIterable iterable;
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setUp() throws Exception {
		super.setUp();
		this.reference = new Point3f[] {
			new Point3f(1., 2., 3.),
			new Point3f(4., 5., 6.),
			new Point3f(7., 8., 9.),
			new Point3f(10., 11., 12.)
		};
		this.iterable = new PointFilterXYIterable(Arrays.asList(this.reference));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void tearDown() throws Exception {
		this.iterable = null;
		this.reference = null;
		super.tearDown();
	}
	
	/**
	 */
	public void testIterator() {
		Iterator<Point2f> iterator;
		
		iterator = this.iterable.iterator();
		
		assertTrue(iterator.hasNext());
		assertEpsilonEquals(new Point2f(1., 2.), iterator.next());
		assertTrue(iterator.hasNext());
		assertEpsilonEquals(new Point2f(4., 5.), iterator.next());
		assertTrue(iterator.hasNext());
		assertEpsilonEquals(new Point2f(7., 8.), iterator.next());
		assertTrue(iterator.hasNext());
		assertEpsilonEquals(new Point2f(10., 11.), iterator.next());
		assertFalse(iterator.hasNext());

		iterator = this.iterable.iterator();
		
		assertTrue(iterator.hasNext());
		assertEpsilonEquals(new Point2f(1., 2.), iterator.next());
		assertTrue(iterator.hasNext());
		assertEpsilonEquals(new Point2f(4., 5.), iterator.next());
		assertTrue(iterator.hasNext());
		assertEpsilonEquals(new Point2f(7., 8.), iterator.next());
		assertTrue(iterator.hasNext());
		assertEpsilonEquals(new Point2f(10., 11.), iterator.next());
		assertFalse(iterator.hasNext());
	}

}
