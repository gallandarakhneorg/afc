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

package org.arakhne.afc.math.geometry.d2.d;

import org.arakhne.afc.math.geometry.PathWindingRule;
import org.arakhne.afc.math.geometry.d2.Vector2D;
import org.arakhne.afc.math.geometry.d2.afp.Circle2afp;
import org.arakhne.afc.math.geometry.d2.afp.Ellipse2afp;
import org.arakhne.afc.math.geometry.d2.afp.MultiShape2afp;
import org.arakhne.afc.math.geometry.d2.afp.OrientedPoint2afp;
import org.arakhne.afc.math.geometry.d2.afp.OrientedRectangle2afp;
import org.arakhne.afc.math.geometry.d2.afp.Parallelogram2afp;
import org.arakhne.afc.math.geometry.d2.afp.Path2afp;
import org.arakhne.afc.math.geometry.d2.afp.RoundRectangle2afp;
import org.arakhne.afc.math.geometry.d2.afp.Segment2afp;
import org.arakhne.afc.math.geometry.d2.afp.TestShapeFactory;
import org.arakhne.afc.math.geometry.d2.afp.Triangle2afp;

@SuppressWarnings("all")
public class TestShapeFactory2d implements TestShapeFactory<Point2d, Vector2d, Rectangle2d> {
	
	public static final TestShapeFactory2d SINGLETON = new TestShapeFactory2d();
	
	@Override
	public Segment2afp<?, ?, ?, Point2d, Vector2d, Rectangle2d> createSegment(double x1, double y1, double x2, double y2) {
		return new Segment2d(x1, y1, x2, y2);
	}
	
	@Override
	public Rectangle2d createRectangle(double x, double y, double width, double height) {
		assert (width >= 0) : "Width must be positive or zero"; //$NON-NLS-1$
		assert (height >= 0) : "Height must be positive or zero"; //$NON-NLS-1$
		return new Rectangle2d(x, y, width, height);
	}

	@Override
	public Ellipse2afp<?, ?, ?, Point2d, Vector2d, Rectangle2d> createEllipse(double x, double y, double width, double height) {
		assert (width >= 0) : "Width must be positive or zero"; //$NON-NLS-1$
		assert (height >= 0) : "Height must be positive or zero"; //$NON-NLS-1$
		return new Ellipse2d(x, y, width, height);
	}

	@Override
	public Circle2afp<?, ?, ?, Point2d, Vector2d, Rectangle2d> createCircle(double x, double y, double radius) {
		assert (radius >= 0) : "Radius must be positive or zero"; //$NON-NLS-1$
		return new Circle2d(x, y, radius);
	}
	
	@Override
	public Point2d createPoint(double x, double y) {
		return new Point2d(x, y);
	}

	@Override
	public Vector2D createVector(double x, double y) {
		return new Vector2d(x, y);
	}

	@Override
	public Path2afp<?, ?, ?, Point2d, Vector2d, Rectangle2d> createPath(PathWindingRule rule) {
		if (rule == null) {
			return new Path2d();
		}
		return new Path2d(rule);
	}

	@Override
	public RoundRectangle2afp<?, ?, ?, Point2d, Vector2d, Rectangle2d> createRoundRectangle(double x, double y, double width,
			double height, double arcWidth, double arcHeight) {
		assert (width >= 0) : "Width must be positive or zero"; //$NON-NLS-1$
		assert (height >= 0) : "Height must be positive or zero"; //$NON-NLS-1$
		assert (arcWidth >= 0) : "Arc width must be positive or zero"; //$NON-NLS-1$
		assert (arcHeight >= 0) : "Arc height must be positive or zero"; //$NON-NLS-1$
		return new RoundRectangle2d(x, y, width, height, arcWidth, arcHeight);
	}

	@Override
	public OrientedRectangle2afp<?, ?, ?, Point2d, Vector2d, Rectangle2d> createOrientedRectangle(
			double centerX, double centerY, double axis1X, double axis1Y,
			double extent1, double extent2) {
		assert (Vector2D.isUnitVector(axis1X, axis1Y)) : "Axis1 must be a unit vector"; //$NON-NLS-1$
		assert (extent1 >= 0) : "Extent1 must be positive or zero"; //$NON-NLS-1$
		assert (extent2 >= 0) : "Extent2 must be positive or zero"; //$NON-NLS-1$
		return new OrientedRectangle2d(centerX, centerY, axis1X, axis1Y, extent1, extent2);
	}
	
	@Override
	public Parallelogram2afp<?, ?, ?, Point2d, Vector2d, Rectangle2d> createParallelogram(
			double cx, double cy, double ux, double uy, double extent1, double vx, double vy, double extent2) {
		assert (Vector2D.isUnitVector(ux, uy)) : "Axis1 must be a unit vector"; //$NON-NLS-1$
		assert (Vector2D.isUnitVector(vx, vy)) : "Axis2 must be a unit vector"; //$NON-NLS-1$
		assert (extent1 >= 0) : "Extent1 must be positive or zero"; //$NON-NLS-1$
		assert (extent2 >= 0) : "Extent2 must be positive or zero"; //$NON-NLS-1$
		return new Parallelogram2d(cx, cy, ux, uy, extent1, vx, vy, extent2);
	}

	@Override
	public Triangle2afp<?, ?, ?, Point2d, Vector2d, Rectangle2d> createTriangle(double x1, double y1, double x2, double y2, double x3, double y3) {
		return new Triangle2d(x1, y1, x2, y2, x3, y3);
	}

	@Override
	public MultiShape2afp<?, ?, ?, ?, Point2d, Vector2d, Rectangle2d> createMultiShape() {
		return new MultiShape2d();
	}

    @Override
    public OrientedPoint2afp createOrientedPoint(double x, double y, double length, double dirX, double dirY) {
        return new OrientedPoint2d(x, y, length, dirX, dirY);
    }

}