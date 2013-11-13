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

package org.arakhne.afc.math.geometry.continuous.object3d.bounds;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.arakhne.afc.math.geometry.ComposedBounds;
import org.arakhne.afc.math.geometry.continuous.euclide.EuclidianPoint3D;
import org.arakhne.afc.math.geometry.continuous.intersection.IntersectionType;
import org.arakhne.afc.math.geometry.continuous.object2d.bounds.ComposedBounds2D;
import org.arakhne.afc.math.geometry.continuous.object3d.Point3f;
import org.arakhne.afc.math.geometry.continuous.object3d.Vector3f;
import org.arakhne.afc.math.geometry.continuous.object3d.plane.PlanarClassificationType;
import org.arakhne.afc.math.geometry.continuous.object3d.plane.Plane;
import org.arakhne.afc.math.geometry.continuous.system.CoordinateSystem3D;

/** This class representes the bounds of an area
 * with could be composed of other smaller bounds.
 * 
 * @author $Author: sgalland$
 * @author $Author: ngaud$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class ComposedBounds3D
extends AbstractBounds3D
implements ComposedBounds<ComposedBounds3D, 
						  Bounds3D,
						  EuclidianPoint3D,
						  Point3f,
						  Vector3f>,
		   TranslatableBounds3D,
		   Collection<Bounds3D> {

	private static final long serialVersionUID = -7105299257324401348L;
	
	/** List of sub-bounds.
	 */
	private List<Bounds3D> subBounds = new ArrayList<Bounds3D>();
	
	/**
	 */
	public ComposedBounds3D() {
		//
	}
	
	/** {@inheritDoc}
	 */
	@Override
	public ComposedBounds3D clone() {
		ComposedBounds3D clone = (ComposedBounds3D)super.clone();
		clone.subBounds = new ArrayList<Bounds3D>();
		for(Bounds3D child : this.subBounds) {
			clone.subBounds.add((Bounds3D)child.clone());
		}
		return clone;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ComposedBounds2D toBounds2D() {
		return toBounds2D(CoordinateSystem3D.getDefaultCoordinateSystem());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final BoundingPrimitiveType3D getBoundType() {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ComposedBounds2D toBounds2D(CoordinateSystem3D system) {
		ComposedBounds2D cb = new ComposedBounds2D();
		for(Bounds3D b : this.subBounds) {
			cb.add(b.toBounds2D());
		}
		return cb;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean add(Bounds3D bounds) {
		return this.subBounds.add(bounds);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void remove(Bounds3D bounds) {
		this.subBounds.remove(bounds);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void remove(Collection<?> bounds) {
		this.subBounds.removeAll(bounds);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterable<Bounds3D> getSubBounds() {
		return Collections.unmodifiableCollection(this.subBounds);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void getLowerUpper(Point3f lower, Point3f upper) {
		if (this.subBounds.isEmpty()) return;
		float minx = Float.POSITIVE_INFINITY;
		float miny = Float.POSITIVE_INFINITY;
		float minz = Float.POSITIVE_INFINITY;
		float maxx = Float.NEGATIVE_INFINITY;
		float maxy = Float.NEGATIVE_INFINITY;
		float maxz = Float.NEGATIVE_INFINITY;
		for(Bounds3D b : this.subBounds) {
			b.getLowerUpper(lower,upper);
			if (lower.getX()<minx) minx = lower.getX();
			if (lower.getY()<miny) miny = lower.getY();
			if (lower.getZ()<minz) minz = lower.getZ();
			if (upper.getX()>maxx) maxx = upper.getX();
			if (upper.getY()>maxy) maxy = upper.getY();
			if (upper.getZ()>maxz) maxz = upper.getZ();
		}
		lower.set(minx, miny, minz);
		upper.set(maxx, maxy, maxz);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public EuclidianPoint3D getLower() {
		if (this.subBounds.isEmpty()) return null;
		float x = Float.POSITIVE_INFINITY;
		float y = Float.POSITIVE_INFINITY;
		float z = Float.POSITIVE_INFINITY;
		Point3f pt;
		for(Bounds3D b : this.subBounds) {
			pt = b.getLower();
			if (pt!=null) {
				if (pt.getX()<x) x = pt.getX();
				if (pt.getY()<y) y = pt.getY();
				if (pt.getZ()<z) z = pt.getZ();
			}
		}
		return new EuclidianPoint3D(x,y,z);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public EuclidianPoint3D getUpper() {
		if (this.subBounds.isEmpty()) return null;
		float x = Float.NEGATIVE_INFINITY;
		float y = Float.NEGATIVE_INFINITY;
		float z = Float.NEGATIVE_INFINITY;
		Point3f pt;
		for(Bounds3D b : this.subBounds) {
			pt = b.getUpper();
			if (pt!=null) {
				if (pt.getX()>x) x = pt.getX();
				if (pt.getY()>y) y = pt.getY();
				if (pt.getZ()>z) z = pt.getZ();
			}
		}
		return new EuclidianPoint3D(x,y,z);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public EuclidianPoint3D getCenter() {
		if (this.subBounds.isEmpty()) return null;
		float ix = Float.POSITIVE_INFINITY;
		float iy = Float.POSITIVE_INFINITY;
		float iz = Float.POSITIVE_INFINITY;
		float ax = Float.NEGATIVE_INFINITY;
		float ay = Float.NEGATIVE_INFINITY;
		float az = Float.NEGATIVE_INFINITY;
		Point3f pt;
		for(Bounds3D b : this.subBounds) {
			pt = b.getLower();
			if (pt!=null) {
				if (pt.getX()>ax) ax = pt.getX();
				if (pt.getY()>ay) ay = pt.getY();
				if (pt.getZ()>az) az = pt.getZ();
				if (pt.getX()<ix) ix = pt.getX();
				if (pt.getY()<iy) iy = pt.getY();
				if (pt.getZ()<iz) iz = pt.getZ();
			}
		}
		return new EuclidianPoint3D((ix+ax)/2.f,(iy+ay)/2.f,(iz+az)/2.f);
	}	

	/**
	 * {@inheritDoc}
	 */
	@Override
	public float distanceSquared(Point3f p) {
		float d = Float.POSITIVE_INFINITY;
		if (!this.subBounds.isEmpty()) {
			float d2;
			for(Bounds3D b : this.subBounds) {
				d2 = b.distanceSquared(p);
				if (d2<d) d = d2;
			}
		}
		return d;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public EuclidianPoint3D nearestPoint(Point3f reference) {
		EuclidianPoint3D nearest = null;
		if (!this.subBounds.isEmpty()) {
			float d = Float.POSITIVE_INFINITY;
			float d2;
			EuclidianPoint3D p;
			for(Bounds3D b : this.subBounds) {
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

	/**
	 * {@inheritDoc}
	 */
	@Override
	public float distanceMaxSquared(Point3f p) {
		if (!this.subBounds.isEmpty()) {
			float d = 0;
			float d2;
			for(Bounds3D b : this.subBounds) {
				d2 = b.distanceMaxSquared(p);
				if (d2>d) d = d2;
			}
			return d;
		}
		return Float.NaN;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public EuclidianPoint3D farestPoint(Point3f reference) {
		EuclidianPoint3D farest = null;
		if (!this.subBounds.isEmpty()) {
			float d = Float.NEGATIVE_INFINITY;
			float d2;
			EuclidianPoint3D p;
			for(Bounds3D b : this.subBounds) {
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

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isEmpty() {
		if (!this.subBounds.isEmpty()) {
			for(Bounds3D b : this.subBounds) {
				if (!b.isEmpty()) return false;
			}
		}
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isInit() {
		if (!this.subBounds.isEmpty()) {
			for(Bounds3D b : this.subBounds) {
				if (!b.isInit()) return false;
			}
			return true;
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void translate(Vector3f v) {
		if (!this.subBounds.isEmpty()) {
			for(Bounds3D b : this.subBounds) {
				if (b instanceof TranslatableBounds3D) {
					((TranslatableBounds3D)b).translate(v);
				}
			}
		}
	}

	/**
	 * Replies the height of this box.
	 * 
	 * @return the height of this box.
	 */
	private float getHeight(CoordinateSystem3D cs) {
		return (cs.getHeightCoordinateIndex()==1)
			?  getSizeY()
			: getSizeZ();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setTranslation(Point3f position, boolean onGround) {
		if (!this.subBounds.isEmpty()) {
			Point3f c = getPosition();
			assert(c!=null);
			Vector3f vv = new Vector3f(position);
			vv.sub(c);
			
			if (onGround) {
				CoordinateSystem3D cs = CoordinateSystem3D.getDefaultCoordinateSystem();
				cs.addHeight(vv, getHeight(cs)/2.f);
			}
			
			for(Bounds3D b : this.subBounds) {
				if (b instanceof TranslatableBounds3D) {
					((TranslatableBounds3D)b).translate(vv);
				}
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setTranslation(Point3f position) {
		if (!this.subBounds.isEmpty()) {
			Point3f c = getPosition();
			assert(c!=null);
			Vector3f vv = new Vector3f(position);
			vv.sub(c);
			for(Bounds3D b : this.subBounds) {
				if (b instanceof TranslatableBounds3D) {
					((TranslatableBounds3D)b).translate(vv);
				}
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Vector3f getSize() {
		if (this.subBounds.isEmpty()) return null;
		Point3f l = getLower();
		Point3f u = getUpper();
		return new Vector3f(u.getX()-l.getX(), u.getY()-l.getY(), u.getZ()-l.getZ());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public float getSizeX() {
		if (this.subBounds.isEmpty()) return Float.NaN;
		Point3f l = getLower();
		Point3f u = getUpper();
		return u.getX()-l.getX();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public float getSizeY() {
		if (this.subBounds.isEmpty()) return Float.NaN;
		Point3f l = getLower();
		Point3f u = getUpper();
		return u.getY()-l.getY();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public float getSizeZ() {
		if (this.subBounds.isEmpty()) return Float.NaN;
		Point3f l = getLower();
		Point3f u = getUpper();
		return u.getZ()-l.getZ();
	}

	//---------------------------------------------
	// IntersectionClassifier
	//---------------------------------------------

	@Override
	public IntersectionType classifies(Bounds3D box) {
		IntersectionType rtype = null;
		if (!this.subBounds.isEmpty()) {
			IntersectionType type;
			for(Bounds3D b : this.subBounds) {
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
	public boolean intersects(Bounds3D box) {
		if (!this.subBounds.isEmpty()) {
			for(Bounds3D b : this.subBounds) {
				if (b.intersects(box)) return true;
			}
		}
		return false;
	}

	@Override
	public IntersectionType classifies(Point3f p) {
		IntersectionType rtype = null;
		if (!this.subBounds.isEmpty()) {
			IntersectionType type;
			for(Bounds3D b : this.subBounds) {
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
	public boolean intersects(Point3f p) {
		if (!this.subBounds.isEmpty()) {
			for(Bounds3D b : this.subBounds) {
				if (b.intersects(p)) return true;
			}
		}
		return false;
	}

	@Override
	public IntersectionType classifies(Point3f c, float r) {
		IntersectionType rtype = null;
		if (!this.subBounds.isEmpty()) {
			IntersectionType type;
			for(Bounds3D b : this.subBounds) {
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
	public boolean intersects(Point3f c, float r) {
		if (!this.subBounds.isEmpty()) {
			for(Bounds3D b : this.subBounds) {
				if (b.intersects(c, r)) return true;
			}
		}
		return false;
	}

	@Override
	public IntersectionType classifies(Point3f l, Point3f u) {
		IntersectionType rtype = null;
		if (!this.subBounds.isEmpty()) {
			IntersectionType type;
			for(Bounds3D b : this.subBounds) {
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
	public boolean intersects(Point3f l, Point3f u) {
		if (!this.subBounds.isEmpty()) {
			for(Bounds3D b : this.subBounds) {
				if (b.intersects(l,u)) return true;
			}
		}
		return false;
	}

	@Override
	public IntersectionType classifies(Point3f center, Vector3f[] axis, float[] extent) {
		IntersectionType rtype = null;
		if (!this.subBounds.isEmpty()) {
			IntersectionType type;
			for(Bounds3D b : this.subBounds) {
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
	public boolean intersects(Point3f center, Vector3f[] axis, float[] extent) {
		if (!this.subBounds.isEmpty()) {
			for(Bounds3D b : this.subBounds) {
				if (b.intersects(center, axis, extent)) return true;
			}
		}
		return false;
	}

	@Override
	public IntersectionType classifies(Plane plane) {
		if (!this.subBounds.isEmpty()) {
			IntersectionType type;
			for(Bounds3D b : this.subBounds) {
				type = b.classifies(plane);
				if (type==IntersectionType.SPANNING)
					return type;
			}
		}
		return IntersectionType.OUTSIDE;
	}

	@Override
	public boolean intersects(Plane plane) {
		if (!this.subBounds.isEmpty()) {
			for(Bounds3D b : this.subBounds) {
				if (b.intersects(plane)) return true;
			}
		}
		return false;
	}

	@Override
	public PlanarClassificationType classifiesAgainst(Plane plane) {
		PlanarClassificationType rtype = null;
		if (!this.subBounds.isEmpty()) {
			PlanarClassificationType type;
			for(Bounds3D b : this.subBounds) {
				type = b.classifiesAgainst(plane);
				switch(type) {
				case COINCIDENT:
					return type;
				case BEHIND:
				case IN_FRONT_OF:
					if (rtype==null)
						rtype = type;
					else if (rtype!=type)
						rtype = PlanarClassificationType.COINCIDENT;
					else
						rtype = type;
				}
			}
		}
		return rtype;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean addAll(Collection<? extends Bounds3D> c) {
		return addAll(c);
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
	public Iterator<Bounds3D> iterator() {
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
