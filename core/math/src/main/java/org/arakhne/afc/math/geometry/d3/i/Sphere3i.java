/* 
 * $Id$
 * 
 * Copyright (C) 2010-2013 Stephane GALLAND.
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
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

package org.arakhne.afc.math.geometry.d3.i;

import org.arakhne.afc.math.geometry.d3.Point3D;
import org.arakhne.afc.math.geometry.d3.ai.Sphere3ai;
import org.eclipse.xtext.xbase.lib.Pure;

/** A circle with 2 integer numbers.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public class Sphere3i
extends AbstractShape3i<Sphere3i>
implements Sphere3ai<Shape3i<?>, Sphere3i, PathElement3i, Point3i, Vector3i, RectangularPrism3i> {

	private static final long serialVersionUID = -7692549016859323986L;

	private int centerX;
	
	private int centerY;

	private int centerZ;

	private int radius;

	/**
	 */
	public Sphere3i() {
		//
	}

	/**
	 * @param center
	 * @param radius
	 */
	public Sphere3i(Point3D<?, ?> center, int radius) {
		assert (center != null) : "Center point must be not null"; //$NON-NLS-1$
		set(center.ix(), center.iy(), center.iz(), radius);
	}

	/**
	 * @param x
	 * @param y
	 * @param z
	 * @param radius
	 */
	public Sphere3i(int x, int y, int z, int radius) {
		set(x, y, z, radius);
	}

	/** Construct a circle from a circle.
	 * @param circle
	 */
	public Sphere3i(Sphere3ai<?, ?, ?, ?, ?, ?> circle) {
		assert (circle != null) : "Circle must be not null"; //$NON-NLS-1$
		set(circle.getX(), circle.getY(), circle.getZ(), circle.getRadius());
	}

	@Pure
	@Override
	public int hashCode() {
		int bits = 1;
		bits = 31 * bits + this.centerX;
		bits = 31 * bits + this.centerY;
		bits = 31 * bits + this.centerZ;
		bits = 31 * bits + this.radius;
		return bits ^ (bits >> 32);
	}

	@Pure
	@Override
	public String toString() {
		StringBuilder b = new StringBuilder();
		b.append("["); //$NON-NLS-1$
		b.append(getX());
		b.append(";"); //$NON-NLS-1$
		b.append(getY());
		b.append(";"); //$NON-NLS-1$
		b.append(getZ());
		b.append(";"); //$NON-NLS-1$
		b.append(getRadius());
		b.append("]"); //$NON-NLS-1$
		return b.toString();
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
		assert (radius >= 0) : "Radius must be positive or equal"; //$NON-NLS-1$
		if (this.radius != radius) {
			this.radius = radius;
			fireGeometryChange();
		}
	}

	@Override
	public void set(int x, int y, int z, int radius) {
		assert (radius >= 0) : "Radius must be positive or zero"; //$NON-NLS-1$
		if (this.centerX != x || this.centerY != y || this.centerZ != z || this.radius != radius) {
			this.centerX = x;
			this.centerY = y;
			this.centerZ = z;
			this.radius = radius;
			fireGeometryChange();
		}
	}

}
