/* 
 * $Id$
 * 
 * Copyright (c) 2013 Christophe BOHRHAUER.
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
package org.arakhne.afc.math.geometry.sfc.geometry3d.continuous.convexhull;

import org.arakhne.afc.math.geometry.d3.continuous.Point3f;
import org.eclipse.xtext.xbase.lib.Pure;

/** This class represents a 3D vertex used inside
 * Convex Hull Computation Algorithms.
 * 
 * @see ConvexHull
 * @param <T> is the type of the points.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class HullVertex3D<T extends Point3f> implements HullObject<T> {

	/** Index of the point.
	 */
	public final int index;
	
	private final int creationLevel;

	/**
	 * @param points is the list of points.
	 * @param index is the index of this vertex.
	 * @param creationLevel is the level in the creation level.
	 */
	@SuppressWarnings("hiding")
	public HullVertex3D(T[] points, int index, int creationLevel) {
		this.index = index;
		this.creationLevel = creationLevel;
	}

	/** {@inheritDoc}
	 */
	@Pure
	@Override
	public int getCreationLevel() {
		return this.creationLevel;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @return {@inheritDoc}
	 */
	@Pure
	@Override
	public String toString() {
		return "[Vertex3D, "+this.index+"]\n";  //$NON-NLS-1$//$NON-NLS-2$
	}

	/** {@inheritDoc}
	 */
	@Pure
	@Override
	public Point3f[] getObjectPoints(T[] points) {
		return new Point3f[] {
			points[this.index],
		};
	}
	
	/** {@inheritDoc}
	 */
	@Pure
	@Override
	public int[] getObjectIndexes() {
		return new int[] {
				this.index,
			};
	}

	/** {@inheritDoc}
	 */
	@Pure
	@Override
	public int indexesInRange(int min, int max) {
		int i = min;
		int a = max;
		if (i>a) {
			int t = i;
			i = a;
			a = t;
		}
		return ((this.index>=i)&&(this.index<=a)) ? 1 : 0;
	}

}