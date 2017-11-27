/*
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
package fr.utbm.set.jasim.io.sfg;

/**
 * Set of parameters to initialize a simulation.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class AbstractParameter {

	private float environmentDimension;
	
	/**
	 * @param dimension must be one of <code>1</code>, <code>1.5</code>, <code>2</code>, <code>2.5</code>, 
	 * or <code>3</code>.
	 */
	public AbstractParameter(float dimension) {
		assert(dimension==1f || dimension==1.5f || dimension==2f || dimension==2.5f || dimension==3f);
		this.environmentDimension = dimension;
	}
	
	/**
	 */
	AbstractParameter() {
		this.environmentDimension = 0f;
	}

	/** Replies the dimension of the environment.
	 * 
	 * @return <code>1</code>, <code>1.5</code>, <code>2</code>, <code>2.5</code>, 
	 * or <code>3</code>.
	 */
	public final float getEnvironmentDimension() {
		return this.environmentDimension;
	}

	/** Set the dimension of the environment.
	 * 
	 * @param dimension <code>1</code>, <code>1.5</code>, <code>2</code>, <code>2.5</code>, 
	 * or <code>3</code>.
	 */
	public final void setEnvironmentDimension(float dimension) {
		assert(dimension==1f || dimension==1.5f || dimension==2f || dimension==2.5f || dimension==3f);
		this.environmentDimension = dimension;
	}

}
