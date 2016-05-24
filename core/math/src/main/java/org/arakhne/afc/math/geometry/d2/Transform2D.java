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

package org.arakhne.afc.math.geometry.d2;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.MathUtil;
import org.arakhne.afc.math.matrix.Matrix3d;
import org.arakhne.afc.math.matrix.SingularMatrixException;


/** A 2D transformation.
 * Is represented internally as a 3x3 floating point matrix. The
 * mathematical representation is row major, as in traditional
 * matrix mathematics.
 *
 * <p>The transformation matrix is:
 * <pre><code>
 * | cos(theta)   | -+sin(theta) | Tx |
 * | -+sin(theta) |   cos(theta) | Ty |
 * | 0            | 0            | 1  |
 * </code></pre>
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class Transform2D extends Matrix3d {

	/** This is the identifity transformation.
	 */
	public static final Transform2D IDENTITY = new Transform2D();

	private static final long serialVersionUID = -2858647743636794878L;

	/**
	 * Constructs a new Transform2D object and sets it to the identity transformation.
	 */
	public Transform2D() {
		super(1., 0., 0., 0., 1., 0., 0., 0., 1.);
	}

	/**
	 * Constructs a new Transform2D object and initializes it from the
	 * specified transform.
	 *
	 * @param tranform the transformation to copy.
	 */
	public Transform2D(Transform2D tranform) {
		super(tranform);
	}

	/** Constructor by copy.
	 *
	 * @param matrix the matrix to copy.
	 */
	public Transform2D(Matrix3d matrix) {
		super(matrix);
	}

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
	 */
	public Transform2D(double m00, double m01, double m02, double m10, double m11, double m12) {
		super(m00, m01, m02, m10, m11, m12, 0., 0., 1.);
	}

	@Pure
	@Override
	public Transform2D  clone() {
		return (Transform2D) super.clone();
	}

	/** Set the position.
	 *
	 * <p>This function changes only the elements of
	 * the matrix related to the translation (m02,
	 * m12). The scaling and the shearing are not changed.
	 *
	 * <p>After a call to this function, the matrix will
	 * contains (? means any value):
	 * <pre>
	 *          [   ?    ?    x   ]
	 *          [   ?    ?    y   ]
	 *          [   ?    ?    ?   ]
	 * </pre>
	 *
	 * @param x x translation.
	 * @param y y translation.
	 * @see #makeTranslationMatrix(double, double)
	 */
	public void setTranslation(double x, double y) {
		this.m02 = x;
		this.m12 = y;
	}

	/** Set the position.
	 *
	 * <p>This function changes only the elements of
	 * the matrix related to the translation (m02,
	 * m12). The scaling and the shearing are not changed.
	 *
	 * <p>After a call to this function, the matrix will
	 * contains (? means any value):
	 * <pre>
	 *          [   ?    ?    t.x   ]
	 *          [   ?    ?    t.y   ]
	 *          [   ?    ?    ?     ]
	 * </pre>
	 *
	 * @param translation the translation.
	 * @see #makeTranslationMatrix(double, double)
	 */
	public void setTranslation(Tuple2D<?> translation) {
		assert translation != null : "Translation must not be null"; //$NON-NLS-1$
		this.m02 = translation.getX();
		this.m12 = translation.getY();
	}

	/** Translate the position.
	 *
	 * <p>This function is equivalent to:
	 * <pre>
	 * this = this *  [   1    0    dx   ]
	 *                [   0    1    dy   ]
	 *                [   0    0    1    ]
	 * </pre>
	 *
	 * @param dx the x translation
	 * @param dy the y translation
	 */
	public void translate(double dx, double dy) {
		this.m02 = this.m00 * dx + this.m01 * dy + this.m02;
		this.m12 = this.m10 * dx + this.m11 * dy + this.m12;
	}

	/** Translate the position.
	 *
	 * <p>This function is equivalent to:
	 * <pre>
	 * this = this *  [   1    0    t.x   ]
	 *                [   0    1    t.y   ]
	 *                [   0    0    1     ]
	 * </pre>
	 *
	 * @param translation the translation.
	 */
	public void translate(Vector2D<?, ?> translation) {
		assert translation != null : "Translation must not be null"; //$NON-NLS-1$
		translate(translation.getX(), translation.getY());
	}

	/** Replies the X translation.
	 *
	 * @return the amount
	 */
	@Pure
	public double getTranslationX() {
		return this.m02;
	}

	/** Replies the Y translation.
	 *
	 * @return the amount
	 */
	@Pure
	public double getTranslationY() {
		return this.m12;
	}

	/** Replies the translation.
	 *
	 * @param translation the vector to set with the translation component.
	 */
	@Pure
	public void getTranslationVector(Tuple2D<?> translation) {
		assert translation != null : "Output translation vector must not be null"; //$NON-NLS-1$
		translation.set(this.m02, this.m12);
	}

	/**
	 * Rotate the object (theta).
	 *
	 * <p>This function is equivalent to:
	 * <pre>
	 * this = this *  [   cos(theta)    -sin(theta)    0   ]
	 *                [   sin(theta)    cos(theta)     0   ]
	 *                [   0             0              1   ]
	 * </pre>
	 *
	 * @param theta the rotation angle, in radians.
	 */
	public void rotate(double theta) {
		// Copied from AWT API
		final double sin = Math.sin(theta);
		if (sin == 1.) {
			rotate90();
		} else if (sin == -1.) {
			rotate270();
		} else {
			final double cos = Math.cos(theta);
			if (cos == -1.) {
				rotate180();
			} else if (cos != 1.) {
				double m0 = this.m00;
				double m1 = this.m01;
				this.m00 =  cos * m0 + sin * m1;
				this.m01 = -sin * m0 + cos * m1;
				m0 = this.m10;
				m1 = this.m11;
				this.m10 =  cos * m0 + sin * m1;
				this.m11 = -sin * m0 + cos * m1;
			}
		}
	}

	private void rotate90() {
		// Copied from AWT API
		double m0 = this.m00;
		this.m00 = this.m01;
		this.m01 = -m0;
		m0 = this.m10;
		this.m10 = this.m11;
		this.m11 = -m0;
	}

	private void rotate180() {
		// Copied from AWT API
		this.m00 = -this.m00;
		this.m11 = -this.m11;
		// If there was a shear, then this rotation has no
		// effect on the state.
		this.m01 = -this.m01;
		this.m10 = -this.m10;
	}

	private void rotate270() {
		// Copied from AWT API
		double m0 = this.m00;
		this.m00 = -this.m01;
		this.m01 = m0;
		m0 = this.m10;
		this.m10 = -this.m11;
		this.m11 = m0;
	}

	/**
	 * Perform SVD on the 2x2 matrix containing the rotation and scaling factors.
	 *
	 * @param scales the scaling factors.
	 * @param rots the rotation factors.
	 */
	@SuppressWarnings("checkstyle:magicnumber")
	protected void getScaleRotate2x2(double[] scales, double[] rots) {
		final double[] tmp = new double[9];

		tmp[0] = this.m00;
		tmp[1] = this.m01;
		tmp[2] = 0;

		tmp[3] = this.m10;
		tmp[4] = this.m11;
		tmp[5] = 0;

		tmp[6] = 0;
		tmp[7] = 0;
		tmp[8] = 0;

		computeSVD(tmp, scales, rots);
	}

	/**
	 * Performs an SVD normalization of this matrix to calculate and return the
	 * rotation angle.
	 *
	 * @return the rotation angle of this matrix. The value is in [-PI; PI]
	 */
	@Pure
	@SuppressWarnings("checkstyle:magicnumber")
	public double getRotation() {
		final double[] tmpScale = new double[3];
		final double[] tmpRot = new double[9];
		getScaleRotate2x2(tmpScale, tmpRot);
		if (Math.signum(tmpRot[0]) != Math.signum(tmpRot[4])) {
			// Sinuses are on the top-left to bottom-right diagonal
			// -s   c   0
			//  c   s   0
			//  0   0   1
			return Math.atan2(tmpRot[4], tmpRot[3]);
		}
		// Sinuses are on the top-right to bottom-left diagonal
		//  c  -s  0
		//  s   c  0
		//  0   0  1
		return Math.atan2(tmpRot[3], tmpRot[0]);
	}

	/** Change the rotation of this matrix.
	 * Performs an SVD normalization of this matrix for determining and preserving the scaling.
	 *
	 * @param angle the rotation angle.
	 */
	@SuppressWarnings("checkstyle:magicnumber")
	public void setRotation(double angle) {
		final double[] tmpScale = new double[3];
		final double[] tmpRot = new double[9];
		getScaleRotate2x2(tmpScale, tmpRot);
		final double cos = Math.cos(angle);
		final double sin = Math.sin(angle);
		// R * S
		this.m00 = tmpScale[0] * cos;
		this.m01 = tmpScale[1] * -sin;
		this.m10 = tmpScale[0] * sin;
		this.m11 = tmpScale[1] * cos;
		// S * R
		//		this.m00 = tmp_scale[0] * cos;
		//		this.m01 = tmp_scale[0] * -sin;
		//		this.m10 = tmp_scale[1] * sin;
		//		this.m11 = tmp_scale[1] * cos;
	}

	/** Concatenates this transform with a scaling transformation.
	 *
	 * <p>This function is equivalent to:
	 * <pre>
	 * this = this *  [   sx   0    0   ]
	 *                [   0    sy   0   ]
	 *                [   0    0    1   ]
	 * </pre>
	 *
	 * @param scaleX scaling along x axis.
	 * @param scaleY scaling along y axis.
	 */
	public void scale(double scaleX, double scaleY) {
		this.m00 *= scaleX;
		this.m11 *= scaleY;
		this.m01 *= scaleY;
		this.m10 *= scaleX;
	}

	/** Concatenates this transform with a scaling transformation.
	 *
	 * <p>This function is equivalent to:
	 * <pre>
	 * this = this *  [   t.x   0     0   ]
	 *                [   0     t.y   0   ]
	 *                [   0     0     1   ]
	 * </pre>
	 *
	 * @param tuple the scaling factors.
	 */
	public void scale(Tuple2D<?> tuple) {
		assert tuple != null : "Tuple must not be null"; //$NON-NLS-1$
		scale(tuple.getX(), tuple.getY());
	}

	/** Concatenates this transform with a scaling transformation.
	 *
	 * <p>This function is equivalent to:
	 * <pre>
	 * this = this *  [   s    0    0   ]
	 *                [   0    s    0   ]
	 *                [   0    0    1   ]
	 * </pre>
	 *
	 * @param scale the scaling factor.
	 */
	public void scale(double scale) {
		this.m00 *= scale;
		this.m11 *= scale;
		this.m01 *= scale;
		this.m10 *= scale;
	}

	/**
	 * Performs an SVD normalization of this matrix to calculate and return the
	 * uniform scale factor. If the matrix has non-uniform scale factors, the
	 * largest of the x, y scale factors will be returned.
	 *
	 * @return the scale factor of this matrix.
	 */
	@Pure
	@SuppressWarnings("checkstyle:magicnumber")
	public double getScale() {
		final double[] tmpScale = new double[3];
		final double[] tmpRot = new double[9];
		getScaleRotate2x2(tmpScale, tmpRot);
		return MathUtil.max(tmpScale);
	}

	/** Performs an SVD normalization of this matrix to calculate and return the
	 * scale factor for X axis.
	 *
	 * @return the x scale factor.
	 */
	@Pure
	@SuppressWarnings("checkstyle:magicnumber")
	public double getScaleX() {
		final double[] tmpScale = new double[3];
		final double[] tmpRot = new double[9];
		getScaleRotate2x2(tmpScale, tmpRot);
		return tmpScale[0];
	}

	/** Performs an SVD normalization of this matrix to calculate and return the
	 * scale factor for Y axis.
	 *
	 * @return the y scale factor.
	 */
	@Pure
	@SuppressWarnings("checkstyle:magicnumber")
	public double getScaleY() {
		final double[] tmpScale = new double[3];
		final double[] tmpRot = new double[9];
		getScaleRotate2x2(tmpScale, tmpRot);
		return tmpScale[1];
	}

	/** Performs an SVD normalization of this matrix to calculate and return the
	 * scale factors for X and Y axess.
	 *
	 * @param scale the tuple to set.
	 */
	@Pure
	@SuppressWarnings("checkstyle:magicnumber")
	public void getScaleVector(Tuple2D<?> scale) {
		assert scale != null : "The output scaling vector must not be null"; //$NON-NLS-1$
		final double[] tmpScale = new double[3];
		final double[] tmpRot = new double[9];
		getScaleRotate2x2(tmpScale, tmpRot);
		scale.set(tmpScale[0], tmpScale[1]);
	}

	/** Change the scale of this matrix.
	 * Performs an SVD normalization of this matrix for determining and preserving the rotation.
	 *
	 * @param scaleX the scaling factor along x axis.
	 * @param scaleY the scaling factor along y axis.
	 * @see #makeScaleMatrix(double, double)
	 */
	@SuppressWarnings("checkstyle:magicnumber")
	public void setScale(double scaleX, double scaleY) {
		final double[] tmpScale = new double[3];
		final double[] tmpRot = new double[9];
		getScaleRotate2x2(tmpScale, tmpRot);
		this.m00 = tmpRot[0] * scaleX;
		this.m01 = tmpRot[1] * scaleY;
		this.m10 = tmpRot[3] * scaleX;
		this.m11 = tmpRot[4] * scaleY;
	}

	/** Set the scale.
	 *
	 * <p>This function changes only the elements of
	 * the matrix related to the scaling (m00,
	 * m11). The shearing and the translation are not changed.
	 * The rotation is lost.
	 *
	 * <p>After a call to this function, the matrix will
	 * contains (? means any value):
	 * <pre>
	 *          [   t.x  ?    ?   ]
	 *          [   ?    t.y  ?   ]
	 *          [   ?    ?    ?   ]
	 * </pre>
	 *
	 * @param tuple the scaling factors.
	 * @see #makeScaleMatrix(double, double)
	 */
	public void setScale(Tuple2D<?> tuple) {
		assert tuple != null : "Tuple must not be null"; //$NON-NLS-1$
		setScale(tuple.getX(), tuple.getY());
	}

	/** Concatenates this transform with a shearing transformation.
	 *
	 * <p>This function is equivalent to:
	 * <pre>
	 * this = this *  [   1    shx  0   ]
	 *                [   shy  1    0   ]
	 *                [   0    0    1   ]
	 * </pre>
	 *
	 * @param shearX the shearing factory along x axis.
	 * @param shearY the shearing factory along y axis.
	 */
	public void shear(double shearX, double shearY) {
		double m0 = this.m00;
		double m1 = this.m01;

		this.m00 = m0 + m1 * shearY;
		this.m01 = m0 * shearX + m1;

		m0 = this.m10;
		m1 = this.m11;
		this.m10 = m0 + m1 * shearY;
		this.m11 = m0 * shearX + m1;
	}

	/** Concatenates this transform with a shearing transformation.
	 *
	 * <p>This function is equivalent to:
	 * <pre>
	 * this = this *  [   1    shx  0   ]
	 *                [   shy  1    0   ]
	 *                [   0    0    1   ]
	 * </pre>
	 *
	 * @param shear the shear factors.
	 */
	public void shear(Tuple2D<?> shear) {
		assert shear != null : "Shear must be not null"; //$NON-NLS-1$
		shear(shear.getX(), shear.getY());
	}

	/**
	 * Sets the value of this matrix to a counter clockwise rotation about the x
	 * axis, and no translation
	 *
	 * <p>This function changes all the elements of
	 * the matrix, icluding the translation.
	 *
	 * <p>After a call to this function, the matrix will
	 * contains (? means any value):
	 * <pre>
	 *          [   cos(theta)  -sin(theta)  0   ]
	 *          [   sin(theta)  cos(theta)   0   ]
	 *          [   0           0            1   ]
	 * </pre>
	 *
	 * @param angle
	 *            the angle to rotate about the X axis in radians
	 * @see #setRotation(double)
	 */
	public void makeRotationMatrix(double angle) {
		final double sinAngle = Math.sin(angle);
		final double cosAngle = Math.cos(angle);

		this.m00 = cosAngle;
		this.m01 = -sinAngle;
		this.m02 = 0.;

		this.m11 = cosAngle;
		this.m10 = sinAngle;
		this.m12 = 0.;

		this.m20 = 0.;
		this.m21 = 0.;
		this.m22 = 1.;
	}

	/**
	 * Sets the value of this matrix to the given translation, without rotation.
	 *
	 * <p>This function changes all the elements of
	 * the matrix including the scaling and the shearing.
	 *
	 * <p>After a call to this function, the matrix will
	 * contains (? means any value):
	 * <pre>
	 *          [   1    0    x   ]
	 *          [   0    1    y   ]
	 *          [   0    0    1   ]
	 * </pre>
	 *
	 * @param dx is the translation along X.
	 * @param dy is the translation along Y.
	 * @see #setTranslation(double, double)
	 * @see #setTranslation(Tuple2D)
	 */
	public void makeTranslationMatrix(double dx, double dy) {
		this.m00 = 1.;
		this.m01 = 0.;
		this.m02 = dx;

		this.m10 = 0.;
		this.m11 = 1.;
		this.m12 = dy;

		this.m20 = 0.;
		this.m21 = 0.;
		this.m22 = 1.;
	}

	/**
	 * Sets the value of this matrix to the given scaling, without rotation.
	 *
	 * <p>This function changes all the elements of
	 * the matrix, including the shearing and the
	 * translation.
	 *
	 * <p>After a call to this function, the matrix will
	 * contains (? means any value):
	 * <pre>
	 *          [   sx  0   0   ]
	 *          [   0   sy  0   ]
	 *          [   0   0   1   ]
	 * </pre>
	 *
	 * @param scaleX is the scaling along X.
	 * @param scaleY is the scaling along Y.
	 * @see #setScale(double, double)
	 * @see #setScale(Tuple2D)
	 */
	public void makeScaleMatrix(double scaleX, double scaleY) {
		this.m00 = scaleX;
		this.m01 = 0.;
		this.m02 = 0.;

		this.m10 = 0.;
		this.m11 = scaleY;
		this.m12 = 0.;

		this.m20 = 0.;
		this.m21 = 0.;
		this.m22 = 1.;
	}

	/**
	 * Multiply this matrix by the tuple t and place the result back into the
	 * tuple (t = this*t).
	 *
	 * @param tuple
	 *            the tuple to be multiplied by this matrix and then replaced
	 */
	public void transform(Tuple2D<?> tuple) {
		assert tuple != null : "Tuple to transform must not be null"; //$NON-NLS-1$
		final double x = this.m00 * tuple.getX() + this.m01 * tuple.getY() + this.m02;
		final double y = this.m10 * tuple.getX() + this.m11 * tuple.getY() + this.m12;
		tuple.set(x, y);
	}

	/**
	 * Multiply this matrix by the tuple t and and place the result into the
	 * tuple "result".
	 *
	 * <p>This function is equivalent to:
	 * <pre>
	 * result = this *  [   t.x   ]
	 *                  [   t.y   ]
	 *                  [   1     ]
	 * </pre>
	 *
	 * @param tuple
	 *            the tuple to be multiplied by this matrix
	 * @param result
	 *            the tuple into which the product is placed
	 */
	public void transform(Tuple2D<?> tuple, Tuple2D<?> result) {
		assert tuple != null : "Tuple to transform must not be null"; //$NON-NLS-1$
		assert result != null : "Output tuple must not be null"; //$NON-NLS-1$
		result.set(
				this.m00 * tuple.getX() + this.m01 * tuple.getY() + this.m02,
				this.m10 * tuple.getX() + this.m11 * tuple.getY() + this.m12);
	}

	/**
	 * Returns an <code>Transform2D</code> object representing the
	 * inverse transformation.
	 * The inverse transform Tx' of this transform Tx
	 * maps coordinates transformed by Tx back
	 * to their original coordinates.
	 * In other words, Tx'(Tx(p)) = p = Tx(Tx'(p)).
	 *
	 * <p>If this transform maps all coordinates onto a point or a line
	 * then it will not have an inverse, since coordinates that do
	 * not lie on the destination point or line will not have an inverse
	 * mapping.
	 * The <code>determinant</code> method can be used to determine if this
	 * transform has no inverse, in which case an exception will be
	 * thrown if the <code>createInverse</code> method is called.
	 * @return a new <code>Transform2D</code> object representing the
	 *     inverse transformation.
	 * @see #determinant()
	 * @throws SingularMatrixException if the matrix cannot be inverted.
	 */
	@Pure
	public Transform2D createInverse() {
		final double det = this.m00 * this.m11 - this.m01 * this.m10;
		if (Math.abs(det) <= Double.MIN_VALUE) {
			throw new SingularMatrixException("Determinant is " + det); //$NON-NLS-1$
		}
		return new Transform2D(
				this.m11 / det,
				-this.m01 / det,
				(this.m01 * this.m12 - this.m11 * this.m02) / det,
				-this.m10 / det,
				this.m00 / det,
				(this.m10 * this.m02 - this.m00 * this.m12) / det);
	}

	/**
	 * Set the components of the transformation.
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
	 */
	public void set(double m00, double m01, double m02, double m10, double m11, double m12) {
		set(m00, m01, m02, m10, m11, m12, 0., 0., 1.);
	}

	/**
	 * Invert this transformation.
	 * The inverse transform Tx' of this transform Tx
	 * maps coordinates transformed by Tx back
	 * to their original coordinates.
	 * In other words, Tx'(Tx(p)) = p = Tx(Tx'(p)).
	 *
	 * <p>If this transform maps all coordinates onto a point or a line
	 * then it will not have an inverse, since coordinates that do
	 * not lie on the destination point or line will not have an inverse
	 * mapping.
	 * The <code>determinant</code> method can be used to determine if this
	 * transform has no inverse, in which case an exception will be
	 * thrown if the <code>createInverse</code> method is called.
	 * @see #determinant()
	 * @throws SingularMatrixException if the matrix cannot be inverted.
	 */
	@Override
	public void invert() {
		final double det = this.m00 * this.m11 - this.m01 * this.m10;
		if (Math.abs(det) <= Double.MIN_VALUE) {
			throw new SingularMatrixException("Determinant is " + det); //$NON-NLS-1$
		}
		set(
				this.m11 / det,
				-this.m01 / det,
				(this.m01 * this.m12 - this.m11 * this.m02) / det,
				-this.m10 / det,
				this.m00 / det,
				(this.m10 * this.m02 - this.m00 * this.m12) / det);
	}

	/**
	 * Invert this transformation.
	 * The inverse transform Tx' of this transform Tx
	 * maps coordinates transformed by Tx back
	 * to their original coordinates.
	 * In other words, Tx'(Tx(p)) = p = Tx(Tx'(p)).
	 *
	 * <p>If this transform maps all coordinates onto a point or a line
	 * then it will not have an inverse, since coordinates that do
	 * not lie on the destination point or line will not have an inverse
	 * mapping.
	 * The <code>determinant</code> method can be used to determine if this
	 * transform has no inverse, in which case an exception will be
	 * thrown if the <code>createInverse</code> method is called.
	 * @param matrix is the matrix to invert
	 * @see #determinant()
	 * @throws SingularMatrixException if the matrix cannot be inverted.
	 */
	@Override
	public void invert(Matrix3d matrix) {
		assert matrix != null : "Matrix must not be null"; //$NON-NLS-1$
		final double det = matrix.getM00() * matrix.getM11() - matrix.getM01() * matrix.getM10();
		if (MathUtil.isEpsilonZero(det)) {
			throw new SingularMatrixException("Determinant is too small: " + det); //$NON-NLS-1$
		}
		set(
				matrix.getM11() / det,
				-matrix.getM01() / det,
				(matrix.getM01() * matrix.getM12() - matrix.getM11() * matrix.getM02()) / det,
				-matrix.getM10() / det,
				matrix.getM00() / det,
				(matrix.getM10() * matrix.getM02() - matrix.getM00() * matrix.getM12()) / det);
	}

}
