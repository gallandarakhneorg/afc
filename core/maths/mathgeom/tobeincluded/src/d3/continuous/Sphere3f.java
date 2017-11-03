package org.arakhne.afc.math.geometry.d3.continuous;

import org.arakhne.afc.math.geometry.d3.Point3D;
import org.eclipse.xtext.xbase.lib.Pure;

public class Sphere3f extends AbstractSphere3F {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2838093667819692460L;

	/** X-coordinate of the sphere center. */
	protected double cx = 0f;

	/** Y-coordinate of the sphere center. */
	protected double cy = 0f;

	/** Z-coordinate of the sphere center. */
	protected double cz = 0f;

	/** Radius of the sphere center (must be always positive). */
	protected double radius = 0f;

	/**
	 */
	public Sphere3f() {
		//
	}

	/**
	 * @param center
	 * @param radius1
	 */
	public Sphere3f(Point3D center, double radius1) {
		set(center, radius1);
	}

	/**
	 * @param x
	 * @param y
	 * @param z
	 * @param radius1
	 */
	public Sphere3f(double x, double y, double z, double radius1) {
		set(x, y, z, radius1);
	}
	
	/** Construct a sphere from a sphere.
	 * @param c
	 */
	public Sphere3f(AbstractSphere3F c) {
		this.cx = c.getX();
		this.cy = c.getY();
		this.cz = c.getZ();
		this.radius = c.getRadius();
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
		this.cx = x;
		this.cy = y;
		this.cz = z;
		this.radius = Math.abs(radius1);
	}

	/** Change the frame of te sphere.
	 * 
	 * @param center
	 * @param radius1
	 */
	@Override
	public void set(Point3D center, double radius1) {
		this.cx = center.getX();
		this.cy = center.getY();
		this.cz = center.getZ();
		this.radius = Math.abs(radius1);
	}

	/** Replies the center X.
	 * 
	 * @return the center x.
	 */
	@Pure
	@Override
	public double getX() {
		return this.cx;
	}

	/** Replies the center y.
	 * 
	 * @return the center y.
	 */
	@Pure
	@Override
	public double getY() {
		return this.cy;
	}

	/** Replies the center z.
	 * 
	 * @return the center z.
	 */
	@Pure
	@Override
	public double getZ() {
		return this.cz;
	}

	/** Replies the center.
	 * 
	 * @return a copy of the center.
	 */
	@Pure
	@Override
	public Point3f getCenter() {
		return new Point3f(this.cx, this.cy, this.cz);
	}

	/** Change the center.
	 * 
	 * @param center
	 */
	@Override
	public void setCenter(Point3D center) {
		this.cx = center.getX();
		this.cy = center.getY();
		this.cz = center.getZ();
	}

	/** Change the center.
	 * 
	 * @param x
	 * @param y
	 * @param z
	 */
	@Override
	public void setCenter(double x, double y, double z) {
		this.cx = x;
		this.cy = y;
		this.cz = z;
	}

	/** Replies the radius.
	 * 
	 * @return the radius.
	 */
	@Pure
	@Override
	public double getRadius() {
		return this.radius;
	}

	/** Set the radius.
	 * 
	 * @param radius1 is the radius.
	 */
	@Override
	public void setRadius(double radius1) {
		this.radius = Math.abs(radius1);
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
