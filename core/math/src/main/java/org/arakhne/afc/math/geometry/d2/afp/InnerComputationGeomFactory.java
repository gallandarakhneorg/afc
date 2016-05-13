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
package org.arakhne.afc.math.geometry.d2.afp;

import org.arakhne.afc.math.geometry.d2.GeomFactory;
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
final class InnerComputationGeomFactory implements GeomFactory<InnerComputationVector2afp, InnerComputationPoint2afp> {

	/** Singleton of the factory.
	 */
	public static final InnerComputationGeomFactory SINGLETON = new InnerComputationGeomFactory();
	
	private InnerComputationGeomFactory() {
		//
	}

	@Override
	public InnerComputationPoint2afp convertToPoint(Point2D<?, ?> p) {
		if (p instanceof InnerComputationPoint2afp) {
			return (InnerComputationPoint2afp) p;
		}
		double x, y;
		if (p == null) {
			x = 0;
			y = 0;
		} else {
			x = p.getX();
			y = p.getY();
		}
		return new InnerComputationPoint2afp(x, y);
	}

	@Override
	public InnerComputationVector2afp convertToVector(Point2D<?, ?> p) {
		double x, y;
		if (p == null) {
			x = 0;
			y = 0;
		} else {
			x = p.getX();
			y = p.getY();
		}
		return new InnerComputationVector2afp(x, y);
	}

	@Override
	public InnerComputationPoint2afp convertToPoint(Vector2D<?, ?> v) {
		double x, y;
		if (v == null) {
			x = 0;
			y = 0;
		} else {
			x = v.getX();
			y = v.getY();
		}
		return new InnerComputationPoint2afp(x, y);
	}

	@Override
	public InnerComputationVector2afp convertToVector(Vector2D<?, ?> v) {
		if (v instanceof InnerComputationVector2afp) {
			return (InnerComputationVector2afp) v;
		}
		double x, y;
		if (v == null) {
			x = 0;
			y = 0;
		} else {
			x = v.getX();
			y = v.getY();
		}
		return new InnerComputationVector2afp(x, y);
	}

	@Override
	public InnerComputationPoint2afp newPoint() {
		return new InnerComputationPoint2afp(0, 0);
	}

	@Override
	public InnerComputationVector2afp newVector() {
		return new InnerComputationVector2afp(0, 0);
	}

	@Override
	public InnerComputationPoint2afp newPoint(double x, double y) {
		return new InnerComputationPoint2afp(x, y);
	}

	@Override
	public InnerComputationVector2afp newVector(double x, double y) {
		return new InnerComputationVector2afp(x, y);
	}

	@Override
	public InnerComputationPoint2afp newPoint(int x, int y) {
		return new InnerComputationPoint2afp(x, y);
	}

	@Override
	public InnerComputationVector2afp newVector(int x, int y) {
		return new InnerComputationVector2afp(x, y);
	}
	
	
}