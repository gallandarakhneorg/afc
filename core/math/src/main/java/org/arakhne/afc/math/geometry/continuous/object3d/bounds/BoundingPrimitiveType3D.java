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

import org.arakhne.afc.math.geometry.BoundingPrimitiveType;
import org.arakhne.afc.math.geometry.continuous.object2d.bounds.BoundingPrimitiveType2D;

/** This enumeration contains all the types of primitive bounding volumes supported by
 * the API.
 * <p>
 * The primitive bounding volumes are the bounding volumes which cannot be decomposed
 * into sub volumes.
 *
 * @author $Author: sgalland$
 * @author $Author: ngaud$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public enum BoundingPrimitiveType3D implements BoundingPrimitiveType<Bounds3D> {

	/** A bouding box aligned on world axis.
	 */
	ALIGNED_BOX(AlignedBoundingBox.class),

	/** A bounding box not aligned on world axis.
	 */
	ORIENTED_BOX(OrientedBoundingBox.class),

	/** A bounding capsule.
	 */
	CAPSULE(BoundingCapsule.class),

	/** A bounding sphere.
	 */
	SPHERE(BoundingSphere.class);
	
	private final Class<? extends Bounds3D> type;
	
	private BoundingPrimitiveType3D(Class<? extends Bounds3D> type) {
		this.type = type;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Class<? extends Bounds3D> getInstanceType() {
		return this.type;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Bounds3D newInstance() throws InstantiationException, IllegalAccessException {
		return this.type.newInstance();
	}
	
	/** Replies the 2D-equivalent primitive type.
	 * 
	 * @return the 2D-equivalent primitive type.
	 */
	public BoundingPrimitiveType2D toBoundingPrimitiveType2D() {
		switch(this) {
		case ALIGNED_BOX:
			return BoundingPrimitiveType2D.ALIGNED_RECTANGLE;
		case ORIENTED_BOX:
			return BoundingPrimitiveType2D.ORIENTED_RECTANGLE;
		case CAPSULE:
			return null;
		case SPHERE:
			return BoundingPrimitiveType2D.CIRCLE;
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public float getDimensions() {
		return 3f;
	}
	
}