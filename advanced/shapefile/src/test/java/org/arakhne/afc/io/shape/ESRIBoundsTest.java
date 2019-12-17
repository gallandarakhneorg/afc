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

package org.arakhne.afc.io.shape;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.arakhne.afc.math.geometry.d2.d.Rectangle2d;

/**
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
@SuppressWarnings("all")
public class ESRIBoundsTest extends AbstractIoShapeTest {

	private double minx, miny, minz, minm;
	private double maxx, maxy, maxz, maxm;
	private ESRIBounds bounds;
	
	@BeforeEach
	public void setUp() throws Exception {
		this.minx = -getRandom().nextDouble();
		this.miny = -getRandom().nextDouble();
		this.minz = -getRandom().nextDouble();
		this.maxx = getRandom().nextDouble();
		this.maxy = getRandom().nextDouble();
		this.maxz = getRandom().nextDouble();
		this.bounds = new ESRIBounds(
				this.minx, this.maxx, 
				this.miny, this.maxy, 
				this.minz, this.maxz,
				this.minm, this.maxm);
	}

	@AfterEach
	public void tearDown() throws Exception {
		this.bounds = null;
	}

	@Test
	public void testCreateUnion() {
		double minx2 = -getRandom().nextDouble();
		double miny2 = -getRandom().nextDouble();
		double minz2 = -getRandom().nextDouble();
		double minm2 = -getRandom().nextDouble();
		double maxx2 = getRandom().nextDouble();
		double maxy2 = getRandom().nextDouble();
		double maxz2 = getRandom().nextDouble();
		double maxm2 = getRandom().nextDouble();
		
		double x = Math.min(this.minx, minx2);
		double X = Math.max(this.maxx, maxx2);
		double y = Math.min(this.miny, miny2);
		double Y = Math.max(this.maxy, maxy2);
		double z = Math.min(this.minz, minz2);
		double Z = Math.max(this.maxz, maxz2);
		double m = Math.min(this.minm, minm2);
		double M = Math.max(this.maxm, maxm2);
		
		ESRIBounds union = this.bounds.createUnion(new ESRIBounds(x, X, y, Y, z, Z, m, M));
		
		assertNotNull(union);
		assertEpsilonEquals(x, union.getMinX());
		assertEpsilonEquals(X, union.getMaxX());
		assertEpsilonEquals(y, union.getMinY());
		assertEpsilonEquals(Y, union.getMaxY());
		assertEpsilonEquals(z, union.getMinZ());
		assertEpsilonEquals(Z, union.getMaxZ());
		assertEpsilonEquals(m, union.getMinM());
		assertEpsilonEquals(M, union.getMaxM());
	}

	@Test
	public void testAdd() {
		double px = getRandom().nextDouble();
		double py = getRandom().nextDouble();
		double pz = -getRandom().nextDouble();
		double pm = getRandom().nextDouble();
		
		double x = Math.min(this.minx, px);
		double X = Math.max(this.maxx, px);
		double y = Math.min(this.miny, py);
		double Y = Math.max(this.maxy, py);
		double z = Math.min(this.minz, pz);
		double Z = Math.max(this.maxz, pz);
		double m = Math.min(this.minm, pm);
		double M = Math.max(this.maxm, pm);
		
		this.bounds.add(new ESRIPoint(px, py, pz, pm));
		
		assertEpsilonEquals(x, this.bounds.getMinX());
		assertEpsilonEquals(X, this.bounds.getMaxX());
		assertEpsilonEquals(y, this.bounds.getMinY());
		assertEpsilonEquals(Y, this.bounds.getMaxY());
		assertEpsilonEquals(z, this.bounds.getMinZ());
		assertEpsilonEquals(Z, this.bounds.getMaxZ());
		assertEpsilonEquals(m, this.bounds.getMinM());
		assertEpsilonEquals(M, this.bounds.getMaxM());
	}

	@Test
	public void testEqualsObject() {
		assertFalse(this.bounds.equals(null));
		assertFalse(this.bounds.equals(new Object()));
		assertTrue(this.bounds.equals(this.bounds));
		assertTrue(this.bounds.equals(new ESRIBounds(this.bounds)));
		assertFalse(this.bounds.equals(new ESRIBounds()));
		assertTrue(this.bounds.equals(new ESRIBounds(
				this.minx, this.maxx, 
				this.miny, this.maxy, 
				this.minz, this.maxz,
				this.minm, this.maxm)));
	}

	@Test
	public void testCompareTo() {
		assertEquals(-1, this.bounds.compareTo(null));
		assertEquals(0, this.bounds.compareTo(this.bounds));
		assertEquals(0, this.bounds.compareTo(new ESRIBounds(this.bounds)));
		assertEquals(1, this.bounds.compareTo(new ESRIBounds()));
		assertEquals(0, this.bounds.compareTo(new ESRIBounds(
				this.minx, this.maxx, 
				this.miny, this.maxy, 
				this.minz, this.maxz,
				this.minm, this.maxm)));
		assertEquals(-1, this.bounds.compareTo(new ESRIBounds(
				this.minx-1, this.maxx, 
				this.miny, this.maxy, 
				this.minz, this.maxz,
				this.minm, this.maxm)));
		assertEquals(1, this.bounds.compareTo(new ESRIBounds(
				this.minx+1, this.maxx, 
				this.miny, this.maxy, 
				this.minz, this.maxz,
				this.minm, this.maxm)));
		assertEquals(-1, this.bounds.compareTo(new ESRIBounds(
				this.minx-1, this.maxx, 
				this.miny-1, this.maxy, 
				this.minz, this.maxz,
				this.minm, this.maxm)));
		assertEquals(1, this.bounds.compareTo(new ESRIBounds(
				this.minx+1, this.maxx, 
				this.miny-1, this.maxy, 
				this.minz, this.maxz,
				this.minm, this.maxm)));
	}

	@Test
	public void testGetMinX() {
		assertEpsilonEquals(this.minx, this.bounds.getMinX());
	}

	@Test
	public void testGetCenterX() {
		assertEpsilonEquals((this.minx+this.maxx)/2., this.bounds.getCenterX());
	}

	@Test
	public void testGetMaxX() {
		assertEpsilonEquals(this.maxx, this.bounds.getMaxX());
	}

	@Test
	public void testGetCenterY() {
		assertEpsilonEquals((this.miny+this.maxy)/2., this.bounds.getCenterY());
	}

	@Test
	public void testGetMinY() {
		assertEpsilonEquals(this.miny, this.bounds.getMinY());
	}

	@Test
	public void testGetMaxY() {
		assertEpsilonEquals(this.maxy, this.bounds.getMaxY());
	}

	@Test
	public void testGetMinZ() {
		assertEpsilonEquals(this.minz, this.bounds.getMinZ());
	}

	@Test
	public void testGetCenterZ() {
		assertEpsilonEquals((this.minz+this.maxz)/2., this.bounds.getCenterZ());
	}

	@Test
	public void testGetMaxZ() {
		assertEpsilonEquals(this.maxz, this.bounds.getMaxZ());
	}

	@Test
	public void testGetMinM() {
		assertEpsilonEquals(this.minm, this.bounds.getMinM());
	}

	@Test
	public void testGetCenterM() {
		assertEpsilonEquals((this.minm+this.maxm)/2., this.bounds.getCenterM());
	}

	@Test
	public void testGetMaxM() {
		assertEpsilonEquals(this.maxm, this.bounds.getMaxM());
	}

	@Test
	public void testToRectangle2D() {
		Rectangle2d expected = new Rectangle2d(
				this.minx, this.miny,
				this.maxx - this.minx,
				this.maxy - this.miny);
		assertEpsilonEquals(expected.getMinX(), this.bounds.toRectangle2d().getMinX());
		assertEpsilonEquals(expected.getMinY(), this.bounds.toRectangle2d().getMinY());
		assertEpsilonEquals(expected.getMaxX(), this.bounds.toRectangle2d().getMaxX());
		assertEpsilonEquals(expected.getMaxY(), this.bounds.toRectangle2d().getMaxY());
	}

	@Test
	public void testEnsureMinMax() {
		ESRIBounds b = new ESRIBounds(
				this.maxx, this.minx, 
				this.miny, this.maxy, 
				this.maxz, this.minz,
				this.minm, this.maxm);
		b.ensureMinMax(); 
		assertEpsilonEquals(this.minx, this.bounds.getMinX());
		assertEpsilonEquals(this.maxx, this.bounds.getMaxX());
		assertEpsilonEquals(this.miny, this.bounds.getMinY());
		assertEpsilonEquals(this.maxy, this.bounds.getMaxY());
		assertEpsilonEquals(this.minz, this.bounds.getMinZ());
		assertEpsilonEquals(this.maxz, this.bounds.getMaxZ());
		assertEpsilonEquals(this.minm, this.bounds.getMinM());
		assertEpsilonEquals(this.maxm, this.bounds.getMaxM());
	}

}
