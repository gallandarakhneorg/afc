/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2016 The original authors, and other authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.arakhne.afc.math.geometry.d2.d;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.Transform2D;
import org.arakhne.afc.math.geometry.d2.afp.Triangle2afp;
import org.arakhne.afc.vmutil.asserts.AssertMessages;

/** A triangle with 2 double precision floating-point numbers.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public class Triangle2d
		extends AbstractShape2d<Triangle2d>
		implements Triangle2afp<Shape2d<?>, Triangle2d, PathElement2d, Point2d, Vector2d, Rectangle2d> {

	private static final long serialVersionUID = 6181771392780117178L;

	private double x1;

	private double y1;

	private double x2;

	private double y2;

	private double x3;

	private double y3;

	private Boolean isCCW;

	/** Construct an empty triangle.
	 */
	public Triangle2d() {
		//
	}

	/** Construct a triangle with the given 3 points.
	 * @param p1 first point.
	 * @param p2 second point.
	 * @param p3 third point.
	 */
	public Triangle2d(Point2D<?, ?> p1, Point2D<?, ?> p2, Point2D<?, ?> p3) {
		assert p1 != null : AssertMessages.notNullParameter(0);
		assert p2 != null : AssertMessages.notNullParameter(1);
		assert p3 != null : AssertMessages.notNullParameter(2);
		set(p1.getX(), p1.getY(), p2.getX(), p2.getY(), p3.getX(), p3.getY());
	}

	/** Construct a triangle with the given 3 points.
	 * @param x1 x coordinate of the first point.
	 * @param y1 y coordinate of the first point.
	 * @param x2 x coordinate of the second point.
	 * @param y2 y coordinate of the second point.
	 * @param x3 x coordinate of the third point.
	 * @param y3 y coordinate of the third point.
	 */
	public Triangle2d(double x1, double y1, double x2, double y2, double x3, double y3) {
		set(x1, y1, x2, y2, x3, y3);
	}

	/** Construct a triangle from a triangle.
	 * @param t the triangle to copy.
	 */
	public Triangle2d(Triangle2afp<?, ?, ?, ?, ?, ?> t) {
		assert t != null : AssertMessages.notNullParameter();
		set(t.getX1(), t.getY1(), t.getX2(), t.getY2(), t.getX3(), t.getY3());
	}

	@Pure
	@Override
	public int hashCode() {
		long bits = 1;
		bits = 31 * bits + Double.hashCode(this.x1);
		bits = 31 * bits + Double.hashCode(this.y1);
		bits = 31 * bits + Double.hashCode(this.x2);
		bits = 31 * bits + Double.hashCode(this.y2);
		bits = 31 * bits + Double.hashCode(this.x3);
		bits = 31 * bits + Double.hashCode(this.y3);
		final int b = (int) bits;
		return b ^ (b >> 31);
	}

	@Pure
	@Override
	public String toString() {
		final StringBuilder b = new StringBuilder();
		b.append("("); //$NON-NLS-1$
		b.append(getX1());
		b.append(", "); //$NON-NLS-1$
		b.append(getY1());
		b.append(")-("); //$NON-NLS-1$
		b.append(getX2());
		b.append(", "); //$NON-NLS-1$
		b.append(getY2());
		b.append(")-("); //$NON-NLS-1$
		b.append(getX3());
		b.append(", "); //$NON-NLS-1$
		b.append(getY3());
		b.append(")"); //$NON-NLS-1$
		return b.toString();
	}

	@Pure
	@Override
	public Triangle2d createTransformedShape(Transform2D transform) {
		if (transform == null || transform.isIdentity()) {
			return clone();
		}
		final Point2d point = getGeomFactory().newPoint(getX1(), getY1());
		transform.transform(point);
		final double x1 = point.getX();
		final double y1 = point.getY();
		point.set(getX2(), getY2());
		transform.transform(point);
		final double x2 = point.getX();
		final double y2 = point.getY();
		point.set(getX3(), getY3());
		transform.transform(point);
		return getGeomFactory().newTriangle(x1, y1, x2, y2, point.getX(), point.getY());
	}

	@Override
	public double getX1() {
		return this.x1;
	}

	@Override
	public double getY1() {
		return this.y1;
	}

	@Override
	public double getX2() {
		return this.x2;
	}

	@Override
	public double getY2() {
		return this.y2;
	}

	@Override
	public double getX3() {
		return this.x3;
	}

	@Override
	public double getY3() {
		return this.y3;
	}

	@Override
	public void setX1(double x) {
		if (this.x1 != x) {
			this.x1 = x;
			this.isCCW = null;
			fireGeometryChange();
		}
	}

	@Override
	public void setY1(double y) {
		if (this.y1 != y) {
			this.y1 = y;
			this.isCCW = null;
			fireGeometryChange();
		}
	}

	@Override
	public void setX2(double x) {
		if (this.x2 != x) {
			this.x2 = x;
			this.isCCW = null;
			fireGeometryChange();
		}
	}

	@Override
	public void setY2(double y) {
		if (this.y2 != y) {
			this.y2 = y;
			this.isCCW = null;
			fireGeometryChange();
		}
	}

	@Override
	public void setX3(double x) {
		if (this.x3 != x) {
			this.x3 = x;
			this.isCCW = null;
			fireGeometryChange();
		}
	}

	@Override
	public void setY3(double y) {
		if (this.y3 != y) {
			this.y3 = y;
			this.isCCW = null;
			fireGeometryChange();
		}
	}

	@Override
	public void set(double x1, double y1, double x2, double y2, double x3, double y3) {
		if (this.x1 != x1 || this.y1 != y1
				|| this.x2 != x2 || this.y2 != y2
				|| this.x3 != x3 || this.y3 != y3) {
			this.x1 = x1;
			this.y1 = y1;
			this.x2 = x2;
			this.y2 = y2;
			this.x3 = x3;
			this.y3 = y3;
			this.isCCW = null;
			fireGeometryChange();
		}
	}

	@Override
	public boolean isCCW() {
		if (this.isCCW == null) {
			this.isCCW = Boolean.valueOf(Triangle2afp.isCCWOrderDefinition(
					getX1(), getY1(), getX2(), getY2(), getX3(), getY3()));
		}
		return this.isCCW.booleanValue();
	}

}
