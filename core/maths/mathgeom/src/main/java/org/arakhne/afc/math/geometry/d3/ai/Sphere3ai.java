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

package org.arakhne.afc.math.geometry.d3.ai;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.TreeSet;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.geometry.CrossingComputationType;
import org.arakhne.afc.math.geometry.GeomConstants;
import org.arakhne.afc.math.geometry.PathWindingRule;
import org.arakhne.afc.math.geometry.d3.GeomFactory3D;
import org.arakhne.afc.math.geometry.d3.Point3D;
import org.arakhne.afc.math.geometry.d3.Transform3D;
import org.arakhne.afc.math.geometry.d3.Tuple3iComparator;
import org.arakhne.afc.math.geometry.d3.Vector3D;
import org.arakhne.afc.vmutil.asserts.AssertMessages;

/** Fonctional interface that represented a 3D sphere.
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
public interface Sphere3ai<
		ST extends Shape3ai<?, ?, IE, P, V, B>,
		IT extends Sphere3ai<?, ?, IE, P, V, B>,
		IE extends PathElement3ai,
		P extends Point3D<? super P, ? super V>,
		V extends Vector3D<? super V, ? super P>,
		B extends RectangularPrism3ai<?, ?, IE, P, V, B>>
		extends Shape3ai<ST, IT, IE, P, V, B> {

	/** Replies if the given point is inside the sphere.
	 *
	 * @param cx is the x-coordinate of the sphere center
	 * @param cy is the y-coordinate of the sphere center
	 * @param cz is the z-coordinate of the sphere center
	 * @param cr is the radius of the sphere center
	 * @param x is the x-coordinate of the point
	 * @param y is the y-coordinate of the point
	 * @param z is the z-coordinate of the point
	 * @return <code>true</code> if the point is inside the sphere.
	 */
	@Pure
	@SuppressWarnings("checkstyle:magicnumber")
	static boolean contains(int cx, int cy, int cz, int cr, int x, int y, int z) {
		assert cr >= 0 : AssertMessages.positiveOrZeroParameter(2);

		final int vx = x - cx;
		final int vy = y - cy;
		final int vz = z - cz;

        if (vx >= -cr && vx <= cr && vy >= -cr && vy <= cr && vz >= -cr && vz <= cr) {
			final int octant;
            final boolean xpos = vx >= 0;
            final boolean ypos = vy >= 0;
            //TODO final boolean zpos = vz >= 0;
            if (xpos) {
                if (ypos) {
                    octant = 0;
                } else {
                    octant = 2;
                }
            } else {
                if (ypos) {
                    octant = 6;
                } else {
                    octant = 4;
                }
            }

			boolean allNull = true;
            final SpherePerimeterIterator<InnerComputationPoint3ai, InnerComputationVector3ai> iterator =
                    new SpherePerimeterIterator<>(
                    InnerComputationGeomFactory.SINGLETON, cx, cy, cz, cr, octant, octant + 2, false);

			while (iterator.hasNext()) {
			    final Point3D<?, ?> pt = iterator.next();
			    // Trivial case
			    if (pt.ix() == x && pt.iy() == y) {
			        return true;
                }

				final int px = cy - pt.iy();
				final int py = pt.ix() - cx;
				final int cpx = x - pt.ix();
				final int cpy = y - pt.iy();
				final int ccw = cpx * py - cpy * px;

                if (ccw > 0) {
                    return false;
                }
                if (ccw < 0) {
                    allNull = false;
                }
			}

			return !allNull;
		}

		return false;
	}

	/** Replies if the given point is inside the quadrant of the given sphere.
	 *
	 * @param cx is the x-coordinate of the sphere center
	 * @param cy is the y-coordinate of the sphere center
	 * @param cz is the z-coordinate of the sphere center
	 * @param cr is the radius of the sphere center
	 * @param quadrant is the quadrant: <table summary="">
	 * <thead>
	 * <tr><th>quadrant</th><th>x</th><th>y</th></tr>
	 * </thead>
	 * <tbody>
	 * <tr><td>0</td><td>&ge;cx</td><td>&ge;cy</td></tr>
	 * <tr><td>1</td><td>&ge;cx</td><td>&lt;cy</td></tr>
	 * <tr><td>2</td><td>&lt;cx</td><td>&ge;cy</td></tr>
	 * <tr><td>3</td><td>&lt;cx</td><td>&lt;cy</td></tr>
	 * </tbody>
	 * </table>
	 * @param x is the x-coordinate of the point
	 * @param y is the y-coordinate of the point
	 * @param z is the z-coordinate of the point
	 * @return <code>true</code> if the point is inside the sphere.
	 */
	@Pure
	@SuppressWarnings("checkstyle:magicnumber")
	static boolean contains(int cx, int cy, int cz, int cr, int quadrant, int x, int y, int z) {
		assert cr >= 0 : AssertMessages.positiveOrZeroParameter(3);
		assert quadrant >= 0 && quadrant <= 3 : AssertMessages.outsideRangeInclusiveParameter(quadrant, 0, 3);

		final int vx = x - cx;
		final int vy = y - cy;

        if (vx >= -cr && vx <= cr && vy >= -cr && vy <= cr) {
            final int octant;
            final boolean xpos = vx >= 0;
            final boolean ypos = vy >= 0;
            if (xpos) {
                if (ypos) {
                    octant = 0;
                } else {
                    octant = 2;
                }
            } else {
                if (ypos) {
                    octant = 6;
                } else {
                    octant = 4;
                }
            }

            if (quadrant * 2 != octant) {
                return false;
            }

			final SpherePerimeterIterator<InnerComputationPoint3ai, InnerComputationVector3ai> iterator =
					new SpherePerimeterIterator<>(InnerComputationGeomFactory.SINGLETON,
					        cx, cy, cz, cr, octant, octant + 2, false);

			while (iterator.hasNext()) {
				final Point3D<?, ?> p = iterator.next();
				final int px = cy - p.iy();
				final int py = p.ix() - cx;
				final int cpx = x - p.ix();
				final int cpy = y - p.iy();
				final int ccw = cpx * py - cpy * px;

                if (ccw > 0) {
                    return false;
                }
			}

			return true;
		}

		return false;
	}

	@Pure
	@Override
	default boolean contains(int x, int y, int z) {
	    return contains(getX(), getY(), getZ(), getRadius(), x, y, z);
	}

    // TODO : integrate z coordinate
    @Pure
    @Override
    @SuppressWarnings({"checkstyle:booleanexpressioncomplexity", "checkstyle:magicnumber", "checkstyle:cyclomaticcomplexity"})
    default boolean contains(RectangularPrism3ai<?, ?, ?, ?, ?, ?> box) {
        assert box != null : AssertMessages.notNullParameter();
        final int cx = getX();
        final int cy = getY();
        final int cz = getZ();
        final int radius = getRadius();
        final int vx1 = box.getMinX() - cx;
        final int vy1 = box.getMinY() - cy;
        //TODO final int vz1 = box.getMinZ() - cz;
        final int vx2 = box.getMaxX() - cx;
        final int vy2 = box.getMaxY() - cy;
        //TODO final int vz2 = box.getMaxZ() - cz;

        if (vx1 >= -radius && vx1 <= radius && vy1 >= -radius && vy1 <= radius && vx2 >= -radius && vx2 <= radius
                && vy2 >= -radius && vy2 <= radius) {
            final int[] quadrants = new int[4];
            final int[] x = new int[] {vx1, vx2, vx2, vx1};
            final int[] y = new int[] {vy1, vy1, vy2, vy2};
            for (int i = 0; i < 4; ++i) {
                final int xcoord = x[i];
                final int ycoord = y[i];
                final int flag = 1 << i;
                if (xcoord > 0) {
                    if (ycoord > 0) {
                        quadrants[0] |= flag;
                    } else {
                        quadrants[1] |= flag;
                    }
                } else {
                    if (ycoord > 0) {
                        quadrants[3] |= flag;
                    } else {
                        quadrants[2] |= flag;
                    }
                }
            }

            for (int i = 0; i < quadrants.length; ++i) {
                if (quadrants[i] != 0) {
                    final SpherePerimeterIterator<P, V> iterator = new SpherePerimeterIterator<>(
                            getGeomFactory(), cx, cy, cz, radius, i * 2, i * 2 + 2, false);
                    while (iterator.hasNext()) {
                        final P p = iterator.next();
                        final int px = cy - p.iy();
                        final int py = p.ix() - cx;

                        for (int j = 0; j < 4; ++j) {
                            if ((quadrants[i] & (1 << j)) != 0) {
                                final int cpx = x[j] - p.ix();
                                final int cpy = y[j] - p.iy();
                                final int ccw = cpx * py - cpy * px;
                                if (ccw > 0) {
                                    return false;
                                }
                            }
                        }
                    }
                }
            }

            return true;
        }

        return false;
    }

	/** Replies the closest point in a sphere to a point.
	 *
	 * <p>The closest point is the point on the perimeter or inside the sphere's disk that
	 * has the lowest Manhatan distance to the given origin point.
	 *
	 * @param cx is the center of the sphere
	 * @param cy is the center of the sphere
	 * @param cz is the center of the sphere
	 * @param cr is the radius of the sphere
	 * @param x is the point
	 * @param y is the point
	 * @param z is the point
	 * @param result the closest point in the sphere to the point.
	 */
	@Pure
	@SuppressWarnings("checkstyle:magicnumber")
	static void computeClosestPointTo(int cx, int cy, int cz, int cr, int x, int y, int z, Point3D<?, ?> result) {
		assert cr >= 0 : AssertMessages.positiveOrZeroParameter(3);
		assert result != null : AssertMessages.notNullParameter(7);

		final int vx = x - cx;
		final int vy = y - cy;

		final int octant;
        final boolean xpos = vx >= 0;
        final boolean ypos = vy >= 0;
        if (xpos) {
            if (ypos) {
                octant = 0;
            } else {
                octant = 2;
            }
        } else {
            if (ypos) {
                octant = 6;
            } else {
                octant = 4;
            }
        }

		final SpherePerimeterIterator<InnerComputationPoint3ai, InnerComputationVector3ai> iterator =
                new SpherePerimeterIterator<>(InnerComputationGeomFactory.SINGLETON, cx, cy, cz, cr, octant, octant + 2, false);

		boolean isInside = true;
		int minDist = Integer.MAX_VALUE;

		while (iterator.hasNext()) {
			final Point3D<?, ?> p = iterator.next();
			final int px = cy - p.iy();
			final int py = p.ix() - cx;
			final int cpx = x - p.ix();
			final int cpy = y - p.iy();
			final int ccw = cpx * py - cpy * px;
			if (ccw >= 0) {
				isInside = false;
				// Mahantan distance
				final int dist = Math.abs(cpx) + Math.abs(cpy);
				if (dist < minDist) {
					minDist = dist;
					result.set(p);
				}
			}
		}

		// inside the sphere
		if (isInside) {
			result.set(x, y, z);
		}
	}

	/** Replies the farthest point in a sphere to a point.
	 *
	 * <p>The farthest point is the point on the perimeter of the sphere that has the highest Manhatan distance
	 * to the given origin point.
	 *
	 * @param cx is the center of the sphere
	 * @param cy is the center of the sphere
	 * @param cz is the center of the sphere
	 * @param cr is the radius of the sphere
	 * @param x is the point
	 * @param y is the point
	 * @param z is the point
	 * @param result the farthest point in the sphere to the point.
	 */
	@Pure
	@SuppressWarnings("checkstyle:magicnumber")
	static void computeFarthestPointTo(int cx, int cy, int cz, int cr, int x, int y, int z, Point3D<?, ?> result) {
		assert cr >= 0 : AssertMessages.positiveOrZeroParameter(3);

		final int vx = x - cx;
		final int vy = y - cy;

		final int octant;
        final boolean xpos = vx >= 0;
        final boolean ypos = vy >= 0;
        if (xpos) {
            if (ypos) {
                octant = 4;
            } else {
                octant = 6;
            }
        } else {
            if (ypos) {
                octant = 2;
            } else {
                octant = 0;
            }
        }

		final SpherePerimeterIterator<InnerComputationPoint3ai, InnerComputationVector3ai> iterator =
                new SpherePerimeterIterator<>(InnerComputationGeomFactory.SINGLETON, cx, cy, cz, cr, octant, octant + 2, false);

		int maxL1Dist = Integer.MIN_VALUE;
		int maxLinfDist = Integer.MIN_VALUE;
		result.set(x, y, z);

		while (iterator.hasNext()) {
		    final Point3D<?, ?>  p = iterator.next();
			final int cpx = Math.abs(p.ix() - x);
			final int cpy = Math.abs(p.iy() - y);
			// Mahantan distance
			final int l1 = cpx + cpy;
			final int linfinv = Math.min(cpx, cpy);
			if (l1 > maxL1Dist || (l1 == maxL1Dist && linfinv < maxLinfDist)) {
				maxL1Dist = l1;
				maxLinfDist = linfinv;
				result.set(p);
			}
		}
	}

	/** Replies if two spheres are intersecting.
	 *
	 * @param x1 is the center of the first sphere
	 * @param y1 is the center of the first sphere
	 * @param z1 is the center of the first sphere
	 * @param radius1 is the radius of the first sphere
	 * @param x2 is the center of the second sphere
	 * @param y2 is the center of the second sphere
	 * @param z2 is the center of the second sphere
	 * @param radius2 is the radius of the second sphere
	 * @return <code>true</code> if the two shapes are intersecting; otherwise
	 * <code>false</code>
	 */
	@Pure
	@SuppressWarnings("checkstyle:magicnumber")
	static boolean intersectsSphereSphere(int x1, int y1, int z1, int radius1, int x2, int y2, int z2, int radius2) {
		assert radius1 >= 0 : AssertMessages.positiveOrZeroParameter(3);
		assert radius2 >= 0 : AssertMessages.positiveOrZeroParameter(7);
		final Point3D<?, ?> point = new InnerComputationPoint3ai();
		computeClosestPointTo(x1, y1, z1, radius1, x2, y2, z2, point);
		return contains(x2, y2, z2, radius2, point.ix(), point.iy(), point.iz());
	}

	/** Replies if a sphere and a rectangle are intersecting.
	 *
	 * @param x1 is the center of the sphere
	 * @param y1 is the center of the sphere
	 * @param z1 is the center of the sphere
	 * @param radius is the radius of the sphere
	 * @param x2 is the first corner of the rectangle.
	 * @param y2 is the first corner of the rectangle.
	 * @param z2 is the first corner of the rectangle.
	 * @param x3 is the second corner of the rectangle.
	 * @param y3 is the second corner of the rectangle.
	 * @param z3 is the second corner of the rectangle.
	 * @return <code>true</code> if the two shapes are intersecting; otherwise
	 * <code>false</code>
	 */
	@Pure
	@SuppressWarnings("checkstyle:parameternumber")
    static boolean intersectsSphereRectangularPrism(int x1, int y1, int z1, int radius, int x2, int y2, int z2, int x3, int y3,
            int z3) {
		assert radius >= 0 : AssertMessages.positiveOrZeroParameter(3);
		final Point3D<?, ?> point = new InnerComputationPoint3ai();
		RectangularPrism3ai.computeClosestPoint(x2, y2, z2, x3, y3, z3, x1, y1, z1, point);
		return contains(x1, y1, z1, radius, point.ix(), point.iy(), point.iz());
	}

	/** Replies if a sphere and a segment are intersecting.
	 *
	 * @param x1 is the center of the sphere
	 * @param y1 is the center of the sphere
	 * @param z1 is the center of the sphere
	 * @param radius is the radius of the sphere
	 * @param x2 is the first point of the segment.
	 * @param y2 is the first point of the segment.
	 * @param z2 is the first point of the segment.
	 * @param x3 is the second point of the segment.
	 * @param y3 is the second point of the segment.
	 * @param z3 is the second point of the segment.
	 * @return <code>true</code> if the two shapes are intersecting; otherwise
	 * <code>false</code>
	 */
	@SuppressWarnings("checkstyle:parameternumber")
	static boolean intersectsSphereSegment(int x1, int y1, int z1, int radius, int x2, int y2, int z2, int x3, int y3, int z3) {
		assert radius >= 0 : AssertMessages.positiveOrZeroParameter(3);
		final Point3D<?, ?> point = new InnerComputationPoint3ai();
		Segment3ai.computeClosestPointToPoint(x2, y2, z1, x3, y3, z3, x1, y1, z1, point);
		return contains(x1, y1, z1, radius, point.ix(), point.iy(), point.iz());
	}

	/** Replies the points of the sphere perimeters starting by the first octant.
	 *
	 * @param <P> the type of the points.
	 * @param <V> the type of the vectors.
	 * @param cx is the center of the radius.
	 * @param cy is the center of the radius.
	 * @param cz is the center of the radius.
	 * @param radius is the radius of the radius.
	 * @param firstOctantIndex is the index of the first octant to treat (value in [0;7].
	 * @param nbOctants is the number of octants to traverse (value in [0; 7 - firstOctantIndex].
	 * @param factory the factory to use for creating the points.
	 * @return the points on the perimeters.
	 */
	@Pure
	@SuppressWarnings("checkstyle:magicnumber")
	static <P extends Point3D<? super P, ? super V>, V extends Vector3D<? super V, ? super P>>
	  Iterator<P> getPointIterator(int cx, int cy, int cz, int radius, int firstOctantIndex, int nbOctants,
			GeomFactory3ai<?, P, V, ?> factory) {
		assert radius >= 0 : AssertMessages.positiveOrZeroParameter(3);
		assert firstOctantIndex >= 0 && firstOctantIndex < 8
				: AssertMessages.outsideRangeInclusiveParameter(firstOctantIndex, 0, 7);
		int maxOctant;
        maxOctant = Math.min(8, firstOctantIndex + nbOctants);
		if (maxOctant > 8) {
			maxOctant = 8;
		}
		return new SpherePerimeterIterator<>(
				factory,
				cx, cy, cz, radius,
				firstOctantIndex, maxOctant, true);
	}

    /** Replies the points of the sphere perimeters starting by the first octant.
     *
     * @return the points on the perimeters.
     */
    @Pure
    @Override
    @SuppressWarnings("checkstyle:magicnumber")
    default Iterator<P> getPointIterator() {
        return new SpherePerimeterIterator<>(getGeomFactory(), getX(), getY(), getZ(), getRadius(), 0, 8, true);
    }

    /** Replies the points of the sphere perimeters starting by the first octant.
     *
     * @param firstOctantIndex is the index of the first octant (see figure) to treat.
     * @param nbOctants is the number of octants to traverse (greater than zero).
     * @return the points on the perimeters.
     */
    @Pure
    default Iterator<P> getPointIterator(int firstOctantIndex, int nbOctants) {
        return getPointIterator(getX(), getY(), getZ(), getRadius(), firstOctantIndex, nbOctants, getGeomFactory());
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
		return getX() == shape.getX()
			&& getY() == shape.getY()
			&& getZ() == shape.getZ()
			&& getRadius() == shape.getRadius();
	}

	@Override
	default void clear() {
		set(0, 0, 0, 0);
	}

	@Pure
	@Override
	default boolean isEmpty() {
		return getRadius() <= 0;
	}

	@Override
	default void set(IT shape) {
		set(shape.getX(), shape.getY(), shape.getZ(), shape.getRadius());
	}

	/** Change the sphere.
	 *
	 * @param center the center of the sphere.
	 * @param radius the radius of the sphere.
	 */
	default void set(Point3D<?, ?> center, int radius) {
		set(center.ix(), center.iy(), center.iz(), Math.abs(radius));
	}

	/** Change the sphere.
	 *
	 * @param x the x coordinate of the center.
	 * @param y the y coordinate of the center.
	 * @param z the z coordinate of the center.
	 * @param radius the radius of the center.
	 */
	void set(int x, int y, int z, int radius);

	/** Change the sphere's center.
	 *
	 * @param center the center of the sphere.
	 */
	default void setCenter(Point3D<?, ?> center) {
		set(center.ix(), center.iy(), center.iz(), getRadius());
	}

	/** Change the sphere's center.
	 *
	 * @param x x coordinate of the center of the sphere.
	 * @param y y coordinate of the center of the sphere.
	 * @param z z coordinate of the center of the sphere.
	 */
	default void setCenter(int x, int y, int z) {
		set(x, y, z, getRadius());
	}

	/** Replies the center X.
	 *
	 * @return the center x.
	 */
	@Pure
	int getX();

	/** Change the center X.
	 *
	 * @param x the center x.
	 */
	@Pure
	void setX(int x);

	/** Replies the center y.
	 *
	 * @return the center y.
	 */
	@Pure
	int getY();

	/** Change the center Y.
	 *
	 * @param y the center y.
	 */
	@Pure
	void setY(int y);

	/** Replies the center z.
	 *
	 * @return the center z.
	 */
	@Pure
	int getZ();

	/** Change the center Z.
	 *
	 * @param z the center z.
	 */
	@Pure
	void setZ(int z);

	/** Replies the center.
	 *
	 * @return a copy of the center.
	 */
	@Pure
	default P getCenter() {
		return getGeomFactory().newPoint(getX(), getY(), getZ());
	}

	/** Replies the radius.
	 *
	 * @return the radius.
	 */
	@Pure
	int getRadius();

	/** Change the radius.
	 *
	 * @param radius the radius.
	 */
	@Pure
	void setRadius(int radius);

	@Pure
	@Override
	default void toBoundingBox(B box) {
		assert box != null : AssertMessages.notNullParameter();
		final int centerX = getX();
		final int centerY = getY();
		final int centerZ = getZ();
		final int radius = getRadius();
		box.setFromCorners(
				centerX - radius,
				centerY - radius,
				centerZ - radius,
				centerX + radius,
				centerY + radius,
				centerZ + radius);
	}

	@Pure
	@Override
	default double getDistanceSquared(Point3D<?, ?> pt) {
		assert pt != null : AssertMessages.notNullParameter();
		final P c = getClosestPointTo(pt);
		return c.getDistanceSquared(pt);
	}

	@Pure
	@Override
	default double getDistanceL1(Point3D<?, ?> pt) {
		assert pt != null : AssertMessages.notNullParameter();
		final P c = getClosestPointTo(pt);
		return c.getDistanceL1(pt);
	}

	@Pure
	@Override
	default double getDistanceLinf(Point3D<?, ?> pt) {
		final P c = getClosestPointTo(pt);
		return c.getDistanceLinf(pt);
	}

	@Pure
	@Override
	default P getClosestPointTo(Point3D<?, ?> pt) {
		assert pt != null : AssertMessages.notNullParameter();
		final P point = getGeomFactory().newPoint();
		computeClosestPointTo(getX(), getY(), getZ(), getRadius(), pt.ix(), pt.iy(), pt.iz(), point);
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
		computeFarthestPointTo(getX(), getY(), getZ(), getRadius(), pt.ix(), pt.iy(), pt.iz(), point);
		return point;
	}

	@Pure
	@Override
	default boolean intersects(RectangularPrism3ai<?, ?, ?, ?, ?, ?> rectangularPrism) {
		assert rectangularPrism != null : AssertMessages.notNullParameter();
		return intersectsSphereRectangularPrism(
				getX(), getY(), getZ(), getRadius(),
				rectangularPrism.getMinX(), rectangularPrism.getMinY(), rectangularPrism.getMinZ(),
				rectangularPrism.getMaxX(), rectangularPrism.getMaxY(), rectangularPrism.getMaxZ());
	}

	@Pure
	@Override
	default boolean intersects(Sphere3ai<?, ?, ?, ?, ?, ?> sphere) {
		assert sphere != null : AssertMessages.notNullParameter();
		return intersectsSphereSphere(
				getX(), getY(), getZ(), getRadius(),
				sphere.getX(), sphere.getY(), sphere.getZ(), sphere.getRadius());
	}

	@Pure
	@Override
	default boolean intersects(Segment3ai<?, ?, ?, ?, ?, ?> segment) {
		assert segment != null : AssertMessages.notNullParameter();
		return intersectsSphereSegment(
				getX(), getY(), getZ(), getRadius(),
				segment.getX1(), segment.getY1(), segment.getZ1(),
				segment.getX2(), segment.getY2(), segment.getZ2());
	}

	@Pure
	@Override
	default boolean intersects(PathIterator3ai<?> iterator) {
		assert iterator != null : AssertMessages.notNullParameter();
		final int mask = iterator.getWindingRule() == PathWindingRule.NON_ZERO ? -1 : 2;
		final int crossings = Path3ai.computeCrossingsFromSphere(
				0,
				iterator,
				getX(), getY(), getZ(), getRadius(),
				CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON);
        return crossings == GeomConstants.SHAPE_INTERSECTS || (crossings & mask) != 0;
	}

	@Pure
	@Override
	default boolean intersects(MultiShape3ai<?, ?, ?, ?, ?, ?, ?> multishape) {
		assert multishape != null : AssertMessages.notNullParameter();
		return multishape.intersects(this);
	}

	@Override
	default void translate(int dx, int dy, int dz) {
		setCenter(getX() + dx, getY() + dy, getZ() + dz);
	}

	@Pure
	@Override
	default PathIterator3ai<IE> getPathIterator(Transform3D transform) {
		if (transform == null || transform.isIdentity()) {
			return new SpherePathIterator<>(this);
		}
		return new TransformedCirclePathIterator<>(this, transform);
	}

	/** Abstract iterator on the path elements of the sphere.
	 *
	 * @param <IE> is the type of the path elements.
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 13.0
	 */
	// TODO integrate z coordinate
	@SuppressWarnings("checkstyle:magicnumber")
	abstract class AbstractCirclePathIterator<IE extends PathElement3ai> implements PathIterator3ai<IE> {

		/**
		 * ArcIterator.btan(Math.PI/2).
		 */
		protected static final double CTRL_VAL = 0.5522847498307933f;

		/**
		 * ctrlpts contains the control points for a set of 4 cubic
		 * bezier curves that approximate a sphere of radius 0.5
		 * centered at 0.5, 0.5.
		 */
		protected static final double PCV = 0.5f + CTRL_VAL * 0.5f;

		/**
		 * ctrlpts contains the control points for a set of 4 cubic
		 * bezier curves that approximate a sphere of radius 0.5
		 * centered at 0.5, 0.5.
		 */
		protected static final double NCV = 0.5f - CTRL_VAL * 0.5f;

		/**
		 * ctrlpts contains the control points for a set of 4 cubic
		 * bezier curves that approximate a sphere of radius 0.5
		 * centered at 0.5, 0.5.
		 */
		protected static final double[][] CTRL_PTS = {
			{1.0, PCV, PCV, 1.0, 0.5, 1.0},
			{NCV, 1.0, 0.0, PCV, 0.0, 0.5},
			{0.0, NCV, NCV, 0.0, 0.5, 0.0},
			{PCV, 0.0, 1.0, NCV, 1.0, 0.5},
		};

		/**
		 * The element factory.
		 */
		protected final Sphere3ai<?, ?, IE, ?, ?, ?> sphere;

		/** Constructor.
		 * @param sphere the sphere.
		 */
		public AbstractCirclePathIterator(Sphere3ai<?, ?, IE, ?, ?, ?> sphere) {
			assert sphere != null : AssertMessages.notNullParameter();
			this.sphere = sphere;
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
		public GeomFactory3ai<IE, ?, ?, ?> getGeomFactory() {
			return this.sphere.getGeomFactory();
		}

		@Override
		public boolean isCurved() {
			return true;
		}

		@Override
		public boolean isMultiParts() {
			return false;
		}

		@Override
		public boolean isPolygon() {
			return true;
		}

	}

	/** Iterator on the path elements of the sphere.
	 *
	 * @param <IE> is the type of the path elements.
	 * @author $Author: sgalland$
	 * @author $Author: tpiotrow$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 13.0
	 */
	@SuppressWarnings("checkstyle:magicnumber")
	class SpherePathIterator<IE extends PathElement3ai> extends AbstractCirclePathIterator<IE> {

		private int x;

		private int y;

		private int z;

		private int radius;

		private int index;

		private int movex;

		private int movey;

		private int movez;

		private int lastx;

		private int lasty;

		private int lastz;

		/** Constructor.
		 * @param sphere the sphere to iterate on.
		 */
		public SpherePathIterator(Sphere3ai<?, ?, IE, ?, ?, ?> sphere) {
			super(sphere);
			if (sphere.isEmpty()) {
				this.index = 6;
			} else {
				this.radius = sphere.getRadius();
				this.x = sphere.getX() - this.radius;
				this.y = sphere.getY() - this.radius;
				this.z = sphere.getZ() - this.radius;
			}
		}

		@Override
		public PathIterator3ai<IE> restartIterations() {
			return new SpherePathIterator<>(this.sphere);
		}

		@Pure
		@Override
		public boolean hasNext() {
			return this.index <= 5;
		}

		// TODO : check indexes
		@Override
		public IE next() {
			if (this.index > 5) {
				throw new NoSuchElementException();
			}
            final int idx = this.index;
			++this.index;
            if (idx == 0) {
                final int dr = 2 * this.radius;
				final double[] ctrls = CTRL_PTS[3];
                this.movex = (int) (this.x + ctrls[6] * dr);
                this.movey = (int) (this.y + ctrls[7] * dr);
                this.movez = (int) (this.z + ctrls[8] * dr);
                this.lastx = this.movex;
				this.lasty = this.movey;
				this.lastz = this.movez;
				return getGeomFactory().newMovePathElement(
                        this.lastx, this.lasty, this.lastz);
            } else if (idx < 5) {
                final int dr = 2 * this.radius;
                final double[] ctrls = CTRL_PTS[idx - 1];
                final int ppx = this.lastx;
                final int ppy = this.lasty;
                final int ppz = this.lastz;
                this.lastx = (int) (this.x + ctrls[6] * dr);
                this.lasty = (int) (this.y + ctrls[7] * dr);
                this.lastz = (int) (this.z + ctrls[8] * dr);
                return getGeomFactory().newCurvePathElement(ppx, ppy, ppz, (int) (this.x + ctrls[0] * dr),
                        (int) (this.y + ctrls[1] * dr), (int) (this.z + ctrls[2] * dr), (int) (this.x + ctrls[3] * dr),
                        (int) (this.y + ctrls[4] * dr), (int) (this.z + ctrls[5] * dr), this.lastx, this.lasty, this.lastz);
            }
            final int ppx = this.lastx;
            final int ppy = this.lasty;
            final int ppz = this.lastz;
			this.lastx = this.movex;
			this.lasty = this.movey;
			this.lastz = this.movez;
			return getGeomFactory().newClosePathElement(
					ppx, ppy, ppz,
					this.lastx, this.lasty, this.lastz);
		}

	}

	/** Iterator on the path elements of the sphere.
	 *
	 * @param <IE> is the type of the path elements.
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 13.0
	 */
	@SuppressWarnings("checkstyle:magicnumber")
	class TransformedCirclePathIterator<IE extends PathElement3ai> extends AbstractCirclePathIterator<IE> {

		private final Transform3D transform;

		private int x;

		private int y;

		private int z;

		private int radius;

		private int index;

		private int movex;

		private int movey;

		private int movez;

		private Point3D<?, ?> p1;

		private Point3D<?, ?> p2;

		private Point3D<?, ?> ptmp1;

		private Point3D<?, ?> ptmp2;

		/** Constructor.
		 * @param sphere the sphere to iterate on.
		 * @param transform the transformation to apply.
		 */
		public TransformedCirclePathIterator(Sphere3ai<?, ?, IE, ?, ?, ?> sphere, Transform3D transform) {
			super(sphere);
			assert transform != null : AssertMessages.notNullParameter(1);
			this.transform = transform;
			if (sphere.isEmpty()) {
				this.index = 6;
			} else {
				this.p1 = new InnerComputationPoint3ai();
				this.p2 = new InnerComputationPoint3ai();
				this.ptmp1 = new InnerComputationPoint3ai();
				this.ptmp2 = new InnerComputationPoint3ai();
				this.radius = sphere.getRadius();
				this.x = sphere.getX() - this.radius;
				this.y = sphere.getY() - this.radius;
				this.z = sphere.getZ() - this.radius;
			}
		}

		@Override
		public PathIterator3ai<IE> restartIterations() {
			return new TransformedCirclePathIterator<>(this.sphere, this.transform);
		}

		@Pure
		@Override
		public boolean hasNext() {
			return this.index <= 5;
		}

		@Override
		public IE next() {
            if (this.index > 5) {
                throw new NoSuchElementException();
            }
			final int idx = this.index;
			++this.index;
            if (idx == 0) {
                final int dr = 2 * this.radius;
                final double[] ctrls = CTRL_PTS[3];
                this.movex = (int) (this.x + ctrls[6] * dr);
                this.movey = (int) (this.y + ctrls[7] * dr);
                this.movez = (int) (this.z + ctrls[8] * dr);
                this.p2.set(this.movex, this.movey, this.movez);
                this.transform.transform(this.p2);
                return getGeomFactory().newMovePathElement(this.p2.ix(), this.p2.iy(), this.p2.iz());
            } else if (idx < 5) {
                final int dr = 2 * this.radius;
                final double[] ctrls = CTRL_PTS[idx - 1];
                this.p1.set(this.p2);
                this.p2.set(this.x + ctrls[6] * dr, this.y + ctrls[7] * dr, this.z + ctrls[8] * dr);
                this.transform.transform(this.p2);
                this.ptmp1.set(this.x + ctrls[0] * dr, this.x + ctrls[1] * dr, this.y + ctrls[2] * dr);
                this.transform.transform(this.ptmp1);
                this.ptmp2.set(this.x + ctrls[3] * dr, this.y + ctrls[4] * dr, this.y + ctrls[5] * dr);
                this.transform.transform(this.ptmp2);
                return getGeomFactory().newCurvePathElement(this.p1.ix(), this.p1.iy(), this.p1.iz(), this.ptmp1.ix(),
                        this.ptmp1.iy(), this.ptmp1.iz(), this.ptmp2.ix(), this.ptmp2.iy(), this.ptmp2.iz(), this.p2.ix(),
                        this.p2.iy(), this.p2.iz());
            }
            this.p1.set(this.p2);
            this.p2.set(this.movex, this.movey, this.movez);
            this.transform.transform(this.p2);
            return getGeomFactory().newClosePathElement(this.p1.ix(), this.p1.iy(), this.p1.iz(), this.p2.ix(), this.p2.iy(),
                    this.p2.iz());
        }

	}

	/** Iterates on points on the perimeter of a sphere.
	 *
	 * <p>The rastrerization is based on a Bresenham algorithm.
	 *
	 * @param <P> the type of the points.
	 * @param <V> the type of the vectors.
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 13.0
	 */
	@SuppressWarnings("checkstyle:magicnumber")
	class SpherePerimeterIterator<P extends Point3D<? super P, ? super V>,
			V extends Vector3D<? super V, ? super P>> implements Iterator<P> {

		private final GeomFactory3D<V, P> factory;

		private final int cx;

		private final int cy;

		//TODO private final int cz;

		private final int cr;

		private final boolean skip;

		private final int maxOctant;

		private int currentOctant;

		private int x;

		private int y;

		//TODO private int z;

		private int dval;

		private P next;

		private final Set<P> junctionPoint = new TreeSet<>(new Tuple3iComparator());

		/** Construct the iterator from the initialOctant (inclusive) to the lastOctant (exclusive).
		 *
		 * @param factory the point factory.
		 * @param centerX the x coordinate of the center of the sphere.
		 * @param centerY the y coordinate of the center of the sphere.
		 * @param centerZ the y coordinate of the center of the sphere.
		 * @param radius the radius of the sphere.
		 * @param initialOctant the octant from which the iteration must start.
		 * @param lastOctant the first octant that must not be iterated on.
		 * @param skip indicates if the first point on an octant must be skip, because it is already replied when treating the
		 *     previous octant.
		 */
		public SpherePerimeterIterator(GeomFactory3D<V, P> factory,
				int centerX, int centerY, int centerZ, int radius, int initialOctant, int lastOctant, boolean skip) {
			assert factory != null : AssertMessages.notNullParameter(0);
			assert radius >= 0 : AssertMessages.positiveOrZeroParameter(4);
			assert initialOctant >= 0 && initialOctant < 8
					: AssertMessages.outsideRangeInclusiveParameter(initialOctant, 0, 7);
			assert lastOctant > initialOctant && lastOctant <= 8
					: AssertMessages.outsideRangeInclusiveParameter(lastOctant, initialOctant + 1, 8);
			this.factory = factory;
			this.cx = centerX;
			this.cy = centerY;
			//TODO this.cz = centerZ;
			this.cr = radius;
			this.skip = skip;
			this.maxOctant = lastOctant;
			this.currentOctant = initialOctant;
			reset();
			searchNext();
		}

		private void reset() {
			this.x = 0;
			this.y = this.cr;
			this.dval = 3 - 2 * this.cr;
            if (this.skip && (this.currentOctant == 3 || this.currentOctant == 4 || this.currentOctant == 6
                    || this.currentOctant == 7)) {
                // skip the first point because already replied in previous octant
                if (this.dval <= 0) {
                    this.dval += 4 * this.x + 6;
                } else {
                    this.dval += 4 * (this.x - this.y) + 10;
                    --this.y;
                }
				++this.x;
			}
		}

		@Pure
		@Override
		public boolean hasNext() {
            return this.next != null;
		}

		// TODO : integrate z coordinate
		@SuppressWarnings("checkstyle:cyclomaticcomplexity")
		private void searchNext() {
            if (this.currentOctant >= this.maxOctant) {
				this.next = null;
			} else {
				this.next = this.factory.newPoint();
				while (true) {
                    switch (this.currentOctant) {
					case 0:
						this.next.set(this.cx + this.x, this.cy + this.y, 0);
						break;
					case 1:
						this.next.set(this.cx + this.y, this.cy + this.x, 0);
						break;
					case 2:
						this.next.set(this.cx + this.x, this.cy - this.y, 0);
						break;
					case 3:
						this.next.set(this.cx + this.y, this.cy - this.x, 0);
						break;
					case 4:
						this.next.set(this.cx - this.x, this.cy - this.y, 0);
						break;
					case 5:
						this.next.set(this.cx - this.y, this.cy - this.x, 0);
						break;
					case 6:
						this.next.set(this.cx - this.x, this.cy + this.y, 0);
						break;
					case 7:
						this.next.set(this.cx - this.y, this.cy + this.x, 0);
						break;
					default:
						throw new NoSuchElementException();
					}

                    if (this.dval <= 0) {
						this.dval += 4 * this.x + 6;
					} else {
						this.dval += 4 * (this.x - this.y) + 10;
						--this.y;
					}
					++this.x;

                    if (this.x > this.y) {
						// The octant is finished.
						// Save the junction.
						boolean cont = this.junctionPoint.contains(this.next);
						if (!cont) {
							final P point = this.factory.newPoint();
							point.set(this.next.ix(), this.next.iy(), this.next.iz());
							this.junctionPoint.add(point);
						}
						// Goto next.
						++this.currentOctant;
						reset();
                        if (this.currentOctant >= this.maxOctant) {
                            if (cont) {
                                this.next = null;
                            }
                            cont = false;
                        }
                        if (!cont) {
                            return;
                        }
					} else {
						return;
					}
				}
			}
		}

		@Override
		public P next() {
			final P pixel = this.next;
            if (pixel == null) {
                throw new NoSuchElementException();
            }
			searchNext();
			return pixel;
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}

	}

}
