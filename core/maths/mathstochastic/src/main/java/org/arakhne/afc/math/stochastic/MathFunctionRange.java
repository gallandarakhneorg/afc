/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2019 The original authors, and other authors.
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

package org.arakhne.afc.math.stochastic;

import org.eclipse.xtext.xbase.lib.Pure;

/** Define the range of a mathematic function.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 * @see "http://en.wikipedia.org/wiki/Range_(mathematics)"
 */
public class MathFunctionRange {

	private final double min;

	private final double max;

	private final boolean includeMin;

	private final boolean includeMax;

	/** A bounding set with one value.
	 *
	 * @param value is the value of the minimal and maximale values in the set.
	 */
	public MathFunctionRange(double value) {
		this(value, true, value, true);
	}

	/** A bounding set with with two bounds.
	 *
	 * @param min1 is the minimal value
	 * @param max1 is the maximal value
	 */
	public MathFunctionRange(double min1, double max1) {
		this(min1, true, max1, true);
	}

	/** A bounding set with with two bounds.
	 *
	 * @param min1 is the minimal value
	 * @param includeMin1 indicates if the minimal value is inside the bounds or outside.
	 * @param max1 is the maximal value
	 * @param includeMax1 indicates if the maximal value is inside the bounds or outside.
	 */
	public MathFunctionRange(double min1, boolean includeMin1, double max1, boolean includeMax1) {
		if (min1 < max1) {
			this.min = min1;
			this.max = max1;
		} else {
			this.min = max1;
			this.max = min1;
		}
		this.includeMin = (this.min != Double.NEGATIVE_INFINITY) && includeMin1;
		this.includeMax = (this.max != Double.POSITIVE_INFINITY) && includeMax1;
	}

	/** Create a set of bounds that correspond to the specified discrete values.
	 *
	 * @param values are put in there own bounds object
	 * @return the set of bounds
	 */
	@Pure
	public static MathFunctionRange[] createDiscreteSet(double... values) {
		final MathFunctionRange[] bounds = new MathFunctionRange[values.length];
		for (int i = 0; i < values.length; ++i) {
			bounds[i] = new MathFunctionRange(values[i]);
		}
		return bounds;
	}

	/** Create a set of bounds that correspond to the specified values.
	 *
	 * <p>The first value is the minimal value of the first bounds,
	 * the second value is the maximal value of the first bounds,
	 * the third value is the minimal value of the second bounds,
	 * the forth value is the maximal value of the second bounds, and so on.
	 *
	 * @param values are put in there own bounds object
	 * @return the set of bounds
	 */
	@Pure
	public static MathFunctionRange[] createSet(double... values) {
		final MathFunctionRange[] bounds = new MathFunctionRange[values.length / 2];
		for (int i = 0, j = 0; i < values.length; i += 2, ++j) {
			bounds[j] = new MathFunctionRange(values[i], values[i + 1]);
		}
		return bounds;
	}

	/** Create a bound that corresponds to {@code -infinity; +infinity}.
	 *
	 * @return the set of bounds
	 */
	@Pure
	public static MathFunctionRange[] createInfinitySet() {
		return new MathFunctionRange[] {
			new MathFunctionRange(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY),
		};
	}

	@Pure
	@Override
	public String toString() {
		final StringBuilder buffer = new StringBuilder();
		buffer.append(this.includeMin ? '[' : '(');
		buffer.append(Double.toString(this.min));
		buffer.append(';');
		buffer.append(Double.toString(this.max));
		buffer.append(this.includeMax ? ']' : ')');
		return buffer.toString();
	}

	/** Replies the minimal value of the value set.
	 *
	 * @return the minimal value of the value set.
	 */
	@Pure
	public double getMin() {
		return this.min;
	}

	/** Replies the maximal value of the value set.
	 *
	 * @return the maximal value of the value set.
	 */
	@Pure
	public double getMax() {
		return this.max;
	}

	/** Replies if the minimal value is included in the value set.
	 *
	 * @return <code>true</code> if the minimal value is inside the set,
	 *     otherwise <code>false</code>
	 */
	@Pure
	public boolean isMinValueIncluded() {
		return this.includeMin;
	}

	/** Replies if the maximal value is included in the value set.
	 *
	 * @return <code>true</code> if the maximal value is inside the set,
	 *     otherwise <code>false</code>
	 */
	@Pure
	public boolean isMaxValueIncluded() {
		return this.includeMax;
	}

}
