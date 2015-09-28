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
package org.arakhne.afc.math.geometry.d3.continuous;

import org.arakhne.afc.math.MathUtil;
import org.arakhne.afc.math.Unefficient;
import org.arakhne.afc.math.geometry.d3.FunctionalVector3D;
import org.arakhne.afc.math.geometry.d3.Point3D;
import org.arakhne.afc.math.geometry.d3.Vector3D;

/** This class represents a 3D plane.
 *
 * @param <PT> is the type of the plane.
 * @author $Author: cbohrhauer$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public abstract class AbstractPlane3D<PT extends AbstractPlane3D<? super PT>> implements Plane3D<PT> {
	
	private static final long serialVersionUID = -8226008101273829655L;

	/** Replies a clone of this plane.
	 * 
	 * @return a clone.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public PT clone() {
		try {
			return (PT)super.clone();
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
		double b = getEquationComponentB(); 
		if (b>=0) buf.append('+');
		buf.append(b);
		buf.append(".y "); //$NON-NLS-1$
		double c = getEquationComponentC(); 
		if (c>=0) buf.append('+');
		buf.append(c);
		buf.append(".z "); //$NON-NLS-1$
		double d = getEquationComponentD(); 
		if (d>=0) buf.append('+');
		buf.append(d);
		buf.append("=0]"); //$NON-NLS-1$
		return buf.toString();
	}

	@Override
	public double distanceTo(Point3D v) {
		return distanceTo(v.getX(), v.getY(), v.getZ());
	}

	@Override
	public double distanceTo(Plane3D<?> p) {
		// Compute the normales
		Vector3D oNormal = p.getNormal();
		oNormal.normalize();
		Vector3D mNormal = getNormal();
		mNormal.normalize();

		double dotProduct = oNormal.dot(mNormal);

		if (MathUtil.isEpsilonEqual(Math.abs(dotProduct),1)) {
			// Planes are colinear.
			// The problem could be restricted to a 1D problem.

			// Compute the coordinate of this pane
			// assuming the origin is (0,0,0)
			double c1 = -distanceTo(0,0,0);

			// Compute the coordinate of the other pane
			// assuming the origin is (0,0,0)
			double c2 = -p.distanceTo(new Point3f(0,0,0));

			if (dotProduct==-1) {
				// The planes have not the same orientation.
				// Reverse one coordinate.
				c2 = -c2;
			}

			return c2 - c1;

		}
		return Double.NaN;
	}

	@Override
	public AbstractSegment3F getIntersection(Plane3D<?> plane) {
		Vector3D n1 = getNormal();
		Vector3D n2 = plane.getNormal();
		Vector3f u = new Vector3f();

		u.cross(n1, n2);
		double ulength = u.length();
		if (MathUtil.isEpsilonZero(ulength)) {
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

		return new Segment3f(new Point3f(n2), u);
	}

	@Override
	public Point3f getIntersection(AbstractSegment3F line) {
		Vector3D n = getNormal();
		Vector3D u = line.getDirection();

		double s = n.dot(u);
		if (MathUtil.isEpsilonZero(s)) {
			// line and plane are parallel
			return null;
		}

		// Assuming L: P0 + si * u
		//
		//      -(a x0 + b y0 + c z0 + d)
		// si = -------------------------
		//               n.u
		Point3f p0 = (Point3f) line.getP1();

		double si = -(
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

	@Override
	public Point3f getProjection(Point3D point) {
		return getProjection(point.getX(), point.getY(), point.getZ());
	}

	@Override
	public boolean intersects(double x, double y, double z) {
		double distance = distanceTo(x,y,z);
        return MathUtil.isEpsilonZero(distance);
	}
	
	@Override
	public boolean intersects(Point3D vec) {
		return intersects(vec.getX(), vec.getY(), vec.getZ());
	}
	
	@Override
	public boolean intersects(Plane3D<?> otherPlane) {
		double distance = distanceTo(otherPlane);
    	if (Double.isNaN(distance)) return true;
    	// The distance could not be computed
    	// the planes intersect.
    	// Planes intersect also when the distance
    	// is null
    	return MathUtil.isEpsilonZero(distance);
	}

	@Override
	public PlaneClassification classifies(Plane3D<?> otherPlane) {
		double distance = distanceTo(otherPlane);

		// The distance could not be computed
		// the planes intersect.
		// Planes intersect also when the distance
		// is null

		if ((distance==Double.NaN)||
			(MathUtil.isEpsilonZero(distance)))
			return PlaneClassification.COINCIDENT;

		if (distance>0) return PlaneClassification.IN_FRONT_OF;
		return PlaneClassification.BEHIND;
	}

	@Override
	public PlaneClassification classifies(Point3D vec) {
		return classifies(vec.getX(), vec.getY(), vec.getZ());
	}

	@Override
	public PlaneClassification classifies(double x, double y, double z) {
		PlaneClassification c;
		double distance = distanceTo(x, y, z);
		if (MathUtil.isEpsilonZero(distance))
			c = PlaneClassification.BEHIND;
		else if (distance>0)
			c = PlaneClassification.IN_FRONT_OF;
		else
			c = PlaneClassification.COINCIDENT;
		return c;
	}

	@Override
	public PlaneClassification classifies(AbstractSphere3F sphere) {
		double distance = distanceTo(sphere.getX(), sphere.getY(), sphere.getZ());
	    if (Math.abs(distance) >= sphere.getRadius()) {
	        if (distance<0) return PlaneClassification.BEHIND;
	        return PlaneClassification.IN_FRONT_OF;
	    }
		return PlaneClassification.COINCIDENT;
	}
	
	@Override
    public PlaneClassification classifies(AlignedBox3f box) {
    	Vector3D normal = getNormal();
    	double minx, miny, minz, maxx, maxy, maxz;

    	// X axis 
    	if(normal.getX()>=0.) { 
    		minx = box.getMinX(); 
    		maxx = box.getMaxX(); 
    	}
    	else { 
    		minx = box.getMaxX(); 
    		maxx = box.getMinX(); 
    	} 

    	// Y axis 
    	if(normal.getY()>=0.) { 
    		miny = box.getMinY(); 
    		maxy = box.getMaxY(); 
    	}
    	else { 
    		miny = box.getMaxY(); 
    		maxy = box.getMinY(); 
    	}
    	
    	// Z axis 
    	if(normal.getZ()>=0.) { 
    		minz = box.getMinZ(); 
    		maxz = box.getMaxZ(); 
    	}
    	else { 
    		minz = box.getMaxZ(); 
    		maxz = box.getMinZ(); 
    	} 
    	
    	double d = getEquationComponentD();

    	if ((FunctionalVector3D.dotProduct(normal.getX(), normal.getY(), normal.getZ(), minx, miny, minz)+d)>0.)
    		return PlaneClassification.IN_FRONT_OF; 
    	
    	if ((FunctionalVector3D.dotProduct(normal.getX(), normal.getY(), normal.getZ(), maxx, maxy, maxz)+d)>=0.)
    		return PlaneClassification.COINCIDENT;
    	
    	return PlaneClassification.BEHIND;
    }

	@Override
    public boolean intersects(AbstractSphere3F sphere) {
    	double distance = distanceTo(sphere.getX(), sphere.getY(), sphere.getZ());
        return MathUtil.isEpsilonZero(distance);
    }
    
	@Override
    @Unefficient
    public boolean intersects(AbstractBoxedShape3F<?> box) {
    	return classifies((AlignedBox3f)box) == PlaneClassification.COINCIDENT;
    }
	

	@Override
	public boolean intersects(AbstractOrientedBox3F box) {
		// Compute the effective radius of the obb and
		// compare it with the distance between the obb center
		// and the plane; source MGPCG pp.235
		Vector3f n = getNormal();

		double dist = Math.abs(distanceTo(box.getCenter()));

		double effectiveRadius;

		effectiveRadius = Math.abs(
				FunctionalVector3D.dotProduct(
						box.getFirstAxisX() * box.getFirstAxisExtent(), 
						box.getFirstAxisY() * box.getFirstAxisExtent(),
						box.getFirstAxisZ() * box.getFirstAxisExtent(),
						n.getX(), n.getY(), n.getZ()));

		effectiveRadius += Math.abs(
				FunctionalVector3D.dotProduct(
						box.getSecondAxisX() * box.getSecondAxisExtent(), 
						box.getSecondAxisY() * box.getSecondAxisExtent(),
						box.getSecondAxisZ() * box.getSecondAxisExtent(),
						n.getX(), n.getY(), n.getZ()));

		effectiveRadius += Math.abs(
				FunctionalVector3D.dotProduct(
						box.getThirdAxisX() * box.getThirdAxisExtent(), 
						box.getThirdAxisY() * box.getThirdAxisExtent(),
						box.getThirdAxisZ() * box.getThirdAxisExtent(),
						n.getX(), n.getY(), n.getZ()));

		return MathUtil.compareEpsilon(dist, effectiveRadius) <= 0;
	}

	@Override
	public boolean intersects(AbstractSegment3F segment) {
		Vector3D n = getNormal();
		Vector3D u = segment.getDirection();

	    double s = n.dot(u);

		if (MathUtil.isEpsilonZero(s)) {
			// The plane and the segment are parallel or coplanar.
			// Test the coplanarity.
			double d = distanceTo(segment.getX1(), segment.getY1(), segment.getZ1());
			return (MathUtil.isEpsilonZero(d));
		}
		
		double si = -(
				getEquationComponentA()*segment.getX1()
				+
				getEquationComponentB()*segment.getY1()
				+
				getEquationComponentC()*segment.getZ1()
				+
				getEquationComponentD()
				) / s;
		
		return (si >= 0. && si <= 1.);
	}

    @Override
	public final void setPivot(Point3D point) {
		setPivot(point.getX(), point.getY(), point.getZ());
	}
    
}