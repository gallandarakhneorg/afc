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
import org.arakhne.afc.math.geometry.base.d1.ImmutableVector1D;
import org.arakhne.afc.math.geometry.base.d1.UnmodifiableVector1D;
import org.arakhne.afc.math.geometry.base.d1.Vector1D;
import org.arakhne.afc.vmutil.json.JsonBuffer;

@SuppressWarnings("all")
public class Vector1DStub implements Vector1D<Vector1DStub, Point1DStub, Segment1DStub> {

	private double x;
	
	private double y;
	
	private Segment1DStub segment;

	public Vector1DStub(Segment1DStub segment) {
		this.x = 0.;
		this.y = 0.;
		this.segment = segment;
	}
	
	public Vector1DStub(double x, double y, Segment1DStub segment) {
		this.x = x;
		this.y = y;
		this.segment = segment;
	}
	
	@Override
	public Vector1DStub clone() {
		try {
			return (Vector1DStub) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new Error(e);
		}
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
	public void toJson(JsonBuffer buffer) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Segment1DStub getSegment() {
		return this.segment;
	}

	@Override
	public void set(Segment1DStub segment, double curviline, double shift) {
		this.segment = segment;
		this.x = curviline;
		this.y = shift;
	}
	
	@Override
	public UnmodifiableVector1D<?, ?, Segment1DStub> toUnmodifiable() {
		return new ImmutableVector1D<Segment1DStub>(this.segment, this.x, this.y);
	}

	@Override
	public GeomFactory1D<Vector1DStub, Point1DStub> getGeomFactory() {
		return new GeomFactory1DStub();
	}

}