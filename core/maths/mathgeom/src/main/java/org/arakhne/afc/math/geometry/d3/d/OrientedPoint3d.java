/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2019 The original authors, and other authors.
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

package org.arakhne.afc.math.geometry.d3.d;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.geometry.d3.OrientedPoint3D;
import org.arakhne.afc.math.geometry.d3.Point3D;
import org.arakhne.afc.math.geometry.d3.Tuple3D;
import org.arakhne.afc.math.geometry.d3.Vector3D;

/** 2D oriented point with double precision floating-point numbers.
 *
 * @author $Author: tpiotrow$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
// TODO : enforce orthogonality constraint
public class OrientedPoint3d
    extends Point3d
    implements OrientedPoint3D<Point3d, Vector3d> {

    private static final long serialVersionUID = 6296312122530686621L;

    private double tx;

    private double ty;

    private double tz;

    private double nx;

    private double ny;

    private double nz;

    /** Construct an empty oriented point.
     */
    public OrientedPoint3d() {
        //
    }

    /** Constructor by copy.
     * @param tuple the tuple to copy.
     */
    public OrientedPoint3d(Tuple3D<?> tuple) {
        super(tuple);
    }

    /** Construct an oriented point from the two given coordinates.
     * @param x x coordinate of the point.
     * @param y y coordinate of the point.
     * @param z z coordinate of the point.
     */
    public OrientedPoint3d(double x, double y, double z) {
        super(x, y, z);
    }

    /** Construct an oriented point from a point and a tangent vector.
     * @param point the point.
     * @param vector the tangent vector.
     */
    public OrientedPoint3d(Point3D<?, ?> point, Vector3D<?, ?> vector) {
        this(point.getX(), point.getY(), point.getZ(), vector.getX(), vector.getY(), vector.getZ());
    }

    /** Construct an oriented point from the given coordinates.
     * @param x x coordinate of the point.
     * @param y y coordinate of the point.
     * @param z z coordinate of the point.
     * @param tanX x coordinate of the vector.
     * @param tanY y coordinate of the vector.
     * @param tanZ z coordinate of the vector.
     */
    public OrientedPoint3d(double x, double y, double z, double tanX, double tanY, double tanZ) {
        set(x, y, z, tanX, tanY, tanZ);
    }

    /** Construct an oriented point from the given coordinates.
     * @param x x coordinate of the point.
     * @param y y coordinate of the point.
     * @param z z coordinate of the point.
     * @param tanX x coordinate of the vector.
     * @param tanY y coordinate of the vector.
     * @param tanZ z coordinate of the vector.
     * @param norX x coordinate of the normal vector.
     * @param norY y coordinate of the normal vector.
     * @param norZ z coordinate of the normal vector.
     * @throws Exception this exception is thrown when the given vectors are not orthogonal.
     */
    public OrientedPoint3d(double x, double y, double z, double tanX, double tanY, double tanZ, double norX, double norY,
            double norZ) throws Exception {
        set(x, y, z, tanX, tanY, tanZ, norX, norY, norZ);
    }

    @Pure
    @Override
    @SuppressWarnings("checkstyle:equalshashcode")
    public int hashCode() {
        int bits = 1;
        bits = 31 * bits + super.hashCode();
        bits = 31 * bits + Double.hashCode(this.tx);
        bits = 31 * bits + Double.hashCode(this.ty);
        bits = 31 * bits + Double.hashCode(this.tz);
        bits = 31 * bits + Double.hashCode(this.nx);
        bits = 31 * bits + Double.hashCode(this.ny);
        bits = 31 * bits + Double.hashCode(this.nz);
        return bits ^ (bits >> 31);
    }

    @Override
    public void setTangentX(int tanX) {
        this.tx = tanX;
    }

    @Override
    public void setTangentX(double tanX) {
        this.tx = tanX;
    }

    @Override
    public void setTangentY(int tanY) {
        this.ty = tanY;
    }

    @Override
    public void setTangentY(double tanY) {
        this.ty = tanY;
    }

    @Override
    public void setTangentZ(int tanZ) {
        this.tz = tanZ;
    }

    @Override
    public void setTangentZ(double tanZ) {
        this.tz = tanZ;
    }

    @Override
    public double getTangentX() {
        return this.tx;
    }

    @Override
    public int itx() {
        return (int) this.tx;
    }

    @Override
    public double getTangentY() {
        return this.ty;
    }

    @Override
    public int ity() {
        return (int) this.ty;
    }

    @Override
    public double getTangentZ() {
        return this.tz;
    }

    @Override
    public int itz() {
        return (int) this.tz;
    }

    @Override
    public double getNormalX() {
        return this.nx;
    }

    @Override
    public int inx() {
        return (int) this.nx;
    }

    @Override
    public void setNormalX(int norX) {
        this.nx = norX;
    }

    @Override
    public void setNormalX(double norX) {
        this.nx = norX;
    }

    @Override
    public double getNormalY() {
        return this.ny;
    }

    @Override
    public int iny() {
        return (int) this.ny;
    }

    @Override
    public void setNormalY(int norY) {
        this.ny = norY;
    }

    @Override
    public void setNormalY(double norY) {
        this.ny = norY;
    }

    @Override
    public double getNormalZ() {
        return this.nz;
    }

    @Override
    public int inz() {
        return (int) this.nz;
    }

    @Override
    public void setNormalZ(int norZ) {
        this.nz = norZ;
    }

    @Override
    public void setNormalZ(double norZ) {
        this.nz = norZ;
    }

    @Override
    public Vector3d getTangent() {
        return getGeomFactory().newVector(this.tx, this.ty, this.tz);
    }

    @Override
    public Vector3d getNormal() {
        return getGeomFactory().newVector(this.nx, this.ny, this.nz);
    }

    @Override
    public Vector3d getSway() {
        return getTangent().cross(getNormal());
    }
}
