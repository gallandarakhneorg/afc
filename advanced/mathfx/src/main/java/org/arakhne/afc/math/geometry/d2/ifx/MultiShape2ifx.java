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

package org.arakhne.afc.math.geometry.d2.ifx;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import com.sun.javafx.collections.NonIterableChange.SimpleUpdateChange;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ModifiableObservableListBase;
import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.geometry.d2.MathFXAttributeNames;
import org.arakhne.afc.math.geometry.d2.ai.MultiShape2ai;
import org.arakhne.afc.vmutil.asserts.AssertMessages;

/** Container for grouping of shapes.
 *
 * <p>The coordinates of the shapes inside the multishape are global. They are not relative to the multishape.
 *
 * @param <T> the type of the shapes inside the multishape.
 * @author $Author: tpiotrow$
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public class MultiShape2ifx<T extends Shape2ifx<?>> extends AbstractShape2ifx<MultiShape2ifx<T>>
		implements MultiShape2ai<Shape2ifx<?>, MultiShape2ifx<T>, T, PathElement2ifx, Point2ifx, Vector2ifx, Rectangle2ifx> {

	private static final long serialVersionUID = -4727279807601027239L;

	private ListProperty<T> elements;

	/**
	 * Construct an empty multishape.
	 */
	public MultiShape2ifx() {
		//
	}

	/** Construct a multishape with shapes inside.
	 *
	 * @param shapes the shapes to add into the multishape.
	 */
	public MultiShape2ifx(@SuppressWarnings("unchecked") T... shapes) {
		assert shapes != null : AssertMessages.notNullParameter();
		addAll(Arrays.asList(shapes));
	}

	/** Construct a multishape with shapes inside.
	 *
	 * @param shapes the shapes to add into the multishape.
	 */
	public MultiShape2ifx(Iterable<? extends T> shapes) {
		assert shapes != null : AssertMessages.notNullParameter();
		for (final T element : shapes) {
			add(element);
		}
	}

	@Pure
	@Override
	public List<T> getBackendDataList() {
		return elementsProperty().get();
	}

	/** Replies the property that contains all the shapes in this multishape.
	 *
	 * @return the elements property.
	 */
	public ListProperty<T> elementsProperty() {
		if (this.elements == null) {
			this.elements = new SimpleListProperty<>(this, MathFXAttributeNames.ELEMENTS, new InternalObservableList<>());
		}
		return this.elements;
	}

	@Override
	public ObjectProperty<Rectangle2ifx> boundingBoxProperty() {
		if (this.boundingBox == null) {
			this.boundingBox = new SimpleObjectProperty<>(this, MathFXAttributeNames.BOUNDING_BOX);
			this.boundingBox.bind(Bindings.createObjectBinding(() -> {
				final Rectangle2ifx box = getGeomFactory().newBox();
				final Rectangle2ifx shapeBox = getGeomFactory().newBox();
				final Iterator<T> iterator = elementsProperty().iterator();
				if (iterator.hasNext()) {
					iterator.next().toBoundingBox(shapeBox);
					box.set(shapeBox);
					while (iterator.hasNext()) {
						iterator.next().toBoundingBox(shapeBox);
						box.setUnion(shapeBox);
					}
				}
				return box;
			}, elementsProperty()));
		}
		return this.boundingBox;
	}

	@SuppressWarnings("unchecked")
	@Override
	public MultiShape2ifx<T> clone() {
		final MultiShape2ifx<T> clone = super.clone();
		clone.elements = null;
		if (this.elements != null) {
			for (final T shape : this.elements) {
				clone.elementsProperty().add((T) shape.clone());
			}
		}
		clone.boundingBox = null;
		return clone;
	}

	@Override
	public int hashCode() {
		long bits = 1;
		bits = 31 * bits + this.elements.hashCode();
		final int b = (int) bits;
		return b ^ (b >> 31);
	}

	@Pure
	@Override
	public Rectangle2ifx toBoundingBox() {
		return boundingBoxProperty().get().clone();
	}

	@Pure
	@Override
	public void toBoundingBox(Rectangle2ifx box) {
		assert box != null : AssertMessages.notNullParameter();
		box.set(boundingBoxProperty().get());
	}

	/** Internal list.
	 *
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 13.0
	 */
	private static class InternalObservableList<T extends Shape2ifx<?>> extends ModifiableObservableListBase<T>
			implements InvalidationListener {

		private final List<T> internalList = new ArrayList<>();

		/** Construct the list.
		 */
		InternalObservableList() {
			//
		}

		private void bind(Shape2ifx<?> shape) {
			assert shape != null;
			final ObjectProperty<Rectangle2ifx> property = shape.boundingBoxProperty();
			property.addListener(this);
		}

		private void unbind(Shape2ifx<?> shape) {
			assert shape != null;
			final ObjectProperty<Rectangle2ifx> property = shape.boundingBoxProperty();
			property.removeListener(this);
		}

		@Override
		public T get(int index) {
			return this.internalList.get(index);
		}

		@Override
		public int size() {
			return this.internalList.size();
		}

		@Override
		protected void doAdd(int index, T element) {
			assert element != null : AssertMessages.notNullParameter(1);
			this.internalList.add(index, element);
			bind(element);
		}

		@Override
		protected T doSet(int index, T element) {
			assert element != null : AssertMessages.notNullParameter(1);
			final T old = this.internalList.set(index, element);
			unbind(old);
			bind(element);
			return old;
		}

		@Override
		protected T doRemove(int index) {
			final T old = this.internalList.remove(index);
			unbind(old);
			return old;
		}

		@Override
		public void invalidated(Observable observable) {
			final int position = indexOf(((Property<?>) observable).getBean());
			if (position >= 0) {
				fireChange(new SimpleUpdateChange<>(position, this));
			}
		}

		@Override
		public boolean equals(Object obj) {
			if (!super.equals(obj)) {
				return false;
			}
			final InternalObservableList<?> lObj = (InternalObservableList<?>) obj;
			if (this.internalList.equals(lObj.internalList)) {
				return this.internalList.equals(lObj.internalList);
			}
			return false;
		}

		@Override
		public int hashCode() {
			int hash = 1;
			final int prime = 31;
			hash = hash * prime + this.internalList.hashCode();
			return hash;
		}

	}

}
