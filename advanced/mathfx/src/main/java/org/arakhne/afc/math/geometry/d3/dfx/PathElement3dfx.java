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

package org.arakhne.afc.math.geometry.d3.dfx;

import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.MathUtil;
import org.arakhne.afc.math.geometry.PathElementType;
import org.arakhne.afc.math.geometry.d3.afp.PathElement3afp;

/** An element of the path.
 *
 * @author $Author: sgalland$
 * @author $Author: hjaffali$
 * @author $Author: tpiotrow$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public abstract class PathElement3dfx implements PathElement3afp {

	private static final long serialVersionUID = 1724746568685625149L;

	/** Type of the element.
	 */
	protected final PathElementType type;

	/** Target point.
	 */
	protected final DoubleProperty toX;

	/** Target point.
	 */
	protected final DoubleProperty toY;

	/** Target point.
	 */
	protected final DoubleProperty toZ;

	/** Is Empty property.
	 */
	protected ReadOnlyBooleanWrapper isEmpty;

	/**
	 * @param type is the type of the element.
	 * @param tox the x coordinate of the target point.
	 * @param toy the x coordinate of the target point.
	 * @param toz the x coordinate of the target point.
	 */
	PathElement3dfx(PathElementType type, DoubleProperty tox, DoubleProperty toy, DoubleProperty toz) {
		assert type != null : "Path element type must be not null"; //$NON-NLS-1$
		assert tox != null : "toX must be not null"; //$NON-NLS-1$
		assert toy != null : "toY must be not null"; //$NON-NLS-1$
		this.type = type;
		this.toX = tox;
		this.toY = toy;
		this.toZ = toz;
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

	@Pure
	@Override
	public final double getToZ() {
		return this.toZ.get();
	}

	/** Replies the x coordinate of the starting point property.
	 *
	 * @return the x coordinate, or <code>null</code> if the type is {@link PathElementType#MOVE_TO}.
	 */
	@Pure
	public abstract DoubleProperty fromXProperty();

	/** Replies the y coordinate of the starting point property.
	 *
	 * @return the y coordinate, or <code>null</code> if the type is {@link PathElementType#MOVE_TO}.
	 */
	@Pure
	public abstract DoubleProperty fromYProperty();

	/** Replies the z coordinate of the starting point property.
	 *
	 * @return the z coordinate, or <code>null</code> if the type is {@link PathElementType#MOVE_TO}.
	 */
	@Pure
	public abstract DoubleProperty fromZProperty();

	/** Replies the x coordinate of the first control point property.
	 *
	 * @return the x coordinate, or <code>null</code> if the type is {@link PathElementType#MOVE_TO},
	 * {@link PathElementType#LINE_TO}, or {@link PathElementType#CLOSE}.
	 */
	@Pure
	public abstract DoubleProperty ctrlX1Property();

	/** Replies the y coordinate of the first control point property.
	 *
	 * @return the y coordinate, or {@link Double#NaN} if the type is {@link PathElementType#MOVE_TO},
	 * {@link PathElementType#LINE_TO}, or {@link PathElementType#CLOSE}.
	 */
	@Pure
	public abstract DoubleProperty ctrlY1Property();

	/** Replies the z coordinate of the first control point property.
	 *
	 * @return the z coordinate, or {@link Double#NaN} if the type is {@link PathElementType#MOVE_TO},
	 * {@link PathElementType#LINE_TO}, or {@link PathElementType#CLOSE}.
	 */
	@Pure
	public abstract DoubleProperty ctrlZ1Property();

	/** Replies the x coordinate of the second control point property.
	 *
	 * @return the x coordinate, or <code>null</code> if the type is {@link PathElementType#MOVE_TO},
	 * {@link PathElementType#LINE_TO}, {@link PathElementType#QUAD_TO}, or {@link PathElementType#CLOSE}.
	 */
	@Pure
	public abstract DoubleProperty ctrlX2Property();

	/** Replies the y coordinate of the second  control point property.
	 *
	 * @return the y coordinate, or <code>null</code> if the type is {@link PathElementType#MOVE_TO},
	 * {@link PathElementType#LINE_TO}, {@link PathElementType#QUAD_TO}, or {@link PathElementType#CLOSE}.
	 */
	@Pure
	public abstract DoubleProperty ctrlY2Property();

	/** Replies the z coordinate of the second  control point property.
	 *
	 * @return the z coordinate, or <code>null</code> if the type is {@link PathElementType#MOVE_TO},
	 * {@link PathElementType#LINE_TO}, {@link PathElementType#QUAD_TO}, or {@link PathElementType#CLOSE}.
	 */
	@Pure
	public abstract DoubleProperty ctrlZ2Property();

	/** Replies the x coordinate of the target point property.
	 *
	 * @return the x coordinate.
	 */
	@Pure
	public DoubleProperty toXProperty() {
		return this.toX;
	}

	/** Replies the y coordinate of the target point property.
	 *
	 * @return the y coordinate.
	 */
	@Pure
	public DoubleProperty toYProperty() {
		return this.toY;
	}

	/** Replies the z coordinate of the target point property.
	 *
	 * @return the z coordinate.
	 */
	@Pure
	public DoubleProperty toZProperty() {
		return this.toZ;
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

	/** An element of the path that represents a <code>MOVE_TO</code>.
	 *
	 * @author $Author: sgalland$
	 * @author $Author: hjaffali$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 13.0
	 */
	static class MovePathElement3dfx extends PathElement3dfx {
		private static final long serialVersionUID = 4465791748559255427L;

		/**
         * @param tox x coordinate of the target point.
         * @param toy y coordinate of the target point.
         * @param toz z coordinate of the target point.
         */
		MovePathElement3dfx(DoubleProperty tox, DoubleProperty toy, DoubleProperty toz) {
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
		public BooleanProperty isEmptyProperty() {
			if (this.isEmpty == null) {
				this.isEmpty = new ReadOnlyBooleanWrapper(this, "isEmpty", true); //$NON-NLS-1$
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
			assert array != null : "Array must be not null"; //$NON-NLS-1$
			assert array.length >= 3 : "Array size is too small"; //$NON-NLS-1$
			array[0] = this.toX.intValue();
			array[1] = this.toY.intValue();
			array[2] = this.toZ.intValue();
		}

		@Pure
		@Override
		public void toArray(double[] array) {
			assert array != null : "Array must be not null"; //$NON-NLS-1$
			assert array.length >= 3 : "Array size is too small"; //$NON-NLS-1$
			array[0] = this.toX.doubleValue();
			array[1] = this.toY.doubleValue();
			array[2] = this.toZ.doubleValue();
		}

		@Pure
		@Override
		public void toArray(DoubleProperty[] array) {
			assert array != null : "Array must be not null"; //$NON-NLS-1$
			assert array.length >= 3 : "Array size is too small"; //$NON-NLS-1$
			array[0] = this.toX;
			array[1] = this.toY;
			array[2] = this.toZ;
		}

		@Pure
		@Override
		public DoubleProperty[] toArray() {
			return new DoubleProperty[] {this.toX, this.toY, this.toZ};
		}

		@Pure
		@Override
		public String toString() {
			return "MOVE:(" //$NON-NLS-1$
					+ getToX() + ";" //$NON-NLS-1$
					+ getToY() + ";" //$NON-NLS-1$
					+ getToZ() + ")"; //$NON-NLS-1$
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

		@Pure
		@Override
		public double getFromZ() {
			return 0.;
		}

		@Pure
		@Override
		public double getCtrlX1() {
			return 0.;
		}

		@Pure
		@Override
		public double getCtrlY1() {
			return 0.;
		}

		@Pure
		@Override
		public double getCtrlZ1() {
			return 0.;
		}

		@Pure
		@Override
		public double getCtrlX2() {
			return 0.;
		}

		@Pure
		@Override
		public double getCtrlY2() {
			return 0.;
		}

		@Pure
		@Override
		public double getCtrlZ2() {
			return 0.;
		}

		@Pure
		@Override
		public DoubleProperty fromXProperty() {
			return null;
		}

		@Pure
		@Override
		public DoubleProperty fromYProperty() {
			return null;
		}

		@Pure
		@Override
		public DoubleProperty fromZProperty() {
			return null;
		}

		@Pure
		@Override
		public DoubleProperty ctrlX1Property() {
			return null;
		}

		@Pure
		@Override
		public DoubleProperty ctrlY1Property() {
			return null;
		}

		@Pure
		@Override
		public DoubleProperty ctrlZ1Property() {
			return null;
		}

		@Pure
		@Override
		public DoubleProperty ctrlX2Property() {
			return null;
		}

		@Pure
		@Override
		public DoubleProperty ctrlY2Property() {
			return null;
		}

		@Pure
		@Override
		public DoubleProperty ctrlZ2Property() {
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
	static class LinePathElement3dfx extends PathElement3dfx {

		private static final long serialVersionUID = -8828290765080530997L;

		private final DoubleProperty fromX;

		private final DoubleProperty fromY;

		private final DoubleProperty fromZ;

		/**
		 * @param fromx x coordinate of the origin point.
		 * @param fromy y coordinate of the origin point.
		 * @param fromz z coordinate of the origin point.
		 * @param tox x coordinate of the target point.
		 * @param toy y coordinate of the target point.
		 * @param toz z coordinate of the target point.
		 */
		LinePathElement3dfx(DoubleProperty fromx, DoubleProperty fromy, DoubleProperty fromz, DoubleProperty tox,
                DoubleProperty toy, DoubleProperty toz) {
			super(PathElementType.LINE_TO, tox, toy, toz);
			assert fromx != null : "fromX must be not null"; //$NON-NLS-1$
			assert fromy != null : "fromY must be not null"; //$NON-NLS-1$
			assert fromz != null : "fromZ must be not null"; //$NON-NLS-1$
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
		public BooleanProperty isEmptyProperty() {
			if (this.isEmpty == null) {
				this.isEmpty = new ReadOnlyBooleanWrapper(this, "isEmpty"); //$NON-NLS-1$
				this.isEmpty.bind(Bindings.createBooleanBinding(() ->
				    MathUtil.isEpsilonEqual(fromXProperty().get(), toXProperty().get())
				            && MathUtil.isEpsilonEqual(fromYProperty().get(), toYProperty().get())
				            && MathUtil.isEpsilonEqual(fromZProperty().get(), toZProperty().get()),
						fromXProperty(), toXProperty(), fromYProperty(), toYProperty(), fromZProperty(), toZProperty()));
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
			assert array != null : "Array must be not null"; //$NON-NLS-1$
			assert array.length >= 3 : "Array size is too small"; //$NON-NLS-1$
			array[0] = this.toX.intValue();
			array[1] = this.toY.intValue();
			array[2] = this.toZ.intValue();
		}

		@Pure
		@Override
		public void toArray(double[] array) {
			assert array != null : "Array must be not null"; //$NON-NLS-1$
			assert array.length >= 3 : "Array size is too small"; //$NON-NLS-1$
			array[0] = this.toX.doubleValue();
			array[1] = this.toY.doubleValue();
			array[2] = this.toZ.doubleValue();
		}

		@Pure
		@Override
		public void toArray(DoubleProperty[] array) {
			assert array != null : "Array must be not null"; //$NON-NLS-1$
			assert array.length >= 3 : "Array size is too small"; //$NON-NLS-1$
			array[0] = this.toX;
			array[1] = this.toY;
			array[2] = this.toZ;
		}

		@Pure
		@Override
		public DoubleProperty[] toArray() {
			return new DoubleProperty[] {this.toX, this.toY, this.toZ};
		}

		@Pure
		@Override
		public String toString() {
			return "LINE:(" //$NON-NLS-1$
					+ getFromX() + ";" //$NON-NLS-1$
					+ getFromY() + ";" //$NON-NLS-1$
					+ getFromZ() + ") -> (" //$NON-NLS-1$
					+ getToX() + ";" //$NON-NLS-1$
					+ getToY() + ";" //$NON-NLS-1$
					+ getToZ() + ")"; //$NON-NLS-1$
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
		public double getFromZ() {
			return this.fromZ.get();
		}

		@Pure
		@Override
		public double getCtrlX1() {
			return 0.;
		}

		@Pure
		@Override
		public double getCtrlY1() {
			return 0.;
		}

		@Pure
		@Override
		public double getCtrlZ1() {
			return 0.;
		}

		@Pure
		@Override
		public double getCtrlX2() {
			return 0.;
		}

		@Pure
		@Override
		public double getCtrlY2() {
			return 0.;
		}

		@Pure
		@Override
		public double getCtrlZ2() {
			return 0.;
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
		public DoubleProperty fromZProperty() {
			return this.fromZ;
		}

		@Pure
		@Override
		public DoubleProperty ctrlX1Property() {
			return null;
		}

		@Pure
		@Override
		public DoubleProperty ctrlY1Property() {
			return null;
		}

		@Pure
		@Override
		public DoubleProperty ctrlZ1Property() {
			return null;
		}

		@Pure
		@Override
		public DoubleProperty ctrlX2Property() {
			return null;
		}

		@Pure
		@Override
		public DoubleProperty ctrlY2Property() {
			return null;
		}

		@Pure
		@Override
		public DoubleProperty ctrlZ2Property() {
			return null;
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
	@SuppressWarnings("checkstyle:magicnumber")
	static class QuadPathElement3dfx extends PathElement3dfx {

		private static final long serialVersionUID = 4782822639304211439L;

		private final DoubleProperty fromX;

		private final DoubleProperty fromY;

		private final DoubleProperty fromZ;

		private final DoubleProperty ctrlX;

		private final DoubleProperty ctrlY;

		private final DoubleProperty ctrlZ;

		/**
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
		QuadPathElement3dfx(DoubleProperty fromx, DoubleProperty fromy, DoubleProperty fromz, DoubleProperty ctrlx,
		        DoubleProperty ctrly, DoubleProperty ctrlz, DoubleProperty tox, DoubleProperty toy, DoubleProperty toz) {
		    super(PathElementType.QUAD_TO, tox, toy, toz);
		    assert fromx != null : "fromX must be not null"; //$NON-NLS-1$
			assert fromy != null : "fromY must be not null"; //$NON-NLS-1$
			assert fromz != null : "fromZ must be not null"; //$NON-NLS-1$
			assert ctrlx != null : "ctrlX must be not null"; //$NON-NLS-1$
			assert ctrly != null : "ctrlY must be not null"; //$NON-NLS-1$
			assert ctrlz != null : "ctrlZ must be not null"; //$NON-NLS-1$
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
		public BooleanProperty isEmptyProperty() {
			if (this.isEmpty == null) {
				this.isEmpty = new ReadOnlyBooleanWrapper(this, "isEmpty"); //$NON-NLS-1$
				this.isEmpty.bind(Bindings.createBooleanBinding(() ->
				    MathUtil.isEpsilonEqual(fromXProperty().get(), toXProperty().get())
				            && MathUtil.isEpsilonEqual(fromYProperty().get(), toYProperty().get())
				            && MathUtil.isEpsilonEqual(fromZProperty().get(), toZProperty().get())
				            && MathUtil.isEpsilonEqual(ctrlX1Property().get(), toXProperty().get())
				            && MathUtil.isEpsilonEqual(ctrlY1Property().get(), toYProperty().get())
				            && MathUtil.isEpsilonEqual(ctrlZ1Property().get(), toZProperty().get()),
						fromXProperty(), toXProperty(), fromYProperty(), toYProperty(), fromZProperty(), toZProperty()));
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
			assert array != null : "Array must be not null"; //$NON-NLS-1$
			assert array.length >= 6 : "Array size is too small"; //$NON-NLS-1$
			array[0] = this.ctrlX.intValue();
			array[1] = this.ctrlY.intValue();
			array[2] = this.ctrlZ.intValue();
			array[3] = this.toX.intValue();
			array[4] = this.toY.intValue();
			array[5] = this.toZ.intValue();
		}

		@Pure
		@Override
		public void toArray(double[] array) {
			assert array != null : "Array must be not null"; //$NON-NLS-1$
			assert array.length >= 6 : "Array size is too small"; //$NON-NLS-1$
			array[0] = this.ctrlX.doubleValue();
			array[1] = this.ctrlY.doubleValue();
			array[2] = this.ctrlZ.doubleValue();
			array[4] = this.toX.doubleValue();
			array[5] = this.toY.doubleValue();
			array[6] = this.toZ.doubleValue();
		}

		@Pure
		@Override
		public void toArray(DoubleProperty[] array) {
			assert array != null : "Array must be not null"; //$NON-NLS-1$
			assert array.length >= 6 : "Array size is too small"; //$NON-NLS-1$
			array[0] = this.ctrlX;
			array[1] = this.ctrlY;
			array[2] = this.ctrlZ;
			array[3] = this.toX;
			array[4] = this.toY;
			array[5] = this.toZ;
		}

		@Pure
		@Override
		public DoubleProperty[] toArray() {
			return new DoubleProperty[] {this.ctrlX, this.ctrlY, this.ctrlZ, this.toX, this.toY, this.toZ};
		}

		@Pure
		@Override
		public String toString() {
			return "QUAD:(" //$NON-NLS-1$
					+ getFromX() + ";" //$NON-NLS-1$
					+ getFromY() + ";" //$NON-NLS-1$
					+ getFromZ() + ") >> (" //$NON-NLS-1$
					+ getCtrlX1() + ";" //$NON-NLS-1$
					+ getCtrlY1() + ";" //$NON-NLS-1$
					+ getCtrlZ1() + ") >> (" //$NON-NLS-1$
					+ getToX() + ";" //$NON-NLS-1$
					+ getToY() + ";" //$NON-NLS-1$
					+ getToZ() + ")"; //$NON-NLS-1$
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
		public double getFromZ() {
			return this.fromZ.get();
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
		public double getCtrlZ1() {
			return this.ctrlZ.get();
		}

		@Pure
		@Override
		public double getCtrlX2() {
			return 0.;
		}

		@Pure
		@Override
		public double getCtrlY2() {
			return 0.;
		}

		@Pure
		@Override
		public double getCtrlZ2() {
			return 0.;
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
		public DoubleProperty fromZProperty() {
			return this.fromZ;
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

		@Pure
		@Override
		public DoubleProperty ctrlZ1Property() {
			return this.ctrlZ;
		}

		@Pure
		@Override
		public DoubleProperty ctrlX2Property() {
			return null;
		}

		@Pure
		@Override
		public DoubleProperty ctrlY2Property() {
			return null;
		}

		@Pure
		@Override
		public DoubleProperty ctrlZ2Property() {
			return null;
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
	@SuppressWarnings("checkstyle:magicnumber")
	static class CurvePathElement3dfx extends PathElement3dfx {

		private static final long serialVersionUID = -2831895270995173092L;

		private final DoubleProperty fromX;

		private final DoubleProperty fromY;

		private final DoubleProperty fromZ;

		private final DoubleProperty ctrlX1;

		private final DoubleProperty ctrlY1;

		private final DoubleProperty ctrlZ1;

		private final DoubleProperty ctrlX2;

		private final DoubleProperty ctrlY2;

		private final DoubleProperty ctrlZ2;

		/**
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
		CurvePathElement3dfx(DoubleProperty fromx, DoubleProperty fromy, DoubleProperty fromz, DoubleProperty ctrlx1,
		        DoubleProperty ctrly1, DoubleProperty ctrlz1, DoubleProperty ctrlx2, DoubleProperty ctrly2, DoubleProperty ctrlz2,
		        DoubleProperty tox, DoubleProperty toy, DoubleProperty toz) {
			super(PathElementType.CURVE_TO, tox, toy, toz);
			assert fromx != null : "fromX must be not null"; //$NON-NLS-1$
			assert fromy != null : "fromY must be not null"; //$NON-NLS-1$
			assert fromz != null : "fromZ must be not null"; //$NON-NLS-1$
			assert ctrlx1 != null : "ctrlX1 must be not null"; //$NON-NLS-1$
			assert ctrly1 != null : "ctrlY1 must be not null"; //$NON-NLS-1$
			assert ctrlz1 != null : "ctrlZ1 must be not null"; //$NON-NLS-1$
			assert ctrlx2 != null : "ctrlX2 must be not null"; //$NON-NLS-1$
			assert ctrly2 != null : "ctrlY2 must be not null"; //$NON-NLS-1$
			assert ctrlz2 != null : "ctrlZ2 must be not null"; //$NON-NLS-1$
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
		public BooleanProperty isEmptyProperty() {
			if (this.isEmpty == null) {
				this.isEmpty = new ReadOnlyBooleanWrapper(this, "isEmpty"); //$NON-NLS-1$
				this.isEmpty.bind(Bindings.createBooleanBinding(() ->
				    MathUtil.isEpsilonEqual(fromXProperty().get(), toXProperty().get())
				            && MathUtil.isEpsilonEqual(fromYProperty().get(), toYProperty().get())
				            && MathUtil.isEpsilonEqual(fromZProperty().get(), toZProperty().get())
				            && MathUtil.isEpsilonEqual(ctrlX1Property().get(), toXProperty().get())
				            && MathUtil.isEpsilonEqual(ctrlY1Property().get(), toYProperty().get())
				            && MathUtil.isEpsilonEqual(ctrlZ1Property().get(), toZProperty().get())
				            && MathUtil.isEpsilonEqual(ctrlX2Property().get(), toXProperty().get())
				            && MathUtil.isEpsilonEqual(ctrlY2Property().get(), toYProperty().get())
				            && MathUtil.isEpsilonEqual(ctrlZ2Property().get(), toZProperty().get()),
						fromXProperty(), toXProperty(), fromYProperty(), toYProperty(), fromZProperty(), toZProperty()));
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
			assert array != null : "Array must be not null"; //$NON-NLS-1$
			assert array.length >= 9 : "Array size is too small"; //$NON-NLS-1$
			array[0] = this.ctrlX1.intValue();
			array[1] = this.ctrlY1.intValue();
			array[2] = this.ctrlZ1.intValue();
			array[3] = this.ctrlX2.intValue();
			array[4] = this.ctrlY2.intValue();
			array[5] = this.ctrlZ2.intValue();
			array[6] = this.toX.intValue();
			array[7] = this.toY.intValue();
			array[8] = this.toZ.intValue();
		}

		@Pure
		@Override
		public void toArray(DoubleProperty[] array) {
			assert array != null : "Array must be not null"; //$NON-NLS-1$
			assert array.length >= 6 : "Array size is too small"; //$NON-NLS-1$
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
		public void toArray(double[] array) {
			assert array != null : "Array must be not null"; //$NON-NLS-1$
			assert array.length >= 9 : "Array size is too small"; //$NON-NLS-1$
			array[0] = this.ctrlX1.doubleValue();
			array[1] = this.ctrlY1.doubleValue();
			array[2] = this.ctrlZ1.doubleValue();
			array[3] = this.ctrlX2.doubleValue();
			array[4] = this.ctrlY2.doubleValue();
			array[5] = this.ctrlZ2.doubleValue();
			array[6] = this.toX.doubleValue();
			array[7] = this.toY.doubleValue();
			array[8] = this.toZ.doubleValue();
		}

		@Pure
		@Override
		@SuppressWarnings("checkstyle:arraytrailingcomma")
		public DoubleProperty[] toArray() {
		    return new DoubleProperty[] {this.ctrlX1, this.ctrlY1, this.ctrlZ1, this.ctrlX2, this.ctrlY2, this.ctrlZ2, this.toX,
		                                 this.toY, this.toZ};
		}

		@Pure
		@Override
		public String toString() {
			return "CURVE:(" //$NON-NLS-1$
					+ getFromX() + ";" //$NON-NLS-1$
					+ getFromY() + ";" //$NON-NLS-1$
					+ getFromZ() + ") >> (" //$NON-NLS-1$
					+ getCtrlX1() + ";" //$NON-NLS-1$
					+ getCtrlY1() + ";" //$NON-NLS-1$
					+ getCtrlZ1() + ") >> (" //$NON-NLS-1$
					+ getCtrlX2() + ";" //$NON-NLS-1$
					+ getCtrlY2() + ";" //$NON-NLS-1$
					+ getCtrlZ2() + ") -> (" //$NON-NLS-1$
					+ getToX() + ";" //$NON-NLS-1$
					+ getToY() + ";" //$NON-NLS-1$
					+ getToZ() + ")"; //$NON-NLS-1$
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
		public double getFromZ() {
			return this.fromZ.get();
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
		public double getCtrlZ1() {
			return this.ctrlZ1.get();
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
		public double getCtrlZ2() {
			return this.ctrlZ2.get();
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
		public DoubleProperty fromZProperty() {
			return this.fromZ;
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
		public DoubleProperty ctrlZ1Property() {
			return this.ctrlZ1;
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

		@Pure
		@Override
		public DoubleProperty ctrlZ2Property() {
			return this.ctrlZ2;
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
	static class ClosePathElement3dfx extends PathElement3dfx {

		private static final long serialVersionUID = 5324688417590599323L;

		private final DoubleProperty fromX;

		private final DoubleProperty fromY;

		private final DoubleProperty fromZ;

		/**
		 * @param fromx x coordinate of the origin point.
		 * @param fromy y coordinate of the origin point.
		 * @param fromz z coordinate of the origin point.
		 * @param tox x coordinate of the target point.
		 * @param toy y coordinate of the target point.
		 * @param toz z coordinate of the target point.
		 */
		ClosePathElement3dfx(DoubleProperty fromx, DoubleProperty fromy, DoubleProperty fromz, DoubleProperty tox,
                DoubleProperty toy, DoubleProperty toz) {
			super(PathElementType.CLOSE, tox, toy, toz);
			assert fromx != null : "fromX must be not null"; //$NON-NLS-1$
			assert fromy != null : "fromY must be not null"; //$NON-NLS-1$
			assert fromz != null : "fromZ must be not null"; //$NON-NLS-1$
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
		public BooleanProperty isEmptyProperty() {
			if (this.isEmpty == null) {
				this.isEmpty = new ReadOnlyBooleanWrapper(this, "isEmpty"); //$NON-NLS-1$
				this.isEmpty.bind(Bindings.createBooleanBinding(() ->
				    MathUtil.isEpsilonEqual(fromXProperty().get(), toXProperty().get())
				            && MathUtil.isEpsilonEqual(fromYProperty().get(), toYProperty().get())
				            && MathUtil.isEpsilonEqual(fromZProperty().get(), toZProperty().get()),
						fromXProperty(), toXProperty(), fromYProperty(), toYProperty(), fromZProperty(), toZProperty()));
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
			assert array != null : "Array must be not null"; //$NON-NLS-1$
			assert array.length >= 3 : "Array size is too small"; //$NON-NLS-1$
			array[0] = (int) this.toX.get();
			array[1] = (int) this.toY.get();
			array[2] = (int) this.toZ.get();
		}

		@Pure
		@Override
		public void toArray(DoubleProperty[] array) {
			assert array != null : "Array must be not null"; //$NON-NLS-1$
			assert array.length >= 3 : "Array size is too small"; //$NON-NLS-1$
			array[0] = this.toX;
			array[1] = this.toY;
			array[2] = this.toZ;
		}

		@Pure
		@Override
		public void toArray(double[] array) {
			assert array != null : "Array must be not null"; //$NON-NLS-1$
			assert array.length >= 3 : "Array size is too small"; //$NON-NLS-1$
			array[0] = this.toX.get();
			array[1] = this.toY.get();
			array[2] = this.toZ.get();
		}

		@Pure
		@Override
		public DoubleProperty[] toArray() {
			return new DoubleProperty[] {this.toX, this.toY, this.toZ};
		}

		@Pure
		@Override
		public String toString() {
			return "CLOSE:(" //$NON-NLS-1$
					+ getFromX() + ";" //$NON-NLS-1$
					+ getFromY() + ";" //$NON-NLS-1$
					+ getFromZ() + ") -> (" //$NON-NLS-1$
					+ getToX() + ";" //$NON-NLS-1$
					+ getToY() + ";" //$NON-NLS-1$
					+ getToZ() + ")"; //$NON-NLS-1$
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
		public double getFromZ() {
			return this.fromZ.get();
		}

		@Pure
		@Override
		public double getCtrlX1() {
			return 0.;
		}

		@Pure
		@Override
		public double getCtrlY1() {
			return 0.;
		}

		@Pure
		@Override
		public double getCtrlZ1() {
			return 0.;
		}

		@Pure
		@Override
		public double getCtrlX2() {
			return 0.;
		}

		@Pure
		@Override
		public double getCtrlY2() {
			return 0.;
		}

		@Pure
		@Override
		public double getCtrlZ2() {
			return 0.;
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
		public DoubleProperty fromZProperty() {
			return this.fromZ;
		}

		@Pure
		@Override
		public DoubleProperty ctrlX1Property() {
			return null;
		}

		@Pure
		@Override
		public DoubleProperty ctrlY1Property() {
			return null;
		}

		@Pure
		@Override
		public DoubleProperty ctrlZ1Property() {
			return null;
		}

		@Pure
		@Override
		public DoubleProperty ctrlX2Property() {
			return null;
		}

		@Pure
		@Override
		public DoubleProperty ctrlY2Property() {
			return null;
		}

		@Pure
		@Override
		public DoubleProperty ctrlZ2Property() {
			return null;
		}

	}

}
