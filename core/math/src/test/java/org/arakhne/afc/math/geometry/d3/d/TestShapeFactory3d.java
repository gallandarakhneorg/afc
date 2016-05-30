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
package org.arakhne.afc.math.geometry.d3.d;

import org.arakhne.afc.math.geometry.PathWindingRule;
import org.arakhne.afc.math.geometry.d3.afp.MultiShape3afp;
import org.arakhne.afc.math.geometry.d3.afp.Path3afp;
import org.arakhne.afc.math.geometry.d3.afp.Segment3afp;
import org.arakhne.afc.math.geometry.d3.afp.Sphere3afp;
import org.arakhne.afc.math.geometry.d3.afp.TestShapeFactory3afp;

@SuppressWarnings("all")
public class TestShapeFactory3d implements TestShapeFactory3afp<Point3d, Vector3d, RectangularPrism3d> {
	
	public static final TestShapeFactory3d SINGLETON = new TestShapeFactory3d();
	
	public Segment3afp<?, ?, ?, Point3d, Vector3d, RectangularPrism3d> createSegment(double x1, double y1, double z1, double x2, double y2, double z2) {
		return new Segment3d(x1, y1, z1, x2, y2, z2);
	}
	
	public RectangularPrism3d createRectangularPrism(double x, double y, double z, double width, double height, double depth) {
		assert (width >= 0) : "Width must be positive or zero";
		assert (height >= 0) : "Height must be positive or zero";
		assert (depth >= 0) : "depth must be positive or zero";
		return new RectangularPrism3d(x, y, z, width, height, depth);
	}

	public Sphere3afp<?, ?, ?, Point3d, Vector3d, RectangularPrism3d> createSphere(double x, double y, double z, double radius) {
		assert (radius >= 0) : "Radius must be positive or zero";
		return new Sphere3d(x, y, z, radius);
	}
	
	public Point3d createPoint(double x, double y, double z) {
		return new Point3d(x, y, z);
	}

	public Vector3d createVector(double x, double y, double z) {
		return new Vector3d(x, y, z);
	}

	public Path3afp<?, ?, ?, Point3d, Vector3d, RectangularPrism3d> createPath(PathWindingRule rule) {
		if (rule == null) {
			return new Path3d();
		}
		return new Path3d(rule);
	}

		@Override
	public MultiShape3afp<?, ?, ?, ?, Point3d, Vector3d, RectangularPrism3d> createMultiShape() {
		return new MultiShape3d();
	}

}