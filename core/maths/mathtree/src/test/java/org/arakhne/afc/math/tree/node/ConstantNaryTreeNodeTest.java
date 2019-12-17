/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2019 The original authors, and other authors.
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.arakhne.afc.math.tree.node.ConstantNaryTreeNode.DefaultConstantNaryTreeNode;
import org.arakhne.afc.vmutil.json.JsonBuffer;


/**
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@SuppressWarnings("all")
public class ConstantNaryTreeNodeTest {

	private final TreeNodeListenerStub<DefaultConstantNaryTreeNode<Object>> listener =
		new TreeNodeListenerStub<>();
	
	private DefaultConstantNaryTreeNode<Object> root;
	private DefaultConstantNaryTreeNode<Object> child1;
	private DefaultConstantNaryTreeNode<Object> child2;
	private DefaultConstantNaryTreeNode<Object> node;
	private DefaultConstantNaryTreeNode<Object> newNode;
	
	/**
	 * @throws Exception
	 */
	@BeforeEach
	public void setUp() throws Exception {
		this.root = new NodeStub("root");  //$NON-NLS-1$
		this.child1 = new NodeStub("child1");  //$NON-NLS-1$
		this.child2 = new NodeStub("child2");  //$NON-NLS-1$
		this.node = new NodeStub("node");  //$NON-NLS-1$
		this.newNode = new NodeStub("newNode");  //$NON-NLS-1$
		
		this.root.setChildAt(0, this.child1);
		this.root.setChildAt(2, this.child2);
		this.child1.setChildAt(4, this.node);
	
		this.listener.reset();
		this.root.addTreeNodeListener(this.listener);
	}
	
	/**
	 * @throws Exception
	 */
	@AfterEach
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
		assertEquals(5, this.root.getChildCount());
		assertEquals(5, this.child1.getChildCount());
		assertEquals(5, this.child2.getChildCount());
		assertEquals(5, this.node.getChildCount());
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
	private class NodeStub extends DefaultConstantNaryTreeNode<Object> {

		private static final long serialVersionUID = -1123134017423112775L;
		
		private final String name;
		
		/**
		 * @param name1
		 */
		public NodeStub(String name1) {
			super(5);
			this.name = name1;
		}
		
		@Override
		public void toJson(JsonBuffer buffer) {
			super.toJson(buffer);
			buffer.add("name", this.name); //$NON-NLS-1$
		}
		
	}
	
}