/* 
 * $Id$
 * 
 * Copyright (c) 2006-10, Multiagent Team, Laboratoire Systemes et Transports, Universite de Technologie de Belfort-Montbeliard.
 * Copyright (C) 2012 Stephane GALLAND.
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 * This program is free software; you can redistribute it and/or modify
 */
package org.arakhne.afc.math.geometry.d2.fp;

import org.arakhne.afc.math.geometry.PathWindingRule;
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
import org.arakhne.afc.math.geometry.d2.afp.Shape2afp;
import org.arakhne.afc.math.geometry.d2.afp.TestShapeFactory;
import org.arakhne.afc.math.geometry.d2.afp.Triangle2afp;

@SuppressWarnings("all")
public class TestShapeFactory2fp implements TestShapeFactory<Point2fp, Rectangle2fp> {
	
	public static final TestShapeFactory2fp SINGLETON = new TestShapeFactory2fp();
	
	public Segment2afp<?, ?, ?, Point2fp, Rectangle2fp> createSegment(double x1, double y1, double x2, double y2) {
		return new Segment2fp(x1, y1, x2, y2);
	}
	
	public Rectangle2fp createRectangle(double x, double y, double width, double height) {
		assert (width >= 0) : "Width must be positive or zero";
		assert (height >= 0) : "Height must be positive or zero";
		return new Rectangle2fp(x, y, width, height);
	}

	public Ellipse2afp<?, ?, ?, Point2fp, Rectangle2fp> createEllipse(double x, double y, double width, double height) {
		assert (width >= 0) : "Width must be positive or zero";
		assert (height >= 0) : "Height must be positive or zero";
		return new Ellipse2fp(x, y, width, height);
	}

	public Circle2afp<?, ?, ?, Point2fp, Rectangle2fp> createCircle(double x, double y, double radius) {
		assert (radius >= 0) : "Radius must be positive or zero";
		return new Circle2fp(x, y, radius);
	}
	
	public Point2fp createPoint(double x, double y) {
		return new Point2fp(x, y);
	}

	public Vector2D createVector(double x, double y) {
		return new Vector2fp(x, y);
	}

	public Path2afp<?, ?, ?, Point2fp, Rectangle2fp> createPath(PathWindingRule rule) {
		if (rule == null) {
			return new Path2fp();
		}
		return new Path2fp(rule);
	}

	@Override
	public RoundRectangle2afp<?, ?, ?, Point2fp, Rectangle2fp> createRoundRectangle(double x, double y, double width,
			double height, double arcWidth, double arcHeight) {
		assert (width >= 0) : "Width must be positive or zero";
		assert (height >= 0) : "Height must be positive or zero";
		assert (arcWidth >= 0) : "Arc width must be positive or zero";
		assert (arcHeight >= 0) : "Arc height must be positive or zero";
		return new RoundRectangle2fp(x, y, width, height, arcWidth, arcHeight);
	}

	@Override
	public OrientedRectangle2afp<?, ?, ?, Point2fp, Rectangle2fp> createOrientedRectangle(
			double centerX, double centerY, double axis1X, double axis1Y,
			double extent1, double extent2) {
		assert (Vector2D.isUnitVector(axis1X, axis1Y)) : "Axis1 must be a unit vector";
		assert (extent1 >= 0) : "Extent1 must be positive or zero";
		assert (extent2 >= 0) : "Extent2 must be positive or zero";
		return new OrientedRectangle2fp(centerX, centerY, axis1X, axis1Y, extent1, extent2);
	}
	
	@Override
	public Parallelogram2afp<?, ?, ?, Point2fp, Rectangle2fp> createParallelogram(
			double cx, double cy, double ux, double uy, double extent1, double vx, double vy, double extent2) {
		assert (Vector2D.isUnitVector(ux, uy)) : "Axis1 must be a unit vector";
		assert (Vector2D.isUnitVector(vx, vy)) : "Axis2 must be a unit vector";
		assert (extent1 >= 0) : "Extent1 must be positive or zero";
		assert (extent2 >= 0) : "Extent2 must be positive or zero";
		return new Parallelogram2fp(cx, cy, ux, uy, extent1, vx, vy, extent2);
	}

	@Override
	public Triangle2afp<?, ?, ?, Point2fp, Rectangle2fp> createTriangle(double x1, double y1, double x2, double y2, double x3, double y3) {
		return new Triangle2fp(x1, y1, x2, y2, x3, y3);
	}

	@Override
	public MultiShape2afp<?, ?, ?, ?, Point2fp, Rectangle2fp> createMultiShape() {
		return new MultiShape2fp();
	}

}