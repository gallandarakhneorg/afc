/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2018 The original authors, and other authors.
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

package org.arakhne.afc.gis.grid;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.gis.primitive.GISPrimitive;
import org.arakhne.afc.references.WeakArrayList;

/**
 * Element inside a grid cell.
 *
 * @param <P> is the type of the user data inside the node.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 * @see GISPrimitive
 */
class GridCellElement<P extends GISPrimitive> implements Comparable<GridCellElement<P>> {

	private final P element;

	private final List<GridCell<P>> cells = new WeakArrayList<>(1);

	/** Constructor.
	 * @param element the element.
	 */
	GridCellElement(P element) {
		assert element != null;
		this.element = element;
	}

	/** Add a reference from this element to a cell that is containing this element.
	 *
	 * @param cell the grid cell.
	 * @return <code>true</code> if the added cell is the reference;
	 * <code>false</code> if the added cell is not the reference.
	 */
	public boolean addCellLink(GridCell<P> cell) {
		if (this.cells.add(cell)) {
			return isReferenceCell(cell);
		}
		return false;
	}

	/** Add a reference from this element to a cell that is containing this element.
	 *
	 * @param cell the grid cell.
	 * @return <code>true</code> if the removed cell is the reference;
	 * <code>false</code> if the removed cell is not the reference.
	 */
	public boolean removeCellLink(GridCell<P> cell) {
		final int idx = this.cells.indexOf(cell);
		if (idx >= 0) {
			this.cells.remove(idx);
		}
		return idx == 0;
	}

	/** Replies the cell on which the given element is located.
	 * The cell links were removed from the element.
	 *
	 * @return a copy of the cells.
	 */
	public List<GridCell<P>> consumeCells() {
		final List<GridCell<P>> list = new ArrayList<>(this.cells);
		this.cells.clear();
		return list;
	}

	/** Replies if the specified cell is the reference cell for the element.
	 * The reference cell is the cell where the element is supported to
	 * be attached when it is supposed by be inside only one cell.
	 *
	 * @param cell is the cell to test
	 * @return <code>true</code> if the specified cell is the reference cell;
	 * <code>false</code> otherwise.
	 */
	@Pure
	public boolean isReferenceCell(GridCell<P> cell) {
		return !this.cells.isEmpty() && this.cells.get(0) == cell;
	}

	@Override
	@Pure
	public boolean equals(Object obj) {
		if (obj instanceof GridCellElement<?>) {
			return this.element.equals(((GridCellElement<?>) obj).get());
		}
		return this.element.equals(obj);
	}

	@Override
	@Pure
	public int hashCode() {
		return this.element.hashCode();
	}

	/** Replies the element.
	 *
	 * @return the element.
	 */
	@Pure
	public P get() {
		return this.element;
	}

	@Override
	@Pure
	public int compareTo(GridCellElement<P> obj) {
		if (obj == null) {
			return Integer.MAX_VALUE;
		}
		final P p2 = obj.get();
		return this.element.getGeoId().compareTo(p2.getGeoId());
	}

	@Override
	@Pure
	public String toString() {
		return this.element.toString();
	}

}
