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

package org.arakhne.afc.math.geometry.d3.dfx;

import org.arakhne.afc.math.geometry.PathWindingRule;
import org.arakhne.afc.math.geometry.d3.afp.MultiShape3afp;
import org.arakhne.afc.math.geometry.d3.afp.Path3afp;
import org.arakhne.afc.math.geometry.d3.afp.Segment3afp;
import org.arakhne.afc.math.geometry.d3.afp.Sphere3afp;
import org.arakhne.afc.math.geometry.d3.afp.TestShapeFactory3afp;

@SuppressWarnings("all")
public class TestShapeFactory3dfx implements TestShapeFactory3afp<Point3dfx, Vector3dfx, RectangularPrism3dfx> {
	
	public static final TestShapeFactory3dfx SINGLETON = new TestShapeFactory3dfx();
	
	@Override
	public Segment3afp<?, ?, ?, Point3dfx, Vector3dfx, RectangularPrism3dfx> createSegment(double x1, double y1, double z1, double x2, double y2, double z2) {
		return new Segment3dfx(x1, y1, z1, x2, y2, z2);
	}
	
	@Override
	public RectangularPrism3dfx createRectangularPrism(double x, double y, double z, double width, double height, double depth) {
		assert (width >= 0) : "Width must be positive or zero"; //$NON-NLS-1$
		assert (height >= 0) : "Height must be positive or zero"; //$NON-NLS-1$
		assert (depth >= 0) : "Depth must be positive or zero"; //$NON-NLS-1$
		return new RectangularPrism3dfx(x, y, z, width, height, depth);
	}

	@Override
	public Sphere3afp<?, ?, ?, Point3dfx, Vector3dfx, RectangularPrism3dfx> createSphere(double x, double y, double z, double radius) {
		assert (radius >= 0) : "Radius must be positive or zero"; //$NON-NLS-1$
		return new Sphere3dfx(x, y, z, radius);
	}
	
	@Override
	public Point3dfx createPoint(double x, double y, double z) {
		return new Point3dfx(x, y, z);
	}

	@Override
	public Vector3dfx createVector(double x, double y, double z) {
		return new Vector3dfx(x, y, z);
	}

	@Override
	public Path3afp<?, ?, ?, Point3dfx, Vector3dfx, RectangularPrism3dfx> createPath(PathWindingRule rule) {
		if (rule == null) {
			return new Path3dfx();
		}
		return new Path3dfx(rule);
	}

	@Override
	public MultiShape3afp<?, ?, ?, ?, Point3dfx, Vector3dfx, RectangularPrism3dfx> createMultiShape() {
		return new MultiShape3dfx();
	}
	
}
