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

import org.junit.Test;

import org.arakhne.afc.math.geometry.PathElementType;
import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.Shape2D;
import org.arakhne.afc.math.geometry.d2.Transform2D;
import org.arakhne.afc.math.geometry.d2.Vector2D;
import org.arakhne.afc.math.geometry.d2.ai.PathIterator2ai;

@SuppressWarnings("all")
public abstract class AbstractRoundRectangle2afpTest<T extends RoundRectangle2afp<?, T, ?, ?, ?, B>,
B extends Rectangle2afp<?, ?, ?, ?, ?, B>> extends AbstractRectangularShape2afpTest<T, B> {

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
        assertTrue(this.shape.equalsToShape(this.shape));
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

        assertEpsilonEquals(0, this.shape.getDistanceSquared(createPoint(7.5, 8.4)));
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
    public void containsShape2D() {
        assertFalse(this.shape.contains(createCircle(0, 0, 1)));
        assertFalse(this.shape.contains(createCircle(0, 0, 7)));
        assertFalse(this.shape.contains(createCircle(0, 0, 20)));
        assertTrue(this.shape.contains(createCircle(6, 10, 1)));
        assertFalse(this.shape.contains(createCircle(5, 8, 5)));
        assertFalse(this.shape.contains(createCircle(5.5, 8.5, 4)));
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

    @Test
    public void getFlatteningPathIterator() {
        PathIterator2afp pi = this.shape.getFlatteningPathIterator();
        assertElement(pi, PathElementType.MOVE_TO, 5.1, 8);
        assertElement(pi, PathElementType.LINE_TO, 9.90000, 8.00000);
        assertElement(pi, PathElementType.LINE_TO, 9.91022, 8.00103);
        assertElement(pi, PathElementType.LINE_TO, 9.92015, 8.00406);
        assertElement(pi, PathElementType.LINE_TO, 9.92974, 8.00899);
        assertElement(pi, PathElementType.LINE_TO, 9.93892, 8.01572);
        assertElement(pi, PathElementType.LINE_TO, 9.94767, 8.02414);
        assertElement(pi, PathElementType.LINE_TO, 9.95591, 8.03416);
        assertElement(pi, PathElementType.LINE_TO, 9.96361, 8.04567);
        assertElement(pi, PathElementType.LINE_TO, 9.97071, 8.05858);
        assertElement(pi, PathElementType.LINE_TO, 9.97402, 8.06552);
        assertElement(pi, PathElementType.LINE_TO, 9.97716, 8.07278);
        assertElement(pi, PathElementType.LINE_TO, 9.98013, 8.08034);
        assertElement(pi, PathElementType.LINE_TO, 9.98292, 8.08818);
        assertElement(pi, PathElementType.LINE_TO, 9.98552, 8.09629);
        assertElement(pi, PathElementType.LINE_TO, 9.98793, 8.10467);
        assertElement(pi, PathElementType.LINE_TO, 9.99014, 8.11329);
        assertElement(pi, PathElementType.LINE_TO, 9.99214, 8.12215);
        assertElement(pi, PathElementType.LINE_TO, 9.99393, 8.13123);
        assertElement(pi, PathElementType.LINE_TO, 9.99550, 8.14053);
        assertElement(pi, PathElementType.LINE_TO, 9.99685, 8.15002);
        assertElement(pi, PathElementType.LINE_TO, 9.99797, 8.15969);
        assertElement(pi, PathElementType.LINE_TO, 9.99885, 8.16954);
        assertElement(pi, PathElementType.LINE_TO, 9.99948, 8.17955);
        assertElement(pi, PathElementType.LINE_TO, 9.99987, 8.18971);
        assertElement(pi, PathElementType.LINE_TO, 10.00000, 8.20000);
        assertElement(pi, PathElementType.LINE_TO, 10.00000, 17.80000);
        assertElement(pi, PathElementType.LINE_TO, 9.99987, 17.81029);
        assertElement(pi, PathElementType.LINE_TO, 9.99948, 17.82045);
        assertElement(pi, PathElementType.LINE_TO, 9.99885, 17.83046);
        assertElement(pi, PathElementType.LINE_TO, 9.99797, 17.84031);
        assertElement(pi, PathElementType.LINE_TO, 9.99685, 17.84998);
        assertElement(pi, PathElementType.LINE_TO, 9.99550, 17.85947);
        assertElement(pi, PathElementType.LINE_TO, 9.99393, 17.86877);
        assertElement(pi, PathElementType.LINE_TO, 9.99214, 17.87785);
        assertElement(pi, PathElementType.LINE_TO, 9.99014, 17.88671);
        assertElement(pi, PathElementType.LINE_TO, 9.98793, 17.89533);
        assertElement(pi, PathElementType.LINE_TO, 9.98552, 17.90371);
        assertElement(pi, PathElementType.LINE_TO, 9.98292, 17.91182);
        assertElement(pi, PathElementType.LINE_TO, 9.98013, 17.91966);
        assertElement(pi, PathElementType.LINE_TO, 9.97716, 17.92722);
        assertElement(pi, PathElementType.LINE_TO, 9.97402, 17.93448);
        assertElement(pi, PathElementType.LINE_TO, 9.97071, 17.94142);
        assertElement(pi, PathElementType.LINE_TO, 9.96361, 17.95433);
        assertElement(pi, PathElementType.LINE_TO, 9.95591, 17.96584);
        assertElement(pi, PathElementType.LINE_TO, 9.94767, 17.97586);
        assertElement(pi, PathElementType.LINE_TO, 9.93892, 17.98428);
        assertElement(pi, PathElementType.LINE_TO, 9.92974, 17.99101);
        assertElement(pi, PathElementType.LINE_TO, 9.92015, 17.99594);
        assertElement(pi, PathElementType.LINE_TO, 9.91022, 17.99897);
        assertElement(pi, PathElementType.LINE_TO, 9.90000, 18.00000);
        assertElement(pi, PathElementType.LINE_TO, 5.10000, 18.00000);
        assertElement(pi, PathElementType.LINE_TO, 5.08978, 17.99897);
        assertElement(pi, PathElementType.LINE_TO, 5.07985, 17.99594);
        assertElement(pi, PathElementType.LINE_TO, 5.07026, 17.99101);
        assertElement(pi, PathElementType.LINE_TO, 5.06108, 17.98428);
        assertElement(pi, PathElementType.LINE_TO, 5.05233, 17.97586);
        assertElement(pi, PathElementType.LINE_TO, 5.04409, 17.96584);
        assertElement(pi, PathElementType.LINE_TO, 5.03639, 17.95433);
        assertElement(pi, PathElementType.LINE_TO, 5.02929, 17.94142);
        assertElement(pi, PathElementType.LINE_TO, 5.02598, 17.93448);
        assertElement(pi, PathElementType.LINE_TO, 5.02284, 17.92722);
        assertElement(pi, PathElementType.LINE_TO, 5.01987, 17.91966);
        assertElement(pi, PathElementType.LINE_TO, 5.01708, 17.91182);
        assertElement(pi, PathElementType.LINE_TO, 5.01448, 17.90371);
        assertElement(pi, PathElementType.LINE_TO, 5.01207, 17.89533);
        assertElement(pi, PathElementType.LINE_TO, 5.00986, 17.88671);
        assertElement(pi, PathElementType.LINE_TO, 5.00786, 17.87785);
        assertElement(pi, PathElementType.LINE_TO, 5.00607, 17.86877);
        assertElement(pi, PathElementType.LINE_TO, 5.00450, 17.85947);
        assertElement(pi, PathElementType.LINE_TO, 5.00315, 17.84998);
        assertElement(pi, PathElementType.LINE_TO, 5.00203, 17.84031);
        assertElement(pi, PathElementType.LINE_TO, 5.00115, 17.83046);
        assertElement(pi, PathElementType.LINE_TO, 5.00052, 17.82045);
        assertElement(pi, PathElementType.LINE_TO, 5.00013, 17.81029);
        assertElement(pi, PathElementType.LINE_TO, 5.00000, 17.80000);
        assertElement(pi, PathElementType.LINE_TO, 5.00000, 8.20000);
        assertElement(pi, PathElementType.LINE_TO, 5.00013, 8.18971);
        assertElement(pi, PathElementType.LINE_TO, 5.00052, 8.17955);
        assertElement(pi, PathElementType.LINE_TO, 5.00115, 8.16954);
        assertElement(pi, PathElementType.LINE_TO, 5.00203, 8.15969);
        assertElement(pi, PathElementType.LINE_TO, 5.00315, 8.15002);
        assertElement(pi, PathElementType.LINE_TO, 5.00450, 8.14053);
        assertElement(pi, PathElementType.LINE_TO, 5.00607, 8.13123);
        assertElement(pi, PathElementType.LINE_TO, 5.00786, 8.12215);
        assertElement(pi, PathElementType.LINE_TO, 5.00986, 8.11329);
        assertElement(pi, PathElementType.LINE_TO, 5.01207, 8.10467);
        assertElement(pi, PathElementType.LINE_TO, 5.01448, 8.09629);
        assertElement(pi, PathElementType.LINE_TO, 5.01708, 8.08818);
        assertElement(pi, PathElementType.LINE_TO, 5.01987, 8.08034);
        assertElement(pi, PathElementType.LINE_TO, 5.02284, 8.07278);
        assertElement(pi, PathElementType.LINE_TO, 5.02598, 8.06552);
        assertElement(pi, PathElementType.LINE_TO, 5.02929, 8.05858);
        assertElement(pi, PathElementType.LINE_TO, 5.03639, 8.04567);
        assertElement(pi, PathElementType.LINE_TO, 5.04409, 8.03416);
        assertElement(pi, PathElementType.LINE_TO, 5.05233, 8.02414);
        assertElement(pi, PathElementType.LINE_TO, 5.06108, 8.01572);
        assertElement(pi, PathElementType.LINE_TO, 5.07026, 8.00899);
        assertElement(pi, PathElementType.LINE_TO, 5.07985, 8.00406);
        assertElement(pi, PathElementType.LINE_TO, 5.08978, 8.00103);
        assertElement(pi, PathElementType.LINE_TO, 5.10000, 8.00000);
        assertElement(pi, PathElementType.CLOSE, 5.10000, 8.00000);
        assertNoElement(pi);
    }

    @Override
	@Test
    public void getClosestPointToCircle2afp() {
        assertFpPointEquals(5.06982, 8.00932, this.shape.getClosestPointTo(createCircle(0, 0, 1)));
        assertFpPointEquals(10, 14, this.shape.getClosestPointTo(createCircle(16, 14, 1)));
        assertFpPointEquals(8, 18, this.shape.getClosestPointTo(createCircle(8, 22, 1)));
        assertFpPointEquals(8, 8, this.shape.getClosestPointTo(createCircle(8, 0, 1)));
        assertFpPointEquals(9.96998, 17.94288, this.shape.getClosestPointTo(createCircle(14, 20, 1)));
        assertFpPointEquals(5.03849, 8.0423, this.shape.getClosestPointTo(createCircle(4.184131706667871, 7.494673851694933, 1)));
        assertClosestPointInBothShapes(this.shape, createCircle(4.188017837226872, 7.537903477557079, 1));
        assertClosestPointInBothShapes(this.shape, createCircle(4.5, 10, 1));
        assertClosestPointInBothShapes(this.shape, createCircle(5.5, 10, 1));
        assertClosestPointInBothShapes(this.shape, createCircle(7, 15, 1));
    }

    @Override
	@Test
    public void getDistanceSquaredCircle2afp() {
        assertEpsilonEquals(71.89428, this.shape.getDistanceSquared(createCircle(0, 0, 1)));
        assertEpsilonEquals(25, this.shape.getDistanceSquared(createCircle(16, 14, 1)));
        assertEpsilonEquals(9, this.shape.getDistanceSquared(createCircle(8, 22, 1)));
        assertEpsilonEquals(49, this.shape.getDistanceSquared(createCircle(8, 0, 1)));
        assertEpsilonEquals(12.42347, this.shape.getDistanceSquared(createCircle(14, 20, 1)));
        assertEpsilonEquals(.00022, this.shape.getDistanceSquared(createCircle(4.184131706667871, 7.494673851694933, 1)));
        assertEpsilonEquals(0, this.shape.getDistanceSquared(createCircle(4.188017837226872, 7.537903477557079, 1)));
        assertEpsilonEquals(0, this.shape.getDistanceSquared(createCircle(4.5, 10, 1)));
        assertEpsilonEquals(0, this.shape.getDistanceSquared(createCircle(5.5, 10, 1)));
        assertEpsilonEquals(0, this.shape.getDistanceSquared(createCircle(7, 15, 1)));
    }

    @Override
	@Test
    public void getClosestPointToSegment2afp() {
        assertFpPointEquals(5.0721, 8.00794, this.shape.getClosestPointTo(createSegment(0, 0, 1, 1)));
        assertFpPointEquals(5.06108, 8.01572, this.shape.getClosestPointTo(createSegment(0, 1, 1, 0)));
        assertFpPointEquals(8, 8, this.shape.getClosestPointTo(createSegment(6, 4, 8, 5)));
        assertClosestPointInBothShapes(this.shape, createSegment(4, 14, 6, 15));
        assertClosestPointInBothShapes(this.shape, createSegment(7, 9, 9, 10));
        assertFpPointEquals(9.90811, 8.00066, this.shape.getClosestPointTo(createSegment(
                9.315811794580389, 7.677476922530425, 11.315811794580389, 8.677476922530425)));
    }

    @Override
	@Test
    public void getDistanceSquaredSegment2afp() {
        assertEpsilonEquals(65.69325, this.shape.getDistanceSquared(createSegment(0, 0, 1, 1)));
        assertEpsilonEquals(74.83131, this.shape.getDistanceSquared(createSegment(0, 1, 1, 0)));
        assertEpsilonEquals(9, this.shape.getDistanceSquared(createSegment(6, 4, 8, 5)));
        assertEpsilonEquals(0, this.shape.getDistanceSquared(createSegment(4, 14, 6, 15)));
        assertEpsilonEquals(0, this.shape.getDistanceSquared(createSegment(7, 9, 9, 10)));
        assertEpsilonEquals(0.000585, this.shape.getDistanceSquared(createSegment(
                9.315811794580389, 7.677476922530425, 11.315811794580389, 8.677476922530425)));
    }

    protected Triangle2afp<?, ?, ?, ?, ?, ?> createTestTriangle(double dx, double dy) {
        return createTriangle(dx, dy, dx + 2, dy + 1.5, dx + 1.5, dy - 1.6);
    }

    @Override
	@Test
    public void getClosestPointToTriangle2afp() {
        assertFpPointEquals(5.07985, 8.00406, this.shape.getClosestPointTo(createTestTriangle(0, 0)));
        assertFpPointEquals(10, 16, this.shape.getClosestPointTo(createTestTriangle(14, 16)));
        assertFpPointEquals(5.04409, 17.96584, this.shape.getClosestPointTo(createTestTriangle(3, 20)));
        assertClosestPointInBothShapes(this.shape, createTestTriangle(7, 19));
        assertClosestPointInBothShapes(this.shape, createTestTriangle(4, 14));
        assertClosestPointInBothShapes(this.shape, createTestTriangle(6, 10));
    }

    @Override
	@Test
    public void getDistanceSquaredTriangle2afp() {
        assertEpsilonEquals(105.05333, this.shape.getDistanceSquared(createTestTriangle(0, 0)));
        assertEpsilonEquals(16, this.shape.getDistanceSquared(createTestTriangle(14, 16)));
        assertEpsilonEquals(0.48453, this.shape.getDistanceSquared(createTestTriangle(3, 20)));
        assertEpsilonEquals(0, this.shape.getDistanceSquared(createTestTriangle(7, 19)));
        assertEpsilonEquals(0, this.shape.getDistanceSquared(createTestTriangle(4, 14)));
        assertEpsilonEquals(0, this.shape.getDistanceSquared(createTestTriangle(6, 10)));
    }

    @Override
	@Test
    public void getClosestPointToRectangle2afp() {
        assertFpPointEquals(5.0829, 8.00294, this.shape.getClosestPointTo(createRectangle(0, 0, 3, 2)));
        assertFpPointEquals(5, 12, this.shape.getClosestPointTo(createRectangle(-1, 12, 3, 2)));
        assertFpPointEquals(5.07026, 17.99101, this.shape.getClosestPointTo(createRectangle(1, 20, 3, 2)));
        assertClosestPointInBothShapes(this.shape, createRectangle(8.5, 8.5, 3, 2));
        assertClosestPointInBothShapes(this.shape, createRectangle(6, 10, 3, 2));
    }

    @Override
	@Test
    public void getDistanceSquaredRectangle2afp() {
        assertEpsilonEquals(40.37454, this.shape.getDistanceSquared(createRectangle(0, 0, 3, 2)));
        assertEpsilonEquals(9, this.shape.getDistanceSquared(createRectangle(-1, 12, 3, 2)));
        assertEpsilonEquals(5.18151, this.shape.getDistanceSquared(createRectangle(1, 20, 3, 2)));
        assertEpsilonEquals(0, this.shape.getDistanceSquared(createRectangle(8.5, 8.5, 3, 2)));
        assertEpsilonEquals(0, this.shape.getDistanceSquared(createRectangle(6, 10, 3, 2)));
    }

    @Override
	@Test
    public void getClosestPointToEllipse2afp() {
        assertFpPointEquals(5.07985, 8.00406, this.shape.getClosestPointTo(createEllipse(0, 0, 3, 2)));
        assertFpPointEquals(5, 13, this.shape.getClosestPointTo(createEllipse(-1, 12, 3, 2)));
        assertFpPointEquals(5.07026, 17.99101, this.shape.getClosestPointTo(createEllipse(1, 20, 3, 2)));
        assertClosestPointInBothShapes(this.shape, createEllipse(8.5, 8.5, 3, 2));
        assertClosestPointInBothShapes(this.shape, createEllipse(6, 10, 3, 2));
    }

    @Override
	@Test
    public void getDistanceSquaredEllipse2afp() {
        assertEpsilonEquals(45.64412, this.shape.getDistanceSquared(createEllipse(0, 0, 3, 2)));
        assertEpsilonEquals(9, this.shape.getDistanceSquared(createEllipse(-1, 12, 3, 2)));
        assertEpsilonEquals(7.57743, this.shape.getDistanceSquared(createEllipse(1, 20, 3, 2)));
        assertEpsilonEquals(0, this.shape.getDistanceSquared(createEllipse(8.5, 8.5, 3, 2)));
        assertEpsilonEquals(0, this.shape.getDistanceSquared(createEllipse(6, 10, 3, 2)));
    }

    @Override
	@Test
    public void getClosestPointToRoundRectangle2afp() {
        assertFpPointEquals(5.07985, 8.00406, this.shape.getClosestPointTo(createRoundRectangle(0, 0, 3, 2, .2, .1)));
        assertFpPointEquals(5, 12.1, this.shape.getClosestPointTo(createRoundRectangle(-1, 12, 3, 2, .2, .1)));
        assertFpPointEquals(5.07026, 17.99101, this.shape.getClosestPointTo(createRoundRectangle(1, 20, 3, 2, .2, .1)));
        assertClosestPointInBothShapes(this.shape, createRoundRectangle(8.5, 8.5, 3, 2, .2, .1));
        assertClosestPointInBothShapes(this.shape, createRoundRectangle(6, 10, 3, 2, .2, .1));
    }

    @Override
	@Test
    public void getDistanceSquaredRoundRectangle2afp() {
        assertEpsilonEquals(40.95395, this.shape.getDistanceSquared(createRoundRectangle(0, 0, 3, 2, .2, .1)));
        assertEpsilonEquals(9, this.shape.getDistanceSquared(createRoundRectangle(-1, 12, 3, 2, .2, .1)));
        assertEpsilonEquals(5.42814, this.shape.getDistanceSquared(createRoundRectangle(1, 20, 3, 2, .2, .1)));
        assertEpsilonEquals(0, this.shape.getDistanceSquared(createRoundRectangle(8.5, 8.5, 3, 2, .2, .1)));
        assertEpsilonEquals(0, this.shape.getDistanceSquared(createRoundRectangle(6, 10, 3, 2, .2, .1)));
    }

    protected Path2afp<?, ?, ?, ?, ?, ?> createNonEmptyPath(double x, double y) {
        Path2afp<?, ?, ?, ?, ?, ?> path = createPath();
        path.moveTo(x, y);
        path.lineTo(x + 1, y + .5);
        path.lineTo(x, y + 1);
        return path;
    }

    @Override
	@Test
    public void getClosestPointToPath2afp() {
        assertFpPointEquals(5.07026, 8.00899, this.shape.getClosestPointTo(createNonEmptyPath(0, 0)));
        assertFpPointEquals(5, 12.5, this.shape.getClosestPointTo(createNonEmptyPath(2, 12)));
        assertClosestPointInBothShapes(this.shape, createNonEmptyPath(9.75, 14));
        assertClosestPointInBothShapes(this.shape, createNonEmptyPath(7, 9));
    }

    @Override
	@Test
    public void getDistanceSquaredPath2afp() {
        assertEpsilonEquals(72.952, this.shape.getDistanceSquared(createNonEmptyPath(0, 0)));
        assertEpsilonEquals(4, this.shape.getDistanceSquared(createNonEmptyPath(2, 12)));
        assertEpsilonEquals(0, this.shape.getDistanceSquared(createNonEmptyPath(9.75, 14)));
        assertEpsilonEquals(0, this.shape.getDistanceSquared(createNonEmptyPath(7, 9)));
    }

    protected MultiShape2afp createTestMultiShape(double dx, double dy) {
        Circle2afp circle = createCircle(dx - 3, dy + 2, 2);
        Triangle2afp triangle = createTestTriangle(dx +1, dy - 1);
        MultiShape2afp multishape = createMultiShape();
        multishape.add(circle);
        multishape.add(triangle);
        return multishape;
    }
    
    @Override
	@Test
    public void getClosestPointToMultiShape2afp() {
        assertFpPointEquals(5.04453, 8.03359, this.shape.getClosestPointTo(createTestMultiShape(0, 0)));
        assertFpPointEquals(5, 12.5, this.shape.getClosestPointTo(createTestMultiShape(2, 12)));
        assertClosestPointInBothShapes(this.shape, createTestMultiShape(9.75, 14));
        assertClosestPointInBothShapes(this.shape, createTestMultiShape(7, 9));
    }

    @Override
	@Test
    public void getDistanceSquaredMultiShape2afp() {
        assertEpsilonEquals(64.89556, this.shape.getDistanceSquared(createTestMultiShape(0, 0)));
        assertEpsilonEquals(0, this.shape.getDistanceSquared(createTestMultiShape(2, 12)));
        assertEpsilonEquals(0, this.shape.getDistanceSquared(createTestMultiShape(9.75, 14)));
        assertEpsilonEquals(0, this.shape.getDistanceSquared(createTestMultiShape(7, 9)));
    }
    
    protected Parallelogram2afp createTestParallelogram(double dx, double dy) {
        Vector2D r = createVector(4, 1).toUnitVector();
        Vector2D s = createVector(-1, -1).toUnitVector();
        return createParallelogram(dx, dy, r.getX(), r.getY(), 2, s.getX(), s.getY(), 1);
    }

    @Override
	@Test
    public void getClosestPointToParallelogram2afp() {
        assertFpPointEquals(5.07985, 8.00406, this.shape.getClosestPointTo(createTestParallelogram(0, 0)));
        assertFpPointEquals(5, 13.19218, this.shape.getClosestPointTo(createTestParallelogram(2, 12)));
        assertClosestPointInBothShapes(this.shape, createTestParallelogram(9.75, 14));
        assertClosestPointInBothShapes(this.shape, createTestParallelogram(7, 9));
    }

    @Override
	@Test
    public void getDistanceSquaredParallelogram2afp() {
        assertEpsilonEquals(52.31862, this.shape.getDistanceSquared(createTestParallelogram(0, 0)));
        assertEpsilonEquals(0.12433, this.shape.getDistanceSquared(createTestParallelogram(2, 12)));
        assertEpsilonEquals(0, this.shape.getDistanceSquared(createTestParallelogram(9.75, 14)));
        assertEpsilonEquals(0, this.shape.getDistanceSquared(createTestParallelogram(7, 9)));
    }
    
    protected OrientedRectangle2afp createTestOrientedRectangle(double dx, double dy) {
        Vector2D r = createVector(4, 1).toUnitVector();
        return createOrientedRectangle(dx, dy, r.getX(), r.getY(), 2, 1);
    }

    @Override
	@Test
    public void getClosestPointToOrientedRectangle2afp() {
        assertFpPointEquals(5.07026, 8.00899, this.shape.getClosestPointTo(createTestOrientedRectangle(0, 0)));
        assertFpPointEquals(5, 11.51493, this.shape.getClosestPointTo(createTestOrientedRectangle(2, 12)));
        assertClosestPointInBothShapes(this.shape, createTestOrientedRectangle(9.75, 14));
        assertClosestPointInBothShapes(this.shape, createTestOrientedRectangle(7, 9));
    }

    @Override
	@Test
    public void getDistanceSquaredOrientedRectangle2afp() {
        assertEpsilonEquals(54.32585, this.shape.getDistanceSquared(createTestOrientedRectangle(0, 0)));
        assertEpsilonEquals(0.66778, this.shape.getDistanceSquared(createTestOrientedRectangle(2, 12)));
        assertEpsilonEquals(0, this.shape.getDistanceSquared(createTestOrientedRectangle(9.75, 14)));
        assertEpsilonEquals(0, this.shape.getDistanceSquared(createTestOrientedRectangle(7, 9)));
    }

}