/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2018 The original authors, and other authors.
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.arakhne.afc.math.tree.node.QuadTreeNode.DefaultQuadTreeNode;
import org.arakhne.afc.vmutil.json.JsonBuffer;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


/**
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@SuppressWarnings("all")
public class QuadTreeNodeTest {

	private final TreeNodeListenerStub<DefaultQuadTreeNode<Object>> listener =
		new TreeNodeListenerStub<>();
	
	private DefaultQuadTreeNode<Object> root;
	private DefaultQuadTreeNode<Object> child1;
	private DefaultQuadTreeNode<Object> child2;
	private DefaultQuadTreeNode<Object> node;
	private DefaultQuadTreeNode<Object> newNode;
	
	/**
	 * 
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
		this.root = new NodeStub("root");  //$NON-NLS-1$
		this.child1 = new NodeStub("child1");  //$NON-NLS-1$
		this.child2 = new NodeStub("child2");  //$NON-NLS-1$
		this.node = new NodeStub("node");  //$NON-NLS-1$
		this.newNode = new NodeStub("newNode");  //$NON-NLS-1$
		
		this.root.setFirstChild(this.child1);
		this.root.setThirdChild(this.child2);
		this.child1.setFourthChild(this.node);
	
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
		assertEquals(4, this.root.getChildCount());
		assertEquals(4, this.child1.getChildCount());
		assertEquals(4, this.child2.getChildCount());
		assertEquals(4, this.node.getChildCount());
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
	public void setFirstChild_newNode() {
		this.node.setFirstChild(this.newNode);
		
		assertSame(this.newNode, this.node.getFirstChild());
		assertSame(this.node, this.newNode.getParentNode());
		
		assertEquals(1, this.listener.additionEvent.size());
		assertEquals(0, this.listener.removalEvent.size());
		assertEquals(0, this.listener.dataEvent.size());
		assertEquals(1, this.listener.parentEvent.size());
		
		assertSame(this.node, this.listener.additionEvent.get(0).getSource());
		assertSame(this.newNode, this.listener.additionEvent.get(0).getChild());
		assertSame(this.node, this.listener.additionEvent.get(0).getParentNode());
		assertSame(0, this.listener.additionEvent.get(0).getChildIndex());

		assertSame(this.newNode, this.listener.parentEvent.get(0).getSource());
		assertSame(this.newNode, this.listener.parentEvent.get(0).getChildNode());
		assertNull(this.listener.parentEvent.get(0).getOldParent());
		assertSame(this.node, this.listener.parentEvent.get(0).getNewParent());
	}
	
	/**
	 */
	@Test
	public void setFirstChild_moveNode() {
		this.node.setFirstChild(this.child2);
		
		assertSame(this.child2, this.node.getFirstChild());
		assertSame(this.node, this.child2.getParentNode());
		
		assertEquals(1, this.listener.additionEvent.size());
		assertEquals(1, this.listener.removalEvent.size());
		assertEquals(0, this.listener.dataEvent.size());
		assertEquals(2, this.listener.parentEvent.size());
		
		assertSame(this.node, this.listener.additionEvent.get(0).getSource());
		assertSame(this.child2, this.listener.additionEvent.get(0).getChild());
		assertSame(this.node, this.listener.additionEvent.get(0).getParentNode());
		assertSame(0, this.listener.additionEvent.get(0).getChildIndex());

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
	public void setSecondChild_newNode() {
		this.node.setSecondChild(this.newNode);
		
		assertSame(this.newNode, this.node.getSecondChild());
		assertSame(this.node, this.newNode.getParentNode());
		
		assertEquals(1, this.listener.additionEvent.size());
		assertEquals(0, this.listener.removalEvent.size());
		assertEquals(0, this.listener.dataEvent.size());
		assertEquals(1, this.listener.parentEvent.size());
		
		assertSame(this.node, this.listener.additionEvent.get(0).getSource());
		assertSame(this.newNode, this.listener.additionEvent.get(0).getChild());
		assertSame(this.node, this.listener.additionEvent.get(0).getParentNode());
		assertSame(1, this.listener.additionEvent.get(0).getChildIndex());

		assertSame(this.newNode, this.listener.parentEvent.get(0).getSource());
		assertSame(this.newNode, this.listener.parentEvent.get(0).getChildNode());
		assertNull(this.listener.parentEvent.get(0).getOldParent());
		assertSame(this.node, this.listener.parentEvent.get(0).getNewParent());
	}
	
	/**
	 */
	@Test
	public void setSecondChild_moveNode() {
		this.node.setSecondChild(this.child2);
		
		assertSame(this.child2, this.node.getSecondChild());
		assertSame(this.node, this.child2.getParentNode());
		
		assertEquals(1, this.listener.additionEvent.size());
		assertEquals(1, this.listener.removalEvent.size());
		assertEquals(0, this.listener.dataEvent.size());
		assertEquals(2, this.listener.parentEvent.size());
		
		assertSame(this.node, this.listener.additionEvent.get(0).getSource());
		assertSame(this.child2, this.listener.additionEvent.get(0).getChild());
		assertSame(this.node, this.listener.additionEvent.get(0).getParentNode());
		assertSame(1, this.listener.additionEvent.get(0).getChildIndex());

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
	public void setThirdChild_newNode() {
		this.node.setThirdChild(this.newNode);
		
		assertSame(this.newNode, this.node.getThirdChild());
		assertSame(this.node, this.newNode.getParentNode());
		
		assertEquals(1, this.listener.additionEvent.size());
		assertEquals(0, this.listener.removalEvent.size());
		assertEquals(0, this.listener.dataEvent.size());
		assertEquals(1, this.listener.parentEvent.size());
		
		assertSame(this.node, this.listener.additionEvent.get(0).getSource());
		assertSame(this.newNode, this.listener.additionEvent.get(0).getChild());
		assertSame(this.node, this.listener.additionEvent.get(0).getParentNode());
		assertSame(2, this.listener.additionEvent.get(0).getChildIndex());

		assertSame(this.newNode, this.listener.parentEvent.get(0).getSource());
		assertSame(this.newNode, this.listener.parentEvent.get(0).getChildNode());
		assertNull(this.listener.parentEvent.get(0).getOldParent());
		assertSame(this.node, this.listener.parentEvent.get(0).getNewParent());
	}
	
	/**
	 */
	@Test
	public void setThirdChild_moveNode() {
		this.node.setThirdChild(this.child2);
		
		assertSame(this.child2, this.node.getThirdChild());
		assertSame(this.node, this.child2.getParentNode());
		
		assertEquals(1, this.listener.additionEvent.size());
		assertEquals(1, this.listener.removalEvent.size());
		assertEquals(0, this.listener.dataEvent.size());
		assertEquals(2, this.listener.parentEvent.size());
		
		assertSame(this.node, this.listener.additionEvent.get(0).getSource());
		assertSame(this.child2, this.listener.additionEvent.get(0).getChild());
		assertSame(this.node, this.listener.additionEvent.get(0).getParentNode());
		assertSame(2, this.listener.additionEvent.get(0).getChildIndex());

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
	public void setFourthChild_newNode() {
		this.node.setFourthChild(this.newNode);
		
		assertSame(this.newNode, this.node.getFourthChild());
		assertSame(this.node, this.newNode.getParentNode());
		
		assertEquals(1, this.listener.additionEvent.size());
		assertEquals(0, this.listener.removalEvent.size());
		assertEquals(0, this.listener.dataEvent.size());
		assertEquals(1, this.listener.parentEvent.size());
		
		assertSame(this.node, this.listener.additionEvent.get(0).getSource());
		assertSame(this.newNode, this.listener.additionEvent.get(0).getChild());
		assertSame(this.node, this.listener.additionEvent.get(0).getParentNode());
		assertSame(3, this.listener.additionEvent.get(0).getChildIndex());

		assertSame(this.newNode, this.listener.parentEvent.get(0).getSource());
		assertSame(this.newNode, this.listener.parentEvent.get(0).getChildNode());
		assertNull(this.listener.parentEvent.get(0).getOldParent());
		assertSame(this.node, this.listener.parentEvent.get(0).getNewParent());
	}
	
	/**
	 */
	@Test
	public void setFourthChild_moveNode() {
		this.node.setFourthChild(this.child2);
		
		assertSame(this.child2, this.node.getFourthChild());
		assertSame(this.node, this.child2.getParentNode());
		
		assertEquals(1, this.listener.additionEvent.size());
		assertEquals(1, this.listener.removalEvent.size());
		assertEquals(0, this.listener.dataEvent.size());
		assertEquals(2, this.listener.parentEvent.size());
		
		assertSame(this.node, this.listener.additionEvent.get(0).getSource());
		assertSame(this.child2, this.listener.additionEvent.get(0).getChild());
		assertSame(this.node, this.listener.additionEvent.get(0).getParentNode());
		assertSame(3, this.listener.additionEvent.get(0).getChildIndex());

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
		assertTrue(this.child2.moveTo(this.node, 0));
		
		assertSame(this.child2, this.node.getFirstChild());
		assertSame(this.node, this.child2.getParentNode());
		
		assertEquals(1, this.listener.additionEvent.size());
		assertEquals(1, this.listener.removalEvent.size());
		assertEquals(0, this.listener.dataEvent.size());
		assertEquals(1, this.listener.parentEvent.size());
		
		assertSame(this.node, this.listener.additionEvent.get(0).getSource());
		assertSame(this.child2, this.listener.additionEvent.get(0).getChild());
		assertSame(this.node, this.listener.additionEvent.get(0).getParentNode());
		assertSame(0, this.listener.additionEvent.get(0).getChildIndex());

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
	private class NodeStub extends DefaultQuadTreeNode<Object> {

		private static final long serialVersionUID = -1123134017423112775L;
		
		private final String name;
		
		/**
		 * @param name1
		 */
		public NodeStub(String name1) {
			this.name = name1;
		}
		
		@Override
		public void toJson(JsonBuffer buffer) {
			super.toJson(buffer);
			buffer.add("name", this.name); //$NON-NLS-1$
		}
		
	}
	
}