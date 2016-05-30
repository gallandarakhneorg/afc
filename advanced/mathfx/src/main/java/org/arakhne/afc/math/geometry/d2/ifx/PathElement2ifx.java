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

import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.geometry.PathElementType;
import org.arakhne.afc.math.geometry.d2.afp.PathElement2afp;
import org.arakhne.afc.math.geometry.d2.ai.PathElement2ai;

/** An element of the path with 2 integer FX properties.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
@SuppressWarnings({"checkstyle:magicnumber", "static-method"})
public abstract class PathElement2ifx implements PathElement2ai {

	private static final long serialVersionUID = -5532787413347691238L;
    /**+
     * Literal constant.
     */
    private static final String IS_EMPTY = "isEmpty";
    /**+
     * Literal constant.
     */
    private static final String ARRAY_NOT_NULL = "Array must be not null";
    /**+
     * Literal constant.
     */
    private static final String ARRAY_SIZE_SMALL = "Array size is too small";
    /**+
     * Literal constant.
     */
    private static final String FROMX_NOT_NULL = "Property fromX must be not null";
    /**+
     * Literal constant.
     */
    private static final String FROMY_NOT_NULL = "Property fromY must be not null";
	/** Type of the element.
	 */
	protected final PathElementType type;

	/** Target point.
	 */
	protected final IntegerProperty toX;

	/** Target point.
	 */
	protected final IntegerProperty toY;

	/** Is Empty property.
	 */
	protected ReadOnlyBooleanWrapper isEmpty;

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

	@Pure
	@Override
	public abstract boolean equals(Object obj);

	@Pure
	@Override
	public abstract int hashCode();

	/** Replies the property that indicates if this patth element is empty.
	 *
	 * @return the isEmpty property.
	 */
	public abstract BooleanProperty isEmptyProperty();

	@Override
	public boolean isEmpty() {
		return isEmptyProperty().get();
	}

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

	@Override
	public int getRadiusX() {
		return 0;
	}

	@Override
	public int getRadiusY() {
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

	/** Replies the property for the x coordinate of the first control point.
	 *
	 * @return the x coordinate, or <code>null</code> if the type is {@link PathElementType#MOVE_TO},
	 * {@link PathElementType#LINE_TO}, or {@link PathElementType#CLOSE}.
	 */
	@Pure
	public IntegerProperty ctrlX1Property() {
		return null;
	}

	/** Replies the property for the y coordinate of the first control point.
	 *
	 * @return the y coordinate, or {@link Double#NaN} if the type is {@link PathElementType#MOVE_TO},
	 * {@link PathElementType#LINE_TO}, or {@link PathElementType#CLOSE}.
	 */
	@Pure
	public IntegerProperty ctrlY1Property() {
		return null;
	}

	/** Replies the property for the x coordinate of the second control point.
	 *
	 * @return the x coordinate, or <code>null</code> if the type is {@link PathElementType#MOVE_TO},
	 * {@link PathElementType#LINE_TO}, {@link PathElementType#QUAD_TO}, or {@link PathElementType#CLOSE}.
	 */
	@Pure
	public IntegerProperty ctrlX2Property() {
		return null;
	}

	/** Replies the property for the y coordinate of the second control point.
	 *
	 * @return the y coordinate, or <code>null</code> if the type is {@link PathElementType#MOVE_TO},
	 * {@link PathElementType#LINE_TO}, {@link PathElementType#QUAD_TO}, or {@link PathElementType#CLOSE}.
	 */
	@Pure
	public IntegerProperty ctrlY2Property() {
		return null;
	}

	/** Replies the property for the radius along the x axis.
	 *
	 * @return the x radius, or <code>null</code> if the type is not {@link PathElementType#ARC_TO}.
	 */
	@Pure
	public IntegerProperty radiusXProperty() {
		return null;
	}

	/** Replies the property for the radius along the y axis.
	 *
	 * @return the y radius, or <code>null</code> if the type is not {@link PathElementType#ARC_TO}.
	 */
	@Pure
	public IntegerProperty radiusYProperty() {
		return null;
	}

	/** Replies the property for the rotation of the x axis.
	 *
	 * @return the x-axis rotation, or <code>null</code> if the type is not {@link PathElementType#ARC_TO}.
	 */
	@Pure
	public DoubleProperty rotationXProperty() {
		return null;
	}

	/** Replies the property for the large ellipse arc flag.
	 *
	 * @return the flag, or <code>null</code> if the type is not {@link PathElementType#ARC_TO}.
	 */
	@Pure
	public BooleanProperty largeArcFlagProperty() {
		return null;
	}

	/** Replies the property for the sweep ellipse arc flag.
	 *
	 * @return the flag, or <code>null</code> if the type is not {@link PathElementType#ARC_TO}.
	 */
	@Pure
	public BooleanProperty sweepFlagProperty() {
		return null;
	}

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
		public BooleanProperty isEmptyProperty() {
			if (this.isEmpty == null) {
				this.isEmpty = new ReadOnlyBooleanWrapper(this, IS_EMPTY, true); //$NON-NLS-1$
			}
			return this.isEmpty;
		}

		@Pure
		@Override
		public boolean isDrawable() {
			return false;
		}

		@Pure
		@Override
		public void toArray(int[] array) {
			assert array != null : ARRAY_NOT_NULL; //$NON-NLS-1$
			assert array.length >= 2 :  ARRAY_SIZE_SMALL; //$NON-NLS-1$
			array[0] = this.toX.get();
			array[1] = this.toY.get();
		}

		@Pure
		@Override
		public void toArray(double[] array) {
			assert array != null : ARRAY_NOT_NULL; //$NON-NLS-1$
			assert array.length >= 2 :  ARRAY_SIZE_SMALL; //$NON-NLS-1$
			array[0] = this.toX.get();
			array[1] = this.toY.get();
		}

		@Pure
		@Override
		public void toArray(IntegerProperty[] array) {
			assert array != null : ARRAY_NOT_NULL; //$NON-NLS-1$
			assert array.length >= 2 :  ARRAY_SIZE_SMALL; //$NON-NLS-1$
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
		public IntegerProperty fromXProperty() {
			return null;
		}

		@Pure
		@Override
		public IntegerProperty fromYProperty() {
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
			assert fromx != null : FROMX_NOT_NULL; //$NON-NLS-1$
			assert fromy != null : FROMY_NOT_NULL; //$NON-NLS-1$
			this.fromX = fromx;
			this.fromY = fromy;
		}

		@Pure
		@Override
		public boolean equals(Object obj) {
			try {
				final PathElement2ai elt = (PathElement2ai) obj;
				return getType() == elt.getType()
						&& getToX() == elt.getToX()
						&& getToY() == elt.getToY()
						&& getFromX() == elt.getFromX()
						&& getFromY() == elt.getFromY();
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
		public BooleanProperty isEmptyProperty() {
			if (this.isEmpty == null) {
				this.isEmpty = new ReadOnlyBooleanWrapper(this, IS_EMPTY); //$NON-NLS-1$
				this.isEmpty.bind(Bindings.createBooleanBinding(() -> {
					return fromXProperty().get() == toXProperty().get()
							&& fromYProperty().get() == toYProperty().get();
				}, fromXProperty(), toXProperty(), fromYProperty(), toYProperty()));
			}
			return this.isEmpty;
		}

		@Pure
		@Override
		public boolean isDrawable() {
			return !isEmpty();
		}

		@Pure
		@Override
		public void toArray(int[] array) {
			assert array != null : ARRAY_NOT_NULL; //$NON-NLS-1$
			assert array.length >= 2 :  ARRAY_SIZE_SMALL; //$NON-NLS-1$
			array[0] = this.toX.get();
			array[1] = this.toY.get();
		}

		@Pure
		@Override
		public void toArray(double[] array) {
			assert array != null : ARRAY_NOT_NULL; //$NON-NLS-1$
			assert array.length >= 2 :  ARRAY_SIZE_SMALL; //$NON-NLS-1$
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
			assert array != null : ARRAY_NOT_NULL; //$NON-NLS-1$
			assert array.length >= 2 :  ARRAY_SIZE_SMALL; //$NON-NLS-1$
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
		public IntegerProperty fromXProperty() {
			return this.fromX;
		}

		@Pure
		@Override
		public IntegerProperty fromYProperty() {
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
			assert fromx != null : FROMX_NOT_NULL; //$NON-NLS-1$
			assert fromy != null : FROMY_NOT_NULL; //$NON-NLS-1$
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
				return getType() == elt.getType()
						&& getToX() == elt.getToX()
						&& getToY() == elt.getToY()
						&& getCtrlX1() == elt.getCtrlX1()
						&& getCtrlY1() == elt.getCtrlY1()
						&& getFromX() == elt.getFromX()
						&& getFromY() == elt.getFromY();
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
		public BooleanProperty isEmptyProperty() {
			if (this.isEmpty == null) {
				this.isEmpty = new ReadOnlyBooleanWrapper(this, IS_EMPTY); //$NON-NLS-1$
				this.isEmpty.bind(Bindings.createBooleanBinding(() -> {
					return fromXProperty().get() == toXProperty().get()
							&& fromYProperty().get() == toYProperty().get()
							&& ctrlX1Property().get() == toXProperty().get()
							&& ctrlY1Property().get() == toYProperty().get();
				}, fromXProperty(), toXProperty(), fromYProperty(), toYProperty()));
			}
			return this.isEmpty;
		}

		@Pure
		@Override
		public boolean isDrawable() {
			return !isEmpty();
		}

		@Pure
		@Override
		public void toArray(int[] array) {
			assert array != null : ARRAY_NOT_NULL; //$NON-NLS-1$
			assert array.length >= 4 :  ARRAY_SIZE_SMALL; //$NON-NLS-1$
			array[0] = this.ctrlX.get();
			array[1] = this.ctrlY.get();
			array[2] = this.toX.get();
			array[3] = this.toY.get();
		}

		@Pure
		@Override
		public void toArray(double[] array) {
			assert array != null : ARRAY_NOT_NULL; //$NON-NLS-1$
			assert array.length >= 4 :  ARRAY_SIZE_SMALL; //$NON-NLS-1$
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
			assert array != null : ARRAY_NOT_NULL; //$NON-NLS-1$
			assert array.length >= 4 :  ARRAY_SIZE_SMALL; //$NON-NLS-1$
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
			assert fromx != null : FROMX_NOT_NULL; //$NON-NLS-1$
			assert fromy != null : FROMY_NOT_NULL; //$NON-NLS-1$
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
				return getType() == elt.getType()
						&& getToX() == elt.getToX()
						&& getToY() == elt.getToY()
						&& getCtrlX1() == elt.getCtrlX1()
						&& getCtrlY1() == elt.getCtrlY1()
						&& getCtrlX2() == elt.getCtrlX2()
						&& getCtrlY2() == elt.getCtrlY2()
						&& getFromX() == elt.getFromX()
						&& getFromY() == elt.getFromY();
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
		public BooleanProperty isEmptyProperty() {
			if (this.isEmpty == null) {
				this.isEmpty = new ReadOnlyBooleanWrapper(this, IS_EMPTY); //$NON-NLS-1$
				this.isEmpty.bind(Bindings.createBooleanBinding(() -> {
					return fromXProperty().get() == toXProperty().get()
							&& fromYProperty().get() == toYProperty().get()
							&& ctrlX1Property().get() == toXProperty().get()
							&& ctrlY1Property().get() == toYProperty().get()
							&& ctrlX2Property().get() == toXProperty().get()
							&& ctrlY2Property().get() == toYProperty().get();
				}, fromXProperty(), toXProperty(), fromYProperty(), toYProperty()));
			}
			return this.isEmpty;
		}

		@Pure
		@Override
		public boolean isDrawable() {
			return !isEmpty();
		}

		@Pure
		@Override
		public void toArray(int[] array) {
			assert array != null : ARRAY_NOT_NULL; //$NON-NLS-1$
			assert array.length >= 6 :  ARRAY_SIZE_SMALL; //$NON-NLS-1$
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
			assert array != null : ARRAY_NOT_NULL; //$NON-NLS-1$
			assert array.length >= 6 :  ARRAY_SIZE_SMALL; //$NON-NLS-1$
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
			assert array != null : ARRAY_NOT_NULL; //$NON-NLS-1$
			assert array.length >= 6 :  ARRAY_SIZE_SMALL; //$NON-NLS-1$
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
			assert fromx != null : FROMX_NOT_NULL; //$NON-NLS-1$
			assert fromy != null : FROMY_NOT_NULL; //$NON-NLS-1$
			this.fromX = fromx;
			this.fromY = fromy;

		}

		@Pure
		@Override
		public boolean equals(Object obj) {
			try {
				final PathElement2ai elt = (PathElement2ai) obj;
				return getType() == elt.getType()
						&& getToX() == elt.getToX()
						&& getToY() == elt.getToY()
						&& getFromX() == elt.getFromX()
						&& getFromY() == elt.getFromY();
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
		public BooleanProperty isEmptyProperty() {
			if (this.isEmpty == null) {
				this.isEmpty = new ReadOnlyBooleanWrapper(this, IS_EMPTY); //$NON-NLS-1$
				this.isEmpty.bind(Bindings.createBooleanBinding(() -> {
					return fromXProperty().get() == toXProperty().get()
							&& fromYProperty().get() == toYProperty().get();
				}, fromXProperty(), toXProperty(), fromYProperty(), toYProperty()));
			}
			return this.isEmpty;
		}

		@Pure
		@Override
		public boolean isDrawable() {
			return !isEmpty();
		}

		@Pure
		@Override
		public void toArray(int[] array) {
			assert array != null : ARRAY_NOT_NULL; //$NON-NLS-1$
			assert array.length >= 2 :  ARRAY_SIZE_SMALL; //$NON-NLS-1$
			array[0] = this.toX.get();
			array[1] = this.toY.get();
		}

		@Pure
		@Override
		public void toArray(double[] array) {
			assert array != null : ARRAY_NOT_NULL; //$NON-NLS-1$
			assert array.length >= 2 :  ARRAY_SIZE_SMALL; //$NON-NLS-1$
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
			assert array != null : ARRAY_NOT_NULL; //$NON-NLS-1$
			assert array.length >= 2 :  ARRAY_SIZE_SMALL; //$NON-NLS-1$
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
		public IntegerProperty fromXProperty() {
			return this.fromX;
		}

		@Pure
		@Override
		public IntegerProperty fromYProperty() {
			return this.fromY;
		}

	}

	/** An element of the path that represents a <code>ARC_TO</code>.
	 *
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 13.0
	 */
	static class ArcPathElement2ifx extends PathElement2ifx {

		private static final long serialVersionUID = 1191891479706357600L;

		private final IntegerProperty fromX;

		private final IntegerProperty fromY;

		private final IntegerProperty radiusX;

		private final IntegerProperty radiusY;

		private final DoubleProperty rotationX;

		private final BooleanProperty largeArcFlag;

		private final BooleanProperty sweepFlag;

		/**
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
		ArcPathElement2ifx(IntegerProperty fromx, IntegerProperty fromy, IntegerProperty tox, IntegerProperty toy,
				IntegerProperty xradius, IntegerProperty yradius, DoubleProperty xrotation,
				BooleanProperty largeArcFlag, BooleanProperty sweepFlag) {
			super(PathElementType.ARC_TO, tox, toy);
			assert fromx != null : "fromx must be not null"; //$NON-NLS-1$
			assert fromy != null : "fromy must be not null"; //$NON-NLS-1$
			assert xradius != null : "xradius must be not null"; //$NON-NLS-1$
			assert yradius != null : "yradius must be not null"; //$NON-NLS-1$
			assert xrotation != null : "xrotation must be not null"; //$NON-NLS-1$
			assert largeArcFlag != null : "largeArcFlag must be not null"; //$NON-NLS-1$
			assert sweepFlag != null : "sweepFlag must be not null"; //$NON-NLS-1$
			this.fromX = fromx;
			this.fromY = fromy;
			this.radiusX = xradius;
			this.radiusY = yradius;
			this.rotationX = xrotation;
			this.largeArcFlag = largeArcFlag;
			this.sweepFlag = sweepFlag;
		}

		@Pure
		@Override
		public boolean equals(Object obj) {
			try {
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
			bits = 31L * bits + this.type.ordinal();
			bits = 31L * bits + getToX();
			bits = 31L * bits + getToY();
			bits = 31L * bits + getRadiusX();
			bits = 31L * bits + getRadiusY();
			bits = 31L * bits + Double.doubleToLongBits(getRotationX());
			bits = 31L * bits + Boolean.hashCode(getLargeArcFlag());
			bits = 31L * bits + Boolean.hashCode(getSweepFlag());
			return (int) (bits ^ (bits >> 32));
		}

		@Pure
		@Override
		public BooleanProperty isEmptyProperty() {
			if (this.isEmpty == null) {
				this.isEmpty = new ReadOnlyBooleanWrapper(this, IS_EMPTY); //$NON-NLS-1$
				this.isEmpty.bind(Bindings.createBooleanBinding(() -> {
					return fromXProperty().get() == toXProperty().get()
							&& fromYProperty().get() == toYProperty().get();
				}, fromXProperty(), toXProperty(), fromYProperty(), toYProperty()));
			}
			return this.isEmpty;
		}

		@Pure
		@Override
		public boolean isDrawable() {
			return !isEmpty();
		}

		@Pure
		@Override
		public void toArray(int[] array) {
			assert array != null : ARRAY_NOT_NULL; //$NON-NLS-1$
			assert array.length >= 2 :  ARRAY_SIZE_SMALL; //$NON-NLS-1$
			array[0] = this.toX.intValue();
			array[1] = this.toY.intValue();
		}

		@Pure
		@Override
		public void toArray(IntegerProperty[] array) {
			assert array != null : ARRAY_NOT_NULL; //$NON-NLS-1$
			assert array.length >= 2 :  ARRAY_SIZE_SMALL; //$NON-NLS-1$
			array[0] = this.toX;
			array[1] = this.toY;
		}

		@Pure
		@Override
		public void toArray(double[] array) {
			assert array != null : ARRAY_NOT_NULL; //$NON-NLS-1$
			assert array.length >= 2 :  ARRAY_SIZE_SMALL; //$NON-NLS-1$
			array[0] = this.toX.doubleValue();
			array[1] = this.toY.doubleValue();
		}

		@Pure
		@Override
		public IntegerProperty[] toArray() {
			return new IntegerProperty[] {this.toX, this.toY};
		}

		@Pure
		@Override
		public String toString() {
			return "ARC(" //$NON-NLS-1$
					+ getRadiusX() + "x" //$NON-NLS-1$
					+ getRadiusY() + "|" //$NON-NLS-1$
					+ getRotationX() + "x" //$NON-NLS-1$
					+ getLargeArcFlag() + "x" //$NON-NLS-1$
					+ getSweepFlag() + "|" //$NON-NLS-1$
					+ getToX() + "x" //$NON-NLS-1$
					+ getToY() + ")"; //$NON-NLS-1$
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
		public IntegerProperty fromXProperty() {
			return this.fromX;
		}

		@Pure
		@Override
		public IntegerProperty fromYProperty() {
			return this.fromY;
		}

		@Override
		public int getRadiusX() {
			return this.radiusX.get();
		}

		@Override
		public int getRadiusY() {
			return this.radiusY.get();
		}

		@Override
		public double getRotationX() {
			return this.rotationX.get();
		}

		@Override
		public boolean getSweepFlag() {
			return this.sweepFlag.get();
		}

		@Override
		public boolean getLargeArcFlag() {
			return this.largeArcFlag.get();
		}

		@Override
		public IntegerProperty radiusXProperty() {
			return this.radiusX;
		}

		@Override
		public IntegerProperty radiusYProperty() {
			return this.radiusY;
		}

		@Override
		public DoubleProperty rotationXProperty() {
			return this.rotationX;
		}

		@Override
		public BooleanProperty largeArcFlagProperty() {
			return this.largeArcFlag;
		}

		@Override
		public BooleanProperty sweepFlagProperty() {
			return this.sweepFlag;
		}

	}

}
