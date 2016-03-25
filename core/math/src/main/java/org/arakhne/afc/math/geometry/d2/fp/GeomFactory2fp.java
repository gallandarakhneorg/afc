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
package org.arakhne.afc.math.geometry.d2.fp;

import org.arakhne.afc.math.geometry.PathWindingRule;
import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.Vector2D;
import org.arakhne.afc.math.geometry.d2.afp.GeomFactory2afp;
import org.arakhne.afc.math.geometry.d2.afp.Path2afp;

/** Factory of geometrical elements.
 * 
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public class GeomFactory2fp implements GeomFactory2afp<PathElement2fp, Point2fp, Rectangle2fp> {

	/** The singleton of the factory.
	 */
	public static final GeomFactory2fp SINGLETON = new GeomFactory2fp();
	
	@Override
	public Point2fp convertToPoint(Point2D p) {
		try {
			return (Point2fp) p;
		} catch (Throwable exception) {
			return new Point2fp(p);
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T extends Vector2D> T convertToVector(Point2D p) {
		return (T) new Vector2fp(p.getX(), p.getY());
	}

	@Override
	public Point2fp convertToPoint(Vector2D v) {
		return new Point2fp(v.getX(), v.getY());
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T extends Vector2D> T convertToVector(Vector2D v) {
		Vector2fp vv;
		try {
			vv = (Vector2fp) v;
		} catch (Throwable exception) {
			vv = new Vector2fp(v.getX(), v.getY());
		}
		return (T) vv;
	}

	@Override
	public Point2fp newPoint(double x, double y) {
		return new Point2fp(x, y);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends Vector2D> T newVector(double x, double y) {
		return (T) new Vector2fp(x, y);
	}

	@Override
	public Point2fp newPoint() {
		return new Point2fp();
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends Vector2D> T newVector() {
		return (T) new Vector2fp();
	}

	@Override
	public Path2afp<?, ?, PathElement2fp, Point2fp, ?> newPath(PathWindingRule rule) {
		return new Path2fp(rule);
	}
	
	@Override
	public Rectangle2fp newBox() {
		return new Rectangle2fp();
	}

	@Override
	public Rectangle2fp newBox(double x, double y, double width, double height) {
		return new Rectangle2fp(x, y, width, height);
	}
	
	@Override
	public PathElement2fp newMovePathElement(double x, double y) {
		return new PathElement2fp.MovePathElement2fp(x, y);
	}

	@Override
	public PathElement2fp newLinePathElement(double startX, double startY, double targetX, double targetY) {
		return new PathElement2fp.LinePathElement2fp(startX, startY, targetX, targetY);
	}

	@Override
	public PathElement2fp newClosePathElement(double lastPointX, double lastPointy, double firstPointX,
			double firstPointY) {
		return new PathElement2fp.ClosePathElement2fp(lastPointX, lastPointy, firstPointX, firstPointY);
	}

	@Override
	public PathElement2fp newCurvePathElement(double startX, double startY, double controlX, double controlY,
			double targetX, double targetY) {
		return new PathElement2fp.QuadPathElement2fp(startX, startY, controlX, controlY, targetX, targetY);
	}

	@Override
	public PathElement2fp newCurvePathElement(double startX, double startY, double controlX1, double controlY1,
			double controlX2, double controlY2, double targetX, double targetY) {
		return new PathElement2fp.CurvePathElement2fp(startX, startY, controlX1, controlY1,
				controlX2, controlY2, targetX, targetY);
	}

}