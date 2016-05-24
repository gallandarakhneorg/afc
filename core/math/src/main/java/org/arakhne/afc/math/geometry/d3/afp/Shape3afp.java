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
package org.arakhne.afc.math.geometry.d3.afp;

import org.arakhne.afc.math.Unefficient;
import org.arakhne.afc.math.geometry.d3.Point3D;
import org.arakhne.afc.math.geometry.d3.Shape3D;
import org.arakhne.afc.math.geometry.d3.Transform3D;
import org.arakhne.afc.math.geometry.d3.Vector3D;
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
public interface Shape3afp<
		ST extends Shape3afp<?, ?, IE, P, V, B>,
		IT extends Shape3afp<?, ?, IE, P, V, B>,
		IE extends PathElement3afp,
		P extends Point3D<? super P, ? super V>,
		V extends Vector3D<? super V, ? super P>,
		B extends RectangularPrism3afp<?, ?, IE, P, V, B>>
		extends Shape3D<ST, IT, PathIterator3afp<IE>, P, V, B> {

	
	
	@Pure
	@Override
	default boolean contains(Point3D<?, ?> p) {
		assert (p != null) : "Point must be not null"; //$NON-NLS-1$
		return contains(p.getX(), p.getY(), p.getZ());
	}

	/** Replies if the given rectangle is inside this shape.
	 * 
	 * @param r
	 * @return <code>true</code> if the given rectangle is inside this
	 * shape, otherwise <code>false</code>.
	 */
	@Pure
	boolean contains(RectangularPrism3afp<?, ?, ?, ?, ?, ?> r);

	@Pure
	@Override
	default void translate(Vector3D<?, ?> vector) {
		assert (vector != null) : "Vector must be not null"; //$NON-NLS-1$
		translate(vector.getX(), vector.getY(), vector.getZ());
	}
	
	@Pure
	@Unefficient
	@Override
	default boolean intersects(Shape3D<?, ?, ?, ?, ?, ?> s) {
		if (s instanceof MultiShape3afp) {
			return intersects((MultiShape3afp<?, ?, ?, ?, ?, ?, ?>) s);
		}
		if(s instanceof Sphere3afp){
			return intersects((Sphere3afp<?, ?, ?, ?, ?, ?>) s);
		}
		if (s instanceof Path3afp) {
			return intersects((Path3afp<?, ?, ?, ?, ?, ?>) s);
		}
		if (s instanceof PathIterator3afp) {
			return intersects((PathIterator3afp<?>) s);
		}
		if (s instanceof RectangularPrism3afp) {
			return intersects((RectangularPrism3afp<?, ?, ?, ?, ?, ?>) s);
		}
		if (s instanceof Segment3afp) {
			return intersects((Segment3afp<?, ?, ?, ?, ?, ?>) s);
		}
		return intersects(getPathIterator());
	}


	/** Replies if this shape is intersecting the given circle.
	 * 
	 * @param s
	 * @return <code>true</code> if this shape is intersecting the given shape;
	 * <code>false</code> if there is no intersection.
	 */
	@Pure
	boolean intersects(Sphere3afp<?, ?, ?, ?, ?, ?> s);

	/** Replies if this shape is intersecting the given Prism.
	 * 
	 * @param s
	 * @return <code>true</code> if this shape is intersecting the given shape;
	 * <code>false</code> if there is no intersection.
	 */
	@Pure
	boolean intersects(Prism3afp<?, ?, ?, ?, ?, ?> s);

	/** Replies if this shape is intersecting the given line.
	 * 
	 * @param s
	 * @return <code>true</code> if this shape is intersecting the given shape;
	 * <code>false</code> if there is no intersection.
	 */
	@Pure
	boolean intersects(Segment3afp<?, ?, ?, ?, ?, ?> s);


	/** Replies if this shape is intersecting the given path.
	 * 
	 * @param s
	 * @return <code>true</code> if this shape is intersecting the given path;
	 * <code>false</code> if there is no intersection.
	 */
	@Pure
	default boolean intersects(Path3afp<?, ?, ?, ?, ?, ?> s) {
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
	boolean intersects(PathIterator3afp<?> iterator);

	/** Replies if this shape is intersecting the given multishape.
	 * 
	 * @param s
	 * @return <code>true</code> if this shape is intersecting the given shape;
	 * <code>false</code> if there is no intersection.
	 */
	@Pure
	boolean intersects(MultiShape3afp<?, ?, ?, ?, ?, ?, ?> s);

	/** Translate the shape.
	 * 
	 * @param dx
	 * @param dy
	 * @param dz
	 */
	void translate(double dx, double dy, double dz); 

	/** Replies if the given point is inside this shape.
	 * 
	 * @param x
	 * @param y
	 * @param z
	 * @return <code>true</code> if the given point is inside this
	 * shape, otherwise <code>false</code>.
	 */
	@Pure
	boolean contains(double x, double y, double z);

	@Override
	GeomFactory3afp<IE, P, V, B> getGeomFactory();
	
	@Pure
	@SuppressWarnings("unchecked")
	@Override
	default ST createTransformedShape(Transform3D transform) {
		if (transform == null || transform.isIdentity()) {
			return (ST) clone();
		}
		PathIterator3afp<?> pi = getPathIterator(transform);
		GeomFactory3afp<IE, P, V, B> factory = getGeomFactory();
		Path3afp<?, ?, ?, P, V, ?> newPath = factory.newPath(pi.getWindingRule());
		PathElement3afp e;
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

	@Override
	default B toBoundingBox() {
		B box = getGeomFactory().newBox();
		toBoundingBox(box);
		return box;
	}

}