/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2022 The original authors, and other authors.
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

package org.arakhne.afc.math.physics;

import static org.arakhne.afc.testtools.XbaseInlineTestUtil.assertInlineParameterUsage;

import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;

import org.arakhne.afc.testtools.AbstractTestCase;

@SuppressWarnings("all")
public class MeasureUnitUtilTest extends AbstractTestCase {
	
	@Test
	public void convertDoubleAngularUnitAngularUnit() {
		assertInlineParameterUsage(MeasureUnitUtil.class, "convert", double.class, AngularUnit.class, AngularUnit.class); //$NON-NLS-1$
	}

	@Test
	public void convertDoubleSpaceUnitSpaceUnit() {
		assertInlineParameterUsage(MeasureUnitUtil.class, "convert", double.class, SpaceUnit.class, SpaceUnit.class); //$NON-NLS-1$
	}

	@Test
	public void convertDoubleSpeedUnitSpeedUnit() {
		assertInlineParameterUsage(MeasureUnitUtil.class, "convert", double.class, SpeedUnit.class, SpeedUnit.class); //$NON-NLS-1$
	}

	@Test
	public void convertDoubleTimeUnitTimeUnit() {
		assertInlineParameterUsage(MeasureUnitUtil.class, "convert", double.class, TimeUnit.class, TimeUnit.class); //$NON-NLS-1$
	}

	@Test
	public void convertLongTimeUnitTimeUnit() {
		assertInlineParameterUsage(MeasureUnitUtil.class, "convert", long.class, TimeUnit.class, TimeUnit.class); //$NON-NLS-1$
	}

	@Test
	public void fh2ftDouble() {
		assertInlineParameterUsage(MeasureUnitUtil.class, "fh2ft", double.class); //$NON-NLS-1$
	}

	@Test
	public void ft2fhDouble() {
		assertInlineParameterUsage(MeasureUnitUtil.class, "ft2fh", double.class); //$NON-NLS-1$
	}

	@Test
	public void ft2inDouble() {
		assertInlineParameterUsage(MeasureUnitUtil.class, "ft2in", double.class); //$NON-NLS-1$
	}

	@Test
	public void in2fhDouble() {
		assertInlineParameterUsage(MeasureUnitUtil.class, "in2fh", double.class); //$NON-NLS-1$
	}

	@Test
	public void in2ftDouble() {
		assertInlineParameterUsage(MeasureUnitUtil.class, "in2ft", double.class); //$NON-NLS-1$
	}

	@Test
	public void inchToMetricDouble() {
		assertInlineParameterUsage(MeasureUnitUtil.class, "inchToMetric", double.class); //$NON-NLS-1$
	}

	@Test
	public void km2mDouble() {
		assertInlineParameterUsage(MeasureUnitUtil.class, "km2m", double.class); //$NON-NLS-1$
	}

	@Test
	public void kmh2msDouble() {
		assertInlineParameterUsage(MeasureUnitUtil.class, "kmh2ms", double.class); //$NON-NLS-1$
	}

	@Test
	public void m2fhDouble() {
		assertInlineParameterUsage(MeasureUnitUtil.class, "m2fh", double.class); //$NON-NLS-1$
	}

	@Test
	public void m2ftDouble() {
		assertInlineParameterUsage(MeasureUnitUtil.class, "m2ft", double.class); //$NON-NLS-1$
	}

	@Test
	public void m2inDouble() {
		assertInlineParameterUsage(MeasureUnitUtil.class, "m2in", double.class); //$NON-NLS-1$
	}

	@Test
	public void m2kmDouble() {
		assertInlineParameterUsage(MeasureUnitUtil.class, "m2km", double.class); //$NON-NLS-1$
	}

	@Test
	public void metricToInchDouble() {
		assertInlineParameterUsage(MeasureUnitUtil.class, "metricToInch", double.class); //$NON-NLS-1$
	}

	@Test
	public void micro2milliDouble() {
		assertInlineParameterUsage(MeasureUnitUtil.class, "micro2milli", double.class); //$NON-NLS-1$
	}

	@Test
	public void micro2nanoDouble() {
		assertInlineParameterUsage(MeasureUnitUtil.class, "micro2nano", double.class); //$NON-NLS-1$
	}

	@Test
	public void micro2unitDouble() {
		assertInlineParameterUsage(MeasureUnitUtil.class, "micro2unit", double.class); //$NON-NLS-1$
	}

	@Test
	public void milli2microDouble() {
		assertInlineParameterUsage(MeasureUnitUtil.class, "milli2micro", double.class); //$NON-NLS-1$
	}

	@Test
	public void milli2nano() {
		assertInlineParameterUsage(MeasureUnitUtil.class, "milli2nano", double.class); //$NON-NLS-1$
	}

	@Test
	public void milli2unit() {
		assertInlineParameterUsage(MeasureUnitUtil.class, "milli2unit", double.class); //$NON-NLS-1$
	}

	@Test
	public void ms2kmh() {
		assertInlineParameterUsage(MeasureUnitUtil.class, "ms2kmh", double.class); //$NON-NLS-1$
	}

	@Test
	public void nano2micro() {
		assertInlineParameterUsage(MeasureUnitUtil.class, "nano2micro", double.class); //$NON-NLS-1$
	}

	@Test
	public void nano2milli() {
		assertInlineParameterUsage(MeasureUnitUtil.class, "nano2milli", double.class); //$NON-NLS-1$
	}

	@Test
	public void nano2unit() {
		assertInlineParameterUsage(MeasureUnitUtil.class, "nano2unit", double.class); //$NON-NLS-1$
	}

	@Test
	public void pix2pixDouble() {
		assertInlineParameterUsage(MeasureUnitUtil.class, "pix2pix", double.class); //$NON-NLS-1$
	}

	@Test
	public void pix2pixFloat() {
		assertInlineParameterUsage(MeasureUnitUtil.class, "pix2pix", float.class); //$NON-NLS-1$
	}

	@Test
	public void pix2pixLong() {
		assertInlineParameterUsage(MeasureUnitUtil.class, "pix2pix", long.class); //$NON-NLS-1$
	}

	@Test
	public void unit2micro() {
		assertInlineParameterUsage(MeasureUnitUtil.class, "unit2micro", double.class); //$NON-NLS-1$
	}

	@Test
	public void unit2milli() {
		assertInlineParameterUsage(MeasureUnitUtil.class, "unit2milli", double.class); //$NON-NLS-1$
	}

	@Test
	public void unit2nano() {
		assertInlineParameterUsage(MeasureUnitUtil.class, "unit2nano", double.class); //$NON-NLS-1$
	}

	
}
