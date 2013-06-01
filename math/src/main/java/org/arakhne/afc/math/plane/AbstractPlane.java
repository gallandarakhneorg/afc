/* 
 * $Id$
 * 
 * Copyright (C) 2013 Christophe BOHRHAUER.
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
package org.arakhne.afc.math.plane;

import org.arakhne.afc.math.MathUtil;
import org.arakhne.afc.math.continous.object3d.Point3f;
import org.arakhne.afc.math.continous.object3d.Tuple3f;
import org.arakhne.afc.math.continous.object3d.Vector3f;
import org.arakhne.afc.math.object.Line3D;

/** This class represents a 3D plane.
 *
 * @author $Author: cbohrhauer$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public abstract class AbstractPlane implements Plane {

	private static final long serialVersionUID = -227130423311287492L;

	/** Replies a clone of this plane.
	 * 
	 * @return a clone.
	 */
	@Override
	public Plane clone() {
		try {
			return (Plane)super.clone();
		}
		catch (CloneNotSupportedException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @return {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuilder buf = new StringBuilder();
		buf.append('[');
		buf.append(getEquationComponentA());
		buf.append(".x "); //$NON-NLS-1$
		float b = getEquationComponentB(); 
		if (b>=0) buf.append('+');
		buf.append(b);
		buf.append(".y "); //$NON-NLS-1$
		float c = getEquationComponentC(); 
		if (c>=0) buf.append('+');
		buf.append(c);
		buf.append(".z "); //$NON-NLS-1$
		float d = getEquationComponentD(); 
		if (d>=0) buf.append('+');
		buf.append(d);
		buf.append("=0]"); //$NON-NLS-1$
		return buf.toString();
	}

	/** {@inheritDoc}
	 */
	@Override
    public PlanarClassificationType classifies(Tuple3f vec) {
        PlanarClassificationType c;
        float distance = distanceTo(vec);
        int cmp = MathUtil.epsilonDistanceSign(distance);
        if (cmp<0)
            c = PlanarClassificationType.BEHIND;
        else if (cmp>0)
            c = PlanarClassificationType.IN_FRONT_OF;
        else
            c = PlanarClassificationType.COINCIDENT;
        return c;
    }
    
	/** {@inheritDoc}
	 */
	@Override
    public PlanarClassificationType classifies(float x, float y, float z) {
        PlanarClassificationType c;
        float distance = distanceTo(x,y,z);
        int cmp = MathUtil.epsilonDistanceSign(distance);
        if (cmp>0)
            c = PlanarClassificationType.IN_FRONT_OF;
        else if (cmp<0)
            c = PlanarClassificationType.BEHIND;
        else
            c = PlanarClassificationType.COINCIDENT;
        return c;
    }

	/** {@inheritDoc}
	 */
	@Override
    public PlanarClassificationType classifies(float lx, float ly, float lz, float ux, float uy, float uz) {
    	assert(lx<=ux);
    	assert(ly<=uy);
    	assert(lz<=uz);
    	
    	Vector3f normal = getNormal();
    	float minx, miny, minz, maxx, maxy, maxz;

    	// X axis 
    	if(normal.getX()>=0.) { 
    		minx = lx; 
    		maxx = ux; 
    	}
    	else { 
    		minx = ux; 
    		maxx = lx; 
    	} 

    	// Y axis 
    	if(normal.getY()>=0.) { 
    		miny = ly; 
    		maxy = uy; 
    	}
    	else { 
    		miny = uy; 
    		maxy = ly; 
    	}
    	
    	// Z axis 
    	if(normal.getZ()>=0.) { 
    		minz = lz; 
    		maxz = uz; 
    	}
    	else { 
    		minz = uz; 
    		maxz = lz; 
    	} 
    	
    	float d = getEquationComponentD();

    	if ((MathUtil.dotProduct(normal.getX(), normal.getY(), normal.getZ(), minx, miny, minz)+d)>0.)
    		return PlanarClassificationType.IN_FRONT_OF; 
    	
    	if ((MathUtil.dotProduct(normal.getX(), normal.getY(), normal.getZ(), maxx, maxy, maxz)+d)>=0.)
    		return PlanarClassificationType.COINCIDENT;
    	
    	return PlanarClassificationType.BEHIND;
    }

	/** {@inheritDoc}
	 */
	@Override
    public PlanarClassificationType classifies(float x, float y, float z, float radius) {
    	float distance = distanceTo(x,y,z);
        if (!MathUtil.epsilonEqualsDistance((distance<0) ? -distance : distance,radius)) {
	        if (distance<-radius) return PlanarClassificationType.BEHIND;
	        if (distance>radius) return PlanarClassificationType.IN_FRONT_OF;
        }
    	return PlanarClassificationType.COINCIDENT;
    }

	/** {@inheritDoc}
	 */
	@Override
    public PlanarClassificationType classifies(Plane otherPlane) {
    	float distance = distanceTo(otherPlane);
    	
    	// The distance could not be computed
    	// the planes intersect.
    	// Planes intersect also when the distance
    	// is null
    	int cmp = MathUtil.epsilonDistanceSign(distance);
    	
    	if ((distance==Double.NaN)||
    		(cmp==0))
    		return PlanarClassificationType.COINCIDENT;
    	
    	if (cmp>0) return PlanarClassificationType.IN_FRONT_OF;
    	return PlanarClassificationType.BEHIND;
    }

	/** {@inheritDoc}
	 */
	@Override
    public boolean intersects(Tuple3f vec) {
        float distance = distanceTo(vec);
        return MathUtil.epsilonDistanceSign(distance)==0;
    }
    
	/** {@inheritDoc}
	 */
	@Override
    public boolean intersects(float x, float y, float z) {
        float distance = distanceTo(x,y,z);
        return MathUtil.epsilonDistanceSign(distance)==0;
    }

	/** {@inheritDoc}
	 */
	@Override
    public boolean intersects(float lx, float ly, float lz, float ux, float uy, float uz) {
    	return classifies(lx, ly, lz, ux, uy, uz) == PlanarClassificationType.COINCIDENT;
    }

	/** {@inheritDoc}
	 */
	@Override
    public boolean intersects(float x, float y, float z, float radius) {
    	float distance = distanceTo(x,y,z);
        return (MathUtil.epsilonEqualsZero(distance));
    }

	/** {@inheritDoc}
	 */
	@Override
    public boolean intersects(Plane otherPlane) {
    	float distance = distanceTo(otherPlane);
    	if (Double.isNaN(distance)) return true;
    	// The distance could not be computed
    	// the planes intersect.
    	// Planes intersect also when the distance
    	// is null
    	return MathUtil.epsilonDistanceSign(distance)==0;
    }

	/** {@inheritDoc}
	 */
	@Override
    public final float distanceTo(Tuple3f v) {
    	return distanceTo(v.getX(), v.getY(), v.getZ());
    }
    
    /**
     * Compute the distance between two colinear planes.
     * 
	 * @param p is the plane to compute the distance to.
     * @return the distance from the plane to the point along the plane's normal Vec3f.
     * It will be positive if the point is on the side of the plane pointed to by the normal Vec3f, negative otherwise.
     * If the result is 0, the point is on the plane. This function could replies
     * {@link Double#NaN} if the planes are not colinear.
     */
    public float distanceTo(Plane p) {
    	// Compute the normales
    	Vector3f oNormal = p.getNormal();
    	oNormal.normalize();
    	Vector3f mNormal = getNormal();
    	mNormal.normalize();
    	
    	float dotProduct = oNormal.dot(mNormal);
    	
    	if (MathUtil.epsilonEqualsDistance(Math.abs(dotProduct),1)) {
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
	public Line3D getIntersection(Plane plane) {
		Vector3f n1 = getNormal();
		Vector3f n2 = plane.getNormal();
		Vector3f u = new Vector3f();
		
		u.cross(n1, n2);
		float ulength = u.length();
		if (MathUtil.epsilonEqualsZeroDistance(ulength)) {
			// planes are parallel
			return null;
		}

		// u is both perpendicular to the two normals,
		// so it is parallel to both planes
	
		// ((d2 n1 - d1 n2) x (n1 x n2))
		// -----------------------------
		//       | n1 x n2 | ^2
		//
		// same as:
		//
		// ((d2 n1 - d1 n2) x u)
		// ---------------------
		//     ulength ^2
		n1.scale(plane.getEquationComponentD());
		n2.scale(getEquationComponentD());
		n1.sub(n2);
		n2.cross(n1, u);
		n2.scale(1.f/(ulength*ulength)); // n2 contains intersection point
		
		u.normalize();

		return new Line3D(new Point3f(n2), u);
	}

	/** {@inheritDoc}
	 */
	@Override
	public Point3f getIntersection(Line3D line) {
		Vector3f n = getNormal();
		Vector3f u = line.getDirection();
		
		float s = n.dot(u);
		if (MathUtil.epsilonEqualsZeroTrigo(s)) {
			// line and plane are parallel
			return null;
		}
		
		// Assuming L: P0 + si * u
		//
		//      -(a x0 + b y0 + c z0 + d)
		// si = -------------------------
		//               n.u
		Point3f p0 = line.getP0();
		
		float si = -(
				getEquationComponentA()*p0.getX()
				+
				getEquationComponentB()*p0.getY()
				+
				getEquationComponentC()*p0.getZ()
				+
				getEquationComponentD()
				)/s;
		
		u.scale(si);
		p0.add(u);
		
		return p0;
	}
	
 }