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

package org.arakhne.afc.math.geometry.d2.dfx;

import static org.arakhne.afc.testtools.XbaseInlineTestUtil.assertInlineParameterUsage;

import javafx.beans.property.ReadOnlyDoubleProperty;
import org.junit.Test;

import org.arakhne.afc.math.geometry.d2.AbstractVector2DTest;

@SuppressWarnings("all")
public class Vector2dfxTest extends AbstractVector2DTest<Vector2dfx, Point2dfx, Vector2dfx> {

	@Override
	public boolean isIntCoordinates() {
		return false;
	}
	
	@Override
	public Vector2dfx createTuple(double x, double y) {
		return new Vector2dfx(x, y);
	}
	
	@Override
	public Vector2dfx createVector(double x, double y) {
		return new Vector2dfx(x, y);
	}

	@Override
	public Point2dfx createPoint(double x, double y) {
		return new Point2dfx(x, y);
	}

	@Test
	public void staticToOrientationVector() {
		assertFpVectorEquals(1, 0, Vector2dfx.toOrientationVector(0));
		assertFpVectorEquals(-1, 0, Vector2dfx.toOrientationVector(Math.PI));
		assertFpVectorEquals(0, 1, Vector2dfx.toOrientationVector(Math.PI/2));
		assertFpVectorEquals(0, -1, Vector2dfx.toOrientationVector(-Math.PI/2));

		assertInlineParameterUsage(Vector2dfx.class, "toOrientationVector", double.class); //$NON-NLS-1$
	}

	@Test
	public void lengthProperty() {
		Vector2dfx vector = new Vector2dfx(1, 2);
		assertEpsilonEquals(2.23607, vector.getLength());
		
		ReadOnlyDoubleProperty property = vector.lengthProperty();
		assertNotNull(property);
		assertEpsilonEquals(2.23607, property.get());
		
		vector.set(4, -10);
		assertEpsilonEquals(10.77033, property.get());
	}

	@Test
	public void lengthSquaredProperty() {
		Vector2dfx vector = new Vector2dfx(1, 2);
		assertEpsilonEquals(5, vector.getLengthSquared());
		
		ReadOnlyDoubleProperty property = vector.lengthSquaredProperty();
		assertNotNull(property);
		assertEpsilonEquals(5, property.get());
		
		vector.set(4, -10);
		assertEpsilonEquals(116, property.get());
	}

}
