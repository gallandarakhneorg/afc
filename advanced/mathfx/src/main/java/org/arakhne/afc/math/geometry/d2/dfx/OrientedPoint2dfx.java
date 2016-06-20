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

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.geometry.MathFXAttributeNames;
import org.arakhne.afc.math.geometry.d2.OrientedPoint2D;
import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.Tuple2D;
import org.arakhne.afc.math.geometry.d2.Vector2D;

/** 2D oriented point with double precison floating point FX properties.
 *
 * @author $Author: tpiotrow$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class OrientedPoint2dfx
    extends Point2dfx
    implements OrientedPoint2D<Point2dfx, Vector2dfx> {

    private static final long serialVersionUID = 1696624733007552173L;

    private DoubleProperty dx;

    private DoubleProperty dy;

    /** Construct an empty oriented point.
     */
    public OrientedPoint2dfx() {
        //
    }

    /** Constructor by copy.
     * @param tuple the tuple to copy.
     */
    public OrientedPoint2dfx(Tuple2D<?> tuple) {
        super(tuple);
    }

    /** Construct an oriented point from the two given coordinates.
     * @param x x coordinate of the point.
     * @param y y coordinate of the point.
     */
    public OrientedPoint2dfx(double x, double y) {
       super(x, y);
    }

    /** Construct an oriented point from the two given coordinates.
     * @param x x coordinate of the point.
     * @param y y coordinate of the point.
     */
    public OrientedPoint2dfx(DoubleProperty x, DoubleProperty y) {
        super(x, y);
    }

    /** Construct an oriented point from a point and a direction vector.
     * @param point the point.
     * @param vector the direction vector.
     */
    public OrientedPoint2dfx(Point2D<?, ?> point, Vector2D<?, ?> vector) {
        this(point.getX(), point.getY(), vector.getX(), vector.getY());
    }

    /** Construct an oriented point from the two given coordinates.
     * @param x x coordinate of the point.
     * @param y y coordinate of the point.
     * @param dirX x coordinate of the vector.
     * @param dirY y coordinate of the vector.
     */
    public OrientedPoint2dfx(double x, double y, double dirX, double dirY) {
        super(x, y);
        dirXProperty().set(dirX);
        dirYProperty().set(dirY);
    }

    @Pure
    @Override
    public int hashCode() {
        int bits = 1;
        bits = 31 * bits + super.hashCode();
        bits = 31 * bits + Double.hashCode(getDirectionX());
        bits = 31 * bits + Double.hashCode(getDirectionY());
        return bits ^ (bits >> 31);
    }

    @Override
    public OrientedPoint2dfx clone() {
        final OrientedPoint2dfx clone = (OrientedPoint2dfx) super.clone();
        if (clone.dx != null) {
            clone.dx = null;
            clone.dirXProperty().set(getDirectionX());
        }
        if (clone.dy != null) {
            clone.dy = null;
            clone.dirYProperty().set(getDirectionY());
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
        dirXProperty().set(dirX);
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
        dirYProperty().set(dirY);
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
}
