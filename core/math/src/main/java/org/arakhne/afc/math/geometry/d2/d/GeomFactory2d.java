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
import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.Vector2D;
import org.arakhne.afc.math.geometry.d2.afp.GeomFactory2afp;
import org.arakhne.afc.vmutil.asserts.AssertMessages;

/** Factory of geometrical elements.
 *
 * @author $Author: sgalland$
 * @author $Author: tpiotrow$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public class GeomFactory2d implements GeomFactory2afp<PathElement2d, Point2d, Vector2d, Rectangle2d> {

	/** The singleton of the factory.
	 */
	public static final GeomFactory2d SINGLETON = new GeomFactory2d();

	@Override
	public Point2d convertToPoint(Point2D<?, ?> pt) {
		assert pt != null : AssertMessages.notNullParameter();
		try {
			return (Point2d) pt;
		} catch (Throwable exception) {
			return new Point2d(pt);
		}
	}

	@Override
	public Point2d convertToPoint(Vector2D<?, ?> v) {
		assert v != null : AssertMessages.notNullParameter();
		return new Point2d(v.getX(), v.getY());
	}

	@Override
	public Vector2d convertToVector(Point2D<?, ?> pt) {
		assert pt != null : AssertMessages.notNullParameter();
		return new Vector2d(pt.getX(), pt.getY());
	}

	@Override
	public Vector2d convertToVector(Vector2D<?, ?> v) {
		assert v != null : AssertMessages.notNullParameter();
		Vector2d vv;
		try {
			vv = (Vector2d) v;
		} catch (Throwable exception) {
			vv = new Vector2d(v.getX(), v.getY());
		}
		return vv;
	}

	@Override
	public Point2d newPoint(double x, double y) {
		return new Point2d(x, y);
	}

	@Override
	public Point2d newPoint(int x, int y) {
		return new Point2d(x, y);
	}

	@Override
	public Point2d newPoint() {
		return new Point2d();
	}

	@Override
	public Vector2d newVector(double x, double y) {
		return new Vector2d(x, y);
	}

	@Override
	public Vector2d newVector(int x, int y) {
		return new Vector2d(x, y);
	}

	@Override
	public Vector2d newVector() {
		return new Vector2d();
	}

	@Override
	public Path2d newPath(PathWindingRule rule) {
		assert rule != null : AssertMessages.notNullParameter();
		return new Path2d(rule);
	}

	@Override
	public Rectangle2d newBox() {
		return new Rectangle2d();
	}

	@Override
	public Rectangle2d newBox(double x, double y, double width, double height) {
		assert width >= 0. : AssertMessages.positiveOrZeroParameter(2);
		assert height >= 0. : AssertMessages.positiveOrZeroParameter(3);
		return new Rectangle2d(x, y, width, height);
	}

	@Override
	public PathElement2d newMovePathElement(double x, double y) {
		return new PathElement2d.MovePathElement2d(x, y);
	}

	@Override
	public PathElement2d newLinePathElement(double startX, double startY, double targetX, double targetY) {
		return new PathElement2d.LinePathElement2d(startX, startY, targetX, targetY);
	}

	@Override
	public PathElement2d newClosePathElement(double lastPointX, double lastPointy, double firstPointX,
			double firstPointY) {
		return new PathElement2d.ClosePathElement2d(lastPointX, lastPointy, firstPointX, firstPointY);
	}

	@Override
	public PathElement2d newCurvePathElement(double startX, double startY, double controlX, double controlY,
			double targetX, double targetY) {
		return new PathElement2d.QuadPathElement2d(startX, startY, controlX, controlY, targetX, targetY);
	}

	@Override
	public PathElement2d newCurvePathElement(double startX, double startY, double controlX1, double controlY1,
			double controlX2, double controlY2, double targetX, double targetY) {
		return new PathElement2d.CurvePathElement2d(startX, startY, controlX1, controlY1,
				controlX2, controlY2, targetX, targetY);
	}

	@Override
	@SuppressWarnings("checkstyle:parameternumber")
	public PathElement2d newArcPathElement(double startX, double startY, double targetX, double targetY, double radiusX,
			double radiusY, double xAxisRotation, boolean largeArcFlag, boolean sweepFlag) {
		return new PathElement2d.ArcPathElement2d(startX, startY, targetX, targetY,
				radiusX, radiusY, xAxisRotation, largeArcFlag, sweepFlag);
	}

	@Override
	public Triangle2d newTriangle(double x1, double y1, double x2, double y2, double x3, double y3) {
		return new Triangle2d(x1, y1, x2, y2, x3, y3);
	}

	@Override
	public Segment2d newSegment(double x1, double y1, double x2, double y2) {
		return new Segment2d(x1, y1, x2, y2);
	}

	@Override
	public OrientedPoint2d newOrientedPoint(double x, double y) {
	    return new OrientedPoint2d(x, y);
	}

	@Override
	public OrientedPoint2d newOrientedPoint(double x, double y, double length) {
	    return new OrientedPoint2d(x, y, length);
	}

	@Override
	public OrientedPoint2d newOrientedPoint(double x, double y, double dirX, double dirY) {
	    return new OrientedPoint2d(x, y, dirX, dirY);
	}

	@Override
	public OrientedPoint2d newOrientedPoint(double x, double y, double length, double dirX, double dirY) {
	    return new OrientedPoint2d(x, y, length, dirX, dirY);
	}

	@Override
	public MultiShape2d<?> newMultiShape() {
		return new MultiShape2d<>();
	}

}
