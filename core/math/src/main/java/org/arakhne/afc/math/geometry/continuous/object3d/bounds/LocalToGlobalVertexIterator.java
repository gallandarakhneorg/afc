/* 
 * $Id$
 * 
 * Copyright (c) 2013 Christophe BOHRHAUER
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

package org.arakhne.afc.math.geometry.continuous.object3d.bounds;

import org.arakhne.afc.math.geometry.continuous.euclide.EuclidianPoint3D;
import org.arakhne.afc.math.geometry.continuous.object3d.Point3f;
import org.arakhne.afc.math.geometry.continuous.object3d.Vector3f;
import org.arakhne.afc.sizediterator.SizedIterator;

/**
 * An interator which auomatically translate local coordinate
 * vectors to the corresponding global point.
 * 
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
class LocalToGlobalVertexIterator implements SizedIterator<EuclidianPoint3D> {

	private final SizedIterator<Vector3f> vertices;
	private final Point3f center;
	
	/**
	 * @param center
	 * @param vertices
	 */
	public LocalToGlobalVertexIterator(Point3f center, SizedIterator<Vector3f> vertices) {
		this.center = center;
		this.vertices = vertices;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int index() {
		return this.vertices.index();
	}

	@Override
	public int totalSize() {
		return this.vertices.totalSize();
	}

	@Override
	public boolean hasNext() {
		return this.vertices.hasNext();
	}

	@Override
	public EuclidianPoint3D next() {
		Vector3f v = this.vertices.next();
		return new EuclidianPoint3D(
				this.center.getX() + v.getX(),
				this.center.getY() + v.getY(),
				this.center.getZ() + v.getZ());
	}

	@Override
	public void remove() {
		this.vertices.remove();
	}

	@Override
	public int rest() {
		// TODO Auto-generated method stub
		return 0;
	}
	
}
