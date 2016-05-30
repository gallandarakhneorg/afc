/* 
 * $Id$
 * 
 * Copyright (c) 2006-10, Multiagent Team, Laboratoire Systemes et Transports, Universite de Technologie de Belfort-Montbeliard.
 * Copyright (C) 2012 Stephane GALLAND.
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 * This program is free software; you can redistribute it and/or modify
 */
package org.arakhne.afc.math.geometry.d3.i;

import org.arakhne.afc.math.geometry.PathWindingRule;
import org.arakhne.afc.math.geometry.d3.Point3D;
import org.arakhne.afc.math.geometry.d3.Vector3D;
import org.arakhne.afc.math.geometry.d3.ai.MultiShape3ai;
import org.arakhne.afc.math.geometry.d3.ai.Path3ai;
import org.arakhne.afc.math.geometry.d3.ai.Segment3ai;
import org.arakhne.afc.math.geometry.d3.ai.Sphere3ai;
import org.arakhne.afc.math.geometry.d3.ai.TestShapeFactory3ai;

@SuppressWarnings("all")
public class TestShapeFactory3i implements TestShapeFactory3ai<Point3i, Vector3i, RectangularPrism3i> {
	
	public static final TestShapeFactory3i SINGLETON = new TestShapeFactory3i();
	
	public Segment3ai<?, ?, ?, Point3i, Vector3i, RectangularPrism3i> createSegment(int x1, int y1, int z1, int x2, int y2, int z2) {
		return new Segment3i(x1, y1, z1, x2, y2, z2);
	}
	
	public RectangularPrism3i createRectangularPrism(int x, int y, int z, int width, int height, int depth) {
		return new RectangularPrism3i(x, y, z, width, height, depth);
	}

	public Sphere3ai<?, ?, ?, Point3i, Vector3i, RectangularPrism3i> createSphere(int x, int y, int z, int radius) {
		return new Sphere3i(x, y, z, radius);
	}
	
	public Point3D createPoint(int x, int y, int z) {
		return new Point3i(x, y, z);
	}

	public Vector3D createVector(int x, int y, int z) {
		return new Vector3i(x, y, z);
	}

	public Path3ai<?, ?, ?, Point3i, Vector3i, RectangularPrism3i> createPath(PathWindingRule rule) {
		if (rule == null) {
			return new Path3i();
		}
		return new Path3i(rule);
	}
	
	public MultiShape3ai<?, ?, ?, ?, Point3i, Vector3i, RectangularPrism3i> createMultiShape() {
		return new MultiShape3i();
	}

}