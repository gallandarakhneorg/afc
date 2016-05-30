/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2016 The original authors, and other authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.arakhne.afc.math.geometry.d2;

@SuppressWarnings("all")
public final class Vector2DStub implements UnmodifiableVector2D<Vector2DStub, Point2DStub> {

	private double x;
	
	private double y;
	
	/**
	 * @param x x coordinate.
	 * @param y y coordinate.
	 */
	public Vector2DStub(double x, double y) {
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
	public Vector2DStub clone() {
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
	public UnmodifiableVector2D<Vector2DStub, Point2DStub> toUnmodifiable() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Vector2DStub toOrthogonalVector() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Vector2DStub toUnitVector() {
		throw new UnsupportedOperationException();
	}

	@Override
	public GeomFactory<Vector2DStub, Point2DStub> getGeomFactory() {
		return new GeomFactoryStub();
	}
	
}
