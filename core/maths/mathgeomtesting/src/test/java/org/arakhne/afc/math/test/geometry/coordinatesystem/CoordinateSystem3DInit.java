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
package org.arakhne.afc.math.test.geometry.coordinatesystem;

import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.arakhne.afc.math.geometry.coordinatesystem.CoordinateSystem3D;
import org.arakhne.afc.math.geometry.coordinatesystem.CoordinateSystemConstants;

/**
 * @author $Author: sgalland$
 * @author $Author: tpiotrow$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
@SuppressWarnings("all")
public class CoordinateSystem3DInit {

	@BeforeEach
	public void setUp() {
		CoordinateSystem3D.setDefaultCoordinateSystem(null);
	}
	
	@Test
	public void getDefaultSimulationCoordinateSystem() {
		CoordinateSystem3D cs = CoordinateSystemConstants.SIMULATION_3D;
		assertSame(CoordinateSystem3D.XYZ_RIGHT_HAND,cs);
	}

	@Test
	public void getDefaultCoordinateSystem() {
		CoordinateSystem3D cs = CoordinateSystem3D.getDefaultCoordinateSystem();
		assertSame(CoordinateSystemConstants.SIMULATION_3D, cs);
	}

	@Test
	public void setDefaultCoordinateSystem() {
		CoordinateSystem3D.setDefaultCoordinateSystem(CoordinateSystem3D.XYZ_LEFT_HAND);
		assertSame(CoordinateSystem3D.XYZ_LEFT_HAND, CoordinateSystem3D.getDefaultCoordinateSystem());
		CoordinateSystem3D.setDefaultCoordinateSystem(CoordinateSystem3D.XZY_RIGHT_HAND);
		assertSame(CoordinateSystem3D.XZY_RIGHT_HAND, CoordinateSystem3D.getDefaultCoordinateSystem());
		CoordinateSystem3D.setDefaultCoordinateSystem(CoordinateSystem3D.XZY_LEFT_HAND);
		assertSame(CoordinateSystem3D.XZY_LEFT_HAND, CoordinateSystem3D.getDefaultCoordinateSystem());
		CoordinateSystem3D.setDefaultCoordinateSystem(null);
		assertSame(CoordinateSystem3D.XYZ_RIGHT_HAND, CoordinateSystem3D.getDefaultCoordinateSystem());
	}

}
