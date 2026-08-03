/*
 * $Id$
 * This file is a part of the Arakhne Foundation Classes, http://www.arakhne.org/afc
 *
 * Copyright (c) 2000-2012 Stephane GALLAND.
 * Copyright (c) 2005-10, Multiagent Team, Laboratoire Systemes et Transports,
 *                        Universite de Technologie de Belfort-Montbeliard.
 * Copyright (c) 2013-2026 The original authors and other contributors.
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

import org.arakhne.afc.math.tree.node.QuadTreeNode.DefaultQuadTreeNode;
import org.arakhne.afc.vmutil.json.JsonBuffer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;


/**
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@DisplayName("QuadTreeNode")
@SuppressWarnings("all")
public class QuadTreeNodeTest {

	private final TreeNodeListenerStub<DefaultQuadTreeNode<Object>> listener =
		new TreeNodeListenerStub<>();
	
	private DefaultQuadTreeNode<Object> root;
	private DefaultQuadTreeNode<Object> child1;
	private DefaultQuadTreeNode<Object> child2;
	private DefaultQuadTreeNode<Object> node;
	private DefaultQuadTreeNode<Object> newNode;
	
	@BeforeEach
	public void setUp() throws Exception {
		root = new NodeStub("root");  //$NON-NLS-1$
		child1 = new NodeStub("child1");  //$NON-NLS-1$
		child2 = new NodeStub("child2");  //$NON-NLS-1$
		node = new NodeStub("node");  //$NON-NLS-1$
		newNode = new NodeStub("newNode");  //$NON-NLS-1$
		
		root.setFirstChild(child1);
		root.setThirdChild(child2);
		child1.setFourthChild(node);
	
		listener.reset();
		root.addTreeNodeListener(listener);
	}
	
	@AfterEach
	public void tearDown() throws Exception {
		root.removeTreeNodeListener(listener);
		root.clear();
		root = child1 = child2 = node = newNode = null;
		listener.reset();
	}

	@DisplayName("getChildCount")
	@Nested
	public class GetChildCount {

		@DisplayName("#1")
		@Test
		public void getChildCount_1() {
			assertEquals(4, root.getChildCount());
		}

		@DisplayName("#2")
		@Test
		public void getChildCount_2() {
			assertEquals(4, child1.getChildCount());
		}

		@DisplayName("#3")
		@Test
		public void getChildCount_3() {
			assertEquals(4, child2.getChildCount());
		}

		@DisplayName("#4")
		@Test
		public void getChildCount_4() {
			assertEquals(4, node.getChildCount());
		}
	}

	@DisplayName("getNotNullChildCount")
	@Nested
	public class GetNotNullChildCount {

		@DisplayName("#1")
		@Test
		public void getNotNullChildCount_1() {
			assertEquals(2, root.getNotNullChildCount());
		}

		@DisplayName("#2")
		@Test
		public void getNotNullChildCount_2() {
			assertEquals(1, child1.getNotNullChildCount());
		}

		@DisplayName("#3")
		@Test
		public void getNotNullChildCount_3() {
			assertEquals(0, child2.getNotNullChildCount());
		}

		@DisplayName("#4")
		@Test
		public void getNotNullChildCount_4() {
			assertEquals(0, node.getNotNullChildCount());
		}
	}

	@DisplayName("setFirstChild")
	@Nested
	public class SetFirstChild {

		@DisplayName("#1")
		@Test
		public void setFirstChild_newNode() {
			node.setFirstChild(newNode);
			
			assertSame(newNode, node.getFirstChild());
			assertSame(node, newNode.getParentNode());
			
			assertEquals(1, listener.additionEvent.size());
			assertEquals(0, listener.removalEvent.size());
			assertEquals(0, listener.dataEvent.size());
			assertEquals(1, listener.parentEvent.size());
			
			assertSame(node, listener.additionEvent.get(0).getSource());
			assertSame(newNode, listener.additionEvent.get(0).getChild());
			assertSame(node, listener.additionEvent.get(0).getParentNode());
			assertSame(0, listener.additionEvent.get(0).getChildIndex());
	
			assertSame(newNode, listener.parentEvent.get(0).getSource());
			assertSame(newNode, listener.parentEvent.get(0).getChildNode());
			assertNull(listener.parentEvent.get(0).getOldParent());
			assertSame(node, listener.parentEvent.get(0).getNewParent());
		}

		@DisplayName("#2")
		@Test
		public void setFirstChild_moveNode() {
			node.setFirstChild(child2);
			
			assertSame(child2, node.getFirstChild());
			assertSame(node, child2.getParentNode());
			
			assertEquals(1, listener.additionEvent.size());
			assertEquals(1, listener.removalEvent.size());
			assertEquals(0, listener.dataEvent.size());
			assertEquals(2, listener.parentEvent.size());
			
			assertSame(node, listener.additionEvent.get(0).getSource());
			assertSame(child2, listener.additionEvent.get(0).getChild());
			assertSame(node, listener.additionEvent.get(0).getParentNode());
			assertSame(0, listener.additionEvent.get(0).getChildIndex());

			assertSame(child2, listener.parentEvent.get(0).getSource());
			assertSame(child2, listener.parentEvent.get(0).getChildNode());
			assertSame(root, listener.parentEvent.get(0).getOldParent());
			assertNull(listener.parentEvent.get(0).getNewParent());

			assertSame(child2, listener.parentEvent.get(1).getSource());
			assertSame(child2, listener.parentEvent.get(1).getChildNode());
			assertNull(listener.parentEvent.get(1).getOldParent());
			assertSame(node, listener.parentEvent.get(1).getNewParent());

			assertSame(root, listener.removalEvent.get(0).getSource());
			assertSame(child2, listener.removalEvent.get(0).getChild());
			assertSame(root, listener.removalEvent.get(0).getParentNode());
			assertSame(2, listener.removalEvent.get(0).getChildIndex());
		}
	}
	
	@DisplayName("setSecondChild")
	@Nested
	public class SetSecondChild {

		@DisplayName("#1")
		@Test
		public void setSecondChild_newNode() {
			node.setSecondChild(newNode);
			
			assertSame(newNode, node.getSecondChild());
			assertSame(node, newNode.getParentNode());
			
			assertEquals(1, listener.additionEvent.size());
			assertEquals(0, listener.removalEvent.size());
			assertEquals(0, listener.dataEvent.size());
			assertEquals(1, listener.parentEvent.size());
			
			assertSame(node, listener.additionEvent.get(0).getSource());
			assertSame(newNode, listener.additionEvent.get(0).getChild());
			assertSame(node, listener.additionEvent.get(0).getParentNode());
			assertSame(1, listener.additionEvent.get(0).getChildIndex());
	
			assertSame(newNode, listener.parentEvent.get(0).getSource());
			assertSame(newNode, listener.parentEvent.get(0).getChildNode());
			assertNull(listener.parentEvent.get(0).getOldParent());
			assertSame(node, listener.parentEvent.get(0).getNewParent());
		}
		
		@DisplayName("#2")
		@Test
		public void setSecondChild_moveNode() {
			node.setSecondChild(child2);
			
			assertSame(child2, node.getSecondChild());
			assertSame(node, child2.getParentNode());
			
			assertEquals(1, listener.additionEvent.size());
			assertEquals(1, listener.removalEvent.size());
			assertEquals(0, listener.dataEvent.size());
			assertEquals(2, listener.parentEvent.size());
			
			assertSame(node, listener.additionEvent.get(0).getSource());
			assertSame(child2, listener.additionEvent.get(0).getChild());
			assertSame(node, listener.additionEvent.get(0).getParentNode());
			assertSame(1, listener.additionEvent.get(0).getChildIndex());
	
			assertSame(child2, listener.parentEvent.get(0).getSource());
			assertSame(child2, listener.parentEvent.get(0).getChildNode());
			assertSame(root, listener.parentEvent.get(0).getOldParent());
			assertNull(listener.parentEvent.get(0).getNewParent());
	
			assertSame(child2, listener.parentEvent.get(1).getSource());
			assertSame(child2, listener.parentEvent.get(1).getChildNode());
			assertNull(listener.parentEvent.get(1).getOldParent());
			assertSame(node, listener.parentEvent.get(1).getNewParent());
	
			assertSame(root, listener.removalEvent.get(0).getSource());
			assertSame(child2, listener.removalEvent.get(0).getChild());
			assertSame(root, listener.removalEvent.get(0).getParentNode());
			assertSame(2, listener.removalEvent.get(0).getChildIndex());
		}
	}

	@DisplayName("setThirdChild")
	@Nested
	public class SetThirdChild {

		@DisplayName("#1")
		@Test
		public void setThirdChild_newNode() {
			node.setThirdChild(newNode);
			
			assertSame(newNode, node.getThirdChild());
			assertSame(node, newNode.getParentNode());
			
			assertEquals(1, listener.additionEvent.size());
			assertEquals(0, listener.removalEvent.size());
			assertEquals(0, listener.dataEvent.size());
			assertEquals(1, listener.parentEvent.size());
			
			assertSame(node, listener.additionEvent.get(0).getSource());
			assertSame(newNode, listener.additionEvent.get(0).getChild());
			assertSame(node, listener.additionEvent.get(0).getParentNode());
			assertSame(2, listener.additionEvent.get(0).getChildIndex());
	
			assertSame(newNode, listener.parentEvent.get(0).getSource());
			assertSame(newNode, listener.parentEvent.get(0).getChildNode());
			assertNull(listener.parentEvent.get(0).getOldParent());
			assertSame(node, listener.parentEvent.get(0).getNewParent());
		}
		
		@DisplayName("#2")
		@Test
		public void setThirdChild_moveNode() {
			node.setThirdChild(child2);
			
			assertSame(child2, node.getThirdChild());
			assertSame(node, child2.getParentNode());
			
			assertEquals(1, listener.additionEvent.size());
			assertEquals(1, listener.removalEvent.size());
			assertEquals(0, listener.dataEvent.size());
			assertEquals(2, listener.parentEvent.size());
			
			assertSame(node, listener.additionEvent.get(0).getSource());
			assertSame(child2, listener.additionEvent.get(0).getChild());
			assertSame(node, listener.additionEvent.get(0).getParentNode());
			assertSame(2, listener.additionEvent.get(0).getChildIndex());
	
			assertSame(child2, listener.parentEvent.get(0).getSource());
			assertSame(child2, listener.parentEvent.get(0).getChildNode());
			assertSame(root, listener.parentEvent.get(0).getOldParent());
			assertNull(listener.parentEvent.get(0).getNewParent());
	
			assertSame(child2, listener.parentEvent.get(1).getSource());
			assertSame(child2, listener.parentEvent.get(1).getChildNode());
			assertNull(listener.parentEvent.get(1).getOldParent());
			assertSame(node, listener.parentEvent.get(1).getNewParent());
	
			assertSame(root, listener.removalEvent.get(0).getSource());
			assertSame(child2, listener.removalEvent.get(0).getChild());
			assertSame(root, listener.removalEvent.get(0).getParentNode());
			assertSame(2, listener.removalEvent.get(0).getChildIndex());
		}
	}

	@DisplayName("setFourthChild")
	@Nested
	public class SetFourthChild {

		@DisplayName("#1")
		@Test
		public void setFourthChild_newNode() {
			node.setFourthChild(newNode);
			
			assertSame(newNode, node.getFourthChild());
			assertSame(node, newNode.getParentNode());
			
			assertEquals(1, listener.additionEvent.size());
			assertEquals(0, listener.removalEvent.size());
			assertEquals(0, listener.dataEvent.size());
			assertEquals(1, listener.parentEvent.size());
			
			assertSame(node, listener.additionEvent.get(0).getSource());
			assertSame(newNode, listener.additionEvent.get(0).getChild());
			assertSame(node, listener.additionEvent.get(0).getParentNode());
			assertSame(3, listener.additionEvent.get(0).getChildIndex());
	
			assertSame(newNode, listener.parentEvent.get(0).getSource());
			assertSame(newNode, listener.parentEvent.get(0).getChildNode());
			assertNull(listener.parentEvent.get(0).getOldParent());
			assertSame(node, listener.parentEvent.get(0).getNewParent());
		}
		
		@DisplayName("#2")
		@Test
		public void setFourthChild_moveNode() {
			node.setFourthChild(child2);
			
			assertSame(child2, node.getFourthChild());
			assertSame(node, child2.getParentNode());
			
			assertEquals(1, listener.additionEvent.size());
			assertEquals(1, listener.removalEvent.size());
			assertEquals(0, listener.dataEvent.size());
			assertEquals(2, listener.parentEvent.size());
			
			assertSame(node, listener.additionEvent.get(0).getSource());
			assertSame(child2, listener.additionEvent.get(0).getChild());
			assertSame(node, listener.additionEvent.get(0).getParentNode());
			assertSame(3, listener.additionEvent.get(0).getChildIndex());
	
			assertSame(child2, listener.parentEvent.get(0).getSource());
			assertSame(child2, listener.parentEvent.get(0).getChildNode());
			assertSame(root, listener.parentEvent.get(0).getOldParent());
			assertNull(listener.parentEvent.get(0).getNewParent());
	
			assertSame(child2, listener.parentEvent.get(1).getSource());
			assertSame(child2, listener.parentEvent.get(1).getChildNode());
			assertNull(listener.parentEvent.get(1).getOldParent());
			assertSame(node, listener.parentEvent.get(1).getNewParent());
	
			assertSame(root, listener.removalEvent.get(0).getSource());
			assertSame(child2, listener.removalEvent.get(0).getChild());
			assertSame(root, listener.removalEvent.get(0).getParentNode());
			assertSame(2, listener.removalEvent.get(0).getChildIndex());
		}
	}

	@DisplayName("moveTo")
	@Nested
	public class MoveTo {

		@DisplayName("#1")
		@Test
		public void moveToNodeInt() {
			assertTrue(child2.moveTo(node, 0));
			
			assertSame(child2, node.getFirstChild());
			assertSame(node, child2.getParentNode());
			
			assertEquals(1, listener.additionEvent.size());
			assertEquals(1, listener.removalEvent.size());
			assertEquals(0, listener.dataEvent.size());
			assertEquals(1, listener.parentEvent.size());
			
			assertSame(node, listener.additionEvent.get(0).getSource());
			assertSame(child2, listener.additionEvent.get(0).getChild());
			assertSame(node, listener.additionEvent.get(0).getParentNode());
			assertSame(0, listener.additionEvent.get(0).getChildIndex());
	
			assertSame(child2, listener.parentEvent.get(0).getSource());
			assertSame(child2, listener.parentEvent.get(0).getChildNode());
			assertSame(root, listener.parentEvent.get(0).getOldParent());
			assertSame(node, listener.parentEvent.get(0).getNewParent());
	
			assertSame(root, listener.removalEvent.get(0).getSource());
			assertSame(child2, listener.removalEvent.get(0).getChild());
			assertSame(root, listener.removalEvent.get(0).getParentNode());
			assertSame(2, listener.removalEvent.get(0).getChildIndex());
		}
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
			name = name1;
		}
		
		@Override
		public void toJson(JsonBuffer buffer) {
			super.toJson(buffer);
			buffer.add("name", name); //$NON-NLS-1$
		}
		
	}
	
}