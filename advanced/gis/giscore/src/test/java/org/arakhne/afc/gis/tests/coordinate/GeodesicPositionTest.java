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

package org.arakhne.afc.gis.tests.coordinate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.arakhne.afc.gis.coordinate.GeodesicPosition;
import org.arakhne.afc.gis.coordinate.GeodesicPositionStringRepresentation;
import org.arakhne.afc.gis.coordinate.SexagesimalLatitudeAxis;
import org.arakhne.afc.gis.coordinate.SexagesimalLongitudeAxis;
import org.arakhne.afc.gis.tests.AbstractGisTestCase;
import org.arakhne.afc.math.geometry.base.d2.Point2D;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
@DisplayName("GeodesicPosition")
@SuppressWarnings("all")
public class GeodesicPositionTest extends AbstractGisTestCase {

	@DisplayName("Sexagesimal to angular")
	@Nested
	public class SexagesimalToAngular {

		private GeodesicPosition pos;

		@BeforeEach
		public void setUp() {
			pos = new GeodesicPosition(
					121, 8, 6, SexagesimalLatitudeAxis.NORTH,
					45, 53, 36, SexagesimalLongitudeAxis.WEST);
		}

		@DisplayName("#1")
		@Test
		public void testSexagesimalToAngular_assertion1() {
			assertEpsilonEquals(121.135, pos.getLatitude());
		}

		@DisplayName("#2")
		@Test
		public void testSexagesimalToAngular_assertion2() {
			assertEpsilonEquals(-45.893333, pos.getLongitude());
		}
	}

	@DisplayName("Angular to sexagesimal")
	@Nested
	public class AngularToSexagesimal {

		private GeodesicPosition pos;

		@DisplayName("#1")
		@Test
		public void testAngularToSexagesimal_assertion1() {
			pos = new GeodesicPosition(121.135,-34.5678989);
			assertEquals(121, pos.getLongitudeDegree());
		}

		@DisplayName("#2")
		@Test
		public void testAngularToSexagesimal_assertion2() {
			pos = new GeodesicPosition(121.135,-34.5678989);
			assertEquals(8, pos.getLongitudeMinute());
		}

		@DisplayName("#3")
		@Test
		public void testAngularToSexagesimal_assertion3() {
			pos = new GeodesicPosition(121.135,-34.5678989);
			assertEpsilonEquals(6., pos.getLongitudeSecond());
		}

		@DisplayName("#4")
		@Test
		public void testAngularToSexagesimal_assertion4() {
			pos = new GeodesicPosition(121.135,-34.5678989);
			assertEquals(SexagesimalLongitudeAxis.EAST, pos.getLongitudeAxis());
		}

		@DisplayName("#5")
		@Test
		public void testAngularToSexagesimal_assertion5() {
			pos = new GeodesicPosition(121.135,-34.5678989);
			assertEquals(34, pos.getLatitudeDegree());
		}

		@DisplayName("#6")
		@Test
		public void testAngularToSexagesimal_assertion6() {
			pos = new GeodesicPosition(121.135,-34.5678989);
			assertEquals(34, pos.getLatitudeMinute());
		}

		@DisplayName("#7")
		@Test
		public void testAngularToSexagesimal_assertion7() {
			pos = new GeodesicPosition(121.135,-34.5678989);
			assertEpsilonEquals(4.43604, pos.getLatitudeSecond());
		}

		@DisplayName("#8")
		@Test
		public void testAngularToSexagesimal_assertion8() {
			pos = new GeodesicPosition(121.135,-34.5678989);
			assertEquals(SexagesimalLatitudeAxis.SOUTH, pos.getLatitudeAxis());
		}

		@DisplayName("#9")
		@Test
		public void testAngularToSexagesimal_assertion9() {
			pos = new GeodesicPosition(-121.135,34.5678989);
			assertEquals(121, pos.getLongitudeDegree());
		}

		@DisplayName("#10")
		@Test
		public void testAngularToSexagesimal_assertion10() {
			pos = new GeodesicPosition(-121.135,34.5678989);
			assertEquals(8, pos.getLongitudeMinute());
		}

		@DisplayName("#11")
		@Test
		public void testAngularToSexagesimal_assertion11() {
			pos = new GeodesicPosition(-121.135,34.5678989);
			assertEpsilonEquals(6, pos.getLongitudeSecond());
		}

		@DisplayName("#12")
		@Test
		public void testAngularToSexagesimal_assertion12() {
			pos = new GeodesicPosition(-121.135,34.5678989);
			assertEquals(SexagesimalLongitudeAxis.WEST, pos.getLongitudeAxis());
		}

		@DisplayName("#13")
		@Test
		public void testAngularToSexagesimal_assertion13() {
			pos = new GeodesicPosition(-121.135,34.5678989);
			assertEquals(34, pos.getLatitudeDegree());
		}

		@DisplayName("#14")
		@Test
		public void testAngularToSexagesimal_assertion14() {
			pos = new GeodesicPosition(-121.135,34.5678989);
			assertEquals(34, pos.getLatitudeMinute());
		}

		@DisplayName("#15")
		@Test
		public void testAngularToSexagesimal_assertion15() {
			pos = new GeodesicPosition(-121.135,34.5678989);
			assertEpsilonEquals(4.43604, pos.getLatitudeSecond());
		}

		@DisplayName("#16")
		@Test
		public void testAngularToSexagesimal_assertion16() {
			pos = new GeodesicPosition(-121.135,34.5678989);
			assertEquals(SexagesimalLatitudeAxis.NORTH, pos.getLatitudeAxis());
		}
	}

    @AfterEach
    void resetPreferences() {
        GeodesicPosition.setPreferredStringRepresentation(null, GeodesicPosition.DEFAULT_SYMBOL_IN_STRING_REPRESENTATION);
    }

    @DisplayName("constructor(double,double)")
    @Nested
    public class ConstructorDoubleDouble {

        @DisplayName("(lambda,phi) stores values")
        @Test
        void constructor_storesValues() {
            GeodesicPosition p = new GeodesicPosition(2.35, 48.85);
            assertEquals(2.35, p.lambda, EPSILON);
            assertEquals(48.85, p.phi, EPSILON);
        }
    }

    @DisplayName("constructor(copy)")
    @Nested
    public class ConstructorCopy {

        @DisplayName("(nonnull source) copies coordinates")
        @Test
        void constructor_nonNullSource_copiesValues() {
            GeodesicPosition src = new GeodesicPosition(10.5, -20.25);
            GeodesicPosition p = new GeodesicPosition(src);
            assertEquals(10.5, p.lambda, EPSILON);
            assertEquals(-20.25, p.phi, EPSILON);
        }

        @DisplayName("(null source) sets NaN")
        @Test
        void constructor_nullSource_setsNaN() {
            GeodesicPosition p = new GeodesicPosition((GeodesicPosition) null);
            assertTrue(Double.isNaN(p.lambda));
            assertTrue(Double.isNaN(p.phi));
        }
    }

    @DisplayName("constructor(sexagesimal)")
    @Nested
    public class ConstructorSexagesimal {

        @DisplayName("(north,east) creates positive coordinates")
        @Test
        void constructor_northEast_positiveCoordinates() {
            GeodesicPosition p = new GeodesicPosition(
                    48, 30, 0., SexagesimalLatitudeAxis.NORTH,
                    2, 15, 0., SexagesimalLongitudeAxis.EAST);

            assertEquals(48.5, p.phi, EPSILON);
            assertEquals(2.25, p.lambda, EPSILON);
        }

        @DisplayName("(south,west) creates negative coordinates")
        @Test
        void constructor_southWest_negativeCoordinates() {
            GeodesicPosition p = new GeodesicPosition(
                    12, 0, 0., SexagesimalLatitudeAxis.SOUTH,
                    45, 0, 0., SexagesimalLongitudeAxis.WEST);

            assertEquals(-12.0, p.phi, EPSILON);
            assertEquals(-45.0, p.lambda, EPSILON);
        }
    }

    @DisplayName("latitude/longitude getters")
    @Nested
    public class CoordinatesGetters {

        @DisplayName("getLatitude/getLongitude")
        @Test
        void getLatitudeLongitude() {
            GeodesicPosition p = new GeodesicPosition(7.75, -43.125);
            assertEquals(-43.125, p.getLatitude(), EPSILON);
            assertEquals(7.75, p.getLongitude(), EPSILON);
        }
    }

    @DisplayName("sexagesimal decomposition")
    @Nested
    public class SexagesimalDecomposition {

        @DisplayName("latitude degree/minute/second")
        @Test
        void latitudeDms() {
            GeodesicPosition p = new GeodesicPosition(0., 12.5); // 12° 30' 0"
            assertEquals(12, p.getLatitudeDegree());
            assertEquals(30, p.getLatitudeMinute());
            assertEquals(0., p.getLatitudeSecond(), EPSILON);
        }

        @DisplayName("longitude degree/minute/second")
        @Test
        void longitudeDms() {
            GeodesicPosition p = new GeodesicPosition(-73.985656, 0.);
            assertEquals(73, p.getLongitudeDegree());
            assertEquals(59, p.getLongitudeMinute());
            assertTrue(p.getLongitudeSecond() >= 0.0 && p.getLongitudeSecond() < 60.0);
        }

        @DisplayName("latitude axis")
        @Test
        void latitudeAxis() {
            assertEquals(SexagesimalLatitudeAxis.NORTH, new GeodesicPosition(0., 5.).getLatitudeAxis());
            assertEquals(SexagesimalLatitudeAxis.SOUTH, new GeodesicPosition(0., -5.).getLatitudeAxis());
        }

        @DisplayName("longitude axis")
        @Test
        void longitudeAxis() {
            assertEquals(SexagesimalLongitudeAxis.EAST, new GeodesicPosition(5., 0.).getLongitudeAxis());
            assertEquals(SexagesimalLongitudeAxis.WEST, new GeodesicPosition(-5., 0.).getLongitudeAxis());
        }
    }

    @DisplayName("toPoint2d")
    @Nested
    public class ToPoint2d {

        @DisplayName("returns point with x=lambda and y=phi")
        @Test
        void toPoint2d_returnsExpectedCoordinates() {
            GeodesicPosition p = new GeodesicPosition(3.14, -2.72);
            Point2D point = p.toPoint2d();
            assertEquals(3.14, point.getX(), EPSILON);
            assertEquals(-2.72, point.getY(), EPSILON);
        }
    }

    @DisplayName("static string converters")
    @Nested
    public class StaticStringConverters {

        @DisplayName("toDMSString returns non-empty")
        @Test
        void toDmsString_nonEmpty() {
            String s = GeodesicPosition.toDMSString(2.35, 48.85, true);
            assertTrue(s != null && !s.isEmpty());
        }

        @DisplayName("toDecimalDegreeString returns non-empty")
        @Test
        void toDecimalDegreeString_nonEmpty() {
            String s = GeodesicPosition.toDecimalDegreeString(2.35, 48.85, false);
            assertTrue(s != null && !s.isEmpty());
        }

        @DisplayName("toDecimalDegreeMinuteString returns non-empty")
        @Test
        void toDecimalDegreeMinuteString_nonEmpty() {
            String s = GeodesicPosition.toDecimalDegreeMinuteString(2.35, 48.85, true);
            assertTrue(s != null && !s.isEmpty());
        }
    }

    @DisplayName("preferences")
    @Nested
    public class PreferencesApi {

        @DisplayName("default preferred representation")
        @Test
        void defaultPreferredRepresentation() {
            GeodesicPosition.setPreferredStringRepresentation(null, true);
            assertEquals(GeodesicPosition.DEFAULT_STRING_REPRESENTATION,
                    GeodesicPosition.getPreferredStringRepresentation());
        }

        @DisplayName("allow null preferred representation")
        @Test
        void allowNullPreferredRepresentation() {
            GeodesicPosition.setPreferredStringRepresentation(null, true);
            assertNull(GeodesicPosition.getPreferredStringRepresentation(true));
        }

        @DisplayName("set/get preferred representation")
        @Test
        void setGetPreferredRepresentation() {
            GeodesicPosition.setPreferredStringRepresentation(
                    GeodesicPositionStringRepresentation.DEGREE_MINUTE, false);

            assertEquals(GeodesicPositionStringRepresentation.DEGREE_MINUTE,
                    GeodesicPosition.getPreferredStringRepresentation());
            assertFalse(GeodesicPosition.getDirectionSymbolInPreferredStringRepresentation());
        }
    }

    @DisplayName("toString")
    @Nested
    public class ToStringApi {

        @DisplayName("uses preferred DEGREE format")
        @Test
        void toString_degree() {
            GeodesicPosition.setPreferredStringRepresentation(
                    GeodesicPositionStringRepresentation.DEGREE, true);
            String s = new GeodesicPosition(2.35, 48.85).toString();
            assertTrue(s != null && !s.isEmpty());
        }

        @DisplayName("uses preferred DEGREE_MINUTE format")
        @Test
        void toString_degreeMinute() {
            GeodesicPosition.setPreferredStringRepresentation(
                    GeodesicPositionStringRepresentation.DEGREE_MINUTE, true);
            String s = new GeodesicPosition(2.35, 48.85).toString();
            assertTrue(s != null && !s.isEmpty());
        }

        @DisplayName("uses preferred DEGREE_MINUTE_SECOND format")
        @Test
        void toString_degreeMinuteSecond() {
            GeodesicPosition.setPreferredStringRepresentation(
                    GeodesicPositionStringRepresentation.DEGREE_MINUTE_SECOND, true);
            String s = new GeodesicPosition(2.35, 48.85).toString();
            assertTrue(s != null && !s.isEmpty());
        }
    }

    @DisplayName("equals/hashCode")
    @Nested
    public class EqualsHashCode {

        @DisplayName("equals same coordinates")
        @Test
        void equals_sameCoordinates() {
            GeodesicPosition a = new GeodesicPosition(1.0, 2.0);
            GeodesicPosition b = new GeodesicPosition(1.0, 2.0);
            assertEquals(a, b);
            assertEquals(a.hashCode(), b.hashCode());
        }

        @DisplayName("not equals different coordinates")
        @Test
        void notEquals_differentCoordinates() {
            GeodesicPosition a = new GeodesicPosition(1.0, 2.0);
            GeodesicPosition b = new GeodesicPosition(1.0, 3.0);
            assertNotEquals(a, b);
        }

        @DisplayName("not equals null")
        @Test
        void notEquals_null() {
            GeodesicPosition a = new GeodesicPosition(1.0, 2.0);
            assertNotEquals(a, null);
        }

        @DisplayName("not equals other type")
        @Test
        void notEquals_otherType() {
            GeodesicPosition a = new GeodesicPosition(1.0, 2.0);
            assertNotEquals(a, "not-a-position");
        }
    }
}
