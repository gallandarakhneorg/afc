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

package org.arakhne.afc.math.geometry.d2;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.vmutil.annotations.XtextOperator;

/** Unmodifiable2D Point.
 *
 * @param <RP> is the type of points that can be returned by this tuple.
 * @param <RV> is the type of vectors that can be returned by this tuple.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public interface UnmodifiablePoint2D<RP extends Point2D<? super RP, ? super RV>,
        RV extends Vector2D<? super RV, ? super RP>> extends UnmodifiableTuple2D<RP>, Point2D<RP, RV> {

    @Override
    default void add(Point2D<?, ?> point, Vector2D<?, ?> vector) {
        throw new UnsupportedOperationException();
    }

    @Override
    default void add(Vector2D<?, ?> vector, Point2D<?, ?> point) {
        throw new UnsupportedOperationException();
    }

    @Override
    default void add(Vector2D<?, ?> vector) {
        throw new UnsupportedOperationException();
    }

    @Override
    default void scaleAdd(int scale, Vector2D<?, ?> vector, Point2D<?, ?> point) {
        throw new UnsupportedOperationException();
    }

    @Override
    default void scaleAdd(double scale, Vector2D<?, ?> vector, Point2D<?, ?> point) {
        throw new UnsupportedOperationException();
    }

    @Override
    default void scaleAdd(int scale, Point2D<?, ?> point, Vector2D<?, ?> vector) {
        throw new UnsupportedOperationException();
    }

    @Override
    default void scaleAdd(double scale, Point2D<?, ?> point, Vector2D<?, ?> vector) {
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
    default void sub(Point2D<?, ?> point, Vector2D<?, ?> vector) {
        throw new UnsupportedOperationException();
    }

    @Override
    default void sub(Vector2D<?, ?> vector) {
        throw new UnsupportedOperationException();
    }

    @Pure
    @Override
    default UnmodifiablePoint2D<RP, RV> toUnmodifiable() {
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

    @Override
    default void turn(double angle) {
        throw new UnsupportedOperationException();
    }

    @Override
    default void turn(double angle, Point2D<?, ?> pointToTurn) {
        throw new UnsupportedOperationException();
    }

    @Override
    default void turn(double angle, Point2D<?, ?> pointToTurn, Point2D<?, ?> origin) {
        throw new UnsupportedOperationException();
    }

    @Override
    default void turnLeft(double angle) {
        throw new UnsupportedOperationException();
    }

    @Override
    default void turnLeft(double angle, Point2D<?, ?> pointToTurn) {
        throw new UnsupportedOperationException();
    }

    @Override
    default void turnLeft(double angle, Point2D<?, ?> pointToTurn, Point2D<?, ?> origin) {
        throw new UnsupportedOperationException();
    }

    @Override
    default void turnRight(double angle) {
        throw new UnsupportedOperationException();
    }

    @Override
    default void turnRight(double angle, Point2D<?, ?> pointToTurn) {
        throw new UnsupportedOperationException();
    }

    @Override
    default void turnRight(double angle, Point2D<?, ?> pointToTurn, Point2D<?, ?> origin) {
        throw new UnsupportedOperationException();
    }

}
