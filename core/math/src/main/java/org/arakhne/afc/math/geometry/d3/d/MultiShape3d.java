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
package org.arakhne.afc.math.geometry.d3.d;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.arakhne.afc.math.geometry.d3.afp.MultiShape3afp;
import org.eclipse.xtext.xbase.lib.Pure;

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
public class MultiShape3d<T extends Shape3d<?>> extends AbstractShape3d<MultiShape3d<T>> implements 
	MultiShape3afp<Shape3d<?>, MultiShape3d<T>, T, PathElement3d, Point3d, Vector3d, RectangularPrism3d> {

	private static final long serialVersionUID = -4727279807601027239L;

	private List<T> elements = new ListResponseModel();

	private RectangularPrism3d bounds = null;

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
		assert (shapes != null) : "Shape array must be not null"; //$NON-NLS-1$
		addAll(Arrays.asList(shapes));
	}

	/** Construct a multishape with shapes inside.
	 *
	 * @param shapes the shapes to add into the multishape.
	 */
	public MultiShape3d(Iterable<? extends T> shapes) {
		assert (shapes != null) : "Shape list must be not null"; //$NON-NLS-1$
		for (T element : shapes) {
			add(element);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	@Pure
	public MultiShape3d<T> clone() {
		MultiShape3d<T> clone = super.clone();
		List<T> clonedList = new ArrayList<>();
		for (T shape : this.elements) {
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
		long bits = 1;
		bits = 31 * bits + this.elements.hashCode();
		int b = (int) bits;
		return b ^ (b >> 32);
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
	public RectangularPrism3d toBoundingBox() {
		if (this.bounds == null) {
			this.bounds = getGeomFactory().newBox();
			MultiShape3afp.super.toBoundingBox(this.bounds);
		}
		return this.bounds;
	}

	@Pure
	@Override
	public void toBoundingBox(RectangularPrism3d box) {
		assert (box != null) : "Rectangle must be not null"; //$NON-NLS-1$
		if (this.bounds == null) {
			this.bounds = getGeomFactory().newBox();
			MultiShape3afp.super.toBoundingBox(this.bounds);
		}
		box.set(this.bounds);
	}

	@Override
	public void translate(double dx, double dy, double dz) {
		if (dx != 0 || dy != 0 || dz != 0) {
			RectangularPrism3d box = this.bounds;
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
		public ListResponseModel() {
			//
		}

		@Override
		public void add(int index, T element) {
			assert (element != null);
			this.delegate.add(index, element);
			if (element instanceof AbstractShape3d<?>) {
				((AbstractShape3d<?>) element).addShapeGeometryChangeListener(this);
			}
		}
		
		@Override
		public T remove(int index) {
			T element = this.delegate.remove(index);
			if (element instanceof AbstractShape3d<?>) {
				((AbstractShape3d<?>) element).removeShapeGeometryChangeListener(this);
			}
			return element;
		}
		
		@Override
		public T set(int index, T element) {
			assert (element != null);
			T oldElement = this.delegate.set(index, element);
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

	}

}