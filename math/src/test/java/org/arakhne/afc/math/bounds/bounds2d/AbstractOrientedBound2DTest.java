/* 
 * $Id$
 * 
 * Copyright (c) 2009-10, Multiagent Team,
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
package org.arakhne.afc.math.bounds.bounds2d;

import org.arakhne.afc.math.bounds.AbstractCombinableBoundsTest;
import org.arakhne.afc.math.continous.object2d.Point2f;


/**
 * Unit test for abstract 2D oriented bounds.
 * 
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public abstract class AbstractOrientedBound2DTest extends AbstractCombinableBoundsTest<Point2f> {

	/**
	 */
	public abstract void testToBounds3D();
	
	/**
	 */
	public abstract void testToBounds3DCoordinateSystem3D();

	/**
	 */
	public abstract void testToBounds3DFloatFloat();
	
	/**
	 */
	public abstract void testToBounds3DFloatFloatCoordinateSystem3D();

	/**
	 */
	public abstract void testGetMinX();

	/**
	 */
	public abstract void testGetMinY();

	/**
	 */
	public abstract void testGetMaxX();

	/**
	 */
	public abstract void testGetMaxY();

	/**
	 */
	public abstract void testGetCenterX();

	/** 
	 */
	public abstract void testGetCenterY();

	/**
	 */
	public abstract void testCombineTuple2fArray();
	
	/**
	 */
	public abstract void testSetTuple2fArray();

	/**
	 */
	public abstract void testGetLocalOrientedVertices();
	
	/**
	 */
	public abstract void testGetVertexCount();

	/**
	 */
	public abstract void testGetLocalVertexAt();

	/**
	 */
	public abstract void testGetGlobalOrientedVertices();

	/**
	 */
	public abstract void testGetGlobalVertexAt();

	/**
	 */
	public abstract void testGetOrientedBoundAxis();

	/**
	 */
	public abstract void testGetOrientedBoundExtents();

	/**
	 */
	public abstract void testGetR();
	
	/**
	 */
	public abstract void testGetS();

	/**
	 */
	public abstract void testGetRExtent();

	/**
	 */
	public abstract void testGetSExtent();

	/** 
	 */
	public abstract void testTranslateVector2f();

	//-------------------------------------------
	// IntersectionClassificier
	//-------------------------------------------
	
	/**
	 */
	public abstract void testClassifiesPoint2fFloat();

	/**
	 */
	public abstract void testIntersectsPoint2fFloat();

	/**
	 */
	public abstract void testClassifiesPoint2fVector2fArrayFloatArray();

	/**
	 */
	public abstract void testIntersectsPoint2fVector2fArrayFloatArray();

}
