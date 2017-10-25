/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2018 The original authors, and other authors.
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
import org.arakhne.afc.math.geometry.d2.afp.Circle2afp;
import org.arakhne.afc.vmutil.asserts.AssertMessages;

/** A circle with 2 double precision floating-point numbers.
 *
 * @author $Author: sgalland$
 * @author $Author: hjaffali$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public class Circle2d
		extends AbstractShape2d<Circle2d>
		implements Circle2afp<Shape2d<?>, Circle2d, PathElement2d, Point2d, Vector2d, Rectangle2d> {

	private static final long serialVersionUID = -8532584773530573738L;

	private double centerX;

	private double centerY;

	private double radius;

	/** Construct an empty circle.
	 */
	public Circle2d() {
		//
	}

	/** Construct a circle at the given position, and with the given radius.
	 * @param center the center of the circle.
	 * @param radius the radius of the circle.
	 */
	public Circle2d(Point2D<?, ?> center, double radius) {
		set(center.getX(), center.getY(), radius);
	}

	/** Construct a circle at the given position, and with the given radius.
	 * @param x x coordinate of the center of the circle.
	 * @param y y coordinate of the center of the circle.
	 * @param radius the radius of the circle.
	 */
	public Circle2d(double x, double y, double radius) {
		set(x, y, radius);
	}

	/** Construct a circle from a circle.
	 * @param circle the circle to copy.
	 */
	public Circle2d(Circle2afp<?, ?, ?, ?, ?, ?> circle) {
		assert circle != null : AssertMessages.notNullParameter();
		set(circle.getX(), circle.getY(), circle.getRadius());
	}

	@Pure
	@Override
	@SuppressWarnings("checkstyle:equalshashcode")
	public int hashCode() {
		int bits = 1;
		bits = 31 * bits + Double.hashCode(this.centerX);
		bits = 31 * bits + Double.hashCode(this.centerY);
		bits = 31 * bits + Double.hashCode(this.radius);
		return bits ^ (bits >> 31);
	}

	@Pure
	@Override
	public double getX() {
		return this.centerX;
	}

	@Pure
	@Override
	public double getY() {
		return this.centerY;
	}

	@Override
	public void setX(double x) {
		if (this.centerX != x) {
			this.centerX = x;
			fireGeometryChange();
		}
	}

	@Override
	public void setY(double y) {
		if (this.centerY != y) {
			this.centerY = y;
			fireGeometryChange();
		}
	}

	@Pure
	@Override
	public double getRadius() {
		return this.radius;
	}

	@Override
	public void setRadius(double radius) {
		if (this.radius != radius) {
			this.radius = radius;
			fireGeometryChange();
		}
	}

	@Override
	public void set(double x, double y, double radius) {
		assert radius >= 0. : AssertMessages.positiveOrZeroParameter(2);
		if (this.centerX != x || this.centerY != y || this.radius != radius) {
			this.centerX = x;
			this.centerY = y;
			this.radius = radius;
			fireGeometryChange();
		}
	}

}
