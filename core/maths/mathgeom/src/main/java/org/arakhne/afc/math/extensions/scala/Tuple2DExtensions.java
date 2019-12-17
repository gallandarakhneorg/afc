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

package org.arakhne.afc.math.extensions.scala;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.geometry.d1.Vector1D;
import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.Vector2D;
import org.arakhne.afc.math.matrix.Matrix2d;
import org.arakhne.afc.vmutil.annotations.ScalaOperator;

/** Xtext extensions for 2D vectors.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public final class Tuple2DExtensions {

	private Tuple2DExtensions() {
		//
	}

	/** Scale this vector: {@code left * right}.
	 *
	 * <p>This function is an implementation of the operator for
	 * the <a href="http://scala-lang.org/">Scala Language</a>.
	 *
	 * <p>The operation {@code right * left} is supported by {@link Matrix2d#$times(double)}.
	 *
	 * @param <V> the type of the vector.
	 * @param left the scaling factor.
	 * @param right the vector.
	 * @return the scaled vector.
	 * @see Vector2D#scale(double)
	 * @see Vector2D#$times(double)
	 */
	@Pure
	@ScalaOperator("*")
	public static <V extends Vector2D<V, ?>> V $times(double left, V right) {
		return org.arakhne.afc.math.extensions.xtext.Tuple2DExtensions.operator_multiply(left, right);
	}

	/** Scale this vector: {@code left * right}.
	 *
	 * <p>This function is an implementation of the operator for
	 * the <a href="http://scala-lang.org/">Scala Language</a>.
	 *
	 * <p>The operation {@code right * left} is supported by {@link Matrix2d#$times(double)}.
	 *
	 * @param <V> the type of the vector.
	 * @param left the scaling factor.
	 * @param right the vector.
	 * @return the scaled vector.
	 * @see Vector2D#scale(double)
	 * @see Vector2D#$times(double)
	 * @since 14.0
	 */
	@Pure
	@ScalaOperator("*")
	public static <V extends Vector1D<V, ?, ?>> V $times(double left, V right) {
		return org.arakhne.afc.math.extensions.xtext.Tuple2DExtensions.operator_multiply(left, right);
	}

	/** Scale this vector: {@code left / right}.
	 *
	 * <p>This function is an implementation of the operator for
	 * the <a href="http://scala-lang.org/">Scala Language</a>.
	 *
	 * <p>The operation {@code right / left} is supported by {@link Matrix2d#$div(double)}.
	 *
	 * @param <V> the type of the vector.
	 * @param left the scaling factor.
	 * @param right the vector.
	 * @return the scaled vector.
	 * @see Vector2D#$div(double)
	 */
	@Pure
	@ScalaOperator("/")
	public static <V extends Vector2D<V, ?>> V $div(double left, V right) {
		return org.arakhne.afc.math.extensions.xtext.Tuple2DExtensions.operator_divide(left, right);
	}

	/** Scale this vector: {@code left / right}.
	 *
	 * <p>This function is an implementation of the operator for
	 * the <a href="http://scala-lang.org/">Scala Language</a>.
	 *
	 * <p>The operation {@code right / left} is supported by {@link Matrix2d#$div(double)}.
	 *
	 * @param <V> the type of the vector.
	 * @param left the scaling factor.
	 * @param right the vector.
	 * @return the scaled vector.
	 * @see Vector2D#$div(double)
	 * @since 14.0
	 */
	@Pure
	@ScalaOperator("/")
	public static <V extends Vector1D<V, ?, ?>> V $div(double left, V right) {
		return org.arakhne.afc.math.extensions.xtext.Tuple2DExtensions.operator_divide(left, right);
	}

	/** Subtract a vector to this scalar: {@code left - right}.
	 *
	 * <p>This function is an implementation of the operator for
	 * the <a href="http://scala-lang.org/">Scala Language</a>.
	 *
	 * <p>The operation {@code right - left} is supported by {@link Vector2D#$minus(double)}.
	 *
	 * @param <V> the type of the vector.
	 * @param left the scaling factor.
	 * @param right the vector.
	 * @return the result.
	 * @see Vector2D#sub(Vector2D)
	 * @see Vector2D#$minus(double)
	 */
	@Pure
	@ScalaOperator("-")
	public static <V extends Vector2D<V, ?>> V $minus(double left, V right) {
		return org.arakhne.afc.math.extensions.xtext.Tuple2DExtensions.operator_minus(left, right);
	}

	/** Subtract a vector to this scalar: {@code left - right}.
	 *
	 * <p>This function is an implementation of the operator for
	 * the <a href="http://scala-lang.org/">Scala Language</a>.
	 *
	 * <p>The operation {@code right - left} is supported by {@link Vector2D#$minus(double)}.
	 *
	 * @param <V> the type of the vector.
	 * @param left the scaling factor.
	 * @param right the vector.
	 * @return the result.
	 * @see Vector2D#sub(Vector2D)
	 * @see Vector2D#$minus(double)
	 * @since 14.0
	 */
	@Pure
	@ScalaOperator("-")
	public static <V extends Vector1D<V, ?, ?>> V $minus(double left, V right) {
		return org.arakhne.afc.math.extensions.xtext.Tuple2DExtensions.operator_minus(left, right);
	}

	/** Subtract the scalar to this point: {@code left - right}.
	 *
	 * <p>This function is an implementation of the operator for
	 * the <a href="http://scala-lang.org/">Scala Language</a>.
	 *
	 * <p>The operation {@code right + left} is supported by {@link Point2D#$minus(double)}.
	 *
	 * @param <P> the type of the point.
	 * @param left the scaling factor.
	 * @param right the vector.
	 * @return the result.
	 * @see Point2D#sub(Point2D, Vector2D)
	 * @see Point2D#$minus(double)
	 */
	@Pure
	@ScalaOperator("-")
	public static <P extends Point2D<P, ?>> P $minus(double left, P right) {
		return org.arakhne.afc.math.extensions.xtext.Tuple2DExtensions.operator_minus(left, right);
	}

	/** Sum of this vector and the given scalar: {@code left + right}.
	 *
	 * <p>This function is an implementation of the operator for
	 * the <a href="http://scala-lang.org/">Scala Language</a>.
	 *
	 * <p>The operation {@code right + left} is supported by {@link Vector2D#$plus(double)}.
	 *
	 * @param <V> the type of the vector.
	 * @param left the scaling factor.
	 * @param right the vector.
	 * @return the result.
	 * @see Vector2D#add(Vector2D, Vector2D)
	 * @see Vector2D#$plus(double)
	 */
	@Pure
	@ScalaOperator("+")
	public static <V extends Vector2D<V, ?>> V $plus(double left, V right) {
		return org.arakhne.afc.math.extensions.xtext.Tuple2DExtensions.operator_plus(left, right);

	}

	/** Sum of this vector and the given scalar: {@code left + right}.
	 *
	 * <p>This function is an implementation of the operator for
	 * the <a href="http://scala-lang.org/">Scala Language</a>.
	 *
	 * <p>The operation {@code right + left} is supported by {@link Vector2D#$plus(double)}.
	 *
	 * @param <V> the type of the vector.
	 * @param left the scaling factor.
	 * @param right the vector.
	 * @return the result.
	 * @see Vector2D#add(Vector2D, Vector2D)
	 * @see Vector2D#$plus(double)
	 * @since 14.0
	 */
	@Pure
	@ScalaOperator("+")
	public static <V extends Vector1D<V, ?, ?>> V $plus(double left, V right) {
		return org.arakhne.afc.math.extensions.xtext.Tuple2DExtensions.operator_plus(left, right);

	}

	/** Sum of this point and a scalar: {@code left + right}.
	 *
	 * <p>This function is an implementation of the operator for
	 * the <a href="http://scala-lang.org/">Scala Language</a>.
	 *
	 * <p>The operation {@code right + left} is supported by {@link Point2D#$plus(double)}.
	 *
	 * @param <P> the type of the point.
	 * @param left the scaling factor.
	 * @param right the vector.
	 * @return the result.
	 * @see Point2D#add(Point2D, Vector2D)
	 * @see Point2D#$plus(double)
	 */
	@Pure
	@ScalaOperator("+")
	public static <P extends Point2D<P, ?>> P $plus(double left, P right) {
		return org.arakhne.afc.math.extensions.xtext.Tuple2DExtensions.operator_plus(left, right);
	}

}
