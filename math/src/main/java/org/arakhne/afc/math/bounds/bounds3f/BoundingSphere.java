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

package org.arakhne.afc.math.bounds.bounds3f;

import java.util.Arrays;
import java.util.Collection;

import org.arakhne.afc.math.MathUtil;
import org.arakhne.afc.math.bounds.bounds2f.BoundingCircle;
import org.arakhne.afc.math.continous.object3d.Point3f;
import org.arakhne.afc.math.continous.object3d.Quaternion;
import org.arakhne.afc.math.continous.object3d.Tuple3f;
import org.arakhne.afc.math.continous.object3d.Vector3f;
import org.arakhne.afc.math.continous.object4d.AxisAngle4f;
import org.arakhne.afc.math.euclide.EuclidianPoint3D;
import org.arakhne.afc.math.intersection.IntersectionType;
import org.arakhne.afc.math.intersection.IntersectionUtil;
import org.arakhne.afc.math.plane.PlanarClassificationType;
import org.arakhne.afc.math.plane.Plane;
import org.arakhne.afc.math.system.CoordinateSystem3D;
import org.arakhne.afc.sizediterator.SizedIterator;
import org.arakhne.afc.util.ArrayUtil;

/**
 * A Bounding Sphere.
 * 
 * @author $Author: sgalland$
 * @author $Author: ngaud$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class BoundingSphere extends AbstractCombinableBounds3D implements TranslatableBounds3D, AlignedCombinableBounds3D, RotatableBounds3D, OrientedCombinableBounds3D {

	private static final long serialVersionUID = -6184671747906774171L;
	
	/**
	 * The vertices that composes this box, it is compiled when it is required not before
	 */
	private transient Vector3f[] vertices = null;

    /** Coordinate of the center.
     */
	EuclidianPoint3D center = new EuclidianPoint3D();
    
    /** Radius of the sphere.
     */
    float radius;   
    
    private boolean isBoundInit;
    
    /** Uninitialized bounding object.
     */
    public BoundingSphere() {
    	this.radius = 1;
    	this.isBoundInit = false;
    }
    
    /** 
     * @param x is the center of the sphere.
     * @param y is the center of the sphere.
     * @param z is the center of the sphere.
     * @param radius is the radius of the sphere.
     */
    public BoundingSphere(float x, float y, float z, float radius) {
        this.center.set(x,y,z);
        this.radius = Math.abs(radius);
    	this.isBoundInit = true;
    }
    
    /** 
     * @param p is the center of the sphere.
     * @param radius is the radius of the sphere.
     */
    public BoundingSphere(Tuple3f<?> p, float radius) {
        this.center.set(p);
        this.radius = Math.abs(radius);
    	this.isBoundInit = true;
    }

    /**
     * @param bboxList are the bounding objects used to initialize this bounding sphere
     */
	public BoundingSphere(Collection<? extends BoundingSphere> bboxList) {
		combineBounds(false,bboxList);
    	this.isBoundInit = true;
	}
	
    /**
     * @param bboxList are the bounding objects used to initialize this bounding sphere
     */
	public BoundingSphere(CombinableBounds3D... bboxList) {
		combineBounds(false,Arrays.asList(bboxList));
    	this.isBoundInit = true;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public final BoundingPrimitiveType3D getBoundType() {
		return BoundingPrimitiveType3D.SPHERE;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public BoundingSphere clone() {
		BoundingSphere clone = (BoundingSphere)super.clone();
		clone.center = (EuclidianPoint3D)this.center.clone();
		return clone;
	}

	/** {@inheritDoc}
	 */
	@Override
	public boolean equals(Object o) {
		if (!isInit()) return false;
		if (this==o) return true;
		if (o instanceof BoundingSphere) {
			BoundingSphere s = (BoundingSphere)o;
			return (this.center.equals(s.center) &&
					this.radius==s.radius);
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void reset() {
		if (this.isBoundInit) {
			this.isBoundInit = false;
			this.center.set(0,0,0);
			this.radius = 0;
			this.vertices = null;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuilder s = new StringBuilder();
    	s.append("BoundingSphere["); //$NON-NLS-1$
    	s.append(this.center.getX());
    	s.append(',');
    	s.append(this.center.getY());
    	s.append(',');
    	s.append(this.center.getZ());
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
    
	/**
	 * {@inheritDoc}
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
	public void set(Tuple3f<?> p, float radius) {
    	this.isBoundInit = true;
		this.center.set(p);
		this.radius = Math.max(0, radius);
		this.vertices = null;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void getLowerUpper(Point3f lower, Point3f upper) {
		assert(lower!=null);
		assert(upper!=null);
		lower.set(
				this.center.getX()-this.radius,
				this.center.getY()-this.radius,
				this.center.getZ()-this.radius);
		upper.set(
				this.center.getX()+this.radius,
				this.center.getY()+this.radius,
				this.center.getZ()+this.radius);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public EuclidianPoint3D getLower() {
		return new EuclidianPoint3D(
				this.center.getX()-this.radius,
				this.center.getY()-this.radius,
				this.center.getZ()-this.radius);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public EuclidianPoint3D getUpper() {
		return new EuclidianPoint3D(
				this.center.getX()+this.radius,
				this.center.getY()+this.radius,
				this.center.getZ()+this.radius);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public EuclidianPoint3D getCenter() {
		return this.center;
	}
	
	private void ensureVertices() {
		if(this.vertices == null) {
			this.vertices = new Vector3f[8];
			float r = getRadius();
			
			this.vertices[0] = new Vector3f( r, r, r);
			this.vertices[1] = new Vector3f(-r, r, r);
			this.vertices[2] = new Vector3f(-r,-r, r);
			this.vertices[3] = new Vector3f( r,-r, r);
			this.vertices[4] = new Vector3f( r,-r,-r);
			this.vertices[5] = new Vector3f( r, r,-r);
			this.vertices[6] = new Vector3f(-r, r,-r);
			this.vertices[7] = new Vector3f(-r,-r,-r);
		}		
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public SizedIterator<Vector3f> getLocalOrientedBoundVertices() {
		ensureVertices();
		return ArrayUtil.sizedIterator(this.vertices);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Vector3f getLocalVertexAt(int index) {
		ensureVertices();
		return this.vertices[index];
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getVertexCount() {
		ensureVertices();
		return this.vertices.length;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public SizedIterator<EuclidianPoint3D> getGlobalOrientedBoundVertices() {
		return new LocalToGlobalVertexIterator(getCenter(), getLocalOrientedBoundVertices());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public EuclidianPoint3D getGlobalVertexAt(int index) {
		ensureVertices();
		EuclidianPoint3D p = new EuclidianPoint3D(getCenter());
		p.add(this.vertices[index]);
		return p;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Vector3f[] getOrientedBoundAxis() {
		return new Vector3f[] {
				new Vector3f(1.,0.,0.),
				new Vector3f(0.,1.,0.),
				new Vector3f(0.,0.,1.)
		};
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Vector3f getOrientedBoundExtentVector() {
		return new Vector3f(this.radius, this.radius, this.radius);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public float[] getOrientedBoundExtents() {
		return new float[] {
				this.radius,
				this.radius,
				this.radius
		};
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Vector3f getR() {
		return new Vector3f(1.,0.,0.);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Vector3f getS() {
		return new Vector3f(0.,1.,0.);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Vector3f getT() {
		return new Vector3f(0.,0.,1.);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public float getRExtent() {
		return this.radius;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public float getSExtent() {
		return this.radius;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public float getTExtent() {
		return this.radius;
	}

	/**
	 * Gets the center of this bounding box
	 * 
	 * @param centerPoint is the point to set with the coordinates of the sphere's center.
	 */
	public void getCenter(Point3f centerPoint) {
		centerPoint.set(this.center);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public float getSizeX() {
		if (!this.isBoundInit) return Float.NaN;
		return this.radius * 2.f;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public float getSizeY() {
		if (!this.isBoundInit) return Float.NaN;
		return this.radius * 2.f;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public float getSizeZ() {
		if (!this.isBoundInit) return Float.NaN;
		return this.radius * 2.f;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Vector3f getSize() {
		if (!this.isBoundInit) return null;
		float s = this.radius*2;
		return new Vector3f(s,s,s);
	}
    
	/** Add the point into the bounds.
	 * 
	 * @param ptX
	 * @param ptY
	 * @param ptZ
	 */
	public void combine(float ptX, float ptY, float ptZ) {
		if (!this.isBoundInit) {
	    	this.isBoundInit = true;
			this.center.set(ptX, ptY, ptZ);
			this.radius = 0.f;
		}
		else {
			float d = MathUtil.distance(ptX,ptY,ptZ,this.center.getX(),this.center.getY(),this.center.getZ());
			if (d>this.radius) {
				d = (d + this.radius)/2.f;
				Vector3f v = new Vector3f(this.center);
				v.setX(v.getX() - ptX);
				v.setY(v.getY() - ptY);
				v.setZ(v.getZ() - ptZ);
				v.normalize();
				v.scale(d);
				this.center.set(ptX,ptY,ptZ);
				this.center.add(v);
				this.radius = d;
			}
			this.vertices = null;
		}
	}
	
	/** Add the point into the bounds.
	 */
	@Override
	protected void combinePoints(boolean isAlreadyInit, Collection<? extends Tuple3f> pointList) {
		if (pointList!=null && !pointList.isEmpty()) {
			if (!isAlreadyInit) {
				float minx, miny, minz;
				float maxx, maxy, maxz;
				
				minx = miny = minz = Float.POSITIVE_INFINITY;
				maxx = maxy = maxz = Float.NEGATIVE_INFINITY;
				
				for(Tuple3f<?> point : pointList) {
					if (point!=null) {
						if (point.getX()<minx) minx = point.getX();
						if (point.getY()<miny) miny = point.getY();
						if (point.getZ()<minz) minz = point.getZ();
						if (point.getX()>maxx) maxx = point.getX();
						if (point.getY()>maxy) maxy = point.getY();
						if (point.getZ()>maxz) maxz = point.getZ();
					}
				}
				
				this.center.set(
						(minx+maxx) / 2.f,
						(miny+maxy) / 2.f,
						(minz+maxz) / 2.f);
				this.radius = 0;
				float distance;
				for(Tuple3f<?> point : pointList) {
					distance = MathUtil.distance(this.center.getX(),this.center.getY(),this.center.getZ(),point.getX(),point.getY(),point.getZ());
					if(distance>this.radius)
						this.radius = distance;
				}
					
				this.vertices = null;
				this.isBoundInit = true;
			}
			else {
				
				for(Tuple3f<?> point : pointList) {
			    	combine(point.getX(),point.getY(),point.getZ());
				}
				
			}
		}
	}
	
	/** Add the bounds into the bounds.
	 */
	@Override
	protected void combineBounds(boolean isAlreadyInit, Collection<? extends Bounds3D> bounds) {
		if (bounds!=null && bounds.size()>0) {
			Point3f tmpCenter = new Point3f(this.center);
			float sphereRadius = this.radius;
			
			if (!isAlreadyInit) {
				// Compute the center of the set of spheres
				float minx, miny, minz;
				float maxx, maxy, maxz;
				
				minx = miny = minz = Float.POSITIVE_INFINITY;
				maxx = maxy = maxz = Float.NEGATIVE_INFINITY;
				
				for(Bounds3D b : bounds) {
					if (b.isInit()) {
						if (b.getMinX()<minx) minx = b.getMinX();
						if (b.getMinY()<miny) miny = b.getMinY();
						if (b.getMinZ()<minz) minz = b.getMinZ();
						if (b.getMaxX()>maxx) maxx = b.getMaxX();
						if (b.getMaxY()>maxy) maxy = b.getMaxY();
						if (b.getMaxZ()>maxz) maxz = b.getMaxZ();
					}
				}
				
				tmpCenter.setX((minx+maxx) / 2.f);
				tmpCenter.setY((miny+maxy) / 2.f);
				tmpCenter.setZ((minz+maxz) / 2.f);
				
				sphereRadius = 0;
			}
			
			// Update the radius
			float distanceMax, r2;
			float d1, d2, d3;
			Point3f bCenter;
			Vector3f v = new Vector3f();
			
			for(Bounds3D b : bounds) {
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
	public float distance(Point3f p) {
		if (!isInit()) return Float.NaN;
		float d = MathUtil.distance(this.center, p);
		return (d<this.radius) ? 0 : d-this.radius;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public float distanceSquared(Point3f p) {
		if (!isInit()) return Float.NaN;
		float d = MathUtil.distance(this.center, p);
		d = (d<this.radius) ? 0 : d-this.radius;
		return d * d;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public EuclidianPoint3D nearestPoint(Point3f reference) {
		if (!isInit()) return null;
		EuclidianPoint3D nearest = new EuclidianPoint3D();
		Vector3f v = new Vector3f();
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
	public float distanceMax(Point3f p) {
		if (!isInit()) return Float.NaN;
		return MathUtil.distance(this.center,p)+this.radius;
	}

	/** {@inheritDoc}
	 */
	@Override
	public float distanceMaxSquared(Point3f p) {
		if (!isInit()) return Float.NaN;
		float d = MathUtil.distance(this.center,p)+this.radius;
		return d * d;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public EuclidianPoint3D farestPoint(Point3f reference) {
		if (!isInit()) return null;
		EuclidianPoint3D farest = new EuclidianPoint3D();
		Vector3f v = new Vector3f();
		v.sub(this.center, reference);
		if (v.length()==0.) {
			farest.set(this.center.getX()+this.radius, this.center.getY(), this.center.getZ());
		}
		else {
			v.normalize();
			v.scale(this.radius);
			farest.add(this.center, v);
		}
		return farest;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void translate(Vector3f v) {
		if (isInit()) {
			this.center.add(v);
			this.vertices = null;
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
		if (isInit()) {
			if (onGround) {
				CoordinateSystem3D cs = CoordinateSystem3D.getDefaultCoordinateSystem();
				Point3f np = new Point3f(position);
				cs.addHeight(np, getHeight(cs)/2.f);
				this.center.set(np);
			}
			else {
				this.center.set(position);
			}
			this.vertices = null;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setTranslation(Point3f position) {
		if (isInit()) {
			this.center.set(position);
			this.vertices = null;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isInit() {
		return this.isBoundInit;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public BoundingCircle toBounds2D() {
		return toBounds2D(CoordinateSystem3D.getDefaultCoordinateSystem());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public BoundingCircle toBounds2D(CoordinateSystem3D system) {
		if (!isInit()) return new BoundingCircle();
		return new BoundingCircle(
				system.toCoordinateSystem2D(this.center),
				this.radius);
	}

	//-------------------------------------
	// IntersectionClassification
	//-------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IntersectionType classifies(Point3f p) {
		if (!isInit()) return IntersectionType.OUTSIDE;
		return (p.distance(this.center) <= this.radius) ?
				IntersectionType.INSIDE : IntersectionType.OUTSIDE;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean intersects(Point3f p) {
		if (!isInit()) return false;
		return p.distance(this.center) <= this.radius;
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public IntersectionType classifies(Point3f c, float r) {
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
	public boolean intersects(Point3f c, float r) {
		if (!isInit()) return false;
		return c.distance(this.center) <= (this.radius+r);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IntersectionType classifies(Point3f l, Point3f u) {
		assert(l.getX()<=u.getX());
		assert(l.getY()<=u.getY());
		assert(l.getZ()<=u.getZ());
		if (!isInit()) return IntersectionType.OUTSIDE;
		return IntersectionUtil.classifiesSolidSphereSolidAlignedBox(
				this.center, this.radius, l, u).invert();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean intersects(Point3f l, Point3f u) {
		if (!isInit()) return false;
		return IntersectionUtil.intersectsSolidSphereSolidAlignedBox(
				this.center, this.radius, l, u);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IntersectionType classifies(Plane plane) {
		if (!isInit()) return IntersectionType.OUTSIDE;
		return (plane.classifies(
				this.center.getX(), this.center.getY(), this.center.getZ(), this.radius) == PlanarClassificationType.COINCIDENT)
			? IntersectionType.SPANNING : IntersectionType.OUTSIDE;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean intersects(Plane plane) {
		if (!isInit()) return false;
		return plane.intersects(
				this.center.getX(), this.center.getY(), this.center.getZ(), this.radius);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PlanarClassificationType classifiesAgainst(Plane plane) {
		if (!isInit()) return null;
		return (plane.classifies(
				this.center.getX(), this.center.getY(), this.center.getZ(), this.radius));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IntersectionType classifies(Point3f obbCenter, Vector3f[] obbAxis, float[] obbExtent) {
		if (!isInit()) return IntersectionType.OUTSIDE;
		return IntersectionUtil.classifiesSolidSphereOrientedBox(
				this.center, this.radius,
				obbCenter, obbAxis, obbExtent).invert();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean intersects(Point3f obbCenter, Vector3f[] obbAxis, float[] obbExtent) {
		if (!isInit()) return false;
		return IntersectionUtil.intersectsSolidSphereOrientedBox(
				this.center, this.radius,
				obbCenter, obbAxis, obbExtent);
	}

	//-------------------------------------
	// RotatableBounds3D
	//-------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void rotate(AxisAngle4f rotation) {
		//
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void rotate(Quaternion rotation) {
		//
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setRotation(AxisAngle4f rotation) {
		//
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setRotation(Quaternion rotation) {
		//
	}

	@Override
	public float area() {
		return (float) (4*Math.pow(this.radius,3)*Math.PI/3);
	}

}
