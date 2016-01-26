/**
 * 
 * fr.utbm.v3g.core.math.Point2dTest.java
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
public class Point2dTest {

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		//
	}

	/**
	 * Test method for {@link org.arakhne.afc.math.geometry.d2.continuous.Point2d#Point2d(double, double)}.
	 */
	@Test
	public static void testPoint2d() {
		Point2d point = new Point2d(-1, -4);
		point.absolute();
	}

}
