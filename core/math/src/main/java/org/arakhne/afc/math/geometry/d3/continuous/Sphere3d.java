package org.arakhne.afc.math.geometry.d3.continuous;

import org.arakhne.afc.math.geometry.d3.Point3D;
import org.eclipse.xtext.xbase.lib.Pure;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

public class Sphere3d extends AbstractSphere3F {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -77754164566103719L;

	/** X-coordinate of the sphere center. */
	protected DoubleProperty cxProperty;

	/** Y-coordinate of the sphere center. */
	protected DoubleProperty cyProperty;

	/** Z-coordinate of the sphere center. */
	protected DoubleProperty czProperty;

	/** Radius of the sphere center (must be always positive). */
	protected DoubleProperty radiusProperty;

	/**
	 */
	public Sphere3d() {
		this.cxProperty = new SimpleDoubleProperty(0f);
		this.cyProperty = new SimpleDoubleProperty(0f);
		this.czProperty = new SimpleDoubleProperty(0f);
		this.radiusProperty = new SimpleDoubleProperty(0f);
	}

	/**
	 * @param center
	 * @param radius1
	 */
	public Sphere3d(Point3D center, double radius1) {
		this();
		set(center, radius1);
	}

	/**
	 * @param x
	 * @param y
	 * @param z
	 * @param radius1
	 */
	public Sphere3d(double x, double y, double z, double radius1) {
		this();
		set(x, y, z, radius1);
	}
	
	/** Construct a sphere from a sphere.
	 * @param c
	 */
	public Sphere3d(AbstractSphere3F c) {
		this();
		this.cxProperty.set(c.getX());
		this.cyProperty.set(c.getY());
		this.czProperty.set(c.getZ());
		this.radiusProperty.set(c.getRadius());
	}

	
	/** Change the frame of the sphere.
	 * 
	 * @param x
	 * @param y
	 * @param z
	 * @param radius1
	 */
	@Override
	public void set(double x, double y, double z, double radius1) {
		this.cxProperty.set(x);
		this.cyProperty.set(y);
		this.czProperty.set(z);
		this.radiusProperty.set(Math.abs(radius1));
	}

	/** Change the frame of te sphere.
	 * 
	 * @param center
	 * @param radius1
	 */
	@Override
	public void set(Point3D center, double radius1) {
		this.cxProperty.set(center.getX());
		this.cyProperty.set(center.getY());
		this.czProperty.set(center.getZ());
		this.radiusProperty.set(Math.abs(radius1));
	}

	/** Replies the center X.
	 * 
	 * @return the center x.
	 */
	@Pure
	@Override
	public double getX() {
		return this.cxProperty.doubleValue();
	}

	/** Replies the center y.
	 * 
	 * @return the center y.
	 */
	@Pure
	@Override
	public double getY() {
		return this.cyProperty.doubleValue();
	}

	/** Replies the center z.
	 * 
	 * @return the center z.
	 */
	@Pure
	@Override
	public double getZ() {
		return this.czProperty.doubleValue();
	}

	/** Replies the center.
	 * 
	 * @return a copy of the center.
	 */
	@Pure
	@Override
	public Point3f getCenter() {
		return new Point3f(this.cxProperty.doubleValue(), this.cyProperty.doubleValue(), this.czProperty.doubleValue());
	}

	/** Change the center.
	 * 
	 * @param center
	 */
	@Override
	public void setCenter(Point3D center) {
		this.cxProperty.set(center.getX());
		this.cyProperty.set(center.getY());
		this.czProperty.set(center.getZ());
	}

	/** Change the center.
	 * 
	 * @param x
	 * @param y
	 * @param z
	 */
	@Override
	public void setCenter(double x, double y, double z) {
		this.cxProperty.set(x);
		this.cyProperty.set(y);
		this.czProperty.set(z);
	}

	/** Replies the radius.
	 * 
	 * @return the radius.
	 */
	@Pure
	@Override
	public double getRadius() {
		return this.radiusProperty.doubleValue();
	}

	/** Set the radius.
	 * 
	 * @param radius1 is the radius.
	 */
	@Override
	public void setRadius(double radius1) {
		this.radiusProperty.set(Math.abs(radius1));
	}

	@Pure
	@Override
	public PathIterator3f getPathIterator(Transform3D transform) {
		// TODO Auto-generated method stub
		return null;
	}

	@Pure
	@Override
	public PathIterator3d getPathIteratorProperty(Transform3D transform) {
		// TODO Auto-generated method stub
		return null;
	}

}
