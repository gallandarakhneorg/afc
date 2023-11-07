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

package org.arakhne.afc.math.test.geometry.d3.d;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.arakhne.afc.math.geometry.coordinatesystem.CoordinateSystem3D;
import org.arakhne.afc.math.geometry.d3.Shape3D;
import org.arakhne.afc.math.geometry.d3.d.OrientedPoint3d;
import org.arakhne.afc.math.geometry.d3.d.Quaternion4d;
import org.arakhne.afc.math.geometry.d3.d.Sphere3d;
import org.arakhne.afc.math.geometry.d3.d.Vector3d;
import org.arakhne.afc.math.test.geometry.AbstractPoint3DTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

@SuppressWarnings("all")
public class OrientedPoint3dTest extends AbstractPoint3DTest<OrientedPoint3d, Vector3d, Quaternion4d, OrientedPoint3d> {

	@Override
	public boolean isIntCoordinates() {
		return false;
	}
	
	@Override
	public Vector3d createVector(double x, double y, double z) {
		return new Vector3d(x, y, z);
	}

	@Override
	public OrientedPoint3d createPoint(double x, double y, double z) {
		return new OrientedPoint3d(x, y, z);
	}

	@Override
	public OrientedPoint3d createTuple(double x, double y, double z) {
		return new OrientedPoint3d(x, y, z);
	}

	@Override
	public void operator_andShape3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Shape3D shape = new Sphere3d(5, 8, 0, 5);
		assertFalse(createPoint(0,0, 0).operator_and(shape));
		assertFalse(createPoint(11,10, 0).operator_and(shape));
		assertFalse(createPoint(11,50, 0).operator_and(shape));
		assertFalse(createPoint(9,12, 0).operator_and(shape));
		assertTrue(createPoint(9,11, 0).operator_and(shape));
		assertTrue(createPoint(8,12, 0).operator_and(shape));
		assertTrue(createPoint(3,7, 0).operator_and(shape));
		assertFalse(createPoint(10,11, 0).operator_and(shape));
		assertTrue(createPoint(9,10, 0).operator_and(shape));
	}
	
	@Override
	public void operator_upToShape3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		Shape3D shape = new Sphere3d(5, 8, 0, 5);
		assertEpsilonEquals(3.74643, createPoint(.5,.5, 0).operator_upTo(shape));
		assertEpsilonEquals(7.9769, createPoint(-1.2,-3.4, 0).operator_upTo(shape));
		assertEpsilonEquals(1.6483, createPoint(-1.2,5.6, 0).operator_upTo(shape));
		assertEpsilonEquals(0, createPoint(7.6,5.6, 0).operator_upTo(shape));
	}

	@DisplayName("setTangentX(int)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
    public void setTangentXInt(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.t.setTangentX(1254);
		assertEpsilonEquals(1254, this.t.getTangentX());
		assertEquals(1254, this.t.itx());
    }

	@DisplayName("setTangentX(double)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
    public void setTangentXDouble(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.t.setTangentX(1254.26);
		assertEpsilonEquals(1254.26, this.t.getTangentX());
		assertEquals(1254, this.t.itx());
    }

	@DisplayName("getTangentX")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
    public void getTangentX(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(.0, this.t.getTangentX());
    }

	@DisplayName("itx")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
    public void itx(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEquals(0, this.t.itx());
    }

	@DisplayName("setTangentY(int)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
    public void setTangentYInt(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.t.setTangentY(1254);
		assertEpsilonEquals(1254, this.t.getTangentY());
		assertEquals(1254, this.t.ity());
    }

	@DisplayName("setTangentY(double)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
    public void setTangentYDouble(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.t.setTangentY(1254.26);
		assertEpsilonEquals(1254.26, this.t.getTangentY());
		assertEquals(1254, this.t.ity());
    }

	@DisplayName("getTangentY")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
    public void getTangentY(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(.0, this.t.getTangentY());
    }

	@DisplayName("ity")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
    public void ity(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEquals(0, this.t.ity());
    }

	@DisplayName("setTangentZ(int)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
    public void setTangentZInt(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.t.setTangentZ(1254);
		assertEpsilonEquals(1254, this.t.getTangentZ());
		assertEquals(1254, this.t.itz());
    }

	@DisplayName("setTangentZ(double)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
    public void setTangentZDouble(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.t.setTangentZ(1254.26);
		assertEpsilonEquals(1254.26, this.t.getTangentZ());
		assertEquals(1254, this.t.itz());
    }

	@DisplayName("getTangentZ")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
    public void getTangentZ(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(.0, this.t.getTangentZ());
    }

	@DisplayName("itz")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
    public void itz(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEquals(0, this.t.itz());
    }

	@DisplayName("setTangent(double,double,double)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
    public void setTangentDoubleDoubleDouble(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		double x = getRandom().nextDouble() * 20.;
		double y = getRandom().nextDouble() * 20.;
		double z = getRandom().nextDouble() * 20.;
		this.t.setTangent(x, y , z);
		assertEpsilonEquals(x, this.t.getTangentX());
		assertEpsilonEquals(y, this.t.getTangentY());
		assertEpsilonEquals(z, this.t.getTangentZ());
    }

	@DisplayName("setTangent(Vector3D)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
    public void setTangentVector3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		double x = getRandom().nextDouble() * 20.;
		double y = getRandom().nextDouble() * 20.;
		double z = getRandom().nextDouble() * 20.;
		this.t.setTangent(createVector(x, y, z));
		assertEpsilonEquals(x, this.t.getTangentX());
		assertEpsilonEquals(y, this.t.getTangentY());
		assertEpsilonEquals(z, this.t.getTangentZ());
    }

	@DisplayName("getTangent")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
    public void getTangent(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		double x = getRandom().nextDouble() * 20.;
		double y = getRandom().nextDouble() * 20.;
		double z = getRandom().nextDouble() * 20.;
		this.t.setTangentX(x);
		this.t.setTangentY(y);
		this.t.setTangentZ(z);
		assertEpsilonEquals(createVector(x, y, z), this.t.getTangent());
    }

	@DisplayName("setNormalX(int)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
    public void setNormalXInt(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.t.setNormalX(1254);
		assertEpsilonEquals(1254, this.t.getNormalX());
		assertEquals(1254, this.t.inx());
    }

	@DisplayName("setNormalX(double)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
    public void setNormalXDouble(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.t.setNormalX(1254.26);
		assertEpsilonEquals(1254.26, this.t.getNormalX());
		assertEquals(1254, this.t.inx());
    }

	@DisplayName("getNormalX")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
    public void getNormalX(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(.0, this.t.getNormalX());
    }

	@DisplayName("inx")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
    public void inx(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEquals(0, this.t.inx());
    }

	@DisplayName("setNormalY(int)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
    public void setNormalYInt(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.t.setNormalY(1254);
		assertEpsilonEquals(1254, this.t.getNormalY());
		assertEquals(1254, this.t.iny());
    }

	@DisplayName("setNormalY(double)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
    public void setNormalYDouble(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.t.setNormalY(1254.26);
		assertEpsilonEquals(1254.26, this.t.getNormalY());
		assertEquals(1254, this.t.iny());
    }

	@DisplayName("getNormalY")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
    public void getNormalY(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(.0, this.t.getNormalY());
    }

	@DisplayName("iny")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
    public void iny(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEquals(0, this.t.iny());
    }

	@DisplayName("setNormalZ(int)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
    public void setNormalZInt(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.t.setNormalZ(1254);
		assertEpsilonEquals(1254, this.t.getNormalZ());
		assertEquals(1254, this.t.inz());
    }

	@DisplayName("setNormalZ(double)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
    public void setNormalZDouble(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		this.t.setNormalZ(1254.26);
		assertEpsilonEquals(1254.26, this.t.getNormalZ());
		assertEquals(1254, this.t.inz());
    }

	@DisplayName("getNormalZ")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
    public void getNormalZ(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(.0, this.t.getNormalZ());
    }

	@DisplayName("inz")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
    public void inz(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEquals(0, this.t.inz());
    }

	@DisplayName("setNormal(double,double,double)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
    public void setNormalDoubleDoubleDouble(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		double x = getRandom().nextDouble() * 20.;
		double y = getRandom().nextDouble() * 20.;
		double z = getRandom().nextDouble() * 20.;
		this.t.setNormal(x, y , z);
		assertEpsilonEquals(x, this.t.getNormalX());
		assertEpsilonEquals(y, this.t.getNormalY());
		assertEpsilonEquals(z, this.t.getNormalZ());
    }

	@DisplayName("setNormal(Vector3D)")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
    public void setNormalVector3D(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		double x = getRandom().nextDouble() * 20.;
		double y = getRandom().nextDouble() * 20.;
		double z = getRandom().nextDouble() * 20.;
		this.t.setNormal(createVector(x, y, z));
		assertEpsilonEquals(x, this.t.getNormalX());
		assertEpsilonEquals(y, this.t.getNormalY());
		assertEpsilonEquals(z, this.t.getNormalZ());
    }

	@DisplayName("getNormal")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
    public void getNormal(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		double x = getRandom().nextDouble() * 20.;
		double y = getRandom().nextDouble() * 20.;
		double z = getRandom().nextDouble() * 20.;
		this.t.setNormalX(x);
		this.t.setNormalY(y);
		this.t.setNormalZ(z);
		assertEpsilonEquals(createVector(x, y, z), this.t.getNormal());
    }

	@DisplayName("getSwayX")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
    public void getSwayX(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(.0, this.t.getSwayX());
    }

	@DisplayName("isx")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
    public void isx(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEquals(0, this.t.isx());
    }

	@DisplayName("getSwayY")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
    public void getSwayY(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(.0, this.t.getSwayY());
    }

	@DisplayName("isy")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
    public void isy(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEquals(0, this.t.isy());
    }

	@DisplayName("getSwayZ")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
    public void getSwayZ(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEpsilonEquals(.0, this.t.getSwayZ());
    }

	@DisplayName("isz")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
    public void isz(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);
		assertEquals(0, this.t.isz());
    }

	@DisplayName("getSway")
	@ParameterizedTest(name = "{index} => {0}")
	@EnumSource(CoordinateSystem3D.class)
    public void getSway(CoordinateSystem3D cs) {
		CoordinateSystem3D.setDefaultCoordinateSystem(cs);

		double nx = getRandom().nextDouble() * 20.;
		double ny = getRandom().nextDouble() * 20.;
		double nz = getRandom().nextDouble() * 20.;
		Vector3d n = createVector(nx, ny, nz);
		this.t.setNormal(n);

		double tx = getRandom().nextDouble() * 20.;
		double ty = getRandom().nextDouble() * 20.;
		double tz = getRandom().nextDouble() * 20.;
		Vector3d t = createVector(tx, ty, tz);
		this.t.setTangent(t);

		Vector3d s = t.cross(n);

		assertEpsilonEquals(s, this.t.getSway());
    }

}
