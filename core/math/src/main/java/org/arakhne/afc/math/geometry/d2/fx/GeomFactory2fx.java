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
package org.arakhne.afc.math.geometry.d2.fx;

import org.arakhne.afc.math.geometry.PathWindingRule;
import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.Vector2D;
import org.arakhne.afc.math.geometry.d2.afp.GeomFactory2afp;
import org.arakhne.afc.math.geometry.d2.afp.MultiShape2afp;
import org.arakhne.afc.math.geometry.d2.afp.Path2afp;

import javafx.beans.property.SimpleDoubleProperty;

/** Factory of geometrical elements.
 * 
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public class GeomFactory2fx implements GeomFactory2afp<PathElement2fx, Point2fx, Vector2fx, Rectangle2fx> {

	/** The singleton of the factory.
	 */
	public static final GeomFactory2fx SINGLETON = new GeomFactory2fx();
	
	@Override
	public Point2fx convertToPoint(Point2D<?, ?> p) {
		assert (p != null) : "Point must be not null"; //$NON-NLS-1$
		try {
			return (Point2fx) p;
		} catch (Throwable exception) {
			return new Point2fx(p);
		}
	}
	
	@Override
	public Vector2fx convertToVector(Point2D<?, ?> p) {
		assert (p != null) : "Point must be not null"; //$NON-NLS-1$
		Vector2fx v;
		try {
			Point2fx pp = (Point2fx) p;
			v = new Vector2fx(pp.xProperty(), pp.yProperty());
		} catch (Throwable exception) {
			v = new Vector2fx(p.getX(), p.getY());
		}
		return v;
	}

	@Override
	public Point2fx convertToPoint(Vector2D<?, ?> v) {
		assert (v != null) : "Vector must be not null"; //$NON-NLS-1$
		Point2fx p;
		try {
			Vector2fx pp = (Vector2fx) v;
			p = new Point2fx(pp.xProperty(), pp.yProperty());
		} catch (Throwable exception) {
			p = new Point2fx(v.getX(), v.getY());
		}
		return p;
	}
	
	@Override
	public Vector2fx convertToVector(Vector2D<?, ?> v) {
		assert (v != null) : "Vector must be not null"; //$NON-NLS-1$
		Vector2fx vv;
		try {
			vv = (Vector2fx) v;
		} catch (Throwable exception) {
			vv = new Vector2fx(v.getX(), v.getY());
		}
		return vv;
	}

	@Override
	public Point2fx newPoint(double x, double y) {
		return new Point2fx(x, y);
	}

	@Override
	public Vector2fx newVector(double x, double y) {
		return new Vector2fx(x, y);
	}

	@Override
	public Point2fx newPoint() {
		return new Point2fx();
	}

	@Override
	public Vector2fx newVector() {
		return new Vector2fx();
	}

	@Override
	public Rectangle2fx newBox() {
		return new Rectangle2fx();
	}
	
	@Override
	public Rectangle2fx newBox(double x, double y, double width, double height) {
		assert (width >= 0.) : "Width must be positive or zero"; //$NON-NLS-1$
		assert (height >= 0.) : "Height must be positive or zero"; //$NON-NLS-1$
		return new Rectangle2fx(x,  y, width, height);
	}

	@Override
	public Path2afp<?, ?, PathElement2fx, Point2fx, Vector2fx, Rectangle2fx> newPath(PathWindingRule rule) {
		assert (rule != null) : "Path winding rule must be not null"; //$NON-NLS-1$
		return new Path2fx(rule);
	}

	@Override
	public PathElement2fx newMovePathElement(double x, double y) {
		return new PathElement2fx.MovePathElement2fx(
				new SimpleDoubleProperty(x),
				new SimpleDoubleProperty(y));
	}

	@Override
	public PathElement2fx newLinePathElement(double startX, double startY, double targetX, double targetY) {
		return new PathElement2fx.LinePathElement2fx(
				new SimpleDoubleProperty(startX),
				new SimpleDoubleProperty(startY),
				new SimpleDoubleProperty(targetX),
				new SimpleDoubleProperty(targetY));
	}

	@Override
	public PathElement2fx newClosePathElement(double lastPointX, double lastPointY, double firstPointX,
			double firstPointY) {
		return new PathElement2fx.ClosePathElement2fx(
				new SimpleDoubleProperty(lastPointX),
				new SimpleDoubleProperty(lastPointY),
				new SimpleDoubleProperty(firstPointX),
				new SimpleDoubleProperty(firstPointY));
	}

	@Override
	public PathElement2fx newCurvePathElement(double startX, double startY, double controlX, double controlY,
			double targetX, double targetY) {
		return new PathElement2fx.QuadPathElement2fx(
				new SimpleDoubleProperty(startX),
				new SimpleDoubleProperty(startY),
				new SimpleDoubleProperty(controlX),
				new SimpleDoubleProperty(controlY),
				new SimpleDoubleProperty(targetX),
				new SimpleDoubleProperty(targetY));
	}

	@Override
	public PathElement2fx newCurvePathElement(double startX, double startY, double controlX1, double controlY1,
			double controlX2, double controlY2, double targetX, double targetY) {
		return new PathElement2fx.CurvePathElement2fx(
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
	public MultiShape2afp<?, ?, ?, PathElement2fx, Point2fx, Vector2fx, Rectangle2fx> newMultiShape() {
		return new MultiShape2fx<>();
	}

}