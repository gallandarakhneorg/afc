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

package org.arakhne.afc.math.bounds.bounds2f;

import org.arakhne.afc.math.continous.object2d.Point2f;
import org.arakhne.afc.math.continous.object2d.Vector2f;
import org.arakhne.afc.math.euclide.EuclidianPoint2D;
import org.arakhne.afc.sizediterator.SizedIterator;

/**
 * An interator which auomatically translate local coordinate
 * vectors to the corresponding global point.
 * 
 * @author $Author: cbohrhauer$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
class LocalToGlobalVertexIterator implements SizedIterator<EuclidianPoint2D> {

	private final SizedIterator<Vector2f> vertices;
	private final Point2f center;
	
	/**
	 * @param center
	 * @param vertices
	 */
	public LocalToGlobalVertexIterator(Point2f center, SizedIterator<Vector2f> vertices) {
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
	public EuclidianPoint2D next() {
		Vector2f v = this.vertices.next();
		return new EuclidianPoint2D(
				this.center.getX() + v.getX(),
				this.center.getY() + v.getY());
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
