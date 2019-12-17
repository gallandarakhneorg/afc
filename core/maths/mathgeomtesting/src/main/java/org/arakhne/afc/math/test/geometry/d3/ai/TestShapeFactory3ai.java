/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2019 The original authors, and other authors.
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

package org.arakhne.afc.math.test.geometry.d3.ai;

import org.arakhne.afc.math.geometry.PathWindingRule;
import org.arakhne.afc.math.geometry.d3.Point3D;
import org.arakhne.afc.math.geometry.d3.Vector3D;
import org.arakhne.afc.math.geometry.d3.ai.MultiShape3ai;
import org.arakhne.afc.math.geometry.d3.ai.Path3ai;
import org.arakhne.afc.math.geometry.d3.ai.RectangularPrism3ai;
import org.arakhne.afc.math.geometry.d3.ai.Segment3ai;
import org.arakhne.afc.math.geometry.d3.ai.Sphere3ai;

@SuppressWarnings("all")
public interface TestShapeFactory3ai<P extends Point3D<? super P, ? super V>,
		V extends Vector3D<? super V, ? super P>,
		B extends RectangularPrism3ai<?, ?, ?, P, V, B>> {
	
	Segment3ai<?, ?, ?, P, V, B> createSegment(int x1, int y1, int z1, int x2, int y2, int z2);
	
	B createRectangularPrism(int x, int y, int z, int width, int height, int depth);

	Sphere3ai<?, ?, ?, P, V, B> createSphere(int x, int y, int z, int radius);
	
	Point3D createPoint(int x, int y, int z);

	Vector3D createVector(int x, int y, int z);

	Path3ai<?, ?, ?, P, V, B> createPath(PathWindingRule rile);
	
	MultiShape3ai<?, ?, ?, ?, P, V, B> createMultiShape();
	
}
