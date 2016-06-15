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

package org.arakhne.afc.math.geometry.d3;

import org.eclipse.xtext.xbase.lib.Pure;

/** A 3D point with three orientation vectors relative to the polyline: the direction, the normal and the sway to the point.
 *
 * <p>The orientation vectors have no physical existence, i.e. they exist only to represent the direction of the
 * point, its normal and its sway when the point is part of a polyline. The normal vector is always perpendicular to the
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
public interface OrientedPoint3D<RP extends Point3D<? super RP, ? super RV>, RV extends Vector3D<? super RV, ? super RP>>
        extends Point3D<RP, RV> {

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

    /** Replies the Z coordinate of the direction vector.
     * If this point is not part of a polyline, the direction vector is null.
     *
     * @return the z coordinate of the direction vector.
     */
    @Pure double getDirectionZ();

    /** Replies the Z coordinate of the direction vector.
     * If this point is not part of a polyline, the direction vector is null.
     *
     * @return the z coordinate of the direction vector.
     */
    @Pure int idz();

    /**  Sets a new value in the Z direction of the point.
     *
     * @param dirZ the new value double z.
     */
    void setDirectionZ(int dirZ);

    /**  Sets a new value in the Z direction of the point.
     *
     * @param dirZ the new value double z.
     */
    void setDirectionZ(double dirZ);

    /** Replies the X coordinate of the normal vector.
     *  If this point is not part of a polyline, the normal vector is null.
     *
     * @return the x coordinate of the normal vector.
     */
    @Pure double getNormalX();

    /** Replies the X coordinate of the normal vector.
     *  If this point is not part of a polyline, the normal vector is null.
     *
     * @return the x coordinate of the normal vector.
     */
    @Pure int inx();

    /** Replies the Y coordinate of the normal vector.
     *  If this point is not part of a polyline, the normal vector is null.
     *
     * @return the y coordinate of the normal vector.
     */
    @Pure double getNormalY();

    /** Replies the Y coordinate of the normal vector.
     *  If this point is not part of a polyline, the normal vector is null.
     *
     * @return the y coordinate of the normal vector.
     */
    @Pure int iny();

    /** Replies the Z coordinate of the normal vector.
     *  If this point is not part of a polyline, the normal vector is null.
     *
     * @return the z coordinate of the normal vector.
     */
    @Pure double getNormalZ();

    /** Replies the Z coordinate of the normal vector.
     *  If this point is not part of a polyline, the normal vector is null.
     *
     * @return the z coordinate of the normal vector.
     */
    @Pure int inz();

    /** Replies the X coordinate of the sway vector.
     *  If this point is not part of a polyline, the normal vector is null.
     *
     * @return the x coordinate of the sway vector.
     */
    @Pure double getSwayX();

    /** Replies the X coordinate of the sway vector.
     *  If this point is not part of a polyline, the normal vector is null.
     *
     * @return the x coordinate of the sway vector.
     */
    @Pure int isx();

    /** Replies the Y coordinate of the sway vector.
     *  If this point is not part of a polyline, the normal vector is null.
     *
     * @return the y coordinate of the sway vector.
     */
    @Pure double getSwayY();

    /** Replies the Y coordinate of the sway vector.
     *  If this point is not part of a polyline, the normal vector is null.
     *
     * @return the y coordinate of the sway vector.
     */
    @Pure int isy();

    /** Replies the Z coordinate of the sway vector.
     *  If this point is not part of a polyline, the normal vector is null.
     *
     * @return the z coordinate of the sway vector.
     */
    @Pure double getSwayZ();

    /** Replies the Z coordinate of the sway vector.
     *  If this point is not part of a polyline, the normal vector is null.
     *
     * @return the z coordinate of the sway vector.
     */
    @Pure int isz();

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
        return getGeomFactory().newPoint(getX(), getY(), getZ());
    }

    /** Change the point.
     *
     * @param x x coordinate of the point.
     * @param y y coordinate of the point.
     * @param z z coordinate of the point.
     * @param length the length of the point on the polyline.
     */
    default void set(int x, int y, int z, int length) {
        Point3D.super.set(x, y, z);
        setLength(length);
    }

    /** Change the point and its orientation vector.
     *
     * @param x x coordinate of the point.
     * @param y y coordinate of the point.
     * @param z z coordinate of the point.
     * @param dirX x coordinate of the vector.
     * @param dirY y coordinate of the vector.
     * @param dirZ z coordinate of the vector.
     */
    default void set(int x, int y, int z, int dirX, int dirY, int dirZ) {
        Point3D.super.set(x, y, z);
        setDirectionX(dirX);
        setDirectionY(dirY);
        setDirectionZ(dirZ);
    }

    /** Change the point and its orientation vector.
     *
     * @param x x coordinate of the point.
     * @param y y coordinate of the point.
     * @param z z coordinate of the point.
     * @param length the length of the point on the polyline.
     * @param dirX x coordinate of the vector.
     * @param dirY y coordinate of the vector.
     * @param dirZ z coordinate of the vector.
     */
    default void set(int x, int y, int z, int length, int dirX, int dirY, int dirZ) {
        Point3D.super.set(x, y, z);
        setLength(length);
        setDirectionX(dirX);
        setDirectionY(dirY);
        setDirectionZ(dirZ);
    }

    /** Change the point.
     *
     * @param x x coordinate of the point.
     * @param y y coordinate of the point.
     * @param z z coordinate of the point.
     * @param length the length of the point on the polyline.
     */
    default void set(double x, double y, double z, double length) {
        Point3D.super.set(x, y, z);
        setLength(length);
    }

    /** Change the point and its orientation vector.
     *
     * @param x x coordinate of the point.
     * @param y y coordinate of the point.
     * @param z z coordinate of the point.
     * @param dirX x coordinate of the vector.
     * @param dirY y coordinate of the vector.
     * @param dirZ z coordinate of the vector.
     */
    default void set(double x, double y, double z, double dirX, double dirY, double dirZ) {
        Point3D.super.set(x, y, z);
        setDirectionX(dirX);
        setDirectionY(dirY);
        setDirectionZ(dirZ);
    }

    /** Change the point and its orientation vector.
     *
     * @param x x coordinate of the point.
     * @param y y coordinate of the point.
     * @param z z coordinate of the point.
     * @param length the length of the point on the polyline.
     * @param dirX x coordinate of the vector.
     * @param dirY y coordinate of the vector.
     * @param dirZ z coordinate of the vector.
     */
    default void set(double x, double y, double z, double length, double dirX, double dirY, double dirZ) {
        Point3D.super.set(x, y, z);
        setLength(length);
        setDirectionX(dirX);
        setDirectionY(dirY);
    }

    // TODO : make set(x, y, z dirX, dirY, dirZ, normX, normY, normZ) ? w/o length ? {int,double} ?

}
