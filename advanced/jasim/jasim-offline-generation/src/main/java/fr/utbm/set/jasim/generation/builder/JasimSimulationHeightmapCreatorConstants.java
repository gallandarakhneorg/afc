/*
 * $Id$
 * 
 * Copyright (c) 2008-09, Multiagent Team - Systems and Transportation Laboratory (SeT)
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of the Systems and Transportation Laboratory ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with the SeT.
 * 
 * http://www.multiagent.fr/
 */
package fr.utbm.set.jasim.generation.builder;

import fr.utbm.set.geom.system.CoordinateSystem3D;
import fr.utbm.set.geom.system.CoordinateSystemConstants;

/**
 * Constants for the heightmap creators.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public interface JasimSimulationHeightmapCreatorConstants {
	
	/**
	 * Indicates if the coordinate system convertion must be done.
	 */
	public static final boolean ALLOW_COORDINATE_SYSTEM_CONVERT = true;

	/**
	 * Indicates if the objects must be transformed according to the
	 * parent's position.
	 */
	public static final boolean ALLOW_OBJECT_TRANSFORMATION = false;

	/**
	 * Type of the heightmap to generate
	 */
	public static final String DEFAULT_HEIGHTMAP_TYPE = "png"; //$NON-NLS-1$

	/**
	 * Count of pixels per meter to obtain the picture width.
	 */
	public static final int DEFAULT_PPM_X = 1;

	/**
	 * Count of pixels per meter to obtain the picture height.
	 */
	public static final int DEFAULT_PPM_Y = 1;
	
	/**
	 * Color under which (inclusive) the pixels of a heightmap are assumed to be not traversable.
	 */
	public static final int DEFAULT_GROUND_ZERO = 0;

	/**
	 * Default coordinate system for 3DS files.
	 */
	public static final CoordinateSystem3D DEFAULT_COORDINATE_SYSTEM_3DS = CoordinateSystemConstants.SIMULATION_3D;

}
