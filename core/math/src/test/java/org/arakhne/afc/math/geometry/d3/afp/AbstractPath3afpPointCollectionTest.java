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
package org.arakhne.afc.math.geometry.d3.afp;

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
import org.arakhne.afc.math.geometry.PathWindingRule;
import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.afp.Path2afp;
import org.arakhne.afc.math.geometry.d3.Point3D;
import org.arakhne.afc.math.geometry.d3.Vector3D;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

@SuppressWarnings("all")
public abstract class AbstractPath3afpPointCollectionTest<P extends Point3D<? super P, ? super V>,
		V extends Vector3D<? super V, ? super P>,
		B extends RectangularPrism3afp<?, ?, ?, P, V, B>> extends AbstractMathTestCase {
	
	/** Is the shape to test.
	 */
	protected Path3afp<?, ?, ?, P, V, B> shape;
	
	/** Is the collection to test.
	 */
	protected Collection<P> collection;
	
	/** Shape factory.
	 */
	protected TestShapeFactory3afp<P, V, B> factory;

	protected abstract TestShapeFactory3afp<P, V, B> createFactory();

	@Before
	public void setUp() throws Exception {
		this.factory = createFactory();
		this.shape = (Path3afp<?, ?, ?, P, V, B>) this.factory.createPath(PathWindingRule.EVEN_ODD);
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
    		fail("Expecting an exception"); //$NON-NLS-1$
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