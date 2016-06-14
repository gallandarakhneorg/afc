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

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.geometry.d3.Point3D;
import org.arakhne.afc.math.geometry.d3.Vector3D;

/**
 * Base class for Prisms. Only rectangular prism
 * are considered here.
 *
 * @param <ST> is the type of the general implementation.
 * @param <IT> is the type of the implementation of this shape.
 * @param <IE> is the type of the path elements.
 * @param <P> is the type of the points.
 * @param <V> is the type of the vectors.
 * @param <B> is the type of the bounding boxes.
 * @author $Author: tpiotrow$
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public interface Prism3afp<
			ST extends Shape3afp<?, ?, IE, P, V, B>,
			IT extends Prism3afp<?, ?, IE, P, V, B>,
			IE extends PathElement3afp,
			P extends Point3D<? super P, ? super V>,
			V extends Vector3D<? super V, ? super P>,
			B extends RectangularPrism3afp<?, ?, IE, P, V, B>>
			extends Shape3afp<ST, IT, IE, P, V, B> {

	@Override
	default void toBoundingBox(B box) {
		assert box != null : "rectangular prism must be not null";
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
		assert width >= 0. : "Width must be positive or zero";
		assert height >= 0. : "Height must be positive or zero";
		assert depth >= 0. : "Depth must be positive or zero";
		setFromCorners(x, y, z, x + width, y + height, z + depth);
	}

	/** Change the frame of the prism.
	 *
	 * @param min is the min corner of the rectangular prism.
	 * @param max is the max corner of the rectangular prism.
	 */
	default void set(Point3D<?, ?> min, Point3D<?, ?> max) {
		assert min != null : "Minimum point must be not null";
		assert max != null : "Maximum point must be not null";
		setFromCorners(min.getX(), min.getY(), min.getZ(), max.getX(), max.getY(), max.getZ());
	}

    @Override
    default void set(IT shape) {
        assert shape != null : "Shape must be not null";
        setFromCorners(shape.getMinX(), shape.getMinY(), shape.getMinZ(), shape.getMaxX(), shape.getMaxY(), shape.getMaxZ());
    }

	/** Change the width of the prism, not the min corner.
	 *
	 * @param width the width of the prism.
	 */
	default void setWidth(double width) {
		assert width >= 0. : "Width must be positive or zero";
		setMaxX(getMinX() + width);
	}

	/** Change the height of the prism, not the min corner.
	 *
	 * @param height the heigth of the prism.
	 */
	default void setHeight(double height) {
		assert height >= 0. : "Height must be positive or zero";
		setMaxY(getMinY() + height);
	}

	/** Change the depth of the prism, not the min corner.
	 *
	 * @param depth the depth of the prism
	 */
	default void setDepth(double depth) {
		assert depth >= 0. : "Height must be positive or zero";
		setMaxZ(getMinZ() + depth);
	}

	/** Change the frame of the rectangular prism conserving previous min and max if needed.
	 *
	 * @param x1 is the coordinate of the first corner.
	 * @param y1 is the coordinate of the first corner.
	 * @param z1 is the coordinate of the first corner.
	 * @param x2 is the coordinate of the second corner.
	 * @param y2 is the coordinate of the second corner.
	 * @param z2 is the coordinate of the second corner.
	 */
	// This function has no default implementation for allowing implementation to be atomic.
	void setFromCorners(double x1, double y1, double z1, double x2, double y2, double z2);

	/** Change the frame of the rectangular prism conserving previous min and max if needed.
	 *
	 * @param p1 the first corner.
	 * @param p2 the second corner.
	 */
	// This function has no default implementation for allowing implementation to be atomic.
	default void setFromCorners(Point3D<?, ?> p1, Point3D<?, ?> p2) {
		assert p1 != null : "First corner point must be not null";
		assert p2 != null : "Second corner point must be not null";
		setFromCorners(p1.getX(), p1.getY(), p1.getZ(), p2.getX(), p2.getY(), p2.getZ());
	}

	/**
     * Sets the framing rectangular prism of this <code>Shape</code>
     * based on the specified center point coordinates and corner point
     * coordinates.  The framing rectangular prism is used by the subclasses of
     * <code>RectangularShape</code> to define their geometry.
     *
     * @param centerX the X coordinate of the specified center point
     * @param centerY the Y coordinate of the specified center point
	 * @param centerZ the Z coordinate of the specified center point
     * @param cornerX the X coordinate of the specified corner point
     * @param cornerY the Y coordinate of the specified corner point
	 * @param cornerZ the Z coordinate of the specified corner point
     */
	default void setFromCenter(double centerX, double centerY, double centerZ, double cornerX, double cornerY, double cornerZ) {
		final double demiWidth = Math.abs(centerX - cornerX);
		final double demiHeight = Math.abs(centerY - cornerY);
		final double demiDepth = Math.abs(centerZ - cornerZ);
        setFromCorners(centerX - demiWidth, centerY - demiHeight, centerZ - demiDepth, centerX + demiWidth, centerY + demiHeight,
                centerZ - demiDepth);
	}

	/**
     * Sets the framing rectangular prism of this <code>Shape</code>
     * based on the specified center point coordinates and corner point
     * coordinates.  The framing rectangular prism is used by the subclasses of
     * <code>RectangularShape</code> to define their geometry.
     *
     * @param center the specified center point
     * @param corner the specified corner point
     */
	default void setFromCenter(Point3D<?, ?> center, Point3D<?, ?> corner) {
		assert center != null : "Center point must be not null";
		assert corner != null : "Corner point must be not null";
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
	default void setCenter(Point3D<?, ?> center) {
	    assert center != null : "Center point must be not null";
	    setCenter(center.getX(), center.getY(), center.getZ());
	}

	/** Set the center's x.
	 *
	 * @param cx the center x.
	 */
	default void setCenterX(double cx) {
		final double demiWidth = getWidth() / 2.;
		setMinX(cx - demiWidth);
		setMaxX(cx + demiWidth);
	}

	/** Set the center's y.
	 *
	 * @param cy the center y.
	 */
	default void setCenterY(double cy) {
		final double demiHeight = getHeight() / 2.;
		setMinY(cy - demiHeight);
		setMaxY(cy + demiHeight);
	}

	/** Set the center's z.
	 *
	 * @param cz the center z.
	 */
	default void setCenterZ(double cz) {
		final double demiDepth = getDepth() / 2.;
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
        return getMinX() == getMaxX() && getMinY() == getMaxY() && getMinZ() == getMaxZ();
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
