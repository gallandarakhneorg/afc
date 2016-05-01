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

import org.arakhne.afc.math.Unefficient;
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
 * @param <V> is the type of the vectors.
 * @param <B> is the type of the bounding boxes.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public interface Shape2afp<
		ST extends Shape2afp<?, ?, IE, P, V, B>,
		IT extends Shape2afp<?, ?, IE, P, V, B>,
		IE extends PathElement2afp,
		P extends Point2D<? super P, ? super V>,
		V extends Vector2D<? super V, ? super P>,
		B extends Rectangle2afp<?, ?, IE, P, V, B>>
		extends Shape2D<ST, IT, PathIterator2afp<IE>, P, V, B> {

	
	
	@Pure
	@Override
	default boolean contains(Point2D<?, ?> p) {
		assert (p != null) : "Point must be not null"; //$NON-NLS-1$
		return contains(p.getX(), p.getY());
	}

	/** Replies if the given rectangle is inside this shape.
	 * 
	 * @param r
	 * @return <code>true</code> if the given rectangle is inside this
	 * shape, otherwise <code>false</code>.
	 */
	@Pure
	boolean contains(Rectangle2afp<?, ?, ?, ?, ?, ?> r);

	@Pure
	@Override
	default void translate(Vector2D<?, ?> vector) {
		assert (vector != null) : "Vector must be not null"; //$NON-NLS-1$
		translate(vector.getX(), vector.getY());
	}
	
	@Pure
	@Unefficient
	@Override
	default boolean intersects(Shape2D<?, ?, ?, ?, ?, ?> s) {
		if (s instanceof Circle2afp) {
			return intersects((Circle2afp<?, ?, ?, ?, ?, ?>) s);
		}
		if (s instanceof Ellipse2afp) {
			return intersects((Ellipse2afp<?, ?, ?, ?, ?, ?>) s);
		}
		if (s instanceof MultiShape2afp) {
			return intersects((MultiShape2afp<?, ?, ?, ?, ?, ?, ?>) s);
		}
		if (s instanceof OrientedRectangle2afp) {
			return intersects((OrientedRectangle2afp<?, ?, ?, ?, ?, ?>) s);
		}
		if (s instanceof Parallelogram2afp) {
			return intersects((Parallelogram2afp<?, ?, ?, ?, ?, ?>) s);
		}
		if (s instanceof Path2afp) {
			return intersects((Path2afp<?, ?, ?, ?, ?, ?>) s);
		}
		if (s instanceof PathIterator2afp) {
			return intersects((PathIterator2afp<?>) s);
		}
		if (s instanceof Rectangle2afp) {
			return intersects((Rectangle2afp<?, ?, ?, ?, ?, ?>) s);
		}
		if (s instanceof RoundRectangle2afp) {
			return intersects((RoundRectangle2afp<?, ?, ?, ?, ?, ?>) s);
		}
		if (s instanceof Segment2afp) {
			return intersects((Segment2afp<?, ?, ?, ?, ?, ?>) s);
		}
		if (s instanceof Triangle2afp) {
			return intersects((Triangle2afp<?, ?, ?, ?, ?, ?>) s);
		}
		return intersects(getPathIterator());
	}

	/** Replies if this shape is intersecting the given ellipse.
	 * 
	 * @param s
	 * @return <code>true</code> if this shape is intersecting the given shape;
	 * <code>false</code> if there is no intersection.
	 */
	@Pure
	boolean intersects(Ellipse2afp<?, ?, ?, ?, ?, ?> s);

	/** Replies if this shape is intersecting the given circle.
	 * 
	 * @param s
	 * @return <code>true</code> if this shape is intersecting the given shape;
	 * <code>false</code> if there is no intersection.
	 */
	@Pure
	boolean intersects(Circle2afp<?, ?, ?, ?, ?, ?> s);

	/** Replies if this shape is intersecting the given rectangle.
	 * 
	 * @param s
	 * @return <code>true</code> if this shape is intersecting the given shape;
	 * <code>false</code> if there is no intersection.
	 */
	@Pure
	boolean intersects(Rectangle2afp<?, ?, ?, ?, ?, ?> s);

	/** Replies if this shape is intersecting the given line.
	 * 
	 * @param s
	 * @return <code>true</code> if this shape is intersecting the given shape;
	 * <code>false</code> if there is no intersection.
	 */
	@Pure
	boolean intersects(Segment2afp<?, ?, ?, ?, ?, ?> s);

	/** Replies if this shape is intersecting the given triangle.
	 * 
	 * @param s
	 * @return <code>true</code> if this shape is intersecting the given shape;
	 * <code>false</code> if there is no intersection.
	 */
	@Pure
	boolean intersects(Triangle2afp<?, ?, ?, ?, ?, ?> s);

	/** Replies if this shape is intersecting the given path.
	 * 
	 * @param s
	 * @return <code>true</code> if this shape is intersecting the given path;
	 * <code>false</code> if there is no intersection.
	 */
	@Pure
	default boolean intersects(Path2afp<?, ?, ?, ?, ?, ?> s) {
		assert (s != null) : "Path must be not null"; //$NON-NLS-1$
		return intersects(s.getPathIterator());
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
	boolean intersects(OrientedRectangle2afp<?, ?, ?, ?, ?, ?> s);
		
	/** Replies if this shape is intersecting the given parallelogram.
	 * 
	 * @param s
	 * @return <code>true</code> if this shape is intersecting the given shape;
	 * <code>false</code> if there is no intersection.
	 */
	@Pure
	boolean intersects(Parallelogram2afp<?, ?, ?, ?, ?, ?> s);

	/** Replies if this shape is intersecting the given rectangle.
	 * 
	 * @param s
	 * @return <code>true</code> if this shape is intersecting the given shape;
	 * <code>false</code> if there is no intersection.
	 */
	@Pure
	boolean intersects(RoundRectangle2afp<?, ?, ?, ?, ?, ?> s);

	/** Replies if this shape is intersecting the given multishape.
	 * 
	 * @param s
	 * @return <code>true</code> if this shape is intersecting the given shape;
	 * <code>false</code> if there is no intersection.
	 */
	@Pure
	boolean intersects(MultiShape2afp<?, ?, ?, ?, ?, ?, ?> s);

	/** Translate the shape.
	 * 
	 * @param dx
	 * @param dy
	 */
	void translate(double dx, double dy); 

	/** Replies if the given point is inside this shape.
	 * 
	 * @param x
	 * @param y
	 * @return <code>true</code> if the given point is inside this
	 * shape, otherwise <code>false</code>.
	 */
	@Pure
	boolean contains(double x, double y);

	@Override
	GeomFactory2afp<IE, P, V, B> getGeomFactory();
	
	@Pure
	@SuppressWarnings("unchecked")
	@Override
	default ST createTransformedShape(Transform2D transform) {
		if (transform == null || transform.isIdentity()) {
			return (ST) clone();
		}
		PathIterator2afp<?> pi = getPathIterator(transform);
		GeomFactory2afp<IE, P, V, B> factory = getGeomFactory();
		Path2afp<?, ?, ?, P, V, ?> newPath = factory.newPath(pi.getWindingRule());
		PathElement2afp e;
		while (pi.hasNext()) {
			e = pi.next();
			switch(e.getType()) {
			case MOVE_TO:
				newPath.moveTo(e.getToX(), e.getToY());
				break;
			case LINE_TO:
				newPath.lineTo(e.getToX(), e.getToY());
				break;
			case QUAD_TO:
				newPath.quadTo(e.getCtrlX1(), e.getCtrlY1(), e.getToX(), e.getToY());
				break;
			case CURVE_TO:
				newPath.curveTo(e.getCtrlX1(), e.getCtrlY1(), e.getCtrlX2(), e.getCtrlY2(), e.getToX(), e.getToY());
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