package org.arakhne.afc.math.geometry.d3.continuous;

import org.arakhne.afc.math.geometry.d3.Tuple3D;

public class PlaneYZ4f extends AbstractPlaneYZ4F {
	
	
	private static final long serialVersionUID = 273926008552432345L;
	
		
	/** Coordinate of the plane.
	 */
	private double x;

	/**
	 * @param x1 is the coordinate of the plane.
	 */
	public PlaneYZ4f(double x1) {
		this.x = x1;
	}
	
	/**
	 * @param p is a point on the plane.
	 */
	public PlaneYZ4f(Tuple3D<?> p) {
		this.x = p.getX();
	}

	/** {@inheritDoc}
	 */
	@Override
	public void set(Plane3D<?> plane) {
		this.x = plane.getEquationComponentA();
		normalize();
	}
	
	@Override
	public Point3f getProjection(double x1, double y, double z) {
		return new Point3f(this.x, y, z);
	}

	@Override
	public void setPivot(double x1, double y, double z) {
		this.x = x1;
	}
	
	/** Set the x coordinate of the plane.
	 *
	 * @param x1
	 */
	public void setX(double x1) {
		this.x = x1;
	}

	/** Replies the x coordinate of the plane.
	 *
	 */
	public double getX() {
		return this.x;
	}
	
	

}
