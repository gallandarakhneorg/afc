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

package org.arakhne.afc.math.geometry.d3.tests.d;

import java.util.ArrayList;
import java.util.List;

import org.arakhne.afc.math.Unefficient;
import org.arakhne.afc.math.geometry.base.coordinatesystem.CoordinateSystem3D;
import org.arakhne.afc.math.geometry.base.d3.Shape3D;
import org.arakhne.afc.math.geometry.d3.afp.AlignedBox3afp;
import org.arakhne.afc.math.geometry.d3.afp.MultiShape3afp;
import org.arakhne.afc.math.geometry.d3.afp.Path3afp;
import org.arakhne.afc.math.geometry.d3.afp.PathIterator3afp;
import org.arakhne.afc.math.geometry.d3.afp.Segment3afp;
import org.arakhne.afc.math.geometry.d3.afp.Sphere3afp;
import org.arakhne.afc.math.geometry.d3.d.AlignedBox3d;
import org.arakhne.afc.math.geometry.d3.d.MultiShape3d;
import org.arakhne.afc.math.geometry.d3.d.Shape3d;
import org.arakhne.afc.math.geometry.d3.general.Shape3DType;
import org.arakhne.afc.math.geometry.d3.tests.afp.AbstractMultiShape3dTestCase;
import org.arakhne.afc.math.geometry.d3.tests.afp.TestShapeFactory3d;
import org.arakhne.afc.vmutil.asserts.AssertMessages;
import org.eclipse.xtext.xbase.lib.Pure;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@SuppressWarnings("all")
@DisplayName("MultiShape3d")
public class MultiShape3dTest extends AbstractMultiShape3dTestCase<MultiShape3d<Shape3d<?>>, Shape3d<?>, AlignedBox3d> {

	@Override
	protected TestShapeFactory3d createFactory() {
		return new BaseTestShapeFactory3d();
	}

	@Override
	@Test
	public void getClosestPointToShape3D(CoordinateSystem3D cs) {
		throw new UnsupportedOperationException();
	}

	@Override
	@Test
	public void getClosestPointToSphere3afp(CoordinateSystem3D cs) {
		throw new UnsupportedOperationException();
	}

	@Override
	@Test
	public void getClosestPointToAlignedBox3afp(CoordinateSystem3D cs) {
		throw new UnsupportedOperationException();
	}

	@Override
	@Test
	public void getClosestPointToSegment3afp(CoordinateSystem3D cs) {
		throw new UnsupportedOperationException();
	}

	@Override
	@Test
	public void getClosestPointToPath3afp(CoordinateSystem3D cs) {
		throw new UnsupportedOperationException();
	}

	@Override
	@Test
	public void getClosestPointToMultiShape3afp(CoordinateSystem3D cs) {
		throw new UnsupportedOperationException();
	}

	@Override
	@Test
	public void getDistanceShape3D(CoordinateSystem3D cs) {
		throw new UnsupportedOperationException();
	}

	@Override
	@Test
	public void getDistanceSquaredShape3D(CoordinateSystem3D cs) {
		throw new UnsupportedOperationException();
	}

	@Override
	@Test
	public void getDistanceSquaredSphere3afp(CoordinateSystem3D cs) {
		throw new UnsupportedOperationException();
	}

	@Override
	@Test
	public void getDistanceSquaredAlignedBox3afp(CoordinateSystem3D cs) {
		throw new UnsupportedOperationException();
	}

	@Override
	@Test
	public void getDistanceSquaredSegment3afp(CoordinateSystem3D cs) {
		throw new UnsupportedOperationException();
	}

	@Override
	@Test
	public void getDistanceSquaredPath3afp(CoordinateSystem3D cs) {
		throw new UnsupportedOperationException();
	}

	@Override
	@Test
	public void getDistanceSquaredMultiShape3afp(CoordinateSystem3D cs) {
		throw new UnsupportedOperationException();
	}

	@Override
	@Test
	public void intersectsMultiShape3afp(CoordinateSystem3D cs) {
		throw new UnsupportedOperationException();
	}

	@Override
	@Test
	public void getType_Class() {
		throw new UnsupportedOperationException();
	}

	@Override
	@Test
	public void getType() {
		throw new UnsupportedOperationException();
	}

	@Override
	@Test
	public void getDistanceSphere3afp(CoordinateSystem3D cs) {
		throw new UnsupportedOperationException();
	}

	@Override
	@Test
	public void getDistanceAlignedBox3afp(CoordinateSystem3D cs) {
		throw new UnsupportedOperationException();
	}

	@Override
	@Test
	public void getDistanceSegment3afp(CoordinateSystem3D cs) {
		throw new UnsupportedOperationException();
	}

	@Override
	@Test
	public void getDistancePath3afp(CoordinateSystem3D cs) {
		throw new UnsupportedOperationException();
	}

	@Override
	@Test
	public void getDistanceMultiShape3afp(CoordinateSystem3D cs) {
		throw new UnsupportedOperationException();
	}

}
