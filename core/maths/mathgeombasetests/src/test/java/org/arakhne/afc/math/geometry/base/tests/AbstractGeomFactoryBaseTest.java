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

package org.arakhne.afc.math.geometry.base.tests;

import org.arakhne.afc.math.geometry.base.AbstractGeomFactoryBase;
import org.arakhne.afc.math.geometry.base.GeomConstants;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/** Test of Abstract implementation of a factory of geometric primitives.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 18.0
 */
@SuppressWarnings("all")
public class AbstractGeomFactoryBaseTest extends AbstractMathTestCase {

	@Test
	@DisplayName("getGlobalSplineApproximationRatio")
	public void getGlobalSplineApproximationRatio() {
		assertEpsilonEquals(GeomConstants.SPLINE_APPROXIMATION_RATIO, AbstractGeomFactoryBase.getGlobalSplineApproximationRatio());
	}

	@Test
	@DisplayName("setGlobalSplineApproximationRatio(null)")
	public void setGlobalSplineApproximationRatio_null() {
		AbstractGeomFactoryBase.setGlobalSplineApproximationRatio(456.3789);
		AbstractGeomFactoryBase.setGlobalSplineApproximationRatio(null);
		assertEpsilonEquals(GeomConstants.SPLINE_APPROXIMATION_RATIO, AbstractGeomFactoryBase.getGlobalSplineApproximationRatio());
	}

	@Test
	@DisplayName("setGlobalSplineApproximationRatio(NaN)")
	public void setGlobalSplineApproximationRatio_nan() {
		AbstractGeomFactoryBase.setGlobalSplineApproximationRatio(456.3789);
		AbstractGeomFactoryBase.setGlobalSplineApproximationRatio(Double.NaN);
		assertEpsilonEquals(GeomConstants.SPLINE_APPROXIMATION_RATIO, AbstractGeomFactoryBase.getGlobalSplineApproximationRatio());
	}

	@Test
	@DisplayName("setGlobalSplineApproximationRatio(357.456)")
	public void setGlobalSplineApproximationRatio_positivedouble() {
		AbstractGeomFactoryBase.setGlobalSplineApproximationRatio(357.456);
		assertEpsilonEquals(357.456, AbstractGeomFactoryBase.getGlobalSplineApproximationRatio());
	}

	@Test
	@DisplayName("setGlobalSplineApproximationRatio(-357.456)")
	public void setGlobalSplineApproximationRatio_negativedouble() {
		AbstractGeomFactoryBase.setGlobalSplineApproximationRatio(-357.456);
		assertEpsilonEquals(0., AbstractGeomFactoryBase.getGlobalSplineApproximationRatio());
	}

	@Test
	@DisplayName("getSplineApproximationRatio")
	public void getSplineApproximationRatio() {
		var object = new GeomFactoryBaseStub();
		assertEpsilonEquals(GeomConstants.SPLINE_APPROXIMATION_RATIO, object.getSplineApproximationRatio());
	}

	@Test
	@DisplayName("setSplineApproximationRatio(null)")
	public void setSplineApproximationRatio_null() {
		AbstractGeomFactoryBase.setGlobalSplineApproximationRatio(null);
		var object = new GeomFactoryBaseStub();
		object.setSplineApproximationRatio(456.3789);
		object.setSplineApproximationRatio(null);
		assertEpsilonEquals(GeomConstants.SPLINE_APPROXIMATION_RATIO, object.getSplineApproximationRatio());
	}

	@Test
	@DisplayName("setSplineApproximationRatio(NaN)")
	public void setSplineApproximationRatio_nan() {
		AbstractGeomFactoryBase.setGlobalSplineApproximationRatio(null);
		var object = new GeomFactoryBaseStub();
		object.setSplineApproximationRatio(456.3789);
		object.setSplineApproximationRatio(Double.NaN);
		assertEpsilonEquals(GeomConstants.SPLINE_APPROXIMATION_RATIO, object.getSplineApproximationRatio());
	}

	@Test
	@DisplayName("setSplineApproximationRatio(456.3789)")
	public void setSplineApproximationRatio_positivedouble() {
		AbstractGeomFactoryBase.setGlobalSplineApproximationRatio(null);
		var object = new GeomFactoryBaseStub();
		object.setSplineApproximationRatio(456.3789);
		assertEpsilonEquals(456.3789, object.getSplineApproximationRatio());
	}

	@Test
	@DisplayName("setSplineApproximationRatio(-456.3789)")
	public void setSplineApproximationRatio_negativedouble() {
		AbstractGeomFactoryBase.setGlobalSplineApproximationRatio(null);
		var object = new GeomFactoryBaseStub();
		object.setSplineApproximationRatio(-456.3789);
		assertEpsilonEquals(0., object.getSplineApproximationRatio());
	}

	private class GeomFactoryBaseStub extends AbstractGeomFactoryBase {
		//
	}

}
