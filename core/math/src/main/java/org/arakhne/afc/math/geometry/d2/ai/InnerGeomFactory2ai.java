/* 
 * $Id$
 * 
 * Copyright (C) 2010-2013 Stephane GALLAND.
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

import org.arakhne.afc.math.geometry.d2.Point2D;

/** Internal-usage factory of geometrical elements.
 * 
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
@SuppressWarnings("rawtypes")
class InnerGeomFactory2ai
	implements GeomFactory2ai {

	/** Default instance.
	 */
	public static final InnerGeomFactory2ai DEFAULT = new InnerGeomFactory2ai();
	
	@Override
	public Point2D convertToPoint(Point2D point) {
		if (point instanceof InnerComputationPoint2ai) {
			return point;
		}
		return new InnerComputationPoint2ai(point.ix(), point.iy());
	}

	@Override
	public Point2D newPoint() {
		return new InnerComputationPoint2ai();
	}

	@Override
	public Point2D newPoint(int x, int y) {
		return new InnerComputationPoint2ai(x, y);
	}

}