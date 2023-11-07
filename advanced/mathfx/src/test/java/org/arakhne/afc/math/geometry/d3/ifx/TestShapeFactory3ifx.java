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

package org.arakhne.afc.math.geometry.d3.ifx;

import org.junit.jupiter.api.Disabled;

import org.arakhne.afc.math.geometry.PathWindingRule;
import org.arakhne.afc.math.geometry.d3.ai.MultiShape3ai;
import org.arakhne.afc.math.geometry.d3.ai.Path3ai;
import org.arakhne.afc.math.geometry.d3.ai.Segment3ai;
import org.arakhne.afc.math.geometry.d3.ai.Sphere3ai;
import org.arakhne.afc.math.test.geometry.d3.ai.TestShapeFactory3ai;

@SuppressWarnings("all")
@Disabled("temporary")
public class TestShapeFactory3ifx implements TestShapeFactory3ai<Point3ifx, Vector3ifx, AlignedBox3ifx> {
	
	public static final TestShapeFactory3ifx SINGLETON = new TestShapeFactory3ifx();
	
	@Override
	public Segment3ai<?, ?, ?, Point3ifx, Vector3ifx, AlignedBox3ifx> createSegment(int x1, int y1, int z1, int x2, int y2, int z2) {
		return new Segment3ifx(x1, y1, z1, x2, y2, z2);
	}
	
	@Override
	public AlignedBox3ifx createAlignedBox(int x, int y, int z, int width, int height, int depth) {
		return new AlignedBox3ifx(x, y, z, width, height, depth);
	}

	@Override
	public Sphere3ai<?, ?, ?, Point3ifx, Vector3ifx, AlignedBox3ifx> createSphere(int x, int y, int z, int radius) {
		return new Sphere3ifx(x, y, z, radius);
	}
	
	@Override
	public Point3ifx createPoint(int x, int y, int z) {
		return new Point3ifx(x, y, z);
	}

	@Override
	public Vector3ifx createVector(int x, int y, int z) {
		return new Vector3ifx(x, y, z);
	}

	@Override
	public Path3ai<?, ?, ?, Point3ifx, Vector3ifx, AlignedBox3ifx> createPath(PathWindingRule rule) {
		if (rule == null) {
			return new Path3ifx();
		}
		return new Path3ifx(rule);

	}
	
	@Override
	public MultiShape3ai<?, ?, ?, ?, Point3ifx, Vector3ifx, AlignedBox3ifx> createMultiShape() {
		return new MultiShape3ifx();
	}

}
