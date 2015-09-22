/**
 * 
 * fr.utbm.v3g.core.math.Tuple2dTest.java
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
package org.arakhne.afc.math.geometry.d2.continuous;

import org.junit.Before;
import org.junit.Test;

/**
 * @author Olivier LAMOTTE (olivier.lamotte@utbm.fr)
 *
 */
public class Tuple2dTest {

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testAbsolute() throws Exception {
		Tuple2d<?> tuple = new Tuple2d<>(-10, -20);
		tuple.absolute();
		System.out.println(tuple.getX()+"  "+tuple.getY());
	}
	
	
}
