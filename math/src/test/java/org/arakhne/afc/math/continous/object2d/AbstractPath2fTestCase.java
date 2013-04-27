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
package org.arakhne.afc.math.continous.object2d;

import org.arakhne.afc.math.generic.PathWindingRule;

/** Abstract tests for the paths.
 * 
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public abstract class AbstractPath2fTestCase extends AbstractShape2fTestCase<Path2f> {
	
	@Override
	public void testIsEmpty() {
		assertFalse(this.r.isEmpty());

		this.r.clear();
		assertTrue(this.r.isEmpty());
		
		this.r.moveTo(1f, 2f);
		assertTrue(this.r.isEmpty());
		this.r.moveTo(3f, 4f);
		assertTrue(this.r.isEmpty());
		this.r.lineTo(5f, 6f);
		assertFalse(this.r.isEmpty());
		this.r.closePath();
		assertFalse(this.r.isEmpty());

		this.r.clear();
		assertTrue(this.r.isEmpty());

		this.r.moveTo(1f, 2f);
		assertTrue(this.r.isEmpty());
		this.r.moveTo(3f, 4f);
		assertTrue(this.r.isEmpty());
		this.r.lineTo(3f, 4f);
		assertTrue(this.r.isEmpty());
		this.r.closePath();
		assertTrue(this.r.isEmpty());

		this.r.clear();
		assertTrue(this.r.isEmpty());

		this.r.moveTo(1f, 2f);
		assertTrue(this.r.isEmpty());
		this.r.moveTo(3f, 4f);
		assertTrue(this.r.isEmpty());
		this.r.lineTo(3f, 4f);
		assertTrue(this.r.isEmpty());
		this.r.lineTo(5f, 6f);
		assertFalse(this.r.isEmpty());
	}

	@Override
	public void testClear() {
		this.r.clear();
		assertEquals(0, this.r.size());
	}

	@Override
	public void testClone() {
		Path2f b = this.r.clone();

		assertNotSame(b, this.r);
		PathElement2f pe1, pe2;
		PathIterator2f i1 = this.r.getPathIterator();
		PathIterator2f i2 = b.getPathIterator();
		assertNotSame(i1, i2);
		while (i1.hasNext()) {
			assertTrue(i2.hasNext());
			pe1 = i1.next();
			pe2 = i2.next();
			assertNotSame(pe1, pe2);
			assertEquals(pe1.type, pe2.type);
			assertEpsilonEquals(pe1.fromX, pe2.fromX);
			assertEpsilonEquals(pe1.fromY, pe2.fromY);
			assertEpsilonEquals(pe1.ctrlX1, pe2.ctrlX1);
			assertEpsilonEquals(pe1.ctrlY1, pe2.ctrlY1);
			assertEpsilonEquals(pe1.ctrlX2, pe2.ctrlX2);
			assertEpsilonEquals(pe1.ctrlY2, pe2.ctrlY2);
			assertEpsilonEquals(pe1.toX, pe2.toX);
			assertEpsilonEquals(pe1.toY, pe2.toY);
			
		}
		assertFalse(i2.hasNext());
		
		b.translate(1f, 1f);

		i1 = this.r.getPathIterator();
		i2 = b.getPathIterator();
		while (i1.hasNext()) {
			assertTrue(i2.hasNext());
			pe1 = i1.next();
			pe2 = i2.next();
			assertEquals(pe1.type, pe2.type);
			assertNotEpsilonEquals(pe1.fromX, pe2.fromX);
			assertNotEpsilonEquals(pe1.fromY, pe2.fromY);
			assertNotEpsilonEquals(pe1.ctrlX1, pe2.ctrlX1);
			assertNotEpsilonEquals(pe1.ctrlY1, pe2.ctrlY1);
			assertNotEpsilonEquals(pe1.ctrlX2, pe2.ctrlX2);
			assertNotEpsilonEquals(pe1.ctrlY2, pe2.ctrlY2);
			assertNotEpsilonEquals(pe1.toX, pe2.toX);
			assertNotEpsilonEquals(pe1.toY, pe2.toY);
			
		}
		assertFalse(i2.hasNext());
	}

	/**
	 */
	public abstract void testContainsPathIterator2fFloatFloat();

	/**
	 */
	public abstract void testContainsPathIterator2fFloatFloatFloatFloat();

	/**
	 */
	public abstract void testIntersectsPathIterator2fFloatFloatFloatFloat();
	
	/**
	 */
	public abstract void testGetClosestPointTo();	

	/**
	 */
	public void testSetWindingRule() {
		assertEquals(PathWindingRule.NON_ZERO, this.r.getWindingRule());
		for(PathWindingRule rule : PathWindingRule.values()) {
			this.r.setWindingRule(rule);
			assertEquals(rule, this.r.getWindingRule());
		}
	}

	/**
	 */
	public abstract void testAddIterator();

	/** 
	 */
	public abstract void testGetPathIteratorVoid();

	/** 
	 */
	public abstract void testGetPathIteratorTransform2D();

	/** 
	 */
	public abstract void testTransformTransform2D();

	/** 
	 */
	public abstract void testCreateTransformedShape2D();

	/** 
	 */
	public abstract void testContainsFloatfloat();

	/** 
	 */
	public abstract void testContainsRectangle2f();

	/** 
	 */
	public abstract void testIntersectsRectangle2f();

	/** 
	 */
	public abstract void testIntersectsEllipse2f();

	/** 
	 */
	public abstract void testIntersectsCircle2f();

	/** 
	 */
	public abstract void testIntersectsSegment2f();

	@Override
	public abstract void testToBoundingBox();
	
	public abstract void testToBoundingBoxWithCtrlPoints();

	/**
	 */
	public abstract void testRemoveLast();

	/**
	 */
	public abstract void testSetLastPointFloatFloat();
	
	/**
	 */
	public abstract void testRemoveFloatFloat();

	/**
	 */
	public abstract void testContainsPointPoint2D();

}