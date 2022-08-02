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

package org.arakhne.afc.math.geometry.d2.i;

import org.arakhne.afc.math.geometry.PathWindingRule;
import org.arakhne.afc.math.geometry.d2.AbstractGeomFactory2D;
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
public class GeomFactory2i extends AbstractGeomFactory2D<Vector2i, Point2i>
		implements GeomFactory2ai<PathElement2i, Point2i, Vector2i, Rectangle2i> {

	/** The singleton of the factory.
	 */
	public static final GeomFactory2i SINGLETON = new GeomFactory2i();

	@Override
	public Point2i convertToPoint(Point2D<?, ?> point) {
		assert point != null : AssertMessages.notNullParameter();
		try {
			return (Point2i) point;
		} catch (Throwable exception) {
			return new Point2i(point);
		}
	}

	@Override
	public Point2i convertToPoint(Vector2D<?, ?> vector) {
		assert vector != null : AssertMessages.notNullParameter();
		return new Point2i(vector.ix(), vector.iy());
	}

	@Override
	public Vector2i convertToVector(Point2D<?, ?> point) {
		assert point != null : AssertMessages.notNullParameter();
		return new Vector2i(point.ix(), point.iy());
	}

	@Override
	public Vector2i convertToVector(Vector2D<?, ?> vector) {
		assert vector != null : AssertMessages.notNullParameter();
		Vector2i vv;
		try {
			vv = (Vector2i) vector;
		} catch (Throwable exception) {
			vv = new Vector2i(vector.ix(), vector.iy());
		}
		return vv;
	}

	@Override
	public Point2i newPoint(int x, int y) {
		return new Point2i(x, y);
	}

	@Override
	public Point2i newPoint(double x, double y) {
		return new Point2i(x, y);
	}

	@Override
	public Point2i newPoint() {
		return new Point2i();
	}

	@Override
	public Vector2i newVector(int x, int y) {
		return new Vector2i(x, y);
	}

	@Override
	public Vector2i newVector(double x, double y) {
		return new Vector2i(x, y);
	}

	@Override
	public Vector2i newVector() {
		return new Vector2i();
	}

	@Override
	public Path2i newPath(PathWindingRule rule) {
		assert rule != null : AssertMessages.notNullParameter();
		return new Path2i(rule);
	}

	@Override
	public Rectangle2i newBox() {
		return new Rectangle2i();
	}

	@Override
	public Rectangle2i newBox(int x, int y, int width, int height) {
		assert width >= 0 : AssertMessages.positiveOrZeroParameter(2);
		assert height >= 0 : AssertMessages.positiveOrZeroParameter(3);
		return new Rectangle2i(x, y, width, height);
	}

	@Override
	public PathElement2i newMovePathElement(int x, int y) {
		return new PathElement2i.MovePathElement2i(x, y);
	}

	@Override
	public PathElement2i newLinePathElement(int startX, int startY, int targetX, int targetY) {
		return new PathElement2i.LinePathElement2i(startX, startY, targetX, targetY);
	}

	@Override
	public PathElement2i newClosePathElement(int lastPointX, int lastPointy, int firstPointX,
			int firstPointY) {
		return new PathElement2i.ClosePathElement2i(lastPointX, lastPointy, firstPointX, firstPointY);
	}

	@Override
	public PathElement2i newCurvePathElement(int startX, int startY, int controlX, int controlY,
			int targetX, int targetY) {
		return new PathElement2i.QuadPathElement2i(startX, startY, controlX, controlY, targetX, targetY);
	}

	@Override
	public PathElement2i newCurvePathElement(int startX, int startY, int controlX1, int controlY1,
			int controlX2, int controlY2, int targetX, int targetY) {
		return new PathElement2i.CurvePathElement2i(startX, startY, controlX1, controlY1,
				controlX2, controlY2, targetX, targetY);
	}

	@Override
	@SuppressWarnings("checkstyle:parameternumber")
	public PathElement2i newArcPathElement(int startX, int startY, int targetX, int targetY, int radiusX, int radiusY,
			double xAxisRotation, boolean largeArcFlag, boolean sweepFlag) {
		return new PathElement2i.ArcPathElement2i(startX, startY, targetX, targetY,
				radiusX, radiusY, xAxisRotation, largeArcFlag, sweepFlag);
	}

	@Override
	public Segment2i newSegment(int x1, int y1, int x2, int y2) {
		return new Segment2i(x1, y1, x2, y2);
	}

	@Override
	public MultiShape2i<?> newMultiShape() {
		return new MultiShape2i<>();
	}

}
