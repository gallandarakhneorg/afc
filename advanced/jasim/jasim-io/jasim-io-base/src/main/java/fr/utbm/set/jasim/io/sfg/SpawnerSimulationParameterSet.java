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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import javax.vecmath.Vector2d;

import fr.utbm.set.geom.GeometryUtil;
import fr.utbm.set.geom.object.Direction1D;
import fr.utbm.set.jasim.agent.SituatedAgent;
import fr.utbm.set.jasim.environment.interfaces.body.AgentBody;
import fr.utbm.set.jasim.environment.interfaces.body.influences.Influence;
import fr.utbm.set.jasim.environment.interfaces.body.perceptions.Perception;

/**
 * Set of parameters to initialize a simulation.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class SpawnerSimulationParameterSet extends AbstractParameter {

	private final UUID id;	
	private String name;
	private PositionParameter position;
	private double endDate;
	private double startDate;
	private double startAngle = 0.;
	private double endAngle = 2.*Math.PI;
	private Vector2d dimension = new Vector2d();
	private SpawnerParameterType type;
	private Direction1D direction;
	
	private final List<EntitySimulationParameterSet> entities = new ArrayList<EntitySimulationParameterSet>();
		
	
	/**
	 * @param dimension must be one of <code>1</code>, <code>1.5</code>, <code>2</code>, <code>2.5</code>, 
	 * or <code>3</code>.
	 * @param id
	 */
	SpawnerSimulationParameterSet(float dimension, UUID id) {
		super(dimension);
		this.id = id;
	}
	
	/** Replies the identifier of this spawner.
	 * 
	 * @return the name of this spawner.
	 */
	public UUID getIdentifier() {
		return this.id;
	}
	
	/** Replies the name of the spawner.
	 * 
	 * @return the name of the spawner or <code>null</code>
	 */
	public String getName() {
		return this.name;
	}

	/** Set the name of the spawner.
	 * 
	 * @param name is the name of the spawner or <code>null</code> to unset.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the position
	 */
	public PositionParameter getPosition() {
		return this.position;
	}

	/**
	 * @param position is the position to set
	 */
	public void setPosition(PositionParameter position) {
		this.position = position;
	}

	/**
	 * @return the dimension of this element.
	 */
	public Vector2d getDimension2D() {
		return this.dimension;
	}

	/**
	 * @return the dimension of this element.
	 */
	public Vector2d getDimension1D5() {
		return this.dimension;
	}

	/**
	 * @return the dimension of this element.
	 */
	public double getDimension1D() {
		return this.dimension.x;
	}

	/**
	 * @param dimension is the dimension to set
	 */
	public void setDimension(Vector2d dimension) {
		this.dimension = dimension;
	}

	/**
	 * @param width
	 * @param height
	 */
	public void setDimension(double width, double height) {
		this.dimension = new Vector2d(width, height);
	}

	/**
	 * @param width
	 */
	public void setDimension(double width) {
		this.dimension = new Vector2d(width, 0);
	}

	/**
	 * @param type is the type of the spawning location
	 */
	public void setType(SpawnerParameterType type) {
		this.type = type;
	}

	/**
	 * @return the type of the spawning location
	 */
	public SpawnerParameterType getType() {
		return this.type;
	}

	/**
	 * @return the end angle
	 */
	public double getEndAngle() {
		return this.endAngle;
	}

	/**
	 * @return the start angle
	 */
	public double getStartAngle() {
		return this.startAngle;
	}

	/**
	 * @param startAngle is the start angle of the generation area.
	 * @param endAngle is the end angle of the generation area.
	 */
	public void setAngles(double startAngle, double endAngle) {
		this.startAngle = GeometryUtil.clampRadian0To2PI(startAngle);
		this.endAngle = GeometryUtil.clampRadian0To2PI(endAngle);
		while (this.endAngle<this.startAngle) {
			this.endAngle += 2. * Math.PI;
		}
	}

	/**
	 * @return the end date
	 */
	public double getEndDate() {
		return this.endDate;
	}

	/**
	 * @param startDate
	 * @param endDate
	 */
	public void setLifeTime(double startDate, double endDate) {
		this.startDate = (startDate<endDate) ? startDate : endDate;
		this.endDate = (startDate<endDate) ? endDate : startDate;
	}

	/**
	 * @return the start date
	 */
	public double getStartDate() {
		return this.startDate;
	}
	
	/** Create an entity definition and reply the corresponding object.
	 *
	 * @param <ADR> is the type of address supported by the entity.
	 * @param <INF> is the type of influences supported by the entity.
	 * @param <PER> is the type of perceptions supported by the entity.
	 * @param agent is the type of the entity.
	 * @param body is the type of the body.
	 * @return the entity.
	 */
	public <ADR, INF extends Influence, PER extends Perception> EntitySimulationParameterSet createEntity(Class<? extends SituatedAgent<ADR,INF,PER>> agent, Class<? extends AgentBody<INF,PER>> body) {
		EntitySimulationParameterSet entity = new EntitySimulationParameterSet(getEnvironmentDimension(), agent, body);
		this.entities.add(entity);
		return entity;
	}
	
	/** Replies the count of entities.
	 * 
	 * @return the count of entities.
	 */
	public int getEntityCount() {
		return this.entities.size();
	}
	
	/** Replies the entity at the given index.
	 * 
	 * @param index 
	 * @return the entity at the given index.
	 */
	public EntitySimulationParameterSet getEntityAt(int index) {
		return this.entities.get(index);
	}

	/** Replies all the entities
	 * 
	 * @return an unmodifiable collection.
	 */
	public List<EntitySimulationParameterSet> getEntities() {
		return Collections.unmodifiableList(this.entities);
	}
	
	/** Set the 1D direction of spawning.
	 * 
	 * @param direction
	 */
	public void setDirection1D(Direction1D direction) {
		this.direction = direction;
	}

	/** Replies the 1D/1.5D direction of spawning.
	 * 
	 * @return the 1D/1.5D direction
	 */
	public Direction1D getDirection1D() {
		return this.direction;
	}

	/**
	 * Set of parameters to initialize a time manager.
	 * 
	 * @author $Author: sgalland$
	 * @version $Name$ $Revision$ $Date$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	public enum SpawnerParameterType {

		/** Spawn on a point
		 */
		POINT,
		
		/** Spawn in an area.
		 */
		AREA;
		
	}

}
