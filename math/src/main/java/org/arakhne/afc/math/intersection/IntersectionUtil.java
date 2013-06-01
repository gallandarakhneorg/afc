/* 
 * $Id$
 * 
 * Copyright (C) 2010-2013 Christophe BOHRHAUER.
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
package org.arakhne.afc.math.intersection;

import org.arakhne.afc.math.MathConstants;
import org.arakhne.afc.math.MathUtil;
import org.arakhne.afc.math.continous.object2d.Point2f;
import org.arakhne.afc.math.continous.object2d.Vector2f;
import org.arakhne.afc.math.continous.object3d.Point3f;
import org.arakhne.afc.math.continous.object3d.Tuple3f;
import org.arakhne.afc.math.continous.object3d.Vector3f;
import org.arakhne.afc.util.OutputParameter;


/**
 * Several intersection functions.
 * <p>
 * Algo inspired from Mathematics for 3D Game Programming and Computer Graphics (MGPCG)
 * and from 3D Game Engine Design (GED)
 * and from Real Time Collision Detection (RTCD).
 * 
 * @author $Author: cbohrhauer$
 * @author $Author: sgalland$
 * @author $Author: ngaud$
 * @author $Author: jdemange$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class IntersectionUtil {

	private static final Vector3f _3D_XBB_AXIS_X = new Vector3f(1,0,0);
	private static final Vector3f _3D_XBB_AXIS_Y = new Vector3f(0,1,0);
	private static final Vector3f _3D_XBB_AXIS_Z = new Vector3f(0,0,1);
	private static final Vector3f[] _3D_XBB_AXIS = new Vector3f[] {
				_3D_XBB_AXIS_X,
				_3D_XBB_AXIS_Y,
				_3D_XBB_AXIS_Z
	};
	private static final Vector2f _2D_XBB_AXIS_X = new Vector2f(1,0);
	private static final Vector2f _2D_XBB_AXIS_Y = new Vector2f(0,1);
	private static final Vector2f[] _2D_XBB_AXIS = new Vector2f[] {
		_2D_XBB_AXIS_X,
		_2D_XBB_AXIS_Y
	};
	
    private IntersectionUtil() {
    	//
    }

    /** Replies if two coplanar triangles intersect.
     * Triangles intersect even if they are connected by two of their
     * edges.
     * <p>
     * Triangle/triangle intersection test routine,
     * by Tomas Moller, 1997.
     * See article "A Fast Triangle-Triangle Intersection Test",
     * Journal of Graphics Tools, 2(2), 1997.
     * 
     * @param v1
     * @param v2
     * @param v3
     * @param u1
     * @param u2
     * @param u3
     * @return <code>true</code> if the two triangles are intersecting.
     * @see #overlapsCoplanarTriangle(Tuple3f, Tuple3f, Tuple3f, Tuple3f, Tuple3f, Tuple3f)
     */
    public static boolean intersectsCoplanarTriangle(Tuple3f<?> v1,Tuple3f<?> v2, Tuple3f<?> v3, Tuple3f<?> u1, Tuple3f<?> u2, Tuple3f<?> u3) {
    	
    	int i0, i1;
    	
    	// first project onto an axis-aligned plane, that maximizes the area
    	// of the triangles, compute indices: i0,i1.
    	{
    		float nx = v1.getY() * (v2.getZ() - v3.getZ()) + v2.getY() * (v3.getZ() - v1.getZ()) + v3.getY() * (v1.getZ() - v2.getZ());
    		float ny = v1.getZ() * (v2.getX() - v3.getX()) + v2.getZ() * (v3.getX() - v1.getX()) + v3.getZ() * (v1.getX() - v2.getX());
    		float nz = v1.getX() * (v2.getY() - v3.getY()) + v2.getX() * (v3.getY() - v1.getY()) + v3.getX() * (v1.getY() - v2.getY());
        	
	    	nx = (nx<0) ? -nx : nx;
	    	ny = (ny<0) ? -ny : ny;
	    	nz = (nz<0) ? -nz : nz;
	    	
	    	if(nx>ny) {
	    		if(nx>nz) {
	    			// nx is greatest
	    			i0 = 1;
	    			i1 = 2;
	    		}
	    		else {
	    			// nz is greatest
	    			i0 = 0;
	    			i1 = 1;
	    		}
	    	}
	    	else {   /* nx<=ny */
	    		if(nz>ny) {
	    			// nz is greatest
	    			i0 = 0;
	    			i1 = 1;                                           
	    		}
	    		else {
	    			// ny is greatest
	    			i0 = 0;
	    			i1 = 2;
	    		}
	    	}
    	}

    	float[] tv1 = new float[] {v1.getX(),v1.getY(),v1.getZ()};
    	float[] tv2 = new float[] {v2.getX(),v2.getY(),v2.getZ()};
    	float[] tv3 = new float[] {v3.getX(),v3.getY(),v3.getZ()};
    	float[] tu1 = new float[] {u1.getX(),u1.getY(),u1.getZ()};
    	float[] tu2 = new float[] {u2.getX(),u2.getY(),u2.getZ()};
    	float[] tu3 = new float[] {u3.getX(),u3.getY(),u3.getZ()};
    	
    	// test all edges of triangle 1 against the edges of triangle 2
    	if (intersectsCoplanarTriangle(i0,i1,0,tv1,tv2,tu1,tu2,tu3)) return true;
    	if (intersectsCoplanarTriangle(i0,i1,0,tv2,tv3,tu1,tu2,tu3)) return true;
    	if (intersectsCoplanarTriangle(i0,i1,0,tv3,tv1,tu1,tu2,tu3)) return true;

    	// finally, test if tri1 is totally contained in tri2 or vice versa
    	if (pointInsideTriangle(i0,i1,tv1,tu1,tu2,tu3)) return true;
    	if (pointInsideTriangle(i0,i1,tu1,tv1,tv2,tv3)) return true;

    	return false;
    }

    /** Replies if two coplanar triangles overlap.
     * Triangles do not overlap if the intersection
     * corresponds to the edges of the triangles.
     * <p>
     * Triangle/triangle intersection test routine,
     * by Tomas Moller, 1997.
     * See article "A Fast Triangle-Triangle Intersection Test",
     * Journal of Graphics Tools, 2(2), 1997.
     * 
     * @param v1
     * @param v2
     * @param v3
     * @param u1
     * @param u2
     * @param u3
     * @return <code>true</code> if the two triangles are overlaping.
     * @see #intersectsCoplanarTriangle(Tuple3f, Tuple3f, Tuple3f, Tuple3f, Tuple3f, Tuple3f)
     */
    public static boolean overlapsCoplanarTriangle(Tuple3f<?> v1,Tuple3f<?> v2, Tuple3f<?> v3, Tuple3f<?> u1, Tuple3f<?> u2, Tuple3f<?> u3) {
    	
    	int i0, i1;
    	
    	// first project onto an axis-aligned plane, that maximizes the area
    	// of the triangles, compute indices: i0,i1.
    	{
    		float nx = v1.getY() * (v2.getZ() - v3.getZ()) + v2.getY() * (v3.getZ() - v1.getZ()) + v3.getY() * (v1.getZ() - v2.getZ());
    		float ny = v1.getZ() * (v2.getX() - v3.getX()) + v2.getZ() * (v3.getX() - v1.getX()) + v3.getZ() * (v1.getX() - v2.getX());
    		float nz = v1.getX() * (v2.getY() - v3.getY()) + v2.getX() * (v3.getY() - v1.getY()) + v3.getX() * (v1.getY() - v2.getY());
        	
	    	nx = (nx<0) ? -nx : nx;
	    	ny = (ny<0) ? -ny : ny;
	    	nz = (nz<0) ? -nz : nz;
	    	
	    	if(nx>ny) {
	    		if(nx>nz) {
	    			// nx is greatest
	    			i0 = 1;
	    			i1 = 2;
	    		}
	    		else {
	    			// nz is greatest
	    			i0 = 0;
	    			i1 = 1;
	    		}
	    	}
	    	else {   /* nx<=ny */
	    		if(nz>ny) {
	    			// nz is greatest
	    			i0 = 0;
	    			i1 = 1;                                           
	    		}
	    		else {
	    			// ny is greatest
	    			i0 = 0;
	    			i1 = 2;
	    		}
	    	}
    	}

    	float[] tv1 = new float[] {v1.getX(),v1.getY(),v1.getZ()};
    	float[] tv2 = new float[] {v2.getX(),v2.getY(),v2.getZ()};
    	float[] tv3 = new float[] {v3.getX(),v3.getY(),v3.getZ()};
    	float[] tu1 = new float[] {u1.getX(),u1.getY(),u1.getZ()};
    	float[] tu2 = new float[] {u2.getX(),u2.getY(),u2.getZ()};
    	float[] tu3 = new float[] {u3.getX(),u3.getY(),u3.getZ()};
    	
    	// test all edges of triangle 1 against the edges of triangle 2
    	int con1 = getSegmentConnection(i0,i1,tv1,tv2,tu1,tu2,tu3);
    	if ((con1!=3)&&
    		(intersectsCoplanarTriangle(i0,i1,con1,tv1,tv2,tu1,tu2,tu3))) return true;
    	
    	int con2 = getSegmentConnection(i0,i1,tv2,tv3,tu1,tu2,tu3);
    	if ((con2!=3)&&
    		(intersectsCoplanarTriangle(i0,i1,con2,tv2,tv3,tu1,tu2,tu3))) return true;

    	int con3 = getSegmentConnection(i0,i1,tv3,tv1,tu1,tu2,tu3);    	
    	if ((con3!=3)&&
    		(intersectsCoplanarTriangle(i0,i1,con3,tv3,tv1,tu1,tu2,tu3))) return true;

    	// finally, test if tri1 is totally contained in tri2 or vice versa
    	if (((con1&1)==0)&&((con3&2)==0)&&
    		(pointInsideTriangle(i0,i1,tv1,tu1,tu2,tu3))) return true;
    	if (((con1&2)==0)&&((con2&1)==0)&&
    	    (pointInsideTriangle(i0,i1,tv2,tu1,tu2,tu3))) return true;
    	if (((con2&2)==0)&&((con3&1)==0)&&
    	    (pointInsideTriangle(i0,i1,tv3,tu1,tu2,tu3))) return true;
    	
    	con1 = getSegmentConnection(i0,i1,tu1,tu2,tv1,tv2,tv3);
    	con2 = getSegmentConnection(i0,i1,tu2,tu3,tv1,tv2,tv3);
    	con3 = getSegmentConnection(i0,i1,tu3,tu1,tv1,tv2,tv3);
    	
    	if (((con1&1)==0)&&((con3&2)==0)&&
    		(pointInsideTriangle(i0,i1,tu1,tv1,tv2,tv3))) return true;
    	if (((con1&2)==0)&&((con2&1)==0)&&
    	    (pointInsideTriangle(i0,i1,tu2,tv1,tv2,tv3))) return true;
    	if (((con2&2)==0)&&((con3&1)==0)&&
    	    (pointInsideTriangle(i0,i1,tu3,tv1,tv2,tv3))) return true;

    	return false;
    }

    /** Replies if a point is inside a triangle.
     */
    private static boolean pointInsideTriangle(int i0, int i1, float[] v, float[] u1, float[] u2, float[] u3) {
    	// is T1 completly inside T2?
    	// check if V0 is inside tri(U0,U1,U2)
    	float a  = u2[i1] - u1[i1];
    	float b  = -(u2[i0] - u1[i0]);
    	float c  = -a * u1[i0] - b * u1[i1];
    	float d0 = a * v[i0] + b * v[i1] + c;

    	a         = u3[i1] - u2[i1];
    	b         = -(u3[i0] - u2[i0]);
    	c         = -a * u2[i0] - b * u2[i1];
    	float d1 = a * v[i0] + b * v[i1] + c;

    	a         = u1[i1] - u2[i1];
    	b         = -(u1[i0] - u3[i0]);
    	c         = -a * u3[i0] - b * u3[i1];
    	float d2 = a * v[i0] + b * v[i1] + c;

    	return ((d0*d1>0.)&&(d0*d2>0.0));
    }

    /** Replies how a segment is connected to a triangle.
     * 
     * @return <code>0</code> when not connected,
     * <code>1</code> when the first segment's vertex was connected,
     * <code>2</code> when the second segment's vertex was connected,
     * <code>3</code> when both segment vertices were connected.
     */
    private static int getSegmentConnection(int i0, int i1, float[] s1, float[] s2, float[] u1, float[] u2, float[] u3) {
    	// Test if the segment is connected to at least one point of the triangle
    	int con = 0;
    	
    	if (((s1[i0]==u1[i0])&&(s1[i1]==u1[i1]))||
    		((s1[i0]==u2[i0])&&(s1[i1]==u2[i1]))||
    		((s1[i0]==u3[i0])&&(s1[i1]==u3[i1]))) con |= 1;
    	
    	if (((s2[i0]==u1[i0])&&(s2[i1]==u1[i1]))||
    		((s2[i0]==u2[i0])&&(s2[i1]==u2[i1]))||
    		((s2[i0]==u3[i0])&&(s2[i1]==u3[i1]))) con |= 2;
    	
    	return con;
    }

    /** Replies if coplanar segment-triangle intersect.
     */
    private static boolean intersectsCoplanarTriangle(int i0, int i1, int con, float[] s1, float[] s2, float[] u1, float[] u2, float[] u3) {
    	float Ax,Ay;

    	Ax = s2[i0] - s1[i0];
    	Ay = s2[i1] - s1[i1];
    	
    	// test edge U0,U1 against V0,V1
    	if (intersectEdgeEdge(i0, i1, con, Ax, Ay, s1, u1, u2)) return true;
    	// test edge U1,U2 against V0,V1
    	if (intersectEdgeEdge(i0, i1, con, Ax, Ay, s1, u2, u3)) return true;
    	// test edge U2,U1 against V0,V1
    	if (intersectEdgeEdge(i0, i1, con, Ax, Ay, s1, u3, u1)) return true;
    	
    	return false;
    }

    /** This edge to edge test is based on Franlin Antonio's gem:
     * "Faster Line Segment Intersection", in Graphics Gems III,
     * pp. 199-202.
     */   
    private static boolean intersectEdgeEdge(int i0, int i1, int con, float Ax, float Ay, float[] v, float[] u1, float[] u2) {
    	// [v,b] is the segment that contains the point v
    	// [c,d] is the segment [u1,u2]
    	
    	// A is the vector (v,b)
    	// B is the vector (d,c)
    	// C is the vector (c,v)
    	
    	float Bx = u1[i0] - u2[i0];
    	float By = u1[i1] - u2[i1];
    	float Cx = v[i0]  - u1[i0];
    	float Cy = v[i1]  - u1[i1];
    	
    	// 
    	
    	float f = Ay * Bx - Ax * By;
    	float d = By * Cx - Bx * Cy; // Line equation: V+d*A
    	
    	boolean up = false;
    	boolean down = false;
    	
		if (f>0) {
			switch(con) {
			case 1: // First point must be ignored
				down = (d>0);
				up = (d<=f);
				break;
			case 2: // Second point must be ignored
				down = (d>=0);
				up = (d<f);
				break;
			case 3: // First and Second points must be ignored
				down = (d>0);
				up = (d<f);
				break;
			default:
				down = (d>=0);
				up = (d<=f);
			}
		}
		else if (f<0) {
			switch(con) {
			case 1: // First point must be ignored
				down = (d>=f);
				up = (d<0);
				break;
			case 2: // Second point must be ignored
				down = (d>f);
				up = (d<=0);
				break;
			case 3: // First and Second points must be ignored
				down = (d>f);
				up = (d<0);
				break;
			default:
				down = (d>=f);
				up = (d<=0);
			}
		}
    	
		if (up&&down) {
    		float e = Ax * Cy - Ay * Cx;
    		if (f>=0) return ((e>=0)&&(e<=f));
			return ((e>=f)&&(e<=0));
		}
    	
    	return false;
    }

    /** Replies if the specified box intersects the specified sphere.
     * <p>
     * A Simple Method for Box-Sphere Intersection Testing by Jim Arvo
     * from "Graphics Gems", Academic Press, 1990
     * <p>
     * This routine tests for intersection between an 3-dimensional             
     * axis-aligned box and an 3-dimensional sphere.  The algorithm type
     * argument indicates whether the objects are to be regarded as
     * surfaces or solids.                                                     
     * 
     * @param sphereCenter are the coordinates of the sphere center.
     * @param radius is the radius of the sphere.
     * @param lower coordinates of the lowest point of the box.
     * @param upper coordinates of the uppest point of the box.
     * @return <code>true</code> if intersecting, otherwise <code>false</code>
     */
    public static boolean intersectsHollowSphereHollowAlignedBox(Point3f sphereCenter, float radius, Point3f lower, Point3f upper) {
    	float r2 = radius*radius;
    	float a, b, dmin, dmax;
    	boolean face;

		dmin = 0;
		dmax = 0;
		
		face = false;

		// X
		a = sphereCenter.getX() - lower.getX();
		a = a*a;

		b = sphereCenter.getX() - upper.getX();
		b = b*b;
		
		dmax += Math.max(a, b);
		
		if( sphereCenter.getX() < lower.getX() ) {
			face = true;
			dmin += a;
		}
		else if( sphereCenter.getX() > upper.getX() ) {
			face = true;
			dmin += b;
		}
		else if( Math.min(a, b) <= r2 ) {
			face = true;
		}
		
		// Y
		a = sphereCenter.getY() - lower.getY();
		a = a*a;

		b = sphereCenter.getY() - upper.getY();
		b = b*b;
		
		dmax += Math.max(a, b);
		
		if( sphereCenter.getY() < lower.getY() ) {
			face = true;
			dmin += a;
		}
		else if( sphereCenter.getY() > upper.getY() ) {
			face = true;
			dmin += b;
		}
		else if( Math.min(a, b) <= r2 ) {
			face = true;
		}
		
		// Z
		a = sphereCenter.getZ() - lower.getZ();
		a = a*a;

		b = sphereCenter.getZ() - upper.getZ();
		b = b*b;
		
		dmax += Math.max(a, b);
		
		if( sphereCenter.getZ() < lower.getZ() ) {
			face = true;
			dmin += a;
		}
		else if( sphereCenter.getZ() > upper.getZ() ) {
			face = true;
			dmin += b;
		}
		else if( Math.min(a, b) <= r2 ) {
			face = true;
		}
		
		return (face && ( dmin <= r2 ) && ( r2 <= dmax));
    }
		
    /** Replies if the specified box intersects the specified sphere.
     * <p>
     * A Simple Method for Box-Sphere Intersection Testing by Jim Arvo
     * from "Graphics Gems", Academic Press, 1990
     * <p>
     * This routine tests for intersection between an 3-dimensional             
     * axis-aligned box and an 3-dimensional sphere.  The algorithm type
     * argument indicates whether the objects are to be regarded as
     * surfaces or solids.                                                     
     * 
     * @param sphereCenter are the coordinates of the sphere center.
     * @param radius is the radius of the sphere.
     * @param lower coordinates of the lowest point of the box.
     * @param upper coordinates of the uppest point of the box.
     * @return <code>true</code> if intersecting, otherwise <code>false</code>
     */
    public static boolean intersectsSolidSphereHollowAlignedBox(Point3f sphereCenter, float radius, Point3f lower, Point3f upper) {
    	float r2 = radius*radius;
    	float a, dmin;
    	boolean face;

		dmin = 0;
		
		face = false;

		// X
		if ( sphereCenter.getX() < lower.getX() ) {
			face = true;
			a = sphereCenter.getX() - lower.getX();
			dmin += a*a;
		}
		else if ( sphereCenter.getX() > upper.getX() ) {
			face = true;
			a = sphereCenter.getX() - upper.getX();
			dmin += a*a;     
		}
		else if ( sphereCenter.getX() - lower.getX() <= radius ) face = true;
		else if ( upper.getX() - sphereCenter.getX() <= radius ) face = true;
		
		// Y
		if ( sphereCenter.getY() < lower.getY() ) {
			face = true;
			a = sphereCenter.getY() - lower.getY();
			dmin += a*a;
		}
		else if ( sphereCenter.getY() > upper.getY() ) {
			face = true;
			a = sphereCenter.getY() - upper.getY();
			dmin += a*a;     
		}
		else if ( sphereCenter.getY() - lower.getY() <= radius ) face = true;
		else if ( upper.getY() - sphereCenter.getY() <= radius ) face = true;
		
		// Z
		if ( sphereCenter.getZ() < lower.getZ() ) {
			face = true;
			a = sphereCenter.getZ() - lower.getZ();
			dmin += a*a;
		}
		else if ( sphereCenter.getZ() > upper.getZ() ) {
			face = true;
			a = sphereCenter.getZ() - upper.getZ();
			dmin += a*a;     
		}
		else if ( sphereCenter.getZ() - lower.getZ() <= radius ) face = true;
		else if ( upper.getZ() - sphereCenter.getZ() <= radius ) face = true;
		
		return ( face && ( dmin <= r2 ) );
    }

    /** Replies if the specified box intersects the specified sphere.
     * <p>
     * A Simple Method for Box-Sphere Intersection Testing by Jim Arvo
     * from "Graphics Gems", Academic Press, 1990
     * <p>
     * This routine tests for intersection between an 3-dimensional             
     * axis-aligned box and an 3-dimensional sphere.  The algorithm type
     * argument indicates whether the objects are to be regarded as
     * surfaces or solids.                                                     
     * 
     * @param sphereCenter are the coordinates of the sphere center.
     * @param radius is the radius of the sphere.
     * @param lower coordinates of the lowest point of the box.
     * @param upper coordinates of the uppest point of the box.
     * @return <code>true</code> if intersecting, otherwise <code>false</code>
     */
    public static boolean intersectsHollowSphereSolidAlignedBox(Point3f sphereCenter, float radius, Point3f lower, Point3f upper) {
    	float r2 = radius*radius;
    	float a, b, dmin, dmax;
    	
    	dmax = 0;
    	dmin = 0;

    	// X
    	a = sphereCenter.getX() - lower.getX();
    	a = a*a;
    	
    	b = sphereCenter.getX() - upper.getX();
    	b = b*b;
    	
    	dmax += Math.max(a, b);
    	if( sphereCenter.getX() < lower.getX() ) dmin += a;
    	else if( sphereCenter.getX() > upper.getX() ) dmin += b;

    	// Y
    	a = sphereCenter.getY() - lower.getY();
    	a = a*a;
    	
    	b = sphereCenter.getY() - upper.getY();
    	b = b*b;
    	
    	dmax += Math.max(a, b);
    	if( sphereCenter.getY() < lower.getY() ) dmin += a;
    	else if( sphereCenter.getY() > upper.getY() ) dmin += b;

    	// Z
    	a = sphereCenter.getZ() - lower.getZ();
    	a = a*a;
    	
    	b = sphereCenter.getZ() - upper.getZ();
    	b = b*b;
    	
    	dmax += Math.max(a, b);
    	if( sphereCenter.getZ() < lower.getZ() ) dmin += a;
    	else if( sphereCenter.getZ() > upper.getZ() ) dmin += b;

    	return ( dmin <= r2 && r2 <= dmax );
    }

    /** Replies if the specified box intersects the specified sphere.
     * <p>
     * A Simple Method for Box-Sphere Intersection Testing by Jim Arvo
     * from "Graphics Gems", Academic Press, 1990
     * <p>
     * This routine tests for intersection between an 3-dimensional             
     * axis-aligned box and an 3-dimensional sphere.  The algorithm type
     * argument indicates whether the objects are to be regarded as
     * surfaces or solids.                                                     
     * 
     * @param sphereCenter are the coordinates of the sphere center.
     * @param radius is the radius of the sphere.
     * @param lower coordinates of the lowest point of the box.
     * @param upper coordinates of the uppest point of the box.
     * @return <code>true</code> if intersecting, otherwise <code>false</code>
     */
    public static boolean intersectsSolidSphereSolidAlignedBox(Point3f sphereCenter, float radius, Point3f lower, Point3f upper) {
    	float r2 = radius*radius;
    	float a, dmin;

    	dmin = 0;
    	
    	// X
		if( sphereCenter.getX() < lower.getX() ) {
			a = sphereCenter.getX() - lower.getX();
			dmin += a*a;
		}
		else if( sphereCenter.getX() > upper.getX() ) {
			a = sphereCenter.getX() - upper.getX();
			dmin += a * a;
		}

    	// Y
		if( sphereCenter.getY() < lower.getY() ) {
			a = sphereCenter.getY() - lower.getY();
			dmin += a*a;
		}
		else if( sphereCenter.getY() > upper.getY() ) {
			a = sphereCenter.getY() - upper.getY();
			dmin += a * a;
		}

    	// Z
		if( sphereCenter.getZ() < lower.getZ() ) {
			a = sphereCenter.getZ() - lower.getZ();
			dmin += a*a;
		}
		else if( sphereCenter.getZ() > upper.getZ() ) {
			a = sphereCenter.getZ() - upper.getZ();
			dmin += a * a;
		}

		return ( dmin <= r2 );
    } 

    /** Replies if the specified rectangle intersects the specified circle.
     * <p>
     * A Simple Method for Box-Sphere Intersection Testing by Jim Arvo
     * from "Graphics Gems", Academic Press, 1990
     * <p>
     * This routine tests for intersection between an 2-dimensional             
     * axis-aligned rectangle and an 2-dimensional circle.  The algorithm type
     * argument indicates whether the objects are to be regarded as
     * surfaces or solids.                                                     
     * 
     * @param circleCenter are the coordinates of the circle center.
     * @param radius is the radius of the circle.
     * @param lower coordinates of the lowest point of the rectangle.
     * @param upper coordinates of the uppest point of the rectangle.
     * @return <code>true</code> if intersecting, otherwise <code>false</code>
     */
    public static boolean intersectsSolidCircleSolidAlignedRectangle(Point2f circleCenter, float radius, Point2f lower, Point2f upper) {
    	float r2 = radius*radius;
    	float a, dmin;

    	dmin = 0;
    	
    	// X
		if( circleCenter.getX() < lower.getX() ) {
			a = circleCenter.getX() - lower.getX();
			dmin += a*a;
		}
		else if( circleCenter.getX() > upper.getX() ) {
			a = circleCenter.getX() - upper.getX();
			dmin += a * a;
		}

    	// Y
		if( circleCenter.getY() < lower.getY() ) {
			a = circleCenter.getY() - lower.getY();
			dmin += a*a;
		}
		else if( circleCenter.getY() > upper.getY() ) {
			a = circleCenter.getY() - upper.getY();
			dmin += a * a;
		}

		return ( dmin < r2 );
    } 

    /**
	 * Classify a sphere against an axis-aligned box.
	 * <p>
	 * This function assumes:
	 * <ul>
	 * <li><code>sphere_radius &gt;= 0</code>/li>
	 * <li><code>box_lx &lt;= box_ux</code>/li>
	 * <li><code>box_ly &lt;= box_uy</code>/li>
	 * <li><code>box_lz &lt;= box_uz</code>/li>
	 * </ul>
	 *
     * @param sphereCenter are the coordinates of the sphere center.
     * @param radius is the radius of the sphere.
     * @param lower coordinates of the lowest point of the box.
     * @param upper coordinates of the uppest point of the box.
	 * @return the value {@link IntersectionType#INSIDE} if the <var>sphere</var> is inside the <var>box</var>;
	 * {@link IntersectionType#OUTSIDE} if the <var>sphere</var> is outside the <var>box</var>;
	 * {@link IntersectionType#ENCLOSING} if the <var>sphere</var> is enclosing the <var>box</var>;
	 * {@link IntersectionType#SPANNING} otherwise.
	 */
	public static IntersectionType classifiesSolidSphereSolidAlignedBox(
			Point3f sphereCenter, float radius,
			Point3f lower, Point3f upper) {
		// Assumptions
		assert(radius >= 0.);
		assert(lower.getX() <= upper.getX());
		assert(lower.getY() <= upper.getY());
		assert(lower.getZ() <= upper.getZ());
		
    	// Compute the distance betwen the sphere center and
    	// the closest point of the box

    	// Compute the distance between the sphere center and
    	// the farest point of the box

		float dmin; // distance between the sphere center and the nearest point of the box. 
		float dmax; // distance between the sphere center and the farest point of the box.
		float a,b; // tmp value
		
		boolean sphereInsideOnAllAxis = false;
		
		dmin = dmax = 0.f;
		
		if (sphereCenter.getX()<lower.getX()) {
			a = lower.getX() - sphereCenter.getX();
			dmin = a*a;
			a = upper.getX() - sphereCenter.getX();
			dmax = a*a;
		}
		else if (sphereCenter.getX()>upper.getX()) {
			a = sphereCenter.getX() - upper.getX();
			dmin = a*a;
			a = sphereCenter.getX() - lower.getX();
			dmax = a*a;
		}
		else {
			a = sphereCenter.getX()-lower.getX();
			b = upper.getX()-sphereCenter.getX();
			if (a>=b) {
				sphereInsideOnAllAxis = (radius<b);
			}
			else {
				sphereInsideOnAllAxis = (radius<a);
				a = b;
			}
			dmax = a*a;
		}
		
		if (sphereCenter.getY()<lower.getY()) {
			a = lower.getY() - sphereCenter.getY();
			dmin += a*a;
			a = upper.getY() - sphereCenter.getY();
			dmax += a*a;
		}
		else if (sphereCenter.getY()>upper.getY()) {
			a = sphereCenter.getY() - upper.getY();
			dmin += a*a;
			a = sphereCenter.getY() - lower.getY();
			dmax += a*a;
		}
		else {
			a = sphereCenter.getY()-lower.getY();
			b = upper.getY()-sphereCenter.getY();
			if (a>=b) {
				sphereInsideOnAllAxis &= (radius<b);
			}
			else {
				sphereInsideOnAllAxis &= (radius<a);
				a = b;
			}
			dmax += a*a;
		}
		
		if (sphereCenter.getZ()<lower.getZ()) {
			a = lower.getZ() - sphereCenter.getZ();
			dmin += a*a;
			a = upper.getZ() - sphereCenter.getZ();
			dmax += a*a;
		}
		else if (sphereCenter.getZ()>upper.getZ()) {
			a = sphereCenter.getZ() - upper.getZ();
			dmin += a*a;
			a = sphereCenter.getZ() - lower.getZ();
			dmax += a*a;
		}
		else {
			a = sphereCenter.getZ()-lower.getZ();
			b = upper.getZ()-sphereCenter.getZ();
			if (a>=b) {
				sphereInsideOnAllAxis &= (radius<b);
			}
			else {
				sphereInsideOnAllAxis &= (radius<a);
				a = b;
			}
			dmax += a*a;
		}

		float sr = radius * radius;
		
		if (dmin==0.) {
			// Sphere center is inside the box.
			if (sphereInsideOnAllAxis) return IntersectionType.INSIDE;
			if (sr>dmax) return IntersectionType.ENCLOSING;
		}
		else {
			// Sphere center is outside the box.
			if (sr<=dmin) return IntersectionType.OUTSIDE;
			if (sr>dmax) return IntersectionType.ENCLOSING;
		}
		return IntersectionType.SPANNING;
	}
	
	/**
	 * Classify a circle against an minimum bounding rectangle.
	 * <p>
	 * This function assumes:
	 * <ul>
	 * <li><code>sphere_radius &gt;= 0</code>/li>
	 * <li><code>box_lx &lt;= box_ux</code>/li>
	 * <li><code>box_ly &lt;= box_uy</code>/li>
	 * </ul>
	 *
     * @param circleCenter are the coordinates of the circle center.
     * @param radius is the radius of the circle.
     * @param lower coordinates of the lowest point of the rectangle.
     * @param upper coordinates of the uppest point of the box.
	 * @return the value {@link IntersectionType#INSIDE} if the <var>circle</var> is inside the rectangle;
	 * {@link IntersectionType#OUTSIDE} if the <var>circle</var> is outside the rectangle;
	 * {@link IntersectionType#ENCLOSING} if the <var>circle</var> is enclosing the rectangle;
	 * {@link IntersectionType#SPANNING} otherwise.
	 */
	public static IntersectionType classifiesSolidCircleSolidAlignedRectangle(
			Point2f circleCenter, float radius,
			Point2f lower, Point2f upper) {
		// Assumptions
		assert(radius >= 0.);
		assert(lower.getX() <= upper.getX());
		assert(lower.getY() <= upper.getY());
		
    	// Compute the distance betwen the sphere center and
    	// the closest point of the box

    	// Compute the distance between the sphere center and
    	// the farest point of the box

		float dmin; // distance between the sphere center and the nearest point of the box. 
		float dmax; // distance between the sphere center and the farest point of the box.
		float a,b; // tmp value
		
		boolean sphereInsideOnAllAxis = false;
		
		dmin = dmax = 0.f;
		
		if (circleCenter.getX()<lower.getX()) {
			a = lower.getX() - circleCenter.getX();
			dmin = a*a;
			a = upper.getX() - circleCenter.getX();
			dmax = a*a;
		}
		else if (circleCenter.getX()>upper.getX()) {
			a = circleCenter.getX() - upper.getX();
			dmin = a*a;
			a = circleCenter.getX() - lower.getX();
			dmax = a*a;
		}
		else {
			a = circleCenter.getX()-lower.getX();
			b = upper.getX()-circleCenter.getX();
			if (a>=b) {
				sphereInsideOnAllAxis = (radius<b);
			}
			else {
				sphereInsideOnAllAxis = (radius<a);
				a = b;
			}
			dmax = a*a;
		}
		
		if (circleCenter.getY()<lower.getY()) {
			a = lower.getY() - circleCenter.getY();
			dmin += a*a;
			a = upper.getY() - circleCenter.getY();
			dmax += a*a;
		}
		else if (circleCenter.getY()>upper.getY()) {
			a = circleCenter.getY() - upper.getY();
			dmin += a*a;
			a = circleCenter.getY() - lower.getY();
			dmax += a*a;
		}
		else {
			a = circleCenter.getY()-lower.getY();
			b = upper.getY()-circleCenter.getY();
			if (a>=b) {
				sphereInsideOnAllAxis &= (radius<b);
			}
			else {
				sphereInsideOnAllAxis &= (radius<a);
				a = b;
			}
			dmax += a*a;
		}
		
		float sr = radius * radius;
		
		if (dmin==0.) {
			// Sphere center is inside the box.
			if (sphereInsideOnAllAxis) return IntersectionType.INSIDE;
			if (sr>dmax) return IntersectionType.ENCLOSING;
		}
		else {
			// Sphere center is outside the box.
			if (sr<=dmin) return IntersectionType.OUTSIDE;
			if (sr>dmax) return IntersectionType.ENCLOSING;
		}
		return IntersectionType.SPANNING;
	}

    /**
     * Tests if the line segment from {@code (x1,y1)} to 
     * {@code (x2,y2)} intersects the line segment
     * from {@code (x3,y3)} to {@code (x4,y4)}.
     *
     * @param x1 the X coordinate of the start point of the
     *           first specified line segment
     * @param y1 the Y coordinate of the start point of the
     *           first specified line segment
     * @param x2 the X coordinate of the end point of the
     *           specified line segment
     * @param y2 the Y coordinate of the end point of the
     *           first specified line segment
     * @param x3 the X coordinate of the start point of the
     *           first specified line segment
     * @param y3 the Y coordinate of the start point of the
     *           first specified line segment
     * @param x4 the X coordinate of the end point of the
     *           specified line segment
     * @param y4 the Y coordinate of the end point of the
     *           first specified line segment
     * @return <code>true</code> if this line segments intersect each 
     * other; <code>false</code> otherwise.
     * @since 3.0
     */
    public static boolean intersectsSegments(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4) {
    	return ((MathUtil.relativeSegmentCCW(x1, y1, x2, y2, x3, y3, false) *
    			MathUtil.relativeSegmentCCW(x1, y1, x2, y2, x4, y4, false) <= 0)
    			&& (MathUtil.relativeSegmentCCW(x3, y3, x4, y4, x1, y1, false) *
    					MathUtil.relativeSegmentCCW(x3, y3, x4, y4, x2, y2, false) <= 0));
    }

    /**
     * Tests if the line from {@code (x1,y1)} to 
     * {@code (x2,y2)} intersects the line segment
     * from {@code (x3,y3)} to {@code (x4,y4)}.
     *
     * @param x1 the X coordinate of the start point of the line
     * @param y1 the Y coordinate of the start point of the line
     * @param x2 the X coordinate of the end point of the line
     * @param y2 the Y coordinate of the end point of the line
     * @param x3 the X coordinate of the start point of the line segment
     * @param y3 the Y coordinate of the start point of the line segment
     * @param x4 the X coordinate of the end point of the line segment
     * @param y4 the Y coordinate of the end point of the line segment
     * @return <code>true</code> if this line intersects the line segment,
     * <code>false</code> otherwise.
     * @since 3.0
     */
    public static boolean intersectsLineSegment(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4) {
    	return (MathUtil.relativeSegmentCCW(x1, y1, x2, y2, x3, y3, false) *
    			MathUtil.relativeSegmentCCW(x1, y1, x2, y2, x4, y4, false) <= 0);
    }

    /**
     * Tests if the line from {@code (x1,y1)} to 
     * {@code (x2,y2)} intersects the line
     * from {@code (x3,y3)} to {@code (x4,y4)}.
     * <p>
     * If lines are colinear, this function replied <code>false</code>.
     *
     * @param x1 the X coordinate of the start point of the first line
     * @param y1 the Y coordinate of the start point of the first line
     * @param x2 the X coordinate of the end point of the first line
     * @param y2 the Y coordinate of the end point of the first line
     * @param x3 the X coordinate of the start point of the second line
     * @param y3 the Y coordinate of the start point of the second line
     * @param x4 the X coordinate of the end point of the second line
     * @param y4 the Y coordinate of the end point of the second line
     * @return <code>true</code> if this line segments intersect each 
     * other; <code>false</code> otherwise.
     * @since 3.0
     */
    public static boolean intersectsLines(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4) {
    	if (MathUtil.isParallelLines(x1, y1, x2, y2, x3, y3, x4, y4)) {
    		return MathUtil.isCollinearPoints(x1, y1, x2, y2, x3, y3);
    	}
    	return true;
    }

    /**
     * Tests if the point {@code (px,py)} 
     * lies inside a 2D triangle
     * given by {@code (x1,y1)}, {@code (x2,y2)}
     * and {@code (x3,y3)} points.
     * <p>
     * <strong>Caution: Tests are "epsiloned."</strong>
     * <p>
     * <strong>Trigonometric Method (Slowest)</strong>
     * <p>
     * A common way to check if a point is in a triangle is to 
     * find the vectors connecting the point to each of the 
     * triangle's three vertices and sum the angles between 
     * those vectors. If the sum of the angles is 2*pi 
     * then the point is inside the triangle, otherwise it 
     * is not. <em>It works, but it is very slow.</em>
     * <p>
     * <center><img src="doc-files/point_segment.gif" alt="Point-Segment Intersection Picture 1">
     * <img src="doc-files/point_segment_2.jpg" alt="Point-Segment Intersection Picture 2"></center>
     * <p>
     * The advantage of the method above is that it's very simple to understand so that once 
     * you read it you should be able to remember it forever and code it up at 
     * any time without having to refer back to anything.
     * <p>
     * There's another method that is also as easy conceptually but executes faster.
     * The downside is there's a little more math involved, but once you see 
     * it worked out it should be no problem.
     * <p>
     * <strong>Barycenric Method (Fastest)</strong>
     * <p>
     * So remember that the three points of the triangle define a plane in space. 
     * Pick one of the points and we can consider all other locations on the plane 
     * as relative to that point. Let's select A -- it'll be our origin on the 
     * plane. Now what we need are basis vectors so we can give coordinate 
     * values to all the locations on the plane. 
     * We'll pick the two edges of the triangle that touch A, 
     * (C - A) and (B - A). 
     * Now we can get to any point on the plane just by starting at A 
     * and walking some distance along (C - A) and then from there walking 
     * some more in the direction (B - A).
     * <p>
     * <center><img src="doc-files/point_segment_3.png" alt="Point-Segment Intersection Picture 3"></center>
     * <p>
     * With that in mind we can now describe any point on the plane as:<br>
     * P = A + u * (C - A) + v * (B - A)
     * <p>
     * Notice now that if u or v < 0 then we've walked in the wrong direction 
     * and must be outside the triangle. Also if u or v > 1 then we've 
     * walked too far in a direction and are outside the triangle. 
     * Finally if u + v > 1 then we've crossed the edge BC again leaving the triangle.
     * <p>
     * Given u and v we can easily calculate the point P with the above 
     * equation, but how can we go in the reverse direction and calculate 
     * u and v from a given point P?<br>
     * P = A + u * (C - A) + v * (B - A)       // Original equation<br>
     * (P - A) = u * (C - A) + v * (B - A)     // Subtract A from both sides<br>
     * v2 = u * v0 + v * v1                    // Substitute v0, v1, v2 for less writing
     * <p>
     * We have two unknowns (u and v) so we need two equations to solve
     * for them.  Dot both sides by v0 to get one and dot both sides by
     * v1 to get a second.<br>
     * (v2) . v0 = (u * v0 + v * v1) . v0<br>
     * (v2) . v1 = (u * v0 + v * v1) . v1<br>
     * <p>
     * Distribute v0 and v1<br>
     * v2 . v0 = u * (v0 . v0) + v * (v1 . v0)<br>
     * v2 . v1 = u * (v0 . v1) + v * (v1 . v1)
     * <p>
     * Now we have two equations and two unknowns and can solve one
     * equation for one variable and substitute into the other.  Or
     * fire up GNU Octave and save some handwriting.<br>
     * Solve[v2.v0 == {u(v0.v0) + v(v1.v0), v2.v1 == u(v0.v1) + v(v1.v1)}, {u, v}]<br>
     * u = ((v1.v1)(v2.v0)-(v1.v0)(v2.v1)) / ((v0.v0)(v1.v1) - (v0.v1)(v1.v0))<br>
     * v = ((v0.v0)(v2.v1)-(v0.v1)(v2.v0)) / ((v0.v0)(v1.v1) - (v0.v1)(v1.v0))
     *
     * @param px the X coordinate of the point
     * @param py the Y coordinate of the point
     * @param ax the X coordinate of the first point of the triangle
     * @param ay the Y coordinate of the first point of the triangle
     * @param bx the X coordinate of the second point of the triangle
     * @param by the Y coordinate of the second point of the triangle
     * @param cx the X coordinate of the third point of the triangle
     * @param cy the Y coordinate of the third point of the triangle
     * @return <code>true</code> if the points is coplanar to the triangle and
     * lies inside it, otherwise <code>false</code>
     * @since 3.0
     */
    public static boolean intersectsPointTriangle(
    		float px, float py, 
    		float ax, float ay,
    		float bx, float by,
    		float cx, float cy) {
    	/* The comment code is the trivial trigonometric implementation:
    	float vx1 = x1 - px;
    	float vy1 = y1 - py;
    	float vx2 = x2 - px;
    	float vy2 = y2 - py;
    	float vx3 = x3 - px;
    	float vy3 = y3 - py;
    	
    	float angle;
    	
    	angle = Math.acos(MathUtil.dotProduct(vx1, vy1, vx2, vy2));
    	angle += Math.acos(MathUtil.dotProduct(vx2, vy2, vx3, vy3));
    	angle += Math.acos(MathUtil.dotProduct(vx3, vy3, vx1, vy1));
    	
    	return MathUtil.epsilonEqualsRadian(angle, MathConstants.TWO_PI);*/
    	
    	//
    	// Compute vectors        
    	//
    	// v0 = C - A
    	float v0x = cx - ax;
    	float v0y = cy - ay;
    	// v1 = B - A
    	float v1x = bx - ax;
    	float v1y = by - ay;
    	// v2 = P - A
    	float v2x = px - ax;
    	float v2y = py - ay;

    	//
    	// Compute dot products
    	//
    	// dot01 = dot(v0, v0)
    	float dot00 = MathUtil.dotProduct(v0x, v0y, v0x, v0y);
    	// dot01 = dot(v0, v1)
    	float dot01 = MathUtil.dotProduct(v0x, v0y, v1x, v1y);
    	// dot02 = dot(v0, v2)
    	float dot02 = MathUtil.dotProduct(v0x, v0y, v2x, v2y);
    	// dot11 = dot(v1, v1)
    	float dot11 = MathUtil.dotProduct(v1x, v1y, v1x, v1y);
    	// dot12 = dot(v1, v2)
    	float dot12 = MathUtil.dotProduct(v1x, v1y, v2x, v2y);

    	//
    	// Compute barycentric coordinates
    	//
    	float invDenom = 1.f / (dot00 * dot11 - dot01 * dot01);
    	float u = (dot11 * dot02 - dot01 * dot12) * invDenom;
    	float v = (dot00 * dot12 - dot01 * dot02) * invDenom;

    	// Check if point is in triangle
    	return (MathUtil.epsilonCompareTo(u, 0.f) >= 0)
    			&& (MathUtil.epsilonCompareTo(v, 0.f) >= 0) 
    		    && (MathUtil.epsilonCompareTo(u + v, 1.f) <= 0);
    }

    /**
     * Tests if the point {@code (px,py,pz)} 
     * lies inside a 3D triangle
     * given by {@code (x1,y1,z1)}, {@code (x2,y2,z2)}
     * and {@code (x3,y3,z3)} points.
     * <p>
     * <strong>Caution: Tests are "epsiloned."</strong>
     * <p>
     * Parameter <var>forceCoplanar</var> has a deep influence on the function
     * result. It indicates if coplanarity test must be done or not.
     * Following table explains this influence:
     * <table>
     * <thead>
     * <tr>
     * <th>Point is coplanar?</th>
     * <th>Point projection on plane is inside triangle?</th>
     * <th><var>forceCoplanar</var></th>
     * <th><code>intersectsPointTrangle()</code> Result</th>
     * </tr>
     * </thead>
     * <tr>
     * <td><code>true</code></td>
     * <td><code>true</code></td>
     * <td><code>true</code></td>
     * <td><code>true</code></td>
     * </tr>
     * <tr>
     * <td><code>true</code></td>
     * <td><code>true</code></td>
     * <td><code>false</code></td>
     * <td><code>true</code></td>
     * </tr>
     * <tr>
     * <td><code>true</code></td>
     * <td><code>false</code></td>
     * <td><code>true</code></td>
     * <td><code>false</code></td>
     * </tr>
     * <tr>
     * <td><code>true</code></td>
     * <td><code>false</code></td>
     * <td><code>false</code></td>
     * <td><code>false</code></td>
     * </tr>
     * <tr>
     * <td><code>false</code></td>
     * <td><code>true</code></td>
     * <td><code>true</code></td>
     * <td><code>false</code></td>
     * </tr>
     * <tr>
     * <td><code>false</code></td>
     * <td><code>true</code></td>
     * <td><code>false</code></td>
     * <td><code>true</code></td>
     * </tr>
     * <tr>
     * <td><code>false</code></td>
     * <td><code>false</code></td>
     * <td><code>true</code></td>
     * <td><code>false</code></td>
     * </tr>
     * <tr>
     * <td><code>false</code></td>
     * <td><code>false</code></td>
     * <td><code>false</code></td>
     * <td><code>false</code></td>
     * </tr>
     * </table>
     * <p>
     * <strong>Trigonometric Method (Slowest)</strong>
     * <p>
     * A common way to check if a point is in a triangle is to 
     * find the vectors connecting the point to each of the 
     * triangle's three vertices and sum the angles between 
     * those vectors. If the sum of the angles is 2*pi 
     * then the point is inside the triangle, otherwise it 
     * is not. <em>It works, but it is very slow.</em>
     * <p>
     * <center><img src="doc-files/point_segment.gif" alt="Point-Segment Intersection Picture 1">
     * <img src="doc-files/point_segment_2.jpg" alt="Point-Segment Intersection Picture 2"></center>
     * <p>
     * The advantage of the method above is that it's very simple to understand so that once 
     * you read it you should be able to remember it forever and code it up at 
     * any time without having to refer back to anything.
     * <p>
     * <strong>Barycenric Method (Fastest)</strong>
     * <p>
     * There's another method that is also as easy conceptually but executes faster.
     * The downside is there's a little more math involved, but once you see 
     * it worked out it should be no problem.
     * <p>
     * So remember that the three points of the triangle define a plane in space. 
     * Pick one of the points and we can consider all other locations on the plane 
     * as relative to that point. Let's select A -- it'll be our origin on the 
     * plane. Now what we need are basis vectors so we can give coordinate 
     * values to all the locations on the plane. 
     * We'll pick the two edges of the triangle that touch A, 
     * (C - A) and (B - A). 
     * Now we can get to any point on the plane just by starting at A 
     * and walking some distance along (C - A) and then from there walking 
     * some more in the direction (B - A).
     * <p>
     * <center><img src="doc-files/point_segment_3.png" alt="Point-Segment Intersection Picture 3"></center>
     * <p>
     * With that in mind we can now describe any point on the plane as:<br>
     * P = A + u * (C - A) + v * (B - A)
     * <p>
     * Notice now that if u or v < 0 then we've walked in the wrong direction 
     * and must be outside the triangle. Also if u or v > 1 then we've 
     * walked too far in a direction and are outside the triangle. 
     * Finally if u + v > 1 then we've crossed the edge BC again leaving the triangle.
     * <p>
     * Given u and v we can easily calculate the point P with the above 
     * equation, but how can we go in the reverse direction and calculate 
     * u and v from a given point P?<br>
     * P = A + u * (C - A) + v * (B - A)       // Original equation<br>
     * (P - A) = u * (C - A) + v * (B - A)     // Subtract A from both sides<br>
     * v2 = u * v0 + v * v1                    // Substitute v0, v1, v2 for less writing
     * <p>
     * We have two unknowns (u and v) so we need two equations to solve
     * for them.  Dot both sides by v0 to get one and dot both sides by
     * v1 to get a second.<br>
     * (v2) . v0 = (u * v0 + v * v1) . v0<br>
     * (v2) . v1 = (u * v0 + v * v1) . v1<br>
     * <p>
     * Distribute v0 and v1<br>
     * v2 . v0 = u * (v0 . v0) + v * (v1 . v0)<br>
     * v2 . v1 = u * (v0 . v1) + v * (v1 . v1)
     * <p>
     * Now we have two equations and two unknowns and can solve one
     * equation for one variable and substitute into the other.  Or
     * fire up GNU Octave and save some handwriting.<br>
     * Solve[v2.v0 == {u(v0.v0) + v(v1.v0), v2.v1 == u(v0.v1) + v(v1.v1)}, {u, v}]<br>
     * u = ((v1.v1)(v2.v0)-(v1.v0)(v2.v1)) / ((v0.v0)(v1.v1) - (v0.v1)(v1.v0))<br>
     * v = ((v0.v0)(v2.v1)-(v0.v1)(v2.v0)) / ((v0.v0)(v1.v1) - (v0.v1)(v1.v0))
     *
     * @param px the X coordinate of the point
     * @param py the Y coordinate of the point
     * @param pz the Z coordinate of the point
     * @param ax the X coordinate of the first point of the triangle
     * @param ay the Y coordinate of the first point of the triangle
     * @param az the Z coordinate of the first point of the triangle
     * @param bx the X coordinate of the second point of the triangle
     * @param by the Y coordinate of the second point of the triangle
     * @param bz the Z coordinate of the second point of the triangle
     * @param cx the X coordinate of the third point of the triangle
     * @param cy the Y coordinate of the third point of the triangle
     * @param cz the Z coordinate of the third point of the triangle
     * @param forceCoplanar is <code>true</code> to force to test
     * if the given point is coplanar to the triangle, <code>false</code>
     * to not consider coplanarity of the point.
     * @return <code>true</code> if the points is coplanar - or not,
     * depending on <var>forceCoplanar</var> - to the triangle and
     * lies inside it, otherwise <code>false</code>
     * @since 3.0
     */
    public static boolean intersectsPointTriangle(
    		float px, float py, float pz,
    		float ax, float ay, float az,
    		float bx, float by, float bz,
    		float cx, float cy, float cz,
    		boolean forceCoplanar) {

    	//
    	// Compute vectors        
    	//
    	// v0 = C - A
    	float v0x = cx - ax;
    	float v0y = cy - ay;
    	float v0z = cz - az;
    	// v1 = B - A
    	float v1x = bx - ax;
    	float v1y = by - ay;
    	float v1z = bz - az;
    	// v2 = P - A
    	float v2x = px - ax;
    	float v2y = py - ay;
    	float v2z = pz - az;

    	//
    	// Compute dot products
    	//
    	// dot01 = dot(v0, v0)
    	float dot00 = MathUtil.dotProduct(v0x, v0y, v0z, v0x, v0y, v0z);
    	// dot01 = dot(v0, v1)
    	float dot01 = MathUtil.dotProduct(v0x, v0y, v0z, v1x, v1y, v1z);
    	// dot02 = dot(v0, v2)
    	float dot02 = MathUtil.dotProduct(v0x, v0y, v0z, v2x, v2y, v2z);
    	// dot11 = dot(v1, v1)
    	float dot11 = MathUtil.dotProduct(v1x, v1y, v1z, v1x, v1y, v1z);
    	// dot12 = dot(v1, v2)
    	float dot12 = MathUtil.dotProduct(v1x, v1y, v1z, v2x, v2y, v2z);

    	//
    	// Compute barycentric coordinates
    	//
    	float invDenom = 1.f / (dot00 * dot11 - dot01 * dot01);
    	float u = (dot11 * dot02 - dot01 * dot12) * invDenom;
    	float v = (dot00 * dot12 - dot01 * dot02) * invDenom;

    	// Check if point is in triangle
    	if ((MathUtil.epsilonCompareTo(u, 0.f) >= 0)
			&& (MathUtil.epsilonCompareTo(v, 0.f) >= 0) 
		    && (MathUtil.epsilonCompareTo(u + v, 1.f) <= 0)) {
    		if (forceCoplanar) {
    			// Triangle's plane equation:
    			// nx = ay * (bz - cz) + by * (cz - az) + cy * (az - bz)
    			// ny = az * (bx - cx) + bz * (cx - ax) + cz * (ax - bx)
    			// nz = ax * (by - cy) + bx * (cy - ay) + cx * (ay - by)
    			// d = - (nx * ax + ny * ay + nz * az)
    			
    	    	// Result dot* variables to prevent memory allocation
    			dot00 = ay * (bz - cz) + by * v0z - cy * v1z;
    			dot01 = az * (bx - cx) + bz * v0x - cz * v1x;
    			dot02 = ax * (by - cy) + bx * v0y - cx * v1y;
    			dot11 = - (dot00 * ax + dot01 * ay + dot02 * az);
    			dot12 = dot00 * px + dot01 * py + dot02 * pz + dot11;
    			
    			return MathUtil.epsilonEqualsZero(dot12);
    		}
    		return true;
    	}
    	return false;
    }

    /**
     * Tests if the point {@code (px,py,pz)} 
     * lies inside a 3D segment
     * given by {@code (x1,y1,z1)} and {@code (x2,y2,z2)}
     * points.
     * <p>
     * This function projects the point on the 3D line and tests if the projection
     * is lying on the segment. To force the point to be on the segment, see below.
     * <p>
     * Parameter <var>forceCollinear</var> has a deep influence on the function
     * result. It indicates if collinear test must be done or not.
     * Following table explains this influence:
     * <table>
     * <thead>
     * <tr>
     * <th>Point is collinear?</th>
     * <th>Point projection on line is inside segment?</th>
     * <th><var>forceCollinear</var></th>
     * <th><code>intersectsPointSegment()</code> Result</th>
     * </tr>
     * </thead>
     * <tr>
     * <td><code>true</code></td>
     * <td><code>true</code></td>
     * <td><code>true</code></td>
     * <td><code>true</code></td>
     * </tr>
     * <tr>
     * <td><code>true</code></td>
     * <td><code>true</code></td>
     * <td><code>false</code></td>
     * <td><code>true</code></td>
     * </tr>
     * <tr>
     * <td><code>true</code></td>
     * <td><code>false</code></td>
     * <td><code>true</code></td>
     * <td><code>false</code></td>
     * </tr>
     * <tr>
     * <td><code>true</code></td>
     * <td><code>false</code></td>
     * <td><code>false</code></td>
     * <td><code>false</code></td>
     * </tr>
     * <tr>
     * <td><code>false</code></td>
     * <td><code>true</code></td>
     * <td><code>true</code></td>
     * <td><code>false</code></td>
     * </tr>
     * <tr>
     * <td><code>false</code></td>
     * <td><code>true</code></td>
     * <td><code>false</code></td>
     * <td><code>true</code></td>
     * </tr>
     * <tr>
     * <td><code>false</code></td>
     * <td><code>false</code></td>
     * <td><code>true</code></td>
     * <td><code>false</code></td>
     * </tr>
     * <tr>
     * <td><code>false</code></td>
     * <td><code>false</code></td>
     * <td><code>false</code></td>
     * <td><code>false</code></td>
     * </tr>
     * </table>
     *
     * @param px the X coordinate of the point
     * @param py the Y coordinate of the point
     * @param pz the Z coordinate of the point
     * @param ax the X coordinate of the first point of the segment
     * @param ay the Y coordinate of the first point of the segment
     * @param az the Z coordinate of the first point of the segment
     * @param bx the X coordinate of the second point of the segment
     * @param by the Y coordinate of the second point of the segment
     * @param bz the Z coordinate of the second point of the segment
     * @param forceCollinear is <code>true</code> to force to test
     * if the given point is collinear to the segment, <code>false</code>
     * to not consider collinearity of the point.
     * @return <code>true</code> if the points is on segment,
     * otherwise <code>false</code>
     * @since 3.0
     */
    public static boolean intersectsPointSegment(
    		float px, float py, float pz,
    		float ax, float ay, float az,
    		float bx, float by, float bz,
    		boolean forceCollinear) {
    	float ratio = MathUtil.projectsPointOnSegment(px, py, pz, ax, ay, az, bx, by, bz);
		
		if (ratio>=0. && ratio<=1.) {
    		if (forceCollinear) {
    			return MathUtil.isCollinearPoints(
    					ax, ay, az,
    		    		bx, by, bz,
    					px, py, pz);
    		}
    		return true;
		}
		
		return false;
    }

    /**
     * Tests if the point {@code (px,py)} 
     * appromativaly lies inside a 2D segment
     * given by {@code (x1,y1)} and {@code (x2,y2)}
     * points.
     * <p>
     * This function uses {@link MathUtil#epsilonEqualsZero(float)}
     * to approximate ownership of the given point.
     *
     * @param px the X coordinate of the point
     * @param py the Y coordinate of the point
     * @param ax the X coordinate of the first point of the segment
     * @param ay the Y coordinate of the first point of the segment
     * @param bx the X coordinate of the second point of the segment
     * @param by the Y coordinate of the second point of the segment
     * @return <code>true</code> if the points is on segment,
     * otherwise <code>false</code>
     * @since 3.0
     */
    public static boolean intersectsPointSegment(
    		float px, float py,
    		float ax, float ay,
    		float bx, float by) {
    	return MathUtil.relativeSegmentCCW(ax, ay, bx, by, px, py, true)==0;
    }

    /**
     * Tests if the two 1D segments are intersecting.
     * <p>
     * This function is assuming that <var>l1</var> is lower
     * or equal to <var>u1</var> and <var>l2</var> is lower
     * or equal to <var>u2</var>.
     *
     * @param l1 the min coordinate of the first segment
     * @param u1 the max coordinate of the first segment
     * @param l2 the min coordinate of the second segment
     * @param u2 the max coordinate of the second segment
     * @return <code>true</code> if the two 1D segments intersect each 
     * other; <code>false</code> otherwise.
     */
    public static boolean intersectsAlignedSegments(float l1, float u1, float l2, float u2) {
    	assert(l1<=u1);
    	assert(l2<=u2);
    	if (l1<l2) return u1>=l2;
    	return u2>=l1;
    }

    /**
     * Classifies two 1D segments.
     * <p>
     * This function is assuming that <var>l1</var> is lower
     * or equal to <var>u1</var> and <var>l2</var> is lower
     * or equal to <var>u2</var>.
     *
     * @param l1 the min coordinate of the first segment
     * @param u1 the max coordinate of the first segment
     * @param l2 the min coordinate of the second segment
     * @param u2 the max coordinate of the second segment
	 * @return the value {@link IntersectionType#INSIDE} if the first segment is inside
	 * the second segment; {@link IntersectionType#OUTSIDE} if the first segment is 
	 * outside the second segment; {@link IntersectionType#ENCLOSING} if the 
	 * first segment is enclosing the second segment;
	 * {@link IntersectionType#SPANNING} otherwise.
     */
    public static IntersectionType classifiesAlignedSegments(float l1, float u1, float l2, float u2) {
    	assert(l1<=u1);
    	assert(l2<=u2);
    	if (l1<l2) {
    		if (u1<l2) return IntersectionType.OUTSIDE;
    		if (u1>u2) return IntersectionType.ENCLOSING;
    		return IntersectionType.SPANNING;
    	}
		if (u2<l1) return IntersectionType.OUTSIDE;
		if (u2>u1) return IntersectionType.INSIDE;
		return IntersectionType.SPANNING;
    }

    /**
     * Tests if the two 2D axis-aligned rectangles are intersecting.
     * <p>
     * This function is assuming that <var>lx1</var> is lower
     * or equal to <var>ux1</var>, <var>ly1</var> is lower
     * or equal to <var>uy1</var>, and so on.
     *
     * @param lx1 the X coordinate of the lowest point of the first rectangle.
     * @param ly1 the Y coordinate of the lowest point of the first rectangle.
     * @param ux1 the X coordinate of the uppest point of the first rectangle.
     * @param uy1 the Y coordinate of the uppest point of the first rectangle.
     * @param lx2 the X coordinate of the lowest point of the second rectangle.
     * @param ly2 the Y coordinate of the lowest point of the second rectangle.
     * @param ux2 the X coordinate of the uppest point of the second rectangle.
     * @param uy2 the Y coordinate of the uppest point of the second rectangle.
     * @return <code>true</code> if the two 2D rectangles intersect each 
     * other; <code>false</code> otherwise.
     */
    public static boolean intersectsAlignedRectangles(float lx1, float ly1, float ux1, float uy1, float lx2, float ly2, float ux2, float uy2) {
    	assert(lx1<=ux1);
    	assert(ly1<=uy1);
    	assert(lx2<=ux2);
    	assert(ly2<=uy2);

    	boolean intersects;
    	if (lx1<lx2) intersects = ux1>lx2;
    	else intersects = ux2>lx1;
    	
    	if (intersects) {
        	if (ly1<ly2) intersects = uy1>ly2;
        	else intersects = uy2>ly1;
    	}
    	
    	return intersects;
    }

    /**
     * Classifies two 2D axis-aligned rectangles.
     * <p>
     * This function is assuming that <var>lx1</var> is lower
     * or equal to <var>ux1</var>, <var>ly1</var> is lower
     * or equal to <var>uy1</var>, and so on.
     *
     * @param lx1 the X coordinate of the lowest point of the first rectangle.
     * @param ly1 the Y coordinate of the lowest point of the first rectangle.
     * @param ux1 the X coordinate of the uppest point of the first rectangle.
     * @param uy1 the Y coordinate of the uppest point of the first rectangle.
     * @param lx2 the X coordinate of the lowest point of the second rectangle.
     * @param ly2 the Y coordinate of the lowest point of the second rectangle.
     * @param ux2 the X coordinate of the uppest point of the second rectangle.
     * @param uy2 the Y coordinate of the uppest point of the second rectangle.
	 * @return the value {@link IntersectionType#INSIDE} if the first rectangle is inside
	 * the second rectangle; {@link IntersectionType#OUTSIDE} if the first rectangle is 
	 * outside the second rectangle; {@link IntersectionType#ENCLOSING} if the 
	 * first rectangle is enclosing the second rectangle;
	 * {@link IntersectionType#ENCLOSING} if the first rectangle is the same as the second rectangle;
	 * {@link IntersectionType#SPANNING} otherwise.
     */
    public static IntersectionType classifiesAlignedRectangles(float lx1, float ly1, float ux1, float uy1, float lx2, float ly2, float ux2, float uy2) {
    	assert(lx1<=ux1);
    	assert(ly1<=uy1);
    	assert(lx2<=ux2);
    	assert(ly2<=uy2);

    	IntersectionType inter;
    	
    	if (lx1<lx2) {
    		if (ux1<=lx2) return IntersectionType.OUTSIDE;
    		if (ux1<ux2) inter = IntersectionType.SPANNING;
    		else inter = IntersectionType.ENCLOSING;
    	} else if (lx1>lx2) {
			if (ux2<=lx1) return IntersectionType.OUTSIDE;
			if (ux1<=ux2) inter = IntersectionType.INSIDE;
			else inter = IntersectionType.SPANNING;
    	} else {
    		if (ux1==ux2) inter = IntersectionType.SAME;
    		else if (ux1<ux2) inter = IntersectionType.INSIDE;
    		else inter = IntersectionType.ENCLOSING;
    	}
    	
    	if (ly1<ly2) {
    		if (uy1<=ly2) return IntersectionType.OUTSIDE;
    		if (uy1<uy2) return inter.and(IntersectionType.SPANNING);
    		return inter.and(IntersectionType.ENCLOSING);
    	} else if (ly1>ly2) {
			if (uy2<=ly1) return IntersectionType.OUTSIDE;
			if (uy1<=uy2) return inter.and(IntersectionType.INSIDE);
			return inter.and(IntersectionType.SPANNING);
    	} else {
    		if (uy1==uy2) return inter.and(IntersectionType.SAME);
    		else if (uy1<uy2) return inter.and(IntersectionType.INSIDE);
    		return inter.and(IntersectionType.ENCLOSING);
    	}
    	
    }
    
    /**
     * Tests if the two 3D axis-aligned boxes are intersecting.
     * <p>
     * This function is assuming that <var>lx1</var> is lower
     * or equal to <var>ux1</var>, <var>ly1</var> is lower
     * or equal to <var>uy1</var>, and so on.
     *
     * @param lower1 coordinates of the lowest point of the first box.
     * @param upper1 coordinates of the uppest point of the first box.
     * @param lower2 coordinates of the lowest point of the second box.
     * @param upper2 coordinates of the uppest point of the second box.
     * @return <code>true</code> if the two 3D boxes intersect each 
     * other; <code>false</code> otherwise.
     */
    public static boolean intersectsAlignedBoxes(Point3f lower1, Point3f upper1, Point3f lower2, Point3f upper2) {
    	assert(lower1.getX()<=upper1.getX());
    	assert(lower1.getY()<=upper1.getY());
    	assert(lower1.getZ()<=upper1.getZ());
    	assert(lower2.getX()<=upper2.getX());
    	assert(lower2.getY()<=upper2.getY());
    	assert(lower2.getZ()<=upper2.getZ());
    	
    	boolean intersects;
    	if (lower1.getX()<lower2.getX()) intersects = upper1.getX()>lower2.getX();
    	else intersects = upper2.getX()>lower1.getX();
    	
    	if (intersects) {
        	if (lower1.getY()<lower2.getY()) intersects = upper1.getY()>lower2.getY();
        	else intersects = upper2.getY()>lower1.getY();
        	
        	if (intersects) {
            	if (lower1.getZ()<lower2.getZ()) intersects = upper1.getZ()>lower2.getZ();
            	else intersects = upper2.getZ()>lower1.getZ();
        	}
    	}
    	
    	return intersects;
    }

    /**
     * Tests if the two 2D minimum bounding rectangles are intersecting.
     * <p>
     * This function is assuming that <var>lx1</var> is lower
     * or equal to <var>ux1</var>, and <var>ly1</var> is lower
     * or equal to <var>uy1</var>.
     *
     * @param lower1 coordinates of the lowest point of the first rectangle.
     * @param upper1 coordinates of the uppest point of the first rectangle.
     * @param lower2 coordinates of the lowest point of the second rectangle.
     * @param upper2 coordinates of the uppest point of the second rectangle.
     * @return <code>true</code> if the two 2D rectangles intersect each 
     * other; <code>false</code> otherwise.
     */
    public static boolean intersectsAlignedRectangles(Point2f lower1, Point2f upper1, Point2f lower2, Point2f upper2) {
    	assert(lower1.getX()<=upper1.getX());
    	assert(lower1.getY()<=upper1.getY());
    	assert(lower2.getX()<=upper2.getX());
    	assert(lower2.getY()<=upper2.getY());

    	boolean intersects;
    	if (lower1.getX()<lower2.getX()) intersects = upper1.getX()>lower2.getX();
    	else intersects = upper2.getX()>lower1.getX();
    	
    	if (intersects) {
        	if (lower1.getY()<lower2.getY()) intersects = upper1.getY()>lower2.getY();
        	else intersects = upper2.getY()>lower1.getY();
    	}
    	
    	return intersects;
    }
    
    /**
     * Classifies two 3D axis-aligned boxes.
     * <p>
     * This function is assuming that <var>lx1</var> is lower
     * or equal to <var>ux1</var>, <var>ly1</var> is lower
     * or equal to <var>uy1</var>, and so on.
     * <p>
     * This function is implemented in th best efficient way according
     * to the priority of the intersections types which are deduced
     * from {@link IntersectionType#and(IntersectionType, IntersectionType)}:
     * a) if one axis is {@code OUTSIDE} the boxes are {@code OUTSIDE}, b) if
     * one axis is {@code SPANNING} the boxes are {@code SPANNING}, c) otherwise
     * the "and" rule is applied.
     *
     * @param lower1 coordinates of the lowest point of the first box.
     * @param upper1 coordinates of the uppest point of the first box.
     * @param lower2 coordinates of the lowest point of the second box.
     * @param upper2 coordinates of the uppest point of the second box.
	 * @return the value {@link IntersectionType#INSIDE} if the first box is inside
	 * the second box; {@link IntersectionType#OUTSIDE} if the first box is 
	 * outside the second box; {@link IntersectionType#ENCLOSING} if the 
	 * first box is enclosing the second box;
	 * {@link IntersectionType#SPANNING} otherwise.
     */
    public static IntersectionType classifiesAlignedBoxes(Point3f lower1, Point3f upper1, Point3f lower2, Point3f upper2) {
    	assert(lower1.getX()<=upper1.getX());
    	assert(lower1.getY()<=upper1.getY());
    	assert(lower1.getZ()<=upper1.getZ());
    	assert(lower2.getX()<=upper2.getX());
    	assert(lower2.getY()<=upper2.getY());
    	assert(lower2.getZ()<=upper2.getZ());
    	
    	IntersectionType inter;
    	
    	if (lower1.getX()<lower2.getX()) {
    		if (upper1.getX()<=lower2.getX()) return IntersectionType.OUTSIDE;
    		if (upper1.getX()<upper2.getX()) inter = IntersectionType.SPANNING;
    		else inter = IntersectionType.ENCLOSING;
    	} else if (lower1.getX()>lower2.getX()) {
			if (upper2.getX()<=lower1.getX()) return IntersectionType.OUTSIDE;
			if (upper1.getX()<=upper2.getX()) inter = IntersectionType.INSIDE;
			else inter = IntersectionType.SPANNING;
    	} else {
    		if (upper1.getX()==upper2.getX()) inter = IntersectionType.SAME;
    		else if (upper1.getX()<upper2.getX()) inter = IntersectionType.INSIDE;
    		else inter = IntersectionType.ENCLOSING;
    	}
    	
    	if (lower1.getY()<lower2.getY()) {
    		if (upper1.getY()<=lower2.getY()) return IntersectionType.OUTSIDE;
    		if (upper1.getY()<upper2.getY()) inter = inter.and(IntersectionType.SPANNING);
    		else inter = inter.and(IntersectionType.ENCLOSING);
    	} else if (lower1.getY()>lower2.getY()) {
			if (upper2.getY()<=lower1.getY()) return IntersectionType.OUTSIDE;
			if (upper1.getY()<=upper2.getY()) inter = inter.and(IntersectionType.INSIDE);
			else inter = inter.and(IntersectionType.SPANNING);
    	} else {
    		if (upper1.getY()==upper2.getY()) inter = inter.and(IntersectionType.SAME);
    		else if (upper1.getY()<upper2.getY()) inter = inter.and(IntersectionType.INSIDE);
    		else inter = inter.and(IntersectionType.ENCLOSING);
    	}
    	
    	if (lower1.getZ()<lower2.getZ()) {
    		if (upper1.getZ()<=lower2.getZ()) return IntersectionType.OUTSIDE;
    		if (upper1.getZ()<upper2.getZ()) return inter.and(IntersectionType.SPANNING);
			return inter.and(IntersectionType.ENCLOSING);
    	} else if (lower1.getZ()>lower2.getZ()) {
			if (upper2.getZ()<=lower1.getZ()) return IntersectionType.OUTSIDE;
			if (upper1.getZ()<=upper2.getZ()) return inter.and(IntersectionType.INSIDE);
			return inter.and(IntersectionType.SPANNING);
    	}
		if (upper1.getZ()==upper2.getZ()) return inter.and(IntersectionType.SAME);
		else if (upper1.getZ()<upper2.getZ()) return inter.and(IntersectionType.INSIDE);
		return inter.and(IntersectionType.ENCLOSING);
    	
    }

    /**
     * Classifies two 2D minimum bounding rectangles.
     * <p>
     * This function is assuming that <var>lx1</var> is lower
     * or equal to <var>ux1</var>, and <var>ly1</var> is lower
     * or equal to <var>uy1</var>.
     * <p>
     * This function is implemented in th best efficient way according
     * to the priority of the intersections types which are deduced
     * from {@link IntersectionType#and(IntersectionType, IntersectionType)}:
     * a) if one axis is {@code OUTSIDE} the boxes are {@code OUTSIDE}, b) if
     * one axis is {@code SPANNING} the boxes are {@code SPANNING}, c) otherwise
     * the "and" rule is applied.
     *
     * @param lower1 coordinates of the lowest point of the first rectangle.
     * @param upper1 coordinates of the uppest point of the first rectangle.
     * @param lower2 coordinates of the lowest point of the second rectangle.
     * @param upper2 coordinates of the uppest point of the second rectangle.
	 * @return the value {@link IntersectionType#INSIDE} if the first rectangle is inside
	 * the second rectangle; {@link IntersectionType#OUTSIDE} if the first rectangle is 
	 * outside the second rectangle; {@link IntersectionType#ENCLOSING} if the 
	 * first rectangle is enclosing the second rectangle;
	 * {@link IntersectionType#SPANNING} otherwise.
     */
    public static IntersectionType classifiesAlignedRectangles(Point2f lower1, Point2f upper1, Point2f lower2, Point2f upper2) {
    	assert(lower1.getX()<=upper1.getX());
    	assert(lower1.getY()<=upper1.getY());
    	assert(lower2.getX()<=upper2.getX());
    	assert(lower2.getY()<=upper2.getY());

    	IntersectionType inter;
    	
    	if (lower1.getX()<lower2.getX()) {
    		if (upper1.getX()<=lower2.getX()) return IntersectionType.OUTSIDE;
    		if (upper1.getX()<upper2.getX()) inter = IntersectionType.SPANNING;
    		else inter = IntersectionType.ENCLOSING;
    	} else if (lower1.getX()>lower2.getX()) {
			if (upper2.getX()<=lower1.getX()) return IntersectionType.OUTSIDE;
			if (upper1.getX()<=upper2.getX()) inter = IntersectionType.INSIDE;
			else inter = IntersectionType.SPANNING;
    	} else {
    		if (upper1.getX()==upper2.getX()) inter = IntersectionType.SAME;
    		else if (upper1.getX()<upper2.getX()) inter = IntersectionType.INSIDE;
    		else inter = IntersectionType.ENCLOSING;
    	}
    	
    	if (lower1.getY()<lower2.getY()) {
    		if (upper1.getY()<=lower2.getY()) return IntersectionType.OUTSIDE;
    		if (upper1.getY()<upper2.getY()) return inter.and(IntersectionType.SPANNING);
			return inter.and(IntersectionType.ENCLOSING);
    	} else if (lower1.getY()>lower2.getY()) {
			if (upper2.getY()<=lower1.getY()) return IntersectionType.OUTSIDE;
			if (upper1.getY()<=upper2.getY()) return inter.and(IntersectionType.INSIDE);
			return inter.and(IntersectionType.SPANNING);
    	}
    	if (upper1.getY()==upper2.getY()) return inter.and(IntersectionType.SAME);
    	else if (upper1.getY()<upper2.getY()) return inter.and(IntersectionType.INSIDE);
    	else return inter.and(IntersectionType.ENCLOSING);
    	
    }
    
    /**
	 * Given a point p, this function computes the point q1 on (or in) 
	 * this OBB, closest to p and the point q2 on farest to p.
	 * 
     * @param center is the center point of the oriented box.
     * @param axis are the unit vectors of the oriented box axis.
     * @param extent are the sizes of the oriented box.
	 * @param p is the point used as reference to compute the closest point on this OBB
	 * @param closest is the closest point or <var>q1</var>, or <code>null</code>.
	 * @param farest is the farest point or <var>q2</var>, or <code>null</code>.
	 */
	public static void computeClosestFarestOBBPoints(
			Point3f center, Vector3f[] axis,  float[] extent,
			Point3f p, Point3f closest, Point3f farest) {
    	assert(axis.length==3);
    	assert(extent.length==3);
    	assert(extent[0]>=0);
    	assert(extent[1]>=0);
    	assert(extent[2]>=0);

    	Vector3f d = new Vector3f();
		d.sub(p, center); // d = p - center

		// Start results at center of box; make steps from there
		if (closest!=null) closest.set(center);		
		if (farest!=null) farest.set(center);		
		
		// For each OBB axis...
		float d1, d2;
		
		for (int i=0; i<axis.length; ++i) {
			// ...project d onto that axis to get the distance along the axis of d from the box center
			d1 = d2 = d.dot(axis[i]);
			
			if (closest!=null) {
				// If distance farther than the box extents, clamp to the box
				if (d1 > extent[i]) d1 = extent[i];
				else if (d1 < -extent[i]) d1 = -extent[i];

				// Step that distance along the axis to get world coordinate
				//q += dist * this.axis[i];
				closest.scaleAdd(d1, axis[i], closest);
			}

			if (farest!=null) {
				// Clamp to the other side of the box
				if (d2 >= 0.) d2 = - extent[i];
				else d2 = extent[i];
				
				// Step that distance along the axis to get world coordinate
				//q += dist * this.axis[i];
				farest.scaleAdd(d2, axis[i], farest);
			}
		}
	}

    /**
	 * Given a point p, this function computes the point q1 on (or in) 
	 * this OBR, closest to p and the point q2 on farest to p.
	 * 
     * @param center is the center point of the oriented rectangle.
     * @param axis are the unit vectors of the oriented rectangle axis.
     * @param extent are the sizes of the oriented rectangle.
	 * @param p is the point used as reference to compute the closest point on this OBR
	 * @param closest is the closest point or <var>q1</var>, or <code>null</code>.
	 * @param farest is the farest point or <var>q2</var>, or <code>null</code>.
	 */
	public static void computeClosestFarestOBRPoints(
			Point2f center, Vector2f[] axis,  float[] extent,
			Point2f p, Point2f closest, Point2f farest) {
    	assert(axis.length==2);
    	assert(extent.length==2);
    	assert(extent[0]>=0);
    	assert(extent[1]>=0);

    	Vector2f d = new Vector2f();
		d.sub(p, center); // d = p - center

		// Start results at center of box; make steps from there
		if (closest!=null) closest.set(center);		
		if (farest!=null) farest.set(center);		
		
		// For each OBB axis...
		float d1, d2;
		
		for (int i=0; i<axis.length; ++i) {
			// ...project d onto that axis to get the distance along the axis of d from the box center
			d1 = d2 = d.dot(axis[i]);
			
			if (closest!=null) {
				// If distance farther than the box extents, clamp to the box
				if (d1 > extent[i]) d1 = extent[i];
				else if (d1 < -extent[i]) d1 = -extent[i];

				// Step that distance along the axis to get world coordinate
				//q += dist * this.axis[i];
				closest.scaleAdd(d1, axis[i], closest);
			}

			if (farest!=null) {
				// Clamp to the other side of the box
				if (d2 >= 0.) d2 = - extent[i];
				else d2 = extent[i];
				
				// Step that distance along the axis to get world coordinate
				//q += dist * this.axis[i];
				farest.scaleAdd(d2, axis[i], farest);
			}
		}
	}

	/**
     * Classifies a sphere against an oriented box.
     * <p>
     * The extents are assumed to be positive or zero.
     * The lengths of the given arrays are assumed to be <code>3</code>.
     *
     * @param sphereCenter are the coordinates of the sphere center.
     * @param sphereRadius is the radius of the sphere.
     * @param boxCenter is the center point of the oriented box.
     * @param boxAxis are the unit vectors of the oriented box axis.
     * @param boxExtent are the sizes of the oriented box.
	 * @return the value {@link IntersectionType#INSIDE} if the sphere is inside
	 * the box; {@link IntersectionType#OUTSIDE} if the sphere is 
	 * outside the box; {@link IntersectionType#ENCLOSING} if the 
	 * sphere is enclosing the box;
	 * {@link IntersectionType#SPANNING} otherwise.
     */
    public static IntersectionType classifiesSolidSphereOrientedBox(
    		Point3f sphereCenter, float sphereRadius,
    		Point3f boxCenter, Vector3f[] boxAxis,  float[] boxExtent) {
    	assert(sphereRadius>=0);
    	assert(boxAxis.length==3);
    	assert(boxExtent.length==3);
    	assert(boxExtent[0]>=0);
    	assert(boxExtent[1]>=0);
    	assert(boxExtent[2]>=0);
    	
		// Find points on OBB closest and farest to sphere center
		Point3f closest = new Point3f();
		Point3f farest = new Point3f();
		computeClosestFarestOBBPoints(
				boxCenter, boxAxis, boxExtent,
				sphereCenter, closest, farest);
		
		// Sphere and OBB intersect if the (squared) distance from sphere
		// center to point p is less than the (squared) sphere radius
		float squaredRadius = sphereRadius * sphereRadius;
		float d;
		
		// Compute (squared) distance to closest point
		if (sphereCenter.distanceSquared(farest)<squaredRadius) return IntersectionType.ENCLOSING;
		
		// Compute (squared) distance to closest point
		d = sphereCenter.distanceSquared(closest);
		if (d>squaredRadius+MathConstants.EPSILON) return IntersectionType.OUTSIDE;
	
		// If the sphere center is inside the box and the
		// radius is inside the box's extents, then the
		// sphere is inside the box.
		
		if (MathUtil.epsilonEqualsZero(d)) {
			Vector3f v = new Vector3f();
			v.sub(sphereCenter, boxCenter);
			for(int i=0; i<boxAxis.length; ++i) {
				d = Math.abs(v.dot(boxAxis[i]));
				if (d+sphereRadius>boxExtent[i]) return IntersectionType.SPANNING;
			}
			return IntersectionType.INSIDE;
		}
		
		return IntersectionType.SPANNING;

    }
    
    /**
     * Classifies a circle against an oriented rectangle.
     * <p>
     * The extents are assumed to be positive or zero.
     * The lengths of the given arrays are assumed to be <code>2</code>.
     *
     * @param circleCenter are the coordinates of the circle center.
     * @param circleRadius is the radius of the circle.
     * @param boxCenter is the center point of the oriented rectangle.
     * @param boxAxis are the unit vectors of the oriented rectangle axis.
     * @param boxExtent are the sizes of the oriented rectangle.
	 * @return the value {@link IntersectionType#INSIDE} if the first circle is inside
	 * the second rectangle; {@link IntersectionType#OUTSIDE} if the first circle is 
	 * outside the second rectangle; {@link IntersectionType#ENCLOSING} if the 
	 * first circle is enclosing the second rectangle;
	 * {@link IntersectionType#SPANNING} otherwise.
     */
    public static IntersectionType classifiesSolidCircleOrientedRectangle(
    		Point2f circleCenter, float circleRadius,
    		Point2f boxCenter, Vector2f[] boxAxis,  float[] boxExtent) {
    	assert(circleRadius>=0);
    	assert(boxAxis.length==2);
    	assert(boxExtent.length==2);
    	assert(boxExtent[0]>=0);
    	assert(boxExtent[1]>=0);
    	
		// Find points on OBR closest and farest to sphere center
		Point2f closest = new Point2f();
		Point2f farest = new Point2f();
		computeClosestFarestOBRPoints(
				boxCenter, boxAxis, boxExtent,
				circleCenter, closest, farest);
		
		// Sphere and OBR intersect if the (squared) distance from sphere
		// center to point p is less than the (squared) sphere radius
		float squaredRadius = circleRadius * circleRadius;
		float d;
		
		// Compute (squared) distance to closest point
		if (circleCenter.distanceSquared(farest)<squaredRadius) return IntersectionType.ENCLOSING;
		
		// Compute (squared) distance to closest point
		d = circleCenter.distanceSquared(closest);
		if (d>squaredRadius+MathConstants.EPSILON) return IntersectionType.OUTSIDE;
	
		// If the sphere center is inside the box and the
		// radius is inside the box's extents, then the
		// sphere is inside the box.
		
		if (MathUtil.epsilonEqualsZero(d)) {
			Vector2f v = new Vector2f();
			v.sub(circleCenter, boxCenter);
			for(int i=0; i<boxAxis.length; ++i) {
				d = Math.abs(v.dot(boxAxis[i]));
				if (d+circleRadius>boxExtent[i]) return IntersectionType.SPANNING;
			}
			return IntersectionType.INSIDE;
		}
		
		return IntersectionType.SPANNING;

    }

    /** Replies if the specified box intersects the specified sphere.
     *
     * @param sphereCenter are the coordinates of the sphere center.
     * @param sphereRadius is the radius of the sphere.
     * @param boxCenter is the center point of the oriented box.
     * @param boxAxis are the unit vectors of the oriented box axis.
     * @param boxExtent are the sizes of the oriented box.
	 * @return <code>true</code> if intersecting, otherwise <code>false</code>
     */
    public static boolean intersectsSolidSphereOrientedBox(
    		Point3f sphereCenter, float sphereRadius,
    		Point3f boxCenter, Vector3f[] boxAxis,  float[] boxExtent) {
		// Find points on OBB closest and farest to sphere center
		Point3f closest = new Point3f();
		Point3f farest = new Point3f();
		IntersectionUtil.computeClosestFarestOBBPoints(
				boxCenter, boxAxis, boxExtent,
				sphereCenter, closest, farest);
		
		// Sphere and OBB intersect if the (squared) distance from sphere
		// center to point p is less than the (squared) sphere radius
		float squaredRadius = sphereRadius * sphereRadius;
		
		if (sphereCenter.distanceSquared(closest)>squaredRadius+MathConstants.EPSILON) return false;
		
		return true;
    }

    /** Replies if the specified rectangle intersects the specified circle.
    *
    * @param circleCenter are the coordinates of the circle center.
    * @param circleRadius is the radius of the circle.
    * @param obrCenter is the center point of the OBR.
    * @param obrAxis are the unit vectors of the OBR axis.
    * @param obrExtent are the sizes of the OBR.
	 * @return <code>true</code> if intersecting, otherwise <code>false</code>
    */
   public static boolean intersectsSolidCircleOrientedRectangle(
   		Point2f circleCenter, float circleRadius,
   		Point2f obrCenter, Vector2f[] obrAxis,  float[] obrExtent) {
		// Find points on OBR closest and farest to sphere center
		Point2f closest = new Point2f();
		Point2f farest = new Point2f();
		IntersectionUtil.computeClosestFarestOBRPoints(
				obrCenter, obrAxis, obrExtent,
				circleCenter, closest, farest);
		
		// Circle and OBR intersect if the (squared) distance from sphere
		// center to point p is less than the (squared) sphere radius
		float squaredRadius = circleRadius * circleRadius;
		
		if (circleCenter.distanceSquared(closest)>squaredRadius+MathConstants.EPSILON) return false;
		
		return true;
   }

   /**
     * Classifies the OBB's axis according to {@code |T.L|}, {@code ra} and {@rb}
     * where {@code T} is the vector between the OBB's centers, {@code L} is
     * the separation vector, {@code ra} is the size of the projection of the first
     * OBB on L, and {@code rb} is the size of the projection of the second
     * OBB on L.
     * <p>
     * This function is also working fine for 2D classifications.
     * 
     * @param tl is {@code |T.L|}
     * @param ra is the size of the projection of the first OBB on L
     * @param rb is the size of the projection of the second OBB on L
     * @param type is the intersection type previously detected for the other axis.
	 * @return the type of intersection
	 * @see <a href="http://www.gamasutra.com/features/19991018/Gomez_5.htm">Gamasutra OBB intersection test</a>
	 * @see #classifiesOrientedBoxes(Point3f, Vector3f[], float[], Point3f, Vector3f[], float[])
	 * @see #classifiesOrientedRectangles(Point2f, Vector2f[], float[], Point2f, Vector2f[], float[])
     */
    private static IntersectionType classifiesOrientedBoxAxis(float tl, float ra, float rb, IntersectionType type) {
    	// Special case: same center, same radius.
    	if (tl==0. && ra==rb) return type;
    	// A and B do not overlap.
    	if (tl > ra+rb) return IntersectionType.OUTSIDE;
    	
    	IntersectionType t;
    	
    	// A is enclosing B
    	if (tl+rb < ra) t = IntersectionType.ENCLOSING;
    	// A is inside B
    	else if (tl+ra < rb) t = IntersectionType.INSIDE;
    	// A and B do overlap
    	else return IntersectionType.SPANNING;
    	
    	return (type==null) ? t : IntersectionType.and(type, t);
    }
    
    /**
     * Classifies two oriented boxes.
     * <p>
     * The extents are assumed to be positive or zero.
     * The lengths of the given arrays are assumed to be <code>3</code>.
     * <p>
     * This function uses the "separating axis theorem" which states that 
     * for any two OBBs (AABB is a special case of OBB)
     * that do not touch, a separating axis can be found.
     * <p>
     * This function uses an general intersection test between two OBB.
     * If the first box is expected to be an AAB, please use the
     * optimized algorithm given by
     * {@link #classifiesAlignedBoxOrientedBox(Point3f, Point3f, Point3f, Vector3f[], float[])}.
     *
     * @param center1 is the center point of the first oriented box.
     * @param axis1 are the unit vectors of the first oriented box axis.
     * @param extent1 are the sizes of the first oriented box.
     * @param center2 is the center point of the second oriented box.
     * @param axis2 are the unit vectors of the second oriented box axis.
     * @param extent2 are the sizes of the second oriented box.
	 * @return the value {@link IntersectionType#INSIDE} if the first box is inside
	 * the second box; {@link IntersectionType#OUTSIDE} if the first box is 
	 * outside the second box; {@link IntersectionType#ENCLOSING} if the 
	 * first box is enclosing the second box;
	 * {@link IntersectionType#SPANNING} otherwise.
	 * @see "RTCD pages 102-105"
	 * @see <a href="http://www.gamasutra.com/features/19991018/Gomez_5.htm">Gamasutra OBB intersection test</a>
     */
    public static IntersectionType classifiesOrientedBoxes(
    		Point3f center1, Vector3f[] axis1,  float[] extent1,
    		Point3f center2, Vector3f[] axis2,  float[] extent2) {
    	assert(axis1.length==3);
    	assert(axis2.length==3);
    	assert(extent1.length==3);
    	assert(extent2.length==3);
    	assert(extent1[0]>=0);
    	assert(extent1[1]>=0);
    	assert(extent1[2]>=0);
    	assert(extent2[0]>=0);
    	assert(extent2[1]>=0);
    	assert(extent2[2]>=0);
    	
    	//translation, in parent frame
    	Vector3f v = new Vector3f();
    	v.sub(center2, center1);

    	//translation, in A's frame
    	float T[] = new float[] {
    			v.dot(axis1[0]),
    			v.dot(axis1[1]),
    			v.dot(axis1[2])};

    	//B's basis with respect to A's local frame
    	float R[][] = new float[3][3];
    	float absR[][] = new float[3][3];
    	int i,k;

    	//calculate rotation matrix
    	for(i=0; i<3 ; ++i) {
    		for(k=0; k<3; ++k ) {
    			R[i][k] = axis1[i].dot(axis2[k]);
    			absR[i][k] = (R[i][k]<0.) ? -R[i][k] : R[i][k];
    		}
    	}

    	// ALGORITHM: Use the separating axis test for all 15 potential
    	// separating axes. If a separating axis could not be found, the two
    	// boxes overlap.
    	float ra, rb, t;
    	IntersectionType type = null;

    	//A's basis vectors
		ra = extent1[0];
		rb = extent2[0]*absR[0][0] + extent2[1]*absR[0][1] + extent2[2]*absR[0][2];
		t = Math.abs(T[0]);
		type = classifiesOrientedBoxAxis(t, ra, rb, type);
		if (type==IntersectionType.OUTSIDE) return type;

		for(i=1; i<3; ++i) {
			ra = extent1[i];
			rb = extent2[0]*absR[i][0] + extent2[1]*absR[i][1] + extent2[2]*absR[i][2];
			t = Math.abs(T[i]);
			type = classifiesOrientedBoxAxis(t, ra, rb, type);
			if (type==IntersectionType.OUTSIDE) return type;
    	}

    	//B's basis vectors
    	for(k=0 ;k<3 ;++k) {
    		ra = extent1[0]*absR[0][k] + extent1[1]*absR[1][k] + extent1[2]*absR[2][k];
    		rb = extent2[k];
    		t =	Math.abs( T[0]*R[0][k] + T[1]*R[1][k] + T[2]*R[2][k] );
    		type = classifiesOrientedBoxAxis(t, ra, rb, type);
			if (type==IntersectionType.OUTSIDE) return type;
    	}

    	//9 cross products

    	//L = A0 x B0
    	ra = extent1[1]*absR[2][0] + extent1[2]*absR[1][0];
    	rb = extent2[1]*absR[0][2] + extent2[2]*absR[0][1];
    	t = Math.abs( T[2]*R[1][0] - T[1]*R[2][0] );
		type = classifiesOrientedBoxAxis(t, ra, rb, type);
		if (type==IntersectionType.OUTSIDE) return type;

    	//L = A0 x B1
    	ra = extent1[1]*absR[2][1] + extent1[2]*absR[1][1];
    	rb = extent2[0]*absR[0][2] + extent2[2]*absR[0][0];
    	t = Math.abs( T[2]*R[1][1] - T[1]*R[2][1] );
		type = classifiesOrientedBoxAxis(t, ra, rb, type);
		if (type==IntersectionType.OUTSIDE) return type;

    	//L = A0 x B2
    	ra = extent1[1]*absR[2][2] + extent1[2]*absR[1][2];
    	rb = extent2[0]*absR[0][1] + extent2[1]*absR[0][0];
    	t = Math.abs( T[2]*R[1][2] - T[1]*R[2][2] );
		type = classifiesOrientedBoxAxis(t, ra, rb, type);
		if (type==IntersectionType.OUTSIDE) return type;

    	//L = A1 x B0
    	ra = extent1[0]*absR[2][0] + extent1[2]*absR[0][0];
    	rb = extent2[1]*absR[1][2] + extent2[2]*absR[1][1];
    	t = Math.abs( T[0]*R[2][0] - T[2]*R[0][0] );
		type = classifiesOrientedBoxAxis(t, ra, rb, type);
		if (type==IntersectionType.OUTSIDE) return type;

    	//L = A1 x B1
    	ra = extent1[0]*absR[2][1] + extent1[2]*absR[0][1];
    	rb = extent2[0]*absR[1][2] + extent2[2]*absR[1][0];
    	t = Math.abs( T[0]*R[2][1] - T[2]*R[0][1] );
		type = classifiesOrientedBoxAxis(t, ra, rb, type);
		if (type==IntersectionType.OUTSIDE) return type;

    	//L = A1 x B2
    	ra = extent1[0]*absR[2][2] + extent1[2]*absR[0][2];
    	rb = extent2[0]*absR[1][1] + extent2[1]*absR[1][0];
    	t = Math.abs( T[0]*R[2][2] - T[2]*R[0][2] );
		type = classifiesOrientedBoxAxis(t, ra, rb, type);
		if (type==IntersectionType.OUTSIDE) return type;

    	//L = A2 x B0
    	ra = extent1[0]*absR[1][0] + extent1[1]*absR[0][0];
    	rb = extent2[1]*absR[2][2] + extent2[2]*absR[2][1];
    	t = Math.abs( T[1]*R[0][0] - T[0]*R[1][0] );
		type = classifiesOrientedBoxAxis(t, ra, rb, type);
		if (type==IntersectionType.OUTSIDE) return type;

    	//L = A2 x B1
    	ra = extent1[0]*absR[1][1] + extent1[1]*absR[0][1];
    	rb = extent2[0]*absR[2][2] + extent2[2]*absR[2][0];
    	t = Math.abs( T[1]*R[0][1] - T[0]*R[1][1] );
		type = classifiesOrientedBoxAxis(t, ra, rb, type);
		if (type==IntersectionType.OUTSIDE) return type;

    	//L = A2 x B2
    	ra = extent1[0]*absR[1][2] + extent1[1]*absR[0][2];
    	rb = extent2[0]*absR[2][1] + extent2[1]*absR[2][0];
    	t = Math.abs( T[1]*R[0][2] - T[0]*R[1][2] );
		type = classifiesOrientedBoxAxis(t, ra, rb, type);
		if (type==IntersectionType.OUTSIDE) return type;

    	/*no separating axis found, the two boxes overlap */

    	return type;
    }

    /**
     * Classifies two oriented rectangles.
     * <p>
     * The extents are assumed to be positive or zero.
     * The lengths of the given arrays are assumed to be <code>2</code>.
     * <p>
     * This function uses the "separating axis theorem" which states that 
     * for any two OBRs (MBRis a special case of OBR)
     * that do not touch, a separating axis can be found.
     * <p>
     * This function uses an general intersection test between two OBB.
     * If the first box is expected to be an AAB, please use the
     * optimized algorithm given by
     * {@link #classifiesAlignedBoxOrientedBox(Point3f, Point3f, Point3f, Vector3f[], float[])}.
     * <p>
     * <strong>Basic Algorithm:</strong>
     * <>
     * To be able to decide whether two polygons are intersecting (touching each other) 
     * we can use the following basic facts:
     * <ul>
     * <li>If two convex polygons are not intersecting, there exists a line that passes between them.</li>
     * <li>Such a line only exists if one of the sides of one of the polygons forms such a line.</li>
     * </ul>
     * <p>
     * The first statement is easy. Since the polygons are both convex, you'll be able to draw a line 
     * with one polygon on one side and the other polygon on the other side unless they are intersecting. 
     * The second is slightly less intuitive.
     * <center><img src="./doc-files/obr_fig1.png" alt="Dividing axis"></center> 
     * Unless the closest sided of the polygons are parallel to each other, the 
     * point where they get closest to each other is the point where a corner of one 
     * polygon gets closest to a side of the other polygon. This side will then 
     * form a separating axis between the polygons. If the sides are parallel, 
     * they both are separating axes.
     * <p>
     * How does this concretely help us decide whether polygon A and B intersect? 
     * We just go over each side of each polygon and check whether it forms a 
     * separating axis. To do this we'll be using some basic vector math to 
     * squash all the points of both polygons onto a line that is perpendicular 
     * to the potential separating line. 
     * <center><img src="./doc-files/obr_fig2.png" alt="Projecting polygons onto a line"></center>
     * Now the whole problem is conveniently 1-dimensional. We can determine a region 
     * in which the points for each polygon lie, and this line is a separating axis 
     * if these regions do not overlap. 
     * <p>
     * If, after checking each line from both polygons, no separating axis was 
     * found, it has been proven that the polygons intersect and something has 
     * to be done about it. 
     *
     * @param center1 is the center point of the first oriented rectangle.
     * @param axis1 are the unit vectors of the first oriented rectangle axis.
     * @param extent1 are the sizes of the first oriented rectangle.
     * @param center2 is the center point of the second oriented rectangle.
     * @param axis2 are the unit vectors of the second oriented rectangle axis.
     * @param extent2 are the sizes of the second oriented rectangle.
	 * @return the value {@link IntersectionType#INSIDE} if the first rectangle is inside
	 * the second rectangle; {@link IntersectionType#OUTSIDE} if the first rectangle is 
	 * outside the second rectangle; {@link IntersectionType#ENCLOSING} if the 
	 * first rectangle is enclosing the second rectangle;
	 * {@link IntersectionType#SPANNING} otherwise.
	 * @see "RTCD pages 102-105"
	 * @see <a href="http://www.gamasutra.com/features/19991018/Gomez_5.htm">Gamasutra OBB intersection test</a>
     */
    public static IntersectionType classifiesOrientedRectangles(
    		Point2f center1, Vector2f[] axis1,  float[] extent1,
    		Point2f center2, Vector2f[] axis2,  float[] extent2) {
    	assert(axis1.length==2);
    	assert(axis2.length==2);
    	assert(extent1.length==2);
    	assert(extent2.length==2);
    	assert(extent1[0]>=0);
    	assert(extent1[1]>=0);
    	assert(extent2[0]>=0);
    	assert(extent2[1]>=0);

    	//translation, in parent frame
    	Vector2f v = new Vector2f();
    	v.sub(center2, center1);

    	//translation, in A's frame
    	float T[] = new float[] {
    			v.dot(axis1[0]),
    			v.dot(axis1[1])};

    	//B's basis with respect to A's local frame
    	float R[][] = new float[2][2];
    	float absR[][] = new float[2][2];
    	int i,k;

    	//calculate rotation matrix
    	for(i=0; i<2; ++i) {
    		for(k=0; k<2; ++k) {
    			R[i][k] = axis1[i].dot(axis2[k]);
    			absR[i][k] = (R[i][k]<0.) ? -R[i][k] : R[i][k];
    		}
    	}

    	// ALGORITHM: Use the separating axis test for all 8 potential
    	// separating axes. If a separating axis could not be found, the two
    	// rectangles overlap.
    	
    	// L is the separation axis
    	// (A0,A1) are the axis of the first rectangle, when used with a 3D vector
    	// (eg. A2 or B2) it is assumed there third coordiates are equal to 0.
    	// (B0,B1) are the axis of the second rectangle, when used with a 3D vector
    	// (eg. A2 or B2) it is assumed there third coordiates are equal to 0.
    	// A2 = B2 = (0,0,1) - virtual 3d vectors used to symplify the 3D algorithm.
    	// ra and rb are respectively the project of A and B on the separation axis.
    	// t is the distance between the A's and B's centers, projected on the separation axis.
    	float ra, rb, t;
    	IntersectionType type = null;

    	//L = A0
		ra = extent1[0];
		rb = extent2[0]*absR[0][0] + extent2[1]*absR[0][1];
		t = Math.abs(T[0]);
		type = classifiesOrientedBoxAxis(t, ra, rb, type);
		if (type==IntersectionType.OUTSIDE) return type;

    	//L = A1
		ra = extent1[1];
		rb = extent2[0]*absR[1][0] + extent2[1]*absR[1][1];
		t = Math.abs(T[1]);
		type = classifiesOrientedBoxAxis(t, ra, rb, type);
		if (type==IntersectionType.OUTSIDE) return type;

    	//L = B0
		ra = extent1[0]*absR[0][0] + extent1[1]*absR[1][0];
		rb = extent2[0];
		t = T[0]*absR[0][0] + T[1]*absR[1][0];
		type = classifiesOrientedBoxAxis(t, ra, rb, type);
		if (type==IntersectionType.OUTSIDE) return type;

    	//L = B1
		ra = extent1[0]*absR[0][1] + extent1[1]*absR[1][1];
		rb = extent2[1];
		t = T[0]*absR[0][1] + T[1]*absR[1][1];
		type = classifiesOrientedBoxAxis(t, ra, rb, type);
		if (type==IntersectionType.OUTSIDE) return type;

    	//L = A0 x B0, ra = rb = t = 0, discarted
		
    	//L = A0 x B1, ra = rb = t = 0, discarted
		
    	//L = A0 x B2
    	ra = extent1[1];
    	rb = extent2[0]*absR[0][1] + extent2[1]*absR[0][0];
    	t = Math.abs( T[1] );
		type = classifiesOrientedBoxAxis(t, ra, rb, type);
		if (type==IntersectionType.OUTSIDE) return type;

    	//L = A1 x B0, ra = rb = t = 0, discarted

    	//L = A1 x B1, ra = rb = t = 0, discarted

    	//L = A1 x B2
    	ra = extent1[0];
    	rb = extent2[0]*absR[1][1] + extent2[1]*absR[1][0];
    	t = Math.abs( T[0] );
		type = classifiesOrientedBoxAxis(t, ra, rb, type);
		if (type==IntersectionType.OUTSIDE) return type;

    	//L = A2 x B0
    	ra = extent1[0]*absR[1][0] + extent1[1]*absR[0][0];
    	rb = extent2[1];
    	t = Math.abs( T[1]*R[0][0] - T[0]*R[1][0] );
		type = classifiesOrientedBoxAxis(t, ra, rb, type);
		if (type==IntersectionType.OUTSIDE) return type;

    	//L = A2 x B1
    	ra = extent1[0]*absR[1][1] + extent1[1]*absR[0][1];
    	rb = extent2[0];
    	t = Math.abs( T[1]*R[0][1] - T[0]*R[1][1] );
		type = classifiesOrientedBoxAxis(t, ra, rb, type);
		if (type==IntersectionType.OUTSIDE) return type;

    	//L = A2 x B2, ra = rb = t = 0, discarted

		/*no separating axis found, the two boxes overlap */

    	return type;
    }

    /** Replies if the specified boxes intersect.
     * <p>
     * The extents are assumed to be positive or zero.
     * The lengths of the given arrays are assumed to be <code>3</code>.
     * <p>
     * This function uses the "separating axis theorem" which states that 
     * for any two OBBs (AABB is a special case of OBB)
     * that do not touch, a separating axis can be found.
     * <p>
     * This function uses an general intersection test between two OBB.
     * If the first box is expected to be an AAB, please use the
     * optimized algorithm given by
     * {@link #intersectsAlignedBoxOrientedBox(Point3f, Point3f, Point3f, Vector3f[], float[])}.
     *
     * @param center1 is the center point of the first oriented box.
     * @param axis1 are the unit vectors of the first oriented box axis.
     * @param extent1 are the sizes of the first oriented box.
     * @param center2 is the center point of the second oriented box.
     * @param axis2 are the unit vectors of the second oriented box axis.
     * @param extent2 are the sizes of the second oriented box.
	 * @return <code>true</code> if intersecting, otherwise <code>false</code>
	 * @see "RTCD pages 102-105"
	 * @see <a href="http://www.gamasutra.com/features/19991018/Gomez_5.htm">OBB collision detection on Gamasutra.com</a>
     */
    public static boolean intersectsOrientedBoxes(
    		Point3f center1, Vector3f[] axis1,  float[] extent1,
    		Point3f center2, Vector3f[] axis2,  float[] extent2) {
    	assert(axis1.length==3);
    	assert(axis2.length==3);
    	assert(extent1.length==3);
    	assert(extent2.length==3);
    	assert(extent1[0]>=0);
    	assert(extent1[1]>=0);
    	assert(extent1[2]>=0);
    	assert(extent2[0]>=0);
    	assert(extent2[1]>=0);
    	assert(extent2[2]>=0);
    	
    	//translation, in parent frame
    	Vector3f v = new Vector3f();
    	v.sub(center2, center1);

    	//translation, in A's frame
    	float T[] = new float[] {
    			v.dot(axis1[0]),
    			v.dot(axis1[1]),
    			v.dot(axis1[2])};

    	//B's basis with respect to A's local frame
    	float R[][] = new float[3][3];
    	float absR[][] = new float[3][3];
    	int i,k;

    	//calculate rotation matrix
    	for(i=0; i<3 ; ++i) {
    		for(k=0; k<3; ++k ) {
    			R[i][k] = axis1[i].dot(axis2[k]);
    			absR[i][k] = (R[i][k]<0.) ? -R[i][k] : R[i][k];
    		}
    	}

    	// ALGORITHM: Use the separating axis test for all 15 potential
    	// separating axes. If a separating axis could not be found, the two
    	// boxes overlap.
    	float ra, rb, t;

    	//A's basis vectors
		for(i=0; i<3; ++i) {
			ra = extent1[i];
			rb = extent2[0]*absR[i][0] + extent2[1]*absR[i][1] + extent2[2]*absR[i][2];
			t = Math.abs(T[i]);
			if (t > ra + rb) return false;
    	}

    	//B's basis vectors
    	for(k=0 ;k<3 ;++k) {
    		ra = extent1[0]*absR[0][k] + extent1[1]*absR[1][k] + extent1[2]*absR[2][k];
    		rb = extent2[k];
    		t =	Math.abs( T[0]*R[0][k] + T[1]*R[1][k] + T[2]*R[2][k] );
			if (t > ra + rb) return false;
    	}

    	//9 cross products

    	//L = A0 x B0
    	ra = extent1[1]*absR[2][0] + extent1[2]*absR[1][0];
    	rb = extent2[1]*absR[0][2] + extent2[2]*absR[0][1];
    	t = Math.abs( T[2]*R[1][0] - T[1]*R[2][0] );
		if (t > ra + rb) return false;

    	//L = A0 x B1
    	ra = extent1[1]*absR[2][1] + extent1[2]*absR[1][1];
    	rb = extent2[0]*absR[0][2] + extent2[2]*absR[0][0];
    	t = Math.abs( T[2]*R[1][1] - T[1]*R[2][1] );
		if (t > ra + rb) return false;

    	//L = A0 x B2
    	ra = extent1[1]*absR[2][2] + extent1[2]*absR[1][2];
    	rb = extent2[0]*absR[0][1] + extent2[1]*absR[0][0];
    	t = Math.abs( T[2]*R[1][2] - T[1]*R[2][2] );
		if (t > ra + rb) return false;

    	//L = A1 x B0
    	ra = extent1[0]*absR[2][0] + extent1[2]*absR[0][0];
    	rb = extent2[1]*absR[1][2] + extent2[2]*absR[1][1];
    	t = Math.abs( T[0]*R[2][0] - T[2]*R[0][0] );
		if (t > ra + rb) return false;

    	//L = A1 x B1
    	ra = extent1[0]*absR[2][1] + extent1[2]*absR[0][1];
    	rb = extent2[0]*absR[1][2] + extent2[2]*absR[1][0];
    	t = Math.abs( T[0]*R[2][1] - T[2]*R[0][1] );
		if (t > ra + rb) return false;

    	//L = A1 x B2
    	ra = extent1[0]*absR[2][2] + extent1[2]*absR[0][2];
    	rb = extent2[0]*absR[1][1] + extent2[1]*absR[1][0];
    	t = Math.abs( T[0]*R[2][2] - T[2]*R[0][2] );
		if (t > ra + rb) return false;

    	//L = A2 x B0
    	ra = extent1[0]*absR[1][0] + extent1[1]*absR[0][0];
    	rb = extent2[1]*absR[2][2] + extent2[2]*absR[2][1];
    	t = Math.abs( T[1]*R[0][0] - T[0]*R[1][0] );
		if (t > ra + rb) return false;

    	//L = A2 x B1
    	ra = extent1[0]*absR[1][1] + extent1[1]*absR[0][1];
    	rb = extent2[0]*absR[2][2] + extent2[2]*absR[2][0];
    	t = Math.abs( T[1]*R[0][1] - T[0]*R[1][1] );
		if (t > ra + rb) return false;

    	//L = A2 x B2
    	ra = extent1[0]*absR[1][2] + extent1[1]*absR[0][2];
    	rb = extent2[0]*absR[2][1] + extent2[1]*absR[2][0];
    	t = Math.abs( T[1]*R[0][2] - T[0]*R[1][2] );
		if (t > ra + rb) return false;

    	/*no separating axis found, the two boxes overlap */

    	return true;
    }

    /** Replies if the specified rectangles intersect.
     * <p>
     * The extents are assumed to be positive or zero.
     * The lengths of the given arrays are assumed to be <code>2</code>.
     * <p>
     * This function uses the "separating axis theorem" which states that 
     * for any two OBBs (AABB is a special case of OBB)
     * that do not touch, a separating axis can be found.
     * <p>
     * This function uses an general intersection test between two OBR.
     * If the first box is expected to be an MBR, please use the
     * optimized algorithm given by
     * {@link #intersectsAlignedRectangleOrientedRectangle(Point2f, Point2f, Point2f, Vector2f[], float[])}.
     *
     * @param center1 is the center point of the first OBR.
     * @param axis1 are the unit vectors of the first OBR axis.
     * @param extent1 are the sizes of the first OBR.
     * @param center2 is the center point of the second OBR.
     * @param axis2 are the unit vectors of the second OBR axis.
     * @param extent2 are the sizes of the second OBR.
	 * @return <code>true</code> if intersecting, otherwise <code>false</code>
	 * @see "RTCD pages 102-105"
	 * @see <a href="http://www.jkh.me/files/tutorials/Separating%20Axis%20Theorem%20for%20Oriented%20Bounding%20Boxes.pdf">Intersection between two oriented boudning rectangles</a>
     */
    public static boolean intersectsOrientedRectangles(
    		Point2f center1, Vector2f[] axis1,  float[] extent1,
    		Point2f center2, Vector2f[] axis2,  float[] extent2) {
    	assert(axis1.length==2);
    	assert(axis2.length==2);
    	assert(extent1.length==2);
    	assert(extent2.length==2);
    	assert(extent1[0]>=0);
    	assert(extent1[1]>=0);
    	assert(extent2[0]>=0);
    	assert(extent2[1]>=0);

    	Vector2f T = new Vector2f();
    	T.sub(center2, center1);
    	
    	Vector2f[] scaledAxis1 = new Vector2f[2];
    	Vector2f[] scaledAxis2 = new Vector2f[2];
    	for(int i=0;i<2;i++){
    		scaledAxis1[i] = new Vector2f(axis1[i]);
    		scaledAxis1[i].scale(extent1[i]);
    		scaledAxis2[i] = new Vector2f(axis2[i]);
    		scaledAxis2[i].scale(extent2[i]);    		
    	} 
    	//Let A the first box and B the second one
    	//L = Ax
    	if (Math.abs(T.dot(axis1[0])) > extent1[0] + Math.abs(scaledAxis2[0].dot(axis1[0])) + Math.abs(scaledAxis2[1].dot(axis1[0])))
    		return false;
    	
    	//L = Ay
    	if (Math.abs(T.dot(axis1[1])) > extent1[1] + Math.abs(scaledAxis2[0].dot(axis1[1])) + Math.abs(scaledAxis2[1].dot(axis1[1])))
    		return false;
    	    	
    	//L=Bx
    	if (Math.abs(T.dot(axis2[0])) > extent2[0] + Math.abs(scaledAxis1[0].dot(axis2[0])) + Math.abs(scaledAxis1[1].dot(axis2[0])))
    		return false;
    	    	
    	//L=By
    	if (Math.abs(T.dot(axis2[1])) > extent2[1] + Math.abs(scaledAxis1[0].dot(axis2[1])) + Math.abs(scaledAxis1[1].dot(axis2[1])))
    		return false;
    	
		/*no separating axis found, the two boxes overlap */
    	return true;
    }

    /**
     * Classifies an axis-aligned box and an oriented box.
     * <p>
     * This function is assuming that <var>lx1</var> is lower
     * or equal to <var>ux1</var>, <var>ly1</var> is lower
     * or equal to <var>uy1</var>, and so on.
     * The extents are assumed to be positive or zero.
     * The lengths of the given arrays are assumed to be <code>3</code>.
     * <p>
     * This function uses the "separating axis theorem" which states that 
     * for any two OBBs (AABB is a special case of OBB)
     * that do not touch, a separating axis can be found.
     * <p>
     * This function uses an optimized algorithm for AABB as first parameter.
     * The general intersection type between two OBB is given by
     * {@link #classifiesOrientedBoxes(Point3f, Vector3f[], float[], Point3f, Vector3f[], float[])}
     *
     * @param lower coordinates of the lowest point of the first AABB box.
     * @param upper coordinates of the uppest point of the first AABB box.
     * @param center is the center point of the second oriented box.
     * @param axis are the unit vectors of the second oriented box axis.
     * @param extent are the sizes of the second oriented box.
	 * @return the value {@link IntersectionType#INSIDE} if the first box is inside
	 * the second box; {@link IntersectionType#OUTSIDE} if the first box is 
	 * outside the second box; {@link IntersectionType#ENCLOSING} if the 
	 * first box is enclosing the second box;
	 * {@link IntersectionType#SPANNING} otherwise.
	 * @see "RTCD pages 102-105"
	 * @see <a href="http://www.gamasutra.com/features/19991018/Gomez_5.htm">OBB collision detection on Gamasutra.com</a>
     */
    public static IntersectionType classifiesAlignedBoxOrientedBox(
    		Point3f lower, Point3f upper,
    		Point3f center, Vector3f[] axis,  float[] extent) {
    	assert(lower.getX()<=upper.getX());
    	assert(lower.getY()<=upper.getY());
    	assert(lower.getZ()<=upper.getZ());
    	assert(axis.length==3);
    	assert(extent.length==3);
    	assert(extent[0]>=0);
    	assert(extent[1]>=0);
    	assert(extent[2]>=0);
    	
    	Point3f aabbCenter = new Point3f(
				(upper.getX()+lower.getX())/2.,
				(upper.getY()+lower.getY())/2.,
				(upper.getZ()+lower.getZ())/2.);
    	
    	return classifiesOrientedBoxes(
    			aabbCenter,
    			_3D_XBB_AXIS,
    			new float[] {
    				upper.getX() - aabbCenter.getX(),
    				upper.getY() - aabbCenter.getY(),
    				upper.getZ() - aabbCenter.getZ()
    			},
    			center, axis, extent);
    }

    /**
     * Classifies an minimum bounding rectangle and an oriented rectangle.
     * <p>
     * This function is assuming that <var>lx1</var> is lower
     * or equal to <var>ux1</var>, and <var>ly1</var> is lower
     * or equal to <var>uy1</var>.
     * The extents are assumed to be positive or zero.
     * The lengths of the given arrays are assumed to be <code>2</code>.
     * <p>
     * This function uses the "separating axis theorem" which states that 
     * for any two OBBs (AABB is a special case of OBB)
     * that do not touch, a separating axis can be found.
     * <p>
     * This function uses an optimized algorithm for AABB as first parameter.
     * The general intersection type between two OBB is given by
     * {@link #classifiesOrientedRectangles(Point2f, Vector2f[], float[], Point2f, Vector2f[], float[])}
     *
     * @param lower coordinates of the lowest point of the first MBR.
     * @param upper coordinates of the uppest point of the first MBR.
     * @param center is the center point of the second oriented rectangle.
     * @param axis are the unit vectors of the second oriented rectangle axis.
     * @param extent are the sizes of the second oriented rectangle.
	 * @return the value {@link IntersectionType#INSIDE} if the first rectangle is inside
	 * the second rectangle; {@link IntersectionType#OUTSIDE} if the first rectangle is 
	 * outside the second rectangle; {@link IntersectionType#ENCLOSING} if the 
	 * first rectangle is enclosing the second rectangle;
	 * {@link IntersectionType#SPANNING} otherwise.
	 * @see "RTCD pages 102-105"
	 * @see <a href="http://www.gamasutra.com/features/19991018/Gomez_5.htm">OBB collision detection on Gamasutra.com</a>
     */
    public static IntersectionType classifiesAlignedRectangleOrientedRectangle(
    		Point2f lower, Point2f upper,
    		Point2f center, Vector2f[] axis,  float[] extent) {
    	assert(lower.getX()<=upper.getX());
    	assert(lower.getY()<=upper.getY());
    	assert(axis.length==2);
    	assert(extent.length==2);
    	assert(extent[0]>=0);
    	assert(extent[1]>=0);
    	
    	Point2f mbrCenter = new Point2f(
				(upper.getX()+lower.getX())/2.,
				(upper.getY()+lower.getY())/2.);
    	
    	return classifiesOrientedRectangles(
    			mbrCenter,
    			_2D_XBB_AXIS,
    			new float[] {
    				upper.getX() - mbrCenter.getX(),
    				upper.getY() - mbrCenter.getY()
    			},
    			center, axis, extent);
    }

    /** Replies if the specified boxes intersect.
     * <p>
     * This function is assuming that <var>lx1</var> is lower
     * or equal to <var>ux1</var>, <var>ly1</var> is lower
     * or equal to <var>uy1</var>, and so on.
     * The extents are assumed to be positive or zero.
     * The lengths of the given arrays are assumed to be <code>3</code>.
     * <p>
     * This function uses the "separating axis theorem" which states that 
     * for any two OBBs (AABB is a special case of OBB)
     * that do not touch, a separating axis can be found.
     * <p>
     * This function uses an optimized algorithm for AABB as first parameter.
     * The general intersection type between two OBB is given by
     * {@link #intersectsOrientedBoxes(Point3f, Vector3f[], float[], Point3f, Vector3f[], float[])}
     *
     * @param lower coordinates of the lowest point of the first AABB box.
     * @param upper coordinates of the uppest point of the first AABB box.
     * @param center is the center point of the second oriented box.
     * @param axis are the unit vectors of the second oriented box axis.
     * @param extent are the sizes of the second oriented box.
	 * @return <code>true</code> if intersecting, otherwise <code>false</code>
	 * @see "RTCD pages 102-105"
	 * @see <a href="http://www.gamasutra.com/features/19991018/Gomez_5.htm">OBB collision detection on Gamasutra.com</a>
     */
    public static boolean intersectsAlignedBoxOrientedBox(
    		Point3f lower, Point3f upper,
    		Point3f center, Vector3f[] axis,  float[] extent) {
    	assert(lower.getX()<=upper.getX());
    	assert(lower.getY()<=upper.getY());
    	assert(lower.getZ()<=upper.getZ());
    	assert(axis.length==3);
    	assert(extent.length==3);
    	assert(extent[0]>=0);
    	assert(extent[1]>=0);
    	assert(extent[2]>=0);
    	
    	Point3f aabbCenter = new Point3f(
				(upper.getX()+lower.getX())/2.,
				(upper.getY()+lower.getY())/2.,
				(upper.getZ()+lower.getZ())/2.);
    	
    	return intersectsOrientedBoxes(
    			aabbCenter,
    			_3D_XBB_AXIS,
    			new float[] {
    				upper.getX() - aabbCenter.getX(),
    				upper.getY() - aabbCenter.getY(),
    				upper.getZ() - aabbCenter.getZ()
    			},
    			center, axis, extent);
    }

    /** Replies if the specified rectangles intersect.
     * <p>
     * This function is assuming that <var>lx1</var> is lower
     * or equal to <var>ux1</var>, and <var>ly1</var> is lower
     * or equal to <var>uy1</var>.
     * The extents are assumed to be positive or zero.
     * The lengths of the given arrays are assumed to be <code>2</code>.
     * <p>
     * This function uses the "separating axis theorem" which states that 
     * for any two OBBs (AABB is a special case of OBB)
     * that do not touch, a separating axis can be found.
     * <p>
     * This function uses an optimized algorithm for AABB as first parameter.
     * The general intersection type between two OBB is given by
     * {@link #intersectsOrientedBoxes(Point3f, Vector3f[], float[], Point3f, Vector3f[], float[])}
     *
     * @param lower coordinates of the lowest point of the first MBR.
     * @param upper coordinates of the uppest point of the first MBR.
     * @param center is the center point of the second OBR.
     * @param axis are the unit vectors of the second OBR axis.
     * @param extent are the sizes of the second OBR.
	 * @return <code>true</code> if intersecting, otherwise <code>false</code>
	 * @see "RTCD pages 102-105"
	 * @see <a href="http://www.gamasutra.com/features/19991018/Gomez_5.htm">OBB collision detection on Gamasutra.com</a>
     */
    public static boolean intersectsAlignedRectangleOrientedRectangle(
    		Point2f lower, Point2f upper,
    		Point2f center, Vector2f[] axis,  float[] extent) {
    	assert(lower.getX()<=upper.getX());
    	assert(lower.getY()<=upper.getY());
    	assert(axis.length==2);
    	assert(extent.length==2);
    	assert(extent[0]>=0);
    	assert(extent[1]>=0);
    	
    	Point2f mbrCenter = new Point2f(
				(upper.getX()+lower.getX())/2.,
				(upper.getY()+lower.getY())/2.);
    	
    	return intersectsOrientedRectangles(
    			mbrCenter,
    			_2D_XBB_AXIS,
    			new float[] {
    				upper.getX() - mbrCenter.getX(),
    				upper.getY() - mbrCenter.getY()
    			},
    			center, axis, extent);
    }

    

	/**
	 * Replies if the specified sphere intersects the specified capsule.
	 * @param sphereCenter - center of the sphere
	 * @param sphereRadius - radius of the sphere
	 * @param capsuleA - Medial line segment start point of the capsule
	 * @param capsuleB - Medial line segment end point of the capsule
	 * @param capsuleRadius - radius of the capsule
	 * @return <code>true</code> if intersecting, otherwise <code>false</code>
	 */
	public static boolean intersectsSphereCapsule(Point3f sphereCenter, float sphereRadius, Point3f capsuleA, Point3f capsuleB, float capsuleRadius) {
		// Compute (squared) distance between sphere center and capsule line segment

		float dist2 = MathUtil.distanceSquaredPointSegment(sphereCenter.getX(), sphereCenter.getY(), sphereCenter.getZ(), capsuleA.getX(), capsuleA.getY(), capsuleA.getZ(), capsuleB.getX(), capsuleB.getY(), capsuleB.getZ());

		// If (squared) distance smaller than (squared) sum of radii, they collide
		float radius = sphereRadius + capsuleRadius;
		return dist2 < radius * radius;
	}
	
	/**
	 * Replies if the specified sphere intersects the specified capsule.
	 * @param sphereCenter - center of the sphere
	 * @param sphereRadius - radius of the sphere
	 * @param capsuleA - Medial line segment start point of the capsule
	 * @param capsuleB - Medial line segment end point of the capsule
	 * @param capsuleRadius - radius of the capsule
	 * @return the value {@link IntersectionType#INSIDE} if the capsule is inside
	 * the sphere; {@link IntersectionType#OUTSIDE} if the capsule is 
	 * outside the sphere; {@link IntersectionType#ENCLOSING} if the 
	 * capsule is enclosing the sphere;
	 */
	public static IntersectionType classifySphereCapsule(Point3f sphereCenter, float sphereRadius, Point3f capsuleA, Point3f capsuleB, float capsuleRadius) {
		// Computedistance between sphere center and capsule line segment
		
		float dist2 = MathUtil.distancePointSegment(sphereCenter.getX(), sphereCenter.getY(), sphereCenter.getZ(), capsuleA.getX(), capsuleA.getY(), capsuleA.getZ(), capsuleB.getX(), capsuleB.getY(), capsuleB.getZ());
		
		// If distance smaller than sum of radii, they collide
		float fullRadius = sphereRadius + capsuleRadius;
		
		if (dist2 < fullRadius) {
			Vector3f ab = new Vector3f(capsuleB);
			ab.sub(capsuleA);
			
			Vector3f acenter = new Vector3f(capsuleA);
			acenter.sub(sphereCenter);
			Vector3f bcenter = new Vector3f(capsuleB);
			bcenter.sub(sphereCenter);
			if((dist2+sphereRadius) <= capsuleRadius) {
				return IntersectionType.ENCLOSING;
			}
			else if (sphereRadius*sphereRadius >= (Math.max(acenter.lengthSquared(),bcenter.lengthSquared())+capsuleRadius)) {
				return IntersectionType.INSIDE;
			}
			return IntersectionType.SPANNING;
		}
		return IntersectionType.OUTSIDE;
	}
	
	/**
	 * Replies if the specified capsules intersect
	 * @param capsule1A - Medial line segment start point of the first capsule
	 * @param capsule1B - Medial line segment end point of the first capsule
	 * @param capsule1Radius - radius of the first capsule
	 * @param capsule2A - Medial line segment start point of the second capsule
	 * @param capsule2B - Medial line segment end point of the second capsule
	 * @param capsule2Radius - radius of the second capsule
	 * @return <code>true</code> if intersecting, otherwise <code>false</code>
	 */
	public static boolean intersectsCapsuleCapsule(Point3f capsule1A, Point3f capsule1B, float capsule1Radius, Point3f capsule2A, Point3f capsule2B, float capsule2Radius) {
		// Compute (squared) distance between the inner structures of the capsules
		OutputParameter<Float> s = new OutputParameter<>();
		OutputParameter<Float> t = new OutputParameter<>();
		Point3f c1 = new Point3f();
		Point3f c2 = new Point3f();
		
		float dist2 = MathUtil.closestPointSegmentSegment(capsule1A, capsule1B, capsule2A, capsule2B,s,t,c1,c2);

		// If (squared) distance smaller than (squared) sum of radii, they collide
		float radius = capsule1Radius + capsule2Radius;
		return dist2 <= radius * radius;
	} 
	
	/**
	 * Compute intersection between an OBB and a capsule
	 * @param center is the center point of the oriented box.
     * @param axis are the unit vectors of the oriented box axis.
     * @param extent are the sizes of the oriented box.
	 * @param capsule1A - capsule medial line segment start point
	 * @param capsule1B - capsule medial line segment end point
	 * @param capsule1Radius - capsule radius
	 * @return <code>true</code> if intersecting, otherwise <code>false</code>
	 */
	public static boolean intersectsOrientedBoxCapsule(Point3f center, Vector3f[] axis,  float[] extent,Point3f capsule1A, Point3f capsule1B, float capsule1Radius) {
		Point3f closestFromA = new Point3f();
		Point3f closestFromB = new Point3f();
		IntersectionUtil.computeClosestFarestOBBPoints(center, axis, extent, capsule1A, closestFromA, null);
		IntersectionUtil.computeClosestFarestOBBPoints(center, axis, extent, capsule1B, closestFromB, null);
		
		float distance = MathUtil.distanceSegmentSegment(capsule1A.getX(), capsule1A.getY(), capsule1B.getX(), capsule1B.getY(), closestFromA.getX(), closestFromA.getY(), closestFromB.getX(), closestFromB.getY());
		
		return (distance <= capsule1Radius);
	}
	
	/**
	 * Compute intersection between an OBB and a capsule
	 * @param center is the center point of the oriented box.
     * @param axis are the unit vectors of the oriented box axis.
     * @param extent are the sizes of the oriented box.
	 * @param capsule1A - capsule medial line segment start point
	 * @param capsule1B - capsule medial line segment end point
	 * @param capsule1Radius - capsule radius
	 * @return the value {@link IntersectionType#INSIDE} if the capsule is inside
	 * the box; {@link IntersectionType#OUTSIDE} if the capsule is 
	 * outside the box; {@link IntersectionType#ENCLOSING} if the 
	 * capsule is enclosing the box;
	 */
	public static IntersectionType classifyOrientedBoxCapsule(Point3f center, Vector3f[] axis,  float[] extent,Point3f capsule1A, Point3f capsule1B, float capsule1Radius) {
		Point3f closestFromA = new Point3f();
		Point3f closestFromB = new Point3f();
		Point3f farestFromA = new Point3f();
		Point3f farestFromB = new Point3f();
		IntersectionUtil.computeClosestFarestOBBPoints(center, axis, extent, capsule1A, closestFromA, farestFromA);
		IntersectionUtil.computeClosestFarestOBBPoints(center, axis, extent, capsule1B, closestFromB, farestFromB);
		
		float distanceToNearest = MathUtil.distanceSegmentSegment(capsule1A.getX(), capsule1A.getY(), capsule1B.getX(), capsule1B.getY(), closestFromA.getX(), closestFromA.getY(), closestFromB.getX(), closestFromB.getY());
		
		if (distanceToNearest > capsule1Radius) {
			return IntersectionType.OUTSIDE;
		}
		
		float distanceToFarest = MathUtil.distanceSegmentSegment(capsule1A.getX(), capsule1A.getY(), capsule1B.getX(), capsule1B.getY(), farestFromA.getX(), farestFromA.getY(), farestFromB.getX(), farestFromB.getY());
		if(distanceToFarest <= capsule1Radius) {
			return IntersectionType.ENCLOSING;
		}
		
		IntersectionType onSphereA = IntersectionUtil.classifiesSolidSphereOrientedBox(capsule1A, capsule1Radius, center, axis, extent);
		IntersectionType onSphereB = IntersectionUtil.classifiesSolidSphereOrientedBox(capsule1B, capsule1Radius, center, axis, extent);

		if(onSphereA.equals(IntersectionType.INSIDE)&&onSphereB.equals(IntersectionType.INSIDE)) {
			return IntersectionType.INSIDE;

		}
		else if(onSphereA.equals(IntersectionType.INSIDE) || onSphereB.equals(IntersectionType.INSIDE)) {
			return IntersectionType.SPANNING;					
		}
		else if(onSphereA.equals(IntersectionType.ENCLOSING) || onSphereB.equals(IntersectionType.ENCLOSING)) {
			return IntersectionType.ENCLOSING;					
		}
		
		return IntersectionType.SPANNING;
	}
	
	/**
	 * Compute intersection between a point and a capsule
	 * @param p - the point to test
	 * @param capsule1A - capsule medial line segment start point
	 * @param capsule1B - capsule medial line segment end point
	 * @param capsule1Radius - capsule radius
	 * @return <code>true</code> if intersecting, otherwise <code>false</code>
	 */
	public static boolean intersectPointCapsule(Point3f p, Point3f capsule1A, Point3f capsule1B, float capsule1Radius) {
		float distPointToCapsuleSegment = MathUtil.distancePointSegment(p.getX(),p.getY(),p.getZ(),capsule1A.getX(),capsule1A.getY(),capsule1A.getZ(),capsule1B.getX(),capsule1B.getY(),capsule1B.getZ());
		return (distPointToCapsuleSegment <= capsule1Radius);
	}
	
	/**
	 * Compute intersection between a point and a capsule
	 * @param p - the point to test
	 * @param capsule1A - capsule medial line segment start point
	 * @param capsule1B - capsule medial line segment end point
	 * @param capsule1Radius - capsule radius
	 * @return the value {@link IntersectionType#INSIDE} if the point is inside
	 * the capsule; {@link IntersectionType#OUTSIDE} if the point is 
	 * outside the capsule; {@link IntersectionType#ENCLOSING} is not defined here;
	 */
	public static IntersectionType classifyPointCapsule(Point3f p, Point3f capsule1A, Point3f capsule1B, float capsule1Radius) {
		float distPointToCapsuleSegment = MathUtil.distancePointSegment(p.getX(),p.getY(),p.getZ(),capsule1A.getX(),capsule1A.getY(),capsule1A.getZ(),capsule1B.getX(),capsule1B.getY(),capsule1B.getZ());
		if (distPointToCapsuleSegment > capsule1Radius) {
			return IntersectionType.OUTSIDE;
		} else if (distPointToCapsuleSegment == capsule1Radius) {
			return IntersectionType.SPANNING;
		}else {
		
			return IntersectionType.INSIDE;
		}
	}
    
}
