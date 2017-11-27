/*
 * 
 * $Id$
 * 
 * Copyright (c) 2006-09, Multiagent Team - Systems and Transportation Laboratory (SeT)
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
package org.arakhne.afc.jasim.environment.model.perceptions;

/**
 * Defines the type of perception generator supported by the JaSim API.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public enum PerceptionGeneratorType {

	/** Perception generator is local (no network support)
	 * and run each agent perception sequentially in the main thread.
	 * The algorithm is based on a top-down approach: starting from 
	 * univers bounds, and converging to the perceivable entities.
	 */
	LOCAL_SEQUENTIAL_TOPDOWN,
	
	/** Perception generator is local (no network support)
	 * and run each agent perception in a dedicated thread.
	 * The algorithm is based on a top-down approach: starting from 
	 * univers bounds, and converging to the perceivable entities.
	 */
	LOCAL_THREADED_TOPDOWN,

	/** Perception generator is local (no network support)
	 * and run each agent perception sequentially in the main thread.
	 * The algorithm is based on a top-down approach: starting from 
	 * the entity bounds bounds, finding the smallest area enclosing
	 * the frustum, and converging to the perceivable entities.
	 */
	LOCAL_SEQUENTIAL_BOTTOMUP,
	
	/** Perception generator is local (no network support)
	 * and run each agent perception in a dedicated thread.
	 * The algorithm is based on a top-down approach: starting from 
	 * the entity bounds bounds, finding the smallest area enclosing
	 * the frustum, and converging to the perceivable entities.
	 */
	LOCAL_THREADED_BOTTOMUP;
	
	/** Replies the default algorithm.
	 * 
	 * @return the default algorithm.
	 */
	public static PerceptionGeneratorType getSystemDefault() {
		return LOCAL_THREADED_TOPDOWN;
	}

}
