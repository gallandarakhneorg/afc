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

import java.util.Collections;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.TreeSet;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.gis.primitive.GISPrimitive;
import org.arakhne.afc.math.geometry.d2.Point2D;
import org.arakhne.afc.math.geometry.d2.afp.Rectangle2afp;
import org.arakhne.afc.math.geometry.d2.d.Rectangle2d;

/**
 * A grid.
 *
 * @param <P> is the type of the user data inside the node.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 * @see GISPrimitive
 */
class Grid<P extends GISPrimitive> implements Iterable<P> {

	private final int nrows;

	private final int ncolumns;

	private final Rectangle2d bounds;

	private GridCell<P>[][] cells;

	private int elementCount;

	private int cellCount;

	/**
	 * @param nRows numbers of rows in the grid
	 * @param nColumns numbers of columns in the grid
	 * @param bounds are the bounds of the grid cell.
	 */
	@SuppressWarnings("unchecked")
	Grid(int nRows, int nColumns, Rectangle2d bounds) {
		this.nrows = nRows;
		this.ncolumns = nColumns;
		this.bounds = bounds;
		this.cells = new GridCell[this.nrows][this.ncolumns];
	}

	@Override
	@Pure
	public String toString() {
		return "(" + this.nrows + "x" + this.ncolumns + "}\n" + this.bounds.toString(); //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
	}

	/** Replies the number of rows in the grid.
	 *
	 * @return the number of rows in the grid.
	 */
	@Pure
	public int getRowCount() {
		return this.nrows;
	}

	/** Replies the number of columns in the grid.
	 *
	 * @return the number of columns in the grid.
	 */
	@Pure
	public int getColumnCount() {
		return this.ncolumns;
	}

	/** Replies the bounds of the cell.
	 *
	 * @return the cell.
	 */
	@Pure
	public Rectangle2d getBounds() {
		return this.bounds;
	}

	/** Clear the grid.
	 */
	@SuppressWarnings("unchecked")
	public void clear() {
		this.cells = new GridCell[this.nrows][this.ncolumns];
		this.elementCount = 0;
		this.cellCount = 0;
	}

	/** Replies if the grid is empty.
	 *
	 * @return <code>true</code> if the grid is empty; otherwise <code>false</code>.
	 */
	@Pure
	public boolean isEmpty() {
		return this.elementCount <= 0;
	}

	/** Replies the number of elements in the grid.
	 *
	 * @return the number of elements in the grid.
	 */
	@Pure
	public int getElementCount() {
		return this.elementCount;
	}

	/** Replies the cell at the specified location.
	 *
	 * @param row the row index.
	 * @param column the column index.
	 * @return the cell or <code>null</code> if no element at
	 *     the specified location.
	 */
	@Pure
	public GridCell<P> getCellAt(int row, int column) {
		return this.cells[row][column];
	}

	/** Replies the bounds covered by a cell at the specified location.
	 *
	 * @param row the row index.
	 * @param column the column index.
	 * @return the bounds.
	 */
	@Pure
	public Rectangle2d getCellBounds(int row, int column) {
		final double cellWidth = getCellWidth();
		final double cellHeight = getCellHeight();
		final double x = this.bounds.getMinX() + cellWidth * column;
		final double y = this.bounds.getMinY() + cellHeight * row;
		return new Rectangle2d(x, y, cellWidth, cellHeight);
	}

	/** Create a cell at the specified location if there is no
	 * cell at this location.
	 *
	 * @param row the row index.
	 * @param column the column index.
	 * @return the cell.
	 */
	public GridCell<P> createCellAt(int row, int column) {
		GridCell<P> cell = this.cells[row][column];
		if (cell == null) {
			cell = new GridCell<>(row, column, getCellBounds(row, column));
			this.cells[row][column] = cell;
			++this.cellCount;
		}
		return cell;
	}

	/** Remove the cell at the specified location.
	 *
	 * @param row the row index.
	 * @param column the column index.
	 * @return the removed cell or <code>null</code> if no element at
	 *     the specified location.
	 */
	public GridCell<P> removeCellAt(int row, int column) {
		final GridCell<P> cell = this.cells[row][column];
		if (cell != null) {
			this.cells[row][column] = null;
			--this.cellCount;
			for (final P element : cell) {
				removeElement(element);
			}
		}
		return cell;
	}

	/** Add the element in the grid.
	 *
	 * @param element the element.
	 * @return <code>true</code> if the element was added;
	 *     otherwise <code>false</code>.
	 */
	public boolean addElement(P element) {
		boolean changed = false;
		if (element != null) {
			final GridCellElement<P> gridElement = new GridCellElement<>(element);
			for (final GridCell<P> cell : getGridCellsOn(element.getGeoLocation().toBounds2D(), true)) {
				if (cell.addElement(gridElement)) {
					changed = true;
				}
			}
		}
		if (changed) {
			++this.elementCount;
		}
		return changed;
	}

	/** Remove the element at the specified location.
	 *
	 * @param element the element.
	 * @return <code>true</code> if the element was removed;
	 *     otherwise <code>false</code>.
	 */
	public boolean removeElement(P element) {
		boolean changed = false;
		if (element != null) {
			GridCellElement<P> gridElement;
			for (final GridCell<P> cell : getGridCellsOn(element.getGeoLocation().toBounds2D())) {
				gridElement = cell.removeElement(element);
				if (gridElement != null) {
					if (cell.isEmpty()) {
						this.cells[cell.row()][cell.column()] = null;
						--this.cellCount;
					}
					for (final GridCell<P> otherCell : gridElement.consumeCells()) {
						assert otherCell != cell;
						otherCell.removeElement(element);
						if (otherCell.isEmpty()) {
							this.cells[otherCell.row()][otherCell.column()] = null;
							--this.cellCount;
						}
					}
					changed = true;
				}
			}
		}
		if (changed) {
			--this.elementCount;
		}
		return changed;
	}

	/** Replies the number of cells in the grid that contains an element.
	 *
	 * @return the number of cells in the grid.
	 */
	@Pure
	public int getCellCount() {
		return this.cellCount;
	}

	/**
	 * Replies an iterator on all the existing grid cells.
	 *
	 * @return the iterator.
	 */
	@Pure
	public Iterable<GridCell<P>> getGridCells() {
		return new CellIterable(0, 0, getRowCount() - 1, getColumnCount() - 1, false);
	}

	/** Replies the grid cells that are intersecting the specified bounds.
	 *
	 * @param bounds the bounds.
	 * @return the grid cells.
	 */
	@Pure
	public Iterable<GridCell<P>> getGridCellsOn(Rectangle2afp<?, ?, ?, ?, ?, ?> bounds) {
		return getGridCellsOn(bounds, false);
	}

	/** Replies the grid cells that are intersecting the specified bounds.
	 *
	 * @param bounds the bounds
	 * @param createCells indicates if the not already created cells should be created.
	 * @return the grid cells.
	 */
	protected Iterable<GridCell<P>> getGridCellsOn(Rectangle2afp<?, ?, ?, ?, ?, ?> bounds, boolean createCells) {
		if (bounds.intersects(this.bounds)) {
			final int c1 = getColumnFor(bounds.getMinX());
			final int r1 = getRowFor(bounds.getMinY());
			final int c2 = getColumnFor(bounds.getMaxX());
			final int r2 = getRowFor(bounds.getMaxY());
			return new CellIterable(r1, c1, r2, c2, createCells);
		}
		return Collections.emptyList();
	}

	/** Replies the width of the cells.
	 *
	 * @return the width of the cells.
	 */
	@Pure
	public double getCellWidth() {
		return this.bounds.getWidth() / getColumnCount();
	}

	/** Replies the height of the cells.
	 *
	 * @return the height of the cells.
	 */
	@Pure
	public double getCellHeight() {
		return this.bounds.getHeight() / getRowCount();
	}

	/** Replies the column index for the specified position.
	 *
	 * @param x the x coordinate.
	 * @return the column index or {@code -1} if outside.
	 */
	@Pure
	public int getColumnFor(double x) {
		final double xx = x - this.bounds.getMinX();
		if (xx >= 0.)  {
			final int idx = (int) (xx / getCellWidth());
			assert idx >= 0;
			if (idx < getColumnCount()) {
				return idx;
			}
			return getColumnCount() - 1;
		}
		return 0;
	}

	/** Replies the row index for the specified position.
	 *
	 * @param y y coordinate.
	 * @return the row index or {@code -1} if outside.
	 */
	@Pure
	public int getRowFor(double y) {
		final double yy = y - this.bounds.getMinY();
		if (yy >= 0.)  {
			final int idx = (int) (yy / getCellHeight());
			assert idx >= 0;
			if (idx < getRowCount()) {
				return idx;
			}
			return getRowCount() - 1;
		}
		return 0;
	}

	/** Replies the cells around the specified point.
	 * The order of the replied cells follows cocentric circles
	 * around the cell that contains the specified point.
	 *
	 * @param position the position.
	 * @param maximalDistance is the distance above which the cells are not replied.
	 * @return the iterator on the cells.
	 */
	@Pure
	public AroundCellIterable<P> getGridCellsAround(Point2D<?, ?> position, double maximalDistance) {
		final int column = getColumnFor(position.getX());
		final int row = getRowFor(position.getY());
		return new AroundIterable(row, column, position, maximalDistance);
	}

	@Override
	@Pure
	public Iterator<P> iterator() {
		return new ElementIterator(new CellIterator(0, 0, getRowCount() - 1, getColumnCount() - 1, false), -1);
	}

	/** Replies the elements that are inside the cells intersecting the
	 * specified bounds.
	 *
	 * @param bounds the bounds.
	 * @return the iterator on the elements.
	 */
	@Pure
	public Iterator<P> iterator(Rectangle2afp<?, ?, ?, ?, ?, ?> bounds) {
		if (this.bounds.intersects(bounds)) {
			final int c1 = getColumnFor(bounds.getMinX());
			final int r1 = getRowFor(bounds.getMinY());
			final int c2 = getColumnFor(bounds.getMaxX());
			final int r2 = getRowFor(bounds.getMaxY());
			return new BoundedElementIterator(new CellIterator(r1, c1, r2, c2, false), -1, bounds);
		}
		return Collections.<P>emptyList().iterator();
	}

	/** Replies the elements that are inside the cells intersecting the
	 * specified bounds.
	 *
	 * @param bounds the bounds.
	 * @param budget is the number of elements to return through the iterator.
	 * @return the iterator on the elements.
	 */
	@Pure
	public Iterator<P> iterator(Rectangle2afp<?, ?, ?, ?, ?, ?> bounds, int budget) {
		if ((budget == -1 || budget > 0) && this.cellCount > 0 && this.bounds.intersects(bounds)) {
			final int c1 = getColumnFor(bounds.getMinX());
			final int r1 = getRowFor(bounds.getMinY());
			final int c2 = getColumnFor(bounds.getMaxX());
			final int r2 = getRowFor(bounds.getMaxY());
			return new BoundedElementIterator(new CellIterator(r1, c1, r2, c2, false), budget, bounds);
		}
		return Collections.<P>emptyList().iterator();
	}

	/** Replies the index of the specified element.
	 *
	 * @param element the element.
	 * @return the index of the specified element or {@code -1} if it was not found.
	 */
	@Pure
	public int indexOf(P element) {
		int idx;
		int globalIdx = 0;
		for (final GridCell<P> cell : getGridCells()) {
			idx = cell.indexOf(element);
			if (idx != -1) {
				return globalIdx + idx;
			}
			globalIdx += cell.getReferenceElementCount();
		}
		return -1;
	}

	/** Replies the element at the specified index.
	 *
	 * @param index the index.
	 * @return the element at the specified position.
	 * @throw {@link IndexOutOfBoundsException}
	 */
	@Pure
	public P getElementAt(int index) {
		if (index >= 0) {
			int idx = 0;
			int eIdx;
			for (final GridCell<P> cell : getGridCells()) {
				eIdx = idx + cell.getReferenceElementCount();
				if (index < eIdx) {
					try {
						return cell.getElementAt(index - idx);
					} catch (IndexOutOfBoundsException exception) {
						throw new IndexOutOfBoundsException(Integer.toString(index));
					}
				}
				idx = eIdx;
			}
		}
		throw new IndexOutOfBoundsException(Integer.toString(index));
	}

	/**
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 14.0
	 */
	private class CellIterable implements Iterable<GridCell<P>> {

		private final boolean createCells;

		private final int minRow;

		private final int minCol;

		private final int maxRow;

		private final int maxCol;

		/**
		 * @param minRow the minimal row index.
		 * @param minCol the minimal column index.
		 * @param maxRow the maximal row index
		 * @param maxCol the maximal column index.
		 * @param createCells indicates if the not already created cells should be created.
		 */
		CellIterable(int minRow, int minCol, int maxRow, int maxCol, boolean createCells) {
			this.createCells = createCells;
			this.minRow = minRow;
			this.minCol = minCol;
			this.maxRow = maxRow;
			this.maxCol = maxCol;
		}

		@Override
		@Pure
		public Iterator<GridCell<P>> iterator() {
			return new CellIterator(this.minRow, this.minCol, this.maxRow, this.maxCol, this.createCells);
		}

	}

	/**
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 14.0
	 */
	private class CellIterator implements Iterator<GridCell<P>> {

		private final int minCol;

		private final int maxRow;

		private final int maxCol;

		private final boolean createCells;

		private int row;

		private int column;

		private GridCell<P> next;

		private boolean searched;

		/**
		 * @param minRow the minimal row index.
		 * @param minCol the minimal column index.
		 * @param maxRow the maximal row index
		 * @param maxCol the maximal column index.
		 * @param createCells indicates if the not already created cells should be created.
		 */
		CellIterator(int minRow, int minCol, int maxRow, int maxCol, boolean createCells) {
			assert minRow >= 0;
			assert minCol >= 0;
			assert minRow < getRowCount();
			assert minCol < getRowCount();
			assert maxRow >= 0;
			assert maxCol >= 0;
			assert maxRow < getRowCount();
			assert maxCol < getRowCount();
			assert minRow <= maxRow;
			assert minCol <= maxCol;
			this.minCol = minCol;
			this.maxRow = maxRow;
			this.maxCol = maxCol;
			this.row = minRow;
			this.column = minCol;
			this.createCells = createCells;
		}

		private void searchNext() {
			this.next = null;
			this.searched = true;
			while (this.next == null && this.row <= this.maxRow) {
				GridCell<P> cell = getCellAt(this.row, this.column);
				if (cell == null && this.createCells) {
					cell = createCellAt(this.row, this.column);
				}
				++this.column;
				if (this.column > this.maxCol) {
					this.column = this.minCol;
					++this.row;
				}
				if (cell != null) {
					this.next = cell;
				}
			}
		}

		@Override
		@Pure
		public boolean hasNext() {
			if (!this.searched) {
				searchNext();
			}
			return this.next != null;
		}

		@Override
		public GridCell<P> next() {
			if (!this.searched) {
				searchNext();
			}
			if (this.next == null) {
				throw new NoSuchElementException();
			}
			this.searched = false;
			return this.next;
		}

		@Override
		public void remove() {
			final GridCell<P> cell = this.next;
			this.next = null;
			this.searched = false;
			if (cell == null) {
				throw new NoSuchElementException();
			}
			removeCellAt(cell.row(), cell.column());
		}

	}

	/**
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 14.0
	 */
	private class ElementIterator implements Iterator<P> {

		private final Iterator<GridCell<P>> iterator;

		private Iterator<GridCellElement<P>> elementIterator;

		private GridCell<P> currentCell;

		private P next;

		private boolean searched;

		private int budget;

		/** Constructor.
		 * @param iterator the iterator on grid cells.
		 * @param budget the budget.
		 */
		ElementIterator(Iterator<GridCell<P>> iterator, int budget) {
			this.iterator = iterator;
			this.budget = budget;
		}

		private void searchNext() {
			GridCellElement<P> gElement;

			this.next = null;
			this.searched = true;

			if (this.budget == -1 || this.budget > 0) {

				while (this.next == null
						&& ((this.elementIterator != null && this.elementIterator.hasNext()) || this.iterator.hasNext())) {

					while ((this.elementIterator == null || !this.elementIterator.hasNext())
						&& this.iterator.hasNext()) {
						this.currentCell = this.iterator.next();
						this.elementIterator = this.currentCell.getGridCellElements();
					}

					while (this.next == null && this.elementIterator != null && this.elementIterator.hasNext()) {
						gElement = this.elementIterator.next();
						if (gElement.isReferenceCell(this.currentCell)) {
							this.next = gElement.get();
							if (this.budget > 0) {
								--this.budget;
							}
						}
					}
				}
			}
		}

		@Override
		@Pure
		public boolean hasNext() {
			if (!this.searched) {
				searchNext();
			}
			return this.next != null;
		}

		@Override
		public P next() {
			if (!this.searched) {
				searchNext();
			}
			if (this.next == null) {
				throw new NoSuchElementException();
			}
			this.searched = false;
			return this.next;
		}

		@Override
		public void remove() {
			final P elt = this.next;
			this.next = null;
			this.searched = false;
			if (elt == null) {
				throw new NoSuchElementException();
			}
			removeElement(elt);
		}

	}

	/**
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 14.0
	 */
	private class BoundedElementIterator implements Iterator<P> {

		private final Rectangle2afp<?, ?, ?, ?, ?, ?> iterationBounds;

		private final Iterator<GridCell<P>> iterator;

		private final Set<GridCellElement<P>> closeList = new TreeSet<>();

		private Iterator<GridCellElement<P>> elementIterator;

		private GridCell<P> currentCell;

		private P next;

		private boolean searched;

		private int budget;

		/** Constructor.
		 * @param iterator the iterator on grid cells.
		 * @param budget the budget.
		 * @param bounds the intersection bounds.
		 */
		BoundedElementIterator(Iterator<GridCell<P>> iterator, int budget, Rectangle2afp<?, ?, ?, ?, ?, ?> bounds) {
			this.iterationBounds = bounds;
			this.iterator = iterator;
			this.budget = budget;
		}

		@SuppressWarnings("checkstyle:cyclomaticcomplexity")
		private void searchNext() {
			GridCellElement<P> gElement;
			P pElement;

			this.next = null;
			this.searched = true;

			if (this.budget == -1 || this.budget > 0) {

				while (this.next == null
						&& ((this.elementIterator != null && this.elementIterator.hasNext()) || this.iterator.hasNext())) {

					while ((this.elementIterator == null || !this.elementIterator.hasNext())
						&& this.iterator.hasNext()) {
						this.currentCell = this.iterator.next();
						this.elementIterator = this.currentCell.getGridCellElements();
					}

					while (this.next == null && this.elementIterator != null && this.elementIterator.hasNext()) {
						gElement = this.elementIterator.next();
						pElement = gElement.get();
						final Rectangle2d pBounds = pElement.getGeoLocation().toBounds2D();
						if (((this.iterationBounds.isEmpty() && this.iterationBounds.equals(pBounds))
							 || this.iterationBounds.intersects(pBounds)) && !this.closeList.contains(gElement)) {
							this.next = pElement;
							this.closeList.add(gElement);
							if (this.budget > 0) {
								--this.budget;
							}
						}
					}
				}
			}
		}

		@Override
		@Pure
		public boolean hasNext() {
			if (!this.searched) {
				searchNext();
			}
			return this.next != null;
		}

		@Override
		public P next() {
			if (!this.searched) {
				searchNext();
			}
			if (this.next == null) {
				throw new NoSuchElementException();
			}
			this.searched = false;
			return this.next;
		}

		@Override
		public void remove() {
			final P elt = this.next;
			this.next = null;
			this.searched = false;
			if (elt == null) {
				throw new NoSuchElementException();
			}
			removeElement(elt);
		}

	}

	/**
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 14.0
	 */
	private class AroundIterable implements AroundCellIterable<P> {

		private final int row;

		private final int column;

		private final double maximalDistance;

		private final Point2D<?, ?> referencePoint;

		AroundIterable(int row, int column, Point2D<?, ?> referencePoint, double maximalDistance) {
			this.row = row;
			this.column = column;
			this.maximalDistance = maximalDistance;
			this.referencePoint = referencePoint;
		}

		@Override
		@Pure
		public Iterator<GridCell<P>> iterator() {
			return aroundIterator();
		}

		@Override
		@Pure
		public AroundCellIterator<P> aroundIterator() {
			return new AroundIterator(this.row, this.column, this.referencePoint, this.maximalDistance);
		}

	}

	/**
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 14.0
	 */
	private class AroundIterator implements AroundCellIterator<P> {

		private final Point2D<?, ?> referencePoint;

		private final double maximalSquaredDistance;

		private final int row;

		private final int column;

		private GridCell<P> next;

		private boolean searched;

		private int level = 1;

		private Side side;

		private int sideIndex;

		private boolean foundInCircle;

		AroundIterator(int row, int column, Point2D<?, ?> referencePoint, double maximalDistance) {
			this.referencePoint = referencePoint;
			this.row = row;
			this.column = column;
			this.maximalSquaredDistance = maximalDistance * maximalDistance;
		}

		private boolean isValidBounds(GridCell<P> cell, int row, int column) {
			Rectangle2d rect = null;
			if (cell != null) {
				rect = cell.getBounds();
			}
			if (rect == null) {
				rect = getCellBounds(row, column);
			}
			assert rect != null;
			return rect.getDistanceSquared(this.referencePoint) <= this.maximalSquaredDistance;
		}

		@SuppressWarnings({"checkstyle:cyclomaticcomplexity", "checkstyle:npathcomplexity"})
		private void searchNext() {
			GridCell<P> cell;

			this.next = null;
			this.searched = true;

			if (this.level == 1) {
				++this.level;
				this.sideIndex = 0;
				this.side = Side.TOP;
				this.foundInCircle = false;
				if (this.row >= 0 && this.row < getRowCount()
						&& this.column >= 0 && this.column < getColumnCount()) {
					cell = getCellAt(this.row, this.column);
					if (isValidBounds(cell, this.row, this.column)) {
						if (cell != null) {
							this.next = cell;
							return;
						}
					}
				}
			}

			do {
				switch (this.side) {
				case TOP:
					if (this.sideIndex == 0) {
						this.foundInCircle = false;
					}
					while (this.next == null && this.sideIndex < (this.level - 1) * 2) {
						final int r = this.row - this.level + 1;
						if (r >= 0 && r < getRowCount()) {
							final int c = this.column - this.level + 1 + this.sideIndex;
							++this.sideIndex;
							if (c >= 0 && c < getColumnCount()) {
								cell = getCellAt(r, c);
								if (isValidBounds(cell, r, c)) {
									this.foundInCircle = true;
									if (cell != null) {
										this.next = cell;
										return;
									}
								}
							}
						} else {
							break;
						}
					}
					this.side = Side.LEFT;
					this.sideIndex = 0;
					break;
				case LEFT:
					while (this.next == null && this.sideIndex < (this.level - 1) * 2) {
						final int c = this.column + this.level - 1;
						if (c >= 0 && c < getColumnCount()) {
							final int r = this.row - this.level + 1 + this.sideIndex;
							++this.sideIndex;
							if (r >= 0 && r < getRowCount()) {
								cell = getCellAt(r, c);
								if (isValidBounds(cell, r, c)) {
									this.foundInCircle = true;
									if (cell != null) {
										this.next = cell;
										return;
									}
								}
							}
						} else {
							break;
						}
					}
					this.side = Side.BOTTOM;
					this.sideIndex = 0;
					break;
				case BOTTOM:
					while (this.next == null && this.sideIndex < (this.level - 1) * 2) {
						final int r = this.row + this.level - 1;
						if (r >= 0 && r < getRowCount()) {
							final int c = this.column - this.level + 2 + this.sideIndex;
							++this.sideIndex;
							if (c >= 0 && c < getColumnCount()) {
								cell = getCellAt(r, c);
								if (isValidBounds(cell, r, c)) {
									this.foundInCircle = true;
									if (cell != null) {
										this.next = cell;
										return;
									}
								}
							}
						} else {
							break;
						}
					}
					this.side = Side.RIGHT;
					this.sideIndex = 0;
					break;
				case RIGHT:
					while (this.next == null && this.sideIndex < (this.level - 1) * 2) {
						final int c = this.column - this.level + 1;
						if (c >= 0 && c < getColumnCount()) {
							final int r = this.row - this.level + 2 + this.sideIndex;
							++this.sideIndex;
							if (r >= 0 && r < getRowCount()) {
								cell = getCellAt(r, c);
								if (isValidBounds(cell, r, c)) {
									this.foundInCircle = true;
									if (cell != null) {
										this.next = cell;
										return;
									}
								}
							}
						} else {
							break;
						}
					}
					this.side = Side.TOP;
					this.sideIndex = 0;
					++this.level;
					if (!this.foundInCircle) {
						return;
					}
					break;
				default:
				}
			}
			while (this.next == null);
		}

		@Override
		@Pure
		public int getLevel() {
			return this.level - 1;
		}

		@Override
		@Pure
		public boolean hasNext() {
			if (!this.searched) {
				searchNext();
			}
			return this.next != null;
		}

		@Override
		public GridCell<P> next() {
			if (!this.searched) {
				searchNext();
			}
			if (this.next == null) {
				throw new NoSuchElementException();
			}
			this.searched = false;
			return this.next;
		}

		@Override
		public void remove() {
			final GridCell<P> cell = this.next;
			this.next = null;
			this.searched = false;
			if (cell == null) {
				throw new NoSuchElementException();
			}
			removeCellAt(cell.row(), cell.column());
		}

	}

	/**
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 14.0
	 */
	private enum Side {
		TOP, LEFT, RIGHT, BOTTOM;
	}

}
