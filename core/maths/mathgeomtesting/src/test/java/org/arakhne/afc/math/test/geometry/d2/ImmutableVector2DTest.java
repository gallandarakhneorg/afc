/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2020 The original authors, and other authors.
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

import org.arakhne.afc.math.geometry.d2.ImmutablePoint2D;
import org.arakhne.afc.math.geometry.d2.ImmutableVector2D;
import org.arakhne.afc.math.geometry.d2.Vector2D;
import org.arakhne.afc.math.test.geometry.d2.AbstractUnmodifiableVector2DTest;

@SuppressWarnings("all")
public class ImmutableVector2DTest extends AbstractUnmodifiableVector2DTest<ImmutableVector2D, ImmutablePoint2D> {
	
	@Override
	public ImmutableVector2D createVector(final double tx, final double ty) {
		return new ImmutableVector2D(tx, ty);
	}

	@Override
	public ImmutablePoint2D createPoint(final double tx, final double ty) {
		return new ImmutablePoint2D(tx,  ty);
	}

	@Override
	public Vector2D createTuple(double x, double y) {
		return new ImmutableVector2D(x, y);
	}

	@Override
	public boolean isIntCoordinates() {
		return false;
	}
	
}
