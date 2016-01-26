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
package org.arakhne.afc.math.geometry.d3.continuous;

import org.arakhne.afc.math.geometry.d3.Point3D;
import org.arakhne.afc.math.geometry.d3.Shape3D;
import org.arakhne.afc.math.geometry.d3.Vector3D;
import org.eclipse.xtext.xbase.lib.Pure;

/** 2D shape with floating-point points.
 * 
 * @author $Author: galland$
 * @author $Author: hjaffali$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public interface Shape3F extends Shape3D<Shape3F> {

	/** Replies the bounds of the shape.
	 * If the current shape is a AlignedBox2f, this function
	 * replies the current shape, NOT A CLONE.
	 * 
	 * @return the bounds of the shape.
	 */
	public AbstractBoxedShape3F<?> toBoundingBox();
	
	/** Replies the bounds of the shape.
	 * 
	 * @param box is set with the bounds of the shape.
	 */
	public void toBoundingBox(AbstractBoxedShape3F<?> box);

	/** Replies the minimal distance from this shape to the given point.
	 * 
	 * @param p
	 * @return the minimal distance between this shape and the point.
	 */
	public double distance(Point3D p);

	/** Replies the squared value of the minimal distance from this shape to the given point.
	 * 
	 * @param p
	 * @return squared value of the minimal distance between this shape and the point.
	 */
	public double distanceSquared(Point3D p);

	/**
	 * Computes the L-1 (Manhattan) distance between this shape and
	 * point p1.  The L-1 distance is equal to abs(x1-x2) + abs(y1-y2) + abs(z1-z2).
	 * @param p the point
	 * @return the distance.
	 */
	public double distanceL1(Point3D p);

	/**
	 * Computes the L-infinite distance between this shape and
	 * point p1.  The L-infinite distance is equal to 
	 * MAX[abs(x1-x2), abs(y1-y2), abs(z1-z2)]. 
	 * @param p the point
	 * @return the distance.
	 */
	public double distanceLinf(Point3D p);

	/** Apply a transformation matrix to this shape.
	 * The components of the matrix (translation, rotation, scaling)
	 * are individually applied only if the shape is supporting it.
	 * 
	 * @param transformationMatrix the transformation.
	 */
	public void transform(Transform3D transformationMatrix);
	
	/** Translate the shape.
	 * 
	 * @param translation
	 */
	public void translate(Vector3D translation); 

	/** Translate the shape.
	 * 
	 * @param dx
	 * @param dy
	 * @param dz
	 */
	public void translate(double dx, double dy, double dz); 
	
	/** Replies if the given point is inside this shape.
	 * 
	 * @param x
	 * @param y
	 * @param z
	 * @return <code>true</code> if the given point is inside this
	 * shape, otherwise <code>false</code>.
	 */
	public boolean contains(double x, double y, double z);
	
	/** Replies the elements of the paths.
	 * 
	 * @param transform is the transformation to apply to the path.
	 * @return the elements of the path.
	 */
	public PathIterator3f getPathIterator(Transform3D transform);

	/** Replies the elements of the paths.
	 * 
	 * @return the elements of the path.
	 */
	public PathIterator3d getPathIteratorProperty();
	
	/** Replies the elements of the paths.
	 * 
	 * @param transform is the transformation to apply to the path.
	 * @return the elements of the path.
	 */
	public PathIterator3d getPathIteratorProperty(Transform3D transform);

	/** Replies the elements of the paths.
	 * 
	 * @return the elements of the path.
	 */
	public PathIterator3f getPathIterator();
	
	/** Apply the transformation to the shape and reply the result.
	 * This function does not change the current shape.
	 * 
	 * @param transform is the transformation to apply to the shape.
	 * @return the result of the transformation.
	 */
	public Shape3F createTransformedShape(Transform3D transform);
	
	/** Replies if this shape is intersecting the given rectangle.
	 * 
	 * @param s
	 * @return <code>true</code> if this shape is intersecting the given shape;
	 * <code>false</code> if there is no intersection.
	 */
	public boolean intersects(AbstractBoxedShape3F<?> s);

	/** Replies if this shape is intersecting the given circle.
	 * 
	 * @param s
	 * @return <code>true</code> if this shape is intersecting the given shape;
	 * <code>false</code> if there is no intersection.
	 */
	public boolean intersects(AbstractSphere3F s);

	/** Replies if this shape is intersecting the given line.
	 * 
	 * @param s
	 * @return <code>true</code> if this shape is intersecting the given shape;
	 * <code>false</code> if there is no intersection.
	 */
	public boolean intersects(AbstractSegment3F s);

	/** Replies if this shape is intersecting the given triangle.
	 * 
	 * @param s
	 * @return <code>true</code> if this shape is intersecting the given shape;
	 * <code>false</code> if there is no intersection.
	 */
	public boolean intersects(AbstractTriangle3F s);

	/** Replies if this shape is intersecting the given capsule.
	 * 
	 * @param s
	 * @return <code>true</code> if this shape is intersecting the given shape;
	 * <code>false</code> if there is no intersection.
	 */
	public boolean intersects(AbstractCapsule3F s);

	/** Replies if this shape is intersecting the given rectangle.
	 * 
	 * @param s
	 * @return <code>true</code> if this shape is intersecting the given shape;
	 * <code>false</code> if there is no intersection.
	 */
	public boolean intersects(AbstractOrientedBox3F s);

	/** Replies if this shape is intersecting the given plane.
	 * 
	 * @param p
	 * @return <code>true</code> if this shape is intersecting the given plane;
	 * <code>false</code> if there is no intersection.
	 */
	public boolean intersects(Plane3D<?> p);
	
	/** Replies if this shape is intersecting the given path.
	 * 
	 * @param s
	 * @return <code>true</code> if this shape is intersecting the given path;
	 * <code>false</code> if there is no intersection.
	 */
	@Pure
	public boolean intersects(Path3f s);
	
	/** Replies if this shape is intersecting the given path.
	 * 
	 * @param s
	 * @return <code>true</code> if this shape is intersecting the given path;
	 * <code>false</code> if there is no intersection.
	 */
	@Pure
	public boolean intersects(Path3d s);

}