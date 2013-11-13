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
package org.arakhne.afc.math.geometry.continuous;

import java.util.Iterator;

import org.arakhne.afc.math.geometry.continuous.object2d.Point2f;
import org.arakhne.afc.math.geometry.continuous.object3d.Tuple3f;

/**
 * This class is an iterable which takes as
 * parameters a collection of 3D points and
 * replies 2D points with the x and y 3D coordinate inside.
 * <p>
 * {@code Point2f.x = Point3d.x}<br>
 * {@code Point2f.y = Point3d.y}<br>
 * 
 * @author $Author: cbohrhauer$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class PointFilterXYIterable implements Iterable<Point2f> {

	private final Iterable<? extends Tuple3f> original;
	
	/**
	 * @param points are the points to iterate on.
	 */
	public PointFilterXYIterable(Iterable<? extends Tuple3f> points) {
		assert(points!=null);
		this.original = points;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterator<Point2f> iterator() {
		return new PointFilterXYIterator(this.original.iterator());
	}

}

