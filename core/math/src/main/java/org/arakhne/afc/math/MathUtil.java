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

import static org.arakhne.afc.math.MathConstants.COHEN_SUTHERLAND_BOTTOM;
import static org.arakhne.afc.math.MathConstants.COHEN_SUTHERLAND_INSIDE;
import static org.arakhne.afc.math.MathConstants.COHEN_SUTHERLAND_LEFT;
import static org.arakhne.afc.math.MathConstants.COHEN_SUTHERLAND_RIGHT;
import static org.arakhne.afc.math.MathConstants.COHEN_SUTHERLAND_TOP;
import static org.arakhne.afc.math.MathConstants.EPSILON;

import org.arakhne.afc.math.geometry.d3.continuous.Vector3f;
import org.arakhne.afc.util.Pair;

/** Mathematic and geometric utilities.
 * 
 * @author $Author: galland$
 * @author $Author: hjaffali$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public final class MathUtil {

	private MathUtil() {
		//
	}

	/** Clamp the given value to the given range.
	 * <p>
	 * If the value is outside the {@code [min;max]}
	 * range, it is clamp to the nearest bounding value
	 * <var>min</var> or <var>max</var>.
	 * 
	 * @param v is the value to clamp.
	 * @param min is the min value of the range.
	 * @param max is the max value of the range.
	 * @return the value in {@code [min;max]} range.
	 */
	public static double clamp(double v, double min, double max) {
		if (min<max) {
			if (v<min) return min;
			if (v>max) return max;
		}
		else {
			if (v>min) return min;
			if (v<max) return max;
		}
		return v;
	}	

	/** Replies if the given value is near zero.
	 * 
	 * @param value is the value to test.
	 * @return <code>true</code> if the given <var>value</var>
	 * is near zero, otherwise <code>false</code>.
	 * @see MathConstants#EPSILON
	 */
	public static boolean isEpsilonZero(double value) {
		return Math.abs(value) <= EPSILON;
	}

	/** Replies if the given value is near zero.
	 * 
	 * @param value is the value to test.
	 * @param epsilon the approximation epsilon.
	 * @return <code>true</code> if the given <var>value</var>
	 * is near zero, otherwise <code>false</code>.
	 */
	public static boolean isEpsilonZero(double value, double epsilon) {
		return Math.abs(value) <= epsilon;
	}

	/** Replies if the given values are near.
	 * 
	 * @param v1
	 * @param v2
	 * @return <code>true</code> if the given <var>v1</var>
	 * is near <var>v2</var>, otherwise <code>false</code>.
	 * @see MathConstants#EPSILON
	 */
	public static boolean isEpsilonEqual(double v1, double v2) {
		return Math.abs(v1 - v2) <= EPSILON;
	}

	/** Replies if the given values are near.
	 * 
	 * @param v1
	 * @param v2
	 * @param epsilon the approximation epsilon.
	 * @return <code>true</code> if the given <var>v1</var>
	 * is near <var>v2</var>, otherwise <code>false</code>.
	 */
	public static boolean isEpsilonEqual(double v1, double v2, double epsilon) {
		return Math.abs(v1 - v2) <= epsilon;
	}

	/** Compares its two arguments for order.
	 * Returns a negative integer,
     * zero, or a positive integer as the first argument is less than, equal
     * to, or greater than the second.<p>
	 * 
	 * @param v1
	 * @param v2
	 * @return a negative integer, zero, or a positive integer as the
     *         first argument is less than, equal to, or greater than the
     *         second.
	 */
	public static int compareEpsilon(double v1, double v2) {
		double v = v1 - v2;
		if (Math.abs(v) <= EPSILON) {
			return 0;
		}
		if (v <= 0.) {
			return (int) v - 1;
		}
		return (int) v + 1;
	}

	/** Compares its two arguments for order.
	 * Returns a negative integer,
     * zero, or a positive integer as the first argument is less than, equal
     * to, or greater than the second.<p>
	 * 
	 * @param v1
	 * @param v2
	 * @param epsilon approximation epsilon
	 * @return a negative integer, zero, or a positive integer as the
     *         first argument is less than, equal to, or greater than the
     *         second.
	 */
	public static int compareEpsilon(double v1, double v2, double epsilon) {
		double v = v1 - v2;
		if (Math.abs(v) <= epsilon) {
			return 0;
		}
		if (v <= 0.) {
			return (int) v - 1;
		}
		return (int) v + 1;
	}

	/** Replies the max value.
	 * 
	 * @param values are the values to scan.
	 * @return the max value.
	 */
	public static double max(double... values) {
		if (values==null || values.length==0) return Double.NaN;
		double m = values[0];
		for(double v : values) {
			if (v>m) m = v;
		}
		return m;
	}

	/** Replies the max value.
	 * 
	 * @param values are the values to scan.
	 * @return the max value.
	 */
	public static double max(float... values) {
		if (values==null || values.length==0) return Float.NaN;
		float m = values[0];
		for(float v : values) {
			if (v>m) m = v;
		}
		return m;
	}

	/** Replies the max value.
	 * 
	 * @param values are the values to scan.
	 * @return the max value.
	 */
	public static int max(int... values) {
		if (values==null || values.length==0) return 0;
		int m = values[0];
		for(int v : values) {
			if (v>m) m = v;
		}
		return m;
	}

	/** Replies the max value.
	 * 
	 * @param values are the values to scan.
	 * @return the max value.
	 */
	public static long max(long... values) {
		if (values==null || values.length==0) return 0;
		long m = values[0];
		for(long v : values) {
			if (v>m) m = v;
		}
		return m;
	}

	/** Replies the max value.
	 * 
	 * @param values are the values to scan.
	 * @return the max value.
	 */
	public static short max(short... values) {
		if (values==null || values.length==0) return 0;
		short m = values[0];
		for(short v : values) {
			if (v>m) m = v;
		}
		return m;
	}

	/** Replies the min value.
	 * 
	 * @param values are the values to scan.
	 * @return the min value.
	 */
	public static double min(double... values) {
		if (values==null || values.length==0) return Double.NaN;
		double m = values[0];
		for(double v : values) {
			if (v<m) m = v;
		}
		return m;
	}

	/** Replies the min value.
	 * 
	 * @param values are the values to scan.
	 * @return the min value.
	 */
	public static float min(float... values) {
		if (values==null || values.length==0) return Float.NaN;
		float m = values[0];
		for(float v : values) {
			if (v<m) m = v;
		}
		return m;
	}

	/** Replies the min value.
	 * 
	 * @param values are the values to scan.
	 * @return the min value.
	 */
	public static int min(int... values) {
		if (values==null || values.length==0) return 0;
		int m = values[0];
		for(int v : values) {
			if (v<m) m = v;
		}
		return m;
	}

	/** Replies the min value.
	 * 
	 * @param values are the values to scan.
	 * @return the min value.
	 */
	public static long min(long... values) {
		if (values==null || values.length==0) return 0;
		long m = values[0];
		for(long v : values) {
			if (v<m) m = v;
		}
		return m;
	}

	/** Replies the min value.
	 * 
	 * @param values are the values to scan.
	 * @return the min value.
	 */
	public static short min(short... values) {
		if (values==null || values.length==0) return 0;
		short m = values[0];
		for(short v : values) {
			if (v<m) m = v;
		}
		return m;
	}

	/** Clamp the given value to fit between the min and max values
	 * according to a cyclic heuristic.
	 * If the given value is not between the minimum and maximum
	 * values, the replied value
	 * is modulo the min-max range.
	 * 
	 * @param value
	 * @param min
	 * @param max
	 * @return the clamped value
	 */
	@Pure
	public static double clampCyclic(double value, double min, double max) {
		if (Double.isNaN(max)) { // NaN is lower than all the number according to double.compareTo()
			return Double.NaN;
		}
		if (Double.isNaN(min)) {
			// Clamp max only
			if (value>max) return max - Double.MAX_VALUE + value;
		}
		else {
			assert(min<=max);
			if (min==max) return min; // special case: empty interval
			if (value<min || value >max) {
				double perimeter = max - min;
				double nvalue = value - min;
				double rest = nvalue % perimeter;
				return (value<0) ? max+rest : rest+min;
			}
		}
		return value;
	}

	/** Replies the <var>value</var> clamped to
	 * the nearest bounds.
	 * If |<var>value</var>-<var>minBounds</var>| &gt;
	 * |<var>value</var>-<var>maxBounds</var>| then the
	 * returned value is <var>maxBounds</var>; otherwise
	 * it is <var>minBounds</var>.
	 *
	 * @param value is the value to clamp.
	 * @param minBounds is the minimal allowed value.
	 * @param maxBounds is the maximal allowed value.
	 * @return <var>minBounds</var> or <var>maxBounds</var>.
	 */
	public static double clampToNearestBounds( double value, double minBounds, double maxBounds ) {
		assert(minBounds<=maxBounds);
		double center = (minBounds+maxBounds) / 2f;
		if (value<=center) return minBounds;
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
	 */
	public static int getCohenSutherlandCode(int px, int py, int rxmin, int rymin, int rxmax, int rymax) {
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
	 */
	public static int getCohenSutherlandCode(double px, double py, double rxmin, double rymin, double rxmax, double rymax) {
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

	/** Determine the min and max values from a set of three values.
	 * <p>
	 * This function has an algorithm that is efficient for 3 values.
	 *
	 * @param a the first value.
	 * @param b the second value.
	 * @param c the third value.
	 * @param minmax the pair with the min and max values.
	 * @since 13.0
	 */
	public static void getMinMax(double a, double b, double c, Pair<Double, Double> minmax) {
		// Efficient implementation of the min/max determination
		double min, max;
		
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
		
		if (a <= b) {
			// case candidates: 123
			if (a <= c) {
				// case candidates: 12
				min = a;
				if (b <= c) {
					// case: 1
					max = c;
				} else {
					// case: 2
					max = b;
				}
			} else {
				// 3
				min = c;
				max = b;
			}
		} else {
			// case candidates: 456
			if (a <= c) {
				// case: 4
				min = b;
				max = c;
			} else {
				// case candidates: 56
				max = a;
				if (b <= c) {
					// case: 5
					min = b;
				} else {
					// case: 6
					min = c;
				}
			}
		}
		minmax.set(new Double(min), new Double(max));
	}

	/** Replies the cosecant of the specified angle.
	 * <p>
	 * <code>csc(a) = 1/sin(a)</code>
	 * <p>
	 * <img src="./doc-files/trigo1.png" alt="[Cosecant function]">
	 * <img src="./doc-files/trigo3.png" alt="[Cosecant function]">
	 * 
	 * @param angle
	 * @return the cosecant.
	 */
	public static double csc(double angle) {
		return 1./Math.sin(angle);
	}

	/** Replies the secant of the specified angle.
	 * <p>
	 * <code>csc(a) = 1/cos(a)</code>
	 * <p>
	 * <img src="./doc-files/trigo1.png" alt="[Secant function]">
	 * <img src="./doc-files/trigo2.png" alt="[Secant function]">
	 * 
	 * @param angle
	 * @return the secant.
	 */
	public static double sec(double angle) {
		return 1./Math.cos(angle);
	}

	/** Replies the cotangent of the specified angle.
	 * <p>
	 * <code>csc(a) = 1/tan(a)</code>
	 * <p>
	 * <img src="./doc-files/trigo1.png" alt="[Cotangent function]">
	 * <img src="./doc-files/trigo3.png" alt="[Cotangent function]">
	 * 
	 * @param angle
	 * @return the cotangent.
	 */
	public static double cot(double angle) {
		return 1./Math.tan(angle);
	}

	/** Replies the versine of the specified angle.
	 * <p>
	 * <code>versin(a) = 1 - cos(a)</code>
	 * <p>
	 * <img src="./doc-files/trigo1.png" alt="[Versine function]">
	 * 
	 * @param angle
	 * @return the cotangent.
	 */
	public static double versin(double angle) {
		return 1. - Math.cos(angle);
	}

	/** Replies the exsecant of the specified angle.
	 * <p>
	 * <code>exsec(a) = sec(a) - 1</code>
	 * <p>
	 * <img src="./doc-files/trigo1.png" alt="[Exsecant function]">
	 * 
	 * @param angle
	 * @return the cotangent.
	 */
	public static double exsec(double angle) {
		return sec(angle) - 1.;
	}

	/** Replies the chord of the specified angle.
	 * <p>
	 * <code>crd(a) = 2 sin(a/2)</code>
	 * <p>
	 * <img src="./doc-files/chord.png" alt="[Chord function]">
	 * 
	 * @param angle
	 * @return the chord.
	 */
	public static double crd(double angle) {
		return 2. * Math.sin(angle/2.);
	}

	/** Replies the half of the versine (aka, haversine) of the specified angle.
	 * <p>
	 * <code>haversine(a) = sin<sup>2</sup>(a/2) = (1-cos(a)) / 2</code>
	 * <p>
	 * <img src="./doc-files/trigo1.png" alt="[Excosecant function]">
	 * 
	 * @param angle
	 * @return the chord.
	 */
	public static double haversine(double angle) {
		double sin2 = Math.sin(angle/2.);
		return sin2*sin2;
	}

	@SuppressWarnings("unused")
	public static int epsilonDistanceSign(double distanceTo) {
		// TODO Auto-generated method stub
		return 0;
	}

	@SuppressWarnings("unused")
	public static boolean epsilonColinear(Vector3f n1, Vector3f n2) {
		// TODO Auto-generated method stub
		return false;
	}

	@SuppressWarnings("unused")
	public static boolean epsilonEqualsDistance(double nw, double nw2) {
		// TODO Auto-generated method stub
		return false;
	}

}