/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2020 The original authors, and other authors.
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

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
@SuppressWarnings("all")
public class ESRIFileUtilTest extends AbstractIoShapeTest {

	/**
	 * Test method for {@link org.arakhne.afc.io.shape.ESRIFileUtil#toESRI_x(double)}.
	 * @throws Exception
	 */
	public void testToESRI_x() throws Exception {
		double n = getRandom().nextDouble();
		assertEpsilonEquals(n, ESRIFileUtil.toESRI_x(n));
	}

	/**
	 * Test method for {@link org.arakhne.afc.io.shape.ESRIFileUtil#fromESRI_x(double)}.
	 * @throws Exception
	 */
	public void testFromESRI_x() throws Exception {
		double n = getRandom().nextDouble();
		assertEpsilonEquals(n, ESRIFileUtil.fromESRI_x(n));
	}

	/**
	 * Test method for {@link org.arakhne.afc.io.shape.ESRIFileUtil#toESRI_y(double)}.
	 * @throws Exception
	 */
	public void testToESRI_y() throws Exception {
		double n = getRandom().nextDouble();
		assertEpsilonEquals(n, ESRIFileUtil.toESRI_y(n));
	}

	/**
	 * Test method for {@link org.arakhne.afc.io.shape.ESRIFileUtil#fromESRI_y(double)}.
	 * @throws Exception
	 */
	public void testFromESRI_y() throws Exception {
		double n = getRandom().nextDouble();
		assertEpsilonEquals(n, ESRIFileUtil.fromESRI_y(n));
	}

	/**
	 * Test method for {@link org.arakhne.afc.io.shape.ESRIFileUtil#toESRI_z(double)}.
	 * @throws Exception
	 */
	public void testToESRI_z() throws Exception {
		double n = getRandom().nextDouble();
		assertEpsilonEquals(n, ESRIFileUtil.toESRI_z(n));
	}

	/**
	 * Test method for {@link org.arakhne.afc.io.shape.ESRIFileUtil#fromESRI_z(double)}.
	 * @throws Exception
	 */
	public void testFromESRI_z() throws Exception {
		double n = getRandom().nextDouble();
		assertEpsilonEquals(n, ESRIFileUtil.fromESRI_z(n));
	}

	/**
	 * Test method for {@link org.arakhne.afc.io.shape.ESRIFileUtil#toESRI_m(double)}.
	 * @throws Exception
	 */
	public void testToESRI_m() throws Exception {
		double n = getRandom().nextDouble();
		assertEpsilonEquals(n, ESRIFileUtil.toESRI_m(n));
	}

	/**
	 * Test method for {@link org.arakhne.afc.io.shape.ESRIFileUtil#fromESRI_m(double)}.
	 * @throws Exception
	 */
	public void testFromESRI_m() throws Exception {
		double n = getRandom().nextDouble();
		assertEpsilonEquals(n, ESRIFileUtil.fromESRI_m(n));
	}

	/**
	 * Test method for {@link org.arakhne.afc.io.shape.ESRIFileUtil#toESRI(double)}.
	 */
	public void testToESRIDouble() {
		double n = getRandom().nextDouble();
		assertEpsilonEquals(n, ESRIFileUtil.toESRI(n));
		assertEpsilonEquals(ESRIFileUtil.ESRI_NAN, ESRIFileUtil.toESRI(ESRIFileUtil.ESRI_NAN));
		assertEpsilonEquals(ESRIFileUtil.ESRI_NAN, ESRIFileUtil.toESRI(Double.NaN));
		assertEpsilonEquals(ESRIFileUtil.ESRI_NAN, ESRIFileUtil.toESRI(Double.POSITIVE_INFINITY));
		assertEpsilonEquals(ESRIFileUtil.ESRI_NAN, ESRIFileUtil.toESRI(Double.NEGATIVE_INFINITY));
	}

	/**
	 * Test method for {@link org.arakhne.afc.io.shape.ESRIFileUtil#toESRI(float)}.
	 */
	public void testToESRIFloat() {
		float n = getRandom().nextFloat();
		assertEpsilonEquals(n, ESRIFileUtil.toESRI(n));
		assertEpsilonEquals(ESRIFileUtil.ESRI_NAN, ESRIFileUtil.toESRI((float)ESRIFileUtil.ESRI_NAN));
		assertEpsilonEquals(ESRIFileUtil.ESRI_NAN, ESRIFileUtil.toESRI(Float.NaN));
		assertEpsilonEquals(ESRIFileUtil.ESRI_NAN, ESRIFileUtil.toESRI(Float.POSITIVE_INFINITY));
		assertEpsilonEquals(ESRIFileUtil.ESRI_NAN, ESRIFileUtil.toESRI(Float.NEGATIVE_INFINITY));
	}

	/**
	 * Test method for {@link org.arakhne.afc.io.shape.ESRIFileUtil#isESRINaN(double)}.
	 * @throws Exception
	 */
	public void testIsESRINaNDouble() throws Exception {
		double n = getRandom().nextDouble();
		assertFalse(ESRIFileUtil.isESRINaN(n));
		assertTrue(ESRIFileUtil.isESRINaN(ESRIFileUtil.ESRI_NAN));
		assertTrue(ESRIFileUtil.isESRINaN(Double.NaN));
		assertTrue(ESRIFileUtil.isESRINaN(Double.POSITIVE_INFINITY));
		assertTrue(ESRIFileUtil.isESRINaN(Double.NEGATIVE_INFINITY));
	}

	/**
	 * Test method for {@link org.arakhne.afc.io.shape.ESRIFileUtil#isESRINaN(float)}.
	 */
	public void testIsESRINaNFloat() {
		float n = getRandom().nextFloat();
		assertFalse(ESRIFileUtil.isESRINaN(n));
		assertTrue(ESRIFileUtil.isESRINaN((float)ESRIFileUtil.ESRI_NAN));
		assertTrue(ESRIFileUtil.isESRINaN(Float.NaN));
		assertTrue(ESRIFileUtil.isESRINaN(Float.POSITIVE_INFINITY));
		assertTrue(ESRIFileUtil.isESRINaN(Float.NEGATIVE_INFINITY));
	}

	/**
	 * Test method for {@link org.arakhne.afc.io.shape.ESRIFileUtil#fromESRI(double)}.
	 */
	public void testFromESRIDouble() {
		double n = getRandom().nextDouble();
		assertEpsilonEquals(n, ESRIFileUtil.fromESRI(n));
		assertTrue(Double.isNaN(ESRIFileUtil.fromESRI(ESRIFileUtil.ESRI_NAN)));
		assertTrue(Double.isNaN(ESRIFileUtil.fromESRI(Double.NaN)));
		assertTrue(Double.isNaN(ESRIFileUtil.fromESRI(Double.POSITIVE_INFINITY)));
		assertTrue(Double.isNaN(ESRIFileUtil.fromESRI(Double.NEGATIVE_INFINITY)));
	}

	/**
	 * Test method for {@link org.arakhne.afc.io.shape.ESRIFileUtil#fromESRI(float)}.
	 * @throws Exception
	 */
	public void testFromESRIFloat() throws Exception {
		float n = getRandom().nextFloat();
		assertEpsilonEquals(n, ESRIFileUtil.fromESRI(n));
		assertTrue(Double.isNaN(ESRIFileUtil.fromESRI((float)ESRIFileUtil.ESRI_NAN)));
		assertTrue(Double.isNaN(ESRIFileUtil.fromESRI(Float.NaN)));
		assertTrue(Double.isNaN(ESRIFileUtil.fromESRI(Float.POSITIVE_INFINITY)));
		assertTrue(Double.isNaN(ESRIFileUtil.fromESRI(Float.NEGATIVE_INFINITY)));
	}

}
