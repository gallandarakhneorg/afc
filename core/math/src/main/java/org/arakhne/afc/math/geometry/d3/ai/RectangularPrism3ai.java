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

package org.arakhne.afc.math.geometry.d3.ai;

import java.util.Iterator;
import java.util.NoSuchElementException;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.MathConstants;
import org.arakhne.afc.math.MathUtil;
import org.arakhne.afc.math.geometry.CrossingComputationType;
import org.arakhne.afc.math.geometry.PathWindingRule;
import org.arakhne.afc.math.geometry.d3.Point3D;
import org.arakhne.afc.math.geometry.d3.Transform3D;
import org.arakhne.afc.math.geometry.d3.Vector3D;
import org.arakhne.afc.math.geometry.d3.ai.Segment3ai.BresenhamLineIterator;
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
public interface RectangularPrism3ai<
		ST extends Shape3ai<?, ?, IE, P, V, B>,
		IT extends RectangularPrism3ai<?, ?, IE, P, V, B>,
		IE extends PathElement3ai,
		P extends Point3D<? super P, ? super V>,
		V extends Vector3D<? super V, ? super P>,
		B extends RectangularPrism3ai<?, ?, IE, P, V, B>>
		extends Prism3ai<ST, IT, IE, P, V, B> {

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
    static boolean intersectsRectangleRectangle(int x1, int y1, int z1, int x2, int y2, int z2, int x3, int y3, int z3,
            int x4, int y4, int z4) {
		assert x1 <= x2 : AssertMessages.lowerEqualParameters(0, x1, 3, x2);
		assert y1 <= y2 : AssertMessages.lowerEqualParameters(1, y1, 4, y2);
		assert z1 <= z2 : AssertMessages.lowerEqualParameters(2, y1, 5, z2);
		assert x3 <= x4 : AssertMessages.lowerEqualParameters(6, x3, 9, x4);
		assert y3 <= y4 : AssertMessages.lowerEqualParameters(7, y3, 10, y4);
		assert z3 <= z4 : AssertMessages.lowerEqualParameters(8, z3, 11, z4);
		return x2 > x3 && x1 < x4 && y2 > y3 && y1 < y4 && z2  > z3 && z1 < z4;
	}

	/** Replies if a rectangle is intersecting a segment.
	 *
	 *<p>The intersection test is partly based on the Cohen-Sutherland
	 * classification of the segment.
	 * This classification permits to detect the base cases;
	 * and to run a clipping-like algorithm for the intersection
	 * detection.
	 *
	 * @param x1 is the first corner of the rectangle.
	 * @param y1 is the first corner of the rectangle.
	 * @param z1 is the first corner of the rectangle.
	 * @param x2 is the second corner of the rectangle.
	 * @param y2 is the second corner of the rectangle.
	 * @param z2 is the second corner of the rectangle.
	 * @param x3 is the first point of the segment.
	 * @param y3 is the first point of the segment.
	 * @param z3 is the first point of the segment.
	 * @param x4 is the second point of the segment.
	 * @param y4 is the second point of the segment.
	 * @param z4 is the second point of the segment.
	 * @return <code>true</code> if the two shapes are intersecting; otherwise
	 * <code>false</code>
	 */
	@Pure
	@SuppressWarnings({"checkstyle:cyclomaticcomplexity", "checkstyle:parameternumber", "checkstyle:magicnumber"})
    static boolean intersectsRectangleSegment(int x1, int y1, int z1, int x2, int y2, int z2, int x3, int y3, int z3,
            int x4, int y4, int z4) {
		assert x1 <= x2 : AssertMessages.lowerEqualParameters(0, x1, 3, x2);
		assert y1 <= y2 : AssertMessages.lowerEqualParameters(1, y1, 4, y2);
		assert z1 <= z2 : AssertMessages.lowerEqualParameters(2, y1, 5, z2);

		int c1 = MathUtil.getCohenSutherlandCode3D(x3, y3, z3, x1, y1, z1, x2, y2, z2);
		final int c2 = MathUtil.getCohenSutherlandCode3D(x4, y4, z4, x1, y1, z1, x2, y2, z2);

		// 0x32; //COHEN_SUTHERLAND_BACK
		// 0x16; //COHEN_SUTHERLAND_FRONT
		// 0x8; //COHEN_SUTHERLAND_LEFT
		// 0x4; //COHEN_SUTHERLAND_RIGHT
		// 0x2; //COHEN_SUTHERLAND_BOTTOM
		// 0x1; //COHEN_SUTHERLAND_TOP

		if (c1 == MathConstants.COHEN_SUTHERLAND_INSIDE || c2 == MathConstants.COHEN_SUTHERLAND_INSIDE) {
		    return true;
		}
		if ((c1 & c2) != 0) {
			return false;
		}

		int sx1 = x3;
		int sy1 = y3;
		final int sz1 = z3;
		final int sx2 = x4;
		final int sy2 = y4;
		final int sz2 = z4;

		// Only for internal use
		final Point3D<?, ?> pts = new InnerComputationPoint3ai();
		final BresenhamLineIterator<InnerComputationPoint3ai, InnerComputationVector3ai> iterator =
				new BresenhamLineIterator<>(
						InnerComputationGeomFactory.SINGLETON, sx1, sy1, sz1, sx2, sy2, sz2);

		while (iterator.hasNext() && c1 != MathConstants.COHEN_SUTHERLAND_INSIDE
				&& c2 != MathConstants.COHEN_SUTHERLAND_INSIDE && (c1 & c2) == 0) {
			if ((c1 & MathConstants.COHEN_SUTHERLAND_TOP) != 0) {
				do {
					iterator.next(pts);
					sy1 = pts.iy();
				}
				while (iterator.hasNext() && sy1 != y2);
                if (sy1 != y2) {
                    return false;
                }
				sx1 = pts.ix();
			} else if ((c1 & MathConstants.COHEN_SUTHERLAND_BOTTOM) != 0) {
				do {
					iterator.next(pts);
					sy1 = pts.iy();
				}
                while (iterator.hasNext() && sy1 != y1);
                if (sy1 != y1) {
                    return false;
                }
				sx1 = pts.ix();
			} else if ((c1 & MathConstants.COHEN_SUTHERLAND_RIGHT) != 0) {
				do {
					iterator.next(pts);
					sx1 = pts.ix();
				}
                while (iterator.hasNext() && sx1 != x2);
                if (sx1 != x2) {
                    return false;
                }
				sy1 = pts.iy();
			} else {
				do {
					iterator.next(pts);
					sx1 = pts.ix();
				}
                while (iterator.hasNext() && sx1 != x1);
                if (sx1 != x1) {
                    return false;
                }
				sy1 = pts.iy();
			}
			c1 = MathUtil.getCohenSutherlandCode(sx1, sy1, x1, y1, x2, y2);
		}

        return c1 == MathConstants.COHEN_SUTHERLAND_INSIDE || c2 == MathConstants.COHEN_SUTHERLAND_INSIDE;
	}

	/** Compute the closest point on the rectangle from the given point.
	 *
	 * @param minx is the x-coordinate of the lowest coordinate of the rectangle.
	 * @param miny is the y-coordinate of the lowest coordinate of the rectangle.
	 * @param minz is the z-coordinate of the lowest coordinate of the rectangle.
	 * @param maxx is the x-coordinate of the highest coordinate of the rectangle.
	 * @param maxy is the y-coordinate of the highest coordinate of the rectangle.
	 * @param maxz is the z-coordinate of the highest coordinate of the rectangle.
	 * @param px is the x-coordinate of the point.
	 * @param py is the y-coordinate of the point.
	 * @param pz is the z-coordinate of the point.
	 * @param result the closest point.
	 */
	@Pure
	@SuppressWarnings({"checkstyle:parameternumber", "checkstyle:magicnumber"})
    static void computeClosestPoint(int minx, int miny, int minz, int maxx, int maxy, int maxz, int px, int py, int pz,
            Point3D<?, ?> result) {
		assert minx <= maxx : AssertMessages.lowerEqualParameters(0, minx, 3, maxx);
		assert miny <= maxy : AssertMessages.lowerEqualParameters(1, miny, 4, maxy);
		assert minz <= maxz : AssertMessages.lowerEqualParameters(2, minz, 5, maxz);
		assert result != null : AssertMessages.notNullParameter(9);

		final int x;
		int same = 0;
		if (px < minx) {
			x = minx;
		} else if (px > maxx) {
			x = maxx;
		} else {
			x = px;
			++same;
		}
		final int y;
		if (py < miny) {
			y = miny;
		} else if (py > maxy) {
			y = maxy;
		} else {
			y = py;
			++same;
		}
		final int z;
		if (pz < minz) {
			z = minz;
		} else if (pz > maxz) {
			z = maxz;
		} else {
			z = pz;
			++same;
		}
		if (same == 3) {
			result.set(px, py, pz);
		} else {
			result.set(x, y, z);
		}
	}

	/** Compute the farthest point on the rectangle from the given point.
	 *
	 * @param minx is the x-coordinate of the lowest coordinate of the rectangle.
	 * @param miny is the y-coordinate of the lowest coordinate of the rectangle.
	 * @param minz is the z-coordinate of the lowest coordinate of the rectangle.
	 * @param maxx is the x-coordinate of the highest coordinate of the rectangle.
	 * @param maxy is the y-coordinate of the highest coordinate of the rectangle.
	 * @param maxz is the z-coordinate of the highest coordinate of the rectangle.
	 * @param px is the x-coordinate of the point.
	 * @param py is the y-coordinate of the point.
	 * @param pz is the z-coordinate of the point.
	 * @param result the farthest point.
	 */
	@Pure
	@SuppressWarnings({"checkstyle:parameternumber", "checkstyle:magicnumber"})
    static void computeFarthestPoint(int minx, int miny, int minz, int maxx, int maxy, int maxz, int px, int py, int pz,
            Point3D<?, ?> result) {
		assert minx <= maxx : AssertMessages.lowerEqualParameters(0, minx, 3, maxx);
		assert miny <= maxy : AssertMessages.lowerEqualParameters(1, miny, 4, maxy);
		assert minz <= maxz : AssertMessages.lowerEqualParameters(2, minz, 5, maxz);
		assert result != null : AssertMessages.notNullParameter(9);

		final int x;
		if (px <= ((minx + maxx) / 2)) {
			x = maxx;
		} else {
			x = minx;
		}
		final int y;
		if (py <= ((miny + maxy) / 2)) {
			y = maxy;
		} else {
			y = miny;
		}
		final int z;
		if (pz <= ((minz + maxz) / 2)) {
			z = maxz;
		} else {
			z = minz;
		}
		result.set(x, y, z);
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
    @SuppressWarnings({ "checkstyle:parameternumber", "checkstyle:npathcomplexity", "checkstyle:magicnumber",
            "checkstyle:cyclomaticcomplexity" })
    static int reduceCohenSutherlandZoneRectangularPrismSegment(int rx1, int ry1, int rz1, int rx2, int ry2, int rz2,
            int sx1, int sy1, int sz1, int sx2, int sy2, int sz2, int codePoint1, int codePoint2,
            Point3D<?, ?> newSegmentP1, Point3D<?, ?> newSegmentP2) {
        assert rx1 <= rx2 : AssertMessages.lowerEqualParameters(0, rx1, 3, rx2);
        assert ry1 <= ry2 : AssertMessages.lowerEqualParameters(1, ry1, 4, ry2);
        assert rz1 <= rz2 : AssertMessages.lowerEqualParameters(2, ry1, 5, ry2);
        assert codePoint1 == MathUtil.getCohenSutherlandCode3D(sx1, sy1, sz1, rx1, ry1, rz1, rx2, ry2, rz2) : AssertMessages
                .invalidValue(8);
        assert codePoint2 == MathUtil.getCohenSutherlandCode3D(sx2, sy2, sz2, rx1, ry1, rz1, rx2, ry2, rz2) : AssertMessages
                .invalidValue(9);
        int segmentX1 = sx1;
        int segmentY1 = sy1;
        int segmentZ1 = sz1;
        int segmentX2 = sx2;
        int segmentY2 = sy2;
        int segmentZ2 = sz2;

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

            int x = 0;
            int y = 0;
            int z = 0;

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
                // point is to the front of clip rectangle
                z = segmentZ1 + (segmentZ2 - segmentZ1) * (rz2 - segmentZ1) / (segmentZ2 - segmentZ1);
                x = rx2;
                y = ry2;
            } else if ((code3 & MathConstants.COHEN_SUTHERLAND_BACK) != 0) {
                // point is to the back of clip rectangle
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

	@Pure
	@Override
	default boolean intersects(RectangularPrism3ai<?, ?, ?, ?, ?, ?> rectangularPrism) {
		assert rectangularPrism != null : AssertMessages.notNullParameter();
		return intersectsRectangleRectangle(
				getMinX(), getMinY(), getMinZ(),
				getMaxX(), getMaxY(), getMaxZ(),
				rectangularPrism.getMinX(), rectangularPrism.getMinY(), rectangularPrism.getMinZ(),
				rectangularPrism.getMaxX(), rectangularPrism.getMaxY(), rectangularPrism.getMaxZ());
	}

	@Pure
	@Override
	default boolean intersects(Sphere3ai<?, ?, ?, ?, ?, ?> sphere) {
		assert sphere != null : AssertMessages.notNullParameter();
		return Sphere3ai.intersectsSphereRectangularPrism(
				sphere.getX(), sphere.getY(), sphere.getZ(),
				sphere.getRadius(),
				getMinX(), getMinY(), getMinZ(),
				getMaxX(), getMaxY(), getMaxZ());
	}

	@Pure
	@Override
	default boolean intersects(Segment3ai<?, ?, ?, ?, ?, ?> segment) {
		assert segment != null : AssertMessages.notNullParameter();
		return intersectsRectangleSegment(
				getMinX(), getMinY(), getMinZ(),
				getMaxX(), getMaxY(), getMaxZ(),
				segment.getX1(), segment.getY1(), segment.getZ1(), segment.getX2(), segment.getY2(), segment.getZ2());
	}

	@Pure
	@Override
	default boolean intersects(PathIterator3ai<?> iterator) {
		assert iterator != null : AssertMessages.notNullParameter();
		final int mask = iterator.getWindingRule() == PathWindingRule.NON_ZERO ? -1 : 2;
		final int crossings = Path3ai.computeCrossingsFromRect(
				0,
				iterator,
				getMinX(), getMinY(), getMinZ(), getMaxX(), getMaxY(), getMaxZ(),
				CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON);
        return crossings == MathConstants.SHAPE_INTERSECTS || (crossings & mask) != 0;

	}

	@Pure
	@Override
	default boolean intersects(MultiShape3ai<?, ?, ?, ?, ?, ?, ?> multishape) {
		assert multishape != null : AssertMessages.notNullParameter();
		return multishape.intersects(this);
	}

	@Pure
	@Override
	default boolean contains(int x, int y, int z) {
		return x >= getMinX() && x <= this.getMaxX() && y >= getMinY() && y <= getMaxY() && z >= getMinZ() && z <= getMaxZ();
	}

	@Pure
	@Override
	default boolean contains(RectangularPrism3ai<?, ?, ?, ?, ?, ?> box) {
		assert box != null : AssertMessages.notNullParameter();
		return box.getMinX() >= getMinX() && box.getMaxX() <= getMaxX()
				&& box.getMinY() >= getMinY() && box.getMaxY() <= getMaxY()
				&& box.getMinZ() >= getMinZ() && box.getMaxZ() <= getMaxZ();
	}

	@Override
	default void set(IT rectangularPrism) {
		assert rectangularPrism != null : AssertMessages.notNullParameter();
        setFromCorners(rectangularPrism.getMinX(), rectangularPrism.getMinY(), rectangularPrism.getMinZ(),
                rectangularPrism.getMaxX(), rectangularPrism.getMaxY(), rectangularPrism.getMaxZ());
	}

	@Pure
	@Override
	default P getClosestPointTo(Point3D<?, ?> pt) {
		assert pt != null : AssertMessages.notNullParameter();
		final P point = getGeomFactory().newPoint();
		computeClosestPoint(getMinX(), getMinY(), getMinZ(), getMaxX(), getMaxY(), getMaxZ(), pt.ix(), pt.iy(), pt.iz(), point);
		return point;
	}

    @Override
    default P getClosestPointTo(RectangularPrism3ai<?, ?, ?, ?, ?, ?> rectangle) {
        throw new UnsupportedOperationException();
    }

    @Override
    default P getClosestPointTo(Sphere3ai<?, ?, ?, ?, ?, ?> circle) {
        throw new UnsupportedOperationException();
    }

    @Override
    default P getClosestPointTo(Segment3ai<?, ?, ?, ?, ?, ?> segment) {
        throw new UnsupportedOperationException();
    }

    @Override
    default P getClosestPointTo(MultiShape3ai<?, ?, ?, ?, ?, ?, ?> multishape) {
        throw new UnsupportedOperationException();
    }

    @Override
    default P getClosestPointTo(Path3ai<?, ?, ?, ?, ?, ?> path) {
        throw new UnsupportedOperationException();
    }

	@Pure
	@Override
	default P getFarthestPointTo(Point3D<?, ?> pt) {
		assert pt != null : AssertMessages.notNullParameter();
		final P point = getGeomFactory().newPoint();
		computeFarthestPoint(getMinX(), getMinY(), getMinZ(), getMaxX(), getMaxY(), getMaxZ(), pt.ix(), pt.iy(), pt.iz(), point);
		return point;
	}

	@Pure
	@Override
	default double getDistanceSquared(Point3D<?, ?> pt) {
		assert pt != null : AssertMessages.notNullParameter();
		final int dx;
        if (pt.ix() < getMinX()) {
			dx = getMinX() - pt.ix();
        } else if (pt.ix() > getMaxX()) {
			dx = pt.ix() - getMaxX();
		} else {
			dx = 0;
		}
		final int dy;
        if (pt.iy() < getMinY()) {
			dy = getMinY() - pt.iy();
        } else if (pt.iy() > getMaxY()) {
			dy = pt.iy() - getMaxY();
		} else {
			dy = 0;
		}
		final int dz;
        if (pt.iz() < getMinZ()) {
			dz = getMinZ() - pt.iz();
        } else if (pt.iz() > getMaxZ()) {
			dz = pt.iz() - getMaxZ();
		} else {
			dz = 0;
		}
        return dx * dx + dy * dy + dz * dz;
	}

	@Pure
	@Override
	default double getDistanceL1(Point3D<?, ?> pt) {
		assert pt != null : AssertMessages.notNullParameter();
		final int dx;
        if (pt.ix() < getMinX()) {
			dx = getMinX() - pt.ix();
        } else if (pt.ix() > getMaxX()) {
			dx = pt.ix() - getMaxX();
		} else {
			dx = 0;
		}
		final int dy;
        if (pt.iy() < getMinY()) {
			dy = getMinY() - pt.iy();
        } else if (pt.iy() > getMaxY()) {
			dy = pt.iy() - getMaxY();
		} else {
			dy = 0;
		}
		final int dz;
        if (pt.iz() < getMinZ()) {
			dz = getMinZ() - pt.iz();
        } else if (pt.iy() > getMaxY()) {
			dz = pt.iz() - getMaxZ();
		} else {
			dz = 0;
		}
		return dx + dy + dz;
	}

	@Pure
	@Override
	default double getDistanceLinf(Point3D<?, ?> pt) {
		assert pt != null : AssertMessages.notNullParameter();
		final int dx;
        if (pt.ix() < getMinX()) {
			dx = getMinX() - pt.ix();
        } else if (pt.ix() > getMaxX()) {
			dx = pt.ix() - getMaxX();
		} else {
			dx = 0;
		}
		final int dy;
        if (pt.iy() < getMinY()) {
			dy = getMinY() - pt.iy();
        } else if (pt.iy() > getMaxY()) {
			dy = pt.iy() - getMaxY();
		} else {
			dy = 0;
		}
		final int dz;
        if (pt.iz() < getMinZ()) {
			dz = getMinZ() - pt.iz();
        } else if (pt.iz() > getMaxZ()) {
			dz = pt.iz() - getMaxZ();
		} else {
			dz = 0;
		}
		return MathUtil.max(dx, dy, dz);
	}

	@Pure
	@Override
	default Iterator<P> getPointIterator() {
		return getPointIterator(Side.TOP);
	}

	/** Replies the points on the bounds of the rectangle.
	 *
	 * @param startingBorder is the first border to reply.
	 * @return the points on the bounds of the rectangle.
	 */
	@Pure
	default Iterator<P> getPointIterator(Side startingBorder) {
		assert startingBorder != null : AssertMessages.notNullParameter();
		return new RectangleSideIterator<>(this, startingBorder);
	}

	@Override
	default PathIterator3ai<IE> getPathIterator(Transform3D transform) {
		if (transform == null || transform.isIdentity()) {
			return new RectanglePathIterator<>(this);
		}
		return new TransformedRectanglePathIterator<>(this, transform);
	}

	/** Compute and replies the union of this rectangular prism and the given prism.
	 * This function does not change this rectangular prism.
	 *
	 *<p>It is equivalent to (where <code>ur</code> is the union):
	 * <pre><code>
	 * RectangularPrism3d ur = new RectangularPrism3d(this);
	 * ur.setUnion(r);
	 * </code></pre>
	 *
	 * @param prism the prism
	 * @return the union of this rectangle and the given rectangle.
	 * @see #setUnion(Prism3ai)
	 */
	@Pure
	default B createUnion(Prism3ai<?, ?, ?, ?, ?, ?> prism) {
		assert prism != null : AssertMessages.notNullParameter();
		final B rr = getGeomFactory().newBox();
		rr.setFromCorners(getMinX(), getMinY(), getMinZ(), getMaxX(), getMaxY(), getMaxZ());
		rr.setUnion(prism);
		return rr;
	}

	/** Compute and replies the intersection of this rectangular prism and the given prism.
     * This function does not change this rectangular prism.
     *
     *<p>It is equivalent to (where <code>ir</code> is the union):
     * <pre><code>
     * RectangularPrism3d ir = new RectangularPrism3d(this);
     * ur.setIntersection(r);
     * </code></pre>
	 *
	 * @param prism the prism
	 * @return the intersection of this rectangle and the given rectangle.
	 * @see #setIntersection(Prism3ai)
	 */
	@Pure
	default B createIntersection(Prism3ai<?, ?, ?, ?, ?, ?> prism) {
		assert prism != null : AssertMessages.notNullParameter();
		final B rr = getGeomFactory().newBox();
		final int x1 = Math.max(getMinX(), prism.getMinX());
		final int y1 = Math.max(getMinY(), prism.getMinY());
		final int z1 = Math.max(getMinZ(), prism.getMinZ());
		final int x2 = Math.min(getMaxX(), prism.getMaxX());
		final int y2 = Math.min(getMaxY(), prism.getMaxY());
		final int z2 = Math.min(getMaxZ(), prism.getMaxZ());
        if (x1 <= x2 && y1 <= y2 && z1 <= z2) {
			rr.setFromCorners(x1, y1, z1, x2, y2, z2);
		} else {
			rr.clear();
		}
		return rr;
	}

	/** Compute the union of this rectangular prism and the given prism.
	 * This function changes this rectangular prism.
	 *
	 * @param prism the prism
	 * @see #createUnion(Prism3ai)
	 */
	default void setUnion(Prism3ai<?, ?, ?, ?, ?, ?> prism) {
		assert prism != null : AssertMessages.notNullParameter();
		setFromCorners(
				Math.min(getMinX(), prism.getMinX()),
				Math.min(getMinY(), prism.getMinY()),
				Math.min(getMinZ(), prism.getMinZ()),
				Math.max(getMaxX(), prism.getMaxX()),
				Math.max(getMaxY(), prism.getMaxY()),
				Math.max(getMaxZ(), prism.getMaxZ()));
	}

	/** Compute the intersection of this rectangular prism and the given prism.
	 * This function changes this rectangular prism.
	 *
	 * <p>If there is no intersection, this rectangular Prism is cleared.
	 *
	 * @param prism the prism
	 * @see #createIntersection(Prism3ai)
	 * @see #clear()
	 */
	default void setIntersection(Prism3ai<?, ?, ?, ?, ?, ?> prism) {
		assert prism != null : AssertMessages.notNullParameter();
		final int x1 = Math.max(getMinX(), prism.getMinX());
		final int y1 = Math.max(getMinY(), prism.getMinY());
		final int z1 = Math.max(getMinZ(), prism.getMinZ());
		final int x2 = Math.min(getMaxX(), prism.getMaxX());
		final int y2 = Math.min(getMaxY(), prism.getMaxY());
		final int z2 = Math.min(getMaxZ(), prism.getMaxZ());
        if (x1 <= x2 && y1 <= y2 && z1 <= z2) {
			setFromCorners(x1, y1, z1, x2, y2, z2);
		} else {
			clear();
		}
	}

	/** Sides of a prism.
	 *
	 * @author $Author: sgalland$
	 * @author $Author: tpiotrow$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 13.0
	 */
	enum Side {
		/** Top.
		 */
		TOP,
		/** Right.
		 */
		RIGHT,
		/** Bottom.
		 */
		BOTTOM,
		/** Left.
		 */
		LEFT,
		/** Front.
		 */
		FRONT,
		/** Back.
		 */
		BACK;
	}

	/** Iterates on points on the sides of a rectangle.
	 *
	 * @param <P> type of the points.
	 * @param <V> type of the vectors.
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 13.0
	 */
	class RectangleSideIterator<P extends Point3D<? super P, ? super V>,
			V extends Vector3D<? super V, ? super P>> implements Iterator<P> {

		private final GeomFactory3ai<?, P, V, ?> factory;

		private final int x0;

		private final int y0;

		private final int z0;

		private final int x1;

		private final int y1;

		private final int z1;

		private final Side firstSide;

		private Side currentSide;

		private int index;

		/**
		 * @param rectangle is the rectangle to iterate.
		 * @param firstSide the first side to iterate on.
		 */
		public RectangleSideIterator(RectangularPrism3ai<?, ?, ?, P, V, ?> rectangle, Side firstSide) {
			assert rectangle != null : AssertMessages.notNullParameter(0);
			assert firstSide != null : AssertMessages.notNullParameter(1);
			this.factory = rectangle.getGeomFactory();
			this.firstSide = firstSide;
			this.x0 = rectangle.getMinX();
			this.y0 = rectangle.getMinY();
			this.z0 = rectangle.getMinZ();
			this.x1 = rectangle.getMaxX();
			this.y1 = rectangle.getMaxY();
			this.z1 = rectangle.getMaxZ();
			this.currentSide = (this.x1 > this.x0 && this.y1 > this.y0 && this.z1 > this.z0) ? this.firstSide : null;
			this.index = 0;
		}

		@Pure
		@Override
		public boolean hasNext() {
            return this.currentSide != null;
		}

		// TODO : integrate z coordinate
		@Override
		@SuppressWarnings({"checkstyle:npathcomplexity", "checkstyle:cyclomaticcomplexity"})
		public P next() {
			int x = 0;
			int y = 0;
			final int z = 0;

            switch (this.currentSide) {
            case TOP:
                x = this.x0 + this.index;
                y = this.y0;
                break;
            case RIGHT:
                x = this.x1;
                y = this.y0 + this.index + 1;
                break;
            case BOTTOM:
                x = this.x1 - this.index - 1;
                y = this.y1;
                break;
            case LEFT:
                x = this.x0;
                y = this.y1 - this.index - 1;
                break;
            case FRONT:
            case BACK:
                break;
            default:
                throw new NoSuchElementException();
            }

            ++this.index;
            Side newSide = null;

            switch (this.currentSide) {
            case TOP:
                if (x >= this.x1) {
                    newSide = Side.RIGHT;
                    this.index = 0;
                }
                break;
            case RIGHT:
                if (y >= this.y1) {
                    newSide = Side.BOTTOM;
                    this.index = 0;
                }
                break;
            case BOTTOM:
                if (x <= this.x0) {
                    newSide = Side.LEFT;
                    this.index = 0;
                }
                break;
            case LEFT:
                if (y <= this.y0 + 1) {
					newSide = Side.TOP;
					this.index = 0;
				}
				break;
			case FRONT:
			case BACK:
			    break;
			default:
				throw new NoSuchElementException();
			}

            if (newSide != null) {
                this.currentSide = (this.firstSide == newSide) ? null : newSide;
			}

			return this.factory.newPoint(x, y, z);
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}

	}

	/** Iterator on the path elements of the rectangle.
	 *
	 * @param <E> the type of the path elements.
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 13.0
	 */
	@SuppressWarnings("checkstyle:magicnumber")
	class RectanglePathIterator<E extends PathElement3ai> implements PathIterator3ai<E> {

		private final RectangularPrism3ai<?, ?, E, ?, ?, ?> rectangle;

		private int x1;

		private int y1;

		private int z1;

		private int x2;

		private int y2;

		private int z2;

		private int index;

		/**
		 * @param rectangle is the rectangle to iterate.
		 */
		public RectanglePathIterator(RectangularPrism3ai<?, ?, E, ?, ?, ?> rectangle) {
			assert rectangle != null : AssertMessages.notNullParameter();
			this.rectangle = rectangle;
			if (rectangle.isEmpty()) {
				this.index = 5;
			} else {
				this.x1 = rectangle.getMinX();
				this.y1 = rectangle.getMinY();
				this.z1 = rectangle.getMinZ();
				this.x2 = rectangle.getMaxX();
				this.y2 = rectangle.getMaxY();
				this.z2 = rectangle.getMaxZ();
			}
		}

		@Override
		public PathIterator3ai<E> restartIterations() {
			return new RectanglePathIterator<>(this.rectangle);
		}

		@Pure
		@Override
		public boolean hasNext() {
			return this.index <= 4;
		}

		@Override
		@SuppressWarnings("checkstyle:returncount")
		public E next() {
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

		@Override
		public boolean isCurved() {
			return false;
		}

		@Override
		public boolean isMultiParts() {
			return false;
		}

		@Override
		public boolean isPolygon() {
			return true;
		}

		@Override
		public GeomFactory3ai<E, ?, ?, ?> getGeomFactory() {
			return this.rectangle.getGeomFactory();
		}

	}

	/** Iterator on the path elements of the rectangle.
	 *
	 * @param <E> the type of the path elements.
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 13.0
	 */
	@SuppressWarnings("checkstyle:magicnumber")
	class TransformedRectanglePathIterator<E extends PathElement3ai> implements PathIterator3ai<E> {

		private final RectangularPrism3ai<?, ?, E, ?, ?, ?> rectangle;

		private final Transform3D transform;

		private int x1;

		private int y1;

		private int z1;

		private int x2;

		private int y2;

		private int z2;

		private int index;

		private Point3D<?, ?> move;

		private Point3D<?, ?> p1;

		private Point3D<?, ?> p2;

		/**
		 * @param rectangle is the rectangle to iterate.
		 * @param transform the transformation to apply on the rectangle.
		 */
		public TransformedRectanglePathIterator(RectangularPrism3ai<?, ?, E, ?, ?, ?> rectangle, Transform3D transform) {
			assert rectangle != null : AssertMessages.notNullParameter(0);
			assert transform != null : AssertMessages.notNullParameter(1);
			this.rectangle = rectangle;
			this.transform = transform;
			if (rectangle.isEmpty()) {
				this.index = 5;
			} else {
				this.move = new InnerComputationPoint3ai();
				this.p1 = new InnerComputationPoint3ai();
				this.p2 = new InnerComputationPoint3ai();
				this.x1 = rectangle.getMinX();
				this.y1 = rectangle.getMinY();
				this.z1 = rectangle.getMinZ();
				this.x2 = rectangle.getMaxX();
				this.y2 = rectangle.getMaxY();
				this.z2 = rectangle.getMaxZ();
			}
		}

		@Override
		public PathIterator3ai<E> restartIterations() {
			return new TransformedRectanglePathIterator<>(this.rectangle, this.transform);
		}

		@Pure
		@Override
		public boolean hasNext() {
			return this.index <= 4;
		}

		@Override
		@SuppressWarnings({"checkstyle:returncount", "checkstyle:cyclomaticcomplexity", "checkstyle:npathcomplexity"})
		public E next() {
            final int idx = this.index;
            ++this.index;
            switch (idx) {
            case 0:
                this.p2.set(this.x1, this.y1, this.z1);
                if (this.transform != null) {
                    this.transform.transform(this.p2);
                }
                this.move.set(this.p2);
                if (this.transform != null) {
                    this.transform.transform(this.p2);
                }
                return this.rectangle.getGeomFactory().newMovePathElement(this.p2.ix(), this.p2.iy(), this.p2.iz());
            case 1:
                this.p1.set(this.p2);
                this.p2.set(this.x1, this.y1, this.z2);
                if (this.transform != null) {
                    this.transform.transform(this.p2);
                }
                return this.rectangle.getGeomFactory().newLinePathElement(this.p1.ix(), this.p1.iy(), this.p1.iz(), this.p2.ix(),
                        this.p2.iy(), this.p2.iz());
            case 2:
                this.p1.set(this.p2);
                this.p2.set(this.x1, this.y2, this.z2);
                if (this.transform != null) {
                    this.transform.transform(this.p2);
                }
                return this.rectangle.getGeomFactory().newLinePathElement(this.p1.ix(), this.p1.iy(), this.p1.iz(), this.p2.ix(),
                        this.p2.iy(), this.p2.iz());
            case 3:
                this.p1.set(this.p2);
                if (this.transform != null) {
                    this.transform.transform(this.p2);
                }
                this.transform.transform(this.p2);
                return this.rectangle.getGeomFactory().newLinePathElement(this.p1.ix(), this.p1.iy(), this.p1.iz(), this.p2.ix(),
                        this.p2.iy(), this.p2.iz());
            case 4:
                this.p1.set(this.p2);
                if (this.transform != null) {
                    this.transform.transform(this.p2);
                }
                this.transform.transform(this.p2);
                return this.rectangle.getGeomFactory().newLinePathElement(this.p1.ix(), this.p1.iy(), this.p1.iz(), this.p2.ix(),
                        this.p2.iy(), this.p2.iz());
            case 5:
                this.p1.set(this.p2);
                this.p2.set(this.x2, this.y2, this.z2);
                if (this.transform != null) {
                    this.transform.transform(this.p2);
                }
                return this.rectangle.getGeomFactory().newLinePathElement(this.p1.ix(), this.p1.iy(), this.p1.iz(), this.p2.ix(),
                        this.p2.iy(), this.p2.iz());
            case 6:
                this.p1.set(this.p2);
                this.p2.set(this.x2, this.y1, this.z2);
                if (this.transform != null) {
                    this.transform.transform(this.p2);
                }
                return this.rectangle.getGeomFactory().newLinePathElement(this.p1.ix(), this.p1.iy(), this.p1.iz(), this.p2.ix(),
                        this.p2.iy(), this.p2.iz());
            case 7:
                return this.rectangle.getGeomFactory().newClosePathElement(this.p2.ix(), this.p2.iy(), this.p2.iz(),
                        this.move.ix(), this.move.iy(), this.move.iz());
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

		@Override
		public boolean isCurved() {
			return false;
		}

		@Override
		public boolean isMultiParts() {
			return false;
		}

		@Override
		public boolean isPolygon() {
			return true;
		}

		@Override
		public GeomFactory3ai<E, ?, ?, ?> getGeomFactory() {
			return this.rectangle.getGeomFactory();
		}

	}

}
