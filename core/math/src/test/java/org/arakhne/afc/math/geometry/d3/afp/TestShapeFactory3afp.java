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
package org.arakhne.afc.math.geometry.d3.afp;

import org.arakhne.afc.math.geometry.PathWindingRule;
import org.arakhne.afc.math.geometry.d3.Point3D;
import org.arakhne.afc.math.geometry.d3.Vector3D;

@SuppressWarnings("all")
public interface TestShapeFactory3afp<P extends Point3D<? super P, ? super V>, V extends Vector3D<? super V, ? super P>, B extends RectangularPrism3afp<?, ?, ?, P, V, B>> {
	
	Segment3afp<?, ?, ?, P, V, B> createSegment(double x1, double y1, double z1, double x2, double y2, double z2);
	
	B createRectangularPrism(double x, double y, double z, double width, double height, double depth);

	Sphere3afp<?, ?, ?, P, V, B> createSphere(double x, double y, double z, double radius);
	
	P createPoint(double x, double y, double z);

	V createVector(double x, double y, double z);

	Path3afp<?, ?, ?, P, V, B> createPath(PathWindingRule rule);

	MultiShape3afp<?, ?, ?, ?, P, V, B> createMultiShape();

}