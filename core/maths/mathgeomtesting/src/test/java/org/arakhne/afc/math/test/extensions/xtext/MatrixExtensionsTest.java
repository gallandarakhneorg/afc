/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2023 The original authors and other contributors.
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

import org.arakhne.afc.math.extensions.xtext.MatrixExtensions;
import org.arakhne.afc.math.matrix.Matrix2d;
import org.arakhne.afc.math.matrix.Matrix3d;
import org.arakhne.afc.math.matrix.Matrix4d;
import org.arakhne.afc.math.test.AbstractMathTestCase;

@SuppressWarnings("all")
public class MatrixExtensionsTest extends AbstractMathTestCase {

    @Test
    public void operator_plusDoubleMatrix2d() {
        Matrix2d source = new Matrix2d(1, 2, 3, 4);
        Matrix2d expected = new Matrix2d(2.5, 3.5, 4.5, 5.5);
        assertEpsilonEquals(expected, MatrixExtensions.operator_plus(1.5, source));
    }

    @Test
    public void operator_plusDoubleMatrix3d() {
        Matrix3d source = new Matrix3d(1, 2, 3, 4, 5, 6, 7, 8, 9);
        Matrix3d expected = new Matrix3d(2.5, 3.5, 4.5, 5.5, 6.5, 7.5, 8.5, 9.5, 10.5);
        assertEpsilonEquals(expected, MatrixExtensions.operator_plus(1.5, source));
    }

    @Test
    public void operator_plusDoubleMatrix4d() {
        Matrix4d source = new Matrix4d(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16);
        Matrix4d expected = new Matrix4d(2.5, 3.5, 4.5, 5.5, 6.5, 7.5, 8.5, 9.5, 10.5, 11.5, 12.5, 13.5, 14.5, 15.5, 16.5, 17.5);
        assertEpsilonEquals(expected, MatrixExtensions.operator_plus(1.5, source));
    }

    @Test
    public void operator_minusDoubleMatrix2d() {
        Matrix2d source = new Matrix2d(1, 2, 3, 4);
        Matrix2d expected = new Matrix2d(.5, -.5, -1.5, -2.5);
        assertEpsilonEquals(expected, MatrixExtensions.operator_minus(1.5, source));
    }

    @Test
    public void operator_minusDoubleMatrix3d() {
        Matrix3d source = new Matrix3d(1, 2, 3, 4, 5, 6, 7, 8, 9);
        Matrix3d expected = new Matrix3d(.5, -.5, -1.5, -2.5, -3.5, -4.5, -5.5, -6.5, -7.5);
        assertEpsilonEquals(expected, MatrixExtensions.operator_minus(1.5, source));
    }

    @Test
    public void operator_minusDoubleMatrix4d() {
        Matrix4d source = new Matrix4d(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16);
        Matrix4d expected = new Matrix4d(.5, -.5, -1.5, -2.5, -3.5, -4.5, -5.5, -6.5, -7.5, -8.5, -9.5,
                -10.5, -11.5, -12.5, -13.5, -14.5);
        assertEpsilonEquals(expected, MatrixExtensions.operator_minus(1.5, source));
    }

    @Test
    public void operator_multiplyDoubleMatrix2d() {
        Matrix2d source = new Matrix2d(1, 2, 3, 4);
        Matrix2d expected = new Matrix2d(1.5, 3, 4.5, 6);
        assertEpsilonEquals(expected, MatrixExtensions.operator_multiply(1.5, source));
    }

    @Test
    public void operator_multiplyDoubleMatrix3d() {
        Matrix3d source = new Matrix3d(1, 2, 3, 4, 5, 6, 7, 8, 9);
        Matrix3d expected = new Matrix3d(1.5, 3, 4.5, 6, 7.5, 9, 10.5, 12, 13.5);
        assertEpsilonEquals(expected, MatrixExtensions.operator_multiply(1.5, source));
    }

    @Test
    public void operator_multiplyDoubleMatrix4d() {
        Matrix4d source = new Matrix4d(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16);
        Matrix4d expected = new Matrix4d(1.5, 3, 4.5, 6, 7.5, 9, 10.5, 12, 13.5, 15, 16.5, 18, 19.5, 21, 22.5, 24);
        assertEpsilonEquals(expected, MatrixExtensions.operator_multiply(1.5, source));
    }

    @Test
    public void operator_divideDoubleMatrix2d() {
        Matrix2d source = new Matrix2d(1, 2, 3, 4);
        Matrix2d expected = new Matrix2d(1.5, .75, .5, .375);
        assertEpsilonEquals(expected, MatrixExtensions.operator_divide(1.5, source));
    }

    @Test
    public void operator_divideDoubleMatrix3d() {
        Matrix3d source = new Matrix3d(1, 2, 3, 4, 5, 6, 7, 8, 9);
        Matrix3d expected = new Matrix3d(1.5, .75, .5, .375, .3, .25, .21429, .1875, .16667);
        assertEpsilonEquals(expected, MatrixExtensions.operator_divide(1.5, source));
    }

    @Test
    public void operator_divideDoubleMatrix4d() {
        Matrix4d source = new Matrix4d(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16);
        Matrix4d expected = new Matrix4d(1.5, .75, .5, .375, .3, .25, .21429, .1875, .16667, .15,
                .136364, .125, .11538, .107143, .1, .09375);
        assertEpsilonEquals(expected, MatrixExtensions.operator_divide(1.5, source));
    }

}
