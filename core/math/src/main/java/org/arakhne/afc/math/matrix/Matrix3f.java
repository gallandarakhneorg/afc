/* 
 * $Id$
 * 
 * Copyright (c) 2006-10, Multiagent Team, Laboratoire Systemes et Transports, Universite de Technologie de Belfort-Montbeliard.
 * Copyright (C) 2012 Stephane GALLAND.
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
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
package org.arakhne.afc.math.matrix;

import java.io.Serializable;
import java.util.Arrays;

import org.arakhne.afc.math.MathConstants;
import org.arakhne.afc.math.MathUtil;
import org.arakhne.afc.math.continous.object3d.Point3f;
import org.arakhne.afc.math.continous.object3d.Tuple3f;
import org.arakhne.afc.math.continous.object3d.Vector3f;
import org.arakhne.afc.vmutil.locale.Locale;

/**
 * Is represented internally as a 3x3 floating point matrix. The mathematical
 * representation is row major, as in traditional matrix mathematics.
 * 
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class Matrix3f implements Serializable, Cloneable, MathConstants {

	private static final long serialVersionUID = -7386754038391115819L;

	/**
	 * The first matrix element in the first row.
	 */
	public float m00;

	/**
	 * The second matrix element in the first row.
	 */
	public float m01;

	/**
	 * The third matrix element in the first row.
	 */
	public float m02;

	/**
	 * The first matrix element in the second row.
	 */
	public float m10;

	/**
	 * The second matrix element in the second row.
	 */
	public float m11;

	/**
	 * The third matrix element in the second row.
	 */
	public float m12;

	/**
	 * The first matrix element in the third row.
	 */
	public float m20;

	/**
	 * The second matrix element in the third row.
	 */
	public float m21;

	/**
	 * The third matrix element in the third row.
	 */
	public float m22;

	/**
	 * Constructs and initializes a Matrix3f from the specified nine values.
	 * 
	 * @param m00
	 *            the [0][0] element
	 * @param m01
	 *            the [0][1] element
	 * @param m02
	 *            the [0][2] element
	 * @param m10
	 *            the [1][0] element
	 * @param m11
	 *            the [1][1] element
	 * @param m12
	 *            the [1][2] element
	 * @param m20
	 *            the [2][0] element
	 * @param m21
	 *            the [2][1] element
	 * @param m22
	 *            the [2][2] element
	 */
	public Matrix3f(float m00, float m01, float m02, float m10, float m11, float m12, float m20, float m21, float m22) {
		this.m00 = m00;
		this.m01 = m01;
		this.m02 = m02;

		this.m10 = m10;
		this.m11 = m11;
		this.m12 = m12;

		this.m20 = m20;
		this.m21 = m21;
		this.m22 = m22;
	}

	/**
	 * Constructs and initializes a Matrix3f from the specified nine- element
	 * array.
	 * 
	 * @param v
	 *            the array of length 9 containing in order
	 */
	public Matrix3f(float[] v) {
		this.m00 = v[0];
		this.m01 = v[1];
		this.m02 = v[2];

		this.m10 = v[3];
		this.m11 = v[4];
		this.m12 = v[5];

		this.m20 = v[6];
		this.m21 = v[7];
		this.m22 = v[8];
	}

	/**
	 * Constructs a new matrix with the same values as the Matrix3f parameter.
	 * 
	 * @param m1
	 *            the source matrix
	 */
	public Matrix3f(Matrix3f m1) {
		this.m00 = m1.m00;
		this.m01 = m1.m01;
		this.m02 = m1.m02;

		this.m10 = m1.m10;
		this.m11 = m1.m11;
		this.m12 = m1.m12;

		this.m20 = m1.m20;
		this.m21 = m1.m21;
		this.m22 = m1.m22;
	}

	/**
	 * Constructs and initializes a Matrix3f to all zeros.
	 */
	public Matrix3f() {
		this.m00 = 0f;
		this.m01 = 0f;
		this.m02 = 0f;

		this.m10 = 0f;
		this.m11 = 0f;
		this.m12 = 0f;

		this.m20 = 0f;
		this.m21 = 0f;
		this.m22 = 0f;
	}

	/**
	 * Returns a string that contains the values of this Matrix3f.
	 * 
	 * @return the String representation
	 */
	@Override
	public String toString() {
		return this.m00 + ", " //$NON-NLS-1$
				+ this.m01 + ", "  //$NON-NLS-1$
				+ this.m02 + "\n"  //$NON-NLS-1$
				+ this.m10 + ", "  //$NON-NLS-1$
				+ this.m11 + ", "  //$NON-NLS-1$
				+ this.m12 + "\n"  //$NON-NLS-1$
				+ this.m20 + ", " //$NON-NLS-1$
				+ this.m21 + ", "  //$NON-NLS-1$
				+ this.m22 + "\n"; //$NON-NLS-1$
	}

	/**
	 * Sets this Matrix3f to identity.
	 */
	public final void setIdentity() {
		this.m00 = 1f;
		this.m01 = 0f;
		this.m02 = 0f;

		this.m10 = 0f;
		this.m11 = 1f;
		this.m12 = 0f;

		this.m20 = 0f;
		this.m21 = 0f;
		this.m22 = 1f;
	}

	/**
	 * Sets the specified element of this matrix3f to the value provided.
	 * 
	 * @param row
	 *            the row number to be modified (zero indexed)
	 * @param column
	 *            the column number to be modified (zero indexed)
	 * @param value
	 *            the new value
	 */
	public final void setElement(int row, int column, float value) {
		switch (row) {
		case 0:
			switch (column) {
			case 0:
				this.m00 = value;
				break;
			case 1:
				this.m01 = value;
				break;
			case 2:
				this.m02 = value;
				break;
			default:
				throw new ArrayIndexOutOfBoundsException();
			}
			break;

		case 1:
			switch (column) {
			case 0:
				this.m10 = value;
				break;
			case 1:
				this.m11 = value;
				break;
			case 2:
				this.m12 = value;
				break;
			default:
				throw new ArrayIndexOutOfBoundsException();
			}
			break;

		case 2:
			switch (column) {
			case 0:
				this.m20 = value;
				break;
			case 1:
				this.m21 = value;
				break;
			case 2:
				this.m22 = value;
				break;
			default:
				throw new ArrayIndexOutOfBoundsException();
			}
			break;

		default:
			throw new ArrayIndexOutOfBoundsException();
		}
	}

	/**
	 * Retrieves the value at the specified row and column of the specified
	 * matrix.
	 * 
	 * @param row
	 *            the row number to be retrieved (zero indexed)
	 * @param column
	 *            the column number to be retrieved (zero indexed)
	 * @return the value at the indexed element.
	 */
	public final float getElement(int row, int column) {
		switch (row) {
		case 0:
			switch (column) {
			case 0:
				return (this.m00);
			case 1:
				return (this.m01);
			case 2:
				return (this.m02);
			default:
				break;
			}
			break;
		case 1:
			switch (column) {
			case 0:
				return (this.m10);
			case 1:
				return (this.m11);
			case 2:
				return (this.m12);
			default:
				break;
			}
			break;

		case 2:
			switch (column) {
			case 0:
				return (this.m20);
			case 1:
				return (this.m21);
			case 2:
				return (this.m22);
			default:
				break;
			}
			break;

		default:
			break;
		}

		throw new ArrayIndexOutOfBoundsException();
	}

	/**
	 * Copies the matrix values in the specified row into the vector parameter.
	 * 
	 * @param row
	 *            the matrix row
	 * @param v
	 *            the vector into which the matrix row values will be copied
	 */
	public final void getRow(int row, Vector3f v) {
		if (row == 0) {
			v.set(this.m00, this.m01, this.m02);
		} else if (row == 1) {
			v.set(this.m10, this.m11, this.m12);
		} else if (row == 2) {
			v.set(this.m20, this.m21, this.m22);
		} else {
			throw new ArrayIndexOutOfBoundsException();
		}

	}

	/**
	 * Copies the matrix values in the specified row into the array parameter.
	 * 
	 * @param row
	 *            the matrix row
	 * @param v
	 *            the array into which the matrix row values will be copied
	 */
	public final void getRow(int row, float v[]) {
		if (row == 0) {
			v[0] = this.m00;
			v[1] = this.m01;
			v[2] = this.m02;
		} else if (row == 1) {
			v[0] = this.m10;
			v[1] = this.m11;
			v[2] = this.m12;
		} else if (row == 2) {
			v[0] = this.m20;
			v[1] = this.m21;
			v[2] = this.m22;
		} else {
			throw new ArrayIndexOutOfBoundsException();
		}

	}

	/**
	 * Copies the matrix values in the specified column into the vector
	 * parameter.
	 * 
	 * @param column
	 *            the matrix column
	 * @param v
	 *            the vector into which the matrix row values will be copied
	 */
	public final void getColumn(int column, Vector3f v) {
		if (column == 0) {
			v.set(this.m00, this.m10, this.m20);
		} else if (column == 1) {
			v.set(this.m01, this.m11, this.m21);
		} else if (column == 2) {
			v.set(this.m02, this.m12, this.m22);
		} else {
			throw new ArrayIndexOutOfBoundsException();
		}

	}

	/**
	 * Copies the matrix values in the specified column into the array
	 * parameter.
	 * 
	 * @param column
	 *            the matrix column
	 * @param v
	 *            the array into which the matrix row values will be copied
	 */
	public final void getColumn(int column, float v[]) {
		if (column == 0) {
			v[0] = this.m00;
			v[1] = this.m10;
			v[2] = this.m20;
		} else if (column == 1) {
			v[0] = this.m01;
			v[1] = this.m11;
			v[2] = this.m21;
		} else if (column == 2) {
			v[0] = this.m02;
			v[1] = this.m12;
			v[2] = this.m22;
		} else {
			throw new ArrayIndexOutOfBoundsException();
		}

	}

	/**
	 * Sets the specified row of this Matrix3f to the 4 values provided.
	 * 
	 * @param row
	 *            the row number to be modified (zero indexed)
	 * @param x
	 *            the first column element
	 * @param y
	 *            the second column element
	 * @param z
	 *            the third column element
	 */
	public final void setRow(int row, float x, float y, float z) {
		switch (row) {
		case 0:
			this.m00 = x;
			this.m01 = y;
			this.m02 = z;
			break;

		case 1:
			this.m10 = x;
			this.m11 = y;
			this.m12 = z;
			break;

		case 2:
			this.m20 = x;
			this.m21 = y;
			this.m22 = z;
			break;

		default:
			throw new ArrayIndexOutOfBoundsException();
		}
	}

	/**
	 * Sets the specified row of this Matrix3f to the Vector provided.
	 * 
	 * @param row
	 *            the row number to be modified (zero indexed)
	 * @param v
	 *            the replacement row
	 */
	public final void setRow(int row, Vector3f v) {
		switch (row) {
		case 0:
			this.m00 = v.getX();
			this.m01 = v.getY();
			this.m02 = v.getZ();
			break;

		case 1:
			this.m10 = v.getX();
			this.m11 = v.getY();
			this.m12 = v.getZ();
			break;

		case 2:
			this.m20 = v.getX();
			this.m21 = v.getY();
			this.m22 = v.getZ();
			break;

		default:
			throw new ArrayIndexOutOfBoundsException();
		}
	}

	/**
	 * Sets the specified row of this Matrix3f to the three values provided.
	 * 
	 * @param row
	 *            the row number to be modified (zero indexed)
	 * @param v
	 *            the replacement row
	 */
	public final void setRow(int row, float v[]) {
		switch (row) {
		case 0:
			this.m00 = v[0];
			this.m01 = v[1];
			this.m02 = v[2];
			break;

		case 1:
			this.m10 = v[0];
			this.m11 = v[1];
			this.m12 = v[2];
			break;

		case 2:
			this.m20 = v[0];
			this.m21 = v[1];
			this.m22 = v[2];
			break;

		default:
			throw new ArrayIndexOutOfBoundsException();
		}
	}

	/**
	 * Sets the specified column of this Matrix3f to the three values provided.
	 * 
	 * @param column
	 *            the column number to be modified (zero indexed)
	 * @param x
	 *            the first row element
	 * @param y
	 *            the second row element
	 * @param z
	 *            the third row element
	 */
	public final void setColumn(int column, float x, float y, float z) {
		switch (column) {
		case 0:
			this.m00 = x;
			this.m10 = y;
			this.m20 = z;
			break;

		case 1:
			this.m01 = x;
			this.m11 = y;
			this.m21 = z;
			break;

		case 2:
			this.m02 = x;
			this.m12 = y;
			this.m22 = z;
			break;

		default:
			throw new ArrayIndexOutOfBoundsException();
		}
	}

	/**
	 * Sets the specified column of this Matrix3f to the vector provided.
	 * 
	 * @param column
	 *            the column number to be modified (zero indexed)
	 * @param v
	 *            the replacement column
	 */
	public final void setColumn(int column, Vector3f v) {
		switch (column) {
		case 0:
			this.m00 = v.getX();
			this.m10 = v.getY();
			this.m20 = v.getZ();
			break;

		case 1:
			this.m01 = v.getX();
			this.m11 = v.getY();
			this.m21 = v.getZ();
			break;

		case 2:
			this.m02 = v.getX();
			this.m12 = v.getY();
			this.m22 = v.getZ();
			break;

		default:
			throw new ArrayIndexOutOfBoundsException();
		}
	}

	/**
	 * Sets the specified column of this Matrix3f to the three values provided.
	 * 
	 * @param column
	 *            the column number to be modified (zero indexed)
	 * @param v
	 *            the replacement column
	 */
	public final void setColumn(int column, float v[]) {
		switch (column) {
		case 0:
			this.m00 = v[0];
			this.m10 = v[1];
			this.m20 = v[2];
			break;

		case 1:
			this.m01 = v[0];
			this.m11 = v[1];
			this.m21 = v[2];
			break;

		case 2:
			this.m02 = v[0];
			this.m12 = v[1];
			this.m22 = v[2];
			break;

		default:
			throw new ArrayIndexOutOfBoundsException();
		}
	}

	/**
	 * Adds a scalar to each component of this matrix.
	 * 
	 * @param scalar
	 *            the scalar adder
	 */
	public final void add(float scalar) {
		this.m00 += scalar;
		this.m01 += scalar;
		this.m02 += scalar;

		this.m10 += scalar;
		this.m11 += scalar;
		this.m12 += scalar;

		this.m20 += scalar;
		this.m21 += scalar;
		this.m22 += scalar;

	}

	/**
	 * Adds a scalar to each component of the matrix m1 and places the result
	 * into this. Matrix m1 is not modified.
	 * 
	 * @param scalar
	 *            the scalar adder
	 * @param m1
	 *            the original matrix values
	 */
	public final void add(float scalar, Matrix3f m1) {
		this.m00 = m1.m00 + scalar;
		this.m01 = m1.m01 + scalar;
		this.m02 = m1.m02 + scalar;

		this.m10 = m1.m10 + scalar;
		this.m11 = m1.m11 + scalar;
		this.m12 = m1.m12 + scalar;

		this.m20 = m1.m20 + scalar;
		this.m21 = m1.m21 + scalar;
		this.m22 = m1.m22 + scalar;
	}

	/**
	 * Sets the value of this matrix to the matrix sum of matrices m1 and m2.
	 * 
	 * @param m1
	 *            the first matrix
	 * @param m2
	 *            the second matrix
	 */
	public final void add(Matrix3f m1, Matrix3f m2) {
		this.m00 = m1.m00 + m2.m00;
		this.m01 = m1.m01 + m2.m01;
		this.m02 = m1.m02 + m2.m02;

		this.m10 = m1.m10 + m2.m10;
		this.m11 = m1.m11 + m2.m11;
		this.m12 = m1.m12 + m2.m12;

		this.m20 = m1.m20 + m2.m20;
		this.m21 = m1.m21 + m2.m21;
		this.m22 = m1.m22 + m2.m22;
	}

	/**
	 * Sets the value of this matrix to the sum of itself and matrix m1.
	 * 
	 * @param m1
	 *            the other matrix
	 */
	public final void add(Matrix3f m1) {
		this.m00 += m1.m00;
		this.m01 += m1.m01;
		this.m02 += m1.m02;

		this.m10 += m1.m10;
		this.m11 += m1.m11;
		this.m12 += m1.m12;

		this.m20 += m1.m20;
		this.m21 += m1.m21;
		this.m22 += m1.m22;
	}

	/**
	 * Sets the value of this matrix to the matrix difference of matrices m1 and
	 * m2.
	 * 
	 * @param m1
	 *            the first matrix
	 * @param m2
	 *            the second matrix
	 */
	public final void sub(Matrix3f m1, Matrix3f m2) {
		this.m00 = m1.m00 - m2.m00;
		this.m01 = m1.m01 - m2.m01;
		this.m02 = m1.m02 - m2.m02;

		this.m10 = m1.m10 - m2.m10;
		this.m11 = m1.m11 - m2.m11;
		this.m12 = m1.m12 - m2.m12;

		this.m20 = m1.m20 - m2.m20;
		this.m21 = m1.m21 - m2.m21;
		this.m22 = m1.m22 - m2.m22;
	}

	/**
	 * Sets the value of this matrix to the matrix difference of itself and
	 * matrix m1 (this = this - m1).
	 * 
	 * @param m1
	 *            the other matrix
	 */
	public final void sub(Matrix3f m1) {
		this.m00 -= m1.m00;
		this.m01 -= m1.m01;
		this.m02 -= m1.m02;

		this.m10 -= m1.m10;
		this.m11 -= m1.m11;
		this.m12 -= m1.m12;

		this.m20 -= m1.m20;
		this.m21 -= m1.m21;
		this.m22 -= m1.m22;
	}

	/**
	 * Sets the value of this matrix to its transpose.
	 */
	public final void transpose() {
		float temp;

		temp = this.m10;
		this.m10 = this.m01;
		this.m01 = temp;

		temp = this.m20;
		this.m20 = this.m02;
		this.m02 = temp;

		temp = this.m21;
		this.m21 = this.m12;
		this.m12 = temp;
	}

	/**
	 * Sets the value of this matrix to the transpose of the argument matrix.
	 * 
	 * @param m1
	 *            the matrix to be transposed
	 */
	public final void transpose(Matrix3f m1) {
		if (this != m1) {
			this.m00 = m1.m00;
			this.m01 = m1.m10;
			this.m02 = m1.m20;

			this.m10 = m1.m01;
			this.m11 = m1.m11;
			this.m12 = m1.m21;

			this.m20 = m1.m02;
			this.m21 = m1.m12;
			this.m22 = m1.m22;
		} else
			this.transpose();
	}

	/**
	 * Sets the value of this matrix to the float value of the Matrix3f
	 * argument.
	 * 
	 * @param m1
	 *            the Matrix3f to be converted to float
	 */
	public final void set(Matrix3f m1) {
		this.m00 = m1.m00;
		this.m01 = m1.m01;
		this.m02 = m1.m02;

		this.m10 = m1.m10;
		this.m11 = m1.m11;
		this.m12 = m1.m12;

		this.m20 = m1.m20;
		this.m21 = m1.m21;
		this.m22 = m1.m22;
	}

	/**
	 * Sets the values in this Matrix3f equal to the row-major array parameter
	 * (ie, the first three elements of the array will be copied into the first
	 * row of this matrix, etc.).
	 * 
	 * @param m
	 *            the float precision array of length 9
	 */
	public final void set(float[] m) {
		this.m00 = m[0];
		this.m01 = m[1];
		this.m02 = m[2];

		this.m10 = m[3];
		this.m11 = m[4];
		this.m12 = m[5];

		this.m20 = m[6];
		this.m21 = m[7];
		this.m22 = m[8];

	}
	
	/**
	 * Set the components of the matrix.
	 * 
	 * @param m00
	 *            the [0][0] element
	 * @param m01
	 *            the [0][1] element
	 * @param m02
	 *            the [0][2] element
	 * @param m10
	 *            the [1][0] element
	 * @param m11
	 *            the [1][1] element
	 * @param m12
	 *            the [1][2] element
	 * @param m20
	 *            the [2][0] element
	 * @param m21
	 *            the [2][1] element
	 * @param m22
	 *            the [2][2] element
	 */
	public void set(float m00, float m01, float m02, float m10, float m11, float m12, float m20, float m21, float m22) {
		this.m00 = m00;
		this.m01 = m01;
		this.m02 = m02;

		this.m10 = m10;
		this.m11 = m11;
		this.m12 = m12;

		this.m20 = m20;
		this.m21 = m21;
		this.m22 = m22;
	}

	/**
	 * Sets the value of this matrix to the matrix inverse of the passed matrix
	 * m1.
	 * 
	 * @param m1
	 *            the matrix to be inverted
	 */
	public void invert(Matrix3f m1) {
		invertGeneral(m1);
	}

	/**
	 * Inverts this matrix in place.
	 */
	public void invert() {
		invertGeneral(this);
	}

	/**
	 * General invert routine. Inverts m1 and places the result in "this". Note
	 * that this routine handles both the "this" version and the non-"this"
	 * version.
	 * 
	 * Also note that since this routine is slow anyway, we won't worry about
	 * allocating a little bit of garbage.
	 */
	private final void invertGeneral(Matrix3f m1) {
		float result[] = new float[9];
		int row_perm[] = new int[3];
		int i;
		float[] tmp = new float[9]; // scratch matrix

		// Use LU decomposition and backsubstitution code specifically
		// for floating-point 3x3 matrices.

		// Copy source matrix to t1tmp
		tmp[0] = m1.m00;
		tmp[1] = m1.m01;
		tmp[2] = m1.m02;

		tmp[3] = m1.m10;
		tmp[4] = m1.m11;
		tmp[5] = m1.m12;

		tmp[6] = m1.m20;
		tmp[7] = m1.m21;
		tmp[8] = m1.m22;

		// Calculate LU decomposition: Is the matrix singular?
		if (!luDecomposition(tmp, row_perm)) {
			throw new SingularMatrixException(Locale.getString("NOT_INVERTABLE_MATRIX")); //$NON-NLS-1$
		}

		// Perform back substitution on the identity matrix
		for (i = 0; i < 9; ++i)
			result[i] = 0f;
		result[0] = 1f;
		result[4] = 1f;
		result[8] = 1f;
		luBacksubstitution(tmp, row_perm, result);

		this.m00 = result[0];
		this.m01 = result[1];
		this.m02 = result[2];

		this.m10 = result[3];
		this.m11 = result[4];
		this.m12 = result[5];

		this.m20 = result[6];
		this.m21 = result[7];
		this.m22 = result[8];

	}

	/**
	 * Given a 3x3 array "matrix0", this function replaces it with the LU
	 * decomposition of a row-wise permutation of itself. The input parameters
	 * are "matrix0" and "dimen". The array "matrix0" is also an output
	 * parameter. The vector "row_perm[3]" is an output parameter that contains
	 * the row permutations resulting from partial pivoting. The output
	 * parameter "even_row_xchg" is 1 when the number of row exchanges is even,
	 * or -1 otherwise. Assumes data type is always float.
	 * 
	 * This function is similar to luDecomposition, except that it is tuned
	 * specifically for 3x3 matrices.
	 * 
	 * @return true if the matrix is nonsingular, or false otherwise.
	 */
	//
	// Reference: Press, Flannery, Teukolsky, Vetterling,
	// _Numerical_Recipes_in_C_, Cambridge University Press,
	// 1988, pp 40-45.
	//
	private static boolean luDecomposition(float[] matrix0, int[] row_perm) {

		float row_scale[] = new float[3];

		// Determine implicit scaling information by looping over rows
		{
			int i, j;
			int ptr, rs;
			float big, temp;

			ptr = 0;
			rs = 0;

			// For each row ...
			i = 3;
			while (i-- != 0) {
				big = 0f;

				// For each column, find the largest element in the row
				j = 3;
				while (j-- != 0) {
					temp = matrix0[ptr++];
					temp = Math.abs(temp);
					if (temp > big) {
						big = temp;
					}
				}

				// Is the matrix singular?
				if (big == 0f) {
					return false;
				}
				row_scale[rs++] = 1f / big;
			}
		}

		{
			int j;
			int mtx;

			mtx = 0;

			// For all columns, execute Crout's method
			for (j = 0; j < 3; ++j) {
				int i, imax, k;
				int target, p1, p2;
				float sum, big, temp;

				// Determine elements of upper diagonal matrix U
				for (i = 0; i < j; ++i) {
					target = mtx + (3 * i) + j;
					sum = matrix0[target];
					k = i;
					p1 = mtx + (3 * i);
					p2 = mtx + j;
					while (k-- != 0) {
						sum -= matrix0[p1] * matrix0[p2];
						++p1;
						p2 += 3;
					}
					matrix0[target] = sum;
				}

				// Search for largest pivot element and calculate
				// intermediate elements of lower diagonal matrix L.
				big = 0f;
				imax = -1;
				for (i = j; i < 3; ++i) {
					target = mtx + (3 * i) + j;
					sum = matrix0[target];
					k = j;
					p1 = mtx + (3 * i);
					p2 = mtx + j;
					while (k-- != 0) {
						sum -= matrix0[p1] * matrix0[p2];
						++p1;
						p2 += 3;
					}
					matrix0[target] = sum;

					// Is this the best pivot so far?
					if ((temp = row_scale[i] * Math.abs(sum)) >= big) {
						big = temp;
						imax = i;
					}
				}

				if (imax < 0) {
					throw new RuntimeException();
				}

				// Is a row exchange necessary?
				if (j != imax) {
					// Yes: exchange rows
					k = 3;
					p1 = mtx + (3 * imax);
					p2 = mtx + (3 * j);
					while (k-- != 0) {
						temp = matrix0[p1];
						matrix0[p1++] = matrix0[p2];
						matrix0[p2++] = temp;
					}

					// Record change in scale factor
					row_scale[imax] = row_scale[j];
				}

				// Record row permutation
				row_perm[j] = imax;

				// Is the matrix singular
				if (matrix0[(mtx + (3 * j) + j)] == 0f) {
					return false;
				}

				// Divide elements of lower diagonal matrix L by pivot
				if (j != (3 - 1)) {
					temp = 1f / (matrix0[(mtx + (3 * j) + j)]);
					target = mtx + (3 * (j + 1)) + j;
					i = 2 - j;
					while (i-- != 0) {
						matrix0[target] *= temp;
						target += 3;
					}
				}
			}
		}

		return true;
	}

	/**
	 * Solves a set of linear equations. The input parameters "matrix1", and
	 * "row_perm" come from luDecompostionD3x3 and do not change here. The
	 * parameter "matrix2" is a set of column vectors assembled into a 3x3
	 * matrix of floating-point values. The procedure takes each column of
	 * "matrix2" in turn and treats it as the right-hand side of the matrix
	 * equation Ax = LUx = b. The solution vector replaces the original column
	 * of the matrix.
	 * 
	 * If "matrix2" is the identity matrix, the procedure replaces its contents
	 * with the inverse of the matrix from which "matrix1" was originally
	 * derived.
	 */
	//
	// Reference: Press, Flannery, Teukolsky, Vetterling,
	// _Numerical_Recipes_in_C_, Cambridge University Press,
	// 1988, pp 44-45.
	//
	private static void luBacksubstitution(float[] matrix1, int[] row_perm,
			float[] matrix2) {

		int i, ii, ip, j, k;
		int rp;
		int cv, rv;

		// rp = row_perm;
		rp = 0;

		// For each column vector of matrix2 ...
		for (k = 0; k < 3; ++k) {
			// cv = &(matrix2[0][k]);
			cv = k;
			ii = -1;

			// Forward substitution
			for (i = 0; i < 3; ++i) {
				float sum;

				ip = row_perm[rp + i];
				sum = matrix2[cv + 3 * ip];
				matrix2[cv + 3 * ip] = matrix2[cv + 3 * i];
				if (ii >= 0) {
					// rv = &(matrix1[i][0]);
					rv = i * 3;
					for (j = ii; j <= i - 1; ++j) {
						sum -= matrix1[rv + j] * matrix2[cv + 3 * j];
					}
				} else if (sum != 0f) {
					ii = i;
				}
				matrix2[cv + 3 * i] = sum;
			}

			// Backsubstitution
			// rv = &(matrix1[3][0]);
			rv = 2 * 3;
			matrix2[cv + 3 * 2] /= matrix1[rv + 2];

			rv -= 3;
			matrix2[cv + 3 * 1] = (matrix2[cv + 3 * 1] - matrix1[rv + 2]
					* matrix2[cv + 3 * 2])
					/ matrix1[rv + 1];

			rv -= 3;
			matrix2[cv + 4 * 0] = (matrix2[cv + 3 * 0] - matrix1[rv + 1]
					* matrix2[cv + 3 * 1] - matrix1[rv + 2]
							* matrix2[cv + 3 * 2])
							/ matrix1[rv + 0];

		}
	}

	/**
	 * Computes the determinant of this matrix.
	 * 
	 * @return the determinant of the matrix
	 */
	public final float determinant() {
		/* det(A,B,C) = det( [ x1 x2 x3 ]
		 *                   [ y1 y2 y3 ]
		 *                   [ z1 z2 z3 ] )
		 */
		return
				this.m00 * (this.m11 * this.m22 - this.m21 * this.m12)
				+ this.m10 * (this.m21 * this.m02 - this.m01 * this.m22)
				+ this.m20 * (this.m01 * this.m12 - this.m11 * this.m02);
	}

	/**
	 * Multiplies each element of this matrix by a scalar.
	 * 
	 * @param scalar
	 *            The scalar multiplier.
	 */
	public final void mul(float scalar) {
		this.m00 *= scalar;
		this.m01 *= scalar;
		this.m02 *= scalar;

		this.m10 *= scalar;
		this.m11 *= scalar;
		this.m12 *= scalar;

		this.m20 *= scalar;
		this.m21 *= scalar;
		this.m22 *= scalar;
	}

	/** Multiply this matrix by the given vector.
	 * 
	 * @param v
	 * @return the vector resulting of <code>this * v</code>.
	 */
	public Vector3f mul(Vector3f v) {
		return new Vector3f(
				this.m00 * v.getX() + this.m01 * v.getY() + this.m02 * v.getZ(),
				this.m10 * v.getX() + this.m11 * v.getY() + this.m12 * v.getZ(),
				this.m20 * v.getX() + this.m21 * v.getY() + this.m22 * v.getZ());
	}

	/** Multiply the transposing of this matrix by the given vector.
	 * 
	 * @param v
	 * @return the vector resulting of <code>transpose(this) * v</code>.
	 */
	public Vector3f mulTransposeLeft(Vector3f v) {
		return new Vector3f(
				this.m00 * v.getX() + this.m10 * v.getY() + this.m20 * v.getZ(),
				this.m01 * v.getX() + this.m11 * v.getY() + this.m21 * v.getZ(),
				this.m02 * v.getX() + this.m12 * v.getY() + this.m22 * v.getZ());
	}

	/**
	 * Multiplies each element of matrix m1 by a scalar and places the result
	 * into this. Matrix m1 is not modified.
	 * 
	 * @param scalar
	 *            the scalar multiplier
	 * @param m1
	 *            the original matrix
	 */
	public final void mul(float scalar, Matrix3f m1) {
		this.m00 = scalar * m1.m00;
		this.m01 = scalar * m1.m01;
		this.m02 = scalar * m1.m02;

		this.m10 = scalar * m1.m10;
		this.m11 = scalar * m1.m11;
		this.m12 = scalar * m1.m12;

		this.m20 = scalar * m1.m20;
		this.m21 = scalar * m1.m21;
		this.m22 = scalar * m1.m22;
	}

	/**
	 * Sets the value of this matrix to the result of multiplying itself with
	 * matrix m1.
	 * 
	 * @param m1
	 *            the other matrix
	 */
	public final void mul(Matrix3f m1) {
		float _m00, _m01, _m02, _m10, _m11, _m12, _m20, _m21, _m22;

		_m00 = this.m00 * m1.m00 + this.m01 * m1.m10 + this.m02 * m1.m20;
		_m01 = this.m00 * m1.m01 + this.m01 * m1.m11 + this.m02 * m1.m21;
		_m02 = this.m00 * m1.m02 + this.m01 * m1.m12 + this.m02 * m1.m22;

		_m10 = this.m10 * m1.m00 + this.m11 * m1.m10 + this.m12 * m1.m20;
		_m11 = this.m10 * m1.m01 + this.m11 * m1.m11 + this.m12 * m1.m21;
		_m12 = this.m10 * m1.m02 + this.m11 * m1.m12 + this.m12 * m1.m22;

		_m20 = this.m20 * m1.m00 + this.m21 * m1.m10 + this.m22 * m1.m20;
		_m21 = this.m20 * m1.m01 + this.m21 * m1.m11 + this.m22 * m1.m21;
		_m22 = this.m20 * m1.m02 + this.m21 * m1.m12 + this.m22 * m1.m22;

		this.m00 = _m00;
		this.m01 = _m01;
		this.m02 = _m02;
		this.m10 = _m10;
		this.m11 = _m11;
		this.m12 = _m12;
		this.m20 = _m20;
		this.m21 = _m21;
		this.m22 = _m22;
	}

	/**
	 * Sets the value of this matrix to the result of multiplying the two
	 * argument matrices together.
	 * 
	 * @param m1
	 *            the first matrix
	 * @param m2
	 *            the second matrix
	 */
	public final void mul(Matrix3f m1, Matrix3f m2) {
		if (this != m1 && this != m2) {
			this.m00 = m1.m00 * m2.m00 + m1.m01 * m2.m10 + m1.m02 * m2.m20;
			this.m01 = m1.m00 * m2.m01 + m1.m01 * m2.m11 + m1.m02 * m2.m21;
			this.m02 = m1.m00 * m2.m02 + m1.m01 * m2.m12 + m1.m02 * m2.m22;

			this.m10 = m1.m10 * m2.m00 + m1.m11 * m2.m10 + m1.m12 * m2.m20;
			this.m11 = m1.m10 * m2.m01 + m1.m11 * m2.m11 + m1.m12 * m2.m21;
			this.m12 = m1.m10 * m2.m02 + m1.m11 * m2.m12 + m1.m12 * m2.m22;

			this.m20 = m1.m20 * m2.m00 + m1.m21 * m2.m10 + m1.m22 * m2.m20;
			this.m21 = m1.m20 * m2.m01 + m1.m21 * m2.m11 + m1.m22 * m2.m21;
			this.m22 = m1.m20 * m2.m02 + m1.m21 * m2.m12 + m1.m22 * m2.m22;
		} else {
			float _m00, _m01, _m02, _m10, _m11, _m12, _m20, _m21, _m22; // vars for temp
			// result matrix

			_m00 = m1.m00 * m2.m00 + m1.m01 * m2.m10 + m1.m02 * m2.m20;
			_m01 = m1.m00 * m2.m01 + m1.m01 * m2.m11 + m1.m02 * m2.m21;
			_m02 = m1.m00 * m2.m02 + m1.m01 * m2.m12 + m1.m02 * m2.m22;

			_m10 = m1.m10 * m2.m00 + m1.m11 * m2.m10 + m1.m12 * m2.m20;
			_m11 = m1.m10 * m2.m01 + m1.m11 * m2.m11 + m1.m12 * m2.m21;
			_m12 = m1.m10 * m2.m02 + m1.m11 * m2.m12 + m1.m12 * m2.m22;

			_m20 = m1.m20 * m2.m00 + m1.m21 * m2.m10 + m1.m22 * m2.m20;
			_m21 = m1.m20 * m2.m01 + m1.m21 * m2.m11 + m1.m22 * m2.m21;
			_m22 = m1.m20 * m2.m02 + m1.m21 * m2.m12 + m1.m22 * m2.m22;

			this.m00 = _m00;
			this.m01 = _m01;
			this.m02 = _m02;
			this.m10 = _m10;
			this.m11 = _m11;
			this.m12 = _m12;
			this.m20 = _m20;
			this.m21 = _m21;
			this.m22 = _m22;
		}
	}

	/**
	 * Multiplies this matrix by matrix m1, does an SVD normalization of the
	 * result, and places the result back into this matrix this =
	 * SVDnorm(this*m1).
	 * 
	 * @param m1
	 *            the matrix on the right hand side of the multiplication
	 */
	public final void mulNormalize(Matrix3f m1) {

		float[] tmp = new float[9]; // scratch matrix
		float[] tmp_rot = new float[9]; // scratch matrix
		float[] tmp_scale = new float[3]; // scratch matrix

		tmp[0] = this.m00 * m1.m00 + this.m01 * m1.m10 + this.m02 * m1.m20;
		tmp[1] = this.m00 * m1.m01 + this.m01 * m1.m11 + this.m02 * m1.m21;
		tmp[2] = this.m00 * m1.m02 + this.m01 * m1.m12 + this.m02 * m1.m22;

		tmp[3] = this.m10 * m1.m00 + this.m11 * m1.m10 + this.m12 * m1.m20;
		tmp[4] = this.m10 * m1.m01 + this.m11 * m1.m11 + this.m12 * m1.m21;
		tmp[5] = this.m10 * m1.m02 + this.m11 * m1.m12 + this.m12 * m1.m22;

		tmp[6] = this.m20 * m1.m00 + this.m21 * m1.m10 + this.m22 * m1.m20;
		tmp[7] = this.m20 * m1.m01 + this.m21 * m1.m11 + this.m22 * m1.m21;
		tmp[8] = this.m20 * m1.m02 + this.m21 * m1.m12 + this.m22 * m1.m22;

		compute_svd(tmp, tmp_scale, tmp_rot);

		this.m00 = tmp_rot[0];
		this.m01 = tmp_rot[1];
		this.m02 = tmp_rot[2];

		this.m10 = tmp_rot[3];
		this.m11 = tmp_rot[4];
		this.m12 = tmp_rot[5];

		this.m20 = tmp_rot[6];
		this.m21 = tmp_rot[7];
		this.m22 = tmp_rot[8];

	}

	/**
	 * Multiplies matrix m1 by matrix m2, does an SVD normalization of the
	 * result, and places the result into this matrix this = SVDnorm(m1*m2).
	 * 
	 * @param m1
	 *            the matrix on the left hand side of the multiplication
	 * @param m2
	 *            the matrix on the right hand side of the multiplication
	 */
	public final void mulNormalize(Matrix3f m1, Matrix3f m2) {

		float[] tmp = new float[9]; // scratch matrix
		float[] tmp_rot = new float[9]; // scratch matrix
		float[] tmp_scale = new float[3]; // scratch matrix

		tmp[0] = m1.m00 * m2.m00 + m1.m01 * m2.m10 + m1.m02 * m2.m20;
		tmp[1] = m1.m00 * m2.m01 + m1.m01 * m2.m11 + m1.m02 * m2.m21;
		tmp[2] = m1.m00 * m2.m02 + m1.m01 * m2.m12 + m1.m02 * m2.m22;

		tmp[3] = m1.m10 * m2.m00 + m1.m11 * m2.m10 + m1.m12 * m2.m20;
		tmp[4] = m1.m10 * m2.m01 + m1.m11 * m2.m11 + m1.m12 * m2.m21;
		tmp[5] = m1.m10 * m2.m02 + m1.m11 * m2.m12 + m1.m12 * m2.m22;

		tmp[6] = m1.m20 * m2.m00 + m1.m21 * m2.m10 + m1.m22 * m2.m20;
		tmp[7] = m1.m20 * m2.m01 + m1.m21 * m2.m11 + m1.m22 * m2.m21;
		tmp[8] = m1.m20 * m2.m02 + m1.m21 * m2.m12 + m1.m22 * m2.m22;

		compute_svd(tmp, tmp_scale, tmp_rot);

		this.m00 = tmp_rot[0];
		this.m01 = tmp_rot[1];
		this.m02 = tmp_rot[2];

		this.m10 = tmp_rot[3];
		this.m11 = tmp_rot[4];
		this.m12 = tmp_rot[5];

		this.m20 = tmp_rot[6];
		this.m21 = tmp_rot[7];
		this.m22 = tmp_rot[8];

	}

	/**
	 * Multiplies the transpose of matrix m1 times the transpose of matrix m2,
	 * and places the result into this.
	 * 
	 * @param m1
	 *            the matrix on the left hand side of the multiplication
	 * @param m2
	 *            the matrix on the right hand side of the multiplication
	 */
	public final void mulTransposeBoth(Matrix3f m1, Matrix3f m2) {
		if (this != m1 && this != m2) {
			this.m00 = m1.m00 * m2.m00 + m1.m10 * m2.m01 + m1.m20 * m2.m02;
			this.m01 = m1.m00 * m2.m10 + m1.m10 * m2.m11 + m1.m20 * m2.m12;
			this.m02 = m1.m00 * m2.m20 + m1.m10 * m2.m21 + m1.m20 * m2.m22;

			this.m10 = m1.m01 * m2.m00 + m1.m11 * m2.m01 + m1.m21 * m2.m02;
			this.m11 = m1.m01 * m2.m10 + m1.m11 * m2.m11 + m1.m21 * m2.m12;
			this.m12 = m1.m01 * m2.m20 + m1.m11 * m2.m21 + m1.m21 * m2.m22;

			this.m20 = m1.m02 * m2.m00 + m1.m12 * m2.m01 + m1.m22 * m2.m02;
			this.m21 = m1.m02 * m2.m10 + m1.m12 * m2.m11 + m1.m22 * m2.m12;
			this.m22 = m1.m02 * m2.m20 + m1.m12 * m2.m21 + m1.m22 * m2.m22;
		} else {
			float _m00, _m01, _m02, _m10, _m11, _m12, _m20, _m21, _m22; // vars for temp
			// result matrix

			_m00 = m1.m00 * m2.m00 + m1.m10 * m2.m01 + m1.m20 * m2.m02;
			_m01 = m1.m00 * m2.m10 + m1.m10 * m2.m11 + m1.m20 * m2.m12;
			_m02 = m1.m00 * m2.m20 + m1.m10 * m2.m21 + m1.m20 * m2.m22;

			_m10 = m1.m01 * m2.m00 + m1.m11 * m2.m01 + m1.m21 * m2.m02;
			_m11 = m1.m01 * m2.m10 + m1.m11 * m2.m11 + m1.m21 * m2.m12;
			_m12 = m1.m01 * m2.m20 + m1.m11 * m2.m21 + m1.m21 * m2.m22;

			_m20 = m1.m02 * m2.m00 + m1.m12 * m2.m01 + m1.m22 * m2.m02;
			_m21 = m1.m02 * m2.m10 + m1.m12 * m2.m11 + m1.m22 * m2.m12;
			_m22 = m1.m02 * m2.m20 + m1.m12 * m2.m21 + m1.m22 * m2.m22;

			this.m00 = _m00;
			this.m01 = _m01;
			this.m02 = _m02;
			this.m10 = _m10;
			this.m11 = _m11;
			this.m12 = _m12;
			this.m20 = _m20;
			this.m21 = _m21;
			this.m22 = _m22;
		}

	}

	/**
	 * Multiplies matrix m1 times the transpose of matrix m2, and places the
	 * result into this.
	 * 
	 * @param m1
	 *            the matrix on the left hand side of the multiplication
	 * @param m2
	 *            the matrix on the right hand side of the multiplication
	 */
	public final void mulTransposeRight(Matrix3f m1, Matrix3f m2) {
		if (this != m1 && this != m2) {
			this.m00 = m1.m00 * m2.m00 + m1.m01 * m2.m01 + m1.m02 * m2.m02;
			this.m01 = m1.m00 * m2.m10 + m1.m01 * m2.m11 + m1.m02 * m2.m12;
			this.m02 = m1.m00 * m2.m20 + m1.m01 * m2.m21 + m1.m02 * m2.m22;

			this.m10 = m1.m10 * m2.m00 + m1.m11 * m2.m01 + m1.m12 * m2.m02;
			this.m11 = m1.m10 * m2.m10 + m1.m11 * m2.m11 + m1.m12 * m2.m12;
			this.m12 = m1.m10 * m2.m20 + m1.m11 * m2.m21 + m1.m12 * m2.m22;

			this.m20 = m1.m20 * m2.m00 + m1.m21 * m2.m01 + m1.m22 * m2.m02;
			this.m21 = m1.m20 * m2.m10 + m1.m21 * m2.m11 + m1.m22 * m2.m12;
			this.m22 = m1.m20 * m2.m20 + m1.m21 * m2.m21 + m1.m22 * m2.m22;
		} else {
			float _m00, _m01, _m02, _m10, _m11, _m12, _m20, _m21, _m22; // vars for temp
			// result matrix

			_m00 = m1.m00 * m2.m00 + m1.m01 * m2.m01 + m1.m02 * m2.m02;
			_m01 = m1.m00 * m2.m10 + m1.m01 * m2.m11 + m1.m02 * m2.m12;
			_m02 = m1.m00 * m2.m20 + m1.m01 * m2.m21 + m1.m02 * m2.m22;

			_m10 = m1.m10 * m2.m00 + m1.m11 * m2.m01 + m1.m12 * m2.m02;
			_m11 = m1.m10 * m2.m10 + m1.m11 * m2.m11 + m1.m12 * m2.m12;
			_m12 = m1.m10 * m2.m20 + m1.m11 * m2.m21 + m1.m12 * m2.m22;

			_m20 = m1.m20 * m2.m00 + m1.m21 * m2.m01 + m1.m22 * m2.m02;
			_m21 = m1.m20 * m2.m10 + m1.m21 * m2.m11 + m1.m22 * m2.m12;
			_m22 = m1.m20 * m2.m20 + m1.m21 * m2.m21 + m1.m22 * m2.m22;

			this.m00 = _m00;
			this.m01 = _m01;
			this.m02 = _m02;
			this.m10 = _m10;
			this.m11 = _m11;
			this.m12 = _m12;
			this.m20 = _m20;
			this.m21 = _m21;
			this.m22 = _m22;
		}
	}

	/**
	 * Multiplies the transpose of matrix m1 times matrix m2, and places the
	 * result into this.
	 * 
	 * @param m1
	 *            the matrix on the left hand side of the multiplication
	 * @param m2
	 *            the matrix on the right hand side of the multiplication
	 */
	public final void mulTransposeLeft(Matrix3f m1, Matrix3f m2) {
		if (this != m1 && this != m2) {
			this.m00 = m1.m00 * m2.m00 + m1.m10 * m2.m10 + m1.m20 * m2.m20;
			this.m01 = m1.m00 * m2.m01 + m1.m10 * m2.m11 + m1.m20 * m2.m21;
			this.m02 = m1.m00 * m2.m02 + m1.m10 * m2.m12 + m1.m20 * m2.m22;

			this.m10 = m1.m01 * m2.m00 + m1.m11 * m2.m10 + m1.m21 * m2.m20;
			this.m11 = m1.m01 * m2.m01 + m1.m11 * m2.m11 + m1.m21 * m2.m21;
			this.m12 = m1.m01 * m2.m02 + m1.m11 * m2.m12 + m1.m21 * m2.m22;

			this.m20 = m1.m02 * m2.m00 + m1.m12 * m2.m10 + m1.m22 * m2.m20;
			this.m21 = m1.m02 * m2.m01 + m1.m12 * m2.m11 + m1.m22 * m2.m21;
			this.m22 = m1.m02 * m2.m02 + m1.m12 * m2.m12 + m1.m22 * m2.m22;
		} else {
			float _m00, _m01, _m02, _m10, _m11, _m12, _m20, _m21, _m22; // vars for temp
			// result matrix

			_m00 = m1.m00 * m2.m00 + m1.m10 * m2.m10 + m1.m20 * m2.m20;
			_m01 = m1.m00 * m2.m01 + m1.m10 * m2.m11 + m1.m20 * m2.m21;
			_m02 = m1.m00 * m2.m02 + m1.m10 * m2.m12 + m1.m20 * m2.m22;

			_m10 = m1.m01 * m2.m00 + m1.m11 * m2.m10 + m1.m21 * m2.m20;
			_m11 = m1.m01 * m2.m01 + m1.m11 * m2.m11 + m1.m21 * m2.m21;
			_m12 = m1.m01 * m2.m02 + m1.m11 * m2.m12 + m1.m21 * m2.m22;

			_m20 = m1.m02 * m2.m00 + m1.m12 * m2.m10 + m1.m22 * m2.m20;
			_m21 = m1.m02 * m2.m01 + m1.m12 * m2.m11 + m1.m22 * m2.m21;
			_m22 = m1.m02 * m2.m02 + m1.m12 * m2.m12 + m1.m22 * m2.m22;

			this.m00 = _m00;
			this.m01 = _m01;
			this.m02 = _m02;
			this.m10 = _m10;
			this.m11 = _m11;
			this.m12 = _m12;
			this.m20 = _m20;
			this.m21 = _m21;
			this.m22 = _m22;
		}
	}

	/**
	 * Perform singular value decomposition normalization of matrix m1 and place
	 * the normalized values into this.
	 * 
	 * @param m1
	 *            Provides the matrix values to be normalized
	 */
	public final void normalize(Matrix3f m1) {

		float[] tmp = new float[9]; // scratch matrix
		float[] tmp_rot = new float[9]; // scratch matrix
		float[] tmp_scale = new float[3]; // scratch matrix

		tmp[0] = m1.m00;
		tmp[1] = m1.m01;
		tmp[2] = m1.m02;

		tmp[3] = m1.m10;
		tmp[4] = m1.m11;
		tmp[5] = m1.m12;

		tmp[6] = m1.m20;
		tmp[7] = m1.m21;
		tmp[8] = m1.m22;

		compute_svd(tmp, tmp_scale, tmp_rot);

		this.m00 = tmp_rot[0];
		this.m01 = tmp_rot[1];
		this.m02 = tmp_rot[2];

		this.m10 = tmp_rot[3];
		this.m11 = tmp_rot[4];
		this.m12 = tmp_rot[5];

		this.m20 = tmp_rot[6];
		this.m21 = tmp_rot[7];
		this.m22 = tmp_rot[8];
	}

	/**
	 * Perform cross product normalization of this matrix.
	 */

	public final void normalizeCP() {
		float mag = 1f / (float)Math.sqrt(this.m00 * this.m00 + this.m10 * this.m10 + this.m20 * this.m20);
		this.m00 = this.m00 * mag;
		this.m10 = this.m10 * mag;
		this.m20 = this.m20 * mag;

		mag = 1f / (float)Math.sqrt(this.m01 * this.m01 + this.m11 * this.m11 + this.m21 * this.m21);
		this.m01 = this.m01 * mag;
		this.m11 = this.m11 * mag;
		this.m21 = this.m21 * mag;

		this.m02 = this.m10 * this.m21 - this.m11 * this.m20;
		this.m12 = this.m01 * this.m20 - this.m00 * this.m21;
		this.m22 = this.m00 * this.m11 - this.m01 * this.m10;
	}

	/**
	 * Perform cross product normalization of matrix m1 and place the normalized
	 * values into this.
	 * 
	 * @param m1
	 *            Provides the matrix values to be normalized
	 */
	public final void normalizeCP(Matrix3f m1) {
		float mag = 1f / (float)Math.sqrt(m1.m00 * m1.m00 + m1.m10 * m1.m10 + m1.m20
				* m1.m20);
		this.m00 = m1.m00 * mag;
		this.m10 = m1.m10 * mag;
		this.m20 = m1.m20 * mag;

		mag = 1f / (float)Math.sqrt(m1.m01 * m1.m01 + m1.m11 * m1.m11 + m1.m21
				* m1.m21);
		this.m01 = m1.m01 * mag;
		this.m11 = m1.m11 * mag;
		this.m21 = m1.m21 * mag;

		this.m02 = this.m10 * this.m21 - this.m11 * this.m20;
		this.m12 = this.m01 * this.m20 - this.m00 * this.m21;
		this.m22 = this.m00 * this.m11 - this.m01 * this.m10;
	}

	/**
	 * Returns true if all of the data members of Matrix3f m1 are equal to the
	 * corresponding data members in this Matrix3f.
	 * 
	 * @param m1
	 *            the matrix with which the comparison is made
	 * @return true or false
	 */
	public boolean equals(Matrix3f m1) {
		try {
			return (this.m00 == m1.m00 && this.m01 == m1.m01
					&& this.m02 == m1.m02 && this.m10 == m1.m10
					&& this.m11 == m1.m11 && this.m12 == m1.m12
					&& this.m20 == m1.m20 && this.m21 == m1.m21 && this.m22 == m1.m22);
		} catch (NullPointerException e2) {
			return false;
		}

	}

	/**
	 * Returns true if the Object t1 is of type Matrix3f and all of the data
	 * members of t1 are equal to the corresponding data members in this
	 * Matrix3f.
	 * 
	 * @param t1
	 *            the matrix with which the comparison is made
	 * @return true or false
	 */
	@Override
	public boolean equals(Object t1) {
		try {
			Matrix3f m2 = (Matrix3f) t1;
			return (this.m00 == m2.m00 && this.m01 == m2.m01
					&& this.m02 == m2.m02 && this.m10 == m2.m10
					&& this.m11 == m2.m11 && this.m12 == m2.m12
					&& this.m20 == m2.m20 && this.m21 == m2.m21 && this.m22 == m2.m22);
		} catch (ClassCastException e1) {
			return false;
		} catch (NullPointerException e2) {
			return false;
		}

	}

	/**
	 * Returns true if the L-infinite distance between this matrix and matrix m1
	 * is less than or equal to the epsilon parameter, otherwise returns false.
	 * The L-infinite distance is equal to MAX[i=0,1,2 ; j=0,1,2 ;
	 * abs(this.m(i,j) - m1.m(i,j)]
	 * 
	 * @param m1
	 *            the matrix to be compared to this matrix
	 * @param epsilon
	 *            the threshold value
	 * @return <code>true</code> if this matrix is equals to the specified matrix at epsilon.
	 */
	public boolean epsilonEquals(Matrix3f m1, float epsilon) {
		float diff;

		diff = this.m00 - m1.m00;
		if ((diff < 0 ? -diff : diff) > epsilon)
			return false;

		diff = this.m01 - m1.m01;
		if ((diff < 0 ? -diff : diff) > epsilon)
			return false;

		diff = this.m02 - m1.m02;
		if ((diff < 0 ? -diff : diff) > epsilon)
			return false;

		diff = this.m10 - m1.m10;
		if ((diff < 0 ? -diff : diff) > epsilon)
			return false;

		diff = this.m11 - m1.m11;
		if ((diff < 0 ? -diff : diff) > epsilon)
			return false;

		diff = this.m12 - m1.m12;
		if ((diff < 0 ? -diff : diff) > epsilon)
			return false;

		diff = this.m20 - m1.m20;
		if ((diff < 0 ? -diff : diff) > epsilon)
			return false;

		diff = this.m21 - m1.m21;
		if ((diff < 0 ? -diff : diff) > epsilon)
			return false;

		diff = this.m22 - m1.m22;
		if ((diff < 0 ? -diff : diff) > epsilon)
			return false;

		return true;
	}

	/**
	 * Returns a hash code value based on the data values in this object. Two
	 * different Matrix3f objects with identical data values (i.e.,
	 * Matrix3f.equals returns true) will return the same hash code value. Two
	 * objects with different data members may return the same hash value,
	 * although this is not likely.
	 * 
	 * @return the integer hash code value
	 */
	@Override
	public int hashCode() {
		long bits = 1L;
		bits = 31L * bits + doubleToLongBits(this.m00);
		bits = 31L * bits + doubleToLongBits(this.m01);
		bits = 31L * bits + doubleToLongBits(this.m02);
		bits = 31L * bits + doubleToLongBits(this.m10);
		bits = 31L * bits + doubleToLongBits(this.m11);
		bits = 31L * bits + doubleToLongBits(this.m12);
		bits = 31L * bits + doubleToLongBits(this.m20);
		bits = 31L * bits + doubleToLongBits(this.m21);
		bits = 31L * bits + doubleToLongBits(this.m22);
		return (int) (bits ^ (bits >> 32));
	}

	private static long doubleToLongBits(float d) {
		// Check for +0 or -0
		if (d == 0f) {
			return 0L;
		}
		return Float.floatToIntBits(d);
	}

	/**
	 * Sets this matrix to all zeros.
	 */
	public final void setZero() {
		this.m00 = 0f;
		this.m01 = 0f;
		this.m02 = 0f;

		this.m10 = 0f;
		this.m11 = 0f;
		this.m12 = 0f;

		this.m20 = 0f;
		this.m21 = 0f;
		this.m22 = 0f;
	}

	/**
	 * Sets this matrix as diagonal.
	 * 
	 * @param m00
	 *            the first element of the diagonal
	 * @param m11
	 *            the second element of the diagonal
	 * @param m22
	 *            the third element of the diagonal
	 */
	public final void setDiagonal(float m00, float m11, float m22) {
		this.m00 = m00;
		this.m01 = 0f;
		this.m02 = 0f;
		this.m10 = 0f;
		this.m11 = m11;
		this.m12 = 0f;
		this.m20 = 0f;
		this.m21 = 0f;
		this.m22 = m22;
	}

	/**
	 * Negates the value of this matrix: this = -this.
	 */
	public final void negate() {
		this.m00 = -this.m00;
		this.m01 = -this.m01;
		this.m02 = -this.m02;

		this.m10 = -this.m10;
		this.m11 = -this.m11;
		this.m12 = -this.m12;

		this.m20 = -this.m20;
		this.m21 = -this.m21;
		this.m22 = -this.m22;

	}

	/**
	 * Sets the value of this matrix equal to the negation of of the Matrix3f
	 * parameter.
	 * 
	 * @param m1
	 *            the source matrix
	 */
	public final void negate(Matrix3f m1) {
		this.m00 = -m1.m00;
		this.m01 = -m1.m01;
		this.m02 = -m1.m02;

		this.m10 = -m1.m10;
		this.m11 = -m1.m11;
		this.m12 = -m1.m12;

		this.m20 = -m1.m20;
		this.m21 = -m1.m21;
		this.m22 = -m1.m22;
	}

	private static boolean epsilonEquals(float a, float b) {
		return Math.abs(a-b) <=  MathConstants.EPSILON;
	}

	private static void compute_svd(float[] m, float[] outScale, float[] outRot) {
		int i;
		float g;
		float[] u1 = new float[9];
		float[] v1 = new float[9];
		float[] t1 = new float[9];
		float[] t2 = new float[9];

		float[] tmp = t1;
		float[] single_values = t2;

		float[] rot = new float[9];
		float[] e = new float[3];
		float[] scales = new float[3];

		int negCnt = 0;
		float c1, c2, c3, c4;
		float s1, s2, s3, s4;

		for (i = 0; i < 9; ++i)
			rot[i] = m[i];

		// u1

		if (m[3] * m[3] < EPSILON) {
			u1[0] = 1f;
			u1[1] = 0f;
			u1[2] = 0f;
			u1[3] = 0f;
			u1[4] = 1f;
			u1[5] = 0f;
			u1[6] = 0f;
			u1[7] = 0f;
			u1[8] = 1f;
		} else if (m[0] * m[0] < EPSILON) {
			tmp[0] = m[0];
			tmp[1] = m[1];
			tmp[2] = m[2];
			m[0] = m[3];
			m[1] = m[4];
			m[2] = m[5];

			m[3] = -tmp[0]; // zero
			m[4] = -tmp[1];
			m[5] = -tmp[2];

			u1[0] = 0f;
			u1[1] = 1f;
			u1[2] = 0f;
			u1[3] = -1f;
			u1[4] = 0f;
			u1[5] = 0f;
			u1[6] = 0f;
			u1[7] = 0f;
			u1[8] = 1f;
		} else {
			g = 1f / (float)Math.sqrt(m[0] * m[0] + m[3] * m[3]);
			c1 = m[0] * g;
			s1 = m[3] * g;
			tmp[0] = c1 * m[0] + s1 * m[3];
			tmp[1] = c1 * m[1] + s1 * m[4];
			tmp[2] = c1 * m[2] + s1 * m[5];

			m[3] = -s1 * m[0] + c1 * m[3]; // zero
			m[4] = -s1 * m[1] + c1 * m[4];
			m[5] = -s1 * m[2] + c1 * m[5];

			m[0] = tmp[0];
			m[1] = tmp[1];
			m[2] = tmp[2];
			u1[0] = c1;
			u1[1] = s1;
			u1[2] = 0f;
			u1[3] = -s1;
			u1[4] = c1;
			u1[5] = 0f;
			u1[6] = 0f;
			u1[7] = 0f;
			u1[8] = 1f;
		}

		// u2

		if (m[6] * m[6] < EPSILON) {
			//
		} else if (m[0] * m[0] < EPSILON) {
			tmp[0] = m[0];
			tmp[1] = m[1];
			tmp[2] = m[2];
			m[0] = m[6];
			m[1] = m[7];
			m[2] = m[8];

			m[6] = -tmp[0]; // zero
			m[7] = -tmp[1];
			m[8] = -tmp[2];

			tmp[0] = u1[0];
			tmp[1] = u1[1];
			tmp[2] = u1[2];
			u1[0] = u1[6];
			u1[1] = u1[7];
			u1[2] = u1[8];

			u1[6] = -tmp[0]; // zero
			u1[7] = -tmp[1];
			u1[8] = -tmp[2];
		} else {
			g = 1f / (float)Math.sqrt(m[0] * m[0] + m[6] * m[6]);
			c2 = m[0] * g;
			s2 = m[6] * g;
			tmp[0] = c2 * m[0] + s2 * m[6];
			tmp[1] = c2 * m[1] + s2 * m[7];
			tmp[2] = c2 * m[2] + s2 * m[8];

			m[6] = -s2 * m[0] + c2 * m[6];
			m[7] = -s2 * m[1] + c2 * m[7];
			m[8] = -s2 * m[2] + c2 * m[8];
			m[0] = tmp[0];
			m[1] = tmp[1];
			m[2] = tmp[2];

			tmp[0] = c2 * u1[0];
			tmp[1] = c2 * u1[1];
			u1[2] = s2;

			tmp[6] = -u1[0] * s2;
			tmp[7] = -u1[1] * s2;
			u1[8] = c2;
			u1[0] = tmp[0];
			u1[1] = tmp[1];
			u1[6] = tmp[6];
			u1[7] = tmp[7];
		}

		// v1

		if (m[2] * m[2] < EPSILON) {
			v1[0] = 1f;
			v1[1] = 0f;
			v1[2] = 0f;
			v1[3] = 0f;
			v1[4] = 1f;
			v1[5] = 0f;
			v1[6] = 0f;
			v1[7] = 0f;
			v1[8] = 1f;
		} else if (m[1] * m[1] < EPSILON) {
			tmp[2] = m[2];
			tmp[5] = m[5];
			tmp[8] = m[8];
			m[2] = -m[1];
			m[5] = -m[4];
			m[8] = -m[7];

			m[1] = tmp[2]; // zero
			m[4] = tmp[5];
			m[7] = tmp[8];

			v1[0] = 1f;
			v1[1] = 0f;
			v1[2] = 0f;
			v1[3] = 0f;
			v1[4] = 0f;
			v1[5] = -1f;
			v1[6] = 0f;
			v1[7] = 1f;
			v1[8] = 0f;
		} else {
			g = 1f / (float)Math.sqrt(m[1] * m[1] + m[2] * m[2]);
			c3 = m[1] * g;
			s3 = m[2] * g;
			tmp[1] = c3 * m[1] + s3 * m[2]; // can assign to m[1]?
			m[2] = -s3 * m[1] + c3 * m[2]; // zero
			m[1] = tmp[1];

			tmp[4] = c3 * m[4] + s3 * m[5];
			m[5] = -s3 * m[4] + c3 * m[5];
			m[4] = tmp[4];

			tmp[7] = c3 * m[7] + s3 * m[8];
			m[8] = -s3 * m[7] + c3 * m[8];
			m[7] = tmp[7];

			v1[0] = 1f;
			v1[1] = 0f;
			v1[2] = 0f;
			v1[3] = 0f;
			v1[4] = c3;
			v1[5] = -s3;
			v1[6] = 0f;
			v1[7] = s3;
			v1[8] = c3;
		}

		// u3

		if (m[7] * m[7] < EPSILON) {
			//
		} else if (m[4] * m[4] < EPSILON) {
			tmp[3] = m[3];
			tmp[4] = m[4];
			tmp[5] = m[5];
			m[3] = m[6]; // zero
			m[4] = m[7];
			m[5] = m[8];

			m[6] = -tmp[3]; // zero
			m[7] = -tmp[4]; // zero
			m[8] = -tmp[5];

			tmp[3] = u1[3];
			tmp[4] = u1[4];
			tmp[5] = u1[5];
			u1[3] = u1[6];
			u1[4] = u1[7];
			u1[5] = u1[8];

			u1[6] = -tmp[3]; // zero
			u1[7] = -tmp[4];
			u1[8] = -tmp[5];

		} else {
			g = 1f / (float)Math.sqrt(m[4] * m[4] + m[7] * m[7]);
			c4 = m[4] * g;
			s4 = m[7] * g;
			tmp[3] = c4 * m[3] + s4 * m[6];
			m[6] = -s4 * m[3] + c4 * m[6]; // zero
			m[3] = tmp[3];

			tmp[4] = c4 * m[4] + s4 * m[7];
			m[7] = -s4 * m[4] + c4 * m[7];
			m[4] = tmp[4];

			tmp[5] = c4 * m[5] + s4 * m[8];
			m[8] = -s4 * m[5] + c4 * m[8];
			m[5] = tmp[5];

			tmp[3] = c4 * u1[3] + s4 * u1[6];
			u1[6] = -s4 * u1[3] + c4 * u1[6];
			u1[3] = tmp[3];

			tmp[4] = c4 * u1[4] + s4 * u1[7];
			u1[7] = -s4 * u1[4] + c4 * u1[7];
			u1[4] = tmp[4];

			tmp[5] = c4 * u1[5] + s4 * u1[8];
			u1[8] = -s4 * u1[5] + c4 * u1[8];
			u1[5] = tmp[5];
		}

		single_values[0] = m[0];
		single_values[1] = m[4];
		single_values[2] = m[8];
		e[0] = m[1];
		e[1] = m[5];

		if (e[0] * e[0] < EPSILON && e[1] * e[1] < EPSILON) {
			//
		} else {
			compute_qr(single_values, e, u1, v1);
		}

		scales[0] = single_values[0];
		scales[1] = single_values[1];
		scales[2] = single_values[2];

		// Do some optimization here. If scale is unity, simply return the
		// rotation matrix.
		if (epsilonEquals(Math.abs(scales[0]), 1f)
				&& epsilonEquals(Math.abs(scales[1]), 1f)
				&& epsilonEquals(Math.abs(scales[2]), 1f)) {
			// System.out.println("Scale components almost to 1f");

			for (i = 0; i < 3; ++i)
				if (scales[i] < 0f)
					++negCnt;

			if ((negCnt == 0) || (negCnt == 2)) {
				// System.out.println("Optimize!!");
				outScale[0] = outScale[1] = outScale[2] = 1f;
				for (i = 0; i < 9; ++i)
					outRot[i] = rot[i];

				return;
			}
		}

		transpose_mat(u1, t1);
		transpose_mat(v1, t2);

		svdReorder(m, t1, t2, scales, outRot, outScale);

	}

	private static void svdReorder(float[] m, float[] t1, float[] t2,
			float[] scales, float[] outRot, float[] outScale) {

		int[] out = new int[3];
		int in0, in1, in2, index, i;
		float[] mag = new float[3];
		float[] rot = new float[9];

		// check for rotation information in the scales
		if (scales[0] < 0f) { // move the rotation info to rotation matrix
			scales[0] = -scales[0];
			t2[0] = -t2[0];
			t2[1] = -t2[1];
			t2[2] = -t2[2];
		}
		if (scales[1] < 0f) { // move the rotation info to rotation matrix
			scales[1] = -scales[1];
			t2[3] = -t2[3];
			t2[4] = -t2[4];
			t2[5] = -t2[5];
		}
		if (scales[2] < 0f) { // move the rotation info to rotation matrix
			scales[2] = -scales[2];
			t2[6] = -t2[6];
			t2[7] = -t2[7];
			t2[8] = -t2[8];
		}

		mat_mul(t1, t2, rot);

		// check for equal scales case and do not reorder
		if (epsilonEquals(Math.abs(scales[0]), Math.abs(scales[1]))
				&& epsilonEquals(Math.abs(scales[1]), Math.abs(scales[2]))) {
			for (i = 0; i < 9; ++i) {
				outRot[i] = rot[i];
			}
			for (i = 0; i < 3; ++i) {
				outScale[i] = scales[i];
			}

		} else {

			// sort the order of the results of SVD
			if (scales[0] > scales[1]) {
				if (scales[0] > scales[2]) {
					if (scales[2] > scales[1]) {
						out[0] = 0;
						out[1] = 2;
						out[2] = 1; // xzy
					} else {
						out[0] = 0;
						out[1] = 1;
						out[2] = 2; // xyz
					}
				} else {
					out[0] = 2;
					out[1] = 0;
					out[2] = 1; // zxy
				}
			} else { // y > x
				if (scales[1] > scales[2]) {
					if (scales[2] > scales[0]) {
						out[0] = 1;
						out[1] = 2;
						out[2] = 0; // yzx
					} else {
						out[0] = 1;
						out[1] = 0;
						out[2] = 2; // yxz
					}
				} else {
					out[0] = 2;
					out[1] = 1;
					out[2] = 0; // zyx
				}
			}

			/*
			 * System.out.println("\nscales="+scales[0]+" "+scales[1]+" "+scales[
			 * 2]); System.out.println("\nrot="+rot[0]+" "+rot[1]+" "+rot[2]);
			 * System.out.println("rot="+rot[3]+" "+rot[4]+" "+rot[5]);
			 * System.out.println("rot="+rot[6]+" "+rot[7]+" "+rot[8]);
			 */

			// sort the order of the input matrix
			mag[0] = (m[0] * m[0] + m[1] * m[1] + m[2] * m[2]);
			mag[1] = (m[3] * m[3] + m[4] * m[4] + m[5] * m[5]);
			mag[2] = (m[6] * m[6] + m[7] * m[7] + m[8] * m[8]);

			if (mag[0] > mag[1]) {
				if (mag[0] > mag[2]) {
					if (mag[2] > mag[1]) {
						// 0 - 2 - 1
						in0 = 0;
						in2 = 1;
						in1 = 2;// xzy
					} else {
						// 0 - 1 - 2
						in0 = 0;
						in1 = 1;
						in2 = 2; // xyz
					}
				} else {
					// 2 - 0 - 1
					in2 = 0;
					in0 = 1;
					in1 = 2; // zxy
				}
			} else { // y > x 1>0
				if (mag[1] > mag[2]) {
					if (mag[2] > mag[0]) {
						// 1 - 2 - 0
						in1 = 0;
						in2 = 1;
						in0 = 2; // yzx
					} else {
						// 1 - 0 - 2
						in1 = 0;
						in0 = 1;
						in2 = 2; // yxz
					}
				} else {
					// 2 - 1 - 0
					in2 = 0;
					in1 = 1;
					in0 = 2; // zyx
				}
			}

			index = out[in0];
			outScale[0] = scales[index];

			index = out[in1];
			outScale[1] = scales[index];

			index = out[in2];
			outScale[2] = scales[index];

			index = out[in0];
			outRot[0] = rot[index];

			index = out[in0] + 3;
			outRot[0 + 3] = rot[index];

			index = out[in0] + 6;
			outRot[0 + 6] = rot[index];

			index = out[in1];
			outRot[1] = rot[index];

			index = out[in1] + 3;
			outRot[1 + 3] = rot[index];

			index = out[in1] + 6;
			outRot[1 + 6] = rot[index];

			index = out[in2];
			outRot[2] = rot[index];

			index = out[in2] + 3;
			outRot[2 + 3] = rot[index];

			index = out[in2] + 6;
			outRot[2 + 6] = rot[index];
		}
	}

	private static int compute_qr(float[] s, float[] e, float[] u, float[] v) {

		int k;
		boolean converged;
		float shift, r;
		float[] cosl = new float[2];
		float[] cosr = new float[2];
		float[] sinl = new float[2];
		float[] sinr = new float[2];
		float[] m = new float[9];

		float utemp, vtemp;
		float f, g;

		final int MAX_INTERATIONS = 10;
		final float CONVERGE_TOL = 4.89E-15f;

		float c_b48 = 1f;
		//int first;
		converged = false;

		//first = 1;

		if (Math.abs(e[1]) < CONVERGE_TOL || Math.abs(e[0]) < CONVERGE_TOL)
			converged = true;

		for (k = 0; k < MAX_INTERATIONS && !converged; ++k) {
			shift = compute_shift(s[1], e[1], s[2]);
			f = (Math.abs(s[0]) - shift) * (d_sign(c_b48, s[0]) + shift / s[0]);
			g = e[0];
			r = compute_rot(f, g, sinr, cosr, 0/*, first*/);
			f = cosr[0] * s[0] + sinr[0] * e[0];
			e[0] = cosr[0] * e[0] - sinr[0] * s[0];
			g = sinr[0] * s[1];
			s[1] = cosr[0] * s[1];

			r = compute_rot(f, g, sinl, cosl, 0/*, first*/);
			//first = 0;
			s[0] = r;
			f = cosl[0] * e[0] + sinl[0] * s[1];
			s[1] = cosl[0] * s[1] - sinl[0] * e[0];
			g = sinl[0] * e[1];
			e[1] = cosl[0] * e[1];

			r = compute_rot(f, g, sinr, cosr, 1/*, first*/);
			e[0] = r;
			f = cosr[1] * s[1] + sinr[1] * e[1];
			e[1] = cosr[1] * e[1] - sinr[1] * s[1];
			g = sinr[1] * s[2];
			s[2] = cosr[1] * s[2];

			r = compute_rot(f, g, sinl, cosl, 1/*, first*/);
			s[1] = r;
			f = cosl[1] * e[1] + sinl[1] * s[2];
			s[2] = cosl[1] * s[2] - sinl[1] * e[1];
			e[1] = f;

			// update u matrices
			utemp = u[0];
			u[0] = cosl[0] * utemp + sinl[0] * u[3];
			u[3] = -sinl[0] * utemp + cosl[0] * u[3];
			utemp = u[1];
			u[1] = cosl[0] * utemp + sinl[0] * u[4];
			u[4] = -sinl[0] * utemp + cosl[0] * u[4];
			utemp = u[2];
			u[2] = cosl[0] * utemp + sinl[0] * u[5];
			u[5] = -sinl[0] * utemp + cosl[0] * u[5];

			utemp = u[3];
			u[3] = cosl[1] * utemp + sinl[1] * u[6];
			u[6] = -sinl[1] * utemp + cosl[1] * u[6];
			utemp = u[4];
			u[4] = cosl[1] * utemp + sinl[1] * u[7];
			u[7] = -sinl[1] * utemp + cosl[1] * u[7];
			utemp = u[5];
			u[5] = cosl[1] * utemp + sinl[1] * u[8];
			u[8] = -sinl[1] * utemp + cosl[1] * u[8];

			// update v matrices

			vtemp = v[0];
			v[0] = cosr[0] * vtemp + sinr[0] * v[1];
			v[1] = -sinr[0] * vtemp + cosr[0] * v[1];
			vtemp = v[3];
			v[3] = cosr[0] * vtemp + sinr[0] * v[4];
			v[4] = -sinr[0] * vtemp + cosr[0] * v[4];
			vtemp = v[6];
			v[6] = cosr[0] * vtemp + sinr[0] * v[7];
			v[7] = -sinr[0] * vtemp + cosr[0] * v[7];

			vtemp = v[1];
			v[1] = cosr[1] * vtemp + sinr[1] * v[2];
			v[2] = -sinr[1] * vtemp + cosr[1] * v[2];
			vtemp = v[4];
			v[4] = cosr[1] * vtemp + sinr[1] * v[5];
			v[5] = -sinr[1] * vtemp + cosr[1] * v[5];
			vtemp = v[7];
			v[7] = cosr[1] * vtemp + sinr[1] * v[8];
			v[8] = -sinr[1] * vtemp + cosr[1] * v[8];

			m[0] = s[0];
			m[1] = e[0];
			m[2] = 0f;
			m[3] = 0f;
			m[4] = s[1];
			m[5] = e[1];
			m[6] = 0f;
			m[7] = 0f;
			m[8] = s[2];

			if (Math.abs(e[1]) < CONVERGE_TOL || Math.abs(e[0]) < CONVERGE_TOL)
				converged = true;
		}

		if (Math.abs(e[1]) < CONVERGE_TOL) {
			compute_2X2(s[0], e[0], s[1], s, sinl, cosl, sinr, cosr, 0);

			utemp = u[0];
			u[0] = cosl[0] * utemp + sinl[0] * u[3];
			u[3] = -sinl[0] * utemp + cosl[0] * u[3];
			utemp = u[1];
			u[1] = cosl[0] * utemp + sinl[0] * u[4];
			u[4] = -sinl[0] * utemp + cosl[0] * u[4];
			utemp = u[2];
			u[2] = cosl[0] * utemp + sinl[0] * u[5];
			u[5] = -sinl[0] * utemp + cosl[0] * u[5];

			// update v matrices

			vtemp = v[0];
			v[0] = cosr[0] * vtemp + sinr[0] * v[1];
			v[1] = -sinr[0] * vtemp + cosr[0] * v[1];
			vtemp = v[3];
			v[3] = cosr[0] * vtemp + sinr[0] * v[4];
			v[4] = -sinr[0] * vtemp + cosr[0] * v[4];
			vtemp = v[6];
			v[6] = cosr[0] * vtemp + sinr[0] * v[7];
			v[7] = -sinr[0] * vtemp + cosr[0] * v[7];
		} else {
			compute_2X2(s[1], e[1], s[2], s, sinl, cosl, sinr, cosr, 1);

			utemp = u[3];
			u[3] = cosl[0] * utemp + sinl[0] * u[6];
			u[6] = -sinl[0] * utemp + cosl[0] * u[6];
			utemp = u[4];
			u[4] = cosl[0] * utemp + sinl[0] * u[7];
			u[7] = -sinl[0] * utemp + cosl[0] * u[7];
			utemp = u[5];
			u[5] = cosl[0] * utemp + sinl[0] * u[8];
			u[8] = -sinl[0] * utemp + cosl[0] * u[8];

			// update v matrices

			vtemp = v[1];
			v[1] = cosr[0] * vtemp + sinr[0] * v[2];
			v[2] = -sinr[0] * vtemp + cosr[0] * v[2];
			vtemp = v[4];
			v[4] = cosr[0] * vtemp + sinr[0] * v[5];
			v[5] = -sinr[0] * vtemp + cosr[0] * v[5];
			vtemp = v[7];
			v[7] = cosr[0] * vtemp + sinr[0] * v[8];
			v[8] = -sinr[0] * vtemp + cosr[0] * v[8];
		}

		return (0);
	}

	private static float d_sign(float a, float b) {
		float x;
		x = (a >= 0 ? a : -a);
		return (b >= 0 ? x : -x);
	}

	private static float compute_shift(float f, float g, float h) {
		float d__1, d__2;
		float fhmn, fhmx, c, fa, ga, ha, as, at, au;
		float ssmin;

		fa = Math.abs(f);
		ga = Math.abs(g);
		ha = Math.abs(h);
		fhmn = Math.min(fa, ha);
		fhmx = Math.max(fa, ha);
		if (fhmn == 0f) {
			ssmin = 0f;
			if (fhmx == 0f) {
				//
			} else {
				d__1 = Math.min(fhmx, ga) / Math.max(fhmx, ga);
			}
		} else {
			if (ga < fhmx) {
				as = fhmn / fhmx + 1f;
				at = (fhmx - fhmn) / fhmx;
				d__1 = ga / fhmx;
				au = d__1 * d__1;
				c = 2f / ((float)Math.sqrt(as * as + au) + (float)Math.sqrt(at * at + au));
				ssmin = fhmn * c;
			} else {
				au = fhmx / ga;
				if (au == 0f) {
					ssmin = fhmn * fhmx / ga;
				} else {
					as = fhmn / fhmx + 1f;
					at = (fhmx - fhmn) / fhmx;
					d__1 = as * au;
					d__2 = at * au;
					c = 1f / ((float)Math.sqrt(d__1 * d__1 + 1f) + (float)Math.sqrt(d__2
							* d__2 + 1f));
					ssmin = fhmn * c * au;
					ssmin += ssmin;
				}
			}
		}

		return (ssmin);
	}

	private static int compute_2X2(float f, float g, float h,
			float[] single_values, float[] snl, float[] csl, float[] snr,
			float[] csr, int index) {

		float c_b3 = 2f;
		float c_b4 = 1f;

		float d__1;
		int pmax;
		float temp;
		boolean swap;
		float a, d, l, m, r, s, t, tsign, fa, ga, ha;
		float ft, gt, ht, mm;
		boolean gasmal;
		float tt, clt, crt, slt, srt;
		float ssmin, ssmax;

		ssmax = single_values[0];
		ssmin = single_values[1];
		clt = 0f;
		crt = 0f;
		slt = 0f;
		srt = 0f;
		tsign = 0f;

		ft = f;
		fa = Math.abs(ft);
		ht = h;
		ha = Math.abs(h);

		pmax = 1;
		if (ha > fa)
			swap = true;
		else
			swap = false;

		if (swap) {
			pmax = 3;
			temp = ft;
			ft = ht;
			ht = temp;
			temp = fa;
			fa = ha;
			ha = temp;

		}
		gt = g;
		ga = Math.abs(gt);
		if (ga == 0f) {

			single_values[1] = ha;
			single_values[0] = fa;
			clt = 1f;
			crt = 1f;
			slt = 0f;
			srt = 0f;
		} else {
			gasmal = true;

			if (ga > fa) {
				pmax = 2;
				if (fa / ga < EPSILON) {

					gasmal = false;
					ssmax = ga;
					if (ha > 1.) {
						ssmin = fa / (ga / ha);
					} else {
						ssmin = fa / ga * ha;
					}
					clt = 1f;
					slt = ht / gt;
					srt = 1f;
					crt = ft / gt;
				}
			}
			if (gasmal) {

				d = fa - ha;
				if (d == fa) {

					l = 1f;
				} else {
					l = d / fa;
				}

				m = gt / ft;

				t = 2f - l;

				mm = m * m;
				tt = t * t;
				s = (float)Math.sqrt(tt + mm);

				if (l == 0f) {
					r = Math.abs(m);
				} else {
					r = (float)Math.sqrt(l * l + mm);
				}

				a = (s + r) * .5f;

				if (ga > fa) {
					pmax = 2;
					if (fa / ga < EPSILON) {

						gasmal = false;
						ssmax = ga;
						if (ha > 1.) {
							ssmin = fa / (ga / ha);
						} else {
							ssmin = fa / ga * ha;
						}
						clt = 1f;
						slt = ht / gt;
						srt = 1f;
						crt = ft / gt;
					}
				}
				if (gasmal) {

					d = fa - ha;
					if (d == fa) {

						l = 1f;
					} else {
						l = d / fa;
					}

					m = gt / ft;

					t = 2f - l;

					mm = m * m;
					tt = t * t;
					s = (float)Math.sqrt(tt + mm);

					if (l == 0f) {
						r = Math.abs(m);
					} else {
						r = (float)Math.sqrt(l * l + mm);
					}

					a = (s + r) * .5f;

					ssmin = ha / a;
					ssmax = fa * a;
					if (mm == 0f) {

						if (l == 0f) {
							t = d_sign(c_b3, ft) * d_sign(c_b4, gt);
						} else {
							t = gt / d_sign(d, ft) + m / t;
						}
					} else {
						t = (m / (s + t) + m / (r + l)) * (a + 1f);
					}
					l = (float)Math.sqrt(t * t + 4f);
					crt = 2f / l;
					srt = t / l;
					clt = (crt + srt * m) / a;
					slt = ht / ft * srt / a;
				}
			}
			if (swap) {
				csl[0] = srt;
				snl[0] = crt;
				csr[0] = slt;
				snr[0] = clt;
			} else {
				csl[0] = clt;
				snl[0] = slt;
				csr[0] = crt;
				snr[0] = srt;
			}

			if (pmax == 1) {
				tsign = d_sign(c_b4, csr[0]) * d_sign(c_b4, csl[0])
						* d_sign(c_b4, f);
			}
			if (pmax == 2) {
				tsign = d_sign(c_b4, snr[0]) * d_sign(c_b4, csl[0])
						* d_sign(c_b4, g);
			}
			if (pmax == 3) {
				tsign = d_sign(c_b4, snr[0]) * d_sign(c_b4, snl[0])
						* d_sign(c_b4, h);
			}
			single_values[index] = d_sign(ssmax, tsign);
			d__1 = tsign * d_sign(c_b4, f) * d_sign(c_b4, h);
			single_values[index + 1] = d_sign(ssmin, d__1);

		}
		return 0;
	}

	private static float compute_rot(float f, float g, float[] sin, float[] cos,
			int index) {
		float cs, sn;
		int i;
		float scale;
		int count;
		float f1, g1;
		float r;
		final double safmn2 = 2.002083095183101E-146;
		final double safmx2 = 4.994797680505588E+145;

		if (g == 0f) {
			cs = 1f;
			sn = 0f;
			r = f;
		} else if (f == 0f) {
			cs = 0f;
			sn = 1f;
			r = g;
		} else {
			f1 = f;
			g1 = g;
			scale = Math.max(Math.abs(f1), Math.abs(g1));
			if (scale >= safmx2) {
				count = 0;
				while (scale >= safmx2) {
					++count;
					f1 *= safmn2;
					g1 *= safmn2;
					scale = Math.max(Math.abs(f1), Math.abs(g1));
				}
				r = (float)Math.sqrt(f1 * f1 + g1 * g1);
				cs = f1 / r;
				sn = g1 / r;
				for (i = 1; i <= count; ++i) {
					r *= safmx2;
				}
			} else if (scale <= safmn2) {
				count = 0;
				while (scale <= safmn2) {
					++count;
					f1 *= safmx2;
					g1 *= safmx2;
					scale = Math.max(Math.abs(f1), Math.abs(g1));
				}
				r = (float)Math.sqrt(f1 * f1 + g1 * g1);
				cs = f1 / r;
				sn = g1 / r;
				for (i = 1; i <= count; ++i) {
					r *= safmn2;
				}
			} else {
				r = (float)Math.sqrt(f1 * f1 + g1 * g1);
				cs = f1 / r;
				sn = g1 / r;
			}
			if (Math.abs(f) > Math.abs(g) && cs < 0f) {
				cs = -cs;
				sn = -sn;
				r = -r;
			}
		}
		sin[index] = sn;
		cos[index] = cs;
		return r;

	}

	private static void mat_mul(float[] m1, float[] m2, float[] m3) {
		int i;
		float[] tmp = new float[9];

		tmp[0] = m1[0] * m2[0] + m1[1] * m2[3] + m1[2] * m2[6];
		tmp[1] = m1[0] * m2[1] + m1[1] * m2[4] + m1[2] * m2[7];
		tmp[2] = m1[0] * m2[2] + m1[1] * m2[5] + m1[2] * m2[8];

		tmp[3] = m1[3] * m2[0] + m1[4] * m2[3] + m1[5] * m2[6];
		tmp[4] = m1[3] * m2[1] + m1[4] * m2[4] + m1[5] * m2[7];
		tmp[5] = m1[3] * m2[2] + m1[4] * m2[5] + m1[5] * m2[8];

		tmp[6] = m1[6] * m2[0] + m1[7] * m2[3] + m1[8] * m2[6];
		tmp[7] = m1[6] * m2[1] + m1[7] * m2[4] + m1[8] * m2[7];
		tmp[8] = m1[6] * m2[2] + m1[7] * m2[5] + m1[8] * m2[8];

		for (i = 0; i < 9; ++i) {
			m3[i] = tmp[i];
		}
	}

	private static void transpose_mat(float[] in, float[] out) {
		out[0] = in[0];
		out[1] = in[3];
		out[2] = in[6];

		out[3] = in[1];
		out[4] = in[4];
		out[5] = in[7];

		out[6] = in[2];
		out[7] = in[5];
		out[8] = in[8];
	}

	/**
	 * Creates a new object of the same class as this object.
	 * 
	 * @return a clone of this instance.
	 * @exception OutOfMemoryError
	 *                if there is not enough memory.
	 * @see java.lang.Cloneable
	 */
	@Override
	public Matrix3f clone() {
		Matrix3f m1 = null;
		try {
			m1 = (Matrix3f) super.clone();
		} catch (CloneNotSupportedException e) {
			// this shouldn't happen, since we are Cloneable
			throw new InternalError();
		}

		// Also need to create new tmp arrays (no need to actually clone them)
		return m1;
	}

	/**
	 * Get the first matrix element in the first row.
	 * 
	 * @return Returns the m00f
	 * @since vecmath 1.5
	 */
	public final float getM00() {
		return this.m00;
	}

	/**
	 * Set the first matrix element in the first row.
	 * 
	 * @param m00
	 *            The m00 to set.
	 * 
	 * @since vecmath 1.5
	 */
	public final void setM00(float m00) {
		this.m00 = m00;
	}

	/**
	 * Get the second matrix element in the first row.
	 * 
	 * @return Returns the m01.
	 * 
	 * @since vecmath 1.5
	 */
	public final float getM01() {
		return this.m01;
	}

	/**
	 * Set the second matrix element in the first row.
	 * 
	 * @param m01
	 *            The m01 to set.
	 * 
	 * @since vecmath 1.5
	 */
	public final void setM01(float m01) {
		this.m01 = m01;
	}

	/**
	 * Get the third matrix element in the first row.
	 * 
	 * @return Returns the m02.
	 * 
	 * @since vecmath 1.5
	 */
	public final float getM02() {
		return this.m02;
	}

	/**
	 * Set the third matrix element in the first row.
	 * 
	 * @param m02
	 *            The m02 to set.
	 * 
	 * @since vecmath 1.5
	 */
	public final void setM02(float m02) {
		this.m02 = m02;
	}

	/**
	 * Get first matrix element in the second row.
	 * 
	 * @return Returns the m10f
	 * 
	 * @since vecmath 1.5
	 */
	public final float getM10() {
		return this.m10;
	}

	/**
	 * Set first matrix element in the second row.
	 * 
	 * @param m10
	 *            The m10 to set.
	 * 
	 * @since vecmath 1.5
	 */
	public final void setM10(float m10) {
		this.m10 = m10;
	}

	/**
	 * Get second matrix element in the second row.
	 * 
	 * @return Returns the m11.
	 * 
	 * @since vecmath 1.5
	 */
	public final float getM11() {
		return this.m11;
	}

	/**
	 * Set the second matrix element in the second row.
	 * 
	 * @param m11
	 *            The m11 to set.
	 * 
	 * @since vecmath 1.5
	 */
	public final void setM11(float m11) {
		this.m11 = m11;
	}

	/**
	 * Get the third matrix element in the second row.
	 * 
	 * @return Returns the m12.
	 * 
	 * @since vecmath 1.5
	 */
	public final float getM12() {
		return this.m12;
	}

	/**
	 * Set the third matrix element in the second row.
	 * 
	 * @param m12
	 *            The m12 to set.
	 * 
	 * @since vecmath 1.5
	 */
	public final void setM12(float m12) {
		this.m12 = m12;
	}

	/**
	 * Get the first matrix element in the third row.
	 * 
	 * @return Returns the m20
	 * 
	 * @since vecmath 1.5
	 */
	public final float getM20() {
		return this.m20;
	}

	/**
	 * Set the first matrix element in the third row.
	 * 
	 * @param m20
	 *            The m20 to set.
	 * 
	 * @since vecmath 1.5
	 */
	public final void setM20(float m20) {
		this.m20 = m20;
	}

	/**
	 * Get the second matrix element in the third row.
	 * 
	 * @return Returns the m21.
	 * 
	 * @since vecmath 1.5
	 */
	public final float getM21() {
		return this.m21;
	}

	/**
	 * Set the second matrix element in the third row.
	 * 
	 * @param m21
	 *            The m21 to set.
	 * 
	 * @since vecmath 1.5
	 */
	public final void setM21(float m21) {
		this.m21 = m21;
	}

	/**
	 * Get the third matrix element in the third row .
	 * 
	 * @return Returns the m22.
	 * 
	 * @since vecmath 1.5
	 */
	public final float getM22() {
		return this.m22;
	}

	/**
	 * Set the third matrix element in the third row.
	 * 
	 * @param m22
	 *            The m22 to set.
	 * 
	 * @since vecmath 1.5
	 */
	public final void setM22(float m22) {
		this.m22 = m22;
	}

	/**
	 * Sets the scale component of the current matrix by factoring out the
	 * current scale (by doing an SVD) and multiplying by the new scale.
	 * 
	 * @param scale
	 *            the new scale amount
	 */
	public final void setScale(float scale) {

		float[] tmp_rot = new float[9]; // scratch matrix
		float[] tmp_scale = new float[3]; // scratch matrix

		getScaleRotate(tmp_scale, tmp_rot);

		this.m00 = tmp_rot[0] * scale;
		this.m01 = tmp_rot[1] * scale;
		this.m02 = tmp_rot[2] * scale;

		this.m10 = tmp_rot[3] * scale;
		this.m11 = tmp_rot[4] * scale;
		this.m12 = tmp_rot[5] * scale;

		this.m20 = tmp_rot[6] * scale;
		this.m21 = tmp_rot[7] * scale;
		this.m22 = tmp_rot[8] * scale;
	}
	
	/**
	 * Performs an SVD normalization of this matrix to calculate and return the
	 * uniform scale factor. If the matrix has non-uniform scale factors, the
	 * largest of the x, y scale factors will be returned. This matrix is
	 * not modified.
	 * 
	 * @return the scale factor of this matrix
	 */
	public final float getScale() {

		float[] tmp_scale = new float[3]; // scratch matrix
		float[] tmp_rot = new float[9]; // scratch matrix
		getScaleRotate(tmp_scale, tmp_rot);

		return (MathUtil.max(tmp_scale));

	}

	/**
	 * perform SVD (if necessary to get rotational component)
	 */
	private void getScaleRotate(float scales[], float rots[]) {

		float[] tmp = new float[9]; // scratch matrix

		tmp[0] = this.m00;
		tmp[1] = this.m01;
		tmp[2] = this.m02;

		tmp[3] = this.m10;
		tmp[4] = this.m11;
		tmp[5] = this.m12;

		tmp[6] = this.m20;
		tmp[7] = this.m21;
		tmp[8] = this.m22;
		compute_svd(tmp, scales, rots);

		return;
	}
	
	/**
	 * Performs singular value decomposition normalization of this matrix.
	 */
	public final void normalize() {
		float[] tmp_rot = new float[9]; // scratch matrix
		float[] tmp_scale = new float[3]; // scratch matrix

		getScaleRotate(tmp_scale, tmp_rot);

		this.m00 = tmp_rot[0];
		this.m01 = tmp_rot[1];
		this.m02 = tmp_rot[2];

		this.m10 = tmp_rot[3];
		this.m11 = tmp_rot[4];
		this.m12 = tmp_rot[5];

		this.m20 = tmp_rot[6];
		this.m21 = tmp_rot[7];
		this.m22 = tmp_rot[8];

	}

	/** Set this matrix with the covariance matrix's elements for the given
	 * set of tuples.
	 * 
	 * @param tuples
	 * @return the mean of the tuples.
	 */
	public final Vector3f cov(Vector3f... tuples) {
		return cov(Arrays.asList(tuples));
	}

	/** Set this matrix with the covariance matrix's elements for the given
	 * set of tuples.
	 * 
	 * @param tuples
	 * @return the mean of the tuples.
	 */
	public final Vector3f cov(Point3f... tuples) {
		return cov(Arrays.asList(tuples));
	}

	/** Set this matrix with the covariance matrix's elements for the given
	 * set of tuples.
	 * 
	 * @param tuples
	 * @return the mean of the tuples.
	 */
	public Vector3f cov(Iterable<? extends Tuple3f<?>> tuples) {
		setZero();

		// Compute the mean m
		Vector3f m = new Vector3f();
		int count = 0;
		for(Tuple3f<?> p : tuples) {
			m.add(p.getX(), p.getY(), p.getZ());
			++count;
		}

		if (count==0) return null;

		m.scale(1f/count);

		// Compute the covariance term [Gottshalk2000]
		// c_ij = sum(p'_i * p'_j) / n
		// c_ij = sum((p_i - m_i) * (p_j - m_j)) / n
		for(Tuple3f<?> p : tuples) {
			this.m00 += (p.getX() - m.getX()) * (p.getX() - m.getX());
			this.m01 += (p.getX() - m.getX()) * (p.getY() - m.getY()); // same as m10
			this.m02 += (p.getX() - m.getX()) * (p.getZ() - m.getZ()); // same as m20
			//cov.m10 += (p.getY() - m.getY()) * (p.getX() - m.getX()); // same as m01
			this.m11 += (p.getY() - m.getY()) * (p.getY() - m.getY());
			this.m12 += (p.getY() - m.getY()) * (p.getZ() - m.getZ()); // same as m21
			//cov.m20 += (p.getZ() - m.getZ()) * (p.getX() - m.getX()); // same as m02
			//cov.m21 += (p.getZ() - m.getZ()) * (p.getY() - m.getY()); // same as m12
			this.m22 += (p.getZ() - m.getZ()) * (p.getZ() - m.getZ());
		}

		this.m00 /= count;
		this.m01 /= count;
		this.m02 /= count;
		this.m10 = this.m01;
		this.m11 /= count;
		this.m12 /= count;
		this.m20 = this.m02;
		this.m21 = this.m12;
		this.m22 /= count;

		return m;
	}

	/** Replies if the matrix is symmetric.
	 * 
	 * @return <code>true</code> if the matrix is symmetric, otherwise
	 * <code>false</code>
	 */
	public boolean isSymmetric() {
		return	this.m01 == this.m10
				&&	this.m02 == this.m20
				&&	this.m12 == this.m21;
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
	 * @param eigenVectors are the matrix of vectors to fill. Eigen vectors are the
	 * columns of the matrix.
	 * @return the eigenvalues which are corresponding to the <var>eigenVectors</var> columns.
	 * @see "Mathematics for 3D Game Programming and Computer Graphics, 2nd edition; pp.437." 
	 */
	public float[] eigenVectorsOfSymmetricMatrix(Matrix3f eigenVectors) {
		// Copy values up to the diagonal
		float m11 = getElement(0,0);
		float m12 = getElement(0,1);
		float m13 = getElement(0,2);
		float m22 = getElement(1,1);
		float m23 = getElement(1,2);
		float m33 = getElement(2,2);

		eigenVectors.setIdentity();

		boolean sweepsConsumed = true;
		int i;
		float u, u2, u2p1, t, c, s;
		float tmp, ri0, ri1, ri2;

		for(int a=0; a<JACOBI_MAX_SWEEPS; ++a) {

			// Exit loop if off-diagonal entries are small enough
			if ((Math.abs(m12) < MathConstants.EPSILON)
					&& (Math.abs(m13) < MathConstants.EPSILON)
					&& (Math.abs(m23) < MathConstants.EPSILON)) {
				sweepsConsumed = false;
				break;
			}

			// Annihilate (1,2) entry
			if (m12 != 0f) {
				u = (m22 - m11) *.5f / m12;
				u2 = u*u;
				u2p1 = u2 + 1f;

				if (u2p1!=u2)
					t = (float)(Math.signum(u) * (Math.sqrt(u2p1) - Math.abs(u)));
				else
					t = .5f / u;

				c = 1f / (float)Math.sqrt(t*t + 1);
				s = c * t;

				m11 -= t * m12;
				m22 += t * m12;
				m12 = 0f;

				tmp = c * m13 - s * m23;
				m23 = s * m13 + c * m23;
				m13 = tmp;

				for(i=0; i<3; ++i) {
					ri0 = eigenVectors.getElement(i,0);
					ri1 = eigenVectors.getElement(i,1);
					eigenVectors.setElement(i, 0, c * ri0 - s * ri1);
					eigenVectors.setElement(i, 1, s * ri0 + c * ri1);
				}
			}

			// Annihilate (1,3) entry
			if (m13 != 0f) {
				u = (m33 - m11) *.5f / m13;
				u2 = u*u;
				u2p1 = u2 + 1f;

				if (u2p1!=u2)
					t = (float)(Math.signum(u) * (Math.sqrt(u2p1) - Math.abs(u)));
				else
					t = .5f / u;

				c = 1f / (float)Math.sqrt(t*t + 1);
				s = c * t;

				m11 -= t * m13;
				m33 += t * m13;
				m13 = 0f;

				tmp = c * m12 - s * m23;
				m23 = s * m12 + c * m23;
				m12 = tmp;

				for(i=0; i<3; ++i) {
					ri0 = eigenVectors.getElement(i,0);
					ri2 = eigenVectors.getElement(i,2);
					eigenVectors.setElement(i, 0, c * ri0 - s * ri2);
					eigenVectors.setElement(i, 2, s * ri0 + c * ri2);
				}
			}

			// Annihilate (2,3) entry
			if (m23 != 0f) {
				u = (m33 - m22) *.5f / m23;
				u2 = u*u;
				u2p1 = u2 + 1f;

				if (u2p1!=u2)
					t = (float)(Math.signum(u) * (Math.sqrt(u2p1) - Math.abs(u)));
				else
					t = .5f / u;

				c = 1f / (float)Math.sqrt(t*t + 1);
				s = c * t;

				m22 -= t * m23;
				m33 += t * m23;
				m23 = 0f;

				tmp = c * m12 - s * m13;
				m13 = s * m12 + c * m13;
				m12 = tmp;

				for(i=0; i<3; ++i) {
					ri1 = eigenVectors.getElement(i,1);
					ri2 = eigenVectors.getElement(i,2);
					eigenVectors.setElement(i, 1, c * ri1 - s * ri2);
					eigenVectors.setElement(i, 2, s * ri1 + c * ri2);
				}
			}
		}

		assert(!sweepsConsumed) : "Sweep count consumed during eigenvector computation"; //$NON-NLS-1$

		// eigenvalues are on the diagonal
		return new float[] { m11, m22, m33 };
	}
	
	/** Replies if the matrix is identity.
	 * <p>
	 * This function uses the equal-to-zero test with the error {@link MathConstants#EPSILON}.
	 * 
	 * @return <code>true</code> if the matrix is identity; <code>false</code> otherwise.
	 * @see MathUtil#isEpsilonZero(float)
	 * @see MathUtil#isEpsilonEqual(float, float)
	 */
	public boolean isIdentity() {
		return MathUtil.isEpsilonEqual(this.m00, 1f)
				&& MathUtil.isEpsilonZero(this.m01)
				&& MathUtil.isEpsilonZero(this.m02)
				&& MathUtil.isEpsilonZero(this.m10)
				&& MathUtil.isEpsilonEqual(this.m11, 1f)
				&& MathUtil.isEpsilonZero(this.m12)
				&& MathUtil.isEpsilonZero(this.m20)
				&& MathUtil.isEpsilonZero(this.m21)
				&& MathUtil.isEpsilonEqual(this.m22, 1f);
	}

}