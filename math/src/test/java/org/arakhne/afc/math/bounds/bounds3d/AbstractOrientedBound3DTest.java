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
package org.arakhne.afc.math.bounds.bounds3d;

import org.arakhne.afc.math.bounds.AbstractCombinableBoundsTest;
import org.arakhne.afc.math.continous.object3d.Point3f;

/**
 * Unit test for abstract 3D oriented bounds.
 * 
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public abstract class AbstractOrientedBound3DTest extends AbstractCombinableBoundsTest<Point3f> {

	/**
	 */
	public abstract void testToBounds2D();
	
	/**
	 */
	public abstract void testToBounds2DCoordinateSystem3D();

	/**
	 */
	public abstract void testGetMinX();

	/**
	 */
	public abstract void testGetMinY();

	/**
	 */
	public abstract void testGetMinZ();

	/**
	 */
	public abstract void testGetMaxX();

	/**
	 */
	public abstract void testGetMaxY();

	/**
	 */
	public abstract void testGetMaxZ();

	/**
	 */
	public abstract void testGetCenterX();

	/** 
	 */
	public abstract void testGetCenterY();

	/** 
	 */
	public abstract void testGetCenterZ();

	/**
	 */
	public abstract void testCombineTuple3fArray();
	
	/**
	 */
	public abstract void testSetTuple3fArray();

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
	public abstract void testGetT();

	/**
	 */
	public abstract void testGetRExtent();

	/**
	 */
	public abstract void testGetSExtent();

	/**
	 */
	public abstract void testGetTExtent();

	/** 
	 */
	public abstract void testTranslateVector3f();

	//-------------------------------------------
	// IntersectionClassificier
	//-------------------------------------------
	
	/**
	 */
	public abstract void testClassifiesPoint3fFloat();

	/**
	 */
	public abstract void testIntersectsPoint3fFloat();

	/**
	 */
	public abstract void testClassifiesPlane();

	/**
	 */
	public abstract void testClassifiesAgainstPlane();

	/**
	 */
	public abstract void testIntersectsPlane();

	/**
	 */
	public abstract void testClassifiesPoint3fVector3fArrayFloatArray();

	/**
	 */
	public abstract void testIntersectsPoint3fVector3fArrayFloatArray();

	/**
	 */
	public abstract void testRotateAxisAngle4f();
	
	/**
	 */
	public abstract void testRotateQuaternion();

	/**
	 */
	public abstract void testSetRotationAxisAngle4f();
	
	/**
	 */
	public abstract void testSetRotationQuaternion();

}