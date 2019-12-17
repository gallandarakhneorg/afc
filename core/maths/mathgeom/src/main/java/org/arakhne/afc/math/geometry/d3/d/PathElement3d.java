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

package org.arakhne.afc.math.geometry.d3.d;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.MathUtil;
import org.arakhne.afc.math.geometry.PathElementType;
import org.arakhne.afc.math.geometry.d3.afp.PathElement3afp;
import org.arakhne.afc.vmutil.ReflectionUtil;
import org.arakhne.afc.vmutil.asserts.AssertMessages;

/** An element of the path with 2 double precision floating-point numbers.
 *
 * @author $Author: sgalland$
 * @author $Author: tpiotrow$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
@SuppressWarnings("checkstyle:magicnumber")
public abstract class PathElement3d implements PathElement3afp {

	private static final long serialVersionUID = -9217295344283468162L;

	/** Type of the element.
	 */
	protected final PathElementType type;

	/** Target point.
	 */
	protected final double toX;

	/** Target point.
	 */
	protected final double toY;

	/** Target point.
	 */
	protected final double toZ;

	/** Constructor.
	 * @param type is the type of the element.
	 * @param tox the x coordinate of the target point.
	 * @param toy the y coordinate of the target point.
	 * @param toz the z coordinate of the target point.
	 */
	PathElement3d(PathElementType type, double tox, double toy, double toz) {
		assert type != null : AssertMessages.notNullParameter(0);
		this.type = type;
		this.toX = tox;
		this.toY = toy;
		this.toZ = toz;
	}

	@Pure
	@Override
	public String toString() {
        return ReflectionUtil.toString(this);
	}

	@Pure
	@Override
	public abstract boolean equals(Object obj);

	@Pure
	@Override
	public abstract int hashCode();

	@Override
	@Pure
	public final double getToX() {
		return this.toX;
	}

	@Override
	@Pure
	public final double getToY() {
		return this.toY;
	}

	@Override
	@Pure
	public final double getToZ() {
		return this.toZ;
	}

	@Pure
	@Override
	public final PathElementType getType() {
		return this.type;
	}


	/** Copy the coords into an array, except the source point.
	 *
	 * @return the array of the points, except the source point.
	 */
	@Pure
	public abstract double[] toArray();

	/** An element of the path that represents a <code>MOVE_TO</code>.
	 *
	 * @author $Author: sgalland$
	 * @author $Author: tpiotrow$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 13.0
	 */
	static class MovePathElement3d extends PathElement3d {

		private static final long serialVersionUID = -399575136145167775L;

		/** Constructor.
         * @param tox x coordinate of the target point.
         * @param toy y coordinate of the target point.
         * @param toz z coordinate of the target point.
         */
		MovePathElement3d(double tox, double toy, double toz) {
			super(PathElementType.MOVE_TO, tox, toy, toz);
		}

		@Pure
		@Override
		public boolean equals(Object obj) {
			try {
				final PathElement3afp elt = (PathElement3afp) obj;
				return getType() == elt.getType()
						&& getToX() == elt.getToX()
						&& getToY() == elt.getToY()
						&& getToZ() == elt.getToZ();
			} catch (Throwable exception) {
				//
			}
			return false;
		}

		@Pure
		@Override
		public int hashCode() {
			long bits = 1L;
			bits = 31L * bits + this.type.ordinal();
			bits = 31L * bits + Double.doubleToLongBits(getToX());
			bits = 31L * bits + Double.doubleToLongBits(getToY());
			bits = 31L * bits + Double.doubleToLongBits(getToZ());
			return (int) (bits ^ (bits >> 32));
		}

		@Pure
		@Override
		public boolean isEmpty() {
			return true;
		}

		@Pure
		@Override
		public boolean isDrawable() {
			return false;
		}

		@Override
		public void toArray(int[] array) {
			assert array != null : AssertMessages.notNullParameter();
			assert array.length >= 3 : AssertMessages.tooSmallArrayParameter(array.length, 3);
			array[0] = (int) this.toX;
			array[1] = (int) this.toY;
			array[2] = (int) this.toZ;
		}

		@Override
		public void toArray(double[] array) {
			assert array != null : AssertMessages.notNullParameter();
			assert array.length >= 3 : AssertMessages.tooSmallArrayParameter(array.length, 3);
			array[0] = this.toX;
			array[1] = this.toY;
			array[2] = this.toZ;
		}

		@Pure
		@Override
		public double[] toArray() {
			return new double[] {this.toX, this.toY, this.toZ};
		}

		@Override
		public double getFromX() {
			return 0.;
		}

		@Override
		public double getFromY() {
			return 0.;
		}

		@Override
		public double getFromZ() {
			return 0.;
		}

		@Override
		public double getCtrlX1() {
			return 0.;
		}

		@Override
		public double getCtrlY1() {
			return 0.;
		}

		@Override
		public double getCtrlZ1() {
			return 0.;
		}

		@Override
		public double getCtrlX2() {
			return 0.;
		}

		@Override
		public double getCtrlY2() {
			return 0.;
		}

		@Override
		public double getCtrlZ2() {
			return 0.;
		}

	}

	/** An element of the path that represents a <code>LINE_TO</code>.
	 *
	 * @author $Author: sgalland$
	 * @author $Author: tpiotrow$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 13.0
	 */
	static class LinePathElement3d extends PathElement3d {

		private static final long serialVersionUID = 8423845888008307447L;

		private final double fromX;

		private final double fromY;

		private final double fromZ;

		private Boolean isEmpty;

		/** Constructor.
         * @param fromx x coordinate of the origin point.
         * @param fromy y coordinate of the origin point.
         * @param fromz z coordinate of the origin point.
         * @param tox x coordinate of the target point.
         * @param toy y coordinate of the target point.
         * @param toz z coordinate of the target point.
         */
		LinePathElement3d(double fromx, double fromy, double fromz, double tox, double toy, double toz) {
			super(PathElementType.LINE_TO, tox, toy, toz);
			this.fromX = fromx;
			this.fromY = fromy;
			this.fromZ = fromz;
		}

		@Pure
		@Override
		public boolean equals(Object obj) {
			try {
				final PathElement3afp elt = (PathElement3afp) obj;
				return getType() == elt.getType()
						&& getToX() == elt.getToX()
						&& getToY() == elt.getToY()
						&& getToZ() == elt.getToZ()
						&& getFromX() == elt.getFromX()
						&& getFromY() == elt.getFromY()
						&& getFromZ() == elt.getFromZ();
			} catch (Throwable exception) {
				//
			}
			return false;
		}

		@Pure
		@Override
		public int hashCode() {
			long bits = 1L;
			bits = 31L * bits + this.type.ordinal();
			bits = 31L * bits + Double.doubleToLongBits(getToX());
			bits = 31L * bits + Double.doubleToLongBits(getToY());
			bits = 31L * bits + Double.doubleToLongBits(getToZ());
			bits = 31L * bits + Double.doubleToLongBits(getFromX());
			bits = 31L * bits + Double.doubleToLongBits(getFromY());
			bits = 31L * bits + Double.doubleToLongBits(getFromZ());
			return (int) (bits ^ (bits >> 32));
		}

		@Pure
		@Override
		public boolean isEmpty() {
			if (this.isEmpty == null) {
				this.isEmpty = MathUtil.isEpsilonEqual(this.fromX, this.toX)
						&& MathUtil.isEpsilonEqual(this.fromY, this.toY)
						&& MathUtil.isEpsilonEqual(this.fromZ, this.toZ);
			}
			return this.isEmpty.booleanValue();
		}

		@Pure
		@Override
		public boolean isDrawable() {
			return !isEmpty();
		}

		@Override
		public void toArray(int[] array) {
			assert array != null : AssertMessages.notNullParameter();
			assert array.length >= 3 : AssertMessages.tooSmallArrayParameter(array.length, 3);
			array[0] = (int) this.toX;
			array[1] = (int) this.toY;
			array[2] = (int) this.toZ;
		}

		@Override
		public void toArray(double[] array) {
			assert array != null : AssertMessages.notNullParameter();
			assert array.length >= 3 : AssertMessages.tooSmallArrayParameter(array.length, 3);
			array[0] = this.toX;
			array[1] = this.toY;
			array[2] = this.toY;
		}

		@Pure
		@Override
		public double[] toArray() {
			return new double[] {this.toX, this.toY, this.toZ};
		}

		@Override
		public double getFromX() {
			return this.fromX;
		}

		@Override
		public double getFromY() {
			return this.fromY;
		}

		@Override
		public double getFromZ() {
			return this.fromZ;
		}

		@Override
		public double getCtrlX1() {
			return 0.;
		}

		@Override
		public double getCtrlY1() {
			return 0.;
		}

		@Override
		public double getCtrlZ1() {
			return 0.;
		}

		@Override
		public double getCtrlX2() {
			return 0.;
		}

		@Override
		public double getCtrlY2() {
			return 0.;
		}

		@Override
		public double getCtrlZ2() {
			return 0.;
		}

	}

	/** An element of the path that represents a <code>QUAD_TO</code>.
	 *
	 * @author $Author: sgalland$
	 * @author $Author: tpiotrow$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 13.0
	 */
	static class QuadPathElement3d extends PathElement3d {

		private static final long serialVersionUID = 6092391345111872481L;

		private final double fromX;

		private final double fromY;

		private final double fromZ;

		private final double ctrlX;

		private final double ctrlY;

		private final double ctrlZ;

		private Boolean isEmpty;

	    /** Constructor.
         * @param fromx x coordinate of the origin point.
         * @param fromy y coordinate of the origin point.
         * @param fromz z coordinate of the origin point.
         * @param ctrlx x coordinate of the control point.
         * @param ctrly y coordinate of the control point.
         * @param ctrlz z coordinate of the control point.
         * @param tox x coordinate of the target point.
         * @param toy y coordinate of the target point.
         * @param toz z coordinate of the target point.
         */
	    QuadPathElement3d(double fromx, double fromy, double fromz, double ctrlx, double ctrly, double ctrlz, double tox,
                double toy, double toz) {
			super(PathElementType.QUAD_TO, tox, toy, toz);
			this.fromX = fromx;
			this.fromY = fromy;
			this.fromZ = fromz;
			this.ctrlX = ctrlx;
			this.ctrlY = ctrly;
			this.ctrlZ = ctrlz;
		}

		@Pure
		@Override
		public boolean equals(Object obj) {
			try {
				final PathElement3afp elt = (PathElement3afp) obj;
				return getType() == elt.getType()
						&& getToX() == elt.getToX()
						&& getToY() == elt.getToY()
						&& getToZ() == elt.getToZ()
						&& getCtrlX1() == elt.getCtrlX1()
						&& getCtrlY1() == elt.getCtrlY1()
						&& getCtrlZ1() == elt.getCtrlZ1()
						&& getFromX() == elt.getFromX()
						&& getFromY() == elt.getFromY()
						&& getFromZ() == elt.getFromZ();
			} catch (Throwable exception) {
				//
			}
			return false;
		}

		@Pure
		@Override
		public int hashCode() {
			long bits = 1L;
			bits = 31L * bits + this.type.ordinal();
			bits = 31L * bits + Double.doubleToLongBits(getToX());
			bits = 31L * bits + Double.doubleToLongBits(getToY());
			bits = 31L * bits + Double.doubleToLongBits(getToZ());
			bits = 31L * bits + Double.doubleToLongBits(getCtrlX1());
			bits = 31L * bits + Double.doubleToLongBits(getCtrlY1());
			bits = 31L * bits + Double.doubleToLongBits(getCtrlZ1());
			bits = 31L * bits + Double.doubleToLongBits(getFromX());
			bits = 31L * bits + Double.doubleToLongBits(getFromY());
			bits = 31L * bits + Double.doubleToLongBits(getFromZ());
			return (int) (bits ^ (bits >> 32));
		}

		@Pure
		@Override
		public boolean isEmpty() {
			if (this.isEmpty == null) {
				this.isEmpty = MathUtil.isEpsilonEqual(this.fromX, this.toX)
						&& MathUtil.isEpsilonEqual(this.fromY, this.toY)
						&& MathUtil.isEpsilonEqual(this.fromZ, this.toZ)
						&& MathUtil.isEpsilonEqual(this.ctrlX, this.toX)
						&& MathUtil.isEpsilonEqual(this.ctrlY, this.toY)
						&& MathUtil.isEpsilonEqual(this.ctrlZ, this.toZ);
			}
			return this.isEmpty.booleanValue();
		}

		@Pure
		@Override
		public boolean isDrawable() {
			return !isEmpty();
		}

		@Override
		public void toArray(int[] array) {
			assert array != null : AssertMessages.notNullParameter();
			assert array.length >= 6 : AssertMessages.tooSmallArrayParameter(array.length, 6);
			array[0] = (int) this.ctrlX;
			array[1] = (int) this.ctrlY;
			array[2] = (int) this.ctrlZ;
			array[3] = (int) this.toX;
			array[4] = (int) this.toY;
			array[5] = (int) this.toZ;
		}

		@Override
		public void toArray(double[] array) {
			assert array != null : AssertMessages.notNullParameter();
			assert array.length >= 6 : AssertMessages.tooSmallArrayParameter(array.length, 6);
			array[0] = this.ctrlX;
			array[1] = this.ctrlY;
			array[2] = this.ctrlZ;
			array[3] = this.toX;
			array[4] = this.toY;
			array[5] = this.toZ;
		}

		@Pure
		@Override
		public double[] toArray() {
			return new double[] {this.ctrlX, this.ctrlY, this.ctrlZ, this.toX, this.toY, this.toZ};
		}

		@Override
		public double getFromX() {
			return this.fromX;
		}

		@Override
		public double getFromY() {
			return this.fromY;
		}

		@Override
		public double getFromZ() {
			return this.fromZ;
		}

		@Override
		public double getCtrlX1() {
			return this.ctrlX;
		}

		@Override
		public double getCtrlY1() {
			return this.ctrlY;
		}

		@Override
		public double getCtrlZ1() {
			return this.ctrlZ;
		}

		@Override
		public double getCtrlX2() {
			return 0.;
		}

		@Override
		public double getCtrlY2() {
			return 0.;
		}

		@Override
		public double getCtrlZ2() {
			return 0.;
		}

	}

	/** An element of the path that represents a <code>CURVE_TO</code>.
	 *
	 * @author $Author: sgalland$
	 * @author $Author: tpiotrow$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 13.0
	 */
	static class CurvePathElement3d extends PathElement3d {

		private static final long serialVersionUID = -2831895270995173092L;

		private final double fromX;

		private final double fromY;

		private final double fromZ;

		private final double ctrlX1;

		private final double ctrlY1;

		private final double ctrlZ1;

		private final double ctrlX2;

		private final double ctrlY2;

		private final double ctrlZ2;

		private Boolean isEmpty;

	    /** Constructor.
         * @param fromx x coordinate of the origin point.
         * @param fromy y coordinate of the origin point.
         * @param fromz z coordinate of the origin point.
         * @param ctrlx1 x coordinate of the first control point.
         * @param ctrly1 y coordinate of the first control point.
         * @param ctrlz1 z coordinate of the first control point.
         * @param ctrlx2 x coordinate of the second control point.
         * @param ctrly2 y coordinate of the second control point.
         * @param ctrlz2 z coordinate of the second control point.
         * @param tox x coordinate of the target point.
         * @param toy y coordinate of the target point.
         * @param toz z coordinate of the target point.
         */
	    @SuppressWarnings("checkstyle:parameternumber")
	    CurvePathElement3d(double fromx, double fromy, double fromz, double ctrlx1, double ctrly1, double ctrlz1, double ctrlx2,
                double ctrly2, double ctrlz2, double tox, double toy, double toz) {
			super(PathElementType.CURVE_TO, tox, toy, toz);
			this.fromX = fromx;
			this.fromY = fromy;
			this.fromZ = fromz;
			this.ctrlX1 = ctrlx1;
			this.ctrlY1 = ctrly1;
			this.ctrlZ1 = ctrlz1;
			this.ctrlX2 = ctrlx2;
			this.ctrlY2 = ctrly2;
			this.ctrlZ2 = ctrlz2;
		}

		@Pure
		@Override
		public boolean equals(Object obj) {
			try {
				final PathElement3afp elt = (PathElement3afp) obj;
				return getType() == elt.getType()
						&& getToX() == elt.getToX()
						&& getToY() == elt.getToY()
						&& getToZ() == elt.getToZ()
						&& getCtrlX1() == elt.getCtrlX1()
						&& getCtrlY1() == elt.getCtrlY1()
						&& getCtrlZ1() == elt.getCtrlZ1()
						&& getCtrlX2() == elt.getCtrlX2()
						&& getCtrlY2() == elt.getCtrlY2()
						&& getCtrlZ2() == elt.getCtrlZ2()
						&& getFromX() == elt.getFromX()
						&& getFromY() == elt.getFromY()
						&& getFromZ() == elt.getFromZ();
			} catch (Throwable exception) {
				//
			}
			return false;
		}

		@Pure
		@Override
		public int hashCode() {
			long bits = 1L;
			bits = 31L * bits + this.type.ordinal();
			bits = 31L * bits + Double.doubleToLongBits(getToX());
			bits = 31L * bits + Double.doubleToLongBits(getToY());
			bits = 31L * bits + Double.doubleToLongBits(getToZ());
			bits = 31L * bits + Double.doubleToLongBits(getCtrlX1());
			bits = 31L * bits + Double.doubleToLongBits(getCtrlY1());
			bits = 31L * bits + Double.doubleToLongBits(getCtrlZ1());
			bits = 31L * bits + Double.doubleToLongBits(getCtrlX2());
			bits = 31L * bits + Double.doubleToLongBits(getCtrlY2());
			bits = 31L * bits + Double.doubleToLongBits(getCtrlZ2());
			bits = 31L * bits + Double.doubleToLongBits(getFromX());
			bits = 31L * bits + Double.doubleToLongBits(getFromY());
			bits = 31L * bits + Double.doubleToLongBits(getFromZ());
			return (int) (bits ^ (bits >> 32));
		}

		@Pure
		@Override
		@SuppressWarnings("checkstyle:booleanexpressioncomplexity")
		public boolean isEmpty() {
			if (this.isEmpty == null) {
				this.isEmpty = MathUtil.isEpsilonEqual(this.fromX, this.toX)
						&& MathUtil.isEpsilonEqual(this.fromY, this.toY)
						&& MathUtil.isEpsilonEqual(this.fromZ, this.toZ)
						&& MathUtil.isEpsilonEqual(this.ctrlX1, this.toX)
						&& MathUtil.isEpsilonEqual(this.ctrlY1, this.toY)
						&& MathUtil.isEpsilonEqual(this.ctrlZ1, this.toZ)
						&& MathUtil.isEpsilonEqual(this.ctrlX2, this.toX)
						&& MathUtil.isEpsilonEqual(this.ctrlY2, this.toY)
						&& MathUtil.isEpsilonEqual(this.ctrlZ2, this.toZ);
			}
			return this.isEmpty.booleanValue();
		}

		@Pure
		@Override
		public boolean isDrawable() {
			return !isEmpty();
		}

		@Override
		public void toArray(int[] array) {
			assert array != null : AssertMessages.notNullParameter();
			assert array.length >= 9 : AssertMessages.tooSmallArrayParameter(array.length, 9);
			array[0] = (int) this.ctrlX1;
			array[1] = (int) this.ctrlY1;
			array[2] = (int) this.ctrlZ1;
			array[3] = (int) this.ctrlX2;
			array[4] = (int) this.ctrlY2;
			array[5] = (int) this.ctrlZ2;
			array[6] = (int) this.toX;
			array[7] = (int) this.toY;
			array[8] = (int) this.toZ;
		}

		@Override
		public void toArray(double[] array) {
			assert array != null : AssertMessages.notNullParameter();
			assert array.length >= 9 : AssertMessages.tooSmallArrayParameter(array.length, 9);
			array[0] = this.ctrlX1;
			array[1] = this.ctrlY1;
			array[2] = this.ctrlZ1;
			array[3] = this.ctrlX2;
			array[4] = this.ctrlY2;
			array[5] = this.ctrlZ2;
			array[6] = this.toX;
			array[7] = this.toY;
			array[8] = this.toZ;
		}

		@Pure
		@Override
		@SuppressWarnings("checkstyle:arraytrailingcomma")
		public double[] toArray() {
            return new double[] {this.ctrlX1, this.ctrlY1, this.ctrlZ1, this.ctrlX2, this.ctrlY2, this.ctrlZ2, this.toX,
                                 this.toY, this.toZ};
		}

		@Override
		public double getFromX() {
			return this.fromX;
		}

		@Override
		public double getFromY() {
			return this.fromY;
		}

		@Override
		public double getFromZ() {
			return this.fromZ;
		}

		@Override
		public double getCtrlX1() {
			return this.ctrlX1;
		}

		@Override
		public double getCtrlY1() {
			return this.ctrlY1;
		}

		@Override
		public double getCtrlZ1() {
			return this.ctrlZ1;
		}

		@Override
		public double getCtrlX2() {
			return this.ctrlX2;
		}

		@Override
		public double getCtrlY2() {
			return this.ctrlY2;
		}

		@Override
		public double getCtrlZ2() {
			return this.ctrlZ2;
		}

	}

	/** An element of the path that represents a <code>CLOSE</code>.
	 *
	 * @author $Author: sgalland$
	 * @author $Author: tpiotrow$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 13.0
	 */
	static class ClosePathElement3d extends PathElement3d {

		private static final long serialVersionUID = 5324688417590599323L;

		private final double fromX;

		private final double fromY;

		private final double fromZ;

		private Boolean isEmpty;

		/** Constructor.
         * @param fromx x coordinate of the origin point.
         * @param fromy y coordinate of the origin point.
         * @param fromz z coordinate of the origin point.
         * @param tox x coordinate of the target point.
         * @param toy y coordinate of the target point.
         * @param toz z coordinate of the target point.
         */
		ClosePathElement3d(double fromx, double fromy, double fromz, double tox, double toy, double toz) {
			super(PathElementType.CLOSE, tox, toy, toz);
			this.fromX = fromx;
			this.fromY = fromy;
			this.fromZ = fromz;
		}

		@Pure
		@Override
		public boolean equals(Object obj) {
			try {
				final PathElement3afp elt = (PathElement3afp) obj;
				return getType() == elt.getType()
						&& getToX() == elt.getToX()
						&& getToY() == elt.getToY()
						&& getToZ() == elt.getToZ()
						&& getFromX() == elt.getFromX()
						&& getFromY() == elt.getFromY()
						&& getFromZ() == elt.getFromZ();
			} catch (Throwable exception) {
				//
			}
			return false;
		}

		@Pure
		@Override
		public int hashCode() {
			long bits = 1L;
			bits = 31L * bits + this.type.ordinal();
			bits = 31L * bits + Double.doubleToLongBits(getToX());
			bits = 31L * bits + Double.doubleToLongBits(getToY());
			bits = 31L * bits + Double.doubleToLongBits(getToZ());
			bits = 31L * bits + Double.doubleToLongBits(getFromX());
			bits = 31L * bits + Double.doubleToLongBits(getFromY());
			bits = 31L * bits + Double.doubleToLongBits(getFromZ());
			return (int) (bits ^ (bits >> 32));
		}

		@Pure
		@Override
		public boolean isEmpty() {
			if (this.isEmpty == null) {
				this.isEmpty = MathUtil.isEpsilonEqual(this.fromX, this.toX)
						&& MathUtil.isEpsilonEqual(this.fromY, this.toY)
						&& MathUtil.isEpsilonEqual(this.fromZ, this.toZ);
			}
			return this.isEmpty.booleanValue();
		}

		@Pure
		@Override
		public boolean isDrawable() {
			return !isEmpty();
		}

		@Override
		public void toArray(int[] array) {
			assert array != null : AssertMessages.notNullParameter();
			assert array.length >= 3 : AssertMessages.tooSmallArrayParameter(array.length, 3);
			array[0] = (int) this.toX;
			array[1] = (int) this.toY;
			array[2] = (int) this.toZ;
		}

		@Override
		public void toArray(double[] array) {
			assert array != null : AssertMessages.notNullParameter();
            assert array.length >= 3 : AssertMessages.tooSmallArrayParameter(array.length, 3);
			array[0] = this.toX;
			array[1] = this.toY;
			array[2] = this.toZ;
		}

		@Pure
		@Override
		public double[] toArray() {
			return new double[] {this.toX, this.toY, this.toZ};
		}

		@Override
		public double getFromX() {
			return this.fromX;
		}

		@Override
		public double getFromY() {
			return this.fromY;
		}

		@Override
		public double getFromZ() {
			return this.fromZ;
		}

		@Override
		public double getCtrlX1() {
			return 0.;
		}

		@Override
		public double getCtrlY1() {
			return 0.;
		}

		@Override
		public double getCtrlZ1() {
			return 0.;
		}

		@Override
		public double getCtrlX2() {
			return 0.;
		}

		@Override
		public double getCtrlY2() {
			return 0.;
		}

		@Override
		public double getCtrlZ2() {
			return 0.;
		}

	}

}
