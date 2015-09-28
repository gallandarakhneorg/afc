package org.arakhne.afc.math.geometry.d3.continuous;

import org.arakhne.afc.math.geometry.d3.Point3D;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

@SuppressWarnings("restriction")
public class Sphere3d extends AbstractSphere3F {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -77754164566103719L;

	/** X-coordinate of the sphere center. */
	protected DoubleProperty cxProperty = new SimpleDoubleProperty(0f);

	/** Y-coordinate of the sphere center. */
	protected DoubleProperty cyProperty = new SimpleDoubleProperty(0f);

	/** Z-coordinate of the sphere center. */
	protected DoubleProperty czProperty = new SimpleDoubleProperty(0f);

	/** Radius of the sphere center (must be always positive). */
	protected DoubleProperty radiusProperty = new SimpleDoubleProperty(0f);

	/**
	 */
	public Sphere3d() {
		//
	}

	/**
	 * @param center
	 * @param radius1
	 */
	public Sphere3d(Point3D center, double radius1) {
		set(center, radius1);
	}

	/**
	 * @param x
	 * @param y
	 * @param z
	 * @param radius1
	 */
	public Sphere3d(double x, double y, double z, double radius1) {
		set(x, y, z, radius1);
	}
	
	/** Construct a sphere from a sphere.
	 * @param c
	 */
	public Sphere3d(AbstractSphere3F c) {
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
	@Override
	public double getX() {
		return this.cxProperty.doubleValue();
	}

	/** Replies the center y.
	 * 
	 * @return the center y.
	 */
	@Override
	public double getY() {
		return this.cyProperty.doubleValue();
	}

	/** Replies the center z.
	 * 
	 * @return the center z.
	 */
	@Override
	public double getZ() {
		return this.czProperty.doubleValue();
	}

	/** Replies the center.
	 * 
	 * @return a copy of the center.
	 */
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

	

}
