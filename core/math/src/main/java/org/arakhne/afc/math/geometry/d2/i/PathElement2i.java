/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2016 The original authors, and other authors.
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

package org.arakhne.afc.math.geometry.d2.i;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.geometry.PathElementType;
import org.arakhne.afc.math.geometry.d2.ai.PathElement2ai;

/** An element of the path with 2 integer numbers.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
@SuppressWarnings("checkstyle:magicnumber")
public abstract class PathElement2i implements PathElement2ai {

	private static final long serialVersionUID = -7762354100984227855L;

	/** Type of the element.
	 */
	protected final PathElementType type;

	/** Target point.
	 */
	protected final int toX;

	/** Target point.
	 */
	protected final int toY;

	/**
	 * @param type is the type of the element.
	 * @param tox the x coordinate of the target point.
	 * @param toy the x coordinate of the target point.
	 */
	PathElement2i(PathElementType type, int tox, int toy) {
		assert type != null : "Path element type must be not null"; //$NON-NLS-1$
		this.type = type;
		this.toX = tox;
		this.toY = toy;
	}

	/** Create an instance of path element.
	 *
	 * @param type is the type of the new element.
	 * @param lastX is the coordinate of the last point.
	 * @param lastY is the coordinate of the last point.
	 * @param coords are the coordinates.
	 * @return the instance of path element.
	 */
	@Pure
	public static PathElement2i newInstance(PathElementType type, int lastX, int lastY, int[] coords) {
		assert type != null : "Path element type must be not null"; //$NON-NLS-1$
		assert coords != null : "Array of coordinates must be not null"; //$NON-NLS-1$
		assert coords.length >= 2 : "Size of the array of coordinates is too small"; //$NON-NLS-1$
		switch (type) {
		case MOVE_TO:
			return new MovePathElement2i(coords[0], coords[1]);
		case LINE_TO:
			return new LinePathElement2i(lastX, lastY, coords[0], coords[1]);
		case QUAD_TO:
			assert coords.length >= 4 : "Size of the array of coordinates is too small"; //$NON-NLS-1$
			return new QuadPathElement2i(lastX, lastY, coords[0], coords[1], coords[2], coords[3]);
		case CURVE_TO:
			assert coords.length >= 6 : "Size of the array of coordinates is too small"; //$NON-NLS-1$
			return new CurvePathElement2i(lastX, lastY, coords[0], coords[1], coords[2], coords[3], coords[4], coords[5]);
		case CLOSE:
			return new ClosePathElement2i(lastX, lastY, coords[0], coords[1]);
		default:
		}
		throw new IllegalArgumentException();
	}

	@Pure
	@Override
	public abstract boolean equals(Object obj);

	@Pure
	@Override
	public abstract int hashCode();

	@Override
	@Pure
	public final int getToX() {
		return this.toX;
	}

	@Override
	@Pure
	public final int getToY() {
		return this.toY;
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
	public abstract int[] toArray();

	/** An element of the path that represents a <code>MOVE_TO</code>.
	 *
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 13.0
	 */
	static class MovePathElement2i extends PathElement2i {

		private static final long serialVersionUID = -574266368740822686L;

		/**
		 * @param tox x coordinate of the target point.
		 * @param toy y coordinate of the target point.
		 */
		MovePathElement2i(int tox, int toy) {
			super(PathElementType.MOVE_TO, tox, toy);
		}

		@Pure
		@Override
		public boolean equals(Object obj) {
			try {
				final PathElement2ai elt = (PathElement2ai) obj;
				return getType() == elt.getType()
						&& getToX() == elt.getToX()
						&& getToY() == elt.getToY();
			} catch (Throwable exception) {
				//
			}
			return false;
		}

		@Pure
		@Override
		public int hashCode() {
			int bits = 1;
			bits = 31 * bits + this.type.ordinal();
			bits = 31 * bits + getToX();
			bits = 31 * bits + getToY();
			return bits ^ (bits >> 32);
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
			assert array != null : "Array must be not null"; //$NON-NLS-1$
			assert array.length >= 2 : "Array size is too small"; //$NON-NLS-1$
			array[0] = this.toX;
			array[1] = this.toY;
		}

		@Override
		public void toArray(double[] array) {
			assert array != null : "Array must be not null"; //$NON-NLS-1$
			assert array.length >= 2 : "Array size is too small"; //$NON-NLS-1$
			array[0] = this.toX;
			array[1] = this.toY;
		}

		@Pure
		@Override
		public int[] toArray() {
			return new int[] {this.toX, this.toY};
		}

		@Pure
		@Override
		public String toString() {
			return "MOVE(" //$NON-NLS-1$
					+ this.toX + "x" //$NON-NLS-1$
					+ this.toY + ")"; //$NON-NLS-1$
		}

		@Override
		public int getFromX() {
			return 0;
		}

		@Override
		public int getFromY() {
			return 0;
		}

		@Override
		public int getCtrlX1() {
			return 0;
		}

		@Override
		public int getCtrlY1() {
			return 0;
		}

		@Override
		public int getCtrlX2() {
			return 0;
		}

		@Override
		public int getCtrlY2() {
			return 0;
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
	static class LinePathElement2i extends PathElement2i {

		private static final long serialVersionUID = 7733931118894880566L;

		private final int fromX;

		private final int fromY;

		/**
		 * @param fromx x coordinate of the origin point.
		 * @param fromy y coordinate of the origin point.
		 * @param tox x coordinate of the target point.
		 * @param toy y coordinate of the target point.
		 */
		LinePathElement2i(int fromx, int fromy, int tox, int toy) {
			super(PathElementType.LINE_TO, tox, toy);
			this.fromX = fromx;
			this.fromY = fromy;
		}

		@Pure
		@Override
		public boolean equals(Object obj) {
			try {
				final PathElement2ai elt = (PathElement2ai) obj;
				if (elt != null) {
					return getType() == elt.getType()
							&& getToX() == elt.getToX()
							&& getToY() == elt.getToY()
							&& getFromX() == elt.getFromX()
							&& getFromY() == elt.getFromY();
				}

			} catch (Throwable exception) {
				//
			}
			return false;
		}

		@Pure
		@Override
		public int hashCode() {
			int bits = 1;
			bits = 31 * bits + this.type.ordinal();
			bits = 31 * bits + getToX();
			bits = 31 * bits + getToY();
			bits = 31 * bits + getFromX();
			bits = 31 * bits + getFromY();
			return bits ^ (bits >> 32);
		}

		@Pure
		@Override
		public boolean isEmpty() {
			return (this.fromX == this.toX) && (this.fromY == this.toY);
		}

		@Pure
		@Override
		public boolean isDrawable() {
			return !isEmpty();
		}

		@Override
		public void toArray(int[] array) {
			assert array != null : "Array must be not null"; //$NON-NLS-1$
			assert array.length >= 2 : "Array size is too small"; //$NON-NLS-1$
			array[0] = this.toX;
			array[1] = this.toY;
		}

		@Override
		public void toArray(double[] array) {
			assert array != null : "Array must be not null"; //$NON-NLS-1$
			assert array.length >= 2 : "Array size is too small"; //$NON-NLS-1$
			array[0] = this.toX;
			array[1] = this.toY;
		}

		@Pure
		@Override
		public int[] toArray() {
			return new int[] {this.toX, this.toY};
		}

		@Pure
		@Override
		public String toString() {
			return "LINE(" //$NON-NLS-1$
					+ this.toX + "x" //$NON-NLS-1$
					+ this.toY + ")"; //$NON-NLS-1$
		}

		@Override
		public int getFromX() {
			return this.fromX;
		}

		@Override
		public int getFromY() {
			return this.fromY;
		}

		@Override
		public int getCtrlX1() {
			return 0;
		}

		@Override
		public int getCtrlY1() {
			return 0;
		}

		@Override
		public int getCtrlX2() {
			return 0;
		}

		@Override
		public int getCtrlY2() {
			return 0;
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
	static class QuadPathElement2i extends PathElement2i {

		private static final long serialVersionUID = 3335394189817197203L;

		private final int fromX;

		private final int fromY;

		private final int ctrlX;

		private final int ctrlY;

		/**
		 * @param fromx x coordinate of the origin point.
		 * @param fromy y coordinate of the origin point.
		 * @param ctrlx x coordinate of the control point.
		 * @param ctrly y coordinate of the control point.
		 * @param tox x coordinate of the target point.
		 * @param toy y coordinate of the target point.
		 */
		QuadPathElement2i(int fromx, int fromy, int ctrlx, int ctrly, int tox, int toy) {
			super(PathElementType.QUAD_TO, tox, toy);
			this.fromX = fromx;
			this.fromY = fromy;
			this.ctrlX = ctrlx;
			this.ctrlY = ctrly;
		}

		@Pure
		@Override
		public boolean equals(Object obj) {
			try {
				final PathElement2ai elt = (PathElement2ai) obj;
				if (elt != null) {
					return getType() == elt.getType()
							&& getToX() == elt.getToX()
							&& getToY() == elt.getToY()
							&& getCtrlX1() == elt.getCtrlX1()
							&& getCtrlY1() == elt.getCtrlY1()
							&& getFromX() == elt.getFromX()
							&& getFromY() == elt.getFromY();
				}

			} catch (Throwable exception) {
				//
			}
			return false;
		}

		@Pure
		@Override
		public int hashCode() {
			int bits = 1;
			bits = 31 * bits + this.type.ordinal();
			bits = 31 * bits + getToX();
			bits = 31 * bits + getToY();
			bits = 31 * bits + getCtrlX1();
			bits = 31 * bits + getCtrlY1();
			bits = 31 * bits + getFromX();
			bits = 31 * bits + getFromY();
			return bits ^ (bits >> 32);
		}

		@Pure
		@Override
		public boolean isEmpty() {
			return (this.fromX == this.toX) && (this.fromY == this.toY)
					&& (this.ctrlX == this.toX) && (this.ctrlY == this.toY);
		}

		@Pure
		@Override
		public boolean isDrawable() {
			return !isEmpty();
		}

		@Override
		public void toArray(int[] array) {
			assert array != null : "Array must be not null"; //$NON-NLS-1$
			assert array.length >= 4 : "Array size is too small"; //$NON-NLS-1$
			array[0] = this.ctrlX;
			array[1] = this.ctrlY;
			array[2] = this.toX;
			array[3] = this.toY;
		}

		@Override
		public void toArray(double[] array) {
			assert array != null : "Array must be not null"; //$NON-NLS-1$
			assert array.length >= 4 : "Array size is too small"; //$NON-NLS-1$
			array[0] = this.ctrlX;
			array[1] = this.ctrlY;
			array[2] = this.toX;
			array[3] = this.toY;
		}

		@Pure
		@Override
		public int[] toArray() {
			return new int[] {this.ctrlX, this.ctrlY, this.toX, this.toY};
		}

		@Pure
		@Override
		public String toString() {
			return "QUAD(" //$NON-NLS-1$
					+ this.ctrlX + "x" //$NON-NLS-1$
					+ this.ctrlY + "|" //$NON-NLS-1$
					+ this.toX + "x" //$NON-NLS-1$
					+ this.toY + ")"; //$NON-NLS-1$
		}

		@Override
		public int getFromX() {
			return this.fromX;
		}

		@Override
		public int getFromY() {
			return this.fromY;
		}

		@Override
		public int getCtrlX1() {
			return this.ctrlX;
		}

		@Override
		public int getCtrlY1() {
			return this.ctrlY;
		}

		@Override
		public int getCtrlX2() {
			return 0;
		}

		@Override
		public int getCtrlY2() {
			return 0;
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
	static class CurvePathElement2i extends PathElement2i {

		private static final long serialVersionUID = 7009674542781709373L;

		private final int fromX;

		private final int fromY;

		private final int ctrlX1;

		private final int ctrlY1;

		private final int ctrlX2;

		private final int ctrlY2;

		/**
		 * @param fromx x coordinate of the origin point.
		 * @param fromy y coordinate of the origin point.
		 * @param ctrlx1 x coordinate of the first control point.
		 * @param ctrly1 y coordinate of the first control point.
		 * @param ctrlx2 x coordinate of the second control point.
		 * @param ctrly2 y coordinate of the second control point.
		 * @param tox x coordinate of the target point.
		 * @param toy y coordinate of the target point.
		 */
		CurvePathElement2i(int fromx, int fromy, int ctrlx1, int ctrly1, int ctrlx2, int ctrly2, int tox, int toy) {
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
			try {
				final PathElement2ai elt = (PathElement2ai) obj;
				if (elt != null) {
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

			} catch (Throwable exception) {
				//
			}
			return false;
		}

		@Pure
		@Override
		public int hashCode() {
			int bits = 1;
			bits = 31 * bits + this.type.ordinal();
			bits = 31 * bits + getToX();
			bits = 31 * bits + getToY();
			bits = 31 * bits + getCtrlX1();
			bits = 31 * bits + getCtrlY1();
			bits = 31 * bits + getCtrlX2();
			bits = 31 * bits + getCtrlY2();
			bits = 31 * bits + getFromX();
			bits = 31 * bits + getFromY();
			return bits ^ (bits >> 32);
		}

		@Pure
		@Override
		public boolean isEmpty() {
			return (this.fromX == this.toX) && (this.fromY == this.toY)
					&& (this.ctrlX1 == this.toX) && (this.ctrlY1 == this.toY)
					&& (this.ctrlX2 == this.toX) && (this.ctrlY2 == this.toY);
		}

		@Pure
		@Override
		public boolean isDrawable() {
			return !isEmpty();
		}

		@Override
		public void toArray(int[] array) {
			assert array != null : "Array must be not null"; //$NON-NLS-1$
			assert array.length >= 6 : "Array size is too small"; //$NON-NLS-1$
			array[0] = this.ctrlX1;
			array[1] = this.ctrlY1;
			array[2] = this.ctrlX2;
			array[3] = this.ctrlY2;
			array[4] = this.toX;
			array[5] = this.toY;
		}

		@Override
		public void toArray(double[] array) {
			assert array != null : "Array must be not null"; //$NON-NLS-1$
			assert array.length >= 6 : "Array size is too small"; //$NON-NLS-1$
			array[0] = this.ctrlX1;
			array[1] = this.ctrlY1;
			array[2] = this.ctrlX2;
			array[3] = this.ctrlY2;
			array[4] = this.toX;
			array[5] = this.toY;
		}

		@Pure
		@Override
		public int[] toArray() {
			return new int[] {this.ctrlX1, this.ctrlY1, this.ctrlX2, this.ctrlY2, this.toX, this.toY};
		}

		@Pure
		@Override
		public String toString() {
			return "CURVE(" //$NON-NLS-1$
					+ this.ctrlX1 + "x" //$NON-NLS-1$
					+ this.ctrlY1 + "|" //$NON-NLS-1$
					+ this.ctrlX2 + "x" //$NON-NLS-1$
					+ this.ctrlY2 + "|" //$NON-NLS-1$
					+ this.toX + "x" //$NON-NLS-1$
					+ this.toY + ")"; //$NON-NLS-1$
		}

		@Override
		public int getFromX() {
			return this.fromX;
		}

		@Override
		public int getFromY() {
			return this.fromY;
		}

		@Override
		public int getCtrlX1() {
			return this.ctrlX1;
		}

		@Override
		public int getCtrlY1() {
			return this.ctrlY1;
		}

		@Override
		public int getCtrlX2() {
			return this.ctrlX2;
		}

		@Override
		public int getCtrlY2() {
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
	static class ClosePathElement2i extends PathElement2i {

		private static final long serialVersionUID = -8709004906872207794L;

		private final int fromX;

		private final int fromY;

		/**
		 * @param fromx x coordinate of the origin point.
		 * @param fromy y coordinate of the origin point.
		 * @param tox x coordinate of the target point.
		 * @param toy y coordinate of the target point.
		 */
		ClosePathElement2i(int fromx, int fromy, int tox, int toy) {
			super(PathElementType.CLOSE, tox, toy);
			this.fromX = fromx;
			this.fromY = fromy;

		}

		@Pure
		@Override
		public boolean equals(Object obj) {
			try {
				final PathElement2ai elt = (PathElement2ai) obj;
                if (elt != null){
                    return getType() == elt.getType()
                            && getToX() == elt.getToX()
                            && getToY() == elt.getToY()
                            && getFromX() == elt.getFromX()
                            && getFromY() == elt.getFromY();
                }

			} catch (Throwable exception) {
				//
			}
			return false;
		}

		@Pure
		@Override
		public int hashCode() {
			int bits = 1;
			bits = 31 * bits + this.type.ordinal();
			bits = 31 * bits + getToX();
			bits = 31 * bits + getToY();
			bits = 31 * bits + getFromX();
			bits = 31 * bits + getFromY();
			return bits ^ (bits >> 32);
		}

		@Pure
		@Override
		public boolean isEmpty() {
			return (this.fromX == this.toX) && (this.fromY == this.toY);
		}

		@Pure
		@Override
		public boolean isDrawable() {
			return !isEmpty();
		}

		@Override
		public void toArray(int[] array) {
			assert array != null : "Array must be not null"; //$NON-NLS-1$
			assert array.length >= 2 : "Array size is too small"; //$NON-NLS-1$
			array[0] = this.toX;
			array[1] = this.toY;
		}

		@Override
		public void toArray(double[] array) {
			assert array != null : "Array must be not null"; //$NON-NLS-1$
			assert array.length >= 2 : "Array size is too small"; //$NON-NLS-1$
			array[0] = this.toX;
			array[1] = this.toY;
		}

		@Pure
		@Override
		public int[] toArray() {
			return new int[] {this.toX, this.toY};
		}

		@Pure
		@Override
		public String toString() {
			return "CLOSE"; //$NON-NLS-1$
		}

		@Override
		public int getFromX() {
			return this.fromX;
		}

		@Override
		public int getFromY() {
			return this.fromY;
		}

		@Override
		public int getCtrlX1() {
			return 0;
		}

		@Override
		public int getCtrlY1() {
			return 0;
		}

		@Override
		public int getCtrlX2() {
			return 0;
		}

		@Override
		public int getCtrlY2() {
			return 0;
		}

	}

}
