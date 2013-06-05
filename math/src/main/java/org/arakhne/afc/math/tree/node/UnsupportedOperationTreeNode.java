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
package org.arakhne.afc.math.tree.node;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.arakhne.afc.math.tree.TreeNode;
import org.arakhne.afc.math.tree.TreeNodeListener;


/**
 * Generic implementation of a tree node that throws
 * {@link UnsupportedOperationException} for all
 * its operation. The sub classes must override the operations
 * to provide a different behaviors.
 * 
 * @param <D> is the type of the data inside the tree
 * @param <N> is the type of the tree nodes.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class UnsupportedOperationTreeNode<D,N extends TreeNode<D,N>>
implements TreeNode<D,N> {
	
	/** {@inheritDoc}
	 */
	@Override
	public N getParentNode() {
		throw new UnsupportedOperationException();
	}
	
	/** {@inheritDoc}
	 */
	@Override
	public boolean setChildAt(int index, N child) throws IndexOutOfBoundsException {
		throw new UnsupportedOperationException();
	}

	/** {@inheritDoc}
	 */
	@Override
	public int indexOf(N child) {
		throw new UnsupportedOperationException();
	}

	/** {@inheritDoc}
	 */
	@Override
	public Class<? extends Enum<?>> getPartitionEnumeration() {
		throw new UnsupportedOperationException();
	}

	/** {@inheritDoc}
	 */
	@Override
	public D[] getAllUserData(D[] a) {
		throw new UnsupportedOperationException();
	}
	
	/** {@inheritDoc}
	 */
	@Override
	public List<D> getAllUserData() {
		throw new UnsupportedOperationException();
	}

	/** {@inheritDoc}
	 */
	@Override
	public D getUserData() {
		throw new UnsupportedOperationException();
	}

	/** {@inheritDoc}
	 */
	@Override
	public int getUserDataCount() {
		throw new UnsupportedOperationException();
	}

	/** {@inheritDoc}
	 */
	@Override
	public D getUserDataAt(int index) throws IndexOutOfBoundsException {
		throw new UnsupportedOperationException();
	}

	/** {@inheritDoc}
	 */
	@Override
	public boolean addUserData(Collection<? extends D> data) {
		throw new UnsupportedOperationException();
	}

	/** {@inheritDoc}
	 */
	@Override
	public boolean addUserData(int index, Collection<? extends D> data) {
		throw new UnsupportedOperationException();
	}

	/** {@inheritDoc}
	 */
	@Override
	public boolean addUserData(D data) {
		throw new UnsupportedOperationException();
	}

	/** {@inheritDoc}
	 */
	@Override
	public void addUserData(int index, D data) {
		throw new UnsupportedOperationException();
	}

	/** {@inheritDoc}
	 */
	@Override
	public boolean removeUserData(Collection<D> data) {
		throw new UnsupportedOperationException();
	}

	/** {@inheritDoc}
	 */
	@Override
	public boolean removeUserData(D data) {
		throw new UnsupportedOperationException();
	}

	/** {@inheritDoc}
	 */
	@Override
	public D removeUserData(int index) {
		throw new UnsupportedOperationException();
	}

	/** {@inheritDoc}
	 */
	@Override
	public boolean setUserData(Collection<D> data) {
		throw new UnsupportedOperationException();
	}

	/** {@inheritDoc}
	 */
	@Override
	public boolean setUserData(D data) {
		throw new UnsupportedOperationException();
	}

	/** {@inheritDoc}
	 */
	@Override
	public boolean setUserDataAt(int index, D data) throws IndexOutOfBoundsException {
		throw new UnsupportedOperationException();
	}

	/** {@inheritDoc}
	 */
	@Override
	public void addTreeNodeListener(TreeNodeListener listener) {
		throw new UnsupportedOperationException();
	}

	/** {@inheritDoc}
	 */
	@Override
	public void removeTreeNodeListener(TreeNodeListener listener) {
		throw new UnsupportedOperationException();
	}

	/** {@inheritDoc}
	 */
	@Override
	public boolean isEmpty() {
		throw new UnsupportedOperationException();
	}

	/** {@inheritDoc}
	 */
	@Override
	public boolean isRoot() {
		throw new UnsupportedOperationException();
	}

	/** {@inheritDoc}
	 */
	@Override
	public boolean isValid() {
		throw new UnsupportedOperationException();
	}

	/** {@inheritDoc}
	 */
	@Override
	public void clear() {
		throw new UnsupportedOperationException();
	}

	/** {@inheritDoc}
	 */
	@Override
	public void removeAllUserData() {
		throw new UnsupportedOperationException();
	}
	
	/** {@inheritDoc}
	 */
	@Override
	public boolean removeChild(N child) {
		throw new UnsupportedOperationException();
	}

	/** {@inheritDoc}
	 */
	@Override
	public void removeDeeplyFromParent() {
		throw new UnsupportedOperationException();
	}

	/** {@inheritDoc}
	 */
	@Override
	public int getDeepNodeCount() {
		throw new UnsupportedOperationException();
	}
	
	/** {@inheritDoc}
	 */
	@Override
	public int getDeepUserDataCount() {
		throw new UnsupportedOperationException();
	}

	/** {@inheritDoc}
	 */
	@Override
	public Iterator<N> children() {
		throw new UnsupportedOperationException();
	}
	
	/** {@inheritDoc}
	 */
	@Override
	public N[] getChildren(Class<N> type) {
		throw new UnsupportedOperationException();
	}

	/** {@inheritDoc}
	 */
	@Override
	public void getChildren(Object[] array) {
		throw new UnsupportedOperationException();
	}

	/** {@inheritDoc}
	 */
	@Override
	public int getDepth() {
		throw new UnsupportedOperationException();
	}

	/** {@inheritDoc}
	 */
	@Override
	public int getMinHeight() {
		throw new UnsupportedOperationException();
	}

	/** {@inheritDoc}
	 */
	@Override
	public int getMaxHeight() {
		throw new UnsupportedOperationException();
	}

	/** {@inheritDoc}
	 */
	@Override
	public int[] getHeights() {
		throw new UnsupportedOperationException();
	}

	/** {@inheritDoc}
	 */
	@Override
	public boolean moveTo(N newParent, int index) {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * @deprecated no replacement
	 */
	@Deprecated
	@Override
	public void setParentNode(N newParent) {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public N getChildAt(int index) throws IndexOutOfBoundsException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getChildCount() {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getNotNullChildCount() {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isLeaf() {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public N removeFromParent() {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int compareTo(N o) {
		throw new UnsupportedOperationException();
	}

}