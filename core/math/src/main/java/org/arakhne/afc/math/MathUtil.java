/* 
 * $Id$
 * 
 * Copyright (C) 2010-2011 Janus Core Developers
 * Copyright (C) 2012 Stéphane GALLAND
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.arakhne.afc.math;

/**
 * Mathematic utilities.
 * 
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public final class MathUtil implements MathConstants {

	private MathUtil() {
		//
	}

	/**
	 * Clamp the given angle in radians to {@code [0;2PI)}.
	 * 
	 * @param radians
	 *            is the angle to clamp
	 * @return the angle in {@code [0;2PI)} range.
	 */
	public static float clampRadian(float radians) {
		float r = radians;
		while (r < 0f)
			r += TWO_PI;
		while (r >= TWO_PI)
			r -= TWO_PI;
		return r;
	}

	/**
	 * Clamp the given value to the given range.
	 * <p>
	 * If the value is outside the {@code [min;max]} range, it is clamp to the
	 * nearest bounding value <var>min</var> or <var>max</var>.
	 * 
	 * @param v
	 *            is the value to clamp.
	 * @param min
	 *            is the min value of the range.
	 * @param max
	 *            is the max value of the range.
	 * @return the value in {@code [min;max]} range.
	 */
	public static float clamp(float v, float min, float max) {
		if (min < max) {
			if (v < min)
				return min;
			if (v > max)
				return max;
		} else {
			if (v > min)
				return min;
			if (v < max)
				return max;
		}
		return v;
	}

	/**
	 * Replies if the given value is near zero.
	 * 
	 * @param value
	 *            is the value to test.
	 * @param epsilon
	 * 			  the accuracy parameter must be the same unit of measurement as the other parameter 
	 * @return <code>true</code> if the given <var>value</var> is near zero,
	 *         otherwise <code>false</code>.
	 */
	public static boolean isEpsilonZero(float value, float epsilon) {
		return Math.abs(value) <= epsilon;
	}

	/**
	 * Replies if the given values are near.
	 * 
	 * @param v1
	 * @param v2
	 * @param epsilon
	 * 			  the accuracy parameter must be the same unit of measurement as others parameters 
	 * @return <code>true</code> if the given <var>v1</var> is near
	 *         <var>v2</var>, otherwise <code>false</code>.
	 */
	public static boolean isEpsilonEqual(float v1, float v2 , float epsilon) {
		return Math.abs(v1 - v2) <= epsilon;
	}

	/**
	 * Replies the max value.
	 * 
	 * @param values
	 *            are the values to scan.
	 * @return the max value.
	 */
	public static float max(float... values) {
		if (values == null || values.length == 0)
			return Float.NaN;
		float m = values[0];
		for (float v : values) {
			if (v > m)
				m = v;
		}
		return m;
	}

	/**
	 * Replies the max value.
	 * 
	 * @param values
	 *            are the values to scan.
	 * @return the max value.
	 */
	public static int max(int... values) {
		if (values == null || values.length == 0)
			return 0;
		int m = values[0];
		for (int v : values) {
			if (v > m)
				m = v;
		}
		return m;
	}

	/**
	 * Replies the min value.
	 * 
	 * @param values
	 *            are the values to scan.
	 * @return the min value.
	 */
	public static float min(float... values) {
		if (values == null || values.length == 0)
			return Float.NaN;
		float m = values[0];
		for (float v : values) {
			if (v < m)
				m = v;
		}
		return m;
	}

	/**
	 * Replies the min value.
	 * 
	 * @param values
	 *            are the values to scan.
	 * @return the min value.
	 */
	public static int min(int... values) {
		if (values == null || values.length == 0)
			return 0;
		int m = values[0];
		for (int v : values) {
			if (v < m)
				m = v;
		}
		return m;
	}

	/**
	 * Replies the specified angle translated between 0 and 2PI.
	 * 
	 * @param radian
	 *            is an angle
	 * @return normalized angle
	 */
	public static float clampRadian0To2PI(float radian) {
		if ((!Float.isNaN(radian)) && (!Float.isInfinite(radian))) {
			return clampTrigo(radian, 0, 2f * PI);
		}
		return radian;
	}

	/**
	 * Replies the specified angle translated between -PI and PI.
	 * 
	 * @param radian
	 *            is an angle
	 * @return normalized angle
	 */
	public static float clampRadianMinusPIToPI(float radian) {
		if ((!Float.isNaN(radian)) && (!Float.isInfinite(radian))) {
			return clampTrigo(radian, -PI, PI);
		}
		return radian;
	}

	/**
	 * Replies the specified angle translated between 0 and 360.
	 * 
	 * @param degree
	 *            is an angle
	 * @return normalized angle
	 */
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
	 */
	public static float clampDegreeMinus180To180(float degree) {
		if ((!Float.isNaN(degree)) && (!Float.isInfinite(degree))) {
			return clampTrigo(degree, -180, 180);
		}
		return degree;
	}

	/**
	 * Clamp the given value to fit between the min and max values according to
	 * a trigonometric-like heuristic. If the given value is not between the
	 * minimum and maximum values and the value is positive, the value is modulo
	 * the perimeter of the counterclockwise circle. If the given value is not
	 * between the minimum and maximum values and the value is negative, the
	 * value is modulo the perimeter of the clockwise circle. The perimeter is
	 * the distance between <var>min</var> and <var>max</var>.
	 * 
	 * @param value
	 * @param min
	 * @param max
	 * @return the clamped value
	 */
	public static float clampTrigo(float value, float min, float max) {
		if (Float.isNaN(max)) { // NaN is lower than all the number according to
								// float.compareTo()
			return Float.NaN;
		}
		if (Float.isNaN(min)) {
			// Clamp max only
			if (value > max)
				return max - Float.MAX_VALUE + value;
		} else {
			assert (min <= max);
			if (min == max)
				return min; // special case: empty interval
			if (value < min || value > max) {
				float perimeter = max - min;
				float nvalue = value - min;
				float rest = nvalue % perimeter;
				return (value < 0) ? max + rest : rest + min;
			}
		}
		return value;
	}

	/**
	 * Replies the <var>value</var> clamped to the nearest bounds. If
	 * |<var>value</var>-<var>minBounds</var>| &gt;
	 * |<var>value</var>-<var>maxBounds</var>| then the returned value is
	 * <var>maxBounds</var>; otherwise it is <var>minBounds</var>.
	 * 
	 * @param value
	 *            is the value to clamp.
	 * @param minBounds
	 *            is the minimal allowed value.
	 * @param maxBounds
	 *            is the maximal allowed value.
	 * @return <var>minBounds</var> or <var>maxBounds</var>.
	 */
	public static float clampToNearestBounds(float value, float minBounds,
			float maxBounds) {
		assert (minBounds <= maxBounds);
		float center = (minBounds + maxBounds) / 2f;
		if (value <= center)
			return minBounds;
		return maxBounds;
	}

	/**
	 * Compute the determinant of two vectors.
	 * <p>
	 * 
	 * <pre>
	 * <code>det(X1,X2) = |X1|.|X2|.sin(a)</code>
	 * </pre>
	 * 
	 * where <code>X1</code> and <code>X2</code> are two vectors and
	 * <code>a</code> is the angle between <code>X1</code> and <code>X2</code>.
	 * 
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @return the determinant
	 */
	public static float determinant(float x1, float y1, float x2, float y2) {
		return x1 * y2 - x2 * y1;
	}

	/**
	 * Compute the dot product of two 2D vectors.
	 * 
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @return the dot product.
	 */
	public static float dotProduct(float x1, float y1, float x2, float y2) {
		return x1 * x2 + y1 * y2;
	}

	/**
	 * Compute the dot product of two 3D vectors.
	 * 
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @return the dot product.
	 */
	public static float dotProduct(float x1, float y1, float z1, float x2,
			float y2, float z2) {
		return x1 * x2 + y1 * y2 + z1 * z2;
	}

	/**
	 * Replies if the first specified value is approximatively equal, less or
	 * greater than to the second sepcified value.
	 * <p>
	 * This operator uses the following table:
	 * <table>
	 * <thead>
	 * <tr>
	 * <th><var>value1</var></th>
	 * <th><var>value2</var></th>
	 * <th>Result</th>
	 * </tr>
	 * </thead> <tbody>
	 * <tr>
	 * <td><code>NaN</code></td>
	 * <td>any number</td>
	 * <td><code>1</code></td>
	 * </tr>
	 * <tr>
	 * <td>any number</td>
	 * <td><code>NaN</code></td>
	 * <td><code>-1</code></td>
	 * </tr>
	 * <tr>
	 * <td><code>n1</code> in [-Infinity;n2[</td>
	 * <td><code>n2</code> in ]n1;+Infinity]</td>
	 * <td><code>-1</code></td>
	 * </tr>
	 * <tr>
	 * <td><code>n1</code> in [-Infinity;Infinity]</td>
	 * <td><code>n2</code> in [n1-epsilon;n1+epsilon]</td>
	 * <td><code>0</code></td>
	 * </tr>
	 * <tr>
	 * <td><code>n1</code> in ]n2;Infinity]</td>
	 * <td><code>n2</code> in [-Infinity;n1[</td>
	 * <td><code>1</code></td>
	 * </tr>
	 * </tbody>
	 * </table>
	 * <p>
	 * <code>NaN</code> is considered by this method to be equal to itself and
	 * greater than all other according to the definition of {@link
	 * float#compareTo(float)}.
	 * 
	 * @param value1
	 * @param value2
	 * @param epsilon the accuracy parameter must be the same unit of measurement as others parameters 
	 * @return a negative value if the parameter <var>value1</var> is lower than
	 *         <var>value2</var>, a positive if <var>value1</var> is greater
	 *         than <var>value2</var>, zero if the two parameters are
	 *         approximatively equal.
	 */
	public static int epsilonCompareTo(float value1, float value2, float epsilon) {
		if (Float.isNaN(value1) || Float.isNaN(value2)
				|| Float.isInfinite(value1) || Float.isInfinite(value2)) {
			return Float.compare(value1, value2);
		}
		return (Math.abs(value1 - value2) <= epsilon) ? 0 : Float.compare(
				value1, value2);
	}

}