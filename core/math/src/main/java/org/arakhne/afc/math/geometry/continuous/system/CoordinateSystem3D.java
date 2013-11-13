/* 
 * $Id$
 * 
 * Copyright (C) 2013 Christophe BOHRHAUER.
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
package org.arakhne.afc.math.geometry.continuous.system;

import org.arakhne.afc.math.MathConstants;
import org.arakhne.afc.math.geometry.GeometryUtil;
import org.arakhne.afc.math.geometry.continuous.euclide.EuclidianPoint2D;
import org.arakhne.afc.math.geometry.continuous.euclide.EuclidianPoint3D;
import org.arakhne.afc.math.geometry.continuous.object2d.Direction2D;
import org.arakhne.afc.math.geometry.continuous.object2d.Point2f;
import org.arakhne.afc.math.geometry.continuous.object2d.Vector2f;
import org.arakhne.afc.math.geometry.continuous.object3d.Direction3D;
import org.arakhne.afc.math.geometry.continuous.object3d.Point3f;
import org.arakhne.afc.math.geometry.continuous.object3d.Quaternion;
import org.arakhne.afc.math.geometry.continuous.object3d.Tuple3f;
import org.arakhne.afc.math.geometry.continuous.object3d.Vector3f;
import org.arakhne.afc.math.geometry.continuous.object4d.AxisAngle4f;
import org.arakhne.afc.math.matrix.Matrix3f;
import org.arakhne.afc.math.matrix.Matrix4f;
import org.arakhne.afc.math.transform.Transform2D_OLD;

/**
 * Represents the different kind of 3D referencials
 * and provides the convertion utilities.
 * <p>
 * A referencial axis is expressed by the front, left and top directions.
 * For example <code>XYZ_LEFT_HAND</code> is for the coordinate system
 * with front direction along <code>+/-X</code> axis,
 * left direction along the <code>+/-Y</code> axis
 * and top direction along the <code>+/-Z</code> axis according to
 * a "left-hand" heuristic.
 * <p>
 * The default coordinate system is:
 * <ul>
 * <li>front: <code>(1,0,0)</code></li>
 * <li>left: <code>(0,1,0)</code></li>
 * <li>top: <code>(0,0,1)</code></li>
 * </ul>
 * 
 * <h3>Rotations</h3>
 * 
 * Rotations in a 3D coordinate system follow the right/left hand rules  
 * (assuming that <code>OX</code>, <code>OY</code> and <code>OZ</code> are the three axis of the coordinate system):
 * <table border="1">
 * <tr>
 * <td>Right-handed rule:</td>
 * <td><ul>
 *     <li>axis cycle is: <code>OX</code> &gt; <code>OY</code> &gt; <code>OZ</code> &gt; <code>OX</code> &gt; <code>OY</code>;</li>
 *     <li>when rotating around <code>OX</code>, positive rotation angle is going from <code>OY</code> to <code>OZ</code></li>
 *     <li>when rotating around <code>OY</code>, positive rotation angle is going from <code>OZ</code> to <code>OX</code></li>
 *     <li>when rotating around <code>OZ</code>, positive rotation angle is going from <code>OX</code> to <code>OY</code></li>
 * 	   </ul><br>
 * 	   <a href=""><img border="0" width="200" src="doc-files/rotation_right.png" alt="[Right-handed Rotation Rule]"></a>
 * </td>
 * </tr><tr>
 * <td>Left-handed rule:</td>
 * <td><ul>
 *     <li>axis cycle is: <code>OX</code> &gt; <code>OY</code> &gt; <code>OZ</code> &gt; <code>OX</code> &gt; <code>OY</code>;</li>
 *     <li>when rotating around <code>OX</code>, positive rotation angle is going from <code>OY</code> to <code>OZ</code></li>
 *     <li>when rotating around <code>OY</code>, positive rotation angle is going from <code>OZ</code> to <code>OX</code></li>
 *     <li>when rotating around <code>OZ</code>, positive rotation angle is going from <code>OX</code> to <code>OY</code></li>
 * 	   </ul><br>
 * 	   <a href=""><img border="0" width="200" src="doc-files/rotation_left.png" alt="[Left-handed Rotation Rule]"></a>
 * </td>
 * </tr></table>
 * 
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public enum CoordinateSystem3D implements CoordinateSystem {

	/** Left handed XZY coordinate system.
	 * <p>
	 * <a hef="doc-files/xzy_left.png"><img src="doc-files/xzy_left.png" border="0" width="200" alt="[Left Handed XZY Coordinate System]"></a>
	 */
	XZY_LEFT_HAND(0,1,/* */1,0),

	/** Left handed XYZ coordinate system.
	 * <p>
	 * <a hef="doc-files/xyz_left.png"><img src="doc-files/xyz_left.png" border="0" width="200" alt="[Left Handed XYZ Coordinate System]"></a>
	 */
	XYZ_LEFT_HAND(-1,0,/* */0,1),

	/** Right handed XZY coordinate system.
	 * <p>
	 * <a hef="doc-files/xzy_right.png"><img src="doc-files/xzy_right.png" border="0" width="200" alt="[Right Handed XZY Coordinate System]"></a>
	 */
	XZY_RIGHT_HAND(0,-1,/* */1,0),	

	/** Right handed XYZ coordinate system.
	 * <p>
	 * <a hef="doc-files/xyz_right.png"><img src="doc-files/xyz_right.png" border="0" width="200" alt="[Right Handed XYZ Coordinate System]"></a>
	 */
	XYZ_RIGHT_HAND(1,0,/* */0,1);

	private static final byte PIVOT_SYSTEM = 0;
	
	private static CoordinateSystem3D userDefault;

	private final byte system;
	
	/** {@inheritDoc}
	 */
	@Override
	public final float getDimensions() {
		return 3f;
	}

	private static byte toSystemIndex(int lefty, int leftz, int topy, int topz) {
		if (lefty<0) {
			if (leftz == 0 && topy == 0 && topz!=0) {
				if (topz<0) return 1;
				return 2;
			}
		}
		else if (lefty>0) {
			if (leftz == 0 && topy == 0 && topz!=0) {
				if (topz<0) return 3;
				return 0;
			}
		}
		else {
			if (lefty==0 && leftz!=0) {
				if (leftz<0) {
					if (topz==0 && topy!=0) {
						if (topy<0) return 4;
						return 5;
					}
				}
				else {
					if (topz==0 && topy!=0) {
						if (topy<0) return 6;
						return 7;
					}
				}
			}
		}
		throw new CoordinateSystemNotFoundException();
	}
	
	private static float[] fromSystemIndex(int index) {
		// Compute the lower right sub-matrix
		float c1, c2, c3, c4;
		switch(index) {
		case 1:
			c1 = -1; c2 = 0; c3 = 0; c4 = -1;
			break;
		case 2:
			c1 = -1; c2 = 0; c3 = 0; c4 = 1;
			break;
		case 3:
			c1 = 1; c2 = 0; c3 = 0; c4 = -1;
			break;
		case 4:
			c1 = 0; c2 = -1; c3 = -1; c4 = 0;
			break;
		case 5:
			c1 = 0; c2 = -1; c3 = 1; c4 = 0;
			break;
		case 6:
			c1 = 0; c2 = 1; c3 = -1; c4 = 0;
			break;
		case 7:
			c1 = 0; c2 = 1; c3 = 1; c4 = 0;
			break;
		default: //0
			c1 = 1; c2 = 0; c3 = 0; c4 = 1;
			break;
		}
		
		return new float[] {c1,c2,c3,c4};
	}
	
	/**
	 */
	private CoordinateSystem3D(int lefty, int leftz, int topy, int topz) {
		this.system = toSystemIndex(lefty,leftz,topy,topz);
	}

	/** Convert the specified point into from the current coordinate system
	 * to the specified coordinate system.
	 * 
	 * @param point is the point to convert
	 * @param targetCoordinateSystem is the target coordinate system.
	 */
	public void toSystem(Point3f point, CoordinateSystem3D targetCoordinateSystem) {
		if (targetCoordinateSystem!=this) {
			toPivot(point);
			targetCoordinateSystem.fromPivot(point);
		}
	}

	/** Convert the specified point into from the current coordinate system
	 * to the specified coordinate system.
	 * 
	 * @param point is the point to convert
	 * @param targetCoordinateSystem is the target coordinate system.
	 */
	public void toSystem(Vector3f point, CoordinateSystem3D targetCoordinateSystem) {
		if (targetCoordinateSystem!=this) {
			toPivot(point);
			targetCoordinateSystem.fromPivot(point);
		}
	}

	/** Convert the specified rotation axis into from the current coordinate system
	 * to the specified coordinate system.
	 * 
	 * @param rotation is the rotation to convert
	 * @param targetCoordinateSystem is the target coordinate system.
	 */
	public void toSystem(AxisAngle4f rotation, CoordinateSystem3D targetCoordinateSystem) {
		if (targetCoordinateSystem!=this) {
			toPivot(rotation);
			targetCoordinateSystem.fromPivot(rotation);
		}
	}

	/** Convert the specified rotation axis into from the current coordinate system
	 * to the specified coordinate system.
	 * 
	 * @param rotation is the rotation to convert
	 * @param targetCoordinateSystem is the target coordinate system.
	 */
	public void toSystem(Quaternion rotation, CoordinateSystem3D targetCoordinateSystem) {
		if (targetCoordinateSystem!=this) {
			toPivot(rotation);
			targetCoordinateSystem.fromPivot(rotation);
		}
	}

	/** Convert the specified transformation matrix into from the current coordinate system
	 * to the specified coordinate system.
	 * 
	 * @param matrix is the matrix to convert
	 * @param targetCoordinateSystem is the target coordinate system.
	 */
	public void toSystem(Matrix4f matrix, CoordinateSystem3D targetCoordinateSystem) {
		if (targetCoordinateSystem!=this) {
			toPivot(matrix);
			targetCoordinateSystem.fromPivot(matrix);
		}
	}

	private void toPivot(Point3f point) {
		if (this.system!=PIVOT_SYSTEM) {
			float[] factors = fromSystemIndex(this.system);
			float y = point.getY() * factors[0] + point.getZ() * factors[1];
			float z = point.getY() * factors[2] + point.getZ() * factors[3];
			point.setY(y);
			point.setZ(z);
		}
	}

	/** Convert the specified point into the default coordinate system.
	 * 
	 * @param point is the point to convert
	 */
	public void toDefault(Point3f point) {
		toSystem(point, getDefaultCoordinateSystem());
	}

	private void fromPivot(Point3f point) {
		if (this.system!=PIVOT_SYSTEM) {
			float[] factors = fromSystemIndex(this.system);
			float y = point.getY() * factors[0] + point.getZ() * factors[2];
			float z = point.getY() * factors[1] + point.getZ() * factors[3];
			point.setY(y);
			point.setZ(z);
		}
	}
	
	/** Convert the specified point from the default coordinate system.
	 * 
	 * @param point is the point to convert
	 */
	public void fromDefault(Point3f point) {
		getDefaultCoordinateSystem().toSystem(point, this);
	}

	private void toPivot(Vector3f vector) {
		if (this.system!=PIVOT_SYSTEM) {
			float[] factors = fromSystemIndex(this.system);
			float y = vector.getY() * factors[0] + vector.getZ() * factors[1];
			float z = vector.getY() * factors[2] + vector.getZ() * factors[3];
			vector.setY(y);
			vector.setZ(z);
		}
	}

	/** Convert the specified vector from the default coordinate system.
	 * 
	 * @param vector is the vector to convert
	 */
	public void toDefault(Vector3f vector) {
		toSystem(vector, getDefaultCoordinateSystem());
	}

	private void fromPivot(Vector3f vector) {
		if (this.system!=PIVOT_SYSTEM) {
			float[] factors = fromSystemIndex(this.system);
			float y = vector.getY() * factors[0] + vector.getZ() * factors[2];
			float z = vector.getY() * factors[1] + vector.getZ() * factors[3];
			vector.setY(y);
			vector.setZ(z);
		}
	}

	/** Convert the specified vector from the default coordinate system.
	 * 
	 * @param vector is the vector to convert
	 */
	public void fromDefault(Vector3f vector) {
		getDefaultCoordinateSystem().toSystem(vector, this);
	}

	private void fromPivot(Matrix4f matrix) {
		if (this.system!=PIVOT_SYSTEM) {
			// Translation
			Vector3f tr = new Vector3f();
			matrix.get(tr);
			fromPivot(tr);
			matrix.setTranslation(tr);
			// Rotation
			Quaternion rotation = new Quaternion();
			matrix.get(rotation);
			fromPivot(rotation);
			matrix.setRotation(rotation);
		}
	}

	/** Convert the specified transformation matrix from the default coordinate system.
	 * 
	 * @param matrix is the matrix to convert
	 * @since 4.0
	 */
	public void fromDefault(Matrix4f matrix) {
		getDefaultCoordinateSystem().toSystem(matrix, this);
	}

	private void toPivot(AxisAngle4f rotation) {
		if (this.system!=PIVOT_SYSTEM) {
			float[] factors = fromSystemIndex(this.system);
			float y = rotation.y * factors[0] + rotation.z * factors[1];
			float z = rotation.y * factors[2] + rotation.z * factors[3];
			rotation.y = y;
			rotation.z = z;
			if (isLeftHanded()) rotation.angle = -rotation.angle;
		}
	}

	/** Convert the specified rotation into the default coordinate system.
	 * 
	 * @param rotation is the rotation to convert
	 */
	public void toDefault(AxisAngle4f rotation) {
		toSystem(rotation, getDefaultCoordinateSystem());
	}

	private void fromPivot(AxisAngle4f rotation) {
		if (this.system!=PIVOT_SYSTEM) {
			float[] factors = fromSystemIndex(this.system);
			float y = rotation.y * factors[0] + rotation.z * factors[2];
			float z = rotation.y * factors[1] + rotation.z * factors[3];
			rotation.y = y;
			rotation.z = z;
			if (isLeftHanded()) rotation.angle = -rotation.angle;
		}
	}

	/** Convert the specified rotation from the default coordinate system.
	 * 
	 * @param rotation is the rotation to convert
	 */
	public void fromDefault(AxisAngle4f rotation) {
		getDefaultCoordinateSystem().toSystem(rotation, this);
	}

	private void toPivot(Quaternion rotation) {
		if (this.system!=PIVOT_SYSTEM) {
			AxisAngle4f axisAngle = new AxisAngle4f();
			axisAngle.set(rotation);
			toPivot(axisAngle);
			rotation.set(axisAngle);
		}
	}

	/** Convert the specified rotation into the default coordinate system.
	 * 
	 * @param rotation is the rotation to convert
	 */
	public void toDefault(Quaternion rotation) {
		toSystem(rotation, getDefaultCoordinateSystem());
	}

	private void toPivot(Matrix4f matrix) {
		if (this.system!=PIVOT_SYSTEM) {
			// Translation
			Vector3f tr = new Vector3f();
			matrix.get(tr);
			toPivot(tr);
			matrix.setTranslation(tr);
			// Rotation
			Quaternion q = new Quaternion();
			matrix.get(q);
			toPivot(q);
			matrix.setRotation(q);
		}
	}
	
	/** Convert the specified matrix into the default coordinate system.
	 * 
	 * @param matrix is the matrix to convert
	 * @since 4.0
	 */
	public void toDefault(Matrix4f matrix) {
		toSystem(matrix, getDefaultCoordinateSystem());
	}
	
	private void fromPivot(Quaternion rotation) {
		if (this.system!=PIVOT_SYSTEM) {
			AxisAngle4f axisAngle = new AxisAngle4f();
			axisAngle.set(rotation);
			fromPivot(axisAngle);
			rotation.set(axisAngle);
		}
	}

	/** Convert the specified rotation from the default coordinate system.
	 * 
	 * @param rotation is the rotation to convert
	 */
	public void fromDefault(Quaternion rotation) {
		getDefaultCoordinateSystem().toSystem(rotation, this);
	}

	/** Replies the preferred coordinate system.
	 * 
	 * @return the preferred coordinate system.
	 */
	public static CoordinateSystem3D getDefaultCoordinateSystem() {
		if (userDefault!=null) return userDefault;
		return CoordinateSystemConstants.SIMULATION_3D;
	}

	/** Set the preferred coordinate system.
	 * 
	 * @param newDefault is the new default coordinate system. If <code>null</code> the default
	 * coordinate system will be set back to the value replied by
	 * {@link CoordinateSystemConstants#SIMULATION_3D}.
	 */
	public static void setDefaultCoordinateSystem(CoordinateSystem3D newDefault) {
		userDefault = newDefault;
	}

	/** Replies the coordinate system which is corresponding to the specified
	 * orientation unit vectors.
	 * <p>
	 * The front vector is assumed to be always <code>(1,0,0)</code>,
	 * the left vector is <code>(0,ly,lz)</code>, and the top
	 * vector is <code>(0,ty,tz)</code>.
	 * 
	 * @param ly
	 * @param lz
	 * @param ty
	 * @param tz
	 * @return the coordinate system which is defined by the specified vectors.
	 */
	public static CoordinateSystem3D fromVectors(int ly, int lz, int ty, int tz) {
		byte system = toSystemIndex(ly, lz, ty, tz);
		for(CoordinateSystem3D cs : CoordinateSystem3D.values()) {
			if (cs.system==system) return cs;
		}
		throw new CoordinateSystemNotFoundException();
	}

	/** Replies the coordinate system which is corresponding to the specified
	 * orientation unit vectors.
	 * <p>
	 * The front vector is assumed to be always <code>(1,0,0)</code>,
	 * the left vector is <code>(0,ly,lz)</code>, and the top
	 * vector is <code>(0,ty,tz)</code>.
	 *
	 * @param ly
	 * @param lz
	 * @param ty
	 * @param tz
	 * @return the coordinate system which is defined by the specified vectors.
	 */
	public static CoordinateSystem3D fromVectors(float ly, float lz, float ty, float tz) {
		return fromVectors((int)ly,(int)lz,(int)ty,(int)tz);
	}
	
	/** Replies the coordinate system which is corresponding to the specified
	 * orientation unit vectors.
	 *
	 * @param vx
	 * @param vy
	 * @param vz
	 * @param lx
	 * @param ly
	 * @param lz
	 * @param ux
	 * @param uy
	 * @param uz
	 * @return the coordinate system which is defined by the specified vectors.
	 */
	public static CoordinateSystem3D fromVectors(float vx, float vy, float vz, float lx, float ly, float lz, float ux, float uy, float uz) {
		if (vx==1. && vy==0. && vz==0.) {
			assert(lx==0. && ux==0.);
			return fromVectors(ly, lz, uy, uz);
		}

		// Transform the given vectors to align V along (1,0,0)
		Matrix3f mat = new Matrix3f();
		mat.m00 = vx;
		mat.m10 = lx;
		mat.m20 = ux;
		mat.m01 = vy;
		mat.m11 = ly;
		mat.m21 = uy;
		mat.m02 = vz;
		mat.m12 = lz;
		mat.m22 = uz;

		Vector3f v1 = new Vector3f(vx, vy, vz);
		mat.transform(v1);
		normalizeVector(v1);
		
		Vector3f v2 = new Vector3f(lx, ly, lz);
		mat.transform(v2);
		normalizeVector(v2);

		Vector3f v3 = new Vector3f(ux, uy, uz);
		mat.transform(v3);
		normalizeVector(v3);

		assert(v1.getX()==1. && v1.getY()==0. && v1.getZ()==0.);
		assert(v2.getX()==0. && v3.getX()==0.);
		return fromVectors(v2.getY(), v2.getZ(), v3.getY(), v3.getZ());
	}
	
	private static void normalizeVector(Vector3f v) {
		v.normalize();
		if (Math.abs(v.getX()-1.) <= MathConstants.EPSILON) {
			v.setX(Math.signum(v.getX()));
			v.setY(0.f);
			v.setZ(0.f);
		}
		else if (Math.abs(v.getY()-1.) <= MathConstants.EPSILON) {
			v.setY(Math.signum(v.getY()));
			v.setX(0.f);
			v.setZ(0.f);
		}
		else if (Math.abs(v.getZ()-1.) <= MathConstants.EPSILON) {
			v.setZ(Math.signum(v.getZ()));
			v.setX(0.f);
			v.setY(0.f);
		}
	}

	/** Replies the coordinate system which is corresponding to the specified
	 * orientation unit vectors.
	 * 
	 * @param vx
	 * @param vy
	 * @param vz
	 * @param lx
	 * @param ly
	 * @param lz
	 * @param ux
	 * @param uy
	 * @param uz
	 * @return the coordinate system which is defined by the specified vectors.
	 */
	public static CoordinateSystem3D fromVectors(int vx, int vy, int vz, int lx, int ly, int lz, int ux, int uy, int uz) {
		if (vx==1 && vy==0 && vz==0) {
			assert(lx==0 && ux==0);
			return fromVectors(ly, lz, uy, uz);
		}

		// Transform the given vectors to align V along (1,0,0)
		Matrix3f mat = new Matrix3f();
		mat.m00 = vx;
		mat.m10 = lx;
		mat.m20 = ux;
		mat.m01 = vy;
		mat.m11 = ly;
		mat.m21 = uy;
		mat.m02 = vz;
		mat.m12 = lz;
		mat.m22 = uz;

		Vector3f v1 = new Vector3f(vx, vy, vz);
		mat.transform(v1);
		normalizeVector(v1);
		
		Vector3f v2 = new Vector3f(lx, ly, lz);
		mat.transform(v2);
		normalizeVector(v2);

		Vector3f v3 = new Vector3f(ux, uy, uz);
		mat.transform(v3);
		normalizeVector(v3);

		assert(v1.getX()==1. && v1.getY()==0. && v1.getZ()==0.);
		assert(v2.getX()==0. && v3.getX()==0.);
		return fromVectors((int)v2.getY(), (int)v2.getZ(), (int)v3.getY(), (int)v3.getZ());
	}

	/** Replies the vertical position from the given 3D point for this coordinate system.
	 * 
	 * @param tuple
	 * @return the vertical position in <var>x/<var>, <var>y</var> or </var>z</var>
	 */
	public final float height(Tuple3f<?> tuple) {
		return height(tuple.getX(), tuple.getY(), tuple.getZ());
	}

	/** Replies the vertical position from the given 3D point for this coordinate system.
	 * 
	 * @param x
	 * @param y
	 * @param z
	 * @return the vertical position in <var>x/<var>, <var>y</var> or </var>z</var>
	 */
	public float height(float x, float y, float z) {
		float[] factors = fromSystemIndex(this.system);
		return (factors[2]!=0.) ? y : z;
	}
	
	/** Replies the horizontal left-right position from the given 3D point for this coordinate system.
	 * 
	 * @param tuple
	 * @return the horizontal and left-right position in <var>x/<var>, <var>y</var> or </var>z</var>
	 * @since 4.0
	 */
	public final float side(Tuple3f<?> tuple) {
		return side(tuple.getX(), tuple.getY(), tuple.getZ());
	}

	/** Replies the horizontal left-right position from the given 3D point for this coordinate system.
	 * 
	 * @param x
	 * @param y
	 * @param z
	 * @return the horizontal and left-right position in <var>x/<var>, <var>y</var> or </var>z</var>
	 * @since 4.0
	 */
	public float side(float x, float y, float z) {
		float[] factors = fromSystemIndex(this.system);
		return (factors[2]!=0.) ? z : y;
	}
	
	/** Replies the horizontal front-back position from the given 3D point for this coordinate system.
	 * 
	 * @param tuple
	 * @return the horizontal and front-back position in <var>x/<var>, <var>y</var> or </var>z</var>
	 * @since 4.0
	 */
	public final static float view(Tuple3f<?> tuple) {
		return view(tuple.getX(), tuple.getY(), tuple.getZ());
	}

	/** Replies the horizontal front-back position from the given 3D point for this coordinate system.
	 * 
	 * @param x
	 * @param y
	 * @param z
	 * @return the horizontal and front-back position in <var>x/<var>, <var>y</var> or </var>z</var>
	 * @since 4.0
	 */
	public static float view(float x, float y, float z) {
		return x;
	}
	
	/** Replies index of the coordinate which is corresponding to
	 * the height for the coordinate system.
	 * 
	 * @return the index of the coordinate of the height.
	 */
	public int getHeightCoordinateIndex() {
		float[] factors = fromSystemIndex(this.system);
		return (factors[2]!=0.) ? 1 : 2;
	}

	/** Replies index of the coordinate which is corresponding to
	 * the side for the coordinate system.
	 * 
	 * @return the index of the coordinate of the side.
	 * @since 4.0
	 */
	public int getSideCoordinateIndex() {
		float[] factors = fromSystemIndex(this.system);
		return (factors[2]!=0.) ? 2 : 1;
	}

	/** Replies index of the coordinate which is corresponding to
	 * the view for the coordinate system.
	 * 
	 * @return the index of the coordinate of the view.
	 * @since 4.0
	 */
	public static int getViewCoordinateIndex() {
		return 0;
	}

	/** Set the vertical position in the given 3D point for this coordinate system.
	 * 
	 * @param tuple
	 * @param height is the height to put in the tuple.
	 */
	public final void setHeight(Tuple3f<?> tuple, float height) {
		float[] factors = fromSystemIndex(this.system);
		if (factors[2]!=0.) tuple.setY(height);
		else tuple.setZ(height);
	}

	/** Add the vertical amount to the height field of the given 3D point
	 * for this coordinate system.
	 * 
	 * @param tuple
	 * @param additionalHeight is the height amount to add to the tuple.
	 */
	public final void addHeight(Tuple3f<?> tuple, float additionalHeight) {
		float[] factors = fromSystemIndex(this.system);
		if (factors[2]!=0.) tuple.setY(tuple.getY() + additionalHeight);
		else tuple.setZ(tuple.getZ() + additionalHeight);
	}

	/** Set the left-right position in the given 3D point for this coordinate system.
	 * 
	 * @param tuple
	 * @param side is the side amount to put in the tuple.
	 * @since 4.0
	 */
	public final void setSide(Tuple3f<?> tuple, float side) {
		float[] factors = fromSystemIndex(this.system);
		if (factors[2]!=0.) tuple.setZ(side);
		else tuple.setY(side);
	}

	/** Add the left-right amount to the field of the given 3D point
	 * for this coordinate system.
	 * 
	 * @param tuple
	 * @param additionalAmount is the amount to add to the tuple.
	 * @since 4.0
	 */
	public final void addSide(Tuple3f<?> tuple, float additionalAmount) {
		float[] factors = fromSystemIndex(this.system);
		if (factors[2]!=0.) tuple.setZ(tuple.getZ() + additionalAmount);
		else tuple.setY(tuple.getY() + additionalAmount);
	}

	/** Set the front-back position in the given 3D point for this coordinate system.
	 * 
	 * @param tuple
	 * @param amount is the amount to put in the tuple.
	 * @since 4.0
	 */
	public final static void setView(Tuple3f<?> tuple, float amount) {
		tuple.setX(amount);
	}

	/** Add the front-back amount to the field of the given 3D point
	 * for this coordinate system.
	 * 
	 * @param tuple
	 * @param additionalViewAmount is the amount to add to the tuple.
	 */
	public final static void addView(Tuple3f<?> tuple, float additionalViewAmount) {
		tuple.setX(tuple.getX() + additionalViewAmount);
	}

	/** Replies the 2D coordinate system which is corresponding to
	 * this 3D coordinate system.
	 * <p>
	 * Be carreful because the <code>y</code> semantic could differ from
	 * the 3D primitive to the 2D primitive.
	 * 
	 * @return the 2D coordinate system.
	 */
	public CoordinateSystem2D toCoordinateSystem2D() {
		switch(this) {
		case XYZ_LEFT_HAND:
			return CoordinateSystem2D.XY_LEFT_HAND;
		case XYZ_RIGHT_HAND:
			return CoordinateSystem2D.XY_RIGHT_HAND;
		case XZY_LEFT_HAND:
			return CoordinateSystem2D.XY_RIGHT_HAND;
		case XZY_RIGHT_HAND:
			return CoordinateSystem2D.XY_LEFT_HAND;
		default:
			throw new CoordinateSystemNotFoundException();
		}
	}
	
	/** Convert the specified point into from the current coordinate system
	 * to the specified coordinate system.
	 * 
	 * @param point is the point to convert
	 * @return the 2D point
	 */
	public Point2f toCoordinateSystem2D(Point3f point) {
		switch(this) {
		case XYZ_RIGHT_HAND:
		case XYZ_LEFT_HAND:
			return new Point2f(point.getX(), point.getY());
		case XZY_LEFT_HAND:
		case XZY_RIGHT_HAND:
			return new Point2f(point.getX(), point.getZ());
		default:
			throw new CoordinateSystemNotFoundException();
		}
	}

	/** Convert the specified point into from the current coordinate system
	 * to the specified coordinate system.
	 * 
	 * @param point is the point to convert
	 * @return the 2D point
	 */
	public EuclidianPoint2D toCoordinateSystem2D(EuclidianPoint3D point) {
		switch(this) {
		case XYZ_RIGHT_HAND:
		case XYZ_LEFT_HAND:
			return new EuclidianPoint2D(point.getX(), point.getY());
		case XZY_LEFT_HAND:
		case XZY_RIGHT_HAND:
			return new EuclidianPoint2D(point.getX(), point.getZ());
		default:
			throw new CoordinateSystemNotFoundException();
		}
	}

	/** Convert the specified point into from the current coordinate system
	 * to the specified coordinate system.
	 * 
	 * @param vector is the vector to convert
	 * @return the 2D vector
	 */
	public Vector2f toCoordinateSystem2D(Vector3f vector) {
		switch(this) {
		case XYZ_RIGHT_HAND:
		case XYZ_LEFT_HAND:
			return new Vector2f(vector.getX(), vector.getY());
		case XZY_LEFT_HAND:
		case XZY_RIGHT_HAND:
			return new Vector2f(vector.getX(), vector.getZ());
		default:
			throw new CoordinateSystemNotFoundException();
		}
	}

	/** Convert the specified point into from the current coordinate system
	 * to the specified coordinate system.
	 * 
	 * @param vector is the vector to convert
	 * @return the 2D vector
	 */
	public Direction2D toCoordinateSystem2D(Direction3D vector) {
		switch(this) {
		case XYZ_RIGHT_HAND:
		case XYZ_LEFT_HAND:
			return new Direction2D(vector.x, vector.y);
		case XZY_LEFT_HAND:
		case XZY_RIGHT_HAND:
			return new Direction2D(vector.x, vector.z);
		default:
			throw new CoordinateSystemNotFoundException();
		}
	}

	/** Convert the specified rotation axis into from the current coordinate system
	 * to the specified coordinate system.
	 * 
	 * @param rotation is the rotation to convert
	 * @return the 2D rotation
	 */
	public float toCoordinateSystem2D(AxisAngle4f rotation) {
		Matrix4f mat = new Matrix4f();
		mat.set(rotation);
		return toCoordinateSystem2DAngleFromTransformation(mat);
	}

	/** Convert the specified rotation axis into from the current coordinate system
	 * to the specified coordinate system.
	 * 
	 * @param rotation is the rotation to convert
	 * @return the 2D rotation
	 */
	public float toCoordinateSystem2D(Quaternion rotation) {
		Matrix4f mat = new Matrix4f();
		mat.set(rotation);
		return toCoordinateSystem2DAngleFromTransformation(mat);
	}

	private float toCoordinateSystem2DAngleFromTransformation(Matrix4f mat) {
		Vector3f ptR = new Vector3f(1,0,0);
		mat.transform(ptR);
		switch(this) {
		case XYZ_LEFT_HAND:
		case XYZ_RIGHT_HAND:
			return GeometryUtil.signedAngle(1, 0, ptR.getX(), ptR.getY());
		case XZY_LEFT_HAND:
		case XZY_RIGHT_HAND:
			return GeometryUtil.signedAngle(ptR.getX(), ptR.getZ(), 1, 0);
		default:
			throw new CoordinateSystemNotFoundException();
		}
	}
	
	/** Convert the specified transformation matrix into from the current coordinate system
	 * to the specified coordinate system.
	 * 
	 * @param matrix is the matrix to convert
	 * @return the 2D transformation matrix
	 * @since 4.0
	 */
	public Transform2D_OLD toCoordinateSystem2D(Matrix4f matrix) {
		// Translation
		Vector3f tr3d = new Vector3f();
		matrix.get(tr3d);
		Vector2f tr2d = toCoordinateSystem2D(tr3d);
		// Rotation
		Quaternion r3d = new Quaternion();
		matrix.get(r3d);
		float r2d = toCoordinateSystem2D(r3d);
		
		Transform2D_OLD m = new Transform2D_OLD();
		m.setTranslation(tr2d);
		m.setRotation(r2d);
		return m;
	}

	/** Convert the specified point from the 2D coordinate system
	 * to this specified coordinate system.
	 * <p>
	 * Thr third coordinate is set to <code>0</code>.
	 * 
	 * @param point is the point to convert
	 * @return the 3D point
	 */
	public final Point3f fromCoordinateSystem2D(Point2f point) {
		return fromCoordinateSystem2D(point, 0);
	}

	/** Convert the specified point from the 2D coordinate system
	 * to this specified coordinate system.
	 * <p>
	 * Thr third coordinate is set to <code>0</code>.
	 * 
	 * @param point is the point to convert
	 * @return the 3D point
	 */
	public final EuclidianPoint3D fromCoordinateSystem2D(EuclidianPoint2D point) {
		return fromCoordinateSystem2D(point, 0);
	}

	/** Convert the specified point from the 2D coordinate system
	 * to this specified coordinate system.
	 * 
	 * @param point is the point to convert
	 * @param thirdCoordinate is the value of the third coordinate to put in the replied point.
	 * @return the 3D point
	 */
	public Point3f fromCoordinateSystem2D(Point2f point, float thirdCoordinate) {
		switch(this) {
		case XYZ_LEFT_HAND:
		case XYZ_RIGHT_HAND:
			return new Point3f(point.getX(), point.getY(), thirdCoordinate);
		case XZY_LEFT_HAND:
		case XZY_RIGHT_HAND:
			return new Point3f(point.getX(), thirdCoordinate, point.getY());
		default:
			throw new CoordinateSystemNotFoundException();
		}
	}

	/** Convert the specified point from the 2D coordinate system
	 * to this specified coordinate system.
	 * 
	 * @param point is the point to convert
	 * @param thirdCoordinate is the value of the third coordinate to put in the replied point.
	 * @return the 3D point
	 */
	public EuclidianPoint3D fromCoordinateSystem2D(EuclidianPoint2D point, float thirdCoordinate) {
		switch(this) {
		case XYZ_LEFT_HAND:
		case XYZ_RIGHT_HAND:
			return new EuclidianPoint3D(point.getX(), point.getY(), thirdCoordinate);
		case XZY_LEFT_HAND:
		case XZY_RIGHT_HAND:
			return new EuclidianPoint3D(point.getX(), thirdCoordinate, point.getY());
		default:
			throw new CoordinateSystemNotFoundException();
		}
	}

	/** Convert the specified vector from the 2D coordinate system
	 * to this specified coordinate system.
	 * <p>
	 * Thr third coordinate is set to <code>0</code>.
	 * 
	 * @param vector is the vector to convert
	 * @return the 3D vector
	 */
	public final Vector3f fromCoordinateSystem2D(Vector2f vector) {
		return fromCoordinateSystem2D(vector, 0);
	}

	/** Convert the specified vector from the 2D coordinate system
	 * to this specified coordinate system.
	 * <p>
	 * Thr third coordinate is set to <code>0</code>.
	 * 
	 * @param vector is the vector to convert
	 * @return the 3D vector
	 */
	public final Direction3D fromCoordinateSystem2D(Direction2D vector) {
		return fromCoordinateSystem2D(vector, 0);
	}

	/** Convert the specified vector from the 2D coordinate system
	 * to this specified coordinate system.
	 * 
	 * @param point is the vector to convert
	 * @param thirdCoordinate is the value of the third coordinate to put in the replied vector.
	 * @return the 3D vector
	 */
	public Vector3f fromCoordinateSystem2D(Vector2f point, float thirdCoordinate) {
		switch(this) {
		case XYZ_LEFT_HAND:
		case XYZ_RIGHT_HAND:
			return new Vector3f(point.getX(), point.getY(), thirdCoordinate);
		case XZY_LEFT_HAND:
		case XZY_RIGHT_HAND:
			return new Vector3f(point.getX(), thirdCoordinate, point.getY());
		default:
			throw new CoordinateSystemNotFoundException();
		}
	}

	/** Convert the specified vector from the 2D coordinate system
	 * to this specified coordinate system.
	 * 
	 * @param point is the vector to convert
	 * @param thirdCoordinate is the value of the third coordinate to put in the replied vector.
	 * @return the 3D vector
	 */
	public Direction3D fromCoordinateSystem2D(Direction2D point, float thirdCoordinate) {
		switch(this) {
		case XYZ_LEFT_HAND:
		case XYZ_RIGHT_HAND:
			return new Direction3D(point.getX(), point.getY(), thirdCoordinate, 0.f);
		case XZY_LEFT_HAND:
		case XZY_RIGHT_HAND:
			return new Direction3D(point.getX(), thirdCoordinate, point.getY(), 0.f);
		default:
			throw new CoordinateSystemNotFoundException();
		}
	}

	/** Convert the specified rotation from the 2D coordinate system
	 * to this specified coordinate system.
	 * 
	 * @param rotation is the angle of rotation to convert.
	 * @return the 3D axis angle
	 */
	public AxisAngle4f fromCoordinateSystem2D(float rotation) {
		switch(this) {
		case XYZ_LEFT_HAND:
		case XYZ_RIGHT_HAND:
			return new AxisAngle4f(0,0,1,rotation);
		case XZY_LEFT_HAND:
		case XZY_RIGHT_HAND:
			return new AxisAngle4f(0,1,0,rotation);
		default:
			throw new CoordinateSystemNotFoundException();
		}
	}
	
	/** Replies the view vector of this coordinate space.
	 * <p>
	 * When objects have not been rotated, they are supposed to
	 * have a front direction colinear to this view vector.
	 * 
	 * @return the view vector (always normalized).
	 */
	public final Vector3f getViewVector() {
		return getViewVector(new Vector3f());
	}

	/** Replies the view vector of this coordinate space.
	 * <p>
	 * When objects have not been rotated, they are supposed to
	 * have a front direction colinear to this view vector.
	 * 
	 * @param vectorToFill is the vector to fill with the view vector coordinates.
	 * @return <var>vectorToFill</var>.
	 */
	public final Vector3f getViewVector(Vector3f vectorToFill) {
		if (vectorToFill!=null) {
			vectorToFill.set(1.f,0.f,0.f);
			fromPivot(vectorToFill);
		}
		return vectorToFill;
	}

	/** Replies the back vector of this coordinate space.
	 * <p>
	 * When objects have not been rotated, they are supposed to
	 * have a back direction colinear to this back vector.
	 * 
	 * @return the back vector (always normalized).
	 */
	public final Vector3f getBackVector() {
		return getBackVector(new Vector3f());
	}

	/** Replies the back vector of this coordinate space.
	 * <p>
	 * When objects have not been rotated, they are supposed to
	 * have a back direction colinear to this back vector.
	 * 
	 * @param vectorToFill is the vector to fill with the back vector coordinates.
	 * @return <var>vectorToFill</var>.
	 */
	public final Vector3f getBackVector(Vector3f vectorToFill) {
		if (vectorToFill!=null) {
			vectorToFill.set(-1.f,0.f,0.f);
			fromPivot(vectorToFill);
		}
		return vectorToFill;
	}

	/** Replies the left vector of this coordinate space.
	 * <p>
	 * When objects have not been rotated, they are supposed to
	 * have a left direction colinear to this left vector.
	 * 
	 * @return the left vector (always normalized).
	 */
	public final Vector3f getLeftVector() {
		return getLeftVector(new Vector3f());
	}

	/** Replies the left vector of this coordinate space.
	 * <p>
	 * When objects have not been rotated, they are supposed to
	 * have a left direction colinear to this left vector.
	 * 
	 * @param vectorToFill is the vector to fill with the left vector coordinates.
	 * @return <var>vectorToFill</var>.
	 */
	public final Vector3f getLeftVector(Vector3f vectorToFill) {
		if (vectorToFill!=null) {
			vectorToFill.set(0.f,1.f,0.f);
			fromPivot(vectorToFill);
		}
		return vectorToFill;
	}

	/** Replies the right vector of this coordinate space.
	 * <p>
	 * When objects have not been rotated, they are supposed to
	 * have a right direction colinear to this right vector.
	 * 
	 * @return the right vector (always normalized).
	 */
	public final Vector3f getRightVector() {
		return getRightVector(new Vector3f());
	}

	/** Replies the right vector of this coordinate space.
	 * <p>
	 * When objects have not been rotated, they are supposed to
	 * have a right direction colinear to this right vector.
	 * 
	 * @param vectorToFill is the vector to fill with the right vector coordinates.
	 * @return <var>vectorToFill</var>.
	 */
	public final Vector3f getRightVector(Vector3f vectorToFill) {
		if (vectorToFill!=null) {
			vectorToFill.set(0.f,-1.f,0.f);
			fromPivot(vectorToFill);
		}
		return vectorToFill;
	}

	/** Replies the up vector of this coordinate space.
	 * <p>
	 * When objects have not been rotated, they are supposed to
	 * have a up direction colinear to this up vector.
	 * 
	 * @return the up vector (always normalized).
	 */
	public final Vector3f getUpVector() {
		return getUpVector(new Vector3f());
	}

	/** Replies the up vector of this coordinate space.
	 * <p>
	 * When objects have not been rotated, they are supposed to
	 * have a up direction colinear to this up vector.
	 * 
	 * @param vectorToFill is the vector to fill with the up vector coordinates.
	 * @return <var>vectorToFill</var>.
	 */
	public final Vector3f getUpVector(Vector3f vectorToFill) {
		if (vectorToFill!=null) {
			vectorToFill.set(0.f,0.f,1.f);
			fromPivot(vectorToFill);
		}
		return vectorToFill;
	}

	/** Replies the down vector of this coordinate space.
	 * <p>
	 * When objects have not been rotated, they are supposed to
	 * have a down direction colinear to this down vector.
	 * 
	 * @return the down vector (always normalized).
	 */
	public final Vector3f getDownVector() {
		return getDownVector(new Vector3f());
	}

	/** Replies the down vector of this coordinate space.
	 * <p>
	 * When objects have not been rotated, they are supposed to
	 * have a down direction colinear to this down vector.
	 * 
	 * @param vectorToFill is the vector to fill with the down vector coordinates.
	 * @return <var>vectorToFill</var>.
	 */
	public final Vector3f getDownVector(Vector3f vectorToFill) {
		if (vectorToFill!=null) {
			vectorToFill.set(0.f,0.f,-1.f);
			fromPivot(vectorToFill);
		}
		return vectorToFill;
	}
	
	/** {@inheritDoc}
	 */
	@Override
	public boolean isRightHanded() {
		return this==XYZ_RIGHT_HAND || this==XZY_RIGHT_HAND;
	}

	/** {@inheritDoc}
	 */
	@Override
	public boolean isLeftHanded() {
		return this==XYZ_LEFT_HAND || this==XZY_LEFT_HAND;
	}
	
	/** Replies if the z coordinate is the up direction.
	 * 
	 * @return <code>true</code> if z coordiante is up.
	 */
	public boolean isZOnUp() {
		return this==XYZ_LEFT_HAND || this==XYZ_RIGHT_HAND;
	}

	/** Replies if the y coordinate is the up direction.
	 * 
	 * @return <code>true</code> if y coordiante is up.
	 */
	public boolean isYOnUp() {
		return this==XZY_LEFT_HAND || this==XZY_RIGHT_HAND;
	}

	/** Replies if the z coordinate is the side direction (left or right).
	 * 
	 * @return <code>true</code> if z coordiante is side.
	 */
	public boolean isZOnSide() {
		return this==XZY_LEFT_HAND || this==XZY_RIGHT_HAND;
	}

	/** Replies if the y coordinate is the side direction (left or right).
	 * 
	 * @return <code>true</code> if y coordiante is side.
	 */
	public boolean isYOnSide() {
		return this==XYZ_LEFT_HAND || this==XYZ_RIGHT_HAND;
	}

}
