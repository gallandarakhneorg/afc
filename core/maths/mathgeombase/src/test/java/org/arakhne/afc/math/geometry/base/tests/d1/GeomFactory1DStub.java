/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2026 The original authors and other contributors.
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

package org.arakhne.afc.math.geometry.base.tests.d1;

import org.arakhne.afc.math.geometry.base.d1.GeomFactory1D;
import org.arakhne.afc.math.geometry.base.d1.Point1D;
import org.arakhne.afc.math.geometry.base.d1.Segment1D;
import org.arakhne.afc.math.geometry.base.d1.Vector1D;

@SuppressWarnings("all")
public class GeomFactory1DStub implements GeomFactory1D<Vector1DStub, Point1DStub> {

	@Override
	public double getSplineApproximationRatio() {
		return 0;
	}

	@Override
	public void setSplineApproximationRatio(Double distance) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Point1DStub convertToPoint(Point1D<?, ?, ?> pts) {
		return new Point1DStub(pts.getX(), pts.getY(), (Segment1DStub) pts.getSegment());
	}

	@Override
	public Point1DStub convertToPoint(Vector1D<?, ?, ?> pt) {
		return new Point1DStub(pt.getX(), pt.getY(), (Segment1DStub) pt.getSegment());
	}

	@Override
	public Vector1DStub convertToVector(Point1D<?, ?, ?> pt) {
		return new Vector1DStub(pt.getX(), pt.getY(), (Segment1DStub) pt.getSegment());
	}

	@Override
	public Vector1DStub convertToVector(Vector1D<?, ?, ?> vector) {
		return new Vector1DStub(vector.getX(), vector.getY(), (Segment1DStub) vector.getSegment());
	}

	@Override
	public Point1DStub newPoint(Segment1D<?, ?> segment) {
		return new Point1DStub((Segment1DStub) segment);
	}

	@Override
	public Point1DStub newPoint(Segment1D<?, ?> segment, double x, double y) {
		return new Point1DStub(x, y, (Segment1DStub) segment);
	}

	@Override
	public Point1DStub newPoint(Segment1D<?, ?> segment, int x, int y) {
		return new Point1DStub(x, y, (Segment1DStub) segment);
	}

	@Override
	public Vector1DStub newVector(Segment1D<?, ?> segment) {
		return new Vector1DStub((Segment1DStub) segment);
	}

	@Override
	public Vector1DStub newVector(Segment1D<?, ?> segment, double x, double y) {
		return new Vector1DStub(x, y, (Segment1DStub) segment);
	}

	@Override
	public Vector1DStub newVector(Segment1D<?, ?> segment, int x, int y) {
		return new Vector1DStub(x, y, (Segment1DStub) segment);
	}

}