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

package org.arakhne.afc.math.geometry.d3;

@SuppressWarnings("all")
public class ImmutablePoint3DTest extends AbstractUnmodifiablePoint3DTest<ImmutablePoint3D, ImmutableVector3D> {
	
	@Override
	public ImmutableVector3D createVector(final double tx, final double ty, final double tz) {
		return new ImmutableVector3D(tx, ty, tz);
	}

	@Override
	public ImmutablePoint3D createPoint(final double tx, final double ty, final double tz) {
		return new ImmutablePoint3D(tx, ty, tz);
	}
	
	@Override
	public Point3D createTuple(double x, double y, double z) {
		return new ImmutablePoint3D(x, y, z);
	}

	@Override
	public boolean isIntCoordinates() {
		return false;
	}
	
	@Override
	public void operator_upToShape3D() {
		//
	}
	
	@Override
	public void operator_andShape3D() {
		//
	}

}
