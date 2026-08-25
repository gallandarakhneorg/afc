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

package org.arakhne.afc.io.shape;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import org.arakhne.afc.io.shape.ESRIBounds;
import org.arakhne.afc.io.shape.ESRIPoint;
import org.arakhne.afc.math.geometry.d2.d.Rectangle2d;

/**
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
@DisplayName("ESRIBounds")
@SuppressWarnings("all")
public class ESRIBoundsTest extends AbstractIoShapeTestCase {

	private double minx, miny, minz, minm;
	private double maxx, maxy, maxz, maxm;
	private ESRIBounds bounds;
	
	@BeforeEach
	public void setUp() throws Exception {
		minx = -getRandom().nextDouble();
		miny = -getRandom().nextDouble();
		minz = -getRandom().nextDouble();
		maxx = getRandom().nextDouble();
		maxy = getRandom().nextDouble();
		maxz = getRandom().nextDouble();
		bounds = new ESRIBounds(
				minx, maxx, 
				miny, maxy, 
				minz, maxz,
				minm, maxm);
	}

	@AfterEach
	public void tearDown() throws Exception {
		bounds = null;
	}

	@DisplayName("createUnion")
	@Nested
	public class createUnion {

		private double minx2;
		private double miny2;
		private double minz2;
		private double minm2;
		private double maxx2;
		private double maxy2;
		private double maxz2;
		private double maxm2;

		private double x;
		private double X;
		private double y;
		private double Y;
		private double z;
		private double Z;
		private double m;
		private double M;

		private ESRIBounds union;

		@BeforeEach
		public void setUp() {
			minx2 = -getRandom().nextDouble();
			miny2 = -getRandom().nextDouble();
			minz2 = -getRandom().nextDouble();
			minm2 = -getRandom().nextDouble();
			maxx2 = getRandom().nextDouble();
			maxy2 = getRandom().nextDouble();
			maxz2 = getRandom().nextDouble();
			maxm2 = getRandom().nextDouble();

			x = Math.min(minx, minx2);
			X = Math.max(maxx, maxx2);
			y = Math.min(miny, miny2);
			Y = Math.max(maxy, maxy2);
			z = Math.min(minz, minz2);
			Z = Math.max(maxz, maxz2);
			m = Math.min(minm, minm2);
			M = Math.max(maxm, maxm2);

			union = bounds.createUnion(new ESRIBounds(x, X, y, Y, z, Z, m, M));
		}

		@DisplayName("union not null")
		@Test
		public void testCreateUnion_notNull() {
			assertNotNull(union);
		}

		@DisplayName("minX")
		@Test
		public void testCreateUnion_minX() {
			assertEpsilonEquals(x, union.getMinX());
		}

		@DisplayName("maxX")
		@Test
		public void testCreateUnion_maxX() {
			assertEpsilonEquals(X, union.getMaxX());
		}

		@DisplayName("minY")
		@Test
		public void testCreateUnion_minY() {
			assertEpsilonEquals(y, union.getMinY());
		}

		@DisplayName("maxY")
		@Test
		public void testCreateUnion_maxY() {
			assertEpsilonEquals(Y, union.getMaxY());
		}

		@DisplayName("minZ")
		@Test
		public void testCreateUnion_minZ() {
			assertEpsilonEquals(z, union.getMinZ());
		}

		@DisplayName("maxZ")
		@Test
		public void testCreateUnion_maxZ() {
			assertEpsilonEquals(Z, union.getMaxZ());
		}

		@DisplayName("minM")
		@Test
		public void testCreateUnion_minM() {
			assertEpsilonEquals(m, union.getMinM());
		}

		@DisplayName("maxM")
		@Test
		public void testCreateUnion_maxM() {
			assertEpsilonEquals(M, union.getMaxM());
		}
	}

	@DisplayName("this + bounds")
	@Nested
	public class OperatorPlusESRIBounds {

		private double minx2;
		private double miny2;
		private double minz2;
		private double minm2;
		private double maxx2;
		private double maxy2;
		private double maxz2;
		private double maxm2;

		private double x;
		private double X;
		private double y;
		private double Y;
		private double z;
		private double Z;
		private double m;
		private double M;

		private ESRIBounds union;

		@BeforeEach
		public void setUp() {
			minx2 = -getRandom().nextDouble();
			miny2 = -getRandom().nextDouble();
			minz2 = -getRandom().nextDouble();
			minm2 = -getRandom().nextDouble();
			maxx2 = getRandom().nextDouble();
			maxy2 = getRandom().nextDouble();
			maxz2 = getRandom().nextDouble();
			maxm2 = getRandom().nextDouble();

			x = Math.min(minx, minx2);
			X = Math.max(maxx, maxx2);
			y = Math.min(miny, miny2);
			Y = Math.max(maxy, maxy2);
			z = Math.min(minz, minz2);
			Z = Math.max(maxz, maxz2);
			m = Math.min(minm, minm2);
			M = Math.max(maxm, maxm2);

			union = bounds.operator_plus(new ESRIBounds(x, X, y, Y, z, Z, m, M));
		}

		@DisplayName("union not null")
		@Test
		public void testCreateUnion_notNull() {
			assertNotNull(union);
		}

		@DisplayName("minX")
		@Test
		public void testCreateUnion_minX() {
			assertEpsilonEquals(x, union.getMinX());
		}

		@DisplayName("maxX")
		@Test
		public void testCreateUnion_maxX() {
			assertEpsilonEquals(X, union.getMaxX());
		}

		@DisplayName("minY")
		@Test
		public void testCreateUnion_minY() {
			assertEpsilonEquals(y, union.getMinY());
		}

		@DisplayName("maxY")
		@Test
		public void testCreateUnion_maxY() {
			assertEpsilonEquals(Y, union.getMaxY());
		}

		@DisplayName("minZ")
		@Test
		public void testCreateUnion_minZ() {
			assertEpsilonEquals(z, union.getMinZ());
		}

		@DisplayName("maxZ")
		@Test
		public void testCreateUnion_maxZ() {
			assertEpsilonEquals(Z, union.getMaxZ());
		}

		@DisplayName("minM")
		@Test
		public void testCreateUnion_minM() {
			assertEpsilonEquals(m, union.getMinM());
		}

		@DisplayName("maxM")
		@Test
		public void testCreateUnion_maxM() {
			assertEpsilonEquals(M, union.getMaxM());
		}
	}

	@DisplayName("this += ESRIPoint")
	@Nested
	public class OperatorAddESRIPoint {

		private double px;
		private double py;
		private double pz;
		private double pm;

		private double x;
		private double X;
		private double y;
		private double Y;
		private double z;
		private double Z;
		private double m;
		private double M;

		@BeforeEach
		public void setUp() {
			px = getRandom().nextDouble();
			py = getRandom().nextDouble();
			pz = -getRandom().nextDouble();
			pm = getRandom().nextDouble();

			x = Math.min(minx, px);
			X = Math.max(maxx, px);
			y = Math.min(miny, py);
			Y = Math.max(maxy, py);
			z = Math.min(minz, pz);
			Z = Math.max(maxz, pz);
			m = Math.min(minm, pm);
			M = Math.max(maxm, pm);

			bounds.operator_add(new ESRIPoint(px, py, pz, pm));
		}

		@DisplayName("minX")
		@Test
		public void testAdd_minX() {
			assertEpsilonEquals(x, bounds.getMinX());
		}

		@DisplayName("maxX")
		@Test
		public void testAdd_maxX() {
			assertEpsilonEquals(X, bounds.getMaxX());
		}

		@DisplayName("minY")
		@Test
		public void testAdd_minY() {
			assertEpsilonEquals(y, bounds.getMinY());
		}

		@DisplayName("maxY")
		@Test
		public void testAdd_maxY() {
			assertEpsilonEquals(Y, bounds.getMaxY());
		}

		@DisplayName("minZ")
		@Test
		public void testAdd_minZ() {
			assertEpsilonEquals(z, bounds.getMinZ());
		}

		@DisplayName("maxZ")
		@Test
		public void testAdd_maxZ() {
			assertEpsilonEquals(Z, bounds.getMaxZ());
		}

		@DisplayName("minM")
		@Test
		public void testAdd_minM() {
			assertEpsilonEquals(m, bounds.getMinM());
		}

		@DisplayName("maxM")
		@Test
		public void testAdd_maxM() {
			assertEpsilonEquals(M, bounds.getMaxM());
		}
	}

	@DisplayName("add")
	@Nested
	public class add {

		private double px;
		private double py;
		private double pz;
		private double pm;

		private double x;
		private double X;
		private double y;
		private double Y;
		private double z;
		private double Z;
		private double m;
		private double M;

		@BeforeEach
		public void setUp() {
			px = getRandom().nextDouble();
			py = getRandom().nextDouble();
			pz = -getRandom().nextDouble();
			pm = getRandom().nextDouble();

			x = Math.min(minx, px);
			X = Math.max(maxx, px);
			y = Math.min(miny, py);
			Y = Math.max(maxy, py);
			z = Math.min(minz, pz);
			Z = Math.max(maxz, pz);
			m = Math.min(minm, pm);
			M = Math.max(maxm, pm);

			bounds.add(new ESRIPoint(px, py, pz, pm));
		}

		@DisplayName("minX")
		@Test
		public void testAdd_minX() {
			assertEpsilonEquals(x, bounds.getMinX());
		}

		@DisplayName("maxX")
		@Test
		public void testAdd_maxX() {
			assertEpsilonEquals(X, bounds.getMaxX());
		}

		@DisplayName("minY")
		@Test
		public void testAdd_minY() {
			assertEpsilonEquals(y, bounds.getMinY());
		}

		@DisplayName("maxY")
		@Test
		public void testAdd_maxY() {
			assertEpsilonEquals(Y, bounds.getMaxY());
		}

		@DisplayName("minZ")
		@Test
		public void testAdd_minZ() {
			assertEpsilonEquals(z, bounds.getMinZ());
		}

		@DisplayName("maxZ")
		@Test
		public void testAdd_maxZ() {
			assertEpsilonEquals(Z, bounds.getMaxZ());
		}

		@DisplayName("minM")
		@Test
		public void testAdd_minM() {
			assertEpsilonEquals(m, bounds.getMinM());
		}

		@DisplayName("maxM")
		@Test
		public void testAdd_maxM() {
			assertEpsilonEquals(M, bounds.getMaxM());
		}
	}

	@DisplayName("equals(Object)")
	@Nested
	public class EqualsTest {

		private ESRIBounds copiedBounds;
		private ESRIBounds emptyBounds;
		private ESRIBounds sameValuesBounds;

		@BeforeEach
		public void setUp() {
			copiedBounds = new ESRIBounds(bounds);
			emptyBounds = new ESRIBounds();
			sameValuesBounds = new ESRIBounds(
					minx, maxx,
					miny, maxy,
					minz, maxz,
					minm, maxm);
		}

		@DisplayName("equals null")
		@Test
		public void testEqualsObject_equalsNull() {
			assertFalse(bounds.equals(null));
		}

		@DisplayName("equals different type")
		@Test
		public void testEqualsObject_equalsDifferentType() {
			assertFalse(bounds.equals(new Object()));
		}

		@DisplayName("equals itself")
		@Test
		public void testEqualsObject_equalsItself() {
			assertTrue(bounds.equals(bounds));
		}

		@DisplayName("equals copied bounds")
		@Test
		public void testEqualsObject_equalsCopiedBounds() {
			assertTrue(bounds.equals(copiedBounds));
		}

		@DisplayName("not equals empty bounds")
		@Test
		public void testEqualsObject_notEqualsEmptyBounds() {
			assertFalse(bounds.equals(emptyBounds));
		}

		@DisplayName("equals same values bounds")
		@Test
		public void testEqualsObject_equalsSameValuesBounds() {
			assertTrue(bounds.equals(sameValuesBounds));
		}
	}

	@DisplayName("compareTo")
	@Nested
	public class CompareTo {

		private ESRIBounds copiedBounds;
		private ESRIBounds emptyBounds;
		private ESRIBounds sameValuesBounds;
		private ESRIBounds minxMinus1Bounds;
		private ESRIBounds minxPlus1Bounds;
		private ESRIBounds minxMinus1MinyMinus1Bounds;
		private ESRIBounds minxPlus1MinyMinus1Bounds;

		@BeforeEach
		public void setUp() {
			copiedBounds = new ESRIBounds(bounds);
			emptyBounds = new ESRIBounds();
			sameValuesBounds = new ESRIBounds(
					minx, maxx,
					miny, maxy,
					minz, maxz,
					minm, maxm);
			minxMinus1Bounds = new ESRIBounds(
					minx - 1, maxx,
					miny, maxy,
					minz, maxz,
					minm, maxm);
			minxPlus1Bounds = new ESRIBounds(
					minx + 1, maxx,
					miny, maxy,
					minz, maxz,
					minm, maxm);
			minxMinus1MinyMinus1Bounds = new ESRIBounds(
					minx - 1, maxx,
					miny - 1, maxy,
					minz, maxz,
					minm, maxm);
			minxPlus1MinyMinus1Bounds = new ESRIBounds(
					minx + 1, maxx,
					miny - 1, maxy,
					minz, maxz,
					minm, maxm);
		}

		@DisplayName("compareTo null")
		@Test
		public void testCompareTo_compareToNull() {
			assertEquals(-1, bounds.compareTo(null));
		}

		@DisplayName("compareTo itself")
		@Test
		public void testCompareTo_compareToItself() {
			assertEquals(0, bounds.compareTo(bounds));
		}

		@DisplayName("compareTo copied bounds")
		@Test
		public void testCompareTo_compareToCopiedBounds() {
			assertEquals(0, bounds.compareTo(copiedBounds));
		}

		@DisplayName("compareTo empty bounds")
		@Test
		public void testCompareTo_compareToEmptyBounds() {
			assertEquals(1, bounds.compareTo(emptyBounds));
		}

		@DisplayName("compareTo same values bounds")
		@Test
		public void testCompareTo_compareToSameValuesBounds() {
			assertEquals(0, bounds.compareTo(sameValuesBounds));
		}

		@DisplayName("compareTo minx-1")
		@Test
		public void testCompareTo_compareToMinxMinus1() {
			assertEquals(-1, bounds.compareTo(minxMinus1Bounds));
		}

		@DisplayName("compareTo minx+1")
		@Test
		public void testCompareTo_compareToMinxPlus1() {
			assertEquals(1, bounds.compareTo(minxPlus1Bounds));
		}

		@DisplayName("compareTo minx-1 miny-1")
		@Test
		public void testCompareTo_compareToMinxMinus1MinyMinus1() {
			assertEquals(-1, bounds.compareTo(minxMinus1MinyMinus1Bounds));
		}

		@DisplayName("compareTo minx+1 miny-1")
		@Test
		public void testCompareTo_compareToMinxPlus1MinyMinus1() {
			assertEquals(1, bounds.compareTo(minxPlus1MinyMinus1Bounds));
		}
	}

	@DisplayName("this <)=> bounds")
	@Nested
	public class OperatorSpaceshipESRIBounds {

		private ESRIBounds copiedBounds;
		private ESRIBounds emptyBounds;
		private ESRIBounds sameValuesBounds;
		private ESRIBounds minxMinus1Bounds;
		private ESRIBounds minxPlus1Bounds;
		private ESRIBounds minxMinus1MinyMinus1Bounds;
		private ESRIBounds minxPlus1MinyMinus1Bounds;

		@BeforeEach
		public void setUp() {
			copiedBounds = new ESRIBounds(bounds);
			emptyBounds = new ESRIBounds();
			sameValuesBounds = new ESRIBounds(
					minx, maxx,
					miny, maxy,
					minz, maxz,
					minm, maxm);
			minxMinus1Bounds = new ESRIBounds(
					minx - 1, maxx,
					miny, maxy,
					minz, maxz,
					minm, maxm);
			minxPlus1Bounds = new ESRIBounds(
					minx + 1, maxx,
					miny, maxy,
					minz, maxz,
					minm, maxm);
			minxMinus1MinyMinus1Bounds = new ESRIBounds(
					minx - 1, maxx,
					miny - 1, maxy,
					minz, maxz,
					minm, maxm);
			minxPlus1MinyMinus1Bounds = new ESRIBounds(
					minx + 1, maxx,
					miny - 1, maxy,
					minz, maxz,
					minm, maxm);
		}

		@DisplayName("compareTo null")
		@Test
		public void testCompareTo_compareToNull() {
			assertEquals(-1, bounds.operator_spaceship(null));
		}

		@DisplayName("compareTo itself")
		@Test
		public void testCompareTo_compareToItself() {
			assertEquals(0, bounds.operator_spaceship(bounds));
		}

		@DisplayName("compareTo copied bounds")
		@Test
		public void testCompareTo_compareToCopiedBounds() {
			assertEquals(0, bounds.operator_spaceship(copiedBounds));
		}

		@DisplayName("compareTo empty bounds")
		@Test
		public void testCompareTo_compareToEmptyBounds() {
			assertEquals(1, bounds.operator_spaceship(emptyBounds));
		}

		@DisplayName("compareTo same values bounds")
		@Test
		public void testCompareTo_compareToSameValuesBounds() {
			assertEquals(0, bounds.operator_spaceship(sameValuesBounds));
		}

		@DisplayName("compareTo minx-1")
		@Test
		public void testCompareTo_compareToMinxMinus1() {
			assertEquals(-1, bounds.operator_spaceship(minxMinus1Bounds));
		}

		@DisplayName("compareTo minx+1")
		@Test
		public void testCompareTo_compareToMinxPlus1() {
			assertEquals(1, bounds.operator_spaceship(minxPlus1Bounds));
		}

		@DisplayName("compareTo minx-1 miny-1")
		@Test
		public void testCompareTo_compareToMinxMinus1MinyMinus1() {
			assertEquals(-1, bounds.operator_spaceship(minxMinus1MinyMinus1Bounds));
		}

		@DisplayName("compareTo minx+1 miny-1")
		@Test
		public void testCompareTo_compareToMinxPlus1MinyMinus1() {
			assertEquals(1, bounds.operator_spaceship(minxPlus1MinyMinus1Bounds));
		}
	}

	@DisplayName("getMinX")
	@Nested
	public class GetMinX {

		@Test
		public void testGetMinX() {
			assertEpsilonEquals(minx, bounds.getMinX());
		}
	}

	@DisplayName("getCenterX")
	@Nested
	public class GetCenterX {

		@Test
		public void testGetCenterX() {
			assertEpsilonEquals((minx+maxx)/2., bounds.getCenterX());
		}
	}

	@DisplayName("getMaxX")
	@Nested
	public class GetMaxX {

		@Test
		public void testGetMaxX() {
			assertEpsilonEquals(maxx, bounds.getMaxX());
		}
	}

	@DisplayName("getCenterY")
	@Nested
	public class GetCenterY {

		@Test
		public void testGetCenterY() {
			assertEpsilonEquals((miny+maxy)/2., bounds.getCenterY());
		}
	}

	@DisplayName("getMinY")
	@Nested
	public class GetMinY {

		@Test
		public void testGetMinY() {
			assertEpsilonEquals(miny, bounds.getMinY());
		}
	}

	@DisplayName("getMaxY")
	@Nested
	public class GetMaxY {

		@Test
		public void testGetMaxY() {
			assertEpsilonEquals(maxy, bounds.getMaxY());
		}
	}

	@DisplayName("getMinZ")
	@Nested
	public class GetMinZ {

		@Test
		public void testGetMinZ() {
			assertEpsilonEquals(minz, bounds.getMinZ());
		}
	}

	@DisplayName("getCenterZ")
	@Nested
	public class GetCenterZ {

		@Test
		public void testGetCenterZ() {
			assertEpsilonEquals((minz+maxz)/2., bounds.getCenterZ());
		}
	}

	@DisplayName("getMaxZ")
	@Nested
	public class GetMaxZ {

		@Test
		public void testGetMaxZ() {
			assertEpsilonEquals(maxz, bounds.getMaxZ());
		}
	}

	@DisplayName("getMinM")
	@Nested
	public class GetMinM {

		@Test
		public void testGetMinM() {
			assertEpsilonEquals(minm, bounds.getMinM());
		}
	}

	@DisplayName("getCenterM")
	@Nested
	public class GetCenterM {

		@Test
		public void testGetCenterM() {
			assertEpsilonEquals((minm+maxm)/2., bounds.getCenterM());
		}
	}

	@DisplayName("getMaxM")
	@Nested
	public class GetMaxM {

		@Test
		public void testGetMaxM() {
			assertEpsilonEquals(maxm, bounds.getMaxM());
		}
	}

	@DisplayName("toRectangle2d")
	@Nested
	public class ToRectangle2d {

		private Rectangle2d expected;
		private Rectangle2d actual;

		@BeforeEach
		public void setUp() {
			expected = new Rectangle2d(
					minx, miny,
					maxx - minx,
					maxy - miny);
			actual = bounds.toRectangle2d();
		}

		@DisplayName("minX")
		@Test
		public void testToRectangle2D_minX() {
			assertEpsilonEquals(expected.getMinX(), actual.getMinX());
		}

		@DisplayName("minY")
		@Test
		public void testToRectangle2D_minY() {
			assertEpsilonEquals(expected.getMinY(), actual.getMinY());
		}

		@DisplayName("maxX")
		@Test
		public void testToRectangle2D_maxX() {
			assertEpsilonEquals(expected.getMaxX(), actual.getMaxX());
		}

		@DisplayName("maxY")
		@Test
		public void testToRectangle2D_maxY() {
			assertEpsilonEquals(expected.getMaxY(), actual.getMaxY());
		}
	}

	@DisplayName("ensureMinMax")
	@Nested
	public class EnsureMinMax {

		private ESRIBounds b;

		@BeforeEach
		public void setUp() {
			b = new ESRIBounds(
					maxx, minx,
					miny, maxy,
					maxz, minz,
					minm, maxm);
			b.ensureMinMax();
		}

		@DisplayName("minX")
		@Test
		public void testEnsureMinMax_minX() {
			assertEpsilonEquals(minx, b.getMinX());
		}

		@DisplayName("maxX")
		@Test
		public void testEnsureMinMax_maxX() {
			assertEpsilonEquals(maxx, b.getMaxX());
		}

		@DisplayName("minY")
		@Test
		public void testEnsureMinMax_minY() {
			assertEpsilonEquals(miny, b.getMinY());
		}

		@DisplayName("maxY")
		@Test
		public void testEnsureMinMax_maxY() {
			assertEpsilonEquals(maxy, b.getMaxY());
		}

		@DisplayName("minZ")
		@Test
		public void testEnsureMinMax_minZ() {
			assertEpsilonEquals(minz, b.getMinZ());
		}

		@DisplayName("maxZ")
		@Test
		public void testEnsureMinMax_maxZ() {
			assertEpsilonEquals(maxz, b.getMaxZ());
		}

		@DisplayName("minM")
		@Test
		public void testEnsureMinMax_minM() {
			assertEpsilonEquals(minm, b.getMinM());
		}

		@DisplayName("maxM")
		@Test
		public void testEnsureMinMax_maxM() {
			assertEpsilonEquals(maxm, b.getMaxM());
		}
	}
}
