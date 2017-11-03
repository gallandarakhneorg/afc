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

package org.arakhne.afc.math.tree.iterator;

import org.junit.Before;

/**
 * <pre><code>
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
 * </code></pre>
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
		this.child1.addUserData("a");  //$NON-NLS-1$
		
		this.child11.addUserData("b");  //$NON-NLS-1$
		
		this.child12.addUserData("c");  //$NON-NLS-1$
		this.child12.addUserData("d");  //$NON-NLS-1$
		
		this.child121.addUserData("e");  //$NON-NLS-1$
		this.child121.addUserData("f");  //$NON-NLS-1$
		this.child121.addUserData("g");  //$NON-NLS-1$
		
		this.child1211.addUserData("h");  //$NON-NLS-1$
		
		this.child1212.addUserData("i");  //$NON-NLS-1$
		
		this.child12121.addUserData("j");  //$NON-NLS-1$
		this.child12121.addUserData("k");  //$NON-NLS-1$
		
		this.child12122.addUserData("l");  //$NON-NLS-1$
		this.child12122.addUserData("m");  //$NON-NLS-1$
		this.child12122.addUserData("n");  //$NON-NLS-1$

		this.child21.addUserData("o");  //$NON-NLS-1$

		this.child22.addUserData("p");  //$NON-NLS-1$

		this.child211.addUserData("q");  //$NON-NLS-1$
		this.child211.addUserData("r");  //$NON-NLS-1$

		this.child222.addUserData("s");  //$NON-NLS-1$
		this.child222.addUserData("t");  //$NON-NLS-1$
	}
	
}