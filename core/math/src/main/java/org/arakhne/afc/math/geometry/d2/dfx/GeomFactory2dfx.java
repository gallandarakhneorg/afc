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
package org.arakhne.afc.math.geometry.d2.dfx;

import org.arakhne.afc.math.geometry.PathWindingRule;
import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.Vector2D;
import org.arakhne.afc.math.geometry.d2.afp.GeomFactory2afp;
import org.arakhne.afc.math.geometry.d2.afp.Path2afp;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

/** Factory of geometrical elements.
 * 
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public class GeomFactory2dfx implements GeomFactory2afp<PathElement2dfx, Point2dfx, Vector2dfx, Rectangle2dfx> {

	/** The singleton of the factory.
	 */
	public static final GeomFactory2dfx SINGLETON = new GeomFactory2dfx();
	
	@Override
	public Point2dfx convertToPoint(Point2D<?, ?> p) {
		assert (p != null) : "Point must be not null"; //$NON-NLS-1$
		try {
			return (Point2dfx) p;
		} catch (Throwable exception) {
			return new Point2dfx(p);
		}
	}
	
	@Override
	public Vector2dfx convertToVector(Point2D<?, ?> p) {
		assert (p != null) : "Point must be not null"; //$NON-NLS-1$
		Vector2dfx v;
		try {
			Point2dfx pp = (Point2dfx) p;
			v = new Vector2dfx(pp.xProperty(), pp.yProperty());
		} catch (Throwable exception) {
			v = new Vector2dfx(p.getX(), p.getY());
		}
		return v;
	}

	@Override
	public Point2dfx convertToPoint(Vector2D<?, ?> v) {
		assert (v != null) : "Vector must be not null"; //$NON-NLS-1$
		Point2dfx p;
		try {
			Vector2dfx pp = (Vector2dfx) v;
			p = new Point2dfx(pp.xProperty(), pp.yProperty());
		} catch (Throwable exception) {
			p = new Point2dfx(v.getX(), v.getY());
		}
		return p;
	}
	
	@Override
	public Vector2dfx convertToVector(Vector2D<?, ?> v) {
		assert (v != null) : "Vector must be not null"; //$NON-NLS-1$
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
	public Vector2dfx newVector(double x, double y) {
		return new Vector2dfx(x, y);
	}

	@Override
	public Vector2dfx newVector(int x, int y) {
		return new Vector2dfx(x, y);
	}

	@Override
	public Point2dfx newPoint() {
		return new Point2dfx();
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
		assert (width >= 0.) : "Width must be positive or zero"; //$NON-NLS-1$
		assert (height >= 0.) : "Height must be positive or zero"; //$NON-NLS-1$
		return new Rectangle2dfx(x,  y, width, height);
	}

	@Override
	public Path2afp<?, ?, PathElement2dfx, Point2dfx, Vector2dfx, Rectangle2dfx> newPath(PathWindingRule rule) {
		assert (rule != null) : "Path winding rule must be not null"; //$NON-NLS-1$
		return new Path2dfx(rule);
	}

	@Override
	public PathElement2dfx newMovePathElement(double x, double y) {
		return new PathElement2dfx.MovePathElement2fx(
				new SimpleDoubleProperty(x),
				new SimpleDoubleProperty(y));
	}

	@Override
	public PathElement2dfx newLinePathElement(double startX, double startY, double targetX, double targetY) {
		return new PathElement2dfx.LinePathElement2fx(
				new SimpleDoubleProperty(startX),
				new SimpleDoubleProperty(startY),
				new SimpleDoubleProperty(targetX),
				new SimpleDoubleProperty(targetY));
	}

	@Override
	public PathElement2dfx newClosePathElement(double lastPointX, double lastPointY, double firstPointX,
			double firstPointY) {
		return new PathElement2dfx.ClosePathElement2fx(
				new SimpleDoubleProperty(lastPointX),
				new SimpleDoubleProperty(lastPointY),
				new SimpleDoubleProperty(firstPointX),
				new SimpleDoubleProperty(firstPointY));
	}

	@Override
	public PathElement2dfx newCurvePathElement(double startX, double startY, double controlX, double controlY,
			double targetX, double targetY) {
		return new PathElement2dfx.QuadPathElement2fx(
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
		return new PathElement2dfx.CurvePathElement2fx(
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