/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2016 The original authors, and other authors.
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

package org.arakhne.afc.math.geometry.d2.i;

import org.arakhne.afc.math.geometry.coordinatesystem.CoordinateSystem2DTestRule;
import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.Vector2D;
import org.arakhne.afc.math.geometry.d2.ai.AbstractGeomFactory2aiTest;
import org.arakhne.afc.math.geometry.d2.ai.GeomFactory2ai;
import org.junit.Rule;

@SuppressWarnings("all")
public class GeomFactory2iTest extends AbstractGeomFactory2aiTest {

	protected GeomFactory2ai<?, ?, ?, ?> createFactory() {
		return new GeomFactory2i();
	}
	
	protected Point2D createPoint(int x, int y) {
		return new Point2i(x, y);
	}

	protected Vector2D createVector(int x, int y) {
		return new Vector2i(x, y);
	}
	
}