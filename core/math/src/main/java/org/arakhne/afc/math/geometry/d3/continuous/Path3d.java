/* 
 * $Id$
 * 
 * Copyright (C) 2015 Stephane GALLAND, Hamza JAFFALI.
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
import org.arakhne.afc.math.geometry.d3.FunctionalPoint3D;
import org.arakhne.afc.math.geometry.d3.Path3D;
import org.arakhne.afc.math.geometry.d3.Point3D;
import org.eclipse.xtext.xbase.lib.Pure;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;

/** A generic 3D-path.
 *
 * @author $Author: hjaffali$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */

public class Path3d extends AbstractShape3F<Path3d> implements Path3D<Shape3F,AlignedBox3d,AbstractPathElement3D,PathIterator3d> {

	private static final long serialVersionUID = -8167977956565440101L;

	/** Multiple of cubic & quad curve size.
	 */
	//36 = (3+3+3)*4
	static final int GROW_SIZE = 36;


	/** Indicates if the two property arrays are strictly equals
	 * 
	 * @param array
	 * @param array2
	 * @return <code>true</code> if every Property in the first array is equals to the Property 
	 * in same index in the second array, <code>false</code> otherwise
	 */
	@Pure
	public static boolean propertyArraysEquals (Property<?>[] array, Property<?> [] array2) {
		if(array.length==array2.length) {
			for(int i=0; i<array.length; i++) {
				if(array[i]==null) {
					if(array2[i]!=null)
						return false;
				} else if(array2[i]==null) {
					return false;
				} else if(!array[i].getValue().equals(array2[i].getValue())) {
					return false;
				}
			}
			return true;
		}
		return false;
	}




	/** Replies the point on the path that is closest to the given point.
	 * <p>
	 * <strong>CAUTION:</strong> This function works only on path iterators
	 * that are replying polyline primitives, ie. if the
	 * {@link PathIterator3d#isPolyline()} of <var>pi</var> is replying
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
	public static Point3d getClosestPointTo(PathIterator3d pathIterator, double x, double y, double z) {
		Point3d closest = null;
		double bestDist = Double.POSITIVE_INFINITY;
		Point3d candidate;
		AbstractPathElement3D pe = pathIterator.next();
		Path3d subPath;

		if (pe.type != PathElementType.MOVE_TO) {
			throw new IllegalArgumentException("missing initial moveto in path definition"); //$NON-NLS-1$
		}

		candidate = new Point3d(pe.getToX(), pe.getToY(), pe.getToZ());

		while (pathIterator.hasNext()) {
			pe = pathIterator.next();

			candidate = null;

			switch(pe.type) {
			case MOVE_TO:
				candidate = new Point3d(pe.getToX(), pe.getToY(), pe.getToZ());
				break;
			case LINE_TO:

				candidate = new Point3d((new Segment3d(pe.getFromX(), pe.getFromY(), pe.getFromZ(), pe.getToX(), pe.getToY(), pe.getToZ())).getClosestPointTo(new Point3d(x,y,z)));

				break;
			case CLOSE:
				if (!pe.isEmpty()) {
					candidate = new Point3d((new Segment3d(pe.getFromX(), pe.getFromY(), pe.getFromZ(), pe.getToX(), pe.getToY(), pe.getToZ())).getClosestPointTo(new Point3d(x,y,z)));
				}
				break;
			case QUAD_TO:
				subPath = new Path3d();
				subPath.moveTo(pe.getFromX(), pe.getFromY(), pe.getFromZ());
				subPath.quadTo(
						pe.getCtrlX1(), pe.getCtrlY1(), pe.getCtrlZ1(),
						pe.getToX(), pe.getToY(), pe.getToZ());

				candidate = subPath.getClosestPointTo(new Point3d(x,y,z));
				break;
			case CURVE_TO:
				subPath = new Path3d();
				subPath.moveTo(pe.getFromX(), pe.getFromY(), pe.getFromZ());
				subPath.curveTo(
						pe.getCtrlX1(), pe.getCtrlY1(), pe.getCtrlZ1(),
						pe.getCtrlX2(), pe.getCtrlY2(), pe.getCtrlZ2(),
						pe.getToX(), pe.getToY(), pe.getToZ());

				candidate = subPath.getClosestPointTo(new Point3d(x,y,z));
				break;
			default:
				throw new IllegalStateException(
						pe.type==null ? null : pe.type.toString());
			}

			if (candidate!=null) {
				double d = candidate.getDistanceSquared(new Point3d(x,y,z));
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
	 * {@link PathIterator3d#isPolyline()} of <var>pi</var> is replying
	 * <code>true</code>.
	 * {@link #getFarthestPointTo(Point3D)} avoids this restriction.
	 * 
	 * @param pi is the iterator on the elements of the path.
	 * @param x
	 * @param y
	 * @param z
	 * @return the farthest point on the shape.
	 */
	public static Point3d getFarthestPointTo(PathIterator3d pathIterator, double x, double y, double z) {
		Point3d farthest = null;
		double bestDist = Double.NEGATIVE_INFINITY;
		Point3d candidate;
		AbstractPathElement3D pe = pathIterator.next();
		Path3d subPath;

		if (pe.type != PathElementType.MOVE_TO) {
			throw new IllegalArgumentException("missing initial moveto in path definition"); //$NON-NLS-1$
		}

		candidate = new Point3d(pe.getToX(), pe.getToY(), pe.getToZ());

		while (pathIterator.hasNext()) {
			pe = pathIterator.next();

			candidate = null;

			switch(pe.type) {
			case MOVE_TO:
				candidate = new Point3d(pe.getToX(), pe.getToY(), pe.getToZ());
				break;
			case LINE_TO:

				candidate = new Point3d((new Segment3d(pe.getFromX(), pe.getFromY(), pe.getFromZ(), pe.getToX(), pe.getToY(), pe.getToZ())).getFarthestPointTo(new Point3d(x,y,z)));

				break;
			case CLOSE:
				if (!pe.isEmpty()) {
					candidate = new Point3d((new Segment3d(pe.getFromX(), pe.getFromY(), pe.getFromZ(), pe.getToX(), pe.getToY(), pe.getToZ())).getFarthestPointTo(new Point3d(x,y,z)));
				}
				break;
			case QUAD_TO:
				subPath = new Path3d();
				subPath.moveTo(pe.getFromX(), pe.getFromY(), pe.getFromZ());
				subPath.quadTo(
						pe.getCtrlX1(), pe.getCtrlY1(), pe.getCtrlZ1(),
						pe.getToX(), pe.getToY(), pe.getToZ());

				candidate = subPath.getFarthestPointTo(new Point3d(x,y,z));
				break;
			case CURVE_TO:
				subPath = new Path3d();
				subPath.moveTo(pe.getFromX(), pe.getFromY(), pe.getFromZ());
				subPath.curveTo(
						pe.getCtrlX1(), pe.getCtrlY1(), pe.getCtrlZ1(),
						pe.getCtrlX2(), pe.getCtrlY2(), pe.getCtrlZ2(),
						pe.getToX(), pe.getToY(), pe.getToZ());

				candidate = subPath.getFarthestPointTo(new Point3d(x,y,z));
				break;
			default:
				throw new IllegalStateException(
						pe.type==null ? null : pe.type.toString());
			}

			if (candidate!=null) {
				double d = candidate.getDistanceSquared(new Point3d(x,y,z));
				if (d>bestDist) {
					bestDist = d;
					farthest = candidate;
				}
			}
		}

		return farthest;
	}

	private static boolean buildGraphicalBoundingBox(PathIterator3d iterator, AlignedBox3d box) {
		boolean foundOneLine = false;
		double xmin = Double.POSITIVE_INFINITY;
		double ymin = Double.POSITIVE_INFINITY;
		double zmin = Double.POSITIVE_INFINITY;
		double xmax = Double.NEGATIVE_INFINITY;
		double ymax = Double.NEGATIVE_INFINITY;
		double zmax = Double.NEGATIVE_INFINITY;
		AbstractPathElement3D element;
		Path3d subPath;
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
				if (element.getToZ()>zmax) zmax = element.getToZ();
				foundOneLine = true;
				break;
			case CURVE_TO:
				subPath = new Path3d();
				subPath.moveTo(element.getFromX(), element.getFromY(), element.getFromZ());
				subPath.curveTo(
						element.getCtrlX1(), element.getCtrlY1(), element.getCtrlZ1(),
						element.getCtrlX2(), element.getCtrlY2(), element.getCtrlZ2(),
						element.getToX(), element.getToY(), element.getToZ());
				if (buildGraphicalBoundingBox(
						subPath.getPathIteratorProperty(MathConstants.SPLINE_APPROXIMATION_RATIO),
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
				subPath = new Path3d();
				subPath.moveTo(element.getFromX(), element.getFromY(), element.getFromZ());
				subPath.quadTo(
						element.getCtrlX1(), element.getCtrlY1(), element.getCtrlZ1(),
						element.getToX(), element.getToY(), element.getToZ());
				if (buildGraphicalBoundingBox(
						subPath.getPathIteratorProperty(MathConstants.SPLINE_APPROXIMATION_RATIO),
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

	private boolean buildLogicalBoundingBox(AlignedBox3d box) {
		if (this.numCoordsProperty.get()>0) {
			double xmin = Double.POSITIVE_INFINITY;
			double ymin = Double.POSITIVE_INFINITY;
			double zmin = Double.POSITIVE_INFINITY;
			double xmax = Double.NEGATIVE_INFINITY;
			double ymax = Double.NEGATIVE_INFINITY;
			double zmax = Double.NEGATIVE_INFINITY;
			for(int i=0; i<this.numCoordsProperty.get(); i+= 3) {
				if (this.coordsProperty[i].get()<xmin) xmin = this.coordsProperty[i].get();
				if (this.coordsProperty[i+1].get()<ymin) ymin = this.coordsProperty[i+1].get();
				if (this.coordsProperty[i+2].get()<zmin) zmin = this.coordsProperty[i+2].get();
				if (this.coordsProperty[i].get()>xmax) xmax = this.coordsProperty[i].get();
				if (this.coordsProperty[i+1].get()>ymax) ymax = this.coordsProperty[i+1].get();
				if (this.coordsProperty[i+2].get()>zmax) zmax = this.coordsProperty[i+2].get();
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
	DoubleProperty[] coordsProperty;

	/** Number of types in the array.
	 */
	IntegerProperty numTypesProperty = new SimpleIntegerProperty(0);

	/** Number of coords in the array.
	 */
	IntegerProperty numCoordsProperty = new SimpleIntegerProperty(0);

	/** Winding rule for the path.
	 */
	PathWindingRule windingRule;

	/** Indicates if the path is empty.
	 * The path is empty when there is no point inside, or
	 * all the points are at the same coordinate, or
	 * when the path does not represents a drawable path
	 * (a path with a line or a curve).
	 */
	private BooleanProperty isEmptyProperty = new SimpleBooleanProperty(true);

	/** Indicates if the path contains base primitives
	 * (no curve).
	 */
	private BooleanProperty isPolylineProperty = new SimpleBooleanProperty(true);

	/** Buffer for the bounds of the path that corresponds
	 * to the points really on the path (eg, the pixels
	 * drawn). The control points of the curves are
	 * not considered in this bounds.
	 */
	private SoftReference<AlignedBox3d> graphicalBounds = null;

	/** Buffer for the bounds of the path that corresponds
	 * to all the points added in the path.
	 */
	private SoftReference<AlignedBox3d> logicalBounds = null;

	/**
	 */
	public Path3d() {
		this(PathWindingRule.NON_ZERO);
	}

	/**
	 * @param iterator
	 */
	public Path3d(Iterator<AbstractPathElement3D> iterator) {
		this(PathWindingRule.NON_ZERO, iterator);
	}

	/**
	 * @param windingRule1
	 */
	public Path3d(PathWindingRule windingRule1) {
		assert(windingRule1!=null);
		this.types = new PathElementType[GROW_SIZE];

		this.coordsProperty = new SimpleDoubleProperty[GROW_SIZE];
		for(int i=0; i<this.coordsProperty.length; i++) {
			this.coordsProperty[i] = new SimpleDoubleProperty();
		}
		this.windingRule = windingRule1;
	}

	/**
	 * @param windingRule1
	 * @param iterator
	 */
	public Path3d(PathWindingRule windingRule1, Iterator<AbstractPathElement3D> iterator) {
		assert(windingRule1!=null);
		this.types = new PathElementType[GROW_SIZE];

		this.coordsProperty = new SimpleDoubleProperty[GROW_SIZE];
		for(int i=0; i<this.coordsProperty.length; i++) {
			this.coordsProperty[i] = new SimpleDoubleProperty();
		}

		this.windingRule = windingRule1;
		add(iterator);
	}

	/** Construct a path from an existing Path3d. 
	 * 
	 * If copyProperties is true, we only copy the properties values of Path3d into 
	 * this properties.
	 * 
	 * If copyProperties is false, the properties of this path will have same references with the Path3d 
	 * properties in parameter. So if the Path3d in parameter changes, this path will be affected.
	 *
	 * 
	 * @param p
	 * @param copyProperties indicates if the properties must be copied or binded
	 */
	public Path3d(Path3d p , boolean copyProperties)  {
		this();

		this.coordsProperty = new DoubleProperty[p.coordsProperty.length];

		if(copyProperties) {
			for(int i=0;i<p.coordsProperty.length;i++) {
				this.coordsProperty[i] = new SimpleDoubleProperty(p.coordsProperty[i].get());
			}

			if(p.isEmptyProperty==null) {
				this.isEmptyProperty=null;
			}
			else {
				this.isEmptyProperty = new SimpleBooleanProperty(p.isEmptyProperty.get());
			}		

			if(p.isPolylineProperty==null) {
				this.isPolylineProperty=null;
			}
			else {
				this.isPolylineProperty = new SimpleBooleanProperty(p.isPolylineProperty.get());
			}

			this.numCoordsProperty.set(p.numCoordsProperty.get());
			this.numTypesProperty.set(p.numTypesProperty.get());
			this.types = p.types.clone();
			this.windingRule = p.windingRule;

			AlignedBox3d box;
			box = p.graphicalBounds==null ? null : new AlignedBox3d(p.graphicalBounds.get());
			if (box!=null) {
				this.graphicalBounds = new SoftReference<>(box.clone());
			}
			box = p.logicalBounds==null ? null : new AlignedBox3d(p.logicalBounds.get());
			if (box!=null) {
				this.logicalBounds = new SoftReference<>(box.clone());
			}

		}
		else {
			for(int i=0;i<p.coordsProperty.length;i++) {
				this.coordsProperty[i] = p.coordsProperty[i];
			}

			if(p.isEmptyProperty==null) {
				this.isEmptyProperty=null;
			}
			else {
				this.isEmptyProperty = p.isEmptyProperty;
			}		

			if(p.isPolylineProperty==null) {
				this.isPolylineProperty=null;
			}
			else {
				this.isPolylineProperty = p.isPolylineProperty;
			}

			this.numCoordsProperty = p.numCoordsProperty;
			this.numTypesProperty = p.numTypesProperty;
			this.types = p.types.clone();
			this.windingRule = p.windingRule;

			AlignedBox3d box;
			box = p.graphicalBounds==null ? null : new AlignedBox3d(p.graphicalBounds.get());
			if (box!=null) {
				this.graphicalBounds = new SoftReference<>(box.clone());
			}
			box = p.logicalBounds==null ? null : new AlignedBox3d(p.logicalBounds.get());
			if (box!=null) {
				this.logicalBounds = new SoftReference<>(box.clone());
			}

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
		for(int i=0, j=0; i<this.numCoordsProperty.get() && j<this.numTypesProperty.get();) {
			switch(this.types[j]) {
			case MOVE_TO:
			case LINE_TO:
				if (x==this.coordsProperty[i].get() && y==this.coordsProperty[i+1].get() && z==this.coordsProperty[i+2].get()) {
					this.numCoordsProperty.set(this.numCoordsProperty.get()-3);
					this.numTypesProperty.set(this.numTypesProperty.get()-1);
					System.arraycopy(this.coordsProperty, i+3, this.coordsProperty, i, this.numCoordsProperty.get());
					System.arraycopy(this.types, j+1, this.types, j, this.numTypesProperty.get());
					this.isEmptyProperty = null;
					return true;
				}
				i += 3;
				++j;
				break;
			case CURVE_TO:
				if ((x==this.coordsProperty[i].get() && y==this.coordsProperty[i+1].get() && z==this.coordsProperty[i+2].get())
						||(x==this.coordsProperty[i+3].get() && y==this.coordsProperty[i+4].get() && z==this.coordsProperty[i+5].get())
						||(x==this.coordsProperty[i+6].get() && y==this.coordsProperty[i+7].get() && z==this.coordsProperty[i+8].get())) {
					this.numCoordsProperty.set(this.numCoordsProperty.get()-9);
					this.numTypesProperty.set(this.numTypesProperty.get()-1);
					System.arraycopy(this.coordsProperty, i+9, this.coordsProperty, i, this.numCoordsProperty.get());
					System.arraycopy(this.types, j+1, this.types, j, this.numTypesProperty.get());
					this.isEmptyProperty = null;
					this.isPolylineProperty = null;
					return true;
				}
				i += 9;
				++j;
				break;
			case QUAD_TO:
				if ((x==this.coordsProperty[i].get() && y==this.coordsProperty[i+1].get() && z==this.coordsProperty[i+2].get())
						||(x==this.coordsProperty[i+3].get() && y==this.coordsProperty[i+4].get() && z==this.coordsProperty[i+5].get())) {
					this.numCoordsProperty.set(this.numCoordsProperty.get()-6);
					this.numTypesProperty.set(this.numTypesProperty.get()-1);
					System.arraycopy(this.coordsProperty, i+6, this.coordsProperty, i, this.numCoordsProperty.get());
					System.arraycopy(this.types, j+1, this.types, j, this.numTypesProperty.get());
					this.isEmptyProperty = null;
					this.isPolylineProperty = null;
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
	@Pure
	boolean containsControlPoint(Point3D p) {
		double x, y, z;
		for(int i=0; i<this.numCoordsProperty.get();) {
			x = this.coordsProperty[i++].get();
			y = this.coordsProperty[i++].get();
			z = this.coordsProperty[i++].get();
			if (x==p.getX() && y==p.getY() && z==p.getZ()) {
				return true;
			}
		}
		return false;
	}


	@Pure
	@Override
	public String toString() {
		StringBuilder b = new StringBuilder();
		b.append("["); //$NON-NLS-1$
		if (this.numCoordsProperty.get()>0) {
			b.append(this.coordsProperty[0].get());
			for(int i=1; i<this.numCoordsProperty.get(); ++i) {
				b.append(", "); //$NON-NLS-1$
				b.append(this.coordsProperty[i].get());
			}
		}
		b.append("]"); //$NON-NLS-1$
		return b.toString();
	}


	@Override
	public boolean isEmpty() {
		if (this.isEmptyProperty==null) {
			this.isEmptyProperty = new SimpleBooleanProperty(true);
			PathIterator3d pi = getPathIteratorProperty();
			AbstractPathElement3D pe;
			while (this.isEmptyProperty.get()==true && pi.hasNext()) {
				pe = pi.next();
				if (pe.isDrawable()) { 
					this.isEmptyProperty.set(false);
				}
			}
		}
		return this.isEmptyProperty.get();
	}

	@Override
	public void clear() {
		this.types = new PathElementType[GROW_SIZE];
		this.coordsProperty = new DoubleProperty[GROW_SIZE];
		this.windingRule = PathWindingRule.NON_ZERO;
		this.numCoordsProperty.set(0);
		this.numTypesProperty.set(0);
		this.isEmptyProperty.set(true);
		this.isPolylineProperty.set(true);
		this.graphicalBounds = null;
		this.logicalBounds = null;
	}

	@Pure
	@Override
	public Path3d clone() {
		Path3d clone = super.clone();
		clone.coordsProperty = this.coordsProperty.clone();
		clone.types = this.types.clone();
		clone.windingRule = this.windingRule;
		return clone;

	}

	@Pure
	@Override
	public Point3d getClosestPointTo(Point3D p) {
		return getClosestPointTo(
				getPathIteratorProperty(MathConstants.SPLINE_APPROXIMATION_RATIO),
				p.getX(), p.getY(),p.getZ());
	}

	@Pure
	@Override
	public Point3d getFarthestPointTo(Point3D p) {
		return getFarthestPointTo(
				getPathIteratorProperty(MathConstants.SPLINE_APPROXIMATION_RATIO),
				p.getX(), p.getY(), p.getZ());
	}

	@Override
	public void set(Shape3F s) {
		clear();
		add(s.getPathIteratorProperty());
	}


	@Override
	public AlignedBox3d toBoundingBox() {
		AlignedBox3d bb = this.graphicalBounds==null ? null : this.graphicalBounds.get();
		if (bb==null) {
			bb = new AlignedBox3d();
			buildGraphicalBoundingBox(
					getPathIteratorProperty(MathConstants.SPLINE_APPROXIMATION_RATIO),
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
	public AlignedBox3d toBoundingBoxWithCtrlPoints() {
		AlignedBox3d bb = this.logicalBounds==null ? null : this.logicalBounds.get();
		if (bb==null) {
			bb = new AlignedBox3d();
			buildLogicalBoundingBox(bb);
			this.logicalBounds = new SoftReference<>(bb);
		}
		return bb;
	}


	@Override
	public void toBoundingBox(AbstractBoxedShape3F<?> box) {
		AlignedBox3d bb = this.graphicalBounds==null ? null : this.graphicalBounds.get();
		if (bb==null) {
			bb = new AlignedBox3d();
			buildGraphicalBoundingBox(
					getPathIteratorProperty(MathConstants.SPLINE_APPROXIMATION_RATIO),
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
		AlignedBox3d bb = this.logicalBounds==null ? null : this.logicalBounds.get();
		if (bb==null) {
			bb = new AlignedBox3d();
			buildLogicalBoundingBox(bb);
			this.logicalBounds = new SoftReference<>(bb);
		}
		box.set(bb);
	}

	@Pure
	@Override
	public double distanceSquared(Point3D p) {
		Point3D c = getClosestPointTo(p);
		return c.getDistanceSquared(p);
	}

	@Pure
	@Override
	public double distanceL1(Point3D p) {
		Point3D c = getClosestPointTo(p);
		return c.getDistanceL1(p);
	}

	@Pure
	@Override
	public double distanceLinf(Point3D p) {
		Point3D c = getClosestPointTo(p);
		return c.getDistanceLinf(p);
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
			Point3D p = new Point3d();
			for(int i=0; i<this.numCoordsProperty.get();) {
				p.set(this.coordsProperty[i].get(), this.coordsProperty[i+1].get(), this.coordsProperty[i+2].get());
				transform.transform(p);
				this.coordsProperty[i++].set(p.getX());
				this.coordsProperty[i++].set(p.getY());
				this.coordsProperty[i++].set(p.getZ());
			}
			this.graphicalBounds = null;
			this.logicalBounds = null;
		}
	}

	@Pure
	@Override
	public Shape3F createTransformedShape(Transform3D transform) {
		Path3d newP = new Path3d(this,true);
		newP.transform(transform);

		return newP;
	}

	@Override
	public void translate(double dx, double dy, double dz) {
		for(int i=0; i<this.numCoordsProperty.get();) {
			this.coordsProperty[i].set(this.coordsProperty[i].get()+dx);
			i++;
			this.coordsProperty[i].set(this.coordsProperty[i].get()+dy);
			i++;
			this.coordsProperty[i].set(this.coordsProperty[i].get()+dz);
			i++;
		}
		AlignedBox3d bb;
		bb = this.logicalBounds==null ? null : this.logicalBounds.get();
		if (bb!=null) bb.translate(dx, dy, dz);
		bb = this.graphicalBounds==null ? null : this.graphicalBounds.get();
		if (bb!=null) bb.translate(dx, dy, dz);
	}


	@Pure
	@Override
	public boolean intersects(AbstractBoxedShape3F<?> s) {
		if (s.isEmpty()) return false;
		int mask = (this.windingRule == PathWindingRule.NON_ZERO ? -1 : 2);
		boolean intersects = false;
		PathIterator3d pi = getPathIteratorProperty(MathConstants.SPLINE_APPROXIMATION_RATIO);

		AbstractPathElement3D pathElement = pi.next();

		if (pathElement.type != PathElementType.MOVE_TO) {
			throw new IllegalArgumentException("missing initial moveto in path definition"); //$NON-NLS-1$
		}

		Path3d subPath;
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
						s.getMinX(), s.getMinY(), s.getMinZ(), 
						s.getMaxX(), s.getMaxY(), s.getMaxZ());

				curx = endx;
				cury = endy;
				curz = endz;
				break;
			case QUAD_TO:
				endx = pathElement.getToX();
				endy = pathElement.getToY();
				endz = pathElement.getToZ();
				subPath = new Path3d();
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
				subPath = new Path3d();
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
							s.getMinX(), s.getMinY(), s.getMinZ(), 
							s.getMaxX(), s.getMaxY(), s.getMaxZ());
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


	@Pure
	@Override
	public boolean intersects(Path3d p) {
		if (p.isEmpty()) return false;
		int mask = (this.windingRule == PathWindingRule.NON_ZERO ? -1 : 2);
		boolean intersects = false;
		PathIterator3d pi = getPathIteratorProperty(MathConstants.SPLINE_APPROXIMATION_RATIO);

		AbstractPathElement3D pathElement = pi.next();

		if (pathElement.type != PathElementType.MOVE_TO) {
			throw new IllegalArgumentException("missing initial moveto in path definition"); //$NON-NLS-1$
		}

		Path3d subPath;
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
				subPath = new Path3d();
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
				subPath = new Path3d();
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

	@Pure
	@Override
	public boolean intersects(Path3f p) {
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


	@Pure
	@Override
	public boolean intersects(AbstractSphere3F s) {
		if (s.isEmpty()) return false;
		int mask = (this.windingRule == PathWindingRule.NON_ZERO ? -1 : 2);
		boolean intersects = false;
		PathIterator3d pi = getPathIteratorProperty(MathConstants.SPLINE_APPROXIMATION_RATIO);

		AbstractPathElement3D pathElement = pi.next();

		if (pathElement.type != PathElementType.MOVE_TO) {
			throw new IllegalArgumentException("missing initial moveto in path definition"); //$NON-NLS-1$
		}

		Path3d subPath;
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
				subPath = new Path3d();
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
				subPath = new Path3d();
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

	@Pure
	@Override
	public boolean intersects(AbstractSegment3F s) {
		if (s.isEmpty()) return false;
		int mask = (this.windingRule == PathWindingRule.NON_ZERO ? -1 : 2);
		boolean intersects = false;
		PathIterator3d pi = getPathIteratorProperty(MathConstants.SPLINE_APPROXIMATION_RATIO);

		AbstractPathElement3D pathElement = pi.next();

		if (pathElement.type != PathElementType.MOVE_TO) {
			throw new IllegalArgumentException("missing initial moveto in path definition"); //$NON-NLS-1$
		}

		Path3d subPath;
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
				subPath = new Path3d();
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
				subPath = new Path3d();
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


	@Pure
	@Override
	public boolean intersects(AbstractTriangle3F triangle) {
		if (triangle.isEmpty()) return false;
		int mask = (this.windingRule == PathWindingRule.NON_ZERO ? -1 : 2);
		boolean intersects = false;
		PathIterator3d pi = getPathIteratorProperty(MathConstants.SPLINE_APPROXIMATION_RATIO);

		AbstractPathElement3D pathElement = pi.next();

		if (pathElement.type != PathElementType.MOVE_TO) {
			throw new IllegalArgumentException("missing initial moveto in path definition"); //$NON-NLS-1$
		}

		Path3d subPath;
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

				intersects = triangle.intersects(new Segment3f(
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
				subPath = new Path3d();
				subPath.moveTo(curx, cury,curz);
				subPath.quadTo(
						pathElement.getCtrlX1(), pathElement.getCtrlY1(), pathElement.getCtrlZ1(),
						endx, endy, endz);

				intersects = subPath.intersects(triangle);

				curx = endx;
				cury = endy;
				curz = endz;
				break;
			case CURVE_TO:
				endx = pathElement.getToX();
				endy = pathElement.getToY();
				endz = pathElement.getToZ();
				subPath = new Path3d();
				subPath.moveTo(curx, cury,curz);
				subPath.curveTo(
						pathElement.getCtrlX1(), pathElement.getCtrlY1(), pathElement.getCtrlZ1(),
						pathElement.getCtrlX2(), pathElement.getCtrlY2(), pathElement.getCtrlZ2(),
						endx, endy, endz);

				intersects = subPath.intersects(triangle);

				curx = endx;
				cury = endy;
				curz = endz;
				break;
			case CLOSE:
				if (curx != movx || cury != movy || curz != movz) {
					intersects = triangle.intersects(new Segment3f(
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


	@Pure
	@Override
	public boolean intersects(AbstractCapsule3F s) {
		if (s.isEmpty()) return false;
		int mask = (this.windingRule == PathWindingRule.NON_ZERO ? -1 : 2);
		boolean intersects = false;
		PathIterator3d pi = getPathIteratorProperty(MathConstants.SPLINE_APPROXIMATION_RATIO);

		AbstractPathElement3D pathElement = pi.next();

		if (pathElement.type != PathElementType.MOVE_TO) {
			throw new IllegalArgumentException("missing initial moveto in path definition"); //$NON-NLS-1$
		}

		Path3d subPath;
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
				subPath = new Path3d();
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
				subPath = new Path3d();
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

	@Pure
	@Override
	public boolean intersects(AbstractOrientedBox3F s) {
		if (s.isEmpty()) return false;
		int mask = (this.windingRule == PathWindingRule.NON_ZERO ? -1 : 2);
		boolean intersects = false;
		PathIterator3d pi = getPathIteratorProperty(MathConstants.SPLINE_APPROXIMATION_RATIO);

		AbstractPathElement3D pathElement = pi.next();

		if (pathElement.type != PathElementType.MOVE_TO) {
			throw new IllegalArgumentException("missing initial moveto in path definition"); //$NON-NLS-1$
		}

		Path3d subPath;
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
				subPath = new Path3d();
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
				subPath = new Path3d();
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
	@Pure
	public boolean intersects(Plane3D<?> p) {
		int mask = (this.windingRule == PathWindingRule.NON_ZERO ? -1 : 2);
		boolean intersects = false;
		PathIterator3d pi = getPathIteratorProperty(MathConstants.SPLINE_APPROXIMATION_RATIO);

		AbstractPathElement3D pathElement = pi.next();

		if (pathElement.type != PathElementType.MOVE_TO) {
			throw new IllegalArgumentException("missing initial moveto in path definition"); //$NON-NLS-1$
		}

		Path3d subPath;
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

				subPath = new Path3d();
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
				subPath = new Path3d();
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
		if (this.isPolylineProperty==null) {
			this.isPolylineProperty.set(true);
			PathIterator3d pi = getPathIteratorProperty();
			AbstractPathElement3D pe;
			PathElementType t;
			while (this.isPolylineProperty.get()==true && pi.hasNext()) {
				pe = pi.next();
				t = pe.getType();
				if (t==PathElementType.CURVE_TO || t==PathElementType.QUAD_TO) { 
					this.isPolylineProperty.set(false);
				}
			}
		}
		return this.isPolylineProperty.get();
	}

	/**
	 * {@inheritDoc}
	 */
	@Pure
	@Override
	public boolean contains(double x, double y, double z) {
		AlignedBox3d ab = this.toBoundingBox();
		return ab.contains(new Point3f(x,y,z));
	}

	@Pure
	public boolean equals(Path3d path) {
		return (this.numCoordsProperty.get()==path.numCoordsProperty.get()
				&& this.numTypesProperty.get()==path.numTypesProperty.get()
				&& propertyArraysEquals(this.coordsProperty, path.coordsProperty)
				&& Arrays.equals(this.types, path.types)
				&& this.windingRule==path.windingRule);
	}

	@Pure
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Path3d) {
			Path3d path = (Path3d)obj;
			return (this.numCoordsProperty.get()==path.numCoordsProperty.get()
					&& this.numTypesProperty.get()==path.numTypesProperty.get()
					&& propertyArraysEquals(this.coordsProperty, path.coordsProperty)
					&& Arrays.equals(this.types, path.types)
					&& this.windingRule==path.windingRule);
		}
		else if (obj instanceof Path3f) {
			Path3f path = (Path3f)obj;
			return (this.numCoordsProperty.get()==path.numCoords
					&& this.numTypesProperty.get()==path.numTypes
					&& this.toString().equals(path.toString())
					&& Arrays.equals(this.types, path.types)
					&& this.windingRule==path.windingRule);
		}
		return false;
	}


	@Pure
	@Override
	public int hashCode() {
		long bits = 1L;
		bits = 31L * bits + this.numCoordsProperty.get();
		bits = 31L * bits + this.numTypesProperty.get();
		bits = 31L * bits + Arrays.hashCode(this.coordsProperty);
		bits = 31L * bits + Arrays.hashCode(this.types);
		bits = 31L * bits + this.windingRule.ordinal();
		return (int) (bits ^ (bits >> 32));
	}

	@Pure
	@Override
	public PathIterator3f getPathIterator(double flatness) {
		return new Path3f.FlatteningPathIterator3f(getWindingRule(), getPathIterator(null), flatness, 10);
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
	@Pure
	public PathIterator3f getPathIterator(Transform3D transform, double flatness) {
		return new Path3f.FlatteningPathIterator3f(getWindingRule(), getPathIterator(transform), flatness, 10);
	}

	/** {@inheritDoc}
	 */
	@Pure
	@Override
	public PathIterator3f getPathIterator(Transform3D transform) {
		if (transform == null) {
			return new CopyPathIterator3f();
		}
		return new TransformPathIterator3f(transform);
	}


	@Pure
	@Override
	public PathIterator3d getPathIteratorProperty(double flatness) {
		return new FlatteningPathIterator3d(getWindingRule(), getPathIteratorProperty(null), flatness, 10);
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
	@Pure
	public PathIterator3d getPathIteratorProperty(Transform3D transform, double flatness) {
		return new FlatteningPathIterator3d(getWindingRule(), getPathIteratorProperty(transform), flatness, 10);
	}

	/** {@inheritDoc}
	 */
	@Pure
	@Override
	public PathIterator3d getPathIteratorProperty(Transform3D transform) {
		if (transform == null) {
			return new CopyPathIterator3d();
		}
		return new TransformPathIterator3d(transform);
	}

	@Pure
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
	public void add(Iterator<AbstractPathElement3D> iterator) {
		AbstractPathElement3D element;
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

	/**Add the element in parameter into this path.
	 * 
	 * If the element changes, the path will not be affected.
	 * 
	 * @param pathElement
	 */
	public void add(AbstractPathElement3D element) {

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

	/** Remove the last action.
	 */
	public void removeLast() {
		if (this.numTypesProperty.get()>0) {
			switch(this.types[this.numTypesProperty.get()-1]) {
			case CLOSE:
				// no coord to remove
				break;
			case MOVE_TO:
			case LINE_TO:
				this.numCoordsProperty.set(this.numCoordsProperty.get()-3);
				break;
			case CURVE_TO:
				this.numCoordsProperty.set(this.numCoordsProperty.get()-9);
				this.isPolylineProperty = null;
				break;
			case QUAD_TO:
				this.numCoordsProperty.set(this.numCoordsProperty.get()-6);
				this.isPolylineProperty = null;
				break;
			default:
				throw new IllegalStateException();
			}
			this.numTypesProperty.set(this.numTypesProperty.get()-1);
			this.isEmptyProperty = null;
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
		if (this.numCoordsProperty.get()>=3) {
			this.coordsProperty[this.numCoordsProperty.get()-3].set(x);
			this.coordsProperty[this.numCoordsProperty.get()-2].set(y);
			this.coordsProperty[this.numCoordsProperty.get()-1].set(z);
			this.graphicalBounds = null;
			this.logicalBounds = null;
		}
	}
	
	/** Change the coordinates of the last inserted point.
	 * 
	 * If the point in parameter is modified, the path will be changed also.
	 * 
	 * @param point
	 */
	public void setLastPoint(Point3d point) {
		if (this.numCoordsProperty.get()>=3) {
			this.coordsProperty[this.numCoordsProperty.get()-3] = point.xProperty;
			this.coordsProperty[this.numCoordsProperty.get()-2] = point.yProperty;
			this.coordsProperty[this.numCoordsProperty.get()-1] = point.zProperty;
			this.graphicalBounds = null;
			this.logicalBounds = null;
		}
	}

	private void ensureSlots(boolean needMove, int n) {
		if (needMove && this.numTypesProperty.get()==0) {
			throw new IllegalStateException("missing initial moveto in path definition"); //$NON-NLS-1$
		}
		if (this.types.length==this.numTypesProperty.get()) {
			this.types = Arrays.copyOf(this.types, this.types.length+GROW_SIZE);
		}
		while ((this.numCoordsProperty.get() + n)>=this.coordsProperty.length) {
			this.coordsProperty = Arrays.copyOf(this.coordsProperty, this.coordsProperty.length+GROW_SIZE);
		}

		for( int i=0; i<this.coordsProperty.length; i++) {
			if(this.coordsProperty[i]==null)
				this.coordsProperty[i] = new SimpleDoubleProperty();
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
		if (this.numTypesProperty.get()>0 && this.types[this.numTypesProperty.get()-1]==PathElementType.MOVE_TO) {
			this.coordsProperty[this.numCoordsProperty.get()-3].set(x);
			this.coordsProperty[this.numCoordsProperty.get()-2].set(y);
			this.coordsProperty[this.numCoordsProperty.get()-1].set(z);
		}
		else {
			ensureSlots(false, 3);
			this.types[this.numTypesProperty.get()] = PathElementType.MOVE_TO;
			this.numTypesProperty.set(this.numTypesProperty.get()+1);

			this.coordsProperty[this.numCoordsProperty.get()].set(x);
			this.numCoordsProperty.set(this.numCoordsProperty.get()+1);

			this.coordsProperty[this.numCoordsProperty.get()].set(y);
			this.numCoordsProperty.set(this.numCoordsProperty.get()+1);

			this.coordsProperty[this.numCoordsProperty.get()].set(z);
			this.numCoordsProperty.set(this.numCoordsProperty.get()+1);
		}
		this.graphicalBounds = null;
		this.logicalBounds = null;
	}

	/**
	 * Adds a point to the path by moving to the specified
	 * coordinates specified in point in paramater.
	 * 
	 * We store the property here, and not the values. So when the point changes,
	 * the path will be automatically updated.
	 *
	 * @param point the specified point
	 */
	public void moveTo(Point3d point) {
		if (this.numTypesProperty.get()>0 && this.types[this.numTypesProperty.get()-1]==PathElementType.MOVE_TO) {
			this.coordsProperty[this.numCoordsProperty.get()-3] = point.xProperty;
			this.coordsProperty[this.numCoordsProperty.get()-2] = point.yProperty;
			this.coordsProperty[this.numCoordsProperty.get()-1] = point.zProperty;
		}
		else {
			ensureSlots(false, 3);
			this.types[this.numTypesProperty.get()] = PathElementType.MOVE_TO;
			this.numTypesProperty.set(this.numTypesProperty.get()+1);

			this.coordsProperty[this.numCoordsProperty.get()] = point.xProperty;
			this.numCoordsProperty.set(this.numCoordsProperty.get()+1);

			this.coordsProperty[this.numCoordsProperty.get()] = point.yProperty;
			this.numCoordsProperty.set(this.numCoordsProperty.get()+1);

			this.coordsProperty[this.numCoordsProperty.get()] = point.zProperty;
			this.numCoordsProperty.set(this.numCoordsProperty.get()+1);
		}
		this.graphicalBounds = null;
		this.logicalBounds = null;
	}

	/**
	 * Adds a point to the path by drawing a straight line from the
	 * current coordinates to the new specified coordinates
	 * specified in the point in paramater.
	 *
	 * We store the property here, and not the value. So when the point changes,
	 * the path will be automatically updated.
	 *
	 * @param point the specified point
	 */
	public void lineTo(Point3d point) {
		ensureSlots(true, 3);
		this.types[this.numTypesProperty.get()] = PathElementType.LINE_TO;
		this.numTypesProperty.set(this.numTypesProperty.get()+1);

		this.coordsProperty[this.numCoordsProperty.get()] = point.xProperty;
		this.numCoordsProperty.set(this.numCoordsProperty.get()+1);

		this.coordsProperty[this.numCoordsProperty.get()] = point.yProperty;
		this.numCoordsProperty.set(this.numCoordsProperty.get()+1);

		this.coordsProperty[this.numCoordsProperty.get()] = point.zProperty;
		this.numCoordsProperty.set(this.numCoordsProperty.get()+1);

		this.isEmptyProperty = null;
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
		this.types[this.numTypesProperty.get()] = PathElementType.LINE_TO;
		this.numTypesProperty.set(this.numTypesProperty.get()+1);

		this.coordsProperty[this.numCoordsProperty.get()].set(x);
		this.numCoordsProperty.set(this.numCoordsProperty.get()+1);

		this.coordsProperty[this.numCoordsProperty.get()].set(y);
		this.numCoordsProperty.set(this.numCoordsProperty.get()+1);

		this.coordsProperty[this.numCoordsProperty.get()].set(z);
		this.numCoordsProperty.set(this.numCoordsProperty.get()+1);

		this.isEmptyProperty = null;
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
		this.types[this.numTypesProperty.get()] = PathElementType.QUAD_TO;
		this.numTypesProperty.set(this.numTypesProperty.get()+1);

		this.coordsProperty[this.numCoordsProperty.get()].set(x1);
		this.numCoordsProperty.set(this.numCoordsProperty.get()+1);

		this.coordsProperty[this.numCoordsProperty.get()].set(y1);
		this.numCoordsProperty.set(this.numCoordsProperty.get()+1);

		this.coordsProperty[this.numCoordsProperty.get()].set(z1);
		this.numCoordsProperty.set(this.numCoordsProperty.get()+1);


		this.coordsProperty[this.numCoordsProperty.get()].set(x2);
		this.numCoordsProperty.set(this.numCoordsProperty.get()+1);

		this.coordsProperty[this.numCoordsProperty.get()].set(y2);
		this.numCoordsProperty.set(this.numCoordsProperty.get()+1);

		this.coordsProperty[this.numCoordsProperty.get()].set(z2);
		this.numCoordsProperty.set(this.numCoordsProperty.get()+1);


		this.isEmptyProperty = null;
		this.isPolylineProperty.set(false);
		this.graphicalBounds = null;
		this.logicalBounds = null;
	}

	/**
	 * Adds a curved segment, defined by two new points, to the path by
	 * drawing a Quadratic curve that intersects both the current
	 * coordinates and the specified endPoint,
	 * using the specified controlPoint as a quadratic
	 * parametric control point.
	 * All coordinates are specified in Point3d.
	 *
	 * We store the property here, and not the value. So when the points changes,
	 * the path will be automatically updated.
	 *
	 * @param controlPoint the quadratic control point
	 * @param endPoint the final end point
	 */
	public void quadTo(Point3d controlPoint, Point3d endPoint) {
		ensureSlots(true, 6);
		this.types[this.numTypesProperty.get()] = PathElementType.QUAD_TO;
		this.numTypesProperty.set(this.numTypesProperty.get()+1);

		this.coordsProperty[this.numCoordsProperty.get()] = controlPoint.xProperty;
		this.numCoordsProperty.set(this.numCoordsProperty.get()+1);

		this.coordsProperty[this.numCoordsProperty.get()] = controlPoint.yProperty;
		this.numCoordsProperty.set(this.numCoordsProperty.get()+1);

		this.coordsProperty[this.numCoordsProperty.get()] = controlPoint.zProperty;
		this.numCoordsProperty.set(this.numCoordsProperty.get()+1);


		this.coordsProperty[this.numCoordsProperty.get()] = endPoint.xProperty;
		this.numCoordsProperty.set(this.numCoordsProperty.get()+1);

		this.coordsProperty[this.numCoordsProperty.get()] = endPoint.yProperty;
		this.numCoordsProperty.set(this.numCoordsProperty.get()+1);

		this.coordsProperty[this.numCoordsProperty.get()] = endPoint.zProperty;
		this.numCoordsProperty.set(this.numCoordsProperty.get()+1);


		this.isEmptyProperty = null;
		this.isPolylineProperty.set(false);
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

		this.types[this.numTypesProperty.get()] = PathElementType.CURVE_TO;
		this.numTypesProperty.set(this.numTypesProperty.get()+1);


		this.coordsProperty[this.numCoordsProperty.get()].set(x1);
		this.numCoordsProperty.set(this.numCoordsProperty.get()+1);

		this.coordsProperty[this.numCoordsProperty.get()].set(y1);
		this.numCoordsProperty.set(this.numCoordsProperty.get()+1);

		this.coordsProperty[this.numCoordsProperty.get()].set(z1);
		this.numCoordsProperty.set(this.numCoordsProperty.get()+1);


		this.coordsProperty[this.numCoordsProperty.get()].set(x2);
		this.numCoordsProperty.set(this.numCoordsProperty.get()+1);

		this.coordsProperty[this.numCoordsProperty.get()].set(y2);
		this.numCoordsProperty.set(this.numCoordsProperty.get()+1);

		this.coordsProperty[this.numCoordsProperty.get()].set(z2);
		this.numCoordsProperty.set(this.numCoordsProperty.get()+1);


		this.coordsProperty[this.numCoordsProperty.get()].set(x3);
		this.numCoordsProperty.set(this.numCoordsProperty.get()+1);

		this.coordsProperty[this.numCoordsProperty.get()].set(y3);
		this.numCoordsProperty.set(this.numCoordsProperty.get()+1);

		this.coordsProperty[this.numCoordsProperty.get()].set(z3);
		this.numCoordsProperty.set(this.numCoordsProperty.get()+1);

		this.isEmptyProperty = null;
		this.isPolylineProperty.set(false);
		this.graphicalBounds = null;
		this.logicalBounds = null;
	}


	/**
	 * Adds a curved segment, defined by three new points, to the path by
	 * drawing a B&eacute;zier curve that intersects both the current
	 * coordinates and the specified endPoint,
	 * using the specified points controlPoint1 and controlPoint2 as
	 * B&eacute;zier control points.
	 * All coordinates are specified in Point3d.
	 *
	 * We store the property here, and not the value. So when the points changes,
	 * the path will be automatically updated.
	 *
	 * @param controlPoint1 the first B&eacute;zier control point
	 * @param controlPoint2 the second B&eacute;zier control point
	 * @param endPoint the final end point
	 */
	public void curveTo(Point3d controlPoint1,
			Point3d controlPoint2,
			Point3d endPoint) {
		ensureSlots(true, 9);

		this.types[this.numTypesProperty.get()] = PathElementType.CURVE_TO;
		this.numTypesProperty.set(this.numTypesProperty.get()+1);


		this.coordsProperty[this.numCoordsProperty.get()] = controlPoint1.xProperty;
		this.numCoordsProperty.set(this.numCoordsProperty.get()+1);

		this.coordsProperty[this.numCoordsProperty.get()] = controlPoint1.yProperty;
		this.numCoordsProperty.set(this.numCoordsProperty.get()+1);

		this.coordsProperty[this.numCoordsProperty.get()] = controlPoint1.zProperty;
		this.numCoordsProperty.set(this.numCoordsProperty.get()+1);


		this.coordsProperty[this.numCoordsProperty.get()] = controlPoint2.xProperty;
		this.numCoordsProperty.set(this.numCoordsProperty.get()+1);

		this.coordsProperty[this.numCoordsProperty.get()] = controlPoint2.yProperty;
		this.numCoordsProperty.set(this.numCoordsProperty.get()+1);

		this.coordsProperty[this.numCoordsProperty.get()] = controlPoint2.zProperty;
		this.numCoordsProperty.set(this.numCoordsProperty.get()+1);


		this.coordsProperty[this.numCoordsProperty.get()] = endPoint.xProperty;
		this.numCoordsProperty.set(this.numCoordsProperty.get()+1);

		this.coordsProperty[this.numCoordsProperty.get()] = endPoint.yProperty;
		this.numCoordsProperty.set(this.numCoordsProperty.get()+1);

		this.coordsProperty[this.numCoordsProperty.get()] = endPoint.zProperty;
		this.numCoordsProperty.set(this.numCoordsProperty.get()+1);

		this.isEmptyProperty = null;
		this.isPolylineProperty.set(false);
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
		if (this.numTypesProperty.get()<=0 ||
				(this.types[this.numTypesProperty.get()-1]!=PathElementType.CLOSE
				&&this.types[this.numTypesProperty.get()-1]!=PathElementType.MOVE_TO)) {
			ensureSlots(true, 0);
			this.types[this.numTypesProperty.get()] = PathElementType.CLOSE;
			this.numTypesProperty.set(this.numTypesProperty.get()+1);
		}
	}

	/** Replies the number of points in the path.
	 *
	 * @return the number of points in the path.
	 */
	@Pure
	public int size() {
		return this.numCoordsProperty.get()/3;
	}

	/** Replies the total lentgh of the path.
	 *
	 * @return the length of the path.
	 */
	//FIXME TO BE IMPLEMENTED IN POLYLINE
	public double length() {

		if (this.isEmpty()) return 0;

		double length = 0;

		PathIterator3d pi = getPathIteratorProperty(MathConstants.SPLINE_APPROXIMATION_RATIO);

		AbstractPathElement3D pathElement = pi.next();

		if (pathElement.type != PathElementType.MOVE_TO) {
			throw new IllegalArgumentException("missing initial moveto in path definition"); //$NON-NLS-1$
		}

		Path3d subPath;
		double curx, cury, curz, movx, movy, movz, endx, endy, endz;
		curx = movx = pathElement.getToX();
		cury = movy = pathElement.getToY();
		curz = movz = pathElement.getToZ();

		while (pi.hasNext()) {
			pathElement = pi.next();

			switch (pathElement.type) {
			case MOVE_TO: 
				movx = curx = pathElement.getToX();
				movy = cury = pathElement.getToY();
				movz = curz = pathElement.getToZ();
				break;
			case LINE_TO:
				endx = pathElement.getToX();
				endy = pathElement.getToY();
				endz = pathElement.getToZ();

				length += FunctionalPoint3D.distancePointPoint(
						curx, cury, curz,  
						endx, endy, endz);

				curx = endx;
				cury = endy;
				curz = endz;
				break;
			case QUAD_TO:
				endx = pathElement.getToX();
				endy = pathElement.getToY();
				endz = pathElement.getToZ();
				subPath = new Path3d();
				subPath.moveTo(curx, cury, curz);
				subPath.quadTo(
						pathElement.getCtrlX1(), pathElement.getCtrlY1(), pathElement.getCtrlZ1(),
						endx, endy, endz);

				length += subPath.length();

				curx = endx;
				cury = endy;
				curz = endz;
				break;
			case CURVE_TO:
				endx = pathElement.getToX();
				endy = pathElement.getToY();
				endz = pathElement.getToZ();
				subPath = new Path3d();
				subPath.moveTo(curx, cury, curz);
				subPath.curveTo(
						pathElement.getCtrlX1(), pathElement.getCtrlY1(), pathElement.getCtrlZ1(),
						pathElement.getCtrlX2(), pathElement.getCtrlY2(), pathElement.getCtrlZ2(),
						endx, endy, endz);

				length += subPath.length();

				curx = endx;
				cury = endy;
				curz = endz;
				break;
			case CLOSE:
				if (curx != movx || cury != movy || curz != movz) {
					length += FunctionalPoint3D.distancePointPoint(
							curx, cury, curz, 
							movx, movy, movz);
				}

				curx = movx;
				cury = movy;
				cury = movz;
				break;
			default:
			}

		}

		return length;
	}

	/** Replies the coordinates of this path in an array of
	 * double precision floating-point numbers.
	 * 
	 * @return the coordinates.
	 */
	@Pure
	public final double[] toDoubleArray() {
		return toDoubleArray(null);
	}

	/** Replies the coordinates of this path in an array of
	 * double precision floating-point numbers.
	 * 
	 * @param transform is the transformation to apply to all the coordinates.
	 * @return the coordinates.
	 */
	@Pure
	public double[] toDoubleArray(Transform3D transform) {
		double[] clone = new double[this.numCoordsProperty.get()];
		if (transform==null) {
			for(int i=0; i<this.numCoordsProperty.get(); ++i) {
				clone[i] = this.coordsProperty[i].get();
			}
		}
		else {
			Point3f p = new Point3f();
			for(int i=0; i<clone.length;) {
				p.x = this.coordsProperty[i].get();
				p.y = this.coordsProperty[i+1].get();
				p.y = this.coordsProperty[i+2].get();
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
	@Pure
	public final Point3D[] toPointArray() {
		return toPointArray(null);
	}

	/** Replies the points of this path in an array.
	 * 
	 * @param transform is the transformation to apply to all the points.
	 * @return the points.
	 */
	@Pure
	public Point3D[] toPointArray(Transform3D transform) {
		Point3D[] clone = new Point3D[this.numCoordsProperty.get()/3];
		if (transform==null) {
			for(int i=0, j=0; j<this.numCoordsProperty.get(); ++i) {
				clone[i] = new Point3f(
						this.coordsProperty[j++].get(),
						this.coordsProperty[j++].get(),
						this.coordsProperty[j++].get());
			}
		}
		else {
			for(int i=0, j=0; j<clone.length; ++i) {
				clone[i] = new Point3f(
						this.coordsProperty[j++].get(),
						this.coordsProperty[j++].get(),
						this.coordsProperty[j++].get());
				transform.transform(clone[i]);
			}
		}
		return clone;
	}

	/** Replies the point at the given index.
	 * The index is in [0;{@link #size()}).
	 *
	 * If the returned point is modified, the path will be changed also.
	 *
	 * @param index
	 * @return the point at the given index.
	 */
	@Pure
	public Point3d getPointAt(int index) {
		Point3d point = new Point3d();
		point.xProperty = this.coordsProperty[index*3];
		point.yProperty = this.coordsProperty[index*3+1];
		point.zProperty = this.coordsProperty[index*3+2];

		return point;
	}

	/** Replies the last point in the path.
	 *
	 * If the returned point is modified, the path will be changed also.
	 *
	 * @return the last point.
	 */
	@Pure
	public Point3d getCurrentPoint() {
		Point3d point = new Point3d();
		point.xProperty = this.coordsProperty[this.numCoordsProperty.get()-3];
		point.yProperty = this.coordsProperty[this.numCoordsProperty.get()-2];
		point.zProperty = this.coordsProperty[this.numCoordsProperty.get()-1];

		return point;
	}
	//-----------------------------------------------------------------------------



	/** A path iterator that does not transform the coordinates.
	 *
	 * @author $Author: hjaffali$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	private class CopyPathIterator3d implements PathIterator3d {

		private final Point3D p1 = new Point3d();
		private final Point3D p2 = new Point3d();
		private IntegerProperty iTypeProperty = new SimpleIntegerProperty(0);
		private IntegerProperty iCoordProperty = new SimpleIntegerProperty(0);
		private DoubleProperty movexProperty, moveyProperty, movezProperty;

		/**
		 */
		public CopyPathIterator3d() {
			this.movexProperty = new SimpleDoubleProperty();
			this.moveyProperty = new SimpleDoubleProperty();
			this.movezProperty = new SimpleDoubleProperty();
		}

		@Pure
		@Override
		public boolean hasNext() {
			return this.iTypeProperty.get()<Path3d.this.numTypesProperty.get();
		}

		@Override
		public AbstractPathElement3D next() {
			int type = this.iTypeProperty.get();
			if (this.iTypeProperty.get()>=Path3d.this.numTypesProperty.get()) {
				throw new NoSuchElementException();
			}
			AbstractPathElement3D element = null;
			switch(Path3d.this.types[type]) {
			case MOVE_TO:
				if (this.iCoordProperty.get()+3>Path3d.this.numCoordsProperty.get()) {
					throw new NoSuchElementException();
				}
				this.movexProperty.set(Path3d.this.coordsProperty[this.iCoordProperty.get()].get());
				this.iCoordProperty.set(this.iCoordProperty.get()+1);

				this.moveyProperty.set(Path3d.this.coordsProperty[this.iCoordProperty.get()].get());
				this.iCoordProperty.set(this.iCoordProperty.get()+1);

				this.movezProperty.set(Path3d.this.coordsProperty[this.iCoordProperty.get()].get());
				this.iCoordProperty.set(this.iCoordProperty.get()+1);

				this.p2.set(this.movexProperty.get(), this.moveyProperty.get(), this.movezProperty.get());
				element = new AbstractPathElement3D.MovePathElement3d(
						this.p2.getX(), this.p2.getY(), this.p2.getZ());
				break;
			case LINE_TO:
				if (this.iCoordProperty.get()+3>Path3d.this.numCoordsProperty.get()) {
					throw new NoSuchElementException();
				}
				this.p1.set(this.p2);
				this.p2.set(
						Path3d.this.coordsProperty[this.iCoordProperty.get()].get(),
						Path3d.this.coordsProperty[this.iCoordProperty.get()+1].get(),
						Path3d.this.coordsProperty[this.iCoordProperty.get()+2].get());
				this.iCoordProperty.set(this.iCoordProperty.get()+3);

				element = new AbstractPathElement3D.LinePathElement3d(
						this.p1.getX(), this.p1.getY(), this.p1.getZ(),
						this.p2.getX(), this.p2.getY(), this.p2.getZ());
				break;
			case QUAD_TO:
			{
				if (this.iCoordProperty.get()+6>Path3d.this.numCoordsProperty.get()) {
					throw new NoSuchElementException();
				}
				this.p1.set(this.p2);
				double ctrlx = Path3d.this.coordsProperty[this.iCoordProperty.get()].get();
				double ctrly = Path3d.this.coordsProperty[this.iCoordProperty.get()+1].get();
				double ctrlz = Path3d.this.coordsProperty[this.iCoordProperty.get()+2].get();
				this.iCoordProperty.set(this.iCoordProperty.get()+3);

				this.p2.set(
						Path3d.this.coordsProperty[this.iCoordProperty.get()].get(),
						Path3d.this.coordsProperty[this.iCoordProperty.get()+1].get(),
						Path3d.this.coordsProperty[this.iCoordProperty.get()+2].get());
				this.iCoordProperty.set(this.iCoordProperty.get()+3);

				element = new AbstractPathElement3D.QuadPathElement3d(
						this.p1.getX(), this.p1.getY(), this.p1.getZ(),
						ctrlx, ctrly, ctrlz,
						this.p2.getX(), this.p2.getY(), this.p2.getZ());
			}
			break;
			case CURVE_TO:
			{
				if (this.iCoordProperty.get()+9>Path3d.this.numCoordsProperty.get()) {
					throw new NoSuchElementException();
				}
				this.p1.set(this.p2);
				double ctrlx1 = Path3d.this.coordsProperty[this.iCoordProperty.get()].get();
				double ctrly1 = Path3d.this.coordsProperty[this.iCoordProperty.get()+1].get();
				double ctrlz1 = Path3d.this.coordsProperty[this.iCoordProperty.get()+2].get();
				double ctrlx2 = Path3d.this.coordsProperty[this.iCoordProperty.get()+3].get();
				double ctrly2 = Path3d.this.coordsProperty[this.iCoordProperty.get()+4].get();
				double ctrlz2 = Path3d.this.coordsProperty[this.iCoordProperty.get()+5].get();
				this.iCoordProperty.set(this.iCoordProperty.get()+6);

				this.p2.set(
						Path3d.this.coordsProperty[this.iCoordProperty.get()].get(),
						Path3d.this.coordsProperty[this.iCoordProperty.get()+1].get(),
						Path3d.this.coordsProperty[this.iCoordProperty.get()+2].get());
				this.iCoordProperty.set(this.iCoordProperty.get()+3);

				element = new AbstractPathElement3D.CurvePathElement3d(
						this.p1.getX(), this.p1.getY(), this.p1.getZ(),
						ctrlx1, ctrly1, ctrlz1,
						ctrlx2, ctrly2, ctrlz2,
						this.p2.getX(), this.p2.getY(), this.p2.getZ());
			}
			break;
			case CLOSE:
				this.p1.set(this.p2);
				this.p2.set(this.movexProperty.get(), this.moveyProperty.get(), this.movezProperty.get());
				element = new AbstractPathElement3D.ClosePathElement3d(
						this.p1.getX(), this.p1.getY(), this.p1.getZ(),
						this.p2.getX(), this.p2.getY(), this.p2.getZ());
				break;
			default:
			}
			if (element==null)
				throw new NoSuchElementException();

			this.iTypeProperty.set(this.iTypeProperty.get()+1);

			return element;
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}

		@Pure
		@Override
		public PathWindingRule getWindingRule() {
			return Path3d.this.getWindingRule();
		}

		@Override
		public boolean isPolyline() {
			return Path3d.this.isPolyline();
		}

	} // class CopyPathIterator3d

	/** A path iterator that does not transform the coordinates.
	 *
	 * @author $Author: hjaffali$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	private class CopyPathIterator3f implements PathIterator3f {

		private final Point3D p1 = new Point3f();
		private final Point3D p2 = new Point3f();
		private int iType = 0;
		private int iCoord = 0;
		private double movex, movey, movez;

		/**
		 */
		public CopyPathIterator3f() {
			//
		}

		@Pure
		@Override
		public boolean hasNext() {
			return this.iType<Path3d.this.numTypesProperty.get();
		}

		@Override
		public AbstractPathElement3F next() {
			int type = this.iType;
			if (this.iType>=Path3d.this.numTypesProperty.get()) {
				throw new NoSuchElementException();
			}
			AbstractPathElement3F element = null;
			switch(Path3d.this.types[type]) {
			case MOVE_TO:
				if (this.iCoord+3>Path3d.this.numCoordsProperty.get()) {
					throw new NoSuchElementException();
				}
				this.movex = Path3d.this.coordsProperty[this.iCoord++].get();

				this.movey = Path3d.this.coordsProperty[this.iCoord++].get();

				this.movez = Path3d.this.coordsProperty[this.iCoord++ ].get();

				this.p2.set(this.movex, this.movey, this.movez);
				element = new AbstractPathElement3F.MovePathElement3f(
						this.p2.getX(), this.p2.getY(), this.p2.getZ());
				break;
			case LINE_TO:
				if (this.iCoord+3>Path3d.this.numCoordsProperty.get()) {
					throw new NoSuchElementException();
				}
				this.p1.set(this.p2);
				this.p2.set(
						Path3d.this.coordsProperty[this.iCoord++].get(),
						Path3d.this.coordsProperty[this.iCoord++].get(),
						Path3d.this.coordsProperty[this.iCoord++].get());

				element = new AbstractPathElement3F.LinePathElement3f(
						this.p1.getX(), this.p1.getY(), this.p1.getZ(),
						this.p2.getX(), this.p2.getY(), this.p2.getZ());
				break;
			case QUAD_TO:
			{
				if (this.iCoord+6>Path3d.this.numCoordsProperty.get()) {
					throw new NoSuchElementException();
				}
				this.p1.set(this.p2);
				double ctrlx = Path3d.this.coordsProperty[this.iCoord++].get();
				double ctrly = Path3d.this.coordsProperty[this.iCoord++].get();
				double ctrlz = Path3d.this.coordsProperty[this.iCoord++].get();

				this.p2.set(
						Path3d.this.coordsProperty[this.iCoord++].get(),
						Path3d.this.coordsProperty[this.iCoord++].get(),
						Path3d.this.coordsProperty[this.iCoord++].get());

				element = new AbstractPathElement3F.QuadPathElement3f(
						this.p1.getX(), this.p1.getY(), this.p1.getZ(),
						ctrlx, ctrly, ctrlz,
						this.p2.getX(), this.p2.getY(), this.p2.getZ());
			}
			break;
			case CURVE_TO:
			{
				if (this.iCoord+9>Path3d.this.numCoordsProperty.get()) {
					throw new NoSuchElementException();
				}
				this.p1.set(this.p2);
				double ctrlx1 = Path3d.this.coordsProperty[this.iCoord++].get();
				double ctrly1 = Path3d.this.coordsProperty[this.iCoord++].get();
				double ctrlz1 = Path3d.this.coordsProperty[this.iCoord++].get();
				double ctrlx2 = Path3d.this.coordsProperty[this.iCoord++].get();
				double ctrly2 = Path3d.this.coordsProperty[this.iCoord++].get();
				double ctrlz2 = Path3d.this.coordsProperty[this.iCoord++].get();

				this.p2.set(
						Path3d.this.coordsProperty[this.iCoord++].get(),
						Path3d.this.coordsProperty[this.iCoord++].get(),
						Path3d.this.coordsProperty[this.iCoord++].get());

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

		@Pure
		@Override
		public PathWindingRule getWindingRule() {
			return Path3d.this.getWindingRule();
		}

		@Override
		public boolean isPolyline() {
			return Path3d.this.isPolyline();
		}

	} // class CopyPathIterator3f



	/** A path iterator that transforms the coordinates.
	 *
	 * @author $Author: hjaffali$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	private class TransformPathIterator3d implements PathIterator3d {

		private final Transform3D transform;
		private final Point3D p1 = new Point3d();
		private final Point3D p2 = new Point3d();
		private final Point3D ptmp1 = new Point3d();
		private final Point3D ptmp2 = new Point3d();
		private IntegerProperty iTypeProperty = new SimpleIntegerProperty(0);
		private IntegerProperty iCoordProperty = new SimpleIntegerProperty(0);
		private DoubleProperty movexProperty, moveyProperty, movezProperty;

		/**
		 * @param transform1
		 */
		public TransformPathIterator3d(Transform3D transform1) {
			assert(transform1!=null);
			this.transform = transform1;
			this.movexProperty = new SimpleDoubleProperty();
			this.moveyProperty = new SimpleDoubleProperty();
			this.movezProperty = new SimpleDoubleProperty();
		}

		@Pure
		@Override
		public boolean hasNext() {
			return this.iTypeProperty.get()<Path3d.this.numTypesProperty.get();
		}

		@Override
		public AbstractPathElement3D next() {
			if (this.iTypeProperty.get()>=Path3d.this.numTypesProperty.get()) {
				throw new NoSuchElementException();
			}
			AbstractPathElement3D element = null;
			switch(Path3d.this.types[this.iTypeProperty.get()]) {
			case MOVE_TO:
				this.movexProperty.set(Path3d.this.coordsProperty[this.iCoordProperty.get()].get());
				this.iCoordProperty.set(this.iCoordProperty.get()+1);

				this.moveyProperty.set(Path3d.this.coordsProperty[this.iCoordProperty.get()].get());
				this.iCoordProperty.set(this.iCoordProperty.get()+1);

				this.movezProperty.set(Path3d.this.coordsProperty[this.iCoordProperty.get()].get());
				this.iCoordProperty.set(this.iCoordProperty.get()+1);

				this.p2.set(this.movexProperty.get(), this.moveyProperty.get(), this.movezProperty.get());
				this.transform.transform(this.p2);
				element = new AbstractPathElement3D.MovePathElement3d(
						this.p2.getX(), this.p2.getY(), this.p2.getZ());
				break;
			case LINE_TO:
				this.p1.set(this.p2);
				this.p2.set(
						Path3d.this.coordsProperty[this.iCoordProperty.get()].get(),
						Path3d.this.coordsProperty[this.iCoordProperty.get()+1].get(),
						Path3d.this.coordsProperty[this.iCoordProperty.get()+2].get());
				this.iCoordProperty.set(this.iCoordProperty.get()+3);
				this.transform.transform(this.p2);
				element = new AbstractPathElement3D.LinePathElement3d(
						this.p1.getX(), this.p1.getY(), this.p1.getZ(),
						this.p2.getX(), this.p2.getY(), this.p2.getZ());
				break;
			case QUAD_TO:
			{
				this.p1.set(this.p2);
				this.ptmp1.set(
						Path3d.this.coordsProperty[this.iCoordProperty.get()].get(),
						Path3d.this.coordsProperty[this.iCoordProperty.get()+1].get(),
						Path3d.this.coordsProperty[this.iCoordProperty.get()+2].get());
				this.iCoordProperty.set(this.iCoordProperty.get()+3);
				this.transform.transform(this.ptmp1);
				this.p2.set(
						Path3d.this.coordsProperty[this.iCoordProperty.get()].get(),
						Path3d.this.coordsProperty[this.iCoordProperty.get()+1].get(),
						Path3d.this.coordsProperty[this.iCoordProperty.get()+2].get());
				this.iCoordProperty.set(this.iCoordProperty.get()+3);
				this.transform.transform(this.p2);
				element = new AbstractPathElement3D.QuadPathElement3d(
						this.p1.getX(), this.p1.getY(), this.p1.getZ(),
						this.ptmp1.getX(), this.ptmp1.getY(), this.ptmp1.getZ(),
						this.p2.getX(), this.p2.getY(), this.p2.getZ());
			}
			break;
			case CURVE_TO:
			{
				this.p1.set(this.p2);
				this.ptmp1.set(
						Path3d.this.coordsProperty[this.iCoordProperty.get()].get(),
						Path3d.this.coordsProperty[this.iCoordProperty.get()+1].get(),
						Path3d.this.coordsProperty[this.iCoordProperty.get()+2].get());
				this.iCoordProperty.set(this.iCoordProperty.get()+3);
				this.transform.transform(this.ptmp1);
				this.ptmp2.set(
						Path3d.this.coordsProperty[this.iCoordProperty.get()].get(),
						Path3d.this.coordsProperty[this.iCoordProperty.get()+1].get(),
						Path3d.this.coordsProperty[this.iCoordProperty.get()+2].get());
				this.iCoordProperty.set(this.iCoordProperty.get()+3);
				this.transform.transform(this.ptmp2);
				this.p2.set(
						Path3d.this.coordsProperty[this.iCoordProperty.get()].get(),
						Path3d.this.coordsProperty[this.iCoordProperty.get()+1].get(),
						Path3d.this.coordsProperty[this.iCoordProperty.get()+2].get());
				this.iCoordProperty.set(this.iCoordProperty.get()+3);
				this.transform.transform(this.p2);
				element = new AbstractPathElement3D.CurvePathElement3d(
						this.p1.getX(), this.p1.getY(), this.p1.getZ(),
						this.ptmp1.getX(), this.ptmp1.getY(), this.ptmp1.getZ(),
						this.ptmp2.getX(), this.ptmp2.getY(), this.ptmp2.getZ(),
						this.p2.getX(), this.p2.getY(), this.p2.getZ());
			}
			break;
			case CLOSE:
				this.p1.set(this.p2);
				this.p2.set(this.movexProperty.get(), this.moveyProperty.get(), this.movezProperty.get());
				this.transform.transform(this.p2);
				element = new AbstractPathElement3D.ClosePathElement3d(
						this.p1.getX(), this.p1.getY(), this.p1.getZ(),
						this.p2.getX(), this.p2.getY(), this.p2.getZ());
				break;
			default:
			}
			if (element==null)
				throw new NoSuchElementException();

			this.iTypeProperty.set(this.iTypeProperty.get()+1);

			return element;
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}

		@Pure
		@Override
		public PathWindingRule getWindingRule() {
			return Path3d.this.getWindingRule();
		}

		@Override
		public boolean isPolyline() {
			return Path3d.this.isPolyline();
		}

	}  // class TransformPathIterator3d


	/** A path iterator that transforms the coordinates.
	 *
	 * @author $Author: hjaffali$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	private class TransformPathIterator3f implements PathIterator3f {

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
		public TransformPathIterator3f(Transform3D transform1) {
			assert(transform1!=null);
			this.transform = transform1;
		}

		@Pure
		@Override
		public boolean hasNext() {
			return this.iType<Path3d.this.numTypesProperty.get();
		}

		@Override
		public AbstractPathElement3F next() {
			if (this.iType>=Path3d.this.numTypesProperty.get()) {
				throw new NoSuchElementException();
			}
			AbstractPathElement3F element = null;
			switch(Path3d.this.types[this.iType]) {
			case MOVE_TO:
				this.movex = Path3d.this.coordsProperty[this.iCoord++].get();

				this.movey = Path3d.this.coordsProperty[this.iCoord++].get();

				this.movez = Path3d.this.coordsProperty[this.iCoord++].get();

				this.p2.set(this.movex, this.movey, this.movez);
				this.transform.transform(this.p2);
				element = new AbstractPathElement3F.MovePathElement3f(
						this.p2.getX(), this.p2.getY(), this.p2.getZ());
				break;
			case LINE_TO:
				this.p1.set(this.p2);
				this.p2.set(
						Path3d.this.coordsProperty[this.iCoord++].get(),
						Path3d.this.coordsProperty[this.iCoord++].get(),
						Path3d.this.coordsProperty[this.iCoord++].get());
				this.transform.transform(this.p2);
				element = new AbstractPathElement3F.LinePathElement3f(
						this.p1.getX(), this.p1.getY(), this.p1.getZ(),
						this.p2.getX(), this.p2.getY(), this.p2.getZ());
				break;
			case QUAD_TO:
			{
				this.p1.set(this.p2);
				this.ptmp1.set(
						Path3d.this.coordsProperty[this.iCoord++].get(),
						Path3d.this.coordsProperty[this.iCoord++].get(),
						Path3d.this.coordsProperty[this.iCoord++].get());
				this.transform.transform(this.ptmp1);
				this.p2.set(
						Path3d.this.coordsProperty[this.iCoord++].get(),
						Path3d.this.coordsProperty[this.iCoord++].get(),
						Path3d.this.coordsProperty[this.iCoord++].get());
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
						Path3d.this.coordsProperty[this.iCoord++].get(),
						Path3d.this.coordsProperty[this.iCoord++].get(),
						Path3d.this.coordsProperty[this.iCoord++].get());
				this.transform.transform(this.ptmp1);
				this.ptmp2.set(
						Path3d.this.coordsProperty[this.iCoord++].get(),
						Path3d.this.coordsProperty[this.iCoord++].get(),
						Path3d.this.coordsProperty[this.iCoord++].get());
				this.transform.transform(this.ptmp2);
				this.p2.set(
						Path3d.this.coordsProperty[this.iCoord++].get(),
						Path3d.this.coordsProperty[this.iCoord++].get(),
						Path3d.this.coordsProperty[this.iCoord++].get());
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

			++this.iType;

			return element;
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}

		@Pure
		@Override
		public PathWindingRule getWindingRule() {
			return Path3d.this.getWindingRule();
		}

		@Override
		public boolean isPolyline() {
			return Path3d.this.isPolyline();
		}

	}  // class TransformPathIterator3f

	/** A path iterator that is flattening the path.
	 * This iterator was copied from AWT FlatteningPathIterator.
	 *
	 * @author $Author: hjaffali$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	protected static class FlatteningPathIterator3d implements PathIterator3d {

		/** Winding rule of the path.
		 */
		private final PathWindingRule windingRule;

		/** The source iterator.
		 */
		private final PathIterator3d pathIterator;

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
		private IntegerProperty holdEndProperty = new SimpleIntegerProperty();

		/**
		 * The index of the curve segment that was last interpolated.  This
		 * is the curve segment ready to be returned in the next call to
		 * next().
		 */
		private IntegerProperty holdIndexProperty = new SimpleIntegerProperty();

		/** The ending x of the last segment.
		 */
		private DoubleProperty currentXProperty = new SimpleDoubleProperty();

		/** The ending y of the last segment.
		 */
		private DoubleProperty currentYProperty = new SimpleDoubleProperty();

		/** The ending z of the last segment.
		 */
		private DoubleProperty currentZProperty = new SimpleDoubleProperty();

		/** The x of the last move segment.
		 */
		private DoubleProperty moveXProperty = new SimpleDoubleProperty();

		/** The y of the last move segment.
		 */
		private DoubleProperty moveYProperty = new SimpleDoubleProperty();

		/** The z of the last move segment.
		 */
		private DoubleProperty moveZProperty = new SimpleDoubleProperty();

		/** The index of the entry in the
		 * levels array of the curve segment
		 * at the holdIndex
		 */
		private IntegerProperty levelIndexProperty = new SimpleIntegerProperty();

		/** True when iteration is done.
		 */
		private BooleanProperty doneProperty = new SimpleBooleanProperty();

		/** The type of the path element.
		 */
		private PathElementType holdType;

		/** The x of the last move segment replied by next.
		 */
		private DoubleProperty lastNextXProperty = new SimpleDoubleProperty();

		/** The y of the last move segment replied by next.
		 */
		private DoubleProperty lastNextYProperty = new SimpleDoubleProperty();

		/** The y of the last move segment replied by next.
		 */
		private DoubleProperty lastNextZProperty = new SimpleDoubleProperty();

		/**
		 * @param windingRule1 is the winding rule of the path.
		 * @param pathIterator1 is the path iterator that may be used to initialize the path.
		 * @param flatness the maximum allowable distance between the
		 * control points and the flattened curve
		 * @param limit1 the maximum number of recursive subdivisions
		 * allowed for any curved segment
		 */
		public FlatteningPathIterator3d(PathWindingRule windingRule1, PathIterator3d pathIterator1, double flatness, int limit1) {
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
			if (this.holdIndexProperty.get() - want < 0) {
				int have = this.hold.length - this.holdIndexProperty.get();
				int newsize = this.hold.length + GROW_SIZE;
				double newhold[] = new double[newsize];
				System.arraycopy(this.hold, this.holdIndexProperty.get(),
						newhold, this.holdIndexProperty.get() + GROW_SIZE,
						have);
				this.hold = newhold;
				this.holdIndexProperty.set(this.holdIndexProperty.get()+GROW_SIZE);
				this.holdEndProperty.set(this.holdEndProperty.get()+GROW_SIZE);
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
		@Pure
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
		@Pure
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

			if (this.holdIndexProperty.get() >= this.holdEndProperty.get()) {
				if (!this.pathIterator.hasNext()) {
					this.doneProperty.set(true);
					return;
				}
				AbstractPathElement3D pathElement = this.pathIterator.next();
				this.holdType = pathElement.type;
				pathElement.toArray(this.hold);
				this.levelIndexProperty.set(0);
				this.levels[0] = 0;
			}

			switch (this.holdType) {
			case MOVE_TO:
			case LINE_TO:
				this.currentXProperty.set(this.hold[0]);
				this.currentYProperty.set(this.hold[1]);
				this.currentZProperty.set(this.hold[2]);
				if (this.holdType == PathElementType.MOVE_TO) {
					this.moveXProperty.set(this.currentXProperty.get());
					this.moveYProperty.set(this.currentYProperty.get());
					this.moveZProperty.set(this.currentZProperty.get());
				}
				this.holdIndexProperty.set(0);
				this.holdEndProperty.set(0);
				break;
			case CLOSE:
				this.currentXProperty.set(this.moveXProperty.get());
				this.currentYProperty.set(this.moveYProperty.get());
				this.currentZProperty.set(this.moveZProperty.get());

				this.holdIndexProperty.set(0);
				this.holdEndProperty.set(0);
				break;
			case QUAD_TO:
				if (this.holdIndexProperty.get() >= this.holdEndProperty.get()) {
					// Move the coordinates to the end of the array.
					this.holdIndexProperty.set(this.hold.length - 9);
					this.holdEndProperty.set(this.hold.length - 3);

					this.hold[this.holdIndexProperty.get() + 0] = this.currentXProperty.get();
					this.hold[this.holdIndexProperty.get() + 1] = this.currentYProperty.get();
					this.hold[this.holdIndexProperty.get() + 2] = this.currentZProperty.get();
					this.hold[this.holdIndexProperty.get() + 3] = this.hold[0];
					this.hold[this.holdIndexProperty.get() + 4] = this.hold[1];
					this.hold[this.holdIndexProperty.get() + 5] = this.hold[2];
					this.hold[this.holdIndexProperty.get() + 6] = this.hold[3];
					this.currentXProperty.set(this.hold[3]);
					this.hold[this.holdIndexProperty.get() + 7] = this.hold[4];
					this.currentYProperty.set(this.hold[4]);
					this.hold[this.holdIndexProperty.get() + 8] = this.hold[5];
					this.currentZProperty.set(this.hold[5]);
				}

				level = this.levels[this.levelIndexProperty.get()];
				while (level < this.limit) {
					if (getQuadSquaredFlatness(this.hold, this.holdIndexProperty.get()) < this.squaredFlatness) {
						break;
					}

					ensureHoldCapacity(6);
					subdivideQuad(
							this.hold, this.holdIndexProperty.get(),
							this.hold, this.holdIndexProperty.get() - 6,
							this.hold, this.holdIndexProperty.get());
					this.holdIndexProperty.set(this.holdIndexProperty.get()-6);

					// Now that we have subdivided, we have constructed
					// two curves of one depth lower than the original
					// curve.  One of those curves is in the place of
					// the former curve and one of them is in the next
					// set of held coordinate slots.  We now set both
					// curves level values to the next higher level.
					level++;
					this.levels[this.levelIndexProperty.get()] = level;
					this.levelIndexProperty.set(this.levelIndexProperty.get()+1);
					this.levels[this.levelIndexProperty.get()] = level;
				}

				// This curve segment is flat enough, or it is too deep
				// in recursion levels to try to flatten any more.  The
				// two coordinates at holdIndex+6 and holdIndex+7 now
				// contain the endpoint of the curve which can be the
				// endpoint of an approximating line segment.
				this.holdIndexProperty.set(this.holdIndexProperty.get()+6);
				this.levelIndexProperty.set(this.levelIndexProperty.get()-1);
				break;
			case CURVE_TO:
				if (this.holdIndexProperty.get() >= this.holdEndProperty.get()) {
					// Move the coordinates to the end of the array.
					this.holdIndexProperty.set(this.hold.length - 12);
					this.holdEndProperty.set(this.hold.length - 3);
					this.hold[this.holdIndexProperty.get() + 0] = this.currentXProperty.get();
					this.hold[this.holdIndexProperty.get() + 1] = this.currentYProperty.get();
					this.hold[this.holdIndexProperty.get() + 2] = this.currentYProperty.get();
					this.hold[this.holdIndexProperty.get() + 3] = this.hold[0];
					this.hold[this.holdIndexProperty.get() + 4] = this.hold[1];
					this.hold[this.holdIndexProperty.get() + 5] = this.hold[2];
					this.hold[this.holdIndexProperty.get() + 6] = this.hold[3];
					this.hold[this.holdIndexProperty.get() + 7] = this.hold[4];
					this.hold[this.holdIndexProperty.get() + 8] = this.hold[5];
					this.hold[this.holdIndexProperty.get() + 9] = this.hold[6];   
					this.currentXProperty.set(this.hold[6]);
					this.hold[this.holdIndexProperty.get() + 10] = this.hold[7];
					this.currentYProperty.set(this.hold[7]);
					this.hold[this.holdIndexProperty.get() + 11] = this.hold[8];
					this.currentYProperty.set(this.hold[8]);
				}

				level = this.levels[this.levelIndexProperty.get()];
				while (level < this.limit) {
					if (getCurveSquaredFlatness(this.hold,this. holdIndexProperty.get()) < this.squaredFlatness) {
						break;
					}

					ensureHoldCapacity(9);
					subdivideCurve(
							this.hold, this.holdIndexProperty.get(),
							this.hold, this.holdIndexProperty.get() - 9,
							this.hold, this.holdIndexProperty.get());
					this.holdIndexProperty.set(this.holdIndexProperty.get()-9);

					// Now that we have subdivided, we have constructed
					// two curves of one depth lower than the original
					// curve.  One of those curves is in the place of
					// the former curve and one of them is in the next
					// set of held coordinate slots.  We now set both
					// curves level values to the next higher level.
					level++;
					this.levels[this.levelIndexProperty.get()] = level;
					this.levelIndexProperty.set(this.levelIndexProperty.get()+1);
					this.levels[this.levelIndexProperty.get()] = level;
				}

				// This curve segment is flat enough, or it is too deep
				// in recursion levels to try to flatten any more.  The
				// two coordinates at holdIndex+9 and holdIndex+10 now
				// contain the endpoint of the curve which can be the
				// endpoint of an approximating line segment.
				this.holdIndexProperty.set(this.holdIndexProperty.get()+9);
				this.levelIndexProperty.set(this.levelIndexProperty.get()-1);
				break;
			default:
			}
		}

		@Pure
		@Override
		public boolean hasNext() {
			return !this.doneProperty.get();
		}

		@Override
		public AbstractPathElement3D next() {
			if (this.doneProperty.get()) {
				throw new NoSuchElementException("flattening iterator out of bounds"); //$NON-NLS-1$
			}

			AbstractPathElement3D element;
			PathElementType type = this.holdType;
			if (type!=PathElementType.CLOSE) {
				double x = this.hold[this.holdIndexProperty.get() + 0];
				double y = this.hold[this.holdIndexProperty.get() + 1];
				double z = this.hold[this.holdIndexProperty.get() + 2];
				if (type == PathElementType.MOVE_TO) {
					element = new AbstractPathElement3D.MovePathElement3d(x, y, z);
				}
				else {
					element = new AbstractPathElement3D.LinePathElement3d(
							this.lastNextXProperty.get(), this.lastNextYProperty.get(), this.lastNextZProperty.get(),
							x, y, z);
				}
				this.lastNextXProperty.set(x);
				this.lastNextYProperty.set(y);
				this.lastNextZProperty.set(z);
			}
			else {
				element = new AbstractPathElement3D.ClosePathElement3d(
						this.lastNextXProperty.get(), this.lastNextYProperty.get(), this.lastNextZProperty.get(),
						this.moveXProperty.get(), this.moveYProperty.get(), this.moveZProperty.get());
				this.lastNextXProperty.set(this.moveXProperty.get());
				this.lastNextYProperty.set(this.moveYProperty.get());
				this.lastNextZProperty.set(this.moveZProperty.get());
			}

			searchNext();

			return element;
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}

		@Pure
		@Override
		public PathWindingRule getWindingRule() {
			return this.windingRule;
		}

		@Pure
		@Override
		public boolean isPolyline() {
			return false; // Because the iterator flats the path, this is no curve inside.
		}

	} // class FlatteningPathIterator3d

	/** An collection of the points of the path.
	 *
	 * @author $Author: hjaffali$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	@SuppressWarnings("unused")
	private class PointCollection3d implements Collection<Point3d> {

		/**
		 */
		public PointCollection3d() {
			//
		}

		@Pure
		@Override
		public int size() {
			return Path3d.this.size();
		}

		@Pure
		@Override
		public boolean isEmpty() {
			return Path3d.this.size()<=0;
		}

		@Pure
		@Override
		public boolean contains(Object o) {
			if (o instanceof Point3d) {
				return Path3d.this.containsControlPoint((Point3d)o);
			}
			return false;
		}

		@Pure
		@Override
		public Iterator<Point3d> iterator() {
			return new PointIterator3d();
		}

		@Pure
		@Override
		public Object[] toArray() {
			return Path3d.this.toPointArray();
		}

		@Pure
		@SuppressWarnings("unchecked")
		@Override
		public <T> T[] toArray(T[] a) {
			Iterator<Point3d> iterator = new PointIterator3d();
			for(int i=0; i<a.length && iterator.hasNext(); ++i) {
				a[i] = (T)iterator.next();
			}
			return a;
		}

		@Override
		public boolean add(Point3d e) {
			if (e!=null) {
				if (Path3d.this.size()==0) {
					Path3d.this.moveTo(e);
				}
				else {
					Path3d.this.lineTo(e);
				}
				return true;
			}
			return false;
		}

		@Override
		public boolean remove(Object o) {
			if (o instanceof Point3d) {
				Point3d p = (Point3d)o;
				return Path3d.this.remove(p.getX(), p.getY(), p.getZ());
			}
			return false;
		}

		@Pure
		@Override
		public boolean containsAll(Collection<?> c) {
			for(Object obj : c) {
				if ((!(obj instanceof Point3d))
						||(!Path3d.this.containsControlPoint((Point3d)obj))) {
					return false;
				}
			}
			return true;
		}

		@Override
		public boolean addAll(Collection<? extends Point3d> c) {
			boolean changed = false;
			for(Point3d pts : c) {
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
				if (obj instanceof Point3d) {
					Point3d pts = (Point3d)obj;
					if (Path3d.this.remove(pts.getX(), pts.getY(), pts.getZ())) {
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
			Path3d.this.clear();
		}

	} // class PointCollection

	/** Iterator on the points of the path.
	 *
	 * @author $Author: hjaffali$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	private class PointIterator3d implements Iterator<Point3d> {

		private int index = 0;
		private Point3d lastReplied = null;

		/**
		 */
		public PointIterator3d() {
			//
		}

		@Pure
		@Override
		public boolean hasNext() {
			return this.index<Path3d.this.size();
		}

		@Override
		public Point3d next() {
			try {
				this.lastReplied = Path3d.this.getPointAt(this.index++);
				return this.lastReplied;
			}
			catch( Throwable e) {
				e.printStackTrace();throw new NoSuchElementException();
			}
		}

		@Override
		public void remove() {
			Point3d p = this.lastReplied;
			this.lastReplied = null;
			if (p==null)
				throw new NoSuchElementException();
			Path3d.this.remove(p.getX(), p.getY(), p.getZ());
		}

	}

}
