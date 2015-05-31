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
public abstract class AbstractDataTreeIteratorTest extends AbstractTreeIteratorTest {
	
	@Before
	@Override
	public void setUp() throws Exception {
		super.setUp();
		this.child1.addUserData("a"); //$NON-NLS-1$
		
		this.child11.addUserData("b"); //$NON-NLS-1$
		
		this.child12.addUserData("c"); //$NON-NLS-1$
		this.child12.addUserData("d"); //$NON-NLS-1$
		
		this.child121.addUserData("e"); //$NON-NLS-1$
		this.child121.addUserData("f"); //$NON-NLS-1$
		this.child121.addUserData("g"); //$NON-NLS-1$
		
		this.child1211.addUserData("h"); //$NON-NLS-1$
		
		this.child1212.addUserData("i"); //$NON-NLS-1$
		
		this.child12121.addUserData("j"); //$NON-NLS-1$
		this.child12121.addUserData("k"); //$NON-NLS-1$
		
		this.child12122.addUserData("l"); //$NON-NLS-1$
		this.child12122.addUserData("m"); //$NON-NLS-1$
		this.child12122.addUserData("n"); //$NON-NLS-1$

		this.child21.addUserData("o"); //$NON-NLS-1$

		this.child22.addUserData("p"); //$NON-NLS-1$

		this.child211.addUserData("q"); //$NON-NLS-1$
		this.child211.addUserData("r"); //$NON-NLS-1$

		this.child222.addUserData("s"); //$NON-NLS-1$
		this.child222.addUserData("t"); //$NON-NLS-1$
	}
	
}