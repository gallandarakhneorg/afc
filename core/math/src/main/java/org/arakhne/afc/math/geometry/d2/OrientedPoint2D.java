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

/** A 2D point with two orientation vectors relative to the polyline: the direction and the normal to the point.
 *
 * <p>The orientation vectors have no physical existence, i.e. they exist only to represent the direction of the
 * point and its normal when the point is part of a polyline. The normal vector is always perpendicular to the
 * direction vector. The point stores its length on the polyline to avoid rounding errors at discretization.
 *
 * @param <RP> is the type of point that can be returned by this tuple.
 * @param <RV> is the type of vector that can be returned by this tuple.
 * @author $Author: tpiotrow$
 * @author $Author: sgalland$
 * @author $Author: olamotte$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public interface OrientedPoint2D<RP extends Point2D<? super RP, ? super RV>, RV extends Vector2D<? super RV, ? super RP>>
        extends Point2D<RP, RV> {

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
     * @param dirX the new value double x.
     */
    void setDirectionX(int dirX);

    /** Sets a new value in the X direction of the point.
     *
     * @param dirX the new value double x.
     */
    void setDirectionX(double dirX);

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
     * @param dirY the new value double y.
     */
    void setDirectionY(int dirY);

    /**  Sets a new value in the Y direction of the point.
     *
     * @param dirY the new value double y.
     */
    void setDirectionY(double dirY);

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

    /** Replies this point.
     * @return this point
     */
    default RP getPoint() {
        return getGeomFactory().newPoint(getX(), getY());
    }

    /** Change the point.
     *
     * @param x x coordinate of the point.
     * @param y y coordinate of the point.
     * @param length the length of the point on the polyline.
     */
    default void set(int x, int y, int length) {
        Point2D.super.set(x, y);
        setLength(length);
    }

    /** Change the point and its orientation vector.
     *
     * @param x x coordinate of the point.
     * @param y y coordinate of the point.
     * @param dirX x coordinate of the vector.
     * @param dirY y coordinate of the vector.
     */
    default void set(int x, int y, int dirX, int dirY) {
        Point2D.super.set(x, y);
        setDirectionX(dirX);
        setDirectionY(dirY);
    }

    /** Change the point and its orientation vector.
     *
     * @param x x coordinate of the point.
     * @param y y coordinate of the point.
     * @param length the length of the point on the polyline.
     * @param dirX x coordinate of the vector.
     * @param dirY y coordinate of the vector.
     */
    default void set(int x, int y, int length, int dirX, int dirY) {
        Point2D.super.set(x, y);
        setLength(length);
        setDirectionX(dirX);
        setDirectionY(dirY);
    }

    /** Change the point.
     *
     * @param x x coordinate of the point.
     * @param y y coordinate of the point.
     * @param length the length of the point on the polyline.
     */
    default void set(double x, double y, double length) {
        Point2D.super.set(x, y);
        setLength(length);
    }

    /** Change the point and its orientation vector.
     *
     * @param x x coordinate of the point.
     * @param y y coordinate of the point.
     * @param dirX x coordinate of the vector.
     * @param dirY y coordinate of the vector.
     */
    default void set(double x, double y, double dirX, double dirY) {
        Point2D.super.set(x, y);
        setDirectionX(dirX);
        setDirectionY(dirY);
    }

    /** Change the point and its orientation vector.
     *
     * @param x x coordinate of the point.
     * @param y y coordinate of the point.
     * @param length the length of the point on the polyline.
     * @param dirX x coordinate of the vector.
     * @param dirY y coordinate of the vector.
     */
    default void set(double x, double y, double length, double dirX, double dirY) {
        Point2D.super.set(x, y);
        setLength(length);
        setDirectionX(dirX);
        setDirectionY(dirY);
    }
}
