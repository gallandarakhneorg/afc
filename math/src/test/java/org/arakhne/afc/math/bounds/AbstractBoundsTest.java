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
package org.arakhne.afc.math.bounds;

import org.arakhne.util.ref.AbstractTestCase;

/** Abstract definition of a unit test for the bounds.
 *
 * @param <P> is the type of the point.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public abstract class AbstractBoundsTest<P> extends AbstractTestCase {

	/**
	 */
	public abstract void testGetLower();

	/** 
	 */
	public abstract void testGetUpper();

	/**
	 */
	public abstract void testGetLowerUpper();

	/** 
	 */
	public abstract void testGetCenter();

	/** 
	 */
	public abstract void testGetPosition();

	/** 
	 */
	public abstract void testGetMathematicalDimension();
	
	/** 
	 */
	public abstract void testClone();

	/**
	 */
	public abstract void testIsEmpty();

	/** 
	 */
	public abstract void testDistanceP();

	/** 
	 */
	public abstract void testDistanceSquaredP();

	/** 
	 */
	public abstract void testNearestPointP();

	/** 
	 */
	public abstract void testDistanceMaxP();

	/** 
	 */
	public abstract void testDistanceMaxSquaredP();

	/** 
	 */
	public abstract void testFarestPointP();

}