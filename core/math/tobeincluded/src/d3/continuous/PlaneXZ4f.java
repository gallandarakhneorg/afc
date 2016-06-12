package org.arakhne.afc.math.geometry.d3.continuous;

import org.arakhne.afc.math.geometry.d3.Tuple3D;
import org.eclipse.xtext.xbase.lib.Pure;

public class PlaneXZ4f extends AbstractPlaneXZ4F {
	
	private static final long serialVersionUID = -7934255044741635384L;
	
	
	/** Is the coordinate of the plane.
	 */
	protected double y;

	/**
	 * @param y1 is the coordinate of the plane
	 */
	public PlaneXZ4f(double y1) {
		this.y = y1;
	}
	
	/**
	 * @param p is a point on the plane.
	 */
	public PlaneXZ4f(Tuple3D<?> p) {
		this.y = p.getY();
	}

	/** {@inheritDoc}
	 */
	@Override
	public void set(Plane3D<?> plane) {
		this.y = plane.getEquationComponentB();
		normalize();
	}

	@Pure
	@Override
	public Point3f getProjection(double x, double y1, double z) {
		return new Point3f(x, this.y, z);
	}

	@Override
	public void setPivot(double x, double y1, double z) {
		this.y = y1;
	}

	/** Set the y coordinate of the plane.
	 *
	 * @param y1
	 */
	public void setY(double y1) {
		this.y = y1;
	}

	/** Replies the y coordinate of the plane.
	 *
	 */
	@Pure
	public double getY() {
		return this.y;
	}

	
	
	
}
