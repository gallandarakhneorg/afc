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
package org.arakhne.afc.jasim.environment.interfaces.body.factory;

/**
 * This class is describing a frustum to spawn.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public abstract class FrustumDescription {

	/**
	 * Type of a frustum.
	 * 
	 * @author $Author: sgalland$
	 * @version $Name$ $Revision$ $Date$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	public enum FrustumType {
		/**
		 * Rectangular perception, with far, left-right, and bottom-up distances.
		 */
		RECTANGLE,
		/**
		 * Pyramidal perception, with far and near distances, and opennessAngle
		 */
		PYRAMID,
		/**
		 * Spherical perception, with far distance.
		 */
		SPHERE,
		/**
		 * Pedestrian frustum: composed of a pyramidal and spherical frustums.
		 */
		PEDESTRIAN;
		
		/**
		 * Replies the frustype which is corresponding to the given string.
		 * <table>
		 * <tr><td>{@code "rectangle"}</td><td>{@code RECTANGLE}</td></tr>
		 * <tr><td>{@code "pyramid"}</td><td>{@code PYRAMID}</td></tr>
		 * <tr><td>{@code "sphere"}</td><td>{@code SPHERE}</td></tr>
		 * <tr><td>{@code "pedestrian"}</td><td>{@code PEDESTRIAN}</td></tr>
		 * </table>
		 * 
		 * @param txt
		 * @return the type or <code>null</code> if the given strin gis not recognized.
		 */
		public static FrustumType parse(String txt) {
			if ("rectangle".equalsIgnoreCase(txt)) //$NON-NLS-1$
				return RECTANGLE;
			if ("pyramid".equalsIgnoreCase(txt)) //$NON-NLS-1$
				return PYRAMID;
			if ("sphere".equalsIgnoreCase(txt)) //$NON-NLS-1$
				return SPHERE;
			if ("pedestrian".equalsIgnoreCase(txt)) //$NON-NLS-1$
				return PEDESTRIAN;
			return null;
		}
	}

	private final FrustumType frustumType;
	private final double eyePosition;

	/**
	 * @param type
	 * @param eyePosition
	 */
	public FrustumDescription(FrustumType type, double eyePosition) {
		this.frustumType = type;
		this.eyePosition = Math.max(0., eyePosition);
	}
	
	/** Replies the type of frustum.
	 * 
	 * @return the type of frustum.
	 */
	public FrustumType getFrustumType() {
		return this.frustumType;
	}
	
	/** Replies the distance between the feet and the eyes.
	 * 
	 * @return the distance between the feet and the eyes.
	 */
	public double getEyePosition() {
		return this.eyePosition;
	}
	
}
