/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2018 The original authors, and other authors.
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

package org.arakhne.afc.math.geometry.d3.ifx;

import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.geometry.MathFXAttributeNames;
import org.arakhne.afc.math.geometry.d3.OrientedPoint3D;
import org.arakhne.afc.math.geometry.d3.Point3D;
import org.arakhne.afc.math.geometry.d3.Tuple3D;
import org.arakhne.afc.math.geometry.d3.Vector3D;
import org.arakhne.afc.vmutil.asserts.AssertMessages;

/** 3D oriented point with double precison floating point FX properties.
 *
 * @author $Author: tpiotrow$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class OrientedPoint3ifx extends Point3ifx implements OrientedPoint3D<Point3ifx, Vector3ifx> {

    private static final long serialVersionUID = 1696624733007552173L;

    /**
     * Tangent vector to this point.
     */
    protected Vector3ifx tangent = new Vector3ifx();

    /**
     * Normal vector to the point.
     */
    private Vector3ifx normal = new Vector3ifx();

    /**
     * Normal vector to the point.
     */
    private ObjectProperty<Vector3ifx> swayProperty;

    /** Construct an empty oriented point.
     */
    public OrientedPoint3ifx() {
        //
    }

    /** Constructor by copy.
     * @param tuple the tuple to copy.
     */
    public OrientedPoint3ifx(Tuple3D<?> tuple) {
        super(tuple);
    }

    /** Constructor by setting.
     * @param tuple the tuple to set.
     */
    public OrientedPoint3ifx(Tuple3ifx<?> tuple) {
        assert tuple != null : AssertMessages.notNullParameter();
        this.x = tuple.x;
        this.y = tuple.y;
        this.z = tuple.z;
    }

    /** Construct an oriented point from the three given coordinates.
     * @param x x coordinate of the point.
     * @param y y coordinate of the point.
     * @param z z coordinate of the point.
     */
    public OrientedPoint3ifx(int x, int y, int z) {
       super(x, y, z);
    }

    /** Construct an oriented point from the three given coordinates.
     * @param x x coordinate of the point.
     * @param y y coordinate of the point.
     * @param z z coordinate of the point.
     */
    public OrientedPoint3ifx(IntegerProperty x, IntegerProperty y, IntegerProperty z) {
        super(x, y, z);
    }

    /** Construct an oriented point from a point and a tangent vector.
     * @param point the point.
     * @param tangent the tangent vector.
     * @param normal the normal vector.
     */
    public OrientedPoint3ifx(Point3D<?, ?> point, Vector3D<?, ?> tangent, Vector3D<?, ?> normal) {
        assert point != null : AssertMessages.notNullParameter(0);
        assert tangent != null : AssertMessages.notNullParameter(1);
        set(point.getX(), point.getY(), point.getZ(), tangent.getX(), tangent.getY(), tangent.getZ(), normal.getX(),
                normal.getY(), normal.getZ());
    }

    /** Constructor by setting from a point and a tangent vector.
     * @param point the point.
     * @param tangent the tangent vector.
     * @param normal the normal vector.
     */
    public OrientedPoint3ifx(Point3ifx point, Vector3ifx tangent, Vector3ifx normal) {
        assert point != null : AssertMessages.notNullParameter(0);
        assert tangent != null : AssertMessages.notNullParameter(1);
        this.x = point.x;
        this.y = point.y;
        this.z = point.z;
        this.tangent = tangent;
        this.normal = normal;
    }

    /** Construct an oriented point from the two given coordinates.
     * @param x x coordinate of the point.
     * @param y y coordinate of the point.
     * @param z z coordinate of the point.
     * @param tanX x coordinate of the tangent vector.
     * @param tanY y coordinate of the tangent vector.
     * @param tanZ z coordinate of the tangent vector.
     * @param norX x coordinate of the normal vector.
     * @param norY y coordinate of the normal vector.
     * @param norZ z coordinate of the normal vector.
     */
    public OrientedPoint3ifx(int x, int y, int z, int tanX, int tanY, int tanZ, int norX, int norY,
            int norZ) {
        super(x, y, z);
        tanXProperty().set(tanX);
        tanYProperty().set(tanY);
        tanZProperty().set(tanZ);
        norXProperty().set(norX);
        norYProperty().set(norY);
        norZProperty().set(norZ);
    }

    @Pure
    @Override
    @SuppressWarnings("checkstyle:equalshashcode")
    public int hashCode() {
        int bits = 1;
        bits = 31 * bits + super.hashCode();
        bits = 31 * bits + Double.hashCode(ix());
        bits = 31 * bits + Double.hashCode(iy());
        bits = 31 * bits + Double.hashCode(iz());
        bits = 31 * bits + Double.hashCode(inx());
        bits = 31 * bits + Double.hashCode(iny());
        bits = 31 * bits + Double.hashCode(inz());
        return bits ^ (bits >> 31);
    }

    @Override
    public OrientedPoint3ifx clone() {
        final OrientedPoint3ifx clone = (OrientedPoint3ifx) super.clone();
        if (clone.tangent != null) {
            clone.tangent = null;
            clone.tangent = this.tangent.clone();
        }
        if (clone.normal != null) {
            clone.normal = null;
            clone.normal = this.normal.clone();
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

    @Override
    public double getTangentZ() {
        return this.tangent.getZ();
    }

    @Override
    public int itz() {
        return this.tangent.iz();
    }

    @Override
    public void setTangentZ(int tanZ) {
        this.tangent.setZ(tanZ);
    }

    @Override
    public void setTangentZ(double tanZ) {
        this.tangent.setZ(tanZ);
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

    /** Replies the property that is the y coordinate of the tangent vector.
     *
     * @return the tangent vector y property.
     */
    @Pure
    public IntegerProperty tanZProperty() {
        return this.tangent.zProperty();
    }

    @Override
    public Vector3ifx getTangent() {
        return this.tangent;
    }

    @Override
    public void setTangent(Vector3ifx tangent) {
        this.tangent = tangent;
    }

    @Override
    public double getNormalX() {
        return this.normal.getX();
    }

    @Override
    public int inx() {
        return this.normal.ix();
    }

    @Override
    public void setNormalX(int norX) {
        this.normal.setX(norX);
    }

    @Override
    public void setNormalX(double norX) {
        this.normal.setX(norX);
    }

    @Override
    public double getNormalY() {
        return this.normal.getY();
    }

    @Override
    public int iny() {
        return this.normal.iy();
    }

    @Override
    public void setNormalY(int norY) {
        this.normal.setY(norY);
    }

    @Override
    public void setNormalY(double norY) {
        this.normal.setY(norY);
    }

    @Override
    public double getNormalZ() {
        return this.normal.getZ();
    }

    @Override
    public int inz() {
        return this.normal.iz();
    }

    @Override
    public void setNormalZ(int norZ) {
        this.normal.setZ(norZ);
    }

    @Override
    public void setNormalZ(double norZ) {
        this.normal.setZ(norZ);
    }

    /** Replies the property that is the x coordinate of the normal vector.
     *
     * @return the normal vector x property.
     */
    @Pure
    public IntegerProperty norXProperty() {
        return this.normal.xProperty();
    }

    /** Replies the property that is the y coordinate of the normal vector.
     *
     * @return the normal vector y property.
     */
    @Pure
    public IntegerProperty norYProperty() {
        return this.normal.yProperty();
    }

    /** Replies the property that is the y coordinate of the normal vector.
     *
     * @return the normal vector y property.
     */
    @Pure
    public IntegerProperty norZProperty() {
        return this.normal.zProperty();
    }

    @Override
    public Vector3ifx getNormal() {
        return this.normal;
    }

    @Override
    public void setNormal(Vector3ifx normal) {
        this.normal = normal;
    }

    @Override
    public Vector3ifx getSway() {
        if (this.swayProperty == null) {
            this.swayProperty = new SimpleObjectProperty<>(this, MathFXAttributeNames.SWAY);
            this.swayProperty.bind(Bindings.createObjectBinding(() -> this.tangent.cross(this.normal), this.tangent.xProperty(),
                    this.tangent.yProperty(), this.tangent.zProperty(), this.normal.xProperty(), this.normal.yProperty(),
                    this.normal.zProperty()));
        }
        return this.swayProperty.get();
    }

    /** Replies the property that is the x coordinate of the sway vector.
     *
     * @return the sway vector x property.
     */
    @Pure ReadOnlyIntegerProperty swayXProperty() {
        return getSway().xProperty();
    }

    /** Replies the property that is the y coordinate of the sway vector.
     *
     * @return the sway vector y property.
     */
    @Pure ReadOnlyIntegerProperty swayYProperty() {
        return getSway().yProperty();
    }

    /** Replies the property that is the z coordinate of the sway vector.
     *
     * @return the sway vector z property.
     */
    @Pure ReadOnlyIntegerProperty swayZProperty() {
        return getSway().zProperty();
    }

    @Override
    public double getSwayX() {
        return getSway().getX();
    }

    @Override
    public int isx() {
        return getSway().ix();
    }

    @Override
    public double getSwayY() {
        return getSway().getY();
    }

    @Override
    public int isy() {
        return getSway().iy();
    }

    @Override
    public double getSwayZ() {
        return getSway().getZ();
    }

    @Override
    public int isz() {
        return getSway().iz();
    }
}
