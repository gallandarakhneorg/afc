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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import org.arakhne.afc.math.geometry.PathWindingRule;
import org.arakhne.afc.math.geometry.d2.continuous.Path2f;
import org.arakhne.afc.math.geometry.d2.continuous.AbstractPathElement2F;
import org.arakhne.afc.math.geometry.d2.continuous.PathIterator2f;
import org.junit.Test;

/** Abstract tests for the paths.
 * 
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public abstract class AbstractPath2fTestCase extends AbstractShape2fTestCase<Path2f> {
	
	@Test
	@Override
	public void isEmpty() {
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

	/**
	 */
	@Test
	public void isPolyline() {
		assertFalse(this.r.isPolyline());

		this.r.clear();
		assertTrue(this.r.isPolyline());
		
		this.r.moveTo(1f, 2f);
		assertTrue(this.r.isPolyline());
		this.r.moveTo(3f, 4f);
		assertTrue(this.r.isPolyline());
		this.r.lineTo(5f, 6f);
		assertTrue(this.r.isPolyline());
		this.r.closePath();
		assertTrue(this.r.isPolyline());

		this.r.clear();
		assertTrue(this.r.isPolyline());

		this.r.moveTo(1f, 2f);
		assertTrue(this.r.isPolyline());
		this.r.moveTo(3f, 4f);
		assertTrue(this.r.isPolyline());
		this.r.curveTo(5f, 6f, 7f, 8f, 9f, 10f);
		assertFalse(this.r.isPolyline());
		this.r.closePath();
		assertFalse(this.r.isPolyline());

		this.r.clear();
		assertTrue(this.r.isPolyline());

		this.r.moveTo(1f, 2f);
		assertTrue(this.r.isPolyline());
		this.r.moveTo(3f, 4f);
		assertTrue(this.r.isPolyline());
		this.r.quadTo(5f, 6f, 7f, 8f);
		assertFalse(this.r.isPolyline());
		this.r.lineTo(5f, 6f);
		assertFalse(this.r.isPolyline());
	}

	@Test
	@Override
	public void clear() {
		this.r.clear();
		assertEquals(0, this.r.size());
	}

	@Test
	@Override
	public void testClone() {
		Path2f b = this.r.clone();

		assertNotSame(b, this.r);
		AbstractPathElement2F pe1, pe2;
		PathIterator2f i1 = this.r.getPathIterator();
		PathIterator2f i2 = b.getPathIterator();
		assertNotSame(i1, i2);
		while (i1.hasNext()) {
			assertTrue(i2.hasNext());
			pe1 = i1.next();
			pe2 = i2.next();
			assertNotSame(pe1, pe2);
			assertEquals(pe1.type, pe2.type);
			assertEpsilonEquals(pe1.getFromX(), pe2.getFromX());
			assertEpsilonEquals(pe1.getFromY(), pe2.getFromY());
			assertEpsilonEquals(pe1.getCtrlX1(), pe2.getCtrlX1());
			assertEpsilonEquals(pe1.getCtrlY1(), pe2.getCtrlY1());
			assertEpsilonEquals(pe1.getCtrlX2(), pe2.getCtrlX2());
			assertEpsilonEquals(pe1.getCtrlY2(), pe2.getCtrlY2());
			assertEpsilonEquals(pe1.getToX(), pe2.getToX());
			assertEpsilonEquals(pe1.getToY(), pe2.getToY());
			
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
			assertEpsilonEquals(pe1.getFromX(), pe2.getFromX());
			assertEpsilonEquals(pe1.getFromY(), pe2.getFromY());
			assertEpsilonEquals(pe1.getCtrlX1(), pe2.getCtrlX1());
			assertEpsilonEquals(pe1.getCtrlY1(), pe2.getCtrlY1());
			assertEpsilonEquals(pe1.getCtrlX2(), pe2.getCtrlX2());
			assertEpsilonEquals(pe1.getCtrlY2(), pe2.getCtrlY2());
			assertEpsilonEquals(pe1.getToX(), pe2.getToX());
			assertEpsilonEquals(pe1.getToY(), pe2.getToY());
			
		}
		assertFalse(i2.hasNext());
	}

	/**
	 */
	@Test
	public abstract void containsPathIterator2fFloatFloat();

	/**
	 */
	@Test
	public abstract void containsPathIterator2fFloatFloatFloatFloat();

	/**
	 */
	@Test
	public abstract void intersectsPathIterator2fFloatFloatFloatFloat();
	
	/**
	 */
	@Test
	public abstract void getClosestPointToPoint2D();	

	/**
	 */
	@Test
	public abstract void getClosestPointToPathIterator2fFloatFloat();	

	/**
	 */
	@Test
	public abstract void getFarthestPointToPoint2D();	

	/**
	 */
	@Test
	public abstract void getFarthestPointToPathIterator2fFloatFloat();	

	/**
	 */
	@Test
	public void setWindingRule() {
		assertEquals(PathWindingRule.NON_ZERO, this.r.getWindingRule());
		for(PathWindingRule rule : PathWindingRule.values()) {
			this.r.setWindingRule(rule);
			assertEquals(rule, this.r.getWindingRule());
		}
	}

	/**
	 */
	@Test
	public abstract void addIterator();

	/** 
	 */
	@Test
	public abstract void getPathIteratorVoid();

	/** 
	 */
	@Test
	public abstract void getPathIteratorTransform2D();

	/** 
	 */
	@Test
	public abstract void transformTransform2D();

	/** 
	 */
	@Test
	public abstract void createTransformedShape2D();

	/** 
	 */
	@Test
	public abstract void containsFloatfloat();

	/** 
	 */
	@Test
	public abstract void containsRectangle2f();

	/** 
	 */
	@Test
	public abstract void intersectsRectangle2f();

	/** 
	 */
	@Test
	public abstract void intersectsEllipse2f();

	/** 
	 */
	@Test
	public abstract void intersectsCircle2f();

	/** 
	 */
	@Test
	public abstract void intersectsSegment2f();

	@Test
	@Override
	public abstract void toBoundingBox();
	
	/**
	 */
	@Test
	public abstract void toBoundingBoxWithCtrlPoints();

	/**
	 */
	@Test
	public abstract void removeLast();

	/**
	 */
	@Test
	public abstract void setLastPointFloatFloat();
	
	/**
	 */
	@Test
	public abstract void removeFloatFloat();

	/**
	 */
	@Test
	public abstract void containsPointPoint2D();

}