/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2019 The original authors, and other authors.
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

package org.arakhne.afc.math.test.geometry.d2.afp;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import org.arakhne.afc.math.geometry.PathElementType;
import org.arakhne.afc.math.geometry.coordinatesystem.CoordinateSystem2D;
import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.Shape2D;
import org.arakhne.afc.math.geometry.d2.Transform2D;
import org.arakhne.afc.math.geometry.d2.Vector2D;
import org.arakhne.afc.math.geometry.d2.afp.Circle2afp;
import org.arakhne.afc.math.geometry.d2.afp.MultiShape2afp;
import org.arakhne.afc.math.geometry.d2.afp.OrientedRectangle2afp;
import org.arakhne.afc.math.geometry.d2.afp.Parallelogram2afp;
import org.arakhne.afc.math.geometry.d2.afp.Path2afp;
import org.arakhne.afc.math.geometry.d2.afp.PathIterator2afp;
import org.arakhne.afc.math.geometry.d2.afp.Rectangle2afp;
import org.arakhne.afc.math.geometry.d2.afp.RoundRectangle2afp;
import org.arakhne.afc.math.geometry.d2.afp.Triangle2afp;
import org.arakhne.afc.math.geometry.d2.ai.PathIterator2ai;

@SuppressWarnings("all")
public abstract class AbstractRoundRectangle2afpTest<T extends RoundRectangle2afp<?, T, ?, ?, ?, B>,
B extends Rectangle2afp<?, ?, ?, ?, ?, B>> extends AbstractRectangularShape2afpTest<T, B> {

    @Override
    protected final T createShape() {
        return (T) createRoundRectangle(5, 8, 5, 10, .1, .2);
    }

    @ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
    public void staticContainsRoundRectanglePoint(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
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

    @ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
    public void staticContainsRoundRectangleRectangle(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
        assertFalse(RoundRectangle2afp.containsRoundRectangleRectangle(5, 8, 5, 10, .1, .2, 0, 0, 1, 1));
        assertFalse(RoundRectangle2afp.containsRoundRectangleRectangle(5, 8, 5, 10, .1, .2, 0, 0, 7, 10));
        assertFalse(RoundRectangle2afp.containsRoundRectangleRectangle(5, 8, 5, 10, .1, .2, 0, 0, 20, 20));
        assertTrue(RoundRectangle2afp.containsRoundRectangleRectangle(5, 8, 5, 10, .1, .2, 6, 10, 1, 1));
        assertFalse(RoundRectangle2afp.containsRoundRectangleRectangle(5, 8, 5, 10, .1, .2, 5, 8, 5, 10));
        assertTrue(RoundRectangle2afp.containsRoundRectangleRectangle(5, 8, 5, 10, .1, .2, 5.5, 8.5, 4, 9));
    }

    @ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
    public void staticIntersectsRoundRectangleSegment(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
        assertFalse(RoundRectangle2afp.intersectsRoundRectangleSegment(5, 8, 10, 18, .1, .2, 0, 0, 1, 1));
        assertFalse(RoundRectangle2afp.intersectsRoundRectangleSegment(5, 8, 10, 18, .1, .2, 20, 20, 21, 21));
        assertTrue(RoundRectangle2afp.intersectsRoundRectangleSegment(5, 8, 10, 18, .1, .2, 0, 0, 7, 12));
        assertFalse(RoundRectangle2afp.intersectsRoundRectangleSegment(5, 8, 10, 18, .1, .2, 0, 0, 7, 8));
        assertFalse(RoundRectangle2afp.intersectsRoundRectangleSegment(5, 8, 10, 18, .1, .2, 6, 7, 4.1, 9));
        assertTrue(RoundRectangle2afp.intersectsRoundRectangleSegment(5, 8, 10, 18, .1, .2, 6.1, 7, 4.1, 9));
    }

    @ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
    public void staticIntersectsRoundRectangleRoundRectangle(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
        assertFalse(RoundRectangle2afp.intersectsRoundRectangleRoundRectangle(5, 8, 10, 18, .1, .2, 0, 0, 1, 1, .1, .2));
        assertFalse(RoundRectangle2afp.intersectsRoundRectangleRoundRectangle(5, 8, 10, 18, .1, .2, 20, 20, 21, 21, .1, .2));
        assertTrue(RoundRectangle2afp.intersectsRoundRectangleRoundRectangle(5, 8, 10, 18, .1, .2, 0, 0, 7, 12, .1, .2));
        assertFalse(RoundRectangle2afp.intersectsRoundRectangleRoundRectangle(5, 8, 10, 18, .1, .2, 0, 0, 7, 8, .1, .2));
        assertFalse(RoundRectangle2afp.intersectsRoundRectangleRoundRectangle(5, 8, 10, 18, .1, .2, 0, 0, 5.01, 8.01, .1, .2));
        assertFalse(RoundRectangle2afp.intersectsRoundRectangleRoundRectangle(5, 8, 10, 18, .1, .2, 0, 0, 5.05, 8.05, .1, .2));
        assertFalse(RoundRectangle2afp.intersectsRoundRectangleRoundRectangle(5, 8, 10, 18, .1, .2, 0, 0, 5.05, 8.1, .1, .2));
        assertTrue(RoundRectangle2afp.intersectsRoundRectangleRoundRectangle(5, 8, 10, 18, .1, .2, 0, 0, 5.05, 8.15, .1, .2));
    }

    @ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
    public void staticIntersectsRoundRectangleRectangle(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
        assertFalse(RoundRectangle2afp.intersectsRoundRectangleRectangle(5, 8, 10, 18, .1, .2, 0, 0, 1, 1));
        assertFalse(RoundRectangle2afp.intersectsRoundRectangleRectangle(5, 8, 10, 18, .1, .2, 20, 20, 21, 21));
        assertTrue(RoundRectangle2afp.intersectsRoundRectangleRectangle(5, 8, 10, 18, .1, .2, 0, 0, 7, 12));
        assertFalse(RoundRectangle2afp.intersectsRoundRectangleRectangle(5, 8, 10, 18, .1, .2, 0, 0, 7, 8));
        assertFalse(RoundRectangle2afp.intersectsRoundRectangleRectangle(5, 8, 10, 18, .1, .2, 0, 0, 5.01, 8.01));
        assertTrue(RoundRectangle2afp.intersectsRoundRectangleRectangle(5, 8, 10, 18, .1, .2, 0, 0, 5.05, 8.05));
        assertTrue(RoundRectangle2afp.intersectsRoundRectangleRectangle(5, 8, 10, 18, .1, .2, 0, 0, 5.05, 8.1));
        assertTrue(RoundRectangle2afp.intersectsRoundRectangleRectangle(5, 8, 10, 18, .1, .2, 0, 0, 5.05, 8.15));
    }

    @ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
    public void staticIntersectsRoundRectangleCircle(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
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

    @ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
    public void staticIntersectsRoundRectangleEllipse(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
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

    @ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
    public void getArcWidth(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
        assertEpsilonEquals(.1, this.shape.getArcWidth());
    }

    @ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
    public void getArcHeight(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
        assertEpsilonEquals(.2, this.shape.getArcHeight());
    }

    @ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
    public void setArcWidth(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
        this.shape.setArcWidth(123.456);
        assertEpsilonEquals(2.5, this.shape.getArcWidth());
    }

    @ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
    public void setArcHeight(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
        this.shape.setArcHeight(123.456);
        assertEpsilonEquals(5, this.shape.getArcHeight());
    }

    @ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
    public void setArcWidth_whenSetMaxX(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
        this.shape.setMaxX(6);
        assertEpsilonEquals(.1, this.shape.getArcWidth());
        this.shape.setMaxX(5.1);
        assertEpsilonEquals(.05, this.shape.getArcWidth());
    }

    @ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
    public void setArcWidth_whenSetMinX(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
        this.shape.setMinX(4);
        assertEpsilonEquals(.1, this.shape.getArcWidth());
        this.shape.setMinX(9.9);
        assertEpsilonEquals(.05, this.shape.getArcWidth());
    }

    @ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
    public void setArcWidth_whenSetWidth(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
        this.shape.setWidth(1);
        assertEpsilonEquals(.1, this.shape.getArcWidth());
        this.shape.setWidth(.1);
        assertEpsilonEquals(.05, this.shape.getArcWidth());
    }

    @ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
    public void setArcHeight_whenSetMaxY(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
        this.shape.setMaxY(9);
        assertEpsilonEquals(.2, this.shape.getArcHeight());
        this.shape.setMaxY(8.1);
        assertEpsilonEquals(.05, this.shape.getArcHeight());
    }

    @ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
    public void setArcHeight_whenSetMinY(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
        this.shape.setMinY(7);
        assertEpsilonEquals(.2, this.shape.getArcHeight());
        this.shape.setMinY(17.9);
        assertEpsilonEquals(.05, this.shape.getArcHeight());
    }

    @ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
    public void setArcHeight_whenSetHeight(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
        this.shape.setHeight(1);
        assertEpsilonEquals(.2, this.shape.getArcHeight());
        this.shape.setHeight(.2);
        assertEpsilonEquals(.1, this.shape.getArcHeight());
    }

    @ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
    public void setDoubleDoubleDoubleDoubleDoubleDouble(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
        this.shape.set(10, 20, 30, 40, 1, 2);
        assertEpsilonEquals(10, this.shape.getMinX());
        assertEpsilonEquals(20, this.shape.getMinY());
        assertEpsilonEquals(40, this.shape.getMaxX());
        assertEpsilonEquals(60, this.shape.getMaxY());
        assertEpsilonEquals(1, this.shape.getArcWidth());
        assertEpsilonEquals(2, this.shape.getArcHeight());
    }

    @Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
    public void setFromCornersDoubleDoubleDoubleDouble(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
        this.shape.setFromCorners(10, 20, 30, 40);
        assertEpsilonEquals(10, this.shape.getMinX());
        assertEpsilonEquals(20, this.shape.getMinY());
        assertEpsilonEquals(30, this.shape.getMaxX());
        assertEpsilonEquals(40, this.shape.getMaxY());
        assertEpsilonEquals(.1, this.shape.getArcWidth());
        assertEpsilonEquals(.2, this.shape.getArcHeight());
    }

    @ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
    public void setFromCornersDoubleDoubleDoubleDoubleDoubleDouble(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
        this.shape.setFromCorners(10, 20, 30, 40, 1, 2);
        assertEpsilonEquals(10, this.shape.getMinX());
        assertEpsilonEquals(20, this.shape.getMinY());
        assertEpsilonEquals(30, this.shape.getMaxX());
        assertEpsilonEquals(40, this.shape.getMaxY());
        assertEpsilonEquals(1, this.shape.getArcWidth());
        assertEpsilonEquals(2, this.shape.getArcHeight());
    }

    @ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
    @Override
    public void equalsObject(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
        assertFalse(this.shape.equals(null));
        assertFalse(this.shape.equals(new Object()));
        assertFalse(this.shape.equals(createRoundRectangle(0, 8, 5, 12, .1, .2)));
        assertFalse(this.shape.equals(createRoundRectangle(5, 8, 5, 0, .1, .2)));
        assertFalse(this.shape.equals(createSegment(5, 8, 5, 10)));
        assertTrue(this.shape.equals(this.shape));
        assertTrue(this.shape.equals(createRoundRectangle(5, 8, 5, 10, .1, .2)));
    }

    @ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
    @Override
    public void equalsObject_withPathIterator(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
        assertFalse(this.shape.equals(createRoundRectangle(0, 8, 5, 12, .1, .2).getPathIterator()));
        assertFalse(this.shape.equals(createRoundRectangle(5, 8, 5, 0, .1, .2).getPathIterator()));
        assertFalse(this.shape.equals(createSegment(5, 8, 5, 10).getPathIterator()));
        assertTrue(this.shape.equals(this.shape.getPathIterator()));
        assertTrue(this.shape.equals(createRoundRectangle(5, 8, 5, 10, .1, .2).getPathIterator()));
    }

    @ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
    @Override
    public void equalsToPathIterator(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
        assertFalse(this.shape.equalsToPathIterator((PathIterator2ai) null));
        assertFalse(this.shape.equalsToPathIterator(createRoundRectangle(0, 8, 5, 12, .1, .2).getPathIterator()));
        assertFalse(this.shape.equalsToPathIterator(createRoundRectangle(5, 8, 5, 0, .1, .2).getPathIterator()));
        assertFalse(this.shape.equalsToPathIterator(createSegment(5, 8, 5, 10).getPathIterator()));
        assertTrue(this.shape.equalsToPathIterator(this.shape.getPathIterator()));
        assertTrue(this.shape.equalsToPathIterator(createRoundRectangle(5, 8, 5, 10, .1, .2).getPathIterator()));
    }

    @Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
    public void equalsToShape(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
        assertFalse(this.shape.equalsToShape(null));
        assertFalse(this.shape.equalsToShape((T) createRoundRectangle(0, 8, 5, 12, .1, .2)));
        assertFalse(this.shape.equalsToShape((T) createRoundRectangle(5, 8, 5, 0, .1, .2)));
        assertTrue(this.shape.equalsToShape(this.shape));
        assertTrue(this.shape.equalsToShape((T) createRoundRectangle(5, 8, 5, 10, .1, .2)));
    }

    @Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
    public void containsDoubleDouble(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
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
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
    public void containsPoint2D(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
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
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
    public void getClosestPointTo(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
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
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
    public void getFarthestPointTo(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
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
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
    public void getDistance(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
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
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
    public void getDistanceSquared(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
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
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
    public void getDistanceL1(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
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
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
    public void getDistanceLinf(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
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
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
    public void setIT(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
        this.shape.set((T) createRoundRectangle(10, 20, 30, 40, 1, 2));
        assertEpsilonEquals(10, this.shape.getMinX());
        assertEpsilonEquals(20, this.shape.getMinY());
        assertEpsilonEquals(40, this.shape.getMaxX());
        assertEpsilonEquals(60, this.shape.getMaxY());
        assertEpsilonEquals(1, this.shape.getArcWidth());
        assertEpsilonEquals(2, this.shape.getArcHeight());
    }

    @Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
    public void getPathIterator(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
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
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
    public void getPathIteratorTransform2D(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
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
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
    public void createTransformedShape(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
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
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
    public void containsRectangle2afp(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
        assertFalse(this.shape.contains(createRectangle(0, 0, 1, 1)));
        assertFalse(this.shape.contains(createRectangle(0, 0, 7, 10)));
        assertFalse(this.shape.contains(createRectangle(0, 0, 20, 20)));
        assertTrue(this.shape.contains(createRectangle(6, 10, 1, 1)));
        assertFalse(this.shape.contains(createRectangle(5, 8, 5, 10)));
        assertTrue(this.shape.contains(createRectangle(5.5, 8.5, 4, 9)));
    }

    @Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
    public void containsShape2D(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
        assertFalse(this.shape.contains(createCircle(0, 0, 1)));
        assertFalse(this.shape.contains(createCircle(0, 0, 7)));
        assertFalse(this.shape.contains(createCircle(0, 0, 20)));
        assertTrue(this.shape.contains(createCircle(6, 10, 1)));
        assertFalse(this.shape.contains(createCircle(5, 8, 5)));
        assertFalse(this.shape.contains(createCircle(5.5, 8.5, 4)));
    }

    @Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
    public void intersectsRectangle2afp(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
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
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
    public void intersectsCircle2afp(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
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
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
    public void intersectsEllipse2afp(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
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
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
    public void intersectsSegment2afp(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
        assertFalse(this.shape.intersects(createSegment(0, 0, 1, 1)));
        assertFalse(this.shape.intersects(createSegment(20, 20, 21, 21)));
        assertTrue(this.shape.intersects(createSegment(0, 0, 7, 12)));
        assertFalse(this.shape.intersects(createSegment(0, 0, 7, 8)));
        assertFalse(this.shape.intersects(createSegment(6, 7, 4.1, 9)));
        assertTrue(this.shape.intersects(createSegment(6.1, 7, 4.1, 9)));
    }

    @Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
    public void intersectsTriangle2afp(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
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
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
    public void intersectsPath2afp(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
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
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
    public void intersectsPathIterator2afp(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
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
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
    public void intersectsOrientedRectangle2afp(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
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

    @ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
    @Override
    public void intersectsParallelogram2afp(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
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
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
    public void intersectsRoundRectangle2afp(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
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
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
    public void inflate(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
        this.shape.inflate(1, 2, 3, 4);
        assertEpsilonEquals(4, this.shape.getMinX());
        assertEpsilonEquals(6, this.shape.getMinY());
        assertEpsilonEquals(13, this.shape.getMaxX());
        assertEpsilonEquals(22, this.shape.getMaxY());
    }

    @Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
    public void intersectsShape2D(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
        assertTrue(this.shape.intersects((Shape2D) createCircle(4.1, 12, 1)));
        assertTrue(this.shape.intersects((Shape2D) createEllipse(5, 11.5, 2, 1)));
    }

    @Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
    public void operator_addVector2D(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
        this.shape.operator_add(createVector(123.456, 456.789));
        assertEpsilonEquals(128.456, this.shape.getMinX());
        assertEpsilonEquals(464.789, this.shape.getMinY());
        assertEpsilonEquals(133.456, this.shape.getMaxX());
        assertEpsilonEquals(474.789, this.shape.getMaxY());
    }

    @Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
    public void operator_plusVector2D(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
        T shape = this.shape.operator_plus(createVector(123.456, 456.789));
        assertEpsilonEquals(128.456, shape.getMinX());
        assertEpsilonEquals(464.789, shape.getMinY());
        assertEpsilonEquals(133.456, shape.getMaxX());
        assertEpsilonEquals(474.789, shape.getMaxY());
    }

    @Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
    public void operator_removeVector2D(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
        this.shape.operator_remove(createVector(123.456, 456.789));
        assertEpsilonEquals(-118.456, this.shape.getMinX());
        assertEpsilonEquals(-448.789, this.shape.getMinY());
        assertEpsilonEquals(-113.456, this.shape.getMaxX());
        assertEpsilonEquals(-438.789, this.shape.getMaxY());
    }

    @Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
    public void operator_minusVector2D(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
        T shape = this.shape.operator_minus(createVector(123.456, 456.789));
        assertEpsilonEquals(-118.456, shape.getMinX());
        assertEpsilonEquals(-448.789, shape.getMinY());
        assertEpsilonEquals(-113.456, shape.getMaxX());
        assertEpsilonEquals(-438.789, shape.getMaxY());
    }

    @Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
    public void operator_multiplyTransform2D(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
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
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
    public void operator_andPoint2D(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
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
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
    public void operator_andShape2D(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
        assertTrue(this.shape.operator_and(createCircle(4.1, 12, 1)));
        assertTrue(this.shape.operator_and(createEllipse(5, 11.5, 2, 1)));
    }

    @Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
    public void operator_upToPoint2D(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
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

    @Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
    public void getClosestPointToCircle2afp(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
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
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
    public void getDistanceSquaredCircle2afp(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
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
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
    public void getClosestPointToSegment2afp(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
        assertFpPointEquals(5.0721, 8.00794, this.shape.getClosestPointTo(createSegment(0, 0, 1, 1)));
        assertFpPointEquals(5.06108, 8.01572, this.shape.getClosestPointTo(createSegment(0, 1, 1, 0)));
        assertFpPointEquals(8, 8, this.shape.getClosestPointTo(createSegment(6, 4, 8, 5)));
        assertClosestPointInBothShapes(this.shape, createSegment(4, 14, 6, 15));
        assertClosestPointInBothShapes(this.shape, createSegment(7, 9, 9, 10));
        assertFpPointEquals(9.90811, 8.00066, this.shape.getClosestPointTo(createSegment(
                9.315811794580389, 7.677476922530425, 11.315811794580389, 8.677476922530425)));
    }

    @Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
    public void getDistanceSquaredSegment2afp(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
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
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
    public void getClosestPointToTriangle2afp(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
        assertFpPointEquals(5.1, 8.0, this.shape.getClosestPointTo(createTestTriangle(0, 0)));
        assertFpPointEquals(10, 16, this.shape.getClosestPointTo(createTestTriangle(14, 16)));
        assertFpPointEquals(5.02929, 17.94142, this.shape.getClosestPointTo(createTestTriangle(3, 20)));
        assertClosestPointInBothShapes(this.shape, createTestTriangle(7, 19));
        assertClosestPointInBothShapes(this.shape, createTestTriangle(4, 14));
        assertClosestPointInBothShapes(this.shape, createTestTriangle(6, 10));
    }

    @Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
    public void getDistanceSquaredTriangle2afp(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
        assertEpsilonEquals(105.11999, this.shape.getDistanceSquared(createTestTriangle(0, 0)));
        assertEpsilonEquals(16, this.shape.getDistanceSquared(createTestTriangle(14, 16)));
        assertEpsilonEquals(0.49044, this.shape.getDistanceSquared(createTestTriangle(3, 20)));
        assertEpsilonEquals(0, this.shape.getDistanceSquared(createTestTriangle(7, 19)));
        assertEpsilonEquals(0, this.shape.getDistanceSquared(createTestTriangle(4, 14)));
        assertEpsilonEquals(0, this.shape.getDistanceSquared(createTestTriangle(6, 10)));
    }

    @Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
    public void getClosestPointToRectangle2afp(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
        assertFpPointEquals(5.1, 8.0, this.shape.getClosestPointTo(createRectangle(0, 0, 3, 2)));
        assertFpPointEquals(5, 12, this.shape.getClosestPointTo(createRectangle(-1, 12, 3, 2)));
        assertFpPointEquals(5.1, 18, this.shape.getClosestPointTo(createRectangle(1, 20, 3, 2)));
        assertClosestPointInBothShapes(this.shape, createRectangle(8.5, 8.5, 3, 2));
        assertClosestPointInBothShapes(this.shape, createRectangle(6, 10, 3, 2));
    }

    @Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
    public void getDistanceSquaredRectangle2afp(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
        assertEpsilonEquals(40.40999, this.shape.getDistanceSquared(createRectangle(0, 0, 3, 2)));
        assertEpsilonEquals(9, this.shape.getDistanceSquared(createRectangle(-1, 12, 3, 2)));
        assertEpsilonEquals(5.20999, this.shape.getDistanceSquared(createRectangle(1, 20, 3, 2)));
        assertEpsilonEquals(0, this.shape.getDistanceSquared(createRectangle(8.5, 8.5, 3, 2)));
        assertEpsilonEquals(0, this.shape.getDistanceSquared(createRectangle(6, 10, 3, 2)));
    }

    @Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
    public void getClosestPointToEllipse2afp(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
        assertFpPointEquals(5.1, 8.0, this.shape.getClosestPointTo(createEllipse(0, 0, 3, 2)));
        assertFpPointEquals(5, 13, this.shape.getClosestPointTo(createEllipse(-1, 12, 3, 2)));
        assertFpPointEquals(5.1, 18, this.shape.getClosestPointTo(createEllipse(1, 20, 3, 2)));
        assertClosestPointInBothShapes(this.shape, createEllipse(8.5, 8.5, 3, 2));
        assertClosestPointInBothShapes(this.shape, createEllipse(6, 10, 3, 2));
    }

    @Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
    public void getDistanceSquaredEllipse2afp(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
        assertEpsilonEquals(45.70503, this.shape.getDistanceSquared(createEllipse(0, 0, 3, 2)));
        assertEpsilonEquals(9, this.shape.getDistanceSquared(createEllipse(-1, 12, 3, 2)));
        assertEpsilonEquals(7.6269, this.shape.getDistanceSquared(createEllipse(1, 20, 3, 2)));
        assertEpsilonEquals(0, this.shape.getDistanceSquared(createEllipse(8.5, 8.5, 3, 2)));
        assertEpsilonEquals(0, this.shape.getDistanceSquared(createEllipse(6, 10, 3, 2)));
    }

    @Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
    public void getClosestPointToRoundRectangle2afp(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
        assertFpPointEquals(5.1, 8.0, this.shape.getClosestPointTo(createRoundRectangle(0, 0, 3, 2, .2, .1)));
        assertFpPointEquals(5, 12.1, this.shape.getClosestPointTo(createRoundRectangle(-1, 12, 3, 2, .2, .1)));
        assertFpPointEquals(5.1, 18.0, this.shape.getClosestPointTo(createRoundRectangle(1, 20, 3, 2, .2, .1)));
        assertClosestPointInBothShapes(this.shape, createRoundRectangle(8.5, 8.5, 3, 2, .2, .1));
        assertClosestPointInBothShapes(this.shape, createRoundRectangle(6, 10, 3, 2, .2, .1));
    }

    @Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
    public void getDistanceSquaredRoundRectangle2afp(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
        assertEpsilonEquals(40.9926, this.shape.getDistanceSquared(createRoundRectangle(0, 0, 3, 2, .2, .1)));
        assertEpsilonEquals(9, this.shape.getDistanceSquared(createRoundRectangle(-1, 12, 3, 2, .2, .1)));
        assertEpsilonEquals(5.45907, this.shape.getDistanceSquared(createRoundRectangle(1, 20, 3, 2, .2, .1)));
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
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
    public void getClosestPointToPath2afp(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
        assertFpPointEquals(5.1, 8.0, this.shape.getClosestPointTo(createNonEmptyPath(0, 0)));
        assertFpPointEquals(5, 12.5, this.shape.getClosestPointTo(createNonEmptyPath(2, 12)));
        assertClosestPointInBothShapes(this.shape, createNonEmptyPath(9.75, 14));
        assertClosestPointInBothShapes(this.shape, createNonEmptyPath(7, 9));
    }

    @Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
    public void getDistanceSquaredPath2afp(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
        assertEpsilonEquals(73.05999, this.shape.getDistanceSquared(createNonEmptyPath(0, 0)));
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
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
    public void getClosestPointToMultiShape2afp(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
        assertFpPointEquals(5.04453, 8.03359, this.shape.getClosestPointTo(createTestMultiShape(0, 0)));
        assertFpPointEquals(5, 12.5, this.shape.getClosestPointTo(createTestMultiShape(2, 12)));
        assertClosestPointInBothShapes(this.shape, createTestMultiShape(9.75, 14));
        assertClosestPointInBothShapes(this.shape, createTestMultiShape(7, 9));
    }

    @Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
    public void getDistanceSquaredMultiShape2afp(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
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
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
    public void getClosestPointToParallelogram2afp(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
        assertFpPointEquals(5.1, 8.0, this.shape.getClosestPointTo(createTestParallelogram(0, 0)));
        assertFpPointEquals(5, 13.19218, this.shape.getClosestPointTo(createTestParallelogram(2, 12)));
        assertClosestPointInBothShapes(this.shape, createTestParallelogram(9.75, 14));
        assertClosestPointInBothShapes(this.shape, createTestParallelogram(7, 9));
    }

    @Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
    public void getDistanceSquaredParallelogram2afp(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
        assertEpsilonEquals(52.36173, this.shape.getDistanceSquared(createTestParallelogram(0, 0)));
        assertEpsilonEquals(0.12433, this.shape.getDistanceSquared(createTestParallelogram(2, 12)));
        assertEpsilonEquals(0, this.shape.getDistanceSquared(createTestParallelogram(9.75, 14)));
        assertEpsilonEquals(0, this.shape.getDistanceSquared(createTestParallelogram(7, 9)));
    }
    
    protected OrientedRectangle2afp createTestOrientedRectangle(double dx, double dy) {
        Vector2D r = createVector(4, 1).toUnitVector();
        return createOrientedRectangle(dx, dy, r.getX(), r.getY(), 2, 1);
    }

    @Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
    public void getClosestPointToOrientedRectangle2afp(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
        assertFpPointEquals(5.1, 8.0, this.shape.getClosestPointTo(createTestOrientedRectangle(0, 0)));
        assertFpPointEquals(5, 11.51493, this.shape.getClosestPointTo(createTestOrientedRectangle(2, 12)));
        assertClosestPointInBothShapes(this.shape, createTestOrientedRectangle(9.75, 14));
        assertClosestPointInBothShapes(this.shape, createTestOrientedRectangle(7, 9));
    }

    @Override
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem2D.class)
    public void getDistanceSquaredOrientedRectangle2afp(CoordinateSystem2D cs) {
    	CoordinateSystem2D.setDefaultCoordinateSystem(cs);
        assertEpsilonEquals(54.40957, this.shape.getDistanceSquared(createTestOrientedRectangle(0, 0)));
        assertEpsilonEquals(0.66778, this.shape.getDistanceSquared(createTestOrientedRectangle(2, 12)));
        assertEpsilonEquals(0, this.shape.getDistanceSquared(createTestOrientedRectangle(9.75, 14)));
        assertEpsilonEquals(0, this.shape.getDistanceSquared(createTestOrientedRectangle(7, 9)));
    }

}