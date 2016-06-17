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

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;

import org.arakhne.afc.math.geometry.PathWindingRule;
import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.Vector2D;
import org.arakhne.afc.math.geometry.d2.afp.GeomFactory2afp;
import org.arakhne.afc.math.geometry.d2.afp.Path2afp;
import org.arakhne.afc.vmutil.asserts.AssertMessages;

/** Factory of geometrical elements.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
@SuppressWarnings("checkstyle:classdataabstractioncoupling")
public class GeomFactory2dfx implements GeomFactory2afp<PathElement2dfx, Point2dfx, Vector2dfx, Rectangle2dfx> {

	/** The singleton of the factory.
	 */
	public static final GeomFactory2dfx SINGLETON = new GeomFactory2dfx();

	@Override
	public Point2dfx convertToPoint(Point2D<?, ?> pt) {
		assert pt != null : AssertMessages.notNullParameter();
		try {
			return (Point2dfx) pt;
		} catch (Throwable exception) {
			return new Point2dfx(pt);
		}
	}

	@Override
	public Point2dfx convertToPoint(Vector2D<?, ?> v) {
		assert v != null : AssertMessages.notNullParameter();
		Point2dfx pt;
		try {
			final Vector2dfx pp = (Vector2dfx) v;
			pt = new Point2dfx(pp.xProperty(), pp.yProperty());
		} catch (Throwable exception) {
			pt = new Point2dfx(v.getX(), v.getY());
		}
		return pt;
	}

	@Override
	public Vector2dfx convertToVector(Point2D<?, ?> pt) {
		assert pt != null : AssertMessages.notNullParameter();
		Vector2dfx v;
		try {
			final Point2dfx pp = (Point2dfx) pt;
			v = new Vector2dfx(pp.xProperty(), pp.yProperty());
		} catch (Throwable exception) {
			v = new Vector2dfx(pt.getX(), pt.getY());
		}
		return v;
	}

	@Override
	public Vector2dfx convertToVector(Vector2D<?, ?> v) {
		assert v != null : AssertMessages.notNullParameter();
		Vector2dfx vv;
		try {
			vv = (Vector2dfx) v;
		} catch (Throwable exception) {
			vv = new Vector2dfx(v.getX(), v.getY());
		}
		return vv;
	}

	@Override
	public Point2dfx newPoint(double x, double y) {
		return new Point2dfx(x, y);
	}

	@Override
	public Point2dfx newPoint(int x, int y) {
		return new Point2dfx(x, y);
	}

	/** Create a point with properties.
	 *
	 * @param x the x property.
	 * @param y the y property.
	 * @return the vector.
	 */
	@SuppressWarnings("static-method")
	public Point2dfx newPoint(DoubleProperty x, DoubleProperty y) {
		return new Point2dfx(x, y);
	}

	@Override
	public Point2dfx newPoint() {
		return new Point2dfx();
	}

	@Override
	public Vector2dfx newVector(double x, double y) {
		return new Vector2dfx(x, y);
	}

	@Override
	public Vector2dfx newVector(int x, int y) {
		return new Vector2dfx(x, y);
	}

	@Override
	public Vector2dfx newVector() {
		return new Vector2dfx();
	}

	@Override
	public Rectangle2dfx newBox() {
		return new Rectangle2dfx();
	}

	@Override
	public Rectangle2dfx newBox(double x, double y, double width, double height) {
		assert width >= 0. : AssertMessages.positiveOrZeroParameter(2);
		assert height >= 0. : AssertMessages.positiveOrZeroParameter(3);
		return new Rectangle2dfx(x,  y, width, height);
	}

	@Override
	public Path2afp<?, ?, PathElement2dfx, Point2dfx, Vector2dfx, Rectangle2dfx> newPath(PathWindingRule rule) {
		assert rule != null : AssertMessages.notNullParameter();
		return new Path2dfx(rule);
	}

	@Override
	public PathElement2dfx newMovePathElement(double x, double y) {
		return new PathElement2dfx.MovePathElement2dfx(
				new SimpleDoubleProperty(x),
				new SimpleDoubleProperty(y));
	}

	@Override
	public PathElement2dfx newLinePathElement(double startX, double startY, double targetX, double targetY) {
		return new PathElement2dfx.LinePathElement2dfx(
				new SimpleDoubleProperty(startX),
				new SimpleDoubleProperty(startY),
				new SimpleDoubleProperty(targetX),
				new SimpleDoubleProperty(targetY));
	}

	@Override
	public PathElement2dfx newClosePathElement(double lastPointX, double lastPointY, double firstPointX,
			double firstPointY) {
		return new PathElement2dfx.ClosePathElement2dfx(
				new SimpleDoubleProperty(lastPointX),
				new SimpleDoubleProperty(lastPointY),
				new SimpleDoubleProperty(firstPointX),
				new SimpleDoubleProperty(firstPointY));
	}

	@Override
	public PathElement2dfx newCurvePathElement(double startX, double startY, double controlX, double controlY,
			double targetX, double targetY) {
		return new PathElement2dfx.QuadPathElement2dfx(
				new SimpleDoubleProperty(startX),
				new SimpleDoubleProperty(startY),
				new SimpleDoubleProperty(controlX),
				new SimpleDoubleProperty(controlY),
				new SimpleDoubleProperty(targetX),
				new SimpleDoubleProperty(targetY));
	}

	@Override
	public PathElement2dfx newCurvePathElement(double startX, double startY, double controlX1, double controlY1,
			double controlX2, double controlY2, double targetX, double targetY) {
		return new PathElement2dfx.CurvePathElement2dfx(
				new SimpleDoubleProperty(startX),
				new SimpleDoubleProperty(startY),
				new SimpleDoubleProperty(controlX1),
				new SimpleDoubleProperty(controlY1),
				new SimpleDoubleProperty(controlX2),
				new SimpleDoubleProperty(controlY2),
				new SimpleDoubleProperty(targetX),
				new SimpleDoubleProperty(targetY));
	}

	@Override
	@SuppressWarnings("checkstyle:parameternumber")
	public PathElement2dfx newArcPathElement(double startX, double startY, double targetX, double targetY,
			double radiusX, double radiusY, double xAxisRotation, boolean largeArcFlag, boolean sweepFlag) {
		return new PathElement2dfx.ArcPathElement2dfx(
				new SimpleDoubleProperty(startX),
				new SimpleDoubleProperty(startY),
				new SimpleDoubleProperty(targetX),
				new SimpleDoubleProperty(targetY),
				new SimpleDoubleProperty(radiusX),
				new SimpleDoubleProperty(radiusY),
				new SimpleDoubleProperty(xAxisRotation),
				new SimpleBooleanProperty(largeArcFlag),
				new SimpleBooleanProperty(sweepFlag));
	}

	@Override
	public MultiShape2dfx<?> newMultiShape() {
		return new MultiShape2dfx<>();
	}

	@Override
	public Triangle2dfx newTriangle(double x1, double y1, double x2, double y2, double x3, double y3) {
		return new Triangle2dfx(x1, y1, x2, y2, x3, y3);
	}

	@Override
	public Segment2dfx newSegment(double x1, double y1, double x2, double y2) {
		return new Segment2dfx(x1, y1, x2, y2);
	}

}
