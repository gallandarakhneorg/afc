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

/** Define a set of values for a mathematic function.
 * 
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class MathBounds {
	
	/** Create a set of bounds that correspond to the specified discrete values.
	 * 
	 * @param values are put in there own bounds object
	 * @return the set of bounds
	 */
	public static MathBounds[] createDiscreteSet(float... values) {
		MathBounds[] bounds = new MathBounds[values.length];
		for( int i=0; i<values.length; ++i) {
			bounds[i] = new MathBounds(values[i]);
		}
		return bounds;
	}
	
	/** Create a set of bounds that correspond to the specified values.
	 * <p>
	 * The first value is the minimal value of the first bounds,
	 * the second value is the maximal value of the first bounds,
	 * the third value is the minimal value of the second bounds,
	 * the forth value is the maximal value of the second bounds, and so on.
	 * 
	 * @param values are put in there own bounds object
	 * @return the set of bounds
	 */
	public static MathBounds[] createSet(float... values) {
		MathBounds[] bounds = new MathBounds[values.length/2];
		for( int i=0, j=0; i<values.length; i+=2, ++j) {
			bounds[j] = new MathBounds(values[i], values[i+1]);
		}
		return bounds;
	}

	/** Create a bound that corresponds to {@code -infinity; +infinity}.
	 * 
	 * @return the set of bounds
	 */
	public static MathBounds[] createInfinitySet() {
		return new MathBounds[] {
				new MathBounds(Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY)
		};
	}

	private final float min;
	private final float max;
	private final boolean includeMin;
	private final boolean includeMax;

	/** A bounding set with one value.
	 * 
	 * @param value is the value of the minimal and maximale values in the set.
	 */
	public MathBounds(float value) {
		this(value, true, value, true);
	}

	/** A bounding set with with two bounds.
	 * 
	 * @param min is the minimal value
	 * @param max is the maximal value
	 */
	public MathBounds(float min, float max) {
		this(min, true, max, true);
	}

	/** A bounding set with with two bounds.
	 * 
	 * @param min is the minimal value
	 * @param includeMin indicates if the minimal value is inside the bounds or outside.
	 * @param max is the maximal value
	 * @param includeMax indicates if the maximal value is inside the bounds or outside.
	 */
	public MathBounds(float min, boolean includeMin, float max, boolean includeMax) {
		if (min<max) {
			this.min = min;
			this.max = max;
		}
		else {
			this.min = max;
			this.max = min;
		}
		this.includeMin = (this.min!=Float.NEGATIVE_INFINITY)&&(includeMin);
		this.includeMax = (this.max!=Float.POSITIVE_INFINITY)&&(includeMax);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @return {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuilder buffer = new StringBuilder();
		buffer.append(this.includeMin ? '[' : '(');
		buffer.append(Float.toString(this.min));
		buffer.append(';');
		buffer.append(Float.toString(this.max));
		buffer.append(this.includeMax ? ']' : ')');
		return buffer.toString();
	}
	
	/** Replies the minimal value of the value set.
	 * 
	 * @return the minimal value of the value set.
	 */
	public float getMin() {
		return this.min;
	}

	/** Replies the maximal value of the value set.
	 * 
	 * @return the maximal value of the value set.
	 */
	public float getMax() {
		return this.max;
	}

	/** Replies if the minimal value is included in the value set.
	 * 
	 * @return <code>true</code> if the minimal value is inside the set,
	 * otherwise <code>false</code>
	 */
	public boolean isMinValueIncluded() {
		return this.includeMin;
	}

	/** Replies if the maximal value is included in the value set.
	 * 
	 * @return <code>true</code> if the maximal value is inside the set,
	 * otherwise <code>false</code>
	 */
	public boolean isMaxValueIncluded() {
		return this.includeMax;
	}

}