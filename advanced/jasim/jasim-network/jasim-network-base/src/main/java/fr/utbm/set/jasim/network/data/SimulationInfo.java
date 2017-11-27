/*
 * $Id$
 * 
 * Copyright (c) 2008-09, Multiagent Team - Systems and Transportation Laboratory (SeT)
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
package fr.utbm.set.jasim.network.data;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import fr.utbm.set.math.AngularUnit;
import fr.utbm.set.math.SpaceUnit;
import fr.utbm.set.math.SpeedUnit;

/**
 * This class describes several parameters of a simulation.
 * 
 * @author $Author: sgalland$
 * @author $Author: ngaud$
 * @author $Author: rzeo$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class SimulationInfo implements Serializable {
	
	private static final long serialVersionUID = 743341973750858028L;
	
	private final UUID simulationId;
	private final String simulationName;
	private final Date simulationDate;
	private final String simulationAuthors;
	private final String simulationVersion;
	private final String simulationDescription;
	private final TimeUnit timeUnit;
	private final SpaceUnit spaceUnit;
	private final SpeedUnit speedUnit;
	private final AngularUnit angularUnit;
	private final double ux, uy, uz;
	private final double vx, vy, vz;
	private final double lx, ly, lz;
	private final float spaceDimension;
	
	/**
	 * @param id is the identifier of the simulation scenario.
	 * @param name is the name of the simulation.
	 * @param date is the date of the simulation scenario.
	 * @param authors are the authors of the simulation scneario.
	 * @param version is the version of the simulation scenario.
	 * @param description is the description of the simulation scenario.
	 * @param timeUnit is the time unit used by the simulator.
	 * @param spaceUnit is the space unit used by the simulator.
	 * @param speedUnit is the speed unit used by the simulator.
	 * @param angularUnit is the rotation speed unit used by the simulator.
	 * @param viewX is the x-component of the view's direction unit vector. 
	 * @param viewY is the y-component of the view's direction unit vector. 
	 * @param viewZ is the z-component of the view's direction unit vector. 
	 * @param upX is the x-component of the up's direction unit vector. 
	 * @param upY is the y-component of the up's direction unit vector. 
	 * @param upZ is the z-component of the up's direction unit vector. 
	 * @param leftX is the x-component of the left's direction unit vector. 
	 * @param leftY is the y-component of the left's direction unit vector. 
	 * @param leftZ is the z-component of the left's direction unit vector.
	 * @param spaceDimension is the dimension assumed by the environment simulator. 
	 */
	public SimulationInfo(UUID id, String name, Date date, String authors, String version, String description, TimeUnit timeUnit, SpaceUnit spaceUnit, SpeedUnit speedUnit, AngularUnit angularUnit,
			double viewX, double viewY, double viewZ,
			double upX, double upY, double upZ,
			double leftX, double leftY, double leftZ,
			float spaceDimension) {
		this.vx = viewX;
		this.vy = viewY;
		this.vz = viewZ;
		this.ux = upX;
		this.uy = upY;
		this.uz = upZ;
		this.lx = leftX;
		this.ly = leftY;
		this.lz = leftZ;
		this.simulationId = id;
		this.simulationName = name;
		this.simulationDate = date;
		this.simulationAuthors = authors;
		this.simulationVersion = version;
		this.simulationDescription = description;
		this.timeUnit = timeUnit;
		this.spaceUnit = spaceUnit;
		this.speedUnit = speedUnit;
		this.angularUnit = angularUnit;
		this.spaceDimension = spaceDimension;
	}
	
	/** Replies the dimension of the space assumed by the
	 * environment simulator.
	 * 
	 * @return one of {@code 1f}, {@code 1.5f}, {@code 2f},
	 * {@code 2.5f} or {@code 3f}.
	 */
	public float getSpaceDimension() {
		return this.spaceDimension;
	}
	
	/** Replies the simulation scenario identifier.
	 * 
	 * @return the simulation scenario identifier.
	 */
	public UUID getId() {
		return this.simulationId;
	}
	
	/** Replies the simulation scenario name.
	 * 
	 * @return the simulation scenario name.
	 */
	public String getName() {
		return this.simulationName;
	}

	/** Replies the simulation scenario date.
	 * 
	 * @return the simulation scenario date.
	 */
	public Date getDate() {
		return this.simulationDate;
	}

	/** Replies the simulation scenario authors.
	 * 
	 * @return the simulation scenario auhors.
	 */
	public String getAuthors() {
		return this.simulationAuthors;
	}

	/** Replies the simulation scenario version.
	 * 
	 * @return the simulation scenario version.
	 */
	public String getVersion() {
		return this.simulationVersion;
	}

	/** Replies the simulation scenario description.
	 * 
	 * @return the simulation scenario description.
	 */
	public String getDescription() {
		return this.simulationDescription;
	}

	/** Replies the time unit used by the simulator.
	 * 
	 * @return the time unit used by the simulator.
	 */
	public TimeUnit getTimeUnit() {
		return this.timeUnit;
	}

	/** Replies the space unit used by the simulator.
	 * 
	 * @return the space unit used by the simulator.
	 */
	public SpaceUnit getSpaceUnit() {
		return this.spaceUnit;
	}

	/** Replies the speed unit used by the simulator.
	 * 
	 * @return the speed unit used by the simulator.
	 */
	public SpeedUnit getSpeedUnit() {
		return this.speedUnit;
	}

	/** Replies the rotation speed unit used by the simulator.
	 * 
	 * @return the rotation speed unit used by the simulator.
	 */
	public AngularUnit getRotationSpeedUnit() {
		return this.angularUnit;
	}
	
	/** Replies the x-component of the view's direction unit vector.
	 * @return the x-component of the view vector. 
	 */
	public double getViewX() {
		return this.vx;
	}

	/** Replies the y-component of the view's direction unit vector.
	 * @return the y-component of the view vector. 
	 */
	public double getViewY() {
		return this.vy;
	}

	/** Replies the z-component of the view's direction unit vector.
	 * @return the z-component of the view vector. 
	 */
	public double getViewZ() {
		return this.vz;
	}

	/** Replies the x-component of the up's direction unit vector.
	 * @return the x-component of the up vector. 
	 */
	public double getUpX() {
		return this.ux;
	}

	/** Replies the y-component of the up's direction unit vector.
	 * @return the y-component of the up vector. 
	 */
	public double getUpY() {
		return this.uy;
	}

	/** Replies the z-component of the up's direction unit vector.
	 * @return the z-component of the up vector. 
	 */
	public double getUpZ() {
		return this.uz;
	}

	/** Replies the x-component of the left's direction unit vector.
	 * @return the x-component of the left vector. 
	 */
	public double getLeftX() {
		return this.lx;
	}

	/** Replies the y-component of the left's direction unit vector.
	 * @return the y-component of the left vector. 
	 */
	public double getLeftY() {
		return this.ly;
	}

	/** Replies the z-component of the left's direction unit vector.
	 * @return the z-component of the left vector. 
	 */
	public double getLeftZ() {
		return this.lz;
	}

}