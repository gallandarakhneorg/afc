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

import static org.arakhne.afc.math.MathConstants.COHEN_SUTHERLAND_BACK;
import static org.arakhne.afc.math.MathConstants.COHEN_SUTHERLAND_BOTTOM;
import static org.arakhne.afc.math.MathConstants.COHEN_SUTHERLAND_FRONT;
import static org.arakhne.afc.math.MathConstants.COHEN_SUTHERLAND_INSIDE;
import static org.arakhne.afc.math.MathConstants.COHEN_SUTHERLAND_LEFT;
import static org.arakhne.afc.math.MathConstants.COHEN_SUTHERLAND_RIGHT;
import static org.arakhne.afc.math.MathConstants.COHEN_SUTHERLAND_TOP;

import org.eclipse.xtext.xbase.lib.Inline;
import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.continous.object2d.Point2f;
import org.arakhne.afc.math.generic.Point2D;
import org.arakhne.afc.math.generic.Vector2D;
import org.arakhne.afc.math.physics.MeasureUnitUtil;
import org.arakhne.afc.vmutil.asserts.AssertMessages;

/** Mathematic and geometric utilities.
 *
 * @author $Author: sgalland$
 * @author $Author: hjaffali$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@SuppressWarnings({"checkstyle:methodcount", "checkstyle:magicnumber"})
public final class MathUtil {

	private MathUtil() {
		//
	}

	/**
	 * Returns the sign of the argument; zero if the argument
	 * is zero, 1 if the argument is greater than zero, -1 if the
	 * argument is less than zero.
	 *
	 * <p>This function differs from {@link Math#signum(double)} because it
	 * is returning a integer value.
	 *
	 * @param value the floating-point value whose sign is to be returned
	 * @return the sign of the argument
	 */
	@Pure
	@Inline(value = "($1 == 0.) ? 0 : (($1 < -0.) ? -1 : 1)")
	public static int sign(double value) {
		return (value == 0.) ? 0 : ((value < -0.) ? -1 : 1);
	}


	/** Clamp the given value to the given range.
	 *
	 * <p>If the value is outside the {@code [min;max]}
	 * range, it is clamp to the nearest bounding value
	 * {@code min} or {@code max}.
	 *
	 * @param v is the value to clamp.
	 * @param min is the min value of the range.
	 * @param max is the max value of the range.
	 * @return the value in {@code [min;max]} range.
	 */
	@Pure
	public static double clamp(double v, double min, double max) {
		assert min <= max : AssertMessages.lowerEqualParameters(1, min, 2, max);
		if (v < min) {
			return min;
		}
		if (v > max) {
			return max;
		}
		return v;
	}

	/** Clamp the given value to the given range.
	 *
	 * <p>If the value is outside the {@code [min;max]}
	 * range, it is clamp to the nearest bounding value
	 * {@code min} or {@code max}.
	 *
	 * @param v is the value to clamp.
	 * @param min is the min value of the range.
	 * @param max is the max value of the range.
	 * @return the value in {@code [min;max]} range.
	 */
	@Pure
	public static int clamp(int v, int min, int max) {
		assert min <= max : AssertMessages.lowerEqualParameters(1, min, 2, max);
		if (v < min) {
			return min;
		}
		if (v > max) {
			return max;
		}
		return v;
	}

	/** Clamp the given value to the given range.
	 *
	 * <p>If the value is outside the {@code [min;max]}
	 * range, it is clamp to the nearest bounding value
	 * {@code min} or {@code max}.
	 *
	 * @param v is the value to clamp.
	 * @param min is the min value of the range.
	 * @param max is the max value of the range.
	 * @return the value in {@code [min;max]} range.
	 * @deprecated since 13.0, see {@link #clamp(double, double, double)}
	 */
	@Deprecated
	public static float clamp(float v, float min, float max) {
		return (float) clamp((double) v, min, max);
	}

	/** Replies if the given value is near zero.
	 *
	 * @param value is the value to test.
	 * @return <code>true</code> if the given {@code value}
	 *     is near zero, otherwise <code>false</code>.
	 * @see Math#ulp(double)
	 */
	@Pure
	@Inline(value = "Math.abs($1) < Math.ulp($1)", imported = Math.class)
	public static boolean isEpsilonZero(double value) {
		return Math.abs(value) < Math.ulp(value);
	}

	/** Replies if the given value is near zero.
	 *
	 * @param value is the value to test.
	 * @param epsilon the approximation epsilon. If {@link Double#NaN}, the function {@link Math#ulp(double)} is
	 *     used for evaluating the epsilon.
	 * @return <code>true</code> if the given {@code value}
	 *     is near zero, otherwise <code>false</code>.
	 */
	@Pure
	@Inline(value = "Math.abs($1) < (Double.isNaN($2) ? Math.ulp($1) : ($2))", imported = Math.class)
	public static boolean isEpsilonZero(double value, double epsilon) {
		final double eps = Double.isNaN(epsilon) ? Math.ulp(value) : epsilon;
		return Math.abs(value) <= eps;
	}

	/** Replies if the given values are near.
	 *
	 * @param v1 first value.
	 * @param v2 second value.
	 * @return <code>true</code> if the given {@code v1}
	 *     is near {@code v2}, otherwise <code>false</code>.
	 * @see Math#ulp(double)
	 */
	@Pure
	@Inline(value = "MathUtil.isEpsilonEqual($1, $2, Double.NaN)", imported = {MathUtil.class})
	public static boolean isEpsilonEqual(double v1, double v2) {
		return isEpsilonEqual(v1, v2, Double.NaN);
	}

	/** Replies if the given values are near.
	 *
	 * @param v1 first value.
	 * @param v2 second value.
	 * @param epsilon the approximation epsilon. If {@link Double#NaN}, the function {@link Math#ulp(double)} is
	 *     used for evaluating the epsilon.
	 * @return <code>true</code> if the given {@code v1}
	 *     is near {@code v2}, otherwise <code>false</code>.
	 */
	@Pure
	public static boolean isEpsilonEqual(double v1, double v2, double epsilon) {
		if (Double.isInfinite(v1)) {
			return Double.isInfinite(v2) && Math.signum(v1) == Math.signum(v2);
		} else if (Double.isNaN(v1)) {
			return false;
		}
		final double value = Math.abs(v1 - v2);
		final double eps = Double.isNaN(epsilon) ? Math.ulp(value) : epsilon;
		return value <= eps;
	}

	/** Compares its two arguments for order.
	 * Returns a negative integer,
	 * zero, or a positive integer as the first argument is less than, equal
	 * to, or greater than the second.
	 *
	 * @param v1 first value.
	 * @param v2 second value.
	 * @return a negative integer, zero, or a positive integer as the
	 *         first argument is less than, equal to, or greater than the
	 *         second.
	 */
	@Pure
	public static int compareEpsilon(double v1, double v2) {
		final double v = v1 - v2;
		if (Math.abs(v) < Math.ulp(v)) {
			return 0;
		}
		if (v <= 0.) {
			return -1;
		}
		return 1;
	}

	/** Compares its two arguments for order.
	 * Returns a negative integer,
	 * zero, or a positive integer as the first argument is less than, equal
	 * to, or greater than the second.
	 *
	 * @param v1 first value.
	 * @param v2 second value.
	 * @param epsilon approximation epsilon. If {@link Double#NaN}, the function {@link Math#ulp(double)} is
	 *     used for evaluating the epsilon.
	 * @return a negative integer, zero, or a positive integer as the
	 *         first argument is less than, equal to, or greater than the
	 *         second.
	 */
	@Pure
	public static int compareEpsilon(double v1, double v2, double epsilon) {
		final double v = v1 - v2;
		final double eps = Double.isNaN(epsilon) ? Math.ulp(v) : epsilon;
		if (Math.abs(v) <= eps) {
			return 0;
		}
		if (v <= 0.) {
			return -1;
		}
		return 1;
	}

	/** Replies the max value.
	 *
	 * @param values are the values to scan.
	 * @return the max value.
	 */
	@Pure
	public static double max(double... values) {
		if (values == null || values.length == 0) {
			return Double.NaN;
		}
		double max = values[0];
		for (final double v : values) {
			if (v > max) {
				max = v;
			}
		}
		return max;
	}

	/** Replies the max value.
	 *
	 * @param values are the values to scan.
	 * @return the max value.
	 */
	@Pure
	public static float max(float... values) {
		if (values == null || values.length == 0) {
			return Float.NaN;
		}
		float max = values[0];
		for (final float v : values) {
			if (v > max) {
				max = v;
			}
		}
		return max;
	}

	/** Replies the max value.
	 *
	 * @param values are the values to scan.
	 * @return the max value.
	 */
	@Pure
	public static int max(int... values) {
		if (values == null || values.length == 0) {
			return 0;
		}
		int max = values[0];
		for (final int v : values) {
			if (v > max) {
				max = v;
			}
		}
		return max;
	}

	/** Replies the max value.
	 *
	 * @param values are the values to scan.
	 * @return the max value.
	 */
	@Pure
	public static long max(long... values) {
		if (values == null || values.length == 0) {
			return 0;
		}
		long max = values[0];
		for (final long v : values) {
			if (v > max) {
				max = v;
			}
		}
		return max;
	}

	/** Replies the max value.
	 *
	 * @param values are the values to scan.
	 * @return the max value.
	 */
	@Pure
	public static short max(short... values) {
		if (values == null || values.length == 0) {
			return 0;
		}
		short max = values[0];
		for (final short v : values) {
			if (v > max) {
				max = v;
			}
		}
		return max;
	}

	/** Replies the min value.
	 *
	 * @param values are the values to scan.
	 * @return the min value.
	 */
	@Pure
	public static double min(double... values) {
		if (values == null || values.length == 0) {
			return Double.NaN;
		}
		double min = values[0];
		for (final double v : values) {
			if (v < min) {
				min = v;
			}
		}
		return min;
	}

	/** Replies the min value.
	 *
	 * @param values are the values to scan.
	 * @return the min value.
	 */
	@Pure
	public static float min(float... values) {
		if (values == null || values.length == 0) {
			return Float.NaN;
		}
		float min = values[0];
		for (final float v : values) {
			if (v < min) {
				min = v;
			}
		}
		return min;
	}

	/** Replies the min value.
	 *
	 * @param values are the values to scan.
	 * @return the min value.
	 */
	@Pure
	public static int min(int... values) {
		if (values == null || values.length == 0) {
			return 0;
		}
		int min = values[0];
		for (final int v : values) {
			if (v < min) {
				min = v;
			}
		}
		return min;
	}

	/** Replies the min value.
	 *
	 * @param values are the values to scan.
	 * @return the min value.
	 */
	@Pure
	public static long min(long... values) {
		if (values == null || values.length == 0) {
			return 0;
		}
		long min = values[0];
		for (final long v : values) {
			if (v < min) {
				min = v;
			}
		}
		return min;
	}

	/** Replies the min value.
	 *
	 * @param values are the values to scan.
	 * @return the min value.
	 */
	@Pure
	public static short min(short... values) {
		if (values == null || values.length == 0) {
			return 0;
		}
		short min = values[0];
		for (final short v : values) {
			if (v < min) {
				min = v;
			}
		}
		return min;
	}

	/** Clamp the given value to fit between the min and max values
	 * according to a cyclic heuristic.
	 * If the given value is not between the minimum and maximum
	 * values, the replied value
	 * is modulo the min-max range.
	 *
	 * @param value the value to clamp.
	 * @param min the minimum value inclusive.
	 * @param max the maximum value exclusive.
	 * @return the clamped value
	 */
	@Pure
	public static double clampCyclic(double value, double min, double max) {
		assert min <= max : AssertMessages.lowerEqualParameters(1, min, 2, max);
		if (Double.isNaN(max) || Double.isNaN(min) || Double.isNaN(max)) {
			return Double.NaN;
		}
		if (value < min) {
			final double perimeter = max - min;
			final double nvalue = min - value;
			double rest = perimeter - (nvalue % perimeter);
			if (rest >= perimeter) {
				rest -= perimeter;
			}
			return min + rest;
		} else if (value >= max) {
			final double perimeter = max - min;
			final double nvalue = value - max;
			final double rest = nvalue % perimeter;
			return min + rest;
		}
		return value;
	}

	/** Replies the {@code value} clamped to
	 * the nearest bounds.
	 * If |{@code value}-{@code minBounds}| &gt;
	 * |{@code value}-{@code maxBounds}| then the
	 * returned value is {@code maxBounds}; otherwise
	 * it is {@code minBounds}.
	 *
	 * @param value is the value to clamp.
	 * @param minBounds is the minimal allowed value.
	 * @param maxBounds is the maximal allowed value.
	 * @return {@code minBounds} or {@code maxBounds}.
	 */
	@Pure
	public static double clampToNearestBounds(double value, double minBounds, double maxBounds) {
		assert minBounds <= maxBounds : AssertMessages.lowerEqualParameters(1, minBounds, 2, maxBounds);
		final double center = (minBounds + maxBounds) / 2;
		if (value <= center) {
			return minBounds;
		}
		return maxBounds;
	}

	/** Compute the zone where the point is against the given rectangle
	 * according to the <a href="http://en.wikipedia.org/wiki/Cohen%E2%80%93Sutherland_algorithm">Cohen-Sutherland algorithm</a>.
	 *
	 * @param px is the coordinates of the points.
	 * @param py is the coordinates of the points.
	 * @param rxmin is the min of the coordinates of the rectangle.
	 * @param rymin is the min of the coordinates of the rectangle.
	 * @param rxmax is the max of the coordinates of the rectangle.
	 * @param rymax is the max of the coordinates of the rectangle.
	 * @return the zone
	 * @see MathConstants#COHEN_SUTHERLAND_BOTTOM
	 * @see MathConstants#COHEN_SUTHERLAND_TOP
	 * @see MathConstants#COHEN_SUTHERLAND_LEFT
	 * @see MathConstants#COHEN_SUTHERLAND_RIGHT
	 * @see MathConstants#COHEN_SUTHERLAND_INSIDE
	 */
	@Pure
	public static int getCohenSutherlandCode(int px, int py, int rxmin, int rymin, int rxmax, int rymax) {
		assert rxmin <= rxmax : AssertMessages.lowerEqualParameters(2, rxmin, 4, rxmax);
		assert rymin <= rymax : AssertMessages.lowerEqualParameters(3, rymin, 5, rymax);
		// initialised as being inside of clip window
		int code = COHEN_SUTHERLAND_INSIDE;
		if (px < rxmin) {
			// to the left of clip window
			code |= COHEN_SUTHERLAND_LEFT;
		}
		if (px > rxmax) {
			// to the right of clip window
			code |= COHEN_SUTHERLAND_RIGHT;
		}
		if (py < rymin) {
			// to the bottom of clip window
			code |= COHEN_SUTHERLAND_BOTTOM;
		}
		if (py > rymax) {
			// to the top of clip window
			code |= COHEN_SUTHERLAND_TOP;
		}
		return code;
	}

	/** Compute the zone where the point is against the given rectangle
	 * according to the <a href="http://en.wikipedia.org/wiki/Cohen%E2%80%93Sutherland_algorithm">Cohen-Sutherland algorithm</a>.
	 *
	 * <p>This function considers that if a point coordinate is equal to the a border coordinate of the rectangle,
	 * it is inside the rectangle.
	 *
	 * @param px is the coordinates of the points.
	 * @param py is the coordinates of the points.
	 * @param rxmin is the min of the coordinates of the rectangle.
	 * @param rymin is the min of the coordinates of the rectangle.
	 * @param rxmax is the max of the coordinates of the rectangle.
	 * @param rymax is the max of the coordinates of the rectangle.
	 * @return the zone
	 * @see MathConstants#COHEN_SUTHERLAND_BOTTOM
	 * @see MathConstants#COHEN_SUTHERLAND_TOP
	 * @see MathConstants#COHEN_SUTHERLAND_LEFT
	 * @see MathConstants#COHEN_SUTHERLAND_RIGHT
	 * @see MathConstants#COHEN_SUTHERLAND_INSIDE
	 */
	@Pure
	public static int getCohenSutherlandCode(double px, double py, double rxmin, double rymin, double rxmax, double rymax) {
		assert rxmin <= rxmax : AssertMessages.lowerEqualParameters(2, rxmin, 4, rxmax);
		assert rymin <= rymax : AssertMessages.lowerEqualParameters(3, rymin, 5, rymax);
		// initialised as being inside of clip window
		int code = COHEN_SUTHERLAND_INSIDE;
		if (px < rxmin) {
			// to the left of clip window
			code |= COHEN_SUTHERLAND_LEFT;
		}
		if (px > rxmax) {
			// to the right of clip window
			code |= COHEN_SUTHERLAND_RIGHT;
		}
		if (py < rymin) {
			// to the bottom of clip window
			code |= COHEN_SUTHERLAND_BOTTOM;
		}
		if (py > rymax) {
			// to the top of clip window
			code |= COHEN_SUTHERLAND_TOP;
		}
		return code;
	}

	/** Compute the zone where the point is against the given rectangular prism
	 * according to the <a href="http://en.wikipedia.org/wiki/Cohen%E2%80%93Sutherland_algorithm">Cohen-Sutherland algorithm</a>.
	 *
	 * @param px is the coordinates of the point.
	 * @param py is the coordinates of the point.
	 * @param pz is the coordinates of the point.
	 * @param rxmin is the min of the coordinates of the rectangular prism.
	 * @param rymin is the min of the coordinates of the rectangular prism.
	 * @param rzmin is the min of the coordinates of the rectangular prism.
	 * @param rxmax is the max of the coordinates of the rectangular prism.
	 * @param rymax is the max of the coordinates of the rectangular prism.
	 * @param rzmax is the max of the coordinates of the rectangular prism.
	 * @return the zone
	 */
	@Pure
	@SuppressWarnings("checkstyle:parameternumber")
    public static int getCohenSutherlandCode3D(int px, int py, int pz, int rxmin, int rymin, int rzmin, int rxmax, int rymax,
            int rzmax) {
		assert rxmin <= rxmax : AssertMessages.lowerEqualParameters(3, rxmin, 6, rxmax);
		assert rymin <= rymax : AssertMessages.lowerEqualParameters(4, rymin, 7, rymax);
		assert rzmin <= rzmax : AssertMessages.lowerEqualParameters(5, rzmin, 8, rzmax);
		// initialised as being inside of clip window
		int code = COHEN_SUTHERLAND_INSIDE;
		if (px < rxmin) {
			// to the left of clip window
			code |= COHEN_SUTHERLAND_LEFT;
		}
		if (px > rxmax) {
			// to the right of clip window
			code |= COHEN_SUTHERLAND_RIGHT;
		}
		if (py < rymin) {
			// to the bottom of clip window
			code |= COHEN_SUTHERLAND_BOTTOM;
		}
		if (py > rymax) {
			// to the top of clip window
			code |= COHEN_SUTHERLAND_TOP;
		}
		if (pz < rzmin) {
			// to the front of clip window
			code |= COHEN_SUTHERLAND_FRONT;
		}
		if (pz > rzmax) {
			// to the back of clip window
			code |= COHEN_SUTHERLAND_BACK;
		}
		return code;
	}

	/** Compute the zone where the point is against the given rectangular prism
	 * according to the <a href="http://en.wikipedia.org/wiki/Cohen%E2%80%93Sutherland_algorithm">Cohen-Sutherland algorithm</a>.
	 *
	 * <p>This function considers that if a point coordinate is equal to the a border coordinate of the rectangle,
	 * it is inside the rectangle.
	 *
	 * @param px is the coordinates of the point.
	 * @param py is the coordinates of the point.
	 * @param pz is the coordinates of the point.
	 * @param rxmin is the min of the coordinates of the rectangular prism.
	 * @param rymin is the min of the coordinates of the rectangular prism.
	 * @param rzmin is the min of the coordinates of the rectangular prism.
	 * @param rxmax is the max of the coordinates of the rectangular prism.
	 * @param rymax is the max of the coordinates of the rectangular prism.
	 * @param rzmax is the max of the coordinates of the rectangular prism.
	 * @return the zone
	 */
	@Pure
	@SuppressWarnings("checkstyle:parameternumber")
    public static int getCohenSutherlandCode3D(double px, double py, double pz, double rxmin, double rymin, double rzmin,
            double rxmax, double rymax, double rzmax) {
		assert rxmin <= rxmax : AssertMessages.lowerEqualParameters(3, rxmin, 6, rxmax);
		assert rymin <= rymax : AssertMessages.lowerEqualParameters(4, rymin, 7, rymax);
		assert rzmin <= rzmax : AssertMessages.lowerEqualParameters(5, rzmin, 8, rzmax);
		// initialised as being inside of clip window
		int code = COHEN_SUTHERLAND_INSIDE;
		if (px < rxmin) {
			// to the left of clip window
			code |= COHEN_SUTHERLAND_LEFT;
		}
		if (px > rxmax) {
			// to the right of clip window
			code |= COHEN_SUTHERLAND_RIGHT;
		}
		if (py < rymin) {
			// to the bottom of clip window
			code |= COHEN_SUTHERLAND_BOTTOM;
		}
		if (py > rymax) {
			// to the top of clip window
			code |= COHEN_SUTHERLAND_TOP;
		}
		if (pz < rzmin) {
			// to the front of clip window
			code |= COHEN_SUTHERLAND_FRONT;
		}
		if (pz > rzmax) {
			// to the back of clip window
			code |= COHEN_SUTHERLAND_BACK;
		}
		return code;
	}

	/** Determine the min and max values from a set of three values.
	 *
	 * <p>This function has an algorithm that is efficient for 3 values.
	 *
	 * <p>If one of the value is {@link Double#NaN}, it is ignored.
	 * If all the values are {@link Double#NaN}, the function replies
	 * <code>null</code>.
	 *
	 * @param value1 the first value.
	 * @param value2 the second value.
	 * @param value3 the third value.
	 * @return the min max range; or <code>null</code>.
	 * @since 13.0
	 */
	public static DoubleRange getMinMax(double value1, double value2, double value3) {
		// Efficient implementation of the min/max determination
		final double min;
		final double max;

		// ---------------------------------
		// Table of cases
		// ---------------------------------
		// a-b a-c b-c sequence min max case
		//  <   <   <  a b c     a   c   1
		//  <   <   >  a c b     a   b   2
		//  <   >   <            -   -
		//  <   >   >  c a b     c   b   3
		//  >   <   <  b a c     b   c   4
		//  >   <   >            -   -
		//  >   >   <  b c a     b   a   5
		//  >   >   >  c b a     c   a   6
		// ---------------------------------

		if (value1 <= value2) {
			// A and B are not NaN
			// case candidates: 123
			if (value1 <= value3) {
				// case candidates: 12
				min = value1;
				if (value2 <= value3) {
					// case: 1
					max = value3;
				} else {
					// case: 2
					max = value2;
				}
			} else {
				// 3
				max = value2;
				if (Double.isNaN(value3)) {
					min = value1;
				} else {
					min = value3;
				}
			}
		} else {
			// case candidates: 456
			if (value1 <= value3) {
				max = value3;
				if (Double.isNaN(value2)) {
					min = value1;
				} else {
					// case: 4
					min = value2;
				}
			} else if (Double.isNaN(value1)) {
				if (value2 <= value3) {
					min = value2;
					max = value3;
				} else if (Double.isNaN(value2)) {
					if (Double.isNaN(value3)) {
						return null;
					}
					min = value3;
					max = min;
				} else if (Double.isNaN(value3)) {
					min = value2;
					max = min;
				} else {
					min = value3;
					max = value2;
				}
			} else if (Double.isNaN(value3)) {
				if (Double.isNaN(value2)) {
					min = value1;
					max = min;
				} else {
					min = value2;
					max = value1;
				}
			} else {
				// B may NaN
				// case candidates: 56
				max = value1;
				if (value2 <= value3) {
					// case: 5
					min = value2;
				} else {
					// case: 6
					min = value3;
				}
			}
		}
		return new DoubleRange(min, max);
	}

	/** Replies the cosecant of the specified angle.
	 *
	 * <p><code>csc(a) = 1/sin(a)</code>
	 *
	 * <p><img src="./doc-files/trigo1.png" alt="[Cosecant function]">
	 * <img src="./doc-files/trigo3.png" alt="[Cosecant function]">
	 *
	 * @param angle the angle.
	 * @return the cosecant of the angle.
	 */
	@Pure
	@Inline(value = "1./Math.sin($1)", imported = {Math.class})
	public static double csc(double angle) {
		return 1. / Math.sin(angle);
	}

	/** Replies the secant of the specified angle.
	 *
	 * <p><code>csc(a) = 1/cos(a)</code>
	 *
	 * <p><img src="./doc-files/trigo1.png" alt="[Secant function]">
	 * <img src="./doc-files/trigo2.png" alt="[Secant function]">
	 *
	 * @param angle the angle.
	 * @return the secant of the angle.
	 */
	@Pure
	@Inline(value = "1./Math.cos($1)", imported = {Math.class})
	public static double sec(double angle) {
		return 1. / Math.cos(angle);
	}

	/** Replies the cotangent of the specified angle.
	 *
	 * <p><code>csc(a) = 1/tan(a)</code>
	 *
	 * <p><img src="./doc-files/trigo1.png" alt="[Cotangent function]">
	 * <img src="./doc-files/trigo3.png" alt="[Cotangent function]">
	 *
	 * @param angle the angle.
	 * @return the cotangent of the angle.
	 */
	@Pure
	@Inline(value = "1./Math.tan($1)", imported = {Math.class})
	public static double cot(double angle) {
		return 1. / Math.tan(angle);
	}

	/** Replies the versine of the specified angle.
	 *
	 * <p><code>versin(a) = 1 - cos(a)</code>
	 *
	 * <p><img src="./doc-files/trigo1.png" alt="[Versine function]">
	 *
	 * @param angle the angle.
	 * @return the versine of the angle.
	 */
	@Pure
	@Inline(value = "1.-Math.cos($1)", imported = {Math.class})
	public static double versin(double angle) {
		return 1. - Math.cos(angle);
	}

	/** Replies the exsecant of the specified angle.
	 *
	 * <p><code>exsec(a) = sec(a) - 1</code>
	 *
	 * <p><img src="./doc-files/trigo1.png" alt="[Exsecant function]">
	 *
	 * @param angle the angle.
	 * @return the exsecant of the angle..
	 */
	@Pure
	@Inline(value = "MathUtil.sec($1)-1.", imported = {MathUtil.class})
	public static double exsec(double angle) {
		return sec(angle) - 1.;
	}

	/** Replies the chord of the specified angle.
	 *
	 * <p><code>crd(a) = 2 sin(a/2)</code>
	 *
	 * <p><img src="./doc-files/chord.png" alt="[Chord function]">
	 *
	 * @param angle the angle.
	 * @return the chord of the angle.
	 */
	@Pure
	@Inline(value = "2.*Math.sin(($1)/2.)", imported = {Math.class})
	public static double crd(double angle) {
		return 2. * Math.sin(angle / 2.);
	}

	/** Replies the half of the versine (aka, haversine) of the specified angle.
	 *
	 * <p><code>haversine(a) = sin<sup>2</sup>(a/2) = (1-cos(a)) / 2</code>
	 *
	 * <p><img src="./doc-files/trigo1.png" alt="[Excosecant function]">
	 *
	 * @param angle the angle.
	 * @return the half of the versine of the angle.
	 */
	@Pure
	public static double haversine(double angle) {
		final double sin2 = Math.sin(angle / 2.);
		return sin2 * sin2;
	}

	/** Clamp the given angle in radians to {@code [0;2PI)}.
	 *
	 * @param radians is the angle to clamp
	 * @return the angle in {@code [0;2PI)} range.
	 * @deprecated since 13.0, {@link #clampCyclic(double, double, double)}
	 */
	@Deprecated
	public static float clampRadian(float radians) {
		float rad = radians;
		while (rad < 0f) {
			rad += MathConstants.TWO_PI;
		}
		while (rad >= MathConstants.TWO_PI) {
			rad -= MathConstants.TWO_PI;
		}
		return rad;
	}

	/** Compute the distance between a point and a segment.
	 *
	 * @param px horizontal position of the point.
	 * @param py vertical position of the point.
	 * @param x1 horizontal position of the first point of the segment.
	 * @param y1 vertical position of the first point of the segment.
	 * @param x2 horizontal position of the second point of the segment.
	 * @param y2 vertical position of the second point of the segment.
	 * @return the distance beetween the point and the segment.
	 * @deprecated since 13.0, see {@code Segment2afp}
	 */
	@Deprecated
	public static float distancePointToSegment(float px, float py, float x1, float y1, float x2, float y2) {
		return distancePointToSegment(px, py, x1, y1, x2, y2, null);
	}

	/** Compute the distance between a point and a segment.
	 *
	 * @param px horizontal position of the point.
	 * @param py vertical position of the point.
	 * @param x1 horizontal position of the first point of the segment.
	 * @param y1 vertical position of the first point of the segment.
	 * @param x2 horizontal position of the second point of the segment.
	 * @param y2 vertical position of the second point of the segment.
	 * @param pts is the point that will be set with the coordinates of the nearest point,
	 *     if not <code>null</code>.
	 * @return the distance beetween the point and the segment.
	 * @deprecated since 13.0, see {@code Segment2afp}
	 */
	@Deprecated
	@SuppressWarnings("checkstyle:all")
	public static float distancePointToSegment(float px, float py, float x1, float y1, float x2, float y2, Point2D pts) {
		final float rdenomenator = (x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1);
		if (rdenomenator == 0.) {
			return distancePointToPoint(px, py, x1, y1);
		}
		final float rnumerator = (px - x1) * (x2 - x1) + (py - y1) * (y2 - y1);
		final float ratio = rnumerator / rdenomenator;

		if (ratio <= 0.) {
			if (pts != null) {
				pts.set(x1, y1);
			}
			return (float) Math.sqrt((px - x1) * (px - x1) + (py - y1) * (py - y1));
		}

		if (ratio>=1.) {
			if (pts!=null) pts.set(x2, y2);
			return (float)Math.sqrt((px-x2)*(px-x2) + (py-y2)*(py-y2));
		}

		if (pts!=null) pts.set(
				ratio * (x2-x1),
				ratio * (y2-y1));

		float s =  ((y1-py)*(x2-x1)-(x1-px)*(y2-y1) ) / rdenomenator;
		return (float)(Math.abs(s) * Math.sqrt(rdenomenator));
	}

	/** Compute the square distance between a point and a segment.
	 *
	 * @param px horizontal position of the point.
	 * @param py vertical position of the point.
	 * @param x1 horizontal position of the first point of the segment.
	 * @param y1 vertical position of the first point of the segment.
	 * @param x2 horizontal position of the second point of the segment.
	 * @param y2 vertical position of the second point of the segment.
	 * @return the distance beetween the point and the segment.
	 * @deprecated since 13.0, see {@code Segment2afp}
	 */
	@Deprecated
	public static float distanceSquaredPointToSegment(float px, float py, float x1, float y1, float x2, float y2) {
		return distanceSquaredPointToSegment(px, py, x1, y1, x2, y2, null);
	}

	/** Compute the square distance between a point and a segment.
	 *
	 * @param px horizontal position of the point.
	 * @param py vertical position of the point.
	 * @param x1 horizontal position of the first point of the segment.
	 * @param y1 vertical position of the first point of the segment.
	 * @param x2 horizontal position of the second point of the segment.
	 * @param y2 vertical position of the second point of the segment.
	 * @param pts is the point that will be set with the coordinates of the nearest point,
	 *     if not <code>null</code>.
	 * @return the distance beetween the point and the segment.
	 * @deprecated since 13.0, see {@code Segment2afp}
	 */
	@Deprecated
	public static float distanceSquaredPointToSegment(float px, float py, float x1, float y1, float x2, float y2, Point2D pts) {
		final float rdenomenator = (x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1);
		if (rdenomenator == 0f) {
			return distanceSquaredPointToPoint(px, py, x1, y1);
		}
		final float rnumerator = (px - x1) * (x2 - x1) + (py - y1) * (y2 - y1);
		final float ratio = rnumerator / rdenomenator;

		if (ratio <= 0f) {
			if (pts != null) {
				pts.set(x1, y1);
			}
			return Math.abs((px - x1) * (px - x1) + (py - y1) * (py - y1));
		}

		if (ratio >= 1f) {
			if (pts != null) {
				pts.set(x2, y2);
			}
			return Math.abs((px - x2) * (px - x2) + (py - y2) * (py - y2));
		}

		if (pts != null) {
			pts.set(
					ratio * (x2 - x1),
					ratio * (y2 - y1));
		}

		final float result =  ((y1 - py) * (x2 - x1) - (x1 - px) * (y2 - y1)) / rdenomenator;
		return (result * result) * Math.abs(rdenomenator);
	}

	/** Compute the distance between a point and a line.
	 *
	 * @param px horizontal position of the point.
	 * @param py vertical position of the point.
	 * @param x1 horizontal position of the first point of the line.
	 * @param y1 vertical position of the first point of the line.
	 * @param x2 horizontal position of the second point of the line.
	 * @param y2 vertical position of the second point of the line.
	 * @return the distance beetween the point and the line.
	 * @see #distanceSquaredPointToLine(float, float, float, float, float, float)
	 * @see #relativeDistancePointToLine(float, float, float, float, float, float)
	 * @deprecated since 13.0, see {@code Segment2afp}
	 */
	@Deprecated
	public static float distancePointToLine(float px, float py, float x1, float y1, float x2, float y2) {
		final float rdenomenator = (x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1);
		if (rdenomenator == 0.) {
			return distancePointToPoint(px, py, x1, y1);
		}
		final float result = ((y1 - py) * (x2 - x1) - (x1 - px) * (y2 - y1)) / rdenomenator;
		return (float) (Math.abs(result) * Math.sqrt(rdenomenator));
	}

	/** Compute the distance between a point and a line.
	 *
	 * @param px horizontal position of the point.
	 * @param py vertical position of the point.
	 * @param x1 horizontal position of the first point of the line.
	 * @param y1 vertical position of the first point of the line.
	 * @param x2 horizontal position of the second point of the line.
	 * @param y2 vertical position of the second point of the line.
	 * @return the distance beetween the point and the line.
	 * @see #distancePointToLine(float, float, float, float, float, float)
	 * @deprecated since 13.0, see {@code Segment2afp}
	 */
	@Deprecated
	public static float distanceSquaredPointToLine(float px, float py, float x1, float y1, float x2, float y2) {
		final float rdenomenator = (x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1);
		if (rdenomenator == 0.) {
			return distanceSquaredPointToPoint(px, py, x1, y1);
		}
		final float result = ((y1 - py) * (x2 - x1) - (x1 - px) * (y2 - y1)) / rdenomenator;
		return (result * result) * Math.abs(rdenomenator);
	}

	/** Compute the distance between two points.
	 *
	 * @param x1 x1
	 * @param y1 y1
	 * @param x2 x2
	 * @param y2 y2
	 * @return the distance.
	 * @deprecated since 13.0
	 */
	@Deprecated
	@SuppressWarnings("checkstyle:all")
	public static float distancePointToPoint(float x1, float y1, float x2, float y2) {
		float dx, dy;
		dx = x1 - x2;
		dy = y1 - y2;
		return (float)Math.sqrt(dx*dx+dy*dy);
	}

	/** Compute the squared distance between 2 points.
	 *
	 * @param x1 horizontal position of the first point.
	 * @param y1 vertical position of the first point.
	 * @param x2 horizontal position of the second point.
	 * @param y2 vertical position of the second point.
	 * @return the squared distance between given points.
	 * @see #distancePointToPoint(float, float, float, float)
	 * @deprecated
	 */
	@Deprecated
	@SuppressWarnings("checkstyle:all")
	public static final float distanceSquaredPointToPoint(float x1, float y1, float x2, float y2) {
		float dx, dy;
		dx = x1 - x2;
		dy = y1 - y2;
		return dx*dx+dy*dy;
	}

	/**
	 * Replies the specified angle translated between 0 and 2PI.
	 *
	 * @param radian
	 *            is an angle
	 * @return normalized angle
	 * @deprecated since 13.0, see {@link #clampCyclic(double, double, double)}
	 */
	@Deprecated
	@SuppressWarnings("checkstyle:all")
	public static float clampRadian0To2PI(float radian) {
		if ((!Float.isNaN(radian)) && (!Float.isInfinite(radian))) {
			return (float) clampCyclic(radian, 0, 2f*MathConstants.PI);
		}
		return radian;
	}

	/**
	 * Replies the specified angle translated between -PI and PI.
	 *
	 * @param radian
	 *            is an angle
	 * @return normalized angle
	 * @deprecated since 13.0, see {@link #clampCyclic(double, double, double)}
	 */
	@Deprecated
	@SuppressWarnings("checkstyle:all")
	public static float clampRadianMinusPIToPI(float radian) {
		if ((!Float.isNaN(radian)) && (!Float.isInfinite(radian))) {
			return clampTrigo(radian, (float)-MathConstants.PI, (float)MathConstants.PI);
		}
		return radian;
	}

	/**
	 * Replies the specified angle translated between 0 and 360.
	 *
	 * @param degree
	 *            is an angle
	 * @return normalized angle
	 * @deprecated since 13.0, see {@link #clampCyclic(double, double, double)}
	 */
	@Deprecated
	@SuppressWarnings("checkstyle:all")
	public static float clampDegree0To360(float degree) {
		if ((!Float.isNaN(degree)) && (!Float.isInfinite(degree))) {
			return clampTrigo(degree, 0, 360);
		}
		return degree;
	}

	/**
	 * Replies the specified angle translated between -180 and 180.
	 *
	 * @param degree
	 *            is an angle
	 * @return normalized angle
	 * @deprecated since 13.0, see {@link #clampCyclic(double, double, double)}
	 */
	@Deprecated
	@SuppressWarnings("checkstyle:all")
	public static float clampDegreeMinus180To180(float degree) {
		if ((!Float.isNaN(degree)) && (!Float.isInfinite(degree))) {
			return clampTrigo(degree, -180, 180);
		}
		return degree;
	}

	/** Clamp the given value to fit between the min and max values
	 * according to a trigonometric-like heuristic.
	 * If the given value is not between the minimum and maximum
	 * values and the value is positive, the value is modulo the perimeter
	 * of the counterclockwise circle.
	 * If the given value is not between the minimum and maximum
	 * values and the value is negative, the value is modulo the perimeter
	 * of the clockwise circle.
	 * The perimeter is the distance between {@code min} and {@code max}.
	 *
	 * @param value
	 * @param min
	 * @param max
	 * @return the clamped value
	 * @deprecated since 13.0, see {@link #clampCyclic(double, double, double)}
	 */
	@Deprecated
	@SuppressWarnings("checkstyle:all")
	public static float clampTrigo(float value, float min, float max) {
		if (Float.isNaN(max)) { // NaN is lower than all the number according to float.compareTo()
			return Float.NaN;
		}
		if (Float.isNaN(min)) {
			// Clamp max only
			if (value>max) return max - Float.MAX_VALUE + value;
		}
		else {
			assert(min<=max);
			if (min==max) return min; // special case: empty interval
			if (value<min || value >max) {
				float perimeter = max - min;
				float nvalue = value - min;
				float rest = nvalue % perimeter;
				return (value<0) ? max+rest : rest+min;
			}
		}
		return value;
	}

	/**
	 * Compute the signed angle between two vectors.
	 *
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @return the angle between <code>-PI</code> and <code>PI</code>.
	 * @deprecated
	 */
	@Deprecated
	@SuppressWarnings("checkstyle:all")
	public static float signedAngle(float x1, float y1, float x2, float y2) {
		float length1 = (float)Math.sqrt(x1 * x1 + y1 * y1);
		float length2 = (float)Math.sqrt(x2 * x2 + y2 * y2);

		if ((length1 == 0f) || (length2 == 0f))
			return Float.NaN;

		float cx1 = x1;
		float cy1 = y1;
		float cx2 = x2;
		float cy2 = y2;

		// A and B are normalized
		if (length1 != 1) {
			cx1 /= length1;
			cy1 /= length1;
		}

		if (length2 != 1) {
			cx2 /= length2;
			cy2 /= length2;
		}

		/*
		 * // First method // Angle // A . B = |A|.|B|.cos(theta) = cos(theta) float dot = x1 * x2 + y1 * y2; float angle = Math.acos(dot);
		 *
		 * // On which side of A, B is located? if ((dot > -1) && (dot < 1)) { dot = MathUtil.determinant(x2, y2, x1, y1); if (dot < 0) angle = -angle; }
		 */

		// Second method
		// A . B = |A|.|B|.cos(theta) = cos(theta)
		float cos = cx1 * cx2 + cy1 * cy2;
		// A x B = |A|.|B|.sin(theta).N = sin(theta) (where N is the unit vector perpendicular to plane AB)
		float sin = cx1 * cy2 - cy1 * cx2;

		float angle = (float)Math.atan2(sin, cos);

		return angle;
	}

	/** Return the trigonometric angle of a vector.
	 *  The vector is from the first point to the
	 *  second point.
	 *  <p>
	 *  The trigonometric angle is the signed angle between
	 *  the vectors (1;0) and (p2.x-p1.x;p2.y-p1.y).
	 *
	 * @param p1 first point.
	 * @param p2 second point.
	 * @return the trigonometric angle in radians in [-PI;PI].
	 * @deprecated
	 */
	@Deprecated
	@SuppressWarnings("checkstyle:all")
	public static final float angleOfVector( Point2D p1, Point2D p2 ) {
		return signedAngle(
				1, 0,
				p2.getX() - p1.getX(),
				p2.getY() - p1.getY());
	}

	/** Return the trigonometric angle of a vector.
	 *  The vector is from the first point to the
	 *  second point.
	 *  <p>
	 *  The trigonometric angle is the signed angle between
	 *  the vectors (1;0) and (x2-x1;y2-y1).
	 *
	 * @param x1 is the coordinate of the vector origin point.
	 * @param y1 is the coordinate of the vector origin point.
	 * @param x2 is the coordinate of the vector target point.
	 * @param y2 is the coordinate of the vector target point.
	 * @return the trigonometric angle in radians in [-PI;PI].
	 * @deprecated
	 */
	@Deprecated
	@SuppressWarnings("checkstyle:all")
	public static final float angleOfVector(float x1, float y1, float x2, float y2) {
		return signedAngle(
				1, 0,
				x2-x1, y2-y1);
	}

	/** Return the trigonometric angle of a vector.
	 *  The vector is from the first point to the
	 *  second point.
	 *  <p>
	 *  The trigonometric angle is the signed angle between
	 *  the vectors (1;0) and (x;y).
	 *
	 * @param x is the coordinate of the vector.
	 * @param y is the coordinate of the vector.
	 * @return the trigonometric angle in radians in [-PI;PI].
	 * @deprecated
	 */
	@Deprecated
	@SuppressWarnings("checkstyle:all")
	public static final float angleOfVector(float x, float y) {
		return signedAngle(
				1, 0,
				x, y);
	}

	/** Replies if a point is closed to a segment.
	 *
	 * @param x1 horizontal location of the first-segment begining.
	 * @param y1 vertical location of the first-segment ending.
	 * @param x2 horizontal location of the second-segment begining.
	 * @param y2 vertical location of the second-segment ending.
	 * @param x horizontal location of the point.
	 * @param y vertical location of the point.
	 * @param hitDistance is the maximal hitting distance.
	 * @return <code>true</code> if the point and the
	 *         line have closed locations.
	 * @deprecated
	 */
	@Deprecated
	@SuppressWarnings("checkstyle:all")
	public static boolean isPointClosedToSegment( float x1, float y1,
			float x2, float y2,
			float x, float y, float hitDistance ) {
		return ( distancePointToSegment(x, y, x1, y1, x2, y2) < hitDistance ) ;
	}

	/** Replies if a point is closed to a line.
	 *
	 * @param x1 horizontal location of the first-line begining.
	 * @param y1 vertical location of the first-line ending.
	 * @param x2 horizontal location of the second-line begining.
	 * @param y2 vertical location of the second-line ending.
	 * @param x horizontal location of the point.
	 * @param y vertical location of the point.
	 * @param hitDistance is the maximal hitting distance.
	 * @return <code>true</code> if the point and the
	 *         line have closed locations.
	 * @deprecated
	 */
	@Deprecated
	@SuppressWarnings("checkstyle:all")
	public static boolean isPointClosedToLine( float x1, float y1,
			float x2, float y2,
			float x, float y, float hitDistance ) {
		return ( distancePointToLine(x, y, x1, y1, x2, y2) < hitDistance ) ;
	}

	/** Replies the metrics from inches.
	 *
	 * @param i the inch value
	 * @return a value in centimeters
	 * @deprecated since 13.0, see {@link MeasureUnitUtil}
	 */
	@Deprecated
	@SuppressWarnings("checkstyle:all")
	public static float inchToMetric( float i ) {
		return i / 0.3937f ;
	}

	/** Replies the inches from metrics.
	 *
	 * @param m the metric value
	 * @return a value in inches
	 * @deprecated since 13.0, see {@link MeasureUnitUtil}
	 */
	@Deprecated
	@SuppressWarnings("checkstyle:all")
	public static float metricToInch( float m ) {
		return m * 0.3937f ;
	}

	/** Replies the {@code value} clamped in
	 * the specified interval assuming the
	 * it is a angle in radians.
	 *
	 * @param value is the value to clamp.
	 * @param min is the minimal allowed value.
	 * @param max is the maximal allowed value.
	 * @return the clamped value.
	 * @deprecated since 13.0, see {@link #clampCyclic(double, double, double)}
	 */
	@Deprecated
	@SuppressWarnings("checkstyle:all")
	public static float clampAngle( float value, float min, float max ) {
		assert(min<=max);
		float v = value;
		while (v>max) {
			v -= 2*Math.PI;
		}
		if (v<min) return min;
		return v;
	}

	/** Replies the {@code value} clamped to
	 * the nearest bounds.
	 * If |{@code value}-{@code minBounds}| &gt;
	 * |{@code value}-{@code maxBounds}| then the
	 * returned value is {@code maxBounds}; otherwise
	 * it is {@code minBounds}.
	 *
	 * @param value is the value to clamp.
	 * @param minBounds is the minimal allowed value.
	 * @param maxBounds is the maximal allowed value.
	 * @return {@code minBounds} or {@code maxBounds}.
	 * @deprecated since 13.0, see {@link #clampToNearestBounds(double, double, double)}
	 */
	@Deprecated
	@SuppressWarnings("checkstyle:all")
	public static float clampToNearestBounds( float value, float minBounds, float maxBounds ) {
		assert(minBounds<=maxBounds);
		float center = (minBounds+maxBounds) / 2f;
		if (value<=center) return minBounds;
		return maxBounds;
	}

	/**
	 * Replies one position factor for the intersection point between two lines.
	 * <p>
	 * Let line equations for L1 and L2:<br>
	 * <code>L1: P1 + factor1 * (P2-P1)</code><br>
	 * <code>L2: P3 + factor2 * (P4-P3)</code><br>
	 * If lines are intersecting, then<br>
	 * <code>P1 + factor1 * (P2-P1) = P3 + factor2 * (P4-P3)</code>
	 * <p>
	 * This function computes and replies <code>factor1</code>.
	 *
	 * @param x1
	 *            is the X coordinate of the first point of the first segment.
	 * @param y1
	 *            is the Y coordinate of the first point of the first segment.
	 * @param x2
	 *            is the X coordinate of the second point of the first segment.
	 * @param y2
	 *            is the Y coordinate of the second point of the first segment.
	 * @param x3
	 *            is the X coordinate of the first point of the second segment.
	 * @param y3
	 *            is the Y coordinate of the first point of the second segment.
	 * @param x4
	 *            is the X coordinate of the second point of the second segment.
	 * @param y4
	 *            is the Y coordinate of the second point of the second segment.
	 * @return <code>factor1</code> or {@link Float#NaN} if no intersection.
	 * @deprecated
	 */
	@Deprecated
	@SuppressWarnings("checkstyle:all")
	public static float getSegmentSegmentIntersectionFactor(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4) {
		float X1 = x2 - x1;
		float Y1 = y2 - y1;
		float X2 = x4 - x3;
		float Y2 = y4 - y3;

		// determinant is zero when parallel = det(L1,L2)
		float det = determinant(X1, Y1, X2, Y2);
		if (det == 0.)
			return Float.NaN;

		// Given line equations:
		// Pa = P1 + ua (P2-P1), and
		// Pb = P3 + ub (P4-P3)
		// and
		// V = (P1-P3)
		// then
		// ua = det(L2,V) / det(L1,L2)
		// ub = det(L1,V) / det(L1,L2)
		float u = determinant(X1, Y1, x1 - x3, y1 - y3) / det;
		if (u < 0. || u > 1.)
			return Float.NaN;
		u = determinant(X2, Y2, x1 - x3, y1 - y3) / det;
		return (u < 0. || u > 1.) ? Float.NaN : u;
	}

	/** Compute the determinant of two vectors.
	 * <p>
	 * <pre><code>det(X1,X2) = |X1|.|X2|.sin(a)</code></pre>
	 * where <code>X1</code> and <code>X2</code> are two vectors
	 * and <code>a</code> is the angle between <code>X1</code>
	 * and <code>X2</code>.
	 *
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @return the determinant
	 * @deprecated
	 */
	@Deprecated
	@SuppressWarnings("checkstyle:all")
	public static float determinant(float x1, float y1, float x2, float y2) {
		return x1*y2 - x2*y1;
	}

	/**
	 * Replies the intersection point between two segments.
	 *
	 * @param x1
	 *            is the X coordinate of the first point of the first segment.
	 * @param y1
	 *            is the Y coordinate of the first point of the first segment.
	 * @param x2
	 *            is the X coordinate of the second point of the first segment.
	 * @param y2
	 *            is the Y coordinate of the second point of the first segment.
	 * @param x3
	 *            is the X coordinate of the first point of the second segment.
	 * @param y3
	 *            is the Y coordinate of the first point of the second segment.
	 * @param x4
	 *            is the X coordinate of the second point of the second segment.
	 * @param y4
	 *            is the Y coordinate of the second point of the second segment.
	 * @return the intersection point or <code>null</code> if none.
	 * @deprecated
	 */
	@Deprecated
	@SuppressWarnings("checkstyle:all")
	public static Point2D getSegmentSegmentIntersectionPoint(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4) {
		float m = getSegmentSegmentIntersectionFactor(x1, y1, x2, y2, x3, y3, x4, y4);
		if (Float.isNaN(m))
			return null;
		return new Point2f(x1 + m * (x2 - x1), y1 + m * (y2 - y1));
	}

	/** Compute the interpolate point between the two points.
	 *
	 * @param p1
	 * @param p2
	 * @param factor is between 0 and 1; 0 for p1, and 1 for p2.
	 * @return the interpolate point.
	 * @deprecated
	 */
	@Deprecated
	@SuppressWarnings("checkstyle:all")
	public static Point2D interpolate(Point2D p1, Point2D p2, float factor) {
		return interpolate(p1.getX(), p1.getY(), p2.getX(), p2.getY(), factor);
	}

	/** Compute the interpolate point between the two points.
	 *
	 * @param p1x
	 * @param p1y
	 * @param p2x
	 * @param p2y
	 * @param factor is between 0 and 1; 0 for p1, and 1 for p2.
	 * @return the interpolate point.
	 * @deprecated
	 */
	@Deprecated
	@SuppressWarnings("checkstyle:all")
	public static Point2D interpolate(float p1x, float p1y, float p2x, float p2y, float factor) {
		float f = (factor<0f) ? 0f : factor;
		if (f>1f) f = 1f;
		float vx = p2x - p1x;
		float vy = p2y - p1y;
		return new Point2f(
				p1x + factor * vx,
				p1y + factor * vy);
	}

	/** Compute the dot product of two vectors.
	 *
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @return the dot product.
	 * @deprecated
	 */
	@Deprecated
	@SuppressWarnings("checkstyle:all")
	public static float dotProduct(float x1, float y1, float x2, float y2) {
		return x1*x2 + y1*y2;
	}

	/**
	 * Replies the relative counterclockwise (CCW) of a segment against a point. Returns an indicator of where
	 * the specified point {@code (px,py)} lies with respect to the line segment from {@code (x1,y1)}
	 *  to {@code (x2,y2)}. The return value can be either 1, -1, or 0 and indicates in which
	 *  direction the specified line must pivot around its first end point, {@code (x1,y1)}, in
	 *  order to point at the specified point {@code (px,py)}.
	 * In other words, given three point P1, P2, and P, is the segments (P1-P2-P) a counterclockwise turn?
	 * <p>
	 * In opposite to {@link #sidePointLine(float, float, float, float, float, float, boolean)},
	 * this function tries to classifies the point if it is colinear to the segment.
	 * The classification is explained below.
	 * <p>
	 * A return value of 1 indicates that the line segment must turn in the direction that takes the
	 * positive X axis towards the negative Y axis. In the default coordinate system used by Java 2D,
	 * this direction is counterclockwise.
	 * <p>
	 * A return value of -1 indicates that the line segment must turn in the direction that takes the
	 * positive X axis towards the positive Y axis. In the default coordinate system, this
	 * direction is clockwise.
	 * <p>
	 * A return value of 0 indicates that the point lies exactly on the line segment.
	 * Note that an indicator value of 0 is rare and not useful for determining colinearity
	 * because of floating point rounding issues.
	 * <p>
	 * If the point is colinear with the line segment, but not between the end points, then the value will be
	 * -1 if the point lies "beyond {@code (x1,y1)}" or 1 if the point lies "beyond {@code (x2,y2)}".
	 *
	 * @param x1
	 *            the X coordinate of the start point of the specified line segment
	 * @param y1
	 *            the Y coordinate of the start point of the specified line segment
	 * @param x2
	 *            the X coordinate of the end point of the specified line segment
	 * @param y2
	 *            the Y coordinate of the end point of the specified line segment
	 * @param px
	 *            the X coordinate of the specified point to be compared with the specified line segment
	 * @param py
	 *            the Y coordinate of the specified point to be compared with the specified line segment
	 * @param approximateZero
	 *            indicates if zero may be approximated or not.
	 * @return an integer that indicates the position of the third specified coordinates with respect to the line segment formed by the first two specified coordinates.
	 * @see #relativeDistancePointToLine(float, float, float, float, float, float)
	 * @see #sidePointLine(float, float, float, float, float, float, boolean)
	 * @deprecated
	 */
	@Deprecated
	@SuppressWarnings("checkstyle:all")
	public static int ccw(float x1, float y1, float x2, float y2, float px, float py, boolean approximateZero) {
		float cx2 = x2 - x1;
		float cy2 = y2 - y1;
		float cpx = px - x1;
		float cpy = py - y1;
		float ccw = cpx * cy2 - cpy * cx2;
		if ((approximateZero && isEpsilonZero(ccw)) || (!approximateZero && ccw == 0.)) {
			// The point is colinear, classify based on which side of
			// the segment the point falls on. We can calculate a
			// relative value using the projection of px,py onto the
			// segment - a negative value indicates the point projects
			// outside of the segment in the direction of the particular
			// endpoint used as the origin for the projection.
			ccw = cpx * cx2 + cpy * cy2;
			if (ccw > 0.) {
				// Reverse the projection to be relative to the original x2,y2
				// x2 and y2 are simply negated.
				// px and py need to have (x2 - x1) or (y2 - y1) subtracted
				// from them (based on the original values)
				// Since we really want to get a positive answer when the
				// point is "beyond (x2,y2)", then we want to calculate
				// the inverse anyway - thus we leave x2 & y2 negated.
				cpx -= cx2;
				cpy -= cy2;
				ccw = cpx * cx2 + cpy * cy2;
				if (ccw < 0f) {
					ccw = 0f;
				}
			}
		}
		return (ccw < 0f) ? -1 : ((ccw > 0f) ? 1 : 0);
	}

	/**
	 * Replies on which side of a line the given point is located.
	 * <p>
	 * A return value of 1 indicates that the line segment must turn in the direction
	 * that takes the positive X axis towards the negative Y axis. In the default
	 * coordinate system used by Java 2D, this direction is counterclockwise.
	 * <p>
	 * A return value of -1 indicates that the line segment must turn in the direction that takes the positive X axis towards the positive Y axis. In the default coordinate system, this direction is clockwise.
	 * <p>
	 * A return value of 0 indicates that the point lies exactly on the line segment. Note that an indicator value of 0 is rare and not useful for determining colinearity because of floating point rounding issues.
	 * <p>
	 * This function uses the equal-to-zero test with the error EPSILON.
	 * <p>
	 * In opposite of {@link #ccw(float, float, float, float, float, float, boolean)},
	 * this function does not try to classify the point if it is colinear
	 * to the segment. If the point is colinear, O is always returns.
	 *
	 * @param x1
	 *            the X coordinate of the start point of the specified line segment
	 * @param y1
	 *            the Y coordinate of the start point of the specified line segment
	 * @param x2
	 *            the X coordinate of the end point of the specified line segment
	 * @param y2
	 *            the Y coordinate of the end point of the specified line segment
	 * @param px
	 *            the X coordinate of the specified point to be compared with the specified line segment
	 * @param py
	 *            the Y coordinate of the specified point to be compared with the specified line segment
	 * @param approximateZero
	 *            indicates if zero may be approximated or not.
	 * @return an integer that indicates the position of the third specified coordinates with respect to the line segment formed by the first two specified coordinates.
	 * @see #relativeDistancePointToLine(float, float, float, float, float, float)
	 * @see #isEpsilonZero(double)
	 * @see #ccw(float, float, float, float, float, float, boolean)
	 * @deprecated
	 */
	@Deprecated
	@SuppressWarnings("checkstyle:all")
	public static int sidePointLine(float x1, float y1, float x2, float y2, float px, float py, boolean approximateZero) {
		float cx2 = x2 - x1;
		float cy2 = y2 - y1;
		float cpx = px - x1;
		float cpy = py - y1;
		float side = cpx * cy2 - cpy * cx2;
		if (approximateZero && side!=0f && isEpsilonZero(side)) {
			side = 0f;
		}
		return (side < 0f) ? -1 : ((side > 0f) ? 1 : 0);
	}

	/**
	 * Replies the relative distance from the given point to the given line.
	 * The replied distance may be negative, depending on which side of
	 * the line the point is.
	 *
	 * @param x1
	 *            the X coordinate of the start point of the specified line segment
	 * @param y1
	 *            the Y coordinate of the start point of the specified line segment
	 * @param x2
	 *            the X coordinate of the end point of the specified line segment
	 * @param y2
	 *            the Y coordinate of the end point of the specified line segment
	 * @param px
	 *            the X coordinate of the specified point to be compared with the specified line segment
	 * @param py
	 *            the Y coordinate of the specified point to be compared with the specified line segment
	 * @return the positive or negative distance from the point to the line
	 * @see #ccw(float, float, float, float, float, float, boolean)
	 * @see #sidePointLine(float, float, float, float, float, float, boolean)
	 * @see #distancePointToLine(float, float, float, float, float, float)
	 * @deprecated
	 */
	@Deprecated
	@SuppressWarnings("checkstyle:all")
	public static float relativeDistancePointToLine(float x1, float y1, float x2, float y2, float px, float py) {
		float r_denomenator = (x2-x1)*(x2-x1) + (y2-y1)*(y2-y1);
		if (r_denomenator==0.) return distancePointToPoint(px, py, x1, y1);
		float s = ((y1-py)*(x2-x1)-(x1-px)*(y2-y1) ) / r_denomenator;
		return (float)(s * Math.sqrt(r_denomenator));
	}

	/** Compute the intersection of two lines specified
	 * by the specified points and vectors.
	 *
	 * @param p1 is a point of the first line.
	 * @param v1 is the direction of the first line.
	 * @param p2 is a point of the second line.
	 * @param v2 is the direction of the second line.
	 * @return the intersection point or <code>null</code> if there is no intersection.
	 * @deprecated
	 */
	@Deprecated
	@SuppressWarnings("checkstyle:all")
	public static Point2D computeLineIntersection(Point2D p1, Vector2D v1, Point2D p2, Vector2D v2) {
		float x1 = p1.getX();
		float y1 = p1.getY();
		float x2 = x1 + v1.getX();
		float y2 = y1 + v1.getY();
		float x3 = p2.getX();
		float y3 = p2.getY();
		float x4 = x3 + v2.getX();
		float y4 = y3 + v2.getY();
		float denom = (y4-y3)*(x2-x1) - (x4-x3)*(y2-y1);
		if (denom==0.) return null;
		float ua = ((x4-x3)*(y1-y3) - (y4-y3)*(x1-x3));
		float ub = ((x2-x1)*(y1-y3) - (y2-y1)*(x1-x3));
		if (ua==ub) return null;
		ua = ua / denom;
		return new Point2f(
				x1 + ua * (x2 - x1),
				y1 + ua * (y2 - y1));
	}

	/**
	 * Replies the projection a point on a segment.
	 *
	 * @param px
	 *            is the coordiante of the point to project
	 * @param py
	 *            is the coordiante of the point to project
	 * @param s1x
	 *            is the x-coordinate of the first line point.
	 * @param s1y
	 *            is the y-coordinate of the first line point.
	 * @param s2x
	 *            is the x-coordinate of the second line point.
	 * @param s2y
	 *            is the y-coordinate of the second line point.
	 * @return the projection of the specified point on the line. If
	 * equal to {@code 0}, the projection is equal to the first segment point.
	 * If equal to {@code 1}, the projection is equal to the second segment point.
	 * If inside {@code ]0;1[}, the projection is between the two segment points.
	 * If inside {@code ]-inf;0[}, the projection is outside on the side of the
	 * first segment point. If inside {@code ]1;+inf[}, the projection is
	 * outside on the side of the second segment point.
	 * @deprecated
	 */
	@Deprecated
	@SuppressWarnings("checkstyle:all")
	public static float projectsPointOnLine(float px, float py, float s1x, float s1y, float s2x, float s2y) {
		float r_numerator = (px-s1x)*(s2x-s1x) + (py-s1y)*(s2y-s1y);
		float r_denomenator = (s2x-s1x)*(s2x-s1x) + (s2y-s1y)*(s2y-s1y);
		return r_numerator / r_denomenator;
	}

	/**
	 * Replies the position factory for the intersection point between two lines.
	 * <p>
	 * Let line equations for L1 and L2:<br>
	 * <code>L1: P1 + factor1 * (P2-P1)</code><br>
	 * <code>L2: P3 + factor2 * (P4-P3)</code><br>
	 * If lines are intersecting, then<br>
	 * <code>P1 + factor1 * (P2-P1) = P3 + factor2 * (P4-P3)</code>
	 * <p>
	 * This function computes and replies <code>factor1</code>.
	 *
	 * @param x1
	 *            is the X coordinate of the first point of the first line.
	 * @param y1
	 *            is the Y coordinate of the first point of the first line.
	 * @param x2
	 *            is the X coordinate of the second point of the first line.
	 * @param y2
	 *            is the Y coordinate of the second point of the first line.
	 * @param x3
	 *            is the X coordinate of the first point of the second line.
	 * @param y3
	 *            is the Y coordinate of the first point of the second line.
	 * @param x4
	 *            is the X coordinate of the second point of the second line.
	 * @param y4
	 *            is the Y coordinate of the second point of the second line.
	 * @return <code>factor1</code> or {@link Float#NaN} if no intersection.
	 * @deprecated
	 */
	@Deprecated
	@SuppressWarnings("checkstyle:all")
	public static float getLineLineIntersectionFactor(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4) {
		float X1 = x2 - x1;
		float Y1 = y2 - y1;
		float X2 = x4 - x3;
		float Y2 = y4 - y3;

		// determinant is zero when parallel = det(L1,L2)
		float det = MathUtil.determinant(X1, Y1, X2, Y2);
		if (det == 0.)
			return Float.NaN;

		// Given line equations:
		// Pa = P1 + ua (P2-P1), and
		// Pb = P3 + ub (P4-P3)
		// and
		// V = (P1-P3)
		// then
		// ua = det(L2,V) / det(L1,L2)
		// ub = det(L1,V) / det(L1,L2)
		return MathUtil.determinant(X2, Y2, x1 - x3, y1 - y3) / det;
	}

	/**
	 * Replies if the given point is inside the given ellipse.
	 *
	 * @param px is the point to test.
	 * @param py is the point to test.
	 * @param ellx is the min corner of the ellipse.
	 * @param elly is the min corner of the ellipse.
	 * @param ellw is the width of the ellipse.
	 * @param ellh is the height of the ellipse.
	 * @return <code>true</code> if the point is inside the ellipse;
	 * <code>false</code> if not.
	 * @deprecated
	 */
	@Deprecated
	@SuppressWarnings("checkstyle:all")
	public static boolean isPointInEllipse(float px, float py, float ellx, float elly, float ellw, float ellh) {
		// Copied from AWT Ellipse2D

		// Normalize the coordinates compared to the ellipse
		// having a center at 0,0 and a radius of 0.5.
		if (ellw <= 0f || ellh <= 0f) {
			return false;
		}
		float normx = (px - ellx) / ellw - 0.5f;
		float normy = (py - elly) / ellh - 0.5f;
		return (normx * normx + normy * normy) < 0.25f;
	}

	/**
	 * Replies if the given point is inside the given ellipse.
	 *
	 * @param px is the point to test.
	 * @param py is the point to test.
	 * @param cx is the center of the circle.
	 * @param cy is the center of the circle.
	 * @param radius is the radius of the circle.
	 * @return <code>true</code> if the point is inside the circle;
	 * <code>false</code> if not.
	 * @deprecated
	 */
	@Deprecated
	@SuppressWarnings("checkstyle:all")
	public static boolean isPointInCircle(float px, float py, float cx, float cy, float radius) {
		return distanceSquaredPointToPoint(
				px, py,
				cx, cy) <= (radius * radius);
	}

	/** Replies the closest point from the given point in the solid ellipse.
	 * A solid ellipse is an ellipse with a border and an interior area.
	 *
	 * @param px is the coordinate of the point.
	 * @param py is the coordinate of the point.
	 * @param ex is the coordinate of the min corner of the ellipse
	 * @param ey is the coordinate of the min corner of the ellipse
	 * @param ew is the width of the ellipse
	 * @param eh is the height of the ellipse
	 * @return the closest point in the ellipse
	 * @see #getClosestPointToSolidEllipse(float, float, float, float, float, float, boolean)
	 * @see #getClosestPointToShallowEllipse(float, float, float, float, float, float)
	 * @deprecated
	 */
	@Deprecated
	public static Point2D getClosestPointToSolidEllipse(float px, float py, float ex, float ey, float ew, float eh) {
		return getClosestPointToSolidEllipse(px, py, ex, ey, ew, eh, false);
	}

	/** Replies the closest point from the given point in the solid ellipse.
	 * A solid ellipse is an ellipse with a border and an interior area.
	 *
	 * @param px is the coordinate of the point.
	 * @param py is the coordinate of the point.
	 * @param ex is the coordinate of the min corner of the ellipse
	 * @param ey is the coordinate of the min corner of the ellipse
	 * @param ew is the width of the ellipse
	 * @param eh is the height of the ellipse
	 * @param returnNullWhenInside indicates if a <code>null</code> value
	 *     may be returned when the point is inside the ellipse, if
	 *     <code>true</code>; or a point all the time if <code>false</code>.
	 * @return the closest point in the ellipse
	 * @see #getClosestPointToShallowEllipse(float, float, float, float, float, float)
	 * @deprecated since 13.0
	 */
	@Deprecated
	@SuppressWarnings("checkstyle:all")
	public static Point2D getClosestPointToSolidEllipse(float px, float py, float ex, float ey, float ew, float eh, boolean returnNullWhenInside) {
		float x, y;
		if (ew<=0f || eh<=0f) {
			x = ex;
			y = ey;
		}
		else {
			// Normalize the coordinates compared to the ellipse
			// having a center at 0,0 and a radius of 0.5.
			float normx = (px - ex) / ew - 0.5f;
			float normy = (py - ey) / eh - 0.5f;
			if ((normx * normx + normy * normy) < 0.25f) {
				// The point is inside the ellipse
				if (returnNullWhenInside) return null;
				x = px;
				y = py;
			}
			else {
				// Compute the intersection between the ellipse
				// centered on (0;0) and the line (0;0)-(x0;y0)
				float a = ew / 2f;
				float b = eh / 2f;
				float x0 = px - (ex + a);
				float y0 = py - (ey + b);

				float denom = a*a*y0*y0 + b*b*x0*x0;
				assert(denom>0f); // because the "inside"-test should discard this case.

				denom = (float)Math.sqrt(denom);
				float factor = (a * b) / denom;
				x = factor * x0;
				y = factor * y0;

				// Translate the point to the original coordinate system
				x += (ex + a);
				y += (ey + b);
			}
		}
		return new Point2f(x, y);
	}

	/** Replies the closest point from the given point in the shallow ellipse.
	 * A shallow ellipse is an ellipse with a border and not an interior area.
	 *
	 * @param px is the coordinate of the point.
	 * @param py is the coordinate of the point.
	 * @param ex is the coordinate of the min corner of the ellipse
	 * @param ey is the coordinate of the min corner of the ellipse
	 * @param ew is the width of the ellipse
	 * @param eh is the height of the ellipse
	 * @return the closest point in the ellipse, or <code>null</code> if
	 * the given point is exactly at the center of the ellipse.
	 * @deprecated
	 */
	@Deprecated
	@SuppressWarnings("checkstyle:all")
	public static Point2D getClosestPointToShallowEllipse(float px, float py, float ex, float ey, float ew, float eh) {
		float x, y;
		if (ew<=0f || eh<=0f) {
			x = ex;
			y = ey;
		}
		else {
			// Compute the intersection between the ellipse
			// centered on (0;0) and the line (0;0)-(x0;y0)
			float a = ew / 2f;
			float b = eh / 2f;
			float x0 = px - (ex + a);
			float y0 = py - (ey + b);

			float denom = a*a*y0*y0 + b*b*x0*x0;
			if (denom==0f) {
				// The point is at the center of the ellipse.
				// Replies allways the same point.
				return null;
			}

			denom = (float)Math.sqrt(denom);
			float factor = (a * b) / denom;
			x = factor * x0;
			y = factor * y0;

			// Translate the point to the original coordinate system
			x += (ex + a);
			y += (ey + b);
		}
		return new Point2f(x, y);
	}

	/**
	 * Replies if two lines are parallel.
	 * <p>
	 * The given two lines are described respectivaly by two points, i.e. {@code (x1,y1)} and {@code (x2,y2)} for the first line, and {@code (x3,y3)} and {@code (x4,y4)} for the second line.
	 * <p>
	 * If you are interested to test if the two lines are colinear, see {@link #isCollinearLines(float, float, float, float, float, float, float, float)}.
	 *
	 * @param x1
	 *            is the X coordinate of the first point of the first line.
	 * @param y1
	 *            is the Y coordinate of the first point of the first line.
	 * @param x2
	 *            is the X coordinate of the second point of the first line.
	 * @param y2
	 *            is the Y coordinate of the second point of the first line.
	 * @param x3
	 *            is the X coordinate of the first point of the second line.
	 * @param y3
	 *            is the Y coordinate of the first point of the second line.
	 * @param x4
	 *            is the X coordinate of the second point of the second line.
	 * @param y4
	 *            is the Y coordinate of the second point of the second line.
	 * @return <code>true</code> if the two given lines are parallel.
	 * @see #isCollinearLines(float, float, float, float, float, float, float, float)
	 * @deprecated
	 */
	@Deprecated
	@SuppressWarnings("checkstyle:all")
	public static boolean isParallelLines(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4) {
		return isCollinearVectors(x2 - x1, y2 - y1, x4 - x3, y4 - y3);
	}

	/**
	 * Replies if two lines are parallel.
	 * <p>
	 * The given two lines are described respectivaly by two points, i.e. {@code (x1,y1,z1)} and {@code (x2,y2,z2)} for the first line, and {@code (x3,y3,z3)} and {@code (x4,y4,z4)} for the second line.
	 * <p>
	 * Line through (x<sub>0</sub>,y<sub>0</sub>,z<sub>0</sub>) in direction (a<sub>0</sub>,b<sub>0</sub>,c<sub>0</sub>) and line through (x<sub>1</sub>,yx<sub>1</sub>,zx<sub>1</sub>) in direction (ax<sub>1</sub>,bx<sub>1</sub>,cx<sub>1</sub>):
	 * 
	 * <p>
	 * Two lines specified by point and direction are coplanar if and only if the determinant in the numerator is zero. In this case they are concurrent (if the denominator is nonzero) or parallel (if the denominator is zero).
	 *
	 * @param x1
	 *            is the X coordinate of the first point of the first line.
	 * @param y1
	 *            is the Y coordinate of the first point of the first line.
	 * @param z1
	 *            is the Z coordinate of the first point of the first line.
	 * @param x2
	 *            is the X coordinate of the second point of the first line.
	 * @param y2
	 *            is the Y coordinate of the second point of the first line.
	 * @param z2
	 *            is the Z coordinate of the second point of the first line.
	 * @param x3
	 *            is the X coordinate of the first point of the second line.
	 * @param y3
	 *            is the Y coordinate of the first point of the second line.
	 * @param z3
	 *            is the Z coordinate of the first point of the second line.
	 * @param x4
	 *            is the X coordinate of the second point of the second line.
	 * @param y4
	 *            is the Y coordinate of the second point of the second line.
	 * @param z4
	 *            is the Z coordinate of the second point of the second line.
	 * @return <code>true</code> if the two given lines are parallel.
	 * @deprecated
	 */
	@Deprecated
	@SuppressWarnings("checkstyle:all")
	public static boolean isParallelLines(float x1, float y1, float z1, float x2, float y2, float z2, float x3, float y3, float z3, float x4, float y4, float z4) {
		return isCollinearVectors(x2 - x1, y2 - y1, z2 - z1, x4 - x3, y4 - y3, z4 - z3);
	}

	/**
	 * Replies if three points are colinear, ie. one the same line.
	 * <p>
	 * Trival approach is: points are collinear iff |AB| + |AC| = |BC|, where A B C are the three points.
	 * <p>
	 * This function uses the equal-to-zero test with the error EPSILON.
	 *
	 * @param x1
	 *            is the X coordinate of the first point
	 * @param y1
	 *            is the Y coordinate of the first point
	 * @param x2
	 *            is the X coordinate of the second point
	 * @param y2
	 *            is the Y coordinate of the second point
	 * @param x3
	 *            is the X coordinate of the third point
	 * @param y3
	 *            is the Y coordinate of the third point
	 * @return <code>true</code> if the three given points are colinear.
	 * @deprecated
	 */
	@Deprecated
	@SuppressWarnings("checkstyle:all")
	public static boolean isCollinearPoints(float x1, float y1, float x2, float y2, float x3, float y3) {
		// Test if three points are colinears
		// iff det( [ x1 x2 x3 ]
		// [ y1 y2 y3 ]
		// [ 1 1 1 ] ) = 0
		// Do not invoked MathUtil.determinant() to limit computations.
		return isEpsilonZero(x1 * (y2 - y3) + x2 * (y3 - y1) + x3 * (y1 - y2));
	}

	/**
	 * Replies if three points are colinear, ie. one the same line.
	 * <p>
	 * Trival approach is: points are collinear iff |AB| + |AC| = |BC|, where A B C are the three points.
	 * <p>
	 * This function uses the equal-to-zero test with the error EPSILON.
	 *
	 * @param x1
	 *            is the X coordinate of the first point
	 * @param y1
	 *            is the Y coordinate of the first point
	 * @param z1
	 *            is the Z coordinate of the first point
	 * @param x2
	 *            is the X coordinate of the second point
	 * @param y2
	 *            is the Y coordinate of the second point
	 * @param z2
	 *            is the Z coordinate of the second point
	 * @param x3
	 *            is the X coordinate of the third point
	 * @param y3
	 *            is the Y coordinate of the third point
	 * @param z3
	 *            is the Z coordinate of the third point
	 * @return <code>true</code> if the three given points are colinear.
	 * @deprecated
	 */
	@Deprecated
	@SuppressWarnings("checkstyle:all")
	public static boolean isCollinearPoints(float x1, float y1, float z1, float x2, float y2, float z2, float x3, float y3, float z3) {
		float dx1 = x2 - x1;
		float dy1 = y2 - y1;
		float dz1 = z2 - z1;
		float dx2 = x3 - x1;
		float dy2 = y3 - y1;
		float dz2 = z3 - z1;

		float cx = dy1 * dz2 - dy2 * dz1;
		float cy = dx2 * dz1 - dx1 * dz2;
		float cz = dx1 * dy2 - dx2 * dy1;

		float sum = cx * cx + cy * cy + cz * cz;

		return isEpsilonZero(sum);
	}

	/**
	 * Replies if two vectors are colinear.
	 * <p>
	 * This function uses the equal-to-zero test with the error EPSILON.
	 *
	 * @param x1
	 *            is the X coordinate of the first vector
	 * @param y1
	 *            is the Y coordinate of the first vector
	 * @param x2
	 *            is the X coordinate of the second vector
	 * @param y2
	 *            is the Y coordinate of the second vector
	 * @return <code>true</code> if the two given vectors are colinear.
	 * @deprecated
	 */
	@Deprecated
	@SuppressWarnings("checkstyle:all")
	public static boolean isCollinearVectors(float x1, float y1, float x2, float y2) {
		// Test if three points are colinears
		// iff det( [ x1 x2 x3 ]
		// [ y1 y2 y3 ]
		// [ 1 1 1 ] ) = 0
		// Do not invoked MathUtil.determinant() to limit computation consumption.
		return isEpsilonZero(x1 * y2 - x2 * y1);
	}

	/**
	 * Replies if two vectors are colinear.
	 * <p>
	 * This function uses the equal-to-zero test with the error EPSILON.
	 *
	 * @param x1
	 *            is the X coordinate of the first vector
	 * @param y1
	 *            is the Y coordinate of the first vector
	 * @param z1
	 *            is the Z coordinate of the first vector
	 * @param x2
	 *            is the X coordinate of the second vector
	 * @param y2
	 *            is the Y coordinate of the second vector
	 * @param z2
	 *            is the Z coordinate of the second vector
	 * @return <code>true</code> if the two given vectors are colinear.
	 * @deprecated
	 */
	@Deprecated
	@SuppressWarnings("checkstyle:all")
	public static boolean isCollinearVectors(float x1, float y1, float z1, float x2, float y2, float z2) {
		// Cross product
		float cx = y1 * z2 - z1 * y2;
		float cy = z1 * x2 - x1 * z2;
		float cz = x1 * y2 - y1 * x2;

		float sum = cx * cx + cy * cy + cz * cz;

		return isEpsilonZero(sum);
	}

	/**
	 * Replies if two lines are colinear.
	 * <p>
	 * The given two lines are described respectivaly by two points, i.e. {@code (x1,y1)} and {@code (x2,y2)} for the first line, and {@code (x3,y3)} and {@code (x4,y4)} for the second line.
	 * <p>
	 * If you are interested to test if the two lines are parallel, see {@link #isParallelLines(float, float, float, float, float, float, float, float)}.
	 *
	 * @param x1
	 *            is the X coordinate of the first point of the first line.
	 * @param y1
	 *            is the Y coordinate of the first point of the first line.
	 * @param x2
	 *            is the X coordinate of the second point of the first line.
	 * @param y2
	 *            is the Y coordinate of the second point of the first line.
	 * @param x3
	 *            is the X coordinate of the first point of the second line.
	 * @param y3
	 *            is the Y coordinate of the first point of the second line.
	 * @param x4
	 *            is the X coordinate of the second point of the second line.
	 * @param y4
	 *            is the Y coordinate of the second point of the second line.
	 * @return <code>true</code> if the two given lines are collinear.
	 * @see #isParallelLines(float, float, float, float, float, float, float, float)
	 * @see #isCollinearPoints(float, float, float, float, float, float)
	 * @deprecated
	 */
	@Deprecated
	@SuppressWarnings("checkstyle:all")
	public static boolean isCollinearLines(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4) {
		return (isParallelLines(x1, y1, x2, y2, x3, y3, x4, y4) && isCollinearPoints(x1, y1, x2, y2, x3, y3));
	}

	/**
	 * Replies if two lines are colinear.
	 * <p>
	 * The given two lines are described respectivaly by two points, i.e. {@code (x1,y1,z1)} and {@code (x2,y2,z2)} for the first line, and {@code (x3,y3,z3)} and {@code (x4,y4,z4)} for the second line.
	 * <p>
	 * If you are interested to test if the two lines are parallel, see {@link #isParallelLines(float, float, float, float, float, float, float, float, float, float, float, float)}.
	 *
	 * @param x1
	 *            is the X coordinate of the first point of the first line.
	 * @param y1
	 *            is the Y coordinate of the first point of the first line.
	 * @param z1
	 *            is the Z coordinate of the first point of the first line.
	 * @param x2
	 *            is the X coordinate of the second point of the first line.
	 * @param y2
	 *            is the Y coordinate of the second point of the first line.
	 * @param z2
	 *            is the Z coordinate of the second point of the first line.
	 * @param x3
	 *            is the X coordinate of the first point of the second line.
	 * @param y3
	 *            is the Y coordinate of the first point of the second line.
	 * @param z3
	 *            is the Z coordinate of the first point of the second line.
	 * @param x4
	 *            is the X coordinate of the second point of the second line.
	 * @param y4
	 *            is the Y coordinate of the second point of the second line.
	 * @param z4
	 *            is the Z coordinate of the second point of the second line.
	 * @return <code>true</code> if the two given lines are collinear.
	 * @see #isParallelLines(float, float, float, float, float, float, float, float, float, float, float, float)
	 * @see #isCollinearPoints(float, float, float, float, float, float, float, float, float)
	 * @deprecated
	 */
	@Deprecated
	@SuppressWarnings("checkstyle:all")
	public static boolean isCollinearLines(float x1, float y1, float z1, float x2, float y2, float z2, float x3, float y3, float z3, float x4, float y4, float z4) {
		return (isParallelLines(x1, y1, z1, x2, y2, z2, x3, y3, z3, x4, y4, z4) && isCollinearPoints(x1, y1, z1, x2, y2, z2, x3, y3, z3));
	}

	/** Compute the zone where the point is against the given rectangle
	 * according to the <a href="http://en.wikipedia.org/wiki/Cohen%E2%80%93Sutherland_algorithm">Cohen-Sutherland algorithm</a>.
	 *
	 * @param px is the coordinates of the points.
	 * @param py is the coordinates of the points.
	 * @param rxmin is the min of the coordinates of the rectangle.
	 * @param rymin is the min of the coordinates of the rectangle.
	 * @param rxmax is the max of the coordinates of the rectangle.
	 * @param rymax is the max of the coordinates of the rectangle.
	 * @return the zone
	 * @deprecated since 13.0, see {@link #getCohenSutherlandCode(double, double, double, double, double, double)}
	 */
	@Deprecated
	@SuppressWarnings("checkstyle:all")
	public static int getCohenSutherlandCode(float px, float py, float rxmin, float rymin, float rxmax, float rymax) {
		assert(rxmin<=rxmax);
		assert(rymin<=rymax);
		// initialised as being inside of clip window
		int code = COHEN_SUTHERLAND_INSIDE;
		if (px<rxmin) {
			// to the left of clip window
			code |= COHEN_SUTHERLAND_LEFT;
		}
		if (px>rxmax) {
			// to the right of clip window
			code |= COHEN_SUTHERLAND_RIGHT;
		}
		if (py<rymin) {
			// to the bottom of clip window
			code |= COHEN_SUTHERLAND_BOTTOM;
		}
		if (py>rymax) {
			// to the top of clip window
			code |= COHEN_SUTHERLAND_TOP;
		}
		return code;
	}

	/** Clip the given segment against the clipping rectangle
	 * according to the <a href="http://en.wikipedia.org/wiki/Cohen%E2%80%93Sutherland_algorithm">Cohen-Sutherland algorithm</a>.
	 *
	 * @param p1 is the first point of the segment.
	 * @param p2 is the first point of the segment.
	 * @param rxmin is the min of the coordinates of the rectangle.
	 * @param rymin is the min of the coordinates of the rectangle.
	 * @param rxmax is the max of the coordinates of the rectangle.
	 * @param rymax is the max of the coordinates of the rectangle.
	 * @return <code>true</code> if the segment has an intersection with the
	 * rectangle and the segment was clipped; <code>false</code> if the segment
	 * does not intersect the rectangle.
	 * @deprecated since 13.0, see {@link #getCohenSutherlandCode(double, double, double, double, double, double)}
	 */
	@Deprecated
	@SuppressWarnings("checkstyle:all")
	public static boolean clipSegmentToRectangle(Point2D p1, Point2D p2, float rxmin, float rymin, float rxmax, float rymax) {
		float x0 = p1.getX();
		float y0 = p1.getY();
		float x1 = p2.getX();
		float y1 = p2.getY();
		int code1 = getCohenSutherlandCode(x0, y0, rxmin, rymin, rxmax, rymax);
		int code2 = getCohenSutherlandCode(x1, y1, rxmin, rymin, rxmax, rymax);
		boolean accept = false;
		boolean cont = true;
		float x, y;
		x = y = 0;

		while (cont) {
			if ((code1 | code2)==0) {
				// Bitwise OR is 0. Trivially accept and get out of loop
				accept = true;
				cont = false;
			}
			else if ((code1 & code2)!=0) {
				// Bitwise AND is not 0. Trivially reject and get out of loop
				cont = false;
			}
			else {
				// failed both tests, so calculate the line segment to clip
				// from an outside point to an intersection with clip edge

				// At least one endpoint is outside the clip rectangle; pick it.
				int code3 = code1!=0 ? code1 : code2;

				// Now find the intersection point;
				// use formulas y = y0 + slope * (x - x0), x = x0 + (1 / slope) * (y - y0)
				if ((code3 & COHEN_SUTHERLAND_TOP)!=0) {
					// point is above the clip rectangle
					x = x0 + (x1 - x0) * (rymax - y0) / (y1 - y0);
					y = rymax;
				}
				else if ((code3 & COHEN_SUTHERLAND_BOTTOM)!=0) {
					// point is below the clip rectangle
					x = x0 + (x1 - x0) * (rymin - y0) / (y1 - y0);
					y = rymin;
				}
				else if ((code3 & COHEN_SUTHERLAND_RIGHT)!=0) {
					// point is to the right of clip rectangle
					y = y0 + (y1 - y0) * (rxmax - x0) / (x1 - x0);
					x = rxmax;
				}
				else if ((code3 & COHEN_SUTHERLAND_LEFT)!=0) {
					// point is to the left of clip rectangle
					y = y0 + (y1 - y0) * (rxmin - x0) / (x1 - x0);
					x = rxmin;
				}
				else {
					code3 = 0;
				}

				if (code3!=0) {
					// Now we move outside point to intersection point to clip
					// and get ready for next pass.
					if (code3 == code1) {
						x0 = x;
						y0 = y;
						code1 = getCohenSutherlandCode(x0, y0, rxmin, rymin, rxmax, rymax);
					}
					else {
						x1 = x;
						y1 = y;
						code2 = getCohenSutherlandCode(x1, y1, rxmin, rymin, rxmax, rymax);
					}
				}
			}
		}
		if (accept) {
			p1.set(x0, y0);
			p2.set(x1, y1);
		}
		return accept;
	}

	/** Clip the given segment against the clipping rectangle
	 * according to the <a href="http://en.wikipedia.org/wiki/Cohen%E2%80%93Sutherland_algorithm">Cohen-Sutherland algorithm</a>.
	 *
	 * @param p1 is the first point of the segment.
	 * @param p2 is the first point of the segment.
	 * @param rxmin is the min of the coordinates of the rectangle.
	 * @param rymin is the min of the coordinates of the rectangle.
	 * @param rxmax is the max of the coordinates of the rectangle.
	 * @param rymax is the max of the coordinates of the rectangle.
	 * @return <code>true</code> if the segment has an intersection with the
	 * rectangle and the segment was clipped; <code>false</code> if the segment
	 * does not intersect the rectangle.
	 * @deprecated since 13.0, see {@link #getCohenSutherlandCode(double, double, double, double, double, double)}
	 */
	@Deprecated
	@SuppressWarnings("checkstyle:all")
	public static boolean clipSegmentToRectangle(Point2D p1, Point2D p2, int rxmin, int rymin, int rxmax, int rymax) {
		int x0 = p1.x();
		int y0 = p1.y();
		int x1 = p2.x();
		int y1 = p2.y();
		int code1 = getCohenSutherlandCode(x0, y0, rxmin, rymin, rxmax, rymax);
		int code2 = getCohenSutherlandCode(x1, y1, rxmin, rymin, rxmax, rymax);
		boolean accept = false;
		boolean cont = true;
		int x, y;
		x = y = 0;

		while (cont) {
			if ((code1 | code2)==0) {
				// Bitwise OR is 0. Trivially accept and get out of loop
				accept = true;
				cont = false;
			}
			else if ((code1 & code2)!=0) {
				// Bitwise AND is not 0. Trivially reject and get out of loop
				cont = false;
			}
			else {
				// failed both tests, so calculate the line segment to clip
				// from an outside point to an intersection with clip edge

				// At least one endpoint is outside the clip rectangle; pick it.
				int code3 = code1!=0 ? code1 : code2;

				// Now find the intersection point;
				// use formulas y = y0 + slope * (x - x0), x = x0 + (1 / slope) * (y - y0)
				if ((code3 & COHEN_SUTHERLAND_TOP)!=0) {
					// point is above the clip rectangle
					x = x0 + (x1 - x0) * (rymax - y0) / (y1 - y0);
					y = rymax;
				}
				else if ((code3 & COHEN_SUTHERLAND_BOTTOM)!=0) {
					// point is below the clip rectangle
					x = x0 + (x1 - x0) * (rymin - y0) / (y1 - y0);
					y = rymin;
				}
				else if ((code3 & COHEN_SUTHERLAND_RIGHT)!=0) {
					// point is to the right of clip rectangle
					y = y0 + (y1 - y0) * (rxmax - x0) / (x1 - x0);
					x = rxmax;
				}
				else if ((code3 & COHEN_SUTHERLAND_LEFT)!=0) {
					// point is to the left of clip rectangle
					y = y0 + (y1 - y0) * (rxmin - x0) / (x1 - x0);
					x = rxmin;
				}
				else {
					code3 = 0;
				}

				if (code3!=0) {
					// Now we move outside point to intersection point to clip
					// and get ready for next pass.
					if (code3 == code1) {
						x0 = x;
						y0 = y;
						code1 = getCohenSutherlandCode(x0, y0, rxmin, rymin, rxmax, rymax);
					}
					else {
						x1 = x;
						y1 = y;
						code2 = getCohenSutherlandCode(x1, y1, rxmin, rymin, rxmax, rymax);
					}
				}
			}
		}
		if (accept) {
			p1.set(x0, y0);
			p2.set(x1, y1);
		}
		return accept;
	}

}
