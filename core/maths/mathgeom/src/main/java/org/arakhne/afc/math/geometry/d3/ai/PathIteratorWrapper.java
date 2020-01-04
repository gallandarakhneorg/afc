/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2020 The original authors, and other authors.
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

package org.arakhne.afc.math.geometry.d3.ai;

import org.arakhne.afc.math.geometry.PathElementType;
import org.arakhne.afc.math.geometry.PathWindingRule;
import org.arakhne.afc.math.geometry.d3.afp.PathElement3afp;
import org.arakhne.afc.math.geometry.d3.afp.PathIterator3afp;


/** Wrapper of a path iterator.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
@SuppressWarnings("rawtypes")
class PathIteratorWrapper implements PathIterator3ai {

    private final GeomFactory3ai<?, ?, ?, ?> factory;

    private final PathIterator3afp<?> iterator;

    /** Create the wrapper.
     *
     * @param factory the geometry factory.
     * @param iterator the original iterator.
     */
    PathIteratorWrapper(GeomFactory3ai<?, ?, ?, ?> factory, PathIterator3afp<?> iterator) {
        this.factory = factory;
        this.iterator = iterator;
    }

    @Override
    public GeomFactory3ai<?, ?, ?, ?> getGeomFactory() {
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
    public PathElement3ai next() {
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
    private static class PathElementWrapper implements PathElement3ai {

        private static final long serialVersionUID = 4217344757665018418L;

        private final PathElement3afp element;

        /** Constructor.
         * @param element the wrapped element.
         */
        PathElementWrapper(PathElement3afp element) {
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
        public int getFromX() {
            return (int) Math.round(this.element.getFromX());
        }

        @Override
        public int getFromY() {
            return (int) Math.round(this.element.getFromY());
        }

        @Override
        public int getFromZ() {
            return (int) Math.round(this.element.getFromZ());
        }

        @Override
        public int getCtrlX1() {
            return (int) Math.round(this.element.getCtrlX1());
        }

        @Override
        public int getCtrlY1() {
            return (int) Math.round(this.element.getCtrlY1());
        }

        @Override
        public int getCtrlZ1() {
            return (int) Math.round(this.element.getCtrlZ1());
        }

        @Override
        public int getCtrlX2() {
            return (int) Math.round(this.element.getCtrlX2());
        }

        @Override
        public int getCtrlY2() {
            return (int) Math.round(this.element.getCtrlY2());
        }

        @Override
        public int getCtrlZ2() {
            return (int) Math.round(this.element.getCtrlZ2());
        }

        @Override
        public int getToX() {
            return (int) Math.round(this.element.getToX());
        }

        @Override
        public int getToY() {
            return (int) Math.round(this.element.getToY());
        }

        @Override
        public int getToZ() {
            return (int) Math.round(this.element.getToZ());
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

        //@Override
        //public int getRadiusX() {
        //    return (int) Math.round(this.element.getRadiusX());
        //}

        //@Override
        //public int getRadiusY() {
        //    return (int) Math.round(this.element.getRadiusY());
        //}

        //@Override
        //public double getRotationX() {
        //    return this.element.getRotationX();
        //}

        //@Override
        //public boolean getSweepFlag() {
        //    return this.element.getSweepFlag();
        //}

        //@Override
        //public boolean getLargeArcFlag() {
        //    return this.element.getLargeArcFlag();
        //}

    }

}
