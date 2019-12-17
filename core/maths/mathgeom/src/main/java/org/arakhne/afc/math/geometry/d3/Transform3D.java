/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2019 The original authors, and other authors.
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

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.matrix.Matrix4d;

/** A 3D transformation.
 * Is represented internally as a 4x4 floating point matrix. The
 * mathematical representation is row major, as in traditional
 * matrix mathematics.
 *
 * <p>The transformation matrix is:
 * <pre><code>
 * | r11 | r12 | r13 | Tx |
 * | r21 | r22 | r23 | Ty |
 * | r31 | r32 | r33 | Tz |
 * | 0   | 0   | 0   | 1  |
 * </code></pre>
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
	@SuppressWarnings("checkstyle:parameternumber")
    public Transform3D(double m00, double m01, double m02, double m03, double m10, double m11, double m12, double m13, double m20,
            double m21, double m22, double m23) {
		super(m00, m01, m02, m03, m10, m11, m12, m13, m20, m21, m22, m23, 0f, 0f, 0f, 1f);
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
	 * The scaling and the shearing are not changed.
	 *
	 * <p>After a call to this function, the matrix will
	 * contains (? means any value):
	 * <pre>
	 *          [   ?    ?    x   ]
	 *          [   ?    ?    y   ]
	 *          [   ?    ?    z   ]
	 *          [   ?    ?    ?   ]
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
	}

	/** Set the position.
	 *
	 * <p>This function changes only the elements of
	 * the matrix related to the translation.
	 * The scaling and the shearing are not changed.
	 *
	 * <p>After a call to this function, the matrix will
	 * contains (? means any value):
	 * <pre>
	 *          [   ?    ?    t.x   ]
	 *          [   ?    ?    t.y   ]
	 *          [   ?    ?    t.z   ]
	 *          [   ?    ?    ?     ]
	 * </pre>
	 *
	 * @param translation the translation
	 * @see #makeTranslationMatrix(double, double, double)
	 */
	public void setTranslation(Tuple3D<?> translation) {
		this.m03 = translation.getX();
		this.m13 = translation.getY();
		this.m23 = translation.getZ();
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
	public void translate(Vector3D<?, ?> translation) {
		this.m03 += translation.getX();
		this.m13 += translation.getY();
		this.m23 += translation.getZ();
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
    public void setRotation(Quaternion rotation) {
        this.m00 = 1.0f - 2.0f * rotation.getY() * rotation.getY() - 2.0f * rotation.getZ() * rotation.getZ();
        this.m10 = 2.0f * (rotation.getX() * rotation.getY() + rotation.getW() * rotation.getZ());
        this.m20 = 2.0f * (rotation.getX() * rotation.getZ() - rotation.getW() * rotation.getY());

        this.m01 = 2.0f * (rotation.getX() * rotation.getY() - rotation.getW() * rotation.getZ());
        this.m11 = 1.0f - 2.0f * rotation.getX() * rotation.getX() - 2.0f * rotation.getZ() * rotation.getZ();
        this.m21 = 2.0f * (rotation.getY() * rotation.getZ() + rotation.getW() * rotation.getX());

        this.m02 = 2.0f * (rotation.getX() * rotation.getZ() + rotation.getW() * rotation.getY());
        this.m12 = 2.0f * (rotation.getY() * rotation.getZ() - rotation.getW() * rotation.getX());
        this.m22 = 1.0f - 2.0f * rotation.getX() * rotation.getX() - 2.0f * rotation.getY() * rotation.getY();
    }

    /** Set this transform's rotation.
     * @param angle the rotation angle.
     */
    public void setRotation(double angle) {
        // TODO
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
    public void rotate(Quaternion rotation) {
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
     * contains (? means any value, and r a value from
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
    public final void makeRotationMatrix(Quaternion rotation) {
        this.m00 = 1.0f - 2.0f * rotation.getY() * rotation.getY() - 2.0f * rotation.getZ() * rotation.getZ();
        this.m10 = 2.0f * (rotation.getX() * rotation.getY() + rotation.getW() * rotation.getZ());
        this.m20 = 2.0f * (rotation.getX() * rotation.getZ() - rotation.getW() * rotation.getY());

        this.m01 = 2.0f * (rotation.getX() * rotation.getY() - rotation.getW() * rotation.getZ());
        this.m11 = 1.0f - 2.0f * rotation.getX() * rotation.getX() - 2.0f * rotation.getZ() * rotation.getZ();
        this.m21 = 2.0f * (rotation.getY() * rotation.getZ() + rotation.getW() * rotation.getX());

        this.m02 = 2.0f * (rotation.getX() * rotation.getZ() + rotation.getW() * rotation.getY());
        this.m12 = 2.0f * (rotation.getY() * rotation.getZ() - rotation.getW() * rotation.getX());
        this.m22 = 1.0f - 2.0f * rotation.getX() * rotation.getX() - 2.0f * rotation.getY() * rotation.getY();

        this.m03 = 0.0;
        this.m13 = 0.0;
        this.m23 = 0.0;

        this.m30 = 0.0;
        this.m31 = 0.0;
        this.m32 = 0.0;
        this.m33 = 1.0;
	}

    /** Sets the value of this matrix to a rotation matrix, and no translation.
     * @param angle the angle of the rotation
     */
    public void makeRotationMatrix(double angle) {
        // TODO
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
		this.m00 = 1f;
		this.m01 = 0f;
		this.m02 = 0f;
		this.m03 = dx;

		this.m10 = 0f;
		this.m11 = 1f;
		this.m12 = 0f;
		this.m13 = dy;

		this.m20 = 0f;
		this.m21 = 0f;
		this.m22 = 1f;
		this.m23 = dz;

		this.m30 = 0f;
		this.m31 = 0f;
		this.m32 = 0f;
		this.m33 = 1f;
	}

	/**
	 * Multiply this matrix by the tuple t and place the result back into the
	 * tuple (t = this*t).
	 *
	 * @param t
	 *            the tuple to be multiplied by this matrix and then replaced
	 */
	public void transform(Tuple3D<?> t) {
		final double x = this.m00 * t.getX() + this.m01 * t.getY() + this.m02 * t.getZ() + this.m03;
		final double y = this.m10 * t.getX() + this.m11 * t.getY() + this.m12 * t.getZ() + this.m13;
		final double z = this.m20 * t.getX() + this.m21 * t.getY() + this.m22 * t.getZ() + this.m23;
		t.set(x, y, z);
	}

	/**
	 * Multiply this matrix by the tuple t and and place the result into the
	 * tuple "result" (result = this*t).
	 *
	 * @param t
	 *            the tuple to be multiplied by this matrix
	 * @param result
	 *            the tuple into which the product is placed
	 */
	public void transform(Tuple3D<?> t, Tuple3D<?> result) {
		result.set(
				this.m00 * t.getX() + this.m01 * t.getY() + this.m02 * t.getZ() + this.m03,
				this.m10 * t.getX() + this.m11 * t.getY() + this.m12 * t.getZ() + this.m13,
				this.m20 * t.getX() + this.m21 * t.getY() + this.m22 * t.getZ() + this.m23);
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
	@SuppressWarnings("checkstyle:parameternumber")
	public void set(double m00, double m01, double m02, double m03, double m10, double m11, double m12, double m13,
            double m20, double m21, double m22, double m23) {
		set(m00, m01, m02, m03, m10, m11, m12, m13, m20, m21, m22, m23, 0f, 0f, 0f, 1f);
	}

}
