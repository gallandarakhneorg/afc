/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2018 The original authors, and other authors.
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

import org.arakhne.afc.math.geometry.GeomConstants;

@SuppressWarnings("all")
public final class GeomFactoryStub implements GeomFactory2D<Vector2DStub, Point2DStub> {

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

	@Override
	public Point2DStub newPoint(double x, double y) {
		return new Point2DStub(x, y);
	}

	@Override
	public Vector2DStub newVector(double x, double y) {
		return new Vector2DStub(x, y);
	}

	@Override
	public Point2DStub newPoint(int x, int y) {
		return new Point2DStub(x, y);
	}

	@Override
	public Vector2DStub newVector(int x, int y) {
		return new Vector2DStub(x, y);
	}

	@Override
	public double getSplineApproximationRatio() {
		return GeomConstants.SPLINE_APPROXIMATION_RATIO;
	}

	@Override
	public void setSplineApproximationRatio(Double distance) {
		throw new UnsupportedOperationException();
	}
	
}