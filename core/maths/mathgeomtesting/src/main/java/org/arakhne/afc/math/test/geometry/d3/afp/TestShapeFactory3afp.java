/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2023 The original authors and other contributors.
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

package org.arakhne.afc.math.test.geometry.d3.afp;

import org.arakhne.afc.math.geometry.PathWindingRule;
import org.arakhne.afc.math.geometry.d3.Point3D;
import org.arakhne.afc.math.geometry.d3.Quaternion;
import org.arakhne.afc.math.geometry.d3.Vector3D;
import org.arakhne.afc.math.geometry.d3.afp.MultiShape3afp;
import org.arakhne.afc.math.geometry.d3.afp.Path3afp;
import org.arakhne.afc.math.geometry.d3.afp.PathElement3afp;
import org.arakhne.afc.math.geometry.d3.afp.AlignedBox3afp;
import org.arakhne.afc.math.geometry.d3.afp.Segment3afp;
import org.arakhne.afc.math.geometry.d3.afp.Sphere3afp;

@SuppressWarnings("all")
public interface TestShapeFactory3afp<P extends Point3D<? super P, ? super V, ? super Q>,
		V extends Vector3D<? super V, ? super P, ? super Q>,
		Q extends Quaternion<? super P, ? super V, ? super Q>,
		B extends AlignedBox3afp<?, ?, ?, P, V, Q, B>> {

    Segment3afp<?, ?, ?, P, V, Q, B> createSegment(double x1, double y1, double z1, double x2, double y2, double z2);

    B createAlignedBox(double x, double y, double z, double width, double height, double depth);

    Sphere3afp<?, ?, ?, P, V, Q, B> createSphere(double x, double y, double z, double radius);

    P createPoint(double x, double y, double z);

    V createVector(double x, double y, double z);

    Path3afp<?, ?, ?, P, V, Q, B> createPath(PathWindingRule rule);

    MultiShape3afp<?, ?, ?, ?, P, V, Q, B> createMultiShape();
}
