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
package org.arakhne.afc.math.geometry.d3.continuous;

import org.arakhne.afc.math.MathConstants;
import org.arakhne.afc.math.MathUtil;
import org.arakhne.afc.math.geometry.d3.FunctionalPoint3D;
import org.arakhne.afc.math.geometry.d3.FunctionalVector3D;
import org.arakhne.afc.math.geometry.d3.Point3D;
import org.arakhne.afc.math.geometry.d3.Vector3D;
import org.arakhne.afc.util.Pair;

/** This class represents a 3D line.
 * <p>
 * The equation of the line is:
 * <math xmlns="http://www.w3.org/1998/Math/MathML">
 *   <mrow>
 *     <mi>L</mi><mo>&#x2061;</mo><mfenced><mi>t</mi></mfenced>
 *     <mo>=</mo>
 *     <mi>P</mi><mo>+</mo>
 *     <mi>t</mi><mo>.</mo>
 *     <mover>
 *       <mi>D</mi>
 *       <mo>&#x20D7;</mo>
 *     </mover>
 *   </mrow>
 * </math>
 * for any real-valued <math><mi>t</mi></math>.
 * <math><mover><mi>D</mi><mo>&#x20D7;</mo></mover></math> is not
 * necessarily unit length. 
 *
 * @author $Author: cbohrhauer$
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public abstract class AbstractSegment3F extends AbstractShape3F<AbstractSegment3F> {

	private static final long serialVersionUID = -6299657640361588067L;

	/**
	 * Tests if the axis-aligned box is intersecting a segment.
	 *
	 * @param sx1 x coordinate of the first point of the segment.
	 * @param sy1 y coordinate of the first point of the segment.
	 * @param sz1 z coordinate of the first point of the segment.
	 * @param sx2 x coordinate of the second point of the segment.
	 * @param sy2 y coordinate of the second point of the segment.
	 * @param sz2 z coordinate of the second point of the segment.
	 * @param minx coordinates of the lowest point of the box.
	 * @param miny coordinates of the lowest point of the box.
	 * @param minz coordinates of the lowest point of the box.
	 * @param maxx coordinates of the uppermost point of the box.
	 * @param maxy coordinates of the uppermost point of the box.
	 * @param maxz coordinates of the uppermost point of the box.
	 * @return <code>true</code> if the two shapes intersect each 
	 * other; <code>false</code> otherwise.
	 * @see "http://books.google.ca/books?id=fvA7zLEFWZgC"
	 */
	public static boolean intersectsSegmentAlignedBox(
			double sx1, double sy1, double sz1, double sx2, double sy2, double sz2,
			double minx, double miny, double minz, double maxx, double maxy, double maxz) {
		assert(minx<=maxx);
		assert(miny<=maxy);
		assert(minz<=maxz);

		double dx = (sx2 - sx1) * .5;
		double dy = (sy2 - sy1) * .5;
		double dz = (sz2 - sz1) * .5;

		double ex = (maxx - minx) * .5;
		double ey = (maxy - miny) * .5;
		double ez = (maxz - minz) * .5;

		double cx = sx1 + dx - (minx + maxx) * .5;
		double cy = sy1 + dy - (miny + maxy) * .5;
		double cz = sz1 + dz - (minz + maxz) * .5;

		double adx = Math.abs(dx);
		double ady = Math.abs(dy);
		double adz = Math.abs(dz);

		if (Math.abs(cx) > (ex + adx)) {
			return false;
		}
		if (Math.abs(cy) > (ey + ady)) {
			return false;
		}
		if (Math.abs(cz) > (ez + adz)) {
			return false;
		}

		if (Math.abs(dy * cz - dz * cy) > (ey * adz + ez * ady)) {
			return false;
		}
		if (Math.abs(dz * cx - dx * cz) > (ez * adx + ex * adz)) {
			return false;
		}
		if (Math.abs(dx * cy - dy * cx) > (ex * ady + ey * adx)) {
			return false;
		}
		return true;
	}

	/**
	 * Tests if the axis-aligned box is intersecting a segment.
	 *
	 * @param sx1 x coordinate of the first point of the segment.
	 * @param sy1 y coordinate of the first point of the segment.
	 * @param sz1 z coordinate of the first point of the segment.
	 * @param sx2 x coordinate of the second point of the segment.
	 * @param sy2 y coordinate of the second point of the segment.
	 * @param sz2 z coordinate of the second point of the segment.
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
	 * @return <code>true</code> if the two shapes intersect each 
	 * other; <code>false</code> otherwise.
	 */
	public static boolean intersectsSegmentOrientedBox(
			double sx1, double sy1, double sz1, double sx2, double sy2, double sz2,
			double centerx,double  centery,double  centerz,
			double axis1x, double axis1y, double axis1z,
			double axis2x, double axis2y, double axis2z,
			double axis3x, double axis3y, double axis3z,
			double extentAxis1, double extentAxis2, double extentAxis3) {
		// project the segment in the OBB axis base.
		double px1 = sx1 - centerx;
		double py1 = sy1 - centery;
		double pz1 = sz1 - centerz;
		double px2 = sx2 - centerx;
		double py2 = sy2 - centery;
		double pz2 = sz2 - centerz;
		
		double x1 = FunctionalVector3D.dotProduct(px1, py1, pz1, axis1x, axis1y, axis1z);
		double y1 = FunctionalVector3D.dotProduct(px1, py1, pz1, axis2x, axis2y, axis2z);
		double z1 = FunctionalVector3D.dotProduct(px1, py1, pz1, axis3x, axis3y, axis3z);

		double x2 = FunctionalVector3D.dotProduct(px2, py2, pz2, axis1x, axis1y, axis1z);
		double y2 = FunctionalVector3D.dotProduct(px2, py2, pz2, axis2x, axis2y, axis2z);
		double z2 = FunctionalVector3D.dotProduct(px2, py2, pz2, axis3x, axis3y, axis3z);

		return intersectsSegmentAlignedBox(
				x1, y1, z1, x2, y2, z2,
				-extentAxis1, -extentAxis2, -extentAxis3,
				extentAxis1, extentAxis2, extentAxis3);
	}

	/**
	 * Tests if the capsule is intersecting a segment.
	 *
	 * @param sx1 x coordinate of the first point of the segment.
	 * @param sy1 y coordinate of the first point of the segment.
	 * @param sz1 z coordinate of the first point of the segment.
	 * @param sx2 x coordinate of the second point of the segment.
	 * @param sy2 y coordinate of the second point of the segment.
	 * @param sz2 z coordinate of the second point of the segment.
	 * @param mx1 x coordinate of the first point of the capsule's segment.
	 * @param my1 y coordinate of the first point of the capsule's segment.
	 * @param mz1 z coordinate of the first point of the capsule's segment.
	 * @param mx2 x coordinate of the second point of the capsule's segment.
	 * @param my2 y coordinate of the second point of the capsule's segment.
	 * @param mz2 z coordinate of the second point of the capsule's segment.
	 * @param radius radius of the capsule.
	 * @return <code>true</code> if the two shapes intersect each 
	 * other; <code>false</code> otherwise.
	 * @see "http://books.google.ca/books?id=fvA7zLEFWZgC"
	 */
	public static boolean intersectsSegmentCapsule(
			double sx1, double sy1, double sz1, double sx2, double sy2, double sz2,
			double mx1, double my1, double mz1, double mx2, double my2, double mz2, double radius) {
		double d = distanceSquaredSegmentSegment(
				sx1, sy1, sz1, sx2, sy2, sz2,
				mx1, my1, mz1, mx2, my2, mz2);
		return d < (radius * radius);
	}

	/**
	 * Replies if two lines are parallel.
	 * <p>
	 * The given two lines are described respectivaly by two points, i.e. {@code (x1,y1,z1)} and {@code (x2,y2,z2)}
	 * for the first line, and {@code (x3,y3,z3)} and {@code (x4,y4,z4)} for the second line.
	 * <p>
	 * If you are interested to test if the two lines are colinear, see
	 * {@link #isCollinearLines(double, double, double, double, double, double, double, double, double, double, double, double)}.
	 * 
	 * @param x1
	 *            is the X coordinate of the first point of the first line.
	 * @param y1
	 *            is the Y coordinate of the first point of the first line.
	 * @param z1
	 *            is the Z coordinate of the first point of the first line.
	 * @param x2
	 *            is the X coordinate of the second point of the first line.
	 * @param y2
	 *            is the Y coordinate of the second point of the first line.
	 * @param z2
	 *            is the Z coordinate of the second point of the first line.
	 * @param x3
	 *            is the X coordinate of the first point of the second line.
	 * @param y3
	 *            is the Y coordinate of the first point of the second line.
	 * @param z3
	 *            is the Z coordinate of the first point of the second line.
	 * @param x4
	 *            is the X coordinate of the second point of the second line.
	 * @param y4
	 *            is the Y coordinate of the second point of the second line.
	 * @param z4
	 *            is the Z coordinate of the second point of the second line.
	 * @return <code>true</code> if the two given lines are parallel.
	 * @see #isCollinearLines(double, double, double, double, double, double, double, double, double, double, double, double)
	 */
	public static boolean isParallelLines(
			double x1, double y1, double z1, double x2, double y2, double z2,
			double x3, double y3, double z3, double x4, double y4, double z4) {
		return FunctionalVector3D.isCollinearVectors(x2 - x1, y2 - y1, z2 - z1, x4 - x3, y4 - y3, z4 - z3);
	}

	/**
	 * Replies if two lines are colinear.
	 * <p>
	 * The given two lines are described respectivaly by two points, i.e. {@code (x1,y1,z1)} and
	 * {@code (x2,y2,z2)} for the first line, and {@code (x3,y3,z3)} and {@code (x4,y4,z4)} for the second line.
	 * <p>
	 * If you are interested to test if the two lines are parallel, see
	 * {@link #isParallelLines(double, double, double, double, double, double, double, double, double, double, double, double)}.
	 * 
	 * @param x1
	 *            is the X coordinate of the first point of the first line.
	 * @param y1
	 *            is the Y coordinate of the first point of the first line.
	 * @param z1
	 *            is the Z coordinate of the first point of the first line.
	 * @param x2
	 *            is the X coordinate of the second point of the first line.
	 * @param y2
	 *            is the Y coordinate of the second point of the first line.
	 * @param z2
	 *            is the Z coordinate of the second point of the first line.
	 * @param x3
	 *            is the X coordinate of the first point of the second line.
	 * @param y3
	 *            is the Y coordinate of the first point of the second line.
	 * @param z3
	 *            is the Z coordinate of the first point of the second line.
	 * @param x4
	 *            is the X coordinate of the second point of the second line.
	 * @param y4
	 *            is the Y coordinate of the second point of the second line.
	 * @param z4
	 *            is the Z coordinate of the second point of the second line.
	 * @return <code>true</code> if the two given lines are collinear.
	 * @see #isParallelLines(double, double, double, double, double, double, double, double, double, double, double, double)
	 * @see Point3f#isCollinearPoints(double, double, double, double, double, double, double, double, double, double)
	 */
	public static boolean isCollinearLines(
			double x1, double y1, double z1, double x2, double y2, double z2,
			double x3, double y3, double z3, double x4, double y4, double z4) {
		return (isParallelLines(x1, y1, z1, x2, y2, z2, x3, y3, z3, x4, y4, z4)
				&& FunctionalPoint3D.isCollinearPoints(x1, y1, z1, x2, y2, z2, x3, y3, z3, MathConstants.EPSILON));
	}

	/**
	 * Replies one position factor for the intersection point between two lines.
	 * <p>
	 * Let line equations for L1 and L2:<br>
	 * <code>L1: P1 + factor1 * (P2-P1)</code><br>
	 * <code>L2: P3 + factor2 * (P4-P3)</code><br>
	 * If lines are intersecting, then<br>
	 * <code>P1 + factor1 * (P2-P1) = P3 + factor2 * (P4-P3)</code>
	 * <p>
	 * This function computes and replies <code>factor1</code>.
	 * 
	 * @param x1 is the first point of the first line.
	 * @param y1 is the first point of the first line.
	 * @param z1 is the first point of the first line.
	 * @param x2 is the second point of the first line.
	 * @param y2 is the second point of the first line.
	 * @param z2 is the second point of the first line.
	 * @param x3 is the first point of the second line.
	 * @param y3 is the first point of the second line.
	 * @param z3 is the first point of the second line.
	 * @param x4 is the second point of the second line.
	 * @param y4 is the second point of the second line.
	 * @param z4 is the second point of the second line.
	 * @return <code>factor1</code> or {@link Double#NaN} if no intersection.
	 */
	public static double computeLineLineIntersectionFactor(
			double x1, double y1, double z1, double x2, double y2, double z2,
			double x3, double y3, double z3, double x4, double y4, double z4) {
		Vector3f a = new Vector3f(x2 - x1, y2 - y1, z2 - z1); 
		Vector3f b = new Vector3f(x4 - x3, y4 - y3, z4 - z3); 
		Vector3f c = new Vector3f(x3 - x1, y3 - y1, z3 - z1); 

		if (MathUtil.isEpsilonZero(c.lengthSquared())) {
			return Double.NaN;
		}

		Vector3D v = a.cross(b);
		if (MathUtil.isEpsilonZero(c.dot(v))) {
			return Double.NaN;
		}

		return FunctionalVector3D.determinant(
				c.getX(), c.getY(), c.getZ(),
				b.getX(), b.getY(), b.getZ(),
				v.getX(), v.getY(), v.getZ()) / c.lengthSquared();
	}

	/**
	 * Replies one position factor for the intersection point between two lines.
	 * <p>
	 * Let line equations for L1 and L2:<br>
	 * <code>L1: P1 + factor1 * (P2-P1)</code><br>
	 * <code>L2: P3 + factor2 * (P4-P3)</code><br>
	 * If lines are intersecting, then<br>
	 * <code>P1 + factor1 * (P2-P1) = P3 + factor2 * (P4-P3)</code>
	 * <p>
	 * This function computes and replies <code>factor1</code> and <code>factor2</code>.
	 * 
	 * @param x1 is the first point of the first line.
	 * @param y1 is the first point of the first line.
	 * @param z1 is the first point of the first line.
	 * @param x2 is the second point of the first line.
	 * @param y2 is the second point of the first line.
	 * @param z2 is the second point of the first line.
	 * @param x3 is the first point of the second line.
	 * @param y3 is the first point of the second line.
	 * @param z3 is the first point of the second line.
	 * @param x4 is the second point of the second line.
	 * @param y4 is the second point of the second line.
	 * @param z4 is the second point of the second line.
	 * @return the tuple (<code>factor1</code>, <code>factor1</code>) or <code>null</code> if no intersection.
	 */
	public static Pair<Double,Double> computeLineLineIntersectionFactors(
			double x1, double y1, double z1, double x2, double y2, double z2,
			double x3, double y3, double z3, double x4, double y4, double z4) {
		Vector3f a = new Vector3f(x2 - x1, y2 - y1, z2 - z1); 
		Vector3f b = new Vector3f(x4 - x3, y4 - y3, z4 - z3); 
		Vector3f c = new Vector3f(x3 - x1, y3 - y1, z3 - z1); 

		if (MathUtil.isEpsilonZero(c.lengthSquared())) {
			return null;
		}

		Vector3D v = a.cross(b);
		if (MathUtil.isEpsilonZero(c.dot(v))) {
			return null;
		}

		double factor1 = FunctionalVector3D.determinant(
				c.getX(), c.getY(), c.getZ(),
				b.getX(), b.getY(), b.getZ(),
				v.getX(), v.getY(), v.getZ()) / c.lengthSquared();

		double factor2 = FunctionalVector3D.determinant(
				c.getX(), c.getY(), c.getZ(),
				a.getX(), a.getY(), a.getZ(),
				v.getX(), v.getY(), v.getZ()) / c.lengthSquared();

		return new Pair<>(new Double(factor1), new Double(factor2));
	}

	/**
	 * Replies the intersection point between two lines.
	 * <p>
	 * Let line equations for L1 and L2:<br>
	 * <code>L1: P1 + factor1 * (P2-P1)</code><br>
	 * <code>L2: P3 + factor2 * (P4-P3)</code><br>
	 * If lines are intersecting, then<br>
	 * <code>P1 + factor1 * (P2-P1) = P3 + factor2 * (P4-P3)</code>
	 * 
	 * @param x1 is the first point of the first line.
	 * @param y1 is the first point of the first line.
	 * @param z1 is the first point of the first line.
	 * @param x2 is the second point of the first line.
	 * @param y2 is the second point of the first line.
	 * @param z2 is the second point of the first line.
	 * @param x3 is the first point of the second line.
	 * @param y3 is the first point of the second line.
	 * @param z3 is the first point of the second line.
	 * @param x4 is the second point of the second line.
	 * @param y4 is the second point of the second line.
	 * @param z4 is the second point of the second line.
	 * @return the intersection point or <code>null</code> if no intersection.
	 */
	public static Point3f computeLineLineIntersection(
			double x1, double y1, double z1, double x2, double y2, double z2,
			double x3, double y3, double z3, double x4, double y4, double z4) {
		double s = computeLineLineIntersectionFactor(x1, y1, z1, x2, y2, z2, x3, y3, z3, x4, y4, z4);
		if (Double.isNaN(s)) {
			return null;
		}
		return new Point3f(
				x1 + s * (x2 - x1),
				y1 + s * (y2 - y1),
				z1 + s * (z2 - z1));
	}

	/**
	 * Replies the intersection point between two lines.
	 * <p>
	 * Let line equations for L1 and L2:<br>
	 * <code>L1: P1 + factor1 * (P2-P1)</code><br>
	 * <code>L2: P3 + factor2 * (P4-P3)</code><br>
	 * If lines are intersecting, then<br>
	 * <code>P1 + factor1 * (P2-P1) = P3 + factor2 * (P4-P3)</code>
	 * 
	 * @param x1 is the first point of the first line.
	 * @param y1 is the first point of the first line.
	 * @param z1 is the first point of the first line.
	 * @param x2 is the second point of the first line.
	 * @param y2 is the second point of the first line.
	 * @param z2 is the second point of the first line.
	 * @param x3 is the first point of the second line.
	 * @param y3 is the first point of the second line.
	 * @param z3 is the first point of the second line.
	 * @param x4 is the second point of the second line.
	 * @param y4 is the second point of the second line.
	 * @param z4 is the second point of the second line.
	 * @return the intersection point or <code>null</code> if no intersection.
	 */
	public static Point3f computeSegmentSegmentIntersection(
			double x1, double y1, double z1, double x2, double y2, double z2,
			double x3, double y3, double z3, double x4, double y4, double z4) {
		Pair<Double, Double> factors = computeLineLineIntersectionFactors(x1, y1, z1, x2, y2, z2, x3, y3, z3, x4, y4, z4);
		if (factors == null) {
			return null;
		}
		double s1 = factors.getA().doubleValue();
		double s2 = factors.getB().doubleValue();
		if (s1 < 0. || s1 > 1. || s2 < 0. || s2 > 1.) {
			return null;
		}
		return new Point3f(
				x1 + s1 * (x2 - x1),
				y1 + s1 * (y2 - y1),
				z1 + s1 * (z2 - z1));
	}

	/** Replies if two lines are intersecting.
	 * 
	 * @param x1 is the first point of the first line.
	 * @param y1 is the first point of the first line.
	 * @param z1 is the first point of the first line.
	 * @param x2 is the second point of the first line.
	 * @param y2 is the second point of the first line.
	 * @param z2 is the second point of the first line.
	 * @param x3 is the first point of the second line.
	 * @param y3 is the first point of the second line.
	 * @param z3 is the first point of the second line.
	 * @param x4 is the second point of the second line.
	 * @param y4 is the second point of the second line.
	 * @param z4 is the second point of the second line.
	 * @return <code>true</code> if the two shapes are intersecting; otherwise
	 * <code>false</code>
	 * @see "http://mathworld.wolfram.com/Line-LineIntersection.html"
	 */
	public static boolean intersectsLineLine(
			double x1, double y1, double z1,
			double x2, double y2, double z2,
			double x3, double y3, double z3,
			double x4, double y4, double z4) {
		double s = computeLineLineIntersectionFactor(x1, y1, z1, x2, y2, z2, x3, y3, z3, x4, y4, z4);
		return !Double.isNaN(s);
	}

	/** Replies if two segments are intersecting.
	 * This function considers that the ends of
	 * the segments are not intersecting.
	 * To include the ends of the segments in the intersection ranges, see
	 * {@link #intersectsSegmentSegmentWithEnds(double, double, double, double, double, double, double, double, double, double, double, double)}
	 * 
	 * @param x1 is the first point of the first segment.
	 * @param y1 is the first point of the first segment.
	 * @param z1 is the first point of the first segment.
	 * @param x2 is the second point of the first segment.
	 * @param y2 is the second point of the first segment.
	 * @param z2 is the second point of the first segment.
	 * @param x3 is the first point of the second segment.
	 * @param y3 is the first point of the second segment.
	 * @param z3 is the first point of the second segment.
	 * @param x4 is the second point of the second segment.
	 * @param y4 is the second point of the second segment.
	 * @param z4 is the second point of the second segment.
	 * @return <code>true</code> if the two shapes are intersecting; otherwise
	 * <code>false</code>
	 * @see #intersectsSegmentSegmentWithEnds(double, double, double, double, double, double, double, double, double, double, double, double)
	 */
	public static boolean intersectsSegmentSegmentWithoutEnds(
			double x1, double y1, double z1,
			double x2, double y2, double z2,
			double x3, double y3, double z3,
			double x4, double y4, double z4) {
		Pair<Double, Double> factors = computeLineLineIntersectionFactors(x1, y1, z1, x2, y2, z2, x3, y3, z3, x4, y4, z4);
		if (factors == null) {
			return false;
		}
		double s1 = factors.getA().doubleValue();
		double s2 = factors.getB().doubleValue();
		return (s1 > 0. && s1 < 1. && s2 > 0. && s2 < 1.);
	}

	/** Replies if two segments are intersecting.
	 * This function considers that the ends of
	 * the segments are intersecting.
	 * To ignore the ends of the segments, see
	 * {@link #intersectsSegmentSegmentWithoutEnds(double, double, double, double, double, double, double, double, double, double, double, double)}.
	 * 
	 * @param x1 is the first point of the first segment.
	 * @param y1 is the first point of the first segment.
	 * @param z1 is the first point of the first segment.
	 * @param x2 is the second point of the first segment.
	 * @param y2 is the second point of the first segment.
	 * @param z2 is the second point of the first segment.
	 * @param x3 is the first point of the second segment.
	 * @param y3 is the first point of the second segment.
	 * @param z3 is the first point of the second segment.
	 * @param x4 is the second point of the second segment.
	 * @param y4 is the second point of the second segment.
	 * @param z4 is the second point of the second segment.
	 * @return <code>true</code> if the two shapes are intersecting; otherwise
	 * <code>false</code>
	 * @see #intersectsSegmentSegmentWithoutEnds(double, double, double, double, double, double, double, double, double, double, double, double)
	 */
	public static boolean intersectsSegmentSegmentWithEnds(
			double x1, double y1, double z1,
			double x2, double y2, double z2,
			double x3, double y3, double z3,
			double x4, double y4, double z4) {
		Pair<Double, Double> factors = computeLineLineIntersectionFactors(x1, y1, z1, x2, y2, z2, x3, y3, z3, x4, y4, z4);
		if (factors == null) {
			return false;
		}
		double s1 = factors.getA().doubleValue();
		double s2 = factors.getB().doubleValue();
		return (s1 >= 0. && s1 <= 1. && s2 >= 0. && s2 <= 1.);
	}

	/**
	 * Compute and replies the perpendicular distance from a point to a line.
	 * <p>
	 * Call the point in space <code>(i,j,k)</code> and let <code>(a,b,c)</code>
	 * and <code>(x,y,z)</code> be two points on the line (call it line A).
	 * The crucial fact we'll use is that the minimum distance between the point
	 * and the line is the perpendicular distance. So we're looking for a point
	 * <code>(L,M,N)</code> which is on A, and such that the line connecting
	 * <code>(L,M,N)</code> and <code>(i,j,k)</code> is perpendicular to A.
	 * 
	 * @param sx1
	 *            is the X coord of the first point of the line
	 * @param sy1
	 *            is the Y coord of the first point of the line
	 * @param sz1
	 *            is the Z coord of the first point of the line
	 * @param sx2
	 *            is the X coord of the second point of the line
	 * @param sy2
	 *            is the Y coord of the second point of the line
	 * @param sz2
	 *            is the Z coord of the second point of the line
	 * @param px
	 *            is the X coord of the point
	 * @param py
	 *            is the Y coord of the point
	 * @param pz
	 *            is the Z coord of the point
	 * @return the distance
	 */
	public static double distanceLinePoint(
			double sx1, double sy1, double sz1, double sx2, double sy2, double sz2,
			double px, double py, double pz) {
		// We use the method of the parallelogram
		// P
		// o------+------o
		// \ | \
		// \ |h \
		// \ | \
		// ------o--+----------o-----------
		// S1 S2

		// vector S1S2 = <vx1, vy1, vz1>
		double vx1 = sx2 - sx1;
		double vy1 = sy2 - sy1;
		double vz1 = sz2 - sz1;

		// vector S1P = <vx2, vy2, vz2>
		double vx2 = px - sx1;
		double vy2 = py - sy1;
		double vz2 = pz - sz1;

		// Let's take the cross product S1S2 X S1P.
		Vector3f cross = new Vector3f();
		FunctionalVector3D.crossProduct(vx1, vy1, vz1, vx2, vy2, vz2, cross);

		// Let's let (a) be the length of S1S2 X S1P.
		double a = cross.length();

		// If you divide (a) by the distance S1S2 you will get the distance of P
		// from line S1S2.
		double s1s2 = Math.sqrt(vx1 * vx1 + vy1 * vy1 + vz1 * vz1);

		// Thus the distance we are looking for is a/s1s2.
		return a / s1s2;
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
	 * @param epsilon the accuracy parameter (distance) must be the same unit of measurement as others parameters 
	 * @return <code>true</code> if the points is on segment,
	 * otherwise <code>false</code>
	 */
	public static boolean containsSegmentPoint(
			double px, double py, double pz,
			double ax, double ay, double az,
			double bx, double by, double bz,
			boolean forceCollinear, double epsilon) {
		double ratio = getPointProjectionFactorOnSegmentLine(px, py, pz, ax, ay, az, bx, by, bz);

		if (ratio>=0. && ratio<=1.) {
			if (forceCollinear) {
				return FunctionalPoint3D.isCollinearPoints(
						ax, ay, az,
						bx, by, bz,
						px, py, pz, epsilon);
			}
			return true;
		}

		return false;
	}

	/**
	 * Compute and replies the perpendicular squared distance from a point to a segment.
	 * 
	 * @param sx1
	 *            is the X coord of the first point of the segment
	 * @param sy1
	 *            is the Y coord of the first point of the segment
	 * @param sz1
	 *            is the Z coord of the first point of the segment
	 * @param sx2
	 *            is the X coord of the second point of the segment
	 * @param sy2
	 *            is the Y coord of the second point of the segment
	 * @param sz2
	 *            is the Z coord of the second point of the segment
	 * @param px
	 *            is the X coord of the point
	 * @param py
	 *            is the Y coord of the point
	 * @param pz
	 *            is the Z coord of the point
	 * @return the distance
	 */
	public static double distanceSquaredSegmentPoint(
			double sx1, double sy1, double sz1, double sx2, double sy2, double sz2,
			double px, double py, double pz) {
		double ratio = getPointProjectionFactorOnSegmentLine(px, py, pz, sx1, sy1, sz1, sx2, sy2, sz2);

		if (ratio <= 0.)
			return FunctionalPoint3D.distanceSquaredPointPoint(px, py, pz, sx1, sy1, sz1);

		if (ratio >= 1.)
			return FunctionalPoint3D.distanceSquaredPointPoint(px, py, pz, sx2, sy2, sz2);

		return FunctionalPoint3D.distanceSquaredPointPoint(
				px, py, pz,
				(1. - ratio) * sx1 + ratio * sx2,
				(1. - ratio) * sy1 + ratio * sy2,
				(1. - ratio) * sz1 + ratio * sz2);
	}

	/**
	 * Computes the L-1 (Manhattan) distance between a segment and
	 * a point.  The L-1 distance is equal to abs(x1-x2) + abs(y1-y2).
	 * 
	 * @param sx1
	 *            is the X coord of the first point of the segment
	 * @param sy1
	 *            is the Y coord of the first point of the segment
	 * @param sz1
	 *            is the Z coord of the first point of the segment
	 * @param sx2
	 *            is the X coord of the second point of the segment
	 * @param sy2
	 *            is the Y coord of the second point of the segment
	 * @param sz2
	 *            is the Z coord of the second point of the segment
	 * @param px
	 *            is the X coord of the point
	 * @param py
	 *            is the Y coord of the point
	 * @param pz
	 *            is the Z coord of the point
	 * @return the distance
	 */
	public static double distanceL1SegmentPoint(
			double sx1, double sy1, double sz1, double sx2, double sy2, double sz2,
			double px, double py, double pz) {
		double ratio = getPointProjectionFactorOnSegmentLine(px, py, pz, sx1, sy1, sz1, sx2, sy2, sz2);

		if (ratio <= 0.)
			return FunctionalPoint3D.distanceL1PointPoint(px, py, pz, sx1, sy1, sz1);

		if (ratio >= 1.)
			return FunctionalPoint3D.distanceL1PointPoint(px, py, pz, sx2, sy2, sz2);

		return FunctionalPoint3D.distanceL1PointPoint(
				px, py, pz,
				(1. - ratio) * sx1 + ratio * sx2,
				(1. - ratio) * sy1 + ratio * sy2,
				(1. - ratio) * sz1 + ratio * sz2);
	}

	/**
	 * Computes the L-infinite distance between a segment and
	 * a point.  The L-infinite distance is equal to 
	 * MAX[abs(x1-x2), abs(y1-y2)]. 
	 * 
	 * @param sx1
	 *            is the X coord of the first point of the segment
	 * @param sy1
	 *            is the Y coord of the first point of the segment
	 * @param sz1
	 *            is the Z coord of the first point of the segment
	 * @param sx2
	 *            is the X coord of the second point of the segment
	 * @param sy2
	 *            is the Y coord of the second point of the segment
	 * @param sz2
	 *            is the Z coord of the second point of the segment
	 * @param px
	 *            is the X coord of the point
	 * @param py
	 *            is the Y coord of the point
	 * @param pz
	 *            is the Z coord of the point
	 * @return the distance
	 */
	public static double distanceLinfSegmentPoint(
			double sx1, double sy1, double sz1, double sx2, double sy2, double sz2,
			double px, double py, double pz) {
		double ratio = getPointProjectionFactorOnSegmentLine(px, py, pz, sx1, sy1, sz1, sx2, sy2, sz2);

		if (ratio <= 0.)
			return FunctionalPoint3D.distanceLinfPointPoint(px, py, pz, sx1, sy1, sz1);

		if (ratio >= 1.)
			return FunctionalPoint3D.distanceLinfPointPoint(px, py, pz, sx2, sy2, sz2);

		return FunctionalPoint3D.distanceLinfPointPoint(
				px, py, pz,
				(1. - ratio) * sx1 + ratio * sx2,
				(1. - ratio) * sy1 + ratio * sy2,
				(1. - ratio) * sz1 + ratio * sz2);
	}

	/**
	 * Compute and replies the perpendicular distance from a point to a segment.
	 * 
	 * @param sx1
	 *            is the X coord of the first point of the segment
	 * @param sy1
	 *            is the Y coord of the first point of the segment
	 * @param sz1
	 *            is the Z coord of the first point of the segment
	 * @param sx2
	 *            is the X coord of the second point of the segment
	 * @param sy2
	 *            is the Y coord of the second point of the segment
	 * @param sz2
	 *            is the Z coord of the second point of the segment
	 * @param px
	 *            is the X coord of the point
	 * @param py
	 *            is the Y coord of the point
	 * @param pz
	 *            is the Z coord of the point
	 * @return the distance
	 */
	public static double distanceSegmentPoint(
			double sx1, double sy1, double sz1, double sx2, double sy2, double sz2,
			double px, double py, double pz) {
		double ratio = getPointProjectionFactorOnSegmentLine(px, py, pz, sx1, sy1, sz1, sx2, sy2, sz2);

		if (ratio <= 0.)
			return FunctionalPoint3D.distancePointPoint(px, py, pz, sx1, sy1, sz1);

		if (ratio >= 1.)
			return FunctionalPoint3D.distancePointPoint(px, py, pz, sx2, sy2, sz2);

		return FunctionalPoint3D.distancePointPoint(
				px, py, pz,
				(1. - ratio) * sx1 + ratio * sx2,
				(1. - ratio) * sy1 + ratio * sy2,
				(1. - ratio) * sz1 + ratio * sz2);
	}

	/**
	 * Compute and replies the perpendicular distance from a point to a line.
	 * <p>
	 * Call the point in space <code>(i,j,k)</code> and let <code>(a,b,c)</code>
	 * and <code>(x,y,z)</code> be two points on the line (call it line A).
	 * The crucial fact we'll use is that the minimum distance between the point
	 * and the line is the perpendicular distance. So we're looking for a point
	 * <code>(L,M,N)</code> which is on A, and such that the line connecting
	 * <code>(L,M,N)</code> and <code>(i,j,k)</code> is perpendicular to A.
	 * 
	 * @param sx1
	 *            is the X coord of the first point of the line
	 * @param sy1
	 *            is the Y coord of the first point of the line
	 * @param sz1
	 *            is the Z coord of the first point of the line
	 * @param sx2
	 *            is the X coord of the second point of the line
	 * @param sy2
	 *            is the Y coord of the second point of the line
	 * @param sz2
	 *            is the Z coord of the second point of the line
	 * @param px
	 *            is the X coord of the point
	 * @param py
	 *            is the Y coord of the point
	 * @param pz
	 *            is the Z coord of the point
	 * @return the distance
	 */
	public static double distanceSquaredLinePoint(
			double sx1, double sy1, double sz1, double sx2, double sy2, double sz2,
			double px, double py, double pz) {
		double d = distanceLinePoint(sx1, sy1, sz1, sx2, sy2, sz2, px, py, pz);
		return d * d;
	}


	/**
	 * Replies the projection a point on a segment.
	 * 
	 * @param px
	 *            is the coordiante of the point to project
	 * @param py
	 *            is the coordiante of the point to project
	 * @param pz
	 *            is the coordiante of the point to project
	 * @param s1x
	 *            is the x-coordinate of the first line point.
	 * @param s1y
	 *            is the y-coordinate of the first line point.
	 * @param s1z
	 *            is the z-coordinate of the first line point.
	 * @param s2x
	 *            is the x-coordinate of the second line point.
	 * @param s2y
	 *            is the y-coordinate of the second line point.
	 * @param s2z
	 *            is the z-coordinate of the second line point.
	 * @return the projection of the specified point on the line.
	 * If equal to {@code 0}, the projection is equal to the first segment point.
	 * If equal to {@code 1}, the projection is equal to the second segment point.
	 * If inside {@code ]0;1[}, the projection is between the two segment points.
	 * If inside {@code ]-inf;0[}, the projection is outside on the side of the first segment point.
	 * If inside {@code ]1;+inf[}, the projection is outside on the side of the second segment point.
	 */
	public static double getPointProjectionFactorOnSegmentLine(
			double px, double py, double pz,
			double s1x, double s1y, double s1z,
			double s2x, double s2y, double s2z) {
		double dx = s2x - s1x;
		double dy = s2y - s1y;
		double dz = s2z - s1z;

		if (dx == 0. && dy == 0. && dz == 0.)
			return 0.;

		return ((px - s1x) * dx + (py - s1y) * dy + (pz - s1z) * dz) / (dx * dx + dy * dy + dz * dz);
	}


	/**
	 * Compute and replies the distance between two segments.
	 * 
	 * @param ax1
	 *            is the X coord of the first point of the first segment
	 * @param ay1
	 *            is the Y coord of the first point of the first segment
	 * @param az1
	 *            is the Z coord of the first point of the first segment
	 * @param ax2
	 *            is the X coord of the second point of the first segment
	 * @param ay2
	 *            is the Y coord of the second point of the first segment
	 * @param az2
	 *            is the Z coord of the second point of the first segment
	 * @param bx1
	 *            is the X coord of the first point of the second segment
	 * @param by1
	 *            is the Y coord of the first point of the second segment
	 * @param bz1
	 *            is the Z coord of the first point of the second segment
	 * @param bx2
	 *            is the X coord of the second point of the second segment
	 * @param by2
	 *            is the Y coord of the second point of the second segment
	 * @param bz2
	 *            is the Z coord of the second point of the second segment
	 * @return the distance
	 */
	public static double distanceSegmentSegment(
			double ax1, double ay1, double az1, double ax2, double ay2, double az2,
			double bx1, double by1, double bz1, double bx2, double by2, double bz2) {
		return Math.sqrt(distanceSquaredSegmentSegment(
				ax1, ay1, az1, ax2, ay2, az2,
				bx1, by1, bz1, bx2, by2, bz2));
	}

	/**
	 * Compute and replies the squared distance between two segments.
	 * 
	 * @param ax1
	 *            is the X coord of the first point of the first segment
	 * @param ay1
	 *            is the Y coord of the first point of the first segment
	 * @param az1
	 *            is the Z coord of the first point of the first segment
	 * @param ax2
	 *            is the X coord of the second point of the first segment
	 * @param ay2
	 *            is the Y coord of the second point of the first segment
	 * @param az2
	 *            is the Z coord of the second point of the first segment
	 * @param bx1
	 *            is the X coord of the first point of the second segment
	 * @param by1
	 *            is the Y coord of the first point of the second segment
	 * @param bz1
	 *            is the Z coord of the first point of the second segment
	 * @param bx2
	 *            is the X coord of the second point of the second segment
	 * @param by2
	 *            is the Y coord of the second point of the second segment
	 * @param bz2
	 *            is the Z coord of the second point of the second segment
	 * @return the squared distance
	 */
	public static double distanceSquaredSegmentSegment(
			double ax1, double ay1, double az1, double ax2, double ay2, double az2,
			double bx1, double by1, double bz1, double bx2, double by2, double bz2) {
		Vector3f u = new Vector3f(ax2 - ax1, ay2 - ay1, az2 - az1);
		Vector3f v = new Vector3f(bx2 - bx1, by2 - by1, bz2 - bz1);
		Vector3f w = new Vector3f(ax1 - bx1, ay1 - by1, az1 - bz1);
		double a = u.dot(u);
		double b = u.dot(v);
		double c = v.dot(v);
		double d = u.dot(w);
		double e = v.dot(w);
		double D = a * c - b * b;
		
		double sc, sN, tc, tN;
		double sD = D;
		double tD = D;

		// compute the line parameters of the two closest points
		if (MathUtil.isEpsilonZero(D)) { 
			// the lines are almost parallel
			// force using point P0 on segment S1
			// to prevent possible division by 0.0 later
			sN = 0.;
			sD = 1.;         
			tN = e;
			tD = c;
		}
		else {
			// get the closest points on the infinite lines
			sN = b*e - c*d;
			tN = a*e - b*d;
			if (sN < 0.) {
				// sc < 0 => the s=0 edge is visible
				sN = 0.;
				tN = e;
				tD = c;
			} else if (sN > sD) {
				// sc > 1  => the s=1 edge is visible
				sN = sD;
				tN = e + b;
				tD = c;
			}
		}

		if (tN < 0.) {
			// tc < 0 => the t=0 edge is visible
			tN = 0.;
			// recompute sc for this edge
			if (-d < 0.)
				sN = 0.;
			else if (-d > a)
				sN = sD;
			else {
				sN = -d;
				sD = a;
			}
		}
		else if (tN > tD) {
			// tc > 1  => the t=1 edge is visible
			tN = tD;
			// recompute sc for this edge
			if ((-d + b) < 0.)
				sN = 0;
			else if ((-d + b) > a)
				sN = sD;
			else {
				sN = (-d +  b);
				sD = a;
			}
		}
		// finally do the division to get sc and tc
		sc = (MathUtil.isEpsilonZero(sN) ? 0. : sN / sD);
		tc = (MathUtil.isEpsilonZero(tN) ? 0. : tN / tD);

		// get the difference of the two closest points
		// =  S1(sc) - S2(tc)
		
		// reuse u, v, w
		u.scale(sc);
		w.add(u);
		v.scale(tc);
		w.sub(v);

		return w.lengthSquared();
	}

//	/** First point on the line.
//	 */
//	protected final Point3f pivot = new Point3f();
//
//	/** Direction vector.
//	 */
//	protected final Vector3f d = new Vector3f();
//
//	/**
//	 */
//	public AbstractSegment3F() {
//		super();
//	}
//
//	/**
//	 * @param p1 is first point on the line
//	 * @param p2 is second point on the line
//	 */
//	public AbstractSegment3F(Point3D p1, Point3D p2) {
//		this.pivot.set(p1);
//		this.d.sub(p2, p1);
//	}
//
//	/**
//	 * @param pivot1 is a point on the line
//	 * @param direction is the direction of the line
//	 */
//	public AbstractSegment3F(Point3D pivot1, Vector3D direction) {
//		this.pivot.set(pivot1);
//		this.d.set(direction);
//	}
//
//	/**
//	 * @param x1 x coordinate of the first point of the segment.
//	 * @param y1 y coordinate of the first point of the segment.
//	 * @param z1 z coordinate of the first point of the segment.
//	 * @param x2 x coordinate of the second point of the segment.
//	 * @param y2 y coordinate of the second point of the segment.
//	 * @param z2 z coordinate of the second point of the segment.
//	 */
//	public AbstractSegment3F(double x1, double y1, double z1, double x2, double y2, double z2) {
//		this.pivot.set(x1, y1, z1);
//		this.d.set(x2 - x1, y2 - y1, z2 - z1);
//	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuilder b = new StringBuilder();
		b.append("]p:("); //$NON-NLS-1$
		b.append(this.getP1().getX());
		b.append(";"); //$NON-NLS-1$
		b.append(this.getP1().getY());
		b.append(";"); //$NON-NLS-1$
		b.append(this.getP1().getZ());
		b.append("),v:("); //$NON-NLS-1$
		b.append(this.getSegmentVector().getX());
		b.append(";"); //$NON-NLS-1$
		b.append(this.getSegmentVector().getY());
		b.append(";"); //$NON-NLS-1$
		b.append(this.getSegmentVector().getZ());
		b.append(")["); //$NON-NLS-1$
		return b.toString();
	}

	/**
	 * Replies the vector that corresponds to the segment.
	 * The vector is from P1 to P2, where P1 is the first point
	 * of the segment. And, P2 is the second point of the segment.
	 * 
	 * @return the vector from P1 to P2. It is not a unit vector.
	 */
	abstract public FunctionalVector3D getSegmentVector();

	
	/**
	 * Replies a copy of the vector that corresponds to the segment.
	 * The vector is from P1 to P2, where P1 is the first point
	 * of the segment. And, P2 is the second point of the segment.
	 * 
	 * @return the copy of the vector from P1 to P2. It is not a unit vector.
	 */
	abstract public FunctionalVector3D getCloneSegmentVector();
	
	/**
	 * Replies the direction of the segment.
	 * The vector is the unit vector that is colinear to the
	 * line from P1 to P2, where P1 is the first point
	 * of the segment, P2 the second point of the segment.
	 * 
	 * @return the unit vector of the direction of the segment..
	 */
	abstract public FunctionalVector3D getDirection();

	/** Set the points of the line.
	 *
	 * @param p1 the first point.
	 * @param p2 the second point.
	 */
	abstract public void set(Point3D p1, Point3D p2);

	/** Set the points of the line.
	 *
	 * @param x1 the first point.
	 * @param y1 the first point.
	 * @param z1 the first point.
	 * @param x2 the second point.
	 * @param y2 the second point.
	 * @param z2 the second point.
	 */
	abstract public void set(double x1, double y1, double z1, double x2, double y2, double z2);

	/**
	 * Replies the first point.
	 * 
	 * @return the point on the line.
	 */
	abstract public FunctionalPoint3D getP1();

	/**
	 * Replies the x coordinate of the first point.
	 * 
	 * @return x coordinate of the first point on the line.
	 */
	abstract public double getX1();

	/**
	 * Replies the y coordinate of the first point.
	 * 
	 * @return y coordinate of the first point on the line.
	 */
	abstract public double getY1();

	/**
	 * Replies the z coordinate of the first point.
	 * 
	 * @return z coordinate of the first point on the line.
	 */
	abstract public double getZ1();

	/**
	 * Set the first point.
	 * 
	 * @param p the point on the line.
	 */
	abstract public void setP1(Point3D p);

	/**
	 * Set the first point.
	 * 
	 * @param x
	 * @param y
	 * @param z
	 */
	abstract public void setP1(double x, double y, double z);

	/**
	 * Set the second point.
	 * 
	 * @param x
	 * @param y
	 * @param z
	 */
	abstract public void setP2(double x, double y, double z);

	/**
	 * Replies the second point.
	 * 
	 * @return the point on the line.
	 */
	abstract public FunctionalPoint3D getP2();

	/**
	 * Replies the x coordinate of the second point.
	 * 
	 * @return x coordinate of the second point on the line.
	 */
	abstract public double getX2();

	/**
	 * Replies the y coordinate of the second point.
	 * 
	 * @return y coordinate of the second point on the line.
	 */
	abstract public double getY2();

	/**
	 * Replies the z coordinate of the second point.
	 * 
	 * @return y coordinate of the second point on the line.
	 */
	abstract public double getZ2();

	/**
	 * Set the second point.
	 * 
	 * @param p the point on the line.
	 */
	abstract public void setP2(Point3D p);

	/** Replies the distance between this segment and the given point.
	 *
	 * @param point
	 * @return the distance.
	 */
	public double distanceSegment(Point3D point) {
		return distanceSegmentPoint(
				getX1(), getY1(), getZ1(),
				getX2(), getY2(), getZ2(),
				point.getX(), point.getY(), point.getZ());
	}

	/** Replies the distance between the line of this segment and the given point.
	 *
	 * @param point
	 * @return the distance.
	 */
	public double distanceLine(Point3D point) {
		return distanceLinePoint(
				getX1(), getY1(), getZ1(),
				getX2(), getY2(), getZ2(),
				point.getX(), point.getY(), point.getZ());
	}

	/** Replies the squared distance between this segment and the given point.
	 *
	 * @param point
	 * @return the squared distance.
	 */
	public double distanceSquaredSegment(Point3D point) {
		return distanceSquaredSegmentPoint(
				getX1(), getY1(), getZ1(),
				getX2(), getY2(), getZ2(),
				point.getX(), point.getY(), point.getZ());
	}

	/** Replies the squared distance between the line of this segment and the given point.
	 *
	 * @param point
	 * @return the squared distance.
	 */
	public double distanceSquaredLine(Point3D point) {
		return distanceSquaredLinePoint(
				getX1(), getY1(), getZ1(),
				getX2(), getY2(), getZ2(),
				point.getX(), point.getY(), point.getZ());
	}

	@Override
	public AlignedBox3f toBoundingBox() {
		AlignedBox3f b = new AlignedBox3f();
		toBoundingBox(b);
		return b;
	}

	@Override
	public void toBoundingBox(AbstractBoxedShape3F<?> box) {
		box.setFromCorners(getX1(), getY1(), getZ1(), getX2(), getY2(), getZ2());
	}

	@Override
	public double distanceSquared(Point3D p) {
		return distanceSquaredSegmentPoint(
				getX1(), getY1(), getZ1(),
				getX2(), getY2(), getZ2(),
				p.getX(), p.getY(), p.getZ());
	}

	@Override
	public double distanceL1(Point3D p) {
		return distanceL1SegmentPoint(
				getX1(), getY1(), getZ1(),
				getX2(), getY2(), getZ2(),
				p.getX(), p.getY(), p.getZ());
	}

	@Override
	public double distanceLinf(Point3D p) {
		return distanceLinfSegmentPoint(
				getX1(), getY1(), getZ1(),
				getX2(), getY2(), getZ2(),
				p.getX(), p.getY(), p.getZ());
	}

	@Override
	public void transform(Transform3D transformationMatrix) {
		transformationMatrix.transform(this.getP1());
		transformationMatrix.transform(this.getSegmentVector());
	}

	@Override
	public void translate(double dx, double dy, double dz) {
		this.getP1().add(dx, dy, dz);
	}

	/** Rotate the segment around its pivot point.
	 * The pivot point is the first point of the segment.
	 *
	 * @param rotation the rotation.
	 */
	public void rotate(Quaternion rotation) {
		Transform3D m = new Transform3D();
		m.setRotation(rotation);
		m.transform(this.getSegmentVector());
		this.getSegmentVector().normalize();
	}

	/** Rotate the segment around a given pivot point.
	 * The default pivot point of the segment is its first point.
	 *
	 * @param rotation the rotation.
	 * @param pivot1 the pivot point. If <code>null</code> the default pivot point is used.
	 */
	public void rotate(Quaternion rotation, Point3D pivot1) {
		if (pivot1==null) {
			rotate(rotation);
		} else {
			Transform3D m = new Transform3D();
			m.setRotation(rotation);

			// Translate to the pivot
			this.getP1().setX(this.getP1().getX() - pivot1.getX());
			this.getP1().setY(this.getP1().getY() - pivot1.getY());
			this.getP1().setZ(this.getP1().getZ() - pivot1.getZ());

			m.transform(this.getP1());

			this.getP1().setX(this.getP1().getX() + pivot1.getX());
			this.getP1().setY(this.getP1().getY() + pivot1.getY());
			this.getP1().setZ(this.getP1().getZ() + pivot1.getZ());

			// Rotate the direction
			m.transform(this.getSegmentVector());
			this.getSegmentVector().normalize();
		}
	}

	@Override
	public boolean contains(double x, double y, double z) {
		return containsSegmentPoint(
				x, y, z,
				getX1(), getY1(), getZ1(),
				getX2(), getY2(), getZ2(),
				true,
				MathConstants.EPSILON);
	}

	@Override
	public boolean intersects(AlignedBox3f s) {
		return intersectsSegmentAlignedBox(
				getX1(), getY1(), getZ1(),
				getX2(), getY2(), getZ2(),
				s.getMinX(), s.getMinY(), s.getMinZ(),
				s.getMaxX(), s.getMaxY(), s.getMaxZ());
	}

	@Override
	public boolean intersects(AbstractSphere3F s) {
		return AbstractSphere3F.intersectsSphereSegment(
				s.getX(), s.getY(), s.getZ(), s.getRadius(),
				getX1(), getY1(), getZ1(),
				getX2(), getY2(), getZ2());
	}

	@Override
	public boolean intersects(AbstractSegment3F s) {
		return intersectsSegmentSegmentWithoutEnds(
				getX1(), getY1(), getZ1(),
				getX2(), getY2(), getZ2(),
				s.getX1(), s.getY1(), s.getZ1(),
				s.getX2(), s.getY2(), s.getZ2());
	}

	@Override
	public boolean intersects(Triangle3f s) {
		return AbstractTriangle3F.intersectsTriangleSegment(
				s.getX1(), s.getY1(), s.getZ1(),
				s.getX2(), s.getY2(), s.getZ2(),
				s.getX3(), s.getY3(), s.getZ3(),
				getX1(), getY1(), getZ1(),
				getX2(), getY2(), getZ2());
	}

	@Override
	public boolean intersects(AbstractCapsule3F s) {
		return intersectsSegmentCapsule(
				getX1(), getY1(), getZ1(),
				getX2(), getY2(), getZ2(),
				s.getMedialX1(), s.getMedialY1(), s.getMedialZ1(),
				s.getMedialX2(), s.getMedialY2(), s.getMedialZ2(),
				s.getRadius());
	}

	@Override
	public boolean intersects(AbstractOrientedBox3F s) {
		return intersectsSegmentOrientedBox(
				getX1(), getY1(), getZ1(),
				getX2(), getY2(), getZ2(),
				s.getCenterX(), s.getCenterY(), s.getCenterZ(),
				s.getFirstAxisX(), s.getFirstAxisY(), s.getFirstAxisZ(),
				s.getSecondAxisX(), s.getSecondAxisY(), s.getSecondAxisZ(),
				s.getThirdAxisX(), s.getThirdAxisY(), s.getThirdAxisZ(),
				s.getFirstAxisExtent(), s.getSecondAxisExtent(), s.getThirdAxisExtent());
	}

	@Override
	public boolean intersects(Plane3D<?> p) {
		Vector3f n = p.getNormal();
		double s = n.dot(this.getSegmentVector());
		return !MathUtil.isEpsilonZero(s);
	}

	@Override
	public boolean isEmpty() {
		return this.getSegmentVector().lengthSquared() == 0.;
	}

	@Override
	public void clear() {
		this.getP1().set(0., 0., 0.);
		this.getSegmentVector().set(0., 0., 0.);
	}

	@Override
	public Point3D getClosestPointTo(Point3D p) {
		double ratio = getPointProjectionFactorOnSegmentLine(
				p.getX(), p.getY(), p.getZ(),
				getX1(), getY1(), getZ1(),
				getX2(), getY2(), getZ2());
		if (ratio <= 0.) {
			return this.getP1().clone();
		}
		if (ratio >= 1.) {
			return new Point3f(
					this.getP1().getX() + this.getSegmentVector().getX(),
					this.getP1().getY() + this.getSegmentVector().getY(),
					this.getP1().getZ() + this.getSegmentVector().getZ());
		}
		return new Point3f(
				this.getP1().getX() + ratio * this.getSegmentVector().getX(),
				this.getP1().getY() + ratio * this.getSegmentVector().getY(),
				this.getP1().getZ() + ratio * this.getSegmentVector().getZ());
	}

	@Override
	public Point3D getFarthestPointTo(Point3D p) {
		double ratio = getPointProjectionFactorOnSegmentLine(
				p.getX(), p.getY(), p.getZ(),
				getX1(), getY1(), getZ1(),
				getX2(), getY2(), getZ2());
		if (ratio <= .5) {
			return new Point3f(
					this.getP1().getX() + this.getSegmentVector().getX(),
					this.getP1().getY() + this.getSegmentVector().getY(),
					this.getP1().getZ() + this.getSegmentVector().getZ());
		}
		return this.getP1().clone();
	}

	@Override
	public void set(Shape3F s) {
		if (s instanceof AbstractSegment3F) {
			AbstractSegment3F g = (AbstractSegment3F) s;
			set(g.getX1(), g.getY1(), g.getZ1(), g.getX2(), g.getY2(), g.getZ2());
		} else {
			AbstractBoxedShape3F<?> r = s.toBoundingBox();
			this.getP1().set(r.getMinX(), r.getMinY(), r.getMinZ());
			this.getSegmentVector().set(r.getSizeX(), r.getSizeY(), r.getSizeZ());
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj instanceof AbstractSegment3F) {
			AbstractSegment3F s3f = (AbstractSegment3F) obj;
			return ((getX1() == s3f.getX1()) &&
					(getY1() == s3f.getY1()) &&
					(getZ1() == s3f.getZ1()) &&
					(getX2() == s3f.getX2()) &&
					(getY2() == s3f.getY2()) &&
					(getZ2() == s3f.getZ2()));
		}
		return false;
	}

	@Override
	public int hashCode() {
		long bits = 1L;
		bits = 31L * bits + doubleToLongBits(getX1());
		bits = 31L * bits + doubleToLongBits(getY1());
		bits = 31L * bits + doubleToLongBits(getZ1());
		bits = 31L * bits + doubleToLongBits(getX2());
		bits = 31L * bits + doubleToLongBits(getY2());
		bits = 31L * bits + doubleToLongBits(getZ2());
		return (int) (bits ^ (bits >> 32));
	}

}
