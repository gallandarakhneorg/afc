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

package org.arakhne.afc.math.geometry.d2.i;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.Transform2D;
import org.arakhne.afc.math.geometry.d2.Vector2D;
import org.arakhne.afc.math.geometry.d2.ai.OrientedPoint2ai;

/** 2D oriented point with int precision floating-point numbers.
 *
 * @author $Author: tpiotrow$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class OrientedPoint2i
    extends AbstractShape2i<OrientedPoint2i>
    implements OrientedPoint2ai<Shape2i<?>, OrientedPoint2i, PathElement2i, Point2i, Vector2i, Rectangle2i> {

    private static final long serialVersionUID = 6296312122530686621L;

    private int px;

    private int py;

    private int dx;

    private int dy;

    private int len;

    /** Construct an empty oriented point.
     */
    public OrientedPoint2i() {
        //
    }

    /** Construct an oriented point from a point.
     * @param point the point.
     */
    public OrientedPoint2i(Point2D<?, ?> point) {
        this(point.ix(), point.iy());
    }

    /** Construct an oriented point from a point and its length on a polyline.
     * @param point the point.
     * @param length the length.
     */
    public OrientedPoint2i(Point2D<?, ?> point, int length) {
        this(point.ix(), point.iy(), length);
    }

    /** Construct an oriented point from the two given coordinates.
     * @param x x coordinate of the point.
     * @param y y coordinate of the point.
     */
    public OrientedPoint2i(int x, int y) {
        set(x, y);
    }

    /** Construct an oriented point from the two given coordinates and the length of the point on a polyline.
     * @param x x coordinate of the point.
     * @param y y coordinate of the point.
     * @param length the length
     */
    public OrientedPoint2i(int x, int y, int length) {
        set(x, y, length);
    }

    /** Construct an oriented point from a point and a direction vector.
     * @param point the point.
     * @param vector the direction vector.
     */
    public OrientedPoint2i(Point2D<?, ?> point, Vector2D<?, ?> vector) {
        this(point.ix(), point.iy(), vector.ix(), vector.iy());
    }

    /** Construct an oriented point from a point, its length, and a direction vector.
     * @param point the point.
     * @param length the length of the point
     * @param vector the direction vector.
     */
    public OrientedPoint2i(Point2D<?, ?> point, int length, Vector2D<?, ?> vector) {
        this(point.ix(), point.iy(), length, vector.ix(), vector.iy());
    }

    /** Construct an oriented point from the two given coordinates.
     * @param x x coordinate of the point.
     * @param y y coordinate of the point.
     * @param dirX x coordinate of the vector.
     * @param dirY y coordinate of the vector.
     */
    public OrientedPoint2i(int x, int y, int dirX, int dirY) {
        set(x, y, dirX, dirY);
    }

    /** Construct an oriented point from the given coordinates.
     * @param x x coordinate of the point.
     * @param y y coordinate of the point.
     * @param length the length of the point on the polyline.
     * @param dirX x coordinate of the vector.
     * @param dirY y coordinate of the vector.
     */
    public OrientedPoint2i(int x, int y, int length, int dirX, int dirY) {
        set(x, y, length, dirX, dirY);
    }

    @Pure
    @Override
    public int hashCode() {
        int bits = 1;
        bits = 31 * bits + Integer.hashCode(this.px);
        bits = 31 * bits + Integer.hashCode(this.py);
        bits = 31 * bits + Integer.hashCode(this.dx);
        bits = 31 * bits + Integer.hashCode(this.dy);
        bits = 31 * bits + Integer.hashCode(this.len);
        return bits ^ (bits >> 31);
    }

    @Pure
    @Override
    public String toString() {
        final StringBuilder b = new StringBuilder();
        b.append("("); //$NON-NLS-1$
        b.append(getX());
        b.append(", "); //$NON-NLS-1$
        b.append(getY());
        b.append(")|("); //$NON-NLS-1$
        b.append(getLength());
        b.append(")|("); //$NON-NLS-1$
        b.append(getDirectionX());
        b.append(", "); //$NON-NLS-1$
        b.append(getDirectionY());
        b.append(")|("); //$NON-NLS-1$
        b.append(getNormalX());
        b.append(", "); //$NON-NLS-1$
        b.append(getNormalY());
        b.append(")"); //$NON-NLS-1$
        return b.toString();
    }

    @Override
    public OrientedPoint2i createTransformedShape(Transform2D transform) {
        if (transform == null || transform.isIdentity()) {
            return clone();
        }
        final Point2i point = getGeomFactory().newPoint(ix(), iy());
        transform.transform(point);
        final int x1 = point.ix();
        final int y1 = point.iy();
        point.set(idx(), idy());
        transform.transform(point);
        return getGeomFactory().newOrientedPoint(x1, y1, point.ix(), point.iy());
    }

    @Override
    public void set(int x, int y) {
        if (this.px != x || this.py != y) {
            this.px = x;
            this.py = y;
            fireGeometryChange();
        }
    }

    @Override
    public void set(int x, int y, int length) {
        if (this.px != x || this.py != y || this.len != length) {
            this.px = x;
            this.py = y;
            this.len = length;
            fireGeometryChange();
        }
    }

    @Override
    public void set(int x, int y, int dirX, int dirY) {
        if (this.px != x || this.py != y || this.dx != dirX || this.dy != dirY) {
            this.px = x;
            this.py = y;
            this.dx = dirX;
            this.dy = dirY;
            fireGeometryChange();
        }
    }

    @Override
    public void set(int x, int y, int length, int dirX, int dirY) {
        if (this.px != x || this.py != y || this.len != length || this.dx != dirX || this.dy != dirY) {
            this.px = x;
            this.py = y;
            this.dx = dirX;
            this.dy = dirY;
            this.len = length;
            fireGeometryChange();
        }
    }

    @Override
    public void set(double x, double y) {
        if (this.px != x || this.py != y) {
            this.px = (int) Math.round(x);
            this.py = (int) Math.round(y);
            fireGeometryChange();
        }
    }

    @Override
    public void set(double x, double y, double length) {
        if (this.px != x || this.py != y || this.len != length) {
            this.px = (int) Math.round(x);
            this.py = (int) Math.round(y);
            this.len = (int) Math.round(length);
            fireGeometryChange();
        }
    }

    @Override
    public void set(double x, double y, double dirX, double dirY) {
        if (this.px != x || this.py != y || this.dx != dirX || this.dy != dirY) {
            this.px = (int) Math.round(x);
            this.py = (int) Math.round(y);
            this.dx = (int) Math.round(dirX);
            this.dy = (int) Math.round(dirY);
            fireGeometryChange();
        }
    }

    @Override
    public void set(double x, double y, double length, double dirX, double dirY) {
        if (this.px != x || this.py != y || this.len != length || this.dx != dirX || this.dy != dirY) {
            this.px = (int) Math.round(x);
            this.py = (int) Math.round(y);
            this.dx = (int) Math.round(dirX);
            this.dy = (int) Math.round(dirY);
            this.len = (int) Math.round(length);
            fireGeometryChange();
        }
    }

    @Override
    public void setX(int x) {
        if (this.px != x) {
            this.px = x;
            fireGeometryChange();
        }
    }

    @Override
    public void setX(double x) {
        if (this.px != x) {
            this.px = (int) Math.round(x);
            fireGeometryChange();
        }
    }

    @Override
    public void setY(int y) {
        if (this.py != y) {
            this.py = y;
            fireGeometryChange();
        }
    }

    @Override
    public void setY(double y) {
        if (this.py != y) {
            this.py = (int) Math.round(y);
            fireGeometryChange();
        }
    }

    @Override
    public void setDirectionX(int dirX) {
        if (this.dx != dirX) {
            this.dx = dirX;
            fireGeometryChange();
        }
    }

    @Override
    public void setDirectionX(double dirX) {
        if (this.dx != dirX) {
            this.dx = (int) Math.round(dirX);
            fireGeometryChange();
        }
    }

    @Override
    public void setDirectionY(int dirY) {
        if (this.dy != dirY) {
            this.dy = dirY;
            fireGeometryChange();
        }
    }

    @Override
    public void setDirectionY(double dirY) {
        if (this.dy != dirY) {
            this.dy = (int) Math.round(dirY);
            fireGeometryChange();
        }
    }

    @Override
    public void setLength(int length) {
        if (this.len != length) {
            this.len = length;
            fireGeometryChange();
        }
    }

    @Override
    public void setLength(double length) {
        if (this.len != length) {
            this.len = (int) Math.round(length);
            fireGeometryChange();
        }
    }

    @Override
    public double getX() {
        return this.px;
    }

    @Override
    public int ix() {
        return this.px;
    }

    @Override
    public double getY() {
        return this.py;
    }

    @Override
    public int iy() {
        return this.py;
    }

    @Override
    public double getDirectionX() {
        return this.dx;
    }

    @Override
    public int idx() {
        return this.dx;
    }

    @Override
    public double getDirectionY() {
        return this.dy;
    }

    @Override
    public int idy() {
        return this.dy;
    }

    @Override
    public double getLength() {
        return this.len;
    }

    @Override
    public int ilen() {
        return this.len;
    }
}
