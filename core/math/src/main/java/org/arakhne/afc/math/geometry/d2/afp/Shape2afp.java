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
package org.arakhne.afc.math.geometry.d2.afp;

import org.arakhne.afc.math.MathConstants;
import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.Shape2D;
import org.arakhne.afc.math.geometry.d2.Transform2D;
import org.arakhne.afc.math.geometry.d2.Vector2D;
import org.eclipse.xtext.xbase.lib.Pure;

/** 2D shape with 2D floating coordinates.
 * 
 * @param <ST> is the type of the general implementation.
 * @param <IT> is the type of the implementation of this shape.
 * @param <IE> is the type of the path elements.
 * @param <P> is the type of the points.
 * @param <B> is the type of the bounding boxes.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public interface Shape2afp<
		ST extends Shape2afp<?, ?, IE, P, B>,
		IT extends Shape2afp<?, ?, IE, P, B>,
		IE extends PathElement2afp,
		P extends Point2D,
		B extends Rectangle2afp<?, ?, IE, P, B>>
		extends Shape2D<ST, IT, PathIterator2afp<IE>, P, B> {

	@Pure
	@Override
	default boolean contains(Point2D p) {
		return contains(p.getX(), p.getY());
	}

	/** Replies if the given rectangle is inside this shape.
	 * 
	 * @param r
	 * @return <code>true</code> if the given rectangle is inside this
	 * shape, otherwise <code>false</code>.
	 */
	@Pure
	boolean contains(Rectangle2afp<?, ?, ?, ?, ?> r);

	@Pure
	@Override
	default void translate(Vector2D vector) {
		translate(vector.getX(), vector.getY());
	}
	
	/** Replies if this shape is intersecting the given ellipse.
	 * 
	 * @param s
	 * @return <code>true</code> if this shape is intersecting the given shape;
	 * <code>false</code> if there is no intersection.
	 */
	@Pure
	public boolean intersects(Ellipse2afp<?, ?, ?, ?, ?> s);

	/** Replies if this shape is intersecting the given circle.
	 * 
	 * @param s
	 * @return <code>true</code> if this shape is intersecting the given shape;
	 * <code>false</code> if there is no intersection.
	 */
	@Pure
	public boolean intersects(Circle2afp<?, ?, ?, ?, ?> s);

	/** Replies if this shape is intersecting the given rectangle.
	 * 
	 * @param s
	 * @return <code>true</code> if this shape is intersecting the given shape;
	 * <code>false</code> if there is no intersection.
	 */
	@Pure
	boolean intersects(Rectangle2afp<?, ?, ?, ?, ?> s);

	/** Replies if this shape is intersecting the given line.
	 * 
	 * @param s
	 * @return <code>true</code> if this shape is intersecting the given shape;
	 * <code>false</code> if there is no intersection.
	 */
	@Pure
	public boolean intersects(Segment2afp<?, ?, ?, ?, ?> s);

	/** Replies if this shape is intersecting the given path.
	 * 
	 * @param s
	 * @return <code>true</code> if this shape is intersecting the given path;
	 * <code>false</code> if there is no intersection.
	 */
	@Pure
	default boolean intersects(Path2afp<?, ?, ?, ?, ?> s) {
		return intersects(s.getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO));
	}

	/** Replies if this shape is intersecting the shape representing the given path iterator.
	 * 
	 * @param iterator
	 * @return <code>true</code> if this shape is intersecting the given shape;
	 * <code>false</code> if there is no intersection.
	 */
	@Pure
	boolean intersects(PathIterator2afp<?> iterator);
	
	/** Replies if this shape is intersecting the given rectangle.
	 * 
	 * @param s
	 * @return <code>true</code> if this shape is intersecting the given shape;
	 * <code>false</code> if there is no intersection.
	 */
	@Pure
	public boolean intersects(OrientedRectangle2afp<?, ?, ?, ?, ?> s);
		
	/** Replies if this shape is intersecting the given rectangle.
	 * 
	 * @param s
	 * @return <code>true</code> if this shape is intersecting the given shape;
	 * <code>false</code> if there is no intersection.
	 */
	@Pure
	public boolean intersects(RoundRectangle2afp<?, ?, ?, ?, ?> s);

	/** Translate the shape.
	 * 
	 * @param dx
	 * @param dy
	 */
	public void translate(double dx, double dy); 

	/** Replies if the given point is inside this shape.
	 * 
	 * @param x
	 * @param y
	 * @return <code>true</code> if the given point is inside this
	 * shape, otherwise <code>false</code>.
	 */
	@Pure
	public boolean contains(double x, double y);

	/** Replies the factory of geometrical elements.
	 *
	 * @return the factory.
	 */
	GeomFactory2afp<IE, P, B> getGeomFactory();
	
	@Pure
	@SuppressWarnings("unchecked")
	@Override
	default ST createTransformedShape(Transform2D transform) {
		PathIterator2afp<?> pi = getPathIterator();
		GeomFactory2afp<IE, P, B> factory = getGeomFactory();
		Path2afp<?, ?, ?, P, ?> newPath = factory.newPath(pi.getWindingRule());
		Point2D p = factory.newPoint();
		Point2D t1 = factory.newPoint();
		Point2D t2 = factory.newPoint();
		PathElement2afp e;
		while (pi.hasNext()) {
			e = pi.next();
			switch(e.getType()) {
			case MOVE_TO:
				p.set(e.getToX(), e.getToY());
				transform.transform(p);
				newPath.moveTo(p.getX(), p.getY());
				break;
			case LINE_TO:
				p.set(e.getToX(), e.getToY());
				transform.transform(p);
				newPath.lineTo(p);
				break;
			case QUAD_TO:
				t1.set(e.getCtrlX1(), e.getCtrlY1());
				transform.transform(t1);
				p.set(e.getToX(), e.getToY());
				transform.transform(p);
				newPath.quadTo(t1.getX(), t1.getY(), p.getX(), p.getY());
				break;
			case CURVE_TO:
				t1.set(e.getCtrlX1(), e.getCtrlY1());
				transform.transform(t1);
				t2.set(e.getCtrlX2(), e.getCtrlY2());
				transform.transform(t2);
				p.set(e.getToX(), e.getToY());
				transform.transform(p);
				newPath.curveTo(t1.getX(), t1.getY(), t2.getX(), t2.getY(), p.getX(), p.getY());
				break;
			case CLOSE:
				newPath.closePath();
				break;
			default:
			}
		}
		return (ST) newPath;
	}

	@Override
	default B toBoundingBox() {
		B box = getGeomFactory().newBox();
		toBoundingBox(box);
		return box;
	}

}