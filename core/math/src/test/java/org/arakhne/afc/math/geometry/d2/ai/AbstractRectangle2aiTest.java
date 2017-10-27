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

package org.arakhne.afc.math.geometry.d2.ai;

import java.util.Iterator;

import org.junit.Test;

import org.arakhne.afc.math.MathConstants;
import org.arakhne.afc.math.MathUtil;
import org.arakhne.afc.math.geometry.PathElementType;
import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.Shape2D;
import org.arakhne.afc.math.geometry.d2.Transform2D;
import org.arakhne.afc.math.geometry.d2.ai.Rectangle2ai.Side;

@SuppressWarnings("all")
public abstract class AbstractRectangle2aiTest<T extends Rectangle2ai<?, T, ?, ?, ?, T>>
        extends AbstractRectangularShape2aiTest<T, T> {

    @Override
    protected final T createShape() {
        return createRectangle(5, 8, 10, 5);
    }

    @Test
    public void staticReducesCohenSutherlandZoneRectangleSegment() {
        Point2D p1 = createPoint(0, 0);
        Point2D p2 = createPoint(0, 0);

        assertEquals(0,
                Rectangle2ai.reducesCohenSutherlandZoneRectangleSegment(10, 12, 40, 37, 20, 45, 43, 15,
                        MathUtil.getCohenSutherlandCode(20, 45, 0, 12, 40, 37),
                        MathUtil.getCohenSutherlandCode(43, 15, 0, 12, 40, 37),
                        p1, p2));
        assertIntPointEquals(26, 37, p1);
        assertIntPointEquals(40, 19, p2);

        assertEquals(0, 
                Rectangle2ai.reducesCohenSutherlandZoneRectangleSegment(10, 12, 40, 37, 20, 55, 43, 15,
                        MathUtil.getCohenSutherlandCode(20, 55, 0, 12, 40, 37),
                        MathUtil.getCohenSutherlandCode(43, 15, 0, 12, 40, 37),
                        p1, p2));
        assertIntPointEquals(30, 37, p1);
        assertIntPointEquals(40, 21, p2);
    }

    @Test
    @Override
    public void testClone() {
        T clone = this.shape.clone();
        assertNotNull(clone);
        assertNotSame(this.shape, clone);
        assertEquals(this.shape.getClass(), clone.getClass());
        assertEpsilonEquals(5, clone.getMinX());
        assertEpsilonEquals(8, clone.getMinY());
        assertEpsilonEquals(15, clone.getMaxX());
        assertEpsilonEquals(13, clone.getMaxY());
    }

    @Test
    @Override
    public void equalsObject() {
        assertFalse(this.shape.equals(null));
        assertFalse(this.shape.equals(new Object()));
        assertFalse(this.shape.equals(createRectangle(0, 0, 5, 5)));
        assertFalse(this.shape.equals(createRectangle(5, 8, 10, 6)));
        assertFalse(this.shape.equals(createSegment(5, 8, 10, 5)));
        assertTrue(this.shape.equals(this.shape));
        assertTrue(this.shape.equals(createRectangle(5, 8, 10, 5)));
    }

    @Test
    @Override
    public void equalsObject_withPathIterator() {
        assertFalse(this.shape.equals(createRectangle(0, 0, 5, 5).getPathIterator()));
        assertFalse(this.shape.equals(createRectangle(5, 8, 10, 6).getPathIterator()));
        assertFalse(this.shape.equals(createSegment(5, 8, 10, 5).getPathIterator()));
        assertTrue(this.shape.equals(this.shape.getPathIterator()));
        assertTrue(this.shape.equals(createRectangle(5, 8, 10, 5).getPathIterator()));
    }

    @Test
    @Override
    public void equalsToShape() {
        assertFalse(this.shape.equalsToShape(null));
        assertFalse(this.shape.equalsToShape(createRectangle(0, 0, 5, 5)));
        assertFalse(this.shape.equalsToShape(createRectangle(5, 8, 10, 6)));
        assertTrue(this.shape.equalsToShape(this.shape));
        assertTrue(this.shape.equalsToShape(createRectangle(5, 8, 10, 5)));
    }

    @Test
    @Override
    public void equalsToPathIterator() {
        assertFalse(this.shape.equalsToPathIterator((PathIterator2ai) null));
        assertFalse(this.shape.equalsToPathIterator(createRectangle(0, 0, 5, 5).getPathIterator()));
        assertFalse(this.shape.equalsToPathIterator(createRectangle(5, 8, 10, 6).getPathIterator()));
        assertFalse(this.shape.equalsToPathIterator(createSegment(5, 8, 10, 5).getPathIterator()));
        assertTrue(this.shape.equalsToPathIterator(this.shape.getPathIterator()));
        assertTrue(this.shape.equalsToPathIterator(createRectangle(5, 8, 10, 5).getPathIterator()));
    }

    @Override
	@Test
    public void getPointIterator() {
        Iterator<? extends Point2D> iterator = this.shape.getPointIterator();
        Point2D p;

        int[] coords;

        coords = new int[] {5,6,7,8,9,10,11,12,13,14,15};
        for(int x : coords) {
            assertTrue(iterator.hasNext());
            p = iterator.next();
            assertNotNull(p);
            assertEquals(x, p.ix());
            assertEquals(8, p.iy());
        }

        coords = new int[] {9,10,11,12,13};
        for(int y : coords) {
            assertTrue(iterator.hasNext());
            p = iterator.next();
            assertNotNull(p);
            assertEquals(15, p.ix());
            assertEquals(y, p.iy());
        }

        coords = new int[] {14,13,12,11,10,9,8,7,6,5};
        for(int x : coords) {
            assertTrue(iterator.hasNext());
            p = iterator.next();
            assertNotNull(p);
            assertEquals(x, p.ix());
            assertEquals(13, p.iy());
        }

        coords = new int[] {12,11,10,9};
        for(int y : coords) {
            assertTrue(iterator.hasNext());
            p = iterator.next();
            assertNotNull(p);
            assertEquals(5, p.ix());
            assertEquals(y, p.iy());
        }

        assertFalse(iterator.hasNext());
    }

    @Test
    public void getPointIteratorSide_Top() {
        Iterator<? extends Point2D> iterator = this.shape.getPointIterator(Side.TOP);
        Point2D p;

        int[] coords;

        coords = new int[] {5,6,7,8,9,10,11,12,13,14,15};
        for(int x : coords) {
            assertTrue(iterator.hasNext());
            p = iterator.next();
            assertNotNull(p);
            assertEquals(x, p.ix());
            assertEquals(8, p.iy());
        }

        coords = new int[] {9,10,11,12,13};
        for(int y : coords) {
            assertTrue(iterator.hasNext());
            p = iterator.next();
            assertNotNull(p);
            assertEquals(15, p.ix());
            assertEquals(y, p.iy());
        }

        coords = new int[] {14,13,12,11,10,9,8,7,6,5};
        for(int x : coords) {
            assertTrue(iterator.hasNext());
            p = iterator.next();
            assertNotNull(p);
            assertEquals(x, p.ix());
            assertEquals(13, p.iy());
        }

        coords = new int[] {12,11,10,9};
        for(int y : coords) {
            assertTrue(iterator.hasNext());
            p = iterator.next();
            assertNotNull(p);
            assertEquals(5, p.ix());
            assertEquals(y, p.iy());
        }

        assertFalse(iterator.hasNext());
    }

    @Test
    public void getPointIteratorSide_Right() {
        Iterator<? extends Point2D> iterator = this.shape.getPointIterator(Side.RIGHT);
        Point2D p;

        int[] coords;

        coords = new int[] {9,10,11,12,13};
        for(int y : coords) {
            assertTrue(iterator.hasNext());
            p = iterator.next();
            assertNotNull(p);
            assertEquals(15, p.ix());
            assertEquals(y, p.iy());
        }

        coords = new int[] {14,13,12,11,10,9,8,7,6,5};
        for(int x : coords) {
            assertTrue(iterator.hasNext());
            p = iterator.next();
            assertNotNull(p);
            assertEquals(x, p.ix());
            assertEquals(13, p.iy());
        }

        coords = new int[] {12,11,10,9};
        for(int y : coords) {
            assertTrue(iterator.hasNext());
            p = iterator.next();
            assertNotNull(p);
            assertEquals(5, p.ix());
            assertEquals(y, p.iy());
        }

        coords = new int[] {5,6,7,8,9,10,11,12,13,14,15};
        for(int x : coords) {
            assertTrue(iterator.hasNext());
            p = iterator.next();
            assertNotNull(p);
            assertEquals(x, p.ix());
            assertEquals(8, p.iy());
        }

        assertFalse(iterator.hasNext());
    }

    @Test
    public void getPointIteratorSide_Bottom() {
        Iterator<? extends Point2D> iterator = this.shape.getPointIterator(Side.BOTTOM);
        Point2D p;

        int[] coords;

        coords = new int[] {14,13,12,11,10,9,8,7,6,5};
        for(int x : coords) {
            assertTrue(iterator.hasNext());
            p = iterator.next();
            assertNotNull(p);
            assertEquals(x, p.ix());
            assertEquals(13, p.iy());
        }

        coords = new int[] {12,11,10,9};
        for(int y : coords) {
            assertTrue(iterator.hasNext());
            p = iterator.next();
            assertNotNull(p);
            assertEquals(5, p.ix());
            assertEquals(y, p.iy());
        }

        coords = new int[] {5,6,7,8,9,10,11,12,13,14,15};
        for(int x : coords) {
            assertTrue(iterator.hasNext());
            p = iterator.next();
            assertNotNull(p);
            assertEquals(x, p.ix());
            assertEquals(8, p.iy());
        }

        coords = new int[] {9,10,11,12,13};
        for(int y : coords) {
            assertTrue(iterator.hasNext());
            p = iterator.next();
            assertNotNull(p);
            assertEquals(15, p.ix());
            assertEquals(y, p.iy());
        }

        assertFalse(iterator.hasNext());
    }

    @Test
    public void getPointIteratorSide_Left() {
        Iterator<? extends Point2D> iterator = this.shape.getPointIterator(Side.LEFT);
        Point2D p;

        int[] coords;

        coords = new int[] {12,11,10,9};
        for(int y : coords) {
            assertTrue(iterator.hasNext());
            p = iterator.next();
            assertNotNull(p);
            assertEquals(5, p.ix());
            assertEquals(y, p.iy());
        }

        coords = new int[] {5,6,7,8,9,10,11,12,13,14,15};
        for(int x : coords) {
            assertTrue(iterator.hasNext());
            p = iterator.next();
            assertNotNull(p);
            assertEquals(x, p.ix());
            assertEquals(8, p.iy());
        }

        coords = new int[] {9,10,11,12,13};
        for(int y : coords) {
            assertTrue(iterator.hasNext());
            p = iterator.next();
            assertNotNull(p);
            assertEquals(15, p.ix());
            assertEquals(y, p.iy());
        }

        coords = new int[] {14,13,12,11,10,9,8,7,6,5};
        for(int x : coords) {
            assertTrue(iterator.hasNext());
            p = iterator.next();
            assertNotNull(p);
            assertEquals(x, p.ix());
            assertEquals(13, p.iy());
        }

        assertFalse(iterator.hasNext());
    }

    @Test
    @Override
    public void getDistance() {
        assertEpsilonEquals(0f, this.shape.getDistance(createPoint(5,8)));
        assertEpsilonEquals(0f, this.shape.getDistance(createPoint(10,10)));
        assertEpsilonEquals(1f, this.shape.getDistance(createPoint(4,8)));
        assertEpsilonEquals(9.433981132f, this.shape.getDistance(createPoint(0,0)));
    }

    @Test
    @Override
    public void getDistanceSquared() {
        assertEpsilonEquals(0f, this.shape.getDistanceSquared(createPoint(5,8)));
        assertEpsilonEquals(0f, this.shape.getDistanceSquared(createPoint(10,10)));
        assertEpsilonEquals(1f, this.shape.getDistanceSquared(createPoint(4,8)));
        assertEpsilonEquals(89f, this.shape.getDistanceSquared(createPoint(0,0)));
    }

    @Test
    @Override
    public void getDistanceL1() {
        assertEpsilonEquals(0f, this.shape.getDistanceL1(createPoint(5,8)));
        assertEpsilonEquals(0f, this.shape.getDistanceL1(createPoint(10,10)));
        assertEpsilonEquals(1f, this.shape.getDistanceL1(createPoint(4,8)));
        assertEpsilonEquals(13f, this.shape.getDistanceL1(createPoint(0,0)));
    }

    @Test
    @Override
    public void getDistanceLinf() {
        assertEpsilonEquals(0f, this.shape.getDistanceLinf(createPoint(5,8)));
        assertEpsilonEquals(0f, this.shape.getDistanceLinf(createPoint(10,10)));
        assertEpsilonEquals(1f, this.shape.getDistanceLinf(createPoint(4,8)));
        assertEpsilonEquals(8f, this.shape.getDistanceLinf(createPoint(0,0)));
    }

    @Test
    @Override
    public void getClosestPointTo() {
        Point2D p;

        p = this.shape.getClosestPointTo(createPoint(5,8));
        assertNotNull(p);
        assertEquals(5, p.ix());
        assertEquals(8, p.iy());

        p = this.shape.getClosestPointTo(createPoint(10,10));
        assertNotNull(p);
        assertEquals(10, p.ix());
        assertEquals(10, p.iy());

        p = this.shape.getClosestPointTo(createPoint(4,8));
        assertNotNull(p);
        assertEquals(5, p.ix());
        assertEquals(8, p.iy());

        p = this.shape.getClosestPointTo(createPoint(0,0));
        assertNotNull(p);
        assertEquals(5, p.ix());
        assertEquals(8, p.iy());
    }

    @Test
    @Override
    public void getFarthestPointTo() {
        Point2D p;

        p = this.shape.getFarthestPointTo(createPoint(5,8));
        assertNotNull(p);
        assertEquals(15, p.ix());
        assertEquals(13, p.iy());

        p = this.shape.getFarthestPointTo(createPoint(10,10));
        assertNotNull(p);
        assertEquals(15, p.ix());
        assertEquals(13, p.iy());

        p = this.shape.getFarthestPointTo(createPoint(4,8));
        assertNotNull(p);
        assertEquals(15, p.ix());
        assertEquals(13, p.iy());

        p = this.shape.getFarthestPointTo(createPoint(0,0));
        assertNotNull(p);
        assertEquals(15, p.ix());
        assertEquals(13, p.iy());

        p = this.shape.getFarthestPointTo(createPoint(24,0));
        assertNotNull(p);
        assertEquals(5, p.ix());
        assertEquals(13, p.iy());

        p = this.shape.getFarthestPointTo(createPoint(0,32));
        assertNotNull(p);
        assertEquals(15, p.ix());
        assertEquals(8, p.iy());
    }

    @Test
    @Override
    public void containsIntInt() {
        assertFalse(this.shape.contains(0,0));
        assertTrue(this.shape.contains(11,10));
        assertFalse(this.shape.contains(11,50));
    }

    @Test
    @Override
    public void containsRectangle2ai() {
        assertFalse(this.shape.contains(createRectangle(0,0,1,1)));
        assertFalse(this.shape.contains(createRectangle(0,0,8,1)));
        assertFalse(this.shape.contains(createRectangle(0,0,8,6)));
        assertFalse(this.shape.contains(createRectangle(0,0,100,100)));
        assertTrue(this.shape.contains(createRectangle(7,10,1,1)));
        assertFalse(this.shape.contains(createRectangle(16,0,100,100)));
    }

    @Test
    @Override
    public void containsShape2D() {
        assertFalse(this.shape.contains(createCircle(0,0,1)));
        assertFalse(this.shape.contains(createCircle(0,0,8)));
        assertFalse(this.shape.contains(createCircle(0,0,6)));
        assertFalse(this.shape.contains(createCircle(0,0,100)));
        assertTrue(this.shape.contains(createCircle(7,10,1)));
        assertFalse(this.shape.contains(createCircle(16,0,100)));
    }

    @Test
    @Override
    public void intersectsRectangle2ai() {
        assertFalse(this.shape.intersects(createRectangle(0,0,1,1)));
        assertFalse(this.shape.intersects(createRectangle(0,0,8,1)));
        assertFalse(this.shape.intersects(createRectangle(0,0,8,6)));
        assertTrue(this.shape.intersects(createRectangle(0,0,100,100)));
        assertTrue(this.shape.intersects(createRectangle(7,10,1,1)));
        assertFalse(this.shape.intersects(createRectangle(16,0,100,100)));
    }

    @Override
    public void intersectsCircle2ai() {
        assertFalse(this.shape.intersects(createCircle(0,0,1)));
        assertFalse(this.shape.intersects(createCircle(0,0,8)));
        assertTrue(this.shape.intersects(createCircle(0,0,100)));
        assertTrue(this.shape.intersects(createCircle(7,10,1)));
        assertFalse(this.shape.intersects(createCircle(16,0,5)));
        assertFalse(this.shape.intersects(createCircle(5,15,1)));
    }

    @Test
    @Override
    public void intersectsSegment2ai() {
        assertFalse(this.shape.intersects(createSegment(0,0,1,1)));
        assertFalse(this.shape.intersects(createSegment(0,0,8,1)));
        assertFalse(this.shape.intersects(createSegment(0,0,8,6)));
        assertTrue(this.shape.intersects(createSegment(0,0,100,100)));
        assertTrue(this.shape.intersects(createSegment(7,10,1,1)));
        assertFalse(this.shape.intersects(createSegment(16,0,100,100)));
    }

    @Override
	@Test
    public void intersectsPath2ai() {
        Path2ai path = createPath();
        path.moveTo(0, 0);
        path.lineTo(2, 2);
        path.quadTo(3, 0, 4, 3);
        path.curveTo(5, -1, 6, 5, 7, -5);
        path.closePath();
        this.shape = createRectangle(0, 0, 1, 1);
        assertTrue(this.shape.intersects(path));
        this.shape = createRectangle(4, 3, 1, 1);
        assertTrue(this.shape.intersects(path));
        this.shape = createRectangle(2, 2, 1, 1);
        assertTrue(this.shape.intersects(path));
        this.shape = createRectangle(2, 1, 1, 1);
        assertTrue(this.shape.intersects(path));
        this.shape = createRectangle(3, 0, 1, 1);
        assertTrue(this.shape.intersects(path));
        this.shape = createRectangle(-1, -1, 1, 1);
        assertTrue(this.shape.intersects(path));
        this.shape = createRectangle(4, -3, 1, 1);
        assertTrue(this.shape.intersects(path));
        this.shape = createRectangle(-3, 4, 1, 1);
        assertFalse(this.shape.intersects(path));
        this.shape = createRectangle(6, -5, 1, 1);
        assertTrue(this.shape.intersects(path));
        this.shape = createRectangle(4, 0, 1, 1);
        assertTrue(this.shape.intersects(path));
        this.shape = createRectangle(5, 0, 1, 1);
        assertTrue(this.shape.intersects(path));
        this.shape = createRectangle(0, -3, 1, 1);
        assertFalse(this.shape.intersects(path));
        this.shape = createRectangle(0, -3, 2, 1);
        assertFalse(this.shape.intersects(path));
    }

    @Test
    @Override
    public void getPathIterator() {
        PathIterator2ai pi = this.shape.getPathIterator();
        assertElement(pi, PathElementType.MOVE_TO, 5,8);
        assertElement(pi, PathElementType.LINE_TO, 15,8);
        assertElement(pi, PathElementType.LINE_TO, 15,13);
        assertElement(pi, PathElementType.LINE_TO, 5,13);
        assertElement(pi, PathElementType.CLOSE, 5,8);
        assertNoElement(pi);
    }

    @Test
    @Override
    public void getPathIteratorTransform2D() {
        Transform2D tr;
        PathIterator2ai pi;

        tr = new Transform2D();
        pi = this.shape.getPathIterator(tr);
        assertElement(pi, PathElementType.MOVE_TO, 5,8);
        assertElement(pi, PathElementType.LINE_TO, 15,8);
        assertElement(pi, PathElementType.LINE_TO, 15,13);
        assertElement(pi, PathElementType.LINE_TO, 5,13);
        assertElement(pi, PathElementType.CLOSE, 5,8);
        assertNoElement(pi);

        tr = new Transform2D();
        tr.makeTranslationMatrix(3.4f, 4.5f);
        pi = this.shape.getPathIterator(tr);
        assertElement(pi, PathElementType.MOVE_TO, 8,13);
        assertElement(pi, PathElementType.LINE_TO, 18,13);
        assertElement(pi, PathElementType.LINE_TO, 18,18);
        assertElement(pi, PathElementType.LINE_TO, 8,18);
        assertElement(pi, PathElementType.CLOSE, 8,13);
        assertNoElement(pi);

        tr = new Transform2D();
        tr.makeRotationMatrix(MathConstants.QUARTER_PI);

        pi = this.shape.getPathIterator(tr);
        assertElement(pi, PathElementType.MOVE_TO, -2,9);
        assertElement(pi, PathElementType.LINE_TO, 5,16);
        assertElement(pi, PathElementType.LINE_TO, 1,20);
        assertElement(pi, PathElementType.LINE_TO, -6,13);
        assertElement(pi, PathElementType.CLOSE, -2,9);
        assertNoElement(pi);
    }

    @Test
    @Override
    public void createTransformedShape() {
        Transform2D tr;
        PathIterator2ai pi;

        tr = new Transform2D();
        pi = this.shape.createTransformedShape(tr).getPathIterator();
        assertElement(pi, PathElementType.MOVE_TO, 5,8);
        assertElement(pi, PathElementType.LINE_TO, 15,8);
        assertElement(pi, PathElementType.LINE_TO, 15,13);
        assertElement(pi, PathElementType.LINE_TO, 5,13);
        assertElement(pi, PathElementType.CLOSE, 5,8);
        assertNoElement(pi);

        tr = new Transform2D();
        tr.makeTranslationMatrix(3.4f, 4.5f);
        pi = this.shape.createTransformedShape(tr).getPathIterator();
        assertElement(pi, PathElementType.MOVE_TO, 8,13);
        assertElement(pi, PathElementType.LINE_TO, 18,13);
        assertElement(pi, PathElementType.LINE_TO, 18,18);
        assertElement(pi, PathElementType.LINE_TO, 8,18);
        assertElement(pi, PathElementType.CLOSE, 8,13);
        assertNoElement(pi);

        tr = new Transform2D();
        tr.makeRotationMatrix(MathConstants.QUARTER_PI);		
        pi = this.shape.createTransformedShape(tr).getPathIterator();
        assertElement(pi, PathElementType.MOVE_TO, -2,9);
        assertElement(pi, PathElementType.LINE_TO, 5,16);
        assertElement(pi, PathElementType.LINE_TO, 1,20);
        assertElement(pi, PathElementType.LINE_TO, -6,13);
        assertElement(pi, PathElementType.CLOSE, -2,9);
        assertNoElement(pi);
    }

    @Test
    @Override
    public void setIT() {
        this.shape.set(createRectangle(10, 12, 14, 16));
        assertEquals(10, this.shape.getMinX());
        assertEquals(12, this.shape.getMinY());
        assertEquals(24, this.shape.getMaxX());
        assertEquals(28, this.shape.getMaxY());
    }

    @Test
    @Override
    public void containsPoint2D() {
        assertFalse(this.shape.contains(createPoint(0,0)));
        assertTrue(this.shape.contains(createPoint(11,10)));
        assertFalse(this.shape.contains(createPoint(11,50)));
    }

    @Test
    @Override
    public void translateVector2D() {
        this.shape.translate(createVector(3, 4));
        assertEquals(8, this.shape.getMinX());
        assertEquals(12, this.shape.getMinY());
        assertEquals(18, this.shape.getMaxX());
        assertEquals(17, this.shape.getMaxY());
    }

    @Test
    public void staticFindsClosestPointRectanglePoint() {
        Point2D p;

        p = createPoint(0, 0);
        Rectangle2ai.findsClosestPointRectanglePoint(5, 8, 15, 13, 5, 8, p);
        assertNotNull(p);
        assertEquals(5, p.ix());
        assertEquals(8, p.iy());

        p = createPoint(0, 0);
        Rectangle2ai.findsClosestPointRectanglePoint(5, 8, 15, 13, 10, 10, p);
        assertNotNull(p);
        assertEquals(10, p.ix());
        assertEquals(10, p.iy());

        p = createPoint(0, 0);
        Rectangle2ai.findsClosestPointRectanglePoint(5, 8, 15, 13, 4, 8, p);
        assertNotNull(p);
        assertEquals(5, p.ix());
        assertEquals(8, p.iy());

        p = createPoint(0, 0);
        Rectangle2ai.findsClosestPointRectanglePoint(5, 8, 15, 13, 0, 0, p);
        assertNotNull(p);
        assertEquals(5, p.ix());
        assertEquals(8, p.iy());
    }

    @Test
    public void staticFindsClosestPointRectangleRectangle() {
        Point2D p;

        p = createPoint(0, 0);
        Rectangle2ai.findsClosestPointRectangleRectangle(5, 8, 15, 13, 5, 8, 7, 9, p);
        assertNotNull(p);
        assertEquals(6, p.ix());
        assertEquals(8, p.iy());

        p = createPoint(0, 0);
        Rectangle2ai.findsClosestPointRectangleRectangle(5, 8, 15, 13, 10, 10, 12, 12, p);
        assertNotNull(p);
        assertEquals(11, p.ix());
        assertEquals(11, p.iy());

        p = createPoint(0, 0);
        Rectangle2ai.findsClosestPointRectangleRectangle(5, 8, 15, 13, 4, 8, 6, 10, p);
        assertNotNull(p);
        assertEquals(5, p.ix());
        assertEquals(9, p.iy());

        p = createPoint(0, 0);
        Rectangle2ai.findsClosestPointRectangleRectangle(5, 8, 15, 13, 0, 0, 2, 2, p);
        assertNotNull(p);
        assertEquals(5, p.ix());
        assertEquals(8, p.iy());

        p = createPoint(0, 0);
        Rectangle2ai.findsClosestPointRectangleRectangle(5, 8, 15, 13, 7, 20, 50, 32, p);
        assertNotNull(p);
        assertEquals(15, p.ix());
        assertEquals(13, p.iy());
    }

    @Test
    public void staticFindsClosestPointRectangleSegment() {
        Point2D p;

        p = createPoint(0, 0);
        Rectangle2ai.findsClosestPointRectangleSegment(5, 8, 15, 13, 5, 8, 7, 9, p);
        assertNotNull(p);
        assertEquals(5, p.ix());
        assertEquals(8, p.iy());

        p = createPoint(0, 0);
        Rectangle2ai.findsClosestPointRectangleSegment(5, 8, 15, 13, 10, 10, 12, 12, p);
        assertNotNull(p);
        assertEquals(10, p.ix());
        assertEquals(10, p.iy());

        p = createPoint(0, 0);
        Rectangle2ai.findsClosestPointRectangleSegment(5, 8, 15, 13, 4, 8, 6, 10, p);
        assertNotNull(p);
        assertEquals(5, p.ix());
        assertEquals(9, p.iy());

        p = createPoint(0, 0);
        Rectangle2ai.findsClosestPointRectangleSegment(5, 8, 15, 13, 0, 0, 2, 2, p);
        assertNotNull(p);
        assertEquals(5, p.ix());
        assertEquals(8, p.iy());

        p = createPoint(0, 0);
        Rectangle2ai.findsClosestPointRectangleSegment(5, 8, 15, 13, 7, 20, 50, 32, p);
        assertNotNull(p);
        assertEquals(7, p.ix());
        assertEquals(13, p.iy());
    }

    @Test
    public void staticFindsFarthestPointRectanglePoint() {
        Point2D p;

        p = createPoint(0, 0);
        Rectangle2ai.findsFarthestPointRectanglePoint(5, 8, 15, 13, 5, 8, p);
        assertNotNull(p);
        assertEquals(15, p.ix());
        assertEquals(13, p.iy());

        p = createPoint(0, 0);
        Rectangle2ai.findsFarthestPointRectanglePoint(5, 8, 15, 13, 10, 10, p);
        assertNotNull(p);
        assertEquals(15, p.ix());
        assertEquals(13, p.iy());

        p = createPoint(0, 0);
        Rectangle2ai.findsFarthestPointRectanglePoint(5, 8, 15, 13, 4, 8, p);
        assertNotNull(p);
        assertEquals(15, p.ix());
        assertEquals(13, p.iy());

        p = createPoint(0, 0);
        Rectangle2ai.findsFarthestPointRectanglePoint(5, 8, 15, 13, 0, 0, p);
        assertNotNull(p);
        assertEquals(15, p.ix());
        assertEquals(13, p.iy());

        p = createPoint(0, 0);
        Rectangle2ai.findsFarthestPointRectanglePoint(5, 8, 15, 13, 24, 0, p);
        assertNotNull(p);
        assertEquals(5, p.ix());
        assertEquals(13, p.iy());

        p = createPoint(0, 0);
        Rectangle2ai.findsFarthestPointRectanglePoint(5, 8, 15, 13, 0, 32, p);
        assertNotNull(p);
        assertEquals(15, p.ix());
        assertEquals(8, p.iy());
    }

    @Test
    public void staticIntersectsRectangleRectangle() {
        assertFalse(Rectangle2ai.intersectsRectangleRectangle(5, 8, 15, 13, 0, 0, 1, 1));
        assertFalse(Rectangle2ai.intersectsRectangleRectangle(5, 8, 15, 13, 0, 0, 8, 1));
        assertFalse(Rectangle2ai.intersectsRectangleRectangle(5, 8, 15, 13, 0, 0, 8, 6));
        assertTrue(Rectangle2ai.intersectsRectangleRectangle(5, 8, 15, 13, 0, 0, 100, 100));
        assertTrue(Rectangle2ai.intersectsRectangleRectangle(5, 8, 15, 13, 7, 10, 8, 11));
        assertFalse(Rectangle2ai.intersectsRectangleRectangle(5, 8, 15, 13, 16, 0,116, 100));
    }

    @Test
    public void staticIntersectsRectangleSegment() {
        assertFalse(Rectangle2ai.intersectsRectangleSegment(5, 8, 15, 13, 0, 0, 1, 1));
        assertFalse(Rectangle2ai.intersectsRectangleSegment(5, 8, 15, 13, 0, 0, 8, 1));
        assertFalse(Rectangle2ai.intersectsRectangleSegment(5, 8, 15, 13, 0, 0, 8, 6));
        assertTrue(Rectangle2ai.intersectsRectangleSegment(5, 8, 15, 13, 0, 0, 100, 100));
        assertTrue(Rectangle2ai.intersectsRectangleSegment(5, 8, 15, 13, 7, 10, 8, 11));
        assertFalse(Rectangle2ai.intersectsRectangleSegment(5, 8, 15, 13, 16, 0, 116, 100));
    }

    @Override
	@Test
    public void inflate() {
        this.shape.inflate(1, 2, 3, 4);
        assertEquals(4, this.shape.getMinX());
        assertEquals(6, this.shape.getMinY());
        assertEquals(18, this.shape.getMaxX());
        assertEquals(17, this.shape.getMaxY());
    }

    @Test
    public void setUnion() {
        this.shape.setUnion(createRectangle(0, 0, 12, 1));
        assertEquals(0, this.shape.getMinX());
        assertEquals(0, this.shape.getMinY());
        assertEquals(15, this.shape.getMaxX());
        assertEquals(13, this.shape.getMaxY());
    }

    @Test
    public void createUnion() {
        T union = this.shape.createUnion(createRectangle(0, 0, 12, 1));
        assertNotSame(this.shape, union);
        assertEquals(0, union.getMinX());
        assertEquals(0, union.getMinY());
        assertEquals(15, union.getMaxX());
        assertEquals(13, union.getMaxY());
        assertEquals(5, this.shape.getMinX());
        assertEquals(8, this.shape.getMinY());
        assertEquals(15, this.shape.getMaxX());
        assertEquals(13, this.shape.getMaxY());
    }

    @Test
    public void setIntersection_noIntersection() {
        this.shape.setIntersection(createRectangle(0, 0, 12, 1));
        assertTrue(this.shape.isEmpty());
    }

    @Test
    public void setIntersection_intersection() {
        this.shape.setIntersection(createRectangle(0, 0, 7, 10));
        assertEquals(5, this.shape.getMinX());
        assertEquals(8, this.shape.getMinY());
        assertEquals(7, this.shape.getMaxX());
        assertEquals(10, this.shape.getMaxY());
    }

    @Test
    public void createIntersection_noIntersection() {
        T box = this.shape.createIntersection(createRectangle(0, 0, 12, 1));
        assertNotSame(this.shape, box);
        assertTrue(box.isEmpty());
        assertEquals(5, this.shape.getMinX());
        assertEquals(8, this.shape.getMinY());
        assertEquals(15, this.shape.getMaxX());
        assertEquals(13, this.shape.getMaxY());
    }

    @Test
    public void createIntersection_intersection() {
        //createRectangle(5, 8, 10, 5);
        T box = this.shape.createIntersection(createRectangle(0, 0, 7, 10));
        assertNotSame(this.shape, box);
        assertEquals(5, box.getMinX());
        assertEquals(8, box.getMinY());
        assertEquals(7, box.getMaxX());
        assertEquals(10, box.getMaxY());
        assertEquals(5, this.shape.getMinX());
        assertEquals(8, this.shape.getMinY());
        assertEquals(15, this.shape.getMaxX());
        assertEquals(13, this.shape.getMaxY());
    }

    @Override
    public void intersectsPathIterator2ai() {
        Path2ai path = createPath();
        path.moveTo(0, 0);
        path.lineTo(2, 2);
        path.quadTo(3, 0, 4, 3);
        path.curveTo(5, -1, 6, 5, 7, -5);
        path.closePath();
        this.shape = createRectangle(0, 0, 1, 1);
        assertTrue(this.shape.intersects((PathIterator2ai) path.getPathIterator()));
        this.shape = createRectangle(4, 3, 1, 1);
        assertTrue(this.shape.intersects((PathIterator2ai) path.getPathIterator()));
        this.shape = createRectangle(2, 2, 1, 1);
        assertTrue(this.shape.intersects((PathIterator2ai) path.getPathIterator()));
        this.shape = createRectangle(2, 1, 1, 1);
        assertTrue(this.shape.intersects((PathIterator2ai) path.getPathIterator()));
        this.shape = createRectangle(3, 0, 1, 1);
        assertTrue(this.shape.intersects((PathIterator2ai) path.getPathIterator()));
        this.shape = createRectangle(-1, -1, 1, 1);
        assertTrue(this.shape.intersects((PathIterator2ai) path.getPathIterator()));
        this.shape = createRectangle(4, -3, 1, 1);
        assertTrue(this.shape.intersects((PathIterator2ai) path.getPathIterator()));
        this.shape = createRectangle(-3, 4, 1, 1);
        assertFalse(this.shape.intersects((PathIterator2ai) path.getPathIterator()));
        this.shape = createRectangle(6, -5, 1, 1);
        assertTrue(this.shape.intersects((PathIterator2ai) path.getPathIterator()));
        this.shape = createRectangle(4, 0, 1, 1);
        assertTrue(this.shape.intersects((PathIterator2ai) path.getPathIterator()));
        this.shape = createRectangle(5, 0, 1, 1);
        assertTrue(this.shape.intersects((PathIterator2ai) path.getPathIterator()));
        this.shape = createRectangle(0, -3, 1, 1);
        assertFalse(this.shape.intersects((PathIterator2ai) path.getPathIterator()));
        this.shape = createRectangle(0, -3, 2, 1);
        assertFalse(this.shape.intersects((PathIterator2ai) path.getPathIterator()));
    }

    @Override
    public void intersectsShape2D() {
        assertTrue(this.shape.intersects((Shape2D) createCircle(0,0,100)));
        assertTrue(this.shape.intersects((Shape2D) createRectangle(7,10,1,1)));
    }

    @Override
    public void operator_addVector2D() {
        this.shape.operator_add(createVector(3, 4));
        assertEquals(8, this.shape.getMinX());
        assertEquals(12, this.shape.getMinY());
        assertEquals(18, this.shape.getMaxX());
        assertEquals(17, this.shape.getMaxY());
    }

    @Override
    public void operator_plusVector2D() {
        T r = this.shape.operator_plus(createVector(3, 4));
        assertEquals(8, r.getMinX());
        assertEquals(12, r.getMinY());
        assertEquals(18, r.getMaxX());
        assertEquals(17, r.getMaxY());
    }

    @Override
    public void operator_removeVector2D() {
        this.shape.operator_remove(createVector(3, 4));
        assertEquals(2, this.shape.getMinX());
        assertEquals(4, this.shape.getMinY());
        assertEquals(12, this.shape.getMaxX());
        assertEquals(9, this.shape.getMaxY());
    }

    @Override
    public void operator_minusVector2D() {
        T r = this.shape.operator_minus(createVector(3, 4));
        assertEquals(2, r.getMinX());
        assertEquals(4, r.getMinY());
        assertEquals(12, r.getMaxX());
        assertEquals(9, r.getMaxY());
    }

    @Override
    public void operator_multiplyTransform2D() {
        Transform2D tr;
        PathIterator2ai pi;

        tr = new Transform2D();
        pi = this.shape.operator_multiply(tr).getPathIterator();
        assertElement(pi, PathElementType.MOVE_TO, 5,8);
        assertElement(pi, PathElementType.LINE_TO, 15,8);
        assertElement(pi, PathElementType.LINE_TO, 15,13);
        assertElement(pi, PathElementType.LINE_TO, 5,13);
        assertElement(pi, PathElementType.CLOSE, 5,8);
        assertNoElement(pi);

        tr = new Transform2D();
        tr.makeTranslationMatrix(3.4f, 4.5f);
        pi = this.shape.operator_multiply(tr).getPathIterator();
        assertElement(pi, PathElementType.MOVE_TO, 8,13);
        assertElement(pi, PathElementType.LINE_TO, 18,13);
        assertElement(pi, PathElementType.LINE_TO, 18,18);
        assertElement(pi, PathElementType.LINE_TO, 8,18);
        assertElement(pi, PathElementType.CLOSE, 8,13);
        assertNoElement(pi);

        tr = new Transform2D();
        tr.makeRotationMatrix(MathConstants.QUARTER_PI);		
        pi = this.shape.operator_multiply(tr).getPathIterator();
        assertElement(pi, PathElementType.MOVE_TO, -2,9);
        assertElement(pi, PathElementType.LINE_TO, 5,16);
        assertElement(pi, PathElementType.LINE_TO, 1,20);
        assertElement(pi, PathElementType.LINE_TO, -6,13);
        assertElement(pi, PathElementType.CLOSE, -2,9);
        assertNoElement(pi);
    }

    @Override
    public void operator_andPoint2D() {
        assertFalse(this.shape.operator_and(createPoint(0,0)));
        assertTrue(this.shape.operator_and(createPoint(11,10)));
        assertFalse(this.shape.operator_and(createPoint(11,50)));
    }

    @Override
    public void operator_andShape2D() {
        assertTrue(this.shape.operator_and(createCircle(0,0,100)));
        assertTrue(this.shape.operator_and(createRectangle(7,10,1,1)));
    }

    @Override
    public void operator_upToPoint2D() {
        assertEpsilonEquals(0f, this.shape.operator_upTo(createPoint(5,8)));
        assertEpsilonEquals(0f, this.shape.operator_upTo(createPoint(10,10)));
        assertEpsilonEquals(1f, this.shape.operator_upTo(createPoint(4,8)));
        assertEpsilonEquals(9.433981132f, this.shape.operator_upTo(createPoint(0,0)));
    }

    @Override
	@Test
    public void getClosestPointToCircle2ai() {
        assertIntPointEquals(5, 8, this.shape.getClosestPointTo(createCircle(0, 0, 2)));
        assertIntPointEquals(11, 13, this.shape.getClosestPointTo(createCircle(11, 20, 2)));
        assertIntPointEquals(5, 10, this.shape.getClosestPointTo(createCircle(2, 10, 2)));
        assertClosestPointInBothShapes(this.shape, createCircle(16, 14, 2));
        assertClosestPointInBothShapes(this.shape, createCircle(11, 10, 2));
    }

    @Override
	@Test
    public void getDistanceSquaredCircle2ai() {
        assertEpsilonEquals(52, this.shape.getDistanceSquared(createCircle(0, 0, 2)));
        assertEpsilonEquals(25, this.shape.getDistanceSquared(createCircle(11, 20, 2)));
        assertEpsilonEquals(1, this.shape.getDistanceSquared(createCircle(2, 10, 2)));
        assertEpsilonEquals(0, this.shape.getDistanceSquared(createCircle(16, 14, 2)));
        assertEpsilonEquals(0, this.shape.getDistanceSquared(createCircle(11, 10, 2)));
    }

    @Override
	@Test
    public void getClosestPointToRectangle2ai() {
        assertIntPointEquals(5, 8, this.shape.getClosestPointTo(createRectangle(0, 0, 2, 2)));
        assertIntPointEquals(12, 13, this.shape.getClosestPointTo(createRectangle(11, 20, 2, 2)));
        assertIntPointEquals(5, 11, this.shape.getClosestPointTo(createRectangle(2, 10, 2, 2)));
        assertClosestPointInBothShapes(this.shape, createRectangle(15, 13, 2, 2));
        assertClosestPointInBothShapes(this.shape, createRectangle(11, 10, 2, 2));
    }

    @Override
	@Test
    public void getDistanceSquaredRectangle2ai() {
        assertEpsilonEquals(45, this.shape.getDistanceSquared(createRectangle(0, 0, 2, 2)));
        assertEpsilonEquals(49, this.shape.getDistanceSquared(createRectangle(11, 20, 2, 2)));
        assertEpsilonEquals(1, this.shape.getDistanceSquared(createRectangle(2, 10, 2, 2)));
        assertEpsilonEquals(0, this.shape.getDistanceSquared(createRectangle(15, 13, 2, 2)));
        assertEpsilonEquals(0, this.shape.getDistanceSquared(createRectangle(11, 10, 2, 2)));
    }

    @Override
	@Test
    public void getClosestPointToSegment2ai() {
        assertIntPointEquals(5, 8, this.shape.getClosestPointTo(createSegment(0, 0, 2, 2)));
        assertIntPointEquals(15, 8, this.shape.getClosestPointTo(createSegment(0, 0, 18, 8)));
        assertIntPointEquals(15, 13, this.shape.getClosestPointTo(createRectangle(18, 8, 15, 14)));
        assertClosestPointInBothShapes(this.shape, createRectangle(6, 10, 13, 12));
    }

    @Override
	@Test
    public void getDistanceSquaredSegment2ai() {
        assertEpsilonEquals(45, this.shape.getDistanceSquared(createSegment(0, 0, 2, 2)));
        assertEpsilonEquals(1, this.shape.getDistanceSquared(createSegment(0, 0, 18, 8)));
        assertEpsilonEquals(1, this.shape.getDistanceSquared(createSegment(18, 8, 15, 14)));
        assertEpsilonEquals(0, this.shape.getDistanceSquared(createSegment(6, 10, 13, 12)));
    }

    protected MultiShape2ai createTestMultiShape(int dx, int dy) {
        MultiShape2ai multishape = createMultiShape();
        Segment2ai segment = createSegment(dx - 5, dy - 4, dx - 8, dy - 1);
        Rectangle2ai rectangle = createRectangle(dx + 2, dy + 1, 3, 2);
        multishape.add(segment);
        multishape.add(rectangle);
        return multishape;
    }

    @Override
	@Test
    public void getClosestPointToMultiShape2ai() {
        assertIntPointEquals(5, 8, this.shape.getClosestPointTo(createTestMultiShape(0, 0)));
        assertIntPointEquals(10, 8, this.shape.getClosestPointTo(createTestMultiShape(18, 8)));
        assertClosestPointInBothShapes(this.shape, createTestMultiShape(6, 10));
    }

    @Override
	@Test
    public void getDistanceSquaredMultiShape2ai() {
        assertEpsilonEquals(25, this.shape.getDistanceSquared(createTestMultiShape(0, 0)));
        assertEpsilonEquals(1, this.shape.getDistanceSquared(createTestMultiShape(18, 8)));
        assertEpsilonEquals(0, this.shape.getDistanceSquared(createTestMultiShape(6, 10)));
    }

    protected Path2ai createTestPath(int dx, int dy) {
        Path2ai path = createPath();
        path.moveTo(dx + 5, dy - 5);
        path.lineTo(dx + 20, dy + 5);
        path.lineTo(dx + 0, dy + 20);
        path.lineTo(dx - 5, dy);
        return path;
    }

    @Override
	@Test
    public void getClosestPointToPath2ai() {
        assertClosestPointInBothShapes(this.shape, createTestPath(0, 0));
        assertIntPointEquals(15, 8, this.shape.getClosestPointTo(createTestPath(47, 8)));
        assertClosestPointInBothShapes(this.shape, createTestPath(6, 10));
    }

    @Override
	@Test
    public void getDistanceSquaredPath2ai() {
        assertEpsilonEquals(0, this.shape.getDistanceSquared(createTestPath(0, 0)));
        assertEpsilonEquals(729, this.shape.getDistanceSquared(createTestPath(47, 8)));
        assertEpsilonEquals(0, this.shape.getDistanceSquared(createTestPath(6, 10)));
    }

}