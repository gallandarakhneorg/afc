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
package org.arakhne.afc.math.geometry.d2.d;

import org.arakhne.afc.math.geometry.PathWindingRule;
import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.Vector2D;
import org.arakhne.afc.math.geometry.d2.afp.GeomFactory2afp;

/** Factory of geometrical elements.
 * 
 * @author $Author: sgalland$
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
	public Point2d convertToPoint(Point2D<?, ?> p) {
		assert (p != null) : "Point must be not null"; //$NON-NLS-1$
		try {
			return (Point2d) p;
		} catch (Throwable exception) {
			return new Point2d(p);
		}
	}
	
	@Override
	public Vector2d convertToVector(Point2D<?, ?> p) {
		assert (p != null) : "Point must be not null"; //$NON-NLS-1$
		return new Vector2d(p.getX(), p.getY());
	}

	@Override
	public Point2d convertToPoint(Vector2D<?, ?> v) {
		assert (v != null) : "Vector must be not null"; //$NON-NLS-1$
		return new Point2d(v.getX(), v.getY());
	}
	
	@Override
	public Vector2d convertToVector(Vector2D<?, ?> v) {
		assert (v != null) : "Vector must be not null"; //$NON-NLS-1$
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
	public Vector2d newVector(double x, double y) {
		return new Vector2d(x, y);
	}

	@Override
	public Point2d newPoint() {
		return new Point2d();
	}

	@Override
	public Vector2d newVector() {
		return new Vector2d();
	}

	@Override
	public Path2d newPath(PathWindingRule rule) {
		assert (rule != null) : "Path winding rule must be not null"; //$NON-NLS-1$
		return new Path2d(rule);
	}
	
	@Override
	public Rectangle2d newBox() {
		return new Rectangle2d();
	}

	@Override
	public Rectangle2d newBox(double x, double y, double width, double height) {
		assert (width >= 0.) : "Width must be positive or zero"; //$NON-NLS-1$
		assert (height >= 0.) : "Height must be positive or zero"; //$NON-NLS-1$
		return new Rectangle2d(x, y, width, height);
	}
	
	@Override
	public PathElement2d newMovePathElement(double x, double y) {
		return new PathElement2d.MovePathElement2fp(x, y);
	}

	@Override
	public PathElement2d newLinePathElement(double startX, double startY, double targetX, double targetY) {
		return new PathElement2d.LinePathElement2fp(startX, startY, targetX, targetY);
	}

	@Override
	public PathElement2d newClosePathElement(double lastPointX, double lastPointy, double firstPointX,
			double firstPointY) {
		return new PathElement2d.ClosePathElement2fp(lastPointX, lastPointy, firstPointX, firstPointY);
	}

	@Override
	public PathElement2d newCurvePathElement(double startX, double startY, double controlX, double controlY,
			double targetX, double targetY) {
		return new PathElement2d.QuadPathElement2fp(startX, startY, controlX, controlY, targetX, targetY);
	}

	@Override
	public PathElement2d newCurvePathElement(double startX, double startY, double controlX1, double controlY1,
			double controlX2, double controlY2, double targetX, double targetY) {
		return new PathElement2d.CurvePathElement2fp(startX, startY, controlX1, controlY1,
				controlX2, controlY2, targetX, targetY);
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
	public MultiShape2d<?> newMultiShape() {
		return new MultiShape2d<>();
	}

}