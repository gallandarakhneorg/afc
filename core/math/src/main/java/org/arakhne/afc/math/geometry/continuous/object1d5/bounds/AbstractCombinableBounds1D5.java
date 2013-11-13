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

package org.arakhne.afc.math.geometry.continuous.object1d5.bounds;

import java.util.Collection;

import org.arakhne.afc.math.geometry.continuous.bounds.NoOnSameSegmentException;
import org.arakhne.afc.math.geometry.continuous.object1d.Segment1D;
import org.arakhne.afc.math.geometry.continuous.object1d5.Point1D5;
import org.arakhne.afc.math.geometry.continuous.object2d.Tuple2f;

/**
 * An implementation of bounds in a 1.5D space.
 *
 * @param <S> is the type of the segment to reply
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public abstract class AbstractCombinableBounds1D5<S extends Segment1D>
extends AbstractBounds1D5<S>
implements CombinableBounds1D5<S> {

	private static final long serialVersionUID = 5584254022465847754L;

	/**
	 * Create a unset box.
	 */
	public AbstractCombinableBounds1D5() {
		super();
	}

	/**
	 * Create a unset box.
	 * 
	 * @param segment is the segment on which this bound must lie
	 */
	public AbstractCombinableBounds1D5(S segment) {
		super(segment);
	}
	
	/**
	 * Create a box from a set of points.
	 *
	 * @param segment is the segment on which this bound must lie
	 * @param points is the set of points.
	 */
	public AbstractCombinableBounds1D5(S segment, float... points) {
		super(segment);
		set(points);
	}
	
	/**
	 * Create a box from a set of points.
	 *
	 * @param segment is the segment on which this bound must lie
	 * @param points is the set of points.
	 */
	public AbstractCombinableBounds1D5(S segment, Tuple2f... points) {
		super(segment);
		set(points);
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

	/** {@inheritDoc}
	 */
	@Override
	public boolean isEmpty() {
		return !isInit() || super.isEmpty();
	}

	/** {@inheritDoc}
	 */
	@Override
	public boolean isInit() {
		return !Float.isNaN(this.lower) && !Float.isNaN(this.lower) && this.lower<=this.upper;
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
	public void combine(Tuple2f... points) {
		float lx, ly, ux, uy;
		lx = this.lower;
		ux = this.upper;
		ly = this.jutting - this.lateralSize/2.f;
		uy = this.jutting + this.lateralSize/2.f;
		
		for(Tuple2f point : points) {
			if (Float.isNaN(lx) || point.getX()<lx) {
				lx = point.getX();
			}
			if (Float.isNaN(ux) || point.getX()>ux) {
				ux = point.getX();
			}
			if (Float.isNaN(ly) || point.getY()<ly) {
				ly = point.getY();
			}
			if (Float.isNaN(uy) || point.getY()>uy) {
				uy = point.getY();
			}
		}
		
		this.lower = lx;
		this.upper = ux;
		this.jutting = (ly+uy)/2.f;
		this.lateralSize = uy-ly;
		clamp();
	}

	@Override
	public final void combine(Bounds1D5<S> bound) {
		if (getSegment()!=bound.getSegment()) throw new NoOnSameSegmentException();
		
		combine(bound.getMinX(), bound.getMaxX());
		
		float juttingDistance = bound.getJutting();
		float demiSize = bound.getLateralSize()/2.f;
		float myDemiSize = this.lateralSize / 2.f;
		
		float lowery = Math.min(juttingDistance-demiSize, this.jutting-myDemiSize); 
		float uppery = Math.max(juttingDistance+demiSize, this.jutting+myDemiSize);
		
		this.jutting = (lowery+uppery) / 2.f;
		this.lateralSize = uppery - lowery;
	}

	@Override
	public final void combine(Collection<? extends Bounds1D5<S>> bounds) {
		for(Bounds1D5<S> bound : bounds) {
			combine(bound);
		}
	}

	/** {@inheritDoc}
	 */
	@Override
	public final void setBox(Bounds1D5<?> bounds) {
		setBox(bounds.getMinX(), bounds.getMaxX(), bounds.getJutting(), bounds.getLateralSize());
	}

	/** Set this bound object from the given bound object.
	 * This function changes the bounds and the segment.
	 * 
	 * @param bounds is the bounds to copy.
	 * @see #setBox(Bounds1D5)
	 * @see #setSegment(Segment1D)
	 */
	@Override
	public final void set(Bounds1D5<S> bounds) {
		S sgmt = bounds.getSegment();
		assert(sgmt!=null);
		this.segment = sgmt;
		setBox(bounds.getMinX(), bounds.getMaxX(), bounds.getJutting(), bounds.getLateralSize());
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
	public void combine(Point1D5 point) {
		Segment1D sgmt = point.getSegment();
		if (sgmt==this.segment) {
			float c = point.getCurvilineCoordinate();
			if (c<this.lower) this.lower = c;
			else if (c>this.upper) this.upper = c;
			
			float j = point.getJuttingDistance();
			float jmin = this.jutting - getLateralSize() / 2.f;
			float jmax = this.jutting + getLateralSize() / 2.f;
			if (j<jmin) {
				float s = jmin - j;
				this.lateralSize += s;
				this.jutting -= s / 2.;
			}
			else if (j>jmax) {
				float s = j - jmax;
				this.lateralSize += s;
				this.jutting += s / 2.;
			}
			
			clamp();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void set(Point1D5 point) {
		Segment1D sgmt = point.getSegment();
		try {
			S s = (S)sgmt;
			float c = point.getCurvilineCoordinate();
			this.segment = s;
			this.lower = c;
			this.upper = c;
			this.jutting = point.getJuttingDistance();
			this.lateralSize = 0.f;
			clamp();
		}
		catch(ClassCastException _) {
			//
		}
	}

	/** {@inheritDoc}
	 */
	@Override
	public final void set(float... points) {
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
	public final void setBox(float lower, float upper, float juttingDistance, float lateralSize) {
		this.jutting = juttingDistance;
		this.lateralSize = (lateralSize<=0) ? 0 : lateralSize;
		if (lower>upper) {
			this.lower = upper;
			this.upper = lower;
		}
		else {
			this.lower = lower;
			this.upper = upper;
		}
	}

	/** {@inheritDoc}
	 */
	@Override
	public final void set(Tuple2f... points) {
		this.lower = this.upper = Float.NaN;
		float l, u;
		l = u = Float.NaN;
		for(Tuple2f point : points) {
			if (Float.isNaN(this.lower) || point.getX()<this.lower) {
				this.lower = point.getX();
			}
			if (Float.isNaN(this.upper) || point.getX()>this.upper) {
				this.upper = point.getX();
			}
			if (Float.isNaN(l) || point.getY()<l) {
				l = point.getY();
			}
			if (Float.isNaN(u) || point.getY()>u) {
				u = point.getY();
			}
		}
		this.jutting = (l+u)/2.f;
		this.lateralSize = u-l;
		clamp();
	}

	/** {@inheritDoc}
	 */
	@Override
	public final void setJutting(float juttingDistance) {
		this.jutting = juttingDistance;
	}

	/** {@inheritDoc}
	 */
	@Override
	public final void setLateralSize(float size) {
		assert(size>=0.);
		this.lateralSize = size;
	}

}
