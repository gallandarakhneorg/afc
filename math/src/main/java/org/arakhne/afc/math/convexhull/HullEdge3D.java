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
package org.arakhne.afc.math.convexhull;

import org.arakhne.afc.math.continous.object3d.Point3f;

/** This class represents a 3D edge used inside
 * Convex Hull Computation Algorithms.
 * 
 * @see ConvexHull
 * @param <T> is the type of the points.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class HullEdge3D<T extends Point3f> implements HullObject<T> {

	/** Index of the first point of this segment.
	 */
	public final int a;

	/** Index of the last point of this segment.
	 */
	public final int b;
	
	private final int creationLevel;

	/**
	 * @param points is the list of points
	 * @param a is the index of the first point
	 * @param b is the index of the last point.
	 * @param creationLevel is the level inside the creation algorithm.
	 */
	public HullEdge3D(T[] points, int a, int b, int creationLevel) {
		this.a = a;
		this.b = b;
		this.creationLevel = creationLevel;
	}
	
	/** {@inheritDoc}
	 */
	@Override
	public int getCreationLevel() {
		return this.creationLevel;
	}
	
	/** {@inheritDoc}
	 * 
	 * @return {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "[Edge3D, "+this.a+","+this.b+"]\n";  //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$
	}

	/** {@inheritDoc}
	 */
	@Override
	public Point3f[] getObjectPoints(T[] points) {
		return new Point3f[] {
			points[this.a],
			points[this.b],
		};
	}
	
	/** {@inheritDoc}
	 */
	@Override
	public int[] getObjectIndexes() {
		return new int[] {
				this.a,
				this.b,
			};
	}

	/** {@inheritDoc}
	 */
	@Override
	public int indexesInRange(int min, int max) {
		int ci = min;
		int ca = max;
		if (ci>ca) {
			int t = ci;
			ci = ca;
			ca = t;
		}
		int c = 0;
		if ((this.a>=ci)&&(this.a<=ca)) ++c;
		if ((this.b>=ci)&&(this.b<=ca)) ++c;
		return c;
	}

}