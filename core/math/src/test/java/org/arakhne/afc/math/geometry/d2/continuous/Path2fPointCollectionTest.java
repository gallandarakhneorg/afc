/* 
 * $Id$
 * 
 * Copyright (C) 2013 Stephane GALLAND.
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

import org.arakhne.afc.math.AbstractMathTestCase;
import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.continuous.Path2f;
import org.arakhne.afc.math.geometry.d2.continuous.Point2f;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class Path2fPointCollectionTest extends AbstractMathTestCase {
	
	/** Is the rectangular shape to test.
	 */
	protected Path2f r;
	
	/** Is the collection to test.
	 */
	protected Collection<Point2D> c;

	/**
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
		this.r = new Path2f();
		this.r.moveTo(1f, 1f);
		this.r.lineTo(2f, 2f);
		this.r.quadTo(3f, 0f, 4f, 3f);
		this.r.curveTo(5f, -1f, 6f, 5f, 7f, -5f);
		this.r.closePath();
		this.c = this.r.toCollection();
	}
	
	/**
	 * @throws Exception
	 */
	@After
	public void tearDown() throws Exception {
		this.r = null;
		this.c = null;
	}
	
	private void assertCoords(float... coords) {
		assertEquals(coords.length/2, this.r.size());
		for(int i=0, j=0; i<this.r.size(); ++i) {
			Point2D p = this.r.getPointAt(i);
			assertEpsilonEquals(coords[j++], p.getX());
			assertEpsilonEquals(coords[j++], p.getY());
		}
	}
	
    /**
     */
	@Test
    public void size() {
    	assertEquals(7, this.c.size());
    	this.r.removeLast();
    	assertEquals(7, this.c.size());
    	this.r.removeLast();
    	assertEquals(4, this.c.size());
    	this.r.clear();
    	assertEquals(0, this.c.size());
    }

    /**
     */
	@Test
    public void isEmpty() {
    	assertFalse(this.c.isEmpty());
    	this.r.removeLast();
    	assertFalse(this.c.isEmpty());
    	this.r.removeLast();
    	assertFalse(this.c.isEmpty());
    	this.r.clear();
    	assertTrue(this.c.isEmpty());
    }

    /**
     */
	@Test
    public void containsObject() {
    	assertFalse(this.c.contains(new Object()));
    	assertTrue(this.c.contains(new Point2f(2f, 2f)));
    	assertTrue(this.c.contains(new Point2f(6f, 5f)));
    	assertFalse(this.c.contains(new Point2f(-1f, 6f)));
    }

    /**
     */
	@Test
    public void iterator() {
    	Point2D p;
    	Iterator<Point2D> iterator = this.c.iterator();
    	assertTrue(iterator.hasNext());
    	p = iterator.next();
    	assertEpsilonEquals(1f, p.getX());
    	assertEpsilonEquals(1f, p.getY());
    	assertTrue(iterator.hasNext());
    	p = iterator.next();
    	assertEpsilonEquals(2f, p.getX());
    	assertEpsilonEquals(2f, p.getY());
    	assertTrue(iterator.hasNext());
    	p = iterator.next();
    	assertEpsilonEquals(3f, p.getX());
    	assertEpsilonEquals(0f, p.getY());
    	assertTrue(iterator.hasNext());
    	p = iterator.next();
    	assertEpsilonEquals(4f, p.getX());
    	assertEpsilonEquals(3f, p.getY());
    	assertTrue(iterator.hasNext());
    	p = iterator.next();
    	assertEpsilonEquals(5f, p.getX());
    	assertEpsilonEquals(-1f, p.getY());
    	assertTrue(iterator.hasNext());
    	p = iterator.next();
    	assertEpsilonEquals(6f, p.getX());
    	assertEpsilonEquals(5f, p.getY());
    	assertTrue(iterator.hasNext());
    	p = iterator.next();
    	assertEpsilonEquals(7f, p.getX());
    	assertEpsilonEquals(-5f, p.getY());
    	assertFalse(iterator.hasNext());
    }

    /**
     */
	@Test
    public void toArray() {
    	Object[] tab = this.c.toArray();
    	assertEquals(7, tab.length);
    	assertTrue(tab[0] instanceof Point2D);
    	assertEpsilonEquals(1f, ((Point2D)tab[0]).getX());
    	assertEpsilonEquals(1f, ((Point2D)tab[0]).getY());
    	assertTrue(tab[1] instanceof Point2D);
    	assertEpsilonEquals(2f, ((Point2D)tab[1]).getX());
    	assertEpsilonEquals(2f, ((Point2D)tab[1]).getY());
    	assertTrue(tab[2] instanceof Point2D);
    	assertEpsilonEquals(3f, ((Point2D)tab[2]).getX());
    	assertEpsilonEquals(0f, ((Point2D)tab[2]).getY());
    	assertTrue(tab[3] instanceof Point2D);
    	assertEpsilonEquals(4f, ((Point2D)tab[3]).getX());
    	assertEpsilonEquals(3f, ((Point2D)tab[3]).getY());
    	assertTrue(tab[4] instanceof Point2D);
    	assertEpsilonEquals(5f, ((Point2D)tab[4]).getX());
    	assertEpsilonEquals(-1f, ((Point2D)tab[4]).getY());
    	assertTrue(tab[5] instanceof Point2D);
    	assertEpsilonEquals(6f, ((Point2D)tab[5]).getX());
    	assertEpsilonEquals(5f, ((Point2D)tab[5]).getY());
    	assertTrue(tab[6] instanceof Point2D);
    	assertEpsilonEquals(7f, ((Point2D)tab[6]).getX());
    	assertEpsilonEquals(-5f, ((Point2D)tab[6]).getY());
    }

    /**
     */
	@Test
    public void toArrayArray() {
    	Point2D[] tab = new Point2D[5];
    	Point2D[] tab2 = this.c.toArray(tab);
    	assertSame(tab, tab2);
    	assertEquals(5, tab.length);
    	assertEpsilonEquals(1f, tab[0].getX());
    	assertEpsilonEquals(1f, tab[0].getY());
    	assertEpsilonEquals(2f, tab[1].getX());
    	assertEpsilonEquals(2f, tab[1].getY());
    	assertEpsilonEquals(3f, tab[2].getX());
    	assertEpsilonEquals(0f, tab[2].getY());
    	assertEpsilonEquals(4f, tab[3].getX());
    	assertEpsilonEquals(3f, tab[3].getY());
    	assertEpsilonEquals(5f, tab[4].getX());
    	assertEpsilonEquals(-1f, tab[4].getY());
    }

    /**
     */
	@Test
    public void add() {
    	assertTrue(this.c.add(new Point2f(123f, 456f)));
    	assertCoords(1f, 1f, 2f, 2f, 3f, 0f, 4f, 3f, 5f, -1f, 6f, 5f, 7f, -5f, 123f, 456f);
    	this.r.clear();
    	assertCoords();
    	assertTrue(this.c.add(new Point2f(123f, 456f)));
    	assertCoords(123f, 456f);
    	assertTrue(this.c.add(new Point2f(789f, 1011f)));
    	assertCoords(123f, 456f, 789f, 1011f);
    }

    /**
     */
	@Test
    public void remove() {
    	assertFalse(this.c.remove(new Object()));
    	assertTrue(this.c.remove(new Point2f(2f, 2f)));
    	assertCoords(1f, 1f, 3f, 0f, 4f, 3f, 5f, -1f, 6f, 5f, 7f, -5f);
    	assertTrue(this.c.remove(new Point2f(6f, 5f)));
    	assertCoords(1f, 1f, 3f, 0f, 4f, 3f);
    }


    /**
     */
	@Test
    public void containsAll() {
    	assertTrue(this.c.containsAll(
    			Arrays.asList(new Point2f(1f, 1f), new Point2f(6f, 5f))));
    	assertFalse(this.c.containsAll(
    			Arrays.asList(new Point2f(1f, 1f), new Point2f(6f, 6f))));
    }

    /**
     */
	@Test
    public void addAll() {
    	this.c.addAll(
    			Arrays.asList(new Point2f(123f, 456f), new Point2f(789f, 1011f)));
    	assertCoords(1f, 1f, 2f, 2f, 3f, 0f, 4f, 3f, 5f, -1f, 6f, 5f, 7f, -5f, 123f, 456f, 789f, 1011f);
    }

    /**
     */
	@Test
    public void removeAll() {
    	this.c.removeAll(
    			Arrays.asList(new Point2f(123f, 456f), new Point2f(2f, 2f)));
    	assertCoords(1f, 1f, 3f, 0f, 4f, 3f, 5f, -1f, 6f, 5f, 7f, -5f);
    }

    /**
     */
	@Test
    public void retainAll() {
    	try {
    		this.c.retainAll(Collections.emptyList());
    		fail("Expecting an exception"); //$NON-NLS-1$
    	}
    	catch(Throwable _) {
    		// Expecting an exception
    	}
    }

    /**
     */
	@Test
    public void clear() {
    	this.c.clear();
    	assertCoords();
    }
    
}