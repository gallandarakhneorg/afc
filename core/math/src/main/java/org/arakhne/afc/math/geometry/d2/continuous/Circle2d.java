package org.arakhne.afc.math.geometry.d2.continuous;

import org.arakhne.afc.math.geometry.d2.Point2D;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.DoubleProperty;

@SuppressWarnings("restriction")
public class Circle2d extends AbstractCircle2F<Circle2d>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4084538191693161101L;
	
	
	/** X-coordinate of the circle center. */
	protected DoubleProperty cxProperty = new SimpleDoubleProperty(0f);
	/** Y-coordinate of the circle center. */
	protected DoubleProperty cyProperty = new SimpleDoubleProperty(0f);
	/** Radius of the circle center (must be always positive). */
	protected DoubleProperty radiusProperty = new SimpleDoubleProperty(0f);

	/**
	 */
	public Circle2d() {
		//
	}

	/**
	 * @param center
	 * @param radius1
	 */
	public Circle2d(Point2D center, double radius1) {
		this.set(center, radius1);
	}

	/**
	 * @param x
	 * @param y
	 * @param radius1
	 */
	public Circle2d(double x, double y, double radius1) {
		this.set(x, y, radius1);
	}
	
	/** Construct a circle from a circle.
	 * @param c
	 */
	public Circle2d(Circle2d c) {
		this.set(c.getX(),c.getY(),c.getRadius());
	}

	@Override
	public void clear() {
		this.cxProperty.set(0f); 
		this.cyProperty.set(0f); 
		this.radiusProperty.set(0f); 
	}
	

	/** Change the frame of the circle.
	 * 
	 * @param x
	 * @param y
	 * @param radius1
	 */
	public void set(double x, double y, double radius1) {
		this.cxProperty.set(x);
		this.cyProperty.set(y);
		this.radiusProperty.set(Math.abs(radius1));
	}

	/** Change the frame of te circle.
	 * 
	 * @param center
	 * @param radius1
	 */
	public void set(Point2D center, double radius1) {
		this.cxProperty.set(center.getX());
		this.cyProperty.set(center.getY());
		this.radiusProperty.set(Math.abs(radius1));
	}

	@Override
	public void set(Shape2F s) {
		if (s instanceof Circle2d) {
			Circle2d c = (Circle2d) s;
			set(c.getX(), c.getY(), c.getRadius());
		} else {
			Rectangle2f r = s.toBoundingBox();
			set(r.getCenterX(), r.getCenterY(),
					Math.min(r.getWidth(), r.getHeight()) / 2.);
		}
	}

	/** Replies the center X.
	 * 
	 * @return the center x.
	 */
	public double getX() {
		return this.cxProperty.doubleValue();
	}

	/** Replies the center y.
	 * 
	 * @return the center y.
 */
	public double getY() {
		return this.cyProperty.doubleValue();
	}

	/** Replies the center.
	 * 
	 * @return a copy of the center.
	 */
	public Point2f getCenter() {
		return new Point2f(this.getX(), this.getY());
	}

	/** Change the center.
	 * 
	 * @param center
	 */
	public void setCenter(Point2D center) {
		this.cxProperty.set(center.getX());
		this.cyProperty.set(center.getY());
	}

	/** Change the center.
	 * 
	 * @param x
	 * @param y
	 */
	public void setCenter(double x, double y) {
		this.cxProperty.set(x);
		this.cyProperty.set(y);
	}

	/** Replies the radius.
	 * 
	 * @return the radius.
	 */
	public double getRadius() {
		return this.radiusProperty.doubleValue();
	}

	/** Set the radius.
	 * 
	 * @param radius1 is the radius.
	 */
	public void setRadius(double radius1) {
		this.radiusProperty.set(Math.abs(radius1));
	}


}
