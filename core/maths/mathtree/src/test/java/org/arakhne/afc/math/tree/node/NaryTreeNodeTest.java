/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2023 The original authors and other contributors.
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
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.arakhne.afc.math.tree.node.NaryTreeNode.DefaultNaryTreeNode;
import org.arakhne.afc.vmutil.json.JsonBuffer;


/**
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@SuppressWarnings("all")
public class NaryTreeNodeTest {

	private final TreeNodeListenerStub<DefaultNaryTreeNode<Object>> listener =
		new TreeNodeListenerStub<>();
	
	private DefaultNaryTreeNode<Object> root;
	private DefaultNaryTreeNode<Object> child1;
	private DefaultNaryTreeNode<Object> child2;
	private DefaultNaryTreeNode<Object> node;
	private DefaultNaryTreeNode<Object> newNode;
	
	/**
	 * 
	 * @throws Exception
	 */
	@BeforeEach
	public void setUp() throws Exception {
		this.root = new NodeStub("root");  //$NON-NLS-1$
		this.child1 = new NodeStub("child1");  //$NON-NLS-1$
		this.child2 = new NodeStub("child2");  //$NON-NLS-1$
		this.node = new NodeStub("node");  //$NON-NLS-1$
		this.newNode = new NodeStub("newNode");  //$NON-NLS-1$
		
		this.root.addChild(this.child1);
		this.root.addChild(this.child2);
		this.child1.addChild(this.node);
	
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
		assertEquals(2, this.root.getChildCount());
		assertEquals(1, this.child1.getChildCount());
		assertEquals(0, this.child2.getChildCount());
		assertEquals(0, this.node.getChildCount());
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
	public void addChild_newNode_m1() {
		this.node.addChild(-1, this.newNode);
		
		assertSame(this.newNode, this.node.getChildAt(0));
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
	public void addChild_newNode_0() {
		this.node.addChild(0, this.newNode);
		
		assertSame(this.newNode, this.node.getChildAt(0));
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
	public void addChild_newNode_1() {
		this.node.addChild(1, this.newNode);
		
		assertSame(this.newNode, this.node.getChildAt(0));
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
	public void addChild_newNode_2() {
		this.node.addChild(2, this.newNode);
		
		assertSame(this.newNode, this.node.getChildAt(0));
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
	public void addChild_moveNode_m1() {
		this.node.addChild(-1, this.child2);
		
		assertSame(this.child2, this.node.getChildAt(0));
		assertSame(this.node, this.child2.getParentNode());
		
		assertEquals(1, this.listener.additionEvent.size());
		assertEquals(1, this.listener.removalEvent.size());
		assertEquals(0, this.listener.dataEvent.size());
		assertEquals(2, this.listener.parentEvent.size());
		
		assertSame(this.node, this.listener.additionEvent.get(0).getSource());
		assertSame(this.child2, this.listener.additionEvent.get(0).getChild());
		assertSame(this.node, this.listener.additionEvent.get(0).getParentNode());
		assertSame(0, this.listener.additionEvent.get(0).getChildIndex());

		assertSame(this.root, this.listener.removalEvent.get(0).getSource());
		assertSame(this.child2, this.listener.removalEvent.get(0).getChild());
		assertSame(this.root, this.listener.removalEvent.get(0).getParentNode());
		assertSame(1, this.listener.removalEvent.get(0).getChildIndex());

		assertSame(this.child2, this.listener.parentEvent.get(0).getSource());
		assertSame(this.child2, this.listener.parentEvent.get(0).getChildNode());
		assertSame(this.root, this.listener.parentEvent.get(0).getOldParent());
		assertNull(this.listener.parentEvent.get(0).getNewParent());

		assertSame(this.child2, this.listener.parentEvent.get(1).getSource());
		assertSame(this.child2, this.listener.parentEvent.get(1).getChildNode());
		assertNull(this.listener.parentEvent.get(1).getOldParent());
		assertSame(this.node, this.listener.parentEvent.get(1).getNewParent());
	}

	/**
	 */
	@Test
	public void addChild_moveNode_0() {
		this.node.addChild(0, this.child2);
		
		assertSame(this.child2, this.node.getChildAt(0));
		assertSame(this.node, this.child2.getParentNode());
		
		assertEquals(1, this.listener.additionEvent.size());
		assertEquals(1, this.listener.removalEvent.size());
		assertEquals(0, this.listener.dataEvent.size());
		assertEquals(2, this.listener.parentEvent.size());
		
		assertSame(this.node, this.listener.additionEvent.get(0).getSource());
		assertSame(this.child2, this.listener.additionEvent.get(0).getChild());
		assertSame(this.node, this.listener.additionEvent.get(0).getParentNode());
		assertSame(0, this.listener.additionEvent.get(0).getChildIndex());

		assertSame(this.root, this.listener.removalEvent.get(0).getSource());
		assertSame(this.child2, this.listener.removalEvent.get(0).getChild());
		assertSame(this.root, this.listener.removalEvent.get(0).getParentNode());
		assertSame(1, this.listener.removalEvent.get(0).getChildIndex());

		assertSame(this.child2, this.listener.parentEvent.get(0).getSource());
		assertSame(this.child2, this.listener.parentEvent.get(0).getChildNode());
		assertSame(this.root, this.listener.parentEvent.get(0).getOldParent());
		assertNull(this.listener.parentEvent.get(0).getNewParent());

		assertSame(this.child2, this.listener.parentEvent.get(1).getSource());
		assertSame(this.child2, this.listener.parentEvent.get(1).getChildNode());
		assertNull(this.listener.parentEvent.get(1).getOldParent());
		assertSame(this.node, this.listener.parentEvent.get(1).getNewParent());
	}

	/**
	 */
	@Test
	public void addChild_moveNode_1() {
		this.node.addChild(1, this.child2);
		
		assertSame(this.child2, this.node.getChildAt(0));
		assertSame(this.node, this.child2.getParentNode());
		
		assertEquals(1, this.listener.additionEvent.size());
		assertEquals(1, this.listener.removalEvent.size());
		assertEquals(0, this.listener.dataEvent.size());
		assertEquals(2, this.listener.parentEvent.size());
		
		assertSame(this.node, this.listener.additionEvent.get(0).getSource());
		assertSame(this.child2, this.listener.additionEvent.get(0).getChild());
		assertSame(this.node, this.listener.additionEvent.get(0).getParentNode());
		assertSame(0, this.listener.additionEvent.get(0).getChildIndex());

		assertSame(this.root, this.listener.removalEvent.get(0).getSource());
		assertSame(this.child2, this.listener.removalEvent.get(0).getChild());
		assertSame(this.root, this.listener.removalEvent.get(0).getParentNode());
		assertSame(1, this.listener.removalEvent.get(0).getChildIndex());

		assertSame(this.child2, this.listener.parentEvent.get(0).getSource());
		assertSame(this.child2, this.listener.parentEvent.get(0).getChildNode());
		assertSame(this.root, this.listener.parentEvent.get(0).getOldParent());
		assertNull(this.listener.parentEvent.get(0).getNewParent());

		assertSame(this.child2, this.listener.parentEvent.get(1).getSource());
		assertSame(this.child2, this.listener.parentEvent.get(1).getChildNode());
		assertNull(this.listener.parentEvent.get(1).getOldParent());
		assertSame(this.node, this.listener.parentEvent.get(1).getNewParent());
	}

	/**
	 */
	@Test
	public void addChild_moveNode_2() {
		this.node.addChild(2, this.child2);
		
		assertSame(this.child2, this.node.getChildAt(0));
		assertSame(this.node, this.child2.getParentNode());
		
		assertEquals(1, this.listener.additionEvent.size());
		assertEquals(1, this.listener.removalEvent.size());
		assertEquals(0, this.listener.dataEvent.size());
		assertEquals(2, this.listener.parentEvent.size());
		
		assertSame(this.node, this.listener.additionEvent.get(0).getSource());
		assertSame(this.child2, this.listener.additionEvent.get(0).getChild());
		assertSame(this.node, this.listener.additionEvent.get(0).getParentNode());
		assertSame(0, this.listener.additionEvent.get(0).getChildIndex());

		assertSame(this.root, this.listener.removalEvent.get(0).getSource());
		assertSame(this.child2, this.listener.removalEvent.get(0).getChild());
		assertSame(this.root, this.listener.removalEvent.get(0).getParentNode());
		assertSame(1, this.listener.removalEvent.get(0).getChildIndex());

		assertSame(this.child2, this.listener.parentEvent.get(0).getSource());
		assertSame(this.child2, this.listener.parentEvent.get(0).getChildNode());
		assertSame(this.root, this.listener.parentEvent.get(0).getOldParent());
		assertNull(this.listener.parentEvent.get(0).getNewParent());

		assertSame(this.child2, this.listener.parentEvent.get(1).getSource());
		assertSame(this.child2, this.listener.parentEvent.get(1).getChildNode());
		assertNull(this.listener.parentEvent.get(1).getOldParent());
		assertSame(this.node, this.listener.parentEvent.get(1).getNewParent());
	}

	/**
	 */
	@Test
	public void setChildAt_newNode_m1() {
		try {
			this.node.setChildAt(-1, this.newNode);
			fail("expecting IndexOutOfBoundsException");  //$NON-NLS-1$
		}
		catch( IndexOutOfBoundsException e) {
			//
		}
	}

	/**
	 */
	@Test
	public void setChildAt_newNode_0() {
		try {
			this.node.setChildAt(0, this.newNode);
			fail("expecting IndexOutOfBoundsException");  //$NON-NLS-1$
		}
		catch(IndexOutOfBoundsException e) {
			//
		}
	}
	
	/**
	 */
	@Test
	public void setChildAt_newNode_1() {
		try {
			this.node.setChildAt(1, this.newNode);
			fail("expecting IndexOutOfBoundsException");  //$NON-NLS-1$
		}
		catch(IndexOutOfBoundsException e) {
			//
		}
	}

	/**
	 */
	@Test
	public void setChildAt_newNode_2() {
		try {
			this.node.setChildAt(2, this.newNode);
			fail("expecting IndexOutOfBoundsException");  //$NON-NLS-1$
		}
		catch(IndexOutOfBoundsException e) {
			//
		}
	}

	/**
	 */
	@Test
	public void setChildAt_moveNode_m1() {
		try {
			this.node.setChildAt(-1, this.child2);
			fail("expecting IndexOutOfBoundsException");  //$NON-NLS-1$
		}
		catch(IndexOutOfBoundsException e) {
			//
		}
	}

	/**
	 */
	@Test
	public void setChildAt_moveNode_0() {
		try {
			this.node.setChildAt(0, this.child2);
			fail("expecting IndexOutOfBoundsException");  //$NON-NLS-1$
		}
		catch(IndexOutOfBoundsException e) {
			//
		}
	}

	/**
	 */
	@Test
	public void setChildAt_moveNode_1() {
		try {
			this.node.setChildAt(1, this.child2);
			fail("expecting IndexOutOfBoundsException");  //$NON-NLS-1$
		}
		catch(IndexOutOfBoundsException e) {
			//
		}
	}

	/**
	 */
	@Test
	public void setChildAt_moveNode_2() {
		try {
			this.node.setChildAt(2, this.child2);
			fail("expecting IndexOutOfBoundsException");  //$NON-NLS-1$
		}
		catch(IndexOutOfBoundsException e) {
			//
		}
	}

	/**
	 */
	@Test
	public void moveToNodeInt_m1() {
		assertTrue(this.child2.moveTo(this.node, -1));

		assertSame(this.child2, this.node.getChildAt(0));
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
		assertSame(1, this.listener.removalEvent.get(0).getChildIndex());
	}

	/**
	 */
	@Test
	public void moveToNodeInt_0() {
		assertTrue(this.child2.moveTo(this.node, 0));
		
		assertSame(this.child2, this.node.getChildAt(0));
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
		assertSame(1, this.listener.removalEvent.get(0).getChildIndex());
	}

	/**
	 */
	@Test
	public void moveToNodeInt_1() {
		assertTrue(this.child2.moveTo(this.node, 1));
		
		assertSame(this.child2, this.node.getChildAt(0));
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
		assertSame(1, this.listener.removalEvent.get(0).getChildIndex());
	}

	/**
	 */
	@Test
	public void moveToNodeInt_2() {
		assertTrue(this.child2.moveTo(this.node, 2));
		
		assertSame(this.child2, this.node.getChildAt(0));
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
		assertSame(1, this.listener.removalEvent.get(0).getChildIndex());
	}

	/**
	 * @author $Author: sgalland$
	 * @version $FullVersion$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	private class NodeStub extends DefaultNaryTreeNode<Object> {

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