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

import java.util.Arrays;
import java.util.Collection;

import org.arakhne.afc.math.MathUtil;
import org.arakhne.afc.math.geometry.continuous.euclide.EuclidianPoint2D;
import org.arakhne.afc.math.geometry.continuous.intersection.IntersectionType;
import org.arakhne.afc.math.geometry.continuous.intersection.IntersectionUtil;
import org.arakhne.afc.math.geometry.continuous.object2d.Point2f;
import org.arakhne.afc.math.geometry.continuous.object2d.Tuple2f;
import org.arakhne.afc.math.geometry.continuous.object2d.Vector2f;
import org.arakhne.afc.math.geometry.continuous.object3d.bounds.BoundingSphere;
import org.arakhne.afc.math.geometry.continuous.system.CoordinateSystem3D;
import org.arakhne.afc.sizediterator.SizedIterator;
import org.arakhne.afc.util.ArrayUtil;

/**
 * A Bounding Circle.
 * 
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class BoundingCircle extends AbstractCombinableBounds2D implements TranslatableBounds2D, AlignedCombinableBounds2D, OrientedCombinableBounds2D {

	private static final long serialVersionUID = -1484927645061384025L;

	/**
	 * The vertices that composes this box, it is compiled when it is required not before
	 */
	private transient Vector2f[] vertices = null;

    /** Coordinate of the center.
     */
	EuclidianPoint2D center = new EuclidianPoint2D();
    
    /** Radius of the sphere.
     */
    float radius;   
    
    private boolean isBoundInit;
    
    /** Uninitialized bounding object.
     */
    public BoundingCircle() {
    	this.radius = 1;
    	this.isBoundInit = false;
    }
    
    /** 
     * @param x is the center of the sphere.
     * @param y is the center of the sphere.
     * @param radius is the radius of the sphere.
     */
    public BoundingCircle(float x, float y, float radius) {
        this.center.set(x,y);
        this.radius = Math.abs(radius);
    	this.isBoundInit = true;
    }
    
    /** 
     * @param p is the center of the sphere.
     * @param radius is the radius of the sphere.
     */
    public BoundingCircle(Tuple2f<?> p, float radius) {
        this.center.set(p);
        this.radius = Math.abs(radius);
    	this.isBoundInit = true;
    }

    /**
     * @param bboxList are the bounding objects used to initialize this bounding sphere
     */
	public BoundingCircle(Collection<? extends BoundingCircle> bboxList) {
		combineBounds(false,bboxList);
    	this.isBoundInit = true;
	}
	
    /**
     * @param bboxList are the bounding objects used to initialize this bounding sphere
     */
	public BoundingCircle(CombinableBounds2D... bboxList) {
		combineBounds(false,Arrays.asList(bboxList));
    	this.isBoundInit = true;
	}
	
	/** {@inheritDoc}
	 */
	@Override
	public final BoundingPrimitiveType2D getBoundType() {
		return BoundingPrimitiveType2D.CIRCLE;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public BoundingCircle clone() {
		BoundingCircle clone = (BoundingCircle)super.clone();
		clone.center = (EuclidianPoint2D)this.center.clone();
		return clone;
	}

	/** {@inheritDoc}
	 */
	@Override
	public BoundingSphere toBounds3D() {
		return toBounds3D(CoordinateSystem3D.getDefaultCoordinateSystem());
	}

	/** {@inheritDoc}
	 */
	@Override
	public BoundingSphere toBounds3D(float z, float zsize) {
		return toBounds3D(z, zsize, CoordinateSystem3D.getDefaultCoordinateSystem());
	}

	/** {@inheritDoc}
	 */
	@Override
	public BoundingSphere toBounds3D(CoordinateSystem3D system) {
		if (!isInit()) return new BoundingSphere();		
		return new BoundingSphere(
				system.fromCoordinateSystem2D(this.center),
				this.radius);
	}

	/** {@inheritDoc}
	 */
	@Override
	public BoundingSphere toBounds3D(float z, float zsize, CoordinateSystem3D system) {
		if (!isInit()) return new BoundingSphere();		
		return new BoundingSphere(
				system.fromCoordinateSystem2D(this.center, z),
				this.radius);
	}

	/** {@inheritDoc}
	 *
	 * @param o {@inheritDoc}
	 * @return {@inheritDoc}
	 */
	@Override
	public boolean equals(Object o) {
		if (!isInit()) return false;
		if (this==o) return true;
		if (o instanceof BoundingCircle) {
			BoundingCircle s = (BoundingCircle)o;
			return (this.center.equals(s.center) &&
					this.radius==s.radius);
		}
		return false;
	}

	/** {@inheritDoc}
	 */
	@Override
	public void reset() {
		if (this.isBoundInit) {
			this.isBoundInit = false;
			this.center.set(0,0);
			this.radius = 0;
			this.vertices = null;
		}
	}

	/** {@inheritDoc}
	 *
	 * @return {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuilder s = new StringBuilder();
    	s.append("BoundingSphere["); //$NON-NLS-1$
    	s.append(this.center.getX());
    	s.append(',');
    	s.append(this.center.getY());
    	s.append("]R"); //$NON-NLS-1$
    	s.append(this.radius);
    	
    	return s.toString();
    }

    /** Replies the radius of this sphere.
     * 
     * @return the radius of this sphere.
     */
    public float getRadius() {
        return this.radius;
    }
    
    /** Set the radius of this sphere.
     * <p>
     * If the given radius is negative, the sphere radius is set to zero.
     * 
     * @param r is the radius of this sphere.
     */
    public void setRadius(float r) {
    	this.isBoundInit = true;
        this.radius = Math.max(0,r);
		this.vertices = null;
    }
    
	/** {@inheritDoc}
	 */
	@Override
	public boolean isEmpty() {
		return this.radius==0;
	}

	/** Set the bounds with the given sphere.
	 * <p>
	 * If the given radius is negative, the sphere radius is set to zero.
	 * 
	 * @param p
	 * @param radius
	 */
	public void set(Tuple2f<?> p, float radius) {
    	this.isBoundInit = true;
		this.center.set(p);
		this.radius = Math.max(0, radius);
		this.vertices = null;
	}

	/** {@inheritDoc}
	 */
	@Override
	public void getLowerUpper(Point2f lower, Point2f upper) {
		assert(lower!=null);
		assert(upper!=null);
		lower.set(this.center.getX()-this.radius,
				this.center.getY()-this.radius);
		upper.set(this.center.getX()+this.radius,
				this.center.getY()+this.radius);
	}

	/** {@inheritDoc}
	 */
	@Override
	public EuclidianPoint2D getLower() {
		return new EuclidianPoint2D(
				this.center.getX()-this.radius,
				this.center.getY()-this.radius);
	}

	/** {@inheritDoc}
	 */
	@Override
	public EuclidianPoint2D getUpper() {
		return new EuclidianPoint2D(
				this.center.getX()+this.radius,
				this.center.getY()+this.radius);
	}

	/** {@inheritDoc}
	 */
	@Override
	public EuclidianPoint2D getCenter() {
		return this.center;
	}
	
	private void ensureVertices() {
		if(this.vertices == null) {
			this.vertices = new Vector2f[4];
			float r = getRadius();
			
			this.vertices[0] = new Vector2f( r, r);
			this.vertices[1] = new Vector2f(-r, r);
			this.vertices[2] = new Vector2f(-r,-r);
			this.vertices[3] = new Vector2f( r,-r);
		}		
	}
	
	/** {@inheritDoc}
	 */
	@Override
	public SizedIterator<Vector2f> getLocalOrientedBoundVertices() {
		ensureVertices();
		return ArrayUtil.sizedIterator(this.vertices);
	}
	
	/** {@inheritDoc}
	 */
	@Override
	public Vector2f getLocalVertexAt(int index) {
		ensureVertices();
		return this.vertices[index];
	}

	/** {@inheritDoc}
	 */
	@Override
	public int getVertexCount() {
		ensureVertices();
		return this.vertices.length;
	}

	/** {@inheritDoc}
	 */
	@Override
	public SizedIterator<EuclidianPoint2D> getGlobalOrientedBoundVertices() {
		return new LocalToGlobalVertexIterator(getCenter(), getLocalOrientedBoundVertices());
	}

	/** {@inheritDoc}
	 */
	@Override
	public EuclidianPoint2D getGlobalVertexAt(int index) {
		ensureVertices();
		EuclidianPoint2D p = new EuclidianPoint2D(getCenter());
		p.add(this.vertices[index]);
		return p;
	}

	/** {@inheritDoc}
	 */
	@Override
	public Vector2f[] getOrientedBoundAxis() {
		return new Vector2f[] {
				new Vector2f(1.,0.),
				new Vector2f(0.,1.)
		};
	}

	/** {@inheritDoc}
	 */
	@Override
	public Vector2f getOrientedBoundExtentVector() {
		return new Vector2f(this.radius, this.radius);
	}

	/** {@inheritDoc}
	 */
	@Override
	public float[] getOrientedBoundExtents() {
		return new float[] {
				this.radius,
				this.radius
		};
	}

	/** {@inheritDoc}
	 */
	@Override
	public Vector2f getR() {
		return new Vector2f(1.,0.);
	}
	
	/** {@inheritDoc}
	 */
	@Override
	public Vector2f getS() {
		return new Vector2f(0.,1.);
	}

	/** {@inheritDoc}
	 */
	@Override
	public float getRExtent() {
		return this.radius;
	}

	/** {@inheritDoc}
	 */
	@Override
	public float getSExtent() {
		return this.radius;
	}

	/**
	 * Gets the center of this bounding box
	 * 
	 * @param centerPoint is the point to set with the coordinates of the sphere's center.
	 */
	public void getCenter(Point2f centerPoint) {
		centerPoint.set(this.center);
	}

	/** {@inheritDoc}
	 */
	@Override
	public float getSizeX() {
		if (!this.isBoundInit) return Float.NaN;
		return this.radius * 2.f;
	}

	/** {@inheritDoc}
	 */
	@Override
	public float getSizeY() {
		if (!this.isBoundInit) return Float.NaN;
		return this.radius * 2.f;
	}

	/** {@inheritDoc}
	 */
	@Override
	public Vector2f getSize() {
		if (!this.isBoundInit) return null;
		float s = this.radius*2;
		return new Vector2f(s,s);
	}
    
	/** Add the point into the bounds.
	 * 
	 * @param ptX
	 * @param ptY
	 */
	public void combine(float ptX, float ptY) {
		if (!this.isBoundInit) {
	    	this.isBoundInit = true;
			this.center.set(ptX, ptY);
			this.radius = 0.f;
		}
		else {
			float d = MathUtil.distance(ptX,ptY,this.center.getX(),this.center.getY());
			if (d>this.radius) {
				d = (d+this.radius) / 2.f;
				Vector2f v = new Vector2f(this.center);
				v.setX(v.getX() - ptX);
				v.setY(v.getY() - ptY);
				v.normalize();
				v.scale(d);
				this.center.set(ptX,ptY);
				this.center.add(v);
				this.radius = d;
			}
			this.vertices = null;
		}
	}
	
	/** Add the point into the bounds.
	 */
	@Override
	protected void combinePoints(boolean isAlreadyInit, Collection<? extends Tuple2f> pointList) {
		if (pointList!=null && !pointList.isEmpty()) {
			if (!isAlreadyInit) {
				float minx, miny;
				float maxx, maxy;
				
				minx = miny = Float.POSITIVE_INFINITY;
				maxx = maxy = Float.NEGATIVE_INFINITY;
				
				for(Tuple2f<?> point : pointList) {
					if (point!=null) {
						if (point.getX()<minx) minx = point.getX();
						if (point.getY()<miny) miny = point.getY();
						if (point.getX()>maxx) maxx = point.getX();
						if (point.getY()>maxy) maxy = point.getY();
					}
				}
				
				this.center.set(
						(minx+maxx) / 2.f,
						(miny+maxy) / 2.f);
				this.radius = 0;
				float distance;
				for(Tuple2f<?> point : pointList) {
					distance = MathUtil.distance(this.center.getX(),this.center.getY(),point.getX(),point.getY());
					if(distance>this.radius)
						this.radius = distance;
				}
					
				this.vertices = null;
				this.isBoundInit = true;
			}
			else {
				
				for(Tuple2f<?> point : pointList) {
			    	combine(point.getX(),point.getY());
				}
				
			}
		}
	}
	
	/** Add the bounds into the bounds.
	 */
	@Override
	protected void combineBounds(boolean isAlreadyInit, Collection<? extends Bounds2D> bounds) {
		if (bounds!=null && bounds.size()>0) {
			Point2f tmpCenter = new Point2f(this.center);
			float sphereRadius = this.radius;
			
			if (!isAlreadyInit) {
				// Compute the center of the set of spheres
				float minx, miny;
				float maxx, maxy;
				
				minx = miny = Float.POSITIVE_INFINITY;
				maxx = maxy = Float.NEGATIVE_INFINITY;
				
				for(Bounds2D b : bounds) {
					if (b.isInit()) {
						if (b.getMinX()<minx) minx = b.getMinX();
						if (b.getMinY()<miny) miny = b.getMinY();
						if (b.getMaxX()>maxx) maxx = b.getMaxX();
						if (b.getMaxY()>maxy) maxy = b.getMaxY();
					}
				}
				
				tmpCenter.setX((minx+maxx) / 2.f);
				tmpCenter.setY((miny+maxy) / 2.f);
				
				sphereRadius = 0;
			}
			
			// Update the radius
			float distanceMax, r2;
			float d1, d2, d3;
			Point2f bCenter;
			Vector2f v = new Vector2f();
			
			for(Bounds2D b : bounds) {
				if (b.isInit()) {
					// We assume that b could be viewed as a sphere, named bs
					// bs is a sphere with the same center as b and which encloses b
					bCenter = b.getCenter();
					
					// Compute the distance from the current center to the farest point of b
					distanceMax = b.distanceMax(tmpCenter);
					
					// v is the vector from the b's center to the current center
					v.sub(tmpCenter, bCenter);

					// r2 is the radius of bs
					r2 = distanceMax - v.length();
					
					//Compute diameter candidate
					d1 = 2.f*sphereRadius;
					d2 = 2.f*r2;
					d3 = distanceMax + sphereRadius;

					if (d2>d3 && d2>d1) {
						// d2 is the higher value:
						// b is enclosing the current sphere
						tmpCenter.set(bCenter);
						sphereRadius = r2;
					}
					else if (d3>d2 && d3>d1) {
						// d3 is the higher value:
						// b is not enclosing the current sphere and
						// the current sphere is not enclosing b
						v.scale(sphereRadius / (sphereRadius+r2));
						tmpCenter.add(bCenter,v);
						sphereRadius = d3 / 2.f;
						
					}
					// ELSE
					// d1 is the higher value:
					// the current sphere is enclosing b.
					// Nothing to change.
				}
			}
			
			this.center.set(tmpCenter);
			this.radius = sphereRadius;
			this.vertices = null;
			this.isBoundInit = true;
		}
	}
	
	/** {@inheritDoc}
	 */
	@Override
	public float distance(Point2f p) {
		if (!isInit()) return Float.NaN;
		float d = MathUtil.distance(this.center, p);
		return (d<this.radius) ? 0 : d-this.radius;
	}
	
	/** {@inheritDoc}
	 */
	@Override
	public float distanceSquared(Point2f p) {
		if (!isInit()) return Float.NaN;
		float d = MathUtil.distance(this.center, p);
		d = (d<this.radius) ? 0 : d-this.radius;
		return d * d;
	}

	/** {@inheritDoc}
	 */
	@Override
	public EuclidianPoint2D nearestPoint(Point2f reference) {
		if (!isInit()) return null;
		EuclidianPoint2D nearest = new EuclidianPoint2D();
		Vector2f v = new Vector2f();
		v.sub(reference, this.center);
		if (v.length()<=this.radius) {
			nearest.set(reference);
		}
		else {
			v.normalize();
			v.scale(this.radius);
			nearest.add(this.center, v);
		}
		return nearest;
	}

	/** {@inheritDoc}
	 */
	@Override
	public float distanceMax(Point2f p) {
		if (!isInit()) return Float.NaN;
		return MathUtil.distance(this.center,p)+this.radius;
	}

	/** {@inheritDoc}
	 */
	@Override
	public float distanceMaxSquared(Point2f p) {
		if (!isInit()) return Float.NaN;
		float d = MathUtil.distance(this.center,p)+this.radius;
		return d * d;
	}

	/** {@inheritDoc}
	 */
	@Override
	public EuclidianPoint2D farestPoint(Point2f reference) {
		if (!isInit()) return null;
		EuclidianPoint2D farest = new EuclidianPoint2D();
		Vector2f v = new Vector2f();
		v.sub(this.center, reference);
		if (v.length()==0.) {
			farest.set(this.center.getX()+this.radius, this.center.getY());
		}
		else {
			v.normalize();
			v.scale(this.radius);
			farest.add(this.center, v);
		}
		return farest;
	}

	/** {@inheritDoc}
	 */
	@Override
	public void translate(Vector2f v) {
		if (isInit()) {
			this.center.add(v);
			this.vertices = null;
		}
	}
	
	/** {@inheritDoc}
	 */
	@Override
	public boolean isInit() {
		return this.isBoundInit;
	}

	//-------------------------------------
	// IntersectionClassification
	//-------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IntersectionType classifies(Point2f p) {
		if (!isInit()) return IntersectionType.OUTSIDE;
		return (p.distance(this.center) <= this.radius) ?
				IntersectionType.INSIDE : IntersectionType.OUTSIDE;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean intersects(Point2f p) {
		if (!isInit()) return false;
		return p.distance(this.center) <= this.radius;
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public IntersectionType classifies(Point2f c, float r) {
		if (!isInit()) return IntersectionType.OUTSIDE;
		float d = MathUtil.distance(this.center, c);
		if (d>this.radius+r) return IntersectionType.OUTSIDE;
		if ((d+r)<=this.radius) return IntersectionType.INSIDE;
		if ((d+this.radius)<=r) return IntersectionType.ENCLOSING;
		return IntersectionType.SPANNING;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean intersects(Point2f c, float r) {
		if (!isInit()) return false;
		return c.distance(this.center) <= (this.radius+r);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IntersectionType classifies(Point2f l, Point2f u) {
		assert(l.getX()<=u.getX());
		assert(l.getY()<=u.getY());
		if (!isInit()) return IntersectionType.OUTSIDE;
		return IntersectionUtil.classifiesSolidCircleSolidAlignedRectangle(
				this.center, this.radius, l, u).invert();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean intersects(Point2f l, Point2f u) {
		if (!isInit()) return false;
		return IntersectionUtil.intersectsSolidCircleSolidAlignedRectangle(
				this.center, this.radius, l, u);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IntersectionType classifies(Point2f obrCenter, Vector2f[] obrAxis, float[] obrExtent) {
		if (!isInit()) return IntersectionType.OUTSIDE;
		return IntersectionUtil.classifiesSolidCircleOrientedRectangle(
				this.center, this.radius,
				obrCenter, obrAxis, obrExtent).invert();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean intersects(Point2f obrCenter, Vector2f[] obrAxis, float[] obrExtent) {
		if (!isInit()) return false;
		return IntersectionUtil.intersectsSolidCircleOrientedRectangle(
				this.center, this.radius,
				obrCenter, obrAxis, obrExtent);
	}

	@Override
	public float area() {
		return (float) (Math.pow(this.radius,2)*Math.PI);
	}

}
