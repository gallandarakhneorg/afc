/* 
 * $Id$
 * 
 * Copyright (C) 2013 Christophe BOHRHAUER.
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
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
public final class Point2DStub implements Point2D<Point2DStub, Vector2DStub> {

	private double x;
	
	private double y;
	
	/**
	 * @param x x coordinate.
	 * @param y y coordinate.
	 */
	public Point2DStub(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Tuple2D) {
			Tuple2D v = (Tuple2D) obj;
			return v.getX() == getX() && v.getY() == getY();
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		long bits = 1;
		bits = 31 * bits + Double.doubleToLongBits(this.x);
		bits = 31 * bits + Double.doubleToLongBits(this.y);
		int b = (int) bits;
		return b ^ (b >> 32);
	}
	
	@Override
	public GeomFactoryStub getGeomFactory() {
		return new GeomFactoryStub();
	}

	@Override
	public Point2DStub clone() {
		throw new UnsupportedOperationException();
	}

	@Override
	public double getX() {
		return this.x;
	}

	@Override
	public int ix() {
		return (int) this.x;
	}

	@Override
	public void setX(int x) {
		this.x = x;
	}

	@Override
	public void setX(double x) {
		this.x = x;
	}

	@Override
	public double getY() {
		return this.y;
	}

	@Override
	public int iy() {
		return (int) this.y;
	}

	@Override
	public void setY(int y) {
		this.y = y;
	}

	@Override
	public void setY(double y) {
		this.y = y;
	}

	@Override
	public UnmodifiablePoint2D<Point2DStub, Vector2DStub> toUnmodifiable() {
		throw new UnsupportedOperationException();
	}

}
