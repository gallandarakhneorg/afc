/* 
 * $Id$
 * 
 * Copyright (C) 2013 Christophe BOHRHAUER.
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
package org.arakhne.afc.math.geometry.d3.continuous.spline;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import org.arakhne.afc.math.geometry.d3.Point3D;
import org.arakhne.afc.math.geometry.d3.continuous.Point3f;

/** Abstract implementation of a spline.
 * 
 * @param <T> the type of the spline.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public abstract class AbstractSpline<T extends AbstractSpline<? super T>> implements Spline {

	private static final long serialVersionUID = -3588325098818758673L;
	
	private double precisionFactor = 0.1;

	/** List of the control points.
	 */
    protected List<? extends Point3D> controlPoints;

    /**
     */
    public AbstractSpline() {
    	this.controlPoints = Collections.emptyList();
    }
    
    /**
     * @param points the control points of the spline.
     */
    public AbstractSpline(Point3D... points) {
    	this(Arrays.asList(points));
    }
    
    /**
     * @param points the control points of the spline.
     */
    public AbstractSpline(List<? extends Point3D> points) {
    	assert (points != null);
    	this.controlPoints = points;
    }
    
    @Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (getClass().isInstance(obj)) {
			AbstractSpline<?> s = (AbstractSpline<?>) obj;
			return ((getDiscretizationFactor() == s.getDiscretizationFactor()) &&
					(getControlPoints().equals(s.getControlPoints())));
		}
		return false;
	}

	@Override
	public int hashCode() {
		long bits = 1L;
		bits = 31L * bits + Double.doubleToLongBits(getDiscretizationFactor());
		bits = 31L * bits + getControlPoints().hashCode();
		return (int) (bits ^ (bits >> 32));
	}
    
	@Override
	public int getControlPointCount() {
		return this.controlPoints.size();
	}
	
	@Override
	public List<Point3D> getControlPoints() {
		return Collections.unmodifiableList(this.controlPoints);
	}
	
    @SuppressWarnings("unchecked")
	@Override
	public T clone() {
		try {
			return (T) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new Error(e);
		}
	}
	
    /**
     * Set the factor that is used for discretizing the spline.
     * The semantic of this factor depends on spline implementation.
     *
     * @param p the discretization factor.
     */
	public void setDiscretizationFactor(double p) {
		this.precisionFactor = p;
	}

	@Override
	public double getDiscretizationFactor() {
		return this.precisionFactor;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + ":" + this.controlPoints.toString(); //$NON-NLS-1$
	}
	
	/** Abstract implementation of an iterator on points for a spline.
	 *
	 * @author $Author: galland$
	 * @version $Name$ $Revision$ $Date$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	protected abstract class AbstractPointIterator implements Iterator<Point3f> {

		/** The control points of the spline.
		 */
		private final Iterator<? extends Point3D> controlPointIterator;

		/** The preferred discretiezation for evolving the parametric value t.
		 */
		protected final double dt;

		/** The parametric value t.
		 */
		protected double t;
		
		private boolean init = false;
		private Point3f nextPoint;

		/**
		 */
		public AbstractPointIterator() {
			this.dt = getDiscretizationFactor();
			assert (this.dt > 0. && this.dt < 1.);
			this.controlPointIterator = getControlPoints().iterator();
		}

		/** Compute the next point to be replied by this iterator.
		 *
		 * @param isFirstSegment indicates if the first segment of the spline was never encountered (<code>true</code>).
		 * @param controlPoints the iterator on the control points that could be used for computation.
		 * @return the next point, or <code>null</code> if none.
		 */
		protected abstract Point3f computeNextPoint(boolean isFirstSegment, Iterator<? extends Point3D> controlPoints);
		
		private void ensureFirstComputation() {
			if (!this.init) {
				this.init = true;
				this.nextPoint = computeNextPoint(true, this.controlPointIterator);
			}
		}
		
		@Override
		public boolean hasNext() {
			ensureFirstComputation();
			return this.nextPoint != null;
		}

		@Override
		public Point3f next() {
			ensureFirstComputation();
			Point3f p = this.nextPoint;
			if (p == null) {
				throw new NoSuchElementException();
			}
			this.nextPoint = computeNextPoint(false, this.controlPointIterator);
			return p;
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}

	}

}
