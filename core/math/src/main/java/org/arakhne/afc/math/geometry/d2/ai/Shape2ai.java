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
package org.arakhne.afc.math.geometry.d2.ai;

import java.util.Iterator;

import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.Shape2D;
import org.arakhne.afc.math.geometry.d2.Transform2D;
import org.arakhne.afc.math.geometry.d2.Vector2D;
import org.eclipse.xtext.xbase.lib.Pure;

/** 2D shape with 2d floating coordinates.
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
public interface Shape2ai<
		ST extends Shape2ai<?, ?, IE, P, B>,
		IT extends Shape2ai<?, ?, IE, P, B>,
		IE extends PathElement2ai,
		P extends Point2D,
		B extends Rectangle2ai<?, ?, IE, P, B>>
		extends Shape2D<ST, IT, PathIterator2ai<IE>, P, B> {

	/** Replies an iterator on the points covered by this shape.
	 * <p>
	 * The implementation of the iterator depends on the shape type.
	 * There is no warranty about the order of the points.
	 * 
	 * @return an iterator on the points.
	 */
	@Pure
	Iterator<P> getPointIterator();

	@Pure
	@Override
	default boolean contains(Point2D p) {
		return contains(p.ix(), p.iy());
	}
	
	/** Replies if the given rectangle is inside this shape.
	 *
	 * @param box the rectangle to test.
	 * @return <code>true</code> if the given box is inside the shape.
	 */
	@Pure
	boolean contains(Rectangle2ai<?, ?, ?, ?, ?> box);

	@Pure
	@Override
	default void translate(Vector2D vector) {
		translate(vector.ix(), vector.iy());
	}

	
	@Pure
	@Override
	default B toBoundingBox() {
		B box = getGeomFactory().newBox();
		toBoundingBox(box);
		return box;
	}

	/** Replies if this shape is intersecting the given rectangle.
	 * 
	 * @param s
	 * @return <code>true</code> if this shape is intersecting the given shape;
	 * <code>false</code> if there is no intersection.
	 */
	@Pure
	public boolean intersects(Rectangle2ai<?, ?, ?, ?, ?> s);

	/** Replies if this shape is intersecting the given circle.
	 * 
	 * @param s
	 * @return <code>true</code> if this shape is intersecting the given shape;
	 * <code>false</code> if there is no intersection.
	 */
	@Pure
	public boolean intersects(Circle2ai<?, ?, ?, ?, ?> s);

	/** Replies if this shape is intersecting the given segment.
	 * 
	 * @param s
	 * @return <code>true</code> if this shape is intersecting the given shape;
	 * <code>false</code> if there is no intersection.
	 */
	@Pure
	public boolean intersects(Segment2ai<?, ?, ?, ?, ?> s);
		
	/** Replies if this shape is intersecting the given path.
	 * 
	 * @param s
	 * @return <code>true</code> if this shape is intersecting the given shape;
	 * <code>false</code> if there is no intersection.
	 */
	@Pure
	default boolean intersects(Path2ai<?, ?, ?, ?, ?> s) {
		return intersects(s.getPathIterator(/*MathConstants.SPLINE_APPROXIMATION_RATIO*/));
	}

	/** Replies if this shape is intersecting the path described by the given iterator.
	 * 
	 * @param s
	 * @return <code>true</code> if this shape is intersecting the given shape;
	 * <code>false</code> if there is no intersection.
	 */
	@Pure
	public boolean intersects(PathIterator2ai<?> s);

	/** Translate the shape.
	 * 
	 * @param dx
	 * @param dy
	 */
	public void translate(int dx, int dy); 

	/** Replies if the given point is inside this shape.
	 * 
	 * @param x
	 * @param y
	 * @return <code>true</code> if the given point is inside this
	 * shape, otherwise <code>false</code>.
	 */
	@Pure
	public boolean contains(int x, int y);

	/** Replies the factory of geometrical elements.
	 *
	 * @return the factory.
	 */
	GeomFactory2ai<IE, P, B> getGeomFactory();

	@Pure
	@SuppressWarnings("unchecked")
	@Override
	default ST createTransformedShape(Transform2D transform) {
		PathIterator2ai<?> pi = getPathIterator();
		GeomFactory2ai<IE, P, B> factory = getGeomFactory();
		Path2ai<?, ?, ?, P, ?> newPath = factory.newPath(pi.getWindingRule());
		Point2D p = factory.newPoint();
		Point2D t1 = factory.newPoint();
		Point2D t2 = factory.newPoint();
		PathElement2ai e;
		while (pi.hasNext()) {
			e = pi.next();
			switch(e.getType()) {
			case MOVE_TO:
				p.set(e.getToX(), e.getToY());
				transform.transform(p);
				newPath.moveTo(p.ix(), p.iy());
				break;
			case LINE_TO:
				p.set(e.getToX(), e.getToY());
				transform.transform(p);
				newPath.lineTo(p.ix(), p.iy());
				break;
			case QUAD_TO:
				t1.set(e.getCtrlX1(), e.getCtrlY1());
				transform.transform(t1);
				p.set(e.getToX(), e.getToY());
				transform.transform(p);
				newPath.quadTo(t1.ix(), t1.iy(), p.ix(), p.iy());
				break;
			case CURVE_TO:
				t1.set(e.getCtrlX1(), e.getCtrlY1());
				transform.transform(t1);
				t2.set(e.getCtrlX2(), e.getCtrlY2());
				transform.transform(t2);
				p.set(e.getToX(), e.getToY());
				transform.transform(p);
				newPath.curveTo(t1.ix(), t1.iy(), t2.ix(), t2.iy(), p.ix(), p.iy());
				break;
			case CLOSE:
				newPath.closePath();
				break;
			default:
			}
		}
		return (ST) newPath;
	}

}