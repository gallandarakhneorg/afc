package org.arakhne.afc.math.geometry.d3.continuous;

import org.arakhne.afc.math.geometry.coordinatesystem.CoordinateSystem3D;
import org.arakhne.afc.math.geometry.d3.Point3D;
import org.arakhne.afc.math.geometry.d3.Vector3D;
import org.eclipse.xtext.xbase.lib.Pure;

public class OrientedBox3f extends AbstractOrientedBox3F{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3792854643360625652L;

	
	/** Center of the box.
	 */
	protected final Point3f center = new Point3f();

	/**
	 * First axis.
	 */
	protected final Vector3f axis1 = new Vector3f();

	/**
	 * Second axis.
	 */
	protected final Vector3f axis2 = new Vector3f();

	/**
	 * Third axis.
	 */
	protected final Vector3f axis3 = new Vector3f();

	/**
	 * First axis extent of the OBB, cannot be negative.
	 */
	protected double extent1;

	/**
	 * Second axis extent of the OBB, cannot be negative.
	 */
	protected double extent2;

	/**
	 * Third axis extent of the OBB, cannot be negative.
	 */
	protected double extent3;

	/**
	 * Build an empty OBB.
	 */
	public OrientedBox3f() {
		//
	}

	/**
	 * Build an OBB.
	 * <p>
	 * The thirds axis is computed from the cross product with the two other axis.
	 * The cross product may be {@link Vector3f#crossLeftHand(org.arakhne.afc.math.geometry.d3.Vector3D)}
	 * or {@link Vector3f#crossRightHand(org.arakhne.afc.math.geometry.d3.Vector3D)} according to
	 * {@link CoordinateSystem3D#getDefaultCoordinateSystem()}.
	 *
	 * @param cx x coordinate of the box center.
	 * @param cy y coordinate of the box center.
	 * @param cz z coordinate of the box center.
	 * @param axis1x x coordinate of the first axis of the box.
	 * @param axis1y y coordinate of the first axis of the box.
	 * @param axis1z z coordinate of the first axis of the box.
	 * @param axis2x x coordinate of the second axis of the box.
	 * @param axis2y y coordinate of the secons axis of the box.
	 * @param axis2z z coordinate of the second axis of the box.
	 * @param axis1Extent extent of the first axis.
	 * @param axis2Extent extent of the second axis.
	 * @param axis3Extent extent of the third axis.
	 */
	public OrientedBox3f(
			double cx, double cy, double cz,
			double axis1x, double axis1y, double axis1z,
			double axis2x, double axis2y, double axis2z,
			double axis1Extent, double axis2Extent, double axis3Extent) {
		set(cx, cy, cz,
				axis1x, axis1y, axis1z, axis2x, axis2y, axis2z,
				axis1Extent, axis2Extent, axis3Extent,
				CoordinateSystem3D.getDefaultCoordinateSystem());
	}

	/**
	 * Build an OBB.
	 * <p>
	 * The thirds axis is computed from the cross product with the two other axis.
	 * The cross product may be {@link Vector3f#crossLeftHand(org.arakhne.afc.math.geometry.d3.Vector3D)}
	 * or {@link Vector3f#crossRightHand(org.arakhne.afc.math.geometry.d3.Vector3D)} according to
	 * the given <code>system</code>.
	 *
	 * @param cx x coordinate of the box center.
	 * @param cy y coordinate of the box center.
	 * @param cz z coordinate of the box center.
	 * @param axis1x x coordinate of the first axis of the box.
	 * @param axis1y y coordinate of the first axis of the box.
	 * @param axis1z z coordinate of the first axis of the box.
	 * @param axis2x x coordinate of the second axis of the box.
	 * @param axis2y y coordinate of the secons axis of the box.
	 * @param axis2z z coordinate of the second axis of the box.
	 * @param axis1Extent extent of the first axis.
	 * @param axis2Extent extent of the second axis.
	 * @param axis3Extent extent of the third axis.
	 * @param system the coordinate system to use for computing the third axis.
	 */
	public OrientedBox3f(
			double cx, double cy, double cz,
			double axis1x, double axis1y, double axis1z,
			double axis2x, double axis2y, double axis2z,
			double axis1Extent, double axis2Extent, double axis3Extent,
			CoordinateSystem3D system) {
		set(cx, cy, cz,
				axis1x, axis1y, axis1z,
				axis2x, axis2y, axis2z,
				axis1Extent, axis2Extent, axis3Extent,
				system);
	}

	/**
	 * Build an OBB.
	 * <p>
	 * The thirds axis is computed from the cross product with the two other axis.
	 * The cross product may be {@link Vector3f#crossLeftHand(org.arakhne.afc.math.geometry.d3.Vector3D)}
	 * or {@link Vector3f#crossRightHand(org.arakhne.afc.math.geometry.d3.Vector3D)} according to
	 * {@link CoordinateSystem3D#getDefaultCoordinateSystem()}.
	 *
	 * @param center the box center.
	 * @param axis1 the first axis of the box.
	 * @param axis2 the second axis of the box.
	 * @param axis1Extent extent of the first axis.
	 * @param axis2Extent extent of the second axis.
	 * @param axis3Extent extent of the third axis.
	 */
	@SuppressWarnings("hiding")
	public OrientedBox3f(
			Point3D center,
			Vector3D axis1,
			Vector3D axis2,
			double axis1Extent, double axis2Extent, double axis3Extent) {
		this(center.getX(), center.getY(), center.getZ(),
				axis1.getX(), axis1.getY(), axis1.getZ(),
				axis2.getX(), axis2.getY(), axis2.getZ(),
				axis1Extent, axis2Extent, axis3Extent);
	}

	/**
	 * Build an OBB from the set of vertex that composes the corresponding object 3D.
	 * 
	 * @param vertices
	 */
	public OrientedBox3f(Iterable<? extends Point3D> vertices) {
		setFromPointCloud(vertices);
	}
	
	
	
	/** Replies the center.
	 *
	 * @return the center.
	 */
	@Pure
	@Override
	public Point3f getCenter() {
		return this.center.clone();
	}

	/** Replies the center x.
	 *
	 * @return the center x.
	 */
	@Pure
	@Override
	public double getCenterX() {
		return this.center.getX();
	}

	/** Replies the center y.
	 *
	 * @return the center y.
	 */
	@Pure
	@Override
	public double getCenterY() {
		return this.center.getY();
	}

	/** Replies the center z.
	 *
	 * @return the center z.
	 */
	@Pure
	@Override
	public double getCenterZ() {
		return this.center.getZ();
	}

	/** Set the center.
	 * 
	 * @param cx the center x.
	 * @param cy the center y.
	 * @param cz the center z.
	 */
	@Override
	public void setCenter(double cx, double cy, double cz) {
		this.center.set(cx, cy, cz);
	}

	/** Set the center.
	 * 
	 * @param center1
	 */
	@Override
	public void setCenter(Point3D center1) {
		setCenter(center1.getX(), center1.getY(), center1.getZ());
	}

	/** Replies the first axis of the oriented box.
	 *
	 * @return the unit vector of the first axis. 
	 */
	@Pure
	@Override
	public Vector3f getFirstAxis() {
		return this.axis1.clone();
	}

	/** Replies coordinate x of the first axis of the oriented box.
	 *
	 * @return the coordinate x of the unit vector of the first axis. 
	 */
	@Pure
	@Override
	public double getFirstAxisX() {
		return this.axis1.getX();
	}

	/** Replies coordinate y of the first axis of the oriented box.
	 *
	 * @return the coordinate y of the unit vector of the first axis. 
	 */
	@Pure
	@Override
	public double getFirstAxisY() {
		return this.axis1.getY();
	}

	/** Replies coordinate z of the first axis of the oriented box.
	 *
	 * @return the coordinate z of the unit vector of the first axis. 
	 */
	@Pure
	@Override
	public double getFirstAxisZ() {
		return this.axis1.getZ();
	}

	/** Replies the second axis of the oriented box.
	 *
	 * @return the unit vector of the second axis. 
	 */
	@Pure
	@Override
	public Vector3f getSecondAxis() {
		return this.axis2.clone();
	}

	/** Replies coordinate x of the second axis of the oriented box.
	 *
	 * @return the coordinate x of the unit vector of the second axis. 
	 */
	@Pure
	@Override
	public double getSecondAxisX() {
		return this.axis2.getX();
	}

	/** Replies coordinate y of the second axis of the oriented box.
	 *
	 * @return the coordinate y of the unit vector of the second axis. 
	 */
	@Pure
	@Override
	public double getSecondAxisY() {
		return this.axis2.getY();
	}

	/** Replies coordinate z of the second axis of the oriented box.
	 *
	 * @return the coordinate z of the unit vector of the second axis. 
	 */
	@Pure
	@Override
	public double getSecondAxisZ() {
		return this.axis2.getZ();
	}

	/** Replies the third axis of the oriented box.
	 *
	 * @return the unit vector of the third axis. 
	 */
	@Pure
	@Override
	public Vector3f getThirdAxis() {
		return this.axis3.clone();
	}

	/** Replies coordinate x of the third axis of the oriented box.
	 *
	 * @return the coordinate x of the unit vector of the third axis. 
	 */
	@Pure
	@Override
	public double getThirdAxisX() {
		return this.axis3.getX();
	}

	/** Replies coordinate y of the third axis of the oriented box.
	 *
	 * @return the coordinate y of the unit vector of the third axis. 
	 */
	@Pure
	@Override
	public double getThirdAxisY() {
		return this.axis3.getY();
	}

	/** Replies coordinate z of the third axis of the oriented box.
	 *
	 * @return the coordinate z of the unit vector of the third axis. 
	 */
	@Pure
	@Override
	public double getThirdAxisZ() {
		return this.axis3.getZ();
	}

	/** Replies the demi-size of the box along its first axis.
	 * 
	 * @return the extent along the first axis.
	 */
	@Pure
	@Override
	public double getFirstAxisExtent() {
		return this.extent1;
	}

	/** Change the demi-size of the box along its first axis.
	 * 
	 * @param extent - the extent along the first axis.
	 */
	@Override
	public void setFirstAxisExtent(double extent) {
		this.extent1 = Math.max(extent, 0);
	}

	/** Replies the demi-size of the box along its second axis.
	 * 
	 * @return the extent along the second axis.
	 */
	@Pure
	@Override
	public double getSecondAxisExtent() {
		return this.extent2;
	}

	/** Change the demi-size of the box along its second axis.
	 * 
	 * @param extent - the extent along the second axis.
	 */
	@Override
	public void setSecondAxisExtent(double extent) {
		this.extent2 = Math.max(extent, 0);
	}

	/** Replies the demi-size of the box along its third axis.
	 * 
	 * @return the extent along the third axis.
	 */
	@Pure
	@Override
	public double getThirdAxisExtent() {
		return this.extent3;
	}

	/** Change the demi-size of the box along its third axis.
	 * 
	 * @param extent - the extent along the third axis.
	 */
	@Override
	public void setThirdAxisExtent(double extent) {
		this.extent3 = Math.max(extent, 0);
	}

	/** Set the first axis of the box.
	 * The third axis is updated to be perpendicular to the two other axis.
	 * 
	 * @param axis - the new values for the first axis.
	 */
	@Override
	public void setFirstAxis(Vector3D axis) {
		setFirstAxis(axis.getX(), axis.getY(), axis.getZ(), getFirstAxisExtent());
	}

	/** Set the first axis of the box.
	 * The third axis is updated to be perpendicular to the two other axis.
	 * 
	 * @param axis - the new values for the first axis.
	 * @param extent - the extent of the axis.
	 */
	@Override
	public void setFirstAxis(Vector3D axis, double extent) {
		setFirstAxis(axis.getX(), axis.getY(), axis.getZ(), extent);
	}

	/** Set the first axis of the box.
	 * The third axis is updated to be perpendicular to the two other axis.
	 * 
	 * @param x
	 * @param y
	 * @param z
	 */
	@Override
	public void setFirstAxis(double x, double y, double z) {
		setFirstAxis(x, y, z, getFirstAxisExtent());
	}

	/** Set the first axis of the box.
	 * The third axis is updated to be perpendicular to the two other axis.
	 * 
	 * @param x
	 * @param y
	 * @param z
	 * @param extent
	 */
	@Override
	public void setFirstAxis(double x, double y, double z, double extent) {
		setFirstAxis(x, y, z, extent, CoordinateSystem3D.getDefaultCoordinateSystem());
	}

	/** Set the first axis of the second .
	 * The third axis is updated to be perpendicular to the two other axis.
	 * 
	 * @param x
	 * @param y
	 * @param z
	 * @param extent
	 * @param system
	 */
	@Override
	public void setFirstAxis(double x, double y, double z, double extent, CoordinateSystem3D system) {
		this.axis1.set(x, y, z);
		assert(this.axis1.isUnitVector());
		if (system.isLeftHanded()) {
			this.axis3.set(this.axis1.crossLeftHand(this.axis2));
		} else {
			this.axis3.set(this.axis3.crossRightHand(this.axis2));
		}
		this.extent1 = extent;
	}

	/** Set the second axis of the box.
	 * The third axis is updated to be perpendicular to the two other axis.
	 * 
	 * @param axis - the new values for the first axis.
	 */
	@Override
	public void setSecondAxis(Vector3D axis) {
		setSecondAxis(axis.getX(), axis.getY(), axis.getZ(), getSecondAxisExtent());
	}

	/** Set the second axis of the box.
	 * The third axis is updated to be perpendicular to the two other axis.
	 * 
	 * @param axis - the new values for the first axis.
	 * @param extent - the extent of the axis.
	 */
	@Override
	public void setSecondAxis(Vector3D axis, double extent) {
		setSecondAxis(axis.getX(), axis.getY(), axis.getZ(), extent);
	}

	/** Set the second axis of the box.
	 * The third axis is updated to be perpendicular to the two other axis.
	 * 
	 * @param x - the new values for the second axis.
	 * @param y - the new values for the second axis.
	 * @param z - the new values for the second axis.
	 */
	@Override
	public void setSecondAxis(double x, double y, double z) {
		setSecondAxis(x, y, z, getSecondAxisExtent());
	}

	/** Set the second axis of the box.
	 * The third axis is updated to be perpendicular to the two other axis.
	 * 
	 * @param x
	 * @param y
	 * @param z
	 * @param extent
	 */
	@Override
	public void setSecondAxis(double x, double y, double z, double extent) {
		setSecondAxis(x, y, z, extent, CoordinateSystem3D.getDefaultCoordinateSystem());
	}

	/** Set the second axis of the box.
	 * The third axis is updated to be perpendicular to the two other axis.
	 * 
	 * @param x
	 * @param y
	 * @param z
	 * @param extent
	 * @param system
	 */
	@Override
	public void setSecondAxis(double x, double y, double z, double extent, CoordinateSystem3D system) {
		this.axis2.set(x, y, z);
		assert(this.axis2.isUnitVector());
		if (system.isLeftHanded()) {
			this.axis3.set(this.axis1.crossLeftHand(this.axis2));
		} else {
			this.axis3.set(this.axis3.crossRightHand(this.axis2));
		}
		this.extent2 = extent;
	}

	/**
	 * Change the attributes of the oriented box.
	 * <p>
	 * The thirds axis is computed from the cross product with the two other axis.
	 * The cross product may be {@link Vector3f#crossLeftHand(org.arakhne.afc.math.geometry.d3.Vector3D)}
	 * or {@link Vector3f#crossRightHand(org.arakhne.afc.math.geometry.d3.Vector3D)} according to
	 * {@link CoordinateSystem3D#getDefaultCoordinateSystem()}.
	 *
	 * @param cx x coordinate of the box center.
	 * @param cy y coordinate of the box center.
	 * @param cz z coordinate of the box center.
	 * @param axis1x x coordinate of the first axis of the box.
	 * @param axis1y y coordinate of the first axis of the box.
	 * @param axis1z z coordinate of the first axis of the box.
	 * @param axis2x x coordinate of the second axis of the box.
	 * @param axis2y y coordinate of the secons axis of the box.
	 * @param axis2z z coordinate of the second axis of the box.
	 * @param axis1Extent extent of the first axis.
	 * @param axis2Extent extent of the second axis.
	 * @param axis3Extent extent of the third axis.
	 */
	@Override
	public void set(
			double cx, double cy, double cz,
			double axis1x, double axis1y, double axis1z,
			double axis2x, double axis2y, double axis2z,
			double axis1Extent, double axis2Extent, double axis3Extent) {
		set(cx, cy, cz,
				axis1x, axis1y, axis1z,
				axis2x, axis2y, axis2z,
				axis1Extent, axis2Extent, axis3Extent,
				CoordinateSystem3D.getDefaultCoordinateSystem());
	}

	/**
	 * Change the attributes of the oriented box.
	 * <p>
	 * The thirds axis is computed from the cross product with the two other axis.
	 * The cross product may be {@link Vector3f#crossLeftHand(org.arakhne.afc.math.geometry.d3.Vector3D)}
	 * or {@link Vector3f#crossRightHand(org.arakhne.afc.math.geometry.d3.Vector3D)} according to
	 * the given <code>system</code>.
	 *
	 * @param cx x coordinate of the box center.
	 * @param cy y coordinate of the box center.
	 * @param cz z coordinate of the box center.
	 * @param axis1x x coordinate of the first axis of the box.
	 * @param axis1y y coordinate of the first axis of the box.
	 * @param axis1z z coordinate of the first axis of the box.
	 * @param axis2x x coordinate of the second axis of the box.
	 * @param axis2y y coordinate of the secons axis of the box.
	 * @param axis2z z coordinate of the second axis of the box.
	 * @param axis1Extent extent of the first axis.
	 * @param axis2Extent extent of the second axis.
	 * @param axis3Extent extent of the third axis.
	 * @param system the coordinate system to use for computing the third axis.
	 */
	@Override
	public void set(
			double cx, double cy, double cz,
			double axis1x, double axis1y, double axis1z,
			double axis2x, double axis2y, double axis2z,
			double axis1Extent, double axis2Extent, double axis3Extent,
			CoordinateSystem3D system) {
		assert (system != null);
		this.center.set(cx, cy, cz);
		this.axis1.set(axis1x, axis1y, axis1z);
		this.axis2.set(axis2x, axis2y, axis2z);
		if (system.isLeftHanded()) {
			this.axis3.set(this.axis1.crossLeftHand(this.axis2));
		} else {
			this.axis3.set(this.axis1.crossRightHand(this.axis2));
		}
		this.extent1 = axis1Extent;
		this.extent2 = axis2Extent;
		this.extent3 = axis3Extent;
	}

	@Override
	public PathIterator3f getPathIterator(Transform3D transform) {
		// TODO Auto-generated method stub
		return null;
	}
}
