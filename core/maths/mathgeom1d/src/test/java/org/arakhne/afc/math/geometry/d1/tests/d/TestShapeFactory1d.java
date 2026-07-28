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

import org.arakhne.afc.math.geometry.base.d1.Segment1D;
import org.arakhne.afc.math.geometry.base.d2.InnerComputationPoint2D;
import org.arakhne.afc.math.geometry.base.d2.Point2D;
import org.arakhne.afc.math.geometry.d1.d.Point1d;
import org.arakhne.afc.math.geometry.d1.d.Rectangle1d;
import org.arakhne.afc.math.geometry.d1.d.Vector1d;
import org.arakhne.afc.math.geometry.d1.tests.afp.TestShapeFactory;

@SuppressWarnings("all")
public class TestShapeFactory1d implements TestShapeFactory<Point1d, Vector1d, Rectangle1d>{

	public static final TestShapeFactory1d SINGLETON = new TestShapeFactory1d();
	
	@Override
	public Point1d createPoint(Segment1D<?, ?> segment, double x, double y) {
		return new Point1d(segment, x, y);
	}

	@Override
	public Vector1d createVector(Segment1D<?, ?> segment, double x, double y) {
		return new Vector1d(segment, x, y);
	}

	@Override
	public Rectangle1d createBox(Segment1D<?, ?> segment, double x, double y, double width, double height) {
		return new Rectangle1d(segment, x, y, width, height);
	}

}