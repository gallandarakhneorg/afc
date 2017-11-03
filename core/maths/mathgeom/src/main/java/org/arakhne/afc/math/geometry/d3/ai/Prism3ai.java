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

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.geometry.d3.Point3D;
import org.arakhne.afc.math.geometry.d3.Vector3D;
import org.arakhne.afc.vmutil.asserts.AssertMessages;

/** Fonctional interface that represented a rectangular shape on a plane.
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
public interface Prism3ai<
		ST extends Shape3ai<?, ?, IE, P, V, B>,
		IT extends Prism3ai<?, ?, IE, P, V, B>,
		IE extends PathElement3ai,
		P extends Point3D<? super P, ? super V>,
		V extends Vector3D<? super V, ? super P>,
		B extends RectangularPrism3ai<?, ?, IE, P, V, B>>
		extends Shape3ai<ST, IT, IE, P, V, B> {

	@Override
	default void toBoundingBox(B box) {
		assert box != null : AssertMessages.notNullParameter();
		box.setFromCorners(getMinX(), getMinY(), getMinZ(), getMaxX(), getMaxY(), getMaxZ());
	}

	@Override
	default void clear() {
		setFromCorners(0, 0, 0, 0, 0, 0);
	}

	/** Change the frame of the prism.
    *
    * @param x x coordinate of the lower corner.
    * @param y y coordinate of the lower corner.
    * @param z z coordinate of the lower corner.
    * @param width width of the prism.
    * @param height height of the prism.
    * @param depth depth of the prism.
    */
	@SuppressWarnings("checkstyle:magicnumber")
	default void set(int x, int y, int z, int width, int height, int depth) {
		assert width >= 0 : AssertMessages.positiveOrZeroParameter(3);
		assert height >= 0 : AssertMessages.positiveOrZeroParameter(4);
		assert depth >= 0 : AssertMessages.positiveOrZeroParameter(5);
		setFromCorners(x, y, z, x + width, y + height, z + depth);
	}

	/** Change the frame of the prism.
	 *
	 * @param min is the min corner of the prism.
	 * @param max is the max corner of the prism.
	 */
	default void set(Point3D<?, ?> min, Point3D<?, ?> max) {
		assert min != null : AssertMessages.notNullParameter(0);
		assert max != null : AssertMessages.notNullParameter(1);
		setFromCorners(min.ix(), min.iy(), min.iz(), max.ix(), max.iy(), max.iz());
	}

	/** Change the width of the prism, not the min corner.
	 *
	 * @param width the width of the prism.
	 */
	default void setWidth(int width) {
		assert width >= 0 : AssertMessages.positiveOrZeroParameter();
		setMaxX(getMinX() + width);
	}

	/** Change the height of the prism, not the min corner.
	 *
	 * @param height the height of the prism.
	 */
	default void setHeight(int height) {
		assert height >= 0 : AssertMessages.positiveOrZeroParameter();
		setMaxY(getMinY() + height);
	}

	/** Change the depth of the prism, not the min corner.
	 *
	 * @param depth the depth of the prism
	 */
	default void setDepth(int depth) {
		assert depth >= 0 : AssertMessages.positiveOrZeroParameter();
		setMaxZ(getMinZ() + depth);
	}

	/** Change the frame of the prism conserving previous min and max if needed.
	 *
	 * @param x1 is the coordinate of the first corner.
	 * @param y1 is the coordinate of the first corner.
	 * @param z1 is the coordinate of the first corner.
	 * @param x2 is the coordinate of the second corner.
	 * @param y2 is the coordinate of the second corner.
	 * @param z2 is the coordinate of the first corner.
	 */
	void setFromCorners(int x1, int y1, int z1, int x2, int y2, int z2);

	/** Change the frame of the prism conserving previous min and max if needed.
	 *
	 * @param p1 the first corner.
	 * @param p2 the second corner.
	 */
	default void setFromCorners(Point3D<?, ?> p1, Point3D<?, ?> p2) {
		assert p1 != null : AssertMessages.notNullParameter(0);
		assert p2 != null : AssertMessages.notNullParameter(1);
		setFromCorners(p1.ix(), p1.iy(), p1.iz(), p2.ix(), p2.iy(), p2.iz());
	}

	/**
     * Sets the framing rectangle of this <code>Shape</code>
     * based on the specified center point coordinates and corner point
     * coordinates.  The framing rectangle is used by the subclasses of
     * <code>RectangularShape</code> to define their geometry.
     *
     * @param centerX the X coordinate of the specified center point
     * @param centerY the Y coordinate of the specified center point
     * @param centerZ the Z coordinate of the specified center point
     * @param cornerX the X coordinate of the specified corner point
     * @param cornerY the Y coordinate of the specified corner point
     * @param cornerZ the Z coordinate of the specified corner point
     */
	default void setFromCenter(int centerX, int centerY, int centerZ, int cornerX, int cornerY, int cornerZ) {
		final int demiWidth = Math.abs(centerX - cornerX);
		final int demiHeight = Math.abs(centerY - cornerY);
		final int demiDepth = Math.abs(centerZ - cornerZ);
        setFromCorners(centerX - demiWidth, centerY - demiHeight, centerZ - demiDepth, centerX + demiWidth, centerY + demiHeight,
                centerZ + demiDepth);
	}

	/**
     * Sets the framing rectangle of this <code>Shape</code>
     * based on the specified center point coordinates and corner point
     * coordinates.  The framing rectangle is used by the subclasses of
     * <code>RectangularShape</code> to define their geometry.
     *
     * @param center the specified center point
     * @param corner the specified corner point
     */
	default void setFromCenter(Point3D<?, ?> center, Point3D<?, ?> corner) {
		assert center != null : AssertMessages.notNullParameter(0);
		assert corner != null : AssertMessages.notNullParameter(1);
		setFromCenter(center.ix(), center.iy(), center.iz(), corner.ix(), corner.iy(), corner.iz());
	}

	/** Replies the min X.
	 *
	 * @return the min x.
	 */
	@Pure
	int getMinX();

	/** Set the min X conserving previous min if needed.
	 *
	 * @param x the min x.
	 */
	void setMinX(int x);

	/** Replies the center x.
	 *
	 * @return the center x.
	 */
	@Pure
	default int getCenterX() {
		return (getMinX() + getMaxX()) / 2;
	}

	/** Replies the max x.
	 *
	 * @return the max x.
	 */
	@Pure
	int getMaxX();

	/** Set the max X conserving previous max if needed.
	 *
	 * @param x the max x.
	 */
	void setMaxX(int x);

	/** Replies the min y.
	 *
	 * @return the min y.
	 */
	@Pure
	int getMinY();

	/** Set the min Y conserving previous min if needed.
	 *
	 * @param y the min y.
	 */
	void setMinY(int y);

	/** Replies the center y.
	 *
	 * @return the center y.
	 */
	@Pure
	default int getCenterY() {
		return (getMinY() + getMaxY()) / 2;
	}

	/** Replies the max y.
	 *
	 * @return the max y.
	 */
	@Pure
	int getMaxY();

	/** Set the max Y conserving previous max if needed.
	 *
	 * @param y the max y.
	 */
	void setMaxY(int y);

	/** Replies the min z.
	 *
	 * @return the min z.
	 */
	@Pure
	int getMinZ();

	/** Set the min Z conserving previous min if needed.
	 *
	 * @param z the min z.
	 */
	void setMinZ(int z);

	/** Replies the center z.
	 *
	 * @return the center z.
	 */
	@Pure
	default int getCenterZ() {
		return (getMinZ() + getMaxZ()) / 2;
	}

	/** Replies the max z.
	 *
	 * @return the max z.
	 */
	@Pure
	int getMaxZ();

	/** Set the max Z conserving previous max if needed.
	 *
	 * @param z the max z.
	 */
	void setMaxZ(int z);

	/** Replies the width.
	 *
	 * @return the width.
	 */
	@Pure
	default int getWidth() {
		return getMaxX() - getMinX();
	}

	/** Replies the height.
	 *
	 * @return the height.
	 */
	@Pure
	default int getHeight() {
		return getMaxY() - getMinY();
	}

	/** Replies the depth.
	 *
	 * @return the depth.
	 */
	@Pure
	default int getDepth() {
		return getMaxZ() - getMinZ();
	}

	@Override
	default void translate(int dx, int dy, int dz) {
		setFromCorners(getMinX() + dx, getMinY() + dy, getMinZ() + dz, getMaxX() + dx, getMaxY() + dy, getMaxZ() + dz);
	}

	@Pure
	@Override
	default boolean isEmpty() {
        return getMinX() == getMaxX() && getMinY() == getMaxY() && getMinZ() == getMaxZ();
	}

	/** Inflate this prism with the given amounts.
	 *
	 * <p>All borders may be inflated. If the value associated to a border
	 * is positive, the border is moved outside the current rectangle.
	 * If the value is negative, the border is moved inside the rectangle.
	 *
	 * @param minXBorder the value to substract to the minimum x.
	 * @param minYBorder the value to substract to the minimum y.
	 * @param minZBorder the value to substract to the minimum z.
	 * @param maxXBorder the value to add to the maximum x.
	 * @param maxYBorder the value to add to the maximum y.
	 * @param maxZBorder the value to add to the maximum z.
	 */
	default void inflate(int minXBorder, int minYBorder, int minZBorder, int maxXBorder, int maxYBorder, int maxZBorder) {
		setFromCorners(
				getMinX() - minXBorder,
				getMinY() - minYBorder,
				getMinZ() - minZBorder,
				getMaxX() + maxXBorder,
				getMaxY() + maxYBorder,
				getMaxZ() + maxZBorder);
	}

}
