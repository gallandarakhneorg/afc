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

import java.io.Serializable;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.MathUtil;
import org.arakhne.afc.math.extensions.xtext.MatrixExtensions;
import org.arakhne.afc.vmutil.annotations.ScalaOperator;
import org.arakhne.afc.vmutil.annotations.XtextOperator;
import org.arakhne.afc.vmutil.asserts.AssertMessages;

/**
 * Is represented internally as a 4x4 floating point matrix. The mathematical
 * representation is row major, as in traditional matrix mathematics.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@SuppressWarnings({"checkstyle:methodcount", "checkstyle:magicnumber"})
public class Matrix4d implements Serializable, Cloneable {

    private static final long serialVersionUID = 7216873052550769543L;

    /**
     * The first matrix element in the first row.
     */
    protected double m00;

    /**
     * The second matrix element in the first row.
     */
    protected double m01;

    /**
     * The third matrix element in the first row.
     */
    protected double m02;

    /**
     * The fourth matrix element in the first row.
     */
    protected double m03;

    /**
     * The first matrix element in the second row.
     */
    protected double m10;

    /**
     * The second matrix element in the second row.
     */
    protected double m11;

    /**
     * The third matrix element in the second row.
     */
    protected double m12;

    /**
     * The fourth matrix element in the second row.
     */
    protected double m13;

    /**
     * The first matrix element in the third row.
     */
    protected double m20;

    /**
     * The second matrix element in the third row.
     */
    protected double m21;

    /**
     * The third matrix element in the third row.
     */
    protected double m22;

    /**
     * The fourth matrix element in the third row.
     */
    protected double m23;

    /**
     * The first matrix element in the fourth row.
     */
    protected double m30;

    /**
     * The second matrix element in the fourth row.
     */
    protected double m31;

    /**
     * The third matrix element in the fourth row.
     */
    protected double m32;

    /**
     * The fourth matrix element in the fourth row.
     */
    protected double m33;

    /** Indicates if the matrix is identity.
     * If <code>null</code> the identity flag must be determined.
     */
    protected Boolean isIdentity;

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
    @SuppressWarnings("checkstyle:parameternumber")
    public Matrix4d(double m00, double m01, double m02, double m03,
            double m10, double m11, double m12, double m13,
            double m20, double m21, double m22, double m23,
            double m30, double m31, double m32, double m33) {
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
    public Matrix4d(double[] v) {
        assert v != null : AssertMessages.notNullParameter();
        assert v.length >= 16 : AssertMessages.tooSmallArrayParameter(v.length, 16);
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
     * @param matrix
     *            the source matrix
     */
    public Matrix4d(Matrix4d matrix) {
        assert matrix != null : AssertMessages.notNullParameter();
        this.m00 = matrix.m00;
        this.m01 = matrix.m01;
        this.m02 = matrix.m02;
        this.m03 = matrix.m03;

        this.m10 = matrix.m10;
        this.m11 = matrix.m11;
        this.m12 = matrix.m12;
        this.m13 = matrix.m13;

        this.m20 = matrix.m20;
        this.m21 = matrix.m21;
        this.m22 = matrix.m22;
        this.m23 = matrix.m23;

        this.m30 = matrix.m30;
        this.m31 = matrix.m31;
        this.m32 = matrix.m32;
        this.m33 = matrix.m33;
    }

    /**
     * Constructs and initializes a Matrix4f to all zeros.
     */
    public Matrix4d() {
        this.m00 = 0.;
        this.m01 = 0.;
        this.m02 = 0.;
        this.m03 = 0.;

        this.m10 = 0.;
        this.m11 = 0.;
        this.m12 = 0.;
        this.m13 = 0.;

        this.m20 = 0.;
        this.m21 = 0.;
        this.m22 = 0.;
        this.m23 = 0.;

        this.m30 = 0.;
        this.m31 = 0.;
        this.m32 = 0.;
        this.m33 = 0.;
    }

    /**
     * Returns a string that contains the values of this Matrix4f.
     *
     * @return the String representation
     */
    @Pure
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
        this.m00 = 1.;
        this.m01 = 0.;
        this.m02 = 0.;
        this.m03 = 0.;

        this.m10 = 0.;
        this.m11 = 1.;
        this.m12 = 0.;
        this.m13 = 0.;

        this.m20 = 0.;
        this.m21 = 0.;
        this.m22 = 1.;
        this.m23 = 0.;

        this.m30 = 0.;
        this.m31 = 0.;
        this.m32 = 0.;
        this.m33 = 1.;

        this.isIdentity = Boolean.TRUE;
    }

    /**
     * Sets the specified element of this matrix3. to the value provided.
     *
     * @param row
     *            the row number to be modified (zero indexed)
     * @param column
     *            the column number to be modified (zero indexed)
     * @param value
     *            the new value
     */
    @SuppressWarnings("checkstyle:cyclomaticcomplexity")
    public final void setElement(int row, int column, double value) {
        assert row >= 0 && row < 4 : AssertMessages.outsideRangeInclusiveParameter(0, row, 0, 3);
        assert column >= 0 && column < 4 : AssertMessages.outsideRangeInclusiveParameter(1, column, 0, 3);
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
    @SuppressWarnings({"checkstyle:returncount", "checkstyle:cyclomaticcomplexity"})
    public final double getElement(int row, int column) {
        assert row >= 0 && row < 4 : AssertMessages.outsideRangeInclusiveParameter(0, row, 0, 3);
        assert column >= 0 && column < 4 : AssertMessages.outsideRangeInclusiveParameter(1, column, 0, 3);
        switch (row) {
        case 0:
            switch (column) {
            case 0:
                return this.m00;
            case 1:
                return this.m01;
            case 2:
                return this.m02;
            case 3:
                return this.m03;
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
            case 2:
                return this.m12;
            case 3:
                return this.m13;
            default:
                break;
            }
            break;

        case 2:
            switch (column) {
            case 0:
                return this.m20;
            case 1:
                return this.m21;
            case 2:
                return this.m22;
            case 3:
                return this.m23;
            default:
                break;
            }
            break;

        case 3:
            switch (column) {
            case 0:
                return this.m30;
            case 1:
                return this.m31;
            case 2:
                return this.m32;
            case 3:
                return this.m33;
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
    public final void getRow(int row, double[] v) {
        assert row >= 0 && row < 4 : AssertMessages.outsideRangeInclusiveParameter(0, row, 0, 3);
        assert v != null : AssertMessages.notNullParameter(1);
        assert v.length >= 4 : AssertMessages.tooSmallArrayParameter(1, v.length, 4);
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
    public final void getColumn(int column, double[] v) {
        assert column >= 0 && column < 4 : AssertMessages.outsideRangeInclusiveParameter(0, column, 0, 3);
        assert v != null : AssertMessages.notNullParameter(1);
        assert v.length >= 4 : AssertMessages.tooSmallArrayParameter(1, v.length, 4);
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
     * @param value1
     *            the first column element
     * @param value2
     *            the second column element
     * @param value3
     *            the third column element
     * @param value4
     *            the fourth column element
     */
    public final void setRow(int row, double value1, double value2, double value3, double value4) {
        assert row >= 0 && row < 4 : AssertMessages.outsideRangeInclusiveParameter(0, row, 0, 3);
        switch (row) {
        case 0:
            this.m00 = value1;
            this.m01 = value2;
            this.m02 = value3;
            this.m03 = value4;
            break;

        case 1:
            this.m10 = value1;
            this.m11 = value2;
            this.m12 = value3;
            this.m13 = value4;
            break;

        case 2:
            this.m20 = value1;
            this.m21 = value2;
            this.m22 = value3;
            this.m23 = value4;
            break;

        case 3:
            this.m30 = value1;
            this.m31 = value2;
            this.m32 = value3;
            this.m33 = value4;
            break;

        default:
            throw new ArrayIndexOutOfBoundsException();
        }

        this.isIdentity = null;
    }

    /**
     * Sets the specified row of this Matrix4f to the three values provided.
     *
     * @param row
     *            the row number to be modified (zero indexed)
     * @param v
     *            the replacement row
     */
    public final void setRow(int row, double[] v) {
        assert row >= 0 && row < 4 : AssertMessages.outsideRangeInclusiveParameter(0, row, 0, 3);
        assert v != null : AssertMessages.notNullParameter(1);
        assert v.length >= 4 : AssertMessages.tooSmallArrayParameter(1, v.length, 4);
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

        this.isIdentity = null;
    }

    /**
     * Sets the specified column of this Matrix4f to the three values provided.
     *
     * @param column
     *            the column number to be modified (zero indexed)
     * @param value1
     *            the first row element
     * @param value2
     *            the second row element
     * @param value3
     *            the third row element
     * @param value4
     *            the fourth row element
     */
    public final void setColumn(int column, double value1, double value2, double value3, double value4) {
        assert column >= 0 && column < 4 : AssertMessages.outsideRangeInclusiveParameter(0, column, 0, 3);
        switch (column) {
        case 0:
            this.m00 = value1;
            this.m10 = value2;
            this.m20 = value3;
            this.m30 = value4;
            break;

        case 1:
            this.m01 = value1;
            this.m11 = value2;
            this.m21 = value3;
            this.m31 = value4;
            break;

        case 2:
            this.m02 = value1;
            this.m12 = value2;
            this.m22 = value3;
            this.m32 = value4;
            break;

        case 3:
            this.m03 = value1;
            this.m13 = value2;
            this.m23 = value3;
            this.m33 = value4;
            break;

        default:
            throw new ArrayIndexOutOfBoundsException();
        }

        this.isIdentity = null;
    }

    /**
     * Sets the specified column of this Matrix4f to the three values provided.
     *
     * @param column
     *            the column number to be modified (zero indexed)
     * @param v
     *            the replacement column
     */
    public final void setColumn(int column, double[] v) {
        assert column >= 0 && column < 4 : AssertMessages.outsideRangeInclusiveParameter(0, column, 0, 3);
        assert v != null : AssertMessages.notNullParameter(1);
        assert v.length >= 4 : AssertMessages.tooSmallArrayParameter(1, v.length, 4);
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
    public final void add(double scalar, Matrix4d matrix) {
        assert matrix != null : AssertMessages.notNullParameter(1);
        this.m00 = matrix.m00 + scalar;
        this.m01 = matrix.m01 + scalar;
        this.m02 = matrix.m02 + scalar;
        this.m03 = matrix.m03 + scalar;

        this.m10 = matrix.m10 + scalar;
        this.m11 = matrix.m11 + scalar;
        this.m12 = matrix.m12 + scalar;
        this.m13 = matrix.m13 + scalar;

        this.m20 = matrix.m20 + scalar;
        this.m21 = matrix.m21 + scalar;
        this.m22 = matrix.m22 + scalar;
        this.m23 = matrix.m23 + scalar;

        this.m30 = matrix.m30 + scalar;
        this.m31 = matrix.m31 + scalar;
        this.m32 = matrix.m32 + scalar;
        this.m33 = matrix.m33 + scalar;

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
    public final void add(Matrix4d matrix1, Matrix4d matrix2) {
        assert matrix1 != null : AssertMessages.notNullParameter(0);
        assert matrix2 != null : AssertMessages.notNullParameter(1);
        this.m00 = matrix1.m00 + matrix2.m00;
        this.m01 = matrix1.m01 + matrix2.m01;
        this.m02 = matrix1.m02 + matrix2.m02;
        this.m03 = matrix1.m03 + matrix2.m03;

        this.m10 = matrix1.m10 + matrix2.m10;
        this.m11 = matrix1.m11 + matrix2.m11;
        this.m12 = matrix1.m12 + matrix2.m12;
        this.m13 = matrix1.m13 + matrix2.m13;

        this.m20 = matrix1.m20 + matrix2.m20;
        this.m21 = matrix1.m21 + matrix2.m21;
        this.m22 = matrix1.m22 + matrix2.m22;
        this.m23 = matrix1.m23 + matrix2.m23;

        this.m30 = matrix1.m30 + matrix2.m30;
        this.m31 = matrix1.m31 + matrix2.m31;
        this.m32 = matrix1.m32 + matrix2.m32;
        this.m33 = matrix1.m33 + matrix2.m33;

        this.isIdentity = null;
    }

    /**
     * Sets the value of this matrix to the sum of itself and matrix m1.
     *
     * @param matrix
     *            the other matrix
     */
    public final void add(Matrix4d matrix) {
        assert matrix != null : AssertMessages.notNullParameter();
        this.m00 += matrix.m00;
        this.m01 += matrix.m01;
        this.m02 += matrix.m02;
        this.m03 += matrix.m03;

        this.m10 += matrix.m10;
        this.m11 += matrix.m11;
        this.m12 += matrix.m12;
        this.m13 += matrix.m13;

        this.m20 += matrix.m20;
        this.m21 += matrix.m21;
        this.m22 += matrix.m22;
        this.m23 += matrix.m23;

        this.m30 += matrix.m30;
        this.m31 += matrix.m31;
        this.m32 += matrix.m32;
        this.m33 += matrix.m33;

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
    public final void sub(Matrix4d matrix1, Matrix4d matrix2) {
        assert matrix1 != null : AssertMessages.notNullParameter(0);
        assert matrix2 != null : AssertMessages.notNullParameter(1);
        this.m00 = matrix1.m00 - matrix2.m00;
        this.m01 = matrix1.m01 - matrix2.m01;
        this.m02 = matrix1.m02 - matrix2.m02;
        this.m03 = matrix1.m03 - matrix2.m03;

        this.m10 = matrix1.m10 - matrix2.m10;
        this.m11 = matrix1.m11 - matrix2.m11;
        this.m12 = matrix1.m12 - matrix2.m12;
        this.m13 = matrix1.m13 - matrix2.m13;

        this.m20 = matrix1.m20 - matrix2.m20;
        this.m21 = matrix1.m21 - matrix2.m21;
        this.m22 = matrix1.m22 - matrix2.m22;
        this.m23 = matrix1.m23 - matrix2.m23;

        this.m30 = matrix1.m30 - matrix2.m30;
        this.m31 = matrix1.m31 - matrix2.m31;
        this.m32 = matrix1.m32 - matrix2.m32;
        this.m33 = matrix1.m33 - matrix2.m33;

        this.isIdentity = null;
    }

    /**
     * Sets the value of this matrix to the matrix difference of itself and
     * matrix m1 (this = this - m1).
     *
     * @param matrix
     *            the other matrix
     */
    public final void sub(Matrix4d matrix) {
        assert matrix != null : AssertMessages.notNullParameter();
        this.m00 -= matrix.m00;
        this.m01 -= matrix.m01;
        this.m02 -= matrix.m02;
        this.m03 -= matrix.m03;

        this.m10 -= matrix.m10;
        this.m11 -= matrix.m11;
        this.m12 -= matrix.m12;
        this.m13 -= matrix.m13;

        this.m20 -= matrix.m20;
        this.m21 -= matrix.m21;
        this.m22 -= matrix.m22;
        this.m23 -= matrix.m23;

        this.m30 -= matrix.m30;
        this.m31 -= matrix.m31;
        this.m32 -= matrix.m32;
        this.m33 -= matrix.m33;

        this.isIdentity = null;
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

        this.isIdentity = null;
    }

    /**
     * Sets the value of this matrix to the transpose of the argument matrix.
     *
     * @param m1
     *            the matrix to be transposed
     */
    public final void transpose(Matrix4d m1) {
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
        } else {
            transpose();
        }

        this.isIdentity = null;
    }

    /**
     * Sets the value of this matrix to the double value of the Matrix3.
     * argument.
     *
     * @param matrix
     *            the Matrix4f to be converted to double
     */
    public final void set(Matrix4d matrix) {
        assert matrix != null : AssertMessages.notNullParameter();
        this.m00 = matrix.m00;
        this.m01 = matrix.m01;
        this.m02 = matrix.m02;
        this.m03 = matrix.m03;

        this.m10 = matrix.m10;
        this.m11 = matrix.m11;
        this.m12 = matrix.m12;
        this.m13 = matrix.m13;

        this.m20 = matrix.m20;
        this.m21 = matrix.m21;
        this.m22 = matrix.m22;
        this.m23 = matrix.m23;

        this.m30 = matrix.m30;
        this.m31 = matrix.m31;
        this.m32 = matrix.m32;
        this.m33 = matrix.m33;

        this.isIdentity = null;
    }

    /**
     * Sets the values in this Matrix4f equal to the row-major array parameter
     * (ie, the first four elements of the array will be copied into the first
     * row of this matrix, etc.).
     *
     * @param matrix
     *            the double precision array of length 16
     */
    public final void set(double[] matrix) {
        assert matrix != null : AssertMessages.notNullParameter();
        assert matrix.length >= 16 : AssertMessages.tooSmallArrayParameter(1, matrix.length, 16);
        this.m00 = matrix[0];
        this.m01 = matrix[1];
        this.m02 = matrix[2];
        this.m03 = matrix[3];

        this.m10 = matrix[4];
        this.m11 = matrix[5];
        this.m12 = matrix[6];
        this.m13 = matrix[7];

        this.m20 = matrix[8];
        this.m21 = matrix[9];
        this.m22 = matrix[10];
        this.m23 = matrix[11];

        this.m30 = matrix[12];
        this.m31 = matrix[13];
        this.m32 = matrix[14];
        this.m33 = matrix[15];

        this.isIdentity = null;
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
    @SuppressWarnings("checkstyle:parameternumber")
    public void set(double m00, double m01, double m02, double m03,
            double m10, double m11, double m12, double m13,
            double m20, double m21, double m22, double m23,
            double m30, double m31, double m32, double m33) {
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

        this.isIdentity = null;
    }

    /**
     * Computes the determinant of this matrix.
     *
     * @return the determinant of the matrix
     */
    @Pure
    public final double determinant() {
        final double det1 = this.m22 * this.m33 - this.m23 * this.m32;
        final double det2 = this.m12 * this.m33 - this.m13 * this.m32;
        final double det3 = this.m12 * this.m23 - this.m13 * this.m22;
        final double det4 = this.m02 * this.m33 - this.m03 * this.m32;
        final double det5 = this.m02 * this.m23 - this.m03 * this.m22;
        final double det6 = this.m02 * this.m13 - this.m03 * this.m12;
        return
                this.m00 * (
                        this.m11 * det1
                        - this.m21 * det2
                        + this.m31 * det3
                        )
                - this.m10 * (
                        this.m01 * det1
                        - this.m21 * det4
                        + this.m31 * det5
                        )
                + this.m20 * (
                        this.m01 * det2
                        - this.m11 * det4
                        + this.m31 * det6
                        )
                - this.m30 * (
                        this.m01 * det3
                        - this.m11 * det5
                        + this.m21 * det6
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
    public final void mul(double scalar, Matrix4d matrix) {
        assert matrix != null : AssertMessages.notNullParameter();
        this.m00 = scalar * matrix.m00;
        this.m01 = scalar * matrix.m01;
        this.m02 = scalar * matrix.m02;
        this.m03 = scalar * matrix.m03;

        this.m10 = scalar * matrix.m10;
        this.m11 = scalar * matrix.m11;
        this.m12 = scalar * matrix.m12;
        this.m13 = scalar * matrix.m13;

        this.m20 = scalar * matrix.m20;
        this.m21 = scalar * matrix.m21;
        this.m22 = scalar * matrix.m22;
        this.m23 = scalar * matrix.m23;

        this.m30 = scalar * matrix.m30;
        this.m31 = scalar * matrix.m31;
        this.m32 = scalar * matrix.m32;
        this.m33 = scalar * matrix.m33;

        this.isIdentity = null;
    }

    /**
     * Sets the value of this matrix to the result of multiplying itself with
     * matrix m1.
     *
     * @param matrix
     *            the other matrix
     */
    public final void mul(Matrix4d matrix) {
        assert matrix != null : AssertMessages.notNullParameter();
        final double m00 = this.m00 * matrix.m00 + this.m01 * matrix.m10 + this.m02 * matrix.m20 + this.m03 * matrix.m30;
        final double m01 = this.m00 * matrix.m01 + this.m01 * matrix.m11 + this.m02 * matrix.m21 + this.m03 * matrix.m31;
        final double m02 = this.m00 * matrix.m02 + this.m01 * matrix.m12 + this.m02 * matrix.m22 + this.m03 * matrix.m32;
        final double m03 = this.m00 * matrix.m03 + this.m01 * matrix.m13 + this.m02 * matrix.m23 + this.m03 * matrix.m33;

        final double m10 = this.m10 * matrix.m00 + this.m11 * matrix.m10 + this.m12 * matrix.m20 + this.m13 * matrix.m30;
        final double m11 = this.m10 * matrix.m01 + this.m11 * matrix.m11 + this.m12 * matrix.m21 + this.m13 * matrix.m31;
        final double m12 = this.m10 * matrix.m02 + this.m11 * matrix.m12 + this.m12 * matrix.m22 + this.m13 * matrix.m32;
        final double m13 = this.m10 * matrix.m03 + this.m11 * matrix.m13 + this.m12 * matrix.m23 + this.m13 * matrix.m33;

        final double m20 = this.m20 * matrix.m00 + this.m21 * matrix.m10 + this.m22 * matrix.m20 + this.m23 * matrix.m30;
        final double m21 = this.m20 * matrix.m01 + this.m21 * matrix.m11 + this.m22 * matrix.m21 + this.m23 * matrix.m31;
        final double m22 = this.m20 * matrix.m02 + this.m21 * matrix.m12 + this.m22 * matrix.m22 + this.m23 * matrix.m32;
        final double m23 = this.m20 * matrix.m03 + this.m21 * matrix.m13 + this.m22 * matrix.m23 + this.m23 * matrix.m33;

        final double m30 = this.m30 * matrix.m00 + this.m31 * matrix.m10 + this.m32 * matrix.m20 + this.m33 * matrix.m30;
        final double m31 = this.m30 * matrix.m01 + this.m31 * matrix.m11 + this.m32 * matrix.m21 + this.m33 * matrix.m31;
        final double m32 = this.m30 * matrix.m02 + this.m31 * matrix.m12 + this.m32 * matrix.m22 + this.m33 * matrix.m32;
        final double m33 = this.m30 * matrix.m03 + this.m31 * matrix.m13 + this.m32 * matrix.m23 + this.m33 * matrix.m33;

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

        this.isIdentity = null;
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
    public final void mul(Matrix4d matrix1, Matrix4d matrix2) {
        assert matrix1 != null : AssertMessages.notNullParameter(0);
        assert matrix2 != null : AssertMessages.notNullParameter(1);
        if (this != matrix1 && this != matrix2) {
            this.m00 = matrix1.m00 * matrix2.m00 + matrix1.m01 * matrix2.m10
                    + matrix1.m02 * matrix2.m20 + matrix1.m03 * matrix2.m30;
            this.m01 = matrix1.m00 * matrix2.m01 + matrix1.m01 * matrix2.m11
                    + matrix1.m02 * matrix2.m21 + matrix1.m03 * matrix2.m31;
            this.m02 = matrix1.m00 * matrix2.m02 + matrix1.m01 * matrix2.m12
                    + matrix1.m02 * matrix2.m22 + matrix1.m03 * matrix2.m32;
            this.m03 = matrix1.m00 * matrix2.m03 + matrix1.m01 * matrix2.m13
                    + matrix1.m02 * matrix2.m23 + matrix1.m03 * matrix2.m33;

            this.m10 = matrix1.m10 * matrix2.m00 + matrix1.m11 * matrix2.m10
                    + matrix1.m12 * matrix2.m20 + matrix1.m13 * matrix2.m30;
            this.m11 = matrix1.m10 * matrix2.m01 + matrix1.m11 * matrix2.m11
                    + matrix1.m12 * matrix2.m21 + matrix1.m13 * matrix2.m31;
            this.m12 = matrix1.m10 * matrix2.m02 + matrix1.m11 * matrix2.m12
                    + matrix1.m12 * matrix2.m22 + matrix1.m13 * matrix2.m32;
            this.m13 = matrix1.m10 * matrix2.m03 + matrix1.m11 * matrix2.m13
                    + matrix1.m12 * matrix2.m23 + matrix1.m13 * matrix2.m33;

            this.m20 = matrix1.m20 * matrix2.m00 + matrix1.m21 * matrix2.m10
                    + matrix1.m22 * matrix2.m20 + matrix1.m23 * matrix2.m30;
            this.m21 = matrix1.m20 * matrix2.m01 + matrix1.m21 * matrix2.m11
                    + matrix1.m22 * matrix2.m21 + matrix1.m23 * matrix2.m31;
            this.m22 = matrix1.m20 * matrix2.m02 + matrix1.m21 * matrix2.m12
                    + matrix1.m22 * matrix2.m22 + matrix1.m23 * matrix2.m32;
            this.m23 = matrix1.m20 * matrix2.m03 + matrix1.m21 * matrix2.m13
                    + matrix1.m22 * matrix2.m23 + matrix1.m23 * matrix2.m33;

            this.m30 = matrix1.m30 * matrix2.m00 + matrix1.m31 * matrix2.m10
                    + matrix1.m32 * matrix2.m20 + matrix1.m33 * matrix2.m30;
            this.m31 = matrix1.m30 * matrix2.m01 + matrix1.m31 * matrix2.m11
                    + matrix1.m32 * matrix2.m21 + matrix1.m33 * matrix2.m31;
            this.m32 = matrix1.m30 * matrix2.m02 + matrix1.m31 * matrix2.m12
                    + matrix1.m32 * matrix2.m22 + matrix1.m33 * matrix2.m32;
            this.m33 = matrix1.m30 * matrix2.m03 + matrix1.m31 * matrix2.m13
                    + matrix1.m32 * matrix2.m23 + matrix1.m33 * matrix2.m33;
        } else {
            final double m00 = matrix1.m00 * matrix2.m00 + matrix1.m01 * matrix2.m10
                    + matrix1.m02 * matrix2.m20 + matrix1.m03 * matrix2.m30;
            final double m01 = matrix1.m00 * matrix2.m01 + matrix1.m01 * matrix2.m11
                    + matrix1.m02 * matrix2.m21 + matrix1.m03 * matrix2.m31;
            final double m02 = matrix1.m00 * matrix2.m02 + matrix1.m01 * matrix2.m12
                    + matrix1.m02 * matrix2.m22 + matrix1.m03 * matrix2.m32;
            final double m03 = matrix1.m00 * matrix2.m03 + matrix1.m01 * matrix2.m13
                    + matrix1.m02 * matrix2.m23 + matrix1.m03 * matrix2.m33;

            final double m10 = matrix1.m10 * matrix2.m00 + matrix1.m11 * matrix2.m10
                    + matrix1.m12 * matrix2.m20 + matrix1.m13 * matrix2.m30;
            final double m11 = matrix1.m10 * matrix2.m01 + matrix1.m11 * matrix2.m11
                    + matrix1.m12 * matrix2.m21 + matrix1.m13 * matrix2.m31;
            final double m12 = matrix1.m10 * matrix2.m02 + matrix1.m11 * matrix2.m12
                    + matrix1.m12 * matrix2.m22 + matrix1.m13 * matrix2.m32;
            final double m13 = matrix1.m10 * matrix2.m03 + matrix1.m11 * matrix2.m13
                    + matrix1.m12 * matrix2.m23 + matrix1.m13 * matrix2.m33;

            final double m20 = matrix1.m20 * matrix2.m00 + matrix1.m21 * matrix2.m10
                    + matrix1.m22 * matrix2.m20 + matrix1.m23 * matrix2.m30;
            final double m21 = matrix1.m20 * matrix2.m01 + matrix1.m21 * matrix2.m11
                    + matrix1.m22 * matrix2.m21 + matrix1.m23 * matrix2.m31;
            final double m22 = matrix1.m20 * matrix2.m02 + matrix1.m21 * matrix2.m12
                    + matrix1.m22 * matrix2.m22 + matrix1.m23 * matrix2.m32;
            final double m23 = matrix1.m20 * matrix2.m03 + matrix1.m21 * matrix2.m13
                    + matrix1.m22 * matrix2.m23 + matrix1.m23 * matrix2.m33;

            final double m30 = matrix1.m30 * matrix2.m00 + matrix1.m31 * matrix2.m10
                    + matrix1.m32 * matrix2.m20 + matrix1.m33 * matrix2.m30;
            final double m31 = matrix1.m30 * matrix2.m01 + matrix1.m31 * matrix2.m11
                    + matrix1.m32 * matrix2.m21 + matrix1.m33 * matrix2.m31;
            final double m32 = matrix1.m30 * matrix2.m02 + matrix1.m31 * matrix2.m12
                    + matrix1.m32 * matrix2.m22 + matrix1.m33 * matrix2.m32;
            final double m33 = matrix1.m30 * matrix2.m03 + matrix1.m31 * matrix2.m13
                    + matrix1.m32 * matrix2.m23 + matrix1.m33 * matrix2.m33;

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

        this.isIdentity = null;
    }

    /**
     * Returns true if all of the data members of Matrix4f m1 are equal to the
     * corresponding data members in this Matrix4f.
     *
     * @param matrix
     *            the matrix with which the comparison is made
     * @return true or false
     */
    @Pure
    @SuppressWarnings("checkstyle:cyclomaticcomplexity")
    public boolean equals(Matrix4d matrix) {
        assert matrix != null : AssertMessages.notNullParameter();
        try {
            return this.m00 == matrix.m00 && this.m01 == matrix.m01
                    && this.m02 == matrix.m02 && this.m03 == matrix.m03
                    && this.m10 == matrix.m10 && this.m11 == matrix.m11
                    && this.m12 == matrix.m12 && this.m13 == matrix.m13
                    && this.m20 == matrix.m20 && this.m21 == matrix.m21
                    && this.m22 == matrix.m22 && this.m23 == matrix.m23
                    && this.m30 == matrix.m30 && this.m31 == matrix.m31
                    && this.m32 == matrix.m32 && this.m33 == matrix.m33;
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
    @Pure
    @Override
    @SuppressWarnings("checkstyle:cyclomaticcomplexity")
    public boolean equals(Object t1) {
        if (t1 == this) {
            return true;
        }
        if (getClass().isInstance(t1)) {
            final Matrix4d m2 = (Matrix4d) t1;
            return this.m00 == m2.m00 && this.m01 == m2.m01
                    && this.m02 == m2.m02 && this.m03 == m2.m03
                    && this.m10 == m2.m10 && this.m11 == m2.m11
                    && this.m12 == m2.m12 && this.m13 == m2.m13
                    && this.m20 == m2.m20 && this.m21 == m2.m21
                    && this.m22 == m2.m22 && this.m23 == m2.m23
                    && this.m30 == m2.m30 && this.m31 == m2.m31
                    && this.m32 == m2.m32 && this.m33 == m2.m33;
        }
        return false;
    }

    /**
     * Returns true if the L-infinite distance between this matrix and matrix m1
     * is less than or equal to the epsilon parameter, otherwise returns false.
     * The L-infinite distance is equal to MAX[i=0, 1, 2, 3 ; j=0, 1, 2, 3 ;
     * abs(this.m(i, j) - m1.m(i, j)]
     *
     * @param matrix
     *            the matrix to be compared to this matrix
     * @param epsilon
     *            the threshold value
     * @return <code>true</code> if this matrix is equals to the specified matrix at epsilon.
     */
    @Pure
    @SuppressWarnings({"checkstyle:cyclomaticcomplexity", "checkstyle:npathcomplexity", "checkstyle:returncount"})
    public boolean epsilonEquals(Matrix4d matrix, double epsilon) {
        assert matrix != null : AssertMessages.notNullParameter();

        double diff;

        diff = this.m00 - matrix.m00;
        if ((diff < 0 ? -diff : diff) > epsilon) {
            return false;
        }

        diff = this.m01 - matrix.m01;
        if ((diff < 0 ? -diff : diff) > epsilon) {
            return false;
        }

        diff = this.m02 - matrix.m02;
        if ((diff < 0 ? -diff : diff) > epsilon) {
            return false;
        }

        diff = this.m03 - matrix.m03;
        if ((diff < 0 ? -diff : diff) > epsilon) {
            return false;
        }

        diff = this.m10 - matrix.m10;
        if ((diff < 0 ? -diff : diff) > epsilon) {
            return false;
        }

        diff = this.m11 - matrix.m11;
        if ((diff < 0 ? -diff : diff) > epsilon) {
            return false;
        }

        diff = this.m12 - matrix.m12;
        if ((diff < 0 ? -diff : diff) > epsilon) {
            return false;
        }

        diff = this.m13 - matrix.m13;
        if ((diff < 0 ? -diff : diff) > epsilon) {
            return false;
        }

        diff = this.m20 - matrix.m20;
        if ((diff < 0 ? -diff : diff) > epsilon) {
            return false;
        }

        diff = this.m21 - matrix.m21;
        if ((diff < 0 ? -diff : diff) > epsilon) {
            return false;
        }

        diff = this.m22 - matrix.m22;
        if ((diff < 0 ? -diff : diff) > epsilon) {
            return false;
        }

        diff = this.m23 - matrix.m23;
        if ((diff < 0 ? -diff : diff) > epsilon) {
            return false;
        }

        diff = this.m30 - matrix.m30;
        if ((diff < 0 ? -diff : diff) > epsilon) {
            return false;
        }

        diff = this.m31 - matrix.m31;
        if ((diff < 0 ? -diff : diff) > epsilon) {
            return false;
        }

        diff = this.m32 - matrix.m32;
        if ((diff < 0 ? -diff : diff) > epsilon) {
            return false;
        }

        diff = this.m33 - matrix.m33;
        if ((diff < 0 ? -diff : diff) > epsilon) {
            return false;
        }

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
    @Pure
    @Override
    public int hashCode() {
        long bits = 1L;
        bits = 31L * bits + Double.hashCode(this.m00);
        bits = 31L * bits + Double.hashCode(this.m01);
        bits = 31L * bits + Double.hashCode(this.m02);
        bits = 31L * bits + Double.hashCode(this.m03);
        bits = 31L * bits + Double.hashCode(this.m10);
        bits = 31L * bits + Double.hashCode(this.m11);
        bits = 31L * bits + Double.hashCode(this.m12);
        bits = 31L * bits + Double.hashCode(this.m13);
        bits = 31L * bits + Double.hashCode(this.m20);
        bits = 31L * bits + Double.hashCode(this.m21);
        bits = 31L * bits + Double.hashCode(this.m22);
        bits = 31L * bits + Double.hashCode(this.m23);
        bits = 31L * bits + Double.hashCode(this.m30);
        bits = 31L * bits + Double.hashCode(this.m31);
        bits = 31L * bits + Double.hashCode(this.m32);
        bits = 31L * bits + Double.hashCode(this.m33);
        return (int) (bits ^ (bits >> 31));
    }

    @Pure
    private static long doubleToLongBits(double doubleValue) {
        // Check for +0 or -0
        if (doubleValue == 0.) {
            return 0;
        }
        return Double.doubleToLongBits(doubleValue);
    }

    /**
     * Sets this matrix to all zeros.
     */
    public final void setZero() {
        this.m00 = 0.;
        this.m01 = 0.;
        this.m02 = 0.;
        this.m03 = 0.;

        this.m10 = 0.;
        this.m11 = 0.;
        this.m12 = 0.;
        this.m13 = 0.;

        this.m20 = 0.;
        this.m21 = 0.;
        this.m22 = 0.;
        this.m23 = 0.;

        this.m30 = 0.;
        this.m31 = 0.;
        this.m32 = 0.;
        this.m33 = 0.;

        this.isIdentity = Boolean.FALSE;
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
        this.m01 = 0.;
        this.m02 = 0.;
        this.m03 = 0.;
        this.m10 = 0.;
        this.m11 = m11;
        this.m12 = 0.;
        this.m13 = 0.;
        this.m20 = 0.;
        this.m21 = 0.;
        this.m22 = m22;
        this.m23 = 0.;
        this.m30 = 0.;
        this.m31 = 0.;
        this.m32 = 0.;
        this.m33 = m33;

        this.isIdentity = null;
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

        this.isIdentity = null;
    }

    /**
     * Sets the value of this matrix equal to the negation of of the Matrix4f
     * parameter.
     *
     * @param matrix
     *            the source matrix
     */
    public final void negate(Matrix4d matrix) {
        assert matrix != null : AssertMessages.notNullParameter();
        this.m00 = -matrix.m00;
        this.m01 = -matrix.m01;
        this.m02 = -matrix.m02;
        this.m03 = -matrix.m03;

        this.m10 = -matrix.m10;
        this.m11 = -matrix.m11;
        this.m12 = -matrix.m12;
        this.m13 = -matrix.m13;

        this.m20 = -matrix.m20;
        this.m21 = -matrix.m21;
        this.m22 = -matrix.m22;
        this.m23 = -matrix.m23;

        this.m30 = -matrix.m30;
        this.m31 = -matrix.m31;
        this.m32 = -matrix.m32;
        this.m33 = -matrix.m33;

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
    public Matrix4d clone() {
        try {
            return (Matrix4d) super.clone();
        } catch (CloneNotSupportedException e) {
            // this shouldn't happen, since we are Cloneable
            throw new InternalError(e);
        }
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
        this.isIdentity = null;
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
        this.isIdentity = null;
    }

    /**
     * Get the third matrix element in the first row.
     *
     * @return Returns the m02.
     */
    @Pure
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
        this.isIdentity = null;
    }

    /**
     * Get the fourth matrix element in the first row.
     *
     * @return Returns the m03.
     */
    @Pure
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
        this.isIdentity = null;
    }

    /**
     * Get first matrix element in the second row.
     *
     * @return Returns the m10
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
        this.isIdentity = null;
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

    /**
     * Get the third matrix element in the second row.
     *
     * @return Returns the m12.
     */
    @Pure
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
        this.isIdentity = null;
    }

    /**
     * Get the fourth matrix element in the second row.
     *
     * @return Returns the m13.
     */
    @Pure
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
        this.isIdentity = null;
    }

    /**
     * Get the first matrix element in the third row.
     *
     * @return Returns the m20
     */
    @Pure
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
        this.isIdentity = null;
    }

    /**
     * Get the second matrix element in the third row.
     *
     * @return Returns the m21.
     */
    @Pure
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
        this.isIdentity = null;
    }

    /**
     * Get the third matrix element in the third row .
     *
     * @return Returns the m22.
     */
    @Pure
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
        this.isIdentity = null;
    }

    /**
     * Get the fourth matrix element in the third row .
     *
     * @return Returns the m23.
     */
    @Pure
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
        this.isIdentity = null;
    }

    /**
     * Get the first matrix element in the fourth row.
     *
     * @return Returns the m30
     */
    @Pure
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
        this.isIdentity = null;
    }

    /**
     * Get the second matrix element in the fourth row.
     *
     * @return Returns the m31.
     */
    @Pure
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
        this.isIdentity = null;
    }

    /**
     * Get the third matrix element in the fourth row .
     *
     * @return Returns the m32.
     */
    @Pure
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
        this.isIdentity = null;
    }

    /**
     * Get the fourth matrix element in the fourth row .
     *
     * @return Returns the m33.
     */
    @Pure
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
        this.isIdentity = null;
    }

    /** Replies if the matrix is symmetric.
     *
     * @return <code>true</code> if the matrix is symmetric, otherwise
     * <code>false</code>
     */
    @Pure
    public boolean isSymmetric() {
        return	this.m01 == this.m10
                &&	this.m02 == this.m20
                &&	this.m03 == this.m03
                &&	this.m12 == this.m21
                &&	this.m13 == this.m31
                &&	this.m23 == this.m32;
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
    @SuppressWarnings({"checkstyle:cyclomaticcomplexity", "checkstyle:booleanexpressioncomplexity"})
    public boolean isIdentity() {
        if (this.isIdentity == null) {
            this.isIdentity = MathUtil.isEpsilonEqual(this.m00, 1.)
                    && MathUtil.isEpsilonZero(this.m01)
                    && MathUtil.isEpsilonZero(this.m02)
                    && MathUtil.isEpsilonZero(this.m03)
                    && MathUtil.isEpsilonZero(this.m10)
                    && MathUtil.isEpsilonEqual(this.m11, 1.)
                    && MathUtil.isEpsilonZero(this.m12)
                    && MathUtil.isEpsilonZero(this.m13)
                    && MathUtil.isEpsilonZero(this.m20)
                    && MathUtil.isEpsilonZero(this.m21)
                    && MathUtil.isEpsilonEqual(this.m22, 1.)
                    && MathUtil.isEpsilonZero(this.m23)
                    && MathUtil.isEpsilonZero(this.m30)
                    && MathUtil.isEpsilonZero(this.m31)
                    && MathUtil.isEpsilonZero(this.m32)
                    && MathUtil.isEpsilonEqual(this.m33, 1.);
        }
        return this.isIdentity;
    }

    /** Add the given matrix to this matrix: {@code this += matrix}.
     *
     * <p>This function is an implementation of the operator for
     * the languages that defined or based on the
     * <a href="https://www.eclipse.org/Xtext/">Xtext framework</a>.
     *
     * @param matrix the matrix.
     * @see #add(Matrix4d)
     */
    @XtextOperator("+=")
    public void operator_add(Matrix4d matrix) {
        add(matrix);
    }

    /** Add the given scalar to this matrix: {@code this += scalar}.
     *
     * <p>This function is an implementation of the operator for
     * the languages that defined or based on the
     * <a href="https://www.eclipse.org/Xtext/">Xtext framework</a>.
     *
     * @param scalar the scalar.
     * @see #add(double)
     */
    @XtextOperator("+=")
    public void operator_add(double scalar) {
        add(scalar);
    }

    /** Substract the given matrix to this matrix: {@code this -= matrix}.
     *
     * <p>This function is an implementation of the operator for
     * the languages that defined or based on the
     * <a href="https://www.eclipse.org/Xtext/">Xtext framework</a>.
     *
     * @param matrix the matrix.
     * @see #sub(Matrix4d)
     */
    @XtextOperator("-=")
    public void operator_remove(Matrix4d matrix) {
        sub(matrix);
    }

    /** Substract the given scalar to this matrix: {@code this -= scalar}.
     *
     * <p>This function is an implementation of the operator for
     * the languages that defined or based on the
     * <a href="https://www.eclipse.org/Xtext/">Xtext framework</a>.
     *
     * @param scalar the scalar.
     * @see #add(double)
     */
    @XtextOperator("-=")
    public void operator_remove(double scalar) {
        add(-scalar);
    }

    /** Replies the addition of the given matrix to this matrix: {@code this + matrix}.
     *
     * <p>This function is an implementation of the operator for
     * the languages that defined or based on the
     * <a href="https://www.eclipse.org/Xtext/">Xtext framework</a>.
     *
     * @param matrix the matrix.
     * @return the sum of the matrices.
     * @see #add(Matrix4d)
     */
    @Pure
    @XtextOperator("+")
    public Matrix4d operator_plus(Matrix4d matrix) {
        final Matrix4d result = new Matrix4d();
        result.add(this, matrix);
        return result;
    }

    /** Replies the addition of the given scalar to this matrix: {@code this + scalar}.
     *
     * <p>This function is an implementation of the operator for
     * the languages that defined or based on the
     * <a href="https://www.eclipse.org/Xtext/">Xtext framework</a>.
     *
     * <p>The operation {@code scalar + this} is supported by {@link MatrixExtensions#operator_plus(double, Matrix4d)}.
     *
     * @param scalar the scalar.
     * @return the sum of the matrix and the scalar.
     * @see #add(double)
     * @see MatrixExtensions#operator_plus(double, Matrix4d)
     */
    @Pure
    @XtextOperator("+")
    public Matrix4d operator_plus(double scalar) {
        final Matrix4d result = new Matrix4d();
        result.add(scalar, this);
        return result;
    }

    /** Replies the substraction of the given matrix to this matrix: {@code this - matrix}.
     *
     * <p>This function is an implementation of the operator for
     * the languages that defined or based on the
     * <a href="https://www.eclipse.org/Xtext/">Xtext framework</a>.
     *
     * @param matrix the matrix.
     * @return the result of the substraction.
     * @see #sub(Matrix4d)
     */
    @Pure
    @XtextOperator("-")
    public Matrix4d operator_minus(Matrix4d matrix) {
        final Matrix4d result = new Matrix4d();
        result.sub(this, matrix);
        return result;
    }

    /** Replies the substraction of the given scalar to this matrix: {@code this - scalar}.
     *
     * <p>This function is an implementation of the operator for
     * the languages that defined or based on the
     * <a href="https://www.eclipse.org/Xtext/">Xtext framework</a>.
     *
     * <p>The operation {@code scalar - this} is supported by {@link MatrixExtensions#operator_plus(double, Matrix4d)}.
     *
     * @param scalar the scalar.
     * @return the result of the substraction.
     * @see #add(double)
     * @see MatrixExtensions#operator_minus(double, Matrix4d)
     */
    @Pure
    @XtextOperator("-")
    public Matrix4d operator_minus(double scalar) {
        final Matrix4d result = new Matrix4d();
        result.add(-scalar, this);
        return result;
    }

    /** Replies the negation of this matrix: {@code -this}.
     *
     * <p>This function is an implementation of the operator for
     * the languages that defined or based on the
     * <a href="https://www.eclipse.org/Xtext/">Xtext framework</a>.
     *
     * @return the negation of this matrix.
     * @see #negate()
     */
    @Pure
    @XtextOperator("(-)")
    public Matrix4d operator_minus() {
        final Matrix4d result = new Matrix4d();
        result.negate(this);
        return result;
    }

    /** Replies the multiplication of the given matrix and this matrix: {@code this * matrix}.
     *
     * <p>This function is an implementation of the operator for
     * the languages that defined or based on the
     * <a href="https://www.eclipse.org/Xtext/">Xtext framework</a>.
     *
     * @param matrix the matrix.
     * @return the multiplication of the matrices.
     * @see #mul(Matrix4d)
     */
    @Pure
    @XtextOperator("*")
    public Matrix4d operator_multiply(Matrix4d matrix) {
        final Matrix4d result = new Matrix4d();
        result.mul(this, matrix);
        return result;
    }

    /** Replies the multiplication of the given scalar and this matrix: {@code this * scalar}.
     *
     * <p>This function is an implementation of the operator for
     * the languages that defined or based on the
     * <a href="https://www.eclipse.org/Xtext/">Xtext framework</a>.
     *
     * <p>The operation {@code scalar * this} is supported by {@link MatrixExtensions#operator_multiply(double, Matrix4d)}.
     *
     * @param scalar the scalar.
     * @return the multiplication of the scalar and the matrix.
     * @see #mul(Matrix4d)
     * @see MatrixExtensions#operator_multiply(double, Matrix4d)
     */
    @Pure
    @XtextOperator("*")
    public Matrix4d operator_multiply(double scalar) {
        final Matrix4d result = new Matrix4d();
        result.mul(scalar, this);
        return result;
    }

    /** Replies the division of this matrix by the given scalar: {@code this / scalar}.
     *
     * <p>This function is an implementation of the operator for
     * the languages that defined or based on the
     * <a href="https://www.eclipse.org/Xtext/">Xtext framework</a>.
     *
     * <p>The operation {@code scalar / this} is supported by {@link MatrixExtensions#operator_divide(double, Matrix4d)}.
     *
     * @param scalar the scalar.
     * @return the division of the matrix by the scalar.
     * @see #mul(double)
     * @see MatrixExtensions#operator_divide(double, Matrix4d)
     */
    @Pure
    @XtextOperator("/")
    public Matrix4d operator_divide(double scalar) {
        final Matrix4d result = new Matrix4d();
        result.mul(1. / scalar, this);
        return result;
    }

    /** Increment this matrix: {@code this++}.
     *
     * <p>This function is an implementation of the operator for
     * the languages that defined or based on the
     * <a href="https://www.eclipse.org/Xtext/">Xtext framework</a>.
     *
     * @see #add(double)
     */
    @XtextOperator("++")
    public void operator_plusPlus() {
        add(1);
    }

    /** Increment this matrix: {@code this--}.
     *
     * <p>This function is an implementation of the operator for
     * the languages that defined or based on the
     * <a href="https://www.eclipse.org/Xtext/">Xtext framework</a>.
     *
     * @see #add(double)
     */
    @XtextOperator("--")
    public void operator_moinsMoins() {
        add(-1);
    }

    /** Replies the transposition of this matrix: {@code !this}.
     *
     * <p>This function is an implementation of the operator for
     * the languages that defined or based on the
     * <a href="https://www.eclipse.org/Xtext/">Xtext framework</a>.
     *
     * @return the transpose
     * @see #add(double)
     */
    @XtextOperator("!")
    public Matrix4d operator_not() {
        final Matrix4d result = new Matrix4d();
        result.transpose(this);
        return result;
    }

    /** Replies the addition of the given matrix to this matrix: {@code this + matrix}.
     *
     * <p>This function is an implementation of the operator for
     * the <a href="http://scala-lang.org/">Scala Language</a>.
     *
     * @param matrix the matrix.
     * @return the sum of the matrices.
     * @see #add(Matrix4d)
     */
    @Pure
    @ScalaOperator("+")
    public Matrix4d $plus(Matrix4d matrix) {
        return operator_plus(matrix);
    }

    /** Replies the addition of the given scalar to this matrix: {@code this + scalar}.
     *
     * <p>This function is an implementation of the operator for
     * the <a href="http://scala-lang.org/">Scala Language</a>.
     *
     * <p>The operation {@code scalar + this} is supported by
     * {@link org.arakhne.afc.math.extensions.scala.MatrixExtensions#$plus(double, Matrix4d)}.
     *
     * @param scalar the scalar.
     * @return the sum of the matrix and the scalar.
     * @see #add(double)
     * @see org.arakhne.afc.math.extensions.scala.MatrixExtensions#$plus(double, Matrix4d)
     */
    @Pure
    @ScalaOperator("+")
    public Matrix4d $plus(double scalar) {
        return operator_plus(scalar);
    }

    /** Replies the substraction of the given matrix to this matrix: {@code this - matrix}.
     *
     * <p>This function is an implementation of the operator for
     * the <a href="http://scala-lang.org/">Scala Language</a>.
     *
     * @param matrix the matrix.
     * @return the result of the substraction.
     * @see #sub(Matrix4d)
     */
    @Pure
    @ScalaOperator("-")
    public Matrix4d $minus(Matrix4d matrix) {
        return operator_minus(matrix);
    }

    /** Replies the substraction of the given scalar to this matrix: {@code this - scalar}.
     *
     * <p>This function is an implementation of the operator for
     * the <a href="http://scala-lang.org/">Scala Language</a>.
     *
     * <p>The operation {@code scalar - this} is supported by
     * {@link org.arakhne.afc.math.extensions.scala.MatrixExtensions#$minus(double, Matrix4d)}.
     *
     * @param scalar the scalar.
     * @return the result of the substraction.
     * @see #add(double)
     * @see org.arakhne.afc.math.extensions.scala.MatrixExtensions#$minus(double, Matrix4d)
     */
    @Pure
    @ScalaOperator("-")
    public Matrix4d $minus(double scalar) {
        return operator_minus(scalar);
    }

    /** Replies the negation of this matrix: {@code -this}.
     *
     * <p>This function is an implementation of the operator for
     * the <a href="http://scala-lang.org/">Scala Language</a>.
     *
     * @return the negation of this matrix.
     * @see #negate()
     */
    @Pure
    @ScalaOperator("(-)")
    public Matrix4d $minus() {
        return operator_minus();
    }

    /** Replies the multiplication of the given matrix and this matrix: {@code this * matrix}.
     *
     * <p>This function is an implementation of the operator for
     * the <a href="http://scala-lang.org/">Scala Language</a>.
     *
     * @param matrix the matrix.
     * @return the multiplication of the matrices.
     * @see #mul(Matrix4d)
     */
    @Pure
    @ScalaOperator("*")
    public Matrix4d $times(Matrix4d matrix) {
        return operator_multiply(matrix);
    }

    /** Replies the multiplication of the given scalar and this matrix: {@code this * scalar}.
     *
     * <p>This function is an implementation of the operator for
     * the <a href="http://scala-lang.org/">Scala Language</a>.
     *
     * <p>The operation {@code scalar * this} is supported by
     * {@link org.arakhne.afc.math.extensions.scala.MatrixExtensions#$times(double, Matrix4d)}.
     *
     * @param scalar the scalar.
     * @return the multiplication of the scalar and the matrix.
     * @see #mul(Matrix4d)
     * @see org.arakhne.afc.math.extensions.scala.MatrixExtensions#$times(double, Matrix4d)
     */
    @Pure
    @ScalaOperator("*")
    public Matrix4d $times(double scalar) {
        return operator_multiply(scalar);
    }

    /** Replies the division of this matrix by the given scalar: {@code this / scalar}.
     *
     * <p>This function is an implementation of the operator for
     * the <a href="http://scala-lang.org/">Scala Language</a>.
     *
     * <p>The operation {@code scalar / this} is supported by
     * {@link org.arakhne.afc.math.extensions.scala.MatrixExtensions#$div(double, Matrix4d)}.
     *
     * @param scalar the scalar.
     * @return the division of the matrix by the scalar.
     * @see #mul(double)
     * @see org.arakhne.afc.math.extensions.scala.MatrixExtensions#$div(double, Matrix4d)
     */
    @Pure
    @ScalaOperator("/")
    public Matrix4d $div(double scalar) {
        return operator_divide(scalar);
    }

    /** Replies the transposition of this matrix: {@code !this}.
     *
     * <p>This function is an implementation of the operator for
     * the <a href="http://scala-lang.org/">Scala Language</a>.
     *
     * @return the transpose
     * @see #add(double)
     */
    @Pure
    @ScalaOperator("!")
    public Matrix4d $bang() {
        return operator_not();
    }

}
