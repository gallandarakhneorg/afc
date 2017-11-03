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

package org.arakhne.afc.math.extensions.xtext;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.matrix.Matrix2d;
import org.arakhne.afc.math.matrix.Matrix3d;
import org.arakhne.afc.math.matrix.Matrix4d;
import org.arakhne.afc.vmutil.annotations.XtextOperator;
import org.arakhne.afc.vmutil.asserts.AssertMessages;

/** Xtext extensions for matrices.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public final class MatrixExtensions {

    private MatrixExtensions() {
        //
    }

    /** Replies the addition of the given scalar to this matrix: {@code left + right}.
     *
     * <p>This function is an implementation of the operator for
     * the languages that defined or based on the
     * <a href="https://www.eclipse.org/Xtext/">Xtext framework</a>.
     *
     * <p>The operation {@code right + left} is supported by {@link Matrix2d#operator_plus(double)}.
     *
     * @param <M> the type of the matrix.
     * @param left the scalar.
     * @param right the matrix.
     * @return the sum of the matrix and the scalar.
     * @see Matrix2d#add(double)
     * @see Matrix2d#operator_plus(double)
     */
    @Pure
    @XtextOperator("+")
    @SuppressWarnings("unchecked")
    public static <M extends Matrix2d> M operator_plus(double left, M right) {
        assert right != null : AssertMessages.notNullParameter(1);
        final M result = (M) right.clone();
        result.add(left, right);
        return result;
    }

    /** Replies the addition of the given scalar to this matrix: {@code left + right}.
     *
     * <p>This function is an implementation of the operator for
     * the languages that defined or based on the
     * <a href="https://www.eclipse.org/Xtext/">Xtext framework</a>.
     *
     * <p>The operation {@code right + left} is supported by {@link Matrix3d#operator_plus(double)}.
     *
     * @param <M> the type of the matrix.
     * @param left the scalar.
     * @param right the matrix.
     * @return the sum of the matrix and the scalar.
     * @see Matrix3d#add(double)
     * @see Matrix3d#operator_plus(double)
     */
    @Pure
    @XtextOperator("+")
    @SuppressWarnings("unchecked")
    public static <M extends Matrix3d> M operator_plus(double left, M right) {
        assert right != null : AssertMessages.notNullParameter(1);
        final M result = (M) right.clone();
        result.add(left, right);
        return result;
    }

    /** Replies the addition of the given scalar to this matrix: {@code left + right}.
     *
     * <p>This function is an implementation of the operator for
     * the languages that defined or based on the
     * <a href="https://www.eclipse.org/Xtext/">Xtext framework</a>.
     *
     * <p>The operation {@code right + left} is supported by {@link Matrix4d#operator_plus(double)}.
     *
     * @param <M> the type of the matrix.
     * @param left the scalar.
     * @param right the matrix.
     * @return the sum of the matrix and the scalar.
     * @see Matrix4d#add(double)
     * @see Matrix4d#operator_plus(double)
     */
    @Pure
    @XtextOperator("+")
    @SuppressWarnings("unchecked")
    public static <M extends Matrix4d> M operator_plus(double left, M right) {
        assert right != null : AssertMessages.notNullParameter(1);
        final M result = (M) right.clone();
        result.add(left, right);
        return result;
    }

    /** Replies the substraction of the given scalar to this matrix: {@code left - right}.
     *
     * <p>This function is an implementation of the operator for
     * the languages that defined or based on the
     * <a href="https://www.eclipse.org/Xtext/">Xtext framework</a>.
     *
     * <p>The operation {@code right - left} is supported by {@link Matrix2d#operator_minus(double)}.
     *
     * @param <M> the type of the matrix.
     * @param left the scalar.
     * @param right the matrix.
     * @return the sum of the matrix and the scalar.
     * @see Matrix2d#add(double)
     * @see Matrix2d#operator_minus(double)
     */
    @Pure
    @XtextOperator("-")
    @SuppressWarnings("unchecked")
    public static <M extends Matrix2d> M operator_minus(double left, M right) {
        assert right != null : AssertMessages.notNullParameter(1);
        final M result = (M) right.clone();
        result.set(
                left - right.getM00(),
                left - right.getM01(),
                left - right.getM10(),
                left - right.getM11());
        return result;
    }

    /** Replies the substraction of the given scalar to this matrix: {@code left - right}.
     *
     * <p>This function is an implementation of the operator for
     * the languages that defined or based on the
     * <a href="https://www.eclipse.org/Xtext/">Xtext framework</a>.
     *
     * <p>The operation {@code right - left} is supported by {@link Matrix3d#operator_minus(double)}.
     *
     * @param <M> the type of the matrix.
     * @param left the scalar.
     * @param right the matrix.
     * @return the sum of the matrix and the scalar.
     * @see Matrix3d#add(double)
     * @see Matrix3d#operator_minus(double)
     */
    @Pure
    @XtextOperator("-")
    @SuppressWarnings("unchecked")
    public static <M extends Matrix3d> M operator_minus(double left, M right) {
        assert right != null : AssertMessages.notNullParameter(1);
        final M result = (M) right.clone();
        result.set(
                left - right.getM00(),
                left - right.getM01(),
                left - right.getM02(),
                left - right.getM10(),
                left - right.getM11(),
                left - right.getM12(),
                left - right.getM20(),
                left - right.getM21(),
                left - right.getM22());
        return result;
    }

    /** Replies the substraction of the given scalar to this matrix: {@code left - right}.
     *
     * <p>This function is an implementation of the operator for
     * the languages that defined or based on the
     * <a href="https://www.eclipse.org/Xtext/">Xtext framework</a>.
     *
     * <p>The operation {@code right - left} is supported by {@link Matrix4d#operator_minus(double)}.
     *
     * @param <M> the type of the matrix.
     * @param left the scalar.
     * @param right the matrix.
     * @return the sum of the matrix and the scalar.
     * @see Matrix4d#add(double)
     * @see Matrix4d#operator_minus(double)
     */
    @Pure
    @XtextOperator("-")
    @SuppressWarnings("unchecked")
    public static <M extends Matrix4d> M operator_minus(double left, M right) {
        assert right != null : AssertMessages.notNullParameter(1);
        final M result = (M) right.clone();
        result.set(
                left - right.getM00(),
                left - right.getM01(),
                left - right.getM02(),
                left - right.getM03(),
                left - right.getM10(),
                left - right.getM11(),
                left - right.getM12(),
                left - right.getM13(),
                left - right.getM20(),
                left - right.getM21(),
                left - right.getM22(),
                left - right.getM23(),
                left - right.getM30(),
                left - right.getM31(),
                left - right.getM32(),
                left - right.getM33());
        return result;
    }


    /** Replies the multiplication of the given scalar and this matrix: {@code left * right}.
     *
     * <p>This function is an implementation of the operator for
     * the languages that defined or based on the
     * <a href="https://www.eclipse.org/Xtext/">Xtext framework</a>.
     *
     * <p>The operation {@code right * left} is supported by {@link Matrix2d#operator_multiply(double)}.
     *
     * @param <M> the type of the matrix.
     * @param left the scalar.
     * @param right the matrix.
     * @return the multiplication of the scalar and the matrix.
     * @see Matrix2d#mul(double)
     * @see Matrix2d#operator_multiply(double)
     */
    @Pure
    @XtextOperator("*")
    @SuppressWarnings("unchecked")
    public static <M extends Matrix2d> M operator_multiply(double left, M right) {
        assert right != null : AssertMessages.notNullParameter(1);
        final M result = (M) right.clone();
        result.mul(left, right);
        return result;
    }

    /** Replies the multiplication of the given scalar and this matrix: {@code left * right}.
     *
     * <p>This function is an implementation of the operator for
     * the languages that defined or based on the
     * <a href="https://www.eclipse.org/Xtext/">Xtext framework</a>.
     *
     * <p>The operation {@code right * left} is supported by {@link Matrix3d#operator_multiply(double)}.
     *
     * @param <M> the type of the matrix.
     * @param left the scalar.
     * @param right the matrix.
     * @return the multiplication of the scalar and the matrix.
     * @see Matrix3d#mul(double)
     * @see Matrix3d#operator_multiply(double)
     */
    @Pure
    @XtextOperator("*")
    @SuppressWarnings("unchecked")
    public static <M extends Matrix3d> M operator_multiply(double left, M right) {
        assert right != null : AssertMessages.notNullParameter(1);
        final M result = (M) right.clone();
        result.mul(left, right);
        return result;
    }

    /** Replies the multiplication of the given scalar and this matrix: {@code left * right}.
     *
     * <p>This function is an implementation of the operator for
     * the languages that defined or based on the
     * <a href="https://www.eclipse.org/Xtext/">Xtext framework</a>.
     *
     * <p>The operation {@code right * left} is supported by {@link Matrix4d#operator_multiply(double)}.
     *
     * @param <M> the type of the matrix.
     * @param left the scalar.
     * @param right the matrix.
     * @return the multiplication of the scalar and the matrix.
     * @see Matrix4d#mul(double)
     * @see Matrix4d#operator_multiply(double)
     */
    @Pure
    @XtextOperator("*")
    @SuppressWarnings("unchecked")
    public static <M extends Matrix4d> M operator_multiply(double left, M right) {
        assert right != null : AssertMessages.notNullParameter(1);
        final M result = (M) right.clone();
        result.mul(left, right);
        return result;
    }


    /** Replies the division of this matrix by the given scalar: {@code left / right}.
     *
     * <p>This function is an implementation of the operator for
     * the languages that defined or based on the
     * <a href="https://www.eclipse.org/Xtext/">Xtext framework</a>.
     *
     * <p>The operation {@code right / left} is supported by {@link Matrix2d#operator_divide(double)}.
     *
     * @param <M> the type of the matrix.
     * @param left the scalar.
     * @param right the matrix.
     * @return the division of the matrix by the scalar.
     * @see Matrix2d#mul(double)
     * @see Matrix2d#operator_divide(double)
     */
    @Pure
    @XtextOperator("/")
    @SuppressWarnings("unchecked")
    public static <M extends Matrix2d> M operator_divide(double left, M right) {
        assert right != null : AssertMessages.notNullParameter(1);
        final M result = (M) right.clone();
        result.set(
                left / right.getM00(),
                left / right.getM01(),
                left / right.getM10(),
                left / right.getM11());
        return result;
    }

    /** Replies the division of this matrix by the given scalar: {@code left / right}.
     *
     * <p>This function is an implementation of the operator for
     * the languages that defined or based on the
     * <a href="https://www.eclipse.org/Xtext/">Xtext framework</a>.
     *
     * <p>The operation {@code right / left} is supported by {@link Matrix3d#operator_divide(double)}.
     *
     * @param <M> the type of the matrix.
     * @param left the scalar.
     * @param right the matrix.
     * @return the division of the matrix by the scalar.
     * @see Matrix3d#mul(double)
     * @see Matrix3d#operator_divide(double)
     */
    @Pure
    @XtextOperator("/")
    @SuppressWarnings("unchecked")
    public static <M extends Matrix3d> M operator_divide(double left, M right) {
        assert right != null : AssertMessages.notNullParameter(1);
        final M result = (M) right.clone();
        result.set(
                left / right.getM00(),
                left / right.getM01(),
                left / right.getM02(),
                left / right.getM10(),
                left / right.getM11(),
                left / right.getM12(),
                left / right.getM20(),
                left / right.getM21(),
                left / right.getM22());
        return result;
    }

    /** Replies the division of this matrix by the given scalar: {@code left / right}.
     *
     * <p>This function is an implementation of the operator for
     * the languages that defined or based on the
     * <a href="https://www.eclipse.org/Xtext/">Xtext framework</a>.
     *
     * <p>The operation {@code right / left} is supported by {@link Matrix4d#operator_divide(double)}.
     *
     * @param <M> the type of the matrix.
     * @param left the scalar.
     * @param right the matrix.
     * @return the division of the matrix by the scalar.
     * @see Matrix4d#mul(double)
     * @see Matrix4d#operator_divide(double)
     */
    @Pure
    @XtextOperator("/")
    @SuppressWarnings("unchecked")
    public static <M extends Matrix4d> M operator_divide(double left, M right) {
        assert right != null : AssertMessages.notNullParameter(1);
        final M result = (M) right.clone();
        result.set(
                left / right.getM00(),
                left / right.getM01(),
                left / right.getM02(),
                left / right.getM03(),
                left / right.getM10(),
                left / right.getM11(),
                left / right.getM12(),
                left / right.getM13(),
                left / right.getM20(),
                left / right.getM21(),
                left / right.getM22(),
                left / right.getM23(),
                left / right.getM30(),
                left / right.getM31(),
                left / right.getM32(),
                left / right.getM33());
        return result;
    }

}
