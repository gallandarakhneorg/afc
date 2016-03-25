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
 * @author $Author: galland$
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
		return (Transform2D)super.clone();
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
	public void translate(Vector2D translation) {
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
	 * @return the <code>translation</code> vector.
	 */
	@Pure
	public Vector2D getTranslationVector(Vector2D translation) {
		translation.set(this.m02, this.m12);
		return translation;
	}

	/**
	 * Replies the rotation for the object (theta).
	 * 
	 * @return the amount
	 */
	@Pure
	public double getRotation() {
		double cosAngle = Math.acos(this.m00);
		// According to the documentation of Math.asin,
		// Math.sign(Math.asin(this.m10)) == Math.sign(this.m10).
		return (this.m10 < 0.) ? -cosAngle : cosAngle;
	}

	/**
	 * Set the rotation for the object (theta).
	 * <p>
	 * This function changes only the elements of 
	 * the matrix related to the rotation (m00,
	 * m01, m10, m11). The translation is not changed. 
	 * <p>
	 * After a call to this function, the matrix will
	 * contains (? means any value):
	 * <pre>
	 *          [   cos(theta)  -sin(theta)  ?   ]
	 *          [   sin(theta)  cos(theta)   ?   ]
	 *          [   ?           ?            ?   ]
	 * </pre>
	 * 
	 * @param theta
	 * @see #makeRotationMatrix(double)
	 */
	public void setRotation(double theta) {
		double cosTheta = Math.cos(theta);
		double sinTheta = Math.sin(theta);
		this.m00 = cosTheta;
		this.m01 = -sinTheta;
		this.m10 = sinTheta;
		this.m11 = cosTheta;
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
	 *          [   sx  ?   ?   ]
	 *          [   ?   sy  ?   ]
	 *          [   ?   ?   ?   ]
	 * </pre>
	 * 
	 * @param scaleX
	 * @param scaleY
	 * @see #makeScaleMatrix(double, double)
	 */
	public void setScale(double scaleX, double scaleY) {
		this.m00 = scaleX;
		this.m11 = scaleY;
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
		this.m00 = tuple.getX();
		this.m11 = tuple.getY();
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
		scale(tuple.getX(), tuple.getY());
	}

	/** Replies the X scaling.
	 * <p>
	 * <pre>
	 *          [   sx   0    0   ]
	 *          [   0    sy   0   ]
	 *          [   0    0    1   ]
	 * </pre>
	 * 
	 * @return the amount
	 */
	@Pure
	public double getScaleX() {
		return this.m00;
	}

	/** Replies the Y scaling.
	 * <p>
	 * <pre>
	 *          [   sx   0    0   ]
	 *          [   0    sy   0   ]
	 *          [   0    0    1   ]
	 * </pre>
	 * 
	 * @return the amount
	 */
	@Pure
	public double getScaleY() {
		return this.m11;
	}

	/** Replies the scaling vector.
	 * <p>
	 * <pre>
	 *          [   sx   0    0   ]
	 *          [   0    sy   0   ]
	 *          [   0    0    1   ]
	 * </pre>
	 * 
	 * @param scale the vector to set with the scaling component.
	 * @return the <code>scale</code> vector.
	 */
	@Pure
	public Vector2D getScaleVector(Vector2D scale) {
		scale.set(this.m00, this.m11);
		return scale;
	}

	/** Set the shearing elements.
	 * <p>
	 * This function changes only the elements of 
	 * the matrix related to the shearing (m01,
	 * m10). The scaling and the translation are not changed. 
	 * The rotation is lost.
	 * <p>
	 * After a call to this function, the matrix will
	 * contains (? means any value):
	 * <pre>
	 *          [   ?    shx  ?   ]
	 *          [   shy  ?    ?   ]
	 *          [   ?    ?    ?   ]
	 * </pre>
	 * 
	 * @param shearX
	 * @param shearY
	 * @see #makeShearMatrix(double, double)
	 */
	public void setShear(double shearX,  double shearY) {
		this.m01 = shearX;
		this.m10 = shearY;
	}

	/** Set the shearing elements.
	 * <p>
	 * This function changes only the elements of 
	 * the matrix related to the shearing (m01,
	 * m10). The scaling and the translation are not changed. 
	 * The rotation is lost.
	 * <p>
	 * After a call to this function, the matrix will
	 * contains (? means any value):
	 * <pre>
	 *          [   ?    t.x  ?   ]
	 *          [   t.y  ?    ?   ]
	 *          [   ?    ?    ?   ]
	 * </pre>
	 * 
	 * @param tuple
	 * @see #makeShearMatrix(double, double)
	 */
	public void setShear(Tuple2D<?> tuple) {
		this.m01 = tuple.getX();
		this.m10 = tuple.getY();
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
	 * this = this *  [   1    t.x  0   ]
	 *                [   t.y  1    0   ]
	 *                [   0    0    1   ]
	 * </pre>
	 * 
	 * @param tuple
	 */
	public void shear(Tuple2D<?> tuple) {
		shear(tuple.getX(), tuple.getY());
	}

	/** Replies the X shearing.
	 * <p>
	 * <pre>
	 *          [   0    shx  0   ]
	 *          [   shy  0    0   ]
	 *          [   0    0    1   ]
	 * </pre>
	 * 
	 * @return the amount
	 */
	@Pure
	public double getShearX() {
		return this.m01;
	}

	/** Replies the Y shearing.
	 * <p>
	 * <pre>
	 *          [   0    shx  0   ]
	 *          [   shy  0    0   ]
	 *          [   0    0    1   ]
	 * </pre>
	 * 
	 * @return the amount
	 */
	@Pure
	public double getShearY() {
		return this.m10;
	}

	/** Replies the shearing vector.
	 * <p>
	 * <pre>
	 *          [   0    shx  0   ]
	 *          [   shy  0    0   ]
	 *          [   0    0    1   ]
	 * </pre>
	 * 
	 * @param shear the vector to set with the shearing component.
	 * @return the <code>shear</code> vector.
	 */
	@Pure
	public Vector2D getShearVector(Vector2D shear) {
		shear.set(this.m01, this.m10);
		return shear;
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
	public final void makeRotationMatrix(double angle) {
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
	public final void makeTranslationMatrix(double dx, double dy) {
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
	public final void makeScaleMatrix(double scaleX, double scaleY) {
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
	 * Sets the value of this matrix to the given scaling, without rotation.
	 * <p>
	 * This function changes all the elements of 
	 * the matrix incuding the scaling and the
	 * translation. 
	 * <p>
	 * After a call to this function, the matrix will
	 * contains (? means any value):
	 * <pre>
	 *          [   1    shx  0   ]
	 *          [   shy  1    0   ]
	 *          [   0    0    1   ]
	 * </pre>
	 * 
	 * @param shearX is the shearing along X.
	 * @param shearY is the shearing along Y.
	 * @see #setShear(double, double)
	 * @see #setShear(Tuple2D)
	 */
	public final void makeShearMatrix(double shearX, double shearY) {
		this.m00 = 1.;
		this.m01 = shearX;
		this.m02 = 0.;

		this.m10 = shearY;
		this.m11 = 1.;
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
