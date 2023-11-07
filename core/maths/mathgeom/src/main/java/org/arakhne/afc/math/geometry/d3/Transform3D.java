/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2023 The original authors and other contributors.
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

package org.arakhne.afc.math.geometry.d3;

import org.arakhne.afc.math.matrix.Matrix4d;
import org.arakhne.afc.vmutil.asserts.AssertMessages;
import org.eclipse.xtext.xbase.lib.Pure;

/** A 3D transformation.
 * Is represented internally as a 4x4 floating point matrix. The
 * mathematical representation is row major, as in traditional
 * matrix mathematics.
 *
 * <p>The transformation matrix is:
 * <pre>{@code 
 * | r11 | r12 | r13 | Tx |
 * | r21 | r22 | r23 | Ty |
 * | r31 | r32 | r33 | Tz |
 * | 0   | 0   | 0   | 1  |
 * }</pre>
 *
 * @author $Author: tpiotrow$
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public class Transform3D extends Matrix4d {

	/** This is the identifity transformation.
	 */
	public static final Transform3D IDENTITY = new Transform3D();

	private static final long serialVersionUID = -8427812783666663224L;

	/**
	 * Constructs a new Transform3D object and sets it to the identity transformation.
	 */
	public Transform3D() {
		setIdentity();
	}

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
	 */
	public Transform3D(double m00, double m01, double m02, double m03, double m10, double m11, double m12, double m13, double m20,
			double m21, double m22, double m23) {
		super(m00, m01, m02, m03, m10, m11, m12, m13, m20, m21, m22, m23, 0., 0., 0., 1.);
	}

	/**
	 * Constructs a new Transform2D object and initializes it from the
	 * specified transform.
	 *
	 * @param transform the transformation to copy.
	 */
	public Transform3D(Transform3D transform) {
		super(transform);
	}

	/** Constructor by copy.
	 *
	 * @param matrix the matrix to copy.
	 */
	public Transform3D(Matrix4d matrix) {
		super(matrix);
	}

	@Pure
	@Override
	public Transform3D  clone() {
		return (Transform3D) super.clone();
	}

	/** Set the position.
	 *
	 * <p>This function changes only the elements of
	 * the matrix related to the translation.
	 * The rotating, scaling and the shearing are not changed.
	 *
	 * <p>After a call to this function, the matrix will
	 * contains (? means any value):
	 * <pre>
	 *          [   ?    ?    ?    x   ]
	 *          [   ?    ?    ?    y   ]
	 *          [   ?    ?    ?    z   ]
	 *          [   ?    ?    ?    ?   ]
	 * </pre>
	 *
	 * @param x x translation.
	 * @param y y translation.
	 * @param z z translation.
	 * @see #makeTranslationMatrix(double, double, double)
	 */
	public void setTranslation(double x, double y, double z) {
		this.m03 = x;
		this.m13 = y;
		this.m23 = z;

		this.isIdentity = null;
	}

	/** Set the position.
	 *
	 * <p>This function changes only the elements of
	 * the matrix related to the translation.
	 * The rotating, scaling and the shearing are not changed.
	 *
	 * <p>After a call to this function, the matrix will
	 * contains (? means any value):
	 * <pre>
	 *          [   ?    ?    ?    t.x   ]
	 *          [   ?    ?    ?    t.y   ]
	 *          [   ?    ?    ?    t.z   ]
	 *          [   ?    ?    ?    ?     ]
	 * </pre>
	 *
	 * @param translation the translation
	 * @see #makeTranslationMatrix(double, double, double)
	 */
	public void setTranslation(Tuple3D<?> translation) {
		this.m03 = translation.getX();
		this.m13 = translation.getY();
		this.m23 = translation.getZ();

		this.isIdentity = null;
	}

	/** Translate the position.
	 *
	 * <p>This function is equivalent to:
	 * <pre>
	 * this = this *  [   0    0    0    dx   ]
	 *                [   0    0    0    dy   ]
	 *                [   0    0    0    dz   ]
	 *                [   0    0    0    1    ]
	 * </pre>
	 *
	 * @param dx the x translation
	 * @param dy the y translation
	 * @param dz the z translation
	 */
	public void translate(double dx, double dy, double dz) {
		this.m03 += dx;
		this.m13 += dy;
		this.m23 += dz;

		this.isIdentity = null;
	}

	/** Translate the position.
	 *
	 * <p>This function is equivalent to:
	 * <pre>
	 * this = this *  [   0    0    0    t.x   ]
	 *                [   0    0    0    t.y   ]
	 *                [   0    0    0    t.z   ]
	 *                [   0    0    0    1     ]
	 * </pre>
	 *
	 * @param translation the translation
	 */
	public void translate(Vector3D<?, ?, ?> translation) {
		this.m03 += translation.getX();
		this.m13 += translation.getY();
		this.m23 += translation.getZ();

		this.isIdentity = null;
	}

	/** Replies the X translation.
	 *
	 * @return the amount
	 */
	@Pure
	public double getTranslationX() {
		return this.m03;
	}

	/** Replies the Y translation.
	 *
	 * @return the amount
	 */
	@Pure
	public double getTranslationY() {
		return this.m13;
	}

	/** Replies the Z translation.
	 *
	 * @return the amount
	 */
	@Pure
	public double getTranslationZ() {
		return this.m23;
	}

	/** Replies the translation vector that is encoded in the matrix.
	 *
	 * <p>This function gets the factors in the following cells of the matrix:
	 * <pre>
	 *          [   ?    ?    ?    t.x   ]
	 *          [   ?    ?    ?    t.y   ]
	 *          [   ?    ?    ?    t.z   ]
	 *          [   ?    ?    ?    ?     ]
	 * </pre>
	 *
	 * @param <V> the type of the vector.
	 * @param <P> the type of the point.
	 * @param <Q> the type of the quaternion.
	 * @return the translation vector.
	 * @since 18.0
	 */
	@Pure
	public <V extends Vector3D<? super V, ? super P, ? super Q>, P extends Point3D<? super P, ? super V, ? super Q>, Q extends Quaternion<? super P, ? super V, ? super Q>> V getTranslation(GeomFactory3D<V, P, Q> factory) {
		assert factory != null : AssertMessages.notNullParameter();
		return factory.newVector(getTranslationX(), getTranslationY(), getTranslationZ());
	}

	/** Replies the quaternion that is encoded in the matrix.
	 *
	 * @param <V> the type of the vector.
	 * @param <P> the type of the point.
	 * @param <Q> the type of the quaternion.
	 * @return the quaternion.
	 * @since 18.0
	 */
	@Pure
	public <V extends Vector3D<? super V, ? super P, ? super Q>, P extends Point3D<? super P, ? super V, ? super Q>, Q extends Quaternion<? super P, ? super V, ? super Q>> Q getRotation(GeomFactory3D<V, P, Q> factory) {
		assert factory != null : AssertMessages.notNullParameter();

		// Compute the numerators for each component from the matrix diagonal 
		final double nw = getM00() + getM11() + getM22() + 1;
		final double nx = getM00() - getM11() - getM22() + 1;
		final double ny = -getM00() + getM11() - getM22() + 1;
		final double nz = -getM00() - getM11() + getM22() + 1;

		// Select the largest numerator to minimize computation errors and computes the other components
		final double x;
		final double y;
		final double z;
		final double w;
		if (nw >= nx && nw >= ny && nw >= nz) {
			w = Math.sqrt(nw) / 2.;
			final double wwww = 4. * w;
			x = (getM12() - getM21()) / wwww;
			y = (getM20() - getM02()) / wwww;
			z = (getM01() - getM10()) / wwww;
		} else if (nx >= nw && nx >= ny && nx >= nz) {
			x = Math.sqrt(nx) / 2.;
			final double xxxx = 4. * x;
			w = (getM12() - getM21()) / xxxx;
			y = (getM01() - getM10()) / xxxx;
			z = (getM20() - getM02()) / xxxx;
		} else if (ny >= nw && ny >= nx && ny >= nz) {
			y = Math.sqrt(ny) / 2.;
			final double yyyy = 4. * y;
			w = (getM20() - getM02()) / yyyy;
			x = (getM01() - getM10()) / yyyy;
			z = (getM12() - getM21()) / yyyy;
		} else {
			assert nz >= nw && nz >= nx && nz >= ny;
			z = Math.sqrt(nz) / 2.;
			final double zzzz = 4. * z;
			w = (getM01() - getM10()) / zzzz;
			x = (getM21() - getM12()) / zzzz;
			y = (getM12() - getM21()) / zzzz;
		}
		return factory.newQuaternion(x, y, z, -w);
	}

	/**
	 * Set the rotation for the object but do not change the translation.
	 *
	 * <p>This function changes only the elements of
	 * the matrix related to the rotation.
	 * The translation is not changed.
	 *
	 * <p>After a call to this function, the matrix will
	 * contains (? means any value, and r is the translation
	 * of the quaternion as a 3x3 matrix):
	 * <pre>
	 *          [   r   r   r   ?   ]
	 *          [   r   r   r   ?   ]
	 *          [   r   r   r   ?   ]
	 *          [   ?   ?   ?   ?   ]
	 * </pre>
	 *
	 * @param rotation the rotation
	 * @see #makeRotationMatrix(Quaternion)
	 */
	public void setRotation(Quaternion<?, ?, ?> rotation) {
		assert rotation != null : AssertMessages.notNullParameter();
		setRotation(rotation.getX(), rotation.getY(), rotation.getZ(), rotation.getW());
	}
	
	/**
	 * Set the rotation for the object but do not change the translation.
	 *
	 * <p>This function changes only the elements of
	 * the matrix related to the rotation.
	 * The translation is not changed.
	 *
	 * <p>After a call to this function, the matrix will
	 * contains (? means any value, and r is the translation
	 * of the quaternion as a 3x3 matrix):
	 * <pre>
	 *          [   r   r   r   ?   ]
	 *          [   r   r   r   ?   ]
	 *          [   r   r   r   ?   ]
	 *          [   ?   ?   ?   ?   ]
	 * </pre>
	 *
	 * @param x x component of the rotation quaternion.
	 * @param y y component of the rotation quaternion.
	 * @param z z component of the rotation quaternion.
	 * @param w w component of the rotation quaternion.
	 * @see #makeRotationMatrix(Quaternion)
	 */
	public void setRotation(double x, double y, double z, double w) {
		this.m00 = 1. - 2. * y * y - 2. * z * z;
		this.m10 = 2. * (x * y + w * z);
		this.m20 = 2. * (x * z - w * y);

		this.m01 = 2. * (x * y - w * z);
		this.m11 = 1. - 2. * x * x - 2. * z * z;
		this.m21 = 2. * (y * z + w * x);

		this.m02 = 2. * (x * z + w * y);
		this.m12 = 2. * (y * z - w * x);
		this.m22 = 1. - 2. * x * x - 2. * y * y;

		this.isIdentity = null;
	}

	/**
	 * Rotate the object.
	 *
	 * <p>This function is equivalent to (where r is the translation
	 * of the quaternion as a 3x3 matrix):
	 * <pre>
	 * this = this *  [   r    r     r     0   ]
	 *                [   r    r     r     0   ]
	 *                [   r    r     r     0   ]
	 *                [   0    0     0     1   ]
	 * </pre>
	 *
	 * @param rotation the rotationi
	 */
	public void rotate(Quaternion<?, ?, ?> rotation) {
		final Transform3D m = new Transform3D();
		m.makeRotationMatrix(rotation);
		mul(m);
	}

	/**
	 * Sets the value of this matrix to a rotation matrix, and no translation.
	 *
	 * <p>This function changes all the elements of
	 * the matrix, including the translation.
	 *
	 * <p>After a call to this function, the matrix will
	 * contains (r a value from
	 * the quaternion):
	 * <pre>
	 *          [   r  r  r  0   ]
	 *          [   r  r  r  0   ]
	 *          [   r  r  r  0   ]
	 *          [   0  0  0  1   ]
	 * </pre>
	 *
	 * @param rotation the rotation
	 * @see #setRotation(Quaternion)
	 */
	public final void makeRotationMatrix(Quaternion<?, ?, ?> rotation) {
		assert rotation != null : AssertMessages.notNullParameter();
		makeRotationMatrix(rotation.getX(), rotation.getY(), rotation.getZ(), rotation.getW());
	}

	/**
	 * Sets the value of this matrix to a rotation matrix, and no translation.
	 *
	 * <p>This function changes all the elements of
	 * the matrix, including the translation.
	 *
	 * <p>After a call to this function, the matrix will
	 * contains (r a value from
	 * the quaternion):
	 * <pre>
	 *          [   r  r  r  0   ]
	 *          [   r  r  r  0   ]
	 *          [   r  r  r  0   ]
	 *          [   0  0  0  1   ]
	 * </pre>
	 *
	 * @param x x component of the rotation quaternion.
	 * @param y y component of the rotation quaternion.
	 * @param z z component of the rotation quaternion.
	 * @param w w component of the rotation quaternion.
	 * @see #setRotation(Quaternion)
	 */
	public final void makeRotationMatrix(double x, double y, double z, double w) {
		setRotation(x, y, z, w);

		this.m03 = 0.;
		this.m13 = 0.;
		this.m23 = 0.;

		this.m30 = 0.;
		this.m31 = 0.;
		this.m32 = 0.;
		this.m33 = 1.;

		this.isIdentity = null;
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
	 *          [   1    0    0    dx   ]
	 *          [   0    1    0    dy   ]
	 *          [   0    0    1    dz   ]
	 *          [   0    0    0    1    ]
	 * </pre>
	 *
	 * @param dx is the position to put in the matrix.
	 * @param dy is the position to put in the matrix.
	 * @param dz is the position to put in the matrix.
	 * @see #setTranslation(double, double, double)
	 * @see #setTranslation(Tuple3D)
	 */
	public final void makeTranslationMatrix(double dx, double dy, double dz) {
		this.m00 = 1.;
		this.m01 = 0.;
		this.m02 = 0.;
		this.m03 = dx;

		this.m10 = 0.;
		this.m11 = 1.;
		this.m12 = 0.;
		this.m13 = dy;

		this.m20 = 0.;
		this.m21 = 0.;
		this.m22 = 1.;
		this.m23 = dz;

		this.m30 = 0.;
		this.m31 = 0.;
		this.m32 = 0.;
		this.m33 = 1.;

		this.isIdentity = null;
	}

	/**
	 * Multiply this matrix by the point t and place the result back into the
	 * tuple (t = this*t). This function applies the rotation and the translation.
	 *
	 * @param t
	 *            the tuple to be multiplied by this matrix and then replaced
	 * @since 18.0
	 */
	public void transform(Point3D<?, ?, ?> t) {
		final double x = t.getX();
		final double y = t.getY();
		final double z = t.getZ();
		
		final double nx = this.m00 * x + this.m01 * y + this.m02 * z + this.m03;
		final double ny = this.m10 * x + this.m11 * y + this.m12 * z + this.m13;
		final double nz = this.m20 * x + this.m21 * y + this.m22 * z + this.m23;
		t.set(nx, ny, nz);
		this.isIdentity = null;
	}

	/**
	 * Multiply this matrix by the point t and place the result back into the
	 * tuple (t = this*t). This function applies the rotation only, and NOT the translation.
	 *
	 * @param t
	 *            the tuple to be multiplied by this matrix and then replaced
	 * @since 18.0
	 */
	public void transform(Vector3D<?, ?, ?> t) {
		final double x = t.getX();
		final double y = t.getY();
		final double z = t.getZ();
		
		final double nx = this.m00 * x + this.m01 * y + this.m02 * z;
		final double ny = this.m10 * x + this.m11 * y + this.m12 * z;
		final double nz = this.m20 * x + this.m21 * y + this.m22 * z;
		t.set(nx, ny, nz);
		this.isIdentity = null;
	}

	/**
	 * Multiply this matrix by the tuple t and and place the result into the
	 * tuple "result" (result = this*t).
	 * This function applies the rotation and translation.
	 *
	 * @param t
	 *            the tuple to be multiplied by this matrix
	 * @param result
	 *            the tuple into which the product is placed
	 * @since 18.0
	 */
	public void transform(Point3D<?, ?, ?> t, Point3D<?, ?, ?> result) {
		final double x = t.getX();
		final double y = t.getY();
		final double z = t.getZ();
		result.set(
				this.m00 * x + this.m01 * y + this.m02 * z + this.m03,
				this.m10 * x + this.m11 * y + this.m12 * z + this.m13,
				this.m20 * x + this.m21 * y + this.m22 * z + this.m23);
	}

	/**
	 * Multiply this matrix by the tuple t and and place the result into the
	 * tuple "result" (result = this*t).
	 * This function applies the rotation only, and NOT the translation.
	 *
	 * @param t
	 *            the tuple to be multiplied by this matrix
	 * @param result
	 *            the tuple into which the product is placed
	 * @since 18.0
	 */
	public void transform(Vector3D<?, ?, ?> t, Vector3D<?, ?, ?> result) {
		final double x = t.getX();
		final double y = t.getY();
		final double z = t.getZ();
		result.set(
				this.m00 * x + this.m01 * y + this.m02 * z,
				this.m10 * x + this.m11 * y + this.m12 * z,
				this.m20 * x + this.m21 * y + this.m22 * z);
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
	 */
	public void set(double m00, double m01, double m02, double m03, double m10, double m11, double m12, double m13,
			double m20, double m21, double m22, double m23) {
		set(m00, m01, m02, m03, m10, m11, m12, m13, m20, m21, m22, m23, 0., 0., 0., 1.);
	}

}
