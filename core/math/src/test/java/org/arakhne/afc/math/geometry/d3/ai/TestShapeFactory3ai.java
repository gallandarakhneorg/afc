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
package org.arakhne.afc.math.geometry.d3.ai;

import org.arakhne.afc.math.geometry.PathWindingRule;
import org.arakhne.afc.math.geometry.d3.Point3D;
import org.arakhne.afc.math.geometry.d3.Vector3D;

@SuppressWarnings("all")
public interface TestShapeFactory3ai<P extends Point3D<? super P, ? super V>,
		V extends Vector3D<? super V, ? super P>,
		B extends RectangularPrism3ai<?, ?, ?, P, V, B>> {
	
	Segment3ai<?, ?, ?, P, V, B> createSegment(int x1, int y1, int z1, int x2, int y2, int z2);
	
	B createRectangle(int x, int y, int z, int width, int height, int depth);

	Sphere3ai<?, ?, ?, P, V, B> createCircle(int x, int y, int z, int radius);
	
	Point3D createPoint(int x, int y, int z);

	Vector3D createVector(int x, int y, int z);

	Path3ai<?, ?, ?, P, V, B> createPath(PathWindingRule rile);
	
	MultiShape3ai<?, ?, ?, ?, P, V, B> createMultiShape();
	
}