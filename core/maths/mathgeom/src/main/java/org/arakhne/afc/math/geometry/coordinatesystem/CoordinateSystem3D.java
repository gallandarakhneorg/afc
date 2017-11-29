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

import org.arakhne.afc.math.MathUtil;
import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.Transform2D;
import org.arakhne.afc.math.geometry.d2.Tuple2D;
import org.arakhne.afc.math.geometry.d2.Vector2D;
import org.arakhne.afc.math.geometry.d2.afp.InnerComputationPoint2afp;
import org.arakhne.afc.math.geometry.d3.Point3D;
import org.arakhne.afc.math.geometry.d3.Quaternion;
import org.arakhne.afc.math.geometry.d3.Transform3D;
import org.arakhne.afc.math.geometry.d3.Tuple3D;
import org.arakhne.afc.math.geometry.d3.Vector3D;
import org.arakhne.afc.math.geometry.d3.afp.InnerComputationPoint3afp;
import org.arakhne.afc.math.geometry.d3.afp.InnerComputationVector3afp;
import org.arakhne.afc.math.geometry.d3.d.Quaternion4d;

/**
 * Represents the different kind of 3D reference and provides the conversion utilities.
 *
 * <p>A referencial axis is expressed by the front, left and top directions. For example <code>XYZ_LEFT_HAND</code> is for the
 * coordinate system with front direction along <code>+/-X</code> axis, left direction along the <code>+/-Y</code> axis and top
 * direction along the <code>+/-Z</code> axis according to a "left-hand" heuristic.
 *
 * <p>The default coordinate system is:
 * <ul>
 * <li>front: <code>(1, 0, 0)</code></li>
 * <li>left: <code>(0, 1, 0)</code></li>
 * <li>top: <code>(0, 0, 1)</code></li>
 * </ul>
 *
 * <h3>Rotations</h3>
 *
 * <p>Rotations in a 3D coordinate system follow the right/left hand rules
 * (assuming that <code>OX</code>, <code>OY</code> and <code>OZ</code> are the three axis of the coordinate system):
 * <table width="100%" summary="Rotations">
 * <tr>
 * <td>Right-handed rule:</td>
 * <td>
 * <ul>
 * <li>axis cycle is: <code>OX</code> &gt; <code>OY</code> &gt; <code>OZ</code> &gt; <code>OX</code> &gt; <code>OY</code>;</li>
 * <li>when rotating around <code>OX</code>, positive rotation angle is going from <code>OY</code> to <code>OZ</code></li>
 * <li>when rotating around <code>OY</code>, positive rotation angle is going from <code>OZ</code> to <code>OX</code></li>
 * <li>when rotating around <code>OZ</code>, positive rotation angle is going from <code>OX</code> to <code>OY</code></li>
 * </ul><br>
 * <img width="200" src="doc-files/rotation_right.png" alt="[Right-handed Rotation Rule]">
 * </td>
 * </tr><tr>
 * <td>Left-handed rule:</td>
 * <td>
 * <ul>
 * <li>axis cycle is: <code>OX</code> &gt; <code>OY</code> &gt; <code>OZ</code> &gt; <code>OX</code> &gt; <code>OY</code>;</li>
 * <li>when rotating around <code>OX</code>, positive rotation angle is going from <code>OY</code> to <code>OZ</code></li>
 * <li>when rotating around <code>OY</code>, positive rotation angle is going from <code>OZ</code> to <code>OX</code></li>
 * <li>when rotating around <code>OZ</code>, positive rotation angle is going from <code>OX</code> to <code>OY</code></li>
 * </ul><br>
 * <img width="200" src="doc-files/rotation_left.png" alt="[Left-handed Rotation Rule]">
 * </td>
 * </tr></table>
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
@SuppressWarnings("checkstyle:methodcount")
public enum CoordinateSystem3D implements CoordinateSystem {

    /**
     * Left handed XZY coordinate system.
     *
     * <p><a href="doc-files/xzy_left.png"><img src="doc-files/xzy_left.png" width="200" alt=
     * "[Left Handed XZY Coordinate System]"></a>
     */
    XZY_LEFT_HAND(0, 1, 1, 0) {
    	@Pure
    	@Override
    	public CoordinateSystem2D toCoordinateSystem2D() {
   			return CoordinateSystem2D.XY_RIGHT_HAND;
    	}

    	@Pure
    	@Override
    	public void toCoordinateSystem2D(Tuple3D<?> tuple, Tuple2D<?> result) {
   			result.set(tuple.getX(), tuple.getZ());
    	}

    	@Override
    	protected double toCoordinateSystem2DAngleFromTransformation(Transform3D mat) {
    		final Vector3D<?, ?> ptR = new InnerComputationVector3afp(1, 0, 0);
    		mat.transform(ptR);
   			return Vector2D.signedAngle(ptR.getX(), ptR.getZ(), 1, 0);
    	}
    },

    /**
     * Left handed XYZ coordinate system.
     *
     * <p><a href="doc-files/xyz_left.png"><img src="doc-files/xyz_left.png" width="200" alt=
     * "[Left Handed XYZ Coordinate System]"></a>
     */
    XYZ_LEFT_HAND(-1, 0, 0, 1) {
    	@Pure
    	@Override
    	public CoordinateSystem2D toCoordinateSystem2D() {
   			return CoordinateSystem2D.XY_LEFT_HAND;
    	}

    	@Pure
    	@Override
    	public void toCoordinateSystem2D(Tuple3D<?> tuple, Tuple2D<?> result) {
   			result.set(tuple.getX(), tuple.getY());
    	}

    	@Override
    	protected double toCoordinateSystem2DAngleFromTransformation(Transform3D mat) {
    		final Vector3D<?, ?> ptR = new InnerComputationVector3afp(1, 0, 0);
    		mat.transform(ptR);
   			return Vector2D.signedAngle(1, 0, ptR.getX(), ptR.getY());
    	}
    },

    /**
     * Right handed XZY coordinate system.
     *
     * <p><a href="doc-files/xzy_right.png"><img src="doc-files/xzy_right.png" width="200" alt=
     * "[Right Handed XZY Coordinate System]"></a>
     */
    XZY_RIGHT_HAND(0, -1, 1, 0) {
    	@Pure
    	@Override
    	public CoordinateSystem2D toCoordinateSystem2D() {
   			return CoordinateSystem2D.XY_LEFT_HAND;
    	}

    	@Pure
    	@Override
    	public void toCoordinateSystem2D(Tuple3D<?> tuple, Tuple2D<?> result) {
   			result.set(tuple.getX(), tuple.getZ());
    	}

    	@Override
    	protected double toCoordinateSystem2DAngleFromTransformation(Transform3D mat) {
    		final Vector3D<?, ?> ptR = new InnerComputationVector3afp(1, 0, 0);
    		mat.transform(ptR);
   			return Vector2D.signedAngle(ptR.getX(), ptR.getZ(), 1, 0);
    	}
    },

    /**
     * Right handed XYZ coordinate system.
     *
     * <p><a href="doc-files/xyz_right.png"><img src="doc-files/xyz_right.png" width="200" alt=
     * "[Right Handed XYZ Coordinate System]"></a>
     */
    XYZ_RIGHT_HAND(1, 0, 0, 1) {
    	@Pure
    	@Override
    	public CoordinateSystem2D toCoordinateSystem2D() {
   			return CoordinateSystem2D.XY_RIGHT_HAND;
    	}

    	@Pure
    	@Override
    	public void toCoordinateSystem2D(Tuple3D<?> tuple, Tuple2D<?> result) {
   			result.set(tuple.getX(), tuple.getY());
    	}

    	@Override
    	protected double toCoordinateSystem2DAngleFromTransformation(Transform3D mat) {
    		final Vector3D<?, ?> ptR = new InnerComputationVector3afp(1, 0, 0);
    		mat.transform(ptR);
   			return Vector2D.signedAngle(1, 0, ptR.getX(), ptR.getY());
    	}
    };

	private static final byte PIVOT_SYSTEM = 0;

    private static CoordinateSystem3D defaultCoordinateSystem;

    private final byte system;

	/** Constructor.
	 *
	 * @param lefty y coordinate of the left vector.
	 * @param leftz z coordinate of the left vector.
	 * @param upy y coordinate of the up vector.
	 * @param upz z coordinate of the up vector.
	 */
	CoordinateSystem3D(int lefty, int leftz, int upy, int upz) {
		this.system = toSystemIndex(lefty, leftz, upy, upz);
	}

	@Pure
	@SuppressWarnings({ "checkstyle:returncount", "checkstyle:cyclomaticcomplexity", "magicnumber" })
	private static byte toSystemIndex(int lefty, int leftz, int topy, int topz) {
		if (lefty < 0) {
			if (leftz == 0 && topy == 0 && topz != 0) {
				if (topz < 0) {
                    return 1;
                }
                return 2;
            }
        } else if (lefty > 0) {
            if (leftz == 0 && topy == 0 && topz != 0) {
                if (topz < 0) {
                    return 3;
                }
                return 0;
            }
        } else {
            if (lefty == 0 && leftz != 0) {
                if (leftz < 0) {
                    if (topz == 0 && topy != 0) {
                        if (topy < 0) {
                            return 4;
                        }
                        return 5;
                    }
                } else {
                    if (topz == 0 && topy != 0) {
                        if (topy < 0) {
                            return 6;
                        }
                        return 7;
                    }
                }
            }
        }
        throw new CoordinateSystemNotFoundException();
    }

    @Pure
    @SuppressWarnings("magicnumber")
    private static double[] fromSystemIndex(int index) {
        // Compute the lower right sub-matrix
        final double c1;
        final double c2;
        final double c3;
        final double c4;
        switch (index) {
        case 1:
            c1 = -1;
            c2 = 0;
            c3 = 0;
            c4 = -1;
            break;
        case 2:
            c1 = -1;
            c2 = 0;
            c3 = 0;
            c4 = 1;
            break;
        case 3:
            c1 = 1;
            c2 = 0;
            c3 = 0;
            c4 = -1;
            break;
        case 4:
            c1 = 0;
            c2 = -1;
            c3 = -1;
            c4 = 0;
            break;
        case 5:
            c1 = 0;
            c2 = -1;
            c3 = 1;
            c4 = 0;
            break;
        case 6:
            c1 = 0;
            c2 = 1;
            c3 = -1;
            c4 = 0;
            break;
        case 7:
            c1 = 0;
            c2 = 1;
            c3 = 1;
            c4 = 0;
            break;
        default:
            c1 = 1;
            c2 = 0;
            c3 = 0;
            c4 = 1;
            break;
        }

        return new double[] {c1, c2, c3, c4 };
    }

    @Pure
    @Override
    public final int getDimensions() {
        return 3;
    }

    @Pure
    @Override
    public boolean isRightHanded() {
        return this == XYZ_RIGHT_HAND || this == XZY_RIGHT_HAND;
    }

    @Pure
    @Override
    public boolean isLeftHanded() {
    	return this == XYZ_LEFT_HAND || this == XZY_LEFT_HAND;
    }

    /** Replies the default coordinate system.
     *
     * <p>If it is not changed, the default coordinate system is the one used for 3D simulation:
     * {@link CoordinateSystemConstants#SIMULATION_3D}.
     *
     * @return the default coordinate system.
     * @see #setDefaultCoordinateSystem(CoordinateSystem3D)
	 */
    @Pure
    public static CoordinateSystem3D getDefaultCoordinateSystem() {
        if (defaultCoordinateSystem != null) {
            return defaultCoordinateSystem;
        }
        return CoordinateSystemConstants.SIMULATION_3D;
    }

    /** Set the default coordinate system.
     *
     * @param system is the new default coordinate system.
     * @see #getDefaultCoordinateSystem()
     */
    public static void setDefaultCoordinateSystem(CoordinateSystem3D system) {
        CoordinateSystem3D.defaultCoordinateSystem = system;
    }

	private void toPivot(Tuple3D<?> point) {
		if (this.system != PIVOT_SYSTEM) {
			final double[] factors = fromSystemIndex(this.system);
			final double y = point.getY() * factors[0] + point.getZ() * factors[1];
			final double z = point.getY() * factors[2] + point.getZ() * factors[3];
			point.setY(y);
			point.setZ(z);
		}
	}

	private void toPivot(Quaternion quaternion) {
		if (this.system != PIVOT_SYSTEM) {
			final double[] factors = fromSystemIndex(this.system);
			final Vector3D<?, ?> vector = quaternion.getAxis();
			final double y = vector.getY() * factors[0] + vector.getZ() * factors[1];
			final double z = vector.getY() * factors[2] + vector.getZ() * factors[3];
			vector.setY(y);
			vector.setZ(z);

			double angle = quaternion.getAngle();

			if (isLeftHanded()) {
				angle = -angle;
			}

			quaternion.setAxisAngle(vector, angle);
		}
	}

	private void toPivot(Transform3D trans) {
		if (this.system != PIVOT_SYSTEM) {
			final double[] factors = fromSystemIndex(this.system);
			final double ty = trans.getM13() * factors[0] + trans.getM23() * factors[1];
			final double tz = trans.getM13() * factors[2] + trans.getM23() * factors[3];
			trans.setTranslation(trans.getTranslationX(), ty, tz);
			final Quaternion r = new Quaternion4d();
			trans.getRotation(r);
			final Vector3D<?, ?> vector = r.getAxis();
			final double ry = vector.getY() * factors[0] + vector.getZ() * factors[1];
			final double rz = vector.getY() * factors[2] + vector.getZ() * factors[3];

			double angle = r.getAngle();

			if (isLeftHanded()) {
				angle = -angle;
			}

			r.setAxisAngle(vector.getX(), ry, rz, angle);
			trans.setRotation(r);
		}

	}

	/** Convert the specified point or vector into from the current coordinate system
	 * to the specified coordinate system.
	 *
	 * @param tuple is the point or vector to convert
	 * @param targetCoordinateSystem is the target coordinate system.
	 */
	public void toSystem(Tuple3D<?> tuple, CoordinateSystem3D targetCoordinateSystem) {
		if (targetCoordinateSystem != this) {
			toPivot(tuple);
			targetCoordinateSystem.fromPivot(tuple);
		}
	}

	/** Convert the specified quaternion into from the current coordinate system
	 * to the specified coordinate system.
	 *
	 * @param quat is the rotation to convert
	 * @param targetCoordinateSystem is the target coordinate system.
	 */
	public void toSystem(Quaternion quat, CoordinateSystem3D targetCoordinateSystem) {
		if (targetCoordinateSystem != this) {
			toPivot(quat);
			targetCoordinateSystem.fromPivot(quat);
		}
	}

	/** Convert the specified transformation matrix into from the current coordinate system
	 * to the specified coordinate system.
	 *
	 * @param matrix is the matrix to convert
	 * @param targetCoordinateSystem is the target coordinate system.
	 */
	public void toSystem(Transform3D matrix, CoordinateSystem3D targetCoordinateSystem) {
		if (targetCoordinateSystem != this) {
			toPivot(matrix);
			targetCoordinateSystem.fromPivot(matrix);
		}
	}

	/** Convert the specified point or vector into the default coordinate system.
	 *
	 * @param tuple is the point or vector to convert
	 */
	public void toDefault(Tuple3D<?> tuple) {
		toSystem(tuple, getDefaultCoordinateSystem());
	}

	/** Convert the specified rotation into the default coordinate system.
	 *
	 * @param rotation is the rotation to convert
	 */
	public void toDefault(Quaternion rotation) {
		toSystem(rotation, getDefaultCoordinateSystem());
	}

	/** Convert the specified rotation into the default coordinate system.
	 *
	 * @param rotation is the rotation to convert
	 */
	public void toDefault(Transform3D rotation) {
		toSystem(rotation, getDefaultCoordinateSystem());
	}

	private void fromPivot(Tuple3D<?> tuple) {
		if (this.system != PIVOT_SYSTEM) {
			final double[] factors = fromSystemIndex(this.system);
			final double y = tuple.getY() * factors[0] + tuple.getZ() * factors[2];
			final double z = tuple.getY() * factors[1] + tuple.getZ() * factors[3];
			tuple.setY(y);
			tuple.setZ(z);
		}
	}

	private void fromPivot(Quaternion rotation) {
		if (this.system != PIVOT_SYSTEM) {
			final Vector3D<?, ?> vector = rotation.getAxis();
			final double vx = vector.getX();
			double vy = vector.getY();
			double vz = vector.getZ();
			final double[] factors = fromSystemIndex(this.system);
			vy = vy * factors[0] + vz * factors[2];
			vz = vy * factors[1] + vz * factors[3];

			double angle = rotation.getAngle();

			if (isLeftHanded()) {
				angle = -angle;
			}

			rotation.setAxisAngle(vx, vy, vz, angle);
		}
	}

	private void fromPivot(Transform3D transformation) {
		if (this.system != PIVOT_SYSTEM) {
			// Translation
			final Vector3D<?, ?> vector = new InnerComputationVector3afp(
					transformation.getTranslationX(),
					transformation.getTranslationY(),
					transformation.getTranslationZ());
			fromPivot(vector);
			transformation.setTranslation(vector);
			// Rotation
			final Quaternion rotation = new Quaternion4d();
			transformation.getRotation(rotation);
			fromPivot(rotation);
			transformation.setRotation(rotation);
		}
	}

	/** Convert the specified point or vector from the default coordinate system.
	 *
	 * @param tuple is the point or vector to convert
	 */
	public void fromDefault(Tuple3D<?> tuple) {
		getDefaultCoordinateSystem().toSystem(tuple, this);
	}

	/** Convert the specified rotation from the default coordinate system.
	 *
	 * @param rotation is the rotation to convert
	 */
	public void fromDefault(Transform3D rotation) {
		getDefaultCoordinateSystem().toSystem(rotation, this);
	}

	/** Convert the specified rotation from the default coordinate system.
	 *
	 * @param rotation is the rotation to convert
	 */
	public void fromDefault(Quaternion rotation) {
		getDefaultCoordinateSystem().toSystem(rotation, this);
	}

	/** Replies the coordinate system which is corresponding to the specified
	 * orientation unit vectors.
	 *
	 * <p>The front vector is assumed to be always <code>(1,0,0)</code>,
	 * the left vector is <code>(0,ly,lz)</code>, and the top
	 * vector is <code>(0,ty,tz)</code>.
	 *
	 * @param ly y coordinate of the left vector.
	 * @param lz z coordinate of the left vector.
	 * @param ty y coordinate of the up vector.
	 * @param tz z coordinate of the up vector.
	 * @return the coordinate system which is defined by the specified vectors.
	 */
	@Pure
	public static CoordinateSystem3D fromVectors(int ly, int lz, int ty, int tz) {
		final byte system = toSystemIndex(ly, lz, ty, tz);
		for (final CoordinateSystem3D cs : CoordinateSystem3D.values()) {
			if (cs.system == system) {
				return cs;
			}
		}
		throw new CoordinateSystemNotFoundException();
	}

	/** Replies the coordinate system which is corresponding to the specified
	 * orientation unit vectors.
	 *
	 * <p>The front vector is assumed to be always <code>(1,0,0)</code>,
	 * the left vector is <code>(0,ly,lz)</code>, and the top
	 * vector is <code>(0,ty,tz)</code>.
	 *
	 * @param ly y coordinate of the left vector.
	 * @param lz z coordinate of the left vector.
	 * @param ty y coordinate of the up vector.
	 * @param tz z coordinate of the up vector.
	 * @return the coordinate system which is defined by the specified vectors.
	 */
	@Pure
	@Inline(value = "fromVectors((int) ($1), (int) ($2), (int) ($3), (int) ($4));")
	public static CoordinateSystem3D fromVectors(double ly, double lz, double ty, double tz) {
		return fromVectors((int) ly, (int) lz, (int) ty, (int) tz);
	}

	/** Replies the coordinate system which is corresponding to the specified
	 * orientation unit vectors.
	 *
	 * @param vx x coordinate of the view vector.
	 * @param vy y coordinate of the view vector.
	 * @param vz z coordinate of the view vector.
	 * @param lx x coordinate of the left vector.
	 * @param ly y coordinate of the left vector.
	 * @param lz z coordinate of the left vector.
	 * @param ux x coordinate of the up vector.
	 * @param uy y coordinate of the up vector.
	 * @param uz z coordinate of the up vector.
	 * @return the coordinate system which is defined by the specified vectors.
	 */
	@Pure
	@SuppressWarnings({"checkstyle:parameternumber"})
	public static CoordinateSystem3D fromVectors(int vx, int vy, int vz, int lx, int ly, int lz, int ux, int uy, int uz) {
		if (vx == 1 && vy == 0 && vz == 0) {
			assert lx == 0 && ux == 0;
			return fromVectors(ly, lz, uy, uz);
		}

		// Transform the given vectors to align V along (1,0,0)
		final Transform3D mat = new Transform3D(vx, vy, vz, 0, lx, ly, lz, 0, ux, uy, uz, 0);

		final Vector3D<?, ?> v1 = new InnerComputationVector3afp(vx, vy, vz);
		mat.transform(v1);
		normalizeVector(v1);

		final Vector3D<?, ?> v2 = new InnerComputationVector3afp(lx, ly, lz);
		mat.transform(v2);
		normalizeVector(v2);

		final Vector3D<?, ?> v3 = new InnerComputationVector3afp(ux, uy, uz);
		mat.transform(v3);
		normalizeVector(v3);

		return fromVectors(v2.iy(), v2.iz(), v3.iy(), v3.iz());
	}

	/** Replies the coordinate system which is corresponding to the specified
	 * orientation unit vectors.
	 *
	 * @param vx x coordinate of the view vector.
	 * @param vy y coordinate of the view vector.
	 * @param vz z coordinate of the view vector.
	 * @param lx x coordinate of the left vector.
	 * @param ly y coordinate of the left vector.
	 * @param lz z coordinate of the left vector.
	 * @param ux x coordinate of the up vector.
	 * @param uy y coordinate of the up vector.
	 * @param uz z coordinate of the up vector.
	 * @return the coordinate system which is defined by the specified vectors.
	 */
	@Pure
	@SuppressWarnings({"checkstyle:parameternumber"})
	public static CoordinateSystem3D fromVectors(double vx, double vy, double vz, double lx, double ly,
			double lz, double ux, double uy, double uz) {
		if (vx == 1. && vy == 0. && vz == 0.) {
			assert lx == 0. && ux == 0.;
			return fromVectors(ly, lz, uy, uz);
		}

		// Transform the given vectors to align V along (1,0,0)
		final Transform3D mat = new Transform3D(vx, vy, vz, 0, lx, ly, lz, 0, ux, uy, uz, 0);

		final Vector3D<?, ?> v1 = new InnerComputationVector3afp(vx, vy, vz);
		mat.transform(v1);
		normalizeVector(v1);

		final Vector3D<?, ?> v2 = new InnerComputationVector3afp(lx, ly, lz);
		mat.transform(v2);
		normalizeVector(v2);

		final Vector3D<?, ?> v3 = new InnerComputationVector3afp(ux, uy, uz);
		mat.transform(v3);
		normalizeVector(v3);

		return fromVectors(v2.getY(), v2.getZ(), v3.getY(), v3.getZ());
	}

	private static void normalizeVector(Vector3D<?, ?> v) {
		v.normalize();
		if (MathUtil.isEpsilonZero(Math.abs(v.getX() - 1.))) {
			v.setX(Math.signum(v.getX()));
			v.setY(0.);
			v.setZ(0.);
		} else if (MathUtil.isEpsilonZero(Math.abs(v.getY() - 1.))) {
			v.setY(Math.signum(v.getY()));
			v.setX(0.);
			v.setZ(0.);
		} else if (MathUtil.isEpsilonZero(Math.abs(v.getZ() - 1.))) {
			v.setZ(Math.signum(v.getZ()));
			v.setX(0.);
			v.setY(0.);
		}
	}

	/** Replies if the z coordinate is the up direction.
	 *
	 * @return <code>true</code> if z coordinate is up.
	 */
	@Pure
	public boolean isZOnUp() {
		return this == XYZ_LEFT_HAND || this == XYZ_RIGHT_HAND;
	}

	/** Replies if the y coordinate is the up direction.
	 *
	 * @return <code>true</code> if y coordinate is up.
	 */
	@Pure
	public boolean isYOnUp() {
		return this == XZY_LEFT_HAND || this == XZY_RIGHT_HAND;
	}

	/** Replies if the z coordinate is the side direction (left or right).
	 *
	 * @return <code>true</code> if z coordinate is side.
	 */
	@Pure
	public boolean isZOnSide() {
		return this == XZY_LEFT_HAND || this == XZY_RIGHT_HAND;
	}

	/** Replies if the y coordinate is the side direction (left or right).
	 *
	 * @return <code>true</code> if y coordinate is side.
	 */
	@Pure
	public boolean isYOnSide() {
		return this == XYZ_LEFT_HAND || this == XYZ_RIGHT_HAND;
	}

	/** Replies the view vector of this coordinate space.
	 *
	 * <p>When objects have not been rotated, they are supposed to
	 * have a front direction colinear to this view vector.
	 *
	 * @return the view vector (always normalized).
	 */
	@Pure
	public final Vector3D<?, ?> getViewVector() {
		return getViewVector(new InnerComputationVector3afp());
	}

	/** Replies the view vector of this coordinate space.
	 *
	 * <p>When objects have not been rotated, they are supposed to
	 * have a front direction colinear to this view vector.
	 *
	 * @param vectorToFill is the vector to fill with the view vector coordinates.
	 * @return <var>vectorToFill</var>.
	 */
	public final Vector3D<?, ?> getViewVector(Vector3D<?, ?> vectorToFill) {
		if (vectorToFill != null) {
			vectorToFill.set(1., 0., 0.);
			fromPivot(vectorToFill);
		}
		return vectorToFill;
	}

	/** Replies the back vector of this coordinate space.
	 *
	 * <p>When objects have not been rotated, they are supposed to
	 * have a back direction colinear to this back vector.
	 *
	 * @return the back vector (always normalized).
	 */
	@Pure
	public final Vector3D<?, ?> getBackVector() {
		return getBackVector(new InnerComputationVector3afp());
	}

	/** Replies the back vector of this coordinate space.
	 *
	 * <p>When objects have not been rotated, they are supposed to
	 * have a back direction colinear to this back vector.
	 *
	 * @param vectorToFill is the vector to fill with the back vector coordinates.
	 * @return <var>vectorToFill</var>.
	 */
	public final Vector3D<?, ?> getBackVector(Vector3D<?, ?> vectorToFill) {
		if (vectorToFill != null) {
			vectorToFill.set(-1., 0., 0.);
			fromPivot(vectorToFill);
		}
		return vectorToFill;
	}

	/** Replies the left vector of this coordinate space.
	 *
	 * <p>When objects have not been rotated, they are supposed to
	 * have a left direction colinear to this left vector.
	 *
	 * @return the left vector (always normalized).
	 */
	@Pure
	public final Vector3D<?, ?> getLeftVector() {
		return getLeftVector(new InnerComputationVector3afp());
	}

	/** Replies the left vector of this coordinate space.
	 *
	 * <p>When objects have not been rotated, they are supposed to
	 * have a left direction colinear to this left vector.
	 *
	 * @param vectorToFill is the vector to fill with the left vector coordinates.
	 * @return <var>vectorToFill</var>.
	 */
	public final Vector3D<?, ?> getLeftVector(Vector3D<?, ?> vectorToFill) {
		if (vectorToFill != null) {
			vectorToFill.set(0., 1., 0.);
			fromPivot(vectorToFill);
		}
		return vectorToFill;
	}

	/** Replies the right vector of this coordinate space.
	 *
	 * <p>When objects have not been rotated, they are supposed to
	 * have a right direction colinear to this right vector.
	 *
	 * @return the right vector (always normalized).
	 */
	@Pure
	public final Vector3D<?, ?> getRightVector() {
		return getRightVector(new InnerComputationVector3afp());
	}

	/** Replies the right vector of this coordinate space.
	 *
	 * <p>When objects have not been rotated, they are supposed to
	 * have a right direction colinear to this right vector.
	 *
	 * @param vectorToFill is the vector to fill with the right vector coordinates.
	 * @return <var>vectorToFill</var>.
	 */
	public final Vector3D<?, ?> getRightVector(Vector3D<?, ?> vectorToFill) {
		if (vectorToFill != null) {
			vectorToFill.set(0., -1., 0.);
			fromPivot(vectorToFill);
		}
		return vectorToFill;
	}

	/** Replies the up vector of this coordinate space.
	 *
	 * <p>When objects have not been rotated, they are supposed to
	 * have a up direction colinear to this up vector.
	 *
	 * @return the up vector (always normalized).
	 */
	@Pure
	public final Vector3D<?, ?> getUpVector() {
		return getUpVector(new InnerComputationVector3afp());
	}

	/** Replies the up vector of this coordinate space.
	 *
	 * <p>When objects have not been rotated, they are supposed to
	 * have a up direction colinear to this up vector.
	 *
	 * @param vectorToFill is the vector to fill with the up vector coordinates.
	 * @return <var>vectorToFill</var>.
	 */
	public final Vector3D<?, ?> getUpVector(Vector3D<?, ?> vectorToFill) {
		if (vectorToFill != null) {
			vectorToFill.set(0., 0., 1.);
			fromPivot(vectorToFill);
		}
		return vectorToFill;
	}

	/** Replies the down vector of this coordinate space.
	 *
	 * <p>When objects have not been rotated, they are supposed to
	 * have a down direction colinear to this down vector.
	 *
	 * @return the down vector (always normalized).
	 */
	@Pure
	public final Vector3D<?, ?> getDownVector() {
		return getDownVector(new InnerComputationVector3afp());
	}

	/** Replies the down vector of this coordinate space.
	 *
	 * <p>When objects have not been rotated, they are supposed to
	 * have a down direction colinear to this down vector.
	 *
	 * @param vectorToFill is the vector to fill with the down vector coordinates.
	 * @return <var>vectorToFill</var>.
	 */
	public final Vector3D<?, ?> getDownVector(Vector3D<?, ?> vectorToFill) {
		if (vectorToFill != null) {
			vectorToFill.set(0., 0., -1.);
			fromPivot(vectorToFill);
		}
		return vectorToFill;
	}

	/** Replies the vertical position from the given 3D point for this coordinate system.
	 *
	 * @param tuple the tuple.
	 * @return the vertical position in <var>x/<var>, <var>y</var> or </var>z</var>
	 */
	@Pure
	public final double height(Tuple3D<?> tuple) {
		return height(tuple.getX(), tuple.getY(), tuple.getZ());
	}

	/** Replies the vertical position from the given 3D point for this coordinate system.
	 *
	 * @param x x coordinate of a tuple.
	 * @param y y coordinate of a tuple.
	 * @param z z coordinate of a tuple.
	 * @return the vertical position in <var>x/<var>, <var>y</var> or </var>z</var>
	 */
	@Pure
	public double height(double x, double y, double z) {
		final double[] factors = fromSystemIndex(this.system);
		return factors[2] != 0. ? y : z;
	}

	/** Replies the horizontal left-right position from the given 3D point for this coordinate system.
	 *
	 * @param tuple the tuple
	 * @return the horizontal and left-right position in <var>x/<var>, <var>y</var> or </var>z</var>
	 * @since 4.0
	 */
	@Pure
	public final double side(Tuple3D<?> tuple) {
		return side(tuple.getX(), tuple.getY(), tuple.getZ());
	}

	/** Replies the horizontal left-right position from the given 3D point for this coordinate system.
	 *
	 * @param x x coordinate of a tuple.
	 * @param y y coordinate of a tuple.
	 * @param z z coordinate of a tuple.
	 * @return the horizontal and left-right position in <var>x/<var>, <var>y</var> or </var>z</var>
	 * @since 4.0
	 */
	@Pure
	public double side(double x, double y, double z) {
		final double[] factors = fromSystemIndex(this.system);
		return factors[2] != 0. ? z : y;
	}

	/** Replies the horizontal front-back position from the given 3D point for this coordinate system.
	 *
	 * @param tuple the tuple.
	 * @return the horizontal and front-back position in <var>x/<var>, <var>y</var> or </var>z</var>
	 * @since 4.0
	 */
	@Pure
	public static final double view(Tuple3D<?> tuple) {
		return view(tuple.getX(), tuple.getY(), tuple.getZ());
	}

	/** Replies the horizontal front-back position from the given 3D point for this coordinate system.
	 *
	 * @param x x coordinate of a tuple.
	 * @param y y coordinate of a tuple.
	 * @param z z coordinate of a tuple.
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
		final double[] factors = fromSystemIndex(this.system);
		return factors[2] != 0. ? 1 : 2;
	}

	/** Replies index of the coordinate which is corresponding to
	 * the side for the coordinate system.
	 *
	 * @return the index of the coordinate of the side.
	 * @since 4.0
	 */
	@Pure
	public int getSideCoordinateIndex() {
		final double[] factors = fromSystemIndex(this.system);
		return factors[2] != 0. ? 2 : 1;
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
	 * @param tuple the tuple.
	 * @param height is the height to put in the tuple.
	 */
	public final void setHeight(Tuple3D<?> tuple, double height) {
		final double[] factors = fromSystemIndex(this.system);
		if (factors[2] != 0.) {
			tuple.setY(height);
		} else {
			tuple.setZ(height);
		}
	}

	/** Add the vertical amount to the height field of the given 3D point
	 * for this coordinate system.
	 *
	 * @param tuple the tuple.
	 * @param additionalHeight is the height amount to add to the tuple.
	 */
	public final void addHeight(Tuple3D<?> tuple, double additionalHeight) {
		final double[] factors = fromSystemIndex(this.system);
		if (factors[2] != 0.) {
			tuple.setY(tuple.getY() + additionalHeight);
		} else {
			tuple.setZ(tuple.getZ() + additionalHeight);
		}
	}

	/** Set the left-right position in the given 3D point for this coordinate system.
	 *
	 * @param tuple the tuple
	 * @param side is the side amount to put in the tuple.
	 * @since 4.0
	 */
	public final void setSide(Tuple3D<?> tuple, double side) {
		final double[] factors = fromSystemIndex(this.system);
		if (factors[2] != 0.) {
			tuple.setZ(side);
		} else {
			tuple.setY(side);
		}
	}

	/** Add the left-right amount to the field of the given 3D point
	 * for this coordinate system.
	 *
	 * @param tuple the tuple.
	 * @param additionalAmount is the amount to add to the tuple.
	 * @since 4.0
	 */
	public final void addSide(Tuple3D<?> tuple, double additionalAmount) {
		final double[] factors = fromSystemIndex(this.system);
		if (factors[2] != 0.) {
			tuple.setZ(tuple.getZ() + additionalAmount);
		} else {
			tuple.setY(tuple.getY() + additionalAmount);
		}
	}

	/** Set the front-back position in the given 3D point for this coordinate system.
	 *
	 * @param tuple the tuple.
	 * @param amount is the amount to put in the tuple.
	 * @since 4.0
	 */
	public static final void setView(Tuple3D<?> tuple, double amount) {
		tuple.setX(amount);
	}

	/** Add the front-back amount to the field of the given 3D point
	 * for this coordinate system.
	 *
	 * @param tuple the tuple.
	 * @param additionalViewAmount is the amount to add to the tuple.
	 */
	public static final void addView(Tuple3D<?> tuple, double additionalViewAmount) {
		tuple.setX(tuple.getX() + additionalViewAmount);
	}

	/** Replies the 2D coordinate system which is corresponding to
	 * this 3D coordinate system.
	 *
	 * <p>Be careful because the <code>y</code> semantic could differ from
	 * the 3D primitive to the 2D primitive.
	 *
	 * @return the 2D coordinate system.
	 */
	@Pure
	public abstract CoordinateSystem2D toCoordinateSystem2D();

	/** Convert the specified point/vector into from the current coordinate system
	 * to the 2D coordinate system.
	 *
	 * @param tuple is the point/vector to convert
	 * @param result the 2D point.
	 */
	@Pure
	public abstract void toCoordinateSystem2D(Tuple3D<?> tuple, Tuple2D<?> result);

	/** Convert the specified transformation into from the current coordinate system
	 * to the 2D coordinate system.
	 *
	 * @param transformation is the transformation to convert
	 * @param result the 2D transformation
	 */
	@Pure
	public void toCoordinateSystem2D(Transform3D transformation, Transform2D result) {
		final double angle = toCoordinateSystem2DAngleFromTransformation(transformation);
		final Point3D<?, ?> pts = new InnerComputationPoint3afp();
		transformation.transform(pts);
		result.setIdentity();
		result.setRotation(angle);
		final Point2D<?, ?> p2 = new InnerComputationPoint2afp();
		toCoordinateSystem2D(pts, p2);
		result.setTranslation(p2);
	}

	/** Convert the specified rotation axis into from the current coordinate system
	 * to the 2D coordinate system.
	 *
	 * @param rotation is the rotation to convert
	 * @return the 2D rotation
	 */
	@Pure
	public double toCoordinateSystem2D(Quaternion rotation) {
		final Transform3D trans = new Transform3D();
		trans.setRotation(rotation);
		return toCoordinateSystem2DAngleFromTransformation(trans);
	}

	/** Extract the 2D rotation angle from a 3D transformation matrix.
	 *
	 * @param mat the matrix.
	 * @return the 2D angle.
	 */
	protected abstract double toCoordinateSystem2DAngleFromTransformation(Transform3D mat);

}
