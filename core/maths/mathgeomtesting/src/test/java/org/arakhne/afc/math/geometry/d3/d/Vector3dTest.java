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

package org.arakhne.afc.math.geometry.d3.d;

import org.junit.Ignore;
import org.junit.Test;

import org.arakhne.afc.math.geometry.d2.d.Vector2d;
import org.arakhne.afc.math.geometry.d3.AbstractVector3DTest;

@SuppressWarnings("all")
@Ignore("temporary")
public class Vector3dTest extends AbstractVector3DTest<Vector3d, Point3d, Vector3d> {

	@Override
	public boolean isIntCoordinates() {
		return false;
	}
	
	@Override
	public Vector3d createVector(double x, double y, double z) {
		return new Vector3d(x, y, z);
	}

	@Override
	public Point3d createPoint(double x, double y, double z) {
		return new Point3d(x, y, z);
	}

	@Override
	public Vector3d createTuple(double x, double y, double z) {
		return new Vector3d(x, y, z);
	}

	@Test
	public void staticToOrientationVector() {
		assertFpVectorEquals(1, 0, Vector2d.toOrientationVector(0));
		assertFpVectorEquals(-1, 0, Vector2d.toOrientationVector(Math.PI));
		assertFpVectorEquals(0, 1, Vector2d.toOrientationVector(Math.PI/2));
		assertFpVectorEquals(0, -1, Vector2d.toOrientationVector(-Math.PI/2));
	}

}
