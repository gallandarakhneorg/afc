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
import org.arakhne.afc.math.continous.object2d.Point2f;
import org.arakhne.afc.math.continous.object2d.Tuple2f;
import org.arakhne.afc.math.continous.object2d.Vector2f;
import org.arakhne.afc.math.generic.Vector2D;

/**
 * Is represented internally as a 2x2 floating point matrix. The mathematical
 * representation is row major, as in traditional matrix mathematics.
 * 
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class Matrix2f implements Serializable, Cloneable, MathConstants {

	private static final long serialVersionUID = -181335987517755500L;

	/**
	 * The first matrix element in the first row.
	 */
	public float m00;

	/**
	 * The second matrix element in the first row.
	 */
	public float m01;

	/**
	 * The first matrix element in the second row.
	 */
	public float m10;

	/**
	 * The second matrix element in the second row.
	 */
	public float m11;

	/**
	 * Constructs and initializes a Matrix2d from the specified nine values.
	 * 
	 * @param m00
	 *            the [0][0] element
	 * @param m01
	 *            the [0][1] element
	 * @param m10
	 *            the [1][0] element
	 * @param m11
	 *            the [1][1] element
	 */
	public Matrix2f(float m00, float m01, float m10, float m11) {
		this.m00 = m00;
		this.m01 = m01;

		this.m10 = m10;
		this.m11 = m11;
	}

	/**
	 * Constructs and initializes a Matrix2d from the specified nine- element
	 * array.
	 * 
	 * @param v
	 *            the array of length 4 containing in order
	 */
	public Matrix2f(float[] v) {
		this.m00 = v[0];
		this.m01 = v[1];

		this.m10 = v[2];
		this.m11 = v[3];
	}

	/**
	 * Constructs a new matrix with the same values as the Matrix2d parameter.
	 * 
	 * @param m1
	 *            the source matrix
	 */
	public Matrix2f(Matrix2f m1) {
		this.m00 = m1.m00;
		this.m01 = m1.m01;

		this.m10 = m1.m10;
		this.m11 = m1.m11;
	}

	/**
	 * Constructs and initializes a Matrix2d to all zeros.
	 */
	public Matrix2f() {
		this.m00 = 0f;
		this.m01 = 0f;

		this.m10 = 0f;
		this.m11 = 0f;
	}

	/**
	 * Returns a string that contains the values of this Matrix2d.
	 * 
	 * @return the String representation
	 */
	@Override
	public String toString() {
		return this.m00 + ", " //$NON-NLS-1$
		+ this.m01 + "\n"  //$NON-NLS-1$
		+ this.m10 + ", "  //$NON-NLS-1$
		+ this.m11 + "\n";  //$NON-NLS-1$
	}

	/**
	 * Sets this Matrix2d to identity.
	 */
	public final void setIdentity() {
		this.m00 = 1f;
		this.m01 = 0f;

		this.m10 = 0f;
		this.m11 = 1f;
	}

	/**
	 * Sets the specified element of this Matrix2f to the value provided.
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
	public final void getRow(int row, Vector2D v) {
		if (row == 0) {
			v.set(this.m00, this.m01);
		}
		else if (row == 1) {
			v.set(this.m10, this.m11);
		}
		else {
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
		}
		else if (row == 1) {
			v[0] = this.m10;
			v[1] = this.m11;
		}
		else {
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
	public final void getColumn(int column, Vector2f v) {
		if (column == 0) {
			v.set(this.m00, this.m10);
		}
		else if (column == 1) {
			v.set(this.m01, this.m11);
		}
		else {
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
		}
		else if (column == 1) {
			v[0] = this.m01;
			v[1] = this.m11;
		}
		else {
			throw new ArrayIndexOutOfBoundsException();
		}
	}

	/**
	 * Sets the specified row of this Matrix2d to the 4 values provided.
	 * 
	 * @param row
	 *            the row number to be modified (zero indexed)
	 * @param x
	 *            the first column element
	 * @param y
	 *            the second column element
	 */
	public final void setRow(int row, float x, float y) {
		switch (row) {
		case 0:
			this.m00 = x;
			this.m01 = y;
			break;

		case 1:
			this.m10 = x;
			this.m11 = y;
			break;

		default:
			throw new ArrayIndexOutOfBoundsException();
		}
	}

	/**
	 * Sets the specified row of this Matrix2d to the Vector provided.
	 * 
	 * @param row
	 *            the row number to be modified (zero indexed)
	 * @param v
	 *            the replacement row
	 */
	public final void setRow(int row, Vector2f v) {
		switch (row) {
		case 0:
			this.m00 = v.getX();
			this.m01 = v.getY();
			break;

		case 1:
			this.m10 = v.getX();
			this.m11 = v.getY();
			break;

		default:
			throw new ArrayIndexOutOfBoundsException();
		}
	}

	/**
	 * Sets the specified row of this Matrix2d to the two values provided.
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
			break;

		case 1:
			this.m10 = v[0];
			this.m11 = v[1];
			break;

		default:
			throw new ArrayIndexOutOfBoundsException();
		}
	}

	/**
	 * Sets the specified column of this Matrix2d to the two values provided.
	 * 
	 * @param column
	 *            the column number to be modified (zero indexed)
	 * @param x
	 *            the first row element
	 * @param y
	 *            the second row element
	 */
	public final void setColumn(int column, float x, float y) {
		switch (column) {
		case 0:
			this.m00 = x;
			this.m10 = y;
			break;

		case 1:
			this.m01 = x;
			this.m11 = y;
			break;

		default:
			throw new ArrayIndexOutOfBoundsException();
		}
	}

	/**
	 * Sets the specified column of this Matrix2d to the vector provided.
	 * 
	 * @param column
	 *            the column number to be modified (zero indexed)
	 * @param v
	 *            the replacement column
	 */
	public final void setColumn(int column, Vector2f v) {
		switch (column) {
		case 0:
			this.m00 = v.getX();
			this.m10 = v.getY();
			break;

		case 1:
			this.m01 = v.getX();
			this.m11 = v.getY();
			break;

		default:
			throw new ArrayIndexOutOfBoundsException();
		}
	}

	/**
	 * Sets the specified column of this Matrix2d to the two values provided.
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
			break;

		case 1:
			this.m01 = v[0];
			this.m11 = v[1];
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

		this.m10 += scalar;
		this.m11 += scalar;
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
	public final void add(float scalar, Matrix2f m1) {
		this.m00 = m1.m00 + scalar;
		this.m01 = m1.m01 + scalar;

		this.m10 = m1.m10 + scalar;
		this.m11 = m1.m11 + scalar;
	}

	/**
	 * Sets the value of this matrix to the matrix sum of matrices m1 and m2.
	 * 
	 * @param m1
	 *            the first matrix
	 * @param m2
	 *            the second matrix
	 */
	public final void add(Matrix2f m1, Matrix2f m2) {
		this.m00 = m1.m00 + m2.m00;
		this.m01 = m1.m01 + m2.m01;

		this.m10 = m1.m10 + m2.m10;
		this.m11 = m1.m11 + m2.m11;
	}

	/**
	 * Sets the value of this matrix to the sum of itself and matrix m1.
	 * 
	 * @param m1
	 *            the other matrix
	 */
	public final void add(Matrix2f m1) {
		this.m00 += m1.m00;
		this.m01 += m1.m01;

		this.m10 += m1.m10;
		this.m11 += m1.m11;
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
	public final void sub(Matrix2f m1, Matrix2f m2) {
		this.m00 = m1.m00 - m2.m00;
		this.m01 = m1.m01 - m2.m01;

		this.m10 = m1.m10 - m2.m10;
		this.m11 = m1.m11 - m2.m11;
	}

	/**
	 * Sets the value of this matrix to the matrix difference of itself and
	 * matrix m1 (this = this - m1).
	 * 
	 * @param m1
	 *            the other matrix
	 */
	public final void sub(Matrix2f m1) {
		this.m00 -= m1.m00;
		this.m01 -= m1.m01;

		this.m10 -= m1.m10;
		this.m11 -= m1.m11;
	}

	/**
	 * Sets the value of this matrix to its transpose.
	 */
	public final void transpose() {
		float temp;
		temp = this.m10;
		this.m10 = this.m01;
		this.m01 = temp;
	}

	/**
	 * Sets the value of this matrix to the transpose of the argument matrix.
	 * 
	 * @param m1
	 *            the matrix to be transposed
	 */
	public final void transpose(Matrix2f m1) {
		if (this != m1) {
			this.m00 = m1.m00;
			this.m01 = m1.m10;

			this.m10 = m1.m01;
			this.m11 = m1.m11;
		}
		else {
			transpose();
		}
	}

	/**
	 * Sets the value of this matrix to the value of the Matrix2d argument.
	 * 
	 * @param m1
	 *            the source Matrix2d
	 */
	public final void set(Matrix2f m1) {
		this.m00 = m1.m00;
		this.m01 = m1.m01;

		this.m10 = m1.m10;
		this.m11 = m1.m11;
	}

	/**
	 * Sets the values in this Matrix2d equal to the row-major array parameter
	 * (ie, the first two elements of the array will be copied into the first
	 * row of this matrix, etc.).
	 * 
	 * @param m
	 *            the double precision array of length 4
	 */
	public final void set(float[] m) {
		this.m00 = m[0];
		this.m01 = m[1];

		this.m10 = m[2];
		this.m11 = m[4];
	}

	/**
	 * Set the components of the matrix.
	 * 
	 * @param m00
	 *            the [0][0] element
	 * @param m01
	 *            the [0][1] element
	 * @param m10
	 *            the [1][0] element
	 * @param m11
	 *            the [1][1] element
	 */
	public void set(float m00, float m01, float m10, float m11) {
		this.m00 = m00;
		this.m01 = m01;

		this.m10 = m10;
		this.m11 = m11;
	}
	
	/**
	 * Computes the determinant of this matrix.
	 * 
	 * @return the determinant of the matrix
	 */
	public final float determinant() {
		return this.m00*this.m11 - this.m01*this.m10;
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

		this.m10 *= scalar;
		this.m11 *= scalar;
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
	public final void mul(float scalar, Matrix2f m1) {
		this.m00 = scalar * m1.m00;
		this.m01 = scalar * m1.m01;

		this.m10 = scalar * m1.m10;
		this.m11 = scalar * m1.m11;
	}

	/**
	 * Sets the value of this matrix to the result of multiplying itself with
	 * matrix m1.
	 * 
	 * @param m1
	 *            the other matrix
	 */
	public final void mul(Matrix2f m1) {
		float _m00, _m01, _m10, _m11;

		_m00 = this.m00 * m1.m00 + this.m01 * m1.m10;
		_m01 = this.m00 * m1.m01 + this.m01 * m1.m11;

		_m10 = this.m10 * m1.m00 + this.m11 * m1.m10;
		_m11 = this.m10 * m1.m01 + this.m11 * m1.m11;

		this.m00 = _m00;
		this.m01 = _m01;
		this.m10 = _m10;
		this.m11 = _m11;
	}

	/**
	 * Multiply this matrix by the given vector and replies the resulting vector.
	 * 
	 * @param v
	 * @return this * v
	 */
	public final Vector2f mul(Vector2D v) {
		Vector2f r = new Vector2f();
		r.set(
				this.m00 * v.getX() + this.m01 * v.getY(),
				this.m10 * v.getX() + this.m11 * v.getY());
		return r;
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
	public final void mul(Matrix2f m1, Matrix2f m2) {
		if (this != m1 && this != m2) {
			this.m00 = m1.m00 * m2.m00 + m1.m01 * m2.m10;
			this.m01 = m1.m00 * m2.m01 + m1.m01 * m2.m11;

			this.m10 = m1.m10 * m2.m00 + m1.m11 * m2.m10;
			this.m11 = m1.m10 * m2.m01 + m1.m11 * m2.m11;
		}
		else {
			float _m00, _m01, _m10, _m11; // vars for temp result matrix

			_m00 = m1.m00 * m2.m00 + m1.m01 * m2.m10;
			_m01 = m1.m00 * m2.m01 + m1.m01 * m2.m11;

			_m10 = m1.m10 * m2.m00 + m1.m11 * m2.m10;
			_m11 = m1.m10 * m2.m01 + m1.m11 * m2.m11;

			this.m00 = _m00;
			this.m01 = _m01;
			this.m10 = _m10;
			this.m11 = _m11;
		}
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
	public final void mulTransposeBoth(Matrix2f m1, Matrix2f m2) {
		if (this != m1 && this != m2) {
			this.m00 = m1.m00 * m2.m00 + m1.m10 * m2.m01;
			this.m01 = m1.m00 * m2.m10 + m1.m10 * m2.m11;

			this.m10 = m1.m01 * m2.m00 + m1.m11 * m2.m01;
			this.m11 = m1.m01 * m2.m10 + m1.m11 * m2.m11;
		}
		else {
			float _m00, _m01, _m10, _m11; // vars for temp result matrix

			_m00 = m1.m00 * m2.m00 + m1.m10 * m2.m01;
			_m01 = m1.m00 * m2.m10 + m1.m10 * m2.m11;

			_m10 = m1.m01 * m2.m00 + m1.m11 * m2.m01;
			_m11 = m1.m01 * m2.m10 + m1.m11 * m2.m11;

			this.m00 = _m00;
			this.m01 = _m01;
			this.m10 = _m10;
			this.m11 = _m11;
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
	public final void mulTransposeRight(Matrix2f m1, Matrix2f m2) {
		if (this != m1 && this != m2) {
			this.m00 = m1.m00 * m2.m00 + m1.m01 * m2.m01;
			this.m01 = m1.m00 * m2.m10 + m1.m01 * m2.m11;

			this.m10 = m1.m10 * m2.m00 + m1.m11 * m2.m01;
			this.m11 = m1.m10 * m2.m10 + m1.m11 * m2.m11;
		}
		else {
			float _m00, _m01, _m10, _m11; // vars for temp result matrix

			_m00 = m1.m00 * m2.m00 + m1.m01 * m2.m01;
			_m01 = m1.m00 * m2.m10 + m1.m01 * m2.m11;

			_m10 = m1.m10 * m2.m00 + m1.m11 * m2.m01;
			_m11 = m1.m10 * m2.m10 + m1.m11 * m2.m11;

			this.m00 = _m00;
			this.m01 = _m01;
			this.m10 = _m10;
			this.m11 = _m11;
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
	public final void mulTransposeLeft(Matrix2f m1, Matrix2f m2) {
		if (this != m1 && this != m2) {
			this.m00 = m1.m00 * m2.m00 + m1.m10 * m2.m10;
			this.m01 = m1.m00 * m2.m01 + m1.m10 * m2.m11;

			this.m10 = m1.m01 * m2.m00 + m1.m11 * m2.m10;
			this.m11 = m1.m01 * m2.m01 + m1.m11 * m2.m11;
		}
		else {
			float _m00, _m01, _m10, _m11; // vars for temp result matrix

			_m00 = m1.m00 * m2.m00 + m1.m10 * m2.m10;
			_m01 = m1.m00 * m2.m01 + m1.m10 * m2.m11;

			_m10 = m1.m01 * m2.m00 + m1.m11 * m2.m10;
			_m11 = m1.m01 * m2.m01 + m1.m11 * m2.m11;

			this.m00 = _m00;
			this.m01 = _m01;
			this.m10 = _m10;
			this.m11 = _m11;
		}
	}

	/**
	 * Perform cross product normalization of this matrix.
	 */
	public final void normalizeCP() {
		double mag = 1.0 / Math.sqrt(this.m00 * this.m00 + this.m10 * this.m10);
		this.m00 = (float)(this.m00 * mag);
		this.m10 = (float)(this.m10 * mag);

		mag = 1.0 / Math.sqrt(this.m01 * this.m01 + this.m11 * this.m11);
		this.m01 = (float)(this.m01 * mag);
		this.m11 = (float)(this.m11 * mag);
	}

	/**
	 * Perform cross product normalization of matrix m1 and place the normalized
	 * values into this.
	 * 
	 * @param m1
	 *            Provides the matrix values to be normalized
	 */
	public final void normalizeCP(Matrix2f m1) {
		double mag = 1.0 / Math.sqrt(m1.m00 * m1.m00 + m1.m10 * m1.m10);
		this.m00 = (float)(m1.m00 * mag);
		this.m10 = (float)(m1.m10 * mag);

		mag = 1.0 / Math.sqrt(m1.m01 * m1.m01 + m1.m11 * m1.m11);
		this.m01 = (float)(m1.m01 * mag);
		this.m11 = (float)(m1.m11 * mag);
	}

	/**
	 * Returns true if all of the data members of Matrix2d m1 are equal to the
	 * corresponding data members in this Matrix2d.
	 * 
	 * @param m1
	 *            the matrix with which the comparison is made
	 * @return true or false
	 */
	public boolean equals(Matrix2f m1) {
		try {
			return (this.m00 == m1.m00 && this.m01 == m1.m01
					&& this.m10 == m1.m10
					&& this.m11 == m1.m11);
		}
		catch (NullPointerException e2) {
			return false;
		}
	}

	/**
	 * Returns true if the Object t1 is of type Matrix2d and all of the data
	 * members of t1 are equal to the corresponding data members in this
	 * Matrix2d.
	 * 
	 * @param t1
	 *            the matrix with which the comparison is made
	 * @return true or false
	 */
	@Override
	public boolean equals(Object t1) {
		try {
			Matrix2f m2 = (Matrix2f) t1;
			return (this.m00 == m2.m00 && this.m01 == m2.m01
					&& this.m10 == m2.m10
					&& this.m11 == m2.m11);
		}
		catch (ClassCastException e1) {
			return false;
		}
		catch (NullPointerException e2) {
			return false;
		}
	}

	/**
	 * Returns a hash code value based on the data values in this object. Two
	 * different Matrix2d objects with identical data values (i.e.,
	 * Matrix2d.equals returns true) will return the same hash code value. Two
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
		bits = 31L * bits + floatToIntBits(this.m10);
		bits = 31L * bits + floatToIntBits(this.m11);
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

		this.m10 = 0f;
		this.m11 = 0f;
	}

	/**
	 * Sets this matrix as diagonal.
	 * 
	 * @param m00
	 *            the first element of the diagonal
	 * @param m11
	 *            the second element of the diagonal
	 */
	public final void setDiagonal(float m00, float m11) {
		this.m00 = m00;
		this.m01 = 0f;
		this.m10 = 0f;
		this.m11 = m11;
	}

	/**
	 * Negates the value of this matrix: this = -this.
	 */
	public final void negate() {
		this.m00 = -this.m00;
		this.m01 = -this.m01;

		this.m10 = -this.m10;
		this.m11 = -this.m11;
	}

	/**
	 * Sets the value of this matrix equal to the negation of of the Matrix2d
	 * parameter.
	 * 
	 * @param m1
	 *            the source matrix
	 */
	public final void negate(Matrix2f m1) {
		this.m00 = -m1.m00;
		this.m01 = -m1.m01;

		this.m10 = -m1.m10;
		this.m11 = -m1.m11;
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
	public Matrix2f clone() {
		Matrix2f m1 = null;
		try {
			m1 = (Matrix2f) super.clone();
		}
		catch (CloneNotSupportedException e) {
			// this shouldn't happen, since we are Cloneable
			throw new InternalError();
		}

		// Also need to create new tmp arrays (no need to actually clone them)
		return m1;
	}

	/**
	 * Get the first matrix element in the first row.
	 * 
	 * @return Returns the m00.
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
	 * Get first matrix element in the second row.
	 * 
	 * @return Returns the m10.
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

	/** Set this matrix with the covariance matrix's elements for the given
	 * set of tuples.
	 * 
	 * @param tuples
	 * @return the mean of the tuples.
	 */
	public final Vector2f cov(Vector2f... tuples) {
		return cov(Arrays.asList(tuples));
	}

	/** Set this matrix with the covariance matrix's elements for the given
	 * set of tuples.
	 * 
	 * @param tuples
	 * @return the mean of the tuples.
	 */
	public final Vector2f cov(Point2f... tuples) {
		return cov(Arrays.asList(tuples));
	}
	
	/** Set this matrix with the covariance matrix's elements for the given
	 * set of tuples.
	 * 
	 * @param tuples
	 * @return the mean of the tuples.
	 */
	public Vector2f cov(Iterable<? extends Tuple2f<?>> tuples) {
		setZero();

		// Compute the mean m
		Vector2f m = new Vector2f();
		int count = 0;
		for(Tuple2f<?> p : tuples) {
			m.add(p.getX(), p.getY());
			++count;
		}

		if (count==0) return null;

		m.scale(1f/count);

		// Compute the covariance term [Gottshalk2000]
		// c_ij = sum(p'_i * p'_j) / n
		// c_ij = sum((p_i - m_i) * (p_j - m_j)) / n
		for(Tuple2f<?> p : tuples) {
			this.m00 += (p.getX() - m.getX()) * (p.getX() - m.getX());
			this.m01 += (p.getX() - m.getX()) * (p.getY() - m.getY()); // same as m10
			//cov.m10 += (p.getY() - m.getY()) * (p.getX() - m.getX()); // same as m01
			this.m11 += (p.getY() - m.getY()) * (p.getY() - m.getY());
		}

		this.m00 /= count;
		this.m01 /= count;
		this.m10 = this.m01;
		this.m11 /= count;

		return m;
	}

	/** Replies if the matrix is symmetric.
	 * 
	 * @return <code>true</code> if the matrix is symmetric, otherwise
	 * <code>false</code>
	 */
	public boolean isSymmetric() {
		return	this.m01 == this.m10;
	}
	
	/**
	 * Compute the eigenvectors of this matrix, assuming it is a symmetric matrix
	 * according to the Jacobi Cyclic Method.
	 * 
	 * @param eigenVectors are the matrix of vectors to fill. Eigen vectors are the
	 * columns of the matrix.
	 * @return the eigenvalues which are corresponding to the <var>eigenVectors</var> columns.
	 * @see #eigenVectorsOfSymmetricMatrix(Matrix2f) 
	 */
	public float[] eigenVectorsOfSymmetricMatrix(Matrix2f eigenVectors) {
		assert(eigenVectors!=null);

		// Copy values up to the diagonal
		float m11 = getElement(0,0);
		float m12 = getElement(0,1);
		float m22 = getElement(1,1);
		
		boolean sweepsConsumed = true;
		int i;
		float u, u2, u2p1, t, c, s;
		float ri0, ri1;
		
		for(int a=0; a<JACOBI_MAX_SWEEPS; ++a) {
			
			// Exit loop if off-diagonal entries are small enough
			if ((Math.abs(m12) < MathConstants.EPSILON)) {
				sweepsConsumed = false;
				break;
			}
			
			// Annihilate (1,2) entry
			if (m12 != 0.) {
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
				&& MathUtil.isEpsilonZero(this.m10)
				&& MathUtil.isEpsilonEqual(this.m11, 1f);
	}

}