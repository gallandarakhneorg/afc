/* 
 * $Id$
 * 
 * Copyright (C) 2010-2013 Stephane GALLAND.
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
package org.arakhne.afc.math.geometry.d2.i;

import org.arakhne.afc.math.geometry.PathWindingRule;
import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.Vector2D;
import org.arakhne.afc.math.geometry.d2.ai.GeomFactory2ai;
import org.arakhne.afc.math.geometry.d2.ai.Path2ai;
import org.arakhne.afc.math.geometry.d2.ai.Segment2ai;

/** Factory of geometrical elements.
 * 
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public class GeomFactory2i implements GeomFactory2ai<PathElement2i, Point2i, Rectangle2i> {

	/** The singleton of the factory.
	 */
	public static final GeomFactory2i SINGLETON = new GeomFactory2i();
	
	@Override
	public Point2i convertToPoint(Point2D point) {
		try {
			return (Point2i) point;
		} catch (Throwable exception) {
			return new Point2i(point);
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T extends Vector2D> T convertToVector(Point2D point) {
		return (T) new Vector2i(point.ix(), point.iy());
	}

	@Override
	public Point2i convertToPoint(Vector2D vector) {
		return new Point2i(vector.ix(), vector.iy());
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T extends Vector2D> T convertToVector(Vector2D vector) {
		Vector2i vv;
		try {
			vv = (Vector2i) vector;
		} catch (Throwable exception) {
			vv = new Vector2i(vector.ix(), vector.iy());
		}
		return (T) vv;
	}

	@Override
	public Point2i newPoint(int x, int y) {
		return new Point2i(x, y);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends Vector2D> T newVector(int x, int y) {
		return (T) new Vector2i(x, y);
	}

	@Override
	public Point2i newPoint() {
		return new Point2i();
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends Vector2D> T newVector() {
		return (T) new Vector2i();
	}

	@Override
	public Path2ai<?, ?, PathElement2i, Point2i, ?> newPath(PathWindingRule rule) {
		return new Path2i(rule);
	}
	
	@Override
	public Rectangle2i newBox() {
		return new Rectangle2i();
	}
	
	@Override
	public Rectangle2i newBox(int x, int y, int width, int height) {
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
	public Segment2ai<?, ?, PathElement2i, Point2i, ?> newSegment(int x1, int y1, int x2, int y2) {
		return new Segment2i(x1, y1, x2, y2);
	}

}