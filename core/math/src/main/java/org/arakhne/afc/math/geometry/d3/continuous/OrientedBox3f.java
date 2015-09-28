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

package org.arakhne.afc.math.geometry.d3.continuous;

import java.util.Arrays;

import org.arakhne.afc.math.geometry.coordinatesystem.CoordinateSystem3D;
import org.arakhne.afc.math.geometry.d3.FunctionalVector3D;
import org.arakhne.afc.math.geometry.d3.Point3D;
import org.arakhne.afc.math.geometry.d3.Vector3D;
import org.arakhne.afc.math.matrix.Matrix3f;

/**
 * Definition of a fixed Oriented Bounding Box (OBB).
 * <p>
 * Algo inspired from Mathematics for 3D Game Programming and Computer 
 * Graphics (MGPCG) and from 3D Game Engine Design (GED) and from Real Time Collision Detection (RTCD).
 * <p>
 * Rotations are not managed yet.
 * 
 * @author $Author: sgalland$
 * @author $Author: ngaud$
 * @author $Author: cbohrhauer$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @see <a href="http://www.terathon.com/books/mathgames2.html">Mathematics for 3D Game Programming &amp; Computer Graphics</a>
 */
public class OrientedBox3f extends AbstractShape3F<OrientedBox3f> {

	private static final long serialVersionUID = -3095583019793802300L;

	/**
	 * Compute intersection between an OBB and a capsule.
	 * 
	 * @param centerx is the center point of the oriented box.
	 * @param centery is the center point of the oriented box.
	 * @param centerz is the center point of the oriented box.
	 * @param axis1x are the unit vectors of the oriented box axis.
	 * @param axis1y are the unit vectors of the oriented box axis.
	 * @param axis1z are the unit vectors of the oriented box axis.
	 * @param axis2x are the unit vectors of the oriented box axis.
	 * @param axis2y are the unit vectors of the oriented box axis.
	 * @param axis2z are the unit vectors of the oriented box axis.
	 * @param axis3x are the unit vectors of the oriented box axis.
	 * @param axis3y are the unit vectors of the oriented box axis.
	 * @param axis3z are the unit vectors of the oriented box axis.
	 * @param extentAxis1 are the sizes of the oriented box.
	 * @param extentAxis2 are the sizes of the oriented box.
	 * @param extentAxis3 are the sizes of the oriented box.
	 * @param capsule1Ax x coordinate of the first point of the capsule medial line.
	 * @param capsule1Ay y coordinate of the first point of the capsule medial line.
	 * @param capsule1Az z coordinate of the first point of the capsule medial line.
	 * @param capsule1Bx x coordinate of the second point of the capsule medial line.
	 * @param capsule1By y coordinate of the second point of the capsule medial line.
	 * @param capsule1Bz z coordinate of the second point of the capsule medial line.
	 * @param capsule1Radius - capsule radius
	 * @return <code>true</code> if intersecting, otherwise <code>false</code>
	 */
	public static boolean intersectsOrientedBoxCapsule(
			double centerx,double  centery,double  centerz,
			double axis1x, double axis1y, double axis1z,
			double axis2x, double axis2y, double axis2z,
			double axis3x, double axis3y, double axis3z,
			double extentAxis1, double extentAxis2, double extentAxis3,
			double capsule1Ax, double capsule1Ay, double capsule1Az, double capsule1Bx, double capsule1By, double capsule1Bz, double capsule1Radius) {

		Point3f closestFromA = new Point3f();
		Point3f closestFromB = new Point3f();

		computeClosestFarestOBBPoints(
				capsule1Ax, capsule1Ay, capsule1Az,
				centerx, centery, centerz, 
				axis1x, axis1y, axis1z,	
				axis2x, axis2y, axis2z,
				axis3x, axis3y, axis3z,
				extentAxis1, extentAxis2, extentAxis3,
				closestFromA, null);
		computeClosestFarestOBBPoints(
				capsule1Bx, capsule1By, capsule1Bz,
				centerx, centery, centerz,
				axis1x, axis1y, axis1z,
				axis2x, axis2y, axis2z,
				axis3x, axis3y, axis3z,
				extentAxis1, extentAxis2, extentAxis3,
				closestFromB,null);

		double distance = AbstractSegment3F.distanceSquaredSegmentSegment(
				capsule1Ax, capsule1Ay, capsule1Az, 
				capsule1Bx, capsule1By, capsule1Bz, 
				closestFromA.getX(), closestFromA.getY(), closestFromA.getZ(),
				closestFromB.getX(), closestFromB.getY(), closestFromB.getZ());

		return (distance <= (capsule1Radius * capsule1Radius));
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
	 * {@link #intersectsOrientedBoxOrientedBox(double, double, double, double, double, double, double, double, double, double, double, double, double, double, double, double, double, double, double, double, double, double, double, double, double, double, double, double, double, double)}
	 *
	 * @param centerx is the center point of the oriented box.
	 * @param centery is the center point of the oriented box.
	 * @param centerz is the center point of the oriented box.
	 * @param axis1x are the unit vectors of the oriented box axis.
	 * @param axis1y are the unit vectors of the oriented box axis.
	 * @param axis1z are the unit vectors of the oriented box axis.
	 * @param axis2x are the unit vectors of the oriented box axis.
	 * @param axis2y are the unit vectors of the oriented box axis.
	 * @param axis2z are the unit vectors of the oriented box axis.
	 * @param axis3x are the unit vectors of the oriented box axis.
	 * @param axis3y are the unit vectors of the oriented box axis.
	 * @param axis3z are the unit vectors of the oriented box axis.
	 * @param extentAxis1 are the sizes of the oriented box.
	 * @param extentAxis2 are the sizes of the oriented box.
	 * @param extentAxis3 are the sizes of the oriented box.
	 * @param lowerx coordinates of the lowest point of the first AABB box.
	 * @param lowery coordinates of the lowest point of the first AABB box.
	 * @param lowerz coordinates of the lowest point of the first AABB box.
	 * @param upperx coordinates of the uppermost point of the first AABB box.
	 * @param uppery coordinates of the uppermost point of the first AABB box.
	 * @param upperz coordinates of the uppermost point of the first AABB box.
	 * @return <code>true</code> if intersecting, otherwise <code>false</code>
	 * @see "RTCD pages 102-105"
	 * @see <a href="http://www.gamasutra.com/features/19991018/Gomez_5.htm">OBB collision detection on Gamasutra.com</a>
	 */
	public static boolean intersectsOrientedBoxAlignedBox(
			double centerx,double  centery,double  centerz,
			double axis1x, double axis1y, double axis1z,
			double axis2x, double axis2y, double axis2z,
			double axis3x, double axis3y, double axis3z,
			double extentAxis1, double extentAxis2, double extentAxis3,
			double lowerx,double  lowery,double  lowerz,
			double upperx,double  uppery,double  upperz) {
		assert(lowerx<=upperx);
		assert(lowery<=uppery);
		assert(lowerz<=upperz);
		assert(extentAxis1>=0);
		assert(extentAxis2>=0);
		assert(extentAxis3>=0);

		double aabbCenterx,aabbCentery,aabbCenterz;
		aabbCenterx = (upperx+lowerx)/2.f;
		aabbCentery = (uppery+lowery)/2.f;
		aabbCenterz = (upperz+lowerz)/2.f;


		return intersectsOrientedBoxOrientedBox(
				aabbCenterx, aabbCentery, aabbCenterz,
				1,0,0,	//Axis 1
				0,1,0,	//Axis 2
				0,0,1,	//Axis 3
				upperx - aabbCenterx, uppery - aabbCentery, upperz - aabbCenterz,
				centerx, centery, centerz,
				axis1x, axis1y, axis1z,
				axis2x, axis2y, axis2z,
				axis3x, axis3y, axis3z,
				extentAxis1, extentAxis2, extentAxis3);
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
	 * {@link #intersectsOrientedBoxAlignedBox(double, double, double, double, double, double, double, double, double, double, double, double, double, double, double, double, double, double, double, double, double)}.
	 *
	 * @param center1x is the center point of the first oriented box.
	 * @param center1y is the center point of the first oriented box.
	 * @param center1z is the center point of the first oriented box.
	 * @param box1Axis1x are the first unit vectors of the first oriented box.
	 * @param box1Axis1y are the first unit vectors of the first oriented box.
	 * @param box1Axis1z are the first unit vectors of the first oriented box.
	 * @param box1Axis2x are the second unit vectors of the first oriented box.
	 * @param box1Axis2y are the second unit vectors of the first oriented box.
	 * @param box1Axis2z are the second unit vectors of the first oriented box.
	 * @param box1Axis3x are the third unit vectors of the first oriented box.
	 * @param box1Axis3y are the third unit vectors of the first oriented box.
	 * @param box1Axis3z are the third unit vectors of the first oriented box.
	 * @param box1ExtentAxis1 is the size of the first axis of the first oriented box.
	 * @param box1ExtentAxis2 is the size of the first axis of the first oriented box.
	 * @param box1ExtentAxis3 is the size of the first axis of the first oriented box.
	 * @param center2x is the center point of the second oriented box.
	 * @param center2y is the center point of the second oriented box.
	 * @param center2z is the center point of the second oriented box.
	 * @param box2Axis1x are the first unit vectors of the second oriented box.
	 * @param box2Axis1y are the first unit vectors of the second oriented box.
	 * @param box2Axis1z are the first unit vectors of the second oriented box.
	 * @param box2Axis2x are the second unit vectors of the second oriented box.
	 * @param box2Axis2y are the second unit vectors of the second oriented box.
	 * @param box2Axis2z are the second unit vectors of the second oriented box.
	 * @param box2Axis3x are the third unit vectors of the second oriented box.
	 * @param box2Axis3y are the third unit vectors of the second oriented box.
	 * @param box2Axis3z are the third unit vectors of the second oriented box.
	 * @param box2ExtentAxis1 is the size of the first axis of the second oriented box.
	 * @param box2ExtentAxis2 is the size of the first axis of the second oriented box.
	 * @param box2ExtentAxis3 is the size of the first axis of the second oriented box.
	 * @return <code>true</code> if intersecting, otherwise <code>false</code>
	 * @see "RTCD pages 102-105"
	 * @see <a href="http://www.gamasutra.com/features/19991018/Gomez_5.htm">OBB collision detection on Gamasutra.com</a>
	 */
	public static boolean intersectsOrientedBoxOrientedBox(
			double center1x, double center1y, double center1z,
			double box1Axis1x, double box1Axis1y, double box1Axis1z,
			double box1Axis2x, double box1Axis2y, double box1Axis2z,
			double box1Axis3x, double box1Axis3y, double box1Axis3z,
			double box1ExtentAxis1, double box1ExtentAxis2, double box1ExtentAxis3,
			double center2x, double center2y, double center2z,
			double box2Axis1x, double box2Axis1y, double box2Axis1z,
			double box2Axis2x, double box2Axis2y, double box2Axis2z,
			double box2Axis3x, double box2Axis3y, double box2Axis3z,
			double box2ExtentAxis1, double box2ExtentAxis2, double box2ExtentAxis3){

		//translation, in parent frame
		double vx, vy, vz;
		vx = center2x - center1x;
		vy = center2y - center1y;
		vz = center2z - center1z;

		//translation, in A's frame
		double tx,ty,tz;
		tx = FunctionalVector3D.dotProduct(vx, vy, vz, box1Axis1x, box1Axis1y, box1Axis1z);
		ty = FunctionalVector3D.dotProduct(vx, vy, vz, box1Axis2x, box1Axis2y, box1Axis2z);
		tz = FunctionalVector3D.dotProduct(vx, vy, vz, box1Axis3x, box1Axis3y, box1Axis3z);

		//B's basis with respect to A's local frame
		double R_1_1, R_1_2, R_1_3,
		R_2_1, R_2_2, R_2_3,
		R_3_1, R_3_2, R_3_3,

		absR_1_1, absR_1_2, absR_1_3,
		absR_2_1, absR_2_2, absR_2_3,
		absR_3_1, absR_3_2, absR_3_3;

		R_1_1 = FunctionalVector3D.dotProduct(box1Axis1x, box1Axis1y,box1Axis1z,box2Axis1x, box2Axis1y,box2Axis1z); absR_1_1 = (R_1_1 < 0) ? -R_1_1 : R_1_1;
		R_1_2 = FunctionalVector3D.dotProduct(box1Axis1x, box1Axis1y,box1Axis1z,box2Axis2x, box2Axis2y,box2Axis2z); absR_1_2 = (R_1_2 < 0) ? -R_1_2 : R_1_2;
		R_1_3 = FunctionalVector3D.dotProduct(box1Axis1x, box1Axis1y,box1Axis1z,box2Axis3x, box2Axis3y,box2Axis3z); absR_1_3 = (R_1_3 < 0) ? -R_1_3 : R_1_3;
		R_2_1 = FunctionalVector3D.dotProduct(box1Axis2x, box1Axis2y,box1Axis2z,box2Axis1x, box2Axis1y,box2Axis1z); absR_2_1 = (R_2_1 < 0) ? -R_2_1 : R_2_1;
		R_2_2 = FunctionalVector3D.dotProduct(box1Axis2x, box1Axis2y,box1Axis2z,box2Axis2x, box2Axis2y,box2Axis2z); absR_2_2 = (R_2_2 < 0) ? -R_2_2 : R_2_2;
		R_2_3 = FunctionalVector3D.dotProduct(box1Axis2x, box1Axis2y,box1Axis2z,box2Axis3x, box2Axis3y,box2Axis3z); absR_2_3 = (R_2_3 < 0) ? -R_2_3 : R_2_3;
		R_3_1 = FunctionalVector3D.dotProduct(box1Axis3x, box1Axis3y,box1Axis3z,box2Axis1x, box2Axis1y,box2Axis1z); absR_3_1 = (R_3_1 < 0) ? -R_3_1 : R_3_1;
		R_3_2 = FunctionalVector3D.dotProduct(box1Axis3x, box1Axis3y,box1Axis3z,box2Axis2x, box2Axis2y,box2Axis2z); absR_3_2 = (R_3_2 < 0) ? -R_3_2 : R_3_2;
		R_3_3 = FunctionalVector3D.dotProduct(box1Axis3x, box1Axis3y,box1Axis3z,box2Axis3x, box2Axis3y,box2Axis3z); absR_3_3 = (R_3_3 < 0) ? -R_3_3 : R_3_3;

		// ALGORITHM: Use the separating axis test for all 15 potential
		// separating axes. If a separating axis could not be found, the two
		// boxes overlap.
		double ra, rb, t;

		ra = box1ExtentAxis1;
		rb = box2ExtentAxis1*absR_1_1+ box2ExtentAxis2*absR_1_2 + box2ExtentAxis3*absR_1_3;
		t = Math.abs(tx);
		if (t > ra + rb) return false;

		ra = box1ExtentAxis2;
		rb = box2ExtentAxis1*absR_2_1+ box2ExtentAxis2*absR_2_2 + box2ExtentAxis3*absR_3_3;
		t = Math.abs(ty);
		if (t > ra + rb) return false;

		ra = box1ExtentAxis3;
		rb = box2ExtentAxis1*absR_3_1+ box2ExtentAxis2*absR_3_2 + box2ExtentAxis3*absR_3_3;
		t = Math.abs(tz);
		if (t > ra + rb) return false;

		//B's basis vectors
		ra = box1ExtentAxis1*absR_1_1+ box1ExtentAxis2*absR_2_1 + box1ExtentAxis3*absR_3_1;
		rb = box2ExtentAxis1;
		t =	Math.abs( tx*R_1_1 + ty*R_2_1 + tz*R_3_1 );
		if (t > ra + rb) return false;

		ra = box1ExtentAxis1*absR_1_2+ box1ExtentAxis2*absR_2_2 + box1ExtentAxis3*absR_3_2;
		rb = box2ExtentAxis2;
		t =	Math.abs( tx*R_1_2 + ty*R_2_2 + tz*R_3_2 );
		if (t > ra + rb) return false;

		ra = box1ExtentAxis1*absR_1_3+ box1ExtentAxis2*absR_2_3 + box1ExtentAxis3*absR_3_3;
		rb = box2ExtentAxis3;
		t =	Math.abs( tx*R_1_3 + ty*R_2_3 + tz*R_3_3 );
		if (t > ra + rb) return false;

		//9 cross products

		//L = A0 x B0
		ra = box1ExtentAxis1*absR_3_1 + box1ExtentAxis3*absR_2_1;
		rb = box1ExtentAxis3*absR_1_3 + box1ExtentAxis3*absR_1_2;
		t = Math.abs( tz*R_2_1 - ty*R_3_1 );
		if (t > ra + rb) return false;


		ra = box1ExtentAxis2*absR_3_1 + box1ExtentAxis3*absR_2_1;
		rb = box1ExtentAxis3*absR_1_3 + box1ExtentAxis3*absR_1_2;
		t = Math.abs( tz*R_2_1 - ty*R_3_1 );
		if (t > ra + rb) return false;

		//L = A0 x B1
		ra = box1ExtentAxis2*absR_3_2 + box1ExtentAxis3*absR_2_2;
		rb = box1ExtentAxis3*absR_1_3 + box1ExtentAxis3*absR_1_1;
		t = Math.abs( tz*R_2_2 - ty*R_3_2 );
		if (t > ra + rb) return false;

		//L = A0 x B2
		ra = box1ExtentAxis2*absR_3_3 + box1ExtentAxis3*absR_2_3;
		rb = box1ExtentAxis3*absR_1_2 + box1ExtentAxis3*absR_1_1;
		t = Math.abs( tz*R_2_3 - ty*R_3_3 );
		if (t > ra + rb) return false;

		//L = A1 x B0
		ra = box1ExtentAxis1*absR_3_1 + box1ExtentAxis3*absR_1_1;
		rb = box1ExtentAxis3*absR_2_3 + box1ExtentAxis3*absR_2_2;
		t = Math.abs( tx*R_3_1 - tz*R_1_1 );
		if (t > ra + rb) return false;

		//L = A1 x B1
		ra = box1ExtentAxis1*absR_3_2 + box1ExtentAxis3*absR_1_2;
		rb = box1ExtentAxis3*absR_2_3 + box1ExtentAxis3*absR_2_1;
		t = Math.abs( tx*R_3_2 - tz*R_1_2 );
		if (t > ra + rb) return false;

		//L = A1 x B2
		ra = box1ExtentAxis1*absR_3_3 + box1ExtentAxis3*absR_1_3;
		rb = box1ExtentAxis3*absR_2_2 + box1ExtentAxis3*absR_2_1;
		t = Math.abs( tx*R_3_3 - tz*R_1_3 );
		if (t > ra + rb) return false;

		//L = A2 x B0
		ra = box1ExtentAxis1*absR_2_1 + box1ExtentAxis2*absR_1_1;
		rb = box1ExtentAxis3*absR_3_3 + box1ExtentAxis3*absR_3_2;
		t = Math.abs( ty*R_1_1 - tx*R_2_1 );
		if (t > ra + rb) return false;

		//L = A2 x B1
		ra = box1ExtentAxis1*absR_2_2 + box1ExtentAxis2*absR_1_2;
		rb = box1ExtentAxis3*absR_3_3 + box1ExtentAxis3*absR_3_1;
		t = Math.abs( ty*R_1_2 - tx*R_2_2 );
		if (t > ra + rb) return false;

		//L = A2 x B2
		ra = box1ExtentAxis1*absR_2_3 + box1ExtentAxis2*absR_1_3;
		rb = box1ExtentAxis3*absR_3_2 + box1ExtentAxis3*absR_3_1;
		t = Math.abs( ty*R_1_3 - tx*R_2_3 );
		if (t > ra + rb) return false;

		/*no separating axis found, the two boxes overlap */
		return true;

	}

	/** Replies if the given point is inside this shape.
	 * 
	 * @param cx x coordinate of the box center.
	 * @param cy y coordinate of the box center.
	 * @param cz z coordinate of the box center.
	 * @param axis1x x coordinate of the first axis of the box.
	 * @param axis1y y coordinate of the first axis of the box.
	 * @param axis1z z coordinate of the first axis of the box.
	 * @param axis2x x coordinate of the second axis of the box.
	 * @param axis2y y coordinate of the secons axis of the box.
	 * @param axis2z z coordinate of the second axis of the box.
	 * @param axis3x x coordinate of the second axis of the box.
	 * @param axis3y y coordinate of the secons axis of the box.
	 * @param axis3z z coordinate of the second axis of the box.
	 * @param axis1Extent extent of the first axis.
	 * @param axis2Extent extent of the second axis.
	 * @param axis3Extent extent of the third axis.
	 * @param px x coordinate of the point.
	 * @param py y coordinate of the point.
	 * @param pz z coordinate of the point.
	 * @return <code>true</code> if the given point is inside this
	 * shape, otherwise <code>false</code>.
	 */
	public static boolean containsOrientedBoxPoint(
			double cx, double cy, double cz,
			double axis1x, double axis1y, double axis1z,
			double axis2x, double axis2y, double axis2z,
			double axis3x, double axis3y, double axis3z,
			double axis1Extent, double axis2Extent, double axis3Extent,
			double px, double py, double pz) {
		double dx = px - cx;
		double dy = py - cy;
		double dz = pz - cz;

		// For each OBB axis...
		double dist;

		// ...project d onto that axis to get the distance along the axis of d from the box center
		dist = FunctionalVector3D.dotProduct(dx, dy, dz, axis1x, axis1y, axis1z);
		// If distance farther than the box extents, return OUTSIDE
		if (Math.abs(dist) > axis1Extent) {
			return false;
		}

		// ...project d onto that axis to get the distance along the axis of d from the box center
		dist = FunctionalVector3D.dotProduct(dx, dy, dz, axis2x, axis2y, axis2z);
		// If distance farther than the box extents, return OUTSIDE
		if (Math.abs(dist) > axis2Extent) {
			return false;
		}

		// ...project d onto that axis to get the distance along the axis of d from the box center
		dist = FunctionalVector3D.dotProduct(dx, dy, dz, axis3x, axis3y, axis3z);
		// If distance farther than the box extents, return OUTSIDE
		if (Math.abs(dist) > axis3Extent) {
			return false;
		}

		return true;
	}

	/**
	 * Given a point p, this function computes the point q1 on (or in) 
	 * this OBB, closest to p and the point q2 on farest to p.
	 * 
	 * @param cx x coordinate of the box center.
	 * @param cy y coordinate of the box center.
	 * @param cz z coordinate of the box center.
	 * @param axis1x x coordinate of the first axis of the box.
	 * @param axis1y y coordinate of the first axis of the box.
	 * @param axis1z z coordinate of the first axis of the box.
	 * @param axis2x x coordinate of the second axis of the box.
	 * @param axis2y y coordinate of the secons axis of the box.
	 * @param axis2z z coordinate of the second axis of the box.
	 * @param axis3x x coordinate of the second axis of the box.
	 * @param axis3y y coordinate of the secons axis of the box.
	 * @param axis3z z coordinate of the second axis of the box.
	 * @param axis1Extent extent of the first axis.
	 * @param axis2Extent extent of the second axis.
	 * @param axis3Extent extent of the third axis.
	 * @param x x coordinate of the point.
	 * @param y y coordinate of the point.
	 * @param z z coordinate of the point.
	 * @param closest set with the coordinates of the closest point, if not <code>null</code>.
	 * @param farthest set with the coordinates of the farthest point, if not <code>null</code>.
	 */
	public static void computeClosestFarestOBBPoints(
			double cx, double cy, double cz,
			double axis1x, double axis1y, double axis1z,
			double axis2x, double axis2y, double axis2z,
			double axis3x, double axis3y, double axis3z,
			double axis1Extent, double axis2Extent, double axis3Extent,
			double x, double y, double z,
			Point3D closest, Point3D farthest) {
		assert(axis1Extent>=0);
		assert(axis2Extent>=0);
		assert(axis3Extent>=0);

		double dx = x - cx;
		double dy = y - cy;
		double dz = z - cz;

		// Start results at center of box; make steps from there
		if (closest != null) {
			closest.set(cx, cy, cz);		
		}
		if (farthest!=null) {
			farthest.set(cx, cy, cz);		
		}

		// For each OBB axis...
		computeClosestFarthestOnAxis(axis1x, axis1y, axis1z, axis1Extent, dx, dy, dz, closest, farthest);
		computeClosestFarthestOnAxis(axis2x, axis2y, axis2z, axis2Extent, dx, dy, dz, closest, farthest);
		computeClosestFarthestOnAxis(axis3x, axis3y, axis3z, axis3Extent, dx, dy, dz, closest, farthest);
	}
	
	private static void computeClosestFarthestOnAxis(
			double ax, double ay, double az, double extent,
			double dx, double dy, double dz,
			Point3D closest, Point3D farthest) {
		double d1, d2;
		
		// ...project d onto that axis to get the distance along the axis of d from the box center
		d1 = d2 = FunctionalVector3D.dotProduct(dx, dy, dz, ax, ay, az);

		if (closest != null) {
			// If distance farther than the box extents, clamp to the box
			if (d1 > extent) {
				d1 = extent;
			} else if (d1 < -extent) {
				d1 = -extent;
			}

			// Step that distance along the axis to get world coordinate
			//q += dist * this.axis[i];
			closest.add(d1 * ax, d1 * ay, d1 * az);
		}

		if (farthest != null) {
			// Clamp to the other side of the box
			if (d2 >= 0.) {
				d2 = -extent;
			}
			else {
				d2 = extent;
			}

			// Step that distance along the axis to get world coordinate
			//q += dist * this.axis[i];
			farthest.add(d2 * ax, d2 * ay, d2 * az);
		}
	}

	/**
	 * Compute the oriented bounding box center, axis and extent.
	 * 
	 * @param points
	 *            is the list of the points enclosed by the OBB
	 * @param R
	 *            is the vector where the R axis of the OBB is put
	 * @param S
	 *            is the vector where the S axis of the OBB is put
	 * @param T
	 *            is the the vector where T axis of the OBB is put
	 * @param center
	 *            is the point which is set with the OBB's center coordinates.
	 * @param extents
	 *            are the extents of the OBB for the R, S and T axis.
	 */
	public static void computeOBBCenterAxisExtents(
			Iterable<? extends Point3D> points,
			Vector3f R, Vector3f S, Vector3f T, Point3f center,
			double[] extents) {
		assert (points != null);
		assert (center != null);
		assert (extents != null && extents.length >= 3);

		Vector3f vecNull = new Vector3f();
		if (R.equals(vecNull) && S.equals(vecNull) && T.equals(vecNull)) {
			// Determining the covariance matrix of the points
			// and set the center of the box
			Matrix3f cov = new Matrix3f();
			cov.cov(points);

			// Determining eigenvectors of covariance matrix and defines RST axis
			Matrix3f rst = new Matrix3f();// eigenvectors

			cov.eigenVectorsOfSymmetricMatrix(rst);
			rst.getColumn(0, R);
			rst.getColumn(1, S);
			rst.getColumn(2, T);
		}//else we have already set some constraints on OBB axis, dosen't need to compute them again 

		double minR = Double.POSITIVE_INFINITY;
		double maxR = Double.NEGATIVE_INFINITY;
		double minS = Double.POSITIVE_INFINITY;
		double maxS = Double.NEGATIVE_INFINITY;
		double minT = Double.POSITIVE_INFINITY;
		double maxT = Double.NEGATIVE_INFINITY;

		double PdotR;
		double PdotS;
		double PdotT;
		Vector3f v = new Vector3f();

		for (Point3D tuple : points) {
			v.set(tuple);

			PdotR = v.dot(R);
			PdotS = v.dot(S);
			PdotT = v.dot(T);

			if (PdotR < minR)
				minR = PdotR;
			if (PdotR > maxR)
				maxR = PdotR;
			if (PdotS < minS)
				minS = PdotS;
			if (PdotS > maxS)
				maxS = PdotS;
			if (PdotT < minT)
				minT = PdotT;
			if (PdotT > maxT)
				maxT = PdotT;
		}

		double a = (maxR + minR) / 2.f;
		double b = (maxS + minS) / 2.f;
		double c = (maxT + minT) / 2.f;

		// Set the center of the OBB
		center.set(a * R.getX() + b * S.getX() + c * T.getX(),

				a * R.getY() + b * S.getY() + c * T.getY(),

				a * R.getZ() + b * S.getZ() + c * T.getZ());

		// Compute extents
		extents[0] = (maxR - minR) / 2.f;
		extents[1] = (maxS - minS) / 2.f;
		extents[2] = (maxT - minT) / 2.f;

		// Normalize axis
		R.normalize();
		S.normalize();
		T.normalize();

		// Selection with largest magnitude eigenvalue to identify R
		int max = -1;
		for (int i = 0; i < 3; i++) {
			if (extents[i] > max) {
				max = i;
			}
		}
		switch (max) {
		case 0: {
			// do nothing, right order
		}
		break;

		case 1: {
			Vector3f tmpVec = new Vector3f(R);
			R.set(S);
			S.set(T);
			T.set(tmpVec);

			double tmpD = extents[0];
			extents[0] = extents[1];
			extents[1] = extents[2];
			extents[2] = tmpD;
		}
		break;
		case 2: {
			Vector3f tmpVec = new Vector3f(S);
			S.set(R);
			R.set(T);
			T.set(tmpVec);

			double tmpD = extents[1];
			extents[1] = extents[0];
			extents[0] = extents[2];
			extents[2] = tmpD;

		}
		break;
		default:
		}
	}

	/** Center of the box.
	 */
	protected final Point3f center = new Point3f();

	/**
	 * First axis.
	 */
	protected final Vector3f axis1 = new Vector3f();

	/**
	 * Second axis.
	 */
	protected final Vector3f axis2 = new Vector3f();

	/**
	 * Third axis.
	 */
	protected final Vector3f axis3 = new Vector3f();

	/**
	 * First axis extent of the OBB, cannot be negative.
	 */
	protected double extent1;

	/**
	 * Second axis extent of the OBB, cannot be negative.
	 */
	protected double extent2;

	/**
	 * Third axis extent of the OBB, cannot be negative.
	 */
	protected double extent3;

	/**
	 * Build an empty OBB.
	 */
	public OrientedBox3f() {
		//
	}

	/**
	 * Build an OBB.
	 * <p>
	 * The thirds axis is computed from the cross product with the two other axis.
	 * The cross product may be {@link Vector3f#crossLeftHand(org.arakhne.afc.math.geometry.d3.Vector3D)}
	 * or {@link Vector3f#crossRightHand(org.arakhne.afc.math.geometry.d3.Vector3D)} according to
	 * {@link CoordinateSystem3D#getDefaultCoordinateSystem()}.
	 *
	 * @param cx x coordinate of the box center.
	 * @param cy y coordinate of the box center.
	 * @param cz z coordinate of the box center.
	 * @param axis1x x coordinate of the first axis of the box.
	 * @param axis1y y coordinate of the first axis of the box.
	 * @param axis1z z coordinate of the first axis of the box.
	 * @param axis2x x coordinate of the second axis of the box.
	 * @param axis2y y coordinate of the secons axis of the box.
	 * @param axis2z z coordinate of the second axis of the box.
	 * @param axis1Extent extent of the first axis.
	 * @param axis2Extent extent of the second axis.
	 * @param axis3Extent extent of the third axis.
	 */
	public OrientedBox3f(
			double cx, double cy, double cz,
			double axis1x, double axis1y, double axis1z,
			double axis2x, double axis2y, double axis2z,
			double axis1Extent, double axis2Extent, double axis3Extent) {
		set(cx, cy, cz,
				axis1x, axis1y, axis1z, axis2x, axis2y, axis2z,
				axis1Extent, axis2Extent, axis3Extent,
				CoordinateSystem3D.getDefaultCoordinateSystem());
	}

	/**
	 * Build an OBB.
	 * <p>
	 * The thirds axis is computed from the cross product with the two other axis.
	 * The cross product may be {@link Vector3f#crossLeftHand(org.arakhne.afc.math.geometry.d3.Vector3D)}
	 * or {@link Vector3f#crossRightHand(org.arakhne.afc.math.geometry.d3.Vector3D)} according to
	 * the given <code>system</code>.
	 *
	 * @param cx x coordinate of the box center.
	 * @param cy y coordinate of the box center.
	 * @param cz z coordinate of the box center.
	 * @param axis1x x coordinate of the first axis of the box.
	 * @param axis1y y coordinate of the first axis of the box.
	 * @param axis1z z coordinate of the first axis of the box.
	 * @param axis2x x coordinate of the second axis of the box.
	 * @param axis2y y coordinate of the secons axis of the box.
	 * @param axis2z z coordinate of the second axis of the box.
	 * @param axis1Extent extent of the first axis.
	 * @param axis2Extent extent of the second axis.
	 * @param axis3Extent extent of the third axis.
	 * @param system the coordinate system to use for computing the third axis.
	 */
	public OrientedBox3f(
			double cx, double cy, double cz,
			double axis1x, double axis1y, double axis1z,
			double axis2x, double axis2y, double axis2z,
			double axis1Extent, double axis2Extent, double axis3Extent,
			CoordinateSystem3D system) {
		set(cx, cy, cz,
				axis1x, axis1y, axis1z,
				axis2x, axis2y, axis2z,
				axis1Extent, axis2Extent, axis3Extent,
				system);
	}

	/**
	 * Build an OBB.
	 * <p>
	 * The thirds axis is computed from the cross product with the two other axis.
	 * The cross product may be {@link Vector3f#crossLeftHand(org.arakhne.afc.math.geometry.d3.Vector3D)}
	 * or {@link Vector3f#crossRightHand(org.arakhne.afc.math.geometry.d3.Vector3D)} according to
	 * {@link CoordinateSystem3D#getDefaultCoordinateSystem()}.
	 *
	 * @param center the box center.
	 * @param axis1 the first axis of the box.
	 * @param axis2 the second axis of the box.
	 * @param axis1Extent extent of the first axis.
	 * @param axis2Extent extent of the second axis.
	 * @param axis3Extent extent of the third axis.
	 */
	@SuppressWarnings("hiding")
	public OrientedBox3f(
			Point3D center,
			Vector3D axis1,
			Vector3D axis2,
			double axis1Extent, double axis2Extent, double axis3Extent) {
		this(center.getX(), center.getY(), center.getZ(),
				axis1.getX(), axis1.getY(), axis1.getZ(),
				axis2.getX(), axis2.getY(), axis2.getZ(),
				axis1Extent, axis2Extent, axis3Extent);
	}

	/**
	 * Build an OBB from the set of vertex that composes the corresponding object 3D.
	 * 
	 * @param vertices
	 */
	public OrientedBox3f(Iterable<? extends Point3D> vertices) {
		setFromPointCloud(vertices);
	}

	@Override
	public String toString() {
		StringBuilder s = new StringBuilder("OBB "); //$NON-NLS-1$
		s.append("center: "); //$NON-NLS-1$
		s.append(this.center.toString());
		s.append(" axis1: "); //$NON-NLS-1$
		s.append(this.axis1.toString());
		s.append(" axis2: "); //$NON-NLS-1$
		s.append(this.axis2.toString());
		s.append(" axis3: "); //$NON-NLS-1$
		s.append(this.axis3.toString());
		s.append(" extent1: "); //$NON-NLS-1$
		s.append(this.extent1);
		s.append(" extent2: "); //$NON-NLS-1$
		s.append(this.extent2);
		s.append(" extent3: "); //$NON-NLS-1$
		s.append(this.extent3);
		return s.toString();
	}

	/** Replies the center.
	 *
	 * @return the center.
	 */
	public Point3f getCenter() {
		return this.center.clone();
	}

	/** Replies the center x.
	 *
	 * @return the center x.
	 */
	public double getCenterX() {
		return this.center.getX();
	}

	/** Replies the center y.
	 *
	 * @return the center y.
	 */
	public double getCenterY() {
		return this.center.getY();
	}

	/** Replies the center z.
	 *
	 * @return the center z.
	 */
	public double getCenterZ() {
		return this.center.getZ();
	}

	/** Set the center.
	 * 
	 * @param cx the center x.
	 * @param cy the center y.
	 * @param cz the center z.
	 */
	public void setCenter(double cx, double cy, double cz) {
		this.center.set(cx, cy, cz);
	}

	/** Set the center.
	 * 
	 * @param center1
	 */
	public void setCenter(Point3D center1) {
		setCenter(center1.getX(), center1.getY(), center1.getZ());
	}

	/** Replies the first axis of the oriented box.
	 *
	 * @return the unit vector of the first axis. 
	 */
	public Vector3f getFirstAxis() {
		return this.axis1.clone();
	}

	/** Replies coordinate x of the first axis of the oriented box.
	 *
	 * @return the coordinate x of the unit vector of the first axis. 
	 */
	public double getFirstAxisX() {
		return this.axis1.getX();
	}

	/** Replies coordinate y of the first axis of the oriented box.
	 *
	 * @return the coordinate y of the unit vector of the first axis. 
	 */
	public double getFirstAxisY() {
		return this.axis1.getY();
	}

	/** Replies coordinate z of the first axis of the oriented box.
	 *
	 * @return the coordinate z of the unit vector of the first axis. 
	 */
	public double getFirstAxisZ() {
		return this.axis1.getZ();
	}

	/** Replies the second axis of the oriented box.
	 *
	 * @return the unit vector of the second axis. 
	 */
	public Vector3f getSecondAxis() {
		return this.axis2.clone();
	}

	/** Replies coordinate x of the second axis of the oriented box.
	 *
	 * @return the coordinate x of the unit vector of the second axis. 
	 */
	public double getSecondAxisX() {
		return this.axis2.getX();
	}

	/** Replies coordinate y of the second axis of the oriented box.
	 *
	 * @return the coordinate y of the unit vector of the second axis. 
	 */
	public double getSecondAxisY() {
		return this.axis2.getY();
	}

	/** Replies coordinate z of the second axis of the oriented box.
	 *
	 * @return the coordinate z of the unit vector of the second axis. 
	 */
	public double getSecondAxisZ() {
		return this.axis2.getZ();
	}

	/** Replies the third axis of the oriented box.
	 *
	 * @return the unit vector of the third axis. 
	 */
	public Vector3f getThirdAxis() {
		return this.axis3.clone();
	}

	/** Replies coordinate x of the third axis of the oriented box.
	 *
	 * @return the coordinate x of the unit vector of the third axis. 
	 */
	public double getThirdAxisX() {
		return this.axis3.getX();
	}

	/** Replies coordinate y of the third axis of the oriented box.
	 *
	 * @return the coordinate y of the unit vector of the third axis. 
	 */
	public double getThirdAxisY() {
		return this.axis3.getY();
	}

	/** Replies coordinate z of the third axis of the oriented box.
	 *
	 * @return the coordinate z of the unit vector of the third axis. 
	 */
	public double getThirdAxisZ() {
		return this.axis3.getZ();
	}

	/** Replies the demi-size of the box along its first axis.
	 * 
	 * @return the extent along the first axis.
	 */
	public double getFirstAxisExtent() {
		return this.extent1;
	}

	/** Change the demi-size of the box along its first axis.
	 * 
	 * @param extent - the extent along the first axis.
	 */
	public void setFirstAxisExtent(double extent) {
		this.extent1 = Math.max(extent, 0);
	}

	/** Replies the demi-size of the box along its second axis.
	 * 
	 * @return the extent along the second axis.
	 */
	public double getSecondAxisExtent() {
		return this.extent2;
	}

	/** Change the demi-size of the box along its second axis.
	 * 
	 * @param extent - the extent along the second axis.
	 */
	public void setSecondAxisExtent(double extent) {
		this.extent2 = Math.max(extent, 0);
	}

	/** Replies the demi-size of the box along its third axis.
	 * 
	 * @return the extent along the third axis.
	 */
	public double getThirdAxisExtent() {
		return this.extent3;
	}

	/** Change the demi-size of the box along its third axis.
	 * 
	 * @param extent - the extent along the third axis.
	 */
	public void setThirdAxisExtent(double extent) {
		this.extent3 = Math.max(extent, 0);
	}

	/** Set the first axis of the box.
	 * The third axis is updated to be perpendicular to the two other axis.
	 * 
	 * @param axis - the new values for the first axis.
	 */
	public void setFirstAxis(Vector3D axis) {
		setFirstAxis(axis.getX(), axis.getY(), axis.getZ(), getFirstAxisExtent());
	}

	/** Set the first axis of the box.
	 * The third axis is updated to be perpendicular to the two other axis.
	 * 
	 * @param axis - the new values for the first axis.
	 * @param extent - the extent of the axis.
	 */
	public void setFirstAxis(Vector3D axis, double extent) {
		setFirstAxis(axis.getX(), axis.getY(), axis.getZ(), extent);
	}

	/** Set the first axis of the box.
	 * The third axis is updated to be perpendicular to the two other axis.
	 * 
	 * @param x
	 * @param y
	 * @param z
	 */
	public void setFirstAxis(double x, double y, double z) {
		setFirstAxis(x, y, z, getFirstAxisExtent());
	}

	/** Set the first axis of the box.
	 * The third axis is updated to be perpendicular to the two other axis.
	 * 
	 * @param x
	 * @param y
	 * @param z
	 * @param extent
	 */
	public void setFirstAxis(double x, double y, double z, double extent) {
		setFirstAxis(x, y, z, extent, CoordinateSystem3D.getDefaultCoordinateSystem());
	}

	/** Set the first axis of the second .
	 * The third axis is updated to be perpendicular to the two other axis.
	 * 
	 * @param x
	 * @param y
	 * @param z
	 * @param extent
	 * @param system
	 */
	public void setFirstAxis(double x, double y, double z, double extent, CoordinateSystem3D system) {
		this.axis1.set(x, y, z);
		assert(this.axis1.isUnitVector());
		if (system.isLeftHanded()) {
			this.axis3.set(this.axis1.crossLeftHand(this.axis2));
		} else {
			this.axis3.set(this.axis3.crossRightHand(this.axis2));
		}
		this.extent1 = extent;
	}

	/** Set the second axis of the box.
	 * The third axis is updated to be perpendicular to the two other axis.
	 * 
	 * @param axis - the new values for the first axis.
	 */
	public void setSecondAxis(Vector3D axis) {
		setSecondAxis(axis.getX(), axis.getY(), axis.getZ(), getSecondAxisExtent());
	}

	/** Set the second axis of the box.
	 * The third axis is updated to be perpendicular to the two other axis.
	 * 
	 * @param axis - the new values for the first axis.
	 * @param extent - the extent of the axis.
	 */
	public void setSecondAxis(Vector3D axis, double extent) {
		setSecondAxis(axis.getX(), axis.getY(), axis.getZ(), extent);
	}

	/** Set the second axis of the box.
	 * The third axis is updated to be perpendicular to the two other axis.
	 * 
	 * @param x - the new values for the second axis.
	 * @param y - the new values for the second axis.
	 * @param z - the new values for the second axis.
	 */
	public void setSecondAxis(double x, double y, double z) {
		setSecondAxis(x, y, z, getSecondAxisExtent());
	}

	/** Set the second axis of the box.
	 * The third axis is updated to be perpendicular to the two other axis.
	 * 
	 * @param x
	 * @param y
	 * @param z
	 * @param extent
	 */
	public void setSecondAxis(double x, double y, double z, double extent) {
		setSecondAxis(x, y, z, extent, CoordinateSystem3D.getDefaultCoordinateSystem());
	}

	/** Set the second axis of the box.
	 * The third axis is updated to be perpendicular to the two other axis.
	 * 
	 * @param x
	 * @param y
	 * @param z
	 * @param extent
	 * @param system
	 */
	public void setSecondAxis(double x, double y, double z, double extent, CoordinateSystem3D system) {
		this.axis2.set(x, y, z);
		assert(this.axis2.isUnitVector());
		if (system.isLeftHanded()) {
			this.axis3.set(this.axis1.crossLeftHand(this.axis2));
		} else {
			this.axis3.set(this.axis3.crossRightHand(this.axis2));
		}
		this.extent2 = extent;
	}

	/**
	 * Change the attributes of the oriented box.
	 * <p>
	 * The thirds axis is computed from the cross product with the two other axis.
	 * The cross product may be {@link Vector3f#crossLeftHand(org.arakhne.afc.math.geometry.d3.Vector3D)}
	 * or {@link Vector3f#crossRightHand(org.arakhne.afc.math.geometry.d3.Vector3D)} according to
	 * {@link CoordinateSystem3D#getDefaultCoordinateSystem()}.
	 *
	 * @param cx x coordinate of the box center.
	 * @param cy y coordinate of the box center.
	 * @param cz z coordinate of the box center.
	 * @param axis1x x coordinate of the first axis of the box.
	 * @param axis1y y coordinate of the first axis of the box.
	 * @param axis1z z coordinate of the first axis of the box.
	 * @param axis2x x coordinate of the second axis of the box.
	 * @param axis2y y coordinate of the secons axis of the box.
	 * @param axis2z z coordinate of the second axis of the box.
	 * @param axis1Extent extent of the first axis.
	 * @param axis2Extent extent of the second axis.
	 * @param axis3Extent extent of the third axis.
	 */
	public void set(
			double cx, double cy, double cz,
			double axis1x, double axis1y, double axis1z,
			double axis2x, double axis2y, double axis2z,
			double axis1Extent, double axis2Extent, double axis3Extent) {
		set(cx, cy, cz,
				axis1x, axis1y, axis1z,
				axis2x, axis2y, axis2z,
				axis1Extent, axis2Extent, axis3Extent,
				CoordinateSystem3D.getDefaultCoordinateSystem());
	}

	/**
	 * Change the attributes of the oriented box.
	 * <p>
	 * The thirds axis is computed from the cross product with the two other axis.
	 * The cross product may be {@link Vector3f#crossLeftHand(org.arakhne.afc.math.geometry.d3.Vector3D)}
	 * or {@link Vector3f#crossRightHand(org.arakhne.afc.math.geometry.d3.Vector3D)} according to
	 * the given <code>system</code>.
	 *
	 * @param cx x coordinate of the box center.
	 * @param cy y coordinate of the box center.
	 * @param cz z coordinate of the box center.
	 * @param axis1x x coordinate of the first axis of the box.
	 * @param axis1y y coordinate of the first axis of the box.
	 * @param axis1z z coordinate of the first axis of the box.
	 * @param axis2x x coordinate of the second axis of the box.
	 * @param axis2y y coordinate of the secons axis of the box.
	 * @param axis2z z coordinate of the second axis of the box.
	 * @param axis1Extent extent of the first axis.
	 * @param axis2Extent extent of the second axis.
	 * @param axis3Extent extent of the third axis.
	 * @param system the coordinate system to use for computing the third axis.
	 */
	public void set(
			double cx, double cy, double cz,
			double axis1x, double axis1y, double axis1z,
			double axis2x, double axis2y, double axis2z,
			double axis1Extent, double axis2Extent, double axis3Extent,
			CoordinateSystem3D system) {
		assert (system != null);
		this.center.set(cx, cy, cz);
		this.axis1.set(axis1x, axis1y, axis1z);
		this.axis2.set(axis2x, axis2y, axis2z);
		if (system.isLeftHanded()) {
			this.axis3.set(this.axis1.crossLeftHand(this.axis2));
		} else {
			this.axis3.set(this.axis1.crossRightHand(this.axis2));
		}
		this.extent1 = axis1Extent;
		this.extent2 = axis2Extent;
		this.extent3 = axis3Extent;
	}

	@Override
	public void set(Shape3F s) {
		if (s instanceof OrientedBox3f) {
			OrientedBox3f c = (OrientedBox3f) s;
			set(c.getCenterX(), c.getCenterY(), c.getCenterZ(),
					c.getFirstAxisX(), c.getFirstAxisY(), c.getFirstAxisZ(),
					c.getSecondAxisX(), c.getSecondAxisY(), c.getSecondAxisZ(),
					c.getFirstAxisExtent(), c.getSecondAxisExtent(), c.getThirdAxisExtent());
		} else {
			AlignedBox3f r = s.toBoundingBox();
			setFromPointCloud(
					new Point3f(r.getMinX(), r.getMinY(), r.getMinZ()),
					new Point3f(r.getMinX(), r.getMinY(), r.getMaxZ()),
					new Point3f(r.getMinX(), r.getMaxY(), r.getMinZ()),
					new Point3f(r.getMinX(), r.getMaxY(), r.getMaxZ()),
					new Point3f(r.getMaxX(), r.getMinY(), r.getMinZ()),
					new Point3f(r.getMaxX(), r.getMinY(), r.getMaxZ()),
					new Point3f(r.getMaxX(), r.getMaxY(), r.getMinZ()),
					new Point3f(r.getMaxX(), r.getMaxY(), r.getMaxZ()));
		}
	}

	/** Set the oriented box from a could of points.
	 *
	 * @param pointCloud - the cloud of points.
	 */
	public void setFromPointCloud(Point3D... pointCloud) {
		setFromPointCloud(Arrays.asList(pointCloud));
	}

	/** Set the oriented box from a could of points.
	 *
	 * @param pointCloud - the cloud of points.
	 */
	public void setFromPointCloud(Iterable<? extends Point3D> pointCloud) {
		Vector3f r = new Vector3f();
		Vector3f s = new Vector3f();
		Vector3f t = new Vector3f();
		Point3f c = new Point3f();
		double[] extents = new double[3];
		computeOBBCenterAxisExtents(
				pointCloud,
				r, s, t,
				c,
				extents);
		set(c.getX(), c.getY(), c.getZ(),
				r.getX(), r.getY(), r.getZ(),
				s.getX(), s.getY(), s.getZ(),
				extents[0], extents[1], extents[2]);
	}

	@Override
	public void clear() {
		this.center.set(0, 0, 0);
		this.axis1.set(0, 0, 0);
		this.axis2.set(0, 0, 0);
		this.axis3.set(0, 0, 0);
		this.extent1 = 0.;
		this.extent2 = 0.;
		this.extent3 = 0.;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj instanceof OrientedBox3f) {
			OrientedBox3f ob3d = (OrientedBox3f) obj;
			return ((getCenter() == ob3d.getCenter()) &&
					(getFirstAxis() == ob3d.getFirstAxis()) &&
					(getSecondAxis() == ob3d.getSecondAxis()) &&
					(getThirdAxis() == ob3d.getThirdAxis()) &&
					(getFirstAxisExtent() == ob3d.getFirstAxisExtent()) &&
					(getSecondAxisExtent() == ob3d.getSecondAxisExtent()) &&
					(getThirdAxisExtent() == ob3d.getThirdAxisExtent()));
		}
		return false;
	}

	@Override
	public int hashCode() {
		long bits = 1L;
		bits = 31L * bits + doubleToLongBits(getCenterX());
		bits = 31L * bits + doubleToLongBits(getCenterY());
		bits = 31L * bits + doubleToLongBits(getCenterZ());
		bits = 31L * bits + doubleToLongBits(getFirstAxisX());
		bits = 31L * bits + doubleToLongBits(getFirstAxisY());
		bits = 31L * bits + doubleToLongBits(getFirstAxisZ());
		bits = 31L * bits + doubleToLongBits(getSecondAxisX());
		bits = 31L * bits + doubleToLongBits(getSecondAxisY());
		bits = 31L * bits + doubleToLongBits(getSecondAxisZ());
		bits = 31L * bits + doubleToLongBits(getThirdAxisX());
		bits = 31L * bits + doubleToLongBits(getThirdAxisY());
		bits = 31L * bits + doubleToLongBits(getThirdAxisZ());
		bits = 31L * bits + doubleToLongBits(getFirstAxisExtent());
		bits = 31L * bits + doubleToLongBits(getSecondAxisExtent());
		bits = 31L * bits + doubleToLongBits(getThirdAxisExtent());
		return (int) (bits ^ (bits >> 32));
	}

	@Override
	public AlignedBox3f toBoundingBox() {
		AlignedBox3f box = new AlignedBox3f();
		toBoundingBox(box);
		return box;
	}

	@Override
	public void toBoundingBox(AlignedBox3f box) {
		double a1x = this.axis1.getX() * this.extent1;
		double a1y = this.axis1.getY() * this.extent1;
		double a1z = this.axis1.getZ() * this.extent1;
		double a2x = this.axis2.getX() * this.extent2;
		double a2y = this.axis2.getY() * this.extent2;
		double a2z = this.axis2.getZ() * this.extent2;
		double a3x = this.axis3.getX() * this.extent3;
		double a3y = this.axis3.getY() * this.extent3;
		double a3z = this.axis3.getZ() * this.extent3;

		double x = getCenterX();
		double y = getCenterY();
		double z = getCenterZ();

		double[] pts;

		pts = new double[] {
				x + a1x + a2x + a3x,
				x + a1x + a2x - a3x,
				x + a1x - a2x + a3x,
				x + a1x - a2x - a3x,
				x - a1x + a2x + a3x,
				x - a1x + a2x - a3x,
				x - a1x - a2x + a3x,
				x - a1x - a2x - a3x,
		};
		double minx = Double.POSITIVE_INFINITY;
		double maxx = Double.NEGATIVE_INFINITY;
		for (double v : pts) {
			if (v < minx) {
				minx = v;
			}
			if (v > maxx) {
				maxx = v;
			}
		}

		pts = new double[] {
				y + a1y + a2y + a3y,
				y + a1y + a2y - a3y,
				y + a1y - a2y + a3y,
				y + a1y - a2y - a3y,
				y - a1y + a2y + a3y,
				y - a1y + a2y - a3y,
				y - a1y - a2y + a3y,
				y - a1y - a2y - a3y,
		};
		double miny = Double.POSITIVE_INFINITY;
		double maxy = Double.NEGATIVE_INFINITY;
		for (double v : pts) {
			if (v < miny) {
				miny = v;
			}
			if (v > maxy) {
				maxy = v;
			}
		}

		pts = new double[] {
				z + a1z + a2z + a3z,
				z + a1z + a2z - a3z,
				z + a1z - a2z + a3z,
				z + a1z - a2z - a3z,
				z - a1z + a2z + a3z,
				z - a1z + a2z - a3z,
				z - a1z - a2z + a3z,
				z - a1z - a2z - a3z,
		};
		double minz = Double.POSITIVE_INFINITY;
		double maxz = Double.NEGATIVE_INFINITY;
		for (double v : pts) {
			if (v < minz) {
				minz = v;
			}
			if (v > maxz) {
				maxz = v;
			}
		}

		box.set(minx, miny, minz, maxx - minx, maxy - miny, maxz - minz);
	}

	@Override
	public double distanceSquared(Point3D p) {
		Vector3f v = new Vector3f();
		v.sub(p, this.center);
		double sqDist = 0.;
		double d, excess;

		// Project vector from box center to p on each axis, getting the distance
		// of p along that axis, and count any excess distance outside box extents

		d = v.dot(this.axis1);
		excess = 0.;
		if (d < -this.extent1) {
			excess = -this.extent1 - d;
		} else if (d > this.extent1) {
			excess = d - this.extent1;
		}
		sqDist += excess * excess;

		d = v.dot(this.axis2);
		excess = 0.;
		if (d < -this.extent2) {
			excess = -this.extent2 - d;
		} else if (d > this.extent2) {
			excess = d - this.extent2;
		}
		sqDist += excess * excess;

		d = v.dot(this.axis3);
		excess = 0.;
		if (d < -this.extent3) {
			excess = -this.extent3 - d;
		} else if (d > this.extent3) {
			excess = d - this.extent3;
		}
		sqDist += excess * excess;

		return sqDist;
	}

	@Override
	public double distanceL1(Point3D p) {
		Vector3f v = new Vector3f();
		v.sub(p, this.center);
		double l1Dist = 0.;
		double d, excess;

		// Project vector from box center to p on each axis, getting the distance
		// of p along that axis, and count any excess distance outside box extents

		d = v.dot(this.axis1);
		excess = 0.;
		if (d < -this.extent1) {
			excess = -this.extent1 - d;
		} else if (d > this.extent1) {
			excess = d - this.extent1;
		}
		l1Dist += Math.abs(excess);

		d = v.dot(this.axis2);
		excess = 0.;
		if (d < -this.extent2) {
			excess = -this.extent2 - d;
		} else if (d > this.extent2) {
			excess = d - this.extent2;
		}
		l1Dist += Math.abs(excess);

		d = v.dot(this.axis3);
		excess = 0.;
		if (d < -this.extent3) {
			excess = -this.extent3 - d;
		} else if (d > this.extent3) {
			excess = d - this.extent3;
		}
		l1Dist += Math.abs(excess);

		return l1Dist;
	}

	@Override
	public double distanceLinf(Point3D p) {
		Vector3f v = new Vector3f();
		v.sub(p, this.center);
		double linfDist, d, excess;

		// Project vector from box center to p on each axis, getting the distance
		// of p along that axis, and count any excess distance outside box extents

		d = v.dot(this.axis1);
		excess = 0.;
		if (d < -this.extent1) {
			excess = -this.extent1 - d;
		} else if (d > this.extent1) {
			excess = d - this.extent1;
		}
		linfDist = Math.abs(excess);

		d = v.dot(this.axis2);
		excess = 0.;
		if (d < -this.extent2) {
			excess = -this.extent2 - d;
		} else if (d > this.extent2) {
			excess = d - this.extent2;
		}
		linfDist = Math.max(linfDist, Math.abs(excess));

		d = v.dot(this.axis3);
		excess = 0.;
		if (d < -this.extent3) {
			excess = -this.extent3 - d;
		} else if (d > this.extent3) {
			excess = d - this.extent3;
		}
		linfDist = Math.max(linfDist, Math.abs(excess));

		return linfDist;
	}

	@Override
	public void transform(Transform3D transformationMatrix) {
		transformationMatrix.transform(this.center);
		transformationMatrix.transform(this.axis1);
		transformationMatrix.transform(this.axis2);
		transformationMatrix.transform(this.axis3);
		this.extent1 = this.axis1.length();
		this.extent2 = this.axis2.length();
		this.extent3 = this.axis3.length();
		this.axis1.normalize();
		this.axis2.normalize();
		this.axis3.normalize();
	}

	@Override
	public void translate(double dx, double dy, double dz) {
		double x = getCenterX() + dx;
		double y = getCenterY() + dy;
		double z = getCenterZ() + dz;
		this.center.set(x, y, z);
	}

	/** Rotate the box around its pivot point.
	 * The pivot point is the center of the box.
	 *
	 * @param rotation the rotation.
	 */
	public void rotate(Quaternion rotation) {
		Transform3D m = new Transform3D();
		m.transform(this.axis1);
		m.transform(this.axis2);
		m.transform(this.axis3);
		this.extent1 = this.axis1.length();
		this.extent2 = this.axis2.length();
		this.extent3 = this.axis3.length();
		this.axis1.normalize();
		this.axis2.normalize();
		this.axis3.normalize();
	}

	/** Rotate the box around a given pivot point.
	 * The default pivot point of the center of the box.
	 *
	 * @param rotation the rotation.
	 * @param pivot the pivot point. If <code>null</code> the default pivot point is used.
	 */
	public void rotate(Quaternion rotation, Point3D pivot) {
		if (pivot!=null) {
			// Change the center
			Transform3D m1 = new Transform3D();
			m1.setTranslation(-pivot.getX(), -pivot.getY(), -pivot.getZ());
			Transform3D m2 = new Transform3D();
			m2.setRotation(rotation);
			Transform3D m3 = new Transform3D();
			m3.setTranslation(pivot.getX(), pivot.getY(), pivot.getZ());

			Transform3D r = new Transform3D();
			r.mul(m1, m2);
			r.mul(m3);
			r.transform(this.center);
		}
		rotate(rotation);
	}

	@Override
	public boolean isEmpty() {
		return this.extent1 <= 0. || this.extent2 <= 0. || this.extent3 <= 0.;
	}

	@Override
	public Point3D getClosestPointTo(Point3D p) {
		Point3f closest = new Point3f();
		computeClosestFarestOBBPoints(
				getCenterX(), getCenterY(), getCenterZ(),
				getFirstAxisX(), getFirstAxisY(), getFirstAxisZ(),
				getSecondAxisX(), getSecondAxisY(), getSecondAxisZ(),
				getThirdAxisX(), getThirdAxisY(), getThirdAxisZ(),
				getFirstAxisExtent(), getSecondAxisExtent(), getThirdAxisExtent(),
				p.getX(), p.getY(), p.getZ(),
				closest, null);
		return closest;
	}

	@Override
	public Point3D getFarthestPointTo(Point3D p) {
		Point3f farthest = new Point3f();
		computeClosestFarestOBBPoints(
				getCenterX(), getCenterY(), getCenterZ(),
				getFirstAxisX(), getFirstAxisY(), getFirstAxisZ(),
				getSecondAxisX(), getSecondAxisY(), getSecondAxisZ(),
				getThirdAxisX(), getThirdAxisY(), getThirdAxisZ(),
				getFirstAxisExtent(), getSecondAxisExtent(), getThirdAxisExtent(),
				p.getX(), p.getY(), p.getZ(),
				null, farthest);
		return farthest;
	}

	@Override
	public boolean contains(double x, double y, double z) {
		return containsOrientedBoxPoint(
				getCenterX(), getCenterY(), getCenterZ(),
				getFirstAxisX(), getFirstAxisY(), getFirstAxisZ(),
				getSecondAxisX(), getSecondAxisY(), getSecondAxisZ(),
				getThirdAxisX(), getThirdAxisY(), getThirdAxisZ(),
				getFirstAxisExtent(), getSecondAxisExtent(), getThirdAxisExtent(),
				x, y, z);
	}

	@Override
	public boolean intersects(AlignedBox3f s) {
		return intersectsOrientedBoxAlignedBox(
				getCenterX(), getCenterY(), getCenterZ(), 
				getFirstAxisX(), getFirstAxisY(), getFirstAxisZ(),
				getSecondAxisX(), getSecondAxisY(), getSecondAxisZ(),
				getThirdAxisX(), getThirdAxisY(), getThirdAxisZ(),
				getFirstAxisExtent(), getSecondAxisExtent(), getThirdAxisExtent(),
				
				s.getMinX(), s.getMinY(), s.getMinZ(),
				s.getMaxX(), s.getMaxY(), s.getMaxZ());
	}

	@Override
	public boolean intersects(AbstractSphere3F s) {
		return AbstractSphere3F.intersectsSolidSphereOrientedBox(
				s.getX(), s.getY(), s.getZ(),
				s.getRadius(),
				
				getCenterX(), getCenterY(), getCenterZ(), 
				getFirstAxisX(), getFirstAxisY(), getFirstAxisZ(),
				getSecondAxisX(), getSecondAxisY(), getSecondAxisZ(),
				getThirdAxisX(), getThirdAxisY(), getThirdAxisZ(),
				getFirstAxisExtent(), getSecondAxisExtent(), getThirdAxisExtent());
	}

	@Override
	public boolean intersects(AbstractSegment3F s) {
		return AbstractSegment3F.intersectsSegmentOrientedBox(
				s.getX1(), s.getY1(), s.getZ1(),
				s.getX2(), s.getY2(), s.getZ2(),
				
				getCenterX(), getCenterY(), getCenterZ(), 
				getFirstAxisX(), getFirstAxisY(), getFirstAxisZ(),
				getSecondAxisX(), getSecondAxisY(), getSecondAxisZ(),
				getThirdAxisX(), getThirdAxisY(), getThirdAxisZ(),
				getFirstAxisExtent(), getSecondAxisExtent(), getThirdAxisExtent());
	}

	@Override
	public boolean intersects(Triangle3f s) {
		return Triangle3f.intersectsTriangleOrientedBox(
				s.getX1(), s.getY1(), s.getZ1(),
				s.getX2(), s.getY2(), s.getZ2(),
				s.getX3(), s.getY3(), s.getZ3(),
				
				getCenterX(), getCenterY(), getCenterZ(), 
				getFirstAxisX(), getFirstAxisY(), getFirstAxisZ(),
				getSecondAxisX(), getSecondAxisY(), getSecondAxisZ(),
				getThirdAxisX(), getThirdAxisY(), getThirdAxisZ(),
				getFirstAxisExtent(), getSecondAxisExtent(), getThirdAxisExtent());
	}

	@Override
	public boolean intersects(AbstractCapsule3F s) {
		return intersectsOrientedBoxCapsule(
				getCenterX(), getCenterY(), getCenterZ(), 
				getFirstAxisX(), getFirstAxisY(), getFirstAxisZ(),
				getSecondAxisX(), getSecondAxisY(), getSecondAxisZ(),
				getThirdAxisX(), getThirdAxisY(), getThirdAxisZ(),
				getFirstAxisExtent(), getSecondAxisExtent(), getThirdAxisExtent(),

				s.getMedialX1(), s.getMedialY1(), s.getMedialZ1(), 
				s.getMedialX2(), s.getMedialY2(), s.getMedialZ2(), 
				s.getRadius());
	}

	@Override
	public boolean intersects(OrientedBox3f s) {
		return intersectsOrientedBoxOrientedBox(
				getCenterX(), getCenterY(), getCenterZ(), 
				getFirstAxisX(), getFirstAxisY(), getFirstAxisZ(),
				getSecondAxisX(), getSecondAxisY(), getSecondAxisZ(),
				getThirdAxisX(), getThirdAxisY(), getThirdAxisZ(),
				getFirstAxisExtent(), getSecondAxisExtent(), getThirdAxisExtent(),

				s.getCenterX(), s.getCenterY(), s.getCenterZ(), 
				s.getFirstAxisX(), s.getFirstAxisY(), s.getFirstAxisZ(),
				s.getSecondAxisX(), s.getSecondAxisY(), s.getSecondAxisZ(),
				s.getThirdAxisX(), s.getThirdAxisY(), s.getThirdAxisZ(),
				s.getFirstAxisExtent(), s.getSecondAxisExtent(), s.getThirdAxisExtent());
	}

	@Override
	public boolean intersects(Plane3D<?> plane) {
		return plane.intersects(this);
	}

}
