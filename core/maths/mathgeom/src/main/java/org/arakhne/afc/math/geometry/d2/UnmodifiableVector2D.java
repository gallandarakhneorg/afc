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

package org.arakhne.afc.math.geometry.d2;

import org.arakhne.afc.vmutil.annotations.XtextOperator;

/** Unmodifiable 2D Vector.
 *
 * @param <RV> is the type of vector that can be returned by this tuple.
 * @param <RP> is the type of point that can be returned by this tuple.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public interface UnmodifiableVector2D<RV extends Vector2D<? super RV, ? super RP>,
        RP extends Point2D<? super RP, ? super RV>> extends UnmodifiableTuple2D<RV>, Vector2D<RV, RP> {

    @Override
    default void add(Vector2D<?, ?> vector1, Vector2D<?, ?> vector2) {
        throw new UnsupportedOperationException();
    }

    @Override
    default void add(Vector2D<?, ?> vector) {
        throw new UnsupportedOperationException();
    }

    @Override
    default void scaleAdd(int scale, Vector2D<?, ?> vector1, Vector2D<?, ?> vector2) {
        throw new UnsupportedOperationException();
    }

    @Override
    default void scaleAdd(double scale, Vector2D<?, ?> vector1, Vector2D<?, ?> vector2) {
        throw new UnsupportedOperationException();
    }

    @Override
    default void scaleAdd(int scale, Vector2D<?, ?> vector) {
        throw new UnsupportedOperationException();
    }

    @Override
    default void scaleAdd(double scale, Vector2D<?, ?> vector) {
        throw new UnsupportedOperationException();
    }

    @Override
    default void sub(Vector2D<?, ?> vector1, Vector2D<?, ?> vector2) {
        throw new UnsupportedOperationException();
    }

    @Override
    default void sub(Point2D<?, ?> point1, Point2D<?, ?> point2) {
        throw new UnsupportedOperationException();
    }

    @Override
    default void sub(Vector2D<?, ?> vector1) {
        throw new UnsupportedOperationException();
    }

    @Override
    default void makeOrthogonal() {
        throw new UnsupportedOperationException();
    }

    @Override
    default void normalize(Vector2D<?, ?> vector) {
        throw new UnsupportedOperationException();
    }

    @Override
    default void normalize() {
        throw new UnsupportedOperationException();
    }

    @Override
    default void turn(double angle) {
        throw new UnsupportedOperationException();
    }

    @Override
    default void turn(double angle, Vector2D<?, ?> vectorToTurn) {
        throw new UnsupportedOperationException();
    }

    @Override
    default void turnLeft(double angle) {
        throw new UnsupportedOperationException();
    }

    @Override
    default void turnLeft(double angle, Vector2D<?, ?> vectorToTurn) {
        throw new UnsupportedOperationException();
    }

    @Override
    default void turnRight(double angle) {
        throw new UnsupportedOperationException();
    }

    @Override
    default void turnRight(double angle, Vector2D<?, ?> vectorToTurn) {
        throw new UnsupportedOperationException();
    }

    @Override
    default void setLength(double newLength) {
        throw new UnsupportedOperationException();
    }

    @Override
    default UnmodifiableVector2D<RV, RP> toUnmodifiable() {
        return this;
    }

    @Override
    @XtextOperator("+=")
    default void operator_add(Vector2D<?, ?> v) {
        throw new UnsupportedOperationException();
    }

    @Override
    @XtextOperator("-=")
    default void operator_remove(Vector2D<?, ?> v) {
        throw new UnsupportedOperationException();
    }

}
