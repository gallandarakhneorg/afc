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

package org.arakhne.afc.gis.tree;

import java.util.Iterator;
import java.util.Set;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.gis.location.GeoId;
import org.arakhne.afc.gis.location.GeoLocation;
import org.arakhne.afc.gis.primitive.GISPrimitive;
import org.arakhne.afc.math.geometry.d2.afp.Rectangle2afp;
import org.arakhne.afc.math.tree.Tree;

/**
 * This interface describes a tree that contains GIS primitives
 * and that permits to find them according to there geo-location.
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
public interface GISTreeSet<P extends GISPrimitive, N extends GISTreeNode<P, N>> extends Set<P> {

	/** Replies the internal tree data structure.
	 *
	 * @return the internal tree data structure.
	 */
	@Pure
	Tree<P, N> getTree();

	/** Replies the tree node that is containing the given coordinates.
	 *
	 * @param x x coordinate.
	 * @param y y coordinate.
	 * @return the tree node with the givne point inside, or <code>null</code>
	 *     if the point is outside all the tree nodes.
	 */
	@Pure
	N getTreeNodeAt(double x, double y);

	/** Set the node factory used by this tree.
	 *
	 * @param factory the factory.
	 */
	void setNodeFactory(GISTreeSetNodeFactory<P, N> factory);

	/** Replies the node factory used by this tree.
	 *
	 * @return the factory
	 */
	@Pure
	GISTreeSetNodeFactory<P, N> getNodeFactory();

	/** Replies if this informed tree set is trying to
	 * compute the best fitting type after a removal.
	 * If <code>false</code>, this class will keep unchanged
	 * the previously computed type.
	 *
	 * @return <code>true</code> if the type is recomputed
	 *     after a removal, <code>false</code> if not.
	 */
	@Pure
	boolean isTypeRecomputedAfterRemoval();

	/** Set if this informed tree set is trying to
	 * compute the best fitting type after a removal.
	 * If <code>false</code>, this class will keep unchanged
	 * the previously computed type.
	 *
	 * @param update is <code>true</code> if the type is recomputed
	 *     after a removal, <code>false</code> if not.
	 */
	void setTypeRecomputedAfterRemoval(boolean update);

	/** Replies the class of the components in this list.
	 *
	 * @return the top most type of the array's elements.
	 */
	@Pure
	Class<? extends P> getElementType();

	/** Replies the element which as the specified identifier.
	 *
	 * <p>This function is time consuming because the location
	 * of the primitive could not be retreived from
	 * the geoId.
	 *
	 * @param identifier is the identifier to text.
	 * @return the entity or <code>null</code> if none was found.
	 */
	@Pure
	P get(GeoId identifier);

	/** Replies the element which as the specified location.
	 *
	 * @param location is the location of the element to find.
	 * @return the entity or <code>null</code> if none was found.
	 */
	@Pure
	P get(GeoLocation location);

	/**
	 * Replies the element at the specified index.
	 *
	 * <p>This method iterates until the right index.
	 *
	 * @param index the index.
	 * @return the element at the given index.
	 * @throws IndexOutOfBoundsException in case of error.
	 */
	@Pure
	P get(int index);

	/**
	 * Force the deep computation of the element count in this set.
	 *
	 * @return the count of elements inside the tree.
	 */
	int computeSize();

	/**
	 * Replies if the given object is inside the set
	 * by user a depth-first iteration on the internal tree.
	 *
	 * @param obj the object.
	 * @return <code>true</code> of <var>o</var> is inside,
	 *     otherwise <code>false</code>
	 * @see #contains(Object)
	 */
	@Pure
	boolean slowContains(Object obj);

	/**
	 * Replies the index of the first instance the specified element.
	 *
	 * <p>This method iterates until the right index.
	 *
	 * @param obj the object.
	 * @return the index or {@code -1} if the object was not inside this set.
	 */
	@Pure
	int indexOf(Object obj);

	/** Replies the set of elements that have an intersection with the specified rectangle.
	 *
	 * <p>This function replies the elements with a broad-first iteration on the elements' tree.
	 *
	 * @param clipBounds is the bounds outside which the elements will not be replied
	 * @return the elements inside the specified bounds.
	 */
	@Pure
	Iterator<P> iterator(Rectangle2afp<?, ?, ?, ?, ?, ?> clipBounds);

	/** Replies the set of elements that have an intersection with the specified rectangle.
	 *
	 * <p>This function replies the elements with a broad-first iteration on the elements' tree.
	 *
	 * @param clipBounds is the bounds outside which the elements will not be replied
	 * @param budget is the maximal size of the replied list. If this value is negative, all the elements will be replied.
	 * @return the elements inside the specified bounds.
	 */
	@Pure
	Iterator<P> iterator(Rectangle2afp<?, ?, ?, ?, ?, ?> clipBounds, int budget);

	/** Replies the bounding rectangles of the internal data-structure elements.
	 *
	 * @return the bounding boxes in the data-structure.
	 */
	@Pure
	Iterator<Rectangle2afp<?, ?, ?, ?, ?, ?>> boundsIterator();

	/** Replies the set of elements that have an intersection with the specified rectangle.
	 *
	 * <p>This function replies the elements with a broad-first iteration on the elements' tree.
	 *
	 * @param clipBounds is the bounds outside which the elements will not be replied
	 * @return the elements inside the specified bounds.
	 */
	@Pure
	Iterable<P> toIterable(Rectangle2afp<?, ?, ?, ?, ?, ?> clipBounds);

	/** Replies the set of elements that have an intersection with the specified rectangle.
	 *
	 * <p>This function replies the elements with a broad-first iteration on the elements' tree.
	 *
	 * @param clipBounds is the bounds outside which the elements will not be replied
	 * @param budget is the maximal size of the replied list. If this value is negative, all the elements will be replied.
	 * @return the elements inside the specified bounds.
	 */
	@Pure
	Iterable<P> toIterable(Rectangle2afp<?, ?, ?, ?, ?, ?> clipBounds, int budget);

}
