/* 
 * $Id$
 * 
 * Copyright (c) 2006-10, Multiagent Team,
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
package org.arakhne.afc.math.geometry.coordinatesystem;

import static org.junit.Assert.assertSame;

import org.junit.Before;
import org.junit.Test;

/**
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@SuppressWarnings("all")
public class CoordinateSystem2DInit {

	@Before
	public void setUp() {
		CoordinateSystem2D.setDefaultCoordinateSystem(null);
	}
	
	@Test
	public void getDefaultSimulationCoordinateSystem() {
		CoordinateSystem2D cs = CoordinateSystemConstants.SIMULATION_2D;
		assertSame(CoordinateSystem2D.XY_RIGHT_HAND,cs);
	}

	@Test
	public void getDefaultCoordinateSystem() {
		CoordinateSystem2D cs = CoordinateSystem2D.getDefaultCoordinateSystem();
		assertSame(CoordinateSystemConstants.SIMULATION_2D, cs);
	}

	@Test
	public void setDefaultCoordinateSystem() {
		CoordinateSystem2D.setDefaultCoordinateSystem(CoordinateSystem2D.XY_LEFT_HAND);
		assertSame(CoordinateSystem2D.XY_LEFT_HAND, CoordinateSystem2D.getDefaultCoordinateSystem());
		CoordinateSystem2D.setDefaultCoordinateSystem(CoordinateSystem2D.XY_RIGHT_HAND);
		assertSame(CoordinateSystem2D.XY_RIGHT_HAND, CoordinateSystem2D.getDefaultCoordinateSystem());
		CoordinateSystem2D.setDefaultCoordinateSystem(null);
		assertSame(CoordinateSystem2D.XY_RIGHT_HAND, CoordinateSystem2D.getDefaultCoordinateSystem());
	}

}
