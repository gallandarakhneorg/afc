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
package org.arakhne.afc.math.geometry.d2.ai;

import org.arakhne.afc.math.geometry.PathWindingRule;
import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.Vector2D;

@SuppressWarnings("all")
public interface TestShapeFactory<P extends Point2D<? super P, ? super V>,
		V extends Vector2D<? super V, ? super P>,
		B extends Rectangle2ai<?, ?, ?, P, V, B>> {
	
	Segment2ai<?, ?, ?, P, V, B> createSegment(int x1, int y1, int x2, int y2);
	
	B createRectangle(int x, int y, int width, int height);

	Circle2ai<?, ?, ?, P, V, B> createCircle(int x, int y, int radius);
	
	Point2D createPoint(int x, int y);

	Vector2D createVector(int x, int y);

	Path2ai<?, ?, ?, P, V, B> createPath(PathWindingRule rile);
	
	MultiShape2ai<?, ?, ?, ?, P, V, B> createMultiShape();
	
}