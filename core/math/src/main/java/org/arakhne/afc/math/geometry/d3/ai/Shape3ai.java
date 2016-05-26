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
package org.arakhne.afc.math.geometry.d3.ai;

import java.util.Iterator;

import org.arakhne.afc.math.Unefficient;
import org.arakhne.afc.math.geometry.d3.Point3D;
import org.arakhne.afc.math.geometry.d3.Shape3D;
import org.arakhne.afc.math.geometry.d3.Transform3D;
import org.arakhne.afc.math.geometry.d3.Vector3D;
import org.eclipse.xtext.xbase.lib.Pure;

/** 2D shape with 2d floating coordinates.
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
public interface Shape3ai<
		ST extends Shape3ai<?, ?, IE, P, V, B>,
		IT extends Shape3ai<?, ?, IE, P, V, B>,
		IE extends PathElement3ai,
		P extends Point3D<? super P, ? super V>,
		V extends Vector3D<? super V, ? super P>,
		B extends RectangularPrism3ai<?, ?, IE, P, V, B>>
		extends Shape3D<ST, IT, PathIterator3ai<IE>, P, V, B> {

	/** Replies an iterator on the points covered by the perimeter of this shape.
	 * <p>
	 * The implementation of the iterator depends on the shape type.
	 * There is no warranty about the order of the points.
	 * 
	 * @return an iterator on the points that are located at the perimeter of the shape.
	 */
	@Pure
	Iterator<P> getPointIterator();

	@Pure
	@Override
	default boolean contains(Point3D<?, ?> p) {
		return contains(p.ix(), p.iy(), p.iz());
	}
	
	/** Replies if the given rectangle is inside this shape.
	 *
	 * @param box the rectangle to test.
	 * @return <code>true</code> if the given box is inside the shape.
	 */
	@Pure
	boolean contains(RectangularPrism3ai<?, ?, ?, ?, ?, ?> box);

	@Pure
	@Override
	default void translate(Vector3D<?, ?> vector) {
		translate(vector.ix(), vector.iy(), vector.iz());
	}

	
	@Pure
	@Override
	default B toBoundingBox() {
		B box = getGeomFactory().newBox();
		toBoundingBox(box);
		return box;
	}
	
	@Pure
	@Unefficient
	@Override
	default boolean intersects(Shape3D<?, ?, ?, ?, ?, ?> s) {
		if (s instanceof Sphere3ai) {
			return intersects((Sphere3ai<?, ?, ?, ?, ?, ?>) s);
		}
		if (s instanceof Path3ai) {
			return intersects((Path3ai<?, ?, ?, ?, ?, ?>) s);
		}
		if (s instanceof PathIterator3ai) {
			return intersects((PathIterator3ai<?>) s);
		}
		if (s instanceof RectangularPrism3ai) {
			return intersects((RectangularPrism3ai<?, ?, ?, ?, ?, ?>) s);
		}
		if (s instanceof Segment3ai) {
			return intersects((Segment3ai<?, ?, ?, ?, ?, ?>) s);
		}
		return intersects(getPathIterator());
	}

	/** Replies if this shape is intersecting the given rectangle.
	 * 
	 * @param s
	 * @return <code>true</code> if this shape is intersecting the given shape;
	 * <code>false</code> if there is no intersection.
	 */
	@Pure
	public boolean intersects(RectangularPrism3ai<?, ?, ?, ?, ?, ?> s);

	/** Replies if this shape is intersecting the given circle.
	 * 
	 * @param s
	 * @return <code>true</code> if this shape is intersecting the given shape;
	 * <code>false</code> if there is no intersection.
	 */
	@Pure
	public boolean intersects(Sphere3ai<?, ?, ?, ?, ?, ?> s);

	/** Replies if this shape is intersecting the given segment.
	 * 
	 * @param s
	 * @return <code>true</code> if this shape is intersecting the given shape;
	 * <code>false</code> if there is no intersection.
	 */
	@Pure
	public boolean intersects(Segment3ai<?, ?, ?, ?, ?, ?> s);
	
	/** Replies if this shape is intersecting the given multishape.
	 * 
	 * @param s
	 * @return <code>true</code> if this shape is intersecting the given shape;
	 * <code>false</code> if there is no intersection.
	 */
	@Pure
	public boolean intersects(MultiShape3ai<?, ?, ?, ?, ?, ?, ?> s);
		
	/** Replies if this shape is intersecting the given path.
	 * 
	 * @param s
	 * @return <code>true</code> if this shape is intersecting the given shape;
	 * <code>false</code> if there is no intersection.
	 */
	@Pure
	default boolean intersects(Path3ai<?, ?, ?, ?, ?, ?> s) {
		return intersects(s.getPathIterator(/*MathConstants.SPLINE_APPROXIMATION_RATIO*/));
	}

	/** Replies if this shape is intersecting the path described by the given iterator.
	 * 
	 * @param s
	 * @return <code>true</code> if this shape is intersecting the given shape;
	 * <code>false</code> if there is no intersection.
	 */
	@Pure
	public boolean intersects(PathIterator3ai<?> s);

	/** Translate the shape.
	 * 
	 * @param dx
	 * @param dy
	 * @param dz
	 */
	public void translate(int dx, int dy, int dz); 

	/** Replies if the given point is inside this shape.
	 * 
	 * @param x
	 * @param y
	 * @param z
	 * @return <code>true</code> if the given point is inside this
	 * shape, otherwise <code>false</code>.
	 */
	@Pure
	public boolean contains(int x, int y, int z);

	@Override
	GeomFactory3ai<IE, P, V, B> getGeomFactory();

	@Pure
	@SuppressWarnings("unchecked")
	@Override
	default ST createTransformedShape(Transform3D transform) {
		if (transform == null || transform.isIdentity()) {
			return (ST) clone();
		}
		PathIterator3ai<?> pi = getPathIterator(transform);
		GeomFactory3ai<IE, P, V, B> factory = getGeomFactory();
		Path3ai<?, ?, ?, P, V, ?> newPath = factory.newPath(pi.getWindingRule());
		PathElement3ai e;
		while (pi.hasNext()) {
			e = pi.next();
			switch(e.getType()) {
			case MOVE_TO:
				newPath.moveTo(e.getToX(), e.getToY(), e.getToZ());
				break;
			case LINE_TO:
				newPath.lineTo(e.getToX(), e.getToY(), e.getToZ());
				break;
			case QUAD_TO:
				newPath.quadTo(e.getCtrlX1(), e.getCtrlY1(), e.getCtrlZ1(), e.getToX(), e.getToY(), e.getToZ());
				break;
			case CURVE_TO:
				newPath.curveTo(e.getCtrlX1(), e.getCtrlY1(), e.getCtrlZ1(), e.getCtrlX2(), e.getCtrlY2(), e.getCtrlZ2(), e.getToX(), e.getToY(), e.getToZ());
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