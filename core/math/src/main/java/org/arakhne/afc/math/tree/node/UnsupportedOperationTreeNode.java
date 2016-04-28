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
import org.eclipse.xtext.xbase.lib.Pure;


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
 * @since 13.0
 */
public class UnsupportedOperationTreeNode<D,N extends TreeNode<D,N>>
implements TreeNode<D,N> {
	
	@Override
	@Pure
	public N getParentNode() {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public boolean setChildAt(int index, N child) throws IndexOutOfBoundsException {
		throw new UnsupportedOperationException();
	}

	@Override
	@Pure
	public int indexOf(N child) {
		throw new UnsupportedOperationException();
	}

	@Override
	@Pure
	public Class<? extends Enum<?>> getPartitionEnumeration() {
		throw new UnsupportedOperationException();
	}

	@Override
	public D[] getAllUserData(D[] a) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	@Pure
	public List<D> getAllUserData() {
		throw new UnsupportedOperationException();
	}

	@Override
	@Pure
	public D getUserData() {
		throw new UnsupportedOperationException();
	}

	@Override
	@Pure
	public int getUserDataCount() {
		throw new UnsupportedOperationException();
	}

	@Override
	@Pure
	public D getUserDataAt(int index) throws IndexOutOfBoundsException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean addUserData(Collection<? extends D> data) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean addUserData(int index, Collection<? extends D> data) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean addUserData(D data) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void addUserData(int index, D data) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean removeUserData(Collection<D> data) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean removeUserData(D data) {
		throw new UnsupportedOperationException();
	}

	@Override
	public D removeUserData(int index) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean setUserData(Collection<D> data) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean setUserData(D data) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean setUserDataAt(int index, D data) throws IndexOutOfBoundsException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void addTreeNodeListener(TreeNodeListener listener) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void removeTreeNodeListener(TreeNodeListener listener) {
		throw new UnsupportedOperationException();
	}

	@Override
	@Pure
	public boolean isEmpty() {
		throw new UnsupportedOperationException();
	}

	@Override
	@Pure
	public boolean isRoot() {
		throw new UnsupportedOperationException();
	}

	@Override
	@Pure
	public boolean isValid() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void clear() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void removeAllUserData() {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public boolean removeChild(N child) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void removeDeeplyFromParent() {
		throw new UnsupportedOperationException();
	}

	@Override
	@Pure
	public int getDeepNodeCount() {
		throw new UnsupportedOperationException();
	}
	
	@Override
	@Pure
	public int getDeepUserDataCount() {
		throw new UnsupportedOperationException();
	}

	@Override
	@Pure
	public Iterator<N> children() {
		throw new UnsupportedOperationException();
	}
	
	@Override
	@Pure
	public N[] getChildren(Class<N> type) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void getChildren(Object[] array) {
		throw new UnsupportedOperationException();
	}

	@Pure
	@Override
	public int getDepth() {
		throw new UnsupportedOperationException();
	}

	@Pure
	@Override
	public int getMinHeight() {
		throw new UnsupportedOperationException();
	}

	@Pure
	@Override
	public int getMaxHeight() {
		throw new UnsupportedOperationException();
	}

	@Pure
	@Override
	public int[] getHeights() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean moveTo(N newParent, int index) {
		throw new UnsupportedOperationException();
	}

	@Pure
	@Override
	public N getChildAt(int index) throws IndexOutOfBoundsException {
		throw new UnsupportedOperationException();
	}

	@Pure
	@Override
	public int getChildCount() {
		throw new UnsupportedOperationException();
	}

	@Pure
	@Override
	public int getNotNullChildCount() {
		throw new UnsupportedOperationException();
	}

	@Pure
	@Override
	public boolean isLeaf() {
		throw new UnsupportedOperationException();
	}

	@Override
	public N removeFromParent() {
		throw new UnsupportedOperationException();
	}

	@Pure
	@Override
	public int compareTo(N o) {
		throw new UnsupportedOperationException();
	}

}