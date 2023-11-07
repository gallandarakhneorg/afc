/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2023 The original authors and other contributors.
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

import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.List;

import org.eclipse.xtext.xbase.lib.Pure;

import org.arakhne.afc.gis.primitive.GISPrimitive;
import org.arakhne.afc.math.geometry.d2.afp.Rectangle2afp;
import org.arakhne.afc.math.tree.Tree;
import org.arakhne.afc.math.tree.iterator.AbstractBreadthFirstTreeIterator;
import org.arakhne.afc.math.tree.iterator.AbstractDataTreeIterator;
import org.arakhne.afc.math.tree.iterator.NodeSelectionTreeIterator;
import org.arakhne.afc.math.tree.iterator.NodeSelector;
import org.arakhne.afc.math.tree.node.UnsupportedOperationTreeNode;

/**
 * This class provides a broad-first iterator supplying
 * the data elements according to a maximum number of
 * data to reply, the budget.
 *
 * @param <P> is the type of the data inside the tree.
 * @param <N> the type of the nodes.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 14.0
 */
class BudgetIterator<P extends GISPrimitive, N extends AbstractGISTreeSetNode<P, N>>
		implements Iterator<P> {

	private final BudgetDataIterator<P, N> it;

	/** Constructor.
	 * @param tree is the tree to iterator on.
	 * @param clipBounds is the bounds outside which the elements will not be replied
	 * @param budget is the maximal size of the replied list. If this value is negative, all the elements will be replied.
	 */
	BudgetIterator(Tree<P, N> tree, Rectangle2afp<?, ?, ?, ?, ?, ?> clipBounds, int budget) {
		this.it = new BudgetDataIterator<>(tree, clipBounds, budget);
	}

	@Override
	@Pure
	public boolean hasNext() {
		return this.it.hasNext();
	}

	@Override
	public P next() {
		return this.it.next();
	}

	@Override
	public void remove() {
		this.it.remove();
	}

	/** Frustum selection with budget.
	 * @param <P> is the type of the data inside the tree.
	 * @param <N> the type of the nodes.
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	private static class BudgetFrustumSelector<P extends GISPrimitive, N extends AbstractGISTreeSetNode<P, N>>
			extends FrustumSelector<P, N> {

		WeakReference<BudgetNode<P, N>> currentNode;

		/** Constructor.
		 *
		 * @param clipBounds the selection bounds.
		 */
		BudgetFrustumSelector(Rectangle2afp<?, ?, ?, ?, ?, ?> clipBounds) {
			super(clipBounds);
		}

		@Override
		@Pure
		public boolean dataCouldBeRepliedByIterator(P data) {
			if (!super.dataCouldBeRepliedByIterator(data)) {
				return false;
			}
			final BudgetNode<P, N> n = this.currentNode == null ? null : this.currentNode.get();
			return n != null && n.branchBudget > 0;
		}

	} // class BudgetFrustumSelector

	/** Data iterator with budget.
	 * @param <P> is the type of the data inside the tree.
	 * @param <N> the type of the nodes.
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	private static class BudgetDataIterator<P extends GISPrimitive, N extends AbstractGISTreeSetNode<P, N>>
			extends AbstractDataTreeIterator<P, BudgetNode<P, N>> {

		private final BudgetFrustumSelector<P, N> selector;

		private int unconsumedBudget;

		/** Constructor.
		 * @param tree is the tree to iterator on.
		 * @param clipBounds is the bounds outside which the elements will not be replied
		 * @param budget is the maximal size of the replied list. If this value is negative, all the elements will be replied.
		 */
		BudgetDataIterator(Tree<P, N> tree, Rectangle2afp<?, ?, ?, ?, ?, ?> clipBounds, int budget) {
			this(tree, new BudgetFrustumSelector<P, N>(clipBounds), budget);
		}

		private BudgetDataIterator(Tree<P, N> tree, BudgetFrustumSelector<P, N> selector, int budget) {
			this(new BudgetNodeIterator<>(tree, selector, budget), selector);
		}

		private BudgetDataIterator(BudgetNodeIterator<P, N> nodeIterator, BudgetFrustumSelector<P, N> selector) {
			super(nodeIterator, selector);
			this.selector = selector;
		}

		@Override
		protected void onBeforeNodeData(BudgetNode<P, N> node) {
			node.branchBudget += this.unconsumedBudget;
			this.unconsumedBudget = 0;
			this.selector.currentNode = new WeakReference<>(node);
		}

		@Override
		protected void onDataSelected(BudgetNode<P, N> node, P data) {
			node.branchBudget = Math.max(0, node.branchBudget - 1);
		}

		@Override
		protected void onAfterNodeData(BudgetNode<P, N> node) {
			this.selector.currentNode = null;
			this.unconsumedBudget = node.branchBudget;
			node.branchBudget = 0;
		}

	} // class BudgetDataIterator

	/** Budget node iterator.
	 * @param <P> is the type of the data inside the tree.
	 * @param <N> the type of the nodes.
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	private static class BudgetNodeIterator<P extends GISPrimitive, N extends AbstractGISTreeSetNode<P, N>>
			extends AbstractBreadthFirstTreeIterator<BudgetNode<P, N>, N>
			implements NodeSelectionTreeIterator<BudgetNode<P, N>> {

		/** Budget that was not consumed by the iterator.
		 */
		int unconsumedBudget;

		private final NodeSelector<N> selector;

		/** Constructor.
		 * @param tree the tree.
		 * @param nodeSelector the node selector.
		 * @param budget the budget.
		 */
		BudgetNodeIterator(Tree<?, N> tree, NodeSelector<N> nodeSelector, int budget) {
			super(new BudgetNode<>(tree.getRoot(), budget));
			this.selector = nodeSelector;
		}

		@Override
		protected BudgetNode<P, N> toTraversableChild(BudgetNode<P, N> parent, N child, int childIndex, int notNullChildIndex) {
			assert parent != null && child != null;
			final int nbNotNull = parent.getNotNullChildCount();
			if (nbNotNull > 0) {
				final int totalBudget = parent.branchBudget;
				int branchBudget = totalBudget / (nbNotNull - notNullChildIndex);
				branchBudget += totalBudget % (nbNotNull - notNullChildIndex);
				if (branchBudget > 0 && (this.selector == null || this.selector.nodeCouldBeTreatedByIterator(child))) {
					parent.branchBudget -= branchBudget;
					assert parent.branchBudget >= 0;
					return new BudgetNode<>(child, branchBudget);
				}
			}
			return null;
		}

		@Override
		protected void onAfterChildNodes(BudgetNode<P, N> node, int childCount) {
			this.unconsumedBudget = Math.max(0, node.branchBudget);
		}

		@Override
		protected void onBeforeChildNodes(BudgetNode<P, N> node) {
			node.branchBudget += this.unconsumedBudget;
			this.unconsumedBudget = 0;
		}

		@Override
		public void setNodeSelector(NodeSelector<BudgetNode<P, N>> selector) {
			throw new UnsupportedOperationException();
		}

	} // class BudgetNodeIterator

	/** Budget node.
	 * @param <P> is the type of the data inside the tree.
	 * @param <N> the type of the nodes.
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	private static class BudgetNode<P extends GISPrimitive, N extends AbstractGISTreeSetNode<P, N>>
			extends UnsupportedOperationTreeNode<P, N> {

		/** Wrapped tree node.
		 */
		final N node;

		/** Current budget for the node and its children.
		 */
		int branchBudget;

		/** Constructor.
		 *
		 * @param node the node.
		 * @param budget the budget.
		 */
		BudgetNode(N node, int budget) {
			this.node = node;
			this.branchBudget = budget;
		}

		@Override
		@Pure
		public N getChildAt(int index) throws IndexOutOfBoundsException {
			return this.node.getChildAt(index);
		}

		@Override
		@Pure
		public int getChildCount() {
			return this.node.getChildCount();
		}

		@Override
		@Pure
		public int getNotNullChildCount() {
			return this.node.getNotNullChildCount();
		}

		@Override
		@Pure
		public boolean isLeaf() {
			return this.node.isLeaf();
		}

		@Override
		public N removeFromParent() {
			return this.node.removeFromParent();
		}

		@Override
		@Pure
		public List<P> getAllUserData() {
			return this.node.getAllUserData();
		}

	} // class BudgetNode

}
