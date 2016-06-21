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

package org.arakhne.afc.math.geometry.d2.dfx;

import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.MathUtil;
import org.arakhne.afc.math.geometry.MathFXAttributeNames;
import org.arakhne.afc.math.geometry.PathElementType;
import org.arakhne.afc.math.geometry.d2.afp.PathElement2afp;
import org.arakhne.afc.vmutil.ReflectionUtil;
import org.arakhne.afc.vmutil.asserts.AssertMessages;

/** An element of the path.
 *
 * @author $Author: sgalland$
 * @author $Author: hjaffali$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
// TODO : create new message and check property initialisation in constructor accepting points.
@SuppressWarnings({"checkstyle:magicnumber", "static-method"})
public abstract class PathElement2dfx implements PathElement2afp {
	private static final long serialVersionUID = 1724746568685625149L;

	/** Type of the element.
	 */
	protected final PathElementType type;

	/** Target point.
	 */
	protected Point2dfx to = new Point2dfx();

	/** Is Empty property.
	 */
	protected ReadOnlyBooleanWrapper isEmpty;

	/**
	 * @param type is the type of the element.
	 * @param tox the x coordinate of the target point.
	 * @param toy the x coordinate of the target point.
	 */
	PathElement2dfx(PathElementType type, DoubleProperty tox, DoubleProperty toy) {
	    assert type != null : AssertMessages.notNullParameter(0);
	    assert tox != null : AssertMessages.notNullParameter(1);
	    assert toy != null : AssertMessages.notNullParameter(2);
	    this.type = type;
	    this.to.x = tox;
	    this.to.y = toy;
	}

	/**
	 * @param type is the type of the element.
	 * @param toPoint the target point.
	 */
	PathElement2dfx(PathElementType type, Point2dfx toPoint) {
		assert type != null : AssertMessages.notNullParameter(0);
		assert toPoint != null : AssertMessages.notNullParameter(1);
		this.type = type;
		this.to = toPoint;
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

	@Pure
	@Override
	public final double getToX() {
		return this.to.getX();
	}

	@Pure
	@Override
	public final double getToY() {
		return this.to.getY();
	}

	/** Replies the property for the x coordinate of the starting point.
	 *
	 * @return the x coordinate, or <code>null</code> if the type is {@link PathElementType#MOVE_TO}.
	 */
	@Pure
	public abstract DoubleProperty fromXProperty();

	/** Replies the property for the y coordinate of the starting point.
	 *
	 * @return the y coordinate, or <code>null</code> if the type is {@link PathElementType#MOVE_TO}.
	 */
	@Pure
	public abstract DoubleProperty fromYProperty();

	/** Replies the property for the x coordinate of the target point.
	 *
	 * @return the x coordinate.
	 */
	@Pure
	public DoubleProperty toXProperty() {
		return this.to.xProperty();
	}

	/** Replies the property for the y coordinate of the target point.
	 *
	 * @return the y coordinate.
	 */
	@Pure
	public DoubleProperty toYProperty() {
		return this.to.yProperty();
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
	public abstract void toArray(DoubleProperty[] array);

	/** Copy the coords into an array, except the source point.
	 *
	 * @return the array of the points, except the source point.
	 */
	@Pure
	public abstract DoubleProperty[] toArray();

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

	/** Replies the property for the x coordinate of the first control point.
	 *
	 * @return the x coordinate, or <code>null</code> if the type is {@link PathElementType#MOVE_TO},
	 * {@link PathElementType#LINE_TO}, or {@link PathElementType#CLOSE}.
	 */
	@Pure
	public DoubleProperty ctrlX1Property() {
		return null;
	}

	/** Replies the property for the y coordinate of the first control point.
	 *
	 * @return the y coordinate, or {@link Double#NaN} if the type is {@link PathElementType#MOVE_TO},
	 * {@link PathElementType#LINE_TO}, or {@link PathElementType#CLOSE}.
	 */
	@Pure
	public DoubleProperty ctrlY1Property() {
		return null;
	}

	/** Replies the property for the x coordinate of the second control point.
	 *
	 * @return the x coordinate, or <code>null</code> if the type is {@link PathElementType#MOVE_TO},
	 * {@link PathElementType#LINE_TO}, {@link PathElementType#QUAD_TO}, or {@link PathElementType#CLOSE}.
	 */
	@Pure
	public DoubleProperty ctrlX2Property() {
		return null;
	}

	/** Replies the property for the y coordinate of the second control point.
	 *
	 * @return the y coordinate, or <code>null</code> if the type is {@link PathElementType#MOVE_TO},
	 * {@link PathElementType#LINE_TO}, {@link PathElementType#QUAD_TO}, or {@link PathElementType#CLOSE}.
	 */
	@Pure
	public DoubleProperty ctrlY2Property() {
		return null;
	}

	/** Replies the property for the radius along the x axis.
	 *
	 * @return the x radius, or <code>null</code> if the type is not {@link PathElementType#ARC_TO}.
	 */
	@Pure
	public DoubleProperty radiusXProperty() {
		return null;
	}

	/** Replies the property for the radius along the y axis.
	 *
	 * @return the y radius, or <code>null</code> if the type is not {@link PathElementType#ARC_TO}.
	 */
	@Pure
	public DoubleProperty radiusYProperty() {
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
	 * @author $Author: hjaffali$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 13.0
	 */
	static class MovePathElement2dfx extends PathElement2dfx {

		private static final long serialVersionUID = 4465791748559255427L;

		/**
		 * @param tox x coordinate of the target point.
		 * @param toy y coordinate of the target point.
		 */
		MovePathElement2dfx(DoubleProperty tox, DoubleProperty toy) {
		    super(PathElementType.MOVE_TO, tox, toy);
		}

		/**
		 * @param toPoint the target point.
		 */
		MovePathElement2dfx(Point2dfx toPoint) {
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

                final PathElement2afp elt = (PathElement2afp) obj;
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
			long bits = 1L;
			bits = 31L * bits + this.type.hashCode();
			bits = 31L * bits + Double.hashCode(getToX());
			bits = 31L * bits + Double.hashCode(getToY());
			return (int) (bits ^ (bits >> 31));
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
			array[0] = this.to.getX();
			array[1] = this.to.getY();
		}

		@Pure
		@Override
		public void toArray(DoubleProperty[] array) {
			assert array != null : AssertMessages.notNullParameter();
			assert array.length >= 2 : AssertMessages.tooSmallArrayParameter(array.length, 2);
			array[0] = this.to.xProperty();
			array[1] = this.to.yProperty();
		}

		@Pure
		@Override
		public DoubleProperty[] toArray() {
			return new DoubleProperty[] {this.to.xProperty(), this.to.yProperty()};
		}

		@Pure
		@Override
		public double getFromX() {
			return 0.;
		}

		@Pure
		@Override
		public double getFromY() {
			return 0.;
		}

		@Override
		public DoubleProperty fromXProperty() {
			return null;
		}

		@Override
		public DoubleProperty fromYProperty() {
			return null;
		}

	}

	/** An element of the path that represents a <code>LINE_TO</code>.
	 *
	 * @author $Author: sgalland$
	 * @author $Author: hjaffali$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 13.0
	 */
	static class LinePathElement2dfx extends PathElement2dfx {

		private static final long serialVersionUID = -8828290765080530997L;

		private Point2dfx from = new Point2dfx();

		/**
		 * @param fromx x coordinate of the origin point.
		 * @param fromy y coordinate of the origin point.
		 * @param tox x coordinate of the target point.
		 * @param toy y coordinate of the target point.
		 */
		LinePathElement2dfx(DoubleProperty fromx, DoubleProperty fromy, DoubleProperty tox, DoubleProperty toy) {
		    super(PathElementType.LINE_TO, tox, toy);
		    assert fromx != null : AssertMessages.notNullParameter(0);
		    assert fromy != null : AssertMessages.notNullParameter(1);
		    this.from.x = fromx;
		    this.from.y = fromy;
		}

		/**
		 * @param fromPoint the origin point.
		 * @param toPoint the target point.
		 */
		LinePathElement2dfx(Point2dfx fromPoint, Point2dfx toPoint) {
			super(PathElementType.LINE_TO, toPoint);
			assert fromPoint != null : AssertMessages.notNullParameter(0);
			this.from = fromPoint;
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
		public BooleanProperty isEmptyProperty() {
			if (this.isEmpty == null) {
				this.isEmpty = new ReadOnlyBooleanWrapper(this, MathFXAttributeNames.IS_EMPTY);
				this.isEmpty.bind(Bindings.createBooleanBinding(() ->
					 MathUtil.isEpsilonEqual(fromXProperty().get(), toXProperty().get())
							&& MathUtil.isEpsilonEqual(fromYProperty().get(), toYProperty().get()),
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
			array[0] = this.to.getX();
			array[1] = this.to.getY();
		}

		@Pure
		@Override
		public void toArray(DoubleProperty[] array) {
			assert array != null : AssertMessages.notNullParameter();
			assert array.length >= 2 : AssertMessages.tooSmallArrayParameter(array.length, 2);
			array[0] = this.to.xProperty();
			array[1] = this.to.yProperty();
		}

		@Pure
		@Override
		public DoubleProperty[] toArray() {
			return new DoubleProperty[] {this.to.xProperty(), this.to.yProperty()};
		}

		@Pure
		@Override
		public double getFromX() {
			return this.from.getX();
		}

		@Pure
		@Override
		public double getFromY() {
			return this.from.getY();
		}

		@Pure
		@Override
		public DoubleProperty fromXProperty() {
			return this.from.xProperty();
		}

		@Pure
		@Override
		public DoubleProperty fromYProperty() {
			return this.from.yProperty();
		}

	}

	/** An element of the path that represents a <code>QUAD_TO</code>.
	 *
	 * @author $Author: sgalland$
	 * @author $Author: hjaffali$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 13.0
	 */
	static class QuadPathElement2dfx extends PathElement2dfx {

		private static final long serialVersionUID = 4782822639304211439L;

		private Point2dfx from = new Point2dfx();

		private Point2dfx ctrl = new Point2dfx();

		/**
		 * @param fromx x coordinate of the origin point.
		 * @param fromy y coordinate of the origin point.
		 * @param ctrlx x coordinate of the control point.
		 * @param ctrly y coordinate of the control point.
		 * @param tox x coordinate of the target point.
		 * @param toy y coordinate of the target point.
		 */
		QuadPathElement2dfx(DoubleProperty fromx, DoubleProperty fromy, DoubleProperty ctrlx,
		        DoubleProperty ctrly, DoubleProperty tox, DoubleProperty toy) {
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

		/**
		 * @param fromPoint the origin point.
		 * @param ctrlPoint the control point.
		 * @param toPoint the target point.
		 */
		QuadPathElement2dfx(Point2dfx fromPoint, Point2dfx ctrlPoint, Point2dfx toPoint) {
			super(PathElementType.QUAD_TO, toPoint);
			assert fromPoint.x != null : AssertMessages.notNullParameter(0);
			assert ctrlPoint.x != null : AssertMessages.notNullParameter(1);
			this.from = fromPoint;
			this.ctrl = ctrlPoint;
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
		public BooleanProperty isEmptyProperty() {
			if (this.isEmpty == null) {
				this.isEmpty = new ReadOnlyBooleanWrapper(this, MathFXAttributeNames.IS_EMPTY);
				this.isEmpty.bind(Bindings.createBooleanBinding(() ->
					 MathUtil.isEpsilonEqual(fromXProperty().get(), toXProperty().get())
							&& MathUtil.isEpsilonEqual(fromYProperty().get(), toYProperty().get())
							&& MathUtil.isEpsilonEqual(ctrlX1Property().get(), toXProperty().get())
							&& MathUtil.isEpsilonEqual(ctrlY1Property().get(), toYProperty().get()),
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
			array[0] = this.ctrl.getX();
			array[1] = this.ctrl.getY();
			array[2] = this.to.getX();
			array[3] = this.to.getY();
		}

		@Pure
		@Override
		public void toArray(DoubleProperty[] array) {
			assert array != null : AssertMessages.notNullParameter();
			assert array.length >= 4 : AssertMessages.tooSmallArrayParameter(array.length, 4);
			array[0] = this.ctrl.xProperty();
			array[1] = this.ctrl.yProperty();
			array[2] = this.to.xProperty();
			array[3] = this.to.yProperty();
		}

		@Pure
		@Override
		public DoubleProperty[] toArray() {
			return new DoubleProperty[] {this.ctrl.xProperty(), this.ctrl.yProperty(), this.to.xProperty(), this.to.yProperty()};
		}

		@Pure
		@Override
		public double getFromX() {
			return this.from.getX();
		}

		@Pure
		@Override
		public double getFromY() {
			return this.from.getY();
		}

		@Pure
		@Override
		public double getCtrlX1() {
			return this.ctrl.getX();
		}

		@Pure
		@Override
		public double getCtrlY1() {
			return this.ctrl.getY();
		}

		@Pure
		@Override
		public DoubleProperty fromXProperty() {
			return this.from.xProperty();
		}

		@Pure
		@Override
		public DoubleProperty fromYProperty() {
			return this.from.yProperty();
		}

		@Pure
		@Override
		public DoubleProperty ctrlX1Property() {
			return this.ctrl.xProperty();
		}

		@Pure
		@Override
		public DoubleProperty ctrlY1Property() {
			return this.ctrl.yProperty();
		}

	}

	/** An element of the path that represents a <code>CURVE_TO</code>.
	 *
	 * @author $Author: sgalland$
	 * @author $Author: hjaffali$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 13.0
	 */
	static class CurvePathElement2dfx extends PathElement2dfx {

		private static final long serialVersionUID = -2831895270995173092L;

		private Point2dfx from = new Point2dfx();

		private Point2dfx ctrl1 = new Point2dfx();

		private Point2dfx ctrl2 = new Point2dfx();

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
		@SuppressWarnings("checkstyle:magicnumber")
		CurvePathElement2dfx(DoubleProperty fromx, DoubleProperty fromy, DoubleProperty ctrlx1,
		        DoubleProperty ctrly1, DoubleProperty ctrlx2, DoubleProperty ctrly2,
		        DoubleProperty tox, DoubleProperty toy) {
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

		/**
		 * @param fromPoint the origin point.
		 * @param ctrl1Point the first control point.
		 * @param ctrl2Point the second control point.
		 * @param toPoint the target point.
		 */
		@SuppressWarnings("checkstyle:magicnumber")
        CurvePathElement2dfx(Point2dfx fromPoint, Point2dfx ctrl1Point, Point2dfx ctrl2Point, Point2dfx toPoint) {
			super(PathElementType.CURVE_TO, toPoint);
			assert fromPoint != null : AssertMessages.notNullParameter(0);
			assert ctrl1Point != null : AssertMessages.notNullParameter(1);
			assert ctrl2Point != null : AssertMessages.notNullParameter(2);
			this.from = fromPoint;
			this.ctrl1 = ctrl1Point;
			this.ctrl2 = ctrl2Point;
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
		public BooleanProperty isEmptyProperty() {
			if (this.isEmpty == null) {
				this.isEmpty = new ReadOnlyBooleanWrapper(this, MathFXAttributeNames.IS_EMPTY);
				this.isEmpty.bind(Bindings.createBooleanBinding(() ->
					MathUtil.isEpsilonEqual(fromXProperty().get(), toXProperty().get())
							&& MathUtil.isEpsilonEqual(fromYProperty().get(), toYProperty().get())
							&& MathUtil.isEpsilonEqual(ctrlX1Property().get(), toXProperty().get())
							&& MathUtil.isEpsilonEqual(ctrlY1Property().get(), toYProperty().get())
							&& MathUtil.isEpsilonEqual(ctrlX2Property().get(), toXProperty().get())
							&& MathUtil.isEpsilonEqual(ctrlY2Property().get(), toYProperty().get()),
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
		public void toArray(DoubleProperty[] array) {
			assert array != null : AssertMessages.notNullParameter();
			assert array.length >= 6 : AssertMessages.tooSmallArrayParameter(array.length, 6);
			array[0] = this.ctrl1.xProperty();
			array[1] = this.ctrl1.yProperty();
			array[2] = this.ctrl2.xProperty();
			array[3] = this.ctrl2.yProperty();
			array[4] = this.to.xProperty();
			array[5] = this.to.yProperty();
		}

		@Pure
		@Override
		public void toArray(double[] array) {
			assert array != null : AssertMessages.notNullParameter();
			assert array.length >= 6 : AssertMessages.tooSmallArrayParameter(array.length, 6);
			array[0] = this.ctrl1.getX();
			array[1] = this.ctrl1.getY();
			array[2] = this.ctrl2.getX();
			array[3] = this.ctrl2.getY();
			array[4] = this.to.getX();
			array[5] = this.to.getY();
		}

		@Pure
		@Override
		public DoubleProperty[] toArray() {
            return new DoubleProperty[] {this.ctrl1.xProperty(), this.ctrl1.yProperty(), this.ctrl2.xProperty(),
                    this.ctrl2.yProperty(), this.to.xProperty(), this.to.yProperty(), };
		}

		@Pure
		@Override
		public double getFromX() {
			return this.from.getX();
		}

		@Pure
		@Override
		public double getFromY() {
			return this.from.getY();
		}

		@Pure
		@Override
		public double getCtrlX1() {
			return this.ctrl1.getX();
		}

		@Pure
		@Override
		public double getCtrlY1() {
			return this.ctrl1.getY();
		}

		@Pure
		@Override
		public double getCtrlX2() {
			return this.ctrl2.getX();
		}

		@Pure
		@Override
		public double getCtrlY2() {
			return this.ctrl2.getY();
		}

		@Pure
		@Override
		public DoubleProperty fromXProperty() {
			return this.from.xProperty();
		}

		@Pure
		@Override
		public DoubleProperty fromYProperty() {
			return this.from.yProperty();
		}

		@Pure
		@Override
		public DoubleProperty ctrlX1Property() {
			return this.ctrl1.xProperty();
		}

		@Pure
		@Override
		public DoubleProperty ctrlY1Property() {
			return this.ctrl1.yProperty();
		}

		@Pure
		@Override
		public DoubleProperty ctrlX2Property() {
			return this.ctrl2.xProperty();
		}

		@Pure
		@Override
		public DoubleProperty ctrlY2Property() {
			return this.ctrl2.yProperty();
		}

	}

	/** An element of the path that represents a <code>CLOSE</code>.
	 *
	 * @author $Author: sgalland$
	 * @author $Author: hjaffali$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 13.0
	 */
	static class ClosePathElement2dfx extends PathElement2dfx {

		private static final long serialVersionUID = 5324688417590599323L;

		private Point2dfx from = new Point2dfx();

		/**
		 * @param fromx x coordinate of the origin point.
		 * @param fromy y coordinate of the origin point.
		 * @param tox x coordinate of the target point.
		 * @param toy y coordinate of the target point.
		 */
		ClosePathElement2dfx(DoubleProperty fromx, DoubleProperty fromy, DoubleProperty tox, DoubleProperty toy) {
		    super(PathElementType.CLOSE, tox, toy);
		    assert fromx != null : AssertMessages.notNullParameter(0);
		    assert fromy != null : AssertMessages.notNullParameter(1);
		    this.from.x = fromx;
		    this.from.y = fromy;
		}

		/**
		 * @param fromPoint the origin point.
		 * @param toPoint the target point.
		 */
		ClosePathElement2dfx(Point2dfx fromPoint, Point2dfx toPoint) {
			super(PathElementType.CLOSE, toPoint);
			assert fromPoint != null : AssertMessages.notNullParameter(0);
			this.from = fromPoint;
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
		public BooleanProperty isEmptyProperty() {
			if (this.isEmpty == null) {
				this.isEmpty = new ReadOnlyBooleanWrapper(this, MathFXAttributeNames.IS_EMPTY);
				this.isEmpty.bind(Bindings.createBooleanBinding(() ->
					MathUtil.isEpsilonEqual(fromXProperty().get(), toXProperty().get())
							&& MathUtil.isEpsilonEqual(fromYProperty().get(), toYProperty().get()),
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
			array[0] = (int) this.to.getX();
			array[1] = (int) this.to.getY();
		}

		@Pure
		@Override
		public void toArray(DoubleProperty[] array) {
			assert array != null : AssertMessages.notNullParameter();
			assert array.length >= 2 : AssertMessages.tooSmallArrayParameter(array.length, 2);
			array[0] = this.to.xProperty();
			array[1] = this.to.yProperty();
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
		public DoubleProperty[] toArray() {
			return new DoubleProperty[] {this.to.xProperty(), this.to.yProperty()};
		}

		@Pure
		@Override
		public double getFromX() {
			return this.from.getX();
		}

		@Pure
		@Override
		public double getFromY() {
			return this.from.getY();
		}

		@Pure
		@Override
		public DoubleProperty fromXProperty() {
			return this.from.xProperty();
		}

		@Pure
		@Override
		public DoubleProperty fromYProperty() {
			return this.from.yProperty();
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
	static class ArcPathElement2dfx extends PathElement2dfx {

		private static final long serialVersionUID = 1191891479706357600L;

		private Point2dfx from = new Point2dfx();

		private final DoubleProperty radiusX;

		private final DoubleProperty radiusY;

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
		ArcPathElement2dfx(DoubleProperty fromx, DoubleProperty fromy, DoubleProperty tox, DoubleProperty toy,
		        DoubleProperty xradius, DoubleProperty yradius, DoubleProperty xrotation,
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

		/**
		 * @param fromPoint the origin point.
		 * @param toPoint the target point.
		 * @param xradius radius of the ellipse along its x axis.
		 * @param yradius radius of the ellipse along its y axis.
		 * @param xrotation rotation (in radians) of the ellipse's x axis.
		 * @param largeArcFlag <code>true</code> iff the path will sweep the long way around the ellipse.
		 * @param sweepFlag <code>true</code> iff the path will sweep clockwise around the ellipse.
		 */
		ArcPathElement2dfx(Point2dfx fromPoint, Point2dfx toPoint, DoubleProperty xradius, DoubleProperty yradius,
		        DoubleProperty xrotation, BooleanProperty largeArcFlag, BooleanProperty sweepFlag) {
		    super(PathElementType.ARC_TO, toPoint);
		    assert fromPoint != null : AssertMessages.notNullParameter(0);
		    assert xradius != null : AssertMessages.notNullParameter(2);
		    assert yradius != null : AssertMessages.notNullParameter(3);
		    assert xrotation != null : AssertMessages.notNullParameter(4);
		    assert largeArcFlag != null : AssertMessages.notNullParameter(5);
		    assert sweepFlag != null : AssertMessages.notNullParameter(6);
			this.from = fromPoint;
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
		public BooleanProperty isEmptyProperty() {
			if (this.isEmpty == null) {
				this.isEmpty = new ReadOnlyBooleanWrapper(this, MathFXAttributeNames.IS_EMPTY);
				this.isEmpty.bind(Bindings.createBooleanBinding(() ->
					MathUtil.isEpsilonEqual(fromXProperty().get(), toXProperty().get())
							&& MathUtil.isEpsilonEqual(fromYProperty().get(), toYProperty().get()),
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
		public void toArray(DoubleProperty[] array) {
			assert array != null : AssertMessages.notNullParameter();
			assert array.length >= 2 : AssertMessages.tooSmallArrayParameter(array.length, 2);
			array[0] = this.to.xProperty();
			array[1] = this.to.yProperty();
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
		public DoubleProperty[] toArray() {
			return new DoubleProperty[] {this.to.xProperty(), this.to.yProperty()};
		}

		@Pure
		@Override
		public double getFromX() {
			return this.from.getX();
		}

		@Pure
		@Override
		public double getFromY() {
			return this.from.getY();
		}

		@Pure
		@Override
		public DoubleProperty fromXProperty() {
			return this.from.xProperty();
		}

		@Pure
		@Override
		public DoubleProperty fromYProperty() {
			return this.from.yProperty();
		}

		@Override
		public double getRadiusX() {
			return this.radiusX.get();
		}

		@Override
		public double getRadiusY() {
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
		public DoubleProperty radiusXProperty() {
			return this.radiusX;
		}

		@Override
		public DoubleProperty radiusYProperty() {
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
