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

package org.arakhne.afc.math.geometry.d2.i;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.geometry.d2.OrientedPoint2D;
import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.Tuple2D;
import org.arakhne.afc.math.geometry.d2.Vector2D;

/** 2D oriented point with int precision floating-point numbers.
 *
 * @author $Author: tpiotrow$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class OrientedPoint2i
    extends Point2i
    implements OrientedPoint2D<Point2i, Vector2i> {

    private static final long serialVersionUID = 6296312122530686621L;

    private int tx;

    private int ty;

    /** Construct an empty oriented point.
     */
    public OrientedPoint2i() {
        //
    }

    /** Construct an oriented point from a point.
     * @param point the point.
     */
    public OrientedPoint2i(Tuple2D<?> point) {
        super(point);
    }

    /** Construct an oriented point from the two given coordinates.
     * @param x x coordinate of the point.
     * @param y y coordinate of the point.
     */
    public OrientedPoint2i(int x, int y) {
        super(x, y);
    }

    /** Construct an oriented point from a point and a tangent vector.
     * @param point the point.
     * @param vector the tangent vector.
     */
    public OrientedPoint2i(Point2D<?, ?> point, Vector2D<?, ?> vector) {
        this(point.ix(), point.iy(), vector.ix(), vector.iy());
    }

    /** Construct an oriented point from the two given coordinates.
     * @param x x coordinate of the point.
     * @param y y coordinate of the point.
     * @param tanX x coordinate of the vector.
     * @param tanY y coordinate of the vector.
     */
    public OrientedPoint2i(int x, int y, int tanX, int tanY) {
        set(x, y, tanX, tanY);
    }

    @Pure
    @Override
    public int hashCode() {
        int bits = 1;
        bits = 31 * bits + super.hashCode();
        bits = 31 * bits + Integer.hashCode(this.tx);
        bits = 31 * bits + Integer.hashCode(this.ty);
        return bits ^ (bits >> 31);
    }

    @Override
    public double getTangentX() {
        return this.tx;
    }

    @Override
    public int itx() {
        return this.tx;
    }

    @Override
    public void setTangentX(int tanX) {
        this.tx = tanX;
    }

    @Override
    public void setTangentX(double tanX) {
        this.tx = (int) Math.round(tanX);
    }

    @Override
    public double getTangentY() {
        return this.ty;
    }

    @Override
    public int ity() {
        return this.ty;
    }

    @Override
    public void setTangentY(int tanY) {
        this.ty = tanY;
    }

    @Override
    public void setTangentY(double tanY) {
        this.ty = (int) Math.round(tanY);
    }

    @Override
    public Vector2i getTangent() {
        return getGeomFactory().newVector(this.tx, this.ty);
    }

    @Override
    public Vector2i getNormal() {
        return getGeomFactory().newVector(-this.ty, this.tx);
    }

}
