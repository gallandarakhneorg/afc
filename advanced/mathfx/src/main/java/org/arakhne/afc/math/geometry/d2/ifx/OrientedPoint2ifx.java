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

import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.geometry.MathFXAttributeNames;
import org.arakhne.afc.math.geometry.d2.OrientedPoint2D;
import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.Tuple2D;
import org.arakhne.afc.math.geometry.d2.Vector2D;
import org.arakhne.afc.vmutil.asserts.AssertMessages;

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

    /**
     * Tangent vector to this point.
     */
    protected Vector2ifx tangent;

    /**
     * Normal vector to the point.
     */
    private ObjectProperty<Vector2ifx> normalProperty;

    /** Construct an empty oriented point.
     */
    public OrientedPoint2ifx() {
        //
    }

    /** Constructor by copy.
     * @param tuple the tuple to copy.
     */
    public OrientedPoint2ifx(Tuple2D<?> tuple) {
        super(tuple);
    }

    /** Constructor by setting.
     * @param tuple the tuple to set.
     */
    public OrientedPoint2ifx(Tuple2ifx<?> tuple) {
        assert tuple != null : AssertMessages.notNullParameter();
        this.x = tuple.x;
        this.y = tuple.y;
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

    /** Construct an oriented point from a point and a tangent vector.
     * @param point the point.
     * @param tangent the tangent vector.
     */
    public OrientedPoint2ifx(Point2D<?, ?> point, Vector2D<?, ?> tangent) {
        assert point != null : AssertMessages.notNullParameter(0);
        assert tangent != null : AssertMessages.notNullParameter(1);
        set(point.ix(), point.iy(), tangent.ix(), tangent.iy());
    }

    /** Constructor by setting from a point and a tangent vector.
     * @param point the point to set.
     * @param tangent the tangent vector to set.
     */
    public OrientedPoint2ifx(Point2ifx point, Vector2ifx tangent) {
        assert point != null : AssertMessages.notNullParameter(0);
        assert tangent != null : AssertMessages.notNullParameter(1);
        this.x = point.x;
        this.y = point.y;
        this.tangent = tangent;
    }

    /** Construct an oriented point from the two given coordinates.
     * @param x x coordinate of the point.
     * @param y y coordinate of the point.
     * @param tanX x coordinate of the vector.
     * @param tanY y coordinate of the vector.
     */
    public OrientedPoint2ifx(int x, int y, int tanX, int tanY) {
        super(x, y);
        tanXProperty().set(tanX);
        tanYProperty().set(tanY);
    }

    @Pure
    @Override
    public int hashCode() {
        int bits = 1;
        bits = 31 * bits + super.hashCode();
        bits = 31 * bits + Integer.hashCode(itx());
        bits = 31 * bits + Integer.hashCode(ity());
        return bits ^ (bits >> 31);
    }

    @Override
    public OrientedPoint2ifx clone() {
        final OrientedPoint2ifx clone = (OrientedPoint2ifx) super.clone();
        if (clone.tangent != null) {
            clone.tangent = null;
            clone.tangent = this.tangent.clone();
        }
        return clone;
    }

    @Override
    public double getTangentX() {
        return this.tangent.getX();
    }

    @Override
    public int itx() {
        return this.tangent.ix();
    }

    @Override
    public void setTangentX(int tanX) {
        this.tangent.setX(tanX);
    }

    @Override
    public void setTangentX(double tanX) {
        this.tangent.setX(tanX);
    }

    @Override
    public double getTangentY() {
        return this.tangent.getY();
    }

    @Override
    public int ity() {
        return this.tangent.iy();
    }

    @Override
    public void setTangentY(int tanY) {
        this.tangent.setY(tanY);
    }

    @Override
    public void setTangentY(double tanY) {
        this.tangent.setY(tanY);
    }

    /** Replies the property that is the x coordinate of the tangent vector.
     *
     * @return the tangent vector x property.
     */
    @Pure
    public IntegerProperty tanXProperty() {
        return this.tangent.xProperty();
    }

    /** Replies the property that is the y coordinate of the tangent vector.
     *
     * @return the tangent vector y property.
     */
    @Pure
    public IntegerProperty tanYProperty() {
        return this.tangent.yProperty();
    }

    @Override
    public Vector2ifx getTangent() {
        return this.tangent;
    }

    @Override
    public void setTangent(Vector2ifx tangent) {
        this.tangent = tangent;
    }

    @Override
    public Vector2ifx getNormal() {
        if (this.normalProperty == null) {
            this.normalProperty = new SimpleObjectProperty<>(this, MathFXAttributeNames.NORMAL);
            this.normalProperty.bind(Bindings.createObjectBinding(() -> this.tangent.toOrthogonalVector(),
                    this.tangent.xProperty(), this.tangent.yProperty()));
        }
        return this.normalProperty.get();
    }

    /** Replies the property that is the x coordinate of the normal vector.
     *
     * @return the normal vector x property.
     */
    @Pure ReadOnlyIntegerProperty norXProperty() {
        return getNormal().xProperty();
    }

    /** Replies the property that is the y coordinate of the normal vector.
     *
     * @return the normal vector y property.
     */
    @Pure ReadOnlyIntegerProperty norYProperty() {
        return getNormal().yProperty();
    }

    @Override
    public double getNormalX() {
        return getNormal().getX();
    }

    @Override
    public int inx() {
        return getNormal().ix();
    }

    @Override
    public double getNormalY() {
        return getNormal().getY();
    }

    @Override
    public int iny() {
        return getNormal().iy();
    }
}
