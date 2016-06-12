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
import org.arakhne.afc.math.geometry.PathElementType;
import org.arakhne.afc.math.geometry.d2.MathFXAttributeNames;
import org.arakhne.afc.math.geometry.d2.afp.PathElement2afp;
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
@SuppressWarnings({"checkstyle:magicnumber", "static-method"})
public abstract class PathElement2dfx implements PathElement2afp {
	private static final long serialVersionUID = 1724746568685625149L;

	private static final String OPEN_CLOSE_PARENTHESES = ")\n\tto: (";

	/** Type of the element.
	 */
	protected final PathElementType type;

	/** Target point.
	 */
	protected final DoubleProperty toX;

	/** Target point.
	 */
	protected final DoubleProperty toY;

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

	@Pure
	@Override
	public final double getToX() {
		return this.toX.get();
	}

	@Pure
	@Override
	public final double getToY() {
		return this.toY.get();
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
		return this.toX;
	}

	/** Replies the property for the y coordinate of the target point.
	 *
	 * @return the y coordinate.
	 */
	@Pure
	public DoubleProperty toYProperty() {
		return this.toY;
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
			array[0] = this.toX.intValue();
			array[1] = this.toY.intValue();
		}

		@Pure
		@Override
		public void toArray(double[] array) {
			assert array != null : AssertMessages.notNullParameter();
			assert array.length >= 2 : AssertMessages.tooSmallArrayParameter(array.length, 2);
			array[0] = this.toX.doubleValue();
			array[1] = this.toY.doubleValue();
		}

		@Pure
		@Override
		public void toArray(DoubleProperty[] array) {
			assert array != null : AssertMessages.notNullParameter();
			assert array.length >= 2 : AssertMessages.tooSmallArrayParameter(array.length, 2);
			array[0] = this.toX;
			array[1] = this.toY;
		}

		@Pure
		@Override
		public DoubleProperty[] toArray() {
			return new DoubleProperty[] {this.toX, this.toY};
		}

		@Pure
		@Override
		public String toString() {
			return "MOVE\n\tto: (" //$NON-NLS-1$
					+ getToX() + ", " //$NON-NLS-1$
					+ getToY() + ")\n"; //$NON-NLS-1$
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

		private final DoubleProperty fromX;

		private final DoubleProperty fromY;

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
			this.fromX = fromx;
			this.fromY = fromy;
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
				this.isEmpty.bind(Bindings.createBooleanBinding(() -> {
					return MathUtil.isEpsilonEqual(fromXProperty().get(), toXProperty().get())
							&& MathUtil.isEpsilonEqual(fromYProperty().get(), toYProperty().get());
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
			assert array != null : AssertMessages.notNullParameter();
			assert array.length >= 2 : AssertMessages.tooSmallArrayParameter(array.length, 2);
			array[0] = this.toX.intValue();
			array[1] = this.toY.intValue();
		}

		@Pure
		@Override
		public void toArray(double[] array) {
			assert array != null : AssertMessages.notNullParameter();
			assert array.length >= 2 : AssertMessages.tooSmallArrayParameter(array.length, 2);
			array[0] = this.toX.doubleValue();
			array[1] = this.toY.doubleValue();
		}

		@Pure
		@Override
		public void toArray(DoubleProperty[] array) {
			assert array != null : AssertMessages.notNullParameter();
			assert array.length >= 2 : AssertMessages.tooSmallArrayParameter(array.length, 2);
			array[0] = this.toX;
			array[1] = this.toY;
		}

		@Pure
		@Override
		public DoubleProperty[] toArray() {
			return new DoubleProperty[] {this.toX, this.toY};
		}

		@Pure
		@Override
		public String toString() {
			return "LINE\n\tfrom: (" //$NON-NLS-1$
					+ getFromX() + ", " //$NON-NLS-1$
					+ getFromY() + OPEN_CLOSE_PARENTHESES //$NON-NLS-1$
					+ getToX() + ", " //$NON-NLS-1$
					+ getToY() + ")\n"; //$NON-NLS-1$
		}

		@Pure
		@Override
		public double getFromX() {
			return this.fromX.get();
		}

		@Pure
		@Override
		public double getFromY() {
			return this.fromY.get();
		}

		@Pure
		@Override
		public DoubleProperty fromXProperty() {
			return this.fromX;
		}

		@Pure
		@Override
		public DoubleProperty fromYProperty() {
			return this.fromY;
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

		private final DoubleProperty fromX;

		private final DoubleProperty fromY;

		private final DoubleProperty ctrlX;

		private final DoubleProperty ctrlY;

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
			this.fromX = fromx;
			this.fromY = fromy;
			this.ctrlX = ctrlx;
			this.ctrlY = ctrly;
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
				this.isEmpty.bind(Bindings.createBooleanBinding(() -> {
					return MathUtil.isEpsilonEqual(fromXProperty().get(), toXProperty().get())
							&& MathUtil.isEpsilonEqual(fromYProperty().get(), toYProperty().get())
							&& MathUtil.isEpsilonEqual(ctrlX1Property().get(), toXProperty().get())
							&& MathUtil.isEpsilonEqual(ctrlY1Property().get(), toYProperty().get());
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
			assert array != null : AssertMessages.notNullParameter();
			assert array.length >= 4 : AssertMessages.tooSmallArrayParameter(array.length, 4);
			array[0] = this.ctrlX.intValue();
			array[1] = this.ctrlY.intValue();
			array[2] = this.toX.intValue();
			array[3] = this.toY.intValue();
		}

		@Pure
		@Override
		public void toArray(double[] array) {
			assert array != null : AssertMessages.notNullParameter();
			assert array.length >= 4 : AssertMessages.tooSmallArrayParameter(array.length, 4);
			array[0] = this.ctrlX.doubleValue();
			array[1] = this.ctrlY.doubleValue();
			array[2] = this.toX.doubleValue();
			array[3] = this.toY.doubleValue();
		}

		@Pure
		@Override
		public void toArray(DoubleProperty[] array) {
			assert array != null : AssertMessages.notNullParameter();
			assert array.length >= 4 : AssertMessages.tooSmallArrayParameter(array.length, 4);
			array[0] = this.ctrlX;
			array[1] = this.ctrlY;
			array[2] = this.toX;
			array[3] = this.toY;
		}

		@Pure
		@Override
		public DoubleProperty[] toArray() {
			return new DoubleProperty[] {this.ctrlX, this.ctrlY, this.toX, this.toY};
		}

		@Pure
		@Override
		public String toString() {
			return "QUAD:\n\tfrom: (" //$NON-NLS-1$
					+ getFromX() + ", " //$NON-NLS-1$
					+ getFromY() + ")\n\tctrl: (" //$NON-NLS-1$
					+ getCtrlX1() + ", " //$NON-NLS-1$
					+ getCtrlY1() + OPEN_CLOSE_PARENTHESES //$NON-NLS-1$
					+ getToX() + ", " //$NON-NLS-1$
					+ getToY() + ")"; //$NON-NLS-1$
		}

		@Pure
		@Override
		public double getFromX() {
			return this.fromX.get();
		}

		@Pure
		@Override
		public double getFromY() {
			return this.fromY.get();
		}

		@Pure
		@Override
		public double getCtrlX1() {
			return this.ctrlX.get();
		}

		@Pure
		@Override
		public double getCtrlY1() {
			return this.ctrlY.get();
		}

		@Pure
		@Override
		public DoubleProperty fromXProperty() {
			return this.fromX;
		}

		@Pure
		@Override
		public DoubleProperty fromYProperty() {
			return this.fromY;
		}

		@Pure
		@Override
		public DoubleProperty ctrlX1Property() {
			return this.ctrlX;
		}

		@Pure
		@Override
		public DoubleProperty ctrlY1Property() {
			return this.ctrlY;
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

		private final DoubleProperty fromX;

		private final DoubleProperty fromY;

		private final DoubleProperty ctrlX1;

		private final DoubleProperty ctrlY1;

		private final DoubleProperty ctrlX2;

		private final DoubleProperty ctrlY2;

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
				this.isEmpty.bind(Bindings.createBooleanBinding(() -> {
					return MathUtil.isEpsilonEqual(fromXProperty().get(), toXProperty().get())
							&& MathUtil.isEpsilonEqual(fromYProperty().get(), toYProperty().get())
							&& MathUtil.isEpsilonEqual(ctrlX1Property().get(), toXProperty().get())
							&& MathUtil.isEpsilonEqual(ctrlY1Property().get(), toYProperty().get())
							&& MathUtil.isEpsilonEqual(ctrlX2Property().get(), toXProperty().get())
							&& MathUtil.isEpsilonEqual(ctrlY2Property().get(), toYProperty().get());
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
			assert array != null : AssertMessages.notNullParameter();
			assert array.length >= 6 : AssertMessages.tooSmallArrayParameter(array.length, 6);
			array[0] = this.ctrlX1.intValue();
			array[1] = this.ctrlY1.intValue();
			array[2] = this.ctrlX2.intValue();
			array[3] = this.ctrlY2.intValue();
			array[4] = this.toX.intValue();
			array[5] = this.toY.intValue();
		}

		@Pure
		@Override
		public void toArray(DoubleProperty[] array) {
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
		public void toArray(double[] array) {
			assert array != null : AssertMessages.notNullParameter();
			assert array.length >= 6 : AssertMessages.tooSmallArrayParameter(array.length, 6);
			array[0] = this.ctrlX1.doubleValue();
			array[1] = this.ctrlY1.doubleValue();
			array[2] = this.ctrlX2.doubleValue();
			array[3] = this.ctrlY2.doubleValue();
			array[4] = this.toX.doubleValue();
			array[5] = this.toY.doubleValue();
		}

		@Pure
		@Override
		public DoubleProperty[] toArray() {
			return new DoubleProperty[] {this.ctrlX1, this.ctrlY1, this.ctrlX2, this.ctrlY2, this.toX, this.toY};
		}

		@Pure
		@Override
		public String toString() {
			return "CURVE:\n\tfrom: (" //$NON-NLS-1$
					+ getFromX() + ", " //$NON-NLS-1$
					+ getFromY() + ")\n\tctrl 1: (" //$NON-NLS-1$
					+ getCtrlX1() + ", " //$NON-NLS-1$
					+ getCtrlY1() + ")\n\tctrl 2: (" //$NON-NLS-1$
					+ getCtrlX2() + ", " //$NON-NLS-1$
					+ getCtrlY2() + OPEN_CLOSE_PARENTHESES //$NON-NLS-1$
					+ this.getToX() + ", " //$NON-NLS-1$
					+ this.getToY() + ")"; //$NON-NLS-1$
		}

		@Pure
		@Override
		public double getFromX() {
			return this.fromX.get();
		}

		@Pure
		@Override
		public double getFromY() {
			return this.fromY.get();
		}

		@Pure
		@Override
		public double getCtrlX1() {
			return this.ctrlX1.get();
		}

		@Pure
		@Override
		public double getCtrlY1() {
			return this.ctrlY1.get();
		}

		@Pure
		@Override
		public double getCtrlX2() {
			return this.ctrlX2.get();
		}

		@Pure
		@Override
		public double getCtrlY2() {
			return this.ctrlY2.get();
		}

		@Pure
		@Override
		public DoubleProperty fromXProperty() {
			return this.fromX;
		}

		@Pure
		@Override
		public DoubleProperty fromYProperty() {
			return this.fromY;
		}

		@Pure
		@Override
		public DoubleProperty ctrlX1Property() {
			return this.ctrlX1;
		}

		@Pure
		@Override
		public DoubleProperty ctrlY1Property() {
			return this.ctrlY1;
		}

		@Pure
		@Override
		public DoubleProperty ctrlX2Property() {
			return this.ctrlX2;
		}

		@Pure
		@Override
		public DoubleProperty ctrlY2Property() {
			return this.ctrlY2;
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

		private final DoubleProperty fromX;

		private final DoubleProperty fromY;

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
			this.fromX = fromx;
			this.fromY = fromy;
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
				this.isEmpty.bind(Bindings.createBooleanBinding(() -> {
					return MathUtil.isEpsilonEqual(fromXProperty().get(), toXProperty().get())
							&& MathUtil.isEpsilonEqual(fromYProperty().get(), toYProperty().get());
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
			assert array != null : AssertMessages.notNullParameter();
			assert array.length >= 2 : AssertMessages.tooSmallArrayParameter(array.length, 2);
			array[0] = (int) this.toX.get();
			array[1] = (int) this.toY.get();
		}

		@Pure
		@Override
		public void toArray(DoubleProperty[] array) {
			assert array != null : AssertMessages.notNullParameter();
			assert array.length >= 2 : AssertMessages.tooSmallArrayParameter(array.length, 2);
			array[0] = this.toX;
			array[1] = this.toY;
		}

		@Pure
		@Override
		public void toArray(double[] array) {
			assert array != null : AssertMessages.notNullParameter();
			assert array.length >= 2 : AssertMessages.tooSmallArrayParameter(array.length, 2);
			array[0] = this.toX.get();
			array[1] = this.toY.get();
		}

		@Pure
		@Override
		public DoubleProperty[] toArray() {
			return new DoubleProperty[] {this.toX, this.toY};
		}

		@Pure
		@Override
		public String toString() {
			return "CLOSE:\n\tfrom: (" //$NON-NLS-1$
					+ getFromX() + ", " //$NON-NLS-1$
					+ getFromY() + ")\n\tto: " //$NON-NLS-1$
					+ getToX() + ", " //$NON-NLS-1$
					+ getToY() + ")"; //$NON-NLS-1$
		}

		@Pure
		@Override
		public double getFromX() {
			return this.fromX.get();
		}

		@Pure
		@Override
		public double getFromY() {
			return this.fromY.get();
		}

		@Pure
		@Override
		public DoubleProperty fromXProperty() {
			return this.fromX;
		}

		@Pure
		@Override
		public DoubleProperty fromYProperty() {
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
	static class ArcPathElement2dfx extends PathElement2dfx {

		private static final long serialVersionUID = 1191891479706357600L;

		private final DoubleProperty fromX;

		private final DoubleProperty fromY;

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
				this.isEmpty.bind(Bindings.createBooleanBinding(() -> {
					return MathUtil.isEpsilonEqual(fromXProperty().get(), toXProperty().get())
							&& MathUtil.isEpsilonEqual(fromYProperty().get(), toYProperty().get());
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
			assert array != null : AssertMessages.notNullParameter();
			assert array.length >= 2 : AssertMessages.tooSmallArrayParameter(array.length, 2);
			array[0] = this.toX.intValue();
			array[1] = this.toY.intValue();
		}

		@Pure
		@Override
		public void toArray(DoubleProperty[] array) {
			assert array != null : AssertMessages.notNullParameter();
			assert array.length >= 2 : AssertMessages.tooSmallArrayParameter(array.length, 2);
			array[0] = this.toX;
			array[1] = this.toY;
		}

		@Pure
		@Override
		public void toArray(double[] array) {
			assert array != null : AssertMessages.notNullParameter();
			assert array.length >= 2 : AssertMessages.tooSmallArrayParameter(array.length, 2);
			array[0] = this.toX.doubleValue();
			array[1] = this.toY.doubleValue();
		}

		@Pure
		@Override
		public DoubleProperty[] toArray() {
			return new DoubleProperty[] {this.toX, this.toY};
		}

		@Pure
		@Override
		public String toString() {
			return "ARC:\n\tfrom: (" //$NON-NLS-1$
					+ getFromX() + ", " //$NON-NLS-1$
					+ getFromY() + OPEN_CLOSE_PARENTHESES //$NON-NLS-1$
					+ getToX() + ", " //$NON-NLS-1$
					+ getToY() + ")\n\tx radius: " //$NON-NLS-1$
					+ getRadiusX() + "\n\ty radius: " //$NON-NLS-1$
					+ getRadiusY() + "\n\trotation: " //$NON-NLS-1$
					+ getRotationX() + "\n\tlarge arc: " //$NON-NLS-1$
					+ getLargeArcFlag() + "\n\tsweep: " //$NON-NLS-1$
					+ getSweepFlag();
		}

		@Pure
		@Override
		public double getFromX() {
			return this.fromX.get();
		}

		@Pure
		@Override
		public double getFromY() {
			return this.fromY.get();
		}

		@Pure
		@Override
		public DoubleProperty fromXProperty() {
			return this.fromX;
		}

		@Pure
		@Override
		public DoubleProperty fromYProperty() {
			return this.fromY;
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
