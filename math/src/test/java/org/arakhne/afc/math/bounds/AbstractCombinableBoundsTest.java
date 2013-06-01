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

/** Abstract definition of a unit test for the combinable bounds.
 *
 * @param <P> is the type of the point.
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public abstract class AbstractCombinableBoundsTest<P> extends AbstractBoundsTest<P> {

	/** 
	 */
	public abstract void testIsInit();

	/**
	 */
	public abstract void testReset();

	/**
	 */
	public abstract void testCombineP();

	/**
	 */
	public abstract void testSetP();

	/**
	 */
	public abstract void testCombineCB();

	/**
	 */
	public abstract void testCombineCollection();
	
	/**
	 */
	public abstract void testSetCB();

	//-------------------------------------------
	// IntersectionClassificier
	//-------------------------------------------

	/**
	 */
	public abstract void testClassifiesIC();

	/**
	 */
	public abstract void testIntersectsIC();

	/**
	 */
	public abstract void testClassifiesP();

	/**
	 */
	public abstract void testIntersectsP();

	/**
	 */
	public abstract void testClassifiesPP();

	/**
	 */
	public abstract void testIntersectsPP();	

}