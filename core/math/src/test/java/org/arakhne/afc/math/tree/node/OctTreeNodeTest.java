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
package org.arakhne.afc.math.tree.node;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.arakhne.afc.math.tree.node.OctTreeNode.DefaultOctTreeNode;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


/**
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class OctTreeNodeTest {

	private final TreeNodeListenerStub<DefaultOctTreeNode<Object>> listener =
		new TreeNodeListenerStub<>();
	
	private DefaultOctTreeNode<Object> root;
	private DefaultOctTreeNode<Object> child1;
	private DefaultOctTreeNode<Object> child2;
	private DefaultOctTreeNode<Object> node;
	private DefaultOctTreeNode<Object> newNode;
	
	/**
	 * 
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
		this.root = new NodeStub("root"); //$NON-NLS-1$
		this.child1 = new NodeStub("child1"); //$NON-NLS-1$
		this.child2 = new NodeStub("child2"); //$NON-NLS-1$
		this.node = new NodeStub("node"); //$NON-NLS-1$
		this.newNode = new NodeStub("newNode"); //$NON-NLS-1$
		
		this.root.setChildAt(0, this.child1);
		this.root.setChildAt(2, this.child2);
		this.child1.setChildAt(4, this.node);
	
		this.listener.reset();
		this.root.addTreeNodeListener(this.listener);
	}
	
	/**
	 * 
	 * @throws Exception
	 */
	@After
	public void tearDown() throws Exception {
		this.root.removeTreeNodeListener(this.listener);
		this.root.clear();
		this.root = this.child1 = this.child2 = this.node = this.newNode = null;
		this.listener.reset();
	}

	/**
	 */
	@Test
	public void getChildCount() {
		assertEquals(8, this.root.getChildCount());
		assertEquals(8, this.child1.getChildCount());
		assertEquals(8, this.child2.getChildCount());
		assertEquals(8, this.node.getChildCount());
	}

	/**
	 */
	@Test
	public void getNotNullChildCount() {
		assertEquals(2, this.root.getNotNullChildCount());
		assertEquals(1, this.child1.getNotNullChildCount());
		assertEquals(0, this.child2.getNotNullChildCount());
		assertEquals(0, this.node.getNotNullChildCount());
	}

	/**
	 */
	@Test
	public void setChildAt_0_newNode() {
		runSetChildAtTest_newNode(0);
	}
	
	/**
	 */
	@Test
	public void setChildAt_1_newNode() {
		runSetChildAtTest_newNode(1);
	}

	/**
	 */
	@Test
	public void setChildAt_2_newNode() {
		runSetChildAtTest_newNode(2);
	}

	/**
	 */
	@Test
	public void setChildAt_3_newNode() {
		runSetChildAtTest_newNode(3);
	}

	/**
	 */
	@Test
	public void setChildAt_4_newNode() {
		runSetChildAtTest_newNode(4);
	}

	/**
	 */
	@Test
	public void setChildAt_5_newNode() {
		runSetChildAtTest_newNode(5);
	}

	/**
	 */
	@Test
	public void setChildAt_6_newNode() {
		runSetChildAtTest_newNode(6);
	}

	/**
	 */
	@Test
	public void setChildAt_7_newNode() {
		runSetChildAtTest_newNode(7);
	}

	private void runSetChildAtTest_newNode(int index) {
		this.node.setChildAt(index, this.newNode);
		
		assertSame(this.newNode, this.node.getChildAt(index));
		assertSame(this.node, this.newNode.getParentNode());
		
		assertEquals(1, this.listener.additionEvent.size());
		assertEquals(0, this.listener.removalEvent.size());
		assertEquals(0, this.listener.dataEvent.size());
		assertEquals(1, this.listener.parentEvent.size());
		
		assertSame(this.node, this.listener.additionEvent.get(0).getSource());
		assertSame(this.newNode, this.listener.additionEvent.get(0).getChild());
		assertSame(this.node, this.listener.additionEvent.get(0).getParentNode());
		assertSame(index, this.listener.additionEvent.get(0).getChildIndex());

		assertSame(this.newNode, this.listener.parentEvent.get(0).getSource());
		assertSame(this.newNode, this.listener.parentEvent.get(0).getChildNode());
		assertNull(this.listener.parentEvent.get(0).getOldParent());
		assertSame(this.node, this.listener.parentEvent.get(0).getNewParent());
	}
	
	/**
	 */
	@Test
	public void setChildAt_0_moveNode() {
		runSetChildAtTest_moveNode(0);
	}

	/**
	 */
	@Test
	public void setChildAt_1_moveNode() {
		runSetChildAtTest_moveNode(1);
	}

	/**
	 */
	@Test
	public void setChildAt_2_moveNode() {
		runSetChildAtTest_moveNode(2);
	}

	/**
	 */
	@Test
	public void setChildAt_3_moveNode() {
		runSetChildAtTest_moveNode(3);
	}

	/**
	 */
	@Test
	public void setChildAt_4_moveNode() {
		runSetChildAtTest_moveNode(4);
	}

	/**
	 */
	@Test
	public void setChildAt_5_moveNode() {
		runSetChildAtTest_moveNode(5);
	}

	/**
	 */
	@Test
	public void setChildAt_6_moveNode() {
		runSetChildAtTest_moveNode(6);
	}

	/**
	 */
	@Test
	public void setChildAt_7_moveNode() {
		runSetChildAtTest_moveNode(7);
	}

	private void runSetChildAtTest_moveNode(int index) {
		this.node.setChildAt(index, this.child2);
		
		assertSame(this.child2, this.node.getChildAt(index));
		assertSame(this.node, this.child2.getParentNode());
		
		assertEquals(1, this.listener.additionEvent.size());
		assertEquals(1, this.listener.removalEvent.size());
		assertEquals(0, this.listener.dataEvent.size());
		assertEquals(2, this.listener.parentEvent.size());
		
		assertSame(this.node, this.listener.additionEvent.get(0).getSource());
		assertSame(this.child2, this.listener.additionEvent.get(0).getChild());
		assertSame(this.node, this.listener.additionEvent.get(0).getParentNode());
		assertSame(index, this.listener.additionEvent.get(0).getChildIndex());

		assertSame(this.child2, this.listener.parentEvent.get(0).getSource());
		assertSame(this.child2, this.listener.parentEvent.get(0).getChildNode());
		assertSame(this.root, this.listener.parentEvent.get(0).getOldParent());
		assertNull(this.listener.parentEvent.get(0).getNewParent());

		assertSame(this.child2, this.listener.parentEvent.get(1).getSource());
		assertSame(this.child2, this.listener.parentEvent.get(1).getChildNode());
		assertNull(this.listener.parentEvent.get(1).getOldParent());
		assertSame(this.node, this.listener.parentEvent.get(1).getNewParent());

		assertSame(this.root, this.listener.removalEvent.get(0).getSource());
		assertSame(this.child2, this.listener.removalEvent.get(0).getChild());
		assertSame(this.root, this.listener.removalEvent.get(0).getParentNode());
		assertSame(2, this.listener.removalEvent.get(0).getChildIndex());
	}

	/**
	 */
	@Test
	public void moveToNodeInt() {
		assertTrue(this.child2.moveTo(this.node, 3));
		
		assertSame(this.child2, this.node.getChildAt(3));
		assertSame(this.node, this.child2.getParentNode());
		
		assertEquals(1, this.listener.additionEvent.size());
		assertEquals(1, this.listener.removalEvent.size());
		assertEquals(0, this.listener.dataEvent.size());
		assertEquals(1, this.listener.parentEvent.size());
		
		assertSame(this.node, this.listener.additionEvent.get(0).getSource());
		assertSame(this.child2, this.listener.additionEvent.get(0).getChild());
		assertSame(this.node, this.listener.additionEvent.get(0).getParentNode());
		assertSame(3, this.listener.additionEvent.get(0).getChildIndex());

		assertSame(this.child2, this.listener.parentEvent.get(0).getSource());
		assertSame(this.child2, this.listener.parentEvent.get(0).getChildNode());
		assertSame(this.root, this.listener.parentEvent.get(0).getOldParent());
		assertSame(this.node, this.listener.parentEvent.get(0).getNewParent());

		assertSame(this.root, this.listener.removalEvent.get(0).getSource());
		assertSame(this.child2, this.listener.removalEvent.get(0).getChild());
		assertSame(this.root, this.listener.removalEvent.get(0).getParentNode());
		assertSame(2, this.listener.removalEvent.get(0).getChildIndex());
	}

	/**
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	private class NodeStub extends DefaultOctTreeNode<Object> {

		private static final long serialVersionUID = -1123134017423112775L;
		
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