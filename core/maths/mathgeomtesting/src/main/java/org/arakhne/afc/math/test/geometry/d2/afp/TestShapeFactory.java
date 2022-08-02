/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2022 The original authors, and other authors.
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

package org.arakhne.afc.math.test.geometry.d2.afp;

import org.arakhne.afc.math.geometry.PathWindingRule;
import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.Vector2D;
import org.arakhne.afc.math.geometry.d2.afp.Circle2afp;
import org.arakhne.afc.math.geometry.d2.afp.Ellipse2afp;
import org.arakhne.afc.math.geometry.d2.afp.MultiShape2afp;
import org.arakhne.afc.math.geometry.d2.afp.OrientedRectangle2afp;
import org.arakhne.afc.math.geometry.d2.afp.Parallelogram2afp;
import org.arakhne.afc.math.geometry.d2.afp.Path2afp;
import org.arakhne.afc.math.geometry.d2.afp.Rectangle2afp;
import org.arakhne.afc.math.geometry.d2.afp.RoundRectangle2afp;
import org.arakhne.afc.math.geometry.d2.afp.Segment2afp;
import org.arakhne.afc.math.geometry.d2.afp.Triangle2afp;

@SuppressWarnings("all")
public interface TestShapeFactory<P extends Point2D<? super P, ? super V>, V extends Vector2D<? super V, ? super P>, B extends Rectangle2afp<?, ?, ?, P, V, B>> {
	
	Segment2afp<?, ?, ?, P, V, B> createSegment(double x1, double y1, double x2, double y2);
	
	B createRectangle(double x, double y, double width, double height);

	Ellipse2afp<?, ?, ?, P, V, B> createEllipse(double x, double y, double width, double height);

	RoundRectangle2afp<?, ?, ?, P, V, B> createRoundRectangle(double x, double y,
			double width, double height, double arcWidth, double arcHeight);

	OrientedRectangle2afp<?, ?, ?, P, V, B> createOrientedRectangle(
			double centerX, double centerY, double axis1X, double axis1Y, double extent1, double extent2);

	Parallelogram2afp<?, ?, ?, P, V, B> createParallelogram(
			double cx, double cy, double ux, double uy, double extent1, double vx, double vy, double extent2);

	Triangle2afp<?, ?, ?, P, V, B> createTriangle(
			double x1, double y1, double x2, double y2, double x3, double y3);

	Circle2afp<?, ?, ?, P, V, B> createCircle(double x, double y, double radius);
	
	P createPoint(double x, double y);

	Vector2D createVector(double x, double y);

	Path2afp<?, ?, ?, P, V, B> createPath(PathWindingRule rule);

	MultiShape2afp<?, ?, ?, ?, P, V, B> createMultiShape();

}