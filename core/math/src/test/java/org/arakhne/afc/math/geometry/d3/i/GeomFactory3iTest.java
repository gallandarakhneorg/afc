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
package org.arakhne.afc.math.geometry.d3.i;

import org.arakhne.afc.math.geometry.d3.Point3D;
import org.arakhne.afc.math.geometry.d3.Vector3D;
import org.arakhne.afc.math.geometry.d3.ai.AbstractGeomFactory3aiTest;
import org.arakhne.afc.math.geometry.d3.ai.GeomFactory3ai;

@SuppressWarnings("all")
public class GeomFactory3iTest extends AbstractGeomFactory3aiTest {

	protected GeomFactory3ai<?, ?, ?, ?> createFactory() {
		return new GeomFactory3i();
	}
	
	protected Point3D createPoint(int x, int y, int z) {
		return new Point3i(x, y, z);
	}

	protected Vector3D createVector(int x, int y, int z) {
		return new Vector3i(x, y, z);
	}
	
}