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
	 * @param center
	 * @param radius1
	 */
	public Sphere3d(Point3d center, DoubleProperty radius1) {
		this();
		setProperties(center,radius1);
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
	
	/**
	 * @param x
	 * @param y
	 * @param z
	 * @param radius1
	 */
	public Sphere3d(DoubleProperty x, DoubleProperty y, DoubleProperty z, DoubleProperty radius1) {
		this();
		setProperties(x, y, z, radius1);
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
	
	/** Bind a sphere from a sphere.
	 * @param c
	 */
	public Sphere3d(Sphere3d c) {
		this();
		setProperties(c.cxProperty,c.cyProperty,c.czProperty,c.radiusProperty);
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
	
	/** Change the frame of the sphere.
	 * 
	 * @param x
	 * @param y
	 * @param z
	 * @param radius1
	 */
	public void setProperties(DoubleProperty x, DoubleProperty y, DoubleProperty z, DoubleProperty radius1) {
		this.cxProperty = x;
		this.cyProperty = y;
		this.czProperty = z;
		this.radiusProperty = radius1;
		this.radiusProperty.set(Math.abs(this.radiusProperty.get()));
	}

	/** Change the frame of the sphere.
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
	
	/** Bind the frame of the sphere with center and radisu properties.
	 * 
	 * @param center
	 * @param radius1
	 */
	public void setProperties(Point3d center, DoubleProperty radius1) {
		setProperties(center.xProperty,center.yProperty,center.zProperty, radius1);
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
	public Point3d getCenter() {
		return new Point3d(this.cxProperty, this.cyProperty, this.czProperty);
	}
	
	/** Replies the center.
	 * 
	 * @return a copy of the center.
	 */
	@Pure
	public Point3f getCenterWithoutProperties() {
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
	
	/** Set the center properties with the properties of the Point3d in parameter.
	 * 
	 * @param center
	 */
	public void setCenterProperties(Point3d center) {
		this.cxProperty = center.xProperty;
		this.cyProperty = center.yProperty;
		this.czProperty = center.zProperty;
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
	
	/** Set the center properties with the properties in parameter.
	 * 
	 * @param x
	 * @param y
	 * @param z
	 */
	public void setCenterProperties(DoubleProperty x, DoubleProperty y, DoubleProperty z) {
		this.cxProperty = x;
		this.cyProperty = y;
		this.czProperty = z;
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
	
	/** Set the radius property with the property in parameter.
	 * 
	 * @param radius1 is the radius.
	 */
	public void setRadiusProperty(DoubleProperty radius1) {
		this.radiusProperty = radius1;
		this.radiusProperty.set(Math.abs(this.radiusProperty.get()));
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
