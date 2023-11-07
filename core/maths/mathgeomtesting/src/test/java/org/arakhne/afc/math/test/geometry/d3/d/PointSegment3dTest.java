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

import org.arakhne.afc.math.geometry.d3.afp.Segment3afp;
import org.arakhne.afc.math.geometry.d3.d.Point3d;
import org.arakhne.afc.math.geometry.d3.d.PointSegment3d;
import org.arakhne.afc.math.geometry.d3.d.Quaternion4d;
import org.arakhne.afc.math.geometry.d3.d.AlignedBox3d;
import org.arakhne.afc.math.geometry.d3.d.Vector3d;
import org.arakhne.afc.math.test.geometry.d3.afp.AbstractSegment3afpTest;
import org.arakhne.afc.math.test.geometry.d3.afp.TestShapeFactory3afp;

@SuppressWarnings("all")
public class PointSegment3dTest extends AbstractSegment3afpTest<PointSegment3d, AlignedBox3d> {

	private static final LocalFactory SINGLETON = new LocalFactory();
	
	@Override
	protected TestShapeFactory3d createFactory() {
		return SINGLETON;
	}

	private static class LocalFactory extends TestShapeFactory3d {

		@Override
		public Segment3afp<?, ?, ?, Point3d, Vector3d, Quaternion4d, AlignedBox3d> createSegment(double x1,
				double y1, double z1, double x2, double y2, double z2) {
			return new PointSegment3d(x1, y1, z1, x2, y2, z2);
		}
	}

}
