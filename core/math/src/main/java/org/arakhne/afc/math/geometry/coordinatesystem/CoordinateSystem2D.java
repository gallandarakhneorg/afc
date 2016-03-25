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

import org.arakhne.afc.math.geometry.d2.ImmutableVector2D;
import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.Transform2D;
import org.arakhne.afc.math.geometry.d2.Vector2D;
import org.eclipse.xtext.xbase.lib.Pure;

/**
 * Represents the different kind of 2D referencials
 * and provides the convertion utilities.
 * <p>
 * A referencial axis is expressed by the front and left directions.
 * For example <code>XY_LEFT_HAND</code> is for the coordinate system
 * with front direction along <code>+X</code> axis,
 * and left direction along the <code>-Y</code> axis according to
 * a "left-hand" heuristic.
 * <p>
 * The default coordinate system is:
 * <ul>
 * <li>front: <code>(1,0)</code></li>
 * <li>left: <code>(0,1)</code></li>
 * </ul>
 * 
 * @author $Author: cbohrhauer$
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public enum CoordinateSystem2D implements CoordinateSystem {

	/** Right handed XY coordinate system.
	 * <p>
	 * <a href="doc-files/xy_right.png"><img border="0" src="doc-files/xy_right.png" width="200" alt="[Right Handed XY Coordinate System]"></a>
	 */
	XY_RIGHT_HAND,

	/** Left handed XY coordinate system.
	 * <p>
         * <a href="doc-files/xy_left.png"><img border="0" src="doc-files/xy_left.png" width="200" alt="[Left Handed XY Coordinate System]"></a>
	 */
	XY_LEFT_HAND;
	
	private static CoordinateSystem2D defaultCoordinateSystem;
	
	/** {@inheritDoc}
	 */
	@Pure
	@Override
	public final int getDimensions() {
		return 2;
	}

	/** Convert the specified point into from the current coordinate system
	 * to the specified coordinate system.
	 * 
	 * @param point is the point to convert
	 * @param targetCoordinateSystem is the target coordinate system.
	 */
	public void toSystem(Point2D point, CoordinateSystem2D targetCoordinateSystem) {
		if (this!=targetCoordinateSystem) {
			point.setY(-point.getY());
		}
	}

	/** Convert the specified point into from the current coordinate system
	 * to the specified coordinate system.
	 * 
	 * @param point is the point to convert
	 * @param targetCoordinateSystem is the target coordinate system.
	 */
	public void toSystem(Vector2D point, CoordinateSystem2D targetCoordinateSystem) {
		if (this!=targetCoordinateSystem) {
			point.setY(-point.getY());
		}
	}

	/** Convert the specified rotation into from the current coordinate system
	 * to the specified coordinate system.
	 * 
	 * @param rotation is the rotation to convert
	 * @param targetCoordinateSystem is the target coordinate system.
	 * @return the converted rotation
	 */
	@Pure
	public double toSystem(double rotation, CoordinateSystem2D targetCoordinateSystem) {
		if (this!=targetCoordinateSystem) {
			return -rotation;
		}
		return rotation;
	}

	/** Convert the specified transformation matrix into from the current coordinate system
	 * to the specified coordinate system.
	 * 
	 * @param matrix is the matrix to convert
	 * @param targetCoordinateSystem is the target coordinate system.
	 */
	public void toSystem(Transform2D matrix, CoordinateSystem2D targetCoordinateSystem) {
		if (this!=targetCoordinateSystem) {
			double r = -matrix.getRotation();
			matrix.setRotation(r);
			matrix.setM21(-matrix.getM21());
		}
	}

	/** Convert the specified point into the default coordinate system.
	 * 
	 * @param point is the point to convert
	 */
	public void toDefault(Point2D point) {
		if (this!=getDefaultCoordinateSystem()) {
			point.setY(-point.getY());
		}
	}

	/** Convert the specified point from the default coordinate system.
	 * 
	 * @param point is the point to convert
	 */
	public void fromDefault(Point2D point) {
		if (this!=getDefaultCoordinateSystem()) {
			point.setY(-point.getY());
		}
	}
	
	/** Convert the specified vector from the default coordinate system.
	 * 
	 * @param vector is the vector to convert
	 */
	public void toDefault(Vector2D vector) {
		if (this!=getDefaultCoordinateSystem()) {
			vector.setY(-vector.getY());
		}
	}

	/** Convert the specified vector from the default coordinate system.
	 * 
	 * @param vector is the vector to convert
	 */
	public void fromDefault(Vector2D vector) {
		if (this!=getDefaultCoordinateSystem()) {
			vector.setY(-vector.getY());
		}
	}

	/** Convert the specified transformation matrix from the default coordinate system.
	 * 
	 * @param matrix is the matrix to convert
	 * @since 4.0
	 */
	public void fromDefault(Transform2D matrix) {
		if (this!=getDefaultCoordinateSystem()) {
			double r = -matrix.getRotation();
			matrix.setRotation(r);
			matrix.setM21(-matrix.getM21());
		}
	}

	/** Convert the specified transformation matrix from the default coordinate system.
	 * 
	 * @param matrix is the matrix to convert
	 */
	public void toDefault(Transform2D matrix) {
		if (this!=getDefaultCoordinateSystem()) {
			double r = -matrix.getRotation();
			matrix.setRotation(r);
			matrix.setM21(-matrix.getM21());
		}
	}

	/** Convert the specified rotation into the default coordinate system.
	 * 
	 * @param rotation is the rotation to convert
	 * @return the converted rotation
	 */
	@Pure
	public double toDefault(double rotation) {
		if (this!=getDefaultCoordinateSystem()) {
			return -rotation;
		}
		return rotation;
	}

	/** Convert the specified rotation from the default coordinate system.
	 * 
	 * @param rotation is the rotation to convert
	 * @return the converted rotation
	 */
	@Pure
	public double fromDefault(double rotation) {
		if (this!=getDefaultCoordinateSystem()) {
			return -rotation;
		}
		return rotation;
	}

	/** Replies the default coordinate system.
	 * 
	 * @return the default coordinate system.
	 * @see #setDefaultCoordinateSystem(CoordinateSystem2D)
	 */
	@Pure
	public static CoordinateSystem2D getDefaultCoordinateSystem() {
		if (defaultCoordinateSystem==null) return CoordinateSystemConstants.SIMULATION_2D;
		return defaultCoordinateSystem;
	}

	/** Set the default coordinate system.
	 * 
	 * @param system is the new default coordinate system.
	 * @see #getDefaultCoordinateSystem()
	 */
	public static void setDefaultCoordinateSystem(CoordinateSystem2D system) {
		defaultCoordinateSystem = system;
	}

	/** Replies the coordinate system which is corresponding to the specified
	 * orientation unit vectors.
	 * <p>
	 * The front vector is assumed to be always <code>(1,0)</code>,
	 * and the left vector is <code>(0,ly)</code>.
	 *
	 * @param ly
	 * @return the coordinate system which is corresponding to the specified vector.
	 */
	@Pure
	public static CoordinateSystem2D fromVectors(int ly) {
		assert(ly!=0);
		return (ly<0) ? XY_LEFT_HAND : XY_RIGHT_HAND;
	}

	/** Replies the coordinate system which is corresponding to the specified
	 * orientation unit vectors.
	 * <p>
	 * The front vector is assumed to be always <code>(1,0)</code>,
	 * and the left vector is <code>(0,ly)</code>.
	 * 
	 * @param ly
	 * @return the coordinate system which is corresponding to the specified vector.
	 */
	@Pure
	public static CoordinateSystem2D fromVectors(double ly) {
		return fromVectors((int)ly);
	}
	
	/** {@inheritDoc}
	 */
	@Pure
	@Override
	public boolean isRightHanded() {
		return this==XY_RIGHT_HAND;
	}

	/** {@inheritDoc}
	 */
	@Pure
	@Override
	public boolean isLeftHanded() {
		return this==XY_LEFT_HAND;
	}
	
	/** Replies the front vector.
	 *
	 * @return the front vector.
	 */
	@Pure
	public static Vector2D getViewVector() {
		return new ImmutableVector2D(1,0);
	}

	/** Replies the front vector.
	 *
	 * @param vectorToFill is the vector to set with the front vector values.
	 * @return the front vector.
	 * @since 4.0
	 */
	public static <T extends Vector2D> T getViewVector(T vectorToFill) {
		assert(vectorToFill!=null);
		vectorToFill.set(1,0);
		return vectorToFill;
	}

	/** Replies the back vector.
	 *
	 * @return the back vector.
	 */
	@Pure
	public static Vector2D getBackVector() {
		return new ImmutableVector2D(-1,0);
	}

	/** Replies the back vector.
	 *
	 * @param vectorToFill is the vector to set with the back vector values.
	 * @return the back vector.
	 * @since 4.0
	 */
	public static <T extends Vector2D> T getBackVector(T vectorToFill) {
		assert(vectorToFill!=null);
		vectorToFill.set(-1,0);
		return vectorToFill;
	}

	/** Replies the left vector.
	 *
	 * @return the left vector.
	 */
	@Pure
	public Vector2D getLeftVector() {
		switch(this) {
		case XY_LEFT_HAND:
			return new ImmutableVector2D(0,-1);
		case XY_RIGHT_HAND:
			return new ImmutableVector2D(0,1);
		default:
			throw new IllegalArgumentException("this"); //$NON-NLS-1$
		}
	}

	/** Replies the left vector.
	 *
	 * @param vectorToFill is the vector to set with the left vector values.
	 * @return the left vector.
	 * @since 4.0
	 */
	public <T extends Vector2D> T getLeftVector(T vectorToFill) {
		assert(vectorToFill!=null);
		switch(this) {
		case XY_LEFT_HAND:
			vectorToFill.set(0,-1);
			return vectorToFill;
		case XY_RIGHT_HAND:
			vectorToFill.set(0,1);
			return vectorToFill;
		default:
			throw new IllegalArgumentException("this"); //$NON-NLS-1$

		}
	}

	/** Replies the right vector.
	 *
	 * @return the right vector.
	 */
	@Pure
	public Vector2D getRightVector() {
		switch(this) {
		case XY_LEFT_HAND:
			return new ImmutableVector2D(0,1);
		case XY_RIGHT_HAND:
			return new ImmutableVector2D(0,-1);
		default:
			throw new IllegalArgumentException("this"); //$NON-NLS-1$
		}
	}

	/** Replies the right vector.
	 *
	 * @param vectorToFill is the vector to set with the right vector values.
	 * @return the right vector.
	 * @since 4.0
	 */
	public <T extends Vector2D> T getRightVector(T vectorToFill) {
		assert(vectorToFill!=null);
		switch(this) {
		case XY_LEFT_HAND:
			vectorToFill.set(0,1);
			return vectorToFill;
		case XY_RIGHT_HAND:
			vectorToFill.set(0,-1);
			return vectorToFill;
		default:
			throw new IllegalArgumentException("this"); //$NON-NLS-1$
		}
	}

	/** Replies the 3D coordinate system which is corresponding to
	 * this 2D coordinate system.
	 * 
	 * @return the 3D coordinate system
	 * @since 4.0
	 */
	@Pure
	public CoordinateSystem3D toCoordinateSystem3D() {
		switch(this) {
		case XY_LEFT_HAND:
			return CoordinateSystem3D.XYZ_LEFT_HAND;
		case XY_RIGHT_HAND:
			return CoordinateSystem3D.XYZ_RIGHT_HAND;
		default:
			throw new CoordinateSystemNotFoundException();
		}
	}

}
