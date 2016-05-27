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

package org.arakhne.afc.math.geometry.d2.ifx;

import javafx.beans.property.IntegerProperty;
import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.MathUtil;
import org.arakhne.afc.math.geometry.PathElementType;
import org.arakhne.afc.math.geometry.d2.ai.PathElement2ai;

/** An element of the path with 2 integer FX properties.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
@SuppressWarnings("checkstyle:magicnumber")
public abstract class PathElement2ifx implements PathElement2ai {

	private static final long serialVersionUID = -5532787413347691238L;

	/** Type of the element.
	 */
	protected final PathElementType type;

	/** Target point.
	 */
	protected final IntegerProperty toX;

	/** Target point.
	 */
	protected final IntegerProperty toY;

	/**
	 * @param type is the type of the element.
	 * @param tox the x coordinate of the target point.
	 * @param toy the x coordinate of the target point.
	 */
	PathElement2ifx(PathElementType type, IntegerProperty tox, IntegerProperty toy) {
		assert type != null : "Path winding rule must be not null"; //$NON-NLS-1$
		assert tox != null : "Property toX must be not null"; //$NON-NLS-1$
		assert toy != null : "Property toY must be not null"; //$NON-NLS-1$
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
	public static PathElement2ifx newInstance(PathElementType type, IntegerProperty lastX,
			IntegerProperty lastY, IntegerProperty[] coords) {
		assert type != null : "Path winding rule must be not null"; //$NON-NLS-1$
		assert coords != null : "Coordinate array must be not null"; //$NON-NLS-1$
		assert coords.length >= 2 : "Size of the oordinate array is too small"; //$NON-NLS-1$
		switch (type) {
		case MOVE_TO:
			return new MovePathElement2ifx(coords[0], coords[1]);
		case LINE_TO:
			return new LinePathElement2ifx(lastX, lastY, coords[0], coords[1]);
		case QUAD_TO:
			assert coords.length >= 4 : "Size of the oordinate array is too small"; //$NON-NLS-1$
			return new QuadPathElement2ifx(lastX, lastY, coords[0], coords[1], coords[2], coords[3]);
		case CURVE_TO:
			assert coords.length >= 6 : "Size of the oordinate array is too small"; //$NON-NLS-1$
			return new CurvePathElement2ifx(lastX, lastY, coords[0], coords[1], coords[2], coords[3], coords[4], coords[5]);
		case CLOSE:
			return new ClosePathElement2ifx(lastX, lastY, coords[0], coords[1]);
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

	/** Replies the x coordinate of the target point property.
	 *
	 * @return the x coordinate.
	 */
	@Pure
	public IntegerProperty toXProperty() {
		return this.toX;
	}

	/** Replies the y coordinate of the target point property.
	 *
	 * @return the y coordinate.
	 */
	@Pure
	public IntegerProperty toYProperty() {
		return this.toY;
	}

	@Override
	@Pure
	public final int getToX() {
		return this.toX.get();
	}

	@Override
	@Pure
	public final int getToY() {
		return this.toY.get();
	}

	@Pure
	@Override
	public final PathElementType getType() {
		return this.type;
	}


	/** Copy the coords into the given array, except the source point.
	 *
	 * @param array the output array.
	 */
	@Pure
	public abstract void toArray(IntegerProperty[] array);

	/** Copy the coords into an array, except the source point.
	 *
	 * @return the array of the points, except the source point.
	 */
	@Pure
	public abstract IntegerProperty[] toArray();

	/** Replies the x coordinate of the starting point property.
	 *
	 * @return the x coordinate, or <code>null</code> if the type is {@link PathElementType#MOVE_TO}.
	 */
	@Pure
	public abstract IntegerProperty fromXProperty();

	/** Replies the y coordinate of the starting point property.
	 *
	 * @return the y coordinate, or <code>null</code> if the type is {@link PathElementType#MOVE_TO}.
	 */
	@Pure
	public abstract IntegerProperty fromYProperty();

	/** Replies the x coordinate of the first control point property.
	 *
	 * @return the x coordinate, or <code>null</code> if the type is {@link PathElementType#MOVE_TO},
	 * {@link PathElementType#LINE_TO}, or {@link PathElementType#CLOSE}.
	 */
	@Pure
	public abstract IntegerProperty ctrlX1Property();

	/** Replies the y coordinate of the first control point property.
	 *
	 * @return the y coordinate, or {@link Double#NaN} if the type is {@link PathElementType#MOVE_TO},
	 * {@link PathElementType#LINE_TO}, or {@link PathElementType#CLOSE}.
	 */
	@Pure
	public abstract IntegerProperty ctrlY1Property();

	/** Replies the x coordinate of the second control point property.
	 *
	 * @return the x coordinate, or <code>null</code> if the type is {@link PathElementType#MOVE_TO},
	 * {@link PathElementType#LINE_TO}, {@link PathElementType#QUAD_TO}, or {@link PathElementType#CLOSE}.
	 */
	@Pure
	public abstract IntegerProperty ctrlX2Property();

	/** Replies the y coordinate of the second  control point property.
	 *
	 * @return the y coordinate, or <code>null</code> if the type is {@link PathElementType#MOVE_TO},
	 * {@link PathElementType#LINE_TO}, {@link PathElementType#QUAD_TO}, or {@link PathElementType#CLOSE}.
	 */
	@Pure
	public abstract IntegerProperty ctrlY2Property();

	/** An element of the path that represents a <code>MOVE_TO</code>.
	 *
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 13.0
	 */
	static class MovePathElement2ifx extends PathElement2ifx {

		private static final long serialVersionUID = 7240290153738547626L;

		/**
		 * @param tox x coordinate of the target point.
		 * @param toy y coordinate of the target point.
		 */
		MovePathElement2ifx(IntegerProperty tox, IntegerProperty toy) {
			super(PathElementType.MOVE_TO, tox, toy);
		}

		@Pure
		@Override
		public boolean equals(Object obj) {
			try {
				final PathElement2ai elt = (PathElement2ai) obj;
				if (elt != null){
                    return getType() == elt.getType()
                            && getToX() == elt.getToX()
                            && getToY() == elt.getToY();
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

		@Pure
		@Override
		public void toArray(int[] array) {
			assert array != null : "Array must be not null"; //$NON-NLS-1$
			assert array.length >= 2 : "Size of the array is too small"; //$NON-NLS-1$
			array[0] = this.toX.get();
			array[1] = this.toY.get();
		}

		@Pure
		@Override
		public void toArray(double[] array) {
			assert array != null : "Array must be not null"; //$NON-NLS-1$
			assert array.length >= 2 : "Size of the array is too small"; //$NON-NLS-1$
			array[0] = this.toX.get();
			array[1] = this.toY.get();
		}

		@Pure
		@Override
		public void toArray(IntegerProperty[] array) {
			assert array != null : "Array must be not null"; //$NON-NLS-1$
			assert array.length >= 2 : "Size of the array is too small"; //$NON-NLS-1$
			array[0] = this.toX;
			array[1] = this.toY;
		}

		@Pure
		@Override
		public IntegerProperty[] toArray() {
			return new IntegerProperty[] {this.toX, this.toY};
		}

		@Pure
		@Override
		public String toString() {
			return "MOVE(" //$NON-NLS-1$
					+ this.toX + "x" //$NON-NLS-1$
					+ this.toY + ")"; //$NON-NLS-1$
		}

		@Pure
		@Override
		public int getFromX() {
			return 0;
		}

		@Pure
		@Override
		public int getFromY() {
			return 0;
		}

		@Pure
		@Override
		public int getCtrlX1() {
			return 0;
		}

		@Pure
		@Override
		public int getCtrlY1() {
			return 0;
		}

		@Pure
		@Override
		public int getCtrlX2() {
			return 0;
		}

		@Pure
		@Override
		public int getCtrlY2() {
			return 0;
		}

		@Pure
		@Override
		public IntegerProperty fromXProperty() {
			return null;
		}

		@Pure
		@Override
		public IntegerProperty fromYProperty() {
			return null;
		}

		@Pure
		@Override
		public IntegerProperty ctrlX1Property() {
			return null;
		}

		@Pure
		@Override
		public IntegerProperty ctrlY1Property() {
			return null;
		}

		@Pure
		@Override
		public IntegerProperty ctrlX2Property() {
			return null;
		}

		@Pure
		@Override
		public IntegerProperty ctrlY2Property() {
			return null;
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
	static class LinePathElement2ifx extends PathElement2ifx {

		private static final long serialVersionUID = 2010270547259311623L;

		private final IntegerProperty fromX;

		private final IntegerProperty fromY;

		/**
		 * @param fromx x coordinate of the origin point.
		 * @param fromy y coordinate of the origin point.
		 * @param tox x coordinate of the target point.
		 * @param toy y coordinate of the target point.
		 */
		LinePathElement2ifx(IntegerProperty fromx, IntegerProperty fromy, IntegerProperty tox, IntegerProperty toy) {
			super(PathElementType.LINE_TO, tox, toy);
			assert fromx != null : "Property fromX must be not null"; //$NON-NLS-1$
			assert fromy != null : "Property fromY must be not null"; //$NON-NLS-1$
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
			return MathUtil.isEpsilonEqual(this.fromX.get(), this.toX.get())
					&& MathUtil.isEpsilonEqual(this.fromY.get(), this.toY.get());
		}

		@Pure
		@Override
		public boolean isDrawable() {
			return !isEmpty();
		}

		@Pure
		@Override
		public void toArray(int[] array) {
			assert array != null : "Array must be not null"; //$NON-NLS-1$
			assert array.length >= 2 : "Size of the array is too small"; //$NON-NLS-1$
			array[0] = this.toX.get();
			array[1] = this.toY.get();
		}

		@Pure
		@Override
		public void toArray(double[] array) {
			assert array != null : "Array must be not null"; //$NON-NLS-1$
			assert array.length >= 2 : "Size of the array is too small"; //$NON-NLS-1$
			array[0] = this.toX.get();
			array[1] = this.toY.get();
		}

		@Pure
		@Override
		public IntegerProperty[] toArray() {
			return new IntegerProperty[] {this.toX, this.toY};
		}

		@Pure
		@Override
		public void toArray(IntegerProperty[] array) {
			assert array != null : "Array must be not null"; //$NON-NLS-1$
			assert array.length >= 2 : "Size of the array is too small"; //$NON-NLS-1$
			array[0] = this.toX;
			array[1] = this.toY;
		}

		@Pure
		@Override
		public String toString() {
			return "LINE(" //$NON-NLS-1$
					+ this.toX + "x" //$NON-NLS-1$
					+ this.toY + ")"; //$NON-NLS-1$
		}

		@Pure
		@Override
		public int getFromX() {
			return this.fromX.get();
		}

		@Pure
		@Override
		public int getFromY() {
			return this.fromY.get();
		}

		@Pure
		@Override
		public int getCtrlX1() {
			return 0;
		}

		@Pure
		@Override
		public int getCtrlY1() {
			return 0;
		}

		@Pure
		@Override
		public int getCtrlX2() {
			return 0;
		}

		@Pure
		@Override
		public int getCtrlY2() {
			return 0;
		}

		@Pure
		@Override
		public IntegerProperty fromXProperty() {
			return this.fromX;
		}

		@Pure
		@Override
		public IntegerProperty fromYProperty() {
			return this.fromY;
		}

		@Pure
		@Override
		public IntegerProperty ctrlX1Property() {
			return null;
		}

		@Pure
		@Override
		public IntegerProperty ctrlY1Property() {
			return null;
		}

		@Pure
		@Override
		public IntegerProperty ctrlX2Property() {
			return null;
		}

		@Pure
		@Override
		public IntegerProperty ctrlY2Property() {
			return null;
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
	static class QuadPathElement2ifx extends PathElement2ifx {

		private static final long serialVersionUID = -1243640360335101578L;

		private final IntegerProperty fromX;

		private final IntegerProperty fromY;

		private final IntegerProperty ctrlX;

		private final IntegerProperty ctrlY;

		/**
		 * @param fromx x coordinate of the origin point.
		 * @param fromy y coordinate of the origin point.
		 * @param ctrlx x coordinate of the control point.
		 * @param ctrly y coordinate of the control point.
		 * @param tox x coordinate of the target point.
		 * @param toy y coordinate of the target point.
		 */
		QuadPathElement2ifx(IntegerProperty fromx, IntegerProperty fromy, IntegerProperty ctrlx,
				IntegerProperty ctrly, IntegerProperty tox, IntegerProperty toy) {
			super(PathElementType.QUAD_TO, tox, toy);
			assert fromx != null : "Property fromX must be not null"; //$NON-NLS-1$
			assert fromy != null : "Property fromY must be not null"; //$NON-NLS-1$
			assert ctrlx != null : "Property ctrlX must be not null"; //$NON-NLS-1$
			assert ctrly != null : "Property ctrlY must be not null"; //$NON-NLS-1$
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
			return MathUtil.isEpsilonEqual(this.fromX.get(), this.toX.get())
					&& MathUtil.isEpsilonEqual(this.fromY.get(), this.toY.get())
					&& MathUtil.isEpsilonEqual(this.ctrlX.get(), this.toX.get())
					&& MathUtil.isEpsilonEqual(this.ctrlY.get(), this.toY.get());

		}

		@Pure
		@Override
		public boolean isDrawable() {
			return !isEmpty();
		}

		@Pure
		@Override
		public void toArray(int[] array) {
			assert array != null : "Array must be not null"; //$NON-NLS-1$
			assert array.length >= 4 : "Size of the array is too small"; //$NON-NLS-1$
			array[0] = this.ctrlX.get();
			array[1] = this.ctrlY.get();
			array[2] = this.toX.get();
			array[3] = this.toY.get();
		}

		@Pure
		@Override
		public void toArray(double[] array) {
			assert array != null : "Array must be not null"; //$NON-NLS-1$
			assert array.length >= 4 : "Size of the array is too small"; //$NON-NLS-1$
			array[0] = this.ctrlX.get();
			array[1] = this.ctrlY.get();
			array[2] = this.toX.get();
			array[3] = this.toY.get();
		}

		@Pure
		@Override
		public IntegerProperty[] toArray() {
			return new IntegerProperty[] {this.ctrlX, this.ctrlY, this.toX, this.toY};
		}

		@Pure
		@Override
		public void toArray(IntegerProperty[] array) {
			assert array != null : "Array must be not null"; //$NON-NLS-1$
			assert array.length >= 4 : "Size of the array is too small"; //$NON-NLS-1$
			array[0] = this.ctrlX;
			array[1] = this.ctrlY;
			array[2] = this.toX;
			array[3] = this.toY;
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

		@Pure
		@Override
		public int getFromX() {
			return this.fromX.get();
		}

		@Pure
		@Override
		public int getFromY() {
			return this.fromY.get();
		}

		@Pure
		@Override
		public int getCtrlX1() {
			return this.ctrlX.get();
		}

		@Pure
		@Override
		public int getCtrlY1() {
			return this.ctrlY.get();
		}

		@Pure
		@Override
		public int getCtrlX2() {
			return 0;
		}

		@Pure
		@Override
		public int getCtrlY2() {
			return 0;
		}

		@Pure
		@Override
		public IntegerProperty fromXProperty() {
			return this.fromX;
		}

		@Pure
		@Override
		public IntegerProperty fromYProperty() {
			return this.fromY;
		}

		@Pure
		@Override
		public IntegerProperty ctrlX1Property() {
			return this.ctrlX;
		}

		@Pure
		@Override
		public IntegerProperty ctrlY1Property() {
			return this.ctrlY;
		}

		@Pure
		@Override
		public IntegerProperty ctrlX2Property() {
			return null;
		}

		@Pure
		@Override
		public IntegerProperty ctrlY2Property() {
			return null;
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
	static class CurvePathElement2ifx extends PathElement2ifx {

		private static final long serialVersionUID = 6354626635759607041L;

		private final IntegerProperty fromX;

		private final IntegerProperty fromY;

		private final IntegerProperty ctrlX1;

		private final IntegerProperty ctrlY1;

		private final IntegerProperty ctrlX2;

		private final IntegerProperty ctrlY2;

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
		CurvePathElement2ifx(IntegerProperty fromx, IntegerProperty fromy, IntegerProperty ctrlx1, IntegerProperty ctrly1,
				IntegerProperty ctrlx2, IntegerProperty ctrly2, IntegerProperty tox, IntegerProperty toy) {
			super(PathElementType.CURVE_TO, tox, toy);
			assert fromx != null : "Property fromX must be not null"; //$NON-NLS-1$
			assert fromy != null : "Property fromY must be not null"; //$NON-NLS-1$
			assert ctrlx1 != null : "Property ctrlX1 must be not null"; //$NON-NLS-1$
			assert ctrly1 != null : "Property ctrlY1 must be not null"; //$NON-NLS-1$
			assert ctrlx2 != null : "Property ctrlX2 must be not null"; //$NON-NLS-1$
			assert ctrly2 != null : "Property ctrlY2 must be not null"; //$NON-NLS-1$
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
			return MathUtil.isEpsilonEqual(this.fromX.get(), this.toX.get())
					&& MathUtil.isEpsilonEqual(this.fromY.get(), this.toY.get())
					&& MathUtil.isEpsilonEqual(this.ctrlX1.get(), this.toX.get())
					&& MathUtil.isEpsilonEqual(this.ctrlY1.get(), this.toY.get())
					&& MathUtil.isEpsilonEqual(this.ctrlX2.get(), this.toX.get())
					&& MathUtil.isEpsilonEqual(this.ctrlY2.get(), this.toY.get());

		}

		@Pure
		@Override
		public boolean isDrawable() {
			return !isEmpty();
		}

		@Pure
		@Override
		public void toArray(int[] array) {
			assert array != null : "Array must be not null"; //$NON-NLS-1$
			assert array.length >= 6 : "Size of the array is too small"; //$NON-NLS-1$
			array[0] = this.ctrlX1.get();
			array[1] = this.ctrlY1.get();
			array[2] = this.ctrlX2.get();
			array[3] = this.ctrlY2.get();
			array[4] = this.toX.get();
			array[5] = this.toY.get();
		}

		@Pure
		@Override
		public void toArray(double[] array) {
			assert array != null : "Array must be not null"; //$NON-NLS-1$
			assert array.length >= 6 : "Size of the array is too small"; //$NON-NLS-1$
			array[0] = this.ctrlX1.get();
			array[1] = this.ctrlY1.get();
			array[2] = this.ctrlX2.get();
			array[3] = this.ctrlY2.get();
			array[4] = this.toX.get();
			array[5] = this.toY.get();
		}

		@Pure
		@Override
		public IntegerProperty[] toArray() {
			return new IntegerProperty[] {this.ctrlX1, this.ctrlY1, this.ctrlX2, this.ctrlY2, this.toX, this.toY};
		}

		@Pure
		@Override
		public void toArray(IntegerProperty[] array) {
			assert array != null : "Array must be not null"; //$NON-NLS-1$
			assert array.length >= 6 : "Size of the array is too small"; //$NON-NLS-1$
			array[0] = this.ctrlX1;
			array[1] = this.ctrlY1;
			array[2] = this.ctrlX2;
			array[3] = this.ctrlY2;
			array[4] = this.toX;
			array[5] = this.toY;
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

		@Pure
		@Override
		public int getFromX() {
			return this.fromX.get();
		}

		@Pure
		@Override
		public int getFromY() {
			return this.fromY.get();
		}

		@Pure
		@Override
		public int getCtrlX1() {
			return this.ctrlX1.get();
		}

		@Pure
		@Override
		public int getCtrlY1() {
			return this.ctrlY1.get();
		}

		@Pure
		@Override
		public int getCtrlX2() {
			return this.ctrlX2.get();
		}

		@Pure
		@Override
		public int getCtrlY2() {
			return this.ctrlY2.get();
		}

		@Pure
		@Override
		public IntegerProperty fromXProperty() {
			return this.fromX;
		}

		@Pure
		@Override
		public IntegerProperty fromYProperty() {
			return this.fromY;
		}

		@Pure
		@Override
		public IntegerProperty ctrlX1Property() {
			return this.ctrlX1;
		}

		@Pure
		@Override
		public IntegerProperty ctrlY1Property() {
			return this.ctrlY1;
		}

		@Pure
		@Override
		public IntegerProperty ctrlX2Property() {
			return this.ctrlX2;
		}

		@Pure
		@Override
		public IntegerProperty ctrlY2Property() {
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
	static class ClosePathElement2ifx extends PathElement2ifx {

		private static final long serialVersionUID = 5424862699995343827L;

		private final IntegerProperty fromX;

		private final IntegerProperty fromY;

		/**
		 * @param fromx x coordinate of the origin point.
		 * @param fromy y coordinate of the origin point.
		 * @param tox x coordinate of the target point.
		 * @param toy y coordinate of the target point.
		 */
		ClosePathElement2ifx(IntegerProperty fromx, IntegerProperty fromy, IntegerProperty tox, IntegerProperty toy) {
			super(PathElementType.CLOSE, tox, toy);
			assert fromx != null : "Property fromX must be not null"; //$NON-NLS-1$
			assert fromy != null : "Property fromY must be not null"; //$NON-NLS-1$
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
			return MathUtil.isEpsilonEqual(this.fromX.get(), this.toX.get())
					&& MathUtil.isEpsilonEqual(this.fromY.get(), this.toY.get());
		}

		@Pure
		@Override
		public boolean isDrawable() {
			return !isEmpty();
		}

		@Pure
		@Override
		public void toArray(int[] array) {
			assert array != null : "Array must be not null"; //$NON-NLS-1$
			assert array.length >= 2 : "Size of the array is too small"; //$NON-NLS-1$
			array[0] = this.toX.get();
			array[1] = this.toY.get();
		}

		@Pure
		@Override
		public void toArray(double[] array) {
			assert array != null : "Array must be not null"; //$NON-NLS-1$
			assert array.length >= 2 : "Size of the array is too small"; //$NON-NLS-1$
			array[0] = this.toX.get();
			array[1] = this.toY.get();
		}

		@Pure
		@Override
		public IntegerProperty[] toArray() {
			return new IntegerProperty[] {this.toX, this.toY};
		}

		@Pure
		@Override
		public void toArray(IntegerProperty[] array) {
			assert array != null : "Array must be not null"; //$NON-NLS-1$
			assert array.length >= 2 : "Size of the array is too small"; //$NON-NLS-1$
			array[0] = this.toX;
			array[1] = this.toY;
		}

		@Pure
		@Override
		public String toString() {
			return "CLOSE"; //$NON-NLS-1$
		}

		@Pure
		@Override
		public int getFromX() {
			return this.fromX.get();
		}

		@Pure
		@Override
		public int getFromY() {
			return this.fromY.get();
		}

		@Pure
		@Override
		public int getCtrlX1() {
			return 0;
		}

		@Pure
		@Override
		public int getCtrlY1() {
			return 0;
		}

		@Pure
		@Override
		public int getCtrlX2() {
			return 0;
		}

		@Pure
		@Override
		public int getCtrlY2() {
			return 0;
		}

		@Pure
		@Override
		public IntegerProperty fromXProperty() {
			return this.fromX;
		}

		@Pure
		@Override
		public IntegerProperty fromYProperty() {
			return this.fromY;
		}

		@Pure
		@Override
		public IntegerProperty ctrlX1Property() {
			return null;
		}

		@Pure
		@Override
		public IntegerProperty ctrlY1Property() {
			return null;
		}

		@Pure
		@Override
		public IntegerProperty ctrlX2Property() {
			return null;
		}

		@Pure
		@Override
		public IntegerProperty ctrlY2Property() {
			return null;
		}

	}

}
