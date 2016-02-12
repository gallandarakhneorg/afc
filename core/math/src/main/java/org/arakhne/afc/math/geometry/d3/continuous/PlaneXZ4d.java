package org.arakhne.afc.math.geometry.d3.continuous;

import org.arakhne.afc.math.geometry.d3.Tuple3D;
import org.eclipse.xtext.xbase.lib.Pure;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

public class PlaneXZ4d extends AbstractPlaneXZ4F {

	private static final long serialVersionUID = 4256782058890686993L;
	
	
	/**
	 * Coordinate of the plane.
	 */
	protected DoubleProperty yProperty;

	/**
	 * @param y1 is the coordinate of the plane.
	 */
	public PlaneXZ4d(double y1) {
		this.yProperty = new SimpleDoubleProperty(y1);
		normalize();
	}

	/**
	 * @param y1 is the coordinate of the plane.
	 */
	public PlaneXZ4d(DoubleProperty y1) {
		this.yProperty = y1;
		normalize();
	}
	
	/**
	 * @param p is a point on the plane.
	 */
	public PlaneXZ4d(Tuple3D<?> p) {
		this(p.getY());
	}

	/**
	 * @param p is a point on the plane.
	 */
	public PlaneXZ4d(Tuple3d<?> p) {
		this(p.yProperty);
	}
	
	/** {@inheritDoc}
	 */
	@Override
	public void set(Plane3D<?> plane) {
		this.yProperty.set(plane.getEquationComponentB());
		normalize();
	}

	/** {@inheritDoc}
	 */
	public void setProperties(Plane4d plane) {
		this.setYProperty(plane.bProperty);
		normalize();
	}
	
	@Pure
	@Override
	public Point3d getProjection(double x, double y1, double z) {
		return new Point3d(x, this.getY(), z);
	}

	@Override
	public void setPivot(double x, double y1, double z) {
		this.yProperty.set(y1);
	}

	public void setPivot(Point3d pivot) {
		this.setYProperty(pivot.yProperty);
	}
	
	/** Set the y coordinate of the plane.
	 *
	 * @param y1
	 */
	public void setY(double y1) {
		this.yProperty.set(y1);
	}

	/** Set the y coordinate of the plane.
	 *
	 * @param y1
	 */
	public void setYProperty(DoubleProperty y1) {
		this.yProperty = y1;
	}
	
	/** Replies the y coordinate of the plane.
	 *
	 */
	@Pure
	public double getY() {
		return this.yProperty.doubleValue();
	}

}
