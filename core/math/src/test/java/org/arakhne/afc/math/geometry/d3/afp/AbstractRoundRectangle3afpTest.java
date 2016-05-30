/* 
 * $Id$
 * 
 * Copyright (C) 2011 Janus Core Developers
 * Copyright (C) 2012-13 Stephane GALLAND.
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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import org.arakhne.afc.math.MathConstants;
import org.arakhne.afc.math.geometry.PathElementType;
import org.arakhne.afc.math.geometry.PathWindingRule;
import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.Shape2D;
import org.arakhne.afc.math.geometry.d2.Transform2D;
import org.arakhne.afc.math.geometry.d2.Vector2D;
import org.arakhne.afc.math.geometry.d2.afp.RoundRectangle2afp.RoundRectanglePathIterator;
import org.arakhne.afc.math.geometry.d2.afp.RoundRectangle2afp.TransformedRoundRectanglePathIterator;
import org.arakhne.afc.math.geometry.d2.ai.PathIterator2ai;
import org.eclipse.xtext.xbase.lib.Pure;
import org.junit.Test;

@SuppressWarnings("all")
public abstract class AbstractRoundRectangle3afpTest<T extends RoundRectangle2afp<?, T, ?, ?, ?, B>,
		B extends Rectangle2afp<?, ?, ?, ?, ?, B>> extends AbstractRectangularShape3afpTest<T, B> {

	@Override
	protected final T createShape() {
		return (T) createRoundRectangle(5, 8, 5, 10, .1, .2);
	}

	@Test
	public void staticContainsRoundRectanglePoint() {
		assertFalse(RoundRectangle2afp.containsRoundRectanglePoint(5, 8, 5, 10, .1, .2, 0, 0));
		assertFalse(RoundRectangle2afp.containsRoundRectanglePoint(5, 8, 5, 10, .1, .2, 20, 0));
		assertFalse(RoundRectangle2afp.containsRoundRectanglePoint(5, 8, 5, 10, .1, .2, 20, 20));
		assertFalse(RoundRectangle2afp.containsRoundRectanglePoint(5, 8, 5, 10, .1, .2, 0, 20));
		assertTrue(RoundRectangle2afp.containsRoundRectanglePoint(5, 8, 5, 10, .1, .2, 8, 13));
		assertTrue(RoundRectangle2afp.containsRoundRectanglePoint(5, 8, 5, 10, .1, .2, 5, 13));
		assertFalse(RoundRectangle2afp.containsRoundRectanglePoint(5, 8, 5, 10, .1, .2, 4.999, 13));
		assertFalse(RoundRectangle2afp.containsRoundRectanglePoint(5, 8, 5, 10, .1, .2, 5, 8));
		assertFalse(RoundRectangle2afp.containsRoundRectanglePoint(5, 8, 5, 10, .1, .2, 5, 18));
		assertFalse(RoundRectangle2afp.containsRoundRectanglePoint(5, 8, 5, 10, .1, .2, 15, 18));
		assertFalse(RoundRectangle2afp.containsRoundRectanglePoint(5, 8, 5, 10, .1, .2, 15, 8));
	}
	
	@Test
	public void staticContainsRoundRectangleRectangle() {
		assertFalse(RoundRectangle2afp.containsRoundRectangleRectangle(5, 8, 5, 10, .1, .2, 0, 0, 1, 1));
		assertFalse(RoundRectangle2afp.containsRoundRectangleRectangle(5, 8, 5, 10, .1, .2, 0, 0, 7, 10));
		assertFalse(RoundRectangle2afp.containsRoundRectangleRectangle(5, 8, 5, 10, .1, .2, 0, 0, 20, 20));
		assertTrue(RoundRectangle2afp.containsRoundRectangleRectangle(5, 8, 5, 10, .1, .2, 6, 10, 1, 1));
		assertFalse(RoundRectangle2afp.containsRoundRectangleRectangle(5, 8, 5, 10, .1, .2, 5, 8, 5, 10));
		assertTrue(RoundRectangle2afp.containsRoundRectangleRectangle(5, 8, 5, 10, .1, .2, 5.5, 8.5, 4, 9));
	}

	@Test
	public void staticIntersectsRoundRectangleSegment() {
		assertFalse(RoundRectangle2afp.intersectsRoundRectangleSegment(5, 8, 10, 18, .1, .2, 0, 0, 1, 1));
		assertFalse(RoundRectangle2afp.intersectsRoundRectangleSegment(5, 8, 10, 18, .1, .2, 20, 20, 21, 21));
		assertTrue(RoundRectangle2afp.intersectsRoundRectangleSegment(5, 8, 10, 18, .1, .2, 0, 0, 7, 12));
		assertFalse(RoundRectangle2afp.intersectsRoundRectangleSegment(5, 8, 10, 18, .1, .2, 0, 0, 7, 8));
		assertFalse(RoundRectangle2afp.intersectsRoundRectangleSegment(5, 8, 10, 18, .1, .2, 6, 7, 4.1, 9));
		assertTrue(RoundRectangle2afp.intersectsRoundRectangleSegment(5, 8, 10, 18, .1, .2, 6.1, 7, 4.1, 9));
	}

	@Test
	public void staticIntersectsRoundRectangleRoundRectangle() {
		assertFalse(RoundRectangle2afp.intersectsRoundRectangleRoundRectangle(5, 8, 10, 18, .1, .2, 0, 0, 1, 1, .1, .2));
		assertFalse(RoundRectangle2afp.intersectsRoundRectangleRoundRectangle(5, 8, 10, 18, .1, .2, 20, 20, 21, 21, .1, .2));
		assertTrue(RoundRectangle2afp.intersectsRoundRectangleRoundRectangle(5, 8, 10, 18, .1, .2, 0, 0, 7, 12, .1, .2));
		assertFalse(RoundRectangle2afp.intersectsRoundRectangleRoundRectangle(5, 8, 10, 18, .1, .2, 0, 0, 7, 8, .1, .2));
		assertFalse(RoundRectangle2afp.intersectsRoundRectangleRoundRectangle(5, 8, 10, 18, .1, .2, 0, 0, 5.01, 8.01, .1, .2));
		assertFalse(RoundRectangle2afp.intersectsRoundRectangleRoundRectangle(5, 8, 10, 18, .1, .2, 0, 0, 5.05, 8.05, .1, .2));
		assertFalse(RoundRectangle2afp.intersectsRoundRectangleRoundRectangle(5, 8, 10, 18, .1, .2, 0, 0, 5.05, 8.1, .1, .2));
		assertTrue(RoundRectangle2afp.intersectsRoundRectangleRoundRectangle(5, 8, 10, 18, .1, .2, 0, 0, 5.05, 8.15, .1, .2));
	}

	@Test
	public void staticIntersectsRoundRectangleRectangle() {
		assertFalse(RoundRectangle2afp.intersectsRoundRectangleRectangle(5, 8, 10, 18, .1, .2, 0, 0, 1, 1));
		assertFalse(RoundRectangle2afp.intersectsRoundRectangleRectangle(5, 8, 10, 18, .1, .2, 20, 20, 21, 21));
		assertTrue(RoundRectangle2afp.intersectsRoundRectangleRectangle(5, 8, 10, 18, .1, .2, 0, 0, 7, 12));
		assertFalse(RoundRectangle2afp.intersectsRoundRectangleRectangle(5, 8, 10, 18, .1, .2, 0, 0, 7, 8));
		assertFalse(RoundRectangle2afp.intersectsRoundRectangleRectangle(5, 8, 10, 18, .1, .2, 0, 0, 5.01, 8.01));
		assertTrue(RoundRectangle2afp.intersectsRoundRectangleRectangle(5, 8, 10, 18, .1, .2, 0, 0, 5.05, 8.05));
		assertTrue(RoundRectangle2afp.intersectsRoundRectangleRectangle(5, 8, 10, 18, .1, .2, 0, 0, 5.05, 8.1));
		assertTrue(RoundRectangle2afp.intersectsRoundRectangleRectangle(5, 8, 10, 18, .1, .2, 0, 0, 5.05, 8.15));
	}

	@Test
	public void staticIntersectsRoundRectangleCircle() {
		assertFalse(RoundRectangle2afp.intersectsRoundRectangleCircle(5, 8, 10, 18, .1, .2, 7, 0, 1));
		assertFalse(RoundRectangle2afp.intersectsRoundRectangleCircle(5, 8, 10, 18, .1, .2, 7, 20, 1));
		assertFalse(RoundRectangle2afp.intersectsRoundRectangleCircle(5, 8, 10, 18, .1, .2, 0, 12, 1));
		assertFalse(RoundRectangle2afp.intersectsRoundRectangleCircle(5, 8, 10, 18, .1, .2, 20, 12, 1));

		assertFalse(RoundRectangle2afp.intersectsRoundRectangleCircle(5, 8, 10, 18, .1, .2, 0, 0, 1));
		assertFalse(RoundRectangle2afp.intersectsRoundRectangleCircle(5, 8, 10, 18, .1, .2, 20, 0, 1));
		assertFalse(RoundRectangle2afp.intersectsRoundRectangleCircle(5, 8, 10, 18, .1, .2, 20, 20, 1));
		assertFalse(RoundRectangle2afp.intersectsRoundRectangleCircle(5, 8, 10, 18, .1, .2, 0, 20, 1));

		assertFalse(RoundRectangle2afp.intersectsRoundRectangleCircle(5, 8, 10, 18, .1, .2, 4, 12, 1));
		assertTrue(RoundRectangle2afp.intersectsRoundRectangleCircle(5, 8, 10, 18, .1, .2, 4.1, 12, 1));
		assertTrue(RoundRectangle2afp.intersectsRoundRectangleCircle(5, 8, 10, 18, .1, .2, 6, 12, 1));
		assertTrue(RoundRectangle2afp.intersectsRoundRectangleCircle(5, 8, 10, 18, .1, .2, 10.9, 12, 1));
		assertFalse(RoundRectangle2afp.intersectsRoundRectangleCircle(5, 8, 10, 18, .1, .2, 11, 12, 1));

		assertFalse(RoundRectangle2afp.intersectsRoundRectangleCircle(5, 8, 10, 18, .1, .2, 7, 7, 1));
		assertTrue(RoundRectangle2afp.intersectsRoundRectangleCircle(5, 8, 10, 18, .1, .2, 7, 7.1, 1));
		assertTrue(RoundRectangle2afp.intersectsRoundRectangleCircle(5, 8, 10, 18, .1, .2, 7, 12, 1));
		assertTrue(RoundRectangle2afp.intersectsRoundRectangleCircle(5, 8, 10, 18, .1, .2, 7, 18.9, 1));
		assertFalse(RoundRectangle2afp.intersectsRoundRectangleCircle(5, 8, 10, 18, .1, .2, 7, 19, 1));

		assertFalse(RoundRectangle2afp.intersectsRoundRectangleCircle(5, 8, 10, 18, .1, .2, 4.32, 7.32, 1));
		assertTrue(RoundRectangle2afp.intersectsRoundRectangleCircle(5, 8, 10, 18, .1, .2, 4.4, 7.4, 1));
		assertTrue(RoundRectangle2afp.intersectsRoundRectangleCircle(5, 8, 10, 18, .1, .2, 4.75, 7.75, 1));
		assertTrue(RoundRectangle2afp.intersectsRoundRectangleCircle(5, 8, 10, 18, .1, .2, 4.19, 7.55, 1));
	}

	@Test
	public void staticIntersectsRoundRectangleEllipse() {
		assertFalse(RoundRectangle2afp.intersectsRoundRectangleEllipse(5, 8, 10, 18, .1, .2, 6, -.5, 2, 1));
		assertFalse(RoundRectangle2afp.intersectsRoundRectangleEllipse(5, 8, 10, 18, .1, .2, 6, 19.5, 2, 1));
		assertFalse(RoundRectangle2afp.intersectsRoundRectangleEllipse(5, 8, 10, 18, .1, .2, -1, 11.5, 2, 1));
		assertFalse(RoundRectangle2afp.intersectsRoundRectangleEllipse(5, 8, 10, 18, .1, .2, 19, 11.5, 2, 1));

		assertFalse(RoundRectangle2afp.intersectsRoundRectangleEllipse(5, 8, 10, 18, .1, .2, -1, -.5, 2, 1));
		assertFalse(RoundRectangle2afp.intersectsRoundRectangleEllipse(5, 8, 10, 18, .1, .2, 19, -.5, 2, 1));
		assertFalse(RoundRectangle2afp.intersectsRoundRectangleEllipse(5, 8, 10, 18, .1, .2, 19, 19.5, 2, 1));
		assertFalse(RoundRectangle2afp.intersectsRoundRectangleEllipse(5, 8, 10, 18, .1, .2, -1, 19.5, 2, 1));

		assertFalse(RoundRectangle2afp.intersectsRoundRectangleEllipse(5, 8, 10, 18, .1, .2, 3, 11.5, 2, 1));
		assertTrue(RoundRectangle2afp.intersectsRoundRectangleEllipse(5, 8, 10, 18, .1, .2, 3.1, 11.5, 2, 1));
		assertTrue(RoundRectangle2afp.intersectsRoundRectangleEllipse(5, 8, 10, 18, .1, .2, 5, 11.5, 2, 1));
		assertTrue(RoundRectangle2afp.intersectsRoundRectangleEllipse(5, 8, 10, 18, .1, .2, 9.9, 11.5, 2, 1));
		assertFalse(RoundRectangle2afp.intersectsRoundRectangleEllipse(5, 8, 10, 18, .1, .2, 10, 11.5, 2, 1));

		assertFalse(RoundRectangle2afp.intersectsRoundRectangleEllipse(5, 8, 10, 18, .1, .2, 6, 6.5, 2, 1));
		assertFalse(RoundRectangle2afp.intersectsRoundRectangleEllipse(5, 8, 10, 18, .1, .2, 6, 6.6, 2, 1));
		assertTrue(RoundRectangle2afp.intersectsRoundRectangleEllipse(5, 8, 10, 18, .1, .2, 6, 11.5, 2, 1));
		assertFalse(RoundRectangle2afp.intersectsRoundRectangleEllipse(5, 8, 10, 18, .1, .2, 6, 18.4, 2, 1));
		assertFalse(RoundRectangle2afp.intersectsRoundRectangleEllipse(5, 8, 10, 18, .1, .2, 6, 18.5, 2, 1));

		assertFalse(RoundRectangle2afp.intersectsRoundRectangleEllipse(5, 8, 10, 18, .1, .2, 3.32, 6.82, 2, 1));
		assertFalse(RoundRectangle2afp.intersectsRoundRectangleEllipse(5, 8, 10, 18, .1, .2, 3.4, 6.9, 2, 1));
		assertTrue(RoundRectangle2afp.intersectsRoundRectangleEllipse(5, 8, 10, 18, .1, .2, 3.75, 7.25, 2, 1));
		assertFalse(RoundRectangle2afp.intersectsRoundRectangleEllipse(5, 8, 10, 18, .1, .2, 3.19, 7.05, 2, 1));
		assertTrue(RoundRectangle2afp.intersectsRoundRectangleEllipse(5, 8, 10, 18, .1, .2, 3.08, 7.45, 2, 1));
	}

	@Test
	public void getArcWidth() {
		assertEpsilonEquals(.1, this.shape.getArcWidth());
	}

	@Test
	public void getArcHeight() {
		assertEpsilonEquals(.2, this.shape.getArcHeight());
	}

	@Test
	public void setArcWidth() {
		this.shape.setArcWidth(123.456);
		assertEpsilonEquals(2.5, this.shape.getArcWidth());
	}

	@Test
	public void setArcHeight() {
		this.shape.setArcHeight(123.456);
		assertEpsilonEquals(5, this.shape.getArcHeight());
	}

	@Test
	public void setArcWidth_whenSetMaxX() {
		this.shape.setMaxX(6);
		assertEpsilonEquals(.1, this.shape.getArcWidth());
		this.shape.setMaxX(5.1);
		assertEpsilonEquals(.05, this.shape.getArcWidth());
	}

	@Test
	public void setArcWidth_whenSetMinX() {
		this.shape.setMinX(4);
		assertEpsilonEquals(.1, this.shape.getArcWidth());
		this.shape.setMinX(9.9);
		assertEpsilonEquals(.05, this.shape.getArcWidth());
	}

	@Test
	public void setArcWidth_whenSetWidth() {
		this.shape.setWidth(1);
		assertEpsilonEquals(.1, this.shape.getArcWidth());
		this.shape.setWidth(.1);
		assertEpsilonEquals(.05, this.shape.getArcWidth());
	}

	@Test
	public void setArcHeight_whenSetMaxY() {
		this.shape.setMaxY(9);
		assertEpsilonEquals(.2, this.shape.getArcHeight());
		this.shape.setMaxY(8.1);
		assertEpsilonEquals(.05, this.shape.getArcHeight());
	}

	@Test
	public void setArcHeight_whenSetMinY() {
		this.shape.setMinY(7);
		assertEpsilonEquals(.2, this.shape.getArcHeight());
		this.shape.setMinY(17.9);
		assertEpsilonEquals(.05, this.shape.getArcHeight());
	}

	@Test
	public void setArcHeight_whenSetHeight() {
		this.shape.setHeight(1);
		assertEpsilonEquals(.2, this.shape.getArcHeight());
		this.shape.setHeight(.2);
		assertEpsilonEquals(.1, this.shape.getArcHeight());
	}

	@Test
	public void setDoubleDoubleDoubleDoubleDoubleDouble() {
		this.shape.set(10, 20, 30, 40, 1, 2);
		assertEpsilonEquals(10, this.shape.getMinX());
		assertEpsilonEquals(20, this.shape.getMinY());
		assertEpsilonEquals(40, this.shape.getMaxX());
		assertEpsilonEquals(60, this.shape.getMaxY());
		assertEpsilonEquals(1, this.shape.getArcWidth());
		assertEpsilonEquals(2, this.shape.getArcHeight());
	}

	@Override
	public void setFromCornersDoubleDoubleDoubleDouble() {
		this.shape.setFromCorners(10, 20, 30, 40);
		assertEpsilonEquals(10, this.shape.getMinX());
		assertEpsilonEquals(20, this.shape.getMinY());
		assertEpsilonEquals(30, this.shape.getMaxX());
		assertEpsilonEquals(40, this.shape.getMaxY());
		assertEpsilonEquals(.1, this.shape.getArcWidth());
		assertEpsilonEquals(.2, this.shape.getArcHeight());
	}
	
	@Test
	public void setFromCornersDoubleDoubleDoubleDoubleDoubleDouble() {
		this.shape.setFromCorners(10, 20, 30, 40, 1, 2);
		assertEpsilonEquals(10, this.shape.getMinX());
		assertEpsilonEquals(20, this.shape.getMinY());
		assertEpsilonEquals(30, this.shape.getMaxX());
		assertEpsilonEquals(40, this.shape.getMaxY());
		assertEpsilonEquals(1, this.shape.getArcWidth());
		assertEpsilonEquals(2, this.shape.getArcHeight());
	}
	
	@Test
	@Override
	public void equalsObject() {
		assertFalse(this.shape.equals(null));
		assertFalse(this.shape.equals(new Object()));
		assertFalse(this.shape.equals(createRoundRectangle(0, 8, 5, 12, .1, .2)));
		assertFalse(this.shape.equals(createRoundRectangle(5, 8, 5, 0, .1, .2)));
		assertFalse(this.shape.equals(createSegment(5, 8, 5, 10)));
		assertTrue(this.shape.equals(this.shape));
		assertTrue(this.shape.equals(createRoundRectangle(5, 8, 5, 10, .1, .2)));
	}

	@Test
	@Override
	public void equalsObject_withPathIterator() {
		assertFalse(this.shape.equals(createRoundRectangle(0, 8, 5, 12, .1, .2).getPathIterator()));
		assertFalse(this.shape.equals(createRoundRectangle(5, 8, 5, 0, .1, .2).getPathIterator()));
		assertFalse(this.shape.equals(createSegment(5, 8, 5, 10).getPathIterator()));
		assertTrue(this.shape.equals(this.shape.getPathIterator()));
		assertTrue(this.shape.equals(createRoundRectangle(5, 8, 5, 10, .1, .2).getPathIterator()));
	}

	@Test
	@Override
	public void equalsToPathIterator() {
		assertFalse(this.shape.equalsToPathIterator((PathIterator2ai) null));
		assertFalse(this.shape.equalsToPathIterator(createRoundRectangle(0, 8, 5, 12, .1, .2).getPathIterator()));
		assertFalse(this.shape.equalsToPathIterator(createRoundRectangle(5, 8, 5, 0, .1, .2).getPathIterator()));
		assertFalse(this.shape.equalsToPathIterator(createSegment(5, 8, 5, 10).getPathIterator()));
		assertTrue(this.shape.equalsToPathIterator(this.shape.getPathIterator()));
		assertTrue(this.shape.equalsToPathIterator(createRoundRectangle(5, 8, 5, 10, .1, .2).getPathIterator()));
	}

	@Override
	public void equalsToShape() {
		assertFalse(this.shape.equalsToShape(null));
		assertFalse(this.shape.equalsToShape((T) createRoundRectangle(0, 8, 5, 12, .1, .2)));
		assertFalse(this.shape.equalsToShape((T) createRoundRectangle(5, 8, 5, 0, .1, .2)));
		assertTrue(this.shape.equalsToShape((T) this.shape));
		assertTrue(this.shape.equalsToShape((T) createRoundRectangle(5, 8, 5, 10, .1, .2)));
	}
	
	@Override
	public void containsDoubleDouble() {
		assertFalse(this.shape.contains(0, 0));
		assertFalse(this.shape.contains(20, 0));
		assertFalse(this.shape.contains(20, 20));
		assertFalse(this.shape.contains(0, 20));
		assertTrue(this.shape.contains(8, 13));
		assertTrue(this.shape.contains(5, 13));
		assertFalse(this.shape.contains(4.999, 13));
		assertFalse(this.shape.contains(5, 8));
		assertFalse(this.shape.contains(5, 18));
		assertFalse(this.shape.contains(15, 18));
		assertFalse(this.shape.contains(15, 8));
	}

	@Override
	public void containsPoint2D() {
		assertFalse(this.shape.contains(createPoint(0, 0)));
		assertFalse(this.shape.contains(createPoint(20, 0)));
		assertFalse(this.shape.contains(createPoint(20, 20)));
		assertFalse(this.shape.contains(createPoint(0, 20)));
		assertTrue(this.shape.contains(createPoint(8, 13)));
		assertTrue(this.shape.contains(createPoint(5, 13)));
		assertFalse(this.shape.contains(createPoint(4.999, 13)));
		assertFalse(this.shape.contains(createPoint(5, 8)));
		assertFalse(this.shape.contains(createPoint(5, 18)));
		assertFalse(this.shape.contains(createPoint(15, 18)));
		assertFalse(this.shape.contains(createPoint(15, 8)));
	}

	@Override
	public void getClosestPointTo() {
		Point2D p;
		
		p = this.shape.getClosestPointTo(createPoint(0, 0));
		assertEpsilonEquals(5.06983, p.getX());
		assertEpsilonEquals(8.00932, p.getY());

		p = this.shape.getClosestPointTo(createPoint(20, 0));
		assertEpsilonEquals(9.95303, p.getX());
		assertEpsilonEquals(8.03044, p.getY());

		p = this.shape.getClosestPointTo(createPoint(20, 20));
		assertEpsilonEquals(9.99206, p.getX());
		assertEpsilonEquals(17.8781, p.getY());

		p = this.shape.getClosestPointTo(createPoint(0, 20));
		assertEpsilonEquals(5.02287, p.getX());
		assertEpsilonEquals(17.92730, p.getY());

		p = this.shape.getClosestPointTo(createPoint(0, 11));
		assertEpsilonEquals(5, p.getX());
		assertEpsilonEquals(11, p.getY());

		p = this.shape.getClosestPointTo(createPoint(20, 11));
		assertEpsilonEquals(10, p.getX());
		assertEpsilonEquals(11, p.getY());

		p = this.shape.getClosestPointTo(createPoint(7, 0));
		assertEpsilonEquals(7, p.getX());
		assertEpsilonEquals(8, p.getY());

		p = this.shape.getClosestPointTo(createPoint(7, 20));
		assertEpsilonEquals(7, p.getX());
		assertEpsilonEquals(18, p.getY());

		p = this.shape.getClosestPointTo(createPoint(0, 8.2));
		assertEpsilonEquals(5, p.getX());
		assertEpsilonEquals(8.2, p.getY());

		p = this.shape.getClosestPointTo(createPoint(5.1, 0));
		assertEpsilonEquals(5.1, p.getX());
		assertEpsilonEquals(8, p.getY());

		p = this.shape.getClosestPointTo(createPoint(7, 10));
		assertEpsilonEquals(7, p.getX());
		assertEpsilonEquals(10, p.getY());
	}

	@Override
	public void getFarthestPointTo() {
		Point2D p;
		
		p = this.shape.getFarthestPointTo(createPoint(0, 0));
		assertEpsilonEquals(9.92696, p.getX());
		assertEpsilonEquals(17.99546, p.getY());

		p = this.shape.getFarthestPointTo(createPoint(20, 0));
		assertEpsilonEquals(5.01988, p.getX());
		assertEpsilonEquals(8.37926, p.getY());

		p = this.shape.getFarthestPointTo(createPoint(20, 20));
		assertEpsilonEquals(5.04194, p.getX());
		assertEpsilonEquals(8.01391, p.getY());

		p = this.shape.getFarthestPointTo(createPoint(0, 20));
		assertEpsilonEquals(9.93974, p.getX());
		assertEpsilonEquals(8.00821, p.getY());

		p = this.shape.getFarthestPointTo(createPoint(0, 11));
		assertEpsilonEquals(9.96556, p.getX());
		assertEpsilonEquals(17.98379, p.getY());

		p = this.shape.getFarthestPointTo(createPoint(20, 11));
		assertEpsilonEquals(4.90695, p.getX());
		assertEpsilonEquals(8.04902, p.getY());

		p = this.shape.getFarthestPointTo(createPoint(7, 0));
		assertEpsilonEquals(9.90806, p.getX());
		assertEpsilonEquals(17.99945, p.getY());

		p = this.shape.getFarthestPointTo(createPoint(7, 20));
		assertEpsilonEquals(9.91206, p.getX());
		assertEpsilonEquals(8.00115, p.getY());

		p = this.shape.getFarthestPointTo(createPoint(7, 10));
		assertEpsilonEquals(9.91803, p.getX());
		assertEpsilonEquals(17.99768, p.getY());
	}

	@Override
	public void getDistance() {
		assertEpsilonEquals(9.47905, this.shape.getDistance(createPoint(0, 0)));
		assertEpsilonEquals(12.86194, this.shape.getDistance(createPoint(20, 0)));
		assertEpsilonEquals(10.23041, this.shape.getDistance(createPoint(20, 20)));
		assertEpsilonEquals(5.43372, this.shape.getDistance(createPoint(0, 20)));
		assertEpsilonEquals(5, this.shape.getDistance(createPoint(0, 11)));
		assertEpsilonEquals(10, this.shape.getDistance(createPoint(20, 11)));
		assertEpsilonEquals(8, this.shape.getDistance(createPoint(7, 0)));
		assertEpsilonEquals(2, this.shape.getDistance(createPoint(7, 20)));
		assertEpsilonEquals(5, this.shape.getDistance(createPoint(0, 8.2)));
		assertEpsilonEquals(8, this.shape.getDistance(createPoint(5.1, 0)));
		assertEpsilonEquals(0, this.shape.getDistance(createPoint(7, 10)));
	}

	@Override
	public void getDistanceSquared() {
		assertEpsilonEquals(89.85239, this.shape.getDistanceSquared(createPoint(0, 0)));
		assertEpsilonEquals(165.4295, this.shape.getDistanceSquared(createPoint(20, 0)));
		assertEpsilonEquals(104.66129, this.shape.getDistanceSquared(createPoint(20, 20)));
		assertEpsilonEquals(29.52531, this.shape.getDistanceSquared(createPoint(0, 20)));
		assertEpsilonEquals(25, this.shape.getDistanceSquared(createPoint(0, 11)));
		assertEpsilonEquals(100, this.shape.getDistanceSquared(createPoint(20, 11)));
		assertEpsilonEquals(64, this.shape.getDistanceSquared(createPoint(7, 0)));
		assertEpsilonEquals(4, this.shape.getDistanceSquared(createPoint(7, 20)));
		assertEpsilonEquals(25, this.shape.getDistanceSquared(createPoint(0, 8.2)));
		assertEpsilonEquals(64, this.shape.getDistanceSquared(createPoint(5.1, 0)));
		assertEpsilonEquals(0, this.shape.getDistanceSquared(createPoint(7, 10)));
	}

	@Override
	public void getDistanceL1() {
		assertEpsilonEquals(13.07915, this.shape.getDistanceL1(createPoint(0, 0)));
		assertEpsilonEquals(18.07741, this.shape.getDistanceL1(createPoint(20, 0)));
		assertEpsilonEquals(12.12984, this.shape.getDistanceL1(createPoint(20, 20)));
		assertEpsilonEquals(7.09557, this.shape.getDistanceL1(createPoint(0, 20)));
		assertEpsilonEquals(5, this.shape.getDistanceL1(createPoint(0, 11)));
		assertEpsilonEquals(10, this.shape.getDistanceL1(createPoint(20, 11)));
		assertEpsilonEquals(8, this.shape.getDistanceL1(createPoint(7, 0)));
		assertEpsilonEquals(2, this.shape.getDistanceL1(createPoint(7, 20)));
		assertEpsilonEquals(5, this.shape.getDistanceL1(createPoint(0, 8.2)));
		assertEpsilonEquals(8, this.shape.getDistanceL1(createPoint(5.1, 0)));
		assertEpsilonEquals(0, this.shape.getDistanceL1(createPoint(7, 10)));
	}

	@Override
	public void getDistanceLinf() {
		assertEpsilonEquals(8.00932, this.shape.getDistanceLinf(createPoint(0, 0)));
		assertEpsilonEquals(10.04697, this.shape.getDistanceLinf(createPoint(20, 0)));
		assertEpsilonEquals(10.007934, this.shape.getDistanceLinf(createPoint(20, 20)));
		assertEpsilonEquals(5.02287, this.shape.getDistanceLinf(createPoint(0, 20)));
		assertEpsilonEquals(5, this.shape.getDistanceLinf(createPoint(0, 11)));
		assertEpsilonEquals(10, this.shape.getDistanceLinf(createPoint(20, 11)));
		assertEpsilonEquals(8, this.shape.getDistanceLinf(createPoint(7, 0)));
		assertEpsilonEquals(2, this.shape.getDistanceLinf(createPoint(7, 20)));
		assertEpsilonEquals(5, this.shape.getDistanceLinf(createPoint(0, 8.2)));
		assertEpsilonEquals(8, this.shape.getDistanceLinf(createPoint(5.1, 0)));
		assertEpsilonEquals(0, this.shape.getDistanceLinf(createPoint(7, 10)));
	}

	@Override
	public void setIT() {
		this.shape.set((T) createRoundRectangle(10, 20, 30, 40, 1, 2));
		assertEpsilonEquals(10, this.shape.getMinX());
		assertEpsilonEquals(20, this.shape.getMinY());
		assertEpsilonEquals(40, this.shape.getMaxX());
		assertEpsilonEquals(60, this.shape.getMaxY());
		assertEpsilonEquals(1, this.shape.getArcWidth());
		assertEpsilonEquals(2, this.shape.getArcHeight());
	}

	@Override
	public void getPathIterator() {
		PathIterator2afp pi = this.shape.getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 5.1, 8);
		assertElement(pi, PathElementType.LINE_TO, 9.9, 8);
		assertElement(pi, PathElementType.CURVE_TO, 9.95523, 8, 10, 8.08954, 10, 8.2);
		assertElement(pi, PathElementType.LINE_TO, 10, 17.8);
		assertElement(pi, PathElementType.CURVE_TO, 10, 17.91046, 9.95523, 18, 9.9, 18);
		assertElement(pi, PathElementType.LINE_TO, 5.1, 18);
		assertElement(pi, PathElementType.CURVE_TO, 5.04477, 18, 5, 17.91046, 5, 17.8);
		assertElement(pi, PathElementType.LINE_TO, 5, 8.2);
		assertElement(pi, PathElementType.CURVE_TO, 5, 8.08954, 5.04477, 8, 5.1, 8);
		assertElement(pi, PathElementType.CLOSE, 5.1, 8);
		assertNoElement(pi);
	}

	@Override
	public void getPathIteratorTransform2D() {
		PathIterator2afp pi;
		
		pi = this.shape.getPathIterator(null);
		assertElement(pi, PathElementType.MOVE_TO, 5.1, 8);
		assertElement(pi, PathElementType.LINE_TO, 9.9, 8);
		assertElement(pi, PathElementType.CURVE_TO, 9.95523, 8, 10, 8.08954, 10, 8.2);
		assertElement(pi, PathElementType.LINE_TO, 10, 17.8);
		assertElement(pi, PathElementType.CURVE_TO, 10, 17.91046, 9.95523, 18, 9.9, 18);
		assertElement(pi, PathElementType.LINE_TO, 5.1, 18);
		assertElement(pi, PathElementType.CURVE_TO, 5.04477, 18, 5, 17.91046, 5, 17.8);
		assertElement(pi, PathElementType.LINE_TO, 5, 8.2);
		assertElement(pi, PathElementType.CURVE_TO, 5, 8.08954, 5.04477, 8, 5.1, 8);
		assertElement(pi, PathElementType.CLOSE, 5.1, 8);
		assertNoElement(pi);

		pi = this.shape.getPathIterator(new Transform2D());
		assertElement(pi, PathElementType.MOVE_TO, 5.1, 8);
		assertElement(pi, PathElementType.LINE_TO, 9.9, 8);
		assertElement(pi, PathElementType.CURVE_TO, 9.95523, 8, 10, 8.08954, 10, 8.2);
		assertElement(pi, PathElementType.LINE_TO, 10, 17.8);
		assertElement(pi, PathElementType.CURVE_TO, 10, 17.91046, 9.95523, 18, 9.9, 18);
		assertElement(pi, PathElementType.LINE_TO, 5.1, 18);
		assertElement(pi, PathElementType.CURVE_TO, 5.04477, 18, 5, 17.91046, 5, 17.8);
		assertElement(pi, PathElementType.LINE_TO, 5, 8.2);
		assertElement(pi, PathElementType.CURVE_TO, 5, 8.08954, 5.04477, 8, 5.1, 8);
		assertElement(pi, PathElementType.CLOSE, 5.1, 8);
		assertNoElement(pi);

		Transform2D tr = new Transform2D();
		tr.setTranslation(10, -1);
		pi = this.shape.getPathIterator(tr);
		assertElement(pi, PathElementType.MOVE_TO, 15.1, 7);
		assertElement(pi, PathElementType.LINE_TO, 19.9, 7);
		assertElement(pi, PathElementType.CURVE_TO, 19.95523, 7, 20, 7.08954, 20, 7.2);
		assertElement(pi, PathElementType.LINE_TO, 20, 16.8);
		assertElement(pi, PathElementType.CURVE_TO, 20, 16.91046, 19.95523, 17, 19.9, 17);
		assertElement(pi, PathElementType.LINE_TO, 15.1, 17);
		assertElement(pi, PathElementType.CURVE_TO, 15.04477, 17, 15, 16.91046, 15, 16.8);
		assertElement(pi, PathElementType.LINE_TO, 15, 7.2);
		assertElement(pi, PathElementType.CURVE_TO, 15, 7.08954, 15.04477, 7, 15.1, 7);
		assertElement(pi, PathElementType.CLOSE, 15.1, 7);
		assertNoElement(pi);
	}

	@Override
	public void createTransformedShape() {
		PathIterator2afp pi;
		
		pi = this.shape.createTransformedShape(null).getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 5.1, 8);
		assertElement(pi, PathElementType.LINE_TO, 9.9, 8);
		assertElement(pi, PathElementType.CURVE_TO, 9.95523, 8, 10, 8.08954, 10, 8.2);
		assertElement(pi, PathElementType.LINE_TO, 10, 17.8);
		assertElement(pi, PathElementType.CURVE_TO, 10, 17.91046, 9.95523, 18, 9.9, 18);
		assertElement(pi, PathElementType.LINE_TO, 5.1, 18);
		assertElement(pi, PathElementType.CURVE_TO, 5.04477, 18, 5, 17.91046, 5, 17.8);
		assertElement(pi, PathElementType.LINE_TO, 5, 8.2);
		assertElement(pi, PathElementType.CURVE_TO, 5, 8.08954, 5.04477, 8, 5.1, 8);
		assertElement(pi, PathElementType.CLOSE, 5.1, 8);
		assertNoElement(pi);

		pi = this.shape.createTransformedShape(new Transform2D()).getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 5.1, 8);
		assertElement(pi, PathElementType.LINE_TO, 9.9, 8);
		assertElement(pi, PathElementType.CURVE_TO, 9.95523, 8, 10, 8.08954, 10, 8.2);
		assertElement(pi, PathElementType.LINE_TO, 10, 17.8);
		assertElement(pi, PathElementType.CURVE_TO, 10, 17.91046, 9.95523, 18, 9.9, 18);
		assertElement(pi, PathElementType.LINE_TO, 5.1, 18);
		assertElement(pi, PathElementType.CURVE_TO, 5.04477, 18, 5, 17.91046, 5, 17.8);
		assertElement(pi, PathElementType.LINE_TO, 5, 8.2);
		assertElement(pi, PathElementType.CURVE_TO, 5, 8.08954, 5.04477, 8, 5.1, 8);
		assertElement(pi, PathElementType.CLOSE, 5.1, 8);
		assertNoElement(pi);

		Transform2D tr = new Transform2D();
		tr.setTranslation(10, -1);
		pi = this.shape.createTransformedShape(tr).getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 15.1, 7);
		assertElement(pi, PathElementType.LINE_TO, 19.9, 7);
		assertElement(pi, PathElementType.CURVE_TO, 19.95523, 7, 20, 7.08954, 20, 7.2);
		assertElement(pi, PathElementType.LINE_TO, 20, 16.8);
		assertElement(pi, PathElementType.CURVE_TO, 20, 16.91046, 19.95523, 17, 19.9, 17);
		assertElement(pi, PathElementType.LINE_TO, 15.1, 17);
		assertElement(pi, PathElementType.CURVE_TO, 15.04477, 17, 15, 16.91046, 15, 16.8);
		assertElement(pi, PathElementType.LINE_TO, 15, 7.2);
		assertElement(pi, PathElementType.CURVE_TO, 15, 7.08954, 15.04477, 7, 15.1, 7);
		assertElement(pi, PathElementType.CLOSE, 15.1, 7);
		assertNoElement(pi);
	}

	@Override
	public void containsRectangle2afp() {
		assertFalse(this.shape.contains(createRectangle(0, 0, 1, 1)));
		assertFalse(this.shape.contains(createRectangle(0, 0, 7, 10)));
		assertFalse(this.shape.contains(createRectangle(0, 0, 20, 20)));
		assertTrue(this.shape.contains(createRectangle(6, 10, 1, 1)));
		assertFalse(this.shape.contains(createRectangle(5, 8, 5, 10)));
		assertTrue(this.shape.contains(createRectangle(5.5, 8.5, 4, 9)));
	}

	@Override
	public void intersectsRectangle2afp() {
		assertFalse(this.shape.intersects(createRectangle(0, 0, 1, 1)));
		assertFalse(this.shape.intersects(createRectangle(20, 20, 21, 21)));
		assertTrue(this.shape.intersects(createRectangle(0, 0, 7, 12)));
		assertFalse(this.shape.intersects(createRectangle(0, 0, 7, 8)));
		assertFalse(this.shape.intersects(createRectangle(0, 0, 5.01, 8.01)));
		assertTrue(this.shape.intersects(createRectangle(0, 0, 5.05, 8.05)));
		assertTrue(this.shape.intersects(createRectangle(0, 0, 5.05, 8.1)));
		assertTrue(this.shape.intersects(createRectangle(0, 0, 5.05, 8.15)));
	}

	@Override
	public void intersectsCircle2afp() {
		assertFalse(this.shape.intersects(createCircle(7, 0, 1)));
		assertFalse(this.shape.intersects(createCircle(7, 20, 1)));
		assertFalse(this.shape.intersects(createCircle(0, 12, 1)));
		assertFalse(this.shape.intersects(createCircle(20, 12, 1)));

		assertFalse(this.shape.intersects(createCircle(0, 0, 1)));
		assertFalse(this.shape.intersects(createCircle(20, 0, 1)));
		assertFalse(this.shape.intersects(createCircle(20, 20, 1)));
		assertFalse(this.shape.intersects(createCircle(0, 20, 1)));

		assertFalse(this.shape.intersects(createCircle(4, 12, 1)));
		assertTrue(this.shape.intersects(createCircle(4.1, 12, 1)));
		assertTrue(this.shape.intersects(createCircle(6, 12, 1)));
		assertTrue(this.shape.intersects(createCircle(10.9, 12, 1)));
		assertFalse(this.shape.intersects(createCircle(11, 12, 1)));

		assertFalse(this.shape.intersects(createCircle(7, 7, 1)));
		assertTrue(this.shape.intersects(createCircle(7, 7.1, 1)));
		assertTrue(this.shape.intersects(createCircle(7, 12, 1)));
		assertTrue(this.shape.intersects(createCircle(7, 18.9, 1)));
		assertFalse(this.shape.intersects(createCircle(7, 19, 1)));

		assertFalse(this.shape.intersects(createCircle(4.32, 7.32, 1)));
		assertTrue(this.shape.intersects(createCircle(4.4, 7.4, 1)));
		assertTrue(this.shape.intersects(createCircle(4.75, 7.75, 1)));
		assertTrue(this.shape.intersects(createCircle(4.19, 7.55, 1)));
	}

	@Override
	public void intersectsEllipse2afp() {
		// Horizontal axis is major
		assertFalse(this.shape.intersects(createEllipse(6, -.5, 2, 1)));
		assertFalse(this.shape.intersects(createEllipse(6, 19.5, 2, 1)));
		assertFalse(this.shape.intersects(createEllipse(-1, 11.5, 2, 1)));
		assertFalse(this.shape.intersects(createEllipse(19, 11.5, 2, 1)));

		assertFalse(this.shape.intersects(createEllipse(-1, -.5, 2, 1)));
		assertFalse(this.shape.intersects(createEllipse(19, -.5, 2, 1)));
		assertFalse(this.shape.intersects(createEllipse(19, 19.5, 2, 1)));
		assertFalse(this.shape.intersects(createEllipse(-1, 19.5, 2, 1)));

		assertFalse(this.shape.intersects(createEllipse(3, 11.5, 2, 1)));
		assertTrue(this.shape.intersects(createEllipse(3.1, 11.5, 2, 1)));
		assertTrue(this.shape.intersects(createEllipse(5, 11.5, 2, 1)));
		assertTrue(this.shape.intersects(createEllipse(9.9, 11.5, 2, 1)));
		assertFalse(this.shape.intersects(createEllipse(10, 11.5, 2, 1)));

		assertFalse(this.shape.intersects(createEllipse(6, 6.5, 2, 1)));
		assertFalse(this.shape.intersects(createEllipse(6, 6.6, 2, 1)));
		assertTrue(this.shape.intersects(createEllipse(6, 11.5, 2, 1)));
		assertFalse(this.shape.intersects(createEllipse(6, 18.4, 2, 1)));
		assertFalse(this.shape.intersects(createEllipse(6, 18.5, 2, 1)));

		assertFalse(this.shape.intersects(createEllipse(3.32, 6.82, 2, 1)));
		assertFalse(this.shape.intersects(createEllipse(3.4, 6.9, 2, 1)));
		assertTrue(this.shape.intersects(createEllipse(3.75, 7.25, 2, 1)));
		assertFalse(this.shape.intersects(createEllipse(3.19, 7.05, 2, 1)));
		assertTrue(this.shape.intersects(createEllipse(3.08, 7.45, 2, 1)));
	}

	@Override
	public void intersectsSegment2afp() {
		assertFalse(this.shape.intersects(createSegment(0, 0, 1, 1)));
		assertFalse(this.shape.intersects(createSegment(20, 20, 21, 21)));
		assertTrue(this.shape.intersects(createSegment(0, 0, 7, 12)));
		assertFalse(this.shape.intersects(createSegment(0, 0, 7, 8)));
		assertFalse(this.shape.intersects(createSegment(6, 7, 4.1, 9)));
		assertTrue(this.shape.intersects(createSegment(6.1, 7, 4.1, 9)));
	}

	@Override
	public void intersectsTriangle2afp() {
		Triangle2afp triangle = createTriangle(5, 8, -10, 1, -1, -2);
		assertTrue(createRoundRectangle(0, 0, 1, 1, .2, .4).intersects(triangle));
		assertTrue(createRoundRectangle(0, 2, 1, 1, .2, .4).intersects(triangle));
		assertTrue(createRoundRectangle(0, 3, 1, 1, .2, .4).intersects(triangle));
		assertTrue(createRoundRectangle(0, 4, 1, 1, .2, .4).intersects(triangle));
		assertTrue(createRoundRectangle(0, 5, 1, 1, .2, .4).intersects(triangle));
		assertTrue(createRoundRectangle(0, 6, 1, 1, .2, .4).intersects(triangle));
		assertTrue(createRoundRectangle(0, 6.05, 1, 1, .2, .4).intersects(triangle));
		assertFalse(createRoundRectangle(0, 6.06, 1, 1, .2, .4).intersects(triangle));
		assertFalse(createRoundRectangle(4.5, 8, 1, 1, .2, .4).intersects(triangle));
	}

	@Override
	public void intersectsPath2afp() {
		Path2afp<?, ?, ?, ?, ?, B> p;
		
		p = createPath();
		p.moveTo(-20, -20);
		p.lineTo(-20, 20);
		p.lineTo(20, 20);
		p.lineTo(20, -20);
		assertFalse(this.shape.intersects(p));
		p.closePath();
		assertTrue(this.shape.intersects(p));
		
		p = createPath();
		p.moveTo(-20, -20);
		p.lineTo(5, 8);
		p.lineTo(-20, 20);
		assertFalse(this.shape.intersects(p));
		p.closePath();
		assertFalse(this.shape.intersects(p));

		p = createPath();
		p.moveTo(-20, -20);
		p.lineTo(30, 20);
		p.lineTo(-20, 20);
		assertFalse(this.shape.intersects(p));
		p.closePath();
		assertTrue(this.shape.intersects(p));

		p = createPath();
		p.moveTo(-20, -20);
		p.lineTo(-20, 20);
		p.lineTo(20, -20);
		assertFalse(this.shape.intersects(p));
		p.closePath();
		assertFalse(this.shape.intersects(p));

		p = createPath();
		p.moveTo(-20, 20);
		p.lineTo(10, 8);
		p.lineTo(20, 18);
		assertTrue(this.shape.intersects(p));
		p.closePath();
		assertTrue(this.shape.intersects(p));

		p = createPath();
		p.moveTo(-20, 20);
		p.lineTo(20, 18);
		p.lineTo(10, 8);
		assertFalse(this.shape.intersects(p));
		p.closePath();
		assertTrue(this.shape.intersects(p));
	}

	@Override
	public void intersectsPathIterator2afp() {
		Path2afp<?, ?, ?, ?, ?, B> p;
		
		p = createPath();
		p.moveTo(-20, -20);
		p.lineTo(-20, 20);
		p.lineTo(20, 20);
		p.lineTo(20, -20);
		assertFalse(this.shape.intersects(p.getPathIterator()));
		p.closePath();
		assertTrue(this.shape.intersects(p.getPathIterator()));
		
		p = createPath();
		p.moveTo(-20, -20);
		p.lineTo(5, 8);
		p.lineTo(-20, 20);
		assertFalse(this.shape.intersects(p.getPathIterator()));
		p.closePath();
		assertFalse(this.shape.intersects(p.getPathIterator()));

		p = createPath();
		p.moveTo(-20, -20);
		p.lineTo(30, 20);
		p.lineTo(-20, 20);
		assertFalse(this.shape.intersects(p.getPathIterator()));
		p.closePath();
		assertTrue(this.shape.intersects(p.getPathIterator()));

		p = createPath();
		p.moveTo(-20, -20);
		p.lineTo(-20, 20);
		p.lineTo(20, -20);
		assertFalse(this.shape.intersects(p.getPathIterator()));
		p.closePath();
		assertFalse(this.shape.intersects(p.getPathIterator()));

		p = createPath();
		p.moveTo(-20, 20);
		p.lineTo(10, 8);
		p.lineTo(20, 18);
		assertTrue(this.shape.intersects(p.getPathIterator()));
		p.closePath();
		assertTrue(this.shape.intersects(p.getPathIterator()));

		p = createPath();
		p.moveTo(-20, 20);
		p.lineTo(20, 18);
		p.lineTo(10, 8);
		assertFalse(this.shape.intersects(p.getPathIterator()));
		p.closePath();
		assertTrue(this.shape.intersects(p.getPathIterator()));
	}

	@Override
	public void intersectsOrientedRectangle2afp() {
		OrientedRectangle2afp rectangle = createOrientedRectangle(
				6, 9,
				0.894427190999916, -0.447213595499958, 13.999990000000002,
				12.999989999999997);
		assertTrue(createRoundRectangle(0, 0, 2, 1, .1, .05).intersects(rectangle));
		assertFalse(createRoundRectangle(-9, 15, 2, 1, .1, .05).intersects(rectangle));
		assertFalse(createRoundRectangle(-8.7, 15, 2, 1, .1, .05).intersects(rectangle));
		assertFalse(createRoundRectangle(-8.7, 15, 2, 1, .1, .05).intersects(rectangle));
		assertFalse(createRoundRectangle(-8.65, 15, 2, 1, .1, .05).intersects(rectangle));
		assertFalse(createRoundRectangle(-8.64, 15, 2, 1, .1, .05).intersects(rectangle));
		assertFalse(createRoundRectangle(-8.63, 15, 2, 1, .1, .05).intersects(rectangle));
		assertTrue(createRoundRectangle(-8.62, 15, 2, 1, .1, .05).intersects(rectangle));
		assertTrue(createRoundRectangle(-8, 15, 2, 1, .1, .05).intersects(rectangle));
		assertFalse(createRoundRectangle(10, 25, 2, 1, .1, .05).intersects(rectangle));
		assertFalse(createRoundRectangle(20, -5, 2, 1, .1, .05).intersects(rectangle));

	}

	@Test
	@Override
	public void intersectsParallelogram2afp() {
		Parallelogram2afp para = createParallelogram(
				6, 9,
				2.425356250363330e-01, 9.701425001453320e-01, 9.219544457292887,
				-7.071067811865475e-01, 7.071067811865475e-01, 1.264911064067352e+01);
		assertFalse(createRoundRectangle(0, 0, 1, 1, .1, .05).intersects(para));
		assertTrue(createRoundRectangle(0, 2, 1, 1, .1, .05).intersects(para));
		assertTrue(createRoundRectangle(-5.5, 8.5, 1, 1, .1, .05).intersects(para));
		assertFalse(createRoundRectangle(-6, 16, 1, 1, .1, .05).intersects(para));
		assertFalse(createRoundRectangle(146, 16, 1, 1, .1, .05).intersects(para));
		assertTrue(createRoundRectangle(12, 14, 1, 1, .1, .05).intersects(para));
		assertTrue(createRoundRectangle(0, 8, 1, 1, .1, .05).intersects(para));
		assertTrue(createRoundRectangle(10, -1, 1, 1, .1, .05).intersects(para));
		assertTrue(createRoundRectangle(-15, -10, 35, 40, .1, .05).intersects(para));
		assertFalse(createRoundRectangle(-4.79634, 14.50886, 1, 1, .1, .05).intersects(para));
	}

	@Override
	public void intersectsRoundRectangle2afp() {
		assertFalse(this.shape.intersects(createRoundRectangle(0, 0, 1, 1, .1, .2)));
		assertFalse(this.shape.intersects(createRoundRectangle(20, 20, 21, 21, .1, .2)));
		assertTrue(this.shape.intersects(createRoundRectangle(0, 0, 7, 12, .1, .2)));
		assertFalse(this.shape.intersects(createRoundRectangle(0, 0, 7, 8, .1, .2)));
		assertFalse(this.shape.intersects(createRoundRectangle(0, 0, 5.01, 8.01, .1, .2)));
		assertFalse(this.shape.intersects(createRoundRectangle(0, 0, 5.05, 8.05, .1, .2)));
		assertFalse(this.shape.intersects(createRoundRectangle(0, 0, 5.05, 8.1, .1, .2)));
		assertTrue(this.shape.intersects(createRoundRectangle(0, 0, 5.05, 8.15, .1, .2)));
	}

	@Override
	public void inflate() {
		this.shape.inflate(1, 2, 3, 4);
		assertEpsilonEquals(4, this.shape.getMinX());
		assertEpsilonEquals(6, this.shape.getMinY());
		assertEpsilonEquals(13, this.shape.getMaxX());
		assertEpsilonEquals(22, this.shape.getMaxY());
	}

	@Override
	public void intersectsShape2D() {
		assertTrue(this.shape.intersects((Shape2D) createCircle(4.1, 12, 1)));
		assertTrue(this.shape.intersects((Shape2D) createEllipse(5, 11.5, 2, 1)));
	}

	@Override
	public void operator_addVector2D() {
		this.shape.operator_add(createVector(123.456, 456.789));
		assertEpsilonEquals(128.456, this.shape.getMinX());
		assertEpsilonEquals(464.789, this.shape.getMinY());
		assertEpsilonEquals(133.456, this.shape.getMaxX());
		assertEpsilonEquals(474.789, this.shape.getMaxY());
	}

	@Override
	public void operator_plusVector2D() {
		T shape = this.shape.operator_plus(createVector(123.456, 456.789));
		assertEpsilonEquals(128.456, shape.getMinX());
		assertEpsilonEquals(464.789, shape.getMinY());
		assertEpsilonEquals(133.456, shape.getMaxX());
		assertEpsilonEquals(474.789, shape.getMaxY());
	}

	@Override
	public void operator_removeVector2D() {
		this.shape.operator_remove(createVector(123.456, 456.789));
		assertEpsilonEquals(-118.456, this.shape.getMinX());
		assertEpsilonEquals(-448.789, this.shape.getMinY());
		assertEpsilonEquals(-113.456, this.shape.getMaxX());
		assertEpsilonEquals(-438.789, this.shape.getMaxY());
	}

	@Override
	public void operator_minusVector2D() {
		T shape = this.shape.operator_minus(createVector(123.456, 456.789));
		assertEpsilonEquals(-118.456, shape.getMinX());
		assertEpsilonEquals(-448.789, shape.getMinY());
		assertEpsilonEquals(-113.456, shape.getMaxX());
		assertEpsilonEquals(-438.789, shape.getMaxY());
	}

	@Override
	public void operator_multiplyTransform2D() {
		PathIterator2afp pi;
		
		pi = this.shape.operator_multiply(null).getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 5.1, 8);
		assertElement(pi, PathElementType.LINE_TO, 9.9, 8);
		assertElement(pi, PathElementType.CURVE_TO, 9.95523, 8, 10, 8.08954, 10, 8.2);
		assertElement(pi, PathElementType.LINE_TO, 10, 17.8);
		assertElement(pi, PathElementType.CURVE_TO, 10, 17.91046, 9.95523, 18, 9.9, 18);
		assertElement(pi, PathElementType.LINE_TO, 5.1, 18);
		assertElement(pi, PathElementType.CURVE_TO, 5.04477, 18, 5, 17.91046, 5, 17.8);
		assertElement(pi, PathElementType.LINE_TO, 5, 8.2);
		assertElement(pi, PathElementType.CURVE_TO, 5, 8.08954, 5.04477, 8, 5.1, 8);
		assertElement(pi, PathElementType.CLOSE, 5.1, 8);
		assertNoElement(pi);

		pi = this.shape.operator_multiply(new Transform2D()).getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 5.1, 8);
		assertElement(pi, PathElementType.LINE_TO, 9.9, 8);
		assertElement(pi, PathElementType.CURVE_TO, 9.95523, 8, 10, 8.08954, 10, 8.2);
		assertElement(pi, PathElementType.LINE_TO, 10, 17.8);
		assertElement(pi, PathElementType.CURVE_TO, 10, 17.91046, 9.95523, 18, 9.9, 18);
		assertElement(pi, PathElementType.LINE_TO, 5.1, 18);
		assertElement(pi, PathElementType.CURVE_TO, 5.04477, 18, 5, 17.91046, 5, 17.8);
		assertElement(pi, PathElementType.LINE_TO, 5, 8.2);
		assertElement(pi, PathElementType.CURVE_TO, 5, 8.08954, 5.04477, 8, 5.1, 8);
		assertElement(pi, PathElementType.CLOSE, 5.1, 8);
		assertNoElement(pi);

		Transform2D tr = new Transform2D();
		tr.setTranslation(10, -1);
		pi = this.shape.operator_multiply(tr).getPathIterator();
		assertElement(pi, PathElementType.MOVE_TO, 15.1, 7);
		assertElement(pi, PathElementType.LINE_TO, 19.9, 7);
		assertElement(pi, PathElementType.CURVE_TO, 19.95523, 7, 20, 7.08954, 20, 7.2);
		assertElement(pi, PathElementType.LINE_TO, 20, 16.8);
		assertElement(pi, PathElementType.CURVE_TO, 20, 16.91046, 19.95523, 17, 19.9, 17);
		assertElement(pi, PathElementType.LINE_TO, 15.1, 17);
		assertElement(pi, PathElementType.CURVE_TO, 15.04477, 17, 15, 16.91046, 15, 16.8);
		assertElement(pi, PathElementType.LINE_TO, 15, 7.2);
		assertElement(pi, PathElementType.CURVE_TO, 15, 7.08954, 15.04477, 7, 15.1, 7);
		assertElement(pi, PathElementType.CLOSE, 15.1, 7);
		assertNoElement(pi);
	}

	@Override
	public void operator_andPoint2D() {
		assertFalse(this.shape.operator_and(createPoint(0, 0)));
		assertFalse(this.shape.operator_and(createPoint(20, 0)));
		assertFalse(this.shape.operator_and(createPoint(20, 20)));
		assertFalse(this.shape.operator_and(createPoint(0, 20)));
		assertTrue(this.shape.operator_and(createPoint(8, 13)));
		assertTrue(this.shape.operator_and(createPoint(5, 13)));
		assertFalse(this.shape.operator_and(createPoint(4.999, 13)));
		assertFalse(this.shape.operator_and(createPoint(5, 8)));
		assertFalse(this.shape.operator_and(createPoint(5, 18)));
		assertFalse(this.shape.operator_and(createPoint(15, 18)));
		assertFalse(this.shape.operator_and(createPoint(15, 8)));
	}

	@Override
	public void operator_andShape2D() {
		assertTrue(this.shape.operator_and(createCircle(4.1, 12, 1)));
		assertTrue(this.shape.operator_and(createEllipse(5, 11.5, 2, 1)));
	}

	@Override
	public void operator_upToPoint2D() {
		assertEpsilonEquals(9.47905, this.shape.operator_upTo(createPoint(0, 0)));
		assertEpsilonEquals(12.86194, this.shape.operator_upTo(createPoint(20, 0)));
		assertEpsilonEquals(10.23041, this.shape.operator_upTo(createPoint(20, 20)));
		assertEpsilonEquals(5.43372, this.shape.operator_upTo(createPoint(0, 20)));
		assertEpsilonEquals(5, this.shape.operator_upTo(createPoint(0, 11)));
		assertEpsilonEquals(10, this.shape.operator_upTo(createPoint(20, 11)));
		assertEpsilonEquals(8, this.shape.operator_upTo(createPoint(7, 0)));
		assertEpsilonEquals(2, this.shape.operator_upTo(createPoint(7, 20)));
		assertEpsilonEquals(5, this.shape.operator_upTo(createPoint(0, 8.2)));
		assertEpsilonEquals(8, this.shape.operator_upTo(createPoint(5.1, 0)));
		assertEpsilonEquals(0, this.shape.operator_upTo(createPoint(7, 10)));
	}

}