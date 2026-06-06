/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2026 The original authors and other contributors.
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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import org.arakhne.afc.math.Unefficient;
import org.arakhne.afc.math.geometry.base.d3.MultiShape3D;
import org.arakhne.afc.math.geometry.base.d3.Point3D;
import org.arakhne.afc.math.geometry.base.d3.Quaternion;
import org.arakhne.afc.math.geometry.base.d3.Shape3D;
import org.arakhne.afc.math.geometry.base.d3.Vector3D;
import org.arakhne.afc.math.geometry.d3.a.Shape3DType;
import org.arakhne.afc.vmutil.asserts.AssertMessages;
import org.eclipse.xtext.xbase.lib.Pure;

/** Container for grouping of shapes.
 *
 * <p>The coordinates of the shapes inside the multishape are global. They are not relative to the multishape.
 *
 * @param <IT> is the type of the implementation of this multishape.
 * @param <CT> is the type of the shapes that are inside this multishape.
 * @param <IE> is the type of the path elements.
 * @param <P> is the type of the points.
 * @param <V> is the type of the vectors.
 * @param <Q> is the type of the quaternions.
 * @param <B> is the type of the bounding boxes.
 * @author $Author: tpiotrow$
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public interface MultiShape3ai<
		IT extends MultiShape3ai<?, CT, IE, P, V, Q, B>,
		CT extends Shape3ai<?, IE, P, V, Q, B>,
		IE extends PathElement3ai,
		P extends Point3D<? super P, ? super V, ? super Q>,
		V extends Vector3D<? super V, ? super P, ? super Q>,
		Q extends Quaternion<? super P, ? super V, ? super Q>,
		B extends AlignedBox3ai<?, IE, P, V, Q, B>>
		extends Shape3ai<IT, IE, P, V, Q, B>,
		MultiShape3D<IT, CT, PathIterator3ai<IE>, P, V, Q, B> {

	@Override
	default Shape3DType getType() {
		return Shape3DType.MULTISHAPE;
	}

	@Pure
	@Override
	default boolean intersects(Sphere3ai<?, ?, ?, ?, ?, ?> sphere) {
		assert sphere != null : AssertMessages.notNullParameter();
		if (sphere.intersects(toBoundingBox())) {
			for (final var shape : getBackendDataList()) {
				if (shape.intersects(sphere)) {
					return true;
				}
			}
		}
		return false;
	}

	@Pure
	@Override
	default boolean intersects(AlignedBox3ai<?, ?, ?, ?, ?, ?> AlignedBox) {
		assert AlignedBox != null : AssertMessages.notNullParameter();
		if (AlignedBox.intersects(toBoundingBox())) {
			for (final var shape : getBackendDataList()) {
				if (shape.intersects(AlignedBox)) {
					return true;
				}
			}
		}
		return false;
	}

	@Pure
	@Override
	default boolean intersects(Segment3ai<?, ?, ?, ?, ?, ?> segment) {
		assert segment != null : AssertMessages.notNullParameter();
		if (segment.intersects(toBoundingBox())) {
			for (final var shape : getBackendDataList()) {
				if (shape.intersects(segment)) {
					return true;
				}
			}
		}
		return false;
	}

	@Pure
	@Override
	default boolean intersects(PathIterator3ai<?> iterator) {
		if (toBoundingBox().intersects(iterator)) {
			for (final var shape : getBackendDataList()) {
				if (shape.intersects(iterator.restartIterations())) {
					return true;
				}
			}
		}
		return false;
	}

	@Pure
	@Override
	@Unefficient
	default boolean intersects(MultiShape3ai<?, ?, ?, ?, ?, ?, ?> multishape) {
		assert multishape != null : AssertMessages.notNullParameter();
		if (multishape.toBoundingBox().intersects(toBoundingBox())) {
			for (final var shape1 : getBackendDataList()) {
				for (final var shape2 : multishape.getBackendDataList()) {
					if (shape1.intersects(shape2)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	@Pure
	@Override
	default boolean contains(int x, int y, int z) {
		if (toBoundingBox().contains(x, y, z)) {
			for (final var shape : getBackendDataList()) {
				if (shape.contains(x, y, z)) {
					return true;
				}
			}
		}
		return false;
	}

	@Pure
	@Override
	default boolean contains(AlignedBox3ai<?, ?, ?, ?, ?, ?> AlignedBox) {
		assert AlignedBox != null : AssertMessages.notNullParameter();
		if (AlignedBox.intersects(toBoundingBox())) {
			for (final var shape : getBackendDataList()) {
				if (shape.contains(AlignedBox)) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	default void translate(int dx, int dy, int dz) {
		for (final var shape : getBackendDataList()) {
			shape.translate(dx, dy, dz);
		}
		onBackendDataChange();
	}

	@Unefficient
	@Pure
	@Override
	default CT getFirstShapeIntersecting(Shape3D<?, ?, ?, ?, ?, ?> shape) {
		assert shape != null : AssertMessages.notNullParameter();
		if (shape.intersects(toBoundingBox())) {
			for (final var innerShape : getBackendDataList()) {
				if (innerShape.intersects(shape)) {
					return innerShape;
				}
			}
		}
		return null;
	}

	@Unefficient
	@Override
	@Pure
	default List<CT> getShapesIntersecting(Shape3D<?, ?, ?, ?, ?, ?> shape) {
		assert shape != null : AssertMessages.notNullParameter();
		final var list = new ArrayList<CT>();
		if (shape.intersects(toBoundingBox())) {
			for (final var subshape : getBackendDataList()) {
				if (subshape.intersects(shape)) {
					list.add(subshape);
				}
			}
		}
		return list;
	}

	@Pure
	@Override
	default void toBoundingBox(B box) {
		assert box != null : AssertMessages.notNullParameter();
		final var iterator = getBackendDataList().iterator();
		if (iterator.hasNext()) {
			iterator.next().toBoundingBox(box);
			final B subbounds = getGeomFactory().newBox();
			while (iterator.hasNext()) {
				final var element = iterator.next();
				element.toBoundingBox(subbounds);
				box.setUnion(subbounds);
			}
		}
	}

	@Override
	default Iterator<P> getPointIterator() {
		return new MultiShapePointIterator<>(getBackendDataList());
	}

    @Override
    default P getClosestPointTo(AlignedBox3ai<?, ?, ?, ?, ?, ?> rectangle) {
        throw new UnsupportedOperationException();
    }

    @Override
    default P getClosestPointTo(Sphere3ai<?, ?, ?, ?, ?, ?> circle) {
        throw new UnsupportedOperationException();
    }

    @Override
    default P getClosestPointTo(Segment3ai<?, ?, ?, ?, ?, ?> segment) {
        throw new UnsupportedOperationException();
    }

    @Override
    default P getClosestPointTo(MultiShape3ai<?, ?, ?, ?, ?, ?, ?> multishape) {
        throw new UnsupportedOperationException();
    }

    @Override
    default P getClosestPointTo(Path3ai<?, ?, ?, ?, ?, ?> path) {
        throw new UnsupportedOperationException();
    }

	/** Iterator on the points of the multishape.
	 *
	 * @param <P> the type of the points.
	 * @param <V> the type of the vectors.
	 * @param <Q> the type of the quaternions.
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	class MultiShapePointIterator<P extends Point3D<? super P, ? super V, ? super Q>,
			V extends Vector3D<? super V, ? super P, ? super Q>,
			Q extends Quaternion<? super P, ? super V, ? super Q>> implements Iterator<P> {

		private final Iterator<? extends Shape3ai<?, ?, P, V, Q, ?>> elements;

		private Iterator<P> currentIterator;

		private P next;

		/** Constructor.
		 * @param list the list of the shapes to iterate on.
		 *
		 */
		public MultiShapePointIterator(List<? extends Shape3ai<?, ?, P, V, Q, ?>> list) {
			assert list != null : AssertMessages.notNullParameter();
			this.elements = list.iterator();
			if (this.elements.hasNext()) {
				this.currentIterator = this.elements.next().getPointIterator();
				searchNext();
			}
		}

		private void searchNext() {
			this.next = null;
			while (true) {
				if (this.currentIterator.hasNext()) {
					this.next = this.currentIterator.next();
					return;
				} else if (this.elements.hasNext()) {
					this.currentIterator = this.elements.next().getPointIterator();
				} else {
					return;
				}
			}
		}

		@Pure
		@Override
		public boolean hasNext() {
			return this.next != null;
		}

		@Override
		public P next() {
			assert this.next != null : new NoSuchElementException();
			final var point = this.next;
			searchNext();
			return point;
		}

	}

}
