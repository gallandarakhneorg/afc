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
package org.arakhne.afc.math.geometry.coordinatesystem;

import org.arakhne.afc.math.MathUtil;
import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.Transform2D;
import org.arakhne.afc.math.geometry.d2.Vector2D;
import org.arakhne.afc.math.geometry.d2.fpfx.Point2fx;
import org.arakhne.afc.math.geometry.d2.fpfx.Vector2fx;
import org.arakhne.afc.math.geometry.d3.Point3D;
import org.arakhne.afc.math.geometry.d3.Vector3D;
import org.eclipse.xtext.xbase.lib.Pure;

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
 * @since 13.0
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
	@Pure
	@Override
	public final int getDimensions() {
		return 3;
	}

	@Pure
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

	@Pure
	private static double[] fromSystemIndex(int index) {
		// Compute the lower right sub-matrix
		double c1, c2, c3, c4;
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

		return new double[] {c1,c2,c3,c4};
	}

	/**
	 */
	@Pure
	private CoordinateSystem3D(int lefty, int leftz, int topy, int topz) {
		this.system = toSystemIndex(lefty,leftz,topy,topz);
	}

	/** Convert the specified point into from the current coordinate system
	 * to the specified coordinate system.
	 * 
	 * @param point is the point to convert
	 * @param targetCoordinateSystem is the target coordinate system.
	 */
	public void toSystem(Point3D point, CoordinateSystem3D targetCoordinateSystem) {
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
	public void toSystem(Vector3D point, CoordinateSystem3D targetCoordinateSystem) {
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
	public void toSystem(Quaternion rotation, CoordinateSystem3D targetCoordinateSystem) {
		if (targetCoordinateSystem!=this) {
			toPivot(rotation);
			targetCoordinateSystem.fromPivot(rotation);
		}
	}

	/** Convert the specified transformation matrix into from the current coordinate system
	 * to the specified coordinate system.
	 * 
	 * @param trans is the matrix to convert
	 * @param targetCoordinateSystem is the target coordinate system.
	 */
	public void toSystem(Transform3D trans, CoordinateSystem3D targetCoordinateSystem) {
		if (targetCoordinateSystem!=this) {
			toPivot(trans);
			targetCoordinateSystem.fromPivot(trans);
		}
	}

	private void toPivot(Point3f point) {
		if (this.system!=PIVOT_SYSTEM) {
			double[] factors = fromSystemIndex(this.system);
			double y = point.getY() * factors[0] + point.getZ() * factors[1];
			double z = point.getY() * factors[2] + point.getZ() * factors[3];
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
			double[] factors = fromSystemIndex(this.system);
			double y = point.getY() * factors[0] + point.getZ() * factors[2];
			double z = point.getY() * factors[1] + point.getZ() * factors[3];
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

	private void toPivot(Vector3D vector) {
		if (this.system!=PIVOT_SYSTEM) {
			double[] factors = fromSystemIndex(this.system);
			double y = vector.getY() * factors[0] + vector.getZ() * factors[1];
			double z = vector.getY() * factors[2] + vector.getZ() * factors[3];
			vector.setY(y);
			vector.setZ(z);
		}
	}

	/** Convert the specified vector from the default coordinate system.
	 * 
	 * @param vector is the vector to convert
	 */
	public void toDefault(Vector3D vector) {
		toSystem(vector, getDefaultCoordinateSystem());
	}

	private void fromPivot(Vector3D vector) {
		if (this.system!=PIVOT_SYSTEM) {
			double[] factors = fromSystemIndex(this.system);
			double y = vector.getY() * factors[0] + vector.getZ() * factors[2];
			double z = vector.getY() * factors[1] + vector.getZ() * factors[3];
			vector.setY(y);
			vector.setZ(z);
		}
	}

	/** Convert the specified vector from the default coordinate system.
	 * 
	 * @param vector is the vector to convert
	 */
	public void fromDefault(Vector3D vector) {
		getDefaultCoordinateSystem().toSystem(vector, this);
	}

	private void fromPivot(Transform3D transformation) {
		if (this.system!=PIVOT_SYSTEM) {
			// Translation
			Vector3D tr = transformation.getTranslation();
			fromPivot(tr);
			transformation.setTranslation(tr);
			// Rotation
			Quaternion rotation = transformation.getRotation();
			fromPivot(rotation);
			transformation.setRotation(rotation);
		}
	}

	private void toPivot(Transform3D trans) {
		if (this.system!=PIVOT_SYSTEM) {
			double[] factors = fromSystemIndex(this.system);
			double ty = trans.getM13() * factors[0] + trans.getM23()* factors[1];
			double tz = trans.getM13() * factors[2] + trans.getM23()* factors[3];
			trans.setTranslation(trans.getTranslationX(), ty, tz);
			Quaternion r = trans.getRotation();
			Vector3D vector = r.getAxis();
			double ry = vector.getY() * factors[0] + vector.getZ() * factors[1];
			double rz = vector.getY() * factors[2] + vector.getZ() * factors[3];

			double angle = r.getAngle();

			if (isLeftHanded()) angle = -angle;

			r.setAxisAngle(vector.getX(), ry, rz, angle);
			trans.setRotation(r);
		}

	}

	/** Convert the specified rotation into the default coordinate system.
	 * 
	 * @param rotation is the rotation to convert
	 */
	public void toDefault(Transform3D rotation) {
		toSystem(rotation, getDefaultCoordinateSystem());
	}

	/** Convert the specified rotation from the default coordinate system.
	 * 
	 * @param rotation is the rotation to convert
	 */
	public void fromDefault(Transform3D rotation) {
		getDefaultCoordinateSystem().toSystem(rotation, this);
	}

	private void toPivot(Quaternion quaternion) {
		if (this.system!=PIVOT_SYSTEM) {
			double[] factors = fromSystemIndex(this.system);
			Vector3D vector = quaternion.getAxis();
			double y = vector.getY() * factors[0] + vector.getZ() * factors[1];
			double z = vector.getY() * factors[2] + vector.getZ() * factors[3];
			vector.setY(y);
			vector.setZ(z);

			double angle = quaternion.getAngle();

			if (isLeftHanded()) angle = -angle;

			quaternion.setAxisAngle(vector, angle);
		}
	}

	/** Convert the specified rotation into the default coordinate system.
	 * 
	 * @param rotation is the rotation to convert
	 */
	public void toDefault(Quaternion rotation) {
		toSystem(rotation, getDefaultCoordinateSystem());
	}

	private void fromPivot(Quaternion rotation) {
		if (this.system!=PIVOT_SYSTEM) {
			Vector3D vector = rotation.getAxis();
			double[] factors = fromSystemIndex(this.system);
			double y = vector.getY() * factors[0] + vector.getZ() * factors[2];
			double z = vector.getY() * factors[1] + vector.getZ() * factors[3];
			vector.setY(y);
			vector.setZ(z);

			double angle = rotation.getAngle();

			if (isLeftHanded()) angle = -angle;

			rotation.setAxisAngle(vector, angle);
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
	@Pure
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
	@Pure
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
	@Pure
	public static CoordinateSystem3D fromVectors(double ly, double lz, double ty, double tz) {
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
	@Pure
	public static CoordinateSystem3D fromVectors(double vx, double vy, double vz, double lx, double ly, double lz, double ux, double uy, double uz) {
		if (vx==1. && vy==0. && vz==0.) {
			assert(lx==0. && ux==0.);
			return fromVectors(ly, lz, uy, uz);
		}

		// Transform the given vectors to align V along (1,0,0)
		Transform3D mat = new Transform3D(vx, vy, vz, 0, lx, ly, lz, 0, ux, uy, uz, 0);

		Vector3D v1 = new Vector3D(vx, vy, vz);
		mat.transform(v1);
		normalizeVector(v1);

		Vector3D v2 = new Vector3D(lx, ly, lz);
		mat.transform(v2);
		normalizeVector(v2);

		Vector3D v3 = new Vector3D(ux, uy, uz);
		mat.transform(v3);
		normalizeVector(v3);

		assert(v1.getX()==1. && v1.getY()==0. && v1.getZ()==0.);
		assert(v2.getX()==0. && v3.getX()==0.);
		return fromVectors(v2.getY(), v2.getZ(), v3.getY(), v3.getZ());
	}

	private static void normalizeVector(Vector3D v) {
		v.normalize();
		if (MathUtil.isEpsilonZero(Math.abs(v.getX()-1.))) {
			v.setX(Math.signum(v.getX()));
			v.setY(0.f);
			v.setZ(0.f);
		}
		else if (MathUtil.isEpsilonZero(Math.abs(v.getY()-1.))) {
			v.setY(Math.signum(v.getY()));
			v.setX(0.f);
			v.setZ(0.f);
		}
		else if (MathUtil.isEpsilonZero(Math.abs(v.getZ()-1.))) {
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
	@Pure
	public static CoordinateSystem3D fromVectors(int vx, int vy, int vz, int lx, int ly, int lz, int ux, int uy, int uz) {
		if (vx==1 && vy==0 && vz==0) {
			assert(lx==0 && ux==0);
			return fromVectors(ly, lz, uy, uz);
		}

		// Transform the given vectors to align V along (1,0,0)
		Transform3D mat = new Transform3D(vx, vy, vz, 0, lx, ly, lz, 0, ux, uy, uz, 0);

		Vector3D v1 = new Vector3D(vx, vy, vz);
		mat.transform(v1);
		normalizeVector(v1);

		Vector3D v2 = new Vector3D(lx, ly, lz);
		mat.transform(v2);
		normalizeVector(v2);

		Vector3D v3 = new Vector3D(ux, uy, uz);
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
	@Pure
	public final double height(Tuple3f<?> tuple) {
		return height(tuple.getX(), tuple.getY(), tuple.getZ());
	}

	/** Replies the vertical position from the given 3D point for this coordinate system.
	 * 
	 * @param x
	 * @param y
	 * @param z
	 * @return the vertical position in <var>x/<var>, <var>y</var> or </var>z</var>
	 */
	@Pure
	public double height(double x, double y, double z) {
		double[] factors = fromSystemIndex(this.system);
		return (factors[2]!=0.) ? y : z;
	}

	/** Replies the horizontal left-right position from the given 3D point for this coordinate system.
	 * 
	 * @param tuple
	 * @return the horizontal and left-right position in <var>x/<var>, <var>y</var> or </var>z</var>
	 * @since 4.0
	 */
	@Pure
	public final double side(Tuple3f<?> tuple) {
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
	@Pure
	public double side(double x, double y, double z) {
		double[] factors = fromSystemIndex(this.system);
		return (factors[2]!=0.) ? z : y;
	}

	/** Replies the horizontal front-back position from the given 3D point for this coordinate system.
	 * 
	 * @param tuple
	 * @return the horizontal and front-back position in <var>x/<var>, <var>y</var> or </var>z</var>
	 * @since 4.0
	 */
	@Pure
	public final static double view(Tuple3f<?> tuple) {
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
	@Pure
	public static double view(double x, double y, double z) {
		return x;
	}

	/** Replies index of the coordinate which is corresponding to
	 * the height for the coordinate system.
	 * 
	 * @return the index of the coordinate of the height.
	 */
	@Pure
	public int getHeightCoordinateIndex() {
		double[] factors = fromSystemIndex(this.system);
		return (factors[2]!=0.) ? 1 : 2;
	}

	/** Replies index of the coordinate which is corresponding to
	 * the side for the coordinate system.
	 * 
	 * @return the index of the coordinate of the side.
	 * @since 4.0
	 */
	@Pure
	public int getSideCoordinateIndex() {
		double[] factors = fromSystemIndex(this.system);
		return (factors[2]!=0.) ? 2 : 1;
	}

	/** Replies index of the coordinate which is corresponding to
	 * the view for the coordinate system.
	 * 
	 * @return the index of the coordinate of the view.
	 * @since 4.0
	 */
	@Pure
	public static int getViewCoordinateIndex() {
		return 0;
	}

	/** Set the vertical position in the given 3D point for this coordinate system.
	 * 
	 * @param tuple
	 * @param height is the height to put in the tuple.
	 */
	public final void setHeight(Tuple3f<?> tuple, double height) {
		double[] factors = fromSystemIndex(this.system);
		if (factors[2]!=0.) tuple.setY(height);
		else tuple.setZ(height);
	}

	/** Add the vertical amount to the height field of the given 3D point
	 * for this coordinate system.
	 * 
	 * @param tuple
	 * @param additionalHeight is the height amount to add to the tuple.
	 */
	public final void addHeight(Tuple3f<?> tuple, double additionalHeight) {
		double[] factors = fromSystemIndex(this.system);
		if (factors[2]!=0.) tuple.setY(tuple.getY() + additionalHeight);
		else tuple.setZ(tuple.getZ() + additionalHeight);
	}

	/** Set the left-right position in the given 3D point for this coordinate system.
	 * 
	 * @param tuple
	 * @param side is the side amount to put in the tuple.
	 * @since 4.0
	 */
	public final void setSide(Tuple3f<?> tuple, double side) {
		double[] factors = fromSystemIndex(this.system);
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
	public final void addSide(Tuple3f<?> tuple, double additionalAmount) {
		double[] factors = fromSystemIndex(this.system);
		if (factors[2]!=0.) tuple.setZ(tuple.getZ() + additionalAmount);
		else tuple.setY(tuple.getY() + additionalAmount);
	}

	/** Set the front-back position in the given 3D point for this coordinate system.
	 * 
	 * @param tuple
	 * @param amount is the amount to put in the tuple.
	 * @since 4.0
	 */
	public final static void setView(Tuple3f<?> tuple, double amount) {
		tuple.setX(amount);
	}

	/** Add the front-back amount to the field of the given 3D point
	 * for this coordinate system.
	 * 
	 * @param tuple
	 * @param additionalViewAmount is the amount to add to the tuple.
	 */
	public final static void addView(Tuple3f<?> tuple, double additionalViewAmount) {
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
	@Pure
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
	 * @param result the 2D point.
	 */
	@Pure
	public void toCoordinateSystem2D(Point3D point, Point2D result) {
		switch(this) {
		case XYZ_RIGHT_HAND:
		case XYZ_LEFT_HAND:
			result.set(point.getX(), point.getY());
			break;
		case XZY_LEFT_HAND:
		case XZY_RIGHT_HAND:
			result.set(point.getX(), point.getZ());
			break;
		default:
			throw new CoordinateSystemNotFoundException();
		}
	}

	/** Convert the specified point into from the current coordinate system
	 * to the specified coordinate system.
	 * 
	 * @param vector is the vector to convert
	 * @param result the 2D vector
	 */
	@Pure
	public void toCoordinateSystem2D(Vector3D vector, Vector2D result) {
		switch(this) {
		case XYZ_RIGHT_HAND:
		case XYZ_LEFT_HAND:
			result.set(vector.getX(), vector.getY());
			break;
		case XZY_LEFT_HAND:
		case XZY_RIGHT_HAND:
			result.set(vector.getX(), vector.getZ());
			break;
		default:
			throw new CoordinateSystemNotFoundException();
		}
	}

	/** Convert the specified transformation into from the current coordinate system
	 * to the specified coordinate system.
	 * 
	 * @param transformation is the transformation to convert
	 * @param result the 2D transformation
	 */
	@Pure
	public void toCoordinateSystem2D(Transform3D transformation, Transform2D result) {
		double angle = toCoordinateSystem2DAngleFromTransformation(transformation);
		Point3D p = new FakePoint3afp();
		transformation.transform(p);
		result.setIdentity();		
		result.setRotation(angle);
		Point2D p2 = new FakePoint2afp();
		toCoordinateSystem2D(p, p2);
		result.setTranslation(p2);
	}

	/** Convert the specified rotation axis into from the current coordinate system
	 * to the specified coordinate system.
	 * 
	 * @param rotation is the rotation to convert
	 * @return the 2D rotation
	 */
	@Pure
	public double toCoordinateSystem2D(Quaternion rotation) {
		Transform3D trans = new Transform3D();
		trans.setRotation(rotation);
		return toCoordinateSystem2DAngleFromTransformation(trans);
	}

	@Pure
	private double toCoordinateSystem2DAngleFromTransformation(Transform3D mat) {
		Vector3D ptR = new Vector3D(1,0,0);
		mat.transform(ptR);
		switch(this) {
		case XYZ_LEFT_HAND:
		case XYZ_RIGHT_HAND:
			return Vector2D.signedAngle(1, 0,ptR.getX(), ptR.getY());
		case XZY_LEFT_HAND:
		case XZY_RIGHT_HAND:
			return Vector2D.signedAngle(ptR.getX(), ptR.getZ(), 1, 0);
		default:
		}
		throw new CoordinateSystemNotFoundException();
	}

	/** Convert the specified point from the 2D coordinate system
	 * to this specified coordinate system.
	 * <p>
	 * Thr third coordinate is set to <code>0</code>.
	 * 
	 * @param point is the point to convert
	 * @return the 3D point
	 */
	@Pure
	public final Point3f fromCoordinateSystem2D(Point2fx point) {
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
	@Pure
	public final Point3D fromCoordinateSystem2D(Point2D point) {
		return fromCoordinateSystem2D(point, 0);
	}

	/** Convert the specified point from the 2D coordinate system
	 * to this specified coordinate system.
	 * 
	 * @param point is the point to convert
	 * @param thirdCoordinate is the value of the third coordinate to put in the replied point.
	 * @return the 3D point
	 */
	@Pure
	public Point3f fromCoordinateSystem2D(Point2fx point, double thirdCoordinate) {
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
	@Pure
	public Point3D fromCoordinateSystem2D(Point2D point, double thirdCoordinate) {
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

	/** Convert the specified vector from the 2D coordinate system
	 * to this specified coordinate system.
	 * <p>
	 * Thr third coordinate is set to <code>0</code>.
	 * 
	 * @param vector is the vector to convert
	 * @return the 3D vector
	 */
	@Pure
	public final Vector3D fromCoordinateSystem2D(Vector2fx vector) {
		return fromCoordinateSystem2D(vector, 0);
	}

	/** Convert the specified vector from the 2D coordinate system
	 * to this specified coordinate system.
	 * 
	 * @param point is the vector to convert
	 * @param thirdCoordinate is the value of the third coordinate to put in the replied vector.
	 * @return the 3D vector
	 */
	@Pure
	public Vector3D fromCoordinateSystem2D(Vector2fx point, double thirdCoordinate) {
		switch(this) {
		case XYZ_LEFT_HAND:
		case XYZ_RIGHT_HAND:
			return new Vector3D(point.getX(), point.getY(), thirdCoordinate);
		case XZY_LEFT_HAND:
		case XZY_RIGHT_HAND:
			return new Vector3D(point.getX(), thirdCoordinate, point.getY());
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
	@Pure
	public Quaternion fromCoordinateSystem2D(double rotation) {
		Quaternion q = new Quaternion();
		switch(this) {
		case XYZ_LEFT_HAND:
		case XYZ_RIGHT_HAND:
			q.setAxisAngle(0,0,1,rotation);
			break;
		case XZY_LEFT_HAND:
		case XZY_RIGHT_HAND:
			q.setAxisAngle(0,1,0,rotation);
			break;
		default:
			throw new CoordinateSystemNotFoundException();
		}
		return q;
	}

	/** Replies the view vector of this coordinate space.
	 * <p>
	 * When objects have not been rotated, they are supposed to
	 * have a front direction colinear to this view vector.
	 * 
	 * @return the view vector (always normalized).
	 */
	@Pure
	public final Vector3D getViewVector() {
		return getViewVector(new Vector3D());
	}

	/** Replies the view vector of this coordinate space.
	 * <p>
	 * When objects have not been rotated, they are supposed to
	 * have a front direction colinear to this view vector.
	 * 
	 * @param vectorToFill is the vector to fill with the view vector coordinates.
	 * @return <var>vectorToFill</var>.
	 */
	public final Vector3D getViewVector(Vector3D vectorToFill) {
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
	@Pure
	public final Vector3D getBackVector() {
		return getBackVector(new Vector3D());
	}

	/** Replies the back vector of this coordinate space.
	 * <p>
	 * When objects have not been rotated, they are supposed to
	 * have a back direction colinear to this back vector.
	 * 
	 * @param vectorToFill is the vector to fill with the back vector coordinates.
	 * @return <var>vectorToFill</var>.
	 */
	public final Vector3D getBackVector(Vector3D vectorToFill) {
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
	@Pure
	public final Vector3D getLeftVector() {
		return getLeftVector(new Vector3D());
	}

	/** Replies the left vector of this coordinate space.
	 * <p>
	 * When objects have not been rotated, they are supposed to
	 * have a left direction colinear to this left vector.
	 * 
	 * @param vectorToFill is the vector to fill with the left vector coordinates.
	 * @return <var>vectorToFill</var>.
	 */
	public final Vector3D getLeftVector(Vector3D vectorToFill) {
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
	@Pure
	public final Vector3D getRightVector() {
		return getRightVector(new Vector3D());
	}

	/** Replies the right vector of this coordinate space.
	 * <p>
	 * When objects have not been rotated, they are supposed to
	 * have a right direction colinear to this right vector.
	 * 
	 * @param vectorToFill is the vector to fill with the right vector coordinates.
	 * @return <var>vectorToFill</var>.
	 */
	public final Vector3D getRightVector(Vector3D vectorToFill) {
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
	@Pure
	public final Vector3D getUpVector() {
		return getUpVector(new Vector3D());
	}

	/** Replies the up vector of this coordinate space.
	 * <p>
	 * When objects have not been rotated, they are supposed to
	 * have a up direction colinear to this up vector.
	 * 
	 * @param vectorToFill is the vector to fill with the up vector coordinates.
	 * @return <var>vectorToFill</var>.
	 */
	public final Vector3D getUpVector(Vector3D vectorToFill) {
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
	@Pure
	public final Vector3D getDownVector() {
		return getDownVector(new Vector3D());
	}

	/** Replies the down vector of this coordinate space.
	 * <p>
	 * When objects have not been rotated, they are supposed to
	 * have a down direction colinear to this down vector.
	 * 
	 * @param vectorToFill is the vector to fill with the down vector coordinates.
	 * @return <var>vectorToFill</var>.
	 */
	public final Vector3D getDownVector(Vector3D vectorToFill) {
		if (vectorToFill!=null) {
			vectorToFill.set(0.f,0.f,-1.f);
			fromPivot(vectorToFill);
		}
		return vectorToFill;
	}

	/** {@inheritDoc}
	 */
	@Pure
	@Override
	public boolean isRightHanded() {
		return this==XYZ_RIGHT_HAND || this==XZY_RIGHT_HAND;
	}

	/** {@inheritDoc}
	 */
	@Pure
	@Override
	public boolean isLeftHanded() {
		return this==XYZ_LEFT_HAND || this==XZY_LEFT_HAND;
	}

	/** Replies if the z coordinate is the up direction.
	 * 
	 * @return <code>true</code> if z coordiante is up.
	 */
	@Pure
	public boolean isZOnUp() {
		return this==XYZ_LEFT_HAND || this==XYZ_RIGHT_HAND;
	}

	/** Replies if the y coordinate is the up direction.
	 * 
	 * @return <code>true</code> if y coordiante is up.
	 */
	@Pure
	public boolean isYOnUp() {
		return this==XZY_LEFT_HAND || this==XZY_RIGHT_HAND;
	}

	/** Replies if the z coordinate is the side direction (left or right).
	 * 
	 * @return <code>true</code> if z coordiante is side.
	 */
	@Pure
	public boolean isZOnSide() {
		return this==XZY_LEFT_HAND || this==XZY_RIGHT_HAND;
	}

	/** Replies if the y coordinate is the side direction (left or right).
	 * 
	 * @return <code>true</code> if y coordiante is side.
	 */
	@Pure
	public boolean isYOnSide() {
		return this==XYZ_LEFT_HAND || this==XYZ_RIGHT_HAND;
	}

}
