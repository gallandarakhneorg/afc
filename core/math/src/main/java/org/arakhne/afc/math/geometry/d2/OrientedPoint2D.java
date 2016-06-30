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
    @Pure double getTangentX();

    /** Replies the X coordinate of the direction vector.
     * If this point is not part of a polyline, the direction vector is null.
     *
     * @return the x coordinate of the direction vector.
     */
    @Pure int itx();

    /** Sets a new value in the X direction of the point.
     *
     * @param tanX the new value double x.
     */
    void setTangentX(int tanX);

    /** Sets a new value in the X direction of the point.
     *
     * @param tanX the new value double x.
     */
    void setTangentX(double tanX);

    /** Replies the Y coordinate of the direction vector.
     * If this point is not part of a polyline, the direction vector is null.
     *
     * @return the y coordinate of the direction vector.
     */
    @Pure double getTangentY();

    /** Replies the Y coordinate of the direction vector.
     * If this point is not part of a polyline, the direction vector is null.
     *
     * @return the y coordinate of the direction vector.
     */
    @Pure int ity();

    /**  Sets a new value in the Y direction of the point.
     *
     * @param tanY the new value double y.
     */
    void setTangentY(int tanY);

    /**  Sets a new value in the Y direction of the point.
     *
     * @param tanY the new value double y.
     */
    void setTangentY(double tanY);

    /** Replies the X coordinate of the normal vector.
     *  If this point is not part of a polyline, the normal vector is null.
     *
     * @return the x coordinate of the normal vector.
     */
    @Pure default double getNormalX() {
        return -getTangentY();
    }

    /** Replies the X coordinate of the normal vector.
     *  If this point is not part of a polyline, the normal vector is null.
     *
     * @return the x coordinate of the normal vector.
     */
    @Pure default int inx() {
        return -ity();
    }

    /** Replies the Y coordinate of the normal vector.
     *  If this point is not part of a polyline, the normal vector is null.
     *
     * @return the y coordinate of the normal vector.
     */
    @Pure default double getNormalY() {
        return getTangentX();
    }

    /** Replies the Y coordinate of the normal vector.
     *  If this point is not part of a polyline, the normal vector is null.
     *
     * @return the y coordinate of the normal vector.
     */
    @Pure default int iny() {
        return itx();
    }

    /** Replies this point.
     *
     * @return this point
     */
    default RP getPoint() {
        return getGeomFactory().newPoint(getX(), getY());
    }

    /** Replies the tangent vector at this point.
     *
     * @return the tangent vector.
     */
    @Pure RV getTangent();

    /** Sets the given vector as the new tangent to this point.
     *  The normal vector is automatically recomputed.
     *
     * @param tangent the vector to set.
     */
    default void setTangent(RV tangent) {
        setTangentX(tangent.getX());
        setTangentY(tangent.getY());
    }

    /** Change the tangent vector.
     *  The normal vector is automatically recomputed.
     *
     * @param x x coordinate of the vector.
     * @param y y coordinate of the vector.
     */
    default void setTangent(double x, double y) {
        setTangentX(x);
        setTangentY(y);
    }

    /** Change the tangent vector.
     *  The normal vector is automatically recomputed.
     *
     * @param x x coordinate of the vector.
     * @param y y coordinate of the vector.
     */
    default void setTangent(int x, int y) {
        setTangentX(x);
        setTangentY(y);
    }

    /** Replies the normal vector at this point.
     *
     * @return the normal vector.
     */
    @Pure RV getNormal();

    /** Change the point and its tangent vector.
     *
     * @param x x coordinate of the point.
     * @param y y coordinate of the point.
     * @param tanX x coordinate of the vector.
     * @param tanY y coordinate of the vector.
     */
    default void set(int x, int y, int tanX, int tanY) {
        Point2D.super.set(x, y);
        setTangentX(tanX);
        setTangentY(tanY);
    }

    /** Change the point and its orientation vector.
     *
     * @param x x coordinate of the point.
     * @param y y coordinate of the point.
     * @param tanX x coordinate of the vector.
     * @param tanY y coordinate of the vector.
     */
    default void set(double x, double y, double tanX, double tanY) {
        Point2D.super.set(x, y);
        setTangentX(tanX);
        setTangentY(tanY);
    }

    /**
     * Returns true if all of the data members of OrientedPoint2D p1 are
     * equal to the corresponding data members in this OrientedPoint2D.
     *
     * @param p1 the point with which the comparison is made
     * @return true or false
     */
    @Pure
    default boolean equals(OrientedPoint2D<?, ?> p1) {
        return Point2D.super.equals(p1) && getTangent().equals(p1.getTangent());
    }
}
