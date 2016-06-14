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

package org.arakhne.afc.math.geometry.d3.i;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.geometry.PathElementType;
import org.arakhne.afc.math.geometry.d3.ai.PathElement3ai;
import org.arakhne.afc.vmutil.ReflectionUtil;

/**
 * An element of the path with 2 integer numbers.
 *
 * @author $Author: sgalland$
 * @author $Author: tpiotrow$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
@SuppressWarnings("checkstyle:magicnumber")
public abstract class PathElement3i implements PathElement3ai {

    private static final long serialVersionUID = -7762354100984227855L;

    /**
     * Type of the element.
     */
    protected final PathElementType type;

    /**
     * Target point.
     */
    protected final int toX;

    /**
     * Target point.
     */
    protected final int toY;

    /**
     * Target point.
     */
    protected final int toZ;

    /**
     * @param type
     *            is the type of the element.
     * @param tox
     *            the x coordinate of the target point.
     * @param toy
     *            the x coordinate of the target point.
     * @param toz
     *            the z coordinate of the target point.
     */
    PathElement3i(PathElementType type, int tox, int toy, int toz) {
        assert type != null : "Path element type must be not null";
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
    public final int getToX() {
        return this.toX;
    }

    @Override
    @Pure
    public final int getToY() {
        return this.toY;
    }

    @Override
    @Pure
    public final int getToZ() {
        return this.toZ;
    }

    @Pure
    @Override
    public final PathElementType getType() {
        return this.type;
    }

    /**
     * Copy the coords into an array, except the source point.
     *
     * @return the array of the points, except the source point.
     */
    @Pure
    public abstract int[] toArray();

    /**
     * An element of the path that represents a <code>MOVE_TO</code>.
     *
     * @author $Author: sgalland$
     * @author $Author: tpiotrow$
     * @version $FullVersion$
     * @mavengroupid $GroupId$
     * @mavenartifactid $ArtifactId$
     * @since 13.0
     */
    static class MovePathElement3i extends PathElement3i {

        private static final long serialVersionUID = -574266368740822686L;

        /**
         * @param tox
         *            x coordinate of the target point.
         * @param toy
         *            y coordinate of the target point.
         * @param toz
         *            z coordinate of the target point.
         */
        MovePathElement3i(int tox, int toy, int toz) {
            super(PathElementType.MOVE_TO, tox, toy, toz);
        }

        @Pure
        @Override
        public boolean equals(Object obj) {
            try {
                final PathElement3ai elt = (PathElement3ai) obj;
                return getType() == elt.getType() && getToX() == elt.getToX() && getToY() == elt.getToY()
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
            assert array != null : "Array must be not null";
            assert array.length >= 3 : "Array size is too small";
            array[0] = this.toX;
            array[1] = this.toY;
            array[2] = this.toZ;
        }

        @Override
        public void toArray(double[] array) {
            assert array != null : "Array must be not null";
            assert array.length >= 3 : "Array size is too small";
            array[0] = this.toX;
            array[1] = this.toY;
            array[2] = this.toZ;
        }

        @Pure
        @Override
        public int[] toArray() {
            return new int[] {this.toX, this.toY, this.toZ};
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
        public int getFromZ() {
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
        public int getCtrlZ1() {
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
        public int getCtrlZ2() {
            return 0;
        }

    }

    /**
     * An element of the path that represents a <code>LINE_TO</code>.
     *
     * @author $Author: sgalland$
     * @author $Author: tpiotrow$
     * @version $FullVersion$
     * @mavengroupid $GroupId$
     * @mavenartifactid $ArtifactId$
     * @since 13.0
     */
    static class LinePathElement3i extends PathElement3i {

        private static final long serialVersionUID = 7733931118894880566L;

        private final int fromX;

        private final int fromY;

        private final int fromZ;

        /**
         * @param fromx x coordinate of the origin point.
         * @param fromy y coordinate of the origin point.
         * @param fromz z coordinate of the origin point.
         * @param tox x coordinate of the target point.
         * @param toy y coordinate of the target point.
         * @param toz z coordinate of the target point.
         */
        LinePathElement3i(int fromx, int fromy, int fromz, int tox, int toy, int toz) {
            super(PathElementType.LINE_TO, tox, toy, toz);
            this.fromX = fromx;
            this.fromY = fromy;
            this.fromZ = fromz;
        }

        @Pure
        @Override
        public boolean equals(Object obj) {
            try {
                final PathElement3ai elt = (PathElement3ai) obj;
                return getType() == elt.getType() && getToX() == elt.getToX() && getToY() == elt.getToY()
                        && getToZ() == elt.getToZ() && getFromX() == elt.getFromX() && getFromY() == elt.getFromY()
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
            return bits ^ bits >> 32;
        }

        @Pure
        @Override
        public boolean isEmpty() {
            return (this.fromX == this.toX) && (this.fromY == this.toY) && (this.fromZ == this.toZ);
        }

        @Pure
        @Override
        public boolean isDrawable() {
            return !isEmpty();
        }

        @Override
        public void toArray(int[] array) {
            assert array != null : "Array must be not null";
            assert array.length >= 3 : "Array size is too small";
            array[0] = this.toX;
            array[1] = this.toY;
            array[2] = this.toZ;
        }

        @Override
        public void toArray(double[] array) {
            assert array != null : "Array must be not null";
            assert array.length >= 3 : "Array size is too small";
            array[0] = this.toX;
            array[1] = this.toY;
            array[2] = this.toZ;
        }

        @Pure
        @Override
        public int[] toArray() {
            return new int[] {this.toX, this.toY, this.toZ};
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
        public int getFromZ() {
            return this.fromZ;
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
        public int getCtrlZ1() {
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
        public int getCtrlZ2() {
            return 0;
        }

    }

    /**
     * An element of the path that represents a <code>QUAD_TO</code>.
     *
     * @author $Author: sgalland$
     * @author $Author: tpiotrow$
     * @version $FullVersion$
     * @mavengroupid $GroupId$
     * @mavenartifactid $ArtifactId$
     * @since 13.0
     */
    static class QuadPathElement3i extends PathElement3i {

        private static final long serialVersionUID = 3335394189817197203L;

        private final int fromX;

        private final int fromY;

        private final int fromZ;

        private final int ctrlX;

        private final int ctrlY;

        private final int ctrlZ;

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
        QuadPathElement3i(int fromx, int fromy, int fromz, int ctrlx, int ctrly, int ctrlz, int tox, int toy, int toz) {
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
                final PathElement3ai elt = (PathElement3ai) obj;
                return getType() == elt.getType() && getToX() == elt.getToX() && getToY() == elt.getToY()
                        && getToZ() == elt.getToZ() && getCtrlX1() == elt.getCtrlX1() && getCtrlY1() == elt.getCtrlY1()
                        && getCtrlZ1() == elt.getCtrlZ1() && getFromX() == elt.getFromX() && getFromY() == elt.getFromY()
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
            return bits ^ bits >> 32;
        }

        @Pure
        @Override
        public boolean isEmpty() {
            return (this.fromX == this.toX) && (this.fromY == this.toY) && (this.ctrlX == this.toX) && (this.ctrlY == this.toY)
                    && (this.ctrlZ == this.toZ) && (this.ctrlZ == this.toZ);
        }

        @Pure
        @Override
        public boolean isDrawable() {
            return !isEmpty();
        }

        @Override
        public void toArray(int[] array) {
            assert array != null : "Array must be not null";
            assert array.length >= 6 : "Array size is too small";
            array[0] = this.ctrlX;
            array[1] = this.ctrlY;
            array[2] = this.ctrlZ;
            array[3] = this.toX;
            array[4] = this.toY;
            array[5] = this.toZ;
        }

        @Override
        public void toArray(double[] array) {
            assert array != null : "Array must be not null";
            assert array.length >= 6 : "Array size is too small";
            array[0] = this.ctrlX;
            array[1] = this.ctrlY;
            array[2] = this.ctrlZ;
            array[3] = this.toX;
            array[4] = this.toY;
            array[5] = this.toZ;
        }

        @Pure
        @Override
        public int[] toArray() {
            return new int[] {this.ctrlX, this.ctrlY, this.ctrlZ, this.toX, this.toY, this.toZ};
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
        public int getFromZ() {
            return this.fromZ;
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
        public int getCtrlZ1() {
            return this.ctrlZ;
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
        public int getCtrlZ2() {
            return 0;
        }

    }

    /**
     * An element of the path that represents a <code>CURVE_TO</code>.
     *
     * @author $Author: sgalland$
     * @author $Author: tpiotrow$
     * @version $FullVersion$
     * @mavengroupid $GroupId$
     * @mavenartifactid $ArtifactId$
     * @since 13.0
     */
    static class CurvePathElement3i extends PathElement3i {

        private static final long serialVersionUID = 7009674542781709373L;

        private final int fromX;

        private final int fromY;

        private final int fromZ;

        private final int ctrlX1;

        private final int ctrlY1;

        private final int ctrlZ1;

        private final int ctrlX2;

        private final int ctrlY2;

        private final int ctrlZ2;

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
        CurvePathElement3i(int fromx, int fromy, int fromz, int ctrlx1, int ctrly1, int ctrlz1, int ctrlx2, int ctrly2,
                int ctrlz2, int tox, int toy, int toz) {
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
                final PathElement3ai elt = (PathElement3ai) obj;
                return getType() == elt.getType() && getToX() == elt.getToX() && getToY() == elt.getToY()
                        && getToZ() == elt.getToZ() && getCtrlX1() == elt.getCtrlX1() && getCtrlY1() == elt.getCtrlY1()
                        && getCtrlZ1() == elt.getCtrlZ1() && getCtrlX2() == elt.getCtrlX2() && getCtrlY2() == elt.getCtrlY2()
                        && getCtrlZ2() == elt.getCtrlZ2() && getFromX() == elt.getFromX() && getFromY() == elt.getFromY()
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
            return bits ^ bits >> 32;
        }

        @Pure
        @Override
        @SuppressWarnings("checkstyle:booleanexpressioncomplexity")
        public boolean isEmpty() {
            return (this.fromX == this.toX) && (this.fromY == this.toY) && (this.ctrlX1 == this.toX) && (this.ctrlY1 == this.toY)
                    && (this.ctrlZ1 == this.toZ) && (this.ctrlX2 == this.toX) && (this.ctrlY2 == this.toY)
                    && (this.ctrlZ2 == this.toZ);
        }

        @Pure
        @Override
        public boolean isDrawable() {
            return !isEmpty();
        }

        @Override
        public void toArray(int[] array) {
            assert array != null : "Array must be not null";
            assert array.length >= 9 : "Array size is too small";
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

        @Override
        public void toArray(double[] array) {
            assert array != null : "Array must be not null";
            assert array.length >= 9 : "Array size is too small";
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
        public int[] toArray() {
            return new int[] {this.ctrlX1, this.ctrlY1, this.ctrlZ1, this.ctrlX2, this.ctrlY2, this.ctrlZ2,
                this.toX, this.toY, this.toZ};
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
        public int getFromZ() {
            return this.fromZ;
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
        public int getCtrlZ1() {
            return this.ctrlZ1;
        }

        @Override
        public int getCtrlX2() {
            return this.ctrlX2;
        }

        @Override
        public int getCtrlY2() {
            return this.ctrlY2;
        }

        @Override
        public int getCtrlZ2() {
            return this.ctrlZ2;
        }

    }

    /**
     * An element of the path that represents a <code>CLOSE</code>.
     *
     * @author $Author: sgalland$
     * @author $Author: tpiotrow$
     * @version $FullVersion$
     * @mavengroupid $GroupId$
     * @mavenartifactid $ArtifactId$
     * @since 13.0
     */
    static class ClosePathElement3i extends PathElement3i {

        private static final long serialVersionUID = -8709004906872207794L;

        private final int fromX;

        private final int fromY;

        private final int fromZ;

        /**
         * @param fromx x coordinate of the origin point.
         * @param fromy y coordinate of the origin point.
         * @param fromz z coordinate of the origin point.
         * @param tox x coordinate of the target point.
         * @param toy y coordinate of the target point.
         * @param toz z coordinate of the target point.
         */
        ClosePathElement3i(int fromx, int fromy, int fromz, int tox, int toy, int toz) {
            super(PathElementType.CLOSE, tox, toy, toz);
            this.fromX = fromx;
            this.fromY = fromy;
            this.fromZ = fromz;
        }

        @Pure
        @Override
        public boolean equals(Object obj) {
            try {
                final PathElement3ai elt = (PathElement3ai) obj;
                return getType() == elt.getType() && getToX() == elt.getToX() && getToY() == elt.getToY()
                        && getToZ() == elt.getToZ() && getFromX() == elt.getFromX() && getFromY() == elt.getFromY()
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
            return bits ^ (bits >> 32);
        }

        @Pure
        @Override
        public boolean isEmpty() {
            return (this.fromX == this.toX) && (this.fromY == this.toY) && (this.fromZ == this.toZ);
        }

        @Pure
        @Override
        public boolean isDrawable() {
            return !isEmpty();
        }

        @Override
        public void toArray(int[] array) {
            assert array != null : "Array must be not null";
            assert array.length >= 3 : "Array size is too small";
            array[0] = this.toX;
            array[1] = this.toY;
            array[2] = this.toZ;
        }

        @Override
        public void toArray(double[] array) {
            assert array != null : "Array must be not null";
            assert array.length >= 3 : "Array size is too small";
            array[0] = this.toX;
            array[1] = this.toY;
            array[2] = this.toZ;
        }

        @Pure
        @Override
        public int[] toArray() {
            return new int[] {this.toX, this.toY, this.toZ};
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
        public int getFromZ() {
            return this.fromZ;
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
        public int getCtrlZ1() {
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
        public int getCtrlZ2() {
            return 0;
        }

    }

}
