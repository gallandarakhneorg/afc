/* 
 * $Id$
 * 
 * Copyright (C) 2010-2013 Christophe BOHRHAUER.
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 * This program is free software; you can redistribute it and/or modify
 */
package org.arakhne.afc.math.geometry.d3.continuous;

import org.arakhne.afc.math.geometry.d3.Point3D;
import org.arakhne.afc.math.geometry.d3.Vector3D;

/** This class represents a 3D line.
 * <p>
 * The equation of the line is:
 * <math xmlns="http://www.w3.org/1998/Math/MathML">
 *   <mrow>
 *     <mi>L</mi><mo>&#x2061;</mo><mfenced><mi>t</mi></mfenced>
 *     <mo>=</mo>
 *     <mi>P</mi><mo>+</mo>
 *     <mi>t</mi><mo>.</mo>
 *     <mover>
 *       <mi>D</mi>
 *       <mo>&#x20D7;</mo>
 *     </mover>
 *   </mrow>
 * </math>
 * for any real-valued <math><mi>t</mi></math>.
 * <math><mover><mi>D</mi><mo>&#x20D7;</mo></mover></math> is not
 * necessarily unit length. 
 *
 * @author $Author: hjaffali$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class Segment3f extends AbstractSegment3F {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7672097812255939548L;

	/** First point on the line.
	 */
	protected final Point3f pivot = new Point3f();

	/** Direction vector.
	 */
	protected final Vector3f d = new Vector3f();

	/**
	 */
	public Segment3f() {
		super();
	}

	/**
	 * @param p1 is first point on the line
	 * @param p2 is second point on the line
	 */
	public Segment3f(Point3D p1, Point3D p2) {
		this.pivot.set(p1);
		this.d.sub(p2, p1);
	}

	/**
	 * @param pivot1 is a point on the line
	 * @param direction is the direction of the line
	 */
	public Segment3f(Point3D pivot1, Vector3D direction) {
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
	public Segment3f(double x1, double y1, double z1, double x2, double y2, double z2) {
		this.pivot.set(x1, y1, z1);
		this.d.set(x2 - x1, y2 - y1, z2 - z1);
	}

	
	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public Vector3f getSegmentVector() {
		return this.d;
	}
	
	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public Vector3f getCloneSegmentVector() {
		return this.d.clone();
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public Vector3f getDirection() {
		Vector3f v = this.d.clone();
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
	public Point3f getP1() {
		return this.pivot;
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	public double getX1() {
		return this.pivot.getX();
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	public double getY1() {
		return this.pivot.getY();
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
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
	public Point3f getP2() {
		return new Point3f(
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

	@Override
	public PathIterator3f getPathIterator(Transform3D transform) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PathIterator3f getPathIterator() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	
}
