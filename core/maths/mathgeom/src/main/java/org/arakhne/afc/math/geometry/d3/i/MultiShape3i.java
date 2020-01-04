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

package org.arakhne.afc.math.geometry.d3.i;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.geometry.d3.ai.MultiShape3ai;
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
public class MultiShape3i<T extends Shape3i<?>> extends AbstractShape3i<MultiShape3i<T>>
        implements MultiShape3ai<Shape3i<?>, MultiShape3i<T>, T, PathElement3i, Point3i, Vector3i, RectangularPrism3i> {

	private static final long serialVersionUID = -4727279807601027239L;

	private List<T> elements = new ListResponseModel();

	private RectangularPrism3i bounds;

	/**
	 * Construct an empty multishape.
	 */
	public MultiShape3i() {
		//
	}

	/** Construct a multishape with shapes inside.
	 *
	 * @param shapes the shapes to add into the multishape.
	 */
	public MultiShape3i(@SuppressWarnings("unchecked") T... shapes) {
		assert shapes != null : AssertMessages.notNullParameter();
		addAll(Arrays.asList(shapes));
	}

	/** Construct a multishape with shapes inside.
	 *
	 * @param shapes the shapes to add into the multishape.
	 */
	public MultiShape3i(Iterable<? extends T> shapes) {
		assert shapes != null : AssertMessages.notNullParameter();
		for (final T element : shapes) {
			add(element);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public MultiShape3i<T> clone() {
		final MultiShape3i<T> clone = super.clone();
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
	@SuppressWarnings("checkstyle:equalshashcode")
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
	public RectangularPrism3i toBoundingBox() {
		if (this.bounds == null) {
			this.bounds = getGeomFactory().newBox();
			MultiShape3ai.super.toBoundingBox(this.bounds);
		}
		return this.bounds;
	}

	@Pure
	@Override
	public void toBoundingBox(RectangularPrism3i box) {
		assert box != null : AssertMessages.notNullParameter();
		if (this.bounds == null) {
			this.bounds = getGeomFactory().newBox();
			MultiShape3ai.super.toBoundingBox(this.bounds);
		}
		box.set(this.bounds);
	}

	@Override
	public void translate(int dx, int dy, int dz) {
		if (dx != 0 || dy != 0 || dz != 0) {
			final RectangularPrism3i box = this.bounds;
			MultiShape3ai.super.translate(dx, dy, dz);
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
			if (element instanceof AbstractShape3i<?>) {
				((AbstractShape3i<?>) element).addShapeGeometryChangeListener(this);
			}
		}

		@Override
		public T remove(int index) {
			final T element = this.delegate.remove(index);
			if (element instanceof AbstractShape3i<?>) {
				((AbstractShape3i<?>) element).removeShapeGeometryChangeListener(this);
			}
			return element;
		}

		@Override
		public T set(int index, T element) {
			assert element != null;
			final T oldElement = this.delegate.set(index, element);
			if (oldElement instanceof AbstractShape3i<?>) {
				((AbstractShape3i<?>) oldElement).removeShapeGeometryChangeListener(this);
			}
			if (element instanceof AbstractShape3i<?>) {
				((AbstractShape3i<?>) element).addShapeGeometryChangeListener(this);
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
		public void shapeGeometryChange(Shape3i<?> shape) {
			onContentGeometryChange();
		}

	}

}
