/* 
 * $Id$
 * 
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
package org.arakhne.afc.math.geometry.d2;

import org.arakhne.afc.math.MathUtil;
import org.arakhne.afc.math.matrix.Matrix3f;
import org.arakhne.afc.math.matrix.SingularMatrixException;
import org.eclipse.xtext.xbase.lib.Pure;


/** A 2D transformation.
 * Is represented internally as a 3x3 floating point matrix. The
 * mathematical representation is row major, as in traditional
 * matrix mathematics.
 * <p>
 * The transformation matrix is:
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
public class Transform2D extends Matrix3f {

	private static final long serialVersionUID = -2858647743636794878L;
	
	/** This is the identifity transformation.
	 */
	public static final Transform2D IDENTITY = new Transform2D();

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
	 * @param tranform
	 */
	public Transform2D(Transform2D tranform) {
		super(tranform);
	}

	/**
	 * @param matrix
	 */
	public Transform2D(Matrix3f matrix) {
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
	 * <p>
	 * This function changes only the elements of 
	 * the matrix related to the translation (m02,
	 * m12). The scaling and the shearing are not changed. 
	 * <p>
	 * After a call to this function, the matrix will
	 * contains (? means any value):
	 * <pre>
	 *          [   ?    ?    x   ]
	 *          [   ?    ?    y   ]
	 *          [   ?    ?    ?   ]
	 * </pre>
	 * 
	 * @param x
	 * @param y
	 * @see #makeTranslationMatrix(double, double)
	 */
	public void setTranslation(double x, double y) {
		this.m02 = x;
		this.m12 = y;
	}

	/** Set the position.
	 * <p>
	 * This function changes only the elements of 
	 * the matrix related to the translation (m02,
	 * m12). The scaling and the shearing are not changed. 
	 * <p>
	 * After a call to this function, the matrix will
	 * contains (? means any value):
	 * <pre>
	 *          [   ?    ?    t.x   ]
	 *          [   ?    ?    t.y   ]
	 *          [   ?    ?    ?     ]
	 * </pre>
	 * 
	 * @param translation
	 * @see #makeTranslationMatrix(double, double)
	 */
	public void setTranslation(Tuple2D<?> translation) {
		assert (translation != null) : "Translation must not be null"; //$NON-NLS-1$
		this.m02 = translation.getX();
		this.m12 = translation.getY();
	}

	/** Translate the position.
	 * <p>
	 * This function is equivalent to:
	 * <pre>
	 * this = this *  [   1    0    dx   ]
	 *                [   0    1    dy   ]
	 *                [   0    0    1    ]
	 * </pre>
	 * 
	 * @param dx
	 * @param dy
	 */
	public void translate(double dx, double dy) {
		this.m02 = this.m00 * dx + this.m01 * dy + this.m02;
		this.m12 = this.m10 * dx + this.m11 * dy + this.m12;
	}

	/** Translate the position.
	 * <p>
	 * This function is equivalent to:
	 * <pre>
	 * this = this *  [   1    0    t.x   ]
	 *                [   0    1    t.y   ]
	 *                [   0    0    1     ]
	 * </pre>
	 * 
	 * @param translation
	 */
	public void translate(Vector2D<?, ?> translation) {
		assert (translation != null) : "Translation must not be null"; //$NON-NLS-1$
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
		assert (translation != null) : "Output translation vector must not be null"; //$NON-NLS-1$
		translation.set(this.m02, this.m12);
	}

	/**
	 * Rotate the object (theta).
	 * <p>
	 * This function is equivalent to:
	 * <pre>
	 * this = this *  [   cos(theta)    -sin(theta)    0   ]
	 *                [   sin(theta)    cos(theta)     0   ]
	 *                [   0             0              1   ]
	 * </pre>
	 * 
	 * @param theta
	 */
	public void rotate(double theta) {
		// Copied from AWT API
		double sin = Math.sin(theta);
		if (sin == 1.) {
			rotate90();
		}
		else if (sin == -1.) {
			rotate270();
		}
		else {
			double cos = Math.cos(theta);
			if (cos == -1.) {
				rotate180();
			}
			else if (cos != 1.) {
				double M0, M1;
				M0 = this.m00;
				M1 = this.m01;
				this.m00 =  cos * M0 + sin * M1;
				this.m01 = -sin * M0 + cos * M1;
				M0 = this.m10;
				M1 = this.m11;
				this.m10 =  cos * M0 + sin * M1;
				this.m11 = -sin * M0 + cos * M1;
			}
		}
	}
	
	private final void rotate90() {
		// Copied from AWT API
        double M0 = this.m00;
        this.m00 = this.m01;
        this.m01 = -M0;
        M0 = this.m10;
        this.m10 = this.m11;
        this.m11 = -M0;
    }
	
    private final void rotate180() {
		// Copied from AWT API
    	this.m00 = -this.m00;
    	this.m11 = -this.m11;
    	// If there was a shear, then this rotation has no
    	// effect on the state.
    	this.m01 = -this.m01;
    	this.m10 = -this.m10;
    }
    
    private final void rotate270() {
		// Copied from AWT API
        double M0 = this.m00;
        this.m00 = -this.m01;
        this.m01 = M0;
        M0 = this.m10;
        this.m10 = -this.m11;
        this.m11 = M0;
    }

	/**
	 * Perform SVD on the 2x2 matrix containing the rotation and scaling factors.
	 *
	 * @param scales the scaling factors.
	 * @param rots the rotation factors.
	 */
	protected void getScaleRotate2x2(double scales[], double rots[]) {
		double[] tmp = new double[9]; // scratch matrix

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
	public double getRotation() {
		double[] tmp_scale = new double[3]; // scratch matrix
		double[] tmp_rot = new double[9]; // scratch matrix
		getScaleRotate2x2(tmp_scale, tmp_rot);
		if (Math.signum(tmp_rot[0]) != Math.signum(tmp_rot[4])) {
			// Sinuses are on the top-left to bottom-right diagonal
			// -s   c   0
			//  c   s   0
			//  0   0   1
			return Math.atan2(tmp_rot[4], tmp_rot[3]);
		}
		// Sinuses are on the top-right to bottom-left diagonal
		//  c  -s  0
		//  s   c  0
		//  0   0  1
		return Math.atan2(tmp_rot[3], tmp_rot[0]);
	}

	/** Change the rotation of this matrix.
	 * Performs an SVD normalization of this matrix for determining and preserving the scaling.
	 * 
	 * @param angle
	 */
	public void setRotation(double angle) {
		double[] tmp_scale = new double[3]; // scratch matrix
		double[] tmp_rot = new double[9]; // scratch matrix
		getScaleRotate2x2(tmp_scale, tmp_rot);
		double cos = Math.cos(angle);
		double sin = Math.sin(angle);
		// R * S
		this.m00 = tmp_scale[0] * cos;
		this.m01 = tmp_scale[1] * -sin;
		this.m10 = tmp_scale[0] * sin;
		this.m11 = tmp_scale[1] * cos;
		// S * R
//		this.m00 = tmp_scale[0] * cos;
//		this.m01 = tmp_scale[0] * -sin;
//		this.m10 = tmp_scale[1] * sin;
//		this.m11 = tmp_scale[1] * cos;
	}

	/** Concatenates this transform with a scaling transformation.
	 * <p>
	 * This function is equivalent to:
	 * <pre>
	 * this = this *  [   sx   0    0   ]
	 *                [   0    sy   0   ]
	 *                [   0    0    1   ]
	 * </pre>
	 * 
	 * @param scaleX
	 * @param scaleY
	 */
	public void scale(double scaleX, double scaleY) {
        this.m00 *= scaleX;
        this.m11 *= scaleY;
        this.m01 *= scaleY;
        this.m10 *= scaleX;
	}

	/** Concatenates this transform with a scaling transformation.
	 * <p>
	 * This function is equivalent to:
	 * <pre>
	 * this = this *  [   t.x   0     0   ]
	 *                [   0     t.y   0   ]
	 *                [   0     0     1   ]
	 * </pre>
	 * 
	 * @param tuple
	 */
	public void scale(Tuple2D<?> tuple) {
		assert (tuple != null) : "Tuple must not be null"; //$NON-NLS-1$
		scale(tuple.getX(), tuple.getY());
	}
	
	/** Concatenates this transform with a scaling transformation.
	 * <p>
	 * This function is equivalent to:
	 * <pre>
	 * this = this *  [   s    0    0   ]
	 *                [   0    s    0   ]
	 *                [   0    0    1   ]
	 * </pre>
	 * 
	 * @param scale
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
	public double getScale() {
		double[] tmp_scale = new double[3]; // scratch matrix
		double[] tmp_rot = new double[9]; // scratch matrix
		getScaleRotate2x2(tmp_scale, tmp_rot);
		return MathUtil.max(tmp_scale);
	}

	/** Performs an SVD normalization of this matrix to calculate and return the
	 * scale factor for X axis.
	 * 
	 * @return the x scale factor.
	 */
	@Pure
	public double getScaleX() {
		double[] tmp_scale = new double[3]; // scratch matrix
		double[] tmp_rot = new double[9]; // scratch matrix
		getScaleRotate2x2(tmp_scale, tmp_rot);
		return tmp_scale[0];
	}

	/** Performs an SVD normalization of this matrix to calculate and return the
	 * scale factor for Y axis.
	 * 
	 * @return the y scale factor.
	 */
	@Pure
	public double getScaleY() {
		double[] tmp_scale = new double[3]; // scratch matrix
		double[] tmp_rot = new double[9]; // scratch matrix
		getScaleRotate2x2(tmp_scale, tmp_rot);
		return tmp_scale[1];
	}

	/** Performs an SVD normalization of this matrix to calculate and return the
	 * scale factors for X and Y axess.
	 * 
	 * @param scale the tuple to set.
	 */
	@Pure
	public void getScaleVector(Tuple2D<?> scale) {
		assert (scale != null) : "The output scaling vector must not be null"; //$NON-NLS-1$
		double[] tmp_scale = new double[3]; // scratch matrix
		double[] tmp_rot = new double[9]; // scratch matrix
		getScaleRotate2x2(tmp_scale, tmp_rot);
		scale.set(tmp_scale[0], tmp_scale[1]);
	}

	/** Change the scale of this matrix.
	 * Performs an SVD normalization of this matrix for determining and preserving the rotation.
	 * 
	 * @param scaleX
	 * @param scaleY
	 * @see #makeScaleMatrix(double, double)
	 */
	public void setScale(double scaleX, double scaleY) {
		double[] tmp_scale = new double[3]; // scratch matrix
		double[] tmp_rot = new double[9]; // scratch matrix
		getScaleRotate2x2(tmp_scale, tmp_rot);
		this.m00 = tmp_rot[0] * scaleX;
		this.m01 = tmp_rot[1] * scaleY;
		this.m10 = tmp_rot[3] * scaleX;
		this.m11 = tmp_rot[4] * scaleY;
	}

	/** Set the scale.
	 * <p>
	 * This function changes only the elements of 
	 * the matrix related to the scaling (m00,
	 * m11). The shearing and the translation are not changed. 
	 * The rotation is lost.
	 * <p>
	 * After a call to this function, the matrix will
	 * contains (? means any value):
	 * <pre>
	 *          [   t.x  ?    ?   ]
	 *          [   ?    t.y  ?   ]
	 *          [   ?    ?    ?   ]
	 * </pre>
	 * 
	 * @param tuple
	 * @see #makeScaleMatrix(double, double)
	 */
	public void setScale(Tuple2D<?> tuple) {
		assert (tuple != null) : "Tuple must not be null"; //$NON-NLS-1$
		setScale(tuple.getX(), tuple.getY());
	}

	/** Concatenates this transform with a shearing transformation.
	 * <p>
	 * This function is equivalent to:
	 * <pre>
	 * this = this *  [   1    shx  0   ]
	 *                [   shy  1    0   ]
	 *                [   0    0    1   ]
	 * </pre>
	 * 
	 * @param shearX
	 * @param shearY
	 */
	public void shear(double shearX, double shearY) {
		double M0, M1;
		M0 = this.m00;
		M1 = this.m01;
		
		this.m00 = M0 + M1 * shearY;
		this.m01 = M0 * shearX + M1;

		M0 = this.m10;
		M1 = this.m11;
		this.m10 = M0 + M1 * shearY;
		this.m11 = M0 * shearX + M1;
	}

	/** Concatenates this transform with a shearing transformation.
	 * <p>
	 * This function is equivalent to:
	 * <pre>
	 * this = this *  [   1    shx  0   ]
	 *                [   shy  1    0   ]
	 *                [   0    0    1   ]
	 * </pre>
	 * 
	 * @param shear
	 */
	public void shear(Tuple2D<?> shear) {
		assert (shear != null) : "Shear must be not null"; //$NON-NLS-1$
		shear(shear.getX(), shear.getY());
	}

	/**
	 * Sets the value of this matrix to a counter clockwise rotation about the x
	 * axis, and no translation
	 * <p>
	 * This function changes all the elements of 
	 * the matrix, icluding the translation. 
	 * <p>
	 * After a call to this function, the matrix will
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
		double sinAngle, cosAngle;

		sinAngle = Math.sin(angle);
		cosAngle = Math.cos(angle);

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
	 * <p>
	 * This function changes all the elements of 
	 * the matrix including the scaling and the shearing. 
	 * <p>
	 * After a call to this function, the matrix will
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
	 * <p>
	 * This function changes all the elements of 
	 * the matrix, including the shearing and the
	 * translation. 
	 * <p>
	 * After a call to this function, the matrix will
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
		assert (tuple != null) : "Tuple to transform must not be null"; //$NON-NLS-1$
		double x, y;
		x = this.m00 * tuple.getX() + this.m01 * tuple.getY() + this.m02;
		y = this.m10 * tuple.getX() + this.m11 * tuple.getY() + this.m12;
		tuple.set(x, y);
	}
	
	/**
	 * Multiply this matrix by the tuple t and and place the result into the
	 * tuple "result".
	 * <p>
	 * This function is equivalent to:
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
		assert (tuple != null) : "Tuple to transform must not be null"; //$NON-NLS-1$
		assert (result != null) : "Output tuple must not be null"; //$NON-NLS-1$
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
	 * <p>
	 * If this transform maps all coordinates onto a point or a line
	 * then it will not have an inverse, since coordinates that do
	 * not lie on the destination point or line will not have an inverse
	 * mapping.
	 * The <code>determinant</code> method can be used to determine if this
	 * transform has no inverse, in which case an exception will be
	 * thrown if the <code>createInverse</code> method is called.
	 * @return a new <code>Transform2D</code> object representing the
	 * inverse transformation.
	 * @see #determinant()
	 * @throws SingularMatrixException if the matrix cannot be inverted.
	 */
	@Pure
	public Transform2D createInverse() {
		double det = this.m00 * this.m11 - this.m01 * this.m10;
		if (Math.abs(det) <= Double.MIN_VALUE) {
			throw new SingularMatrixException("Determinant is "+det); //$NON-NLS-1$
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
	 * <p>
	 * If this transform maps all coordinates onto a point or a line
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
		double det = this.m00 * this.m11 - this.m01 * this.m10;
		if (Math.abs(det) <= Double.MIN_VALUE) {
			throw new SingularMatrixException("Determinant is "+det); //$NON-NLS-1$
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
	 * <p>
	 * If this transform maps all coordinates onto a point or a line
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
	public void invert(Matrix3f matrix) {
		assert (matrix != null) : "Matrix must not be null"; //$NON-NLS-1$
		double det = matrix.getM00() * matrix.getM11() - matrix.getM01() * matrix.getM10();
		if (MathUtil.isEpsilonZero(det)) {
			throw new SingularMatrixException("Determinant is too small: "+det); //$NON-NLS-1$
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
