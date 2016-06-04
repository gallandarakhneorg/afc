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

package org.arakhne.afc.math.tree.iterator;

import org.junit.Before;

/**
 * <code><pre>
 * root
 * + child1                  a
 * | + child11               b
 * | \ child12               cd
 * |   + child121            efg
 * |   | + child1211         h
 * |   | \ child1212         i
 * |   |   + child12121      jk
 * |   |   \ child12122      lmn
 * |   \ child122 
 * \ child2
 *   + child21               o
 *   | + child211            qr
 *   | \ null 
 *   \ child22               p
 *     + null 
 *     \ child222            st
 * </pre></code>
 * 
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@SuppressWarnings("all")
public abstract class AbstractDataTreeIteratorTest extends AbstractTreeIteratorTest {
	
	@Before
	@Override
	public void setUp() throws Exception {
		super.setUp();
		this.child1.addUserData("a"); 
		
		this.child11.addUserData("b"); 
		
		this.child12.addUserData("c"); 
		this.child12.addUserData("d"); 
		
		this.child121.addUserData("e"); 
		this.child121.addUserData("f"); 
		this.child121.addUserData("g"); 
		
		this.child1211.addUserData("h"); 
		
		this.child1212.addUserData("i"); 
		
		this.child12121.addUserData("j"); 
		this.child12121.addUserData("k"); 
		
		this.child12122.addUserData("l"); 
		this.child12122.addUserData("m"); 
		this.child12122.addUserData("n"); 

		this.child21.addUserData("o"); 

		this.child22.addUserData("p"); 

		this.child211.addUserData("q"); 
		this.child211.addUserData("r"); 

		this.child222.addUserData("s"); 
		this.child222.addUserData("t"); 
	}
	
}