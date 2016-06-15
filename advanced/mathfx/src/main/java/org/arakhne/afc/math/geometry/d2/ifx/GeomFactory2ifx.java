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

package org.arakhne.afc.math.geometry.d2.ifx;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;

import org.arakhne.afc.math.geometry.PathWindingRule;
import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.Vector2D;
import org.arakhne.afc.math.geometry.d2.ai.GeomFactory2ai;
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
public class GeomFactory2ifx implements GeomFactory2ai<PathElement2ifx, Point2ifx, Vector2ifx, Rectangle2ifx> {

	/** The singleton of the factory.
	 */
	public static final GeomFactory2ifx SINGLETON = new GeomFactory2ifx();

	@Override
	public Point2ifx convertToPoint(Point2D<?, ?> point) {
		assert point != null : AssertMessages.notNullParameter();
		try {
			return (Point2ifx) point;
		} catch (Throwable exception) {
			return new Point2ifx(point);
		}
	}

	@Override
	public Point2ifx convertToPoint(Vector2D<?, ?> vector) {
		assert vector != null : AssertMessages.notNullParameter();
		return new Point2ifx(vector.ix(), vector.iy());
	}

	@Override
	public Vector2ifx convertToVector(Point2D<?, ?> point) {
		assert point != null : AssertMessages.notNullParameter();
		return new Vector2ifx(point.ix(), point.iy());
	}

	@Override
	public Vector2ifx convertToVector(Vector2D<?, ?> vector) {
		assert vector != null : AssertMessages.notNullParameter();
		Vector2ifx vv;
		try {
			vv = (Vector2ifx) vector;
		} catch (Throwable exception) {
			vv = new Vector2ifx(vector.ix(), vector.iy());
		}
		return vv;
	}

	@Override
	public Point2ifx newPoint(int x, int y) {
		return new Point2ifx(x, y);
	}

	@Override
	public Point2ifx newPoint(double x, double y) {
		return new Point2ifx(x, y);
	}

	@Override
	public Point2ifx newPoint() {
		return new Point2ifx();
	}

	/** Create a point with properties.
	 *
	 * @param x the x property.
	 * @param y the y property.
	 * @return the vector.
	 */
	@SuppressWarnings("static-method")
	public Point2ifx newPoint(IntegerProperty x, IntegerProperty y) {
		return new Point2ifx(x, y);
	}

	@Override
	public Vector2ifx newVector(int x, int y) {
		return new Vector2ifx(x, y);
	}

	@Override
	public Vector2ifx newVector(double x, double y) {
		return new Vector2ifx(x, y);
	}

	@Override
	public Vector2ifx newVector() {
		return new Vector2ifx();
	}

	@Override
	public Path2ifx newPath(PathWindingRule rule) {
		assert rule != null : AssertMessages.notNullParameter();
		return new Path2ifx(rule);
	}

	@Override
	public Rectangle2ifx newBox() {
		return new Rectangle2ifx();
	}

	@Override
	public Rectangle2ifx newBox(int x, int y, int width, int height) {
		assert width >= 0 : AssertMessages.positiveOrZeroParameter(2);
		assert height >= 0 : AssertMessages.positiveOrZeroParameter(3);
		return new Rectangle2ifx(x, y, width, height);
	}

	@Override
	public PathElement2ifx newMovePathElement(int x, int y) {
		return new PathElement2ifx.MovePathElement2ifx(
				new SimpleIntegerProperty(x),
				new SimpleIntegerProperty(y));
	}

	@Override
	public PathElement2ifx newLinePathElement(int startX, int startY, int targetX, int targetY) {
		return new PathElement2ifx.LinePathElement2ifx(
				new SimpleIntegerProperty(startX),
				new SimpleIntegerProperty(startY),
				new SimpleIntegerProperty(targetX),
				new SimpleIntegerProperty(targetY));
	}

	@Override
	public PathElement2ifx newClosePathElement(int lastPointX, int lastPointY, int firstPointX,
			int firstPointY) {
		return new PathElement2ifx.ClosePathElement2ifx(
				new SimpleIntegerProperty(lastPointX),
				new SimpleIntegerProperty(lastPointY),
				new SimpleIntegerProperty(firstPointX),
				new SimpleIntegerProperty(firstPointY));
	}

	@Override
	public PathElement2ifx newCurvePathElement(int startX, int startY, int controlX, int controlY,
			int targetX, int targetY) {
		return new PathElement2ifx.QuadPathElement2ifx(
				new SimpleIntegerProperty(startX),
				new SimpleIntegerProperty(startY),
				new SimpleIntegerProperty(controlX),
				new SimpleIntegerProperty(controlY),
				new SimpleIntegerProperty(targetX),
				new SimpleIntegerProperty(targetY));
	}

	@Override
	public PathElement2ifx newCurvePathElement(int startX, int startY, int controlX1, int controlY1,
			int controlX2, int controlY2, int targetX, int targetY) {
		return new PathElement2ifx.CurvePathElement2ifx(
				new SimpleIntegerProperty(startX),
				new SimpleIntegerProperty(startY),
				new SimpleIntegerProperty(controlX1),
				new SimpleIntegerProperty(controlY1),
				new SimpleIntegerProperty(controlX2),
				new SimpleIntegerProperty(controlY2),
				new SimpleIntegerProperty(targetX),
				new SimpleIntegerProperty(targetY));
	}

	@Override
	@SuppressWarnings("checkstyle:parameternumber")
	public PathElement2ifx newArcPathElement(int startX, int startY, int targetX, int targetY, int radiusX, int radiusY,
			double xAxisRotation, boolean largeArcFlag, boolean sweepFlag) {
		return new PathElement2ifx.ArcPathElement2ifx(
				new SimpleIntegerProperty(startX),
				new SimpleIntegerProperty(startY),
				new SimpleIntegerProperty(targetX),
				new SimpleIntegerProperty(targetY),
				new SimpleIntegerProperty(radiusX),
				new SimpleIntegerProperty(radiusY),
				new SimpleDoubleProperty(xAxisRotation),
				new SimpleBooleanProperty(largeArcFlag),
				new SimpleBooleanProperty(sweepFlag));
	}

	@Override
	public Segment2ifx newSegment(int x1, int y1, int x2, int y2) {
		return new Segment2ifx(x1, y1, x2, y2);
	}

	@Override
	public MultiShape2ifx<?> newMultiShape() {
		return new MultiShape2ifx<>();
	}

}
