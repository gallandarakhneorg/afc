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

package org.arakhne.afc.math.geometry.d3.i;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.geometry.d3.Point3D;
import org.arakhne.afc.math.geometry.d3.ai.Sphere3ai;
import org.arakhne.afc.vmutil.asserts.AssertMessages;

/** A circle with 2 integer numbers.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public class Sphere3i extends AbstractShape3i<Sphere3i>
        implements Sphere3ai<Shape3i<?>, Sphere3i, PathElement3i, Point3i, Vector3i, RectangularPrism3i> {

	private static final long serialVersionUID = -7692549016859323986L;

	private int centerX;

	private int centerY;

	private int centerZ;

	private int radius;

	/** Construct an empty sphere.
     */
	public Sphere3i() {
		//
	}

	/** Construct a Sphere at the given position and with the given radius.
     * @param center the center position of the sphere.
     * @param radius the radius of the sphere.
     */
	public Sphere3i(Point3D<?, ?> center, int radius) {
		assert center != null : AssertMessages.notNullParameter(0);
		set(center.ix(), center.iy(), center.iz(), radius);
	}


	/** Construct a sphere at the given position and with the given radius.
	 * @param x x coordinate of the the center position of the sphere.
	 * @param y y coordinate of the the center position of the sphere.
	 * @param z z coordinate of the the center position of the sphere.
	 * @param radius the radius of the circle.
	 */
	public Sphere3i(int x, int y, int z, int radius) {
		set(x, y, z, radius);
	}

	/** Construct a sphere from a sphere.
	 * @param sphere the sphere to copy.
	 */
	public Sphere3i(Sphere3ai<?, ?, ?, ?, ?, ?> sphere) {
		assert sphere != null : AssertMessages.notNullParameter();
		set(sphere.getX(), sphere.getY(), sphere.getZ(), sphere.getRadius());
	}

	@Pure
	@Override
	public int hashCode() {
		int bits = 1;
		bits = 31 * bits + this.centerX;
		bits = 31 * bits + this.centerY;
		bits = 31 * bits + this.centerZ;
		bits = 31 * bits + this.radius;
		return bits ^ (bits >> 31);
	}

	@Pure
	@Override
	public int getX() {
		return this.centerX;
	}

	@Pure
	@Override
	public int getY() {
		return this.centerY;
	}

	@Pure
	@Override
	public int getZ() {
		return this.centerZ;
	}

	@Override
	public void setX(int x) {
		if (this.centerX != x) {
			this.centerX = x;
			fireGeometryChange();
		}
	}

	@Override
	public void setY(int y) {
		if (this.centerY != y) {
			this.centerY = y;
			fireGeometryChange();
		}
	}

	@Override
	public void setZ(int z) {
		if (this.centerZ != z) {
			this.centerZ = z;
			fireGeometryChange();
		}
	}

	@Pure
	@Override
	public int getRadius() {
		return this.radius;
	}

	@Override
	public void setRadius(int radius) {
		assert radius >= 0 : AssertMessages.positiveOrZeroParameter();
		if (this.radius != radius) {
			this.radius = radius;
			fireGeometryChange();
		}
	}

	@Override
	public void set(int x, int y, int z, int radius) {
		assert radius >= 0 : AssertMessages.positiveOrZeroParameter(3);
		if (this.centerX != x || this.centerY != y || this.centerZ != z || this.radius != radius) {
			this.centerX = x;
			this.centerY = y;
			this.centerZ = z;
			this.radius = radius;
			fireGeometryChange();
		}
	}

}
