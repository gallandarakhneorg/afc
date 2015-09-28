package org.arakhne.afc.math.geometry.d3.continuous;

import org.arakhne.afc.math.geometry.d3.Point3D;
import org.arakhne.afc.math.geometry.d3.Vector3D;

public class Segment3d extends AbstractSegment3F {

	/**
	 * 
	 */
	private static final long serialVersionUID = -451687409923159781L;

	/** First point on the line.
	 */
	protected final Point3d pivot = new Point3d();

	/** Direction vector.
	 */
	protected final Vector3d d = new Vector3d();

	/**
	 */
	public Segment3d() {
		super();
	}

	/**
	 * @param p1 is first point on the line
	 * @param p2 is second point on the line
	 */
	public Segment3d(Point3D p1, Point3D p2) {
		this.pivot.set(p1);
		this.d.sub(p2, p1);
	}

	/**
	 * @param pivot1 is a point on the line
	 * @param direction is the direction of the line
	 */
	public Segment3d(Point3D pivot1, Vector3D direction) {
		this.pivot.set(pivot1);
		this.d.set(direction);
	}

	/**
	 * @param x1 x coordinate of the first point of the segment.
	 * @param y1 y coordinate of the first point of the segment.
	 * @param z1 z coordinate of the first point of the segment.
	 * @param x2 x coordinate of the second point of the segment.
	 * @param y2 y coordinate of the second point of the segment.
	 * @param z2 z coordinate of the second point of the segment.
	 */
	public Segment3d(double x1, double y1, double z1, double x2, double y2, double z2) {
		this.pivot.set(x1, y1, z1);
		this.d.set(x2 - x1, y2 - y1, z2 - z1);
	}

	
	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public Vector3d getSegmentVector() {
		return this.d;
	}
	
	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public Vector3d getCloneSegmentVector() {
		return this.d.clone();
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public Vector3d getDirection() {
		Vector3d v = this.d.clone();
		v.normalize();
		return v;
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public void set(Point3D p1, Point3D p2) {
		this.pivot.set(p1);
		this.d.sub(p2, p1);
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public void set(double x1, double y1, double z1, double x2, double y2, double z2) {
		this.pivot.set(x1, y1, z1);
		this.d.set(x2 - x1, y2 - y1, z2 - z1);
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public Point3d getP1() {
		return this.pivot;
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public double getX1() {
		return this.pivot.getX();
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public double getY1() {
		return this.pivot.getY();
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public double getZ1() {
		return this.pivot.getZ();
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public void setP1(Point3D p) {
		this.pivot.set(p);
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public void setP1(double x, double y, double z) {
		this.pivot.set(x, y, z);
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public void setP2(double x, double y, double z) {
		this.d.set(x - this.pivot.getX(), y - this.pivot.getY(), z - this.pivot.getZ());
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public Point3d getP2() {
		return new Point3d(
				this.pivot.getX() + this.d.getX(),
				this.pivot.getY() + this.d.getY(),
				this.pivot.getZ() + this.d.getZ());
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public double getX2() {
		return this.pivot.getX() + this.d.getX();
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public double getY2() {
		return this.pivot.getX() + this.d.getX();
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public double getZ2() {
		return this.pivot.getZ() + this.d.getZ();
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public void setP2(Point3D p) {
		this.d.sub(p, this.pivot);
	}
	
	
	
}

