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

package org.arakhne.afc.gis.grid;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import org.arakhne.afc.gis.primitive.GISPrimitive;
import org.arakhne.afc.references.WeakArrayList;
import org.eclipse.xtext.xbase.lib.Pure;

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
public class GridCellElement<P extends GISPrimitive> implements Comparable<GridCellElement<P>> {

	private final P element;

	private final List<GridCell<P>> cells = new WeakArrayList<>(1);

	private WeakReference<GridCell<P>> referenceCell;

	/** Constructor.
	 * @param element the element.
	 */
	public GridCellElement(P element) {
		assert element != null;
		this.element = element;
	}

	private boolean setReferenceCell(GridCell<P> cell) {
		if (this.referenceCell == null) {
			this.referenceCell = new WeakReference<>(cell);
			return true;
		}
		final var ref = this.referenceCell.get();
		if (ref == null) {
			this.referenceCell = new WeakReference<>(cell);
			return true;
		}
		return false;
	}

	/** Add a reference from this element to a cell that is containing this element.
	 *
	 * @param cell the grid cell.
	 * @return {@code true} if the added cell is the reference;
	 *     {@code false} if the added cell is not the reference.
	 */
	public boolean addCellLink(GridCell<P> cell) {
		assert cell != null;
		this.cells.add(cell);
		return setReferenceCell(cell);
	}

	private boolean unsetReferenceCell(GridCell<P> cell) {
		if (this.referenceCell != null) {
			var ref = this.referenceCell.get();
			if (ref == cell) {
				this.referenceCell = null;
				// Set the reference to the next cell in the list
				if (!this.cells.isEmpty()) {
					ref = this.cells.get(0);
					if (ref != null) {
						this.referenceCell = new WeakReference<>(ref);
					}
				}
				return true;
			}
		}
		return false;
	}

	/** Add a reference from this element to a cell that is containing this element.
	 *
	 * @param cell the grid cell.
	 * @return {@code true} if the removed cell is the reference;
	 *     {@code false} if the removed cell is not the reference.
	 */
	public boolean removeCellLink(GridCell<P> cell) {
		assert cell != null;
		this.cells.remove(cell);
		return unsetReferenceCell(cell);
	}

	/** Replies the cell on which the given element is located.
	 * The cell links were removed from the element.
	 *
	 * @return a copy of the cells.
	 */
	public List<GridCell<P>> consumeCells() {
		final var list = new ArrayList<GridCell<P>>(this.cells.size());
		for (final var cell : this.cells) {
			if (cell != null) {
				list.add(cell);
			}
		}
		this.cells.clear();
		this.referenceCell = null;
		return list;
	}

	/** Replies if the specified cell is the reference cell for the element.
	 * The reference cell is the cell where the element is supported to
	 * be attached when it is supposed by be inside only one cell.
	 *
	 * @param cell is the cell to test
	 * @return {@code true} if the specified cell is the reference cell;
	 *     {@code false} otherwise.
	 */
	@Pure
	public boolean isReferenceCell(GridCell<P> cell) {
		return this.referenceCell != null && this.referenceCell.get() == cell;
	}

	@Override
	@Pure
	public boolean equals(Object obj) {
		if (obj instanceof GridCellElement<?> g) {
			return this.element.equals(g.get());
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
		final var p2 = obj.get();
		return this.element.getGeoId().compareTo(p2.getGeoId());
	}

	@Override
	@Pure
	public String toString() {
		return this.element.toString();
	}

}
