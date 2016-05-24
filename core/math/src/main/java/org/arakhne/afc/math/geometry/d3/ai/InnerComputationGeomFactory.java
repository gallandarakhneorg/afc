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
package org.arakhne.afc.math.geometry.d3.ai;

import org.arakhne.afc.math.geometry.d2.GeomFactory2D;
import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.Vector2D;

/** Factory of immutable geometrical primitives.
 * 
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
final class InnerComputationGeomFactory implements GeomFactory2D<InnerComputationVector3ai, InnerComputationPoint3ai> {

	/** Singleton of the factory.
	 */
	public static final InnerComputationGeomFactory SINGLETON = new InnerComputationGeomFactory();
	
	private InnerComputationGeomFactory() {
		//
	}

	@Override
	public InnerComputationPoint3ai convertToPoint(Point2D<?, ?> p) {
		if (p instanceof InnerComputationPoint3ai) {
			return (InnerComputationPoint3ai) p;
		}
		int x, y;
		if (p == null) {
			x = 0;
			y = 0;
		} else {
			x = p.ix();
			y = p.iy();
		}
		return new InnerComputationPoint3ai(x, y);
	}

	@Override
	public InnerComputationVector3ai convertToVector(Point2D<?, ?> p) {
		int x, y;
		if (p == null) {
			x = 0;
			y = 0;
		} else {
			x = p.ix();
			y = p.iy();
		}
		return new InnerComputationVector3ai(x, y);
	}

	@Override
	public InnerComputationPoint3ai convertToPoint(Vector2D<?, ?> v) {
		int x, y;
		if (v == null) {
			x = 0;
			y = 0;
		} else {
			x = v.ix();
			y = v.iy();
		}
		return new InnerComputationPoint3ai(x, y);
	}

	@Override
	public InnerComputationVector3ai convertToVector(Vector2D<?, ?> v) {
		if (v instanceof InnerComputationVector3ai) {
			return (InnerComputationVector3ai) v;
		}
		int x, y;
		if (v == null) {
			x = 0;
			y = 0;
		} else {
			x = v.ix();
			y = v.iy();
		}
		return new InnerComputationVector3ai(x, y);
	}

	@Override
	public InnerComputationPoint3ai newPoint() {
		return new InnerComputationPoint3ai(0, 0);
	}

	@Override
	public InnerComputationVector3ai newVector() {
		return new InnerComputationVector3ai(0, 0);
	}

	@Override
	public InnerComputationPoint3ai newPoint(double x, double y) {
		return new InnerComputationPoint3ai((int) Math.round(x), (int) Math.round(y));
	}

	@Override
	public InnerComputationVector3ai newVector(double x, double y) {
		return new InnerComputationVector3ai((int) Math.round(x), (int) Math.round(y));
	}

	@Override
	public InnerComputationPoint3ai newPoint(int x, int y) {
		return new InnerComputationPoint3ai(x, y);
	}

	@Override
	public InnerComputationVector3ai newVector(int x, int y) {
		return new InnerComputationVector3ai(x, y);
	}
	
}