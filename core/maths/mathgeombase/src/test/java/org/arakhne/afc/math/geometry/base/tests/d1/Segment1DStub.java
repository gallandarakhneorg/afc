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

import org.arakhne.afc.math.geometry.base.d1.Segment1D;
import org.arakhne.afc.math.geometry.base.d2.InnerComputationPoint2D;
import org.arakhne.afc.math.geometry.base.d2.InnerComputationVector2D;
import org.arakhne.afc.math.geometry.base.d2.Point2D;
import org.arakhne.afc.math.geometry.base.d2.Vector2D;

@SuppressWarnings("all")
public class Segment1DStub implements Segment1D<InnerComputationPoint2D, InnerComputationVector2D> {

	private final double x1;
	
	private final double y1;
	
	private final double x2;
	
	private final double y2;

	private Segment1DStub firstConnected;
	
	private Segment1DStub lastConnected;

	public Segment1DStub() {
		this.x1 = 1.;
		this.y1 = 2.;
		this.x2 = 18.;
		this.y2 = 34.;
	}
	
	public Segment1DStub(double x1, double y1, double x2, double y2) {
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
	}

	@Override
	public double getLength() {
		final var a = this.x2 - this.x1;
		final var b = this.y2 - this.y1;
		return Math.sqrt(a * a + b * b);
	}

	@Override
	public InnerComputationPoint2D getFirstPoint() {
		return new InnerComputationPoint2D(this.x1, this.y1);
	}

	@Override
	public InnerComputationPoint2D getLastPoint() {
		return new InnerComputationPoint2D(this.x2, this.y2);
	}

	@Override
	public InnerComputationVector2D getTangentAt(double positionOnSegment) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void projectsOnPlane(double positionOnSegment, Point2D<?, ?> position, Vector2D<?, ?> tangent) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void projectsOnPlane(double positionOnSegment, double shiftDistance, Point2D<?, ?> position,
			Vector2D<?, ?> tangent) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isFirstPointConnectedTo(Segment1D<?, ?> otherSegment) {
		return this.firstConnected == otherSegment;
	}

	@Override
	public boolean isLastPointConnectedTo(Segment1D<?, ?> otherSegment) {
		return this.lastConnected == otherSegment;
	}

	void setFirstConnection(Segment1DStub segment) {
		this.firstConnected = segment;
	}

	void setLastConnection(Segment1DStub segment) {
		this.lastConnected = segment;
	}

}