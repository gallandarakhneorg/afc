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

import org.arakhne.afc.math.geometry3d.continuous.Vector3f;

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
		return  Math.abs(v1 - v2) <= epsilon;
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
		return clampCyclic(radian, 0, 2f * PI);
	}

	/**
	 * Replies the specified angle translated between -PI and PI.
	 * 
	 * @param radian
	 *            is an angle
	 * @return normalized angle
	 */
	public static float clampRadianMinusPIToPI(float radian) {
		return clampCyclic(radian, -PI, PI);
	}

	/**
	 * Replies the specified angle translated between 0 and 360.
	 * 
	 * @param degree
	 *            is an angle
	 * @return normalized angle
	 */
	public static float clampDegree0To360(float degree) {
		return clampCyclic(degree, 0, 360);
	}

	/**
	 * Replies the specified angle translated between -180 and 180.
	 * 
	 * @param degree
	 *            is an angle
	 * @return normalized angle
	 */
	public static float clampDegreeMinus180To180(float degree) {
		return clampCyclic(degree, -180, 180);
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
	public static float clampCyclic(float value, float min, float max) {

		assert (!Float.isNaN(min));
		assert (!Float.isNaN(max));
		assert (min <= max);
		if ((!Float.isNaN(value)) && (!Float.isInfinite(value))) {		
			if (min == max)
				return min; // special case: empty interval
			if (value < min || value > max) {
				float perimeter = max - min;
				float nvalue = (value < min)? min - value : value - min;
				float rest = nvalue % perimeter;
				return (value < min) ? max - rest : rest + min;
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
	
	   /** Compute the right-handed cross-product of two vectors.
     * <p>
     * The right-handed cross product is described by the following figure:<br>
     * <a href="doc-files/right_handed_cross_product.png"><img src="doc-files/right_handed_cross_product.png" alt="[Right Handed Cross Product]" width="200" border="0"></a>
     * 
     * @param x1
     * @param y1
     * @param z1
     * @param x2
     * @param y2
     * @param z2
     * @return the cross-product.
     * @see #crossProductLeftHand(float, float, float, float, float, float)
     * @see #crossProductRightHand(float, float, float, float, float, float, Vector3f)
     */
    public static Vector3f crossProductRightHand(float x1, float y1, float z1, float x2, float y2, float z2) {
            float x = y1*z2 - z1*y2;
            float y = z1*x2 - x1*z2;
            float z = x1*y2 - y1*x2;
            return new Vector3f(x,y,z);
    }
    
    /** Compute the left-handed cross-product of two vectors.
     * <p>
     * The left-handed cross product is described by the following figure:<br>
     * <a href="doc-files/left_handed_cross_product.png"><img src="doc-files/left_handed_cross_product.png" alt="[Left Handed Cross Product]" width="200" border="0"></a>
     * 
     * @param x1
     * @param y1
     * @param z1
     * @param x2
     * @param y2
     * @param z2
     * @return the cross-product.
     * @see #crossProductLeftHand(float, float, float, float, float, float, Vector3f)
     * @see #crossProductRightHand(float, float, float, float, float, float)
     */
    public static Vector3f crossProductLeftHand(float x1, float y1, float z1, float x2, float y2, float z2) {
            float x = y2*z1 - z2*y1;
            float y = z2*x1 - x2*z1;
            float z = x2*y1 - y2*x1;
            return new Vector3f(x,y,z);
    }
    
    /**
     * Compute the eigenvectors of the given symmetric matrix
     * according to the Jacobi Cyclic Method.
     * 
     * @param matrix is the symmetric matrix.
     * @param eigenVectors are the matrix of vectors to fill. Eigen vectors are the
     * columns of the matrix.
     * @return the eigenvalues which are corresponding to the <var>eigenVectors</var> columns.
     * @see #eigenVectorsOfSymmetricMatrix(Matrix3, Matrix3) 
     */
    public static float[] eigenVectorsOfSymmetricMatrix(Matrix2f matrix, Matrix2f eigenVectors) {

            // Copy values up to the diagonal
            float m11 = matrix.getElement(0,0);
            float m12 = matrix.getElement(0,1);
            float m22 = matrix.getElement(1,1);
            
            eigenVectors.setIdentity();
            
            boolean sweepsConsumed = true;
            int i;
            float u, u2, u2p1, t, c, s;
            float ri0, ri1;
            
            for(int a=0; a<JACOBI_MAX_SWEEPS; ++a) {
                    
                    // Exit loop if off-diagonal entries are small enough
                    if ((Math.abs(m12) < MathConstants.JVM_MIN_FLOAT_EPSILON)) {
                            sweepsConsumed = false;
                            break;
                    }
                    
                    // Annihilate (1,2) entry
                    if (m12 != 0.) {
                            u = (m22 - m11) *.5f / m12;
                            u2 = u*u;
                            u2p1 = u2 + 1.f;
                            
                            if (u2p1!=u2)
                                    t = (float) (Math.signum(u) * (Math.sqrt(u2p1) - Math.abs(u)));
                            else
                                    t = .5f / u;
                            
                            c = (float) (1.f / Math.sqrt(t*t + 1));
                            s = c * t;
                            
                            m11 -= t * m12;
                            m22 += t * m12;
                            m12 = 0.f;
                            
                            for(i=0; i<2; ++i) {
                                    ri0 = eigenVectors.getElement(i,0);
                                    ri1 = eigenVectors.getElement(i,1);
                                    eigenVectors.setElement(i, 0, c * ri0 - s * ri1);
                                    eigenVectors.setElement(i, 1, s * ri0 + c * ri1);
                            }
                    }
            }
            
            assert(!sweepsConsumed) : "Sweep count consumed during eigenvector computation"; //$NON-NLS-1$
            
            // eigenvalues are on the diagonal
            return new float[] { m11, m22 };
    }
    

    /**
     * Compute the eigenvectors of the given symmetric matrix
     * according to the Jacobi Cyclic Method.
     * <p>
     * Given the n x n real symmetric matrix A, the routine 
     * Jacobi_Cyclic_Method calculates the eigenvalues and 
     * eigenvectors of A by successively sweeping through the 
     * matrix A annihilating off-diagonal non-zero elements 
     * by a rotation of the row and column in which the 
     * non-zero element occurs.  
     * <p>
     * The Jacobi procedure for finding the eigenvalues and eigenvectors of a
     * symmetric matrix A is based on finding a similarity transformation
     * which diagonalizes A.  The similarity transformation is given by a
     * product of a sequence of orthogonal (rotation) matrices each of which
     * annihilates an off-diagonal element and its transpose.  The rotation
     * effects only the rows and columns containing the off-diagonal element 
     * and its transpose, i.e. if a[i][j] is an off-diagonal element, then
     * the orthogonal transformation rotates rows a[i][] and a[j][], and
     * equivalently it rotates columns a[][i] and a[][j], so that a[i][j] = 0
     * and a[j][i] = 0.
     * The cyclic Jacobi method considers the off-diagonal elements in the
     * following order: (0,1),(0,2),...,(0,n-1),(1,2),...,(n-2,n-1).  If the
     * the magnitude of the off-diagonal element is greater than a treshold,
     * then a rotation is performed to annihilate that off-diagnonal element.
     * The process described above is called a sweep.  After a sweep has been
     * completed, the threshold is lowered and another sweep is performed
     * with the new threshold. This process is completed until the final
     * sweep is performed with the final threshold.
     * The orthogonal transformation which annihilates the matrix element
     * a[k][m], k != m, is Q = q[i][j], where q[i][j] = 0 if i != j, i,j != k
     * i,j != m and q[i][j] = 1 if i = j, i,j != k, i,j != m, q[k][k] =
     * q[m][m] = cos(phi), q[k][m] = -sin(phi), and q[m][k] = sin(phi), where
     * the angle phi is determined by requiring a[k][m] -> 0.  This condition
     * on the angle phi is equivalent to<br>
     * cot(2 phi) = 0.5 * (a[k][k] - a[m][m]) / a[k][m]<br>
     * Since tan(2 phi) = 2 tan(phi) / (1.0 - tan(phi)^2),<br>
     * tan(phi)^2 + 2cot(2 phi) * tan(phi) - 1 = 0.<br>
     * Solving for tan(phi), choosing the solution with smallest magnitude,
     * tan(phi) = - cot(2 phi) + sgn(cot(2 phi)) sqrt(cot(2phi)^2 + 1).
     * Then cos(phi)^2 = 1 / (1 + tan(phi)^2) and sin(phi)^2 = 1 - cos(phi)^2.
     * Finally by taking the sqrts and assigning the sign to the sin the same
     * as that of the tan, the orthogonal transformation Q is determined.
     * Let A" be the matrix obtained from the matrix A by applying the
     * similarity transformation Q, since Q is orthogonal, A" = Q'AQ, where Q'
     * is the transpose of Q (which is the same as the inverse of Q).  Then
     * a"[i][j] = Q'[i][p] a[p][q] Q[q][j] = Q[p][i] a[p][q] Q[q][j],
     * where repeated indices are summed over.
     * If i is not equal to either k or m, then Q[i][j] is the Kronecker
     * delta.   So if both i and j are not equal to either k or m,
     * a"[i][j] = a[i][j].
     * If i = k, j = k,<br>
     * a"[k][k] = a[k][k]*cos(phi)^2 + a[k][m]*sin(2 phi) + a[m][m]*sin(phi)^2<br>
     * If i = k, j = m,<br>
     * a"[k][m] = a"[m][k] = 0 = a[k][m]*cos(2 phi) + 0.5 * (a[m][m] - a[k][k])*sin(2 phi)<br>
     * If i = k, j != k or m,<br>
     * a"[k][j] = a"[j][k] = a[k][j] * cos(phi) + a[m][j] * sin(phi)<br>
     * If i = m, j = k, a"[m][k] = 0<br>
     * If i = m, j = m,<br>
     * a"[m][m] = a[m][m]*cos(phi)^2 - a[k][m]*sin(2 phi) + a[k][k]*sin(phi)^2<br>
     * If i= m, j != k or m,<br>
     * a"[m][j] = a"[j][m] = a[m][j] * cos(phi) - a[k][j] * sin(phi)
     * <p>
     * If X is the matrix of normalized eigenvectors stored so that the ith
     * column corresponds to the ith eigenvalue, then AX = X Lamda, where
     * Lambda is the diagonal matrix with the ith eigenvalue stored at
     * Lambda[i][i], i.e. X'AX = Lambda and X is orthogonal, the eigenvectors
     * are normalized and orthogonal.  So, X = Q1 Q2 ... Qs, where Qi is
     * the ith orthogonal matrix,  i.e. X can be recursively approximated by
     * the recursion relation X" = X Q, where Q is the orthogonal matrix and
     * the initial estimate for X is the identity matrix.<br>
     * If j = k, then x"[i][k] = x[i][k] * cos(phi) + x[i][m] * sin(phi),<br>
     * if j = m, then x"[i][m] = x[i][m] * cos(phi) - x[i][k] * sin(phi), and<br>
     * if j != k and j != m, then x"[i][j] = x[i][j].
     *  
     * @param matrix is the symmetric matrix.
     * @param eigenVectors are the matrix of vectors to fill. Eigen vectors are the
     * columns of the matrix.
     * @return the eigenvalues which are corresponding to the <var>eigenVectors</var> columns.
     * @see "Mathematics for 3D Game Programming and Computer Graphics, 2nd edition; pp.437." 
     */
    public static float[] eigenVectorsOfSymmetricMatrix(Matrix3f matrix, Matrix3f eigenVectors) {
            return eigenVectorsJacobi(new Matrix3f(matrix), new Matrix3f(eigenVectors));
    }


    private static float[] eigenVectorsJacobi(Matrix3f m, Matrix3f r) {

            // Copy values up to the diagonal
            float m11 = m.getElement(0,0);
            float m12 = m.getElement(0,1);
            float m13 = m.getElement(0,2);
            float m22 = m.getElement(1,1);
            float m23 = m.getElement(1,2);
            float m33 = m.getElement(2,2);
            
            r.setIdentity();
            
            boolean sweepsConsumed = true;
            int i;
            float u, u2, u2p1, t, c, s;
            float tmp, ri0, ri1, ri2;
            
            for(int a=0; a<JACOBI_MAX_SWEEPS; ++a) {
                    
                    // Exit loop if off-diagonal entries are small enough
                    if ((Math.abs(m12) < MathConstants.JVM_MIN_FLOAT_EPSILON)
                            && (Math.abs(m13) < MathConstants.JVM_MIN_FLOAT_EPSILON)
                            && (Math.abs(m23) < MathConstants.JVM_MIN_FLOAT_EPSILON)) {
                            sweepsConsumed = false;
                            break;
                    }
                    
                    // Annihilate (1,2) entry
                    if (m12 != 0.) {
                            u = (m22 - m11) *.5f / m12;
                            u2 = u*u;
                            u2p1 = u2 + 1.f;
                            
                            if (u2p1!=u2)
                                    t = (float) (Math.signum(u) * (Math.sqrt(u2p1) - Math.abs(u)));
                            else
                                    t = .5f / u;
                            
                            c = (float) (1.f / Math.sqrt(t*t + 1));
                            s = c * t;
                            
                            m11 -= t * m12;
                            m22 += t * m12;
                            m12 = 0.f;
                            
                            tmp = c * m13 - s * m23;
                            m23 = s * m13 + c * m23;
                            m13 = tmp;
                            
                            for(i=0; i<3; ++i) {
                                    ri0 = r.getElement(i,0);
                                    ri1 = r.getElement(i,1);
                                    r.setElement(i, 0, c * ri0 - s * ri1);
                                    r.setElement(i, 1, s * ri0 + c * ri1);
                            }
                    }
                    
                    // Annihilate (1,3) entry
                    if (m13 != 0.) {
                            u = (m33 - m11) *.5f / m13;
                            u2 = u*u;
                            u2p1 = u2 + 1.f;
                            
                            if (u2p1!=u2)
                                    t = (float) (Math.signum(u) * (Math.sqrt(u2p1) - Math.abs(u)));
                            else
                                    t = .5f / u;
                            
                            c = (float) (1.f / Math.sqrt(t*t + 1));
                            s = c * t;
                            
                            m11 -= t * m13;
                            m33 += t * m13;
                            m13 = 0.f;
                            
                            tmp = c * m12 - s * m23;
                            m23 = s * m12 + c * m23;
                            m12 = tmp;
                            
                            for(i=0; i<3; ++i) {
                                    ri0 = r.getElement(i,0);
                                    ri2 = r.getElement(i,2);
                                    r.setElement(i, 0, c * ri0 - s * ri2);
                                    r.setElement(i, 2, s * ri0 + c * ri2);
                            }
                    }

                    // Annihilate (2,3) entry
                    if (m23 != 0.) {
                            u = (m33 - m22) *.5f / m23;
                            u2 = u*u;
                            u2p1 = u2 + 1.f;
                            
                            if (u2p1!=u2)
                                    t = (float) (Math.signum(u) * (Math.sqrt(u2p1) - Math.abs(u)));
                            else
                                    t = .5f / u;
                            
                            c = (float) (1.f / Math.sqrt(t*t + 1));
                            s = c * t;
                            
                            m22 -= t * m23;
                            m33 += t * m23;
                            m23 = 0.f;
                            
                            tmp = c * m12 - s * m13;
                            m13 = s * m12 + c * m13;
                            m12 = tmp;
                            
                            for(i=0; i<3; ++i) {
                                    ri1 = r.getElement(i,1);
                                    ri2 = r.getElement(i,2);
                                    r.setElement(i, 1, c * ri1 - s * ri2);
                                    r.setElement(i, 2, s * ri1 + c * ri2);
                            }
                    }
            }
            
            assert(!sweepsConsumed) : "Sweep count consumed during eigenvector computation"; //$NON-NLS-1$
            
            // eigenvalues are on the diagonal
            return new float[] { m11, m22, m33 };
    }    
}