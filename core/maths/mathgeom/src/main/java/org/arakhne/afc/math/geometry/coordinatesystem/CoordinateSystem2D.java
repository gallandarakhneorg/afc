/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2018 The original authors, and other authors.
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

package org.arakhne.afc.math.geometry.coordinatesystem;

import org.eclipse.xtext.xbase.lib.Inline;
import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.geometry.d2.ImmutableVector2D;
import org.arakhne.afc.math.geometry.d2.Transform2D;
import org.arakhne.afc.math.geometry.d2.Tuple2D;
import org.arakhne.afc.math.geometry.d2.Vector2D;
import org.arakhne.afc.math.geometry.d2.d.Vector2d;
import org.arakhne.afc.math.geometry.d3.Quaternion;
import org.arakhne.afc.math.geometry.d3.Transform3D;
import org.arakhne.afc.math.geometry.d3.Tuple3D;
import org.arakhne.afc.math.geometry.d3.Vector3D;
import org.arakhne.afc.math.geometry.d3.d.Quaternion4d;
import org.arakhne.afc.math.geometry.d3.d.Vector3d;
import org.arakhne.afc.vmutil.asserts.AssertMessages;

/**
 * Represents the different kind of 2D referencials
 * and provides the conversion utilities.
 *
 * <p>A referencial axis is expressed by the front and left directions.
 * For example <code>XY_LEFT_HAND</code> is for the coordinate system
 * with front direction along <code>+X</code> axis,
 * and left direction along the <code>-Y</code> axis according to
 * a "left-hand" heuristic.
 *
 * <p>The default coordinate system is:
 * <ul>
 * <li>front: <code>(1, 0)</code></li>
 * <li>left: <code>(0, 1)</code></li>
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
	 *
	 * <p><a href="doc-files/xy_right.png"><img src="doc-files/xy_right.png" width="200"
	 * alt="[Right Handed XY Coordinate System]"></a>
	 */
	XY_RIGHT_HAND {
		@Pure
		@Override
		public Vector2D<?, ?> getLeftVector() {
			return new ImmutableVector2D(0, 1);
		}

		@Pure
		@Override
		public <T extends Vector2D<?, ?>> T getLeftVector(T vectorToFill) {
			assert vectorToFill != null;
			vectorToFill.set(0, 1);
			return vectorToFill;
		}

		@Pure
		@Override
		public Vector2D<?, ?> getRightVector() {
			return new ImmutableVector2D(0, -1);
		}

		@Override
		public <T extends Vector2D<?, ?>> T getRightVector(T vectorToFill) {
			assert vectorToFill != null;
			vectorToFill.set(0, -1);
			return vectorToFill;
		}

		@Pure
		@Override
		public CoordinateSystem3D toCoordinateSystem3D() {
			return CoordinateSystem3D.XYZ_RIGHT_HAND;
		}
	},

	/** Left handed XY coordinate system.
	 *
	 * <p><a href="doc-files/xy_left.png"><img src="doc-files/xy_left.png" width="200"
	 * alt="[Left Handed XY Coordinate System]"></a>
	 */
	XY_LEFT_HAND {
		@Pure
		@Override
		public Vector2D<?, ?> getLeftVector() {
			return new ImmutableVector2D(0, -1);
		}

		@Pure
		@Override
		public <T extends Vector2D<?, ?>> T getLeftVector(T vectorToFill) {
			assert vectorToFill != null;
			vectorToFill.set(0, -1);
			return vectorToFill;
		}

		@Pure
		@Override
		public Vector2D<?, ?> getRightVector() {
			return new ImmutableVector2D(0, 1);
		}

		@Override
		public <T extends Vector2D<?, ?>> T getRightVector(T vectorToFill) {
			assert vectorToFill != null;
			vectorToFill.set(0, 1);
			return vectorToFill;
		}

		@Pure
		@Override
		public CoordinateSystem3D toCoordinateSystem3D() {
			return CoordinateSystem3D.XYZ_LEFT_HAND;
		}
	};

	private static CoordinateSystem2D defaultCoordinateSystem;

	@Pure
	@Override
	public final int getDimensions() {
		return 2;
	}

	/** Convert the specified point/vector from the current coordinate system
	 * to the specified coordinate system.
	 *
	 * @param tuple is the point/vector to convert
	 * @param targetCoordinateSystem is the target coordinate system.
	 */
	public void toSystem(Tuple2D<?> tuple, CoordinateSystem2D targetCoordinateSystem) {
        assert tuple != null : AssertMessages.notNullParameter(0);
		assert targetCoordinateSystem != null : AssertMessages.notNullParameter(1);
		if (this != targetCoordinateSystem) {
			tuple.setY(-tuple.getY());
		}
	}

	/** Convert the specified rotation from the current coordinate system
	 * to the specified coordinate system.
	 *
	 * @param rotation is the rotation to convert
	 * @param targetCoordinateSystem is the target coordinate system.
	 * @return the converted rotation
	 */
	@Pure
	public double toSystem(double rotation, CoordinateSystem2D targetCoordinateSystem) {
        assert targetCoordinateSystem != null : AssertMessages.notNullParameter(1);
		if (this != targetCoordinateSystem) {
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
        assert matrix != null : AssertMessages.notNullParameter(0);
        assert targetCoordinateSystem != null : AssertMessages.notNullParameter(1);
		if (this != targetCoordinateSystem) {
			final double r = -matrix.getRotation();
			matrix.setRotation(r);
			matrix.setM12(-matrix.getM12());
		}
	}

	/** Convert the specified point/vector into the default coordinate system.
	 *
	 * @param tuple is the point/vector to convert
	 */
	public void toDefault(Tuple2D<?> tuple) {
		if (this != getDefaultCoordinateSystem()) {
			tuple.setY(-tuple.getY());
		}
	}

	/** Convert the specified transformation matrix from the default coordinate system.
	 *
	 * @param matrix is the matrix to convert
	 */
	public void toDefault(Transform2D matrix) {
		if (this != getDefaultCoordinateSystem()) {
			final double r = -matrix.getRotation();
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
		if (this != getDefaultCoordinateSystem()) {
			return -rotation;
		}
		return rotation;
	}

	/** Convert the specified point/vector from the default coordinate system.
	 *
	 * @param tuple is the point/vector to convert
	 */
	public void fromDefault(Tuple2D<?> tuple) {
		if (this != getDefaultCoordinateSystem()) {
			tuple.setY(-tuple.getY());
		}
	}

	/** Convert the specified transformation matrix from the default coordinate system.
	 *
	 * @param matrix is the matrix to convert
	 */
	public void fromDefault(Transform2D matrix) {
		if (this != getDefaultCoordinateSystem()) {
			final double r = -matrix.getRotation();
			matrix.setRotation(r);
			matrix.setM21(-matrix.getM21());
		}
	}

	/** Convert the specified rotation from the default coordinate system.
	 *
	 * @param rotation is the rotation to convert
	 * @return the converted rotation
	 */
	@Pure
	public double fromDefault(double rotation) {
		if (this != getDefaultCoordinateSystem()) {
			return -rotation;
		}
		return rotation;
	}

	/** Replies the default coordinate system.
	 *
	 * <p>If it is not changed, the default coordinate system is the one used for 2D simulation:
	 * {@link CoordinateSystemConstants#SIMULATION_2D}.
	 *
	 * @return the default coordinate system.
	 * @see #setDefaultCoordinateSystem(CoordinateSystem2D)
	 */
	@Pure
	public static CoordinateSystem2D getDefaultCoordinateSystem() {
		if (defaultCoordinateSystem == null) {
			return CoordinateSystemConstants.SIMULATION_2D;
		}
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
	 *
	 * <p>The front vector is assumed to be always <code>(1, 0)</code>,
	 * and the left vector is <code>(0, ly)</code>.
	 *
	 * @param ly y coordinate to left.
	 * @return the coordinate system which is corresponding to the specified vector.
	 */
	@Pure
	public static CoordinateSystem2D fromVectors(int ly) {
		assert ly != 0;
		return (ly < 0) ? XY_LEFT_HAND : XY_RIGHT_HAND;
	}

	/** Replies the coordinate system which is corresponding to the specified
	 * orientation unit vectors.
	 *
	 * <p>The front vector is assumed to be always <code>(1, 0)</code>,
	 * and the left vector is <code>(0, ly)</code>.
	 * If <code>ly</code> is negative, then the corrdinate system is left-handed, otherwise
	 * it is right-handed.
	 *
	 * @param ly y coordinate to the left.
	 * @return the coordinate system which is corresponding to the specified vector.
	 */
	@Pure
	public static CoordinateSystem2D fromVectors(double ly) {
		assert ly != 0.;
		return (ly < 0.) ? XY_LEFT_HAND : XY_RIGHT_HAND;
	}

	@Pure
	@Override
	public boolean isRightHanded() {
		return this == XY_RIGHT_HAND;
	}

	@Pure
	@Override
	public boolean isLeftHanded() {
		return this == XY_LEFT_HAND;
	}

	/** Replies the front vector.
	 *
	 * @return the front vector.
	 */
	@Pure
	@Inline(value = "new ImmutableVector2D(1, 0)", imported = {ImmutableVector2D.class})
	public static Vector2D<?, ?> getViewVector() {
		return new ImmutableVector2D(1, 0);
	}

	/** Replies the front vector.
	 *
	 * @param <T> the type of the vector.
	 * @param vectorToFill is the vector to set with the front vector values.
	 * @return <code>vectorToFill</code>.
	 */
	public static <T extends Vector2D<?, ?>> T getViewVector(T vectorToFill) {
		assert vectorToFill != null;
		vectorToFill.set(1, 0);
		return vectorToFill;
	}

	/** Replies the back vector.
	 *
	 * @return the back vector.
	 */
	@Pure
	@Inline(value = "new ImmutableVector2D(-1, 0)", imported = {ImmutableVector2D.class})
	public static Vector2D<?, ?> getBackVector() {
		return new ImmutableVector2D(-1, 0);
	}

	/** Replies the back vector.
	 *
	 * @param <T> the type of the vector.
	 * @param vectorToFill is the vector to set with the back vector values.
	 * @return the back vector.
	 */
	public static <T extends Vector2D<?, ?>> T getBackVector(T vectorToFill) {
		assert vectorToFill != null;
		vectorToFill.set(-1, 0);
		return vectorToFill;
	}

	/** Replies the left vector.
	 *
	 * @return the left vector.
	 */
	@Pure
	public abstract Vector2D<?, ?> getLeftVector();

	/** Replies the left vector.
	 *
	 * @param <T> the type of the vector.
	 * @param vectorToFill is the vector to set with the left vector values.
	 * @return the left vector.
	 */
	public abstract <T extends Vector2D<?, ?>> T getLeftVector(T vectorToFill);

	/** Replies the right vector.
	 *
	 * @return the right vector.
	 */
	@Pure
	public abstract Vector2D<?, ?> getRightVector();

	/** Replies the right vector.
	 *
	 * @param <T> the type of the vector.
	 * @param vectorToFill is the vector to set with the right vector values.
	 * @return the right vector.
	 */
	public abstract <T extends Vector2D<?, ?>> T getRightVector(T vectorToFill);

	/** Replies the 3D coordinate system which is corresponding to
	 * this 2D coordinate system.
	 *
	 * @return the 3D coordinate system
	 */
	@Pure
	public abstract CoordinateSystem3D toCoordinateSystem3D();

	/** Convert the specified point into from the current coordinate system
	 * to the 3D coordinate system.
	 *
	 * @param point is the point to convert
	 * @param result the 3D point.
	 * @since 15.0
	 */
	@Pure
	public void toCoordinateSystem3D(Tuple2D<?> point, Tuple3D<?> result) {
		final CoordinateSystem3D cs = toCoordinateSystem3D();
		CoordinateSystem3D.setView(result, point.getX());
		cs.setSide(result, point.getY());
		cs.setHeight(result, 0);
	}

	/** Convert the specified rotation axis into from the current coordinate system
	 * to the 3D coordinate system.
	 *
	 * @param rotation is the rotation to convert
	 * @param result the 3D rotation.
	 * @since 15.0
	 */
	@Pure
	public void toCoordinateSystem3D(double rotation, Quaternion result) {
		final CoordinateSystem3D cs = toCoordinateSystem3D();
		final Vector3D<?, ?> up = cs.getUpVector();
		result.setAxisAngle(up.getX(), up.getY(), up.getZ(), rotation);
	}

	/** Convert the specified transformation into from the current coordinate system
	 * to the 3D coordinate system.
	 *
	 * @param transformation is the transformation to convert
	 * @param result the 3D transformation
	 * @since 15.0
	 */
	@Pure
	public void toCoordinateSystem3D(Transform2D transformation, Transform3D result) {
		final double angle = transformation.getRotation();
		final Quaternion4d quat = new Quaternion4d();
		toCoordinateSystem3D(angle, quat);
		result.makeRotationMatrix(quat);
		final Vector2d translation2d = new Vector2d();
		final Vector3d translation3d = new Vector3d();
		transformation.getTranslationVector(translation2d);
		toCoordinateSystem3D(translation2d, translation3d);
		result.setTranslation(translation3d);
	}

}
