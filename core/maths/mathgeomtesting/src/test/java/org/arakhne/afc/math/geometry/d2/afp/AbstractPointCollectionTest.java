/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2018 The original authors, and other authors.
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

package org.arakhne.afc.math.geometry.d2.afp;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import org.arakhne.afc.math.AbstractMathTestCase;
import org.arakhne.afc.math.geometry.PathWindingRule;
import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.Vector2D;

@SuppressWarnings("all")
public abstract class AbstractPointCollectionTest<P extends Point2D<? super P, ? super V>,
		V extends Vector2D<? super V, ? super P>,
		B extends Rectangle2afp<?, ?, ?, P, V, B>> extends AbstractMathTestCase {
	
	/** Is the shape to test.
	 */
	protected Path2afp<?, ?, ?, P, V, B> shape;
	
	/** Is the collection to test.
	 */
	protected Collection<P> collection;
	
	/** Shape factory.
	 */
	protected TestShapeFactory<P, V, B> factory;

	protected abstract TestShapeFactory<P, V, B> createFactory();

	@Before
	public void setUp() throws Exception {
		this.factory = createFactory();
		this.shape = this.factory.createPath(PathWindingRule.EVEN_ODD);
		this.shape.moveTo(1, 1);
		this.shape.lineTo(2, 2);
		this.shape.quadTo(3, 0, 4, 3);
		this.shape.curveTo(5, -1, 6, 5, 7, -5);
		this.shape.closePath();
		this.collection = this.shape.toCollection();
	}
	
	/**
	 * @throws Exception
	 */
	@After
	public void tearDown() throws Exception {
		this.shape = null;
		this.collection = null;
		this.factory = null;
	}
	
	private void assertCoords(double... coords) {
		assertEquals(coords.length/2, this.shape.size());
		for(int i=0, j=0; i<this.shape.size(); ++i) {
			Point2D p = this.shape.getPointAt(i);
			assertEpsilonEquals(coords[j++], p.getX());
			assertEpsilonEquals(coords[j++], p.getY());
		}
	}
	
    /**
     */
	@Test
    public void size() {
    	assertEquals(7, this.collection.size());
    	this.shape.removeLast();
    	assertEquals(7, this.collection.size());
    	this.shape.removeLast();
    	assertEquals(4, this.collection.size());
    	this.shape.clear();
    	assertEquals(0, this.collection.size());
    }

    /**
     */
	@Test
    public void isEmpty() {
    	assertFalse(this.collection.isEmpty());
    	this.shape.removeLast();
    	assertFalse(this.collection.isEmpty());
    	this.shape.removeLast();
    	assertFalse(this.collection.isEmpty());
    	this.shape.clear();
    	assertTrue(this.collection.isEmpty());
    }

    /**
     */
	@Test
    public void containsObject() {
    	assertFalse(this.collection.contains(new Object()));
    	assertTrue(this.collection.contains(this.factory.createPoint(2, 2)));
    	assertTrue(this.collection.contains(this.factory.createPoint(6, 5)));
    	assertFalse(this.collection.contains(this.factory.createPoint(-1, 6)));
    }

    /**
     */
	@Test
    public void iterator() {
    	Point2D p;
    	Iterator<P> iterator = this.collection.iterator();
    	assertTrue(iterator.hasNext());
    	p = iterator.next();
    	assertEpsilonEquals(1, p.getX());
    	assertEpsilonEquals(1, p.getY());
    	assertTrue(iterator.hasNext());
    	p = iterator.next();
    	assertEpsilonEquals(2, p.getX());
    	assertEpsilonEquals(2, p.getY());
    	assertTrue(iterator.hasNext());
    	p = iterator.next();
    	assertEpsilonEquals(3, p.getX());
    	assertEpsilonEquals(0, p.getY());
    	assertTrue(iterator.hasNext());
    	p = iterator.next();
    	assertEpsilonEquals(4, p.getX());
    	assertEpsilonEquals(3, p.getY());
    	assertTrue(iterator.hasNext());
    	p = iterator.next();
    	assertEpsilonEquals(5, p.getX());
    	assertEpsilonEquals(-1, p.getY());
    	assertTrue(iterator.hasNext());
    	p = iterator.next();
    	assertEpsilonEquals(6, p.getX());
    	assertEpsilonEquals(5, p.getY());
    	assertTrue(iterator.hasNext());
    	p = iterator.next();
    	assertEpsilonEquals(7, p.getX());
    	assertEpsilonEquals(-5, p.getY());
    	assertFalse(iterator.hasNext());
    }

    /**
     */
	@Test
    public void toArray() {
    	Object[] tab = this.collection.toArray();
    	assertEquals(7, tab.length);
    	assertTrue(tab[0] instanceof Point2D);
    	assertEpsilonEquals(1, ((Point2D) tab[0]).getX());
    	assertEpsilonEquals(1, ((Point2D) tab[0]).getY());
    	assertTrue(tab[1] instanceof Point2D);
    	assertEpsilonEquals(2, ((Point2D) tab[1]).getX());
    	assertEpsilonEquals(2, ((Point2D) tab[1]).getY());
    	assertTrue(tab[2] instanceof Point2D);
    	assertEpsilonEquals(3, ((Point2D) tab[2]).getX());
    	assertEpsilonEquals(0, ((Point2D) tab[2]).getY());
    	assertTrue(tab[3] instanceof Point2D);
    	assertEpsilonEquals(4, ((Point2D) tab[3]).getX());
    	assertEpsilonEquals(3, ((Point2D) tab[3]).getY());
    	assertTrue(tab[4] instanceof Point2D);
    	assertEpsilonEquals(5, ((Point2D) tab[4]).getX());
    	assertEpsilonEquals(-1, ((Point2D) tab[4]).getY());
    	assertTrue(tab[5] instanceof Point2D);
    	assertEpsilonEquals(6, ((Point2D) tab[5]).getX());
    	assertEpsilonEquals(5, ((Point2D) tab[5]).getY());
    	assertTrue(tab[6] instanceof Point2D);
    	assertEpsilonEquals(7, ((Point2D) tab[6]).getX());
    	assertEpsilonEquals(-5, ((Point2D) tab[6]).getY());
    }

    /**
     */
	@Test
    public void toArrayArray() {
    	Point2D[] tab = new Point2D[5];
    	Point2D[] tab2 = this.collection.toArray(tab);
    	assertSame(tab, tab2);
    	assertEquals(5, tab.length);
    	assertEpsilonEquals(1, tab[0].getX());
    	assertEpsilonEquals(1, tab[0].getY());
    	assertEpsilonEquals(2, tab[1].getX());
    	assertEpsilonEquals(2, tab[1].getY());
    	assertEpsilonEquals(3, tab[2].getX());
    	assertEpsilonEquals(0, tab[2].getY());
    	assertEpsilonEquals(4, tab[3].getX());
    	assertEpsilonEquals(3, tab[3].getY());
    	assertEpsilonEquals(5, tab[4].getX());
    	assertEpsilonEquals(-1, tab[4].getY());
    }

    /**
     */
	@Test
    public void add() {
    	assertTrue(this.collection.add(this.factory.createPoint(123, 456)));
    	assertCoords(1, 1, 2, 2, 3, 0, 4, 3, 5, -1, 6, 5, 7, -5, 123, 456);
    	this.shape.clear();
    	assertCoords();
    	assertTrue(this.collection.add(this.factory.createPoint(123, 456)));
    	assertCoords(123, 456);
    	assertTrue(this.collection.add(this.factory.createPoint(789, 1011)));
    	assertCoords(123, 456, 789, 1011);
    }

    /**
     */
	@Test
    public void remove() {
    	assertFalse(this.collection.remove(new Object()));
    	assertTrue(this.collection.remove(this.factory.createPoint(2, 2)));
    	assertCoords(1, 1, 3, 0, 4, 3, 5, -1, 6, 5, 7, -5);
    	assertTrue(this.collection.remove(this.factory.createPoint(6, 5)));
    	assertCoords(1, 1, 3, 0, 4, 3);
    }


    /**
     */
	@Test
    public void containsAll() {
    	assertTrue(this.collection.containsAll(
    			Arrays.asList(this.factory.createPoint(1, 1), this.factory.createPoint(6, 5))));
    	assertFalse(this.collection.containsAll(
    			Arrays.asList(this.factory.createPoint(1, 1), this.factory.createPoint(6, 6))));
    }

    /**
     */
	@Test
    public void addAll() {
    	this.collection.addAll(
    			Arrays.asList(this.factory.createPoint(123, 456), this.factory.createPoint(789, 1011)));
    	assertCoords(1, 1, 2, 2, 3, 0, 4, 3, 5, -1, 6, 5, 7, -5, 123, 456, 789, 1011);
    }

    /**
     */
	@Test
    public void removeAll() {
    	this.collection.removeAll(
    			Arrays.asList(this.factory.createPoint(123, 456), this.factory.createPoint(2, 2)));
    	assertCoords(1, 1, 3, 0, 4, 3, 5, -1, 6, 5, 7, -5);
    }

    /**
     */
	@Test
    public void retainAll() {
    	try {
    		this.collection.retainAll(Collections.emptyList());
    		fail("Expecting an exception");  //$NON-NLS-1$
    	}
    	catch(Throwable e1) {
    		e1.equals(e1);
    		// Expecting an exception
    	}
    }

    /**
     */
	@Test
    public void clear() {
    	this.collection.clear();
    	assertCoords();
    }
    
}