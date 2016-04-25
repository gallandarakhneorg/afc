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
package org.arakhne.afc.math.geometry.d2.afp;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.Arrays;

import org.arakhne.afc.math.AbstractMathTestCase;
import org.arakhne.afc.math.geometry.PathElementType;
import org.arakhne.afc.math.geometry.PathWindingRule;
import org.arakhne.afc.math.geometry.coordinatesystem.CoordinateSystem2DTestRule;
import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.Vector2D;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

@SuppressWarnings("all")
public interface TestShapeFactory<P extends Point2D, B extends Rectangle2afp<?, ?, ?, P, B>> {
	
	Segment2afp<?, ?, ?, P, B> createSegment(double x1, double y1, double x2, double y2);
	
	B createRectangle(double x, double y, double width, double height);

	Ellipse2afp<?, ?, ?, P, B> createEllipse(double x, double y, double width, double height);

	RoundRectangle2afp<?, ?, ?, P, B> createRoundRectangle(double x, double y,
			double width, double height, double arcWidth, double arcHeight);

	OrientedRectangle2afp<?, ?, ?, P, B> createOrientedRectangle(
			double centerX, double centerY, double axis1X, double axis1Y, double extent1, double extent2);

	Parallelogram2afp<?, ?, ?, P, B> createParallelogram(
			double cx, double cy, double ux, double uy, double extent1, double vx, double vy, double extent2);

	Triangle2afp<?, ?, ?, P, B> createTriangle(
			double x1, double y1, double x2, double y2, double x3, double y3);

	Circle2afp<?, ?, ?, P, B> createCircle(double x, double y, double radius);
	
	P createPoint(double x, double y);

	Vector2D createVector(double x, double y);

	Path2afp<?, ?, ?, P, B> createPath(PathWindingRule rule);

	MultiShape2afp<?, ?, ?, ?, P, B> createMultiShape();

}