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
package org.arakhne.afc.math.geometry.d2.fp;

import org.arakhne.afc.math.geometry.d2.afp.AbstractPathWindingRule2afpTest;
import org.arakhne.afc.math.geometry.d2.afp.TestShapeFactory;

@SuppressWarnings("all")
public class PathWindingRule2fpTest extends AbstractPathWindingRule2afpTest<Point2fp, Rectangle2fp> {

	@Override
	protected TestShapeFactory<Point2fp, Rectangle2fp> createFactory() {
		return TestShapeFactory2fp.SINGLETON;
	}

}
