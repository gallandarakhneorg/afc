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

package org.arakhne.afc.math.test.extensions.xtext;

import org.junit.jupiter.api.Test;

import org.arakhne.afc.math.extensions.xtext.Tuple2DExtensions;
import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.Vector2D;
import org.arakhne.afc.math.geometry.d2.d.Point2d;
import org.arakhne.afc.math.geometry.d2.d.Vector2d;
import org.arakhne.afc.math.test.AbstractMathTestCase;

@SuppressWarnings("all")
public class Tuple2DExtensionsTest extends AbstractMathTestCase {

    @Test
    public void operator_multiplyDoubleVector2D() {
        Vector2D tuple = new Vector2d(3, 2);
        assertFpVectorEquals(4.5, 3, Tuple2DExtensions.operator_multiply(1.5, tuple));
    }

    @Test
    public void operator_divideDoubleVector2D() {
        Vector2D tuple = new Vector2d(3, 2);
        assertFpVectorEquals(.5, .75, Tuple2DExtensions.operator_divide(1.5, tuple));
    }

    @Test
    public void operator_minusDoubleVector2D() {
        Vector2D tuple = new Vector2d(3, 2);
        assertFpVectorEquals(-1.5, -.5, Tuple2DExtensions.operator_minus(1.5, tuple));
    }

    @Test
    public void operator_plusDoubleVector2D() {
        Vector2D tuple = new Vector2d(3, 2);
        assertFpVectorEquals(4.5, 3.5, Tuple2DExtensions.operator_plus(1.5, tuple));
    }

    @Test
    public void operator_minusDoublePoint2D() {
        Point2D tuple = new Point2d(3, 2);
        assertFpPointEquals(-1.5, -.5, Tuple2DExtensions.operator_minus(1.5, tuple));
    }

    @Test
    public void operator_plusDoublePoint2D() {
        Point2D tuple = new Point2d(3, 2);
        assertFpPointEquals(4.5, 3.5, Tuple2DExtensions.operator_plus(1.5, tuple));
    }

}
