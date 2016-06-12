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

package org.arakhne.afc.math.geometry.d2.dfx;

import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.geometry.d2.MathFXAttributeNames;
import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.Vector2D;
import org.arakhne.afc.math.geometry.d2.afp.OrientedPoint2afp;

/** 2D oriented point with double precison floating point FX properties.
 *
 * @author $Author: tpiotrow$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class OrientedPoint2dfx
    extends AbstractShape2dfx<OrientedPoint2dfx>
    implements OrientedPoint2afp<Shape2dfx<?>, OrientedPoint2dfx, PathElement2dfx, Point2dfx, Vector2dfx, Rectangle2dfx> {

    private static final long serialVersionUID = 1696624733007552173L;

    private DoubleProperty px;

    private DoubleProperty py;

    private DoubleProperty dx;

    private DoubleProperty dy;

    private DoubleProperty len;

    /** Construct an empty oriented point.
     */
    public OrientedPoint2dfx() {
        //
    }

    /** Construct an oriented point from a point.
     * @param point the point.
     */
    public OrientedPoint2dfx(Point2D<?, ?> point) {
        this(point.getX(), point.getY());
    }

    /** Construct an oriented point from a point and its length on a polyline.
     * @param point the point.
     * @param length the length.
     */
    public OrientedPoint2dfx(Point2D<?, ?> point, double length) {
        this(point.getX(), point.getY(), length);
    }

    /** Construct an oriented point from the two given coordinates.
     * @param x x coordinate of the point.
     * @param y y coordinate of the point.
     */
    public OrientedPoint2dfx(double x, double y) {
        xProperty().set(x);
        yProperty().set(y);
    }

    /** Construct an oriented point from the two given coordinates.
     * @param x x coordinate of the point.
     * @param y y coordinate of the point.
     */
    public OrientedPoint2dfx(DoubleProperty x, DoubleProperty y) {
        this.px = x;
        this.py = y;
    }

    /** Construct an oriented point from the two given coordinates and the length of the point on a polyline.
     * @param x x coordinate of the point.
     * @param y y coordinate of the point.
     * @param length the length
     */
    public OrientedPoint2dfx(double x, double y, double length) {
        xProperty().set(x);
        yProperty().set(y);
        lengthProperty().set(length);
    }

    /** Construct an oriented point from the two given coordinates and the length of the point on a polyline.
     * @param x x coordinate of the point.
     * @param y y coordinate of the point.
     * @param length the length
     */
    public OrientedPoint2dfx(DoubleProperty x, DoubleProperty y, DoubleProperty length) {
        this.px = x;
        this.py = y;
        this.len = length;
    }

    /** Construct an oriented point from a point and a direction vector.
     * @param point the point.
     * @param vector the direction vector.
     */
    public OrientedPoint2dfx(Point2D<?, ?> point, Vector2D<?, ?> vector) {
        this(point.getX(), point.getY(), vector.getX(), vector.getY());
    }

    /** Construct an oriented point from a point, its length, and a direction vector.
     * @param point the point.
     * @param length the length of the point
     * @param vector the direction vector.
     */
    public OrientedPoint2dfx(Point2D<?, ?> point, double length, Vector2D<?, ?> vector) {
        this(point.getX(), point.getY(), length, vector.getX(), vector.getY());
    }

    /** Construct an oriented point from the two given coordinates.
     * @param x x coordinate of the point.
     * @param y y coordinate of the point.
     * @param dirX x coordinate of the vector.
     * @param dirY y coordinate of the vector.
     */
    public OrientedPoint2dfx(double x, double y, double dirX, double dirY) {
        xProperty().set(x);
        yProperty().set(y);
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
    public OrientedPoint2dfx(double x, double y, double length, double dirX, double dirY) {
        xProperty().set(x);
        yProperty().set(y);
        lengthProperty().set(length);
        dirXProperty().set(dirX);
        dirYProperty().set(dirY);
    }

    @Pure
    @Override
    public int hashCode() {
        int bits = 1;
        bits = 31 * bits + Double.hashCode(getX());
        bits = 31 * bits + Double.hashCode(getY());
        bits = 31 * bits + Double.hashCode(getDirectionX());
        bits = 31 * bits + Double.hashCode(getDirectionY());
        bits = 31 * bits + Double.hashCode(getLength());
        return bits ^ (bits >> 31);
    }

    @Override
    public OrientedPoint2dfx clone() {
        final OrientedPoint2dfx clone = super.clone();
        if (clone.px != null) {
            clone.px = null;
            clone.xProperty().set(getX());
        }
        if (clone.py != null) {
            clone.py = null;
            clone.yProperty().set(getY());
        }
        if (clone.dx != null) {
            clone.dx = null;
            clone.dirXProperty().set(getDirectionX());
        }
        if (clone.dy != null) {
            clone.dy = null;
            clone.dirYProperty().set(getDirectionY());
        }
        if (clone.len != null) {
            clone.len = null;
            clone.lengthProperty().set(getLength());
        }
        return clone;
    }

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
    public double getX() {
        return this.px == null ? 0 : this.px.doubleValue();
    }

    @Override
    public int ix() {
        return this.px == null ? 0 : this.px.intValue();
    }

    @Override
    public double getY() {
        return this.py == null ? 0 : this.py.doubleValue();
    }

    @Override
    public int iy() {
        return this.py == null ? 0 : this.py.intValue();
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
    public double getDirectionY() {
        return this.dy == null ? 0 : this.dy.doubleValue();
    }

    @Override
    public int idy() {
        return this.dy == null ? 0 : this.dy.intValue();
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
    public void setX(int x) {
        xProperty().set(x);
    }

    @Override
    public void setX(double x) {
        xProperty().set(x);
    }

    @Override
    public void setY(int y) {
        yProperty().set(y);
    }

    @Override
    public void setY(double y) {
        yProperty().set(y);
    }

    @Override
    public void setDirectionX(int dirX) {
        dirXProperty().set(dirX);
    }

    @Override
    public void setDirectionX(double dirX) {
        dirXProperty().set(dirX);
    }

    @Override
    public void setDirectionY(int dirY) {
        dirYProperty().set(dirY);
    }

    @Override
    public void setDirectionY(double dirY) {
        dirYProperty().set(dirY);
    }

    @Override
    public void setLength(double length) {
        lengthProperty().set(length);
    }

    @Override
    public void setLength(int length) {
        lengthProperty().set(length);
    }

    /** Replies the x property.
     *
     * @return the x property.
     */
    @Pure
    public DoubleProperty xProperty() {
        if (this.px == null) {
            this.px = new SimpleDoubleProperty(this, MathFXAttributeNames.X);
        }
        return this.px;
    }

    /** Replies the y property.
     *
     * @return the y property.
     */
    @Pure
    public DoubleProperty yProperty() {
        if (this.py == null) {
            this.py = new SimpleDoubleProperty(this, MathFXAttributeNames.Y);
        }
        return this.py;
    }

    /** Replies the property that is the x coordinate of the direction vector.
     *
     * @return the direction vector x property.
     */
    @Pure
    public DoubleProperty dirXProperty() {
        if (this.dx == null) {
            this.dx = new SimpleDoubleProperty(this, MathFXAttributeNames.X1);
        }
        return this.dx;
    }

    /** Replies the property that is the y coordinate of the direction vector.
     *
     * @return the direction vector y property.
     */
    @Pure
    public DoubleProperty dirYProperty() {
        if (this.dy == null) {
            this.dy = new SimpleDoubleProperty(this, MathFXAttributeNames.Y1);
        }
        return this.dy;
    }

    /** Replies the length property of the point.
     *
     * @return the length property.
     */
    @Pure
    public DoubleProperty lengthProperty() {
        if (this.len == null) {
            this.len = new SimpleDoubleProperty(this, MathFXAttributeNames.LENGTH);
        }
        return this.len;
    }

    @Override
    public ObjectProperty<Rectangle2dfx> boundingBoxProperty() {
        if (this.boundingBox == null) {
            this.boundingBox = new SimpleObjectProperty<>(this, MathFXAttributeNames.BOUNDING_BOX);
            this.boundingBox.bind(Bindings.createObjectBinding(() -> {
                return toBoundingBox();
            }, this.dx, this.dy, this.px, this.py));
        }
        return this.boundingBox;
    }
}
