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
package org.arakhne.afc.math.geometry.d2.continuous;

import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.Tuple2D;
import org.arakhne.afc.math.geometry.d2.Vector2D;
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
 * | cos(theta) | -sin(theta) | Tx |
 * | sin(theta) | cos(theta)  | Ty |
 * | 0          | 0           | 1  |
 * </code></pre>
 *
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class Transform2D extends Matrix3f {

	private static final long serialVersionUID = -3437760883865605968L;

	/** This is the identifity transformation.
	 */
	public static final Transform2D IDENTITY = new Transform2D();

	/**
	 * Constructs a new Transform2D object and sets it to the identity transformation.
	 */
	public Transform2D() {
		super(1f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 1f);
	}

	/**
	 * Constructs a new Transform2D object and initializes it from the
	 * specified transform.
	 * 
	 * @param t
	 */
	public Transform2D(Transform2D t) {
		super(t);
	}

	/**
	 * @param m
	 */
	public Transform2D(Matrix3f m) {
		super(m);
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
	@SuppressWarnings("hiding")
	public Transform2D(double m00, double m01, double m02, double m10, double m11, double m12) {
		super(m00, m01, m02, m10, m11, m12, 0f, 0f, 1f);
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
	 * @param t
	 * @see #makeTranslationMatrix(double, double)
	 */
	public void setTranslation(Tuple2D<?> t) {
		this.m02 = t.getX();
		this.m12 = t.getY();
	}

	/** Translate the position.
	 * <p>
	 * This function is equivalent to:
	 * <pre>
	 * this = this *  [   0    0    dx   ]
	 *                [   0    0    dy   ]
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
	 * this = this *  [   0    0    t.x   ]
	 *                [   0    0    t.y   ]
	 *                [   0    0    1     ]
	 * </pre>
	 * 
	 * @param t
	 */
	public void translate(Vector2D t) {
		translate(t.getX(), t.getY());
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
	 * @return the amount
	 */
	@Pure
	public Vector2f getTranslationVector() {
		return new Vector2f(this.m02, this.m12);
	}

	/**
	 * Replies the rotation for the object (theta).
	 * 
	 * @return the amount
	 */
	@Pure
	public double getRotation() {
		double cosAngle = Math.acos(this.m00);
		double sinAngle = Math.asin(this.m10);
		return (sinAngle<0f) ? -cosAngle : cosAngle;
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
		if (sin == 1f) {
			rotate90();
		}
		else if (sin == -1f) {
			rotate270();
		}
		else {
			double cos = Math.cos(theta);
			if (cos == -1f) {
				rotate180();
			}
			else if (cos != 1f) {
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
	 * <p>
	 * After a call to this function, the matrix will
	 * contains (? means any value):
	 * <pre>
	 *          [   sx  ?   ?   ]
	 *          [   ?   sy  ?   ]
	 *          [   ?   ?   ?   ]
	 * </pre>
	 * 
	 * @param sx
	 * @param sy
	 * @see #makeScaleMatrix(double, double)
	 */
	public void setScale(double sx, double sy) {
		this.m00 = sx;
		this.m11 = sy;
	}

	/** Set the scale.
	 * <p>
	 * This function changes only the elements of 
	 * the matrix related to the scaling (m00,
	 * m11). The shearing and the translation are not changed. 
	 * <p>
	 * After a call to this function, the matrix will
	 * contains (? means any value):
	 * <pre>
	 *          [   t.x  ?    ?   ]
	 *          [   ?    t.y  ?   ]
	 *          [   ?    ?    ?   ]
	 * </pre>
	 * 
	 * @param t
	 * @see #makeScaleMatrix(double, double)
	 */
	public void setScale(Tuple2D<?> t) {
		this.m00 = t.getX();
		this.m11 = t.getY();
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
	 * @param sx
	 * @param sy
	 */
	public void scale(double sx, double sy) {
        this.m00 *= sx;
        this.m11 *= sy;
        this.m01 *= sy;
        this.m10 *= sx;
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
	 * @param t
	 */
	public void scale(Tuple2D<?> t) {
		scale(t.getX(), t.getY());
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
	 * @return the amount
	 */
	@Pure
	public Vector2f getScaleVector() {
		return new Vector2f(this.m00, this.m11);
	}

	/** Set the shearing elements.
	 * <p>
	 * This function changes only the elements of 
	 * the matrix related to the shearing (m01,
	 * m10). The scaling and the translation are not changed. 
	 * <p>
	 * After a call to this function, the matrix will
	 * contains (? means any value):
	 * <pre>
	 *          [   ?    shx  ?   ]
	 *          [   shy  ?    ?   ]
	 *          [   ?    ?    ?   ]
	 * </pre>
	 * 
	 * @param shx
	 * @param shy
	 * @see #makeShearMatrix(double, double)
	 */
	public void setShear(double shx,  double shy) {
		this.m01 = shx;
		this.m10 = shy;
	}

	/** Set the shearing elements.
	 * <p>
	 * This function changes only the elements of 
	 * the matrix related to the shearing (m01,
	 * m10). The scaling and the translation are not changed. 
	 * <p>
	 * After a call to this function, the matrix will
	 * contains (? means any value):
	 * <pre>
	 *          [   ?    t.x  ?   ]
	 *          [   t.y  ?    ?   ]
	 *          [   ?    ?    ?   ]
	 * </pre>
	 * 
	 * @param t
	 * @see #makeShearMatrix(double, double)
	 */
	public void setShear(Tuple2D<?> t) {
		this.m01 = t.getX();
		this.m10 = t.getY();
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
	 * @param shx
	 * @param shy
	 */
	public void shear(double shx, double shy) {
		double M0, M1;
		M0 = this.m00;
		M1 = this.m01;
		
		this.m00 = M0 + M1 * shy;
		this.m01 = M0 * shx + M1;

		M0 = this.m10;
		M1 = this.m11;
		this.m10 = M0 + M1 * shy;
		this.m11 = M0 * shx + M1;
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
	 * @param t
	 */
	public void shear(Tuple2D<?> t) {
		shear(t.getX(), t.getY());
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
	 * @return the amount
	 */
	@Pure
	public Vector2f getShearVector() {
		return new Vector2f(this.m01, this.m10);
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
		this.m02 = 0f;

		this.m10 = sinAngle;
		this.m11 = cosAngle;
		this.m12 = 0f;

		this.m20 = 0f;
		this.m21 = 0f;
		this.m22 = 1f;
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
		this.m00 = 1f;
		this.m01 = 0f;
		this.m02 = dx;

		this.m10 = 0f;
		this.m11 = 1f;
		this.m12 = dy;

		this.m20 = 0f;
		this.m21 = 0f;
		this.m22 = 1f;
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
	 * @param sx is the scaling along X.
	 * @param sy is the scaling along Y.
	 * @see #setScale(double, double)
	 * @see #setScale(Tuple2D)
	 */
	public final void makeScaleMatrix(double sx, double sy) {
		this.m00 = sx;
		this.m01 = 0f;
		this.m02 = 0f;

		this.m10 = 0f;
		this.m11 = sy;
		this.m12 = 0f;

		this.m20 = 0f;
		this.m21 = 0f;
		this.m22 = 1f;
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
	 * @param shx is the shearing along X.
	 * @param shy is the shearing along Y.
	 * @see #setShear(double, double)
	 * @see #setShear(Tuple2D)
	 */
	public final void makeShearMatrix(double shx, double shy) {
		this.m00 = 1f;
		this.m01 = shx;
		this.m02 = 0f;

		this.m10 = shy;
		this.m11 = 1f;
		this.m12 = 0f;

		this.m20 = 0f;
		this.m21 = 0f;
		this.m22 = 1f;
	}

	/**
	 * Multiply this matrix by the tuple t and place the result back into the
	 * tuple (t = this*t).
	 * 
	 * @param t
	 *            the tuple to be multiplied by this matrix and then replaced
	 */
	public void transform(Tuple2D<?> t) {
		double x, y;
		x = this.m00 * t.getX() + this.m01 * t.getY() + this.m02;
		y = this.m10 * t.getX() + this.m11 * t.getY() + this.m12;
		t.set(x, y);
	}

	/** Multiply this matrix by the tuple <x,y> and return the result.
	 * <p>
	 * This function is equivalent to:
	 * <pre>
	 * result = this *  [   x   ]
	 *                  [   y   ]
	 *                  [   1   ]
	 * </pre>
	 * 
	 * @param x
	 * @param y
	 * @return the transformation result
	 */
	@Pure
	public Point2D transform(double x, double y) {
		double rx, ry;
		rx = this.m00 * x + this.m01 * y + this.m02;
		ry = this.m10 * x + this.m11 * y + this.m12;
		return new Point2f(rx, ry);
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
	 * @param t
	 *            the tuple to be multiplied by this matrix
	 * @param result
	 *            the tuple into which the product is placed
	 */
	public void transform(Tuple2D<?> t, Tuple2D<?> result) {
		result.set(
				this.m00 * t.getX() + this.m01 * t.getY() + this.m02,
				this.m10 * t.getX() + this.m11 * t.getY() + this.m12);
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
	@SuppressWarnings("hiding")
	public void set(double m00, double m01, double m02, double m10, double m11, double m12) {
		set(m00, m01, m02, m10, m11, m12, 0f, 0f, 1f);
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
	 * @param m is the matrix to invert
	 * @see #determinant()
	 * @throws SingularMatrixException if the matrix cannot be inverted.
	 */
	@Override
	public void invert(Matrix3f m) {
		double det = m.m00 * m.m11 - m.m01 * m.m10;
		if (Math.abs(det) <= Double.MIN_VALUE) {
			throw new SingularMatrixException("Determinant is "+det); //$NON-NLS-1$
		}
		set(
				m.m11 / det, 
				-m.m01 / det, 
				(m.m01 * m.m12 - m.m11 * m.m02) / det, 
				-m.m10 / det, 
				m.m00 / det, 
				(m.m10 * m.m02 - m.m00 * m.m12) / det);
	}

}
