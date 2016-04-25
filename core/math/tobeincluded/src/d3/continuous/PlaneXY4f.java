package org.arakhne.afc.math.geometry.d3.continuous;

import org.arakhne.afc.math.geometry.d3.Tuple3D;
import org.eclipse.xtext.xbase.lib.Pure;

public class PlaneXY4f extends AbstractPlaneXY4F{
	

	private static final long serialVersionUID = -2561113485612246988L;
	
	
	/**
	 * Coordinate of the plane.
	 */
	protected double z;

	/**
	 * @param z1 is the coordinate of the plane.
	 */
	public PlaneXY4f(double z1) {
		this.z = z1;
	}

	/**
	 * @param p is a point on the plane.
	 */
	public PlaneXY4f(Tuple3D<?> p) {
		this.z = p.getZ();
	}

	/** {@inheritDoc}
	 */
	@Override
	public void set(Plane3D<?> plane) {
		this.z = plane.getEquationComponentC();
		normalize();
	}

	@Pure
	@Override
	public Point3f getProjection(double x, double y, double z1) {
		return new Point3f(x, y, this.z);
	}

	@Override
	public void setPivot(double x, double y, double z1) {
		this.z = z1;
	}

	/** Set the z coordinate of the plane.
	 *
	 * @param z1
	 */
	public void setZ(double z1) {
		this.z = z1;
	}

	/** Replies the z coordinate of the plane.
	 *
	 */
	@Pure
	public double getZ() {
		return this.z;
	}
	
	

}
