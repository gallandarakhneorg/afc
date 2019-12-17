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
	 * <p>If you would like to treat the zero value as a positive number, see {@link #signNoZero(double)}.
	 *
	 * @param value the floating-point value whose sign is to be returned
	 * @return the sign of the argument, {@code -1}, {@code 0} or {@code 1}.
	 * @see #signNoZero(double)
	 * @see Math#signum(double)
	 */
	@Pure
	@Inline(value = "($1 == 0. || Double.isNaN($1)) ? 0 : (($1 < -0.) ? -1 : 1)")
	public static int sign(double value) {
		return (value == 0. || Double.isNaN(value)) ? 0 : ((value < -0.) ? -1 : 1);
	}

	/**
	 * Returns the sign of the argument; 1 if the argument is greater than or equal to zero, -1 if the
	 * argument is less than zero.
	 *
	 * <p>This function differs from {@link #sign(double)} because it
	 * assumes the zero value has a positive sign.
	 *
	 * @param value the floating-point value whose sign is to be returned
	 * @return the sign of the argument, {@code -1} or {@code 1}.
	 * @see #sign(double)
	 * @see Math#signum(double)
	 * @since 16.0
	 */
	@Pure
	@Inline(value = "(($1 < -0.) ? -1 : 1)")
	public static int signNoZero(double value) {
		return (value < -0.) ? -1 : 1;
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

}
