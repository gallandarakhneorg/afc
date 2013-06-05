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

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import org.arakhne.afc.math.continous.object2d.Point2f;
import org.arakhne.afc.math.continous.object2d.Tuple2f;

/**
 * An abstract implementation of bounds.
 * 
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public abstract class AbstractCombinableBounds2D extends AbstractBounds2D implements CombinableBounds2D {

	private static final long serialVersionUID = 7076286742121359980L;

	/**
	 * Add the points into the bounds.
	 *
	 * @param isInit must indicates if this bounding object is supposed to be already initialized or not.
	 * This parameter is used to improve the performances. 
	 * @param pointList are the points used to update the bounding box coordinates 
	 */
	protected abstract void combinePoints(boolean isInit, Collection<? extends Tuple2f> pointList);
	
	/**
	 * Add the bounds into the bounds.
	 * 
	 * @param isInit must indicates if this bounding object is supposed to be already initialized or not.
	 * This parameter is used to improve the performances. 
	 * @param bounds are the bounds used to update the bounding box coordinates 
	 */
	protected abstract void combineBounds(boolean isInit, Collection<? extends Bounds2D> bounds);

	/** {@inheritDoc}
	 */
	@Override
	public final void combine(Point2f point) {
		combinePoints(isInit(), Collections.singleton(point));
	}

	/** {@inheritDoc}
	 */
	@Override
	public final void set(Point2f point) {
		combinePoints(false, Collections.singleton(point));
	}

	/** {@inheritDoc}
	 */
	@Override
	public final void combine(Bounds2D bound) {
		combineBounds(isInit(), Collections.singletonList(bound));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void combine(Collection<? extends Bounds2D> bounds) {
		combineBounds(isInit(), bounds);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void set(Bounds2D bounds) {
		combineBounds(false, Collections.singletonList(bounds));
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void combine(Tuple2f<?>... pointList) {
		combinePoints(isInit(), Arrays.asList(pointList));
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void set(Tuple2f<?>... points) {
		combinePoints(false, Arrays.asList(points));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void set(Collection<? extends Tuple2f<?>> points) {
		combinePoints(false, points);
	}

}
