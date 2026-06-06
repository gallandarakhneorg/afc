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

package org.arakhne.afc.math.geometry.d1.tests.d;

import org.arakhne.afc.math.geometry.d1.d.DefaultSegment1d;
import org.arakhne.afc.math.geometry.d1.d.Point1d;
import org.arakhne.afc.math.geometry.d1.d.Tuple1d;
import org.arakhne.afc.math.geometry.d1.d.Vector1d;
import org.arakhne.afc.math.geometry.d1.tests.AbstractTuple1DTestCase;
import org.arakhne.afc.math.geometry.d2.d.Point2d;

@SuppressWarnings("all")
public class Tuple1dTest extends AbstractTuple1DTestCase<Tuple1d<Vector1d, Vector1d, Point1d, DefaultSegment1d>, DefaultSegment1d> {

	@Override
	public boolean isIntCoordinates() {
		return false;
	}
	
	@Override
	public Tuple1d createTuple(double x, double y) {
		return new Tuple1d(getS(), x, y);
	}

	@Override
	public DefaultSegment1d createSegment() {
		return new DefaultSegment1d(
				new Point2d(125.3569, 14587.659),
				new Point2d(442.74158, 12473.93215));
	}

}
