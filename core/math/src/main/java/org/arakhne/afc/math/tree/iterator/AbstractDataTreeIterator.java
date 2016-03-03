/* 
 * $Id$
 * 
 * Copyright (c) 2011, Multiagent Team,
 * Laboratoire Systemes et Transports,
 * Universite de Technologie de Belfort-Montbeliard.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of the Laboratoire Systemes et Transports
 * of the Universite de Technologie de Belfort-Montbeliard ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with the SeT.
 *
 * http://www.multiagent.fr/
 */
package org.arakhne.afc.math.tree.iterator;

import java.util.Iterator;
import java.util.NoSuchElementException;

import org.arakhne.afc.math.tree.TreeNode;

/**
 * This class is an iterator on a tree that replies the user data.
 * <p>
 * This iterator go thru the tree according to the node iterator
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
public abstract class AbstractDataTreeIterator<D,N extends TreeNode<D,?>> implements DataSelectionTreeIterator<D,N> {

	/** Node iterator.
	 */
	protected final NodeSelectionTreeIterator<N> nodeIterator;
	
	/** Current iterator on the data.
	 */
	private Iterator<D> dataIterator = null;
	
	/** Selector/Filter of data.
	 */
	private DataSelector<D> dataSelector;
	
	/** Next data to reply or <code>null</code> if none.
	 */
	private D nextData;
		
	/** Node of the data stored inside <var>nextData</var> or <code>null</code> if none.
	 */
	private N nextDataNode = null;

	/** Search should not be directly invoked from the
	 * constructor because setDataSelector() or setNodeSelector()
	 * may be invoke later. That why the flag started is
	 * required to differ the initialization of the iterator
	 * as late as possible.
	 */
	private boolean searched = false;

	/**
	 * @param nodeIterator is the iterator on the nodes.
	 * @param dataSelector permits to filter the user data repliable by this iterator.
	 */
	public AbstractDataTreeIterator(NodeSelectionTreeIterator<N> nodeIterator, DataSelector<D> dataSelector) {
		assert(nodeIterator!=null);
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

	private void searchNext() {
		N node = this.nextDataNode;
		N oldNode;
		D data;
		
		this.searched = true;
		this.nextData = null;
		this.nextDataNode = null;
		
		while (this.nextData==null &&
				((this.dataIterator!=null && this.dataIterator.hasNext()) || this.nodeIterator.hasNext())) {
			
			oldNode = node;
			
			while ((this.dataIterator==null || !this.dataIterator.hasNext())
				&&this.nodeIterator.hasNext()) {
				node = this.nodeIterator.next();
				this.dataIterator = node.getAllUserData().iterator();
			}
			
			if (oldNode!=node) {
				if (oldNode!=null)
					onAfterNodeData(oldNode);
				if (node!=null)
					onBeforeNodeData(node);
			}
			
			while (this.nextData==null && this.dataIterator!=null && this.dataIterator.hasNext()) {
				data = this.dataIterator.next();
				if (this.dataSelector==null || this.dataSelector.dataCouldBeRepliedByIterator(data)) {
					this.nextData = data;
					this.nextDataNode = node;
					onDataSelected(node, data);
				}
			}
		}
		
		if (this.nextData==null)
			this.dataIterator = null;
	}
	
	/** Invoked when a data was selected by the iterator for the next
	 * invocation of {@link #next()}
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

	@Override
	public boolean hasNext() {
		if (!this.searched) searchNext();
		return this.nextData!=null;
	}

	@Override
	public D next() {
		if (!this.searched) searchNext();
		if (this.nextData==null) throw new NoSuchElementException();
		this.searched = false;
		return this.nextData;
	}

	@Override
	public void remove() {
		if (this.dataIterator==null) throw new NoSuchElementException();
		this.dataIterator.remove();
	}
	
}
