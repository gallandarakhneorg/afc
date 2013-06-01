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

import org.arakhne.afc.math.bounds.BoundingPrimitiveType;
import org.arakhne.afc.math.bounds.bounds3f.BoundingPrimitiveType3D;

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
public enum BoundingPrimitiveType2D implements BoundingPrimitiveType<Bounds2D> {

	/** A bouding rectangle aligned on world axis.
	 */
	ALIGNED_RECTANGLE(MinimumBoundingRectangle.class),

	/** A bounding rectangle not aligned on world axis.
	 */
	ORIENTED_RECTANGLE(OrientedBoundingRectangle.class),

	/** A bounding circle.
	 */
	CIRCLE(BoundingCircle.class);
	
	private final Class<? extends Bounds2D> type;
	
	private BoundingPrimitiveType2D(Class<? extends Bounds2D> type) {
		this.type = type;
	}
	
	/** {@inheritDoc}
	 */
	@Override
	public Class<? extends Bounds2D> getInstanceType() {
		return this.type;
	}
	
	/** {@inheritDoc}
	 */
	@Override
	public Bounds2D newInstance() throws InstantiationException, IllegalAccessException {
		return this.type.newInstance();
	}
	
	/** Replies the 3D-equivalent primitive type.
	 * 
	 * @return the 3D-equivalent primitive type, or <code>null</code> if no
	 * equivalent is available.
	 */
	public BoundingPrimitiveType3D toBoundingPrimitiveType3D() {
		switch(this) {
		case ALIGNED_RECTANGLE:
			return BoundingPrimitiveType3D.ALIGNED_BOX;
		case ORIENTED_RECTANGLE:
			return BoundingPrimitiveType3D.ORIENTED_BOX;
		case CIRCLE:
			return BoundingPrimitiveType3D.SPHERE;
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public float getDimensions() {
		return 2f;
	}
	
}