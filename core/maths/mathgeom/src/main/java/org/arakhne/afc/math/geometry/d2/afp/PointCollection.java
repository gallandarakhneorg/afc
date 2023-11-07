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

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.Vector2D;
import org.arakhne.afc.vmutil.asserts.AssertMessages;

/** An collection of the points of the path.
 *
 * @param <P> the type of the points.
 * @param <V> the type of the vectors.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
class PointCollection<P extends Point2D<? super P, ? super V>, V extends Vector2D<? super V, ? super P>>
        implements Collection<P> {

    private final Path2afp<?, ?, ?, P, V, ?> path;

    /** Constructor.
     * @param path the path to iterate on.
     */
    PointCollection(Path2afp<?, ?, ?, P, V, ?> path) {
        assert path != null : AssertMessages.notNullParameter();
        this.path = path;
    }

    /** Replies the original path.
     *
     * @return the original path.
     */
    protected Path2afp<?, ?, ?, P, V, ?> getWrappedPath() {
        return this.path;
    }

    @Pure
    @Override
    public int size() {
        return this.path.size();
    }

    @Pure
    @Override
    public boolean isEmpty() {
        return this.path.size() <= 0;
    }

    @Pure
    @Override
    public boolean contains(Object obj) {
        if (obj instanceof Point2D) {
            return this.path.containsControlPoint((Point2D<?, ?>) obj);
        }
        return false;
    }

    @Pure
    @Override
    public Iterator<P> iterator() {
        return new PointIterator();
    }

    @Pure
    @Override
    public Object[] toArray() {
        return this.path.toPointArray();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T[] toArray(T[] array) {
        assert array != null : AssertMessages.notNullParameter();
        final Iterator<P> iterator = new PointIterator();
        for (int i = 0; i < array.length && iterator.hasNext(); ++i) {
            array[i] = (T) iterator.next();
        }
        return array;
    }

    @Override
    public boolean add(P element) {
        if (element != null) {
            if (this.path.size() == 0) {
                this.path.moveTo(element.getX(), element.getY());
            } else {
                this.path.lineTo(element.getX(), element.getY());
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean remove(Object obj) {
        if (obj instanceof Point2D) {
            final Point2D<?, ?> p = (Point2D<?, ?>) obj;
            return this.path.remove(p.getX(), p.getY());
        }
        return false;
    }

    @Pure
    @Override
    public boolean containsAll(Collection<?> collection) {
        assert collection != null : AssertMessages.notNullParameter();
        for (final Object obj : collection) {
            if ((!(obj instanceof Point2D))
                    || (!this.path.containsControlPoint((Point2D<?, ?>) obj))) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends P> collection) {
        assert collection != null : AssertMessages.notNullParameter();
        boolean changed = false;
        for (final P pts : collection) {
            if (add(pts)) {
                changed = true;
            }
        }
        return changed;
    }

    @Override
    public boolean removeAll(Collection<?> collection) {
        assert collection != null : AssertMessages.notNullParameter();
        boolean changed = false;
        for (final Object obj : collection) {
            if (obj instanceof Point2D) {
                final Point2D<?, ?> pts = (Point2D<?, ?>) obj;
                if (this.path.remove(pts.getX(), pts.getY())) {
                    changed = true;
                }
            }
        }
        return changed;
    }

    @Override
    public boolean retainAll(Collection<?> collection) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
        this.path.clear();
    }

    /** Iterator on the points of the path.
     *
     * @author $Author: sgalland$
     * @version $FullVersion$
     * @mavengroupid $GroupId$
     * @mavenartifactid $ArtifactId$
     * @since 13.0
     */
    private class PointIterator implements Iterator<P> {

        private int index;

        private P lastReplied;

        /** Construct the iterator.
         */
        PointIterator() {
            //
        }

        @Pure
        @Override
        public boolean hasNext() {
            return this.index < getWrappedPath().size();
        }

        @Override
        public P next() {
            try {
                this.lastReplied = getWrappedPath().getPointAt(this.index++);
                return this.lastReplied;
            } catch (Throwable e) {
                throw new NoSuchElementException();
            }
        }

        @Override
        public void remove() {
            final Point2D<?, ?> p = this.lastReplied;
            this.lastReplied = null;
            if (p == null) {
                throw new NoSuchElementException();
            }
            getWrappedPath().remove(p.getX(), p.getY());
        }

    }

}
