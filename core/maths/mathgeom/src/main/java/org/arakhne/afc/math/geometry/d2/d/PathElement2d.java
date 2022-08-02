/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2022 The original authors, and other authors.
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

package org.arakhne.afc.math.geometry.d2.d;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.MathUtil;
import org.arakhne.afc.math.geometry.PathElementType;
import org.arakhne.afc.math.geometry.d2.afp.PathElement2afp;
import org.arakhne.afc.vmutil.ReflectionUtil;
import org.arakhne.afc.vmutil.asserts.AssertMessages;

/** An element of the path with 2 double precision floating-point numbers.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public abstract class PathElement2d implements PathElement2afp {

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

	/** Constructor.
	 * @param type is the type of the element.
	 * @param tox the x coordinate of the target point.
	 * @param toy the x coordinate of the target point.
	 */
	PathElement2d(PathElementType type, double tox, double toy) {
		assert type != null : AssertMessages.notNullParameter(0);
		this.type = type;
		this.toX = tox;
		this.toY = toy;
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

	@Pure
	@Override
	public final PathElementType getType() {
		return this.type;
	}

	@Override
	public double getCtrlX1() {
		return 0;
	}

	@Override
	public double getCtrlY1() {
		return 0;
	}

	@Override
	public double getCtrlX2() {
		return 0;
	}

	@Override
	public double getCtrlY2() {
		return 0;
	}

	@Override
	public double getRadiusX() {
		return 0;
	}

	@Override
	public double getRadiusY() {
		return 0;
	}

	@Override
	public double getRotationX() {
		return 0;
	}

	@Override
	public boolean getSweepFlag() {
		return false;
	}

	@Override
	public boolean getLargeArcFlag() {
		return false;
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
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 13.0
	 */
	static class MovePathElement2d extends PathElement2d {

		private static final long serialVersionUID = -399575136145167775L;

		/** Constructor.
		 * @param tox x coordinate of the target point.
		 * @param toy y coordinate of the target point.
		 */
		MovePathElement2d(double tox, double toy) {
			super(PathElementType.MOVE_TO, tox, toy);
		}

		@Pure
		@Override
		public boolean equals(Object obj) {
			if (obj == this) {
				return true;
			}
			if (getClass().isInstance(obj)) {
				final PathElement2afp elt = (PathElement2afp) obj;
				return getType() == elt.getType()
						&& getToX() == elt.getToX()
						&& getToY() == elt.getToY();
			}
			return false;
		}

		@Pure
		@Override
		public int hashCode() {
			long bits = 1L;
			bits = 31L * bits + this.type.hashCode();
			bits = 31L * bits + Double.hashCode(getToX());
			bits = 31L * bits + Double.hashCode(getToY());
			return (int) (bits ^ (bits >> 31));
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
			assert array.length >= 2 : AssertMessages.tooSmallArrayParameter(array.length, 2);
			array[0] = (int) this.toX;
			array[1] = (int) this.toY;
		}

		@Override
		public void toArray(double[] array) {
			assert array != null : AssertMessages.notNullParameter();
			assert array.length >= 2 : AssertMessages.tooSmallArrayParameter(array.length, 2);
			array[0] = this.toX;
			array[1] = this.toY;
		}

		@Pure
		@Override
		public double[] toArray() {
			return new double[] {this.toX, this.toY};
		}

		@Override
		public double getFromX() {
			return 0.;
		}

		@Override
		public double getFromY() {
			return 0.;
		}

	}

	/** An element of the path that represents a <code>LINE_TO</code>.
	 *
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 13.0
	 */
	static class LinePathElement2d extends PathElement2d {

		private static final long serialVersionUID = 8423845888008307447L;

		private final double fromX;

		private final double fromY;

		private Boolean isEmpty;

		/** Constructor.
		 * @param fromx x coordinate of the origin point.
		 * @param fromy y coordinate of the origin point.
		 * @param tox x coordinate of the target point.
		 * @param toy y coordinate of the target point.
		 */
		LinePathElement2d(double fromx, double fromy, double tox, double toy) {
			super(PathElementType.LINE_TO, tox, toy);
			this.fromX = fromx;
			this.fromY = fromy;
		}

		@Pure
		@Override
		public boolean equals(Object obj) {
			if (obj == this) {
				return true;
			}
			if (getClass().isInstance(obj)) {
				final PathElement2afp elt = (PathElement2afp) obj;
				return getType() == elt.getType()
						&& getToX() == elt.getToX()
						&& getToY() == elt.getToY()
						&& getFromX() == elt.getFromX()
						&& getFromY() == elt.getFromY();
			}
			return false;
		}

		@Pure
		@Override
		public int hashCode() {
			long bits = 1L;
			bits = 31L * bits + this.type.hashCode();
			bits = 31L * bits + Double.hashCode(getToX());
			bits = 31L * bits + Double.hashCode(getToY());
			bits = 31L * bits + Double.hashCode(getFromX());
			bits = 31L * bits + Double.hashCode(getFromY());
			return (int) (bits ^ (bits >> 31));
		}

		@Pure
		@Override
		public boolean isEmpty() {
			if (this.isEmpty == null) {
				this.isEmpty = MathUtil.isEpsilonEqual(this.fromX, this.toX)
						&& MathUtil.isEpsilonEqual(this.fromY, this.toY);
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
			assert array.length >= 2 : AssertMessages.tooSmallArrayParameter(array.length, 2);
			array[0] = (int) this.toX;
			array[1] = (int) this.toY;
		}

		@Override
		public void toArray(double[] array) {
			assert array != null : AssertMessages.notNullParameter();
			assert array.length >= 2 : AssertMessages.tooSmallArrayParameter(array.length, 2);
			array[0] = this.toX;
			array[1] = this.toY;
		}

		@Pure
		@Override
		public double[] toArray() {
			return new double[] {this.toX, this.toY};
		}

		@Override
		public double getFromX() {
			return this.fromX;
		}

		@Override
		public double getFromY() {
			return this.fromY;
		}

	}

	/** An element of the path that represents a <code>QUAD_TO</code>.
	 *
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 13.0
	 */
	@SuppressWarnings("checkstyle:magicnumber")
	static class QuadPathElement2d extends PathElement2d {

		private static final long serialVersionUID = 6092391345111872481L;

		private final double fromX;

		private final double fromY;

		private final double ctrlX;

		private final double ctrlY;

		private Boolean isEmpty;

		/** Constructor.
		 * @param fromx x coordinate of the origin point.
		 * @param fromy y coordinate of the origin point.
		 * @param ctrlx x coordinate of the control point.
		 * @param ctrly y coordinate of the control point.
		 * @param tox x coordinate of the target point.
		 * @param toy y coordinate of the target point.
		 */
		QuadPathElement2d(double fromx, double fromy, double ctrlx, double ctrly, double tox, double toy) {
			super(PathElementType.QUAD_TO, tox, toy);
			this.fromX = fromx;
			this.fromY = fromy;
			this.ctrlX = ctrlx;
			this.ctrlY = ctrly;
		}

		@Pure
		@Override
		public boolean equals(Object obj) {
			if (obj == this) {
				return true;
			}
			if (getClass().isInstance(obj)) {
				final PathElement2afp elt = (PathElement2afp) obj;
				return getType() == elt.getType()
						&& getToX() == elt.getToX()
						&& getToY() == elt.getToY()
						&& getCtrlX1() == elt.getCtrlX1()
						&& getCtrlY1() == elt.getCtrlY1()
						&& getFromX() == elt.getFromX()
						&& getFromY() == elt.getFromY();
			}
			return false;
		}

		@Pure
		@Override
		public int hashCode() {
			long bits = 1L;
			bits = 31L * bits + this.type.hashCode();
			bits = 31L * bits + Double.hashCode(getToX());
			bits = 31L * bits + Double.hashCode(getToY());
			bits = 31L * bits + Double.hashCode(getCtrlX1());
			bits = 31L * bits + Double.hashCode(getCtrlY1());
			bits = 31L * bits + Double.hashCode(getFromX());
			bits = 31L * bits + Double.hashCode(getFromY());
			return (int) (bits ^ (bits >> 31));
		}

		@Pure
		@Override
		public boolean isEmpty() {
			if (this.isEmpty == null) {
				this.isEmpty = MathUtil.isEpsilonEqual(this.fromX, this.toX)
						&& MathUtil.isEpsilonEqual(this.fromY, this.toY)
						&& MathUtil.isEpsilonEqual(this.ctrlX, this.toX)
						&& MathUtil.isEpsilonEqual(this.ctrlY, this.toY);
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
			assert array.length >= 4 : AssertMessages.tooSmallArrayParameter(array.length, 4);
			array[0] = (int) this.ctrlX;
			array[1] = (int) this.ctrlY;
			array[2] = (int) this.toX;
			array[3] = (int) this.toY;
		}

		@Override
		public void toArray(double[] array) {
			assert array != null : AssertMessages.notNullParameter();
			assert array.length >= 4 : AssertMessages.tooSmallArrayParameter(array.length, 4);
			array[0] = this.ctrlX;
			array[1] = this.ctrlY;
			array[2] = this.toX;
			array[3] = this.toY;
		}

		@Pure
		@Override
		public double[] toArray() {
			return new double[] {this.ctrlX, this.ctrlY, this.toX, this.toY};
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
		public double getCtrlX1() {
			return this.ctrlX;
		}

		@Override
		public double getCtrlY1() {
			return this.ctrlY;
		}

	}

	/** An element of the path that represents a <code>CURVE_TO</code>.
	 *
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 13.0
	 */
	@SuppressWarnings("checkstyle:magicnumber")
	static class CurvePathElement2d extends PathElement2d {

		private static final long serialVersionUID = -2831895270995173092L;

		private final double fromX;

		private final double fromY;

		private final double ctrlX1;

		private final double ctrlY1;

		private final double ctrlX2;

		private final double ctrlY2;

		private Boolean isEmpty;

		/** Constructor.
		 * @param fromx x coordinate of the origin point.
		 * @param fromy y coordinate of the origin point.
		 * @param ctrlx1 x coordinate of the first control point.
		 * @param ctrly1 y coordinate of the first control point.
		 * @param ctrlx2 x coordinate of the second control point.
		 * @param ctrly2 y coordinate of the second control point.
		 * @param tox x coordinate of the target point.
		 * @param toy y coordinate of the target point.
		 */
		CurvePathElement2d(double fromx, double fromy, double ctrlx1, double ctrly1, double ctrlx2,
				double ctrly2, double tox, double toy) {
			super(PathElementType.CURVE_TO, tox, toy);
			this.fromX = fromx;
			this.fromY = fromy;
			this.ctrlX1 = ctrlx1;
			this.ctrlY1 = ctrly1;
			this.ctrlX2 = ctrlx2;
			this.ctrlY2 = ctrly2;
		}

		@Pure
		@Override
		public boolean equals(Object obj) {
			if (obj == this) {
				return true;
			}
			if (getClass().isInstance(obj)) {
				final PathElement2afp elt = (PathElement2afp) obj;
				return getType() == elt.getType()
						&& getToX() == elt.getToX()
						&& getToY() == elt.getToY()
						&& getCtrlX1() == elt.getCtrlX1()
						&& getCtrlY1() == elt.getCtrlY1()
						&& getCtrlX2() == elt.getCtrlX2()
						&& getCtrlY2() == elt.getCtrlY2()
						&& getFromX() == elt.getFromX()
						&& getFromY() == elt.getFromY();
			}
			return false;
		}

		@Pure
		@Override
		public int hashCode() {
			long bits = 1L;
			bits = 31L * bits + this.type.hashCode();
			bits = 31L * bits + Double.hashCode(getToX());
			bits = 31L * bits + Double.hashCode(getToY());
			bits = 31L * bits + Double.hashCode(getCtrlX1());
			bits = 31L * bits + Double.hashCode(getCtrlY1());
			bits = 31L * bits + Double.hashCode(getCtrlX2());
			bits = 31L * bits + Double.hashCode(getCtrlY2());
			bits = 31L * bits + Double.hashCode(getFromX());
			bits = 31L * bits + Double.hashCode(getFromY());
			return (int) (bits ^ (bits >> 31));
		}

		@Pure
		@Override
		public boolean isEmpty() {
			if (this.isEmpty == null) {
				this.isEmpty = MathUtil.isEpsilonEqual(this.fromX, this.toX)
						&& MathUtil.isEpsilonEqual(this.fromY, this.toY)
						&& MathUtil.isEpsilonEqual(this.ctrlX1, this.toX)
						&& MathUtil.isEpsilonEqual(this.ctrlY1, this.toY)
						&& MathUtil.isEpsilonEqual(this.ctrlX2, this.toX)
						&& MathUtil.isEpsilonEqual(this.ctrlY2, this.toY);
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
			array[0] = (int) this.ctrlX1;
			array[1] = (int) this.ctrlY1;
			array[2] = (int) this.ctrlX2;
			array[3] = (int) this.ctrlY2;
			array[4] = (int) this.toX;
			array[5] = (int) this.toY;
		}

		@Override
		public void toArray(double[] array) {
			assert array != null : AssertMessages.notNullParameter();
			assert array.length >= 6 : AssertMessages.tooSmallArrayParameter(array.length, 6);
			array[0] = this.ctrlX1;
			array[1] = this.ctrlY1;
			array[2] = this.ctrlX2;
			array[3] = this.ctrlY2;
			array[4] = this.toX;
			array[5] = this.toY;
		}

		@Pure
		@Override
		public double[] toArray() {
			return new double[] {this.ctrlX1, this.ctrlY1, this.ctrlX2, this.ctrlY2, this.toX, this.toY};
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
		public double getCtrlX1() {
			return this.ctrlX1;
		}

		@Override
		public double getCtrlY1() {
			return this.ctrlY1;
		}

		@Override
		public double getCtrlX2() {
			return this.ctrlX2;
		}

		@Override
		public double getCtrlY2() {
			return this.ctrlY2;
		}

	}

	/** An element of the path that represents a <code>CLOSE</code>.
	 *
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 13.0
	 */
	static class ClosePathElement2d extends PathElement2d {

		private static final long serialVersionUID = 5324688417590599323L;

		private final double fromX;

		private final double fromY;

		private Boolean isEmpty;

		/** Constructor.
		 * @param fromx x coordinate of the origin point.
		 * @param fromy y coordinate of the origin point.
		 * @param tox x coordinate of the target point.
		 * @param toy y coordinate of the target point.
		 */
		ClosePathElement2d(double fromx, double fromy, double tox, double toy) {
			super(PathElementType.CLOSE, tox, toy);
			this.fromX = fromx;
			this.fromY = fromy;
		}

		@Pure
		@Override
		public boolean equals(Object obj) {
			if (obj == this) {
				return true;
			}
			if (getClass().isInstance(obj)) {
				final PathElement2afp elt = (PathElement2afp) obj;
				return getType() == elt.getType()
						&& getToX() == elt.getToX()
						&& getToY() == elt.getToY()
						&& getFromX() == elt.getFromX()
						&& getFromY() == elt.getFromY();
			}
			return false;
		}

		@Pure
		@Override
		public int hashCode() {
			long bits = 1L;
			bits = 31L * bits + this.type.hashCode();
			bits = 31L * bits + Double.hashCode(getToX());
			bits = 31L * bits + Double.hashCode(getToY());
			bits = 31L * bits + Double.hashCode(getFromX());
			bits = 31L * bits + Double.hashCode(getFromY());
			return (int) (bits ^ (bits >> 31));
		}

		@Pure
		@Override
		public boolean isEmpty() {
			if (this.isEmpty == null) {
				this.isEmpty = MathUtil.isEpsilonEqual(this.fromX, this.toX)
						&& MathUtil.isEpsilonEqual(this.fromY, this.toY);
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
			assert array.length >= 2 : AssertMessages.tooSmallArrayParameter(array.length, 2);
			array[0] = (int) this.toX;
			array[1] = (int) this.toY;
		}

		@Override
		public void toArray(double[] array) {
			assert array != null : AssertMessages.notNullParameter();
			assert array.length >= 2 : AssertMessages.tooSmallArrayParameter(array.length, 2);
			array[0] = this.toX;
			array[1] = this.toY;
		}

		@Pure
		@Override
		public double[] toArray() {
			return new double[] {this.toX, this.toY};
		}

		@Override
		public double getFromX() {
			return this.fromX;
		}

		@Override
		public double getFromY() {
			return this.fromY;
		}

	}

	/** An element of the path that represents a <code>Arc_TO</code>.
	 *
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 13.0
	 */
	@SuppressWarnings("checkstyle:magicnumber")
	static class ArcPathElement2d extends PathElement2d {

		private static final long serialVersionUID = -2831895270995173092L;

		private final double fromX;

		private final double fromY;

		private final double xradius;

		private final double yradius;

		private final double xrotation;

		private final boolean largeArcFlag;

		private boolean sweepFlag;

		private Boolean isEmpty;

		/** Constructor.
		 * @param fromx x coordinate of the origin point.
		 * @param fromy y coordinate of the origin point.
		 * @param tox x coordinate of the target point.
		 * @param toy y coordinate of the target point.
		 * @param xradius radius of the ellipse along its x axis.
		 * @param yradius radius of the ellipse along its y axis.
		 * @param xrotation rotation (in radians) of the ellipse's x axis.
		 * @param largeArcFlag <code>true</code> iff the path will sweep the long way around the ellipse.
		 * @param sweepFlag <code>true</code> iff the path will sweep clockwise around the ellipse.
		 */
		ArcPathElement2d(double fromx, double fromy, double tox, double toy, double xradius,
				double yradius, double xrotation, boolean largeArcFlag, boolean sweepFlag) {
			super(PathElementType.ARC_TO, tox, toy);
			this.fromX = fromx;
			this.fromY = fromy;
			this.xradius = xradius;
			this.yradius = yradius;
			this.xrotation = xrotation;
			this.largeArcFlag = largeArcFlag;
			this.sweepFlag = sweepFlag;
		}

		@Pure
		@Override
		public boolean equals(Object obj) {
			try {
                if (obj == null) {
                    return false;
                }

                if (this.getClass() != obj.getClass()) {
                    return false;
                }

				final PathElement2afp elt = (PathElement2afp) obj;
				return getType() == elt.getType()
						&& getToX() == elt.getToX()
						&& getToY() == elt.getToY()
						&& getRadiusX() == elt.getRadiusX()
						&& getRadiusY() == elt.getRadiusY()
						&& getRotationX() == elt.getRotationX()
						&& getLargeArcFlag() == elt.getLargeArcFlag()
						&& getSweepFlag() == elt.getSweepFlag();
			} catch (Throwable exception) {
				//
			}
			return false;
		}

		@Pure
		@Override
		public int hashCode() {
			long bits = 1L;
			bits = 31L * bits + this.type.hashCode();
			bits = 31L * bits + Double.hashCode(getToX());
			bits = 31L * bits + Double.hashCode(getToY());
			bits = 31L * bits + Double.hashCode(getRadiusX());
			bits = 31L * bits + Double.hashCode(getRadiusY());
			bits = 31L * bits + Double.hashCode(getRotationX());
			bits = 31L * bits + Boolean.hashCode(getLargeArcFlag());
			bits = 31L * bits + Boolean.hashCode(getSweepFlag());
			return (int) (bits ^ (bits >> 31));
		}

		@Pure
		@Override
		public boolean isEmpty() {
			if (this.isEmpty == null) {
				this.isEmpty = MathUtil.isEpsilonEqual(this.fromX, this.toX)
						&& MathUtil.isEpsilonEqual(this.fromY, this.toY);
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
			assert array.length >= 2 : AssertMessages.tooSmallArrayParameter(array.length, 2);
			array[0] = (int) this.toX;
			array[1] = (int) this.toY;
		}

		@Override
		public void toArray(double[] array) {
			assert array != null : AssertMessages.notNullParameter();
			assert array.length >= 2 : AssertMessages.tooSmallArrayParameter(array.length, 2);
			array[0] = this.toX;
			array[1] = this.toY;
		}

		@Pure
		@Override
		public double[] toArray() {
			return new double[] {this.toX, this.toY};
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
		public double getRadiusX() {
			return this.xradius;
		}

		@Override
		public double getRadiusY() {
			return this.yradius;
		}

		@Override
		public double getRotationX() {
			return this.xrotation;
		}

		@Override
		public boolean getSweepFlag() {
			return this.sweepFlag;
		}

		@Override
		public boolean getLargeArcFlag() {
			return this.largeArcFlag;
		}

	}

}
