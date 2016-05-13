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
package org.arakhne.afc.math.geometry.d2;

/** Factory of immutable geometrical primitives.
 * 
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
final class ImmutableGeomFactory implements GeomFactory<ImmutableVector2D, ImmutablePoint2D> {

	/** Singleton of the factory.
	 */
	public static final ImmutableGeomFactory SINGLETON = new ImmutableGeomFactory();
	
	private ImmutableGeomFactory() {
		//
	}

	@Override
	public ImmutablePoint2D convertToPoint(Point2D<?, ?> p) {
		if (p instanceof ImmutablePoint2D) {
			return (ImmutablePoint2D) p;
		}
		double x, y;
		if (p == null) {
			x = 0;
			y = 0;
		} else {
			x = p.getX();
			y = p.getY();
		}
		return new ImmutablePoint2D(x, y);
	}

	@Override
	public ImmutableVector2D convertToVector(Point2D<?, ?> p) {
		double x, y;
		if (p == null) {
			x = 0;
			y = 0;
		} else {
			x = p.getX();
			y = p.getY();
		}
		return new ImmutableVector2D(x, y);
	}

	@Override
	public ImmutablePoint2D convertToPoint(Vector2D<?, ?> v) {
		double x, y;
		if (v == null) {
			x = 0;
			y = 0;
		} else {
			x = v.getX();
			y = v.getY();
		}
		return new ImmutablePoint2D(x, y);
	}

	@Override
	public ImmutableVector2D convertToVector(Vector2D<?, ?> v) {
		if (v instanceof ImmutableVector2D) {
			return (ImmutableVector2D) v;
		}
		double x, y;
		if (v == null) {
			x = 0;
			y = 0;
		} else {
			x = v.getX();
			y = v.getY();
		}
		return new ImmutableVector2D(x, y);
	}

	@Override
	public ImmutablePoint2D newPoint() {
		return new ImmutablePoint2D(0, 0);
	}

	@Override
	public ImmutableVector2D newVector() {
		return new ImmutableVector2D(0, 0);
	}

	@Override
	public ImmutablePoint2D newPoint(double x, double y) {
		return new ImmutablePoint2D(x, y);
	}

	@Override
	public ImmutableVector2D newVector(double x, double y) {
		return new ImmutableVector2D(x, y);
	}

	@Override
	public ImmutablePoint2D newPoint(int x, int y) {
		return new ImmutablePoint2D(x, y);
	}

	@Override
	public ImmutableVector2D newVector(int x, int y) {
		return new ImmutableVector2D(x, y);
	}
	
}