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
package org.arakhne.afc.math.geometry;

import java.util.Iterator;

import org.arakhne.afc.math.geometry2d.Point2D;
import org.arakhne.afc.math.geometry2d.continuous.Point2f;
import org.arakhne.afc.math.geometry2d.discrete.Point2i;
import org.arakhne.afc.math.geometry3d.Point3D;
import org.arakhne.afc.math.geometry3d.continuous.Point3f;

/**
 * This class is an iterator which takes as
 * parameters a collection of 3D points and
 * replies 2D points with the y and z 3D coordinate inside.
 * <p>
 * {@code Point2f.x = Point3d.y}<br>
 * {@code Point2f.y = Point3d.z}<br>
 * 
 * @author $Author: cbohrhauer$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
class PointFilterYZIterator
implements Iterator<Point2D> {

	private final Iterator<? extends Point3D> original;
	
	/**
	 * @param points are the points to iterate on.
	 */
	public PointFilterYZIterator(Iterator<? extends Point3D> points) {
		assert(points!=null);
		this.original = points;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean hasNext() {
		return this.original.hasNext();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Point2D next() {
		Point3D p = this.original.next();
		assert(p!=null);
		if (p instanceof Point3f) {
			return new Point2f(p.getY(), p.getZ());
		}
		return new Point2i(p.y(), p.z());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void remove() {
		this.original.remove();
	}

}

