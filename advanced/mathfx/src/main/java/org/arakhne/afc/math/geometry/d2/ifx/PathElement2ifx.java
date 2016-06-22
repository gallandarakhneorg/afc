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

import org.arakhne.afc.math.geometry.MathFXAttributeNames;
import org.arakhne.afc.math.geometry.PathElementType;
import org.arakhne.afc.math.geometry.d2.afp.PathElement2afp;
import org.arakhne.afc.math.geometry.d2.ai.PathElement2ai;
import org.arakhne.afc.vmutil.ReflectionUtil;
import org.arakhne.afc.vmutil.asserts.AssertMessages;

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

	/** Type of the element.
	 */
	protected final PathElementType type;

	/** Target point.
	 */
	protected final Point2ifx to = new Point2ifx();

	/** Is Empty property.
	 */
	protected ReadOnlyBooleanWrapper isEmpty;

	/**
	 * @param type is the type of the element.
	 * @param tox the x coordinate of the target point.
	 * @param toy the x coordinate of the target point.
	 */
	PathElement2ifx(PathElementType type, IntegerProperty tox, IntegerProperty toy) {
	    assert type != null : AssertMessages.notNullParameter(0);
	    assert tox != null : AssertMessages.notNullParameter(1);
	    assert toy != null : AssertMessages.notNullParameter(2);
	    this.type = type;
	    this.to.x = tox;
	    this.to.y = toy;
	}

	/** Constructor by setting.
	 * @param type is the type of the element.
	 * @param toPoint the point to set as the target point.
	 */
	PathElement2ifx(PathElementType type, Point2ifx toPoint) {
		assert type != null : AssertMessages.notNullParameter(0);
		assert toPoint.x != null : AssertMessages.notNullParameter(1);
		assert toPoint.y != null : AssertMessages.notNullParameter(1);
		this.type = type;
		this.to.x = toPoint.x;
		this.to.y = toPoint.y;
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
		return this.to.x;
	}

	/** Replies the y coordinate of the target point property.
	 *
	 * @return the y coordinate.
	 */
	@Pure
	public IntegerProperty toYProperty() {
		return this.to.y;
	}

	@Override
	@Pure
	public final int getToX() {
		return this.to.ix();
	}

	@Override
	@Pure
	public final int getToY() {
		return this.to.iy();
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

		/** Constructor by setting.
		 * @param toPoint the point to set as the target point.
		 */
		MovePathElement2ifx(Point2ifx toPoint) {
			super(PathElementType.MOVE_TO, toPoint);
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
			bits = 31 * bits + this.type.hashCode();
			bits = 31 * bits + Integer.hashCode(getToX());
			bits = 31 * bits + Integer.hashCode(getToY());
			return bits ^ (bits >> 31);
		}

		@Pure
		@Override
		public BooleanProperty isEmptyProperty() {
			if (this.isEmpty == null) {
				this.isEmpty = new ReadOnlyBooleanWrapper(this, MathFXAttributeNames.IS_EMPTY, true);
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
			assert array != null : AssertMessages.notNullParameter();
			assert array.length >= 2 : AssertMessages.tooSmallArrayParameter(array.length, 2);
			array[0] = this.to.ix();
			array[1] = this.to.iy();
		}

		@Pure
		@Override
		public void toArray(double[] array) {
			assert array != null : AssertMessages.notNullParameter();
			assert array.length >= 2 : AssertMessages.tooSmallArrayParameter(array.length, 2);
			array[0] = this.to.ix();
			array[1] = this.to.iy();
		}

		@Pure
		@Override
		public void toArray(IntegerProperty[] array) {
			assert array != null : AssertMessages.notNullParameter();
			assert array.length >= 2 : AssertMessages.tooSmallArrayParameter(array.length, 2);
			array[0] = this.to.x;
			array[1] = this.to.y;
		}

		@Pure
		@Override
		public IntegerProperty[] toArray() {
			return new IntegerProperty[] {this.to.x, this.to.y};
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

		private final Point2ifx from = new Point2ifx();

		/**
		 * @param fromx x coordinate of the origin point.
		 * @param fromy y coordinate of the origin point.
		 * @param tox x coordinate of the target point.
		 * @param toy y coordinate of the target point.
		 */
		LinePathElement2ifx(IntegerProperty fromx, IntegerProperty fromy, IntegerProperty tox, IntegerProperty toy) {
		    super(PathElementType.LINE_TO, tox, toy);
		    assert fromx != null : AssertMessages.notNullParameter(0);
		    assert fromy != null : AssertMessages.notNullParameter(1);
		    this.from.x = fromx;
		    this.from.y = fromy;
		}

		/** Constructor by setting.
		 * @param fromPoint the point to set as the origin point.
		 * @param toPoint the point to set as the target point.
		 */
		LinePathElement2ifx(Point2ifx fromPoint, Point2ifx toPoint) {
			super(PathElementType.LINE_TO, toPoint);
			assert fromPoint.x != null : AssertMessages.notNullParameter(0);
			assert fromPoint.y != null : AssertMessages.notNullParameter(0);
			this.from.x = fromPoint.x;
			this.from.y = fromPoint.y;
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
			bits = 31 * bits + this.type.hashCode();
			bits = 31 * bits + Integer.hashCode(getToX());
			bits = 31 * bits + Integer.hashCode(getToY());
			bits = 31 * bits + Integer.hashCode(getFromX());
			bits = 31 * bits + Integer.hashCode(getFromY());
			return bits ^ (bits >> 31);
		}

		@Pure
		@Override
		public BooleanProperty isEmptyProperty() {
			if (this.isEmpty == null) {
				this.isEmpty = new ReadOnlyBooleanWrapper(this, MathFXAttributeNames.IS_EMPTY);
				this.isEmpty.bind(Bindings.createBooleanBinding(() ->
					fromXProperty().get() == toXProperty().get()
							&& fromYProperty().get() == toYProperty().get(),
						fromXProperty(), toXProperty(), fromYProperty(), toYProperty()));
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
			assert array != null : AssertMessages.notNullParameter();
			assert array.length >= 2 : AssertMessages.tooSmallArrayParameter(array.length, 2);
			array[0] = this.to.ix();
			array[1] = this.to.iy();
		}

		@Pure
		@Override
		public void toArray(double[] array) {
			assert array != null : AssertMessages.notNullParameter();
			assert array.length >= 2 : AssertMessages.tooSmallArrayParameter(array.length, 2);
			array[0] = this.to.ix();
			array[1] = this.to.iy();
		}

		@Pure
		@Override
		public IntegerProperty[] toArray() {
			return new IntegerProperty[] {this.to.xProperty(), this.to.yProperty()};
		}

		@Pure
		@Override
		public void toArray(IntegerProperty[] array) {
			assert array != null : AssertMessages.notNullParameter();
			assert array.length >= 2 : AssertMessages.tooSmallArrayParameter(array.length, 2);
			array[0] = this.to.xProperty();
			array[1] = this.to.yProperty();
		}

		@Pure
		@Override
		public int getFromX() {
			return this.from.ix();
		}

		@Pure
		@Override
		public int getFromY() {
			return this.from.iy();
		}

		@Pure
		@Override
		public IntegerProperty fromXProperty() {
			return this.from.xProperty();
		}

		@Pure
		@Override
		public IntegerProperty fromYProperty() {
			return this.from.yProperty();
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

		private final Point2ifx from = new Point2ifx();

		private final Point2ifx ctrl = new Point2ifx();

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
		    assert fromx != null : AssertMessages.notNullParameter(0);
		    assert fromy != null : AssertMessages.notNullParameter(1);
		    assert ctrlx != null : AssertMessages.notNullParameter(2);
		    assert ctrly != null : AssertMessages.notNullParameter(3);
		    this.from.x = fromx;
		    this.from.y = fromy;
		    this.ctrl.x = ctrlx;
		    this.ctrl.y = ctrly;
		}

		/** Constructor by setting.
		 * @param fromPoint the point to set as the origin point.
		 * @param ctrlPoint the point to set as the control point.
		 * @param toPoint the point to set as the target point.
		 */
		QuadPathElement2ifx(Point2ifx fromPoint, Point2ifx ctrlPoint, Point2ifx toPoint) {
			super(PathElementType.QUAD_TO, toPoint);
			assert fromPoint.x != null : AssertMessages.notNullParameter(0);
			assert fromPoint.y != null : AssertMessages.notNullParameter(0);
			assert ctrlPoint.x != null : AssertMessages.notNullParameter(1);
			assert ctrlPoint.y != null : AssertMessages.notNullParameter(1);
			this.from.x = fromPoint.x;
			this.from.y = fromPoint.y;
			this.ctrl.x = ctrlPoint.x;
			this.ctrl.y = ctrlPoint.y;
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
			bits = 31 * bits + this.type.hashCode();
			bits = 31 * bits + Integer.hashCode(getToX());
			bits = 31 * bits + Integer.hashCode(getToY());
			bits = 31 * bits + Integer.hashCode(getCtrlX1());
			bits = 31 * bits + Integer.hashCode(getCtrlY1());
			bits = 31 * bits + Integer.hashCode(getFromX());
			bits = 31 * bits + Integer.hashCode(getFromY());
			return bits ^ (bits >> 31);
		}

		@Pure
		@Override
		public BooleanProperty isEmptyProperty() {
			if (this.isEmpty == null) {
				this.isEmpty = new ReadOnlyBooleanWrapper(this, MathFXAttributeNames.IS_EMPTY);
				this.isEmpty.bind(Bindings.createBooleanBinding(() ->
					fromXProperty().get() == toXProperty().get()
							&& fromYProperty().get() == toYProperty().get()
							&& ctrlX1Property().get() == toXProperty().get()
							&& ctrlY1Property().get() == toYProperty().get(),
						fromXProperty(), toXProperty(), fromYProperty(), toYProperty()));
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
			assert array != null : AssertMessages.notNullParameter();
			assert array.length >= 4 : AssertMessages.tooSmallArrayParameter(array.length, 4);
			array[0] = this.ctrl.ix();
			array[1] = this.ctrl.iy();
			array[2] = this.to.ix();
			array[3] = this.to.iy();
		}

		@Pure
		@Override
		public void toArray(double[] array) {
			assert array != null : AssertMessages.notNullParameter();
			assert array.length >= 4 : AssertMessages.tooSmallArrayParameter(array.length, 4);
			array[0] = this.ctrl.ix();
			array[1] = this.ctrl.iy();
			array[2] = this.to.ix();
			array[3] = this.to.iy();
		}

		@Pure
		@Override
		public IntegerProperty[] toArray() {
			return new IntegerProperty[] {this.ctrl.x, this.ctrl.y, this.to.x, this.to.y};
		}

		@Pure
		@Override
		public void toArray(IntegerProperty[] array) {
			assert array != null : AssertMessages.notNullParameter();
			assert array.length >= 4 : AssertMessages.tooSmallArrayParameter(array.length, 4);
			array[0] = this.ctrl.x;
			array[1] = this.ctrl.y;
			array[2] = this.to.x;
			array[3] = this.to.y;
		}

		@Pure
		@Override
		public int getFromX() {
			return this.from.ix();
		}

		@Pure
		@Override
		public int getFromY() {
			return this.from.iy();
		}

		@Pure
		@Override
		public int getCtrlX1() {
			return this.ctrl.ix();
		}

		@Pure
		@Override
		public int getCtrlY1() {
			return this.ctrl.iy();
		}

		@Pure
		@Override
		public IntegerProperty fromXProperty() {
			return this.from.x;
		}

		@Pure
		@Override
		public IntegerProperty fromYProperty() {
			return this.from.y;
		}

		@Pure
		@Override
		public IntegerProperty ctrlX1Property() {
			return this.ctrl.x;
		}

		@Pure
		@Override
		public IntegerProperty ctrlY1Property() {
			return this.ctrl.y;
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

		private final Point2ifx from = new Point2ifx();

		private final Point2ifx ctrl1 = new Point2ifx();

		private final Point2ifx ctrl2 = new Point2ifx();

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
		    assert fromx != null : AssertMessages.notNullParameter(0);
		    assert fromy != null : AssertMessages.notNullParameter(1);
		    assert ctrlx1 != null : AssertMessages.notNullParameter(2);
		    assert ctrly1 != null : AssertMessages.notNullParameter(3);
		    assert ctrlx2 != null : AssertMessages.notNullParameter(4);
		    assert ctrly2 != null : AssertMessages.notNullParameter(5);
		    this.from.x = fromx;
		    this.from.y = fromy;
		    this.ctrl1.x = ctrlx1;
		    this.ctrl1.y = ctrly1;
		    this.ctrl2.x = ctrlx2;
		    this.ctrl2.y = ctrly2;
		}

		/** Constructor by setting.
		 * @param fromPoint the point to set as the origin point.
		 * @param ctrl1Point the point to set as the first control point.
		 * @param ctrl2Point the point to set as the second control point.
		 * @param toPoint the point to set as the target point.
		 */
		CurvePathElement2ifx(Point2ifx fromPoint, Point2ifx ctrl1Point,
				Point2ifx ctrl2Point, Point2ifx toPoint) {
			super(PathElementType.CURVE_TO, toPoint);
			assert fromPoint.x != null : AssertMessages.notNullParameter(0);
			assert fromPoint.y != null : AssertMessages.notNullParameter(0);
			assert ctrl1Point.x != null : AssertMessages.notNullParameter(1);
			assert ctrl1Point.y != null : AssertMessages.notNullParameter(1);
			assert ctrl2Point.x != null : AssertMessages.notNullParameter(2);
			assert ctrl2Point.y != null : AssertMessages.notNullParameter(2);
			this.from.x = fromPoint.x;
			this.from.y = fromPoint.y;
			this.ctrl1.x = ctrl1Point.x;
			this.ctrl1.y = ctrl1Point.y;
			this.ctrl2.x = ctrl2Point.x;
			this.ctrl2.y = ctrl2Point.y;
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
			bits = 31 * bits + this.type.hashCode();
			bits = 31 * bits + Integer.hashCode(getToX());
			bits = 31 * bits + Integer.hashCode(getToY());
			bits = 31 * bits + Integer.hashCode(getCtrlX1());
			bits = 31 * bits + Integer.hashCode(getCtrlY1());
			bits = 31 * bits + Integer.hashCode(getCtrlX2());
			bits = 31 * bits + Integer.hashCode(getCtrlY2());
			bits = 31 * bits + Integer.hashCode(getFromX());
			bits = 31 * bits + Integer.hashCode(getFromY());
			return bits ^ (bits >> 31);
		}

		@Pure
		@Override
		public BooleanProperty isEmptyProperty() {
			if (this.isEmpty == null) {
				this.isEmpty = new ReadOnlyBooleanWrapper(this, MathFXAttributeNames.IS_EMPTY);
				this.isEmpty.bind(Bindings.createBooleanBinding(() ->
					fromXProperty().get() == toXProperty().get()
							&& fromYProperty().get() == toYProperty().get()
							&& ctrlX1Property().get() == toXProperty().get()
							&& ctrlY1Property().get() == toYProperty().get()
							&& ctrlX2Property().get() == toXProperty().get()
							&& ctrlY2Property().get() == toYProperty().get(),
						fromXProperty(), toXProperty(), fromYProperty(), toYProperty()));
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
			assert array != null : AssertMessages.notNullParameter();
			assert array.length >= 6 : AssertMessages.tooSmallArrayParameter(array.length, 6);
			array[0] = this.ctrl1.ix();
			array[1] = this.ctrl1.iy();
			array[2] = this.ctrl2.ix();
			array[3] = this.ctrl2.iy();
			array[4] = this.to.ix();
			array[5] = this.to.iy();
		}

		@Pure
		@Override
		public void toArray(double[] array) {
			assert array != null : AssertMessages.notNullParameter();
			assert array.length >= 6 : AssertMessages.tooSmallArrayParameter(array.length, 6);
			array[0] = this.ctrl1.ix();
			array[1] = this.ctrl1.iy();
			array[2] = this.ctrl2.ix();
			array[3] = this.ctrl2.iy();
			array[4] = this.to.ix();
			array[5] = this.to.iy();
		}

		@Pure
		@Override
		public IntegerProperty[] toArray() {
			return new IntegerProperty[] {this.ctrl1.x, this.ctrl1.y, this.ctrl2.x, this.ctrl2.y, this.to.x, this.to.y};
		}

		@Pure
		@Override
		public void toArray(IntegerProperty[] array) {
			assert array != null : AssertMessages.notNullParameter();
			assert array.length >= 6 : AssertMessages.tooSmallArrayParameter(array.length, 6);
			array[0] = this.ctrl1.x;
			array[1] = this.ctrl1.y;
			array[2] = this.ctrl2.x;
			array[3] = this.ctrl2.y;
			array[4] = this.to.x;
			array[5] = this.to.y;
		}

		@Pure
		@Override
		public int getFromX() {
			return this.from.ix();
		}

		@Pure
		@Override
		public int getFromY() {
			return this.from.iy();
		}

		@Pure
		@Override
		public int getCtrlX1() {
			return this.ctrl1.ix();
		}

		@Pure
		@Override
		public int getCtrlY1() {
			return this.ctrl1.iy();
		}

		@Pure
		@Override
		public int getCtrlX2() {
			return this.ctrl2.ix();
		}

		@Pure
		@Override
		public int getCtrlY2() {
			return this.ctrl2.iy();
		}

		@Pure
		@Override
		public IntegerProperty fromXProperty() {
			return this.from.x;
		}

		@Pure
		@Override
		public IntegerProperty fromYProperty() {
			return this.from.y;
		}

		@Pure
		@Override
		public IntegerProperty ctrlX1Property() {
			return this.ctrl1.x;
		}

		@Pure
		@Override
		public IntegerProperty ctrlY1Property() {
			return this.ctrl1.y;
		}

		@Pure
		@Override
		public IntegerProperty ctrlX2Property() {
			return this.ctrl2.x;
		}

		@Pure
		@Override
		public IntegerProperty ctrlY2Property() {
			return this.ctrl2.y;
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

		private final Point2ifx from = new Point2ifx();

		/**
		 * @param fromx x coordinate of the origin point.
		 * @param fromy y coordinate of the origin point.
		 * @param tox x coordinate of the target point.
		 * @param toy y coordinate of the target point.
		 */
		ClosePathElement2ifx(IntegerProperty fromx, IntegerProperty fromy, IntegerProperty tox, IntegerProperty toy) {
		    super(PathElementType.CLOSE, tox, toy);
		    assert fromx != null : AssertMessages.notNullParameter(0);
		    assert fromy != null : AssertMessages.notNullParameter(1);
		    this.from.x = fromx;
		    this.from.y = fromy;
		}

		/** Constructor by setting.
		 * @param fromPoint the point to set as the origin point.
		 * @param toPoint the point to set as the target point.
		 */
		ClosePathElement2ifx(Point2ifx fromPoint, Point2ifx toPoint) {
			super(PathElementType.CLOSE, toPoint);
			assert fromPoint.x != null : AssertMessages.notNullParameter(0);
			assert fromPoint.y != null : AssertMessages.notNullParameter(0);
			this.from.x = fromPoint.x;
			this.from.y = fromPoint.y;

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
			bits = 31 * bits + this.type.hashCode();
			bits = 31 * bits + Integer.hashCode(getToX());
			bits = 31 * bits + Integer.hashCode(getToY());
			bits = 31 * bits + Integer.hashCode(getFromX());
			bits = 31 * bits + Integer.hashCode(getFromY());
			return bits ^ (bits >> 31);
		}

		@Pure
		@Override
		public BooleanProperty isEmptyProperty() {
			if (this.isEmpty == null) {
				this.isEmpty = new ReadOnlyBooleanWrapper(this, MathFXAttributeNames.IS_EMPTY);
				this.isEmpty.bind(Bindings.createBooleanBinding(() ->
					fromXProperty().get() == toXProperty().get()
							&& fromYProperty().get() == toYProperty().get(),
						fromXProperty(), toXProperty(), fromYProperty(), toYProperty()));
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
			assert array != null : AssertMessages.notNullParameter();
			assert array.length >= 2 : AssertMessages.tooSmallArrayParameter(array.length, 2);
			array[0] = this.to.ix();
			array[1] = this.to.iy();
		}

		@Pure
		@Override
		public void toArray(double[] array) {
			assert array != null : AssertMessages.notNullParameter();
			assert array.length >= 2 : AssertMessages.tooSmallArrayParameter(array.length, 2);
			array[0] = this.to.ix();
			array[1] = this.to.iy();
		}

		@Pure
		@Override
		public IntegerProperty[] toArray() {
			return new IntegerProperty[] {this.to.x, this.to.y};
		}

		@Pure
		@Override
		public void toArray(IntegerProperty[] array) {
			assert array != null : AssertMessages.notNullParameter();
			assert array.length >= 2 : AssertMessages.tooSmallArrayParameter(array.length, 2);
			array[0] = this.to.x;
			array[1] = this.to.y;
		}

		@Pure
		@Override
		public int getFromX() {
			return this.from.ix();
		}

		@Pure
		@Override
		public int getFromY() {
			return this.from.iy();
		}

		@Pure
		@Override
		public IntegerProperty fromXProperty() {
			return this.from.x;
		}

		@Pure
		@Override
		public IntegerProperty fromYProperty() {
			return this.from.y;
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

		private final Point2ifx from = new Point2ifx();

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
		@SuppressWarnings("checkstyle:magicnumber")
		ArcPathElement2ifx(IntegerProperty fromx, IntegerProperty fromy, IntegerProperty tox, IntegerProperty toy,
		        IntegerProperty xradius, IntegerProperty yradius, DoubleProperty xrotation,
		        BooleanProperty largeArcFlag, BooleanProperty sweepFlag) {
		    super(PathElementType.ARC_TO, tox, toy);
		    assert fromx != null : AssertMessages.notNullParameter(0);
		    assert fromy != null : AssertMessages.notNullParameter(1);
		    assert xradius != null : AssertMessages.notNullParameter(4);
		    assert yradius != null : AssertMessages.notNullParameter(5);
		    assert xrotation != null : AssertMessages.notNullParameter(6);
		    assert largeArcFlag != null : AssertMessages.notNullParameter(7);
		    assert sweepFlag != null : AssertMessages.notNullParameter(8);
		    this.from.x = fromx;
		    this.from.y = fromy;
		    this.radiusX = xradius;
		    this.radiusY = yradius;
		    this.rotationX = xrotation;
		    this.largeArcFlag = largeArcFlag;
		    this.sweepFlag = sweepFlag;
		}

		/** Constructor by setting.
		 * @param fromPoint the point to set as the origin point.
		 * @param toPoint the point to set as the target point.
		 * @param xradius radius of the ellipse along its x axis.
		 * @param yradius radius of the ellipse along its y axis.
		 * @param xrotation rotation (in radians) of the ellipse's x axis.
		 * @param largeArcFlag <code>true</code> iff the path will sweep the long way around the ellipse.
		 * @param sweepFlag <code>true</code> iff the path will sweep clockwise around the ellipse.
		 */
		@SuppressWarnings("checkstyle:magicnumber")
		ArcPathElement2ifx(Point2ifx fromPoint, Point2ifx toPoint,
				IntegerProperty xradius, IntegerProperty yradius, DoubleProperty xrotation,
				BooleanProperty largeArcFlag, BooleanProperty sweepFlag) {
			super(PathElementType.ARC_TO, toPoint);
			assert fromPoint.x != null : AssertMessages.notNullParameter(0);
			assert fromPoint.y != null : AssertMessages.notNullParameter(0);
			assert xradius != null : AssertMessages.notNullParameter(2);
			assert yradius != null : AssertMessages.notNullParameter(3);
			assert xrotation != null : AssertMessages.notNullParameter(4);
			assert largeArcFlag != null : AssertMessages.notNullParameter(5);
			assert sweepFlag != null : AssertMessages.notNullParameter(6);
			this.from.x = fromPoint.x;
			this.from.y = fromPoint.y;
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
			bits = 31L * bits + Integer.hashCode(getToX());
			bits = 31L * bits + Integer.hashCode(getToY());
			bits = 31L * bits + Integer.hashCode(getRadiusX());
			bits = 31L * bits + Integer.hashCode(getRadiusY());
			bits = 31L * bits + Double.doubleToLongBits(getRotationX());
			bits = 31L * bits + Boolean.hashCode(getLargeArcFlag());
			bits = 31L * bits + Boolean.hashCode(getSweepFlag());
			return (int) (bits ^ (bits >> 31));
		}

		@Pure
		@Override
		public BooleanProperty isEmptyProperty() {
			if (this.isEmpty == null) {
				this.isEmpty = new ReadOnlyBooleanWrapper(this, MathFXAttributeNames.IS_EMPTY);
				this.isEmpty.bind(Bindings.createBooleanBinding(() ->
					fromXProperty().get() == toXProperty().get()
							&& fromYProperty().get() == toYProperty().get(),
						fromXProperty(), toXProperty(), fromYProperty(), toYProperty()));
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
			assert array != null : AssertMessages.notNullParameter();
			assert array.length >= 2 : AssertMessages.tooSmallArrayParameter(array.length, 2);
			array[0] = this.to.ix();
			array[1] = this.to.iy();
		}

		@Pure
		@Override
		public void toArray(IntegerProperty[] array) {
			assert array != null : AssertMessages.notNullParameter();
			assert array.length >= 2 : AssertMessages.tooSmallArrayParameter(array.length, 2);
			array[0] = this.to.x;
			array[1] = this.to.y;
		}

		@Pure
		@Override
		public void toArray(double[] array) {
			assert array != null : AssertMessages.notNullParameter();
			assert array.length >= 2 : AssertMessages.tooSmallArrayParameter(array.length, 2);
			array[0] = this.to.getX();
			array[1] = this.to.getY();
		}

		@Pure
		@Override
		public IntegerProperty[] toArray() {
			return new IntegerProperty[] {this.to.x, this.to.y};
		}

		@Pure
		@Override
		public int getFromX() {
			return this.from.ix();
		}

		@Pure
		@Override
		public int getFromY() {
			return this.from.iy();
		}

		@Pure
		@Override
		public IntegerProperty fromXProperty() {
			return this.from.x;
		}

		@Pure
		@Override
		public IntegerProperty fromYProperty() {
			return this.from.y;
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
