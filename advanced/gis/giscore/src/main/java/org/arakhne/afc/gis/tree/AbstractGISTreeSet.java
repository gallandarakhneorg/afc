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

package org.arakhne.afc.gis.tree;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

import org.arakhne.afc.gis.GISSet;
import org.arakhne.afc.gis.location.GeoId;
import org.arakhne.afc.gis.location.GeoLocation;
import org.arakhne.afc.gis.primitive.GISPrimitive;
import org.arakhne.afc.math.geometry.d2.afp.Rectangle2afp;
import org.arakhne.afc.math.geometry.d2.d.Point2d;
import org.arakhne.afc.math.geometry.d2.d.Rectangle2d;
import org.arakhne.afc.math.tree.LinkedTree;
import org.arakhne.afc.math.tree.Tree;
import org.arakhne.afc.math.tree.iterator.BreadthFirstTreeIterator;
import org.arakhne.afc.math.tree.iterator.DataBreadthFirstTreeIterator;
import org.arakhne.afc.math.tree.iterator.DataSelector;
import org.arakhne.afc.math.tree.iterator.NodeSelector;
import org.arakhne.afc.math.tree.iterator.PrefixDataDepthFirstTreeIterator;
import org.arakhne.afc.math.tree.iterator.PrefixDepthFirstTreeIterator;
import org.arakhne.afc.vmutil.ReflectionUtil;
import org.eclipse.xtext.xbase.lib.Pure;

/**
 * This class describes a quad tree that contains GIS primitives
 * and thatp permits to find them according to there geo-location.
 *
 * @param <P> is the type of the user data inside the node.
 * @param <N> is the type of the nodes.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 * @see GISPrimitive
 */
abstract class AbstractGISTreeSet<P extends GISPrimitive,
		N extends AbstractGISTreeSetNode<P, N>> implements GISSet<P>, GISTreeSet<P, N> {

	/** Bounds of the all the data in the tree.
	 */
	Rectangle2afp<?, ?, ?, ?, ?, ?> worldBounds;

	private Class<? extends P> clazz;

	private boolean updateWhenRemove;

	/** Internal representation.
	 */
	private final LinkedTree<P, N> tree = new LinkedTree<>();

	/**
	 * Create an empty tree.
	 */
	AbstractGISTreeSet() {
		this.worldBounds = null;
	}

	/** Constructor.
	 * @param bounds are the bounds of the scene stored inside this tree.
	 */
	AbstractGISTreeSet(Rectangle2afp<?, ?, ?, ?, ?, ?> bounds) {
		this.worldBounds = bounds;
	}

	/** Constructor.
	 * @param boundsX is the bounds of the scene.
	 * @param boundsY is the bounds of the scene.
	 * @param boundsWidth is the bounds of the scene.
	 * @param boundsHeight is the bounds of the scene.
	 */
	AbstractGISTreeSet(double boundsX, double boundsY, double boundsWidth, double boundsHeight) {
		this.worldBounds = new Rectangle2d(
				boundsX, boundsY,
				boundsWidth, boundsHeight);
	}

	/** Extract the upper class that contains all the elements of
	 * this array.
	 *
	 * @param <E> is the type of the list's elements.
	 * @param collection is the collection to scan
	 * @return the top class of all the elements.
	 */
	@SuppressWarnings("unchecked")
	@Pure
	protected static <E> Class<? extends E> extractClassFrom(Collection<? extends E> collection) {
		Class<? extends E> clazz = null;
		for (final var elt : collection) {
			clazz = (Class<? extends E>) ReflectionUtil.getCommonType(clazz, elt.getClass());
		}
		return clazz == null ? (Class<E>) Object.class : clazz;
	}

	@Override
	@Pure
	public Tree<P, N> getTree() {
		return this.tree;
	}

	//-----------------------------------------------------------------
	// Utilities
	//----------------------------------------------------------------

	@Override
	public abstract void setNodeFactory(GISTreeSetNodeFactory<P, N> factory);

	@Override
	@Pure
	public abstract GISTreeSetNodeFactory<P, N> getNodeFactory();

	@Override
	@Pure
	public boolean isTypeRecomputedAfterRemoval() {
		return this.updateWhenRemove;
	}

	@Override
	public void setTypeRecomputedAfterRemoval(boolean update) {
		this.updateWhenRemove = update;
	}

	/** Update the component type information with
	 * the type of the new array element.
	 *
	 * @param newElement is the element for which the known top type in this array must be eventually updated.
	 */
	@SuppressWarnings("unchecked")
	protected final void updateComponentType(P newElement) {
		final var lclazz = (Class<? extends P>) newElement.getClass();
		this.clazz = (Class<? extends P>) ReflectionUtil.getCommonType(this.clazz, lclazz);
	}

	/** Update the component type information with
	 * the type of the new array element.
	 *
	 * @param newElements are the elements for which the known top type in this array must be eventually updated.
	 */
	@SuppressWarnings("unchecked")
	protected final void updateComponentType(Collection<? extends P> newElements) {
		final var lclazz = extractClassFrom(newElements);
		this.clazz = (Class<? extends P>) ReflectionUtil.getCommonType(this.clazz, lclazz);
	}

	@Override
	@Pure
	public Class<? extends P> getElementType() {
		return this.clazz;
	}

	@Override
	@Pure
	public String toString() {
		final var buffer = new StringBuilder();
		buffer.append("["); //$NON-NLS-1$
		for (final var element : this.tree.toDataDepthFirstIterable()) {
			if (buffer.length() > 1) {
				buffer.append(", "); //$NON-NLS-1$
			}
			buffer.append(element.toString());
		}
		buffer.append("]"); //$NON-NLS-1$
		return buffer.toString();
	}

	@Override
	@Pure
	public N getTreeNodeAt(double x, double y) {
		final var iterator = new PrefixDepthFirstTreeIterator<>(this.tree, new PointSelector<N>(x, y));
		while (iterator.hasNext()) {
			final var node = iterator.next();
			if (node.isLeaf()) {
				return node;
			}
		}
		return null;
	}

	@Override
	@Pure
	public P get(GeoId identifier) {
		if (identifier != null) {
			final var objBounds = identifier.toBounds2D();
			if (objBounds != null) {
				final var selector = new GeoIdSelector<P, N>(objBounds, identifier);
				final var iterator = new PrefixDataDepthFirstTreeIterator<>(this.tree, selector, selector);
				if (iterator.hasNext()) {
					return iterator.next();
				}
			}
		}
		return null;
	}

	@Override
	@Pure
	public P get(GeoLocation location) {
		if (location != null) {
			final var selector = new GeoLocationSelector<P, N>(location);
			final var iterator = new PrefixDataDepthFirstTreeIterator<>(this.tree, selector, selector);
			if (iterator.hasNext()) {
				return iterator.next();
			}
		}
		return null;
	}

	@Override
	@Pure
	public P get(int index) {
		if (index < 0) {
			throw new IndexOutOfBoundsException("index<0"); //$NON-NLS-1$
		}
		final var iter = this.tree.dataDepthFirstIterator();
		var idx = 0;
		while (iter.hasNext()) {
			final var data = iter.next();
			if (idx == index) {
				return data;
			}
			++idx;
		}
		throw new IndexOutOfBoundsException("index>=" + idx); //$NON-NLS-1$
	}

	//-----------------------------------------------------------------
	// Collection Interface
	//----------------------------------------------------------------

	@Override
	public void clear() {
		this.tree.clear();
		this.worldBounds = null;
		this.clazz = null;
	}

	@Override
	@Pure
	public boolean isEmpty() {
		return this.tree.isEmpty();
	}

	@Override
	@Pure
	public int size() {
		return this.tree.getUserDataCount();
	}

	@Override
	public int computeSize() {
		return this.tree.computeUserDataCount();
	}

	@Override
	@Pure
	public Iterator<P> iterator() {
		return this.tree.dataBreadthFirstIterator();
	}

	@Override
	@Pure
	public Iterator<P> iterator(Rectangle2afp<?, ?, ?, ?, ?, ?> clipBounds, int budget) {
		final var root = this.tree.getRoot();
		if (root == null || clipBounds == null || clipBounds.isEmpty()) {
			return Collections.emptyIterator();
		}
		if (budget >= 0) {
			return new BudgetIterator<>(this.tree, clipBounds, budget);
		}
		final var selector = new FrustumSelector<P, N>(clipBounds);
		return new DataBreadthFirstTreeIterator<>(
				this.tree,
				selector,
				selector);
	}

	@Override
	@Pure
	public Iterator<P> iterator(Rectangle2afp<?, ?, ?, ?, ?, ?> clipBounds) {
		return iterator(clipBounds, -1);
	}

	@Override
	@Pure
	public Object[] toArray() {
		final var count = this.tree.getUserDataCount();
		final var tab = new Object[count];
		var i = 0;
		for (final var element : this.tree.toDataDepthFirstIterable()) {
			if (i >= count) {
				break;
			}
			tab[i] = element;
			++i;
		}
		return tab;
	}

	@Override
	@SuppressWarnings("unchecked")
	@Pure
	public <T> T[] toArray(T[] array) {
		assert array != null;
		final var clazz1 = (Class<T[]>) array.getClass();
		final var clazz2 = (Class<? extends T>) clazz1.getComponentType();

		var count = this.tree.getUserDataCount();
		var tab = array;

		if (array.length > count) {
			count = array.length;
		}
		if (array.length < count) {
			tab = clazz1.cast(Array.newInstance(clazz2, count));
		}

		var i = 0;
		for (final var element : this.tree.toDataDepthFirstIterable()) {
			if (i >= count) {
				break;
			}
			tab[i] = clazz2.cast(element);
			++i;
		}
		return tab;
	}

	@Override
	@SuppressWarnings("unchecked")
	@Pure
	public boolean contains(Object obj) {
		if (obj == null) {
			return false;
		}
		try {
			final var primitive = (P) obj;
			final var iter = nodeIterator(primitive.getGeoLocation().toBounds2D());
			while (iter.hasNext()) {
				final var node = iter.next();
				for (var i = 0; i < node.getUserDataCount(); ++i) {
					if (node.getUserDataAt(i).equals(obj)) {
						return true;
					}
				}
			}
		} catch (ClassCastException exception) {
			//
		}
		return false;
	}

	@Override
	@Pure
	public boolean slowContains(Object obj) {
		if (obj == null) {
			return false;
		}
		try {
			final var iterator = new PrefixDataDepthFirstTreeIterator<>(getTree());
			while (iterator.hasNext()) {
				final var primitive = iterator.next();
				if (obj.equals(primitive)) {
					return true;
				}
			}
		} catch (ClassCastException exception) {
			//
		}
		return false;
	}

	@Override
	@Pure
	public boolean containsAll(Collection<?> col) {
		for (final var object : col) {
			if (!contains(object)) {
				return false;
			}
		}
		return true;
	}

	@Override
	@SuppressWarnings("unchecked")
	public boolean remove(Object obj) {
		final var root = this.tree.getRoot();
		if (root == null) {
			return false;
		}
		try {
			final var primitive = (P) obj;
			final var iter = nodeIterator(primitive.getGeoLocation().toBounds2D());
			while (iter.hasNext()) {
				final var node = iter.next();
				if (node.removeUserData(primitive)) {
					if (isEmpty()) {
						this.clazz = null;
					} else if (isTypeRecomputedAfterRemoval()) {
						this.clazz = extractClassFrom(this);
					}
					return true;
				}
			}
		} catch (ClassCastException exception) {
			//
		}
		return false;
	}

	@Override
	@SuppressWarnings("unchecked")
	public boolean removeAll(Collection<?> col) {
		var changed = false;
		final var root = this.tree.getRoot();
		if (root == null) {
			return false;
		}
		for (final var o : col) {
			try {
				final var primitive = (P) o;
				final var iter = nodeIterator(primitive.getGeoLocation().toBounds2D());
				while (iter.hasNext()) {
					final var node = iter.next();
					if (node.removeUserData(primitive)) {
						changed = true;
					}
				}
			} catch (ClassCastException exception) {
				//
			}
		}
		if (changed) {
			if (isEmpty()) {
				this.clazz = null;
			} else if (isTypeRecomputedAfterRemoval()) {
				this.clazz = extractClassFrom(this);
			}
		}
		return changed;
	}

	@Override
	public boolean retainAll(Collection<?> col) {
		clear();
		return addAll(new CheckedCollection(col));
	}

	@Override
	public boolean addAll(Collection<? extends P> col) {
		if (col == null) {
			return false;
		}
		var changed = false;
		for (final var element : col) {
			changed = add(element) | changed;
		}
		return changed;
	}

	//-----------------------------------------------------------------
	// Several functions compliant with List interface
	//----------------------------------------------------------------

	@Override
	@SuppressWarnings("unchecked")
	@Pure
	public int indexOf(Object obj) {
		if (obj == null) {
			return -1;
		}
		final P element;
		try {
			element = (P) obj;
		} catch (ClassCastException exception) {
			return -1;
		}
		final var iter = this.tree.dataDepthFirstIterator();
		var idx = 0;
		while (iter.hasNext()) {
			final var data = iter.next();
			if (data == element) {
				return idx;
			}
			++idx;
		}
		return -1;
	}

	//-----------------------------------------------------------------
	// Dedicated API
	//----------------------------------------------------------------

	/** Replies the set of nodes that have an intersection with the specified rectangle.
	 *
	 * <p>This function replies the nodes with a broad-first iteration on the elements' tree.
	 *
	 * @param clipBounds is the bounds outside which the nodes will not be replied
	 * @return the nodes inside the specified bounds.
	 */
	@Pure
	Iterator<N> nodeIterator(Rectangle2afp<?, ?, ?, ?, ?, ?> clipBounds) {
		return new BreadthFirstTreeIterator<>(this.tree, new FrustumSelector<P, N>(clipBounds));
	}

	@Override
	@Pure
	public Iterator<Rectangle2afp<?, ?, ?, ?, ?, ?>> boundsIterator() {
		return new BoundsIterator(this.tree.iterator());
	}

	@Override
	@Pure
	public Iterable<P> toIterable(final Rectangle2afp<?, ?, ?, ?, ?, ?> clipBounds) {
		return () -> AbstractGISTreeSet.this.iterator(clipBounds);
	}

	@Override
	@Pure
	public Iterable<P> toIterable(final Rectangle2afp<?, ?, ?, ?, ?, ?> clipBounds, final int budget) {
		return () -> AbstractGISTreeSet.this.iterator(clipBounds, budget);
	}

	//-----------------------------------------------------------------
	// Subclasses
	//----------------------------------------------------------------

	/** Internal collection.
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @since 14.0
	 */
	private class CheckedCollection implements Collection<P>, Serializable {

		private static final long serialVersionUID = 1867729331611401138L;

		private final Collection<?> collection;

		/** Constructor.
		 * @param col the original collection.
		 */
		CheckedCollection(Collection<?> col) {
			assert col != null;
			this.collection = col;
		}

		@Override
		@Pure
		public int size() {
			return this.collection.size();
		}

		@Override
		@Pure
		public boolean isEmpty() {
			return this.collection.isEmpty();
		}

		@Override
		@Pure
		public boolean contains(Object obj) {
			return this.collection.contains(obj);
		}

		@Override
		@Pure
		public Object[] toArray() {
			return this.collection.toArray();
		}

		@Override
		@Pure
		public <T> T[] toArray(T[] array) {
			return this.collection.toArray(array);
		}

		@Override
		@Pure
		public String toString() {
			return this.collection.toString();
		}

		@Override
		public boolean remove(Object obj) {
			return this.collection.remove(obj);
		}

		@Override
		@Pure
		public boolean containsAll(Collection<?> coll) {
			return this.collection.containsAll(coll);
		}

		@Override
		public boolean removeAll(Collection<?> coll) {
			return this.collection.removeAll(coll);
		}

		@Override
		public boolean retainAll(Collection<?> coll) {
			return this.collection.retainAll(coll);
		}

		@Override
		public void clear() {
			this.collection.clear();
		}

		@Override
		@Pure
		public Iterator<P> iterator() {
			return new CheckedIterator();
		}

		@Override
		public boolean add(P elt) {
			return false;
		}

		@Override
		public boolean addAll(Collection<? extends P> coll) {
			return false;
		}

		/** Internal iterator.
		 * @author $Author: sgalland$
		 * @version $FullVersion$
		 * @mavengroupid $GroupId$
		 * @mavenartifactid $ArtifactId$
		 * @since 14.0
		 */
		private class CheckedIterator implements Iterator<P> {

			private P next;

			private final Iterator<?> it;

			/** Constructor.
			 */
			@SuppressWarnings({ "unchecked" })
			CheckedIterator() {
				this.it = CheckedCollection.this.collection.iterator();
				this.next = null;
				while (this.it.hasNext()) {
					try {
						this.next = (P) this.it.next();
						if (this.next != null) {
							break;
						}
					} catch (ClassCastException exception) {
						this.next = null;
					}
				}
			}

			@Override
			@Pure
			public boolean hasNext() {
				return this.next != null;
			}

			@Override
			@SuppressWarnings("unchecked")
			public P next() {
				final var n = this.next;
				this.next = null;
				while (this.it.hasNext()) {
					try {
						this.next = (P) this.it.next();
						if (this.next != null) {
							break;
						}
					} catch (Exception exception) {
						this.next = null;
					}
				}
				return n;
			}

			@Override
			public void remove() {
				this.it.remove();
			}
		}

	} /* class CheckedCollection */

	/**
	 * This class describes an iterator node selector based on location.
	 *
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	private static class GeoLocationSelector<P extends GISPrimitive, N extends AbstractGISTreeSetNode<P, N>>
			implements NodeSelector<N>, DataSelector<P> {

		private final GeoLocation referenceGeoLocation;

		private final Rectangle2afp<?, ?, ?, ?, ?, ?> bounds;

		/** Constructor.
		 * @param location the location.
		 */
		GeoLocationSelector(GeoLocation location) {
			assert location != null;
			this.referenceGeoLocation = location;
			this.bounds = location.toBounds2D();
		}

		@Override
		@Pure
		public boolean nodeCouldBeTreatedByIterator(N node) {
			return node.intersects(this.bounds);
		}

		@Override
		@Pure
		public boolean dataCouldBeRepliedByIterator(P data) {
			if (data != null) {
				return this.referenceGeoLocation.equals(data.getGeoLocation());
			}
			return false;
		}

	} /* class GeoLocationSelector */

	/**
	 * This class describes an iterator node selector based on identifier.
	 *
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	private static class GeoIdSelector<P extends GISPrimitive, N extends AbstractGISTreeSetNode<P, N>>
			implements NodeSelector<N>, DataSelector<P> {

		private final GeoId referenceIdentifier;

		private final Rectangle2afp<?, ?, ?, ?, ?, ?> bounds;

		/** Constructor.
		 * @param bounds the bounds.
		 * @param identifier the identifier.
		 */
		GeoIdSelector(Rectangle2afp<?, ?, ?, ?, ?, ?> bounds, GeoId identifier) {
			assert identifier != null;
			assert bounds != null;
			this.referenceIdentifier = identifier;
			this.bounds = bounds;
		}

		@Override
		@Pure
		public boolean nodeCouldBeTreatedByIterator(N node) {
			return node.intersects(this.bounds);
		}

		@Override
		@Pure
		public boolean dataCouldBeRepliedByIterator(P data) {
			if (data != null) {
				return this.referenceIdentifier.equals(data.getGeoId());
			}
			return false;
		}

	} /* class GeoIdSelector */

	/**
	 * This class describes an iterator on node bounds.
	 *
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 * @see GISPrimitive
	 */
	private static class BoundsIterator implements Iterator<Rectangle2afp<?, ?, ?, ?, ?, ?>> {

		private final Iterator<? extends AbstractGISTreeSetNode<?, ?>> iterator;

		/** Constructor.
		 * @param iterator the iterator.
		 */
		BoundsIterator(Iterator<? extends AbstractGISTreeSetNode<?, ?>> iterator) {
			this.iterator = iterator;
		}

		@Override
		@Pure
		public boolean hasNext() {
			return this.iterator.hasNext();
		}

		@Override
		public Rectangle2afp<?, ?, ?, ?, ?, ?> next() {
			final var node = this.iterator.next();
			return node.getBounds();
		}

		@Override
		public void remove() {
			this.iterator.remove();
		}

	} /* class BoundsIterator */

	/**
	 * This class describes an iterator node selector based on a point.
	 *
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	private static class PointSelector<N extends AbstractGISTreeSetNode<?, N>> implements NodeSelector<N> {

		private final Point2d point;

		/** Constructor.
		 *
		 * @param x x coordinate.
		 * @param y y coordinate.
		 */
		PointSelector(double x, double y) {
			this.point = new Point2d(x, y);
		}

		@Override
		@Pure
		public boolean nodeCouldBeTreatedByIterator(N node) {
			return node.contains(this.point);
		}

	} /* class PointSelector */

}
