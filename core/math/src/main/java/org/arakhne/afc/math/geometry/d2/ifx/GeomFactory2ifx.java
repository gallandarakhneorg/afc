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
package org.arakhne.afc.math.geometry.d2.ifx;

import org.arakhne.afc.math.geometry.PathWindingRule;
import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.Vector2D;
import org.arakhne.afc.math.geometry.d2.ai.GeomFactory2ai;
import org.arakhne.afc.math.geometry.d2.ai.Path2ai;
import org.arakhne.afc.math.geometry.d2.ai.Segment2ai;

import javafx.beans.property.SimpleIntegerProperty;

/** Factory of geometrical elements.
 * 
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public class GeomFactory2ifx implements GeomFactory2ai<PathElement2ifx, Point2ifx, Rectangle2ifx> {

	/** The singleton of the factory.
	 */
	public static final GeomFactory2ifx SINGLETON = new GeomFactory2ifx();
	
	@Override
	public Point2ifx convertToPoint(Point2D point) {
		assert (point != null) : "Point must be not null"; //$NON-NLS-1$
		try {
			return (Point2ifx) point;
		} catch (Throwable exception) {
			return new Point2ifx(point);
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T extends Vector2D> T convertToVector(Point2D point) {
		assert (point != null) : "Point must be not null"; //$NON-NLS-1$
		return (T) new Vector2ifx(point.ix(), point.iy());
	}

	@Override
	public Point2ifx convertToPoint(Vector2D vector) {
		assert (vector != null) : "Vector must be not null"; //$NON-NLS-1$
		return new Point2ifx(vector.ix(), vector.iy());
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T extends Vector2D> T convertToVector(Vector2D vector) {
		assert (vector != null) : "Vector must be not null"; //$NON-NLS-1$
		Vector2ifx vv;
		try {
			vv = (Vector2ifx) vector;
		} catch (Throwable exception) {
			vv = new Vector2ifx(vector.ix(), vector.iy());
		}
		return (T) vv;
	}

	@Override
	public Point2ifx newPoint(int x, int y) {
		return new Point2ifx(x, y);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends Vector2D> T newVector(int x, int y) {
		return (T) new Vector2ifx(x, y);
	}

	@Override
	public Point2ifx newPoint() {
		return new Point2ifx();
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends Vector2D> T newVector() {
		return (T) new Vector2ifx();
	}

	@Override
	public Path2ai<?, ?, PathElement2ifx, Point2ifx, ?> newPath(PathWindingRule rule) {
		assert (rule != null) : "Path winding rule must be not null"; //$NON-NLS-1$
		return new Path2ifx(rule);
	}
	
	@Override
	public Rectangle2ifx newBox() {
		return new Rectangle2ifx();
	}
	
	@Override
	public Rectangle2ifx newBox(int x, int y, int width, int height) {
		assert (width >= 0) : "Width must be positive or zero"; //$NON-NLS-1$
		assert (height >= 0) : "Height must be positive or zero"; //$NON-NLS-1$
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
	public Segment2ai<?, ?, PathElement2ifx, Point2ifx, ?> newSegment(int x1, int y1, int x2, int y2) {
		return new Segment2ifx(x1, y1, x2, y2);
	}

}