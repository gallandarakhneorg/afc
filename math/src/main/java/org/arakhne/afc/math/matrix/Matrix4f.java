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
import org.arakhne.afc.math.continous.object3d.Point3f;
import org.arakhne.afc.math.continous.object3d.Quaternion;
import org.arakhne.afc.math.continous.object3d.Vector3f;
import org.arakhne.afc.math.continous.object4d.AxisAngle4f;

/**
 * Is represented internally as a 4x4 floating point matrix. The mathematical
 * representation is row major, as in traditional matrix mathematics.
 * 
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class Matrix4f implements Serializable, Cloneable, MathConstants {

	private static final long serialVersionUID = 7216873052550769543L;

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
	 * The fourth matrix element in the first row.
	 */
	public float m03;

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
	 * The fourth matrix element in the second row.
	 */
	public float m13;

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
	 * The fourth matrix element in the third row.
	 */
	public float m23;

	/**
	 * The first matrix element in the fourth row.
	 */
	public float m30;

	/**
	 * The second matrix element in the fourth row.
	 */
	public float m31;

	/**
	 * The third matrix element in the fourth row.
	 */
	public float m32;

	/**
	 * The fourth matrix element in the fourth row.
	 */
	public float m33;

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
	public Matrix4f(float m00, float m01, float m02, float m03, float m10, float m11, float m12, float m13, float m20, float m21, float m22, float m23, float m30, float m31, float m32, float m33) {
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
	public Matrix4f(float[] v) {
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
	public final void getRow(int row, float v[]) {
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
	public final void getColumn(int column, float v[]) {
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
	public final void setRow(int row, float a, float b, float c, float d) {
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
	public final void setRow(int row, float v[]) {
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
	public final void setColumn(int column, float a, float b, float c, float d) {
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
	public final void setColumn(int column, float v[]) {
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
	public final void add(float scalar) {
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
	public final void add(float scalar, Matrix4f m1) {
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
		float temp;

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
	 * Sets the value of this matrix to the float value of the Matrix3f
	 * argument.
	 * 
	 * @param m1
	 *            the Matrix4f to be converted to float
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
	 *            the float precision array of length 16
	 */
	public final void set(float[] m) {
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
	public void set(float m00, float m01, float m02, float m03, float m10, float m11, float m12, float m13, float m20, float m21, float m22, float m23, float m30, float m31, float m32, float m33) {
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
	public final float determinant() {
		float det1 = this.m22 * this.m33 - this.m23 * this.m32;
		float det2 = this.m12 * this.m33 - this.m13 * this.m32;
		float det3 = this.m12 * this.m23 - this.m13 * this.m22;
		float det4 = this.m02 * this.m33 - this.m03 * this.m32;
		float det5 = this.m02 * this.m23 - this.m03 * this.m22;
		float det6 = this.m02 * this.m13 - this.m03 * this.m12;
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
	public final void mul(float scalar) {
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
	public final void mul(float scalar, Matrix4f m1) {
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
		float _m00, _m01, _m02, _m03, _m10, _m11, _m12, _m13, _m20, _m21, _m22, _m23, _m30, _m31, _m32, _m33;

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
			float _m00, _m01, _m02, _m03, _m10, _m11, _m12, _m13, _m20, _m21, _m22, _m23, _m30, _m31, _m32, _m33; // vars for temp
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
	public boolean epsilonEquals(Matrix4f m1, float epsilon) {
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
		bits = 31L * bits + floatToIntBits(this.m00);
		bits = 31L * bits + floatToIntBits(this.m01);
		bits = 31L * bits + floatToIntBits(this.m02);
		bits = 31L * bits + floatToIntBits(this.m03);
		bits = 31L * bits + floatToIntBits(this.m10);
		bits = 31L * bits + floatToIntBits(this.m11);
		bits = 31L * bits + floatToIntBits(this.m12);
		bits = 31L * bits + floatToIntBits(this.m13);
		bits = 31L * bits + floatToIntBits(this.m20);
		bits = 31L * bits + floatToIntBits(this.m21);
		bits = 31L * bits + floatToIntBits(this.m22);
		bits = 31L * bits + floatToIntBits(this.m23);
		bits = 31L * bits + floatToIntBits(this.m30);
		bits = 31L * bits + floatToIntBits(this.m31);
		bits = 31L * bits + floatToIntBits(this.m32);
		bits = 31L * bits + floatToIntBits(this.m33);
		return (int) (bits ^ (bits >> 32));
	}

	private static int floatToIntBits(float d) {
		// Check for +0 or -0
		if (d == 0f) {
			return 0;
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
	public final void setDiagonal(float m00, float m11, float m22, float m33) {
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
	public final float getM00() {
		return this.m00;
	}

	/**
	 * Set the first matrix element in the first row.
	 * 
	 * @param m00
	 *            The m00 to set.
	 */
	public final void setM00(float m00) {
		this.m00 = m00;
	}

	/**
	 * Get the second matrix element in the first row.
	 * 
	 * @return Returns the m01.
	 */
	public final float getM01() {
		return this.m01;
	}

	/**
	 * Set the second matrix element in the first row.
	 * 
	 * @param m01
	 *            The m01 to set.
	 */
	public final void setM01(float m01) {
		this.m01 = m01;
	}

	/**
	 * Get the third matrix element in the first row.
	 * 
	 * @return Returns the m02.
	 */
	public final float getM02() {
		return this.m02;
	}

	/**
	 * Set the third matrix element in the first row.
	 * 
	 * @param m02
	 *            The m02 to set.
	 */
	public final void setM02(float m02) {
		this.m02 = m02;
	}

	/**
	 * Get the fourth matrix element in the first row.
	 * 
	 * @return Returns the m03.
	 */
	public final float getM03() {
		return this.m03;
	}

	/**
	 * Set the fourth matrix element in the first row.
	 * 
	 * @param m03
	 *            The m03 to set.
	 */
	public final void setM03(float m03) {
		this.m03 = m03;
	}

	/**
	 * Get first matrix element in the second row.
	 * 
	 * @return Returns the m10
	 */
	public final float getM10() {
		return this.m10;
	}

	/**
	 * Set first matrix element in the second row.
	 * 
	 * @param m10
	 *            The m10 to set.
	 */
	public final void setM10(float m10) {
		this.m10 = m10;
	}

	/**
	 * Get second matrix element in the second row.
	 * 
	 * @return Returns the m11.
	 */
	public final float getM11() {
		return this.m11;
	}

	/**
	 * Set the second matrix element in the second row.
	 * 
	 * @param m11
	 *            The m11 to set.
	 */
	public final void setM11(float m11) {
		this.m11 = m11;
	}

	/**
	 * Get the third matrix element in the second row.
	 * 
	 * @return Returns the m12.
	 */
	public final float getM12() {
		return this.m12;
	}

	/**
	 * Set the third matrix element in the second row.
	 * 
	 * @param m12
	 *            The m12 to set.
	 */
	public final void setM12(float m12) {
		this.m12 = m12;
	}

	/**
	 * Get the fourth matrix element in the second row.
	 * 
	 * @return Returns the m13.
	 */
	public final float getM13() {
		return this.m13;
	}

	/**
	 * Set the fourth matrix element in the second row.
	 * 
	 * @param m13
	 *            The m13 to set.
	 */
	public final void setM13(float m13) {
		this.m13 = m13;
	}

	/**
	 * Get the first matrix element in the third row.
	 * 
	 * @return Returns the m20
	 */
	public final float getM20() {
		return this.m20;
	}

	/**
	 * Set the first matrix element in the third row.
	 * 
	 * @param m20
	 *            The m20 to set.
	 */
	public final void setM20(float m20) {
		this.m20 = m20;
	}

	/**
	 * Get the second matrix element in the third row.
	 * 
	 * @return Returns the m21.
	 */
	public final float getM21() {
		return this.m21;
	}

	/**
	 * Set the second matrix element in the third row.
	 * 
	 * @param m21
	 *            The m21 to set.
	 */
	public final void setM21(float m21) {
		this.m21 = m21;
	}

	/**
	 * Get the third matrix element in the third row .
	 * 
	 * @return Returns the m22.
	 */
	public final float getM22() {
		return this.m22;
	}

	/**
	 * Set the third matrix element in the third row.
	 * 
	 * @param m22
	 *            The m22 to set.
	 */
	public final void setM22(float m22) {
		this.m22 = m22;
	}

	/**
	 * Get the fourth matrix element in the third row .
	 * 
	 * @return Returns the m23.
	 */
	public final float getM23() {
		return this.m23;
	}

	/**
	 * Set the fourth matrix element in the third row.
	 * 
	 * @param m23
	 *            The m23 to set.
	 */
	public final void setM23(float m23) {
		this.m23 = m23;
	}

	/**
	 * Get the first matrix element in the fourth row.
	 * 
	 * @return Returns the m30
	 */
	public final float getM30() {
		return this.m30;
	}

	/**
	 * Set the first matrix element in the fourth row.
	 * 
	 * @param m30
	 *            The m30 to set.
	 */
	public final void setM30(float m30) {
		this.m30 = m30;
	}

	/**
	 * Get the second matrix element in the fourth row.
	 * 
	 * @return Returns the m31.
	 */
	public final float getM31() {
		return this.m31;
	}

	/**
	 * Set the second matrix element in the fourth row.
	 * 
	 * @param m31
	 *            The m31 to set.
	 */
	public final void setM31(float m31) {
		this.m31 = m31;
	}

	/**
	 * Get the third matrix element in the fourth row .
	 * 
	 * @return Returns the m32.
	 */
	public final float getM32() {
		return this.m32;
	}

	/**
	 * Set the third matrix element in the fourth row.
	 * 
	 * @param m32
	 *            The m32 to set.
	 */
	public final void setM32(float m32) {
		this.m32 = m32;
	}

	/**
	 * Get the fourth matrix element in the fourth row .
	 * 
	 * @return Returns the m33.
	 */
	public final float getM33() {
		return this.m33;
	}

	/**
	 * Set the fourth matrix element in the fourth row.
	 * 
	 * @param m33
	 *            The m33 to set.
	 */
	public final void setM33(float m33) {
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
	 * @see MathUtil#isEpsilonZero(float)
	 * @see MathUtil#isEpsilonEqual(float, float)
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


	 /**
	 * Transforms the normal parameter by this transform and places the value
	 * back into normal.  The fourth element of the normal is assumed to be zero.
	 * @param normal   the input normal to be transformed.
	  */
	public void transform(Vector3f normal) {
      float x, y;

      x =  this.m00*normal.getX() + this.m01*normal.getY() + this.m02*normal.getZ();
      y =  this.m10*normal.getX() + this.m11*normal.getY() + this.m12*normal.getZ();
      normal.setX(this.m20*normal.getX() + this.m21*normal.getY() + this.m22*normal.getZ());
      normal.setY(x);
      normal.setZ(y);
	}

	  /**
	   * Transforms the point parameter with this Matrix4f and
	   * places the result back into point.  The fourth element of the
	   * point input paramter is assumed to be one.
	   * @param point  the input point to be transformed.
	   */
	public final void transform(Point3f point)
	{
	    float  x, y;
	    x = this.m00*point.getX() + this.m01*point.getY() + this.m02*point.getZ() + this.m03;
	    y = this.m10*point.getX() + this.m11*point.getY() + this.m12*point.getZ() + this.m13;
	    point.setZ(this.m20*point.getX() + this.m21*point.getY() + this.m22*point.getZ() + this.m23);
	    point.setX(x);
	    point.setY(y);
	}
	    
  /**
   * Sets the value of this matrix to the matrix conversion of the
   * double precision quaternion argument.
   * @param q1 the quaternion to be converted
   */
  public final void set(Quaternion q1)
  {
	this.m00 = 1.0f - 2.0f*q1.getY()*q1.getY() - 2.0f*q1.getZ()*q1.getZ();
	this.m10 = 2.0f*(q1.getX()*q1.getY() + q1.getW()*q1.getZ());
	this.m20 = 2.0f*(q1.getX()*q1.getZ() - q1.getW()*q1.getY());

	this.m01 = 2.0f*(q1.getX()*q1.getY() - q1.getW()*q1.getZ());
	this.m11 = (float) (1.0f - 2.0*q1.getX()*q1.getX() - 2.0*q1.getZ()*q1.getZ());
	this.m21 = 2.0f*(q1.getY()*q1.getZ() + q1.getW()*q1.getX());

	this.m02 = 2.0f*(q1.getX()*q1.getZ() + q1.getW()*q1.getY());
	this.m12 = 2.0f*(q1.getY()*q1.getZ() - q1.getW()*q1.getX());
	this.m22 = 1.0f - 2.0f*q1.getX()*q1.getX() - 2.0f*q1.getY()*q1.getY();

	this.m03 = 0.0f;
	this.m13 = 0.0f;
	this.m23 = 0.0f;

	this.m30 = 0.0f;
	this.m31 = 0.0f;
	this.m32 = 0.0f;
	this.m33 = 1.0f;
  }

  /**
   * Sets the value of this matrix to the matrix conversion of the
   * (single precision) axis and angle argument.
   * @param a1 the axis and angle to be converted
   */
  public final void set(AxisAngle4f a1)
  {
    float mag = (float)Math.sqrt( a1.x*a1.x + a1.y*a1.y + a1.z*a1.z);
    if( mag < 1.0E-8 ) {
	 this.m00 = 1.0f;
	 this.m01 = 0.0f;
	 this.m02 = 0.0f;

	 this.m10 = 0.0f;
	 this.m11 = 1.0f;
	 this.m12 = 0.0f;

	 this.m20 = 0.0f;
	 this.m21 = 0.0f;
	 this.m22 = 1.0f;
    } else {
	 mag = 1.0f/mag;
       float ax = a1.x*mag;
       float ay = a1.y*mag;
       float az = a1.z*mag;

       float sinTheta = (float)Math.sin(a1.angle);
       float cosTheta = (float)Math.cos(a1.angle);
       float t = 1.0f - cosTheta;
       
       float xz = ax * az;
       float xy = ax * ay;
       float yz = ay * az;
       
       this.m00 = t * ax * ax + cosTheta;
       this.m01 = t * xy - sinTheta * az;
       this.m02 = t * xz + sinTheta * ay;

       this.m10 = t * xy + sinTheta * az;
       this.m11 = t * ay * ay + cosTheta;
       this.m12 = t * yz - sinTheta * ax;

       this.m20 = t * xz - sinTheta * ay;
       this.m21 = t * yz + sinTheta * ax;
       this.m22 = t * az * az + cosTheta;
    }
    this.m03 = 0.0f;
    this.m13 = 0.0f;
    this.m23 = 0.0f;

    this.m30 = 0.0f;
    this.m31 = 0.0f;
    this.m32 = 0.0f;
    this.m33 = 1.0f;
  }

  /**
   * Modifies the translational components of this matrix to the values
   * of the Vector3f argument; the other values of this matrix are not
   * modified.
   * @param trans  the translational component
   */  
  public final void setTranslation(Vector3f trans)
  {
     this.m03 = trans.getX(); 
     this.m13 = trans.getY();
     this.m23 = trans.getZ();
  }
  
  /**
   * Sets the value of this matrix to the matrix inverse
   * of the passed (user declared) matrix m1.
   * @param m1 the matrix to be inverted
   */
  public final void invert(Matrix4f m1)
  {

     invertGeneral( m1);    
  }

  /**
   * Inverts this matrix in place.
   */
  public final void invert()
  {
     invertGeneral( this );    
  }

    /**
     * General invert routine.  Inverts m1 and places the result in "this".
     * Note that this routine handles both the "this" version and the
     * non-"this" version.
     *
     * Also note that since this routine is slow anyway, we won't worry
     * about allocating a little bit of garbage.
     */
    final void invertGeneral(Matrix4f  m1) {
	float temp[] = new float[16];
	float result[] = new float[16];
	int row_perm[] = new int[4];
	int i, r, c;

	// Use LU decomposition and backsubstitution code specifically
	// for floating-point 4x4 matrices.

	// Copy source matrix to t1tmp 
        temp[0] = m1.m00;
        temp[1] = m1.m01;
        temp[2] = m1.m02;
        temp[3] = m1.m03;
 
        temp[4] = m1.m10;
        temp[5] = m1.m11;
        temp[6] = m1.m12;
        temp[7] = m1.m13;
 
        temp[8] = m1.m20;
        temp[9] = m1.m21;
        temp[10] = m1.m22;
        temp[11] = m1.m23;
 
        temp[12] = m1.m30;
        temp[13] = m1.m31;
        temp[14] = m1.m32;
        temp[15] = m1.m33;

	// Calculate LU decomposition: Is the matrix singular? 
	if (!luDecomposition(temp, row_perm)) {
	    // Matrix has no inverse 
	    throw new SingularMatrixException("cannot invert matrix");
	}

	// Perform back substitution on the identity matrix 
        for(i=0;i<16;i++) result[i] = 0.0f;
        result[0] = 1.0f; result[5] = 1.0f; result[10] = 1.0f; result[15] = 1.0f;
	luBacksubstitution(temp, row_perm, result);

        this.m00 = result[0];
        this.m01 = result[1];
        this.m02 = result[2];
        this.m03 = result[3];

        this.m10 = result[4];
        this.m11 = result[5];
        this.m12 = result[6];
        this.m13 = result[7];
 
        this.m20 = result[8];
        this.m21 = result[9];
        this.m22 = result[10];
        this.m23 = result[11];
 
        this.m30 = result[12];
        this.m31 = result[13];
        this.m32 = result[14];
        this.m33 = result[15];

    }
    
    /**
     * Given a 4x4 array "matrix0", this function replaces it with the 
     * LU decomposition of a row-wise permutation of itself.  The input 
     * parameters are "matrix0" and "dimen".  The array "matrix0" is also 
     * an output parameter.  The vector "row_perm[4]" is an output 
     * parameter that contains the row permutations resulting from partial 
     * pivoting.  The output parameter "even_row_xchg" is 1 when the 
     * number of row exchanges is even, or -1 otherwise.  Assumes data 
     * type is always float.
     *
     * This function is similar to luDecomposition, except that it
     * is tuned specifically for 4x4 matrices.
     *
     * @return true if the matrix is nonsingular, or false otherwise.
     */
    //
    // Reference: Press, Flannery, Teukolsky, Vetterling, 
    //	      _Numerical_Recipes_in_C_, Cambridge University Press, 
    //	      1988, pp 40-45.
    //
    static boolean luDecomposition(float[] matrix0,
				   int[] row_perm) {

	float row_scale[] = new float[4];

	// Determine implicit scaling information by looping over rows 
	{
	    int i, j;
	    int ptr, rs;
	    float big, temp;

	    ptr = 0;
	    rs = 0;

	    // For each row ... 
	    i = 4;
	    while (i-- != 0) {
		big = 0.0f;

		// For each column, find the largest element in the row 
		j = 4;
		while (j-- != 0) {
		    temp = matrix0[ptr++];
		    temp = Math.abs(temp);
		    if (temp > big) {
			big = temp;
		    }
		}

		// Is the matrix singular? 
		if (big == 0.0) {
		    return false;
		}
		row_scale[rs++] = 1.0f / big;
	    }
	}

	{
	    int j;
	    int mtx;

	    mtx = 0;

	    // For all columns, execute Crout's method 
	    for (j = 0; j < 4; j++) {
		int i, imax, k;
		int target, p1, p2;
		float sum, big, temp;

		// Determine elements of upper diagonal matrix U 
		for (i = 0; i < j; i++) {
		    target = mtx + (4*i) + j;
		    sum = matrix0[target];
		    k = i;
		    p1 = mtx + (4*i);
		    p2 = mtx + j;
		    while (k-- != 0) {
			sum -= matrix0[p1] * matrix0[p2];
			p1++;
			p2 += 4;
		    }
		    matrix0[target] = sum;
		}

		// Search for largest pivot element and calculate
		// intermediate elements of lower diagonal matrix L.
		big = 0.0f;
		imax = -1;
		for (i = j; i < 4; i++) {
		    target = mtx + (4*i) + j;
		    sum = matrix0[target];
		    k = j;
		    p1 = mtx + (4*i);
		    p2 = mtx + j;
		    while (k-- != 0) {
			sum -= matrix0[p1] * matrix0[p2];
			p1++;
			p2 += 4;
		    }
		    matrix0[target] = sum;

		    // Is this the best pivot so far? 
		    if ((temp = row_scale[i] * Math.abs(sum)) >= big) {
			big = temp;
			imax = i;
		    }
		}

		if (imax < 0) {
		    throw new RuntimeException("Logic error: imax < 0");
		}

		// Is a row exchange necessary? 
		if (j != imax) {
		    // Yes: exchange rows 
		    k = 4;
		    p1 = mtx + (4*imax);
		    p2 = mtx + (4*j);
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
		if (matrix0[(mtx + (4*j) + j)] == 0.0) {
		    return false;
		}

		// Divide elements of lower diagonal matrix L by pivot 
		if (j != (4-1)) {
		    temp = 1.0f / (matrix0[(mtx + (4*j) + j)]);
		    target = mtx + (4*(j+1)) + j;
		    i = 3 - j;
		    while (i-- != 0) {
			matrix0[target] *= temp;
			target += 4;
		    }
		}
	    }
	}

	return true;
    }
    
    /**
     * Solves a set of linear equations.  The input parameters "matrix1",
     * and "row_perm" come from luDecompostionD4x4 and do not change
     * here.  The parameter "matrix2" is a set of column vectors assembled
     * into a 4x4 matrix of floating-point values.  The procedure takes each
     * column of "matrix2" in turn and treats it as the right-hand side of the
     * matrix equation Ax = LUx = b.  The solution vector replaces the
     * original column of the matrix.
     *
     * If "matrix2" is the identity matrix, the procedure replaces its contents
     * with the inverse of the matrix from which "matrix1" was originally
     * derived.
     */
    //
    // Reference: Press, Flannery, Teukolsky, Vetterling, 
    //	      _Numerical_Recipes_in_C_, Cambridge University Press, 
    //	      1988, pp 44-45.
    //
    static void luBacksubstitution(float[] matrix1,
				   int[] row_perm,
				   float[] matrix2) {

	int i, ii, ip, j, k;
	int rp;
	int cv, rv;
	
	//	rp = row_perm;
	rp = 0;

	// For each column vector of matrix2 ... 
	for (k = 0; k < 4; k++) {
	    //	    cv = &(matrix2[0][k]);
	    cv = k;
	    ii = -1;

	    // Forward substitution 
	    for (i = 0; i < 4; i++) {
		float sum;

		ip = row_perm[rp+i];
		sum = matrix2[cv+4*ip];
		matrix2[cv+4*ip] = matrix2[cv+4*i];
		if (ii >= 0) {
		    //		    rv = &(matrix1[i][0]);
		    rv = i*4;
		    for (j = ii; j <= i-1; j++) {
			sum -= matrix1[rv+j] * matrix2[cv+4*j];
		    }
		}
		else if (sum != 0.0) {
		    ii = i;
		}
		matrix2[cv+4*i] = sum;
	    }

	    // Backsubstitution 
	    //	    rv = &(matrix1[3][0]);
	    rv = 3*4;
	    matrix2[cv+4*3] /= matrix1[rv+3];

	    rv -= 4;
	    matrix2[cv+4*2] = (matrix2[cv+4*2] -
			    matrix1[rv+3] * matrix2[cv+4*3]) / matrix1[rv+2];

	    rv -= 4;
	    matrix2[cv+4*1] = (matrix2[cv+4*1] -
			    matrix1[rv+2] * matrix2[cv+4*2] -
			    matrix1[rv+3] * matrix2[cv+4*3]) / matrix1[rv+1];

	    rv -= 4;
	    matrix2[cv+4*0] = (matrix2[cv+4*0] -
			    matrix1[rv+1] * matrix2[cv+4*1] -
			    matrix1[rv+2] * matrix2[cv+4*2] -
			    matrix1[rv+3] * matrix2[cv+4*3]) / matrix1[rv+0];
	}
    }

    /**
     * Sets the rotational component (upper 3x3) of this matrix to the
     * matrix equivalent values of the quaternion argument; the other
     * elements of this matrix are unchanged; a singular value
     * decomposition is performed on this object's upper 3x3 matrix to
     * factor out the scale, then this object's upper 3x3 matrix components
     * are replaced by the matrix equivalent of the quaternion,  
     * and then the scale is reapplied to the rotational components.
     * @param q1    the quaternion that specifies the rotation
     */  
    public void setRotation(Quaternion q1){  
	float[]    tmp_rot = new float[9];  // scratch matrix
	float[]    tmp_scale = new float[3];  // scratch matrix
        getScaleRotate( tmp_scale, tmp_rot );
 
        this.m00 = (1.0f - 2.0f*q1.getY()*q1.getY() - 2.0f*q1.getZ()*q1.getZ())*tmp_scale[0];
        this.m10 = (2.0f*(q1.getX()*q1.getY() + q1.getW()*q1.getZ()))*tmp_scale[0];
        this.m20 = (2.0f*(q1.getX()*q1.getZ() - q1.getW()*q1.getY()))*tmp_scale[0];
 
        this.m01 = (2.0f*(q1.getX()*q1.getY() - q1.getW()*q1.getZ()))*tmp_scale[1];
        this.m11 = (1.0f - 2.0f*q1.getX()*q1.getX() - 2.0f*q1.getZ()*q1.getZ())*tmp_scale[1];
        this.m21 = (2.0f*(q1.getY()*q1.getZ() + q1.getW()*q1.getX()))*tmp_scale[1];
 
        this.m02 = (2.0f*(q1.getX()*q1.getZ() + q1.getW()*q1.getY()))*tmp_scale[2];
        this.m12 = (2.0f*(q1.getY()*q1.getZ() - q1.getW()*q1.getX()))*tmp_scale[2];
        this.m22 = (1.0f - 2.0f*q1.getX()*q1.getX() - 2.0f*q1.getY()*q1.getY())*tmp_scale[2];
 
    }

    private final void getScaleRotate(float scales[], float rots[]) {
   	 
		float[]    tmp = new float[9];  // scratch matrix
		tmp[0] = this.m00;
		tmp[1] = this.m01;
		tmp[2] = this.m02;
	 
		tmp[3] = this.m10;
		tmp[4] = this.m11;
		tmp[5] = this.m12;
	 
		tmp[6] = this.m20;
		tmp[7] = this.m21;
		tmp[8] = this.m22;
	 
		Matrix3f.compute_svd( tmp, scales, rots);
    }

    /** 
     * Performs an SVD normalization of this matrix in order to acquire 
     * the normalized rotational component; the values are placed into 
     * the Quaternion parameter. 
     * @param q1  quaternion into which the rotation component is placed 
     */
    public void get(Quaternion q1){
	float[]    tmp_rot = new float[9];  // scratch matrix
	float[]    tmp_scale = new float[3];  // scratch matrix
	getScaleRotate( tmp_scale, tmp_rot );

        float ww;

        ww = 0.25f*(1.0f + tmp_rot[0] + tmp_rot[4] + tmp_rot[8]);
        if(!((ww<0?-ww:ww) < 1.0e-30)) {
          q1.setW((float)Math.sqrt(ww));
          ww = 0.25f/q1.getW();
          q1.setX((tmp_rot[7] - tmp_rot[5])*ww);
          q1.setY((tmp_rot[2] - tmp_rot[6])*ww);
          q1.setZ((tmp_rot[3] - tmp_rot[1])*ww);
          return;
        }

        q1.setW(0.0f);
        ww = -0.5f*(tmp_rot[4] + tmp_rot[8]);
        if(!((ww<0?-ww:ww) < 1.0e-30)) {
          q1.setX((float)Math.sqrt(ww));
          ww = 0.5f/q1.getX();
          q1.setY(tmp_rot[3]*ww);
          q1.setZ(tmp_rot[6]*ww);
          return;
        }

        q1.setX(0.0f);
        ww = 0.5f*(1.0f - tmp_rot[8]);
        if(!((ww<0?-ww:ww) < 1.0e-30)) {
          q1.setY((float)(Math.sqrt(ww)));
          q1.setZ(tmp_rot[7]/(2.0f*q1.getY()));
          return;
        }  
     
        q1.setY(0.0f);
        q1.setZ(1.0f);

    }

    /**
     * Retrieves the translational components of this matrix.
     * @param trans  the vector that will receive the translational component
     */
    public final void get(Vector3f trans)
    {  
        trans.setX(this.m03);
        trans.setY(this.m13);
        trans.setZ(this.m23);
    }
}