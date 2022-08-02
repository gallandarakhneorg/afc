/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2022 The original authors, and other authors.
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

package org.arakhne.afc.math.geometry.d1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.eclipse.xtext.xbase.lib.Inline;
import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.MathUtil;
import org.arakhne.afc.math.geometry.coordinatesystem.CoordinateSystem2D;
import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.Transform2D;
import org.arakhne.afc.math.geometry.d2.Tuple2D;
import org.arakhne.afc.math.geometry.d2.d.Vector2d;
import org.arakhne.afc.vmutil.asserts.AssertMessages;

/**
 * Is represented internally as curviline translation and a shift translation.
 *
 * <p>The shift distance is positive or
 * negative according to the side vector of the current {@link CoordinateSystem2D}.
 *
 * <p>If a path was given, this transformation uses this list of the segments to translate
 * a point and change its associated segment if required. If no path was given to
 * this transformation, all the translation will be located on the current segment
 * space (even if the coordinates are too big or too low to be on the segment).
 * The transformed entities (<code>Point1D5</code>...) is supposed to be on
 * the segments of the path if it was supplied. The path is the list of the segments
 * which must follow the current segment of the entity.
 *
 * <p>Caution, the stored path is the reference passed to the functions, not a copy.
 *
 * <p>If a path was given, the translation is positive along the path. If a path
 * was not given, the translation will be directly applied to the coordinates.
 *
 * @param <S> is the type of the segments supported by this transformation.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @see Point1D
 */
public class Transform1D<S extends Segment1D<?, ?>> {

    /** Indicates if the matrix is identity.
     * If <code>null</code> the identity flag must be determined.
     */
    protected Boolean isIdentity;

    private List<S> path;

	private Direction1D firstSegmentDirection;

	private double curvilineTranslation;

	private double shiftTranslation;

	/**
	 * Constructs a new Transform1D object and sets it to the identity.
	 */
	public Transform1D() {
		setIdentity();
	}

	/**
	 * Constructs a new Transform1D object and initializes it from the
	 * specified transform.
	 *
	 * @param transform the transformation to copy.
	 */
	public Transform1D(Transform1D<? extends S> transform) {
		assert transform != null : AssertMessages.notNullParameter();
		final List<? extends S> path = transform.getPath();
		this.path = path == null || path.isEmpty() ? null : new ArrayList<>(path);
		this.firstSegmentDirection = transform.firstSegmentDirection;
		this.curvilineTranslation = transform.curvilineTranslation;
		this.shiftTranslation = transform.shiftTranslation;
	}

	/**
	 * Constructs a new Transform1D object and initializes it from the
	 * specified parameters.
	 *
	 * @param curvilineMove is the distance along the segments.
	 * @param shiftMove is the distance to move on the left or the right
	 */
	public Transform1D(double curvilineMove, double shiftMove) {
		this.path = null;
		this.firstSegmentDirection = detectFirstSegmentDirection(null);
		this.curvilineTranslation = curvilineMove;
		this.shiftTranslation = shiftMove;
	}

	/**
	 * Constructs a new Transform1D object and initializes it from the
	 * specified parameters.
	 *
	 * <p>If the given <var>path</var> contains only one segment,
	 * the transformation will follow the segment's direction.
	 *
	 * @param path is the path to use.
	 * @param curvilineMove is the distance along the segments.
	 * @param shiftMove is the distance to move on the left or the right
	 */
	public Transform1D(List<? extends S> path, double curvilineMove, double shiftMove) {
		this.path = path == null || path.isEmpty() ? null : new ArrayList<>(path);
		this.firstSegmentDirection = detectFirstSegmentDirection(null);
		this.curvilineTranslation = curvilineMove;
		this.shiftTranslation = shiftMove;
	}

	/**
	 * Constructs a new Transform1D object and initializes it from the
	 * specified parameters.
	 *
	 * @param path is the path to use.
	 * @param direction is the direction to follow on the path if the path contains only one segment.
	 * @param curvilineMove is the distance along the segments.
	 * @param shiftMove is the distance to move on the left or the right
	 */
	public Transform1D(List<? extends S> path, Direction1D direction, double curvilineMove, double shiftMove) {
		this.path = path == null || path.isEmpty() ? null : new ArrayList<>(path);
		this.firstSegmentDirection = detectFirstSegmentDirection(direction);
		this.curvilineTranslation = curvilineMove;
		this.shiftTranslation = shiftMove;
	}

	/**
	 * Constructs a new Transform1D object and initializes it from the
	 * specified parameters.
	 *
	 * <p>If the given <var>path</var> contains only one segment,
	 * the transformation will follow the segment's direction.
	 *
	 * @param path is the path to use.
	 */
	public Transform1D(List<? extends S> path) {
		this.path = path == null || path.isEmpty() ? null : new ArrayList<>(path);
		this.firstSegmentDirection = detectFirstSegmentDirection(null);
		this.curvilineTranslation = 0.;
		this.shiftTranslation = 0.;
	}

	/**
	 * Constructs a new Transform1D object and initializes it from the
	 * specified parameters.
	 *
	 * @param path is the path to use.
	 * @param direction is the direction to follow on the path if the path contains only one segment.
	 */
	public Transform1D(List<? extends S> path, Direction1D direction) {
		this.path = path == null || path.isEmpty() ? null : new ArrayList<>(path);
		this.firstSegmentDirection = detectFirstSegmentDirection(null);
		this.curvilineTranslation = 0.;
		this.shiftTranslation = 0.;
	}

	private Direction1D detectFirstSegmentDirection(Direction1D defaultValue) {
		if (this.path != null && this.path.size() > 1) {
			int i = 1;
			S s1 = this.path.get(0);
			S s2 = this.path.get(i);
			boolean eq = s1.equals(s2);
			while (eq && i < (this.path.size() - 1)) {
				++i;
				s1 = s2;
				s2 = this.path.get(i);
				eq = s1.equals(s2);
			}
			if (!eq) {
				boolean sgDir = s1.isLastPointConnectedTo(s2);
				if ((i % 2) == 0) {
					sgDir = !sgDir;
				}
				return sgDir ? Direction1D.SEGMENT_DIRECTION : Direction1D.REVERTED_DIRECTION;
			}
		}
		if (defaultValue == null) {
			return Direction1D.SEGMENT_DIRECTION;
		}
		return defaultValue;
	}

	/** Replies if this transformation has a path.
	 *
	 * @return <code>true</code> if this transformation has a path,
	 *     otherwise <code>false</code>.
	 */
	@Pure
	public boolean hasPath() {
		return this.path != null && !this.path.isEmpty();
	}

	/** Replies the path used by this transformation.
	 *
	 * @return the path.
	 */
	@Pure
	public List<S> getPath() {
		if (this.path == null) {
			return Collections.emptyList();
		}
		return Collections.unmodifiableList(this.path);
	}

	/** Replies the direction to follow along the first segment.
	 *
	 * @return the direction in case of a one-segment path.
	 */
	@Pure
	public Direction1D getFirstSegmentPathDirection() {
		return this.firstSegmentDirection;
	}

	/** Set the path used by this transformation.
	 *
	 * <p>If the given <var>path</var> contains only one segment,
	 * the transformation will follow the segment's direction.
	 *
	 * @param path is the new path
	 */
	@Inline(value = "setPath($1, null)")
	public void setPath(List<? extends S> path) {
		setPath(path, null);
	}

	/** Set the path used by this transformation.
	 *
	 * @param path is the new path
	 * @param direction is the direction to follow on the path if the path contains only one segment.
	 */
	public void setPath(List<? extends S> path, Direction1D direction) {
		this.path = path == null || path.isEmpty() ? null : new ArrayList<>(path);
		this.firstSegmentDirection = detectFirstSegmentDirection(direction);
	}

	/** Set this transformation as identify, ie all set to zero and no path.
	 */
	public void setIdentity() {
		this.path = null;
		this.firstSegmentDirection = detectFirstSegmentDirection(null);
		this.curvilineTranslation = 0.;
		this.shiftTranslation = 0.;
        this.isIdentity = Boolean.TRUE;
	}

	/** Replies if the transformation is the identity transformation.
	 *
	 * @return {@code true} if the transformation is identity.
	 */
	@Pure
	public boolean isIdentity() {
        if (this.isIdentity == null) {
            this.isIdentity = MathUtil.isEpsilonZero(this.curvilineTranslation)
                    && MathUtil.isEpsilonZero(this.curvilineTranslation);
        }
        return this.isIdentity.booleanValue();
	}

	/** Set the position.  This function does not change the path.
	 *
	 * @param curvilineCoord the curviline translation.
	 * @param shiftCoord the shifting translation.
	 */
	public void setTranslation(double curvilineCoord, double shiftCoord) {
		this.curvilineTranslation = curvilineCoord;
		this.shiftTranslation = shiftCoord;
        this.isIdentity = null;
	}

	/** Set the position.
	 *
	 * <p>If the given <var>path</var> contains only one segment,
	 * the transformation will follow the segment's direction.
	 *
	 * @param path the path to follow.
	 * @param curvilineCoord the curviline translation.
	 * @param shiftCoord the shifting translation.
	 */
	@Inline(value = "setTranslate($1, null, $2, $3)")
	public void setTranslation(List<? extends S> path, double curvilineCoord, double shiftCoord) {
		setTranslation(path, null, curvilineCoord, shiftCoord);
	}

	/** Set the position.
	 *
	 * @param path the path to follow.
	 * @param direction is the direction to follow on the path if the path contains only one segment.
	 * @param curvilineCoord the curviline translation.
	 * @param shiftCoord the shifting translation.
	 */
	public void setTranslation(List<? extends S> path, Direction1D direction, double curvilineCoord, double shiftCoord) {
		this.path = path == null || path.isEmpty() ? null : new ArrayList<>(path);
		this.firstSegmentDirection = detectFirstSegmentDirection(direction);
		this.curvilineTranslation = curvilineCoord;
		this.shiftTranslation = shiftCoord;
        this.isIdentity = null;
	}

	/** Set the position. This function does not change the path.
	 *
	 * @param position where <code>x</code> is the curviline coordinate and <code>y</code> is the shift coordinate.
	 */
	public void setTranslation(Tuple2D<?> position) {
		assert position != null : AssertMessages.notNullParameter();
		this.curvilineTranslation = position.getX();
		this.shiftTranslation = position.getY();
        this.isIdentity = null;
	}

	/** Set the position.
	 *
	 * <p>If the given <var>path</var> contains only one segment,
	 * the transformation will follow the segment's direction.
	 *
	 * @param path the path to follow.
	 * @param position where <code>x</code> is the curviline coordinate and <code>y</code> is the shift coordinate.
	 */
	@Inline(value = "setTranslation($1, null, $2)")
	public void setTranslation(List<? extends S> path, Tuple2D<?> position) {
		setTranslation(path, null, position);
	}

	/** Set the position.
	 *
	 * @param path the path to follow.
	 * @param direction is the direction to follow on the path if the path contains only one segment.
	 * @param position where <code>x</code> is the curviline coordinate and <code>y</code> is the shift coordinate.
	 */
	public void setTranslation(List<? extends S> path, Direction1D direction, Tuple2D<?> position) {
		assert position != null : AssertMessages.notNullParameter(3);
		this.path = path == null || path.isEmpty() ? null : new ArrayList<>(path);
		this.firstSegmentDirection = detectFirstSegmentDirection(direction);
		this.curvilineTranslation = position.getX();
		this.shiftTranslation = position.getY();
        this.isIdentity = null;
	}

	/** Translate the coordinates. This function does not change the path.
	 *
	 * @param curvilineCoord the curviline translation.
	 * @param shiftCoord the shifting translation.
	 */
	public void translate(double curvilineCoord, double shiftCoord) {
		this.curvilineTranslation += curvilineCoord;
		this.shiftTranslation += shiftCoord;
        this.isIdentity = null;
	}

	/** Translate.
	 *
	 * <p>If the given <var>path</var> contains only one segment,
	 * the transformation will follow the segment's direction.
	 *
	 * @param thePath the path to follow.
	 * @param curvilineCoord the curviline translation.
	 * @param shiftCoord the shifting translation.
	 */
	@Inline(value = "translate($1, null, $2, $3)")
	public void translate(List<? extends S> thePath, double curvilineCoord, double shiftCoord) {
		translate(thePath, null, curvilineCoord, shiftCoord);
	}

	/** Translate.
	 *
	 * @param thePath the path to follow.
	 * @param direction is the direction to follow on the path if the path contains only one segment.
	 * @param curvilineCoord the curviline translation.
	 * @param shiftCoord the shifting translation.
	 */
	public void translate(List<? extends S> thePath, Direction1D direction, double curvilineCoord, double shiftCoord) {
		this.path = thePath == null || thePath.isEmpty() ? null : new ArrayList<>(thePath);
		this.firstSegmentDirection = detectFirstSegmentDirection(direction);
		this.curvilineTranslation += curvilineCoord;
		this.shiftTranslation += shiftCoord;
        this.isIdentity = null;
	}

	/** Translate the coordinates. This function does not change the path.
	 *
	 * @param move where <code>x</code> is the curviline coordinate and <code>y</code> is the shift coordinate.
	 */
	public void translate(Tuple2D<?> move) {
		assert move != null : AssertMessages.notNullParameter();
		this.curvilineTranslation += move.getX();
		this.shiftTranslation += move.getY();
        this.isIdentity = null;
	}

	/** Translate.
	 *
	 * <p>If the given <var>path</var> contains only one segment,
	 * the transformation will follow the segment's direction.
	 *
	 * @param thePath the path to follow.
	 * @param move where <code>x</code> is the curviline coordinate and <code>y</code> is the shift coordinate.
	 */
	@Inline(value = "translate($1, null, $2)")
	public void translate(List<? extends S> thePath, Tuple2D<?> move) {
		translate(thePath, null, move);
	}

	/** Translate.
	 *
	 * @param thePath the path to follow.
	 * @param direction is the direction to follow on the path if the path contains only one segment.
	 * @param move where <code>x</code> is the curviline coordinate and <code>y</code> is the shift coordinate.
	 */
	public void translate(List<? extends S> thePath, Direction1D direction, Tuple2D<?> move) {
		assert move != null : AssertMessages.notNullParameter(2);
		this.path = thePath == null || thePath.isEmpty() ? null : new ArrayList<>(thePath);
		this.firstSegmentDirection = detectFirstSegmentDirection(direction);
		this.curvilineTranslation += move.getX();
		this.shiftTranslation += move.getY();
        this.isIdentity = null;
	}

	/** Replies the curviline transformation.
	 *
	 * @return the amount
	 */
	@Pure
	public double getCurvilineTransformation() {
		return this.curvilineTranslation;
	}

	/** Replies the shift transformation.
	 *
	 * @return the amount
	 */
	@Pure
	public double getShiftTransformation() {
		return this.shiftTranslation;
	}

	/** Transform the specified 1.5D point.
	 * If the point is on a segment from the Transform1D path, it will be transformed
	 * to follow the path. If the point is not on a segment from the Transform1D path,
	 * the curviline and shift transformations will be simply added.
	 *
	 * @param point the point to transform.
	 * @return the index of the segment on which the point lies in the segment path;
	 *     or <code>-1</code> if the segment of the point is not the first segment in the path.
	 */
	@Inline(value = "transform($1, null)")
	public int transform(Point1D<?, ?, ? super S> point) {
		return transform(point, null);
	}

	/** Transform the specified 1.5D point.
	 * If the point is on a segment from the Transform1D path, it will be transformed
	 * to follow the path. If the point is not on a segment from the Transform1D path,
	 * the curviline and shift transformations will be simply added.
	 *
	 * @param point the point to transform.
	 * @param appliedTranslation is set by this function with the really applied distances. The values are always positives.
	 * @return the index of the segment on which the point lies in the segment path;
	 *      or <code>-1</code> if the segment of the point is not the first segment in the path.
	 */
	@Pure
	@SuppressWarnings({"checkstyle:cyclomaticcomplexity", "checkstyle:nestedifdepth", "rawtypes", "unchecked"})
	public int transform(Point1D<?, ?, ? super S> point, Tuple2D<?> appliedTranslation) {
		assert point != null : AssertMessages.notNullParameter(0);
		assert point.getSegment() != null;

		int idx;
		double distanceToSegmentEnd;
		Direction1D direction;
		Segment1D<?, ?> previousSegment;
		double shift = point.getY();
		double curviline = point.getCurvilineCoordinate();
		Segment1D<?, ?> segment = point.getSegment();
		double distance = 0;

		// Ensure the path is valid
		final List<? extends Segment1D<?, ?>> rpath;
		if (this.path == null || this.path.isEmpty()) {
			rpath = Collections.singletonList(segment);
		} else {
			rpath = this.path;
		}

		// Test if the point is located on the the first segment
		if (!segment.equals(rpath.get(0))) {
			return -1;
		}

		// Change the shift.
		if (this.firstSegmentDirection.isSegmentDirection()) {
			shift += this.shiftTranslation;
		} else {
			shift -= this.shiftTranslation;
		}

		double distanceToMove = this.curvilineTranslation;
		if (distanceToMove >= 0.) {
			// Move foward, along the path.
			direction = this.firstSegmentDirection;
			idx = 0;
			do {
				if (direction.isSegmentDirection()) {
					distanceToSegmentEnd = segment.getLength() - curviline;
					if (distanceToSegmentEnd < distanceToMove) {
						distance += distanceToSegmentEnd;
						if ((idx + 1) < rpath.size()) {
							previousSegment = segment;
							++idx;
							segment = rpath.get(idx);
							distanceToMove -= distanceToSegmentEnd;
							if (segment.isLastPointConnectedTo(previousSegment) || previousSegment.equals(segment)) {
								shift = -shift;
								curviline = segment.getLength();
								direction = Direction1D.REVERTED_DIRECTION;
							} else {
								curviline = 0.;
							}
						} else {
							curviline = segment.getLength();
							distanceToMove = 0.;
						}
					} else {
						curviline += distanceToMove;
						distance += distanceToMove;
						distanceToMove = 0.;
					}
				} else {
					distanceToSegmentEnd = curviline;
					if (distanceToSegmentEnd < distanceToMove) {
						distance += distanceToSegmentEnd;
						if ((idx + 1) < rpath.size()) {
							++idx;
							previousSegment = segment;
							segment = rpath.get(idx);
							distanceToMove -= distanceToSegmentEnd;
							if (segment.isFirstPointConnectedTo(previousSegment)
									|| previousSegment.equals(segment)) {
								shift = -shift;
								curviline = 0.;
								direction = Direction1D.SEGMENT_DIRECTION;
							} else {
								curviline = segment.getLength();
							}
						} else {
							distanceToMove = 0.;
							curviline = 0.;
						}
					} else {
						curviline -= distanceToMove;
						distance += distanceToMove;
						distanceToMove = 0.;
					}
				}
			} while (distanceToMove > 0.);
		} else {
			// Move backward. This operation can be applied only of
			// the current segment.
			idx = 0;
			if (this.firstSegmentDirection.isSegmentDirection()) {
				// Caution: distanceToMove is negative
				distance = Math.min(curviline, -distanceToMove);
				curviline -= distance;
				if (curviline < 0.) {
					curviline = 0.;
				}
			} else {
				// Caution: distanceToMove is negative
				distance = Math.min(segment.getLength() - curviline, -distanceToMove);
				curviline += distance;
				if (curviline > segment.getLength()) {
					curviline = segment.getLength();
				}
			}
		}

		if (appliedTranslation != null) {
			appliedTranslation.set(
					distance,
					Math.abs(this.shiftTranslation));
		}

		final Point1D fake = point;
		fake.set(segment, curviline, shift);

		return idx;
	}

	/** Replies a 2D transformation that is corresponding to this transformation.
	 *
	 * @param segment is the segment.
	 * @return the 2D transformation or <code>null</code> if the segment could not be mapped
	 *     to 2D.
	 */
	@Pure
	public final Transform2D toTransform2D(Segment1D<?, ?> segment) {
		assert segment != null : AssertMessages.notNullParameter(0);
		final Point2D<?, ?> a = segment.getFirstPoint();
		if (a == null) {
			return null;
		}
		final Point2D<?, ?> b = segment.getLastPoint();
		if (b == null) {
			return null;
		}
		return toTransform2D(a.getX(), a.getY(), b.getX(), b.getY());
	}

	/** Replies a 2D transformation that is corresponding to this transformation.
	 *
	 * @param startPoint is the 2D position of the start point of the segment.
	 * @param endPoint is the 2D position of the end point of the segment
	 * @return the 2D transformation.
	 */
	@Pure
	public final Transform2D toTransform2D(Point2D<?, ?> startPoint, Point2D<?, ?> endPoint) {
		assert startPoint != null : AssertMessages.notNullParameter(0);
		assert endPoint != null : AssertMessages.notNullParameter(1);
		return toTransform2D(startPoint.getX(), startPoint.getY(), endPoint.getX(), endPoint.getY());
	}

	/** Replies a 2D transformation that is corresponding to this transformation.
	 *
	 * <p>The used coordinate system is replied by {@link CoordinateSystem2D#getDefaultCoordinateSystem()}.
	 *
	 * @param startX is the 2D x coordinate of the start point of the segment.
	 * @param startY is the 2D y coordinate of the start point of the segment.
	 * @param endX is the 2D x coordinate of the end point of the segment
	 * @param endY is the 2D y coordinate of the end point of the segment
	 * @return the 2D transformation.
	 */
	@Pure
	public final Transform2D toTransform2D(double startX, double startY, double endX, double endY) {
		final Transform2D trans = new Transform2D();

		final Vector2d direction = new Vector2d(endX - startX, endY - startY);

		final Vector2d shiftVector = direction.toOrthogonalVector();

		double c = this.curvilineTranslation;
		double j = this.shiftTranslation;

		if (!this.firstSegmentDirection.isSegmentDirection()) {
			c = -c;
			j = -j;
		}

		shiftVector.scale(j);

		direction.normalize();
		direction.scale(c);
		direction.add(shiftVector);

		trans.setTranslation(direction);

		return trans;
	}

	@Pure
	@Override
	public String toString() {
		return "[ " //$NON-NLS-1$
				+ this.curvilineTranslation + "; " //$NON-NLS-1$
				+ this.shiftTranslation + " ] on "  //$NON-NLS-1$
				+ this.path + "\n";  //$NON-NLS-1$
	}

	@Pure
	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj instanceof Transform1D<?>) {
			final Transform1D<?> t = (Transform1D<?>) obj;
			return this.firstSegmentDirection == t.firstSegmentDirection
					&& this.curvilineTranslation == t.curvilineTranslation
					&& this.shiftTranslation == t.shiftTranslation
					&& Objects.equals(this.path, t.path);
		}
		return false;
	}

	@Pure
	@Override
	public int hashCode() {
		int bits = 1;
		bits = 31 * bits + Objects.hashCode(this.path);
		bits = 31 * bits + Double.hashCode(this.curvilineTranslation);
		bits = 31 * bits + Double.hashCode(this.shiftTranslation);
		bits = 31 * bits + Objects.hashCode(this.firstSegmentDirection);
		return bits ^ (bits >> 31);
	}

}
