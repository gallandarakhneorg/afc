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

package org.arakhne.afc.math.tree.iterator;

import java.util.Iterator;
import java.util.NoSuchElementException;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.math.tree.TreeNode;

/**
 * This class is an iterator on a tree that replies the user data.
 *
 * <p>This iterator go thru the tree according to the node iterator
 * passed to the constructor.
 *
 * @param <D> is the type of the data inside the tree
 * @param <N> is the type of the tree nodes.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public abstract class AbstractDataTreeIterator<D, N extends TreeNode<D, ?>> implements DataSelectionTreeIterator<D, N> {

	/** Node iterator.
	 */
	protected final NodeSelectionTreeIterator<N> nodeIterator;

	/** Current iterator on the data.
	 */
	private Iterator<D> dataIterator;

	/** Selector/Filter of data.
	 */
	private DataSelector<D> dataSelector;

	/** Next data to reply or <code>null</code> if none.
	 */
	private D nextData;

	/** Node of the data stored inside <var>nextData</var> or <code>null</code> if none.
	 */
	private N nextDataNode;

	/** Search should not be directly invoked from the
	 * constructor because setDataSelector() or setNodeSelector()
	 * may be invoke later. That why the flag started is
	 * required to differ the initialization of the iterator
	 * as late as possible.
	 */
	private boolean searched;

	/** Constructor.
	 * @param nodeIterator is the iterator on the nodes.
	 * @param dataSelector permits to filter the user data repliable by this iterator.
	 */
	public AbstractDataTreeIterator(NodeSelectionTreeIterator<N> nodeIterator, DataSelector<D> dataSelector) {
		assert nodeIterator != null;
		this.dataSelector = dataSelector;
		this.nodeIterator = nodeIterator;
	}

	@Override
	public void setDataSelector(DataSelector<D> selector) {
		this.dataSelector = selector;
	}

	@Override
	public void setNodeSelector(NodeSelector<N> selector) {
		this.nodeIterator.setNodeSelector(selector);
	}

	@SuppressWarnings("checkstyle:cyclomaticcomplexity")
	private void searchNext() {
		N node = this.nextDataNode;
		N oldNode;
		D data;

		this.searched = true;
		this.nextData = null;
		this.nextDataNode = null;

		while (this.nextData == null
				&& ((this.dataIterator != null && this.dataIterator.hasNext()) || this.nodeIterator.hasNext())) {

			oldNode = node;

			while ((this.dataIterator == null || !this.dataIterator.hasNext())
				&& this.nodeIterator.hasNext()) {
				node = this.nodeIterator.next();
				this.dataIterator = node.getAllUserData().iterator();
			}

			if (oldNode != node) {
				if (oldNode != null) {
					onAfterNodeData(oldNode);
				}
				if (node != null) {
					onBeforeNodeData(node);
				}
			}

			while (this.nextData == null && this.dataIterator != null && this.dataIterator.hasNext()) {
				data = this.dataIterator.next();
				if (this.dataSelector == null || this.dataSelector.dataCouldBeRepliedByIterator(data)) {
					this.nextData = data;
					this.nextDataNode = node;
					onDataSelected(node, data);
				}
			}
		}

		if (this.nextData == null) {
			this.dataIterator = null;
		}
	}

	/** Invoked when a data was selected by the iterator for the next
	 * invocation of {@link #next()}.
	 *
	 * @param node is the node that is containing the data.
	 * @param data is the selected data.
	 */
	protected void onDataSelected(N node, D data) {
		//
	}

	/** Invoked before any data from the given node was treated by the iterator.
	 *
	 * @param node is the node that is containing the data.
	 */
	protected void onBeforeNodeData(N node) {
		//
	}

	/** Invoked after all the data from the given node was treated by the iterator.
	 *
	 * @param node is the node that is containing the data.
	 */
	protected void onAfterNodeData(N node) {
		//
	}

	@Pure
	@Override
	public boolean hasNext() {
		if (!this.searched) {
			searchNext();
		}
		return this.nextData != null;
	}

	@Override
	public D next() {
		if (!this.searched) {
			searchNext();
		}
		if (this.nextData == null) {
			throw new NoSuchElementException();
		}
		this.searched = false;
		return this.nextData;
	}

	@Override
	public void remove() {
		if (this.dataIterator == null) {
			throw new NoSuchElementException();
		}
		this.dataIterator.remove();
	}

}
