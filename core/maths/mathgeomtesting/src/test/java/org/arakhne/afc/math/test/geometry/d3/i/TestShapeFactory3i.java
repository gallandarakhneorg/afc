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

package org.arakhne.afc.math.test.geometry.d3.i;

import org.arakhne.afc.math.geometry.PathWindingRule;
import org.arakhne.afc.math.geometry.d3.Point3D;
import org.arakhne.afc.math.geometry.d3.Vector3D;
import org.arakhne.afc.math.geometry.d3.ai.MultiShape3ai;
import org.arakhne.afc.math.geometry.d3.ai.Path3ai;
import org.arakhne.afc.math.geometry.d3.ai.Segment3ai;
import org.arakhne.afc.math.geometry.d3.ai.Sphere3ai;
import org.arakhne.afc.math.geometry.d3.i.MultiShape3i;
import org.arakhne.afc.math.geometry.d3.i.Path3i;
import org.arakhne.afc.math.geometry.d3.i.Point3i;
import org.arakhne.afc.math.geometry.d3.i.RectangularPrism3i;
import org.arakhne.afc.math.geometry.d3.i.Segment3i;
import org.arakhne.afc.math.geometry.d3.i.Sphere3i;
import org.arakhne.afc.math.geometry.d3.i.Vector3i;
import org.arakhne.afc.math.test.geometry.d3.ai.TestShapeFactory3ai;

@SuppressWarnings("all")
public class TestShapeFactory3i implements TestShapeFactory3ai<Point3i, Vector3i, RectangularPrism3i> {
	
	public static final TestShapeFactory3i SINGLETON = new TestShapeFactory3i();
	
	@Override
	public Segment3ai<?, ?, ?, Point3i, Vector3i, RectangularPrism3i> createSegment(int x1, int y1, int z1, int x2, int y2, int z2) {
		return new Segment3i(x1, y1, z1, x2, y2, z2);
	}
	
	@Override
	public RectangularPrism3i createRectangularPrism(int x, int y, int z, int width, int height, int depth) {
		return new RectangularPrism3i(x, y, z, width, height, depth);
	}

	@Override
	public Sphere3ai<?, ?, ?, Point3i, Vector3i, RectangularPrism3i> createSphere(int x, int y, int z, int radius) {
		return new Sphere3i(x, y, z, radius);
	}
	
	@Override
	public Point3D createPoint(int x, int y, int z) {
		return new Point3i(x, y, z);
	}

	@Override
	public Vector3D createVector(int x, int y, int z) {
		return new Vector3i(x, y, z);
	}

	@Override
	public Path3ai<?, ?, ?, Point3i, Vector3i, RectangularPrism3i> createPath(PathWindingRule rule) {
		if (rule == null) {
			return new Path3i();
		}
		return new Path3i(rule);
	}
	
	@Override
	public MultiShape3ai<?, ?, ?, ?, Point3i, Vector3i, RectangularPrism3i> createMultiShape() {
		return new MultiShape3i();
	}

}
