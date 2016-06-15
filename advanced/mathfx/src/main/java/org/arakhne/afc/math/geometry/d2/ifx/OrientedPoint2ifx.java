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

package org.arakhne.afc.math.geometry.d2.ifx;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.geometry.MathFXAttributeNames;
import org.arakhne.afc.math.geometry.d2.OrientedPoint2D;
import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.Vector2D;

/** 2D oriented point with integer FX properties.
 *
 * @author $Author: tpiotrow$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class OrientedPoint2ifx
    extends Point2ifx
    implements OrientedPoint2D<Point2ifx, Vector2ifx> {

    private static final long serialVersionUID = 1696624733007552173L;

    private IntegerProperty dx;

    private IntegerProperty dy;

    private IntegerProperty len;

    /** Construct an empty oriented point.
     */
    public OrientedPoint2ifx() {
        //
    }

    /** Constructor by copy.
     * @param tuple the tuple to copy.
     */
    public OrientedPoint2ifx(Point2D<?, ?> tuple) {
        super(tuple);
    }

    /** Construct an oriented point from a point and its length on a polyline.
     * @param point the point.
     * @param length the length.
     */
    public OrientedPoint2ifx(Point2D<?, ?> point, int length) {
        this(point.ix(), point.iy(), length);
    }

    /** Construct an oriented point from the two given coordinates.
     * @param x x coordinate of the point.
     * @param y y coordinate of the point.
     */
    public OrientedPoint2ifx(int x, int y) {
        super(x, y);
    }

    /** Construct an oriented point from the two given coordinates.
     * @param x x coordinate of the point.
     * @param y y coordinate of the point.
     */
    public OrientedPoint2ifx(IntegerProperty x, IntegerProperty y) {
        super(x, y);
    }

    /** Construct an oriented point from the two given coordinates and the length of the point on a polyline.
     * @param x x coordinate of the point.
     * @param y y coordinate of the point.
     * @param length the length
     */
    public OrientedPoint2ifx(int x, int y, int length) {
        super(x, y);
        lengthProperty().set(length);
    }

    /** Construct an oriented point from the two given coordinates and the length of the point on a polyline.
     * @param x x coordinate of the point.
     * @param y y coordinate of the point.
     * @param length the length
     */
    public OrientedPoint2ifx(IntegerProperty x, IntegerProperty y, IntegerProperty length) {
        super(x, y);
        this.len = length;
    }

    /** Construct an oriented point from a point and a direction vector.
     * @param point the point.
     * @param vector the direction vector.
     */
    public OrientedPoint2ifx(Point2D<?, ?> point, Vector2D<?, ?> vector) {
        this(point.ix(), point.iy(), vector.ix(), vector.iy());
    }

    /** Construct an oriented point from a point, its length, and a direction vector.
     * @param point the point.
     * @param length the length of the point
     * @param vector the direction vector.
     */
    public OrientedPoint2ifx(Point2D<?, ?> point, int length, Vector2D<?, ?> vector) {
        this(point.ix(), point.iy(), length, vector.ix(), vector.iy());
    }

    /** Construct an oriented point from the two given coordinates.
     * @param x x coordinate of the point.
     * @param y y coordinate of the point.
     * @param dirX x coordinate of the vector.
     * @param dirY y coordinate of the vector.
     */
    public OrientedPoint2ifx(int x, int y, int dirX, int dirY) {
        super(x, y);
        dirXProperty().set(dirX);
        dirYProperty().set(dirY);
    }

    /** Construct an oriented point from the given coordinates.
     * @param x x coordinate of the point.
     * @param y y coordinate of the point.
     * @param length the length of the point on the polyline.
     * @param dirX x coordinate of the vector.
     * @param dirY y coordinate of the vector.
     */
    public OrientedPoint2ifx(int x, int y, int length, int dirX, int dirY) {
        super(x, y);
        lengthProperty().set(length);
        dirXProperty().set(dirX);
        dirYProperty().set(dirY);
    }

    @Pure
    @Override
    public int hashCode() {
        int bits = 1;
        bits = 31 * bits + super.hashCode();
        bits = 31 * bits + Integer.hashCode(idx());
        bits = 31 * bits + Integer.hashCode(idy());
        bits = 31 * bits + Integer.hashCode(ilen());
        return bits ^ (bits >> 31);
    }

    @Override
    public OrientedPoint2ifx clone() {
        final OrientedPoint2ifx clone = (OrientedPoint2ifx) super.clone();
        if (clone.dx != null) {
            clone.dx = null;
            clone.dirXProperty().set(idx());
        }
        if (clone.dy != null) {
            clone.dy = null;
            clone.dirYProperty().set(idy());
        }
        if (clone.len != null) {
            clone.len = null;
            clone.lengthProperty().set(ilen());
        }
        return clone;
    }

    @Override
    public double getDirectionX() {
        return this.dx == null ? 0 : this.dx.doubleValue();
    }

    @Override
    public int idx() {
        return this.dx == null ? 0 : this.dx.intValue();
    }

    @Override
    public void setDirectionX(int dirX) {
        dirXProperty().set(dirX);
    }

    @Override
    public void setDirectionX(double dirX) {
        dirXProperty().set((int) Math.round(dirX));
    }

    @Override
    public double getDirectionY() {
        return this.dy == null ? 0 : this.dy.doubleValue();
    }

    @Override
    public int idy() {
        return this.dy == null ? 0 : this.dy.intValue();
    }

    @Override
    public void setDirectionY(int dirY) {
        dirYProperty().set(dirY);
    }

    @Override
    public void setDirectionY(double dirY) {
        dirYProperty().set((int) Math.round(dirY));
    }

    @Override
    public double getLength() {
        return this.len == null ? 0 : this.len.doubleValue();
    }

    @Override
    public int ilen() {
        return this.len == null ? 0 : this.len.intValue();
    }

    @Override
    public void setLength(double length) {
        lengthProperty().set((int) Math.round(length));
    }

    @Override
    public void setLength(int length) {
        lengthProperty().set(length);
    }

    /** Replies the property that is the x coordinate of the direction vector.
     *
     * @return the direction vector x property.
     */
    @Pure
    public IntegerProperty dirXProperty() {
        if (this.dx == null) {
            this.dx = new SimpleIntegerProperty(this, MathFXAttributeNames.X1);
        }
        return this.dx;
    }

    /** Replies the property that is the y coordinate of the direction vector.
     *
     * @return the direction vector y property.
     */
    @Pure
    public IntegerProperty dirYProperty() {
        if (this.dy == null) {
            this.dy = new SimpleIntegerProperty(this, MathFXAttributeNames.Y1);
        }
        return this.dy;
    }

    /** Replies the length property of the point.
     *
     * @return the length property.
     */
    @Pure
    public IntegerProperty lengthProperty() {
        if (this.len == null) {
            this.len = new SimpleIntegerProperty(this, MathFXAttributeNames.LENGTH);
        }
        return this.len;
    }
}
