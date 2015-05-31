/* 
 * $Id$
 * 
 * Copyright (c) 2013 Christophe BOHRHAUER.
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
package org.arakhne.afc.math.geometry.sfc.geometry3d.continuous.convexhull;

import org.arakhne.afc.math.geometry.sfc.geometry3d.continuous.PlanarClassificationType;
import org.arakhne.afc.math.geometry.sfc.geometry3d.continuous.Plane;
import org.arakhne.afc.math.geometry.sfc.geometry3d.continuous.Plane4f;
import org.arakhne.afc.math.geometry.sfc.geometry3d.continuous.PlaneClassifier;
import org.arakhne.afc.math.geometry.sfc.geometry3d.continuous.Point3f;
import org.arakhne.afc.math.geometry.sfc.geometry3d.continuous.Tuple3f;
import org.arakhne.afc.math.geometry.sfc.geometry3d.continuous.Vector3f;
import org.arakhne.afc.math.sfc.MathUtil;

/** This class represents a 3D triangle used inside
 * Convex Hull Computation Algorithms.
 * 
 * @see ConvexHull
 * @param <T> is the type of the points.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class HullTriangle3D<T extends Point3f> implements HullObject<T>, PlaneClassifier {

	/** Index of the first point of the triangle.
	 */
	public final int a;

	/** Index of the second point of the triangle.
	 */
	public final int b;

	/** Index of the third point of the triangle.
	 */
	public final int c;

	/** normal to the triangle facet.
	 */
	public final float nx;
	/** normal to the triangle facet.
	 */
	public final float ny;
	/** normal to the triangle facet.
	 */
	public final float nz;
	/** normal to the triangle facet.
	 */
	public final float nw;
	
	/** Level inside the creation algorithm.
	 */
	private final int creationLevel;

	/**
	 * @param points is the list of points
	 * @param a is the index of the first point in the list of points.
	 * @param b is the index of the second point in the list of points.
	 * @param c is the index of the third point in the list of points.
	 * @param creationLevel is the level in the creation algorithm.
	 */
	public HullTriangle3D(T[] points, int a, int b, int c, int creationLevel) {
		this.a = a;
		this.b = b;
		this.c = c;
		this.creationLevel = creationLevel;

		T pa = points[this.a];
		T pb = points[this.b];
		T pc = points[this.c];
		
		float lnx = pa.getY() * (pb.getZ() - pc.getZ()) + pb.getY() * (pc.getZ() - pa.getZ()) + pc.getY() * (pa.getZ() - pb.getZ());
		float lny = pa.getZ() * (pb.getX() - pc.getX()) + pb.getZ() * (pc.getX() - pa.getX()) + pc.getZ() * (pa.getX() - pb.getX());
		float lnz = pa.getX() * (pb.getY() - pc.getY()) + pb.getX() * (pc.getY() - pa.getY()) + pc.getX() * (pa.getY() - pb.getY());
		float d = - (lnx * pa.getX() + lny * pa.getY() + lnz * pa.getZ());
		float t = (float) Math.sqrt(lnx*lnx+lny*lny+lnz*lnz);
		this.nx = lnx / t;
		this.ny = lny / t;
		this.nz = lnz / t;
		this.nw = d / t;
	}

	/** {@inheritDoc}
	 */
	@Override
	public int getCreationLevel() {
		return this.creationLevel;
	}

	/** {@inheritDoc}
	 * 
	 * @return {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "[Triangle3D, "+this.a+","+this.b+","+this.c+"]\n";  //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
	}
	
	/** Replies the normal to the triangle.
	 * 
	 * @return the normal to the facet.
	 * @see #nx
	 * @see #ny
	 * @see #nz
	 */
	public Vector3f getNormal() {
		return new Vector3f(this.nx,this.ny,this.nz);
	}

    /**
     * Classifies a point with respect to the plane.
     * Classifying a point with respect to a plane is done by passing the (x, y, z) values of the point into the plane equation,
     * Ax + By + Cz + D = 0. The result of this operation is the distance from the plane to the point along the plane's normal Vec3f.
     * It will be positive if the point is on the side of the plane pointed to by the normal Vec3f, negative otherwise.
     * If the result is 0, the point is on the plane.
     * 
     * @param x is the coordinate of the point
     * @param y is the coordinate of the point
     * @param z is the coordinate of the point
     * @return the distance from the given point to the triangle.
     */
    public float distanceTo(float x, float y, float z) {
    	return this.nx * x + this.ny * y + this.nz * z + this.nw;
    }
    
	/**
	 * Classifies a point with respect to the plane.
	 * <p>
	 * Classifying a point with respect to a plane is done by passing
	 * the (x, y, z) values of the point into the plane equation,
	 * Ax + By + Cz + D = 0. The result of this operation is the
	 * distance from the plane to the point along the plane's normal vector.
	 * It will be positive if the point is on the side of the
	 * plane pointed to by the normal Vec3f, negative otherwise.
	 * If the result is 0, the point is on the plane.
	 * 
	 * @param vec is the point to classify 
	 * @return the classification of the point against the plane.
	 */
    @Override
    public PlaneClassification classifies(Tuple3f vec) {
        PlaneClassification lc;
        int cmp = MathUtil.epsilonDistanceSign(distanceTo(vec));
        if (cmp < 0)
            lc = PlaneClassification.BEHIND;
        else if (cmp > 0)
            lc = PlaneClassification.IN_FRONT_OF;
        else
            lc = PlaneClassification.COINCIDENT;
        return lc;
    }
    
	/** {@inheritDoc}
	 */
	@Override
    public boolean intersects(Tuple3f vec) {
        return MathUtil.epsilonDistanceSign(distanceTo(vec))==0;
    }

    /**
	 * Classifies a point with respect to the plane.
	 * <p>
	 * Classifying a point with respect to a plane is done by passing
	 * the (x, y, z) values of the point into the plane equation,
	 * Ax + By + Cz + D = 0. The result of this operation is the
	 * distance from the plane to the point along the plane's normal vector.
	 * It will be positive if the point is on the side of the
	 * plane pointed to by the normal Vec3f, negative otherwise.
	 * If the result is 0, the point is on the plane.
	 * 
	 * @param x is the coordinate of the point to classify. 
	 * @param y is the coordinate of the point to classify. 
	 * @param z is the coordinate of the point to classify. 
	 * @return the classification of the point against the plane.
	 */
	@Override
    public PlaneClassification classifies(float x, float y, float z) {
        PlaneClassification lc;
        int cmp = MathUtil.epsilonDistanceSign(distanceTo(x,y,z));
        if (cmp < 0)
            lc = PlaneClassification.BEHIND;
        else if (cmp > 0)
            lc = PlaneClassification.IN_FRONT_OF;
        else
            lc = PlaneClassification.COINCIDENT;
        return lc;
    }

	/**
	 * Classifies a point with respect to the plane.
	 * <p>
	 * Classifying a point with respect to a plane is done by passing
	 * the (x, y, z) values of the point into the plane equation,
	 * Ax + By + Cz + D = 0. The result of this operation is the
	 * distance from the plane to the point along the plane's normal vector.
	 * It will be positive if the point is on the side of the
	 * plane pointed to by the normal Vec3f, negative otherwise.
	 * If the result is 0, the point is on the plane.
	 * 
	 * @param x is the coordinate of the point to classify. 
	 * @param y is the coordinate of the point to classify. 
	 * @param z is the coordinate of the point to classify. 
	 * @return the classification of the point against the plane.
	 */
	@Override
    public boolean intersects(float x, float y, float z) {
        return MathUtil.epsilonDistanceSign(distanceTo(x,y,z))==0;
    }

    /**
	 * Classifies a box with respect to the plane.
	 * <p>
	 * Classifying a point with respect to a plane is done by passing
	 * the (x, y, z) values of the point into the plane equation,
	 * Ax + By + Cz + D = 0. The result of this operation is the
	 * distance from the plane to the point along the plane's normal vector.
	 * It will be positive if the point is on the side of the
	 * plane pointed to by the normal Vec3f, negative otherwise.
	 * If the result is 0, the point is on the plane.
	 * 
	 * @param lx is the coordinate of the lower point of the box to classify. 
	 * @param ly is the coordinate of the lower point of the box to classify. 
	 * @param lz is the coordinate of the lower point of the box to classify. 
	 * @param ux is the coordinate of the upper point of the box to classify. 
	 * @param uy is the coordinate of the upper point of the box to classify. 
	 * @param uz is the coordinate of the upper point of the box to classify. 
	 * @return the classification of the box against the plane.
	 */
	@Override
    public PlaneClassification classifies(float lx, float ly, float lz, float ux, float uy, float uz) {
        float[] d = new float[] {
        				distanceTo(lx,ly,lz),
        				distanceTo(ux,ly,lz),
        				distanceTo(ux,uy,lz),
        				distanceTo(lx,uy,lz),
        				distanceTo(lx,ly,uz),
        				distanceTo(ux,ly,uz),
        				distanceTo(ux,uy,uz),
        				distanceTo(lx,uy,uz)
        };
        
        float sign = 0;
        for(int i=0,j=1; i<7; ++i,++j) {
        	sign = Math.signum(d[i]);
        	if ((sign==0)||(sign!=Math.signum(d[j]))) {
        		// Signs are different, it means that
        		// the box intersects the plan
        		return PlaneClassification.COINCIDENT;
        	}
        }
        
        return (sign<0) ? PlaneClassification.BEHIND : PlaneClassification.IN_FRONT_OF;
    }

	/** {@inheritDoc}
	 */
	@Override
    public boolean intersects(float lx, float ly, float lz, float ux, float uy, float uz) {
        float[] d = new float[] {
        				distanceTo(lx,ly,lz),
        				distanceTo(ux,ly,lz),
        				distanceTo(ux,uy,lz),
        				distanceTo(lx,uy,lz),
        				distanceTo(lx,ly,uz),
        				distanceTo(ux,ly,uz),
        				distanceTo(ux,uy,uz),
        				distanceTo(lx,uy,uz)
        };
        
        float sign = 0;
        for(int i=0,j=1; i<7; ++i,++j) {
        	sign = Math.signum(d[i]);
        	if ((sign==0)||(sign!=Math.signum(d[j]))) {
        		// Signs are different, it means that
        		// the box intersects the plan
        		return true;
        	}
        }
        
        return false;
    }

    /**
	 * Classifies a sphere with respect to the plane.
	 * <p>
	 * Classifying a point with respect to a plane is done by passing
	 * the (x, y, z) values of the point into the plane equation,
	 * Ax + By + Cz + D = 0. The result of this operation is the
	 * distance from the plane to the point along the plane's normal vector.
	 * It will be positive if the point is on the side of the
	 * plane pointed to by the normal Vec3f, negative otherwise.
	 * If the result is 0, the point is on the plane.
	 * 
	 * @param x is the coordinate of the center of the sphere to classify. 
	 * @param y is the coordinate of the center of the sphere to classify. 
	 * @param z is the coordinate of the center of the sphere to classify. 
	 * @param radius is the radius of the sphere to classify. 
	 * @return the classification of the sphere against the plane.
	 */
	@Override
    public PlaneClassification classifies(float x, float y, float z, float radius) {
        float d = Math.abs(distanceTo(x,y,z));
        int cmp = MathUtil.epsilonDistanceSign(d-radius); 
        if (cmp==0) return PlaneClassification.COINCIDENT; 
        if (d>radius) return PlaneClassification.IN_FRONT_OF;
        return PlaneClassification.BEHIND;
    }

	/** {@inheritDoc}
	 */
	@Override
    public boolean intersects(float x, float y, float z, float radius) {
        float d = Math.abs(distanceTo(x,y,z));
        return MathUtil.epsilonDistanceSign(d-radius)==0; 
    }

	/** {@inheritDoc}
	 */
	@Override
    public PlaneClassification classifies(Plane otherPlane) {
    	float distance = distanceTo(otherPlane);
    	
    	// The distance could not be computed
    	// the planes intersect.
    	// Planes intersect also when the distance
    	// is null
    	if ((distance==Float.NaN)||
    		(distance==0))
    		return PlaneClassification.COINCIDENT;
    	
    	if (distance>0) return PlaneClassification.IN_FRONT_OF;
    	return PlaneClassification.BEHIND;
    }

	/** {@inheritDoc}
	 */
	@Override
    public boolean intersects(Plane otherPlane) {
    	float distance = distanceTo(otherPlane);
    	// The distance could not be computed
    	// the planes intersect.
    	// Planes intersect also when the distance
    	// is null
    	return ((distance==Float.NaN)||(distance==0));
    }

    /**
     * Classifies a point with respect to the plane.
     * Classifying a point with respect to a plane is done by passing the (x, y, z) values of the point into the plane equation,
     * Ax + By + Cz + D = 0. The result of this operation is the distance from the plane to the point along the plane's normal Vec3f.
     * It will be positive if the point is on the side of the plane pointed to by the normal Vec3f, negative otherwise.
     * If the result is 0, the point is on the plane.
     * 
     * @param v is the point
     * @return the distance from the given point to this plane.
     */
    public final float distanceTo(Tuple3f v) {
    	return distanceTo(v.getX(), v.getY(), v.getZ());
    }
    
    /**
     * Compute the distance between two colinear planes.
     * 
     * @param p is a plane
     * @return the distance from the plane to the point along the plane's normal Vec3f.
     * It will be positive if the point is on the side of the plane pointed to by the normal Vec3f, negative otherwise.
     * If the result is 0, the point is on the plane. This function could replies
     * {@link Float#NaN} if the planes are not colinear.
     */
    public float distanceTo(Plane p) {
    	// Compute the normales
    	Vector3f oNormal = p.getNormal();
    	oNormal.normalize();
    	Vector3f mNormal = new Vector3f(this.nx,this.ny,this.nz);
    	mNormal.normalize();
    	
    	float dotProduct = oNormal.dot(mNormal);
    	
    	if (Math.abs(dotProduct)==1) {
    		// Planes are colinear.
    		// The problem could be restricted to a 1D problem.
    		
    		// Compute the coordinate of this pane
    		// assuming the origin is (0,0,0)
    		float c1 = -distanceTo(0,0,0);
    		
    		// Compute the coordinate of the other pane
    		// assuming the origin is (0,0,0)
    		float c2 = -p.distanceTo(0,0,0);
    		
    		if (dotProduct==-1) {
    			// The planes have not the same orientation.
    			// Reverse one coordinate.
    			c2 = -c2;
    		}
    		
    		return c2 - c1;

    	}
    	return Float.NaN;
    }

	/** {@inheritDoc}
	 */
	@Override
	public Point3f[] getObjectPoints(T[] points) {
		return new Point3f[] {
			points[this.a],
			points[this.b],
			points[this.c]
		};
	}

	/** {@inheritDoc}
	 */
	@Override
	public int[] getObjectIndexes() {
		return new int[] {
				this.a,
				this.b,
				this.c,
			};
	}
	
	/** Replies if this triangle is equals to the specified object.
	 */
	@Override
	public boolean equals(Object o) {
		if (o instanceof HullTriangle3D<?>) {
			int la = this.a;
			int lb = this.b;
			int lc = this.c;
			int d = ((HullTriangle3D<?>)o).a;
			int e = ((HullTriangle3D<?>)o).b;
			int f = ((HullTriangle3D<?>)o).c;

			if ((lb<la)&&(lb<lc)) {
				int t = lb;
				lb = lc;
				lc = la;
				la = t;
			}
			else if ((lc<la)&&(lc<lb)) {
				int t = la;
				la = lc;
				lc = lb;
				lb = t;
			}

			if ((e<d)&&(e<f)) {
				int t = e;
				e = f;
				f = d;
				d = t;
			}
			else if ((f<d)&&(f<e)) {
				int t = d;
				d = f;
				f = e;
				e = t;
			}
			
			return ((la==d)&&(lb==e)&&(lc==f));			
		}
		return false;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		int v1, v2, v3;
		
		if (this.a<this.b && this.a<this.c) {
			v1 = this.a;
			if (this.b<this.c) {
				v2 = this.b;
				v3 = this.c;
			}
			else {
				v2 = this.c;
				v3 = this.b;
			}
		}
		else if (this.b<this.a && this.b<this.c) {
			v1 = this.b;
			if (this.a<this.c) {
				v2 = this.a;
				v3 = this.c;
			}
			else {
				v2 = this.c;
				v3 = this.a;
			}
		}
		else {
			v1 = this.c;
			if (this.a<this.b) {
				v2 = this.a;
				v3 = this.b;
			}
			else {
				v2 = this.b;
				v3 = this.a;
			}
		}
		
		result = PRIME * result + Float.floatToIntBits(v1);
		result = PRIME * result + Float.floatToIntBits(v2);
		result = PRIME * result + Float.floatToIntBits(v3);
		
		return result;
	}
	
	/** Replies if this triangle has the same points as the specified triangle.
	 * 
	 * @param triangle is another triangle
	 * @return <code>true</code> if this triangle and the given one are equal,
	 * otherwise <code>false</code>
	 */
	public boolean hasSamePoints(HullTriangle3D<T> triangle) {
		int la = this.a;
		int lb = this.b;
		int lc = this.c;
		int d = triangle.a;
		int e = triangle.b;
		int f = triangle.c;
		
		if (la>lb) {
			int t = la;
			la = lb;
			lb = t;
		}
		if (la>lc) {
			int t = la;
			la = lc;
			lc = t;
		}
		if (lb>lc) {
			int t = lb;
			lb = lc;
			lc = t;
		}

		if (d>e) {
			int t = d;
			d = e;
			e = t;
		}
		if (d>f) {
			int t = d;
			d = f;
			f = t;
		}
		if (e>f) {
			int t = e;
			e = f;
			f = t;
		}
		
		return ((la==d)&&(lb==e)&&(lc==f));
	}

	/** Replies if this triangle is co-planar to the specified triangle.
	 * 
	 * @param o is another triangle
	 * @return <code>true</code> if this triangle and the given one are coplanar,
	 * otherwise <code>false</code>
	 */
	public boolean isCoplanar(HullTriangle3D<T> o) {
		Vector3f n1 = getNormal();
		Vector3f n2 = o.getNormal();
		return ((MathUtil.epsilonColinear(n1, n2))&&
			    (MathUtil.epsilonEqualsDistance(this.nw, o.nw)));
	}
	
	/** Replies the plane of the triangle.
	 * 
	 * @return the plane of this triangle.
	 */
	public Plane getPlane() {
		return new Plane4f(this.nx,this.ny,this.nz,this.nw);
	}

	/** {@inheritDoc}
	 */
	@Override
	public int indexesInRange(int min, int max) {
		int mi = min;
		int ma = max;
		if (mi>ma) {
			int t = mi;
			mi = ma;
			ma = t;
		}
		int lc = 0;
		if ((this.a>=mi)&&(this.a<=ma)) ++lc;
		if ((this.b>=mi)&&(this.b<=ma)) ++lc;
		if ((this.c>=mi)&&(this.c<=ma)) ++lc;
		return lc;
	}

}