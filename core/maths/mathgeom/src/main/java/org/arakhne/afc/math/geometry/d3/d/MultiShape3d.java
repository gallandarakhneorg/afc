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

package org.arakhne.afc.math.geometry.d3.d;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.geometry.d3.afp.MultiShape3afp;
import org.arakhne.afc.vmutil.asserts.AssertMessages;

/** Container for grouping of shapes.
 *
 * <p>The coordinates of the shapes inside the multishape are global. They are not relative to the multishape.
 *
 * <p>Caution: The multishape does not detect the bound change of the stored shapes.
 *
 * @param <T> the type of the shapes inside the multishape.
 * @author $Author: tpiotrow$
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public class MultiShape3d<T extends Shape3d<?>> extends AbstractShape3d<MultiShape3d<T>>
        implements MultiShape3afp<Shape3d<?>, MultiShape3d<T>, T, PathElement3d, Point3d, Vector3d, Quaternion4d, AlignedBox3d> {

	private static final long serialVersionUID = -4727279807601027239L;

	private List<T> elements = new ListResponseModel();

	private AlignedBox3d bounds;

	/**
	 * Construct an empty multishape.
	 */
	public MultiShape3d() {
		//
	}

	/** Construct a multishape with shapes inside.
	 *
	 * @param shapes the shapes to add into the multishape.
	 */
	public MultiShape3d(@SuppressWarnings("unchecked") T... shapes) {
		assert shapes != null : AssertMessages.notNullParameter();
		addAll(Arrays.asList(shapes));
	}

	/** Construct a multishape with shapes inside.
	 *
	 * @param shapes the shapes to add into the multishape.
	 */
	public MultiShape3d(Iterable<? extends T> shapes) {
		assert shapes != null : AssertMessages.notNullParameter();
		for (final T element : shapes) {
			add(element);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	@Pure
	public MultiShape3d<T> clone() {
		final MultiShape3d<T> clone = super.clone();
		final List<T> clonedList = new ArrayList<>();
		for (final T shape : this.elements) {
			clonedList.add((T) shape.clone());
		}
		clone.elements = clonedList;
		if (this.bounds != null) {
			clone.bounds = this.bounds.clone();
		}
		return clone;
	}

	@Override
	@Pure
	public int hashCode() {
		return this.elements.hashCode();
	}

	@Override
	public void onBackendDataChange() {
		this.bounds = null;
		fireGeometryChange();
	}

	/** Invoked when the geometry of the content has changed.
	 */
	protected void onContentGeometryChange() {
		this.bounds = null;
		fireGeometryChange();
	}

	@Pure
	@Override
	public List<T> getBackendDataList() {
		return this.elements;
	}

	@Pure
	@Override
	public AlignedBox3d toBoundingBox() {
		if (this.bounds == null) {
			this.bounds = getGeomFactory().newBox();
			MultiShape3afp.super.toBoundingBox(this.bounds);
		}
		return this.bounds;
	}

	@Pure
	@Override
	public void toBoundingBox(AlignedBox3d box) {
		assert box != null : AssertMessages.notNullParameter();
		if (this.bounds == null) {
			this.bounds = getGeomFactory().newBox();
			MultiShape3afp.super.toBoundingBox(this.bounds);
		}
		box.set(this.bounds);
	}

	@Override
	public void translate(double dx, double dy, double dz) {
		if (dx != 0 || dy != 0 || dz != 0) {
			final AlignedBox3d box = this.bounds;
			MultiShape3afp.super.translate(dx, dy, dz);
			if (box != null) {
				box.translate(dx, dy, dz);
				this.bounds = box;
			}
			fireGeometryChange();
		}
	}

	/** List responsive model.
	 *
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 13.0
	 */
	private class ListResponseModel extends AbstractList<T> implements ShapeGeometryChangeListener {

		private List<T> delegate = new ArrayList<>();

		/** Construct an empty model.
		 */
		ListResponseModel() {
			//
		}

		@Override
		public void add(int index, T element) {
			assert element != null;
			this.delegate.add(index, element);
			if (element instanceof AbstractShape3d<?>) {
				((AbstractShape3d<?>) element).addShapeGeometryChangeListener(this);
			}
		}

		@Override
		public T remove(int index) {
			final T element = this.delegate.remove(index);
			if (element instanceof AbstractShape3d<?>) {
				((AbstractShape3d<?>) element).removeShapeGeometryChangeListener(this);
			}
			return element;
		}

		@Override
		public T set(int index, T element) {
			assert element != null;
			final T oldElement = this.delegate.set(index, element);
			if (oldElement instanceof AbstractShape3d<?>) {
				((AbstractShape3d<?>) oldElement).removeShapeGeometryChangeListener(this);
			}
			if (element instanceof AbstractShape3d<?>) {
				((AbstractShape3d<?>) element).addShapeGeometryChangeListener(this);
			}
			return oldElement;
		}

		@Override
		public T get(int index) {
			return this.delegate.get(index);
		}

		@Override
		public int size() {
			return this.delegate.size();
		}

		@Override
		public void shapeGeometryChange(Shape3d<?> shape) {
			onContentGeometryChange();
		}

		@Override
		public boolean addAll(Collection<? extends T> c) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public void planeGeometryChange(AbstractPlane3d<?> shape) {
			// TODO Auto-generated method stub
			
		}

	}

}
