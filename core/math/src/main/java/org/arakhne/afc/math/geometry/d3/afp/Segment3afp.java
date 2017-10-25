/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2018 The original authors, and other authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.arakhne.afc.math.geometry.d3.afp;

import java.util.NoSuchElementException;

import org.eclipse.xtext.xbase.lib.Pair;
import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.MathConstants;
import org.arakhne.afc.math.MathUtil;
import org.arakhne.afc.math.Unefficient;
import org.arakhne.afc.math.geometry.CrossingComputationType;
import org.arakhne.afc.math.geometry.PathWindingRule;
import org.arakhne.afc.math.geometry.coordinatesystem.CoordinateSystem3D;
import org.arakhne.afc.math.geometry.d2.Vector2D;
import org.arakhne.afc.math.geometry.d3.Point3D;
import org.arakhne.afc.math.geometry.d3.Transform3D;
import org.arakhne.afc.math.geometry.d3.Vector3D;
import org.arakhne.afc.vmutil.asserts.AssertMessages;


/** Fonctional interface that represented a 2D segment/line on a plane.
 *
 * @param <ST> is the type of the general implementation.
 * @param <IT> is the type of the implementation of this shape.
 * @param <IE> is the type of the path elements.
 * @param <P> is the type of the points.
 * @param <V> is the type of the vectors.
 * @param <B> is the type of the bounding boxes.
 * @author $Author: sgalland$
 * @author $Author: hjaffali$
 * @author $Author: tpiotrow$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@SuppressWarnings({"checkstyle:methodcount", "checkstyle:parameternumber"})
public interface Segment3afp<
			ST extends Shape3afp<?, ?, IE, P, V, B>,
			IT extends Segment3afp<?, ?, IE, P, V, B>,
			IE extends PathElement3afp,
			P extends Point3D<? super P, ? super V>,
			V extends Vector3D<? super V, ? super P>,
			B extends RectangularPrism3afp<?, ?, IE, P, V, B>>
			extends Shape3afp<ST, IT, IE, P, V, B> {

	/**
	 * Replies if two lines are colinear.
	 *
	 *<p>The given two lines are described respectivaly by two points, i.e. {@code (x1,y1)} and {@code (x2,y2)} for the first
	 * line, and {@code (x3,y3)} and {@code (x4,y4)} for the second line.
	 *
	 * <p>If you are interested to test if the two lines are parallel, see
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
	 * @see Point3D#isCollinearPoints(double, double, double, double, double, double, double, double, double)
	 */
	@Pure
	static boolean isCollinearLines(double x1, double y1, double z1, double x2, double y2, double z2, double x3, double y3,
			double z3, double x4, double y4, double z4) {
		return isParallelLines(x1, y1, z1, x2, y2, z2, x3, y3, z3, x4, y4, z4)
				&& Point3D.isCollinearPoints(x1, y1, z1, x2, y2, z2, x3, y3, z3);
	}

	/**
	 * Replies if two lines are parallel.
	 *
	 * <p>The given two lines are described respectivaly by two points, i.e. {@code (x1,y1)} and {@code (x2,y2)} for the first
	 * line, and {@code (x3,y3)} and {@code (x4,y4)} for the second line.
	 *
	 * <p>If you are interested to test if the two lines are colinear, see
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
	@Pure
	static boolean isParallelLines(double x1, double y1, double z1, double x2, double y2, double z2, double x3, double y3,
			double z3, double x4, double y4, double z4) {
		return Vector3D.isCollinearVectors(x2 - x1, y2 - y1, z2 - z1, x4 - x3, y4 - y3, z4 - z3);
	}

	/**
	 * Replies the intersection point between two segments.
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
	 * @param result the intersection point.
	 * @return <code>true</code> if an intersection exists.
	 */
	@Pure
	static boolean computeSegmentSegmentIntersection(double x1, double y1, double z1, double x2, double y2, double z2,
			double x3, double y3, double z3, double x4, double y4, double z4,
			Point3D<?, ?> result) {

		final Pair<Double, Double> factors = computeSegmentSegmentIntersectionFactors(x1, y1, z1, x2, y2, z2, x3, y3, z3, x4, y4,
				z4);
		if (factors == null) {
			return false;
		}
		final double s1 = factors.getKey().doubleValue();
		final double s2 = factors.getValue().doubleValue();
		if (s1 < 0. || s1 > 1. || s2 < 0. || s2 > 1.) {
			return false;
		}
		result.set(
				x1 + s1 * (x2 - x1),
				y1 + s1 * (y2 - y1),
				z1 + s1 * (z2 - z1));
		return true;
	}

	/**
	 * Replies one position factor for the intersection point between two lines.
	 *
	 * <p>Let line equations for L1 and L2:<br>
	 * <code>L1: P1 + factor1 * (P2-P1)</code><br>
	 * <code>L2: P3 + factor2 * (P4-P3)</code><br>
	 * If lines are intersecting, then<br>
	 * <code>P1 + factor1 * (P2-P1) = P3 + factor2 * (P4-P3)</code>
	 *
	 *<p>This function computes and replies <code>factor1</code>.
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
	 * @return <code>factor1</code> or {@link Double#NaN} if no intersection.
	 */
	@Pure
	static double computeSegmentSegmentIntersectionFactor(double x1, double y1, double z1, double x2, double y2, double z2,
			double x3, double y3, double z3, double x4, double y4, double z4) {
		final double x21 = x2 - x1;
		final double y21 = y2 - y1;
		final double z21 = z2 - z1;
		final double x43 = x4 - x3;
		final double y43 = y4 - y3;
		final double z43 = z4 - z3;
		final double x31 = x3 - x1;
		final double y31 = y3 - y1;
		final double z31 = z3 - z1;
		final double x;
		final double y;
		final double z;

		if (CoordinateSystem3D.getDefaultCoordinateSystem().isLeftHanded()) {
			x = y43 * z21 - z43 * y21;
			y = z43 * x21 - x43 * z21;
			z = x43 * y21 - y43 * x21;
		} else {
			x = y21 * z43 - z21 * y43;
			y = z21 * x43 - x21 * z43;
			z = x21 * y43 - y21 * x43;
		}

		if (MathUtil.isEpsilonZero(x * x + y * y + z * z)) {
			return Double.NaN;
		}

		if (MathUtil.isEpsilonZero(Vector3D.dotProduct(x31, y31, z31, x, y, z))) {
			return Double.NaN;
		}

		return Vector3D.determinant(
				x31, y31, z31,
				x43, y43, z43,
				x, y, z) / (x * x + y * y + z * z);
	}

	/**
	 * Replies one position factor for the intersection point between two lines.
	 *
	 * <p>Let line equations for L1 and L2:<br>
	 * <code>L1: P1 + factor1 * (P2-P1)</code><br>
	 * <code>L2: P3 + factor2 * (P4-P3)</code><br>
	 * If lines are intersecting, then<br>
	 * <code>P1 + factor1 * (P2-P1) = P3 + factor2 * (P4-P3)</code>
	 *
	 * <p>This function computes and replies <code>factor1</code>.
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
	 * @return <code>factor1</code> or {@link Double#NaN} if no intersection.
	 */
	@Pure
	static Pair<Double, Double> computeSegmentSegmentIntersectionFactors(double x1, double y1, double z1, double x2, double y2,
			double z2, double x3, double y3, double z3, double x4, double y4, double z4) {
		final double x21 = x2 - x1;
		final double y21 = y2 - y1;
		final double z21 = z2 - z1;
		final double x43 = x4 - x3;
		final double y43 = y4 - y3;
		final double z43 = z4 - z3;
		final double x31 = x3 - x1;
		final double y31 = y3 - y1;
		final double z31 = z3 - z1;
		final double x;
		final double y;
		final double z;

		if (CoordinateSystem3D.getDefaultCoordinateSystem().isLeftHanded()) {
			x = y43 * z21 - z43 * y21;
			y = z43 * x21 - x43 * z21;
			z = x43 * y21 - y43 * x21;
		} else {
			x = y21 * z43 - z21 * y43;
			y = z21 * x43 - x21 * z43;
			z = x21 * y43 - y21 * x43;
		}

		if (MathUtil.isEpsilonZero(x * x + y * y + z * z)) {
			return null;
		}

		if (MathUtil.isEpsilonZero(Vector3D.dotProduct(x31, y31, z31, x, y, z))) {
			return null;
		}

		final double factor1 = Vector3D.determinant(
				x31, y31, z31,
				x43, y43, z43,
				x, y, z) / (x * x + y * y + z * z);

		final double factor2 = Vector3D.determinant(
				x31, y31, z31,
				x21, y21, z21,
				x, y, z) / (x * x + y * y + z * z);

		return new Pair<>(Double.valueOf(factor1), Double.valueOf(factor2));
	}

	/**
	 * Replies the position factory for the intersection point between two lines.
	 *
	 * <p>Let line equations for L1 and L2:<br>
	 * <code>L1: P1 + factor1 * (P2-P1)</code><br>
	 * <code>L2: P3 + factor2 * (P4-P3)</code><br>
	 * If lines are intersecting, then<br>
	 * <code>P1 + factor1 * (P2-P1) = P3 + factor2 * (P4-P3)</code>
	 *
	 * <p>This function computes and replies <code>factor1</code>.
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
	 * @return <code>factor1</code> or {@link Double#NaN} if no intersection.
	 */
	@Pure
	static double computeLineLineIntersectionFactor(double x1, double y1, double z1, double x2, double y2, double z2,
			double x3, double y3, double z3, double x4, double y4, double z4) {
		final double vx1 = x2 - x1;
		final double vy1 = y2 - y1;
		final double vx2 = x4 - x3;
		final double vy2 = y4 - y3;

		// determinant is zero when parallel = det(L1,L2)
		final double det = Vector2D.perpProduct(vx1, vy1, vx2, vy2);
		if (det == 0.) {
			return Double.NaN;
		}

		// Given line equations:
		// Pa = P1 + ua (P2-P1), and
		// Pb = P3 + ub (P4-P3)
		// and
		// V = (P1-P3)
		// then
		// ua = det(L2,V) / det(L1,L2)
		// ub = det(L1,V) / det(L1,L2)
		return Vector2D.perpProduct(vx2, vy2, x1 - x3, y1 - y3) / det;
	}

	/** Compute the intersection of two lines specified
	 * by the specified points and vectors.
	 *
	 * @param x1 x position of the first point of the line.
	 * @param y1 y position of the first point of the line.
	 * @param z1 z position of the first point of the line.
	 * @param x2 x position of the second point of the line.
	 * @param y2 y position of the second point of the line.
	 * @param z2 z position of the second point of the line.
	 * @param x3 x position of the first point of the line.
	 * @param y3 y position of the first point of the line.
	 * @param z3 z position of the first point of the line.
	 * @param x4 x position of the second point of the line.
	 * @param y4 y position of the second point of the line.
	 * @param z4 z position of the second point of the line.
	 * @param result the intersection point.
	 * @return <code>true</code> if there is an intersection.
	 */
	@Pure
	static boolean computeLineLineIntersection(double x1, double y1, double z1, double x2, double y2, double z2,
			double x3, double y3, double z3, double x4, double y4, double z4,
			Point3D<?, ?> result) {
		assert result != null : AssertMessages.notNullParameter();
		final double x21 = x2 - x1;
		final double x43 = x4 - x3;
		final double y21 = y2 - y1;
		final double y43 = y4 - y3;
		final double z21 = z2 - z1;
		//TODO final double z43 = z4 - z3;

		final double denom = y43 * x21 - x43 * y21;
		if (denom == 0.) {
			return false;
		}
		final double x13 = x1 - x3;
		final double y13 = y1 - y3;
		//TODO final double z13 = z1 - z3;
		double intersectionFactor1 = x43 * y13 - y43 * x13;
		final double intersectionFactor2 = x21 * y13 - y21 * x13;
		if (intersectionFactor1 == intersectionFactor2) {
			return false;
		}
		intersectionFactor1 = intersectionFactor1 / denom;
		// FIXME : incorrect formula
		result.set(
				x1 + intersectionFactor1 * x21,
				y1 + intersectionFactor1 * y21,
				z1 + intersectionFactor1 * z21);
		return true;
	}

	/** Compute the distance between a point and a line.
	 *
	 * @param x1 x position of the first point of the line.
	 * @param y1 y position of the first point of the line.
	 * @param z1 z position of the first point of the line.
	 * @param x2 x position of the second point of the line.
	 * @param y2 y position of the second point of the line.
	 * @param z2 z position of the second point of the line.
	 * @param px x position of the point.
	 * @param py y position of the point.
	 * @param pz z position of the point.
	 * @return the distance beetween the point and the line.
	 * @see #computeDistanceLinePoint(double, double, double, double, double, double, double, double, double)
	 */
	@Pure
	static double computeDistanceSquaredLinePoint(double x1, double y1, double z1, double x2, double y2, double z2, double px,
			double py, double pz) {
		final double d = computeDistanceLinePoint(x1, y1, z1, x2, y2, z2, px, py, pz);
		return d * d;
	}

	/** Compute the square distance between a point and a segment.
	 *
	 * @param x1 x position of the first point of the segment.
	 * @param y1 y position of the first point of the segment.
	 * @param z1 z position of the first point of the segment.
	 * @param x2 x position of the second point of the segment.
	 * @param y2 y position of the second point of the segment.
	 * @param z2 y position of the second point of the segment.
	 * @param px x position of the point.
	 * @param py y position of the point.
	 * @param pz z position of the point.
	 * @return the distance beetween the point and the segment.
	 */
	static double computeDistanceSquaredSegmentPoint(double x1, double y1, double z1, double x2, double y2, double z2, double px,
			double py, double pz) {
		final double ratio = computeProjectedPointOnLine(px, py, pz, x1, y1, z1, x2, y2, z2);

		if (ratio <= 0.) {
			return Point3D.getDistanceSquaredPointPoint(px, py, pz, x1, y1, z1);
		}

		if (ratio >= 1.) {
			return Point3D.getDistanceSquaredPointPoint(px, py, pz, x2, y2, z2);
		}

		return Point3D.getDistancePointPoint(
				px, py, pz,
				(1. - ratio) * x1 + ratio * x2,
				(1. - ratio) * y1 + ratio * y2,
				(1. - ratio) * z1 + ratio * z2);
	}

	/** Compute the distance between a point and a segment.
	 *
	 * @param x1 x position of the first point of the segment.
	 * @param y1 y position of the first point of the segment.
	 * @param z1 z position of the first point of the segment.
	 * @param x2 x position of the second point of the segment.
	 * @param y2 y position of the second point of the segment.
	 * @param z2 z position of the second point of the segment.
	 * @param px x position of the point.
	 * @param py y position of the point.
	 * @param pz z position of the point.
	 * @return the distance beetween the point and the segment.
	 */
	static double computeDistanceSegmentPoint(double x1, double y1, double z1, double x2, double y2, double z2, double px,
			double py, double pz) {
		final double ratio = computeProjectedPointOnLine(px, py, pz, x1, y1, z1, x2, y2, z2);

		if (ratio <= 0.) {
			return Point3D.getDistancePointPoint(px, py, pz, x1, y1, z1);
		}

		if (ratio >= 1.) {
			return Point3D.getDistancePointPoint(px, py, pz, x2, y2, z2);
		}

		return Point3D.getDistancePointPoint(
				px, py, pz,
				(1. - ratio) * x1 + ratio * x2,
				(1. - ratio) * y1 + ratio * y2,
				(1. - ratio) * z1 + ratio * z2);
	}

	/** Compute the distance between a point and a line.
	 *
	 * @param x1 x position of the first point of the line.
	 * @param y1 y position of the first point of the line.
	 * @param z1 z position of the first point of the line.
	 * @param x2 x position of the second point of the line.
	 * @param y2 y position of the second point of the line.
	 * @param z2 z position of the second point of the line.
	 * @param px x position of the point.
	 * @param py y position of the point.
	 * @param pz z position of the point.
	 * @return the distance beetween the point and the line.
	 * @see #computeDistanceSquaredLinePoint(double, double, double, double, double, double, double, double, double)
	 * @see #computeRelativeDistanceLinePoint(double, double, double, double, double, double, double, double, double)
	 */
	@Pure
	static double computeDistanceLinePoint(double x1, double y1, double z1, double x2, double y2, double z2, double px, double py,
			double pz) {
		final double vx1 = x2 - x1;
		final double vy1 = y2 - y1;
		final double vz1 = z2 - z1;

		final double vx2 = px - x1;
		final double vy2 = py - y1;
		final double vz2 = pz - z1;

		final double x;
		final double y;
		final double z;
		if (CoordinateSystem3D.getDefaultCoordinateSystem().isLeftHanded()) {
			x = vy2 * vz1 - vz2 * vy1;
			y = vz2 * vx1 - vx2 * vz1;
			z = vx2 * vy1 - vy2 * vx1;
		} else {
			x = vy1 * vz2 - vz1 * vy2;
			y = vz1 * vx2 - vx1 * vz2;
			z = vx1 * vy2 - vy1 * vx2;
		}

		return Math.sqrt(x * x + y * y + z * z) / Math.sqrt(vx1 * vx1 + vy1 * vy1 + vz1 * vz1);
	}

	/** Replies if a point is closed to a segment.
	 *
	 * @param x1 x location of the segment begining.
	 * @param y1 y location of the segment begining.
	 * @param z1 z location of the segment begining.
	 * @param x2 x location of the second-segment ending.
	 * @param y2 y location of the second-segment ending.
	 * @param z2 z location of the second-segment ending.
	 * @param x x location of the point.
	 * @param y y location of the point.
	 * @param z z location of the point.
	 * @param hitDistance is the maximal hitting distance.
	 * @return <code>true</code> if the point and the
	 *         line have closed locations.
	 */
	@Pure
	@SuppressWarnings("checkstyle:magicnumber")
	static boolean isPointClosedToSegment(double x1, double y1, double z1, double x2, double y2, double z2, double x, double y,
			double z, double hitDistance) {
		assert hitDistance >= 0. : AssertMessages.positiveOrZeroParameter(9);
		return computeDistanceSegmentPoint(x1, y1, z1, x2, y2, z2, x, y, z) < hitDistance;
	}

	/** Replies if a point is closed to a line.
	 *
	 * @param x1 x location of the line begining.
	 * @param y1 y location of the line begining.
	 * @param z1 z location of the line begining.
	 * @param x2 x location of the line ending.
	 * @param y2 y location of the line ending.
	 * @param z2 z location of the line ending.
	 * @param x x location of the point.
	 * @param y y location of the point.
	 * @param z z location of the point.
	 * @param hitDistance is the maximal hitting distance.
	 * @return <code>true</code> if the point and the
	 *         line have closed locations.
	 */
	@Pure
	@SuppressWarnings("checkstyle:magicnumber")
	static boolean isPointClosedToLine(double x1, double y1, double z1, double x2, double y2, double z2, double x, double y,
			double z, double hitDistance) {
		assert hitDistance >= 0. : AssertMessages.positiveOrZeroParameter(9);
		return computeDistanceLinePoint(x1, y1, z1, x2, y2, z2, x, y, z) < hitDistance;
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
	 * @return the projection of the specified point on the line. If
	 *     equal to {@code 0}, the projection is equal to the first segment point.
	 *     If equal to {@code 1}, the projection is equal to the second segment point.
	 *     If inside {@code ]0;1[}, the projection is between the two segment points.
	 *     If inside {@code ]-inf;0[}, the projection is outside on the side of the
	 *     first segment point. If inside {@code ]1;+inf[}, the projection is
	 *     outside on the side of the second segment point.
	 */
	@Pure
	static double computeProjectedPointOnLine(double px, double py, double pz, double s1x, double s1y, double s1z, double s2x,
			double s2y, double s2z) {
		final double vx = s2x - s1x;
		final double vy = s2y - s1y;
		final double vz = s2z - s1z;
		final double numerator = (px - s1x) * vx + (py - s1y) * vy + (pz - s1z) * vz;
		final double denomenator = vx * vx + vy * vy + vz * vz;
		return numerator / denomenator;
	}

	/**
	 * Replies the relative distance from the given point to the given line.
	 * The replied distance may be negative, depending on which side of
	 * the line the point is.
	 *
	 * @param x1
	 *            the X coordinate of the start point of the specified line segment
	 * @param y1
	 *            the Y coordinate of the start point of the specified line segment
	 * @param z1
	 *            the Z coordinate of the start point of the specified line segment
	 * @param x2
	 *            the X coordinate of the end point of the specified line segment
	 * @param y2
	 *            the Y coordinate of the end point of the specified line segment
	 * @param z2
	 *            the Z coordinate of the end point of the specified line segment
	 * @param px
	 *            the X coordinate of the specified point to be compared with the specified line segment
	 * @param py
	 *            the Y coordinate of the specified point to be compared with the specified line segment
	 * @param pz
	 *            the Z coordinate of the specified point to be compared with the specified line segment
	 * @return the positive or negative distance from the point to the line
	 * @see #ccw(double, double, double, double, double, double, double, double, double, double)
	 * @see #computeSideLinePoint(double, double, double, double, double, double, double, double, double, double)
	 */
	@Pure
	static double computeRelativeDistanceLinePoint(double x1, double y1, double z1, double x2, double y2, double z2, double px,
			double py, double pz) {
		final double x21 = x2 - x1;
		final double y21 = y2 - y1;
		final double z21 = z2 - z1;
		final double denomenator = x21 * x21 + y21 * y21 + z21 * z21;
		if (denomenator == 0.) {
			return Point3D.getDistancePointPoint(px, py, pz, x1, y1, z1);
		}
		final double factor = ((y1 - py) * x21 - (x1 - px) * y21) / denomenator;
		return factor * Math.sqrt(denomenator);
	}

	/**
	 * Replies on which side of a line the given point is located.
	 *
	 * <p>A return value of 1 indicates that the line segment must turn in the direction that takes the positive X axis towards
	 * the negative Y axis. In the default coordinate system used by Java 2D, this direction is counterclockwise.
	 *
	 * <p>A return value of -1 indicates that the line segment must turn in the direction that takes the positive X axis towards
	 * the positive Y axis. In the default coordinate system, this direction is clockwise.
	 *
	 * <p>A return value of 0 indicates that the point lies exactly on the line segment. Note that an indicator value of 0 is rare
	 * and not useful for determining colinearity because of floating point rounding issues.
	 *
	 * <p>This function uses the equal-to-zero test with the error {@link Math#ulp(double)}.
	 *
	 * <p>In opposite of {@link #ccw(double, double, double, double, double, double, double, double, double, double)}, this
	 * function does not try to classify the point if it is colinear to the segment. If the point is colinear, O is always
	 * returned.
	 *
	 * @param x1
	 *            the X coordinate of the start point of the specified line segment
	 * @param y1
	 *            the Y coordinate of the start point of the specified line segment
	 * @param z1
	 *            the Z coordinate of the start point of the specified line segment
	 * @param x2
	 *            the X coordinate of the end point of the specified line segment
	 * @param y2
	 *            the Y coordinate of the end point of the specified line segment
	 * @param z2
	 *            the Z coordinate of the end point of the specified line segment
	 * @param px
	 *            the X coordinate of the specified point to be compared with the specified line segment
	 * @param py
	 *            the Y coordinate of the specified point to be compared with the specified line segment
	 * @param pz
	 *            the Z coordinate of the specified point to be compared with the specified line segment
	 * @param epsilon
	 *            approximate epsilon.
	 * @return an integer that indicates the position of the third specified coordinates with respect to the line segment formed
	 *         by the first two specified coordinates.
	 * @see #computeRelativeDistanceLinePoint(double, double, double, double, double, double, double, double, double)
	 * @see MathUtil#isEpsilonZero(double)
	 * @see #ccw(double, double, double, double, double, double, double, double, double, double)
	 */
	@Pure
	static int computeSideLinePoint(double x1, double y1, double z1, double x2, double y2, double z2, double px, double py,
			double pz, double epsilon) {
		final double x21 = x2 - x1;
		final double y21 = y2 - y1;
		final double xp1 = px - x1;
		final double yp1 = py - y1;
		double side = xp1 * y21 - yp1 * x21;
		if (side != 0f && MathUtil.isEpsilonZero(side, epsilon)) {
			side = 0f;
		}
		return (side < 0) ? -1 : ((side > 0) ? 1 : 0);
	}

	/**
	 * Replies the relative counterclockwise (CCW) of a segment against a point. Returns an indicator of where
	 * the specified point {@code (px,py)} lies with respect to the line segment from {@code (x1,y1)}
	 *  to {@code (x2,y2)}. The return value can be either 1, -1, or 0 and indicates in which
	 *  direction the specified line must pivot around its first end point, {@code (x1,y1)}, in
	 *  order to point at the specified point {@code (px,py)}.
	 * In other words, given three point P1, P2, and P, is the segments (P1-P2-P) a counterclockwise turn?
	 *
	 * <p>In opposite to
	 * {@link #computeSideLinePoint(double, double, double, double, double, double, double, double, double, double)},
	 * this function tries to classifies the point if it is colinear to the segment.
	 * The classification is explained below.
	 *
	 * <p>A return value of 1 indicates that the line segment must turn in the direction that takes the
	 * positive X axis towards the negative Y axis. In the default coordinate system used by Java 2D,
	 * this direction is counterclockwise.
	 *
	 * <p>A return value of -1 indicates that the line segment must turn in the direction that takes the
	 * positive X axis towards the positive Y axis. In the default coordinate system, this
	 * direction is clockwise.
	 *
	 * <p>A return value of 0 indicates that the point lies exactly on the line segment.
	 * Note that an indicator value of 0 is rare and not useful for determining colinearity
	 * because of floating point rounding issues.
	 *
	 * <p>If the point is colinear with the line segment, but not between the end points, then the value will be
	 * -1 if the point lies "beyond {@code (x1,y1)}" or 1 if the point lies "beyond {@code (x2,y2)}".
	 *
	 * @param x1
	 *            the X coordinate of the start point of the specified line segment
	 * @param y1
	 *            the Y coordinate of the start point of the specified line segment
	 * @param z1
	 *            the Z coordinate of the start point of the specified line segment
	 * @param x2
	 *            the X coordinate of the end point of the specified line segment
	 * @param y2
	 *            the Y coordinate of the end point of the specified line segment
	 * @param z2
	 *            the Z coordinate of the end point of the specified line segment
	 * @param px
	 *            the X coordinate of the specified point to be compared with the specified line segment
	 * @param py
	 *            the Y coordinate of the specified point to be compared with the specified line segment
	 * @param pz
	 *            the Z coordinate of the specified point to be compared with the specified line segment
	 * @param epsilon approximation of the tests for equality to zero.
	 * @return an integer that indicates the position of the third specified coordinates with respect to
	 *     the line segment formed by the first two specified coordinates.
	 * @see #computeRelativeDistanceLinePoint(double, double, double, double, double, double, double, double, double)
	 * @see #computeSideLinePoint(double, double, double, double, double, double, double, double, double, double)
	 */
	@Pure
	static int ccw(double x1, double y1, double z1, double x2, double y2, double z2, double px, double py, double pz,
			double epsilon) {
		final double x21 = x2 - x1;
		final double y21 = y2 - y1;
		//TODO final double z21 = z2 - z1;
		double xp1 = px - x1;
		double yp1 = py - y1;
		double ccw = xp1 * y21 - yp1 * x21;
		if (MathUtil.isEpsilonZero(ccw, epsilon)) {
			// The point is colinear, classify based on which side of
			// the segment the point falls on. We can calculate a
			// relative value using the projection of px,py onto the
			// segment - a negative value indicates the point projects
			// outside of the segment in the direction of the particular
			// endpoint used as the origin for the projection.
			ccw = xp1 * x21 + yp1 * y21;
			if (ccw > 0.) {
				// Reverse the projection to be relative to the original x2,y2
				// x2 and y2 are simply negated.
				// px and py need to have (x2 - x1) or (y2 - y1) subtracted
				// from them (based on the original values)
				// Since we really want to get a positive answer when the
				// point is "beyond (x2,y2)", then we want to calculate
				// the inverse anyway - thus we leave x2 & y2 negated.
				xp1 -= x21;
				yp1 -= y21;
				ccw = xp1 * x21 + yp1 * y21;
				if (ccw < 0) {
					ccw = 0.;
				}
			}
		}
		return (ccw < 0.) ? -1 : ((ccw > 0.) ? 1 : 0);
	}

	/** Compute the interpolate point between the two points.
	 *
	 * @param p1x x coordinate of the first point.
	 * @param p1y y coordinate of the first point.
	 * @param p1z z coordinate of the first point.
	 * @param p2x x coordinate of the second point.
	 * @param p2y y coordinate of the second point.
	 * @param p2z z coordinate of the second point.
	 * @param factor is between 0 and 1; 0 for p1, and 1 for p2.
	 * @param result the interpolated point.
	 */
	@Pure
	static void interpolate(double p1x, double p1y, double p1z, double p2x, double p2y, double p2z, double factor,
			Point3D<?, ?> result) {
		assert result != null : AssertMessages.notNullParameter();
		assert factor >= 0. && factor <= 1. : AssertMessages.outsideRangeInclusiveParameter(factor, 0, 1);
		final double vx = p2x - p1x;
		final double vy = p2y - p1y;
		final double vz = p2z - p1z;
		result.set(
				p1x + factor * vx,
				p1y + factor * vy,
				p1z + factor * vz);
	}

	/** Replies the point on the segment that is farthest to the given point.
	 *
	 * @param ax is the x coordinate of the first point of the segment.
	 * @param ay is the y coordinate of the first point of the segment.
	 * @param az is the z coordinate of the first point of the segment.
	 * @param bx is the x coordinate of the second point of the segment.
	 * @param by is the y coordinate of the second point of the segment.
	 * @param bz is the z coordinate of the second point of the segment.
	 * @param px is the x coordinate of the point.
	 * @param py is the y coordinate of the point.
	 * @param pz is the z coordinate of the point.
	 * @param result the farthest point on the shape.
	 */
	@Pure
	static void computeFarthestPointTo(double ax, double ay, double az, double bx, double by, double bz, double px, double py,
			double pz, Point3D<?, ?> result) {
		assert result != null : AssertMessages.notNullParameter();
		final double xpa = px - ax;
		final double ypa = py - ay;
		final double zpa = pz - az;
		final double xpb = px - bx;
		final double ypb = py - by;
		final double zpb = pz - bz;
		if ((xpa * xpa + ypa * ypa + zpa * zpa) >= (xpb * xpb + ypb * ypb + zpb * zpb)) {
			result.set(ax, ay, az);
		} else {
			result.set(bx, by, bz);
		}
	}

	/** Replies the point on the segment that is closest to the given point.
	 *
	 * @param ax is the x coordinate of the first point of the segment.
	 * @param ay is the y coordinate of the first point of the segment.
	 * @param az is the z coordinate of the first point of the segment.
	 * @param bx is the x coordinate of the second point of the segment.
	 * @param by is the y coordinate of the second point of the segment.
	 * @param bz is the z coordinate of the second point of the segment.
	 * @param px is the x coordinate of the point.
	 * @param py is the y coordinate of the point.
	 * @param pz is the z coordinate of the point.
	 * @param result the is point on the shape.
	 */
	@Pure
	static void computeClosestPointToPoint(
			double ax, double ay, double az, double bx, double by, double bz, double px, double py, double pz,
			Point3D<?, ?> result) {
		assert result != null : AssertMessages.notNullParameter();
		final double ratio = Segment3afp.computeProjectedPointOnLine(px, py, pz, ax, ay, az, bx, by, bz);
		if (ratio <= 0.) {
			result.set(ax, ay, az);
		} else if (ratio >= 1.) {
			result.set(bx, by, bz);
		} else {
			result.set(
					ax + (bx - ax) * ratio,
					ay + (by - ay) * ratio,
					az + (bz - az) * ratio);
		}
	}

	/** Replies the point on the segment that is closest to the rectangle.
	 *
	 * @param sx1 is the x coordinate of the first point of the segment.
	 * @param sy1 is the y coordinate of the first point of the segment.
	 * @param sz1 is the z coordinate of the first point of the segment.
	 * @param sx2 is the x coordinate of the second point of the segment.
	 * @param sy2 is the y coordinate of the second point of the segment.
	 * @param sz2 is the z coordinate of the second point of the segment.
	 * @param rx is the x coordinate of the rectangle.
	 * @param ry is the y coordinate of the rectangle.
	 * @param rz is the z coordinate of the rectangle.
	 * @param rwidth is the width of the rectangle.
	 * @param rheight is the height of the rectangle.
	 * @param rdepth is the depth of the rectangle.
	 * @param result the is point on the segment.
	 */
	@Pure
	@SuppressWarnings({"checkstyle:parameternumber", "checkstyle:magicnumber"})
	static void computeClosestPointToRectangle(double sx1, double sy1, double sz1, double sx2, double sy2, double sz2,
			double rx, double ry, double rz, double rwidth, double rheight, double rdepth, Point3D<?, ?> result) {
		assert rwidth >= 0. : AssertMessages.positiveOrZeroParameter(10);
		assert rheight >= 0. : AssertMessages.positiveOrZeroParameter(11);
		assert rdepth >= 0. : AssertMessages.positiveOrZeroParameter(12);
		final double rmaxx = rx + rwidth;
		final double rmaxy = ry + rheight;
		final double rmaxz = rz + rdepth;
		final int code1 = MathUtil.getCohenSutherlandCode3D(sx1, sy1, sz1, rx, ry, rz, rmaxx, rmaxy, rmaxz);
		final int code2 = MathUtil.getCohenSutherlandCode3D(sx2, sy2, sz1, rx, ry, rz, rmaxx, rmaxy, rmaxz);
		final Point3D<?, ?> tmp1 = new InnerComputationPoint3afp();
		final Point3D<?, ?> tmp2 = new InnerComputationPoint3afp();
		final int zone = RectangularPrism3afp.reduceCohenSutherlandZoneRectangleSegment(
				rx, ry, rz, rmaxx, rmaxy, rmaxz,
				sx1, sy1, sz1, sx2, sy2, sz2,
				code1, code2,
				tmp1, tmp2);
		if ((zone & MathConstants.COHEN_SUTHERLAND_LEFT) != 0) {
			computeClosestPointToSegment(
					sx1, sy1, sz1, sx2, sy2, sz2,
					rx, ry, rz, rx, rmaxy, rmaxz, result);
		} else if ((zone & MathConstants.COHEN_SUTHERLAND_RIGHT) != 0) {
			computeClosestPointToSegment(
					sx1, sy1, sz1, sx2, sy2, sz2,
					rmaxx, ry, rz, rmaxx, rmaxy, rmaxz, result);
		} else if ((zone & MathConstants.COHEN_SUTHERLAND_BOTTOM) != 0) {
			computeClosestPointToSegment(
					sx1, sy1, sz1, sx2, sy2, sz2,
					rx, ry, rz, rmaxx, ry, rmaxz, result);
		} else if ((zone & MathConstants.COHEN_SUTHERLAND_TOP) != 0) {
			computeClosestPointToSegment(
					sx1, sy1, sz1, sx2, sy2, sz2,
					rx, rmaxy, rz, rmaxx, rmaxy, rmaxz, result);
		} else if ((zone & MathConstants.COHEN_SUTHERLAND_FRONT) != 0) {
			computeClosestPointToSegment(
					sx1, sy1, sz1, sx2, sy2, sz2,
					rx, ry, rz, rmaxx, rmaxy, rz, result);
		} else if ((zone & MathConstants.COHEN_SUTHERLAND_BACK) != 0) {
			computeClosestPointToSegment(
					sx1, sy1, sz1, sx2, sy2, sz2,
					rx, ry, rmaxz, rmaxx, rmaxy, rmaxz, result);
		} else {
			computeClosestPointToPoint(
					tmp1.getX(), tmp1.getY(), tmp1.getZ(), tmp2.getX(), tmp2.getY(), tmp2.getZ(),
					(rx + rmaxx) / 2., (ry + rmaxy) / 2., (rz + rmaxz) / 2., result);
		}
	}

	/** Replies the point on the first segment that is closest to the second segment.
	 *
	 * @param s1x1 is the x coordinate of the first point of the first segment.
	 * @param s1y1 is the y coordinate of the first point of the first segment.
	 * @param s1z1 is the z coordinate of the first point of the first segment.
	 * @param s1x2 is the x coordinate of the second point of the first segment.
	 * @param s1y2 is the y coordinate of the second point of the first segment.
	 * @param s1z2 is the z coordinate of the second point of the first segment.
	 * @param s2x1 is the x coordinate of the first point of the second segment.
	 * @param s2y1 is the y coordinate of the first point of the second segment.
	 * @param s2z1 is the z coordinate of the first point of the second segment.
	 * @param s2x2 is the x coordinate of the second point of the second segment.
	 * @param s2y2 is the y coordinate of the second point of the second segment.
	 * @param s2z2 is the z coordinate of the second point of the second segment.
	 * @param result the is point on the shape.
	 * @return the square distance between the segments.
	 */
	@Pure
	@SuppressWarnings({"checkstyle:parameternumber", "checkstyle:npathcomplexity"})
	static double computeClosestPointToSegment(
			double s1x1, double s1y1, double s1z1, double s1x2, double s1y2, double s1z2,
			double s2x1, double s2y1, double s2z1, double s2x2, double s2y2, double s2z2,
			Point3D<?, ?> result) {
		return computeClosestPointToSegment(
				s1x1, s1y1, s1z1, s1x2, s1y2, s1z2, s2x1, s2y1, s2z1, s2x2, s2y2, s2z2,
				result, null);
	}

	/** Replies the point on the first segment that is closest to the second segment.
	 *
	 * @param s1x1 is the x coordinate of the first point of the first segment.
	 * @param s1y1 is the y coordinate of the first point of the first segment.
	 * @param s1z1 is the z coordinate of the first point of the first segment.
	 * @param s1x2 is the x coordinate of the second point of the first segment.
	 * @param s1y2 is the y coordinate of the second point of the first segment.
	 * @param s1z2 is the z coordinate of the second point of the first segment.
	 * @param s2x1 is the x coordinate of the first point of the second segment.
	 * @param s2y1 is the y coordinate of the first point of the second segment.
	 * @param s2z1 is the z coordinate of the first point of the second segment.
	 * @param s2x2 is the x coordinate of the second point of the second segment.
	 * @param s2y2 is the y coordinate of the second point of the second segment.
	 * @param s2z2 is the z coordinate of the second point of the second segment.
	 * @param resultOnFirstSegment the point on the first segment.
	 * @param resultOnSecondSegment the point on the second segment.
	 * @return the square distance between the segments.
	 */
	@Pure
	@SuppressWarnings({"checkstyle:parameternumber", "checkstyle:npathcomplexity"})
	static double computeClosestPointToSegment(
			double s1x1, double s1y1, double s1z1, double s1x2, double s1y2, double s1z2,
			double s2x1, double s2y1, double s2z1, double s2x2, double s2y2, double s2z2,
			Point3D<?, ?> resultOnFirstSegment, Point3D<?, ?> resultOnSecondSegment) {
		final double ux = s1x2 - s1x1;
		final double uy = s1y2 - s1y1;
		final double uz = s1z2 - s1z1;
		final double vx = s2x2 - s2x1;
		final double vy = s2y2 - s2y1;
		final double vz = s2z2 - s2z1;
		final double wx = s1x1 - s2x1;
		final double wy = s1y1 - s2y1;
		final double wz = s1z1 - s2z1;
		final double a = Vector2D.dotProduct(ux, uy, ux, uy);
		final double b = Vector2D.dotProduct(ux, uy, vx, vy);
		final double c = Vector2D.dotProduct(vx, vy, vx, vy);
		final double d = Vector2D.dotProduct(ux, uy, wx, wy);
		final double e = Vector2D.dotProduct(vx, vy, wx, wy);
		final double bigD = a * c - b * b;
		double svD = bigD;
		double tvD = bigD;
		double svN;
		double tvN;
		// compute the line parameters of the two closest points
		if (MathUtil.isEpsilonZero(bigD)) {
			// the lines are almost parallel
			// force using point P0 on segment S1
			svN = 0.;
			// to prevent possible division by 0.0 later
			svD = 1.;
			tvN = e;
			tvD = c;
		} else {
			// get the closest points on the infinite lines
			svN = b * e - c * d;
			tvN = a * e - b * d;
			if (svN < 0.) {
				// sc < 0 => the s=0 edge is visible
				svN = 0.;
				tvN = e;
				tvD = c;
			} else if (svN > svD) {
				// sc > 1  => the s=1 edge is visible
				svN = svD;
				tvN = e + b;
				tvD = c;
			}
		}

		if (tvN < 0.) {
			// tc < 0 => the t=0 edge is visible
			tvN = 0.;
			// recompute sc for this edge
			if (-d < 0.) {
				svN = 0.0;
			} else if (-d > a) {
				svN = svD;
			} else {
				svN = -d;
				svD = a;
			}
		} else if (tvN > tvD) {
			// tc > 1  => the t=1 edge is visible
			tvN = tvD;
			// recompute sc for this edge
			if ((-d + b) < 0.) {
				svN = 0;
			} else if ((-d + b) > a) {
				svN = svD;
			} else {
				svN = -d +  b;
				svD = a;
			}
		}

		// finally do the division to get sc and tc
		final double sc = MathUtil.isEpsilonZero(svN) ? 0. : (svN / svD);
		final double tc = MathUtil.isEpsilonZero(tvN) ? 0. : (tvN / tvD);

		// get the difference of the two closest points
		// =  S1(sc) - S2(tc)
		final double dPx = wx + (sc * ux) - (tc * vx);
		final double dPy = wy + (sc * uy) - (tc * vy);
		final double dPz = wz + (sc * uz) - (tc * vz);

		if (resultOnFirstSegment != null) {
			resultOnFirstSegment.set(s1x1 + sc * ux, s1y1 + sc * uy, s1z1 + sc * uz);
		}

		if (resultOnSecondSegment != null) {
			resultOnSecondSegment.set(s2x1 + tc * vx, s2y1 + tc * vy, s2z1 + tc * vz);
		}

		return dPx * dPx + dPy * dPy + dPz * dPz;
	}

	/**
	 * Calculates the number of times the line from (x0,y0,z0) to (x1,y1,z1)
	 * crosses the ray extending to the right from (px,py,pz).
	 * If the point lies on the line, then no crossings are recorded.
	 * +1 is returned for a crossing where the Y coordinate is increasing
	 * -1 is returned for a crossing where the Y coordinate is decreasing
	 *
	 * <p>This function differs from
	 * {@link #computeCrossingsFromPointWithoutEquality(double, double, double, double, double, double, double, double, double)}.
	 * The equality test is used in this function.
	 *
	 * @param px is the reference point to test.
	 * @param py is the reference point to test.
	 * @param pz is the reference point to test.
	 * @param x0 is the first point of the line.
	 * @param y0 is the first point of the line.
	 * @param z0 is the first point of the line.
	 * @param x1 is the second point of the line.
	 * @param y1 is the secondpoint of the line.
	 * @param z1 is the secondpoint of the line.
	 * @return the crossing.
	 */
	@Pure
	@SuppressWarnings("checkstyle:npathcomplexity")
	static int computeCrossingsFromPoint(
			double px, double py, double pz,
			double x0, double y0, double z0,
			double x1, double y1, double z1) {
		// Copied from AWT API
		if (py < y0 && py < y1) {
			return 0;
		}
		if (py >= y0 && py >= y1) {
			return 0;
		}
		// assert(y0 != y1);
		if (px >= x0 && px >= x1) {
			return 0;
		}
		if (px <  x0 && px <  x1) {
			return (y0 < y1) ? 1 : -1;
		}
		final double xintercept = x0 + (py - y0) * (x1 - x0) / (y1 - y0);
		if (px >= xintercept) {
			return 0;
		}
		return (y0 < y1) ? 1 : -1;
	}

	/**
	 * Calculates the number of times the line from (x0,y0) to (x1,y1)
	 * crosses the ray extending to the right from (px,py).
	 * If the point lies on the line, then no crossings are recorded.
	 * +1 is returned for a crossing where the Y coordinate is increasing
	 * -1 is returned for a crossing where the Y coordinate is decreasing
	 *
	 * <p>This function differs from
	 * {@link #computeCrossingsFromPoint(double, double, double, double, double, double, double, double, double)}.
	 * The equality test is not used in this function.
	 *
	 * @param px is the reference point to test.
	 * @param py is the reference point to test.
	 * @param pz is the reference point to test.
	 * @param x0 is the first point of the line.
	 * @param y0 is the first point of the line.
	 * @param z0 is the first point of the line.
	 * @param x1 is the second point of the line.
	 * @param y1 is the second point of the line.
	 * @param z1 is the second point of the line.
	 * @return the crossing.
	 */
	@Pure
	@SuppressWarnings("checkstyle:npathcomplexity")
	static int computeCrossingsFromPointWithoutEquality(
			double px, double py, double pz,
			double x0, double y0, double z0,
			double x1, double y1, double z1) {
		// Copied from AWT API
		if (py < y0 && py < y1) {
			return 0;
		}
		if (py > y0 && py > y1) {
			return 0;
		}
		// assert(y0 != y1);
		if (px > x0 && px > x1) {
			return 0;
		}
		if (px < x0 && px < x1) {
			return (y0 < y1) ? 1 : -1;
		}
		final double xintercept = x0 + (py - y0) * (x1 - x0) / (y1 - y0);
		if (px > xintercept) {
			return 0;
		}
		return (y0 < y1) ? 1 : -1;
	}

	/**
	 * Calculates the number of times the line from (x0,y0,z0) to (x1,y1,z1)
	 * crosses the segment (sx0,sy0,sz0) to (sx1,sy1,sz1) extending to the right.
	 *
	 * @param crossings is the initial value for the number of crossings.
	 * @param sx1 is the first point of the segment to extend.
	 * @param sy1 is the first point of the segment to extend.
	 * @param sz1 is the first point of the segment to extend.
	 * @param sx2 is the second point of the segment to extend.
	 * @param sy2 is the second point of the segment to extend.
	 * @param sz2 is the second point of the segment to extend.
	 * @param x0 is the first point of the line.
	 * @param y0 is the first point of the line.
	 * @param z0 is the first point of the line.
	 * @param x1 is the second point of the line.
	 * @param y1 is the second point of the line.
	 * @param z1 is the second point of the line.
	 * @return the crossing, or {@link MathConstants#SHAPE_INTERSECTS}
	 */
	@Pure
	@SuppressWarnings({"checkstyle:npathcomplexity", "checkstyle:cyclomaticcomplexity"})
	// TODO : check 3D version
	static int computeCrossingsFromSegment(
			int crossings,
			double sx1, double sy1, double sz1,
			double sx2, double sy2, double sz2,
			double x0, double y0, double z0,
			double x1, double y1, double z1) {
		int numCrosses = crossings;

		final double xmin = Math.min(sx1, sx2);
		final double xmax = Math.max(sx1, sx2);
		final double ymin = Math.min(sy1, sy2);
		final double ymax = Math.max(sy1, sy2);
		//TODO final double zmin = Math.min(sz1, sz2);
		//TODO final double zmax = Math.max(sz1, sz2);

		if (y0 <= ymin && y1 <= ymin) {
			return numCrosses;
		}
		if (y0 >= ymax && y1 >= ymax) {
			return numCrosses;
		}
		if (x0 <= xmin && x1 <= xmin) {
			return numCrosses;
		}
		if (x0 >= xmax && x1 >= xmax) {
			// The line is entirely at the right of the shadow
			if (y0 < y1) {
				if (y0 <= ymin) {
					++numCrosses;
				}
				if (y1 >= ymax) {
					++numCrosses;
				}
			} else {
				if (y1 <= ymin) {
					--numCrosses;
				}
				if (y0 >= ymax) {
					--numCrosses;
				}
			}
		} else if (intersectsSegmentSegmentWithEnds(x0, y0, z0, x1, y1, z1, sx1, sy1, sz1, sx2, sy2, sz2)) {
			return MathConstants.SHAPE_INTERSECTS;
		} else {
			final int side1;
			final int side2;
			if (sy1 <= sy2) {
				side1 = computeSideLinePoint(sx1, sy1, sz1, sx2, sy2, sz2, x0, y0, z0, 0.);
				side2 = computeSideLinePoint(sx1, sy1, sz1, sx2, sy2, sz2, x1, y1, z1, 0.);
			} else {
				side1 = computeSideLinePoint(sx2, sy2, sz2, sx1, sy1, sz1, x0, y0, z1, 0.);
				side2 = computeSideLinePoint(sx2, sy2, sz2, sx1, sy1, sz1, x1, y1, z1, 0.);
			}
			if (side1 > 0 || side2 > 0) {
				final int n1 = computeCrossingsFromPoint(sx1, sy1, sz1, x0, y0, z0, x1, y1, z1);
				final int n2;
				if (n1 != 0) {
					n2 = computeCrossingsFromPointWithoutEquality(sx2, sy2, sz2, x0, y0, z0, x1, y1, z1);
				} else {
					n2 = computeCrossingsFromPoint(sx2, sy2, sz2, x0, y0, z0, x1, y1, z1);
				}
				numCrosses += n1;
				numCrosses += n2;
			}
		}

		return numCrosses;
	}

	/**
	 * Calculates the number of times the line from (x0,y0,z0) to (x1,y1,z1)
	 * crosses the sphere (cx,cy,cz,radius) with radius extending to the right.
	 *
	 * @param crossings is the initial value for the number of crossings.
	 * @param cx is the center of the circle to extend.
	 * @param cy is the center of the circle to extend.
	 * @param cz is the center of the circle to extend.
	 * @param radius is the radius of the circle to extend.
	 * @param x0 is the first point of the line.
	 * @param y0 is the first point of the line.
	 * @param z0 is the first point of the line.
	 * @param x1 is the second point of the line.
	 * @param y1 is the secondpoint of the line.
	 * @param z1 is the secondpoint of the line.
	 * @return the crossing, or {@link MathConstants#SHAPE_INTERSECTS}.
	 */
	@Pure
	@SuppressWarnings("checkstyle:magicnumber")
	// TODO : implement 3D version
	static int computeCrossingsFromSphere(
			int crossings,
			double cx, double cy, double cz,
			double radius,
			double x0, double y0, double z0,
			double x1, double y1, double z1) {
		assert radius >= 0. : AssertMessages.positiveOrZeroParameter(4);
		//		int numCrosses = crossings;
		//
		//		double xmin = cx - Math.abs(radius);
		//		double ymin = cy - Math.abs(radius);
		//		double ymax = cy + Math.abs(radius);
		//
		//		if (y0<=ymin && y1<=ymin) return numCrosses;
		//		if (y0>=ymax && y1>=ymax) return numCrosses;
		//		if (x0<=xmin && x1<=xmin) return numCrosses;
		//
		//		if (x0>=cx+radius && x1>=cx+radius) {
		//			// The line is entirely at the right of the shadow
		//			if (y0<y1) {
		//				if (y0<=ymin) ++numCrosses;
		//				if (y1>=ymax) ++numCrosses;
		//			}
		//			else {
		//				if (y1<=ymin) --numCrosses;
		//				if (y0>=ymax) --numCrosses;
		//			}
		//		}
		//		else if (Sphere3afp.intersectsCircleSegment(
		//				cx, cy, radius,
		//				x0, y0, x1, y1)) {
		//			return MathConstants.SHAPE_INTERSECTS;
		//		} else {
		//			numCrosses += computeCrossingsFromPoint(cx, ymin, x0, y0, x1, y1);
		//			numCrosses += computeCrossingsFromPoint(cx, ymax, x0, y0, x1, y1);
		//		}
		//
		//		return numCrosses;
		return -1;
	}

	/**
	 * Accumulate the number of times the line crosses the shadow
	 * extending to the right of the rectangle.  See the comment
	 * for the {@link MathConstants#SHAPE_INTERSECTS} constant for more complete details.
	 *
	 * @param crossings is the initial value for the number of crossings.
	 * @param rxmin is the first corner of the rectangle.
	 * @param rymin is the first corner of the rectangle.
	 * @param rzmin is the first corner of the rectangle.
	 * @param rxmax is the second corner of the rectangle.
	 * @param rymax is the second corner of the rectangle.
	 * @param rzmax is the second corner of the rectangle.
	 * @param x0 is the first point of the line.
	 * @param y0 is the first point of the line.
	 * @param z0 is the first point of the line.
	 * @param x1 is the second point of the line.
	 * @param y1 is the secondpoint of the line.
	 * @param z1 is the secondpoint of the line.
	 * @return the crossing, or {@link MathConstants#SHAPE_INTERSECTS}.
	 */
	@Pure
	@SuppressWarnings("checkstyle:magicnumber")
	// TODO : implement 3D version
	static int computeCrossingsFromRect(
			int crossings,
			double rxmin, double rymin, double rzmin,
			double rxmax, double rymax, double rzmax,
			double x0, double y0, double z0,
			double x1, double y1, double z1) {
		assert rxmin <= rxmax : AssertMessages.lowerEqualParameters(1,  rxmin, 4, rxmax);
		assert rymin <= rymax : AssertMessages.lowerEqualParameters(2,  rymin, 5, rymax);
		assert rzmin <= rzmax : AssertMessages.lowerEqualParameters(3,  rzmin, 6, rzmax);
		//		int numCrosses = crossings;
		//
		//		if (y0 >= rymax && y1 >= rymax) return numCrosses;
		//		if (y0 <= rymin && y1 <= rymin) return numCrosses;
		//		if (x0 <= rxmin && x1 <= rxmin) return numCrosses;
		//		if (x0 >= rxmax && x1 >= rxmax) {
		//			// Line is entirely to the right of the rect
		//			// and the vertical ranges of the two overlap by a non-empty amount
		//			// Thus, this line segment is partially in the "right-shadow"
		//			// Path may have done a complete crossing
		//			// Or path may have entered or exited the right-shadow
		//			if (y0 < y1) {
		//				// y-increasing line segment...
		//				// We know that y0 < rymax and y1 > rymin
		//				if (y0 <= rymin) ++numCrosses;
		//				if (y1 >= rymax) ++numCrosses;
		//			}
		//			else if (y1 < y0) {
		//				// y-decreasing line segment...
		//				// We know that y1 < rymax and y0 > rymin
		//				if (y1 <= rymin) --numCrosses;
		//				if (y0 >= rymax) --numCrosses;
		//			}
		//			return numCrosses;
		//		}
		//		// Remaining case:
		//		// Both x and y ranges overlap by a non-empty amount
		//		// First do trivial INTERSECTS rejection of the cases
		//		// where one of the endpoints is inside the rectangle.
		//		if ((x0 > rxmin && x0 < rxmax && y0 > rymin && y0 < rymax) ||
		//				(x1 > rxmin && x1 < rxmax && y1 > rymin && y1 < rymax)) {
		//			return MathConstants.SHAPE_INTERSECTS;
		//		}
		//		// Otherwise calculate the y intercepts and see where
		//		// they fall with respect to the rectangle
		//		double xi0 = x0;
		//		if (y0 < rymin) {
		//			xi0 += ((rymin - y0) * (x1 - x0) / (y1 - y0));
		//		}
		//		else if (y0 > rymax) {
		//			xi0 += ((rymax - y0) * (x1 - x0) / (y1 - y0));
		//		}
		//		double xi1 = x1;
		//		if (y1 < rymin) {
		//			xi1 += ((rymin - y1) * (x0 - x1) / (y0 - y1));
		//		}
		//		else if (y1 > rymax) {
		//			xi1 += ((rymax - y1) * (x0 - x1) / (y0 - y1));
		//		}
		//		if (xi0 <= rxmin && xi1 <= rxmin) return numCrosses;
		//		if (xi0 >= rxmax && xi1 >= rxmax) {
		//			if (y0 < y1) {
		//				// y-increasing line segment...
		//				// We know that y0 < rymax and y1 > rymin
		//				if (y0 <= rymin) ++numCrosses;
		//				if (y1 >= rymax) ++numCrosses;
		//			}
		//			else if (y1 < y0) {
		//				// y-decreasing line segment...
		//				// We know that y1 < rymax and y0 > rymin
		//				if (y1 <= rymin) --numCrosses;
		//				if (y0 >= rymax) --numCrosses;
		//			}
		//			return numCrosses;
		//		}
		//		return MathConstants.SHAPE_INTERSECTS;
		return -1;
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
	 */
	@Pure
	static boolean intersectsLineLine(double x1, double y1, double z1, double x2, double y2, double z2, double x3, double y3,
			double z3, double x4, double y4, double z4) {
		if (isParallelLines(x1, y1, z1, x2, y2, z2, x3, y3, z3, x4, y4, z4)) {
			return Point3D.isCollinearPoints(x1, y1, z1, x2, y2, z2, x3, y3, z3);
		}
		return true;
	}

	/** Replies if a segment and a line are intersecting.
	 *
	 * @param x1 is the first point of the first segment.
	 * @param y1 is the first point of the first segment.
	 * @param z1 is the first point of the first segment.
	 * @param x2 is the second point of the first segment.
	 * @param y2 is the second point of the first segment.
	 * @param z2 is the second point of the first segment.
	 * @param x3 is the first point of the second line.
	 * @param y3 is the first point of the second line.
	 * @param z3 is the first point of the second line.
	 * @param x4 is the second point of the second line.
	 * @param y4 is the second point of the second line.
	 * @param z4 is the second point of the second line.
	 * @return <code>true</code> if the two shapes are intersecting; otherwise
	 * <code>false</code>
	 */
	@Pure
	static boolean intersectsSegmentLine(double x1, double y1, double z1, double x2, double y2, double z2, double x3, double y3,
			double z3, double x4, double y4, double z4) {
		return (computeSideLinePoint(x3, y3, z3, x4, y4, z4, x1, y1, z1, Double.NaN)
				* computeSideLinePoint(x3, y3, z3, x4, y4, z4, x2, y2, z2, Double.NaN)) <= 0;
	}

	/** Do an intersection test of two segments for ensuring that the answer of "no intersect" is safe.
	 *
	 * <p>If the function replies {@link UncertainIntersection#NO}, we are sure that the two given segments are not
	 * intersecting. If the function replies {@link UncertainIntersection#PERHAPS}, the two segments may intersects
	 * (uncertain answer).
	 *
	 * <p>This function considers that the ends of the segments are intersecting.
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
	 * @return the type of intersection.
	 * @see #intersectsSegmentSegmentWithEnds(double, double, double, double, double,
	 *     double, double, double, double, double, double, double)
	 */
	@Pure
	// TODO : implement 3D
	static UncertainIntersection getNoSegmentSegmentWithEndsIntersection(double x1, double y1, double z1, double x2, double y2,
			double z2, double x3, double y3, double z3, double x4, double y4, double z4) {
		final double vx1 = x2 - x1;
		final double vy1 = y2 - y1;

		// Based on CCW. It is different than MathUtil.ccw(); because
		// this small algorithm is computing a ccw of 0 for colinear points.
		final double vx2a = x3 - x1;
		final double vy2a = y3 - y1;
		double f1 = vx2a * vy1 - vy2a * vx1;

		final double vx2b = x4 - x1;
		final double vy2b = y4 - y1;
		double f2 = vx2b * vy1 - vy2b * vx1;

		// s = f1 * f2
		//
		// f1  f2  s   intersect
		// -1  -1   1  F
		// -1   0   0  ON SEGMENT?
		// -1   1  -1  T
		//  0  -1   0  ON SEGMENT?
		//  0   0   0  SEGMENT INTERSECTION?
		//  0   1   1  ON SEGMENT?
		//  1  -1  -1  T
		//  1   0   0  ON SEGMENT?
		//  1   1   1  F
		final double sign = f1 * f2;

		if (sign < 0) {
			return UncertainIntersection.PERHAPS;
		}
		if (sign > 0) {
			return UncertainIntersection.NO;
		}

		final double squaredLength = vx1 * vx1 + vy1 * vy1;

		if (f1 == 0f && f2 == 0) {
			// Projection of the point on the segment line:
			// <0 -> means before first point
			// >1 -> means after second point
			// otherwhise on the segment.

			f1 = (vx2a * vx1 + vy2a * vy1) / squaredLength;
			f2 = (vx2b * vx1 + vy2b * vy1) / squaredLength;

			// No intersection when:
			// f1<0 && f2<0 or
			// f1>1 && f2>1

			return UncertainIntersection.fromBoolean((f1 >= 0f || f2 >= 0) && (f1 <= 1f || f2 <= 1));
		}

		if (f1 == 0) {
			// Projection of the point on the segment line:
			// <0 -> means before first point
			// >1 -> means after second point
			// otherwhise on the segment.

			f1 = (vx2a * vx1 + vy2a * vy1) / squaredLength;

			// No intersection when:
			// f1<=0 && f2<=0 or
			// f1>=1 && f2>=1

			return UncertainIntersection.fromBoolean(f1 >= 0f && f1 <= 1);
		}

		if (f2 == 0) {
			// Projection of the point on the segment line:
			// <0 -> means before first point
			// >1 -> means after second point
			// otherwhise on the segment.

			f2 = (vx2b * vx1 + vy2b * vy1) / squaredLength;

			// No intersection when:
			// f1<0 && f2<0 or
			// f1>1 && f2>1

			return UncertainIntersection.fromBoolean(f2 >= 0 && f2 <= 1);
		}

		return UncertainIntersection.NO;
	}

	/** Do an intersection test of two segments for ensuring that the answer of "no intersect" is safe.
	 * If the function replies <code>true</code>, it may
	 * This function does not consider that the ends of
	 * the segments are intersecting.
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
	 * @return the type of intersection.
	 * @see #intersectsSegmentSegmentWithoutEnds(double, double, double, double,
	 *  double, double, double, double, double, double, double, double)
	 */
	@Pure
	// TODO : implement 3D
	static UncertainIntersection getNoSegmentSegmentWithoutEndsIntersection(double x1, double y1, double z1, double x2, double y2,
			double z2, double x3, double y3, double z3, double x4, double y4, double z4) {

		final double vx1 = x2 - x1;
		final double vy1 = y2 - y1;

		final double vx2a = x3 - x1;
		final double vy2a = y3 - y1;
		double f1 = vx2a * vy1 - vy2a * vx1;

		final double vx2b = x4 - x1;
		final double vy2b = y4 - y1;
		double f2 = vx2b * vy1 - vy2b * vx1;

		// s = f1 * f2
		//
		// f1  f2  s   intersect
		// -1  -1   1  F
		// -1   0   0  F
		// -1   1  -1  T
		//  0  -1   0  F
		//  0   0   0  SEGMENT INTERSECTION?
		//  0   1   1  F
		//  1  -1  -1  T
		//  1   0   0  F
		//  1   1   1  F

		final double sign = f1 * f2;

		if (sign < 0) {
			return UncertainIntersection.PERHAPS;
		}
		if (sign > 0) {
			return UncertainIntersection.NO;
		}

		if (f1 == 0f && f2 == 0) {
			// Projection of the point on the segment line:
			// <0 -> means before first point
			// >1 -> means after second point
			// otherwhise on the segment.

			final double squaredLength = vx1 * vx1 + vy1 * vy1;

			f1 = (vx2a * vx1 + vy2a * vy1) / squaredLength;
			f2 = (vx2b * vx1 + vy2b * vy1) / squaredLength;

			// No intersection when:
			// f1<=0 && f2<=0 or
			// f1>=1 && f2>=1

			return UncertainIntersection.fromBoolean((f1 > 0f || f2 > 0) && (f1 < 1f || f2 < 1));
		}

		return UncertainIntersection.NO;
	}

	/** Replies if two segments are intersecting.
	 * This function considers that the ends of
	 * the segments are not intersecting.
	 * To include the ends of the segments in the intersection ranges, see
	 * {@link #intersectsSegmentSegmentWithEnds(double, double, double, double, double, double,
	 *     double, double, double, double, double, double)}.
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
	 * @see #intersectsSegmentSegmentWithEnds(double, double, double, double, double, double,
	 *     double, double, double, double, double, double)
	 */
	@Pure
	static boolean intersectsSegmentSegmentWithoutEnds(double x1, double y1, double z1, double x2, double y2, double z2,
			double x3, double y3, double z3, double x4, double y4, double z4) {
		final UncertainIntersection r;
		r = getNoSegmentSegmentWithoutEndsIntersection(x1, y1, z1, x2, y2, z2, x3, y3, z3, x4, y4, z4);
		if (!r.booleanValue()) {
			return r.booleanValue();
		}
		return getNoSegmentSegmentWithoutEndsIntersection(x3, y3, z3, x4, y4, z4, x1, y1, z1, x2, y2, z2).booleanValue();
	}

	/** Replies if two segments are intersecting.
	 * This function considers that the ends of
	 * the segments are intersecting.
	 * To ignore the ends of the segments, see
	 * {@link #intersectsSegmentSegmentWithoutEnds(double, double, double, double, double,
	 *     double, double, double, double, double, double, double)}.
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
	 * @see #intersectsSegmentSegmentWithoutEnds(double, double, double, double, double, double, double,
	 *     double, double, double, double, double)
	 */
	@Pure
	static boolean intersectsSegmentSegmentWithEnds(double x1, double y1, double z1, double x2, double y2, double z2,
			double x3, double y3, double z3, double x4, double y4, double z4) {
		final UncertainIntersection r;
		r = getNoSegmentSegmentWithEndsIntersection(x1, y1, z1, x2, y2, z2, x3, y3, z3, x4, y4, z4);
		if (!r.booleanValue()) {
			return r.booleanValue();
		}
		return getNoSegmentSegmentWithEndsIntersection(x3, y3, z3, x4, y4, z4, x1, y1, z1, x2, y2, z2).booleanValue();
	}

	@Pure
	@Override
	default boolean equalsToShape(IT shape) {
		if (shape == null) {
			return false;
		}
		if (shape == this) {
			return true;
		}
		return getX1() == shape.getX1()
				&& getY1() == shape.getY1()
				&& getZ1() == shape.getZ1()
				&& getX2() == shape.getX2()
				&& getY2() == shape.getY2()
				&& getZ2() == shape.getZ2();
	}

	@Override
	default boolean isEmpty() {
		return getX1() == getX2() && getY1() == getY2() && getZ1() == getZ2();
	}

	/** Change the line.
	 *
	 * @param x1 x coordinate of the first point.
	 * @param y1 y coordinate of the first point.
	 * @param z1 z coordinate of the first point.
	 * @param x2 x coordinate of the second point.
	 * @param y2 y coordinate of the second point.
	 * @param z2 z coordinate of the second point.
	 */
	// No default implementation to ensure atomic change.
	void set(double x1, double y1, double z1, double x2, double y2, double z2);

	/** Change the line.
	 *
	 * @param firstPoint the first point.
	 * @param secondPoint the second point.
	 */
	default void set(Point3D<?, ?> firstPoint, Point3D<?, ?> secondPoint) {
		assert firstPoint != null : AssertMessages.notNullParameter(0);
		assert secondPoint != null : AssertMessages.notNullParameter(1);
		set(firstPoint.getX(), firstPoint.getY(), firstPoint.getZ(), secondPoint.getX(), secondPoint.getY(), secondPoint.getZ());
	}

	@Override
	default void set(IT shape) {
		assert shape != null : AssertMessages.notNullParameter();
		set(shape.getX1(), shape.getY1(), shape.getZ1(), shape.getX2(), shape.getY2(), shape.getZ2());
	}

	/** Sets a new value in the X of the first point.
	 *
	 * @param x the new value double x
	 */
	void setX1(double x);

	/**Sets a new value in the Y of the first point.
	 *
	 * @param y the new value double y
	 */
	void setY1(double y);

	/**Sets a new value in the Z of the first point.
	 *
	 * @param z the new value double z
	 */
	void setZ1(double z);

	/**Sets a new value in the X of the second point.
	 *
	 * @param x the new value double x
	 */
	void setX2(double x);

	/**Sets a new value in the Y of the second point.
	 *
	 * @param y the new value double y
	 */
	void setY2(double y);

	/**Sets a new value in the Z of the second point.
	 *
	 * @param z the new value double z
	 */
	void setZ2(double z);

	/** Replies the X of the first point.
	 *
	 * @return the x of the first point.
	 */
	@Pure
	double getX1();

	/** Replies the Y of the first point.
	 *
	 * @return the y of the first point.
	 */
	@Pure
	double getY1();

	/** Replies the Z of the first point.
	 *
	 * @return the z of the first point.
	 */
	@Pure
	double getZ1();

	/** Replies the X of the second point.
	 *
	 * @return the x of the second point.
	 */
	@Pure
	double getX2();

	/** Replies the Y of the second point.
	 *
	 * @return the y of the second point.
	 */
	@Pure
	double getY2();

	/** Replies the Z of the second point.
	 *
	 * @return the z of the second point.
	 */
	@Pure
	double getZ2();

	/** Replies the first point.
	 *
	 * @return the first point.
	 */
	@Pure
	default P getP1() {
		return getGeomFactory().newPoint(getX1(), getY1(), getZ1());
	}

	/** Replies the second point.
	 *
	 * @return the second point.
	 */
	@Pure
	default P getP2() {
		return getGeomFactory().newPoint(getX2(), getY2(), getZ2());
	}

	/** Change the first point.
	 *
	 * @param x x coordinate of the first point.
	 * @param y y coordinate of the first point.
	 * @param z z coordinate of the first point.
	 */
	default void setP1(double x, double y, double z) {
		set(x, y, z, getX2(), getY2(), getZ2());
	}

	/** Change the first point.
	 *
	 * @param point the first point.
	 */
	default void setP1(Point3D<?, ?> point) {
		assert point != null : AssertMessages.notNullParameter();
		set(point.getX(), point.getY(), point.getZ(), getX2(), getY2(), getZ2());
	}

	/** Change the first point.
	 *
	 * @param x x coordinate of the first point.
	 * @param y y coordinate of the first point.
	 * @param z z coordinate of the first point.
	 */
	default void setP2(double x, double y, double z) {
		set(getX1(), getY1(), getZ1(), x, y, z);
	}

	/** Change the second point.
	 *
	 * @param point the second point.
	 */
	default void setP2(Point3D<?, ?> point) {
		assert point != null : AssertMessages.notNullParameter();
		set(getX1(), getY1(), getZ1(), point.getX(), point.getY(), point.getZ());
	}

	/** Replies the length of the segment.
	 *
	 * @return the length.
	 */
	@Pure
	default double getLength() {
		return Point3D.getDistancePointPoint(getX1(), getY1(), getZ1(), getX2(), getY2(), getZ2());
	}

	/** Replies the squared length of the segment.
	 *
	 * @return the squared length.
	 */
	@Pure
	default double getLengthSquared() {
		return Point3D.getDistanceSquaredPointPoint(getX1(), getY1(), getZ1(), getX2(), getY2(), getZ2());
	}

	@Pure
	@Override
	default void toBoundingBox(B box) {
		assert box != null : AssertMessages.notNullParameter();
		box.setFromCorners(
				this.getX1(),
				this.getY1(),
				this.getZ1(),
				this.getX2(),
				this.getY2(),
				this.getZ2());
	}

	@Pure
	@Override
	default double getDistanceSquared(Point3D<?, ?> point) {
		assert point != null : AssertMessages.notNullParameter();
		return computeDistanceSquaredSegmentPoint(
				getX1(), getY1(), getZ1(),
				getX2(), getY2(), getZ2(),
				point.getX(), point.getY(), point.getZ());
	}

	@Pure
	@Override
	default double getDistanceL1(Point3D<?, ?> point) {
		assert point != null : AssertMessages.notNullParameter();
		double ratio = computeProjectedPointOnLine(point.getX(), point.getY(), point.getZ(), getX1(), getY1(), getZ1(), getX2(),
				getY2(), getZ2());
		ratio = MathUtil.clamp(ratio, 0, 1);
		final double vx = (getX2() - getX1()) * ratio;
		final double vy = (getY2() - getY1()) * ratio;
		final double vz = (getZ2() - getZ1()) * ratio;
		return Math.abs(getX1() + vx - point.getX())
				+ Math.abs(getY1() + vy - point.getY())
				+ Math.abs(getZ1() + vz - point.getZ());
	}

	@Pure
	@Override
	default double getDistanceLinf(Point3D<?, ?> point) {
		assert point != null : AssertMessages.notNullParameter();
		double ratio = computeProjectedPointOnLine(point.getX(), point.getY(), point.getZ(), getX1(), getY1(), getZ1(), getX2(),
				getY2(), getZ2());
		ratio = MathUtil.clamp(ratio, 0, 1);
		final double vx = (getX2() - getX1()) * ratio;
		final double vy = (getY2() - getY1()) * ratio;
		final double vz = (getZ2() - getZ1()) * ratio;
		return MathUtil.max(
				Math.abs(this.getX1() + vx - point.getX()),
				Math.abs(this.getY1() + vy - point.getY()),
				Math.abs(this.getZ1() + vz - point.getZ()));
	}

	/** {@inheritDoc}
	 *
	 * <p>This function uses the equal-to-zero test with the error {@link Math#ulp(double)}.
	 *
	 * @see MathUtil#isEpsilonZero(double)
	 */
	@Override
	default boolean contains(double x, double y, double z) {
		return MathUtil.isEpsilonZero(
				computeDistanceSquaredSegmentPoint(
						getX1(), getY1(), getZ1(),
						getX2(), getY2(), getZ2(),
						x, y, z));
	}

	@Override
	default boolean contains(RectangularPrism3afp<?, ?, ?, ?, ?, ?> rectangularPrism) {
		assert rectangularPrism != null : AssertMessages.notNullParameter();
		return (getX1() == getX2() || getY1() == getY2() || getZ1() == getZ2())
				&& contains(rectangularPrism.getMinX(), rectangularPrism.getMinY(), rectangularPrism.getMinZ())
				&& contains(rectangularPrism.getMaxX(), rectangularPrism.getMaxY(), rectangularPrism.getMaxZ());
	}

	@Override
	default void translate(double dx, double dy, double dz) {
		set(getX1() + dx, getY1() + dy, getZ1() + dz, getX2() + dx, getY2() + dy, getZ2() + dz);
	}

	/** Transform the current segment.
	 * This function changes the current segment.
	 *
	 * @param transform is the affine transformation to apply.
	 * @see #createTransformedShape
	 */
	default void transform(Transform3D transform) {
		assert transform != null : AssertMessages.notNullParameter();
		final Point3D<?, ?> p = new InnerComputationPoint3afp(getX1(),  getY1(), getZ1());
		transform.transform(p);
		final double x1 = p.getX();
		final double y1 = p.getY();
		final double z1 = p.getZ();
		p.set(getX2(), getY2(), getZ2());
		transform.transform(p);
		set(x1, y1, z1, p.getX(), p.getY(), p.getZ());
	}

	@Override
	default void clear() {
		set(0, 0, 0, 0, 0, 0);
	}

	/** Clip the segment against the clipping rectangle
	 * according to the <a href="http://en.wikipedia.org/wiki/Cohen%E2%80%93Sutherland_algorithm">Cohen-Sutherland algorithm</a>.
	 *
	 * @param rxmin is the min of the coordinates of the rectangle.
	 * @param rymin is the min of the coordinates of the rectangle.
	 * @param rzmin is the min of the coordinates of the rectangle.
	 * @param rxmax is the max of the coordinates of the rectangle.
	 * @param rymax is the max of the coordinates of the rectangle.
	 * @param rzmax is the max of the coordinates of the rectangle.
	 * @return <code>true</code> if the segment has an intersection with the
	 *     rectangle and the segment was clipped; <code>false</code> if the segment
	 *     does not intersect the rectangle.
	 */
	@Pure
	@SuppressWarnings("checkstyle:magicnumber")
	// TODO
	default boolean clipToRectangle(double rxmin, double rymin, double rzmin, double rxmax, double rymax, double rzmax) {
		assert rxmin <= rxmax : AssertMessages.lowerEqualParameters(0, rxmin, 3, rxmax);
		assert rymin <= rymax : AssertMessages.lowerEqualParameters(1, rymin, 4, rymax);
		assert rzmin <= rzmax : AssertMessages.lowerEqualParameters(2, rzmin, 5, rzmax);
		double x0 = getX1();
		double y0 = getY1();
		double z0 = getZ1();
		double x1 = getX2();
		double y1 = getY2();
		double z1 = getZ2();
		int code1 = MathUtil.getCohenSutherlandCode3D(x0, y0, z0, rxmin, rymin, rzmin, rxmax, rymax, rzmax);
		int code2 = MathUtil.getCohenSutherlandCode3D(x1, y1, z1, rxmin, rymin, rzmin, rxmax, rymax, rzmax);
		boolean accept = false;
		boolean cont = true;
		double x = 0;
		double y = 0;
		final double z = 0;

		while (cont) {
			if ((code1 | code2) == 0) {
				// Bitwise OR is 0. Trivially accept and get out of loop
				accept = true;
				cont = false;
			} else if ((code1 & code2) != 0) {
				// Bitwise AND is not 0. Trivially reject and get out of loop
				cont = false;
			} else {
				// failed both tests, so calculate the line segment to clip
				// from an outside point to an intersection with clip edge

				// At least one endpoint is outside the clip rectangle; pick it.
				int code3 = code1 != 0 ? code1 : code2;

				// TODO update formula & use FRONT / BACK for z coordinate

				// Now find the intersection point;
				// use formulas y = y0 + slope * (x - x0), x = x0 + (1 / slope) * (y - y0)
				if ((code3 & MathConstants.COHEN_SUTHERLAND_TOP) != 0) {
					// point is above the clip rectangle
					x = x0 + (x1 - x0) * (rymax - y0) / (y1 - y0);
					y = rymax;
				} else if ((code3 & MathConstants.COHEN_SUTHERLAND_BOTTOM) != 0) {
					// point is below the clip rectangle
					x = x0 + (x1 - x0) * (rymin - y0) / (y1 - y0);
					y = rymin;
				} else if ((code3 & MathConstants.COHEN_SUTHERLAND_RIGHT) != 0) {
					// point is to the right of clip rectangle
					y = y0 + (y1 - y0) * (rxmax - x0) / (x1 - x0);
					x = rxmax;
				} else if ((code3 & MathConstants.COHEN_SUTHERLAND_LEFT) != 0) {
					// point is to the left of clip rectangle
					y = y0 + (y1 - y0) * (rxmin - x0) / (x1 - x0);
					x = rxmin;
				} else {
					code3 = 0;
				}

				if (code3 != 0) {
					// Now we move outside point to intersection point to clip
					// and get ready for next pass.
					if (code3 == code1) {
						x0 = x;
						y0 = y;
						z0 = z;
						code1 = MathUtil.getCohenSutherlandCode3D(x0, y0, z0, rxmin, rymin, rzmin, rxmax, rymax, rzmax);
					} else {
						x1 = x;
						y1 = y;
						z1 = z;
						code2 = MathUtil.getCohenSutherlandCode3D(x1, y1, z1, rxmin, rymin, rzmin, rxmax, rymax, rzmax);
					}
				}
			}
		}
		if (accept) {
			set(x0, y0, z0, x1, y1, z1);
		}
		return accept;
	}

	@Pure
	@Override
	default boolean intersects(Sphere3afp<?, ?, ?, ?, ?, ?> sphere) {
		assert sphere != null : AssertMessages.notNullParameter();
		return Sphere3afp.intersectsSphereSegment(
				sphere.getX(), sphere.getY(), sphere.getZ(),
				sphere.getRadius(),
				getX1(), getY1(), getZ1(),
				getX2(), getY2(), getZ2());
	}

	@Pure
	@Override
	default boolean intersects(RectangularPrism3afp<?, ?, ?, ?, ?, ?> prism) {
		assert prism != null : AssertMessages.notNullParameter();
		return RectangularPrism3afp.intersectsRectangleSegment(
				prism.getMinX(), prism.getMinY(),
				prism.getMaxX(), prism.getMaxY(),
				getX1(), getY1(),
				getX2(), getY2());
	}

	@Pure
	@Override
	default boolean intersects(Segment3afp<?, ?, ?, ?, ?, ?> segment) {
		assert segment != null : AssertMessages.notNullParameter();
		return intersectsSegmentSegmentWithEnds(
				getX1(), getY1(), getZ1(),
				getX2(), getY2(), getZ2(),
				segment.getX1(), segment.getY1(), segment.getZ1(),
				segment.getX2(), segment.getY2(), segment.getZ2());
	}

	@Pure
	@Override
	default boolean intersects(PathIterator3afp<?> iterator) {
		assert iterator != null : AssertMessages.notNullParameter();
		final int mask = iterator.getWindingRule() == PathWindingRule.NON_ZERO ? -1 : 2;
		final int crossings = Path3afp.computeCrossingsFromSegment(
				0,
				iterator,
				getX1(), getY1(), getZ1(), getX2(), getY2(), getZ2(),
				CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON);
		return crossings == MathConstants.SHAPE_INTERSECTS || (crossings & mask) != 0;

	}

	@Pure
	@Override
	default boolean intersects(MultiShape3afp<?, ?, ?, ?, ?, ?, ?> multishape) {
		assert multishape != null : AssertMessages.notNullParameter();
		return multishape.intersects(this);
	}

	/**
	 * Result of the intersection between segments in a context where a single test is not enough.
	 *
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 13.0
	 * @see Segment3afp#getNoSegmentSegmentWithEndsIntersection(double, double, double, double, double, double, double, double,
	 *      double, double, double, double)
	 * @see Segment3afp#getNoSegmentSegmentWithoutEndsIntersection(double, double, double, double, double, double, double, double,
	 *      double, double, double, double)
	 */
	enum UncertainIntersection {
		/** Intersection, uncertainly.
		 */
		PERHAPS {
			@Override
			public boolean booleanValue() {
				return true;
			}
		},
		/** No intersection, certainly.
		 */
		NO {
			@Override
			public boolean booleanValue() {
				return false;
			}
		};

		/** Replies the boolean representation of the constant.
		 *
		 * @return the boolean representation.
		 */
		public abstract boolean booleanValue();

		/** Replies the UncertainIntersection for the given boolean.
		 *
		 * @param value the boolean value.
		 * @return the UncertainIntersection.
		 */
		public static UncertainIntersection fromBoolean(boolean value) {
			if (value) {
				return UncertainIntersection.PERHAPS;
			}
			return UncertainIntersection.NO;
		}

	}

	@Pure
	@Override
	default PathIterator3afp<IE> getPathIterator(Transform3D transform) {
		return new SegmentPathIterator<>(this, transform);
	}

	@Pure
	@Override
	default P getClosestPointTo(Point3D<?, ?> pt) {
		assert pt != null : AssertMessages.notNullParameter();
		final P point = getGeomFactory().newPoint();
		Segment3afp.computeClosestPointToPoint(
				getX1(), getY1(), getZ1(),
				getX2(), getY2(), getZ2(),
				pt.getX(), pt.getY(), pt.getZ(),
				point);
		return point;
	}

	@Pure
	@Override
	default P getClosestPointTo(Sphere3afp<?, ?, ?, ?, ?, ?> sphere) {
		assert sphere != null : AssertMessages.notNullParameter();
		return getClosestPointTo(sphere.getCenter());
	}

	@Override
	@Unefficient
	default P getClosestPointTo(Path3afp<?, ?, ?, ?, ?, ?> path) {
		assert path != null : AssertMessages.notNullParameter();
		final P point = getGeomFactory().newPoint();
		Path3afp.getClosestPointTo(getPathIterator(), path.getPathIterator(), point);
		return point;
	}

	@Pure
	@Override
	default P getClosestPointTo(RectangularPrism3afp<?, ?, ?, ?, ?, ?> rectangle) {
		assert rectangle != null : AssertMessages.notNullParameter();
		final P point = getGeomFactory().newPoint();
		computeClosestPointToRectangle(getX1(), getY1(), getZ1(), getX2(), getY2(), getZ2(), rectangle.getMinX(),
				rectangle.getMinY(), rectangle.getMinZ(), rectangle.getWidth(), rectangle.getHeight(), rectangle.getDepth(),
				point);
		return point;
	}

	@Pure
	@Override
	default P getClosestPointTo(Segment3afp<?, ?, ?, ?, ?, ?> segment) {
		assert segment != null : AssertMessages.notNullParameter();
		final P point = getGeomFactory().newPoint();
		computeClosestPointToSegment(getX1(), getY1(), getZ1(), getX2(), getY2(), getZ2(),
				segment.getX1(), segment.getY1(), segment.getZ1(), segment.getX2(), segment.getY2(), segment.getZ2(), point);
		return point;
	}

	@Pure
	@Override
	default P getFarthestPointTo(Point3D<?, ?> pt) {
		assert pt != null : AssertMessages.notNullParameter();
		final P point = getGeomFactory().newPoint();
		Segment3afp.computeFarthestPointTo(
				getX1(), getY1(), getZ1(),
				getX2(), getY2(), getZ2(),
				pt.getX(), pt.getY(), pt.getZ(),
				point);
		return point;
	}

	/** Iterator on the path elements of the segment.
	 *
	 * @param <T> the type of the path elements.
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 13.0
	 */
	class SegmentPathIterator<T extends PathElement3afp> implements PathIterator3afp<T> {

		private final Segment3afp<?, ?, T, ?, ?, ?> segment;

		private final Point3D<?, ?> p1;

		private final Point3D<?, ?> p2;

		private final Transform3D transform;

		private final double x1;

		private final double y1;

		private final double z1;

		private final double x2;

		private final double y2;

		private final double z2;

		private int index;

		/**
		 * @param segment the iterated segment.
		 * @param transform the transformation, or <code>null</code>.
		 */
		public SegmentPathIterator(Segment3afp<?, ?, T, ?, ?, ?> segment, Transform3D transform) {
			assert segment != null : AssertMessages.notNullParameter();
			this.segment = segment;
			this.p1 = new InnerComputationPoint3afp();
			this.p2 = new InnerComputationPoint3afp();
			this.transform = (transform == null || transform.isIdentity()) ? null : transform;
			this.x1 = segment.getX1();
			this.y1 = segment.getY1();
			this.z1 = segment.getZ1();
			this.x2 = segment.getX2();
			this.y2 = segment.getY2();
			this.z2 = segment.getZ2();
			if (this.x1 == this.x2 && this.y1 == this.y2 && this.z1 == this.z2) {
				this.index = 2;
			}
		}

		@Override
		public PathIterator3afp<T> restartIterations() {
			return new SegmentPathIterator<>(this.segment, this.transform);
		}

		@Pure
		@Override
		public boolean hasNext() {
			return this.index <= 1;
		}

		@Override
		public T next() {
			if (this.index > 1) {
				throw new NoSuchElementException();
			}
			final int idx = this.index;
			++this.index;
			switch (idx) {
			case 0:
				this.p2.set(this.x1, this.y1, this.z1);
				if (this.transform != null) {
					this.transform.transform(this.p2);
				}
				return this.segment.getGeomFactory().newMovePathElement(
						this.p2.getX(), this.p2.getY(), this.p2.getZ());
			case 1:
				this.p1.set(this.p2);
				this.p2.set(this.x2, this.y2, this.z2);
				if (this.transform != null) {
					this.transform.transform(this.p2);
				}
				return this.segment.getGeomFactory().newLinePathElement(
						this.p1.getX(), this.p1.getY(), this.p1.getZ(),
						this.p2.getX(), this.p2.getY(), this.p2.getZ());
			default:
				throw new NoSuchElementException();
			}
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}

		@Pure
		@Override
		public PathWindingRule getWindingRule() {
			return PathWindingRule.NON_ZERO;
		}

		@Pure
		@Override
		public boolean isPolyline() {
			return true;
		}

		@Pure
		@Override
		public boolean isCurved() {
			return false;
		}

		@Pure
		@Override
		public boolean isPolygon() {
			return false;
		}

		@Pure
		@Override
		public boolean isMultiParts() {
			return false;
		}

		@Override
		public GeomFactory3afp<T, ?, ?, ?> getGeomFactory() {
			return this.segment.getGeomFactory();
		}

	}

}
