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

package org.arakhne.afc.math.geometry.d3.ifx;

import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.MathUtil;
import org.arakhne.afc.math.geometry.MathFXAttributeNames;
import org.arakhne.afc.math.geometry.PathElementType;
import org.arakhne.afc.math.geometry.d3.ai.PathElement3ai;
import org.arakhne.afc.vmutil.ReflectionUtil;
import org.arakhne.afc.vmutil.asserts.AssertMessages;

/** An element of the path with 3 integer FX properties.
 *
 * @author $Author: sgalland$
 * @author $Author: tpiotrow$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public abstract class PathElement3ifx implements PathElement3ai {

	private static final long serialVersionUID = -5532787413347691238L;


	/** Type of the element.
	 */
	protected final PathElementType type;

	/** Target point.
	 */
	protected Point3ifx to = new Point3ifx();

    /** Is Empty property.
     */
    protected ReadOnlyBooleanWrapper isEmpty;

	/** Constructor.
	 * @param type is the type of the element.
	 * @param tox the x coordinate of the target point.
	 * @param toy the y coordinate of the target point.
	 * @param toz the z coordinate of the target point.
	 */
	PathElement3ifx(PathElementType type, IntegerProperty tox, IntegerProperty toy, IntegerProperty toz) {
	    assert type != null : AssertMessages.notNullParameter(0);
	    assert tox != null : AssertMessages.notNullParameter(1);
	    assert toy != null : AssertMessages.notNullParameter(2);
	    assert toz != null : AssertMessages.notNullParameter(3);
	    this.type = type;
	    this.to.x = tox;
	    this.to.y = toy;
	    this.to.z = toz;
	}

	/** Constutor by setting.
	 * @param type is the type of the element.
	 * @param toPoint the point to set as the target point.
	 */
	PathElement3ifx(PathElementType type, Point3ifx toPoint) {
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
	public abstract int hashCode();

	/** Replies the x coordinate of the target point property.
	 *
	 * @return the x coordinate.
	 */
	@Pure
	public IntegerProperty toXProperty() {
		return this.to.xProperty();
	}

	/** Replies the y coordinate of the target point property.
	 *
	 * @return the y coordinate.
	 */
	@Pure
	public IntegerProperty toYProperty() {
		return this.to.yProperty();
	}

	/** Replies the z coordinate of the target point property.
	 *
	 * @return the z coordinate.
	 */
	@Pure
	public IntegerProperty toZProperty() {
		return this.to.zProperty();
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

	@Override
	@Pure
	public final int getToZ() {
		return this.to.iz();
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

	/** Replies the z coordinate of the starting point property.
	 *
	 * @return the z coordinate, or <code>null</code> if the type is {@link PathElementType#MOVE_TO}.
	 */
	@Pure
	public abstract IntegerProperty fromZProperty();

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

	/** Replies the z coordinate of the first control point property.
	 *
	 * @return the z coordinate, or {@link Double#NaN} if the type is {@link PathElementType#MOVE_TO},
	 * {@link PathElementType#LINE_TO}, or {@link PathElementType#CLOSE}.
	 */
	@Pure
	public abstract IntegerProperty ctrlZ1Property();

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

	/** Replies the z coordinate of the second  control point property.
	 *
	 * @return the z coordinate, or <code>null</code> if the type is {@link PathElementType#MOVE_TO},
	 * {@link PathElementType#LINE_TO}, {@link PathElementType#QUAD_TO}, or {@link PathElementType#CLOSE}.
	 */
	@Pure
	public abstract IntegerProperty ctrlZ2Property();

	/** An element of the path that represents a <code>MOVE_TO</code>.
	 *
	 * @author $Author: sgalland$
	 * @author $Author: tpiotrow$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 13.0
	 */
	static class MovePathElement3ifx extends PathElement3ifx {

		private static final long serialVersionUID = 7240290153738547626L;

		/** Constructor.
		 * @param tox x coordinate of the target point.
		 * @param toy y coordinate of the target point.
		 * @param toz z coordinate of the target point.
		 */
		MovePathElement3ifx(IntegerProperty tox, IntegerProperty toy, IntegerProperty toz) {
		    super(PathElementType.MOVE_TO, tox, toy, toz);
		}

		/** Constructor by setting.
         * @param toPoint the point to set as the target point.
         */
		MovePathElement3ifx(Point3ifx toPoint) {
			super(PathElementType.MOVE_TO, toPoint);
		}

		@Pure
		@Override
		public boolean equals(Object obj) {
			try {
				final PathElement3ai elt = (PathElement3ai) obj;
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
			int bits = 1;
			bits = 31 * bits + this.type.ordinal();
			bits = 31 * bits + getToX();
			bits = 31 * bits + getToY();
			bits = 31 * bits + getToZ();
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
			assert array.length >= 3 : AssertMessages.tooSmallArrayParameter(0, 3);
			array[0] = this.to.ix();
			array[1] = this.to.iy();
			array[2] = this.to.iz();
		}

		@Pure
		@Override
		public void toArray(double[] array) {
			assert array != null : AssertMessages.notNullParameter();
			assert array.length >= 3 : AssertMessages.tooSmallArrayParameter(0, 3);
			array[0] = this.to.getX();
			array[1] = this.to.getY();
			array[2] = this.to.getZ();
		}

		@Pure
		@Override
		public void toArray(IntegerProperty[] array) {
			assert array != null : AssertMessages.notNullParameter();
			assert array.length >= 3 : AssertMessages.tooSmallArrayParameter(0, 3);
			array[0] = this.to.xProperty();
			array[1] = this.to.yProperty();
			array[2] = this.to.zProperty();
		}

		@Pure
		@Override
		public IntegerProperty[] toArray() {
			return new IntegerProperty[] {this.to.xProperty(), this.to.yProperty(), this.to.zProperty()};
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
		public int getFromZ() {
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
		public int getCtrlZ1() {
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
		public int getCtrlZ2() {
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
		public IntegerProperty fromZProperty() {
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
		public IntegerProperty ctrlZ1Property() {
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

		@Pure
		@Override
		public IntegerProperty ctrlZ2Property() {
			return null;
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
	static class LinePathElement2ifx extends PathElement3ifx {

		private static final long serialVersionUID = 2010270547259311623L;

		private Point3ifx from = new Point3ifx();

		/** Constructor.
		 * @param fromx x coordinate of the origin point.
		 * @param fromy y coordinate of the origin point.
		 * @param fromz z coordinate of the origin point.
		 * @param tox x coordinate of the target point.
		 * @param toy y coordinate of the target point.
		 * @param toz z coordinate of the target point.
		 */
		LinePathElement2ifx(IntegerProperty fromx, IntegerProperty fromy, IntegerProperty fromz, IntegerProperty tox,
		        IntegerProperty toy, IntegerProperty toz) {
		    super(PathElementType.LINE_TO, tox, toy, toz);
		    assert fromx != null : AssertMessages.notNullParameter(0);
		    assert fromy != null : AssertMessages.notNullParameter(1);
		    assert fromz != null : AssertMessages.notNullParameter(2);
		    this.from.x = fromx;
		    this.from.y = fromy;
		    this.from.z = fromz;
		}

		/** Constructor by setting.
		 * @param fromPoint the point to set as the origin point.
		 * @param toPoint the point to set as the target point.
		 */
		LinePathElement2ifx(Point3ifx fromPoint, Point3ifx toPoint) {
		    super(PathElementType.LINE_TO, toPoint);
		    assert fromPoint != null : AssertMessages.notNullParameter(0);
		    this.from = fromPoint;
		}

		@Pure
		@Override
		public boolean equals(Object obj) {
			try {
				final PathElement3ai elt = (PathElement3ai) obj;
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
			int bits = 1;
			bits = 31 * bits + this.type.ordinal();
			bits = 31 * bits + getToX();
			bits = 31 * bits + getToY();
			bits = 31 * bits + getToZ();
			bits = 31 * bits + getFromX();
			bits = 31 * bits + getFromY();
			bits = 31 * bits + getFromZ();
			return bits ^ (bits >> 31);
		}

        @Pure
        @Override
        public BooleanProperty isEmptyProperty() {
            if (this.isEmpty == null) {
                this.isEmpty = new ReadOnlyBooleanWrapper(this, MathFXAttributeNames.IS_EMPTY);
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
			assert array != null : AssertMessages.notNullParameter();
			assert array.length >= 3 : AssertMessages.tooSmallArrayParameter(0, 3);
			array[0] = this.to.ix();
			array[1] = this.to.iy();
			array[2] = this.to.iz();
		}

		@Pure
		@Override
		public void toArray(double[] array) {
			assert array != null : AssertMessages.notNullParameter();
			assert array.length >= 3 : AssertMessages.tooSmallArrayParameter(0, 3);
			array[0] = this.to.getX();
			array[1] = this.to.getY();
			array[2] = this.to.getZ();
		}

		@Pure
		@Override
		public IntegerProperty[] toArray() {
			return new IntegerProperty[] {this.to.xProperty(), this.to.yProperty(), this.to.zProperty()};
		}

		@Pure
		@Override
		public void toArray(IntegerProperty[] array) {
			assert array != null : AssertMessages.notNullParameter();
			assert array.length >= 3 : AssertMessages.tooSmallArrayParameter(0, 3);
			array[0] = this.to.xProperty();
			array[1] = this.to.xProperty();
			array[2] = this.to.xProperty();
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
		public int getFromZ() {
			return this.from.iz();
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
		public int getCtrlZ1() {
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
		public int getCtrlZ2() {
			return 0;
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

		@Pure
		@Override
		public IntegerProperty fromZProperty() {
			return this.from.zProperty();
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
		public IntegerProperty ctrlZ1Property() {
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

		@Pure
		@Override
		public IntegerProperty ctrlZ2Property() {
			return null;
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
	@SuppressWarnings("checkstyle:magicnumber")
	static class QuadPathElement3ifx extends PathElement3ifx {

		private static final long serialVersionUID = -1243640360335101578L;

		private Point3ifx from = new Point3ifx();

		private Point3ifx ctrl = new Point3ifx();

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
		@SuppressWarnings("checkstyle:magicnumber")
		QuadPathElement3ifx(IntegerProperty fromx, IntegerProperty fromy, IntegerProperty fromz, IntegerProperty ctrlx,
		        IntegerProperty ctrly, IntegerProperty ctrlz, IntegerProperty tox, IntegerProperty toy, IntegerProperty toz) {
		    super(PathElementType.QUAD_TO, tox, toy, toz);
		    assert fromx != null : AssertMessages.notNullParameter(0);
		    assert fromy != null : AssertMessages.notNullParameter(1);
		    assert fromz != null : AssertMessages.notNullParameter(2);
		    assert ctrlx != null : AssertMessages.notNullParameter(3);
		    assert ctrly != null : AssertMessages.notNullParameter(4);
		    assert ctrlz != null : AssertMessages.notNullParameter(5);
		    this.from.x = fromx;
		    this.from.y = fromy;
		    this.from.z = fromz;
		    this.ctrl.x = ctrlx;
		    this.ctrl.y = ctrly;
		    this.ctrl.z = ctrlz;
		}

		/** Constructor by setting.
		 * @param fromPoint the point to set as the origin point.
		 * @param ctrlPoint the point to set as the control point.
		 * @param toPoint the point to set as the target point.
		 */
		@SuppressWarnings("checkstyle:magicnumber")
		QuadPathElement3ifx(Point3ifx fromPoint, Point3ifx ctrlPoint, Point3ifx toPoint) {
		    super(PathElementType.QUAD_TO, toPoint);
		    assert fromPoint != null : AssertMessages.notNullParameter(0);
			assert ctrlPoint != null : AssertMessages.notNullParameter(1);
			this.from = fromPoint;
			this.ctrl = ctrlPoint;
		}

		@Pure
		@Override
		public boolean equals(Object obj) {
			try {
				final PathElement3ai elt = (PathElement3ai) obj;
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
			int bits = 1;
			bits = 31 * bits + this.type.ordinal();
			bits = 31 * bits + getToX();
			bits = 31 * bits + getToY();
			bits = 31 * bits + getToZ();
			bits = 31 * bits + getCtrlX1();
			bits = 31 * bits + getCtrlY1();
			bits = 31 * bits + getCtrlZ1();
			bits = 31 * bits + getFromX();
			bits = 31 * bits + getFromY();
			bits = 31 * bits + getFromZ();
			return bits ^ (bits >> 31);
		}

        @Pure
        @Override
        public BooleanProperty isEmptyProperty() {
            if (this.isEmpty == null) {
                this.isEmpty = new ReadOnlyBooleanWrapper(this, MathFXAttributeNames.IS_EMPTY);
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
			assert array != null : AssertMessages.notNullParameter();
			assert array.length >= 6 : AssertMessages.tooSmallArrayParameter(0, 6);
			array[0] = this.ctrl.ix();
			array[1] = this.ctrl.iy();
			array[2] = this.ctrl.iz();
			array[3] = this.to.ix();
			array[4] = this.to.iy();
			array[5] = this.to.iz();
		}

		@Pure
		@Override
		public void toArray(double[] array) {
			assert array != null : AssertMessages.notNullParameter();
			assert array.length >= 6 : AssertMessages.tooSmallArrayParameter(0, 6);
			array[0] = this.ctrl.getX();
			array[1] = this.ctrl.getY();
			array[2] = this.ctrl.getZ();
			array[3] = this.to.getX();
			array[4] = this.to.getY();
			array[5] = this.to.getZ();
		}

		@Pure
		@Override
		public IntegerProperty[] toArray() {
            return new IntegerProperty[] {this.ctrl.xProperty(), this.ctrl.yProperty(), this.ctrl.zProperty(),
                    this.to.xProperty(), this.to.yProperty(), this.to.zProperty(), };
		}

		@Pure
		@Override
		public void toArray(IntegerProperty[] array) {
			assert array != null : AssertMessages.notNullParameter();
			assert array.length >= 6 : AssertMessages.tooSmallArrayParameter(0, 6);
			array[0] = this.ctrl.xProperty();
			array[1] = this.ctrl.yProperty();
			array[2] = this.ctrl.zProperty();
			array[3] = this.to.xProperty();
			array[4] = this.to.yProperty();
			array[5] = this.to.zProperty();
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
		public int getFromZ() {
			return this.from.iz();
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
		public int getCtrlZ1() {
			return this.ctrl.iz();
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
		public int getCtrlZ2() {
			return 0;
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

		@Pure
		@Override
		public IntegerProperty fromZProperty() {
			return this.from.zProperty();
		}

		@Pure
		@Override
		public IntegerProperty ctrlX1Property() {
			return this.ctrl.xProperty();
		}

		@Pure
		@Override
		public IntegerProperty ctrlY1Property() {
			return this.ctrl.yProperty();
		}

		@Pure
		@Override
		public IntegerProperty ctrlZ1Property() {
			return this.ctrl.zProperty();
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

		@Pure
		@Override
		public IntegerProperty ctrlZ2Property() {
			return null;
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
	@SuppressWarnings("checkstyle:magicnumber")
	static class CurvePathElement3ifx extends PathElement3ifx {

		private static final long serialVersionUID = 6354626635759607041L;

		private Point3ifx from = new Point3ifx();

		private Point3ifx ctrl1 = new Point3ifx();

		private Point3ifx ctrl2 = new Point3ifx();

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
		@SuppressWarnings({"checkstyle:parameternumber", "checkstyle:magicnumber"})
		CurvePathElement3ifx(IntegerProperty fromx, IntegerProperty fromy, IntegerProperty fromz, IntegerProperty ctrlx1,
		        IntegerProperty ctrly1, IntegerProperty ctrlz1, IntegerProperty ctrlx2, IntegerProperty ctrly2,
		        IntegerProperty ctrlz2, IntegerProperty tox, IntegerProperty toy, IntegerProperty toz) {
		    super(PathElementType.CURVE_TO, tox, toy, toz);
		    assert fromx != null : AssertMessages.notNullParameter(0);
		    assert fromy != null : AssertMessages.notNullParameter(1);
		    assert fromz != null : AssertMessages.notNullParameter(2);
		    assert ctrlx1 != null : AssertMessages.notNullParameter(3);
		    assert ctrly1 != null : AssertMessages.notNullParameter(4);
		    assert ctrlz1 != null : AssertMessages.notNullParameter(5);
		    assert ctrlx2 != null : AssertMessages.notNullParameter(6);
		    assert ctrly2 != null : AssertMessages.notNullParameter(7);
		    assert ctrlz2 != null : AssertMessages.notNullParameter(8);
		    this.from.x = fromx;
		    this.from.y = fromy;
		    this.from.z = fromz;
		    this.ctrl1.x = ctrlx1;
		    this.ctrl1.y = ctrly1;
		    this.ctrl1.z = ctrlz1;
		    this.ctrl2.x = ctrlx2;
		    this.ctrl2.y = ctrly2;
		    this.ctrl2.z = ctrlz2;
		}

		/** Constructor by setting.
		 * @param fromPoint the point to set as the origin point.
		 * @param ctrl1Point the point to set as the first control point.
		 * @param ctrl2Point the point to set as the second control point.
		 * @param toPoint the point to set as the target point.
		 */
		@SuppressWarnings({"checkstyle:parameternumber", "checkstyle:magicnumber"})
		CurvePathElement3ifx(Point3ifx fromPoint, Point3ifx ctrl1Point, Point3ifx ctrl2Point, Point3ifx toPoint) {
			super(PathElementType.CURVE_TO, toPoint);
			assert fromPoint != null : AssertMessages.notNullParameter(0);
			assert ctrl1Point != null : AssertMessages.notNullParameter(1);
			assert ctrl2Point != null : AssertMessages.notNullParameter(2);
			assert toPoint != null : AssertMessages.notNullParameter(3);
			this.from = fromPoint;
			this.ctrl1 = ctrl1Point;
			this.ctrl2 = ctrl2Point;
		}

		@Pure
		@Override
		public boolean equals(Object obj) {
			try {
				final PathElement3ai elt = (PathElement3ai) obj;
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
			int bits = 1;
			bits = 31 * bits + this.type.ordinal();
			bits = 31 * bits + getToX();
			bits = 31 * bits + getToY();
			bits = 31 * bits + getToZ();
			bits = 31 * bits + getCtrlX1();
			bits = 31 * bits + getCtrlY1();
			bits = 31 * bits + getCtrlZ1();
			bits = 31 * bits + getCtrlX2();
			bits = 31 * bits + getCtrlY2();
			bits = 31 * bits + getCtrlZ2();
			bits = 31 * bits + getFromX();
			bits = 31 * bits + getFromY();
			bits = 31 * bits + getFromZ();
			return bits ^ (bits >> 31);
		}

        @Pure
        @Override
        @SuppressWarnings("checkstyle:booleanexpressioncomplexity")
        public BooleanProperty isEmptyProperty() {
            if (this.isEmpty == null) {
                this.isEmpty = new ReadOnlyBooleanWrapper(this, MathFXAttributeNames.IS_EMPTY);
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
			assert array != null : AssertMessages.notNullParameter();
			assert array.length >= 9 : AssertMessages.tooSmallArrayParameter(0, 9);
			array[0] = this.ctrl1.ix();
			array[1] = this.ctrl1.iy();
			array[2] = this.ctrl1.iz();
			array[3] = this.ctrl2.ix();
			array[4] = this.ctrl2.iy();
			array[5] = this.ctrl2.iz();
			array[6] = this.to.ix();
			array[7] = this.to.iy();
			array[8] = this.to.iz();
		}

		@Pure
		@Override
		public void toArray(double[] array) {
			assert array != null : AssertMessages.notNullParameter();
			assert array.length >= 9 : AssertMessages.tooSmallArrayParameter(0, 9);
			array[0] = this.ctrl1.getX();
			array[1] = this.ctrl1.getY();
			array[2] = this.ctrl1.getZ();
			array[3] = this.ctrl2.getX();
			array[4] = this.ctrl2.getY();
			array[5] = this.ctrl2.getZ();
			array[6] = this.to.getX();
			array[7] = this.to.getY();
			array[8] = this.to.getZ();
		}

		@Pure
		@Override
		@SuppressWarnings("checkstyle:arraytrailingcomma")
		public IntegerProperty[] toArray() {
            return new IntegerProperty[] {this.ctrl1.xProperty(), this.ctrl1.yProperty(), this.ctrl1.zProperty(),
                    this.ctrl2.xProperty(), this.ctrl2.yProperty(), this.ctrl2.zProperty(), this.to.xProperty(),
                    this.to.yProperty(), this.to.zProperty() };
		}

		@Pure
		@Override
		public void toArray(IntegerProperty[] array) {
			assert array != null : AssertMessages.notNullParameter();
			assert array.length >= 9 : AssertMessages.tooSmallArrayParameter(0, 9);
			array[0] = this.ctrl1.xProperty();
			array[1] = this.ctrl1.yProperty();
			array[2] = this.ctrl1.zProperty();
			array[3] = this.ctrl2.xProperty();
			array[4] = this.ctrl2.yProperty();
			array[5] = this.ctrl2.zProperty();
			array[6] = this.to.xProperty();
			array[7] = this.to.yProperty();
			array[8] = this.to.zProperty();
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
		public int getFromZ() {
			return this.from.iz();
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
		public int getCtrlZ1() {
			return this.ctrl1.iz();
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
		public int getCtrlZ2() {
			return this.ctrl2.iz();
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

		@Pure
		@Override
		public IntegerProperty fromZProperty() {
			return this.from.zProperty();
		}

		@Pure
		@Override
		public IntegerProperty ctrlX1Property() {
			return this.ctrl1.xProperty();
		}

		@Pure
		@Override
		public IntegerProperty ctrlY1Property() {
			return this.ctrl1.yProperty();
		}

		@Pure
		@Override
		public IntegerProperty ctrlZ1Property() {
			return this.ctrl1.zProperty();
		}

		@Pure
		@Override
		public IntegerProperty ctrlX2Property() {
			return this.ctrl2.xProperty();
		}

		@Pure
		@Override
		public IntegerProperty ctrlY2Property() {
			return this.ctrl2.yProperty();
		}

		@Pure
		@Override
		public IntegerProperty ctrlZ2Property() {
			return this.ctrl2.zProperty();
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
	static class ClosePathElement3ifx extends PathElement3ifx {

		private static final long serialVersionUID = 5424862699995343827L;

		private Point3ifx from = new Point3ifx();

		/** Constructor.
		 * @param fromx x coordinate of the origin point.
		 * @param fromy y coordinate of the origin point.
		 * @param fromz z coordinate of the origin point.
		 * @param tox x coordinate of the target point.
		 * @param toy y coordinate of the target point.
		 * @param toz z coordinate of the target point.
		 */
		ClosePathElement3ifx(IntegerProperty fromx, IntegerProperty fromy, IntegerProperty fromz, IntegerProperty tox,
		        IntegerProperty toy, IntegerProperty toz) {
		    super(PathElementType.CLOSE, tox, toy, toz);
		    assert fromx != null : AssertMessages.notNullParameter(0);
		    assert fromy != null : AssertMessages.notNullParameter(1);
		    assert fromz != null : AssertMessages.notNullParameter(2);
		    this.from.x = fromx;
		    this.from.y = fromy;
		    this.from.z = fromz;
		}

		/** Constructor by setting.
		 * @param fromPoint the point to set as the origin point.
		 * @param toPoint the point to set as the target point.
		 */
		ClosePathElement3ifx(Point3ifx fromPoint, Point3ifx toPoint) {
		    super(PathElementType.CLOSE, toPoint);
		    assert fromPoint != null : AssertMessages.notNullParameter(0);
			this.from = fromPoint;
		}

		@Pure
		@Override
		public boolean equals(Object obj) {
			try {
				final PathElement3ai elt = (PathElement3ai) obj;
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
			int bits = 1;
			bits = 31 * bits + this.type.ordinal();
			bits = 31 * bits + getToX();
			bits = 31 * bits + getToY();
			bits = 31 * bits + getToZ();
			bits = 31 * bits + getFromX();
			bits = 31 * bits + getFromY();
			bits = 31 * bits + getFromZ();
			return bits ^ (bits >> 31);
		}

        @Pure
        @Override
        public BooleanProperty isEmptyProperty() {
            if (this.isEmpty == null) {
                this.isEmpty = new ReadOnlyBooleanWrapper(this, MathFXAttributeNames.IS_EMPTY);
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
			assert array != null : AssertMessages.notNullParameter();
			assert array.length >= 3 : AssertMessages.tooSmallArrayParameter(0, 3);
			array[0] = this.to.ix();
			array[1] = this.to.iy();
			array[2] = this.to.iz();
		}

		@Pure
		@Override
		public void toArray(double[] array) {
			assert array != null : AssertMessages.notNullParameter();
			assert array.length >= 3 : AssertMessages.tooSmallArrayParameter(0, 3);
			array[0] = this.to.getX();
			array[1] = this.to.getY();
			array[2] = this.to.getZ();
		}

		@Pure
		@Override
		public IntegerProperty[] toArray() {
			return new IntegerProperty[] {this.to.xProperty(), this.to.yProperty(), this.to.zProperty()};
		}

		@Pure
		@Override
		public void toArray(IntegerProperty[] array) {
			assert array != null : AssertMessages.notNullParameter();
			assert array.length >= 3 : AssertMessages.tooSmallArrayParameter(0, 3);
			array[0] = this.to.xProperty();
			array[1] = this.to.yProperty();
			array[2] = this.to.zProperty();
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
		public int getFromZ() {
			return this.from.iz();
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
		public int getCtrlZ1() {
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
		public int getCtrlZ2() {
			return 0;
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

		@Pure
		@Override
		public IntegerProperty fromZProperty() {
			return this.from.zProperty();
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
		public IntegerProperty ctrlZ1Property() {
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

		@Pure
		@Override
		public IntegerProperty ctrlZ2Property() {
			return null;
		}

	}

}
