/* 
 * $Id$
 * 
 * Copyright (c) 2006-10, Multiagent Team, Laboratoire Systemes et Transports, Universite de Technologie de Belfort-Montbeliard.
 * Copyright (C) 2012 Stephane GALLAND.
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
package org.arakhne.afc.math;

import java.io.Serializable;

import org.eclipse.xtext.xbase.lib.Pure;

/** A range of double floating-point numbers.
 * 
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public class DoubleRange implements Cloneable, Serializable, Comparable<DoubleRange> {

	private static final long serialVersionUID = -6358924548037498965L;

	private final double min;
	
	private final double max;
	
	/**
	 * @param min
	 * @param max
	 */
	public DoubleRange(double min, double max) {
		assert (min <= max) : "min must be lower or equal to max"; //$NON-NLS-1$
		this.min = min;
		this.max = max;
	}
	
	@Override
	public DoubleRange clone() {
		try {
			return (DoubleRange) super.clone();
		} catch (CloneNotSupportedException exception) {
			throw new Error(exception);
		}
	}
	
	@Override
	@Pure
	public boolean equals(Object obj) {
		if (obj instanceof DoubleRange) {
			DoubleRange range = (DoubleRange) obj;
			return this.min == range.getMin() && this.max == range.getMax();
		}
		return false;
	}
	
	@Override
	@Pure
	public int hashCode() {
		long bits = 1;
		bits = 31 * bits + Double.doubleToLongBits(this.min);
		bits = 31 * bits + Double.doubleToLongBits(this.max);
		int b = (int) bits;
		return b ^ (b >> 32);
	}
	
	/** Replies the minimum value.
	 *
	 * @return the minimum value.
	 */
	@Pure
	public double getMin() {
		return this.min;
	}

	/** Replies the maximum value.
	 *
	 * @return the maximum value.
	 */
	@Pure
	public double getMax() {
		return this.max;
	}
	
	/** Replies if the value is inside the range.
	 *
	 * @param value the value.
	 * @return <code>true</code> if the value is in the range; <code>false</code> otherwise.
	 */
	@Pure
	public boolean contains(double value) {
		return this.min <= value && value <= this.max;
	}

	@Override
	@Pure
	public int compareTo(DoubleRange o) {
		if (o == null) {
			return Integer.MAX_VALUE;
		}
		int cmp = Double.compare(this.min, o.min);
		if (cmp != 0) {
			return cmp;
		}
		return Double.compare(this.max, o.max);
	}
	
	@Override
	@Pure
	public String toString() {
		return "[" + Double.toString(this.min) + "; " + Double.toString(this.max) + "]"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}

}