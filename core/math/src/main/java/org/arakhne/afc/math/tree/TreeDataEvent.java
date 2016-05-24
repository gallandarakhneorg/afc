/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2016 The original authors, and other authors.
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

package org.arakhne.afc.math.tree;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.eclipse.xtext.xbase.lib.Pure;

/**
 * Event on tree nodes.
 *
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 * @since 13.0
 */
public class TreeDataEvent {

	private final TreeNode<?, ?> node;

	private final List<?> oldValues;

	private final List<?> newValues;

	private final List<?> allValues;

	private final int delta;

	/** Construct an event.
	 *
	 * @param node1 is the node on which the event that occurs
	 * @param oldValues1 is the list of old user data associated to the node
	 * @param newValues1 is the list of new user data associated to the node
	 * @param allValues1 is the list of all user data currently associated to the node
	 */
	public TreeDataEvent(TreeNode<?, ?> node1, Collection<?> oldValues1, Collection<?> newValues1, Collection<?> allValues1) {
		int aCount = 0;
		int rCount = 0;
		this.node = node1;
		if (oldValues1 == null) {
			this.oldValues = null;
		} else if (oldValues1 instanceof List<?>) {
			this.oldValues = (List<?>) oldValues1;
			rCount = this.oldValues.size();
		} else {
			this.oldValues = new ArrayList<>(oldValues1);
			rCount = this.oldValues.size();
		}
		if (newValues1 == null) {
			this.newValues = null;
		} else if (newValues1 instanceof List<?>) {
			this.newValues = (List<?>) newValues1;
			aCount = this.newValues.size();
		} else {
			this.newValues = new ArrayList<>(newValues1);
			aCount = this.newValues.size();
		}
		if (allValues1 == null) {
			this.allValues = null;
		} else if (allValues1 instanceof List<?>) {
			this.allValues = (List<?>) allValues1;
		} else {
			this.allValues = new ArrayList<>(allValues1);
		}
		this.delta = aCount - rCount;
	}

	/** Construct an event.
	 *
	 * @param node1 is the node on which the event that occurs
	 * @param delta1 is the difference between the size of the data list
	 *     before change and size after change.
	 * @param allValues1 is the list of all user data currently associated to the node
	 */
	public TreeDataEvent(TreeNode<?, ?> node1, int delta1, Collection<?> allValues1) {
		final int aCount = 0;
		final int rCount = 0;
		this.node = node1;
		this.oldValues = null;
		this.newValues = null;
		if (allValues1 == null) {
			this.allValues = null;
		} else if (allValues1 instanceof List<?>) {
			this.allValues = (List<?>) allValues1;
		} else {
			this.allValues = new ArrayList<>(allValues1);
		}
		this.delta = aCount - rCount;
	}

	/** Replies the node on which the event occurs.
	 *
	 * @return the node on which the event occurs.
	 */
	@Pure
	public TreeNode<?, ?> getNode() {
		return this.node;
	}

	/** Replies the count of removed values.
	 *
	 * @return the count of removed values.
	 */
	@Pure
	public int getRemovedValueCount() {
		return (this.oldValues == null) ? 0 : this.oldValues.size();
	}

	/** Replies the count of added values.
	 *
	 * @return the count of added values.
	 */
	@Pure
	public int getAddedValueCount() {
		return (this.newValues == null) ? 0 : this.newValues.size();
	}

	/** Replies the count of all associated values.
	 *
	 * @return the count of all associated values.
	 */
	@Pure
	public int getCurrentValueCount() {
		return (this.allValues == null) ? 0 : this.allValues.size();
	}

	/** Replies the value removed from a node.
	 *
	 * @param index is the index of the value
	 * @return the old value at the given index.
	 */
	@Pure
	public Object getRemovedValueAt(int index) {
		return this.oldValues.get(index);
	}

	/** Replies the value added to a node.
	 *
	 * @param index is the index of the value
	 * @return the new value at the given index.
	 */
	@Pure
	public Object getAddedValueAt(int index) {
		return this.newValues.get(index);
	}

	/** Replies the value associated to a node.
	 *
	 * @param index is the index of the value
	 * @return the value at the given index.
	 */
	@Pure
	public Object getCurrentValueAt(int index) {
		return this.allValues.get(index);
	}

	/** Replies the amount of data that cause this
	 * event.
	 *
	 * <p>The amount may be positive is the global size of
	 * the data list has increased. It may be negative
	 * is the global size of the data list has decreased.
	 * The value of the delta corresponds to the difference
	 * between the size before change and size after
	 * change.
	 *
	 * @return amount of data change.
	 */
	@Pure
	public int getDelta() {
		return this.delta;
	}

	/** Replies the list of added data.
	 *
	 * @return the list of added data.
	 */
	@Pure
	public List<Object> getAddedValues() {
		if (this.newValues == null) {
			return Collections.emptyList();
		}
		return Collections.unmodifiableList(this.newValues);
	}

	/** Replies the list of removed data.
	 *
	 * @return the list of removed data.
	 */
	@Pure
	public List<Object> getRemovedValues() {
		if (this.oldValues == null) {
			return Collections.emptyList();
		}
		return Collections.unmodifiableList(this.oldValues);
	}

	/** Replies the list of current data.
	 *
	 * @return the list of current data.
	 */
	@Pure
	public List<Object> getCurrentValues() {
		if (this.allValues == null) {
			return Collections.emptyList();
		}
		return Collections.unmodifiableList(this.allValues);
	}

}
