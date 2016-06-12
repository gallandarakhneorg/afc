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

package org.arakhne.afc.math.geometry.d2.d;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.Transform2D;
import org.arakhne.afc.math.geometry.d2.Vector2D;
import org.arakhne.afc.math.geometry.d2.afp.OrientedPoint2afp;

/** 2D oriented point with double precision floating-point numbers.
 *
 * @author $Author: tpiotrow$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class OrientedPoint2d
    extends AbstractShape2d<OrientedPoint2d>
    implements OrientedPoint2afp<Shape2d<?>, OrientedPoint2d, PathElement2d, Point2d, Vector2d, Rectangle2d> {

    private static final long serialVersionUID = 6296312122530686621L;

    private double px;

    private double py;

    private double dx;

    private double dy;

    private double len;

    /** Construct an empty oriented point.
     */
    public OrientedPoint2d() {
        //
    }

    /** Construct an oriented point from a point.
     * @param point the point.
     */
    public OrientedPoint2d(Point2D<?, ?> point) {
        this(point.getX(), point.getY());
    }

    /** Construct an oriented point from a point and its length on a polyline.
     * @param point the point.
     * @param length the length.
     */
    public OrientedPoint2d(Point2D<?, ?> point, double length) {
        this(point.getX(), point.getY(), length);
    }

    /** Construct an oriented point from the two given coordinates.
     * @param x x coordinate of the point.
     * @param y y coordinate of the point.
     */
    public OrientedPoint2d(double x, double y) {
        set(x, y);
    }

    /** Construct an oriented point from the two given coordinates and the length of the point on a polyline.
     * @param x x coordinate of the point.
     * @param y y coordinate of the point.
     * @param length the length
     */
    public OrientedPoint2d(double x, double y, double length) {
        set(x, y, length);
    }

    /** Construct an oriented point from a point and a direction vector.
     * @param point the point.
     * @param vector the direction vector.
     */
    public OrientedPoint2d(Point2D<?, ?> point, Vector2D<?, ?> vector) {
        this(point.getX(), point.getY(), vector.getX(), vector.getY());
    }

    /** Construct an oriented point from a point, its length, and a direction vector.
     * @param point the point.
     * @param length the length of the point
     * @param vector the direction vector.
     */
    public OrientedPoint2d(Point2D<?, ?> point, double length, Vector2D<?, ?> vector) {
        this(point.getX(), point.getY(), length, vector.getX(), vector.getY());
    }

    /** Construct an oriented point from the two given coordinates.
     * @param x x coordinate of the point.
     * @param y y coordinate of the point.
     * @param dirX x coordinate of the vector.
     * @param dirY y coordinate of the vector.
     */
    public OrientedPoint2d(double x, double y, double dirX, double dirY) {
        set(x, y, dirX, dirY);
    }

    /** Construct an oriented point from the given coordinates.
     * @param x x coordinate of the point.
     * @param y y coordinate of the point.
     * @param length the length of the point on the polyline.
     * @param dirX x coordinate of the vector.
     * @param dirY y coordinate of the vector.
     */
    public OrientedPoint2d(double x, double y, double length, double dirX, double dirY) {
        set(x, y, length, dirX, dirY);
    }

    @Pure
    @Override
    public int hashCode() {
        long bits = 1;
        bits = 31 * bits + Double.hashCode(this.px);
        bits = 31 * bits + Double.hashCode(this.py);
        bits = 31 * bits + Double.hashCode(this.dx);
        bits = 31 * bits + Double.hashCode(this.dy);
        bits = 31 * bits + Double.hashCode(this.len);
        final int b = (int) bits;
        return b ^ (b >> 31);
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
    public OrientedPoint2d createTransformedShape(Transform2D transform) {
        if (transform == null || transform.isIdentity()) {
            return clone();
        }
        final Point2d point = getGeomFactory().newPoint(getX(), getY());
        transform.transform(point);
        final double x1 = point.getX();
        final double y1 = point.getY();
        point.set(getDirectionX(), getDirectionY());
        transform.transform(point);
        return getGeomFactory().newOrientedPoint(x1, y1, point.getX(), point.getY());
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
            this.px = x;
            this.py = y;
            fireGeometryChange();
        }
    }

    @Override
    public void set(double x, double y, double length) {
        if (this.px != x || this.py != y || this.len != length) {
            this.px = x;
            this.py = y;
            this.len = length;
            fireGeometryChange();
        }
    }

    @Override
    public void set(double x, double y, double dirX, double dirY) {
        if (this.px != x || this.py != y || this.dx != dirX || this.dy != dirY) {
            this.px = x;
            this.py = y;
            this.dx = dirX;
            this.dy = dirY;
            fireGeometryChange();
        }
    }

    @Override
    public void set(double x, double y, double length, double dirX, double dirY) {
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
    public void setX(int x) {
        if (this.px != x) {
            this.px = x;
            fireGeometryChange();
        }
    }

    @Override
    public void setX(double x) {
        if (this.px != x) {
            this.px = x;
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
            this.py = y;
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
            this.dx = dirX;
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
            this.dy = dirY;
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
            this.len = length;
            fireGeometryChange();
        }
    }

    @Override
    public double getX() {
        return this.px;
    }

    @Override
    public int ix() {
        return (int) Math.round(this.px);
    }

    @Override
    public double getY() {
        return this.py;
    }

    @Override
    public int iy() {
        return (int) Math.round(this.py);
    }

    @Override
    public double getDirectionX() {
        return this.dx;
    }

    @Override
    public int idx() {
        return (int) Math.round(this.dx);
    }

    @Override
    public double getDirectionY() {
        return this.dy;
    }

    @Override
    public int idy() {
        return (int) Math.round(this.dy);
    }

    @Override
    public double getLength() {
        return this.len;
    }

    @Override
    public int ilen() {
        return (int) Math.round(this.len);
    }
}
