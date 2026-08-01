/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2026 The original authors and other contributors.
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

import org.arakhne.afc.math.MathUtil;
import org.arakhne.afc.math.geometry.base.d3.BoundsReceiver3D;
import org.arakhne.afc.math.geometry.base.d3.Point3D;
import org.arakhne.afc.math.geometry.base.d3.Quaternion;
import org.arakhne.afc.math.geometry.base.d3.Vector3D;
import org.arakhne.afc.vmutil.asserts.AssertMessages;
import org.eclipse.xtext.xbase.lib.Pure;

/**
 * Base class for rectangular prisms, or boxes. Only rectangular prism
 * are considered here.
 *
 * @param <IT> is the type of the implementation of this shape.
 * @param <IE> is the type of the path elements.
 * @param <P> is the type of the points.
 * @param <V> is the type of the vectors.
 * @param <Q> is the type of the quaternions.
 * @param <B> is the type of the bounding boxes.
 * @author $Author: tpiotrow$
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@SuppressWarnings("checkstyle:magicnumber")
public interface Box3afp<
			IT extends Box3afp<?, IE, P, V, Q, B>,
			IE extends PathElement3afp,
			P extends Point3D<? super P, ? super V, ? super Q>,
			V extends Vector3D<? super V, ? super P, ? super Q>,
			Q extends Quaternion<? super P, ? super V, ? super Q>,
			B extends AlignedBox3afp<?, IE, P, V, Q, B>>
		extends Shape3afp<IT, IE, P, V, Q, B>, BoundsReceiver3D {

	/** Move the first rectangular prism to avoid collision with the second rectangular box.
	 *
	 * @param rminx1 minimum x coordinate of the first box.
	 * @param rminy1 minimum y coordinate of the first box.
	 * @param rminz1 minimum z coordinate of the first box.
	 * @param rmaxx1 maximum x coordinate of the first box.
	 * @param rmaxy1 maximum y coordinate of the first box.
	 * @param rmaxz1 maximum z coordinate of the first box.
	 * @param rminx2 minimum x coordinate of the second box.
	 * @param rminy2 minimum y coordinate of the second box.
	 * @param rminz2 minimum z coordinate of the second box.
	 * @param rmaxx2 maximum x coordinate of the second box.
	 * @param rmaxy2 maximum y coordinate of the second box.
	 * @param rmaxz2 maximum z coordinate of the second box.
	 * @param newMinimumCorner the new coordinates of the minimum corner for the first box. It can be {@code null}.
	 * @param newMaximumCorner the new coordinates of the maximum corner for the first box. It can be {@code null}.
	 * @param displacementVector the displacement vector. It can be {@code null}.
	 */
	@SuppressWarnings("checkstyle:parameternumber")
	static void avoidCollisionBoxBox(
			double rminx1, double rminy1, double rminz1,
			double rmaxx1, double rmaxy1, double rmaxz1,
			double rminx2, double rminy2, double rminz2,
			double rmaxx2, double rmaxy2, double rmaxz2,
			Point3D<?, ?, ?> newMinimumCorner,
			Point3D<?, ?, ?> newMaximumCorner,
			Vector3D<?, ?, ?> displacementVector) {
		assert rminx1 <= rmaxx1 : AssertMessages.lowerEqualParameters(0, Double.valueOf(rminx1), 3, Double.valueOf(rmaxx1));
		assert rminy1 <= rmaxy1 : AssertMessages.lowerEqualParameters(1, Double.valueOf(rminy1), 4, Double.valueOf(rmaxy1));
		assert rminz1 <= rmaxz1 : AssertMessages.lowerEqualParameters(2, Double.valueOf(rminz1), 5, Double.valueOf(rmaxz1));
		assert rminx2 <= rmaxx2 : AssertMessages.lowerEqualParameters(6, Double.valueOf(rminx2), 9, Double.valueOf(rmaxx2));
		assert rminy2 <= rmaxy2 : AssertMessages.lowerEqualParameters(7, Double.valueOf(rminy2), 10, Double.valueOf(rmaxy2));
		assert rminz2 <= rmaxz2 : AssertMessages.lowerEqualParameters(8, Double.valueOf(rminz2), 11, Double.valueOf(rmaxz2));
		assert newMinimumCorner != null || newMaximumCorner != null || displacementVector != null
				: AssertMessages.constraintViolation("you must provide an object for " //$NON-NLS-1$
						+ "newMinimumCorner, newMaximumCorner or " //$NON-NLS-1$
						+ "displacementVector"); //$NON-NLS-1$

		final var dx1 = rmaxx2 - rminx1;
		final var dx2 = rminx2 - rmaxx1;
		final var dy1 = rmaxy2 - rminy1;
		final var dy2 = rminy2 - rmaxy1;
		final var dz1 = rmaxz2 - rminz1;
		final var dz2 = rminz2 - rmaxz1;

		final var absdx1 = Math.abs(dx1);
		final var absdx2 = Math.abs(dx2);
		final var absdy1 = Math.abs(dy1);
		final var absdy2 = Math.abs(dy2);
		final var absdz1 = Math.abs(dz1);
		final var absdz2 = Math.abs(dz2);

		final var min = MathUtil.min(absdx1, absdx2, absdy1, absdy2, absdz1, absdz2);

		var dx = 0.;
		var dy = 0.;
		var dz = 0.;

		if (min == absdy1) {
			dy = dy1;
		} else if (min == absdy2) {
			dy = dy2;
		} else if (min == absdz1) {
			dz = dz1;
		} else if (min == absdz2) {
			dz = dz2;
		} else if (min == absdx1) {
			dx = dx1;
		} else {
			dx = dx2;
		}
		if (newMinimumCorner != null) {
			newMinimumCorner.set(rminx1 + dx, rminy1 + dy, rminz1 + dz);
		}
		if (newMaximumCorner != null) {
			newMaximumCorner.set(rmaxx1 + dx, rmaxy1 + dy, rmaxz1 + dz);
		}
		if (displacementVector != null) {
			displacementVector.set(dx, dy, dz);
		}
	}

	/** Move this aligned box to avoid collision
	 * with the reference aligned box.
	 *
	 * @param rminx1 minimum x coordinate of the first box.
	 * @param rminy1 minimum y coordinate of the first box.
	 * @param rminz1 minimum z coordinate of the first box.
	 * @param rmaxx1 maximum x coordinate of the first box.
	 * @param rmaxy1 maximum y coordinate of the first box.
	 * @param rmaxz1 maximum z coordinate of the first box.
	 * @param rminx2 minimum x coordinate of the second box.
	 * @param rminy2 minimum y coordinate of the second box.
	 * @param rminz2 minimum z coordinate of the second box.
	 * @param rmaxx2 maximum x coordinate of the second box.
	 * @param rmaxy2 maximum y coordinate of the second box.
	 * @param rmaxz2 maximum z coordinate of the second box.
	 * @param allowedDisplacementDirection is the direction of the allowed displacement (it is an input).
	 *     This vector is set according to the result before returning. It can be {@code null}.
	 * @param newMinimumCorner the new coordinates of the minimum corner for the first box. It can be {@code null}.
	 * @param newMaximumCorner the new coordinates of the maximum corner for the first box. It can be {@code null}.
	 * @param displacementVector the displacement vector. It can be {@code null}.
	 */
	@SuppressWarnings("checkstyle:parameternumber")
	static void avoidCollisionBoxBox(
			double rminx1, double rminy1, double rminz1,
			double rmaxx1, double rmaxy1, double rmaxz1,
			double rminx2, double rminy2, double rminz2,
			double rmaxx2, double rmaxy2, double rmaxz2,
			Vector3D<?, ?, ?> allowedDisplacementDirection,
			Point3D<?, ?, ?> newMinimumCorner,
			Point3D<?, ?, ?> newMaximumCorner,
			Vector3D<?, ?, ?> displacementVector) {
		assert rminx1 <= rmaxx1 : AssertMessages.lowerEqualParameters(0, Double.valueOf(rminx1), 3, Double.valueOf(rmaxx1));
		assert rminy1 <= rmaxy1 : AssertMessages.lowerEqualParameters(1, Double.valueOf(rminy1), 4, Double.valueOf(rmaxy1));
		assert rminz1 <= rmaxz1 : AssertMessages.lowerEqualParameters(2, Double.valueOf(rminz1), 5, Double.valueOf(rmaxz1));
		assert rminx2 <= rmaxx2 : AssertMessages.lowerEqualParameters(6, Double.valueOf(rminx2), 9, Double.valueOf(rmaxx2));
		assert rminy2 <= rmaxy2 : AssertMessages.lowerEqualParameters(7, Double.valueOf(rminy2), 10, Double.valueOf(rmaxy2));
		assert rminz2 <= rmaxz2 : AssertMessages.lowerEqualParameters(8, Double.valueOf(rminz2), 11, Double.valueOf(rmaxz2));
		assert newMinimumCorner != null || newMaximumCorner != null || displacementVector != null
				: AssertMessages.constraintViolation("you must provide an object for " //$NON-NLS-1$
						+ "newMinimumCorner, newMaximumCorner or " //$NON-NLS-1$
						+ "displacementVector"); //$NON-NLS-1$

		if (allowedDisplacementDirection == null || MathUtil.isEpsilonZero(allowedDisplacementDirection.getLengthSquared())) {
			avoidCollisionBoxBox(
					rminx1, rminy1, rminz1, rmaxx1, rmaxy1, rmaxz1,
					rminx2, rminy2, rminz2, rmaxx2, rmaxy2, rmaxz2,
					newMinimumCorner, newMaximumCorner, displacementVector);
		} else {
			final var dx1 = rmaxx2 - rminx1;
			final var dx2 = rminx2 - rmaxx1;
			final var dy1 = rmaxy2 - rminy1;
			final var dy2 = rminy2 - rmaxy1;
			final var dz1 = rmaxz2 - rminz1;
			final var dz2 = rminz2 - rmaxz1;

			final var absdx1 = Math.abs(dx1);
			final var absdx2 = Math.abs(dx2);
			final var absdy1 = Math.abs(dy1);
			final var absdy2 = Math.abs(dy2);
			final var absdz1 = Math.abs(dz1);
			final var absdz2 = Math.abs(dz2);

			final double dx;
			final double dy;
			final double dz;

			if (allowedDisplacementDirection.getX() < 0) {
				dx = -Math.min(absdx1, absdx2);
			} else {
				dx = Math.min(absdx1, absdx2);
			}

			if (allowedDisplacementDirection.getY() < 0) {
				dy = -Math.min(absdy1, absdy2);
			} else {
				dy = Math.min(absdy1, absdy2);
			}

			if (allowedDisplacementDirection.getZ() < 0) {
				dz = -Math.min(absdz1, absdz2);
			} else {
				dz = Math.min(absdz1, absdz2);
			}

			allowedDisplacementDirection.set(dx, dy, dz);

			if (newMinimumCorner != null) {
				newMinimumCorner.set(rminx1 + dx, rminy1 + dy, rminz1 + dz);
			}
			if (newMaximumCorner != null) {
				newMaximumCorner.set(rmaxx1 + dx, rmaxy1 + dy, rmaxz1 + dz);
			}
			if (displacementVector != null) {
				displacementVector.set(dx, dy, dz);
			}
		}
	}

	@Override
	default void toBoundingBox(BoundsReceiver3D box) {
		assert box != null : AssertMessages.notNullParameter();
		box.setFromCorners(getMinX(), getMinY(), getMinZ(), getMaxX(), getMaxY(), getMaxZ());
	}

	@Override
	default void clear() {
		setFromCorners(0, 0, 0, 0, 0, 0);
	}

	/** Change the frame of the prism.
	 *
	 * @param x x coordinate of the lower front corner of the prism.
	 * @param y y coordinate of the lower front corner of the prism.
     * @param z z coordinate of the lower front corner of the prism.
     * @param width width of the prism.
     * @param height height of the prism.
     * @param depth depth of the prism.
	 */
	default void set(double x, double y, double z, double width, double height, double depth) {
		assert width >= 0. : AssertMessages.positiveOrZeroParameter(3);
		assert height >= 0. : AssertMessages.positiveOrZeroParameter(4);
		assert depth >= 0. : AssertMessages.positiveOrZeroParameter(5);
		setFromCorners(x, y, z, x + width, y + height, z + depth);
	}

	/** Change the frame of the prism.
	 *
	 * @param min is the min corner of the rectangular prism.
	 * @param max is the max corner of the rectangular prism.
	 */
	default void set(Point3D<?, ?, ?> min, Point3D<?, ?, ?> max) {
		assert min != null : AssertMessages.notNullParameter(0);
		assert max != null : AssertMessages.notNullParameter(1);
		setFromCorners(min.getX(), min.getY(), min.getZ(), max.getX(), max.getY(), max.getZ());
	}

    @Override
    default void set(IT shape) {
        assert shape != null : AssertMessages.notNullParameter();
        setFromCorners(shape.getMinX(), shape.getMinY(), shape.getMinZ(), shape.getMaxX(), shape.getMaxY(), shape.getMaxZ());
    }

	/** Change the width of the prism, not the min corner.
	 *
	 * @param width the width of the prism.
	 */
	default void setWidth(double width) {
		assert width >= 0. : AssertMessages.positiveOrZeroParameter();
		setMaxX(getMinX() + width);
	}

	/** Change the height of the prism, not the min corner.
	 *
	 * @param height the heigth of the prism.
	 */
	default void setHeight(double height) {
		assert height >= 0. : AssertMessages.positiveOrZeroParameter();
		setMaxY(getMinY() + height);
	}

	/** Change the depth of the prism, not the min corner.
	 *
	 * @param depth the depth of the prism
	 */
	default void setDepth(double depth) {
		assert depth >= 0. : AssertMessages.positiveOrZeroParameter();
		setMaxZ(getMinZ() + depth);
	}

	/** Change the frame of the rectangular prism conserving previous min and max if needed.
	 *
	 * @param p1 the first corner.
	 * @param p2 the second corner.
	 */
	// This function has no default implementation for allowing implementation to be atomic.
	default void setFromCorners(Point3D<?, ?, ?> p1, Point3D<?, ?, ?> p2) {
		assert p1 != null : AssertMessages.notNullParameter(0);
		assert p2 != null : AssertMessages.notNullParameter(1);
		setFromCorners(p1.getX(), p1.getY(), p1.getZ(), p2.getX(), p2.getY(), p2.getZ());
	}

	/**
     * Sets the framing rectangular prism of this {@code Shape}
     * based on the specified center point coordinates and corner point
     * coordinates.  The framing rectangular prism is used by the subclasses of
     * {@code RectangularShape} to define their geometry.
     *
     * @param centerX the X coordinate of the specified center point
     * @param centerY the Y coordinate of the specified center point
	 * @param centerZ the Z coordinate of the specified center point
     * @param cornerX the X coordinate of the specified corner point
     * @param cornerY the Y coordinate of the specified corner point
	 * @param cornerZ the Z coordinate of the specified corner point
     */
	default void setFromCenter(double centerX, double centerY, double centerZ, double cornerX, double cornerY, double cornerZ) {
		final var demiWidth = Math.abs(centerX - cornerX);
		final var demiHeight = Math.abs(centerY - cornerY);
		final var demiDepth = Math.abs(centerZ - cornerZ);
        setFromCorners(
        		centerX - demiWidth,
        		centerY - demiHeight,
        		centerZ - demiDepth,
        		centerX + demiWidth,
        		centerY + demiHeight,
                centerZ + demiDepth);
	}

	/**
     * Sets the framing rectangular prism of this {@code Shape}
     * based on the specified center point coordinates and corner point
     * coordinates.  The framing rectangular prism is used by the subclasses of
     * {@code RectangularShape} to define their geometry.
     *
     * @param center the specified center point
     * @param corner the specified corner point
     */
	default void setFromCenter(Point3D<?, ?, ?> center, Point3D<?, ?, ?> corner) {
		assert center != null : AssertMessages.notNullParameter(0);
		assert corner != null : AssertMessages.notNullParameter(1);
		setFromCenter(center.getX(), center.getY(), center.getZ(), corner.getX(), corner.getY(), corner.getZ());
	}

	/** Replies the min X.
	 *
	 * @return the min x.
	 */
	@Pure
	double getMinX();

	/** Set the min X conserving previous min if needed.
	 *
	 * @param x the min x.
	 */
	void setMinX(double x);

	/** Replies the center x.
	 *
	 * @return the center x.
	 */
	@Pure
	default double getCenterX() {
		return (getMinX() + getMaxX()) / 2;
	}

	/** Replies the max x.
	 *
	 * @return the max x.
	 */
	@Pure
	double getMaxX();

	/** Set the max X conserving previous max if needed.
	 *
	 * @param x the max x.
	 */
	void setMaxX(double x);

	/** Replies the min y.
	 *
	 * @return the min y.
	 */
	@Pure
	double getMinY();

	/** Set the min Y conserving previous min if needed.
	 *
	 * @param y the min y.
	 */
	void setMinY(double y);

	/** Replies the center y.
	 *
	 * @return the center y.
	 */
	@Pure
	default double getCenterY() {
		return (getMinY() + getMaxY()) / 2;
	}

	/** Replies the max y.
	 *
	 * @return the max y.
	 */
	@Pure
	double getMaxY();

	/** Set the max Y conserving previous max if needed.
	 *
	 * @param y the max y.
	 */
	void setMaxY(double y);

	/** Replies the min z.
	 *
	 * @return the min z.
	 */
	@Pure
	double getMinZ();

	/** Set the min Z conserving previous min if needed.
	 *
	 * @param z the min z.
	 */
	void setMinZ(double z);

	/** Replies the center z.
	 *
	 * @return the center z.
	 */
	@Pure
	default double getCenterZ() {
		return (getMinZ() + getMaxZ()) / 2;
	}

	/** Replies the max z.
	 *
	 * @return the max z.
	 */
	@Pure
	double getMaxZ();

	/** Set the max Z conserving previous max if needed.
	 *
	 * @param z the max z.
	 */
	void setMaxZ(double z);

	/** Replies the center.
	 *
	 * @return the center.
	 */
	default P getCenter() {
		return getGeomFactory().newPoint(getCenterX(), getCenterY(), getCenterZ());
	}

	/** Set the center.
	 *
	 * @param cx the center x.
	 * @param cy the center y.
	 * @param cz the center z.
	 */
	default void setCenter(double cx, double cy, double cz) {
		setCenterX(cx);
		setCenterY(cy);
		setCenterZ(cz);
	}

	/** Set the center.
	 *
	 * @param center the center point.
	 */
	default void setCenter(Point3D<?, ?, ?> center) {
	    assert center != null : AssertMessages.notNullParameter();
	    setCenter(center.getX(), center.getY(), center.getZ());
	}

	/** Set the center's x.
	 *
	 * @param cx the center x.
	 */
	default void setCenterX(double cx) {
		final var demiWidth = getWidth() / 2.;
		setMinX(cx - demiWidth);
		setMaxX(cx + demiWidth);
	}

	/** Set the center's y.
	 *
	 * @param cy the center y.
	 */
	default void setCenterY(double cy) {
		final var demiHeight = getHeight() / 2.;
		setMinY(cy - demiHeight);
		setMaxY(cy + demiHeight);
	}

	/** Set the center's z.
	 *
	 * @param cz the center z.
	 */
	default void setCenterZ(double cz) {
		final var demiDepth = getDepth() / 2.;
		setMinZ(cz - demiDepth);
		setMaxZ(cz + demiDepth);
	}

	/** Replies the width.
	 *
	 * @return the width.
	 */
	@Pure
	default double getWidth() {
		return getMaxX() - getMinX();
	}

	/** Replies the height.
	 *
	 * @return the height.
	 */
	@Pure
	default double getHeight() {
		return getMaxY() - getMinY();
	}

	/** Replies the depth.
	 *
	 * @return the depth.
	 */
	@Pure
	default double getDepth() {
		return getMaxZ() - getMinZ();
	}

	@Override
	default void translate(double dx, double dy, double dz) {
		setFromCorners(getMinX() + dx, getMinY() + dy, getMinZ() + dz, getMaxX() + dx, getMaxY() + dy, getMaxZ() + dz);
	}

	@Pure
	@Override
	default boolean isEmpty() {
        return getMinX() >= getMaxX() || getMinY() >= getMaxY() || getMinZ() >= getMaxZ();
	}

	@Pure
	@Override
	default boolean isDegeneratedPoint() {
        return getMinX() >= getMaxX() && getMinY() >= getMaxY() && getMinZ() >= getMaxZ();
	}

	/** Inflate this rectangular prism with the given amounts.
	 *
	 * <p>All borders may be inflated. If the value associated to a border
	 * is positive, the border is moved outside the current prism.
	 * If the value is negative, the border is moved inside the prism.
	 *
	 * @param minXBorder the value to substract to the minimum x.
	 * @param minYBorder the value to substract to the minimum y.
	 * @param minZBorder the value to substract to the minimum z.
	 * @param maxXBorder the value to add to the maximum x.
	 * @param maxYBorder the value to add to the maximum y.
	 * @param maxZBorder the value to add to the maximum z.
	 */
	default void inflate(double minXBorder, double minYBorder, double minZBorder, double maxXBorder, double maxYBorder,
            double maxZBorder) {
		setFromCorners(
				getMinX() - minXBorder,
				getMinY() - minYBorder,
				getMinZ() - minZBorder,
				getMaxX() + maxXBorder,
				getMaxY() + maxYBorder,
				getMaxZ() + maxZBorder);
	}

}
