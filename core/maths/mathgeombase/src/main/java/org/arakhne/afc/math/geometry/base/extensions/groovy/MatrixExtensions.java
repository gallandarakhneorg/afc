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

package org.arakhne.afc.math.geometry.base.extensions.groovy;

import org.arakhne.afc.math.geometry.base.matrix.Matrix2d;
import org.arakhne.afc.math.geometry.base.matrix.Matrix3d;
import org.arakhne.afc.math.geometry.base.matrix.Matrix4d;
import org.arakhne.afc.vmutil.annotations.GroovyOperator;
import org.arakhne.afc.vmutil.asserts.AssertMessages;
import org.eclipse.xtext.xbase.lib.Pure;

/** Groovy extensions for matrices.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 18.0
 */
public final class MatrixExtensions {

    private MatrixExtensions() {
        //
    }

    /** Replies the addition of the given scalar to this matrix: {@code left + right}.
     *
	 * <p>This function is an implementation of the operator for
	 * the languages that defined or based on <a href="http://groovy-lang.org/">Groovy</a>.
     *
     * <p>The operation {@code right + left} is supported by {@link Matrix2d#plus(double)}.
     *
     * @param <M> the type of the matrix.
     * @param left the scalar.
     * @param right the matrix.
     * @return the sum of the matrix and the scalar.
     * @see Matrix2d#add(double)
     * @see Matrix2d#plus(double)
     */
    @Pure
    @GroovyOperator("+")
    @SuppressWarnings("unchecked")
    public static <M extends Matrix2d> M plus(double left, M right) {
        assert right != null : AssertMessages.notNullParameter(1);
        final var result = (M) right.clone();
        result.add(left, right);
        return result;
    }

    /** Replies the addition of the given scalar to this matrix: {@code left + right}.
     *
	 * <p>This function is an implementation of the operator for
	 * the languages that defined or based on <a href="http://groovy-lang.org/">Groovy</a>.
     *
     * <p>The operation {@code right + left} is supported by {@link Matrix3d#plus(double)}.
     *
     * @param <M> the type of the matrix.
     * @param left the scalar.
     * @param right the matrix.
     * @return the sum of the matrix and the scalar.
     * @see Matrix3d#add(double)
     * @see Matrix3d#plus(double)
     */
    @Pure
    @GroovyOperator("+")
    @SuppressWarnings("unchecked")
    public static <M extends Matrix3d> M plus(double left, M right) {
        assert right != null : AssertMessages.notNullParameter(1);
        final var result = (M) right.clone();
        result.add(left, right);
        return result;
    }

    /** Replies the addition of the given scalar to this matrix: {@code left + right}.
     *
	 * <p>This function is an implementation of the operator for
	 * the languages that defined or based on <a href="http://groovy-lang.org/">Groovy</a>.
     *
     * <p>The operation {@code right + left} is supported by {@link Matrix4d#plus(double)}.
     *
     * @param <M> the type of the matrix.
     * @param left the scalar.
     * @param right the matrix.
     * @return the sum of the matrix and the scalar.
     * @see Matrix4d#add(double)
     * @see Matrix4d#plus(double)
     */
    @Pure
    @GroovyOperator("+")
    @SuppressWarnings("unchecked")
    public static <M extends Matrix4d> M plus(double left, M right) {
        assert right != null : AssertMessages.notNullParameter(1);
        final var result = (M) right.clone();
        result.add(left, right);
        return result;
    }

    /** Replies the subtraction of the given scalar to this matrix: {@code left - right}.
     *
     *
     * <p>The operation {@code right - left} is supported by {@link Matrix2d#minus(double)}.
     *
     * @param <M> the type of the matrix.
     * @param left the scalar.
     * @param right the matrix.
     * @return the sum of the matrix and the scalar.
     * @see Matrix2d#add(double)
     * @see Matrix2d#minus(double)
     */
    @Pure
    @GroovyOperator("-")
    @SuppressWarnings("unchecked")
    public static <M extends Matrix2d> M minus(double left, M right) {
        assert right != null : AssertMessages.notNullParameter(1);
        final var result = (M) right.clone();
        result.set(
                left - right.getM00(),
                left - right.getM01(),
                left - right.getM10(),
                left - right.getM11());
        return result;
    }

    /** Replies the subtraction of the given scalar to this matrix: {@code left - right}.
     *
	 * <p>This function is an implementation of the operator for
	 * the languages that defined or based on <a href="http://groovy-lang.org/">Groovy</a>.
     *
     * <p>The operation {@code right - left} is supported by {@link Matrix3d#minus(double)}.
     *
     * @param <M> the type of the matrix.
     * @param left the scalar.
     * @param right the matrix.
     * @return the sum of the matrix and the scalar.
     * @see Matrix3d#add(double)
     * @see Matrix3d#minus(double)
     */
    @Pure
    @GroovyOperator("-")
    @SuppressWarnings("unchecked")
    public static <M extends Matrix3d> M minus(double left, M right) {
        assert right != null : AssertMessages.notNullParameter(1);
        final var result = (M) right.clone();
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

    /** Replies the subtraction of the given scalar to this matrix: {@code left - right}.
     *
	 * <p>This function is an implementation of the operator for
	 * the languages that defined or based on <a href="http://groovy-lang.org/">Groovy</a>.
     *
     * <p>The operation {@code right - left} is supported by {@link Matrix4d#minus(double)}.
     *
     * @param <M> the type of the matrix.
     * @param left the scalar.
     * @param right the matrix.
     * @return the sum of the matrix and the scalar.
     * @see Matrix4d#add(double)
     * @see Matrix4d#minus(double)
     */
    @Pure
    @GroovyOperator("-")
    @SuppressWarnings("unchecked")
    public static <M extends Matrix4d> M minus(double left, M right) {
        assert right != null : AssertMessages.notNullParameter(1);
        final var result = (M) right.clone();
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
	 * the languages that defined or based on <a href="http://groovy-lang.org/">Groovy</a>.
     *
     * <p>The operation {@code right * left} is supported by {@link Matrix2d#multiply(double)}.
     *
     * @param <M> the type of the matrix.
     * @param left the scalar.
     * @param right the matrix.
     * @return the multiplication of the scalar and the matrix.
     * @see Matrix2d#mul(double)
     * @see Matrix2d#multiply(double)
     */
    @Pure
    @GroovyOperator("*")
    @SuppressWarnings("unchecked")
    public static <M extends Matrix2d> M multiply(double left, M right) {
        assert right != null : AssertMessages.notNullParameter(1);
        final var result = (M) right.clone();
        result.mul(left, right);
        return result;
    }

    /** Replies the multiplication of the given scalar and this matrix: {@code left * right}.
     *
	 * <p>This function is an implementation of the operator for
	 * the languages that defined or based on <a href="http://groovy-lang.org/">Groovy</a>.
     *
     * <p>The operation {@code right * left} is supported by {@link Matrix3d#multiply(double)}.
     *
     * @param <M> the type of the matrix.
     * @param left the scalar.
     * @param right the matrix.
     * @return the multiplication of the scalar and the matrix.
     * @see Matrix3d#mul(double)
     * @see Matrix3d#multiply(double)
     */
    @Pure
    @GroovyOperator("*")
    @SuppressWarnings("unchecked")
    public static <M extends Matrix3d> M multiply(double left, M right) {
        assert right != null : AssertMessages.notNullParameter(1);
        final var result = (M) right.clone();
        result.mul(left, right);
        return result;
    }

    /** Replies the multiplication of the given scalar and this matrix: {@code left * right}.
     *
	 * <p>This function is an implementation of the operator for
	 * the languages that defined or based on <a href="http://groovy-lang.org/">Groovy</a>.
     *
     * <p>The operation {@code right * left} is supported by {@link Matrix4d#multiply(double)}.
     *
     * @param <M> the type of the matrix.
     * @param left the scalar.
     * @param right the matrix.
     * @return the multiplication of the scalar and the matrix.
     * @see Matrix4d#mul(double)
     * @see Matrix4d#multiply(double)
     */
    @Pure
    @GroovyOperator("*")
    @SuppressWarnings("unchecked")
    public static <M extends Matrix4d> M multiply(double left, M right) {
        assert right != null : AssertMessages.notNullParameter(1);
        final var result = (M) right.clone();
        result.mul(left, right);
        return result;
    }


    /** Replies the division of this matrix by the given scalar: {@code left / right}.
     *
	 * <p>This function is an implementation of the operator for
	 * the languages that defined or based on <a href="http://groovy-lang.org/">Groovy</a>.
     *
     * <p>The operation {@code right / left} is supported by {@link Matrix2d#div(double)}.
     *
     * @param <M> the type of the matrix.
     * @param left the scalar.
     * @param right the matrix.
     * @return the division of the matrix by the scalar.
     * @see Matrix2d#mul(double)
     * @see Matrix2d#div(double)
     */
    @Pure
    @GroovyOperator("/")
    @SuppressWarnings("unchecked")
    public static <M extends Matrix2d> M div(double left, M right) {
        assert right != null : AssertMessages.notNullParameter(1);
        final var result = (M) right.clone();
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
	 * the languages that defined or based on <a href="http://groovy-lang.org/">Groovy</a>.
     *
     * <p>The operation {@code right / left} is supported by {@link Matrix3d#div(double)}.
     *
     * @param <M> the type of the matrix.
     * @param left the scalar.
     * @param right the matrix.
     * @return the division of the matrix by the scalar.
     * @see Matrix3d#mul(double)
     * @see Matrix3d#div(double)
     */
    @Pure
    @GroovyOperator("/")
    @SuppressWarnings("unchecked")
    public static <M extends Matrix3d> M div(double left, M right) {
        assert right != null : AssertMessages.notNullParameter(1);
        final var result = (M) right.clone();
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
	 * the languages that defined or based on <a href="http://groovy-lang.org/">Groovy</a>.
     *
     * <p>The operation {@code right / left} is supported by {@link Matrix4d#div(double)}.
     *
     * @param <M> the type of the matrix.
     * @param left the scalar.
     * @param right the matrix.
     * @return the division of the matrix by the scalar.
     * @see Matrix4d#mul(double)
     * @see Matrix4d#div(double)
     */
    @Pure
    @GroovyOperator("/")
    @SuppressWarnings("unchecked")
    public static <M extends Matrix4d> M div(double left, M right) {
        assert right != null : AssertMessages.notNullParameter(1);
        final var result = (M) right.clone();
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
