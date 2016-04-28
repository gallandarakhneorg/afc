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
package org.arakhne.afc.math.geometry.d2.ifx;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.arakhne.afc.math.geometry.d2.ai.MultiShape2ai;
import org.eclipse.xtext.xbase.lib.Pure;

import javafx.beans.binding.Bindings;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.ModifiableObservableListBase;

/** Container for grouping of shapes.
 * 
 * <p>The coordinates of the shapes inside the multishape are global. They are not relative to the multishape.
 *
 * <p>Caution: The multishape does not detect the bound change of the stored shapes.
 * 
 * @author $Author: tpiotrowski$
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public class MultiShape2ifx extends AbstractShape2ifx<MultiShape2ifx> implements 
	MultiShape2ai<Shape2ifx<?>, MultiShape2ifx, Shape2ifx<?>, PathElement2ifx, Point2ifx, Vector2ifx, Rectangle2ifx> {

	private static final long serialVersionUID = -4727279807601027239L;

	private ListProperty<Shape2ifx<?>> elements;

	private ReadOnlyObjectWrapper<Rectangle2ifx> boundingBox;
	
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
	public MultiShape2ifx(Shape2ifx<?>... shapes) {
		assert (shapes != null) : "Shape array must be not null"; //$NON-NLS-1$
		addAll(Arrays.asList(shapes));
	}

	/** Construct a multishape with shapes inside.
	 *
	 * @param shapes the shapes to add into the multishape.
	 */
	public MultiShape2ifx(Iterable<? extends Shape2ifx<?>> shapes) {
		assert (shapes != null) : "Shape list must be not null"; //$NON-NLS-1$
		for (Shape2ifx<?> element : shapes) {
			add(element);
		}
	}

	@Pure
	@Override
	public List<Shape2ifx<?>> getBackendDataList() {
		return elementsProperty().get();
	}

	/** Replies the property that contains all the shapes in this multishape.
	 *
	 * @return the elements property.
	 */
	public ListProperty<Shape2ifx<?>> elementsProperty() {
		if (this.elements == null) {
			ModifiableObservableListBase<Shape2ifx<?>> list = new ModifiableObservableListBase<Shape2ifx<?>>() {
				private final List<Shape2ifx<?>> internalList = new ArrayList<>();
				@Override
				public Shape2ifx<?> get(int index) {
					return this.internalList.get(index);
				}

				@Override
				public int size() {
					return this.internalList.size();
				}

				@Override
				protected void doAdd(int index, Shape2ifx<?> element) {
					assert (element != null) : "New element in the list of shapes must be not null"; //$NON-NLS-1$
					this.internalList.add(index, element);
				}

				@Override
				protected Shape2ifx<?> doSet(int index, Shape2ifx<?> element) {
					assert (element != null) : "New element in the list of shapes must be not null"; //$NON-NLS-1$
					return this.internalList.set(index, element);
				}

				@Override
				protected Shape2ifx<?> doRemove(int index) {
					return this.internalList.remove(index);
				}
			};
			this.elements = new SimpleListProperty<>(this, "elements", list); //$NON-NLS-1$
		}
		return this.elements;
	}
	
	/** Replies the property that contains the bouding box for this multishape.
	 *
	 * @return the bounding box.
	 */
	public ReadOnlyObjectProperty<Rectangle2ifx> boundingBoxProperty() {
		if (this.boundingBox == null) {
			this.boundingBox = new ReadOnlyObjectWrapper<>(this, "boundingBox"); //$NON-NLS-1$
			this.boundingBox.bind(Bindings.createObjectBinding(
					() -> {
						Rectangle2ifx box = new Rectangle2ifx();
						Rectangle2ifx shapeBox = new Rectangle2ifx();
						Iterator<Shape2ifx<?>> iterator = elementsProperty().iterator();
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
		return this.boundingBox.getReadOnlyProperty();
	}

	@Override
	public MultiShape2ifx clone() {
		MultiShape2ifx clone = super.clone();
		clone.elements = null;
		for (Shape2ifx<?> shape : this.elements) {
			clone.elementsProperty().add(shape.clone());
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
	public Rectangle2ifx toBoundingBox() {
		return boundingBoxProperty().get().clone();
	}

	@Pure
	@Override
	public void toBoundingBox(Rectangle2ifx box) {
		assert (box != null) : "Rectangle must be not null"; //$NON-NLS-1$
		box.set(boundingBoxProperty().get());
	}

}