/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2020 The original authors, and other authors.
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

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.MathConstants;
import org.arakhne.afc.math.MathUtil;
import org.arakhne.afc.math.geometry.CrossingComputationType;
import org.arakhne.afc.math.geometry.GeomConstants;
import org.arakhne.afc.math.geometry.PathWindingRule;
import org.arakhne.afc.math.geometry.d3.Point3D;
import org.arakhne.afc.math.geometry.d3.Transform3D;
import org.arakhne.afc.math.geometry.d3.Vector3D;
import org.arakhne.afc.vmutil.asserts.AssertMessages;

/** Fonctional interface that represented a 2D rectangle on a plane.
 *
 * @param <ST> is the type of the general implementation.
 * @param <IT> is the type of the implementation of this shape.
 * @param <IE> is the type of the path elements.
 * @param <P> is the type of the points.
 * @param <V> is the type of the vectors.
 * @param <B> is the type of the bounding boxes.
 * @author $Author: sgalland$
 * @author $Author: hjaffali$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public interface RectangularPrism3afp<
		ST extends Shape3afp<?, ?, IE, P, V, B>,
		IT extends RectangularPrism3afp<?, ?, IE, P, V, B>,
		IE extends PathElement3afp,
		P extends Point3D<? super P, ? super V>,
		V extends Vector3D<? super V, ? super P>,
		B extends RectangularPrism3afp<?, ?, IE, P, V, B>>
		extends Prism3afp<ST, IT, IE, P, V, B> {

    /** Compute the point on the rectangle that is the closest to the given point.
     *
     * @param rminx the minimum x coordinate of the rectangle.
     * @param rminy the minimum y coordinate of the rectangle.
     * @param rminz the minimum z coordinate of the rectangle.
     * @param rmaxx the maximum x coordinate of the rectangle.
     * @param rmaxy the maximum y coordinate of the rectangle.
     * @param rmaxz the maximum z coordinate of the rectangle.
     * @param px the x coordinate of the point.
     * @param py the y coordinate of the point.
     * @param pz the z coordinate of the point.
     * @param closest is set with the closest point on the rectangle.
     */
    @Pure
    @SuppressWarnings({"checkstyle:magicnumber", "checkstyle:parameternumber"})
    static void computeClosestPointRectanglePoint(double rminx, double rminy, double rminz, double rmaxx, double rmaxy,
            double rmaxz, double px, double py, double pz, Point3D<?, ?> closest) {
        assert rmaxx >= rminx : AssertMessages.lowerEqualParameters(0, rminx, 3, rmaxx);
        assert rmaxy >= rminy : AssertMessages.lowerEqualParameters(1, rminy, 4, rmaxy);
        assert rmaxz >= rminz : AssertMessages.lowerEqualParameters(2, rminy, 5, rmaxy);
        final double x;
        if (px < rminx) {
            x = rminx;
        } else if (px > rmaxx) {
            x = rmaxx;
        } else {
            x = px;
        }
        final double y;
        if (py < rminy) {
            y = rminy;
        } else if (py > rmaxy) {
            y = rmaxy;
        } else {
            y = py;
        }
        final double z;
        if (pz < rminz) {
            z = rminz;
        } else if (pz > rmaxz) {
            z = rmaxz;
        } else {
            z = pz;
        }
        closest.set(x, y, z);
    }

    /** Compute the point on the first rectangle that is the closest to the second rectangle.
     *
     * @param rminx1 the minimum x coordinate of the first rectangle.
     * @param rminy1 the minimum y coordinate of the first rectangle.
     * @param rminz1 the minimum z coordinate of the first rectangle.
     * @param rmaxx1 the maximum x coordinate of the first rectangle.
     * @param rmaxy1 the maximum y coordinate of the first rectangle.
     * @param rmaxz1 the maximum z coordinate of the first rectangle.
     * @param rminx2 the minimum x coordinate of the second rectangle.
     * @param rminy2 the minimum y coordinate of the second rectangle.
     * @param rminz2 the minimum z coordinate of the second rectangle.
     * @param rmaxx2 the maximum x coordinate of the second rectangle.
     * @param rmaxy2 the maximum y coordinate of the second rectangle.
     * @param rmaxz2 the maximum z coordinate of the second rectangle.
     * @param closest is set with the closest point on the first rectangle.
     */
    @Pure
    @SuppressWarnings({"checkstyle:parameternumber", "checkstyle:magicnumber"})
    static void computeClosestPointRectangleRectangle(
            double rminx1, double rminy1, double rminz1, double rmaxx1, double rmaxy1, double rmaxz1,
            double rminx2, double rminy2, double rminz2, double rmaxx2, double rmaxy2, double rmaxz2,
            Point3D<?, ?> closest) {
        assert rmaxx1 >= rminx1 : AssertMessages.lowerEqualParameters(0, rminx1, 3, rmaxx1);
        assert rmaxy1 >= rminy1 : AssertMessages.lowerEqualParameters(1, rminy1, 4, rmaxy1);
        assert rmaxz1 >= rminz1 : AssertMessages.lowerEqualParameters(2, rminz1, 5, rmaxz1);
        assert rmaxx2 >= rminx2 : AssertMessages.lowerEqualParameters(6, rminx2, 9, rmaxx2);
        assert rmaxy2 >= rminy2 : AssertMessages.lowerEqualParameters(7, rminy2, 10, rmaxy2);
        assert rmaxz2 >= rminz2 : AssertMessages.lowerEqualParameters(8, rminz2, 11, rmaxz2);
        final double px;
        final double cx = (rminx2 + rmaxx2) / 2.;
        if (cx <= rminx1) {
            px = rminx1;
        } else if (cx >= rmaxx1) {
            px = rmaxx1;
        } else {
            px = cx;
        }
        final double py;
        final double cy = (rminy2 + rmaxy2) / 2.;
        if (cy <= rminy1) {
            py = rminy1;
        } else if (cy >= rmaxy1) {
            py = rmaxy1;
        } else {
            py = cy;
        }
        final double pz;
        final double cz = (rminz2 + rmaxz2) / 2.;
        if (cz <= rminx1) {
            pz = rminy1;
        } else if (cz >= rmaxz1) {
            pz = rmaxz1;
        } else {
            pz = cz;
        }
        closest.set(px, py, pz);
    }

    /** Compute the point on the rectangle that is the closest to the segment.
     *
     * @param rminx the minimum x coordinate of the rectangle.
     * @param rminy the minimum y coordinate of the rectangle.
     * @param rminz the minimum z coordinate of the rectangle.
     * @param rmaxx the maximum x coordinate of the rectangle.
     * @param rmaxy the maximum y coordinate of the rectangle.
     * @param rmaxz the maximum z coordinate of the rectangle.
     * @param sx1 the x coordinate of the first point of the segment.
     * @param sy1 the y coordinate of the first point of the segment.
     * @param sz1 the z coordinate of the first point of the segment.
     * @param sx2 the x coordinate of the second point of the segment.
     * @param sy2 the y coordinate of the second point of the segment.
     * @param sz2 the z coordinate of the second point of the segment.
     * @param closest is set with the closest point on the rectangle.
     */
    @Pure
    @SuppressWarnings({"checkstyle:parameternumber", "checkstyle:magicnumber"})
    static void computeClosestPointRectangleSegment(
            double rminx, double rminy, double rminz, double rmaxx, double rmaxy, double rmaxz,
            double sx1, double sy1, double sz1, double sx2, double sy2, double sz2,
            Point3D<?, ?> closest) {
        assert rmaxx >= rminx : AssertMessages.lowerEqualParameters(0, rminx, 3, rmaxx);
        assert rmaxy >= rminy : AssertMessages.lowerEqualParameters(1, rminy, 4, rmaxy);
        assert rmaxz >= rminz : AssertMessages.lowerEqualParameters(2, rminz, 5, rmaxz);
        final int code1 = MathUtil.getCohenSutherlandCode3D(sx1, sy1, sz1, rminx, rminy, rminz, rmaxx, rmaxy, rmaxz);
        final int code2 = MathUtil.getCohenSutherlandCode3D(sx2, sy2, sz2, rminx, rminy, rminz, rmaxx, rmaxy, rmaxz);
        final Point3D<?, ?> tmp1 = new InnerComputationPoint3afp();
        final int zone = RectangularPrism3afp.reduceCohenSutherlandZoneRectangleSegment(
                rminx, rminy, rminz, rmaxx, rmaxy, rmaxz,
                sx1, sy1, sz1, sx2, sy2, sz2,
                code1, code2,
                tmp1, null);
        final double closex;
        final double closey;
        final double closez;
        if ((zone & MathConstants.COHEN_SUTHERLAND_LEFT) != 0) {
            closex = rminx;
            if (sx1 >= sx2) {
                closey = MathUtil.clamp(sy1, rminy, rmaxy);
                closez = MathUtil.clamp(sz1, rminz, rmaxz);
            } else {
                closey = MathUtil.clamp(sy2, rminy, rmaxy);
                closez = MathUtil.clamp(sz2, rminz, rmaxz);
            }
        } else if ((zone & MathConstants.COHEN_SUTHERLAND_RIGHT) != 0) {
            closex = rmaxx;
            if (sx1 <= sx2) {
                closey = MathUtil.clamp(sy1, rminy, rmaxy);
                closez = MathUtil.clamp(sz1, rminz, rmaxz);
            } else {
                closey = MathUtil.clamp(sy2, rminy, rmaxy);
                closez = MathUtil.clamp(sz2, rminz, rmaxz);
            }
        } else if ((zone & MathConstants.COHEN_SUTHERLAND_BOTTOM) != 0) {
            closey = rminy;
            if (sy1 >= sy2) {
                closex = MathUtil.clamp(sx1, rminx, rmaxx);
                closez = MathUtil.clamp(sz1, rminz, rmaxz);
            } else {
                closex = MathUtil.clamp(sx2, rminx, rmaxx);
                closez = MathUtil.clamp(sz2, rminz, rmaxz);
            }
        } else if ((zone & MathConstants.COHEN_SUTHERLAND_TOP) != 0) {
            closey = rmaxy;
            if (sy1 <= sy2) {
                closex = MathUtil.clamp(sx1, rminx, rmaxx);
                closez = MathUtil.clamp(sz1, rminz, rmaxz);
            } else {
                closex = MathUtil.clamp(sx2, rminx, rmaxx);
                closez = MathUtil.clamp(sz2, rminz, rmaxz);
            }
        } else if ((zone & MathConstants.COHEN_SUTHERLAND_FRONT) != 0) {
            closez = rminz;
            if (sz1 >= sz2) {
                closex = MathUtil.clamp(sx1, rminx, rmaxx);
                closey = MathUtil.clamp(sy1, rminy, rmaxy);
            } else {
                closex = MathUtil.clamp(sx2, rminx, rmaxx);
                closey = MathUtil.clamp(sy2, rminy, rmaxy);
            }
        } else if ((zone & MathConstants.COHEN_SUTHERLAND_BACK) != 0) {
            closez = rmaxz;
            if (sy1 <= sy2) {
                closex = MathUtil.clamp(sx1, rminx, rmaxx);
                closey = MathUtil.clamp(sy1, rminy, rmaxy);
            } else {
                closex = MathUtil.clamp(sx2, rminx, rmaxx);
                closey = MathUtil.clamp(sy2, rminy, rmaxy);
            }
        } else {
            closex = tmp1.getX();
            closey = tmp1.getY();
            closez = tmp1.getZ();
        }
        closest.set(closex, closey, closez);
    }

    /** Update the given Cohen-Sutherland code that corresponds to the given segment in order
     * to obtain a segment restricted to a single Cohen-Sutherland zone.
     * This function is at the heart of the
     * <a href="http://en.wikipedia.org/wiki/Cohen%E2%80%93Sutherland_algorithm">Cohen-Sutherland algorithm</a>.
     *
     * <p>The result of this function may be: <ul>
     * <li>the code for a single zone, or</li>
     * <li>the code that corresponds to a single column, or </li>
     * <li>the code that corresponds to a single row.</li>
     * </ul>
     *
     * @param rx1 is the first corner of the rectangle.
     * @param ry1 is the first corner of the rectangle.
     * @param rz1 is the first corner of the rectangle.
     * @param rx2 is the second corner of the rectangle.
     * @param ry2 is the second corner of the rectangle.
     * @param rz2 is the second corner of the rectangle.
     * @param sx1 is the first point of the segment.
     * @param sy1 is the first point of the segment.
     * @param sz1 is the first point of the segment.
     * @param sx2 is the second point of the segment.
     * @param sy2 is the second point of the segment.
     * @param sz2 is the second point of the segment.
     * @param codePoint1 the Cohen-Sutherland code for the first point of the segment.
     * @param codePoint2 the Cohen-Sutherland code for the second point of the segment.
     * @param newSegmentP1 is set with the new coordinates of the segment first point. If <code>null</code>,
     *     this parameter is ignored.
     * @param newSegmentP2 is set with the new coordinates of the segment second point. If <code>null</code>,
     *     this parameter is ignored.
     * @return the rectricted Cohen-Sutherland zone.
     */
    @Pure
    @SuppressWarnings({ "checkstyle:parameternumber", "checkstyle:npathcomplexity", "checkstyle:cyclomaticcomplexity",
            "checkstyle:magicnumber" })
    static int reduceCohenSutherlandZoneRectangleSegment(double rx1, double ry1, double rz1, double rx2, double ry2, double rz2,
            double sx1, double sy1, double sz1, double sx2, double sy2, double sz2, int codePoint1, int codePoint2,
            Point3D<?, ?> newSegmentP1, Point3D<?, ?> newSegmentP2) {
        assert rx1 <= rx2 : AssertMessages.lowerEqualParameters(0, rx1, 3, rx2);
        assert ry1 <= ry2 : AssertMessages.lowerEqualParameters(1, ry1, 4, ry2);
        assert rz1 <= rz2 : AssertMessages.lowerEqualParameters(2, ry1, 5, ry2);
        assert codePoint1 == MathUtil.getCohenSutherlandCode3D(sx1, sy1, sz1, rx1, ry1, rz1, rx2, ry2, rz2) : AssertMessages
                .invalidValue(8);
        assert codePoint2 == MathUtil.getCohenSutherlandCode3D(sx2, sy2, sz2, rx1, ry1, rz1, rx2, ry2, rz2) : AssertMessages
                .invalidValue(9);
        double segmentX1 = sx1;
        double segmentY1 = sy1;
        double segmentZ1 = sy1;
        double segmentX2 = sx2;
        double segmentY2 = sy2;
        double segmentZ2 = sy2;

        int code1 = codePoint1;
        int code2 = codePoint2;

        while (true) {
            if ((code1 | code2) == 0) {
                // Bitwise OR is 0. Trivially accept and get out of loop
                if (newSegmentP1 != null) {
                    newSegmentP1.set(segmentX1, segmentY1, segmentZ1);
                }
                if (newSegmentP2 != null) {
                    newSegmentP2.set(segmentX2, segmentY2, segmentZ2);
                }
                return 0;
            }
            if ((code1 & code2) != 0) {
                // Bitwise AND is not 0. Trivially reject and get out of loop
                if (newSegmentP1 != null) {
                    newSegmentP1.set(segmentX1, segmentY1, segmentZ1);
                }
                if (newSegmentP2 != null) {
                    newSegmentP2.set(segmentX2, segmentY2, segmentZ2);
                }
                return code1 & code2;
            }

            // failed both tests, so calculate the line segment intersection

            // At least one endpoint is outside the clip rectangle; pick it.
            int code3 = (code1 != 0) ? code1 : code2;

            double x = 0;
            double y = 0;
            double z = 0;

            // Now find the intersection point;
            // use formulas y = y0 + slope * (x - x0), x = x0 + (1 / slope) * (y - y0)
            if ((code3 & MathConstants.COHEN_SUTHERLAND_TOP) != 0) {
                // point is above the clip rectangle
                x = segmentX1 + (segmentX2 - segmentX1) * (ry2 - segmentY1) / (segmentY2 - segmentY1);
                y = ry2;
                z = rz2;
            } else if ((code3 & MathConstants.COHEN_SUTHERLAND_BOTTOM) != 0) {
                // point is below the clip rectangle
                x = segmentX1 + (segmentX2 - segmentX1) * (ry1 - segmentY1) / (segmentY2 - segmentY1);
                y = ry1;
                z = rz1;
            } else if ((code3 & MathConstants.COHEN_SUTHERLAND_RIGHT) != 0) {
                // point is to the right of clip rectangle
                y = segmentY1 + (segmentY2 - segmentY1) * (rx2 - segmentX1) / (segmentX2 - segmentX1);
                x = rx2;
                z = rz2;
            } else if ((code3 & MathConstants.COHEN_SUTHERLAND_LEFT) != 0) {
                // point is to the left of clip rectangle
                y = segmentY1 + (segmentY2 - segmentY1) * (rx1 - segmentX1) / (segmentX2 - segmentX1);
                x = rx1;
                z = rz1;
            } else if ((code3 & MathConstants.COHEN_SUTHERLAND_FRONT) != 0) {
                // point is to the right of clip rectangle
                z = segmentZ1 + (segmentZ2 - segmentZ1) * (rz2 - segmentZ1) / (segmentZ2 - segmentZ1);
                x = rx2;
                y = ry2;
            } else if ((code3 & MathConstants.COHEN_SUTHERLAND_BACK) != 0) {
                // point is to the left of clip rectangle
                z = segmentZ1 + (segmentZ2 - segmentZ1) * (rz1 - segmentZ1) / (segmentZ2 - segmentZ1);
                x = rx1;
                y = ry1;
            } else {
                code3 = 0;
            }

            if (code3 != 0) {
                // Now we move outside point to intersection point to clip
                // and get ready for next pass.
                if (code3 == code1) {
                    segmentX1 = x;
                    segmentY1 = y;
                    segmentZ1 = z;
                    code1 = MathUtil.getCohenSutherlandCode3D(segmentX1, segmentY1, segmentZ1, rx1, ry1, rz1, rx2, ry2, rz2);
                } else {
                    segmentX2 = x;
                    segmentY2 = y;
                    segmentZ2 = z;
                    code2 = MathUtil.getCohenSutherlandCode3D(segmentX2, segmentY2, segmentZ2, rx1, ry1, rz1, rx2, ry2, rz2);
                }
            }
        }
    }

	/** Replies if two rectangles are intersecting.
	 *
	 * @param x1 is the first corner of the first rectangle.
	 * @param y1 is the first corner of the first rectangle.
	 * @param z1 is the first corner of the first rectangle.
	 * @param x2 is the second corner of the first rectangle.
	 * @param y2 is the second corner of the first rectangle.
	 * @param z2 is the second corner of the first rectangle.
	 * @param x3 is the first corner of the second rectangle.
	 * @param y3 is the first corner of the second rectangle.
	 * @param z3 is the first corner of the second rectangle.
	 * @param x4 is the second corner of the second rectangle.
	 * @param y4 is the second corner of the second rectangle.
	 * @param z4 is the second corner of the second rectangle.
	 * @return <code>true</code> if the two shapes are intersecting; otherwise
	 * <code>false</code>
	 */
	@Pure
	@SuppressWarnings({"checkstyle:parameternumber", "checkstyle:magicnumber"})
    static boolean intersectsRectangleRectangle(double x1, double y1, double z1, double x2, double y2, double z2, double x3,
            double y3, double z3, double x4, double y4, double z4) {
		assert x1 <= x2 : AssertMessages.lowerEqualParameters(0, x1, 3, x2);
		assert y1 <= y2 : AssertMessages.lowerEqualParameters(1, y1, 4, y2);
		assert z1 <= z2 : AssertMessages.lowerEqualParameters(2, z1, 5, z2);
		assert x3 <= x4 : AssertMessages.lowerEqualParameters(6, x3, 9, x4);
		assert y3 <= y4 : AssertMessages.lowerEqualParameters(7, y3, 10, y4);
		assert z3 <= z4 : AssertMessages.lowerEqualParameters(8, z3, 11, z4);
		return x2 > x3 && x1 < x4 && y2 > y3 && y1 < y4 && z2 > z3 && z1 < z4;
	}

	/** Replies if two rectangles are intersecting.
	 *
	 * @param x1 is the first corner of the rectangle.
	 * @param y1 is the first corner of the rectangle.
	 * @param z1 is the first corner of the rectangle.
	 * @param x2 is the second corner of the rectangle.
	 * @param y2 is the second corner of the rectangle.
	 * @param z2 is the second corner of the rectangle.
	 * @param x3 is the first point of the line.
	 * @param y3 is the first point of the line.
	 * @param z3 is the first point of the line.
	 * @param x4 is the second point of the line.
	 * @param y4 is the second point of the line.
	 * @param z4 is the second point of the line.
	 * @return <code>true</code> if the two shapes are intersecting; otherwise
	 * <code>false</code>
	 */
	@Pure
	// TODO
	@SuppressWarnings({"checkstyle:parameternumber", "checkstyle:magicnumber"})
    static boolean intersectsRectangleLine(double x1, double y1, double z1, double x2, double y2, double z2, double x3, double y3,
            double z3, double x4, double y4, double z4) {
		assert x1 <= x2 : AssertMessages.lowerEqualParameters(0, x1, 3, x2);
		assert y1 <= y2 : AssertMessages.lowerEqualParameters(1, y1, 4, y2);
		assert z1 <= z2 : AssertMessages.lowerEqualParameters(2, z1, 5, z2);
	    //		int a, b;
	    //		a = Segment2afp.ccw(x3, y3, x4, y4, x1, y1, 0.);
	    //		b = Segment2afp.ccw(x3, y3, x4, y4, x2, y1, 0.);
	    //		if (a!=b && b!=0) return true;
	    //		b = Segment2afp.ccw(x3, y3, x4, y4, x2, y2, 0.);
	    //		if (a!=b && b!=0) return true;
	    //		b = Segment2afp.ccw(x3, y3, x4, y4, x1, y2, 0.);
	    //		return (a!=b && b!=0);
	    return false;
	}

	/** Replies if the rectangle is intersecting the segment.
	 *
	 * @param rx1 is the first corner of the rectangle.
	 * @param ry1 is the first corner of the rectangle.
	 * @param rx2 is the second corner of the rectangle.
	 * @param ry2 is the second corner of the rectangle.
	 * @param sx1 is the first point of the segment.
	 * @param sy1 is the first point of the segment.
	 * @param sx2 is the second point of the segment.
	 * @param sy2 is the second point of the segment.
	 * @return <code>true</code> if the two shapes are intersecting; otherwise
	 * <code>false</code>
	 */
	@Pure
	// TODO
	@SuppressWarnings({"checkstyle:parameternumber", "checkstyle:magicnumber"})
    static boolean intersectsRectangleSegment(double rx1, double ry1, double rx2, double ry2, double sx1, double sy1, double sx2,
            double sy2) {
		assert rx1 <= rx2 : AssertMessages.lowerEqualParameters(0, rx1, 3, rx2);
		assert ry1 <= ry2 : AssertMessages.lowerEqualParameters(1, ry1, 4, ry2);
	    //		double segmentX1 = sx1;
	    //		double segmentY1 = sy1;
	    //		double segmentX2 = sx2;
	    //		double segmentY2 = sy2;
	    //
	    //		int code1 = MathUtil.getCohenSutherlandCode(segmentX1, segmentY1, rx1, ry1, rx2, ry2);
	    //		int code2 = MathUtil.getCohenSutherlandCode(segmentX2, segmentY2, rx1, ry1, rx2, ry2);
	    //
	    //		while (true) {
	    //			if ((code1 | code2) == 0) {
	    //				// Bitwise OR is 0. Trivially accept and get out of loop
	    //				// Special case: if the segment is empty, it is on the border => no intersection.
	    //				return segmentX1 != segmentX2 || segmentY1 != segmentY2;
	    //			}
	    //			if ((code1 & code2) != 0) {
	    //				// Bitwise AND is not 0. Trivially reject and get out of loop
	    //				return false;
	    //			}
	    //
	    //			// failed both tests, so calculate the line segment intersection
	    //
	    //			// At least one endpoint is outside the clip rectangle; pick it.
	    //			int code3 = (code1 != 0) ? code1 : code2;
	    //
	    //			double x = 0;
	    //			double y = 0;
	    //
	    //			// Now find the intersection point;
	    //			// use formulas y = y0 + slope * (x - x0), x = x0 + (1 / slope) * (y - y0)
	    //			if ((code3 & MathConstants.COHEN_SUTHERLAND_TOP) != 0) {
	    //				// point is above the clip rectangle
	    //				x = segmentX1 + (segmentX2 - segmentX1) * (ry2 - segmentY1) / (segmentY2 - segmentY1);
	    //				y = ry2;
	    //			}
	    //			else if ((code3 & MathConstants.COHEN_SUTHERLAND_BOTTOM) != 0) {
	    //				// point is below the clip rectangle
	    //				x = segmentX1 + (segmentX2 - segmentX1) * (ry1 - segmentY1) / (segmentY2 - segmentY1);
	    //				y = ry1;
	    //			}
	    //			else if ((code3 & MathConstants.COHEN_SUTHERLAND_RIGHT) != 0) {
	    //				// point is to the right of clip rectangle
	    //				y = segmentY1 + (segmentY2 - segmentY1) * (rx2 - segmentX1) / (segmentX2 - segmentX1);
	    //				x = rx2;
	    //			}
	    //			else if ((code3 & MathConstants.COHEN_SUTHERLAND_LEFT) != 0) {
	    //				// point is to the left of clip rectangle
	    //				y = segmentY1 + (segmentY2 - segmentY1) * (rx1 - segmentX1) / (segmentX2 - segmentX1);
	    //				x = rx1;
	    //			}
	    //			else {
	    //				code3 = 0;
	    //			}
	    //
	    //			if (code3 != 0) {
	    //				// Now we move outside point to intersection point to clip
	    //				// and get ready for next pass.
	    //				if (code3 == code1) {
	    //					segmentX1 = x;
	    //					segmentY1 = y;
	    //					code1 = MathUtil.getCohenSutherlandCode(segmentX1, segmentY1, rx1, ry1, rx2, ry2);
	    //				}
	    //				else {
	    //					segmentX2 = x;
	    //					segmentY2 = y;
	    //					code2 = MathUtil.getCohenSutherlandCode(segmentX2, segmentY2, rx1, ry1, rx2, ry2);
	    //				}
	    //			}
	    //		}
	    return false;
	}

	/** Replies if a rectangle is inside in the rectangle.
	 *
	 * @param enclosingX1 is the lowest corner of the enclosing-candidate rectangle.
	 * @param enclosingY1 is the lowest corner of the enclosing-candidate rectangle.
	 * @param enclosingZ1 is the lowest corner of the enclosing-candidate rectangle.
	 * @param enclosingX2 is the uppest corner of the enclosing-candidate rectangle.
	 * @param enclosingY2 is the uppest corner of the enclosing-candidate rectangle.
	 * @param enclosingZ2 is the uppest corner of the enclosing-candidate rectangle.
	 * @param innerX1 is the lowest corner of the inner-candidate rectangle.
	 * @param innerY1 is the lowest corner of the inner-candidate rectangle.
	 * @param innerZ1 is the lowest corner of the inner-candidate rectangle.
	 * @param innerX2 is the uppest corner of the inner-candidate rectangle.
	 * @param innerY2 is the uppest corner of the inner-candidate rectangle.
	 * @param innerZ2 is the uppest corner of the inner-candidate rectangle.
	 * @return <code>true</code> if the given rectangle is inside the ellipse;
	 *     otherwise <code>false</code>.
	 */
	@Pure
	@SuppressWarnings({"checkstyle:parameternumber", "checkstyle:magicnumber"})
	static boolean containsRectangleRectangle(double enclosingX1, double enclosingY1, double enclosingZ1,
			double enclosingX2, double enclosingY2, double enclosingZ2,
			double innerX1, double innerY1, double innerZ1,
			double innerX2, double innerY2, double innerZ2) {
		assert enclosingX1 <= enclosingX2 : AssertMessages.lowerEqualParameters(0, enclosingX1, 3, enclosingX2);
		assert enclosingY1 <= enclosingY2 : AssertMessages.lowerEqualParameters(1, enclosingY1, 4, enclosingY2);
		assert enclosingZ1 <= enclosingZ2 : AssertMessages.lowerEqualParameters(2, enclosingZ1, 5, enclosingZ2);
		assert innerX1 <= innerX2 : AssertMessages.lowerEqualParameters(0, innerX1, 3, innerX2);
		assert innerY1 <= innerY2 : AssertMessages.lowerEqualParameters(1, innerY1, 4, innerY2);
		assert innerZ1 <= innerZ2 : AssertMessages.lowerEqualParameters(2, innerZ1, 5, innerZ2);
		return innerX1 >= enclosingX1
		    && innerY1 >= enclosingY1
		    && innerZ1 >= enclosingZ1
		    && innerX2 <= enclosingX2
		    && innerY2 <= enclosingY2
		    && innerZ2 <= enclosingZ2;
	}

	/** Replies if a point is inside in the rectangle.
	 *
	 * @param rx1 is the lowest corner of the rectangle.
	 * @param ry1 is the lowest corner of the rectangle.
	 * @param rz1 is the lowest corner of the rectangle.
	 * @param rx2 is the uppest corner of the rectangle.
	 * @param ry2 is the uppest corner of the rectangle.
	 * @param rz2 is the uppest corner of the rectangle.
	 * @param px is the point.
	 * @param py is the point.
	 * @param pz is the point.
	 * @return <code>true</code> if the given point is inside the rectangle;
	 *     otherwise <code>false</code>.
	 */
	@Pure
	@SuppressWarnings({"checkstyle:parameternumber", "checkstyle:magicnumber"})
	static boolean containsRectanglePoint(
			double rx1, double ry1, double rz1, double rx2, double ry2, double rz2,
			double px, double py, double pz) {
		assert rx1 <= rx2 : AssertMessages.lowerEqualParameters(0, rx1, 3, rx2);
		assert ry1 <= ry2 : AssertMessages.lowerEqualParameters(1, ry1, 4, ry2);
		assert rz1 <= rz2 : AssertMessages.lowerEqualParameters(2, rz1, 5, rz2);
		return (px >= rx1 && px <= rx2) && (py >= ry1 && py <= ry2) && (pz >= rz1 && pz <= rz2);
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
		return getMinX() == shape.getMinX()
			&& getMinY() == shape.getMinY()
			&& getMinZ() == shape.getMinZ()
			&& getMaxX() == shape.getMaxX()
			&& getMaxY() == shape.getMaxY()
			&& getMaxZ() == shape.getMaxZ();
	}

	@Override
	default void set(IT shape) {
		assert shape != null : AssertMessages.notNullParameter();
		setFromCorners(shape.getMinX(), shape.getMinY(), shape.getMinZ(), shape.getMaxX(), shape.getMaxY(), shape.getMaxZ());
	}

	@Pure
	@Override
	default double getDistanceSquared(Point3D<?, ?> pt) {
		assert pt != null : AssertMessages.notNullParameter();
		final double dx;
        if (pt.getX() < getMinX()) {
            dx = getMinX() - pt.getX();
        } else if (pt.getX() > getMaxX()) {
            dx = pt.getX() - getMaxX();
        } else {
            dx = 0f;
        }
		final double dy;
        if (pt.getY() < getMinY()) {
            dy = getMinY() - pt.getY();
        } else if (pt.getY() > getMaxY()) {
            dy = pt.getY() - getMaxY();
        } else {
            dy = 0f;
        }
		final double dz;
        if (pt.getZ() < getMinZ()) {
            dz = getMinZ() - pt.getZ();
        } else if (pt.getZ() > getMaxZ()) {
            dz = pt.getZ() - getMaxZ();
        } else {
            dz = 0f;
        }
        return dx * dx + dy * dy + dz * dz;
	}

	@Pure
	@Override
	default double getDistanceL1(Point3D<?, ?> pt) {
		assert pt != null :  AssertMessages.notNullParameter();
		final double dx;
        if (pt.getX() < getMinX()) {
            dx = getMinX() - pt.getX();
        } else if (pt.getX() > getMaxX()) {
            dx = pt.getX() - getMaxX();
        } else {
            dx = 0f;
        }
		final double dy;
        if (pt.getY() < getMinY()) {
            dy = getMinY() - pt.getY();
        } else if (pt.getY() > getMaxY()) {
            dy = pt.getY() - getMaxY();
        } else {
            dy = 0f;
        }
		final double dz;
        if (pt.getZ() < getMinZ()) {
            dz = getMinZ() - pt.getZ();
        } else if (pt.getZ() > getMaxZ()) {
            dz = pt.getZ() - getMaxZ();
        } else {
            dz = 0f;
        }
		return dx + dy + dz;
	}

	@Pure
	@Override
	default double getDistanceLinf(Point3D<?, ?> pt) {
		assert pt != null :  AssertMessages.notNullParameter();
		final double dx;
        if (pt.getX() < getMinX()) {
            dx = getMinX() - pt.getX();
        } else if (pt.getX() > getMaxX()) {
            dx = pt.getX() - getMaxX();
        } else {
            dx = 0f;
        }
		final double dy;
        if (pt.getY() < getMinY()) {
            dy = getMinY() - pt.getY();
        } else if (pt.getY() > getMaxY()) {
            dy = pt.getY() - getMaxY();
        } else {
            dy = 0f;
        }
		final double dz;
        if (pt.getZ() < getMinZ()) {
            dz = getMinZ() - pt.getZ();
        } else if (pt.getZ() > getMaxZ()) {
            dz = pt.getZ() - getMaxZ();
        } else {
            dz = 0f;
        }
		return MathUtil.max(dx, dy, dz);
	}

	@Pure
	@Override
	default boolean contains(double x, double y, double z) {
        return x >= getMinX() && x <= getMaxX() && y >= getMinY() && y <= getMaxY() && z >= getMinZ() && z <= getMaxZ();
	}

	@Pure
	@Override
	default boolean contains(RectangularPrism3afp<?, ?, ?, ?, ?, ?> rectangularPrism) {
        assert rectangularPrism != null :  AssertMessages.notNullParameter();
        return containsRectangleRectangle(getMinX(), getMinY(), getMinZ(), getMaxX(), getMaxY(), getMaxZ(),
                rectangularPrism.getMinX(), rectangularPrism.getMinY(), rectangularPrism.getMinZ(), rectangularPrism.getMaxX(),
                rectangularPrism.getMaxY(), rectangularPrism.getMaxZ());
	}

	/** Add the given coordinate in the rectangle.
	 *
	 * <p>The corners of the rectangles are moved to
	 * enclose the given coordinate.
	 *
	 * @param pt the point
	 */
	default void add(Point3D<?, ?> pt) {
		assert pt != null :  AssertMessages.notNullParameter();
		add(pt.getX(), pt.getY(), pt.getZ());
	}

	/** Add the given coordinate in the rectangle.
	 *
	 * <p>The corners of the rectangles are moved to
	 * enclose the given coordinates.
	 *
     * @param x x coordinate of the point.
     * @param y y coordinate of the point.
	 * @param z z coordinate of the point.
	 */
	default void add(double x, double y, double z) {
		if (x < getMinX()) {
			setMinX(x);
		} else if (x > getMaxX()) {
			setMaxX(x);
		}
		if (y < getMinY()) {
			setMinY(y);
		} else if (y > getMaxY()) {
			setMaxY(y);
		}
		if (z < getMinZ()) {
			setMinZ(z);
		} else if (z > getMaxZ()) {
			setMaxZ(z);
		}
	}

	/** Compute and replies the union of this rectangle and the given rectangle.
	 * This function does not change this rectangle.
	 *
	 * <p>It is equivalent to (where <code>ur</code> is the union):
	 * <pre><code>
	 * Rectangle2f ur = new Rectangle2f(this);
	 * ur.setUnion(r);
	 * </code></pre>
	 *
	 * @param prism the prism
	 * @return the union of this rectangle and the given rectangle.
	 * @see #setUnion(Prism3afp)
	 */
	@Pure
	default B createUnion(Prism3afp<?, ?, ?, ?, ?, ?> prism) {
		assert prism != null :  AssertMessages.notNullParameter();
		final B rr = getGeomFactory().newBox();
		rr.setFromCorners(getMinX(), getMinY(), getMinZ(), getMaxX(), getMaxY(), getMaxZ());
		rr.setUnion(prism);
		return rr;
	}

	/** Compute and replies the intersection of this rectangle and the given rectangle.
	 * This function does not change this rectangle.
	 *
	 * <p>It is equivalent to (where <code>ir</code> is the intersection):
	 * <pre><code>
	 * Rectangle2f ir = new Rectangle2f(this);
	 * ir.setIntersection(r);
	 * </code></pre>
	 *
	 * @param prism the prism
	 * @return the union of this rectangle and the given rectangle.
	 * @see #setIntersection(Prism3afp)
	 */
	@Pure
	default B createIntersection(Prism3afp<?, ?, ?, ?, ?, ?> prism) {
		assert prism != null :  AssertMessages.notNullParameter();
		final B rr = getGeomFactory().newBox();
		final double x1 = Math.max(getMinX(), prism.getMinX());
		final double y1 = Math.max(getMinY(), prism.getMinY());
		final double z1 = Math.max(getMinZ(), prism.getMinZ());
		final double x2 = Math.min(getMaxX(), prism.getMaxX());
		final double y2 = Math.min(getMaxY(), prism.getMaxY());
		final double z2 = Math.min(getMaxZ(), prism.getMaxZ());
        if (x1 <= x2 && y1 <= y2 && z1 <= z2) {
			rr.setFromCorners(x1, y1, z1, x2, y2, z2);
        } else {
			rr.clear();
		}
		return rr;
	}

	/** Compute the union of this rectangle and the given rectangle and
	 * change this rectangle with the result of the union.
	 *
	 * @param prism the prism
	 * @see #createUnion(Prism3afp)
	 */
	default void setUnion(Prism3afp<?, ?, ?, ?, ?, ?> prism) {
		assert prism != null :  AssertMessages.notNullParameter();
		setFromCorners(
				Math.min(getMinX(), prism.getMinX()),
				Math.min(getMinY(), prism.getMinY()),
				Math.min(getMinZ(), prism.getMinZ()),
				Math.max(getMaxX(), prism.getMaxX()),
				Math.max(getMaxY(), prism.getMaxY()),
				Math.max(getMaxZ(), prism.getMaxZ()));
	}

	/** Compute the intersection of this rectangle and the given rectangle.
	 * This function changes this rectangle.
	 *
	 * <p>If there is no intersection, this rectangle is cleared.
	 *
	 * @param prism the prism
	 * @see #createIntersection(Prism3afp)
	 * @see #clear()
	 */
	default void setIntersection(Prism3afp<?, ?, ?, ?, ?, ?> prism) {
		assert prism != null :  AssertMessages.notNullParameter();
		final double x1 = Math.max(getMinX(), prism.getMinX());
		final double y1 = Math.max(getMinY(), prism.getMinY());
		final double z1 = Math.max(getMinZ(), prism.getMinZ());
		final double x2 = Math.min(getMaxX(), prism.getMaxX());
		final double y2 = Math.min(getMaxY(), prism.getMaxY());
		final double z2 = Math.min(getMaxZ(), prism.getMaxZ());
        if (x1 <= x2 && y1 <= y2 && z1 <= z2) {
            setFromCorners(x1, y1, z1, x2, y2, z2);
        } else {
			clear();
		}
	}

	@Pure
	@Override
	default boolean intersects(RectangularPrism3afp<?, ?, ?, ?, ?, ?> prism) {
		assert prism != null :  AssertMessages.notNullParameter();
		return intersectsRectangleRectangle(
				getMinX(), getMinY(), getMinZ(),
				getMaxX(), getMaxY(), getMaxZ(),
				prism.getMinX(), prism.getMinY(), prism.getMinZ(),
				prism.getMaxX(), prism.getMaxY(), prism.getMaxZ());
	}

	@Pure
	@Override
	default boolean intersects(Sphere3afp<?, ?, ?, ?, ?, ?> sphere) {
		assert sphere != null :  AssertMessages.notNullParameter();
		return Sphere3afp.intersectsSpherePrism(
				sphere.getX(), sphere.getY(), sphere.getZ(),
				sphere.getRadius(),
				getMinX(), getMinY(), getMinZ(),
				getMaxX(), getMaxY(), getMaxZ());
	}

	@Pure
	@Override
	default boolean intersects(Segment3afp<?, ?, ?, ?, ?, ?> segment) {
		assert segment != null :  AssertMessages.notNullParameter();
		return intersectsRectangleSegment(
				getMinX(), getMinY(),
				getMaxX(), getMaxY(),
				segment.getX1(), segment.getY1(),
				segment.getX2(), segment.getY2());
	}

	@Pure
	@Override
	default boolean intersects(PathIterator3afp<?> iterator) {
		assert iterator != null :  AssertMessages.notNullParameter();
		final int mask = iterator.getWindingRule() == PathWindingRule.NON_ZERO ? -1 : 2;
		final int crossings = Path3afp.computeCrossingsFromRect(
				0,
				iterator,
				getMinX(), getMinY(), getMinZ(), getMaxX(), getMaxY(), getMaxZ(),
				CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON);
        return crossings == GeomConstants.SHAPE_INTERSECTS || (crossings & mask) != 0;

	}

	@Pure
	@Override
	default boolean intersects(MultiShape3afp<?, ?, ?, ?, ?, ?, ?> multishape) {
		assert multishape != null :  AssertMessages.notNullParameter();
		return multishape.intersects(this);
	}

	/** Move this rectangular prism to avoid collision
	 * with the reference rectangular prism.
	 *
	 * @param reference is the rectangular prism we want to avoid collision with.
	 * @param result the displacement vector.
	 */
	// TODO : integrate z coordinate
	default void avoidCollisionWith(RectangularPrism3afp<?, ?, ?, ?, ?, ?> reference, Vector3D<?, ?> result) {
		assert reference != null :  AssertMessages.notNullParameter();
		assert result != null : AssertMessages.notNullParameter();
		final double dx1 = reference.getMaxX() - getMinX();
		final double dx2 = getMaxX() - reference.getMinX();
		final double dy1 = reference.getMaxY() - getMinY();
		final double dy2 = getMaxY() - reference.getMinY();

		final double absdx1 = Math.abs(dx1);
		final double absdx2 = Math.abs(dx2);
		final double absdy1 = Math.abs(dy1);
		final double absdy2 = Math.abs(dy2);

		double dx = 0;
		double dy = 0;
		final double dz = 0;

		// TODO compute dz
		if (dx1 >= 0 && absdx1 <= absdx2 && absdx1 <= absdy1 && absdx1 <= absdy2) {
            // Move according to dx1
            dx = dx1;
        } else if (dx2 >= 0 && absdx2 <= absdx1 && absdx2 <= absdy1 && absdx2 <= absdy2) {
            // Move according to dx2
            dx = -dx2;
        } else if (dy1 >= 0 && absdy1 <= absdx1 && absdy1 <= absdx2 && absdy1 <= absdy2) {
            // Move according to dy1
            dy = dy1;
        } else {
            // Move according to dy2
            dy = -dy2;
        }
		set(
                getMinX() + dx,
                getMinY() + dy,
                getMinZ() + dz,
				getWidth(),
				getHeight(),
				getDepth());

		result.set(dx, dy, dz);
	}

	/** Move this rectangle to avoid collision
	 * with the reference rectangle.
	 *
	 * @param reference is the rectangle to avoid collision with.
	 * @param displacementDirection is the direction of the allowed displacement (it is an input).
	 *     This vector is set according to the result before returning.
	 * @param result the displacement vector.
	 */
	default void avoidCollisionWith(RectangularPrism3afp<?, ?, ?, ?, ?, ?> reference, Vector3D<?, ?> displacementDirection,
            Vector3D<?, ?> result) {
		assert reference != null : AssertMessages.notNullParameter(0);
		assert result != null : AssertMessages.notNullParameter(2);
		if (displacementDirection == null || displacementDirection.getLengthSquared() == 0) {
			avoidCollisionWith(reference, result);
			return;
		}

		final double dx1 = reference.getMaxX() - getMinX();
		final double dx2 = reference.getMinX() - getMaxX();
		final double dy1 = reference.getMaxY() - getMinY();
		final double dy2 = reference.getMinY() - getMaxY();
		final double dz1 = reference.getMaxZ() - getMinZ();
		final double dz2 = reference.getMinZ() - getMaxZ();

		final double absdx1 = Math.abs(dx1);
		final double absdx2 = Math.abs(dx2);
		final double absdy1 = Math.abs(dy1);
		final double absdy2 = Math.abs(dy2);
		final double absdz1 = Math.abs(dz1);
		final double absdz2 = Math.abs(dz2);

		final double dx;
		final double dy;
		final double dz;

        if (displacementDirection.getX() < 0) {
            dx = -Math.min(absdx1, absdx2);
        } else {
            dx = Math.min(absdx1, absdx2);
        }

        if (displacementDirection.getY() < 0) {
            dy = -Math.min(absdy1, absdy2);
        } else {
            dy = Math.min(absdy1, absdy2);
        }

        if (displacementDirection.getZ() < 0) {
            dz = -Math.min(absdz1, absdz2);
        } else {
            dz = Math.min(absdz1, absdz2);
        }

		set(
                getMinX() + dx,
                getMinY() + dy,
                getMinZ() + dz,
				getWidth(),
				getHeight(),
				getDepth());

		displacementDirection.set(dx, dy, dz);
		result.set(dx, dy, dz);
	}

	@Pure
	@Override
	default P getClosestPointTo(Point3D<?, ?> pt) {
		assert pt != null : AssertMessages.notNullParameter();
		final double x;
		int same = 0;
        if (pt.getX() < getMinX()) {
            x = getMinX();
        } else if (pt.getX() > getMaxX()) {
            x = getMaxX();
        } else {
            x = pt.getX();
            ++same;
        }
        final double y;
        if (pt.getY() < getMinY()) {
            y = getMinY();
        } else if (pt.getY() > getMaxY()) {
            y = getMaxY();
        } else {
            y = pt.getY();
            ++same;
        }
        final double z;
        if (pt.getZ() < getMinZ()) {
            z = getMinZ();
        } else if (pt.getZ() > getMaxZ()) {
            z = getMaxZ();
        } else {
            z = pt.getZ();
            ++same;
        }
        if (same == 3) {
            return getGeomFactory().convertToPoint(pt);
        }
		return getGeomFactory().newPoint(x, y, z);
	}

    @Override
    default P getClosestPointTo(Sphere3afp<?, ?, ?, ?, ?, ?> circle) {
        assert circle != null : AssertMessages.notNullParameter();
        return getClosestPointTo(circle.getCenter());
    }

    @Override
    default P getClosestPointTo(Path3afp<?, ?, ?, ?, ?, ?> path) {
        assert path != null : AssertMessages.notNullParameter();
        final P point = getGeomFactory().newPoint();
        Path3afp.getClosestPointTo(getPathIterator(), path.getPathIterator(), point);
        return point;
    }

    @Override
    default P getClosestPointTo(RectangularPrism3afp<?, ?, ?, ?, ?, ?> rectangle) {
        assert rectangle != null : AssertMessages.notNullParameter();
        final P point = getGeomFactory().newPoint();
        computeClosestPointRectangleRectangle(getMinX(), getMinY(), getMinZ(), getMaxX(), getMaxY(), getMaxZ(),
                rectangle.getMinX(), rectangle.getMinY(), rectangle.getMinZ(), rectangle.getMaxX(), rectangle.getMaxY(),
                rectangle.getMaxZ(),
                point);
        return point;
    }

    @Override
    default P getClosestPointTo(Segment3afp<?, ?, ?, ?, ?, ?> segment) {
        assert segment != null : AssertMessages.notNullParameter();
        final P point = getGeomFactory().newPoint();
        computeClosestPointRectangleSegment(
                getMinX(), getMinY(), getMinZ(), getMaxX(), getMaxY(), getMaxZ(),
                segment.getX1(), segment.getY1(), segment.getZ1(), segment.getX2(), segment.getY2(), segment.getZ2(), point);
        return point;
    }

	@Pure
	@Override
	default P getFarthestPointTo(Point3D<?, ?> pt) {
		assert pt != null : AssertMessages.notNullParameter();
		final double x;
        if (pt.getX() <= getCenterX()) {
            x = getMaxX();
        } else {
            x = getMinX();
        }
        final double y;
        if (pt.getY() <= getCenterY()) {
            y = getMaxY();
        } else {
            y = getMinY();
        }
        final double z;
        if (pt.getZ() <= getCenterZ()) {
            z = getMaxZ();
        } else {
            z = getMinZ();
        }
		return getGeomFactory().newPoint(x, y, z);
	}

	@Pure
	@Override
	default PathIterator3afp<IE> getPathIterator(Transform3D transform) {
		if (transform == null || transform.isIdentity()) {
			return new RectanglePathIterator<>(this);
		}
		return new TransformedRectanglePathIterator<>(this, transform);
	}


	/** Iterator on the path elements of the rectangle.
	 *
	 * @param <T> the type of the path elements.
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 13.0
	 */
	@SuppressWarnings("checkstyle:magicnumber")
	class RectanglePathIterator<T extends PathElement3afp>
			implements PathIterator3afp<T> {

		private final RectangularPrism3afp<?, ?, T, ?, ?, ?> rectangle;

		private double x1;

		private double y1;

		private double z1;

		private double x2;

		private double y2;

		private double z2;

		private int index;

		/** Constructor.
		 * @param rectangle the iterated rectangle.
		 */
		public RectanglePathIterator(RectangularPrism3afp<?, ?, T, ?, ?, ?> rectangle) {
			assert rectangle != null : AssertMessages.notNullParameter();
			this.rectangle = rectangle;
			if (rectangle.isEmpty()) {
				this.index = 5;
			} else {
				this.x1 = rectangle.getMinX();
				this.x2 = rectangle.getMaxX();
				this.y1 = rectangle.getMinY();
				this.y2 = rectangle.getMaxY();
				this.z1 = rectangle.getMinZ();
				this.z2 = rectangle.getMaxZ();
			}
		}

		@Override
		public PathIterator3afp<T> restartIterations() {
			return new RectanglePathIterator<>(this.rectangle);
		}

		@Pure
		@Override
		public boolean hasNext() {
			return this.index <= 4;
		}

		@Override
		@SuppressWarnings("checkstyle:returncount")
		public T next() {
			final int idx = this.index;
            ++this.index;
            switch (idx) {
			case 0:
				return this.rectangle.getGeomFactory().newMovePathElement(
						this.x1, this.y1, this.z1);
			case 1:
				return this.rectangle.getGeomFactory().newLinePathElement(
						this.x1, this.y1, this.z1,
						this.x1, this.y1, this.z2);
			case 2:
				return this.rectangle.getGeomFactory().newLinePathElement(
						this.x1, this.y1, this.z2,
						this.x1, this.y2, this.z2);
			case 3:
				return this.rectangle.getGeomFactory().newLinePathElement(
						this.x1, this.y2, this.z2,
						this.x1, this.y2, this.z1);
			case 4:
				return this.rectangle.getGeomFactory().newLinePathElement(
						this.x1, this.y2, this.z1,
						this.x2, this.y2, this.z1);
			case 5:
				return this.rectangle.getGeomFactory().newLinePathElement(
						this.x2, this.y2, this.z1,
						this.x2, this.y2, this.z2);
			case 6:
				return this.rectangle.getGeomFactory().newLinePathElement(
						this.x2, this.y2, this.z2,
						this.x2, this.y1, this.z2);
			case 7:
				return this.rectangle.getGeomFactory().newClosePathElement(
						this.x2, this.y1, this.z2,
						this.x2, this.y1, this.z1);
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
			return false;
		}

		@Pure
		@Override
		public boolean isCurved() {
			return false;
		}

		@Pure
		@Override
		public boolean isPolygon() {
			return true;
		}

		@Pure
		@Override
		public boolean isMultiParts() {
			return false;
		}

		@Override
		public GeomFactory3afp<T, ?, ?, ?> getGeomFactory() {
			return this.rectangle.getGeomFactory();
		}

	}

	/** Iterator on the path elements of the rectangle.
	 *
	 * @param <T> the type of the path elements.
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	@SuppressWarnings("checkstyle:magicnumber")
	class TransformedRectanglePathIterator<T extends PathElement3afp> implements PathIterator3afp<T> {

		private final Transform3D transform;

		private final RectangularPrism3afp<?, ?, T, ?, ?, ?> rectangle;

		private Point3D<?, ?> p1;

		private Point3D<?, ?> p2;

		private double x1;

		private double y1;

		private double z1;

		private double x2;

		private double y2;

		private double z2;

		private int index;

		/** Constructor.
		 * @param rectangle the iterated rectangle.
		 * @param transform the transformation.
		 */
		public TransformedRectanglePathIterator(RectangularPrism3afp<?, ?, T, ?, ?, ?> rectangle, Transform3D transform) {
			assert rectangle != null : AssertMessages.notNullParameter(0);
			assert transform != null : AssertMessages.notNullParameter(1);
			this.rectangle = rectangle;
			this.transform = transform;
			if (rectangle.isEmpty()) {
				this.index = 5;
			} else {
				this.index = 0;
				this.p1 = new InnerComputationPoint3afp();
				this.p2 = new InnerComputationPoint3afp();
				this.x1 = rectangle.getMinX();
				this.x2 = rectangle.getMaxX();
				this.y1 = rectangle.getMinY();
				this.y2 = rectangle.getMaxY();
				this.z1 = rectangle.getMinZ();
				this.z2 = rectangle.getMaxZ();
			}
		}

		@Override
		public PathIterator3afp<T> restartIterations() {
			return new TransformedRectanglePathIterator<>(this.rectangle, this.transform);
		}

		@Pure
		@Override
		public boolean hasNext() {
			return this.index <= 4;
		}

		@Override
		@SuppressWarnings("checkstyle:returncount")
		public T next() {
			final int idx = this.index;
			++this.index;
            switch (idx) {
			case 0:
				this.p2.set(this.x1, this.y1, this.z1);
				this.transform.transform(this.p2);
				return this.rectangle.getGeomFactory().newMovePathElement(
						this.p2.getX(), this.p2.getY(), this.p2.getZ());
			case 1:
				this.p1.set(this.p2);
				this.p2.set(this.x1, this.y1, this.z2);
				this.transform.transform(this.p2);
				return this.rectangle.getGeomFactory().newLinePathElement(
						this.p1.getX(), this.p1.getY(), this.p1.getZ(),
						this.p2.getX(), this.p2.getY(), this.p2.getZ());
			case 2:
				this.p1.set(this.p2);
				this.p2.set(this.x1, this.y2, this.z2);
				this.transform.transform(this.p2);
				return this.rectangle.getGeomFactory().newLinePathElement(
						this.p1.getX(), this.p1.getY(), this.p1.getZ(),
						this.p2.getX(), this.p2.getY(), this.p2.getZ());
			case 3:
				this.p1.set(this.p2);
				this.p2.set(this.x1, this.y2, this.z1);
				this.transform.transform(this.p2);
				return this.rectangle.getGeomFactory().newLinePathElement(
						this.p1.getX(), this.p1.getY(), this.p1.getZ(),
						this.p2.getX(), this.p2.getY(), this.p2.getZ());
			case 4:
				this.p1.set(this.p2);
				this.p2.set(this.x2, this.y2, this.z1);
				this.transform.transform(this.p2);
				return this.rectangle.getGeomFactory().newLinePathElement(
						this.p1.getX(), this.p1.getY(), this.p1.getZ(),
						this.p2.getX(), this.p2.getY(), this.p2.getZ());
			case 5:
				this.p1.set(this.p2);
				this.p2.set(this.x2, this.y2, this.z2);
				this.transform.transform(this.p2);
				return this.rectangle.getGeomFactory().newLinePathElement(
						this.p1.getX(), this.p1.getY(), this.p1.getZ(),
						this.p2.getX(), this.p2.getY(), this.p2.getZ());
			case 6:
				this.p1.set(this.p2);
				this.p2.set(this.x2, this.y1, this.z2);
				this.transform.transform(this.p2);
				return this.rectangle.getGeomFactory().newLinePathElement(
						this.p1.getX(), this.p1.getY(), this.p1.getZ(),
						this.p2.getX(), this.p2.getY(), this.p2.getZ());
			case 7:
				this.p1.set(this.p2);
				this.p2.set(this.x2, this.y1, this.z1);
				this.transform.transform(this.p2);
				return this.rectangle.getGeomFactory().newClosePathElement(
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
			return false;
		}

		@Pure
		@Override
		public boolean isCurved() {
			return false;
		}

		@Pure
		@Override
		public boolean isPolygon() {
			return true;
		}

		@Pure
		@Override
		public boolean isMultiParts() {
			return false;
		}

		@Override
		public GeomFactory3afp<T, ?, ?, ?> getGeomFactory() {
			return this.rectangle.getGeomFactory();
		}

	}

}
