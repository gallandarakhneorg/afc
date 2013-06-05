/* 
 * $Id$
 * 
 * Copyright (C) 2012 Stephane GALLAND.
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
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
package org.arakhne.afc.math.transform;

import org.arakhne.afc.math.continous.object3d.Point3f;
import org.arakhne.afc.math.continous.object3d.Quaternion;
import org.arakhne.afc.math.continous.object3d.Vector3f;
import org.arakhne.afc.math.continous.object4d.AxisAngle4f;
import org.arakhne.afc.math.generic.Tuple3D;
import org.arakhne.afc.math.matrix.Matrix3f;
import org.arakhne.afc.math.matrix.Matrix4f;


/** A 3D transformation.
 * Is represented internally as a 4x4 floating point matrix. The
 * mathematical representation is row major, as in traditional
 * matrix mathematics.
 * <p>
 * The transformation matrix is:
 * <pre><code>
 * | r11 | r12 | r13 | Tx |
 * | r21 | r22 | r23 | Ty |
 * | r31 | r32 | r33 | Tz |
 * | 0   | 0   | 0   | 1  |
 * </code></pre>
 *
 * @author $Author: galland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class Transform3D extends Matrix4f {
	
	private static final long serialVersionUID = -8427812783666663224L;
	
	/** This is the identifity transformation.
	 */
	public static final Transform3D IDENTITY = new Transform3D();

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
	public Transform3D(float m00, float m01, float m02, float m03, float m10, float m11, float m12, float m13, float m20, float m21, float m22, float m23) {
		super(m00, m01, m02, m03, m10, m11, m12, m13, m20, m21, m22, m23, 0f, 0f, 0f, 1f);
	}

	/**
	 * Constructs a new Transform3D object and initializes it from the
	 * specified transform.
	 * 
	 * @param t
	 */
	public Transform3D(Transform3D t) {
		super(t);
	}

	/**
	 * @param m
	 */
	public Transform3D(Matrix4f m) {
		super(m);
	}

	@Override
	public Transform3D  clone() {
		return (Transform3D)super.clone();
	}

	/** Set the position.
	 * <p>
	 * This function changes only the elements of 
	 * the matrix related to the translation.
	 * The scaling and the shearing are not changed. 
	 * <p>
	 * After a call to this function, the matrix will
	 * contains (? means any value):
	 * <pre>
	 *          [   ?    ?    x   ]
	 *          [   ?    ?    y   ]
	 *          [   ?    ?    z   ]
	 *          [   ?    ?    ?   ]
	 * </pre>
	 * 
	 * @param x
	 * @param y
	 * @param z
	 * @see #makeTranslationMatrix(float, float, float)
	 */
	public void setTranslation(float x, float y, float z) {
		this.m03 = x;
		this.m13 = y;
		this.m23 = z;
	}
	
	/** Set the position.
	 * <p>
	 * This function changes only the elements of 
	 * the matrix related to the translation.
	 * The scaling and the shearing are not changed. 
	 * <p>
	 * After a call to this function, the matrix will
	 * contains (? means any value):
	 * <pre>
	 *          [   ?    ?    t.x   ]
	 *          [   ?    ?    t.y   ]
	 *          [   ?    ?    t.z   ]
	 *          [   ?    ?    ?     ]
	 * </pre>
	 * 
	 * @param t
	 * @see #makeTranslationMatrix(float, float, float)
	 */
	public void setTranslation(Tuple3D<?> t) {
		this.m03 = t.getX();
		this.m13 = t.getY();
		this.m23 = t.getZ();
	}

	/** Translate the position.
	 * <p>
	 * This function is equivalent to:
	 * <pre>
	 * this = this *  [   0    0    0    dx   ]
	 *                [   0    0    0    dy   ]
	 *                [   0    0    0    dz   ]
	 *                [   0    0    0    1    ]
	 * </pre>
	 * 
	 * @param dx
	 * @param dy
	 * @param dz
	 */
	public void translate(float dx, float dy, float dz) {
		this.m03 += dx;
		this.m13 += dy;
		this.m23 += dz;
	}
	
	/** Translate the position.
	 * <p>
	 * This function is equivalent to:
	 * <pre>
	 * this = this *  [   0    0    0    t.x   ]
	 *                [   0    0    0    t.y   ]
	 *                [   0    0    0    t.z   ]
	 *                [   0    0    0    1     ]
	 * </pre>
	 * 
	 * @param t
	 */
	public void translate(Vector3f t) {
		this.m03 += t.getX();
		this.m13 += t.getY();
		this.m23 += t.getZ();
	}

	/** Replies the X translation.
	 * 
	 * @return the amount
	 */
	public float getTranslationX() {
		return this.m03;
	}

	/** Replies the Y translation.
	 * 
	 * @return the amount
	 */
	public float getTranslationY() {
		return this.m13;
	}

	/** Replies the Z translation.
	 * 
	 * @return the amount
	 */
	public float getTranslationZ() {
		return this.m23;
	}

	/** Replies the translation.
	 * 
	 * @return the amount
	 */
	public Vector3f getTranslation() {
		return new Vector3f(this.m03, this.m13, this.m23);
	}

	/**
     * Replies the rotation for the object.
	 * 
	 * @return the amount
     */
    public Quaternion getRotation() {
    	Quaternion q = new Quaternion();
    	q.setFromMatrix(this);
    	return q;
    }

    /**
     * Set the rotation for the object but do not change the translation.
	 * <p>
	 * This function changes only the elements of 
	 * the matrix related to the rotation.
	 * The translation is not changed. 
	 * <p>
	 * After a call to this function, the matrix will
	 * contains (? means any value, and r is the translation
	 * of the quaternion as a 3x3 matrix):
	 * <pre>
	 *          [   r   r   r   ?   ]
	 *          [   r   r   r   ?   ]
	 *          [   r   r   r   ?   ]
	 *          [   ?   ?   ?   ?   ]
	 * </pre>
     * 
     * @param rotation
     * @see #makeRotationMatrix(Quaternion)
     */
    @Override
	public void setRotation(Quaternion rotation) {
		this.m00 = (1.0f - 2.0f*rotation.getY()*rotation.getY() - 2.0f*rotation.getZ()*rotation.getZ());
        this.m10 = (2.0f*(rotation.getX()*rotation.getY() + rotation.getW()*rotation.getZ()));
        this.m20 = (2.0f*(rotation.getX()*rotation.getZ() - rotation.getW()*rotation.getY()));

        this.m01 = (2.0f*(rotation.getX()*rotation.getY() - rotation.getW()*rotation.getZ()));
        this.m11 = (1.0f - 2.0f*rotation.getX()*rotation.getX() - 2.0f*rotation.getZ()*rotation.getZ());
        this.m21 = (2.0f*(rotation.getY()*rotation.getZ() + rotation.getW()*rotation.getX()));

        this.m02 = (2.0f*(rotation.getX()*rotation.getZ() + rotation.getW()*rotation.getY()));
        this.m12 = (2.0f*(rotation.getY()*rotation.getZ() - rotation.getW()*rotation.getX()));
        this.m22 = (1.0f - 2.0f*rotation.getX()*rotation.getX() - 2.0f*rotation.getY()*rotation.getY());
    }


    /**   
     * Sets the rotational component (upper 3x3) of this matrix to the
     * matrix equivalent values of the axis-angle argument; the other
     * elements of this matrix are unchanged; a singular value
     * decomposition is performed on this object's upper 3x3 matrix to
     * factor out the scale, then this object's upper 3x3 matrix components
     * are replaced by the matrix equivalent of the axis-angle,
     * and then the scale is reapplied to the rotational components.
     * @param a1 the axis-angle to be converted (x, y, z, angle)
     */
    public final void setRotation(AxisAngle4f a1)
    {  
	float[]    tmp_rot = new float[9];  // scratch matrix
	float[]    tmp_scale = new float[3];  // scratch matrix
	
	getScaleRotate( tmp_scale, tmp_rot );

        float mag = (float) (1.0f/Math.sqrt( a1.x*a1.x + a1.y*a1.y + a1.z*a1.z));
        float ax = a1.x*mag;
        float ay = a1.y*mag;
        float az = a1.z*mag;

        float sinTheta = (float) Math.sin(a1.angle);
        float cosTheta = (float) Math.cos(a1.angle);
        float t = 1.0f - cosTheta;

        float xz = a1.x * a1.z;
        float xy = a1.x * a1.y;
        float yz = a1.y * a1.z;

        this.m00 = (t * ax * ax + cosTheta)*tmp_scale[0];
        this.m01 = (t * xy - sinTheta * az)*tmp_scale[1];
        this.m02 = (t * xz + sinTheta * ay)*tmp_scale[2];

        this.m10 = (t * xy + sinTheta * az)*tmp_scale[0];
        this.m11 = (t * ay * ay + cosTheta)*tmp_scale[1];
        this.m12 = (t * yz - sinTheta * ax)*tmp_scale[2];
 
        this.m20 = (t * xz - sinTheta * ay)*tmp_scale[0];
        this.m21 = (t * yz + sinTheta * ax)*tmp_scale[1];
        this.m22 = (t * az * az + cosTheta)*tmp_scale[2];
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
     * Rotate the object.
	 * <p>
	 * This function is equivalent to (where r is the translation
	 * of the quaternion as a 3x3 matrix):
	 * <pre>
	 * this = this *  [   r    r     r     0   ]
	 *                [   r    r     r     0   ]
	 *                [   r    r     r     0   ]
	 *                [   0    0     0     1   ]
	 * </pre>
     * 
     * @param rotation
     */
    public void rotate(Quaternion rotation) {
    	Transform3D m = new Transform3D();
    	m.makeRotationMatrix(rotation);
    	mul(m);
    }
    
    /**
	 * Sets the value of this matrix to a rotation matrix, and no translation.
	 * <p>
	 * This function changes all the elements of 
	 * the matrix, including the translation. 
	 * <p>
	 * After a call to this function, the matrix will
	 * contains (? means any value, and r a value from
	 * the quaternion):
	 * <pre>
	 *          [   r  r  r  0   ]
	 *          [   r  r  r  0   ]
	 *          [   r  r  r  0   ]
	 *          [   0  0  0  1   ]
	 * </pre>
	 * 
	 * @param rotation
	 * @see #setRotation(Quaternion)
	 */
	public final void makeRotationMatrix(Quaternion rotation) {
		this.m00 = (1.0f - 2.0f*rotation.getY()*rotation.getY() - 2.0f*rotation.getZ()*rotation.getZ());
        this.m10 = (2.0f*(rotation.getX()*rotation.getY() + rotation.getW()*rotation.getZ()));
        this.m20 = (2.0f*(rotation.getX()*rotation.getZ() - rotation.getW()*rotation.getY()));

        this.m01 = (2.0f*(rotation.getX()*rotation.getY() - rotation.getW()*rotation.getZ()));
        this.m11 = (1.0f - 2.0f*rotation.getX()*rotation.getX() - 2.0f*rotation.getZ()*rotation.getZ());
        this.m21 = (2.0f*(rotation.getY()*rotation.getZ() + rotation.getW()*rotation.getX()));

        this.m02 = (2.0f*(rotation.getX()*rotation.getZ() + rotation.getW()*rotation.getY()));
        this.m12 = (2.0f*(rotation.getY()*rotation.getZ() - rotation.getW()*rotation.getX()));
        this.m22 = (1.0f - 2.0f*rotation.getX()*rotation.getX() - 2.0f*rotation.getY()*rotation.getY());

        this.m03 = (float) 0.0;
        this.m13 = (float) 0.0;
        this.m23 = (float) 0.0;

        this.m30 = (float) 0.0;
        this.m31 = (float) 0.0;
        this.m32 = (float) 0.0;
        this.m33 = (float) 1.0;
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
	 *          [   1    0    0    dx   ]
	 *          [   0    1    0    dy   ]
	 *          [   0    0    1    dz   ]
	 *          [   0    0    0    1    ]
	 * </pre>
	 * 
	 * @param dx is the position to put in the matrix.
	 * @param dy is the position to put in the matrix.
	 * @param dz is the position to put in the matrix.
	 * @see #setTranslation(float, float, float)
	 * @see #setTranslation(Tuple3D)
	 */
	public final void makeTranslationMatrix(float dx, float dy, float dz) {
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
		float x, y, z;
		x = this.m00 * t.getX() + this.m01 * t.getY() + this.m02 * t.getZ() + this.m03;
		y = this.m10 * t.getX() + this.m11 * t.getY() + this.m12 * t.getZ() + this.m13;
		z = this.m20 * t.getX() + this.m21 * t.getY() + this.m22 * t.getZ() + this.m23;
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
	public void set(float m00, float m01, float m02, float m03, float m10, float m11, float m12, float m13, float m20, float m21, float m22, float m23) {
		set(m00, m01, m02, m03, m10, m11, m12, m13, m20, m21, m22, m23, 0f, 0f, 0f, 1f);
	}

    /** 
     * Performs an SVD normalization of this matrix in order to acquire 
     * the normalized rotational component; the values are placed into 
     * the Quat4f parameter. 
     * @param q1  quaternion into which the rotation component is placed 
     */
    @Override
	public final void get(Quaternion q1){
    	
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
	 * Helping function that specifies the position and orientation of a view matrix.
	 * 
	 * @param eye is the location of the eye
	 * @param viewDirection is the view vector
	 * @param up is an up vector specifying the frustum's up direction
	 */
	public final void lookAt(Point3f eye, Vector3f viewDirection, Vector3f up) {
		lookAt(viewDirection, up);
		
		// Apply the translation that move the object to the
		// eye's location
		Matrix4f m = new Matrix4f();
		m.setIdentity();
		m.setTranslation(new Vector3f(eye.getX(), eye.getY(), eye.getZ()));
		
		mul(m);
	}

	/**
	 * Helping function that specifies the position and orientation of a view matrix.
	 * 
	 * @param viewDirection is the view vector
	 * @param up is an up vector specifying the frustum's up direction
	 */
	public final void lookAt(Vector3f viewDirection, Vector3f up) {
		// Z = direction
		Vector3f z = new Vector3f(viewDirection);
		z.normalize();

		// X = up cross Z
		Vector3f x = new Vector3f();
		x.cross(up, z);
		x.normalize();
		
		// Y = z cross x
		Vector3f y = new Vector3f();
		y.cross(z, x);

		// Delete all current transformation
		setIdentity();

		// Assuming:
		// z: view direction
		// y: up direction
		// compute the new orientation
		
		setColumn(0, x.getX(), x.getY(), x.getZ(), 0.f);
		setColumn(1, y.getX(), y.getY(), y.getZ(), 0.f);
		setColumn(2, z.getX(), z.getY(), z.getZ(), 0.f);
	}
}
