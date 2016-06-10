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

package org.arakhne.afc.math.geometry.d2.i;

import static org.arakhne.afc.testtools.XbaseInlineTestUtil.assertInlineParameterUsage;

import org.junit.Test;

import org.arakhne.afc.math.geometry.d2.AbstractVector2DTest;

@SuppressWarnings("all")
public class Vector2iTest extends AbstractVector2DTest<Vector2i, Point2i, Vector2i> {

	@Override
	public boolean isIntCoordinates() {
		return true;
	}
	
	@Override
	public Vector2i createTuple(double x, double y) {
		return new Vector2i(x, y);
	}
	
	@Override
	public Vector2i createVector(double x, double y) {
		return new Vector2i(x, y);
	}

	@Override
	public Point2i createPoint(double x, double y) {
		return new Point2i(x, y);
	}

    @Test
    public void staticToOrientationVector() {
        assertFpVectorEquals(1, 0, Vector2i.toOrientationVector(0));
        assertFpVectorEquals(-1, 0, Vector2i.toOrientationVector(Math.PI));
        assertFpVectorEquals(0, 1, Vector2i.toOrientationVector(Math.PI/2));
        assertFpVectorEquals(0, -1, Vector2i.toOrientationVector(-Math.PI/2));

        assertInlineParameterUsage(Vector2i.class, "toOrientationVector", double.class);
    }

}
