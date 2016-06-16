/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2016 The original authors, and other authors.
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

import org.arakhne.afc.math.MathConstants;
import org.arakhne.afc.math.MathUtil;
import org.arakhne.afc.math.geometry.PathElementType;
import org.arakhne.afc.math.geometry.d3.Point3D;
import org.arakhne.afc.vmutil.asserts.AssertMessages;
import org.arakhne.afc.vmutil.locale.Locale;

/** Shadow of a path that is used for computing the crossing values
 * between a shape and the shadow.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
class ClosestPointPathShadow3afp {

	private final PathIterator3afp<?> pathIterator;

	private final Point3D<?, ?> otherShapeClosestPoint = new InnerComputationPoint3afp();

	private final Point3D<?, ?> shadowShapeClosestPoint = new InnerComputationPoint3afp();

	private final Point3D<?, ?> temporaryPoint1 = new InnerComputationPoint3afp();

	private final Point3D<?, ?> temporaryPoint2 = new InnerComputationPoint3afp();

	private double minDistance = Double.POSITIVE_INFINITY;

	private final double boundingMinX;

	private final double boundingMinY;

	//TODO private final double boundingMinZ;

	//TODO private final double boundingMaxX;

	private final double boundingMaxY;

	//TODO private final double boundingMaxZ;

	private boolean started;

	private int crossings;

	private boolean hasX4ymin;

	private boolean hasX4ymax;

	private double x4ymin;

	private double x4ymax;

	/** Construct new path shadow.
	 * @param pathIterator the iterator on the path that is constituting the shadow.
	 * @param bounds the bounds of the shadow.
	 */
	ClosestPointPathShadow3afp(PathIterator3afp<?> pathIterator, RectangularPrism3afp<?, ?, ?, ?, ?, ?> bounds) {
		assert pathIterator != null : AssertMessages.notNullParameter(0);
		assert bounds != null : AssertMessages.notNullParameter(1);
		this.pathIterator = pathIterator;
		this.boundingMinX = bounds.getMinX();
		this.boundingMinY = bounds.getMinY();
		this.boundingMaxY = bounds.getMaxY();
		//TODO this.boundingMinZ = bounds.getMinZ();
		//TODO this.boundingMaxX = bounds.getMaxX();
		//TODO this.boundingMaxZ = bounds.getMaxZ();
	}

	/** Compute the crossings between this shadow and
	 * the given segment.
	 *
	 * @param crossings is the initial value of the crossings.
	 * @param x0 is the first point of the segment.
	 * @param y0 is the first point of the segment.
	 * @param z0 is the first point of the segment.
	 * @param x1 is the second point of the segment.
	 * @param y1 is the second point of the segment.
	 * @param z1 is the second point of the segment.
	 * @return the crossings or {@link MathConstants#SHAPE_INTERSECTS}.
	 */
	@SuppressWarnings("checkstyle:npathcomplexity")
	public int computeCrossings(
			int crossings,
			double x0, double y0, double z0,
			double x1, double y1, double z1) {
		// The segment is intersecting the bounds of the shadow path.
		// We must consider the shape of shadow path now.
		this.x4ymin = this.boundingMinX;
		this.x4ymax = this.boundingMinX;
		this.crossings = 0;
		this.hasX4ymin = false;
		this.hasX4ymax = false;
		final PathIterator3afp<?> iterator;
		if (this.started) {
			iterator = this.pathIterator.restartIterations();
		} else {
			this.started = true;
			iterator = this.pathIterator;
		}

		discretizePathIterator(
				iterator,
				x0, y0, z0, x1, y1, z1);

		// Test if the shape is intesecting the shadow shape.
		if (this.crossings == MathConstants.SHAPE_INTERSECTS) {
			// The given line is intersecting the path shape
			return MathConstants.SHAPE_INTERSECTS;
		}

		// There is no intersection with the shadow path's shape.
		// Compute the crossings with the minimum/maximum y borders.
		int inc = 0;
		if (this.hasX4ymin) {
			++inc;
		}
		if (this.hasX4ymax) {
			++inc;
		}

		final int numCrosses;
		if (y0 < y1) {
			numCrosses = inc;
		} else {
			numCrosses = -inc;
		}

		// Apply the previously computed crossings
		return crossings + numCrosses;
	}

	@SuppressWarnings({"checkstyle:parameternumber", "checkstyle:cyclomaticcomplexity",
		"checkstyle:npathcomplexity", "checkstyle:returncount"})
	private void discretizePathIterator(
			PathIterator3afp<?> pi,
			double x1, double y1, double z1, double x2, double y2, double z2) {
		if (!pi.hasNext() || this.crossings == MathConstants.SHAPE_INTERSECTS) {
			return;
		}
		PathElement3afp element;

		element = pi.next();
		if (element.getType() != PathElementType.MOVE_TO) {
			throw new IllegalArgumentException(Locale.getString(Path3afp.class, "E1")); //$NON-NLS-1$
		}

		Path3afp<?, ?, ?, ?, ?, ?> localPath;
		double movx = element.getToX();
		double movy = element.getToY();
		double movz = element.getToZ();
		double curx = movx;
		double cury = movy;
		double curz = movz;
		double endx;
		double endy;
		double endz;
		double distance;
		while (pi.hasNext()) {
			element = pi.next();
			switch (element.getType()) {
			case MOVE_TO:
				movx = element.getToX();
				curx = movx;
				movy = element.getToY();
				cury = movy;
				movz = element.getToZ();
				curz = movz;
				break;
			case LINE_TO:
				endx = element.getToX();
				endy = element.getToY();
				endz = element.getToZ();
				distance = Segment3afp.computeClosestPointToSegment(x1, y1, z1, x2, y2, z2, curx, cury, curz, endx, endy, endz,
						this.temporaryPoint1, this.temporaryPoint2);
				if (distance <= 0.) {
					this.otherShapeClosestPoint.set(this.temporaryPoint1);
					this.shadowShapeClosestPoint.set(this.temporaryPoint2);
					this.crossings = MathConstants.SHAPE_INTERSECTS;
					return;
				} else if (distance < this.minDistance) {
					this.minDistance = distance;
					this.otherShapeClosestPoint.set(this.temporaryPoint1);
					this.shadowShapeClosestPoint.set(this.temporaryPoint2);
				}
				crossSegmentTwoShadowLines(
						curx, cury, curz,
						endx, endy, endz,
						x1, y1, z1, x2, y2, z2);
				if (this.crossings == MathConstants.SHAPE_INTERSECTS) {
					return;
				}
				curx = endx;
				cury = endy;
				curz = endz;
				break;
			case QUAD_TO:
				endx = element.getToX();
				endy = element.getToY();
				endz = element.getToZ();
				// only for local use.
				localPath = pi.getGeomFactory().newPath(pi.getWindingRule());
				localPath.moveTo(curx, cury, curz);
				localPath.quadTo(
						element.getCtrlX1(), element.getCtrlY1(), element.getCtrlZ1(),
						endx, endy, endz);
				discretizePathIterator(
						localPath.getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO),
						x1, y1, z1, x2, y2, z2);
				if (this.crossings == MathConstants.SHAPE_INTERSECTS) {
					return;
				}
				curx = endx;
				cury = endy;
				curz = endz;
				break;
			case CURVE_TO:
				endx = element.getToX();
				endy = element.getToY();
				endz = element.getToZ();
				// only for local use.
				localPath = pi.getGeomFactory().newPath(pi.getWindingRule());
				localPath.moveTo(curx, cury, curz);
				localPath.curveTo(
						element.getCtrlX1(), element.getCtrlY1(), element.getCtrlZ1(),
						element.getCtrlX2(), element.getCtrlY2(), element.getCtrlZ2(),
						endx, endy, endz);
				discretizePathIterator(
						localPath.getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO),
						x1, y1, z1, x2, y2, z2);
				if (this.crossings == MathConstants.SHAPE_INTERSECTS) {
					return;
				}
				curx = endx;
				cury = endy;
				curz = endz;
				break;
			case ARC_TO:
				endx = element.getToX();
				endy = element.getToY();
				endz = element.getToZ();
				// only for local use.
				localPath = pi.getGeomFactory().newPath(pi.getWindingRule());
				localPath.moveTo(curx, cury, curz);
				//localPath.arcTo(
				//        endx, endy,
				//        element.getRadiusX(), element.getRadiusY(),
				//        element.getRotationX(), element.getLargeArcFlag(),
				//        element.getSweepFlag());
				discretizePathIterator(
						localPath.getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO),
						x1, y1, z1, x2, y2, z2);
				if (this.crossings == MathConstants.SHAPE_INTERSECTS) {
					return;
				}
				curx = endx;
				cury = endy;
				curz = endz;
				break;
			case CLOSE:
				if (cury != movy || curx != movx || curz != movz) {
					distance = Segment3afp.computeClosestPointToSegment(x1, y1, z1, x2, y2, z2, curx, cury, curz, movx, movy,
							movz, this.temporaryPoint1, this.temporaryPoint2);
					if (distance <= 0.) {
						this.otherShapeClosestPoint.set(this.temporaryPoint1);
						this.shadowShapeClosestPoint.set(this.temporaryPoint2);
						this.crossings = MathConstants.SHAPE_INTERSECTS;
						return;
					} else if (distance < this.minDistance) {
						this.minDistance = distance;
						this.otherShapeClosestPoint.set(this.temporaryPoint1);
						this.shadowShapeClosestPoint.set(this.temporaryPoint2);
					}
					crossSegmentTwoShadowLines(
							curx, cury, curz,
							movx, movy, movz,
							x1, y1, z1, x2, y2, z2);
				}
				if (this.crossings != 0) {
					return;
				}
				curx = movx;
				cury = movy;
				curz = movz;
				break;
			default:
			}
		}

		assert this.crossings != MathConstants.SHAPE_INTERSECTS;
		if (curx != movx || cury != movy) {
			// Assume that when is the path is open, only
			// SHAPE_INTERSECTS may be return
			this.crossings = 0;
		}
	}

	private void setCrossingCoordinateForYMax(double x, double y) {
		if (MathUtil.compareEpsilon(y, this.boundingMaxY) >= 0 && x > this.x4ymax) {
			this.x4ymax = x;
			this.hasX4ymax = true;
		}
	}

	private void setCrossingCoordinateForYMin(double x, double y) {
		if (MathUtil.compareEpsilon(y, this.boundingMinY) <= 0 && x > this.x4ymin) {
			this.x4ymin = x;
			this.hasX4ymin = true;
		}
	}

	/** Determine where the segment is crossing the two shadow lines.
	 *
	 * @param shadowX0 x coordinate of the reference point of the first shadow line.
	 * @param shadowY0 y coordinate of the reference point of the first shadow line.
	 * @param shadowX1 x coordinate of the reference point of the second shadow line.
	 * @param shadowY1 y coordinate of the reference point of the second shadow line.
	 * @param sx0 x coordinate of the first point of the segment.
	 * @param sy0 y coordinate of the first point of the segment.
	 * @param sx1 x coordinate of the second point of the segment.
	 * @param sy1 y coordinate of the second point of the segment.
	 */
	@SuppressWarnings({"checkstyle:parameternumber", "checkstyle:cyclomaticcomplexity",
			"checkstyle:npathcomplexity"})
	private void crossSegmentTwoShadowLines(
			double shadowX0, double shadowY0, double shadowZ0,
			double shadowX1, double shadowY1, double shadowZ1,
			double sx0, double sy0, double sz0,
			double sx1, double sy1, double sz1) {
		// Update the global bounds of the shadow.
		final double shadowXmin = Math.min(shadowX0, shadowX1);
		final double shadowXmax = Math.max(shadowX0, shadowX1);
		final double shadowYmin = Math.min(shadowY0, shadowY1);
		final double shadowYmax = Math.max(shadowY0, shadowY1);

		if (sy0 < shadowYmin && sy1 < shadowYmin) {
			// The segment is entirely at the bottom of the shadow.
			return;
		}
		if (sy0 > shadowYmax && sy1 > shadowYmax) {
			// The segment is entirely at the top of the shadow.
			return;
		}
		if (sx0 < shadowXmin && sx1 < shadowXmin) {
			// The segment is entirely at the left of the shadow.
			return;
		}
		if (sx0 >= shadowXmax && sx1 >= shadowXmax) {
			// The line is entirely at the right of the shadow
			final double alpha = (sx1 - sx0) / (sy1 - sy0);
			if (sy0 < sy1) {
				if (sy0 <= shadowYmin) {
					final double xintercept = sx0 + (shadowYmin - sy0) * alpha;
					setCrossingCoordinateForYMin(xintercept, shadowYmin);
					++this.crossings;
				}
				if (sy1 >= shadowYmax) {
					final double xintercept = sx0 + (shadowYmax - sy0) * alpha;
					setCrossingCoordinateForYMax(xintercept, shadowYmax);
					++this.crossings;
				}
			} else {
				if (sy1 <= shadowYmin) {
					final double xintercept = sx0 + (shadowYmin - sy0) * alpha;
					setCrossingCoordinateForYMin(xintercept, shadowYmin);
					--this.crossings;
				}
				if (sy0 >= shadowYmax) {
					final double xintercept = sx0 + (shadowYmax - sy0) * alpha;
					setCrossingCoordinateForYMax(xintercept, shadowYmax);
					--this.crossings;
				}
			}
		} else if (Segment3afp.intersectsSegmentSegmentWithoutEnds(
				shadowX0, shadowY0, shadowZ0, shadowX1, shadowY1, shadowZ1,
				sx0, sy0, sz0, sx1, sy1, sz1)) {
			// The segment is intersecting the shadowed segment.
			this.crossings = MathConstants.SHAPE_INTERSECTS;
		} else {
			final int side1;
			final int side2;
			if (shadowY0 <= shadowY1) {
				side1 = Segment3afp.computeSideLinePoint(
						shadowX0, shadowY0, shadowZ0,
						shadowX1, shadowY1, shadowZ1,
						sx0, sy0, sz0, 0.);
				side2 = Segment3afp.computeSideLinePoint(
						shadowX0, shadowY0, shadowZ0,
						shadowX1, shadowY1, shadowZ1,
						sx1, sy1, sz1, 0.);
			} else {
				side1 = Segment3afp.computeSideLinePoint(
						shadowX1, shadowY1, shadowZ1,
						shadowX0, shadowY0, shadowZ0,
						sx0, sy0, sz0, 0.);
				side2 = Segment3afp.computeSideLinePoint(
						shadowX1, shadowY1, shadowZ1,
						shadowX0, shadowY0, shadowZ0,
						sx1, sy1, sz1, 0.);
			}
			if (side1 > 0 || side2 > 0) {
				final double x0;
				final double x1;
				if (shadowYmin == shadowY0) {
					x0 = shadowX0;
					x1 = shadowX1;
				} else {
					x0 = shadowX1;
					x1 = shadowX0;
				}
				crossSegmentShadowLine(
						x0, shadowYmin,
						sx0, sy0, sx1, sy1);
				crossSegmentShadowLine(
						x1, shadowYmax,
						sx0, sy0, sx1, sy1);
			}
		}
	}

	/** Determine where the segment is crossing the shadow line.
	 *
	 * @param shadowx x coordinate of the reference point of the shadow line.
	 * @param shadowy y coordinate of the reference point of the shadow line.
	 * @param sx0 x coordinate of the first point of the segment.
	 * @param sy0 y coordinate of the first point of the segment.
	 * @param sx1 x coordinate of the second point of the segment.
	 * @param sy1 y coordinate of the second point of the segment.
	 */
	private void crossSegmentShadowLine(
			double shadowx, double shadowy,
			double sx0, double sy0,
			double sx1, double sy1) {
		if (shadowy <  sy0 && shadowy <  sy1) {
			// Segment is entirely at the top of shadow line
			return;
		}
		if (shadowy > sy0 && shadowy > sy1) {
			// Segment is entirely at the bottom of the shadow line
			return;
		}
		if (shadowx > sx0 && shadowx > sx1) {
			// Segment is entirely at the left of the shadow line
			return;
		}
		// Compute the intersection point between the segment and the shadow line
		final double xintercept = sx0 + (shadowy - sy0) * (sx1 - sx0) / (sy1 - sy0);
		if (shadowx > xintercept) {
			// The intersection point is on the left of the shadow line.
			return;
		}

		setCrossingCoordinateForYMax(xintercept, shadowy);
		setCrossingCoordinateForYMin(xintercept, shadowy);

		if (sy0 < sy1) {
			++this.crossings;
		} else {
			--this.crossings;
		}
	}

	/** Replies the closest point on the shape that is compared to the shadow.
	 *
	 * @return the closest point.
	 */
	public Point3D<?, ?> getClosestPointInOtherShape() {
		return this.otherShapeClosestPoint;
	}

	/** Replies the closest point on the shape that is compared to the shadow.
	 *
	 * @return the closest point.
	 */
	public Point3D<?, ?> getClosestPointInShadowShape() {
		return this.shadowShapeClosestPoint;
	}

}
