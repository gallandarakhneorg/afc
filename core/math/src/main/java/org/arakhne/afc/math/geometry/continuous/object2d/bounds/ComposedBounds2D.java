/* 
 * $Id$
 * 
 * Copyright (c) 2013 Christophe BOHRHAUER
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
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

package org.arakhne.afc.math.geometry.continuous.object2d.bounds;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.arakhne.afc.math.geometry.ComposedBounds;
import org.arakhne.afc.math.geometry.continuous.euclide.EuclidianPoint2D;
import org.arakhne.afc.math.geometry.continuous.intersection.IntersectionType;
import org.arakhne.afc.math.geometry.continuous.object2d.Point2f;
import org.arakhne.afc.math.geometry.continuous.object2d.Vector2f;
import org.arakhne.afc.math.geometry.continuous.object3d.bounds.ComposedBounds3D;
import org.arakhne.afc.math.geometry.continuous.system.CoordinateSystem3D;

/** This class representes the bounds of an area
 * with could be composed of other smaller bounds.
 * 
 * @author $Author: cbohrhauer$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class ComposedBounds2D
extends AbstractBounds2D
implements ComposedBounds<ComposedBounds2D,
                          Bounds2D,
                          EuclidianPoint2D,
                          Point2f,
                          Vector2f>,
           TranslatableBounds2D,
           Collection<Bounds2D> {

	private static final long serialVersionUID = -7105299257324401348L;
	
	/** List of sub-bounds.
	 */
	private List<Bounds2D> subBounds = new ArrayList<Bounds2D>();
	
	/**
	 */
	public ComposedBounds2D() {
		//
	}
	
	/** {@inheritDoc}
	 */
	@Override
	public final BoundingPrimitiveType2D getBoundType() {
		return null;
	}

	/** {@inheritDoc}
	 */
	@Override
	public ComposedBounds2D clone() {
		ComposedBounds2D clone = (ComposedBounds2D)super.clone();
		clone.subBounds = new ArrayList<Bounds2D>();
		for(Bounds2D child : this.subBounds) {
			clone.subBounds.add((Bounds2D)child.clone());
		}
		return clone;
	}

	/** {@inheritDoc}
	 */
	@Override
	public ComposedBounds3D toBounds3D() {
		return toBounds3D(CoordinateSystem3D.getDefaultCoordinateSystem());
	}

	/** {@inheritDoc}
	 */
	@Override
	public ComposedBounds3D toBounds3D(float z, float zsize) {
		return toBounds3D(z, zsize, CoordinateSystem3D.getDefaultCoordinateSystem());
	}

	/** {@inheritDoc}
	 */
	@Override
	public ComposedBounds3D toBounds3D(CoordinateSystem3D system) {
		ComposedBounds3D cb = new ComposedBounds3D();
		for(Bounds2D b : this.subBounds) {
			cb.add(b.toBounds3D(system));
		}
		return cb;
	}

	/** {@inheritDoc}
	 */
	@Override
	public ComposedBounds3D toBounds3D(float z, float zsize, CoordinateSystem3D system) {
		ComposedBounds3D cb = new ComposedBounds3D();
		for(Bounds2D b : this.subBounds) {
			cb.add(b.toBounds3D(z, zsize, system));
		}
		return cb;
	}

	/** {@inheritDoc}
	 */
	@Override
	public boolean add(Bounds2D bounds) {
		return this.subBounds.add(bounds);
	}

	/** {@inheritDoc}
	 */
	@Override
	public boolean addAll(Collection<? extends Bounds2D> bounds) {
		return this.subBounds.addAll(bounds);
	}

	/** {@inheritDoc}
	 */
	@Override
	public void remove(Bounds2D bounds) {
		this.subBounds.remove(bounds);
	}

	/** {@inheritDoc}
	 */
	@Override
	public void remove(Collection<?> bounds) {
		this.subBounds.removeAll(bounds);
	}

	/** {@inheritDoc}
	 */
	@Override
	public Iterable<Bounds2D> getSubBounds() {
		return Collections.unmodifiableCollection(this.subBounds);
	}

	/** {@inheritDoc}
	 */
	@Override
	public void getLowerUpper(Point2f lower, Point2f upper) {
		if (this.subBounds.isEmpty()) return;
		float minx = Float.POSITIVE_INFINITY;
		float miny = Float.POSITIVE_INFINITY;
		float maxx = Float.NEGATIVE_INFINITY;
		float maxy = Float.NEGATIVE_INFINITY;
		for(Bounds2D b : this.subBounds) {
			b.getLowerUpper(lower,upper);
			if (lower.getX()<minx) minx = lower.getX();
			if (lower.getY()<miny) miny = lower.getY();
			if (upper.getX()>maxx) maxx = upper.getX();
			if (upper.getY()>maxy) maxy = upper.getY();
		}
		lower.set(minx, miny);
		upper.set(maxx, maxy);
	}

	/** {@inheritDoc}
	 */
	@Override
	public EuclidianPoint2D getLower() {
		if (this.subBounds.isEmpty()) return null;
		float x = Float.POSITIVE_INFINITY;
		float y = Float.POSITIVE_INFINITY;
		Point2f pt;
		for(Bounds2D b : this.subBounds) {
			pt = b.getLower();
			if (pt!=null) {
				if (pt.getX()<x) x = pt.getX();
				if (pt.getY()<y) y = pt.getY();
			}
		}
		return new EuclidianPoint2D(x,y);
	}

	/** {@inheritDoc}
	 */
	@Override
	public EuclidianPoint2D getUpper() {
		if (this.subBounds.isEmpty()) return null;
		float x = Float.NEGATIVE_INFINITY;
		float y = Float.NEGATIVE_INFINITY;
		Point2f pt;
		for(Bounds2D b : this.subBounds) {
			pt = b.getLower();
			if (pt!=null) {
				if (pt.getX()>x) x = pt.getX();
				if (pt.getY()>y) y = pt.getY();
			}
		}
		return new EuclidianPoint2D(x,y);
	}

	/** {@inheritDoc}
	 */
	@Override
	public EuclidianPoint2D getCenter() {
		if (this.subBounds.isEmpty()) return null;
		float ix = Float.POSITIVE_INFINITY;
		float iy = Float.POSITIVE_INFINITY;
		float ax = Float.NEGATIVE_INFINITY;
		float ay = Float.NEGATIVE_INFINITY;
		Point2f pt;
		for(Bounds2D b : this.subBounds) {
			pt = b.getLower();
			if (pt!=null) {
				if (pt.getX()>ax) ax = pt.getX();
				if (pt.getY()>ay) ay = pt.getY();
				if (pt.getX()<ix) ix = pt.getX();
				if (pt.getY()<iy) iy = pt.getY();
			}
		}
		return new EuclidianPoint2D((ix+ax)/2.f,(iy+ay)/2.f);
	}	

	/** {@inheritDoc}
	 */
	@Override
	public float distanceSquared(Point2f p) {
		float d = Float.POSITIVE_INFINITY;
		if (!this.subBounds.isEmpty()) {
			float d2;
			for(Bounds2D b : this.subBounds) {
				d2 = b.distanceSquared(p);
				if (d2<d) d = d2;
			}
		}
		return d;
	}

	/** {@inheritDoc}
	 */
	@Override
	public EuclidianPoint2D nearestPoint(Point2f reference) {
		EuclidianPoint2D nearest = null;
		if (!this.subBounds.isEmpty()) {
			float d = Float.POSITIVE_INFINITY;
			float d2;
			EuclidianPoint2D p;
			for(Bounds2D b : this.subBounds) {
				p = b.nearestPoint(reference);
				d2 = p.distance(reference);
				if (d2<d) {
					nearest = p;
					d = d2;
				}
			}
		}
		return nearest;
	}

	/** {@inheritDoc}
	 */
	@Override
	public float distanceMaxSquared(Point2f p) {
		if (!this.subBounds.isEmpty()) {
			float d = 0;
			float d2;
			for(Bounds2D b : this.subBounds) {
				d2 = b.distanceMaxSquared(p);
				if (d2>d) d = d2;
			}
			return d;
		}
		return Float.NaN;
	}

	/** {@inheritDoc}
	 */
	@Override
	public EuclidianPoint2D farestPoint(Point2f reference) {
		EuclidianPoint2D farest = null;
		if (!this.subBounds.isEmpty()) {
			float d = Float.NEGATIVE_INFINITY;
			float d2;
			EuclidianPoint2D p;
			for(Bounds2D b : this.subBounds) {
				p = b.farestPoint(reference);
				d2 = p.distance(reference);
				if (d2>d) {
					farest = p;
					d = d2;
				}
			}
		}
		return farest;
	}

	/** {@inheritDoc}
	 */
	@Override
	public boolean isEmpty() {
		if (!this.subBounds.isEmpty()) {
			for(Bounds2D b : this.subBounds) {
				if (!b.isEmpty()) return false;
			}
		}
		return true;
	}

	/** {@inheritDoc}
	 */
	@Override
	public boolean isInit() {
		if (!this.subBounds.isEmpty()) {
			for(Bounds2D b : this.subBounds) {
				if (!b.isInit()) return false;
			}
			return true;
		}
		return false;
	}

	/** {@inheritDoc}
	 */
	@Override
	public void translate(Vector2f v) {
		if (!this.subBounds.isEmpty()) {
			for(Bounds2D b : this.subBounds) {
				if (b instanceof TranslatableBounds2D) {
					((TranslatableBounds2D)b).translate(v);
				}
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Vector2f getSize() {
		if (this.subBounds.isEmpty()) return null;
		Point2f l = getLower();
		Point2f u = getUpper();
		return new Vector2f(u.getX()-l.getX(), u.getY()-l.getY());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public float getSizeX() {
		if (this.subBounds.isEmpty()) return Float.NaN;
		Point2f l = getLower();
		Point2f u = getUpper();
		return u.getX()-l.getX();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public float getSizeY() {
		if (this.subBounds.isEmpty()) return Float.NaN;
		Point2f l = getLower();
		Point2f u = getUpper();
		return u.getY()-l.getY();
	}

	//---------------------------------------------
	// IntersectionClassifier
	//---------------------------------------------

	@Override
	public IntersectionType classifies(Bounds2D box) {
		IntersectionType rtype = null;
		if (!this.subBounds.isEmpty()) {
			IntersectionType type;
			for(Bounds2D b : this.subBounds) {
				type = b.classifies(box);
				// If box is inside one bound, it sure box is inside the collection of bounds
				if (type==IntersectionType.INSIDE) return type;
				if (rtype==null) rtype = type;
				else rtype = IntersectionType.or(rtype, type);
			}
		}
		return rtype;
	}
	
	@Override
	public boolean intersects(Bounds2D box) {
		if (!this.subBounds.isEmpty()) {
			for(Bounds2D b : this.subBounds) {
				if (b.intersects(box)) return true;
			}
		}
		return false;
	}

	@Override
	public IntersectionType classifies(Point2f p) {
		IntersectionType rtype = null;
		if (!this.subBounds.isEmpty()) {
			IntersectionType type;
			for(Bounds2D b : this.subBounds) {
				type = b.classifies(p);
				// If box is inside one bound, it sure box is inside the collection of bounds
				if (type==IntersectionType.INSIDE) return type;
				if (rtype==null) rtype = type;
				else rtype = IntersectionType.or(rtype, type);
			}
		}
		return rtype;
	}

	@Override
	public boolean intersects(Point2f p) {
		if (!this.subBounds.isEmpty()) {
			for(Bounds2D b : this.subBounds) {
				if (b.intersects(p)) return true;
			}
		}
		return false;
	}

	@Override
	public IntersectionType classifies(Point2f c, float r) {
		IntersectionType rtype = null;
		if (!this.subBounds.isEmpty()) {
			IntersectionType type;
			for(Bounds2D b : this.subBounds) {
				type = b.classifies(c, r);
				// If box is inside one bound, it sure box is inside the collection of bounds
				if (type==IntersectionType.INSIDE) return type;
				if (rtype==null) rtype = type;
				else rtype = IntersectionType.or(rtype, type);
			}
		}
		return rtype;
	}

	@Override
	public boolean intersects(Point2f c, float r) {
		if (!this.subBounds.isEmpty()) {
			for(Bounds2D b : this.subBounds) {
				if (b.intersects(c, r)) return true;
			}
		}
		return false;
	}

	@Override
	public IntersectionType classifies(Point2f l, Point2f u) {
		IntersectionType rtype = null;
		if (!this.subBounds.isEmpty()) {
			IntersectionType type;
			for(Bounds2D b : this.subBounds) {
				type = b.classifies(l, u);
				// If box is inside one bound, it sure box is inside the collection of bounds
				if (type==IntersectionType.INSIDE) return type;
				if (rtype==null) rtype = type;
				else rtype = IntersectionType.or(rtype, type);
			}
		}
		return rtype;
	}

	@Override
	public boolean intersects(Point2f l, Point2f u) {
		if (!this.subBounds.isEmpty()) {
			for(Bounds2D b : this.subBounds) {
				if (b.intersects(l,u)) return true;
			}
		}
		return false;
	}

	@Override
	public IntersectionType classifies(Point2f center, Vector2f[] axis, float[] extent) {
		IntersectionType rtype = null;
		if (!this.subBounds.isEmpty()) {
			IntersectionType type;
			for(Bounds2D b : this.subBounds) {
				type = b.classifies(center, axis, extent);
				// If box is inside one bound, it sure box is inside the collection of bounds
				if (type==IntersectionType.INSIDE) return type;
				if (rtype==null) rtype = type;
				else rtype = IntersectionType.or(rtype, type);
			}
		}
		return rtype;
	}

	@Override
	public boolean intersects(Point2f center, Vector2f[] axis, float[] extent) {
		if (!this.subBounds.isEmpty()) {
			for(Bounds2D b : this.subBounds) {
				if (b.intersects(center, axis, extent)) return true;
			}
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void clear() {
		this.subBounds.clear();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean contains(Object o) {
		return this.subBounds.contains(o);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean containsAll(Collection<?> c) {
		return this.subBounds.containsAll(c);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterator<Bounds2D> iterator() {
		return this.subBounds.iterator();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean remove(Object o) {
		return this.subBounds.remove(o);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean removeAll(Collection<?> c) {
		return this.subBounds.removeAll(c);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean retainAll(Collection<?> c) {
		return this.subBounds.retainAll(c);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int size() {
		return this.subBounds.size();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object[] toArray() {
		return this.subBounds.toArray();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <T> T[] toArray(T[] a) {
		return this.subBounds.toArray(a);
	}

}
