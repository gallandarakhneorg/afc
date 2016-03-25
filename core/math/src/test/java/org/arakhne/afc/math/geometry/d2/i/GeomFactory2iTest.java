/* 
 * $Id$
 * 
 * Copyright (C) 2011 Janus Core Developers
 * Copyright (C) 2012-13 Stephane GALLAND.
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
package org.arakhne.afc.math.geometry.d2.i;

import org.arakhne.afc.math.geometry.coordinatesystem.CoordinateSystem2DTestRule;
import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.Vector2D;
import org.arakhne.afc.math.geometry.d2.ai.AbstractGeomFactory2aiTest;
import org.arakhne.afc.math.geometry.d2.ai.GeomFactory2ai;
import org.junit.Rule;

@SuppressWarnings("all")
public class GeomFactory2iTest extends AbstractGeomFactory2aiTest {

	protected GeomFactory2ai<?, ?, ?> createFactory() {
		return new GeomFactory2i();
	}
	
	protected Point2D createPoint(int x, int y) {
		return new Point2i(x, y);
	}

	protected Vector2D createVector(int x, int y) {
		return new Vector2i(x, y);
	}
	
}