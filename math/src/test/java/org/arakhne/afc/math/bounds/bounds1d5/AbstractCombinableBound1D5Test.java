/* 
 * $Id$
 * 
 * Copyright (c) 2008-10, Multiagent Team,
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
package org.arakhne.afc.math.bounds.bounds1d5;

import org.arakhne.afc.math.bounds.AbstractCombinableBoundsTest;
import org.arakhne.afc.math.object.Point1D5;

/**
 * Unit test for abstract 1.5D combination bounds.
 * 
 * @param <S> is the type of segment
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public abstract class AbstractCombinableBound1D5Test<S> extends AbstractCombinableBoundsTest<Point1D5> {

	/** 
	 */
	public abstract void testToBounds1D();

	/**
	 */
	public abstract void testToBounds2D();

	/**
	 */
	public abstract void testToBounds2DCoordinateSystem2D();

	/**
	 */
	public abstract void testToBounds3D();

	/**
	 */
	public abstract void testToBounds3DCoordinateSystem3D();
	
	/**
	 */
	public abstract void testToBounds3DFloatFloatCoordinateSystem3D();

	/**
	 */
	public abstract void testGetSegment();
	
	/**
	 */
	public abstract  void testGetMinX();

	/**
	 */
	public abstract void testGetMaxX();

	/**
	 */
	public abstract void testGetCenterX();

	/**
	 */
	public abstract  void testGetMinY();

	/**
	 */
	public abstract void testGetMaxY();

	/**
	 */
	public abstract void testGetCenterY();

	/**
	 */
	public abstract void testGetSizeX();
	
	/**
	 */
	public abstract void testGetSizeY();

	/**
	 */
	public abstract void testCombineFloatArray();

	/**
	 */
	public abstract void testSetFloatArray();

	/** 
	 */
	public abstract void testSetBoxBounds1D5();

	/** 
	 */
	public abstract void testSetSegmentS();

	/**
	 */
	public abstract void testClamp();

	/**
	 */
	public abstract void testGetJutting();

	/**
	 */
	public abstract void testGetLateralSize();

	/**
	 */
	public abstract void testDistancePoint1D();

	/**
	 */
	public abstract void testDistanceMaxPoint1D();

	/**
	 */
	public abstract void testDistanceSquaredPoint1D();

	/**
	 */
	public abstract void testDistanceMaxSquaredPoint1D();

	/**
	 */
	public abstract void testCombineTuple2dArray();

	/**
	 */
	public abstract void testSetTuple2dArray();

	/**
	 */
	public abstract void testSetJuttingFloat();

	/**
	 */
	public abstract void testSetLateralSizeFloat();

	/**
	 */
	public abstract void testSetBoxFloatFloatFloatFloat();

}
