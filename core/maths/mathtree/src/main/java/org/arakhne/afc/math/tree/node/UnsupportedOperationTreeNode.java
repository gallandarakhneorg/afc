/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2022 The original authors, and other authors.
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

package org.arakhne.afc.math.tree.node;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.eclipse.xtext.xbase.lib.Pure;

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
 * @since 13.0
 */
@SuppressWarnings("checkstyle:methodcount")
public class UnsupportedOperationTreeNode<D, N extends TreeNode<D, N>>
		implements TreeNode<D, N> {

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
	public D[] getAllUserData(D[] array) {
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
	public int compareTo(N obj) {
		throw new UnsupportedOperationException();
	}

}
