package org.arakhne.afc.math.geometry.d3.continuous;

import org.arakhne.afc.math.geometry.d3.Tuple3D;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

public class PlaneXY4d extends AbstractPlaneXY4F{
	

	private static final long serialVersionUID = 4944742708014964987L;
	
	/**
	 * Coordinate of the plane.
	 */
	protected DoubleProperty zProperty = new SimpleDoubleProperty(0f);

	/**
	 * @param z1 is the coordinate of the plane.
	 */
	public PlaneXY4d(double z1) {
		this.setZ(z1);
	}

	/**
	 * @param p is a point on the plane.
	 */
	public PlaneXY4d(Tuple3D<?> p) {
		this.setZ(p.getZ());
	}

	/** {@inheritDoc}
	 */
	@Override
	public void set(Plane3D<?> plane) {
		this.setZ(plane.getEquationComponentC());
		normalize();
	}
	
	@Override
	public Point3f getProjection(double x, double y, double z1) {
		return new Point3f(x, y, this.getZ());
	}

	@Override
	public void setPivot(double x, double y, double z1) {
		this.setZ(z1);
	}

	/** Set the z coordinate of the plane.
	 *
	 * @param z1
	 */
	public void setZ(double z1) {
		this.zProperty.set(z1);
	}

	/** Replies the z coordinate of the plane.
	 *
	 */
	public double getZ() {
		return this.zProperty.doubleValue();
	}
	
	

}

