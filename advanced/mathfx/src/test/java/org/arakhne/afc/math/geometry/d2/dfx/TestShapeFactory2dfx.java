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

package org.arakhne.afc.math.geometry.d2.dfx;

import org.arakhne.afc.math.geometry.PathWindingRule;
import org.arakhne.afc.math.geometry.d2.OrientedPoint2D;
import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.Vector2D;
import org.arakhne.afc.math.geometry.d2.afp.Circle2afp;
import org.arakhne.afc.math.geometry.d2.afp.Ellipse2afp;
import org.arakhne.afc.math.geometry.d2.afp.MultiShape2afp;
import org.arakhne.afc.math.geometry.d2.afp.OrientedRectangle2afp;
import org.arakhne.afc.math.geometry.d2.afp.Parallelogram2afp;
import org.arakhne.afc.math.geometry.d2.afp.Path2afp;
import org.arakhne.afc.math.geometry.d2.afp.RoundRectangle2afp;
import org.arakhne.afc.math.geometry.d2.afp.Segment2afp;
import org.arakhne.afc.math.geometry.d2.afp.TestShapeFactory;
import org.arakhne.afc.math.geometry.d2.afp.Triangle2afp;

@SuppressWarnings("all")
public class TestShapeFactory2dfx implements TestShapeFactory<Point2dfx, Vector2dfx, Rectangle2dfx> {
	
	public static final TestShapeFactory2dfx SINGLETON = new TestShapeFactory2dfx();
	
	public Segment2afp<?, ?, ?, Point2dfx, Vector2dfx, Rectangle2dfx> createSegment(double x1, double y1, double x2, double y2) {
		return new Segment2dfx(x1, y1, x2, y2);
	}
	
	public Rectangle2dfx createRectangle(double x, double y, double width, double height) {
		assert (width >= 0) : "Width must be positive or zero";
		assert (height >= 0) : "Height must be positive or zero";
		return new Rectangle2dfx(x, y, width, height);
	}

	public Circle2afp<?, ?, ?, Point2dfx, Vector2dfx, Rectangle2dfx> createCircle(double x, double y, double radius) {
		assert (radius >= 0) : "Radius must be positive or zero";
		return new Circle2dfx(x, y, radius);
	}
	
	public Point2dfx createPoint(double x, double y) {
		return new Point2dfx(x, y);
	}

	public Vector2D createVector(double x, double y) {
		return new Vector2dfx(x, y);
	}

	public Path2afp<?, ?, ?, Point2dfx, Vector2dfx, Rectangle2dfx> createPath(PathWindingRule rule) {
		if (rule == null) {
			return new Path2dfx();
		}
		return new Path2dfx(rule);
	}

	@Override
	public RoundRectangle2afp<?, ?, ?, Point2dfx, Vector2dfx, Rectangle2dfx> createRoundRectangle(double x, double y, double width,
			double height, double arcWidth, double arcHeight) {
		assert (width >= 0) : "Width must be positive or zero";
		assert (height >= 0) : "Height must be positive or zero";
		assert (arcWidth >= 0) : "Arc width must be positive or zero";
		assert (arcHeight >= 0) : "Arc height must be positive or zero";
		return new RoundRectangle2dfx(x, y, width, height, arcWidth, arcHeight);
	}

	@Override
	public OrientedRectangle2afp<?, ?, ?, Point2dfx, Vector2dfx, Rectangle2dfx> createOrientedRectangle(
			double centerX, double centerY, double axis1X, double axis1Y,
			double extent1, double extent2) {
		assert (Vector2D.isUnitVector(axis1X, axis1Y)) : "Axis1 must be a unit vector";
		assert (extent1 >= 0) : "Extent1 must be positive or zero";
		assert (extent2 >= 0) : "Extent2 must be positive or zero";
		return new OrientedRectangle2dfx(centerX, centerY, axis1X, axis1Y, extent1, extent2);
	}

	@Override
	public Parallelogram2afp<?, ?, ?, Point2dfx, Vector2dfx, Rectangle2dfx> createParallelogram(
			double cx, double cy, double ux, double uy, double extent1, double vx, double vy, double extent2) {
		assert (Vector2D.isUnitVector(ux, uy)) : "Axis1 must be a unit vector";
		assert (Vector2D.isUnitVector(vx, vy)) : "Axis2 must be a unit vector";
		assert (extent1 >= 0) : "Extent1 must be positive or zero";
		assert (extent2 >= 0) : "Extent2 must be positive or zero";
		return new Parallelogram2dfx(cx, cy, ux, uy, extent1, vx, vy, extent2);
	}

	@Override
	public Ellipse2afp<?, ?, ?, Point2dfx, Vector2dfx, Rectangle2dfx> createEllipse(double x, double y, double width, double height) {
		assert (width >= 0) : "Width must be positive or zero";
		assert (height >= 0) : "Height must be positive or zero";
		return new Ellipse2dfx(x, y, width, height);
	}

	@Override
	public Triangle2afp<?, ?, ?, Point2dfx, Vector2dfx, Rectangle2dfx> createTriangle(double x1, double y1, double x2, double y2, double x3,
			double y3) {
		return new Triangle2dfx(x1, y1, x2, y2, x3, y3);
	}

	@Override
	public MultiShape2afp<?, ?, ?, ?, Point2dfx, Vector2dfx, Rectangle2dfx> createMultiShape() {
		return new MultiShape2dfx();
	}

    @Override
    public OrientedPoint2dfx createOrientedPoint(double x, double y, double length, double dirX, double dirY) {
        return new OrientedPoint2dfx(x, y, length, dirX, dirY);
    }
	
}
