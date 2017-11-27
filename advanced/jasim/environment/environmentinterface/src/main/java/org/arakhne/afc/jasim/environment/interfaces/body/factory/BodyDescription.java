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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import org.arakhne.afc.jasim.environment.interfaces.body.AgentBody;
import org.arakhne.afc.jasim.environment.semantics.Semantic;

/**
 * This class is describing a body to spawn.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class BodyDescription {

	private final Class<? extends AgentBody<?,?>> type;
	private final double a,b,c;
	private final boolean isPedestrian;
	private Collection<Semantic> semantics = null;

	/** Create a description for a pedestrian body.
	 * 
	 * @param type is the type of the body to spawn.
	 * @param height is the distance from the feet to the head.
	 * @param radius is the body's radius.
	 * @return the description
	 */
	public static BodyDescription createPedestrianBodyDescription(
			Class<? extends AgentBody<?,?>> type,
			double height, double radius) {
		return new BodyDescription(type, height, radius, true);
	}
	
	/** Create a description for a vehicle body.
	 * 
	 * @param type is the type of the body to spawn.
	 * @param length is the length of the vehicle.
	 * @param lateralSize is the vehicle lateral size.
	 * @param height is the vehicle up-down size.
	 * @return the description
	 */
	public static BodyDescription createVehicleBodyDescription(
			Class<? extends AgentBody<?,?>> type,
			double length, double lateralSize, double height) {
		return new BodyDescription(type, length, lateralSize, false);
	}
	
	private BodyDescription(Class<? extends AgentBody<?,?>> type,
			double a, double b, boolean isPedestrian) {
		this.type = type;
		this.a = a;
		this.b = b;
		this.c = 0.;
		this.isPedestrian = isPedestrian;
	}
	
	/** Replies if this description describes a pedestrian body.
	 * 
	 * @return <code>true</code> if the description is for a pedestrian body,
	 * otherwise <code>false</code>
	 */
	public boolean isPedestrian() {
		return this.isPedestrian;
	}

	/** Replies if this description describes a vehicle body.
	 * 
	 * @return <code>true</code> if the description is for a vehicle body,
	 * otherwise <code>false</code>
	 */
	public boolean isVehicle() {
		return !this.isPedestrian;
	}
	
	/** Replies the type of the body to spawn.
	 * 
	 * @return the type of the body to spawn.
	 */
	public Class<? extends AgentBody<?,?>> getBodyType() {
		return this.type;
	}

	/** Replies the distance between the feet and the top of the head.
	 * 
	 * @return the distance between the feet and the top of the head.
	 */
	public double getPedestrianHeight() {
		return this.a;
	}
		
	/** Replies the radius of the pedestrian body.
	 * 
	 * @return the distance between the feet and the top of the head.
	 */
	public double getPedestrianRadius() {
		return this.b;
	}

	/** Replies the length of a vehicle.
	 * 
	 * @return the length of a vehicle.
	 */
	public double getVehicleLength() {
		return this.a;
	}
		
	/** Replies the vehicle lateral size.
	 * 
	 * @return the vehicle lateral size.
	 */
	public double getVehicleLateralSize() {
		return this.b;
	}

	/** Replies the vehicle up-down size.
	 * 
	 * @return the vehicle up-down size.
	 */
	public double getVehicleVerticalSize() {
		return this.c;
	}

	/** Add the list of semantics.
	 * 
	 * @param semantic
	 */
	public void addSemantics(Semantic semantic) {
		if (this.semantics==null) {
			this.semantics = new ArrayList<Semantic>();
		}
		this.semantics.add(semantic);
	}

	/** Replies the list of semantics.
	 * 
	 * @return a list, never <code>null</code>
	 */
	public Collection<? extends Semantic> getSemantics() {
		if (this.semantics==null) return Collections.emptyList();
		return Collections.unmodifiableCollection(this.semantics);
	}

}
