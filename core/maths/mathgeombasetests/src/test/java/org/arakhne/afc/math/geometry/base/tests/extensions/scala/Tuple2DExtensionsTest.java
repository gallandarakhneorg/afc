/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2026 The original authors and other contributors.
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

package org.arakhne.afc.math.geometry.base.tests.extensions.scala;

import org.arakhne.afc.math.geometry.base.d2.InnerComputationPoint2D;
import org.arakhne.afc.math.geometry.base.d2.InnerComputationVector2D;
import org.arakhne.afc.math.geometry.base.d2.Point2D;
import org.arakhne.afc.math.geometry.base.d2.Vector2D;
import org.arakhne.afc.math.geometry.base.extensions.scala.Tuple2DExtensions;
import org.arakhne.afc.math.geometry.base.tests.AbstractMathTestCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Tuple2DExtensions")
@SuppressWarnings("all")
public class Tuple2DExtensionsTest extends AbstractMathTestCase {

    @Test
    @DisplayName("double * Vector2D")
    public void multiplyDoubleVector2D() {
        Vector2D tuple = new InnerComputationVector2D(3, 2);
        assertFpVectorEquals(4.5, 3, Tuple2DExtensions.$times(1.5, tuple));
    }

    @Test
    @DisplayName("double / Vector2D")
    public void divideDoubleVector2D() {
        Vector2D tuple = new InnerComputationVector2D(3, 2);
        assertFpVectorEquals(.5, .75, Tuple2DExtensions.$div(1.5, tuple));
    }

    @Test
    @DisplayName("double - Vector2D")
    public void minusDoubleVector2D() {
        Vector2D tuple = new InnerComputationVector2D(3, 2);
        assertFpVectorEquals(-1.5, -.5, Tuple2DExtensions.$minus(1.5, tuple));
    }

    @Test
    @DisplayName("double + Vector2D")
    public void plusDoubleVector2D() {
        Vector2D tuple = new InnerComputationVector2D(3, 2);
        assertFpVectorEquals(4.5, 3.5, Tuple2DExtensions.$plus(1.5, tuple));
    }

    @Test
    @DisplayName("double - Vector2D")
    public void minusDoublePoint2D() {
        Point2D tuple = new InnerComputationPoint2D(3, 2);
        assertFpPointEquals(-1.5, -.5, Tuple2DExtensions.$minus(1.5, tuple));
    }

    @Test
    @DisplayName("double + Point2D")
    public void plusDoublePoint2D() {
        Point2D tuple = new InnerComputationPoint2D(3, 2);
        assertFpPointEquals(4.5, 3.5, Tuple2DExtensions.$plus(1.5, tuple));
    }

}
