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

package org.arakhne.afc.math.matrix;

import static org.arakhne.afc.math.MathConstants.JACOBI_EPSILON;
import static org.arakhne.afc.math.MathConstants.JACOBI_MAX_SWEEPS;

import java.io.Serializable;
import java.util.Arrays;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.MathUtil;
import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.Tuple2D;
import org.arakhne.afc.math.geometry.d2.Vector2D;
import org.arakhne.afc.vmutil.asserts.AssertMessages;

/**
 * Is represented internally as a 2x2 floating point matrix. The mathematical
 * representation is row major, as in traditional matrix mathematics.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@SuppressWarnings({"checkstyle:magicnumber", "checkstyle:methodcount"})
public class Matrix2d implements Serializable, Cloneable {

	private static final long serialVersionUID = -181335987517755500L;

	/**
	 * The first matrix element in the first row.
	 */
	protected double m00;

	/**
	 * The second matrix element in the first row.
	 */
	protected double m01;

	/**
	 * The first matrix element in the second row.
	 */
	protected double m10;

	/**
	 * The second matrix element in the second row.
	 */
	protected double m11;

	/** Indicates if the matrix is identity.
	 * If <code>null</code> the identity flag must be determined.
	 */
	protected Boolean isIdentity;

	/**
	 * Constructs and initializes a Matrix2f from the specified nine values.
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
	public Matrix2d(double m00, double m01, double m10, double m11) {
		this.m00 = m00;
		this.m01 = m01;

		this.m10 = m10;
		this.m11 = m11;
	}

	/**
	 * Constructs and initializes a Matrix2f from the specified nine- element
	 * array.
	 *
	 * @param matrix
	 *            the array of length 4 containing in order
	 */
	public Matrix2d(double[] matrix) {
		assert matrix != null : AssertMessages.notNullParameter();
		assert matrix.length >= 4 : AssertMessages.tooSmallArrayParameter(matrix.length, 4);
		this.m00 = matrix[0];
		this.m01 = matrix[1];

		this.m10 = matrix[2];
		this.m11 = matrix[3];
	}

	/**
	 * Constructs a new matrix with the same values as the Matrix2f parameter.
	 *
	 * @param matrix
	 *            the source matrix
	 */
	public Matrix2d(Matrix2d matrix) {
		assert matrix != null : AssertMessages.notNullParameter();
		this.m00 = matrix.m00;
		this.m01 = matrix.m01;

		this.m10 = matrix.m10;
		this.m11 = matrix.m11;
	}

	/**
	 * Constructs and initializes a Matrix2f to all zeros.
	 */
	public Matrix2d() {
		this.m00 = 0.;
		this.m01 = 0.;

		this.m10 = 0.;
		this.m11 = 0.;
	}

	/**
	 * Returns a string that contains the values of this Matrix2f.
	 *
	 * @return the String representation
	 */
	@Pure
	@Override
	public String toString() {
		return this.m00 + ", " //$NON-NLS-1$
				+ this.m01 + "\n" //$NON-NLS-1$
				+ this.m10 + ", " //$NON-NLS-1$
				+ this.m11 + "\n"; //$NON-NLS-1$
	}

	/**
	 * Sets this Matrix2f to identity.
	 */
	public final void setIdentity() {
		this.m00 = 1.;
		this.m01 = 0.;

		this.m10 = 0.;
		this.m11 = 1.;

		this.isIdentity = Boolean.TRUE;
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
	public final void setElement(int row, int column, double value) {
		assert row >= 0 && row < 2 : AssertMessages.outsideRangeInclusiveParameter(0, row, 0, 1);
		assert column >= 0 && column < 2 : AssertMessages.outsideRangeInclusiveParameter(1, column, 0, 1);
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
		this.isIdentity = null;
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
	@Pure
	public final double getElement(int row, int column) {
		assert row >= 0 && row < 2 : AssertMessages.outsideRangeInclusiveParameter(0, row, 0, 1);
		assert column >= 0 && column < 2 : AssertMessages.outsideRangeInclusiveParameter(1, column, 0, 1);
		switch (row) {
		case 0:
			switch (column) {
			case 0:
				return this.m00;
			case 1:
				return this.m01;
			default:
				break;
			}
			break;
		case 1:
			switch (column) {
			case 0:
				return this.m10;
			case 1:
				return this.m11;
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
	 * @param vector
	 *            the vector into which the matrix row values will be copied
	 */
	public final void getRow(int row, Tuple2D<?> vector) {
		assert row >= 0 && row < 2 : AssertMessages.outsideRangeInclusiveParameter(0, row, 0, 1);
		assert vector != null : AssertMessages.notNullParameter(1);
		if (row == 0) {
			vector.set(this.m00, this.m01);
		} else if (row == 1) {
			vector.set(this.m10, this.m11);
		} else {
			throw new ArrayIndexOutOfBoundsException();
		}

	}

	/**
	 * Copies the matrix values in the specified row into the array parameter.
	 *
	 * @param row
	 *            the matrix row
	 * @param vector
	 *            the array into which the matrix row values will be copied
	 */
	public final void getRow(int row, double[] vector) {
		assert row >= 0 && row < 2 : AssertMessages.outsideRangeInclusiveParameter(0, row, 0, 1);
		assert vector != null : AssertMessages.notNullParameter(1);
		assert vector.length >= 2 : AssertMessages.tooSmallArrayParameter(1, vector.length, 2);
		if (row == 0) {
			vector[0] = this.m00;
			vector[1] = this.m01;
		} else if (row == 1) {
			vector[0] = this.m10;
			vector[1] = this.m11;
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
	 * @param vector
	 *            the vector into which the matrix row values will be copied
	 */
	public final void getColumn(int column, Tuple2D<?> vector) {
		assert column >= 0 && column < 2 : AssertMessages.outsideRangeInclusiveParameter(0, column, 0, 1);
		assert vector != null : AssertMessages.notNullParameter(1);
		if (column == 0) {
			vector.set(this.m00, this.m10);
		} else if (column == 1) {
			vector.set(this.m01, this.m11);
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
	 * @param vector
	 *            the array into which the matrix row values will be copied
	 */
	public final void getColumn(int column, double[] vector) {
		assert column >= 0 && column < 2 : AssertMessages.outsideRangeInclusiveParameter(0, column, 0, 1);
		assert vector != null : AssertMessages.notNullParameter(1);
		assert vector.length >= 2 : AssertMessages.tooSmallArrayParameter(1, vector.length, 2);
		if (column == 0) {
			vector[0] = this.m00;
			vector[1] = this.m10;
		} else if (column == 1) {
			vector[0] = this.m01;
			vector[1] = this.m11;
		} else {
			throw new ArrayIndexOutOfBoundsException();
		}
	}

	/**
	 * Sets the specified row of this Matrix2f to the 4 values provided.
	 *
	 * @param row
	 *            the row number to be modified (zero indexed)
	 * @param x
	 *            the first column element
	 * @param y
	 *            the second column element
	 */
	public final void setRow(int row, double x, double y) {
		assert row >= 0 && row < 2 : AssertMessages.outsideRangeInclusiveParameter(0, row, 0, 1);
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
		this.isIdentity = null;
	}

	/**
	 * Sets the specified row of this Matrix2f to the Vector provided.
	 *
	 * @param row
	 *            the row number to be modified (zero indexed)
	 * @param vector
	 *            the replacement row
	 */
	public final void setRow(int row, Tuple2D<?> vector) {
		assert row >= 0 && row < 2 : AssertMessages.outsideRangeInclusiveParameter(0, row, 0, 1);
		assert vector != null : AssertMessages.notNullParameter(1);
		switch (row) {
		case 0:
			this.m00 = vector.getX();
			this.m01 = vector.getY();
			break;

		case 1:
			this.m10 = vector.getX();
			this.m11 = vector.getY();
			break;

		default:
			throw new ArrayIndexOutOfBoundsException();
		}
		this.isIdentity = null;
	}

	/**
	 * Sets the specified row of this Matrix2f to the two values provided.
	 *
	 * @param row
	 *            the row number to be modified (zero indexed)
	 * @param vector
	 *            the replacement row
	 */
	public final void setRow(int row, double[] vector) {
		assert row >= 0 && row < 2 : AssertMessages.outsideRangeInclusiveParameter(0, row, 0, 1);
		assert vector != null : AssertMessages.notNullParameter(1);
		assert vector.length >= 2 : AssertMessages.tooSmallArrayParameter(1, vector.length, 2);
		switch (row) {
		case 0:
			this.m00 = vector[0];
			this.m01 = vector[1];
			break;

		case 1:
			this.m10 = vector[0];
			this.m11 = vector[1];
			break;

		default:
			throw new ArrayIndexOutOfBoundsException();
		}
		this.isIdentity = null;
	}

	/**
	 * Sets the specified column of this Matrix2f to the two values provided.
	 *
	 * @param column
	 *            the column number to be modified (zero indexed)
	 * @param x
	 *            the first row element
	 * @param y
	 *            the second row element
	 */
	public final void setColumn(int column, double x, double y) {
		assert column >= 0 && column < 2 : AssertMessages.outsideRangeInclusiveParameter(0, column, 0, 1);
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
		this.isIdentity = null;
	}

	/**
	 * Sets the specified column of this Matrix2f to the vector provided.
	 *
	 * @param column
	 *            the column number to be modified (zero indexed)
	 * @param vector
	 *            the replacement column
	 */
	public final void setColumn(int column, Tuple2D<?> vector) {
		assert column >= 0 && column < 2 : AssertMessages.outsideRangeInclusiveParameter(0, column, 0, 1);
		assert vector != null : AssertMessages.notNullParameter(1);
		switch (column) {
		case 0:
			this.m00 = vector.getX();
			this.m10 = vector.getY();
			break;

		case 1:
			this.m01 = vector.getX();
			this.m11 = vector.getY();
			break;

		default:
			throw new ArrayIndexOutOfBoundsException();
		}
		this.isIdentity = null;
	}

	/**
	 * Sets the specified column of this Matrix2f to the two values provided.
	 *
	 * @param column
	 *            the column number to be modified (zero indexed)
	 * @param vector
	 *            the replacement column
	 */
	public final void setColumn(int column, double[] vector) {
		assert column >= 0 && column < 2 : AssertMessages.outsideRangeInclusiveParameter(0, column, 0, 1);
		assert vector != null : AssertMessages.notNullParameter(1);
		assert vector.length >= 2 : AssertMessages.tooSmallArrayParameter(1, vector.length, 2);
		switch (column) {
		case 0:
			this.m00 = vector[0];
			this.m10 = vector[1];
			break;

		case 1:
			this.m01 = vector[0];
			this.m11 = vector[1];
			break;

		default:
			throw new ArrayIndexOutOfBoundsException();
		}
		this.isIdentity = null;
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

		this.m10 += scalar;
		this.m11 += scalar;

		this.isIdentity = null;
	}

	/**
	 * Adds a scalar to each component of the matrix m1 and places the result
	 * into this. Matrix m1 is not modified.
	 *
	 * @param scalar
	 *            the scalar adder
	 * @param matrix
	 *            the original matrix values
	 */
	public final void add(double scalar, Matrix2d matrix) {
		assert matrix != null : AssertMessages.notNullParameter(1);
		this.m00 = matrix.m00 + scalar;
		this.m01 = matrix.m01 + scalar;

		this.m10 = matrix.m10 + scalar;
		this.m11 = matrix.m11 + scalar;

		this.isIdentity = null;
	}

	/**
	 * Sets the value of this matrix to the matrix sum of matrices m1 and m2.
	 *
	 * @param matrix1
	 *            the first matrix
	 * @param matrix2
	 *            the second matrix
	 */
	public final void add(Matrix2d matrix1, Matrix2d matrix2) {
		assert matrix1 != null : AssertMessages.notNullParameter(0);
		assert matrix2 != null : AssertMessages.notNullParameter(1);
		this.m00 = matrix1.m00 + matrix2.m00;
		this.m01 = matrix1.m01 + matrix2.m01;

		this.m10 = matrix1.m10 + matrix2.m10;
		this.m11 = matrix1.m11 + matrix2.m11;

		this.isIdentity = null;
	}

	/**
	 * Sets the value of this matrix to the sum of itself and matrix m1.
	 *
	 * @param matrix
	 *            the other matrix
	 */
	public final void add(Matrix2d matrix) {
		assert matrix != null : AssertMessages.notNullParameter();
		this.m00 += matrix.m00;
		this.m01 += matrix.m01;

		this.m10 += matrix.m10;
		this.m11 += matrix.m11;

		this.isIdentity = null;
	}

	/**
	 * Sets the value of this matrix to the matrix difference of matrices m1 and
	 * m2.
	 *
	 * @param matrix1
	 *            the first matrix
	 * @param matrix2
	 *            the second matrix
	 */
	public final void sub(Matrix2d matrix1, Matrix2d matrix2) {
		assert matrix1 != null : AssertMessages.notNullParameter(0);
		assert matrix2 != null : AssertMessages.notNullParameter(1);
		this.m00 = matrix1.m00 - matrix2.m00;
		this.m01 = matrix1.m01 - matrix2.m01;

		this.m10 = matrix1.m10 - matrix2.m10;
		this.m11 = matrix1.m11 - matrix2.m11;

		this.isIdentity = null;
	}

	/**
	 * Sets the value of this matrix to the matrix difference of itself and
	 * matrix m1 (this = this - m1).
	 *
	 * @param matrix
	 *            the other matrix
	 */
	public final void sub(Matrix2d matrix) {
		assert matrix != null : AssertMessages.notNullParameter();
		this.m00 -= matrix.m00;
		this.m01 -= matrix.m01;

		this.m10 -= matrix.m10;
		this.m11 -= matrix.m11;

		this.isIdentity = null;
	}

	/**
	 * Sets the value of this matrix to its transpose.
	 */
	public final void transpose() {
		final double temp = this.m10;
		this.m10 = this.m01;
		this.m01 = temp;
	}

	/**
	 * Sets the value of this matrix to the transpose of the argument matrix.
	 *
	 * @param matrix
	 *            the matrix to be transposed
	 */
	public final void transpose(Matrix2d matrix) {
		assert matrix != null : AssertMessages.notNullParameter();
		if (this != matrix) {
			this.m00 = matrix.m00;
			this.m01 = matrix.m10;

			this.m10 = matrix.m01;
			this.m11 = matrix.m11;

			this.isIdentity = matrix.isIdentity();
		} else {
			transpose();
		}
	}

	/**
	 * Sets the value of this matrix to the value of the Matrix2f argument.
	 *
	 * @param matrix
	 *            the source Matrix2f
	 */
	public final void set(Matrix2d matrix) {
		assert matrix != null : AssertMessages.notNullParameter();
		this.m00 = matrix.m00;
		this.m01 = matrix.m01;

		this.m10 = matrix.m10;
		this.m11 = matrix.m11;

		this.isIdentity = matrix.isIdentity();
	}

	/**
	 * Sets the values in this Matrix2f equal to the row-major array parameter
	 * (ie, the first two elements of the array will be copied into the first
	 * row of this matrix, etc.).
	 *
	 * @param matrix
	 *            the double precision array of length 4
	 */
	public final void set(double[] matrix) {
		assert matrix != null : AssertMessages.notNullParameter();
		assert matrix.length >= 2 : AssertMessages.tooSmallArrayParameter(matrix.length, 2);
		this.m00 = matrix[0];
		this.m01 = matrix[1];

		this.m10 = matrix[2];
		this.m11 = matrix[4];

		this.isIdentity = null;
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
	public void set(double m00, double m01, double m10, double m11) {
		this.m00 = m00;
		this.m01 = m01;

		this.m10 = m10;
		this.m11 = m11;

		this.isIdentity = null;
	}

	/**
	 * Computes the determinant of this matrix.
	 *
	 * @return the determinant of the matrix
	 */
	@Pure
	public final double determinant() {
		return this.m00 * this.m11 - this.m01 * this.m10;
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

		this.m10 *= scalar;
		this.m11 *= scalar;

		this.isIdentity = null;
	}

	/**
	 * Multiplies each element of matrix m1 by a scalar and places the result
	 * into this. Matrix m1 is not modified.
	 *
	 * @param scalar
	 *            the scalar multiplier
	 * @param matrix
	 *            the original matrix
	 */
	public final void mul(double scalar, Matrix2d matrix) {
		assert matrix != null : AssertMessages.notNullParameter(1);
		this.m00 = scalar * matrix.m00;
		this.m01 = scalar * matrix.m01;

		this.m10 = scalar * matrix.m10;
		this.m11 = scalar * matrix.m11;

		this.isIdentity = null;
	}

	/**
	 * Sets the value of this matrix to the result of multiplying itself with
	 * matrix m1.
	 *
	 * @param matrix
	 *            the other matrix
	 */
	public final void mul(Matrix2d matrix) {
		assert matrix != null : AssertMessages.notNullParameter();

		final double m00 = this.m00 * matrix.m00 + this.m01 * matrix.m10;
		final double m01 = this.m00 * matrix.m01 + this.m01 * matrix.m11;

		final double m10 = this.m10 * matrix.m00 + this.m11 * matrix.m10;
		final double m11 = this.m10 * matrix.m01 + this.m11 * matrix.m11;

		this.m00 = m00;
		this.m01 = m01;
		this.m10 = m10;
		this.m11 = m11;

		this.isIdentity = null;
	}

	/**
	 * Multiply this matrix by the given vector v and set the resulting vector.
	 *
	 * @param vector the input vector
	 * @param result is set with (this * v).
	 */
	@Pure
	public final void mul(Tuple2D<?> vector, Tuple2D<?> result) {
		assert vector != null : AssertMessages.notNullParameter(0);
		assert result != null : AssertMessages.notNullParameter(1);
		result.set(
				this.m00 * vector.getX() + this.m01 * vector.getY(),
				this.m10 * vector.getX() + this.m11 * vector.getY());
	}

	/**
	 * Sets the value of this matrix to the result of multiplying the two
	 * argument matrices together.
	 *
	 * @param matrix1
	 *            the first matrix
	 * @param matrix2
	 *            the second matrix
	 */
	public final void mul(Matrix2d matrix1, Matrix2d matrix2) {
		assert matrix1 != null : AssertMessages.notNullParameter(0);
		assert matrix2 != null : AssertMessages.notNullParameter(1);
		if (this != matrix1 && this != matrix2) {
			this.m00 = matrix1.m00 * matrix2.m00 + matrix1.m01 * matrix2.m10;
			this.m01 = matrix1.m00 * matrix2.m01 + matrix1.m01 * matrix2.m11;

			this.m10 = matrix1.m10 * matrix2.m00 + matrix1.m11 * matrix2.m10;
			this.m11 = matrix1.m10 * matrix2.m01 + matrix1.m11 * matrix2.m11;
		} else {
			final double m00 = matrix1.m00 * matrix2.m00 + matrix1.m01 * matrix2.m10;
			final double m01 = matrix1.m00 * matrix2.m01 + matrix1.m01 * matrix2.m11;

			final double m10 = matrix1.m10 * matrix2.m00 + matrix1.m11 * matrix2.m10;
			final double m11 = matrix1.m10 * matrix2.m01 + matrix1.m11 * matrix2.m11;

			this.m00 = m00;
			this.m01 = m01;
			this.m10 = m10;
			this.m11 = m11;
		}

		this.isIdentity = null;
	}

	/**
	 * Multiplies the transpose of matrix m1 times the transpose of matrix m2,
	 * and places the result into this.
	 *
	 * @param matrix1
	 *            the matrix on the left hand side of the multiplication
	 * @param matrix2
	 *            the matrix on the right hand side of the multiplication
	 */
	public final void mulTransposeBoth(Matrix2d matrix1, Matrix2d matrix2) {
		assert matrix1 != null : AssertMessages.notNullParameter(0);
		assert matrix2 != null : AssertMessages.notNullParameter(1);
		if (this != matrix1 && this != matrix2) {
			this.m00 = matrix1.m00 * matrix2.m00 + matrix1.m10 * matrix2.m01;
			this.m01 = matrix1.m00 * matrix2.m10 + matrix1.m10 * matrix2.m11;

			this.m10 = matrix1.m01 * matrix2.m00 + matrix1.m11 * matrix2.m01;
			this.m11 = matrix1.m01 * matrix2.m10 + matrix1.m11 * matrix2.m11;
		} else {
			final double m00 = matrix1.m00 * matrix2.m00 + matrix1.m10 * matrix2.m01;
			final double m01 = matrix1.m00 * matrix2.m10 + matrix1.m10 * matrix2.m11;

			final double m10 = matrix1.m01 * matrix2.m00 + matrix1.m11 * matrix2.m01;
			final double m11 = matrix1.m01 * matrix2.m10 + matrix1.m11 * matrix2.m11;

			this.m00 = m00;
			this.m01 = m01;
			this.m10 = m10;
			this.m11 = m11;
		}

		this.isIdentity = null;
	}

	/**
	 * Multiplies matrix m1 times the transpose of matrix m2, and places the
	 * result into this.
	 *
	 * @param matrix1
	 *            the matrix on the left hand side of the multiplication
	 * @param matrix2
	 *            the matrix on the right hand side of the multiplication
	 */
	public final void mulTransposeRight(Matrix2d matrix1, Matrix2d matrix2) {
		assert matrix1 != null : AssertMessages.notNullParameter(0);
		assert matrix2 != null : AssertMessages.notNullParameter(1);
		if (this != matrix1 && this != matrix2) {
			this.m00 = matrix1.m00 * matrix2.m00 + matrix1.m01 * matrix2.m01;
			this.m01 = matrix1.m00 * matrix2.m10 + matrix1.m01 * matrix2.m11;

			this.m10 = matrix1.m10 * matrix2.m00 + matrix1.m11 * matrix2.m01;
			this.m11 = matrix1.m10 * matrix2.m10 + matrix1.m11 * matrix2.m11;
		} else {
			final double m00 = matrix1.m00 * matrix2.m00 + matrix1.m01 * matrix2.m01;
			final double m01 = matrix1.m00 * matrix2.m10 + matrix1.m01 * matrix2.m11;

			final double m10 = matrix1.m10 * matrix2.m00 + matrix1.m11 * matrix2.m01;
			final double m11 = matrix1.m10 * matrix2.m10 + matrix1.m11 * matrix2.m11;

			this.m00 = m00;
			this.m01 = m01;
			this.m10 = m10;
			this.m11 = m11;
		}

		this.isIdentity = null;
	}

	/**
	 * Multiplies the transpose of matrix m1 times matrix m2, and places the
	 * result into this.
	 *
	 * @param matrix1
	 *            the matrix on the left hand side of the multiplication
	 * @param matrix2
	 *            the matrix on the right hand side of the multiplication
	 */
	public final void mulTransposeLeft(Matrix2d matrix1, Matrix2d matrix2) {
		assert matrix1 != null : AssertMessages.notNullParameter(0);
		assert matrix2 != null : AssertMessages.notNullParameter(1);
		if (this != matrix1 && this != matrix2) {
			this.m00 = matrix1.m00 * matrix2.m00 + matrix1.m10 * matrix2.m10;
			this.m01 = matrix1.m00 * matrix2.m01 + matrix1.m10 * matrix2.m11;

			this.m10 = matrix1.m01 * matrix2.m00 + matrix1.m11 * matrix2.m10;
			this.m11 = matrix1.m01 * matrix2.m01 + matrix1.m11 * matrix2.m11;
		} else {
			final double m00 = matrix1.m00 * matrix2.m00 + matrix1.m10 * matrix2.m10;
			final double m01 = matrix1.m00 * matrix2.m01 + matrix1.m10 * matrix2.m11;

			final double m10 = matrix1.m01 * matrix2.m00 + matrix1.m11 * matrix2.m10;
			final double m11 = matrix1.m01 * matrix2.m01 + matrix1.m11 * matrix2.m11;

			this.m00 = m00;
			this.m01 = m01;
			this.m10 = m10;
			this.m11 = m11;
		}

		this.isIdentity = null;
	}

	/**
	 * Perform cross product normalization of this matrix.
	 */
	public final void normalizeCP() {
		double mag = 1.0 / Math.sqrt(this.m00 * this.m00 + this.m10 * this.m10);
		this.m00 = this.m00 * mag;
		this.m10 = this.m10 * mag;

		mag = 1.0 / Math.sqrt(this.m01 * this.m01 + this.m11 * this.m11);
		this.m01 = this.m01 * mag;
		this.m11 = this.m11 * mag;

		this.isIdentity = null;
	}

	/**
	 * Perform cross product normalization of matrix m1 and place the normalized
	 * values into this.
	 *
	 * @param matrix
	 *            Provides the matrix values to be normalized
	 */
	public final void normalizeCP(Matrix2d matrix) {
		assert matrix != null : AssertMessages.notNullParameter();
		double mag = 1.0 / Math.sqrt(matrix.m00 * matrix.m00 + matrix.m10 * matrix.m10);
		this.m00 = matrix.m00 * mag;
		this.m10 = matrix.m10 * mag;

		mag = 1.0 / Math.sqrt(matrix.m01 * matrix.m01 + matrix.m11 * matrix.m11);
		this.m01 = matrix.m01 * mag;
		this.m11 = matrix.m11 * mag;

		this.isIdentity = null;
	}

	/**
	 * Returns true if all of the data members of Matrix2f m1 are equal to the
	 * corresponding data members in this Matrix2f.
	 *
	 * @param matrix
	 *            the matrix with which the comparison is made
	 * @return true or false
	 */
	@Pure
	public boolean equals(Matrix2d matrix) {
		try {
			return this.m00 == matrix.m00 && this.m01 == matrix.m01
					&& this.m10 == matrix.m10
					&& this.m11 == matrix.m11;
		} catch (NullPointerException e2) {
			return false;
		}
	}

	/**
	 * Returns true if the Object t1 is of type Matrix2f and all of the data
	 * members of t1 are equal to the corresponding data members in this
	 * Matrix2f.
	 *
	 * @param object
	 *            the matrix with which the comparison is made
	 * @return true or false
	 */
	@Pure
	@Override
	public boolean equals(Object object) {
		if (object == this) {
			return true;
		}
		if (getClass().isInstance(object)) {
			final Matrix2d m2 = (Matrix2d) object;
			return this.m00 == m2.m00 && this.m01 == m2.m01
					&& this.m10 == m2.m10
					&& this.m11 == m2.m11;
        }
		return false;
	}

	/**
	 * Returns a hash code value based on the data values in this object. Two
	 * different Matrix2f objects with identical data values (i.e.,
	 * Matrix2f.equals returns true) will return the same hash code value. Two
	 * objects with different data members may return the same hash value,
	 * although this is not likely.
	 *
	 * @return the integer hash code value
	 */
	@Pure
	@Override
	public int hashCode() {
		long bits = 1L;
		bits = 31L * bits + Double.hashCode(this.m00);
		bits = 31L * bits + Double.hashCode(this.m01);
		bits = 31L * bits + Double.hashCode(this.m10);
		bits = 31L * bits + Double.hashCode(this.m11);
		return (int) (bits ^ (bits >> 31));
	}

	/**
	 * Sets this matrix to all zeros.
	 */
	public final void setZero() {
		this.m00 = 0.;
		this.m01 = 0.;

		this.m10 = 0.;
		this.m11 = 0.;

		this.isIdentity = Boolean.FALSE;
	}

	/**
	 * Sets this matrix as diagonal.
	 *
	 * @param m00
	 *            the first element of the diagonal
	 * @param m11
	 *            the second element of the diagonal
	 */
	public final void setDiagonal(double m00, double m11) {
		this.m00 = m00;
		this.m01 = 0.;
		this.m10 = 0.;
		this.m11 = m11;

		this.isIdentity = null;
	}

	/**
	 * Negates the value of this matrix: this = -this.
	 */
	public final void negate() {
		this.m00 = -this.m00;
		this.m01 = -this.m01;

		this.m10 = -this.m10;
		this.m11 = -this.m11;

		this.isIdentity = null;
	}

	/**
	 * Sets the value of this matrix equal to the negation of of the Matrix2f
	 * parameter.
	 *
	 * @param matrix
	 *            the source matrix
	 */
	public final void negate(Matrix2d matrix) {
		assert matrix != null : AssertMessages.notNullParameter();
		this.m00 = -matrix.m00;
		this.m01 = -matrix.m01;

		this.m10 = -matrix.m10;
		this.m11 = -matrix.m11;

		this.isIdentity = null;
	}

	/**
	 * Creates a new object of the same class as this object.
	 *
	 * @return a clone of this instance.
	 * @exception OutOfMemoryError
	 *                if there is not enough memory.
	 * @see java.lang.Cloneable
	 */
	@Pure
	@Override
	public Matrix2d clone() {
		Matrix2d m1 = null;
		try {
			m1 = (Matrix2d) super.clone();
		} catch (CloneNotSupportedException e) {
			// this shouldn't happen, since we are Cloneable
			throw new InternalError(e);
		}

		// Also need to create new tmp arrays (no need to actually clone them)
		return m1;
	}

	/**
	 * Get the first matrix element in the first row.
	 *
	 * @return Returns the m00.
	 */
	@Pure
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
	@Pure
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
	 * Get first matrix element in the second row.
	 *
	 * @return Returns the m10.
	 */
	@Pure
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
	@Pure
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
		this.isIdentity = null;
	}

	/** Set this matrix with the covariance matrix's elements for the given
	 * set of tuples.
	 *
	 * @param result the mean of the tuples.
	 * @param tuples the input tuples.
	 * @return <code>true</code> if the covariance matrix could be computed.
	 */
	public final boolean cov(Vector2D<?, ?> result, Vector2D<?, ?>... tuples) {
		assert result != null : AssertMessages.notNullParameter(0);
		assert tuples != null : AssertMessages.notNullParameter(1);
		return cov(result, Arrays.asList(tuples));
	}

	/** Set this matrix with the covariance matrix's elements for the given
	 * set of tuples.
	 *
	 * @param result the mean of the tuples.
	 * @param tuples the input tuples.
	 * @return <code>true</code> if the covariance matrix could be computed.
	 */
	public final boolean cov(Vector2D<?, ?> result, Point2D<?, ?>... tuples) {
		assert result != null : AssertMessages.notNullParameter(0);
		assert tuples != null : AssertMessages.notNullParameter(1);
		return cov(result, Arrays.asList(tuples));
	}

	/** Set this matrix with the covariance matrix's elements for the given
	 * set of tuples.
	 *
	 * @param result the mean of the tuples.
	 * @param tuples the input tuples.
	 * @return <code>true</code> if the covariance matrix could be computed.
	 */
	public boolean cov(Vector2D<?, ?> result, Iterable<? extends Tuple2D<?>> tuples) {
		assert result != null : AssertMessages.notNullParameter(0);
		assert tuples != null : AssertMessages.notNullParameter(1);
		setZero();

		// Compute the mean m
		double mx = 0;
		double my = 0;
		int count = 0;
		for (final Tuple2D<?> p : tuples) {
			mx += p.getX();
			my += p.getY();
			++count;
		}

		if (count == 0) {
			return false;
		}

		final double scale = 1. / count;
		mx *= scale;
		my *= scale;

		// Compute the covariance term [Gottshalk2000]
		// c_ij = sum(p'_i * p'_j) / n
		// c_ij = sum((p_i - m_i) * (p_j - m_j)) / n
		for (final Tuple2D<?> p : tuples) {
			this.m00 += (p.getX() - mx) * (p.getX() - mx);
			this.m01 += (p.getX() - mx) * (p.getY() - my);
			//cov.m10 += (p.getY() - my) * (p.getX() - mx); // same as m01
			this.m11 += (p.getY() - my) * (p.getY() - my);
		}

		this.m00 /= count;
		this.m01 /= count;
		this.m10 = this.m01;
		this.m11 /= count;

		result.set(mx, my);

		this.isIdentity = null;

		return true;
	}

	/** Replies if the matrix is symmetric.
	 *
	 * @return <code>true</code> if the matrix is symmetric, otherwise
	 * <code>false</code>
	 */
	@Pure
	public boolean isSymmetric() {
		return	this.m01 == this.m10;
	}

	/**
	 * Compute the eigenvectors of this matrix, assuming it is a symmetric matrix
	 * according to the Jacobi Cyclic Method.
	 *
	 * @param eigenVectors are the matrix of vectors to fill. Eigen vectors are the
	 *     columns of the matrix.
	 * @return the eigenvalues which are corresponding to the {@code eigenVectors} columns.
	 * @see #eigenVectorsOfSymmetricMatrix(Matrix2d)
	 */
	public double[] eigenVectorsOfSymmetricMatrix(Matrix2d eigenVectors) {
		assert eigenVectors != null : AssertMessages.notNullParameter();

		// Copy values up to the diagonal
		double m11 = getM00();
		double m12 = getM01();
		double m22 = getM11();

		eigenVectors.setIdentity();

		boolean sweepsConsumed = true;

		for (int a = 0; a < JACOBI_MAX_SWEEPS; ++a) {

			// Exit loop if off-diagonal entries are small enough
			if (Math.abs(m12) < JACOBI_EPSILON) {
				sweepsConsumed = false;
				break;
			}

			// Annihilate (1, 2) entry
			if (m12 != 0.) {
				final double u = (m22 - m11) * .5 / m12;
				final double u2 = u * u;
				final double u2p1 = u2 + 1.;

				final double t;
				if (u2p1 != u2) {
					t = Math.signum(u) * (Math.sqrt(u2p1) - Math.abs(u));
				} else {
					t = .5 / u;
				}

				final double c = 1. / Math.sqrt(t * t + 1);
				final double s = c * t;

				m11 -= t * m12;
				m22 += t * m12;
				m12 = 0.;

				for (int i = 0; i < 2; ++i) {
					final double ri0 = eigenVectors.getElement(i, 0);
					final double ri1 = eigenVectors.getElement(i, 1);
					eigenVectors.setElement(i, 0, c * ri0 - s * ri1);
					eigenVectors.setElement(i, 1, s * ri0 + c * ri1);
				}
			}
		}

		assert !sweepsConsumed;

		// eigenvalues are on the diagonal
		return new double[] {m11, m22};
	}

	/** Replies if the matrix is identity.
	 *
	 * <p>This function uses the equal-to-zero test with the error {@link Math#ulp(double)}.
	 *
	 * @return <code>true</code> if the matrix is identity; <code>false</code> otherwise.
	 * @see MathUtil#isEpsilonZero(double)
	 * @see MathUtil#isEpsilonEqual(double, double)
	 */
	@Pure
	public boolean isIdentity() {
		if (this.isIdentity == null) {
			this.isIdentity = MathUtil.isEpsilonEqual(this.m00, 1.)
				&& MathUtil.isEpsilonZero(this.m01)
				&& MathUtil.isEpsilonZero(this.m10)
				&& MathUtil.isEpsilonEqual(this.m11, 1.);
		}
		return this.isIdentity.booleanValue();
	}

	/** Add the given matrix to this matrix: {@code this += matrix}
	 *
	 * <p>This function is an implementation of the "-" operator for
	 * the languages that defined or based on the
	 * <a href="https://www.eclipse.org/Xtext/">Xtext framework</a>.
	 *
	 * @param matrix the matrix.
	 * @see #add(Matrix2d)
	 */
	public void operator_add(Matrix2d matrix) {
		add(matrix);
	}

	/** Add the given scalar to this matrix: {@code this += scalar}
	 *
	 * <p>This function is an implementation of the "-" operator for
	 * the languages that defined or based on the
	 * <a href="https://www.eclipse.org/Xtext/">Xtext framework</a>.
	 *
	 * @param scalar the scalar.
	 * @see #add(double)
	 */
	public void operator_add(double scalar) {
		add(scalar);
	}

	/** Substract the given matrix to this matrix: {@code this -= matrix}
	 *
	 * <p>This function is an implementation of the "-" operator for
	 * the languages that defined or based on the
	 * <a href="https://www.eclipse.org/Xtext/">Xtext framework</a>.
	 *
	 * @param matrix the matrix.
	 * @see #sub(Matrix2d)
	 */
	public void operator_remove(Matrix2d matrix) {
		sub(matrix);
	}

	/** Substract the given scalar to this matrix: {@code this -= scalar}
	 *
	 * <p>This function is an implementation of the "-" operator for
	 * the languages that defined or based on the
	 * <a href="https://www.eclipse.org/Xtext/">Xtext framework</a>.
	 *
	 * @param scalar the scalar.
	 * @see #add(double)
	 */
	public void operator_remove(double scalar) {
		add(-scalar);
	}

	/** Replies the addition of the given matrix to this matrix: {@code this + matrix}
	 *
	 * <p>This function is an implementation of the "-" operator for
	 * the languages that defined or based on the
	 * <a href="https://www.eclipse.org/Xtext/">Xtext framework</a>.
	 *
	 * @param matrix the matrix.
	 * @return the sum of the matrices.
	 * @see #add(Matrix2d)
	 */
	@Pure
	public Matrix2d operator_plus(Matrix2d matrix) {
		final Matrix2d result = new Matrix2d();
		result.add(this, matrix);
		return result;
	}

	/** Replies the addition of the given scalar to this matrix: {@code this + scalar}
	 *
	 * <p>This function is an implementation of the "-" operator for
	 * the languages that defined or based on the
	 * <a href="https://www.eclipse.org/Xtext/">Xtext framework</a>.
	 *
	 * @param scalar the scalar.
	 * @return the sum of the matrix and the scalar.
	 * @see #add(double)
	 */
	@Pure
	public Matrix2d operator_plus(double scalar) {
		final Matrix2d result = new Matrix2d();
		result.add(scalar, this);
		return result;
	}

	/** Replies the substraction of the given matrix to this matrix: {@code this - matrix}
	 *
	 * <p>This function is an implementation of the "-" operator for
	 * the languages that defined or based on the
	 * <a href="https://www.eclipse.org/Xtext/">Xtext framework</a>.
	 *
	 * @param matrix the matrix.
	 * @return the result of the substraction.
	 * @see #sub(Matrix2d)
	 */
	@Pure
	public Matrix2d operator_minus(Matrix2d matrix) {
		final Matrix2d result = new Matrix2d();
		result.sub(this, matrix);
		return result;
	}

	/** Replies the substraction of the given scalar to this matrix: {@code this - scalar}
	 *
	 * <p>This function is an implementation of the "-" operator for
	 * the languages that defined or based on the
	 * <a href="https://www.eclipse.org/Xtext/">Xtext framework</a>.
	 *
	 * @param scalar the scalar.
	 * @return the result of the substraction.
	 * @see #add(double)
	 */
	@Pure
	public Matrix2d operator_minus(double scalar) {
		final Matrix2d result = new Matrix2d();
		result.add(-scalar, this);
		return result;
	}

	/** Replies the negation of this matrix: {@code -this}
	 *
	 * <p>This function is an implementation of the "-" operator for
	 * the languages that defined or based on the
	 * <a href="https://www.eclipse.org/Xtext/">Xtext framework</a>.
	 *
	 * @return the negation of this matrix.
	 * @see #negate()
	 */
	@Pure
	public Matrix2d operator_minus() {
		final Matrix2d result = new Matrix2d();
		result.negate(this);
		return result;
	}

	/** Replies the multiplication of the given matrix and this matrix: {@code this * matrix}
	 *
	 * <p>This function is an implementation of the "-" operator for
	 * the languages that defined or based on the
	 * <a href="https://www.eclipse.org/Xtext/">Xtext framework</a>.
	 *
	 * @param matrix the matrix.
	 * @return the multiplication of the matrices.
	 * @see #mul(Matrix2d)
	 */
	@Pure
	public Matrix2d operator_multiply(Matrix2d matrix) {
		final Matrix2d result = new Matrix2d();
		result.mul(this, matrix);
		return result;
	}

	/** Replies the multiplication of the given scalar and this matrix: {@code this * scalar}
	 *
	 * <p>This function is an implementation of the "-" operator for
	 * the languages that defined or based on the
	 * <a href="https://www.eclipse.org/Xtext/">Xtext framework</a>.
	 *
	 * @param scalar the scalar.
	 * @return the multiplication of the scalar and the matrix.
	 * @see #mul(Matrix2d)
	 */
	@Pure
	public Matrix2d operator_multiply(double scalar) {
		final Matrix2d result = new Matrix2d();
		result.mul(scalar, this);
		return result;
	}

	/** Replies the division of this matrix by the given scalar: {@code this / scalar}
	 *
	 * <p>This function is an implementation of the "-" operator for
	 * the languages that defined or based on the
	 * <a href="https://www.eclipse.org/Xtext/">Xtext framework</a>.
	 *
	 * @param scalar the scalar.
	 * @return the division of the matrix by the scalar.
	 * @see #mul(double)
	 */
	@Pure
	public Matrix2d operator_divide(double scalar) {
		final Matrix2d result = new Matrix2d();
		result.mul(1. / scalar, this);
		return result;
	}

	/** Increment this matrix: {@code this++}
	 *
	 * <p>This function is an implementation of the "-" operator for
	 * the languages that defined or based on the
	 * <a href="https://www.eclipse.org/Xtext/">Xtext framework</a>.
	 *
	 * @see #add(double)
	 */
	public void operator_plusPlus() {
		add(1);
	}

	/** Increment this matrix: {@code this--}
	 *
	 * <p>This function is an implementation of the "-" operator for
	 * the languages that defined or based on the
	 * <a href="https://www.eclipse.org/Xtext/">Xtext framework</a>.
	 *
	 * @see #add(double)
	 */
	public void operator_moinsMoins() {
		add(-1);
	}

	/** Replies the transposition of this matrix: {@code !this}
	 *
	 * <p>This function is an implementation of the "-" operator for
	 * the languages that defined or based on the
	 * <a href="https://www.eclipse.org/Xtext/">Xtext framework</a>.
	 *
	 * @return the transpose
	 * @see #add(double)
	 */
	public Matrix2d operator_not() {
		final Matrix2d result = new Matrix2d();
		result.transpose(this);
		return result;
	}

}
