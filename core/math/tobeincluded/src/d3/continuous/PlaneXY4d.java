package org.arakhne.afc.math.geometry.d3.continuous;

import org.arakhne.afc.math.geometry.d3.Tuple3D;
import org.eclipse.xtext.xbase.lib.Pure;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

public class PlaneXY4d extends AbstractPlaneXY4F{
	

	private static final long serialVersionUID = 4944742708014964987L;
	
	/**
	 * Coordinate of the plane.
	 */
	protected DoubleProperty zProperty;

	/**
	 * @param z1 is the coordinate of the plane.
	 */
	public PlaneXY4d(double z1) {
		this.zProperty = new SimpleDoubleProperty(z1);
		normalize();
	}
	
	/**
	 * @param z1 is the coordinate of the plane.
	 */
	public PlaneXY4d(DoubleProperty z1) {
		this.zProperty = z1;
		normalize();
	}

	/**
	 * @param p is a point on the plane.
	 */
	public PlaneXY4d(Tuple3D<?> p) {
		this(p.getZ());
	}
	
	/**
	 * @param p is a point on the plane.
	 */
	public PlaneXY4d(Tuple3d<?> p) {
		this(p.zProperty);
	}

	/** {@inheritDoc}
	 */
	@Override
	public void set(Plane3D<?> plane) {
		this.setZ(plane.getEquationComponentC());
		normalize();
	}
	
	/** {@inheritDoc}
	 */
	public void setProperties(Plane4d plane) {
		this.setZProperty(plane.cProperty);
		normalize();
	}

	@Pure
	@Override
	public Point3d getProjection(double x, double y, double z1) {
		return new Point3d(x, y, this.getZ());
	}

	@Override
	public void setPivot(double x, double y, double z1) {
		this.setZ(z1);
	}
	
	public void setPivot(Point3d pivot) {
		this.setZProperty(pivot.zProperty);
	}

	/** Set the z coordinate of the plane.
	 *
	 * @param z1
	 */
	public void setZ(double z1) {
		this.zProperty.set(z1);
	}
	
	/** Set the z coordinate of the plane.
	 *
	 * @param z1
	 */
	public void setZProperty(DoubleProperty z1) {
		this.zProperty = z1;
	}

	/** Replies the z coordinate of the plane.
	 *
	 */
	@Pure
	public double getZ() {
		return this.zProperty.get();
	}
	

}

