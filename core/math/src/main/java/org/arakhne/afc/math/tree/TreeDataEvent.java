/* 
 * $Id$
 * 
 * Copyright (c) 2005-10, Multiagent Team,
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Event on tree nodes.
 * 
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class TreeDataEvent {
	
	private final TreeNode<?,?> node;
	private final List<?> oldValues;
	private final List<?> newValues;
	private final List<?> allValues;
	private final int delta;

	/**
	 * @param node1 is the node on which the event that occurs
	 * @param oldValues1 is the list of old user data associated to the node
	 * @param newValues1 is the list of new user data associated to the node
	 * @param allValues1 is the list of all user data currently associated to the node
	 */
	public TreeDataEvent(TreeNode<?,?> node1, Collection<?> oldValues1, Collection<?> newValues1, Collection<?> allValues1) {
		int aCount = 0;
		int rCount = 0;
		this.node = node1;
		if (oldValues1==null)
			this.oldValues = null;
		else if (oldValues1 instanceof List<?>) {
			this.oldValues = (List<?>)oldValues1;
			rCount = this.oldValues.size();
		}
		else {
			this.oldValues = new ArrayList<>(oldValues1);
			rCount = this.oldValues.size();
		}
		if (newValues1==null)
			this.newValues = null;
		else if (newValues1 instanceof List<?>) {
			this.newValues = (List<?>)newValues1;
			aCount = this.newValues.size();
		}
		else {
			this.newValues = new ArrayList<>(newValues1);
			aCount = this.newValues.size();
		}
		if (allValues1==null)
			this.allValues = null;
		else if (allValues1 instanceof List<?>)
			this.allValues = (List<?>)allValues1;
		else
			this.allValues = new ArrayList<>(allValues1);
		this.delta = aCount - rCount;
	}
	
	/**
	 * @param node1 is the node on which the event that occurs
	 * @param delta1 is the difference between the size of the data list
	 * before change and size after change.
	 * @param allValues1 is the list of all user data currently associated to the node
	 */
	public TreeDataEvent(TreeNode<?,?> node1, int delta1, Collection<?> allValues1) {
		int aCount = 0;
		int rCount = 0;
		this.node = node1;
		this.oldValues = this.newValues = null;
		if (allValues1==null)
			this.allValues = null;
		else if (allValues1 instanceof List<?>)
			this.allValues = (List<?>)allValues1;
		else
			this.allValues = new ArrayList<>(allValues1);
		this.delta = aCount - rCount;
	}

	/** Replies the node on which the event occurs.
	 * 
	 * @return the node on which the event occurs.
	 */
	public TreeNode<?,?> getNode() {
		return this.node;
	}
	
	/** Replies the count of removed values.
	 * 
	 * @return the count of removed values.
	 * @since 4.0
	 */
	public int getRemovedValueCount() {
		return (this.oldValues==null) ? 0 : this.oldValues.size();
	}

	/** Replies the count of added values.
	 * 
	 * @return the count of added values.
	 * @since 4.0
	 */
	public int getAddedValueCount() {
		return (this.newValues==null) ? 0 : this.newValues.size();
	}

	/** Replies the count of all associated values.
	 * 
	 * @return the count of all associated values.
	 */
	public int getCurrentValueCount() {
		return (this.allValues==null) ? 0 : this.allValues.size();
	}

	/** Replies the value removed from a node.
	 * 
	 * @param index is the index of the value
	 * @return the old value at the given index.
	 * @since 4.0
	 */
	public Object getRemovedValueAt(int index) {
		return this.oldValues.get(index);
	}

	/** Replies the value added to a node.
	 * 
	 * @param index is the index of the value
	 * @return the new value at the given index.
	 * @since 4.0
	 */
	public Object getAddedValueAt(int index) {
		return this.newValues.get(index);
	}

	/** Replies the value associated to a node.
	 * 
	 * @param index is the index of the value
	 * @return the value at the given index.
	 */
	public Object getCurrentValueAt(int index) {
		return this.allValues.get(index);
	}

	/** Replies the amount of data that cause this
	 * event.
	 * <p>
	 * The amount may be positive is the global size of
	 * the data list has increased. It may be negative
	 * is the global size of the data list has decreased.
	 * The value of the delta corresponds to the difference
	 * between the size before change and size after
	 * change.
	 * 
	 * @return amount of data change.
	 */
	public int getDelta() {
		return this.delta;
	}
	
	/** Replies the list of added data.
	 * 
	 * @return the list of added data.
	 * @since 4.0
	 */
	public List<Object> getAddedValues() {
		if (this.newValues==null) return Collections.emptyList();
		return Collections.unmodifiableList(this.newValues);
	}

	/** Replies the list of removed data.
	 * 
	 * @return the list of removed data.
	 * @since 4.0
	 */
	public List<Object> getRemovedValues() {
		if (this.oldValues==null) return Collections.emptyList();
		return Collections.unmodifiableList(this.oldValues);
	}

	/** Replies the list of current data.
	 * 
	 * @return the list of current data.
	 * @since 4.0
	 */
	public List<Object> getCurrentValues() {
		if (this.allValues==null) return Collections.emptyList();
		return Collections.unmodifiableList(this.allValues);
	}

}