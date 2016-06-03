/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2016 The original authors, and other authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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

	/** Construct a range.
	 *
	 * @param min the min value.
	 * @param max the max value.
	 */
	public DoubleRange(double min, double max) {
		assert min <= max : "min must be lower or equal to max"; //$NON-NLS-1$
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
			final DoubleRange range = (DoubleRange) obj;
			return this.min == range.getMin() && this.max == range.getMax();
		}
		return false;
	}

	@Override
	@Pure
	public int hashCode() {
		long bits = 1;
		bits = 31 * bits + Double.hashCode(this.min);
		bits = 31 * bits + Double.hashCode(this.max);
		final int b = (int) bits;
		return b ^ (b >> 31);
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
	public int compareTo(DoubleRange range) {
		if (range == null) {
			return Integer.MAX_VALUE;
		}
		final int cmp = Double.compare(this.min, range.min);
		if (cmp != 0) {
			return cmp;
		}
		return Double.compare(this.max, range.max);
	}

	@Override
	@Pure
	public String toString() {
		return "[" + Double.toString(this.min) + "; " + Double.toString(this.max) + "]"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}

}
