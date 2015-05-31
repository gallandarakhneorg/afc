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

import org.arakhne.afc.math.tree.LinkedTree;
import org.arakhne.afc.math.tree.node.BinaryTreeNode.BinaryTreeZone;
import org.arakhne.afc.math.tree.node.BinaryTreeNode.DefaultBinaryTreeNode;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * <code><pre>
 * root
 * + child1
 * | + child11 
 * | \ child12
 * |   + child121
 * |   | + child1211
 * |   | \ child1212
 * |   |   + child12121
 * |   |   \ child12122
 * |   \ child122 
 * \ child2
 *   + child21 
 *   | + child211
 *   | \ null 
 *   \ child22
 *     + null 
 *     \ child222
 * </pre></code>
 * 
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public abstract class AbstractTreeIteratorTest {

	/**
	 */
	protected DefaultBinaryTreeNode<Object> root;
	/**
	 */
	protected DefaultBinaryTreeNode<Object> child1;
	/**
	 */
	protected DefaultBinaryTreeNode<Object> child11;
	/**
	 */
	protected DefaultBinaryTreeNode<Object> child12;
	/**
	 */
	protected DefaultBinaryTreeNode<Object> child121;
	/**
	 */
	protected DefaultBinaryTreeNode<Object> child1211;
	/**
	 */
	protected DefaultBinaryTreeNode<Object> child1212;
	/**
	 */
	protected DefaultBinaryTreeNode<Object> child12121;
	/**
	 */
	protected DefaultBinaryTreeNode<Object> child12122;
	/**
	 */
	protected DefaultBinaryTreeNode<Object> child122;
	/**
	 */
	protected DefaultBinaryTreeNode<Object> child2;
	/**
	 */
	protected DefaultBinaryTreeNode<Object> child21;
	/**
	 */
	protected DefaultBinaryTreeNode<Object> child211;
	/**
	 */
	protected DefaultBinaryTreeNode<Object> child22;
	/**
	 */
	protected DefaultBinaryTreeNode<Object> child222;
	/**
	 */
	protected LinkedTree<Object,DefaultBinaryTreeNode<Object>> tree;
	
	/**
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
		this.root = new NodeStub("root"); //$NON-NLS-1$
		this.child1 = new NodeStub("child1"); //$NON-NLS-1$
		this.child11 = new NodeStub("child11"); //$NON-NLS-1$;
		this.child12 = new NodeStub("child12"); //$NON-NLS-1$;
		this.child121 = new NodeStub("child121"); //$NON-NLS-1$;
		this.child1211 = new NodeStub("child1211"); //$NON-NLS-1$;
		this.child1212 = new NodeStub("child1212"); //$NON-NLS-1$;
		this.child12121 = new NodeStub("child12121"); //$NON-NLS-1$;
		this.child12122 = new NodeStub("child12122"); //$NON-NLS-1$;
		this.child122 = new NodeStub("child122"); //$NON-NLS-1$;
		this.child2 = new NodeStub("child2"); //$NON-NLS-1$
		this.child21 = new NodeStub("child21"); //$NON-NLS-1$;
		this.child211 = new NodeStub("child211"); //$NON-NLS-1$;
		this.child22 = new NodeStub("child22"); //$NON-NLS-1$;
		this.child222 = new NodeStub("child222"); //$NON-NLS-1$;
		
		this.root.setChildAt(BinaryTreeZone.LEFT,			this.child1);
		this.root.setChildAt(BinaryTreeZone.RIGHT,			this.child2);
		this.child1.setChildAt(BinaryTreeZone.LEFT,			this.child11);
		this.child1.setChildAt(BinaryTreeZone.RIGHT,		this.child12);
		this.child12.setChildAt(BinaryTreeZone.LEFT,		this.child121);
		this.child12.setChildAt(BinaryTreeZone.RIGHT,		this.child122);
		this.child121.setChildAt(BinaryTreeZone.LEFT,		this.child1211);
		this.child121.setChildAt(BinaryTreeZone.RIGHT,		this.child1212);
		this.child1212.setChildAt(BinaryTreeZone.LEFT,		this.child12121);
		this.child1212.setChildAt(BinaryTreeZone.RIGHT,		this.child12122);
		this.child2.setChildAt(BinaryTreeZone.LEFT,			this.child21);
		this.child2.setChildAt(BinaryTreeZone.RIGHT,		this.child22);
		this.child21.setChildAt(BinaryTreeZone.LEFT,		this.child211);
		this.child22.setChildAt(BinaryTreeZone.RIGHT,		this.child222);
		
		this.tree = new LinkedTree<>(this.root);
	}
	
	/**
	 * @throws Exception
	 */
	@After
	public void tearDown() throws Exception {
		this.root = null;
		this.child1 = null;
		this.child11 = null;
		this.child12 = null;
		this.child121 = null;
		this.child1211 = null;
		this.child1212 = null;
		this.child12121 = null;
		this.child12122 = null;
		this.child122 = null;
		this.child2 = null;
		this.child21 = null;
		this.child211 = null;
		this.child22 = null;
		this.child222 = null;
		this.tree = null;
	}
	
	/**
	 */
	@Test
	public abstract void iterate();

	/**
	 */
	@Test
	public abstract void remove();

	/**
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	private class NodeStub extends DefaultBinaryTreeNode<Object> {

		private static final long serialVersionUID = 925867490989981080L;
		
		private final String name;
		
		/**
		 * @param name
		 */
		public NodeStub(String name) {
			this.name = name;
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public String toString() {
			return this.name;
		}
		
	}

}