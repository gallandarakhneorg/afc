package org.arakhne.afc.math.geometry.d3.continuous;

import org.arakhne.afc.math.geometry.d3.Tuple3D;
import org.eclipse.xtext.xbase.lib.Pure;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

public class PlaneYZ4d extends AbstractPlaneYZ4F {
	
	private static final long serialVersionUID = 8832834657138806977L;
	
	
	/**
	 * Coordinate of the plane.
	 */
	protected DoubleProperty xProperty;

	/**
	 * @param x1 is the coordinate of the plane.
	 */
	public PlaneYZ4d(double x1) {
		this.xProperty = new SimpleDoubleProperty(x1);
		normalize();
	}

	/**
	 * @param x1 is the coordinate of the plane.
	 */
	public PlaneYZ4d(DoubleProperty x1) {
		this.xProperty = x1;
		normalize();
	}
	
	/**
	 * @param p is a point on the plane.
	 */
	public PlaneYZ4d(Tuple3D<?> p) {
		this(p.getX());
	}

	/**
	 * @param p is a point on the plane.
	 */
	public PlaneYZ4d(Tuple3d<?> p) {
		this(p.xProperty);
	}
	
	/** {@inheritDoc}
	 */
	@Override
	public void set(Plane3D<?> plane) {
		this.setX(plane.getEquationComponentA());
		normalize();
	}

	/** {@inheritDoc}
	 */
	public void setProperties(Plane4d plane) {
		this.setXProperty(plane.aProperty);
		normalize();
	}
	
	@Pure
	@Override
	public Point3d getProjection(double x1, double y, double z) {
		return new Point3d(this.getX(), y, z);
	}

	@Override
	public void setPivot(double x1, double y, double z) {
		this.xProperty.set(x1);
	}
	
	public void setPivot(Point3d pivot) {
		this.setXProperty(pivot.xProperty);
	}
	
	/** Set the x coordinate of the plane.
	 *
	 * @param x1
	 */
	public void setX(double x1) {
		this.xProperty.set(x1);
	}

	/** Set the x coordinate of the plane.
	 *
	 * @param x1
	 */
	public void setXProperty(DoubleProperty x1) {
		this.xProperty = x1;
	}
	
	/** Replies the x coordinate of the plane.
	 *
	 */
	@Pure
	public double getX() {
		return this.xProperty.doubleValue();
	}
	

}

