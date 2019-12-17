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

package org.arakhne.afc.math.geometry.d3.dfx;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import javafx.beans.property.ReadOnlyDoubleProperty;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import org.arakhne.afc.math.geometry.d3.Vector3D;
import org.arakhne.afc.math.test.AbstractMathTestCase;

@SuppressWarnings("all")
@Disabled("temporary")
public class UnitVectorProperty3dfxTest extends AbstractMathTestCase {

	private static final double ox = 123.456;
	private static final double oy = 951.753;
	private static final double oz = 142.873;
	private static final double ux = 0.12723;
	private static final double uy = 0.98088;
	private static final double uz = 0.14725;
	
	private UnitVectorProperty3dfx property;
	
	@BeforeEach
	public void setUp() {
		this.property = new UnitVectorProperty3dfx(this, "test", new GeomFactory3dfx()); //$NON-NLS-1$
		double length = Math.sqrt(ox * ox + oy * oy + oz * oz);
		this.property.set(ox / length, oy / length, oz / length);
	}
	
	@Test
	public void setDoubleDouble_notUnitVector() {
		assertThrows(AssertionError.class, () -> this.property.set(ox, oy, oz));
	}
	
	@Test
	@Disabled
	public void setDoubleDouble_unitVector() {
		this.property.set(0.031598, -0.999501, 0.417652);
		assertEpsilonEquals(0.031598, this.property.getX());
		assertEpsilonEquals(-0.999501, this.property.getY());
		assertEpsilonEquals(0.417652, this.property.getZ());
	}

	@Test
	public void setVector3fx_notUnitVector() {
		assertThrows(AssertionError.class, () -> this.property.set(new Vector3dfx(ox, oy, oz)));
	}
	
	@Test
	@Disabled
	public void setVector3fx_unitVector() {
		this.property.set(new Vector3dfx(0.031598, -0.999501, 0.417652));
		assertEpsilonEquals(0.031598, this.property.getX());
		assertEpsilonEquals(-0.999501, this.property.getY());
		assertEpsilonEquals(0.417652, this.property.getZ());
	}

	@Test
	public void setVector3D_onVector() {
		assertThrows(RuntimeException.class, () -> {
			Vector3D v = this.property.get();
			v.set(new Vector3dfx(0.031598, -0.999501, 0.417652));
		});
	}

	@Test
	public void setDoubleDouble_onVector() {
		assertThrows(RuntimeException.class, () -> {
			Vector3D v = this.property.get();
			v.set(0.031598, -0.999501, 0.417652);
		});
	}

	@Test
	@Disabled
	public void get() {
		Vector3D v = this.property.get();
		assertNotNull(v);
		assertEpsilonEquals(ux, v.getX());
		assertEpsilonEquals(uy, v.getY());
		assertEpsilonEquals(uz, v.getZ());
	}
	
	@Test
	public void getX() {
		assertEpsilonEquals(ux, this.property.getX());
	}
	
	@Test
	public void getY() {
	    assertEpsilonEquals(uy, this.property.getY());
	}

	@Test
	public void getZ() {
		assertEpsilonEquals(uz, this.property.getZ());
	}

	@Test
	public void xProperty() {
		ReadOnlyDoubleProperty x = this.property.xProperty();
		assertNotNull(x);
		assertEpsilonEquals(ux, x.get());
	}
	
	@Test
	public void yProperty() {
	    ReadOnlyDoubleProperty y = this.property.yProperty();
	    assertNotNull(y);
	    assertEpsilonEquals(uy, y.get());
	}

	@Test
	public void zProperty() {
		ReadOnlyDoubleProperty z = this.property.zProperty();
		assertNotNull(z);
		assertEpsilonEquals(uz, z.get());
	}

}
