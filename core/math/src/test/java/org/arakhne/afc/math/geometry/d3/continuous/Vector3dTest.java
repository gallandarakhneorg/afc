/**
 * 
 * fr.utbm.v3g.core.math.Vector3dTest.java
 *
 * Copyright (c) 2008-10, Multiagent Team - Systems and Transportation Laboratory (SeT)
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of the Systems and Transportation Laboratory ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with the SeT.
 * 
 * http://www.multiagent.fr/
 *
 * Primary author : Olivier LAMOTTE (olivier.lamotte@utbm.fr) - 2015
 *
 */
package org.arakhne.afc.math.geometry.d3.continuous;

import org.junit.Before;
import org.junit.Test;

/**
 * @author Olivier LAMOTTE (olivier.lamotte@utbm.fr)
 *
 */
public class Vector3dTest {

	private Vector3d v;
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		
		this.v = new Vector3d(10, 10, 10);
		
	}

	/**
	 * Test method for {@link org.arakhne.afc.math.geometry.d3.continuous.Vector3d#set(org.arakhne.afc.math.geometry.d3.continuous.Vector3d)}.
	 */
	@Test
	public void testSet() throws Exception {
		
		System.out.println(v.length());
		this.v.set(20, 20, 20);
		System.out.println(v.length());
		
	}

}
