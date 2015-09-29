package org.arakhne.afc.math.geometry.d3.continuous;

import org.arakhne.afc.math.geometry.d3.Tuple3D;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

@SuppressWarnings("restriction")
public class PlaneYZ4d extends AbstractPlaneYZ4F {
	
	private static final long serialVersionUID = 8832834657138806977L;
	
	
	/** Coordinate of the plane.
	 */
	private DoubleProperty xProperty = new SimpleDoubleProperty(0f);

	/**
	 * @param x1 is the coordinate of the plane.
	 */
	public PlaneYZ4d(double x1) {
		this.setX(x1);
	}
	
	/**
	 * @param p is a point on the plane.
	 */
	public PlaneYZ4d(Tuple3D<?> p) {
		this.setX(p.getX());
	}

	/** {@inheritDoc}
	 */
	@Override
	public void set(Plane3D<?> plane) {
		this.setX(plane.getEquationComponentA());
		normalize();
	}
	
	@Override
	public Point3d getProjection(double x1, double y, double z) {
		return new Point3d(this.getX(), y, z);
	}

	@Override
	public void setPivot(double x1, double y, double z) {
		this.xProperty.set(x1);
	}
	
	/** Set the x coordinate of the plane.
	 *
	 * @param x1
	 */
	public void setX(double x1) {
		this.xProperty.set(x1);
	}

	/** Replies the x coordinate of the plane.
	 *
	 */
	public double getX() {
		return this.xProperty.doubleValue();
	}
	

}

