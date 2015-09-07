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

import org.arakhne.afc.math.MathConstants;
import org.arakhne.afc.math.MathUtil;

/**
 * Is represented internally as a 4x4 floating point matrix. The mathematical
 * representation is row major, as in traditional matrix mathematics.
 * 
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class Matrix4f implements Serializable, Cloneable {

	private static final long serialVersionUID = 7216873052550769543L;

	/**
	 * The first matrix element in the first row.
	 */
	public double m00;

	/**
	 * The second matrix element in the first row.
	 */
	public double m01;

	/**
	 * The third matrix element in the first row.
	 */
	public double m02;

	/**
	 * The fourth matrix element in the first row.
	 */
	public double m03;

	/**
	 * The first matrix element in the second row.
	 */
	public double m10;

	/**
	 * The second matrix element in the second row.
	 */
	public double m11;

	/**
	 * The third matrix element in the second row.
	 */
	public double m12;

	/**
	 * The fourth matrix element in the second row.
	 */
	public double m13;

	/**
	 * The first matrix element in the third row.
	 */
	public double m20;

	/**
	 * The second matrix element in the third row.
	 */
	public double m21;

	/**
	 * The third matrix element in the third row.
	 */
	public double m22;

	/**
	 * The fourth matrix element in the third row.
	 */
	public double m23;

	/**
	 * The first matrix element in the fourth row.
	 */
	public double m30;

	/**
	 * The second matrix element in the fourth row.
	 */
	public double m31;

	/**
	 * The third matrix element in the fourth row.
	 */
	public double m32;

	/**
	 * The fourth matrix element in the fourth row.
	 */
	public double m33;

	/**
	 * Constructs and initializes a Matrix4f from the specified nine values.
	 * 
	 * @param m00
	 *            the [0][0] element
	 * @param m01
	 *            the [0][1] element
	 * @param m02
	 *            the [0][2] element
	 * @param m03
	 *            the [0][3] element
	 * @param m10
	 *            the [1][0] element
	 * @param m11
	 *            the [1][1] element
	 * @param m12
	 *            the [1][2] element
	 * @param m13
	 *            the [1][3] element
	 * @param m20
	 *            the [2][0] element
	 * @param m21
	 *            the [2][1] element
	 * @param m22
	 *            the [2][2] element
	 * @param m23
	 *            the [2][3] element
	 * @param m30
	 *            the [3][0] element
	 * @param m31
	 *            the [3][1] element
	 * @param m32
	 *            the [3][2] element
	 * @param m33
	 *            the [3][3] element
	 */
	public Matrix4f(double m00, double m01, double m02, double m03, double m10, double m11, double m12, double m13, double m20, double m21, double m22, double m23, double m30, double m31, double m32, double m33) {
		this.m00 = m00;
		this.m01 = m01;
		this.m02 = m02;
		this.m03 = m03;

		this.m10 = m10;
		this.m11 = m11;
		this.m12 = m12;
		this.m13 = m13;

		this.m20 = m20;
		this.m21 = m21;
		this.m22 = m22;
		this.m23 = m23;

		this.m30 = m30;
		this.m31 = m31;
		this.m32 = m32;
		this.m33 = m33;
	}

	/**
	 * Constructs and initializes a Matrix4f from the specified sixteen- element
	 * array.
	 * 
	 * @param v
	 *            the array of length 16 containing in order
	 */
	public Matrix4f(double[] v) {
		this.m00 = v[0];
		this.m01 = v[1];
		this.m02 = v[2];
		this.m03 = v[3];

		this.m10 = v[4];
		this.m11 = v[5];
		this.m12 = v[6];
		this.m13 = v[7];

		this.m20 = v[8];
		this.m21 = v[9];
		this.m22 = v[10];
		this.m23 = v[11];

		this.m30 = v[12];
		this.m31 = v[13];
		this.m32 = v[14];
		this.m33 = v[15];
	}

	/**
	 * Constructs a new matrix with the same values as the Matrix4f parameter.
	 * 
	 * @param m1
	 *            the source matrix
	 */
	public Matrix4f(Matrix4f m1) {
		this.m00 = m1.m00;
		this.m01 = m1.m01;
		this.m02 = m1.m02;
		this.m03 = m1.m03;

		this.m10 = m1.m10;
		this.m11 = m1.m11;
		this.m12 = m1.m12;
		this.m13 = m1.m13;

		this.m20 = m1.m20;
		this.m21 = m1.m21;
		this.m22 = m1.m22;
		this.m23 = m1.m23;

		this.m30 = m1.m30;
		this.m31 = m1.m31;
		this.m32 = m1.m32;
		this.m33 = m1.m33;
	}

	/**
	 * Constructs and initializes a Matrix4f to all zeros.
	 */
	public Matrix4f() {
		this.m00 = 0f;
		this.m01 = 0f;
		this.m02 = 0f;
		this.m03 = 0f;

		this.m10 = 0f;
		this.m11 = 0f;
		this.m12 = 0f;
		this.m13 = 0f;

		this.m20 = 0f;
		this.m21 = 0f;
		this.m22 = 0f;
		this.m23 = 0f;

		this.m30 = 0f;
		this.m31 = 0f;
		this.m32 = 0f;
		this.m33 = 0f;
	}

	/**
	 * Returns a string that contains the values of this Matrix4f.
	 * 
	 * @return the String representation
	 */
	@Override
	public String toString() {
		return this.m00 + ", " //$NON-NLS-1$
		+ this.m01 + ", " //$NON-NLS-1$
		+ this.m02 + ", " //$NON-NLS-1$
		+ this.m03 + "\n" //$NON-NLS-1$
		+ this.m10 + ", " //$NON-NLS-1$
		+ this.m11 + ", " //$NON-NLS-1$
		+ this.m12 + ", " //$NON-NLS-1$
		+ this.m13 + "\n" //$NON-NLS-1$
		+ this.m20 + ", " //$NON-NLS-1$
		+ this.m21 + ", " //$NON-NLS-1$
		+ this.m22 + ", " //$NON-NLS-1$
		+ this.m23 + "\n" //$NON-NLS-1$
		+ this.m30 + ", " //$NON-NLS-1$
		+ this.m31 + ", " //$NON-NLS-1$
		+ this.m32 + ", " //$NON-NLS-1$
		+ this.m33 + "\n"; //$NON-NLS-1$
	}

	/**
	 * Sets this Matrix4f to identity.
	 */
	public final void setIdentity() {
		this.m00 = 1f;
		this.m01 = 0f;
		this.m02 = 0f;
		this.m03 = 0f;

		this.m10 = 0f;
		this.m11 = 1f;
		this.m12 = 0f;
		this.m13 = 0f;

		this.m20 = 0f;
		this.m21 = 0f;
		this.m22 = 1f;
		this.m23 = 0f;

		this.m30 = 0f;
		this.m31 = 0f;
		this.m32 = 0f;
		this.m33 = 1f;
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
	public final void setElement(int row, int column, double value) {
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
			case 3:
				this.m03 = value;
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
			case 3:
				this.m13 = value;
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
			case 3:
				this.m23 = value;
				break;
			default:
				throw new ArrayIndexOutOfBoundsException();
			}
			break;

		case 3:
			switch (column) {
			case 0:
				this.m30 = value;
				break;
			case 1:
				this.m31 = value;
				break;
			case 2:
				this.m32 = value;
				break;
			case 3:
				this.m33 = value;
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
	public final double getElement(int row, int column) {
		switch (row) {
		case 0:
			switch (column) {
			case 0:
				return (this.m00);
			case 1:
				return (this.m01);
			case 2:
				return (this.m02);
			case 3:
				return (this.m03);
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
			case 3:
				return (this.m13);
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
			case 3:
				return (this.m23);
			default:
				break;
			}
			break;

		case 3:
			switch (column) {
			case 0:
				return (this.m30);
			case 1:
				return (this.m31);
			case 2:
				return (this.m32);
			case 3:
				return (this.m33);
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
	 * Copies the matrix values in the specified row into the array parameter.
	 * 
	 * @param row
	 *            the matrix row
	 * @param v
	 *            the array into which the matrix row values will be copied
	 */
	public final void getRow(int row, double v[]) {
		if (row == 0) {
			v[0] = this.m00;
			v[1] = this.m01;
			v[2] = this.m02;
			v[3] = this.m03;
		} else if (row == 1) {
			v[0] = this.m10;
			v[1] = this.m11;
			v[2] = this.m12;
			v[3] = this.m13;
		} else if (row == 2) {
			v[0] = this.m20;
			v[1] = this.m21;
			v[2] = this.m22;
			v[3] = this.m23;
		} else if (row == 3) {
			v[0] = this.m30;
			v[1] = this.m31;
			v[2] = this.m32;
			v[3] = this.m33;
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
	public final void getColumn(int column, double v[]) {
		if (column == 0) {
			v[0] = this.m00;
			v[1] = this.m10;
			v[2] = this.m20;
			v[3] = this.m30;
		} else if (column == 1) {
			v[0] = this.m01;
			v[1] = this.m11;
			v[2] = this.m21;
			v[3] = this.m31;
		} else if (column == 2) {
			v[0] = this.m02;
			v[1] = this.m12;
			v[2] = this.m22;
			v[3] = this.m32;
		} else if (column == 3) {
			v[0] = this.m03;
			v[1] = this.m13;
			v[2] = this.m23;
			v[3] = this.m33;
		} else {
			throw new ArrayIndexOutOfBoundsException();
		}

	}

	/**
	 * Sets the specified row of this Matrix4f to the 4 values provided.
	 * 
	 * @param row
	 *            the row number to be modified (zero indexed)
	 * @param a
	 *            the first column element
	 * @param b
	 *            the second column element
	 * @param c
	 *            the third column element
	 * @param d
	 *            the fourth column element
	 */
	public final void setRow(int row, double a, double b, double c, double d) {
		switch (row) {
		case 0:
			this.m00 = a;
			this.m01 = b;
			this.m02 = c;
			this.m03 = d;
			break;

		case 1:
			this.m10 = a;
			this.m11 = b;
			this.m12 = c;
			this.m13 = d;
			break;

		case 2:
			this.m20 = a;
			this.m21 = b;
			this.m22 = c;
			this.m23 = d;
			break;

		case 3:
			this.m30 = a;
			this.m31 = b;
			this.m32 = c;
			this.m33 = d;
			break;

		default:
			throw new ArrayIndexOutOfBoundsException();
		}
	}

	/**
	 * Sets the specified row of this Matrix4f to the three values provided.
	 * 
	 * @param row
	 *            the row number to be modified (zero indexed)
	 * @param v
	 *            the replacement row
	 */
	public final void setRow(int row, double v[]) {
		switch (row) {
		case 0:
			this.m00 = v[0];
			this.m01 = v[1];
			this.m02 = v[2];
			this.m03 = v[3];
			break;

		case 1:
			this.m10 = v[0];
			this.m11 = v[1];
			this.m12 = v[2];
			this.m13 = v[3];
			break;

		case 2:
			this.m20 = v[0];
			this.m21 = v[1];
			this.m22 = v[2];
			this.m23 = v[3];
			break;

		case 3:
			this.m30 = v[0];
			this.m31 = v[1];
			this.m32 = v[2];
			this.m33 = v[3];
			break;

		default:
			throw new ArrayIndexOutOfBoundsException();
		}
	}

	/**
	 * Sets the specified column of this Matrix4f to the three values provided.
	 * 
	 * @param column
	 *            the column number to be modified (zero indexed)
	 * @param a
	 *            the first row element
	 * @param b
	 *            the second row element
	 * @param c
	 *            the third row element
	 * @param d
	 *            the fourth row element
	 */
	public final void setColumn(int column, double a, double b, double c, double d) {
		switch (column) {
		case 0:
			this.m00 = a;
			this.m10 = b;
			this.m20 = c;
			this.m30 = d;
			break;

		case 1:
			this.m01 = a;
			this.m11 = b;
			this.m21 = c;
			this.m31 = d;
			break;

		case 2:
			this.m02 = a;
			this.m12 = b;
			this.m22 = c;
			this.m32 = d;
			break;

		case 3:
			this.m03 = a;
			this.m13 = b;
			this.m23 = c;
			this.m33 = d;
			break;

		default:
			throw new ArrayIndexOutOfBoundsException();
		}
	}

	/**
	 * Sets the specified column of this Matrix4f to the three values provided.
	 * 
	 * @param column
	 *            the column number to be modified (zero indexed)
	 * @param v
	 *            the replacement column
	 */
	public final void setColumn(int column, double v[]) {
		switch (column) {
		case 0:
			this.m00 = v[0];
			this.m10 = v[1];
			this.m20 = v[2];
			this.m30 = v[3];
			break;

		case 1:
			this.m01 = v[0];
			this.m11 = v[1];
			this.m21 = v[2];
			this.m31 = v[3];
			break;

		case 2:
			this.m02 = v[0];
			this.m12 = v[1];
			this.m22 = v[2];
			this.m32 = v[3];
			break;

		case 3:
			this.m03 = v[0];
			this.m13 = v[1];
			this.m23 = v[2];
			this.m33 = v[3];
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
	public final void add(double scalar) {
		this.m00 += scalar;
		this.m01 += scalar;
		this.m02 += scalar;
		this.m03 += scalar;

		this.m10 += scalar;
		this.m11 += scalar;
		this.m12 += scalar;
		this.m13 += scalar;

		this.m20 += scalar;
		this.m21 += scalar;
		this.m22 += scalar;
		this.m23 += scalar;

		this.m30 += scalar;
		this.m31 += scalar;
		this.m32 += scalar;
		this.m33 += scalar;
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
	public final void add(double scalar, Matrix4f m1) {
		this.m00 = m1.m00 + scalar;
		this.m01 = m1.m01 + scalar;
		this.m02 = m1.m02 + scalar;
		this.m03 = m1.m03 + scalar;

		this.m10 = m1.m10 + scalar;
		this.m11 = m1.m11 + scalar;
		this.m12 = m1.m12 + scalar;
		this.m13 = m1.m12 + scalar;

		this.m20 = m1.m20 + scalar;
		this.m21 = m1.m21 + scalar;
		this.m22 = m1.m22 + scalar;
		this.m23 = m1.m23 + scalar;

		this.m30 = m1.m30 + scalar;
		this.m31 = m1.m31 + scalar;
		this.m32 = m1.m32 + scalar;
		this.m33 = m1.m33 + scalar;
	}

	/**
	 * Sets the value of this matrix to the matrix sum of matrices m1 and m2.
	 * 
	 * @param m1
	 *            the first matrix
	 * @param m2
	 *            the second matrix
	 */
	public final void add(Matrix4f m1, Matrix4f m2) {
		this.m00 = m1.m00 + m2.m00;
		this.m01 = m1.m01 + m2.m01;
		this.m02 = m1.m02 + m2.m02;
		this.m03 = m1.m03 + m2.m03;

		this.m10 = m1.m10 + m2.m10;
		this.m11 = m1.m11 + m2.m11;
		this.m12 = m1.m12 + m2.m12;
		this.m13 = m1.m13 + m2.m13;

		this.m20 = m1.m20 + m2.m20;
		this.m21 = m1.m21 + m2.m21;
		this.m22 = m1.m22 + m2.m22;
		this.m23 = m1.m23 + m2.m23;

		this.m30 = m1.m30 + m2.m30;
		this.m31 = m1.m31 + m2.m31;
		this.m32 = m1.m32 + m2.m32;
		this.m33 = m1.m33 + m2.m33;
	}

	/**
	 * Sets the value of this matrix to the sum of itself and matrix m1.
	 * 
	 * @param m1
	 *            the other matrix
	 */
	public final void add(Matrix4f m1) {
		this.m00 += m1.m00;
		this.m01 += m1.m01;
		this.m02 += m1.m02;
		this.m03 += m1.m03;

		this.m10 += m1.m10;
		this.m11 += m1.m11;
		this.m12 += m1.m12;
		this.m13 += m1.m13;

		this.m20 += m1.m20;
		this.m21 += m1.m21;
		this.m22 += m1.m22;
		this.m23 += m1.m23;

		this.m30 += m1.m30;
		this.m31 += m1.m31;
		this.m32 += m1.m32;
		this.m33 += m1.m33;
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
	public final void sub(Matrix4f m1, Matrix4f m2) {
		this.m00 = m1.m00 - m2.m00;
		this.m01 = m1.m01 - m2.m01;
		this.m02 = m1.m02 - m2.m02;
		this.m03 = m1.m03 - m2.m03;

		this.m10 = m1.m10 - m2.m10;
		this.m11 = m1.m11 - m2.m11;
		this.m12 = m1.m12 - m2.m12;
		this.m13 = m1.m13 - m2.m13;

		this.m20 = m1.m20 - m2.m20;
		this.m21 = m1.m21 - m2.m21;
		this.m22 = m1.m22 - m2.m22;
		this.m23 = m1.m23 - m2.m23;

		this.m30 = m1.m30 - m2.m30;
		this.m31 = m1.m31 - m2.m31;
		this.m32 = m1.m32 - m2.m32;
		this.m33 = m1.m33 - m2.m33;
	}

	/**
	 * Sets the value of this matrix to the matrix difference of itself and
	 * matrix m1 (this = this - m1).
	 * 
	 * @param m1
	 *            the other matrix
	 */
	public final void sub(Matrix4f m1) {
		this.m00 -= m1.m00;
		this.m01 -= m1.m01;
		this.m02 -= m1.m02;
		this.m03 -= m1.m03;

		this.m10 -= m1.m10;
		this.m11 -= m1.m11;
		this.m12 -= m1.m12;
		this.m13 -= m1.m13;

		this.m20 -= m1.m20;
		this.m21 -= m1.m21;
		this.m22 -= m1.m22;
		this.m23 -= m1.m23;

		this.m30 -= m1.m30;
		this.m31 -= m1.m31;
		this.m32 -= m1.m32;
		this.m33 -= m1.m33;
	}

	/**
	 * Sets the value of this matrix to its transpose.
	 */
	public final void transpose() {
		double temp;

		temp = this.m10;
		this.m10 = this.m01;
		this.m01 = temp;

		temp = this.m20;
		this.m20 = this.m02;
		this.m02 = temp;

		temp = this.m30;
		this.m30 = this.m03;
		this.m03 = temp;

		temp = this.m12;
		this.m12 = this.m21;
		this.m21 = temp;

		temp = this.m13;
		this.m13 = this.m31;
		this.m31 = temp;

		temp = this.m23;
		this.m23 = this.m32;
		this.m32 = temp;
	}

	/**
	 * Sets the value of this matrix to the transpose of the argument matrix.
	 * 
	 * @param m1
	 *            the matrix to be transposed
	 */
	public final void transpose(Matrix4f m1) {
		if (this != m1) {
			this.m00 = m1.m00;
			this.m01 = m1.m10;
			this.m02 = m1.m20;
			this.m03 = m1.m30;

			this.m10 = m1.m01;
			this.m11 = m1.m11;
			this.m12 = m1.m21;
			this.m13 = m1.m31;

			this.m20 = m1.m02;
			this.m21 = m1.m12;
			this.m22 = m1.m22;
			this.m23 = m1.m32;

			this.m30 = m1.m03;
			this.m31 = m1.m13;
			this.m32 = m1.m23;
			this.m33 = m1.m33;
		}
		else {
			this.transpose();
		}
	}

	/**
	 * Sets the value of this matrix to the double value of the Matrix3f
	 * argument.
	 * 
	 * @param m1
	 *            the Matrix4f to be converted to double
	 */
	public final void set(Matrix4f m1) {
		this.m00 = m1.m00;
		this.m01 = m1.m01;
		this.m02 = m1.m02;
		this.m03 = m1.m03;

		this.m10 = m1.m10;
		this.m11 = m1.m11;
		this.m12 = m1.m12;
		this.m13 = m1.m13;

		this.m20 = m1.m20;
		this.m21 = m1.m21;
		this.m22 = m1.m22;
		this.m23 = m1.m23;

		this.m30 = m1.m30;
		this.m31 = m1.m31;
		this.m32 = m1.m32;
		this.m33 = m1.m33;
	}

	/**
	 * Sets the values in this Matrix4f equal to the row-major array parameter
	 * (ie, the first four elements of the array will be copied into the first
	 * row of this matrix, etc.).
	 * 
	 * @param m
	 *            the double precision array of length 16
	 */
	public final void set(double[] m) {
		this.m00 = m[0];
		this.m01 = m[1];
		this.m02 = m[2];
		this.m03 = m[3];

		this.m10 = m[4];
		this.m11 = m[5];
		this.m12 = m[6];
		this.m13 = m[7];

		this.m20 = m[8];
		this.m21 = m[9];
		this.m22 = m[10];
		this.m23 = m[11];

		this.m30 = m[12];
		this.m31 = m[13];
		this.m32 = m[14];
		this.m33 = m[15];
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
	 * @param m03
	 *            the [0][3] element
	 * @param m10
	 *            the [1][0] element
	 * @param m11
	 *            the [1][1] element
	 * @param m12
	 *            the [1][2] element
	 * @param m13
	 *            the [1][3] element
	 * @param m20
	 *            the [2][0] element
	 * @param m21
	 *            the [2][1] element
	 * @param m22
	 *            the [2][2] element
	 * @param m23
	 *            the [2][3] element
	 * @param m30
	 *            the [3][0] element
	 * @param m31
	 *            the [3][1] element
	 * @param m32
	 *            the [3][2] element
	 * @param m33
	 *            the [3][3] element
	 */
	public void set(double m00, double m01, double m02, double m03, double m10, double m11, double m12, double m13, double m20, double m21, double m22, double m23, double m30, double m31, double m32, double m33) {
		this.m00 = m00;
		this.m01 = m01;
		this.m02 = m02;
		this.m03 = m03;

		this.m10 = m10;
		this.m11 = m11;
		this.m12 = m12;
		this.m13 = m13;

		this.m20 = m20;
		this.m21 = m21;
		this.m22 = m22;
		this.m23 = m23;

		this.m30 = m30;
		this.m31 = m31;
		this.m32 = m32;
		this.m33 = m33;
	}

	/**
	 * Computes the determinant of this matrix.
	 * 
	 * @return the determinant of the matrix
	 */
	public final double determinant() {
		double det1 = this.m22 * this.m33 - this.m23 * this.m32;
		double det2 = this.m12 * this.m33 - this.m13 * this.m32;
		double det3 = this.m12 * this.m23 - this.m13 * this.m22;
		double det4 = this.m02 * this.m33 - this.m03 * this.m32;
		double det5 = this.m02 * this.m23 - this.m03 * this.m22;
		double det6 = this.m02 * this.m13 - this.m03 * this.m12;
		return
				  this.m00 * (
						  this.m11 * det1 +
						  this.m21 * det2 +
						  this.m31 * det3
				  ) +
				  this.m10 * (
						  this.m01 * det1 +
						  this.m21 * det4 +
						  this.m31 * det5
				  ) +
				  this.m20 * (
						  this.m01 * det2 +
						  this.m11 * det4 +
						  this.m31 * det6
				  ) +
				  this.m30 * (
						  this.m01 * det3 +
						  this.m11 * det5 +
						  this.m21 * det6
				  );
	}

	/**
	 * Multiplies each element of this matrix by a scalar.
	 * 
	 * @param scalar
	 *            The scalar multiplier.
	 */
	public final void mul(double scalar) {
		this.m00 *= scalar;
		this.m01 *= scalar;
		this.m02 *= scalar;
		this.m03 *= scalar;

		this.m10 *= scalar;
		this.m11 *= scalar;
		this.m12 *= scalar;
		this.m13 *= scalar;

		this.m20 *= scalar;
		this.m21 *= scalar;
		this.m22 *= scalar;
		this.m23 *= scalar;

		this.m30 *= scalar;
		this.m31 *= scalar;
		this.m32 *= scalar;
		this.m33 *= scalar;
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
	public final void mul(double scalar, Matrix4f m1) {
		this.m00 = scalar * m1.m00;
		this.m01 = scalar * m1.m01;
		this.m02 = scalar * m1.m02;
		this.m03 = scalar * m1.m03;

		this.m10 = scalar * m1.m10;
		this.m11 = scalar * m1.m11;
		this.m12 = scalar * m1.m12;
		this.m13 = scalar * m1.m13;

		this.m20 = scalar * m1.m20;
		this.m21 = scalar * m1.m21;
		this.m22 = scalar * m1.m22;
		this.m23 = scalar * m1.m23;

		this.m30 = scalar * m1.m30;
		this.m31 = scalar * m1.m31;
		this.m32 = scalar * m1.m32;
		this.m33 = scalar * m1.m33;
	}

	/**
	 * Sets the value of this matrix to the result of multiplying itself with
	 * matrix m1.
	 * 
	 * @param m1
	 *            the other matrix
	 */
	public final void mul(Matrix4f m1) {
		double _m00, _m01, _m02, _m03, _m10, _m11, _m12, _m13, _m20, _m21, _m22, _m23, _m30, _m31, _m32, _m33;

		_m00 = this.m00 * m1.m00 + this.m01 * m1.m10 + this.m02 * m1.m20 + this.m03 * m1.m30;
		_m01 = this.m00 * m1.m01 + this.m01 * m1.m11 + this.m02 * m1.m21 + this.m03 * m1.m31;
		_m02 = this.m00 * m1.m02 + this.m01 * m1.m12 + this.m02 * m1.m22 + this.m03 * m1.m32;
		_m03 = this.m00 * m1.m03 + this.m01 * m1.m13 + this.m02 * m1.m23 + this.m03 * m1.m33;

		_m10 = this.m10 * m1.m00 + this.m11 * m1.m10 + this.m12 * m1.m20 + this.m13 * m1.m30;
		_m11 = this.m10 * m1.m01 + this.m11 * m1.m11 + this.m12 * m1.m21 + this.m13 * m1.m31;
		_m12 = this.m10 * m1.m02 + this.m11 * m1.m12 + this.m12 * m1.m22 + this.m13 * m1.m32;
		_m13 = this.m10 * m1.m03 + this.m11 * m1.m13 + this.m12 * m1.m23 + this.m13 * m1.m33;

		_m20 = this.m20 * m1.m00 + this.m21 * m1.m10 + this.m22 * m1.m20 + this.m23 * m1.m30;
		_m21 = this.m20 * m1.m01 + this.m21 * m1.m11 + this.m22 * m1.m21 + this.m23 * m1.m31;
		_m22 = this.m20 * m1.m02 + this.m21 * m1.m12 + this.m22 * m1.m22 + this.m23 * m1.m32;
		_m23 = this.m20 * m1.m03 + this.m21 * m1.m13 + this.m22 * m1.m23 + this.m23 * m1.m33;

		_m30 = this.m30 * m1.m00 + this.m31 * m1.m10 + this.m32 * m1.m20 + this.m33 * m1.m30;
		_m31 = this.m30 * m1.m01 + this.m31 * m1.m11 + this.m32 * m1.m21 + this.m33 * m1.m31;
		_m32 = this.m30 * m1.m02 + this.m31 * m1.m12 + this.m32 * m1.m22 + this.m33 * m1.m32;
		_m33 = this.m30 * m1.m03 + this.m31 * m1.m13 + this.m32 * m1.m23 + this.m33 * m1.m33;

		this.m00 = _m00;
		this.m01 = _m01;
		this.m02 = _m02;
		this.m03 = _m03;
		this.m10 = _m10;
		this.m11 = _m11;
		this.m12 = _m12;
		this.m13 = _m13;
		this.m20 = _m20;
		this.m21 = _m21;
		this.m22 = _m22;
		this.m23 = _m23;
		this.m30 = _m30;
		this.m31 = _m31;
		this.m32 = _m32;
		this.m33 = _m33;
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
	public final void mul(Matrix4f m1, Matrix4f m2) {
		if (this != m1 && this != m2) {
			this.m00 = m1.m00 * m2.m00 + m1.m01 * m2.m10 + m1.m02 * m2.m20 + m1.m03 * m2.m30;
			this.m01 = m1.m00 * m2.m01 + m1.m01 * m2.m11 + m1.m02 * m2.m21 + m1.m03 * m2.m31;
			this.m02 = m1.m00 * m2.m02 + m1.m01 * m2.m12 + m1.m02 * m2.m22 + m1.m03 * m2.m32;
			this.m03 = m1.m00 * m2.m03 + m1.m01 * m2.m13 + m1.m02 * m2.m23 + m1.m03 * m2.m33;

			this.m10 = m1.m10 * m2.m00 + m1.m11 * m2.m10 + m1.m12 * m2.m20 + m1.m13 * m2.m30;
			this.m11 = m1.m10 * m2.m01 + m1.m11 * m2.m11 + m1.m12 * m2.m21 + m1.m13 * m2.m31;
			this.m12 = m1.m10 * m2.m02 + m1.m11 * m2.m12 + m1.m12 * m2.m22 + m1.m13 * m2.m32;
			this.m13 = m1.m10 * m2.m03 + m1.m11 * m2.m13 + m1.m12 * m2.m23 + m1.m13 * m2.m33;

			this.m20 = m1.m20 * m2.m00 + m1.m21 * m2.m10 + m1.m22 * m2.m20 + m1.m23 * m2.m30;
			this.m21 = m1.m20 * m2.m01 + m1.m21 * m2.m11 + m1.m22 * m2.m21 + m1.m23 * m2.m31;
			this.m22 = m1.m20 * m2.m02 + m1.m21 * m2.m12 + m1.m22 * m2.m22 + m1.m23 * m2.m32;
			this.m23 = m1.m20 * m2.m03 + m1.m21 * m2.m13 + m1.m22 * m2.m23 + m1.m23 * m2.m33;

			this.m30 = m1.m30 * m2.m00 + m1.m31 * m2.m10 + m1.m32 * m2.m20 + m1.m33 * m2.m30;
			this.m31 = m1.m30 * m2.m01 + m1.m31 * m2.m11 + m1.m32 * m2.m21 + m1.m33 * m2.m31;
			this.m32 = m1.m30 * m2.m02 + m1.m31 * m2.m12 + m1.m32 * m2.m22 + m1.m33 * m2.m32;
			this.m33 = m1.m30 * m2.m03 + m1.m31 * m2.m13 + m1.m32 * m2.m23 + m1.m33 * m2.m33;
		} else {
			double _m00, _m01, _m02, _m03, _m10, _m11, _m12, _m13, _m20, _m21, _m22, _m23, _m30, _m31, _m32, _m33; // vars for temp
																// result matrix

			_m00 = m1.m00 * m2.m00 + m1.m01 * m2.m10 + m1.m02 * m2.m20 + m1.m03 * m2.m30;
			_m01 = m1.m00 * m2.m01 + m1.m01 * m2.m11 + m1.m02 * m2.m21 + m1.m03 * m2.m31;
			_m02 = m1.m00 * m2.m02 + m1.m01 * m2.m12 + m1.m02 * m2.m22 + m1.m03 * m2.m32;
			_m03 = m1.m00 * m2.m03 + m1.m01 * m2.m13 + m1.m02 * m2.m23 + m1.m03 * m2.m33;

			_m10 = m1.m10 * m2.m00 + m1.m11 * m2.m10 + m1.m12 * m2.m20 + m1.m13 * m2.m30;
			_m11 = m1.m10 * m2.m01 + m1.m11 * m2.m11 + m1.m12 * m2.m21 + m1.m13 * m2.m31;
			_m12 = m1.m10 * m2.m02 + m1.m11 * m2.m12 + m1.m12 * m2.m22 + m1.m13 * m2.m32;
			_m13 = m1.m10 * m2.m03 + m1.m11 * m2.m13 + m1.m12 * m2.m23 + m1.m13 * m2.m33;

			_m20 = m1.m20 * m2.m00 + m1.m21 * m2.m10 + m1.m22 * m2.m20 + m1.m23 * m2.m30;
			_m21 = m1.m20 * m2.m01 + m1.m21 * m2.m11 + m1.m22 * m2.m21 + m1.m23 * m2.m31;
			_m22 = m1.m20 * m2.m02 + m1.m21 * m2.m12 + m1.m22 * m2.m22 + m1.m23 * m2.m32;
			_m23 = m1.m20 * m2.m03 + m1.m21 * m2.m13 + m1.m22 * m2.m23 + m1.m23 * m2.m33;

			_m30 = m1.m30 * m2.m00 + m1.m31 * m2.m10 + m1.m32 * m2.m20 + m1.m33 * m2.m30;
			_m31 = m1.m30 * m2.m01 + m1.m31 * m2.m11 + m1.m32 * m2.m21 + m1.m33 * m2.m31;
			_m32 = m1.m30 * m2.m02 + m1.m31 * m2.m12 + m1.m32 * m2.m22 + m1.m33 * m2.m32;
			_m33 = m1.m30 * m2.m03 + m1.m31 * m2.m13 + m1.m32 * m2.m23 + m1.m33 * m2.m33;

			this.m00 = _m00;
			this.m01 = _m01;
			this.m02 = _m02;
			this.m03 = _m03;
			this.m10 = _m10;
			this.m11 = _m11;
			this.m12 = _m12;
			this.m13 = _m13;
			this.m20 = _m20;
			this.m21 = _m21;
			this.m22 = _m22;
			this.m23 = _m23;
			this.m30 = _m30;
			this.m31 = _m31;
			this.m32 = _m32;
			this.m33 = _m33;
		}
	}

	/**
	 * Returns true if all of the data members of Matrix4f m1 are equal to the
	 * corresponding data members in this Matrix4f.
	 * 
	 * @param m1
	 *            the matrix with which the comparison is made
	 * @return true or false
	 */
	public boolean equals(Matrix4f m1) {
		try {
			return (this.m00 == m1.m00 && this.m01 == m1.m01
					&& this.m02 == m1.m02 && this.m03 == m1.m03
					&& this.m10 == m1.m10 && this.m11 == m1.m11
					&& this.m12 == m1.m12 && this.m13 == m1.m13
					&& this.m20 == m1.m20 && this.m21 == m1.m21
					&& this.m22 == m1.m22 && this.m23 == m1.m23
					&& this.m30 == m1.m30 && this.m31 == m1.m31
					&& this.m32 == m1.m32 && this.m33 == m1.m33);
		} catch (NullPointerException e2) {
			return false;
		}

	}

	/**
	 * Returns true if the Object t1 is of type Matrix4f and all of the data
	 * members of t1 are equal to the corresponding data members in this
	 * Matrix4f.
	 * 
	 * @param t1
	 *            the matrix with which the comparison is made
	 * @return true or false
	 */
	@Override
	public boolean equals(Object t1) {
		try {
			Matrix4f m2 = (Matrix4f) t1;
			return (this.m00 == m2.m00 && this.m01 == m2.m01
					&& this.m02 == m2.m02 && this.m03 == m2.m03
					&& this.m10 == m2.m10 && this.m11 == m2.m11
					&& this.m12 == m2.m12 && this.m13 == m2.m13
					&& this.m20 == m2.m20 && this.m21 == m2.m21
					&& this.m22 == m2.m22 && this.m23 == m2.m23
					&& this.m30 == m2.m30 && this.m31 == m2.m31
					&& this.m32 == m2.m32 && this.m33 == m2.m33);
		} catch (ClassCastException e1) {
			return false;
		} catch (NullPointerException e2) {
			return false;
		}

	}

	/**
	 * Returns true if the L-infinite distance between this matrix and matrix m1
	 * is less than or equal to the epsilon parameter, otherwise returns false.
	 * The L-infinite distance is equal to MAX[i=0,1,2,3 ; j=0,1,2,3 ;
	 * abs(this.m(i,j) - m1.m(i,j)]
	 * 
	 * @param m1
	 *            the matrix to be compared to this matrix
	 * @param epsilon
	 *            the threshold value
	 * @return <code>true</code> if this matrix is equals to the specified matrix at epsilon.
	 */
	public boolean epsilonEquals(Matrix4f m1, double epsilon) {
		double diff;

		diff = this.m00 - m1.m00;
		if ((diff < 0 ? -diff : diff) > epsilon)
			return false;

		diff = this.m01 - m1.m01;
		if ((diff < 0 ? -diff : diff) > epsilon)
			return false;

		diff = this.m02 - m1.m02;
		if ((diff < 0 ? -diff : diff) > epsilon)
			return false;

		diff = this.m03 - m1.m03;
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

		diff = this.m13 - m1.m13;
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

		diff = this.m23 - m1.m23;
		if ((diff < 0 ? -diff : diff) > epsilon)
			return false;

		diff = this.m30 - m1.m30;
		if ((diff < 0 ? -diff : diff) > epsilon)
			return false;

		diff = this.m31 - m1.m31;
		if ((diff < 0 ? -diff : diff) > epsilon)
			return false;

		diff = this.m32 - m1.m32;
		if ((diff < 0 ? -diff : diff) > epsilon)
			return false;

		diff = this.m33 - m1.m33;
		if ((diff < 0 ? -diff : diff) > epsilon)
			return false;

		return true;
	}

	/**
	 * Returns a hash code value based on the data values in this object. Two
	 * different Matrix4f objects with identical data values (i.e.,
	 * Matrix4f.equals returns true) will return the same hash code value. Two
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
		bits = 31L * bits + doubleToLongBits(this.m03);
		bits = 31L * bits + doubleToLongBits(this.m10);
		bits = 31L * bits + doubleToLongBits(this.m11);
		bits = 31L * bits + doubleToLongBits(this.m12);
		bits = 31L * bits + doubleToLongBits(this.m13);
		bits = 31L * bits + doubleToLongBits(this.m20);
		bits = 31L * bits + doubleToLongBits(this.m21);
		bits = 31L * bits + doubleToLongBits(this.m22);
		bits = 31L * bits + doubleToLongBits(this.m23);
		bits = 31L * bits + doubleToLongBits(this.m30);
		bits = 31L * bits + doubleToLongBits(this.m31);
		bits = 31L * bits + doubleToLongBits(this.m32);
		bits = 31L * bits + doubleToLongBits(this.m33);
		return (int) (bits ^ (bits >> 32));
	}

	private static long doubleToLongBits(double d) {
		// Check for +0 or -0
		if (d == 0.) {
			return 0;
		}
		return Double.doubleToLongBits(d);
	}
	
	/**
	 * Sets this matrix to all zeros.
	 */
	public final void setZero() {
		this.m00 = 0f;
		this.m01 = 0f;
		this.m02 = 0f;
		this.m03 = 0f;

		this.m10 = 0f;
		this.m11 = 0f;
		this.m12 = 0f;
		this.m13 = 0f;

		this.m20 = 0f;
		this.m21 = 0f;
		this.m22 = 0f;
		this.m23 = 0f;

		this.m30 = 0f;
		this.m31 = 0f;
		this.m32 = 0f;
		this.m33 = 0f;
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
	 * @param m33
	 *            the fourth element of the diagonal
	 */
	public final void setDiagonal(double m00, double m11, double m22, double m33) {
		this.m00 = m00;
		this.m01 = 0f;
		this.m02 = 0f;
		this.m03 = 0f;
		this.m10 = 0f;
		this.m11 = m11;
		this.m12 = 0f;
		this.m13 = 0f;
		this.m20 = 0f;
		this.m21 = 0f;
		this.m22 = m22;
		this.m23 = 0f;
		this.m30 = 0f;
		this.m31 = 0f;
		this.m32 = 0f;
		this.m33 = m33;
	}

	/**
	 * Negates the value of this matrix: this = -this.
	 */
	public final void negate() {
		this.m00 = -this.m00;
		this.m01 = -this.m01;
		this.m02 = -this.m02;
		this.m03 = -this.m03;

		this.m10 = -this.m10;
		this.m11 = -this.m11;
		this.m12 = -this.m12;
		this.m13 = -this.m13;

		this.m20 = -this.m20;
		this.m21 = -this.m21;
		this.m22 = -this.m22;
		this.m23 = -this.m23;

		this.m30 = -this.m30;
		this.m31 = -this.m31;
		this.m32 = -this.m32;
		this.m33 = -this.m33;
	}

	/**
	 * Sets the value of this matrix equal to the negation of of the Matrix4f
	 * parameter.
	 * 
	 * @param m1
	 *            the source matrix
	 */
	public final void negate(Matrix4f m1) {
		this.m00 = -m1.m00;
		this.m01 = -m1.m01;
		this.m02 = -m1.m02;
		this.m03 = -m1.m03;

		this.m10 = -m1.m10;
		this.m11 = -m1.m11;
		this.m12 = -m1.m12;
		this.m13 = -m1.m13;

		this.m20 = -m1.m20;
		this.m21 = -m1.m21;
		this.m22 = -m1.m22;
		this.m23 = -m1.m23;

		this.m30 = -m1.m30;
		this.m31 = -m1.m31;
		this.m32 = -m1.m32;
		this.m33 = -m1.m33;
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
	public Matrix4f clone() {
		Matrix4f m1 = null;
		try {
			m1 = (Matrix4f) super.clone();
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
	 */
	public final double getM00() {
		return this.m00;
	}

	/**
	 * Set the first matrix element in the first row.
	 * 
	 * @param m00
	 *            The m00 to set.
	 */
	public final void setM00(double m00) {
		this.m00 = m00;
	}

	/**
	 * Get the second matrix element in the first row.
	 * 
	 * @return Returns the m01.
	 */
	public final double getM01() {
		return this.m01;
	}

	/**
	 * Set the second matrix element in the first row.
	 * 
	 * @param m01
	 *            The m01 to set.
	 */
	public final void setM01(double m01) {
		this.m01 = m01;
	}

	/**
	 * Get the third matrix element in the first row.
	 * 
	 * @return Returns the m02.
	 */
	public final double getM02() {
		return this.m02;
	}

	/**
	 * Set the third matrix element in the first row.
	 * 
	 * @param m02
	 *            The m02 to set.
	 */
	public final void setM02(double m02) {
		this.m02 = m02;
	}

	/**
	 * Get the fourth matrix element in the first row.
	 * 
	 * @return Returns the m03.
	 */
	public final double getM03() {
		return this.m03;
	}

	/**
	 * Set the fourth matrix element in the first row.
	 * 
	 * @param m03
	 *            The m03 to set.
	 */
	public final void setM03(double m03) {
		this.m03 = m03;
	}

	/**
	 * Get first matrix element in the second row.
	 * 
	 * @return Returns the m10
	 */
	public final double getM10() {
		return this.m10;
	}

	/**
	 * Set first matrix element in the second row.
	 * 
	 * @param m10
	 *            The m10 to set.
	 */
	public final void setM10(double m10) {
		this.m10 = m10;
	}

	/**
	 * Get second matrix element in the second row.
	 * 
	 * @return Returns the m11.
	 */
	public final double getM11() {
		return this.m11;
	}

	/**
	 * Set the second matrix element in the second row.
	 * 
	 * @param m11
	 *            The m11 to set.
	 */
	public final void setM11(double m11) {
		this.m11 = m11;
	}

	/**
	 * Get the third matrix element in the second row.
	 * 
	 * @return Returns the m12.
	 */
	public final double getM12() {
		return this.m12;
	}

	/**
	 * Set the third matrix element in the second row.
	 * 
	 * @param m12
	 *            The m12 to set.
	 */
	public final void setM12(double m12) {
		this.m12 = m12;
	}

	/**
	 * Get the fourth matrix element in the second row.
	 * 
	 * @return Returns the m13.
	 */
	public final double getM13() {
		return this.m13;
	}

	/**
	 * Set the fourth matrix element in the second row.
	 * 
	 * @param m13
	 *            The m13 to set.
	 */
	public final void setM13(double m13) {
		this.m13 = m13;
	}

	/**
	 * Get the first matrix element in the third row.
	 * 
	 * @return Returns the m20
	 */
	public final double getM20() {
		return this.m20;
	}

	/**
	 * Set the first matrix element in the third row.
	 * 
	 * @param m20
	 *            The m20 to set.
	 */
	public final void setM20(double m20) {
		this.m20 = m20;
	}

	/**
	 * Get the second matrix element in the third row.
	 * 
	 * @return Returns the m21.
	 */
	public final double getM21() {
		return this.m21;
	}

	/**
	 * Set the second matrix element in the third row.
	 * 
	 * @param m21
	 *            The m21 to set.
	 */
	public final void setM21(double m21) {
		this.m21 = m21;
	}

	/**
	 * Get the third matrix element in the third row .
	 * 
	 * @return Returns the m22.
	 */
	public final double getM22() {
		return this.m22;
	}

	/**
	 * Set the third matrix element in the third row.
	 * 
	 * @param m22
	 *            The m22 to set.
	 */
	public final void setM22(double m22) {
		this.m22 = m22;
	}

	/**
	 * Get the fourth matrix element in the third row .
	 * 
	 * @return Returns the m23.
	 */
	public final double getM23() {
		return this.m23;
	}

	/**
	 * Set the fourth matrix element in the third row.
	 * 
	 * @param m23
	 *            The m23 to set.
	 */
	public final void setM23(double m23) {
		this.m23 = m23;
	}

	/**
	 * Get the first matrix element in the fourth row.
	 * 
	 * @return Returns the m30
	 */
	public final double getM30() {
		return this.m30;
	}

	/**
	 * Set the first matrix element in the fourth row.
	 * 
	 * @param m30
	 *            The m30 to set.
	 */
	public final void setM30(double m30) {
		this.m30 = m30;
	}

	/**
	 * Get the second matrix element in the fourth row.
	 * 
	 * @return Returns the m31.
	 */
	public final double getM31() {
		return this.m31;
	}

	/**
	 * Set the second matrix element in the fourth row.
	 * 
	 * @param m31
	 *            The m31 to set.
	 */
	public final void setM31(double m31) {
		this.m31 = m31;
	}

	/**
	 * Get the third matrix element in the fourth row .
	 * 
	 * @return Returns the m32.
	 */
	public final double getM32() {
		return this.m32;
	}

	/**
	 * Set the third matrix element in the fourth row.
	 * 
	 * @param m32
	 *            The m32 to set.
	 */
	public final void setM32(double m32) {
		this.m32 = m32;
	}

	/**
	 * Get the fourth matrix element in the fourth row .
	 * 
	 * @return Returns the m33.
	 */
	public final double getM33() {
		return this.m33;
	}

	/**
	 * Set the fourth matrix element in the fourth row.
	 * 
	 * @param m33
	 *            The m33 to set.
	 */
	public final void setM33(double m33) {
		this.m33 = m33;
	}

	/** Replies if the matrix is symmetric.
	 * 
	 * @return <code>true</code> if the matrix is symmetric, otherwise
	 * <code>false</code>
	 */
	public boolean isSymmetric() {
		return	this.m01 == this.m10
			&&	this.m02 == this.m20
			&&	this.m03 == this.m03
			&&	this.m12 == this.m21
			&&	this.m13 == this.m31
			&&	this.m23 == this.m32;
	}
	
	/** Replies if the matrix is identity.
	 * <p>
	 * This function uses the equal-to-zero test with the error {@link MathConstants#EPSILON}.
	 * 
	 * @return <code>true</code> if the matrix is identity; <code>false</code> otherwise.
	 * @see MathUtil#isEpsilonZero(double)
	 * @see MathUtil#isEpsilonEqual(double, double)
	 */
	public boolean isIdentity() {
		return MathUtil.isEpsilonEqual(this.m00, 1f)
				&& MathUtil.isEpsilonZero(this.m01)
				&& MathUtil.isEpsilonZero(this.m02)
				&& MathUtil.isEpsilonZero(this.m03)
				&& MathUtil.isEpsilonZero(this.m10)
				&& MathUtil.isEpsilonEqual(this.m11, 1f)
				&& MathUtil.isEpsilonZero(this.m12)
				&& MathUtil.isEpsilonZero(this.m13)
				&& MathUtil.isEpsilonZero(this.m20)
				&& MathUtil.isEpsilonZero(this.m21)
				&& MathUtil.isEpsilonEqual(this.m22, 1f)
				&& MathUtil.isEpsilonZero(this.m23)
				&& MathUtil.isEpsilonZero(this.m30)
				&& MathUtil.isEpsilonZero(this.m31)
				&& MathUtil.isEpsilonZero(this.m32)
				&& MathUtil.isEpsilonEqual(this.m33, 1f);
	}

}