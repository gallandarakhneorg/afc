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

package org.arakhne.afc.math.geometry.d3.ai;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.MathConstants;
import org.arakhne.afc.math.MathUtil;
import org.arakhne.afc.math.geometry.CrossingComputationType;
import org.arakhne.afc.math.geometry.PathElementType;
import org.arakhne.afc.math.geometry.PathWindingRule;
import org.arakhne.afc.math.geometry.d3.Path3D;
import org.arakhne.afc.math.geometry.d3.PathIterator3D;
import org.arakhne.afc.math.geometry.d3.Point3D;
import org.arakhne.afc.math.geometry.d3.Transform3D;
import org.arakhne.afc.math.geometry.d3.Vector3D;
import org.arakhne.afc.math.geometry.d3.afp.Segment3afp;

/** Fonctional interface that represented a 2D path on a plane.
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
public interface Path3ai<
		ST extends Shape3ai<?, ?, IE, P, V, B>,
		IT extends Path3ai<?, ?, IE, P, V, B>,
		IE extends PathElement3ai,
		P extends Point3D<? super P, ? super V>,
		V extends Vector3D<? super V, ? super P>,
		B extends RectangularPrism3ai<?, ?, IE, P, V, B>>
		extends Shape3ai<ST, IT, IE, P, V, B>, Path3D<ST, IT, PathIterator3ai<IE>, P, V, B> {

	/** Multiple of cubic & quad curve size.
	 */
	int GROW_SIZE = 24;

	/** Default depth for the flattening of the path.
	 */
	int DEFAULT_FLATTENING_LIMIT = 10;

	/** The default winding rule: {@link PathWindingRule#NON_ZERO}.
	 */
	PathWindingRule DEFAULT_WINDING_RULE = PathWindingRule.NON_ZERO;

	/** Compute the box that corresponds to the drawable elements of the path.
	 *
	 * <p>An element is drawable if it is a line, a curve, or a closing path element.
	 *
	 * @param iterator the iterator on the path elements.
	 * @param box the box to set.
	 * @return <code>true</code> if a drawable element was found.
	 */
	@SuppressWarnings({"checkstyle:cyclomaticcomplexity", "checkstyle:npathcomplexity"})
	static boolean computeDrawableElementBoundingBox(PathIterator3ai<?> iterator,
			RectangularPrism3ai<?, ?, ?, ?, ?, ?> box) {
		assert iterator != null : "Iterator must not be null"; //$NON-NLS-1$
		assert box != null : "Rectangle must not be null"; //$NON-NLS-1$
		final GeomFactory3ai<?, ?, ?, ?> factory = iterator.getGeomFactory();
		boolean foundOneLine = false;
		int xmin = Integer.MAX_VALUE;
		int ymin = Integer.MAX_VALUE;
		int zmin = Integer.MAX_VALUE;
		int xmax = Integer.MIN_VALUE;
		int ymax = Integer.MIN_VALUE;
		int zmax = Integer.MIN_VALUE;
		PathElement3ai element;
		Path3ai<?, ?, ?, ?, ?, ?> subPath;
		while (iterator.hasNext()) {
			element = iterator.next();
            switch (element.getType()) {
			case LINE_TO:
                if (element.getFromX() < xmin) {
                    xmin = element.getFromX();
                }
                if (element.getFromY() < ymin) {
                    ymin = element.getFromY();
                }
                if (element.getFromZ() < zmin) {
                    zmin = element.getFromZ();
                }
                if (element.getFromX() > xmax) {
                    xmax = element.getFromX();
                }
                if (element.getFromY() > ymax) {
                    ymax = element.getFromY();
                }
                if (element.getFromZ() > zmax) {
                    zmax = element.getFromZ();
                }
                if (element.getToX() < xmin) {
                    xmin = element.getToX();
                }
                if (element.getToY() < ymin) {
                    ymin = element.getToY();
                }
                if (element.getToZ() < zmin) {
                    zmin = element.getToZ();
                }
                if (element.getToX() > xmax) {
                    xmax = element.getToX();
                }
                if (element.getToY() > ymax) {
                    ymax = element.getToY();
                }
                if (element.getToZ() > zmax) {
                    zmax = element.getToZ();
                }
				foundOneLine = true;
				break;
			case CURVE_TO:
				subPath = factory.newPath(iterator.getWindingRule());
				subPath.moveTo(element.getFromX(), element.getFromY(), element.getFromZ());
				subPath.curveTo(
						element.getCtrlX1(), element.getCtrlY1(), element.getCtrlZ1(),
						element.getCtrlX2(), element.getCtrlY2(), element.getCtrlZ2(),
						element.getToX(), element.getToY(), element.getToZ());
				if (computeDrawableElementBoundingBox(
						subPath.getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO),
						box)) {
                    if (box.getMinX() < xmin) {
                        xmin = box.getMinX();
                    }
                    if (box.getMinY() < ymin) {
                        ymin = box.getMinY();
                    }
                    if (box.getMinZ() < zmin) {
                        zmin = box.getMinZ();
                    }
                    if (box.getMaxX() > xmax) {
                        xmax = box.getMaxX();
                    }
                    if (box.getMaxY() > ymax) {
                        ymax = box.getMaxY();
                    }
                    if (box.getMaxZ() > zmax) {
                        zmax = box.getMaxZ();
                    }
					foundOneLine = true;
				}
				break;
			case QUAD_TO:
				subPath = factory.newPath(iterator.getWindingRule());
				subPath.moveTo(element.getFromX(), element.getFromY(), element.getFromZ());
				subPath.quadTo(
						element.getCtrlX1(), element.getCtrlY1(), element.getCtrlZ1(),
						element.getToX(), element.getToY(), element.getToZ());
				if (computeDrawableElementBoundingBox(
						subPath.getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO),
						box)) {
                    if (box.getMinX() < xmin) {
                        xmin = box.getMinX();
                    }
                    if (box.getMinY() < ymin) {
                        ymin = box.getMinY();
                    }
                    if (box.getMinZ() < zmin) {
                        zmin = box.getMinZ();
                    }
                    if (box.getMaxX() > xmax) {
                        xmax = box.getMaxX();
                    }
                    if (box.getMaxY() > ymax) {
                        ymax = box.getMaxY();
                    }
                    if (box.getMaxZ() > zmax) {
                        zmax = box.getMaxZ();
                    }
					foundOneLine = true;
				}
				break;
			case MOVE_TO:
			case CLOSE:
			case ARC_TO:
			default:
			}
		}
		if (foundOneLine) {
			box.setFromCorners(xmin, ymin, zmin, xmax, ymax, zmax);
		} else {
			box.clear();
		}
		return foundOneLine;
	}

	/** Compute the box that corresponds to the control points of the path.
	 *
	 * <p>An element is drawable if it is a line, a curve, or a closing path element.
	 * The box fits the drawn lines and the drawn curves. The control points of the
	 * curves may be outside the output box. For obtaining the bounding box
	 * of the drawn lines and cruves, use
	 * {@link #computeDrawableElementBoundingBox(PathIterator3ai, RectangularPrism3ai)}.
	 *
	 * @param iterator the iterator on the path elements.
	 * @param box the box to set.
	 * @return <code>true</code> if a control point was found.
	 * @see #computeDrawableElementBoundingBox(PathIterator3ai, RectangularPrism3ai)
	 */
	@SuppressWarnings({"checkstyle:cyclomaticcomplexity", "checkstyle:npathcomplexity"})
	static boolean computeControlPointBoundingBox(PathIterator3ai<?> iterator,
			RectangularPrism3ai<?, ?, ?, ?, ?, ?> box) {
		assert iterator != null : "Iterator must be not null"; //$NON-NLS-1$
		assert box != null : "Rectangle must be not null"; //$NON-NLS-1$
		boolean foundOneControlPoint = false;
		int xmin = Integer.MAX_VALUE;
		int ymin = Integer.MAX_VALUE;
		int zmin = Integer.MAX_VALUE;
		int xmax = Integer.MIN_VALUE;
		int ymax = Integer.MIN_VALUE;
		int zmax = Integer.MIN_VALUE;
		PathElement3ai element;
		while (iterator.hasNext()) {
			element = iterator.next();
            switch (element.getType()) {
			case LINE_TO:
                if (element.getFromX() < xmin) {
                    xmin = element.getFromX();
                }
                if (element.getFromY() < ymin) {
                    ymin = element.getFromY();
                }
                if (element.getFromZ() < zmin) {
                    zmin = element.getFromZ();
                }
                if (element.getFromX() > xmax) {
                    xmax = element.getFromX();
                }
                if (element.getFromY() > ymax) {
                    ymax = element.getFromY();
                }
                if (element.getFromZ() > zmax) {
                    zmax = element.getFromZ();
                }
                if (element.getToX() < xmin) {
                    xmin = element.getToX();
                }
                if (element.getToY() < ymin) {
                    ymin = element.getToY();
                }
                if (element.getToZ() < zmin) {
                    zmin = element.getToZ();
                }
                if (element.getToX() > xmax) {
                    xmax = element.getToX();
                }
                if (element.getToY() > ymax) {
                    ymax = element.getToY();
                }
                if (element.getToZ() > zmax) {
                    zmax = element.getToZ();
                }
				foundOneControlPoint = true;
				break;
			case CURVE_TO:
                if (element.getFromX() < xmin) {
                    xmin = element.getFromX();
                }
                if (element.getFromY() < ymin) {
                    ymin = element.getFromY();
                }
                if (element.getFromZ() < zmin) {
                    zmin = element.getFromZ();
                }
                if (element.getFromX() > xmax) {
                    xmax = element.getFromX();
                }
                if (element.getFromY() > ymax) {
                    ymax = element.getFromY();
                }
                if (element.getFromZ() > zmax) {
                    zmax = element.getFromZ();
                }
                if (element.getCtrlX1() < xmin) {
                    xmin = element.getCtrlX1();
                }
                if (element.getCtrlY1() < ymin) {
                    ymin = element.getCtrlY1();
                }
                if (element.getCtrlZ1() < zmin) {
                    zmin = element.getCtrlZ1();
                }
                if (element.getCtrlX1() > xmax) {
                    xmax = element.getCtrlX1();
                }
                if (element.getCtrlY1() > ymax) {
                    ymax = element.getCtrlY1();
                }
                if (element.getCtrlZ1() > zmax) {
                    zmax = element.getCtrlZ1();
                }
                if (element.getCtrlX2() < xmin) {
                    xmin = element.getCtrlX2();
                }
                if (element.getCtrlY2() < ymin) {
                    ymin = element.getCtrlY2();
                }
                if (element.getCtrlZ2() < zmin) {
                    zmin = element.getCtrlZ2();
                }
                if (element.getCtrlX2() > xmax) {
                    xmax = element.getCtrlX2();
                }
                if (element.getCtrlY2() > ymax) {
                    ymax = element.getCtrlY2();
                }
                if (element.getCtrlZ2() > zmax) {
                    zmax = element.getCtrlZ2();
                }
                if (element.getToX() < xmin) {
                    xmin = element.getToX();
                }
                if (element.getToY() < ymin) {
                    ymin = element.getToY();
                }
                if (element.getToZ() < zmin) {
                    zmin = element.getToZ();
                }
                if (element.getToX() > xmax) {
                    xmax = element.getToX();
                }
                if (element.getToY() > ymax) {
                    ymax = element.getToY();
                }
                if (element.getToZ() > zmax) {
                    zmax = element.getToZ();
                }
				foundOneControlPoint = true;
				break;
			case QUAD_TO:
                if (element.getFromX() < xmin) {
                    xmin = element.getFromX();
                }
                if (element.getFromY() < ymin) {
                    ymin = element.getFromY();
                }
                if (element.getFromZ() < zmin) {
                    zmin = element.getFromZ();
                }
                if (element.getFromX() > xmax) {
                    xmax = element.getFromX();
                }
                if (element.getFromY() > ymax) {
                    ymax = element.getFromY();
                }
                if (element.getFromZ() > zmax) {
                    zmax = element.getFromZ();
                }
                if (element.getCtrlX1() < xmin) {
                    xmin = element.getCtrlX1();
                }
                if (element.getCtrlY1() < ymin) {
                    ymin = element.getCtrlY1();
                }
                if (element.getCtrlZ1() < zmin) {
                    zmin = element.getCtrlZ1();
                }
                if (element.getCtrlX1() > xmax) {
                    xmax = element.getCtrlX1();
                }
                if (element.getCtrlY1() > ymax) {
                    ymax = element.getCtrlY1();
                }
                if (element.getCtrlZ1() > zmax) {
                    zmax = element.getCtrlZ1();
                }
                if (element.getToX() < xmin) {
                    xmin = element.getToX();
                }
                if (element.getToY() < ymin) {
                    ymin = element.getToY();
                }
                if (element.getToZ() < zmin) {
                    zmin = element.getToZ();
                }
                if (element.getToX() > xmax) {
                    xmax = element.getToX();
                }
                if (element.getToY() > ymax) {
                    ymax = element.getToY();
                }
                if (element.getToZ() > zmax) {
                    zmax = element.getToZ();
                }
				foundOneControlPoint = true;
				break;
			case MOVE_TO:
			case CLOSE:
			case ARC_TO:
			default:
			}
		}
		if (foundOneControlPoint) {
			box.setFromCorners(xmin, ymin, zmin, xmax, ymax, zmax);
		} else {
			box.clear();
		}
		return foundOneControlPoint;
	}

	/**
	 * Calculates the number of times the given path
	 * crosses the given circle extending to the right.
	 *
	 * @param crossings is the initial value for crossing.
	 * @param pi is the description of the path.
	 * @param x1 is the first point of the segment.
	 * @param y1 is the first point of the segment.
	 * @param z1 is the first point of the segment.
	 * @param x2 is the first point of the segment.
	 * @param y2 is the first point of the segment.
	 * @param z2 is the first point of the segment.
	 * @param type is the type of special computation to apply. If <code>null</code>, it
	 *     is equivalent to {@link CrossingComputationType#STANDARD}.
	 * @return the crossing
	 */
	@SuppressWarnings({"checkstyle:cyclomaticcomplexity", "checkstyle:npathcomplexity", "checkstyle:parameternumber"})
	static int computeCrossingsFromSegment(int crossings, PathIterator3ai<?> pi, int x1, int y1, int z1, int x2, int y2, int z2,
			CrossingComputationType type) {
		assert pi != null : "Iterator must not be null"; //$NON-NLS-1$

		// Copied from the AWT API
		if (!pi.hasNext()) {
            return 0;
        }
		PathElement3ai element;

		element = pi.next();
		if (element.getType() != PathElementType.MOVE_TO) {
			throw new IllegalArgumentException("missing initial moveto in path definition"); //$NON-NLS-1$
		}

		int movx = element.getToX();
		int movy = element.getToY();
		int movz = element.getToZ();
		int curx = movx;
		int cury = movy;
		int curz = movz;
		int endx;
		int endy;
		int endz;
		int numCrosses = crossings;
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
				numCrosses = Segment3ai.computeCrossingsFromSegment(
						numCrosses,
						x1, y1, z1, x2, y2, z2,
						curx, cury, curz,
						endx, endy, endz);
                if (numCrosses == MathConstants.SHAPE_INTERSECTS) {
                    return numCrosses;
                }
				curx = endx;
				cury = endy;
				curz = endz;
				break;
			case QUAD_TO:
			    endx = element.getToX();
			    endy = element.getToY();
			    endz = element.getToZ();
			    Path3ai<?, ?, ?, ?, ?, ?> localPath = pi.getGeomFactory().newPath(pi.getWindingRule());
                localPath.moveTo(element.getFromX(), element.getFromY(), element.getFromZ());
			    localPath.quadTo(
			            element.getCtrlX1(), element.getCtrlY1(), element.getCtrlZ1(),
			            endx, endy, endz);
			    numCrosses = computeCrossingsFromSegment(
			            numCrosses,
			            localPath.getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO),
			            x1, y1, z1, x2, y2, z2,
			            CrossingComputationType.STANDARD);
                if (numCrosses == MathConstants.SHAPE_INTERSECTS) {
                    return numCrosses;
                }
			    curx = endx;
			    cury = endy;
			    curz = endz;
			    break;
            case CURVE_TO:
				endx = element.getToX();
				endy = element.getToY();
				endz = element.getToZ();
				localPath = pi.getGeomFactory().newPath(pi.getWindingRule());
				localPath.moveTo(element.getFromX(), element.getFromY(), element.getFromZ());
				localPath.curveTo(
						element.getCtrlX1(), element.getCtrlY1(), element.getCtrlZ1(),
						element.getCtrlX2(), element.getCtrlY2(), element.getCtrlZ2(),
						endx, endy, endz);
				numCrosses = computeCrossingsFromSegment(
						numCrosses,
						localPath.getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO),
						x1, y1, z1, x2, y2, z2,
						CrossingComputationType.STANDARD);
                if (numCrosses == MathConstants.SHAPE_INTERSECTS) {
                    return numCrosses;
                }
				curx = endx;
				cury = endy;
				curz = endz;
				break;
			case CLOSE:
				if (cury != movy || curx != movx || curz != movz) {
					numCrosses = Segment3ai.computeCrossingsFromSegment(
							numCrosses,
							x1, y1, z1, x2, y2, z2,
							curx, cury, curz,
							movx, movy, movz);
                    if (numCrosses == MathConstants.SHAPE_INTERSECTS) {
                        return numCrosses;
                    }
				}
				curx = movx;
				cury = movy;
				curz = movz;
				break;
			case ARC_TO:
			default:
			}
		}

        assert numCrosses != MathConstants.SHAPE_INTERSECTS;

		final boolean isOpen = (curx != movx) || (cury != movy) || (curz != movz);

		if (isOpen && type != null) {
			switch (type) {
			case AUTO_CLOSE:
				numCrosses = Segment3ai.computeCrossingsFromSegment(
						numCrosses,
						x1, y1, z1, x2, y2, z2,
						curx, cury, curz,
						movx, movy, movz);
				break;
			case SIMPLE_INTERSECTION_WHEN_NOT_POLYGON:
				// Assume that when is the path is open, only
				// SHAPE_INTERSECTS may be return
				numCrosses = 0;
				break;
			case STANDARD:
			default:
			}
		}

		return numCrosses;
	}

	/**
	 * Calculates the number of times the given path
	 * crosses the given circle extending to the right.
	 *
	 * @param crossings is the initial value for crossing.
	 * @param pi is the description of the path.
	 * @param cx is the center of the circle.
	 * @param cy is the center of the circle.
	 * @param cz is the center of the circle.
	 * @param radius is the radius of the circle.
	 * @param type is the type of special computation to apply. If <code>null</code>, it
	 *     is equivalent to {@link CrossingComputationType#STANDARD}.
	 * @return the crossing
	 */
	@SuppressWarnings({"checkstyle:npathcomplexity", "checkstyle:cyclomaticcomplexity"})
	static int computeCrossingsFromSphere(int crossings, PathIterator3ai<?> pi, int cx, int cy, int cz, int radius,
			CrossingComputationType type) {
		assert pi != null : "Iterator must not be null"; //$NON-NLS-1$
		assert radius >= 0 : "Circle radius must be positive or zero"; //$NON-NLS-1$

		// Copied from the AWT API
		if (!pi.hasNext()) {
            return 0;
        }
		PathElement3ai element;

		element = pi.next();
		if (element.getType() != PathElementType.MOVE_TO) {
			throw new IllegalArgumentException("missing initial moveto in path definition"); //$NON-NLS-1$
		}

		int movx = element.getToX();
		int movy = element.getToY();
		int movz = element.getToZ();
		int curx = movx;
		int cury = movy;
		int curz = movz;
		int endx;
		int endy;
		int endz;
		int numCrosses = crossings;
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
				numCrosses = Segment3ai.computeCrossingsFromSphere(
						numCrosses,
						cx, cy, cz, radius,
						curx, cury, curz,
						endx, endy, endz);
                if (numCrosses == MathConstants.SHAPE_INTERSECTS) {
					return numCrosses;
				}
				curx = endx;
				cury = endy;
				curz = endz;
				break;
			case QUAD_TO:
                endx = element.getToX();
                endy = element.getToY();
                endz = element.getToZ();
                Path3ai<?, ?, ?, ?, ?, ?> localPath = pi.getGeomFactory().newPath(pi.getWindingRule());
                localPath.moveTo(element.getFromX(), element.getFromY(), element.getFromZ());
                localPath.quadTo(
						element.getCtrlX1(), element.getCtrlY1(), element.getCtrlZ1(),
						endx, endy, endz);
                numCrosses = computeCrossingsFromSphere(
						numCrosses,
						localPath.getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO),
						cx, cy, cz, radius,
						CrossingComputationType.STANDARD);
                if (numCrosses == MathConstants.SHAPE_INTERSECTS) {
					return numCrosses;
				}
                curx = endx;
                cury = endy;
                curz = endz;
                break;
            case CURVE_TO:
				endx = element.getToX();
				endy = element.getToY();
				endz = element.getToZ();
				localPath = pi.getGeomFactory().newPath(pi.getWindingRule());
				localPath.moveTo(element.getFromX(), element.getFromY(), element.getFromZ());
				localPath.curveTo(
						element.getCtrlX1(), element.getCtrlY1(), element.getCtrlZ1(),
						element.getCtrlX2(), element.getCtrlY2(), element.getCtrlZ2(),
						endx, endy, endz);
				numCrosses = computeCrossingsFromSphere(
						numCrosses,
						localPath.getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO),
						cx, cy, cz, radius,
						CrossingComputationType.STANDARD);
                if (numCrosses == MathConstants.SHAPE_INTERSECTS) {
					return numCrosses;
				}
				curx = endx;
				cury = endy;
				curz = endz;
				break;
			case CLOSE:
				if (cury != movy || curx != movx || curz != movz) {
					numCrosses = Segment3ai.computeCrossingsFromSphere(
							numCrosses,
							cx, cy, cz, radius,
							curx, cury, curz,
							movx, movy, movz);
                    if (numCrosses == MathConstants.SHAPE_INTERSECTS) {
						return numCrosses;
					}
				}
				curx = movx;
				cury = movy;
				curz = movz;
				break;
			case ARC_TO:
			default:
			}
		}

		assert numCrosses != MathConstants.SHAPE_INTERSECTS;

		final boolean isOpen = (curx != movx) || (cury != movy) || (curz != movz);

		if (isOpen && type != null) {
            switch (type) {
			case AUTO_CLOSE:
				// Auto close
				numCrosses = Segment3ai.computeCrossingsFromSphere(
						numCrosses,
						cx, cy, cz, radius,
						curx, cury, curz,
						movx, movy, movz);
				break;
			case SIMPLE_INTERSECTION_WHEN_NOT_POLYGON:
				// Assume that when is the path is open, only
				// SHAPE_INTERSECTS may be return
				numCrosses = 0;
				break;
			case STANDARD:
			default:
				// Standard behavior
				break;
			}
		}

		return numCrosses;
	}

	/**
	 * Calculates the number of times the given path
	 * crosses the ray extending to the right from (px,py).
	 * If the point lies on a part of the path,
	 * then no crossings are counted for that intersection.
	 * +1 is added for each crossing where the Y coordinate is increasing
	 * -1 is added for each crossing where the Y coordinate is decreasing
	 * The return value is the sum of all crossings for every segment in
	 * the path.
	 * The path must start with a MOVE_TO, otherwise an exception is
	 * thrown.
	 *
	 * @param crossings the initial crossing.
	 * @param pi is the description of the path.
	 * @param px is the reference point to test.
	 * @param py is the reference point to test.
	 * @param pz is the reference point to test.
	 * @param type is the type of special computation to apply. If <code>null</code>, it
	 *     is equivalent to {@link CrossingComputationType#STANDARD}.
	 * @return the crossing, or {@link MathConstants#SHAPE_INTERSECTS}
	 */
	@SuppressWarnings({"checkstyle:npathcomplexity", "checkstyle:cyclomaticcomplexity"})
    static int computeCrossingsFromPoint(int crossings, PathIterator3ai<?> pi, int px, int py, int pz,
            CrossingComputationType type) {
		assert pi != null : "Iterator must not be null"; //$NON-NLS-1$

		// Copied and adapted from the AWT API
		if (!pi.hasNext()) {
            return 0;
        }
		PathElement3ai element;

		element = pi.next();
		if (element.getType() != PathElementType.MOVE_TO) {
			throw new IllegalArgumentException("missing initial moveto in path definition"); //$NON-NLS-1$
		}

		int movx = element.getToX();
		int movy = element.getToY();
		int movz = element.getToZ();
		int curx = movx;
		int cury = movy;
		int curz = movz;
		int endx;
		int endy;
		int endz;
		int numCrossings = crossings;

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
				numCrossings = Segment3ai.computeCrossingsFromPoint(
						numCrossings,
						px, py, pz,
						curx, cury, curz,
						endx, endy, endz);
                if (numCrossings == MathConstants.SHAPE_INTERSECTS) {
					return numCrossings;
				}
				curx = endx;
				cury = endy;
				curz = endz;
				break;
			case QUAD_TO:
				endx = element.getToX();
				endy = element.getToY();
				endz = element.getToZ();
				Path3ai<?, ?, ?, ?, ?, ?> curve = pi.getGeomFactory().newPath(pi.getWindingRule());
				curve.moveTo(element.getFromX(), element.getFromY(), element.getFromZ());
				curve.quadTo(element.getCtrlX1(), element.getCtrlY1(), element.getCtrlZ1(), endx, endy, endz);
				numCrossings = computeCrossingsFromPoint(
						numCrossings,
						curve.getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO),
						px, py, pz, CrossingComputationType.STANDARD);
                if (numCrossings == MathConstants.SHAPE_INTERSECTS) {
					return numCrossings;
				}
				curx = endx;
				cury = endy;
				curz = endz;
				break;
			case CURVE_TO:
				endx = element.getToX();
				endy = element.getToY();
				endz = element.getToZ();
				curve = pi.getGeomFactory().newPath(pi.getWindingRule());
				curve.moveTo(element.getFromX(), element.getFromY(), element.getFromZ());
				curve.curveTo(
						element.getCtrlX1(), element.getCtrlY1(), element.getCtrlZ1(),
						element.getCtrlX2(), element.getCtrlY2(), element.getCtrlZ2(),
						endx, endy, endz);
				numCrossings = computeCrossingsFromPoint(
						numCrossings,
						curve.getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO),
						px, py, pz, CrossingComputationType.STANDARD);
                if (numCrossings == MathConstants.SHAPE_INTERSECTS) {
					return numCrossings;
				}
				curx = endx;
				cury = endy;
				curz = endz;
				break;
			case CLOSE:
				if (cury != movy || curx != movx || curz != movz) {
					numCrossings = Segment3ai.computeCrossingsFromPoint(
							numCrossings,
							px, py, pz,
							curx, cury, curz,
							movx, movy, movz);
                    if (numCrossings == MathConstants.SHAPE_INTERSECTS) {
						return numCrossings;
					}
				}
				curx = movx;
				cury = movy;
				curz = movz;
				break;
			case ARC_TO:
			default:
			}
		}

        assert numCrossings != MathConstants.SHAPE_INTERSECTS;

		final boolean isOpen = (curx != movx) || (cury != movy) || (curz != movz);

		if (isOpen && type != null) {
			switch (type) {
			case AUTO_CLOSE:
			    // Not closed
			    if (movx == px && movy == py && movz == pz) {
                    return MathConstants.SHAPE_INTERSECTS;
                }
				numCrossings = Segment3ai.computeCrossingsFromPoint(
						numCrossings,
						px, py, pz,
						curx, cury, curz,
						movx, movy, movz);
				break;
			case SIMPLE_INTERSECTION_WHEN_NOT_POLYGON:
				// Assume that when is the path is open, only
				// SHAPE_INTERSECTS may be return
				numCrossings = 0;
				break;
			case STANDARD:
			default:
				break;
			}
		}

		return numCrossings;
	}

	/**
	 * Accumulate the number of times the path crosses the shadow
	 * extending to the right of the second path.  See the comment
	 * for the SHAPE_INTERSECTS constant for more complete details.
	 * The return value is the sum of all crossings for both the
	 * top and bottom of the shadow for every segment in the path,
	 * or the special value SHAPE_INTERSECTS if the path ever enters
	 * the interior of the rectangle.
	 * The path must start with a SEG_MOVETO, otherwise an exception is
	 * thrown.
	 * The caller must check r[xy]{min,max} for NaN values.
	 *
	 * @param crossings the intial crossing.
	 * @param iterator is the iterator on the path elements.
	 * @param shadow is the description of the shape to project to the right.
     * @param type is the type of special computation to apply. If <code>null</code>, it
     *     is equivalent to {@link CrossingComputationType#STANDARD}.
	 * @return the crossings.
	 * @see "Weiler–Atherton clipping algorithm"
	 */
	@SuppressWarnings({"checkstyle:npathcomplexity", "checkstyle:cyclomaticcomplexity"})
    static int computeCrossingsFromPath(int crossings, PathIterator3ai<?> iterator, BasicPathShadow3ai shadow,
            CrossingComputationType type) {
		assert iterator != null : "Iterator must not be null"; //$NON-NLS-1$
		assert shadow != null : "The shadow projected on the right must not be null"; //$NON-NLS-1$

        if (!iterator.hasNext()) {
            return 0;
        }

		PathElement3ai pathElement1 = iterator.next();

		if (pathElement1.getType() != PathElementType.MOVE_TO) {
			throw new IllegalArgumentException("missing initial moveto in the first path definition"); //$NON-NLS-1$
		}

		final GeomFactory3ai<?, ?, ?, ?> factory = iterator.getGeomFactory();
		Path3ai<?, ?, ?, ?, ?, ?> subPath;
		int movx = pathElement1.getToX();
        int curx = movx;
        int movy = pathElement1.getToY();
        int cury = movy;
        int movz = pathElement1.getToZ();
        int curz = movz;
		int numCrossings = crossings;
		int endx;
		int endy;
		int endz;
		while (numCrossings != MathConstants.SHAPE_INTERSECTS
				&& iterator.hasNext()) {
			pathElement1 = iterator.next();
			switch (pathElement1.getType()) {
			case MOVE_TO:
			    // Count should always be a multiple of 3 here.
			    // assert((crossings & 1) != 0);
			    movx = pathElement1.getToX();
			    curx = movx;
			    movy = pathElement1.getToY();
			    cury = movy;
			    movz = pathElement1.getToZ();
			    curz = movz;
			    break;
			case LINE_TO:
				endx = pathElement1.getToX();
				endy = pathElement1.getToY();
				endz = pathElement1.getToZ();
				numCrossings = shadow.computeCrossings(numCrossings,
						curx, cury, curz,
						endx, endy, endz);
                if (numCrossings == MathConstants.SHAPE_INTERSECTS) {
                    return numCrossings;
                }
				curx = endx;
				cury = endy;
				curz = endz;
				break;
			case QUAD_TO:
				endx = pathElement1.getToX();
				endy = pathElement1.getToY();
				endz = pathElement1.getToZ();
				// only for local use.
				subPath = factory.newPath(iterator.getWindingRule());
				subPath.moveTo(curx, cury, curz);
				subPath.quadTo(
						pathElement1.getCtrlX1(), pathElement1.getCtrlY1(), pathElement1.getCtrlZ1(),
						endx, endy, endz);
				numCrossings = computeCrossingsFromPath(
				        numCrossings,
						subPath.getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO),
						shadow,
						CrossingComputationType.STANDARD);
                if (numCrossings == MathConstants.SHAPE_INTERSECTS) {
                    return numCrossings;
                }
				curx = endx;
				cury = endy;
				curz = endz;
				break;
			case CURVE_TO:
				endx = pathElement1.getToX();
				endy = pathElement1.getToY();
				endz = pathElement1.getToZ();
				// only for local use
				subPath = factory.newPath(iterator.getWindingRule());
				subPath.moveTo(curx, cury, curz);
				subPath.curveTo(
						pathElement1.getCtrlX1(), pathElement1.getCtrlY1(), pathElement1.getCtrlZ1(),
						pathElement1.getCtrlX2(), pathElement1.getCtrlY2(), pathElement1.getCtrlZ2(),
						endx, endy, endz);
				numCrossings = computeCrossingsFromPath(
				        numCrossings,
						subPath.getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO),
						shadow,
						CrossingComputationType.STANDARD);
                if (numCrossings == MathConstants.SHAPE_INTERSECTS) {
                    return numCrossings;
                }
				curx = endx;
				cury = endy;
				curz = endz;
				break;
			case CLOSE:
				if (curx != movx || cury != movy || curz != movz) {
					numCrossings = shadow.computeCrossings(numCrossings,
							curx, cury, curz,
							movx, movy, movz);
				}
				// Stop as soon as possible
				if (numCrossings != 0) {
                    return numCrossings;
                }
				curx = movx;
				cury = movy;
				curz = movz;
				break;
			case ARC_TO:
			default:
			}
		}

        assert numCrossings != MathConstants.SHAPE_INTERSECTS;

		final boolean isOpen = (curx != movx) || (cury != movy) || (curz != movz);

		if (isOpen && type != null) {
			switch (type) {
			case AUTO_CLOSE:
				// Not closed
				numCrossings = shadow.computeCrossings(numCrossings,
						curx, cury, curz,
						movx, movy, movz);
				break;
			case SIMPLE_INTERSECTION_WHEN_NOT_POLYGON:
				// Assume that when is the path is open, only
				// SHAPE_INTERSECTS may be returned
				numCrossings = 0;
				break;
			case STANDARD:
			default:
			    break;
			}
		}

		return numCrossings;
	}

	/**
	 * Tests if the specified coordinates are inside the closed
	 * boundary of the specified {@link PathIterator3ai}.
	 *
	 * <p>This method provides a basic facility for implementors of
	 * the {@link Shape3ai} interface to implement support for the
	 * {@link Shape3ai#contains(int, int, int)} method.
	 *
	 * @param pi the specified {@code PathIterator2f}
	 * @param x the specified X coordinate
	 * @param y the specified Y coordinate
	 * @param z the specified Z coordinate
	 * @return {@code true} if the specified coordinates are inside the
	 *         specified {@code PathIterator2f}; {@code false} otherwise
	 */
	static boolean contains(PathIterator3ai<?> pi, int x, int y, int z) {
		assert pi != null : "Iterator must not be null"; //$NON-NLS-1$
		// Copied from the AWT API
		final int mask = pi.getWindingRule() == PathWindingRule.NON_ZERO ? -1 : 1;
		final int cross = computeCrossingsFromPoint(0, pi, x, y, z, CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON);
		return (cross & mask) != 0;
	}

    /**
     * Tests if the specified rectangle is inside the closed
     * boundary of the specified {@link PathIterator3ai}.
     *
     * <p>The points on the path are assumed to be outside the path area.
     * It means that is the rectangle is intersecting the path, this
     * function replies <code>false</code>.
     *
     * @param pi the specified {@code PathIterator3ai}
     * @param rx the lowest corner of the rectangle.
     * @param ry the lowest corner of the rectangle.
     * @param rz the lowest corner of the rectangle.
     * @param rwidth is the width of the rectangle.
     * @param rheight is the width of the rectangle.
     * @param rdepth is the width of the rectangle.
     * @return {@code true} if the specified rectangle is inside the
     *         specified {@code PathIterator2f}; {@code false} otherwise.
     */
    static boolean contains(PathIterator3ai<?> pi, int rx, int ry, int rz, int rwidth, int rheight, int rdepth) {
        assert pi != null : "Iterator must not be null"; //$NON-NLS-1$
        assert rwidth >= 0 : "Rectangle width must be positive or zero."; //$NON-NLS-1$
        assert rheight >= 0 : "Rectangle height must be positive or zero"; //$NON-NLS-1$
        assert rdepth >= 0 : "Rectangle height must be positive or zero"; //$NON-NLS-1$
        // Copied from AWT API
        final int mask = pi.getWindingRule() == PathWindingRule.NON_ZERO ? -1 : 2;
        final int crossings = computeCrossingsFromRect(
                0,
                pi,
                rx, ry, rz, rx + rwidth, ry + rheight, rz + rdepth,
                CrossingComputationType.AUTO_CLOSE);
        return crossings != MathConstants.SHAPE_INTERSECTS && (crossings & mask) != 0;
    }

    @Pure
    @Override
    default boolean contains(RectangularPrism3ai<?, ?, ?, ?, ?, ?> box) {
        assert box != null : "Rectangle must not be null"; //$NON-NLS-1$
        return contains(getPathIterator(),
                box.getMinX(), box.getMinY(), box.getMinZ(), box.getWidth(), box.getHeight(), box.getHeight());
    }

    @Override
    default boolean contains(int x, int y, int z) {
        return contains(getPathIterator(), x, y, z);
    }

    /**
     * Accumulate the number of times the path crosses the shadow
     * extending to the right of the rectangle.  See the comment
     * for the SHAPE_INTERSECTS constant for more complete details.
     * The return value is the sum of all crossings for both the
     * top and bottom of the shadow for every segment in the path,
     * or the special value SHAPE_INTERSECTS if the path ever enters
     * the interior of the rectangle.
     * The path must start with a SEG_MOVETO, otherwise an exception is
     * thrown.
     * The caller must check r[xy]{min,max} for NaN values.
     *
     * @param crossings the initial crossing.
     * @param pi is the iterator on the path elements.
     * @param rxmin is the first corner of the rectangle.
     * @param rymin is the first corner of the rectangle.
     * @param rzmin is the first corner of the rectangle.
     * @param rxmax is the second corner of the rectangle.
     * @param rymax is the second corner of the rectangle.
     * @param rzmax is the second corner of the rectangle.
     * @param type is the type of special computation to apply. If <code>null</code>, it
     *     is equivalent to {@link CrossingComputationType#STANDARD}.
     * @return the crossings.
     */
    @SuppressWarnings({"checkstyle:npathcomplexity", "checkstyle:cyclomaticcomplexity", "checkstyle:parameternumber"})
	static int computeCrossingsFromRect(
			int crossings,
			PathIterator3ai<?> pi,
			int rxmin, int rymin, int rzmin,
			int rxmax, int rymax, int rzmax,
			CrossingComputationType type) {

	    assert pi != null : "Iterator must not be null"; //$NON-NLS-1$

	    // Copied from AWT API
	    if (!pi.hasNext()) {
            return 0;
        }

		PathElement3ai pathElement = pi.next();

		if (pathElement.getType() != PathElementType.MOVE_TO) {
			throw new IllegalArgumentException("missing initial moveto in path definition"); //$NON-NLS-1$
		}

		int numCrossings = crossings;
		int endx;
		int endy;
		int endz;
		int movx = pathElement.getToX();
		int curx = movx;
		int movy = pathElement.getToY();
		int cury = movy;
		int movz = pathElement.getToZ();
		int curz = movz;

		while (pi.hasNext()) {
			pathElement = pi.next();
			switch (pathElement.getType()) {
			case MOVE_TO:
			    // Count should always be a multiple of 2 here.
			    // assert((crossings & 1) != 0);
			    movx = pathElement.getToX();
			    curx = movx;
		        movy = pathElement.getToY();
		        cury = movy;
		        movz = pathElement.getToZ();
		        curz = movz;
				break;
			case LINE_TO:
				endx = pathElement.getToX();
				endy = pathElement.getToY();
				endz = pathElement.getToZ();
				numCrossings = Segment3ai.computeCrossingsFromRect(
						numCrossings,
						rxmin, rymin, rzmin,
						rxmax, rymax, rzmax,
						curx, cury, curz,
						endx, endy, endz);
                if (numCrossings == MathConstants.SHAPE_INTERSECTS) {
					return MathConstants.SHAPE_INTERSECTS;
				}
				curx = endx;
				cury = endy;
				curz = endz;
				break;
			case QUAD_TO:
				endx = pathElement.getToX();
				endy = pathElement.getToY();
				endz = pathElement.getToZ();
				Path3ai<?, ?, ?, ?, ?, ?> curve = pi.getGeomFactory().newPath(pi.getWindingRule());
				curve.moveTo(pathElement.getFromX(), pathElement.getFromY(), pathElement.getFromZ());
				curve.quadTo(pathElement.getCtrlX1(), pathElement.getCtrlY1(), pathElement.getCtrlZ1(), endx, endy, endz);
				numCrossings = computeCrossingsFromRect(
						numCrossings,
						curve.getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO),
						rxmin, rymin, rzmin, rxmax, rymax, rzmax,
						CrossingComputationType.STANDARD);
                if (numCrossings == MathConstants.SHAPE_INTERSECTS) {
					return MathConstants.SHAPE_INTERSECTS;
				}
				curx = endx;
				cury = endy;
				curz = endz;
				break;
			case CURVE_TO:
				endx = pathElement.getToX();
				endy = pathElement.getToY();
				endz = pathElement.getToZ();
				curve = pi.getGeomFactory().newPath(pi.getWindingRule());
				curve.moveTo(pathElement.getFromX(), pathElement.getFromY(), pathElement.getFromZ());
                curve.curveTo(pathElement.getCtrlX1(), pathElement.getCtrlY1(), pathElement.getCtrlZ1(), pathElement.getCtrlX2(),
                        pathElement.getCtrlY2(), pathElement.getCtrlZ2(), endx, endy, endz);
				numCrossings = computeCrossingsFromRect(
						numCrossings,
						curve.getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO),
						rxmin, rymin, rzmin, rxmax, rymax, rzmax,
						CrossingComputationType.STANDARD);
                if (numCrossings == MathConstants.SHAPE_INTERSECTS) {
					return MathConstants.SHAPE_INTERSECTS;
				}
				curx = endx;
				cury = endy;
				curz = endz;
				break;
			case CLOSE:
				if (curx != movx || cury != movy || curz != movz) {
					numCrossings = Segment3ai.computeCrossingsFromRect(
							numCrossings,
							rxmin, rymin, rzmin,
							rxmax, rymax, rzmax,
							curx, cury, curz,
							movx, movy, movz);
				}
				// Stop as soon as possible
				if (numCrossings != 0) {
					return numCrossings;
				}
				curx = movx;
				cury = movy;
				curz = movz;
				// Count should always be a multiple of 2 here.
				// assert((crossings & 1) != 0);
				break;
			case ARC_TO:
			default:
			}
		}

        assert numCrossings != MathConstants.SHAPE_INTERSECTS;

		final boolean isOpen = (curx != movx) || (cury != movy) || (curz != movz);

		if (isOpen && type != null) {
			switch (type) {
			case AUTO_CLOSE:
				// Not closed
				numCrossings = Segment3ai.computeCrossingsFromRect(
						numCrossings,
						rxmin, rymin, rzmin,
						rxmax, rymax, rzmax,
						curx, cury, curz,
						movx, movy, movz);
				break;
			case SIMPLE_INTERSECTION_WHEN_NOT_POLYGON:
				// Assume that when is the path is open, only
				// SHAPE_INTERSECTS may be return
				numCrossings = 0;
				break;
			case STANDARD:
			default:
				break;
			}
		}

		return numCrossings;
	}

	/**
	 * Tests if the interior of the specified {@link PathIterator3ai}
	 * intersects the interior of a specified set of rectangular
	 * coordinates.
	 *
	 * <p>This method provides a basic facility for implementors of
	 * the {@link Shape3ai} interface to implement support for the
	 * {@code intersects()} method.
	 *
	 * <p>This method object may conservatively return true in
	 * cases where the specified rectangular area intersects a
	 * segment of the path, but that segment does not represent a
	 * boundary between the interior and exterior of the path.
	 * Such a case may occur if some set of segments of the
	 * path are retraced in the reverse direction such that the
	 * two sets of segments cancel each other out without any
	 * interior area between them.
	 * To determine whether segments represent true boundaries of
	 * the interior of the path would require extensive calculations
	 * involving all of the segments of the path and the winding
	 * rule and are thus beyond the scope of this implementation.
	 *
	 * @param pi the specified {@code PathIterator}
	 * @param x the specified X coordinate
	 * @param y the specified Y coordinate
	 * @param z the specified Z coordinate
	 * @param width the width of the specified rectangular coordinates
	 * @param height the height of the specified rectangular coordinates
	 * @param depth the depth of the specified rectangular coordinates
	 * @return {@code true} if the specified {@code PathIterator} and
	 *         the interior of the specified set of rectangular
	 *         coordinates intersect each other; {@code false} otherwise.
	 */
	static boolean intersects(PathIterator3ai<?> pi, int x, int y, int z, int width, int height, int depth) {
		assert pi != null : "Iterator must not be null"; //$NON-NLS-1$
		assert width >= 0 : "Rectangle width must be positive or zero."; //$NON-NLS-1$
		assert height >= 0 : "Rectangle height must be positive or zero"; //$NON-NLS-1$
		assert depth >= 0 : "Rectangle height must be positive or zero"; //$NON-NLS-1$

		if (width == 0 || height == 0 || depth == 0) {
			return false;
		}
		final int mask = pi.getWindingRule() == PathWindingRule.NON_ZERO ? -1 : 2;
        final int crossings = computeCrossingsFromRect(0, pi, x, y, z, x + width, y + height, z + depth,
                CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON);
        return crossings == MathConstants.SHAPE_INTERSECTS || (crossings & mask) != 0;
	}

	@Pure
	@Override
	default boolean intersects(Sphere3ai<?, ?, ?, ?, ?, ?> sphere) {
	    assert sphere != null : "Sphere must not be null"; //$NON-NLS-1$
	    final int mask = getWindingRule() == PathWindingRule.NON_ZERO ? -1 : 2;
	    final int crossings = computeCrossingsFromSphere(
	            0,
	            getPathIterator(),
	            sphere.getX(), sphere.getY(), sphere.getZ(), sphere.getRadius(),
	            CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON);
        return crossings == MathConstants.SHAPE_INTERSECTS || (crossings & mask) != 0;
	}

	@Pure
	@Override
	default boolean intersects(RectangularPrism3ai<?, ?, ?, ?, ?, ?> rectangularPrism) {
	    assert rectangularPrism != null : "RectangularPrism must be not null"; //$NON-NLS-1$
        return intersects(getPathIterator(), rectangularPrism.getMinX(), rectangularPrism.getMinY(), rectangularPrism.getMinZ(),
                rectangularPrism.getWidth(), rectangularPrism.getHeight(), rectangularPrism.getDepth());
	}

	@Pure
	@Override
	default boolean intersects(Segment3ai<?, ?, ?, ?, ?, ?> segment) {
	    assert segment != null : "Segment must not be null"; //$NON-NLS-1$
	    final int mask = getWindingRule() == PathWindingRule.NON_ZERO ? -1 : 2;
	    final int crossings = computeCrossingsFromSegment(
	            0,
	            getPathIterator(),
	            segment.getX1(), segment.getY1(), segment.getZ1(), segment.getX2(), segment.getY2(), segment.getZ2(),
	            CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON);
        return crossings == MathConstants.SHAPE_INTERSECTS || (crossings & mask) != 0;
	}

	@Override
	default boolean intersects(PathIterator3ai<?> iterator) {
	    assert iterator != null : "Iterator must not be null"; //$NON-NLS-1$
	    final int mask = getWindingRule() == PathWindingRule.NON_ZERO ? -1 : 2;
	    final int crossings = computeCrossingsFromPath(
	            0, iterator,
	            new BasicPathShadow3ai(this),
	            CrossingComputationType.SIMPLE_INTERSECTION_WHEN_NOT_POLYGON);
        return crossings == MathConstants.SHAPE_INTERSECTS || (crossings & mask) != 0;
	}

	@Pure
	@Override
	default boolean intersects(MultiShape3ai<?, ?, ?, ?, ?, ?, ?> multishape) {
	    assert multishape != null : "MultiShape must be not null"; //$NON-NLS-1$
	    return multishape.intersects(this);
	}

	/** Replies the point on the path that is closest to the given point.
	 *
	 * <p><strong>CAUTION:</strong> This function works only on path iterators
	 * that are replying polyline primitives, ie. if the
	 * {@link PathIterator3D#isPolyline()}of {@code pi} is replying
	 * <code>true</code>.
	 * {@link #getClosestPointTo(Point3D)} avoids this restriction.
	 *
	 * @param pi is the iterator on the elements of the path.
     * @param x x coordinate of the point.
     * @param y y coordinate of the point.
     * @param z z coordinate of the point.
	 * @param result the closest point on the shape; or the point itself
	 *     if it is inside the shape.
	 */
	@SuppressWarnings({"checkstyle:cyclomaticcomplexity", "checkstyle:npathcomplexity"})
	static void getClosestPointTo(PathIterator3ai<? extends PathElement3ai> pi, int x, int y, int z, Point3D<?, ?> result) {
		assert pi != null : "Iterator must not be null"; //$NON-NLS-1$

		int bestManhantanDist = Integer.MAX_VALUE;
		int bestLinfinvDist = Integer.MAX_VALUE;
		Point3D<?, ?> candidate;
		PathElement3ai pe;

		final int mask = pi.getWindingRule() == PathWindingRule.NON_ZERO ? -1 : 1;
		int crossings = 0;
		boolean isClosed = false;
		int moveX = 0;
		int moveY = 0;
		int moveZ = 0;
		int currentX = 0;
		int currentY = 0;
		int currentZ = 0;

		while (pi.hasNext()) {
			pe = pi.next();

			candidate = null;

			currentX = pe.getToX();
			currentY = pe.getToY();
			currentZ = pe.getToZ();

            switch (pe.getType()) {
			case MOVE_TO:
				moveX = pe.getToX();
				moveY = pe.getToY();
				moveZ = pe.getToZ();
				isClosed = false;
				break;
			case LINE_TO:
                isClosed = false;
                candidate = new InnerComputationPoint3ai();
                Segment3ai.computeClosestPointToPoint(pe.getFromX(), pe.getFromY(), pe.getFromZ(), pe.getToX(), pe.getToY(),
                        pe.getToZ(), x, y, z, candidate);
                if (crossings != MathConstants.SHAPE_INTERSECTS) {
					crossings = Segment3ai.computeCrossingsFromPoint(
							crossings,
							x, y, z,
							pe.getFromX(), pe.getFromY(), pe.getFromZ(), pe.getToX(), pe.getToY(), pe.getToZ());
				}
                break;
            case CLOSE:
				isClosed = true;
				if (!pe.isEmpty()) {
					candidate = new InnerComputationPoint3ai();
                    Segment3ai.computeClosestPointToPoint(pe.getFromX(), pe.getFromY(), pe.getFromZ(), pe.getToX(), pe.getToY(),
                            pe.getToZ(), x, y, z, candidate);
                    if (crossings != MathConstants.SHAPE_INTERSECTS) {
						crossings = Segment3ai.computeCrossingsFromPoint(
								crossings,
								x, y, z,
								pe.getFromX(), pe.getFromY(), pe.getFromZ(), pe.getToX(), pe.getToY(), pe.getToZ());
					}
				}
				break;
			case QUAD_TO:
			case CURVE_TO:
			case ARC_TO:
			default:
				throw new IllegalStateException();
			}

            if (candidate != null) {
                final int dx = Math.abs(x - candidate.ix());
                final int dy = Math.abs(y - candidate.iy());
                final int dz = Math.abs(z - candidate.iz());
                final int manhatanDist = dx + dy + dz;
				if (manhatanDist <= 0) {
					result.set(candidate);
					return;
				}
				final int linfinvDist = MathUtil.min(dx, dy, dz);
				if (manhatanDist < bestManhantanDist || (manhatanDist == bestManhantanDist && linfinvDist < bestLinfinvDist)) {
					bestManhantanDist = manhatanDist;
					bestLinfinvDist = linfinvDist;
					result.set(candidate);
				}
			}
		}

        if (!isClosed && crossings != MathConstants.SHAPE_INTERSECTS) {
			crossings = Segment3ai.computeCrossingsFromPoint(
					crossings,
					x, y, z,
					currentX, currentY, currentZ,
					moveX, moveY, moveZ);
		}

        if (crossings == MathConstants.SHAPE_INTERSECTS || (crossings & mask) != 0) {
			result.set(x, y, z);
		}
	}

    @Override
    default P getClosestPointTo(Point3D<?, ?> pt) {
        assert pt != null : "Point must not be null"; //$NON-NLS-1$
        final P point = getGeomFactory().newPoint();
        getClosestPointTo(getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO), pt.ix(), pt.iy(), pt.iz(), point);
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

	/** Replies the point on the path that is farthest to the given point.
	 *
	 * <p><strong>CAUTION:</strong> This function works only on path iterators
	 * that are replying polyline primitives, ie. if the
	 * {@link PathIterator3D#isPolyline()} of {@code pi} is replying
	 * <code>true</code>.
	 * {@link #getFarthestPointTo(Point3D)} avoids this restriction.
	 *
	 * @param pi is the iterator on the elements of the path.
     * @param x x coordinate of the point.
     * @param y y coordinate of the point.
	 * @param z z coordinate of the point.
	 * @param result the farthest point on the shape.
	 */
	static void getFarthestPointTo(PathIterator3ai<? extends PathElement3ai> pi, int x, int y, int z, Point3D<?, ?> result) {
		assert pi != null : "Iterator must not be null"; //$NON-NLS-1$

		int bestX = x;
		int bestY = y;
		int bestZ = z;
		int bestManhatanDist = Integer.MIN_VALUE;
		int bestLinfinvDist = Integer.MIN_VALUE;
		PathElement3ai pe;
		final Point3D<?, ?> point = new InnerComputationPoint3ai();

		while (pi.hasNext()) {
			pe = pi.next();

			boolean foundCandidate = false;
			final int candidateX;
			final int candidateY;
			final int candidateZ;

            switch (pe.getType()) {
			case MOVE_TO:
				foundCandidate = true;
				candidateX = pe.getToX();
				candidateY = pe.getToY();
				candidateZ = pe.getToZ();
				break;
			case LINE_TO:
			case CLOSE:
				Segment3ai.computeFarthestPointTo(
						pe.getFromX(), pe.getFromY(), pe.getFromZ(), pe.getToX(), pe.getToY(), pe.getToZ(),
						x, y, z, point);
				foundCandidate = true;
				candidateX = point.ix();
				candidateY = point.iy();
				candidateZ = point.iz();
				break;
			case QUAD_TO:
			case CURVE_TO:
			case ARC_TO:
			default:
				throw new IllegalStateException(
						pe.getType() == null ? null : pe.getType().toString());
			}

			if (foundCandidate) {
                final int dx = Math.abs(x - candidateX);
                final int dy = Math.abs(y - candidateY);
                final int dz = Math.abs(y - candidateY);
                final int manhatanDist = dx + dy + dz;
                final int linfinvDist = MathUtil.min(dx, dy, dz);
				if ((manhatanDist > bestManhatanDist) || (manhatanDist == bestManhatanDist && linfinvDist < bestLinfinvDist)) {
					bestManhatanDist = manhatanDist;
					bestLinfinvDist = linfinvDist;
					bestX = candidateX;
					bestY = candidateY;
					bestZ = candidateZ;
				}
			}
		}

		result.set(bestX, bestY, bestZ);
	}

    @Override
    default P getFarthestPointTo(Point3D<?, ?> pt) {
        assert pt != null : "Point must not be null"; //$NON-NLS-1$
        final P point = getGeomFactory().newPoint();
        getFarthestPointTo(getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO), pt.ix(), pt.iy(), pt.iz(), point);
        return point;
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
		return equalsToPathIterator(shape.getPathIterator());
	}

	/** Add the elements replied by the iterator into this path.
    *
    * @param iterator the iterator on the elements to add.
    */
	default void add(Iterator<? extends PathElement3ai> iterator) {
		assert iterator != null : "Iterator must not be null"; //$NON-NLS-1$
		PathElement3ai element;
		while (iterator.hasNext()) {
			element = iterator.next();
            switch (element.getType()) {
			case MOVE_TO:
				moveTo(element.getToX(), element.getToY(), element.getToZ());
				break;
			case LINE_TO:
				lineTo(element.getToX(), element.getToY(), element.getToZ());
				break;
			case QUAD_TO:
                quadTo(element.getCtrlX1(), element.getCtrlY1(), element.getCtrlZ1(), element.getToX(), element.getToY(),
                        element.getToZ());
				break;
			case CURVE_TO:
                curveTo(element.getCtrlX1(), element.getCtrlY1(), element.getCtrlZ1(), element.getCtrlX2(), element.getCtrlY2(),
                        element.getCtrlZ2(), element.getToX(), element.getToY(), element.getToZ());
				break;
			case CLOSE:
				closePath();
				break;
			case ARC_TO:
			default:
			}
		}
	}

	/** Set the path.
	 *
	 * @param path the path to copy.
	 */
	default void set(Path3ai<?, ?, ?, ?, ?, ?> path) {
		assert path != null : "Path must not be null"; //$NON-NLS-1$
		clear();
		add(path.getPathIterator());
	}

	/**
	 * Adds a point to the path by moving to the specified
	 * coordinates specified in double precision.
	 *
	 * @param x the specified X coordinate
	 * @param y the specified Y coordinate
	 * @param z the specified Y coordinate
	 */
	void moveTo(int x, int y, int z);

	@Override
	default void moveTo(Point3D<?, ?> position) {
		assert position != null : "Position must not be null"; //$NON-NLS-1$
		moveTo(position.ix(), position.iy(), position.iz());
	}

	/**
	 * Adds a point to the path by drawing a straight line from the
	 * current coordinates to the new specified coordinates
	 * specified in double precision.
	 *
	 * @param x the specified X coordinate
	 * @param y the specified Y coordinate
	 * @param z the specified Y coordinate
	 */
	void lineTo(int x, int y, int z);

	@Override
	default void lineTo(Point3D<?, ?> to) {
		assert to != null : "Position must not be null"; //$NON-NLS-1$
		lineTo(to.ix(), to.iy(), to.iz());
	}

	/**
	 * Adds a curved segment, defined by two new points, to the path by
	 * drawing a Quadratic curve that intersects both the current
	 * coordinates and the specified coordinates {@code (x2,y2)},
	 * using the specified point {@code (x1,y1)} as a quadratic
	 * parametric control point.
	 * All coordinates are specified in double precision.
	 *
	 * @param x1 the X coordinate of the quadratic control point
	 * @param y1 the Y coordinate of the quadratic control point
	 * @param z1 the Z coordinate of the quadratic control point
	 * @param x2 the X coordinate of the final end point
	 * @param y2 the Y coordinate of the final end point
	 * @param z2 the Z coordinate of the final end point
	 */
	void quadTo(int x1, int y1, int z1, int x2, int y2, int z2);

	@Override
	default void quadTo(Point3D<?, ?> ctrl, Point3D<?, ?> to) {
		assert ctrl != null : "Control point must not be null"; //$NON-NLS-1$
		assert to != null : "Destination point must not be null"; //$NON-NLS-1$
		quadTo(ctrl.ix(), ctrl.iy(), ctrl.iz(), to.ix(), to.iy(), to.iz());
	}

	/**
	 * Adds a curved segment, defined by three new points, to the path by
	 * drawing a B&eacute;zier curve that intersects both the current
	 * coordinates and the specified coordinates {@code (x3,y3)},
	 * using the specified points {@code (x1,y1)} and {@code (x2,y2)} as
	 * B&eacute;zier control points.
	 * All coordinates are specified in double precision.
	 *
	 * @param x1 the X coordinate of the first B&eacute;zier control point
	 * @param y1 the Y coordinate of the first B&eacute;zier control point
	 * @param z1 the Z coordinate of the first B&eacute;zier control point
	 * @param x2 the X coordinate of the second B&eacute;zier control point
	 * @param y2 the Y coordinate of the second B&eacute;zier control point
	 * @param z2 the Z coordinate of the second B&eacute;zier control point
	 * @param x3 the X coordinate of the final end point
	 * @param y3 the Y coordinate of the final end point
	 * @param z3 the Z coordinate of the final end point
	 */
	@SuppressWarnings("checkstyle:parameternumber")
	void curveTo(int x1, int y1, int z1,
			int x2, int y2, int z2,
			int x3, int y3, int z3);

	@Override
	default void curveTo(Point3D<?, ?> ctrl1, Point3D<?, ?> ctrl2, Point3D<?, ?> to) {
		assert ctrl1 != null : "First control point must not be null"; //$NON-NLS-1$
		assert ctrl2 != null : "Second control point must not be null"; //$NON-NLS-1$
		assert to != null : "Destination point must not be null"; //$NON-NLS-1$
		curveTo(ctrl1.ix(), ctrl1.iy(), ctrl1.iz(), ctrl2.ix(), ctrl2.iy(), ctrl2.iz(), to.ix(), to.iy(), to.iz());
	}

    @Pure
    @Override
    default double getDistanceSquared(Point3D<?, ?> point) {
		assert point != null : "Point must not be null"; //$NON-NLS-1$
		final Point3D<?, ?> c = getClosestPointTo(point);
		return c.getDistanceSquared(point);
	}

	@Pure
	@Override
	default double getDistanceL1(Point3D<?, ?> point) {
		assert point != null : "Point must not be null"; //$NON-NLS-1$
		final Point3D<?, ?> c = getClosestPointTo(point);
		return c.getDistanceL1(point);
	}

	@Pure
	@Override
	default double getDistanceLinf(Point3D<?, ?> point) {
		assert point != null : "Point must not be null"; //$NON-NLS-1$
		final Point3D<?, ?> c = getClosestPointTo(point);
		return c.getDistanceLinf(point);
	}

	@Override
	default double getLengthSquared() {
		if (isEmpty()) {
            return 0;
        }

		double length = 0;

		final PathIterator3ai<?> pi = getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO);

		PathElement3ai pathElement = pi.next();

		if (pathElement.getType() != PathElementType.MOVE_TO) {
			throw new IllegalArgumentException("missing initial moveto in path definition"); //$NON-NLS-1$
		}

		while (pi.hasNext()) {
			pathElement = pi.next();

			switch (pathElement.getType()) {
			case LINE_TO:
				length += Point3D.getDistanceSquaredPointPoint(
						pathElement.getFromX(), pathElement.getFromY(), pathElement.getFromZ(),
						pathElement.getToX(), pathElement.getToY(), pathElement.getToZ());
				break;
			case CLOSE:
				length += Point3D.getDistanceSquaredPointPoint(
						pathElement.getFromX(), pathElement.getFromY(), pathElement.getFromZ(),
						pathElement.getToX(), pathElement.getToY(), pathElement.getToZ());
				break;
			case QUAD_TO:
			case CURVE_TO:
				throw new IllegalStateException("Curve in path is not supported"); //$NON-NLS-1$
			case MOVE_TO:
			case ARC_TO:
			default:
			}

		}

		return length;
	}

	@Pure
	@Override
	default P getCurrentPoint() {
		return getGeomFactory().newPoint(getCurrentX(), getCurrentY(), getCurrentZ());
	}

	/** Replies the x coordinate of the last point in the path.
	 *
	 * @return the x coordinate of the last point in the path.
	 */
	@Pure
	int getCurrentX();

	/** Replies the x coordinate of the last point in the path.
	 *
	 * @return the x coordinate of the last point in the path.
	 */
	@Pure
	int getCurrentY();

	/** Replies the z coordinate of the last point in the path.
	 *
	 * @return the z coordinate of the last point in the path.
	 */
	@Pure
	int getCurrentZ();

	/** Replies the coordinate at the given index.
	 * The index is in [0;{@link #size()}*3).
	 *
	 * @param index the index
	 * @return the coordinate at the given index.
	 */
	@Pure
	int getCoordAt(int index);

	/** Change the coordinates of the last inserted point.
    *
    * @param x the new x coordinate of the last point.
    * @param y the new y coordinate of the last point.
    * @param z the new z coordinate of the last point.
    */
	void setLastPoint(int x, int y, int z);

	@Override
	default void setLastPoint(Point3D<?, ?> point) {
		assert point != null : "Point must not be null"; //$NON-NLS-1$
		setLastPoint(point.ix(), point.iy(), point.iz());
	}

	/** Transform the current path.
	 * This function changes the current path.
	 *
	 * @param transform is the affine transformation to apply.
	 * @see #createTransformedShape
	 */
	void transform(Transform3D transform);

	/** Remove the point with the given coordinates.
	 *
	 * <p>If the given coordinates do not match exactly a point in the path, nothing is removed.
	 *
	 * @param x the x coordinate of the point to remove.
	 * @param y the y coordinate of the point to remove.
	 * @param z the z coordinate of the point to remove.
	 * @return <code>true</code> if the point was removed; <code>false</code> otherwise.
	 */
	boolean remove(int x, int y, int z);

	@Pure
	@Override
	default PathIterator3ai<IE> getPathIterator(double flatness) {
		return new FlatteningPathIterator<>(this, getPathIterator(null), flatness, DEFAULT_FLATTENING_LIMIT);
	}

	@Pure
	@Override
	default PathIterator3ai<IE> getPathIterator(Transform3D transform) {
		if (transform == null || transform.isIdentity()) {
			return new PathPathIterator<>(this);
		}
		return new TransformedPathIterator<>(this, transform);
	}

	@Pure
	@Override
	default Iterator<P> getPointIterator() {
		final PathIterator3ai<IE> pathIterator = getPathIterator(MathConstants.SPLINE_APPROXIMATION_RATIO);
		return new PixelIterator<>(pathIterator, getGeomFactory());
	}

	@Pure
	@Override
	default Collection<P> toCollection() {
		return new PointCollection<>(this);
	}

	/** Private API for Path3ai.
	 *
	 * @author $Author: sgalland$
	 * @version $Name$ $Revision$ $Date$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 13.0
	 */
	final class PrivateAPI {

		private PrivateAPI() {
			//
		}

		/** Not documented.
		 *
		 * @param crossings the previous value of the crossing.
		 * @param rxmin not documented.
		 * @param rymin not documented.
		 * @param rzmin not documented.
		 * @param rxmax not documented.
		 * @param rymax not documented.
		 * @param rzmax not documented.
		 * @param curx not documented.
		 * @param cury not documented.
		 * @param curz not documented.
		 * @param movx not documented.
		 * @param movy not documented.
		 * @param movz not documented.
		 * @param intersectingBehavior not documented.
		 * @return the crossing.
		 */
		@Pure
		@SuppressWarnings("checkstyle:parameternumber")
		private static int crossingHelper(
				int crossings,
				int rxmin, int rymin, int rzmin,
				int rxmax, int rymax, int rzmax,
				int curx, int cury, int curz,
				int movx, int movy, int movz,
				boolean intersectingBehavior) {
			int crosses = Segment3ai.computeCrossingsFromRect(crossings,
					rxmin, rymin, rzmin,
					rxmax, rymax, rzmax,
					curx, cury, curz,
					movx, movy, movz);
            if (!intersectingBehavior && crosses == MathConstants.SHAPE_INTERSECTS) {
                final int x1 = rxmin + 1;
                final int x2 = rxmax - 1;
                final int y1 = rymin + 1;
                final int y2 = rymax - 1;
                final int z1 = rzmin + 1;
                final int z2 = rzmax - 1;
				crosses = Segment3ai.computeCrossingsFromRect(crossings,
						x1, y1, z1,
						x2, y2, z2,
						curx, cury, curz,
						movx, movy, movz);
			}
			return crosses;
		}

	}

	/** An abstract path iterator.
	 *
	 * @param <E> the type of the path elements.
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 13.0
	 */
	abstract class AbstractPathIterator<E extends PathElement3ai> implements PathIterator3ai<E> {

		/** Path.
		 */
		protected final Path3ai<?, ?, E, ?, ?, ?> path;

		/**
		 * @param path the path.
		 */
		public AbstractPathIterator(Path3ai<?, ?, E, ?, ?, ?> path) {
			assert path != null : "Path must not be null"; //$NON-NLS-1$
			this.path = path;
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}

		@Override
		public PathWindingRule getWindingRule() {
			return this.path.getWindingRule();
		}

		@Override
		public boolean isPolyline() {
			return this.path.isPolyline();
		}

		@Override
		public boolean isCurved() {
			return this.path.isCurved();
		}

		@Override
		public boolean isMultiParts() {
			return this.path.isMultiParts();
		}

		@Override
		public boolean isPolygon() {
			return this.path.isPolygon();
		}

		@Override
		public GeomFactory3ai<E, ?, ?, ?> getGeomFactory() {
			return this.path.getGeomFactory();
		}

	}

	/** A path iterator that does not transform the coordinates.
	 *
	 * @param <E> the type of the path elements.
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 13.0
	 */
	class PathPathIterator<E extends PathElement3ai> extends AbstractPathIterator<E> {

		private final Point3D<?, ?> p1;

		private final Point3D<?, ?> p2;

		private int typeIndex;

		private int coordIndex;

		private int movex;

		private int movey;

		private int movez;

		/**
		 * @param path the path.
		 */
		public PathPathIterator(Path3ai<?, ?, E, ?, ?, ?> path) {
			super(path);
			this.p1 = new InnerComputationPoint3ai();
			this.p2 = new InnerComputationPoint3ai();
		}

		@Override
		public PathIterator3ai<E> restartIterations() {
			return new PathPathIterator<>(this.path);
		}

		@Override
		public boolean hasNext() {
			return this.typeIndex < this.path.getPathElementCount();
		}

		@Override
		@SuppressWarnings("checkstyle:magicnumber")
		public E next() {
			final int type = this.typeIndex;
            if (this.typeIndex >= this.path.getPathElementCount()) {
				throw new NoSuchElementException();
			}
			E element = null;
            switch (this.path.getPathElementTypeAt(type)) {
			case MOVE_TO:
                if (this.coordIndex + 2 > (this.path.size() * 2)) {
					throw new NoSuchElementException();
				}
				this.movex = this.path.getCoordAt(this.coordIndex++);
				this.movey = this.path.getCoordAt(this.coordIndex++);
				this.movez = this.path.getCoordAt(this.coordIndex++);
				this.p2.set(this.movex, this.movey, this.movez);
				element = getGeomFactory().newMovePathElement(
						this.p2.ix(), this.p2.iy(), this.p2.iz());
				break;
			case LINE_TO:
                if (this.coordIndex + 2 > (this.path.size() * 2)) {
					throw new NoSuchElementException();
				}
				this.p1.set(this.p2);
				this.p2.set(
						this.path.getCoordAt(this.coordIndex++),
						this.path.getCoordAt(this.coordIndex++),
						this.path.getCoordAt(this.coordIndex++));
				element = getGeomFactory().newLinePathElement(
						this.p1.ix(), this.p1.iy(), this.p1.iz(),
						this.p2.ix(), this.p2.iy(), this.p2.iz());
				break;
			case QUAD_TO:
                if (this.coordIndex + 4 > (this.path.size() * 2)) {
                    throw new NoSuchElementException();
                }
                this.p1.set(this.p2);
                final int ctrlx = this.path.getCoordAt(this.coordIndex++);
                final int ctrly = this.path.getCoordAt(this.coordIndex++);
                final int ctrlz = this.path.getCoordAt(this.coordIndex++);
                this.p2.set(
						this.path.getCoordAt(this.coordIndex++),
						this.path.getCoordAt(this.coordIndex++),
						this.path.getCoordAt(this.coordIndex++));
                element = getGeomFactory().newCurvePathElement(
						this.p1.ix(), this.p1.iy(), this.p1.iz(),
						ctrlx, ctrly, ctrlz,
						this.p2.ix(), this.p2.iy(), this.p2.iz());
                break;
			case CURVE_TO:
                if (this.coordIndex + 6 > (this.path.size() * 2)) {
                    throw new NoSuchElementException();
                }
                this.p1.set(this.p2);
                final int ctrlx1 = this.path.getCoordAt(this.coordIndex++);
                final int ctrly1 = this.path.getCoordAt(this.coordIndex++);
                final int ctrlz1 = this.path.getCoordAt(this.coordIndex++);
                final int ctrlx2 = this.path.getCoordAt(this.coordIndex++);
                final int ctrly2 = this.path.getCoordAt(this.coordIndex++);
                final int ctrlz2 = this.path.getCoordAt(this.coordIndex++);
                this.p2.set(
						this.path.getCoordAt(this.coordIndex++),
						this.path.getCoordAt(this.coordIndex++),
						this.path.getCoordAt(this.coordIndex++));
                element = getGeomFactory().newCurvePathElement(
						this.p1.ix(), this.p1.iy(), this.p1.iz(),
						ctrlx1, ctrly1, ctrlz1,
						ctrlx2, ctrly2, ctrlz2,
						this.p2.ix(), this.p2.iy(), this.p2.iz());
                break;
			case CLOSE:
				this.p1.set(this.p2);
				this.p2.set(this.movex, this.movey, this.movez);
				element = getGeomFactory().newClosePathElement(
						this.p1.ix(), this.p1.iy(), this.p1.iz(),
						this.p2.ix(), this.p2.iy(), this.p2.iz());
				break;
			case ARC_TO:
			default:
			}
            if (element == null) {
                throw new NoSuchElementException();
            }

			++this.typeIndex;

			return element;
		}

	}

	/** A path iterator that transforms the coordinates.
	 *
	 * @param <E> the type of the path elements.
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 13.0
	 */
	class TransformedPathIterator<E extends PathElement3ai> extends AbstractPathIterator<E> {

		private final Transform3D transform;

		private final Point3D<?, ?> p1;

		private final Point3D<?, ?> p2;

		private final Point3D<?, ?> ptmp1;

		private final Point3D<?, ?> ptmp2;

		private int typeIndex;

		private int coordIndex;

		private int movex;

		private int movey;

		private int movez;

		/**
		 * @param path the path.
		 * @param transform the transformation.
		 */
		public TransformedPathIterator(Path3ai<?, ?, E, ?, ?, ?> path, Transform3D transform) {
			super(path);
			assert transform != null : "Transformation must not be null"; //$NON-NLS-1$
			this.transform = transform;
			this.p1 = new InnerComputationPoint3ai();
			this.p2 = new InnerComputationPoint3ai();
			this.ptmp1 = new InnerComputationPoint3ai();
			this.ptmp2 = new InnerComputationPoint3ai();
		}

		@Override
		public PathIterator3ai<E> restartIterations() {
			return new TransformedPathIterator<>(this.path, this.transform);
		}

		@Override
		public boolean hasNext() {
			return this.typeIndex < this.path.getPathElementCount();
		}

		@Override
        public E next() {
            if (this.typeIndex >= this.path.getPathElementCount()) {
                throw new NoSuchElementException();
			}
			E element = null;
            switch (this.path.getPathElementTypeAt(this.typeIndex++)) {
			case MOVE_TO:
				this.movex = this.path.getCoordAt(this.coordIndex++);
				this.movey = this.path.getCoordAt(this.coordIndex++);
				this.movez = this.path.getCoordAt(this.coordIndex++);
				this.p2.set(this.movex, this.movey, this.movez);
				this.transform.transform(this.p2);
				element = getGeomFactory().newMovePathElement(
						this.p2.ix(), this.p2.iy(), this.p2.iz());
				break;
			case LINE_TO:
				this.p1.set(this.p2);
				this.p2.set(
						this.path.getCoordAt(this.coordIndex++),
						this.path.getCoordAt(this.coordIndex++),
						this.path.getCoordAt(this.coordIndex++));
				this.transform.transform(this.p2);
				element = getGeomFactory().newLinePathElement(
						this.p1.ix(), this.p1.iy(), this.p1.iz(),
						this.p2.ix(), this.p2.iy(), this.p2.iz());
				break;
			case QUAD_TO:
                this.p1.set(this.p2);
                this.ptmp1.set(
						this.path.getCoordAt(this.coordIndex++),
						this.path.getCoordAt(this.coordIndex++),
						this.path.getCoordAt(this.coordIndex++));
                this.transform.transform(this.ptmp1);
                this.p2.set(
						this.path.getCoordAt(this.coordIndex++),
						this.path.getCoordAt(this.coordIndex++),
						this.path.getCoordAt(this.coordIndex++));
                this.transform.transform(this.p2);
                element = getGeomFactory().newCurvePathElement(
						this.p1.ix(), this.p1.iy(), this.p1.iz(),
						this.ptmp1.ix(), this.ptmp1.iy(), this.ptmp1.iz(),
						this.p2.ix(), this.p2.iy(), this.p2.iz());
                break;
			case CURVE_TO:
                this.p1.set(this.p2);
                this.ptmp1.set(
						this.path.getCoordAt(this.coordIndex++),
						this.path.getCoordAt(this.coordIndex++),
						this.path.getCoordAt(this.coordIndex++));
                this.transform.transform(this.ptmp1);
                this.ptmp2.set(
						this.path.getCoordAt(this.coordIndex++),
						this.path.getCoordAt(this.coordIndex++),
						this.path.getCoordAt(this.coordIndex++));
                this.transform.transform(this.ptmp2);
                this.p2.set(
						this.path.getCoordAt(this.coordIndex++),
						this.path.getCoordAt(this.coordIndex++),
						this.path.getCoordAt(this.coordIndex++));
                this.transform.transform(this.p2);
                element = getGeomFactory().newCurvePathElement(
						this.p1.ix(), this.p1.iy(), this.p1.iz(),
						this.ptmp1.ix(), this.ptmp1.iy(), this.ptmp1.iz(),
						this.ptmp2.ix(), this.ptmp2.iy(), this.ptmp2.iz(),
						this.p2.ix(), this.p2.iy(), this.p2.iz());
                break;
			case CLOSE:
				this.p1.set(this.p2);
				this.p2.set(this.movex, this.movey, this.movez);
				this.transform.transform(this.p2);
				element = getGeomFactory().newClosePathElement(
						this.p1.ix(), this.p1.iy(), this.p1.iz(),
						this.p2.ix(), this.p2.iy(), this.p1.iz());
				break;
			case ARC_TO:
			default:
			}
            if (element == null) {
                throw new NoSuchElementException();
            }
			return element;
		}

	}

	/** Iterator on the pixels of the path.
	 *
	 * @param <P> the type of the points.
	 * @param <V> the type of the vectors.
	 * @author $Author: sgalland$
	 * @author $Author: tpiotrow$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 13.0
	 */
	class PixelIterator<P extends Point3D<? super P, ? super V>,
			V extends Vector3D<? super V, ? super P>> implements Iterator<P> {

		private final PathIterator3ai<?> pathIterator;

		private final GeomFactory3ai<?, P, V, ?> factory;

		private Iterator<P> lineIterator;

		private P next;

		/**
		 * @param pi the iterator.
		 * @param factory the element factory.
		 */
		public PixelIterator(PathIterator3ai<?> pi, GeomFactory3ai<?, P, V, ?> factory) {
			assert pi != null : "Iterator must not be null"; //$NON-NLS-1$
			assert factory != null : "Factory must not be null"; //$NON-NLS-1$
			this.pathIterator = pi;
			this.factory = factory;
			searchNext();
		}

		private void searchNext() {
			final P old = this.next;
			this.next = null;
            while (this.pathIterator.hasNext() && (this.lineIterator == null || !this.lineIterator.hasNext())) {
				this.lineIterator = null;
				final PathElement3ai elt = this.pathIterator.next();
				if (elt.isDrawable()) {
                    switch (elt.getType()) {
					case LINE_TO:
					case CLOSE:
						final Segment3ai<?, ?, ?, P, V, ?> segment = this.factory.newSegment(
								elt.getFromX(), elt.getFromY(), elt.getFromZ(),
								elt.getToX(), elt.getToY(), elt.getToZ());
						this.lineIterator = segment.getPointIterator();
						break;
					case MOVE_TO:
					case CURVE_TO:
					case QUAD_TO:
					case ARC_TO:
					default:
						throw new IllegalStateException();
					}
				}
			}
            if (this.lineIterator != null && this.lineIterator.hasNext()) {
				this.next = this.lineIterator.next();
				while (this.next.equals(old)) {
					this.next = this.lineIterator.next();
				}
			}
		}

		@Override
		public boolean hasNext() {
            return this.next != null;
		}

		@Override
		public P next() {
			final P n = this.next;
            if (n == null) {
                throw new NoSuchElementException();
            }
			searchNext();
			return n;
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}

	}

	/** An collection of the points of the path.
	 *
	 * @param <P> the type of the points.
	 * @param <V> the type of the vectors.
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 13.0
	 */
	class PointCollection<P extends Point3D<? super P, ? super V>,
			V extends Vector3D<? super V, ? super P>> implements Collection<P> {

		private final Path3ai<?, ?, ?, P, V, ?> path;

		/**
		 * @param path the path from which the points are extracted.
		 */
		public PointCollection(Path3ai<?, ?, ?, P, V, ?> path) {
			assert path != null : "Path must not be null"; //$NON-NLS-1$
			this.path = path;
		}

		@Override
		public int size() {
			return this.path.size();
		}

		@Override
		public boolean isEmpty() {
            return this.path.size() <= 0;
		}

		@Override
		public boolean contains(Object obj) {
			if (obj instanceof Point3D) {
				return this.path.contains((Point3D<?, ?>) obj);
			}
			return false;
		}

		@Override
		public Iterator<P> iterator() {
			return new PointIterator<>(this.path);
		}

		@Override
		public Object[] toArray() {
			return this.path.toPointArray();
		}

		@SuppressWarnings("unchecked")
		@Override
		public <T> T[] toArray(T[] array) {
			assert array != null : "Array must not be null"; //$NON-NLS-1$
			final Iterator<P> iterator = new PointIterator<>(this.path);
            for (int i = 0; i < array.length && iterator.hasNext(); ++i) {
                array[i] = (T) iterator.next();
			}
			return array;
		}

		@Override
		public boolean add(P element) {
			if (element != null) {
                if (this.path.size() == 0) {
					this.path.moveTo(element.ix(), element.iy(), element.iz());
				} else {
					this.path.lineTo(element.ix(), element.iy(), element.iz());
				}
				return true;
			}
			return false;
		}

		@Override
		public boolean remove(Object obj) {
			if (obj instanceof Point3D) {
				final Point3D<?, ?> p = (Point3D<?, ?>) obj;
				return this.path.remove(p.ix(), p.iy(), p.iz());
			}
			return false;
		}

		@Override
		public boolean containsAll(Collection<?> collection) {
			assert collection != null : "Collection must not be null"; //$NON-NLS-1$
            for (final Object obj : collection) {
				if ((!(obj instanceof Point3D))
                        || (!this.path.contains((Point3D<?, ?>) obj))) {
					return false;
				}
			}
			return true;
		}

		@Override
		public boolean addAll(Collection<? extends P> collection) {
			assert collection != null : "Collection must not be null"; //$NON-NLS-1$
			boolean changed = false;
            for (final P pts : collection) {
				if (add(pts)) {
					changed = true;
				}
			}
			return changed;
		}

		@Override
		public boolean removeAll(Collection<?> collection) {
			assert collection != null : "Collection must not be null"; //$NON-NLS-1$
			boolean changed = false;
            for (final Object obj : collection) {
				if (obj instanceof Point3D) {
					final Point3D<?, ?> pts = (Point3D<?, ?>) obj;
					if (this.path.remove(pts.ix(), pts.iy(), pts.iz())) {
						changed = true;
					}
				}
			}
			return changed;
		}

		@Override
		public boolean retainAll(Collection<?> collection) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void clear() {
			this.path.clear();
		}

	}

	/** Iterator on the points of the path.
	 *
	 * @param <P> the type of the points.
	 * @param <V> the type of the vectors.
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 13.0
	 */
	class PointIterator<P extends Point3D<? super P, ? super V>,
			V extends Vector3D<? super V, ? super P>> implements Iterator<P> {

		private final Path3ai<?, ?, ?, P, V, ?> path;

		private int index;

		private P lastReplied;

		/**
		 * @param path the path to iterate on.
		 */
		public PointIterator(Path3ai<?, ?, ?, P, V, ?> path) {
			assert path != null : "Path must not be null"; //$NON-NLS-1$
			this.path = path;
		}

		@Override
		public boolean hasNext() {
			return this.index < this.path.size();
		}

		@Override
		public P next() {
			try {
				this.lastReplied = this.path.getPointAt(this.index++);
				return this.lastReplied;
            } catch (Throwable exception) {
				throw new NoSuchElementException();
			}
		}

		@Override
		public void remove() {
			final Point3D<?, ?> p = this.lastReplied;
			this.lastReplied = null;
            if (p == null) {
                throw new NoSuchElementException();
            }
			this.path.remove(p.ix(), p.iy(), p.iz());
		}

	}

	/** A path iterator that is flattening the path.
	 * This iterator was copied from AWT FlatteningPathIterator.
	 *
	 * @param <E> the type of the path elements.
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 13.0
	 */
	// TODO : integrate z coordinate
	@SuppressWarnings("checkstyle:magicnumber")
	class FlatteningPathIterator<E extends PathElement3ai> implements PathIterator3ai<E> {

		/** Path.
		 */
		private final Path3ai<?, ?, E, ?, ?, ?> path;

		/** The source iterator.
		 */
		private final PathIterator3ai<? extends E> pathIterator;

		/**
		 * Square of the flatness parameter for testing against squared lengths.
		 */
		private final double squaredFlatness;

		/**
		 * Maximum number of recursion levels.
		 */
		private final int limit;

		/** The recursion level at which each curve being held in storage was generated.
		 */
		private int[] levels;

		/** The cache of interpolated coords.
		 * Note that this must be long enough
		 * to store a full cubic segment and
		 * a relative cubic segment to avoid
		 * aliasing when copying the coords
		 * of a curve to the end of the array.
		 * This is also serendipitously equal
		 * to the size of a full quad segment
		 * and 2 relative quad segments.
		 */
		private double[] hold = new double[14];

		/** The index of the last curve segment being held for interpolation.
		 */
		private int holdEnd;

		/**
		 * The index of the curve segment that was last interpolated.  This
		 * is the curve segment ready to be returned in the next call to
		 * next().
		 */
		private int holdIndex;

		/** The ending x of the last segment.
		 */
		private double currentX;

		/** The ending y of the last segment.
		 */
		private double currentY;

		/** The ending y of the last segment.
		 */
		private double currentZ;

		/** The x of the last move segment.
		 */
		private double moveX;

		/** The y of the last move segment.
		 */
		private double moveY;

		/** The y of the last move segment.
		 */
		private double moveZ;

		/** The index of the entry in the
		 * levels array of the curve segment
		 * at the holdIndex.
		 */
		private int levelIndex;

		/** True when iteration is done.
		 */
		private boolean done;

		/** The type of the path element.
		 */
		private PathElementType holdType;

		/** The x of the last move segment replied by next.
		 */
		private int lastNextX;

		/** The y of the last move segment replied by next.
		 */
		private int lastNextY;

		/** The z of the last move segment replied by next.
		 */
		private int lastNextZ;

		/**
		 * @param path is the path.
		 * @param pathIterator is the path iterator that may be used to initialize the path.
		 * @param flatness the maximum allowable distance between the
		 *     control points and the flattened curve
		 * @param limit the maximum number of recursive subdivisions
		 *     allowed for any curved segment
		 */
		public FlatteningPathIterator(Path3ai<?, ?, E, ?, ?, ?> path, PathIterator3ai<? extends E> pathIterator,
				double flatness, int limit) {
			assert path != null : "Path must not be null"; //$NON-NLS-1$
			assert pathIterator != null : "Iterator must not be null"; //$NON-NLS-1$
			assert flatness > 0f : "Flatness factor must be positive."; //$NON-NLS-1$
			assert limit >= 0 : "Number of recursive subdivisions must be positive or zero."; //$NON-NLS-1$
			this.path = path;
			this.pathIterator = pathIterator;
			this.squaredFlatness = flatness * flatness;
			this.limit = limit;
			this.levels = new int[limit + 1];
			searchNext(true);
		}

		@Override
		public PathIterator3ai<E> restartIterations() {
			return new FlatteningPathIterator<>(this.path, this.pathIterator.restartIterations(),
					Math.sqrt(this.squaredFlatness), this.limit);
		}

		/**
		 * Ensures that the hold array can hold up to (want) more values.
		 * It is currently holding (hold.length - holdIndex) values.
		 */
		private void ensureHoldCapacity(int want) {
			if (this.holdIndex - want < 0) {
				final int have = this.hold.length - this.holdIndex;
				final int newsize = this.hold.length + GROW_SIZE;
				final double[] newhold = new double[newsize];
				System.arraycopy(this.hold, this.holdIndex,
						newhold, this.holdIndex + GROW_SIZE,
						have);
				this.hold = newhold;
				this.holdIndex += GROW_SIZE;
				this.holdEnd += GROW_SIZE;
			}
		}

		/**
		 * Returns the square of the flatness, or maximum distance of a
		 * control point from the line connecting the end points, of the
		 * quadratic curve specified by the control points stored in the
		 * indicated array at the indicated index.
		 * @param coords an array containing coordinate values
		 * @param offset the index into <code>coords</code> from which to
		 *          to start getting the values from the array
		 * @return the flatness of the quadratic curve that is defined by the
		 *          values in the specified array at the specified index.
		 */
		// TODO : validate indexes
		private static double getQuadSquaredFlatness(double[] coords, int offset) {
			return Segment3afp.computeDistanceSquaredLinePoint(
					coords[offset + 0], coords[offset + 1], coords[offset + 2],
					coords[offset + 6], coords[offset + 7], coords[offset + 8],
					coords[offset + 3], coords[offset + 4], coords[offset + 5]);
		}

		/**
         * Subdivides the quadratic curve specified by the coordinates stored in the <code>src</code> array at indices
         * <code>srcoff</code> through <code>srcoff</code>&nbsp;+&nbsp;5 and stores the resulting two subdivided curves into the
         * two result arrays at the corresponding indices. Either or both of the <code>left</code> and <code>right</code> arrays
         * can be <code>null</code> or a reference to the same array and offset as the <code>src</code> array. Note that the last
         * point in the first subdivided curve is the same as the first point in the second subdivided curve. Thus, it is possible
         * to pass the same array for <code>left</code> and <code>right</code> and to use offsets such that to avoid allocating
         * extra storage for this common point.
         *
         * @param src
         *            the array holding the coordinates for the source curve <code>rightoff</code> equals <code>leftoff</code> + 4
         *            in order
         * @param srcoff
         *            the offset into the array of the beginning of the the 6 source coordinates.
         * @param left
         *            the array for storing the coordinates for the first half of the subdivided curve.
         * @param leftoff
         *            the offset into the array of the beginning of the the 6 left coordinates.
         * @param right
         *            the array for storing the coordinates for the second half of the subdivided curve.
         * @param rightoff
         *            the offset into the array of the beginning of the the 6 right coordinates.
         */
		// TODO : integrate z coordinate
		private static void subdivideQuad(double[] src, int srcoff,
				double[] left, int leftoff,
				double[] right, int rightoff) {
			double x1 = src[srcoff + 0];
			double y1 = src[srcoff + 1];
			double x2 = src[srcoff + 4];
			double y2 = src[srcoff + 5];
			if (left != null) {
				left[leftoff + 0] = x1;
				left[leftoff + 1] = y1;
			}
			if (right != null) {
				right[rightoff + 4] = x2;
				right[rightoff + 5] = y2;
			}
			double ctrlx = src[srcoff + 2];
			double ctrly = src[srcoff + 3];
			x1 = (x1 + ctrlx) / 2;
			y1 = (y1 + ctrly) / 2;
			x2 = (x2 + ctrlx) / 2;
			y2 = (y2 + ctrly) / 2;
			ctrlx = (x1 + x2) / 2;
			ctrly = (y1 + y2) / 2;
			if (left != null) {
				left[leftoff + 2] = x1;
				left[leftoff + 3] = y1;
				left[leftoff + 4] = ctrlx;
				left[leftoff + 5] = ctrly;
			}
			if (right != null) {
				right[rightoff + 0] = ctrlx;
				right[rightoff + 1] = ctrly;
				right[rightoff + 2] = x2;
				right[rightoff + 3] = y2;
			}
		}

		/**
		 * Returns the square of the flatness of the cubic curve specified
		 * by the control points stored in the indicated array at the
		 * indicated index. The flatness is the maximum distance
		 * of a control point from the line connecting the end points.
		 * @param coords an array containing coordinates
		 * @param offset the index of <code>coords</code> from which to begin
		 *          getting the end points and control points of the curve
		 * @return the square of the flatness of the <code>CubicCurve2D</code>
		 *          specified by the coordinates in <code>coords</code> at
		 *          the specified offset.
		 */
		// TODO : validate indexes
		private static double getCurveSquaredFlatness(double[] coords, int offset) {
			return Math.max(
					Segment3afp.computeDistanceSquaredSegmentPoint(
							coords[offset + 9],
							coords[offset + 10],
							coords[offset + 11],
							coords[offset + 3],
							coords[offset + 4],
							coords[offset + 5],
							coords[offset + 0],
							coords[offset + 1],
							coords[offset + 2]),
					Segment3afp.computeDistanceSquaredSegmentPoint(
							coords[offset + 9],
							coords[offset + 10],
							coords[offset + 11],
							coords[offset + 6],
							coords[offset + 7],
							coords[offset + 8],
							coords[offset + 0],
							coords[offset + 1],
							coords[offset + 2]));
		}

		/**
         * Subdivides the cubic curve specified by the coordinates stored in the <code>src</code> array at indices
         * <code>srcoff</code> through (<code>srcoff</code>&nbsp;+&nbsp;7) and stores the resulting two subdivided curves into the
         * two result arrays at the corresponding indices. Either or both of the <code>left</code> and <code>right</code> arrays
         * may be <code>null</code> or a reference to the same array as the <code>src</code> array. Note that the last point in
         * the first subdivided curve is the same as the first point in the second subdivided curve. Thus, it is possible to pass
         * the same array for <code>left</code> and <code>right</code> and to use offsets, such as <code>rightoff</code> equals (
         * <code>leftoff</code> + 6), in order to avoid allocating extra storage for this common point.
         *
         * @param src
         *            the array holding the coordinates for the source curve
         * @param srcoff
         *            the offset into the array of the beginning of the the 6 source coordinates.
         * @param left
         *            the array for storing the coordinates for the first half of the subdivided curve.
         * @param leftoff
         *            the offset into the array of the beginning of the the 6 left coordinates.
         * @param right
         *            the array for storing the coordinates for the second half of the subdivided curve.
         * @param rightoff
         *            the offset into the array of the beginning of the the 6 right coordinates.
         */
		// TODO : integrate z coordinate
		private static void subdivideCurve(
				double[] src, int srcoff,
				double[] left, int leftoff,
				double[] right, int rightoff) {
			double x1 = src[srcoff + 0];
			double y1 = src[srcoff + 1];
			double x2 = src[srcoff + 6];
			double y2 = src[srcoff + 7];
			if (left != null) {
				left[leftoff + 0] = x1;
				left[leftoff + 1] = y1;
			}
			if (right != null) {
				right[rightoff + 6] = x2;
				right[rightoff + 7] = y2;
			}
			double ctrlx1 = src[srcoff + 2];
			x1 = (x1 + ctrlx1) / 2f;
			double ctrly1 = src[srcoff + 3];
			y1 = (y1 + ctrly1) / 2f;
			double ctrlx2 = src[srcoff + 4];
			x2 = (x2 + ctrlx2) / 2f;
			double ctrly2 = src[srcoff + 5];
			y2 = (y2 + ctrly2) / 2f;
			double centerx = (ctrlx1 + ctrlx2) / 2f;
			double centery = (ctrly1 + ctrly2) / 2f;
			ctrlx1 = (x1 + centerx) / 2f;
			ctrly1 = (y1 + centery) / 2f;
			ctrlx2 = (x2 + centerx) / 2f;
			ctrly2 = (y2 + centery) / 2f;
			centerx = (ctrlx1 + ctrlx2) / 2f;
			centery = (ctrly1 + ctrly2) / 2f;
			if (left != null) {
				left[leftoff + 2] = x1;
				left[leftoff + 3] = y1;
				left[leftoff + 4] = ctrlx1;
				left[leftoff + 5] = ctrly1;
				left[leftoff + 6] = centerx;
				left[leftoff + 7] = centery;
			}
			if (right != null) {
				right[rightoff + 0] = centerx;
				right[rightoff + 1] = centery;
				right[rightoff + 2] = ctrlx2;
				right[rightoff + 3] = ctrly2;
				right[rightoff + 4] = x2;
				right[rightoff + 5] = y2;
			}
		}

		private void searchNext(boolean isFirst) {
			do {
				flattening();
			}
			while (!this.done && !isFirst && isSame());
		}

		private boolean isSame() {
			final PathElementType type = this.holdType;
			final int x;
			final int y;
			final int z;
            if (type == PathElementType.CLOSE) {
				x = (int) Math.round(this.moveX);
				y = (int) Math.round(this.moveY);
				z = (int) Math.round(this.moveZ);
			} else {
				x = (int) Math.round(this.hold[this.holdIndex + 0]);
				y = (int) Math.round(this.hold[this.holdIndex + 1]);
				z = (int) Math.round(this.hold[this.holdIndex + 2]);
			}
            return x == this.lastNextX && y == this.lastNextY && z == this.lastNextZ;
		}

		// TODO : integrate z coordinate
		@SuppressWarnings({"checkstyle:npathcomplexity", "checkstyle:cyclomaticcomplexity"})
		private void flattening() {
			int level;

			if (this.holdIndex >= this.holdEnd) {
				if (!this.pathIterator.hasNext()) {
					this.done = true;
					return;
				}
				final PathElement3ai pathElement = this.pathIterator.next();
				this.holdType = pathElement.getType();
				pathElement.toArray(this.hold);
				this.levelIndex = 0;
				this.levels[0] = 0;
			}

			switch (this.holdType) {
			case MOVE_TO:
			case LINE_TO:
				this.currentX = this.hold[0];
				this.currentY = this.hold[1];
				if (this.holdType == PathElementType.MOVE_TO) {
					this.moveX = this.currentX;
					this.moveY = this.currentY;
				}
				this.holdIndex = 0;
				this.holdEnd = 0;
				break;
			case CLOSE:
				this.currentX = this.moveX;
				this.currentY = this.moveY;
				this.holdIndex = 0;
				this.holdEnd = 0;
				break;
			case QUAD_TO:
				if (this.holdIndex >= this.holdEnd) {
					// Move the coordinates to the end of the array.
					this.holdIndex = this.hold.length - 6;
					this.holdEnd = this.hold.length - 2;
					this.hold[this.holdIndex + 0] = this.currentX;
					this.hold[this.holdIndex + 1] = this.currentY;
					this.hold[this.holdIndex + 2] = this.hold[0];
					this.hold[this.holdIndex + 3] = this.hold[1];
					this.currentX = this.hold[2];
					this.hold[this.holdIndex + 4] = this.currentX;
					this.currentY = this.hold[3];
					this.hold[this.holdIndex + 5] = this.currentY;
				}

				level = this.levels[this.levelIndex];
				while (level < this.limit) {
					if (getQuadSquaredFlatness(this.hold, this.holdIndex) < this.squaredFlatness) {
						break;
					}

					ensureHoldCapacity(4);
					subdivideQuad(
							this.hold, this.holdIndex,
							this.hold, this.holdIndex - 4,
							this.hold, this.holdIndex);
					this.holdIndex -= 4;

					// Now that we have subdivided, we have constructed
					// two curves of one depth lower than the original
					// curve.  One of those curves is in the place of
					// the former curve and one of them is in the next
					// set of held coordinate slots.  We now set both
					// curves level values to the next higher level.
					level++;
					this.levels[this.levelIndex] = level;
					this.levelIndex++;
					this.levels[this.levelIndex] = level;
				}

				// This curve segment is flat enough, or it is too deep
				// in recursion levels to try to flatten any more.  The
				// two coordinates at holdIndex+4 and holdIndex+5 now
				// contain the endpoint of the curve which can be the
				// endpoint of an approximating line segment.
				this.holdIndex += 4;
				this.levelIndex--;
				break;
			case CURVE_TO:
				if (this.holdIndex >= this.holdEnd) {
					// Move the coordinates to the end of the array.
					this.holdIndex = this.hold.length - 8;
					this.holdEnd = this.hold.length - 2;
					this.hold[this.holdIndex + 0] = this.currentX;
					this.hold[this.holdIndex + 1] = this.currentY;
					this.hold[this.holdIndex + 2] = this.hold[0];
					this.hold[this.holdIndex + 3] = this.hold[1];
					this.hold[this.holdIndex + 4] = this.hold[2];
					this.hold[this.holdIndex + 5] = this.hold[3];
					this.currentX = this.hold[4];
					this.hold[this.holdIndex + 6] = this.currentX;
					this.currentY = this.hold[5];
					this.hold[this.holdIndex + 7] = this.currentY;
				}

				level = this.levels[this.levelIndex];
				while (level < this.limit) {
					if (getCurveSquaredFlatness(this.hold, this.holdIndex) < this.squaredFlatness) {
						break;
					}

					ensureHoldCapacity(6);
					subdivideCurve(
							this.hold, this.holdIndex,
							this.hold, this.holdIndex - 6,
							this.hold, this.holdIndex);
					this.holdIndex -= 6;

					// Now that we have subdivided, we have constructed
					// two curves of one depth lower than the original
					// curve.  One of those curves is in the place of
					// the former curve and one of them is in the next
					// set of held coordinate slots.  We now set both
					// curves level values to the next higher level.
					level++;
					this.levels[this.levelIndex] = level;
					this.levelIndex++;
					this.levels[this.levelIndex] = level;
				}

				// This curve segment is flat enough, or it is too deep
				// in recursion levels to try to flatten any more.  The
				// two coordinates at holdIndex+6 and holdIndex+7 now
				// contain the endpoint of the curve which can be the
				// endpoint of an approximating line segment.
				this.holdIndex += 6;
				this.levelIndex--;
				break;
			case ARC_TO:
			default:
			}
		}

		@Override
		public boolean hasNext() {
			return !this.done;
		}

		@Override
		public E next() {
			if (this.done) {
				throw new NoSuchElementException("flattening iterator out of bounds"); //$NON-NLS-1$
			}

			final E element;
			final PathElementType type = this.holdType;
            if (type != PathElementType.CLOSE) {
				final int x = (int) Math.round(this.hold[this.holdIndex + 0]);
				final int y = (int) Math.round(this.hold[this.holdIndex + 1]);
				final int z = (int) Math.round(this.hold[this.holdIndex + 2]);
				if (type == PathElementType.MOVE_TO) {
					element = this.path.getGeomFactory().newMovePathElement(x, y, z);
				} else {
					element = this.path.getGeomFactory().newLinePathElement(
							this.lastNextX, this.lastNextY, this.lastNextZ,
							x, y, z);
				}
				this.lastNextX = x;
				this.lastNextY = y;
				this.lastNextY = z;
			} else {
				final int x = (int) Math.round(this.moveX);
				final int y = (int) Math.round(this.moveY);
				final int z = (int) Math.round(this.moveZ);
				element = this.path.getGeomFactory().newClosePathElement(
						this.lastNextX, this.lastNextY, this.lastNextZ,
						x, y, z);
				this.lastNextX = x;
				this.lastNextY = y;
				this.lastNextZ = z;
			}

			searchNext(false);

			return element;
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}

		@Override
		public PathWindingRule getWindingRule() {
			return this.path.getWindingRule();
		}

		@Override
		public boolean isPolyline() {
			return !isMultiParts() && !isPolygon();
		}

		@Override
		public boolean isCurved() {
			// Because the iterator flats the path, this is no curve inside.
			return false;
		}

		@Override
		public boolean isMultiParts() {
			return this.path.isMultiParts();
		}

		@Override
		public boolean isPolygon() {
			return this.path.isPolygon();
		}

		@Override
		public GeomFactory3ai<E, ?, ?, ?> getGeomFactory() {
			return this.path.getGeomFactory();
		}

	}

}
