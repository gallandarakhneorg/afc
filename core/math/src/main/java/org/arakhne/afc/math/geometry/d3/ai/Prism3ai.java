/* 
 * $Id$
 * 
 * Copyright (C) 2010-2013 Stephane GALLAND.
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 * This program is free software; you can redistribute it and/or modify
 */

package org.arakhne.afc.math.geometry.d3.ai;

import org.arakhne.afc.math.geometry.d3.Point3D;
import org.arakhne.afc.math.geometry.d3.Vector3D;
import org.eclipse.xtext.xbase.lib.Pure;

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
		assert (box != null) : "Rectangle must not be null"; //$NON-NLS-1$
		box.setFromCorners(getMinX(), getMinY(), getMinZ(), getMaxX(), getMaxY(), getMaxZ());
	}
	
	@Override
	default void clear() {
		setFromCorners(0, 0, 0, 0, 0, 0);
	}

	/** Change the frame of the rectangle.
	 * 
	 * @param x
	 * @param y
	 * @param z
	 * @param width
	 * @param height
	 * @param depth 
	 */
	default void set(int x, int y, int z, int width, int height, int depth) {
		assert (width >= 0) : "Width must be positive or zero"; //$NON-NLS-1$
		assert (height >= 0) : "Height must be positive or zero"; //$NON-NLS-1$
		assert (depth >= 0) : "Height must be positive or zero"; //$NON-NLS-1$
		setFromCorners(x, y, z, x + width, y + height, z + depth);
	}
	
	/** Change the frame of the rectangle.
	 * 
	 * @param min is the min corner of the rectangle.
	 * @param max is the max corner of the rectangle.
	 */
	default void set(Point3D<?, ?> min, Point3D<?, ?> max) {
		assert (min != null) : "Minimum point must be not be null"; //$NON-NLS-1$
		assert (max != null) : "Maximum point must be not be null"; //$NON-NLS-1$
		setFromCorners(min.ix(), min.iy(), min.iz(), max.ix(), max.iy(), max.iz());
	}
	
	/** Change the width of the rectangle, not the min corner.
	 * 
	 * @param width
	 */
	default void setWidth(int width) {
		assert (width >= 0) : "Width must be positive or zero"; //$NON-NLS-1$
		setMaxX(getMinX() + width);
	}
	
	/** Change the height of the rectangle, not the min corner.
	 * 
	 * @param height
	 */
	default void setHeight(int height) {
		assert (height >= 0) : "Height must be positive or zero"; //$NON-NLS-1$
		setMaxY(getMinY() + height);
	}

	/** Change the depth of the rectangle, not the min corner.
	 * 
	 * @param depth
	 */
	default void setDepth(int depth) {
		assert (depth >= 0) : "Height must be positive or zero"; //$NON-NLS-1$
		setMaxZ(getMinZ() + depth);
	}
	
	/** Change the frame of the rectangle conserving previous min and max if needed.
	 * 
	 * @param x1 is the coordinate of the first corner.
	 * @param y1 is the coordinate of the first corner.
	 * @param z1 is the coordinate of the first corner.
	 * @param x2 is the coordinate of the second corner.
	 * @param y2 is the coordinate of the second corner.
	 * @param z2 is the coordinate of the first corner.
	 */
	void setFromCorners(int x1, int y1, int z1, int x2, int y2, int z2);
	
	/** Change the frame of the rectangle conserving previous min and max if needed.
	 * 
	 * @param p1 the first corner.
	 * @param p2 the second corner.
	 */
	default void setFromCorners(Point3D<?, ?> p1, Point3D<?, ?> p2) {
		assert (p1 != null) : "First corner must be not be null"; //$NON-NLS-1$
		assert (p2 != null) : "Second corner must be not be null"; //$NON-NLS-1$
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
		int demiWidth = Math.abs(centerX - cornerX);
		int demiHeight = Math.abs(centerY - cornerY);
		int demiDepth = Math.abs(centerZ - cornerZ);
		setFromCorners(centerX - demiWidth, centerY - demiHeight, centerZ - demiDepth, centerX + demiWidth, centerY + demiHeight, centerZ + demiDepth);
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
		assert (center != null) : "Center must be not be null"; //$NON-NLS-1$
		assert (corner != null) : "Corner must be not be null"; //$NON-NLS-1$
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
		return getMinX()==getMaxX() && getMinY()==getMaxY() && getMinZ() == getMaxZ(); 
	}
	
	/** Inflate this rectangle with the given amounts.
	 * 
	 * @param minXBorder
	 * @param minYBorder
	 * @param minZBorder
	 * @param maxXBorder
	 * @param maxYBorder
	 * @param maxZBorder 
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
