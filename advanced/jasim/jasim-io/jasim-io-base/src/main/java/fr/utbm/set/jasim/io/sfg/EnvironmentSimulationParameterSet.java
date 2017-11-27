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

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.vecmath.Point2d;
import javax.vecmath.Point3d;
import javax.vecmath.Vector2d;

import fr.utbm.set.jasim.agent.SituatedAgent;
import fr.utbm.set.jasim.environment.interfaces.body.AgentBody;
import fr.utbm.set.jasim.environment.model.ground.AlignedIndoorGround;
import fr.utbm.set.jasim.environment.model.ground.Ground;
import fr.utbm.set.jasim.environment.model.ground.OrientedIndoorGround;
import fr.utbm.set.jasim.environment.model.ground.RepulsionHeightmapGround;
import fr.utbm.set.jasim.environment.model.ground.SimpleHeightmapGround;
import fr.utbm.set.jasim.environment.model.perceptions.PerceptionGeneratorType;
import fr.utbm.set.jasim.environment.semantics.Semantic;
import fr.utbm.set.math.MathUtil;

/**
 * Set of parameters to initialize a simulation.
 * 
 * @author $Author: sgalland$
 * @version $Name$ $Revision$ $Date$
 * @mavengroupid $GroupId$
 * @mavenartifactid $ArtifactId$
 */
public class EnvironmentSimulationParameterSet extends AbstractParameter {

	private final UUID placeIdentifier;
	private String placeName;
	private UUID groundIdentifier;
	private GroundType groundType;
	private URL groundPrebuiltResource;
	private URL groundHeightResource;
	private URL groundRoadShapeResource;
	private URL groundRoadAttributeResource;
	private double minX, minY, minZ;
	private double maxX, maxY, maxZ;
	private double centerX, centerY;
	private double Rx, Ry;
	private double Sx, Sy;
	private double groundZero = Double.NaN;
	private byte rawGroundZero = -128;
	private URL groundSemantic;
	private PerceptionGeneratorType perceptionGeneratorType = PerceptionGeneratorType.getSystemDefault(); 
	
	private Class<?> staticType;
	private URL staticPrebuiltResource;
	
	private Class<?> dynamicType;
	private URL dynamicPrebuiltResource;
	
	private final Map<String, PositionParameter> probes = new HashMap<String, PositionParameter>();
	
	private final Map<Class<? extends AgentBody<?,?>>, Class<? extends SituatedAgent<?,?,?>>> agentMapping = new HashMap<Class<? extends AgentBody<?,?>>, Class<? extends SituatedAgent<?,?,?>>>();
	
	private final Map<Class<? extends SituatedAgent<?,?,?>>, Collection<Class<? extends Semantic>>> agentSemantics = new HashMap<Class<? extends SituatedAgent<?,?,?>>, Collection<Class<? extends Semantic>>>();
	
	private final List<PortalSimulationParameterSet> portals = new ArrayList<PortalSimulationParameterSet>();

	/**
	 * @param dimension must be one of <code>1</code>, <code>1.5</code>, <code>2</code>, <code>2.5</code>, 
	 * or <code>3</code>.
	 * @param placeId is the identifier of the place that is corresponding to this parameter set.
	 */
	EnvironmentSimulationParameterSet(float dimension, UUID placeId) {
		super(dimension);
		this.placeIdentifier = placeId;
	}
	
	/** Replies the name of the place.
	 * 
	 * @return the name of the place or <code>null</code>.
	 */
	public String getPlaceName() {
		return this.placeName;
	}
	
	/** Set the name of the place.
	 * 
	 * @param name is the name of the place or <code>null</code> to unset.
	 */
	public void setPlaceName(String name) {
		this.placeName = name;
	}

	/** Replies the type of perception generator of the place.
	 * 
	 * @return the type of perception generator of the place.
	 */
	public PerceptionGeneratorType getPerceptionGeneratorType() {
		return this.perceptionGeneratorType;
	}

	/** Set the type of perception generator of the place.
	 * 
	 * @param type is the type of perception generator of the place.
	 */
	public void setPerceptionGeneratorType(PerceptionGeneratorType type) {
		this.perceptionGeneratorType = type;
	}

	/**
	 * @param portal is the new portal.
	 */
	public void addPortal(PortalSimulationParameterSet portal) {
		this.portals.add(portal);
	}
	
	/** Replies the portals in this environment.
	 * @return the portals in this environment.
	 */
	public Iterable<PortalSimulationParameterSet> getPortals() {
		return this.portals;
	}

	/** Replies the identifier of the place that is corresponding to this parameter set.
	 * 
	 * @return the identifier of the place that is corresponding to this parameter set.
	 */
	public UUID getPlaceIdentifier() {
		return this.placeIdentifier;
	}
	
	/** Replies the identifier of the ground object.
	 * 
	 * @return the identifier or <code>null</code>
	 */
	public UUID getGroundIdentifier() {
		return this.groundIdentifier;
	}

	/** Set the identifier of the ground object.
	 * 
	 * @param id is the identifier or <code>null</code>
	 */
	public void setGroundIdentifier(UUID id) {
		this.groundIdentifier = id;
	}

	/** Replies the type of the ground object.
	 * 
	 * @return the type or <code>null</code>
	 */
	public GroundType getGroundType() {
		return this.groundType;
	}
	
	/** Set the type of the ground object.
	 * 
	 * @param type is the ground type.
	 */
	public void setGroundType(GroundType type) {
		this.groundType = type;
	}

	/** Replies the minimal point of the ground.
	 * 
	 * @return the minimal point
	 */
	public Point3d getGroundMinPoint() {
		return new Point3d(this.minX, this.minY, this.minZ);
	}
	
	/** Replies the maximal point of the ground.
	 * 
	 * @return the maximal point
	 */
	public Point3d getGroundMaxPoint() {
		return new Point3d(this.maxX, this.maxY, this.maxZ);
	}

	/** Replies the ground zero height in meters if set.
	 * 
	 * @return ground zero height
	 * @see #isGroundZeroColor()
	 * @see #getGroundZeroColor()
	 */
	public double getGroundZeroMeters() {
		if (Double.isNaN(this.groundZero)) throw new IllegalArgumentException();
		return this.groundZero;
	}

	/** Replies if the ground zero haight is expressed with a raw color or
	 * with a height in meters.
	 * <p>
	 * If the ground zero height is a color, the function {@link #getGroundZeroColor()}
	 * should be called to obtain the ground azero height, otherwise the
	 * function {@link #getGroundZeroMeters()} should be called. 
	 * 
	 * @return <code>true</code> if the ground zero height is a color, otherwise
	 * <code>false</code>
	 * @see #getGroundZeroColor()
	 * @see #getGroundZeroMeters()
	 */
	public boolean isGroundZeroColor() {
		return Double.isNaN(this.groundZero);
	}

	/** Replies the ground zero height as a color if set.
	 * 
	 * @return ground zero height
	 * @see #isGroundZeroColor()
	 * @see #getGroundZeroMeters()
	 */
	public byte getGroundZeroColor() {
		if (!Double.isNaN(this.groundZero)) throw new IllegalArgumentException();
		return this.rawGroundZero;
	}

	/** Set the minimal point of the ground.
	 * 
	 * @param x
	 * @param y
	 * @param z
	 */
	public void setGroundMinPoint(double x, double y, double z) {
		this.minX = x;
		this.minY = y;
		this.minZ = z;
	}

	/** Set the center point of the ground.
	 * 
	 * @param x
	 * @param y
	 * @param z
	 */
	public void setGroundCenterPoint(double x, double y, double z) {
		this.centerX = x;
		this.centerY = y;
		this.minZ = this.maxZ = z;
	}

	/** Replies the center point of the ground.
	 * 
	 * @return the center point
	 */
	public Point2d getGroundCenterPoint() {
		return new Point2d(this.centerX, this.centerY);
	}

	/** Set the R and S vectors of an oriented ground.
	 * 
	 * @param Rx
	 * @param Ry
	 * @param Sx
	 * @param Sy
	 */
	public void setGroundAxisVectors(double Rx, double Ry, double Sx, double Sy) {
		this.Rx = Rx;
		this.Ry = Ry;
		this.Sx = Sx;
		this.Sy = Sy;
		
		this.minX = MathUtil.min(
				this.centerX-Rx,
				this.centerX+Rx,
				this.centerX-Sx,
				this.centerX+Sx);
		this.maxX = MathUtil.max(
				this.centerX-Rx,
				this.centerX+Rx,
				this.centerX-Sx,
				this.centerX+Sx);
		this.minY = MathUtil.min(
				this.centerY-Ry,
				this.centerY+Ry,
				this.centerY-Sy,
				this.centerY+Sy);
		this.maxY = MathUtil.max(
				this.centerY-Ry,
				this.centerY+Ry,
				this.centerY-Sy,
				this.centerY+Sy);
	}
	
	/** Replies the R axis vector for an oriented ground.
	 * 
	 * @return the R axis vector for an oriented ground.
	 */
	public Vector2d getRAxis() {
		return new Vector2d(this.Rx, this.Ry);
	}

	/** Replies the S axis vector for an oriented ground.
	 * 
	 * @return the S axis vector for an oriented ground.
	 */
	public Vector2d getSAxis() {
		return new Vector2d(this.Sx, this.Sy);
	}

	/** Set the maximal point of the ground.
	 * 
	 * @param x
	 * @param y
	 * @param z
	 */
	public void setGroundMaxPoint(double x, double y, double z) {
		this.maxX = x;
		this.maxY = y;
		this.maxZ = z;
	}

	/** Set the ground zero height.
	 * 
	 * @param z is the ground zero height
	 */
	public void setGroundZero(double z) {
		this.groundZero = z;
	}

	/** Set the ground zero height.
	 * 
	 * @param color is the ground zero height represented by a color
	 */
	public void setRawGroundZero(byte color) {
		this.rawGroundZero = color;
		this.groundZero = Double.NaN;
	}

	/** Replies the resource for ground's prebuilt structure.
	 * 
	 * @return the resource url or <code>null</code>
	 */
	public URL getGroundPrebuiltResource() {
		return this.groundPrebuiltResource;
	}
	
	/** Set the resource for ground's prebuilt structure.
	 * 
	 * @param prebuiltResource is the resource url or <code>null</code>
	 */
	public void setGroundPrebuiltResource(URL prebuiltResource) {
		this.groundPrebuiltResource = prebuiltResource;
	}

	/** Replies the resource for ground's entity list.
	 * 
	 * @return the resource url or <code>null</code>
	 */
	public URL getGroundHeightResource() {
		return this.groundHeightResource;
	}

	/** Set the resource for ground's height list.
	 * 
	 * @param heights is the resource url or <code>null</code>
	 */
	public void setGroundHeightResource(URL heights) {
		this.groundHeightResource = heights;
	}

	/** Replies the resource for ground's road network shapes.
	 * 
	 * @return the resource url or <code>null</code>
	 */
	public URL getGroundRoadShapeResource() {
		return this.groundRoadShapeResource;
	}

	/** Set the resource for ground's road network shapes.
	 * 
	 * @param shapes is the resource url or <code>null</code>
	 */
	public void setGroundRoadShapeResource(URL shapes) {
		this.groundRoadShapeResource = shapes;
	}

	/** Replies the resource for ground's road network attribites.
	 * 
	 * @return the resource url or <code>null</code>
	 */
	public URL getGroundRoadAttributeResource() {
		return this.groundRoadAttributeResource;
	}

	/** Set the resource for ground's road network attributes.
	 * 
	 * @param attributes is the resource url or <code>null</code>
	 */
	public void setGroundRoadAttributeResource(URL attributes) {
		this.groundRoadAttributeResource = attributes;
	}

	/** Replies the resource for ground's semantic.
	 * 
	 * @return the resource url or <code>null</code>
	 */
	public URL getGroundSemanticResource() {
		return this.groundSemantic;
	}

	/** Set the resource for ground's semantic.
	 * 
	 * @param resource is the resource url or <code>null</code>
	 */
	public void setGroundSemanticResource(URL resource) {
		this.groundSemantic = resource;
	}

	/** Replies the type of the static environment object.
	 * 
	 * @return the type or <code>null</code>
	 */
	public Class<?> getStaticEnvironmentType() {
		return this.staticType;
	}
	
	/** Set the type of the static environment object.
	 * 
	 * @param type is the static environment type.
	 */
	public void setStaticEnvironmentType(Class<?> type) {
		this.staticType = type;
	}

	/** Replies the resource for static environment's prebuilt structure.
	 * 
	 * @return the resource url or <code>null</code>
	 */
	public URL getStaticEnvironmentPrebuiltResource() {
		return this.staticPrebuiltResource;
	}
	
	/** Set the resource for the static environment object.
	 * 
	 * @param prebuiltStructure is the URL where a prebuilt structure of the environment is available, or <code>null</code>
	 */
	public void setStaticEnvironmentResources(URL prebuiltStructure) {
		this.staticPrebuiltResource = prebuiltStructure;
	}

	/** Replies the type of the dynamic environment object.
	 * 
	 * @return the type or <code>null</code>
	 */
	public Class<?> getDynamicEnvironmentType() {
		return this.dynamicType;
	}
	
	/** Set the type of the dynamic environment object.
	 * 
	 * @param type is the dynamic environment type.
	 */
	public void setDynamicEnvironmentType(Class<?> type) {
		this.dynamicType = type;
	}

	/** Replies the resource for dynamic environment's prebuilt structure.
	 * 
	 * @return the resource url or <code>null</code>
	 */
	public URL getDynamicEnvironmentPrebuiltResource() {
		return this.dynamicPrebuiltResource;
	}
	
	/** Set the resource for the dynamic environment object.
	 * 
	 * @param prebuiltStructure is the URL where a prebuilt structure of the environment is available, or <code>null</code>
	 */
	public void setDynamicEnvironmentResources(URL prebuiltStructure) {
		this.dynamicPrebuiltResource = prebuiltStructure;
	}
	
	/** Add an environment probe.
	 * 
	 * @param name
	 * @param position is the position of the probe.
	 */
	public void addProbe(String name, PositionParameter position) {
		this.probes.put(name, position);
	}

	/** Remove an environment probe.
	 * 
	 * @param name
	 */
	public void removeProbe(String name) {
		this.probes.remove(name);
	}

	/** Get the position of a probe.
	 * 
	 * @param name
	 * @return the probe position or <code>null</code> if none.
	 */
	public PositionParameter getProbePosition(String name) {
		return this.probes.get(name);
	}

	/** Replies the count of probes.
	 * 
	 * @return the count of probes.
	 */
	public int getProbeCount() {
		return this.probes.size();
	}

	/** Replies all the probes
	 * 
	 * @return an unmodifiable map.
	 */
	public Map<String,PositionParameter> getProbes() {
		return Collections.unmodifiableMap(this.probes);
	}
	
	/** Add an agent creation mapping in dynamic environment.
	 * <p>
	 * This mapping is used to specify which agent class to instance for
	 * a body.
	 * 
	 * @param bodyType is the type of the body to map to an agent class 
	 * @param agentType is the type of the agent to instance.
	 */
	public void addDynamicEnvironmentAgentMapping(Class<? extends AgentBody<?,?>> bodyType, Class<? extends SituatedAgent<?,?,?>> agentType) {
		assert(bodyType!=null);
		this.agentMapping.put(bodyType, agentType);
	}

	/** Add an agent semantic in dynamic environment.
	 * 
	 * @param agentType is the type of the agent to instance.
	 * @param semantic is the new semantic to attach to the agent type.
	 */
	public void addDynamicEnvironmentAgentSemantic(Class<? extends SituatedAgent<?,?,?>> agentType, Class<? extends Semantic> semantic) {
		assert(semantic!=null);
		Collection<Class<? extends Semantic>> list = this.agentSemantics.get(agentType);
		if (list==null) {
			list = new ArrayList<Class<? extends Semantic>>();
			this.agentSemantics.put(agentType, list);
		}
		list.add(semantic);
	}

	/** Replies the agent semantics in dynamic environment.
	 * 
	 * @param agentType is the type of the agent to instance.
	 * @return a collection of semantics, never <code>null</code>
	 */
	public Collection<Class<? extends Semantic>> getDynamicEnvironmentAgentSemantics(Class<? extends SituatedAgent<?,?,?>> agentType) {
		Collection<Class<? extends Semantic>> list = this.agentSemantics.get(agentType);
		if (list==null) return Collections.emptyList();
		return list;
	}

	/** Replies the agent semantics in dynamic environment.
	 * 
	 * @return a collection of semantics, never <code>null</code>
	 */
	public Map<Class<? extends SituatedAgent<?,?,?>>, Collection<Class<? extends Semantic>>> getDynamicEnvironmentAgentSemantics() {
		return Collections.unmodifiableMap(this.agentSemantics);
	}

	/** Add an agent creation mapping in dynamic environment.
	 * <p>
	 * This mapping is used to specify which agent class to instance for
	 * a body.
	 * 
	 * @param agentType is the type of the agent to instance.
	 */
	public void addDefaultDynamicEnvironmentAgentMapping(Class<? extends SituatedAgent<?,?,?>> agentType) {
		this.agentMapping.put(null, agentType);
	}

	/** Replies a agent creation mapping in dynamic environment.
	 * <p>
	 * This mapping is used to specify which agent class to instance for
	 * a body.
	 * 
	 * @param bodyType is the type of the body to map to an agent class 
	 * @return the type of the agent to instance.
	 */
	public Class<? extends SituatedAgent<?,?,?>> getDynamicEnvironmentAgentMapping(Class<?> bodyType) {
		assert(bodyType!=null);
		return this.agentMapping.get(bodyType);
	}

	/** Replies a agent creation mapping in dynamic environment.
	 * <p>
	 * This mapping is used to specify which agent class to instance for
	 * a body.
	 * 
	 * @return the types of the agent to instances.
	 */
	public Map<Class<? extends AgentBody<?,?>>, Class<? extends SituatedAgent<?,?,?>>> getDynamicEnvironmentAgentMapping() {
		return Collections.unmodifiableMap(this.agentMapping);
	}

	/** Replies the default agent creation mapping in dynamic environment.
	 * <p>
	 * This mapping is used to specify which agent class to instance for
	 * a body.
	 * 
	 * @return the type of the agent to instance.
	 */
	public Class<? extends SituatedAgent<?,?,?>> getDefaultDynamicEnvironmentAgentMapping() {
		return this.agentMapping.get(null);
	}
	
	/**
	 * Type of a heightmap ground.
	 * 
	 * @author $Author: sgalland$
	 * @version $Name$ $Revision$ $Date$
	 * @mavengroupid $GroupId$
	 * @mavenartifactid $ArtifactId$
	 */
	public enum GroundType {

		/** No ground.
		 */
		NONE(null),

		/** The ground has a constant height.
		 */
		ALIGNED_CONSTANT(AlignedIndoorGround.class),

		/** The ground has a constant height.
		 * @since 2.0
		 */
		ORIENTED_CONSTANT(OrientedIndoorGround.class),

		/** The ground is an heightmap with only height values.
		 */
		STANDARD_HEIGHTMAP(SimpleHeightmapGround.class),
		
		/** The ground is an heightmap with repulsion vectors.
		 */
		REPULSION_HEIGHTMAP(RepulsionHeightmapGround.class);

		private final Class<? extends Ground> defaultClass;
		
		private GroundType(Class<? extends Ground> type) {
			this.defaultClass = type;
		}
		
		/** Replies the java type associated to this type of heightmap ground.
		 * 
		 * @return the class that permits to instance a ground of this type.
		 */
		public Class<? extends Ground> toJavaClass() {
			return this.defaultClass;
		}
		
	}

}
