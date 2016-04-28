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

@SuppressWarnings("all")
public final class GeomFactoryStub implements GeomFactory<Vector2DStub, Point2DStub> {

	public GeomFactoryStub() {
		//
	}

	@Override
	public Point2DStub convertToPoint(Point2D<?, ?> p) {
		if (p instanceof Point2DStub) {
			return (Point2DStub) p;
		}
		double x, y;
		if (p == null) {
			x = 0;
			y = 0;
		} else {
			x = p.getX();
			y = p.getY();
		}
		return new Point2DStub(x, y);
	}

	@Override
	public Vector2DStub convertToVector(Point2D<?, ?> p) {
		double x, y;
		if (p == null) {
			x = 0;
			y = 0;
		} else {
			x = p.getX();
			y = p.getY();
		}
		return new Vector2DStub(x, y);
	}

	@Override
	public Point2DStub convertToPoint(Vector2D<?, ?> v) {
		double x, y;
		if (v == null) {
			x = 0;
			y = 0;
		} else {
			x = v.getX();
			y = v.getY();
		}
		return new Point2DStub(x, y);
	}

	@Override
	public Vector2DStub convertToVector(Vector2D<?, ?> v) {
		if (v instanceof Vector2DStub) {
			return (Vector2DStub) v;
		}
		double x, y;
		if (v == null) {
			x = 0;
			y = 0;
		} else {
			x = v.getX();
			y = v.getY();
		}
		return new Vector2DStub(x, y);
	}

	@Override
	public Point2DStub newPoint() {
		return new Point2DStub(0, 0);
	}

	@Override
	public Vector2DStub newVector() {
		return new Vector2DStub(0, 0);
	}
	
	
}