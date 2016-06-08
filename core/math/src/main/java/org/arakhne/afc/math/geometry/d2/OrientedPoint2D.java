/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2016 The original authors, and other authors.
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

import org.arakhne.afc.math.geometry.coordinatesystem.CoordinateSystem2D;
import org.arakhne.afc.vmutil.asserts.AssertMessages;

/** A point 2D with two orientation vectors relative to the polyline: the direction and the normal to the point.
 *
 * <p>The orientation vectors have no physical existence, i.e. they exist only to represent the direction of the
 * point and its normal when the point is part of a polyline. The normal vector is always perpendicular to the
 * direction vector. The point stores its length on the polyline to avoid rounding errors at discretization.
 *
 * @param <ST> is the type of the general implementation.
 * @param <IT> is the type of the implementation of this shape.
 * @param <I> is the type of the path elements.
 * @param <P> is the type of the points.
 * @param <V> is the type of the vectors.
 * @param <B> is the type of the bounding boxes.
 * @author $Author: tpiotrow$
 * @author $Author: sgalland$
 * @author $Author: olamotte$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public interface OrientedPoint2D<
        ST extends Shape2D<?, ?, I, P, V, B>,
        IT extends OrientedPoint2D<?, ?, I, P, V, B>,
        I extends PathIterator2D<?>,
        P extends Point2D<? super P, ? super V>,
        V extends Vector2D<? super V, ? super P>,
        B extends Shape2D<?, ?, I, P, V, B>>
        extends Shape2D<ST, IT, I, P, V, B> {

    /** Replies the X coordinate of the point.
     *
     * @return the x coordinate of the point.
     */
    @Pure double getX();

    /** Replies the X coordinate of the point.
     *
     * @return the x coordinate of the point.
     */
    @Pure int ix();

    /** Sets a new value in the X of the point.
     *
     * @param x the new value double x.
     */
    void setX(int x);

    /** Sets a new value in the X of the point.
     *
     * @param x the new value double x.
     */
    void setX(double x);

    /** Replies the Y coordinate of the point.
     *
     * @return the y coordinate of the point.
     */
    @Pure double getY();

    /** Replies the Y coordinate of the point.
     *
     * @return the y coordinate of the point.
     */
    @Pure int iy();

    /**  Sets a new value in the Y of the point.
     * @param y the new value double y.
     */
    void setY(int y);

    /**  Sets a new value in the Y of the point.
     * @param y the new value double y.
     */
    void setY(double y);

    /** Replies the X coordinate of the direction vector.
     * If this point is not part of a polyline, the direction vector is null.
     *
     * @return the x coordinate of the direction vector.
     */
    @Pure double getDirectionX();

    /** Replies the X coordinate of the direction vector.
     * If this point is not part of a polyline, the direction vector is null.
     *
     * @return the x coordinate of the direction vector.
     */
    @Pure int idx();

    /** Sets a new value in the X direction of the point.
     *
     * @param x the new value double x.
     */
    void setDirectionX(int x);

    /** Sets a new value in the X direction of the point.
     *
     * @param x the new value double x.
     */
    void setDirectionX(double x);

    /** Replies the Y coordinate of the direction vector.
     * If this point is not part of a polyline, the direction vector is null.
     *
     * @return the y coordinate of the direction vector.
     */
    @Pure double getDirectionY();

    /** Replies the Y coordinate of the direction vector.
     * If this point is not part of a polyline, the direction vector is null.
     *
     * @return the y coordinate of the direction vector.
     */
    @Pure int idy();

    /**  Sets a new value in the Y direction of the point.
     *
     * @param y the new value double y.
     */
    void setDirectionY(int y);

    /**  Sets a new value in the Y direction of the point.
     *
     * @param y the new value double y.
     */
    void setDirectionY(double y);

    /** Replies the X coordinate of the normal vector.
     *  If this point is not part of a polyline, the normal vector is null.
     *
     * @return the x coordinate of the normal vector.
     */
    @Pure default double getNormalX() {
        return -getDirectionY();
    }

    /** Replies the X coordinate of the normal vector.
     *  If this point is not part of a polyline, the normal vector is null.
     *
     * @return the x coordinate of the normal vector.
     */
    @Pure default int inx() {
        return -idy();
    }

    /** Replies the Y coordinate of the normal vector.
     *  If this point is not part of a polyline, the normal vector is null.
     *
     * @return the y coordinate of the normal vector.
     */
    @Pure default double getNormalY() {
        return getDirectionX();
    }

    /** Replies the Y coordinate of the normal vector.
     *  If this point is not part of a polyline, the normal vector is null.
     *
     * @return the y coordinate of the normal vector.
     */
    @Pure default int iny() {
        return idx();
    }

    /** Replies the geometrical length of this point on
     * the polyline.
     *
     * @return the length of the point on the polyline
     */
    @Pure double getLength();

    /** Replies the geometrical length of this point on
     * the polyline.
     *
     * @return the length of the point on the polyline
     */
    @Pure int ilen();

    /** Sets a new value for the length of the point.
     *
     * @param length the length of the point on the polyline.
     */
    void setLength(int length);

    /** Sets a new value for the length of the point.
     *
     * @param length the length of the point on the polyline.
     */
    void setLength(double length);

    @Override
    default boolean contains(Point2D<?, ?> pt) {
        return getX() == pt.getX() && getY() == pt.getY();
    }

    @Override
    default boolean equalsToShape(IT shape) {
        if (shape == null) {
            return false;
        }
        if (shape == this) {
            return true;
        }
        // We don't need to check normal vector because it depends of direction
        return getX() == shape.getX() && getY() == shape.getY()
                && getDirectionX() == shape.getDirectionX()
                && getDirectionY() == shape.getDirectionY();
    }

    /** Replies this point.
     * @return this point
     */
    default P getPoint() {
        return getGeomFactory().newPoint(getX(), getY());
    }

    @Override
    default double getDistanceL1(Point2D<?, ?> pt) {
        assert pt != null : AssertMessages.notNullParameter();
        return Point2D.getDistanceL1PointPoint(getX(), getY(), pt.getX(), pt.getY());
    }

    @Override
    default double getDistanceLinf(Point2D<?, ?> pt) {
        assert pt != null : AssertMessages.notNullParameter();
        return Point2D.getDistanceLinfPointPoint(getX(), getY(), pt.getX(), pt.getY());
    }

    @Override
    default double getDistanceSquared(Point2D<?, ?> pt) {
        assert pt != null : AssertMessages.notNullParameter();
        return Point2D.getDistanceSquaredPointPoint(getX(), getY(), pt.getX(), pt.getY());
    }

    @Override
    default double getDistance(Point2D<?, ?> pt) {
        assert pt != null : AssertMessages.notNullParameter();
        return Point2D.getDistancePointPoint(getX(), getY(), pt.getX(), pt.getY());
    }

    @Override
    default P getClosestPointTo(Point2D<?, ?> point) {
        return getPoint();
    }

    @Override
    default P getFarthestPointTo(Point2D<?, ?> point) {
        return getPoint();
    }

    @Override
    default boolean isEmpty() {
        return false;
    }

    /** Change the point.
     *
     * @param x x coordinate of the point.
     * @param y y coordinate of the point.
     */
    // No default implementation to ensure atomic change
    void set(int x, int y);

    /** Change the point.
     *
     * @param x x coordinate of the point.
     * @param y y coordinate of the point.
     * @param length the length of the point on the polyline.
     */
    // No default implementation to ensure atomic change
    void set(int x, int y, int length);

    /** Change the point and its orientation vector.
     *
     * @param x x coordinate of the point.
     * @param y y coordinate of the point.
     * @param dirX x coordinate of the vector.
     * @param dirY y coordinate of the vector.
     */
    // No default implementation to ensure atomic change
    void set(int x, int y, int dirX, int dirY);

    /** Change the point and its orientation vector.
     *
     * @param x x coordinate of the point.
     * @param y y coordinate of the point.
     * @param length the length of the point on the polyline.
     * @param dirX x coordinate of the vector.
     * @param dirY y coordinate of the vector.
     */
    // No default implementation to ensure atomic change
    void set(int x, int y, int length, int dirX, int dirY);

    /** Change the point.
     *
     * @param x x coordinate of the point.
     * @param y y coordinate of the point.
     */
    // No default implementation to ensure atomic change
    void set(double x, double y);

    /** Change the point.
     *
     * @param x x coordinate of the point.
     * @param y y coordinate of the point.
     * @param length the length of the point on the polyline.
     */
    // No default implementation to ensure atomic change
    void set(double x, double y, double length);

    /** Change the point and its orientation vector.
     *
     * @param x x coordinate of the point.
     * @param y y coordinate of the point.
     * @param dirX x coordinate of the vector.
     * @param dirY y coordinate of the vector.
     */
    // No default implementation to ensure atomic change
    void set(double x, double y, double dirX, double dirY);

    /** Change the point and its orientation vector.
     *
     * @param x x coordinate of the point.
     * @param y y coordinate of the point.
     * @param length the length of the point on the polyline.
     * @param dirX x coordinate of the vector.
     * @param dirY y coordinate of the vector.
     */
    // No default implementation to ensure atomic change
    void set(double x, double y, double length, double dirX, double dirY);

    @Override
    default void set(IT shape) {
        assert shape != null : AssertMessages.notNullParameter();
        set(shape.getX(), shape.getY(), shape.getDirectionX(), shape.getDirectionY());
    }

    @Override
    default void clear() {
        set(0, 0, 0, 0, 0);
    }

    /** Turn this point about the given rotation angle around the origin point.
     *
     * <p>The rotation is done according to the trigonometric coordinate.
     * A positive rotation angle corresponds to a left or right rotation
     * according to the current {@link CoordinateSystem2D}.
     *
     * @param angle is the rotation angle in radians.
     * @see #turn(double, OrientedPoint2D, OrientedPoint2D)
     * @see #turnLeft(double)
     * @see #turnRight(double)
     */
    default void turn(double angle) {
        turn(angle, this);
    }

    /** Turn the given point about the given rotation angle around the origin point, and set this
     * point with the result.
     *
     * <p>The rotation is done according to the trigonometric coordinate.
     * A positive rotation angle corresponds to a left or right rotation
     * according to the current {@link CoordinateSystem2D}.
     *
     * @param angle is the rotation angle in radians.
     * @param pointToTurn the point to turn.
     * @see #turn(double, OrientedPoint2D, OrientedPoint2D)
     * @see #turn(double)
     * @see #turnLeft(double)
     * @see #turnRight(double)
     */
    default void turn(double angle, OrientedPoint2D<?, ?, ?, ?, ?, ?> pointToTurn) {
        assert pointToTurn != null : AssertMessages.notNullParameter(1);
        final double sin = Math.sin(angle);
        final double cos = Math.cos(angle);
        final double x =  cos * pointToTurn.getX() - sin * pointToTurn.getY();
        final double y =  sin * pointToTurn.getX() + cos * pointToTurn.getY();
        set(x, y);
    }

    /** Turn the given point about the given rotation angle around the origin point, and set this
     * point with the result.
     *
     * <p>The rotation is done according to the trigonometric coordinate.
     * A positive rotation angle corresponds to a left or right rotation
     * according to the current {@link CoordinateSystem2D}.
     *
     * @param angle is the rotation angle in radians.
     * @param pointToTurn the point to turn.
     * @param origin the origin point.
     * @see #turn(double, OrientedPoint2D)
     * @see #turn(double)
     * @see #turnLeft(double)
     * @see #turnRight(double)
     */
    default void turn(double angle, OrientedPoint2D<?, ?, ?, ?, ?, ?> pointToTurn, OrientedPoint2D<?, ?, ?, ?, ?, ?> origin) {
        assert pointToTurn != null : AssertMessages.notNullParameter(1);
        assert origin != null : AssertMessages.notNullParameter(2);
        final double sin = Math.sin(angle);
        final double cos = Math.cos(angle);
        final double vx = pointToTurn.getX() - origin.getX();
        final double vy = pointToTurn.getY() - origin.getY();
        final double x =  cos * vx - sin * vy;
        final double y =  sin * vx + cos * vy;
        set(x + origin.getX(), y + origin.getY());
    }

    /** Turn this vector on the left around the origin when the given rotation angle is positive.
     *
     * <p>A positive rotation angle corresponds to a left or right rotation
     * according to the current {@link CoordinateSystem2D}.
     *
     * @param angle is the rotation angle in radians.
     * @see CoordinateSystem2D
     * @see #turnLeft(double, OrientedPoint2D, OrientedPoint2D)
     * @see #turn(double)
     * @see #turnRight(double)
     */
    default void turnLeft(double angle) {
        turnLeft(angle, this);
    }

    /** Turn the given vector on the left, and set this
     * vector with the result.
     *
     * <p>A positive rotation angle corresponds to a left or right rotation
     * according to the current {@link CoordinateSystem2D}.
     *
     * @param angle is the rotation angle in radians.
     * @param pointToTurn the vector to turn.
     * @see CoordinateSystem2D
     * @see #turnLeft(double, OrientedPoint2D, OrientedPoint2D)
     * @see #turn(double)
     * @see #turnRight(double)
     */
    default void turnLeft(double angle, OrientedPoint2D<?, ?, ?, ?, ?, ?> pointToTurn) {
        assert pointToTurn != null : AssertMessages.notNullParameter(1);
        final double sin = Math.sin(angle);
        final double cos = Math.cos(angle);
        final double x;
        final double y;
        if (CoordinateSystem2D.getDefaultCoordinateSystem().isRightHanded()) {
            x =  cos * pointToTurn.getX() - sin * pointToTurn.getY();
            y =  sin * pointToTurn.getX() + cos * pointToTurn.getY();
        } else {
            x =  cos * pointToTurn.getX() + sin * pointToTurn.getY();
            y = -sin * pointToTurn.getX() + cos * pointToTurn.getY();
        }
        set(x, y);
    }

    /** Turn the given vector on the left, and set this
     * vector with the result.
     *
     * <p>A positive rotation angle corresponds to a left or right rotation
     * according to the current {@link CoordinateSystem2D}.
     *
     * @param angle is the rotation angle in radians.
     * @param pointToTurn the vector to turn.
     * @param origin the origin point.
     * @see CoordinateSystem2D
     * @see #turnLeft(double, OrientedPoint2D)
     * @see #turn(double)
     * @see #turnRight(double)
     */
    default void turnLeft(double angle, OrientedPoint2D<?, ?, ?, ?, ?, ?> pointToTurn, OrientedPoint2D<?, ?, ?, ?, ?, ?> origin) {
        assert pointToTurn != null : AssertMessages.notNullParameter(1);
        assert origin != null : AssertMessages.notNullParameter(2);
        final double sin = Math.sin(angle);
        final double cos = Math.cos(angle);
        final double vx = pointToTurn.getX() - origin.getX();
        final double vy = pointToTurn.getY() - origin.getY();
        final double x;
        final double y;
        if (CoordinateSystem2D.getDefaultCoordinateSystem().isRightHanded()) {
            x =  cos * vx - sin * vy;
            y =  sin * vx + cos * vy;
        } else {
            x =  cos * vx + sin * vy;
            y = -sin * vx + cos * vy;
        }
        set(x + origin.getX(), y + origin.getY());
    }

    /** Turn this vector on the right around the origin when the given rotation angle is positive.
     *
     * <p>A positive rotation angle corresponds to a left or right rotation
     * according to the current {@link CoordinateSystem2D}.
     *
     * @param angle is the rotation angle in radians.
     * @see CoordinateSystem2D
     * @see #turn(double)
     * @see #turnLeft(double)
     */
    default void turnRight(double angle) {
        turnLeft(-angle, this);
    }

    /** Turn this vector on the right around the origin when the given rotation angle is positive.
     *
     * <p>A positive rotation angle corresponds to a left or right rotation
     * according to the current {@link CoordinateSystem2D}.
     *
     * @param angle is the rotation angle in radians.
     * @param pointToTurn the vector to turn.
     * @see CoordinateSystem2D
     * @see #turn(double)
     * @see #turnLeft(double)
     */
    default void turnRight(double angle, OrientedPoint2D<?, ?, ?, ?, ?, ?> pointToTurn) {
        turnLeft(-angle, pointToTurn);
    }

    /** Turn this vector on the right around the origin when the given rotation angle is positive.
     *
     * <p>A positive rotation angle corresponds to a left or right rotation
     * according to the current {@link CoordinateSystem2D}.
     *
     * @param angle is the rotation angle in radians.
     * @param pointToTurn the vector to turn.
     * @param origin the origin point.
     * @see CoordinateSystem2D
     * @see #turn(double)
     * @see #turnLeft(double)
     */
    default void turnRight(double angle, OrientedPoint2D<?, ?, ?, ?, ?, ?> pointToTurn,
            OrientedPoint2D<?, ?, ?, ?, ?, ?> origin) {
        turnLeft(-angle, pointToTurn, origin);
    }

}
