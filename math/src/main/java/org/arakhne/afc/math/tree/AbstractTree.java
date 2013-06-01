/* 
 * $Id$
 * 
 * Copyright (c) 2005-11, Multiagent Team,
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
package org.arakhne.afc.math.tree;

import java.io.Serializable;
import java.util.Iterator;

import org.arakhne.afc.math.tree.iterator.BroadFirstTreeIterator;
import org.arakhne.afc.math.tree.iterator.DataBroadFirstTreeIterator;
import org.arakhne.afc.math.tree.iterator.DepthFirstNodeOrder;
import org.arakhne.afc.math.tree.iterator.InfixDataDepthFirstTreeIterator;
import org.arakhne.afc.math.tree.iterator.InfixDepthFirstTreeIterator;
import org.arakhne.afc.math.tree.iterator.PostfixDataDepthFirstTreeIterator;
import org.arakhne.afc.math.tree.iterator.PostfixDepthFirstTreeIterator;
import org.arakhne.afc.math.tree.iterator.PrefixDataDepthFirstTreeIterator;
import org.arakhne.afc.math.tree.iterator.PrefixDepthFirstTreeIterator;


/**
 * This is the generic implementation of a
 * tree based on linked lists.
 * <p>
 * This tree assumes that the nodes are linked with there
 * references.
 * 
 * @param <D> is the type of the data inside the tree
 * @param <N> is the type of the tree nodes.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public abstract class AbstractTree<D,N extends TreeNode<D,N>> implements DepthFirstIterableTree<D,N>, BroadFirstIterableTree<D,N>, Serializable {

	private static final long serialVersionUID = 1192947956138993568L;

	/** {@inheritDoc}
	 */
	@Override
	public final boolean isEmpty() {
		return getUserDataCount()==0;
	}

	/** {@inheritDoc}
	 * The default iterator is a depth first iterator.
	 */
	@Override
	public final Iterator<N> iterator() {
		return depthFirstIterator();
	}

	/** {@inheritDoc}
	 */
	@Override
	public final Iterator<N> depthFirstIterator(DepthFirstNodeOrder nodeOrder) {
		switch(nodeOrder) {
		case POSTFIX:
			return new PostfixDepthFirstTreeIterator<N>(this);
		case INFIX:
			return new InfixDepthFirstTreeIterator<N>(this);
		default:
			return new PrefixDepthFirstTreeIterator<N>(this);
		}
	}

	/** {@inheritDoc}
	 */
	@Override
	public final Iterator<N> depthFirstIterator(int infixPosition) {
		if (infixPosition<=0)
			return new PrefixDepthFirstTreeIterator<N>(this);
		return new InfixDepthFirstTreeIterator<N>(this,infixPosition);
	}

	/** {@inheritDoc}
	 */
	@Override
	public final Iterator<N> depthFirstIterator() {
		return new PrefixDepthFirstTreeIterator<N>(this);
	}

	/** {@inheritDoc}
	 */
	@Override
	public final Iterator<N> broadFirstIterator() {
		return new BroadFirstTreeIterator<N>(this);
	}

	/** {@inheritDoc}
	 */
	@Override
	public final Iterator<D> dataDepthFirstIterator() {
		return new PrefixDataDepthFirstTreeIterator<D, N>(this);
	}

	/** {@inheritDoc}
	 */
	@Override
	public final Iterator<D> dataDepthFirstIterator(DepthFirstNodeOrder nodeOrder) {
		switch(nodeOrder) {
		case POSTFIX:
			return new PostfixDataDepthFirstTreeIterator<D, N>(this);
		case INFIX:
			return new InfixDataDepthFirstTreeIterator<D, N>(this);
		default:
			return new PrefixDataDepthFirstTreeIterator<D, N>(this);
		}
	}

	/** {@inheritDoc}
	 */
	@Override
	public final Iterator<D> dataDepthFirstIterator(int infixPosition) {
		if (infixPosition<=0)
			return new PrefixDataDepthFirstTreeIterator<D, N>(this);
		return new InfixDataDepthFirstTreeIterator<D, N>(this,infixPosition);
	}


	/** {@inheritDoc}
	 */
	@Override
	public final Iterator<D> dataBroadFirstIterator() {
		return new DataBroadFirstTreeIterator<D, N>(this);
	}

	/** {@inheritDoc}
	 */
	@Override
	public final Iterable<N> toDepthFirstIterable() {
		return new Iterable<N>() {
			@Override
			public Iterator<N> iterator() {
				return AbstractTree.this.depthFirstIterator();
			}
		};
	}

	/** {@inheritDoc}
	 */
	@Override
	public final Iterable<N> toDepthFirstIterable(final DepthFirstNodeOrder nodeOrder) {
		return new Iterable<N>() {
			@Override
			public Iterator<N> iterator() {
				return AbstractTree.this.depthFirstIterator(nodeOrder);
			}
		};
	}

	/** {@inheritDoc}
	 */
	@Override
	public final Iterable<N> toDepthFirstIterable(final int infixPosition) {
		return new Iterable<N>() {
			@Override
			public Iterator<N> iterator() {
				return AbstractTree.this.depthFirstIterator(infixPosition);
			}
		};
	}

	/** {@inheritDoc}
	 */
	@Override
	public final Iterable<N> toBroadFirstIterable() {
		return new Iterable<N>() {
			@Override
			public Iterator<N> iterator() {
				return AbstractTree.this.broadFirstIterator();
			}
		};
	}

	/** {@inheritDoc}
	 */
	@Override
	public final Iterable<D> toDataDepthFirstIterable() {
		return new Iterable<D>() {
			@Override
			public Iterator<D> iterator() {
				return AbstractTree.this.dataDepthFirstIterator();
			}
		};
	}

	/** {@inheritDoc}
	 */
	@Override
	public final Iterable<D> toDataDepthFirstIterable(final DepthFirstNodeOrder nodeOrder) {
		return new Iterable<D>() {
			@Override
			public Iterator<D> iterator() {
				return AbstractTree.this.dataDepthFirstIterator(nodeOrder);
			}
		};
	}

	/** {@inheritDoc}
	 */
	@Override
	public final Iterable<D> toDataDepthFirstIterable(final int infixPosition) {
		return new Iterable<D>() {
			@Override
			public Iterator<D> iterator() {
				return AbstractTree.this.dataDepthFirstIterator(infixPosition);
			}
		};
	}

	/** {@inheritDoc}
	 */
	@Override
	public final Iterable<D> toDataBroadFirstIterable() {
		return new Iterable<D>() {
			@Override
			public Iterator<D> iterator() {
				return AbstractTree.this.dataBroadFirstIterator();
			}
		};
	}
	
}