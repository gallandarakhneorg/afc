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
package org.arakhne.afc.math.bounds.bounds1d;

import org.arakhne.afc.math.bounds.AbstractCombinableBoundsTest;
import org.arakhne.afc.math.object.Point1D;


/**
 * Unit test for abstract 1D combination bounds.
 * 
 * @param <S> is the type of segment
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public abstract class AbstractCombinableBound1DTest<S> extends AbstractCombinableBoundsTest<Point1D> {

	/**
	 */
	public abstract void testToBounds1D5();

	/**
	 */
	public abstract void testToBounds1D5FloatFloat();

	/**
	 */
	public abstract void testToBounds2D();

	/**
	 */
	public abstract void testToBounds2DCoordinateSystem2D();

	/**
	 */
	public abstract void testToBounds2DFloatCoordinateSystem2D();

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
	public abstract void testToBounds3DFloatFloatFloatCoordinateSystem3D();

	/**
	 */
	public abstract void testClamp();

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
	public abstract void testGetSize();
	
	/**
	 */
	public abstract void testCombineFloatArray();

	/**
	 */
	public abstract void testSetFloatArray();

	/**
	 */
	public abstract void testSetBoxFloatFloat();

	/** 
	 */
	public abstract void testSetBoxBounds1D();

	/** 
	 */
	public abstract void testSetSegmentS();

}
