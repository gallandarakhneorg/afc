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

package org.arakhne.afc.math.geometry.d3.afp;

import java.util.ArrayList;
import java.util.List;

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
public interface MultiShape3afp<
		IT extends MultiShape3afp<?, CT, IE, P, V, Q, B>,
		CT extends Shape3afp<?, IE, P, V, Q, B>,
		IE extends PathElement3afp,
		P extends Point3D<? super P, ? super V, ? super Q>,
		V extends Vector3D<? super V, ? super P, ? super Q>,
		Q extends Quaternion<? super P, ? super V, ? super Q>,
		B extends AlignedBox3afp<?, IE, P, V, Q, B>>
		extends Shape3afp<IT, IE, P, V, Q, B>,
		MultiShape3D<IT, CT, PathIterator3afp<IE>, P, V, Q, B> {

	@Override
	default Shape3DType getType() {
		return Shape3DType.MULTISHAPE;
	}

	@Pure
	@Override
	default boolean intersects(Sphere3afp<?, ?, ?, ?, ?, ?> sphere) {
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
	default boolean intersects(AlignedBox3afp<?, ?, ?, ?, ?, ?> prism) {
		assert prism != null : AssertMessages.notNullParameter();
		if (prism.intersects(toBoundingBox())) {
			for (final var shape : getBackendDataList()) {
				if (shape.intersects(prism)) {
					return true;
				}
			}
		}
		return false;
	}

	@Pure
	@Override
	default boolean intersects(Segment3afp<?, ?, ?, ?, ?, ?, ?> segment) {
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
	default boolean intersects(PathIterator3afp<?> iterator) {
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
	default boolean intersects(MultiShape3afp<?, ?, ?, ?, ?, ?, ?> multishape) {
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
	default boolean contains(double x, double y, double z) {
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
	default boolean contains(AlignedBox3afp<?, ?, ?, ?, ?, ?> AlignedBox) {
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
	default void translate(double dx, double dy, double dz) {
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
			final var subbounds = getGeomFactory().newBox();
			while (iterator.hasNext()) {
				final var element = iterator.next();
				element.toBoundingBox(subbounds);
				box.setUnion(subbounds);
			}
		}
	}

    @Override
    default P getClosestPointTo(Sphere3afp<?, ?, ?, ?, ?, ?> circle) {
        assert circle != null : AssertMessages.notNullParameter();
        var min = Double.POSITIVE_INFINITY;
        final var closest = getGeomFactory().newPoint();
        for (final var innerShape : getBackendDataList()) {
            final var point = innerShape.getClosestPointTo(circle);
            final var dist = circle.getDistanceSquared(point);
            if (dist < min) {
                min = dist;
                closest.set(point);
            }
        }
        return closest;
    }

    @Override
    default P getClosestPointTo(Segment3afp<?, ?, ?, ?, ?, ?, ?> segment) {
        assert segment != null : AssertMessages.notNullParameter();
        var min = Double.POSITIVE_INFINITY;
        final var closest = getGeomFactory().newPoint();
        for (final var innerShape : getBackendDataList()) {
            final var point = innerShape.getClosestPointTo(segment);
            final var dist = segment.getDistanceSquared(point);
            if (dist < min) {
                min = dist;
                closest.set(point);
            }
        }
        return closest;
    }

    @Override
    default P getClosestPointTo(AlignedBox3afp<?, ?, ?, ?, ?, ?> rectangle) {
        assert rectangle != null : AssertMessages.notNullParameter();
        var min = Double.POSITIVE_INFINITY;
        final var closest = getGeomFactory().newPoint();
        for (final var innerShape : getBackendDataList()) {
            final var point = innerShape.getClosestPointTo(rectangle);
            final var dist = rectangle.getDistanceSquared(point);
            if (dist < min) {
                min = dist;
                closest.set(point);
            }
        }
        return closest;
    }

    @Override
    default P getClosestPointTo(Path3afp<?, ?, ?, ?, ?, ?> path) {
        assert path != null : AssertMessages.notNullParameter();
        var min = Double.POSITIVE_INFINITY;
        final var closest = getGeomFactory().newPoint();
        for (final var innerShape : getBackendDataList()) {
            final var point = innerShape.getClosestPointTo(path);
            final var dist = path.getDistanceSquared(point);
            if (dist < min) {
                min = dist;
                closest.set(point);
            }
        }
        return closest;
    }

    @Override
    default P getClosestPointTo(MultiShape3afp<?, ?, ?, ?, ?, ?, ?> multishape) {
        assert multishape != null : AssertMessages.notNullParameter();
        var min = Double.POSITIVE_INFINITY;
        final var closest = getGeomFactory().newPoint();
        for (final var innerShape : getBackendDataList()) {
            final var point = innerShape.getClosestPointTo(multishape);
            final var dist = multishape.getDistanceSquared(point);
            if (dist < min) {
                min = dist;
                closest.set(point);
            }
        }
        return closest;
    }

}
