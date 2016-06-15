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

import org.arakhne.afc.math.geometry.d2.OrientedPoint2D;
import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.Tuple2D;
import org.arakhne.afc.math.geometry.d2.Vector2D;

/** 2D oriented point with double precision floating-point numbers.
 *
 * @author $Author: tpiotrow$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class OrientedPoint2d
    extends Point2d
    implements OrientedPoint2D<Point2d, Vector2d> {

    private static final long serialVersionUID = 6296312122530686621L;

    private double dx;

    private double dy;

    private double len;

    /** Construct an empty oriented point.
     */
    public OrientedPoint2d() {
        //
    }

    /** Constructor by copy.
     * @param tuple the tuple to copy.
     */
    public OrientedPoint2d(Tuple2D<?> tuple) {
        super(tuple);
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
        super(x, y);
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
        int bits = 1;
        bits = 31 * bits + super.hashCode();
        bits = 31 * bits + Double.hashCode(this.dx);
        bits = 31 * bits + Double.hashCode(this.dy);
        bits = 31 * bits + Double.hashCode(this.len);
        return bits ^ (bits >> 31);
    }

    @Override
    public void setDirectionX(int dirX) {
        this.dx = dirX;
    }

    @Override
    public void setDirectionX(double dirX) {
        this.dx = dirX;
    }

    @Override
    public void setDirectionY(int dirY) {
        this.dy = dirY;
    }

    @Override
    public void setDirectionY(double dirY) {
        this.dy = dirY;
    }

    @Override
    public void setLength(int length) {
        this.len = length;
    }

    @Override
    public void setLength(double length) {
        this.len = length;
    }

    @Override
    public double getDirectionX() {
        return this.dx;
    }

    @Override
    public int idx() {
        return (int) this.dx;
    }

    @Override
    public double getDirectionY() {
        return this.dy;
    }

    @Override
    public int idy() {
        return (int) this.dy;
    }

    @Override
    public double getLength() {
        return this.len;
    }

    @Override
    public int ilen() {
        return (int) this.len;
    }
}
