/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2026 The original authors and other contributors.
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

package org.arakhne.afc.math.geometry.fx.tests.d2.d;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import javafx.beans.property.ReadOnlyDoubleProperty;
import org.arakhne.afc.math.geometry.base.d2.Vector2D;
import org.arakhne.afc.math.geometry.base.tests.AbstractMathTestCase;
import org.arakhne.afc.math.geometry.fx.d2.d.GeomFactory2dfx;
import org.arakhne.afc.math.geometry.fx.d2.d.UnitVectorProperty;
import org.arakhne.afc.math.geometry.fx.d2.d.Vector2dfx;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("UnitVectorProperty")
@SuppressWarnings("all")
public class UnitVectorPropertyTest extends AbstractMathTestCase {

	private static final double ox = 123.456;
	private static final double oy = 951.753;
	private static final double ux = 0.12864;
	private static final double uy = 0.99169;
	
	private UnitVectorProperty property;
	
	@BeforeEach
	public void setUp() {
		property = new UnitVectorProperty(this, "test", new GeomFactory2dfx()); //$NON-NLS-1$
		double length = Math.hypot(ox, oy);
		property.set(ox / length, oy / length);
	}
	
	@DisplayName("set")
	@Nested
	public class Set {

		@DisplayName("(double,double)")
		@Nested
		public class WithDoubleDouble {

			@DisplayName("Not unit vector")
			@Test
			public void setDoubleDouble_notUnitVector() {
				assertThrows(AssertionError.class, () -> property.set(ox, oy));
			}
	
			@DisplayName("Unit vector")
			@Test
			public void setDoubleDouble_unitVector() {
				property.set(0.031598, -0.999501);
				assertEpsilonEquals(0.031598, property.getX());
				assertEpsilonEquals(-0.999501, property.getY());
			}

			@DisplayName("Any vector")
			@Test
			public void setDoubleDouble_onVector() {
				assertThrows(RuntimeException.class, () -> {
					Vector2D v = property.get();
					v.set(0.031598, -0.999501);
				});
			}
		}

		@DisplayName("(Vector2D)")
		@Nested
		public class WithVector2D {

			@DisplayName("Not unit vector")
			@Test
			public void setVector2fx_notUnitVector() {
				assertThrows(AssertionError.class, () -> property.set(new Vector2dfx(ox, oy)));
			}

			@DisplayName("Unit vector")
			@Test
			public void setVector2fx_unitVector() {
				property.set(new Vector2dfx(0.031598, -0.999501));
				assertEpsilonEquals(0.031598, property.getX());
				assertEpsilonEquals(-0.999501, property.getY());
			}

			@DisplayName("Any vector")
			@Test
			public void setVector2D_onVector() {
				assertThrows(RuntimeException.class, () -> {
					Vector2D v = property.get();
					v.set(new Vector2dfx(0.031598, -0.999501));
				});
			}
		}
	}

	@DisplayName("get")
	@Nested
	public class Get {

		@DisplayName("#1")
		@Test
		public void get() {
			Vector2D v = property.get();
			assertNotNull(v);
			assertEpsilonEquals(ux, v.getX());
			assertEpsilonEquals(uy, v.getY());
		}
	}
	
	@DisplayName("getX")
	@Nested
	public class GetX {

		@DisplayName("#1")
		@Test
		public void getX() {
			assertEpsilonEquals(ux, property.getX());
		}
	}
	
	@DisplayName("getY")
	@Nested
	public class GetY {

		@DisplayName("#1")
		@Test
		public void getY() {
			assertEpsilonEquals(uy, property.getY());
		}
	
	}
	
	@DisplayName("xProperty")
	@Nested
	public class XProperty {

		@DisplayName("#1")
		@Test
		public void xProperty() {
			ReadOnlyDoubleProperty x = property.xProperty();
			assertNotNull(x);
			assertEpsilonEquals(ux, x.get());
		}
	}
	
	@DisplayName("yProperty")
	@Nested
	public class YProperty {

		@DisplayName("#1")
		@Test
		public void yProperty() {
			ReadOnlyDoubleProperty y = property.yProperty();
			assertNotNull(y);
			assertEpsilonEquals(uy, y.get());
		}
	}

}
