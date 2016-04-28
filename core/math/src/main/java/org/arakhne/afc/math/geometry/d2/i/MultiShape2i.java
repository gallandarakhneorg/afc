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
package org.arakhne.afc.math.geometry.d2.i;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.arakhne.afc.math.geometry.d2.ai.MultiShape2ai;
import org.eclipse.xtext.xbase.lib.Pure;

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
public class MultiShape2i extends AbstractShape2i<MultiShape2i> implements 
	MultiShape2ai<Shape2i<?>, MultiShape2i, Shape2i<?>, PathElement2i, Point2i, Vector2i, Rectangle2i> {

	private static final long serialVersionUID = -4727279807601027239L;

	private List<Shape2i<?>> elements = new ArrayList<>();

	private Rectangle2i bounds = null;
	
	/**
	 * Construct an empty multishape.
	 */
	public MultiShape2i() {
		//
	}

	/** Construct a multishape with shapes inside.
	 *
	 * @param shapes the shapes to add into the multishape.
	 */
	public MultiShape2i(Shape2i<?>... shapes) {
		assert (shapes != null) : "Shape array must be not null"; //$NON-NLS-1$
		addAll(Arrays.asList(shapes));
	}

	/** Construct a multishape with shapes inside.
	 *
	 * @param shapes the shapes to add into the multishape.
	 */
	public MultiShape2i(Iterable<? extends Shape2i<?>> shapes) {
		assert (shapes != null) : "Shape list must be not null"; //$NON-NLS-1$
		for (Shape2i<?> element : shapes) {
			add(element);
		}
	}
	
	@Override
	public MultiShape2i clone() {
		MultiShape2i clone = super.clone();
		List<Shape2i<?>> clonedList = new ArrayList<>();
		for (Shape2i<?> shape : this.elements) {
			clonedList.add(shape.clone());
		}
		clone.elements = clonedList;
		if (this.bounds != null) {
			clone.bounds = this.bounds.clone();
		}
		return clone;
	}
	
	@Override
	public int hashCode() {
		long bits = 1;
		bits = 31 * bits + this.elements.hashCode();
		int b = (int) bits;
		return b ^ (b >> 32);
	}

	@Override
	public void set(MultiShape2i s) {
		Rectangle2i box = this.bounds;
		MultiShape2ai.super.set(s);
		if (this.bounds == null) {
			this.bounds = box;
		}
	}

	@Override
	public void onBackendDataChange() {
		this.bounds = null;
	}

	@Pure
	@Override
	public List<Shape2i<?>> getBackendDataList() {
		return this.elements;
	}

	@Pure
	@Override
	public Rectangle2i toBoundingBox() {
		if (this.bounds == null) {
			this.bounds = new Rectangle2i();
			MultiShape2ai.super.toBoundingBox(this.bounds);
		}
		return this.bounds;
	}
	
	@Pure
	@Override
	public void toBoundingBox(Rectangle2i box) {
		assert (box != null) : "Rectangle must be not null"; //$NON-NLS-1$
		if (this.bounds == null) {
			this.bounds = new Rectangle2i();
			MultiShape2ai.super.toBoundingBox(this.bounds);
		}
		box.set(this.bounds);
	}
	
	@Override
	public void translate(int dx, int dy) {
		Rectangle2i box = this.bounds;
		MultiShape2ai.super.translate(dx, dy);
		if (box != null) {
			box.translate(dx, dy);
			this.bounds = box;
		}
	}

}