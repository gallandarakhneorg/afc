/* 
 * $Id$
 * 
 * Copyright (c) 2016, Multiagent Team,
 * Laboratoire Systemes et Transports,
 * Universite de Technologie de Belfort-Montbeliard.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of the Laboratoire Systemes et Transports (SET)
 * of Universite de Technologie de Belfort-Montbeliard.
 * You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with the SET.
 *
 * http://www.multiagent.fr/
 */
package org.arakhne.afc.math.geometry.d3.ad;

import org.arakhne.afc.math.geometry.d3.Point3D;
import org.arakhne.afc.math.geometry.d3.Vector3D;
import org.eclipse.xtext.xbase.lib.Pure;

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
public interface Prism3ad<
			ST extends Shape3ad<?, ?, IE, P, V, B>,
			IT extends Prism3ad<?, ?, IE, P, V, B>,
			IE extends PathElement3ad,
			P extends Point3D<? super P, ? super V>,
			V extends Vector3D<? super V, ? super P>,
			B extends RectangularPrism3ad<?, ?, IE, P, V, B>> 
			extends Shape3ad<ST, IT, IE, P, V, B> {

	@Override
	default void toBoundingBox(B box) {
		assert (box != null) : "rectagular prism must be not null"; //$NON-NLS-1$
		box.setFromCorners(getMinX(), getMinY(), getMinZ(), getMaxX(), getMaxY(), getMaxZ());
	}
	
	@Override
	default void clear() {
		setFromCorners(0, 0, 0, 0, 0, 0);
	}

	/** Change the frame of the rectagular prism.
	 * 
	 * @param x
	 * @param y
	 * @param z
	 * @param width
	 * @param height
	 * @param depth 
	 */
	default void set(double x, double y, double z, double width, double height, double depth) {
		assert (width >= 0.) : "Width must be positive or zero"; //$NON-NLS-1$
		assert (height >= 0.) : "Height must be positive or zero"; //$NON-NLS-1$
		assert (depth >= 0.) : "Depth must be positive or zero"; //$NON-NLS-1$
		setFromCorners(x, y, z, x + width, y + height, z + depth);
	}
	
	/** Change the frame of the rectagular prism.
	 * 
	 * @param min is the min corner of the rectagular prism.
	 * @param max is the max corner of the rectagular prism.
	 */
	default void set(Point3D<?, ?> min, Point3D<?, ?> max) {
		assert (min != null) : "Minimum point must be not null"; //$NON-NLS-1$
		assert (max != null) : "Maximum point must be not null"; //$NON-NLS-1$
		setFromCorners(min.getX(), min.getY(), min.getZ(), max.getX(), max.getY(), max.getZ());
	}
	
	/** Change the width of the rectagular prism, not the min corner.
	 * 
	 * @param width
	 */
	default void setWidth(double width) {
		assert (width >= 0.) : "Width must be positive or zero"; //$NON-NLS-1$
		setMaxX(getMinX() + width);
	}
	
	/** Change the height of the rectagular prism, not the min corner.
	 * 
	 * @param height
	 */
	default void setHeight(double height) {
		assert (height >= 0.) : "Height must be positive or zero"; //$NON-NLS-1$
		setMaxY(getMinY() + height);
	}

	/** Change the depth of the rectagular prism, not the min corner.
	 * 
	 * @param depth
	 */
	default void setDepth(double depth) {
		assert (depth >= 0.) : "Height must be positive or zero"; //$NON-NLS-1$
		setMaxZ(getMinZ() + depth);
	}
	
	/** Change the frame of the rectagular prism conserving previous min and max if needed.
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
	
	/** Change the frame of the rectagular prism conserving previous min and max if needed.
	 * 
	 * @param p1 the first corner.
	 * @param p2 the second corner.
	 */
	// This function has no default implementation for allowing implementation to be atomic. 
	default void setFromCorners(Point3D<?, ?> p1, Point3D<?, ?> p2) {
		assert (p1 != null) : "First corner point must be not null"; //$NON-NLS-1$
		assert (p2 != null) : "Second corner point must be not null"; //$NON-NLS-1$
		setFromCorners(p1.getX(), p1.getY(), p1.getZ(), p2.getX(), p2.getY(), p2.getZ());
	}

	/**
     * Sets the framing rectagular prism of this <code>Shape</code>
     * based on the specified center point coordinates and corner point
     * coordinates.  The framing rectagular prism is used by the subclasses of
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
		double demiWidth = Math.abs(centerX - cornerX);
		double demiHeight = Math.abs(centerY - cornerY);
		double demiDepth = Math.abs(centerZ - cornerZ);
		setFromCorners(centerX - demiWidth, centerY - demiHeight, centerZ - demiDepth, centerX + demiWidth, centerY + demiHeight, centerZ - demiDepth);
	}
	
	/**
     * Sets the framing rectagular prism of this <code>Shape</code>
     * based on the specified center point coordinates and corner point
     * coordinates.  The framing rectagular prism is used by the subclasses of
     * <code>RectangularShape</code> to define their geometry.
     *
     * @param center the specified center point
     * @param corner the specified corner point
     */
	default void setFromCenter(Point3D<?, ?> center, Point3D<?, ?> corner) {
		assert (center != null) : "Center point must be not null"; //$NON-NLS-1$
		assert (corner != null) : "Corner point must be not null"; //$NON-NLS-1$
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

	/** Set the center's x.
	 * 
	 * @param cx the center x.
	 */
	default void setCenterX(double cx) {
		double demiWidth = getWidth() / 2.;
		setMinX(cx - demiWidth);
		setMaxX(cx + demiWidth);
	}
	
	/** Set the center's y.
	 * 
	 * @param cy the center y.
	 */
	default void setCenterY(double cy) {
		double demiHeight = getHeight() / 2.;
		setMinY(cy - demiHeight);
		setMaxY(cy + demiHeight);
	}

	/** Set the center's z.
	 * 
	 * @param cz the center z.
	 */
	default void setCenterZ(double cz) {
		double demiDepth = getDepth() / 2.;
		setMinY(cz - demiDepth);
		setMaxY(cz + demiDepth);
	}

	/** Set the center.
	 * 
	 * @param center
	 */
	default void setCenter(Point3D<?, ?> center) {
		assert (center != null) : "Center point must be not null"; //$NON-NLS-1$
		setCenter(center.getX(), center.getY(), center.getZ());
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
		return getMinX()==getMaxX() && getMinY()==getMaxY() && getMinZ() == getMaxZ(); 
	}
	
	/** Inflate this rectagular prism with the given amounts.
	 * 
	 * @param minXBorder
	 * @param minYBorder
	 * @param minZBorder 
	 * @param maxXBorder
	 * @param maxYBorder
	 * @param maxZBorder 
	 */
	default void inflate(double minXBorder, double minYBorder, double minZBorder, double maxXBorder, double maxYBorder, double maxZBorder) {
		setFromCorners(
				getMinX() - minXBorder,
				getMinY() - minYBorder,
				getMinZ() - minZBorder,
				getMaxX() + maxXBorder,
				getMaxY() + maxYBorder,
				getMaxZ() + maxZBorder);
	}

	@Override
	default void set(IT s) {
		assert (s != null) : "Shape must be not null"; //$NON-NLS-1$
		setFromCorners(s.getMinX(), s.getMinY(), s.getMinZ(), s.getMaxX(), s.getMaxY(), s.getMaxZ());
	}
}
