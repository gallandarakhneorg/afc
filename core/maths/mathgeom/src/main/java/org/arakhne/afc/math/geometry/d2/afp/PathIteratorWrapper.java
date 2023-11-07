/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2023 The original authors and other contributors.
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

package org.arakhne.afc.math.geometry.d2.afp;

import org.arakhne.afc.math.geometry.PathElementType;
import org.arakhne.afc.math.geometry.PathWindingRule;
import org.arakhne.afc.math.geometry.d2.ai.PathElement2ai;
import org.arakhne.afc.math.geometry.d2.ai.PathIterator2ai;


/** Wrapper of a path iterator.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
@SuppressWarnings("rawtypes")
class PathIteratorWrapper implements PathIterator2afp {

    private final GeomFactory2afp<?, ?, ?, ?> factory;

    private final PathIterator2ai<?> iterator;

    /** Create the wrapper.
     *
     * @param factory the geometry factory.
     * @param iterator the original iterator.
     */
    PathIteratorWrapper(GeomFactory2afp<?, ?, ?, ?> factory, PathIterator2ai<?> iterator) {
        this.factory = factory;
        this.iterator = iterator;
    }

    @Override
    public GeomFactory2afp<?, ?, ?, ?> getGeomFactory() {
        return this.factory;
    }

    @Override
    public PathIteratorWrapper restartIterations() {
        return new PathIteratorWrapper(this.factory, this.iterator.restartIterations());
    }

    @Override
    public PathWindingRule getWindingRule() {
        return this.iterator.getWindingRule();
    }

    @Override
    public boolean isPolyline() {
        return this.iterator.isPolyline();
    }

    @Override
    public boolean isCurved() {
        return this.iterator.isCurved();
    }

    @Override
    public boolean isMultiParts() {
        return this.iterator.isMultiParts();
    }

    @Override
    public boolean isPolygon() {
        return this.iterator.isPolygon();
    }

    @Override
    public boolean hasNext() {
        return this.iterator.hasNext();
    }

    @Override
    public PathElement2afp next() {
        return new PathElementWrapper(this.iterator.next());
    }

    /** Wrapper of a path iterator.
     *
     * @author $Author: sgalland$
     * @version $FullVersion$
     * @mavengroupid $GroupId$
     * @mavenartifactid $ArtifactId$
     * @since 13.0
     */
    private static class PathElementWrapper implements PathElement2afp {

        private static final long serialVersionUID = -3937298048366892845L;

        private final PathElement2ai element;

        /** Constructor.
         *
         * @param element the element to wrap.
         */
        PathElementWrapper(PathElement2ai element) {
            this.element = element;
        }

        @Override
        public boolean isEmpty() {
            return this.element.isEmpty();
        }

        @Override
        public boolean isDrawable() {
            return this.element.isDrawable();
        }

        @Override
        public double getFromX() {
            return this.element.getFromX();
        }

        @Override
        public double getFromY() {
            return this.element.getFromY();
        }

        @Override
        public double getCtrlX1() {
            return this.element.getCtrlX1();
        }

        @Override
        public double getCtrlY1() {
            return this.element.getCtrlY1();
        }

        @Override
        public double getCtrlX2() {
            return this.element.getCtrlX2();
        }

        @Override
        public double getCtrlY2() {
            return this.element.getCtrlY2();
        }

        @Override
        public double getToX() {
            return this.element.getToX();
        }

        @Override
        public double getToY() {
            return this.element.getToY();
        }

        @Override
        public PathElementType getType() {
            return this.element.getType();
        }

        @Override
        public void toArray(int[] array) {
            this.element.toArray(array);
        }

        @Override
        public void toArray(double[] array) {
            this.element.toArray(array);
        }

        @Override
        public double getRadiusX() {
            return this.element.getRadiusX();
        }

        @Override
        public double getRadiusY() {
            return this.element.getRadiusY();
        }

        @Override
        public double getRotationX() {
            return this.element.getRotationX();
        }

        @Override
        public boolean getSweepFlag() {
            return this.element.getSweepFlag();
        }

        @Override
        public boolean getLargeArcFlag() {
            return this.element.getLargeArcFlag();
        }

    }

}
