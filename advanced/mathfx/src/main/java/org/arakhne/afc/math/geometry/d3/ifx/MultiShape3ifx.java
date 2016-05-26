/* 
 * $Id$
 * 
 * Copyright (C) 2013 Christophe BOHRHAUER.
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 * This program is free software; you can redistribute it and/or modify
 */
package org.arakhne.afc.math.geometry.d3.ifx;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.arakhne.afc.math.geometry.d3.ai.MultiShape3ai;
import org.eclipse.xtext.xbase.lib.Pure;

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
public class MultiShape3ifx<T extends Shape3ifx<?>> extends AbstractShape3ifx<MultiShape3ifx<T>> implements 
	MultiShape3ai<Shape3ifx<?>, MultiShape3ifx<T>, T, PathElement3ifx, Point3ifx, Vector3ifx, RectangularPrism3ifx> {

	private static final long serialVersionUID = -4727279807601027239L;

	private ListProperty<T> elements;
	
	/**
	 * Construct an empty multishape.
	 */
	public MultiShape3ifx() {
		//
	}

	/** Construct a multishape with shapes inside.
	 *
	 * @param shapes the shapes to add into the multishape.
	 */
	public MultiShape3ifx(@SuppressWarnings("unchecked") T... shapes) {
		assert (shapes != null) : "Shape array must be not null"; //$NON-NLS-1$
		addAll(Arrays.asList(shapes));
	}

	/** Construct a multishape with shapes inside.
	 *
	 * @param shapes the shapes to add into the multishape.
	 */
	public MultiShape3ifx(Iterable<? extends T> shapes) {
		assert (shapes != null) : "Shape list must be not null"; //$NON-NLS-1$
		for (T element : shapes) {
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
			this.elements = new SimpleListProperty<>(this, "elements", new InternalObservableList<>()); //$NON-NLS-1$
		}
		return this.elements;
	}
	
	@Override
	public ObjectProperty<RectangularPrism3ifx> boundingBoxProperty() {
		if (this.boundingBox == null) {
			this.boundingBox = new SimpleObjectProperty<>(this, "boundingBox"); //$NON-NLS-1$
			this.boundingBox.bind(Bindings.createObjectBinding(
					() -> {
						RectangularPrism3ifx box = getGeomFactory().newBox();
						RectangularPrism3ifx shapeBox = getGeomFactory().newBox();
						Iterator<T> iterator = elementsProperty().iterator();
						if (iterator.hasNext()) {
							iterator.next().toBoundingBox(shapeBox);
							box.set(shapeBox);
							while (iterator.hasNext()) {
								iterator.next().toBoundingBox(shapeBox);
								box.setUnion(shapeBox);
							}
						}
						return box;
					},
					elementsProperty()));
		}
		return this.boundingBox;
	}

	@SuppressWarnings("unchecked")
	@Override
	public MultiShape3ifx<T> clone() {
		MultiShape3ifx<T> clone = super.clone();
		clone.elements = null;
		if (this.elements != null) {
			for (T shape : this.elements) {
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
		int b = (int) bits;
		return b ^ (b >> 32);
	}

	@Pure
	@Override
	public RectangularPrism3ifx toBoundingBox() {
		return boundingBoxProperty().get().clone();
	}

	@Pure
	@Override
	public void toBoundingBox(RectangularPrism3ifx box) {
		assert (box != null) : "Rectangle must be not null"; //$NON-NLS-1$
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
	private static class InternalObservableList<T extends Shape3ifx<?>> extends ModifiableObservableListBase<T>
			implements InvalidationListener {

		private final List<T> internalList = new ArrayList<>();

		/** Construct the list.
		 */
		public InternalObservableList() {
			//
		}
		
		private void bind(Shape3ifx<?> shape) {
			assert (shape != null);
			ObjectProperty<RectangularPrism3ifx> property = shape.boundingBoxProperty();
			property.addListener(this);
		}
		
		private void unbind(Shape3ifx<?> shape) {
			assert (shape != null);
			ObjectProperty<RectangularPrism3ifx> property = shape.boundingBoxProperty();
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
			assert (element != null) : "New element in the list of shapes must be not null"; //$NON-NLS-1$
			this.internalList.add(index, element);
			bind(element);
		}

		@Override
		protected T doSet(int index, T element) {
			assert (element != null) : "New element in the list of shapes must be not null"; //$NON-NLS-1$
			T old = this.internalList.set(index, element);
			unbind(old);
			bind(element);
			return old;
		}

		@Override
		protected T doRemove(int index) {
			T old = this.internalList.remove(index);
			unbind(old);
			return old;
		}

		@Override
		public void invalidated(Observable observable) {
			int position = indexOf(((Property<?>) observable).getBean());
			if (position >= 0) {
				fireChange(new SimpleUpdateChange<>(position, this));
			}
		}

	}

}