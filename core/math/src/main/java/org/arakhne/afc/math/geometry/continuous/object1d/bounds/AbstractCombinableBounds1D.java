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

package org.arakhne.afc.math.geometry.continuous.object1d.bounds;

import java.util.Collection;

import org.arakhne.afc.math.geometry.continuous.bounds.NoOnSameSegmentException;
import org.arakhne.afc.math.geometry.continuous.object1d.Point1D;
import org.arakhne.afc.math.geometry.continuous.object1d.Segment1D;

/**
 * An implementation of bounds in a 1D space.
 *
 * @param <S> is the type of the segment to reply
 * @author $Author: cbohrhauer$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public abstract class AbstractCombinableBounds1D<S extends Segment1D>
extends AbstractBounds1D<S>
implements CombinableBounds1D<S> {

	private static final long serialVersionUID = 8063427541040498212L;

	/**
	 * Create a unset box.
	 * 
	 * @param segment is the segment on which this bound must lie
	 */
	public AbstractCombinableBounds1D(S segment) {
		super(segment);
	}
	
	/**
	 * Create a box from a set of points.
	 *
	 * @param segment is the segment on which this bound must lie
	 * @param points is the set of points.
	 */
	public AbstractCombinableBounds1D(S segment, float... points) {
		super(segment);
		set(points);
	}
	
	/** {@inheritDoc}
	 */
	@Override
	public boolean isEmpty() {
		return !isInit() || super.isEmpty();
	}

	/** {@inheritDoc}
	 */
	@Override
	public void clamp() {
		if (Float.isNaN(this.lower) || Float.isNaN(this.upper)) return;

		float center = (this.lower+this.upper) / 2.f;
		
		if (center<0.) {
			this.lower -= center;
			this.upper -= center;
		}
		
		Segment1D sgmt = getSegment();
		assert(sgmt!=null);
		float length = sgmt.getLength();
		if (center>length) {
			float delta = center - length;
			this.lower -= delta;
			this.upper -= delta;
		}
	}
	

	@Override
	public void combine(float... x) {
		for(float point : x) {
			if (Float.isNaN(this.lower) || point<this.lower) {
				this.lower = point;
			}
			if (Float.isNaN(this.upper) || point>this.upper) {
				this.upper = point;
			}
		}
		clamp();
	}

	@Override
	public final void combine(Bounds1D<S> bound) {
		if (getSegment()!=bound.getSegment()) throw new NoOnSameSegmentException();
		
		combine(bound.getMinX(), bound.getMaxX());
	}

	@Override
	public final void combine(Collection<? extends Bounds1D<S>> bounds) {
		for(Bounds1D<S> bound : bounds) {
			combine(bound);
		}
	}

	/** {@inheritDoc}
	 */
	@Override
	public final void setBox(Bounds1D<?> bounds) {
		setBox(bounds.getMinX(), bounds.getMaxX());
	}

	/** Set this bound object from the given bound object.
	 * This function changes the bounds and the segment.
	 * 
	 * @param bounds is the bounds to copy.
	 * @see #setBox(Bounds1D)
	 * @see #setSegment(Segment1D)
	 */
	@Override
	public final void set(Bounds1D<S> bounds) {
		S sgmt = bounds.getSegment();
		assert(sgmt!=null);
		this.segment = sgmt;
		setBox(bounds.getMinX(), bounds.getMaxX());
		clamp();
	}

	/** {@inheritDoc}
	 */
	@Override
	public final void setSegment(S segment) {
		assert(segment!=null);
		this.segment = segment;
		clamp();
	}

	/** {@inheritDoc}
	 */
	@Override
	public void reset() {
		this.lower = this.upper = Float.NaN;
	}

	/** {@inheritDoc}
	 */
	@Override
	public void combine(Point1D point) {
		Segment1D sgmt = point.getSegment();
		if (sgmt==this.segment) {
			float c = point.getCurvilineCoordinate();
			if (c<this.lower) this.lower = c;
			else if (c>this.upper) this.upper = c;
			clamp();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void set(Point1D point) {
		Segment1D sgmt = point.getSegment();
		try {
			S s = (S)sgmt;
			float c = point.getCurvilineCoordinate();
			this.segment = s;
			this.lower = c;
			this.upper = c;
			clamp();
		}
		catch(ClassCastException _) {
			//
		}
	}

	/** {@inheritDoc}
	 */
	@Override
	public boolean isInit() {
		return !Float.isNaN(this.lower) && !Float.isNaN(this.lower) && this.lower<=this.upper;
	}
	
	/** {@inheritDoc}
	 */
	@Override
	public void set(float... points) {
		this.lower = this.upper = Float.NaN;
		for(float point : points) {
			if (Float.isNaN(this.lower) || point<this.lower) {
				this.lower = point;
			}
			if (Float.isNaN(this.upper) || point>this.upper) {
				this.upper = point;
			}
		}
		clamp();
	}

	/** {@inheritDoc}
	 */
	@Override
	public void setBox(float lower, float upper) {
		this.lower = lower;
		this.upper = upper;
	}

}
