/* 
 * $Id$
 * 
 * Copyright (C) 2015 Hamza JAFFALI.
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

import java.lang.ref.SoftReference;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.arakhne.afc.math.MathConstants;
import org.arakhne.afc.math.geometry.PathElementType;
import org.arakhne.afc.math.geometry.PathWindingRule;
import org.arakhne.afc.math.geometry.d3.Path3D;
import org.arakhne.afc.math.geometry.d3.Point3D;

/** A generic 3D-path.
 *
 * @author $Author: hjaffali$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */

public class Path3f extends AbstractShape3F<Path3f> implements Path3D<Shape3F,AlignedBox3f,AbstractPathElement3F,PathIterator3f> {

	private static final long serialVersionUID = -8167977956565440101L;

	/** Multiple of cubic & quad curve size.
	 */
	//36 = (3+3+3)*4
	static final int GROW_SIZE = 36;



	/** Replies the point on the path that is closest to the given point.
	 * <p>
	 * <strong>CAUTION:</strong> This function works only on path iterators
	 * that are replying polyline primitives, ie. if the
	 * {@link PathIterator3f#isPolyline()} of <var>pi</var> is replying
	 * <code>true</code>.
	 * {@link #getClosestPointTo(Point3D)} avoids this restriction.
	 * 
	 * @param pi is the iterator on the elements of the path.
	 * @param x
	 * @param y
	 * @param z
	 * @return the closest point on the shape; or the point itself
	 * if it is inside the shape.
	 */
	public static Point3D getClosestPointTo(PathIterator3f pathIterator, double x, double y, double z) {
		Point3D closest = null;
		double bestDist = Double.POSITIVE_INFINITY;
		Point3D candidate;
		AbstractPathElement3F pe = pathIterator.next();
		Path3f subPath;

		if (pe.type != PathElementType.MOVE_TO) {
			throw new IllegalArgumentException("missing initial moveto in path definition"); //$NON-NLS-1$
		}

		candidate = new Point3f(pe.getToX(), pe.getToY(), pe.getToZ());

		while (pathIterator.hasNext()) {
			pe = pathIterator.next();

			candidate = null;

			switch(pe.type) {
			case MOVE_TO:
				candidate = new Point3f(pe.getToX(), pe.getToY(), pe.getToZ());
				break;
			case LINE_TO:

				candidate = (new Segment3f(pe.getFromX(), pe.getFromY(), pe.getFromZ(), pe.getToX(), pe.getToY(), pe.getToZ())).getClosestPointTo(new Point3f(x,y,z));

				break;
			case CLOSE:
				if (!pe.isEmpty()) {
					candidate = (new Segment3f(pe.getFromX(), pe.getFromY(), pe.getFromZ(), pe.getToX(), pe.getToY(), pe.getToZ())).getClosestPointTo(new Point3f(x,y,z));
				}
				break;
			case QUAD_TO:
				subPath = new Path3f();
				subPath.moveTo(pe.getFromX(), pe.getFromY(), pe.getFromZ());
				subPath.quadTo(
						pe.getCtrlX1(), pe.getCtrlY1(), pe.getCtrlZ1(),
						pe.getToX(), pe.getToY(), pe.getToZ());

				candidate = subPath.getClosestPointTo(new Point3f(x,y,z));
				break;
			case CURVE_TO:
				subPath = new Path3f();
				subPath.moveTo(pe.getFromX(), pe.getFromY(), pe.getFromZ());
				subPath.curveTo(
						pe.getCtrlX1(), pe.getCtrlY1(), pe.getCtrlZ1(),
						pe.getCtrlX2(), pe.getCtrlY2(), pe.getCtrlZ2(),
						pe.getToX(), pe.getToY(), pe.getToZ());

				candidate = subPath.getClosestPointTo(new Point3f(x,y,z));
				break;
			default:
				throw new IllegalStateException(
						pe.type==null ? null : pe.type.toString());
			}

			if (candidate!=null) {
				double d = candidate.distanceSquared(new Point3f(x,y,z));
				if (d<bestDist) {
					bestDist = d;
					closest = candidate;
				}
			}
		}

		return closest;
	}


	/** Replies the point on the path that is farthest to the given point.
	 * <p>
	 * <strong>CAUTION:</strong> This function works only on path iterators
	 * that are replying polyline primitives, ie. if the
	 * {@link PathIterator3f#isPolyline()} of <var>pi</var> is replying
	 * <code>true</code>.
	 * {@link #getFarthestPointTo(Point3D)} avoids this restriction.
	 * 
	 * @param pi is the iterator on the elements of the path.
	 * @param x
	 * @param y
	 * @param z
	 * @return the farthest point on the shape.
	 */
	public static Point3D getFarthestPointTo(PathIterator3f pathIterator, double x, double y, double z) {
		Point3D farthest = null;
		double bestDist = Double.POSITIVE_INFINITY;
		Point3D candidate;
		AbstractPathElement3F pe = pathIterator.next();
		Path3f subPath;

		if (pe.type != PathElementType.MOVE_TO) {
			throw new IllegalArgumentException("missing initial moveto in path definition"); //$NON-NLS-1$
		}

		candidate = new Point3f(pe.getToX(), pe.getToY(), pe.getToZ());

		while (pathIterator.hasNext()) {
			pe = pathIterator.next();

			candidate = null;

			switch(pe.type) {
			case MOVE_TO:
				candidate = new Point3f(pe.getToX(), pe.getToY(), pe.getToZ());
				break;
			case LINE_TO:

				candidate = (new Segment3f(pe.getFromX(), pe.getFromY(), pe.getFromZ(), pe.getToX(), pe.getToY(), pe.getToZ())).getFarthestPointTo(new Point3f(x,y,z));

				break;
			case CLOSE:
				if (!pe.isEmpty()) {
					candidate = (new Segment3f(pe.getFromX(), pe.getFromY(), pe.getFromZ(), pe.getToX(), pe.getToY(), pe.getToZ())).getFarthestPointTo(new Point3f(x,y,z));
				}
				break;
			case QUAD_TO:
				subPath = new Path3f();
				subPath.moveTo(pe.getFromX(), pe.getFromY(), pe.getFromZ());
				subPath.quadTo(
						pe.getCtrlX1(), pe.getCtrlY1(), pe.getCtrlZ1(),
						pe.getToX(), pe.getToY(), pe.getToZ());

				candidate = subPath.getFarthestPointTo(new Point3f(x,y,z));
				break;
			case CURVE_TO:
				subPath = new Path3f();
				subPath.moveTo(pe.getFromX(), pe.getFromY(), pe.getFromZ());
				subPath.curveTo(
						pe.getCtrlX1(), pe.getCtrlY1(), pe.getCtrlZ1(),
						pe.getCtrlX2(), pe.getCtrlY2(), pe.getCtrlZ2(),
						pe.getToX(), pe.getToY(), pe.getToZ());

				candidate = subPath.getFarthestPointTo(new Point3f(x,y,z));
				break;
			default:
				throw new IllegalStateException(
						pe.type==null ? null : pe.type.toString());
			}

			if (candidate!=null) {
				double d = candidate.distanceSquared(new Point3f(x,y,z));
				if (d<bestDist) {
					bestDist = d;
					farthest = candidate;
				}
			}
		}

		return farthest;
	}

	private static boolean buildGraphicalBoundingBox(PathIterator3f iterator, AlignedBox3f box) {
		boolean foundOneLine = false;
		double xmin = Double.POSITIVE_INFINITY;
		double ymin = Double.POSITIVE_INFINITY;
		double zmin = Double.POSITIVE_INFINITY;
		double xmax = Double.NEGATIVE_INFINITY;
		double ymax = Double.NEGATIVE_INFINITY;
		double zmax = Double.NEGATIVE_INFINITY;
		AbstractPathElement3F element;
		Path3f subPath;
		while (iterator.hasNext()) {
			element = iterator.next();
			switch(element.type) {
			case LINE_TO:
				if (element.getFromX()<xmin) xmin = element.getFromX();
				if (element.getFromY()<ymin) ymin = element.getFromY();
				if (element.getFromZ()<zmin) zmin = element.getFromZ();
				if (element.getFromX()>xmax) xmax = element.getFromX();
				if (element.getFromY()>ymax) ymax = element.getFromY();
				if (element.getFromZ()>zmax) zmax = element.getFromZ();
				if (element.getToX()<xmin) xmin = element.getToX();
				if (element.getToY()<ymin) ymin = element.getToY();
				if (element.getToZ()<zmin) zmin = element.getToZ();
				if (element.getToX()>xmax) xmax = element.getToX();
				if (element.getToY()>ymax) ymax = element.getToY();
				if (element.getToZ()<zmax) zmax = element.getToZ();
				foundOneLine = true;
				break;
			case CURVE_TO:
				subPath = new Path3f();
				subPath.moveTo(element.getFromX(), element.getFromY(), element.getFromZ());
				subPath.curveTo(
						element.getCtrlX1(), element.getCtrlY1(), element.getCtrlZ1(),
						element.getCtrlX2(), element.getCtrlY2(), element.getCtrlZ2(),
						element.getToX(), element.getToY(), element.getToZ());
				if (buildGraphicalBoundingBox(
						subPath.getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO),
						box)) {
					if (box.getMinX()<xmin) xmin = box.getMinX();
					if (box.getMinY()<ymin) ymin = box.getMinY();
					if (box.getMinZ()<zmin) zmin = box.getMinZ();
					if (box.getMaxX()>xmax) xmax = box.getMaxX();
					if (box.getMinY()>ymax) ymax = box.getMinY();
					if (box.getMinZ()>zmax) zmax = box.getMinZ();
					foundOneLine = true;
				}
				break;
			case QUAD_TO:
				subPath = new Path3f();
				subPath.moveTo(element.getFromX(), element.getFromY(), element.getFromZ());
				subPath.quadTo(
						element.getCtrlX1(), element.getCtrlY1(), element.getCtrlZ1(),
						element.getToX(), element.getToY(), element.getToZ());
				if (buildGraphicalBoundingBox(
						subPath.getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO),
						box)) {
					if (box.getMinX()<xmin) xmin = box.getMinX();
					if (box.getMinY()<ymin) ymin = box.getMinY();
					if (box.getMinZ()<zmin) zmin = box.getMinZ();
					if (box.getMaxX()>xmax) xmax = box.getMaxX();
					if (box.getMinY()>ymax) ymax = box.getMinY();
					if (box.getMinZ()>zmax) zmax = box.getMinZ();
					foundOneLine = true;
				}
				break;
			case MOVE_TO:
			case CLOSE:
			default:
			}
		}
		if (foundOneLine) {
			box.setFromCorners(xmin, ymin, zmin, xmax, ymax, zmax);
		}
		else {
			box.clear();
		}
		return foundOneLine;

	}

	private boolean buildLogicalBoundingBox(AlignedBox3f box) {
		if (this.numCoords>0) {
			double xmin = Double.POSITIVE_INFINITY;
			double ymin = Double.POSITIVE_INFINITY;
			double zmin = Double.POSITIVE_INFINITY;
			double xmax = Double.NEGATIVE_INFINITY;
			double ymax = Double.NEGATIVE_INFINITY;
			double zmax = Double.NEGATIVE_INFINITY;
			for(int i=0; i<this.numCoords; i+= 3) {
				if (this.coords[i]<xmin) xmin = this.coords[i];
				if (this.coords[i+1]<ymin) ymin = this.coords[i+1];
				if (this.coords[i+2]<zmin) zmin = this.coords[i+2];
				if (this.coords[i]>xmax) xmax = this.coords[i];
				if (this.coords[i+1]>ymax) ymax = this.coords[i+1];
				if (this.coords[i+2]>zmax) zmax = this.coords[i+2];
			}
			box.setFromCorners(xmin, ymin, zmin, xmax, ymax, zmax);
			return true;
		}
		return false;
	}
	//---------------------------------------------------------------

	/** Array of types.
	 */
	PathElementType[] types;

	/** Array of coords.
	 */
	double[] coords;

	/** Number of types in the array.
	 */
	int numTypes = 0;

	/** Number of coords in the array.
	 */
	int numCoords = 0;

	/** Winding rule for the path.
	 */
	PathWindingRule windingRule;

	/** Indicates if the path is empty.
	 * The path is empty when there is no point inside, or
	 * all the points are at the same coordinate, or
	 * when the path does not represents a drawable path
	 * (a path with a line or a curve).
	 */
	private Boolean isEmpty = Boolean.TRUE;

	/** Indicates if the path contains base primitives
	 * (no curve).
	 */
	private Boolean isPolyline = Boolean.TRUE;

	/** Buffer for the bounds of the path that corresponds
	 * to the points really on the path (eg, the pixels
	 * drawn). The control points of the curves are
	 * not considered in this bounds.
	 */
	private SoftReference<AlignedBox3f> graphicalBounds = null;

	/** Buffer for the bounds of the path that corresponds
	 * to all the points added in the path.
	 */
	private SoftReference<AlignedBox3f> logicalBounds = null;

	/**
	 */
	public Path3f() {
		this(PathWindingRule.NON_ZERO);
	}

	/**
	 * @param iterator
	 */
	public Path3f(Iterator<AbstractPathElement3F> iterator) {
		this(PathWindingRule.NON_ZERO, iterator);
	}

	/**
	 * @param windingRule1
	 */
	public Path3f(PathWindingRule windingRule1) {
		assert(windingRule1!=null);
		this.types = new PathElementType[GROW_SIZE];
		this.coords = new double[GROW_SIZE];
		this.windingRule = windingRule1;
	}

	/**
	 * @param windingRule1
	 * @param iterator
	 */
	public Path3f(PathWindingRule windingRule1, Iterator<AbstractPathElement3F> iterator) {
		assert(windingRule1!=null);
		this.types = new PathElementType[GROW_SIZE];
		this.coords = new double[GROW_SIZE];
		this.windingRule = windingRule1;
		add(iterator);
	}

	/**
	 * @param p
	 */
	public Path3f(Path3f p) {
		this.coords = p.coords.clone();
		this.isEmpty = p.isEmpty;
		this.isPolyline = p.isPolyline;
		this.numCoords = p.numCoords;
		this.numTypes = p.numTypes;
		this.types = p.types.clone();
		this.windingRule = p.windingRule;
		AlignedBox3f box;
		box = p.graphicalBounds==null ? null : p.graphicalBounds.get();
		if (box!=null) {
			this.graphicalBounds = new SoftReference<>(box.clone());
		}
		box = p.logicalBounds==null ? null : p.logicalBounds.get();
		if (box!=null) {
			this.logicalBounds = new SoftReference<>(box.clone());
		}
	}

	/** Remove the point with the given coordinates.
	 * 
	 * @param x
	 * @param y
	 * @param z
	 * @return <code>true</code> if the point was removed; <code>false</code> otherwise.
	 */
	boolean remove(double x, double y, double z) {
		for(int i=0, j=0; i<this.numCoords && j<this.numTypes;) {
			switch(this.types[j]) {
			case MOVE_TO:
			case LINE_TO:
				if (x==this.coords[i] && y==this.coords[i+1] && z==this.coords[i+2]) {
					this.numCoords -= 3;
					--this.numTypes;
					System.arraycopy(this.coords, i+3, this.coords, i, this.numCoords);
					System.arraycopy(this.types, j+1, this.types, j, this.numTypes);
					this.isEmpty = null;
					return true;
				}
				i += 3;
				++j;
				break;
			case CURVE_TO:
				if ((x==this.coords[i] && y==this.coords[i+1] && z==this.coords[i+2])
						||(x==this.coords[i+3] && y==this.coords[i+4] && z==this.coords[i+5])
						||(x==this.coords[i+6] && y==this.coords[i+7] && z==this.coords[i+8])) {
					this.numCoords -= 9;
					--this.numTypes;
					System.arraycopy(this.coords, i+9, this.coords, i, this.numCoords);
					System.arraycopy(this.types, j+1, this.types, j, this.numTypes);
					this.isEmpty = null;
					this.isPolyline = null;
					return true;
				}
				i += 9;
				++j;
				break;
			case QUAD_TO:
				if ((x==this.coords[i] && y==this.coords[i+1] && z==this.coords[i+2])
						||(x==this.coords[i+3] && y==this.coords[i+4] && y==this.coords[i+5])) {
					this.numCoords -= 6;
					--this.numTypes;
					System.arraycopy(this.coords, i+6, this.coords, i, this.numCoords);
					System.arraycopy(this.types, j+1, this.types, j, this.numTypes);
					this.isEmpty = null;
					this.isPolyline = null;
					return true;
				}
				i += 6;
				++j;
				break;
			case CLOSE:
				++j;
				break;
			default:
				break;
			}
		}
		return false;
	}

	/** Replies if the given points exists in the coordinates of this path.
	 * 
	 * @param p
	 * @return <code>true</code> if the point is a control point of the path.
	 */
	boolean containsPoint(Point3D p) {
		double x, y, z;
		for(int i=0; i<this.numCoords;) {
			x = this.coords[i++];
			y = this.coords[i++];
			z = this.coords[i++];
			if (x==p.getX() && y==p.getY() && z==p.getZ()) {
				return true;
			}
		}
		return false;
	}


	@Override
	public String toString() {
		StringBuilder b = new StringBuilder();
		b.append("["); //$NON-NLS-1$
		if (this.numCoords>0) {
			b.append(this.coords[0]);
			for(int i=1; i<this.numCoords; ++i) {
				b.append(", "); //$NON-NLS-1$
				b.append(this.coords[i]);
			}
		}
		b.append("]"); //$NON-NLS-1$
		return b.toString();
	}


	@Override
	public boolean isEmpty() {
		if (this.isEmpty==null) {
			this.isEmpty = Boolean.TRUE;
			PathIterator3f pi = getPathIterator();
			AbstractPathElement3F pe;
			while (this.isEmpty==Boolean.TRUE && pi.hasNext()) {
				pe = pi.next();
				if (pe.isDrawable()) { 
					this.isEmpty = Boolean.FALSE;
				}
			}
		}
		return this.isEmpty.booleanValue();
	}

	@Override
	public void clear() {
		this.types = new PathElementType[GROW_SIZE];
		this.coords = new double[GROW_SIZE];
		this.windingRule = PathWindingRule.NON_ZERO;
		this.numCoords = 0;
		this.numTypes = 0;
		this.isEmpty = Boolean.TRUE;
		this.isPolyline = Boolean.TRUE;
		this.graphicalBounds = null;
		this.logicalBounds = null;
	}

	@Override
	public Path3f clone() {
		Path3f clone = super.clone();
		clone.coords = this.coords.clone();
		clone.types = this.types.clone();
		clone.windingRule = this.windingRule;

		return clone;
	}

	@Override
	public Point3D getClosestPointTo(Point3D p) {
		return getClosestPointTo(
				getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO),
				p.getX(), p.getY(),p.getZ());
	}


	@Override
	public Point3D getFarthestPointTo(Point3D p) {
		return getFarthestPointTo(
				getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO),
				p.getX(), p.getY(), p.getZ());
	}

	@Override
	public void set(Shape3F s) {
		clear();
		add(s.getPathIterator());
	}


	@Override
	public AlignedBox3f toBoundingBox() {
		AlignedBox3f bb = this.graphicalBounds==null ? null : this.graphicalBounds.get();
		if (bb==null) {
			bb = new AlignedBox3f();
			buildGraphicalBoundingBox(
					getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO),
					bb);
			this.graphicalBounds = new SoftReference<>(bb);
		}
		return bb;
	}

	/** Replies the bounding box of all the points added in this path.
	 * <p>
	 * The replied bounding box includes the (invisible) control points.
	 * 
	 * @return the bounding box with the control points.
	 * @see #toBoundingBox()
	 */
	public AlignedBox3f toBoundingBoxWithCtrlPoints() {
		AlignedBox3f bb = this.logicalBounds==null ? null : this.logicalBounds.get();
		if (bb==null) {
			bb = new AlignedBox3f();
			buildLogicalBoundingBox(bb);
			this.logicalBounds = new SoftReference<>(bb);
		}
		return bb;
	}


	@Override
	public void toBoundingBox(AbstractBoxedShape3F<?> box) {
		AlignedBox3f bb = this.graphicalBounds==null ? null : this.graphicalBounds.get();
		if (bb==null) {
			bb = new AlignedBox3f();
			buildGraphicalBoundingBox(
					getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO),
					bb);
			this.graphicalBounds = new SoftReference<>(bb);
		}
		box.set(bb);

	}

	/** Compute the bounding box of all the points added in this path.
	 * <p>
	 * The replied bounding box includes the (invisible) control points.
	 * 
	 * @param box is the rectangle to set with the bounds.
	 * @see #toBoundingBox()
	 */
	public void toBoundingBoxWithCtrlPoints(AlignedBox3f box) {
		AlignedBox3f bb = this.logicalBounds==null ? null : this.logicalBounds.get();
		if (bb==null) {
			bb = new AlignedBox3f();
			buildLogicalBoundingBox(bb);
			this.logicalBounds = new SoftReference<>(bb);
		}
		box.set(bb);
	}

	@Override
	public double distanceSquared(Point3D p) {
		Point3D c = getClosestPointTo(p);
		return c.distanceSquared(p);
	}

	@Override
	public double distanceL1(Point3D p) {
		Point3D c = getClosestPointTo(p);
		return c.distanceL1(p);
	}

	@Override
	public double distanceLinf(Point3D p) {
		Point3D c = getClosestPointTo(p);
		return c.distanceLinf(p);
	}

	/** Transform the current path.
	 * This function changes the current path.
	 * 
	 * @param transform is the spatial transformation to apply.
	 * @see #createTransformedShape(Transform3D)
	 */
	@Override
	public void transform(Transform3D transform) {
		if (transform!=null) {
			Point3D p = new Point3f();
			for(int i=0; i<this.numCoords;) {
				p.set(this.coords[i], this.coords[i+1], this.coords[i+2]);
				transform.transform(p);
				this.coords[i++] = p.getX();
				this.coords[i++] = p.getY();
				this.coords[i++] = p.getZ();
			}
			this.graphicalBounds = null;
			this.logicalBounds = null;
		}
	}

	@Override
	public Shape3F createTransformedShape(Transform3D transform) {
		Path3f newP = new Path3f(this);
		newP.transform(transform);

		return newP;
	}

	@Override
	public void translate(double dx, double dy, double dz) {
		for(int i=0; i<this.numCoords;) {
			this.coords[i++] += dx;
			this.coords[i++] += dy;
			this.coords[i++] += dz;
		}
		AlignedBox3f bb;
		bb = this.logicalBounds==null ? null : this.logicalBounds.get();
		if (bb!=null) bb.translate(dx, dy, dz);
		bb = this.graphicalBounds==null ? null : this.graphicalBounds.get();
		if (bb!=null) bb.translate(dx, dy, dz);
	}


	@Override
	public boolean intersects(AlignedBox3f s) {
		if (s.isEmpty()) return false;
		int mask = (this.windingRule == PathWindingRule.NON_ZERO ? -1 : 2);
		boolean intersects = false;
		PathIterator3f pi = getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO);

		AbstractPathElement3F pathElement = pi.next();

		if (pathElement.type != PathElementType.MOVE_TO) {
			throw new IllegalArgumentException("missing initial moveto in path definition"); //$NON-NLS-1$
		}

		Path3f subPath;
		double curx, cury, curz, movx, movy, movz, endx, endy, endz;
		curx = movx = pathElement.getToX();
		cury = movy = pathElement.getToY();
		curz = movz = pathElement.getToZ();

		while (pi.hasNext() && intersects==false) {
			pathElement = pi.next();

			switch (pathElement.type) {
			case MOVE_TO: 
				movx = curx = pathElement.getToX();
				movy = cury = pathElement.getToY();
				movz = cury = pathElement.getToZ();
				break;
			case LINE_TO:
				endx = pathElement.getToX();
				endy = pathElement.getToY();
				endz = pathElement.getToZ();

				intersects = AbstractSegment3F.intersectsSegmentAlignedBox(
						curx, cury, curz, 
						endx, endy, endz, 
						s.minx, s.miny, s.minz, 
						s.maxx, s.maxy, s.maxz);

				curx = endx;
				cury = endy;
				curz = endz;
				break;
			case QUAD_TO:
				endx = pathElement.getToX();
				endy = pathElement.getToY();
				endz = pathElement.getToZ();
				subPath = new Path3f();
				subPath.moveTo(curx, cury,curz);
				subPath.quadTo(
						pathElement.getCtrlX1(), pathElement.getCtrlY1(), pathElement.getCtrlZ1(),
						endx, endy, endz);

				intersects = subPath.intersects(s);

				curx = endx;
				cury = endy;
				curz = endz;
				break;
			case CURVE_TO:
				endx = pathElement.getToX();
				endy = pathElement.getToY();
				endz = pathElement.getToZ();
				subPath = new Path3f();
				subPath.moveTo(curx, cury,curz);
				subPath.curveTo(
						pathElement.getCtrlX1(), pathElement.getCtrlY1(), pathElement.getCtrlZ1(),
						pathElement.getCtrlX2(), pathElement.getCtrlY2(), pathElement.getCtrlZ2(),
						endx, endy, endz);

				intersects = subPath.intersects(s);

				curx = endx;
				cury = endy;
				curz = endz;
				break;
			case CLOSE:
				if (curx != movx || cury != movy || curz != movz) {
					intersects = AbstractSegment3F.intersectsSegmentAlignedBox(
							curx, cury, curz, 
							movx, movy, movz, 
							s.minx, s.miny, s.minz, 
							s.maxx, s.maxy, s.maxz);
				}

				curx = movx;
				cury = movy;
				curz = movz;
				break;
			default:
			}

		}

		return intersects && (mask!=0);
	}


	public boolean intersects(Path3f p) {
		//FIXME : STILL REMAIN A PROBLEM HERE
		if (p.isEmpty()) return false;
		int mask = (this.windingRule == PathWindingRule.NON_ZERO ? -1 : 2);
		boolean intersects = false;
		PathIterator3f pi = getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO);

		AbstractPathElement3F pathElement = pi.next();

		if (pathElement.type != PathElementType.MOVE_TO) {
			throw new IllegalArgumentException("missing initial moveto in path definition"); //$NON-NLS-1$
		}

		Path3f subPath;
		double curx, cury, curz, movx, movy, movz, endx, endy, endz;
		curx = movx = pathElement.getToX();
		cury = movy = pathElement.getToY();
		curz = movz = pathElement.getToZ();

		while (pi.hasNext() && intersects==false) {
			pathElement = pi.next();

			switch (pathElement.type) {
			case MOVE_TO: 
				movx = curx = pathElement.getToX();
				movy = cury = pathElement.getToY();
				movz = cury = pathElement.getToZ();
				break;
			case LINE_TO:
				endx = pathElement.getToX();
				endy = pathElement.getToY();
				endz = pathElement.getToZ();

				intersects = p.intersects(new Segment3f(
						curx, cury, curz, 
						endx, endy, endz));

				curx = endx;
				cury = endy;
				curz = endz;
				break;
			case QUAD_TO:
				endx = pathElement.getToX();
				endy = pathElement.getToY();
				endz = pathElement.getToZ();
				subPath = new Path3f();
				subPath.moveTo(curx, cury,curz);
				subPath.quadTo(
						pathElement.getCtrlX1(), pathElement.getCtrlY1(), pathElement.getCtrlZ1(),
						endx, endy, endz);

				intersects = subPath.intersects(p.clone());

				curx = endx;
				cury = endy;
				curz = endz;
				break;
			case CURVE_TO:
				endx = pathElement.getToX();
				endy = pathElement.getToY();
				endz = pathElement.getToZ();
				subPath = new Path3f();
				subPath.moveTo(curx, cury,curz);
				subPath.curveTo(
						pathElement.getCtrlX1(), pathElement.getCtrlY1(), pathElement.getCtrlZ1(),
						pathElement.getCtrlX2(), pathElement.getCtrlY2(), pathElement.getCtrlZ2(),
						endx, endy, endz);

				intersects = subPath.intersects(p.clone());

				curx = endx;
				cury = endy;
				curz = endz;
				break;
			case CLOSE:
				if (curx != movx || cury != movy || curz != movz) {
					intersects = p.intersects(new Segment3f(
							curx, cury, curz, 
							movx, movy, movz));
				}

				curx = movx;
				cury = movy;
				curz = movz;
				break;
			default:
			}

		}

		return intersects && (mask!=0);
	}

	@Override
	public boolean intersects(AbstractSphere3F s) {
		if (s.isEmpty()) return false;
		int mask = (this.windingRule == PathWindingRule.NON_ZERO ? -1 : 2);
		boolean intersects = false;
		PathIterator3f pi = getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO);

		AbstractPathElement3F pathElement = pi.next();

		if (pathElement.type != PathElementType.MOVE_TO) {
			throw new IllegalArgumentException("missing initial moveto in path definition"); //$NON-NLS-1$
		}

		Path3f subPath;
		double curx, cury, curz, movx, movy, movz, endx, endy, endz;
		curx = movx = pathElement.getToX();
		cury = movy = pathElement.getToY();
		curz = movz = pathElement.getToZ();

		while (pi.hasNext() && intersects==false) {
			pathElement = pi.next();

			switch (pathElement.type) {
			case MOVE_TO: 
				movx = curx = pathElement.getToX();
				movy = cury = pathElement.getToY();
				movz = cury = pathElement.getToZ();
				break;
			case LINE_TO:
				endx = pathElement.getToX();
				endy = pathElement.getToY();
				endz = pathElement.getToZ();

				intersects = s.intersects(new Segment3f(
						curx, cury, curz, 
						endx, endy, endz));

				curx = endx;
				cury = endy;
				curz = endz;
				break;
			case QUAD_TO:
				endx = pathElement.getToX();
				endy = pathElement.getToY();
				endz = pathElement.getToZ();
				subPath = new Path3f();
				subPath.moveTo(curx, cury,curz);
				subPath.quadTo(
						pathElement.getCtrlX1(), pathElement.getCtrlY1(), pathElement.getCtrlZ1(),
						endx, endy, endz);

				intersects = subPath.intersects(s);

				curx = endx;
				cury = endy;
				curz = endz;
				break;
			case CURVE_TO:
				endx = pathElement.getToX();
				endy = pathElement.getToY();
				endz = pathElement.getToZ();
				subPath = new Path3f();
				subPath.moveTo(curx, cury,curz);
				subPath.curveTo(
						pathElement.getCtrlX1(), pathElement.getCtrlY1(), pathElement.getCtrlZ1(),
						pathElement.getCtrlX2(), pathElement.getCtrlY2(), pathElement.getCtrlZ2(),
						endx, endy, endz);

				intersects = subPath.intersects(s);

				curx = endx;
				cury = endy;
				curz = endz;
				break;
			case CLOSE:
				if (curx != movx || cury != movy || curz != movz) {
					intersects = s.intersects(new Segment3f(
							curx, cury, curz, 
							movx, movy, movz));
				}

				curx = movx;
				cury = movy;
				curz = movz;
				break;
			default:
			}

		}

		return intersects && (mask!=0);
	}

	@Override
	public boolean intersects(AbstractSegment3F s) {
		if (s.isEmpty()) return false;
		int mask = (this.windingRule == PathWindingRule.NON_ZERO ? -1 : 2);
		boolean intersects = false;
		PathIterator3f pi = getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO);

		AbstractPathElement3F pathElement = pi.next();

		if (pathElement.type != PathElementType.MOVE_TO) {
			throw new IllegalArgumentException("missing initial moveto in path definition"); //$NON-NLS-1$
		}

		Path3f subPath;
		double curx, cury, curz, movx, movy, movz, endx, endy, endz;
		curx = movx = pathElement.getToX();
		cury = movy = pathElement.getToY();
		curz = movz = pathElement.getToZ();

		while (pi.hasNext() && intersects==false) {
			pathElement = pi.next();

			switch (pathElement.type) {
			case MOVE_TO: 
				movx = curx = pathElement.getToX();
				movy = cury = pathElement.getToY();
				movz = cury = pathElement.getToZ();
				break;
			case LINE_TO:
				endx = pathElement.getToX();
				endy = pathElement.getToY();
				endz = pathElement.getToZ();

				intersects = s.intersects(new Segment3f(
						curx, cury, curz, 
						endx, endy, endz));

				curx = endx;
				cury = endy;
				curz = endz;
				break;
			case QUAD_TO:
				endx = pathElement.getToX();
				endy = pathElement.getToY();
				endz = pathElement.getToZ();
				subPath = new Path3f();
				subPath.moveTo(curx, cury,curz);
				subPath.quadTo(
						pathElement.getCtrlX1(), pathElement.getCtrlY1(), pathElement.getCtrlZ1(),
						endx, endy, endz);

				intersects = subPath.intersects(s);

				curx = endx;
				cury = endy;
				curz = endz;
				break;
			case CURVE_TO:
				endx = pathElement.getToX();
				endy = pathElement.getToY();
				endz = pathElement.getToZ();
				subPath = new Path3f();
				subPath.moveTo(curx, cury,curz);
				subPath.curveTo(
						pathElement.getCtrlX1(), pathElement.getCtrlY1(), pathElement.getCtrlZ1(),
						pathElement.getCtrlX2(), pathElement.getCtrlY2(), pathElement.getCtrlZ2(),
						endx, endy, endz);

				intersects = subPath.intersects(s);

				curx = endx;
				cury = endy;
				curz = endz;
				break;
			case CLOSE:
				if (curx != movx || cury != movy || curz != movz) {
					intersects = s.intersects(new Segment3f(
							curx, cury, curz, 
							movx, movy, movz));
				}

				curx = movx;
				cury = movy;
				curz = movz;
				break;
			default:
			}

		}

		return intersects && (mask!=0);
	}


	@Override
	public boolean intersects(Triangle3f s) {
		if (s.isEmpty()) return false;
		int mask = (this.windingRule == PathWindingRule.NON_ZERO ? -1 : 2);
		boolean intersects = false;
		PathIterator3f pi = getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO);

		AbstractPathElement3F pathElement = pi.next();

		if (pathElement.type != PathElementType.MOVE_TO) {
			throw new IllegalArgumentException("missing initial moveto in path definition"); //$NON-NLS-1$
		}

		Path3f subPath;
		double curx, cury, curz, movx, movy, movz, endx, endy, endz;
		curx = movx = pathElement.getToX();
		cury = movy = pathElement.getToY();
		curz = movz = pathElement.getToZ();

		while (pi.hasNext() && intersects==false) {
			pathElement = pi.next();

			switch (pathElement.type) {
			case MOVE_TO: 
				movx = curx = pathElement.getToX();
				movy = cury = pathElement.getToY();
				movz = cury = pathElement.getToZ();
				break;
			case LINE_TO:
				endx = pathElement.getToX();
				endy = pathElement.getToY();
				endz = pathElement.getToZ();

				intersects = s.intersects(new Segment3f(
						curx, cury, curz, 
						endx, endy, endz));

				curx = endx;
				cury = endy;
				curz = endz;
				break;
			case QUAD_TO:
				endx = pathElement.getToX();
				endy = pathElement.getToY();
				endz = pathElement.getToZ();
				subPath = new Path3f();
				subPath.moveTo(curx, cury,curz);
				subPath.quadTo(
						pathElement.getCtrlX1(), pathElement.getCtrlY1(), pathElement.getCtrlZ1(),
						endx, endy, endz);

				intersects = subPath.intersects(s);

				curx = endx;
				cury = endy;
				curz = endz;
				break;
			case CURVE_TO:
				endx = pathElement.getToX();
				endy = pathElement.getToY();
				endz = pathElement.getToZ();
				subPath = new Path3f();
				subPath.moveTo(curx, cury,curz);
				subPath.curveTo(
						pathElement.getCtrlX1(), pathElement.getCtrlY1(), pathElement.getCtrlZ1(),
						pathElement.getCtrlX2(), pathElement.getCtrlY2(), pathElement.getCtrlZ2(),
						endx, endy, endz);

				intersects = subPath.intersects(s);

				curx = endx;
				cury = endy;
				curz = endz;
				break;
			case CLOSE:
				if (curx != movx || cury != movy || curz != movz) {
					intersects = s.intersects(new Segment3f(
							curx, cury, curz, 
							movx, movy, movz));
				}

				curx = movx;
				cury = movy;
				curz = movz;
				break;
			default:
			}

		}

		return intersects && (mask!=0);
	}


	@Override
	public boolean intersects(AbstractCapsule3F s) {
		if (s.isEmpty()) return false;
		int mask = (this.windingRule == PathWindingRule.NON_ZERO ? -1 : 2);
		boolean intersects = false;
		PathIterator3f pi = getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO);

		AbstractPathElement3F pathElement = pi.next();

		if (pathElement.type != PathElementType.MOVE_TO) {
			throw new IllegalArgumentException("missing initial moveto in path definition"); //$NON-NLS-1$
		}

		Path3f subPath;
		double curx, cury, curz, movx, movy, movz, endx, endy, endz;
		curx = movx = pathElement.getToX();
		cury = movy = pathElement.getToY();
		curz = movz = pathElement.getToZ();

		while (pi.hasNext() && intersects==false) {
			pathElement = pi.next();

			switch (pathElement.type) {
			case MOVE_TO: 
				movx = curx = pathElement.getToX();
				movy = cury = pathElement.getToY();
				movz = cury = pathElement.getToZ();
				break;
			case LINE_TO:
				endx = pathElement.getToX();
				endy = pathElement.getToY();
				endz = pathElement.getToZ();

				intersects = s.intersects(new Segment3f(
						curx, cury, curz, 
						endx, endy, endz));

				curx = endx;
				cury = endy;
				curz = endz;
				break;
			case QUAD_TO:
				endx = pathElement.getToX();
				endy = pathElement.getToY();
				endz = pathElement.getToZ();
				subPath = new Path3f();
				subPath.moveTo(curx, cury,curz);
				subPath.quadTo(
						pathElement.getCtrlX1(), pathElement.getCtrlY1(), pathElement.getCtrlZ1(),
						endx, endy, endz);

				intersects = subPath.intersects(s);

				curx = endx;
				cury = endy;
				curz = endz;
				break;
			case CURVE_TO:
				endx = pathElement.getToX();
				endy = pathElement.getToY();
				endz = pathElement.getToZ();
				subPath = new Path3f();
				subPath.moveTo(curx, cury,curz);
				subPath.curveTo(
						pathElement.getCtrlX1(), pathElement.getCtrlY1(), pathElement.getCtrlZ1(),
						pathElement.getCtrlX2(), pathElement.getCtrlY2(), pathElement.getCtrlZ2(),
						endx, endy, endz);

				intersects = subPath.intersects(s);

				curx = endx;
				cury = endy;
				curz = endz;
				break;
			case CLOSE:
				if (curx != movx || cury != movy || curz != movz) {
					intersects = s.intersects(new Segment3f(
							curx, cury, curz, 
							movx, movy, movz));
				}

				curx = movx;
				cury = movy;
				curz = movz;
				break;
			default:
			}

		}

		return intersects && (mask!=0);
	}

	@Override
	public boolean intersects(AbstractOrientedBox3F s) {
		if (s.isEmpty()) return false;
		int mask = (this.windingRule == PathWindingRule.NON_ZERO ? -1 : 2);
		boolean intersects = false;
		PathIterator3f pi = getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO);

		AbstractPathElement3F pathElement = pi.next();

		if (pathElement.type != PathElementType.MOVE_TO) {
			throw new IllegalArgumentException("missing initial moveto in path definition"); //$NON-NLS-1$
		}

		Path3f subPath;
		double curx, cury, curz, movx, movy, movz, endx, endy, endz;
		curx = movx = pathElement.getToX();
		cury = movy = pathElement.getToY();
		curz = movz = pathElement.getToZ();

		while (pi.hasNext() && intersects==false) {
			pathElement = pi.next();

			switch (pathElement.type) {
			case MOVE_TO: 
				movx = curx = pathElement.getToX();
				movy = cury = pathElement.getToY();
				movz = cury = pathElement.getToZ();
				break;
			case LINE_TO:
				endx = pathElement.getToX();
				endy = pathElement.getToY();
				endz = pathElement.getToZ();

				intersects = s.intersects(new Segment3f(
						curx, cury, curz, 
						endx, endy, endz));

				curx = endx;
				cury = endy;
				curz = endz;
				break;
			case QUAD_TO:
				endx = pathElement.getToX();
				endy = pathElement.getToY();
				endz = pathElement.getToZ();
				subPath = new Path3f();
				subPath.moveTo(curx, cury,curz);
				subPath.quadTo(
						pathElement.getCtrlX1(), pathElement.getCtrlY1(), pathElement.getCtrlZ1(),
						endx, endy, endz);

				intersects = subPath.intersects(s);

				curx = endx;
				cury = endy;
				curz = endz;
				break;
			case CURVE_TO:
				endx = pathElement.getToX();
				endy = pathElement.getToY();
				endz = pathElement.getToZ();
				subPath = new Path3f();
				subPath.moveTo(curx, cury,curz);
				subPath.curveTo(
						pathElement.getCtrlX1(), pathElement.getCtrlY1(), pathElement.getCtrlZ1(),
						pathElement.getCtrlX2(), pathElement.getCtrlY2(), pathElement.getCtrlZ2(),
						endx, endy, endz);

				intersects = subPath.intersects(s);

				curx = endx;
				cury = endy;
				curz = endz;
				break;
			case CLOSE:
				if (curx != movx || cury != movy || curz != movz) {
					intersects = s.intersects(new Segment3f(
							curx, cury, curz, 
							movx, movy, movz));
				}

				curx = movx;
				cury = movy;
				curz = movz;
				break;
			default:
			}

		}

		return intersects && (mask!=0);
	}


	public boolean intersects(Plane3D<?> p) {
		int mask = (this.windingRule == PathWindingRule.NON_ZERO ? -1 : 2);
		boolean intersects = false;
		PathIterator3f pi = getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO);

		AbstractPathElement3F pathElement = pi.next();

		if (pathElement.type != PathElementType.MOVE_TO) {
			throw new IllegalArgumentException("missing initial moveto in path definition"); //$NON-NLS-1$
		}

		Path3f subPath;
		double curx, cury, curz, movx, movy, movz, endx, endy, endz;
		curx = movx = pathElement.getToX();
		cury = movy = pathElement.getToY();
		curz = movz = pathElement.getToZ();

		while (pi.hasNext() && intersects==false) {
			pathElement = pi.next();

			switch (pathElement.type) {
			case MOVE_TO: 
				movx = curx = pathElement.getToX();
				movy = cury = pathElement.getToY();
				movz = cury = pathElement.getToZ();
				break;
			case LINE_TO:
				endx = pathElement.getToX();
				endy = pathElement.getToY();
				endz = pathElement.getToZ();

				intersects = p.intersects(new Segment3f(
						curx, cury, curz, 
						endx, endy, endz));

				curx = endx;
				cury = endy;
				curz = endz;
				break;
			case QUAD_TO:
				endx = pathElement.getToX();
				endy = pathElement.getToY();
				endz = pathElement.getToZ();
				subPath = new Path3f();
				subPath.moveTo(curx, cury,curz);
				subPath.quadTo(
						pathElement.getCtrlX1(), pathElement.getCtrlY1(), pathElement.getCtrlZ1(),
						endx, endy, endz);

				intersects = subPath.intersects(p);

				curx = endx;
				cury = endy;
				curz = endz;
				break;
			case CURVE_TO:
				endx = pathElement.getToX();
				endy = pathElement.getToY();
				endz = pathElement.getToZ();
				subPath = new Path3f();
				subPath.moveTo(curx, cury,curz);
				subPath.curveTo(
						pathElement.getCtrlX1(), pathElement.getCtrlY1(), pathElement.getCtrlZ1(),
						pathElement.getCtrlX2(), pathElement.getCtrlY2(), pathElement.getCtrlZ2(),
						endx, endy, endz);

				intersects = subPath.intersects(p);

				curx = endx;
				cury = endy;
				curz = endz;
				break;
			case CLOSE:
				if (curx != movx || cury != movy || curz != movz) {
					intersects = p.intersects(new Segment3f(
							curx, cury, curz, 
							movx, movy, movz));
				}

				curx = movx;
				cury = movy;
				curz = movz;
				break;
			default:
			}

		}

		return intersects && (mask!=0);
	}

	@Override
	public boolean isPolyline() {
		if (this.isPolyline==null) {
			this.isPolyline = Boolean.TRUE;
			PathIterator3f pi = getPathIterator();
			AbstractPathElement3F pe;
			PathElementType t;
			while (this.isPolyline==Boolean.TRUE && pi.hasNext()) {
				pe = pi.next();
				t = pe.getType();
				if (t==PathElementType.CURVE_TO || t==PathElementType.QUAD_TO) { 
					this.isPolyline = Boolean.FALSE;
				}
			}
		}
		return this.isPolyline.booleanValue();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean contains(double x, double y, double z) {
		AlignedBox3f ab = this.toBoundingBox();
		return ab.contains(new Point3f(x,y,z));
	}


	public boolean equals(Path3f path) {
		return (this.numCoords==path.numCoords
				&& this.numTypes==path.numTypes
				&& Arrays.equals(this.coords, path.coords)
				&& Arrays.equals(this.types, path.types)
				&& this.windingRule==path.windingRule);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Path3f) {
			Path3f p = (Path3f)obj;
			return (this.numCoords==p.numCoords
					&& this.numTypes==p.numTypes
					&& Arrays.equals(this.coords, p.coords)
					&& Arrays.equals(this.types, p.types)
					&& this.windingRule==p.windingRule);
		}
		return false;
	}

	@Override
	public int hashCode() {
		long bits = 1L;
		bits = 31L * bits + this.numCoords;
		bits = 31L * bits + this.numTypes;
		bits = 31L * bits + Arrays.hashCode(this.coords);
		bits = 31L * bits + Arrays.hashCode(this.types);
		bits = 31L * bits + this.windingRule.ordinal();
		return (int) (bits ^ (bits >> 32));
	}

	@Override
	public PathIterator3f getPathIterator(double flatness) {
		return new FlatteningPathIterator(getWindingRule(), getPathIterator(null), flatness, 10);
	}

	/** Replies an iterator on the path elements.
	 * <p>
	 * Only {@link PathElementType#MOVE_TO},
	 * {@link PathElementType#LINE_TO}, and 
	 * {@link PathElementType#CLOSE} types are returned by the iterator.
	 * <p>
	 * The amount of subdivision of the curved segments is controlled by the 
	 * flatness parameter, which specifies the maximum distance that any point 
	 * on the unflattened transformed curve can deviate from the returned
	 * flattened path segments. Note that a limit on the accuracy of the
	 * flattened path might be silently imposed, causing very small flattening
	 * parameters to be treated as larger values. This limit, if there is one,
	 * is defined by the particular implementation that is used.
	 * <p>
	 * The iterator for this class is not multi-threaded safe.
	 *
	 * @param transform is an optional affine Transform3D to be applied to the
	 * coordinates as they are returned in the iteration, or <code>null</code> if 
	 * untransformed coordinates are desired.
	 * @param flatness is the maximum distance that the line segments used to approximate
	 * the curved segments are allowed to deviate from any point on the original curve.
	 * @return an iterator on the path elements.
	 */
	public PathIterator3f getPathIterator(Transform3D transform, double flatness) {
		return new FlatteningPathIterator(getWindingRule(), getPathIterator(transform), flatness, 10);
	}

	/** {@inheritDoc}
	 */
	@Override
	public PathIterator3f getPathIterator(Transform3D transform) {
		if (transform == null) {
			return new CopyPathIterator();
		}
		return new TransformPathIterator(transform);
	}

	@Override
	public PathWindingRule getWindingRule() {
		return this.windingRule;
	}

	/** Set the winding rule for the path.
	 * 
	 * @param r is the winding rule for the path.
	 */
	public void setWindingRule(PathWindingRule r) {
		assert(r!=null);
		this.windingRule = r;
	}

	/** Add the elements replied by the iterator into this path.
	 * 
	 * @param iterator
	 */
	public void add(Iterator<AbstractPathElement3F> iterator) {
		AbstractPathElement3F element;
		while (iterator.hasNext()) {
			element = iterator.next();
			switch(element.type) {
			case MOVE_TO:
				moveTo(element.getToX(), element.getToY(), element.getToZ());
				break;
			case LINE_TO:
				lineTo(element.getToX(), element.getToY(),  element.getToZ());
				break;
			case QUAD_TO:
				quadTo(element.getCtrlX1(), element.getCtrlY1(), element.getCtrlZ1(), element.getToX(), element.getToY(), element.getToZ());
				break;
			case CURVE_TO:
				curveTo(element.getCtrlX1(), element.getCtrlY1(), element.getCtrlZ1(), element.getCtrlX2(), element.getCtrlY2(), element.getCtrlZ2(), element.getToX(), element.getToY(), element.getToZ());
				break;
			case CLOSE:
				closePath();
				break;
			default:
			}
		}
	}

	/** Remove the last action.
	 */
	public void removeLast() {
		if (this.numTypes>0) {
			switch(this.types[this.numTypes-1]) {
			case CLOSE:
				// no coord to remove
				break;
			case MOVE_TO:
			case LINE_TO:
				this.numCoords -= 3;
				break;
			case CURVE_TO:
				this.numCoords -= 9;
				this.isPolyline = null;
				break;
			case QUAD_TO:
				this.numCoords -= 6;
				this.isPolyline = null;
				break;
			default:
				throw new IllegalStateException();
			}
			--this.numTypes;
			this.isEmpty = null;
			this.graphicalBounds = null;
			this.logicalBounds = null;
		}
	}

	/** Change the coordinates of the last inserted point.
	 * 
	 * @param x
	 * @param y
	 * @param z
	 */
	public void setLastPoint(double x, double y, double z) {
		if (this.numCoords>=3) {
			this.coords[this.numCoords-3] = x;
			this.coords[this.numCoords-2] = y;
			this.coords[this.numCoords-1] = z;
			this.graphicalBounds = null;
			this.logicalBounds = null;
		}
	}

	private void ensureSlots(boolean needMove, int n) {
		if (needMove && this.numTypes==0) {
			throw new IllegalStateException("missing initial moveto in path definition"); //$NON-NLS-1$
		}
		if (this.types.length==this.numTypes) {
			this.types = Arrays.copyOf(this.types, this.types.length+GROW_SIZE);
		}
		while ((this.numCoords+n)>=this.coords.length) {
			this.coords = Arrays.copyOf(this.coords, this.coords.length+GROW_SIZE);
		}
	}

	/**
	 * Adds a point to the path by moving to the specified
	 * coordinates specified in double precision.
	 *
	 * @param x the specified X coordinate
	 * @param y the specified Y coordinate
	 * @param z the specified Z coordinate
	 */
	public void moveTo(double x, double y, double z) {
		if (this.numTypes>0 && this.types[this.numTypes-1]==PathElementType.MOVE_TO) {
			this.coords[this.numCoords-3] = x;
			this.coords[this.numCoords-2] = y;
			this.coords[this.numCoords-1] = z;
		}
		else {
			ensureSlots(false, 3);
			this.types[this.numTypes++] = PathElementType.MOVE_TO;
			this.coords[this.numCoords++] = x;
			this.coords[this.numCoords++] = y;
			this.coords[this.numCoords++] = z;
		}
		this.graphicalBounds = null;
		this.logicalBounds = null;
	}

	/**
	 * Adds a point to the path by drawing a straight line from the
	 * current coordinates to the new specified coordinates
	 * specified in double precision.
	 *
	 * @param x the specified X coordinate
	 * @param y the specified Y coordinate
	 * @param z the specified Z coordinate
	 */
	public void lineTo(double x, double y, double z) {
		ensureSlots(true, 3);
		this.types[this.numTypes++] = PathElementType.LINE_TO;
		this.coords[this.numCoords++] = x;
		this.coords[this.numCoords++] = y;
		this.coords[this.numCoords++] = z;
		this.isEmpty = null;
		this.graphicalBounds = null;
		this.logicalBounds = null;
	}

	/**
	 * Adds a curved segment, defined by two new points, to the path by
	 * drawing a Quadratic curve that intersects both the current
	 * coordinates and the specified coordinates {@code (x2,y2,z2)},
	 * using the specified point {@code (x1,y1,z1)} as a quadratic
	 * parametric control point.
	 * All coordinates are specified in double precision.
	 *
	 * @param x1 the X coordinate of the quadratic control point
	 * @param y1 the Y coordinate of the quadratic control point
	 * @param z1 the Z coordinate of the quadratic control point
	 * @param x2 the X coordinate of the final end point
	 * @param y2 the Y coordinate of the final end point
	 * @param z2 the Z coordinate of the final end point
	 */
	public void quadTo(double x1, double y1, double z1, double x2, double y2, double z2) {
		ensureSlots(true, 6);
		this.types[this.numTypes++] = PathElementType.QUAD_TO;
		this.coords[this.numCoords++] = x1;
		this.coords[this.numCoords++] = y1;
		this.coords[this.numCoords++] = z1;
		this.coords[this.numCoords++] = x2;
		this.coords[this.numCoords++] = y2;
		this.coords[this.numCoords++] = z2;
		this.isEmpty = null;
		this.isPolyline = Boolean.FALSE;
		this.graphicalBounds = null;
		this.logicalBounds = null;
	}

	/**
	 * Adds a curved segment, defined by three new points, to the path by
	 * drawing a B&eacute;zier curve that intersects both the current
	 * coordinates and the specified coordinates {@code (x3,y3,z3)},
	 * using the specified points {@code (x1,y1,z1)} and {@code (x2,y2,z2)} as
	 * B&eacute;zier control points.
	 * All coordinates are specified in double precision.
	 *
	 * @param x1 the X coordinate of the first B&eacute;zier control point
	 * @param y1 the Y coordinate of the first B&eacute;zier control point
	 * @param z1 the Z coordinate of the first B&eacute;zier control point
	 * @param x2 the X coordinate of the second B&eacute;zier control point
	 * @param y2 the Y coordinate of the second B&eacute;zier control point
	 * @param z2 the Z coordinate of the second B&eacute;zier control point
	 * @param x3 the X coordinate of the final end point
	 * @param y3 the Y coordinate of the final end point
	 * @param z3 the Z coordinate of the final end point
	 */
	public void curveTo(double x1, double y1, double z1,
			double x2, double y2, double z2,
			double x3, double y3, double z3) {
		ensureSlots(true, 9);
		this.types[this.numTypes++] = PathElementType.CURVE_TO;
		this.coords[this.numCoords++] = x1;
		this.coords[this.numCoords++] = y1;
		this.coords[this.numCoords++] = z1;
		this.coords[this.numCoords++] = x2;
		this.coords[this.numCoords++] = y2;
		this.coords[this.numCoords++] = z2;
		this.coords[this.numCoords++] = x3;
		this.coords[this.numCoords++] = y3;
		this.coords[this.numCoords++] = z3;
		this.isEmpty = null;
		this.isPolyline = Boolean.FALSE;
		this.graphicalBounds = null;
		this.logicalBounds = null;
	}

	/**
	 * Closes the current subpath by drawing a straight line back to
	 * the coordinates of the last {@code moveTo}.  If the path is already
	 * closed or if the previous coordinates are for a {@code moveTo}
	 * then this method has no effect.
	 */
	public void closePath() {
		if (this.numTypes<=0 ||
				(this.types[this.numTypes-1]!=PathElementType.CLOSE
				&&this.types[this.numTypes-1]!=PathElementType.MOVE_TO)) {
			ensureSlots(true, 0);
			this.types[this.numTypes++] = PathElementType.CLOSE;
		}
	}

	/** Replies the number of points in the path.
	 *
	 * @return the number of points in the path.
	 */
	public int size() {
		return this.numCoords/3;
	}

	/** Replies the coordinates of this path in an array of
	 * double precision floating-point numbers.
	 * 
	 * @return the coordinates.
	 */
	public final double[] toDoubleArray() {
		return toDoubleArray(null);
	}

	/** Replies the coordinates of this path in an array of
	 * double precision floating-point numbers.
	 * 
	 * @param transform is the transformation to apply to all the coordinates.
	 * @return the coordinates.
	 */
	public double[] toDoubleArray(Transform3D transform) {
		double[] clone = new double[this.numCoords];
		if (transform==null) {
			for(int i=0; i<this.numCoords; ++i) {
				clone[i] = this.coords[i];
			}
		}
		else {
			Point3f p = new Point3f();
			for(int i=0; i<clone.length;) {
				p.x = this.coords[i];
				p.y = this.coords[i+1];
				p.y = this.coords[i+2];
				transform.transform(p);
				clone[i++] = p.x;
				clone[i++] = p.y;
				clone[i++] = p.z;
			}
		}
		return clone;
	}

	/** Replies the points of this path in an array.
	 * 
	 * @return the points.
	 */
	public final Point3D[] toPointArray() {
		return toPointArray(null);
	}

	/** Replies the points of this path in an array.
	 * 
	 * @param transform is the transformation to apply to all the points.
	 * @return the points.
	 */
	public Point3D[] toPointArray(Transform3D transform) {
		Point3D[] clone = new Point3D[this.numCoords/3];
		if (transform==null) {
			for(int i=0, j=0; j<this.numCoords; ++i) {
				clone[i] = new Point3f(
						this.coords[j++],
						this.coords[j++],
						this.coords[j++]);
			}
		}
		else {
			for(int i=0, j=0; j<clone.length; ++i) {
				clone[i] = new Point3f(
						this.coords[j++],
						this.coords[j++],
						this.coords[j++]);
				transform.transform(clone[i]);
			}
		}
		return clone;
	}

	/** Replies the point at the given index.
	 * The index is in [0;{@link #size()}).
	 *
	 * @param index
	 * @return the point at the given index.
	 */
	public Point3f getPointAt(int index) {
		return new Point3f(
				this.coords[index*3],
				this.coords[index*3+1],
				this.coords[index*3+2]);
	}

	/** Replies the last point in the path.
	 *
	 * @return the last point.
	 */
	//FIXME : TO BE VERIFYIED 
	public Point3f getCurrentPoint() {
		return new Point3f(
				this.coords[this.coords.length-3],
				this.coords[this.coords.length-2],
				this.coords[this.coords.length-1]);
	}
	//-----------------------------------------------------------------------------



	/** A path iterator that does not transform the coordinates.
	 *
	 * @author $Author: hjaffali$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	private class CopyPathIterator implements PathIterator3f {

		private final Point3D p1 = new Point3f();
		private final Point3D p2 = new Point3f();
		private int iType = 0;
		private int iCoord = 0;
		private double movex, movey, movez;

		/**
		 */
		public CopyPathIterator() {
			//
		}

		@Override
		public boolean hasNext() {
			return this.iType<Path3f.this.numTypes;
		}

		@Override
		public AbstractPathElement3F next() {
			int type = this.iType;
			if (this.iType>=Path3f.this.numTypes) {
				throw new NoSuchElementException();
			}
			AbstractPathElement3F element = null;
			switch(Path3f.this.types[type]) {
			case MOVE_TO:
				if (this.iCoord+3>Path3f.this.numCoords) {
					throw new NoSuchElementException();
				}
				this.movex = Path3f.this.coords[this.iCoord++];
				this.movey = Path3f.this.coords[this.iCoord++];
				this.movez = Path3f.this.coords[this.iCoord++];
				this.p2.set(this.movex, this.movey, this.movez);
				element = new AbstractPathElement3F.MovePathElement3f(
						this.p2.getX(), this.p2.getY(), this.p2.getZ());
				break;
			case LINE_TO:
				if (this.iCoord+3>Path3f.this.numCoords) {
					throw new NoSuchElementException();
				}
				this.p1.set(this.p2);
				this.p2.set(
						Path3f.this.coords[this.iCoord++],
						Path3f.this.coords[this.iCoord++],
						Path3f.this.coords[this.iCoord++]);
				element = new AbstractPathElement3F.LinePathElement3f(
						this.p1.getX(), this.p1.getY(), this.p1.getZ(),
						this.p2.getX(), this.p2.getY(), this.p2.getZ());
				break;
			case QUAD_TO:
			{
				if (this.iCoord+6>Path3f.this.numCoords) {
					throw new NoSuchElementException();
				}
				this.p1.set(this.p2);
				double ctrlx = Path3f.this.coords[this.iCoord++];
				double ctrly = Path3f.this.coords[this.iCoord++];
				double ctrlz = Path3f.this.coords[this.iCoord++];
				this.p2.set(
						Path3f.this.coords[this.iCoord++],
						Path3f.this.coords[this.iCoord++],
						Path3f.this.coords[this.iCoord++]);
				element = new AbstractPathElement3F.QuadPathElement3f(
						this.p1.getX(), this.p1.getY(), this.p1.getZ(),
						ctrlx, ctrly, ctrlz,
						this.p2.getX(), this.p2.getY(), this.p2.getZ());
			}
			break;
			case CURVE_TO:
			{
				if (this.iCoord+9>Path3f.this.numCoords) {
					throw new NoSuchElementException();
				}
				this.p1.set(this.p2);
				double ctrlx1 = Path3f.this.coords[this.iCoord++];
				double ctrly1 = Path3f.this.coords[this.iCoord++];
				double ctrlz1 = Path3f.this.coords[this.iCoord++];
				double ctrlx2 = Path3f.this.coords[this.iCoord++];
				double ctrly2 = Path3f.this.coords[this.iCoord++];
				double ctrlz2 = Path3f.this.coords[this.iCoord++];
				this.p2.set(
						Path3f.this.coords[this.iCoord++],
						Path3f.this.coords[this.iCoord++],
						Path3f.this.coords[this.iCoord++]);
				element = new AbstractPathElement3F.CurvePathElement3f(
						this.p1.getX(), this.p1.getY(), this.p1.getZ(),
						ctrlx1, ctrly1, ctrlz1,
						ctrlx2, ctrly2, ctrlz2,
						this.p2.getX(), this.p2.getY(), this.p2.getZ());
			}
			break;
			case CLOSE:
				this.p1.set(this.p2);
				this.p2.set(this.movex, this.movey, this.movez);
				element = new AbstractPathElement3F.ClosePathElement3f(
						this.p1.getX(), this.p1.getY(), this.p1.getZ(),
						this.p2.getX(), this.p2.getY(), this.p2.getZ());
				break;
			default:
			}
			if (element==null)
				throw new NoSuchElementException();

			++this.iType;

			return element;
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}

		@Override
		public PathWindingRule getWindingRule() {
			return Path3f.this.getWindingRule();
		}

		@Override
		public boolean isPolyline() {
			return Path3f.this.isPolyline();
		}

	} // class CopyPathIterator

	/** A path iterator that transforms the coordinates.
	 *
	 * @author $Author: hjaffali$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	private class TransformPathIterator implements PathIterator3f {

		private final Transform3D transform;
		private final Point3D p1 = new Point3f();
		private final Point3D p2 = new Point3f();
		private final Point3D ptmp1 = new Point3f();
		private final Point3D ptmp2 = new Point3f();
		private int iType = 0;
		private int iCoord = 0;
		private double movex, movey, movez;

		/**
		 * @param transform1
		 */
		public TransformPathIterator(Transform3D transform1) {
			assert(transform1!=null);
			this.transform = transform1;
		}

		@Override
		public boolean hasNext() {
			return this.iType<Path3f.this.numTypes;
		}

		@Override
		public AbstractPathElement3F next() {
			if (this.iType>=Path3f.this.numTypes) {
				throw new NoSuchElementException();
			}
			AbstractPathElement3F element = null;
			switch(Path3f.this.types[this.iType++]) {
			case MOVE_TO:
				this.movex = Path3f.this.coords[this.iCoord++];
				this.movey = Path3f.this.coords[this.iCoord++];
				this.movez = Path3f.this.coords[this.iCoord++];
				this.p2.set(this.movex, this.movey, this.movez);
				this.transform.transform(this.p2);
				element = new AbstractPathElement3F.MovePathElement3f(
						this.p2.getX(), this.p2.getY(), this.p2.getZ());
				break;
			case LINE_TO:
				this.p1.set(this.p2);
				this.p2.set(
						Path3f.this.coords[this.iCoord++],
						Path3f.this.coords[this.iCoord++],
						Path3f.this.coords[this.iCoord++]);
				this.transform.transform(this.p2);
				element = new AbstractPathElement3F.LinePathElement3f(
						this.p1.getX(), this.p1.getY(), this.p1.getZ(),
						this.p2.getX(), this.p2.getY(), this.p2.getZ());
				break;
			case QUAD_TO:
			{
				this.p1.set(this.p2);
				this.ptmp1.set(
						Path3f.this.coords[this.iCoord++],
						Path3f.this.coords[this.iCoord++],
						Path3f.this.coords[this.iCoord++]);
				this.transform.transform(this.ptmp1);
				this.p2.set(
						Path3f.this.coords[this.iCoord++],
						Path3f.this.coords[this.iCoord++],
						Path3f.this.coords[this.iCoord++]);
				this.transform.transform(this.p2);
				element = new AbstractPathElement3F.QuadPathElement3f(
						this.p1.getX(), this.p1.getY(), this.p1.getZ(),
						this.ptmp1.getX(), this.ptmp1.getY(), this.ptmp1.getZ(),
						this.p2.getX(), this.p2.getY(), this.p2.getZ());
			}
			break;
			case CURVE_TO:
			{
				this.p1.set(this.p2);
				this.ptmp1.set(
						Path3f.this.coords[this.iCoord++],
						Path3f.this.coords[this.iCoord++],
						Path3f.this.coords[this.iCoord++]);
				this.transform.transform(this.ptmp1);
				this.ptmp2.set(
						Path3f.this.coords[this.iCoord++],
						Path3f.this.coords[this.iCoord++],
						Path3f.this.coords[this.iCoord++]);
				this.transform.transform(this.ptmp2);
				this.p2.set(
						Path3f.this.coords[this.iCoord++],
						Path3f.this.coords[this.iCoord++],
						Path3f.this.coords[this.iCoord++]);
				this.transform.transform(this.p2);
				element = new AbstractPathElement3F.CurvePathElement3f(
						this.p1.getX(), this.p1.getY(), this.p1.getZ(),
						this.ptmp1.getX(), this.ptmp1.getY(), this.ptmp1.getZ(),
						this.ptmp2.getX(), this.ptmp2.getY(), this.ptmp2.getZ(),
						this.p2.getX(), this.p2.getY(), this.p2.getZ());
			}
			break;
			case CLOSE:
				this.p1.set(this.p2);
				this.p2.set(this.movex, this.movey, this.movez);
				this.transform.transform(this.p2);
				element = new AbstractPathElement3F.ClosePathElement3f(
						this.p1.getX(), this.p1.getY(), this.p1.getZ(),
						this.p2.getX(), this.p2.getY(), this.p2.getZ());
				break;
			default:
			}
			if (element==null)
				throw new NoSuchElementException();
			return element;
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}

		@Override
		public PathWindingRule getWindingRule() {
			return Path3f.this.getWindingRule();
		}

		@Override
		public boolean isPolyline() {
			return Path3f.this.isPolyline();
		}

	}  // class TransformPathIterator

	/** A path iterator that is flattening the path.
	 * This iterator was copied from AWT FlatteningPathIterator.
	 *
	 * @author $Author: hjaffali$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	private static class FlatteningPathIterator implements PathIterator3f {

		/** Winding rule of the path.
		 */
		private final PathWindingRule windingRule;

		/** The source iterator.
		 */
		private final PathIterator3f pathIterator;

		/**
		 * Square of the flatness parameter for testing against squared lengths.
		 */
		private final double squaredFlatness;

		/**
		 * Maximum number of recursion levels.
		 */
		private final int limit; 

		/** The recursion level at which each curve being held in storage was generated.
		 */
		private int levels[];

		/** The cache of interpolated coords.
		 * Note that this must be long enough
		 * to store a full cubic segment and
		 * a relative cubic segment to avoid
		 * aliasing when copying the coords
		 * of a curve to the end of the array.
		 *
		 */
		private double hold[] = new double[36];

		/** The index of the last curve segment being held for interpolation.
		 */
		private int holdEnd;

		/**
		 * The index of the curve segment that was last interpolated.  This
		 * is the curve segment ready to be returned in the next call to
		 * next().
		 */
		private int holdIndex;

		/** The ending x of the last segment.
		 */
		private double currentX;

		/** The ending y of the last segment.
		 */
		private double currentY;

		/** The ending z of the last segment.
		 */
		private double currentZ;

		/** The x of the last move segment.
		 */
		private double moveX;

		/** The y of the last move segment.
		 */
		private double moveY;

		/** The z of the last move segment.
		 */
		private double moveZ;

		/** The index of the entry in the
		 * levels array of the curve segment
		 * at the holdIndex
		 */
		private int levelIndex;

		/** True when iteration is done.
		 */
		private boolean done;

		/** The type of the path element.
		 */
		private PathElementType holdType;

		/** The x of the last move segment replied by next.
		 */
		private double lastNextX;

		/** The y of the last move segment replied by next.
		 */
		private double lastNextY;

		/** The y of the last move segment replied by next.
		 */
		private double lastNextZ;

		/**
		 * @param windingRule1 is the winding rule of the path.
		 * @param pathIterator1 is the path iterator that may be used to initialize the path.
		 * @param flatness the maximum allowable distance between the
		 * control points and the flattened curve
		 * @param limit1 the maximum number of recursive subdivisions
		 * allowed for any curved segment
		 */
		public FlatteningPathIterator(PathWindingRule windingRule1, PathIterator3f pathIterator1, double flatness, int limit1) {
			assert(windingRule1!=null);
			assert(flatness>=0f);
			assert(limit1>=0);
			this.windingRule = windingRule1;
			this.pathIterator = pathIterator1;
			this.squaredFlatness = flatness * flatness;
			this.limit = limit1;
			this.levels = new int[limit1 + 1];
			searchNext();
		}

		/**
		 * Ensures that the hold array can hold up to (want) more values.
		 * It is currently holding (hold.length - holdIndex) values.
		 */
		private void ensureHoldCapacity(int want) {
			if (this.holdIndex - want < 0) {
				int have = this.hold.length - this.holdIndex;
				int newsize = this.hold.length + GROW_SIZE;
				double newhold[] = new double[newsize];
				System.arraycopy(this.hold, this.holdIndex,
						newhold, this.holdIndex + GROW_SIZE,
						have);
				this.hold = newhold;
				this.holdIndex += GROW_SIZE;
				this.holdEnd += GROW_SIZE;
			}
		}

		/**
		 * Returns the square of the flatness, or maximum distance of a
		 * control point from the line connecting the end points, of the
		 * quadratic curve specified by the control points stored in the
		 * indicated array at the indicated index.
		 * @param coords an array containing coordinate values
		 * @param offset the index into <code>coords</code> from which to
		 *          to start getting the values from the array
		 * @return the flatness of the quadratic curve that is defined by the
		 *          values in the specified array at the specified index.
		 */
		private static double getQuadSquaredFlatness(double coords[], int offset) {
			return AbstractSegment3F.distanceSquaredLinePoint(
					coords[offset + 0], coords[offset + 1], coords[offset + 2],
					coords[offset + 6], coords[offset + 7], coords[offset + 8],
					coords[offset + 3], coords[offset + 4], coords[offset + 5]);
		}

		/**
		 * Subdivides the quadratic curve specified by the coordinates
		 * stored in the <code>src</code> array at indices
		 * <code>srcoff</code> through <code>srcoff</code>&nbsp;+&nbsp;8
		 * and stores the resulting two subdivided curves into the two
		 * result arrays at the corresponding indices.
		 * Either or both of the <code>left</code> and <code>right</code>
		 * arrays can be <code>null</code> or a reference to the same array
		 * and offset as the <code>src</code> array.
		 * Note that the last point in the first subdivided curve is the
		 * same as the first point in the second subdivided curve.  Thus,
		 * it is possible to pass the same array for <code>left</code> and
		 * <code>right</code> and to use offsets such that
		 * <code>rightoff</code> equals <code>leftoff</code> + 6 in order
		 * to avoid allocating extra storage for this common point.
		 * @param src the array holding the coordinates for the source curve
		 * @param srcoff the offset into the array of the beginning of the
		 * the 9 source coordinates
		 * @param left the array for storing the coordinates for the first
		 * half of the subdivided curve
		 * @param leftoff the offset into the array of the beginning of the
		 * the 9 left coordinates
		 * @param right the array for storing the coordinates for the second
		 * half of the subdivided curve
		 * @param rightoff the offset into the array of the beginning of the
		 * the 9 right coordinates
		 */
		private static void subdivideQuad(double src[], int srcoff,
				double left[], int leftoff,
				double right[], int rightoff) {
			double x1 = src[srcoff + 0];
			double y1 = src[srcoff + 1];
			double z1 = src[srcoff + 2];
			double ctrlx = src[srcoff + 3];
			double ctrly = src[srcoff + 4];
			double ctrlz = src[srcoff + 5];
			double x2 = src[srcoff + 6];
			double y2 = src[srcoff + 7];
			double z2 = src[srcoff + 8];
			if (left != null) {
				left[leftoff + 0] = x1;
				left[leftoff + 1] = y1;
				left[leftoff + 2] = z1;
			}
			if (right != null) {
				right[rightoff + 6] = x2;
				right[rightoff + 7] = y2;
				right[rightoff + 8] = z2;
			}
			x1 = (x1 + ctrlx) / 2f;
			y1 = (y1 + ctrly) / 2f;
			z1 = (z1 + ctrlz) / 2f;
			x2 = (x2 + ctrlx) / 2f;
			y2 = (y2 + ctrly) / 2f;
			z2 = (z2 + ctrlz) / 2f;
			ctrlx = (x1 + x2) / 2f;
			ctrly = (y1 + y2) / 2f;
			ctrlz = (z1 + z2) / 2f;
			if (left != null) {
				left[leftoff + 3] = x1;
				left[leftoff + 4] = y1;
				left[leftoff + 5] = z1;
				left[leftoff + 6] = ctrlx;
				left[leftoff + 7] = ctrly;
				left[leftoff + 8] = ctrlz;
			}
			if (right != null) {
				right[rightoff + 0] = ctrlx;
				right[rightoff + 1] = ctrly;
				right[rightoff + 2] = ctrlz;
				right[rightoff + 3] = x2;
				right[rightoff + 4] = y2;
				right[rightoff + 5] = z2;
			}
		}

		/**
		 * Returns the square of the flatness of the cubic curve specified
		 * by the control points stored in the indicated array at the
		 * indicated index. The flatness is the maximum distance
		 * of a control point from the line connecting the end points.
		 * @param coords an array containing coordinates
		 * @param offset the index of <code>coords</code> from which to begin
		 *          getting the end points and control points of the curve
		 * @return the square of the flatness of the <code>CubicCurve3D</code>
		 *          specified by the coordinates in <code>coords</code> at
		 *          the specified offset.
		 */
		private static double getCurveSquaredFlatness(double coords[], int offset) {
			return Math.max(
					AbstractSegment3F.distanceSquaredSegmentPoint(
							coords[offset + 9],
							coords[offset + 10],
							coords[offset + 11],
							coords[offset + 3],
							coords[offset + 4],
							coords[offset + 5],
							coords[offset + 0],
							coords[offset + 1],
							coords[offset + 2]),
					AbstractSegment3F.distanceSquaredSegmentPoint(
							coords[offset + 9],
							coords[offset + 10],
							coords[offset + 11],
							coords[offset + 6],
							coords[offset + 7],
							coords[offset + 8],
							coords[offset + 0],
							coords[offset + 1],
							coords[offset + 2]));
		}

		/**
		 * Subdivides the cubic curve specified by the coordinates
		 * stored in the <code>src</code> array at indices <code>srcoff</code>
		 * through (<code>srcoff</code>&nbsp;+&nbsp;11) and stores the
		 * resulting two subdivided curves into the two result arrays at the
		 * corresponding indices.
		 * Either or both of the <code>left</code> and <code>right</code>
		 * arrays may be <code>null</code> or a reference to the same array
		 * as the <code>src</code> array.
		 * Note that the last point in the first subdivided curve is the
		 * same as the first point in the second subdivided curve. Thus,
		 * it is possible to pass the same array for <code>left</code>
		 * and <code>right</code> and to use offsets, such as <code>rightoff</code>
		 * equals (<code>leftoff</code> + 9), in order
		 * to avoid allocating extra storage for this common point.
		 * @param src the array holding the coordinates for the source curve
		 * @param srcoff the offset into the array of the beginning of the
		 * the 9 source coordinates
		 * @param left the array for storing the coordinates for the first
		 * half of the subdivided curve
		 * @param leftoff the offset into the array of the beginning of the
		 * the 9 left coordinates
		 * @param right the array for storing the coordinates for the second
		 * half of the subdivided curve
		 * @param rightoff the offset into the array of the beginning of the
		 * the 9 right coordinates
		 */
		private static void subdivideCurve(
				double src[], int srcoff,
				double left[], int leftoff,
				double right[], int rightoff) {
			double x1 = src[srcoff + 0];
			double y1 = src[srcoff + 1];
			double z1 = src[srcoff + 2];
			double ctrlx1 = src[srcoff + 3];
			double ctrly1 = src[srcoff + 4];
			double ctrlz1 = src[srcoff + 5];
			double ctrlx2 = src[srcoff + 6];
			double ctrly2 = src[srcoff + 7];
			double ctrlz2 = src[srcoff + 8];
			double x2 = src[srcoff + 9];
			double y2 = src[srcoff + 10];
			double z2 = src[srcoff + 11];
			if (left != null) {
				left[leftoff + 0] = x1;
				left[leftoff + 1] = y1;
				left[leftoff + 2] = z1;
			}
			if (right != null) {
				right[rightoff + 9] = x2;
				right[rightoff + 10] = y2;
				right[rightoff + 11] = z2;
			}
			x1 = (x1 + ctrlx1) / 2f;
			y1 = (y1 + ctrly1) / 2f;
			y1 = (z1 + ctrlz1) / 2f;
			x2 = (x2 + ctrlx2) / 2f;
			y2 = (y2 + ctrly2) / 2f;
			z2 = (z2 + ctrlz2) / 2f;
			double centerx = (ctrlx1 + ctrlx2) / 2f;
			double centery = (ctrly1 + ctrly2) / 2f;
			double centerz = (ctrlz1 + ctrlz2) / 2f;
			ctrlx1 = (x1 + centerx) / 2f;
			ctrly1 = (y1 + centery) / 2f;
			ctrlz1 = (z1 + centerz) / 2f;
			ctrlx2 = (x2 + centerx) / 2f;
			ctrly2 = (y2 + centery) / 2f;
			ctrlz2 = (z2 + centerz) / 2f;
			centerx = (ctrlx1 + ctrlx2) / 2f;
			centery = (ctrly1 + ctrly2) / 2f;
			centerz = (ctrlz1 + ctrlz2) / 2f;
			if (left != null) {
				left[leftoff + 3] = x1;
				left[leftoff + 4] = y1;
				left[leftoff + 5] = z1;
				left[leftoff + 6] = ctrlx1;
				left[leftoff + 7] = ctrly1;
				left[leftoff + 8] = ctrlz1;
				left[leftoff + 9] = centerx;
				left[leftoff + 10] = centery;
				left[leftoff + 11] = centerz;
			}
			if (right != null) {
				right[rightoff + 0] = centerx;
				right[rightoff + 1] = centery;
				right[rightoff + 2] = centerz;
				right[rightoff + 3] = ctrlx2;
				right[rightoff + 4] = ctrly2;
				right[rightoff + 5] = ctrly2;
				right[rightoff + 6] = x2;
				right[rightoff + 7] = y2;
				right[rightoff + 8] = z2;
			}
		}

		private void searchNext() {
			int level;

			if (this.holdIndex >= this.holdEnd) {
				if (!this.pathIterator.hasNext()) {
					this.done = true;
					return;
				}
				AbstractPathElement3F pathElement = this.pathIterator.next();
				this.holdType = pathElement.type;
				pathElement.toArray(this.hold);
				this.levelIndex = 0;
				this.levels[0] = 0;
			}

			switch (this.holdType) {
			case MOVE_TO:
			case LINE_TO:
				this.currentX = this.hold[0];
				this.currentY = this.hold[1];
				this.currentZ = this.hold[2];
				if (this.holdType == PathElementType.MOVE_TO) {
					this.moveX = this.currentX;
					this.moveY = this.currentY;
					this.moveZ = this.currentZ;
				}
				this.holdIndex = 0;
				this.holdEnd = 0;
				break;
			case CLOSE:
				this.currentX = this.moveX;
				this.currentY = this.moveY;
				this.currentZ = this.moveZ;
				this.holdIndex = 0;
				this.holdEnd = 0;
				break;
			case QUAD_TO:
				if (this.holdIndex >= this.holdEnd) {
					// Move the coordinates to the end of the array.
					this.holdIndex = this.hold.length - 9;
					this.holdEnd = this.hold.length - 3;
					this.hold[this.holdIndex + 0] = this.currentX;
					this.hold[this.holdIndex + 1] = this.currentY;
					this.hold[this.holdIndex + 2] = this.currentZ;
					this.hold[this.holdIndex + 3] = this.hold[0];
					this.hold[this.holdIndex + 4] = this.hold[1];
					this.hold[this.holdIndex + 5] = this.hold[2];
					this.hold[this.holdIndex + 6] = this.currentX = this.hold[3];
					this.hold[this.holdIndex + 7] = this.currentY = this.hold[4];
					this.hold[this.holdIndex + 8] = this.currentY = this.hold[5];
				}

				level = this.levels[this.levelIndex];
				while (level < this.limit) {
					if (getQuadSquaredFlatness(this.hold, this.holdIndex) < this.squaredFlatness) {
						break;
					}

					ensureHoldCapacity(6);
					subdivideQuad(
							this.hold, this.holdIndex,
							this.hold, this.holdIndex - 6,
							this.hold, this.holdIndex);
					this.holdIndex -= 6;

					// Now that we have subdivided, we have constructed
					// two curves of one depth lower than the original
					// curve.  One of those curves is in the place of
					// the former curve and one of them is in the next
					// set of held coordinate slots.  We now set both
					// curves level values to the next higher level.
					level++;
					this.levels[this.levelIndex] = level;
					this.levelIndex++;
					this.levels[this.levelIndex] = level;
				}

				// This curve segment is flat enough, or it is too deep
				// in recursion levels to try to flatten any more.  The
				// two coordinates at holdIndex+6 and holdIndex+7 now
				// contain the endpoint of the curve which can be the
				// endpoint of an approximating line segment.
				this.holdIndex += 6;
				this.levelIndex--;
				break;
			case CURVE_TO:
				if (this.holdIndex >= this.holdEnd) {
					// Move the coordinates to the end of the array.
					this.holdIndex = this.hold.length - 12;
					this.holdEnd = this.hold.length - 3;
					this.hold[this.holdIndex + 0] = this.currentX;
					this.hold[this.holdIndex + 1] = this.currentY;
					this.hold[this.holdIndex + 2] = this.currentY;
					this.hold[this.holdIndex + 3] = this.hold[0];
					this.hold[this.holdIndex + 4] = this.hold[1];
					this.hold[this.holdIndex + 5] = this.hold[2];
					this.hold[this.holdIndex + 6] = this.hold[3];
					this.hold[this.holdIndex + 7] = this.hold[4];
					this.hold[this.holdIndex + 8] = this.hold[5];
					this.hold[this.holdIndex + 9] = this.currentX = this.hold[6];
					this.hold[this.holdIndex + 10] = this.currentY = this.hold[7];
					this.hold[this.holdIndex + 11] = this.currentY = this.hold[8];
				}

				level = this.levels[this.levelIndex];
				while (level < this.limit) {
					if (getCurveSquaredFlatness(this.hold,this. holdIndex) < this.squaredFlatness) {
						break;
					}

					ensureHoldCapacity(9);
					subdivideCurve(
							this.hold, this.holdIndex,
							this.hold, this.holdIndex - 9,
							this.hold, this.holdIndex);
					this.holdIndex -= 9;

					// Now that we have subdivided, we have constructed
					// two curves of one depth lower than the original
					// curve.  One of those curves is in the place of
					// the former curve and one of them is in the next
					// set of held coordinate slots.  We now set both
					// curves level values to the next higher level.
					level++;
					this.levels[this.levelIndex] = level;
					this.levelIndex++;
					this.levels[this.levelIndex] = level;
				}

				// This curve segment is flat enough, or it is too deep
				// in recursion levels to try to flatten any more.  The
				// two coordinates at holdIndex+9 and holdIndex+10 now
				// contain the endpoint of the curve which can be the
				// endpoint of an approximating line segment.
				this.holdIndex += 9;
				this.levelIndex--;
				break;
			default:
			}
		}

		@Override
		public boolean hasNext() {
			return !this.done;
		}

		@Override
		public AbstractPathElement3F next() {
			if (this.done) {
				throw new NoSuchElementException("flattening iterator out of bounds"); //$NON-NLS-1$
			}

			AbstractPathElement3F element;
			PathElementType type = this.holdType;
			if (type!=PathElementType.CLOSE) {
				double x = this.hold[this.holdIndex + 0];
				double y = this.hold[this.holdIndex + 1];
				double z = this.hold[this.holdIndex + 2];
				if (type == PathElementType.MOVE_TO) {
					element = new AbstractPathElement3F.MovePathElement3f(x, y, z);
				}
				else {
					element = new AbstractPathElement3F.LinePathElement3f(
							this.lastNextX, this.lastNextY, this.lastNextZ,
							x, y, z);
				}
				this.lastNextX = x;
				this.lastNextY = y;
				this.lastNextZ = z;
			}
			else {
				element = new AbstractPathElement3F.ClosePathElement3f(
						this.lastNextX, this.lastNextY, this.lastNextZ,
						this.moveX, this.moveY, this.moveZ);
				this.lastNextX = this.moveX;
				this.lastNextY = this.moveY;
				this.lastNextZ = this.moveZ;
			}

			searchNext();

			return element;
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}

		@Override
		public PathWindingRule getWindingRule() {
			return this.windingRule;
		}

		@Override
		public boolean isPolyline() {
			return false; // Because the iterator flats the path, this is no curve inside.
		}

	} // class FlatteningPathIterator

	/** An collection of the points of the path.
	 *
	 * @author $Author: hjaffali$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	@SuppressWarnings("unused")
	private class PointCollection implements Collection<Point3D> {

		/**
		 */
		public PointCollection() {
			//
		}

		@Override
		public int size() {
			return Path3f.this.size();
		}

		@Override
		public boolean isEmpty() {
			return Path3f.this.size()<=0;
		}

		@Override
		public boolean contains(Object o) {
			if (o instanceof Point3D) {
				return Path3f.this.containsPoint((Point3D)o);
			}
			return false;
		}

		@Override
		public Iterator<Point3D> iterator() {
			return new PointIterator();
		}

		@Override
		public Object[] toArray() {
			return Path3f.this.toPointArray();
		}

		@SuppressWarnings("unchecked")
		@Override
		public <T> T[] toArray(T[] a) {
			Iterator<Point3D> iterator = new PointIterator();
			for(int i=0; i<a.length && iterator.hasNext(); ++i) {
				a[i] = (T)iterator.next();
			}
			return a;
		}

		@Override
		public boolean add(Point3D e) {
			if (e!=null) {
				if (Path3f.this.size()==0) {
					Path3f.this.moveTo(e.getX(), e.getY(), e.getZ());
				}
				else {
					Path3f.this.lineTo(e.getX(), e.getY(), e.getZ());
				}
				return true;
			}
			return false;
		}

		@Override
		public boolean remove(Object o) {
			if (o instanceof Point3D) {
				Point3D p = (Point3D)o;
				return Path3f.this.remove(p.getX(), p.getY(), p.getZ());
			}
			return false;
		}

		@Override
		public boolean containsAll(Collection<?> c) {
			for(Object obj : c) {
				if ((!(obj instanceof Point3D))
						||(!Path3f.this.containsPoint((Point3D)obj))) {
					return false;
				}
			}
			return true;
		}

		@Override
		public boolean addAll(Collection<? extends Point3D> c) {
			boolean changed = false;
			for(Point3D pts : c) {
				if (add(pts)) {
					changed = true;
				}
			}
			return changed;
		}

		@Override
		public boolean removeAll(Collection<?> c) {
			boolean changed = false;
			for(Object obj : c) {
				if (obj instanceof Point3D) {
					Point3D pts = (Point3D)obj;
					if (Path3f.this.remove(pts.getX(), pts.getY(), pts.getZ())) {
						changed = true;
					}
				}
			}
			return changed;
		}

		@Override
		public boolean retainAll(Collection<?> c) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void clear() {
			Path3f.this.clear();
		}

	} // class PointCollection

	/** Iterator on the points of the path.
	 *
	 * @author $Author: hjaffali$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	private class PointIterator implements Iterator<Point3D> {

		private int index = 0;
		private Point3D lastReplied = null;

		/**
		 */
		public PointIterator() {
			//
		}

		@Override
		public boolean hasNext() {
			return this.index<Path3f.this.size();
		}

		@Override
		public Point3D next() {
			try {
				this.lastReplied = Path3f.this.getPointAt(this.index++);
				return this.lastReplied;
			}
			catch( Throwable e) {
				e.printStackTrace();throw new NoSuchElementException();
			}
		}

		@Override
		public void remove() {
			Point3D p = this.lastReplied;
			this.lastReplied = null;
			if (p==null)
				throw new NoSuchElementException();
			Path3f.this.remove(p.getX(), p.getY(), p.getZ());
		}

	}

}
