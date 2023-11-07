/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2023 The original authors and other contributors.
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

package org.arakhne.afc.math.test.geometry.d2;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.Tuple2D;
import org.arakhne.afc.math.geometry.d2.UnmodifiablePoint2D;
import org.arakhne.afc.vmutil.json.JsonBuffer;

@SuppressWarnings("all")
public final class Point2DStub implements Point2D<Point2DStub, Vector2DStub> {

	private static final long serialVersionUID = -7864528630398049668L;

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
		bits = 31 * bits + Double.hashCode(this.x);
		bits = 31 * bits + Double.hashCode(this.y);
		int b = (int) bits;
		return b ^ (b >> 31);
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

	@Pure
	@Override
	public String toString() {
		final JsonBuffer objectDescription = new JsonBuffer();
		toJson(objectDescription);
        return objectDescription.toString();
	}

	@Override
	public void toJson(JsonBuffer buffer) {
		buffer.add("x", getX()); //$NON-NLS-1$
		buffer.add("y", getY()); //$NON-NLS-1$
	}

}
