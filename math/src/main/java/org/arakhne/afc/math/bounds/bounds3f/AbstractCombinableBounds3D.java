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

package org.arakhne.afc.math.bounds.bounds3f;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import org.arakhne.afc.math.continous.object3d.Point3f;
import org.arakhne.afc.math.continous.object3d.Tuple3f;

/**
 * An abstract implementation of bounds.
 * 
 * @author $Author: sgalland$
 * @author $Author: ngaud$
 * @author $Author: cbohrhauer$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public abstract class AbstractCombinableBounds3D
extends AbstractBounds3D
implements CombinableBounds3D {

	private static final long serialVersionUID = 3405142855839820841L;

	/**
	 * Add the points into the bounds.
	 *
	 * @param isInit must indicates if this bounding object is supposed to be already initialized or not.
	 * This parameter is used to improve the performances. 
	 * @param pointList are the points used to update the bounding box coordinates 
	 */
	protected abstract void combinePoints(boolean isInit, Collection<? extends Tuple3f> pointList);
	
	/**
	 * Add the bounds into the bounds.
	 * 
	 * @param isInit must indicates if this bounding object is supposed to be already initialized or not.
	 * This parameter is used to improve the performances. 
	 * @param bounds are the bounds used to update the bounding box coordinates 
	 */
	protected abstract void combineBounds(boolean isInit, Collection<? extends Bounds3D> bounds);

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void combine(Point3f point) {
		combinePoints(isInit(), Collections.singleton(point));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void set(Point3f point) {
		combinePoints(false, Collections.singleton(point));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void combine(Bounds3D bound) {
		combineBounds(isInit(), Collections.singletonList(bound));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void combine(Collection<? extends Bounds3D> bounds) {
		combineBounds(isInit(), bounds);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void combinePoints(Collection<? extends Tuple3f> points) {
		combinePoints(isInit(), points);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void set(Bounds3D bounds) {
		combineBounds(false, Collections.singletonList(bounds));
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void combine(Tuple3f<?>... pointList) {
		combinePoints(isInit(), Arrays.asList(pointList));
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void set(Tuple3f<?>... points) {
		combinePoints(false, Arrays.asList(points));
	}
	
	/** {@inheritDoc}
	 */
	@Override
	public final void set(Collection<? extends Tuple3f> points) {
		combinePoints(false, points);
	}


}
